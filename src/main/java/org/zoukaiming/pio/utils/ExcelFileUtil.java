package org.zoukaiming.pio.utils;

import java.beans.PropertyDescriptor;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.util.Assert;
import org.zoukaiming.pio.utils.common.Exclude;
import org.zoukaiming.pio.utils.common.Include;
import org.zoukaiming.pio.utils.read.ExcelFileReadContext;
import org.zoukaiming.pio.utils.read.ExcelFileReadRowProcessor;
import org.zoukaiming.pio.utils.read.ExcelFileReadSheetProcessor;

import com.alibaba.fastjson.util.TypeUtils;

/**
 * @author zoukaiming
 */
public class ExcelFileUtil {

    public static void readInputStreamToObject(InputStream workbookInputStream,
                                               ExcelFileReadSheetProcessor<?>... sheetProcessors) {
        Assert.isTrue(workbookInputStream != null, "workbookInputStream can't be null");
        Assert.isTrue(sheetProcessors != null && sheetProcessors.length != 0, "sheetProcessor can't be null");
        try {
            Workbook workbook = WorkbookFactory.create(workbookInputStream);
            for (ExcelFileReadSheetProcessor<?> sheetProcessor : sheetProcessors) {
                try {
                    Class clazz = sheetProcessor.getTargetClass();
                    Integer sheetIndex = sheetProcessor.getSheetIndex();
                    String sheetName = sheetProcessor.getSheetName();
                    Sheet sheet = null;
                    if (sheetIndex != null) {
                        sheet = workbook.getSheetAt(sheetIndex);
                    }
                    if (sheet != null) {
                        if (sheetProcessor.getSheetName() != null && !sheetName.equals(sheet.getSheetName())) {
                            throw new RuntimeException("Sheet Not Found Exception. for sheet index" + sheetIndex
                                                       + " and sheet name:" + sheetName);
                        }
                    } else {
                        sheet = workbook.getSheet(sheetName);
                    }
                    if (sheet == null) {
                        throw new RuntimeException("Sheet Not Found Exception. for sheet name:" + sheetName);
                    }

                    Integer pageSize = sheetProcessor.getPageSize();
                    int startRow = sheetProcessor.getStartRow();
                    ExcelFileReadContext context = new ExcelFileReadContext();
                    context.setCurSheetIndex(sheetIndex);
                    context.setCurSheetName(sheetName);
                    context.setLastRowIndex(Long.valueOf(sheet.getLastRowNum()));

                    if (sheetProcessor.getPageSize() != null) {
                        int total = sheet.getLastRowNum();
                        int count = (total + pageSize - 1) / pageSize;
                        for (int i = 0; i < count; i++) {
                            @SuppressWarnings("unchecked")
                            List list = readInputStreamToObject(context, sheet, startRow + pageSize * i, pageSize,
                                                                sheetProcessor.getFieldMapping(), clazz,
                                                                sheetProcessor.getInclude(),
                                                                sheetProcessor.getExclude(),
                                                                sheetProcessor.getRowProcessor());
                            sheetProcessor.process(context, list);
                        }
                    } else {
                        @SuppressWarnings("unchecked")
                        List list = readInputStreamToObject(context, sheet, startRow, pageSize,
                                                            sheetProcessor.getFieldMapping(), clazz,
                                                            sheetProcessor.getInclude(), sheetProcessor.getExclude(),
                                                            sheetProcessor.getRowProcessor());
                        sheetProcessor.process(context, list);
                    }
                } catch (RuntimeException e) {
                    sheetProcessor.onExcepton(e);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> List<T> readInputStreamToObject(ExcelFileReadContext<T> context, Sheet sheet, int startRow,
                                                       Integer pageSize, ExcelFileFieldMapping fieldMapping,
                                                       Class<T> targetClass, Include<String> include,
                                                       Exclude<String> exclude, ExcelFileReadRowProcessor<T> processor) {
        Assert.isTrue(sheet != null, "sheet can't be null");
        Assert.isTrue(startRow >= 0, "startRow must greater than or equal to 0");
        Assert.isTrue(pageSize == null || pageSize >= 1, "pageSize == null || pageSize >= 1");
        Assert.isTrue(fieldMapping != null, "fieldMapping can't be null");
        Assert.isTrue(targetClass != null, "clazz can't be null");

        List<T> list = new ArrayList<T>();
        if (sheet.getPhysicalNumberOfRows() == 0) {
            return list;
        }
        //
        for (int i = startRow; i <= sheet.getLastRowNum(); i++) {
            // //////proc row
            context.setCurRowIndex((Long.valueOf(i)));
            context.setCurRow(null);
            context.setCurColIndex(null);
            context.setCurColStrIndex(null);
            context.setCurRowLastColIndex(null);
            if (pageSize != null && (i - startRow >= pageSize)) {
                break;
            }
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            T t = procRow(context, row, fieldMapping, targetClass, include, exclude, processor);
            if (t != null) {// ignore empty row
                if (processor != null) {
                    t = processor.process(context, row, t);
                }
                if (t != null) {
                    list.add(t);
                }
            }
        }
        return list;
    }

    private static <T> T procRow(ExcelFileReadContext<T> context, Row row, ExcelFileFieldMapping fieldMapping,
                                 Class<T> targetClass, Include<String> include, Exclude<String> exclude,
                                 ExcelFileReadRowProcessor<T> processor) {
        boolean isEmptyRow = true;
        // Map<String, Object> cache = new HashMap<String, Object>();
        short minColIx = row.getFirstCellNum();
        short maxColIx = row.getLastCellNum();// note ,this return value is 1-based.
        short lastColIndex = (short) (maxColIx - 1);
        try {
            context.setCurRow(targetClass.newInstance());
        } catch (Exception e1) {
            throw new RuntimeException("error", e1);
        }

        for (Entry<Integer, Map<String, InnerEntry>> fieldMappingEntry : fieldMapping.entrySet()) {
            int curColIndex = fieldMappingEntry.getKey();// excel index;
            context.setCurColIndex(curColIndex);
            context.setCurColStrIndex(convertIntIndexToColCharIndex(curColIndex));

            if (curColIndex > lastColIndex || curColIndex < minColIx) {
                Map<String, InnerEntry> fields = fieldMappingEntry.getValue();
                for (Map.Entry<String, InnerEntry> field : fields.entrySet()) {
                    String fieldName = field.getValue().getFieldName();
                    if (field.getValue().isRequired() || (include != null && include.contains(fieldName))) {
                        ExcelFileException e = new ExcelFileException();
                        e.setRowIndex(row.getRowNum());
                        e.setColIndex(curColIndex);
                        e.setColStrIndex(convertIntIndexToColCharIndex(curColIndex));//
                        e.setSystemErrorCode(ExcelFileException.CODE_OF_CELL_VALUE_REQUIRED);
                        throw e;
                    } else {
                        // TODO SET NULL
                    }
                }
            } else {
                Cell cell = row.getCell((int) curColIndex);
                Map<String, InnerEntry> fields = fieldMappingEntry.getValue();
                for (Map.Entry<String, InnerEntry> fieldEntry : fields.entrySet()) {
                    // ////proc cell
                    context.setCurColIndex(curColIndex);
                    context.setCurColStrIndex(convertIntIndexToColCharIndex(curColIndex));
                    context.setCurRowLastColIndex(Integer.valueOf(row.getLastCellNum()));

                    String fieldName = fieldEntry.getValue().getFieldName();
                    InnerEntry entry = fieldEntry.getValue();
                    if (cell == null) {
                        if (entry.isRequired()) {
                            ExcelFileException e = new ExcelFileException();
                            e.setRowIndex(row.getRowNum());
                            e.setColIndex(curColIndex);
                            e.setColStrIndex(convertIntIndexToColCharIndex(curColIndex));//
                            e.setSystemErrorCode(ExcelFileException.CODE_OF_CELL_VALUE_REQUIRED);
                            throw e;
                        } else {
                            continue;
                        }
                    }
                    if (include != null && !include.contains(fieldName)) {
                        continue;
                    }
                    if (exclude != null && exclude.contains(fieldName)) {
                        continue;
                    }
                    PropertyDescriptor pd = org.springframework.beans.BeanUtils.getPropertyDescriptor(targetClass,
                                                                                                      fieldName);
                    if (pd == null || pd.getWriteMethod() == null) {
                        continue;
                    }
                    int cellType = cell.getCellType();
                    Object convertedValue = null;
                    switch (cellType) {
                        case Cell.CELL_TYPE_BLANK:
                            // null;
                            Object value = null;
                            convertedValue = procValueConvert(context, row, cell, entry, fieldName, value);
                            break;
                        case Cell.CELL_TYPE_BOOLEAN:
                            isEmptyRow = false;
                            boolean bool = cell.getBooleanCellValue();
                            String boolStr = String.valueOf(bool);
                            convertedValue = procValueConvert(context, row, cell, entry, fieldName, boolStr);
                            break;
                        case Cell.CELL_TYPE_ERROR:
                            cell.getErrorCellValue();
                            // ignore
                            continue;
                            // break;
                        case Cell.CELL_TYPE_FORMULA:
                            isEmptyRow = false;
                            String formula = cell.getCellFormula();
                            if (StringUtils.isBlank(formula)) {
                                formula = null;
                            }
                            if (formula != null) {
                                formula = formula.trim();
                            }
                            convertedValue = procValueConvert(context, row, cell, entry, fieldName, formula);
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            isEmptyRow = false;
                            Object inputValue = null;// 单元格值
                            double doubleVal = cell.getNumericCellValue();
                            long longVal = Math.round(cell.getNumericCellValue());
                            if (Double.parseDouble(longVal + ".0") == doubleVal) {
                                inputValue = longVal;
                            } else {
                                inputValue = doubleVal;
                            }
                            // 暂不支持日期/时间
                            // cell.getCellStyle().getDataFormatString();
                            convertedValue = procValueConvert(context, row, cell, entry, fieldName, inputValue);
                            break;
                        case Cell.CELL_TYPE_STRING:
                            isEmptyRow = false;
                            String str = cell.getStringCellValue();
                            if (StringUtils.isBlank(str)) {
                                str = null;
                            }
                            if (str != null) {
                                str = str.trim();
                            }
                            convertedValue = procValueConvert(context, row, cell, entry, fieldName, str);
                            break;
                        default:
                            throw new RuntimeException("unsupport cell type " + cellType);
                    }
                    if (convertedValue != null) {// ignore null
                        try {
                            Class<?> paramType = pd.getWriteMethod().getParameterTypes()[0];
                            if (convertedValue != null && !paramType.isAssignableFrom(convertedValue.getClass())) {
                                convertedValue = TypeUtils.cast(convertedValue, paramType, null);
                            }
                            pd.getWriteMethod().invoke(context.getCurRow(), convertedValue);
                        } catch (Exception e1) {
                            ExcelFileException e = new ExcelFileException(e1);
                            e.setRowIndex(row.getRowNum());
                            e.setColStrIndex(convertIntIndexToColCharIndex(cell.getColumnIndex()));
                            e.setColIndex(cell.getColumnIndex());
                            e.setSystemErrorCode(ExcelFileException.CODE_OF_PROCESS_EXCEPTION);
                            throw e;
                        }
                    }
                }
            }
        }
        if (isEmptyRow) {
            return null;
        } else {
            return context.getCurRow();
        }
    }

    private static Object procValueConvert(ExcelFileReadContext<?> context, Row row, Cell cell, InnerEntry entry,
                                           String fieldName, Object value) {
        Object convertedValue = value;
        if (entry.getValueMapping() != null) {
            ExcelFileCellValueMapping<String, Object> valueMapping = entry.getValueMapping();
            String strValue = TypeUtils.castToString(value);
            convertedValue = valueMapping.get(strValue);
            if (convertedValue == null) {
                if (!valueMapping.containsKey(strValue)) {
                    if (valueMapping.containsKey(ExcelFileCellValueMapping.DEFAULT_KEY)) {
                        Object defalutValue = valueMapping.get(ExcelFileCellValueMapping.DEFAULT_KEY);
                        if (ExcelFileCellValueMapping.DEFAULT_VALUE == defalutValue) {
                            convertedValue = value;
                        } else {
                            convertedValue = defalutValue;
                        }
                    } else if (valueMapping.getDefaultProcessor() != null) {
                        try {
                            convertedValue = valueMapping.getDefaultProcessor().process(context, cell,
                                                                                        new ExcelFileCellValue(value));
                        } catch (RuntimeException re) {
                            if (re instanceof ExcelFileException) {
                                ((ExcelFileException) re).setRowIndex(row.getRowNum());
                                ((ExcelFileException) re).setColStrIndex(convertIntIndexToColCharIndex(cell.getColumnIndex()));
                                ((ExcelFileException) re).setColIndex(cell.getColumnIndex());
                                ((ExcelFileException) re).setSystemErrorCode(ExcelFileException.CODE_OF_PROCESS_EXCEPTION);
                                throw re;
                            } else {
                                ExcelFileException e = new ExcelFileException(re);
                                e.setRowIndex(row.getRowNum());
                                e.setColStrIndex(convertIntIndexToColCharIndex(cell.getColumnIndex()));
                                e.setColIndex(cell.getColumnIndex());
                                e.setSystemErrorCode(ExcelFileException.CODE_OF_PROCESS_EXCEPTION);
                                throw e;
                            }
                        }
                        if (convertedValue != null && convertedValue instanceof ExcelFileCellValue) {
                            convertedValue = value;
                        }
                    } else {
                        ExcelFileException e = new ExcelFileException();
                        e.setRowIndex(row.getRowNum());
                        e.setColStrIndex(convertIntIndexToColCharIndex(cell.getColumnIndex()));
                        e.setColIndex(cell.getColumnIndex());
                        e.setSystemErrorCode(ExcelFileException.CODE_OF_CELL_VALUE_NOT_MATCHED);
                        throw e;
                    }
                }
            }
        } else if (entry.getProcessor() != null) {
            try {
                convertedValue = entry.getProcessor().process(context, cell, new ExcelFileCellValue(value));
            } catch (RuntimeException re) {
                if (re instanceof ExcelFileException) {
                    ((ExcelFileException) re).setRowIndex(row.getRowNum());
                    ((ExcelFileException) re).setColStrIndex(convertIntIndexToColCharIndex(cell.getColumnIndex()));
                    ((ExcelFileException) re).setColIndex(cell.getColumnIndex());
                    ((ExcelFileException) re).setSystemErrorCode(ExcelFileException.CODE_OF_PROCESS_EXCEPTION);
                    throw re;
                } else {
                    ExcelFileException e = new ExcelFileException(re);
                    e.setRowIndex(row.getRowNum());
                    e.setColStrIndex(convertIntIndexToColCharIndex(cell.getColumnIndex()));
                    e.setColIndex(cell.getColumnIndex());
                    e.setSystemErrorCode(ExcelFileException.CODE_OF_PROCESS_EXCEPTION);
                    throw e;
                }
            }
            if (convertedValue != null && convertedValue instanceof ExcelFileCellValue) {
                convertedValue = value;
            }
        }
        if (convertedValue == null && entry.isRequired()) {
            ExcelFileException e = new ExcelFileException();
            e.setRowIndex(row.getRowNum());
            e.setColStrIndex(convertIntIndexToColCharIndex(cell.getColumnIndex()));
            e.setColIndex(cell.getColumnIndex());
            e.setSystemErrorCode(ExcelFileException.CODE_OF_CELL_VALUE_REQUIRED);
            throw e;
        } else {
            return convertedValue;
        }
    }

    public static int convertColCharIndexToIntIndex(String colIndex) {
        char[] chars = colIndex.toCharArray();
        int index = 0;
        int baseStep = 'z' - 'a' + 1;
        int curStep = 1;
        for (int i = chars.length - 1; i >= 0; i--) {
            char ch = chars[i];
            if (ch >= 'a' && ch <= 'z') {
                index += (ch - 'a' + 1) * curStep;
            } else if (ch >= 'A' && ch <= 'Z') {
                index += (ch - 'A' + 1) * curStep;
            } else {
                throw new IllegalArgumentException("colIndex");
            }
            curStep *= baseStep;
        }
        index--;
        return index;
    }

    /**
     * @param index start from 0
     * @return
     */
    public static String convertIntIndexToColCharIndex(int index) {
        Assert.isTrue(index >= 0);
        StringBuilder sb = new StringBuilder();
        do {
            char c = (char) ((index % 26) + 'A');
            sb.insert(0, c);
            index = index / 26 - 1;
        } while (index >= 0);
        return sb.toString();
    }

    public static void test() {

    }

}

package org.zoukaiming.pio.utils.read;

import java.util.List;

import org.zoukaiming.pio.utils.ExcelFileFieldMapping;
import org.zoukaiming.pio.utils.common.Exclude;
import org.zoukaiming.pio.utils.common.Include;

/**
 * @author zoukaiming
 */
public abstract class ExcelFileReadSheetProcessor<T> {

    private Integer                      sheetIndex;
    private String                       sheetName;
    private Class<T>                     targetClass;
    private int                          startRow = 0;
    private Integer                      pageSize;
    private ExcelFileFieldMapping        fieldMapping;
    private ExcelFileReadRowProcessor<T> rowProcessor;
    private Include<String>              include;
    private Exclude<String>              exclude;

    public abstract void process(ExcelFileReadContext<T> context, List<T> list);

    public abstract void onExcepton(RuntimeException e);

    public Integer getSheetIndex() {
        return sheetIndex;
    }

    public void setSheetIndex(Integer sheetIndex) {
        this.sheetIndex = sheetIndex;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public ExcelFileFieldMapping getFieldMapping() {
        return fieldMapping;
    }

    public void setFieldMapping(ExcelFileFieldMapping fieldMapping) {
        this.fieldMapping = fieldMapping;
    }

    public ExcelFileReadRowProcessor<T> getRowProcessor() {
        return rowProcessor;
    }

    public void setRowProcessor(ExcelFileReadRowProcessor<T> rowProcessor) {
        this.rowProcessor = rowProcessor;
    }

    public Include<String> getInclude() {
        return include;
    }

    public void setInclude(Include<String> include) {
        this.include = include;
    }

    public Exclude<String> getExclude() {
        return exclude;
    }

    public void setExclude(Exclude<String> exclude) {
        this.exclude = exclude;
    }

    public Class<T> getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class<T> targetClass) {
        this.targetClass = targetClass;
    }

}

package com.allen.common.pio.util;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.zoukaiming.pio.utils.ExcelFileCellValue;
import org.zoukaiming.pio.utils.ExcelFileCellValueMapping;
import org.zoukaiming.pio.utils.ExcelFileException;
import org.zoukaiming.pio.utils.ExcelFileFieldMapping;
import org.zoukaiming.pio.utils.ExcelFileUtil;
import org.zoukaiming.pio.utils.read.ExcelFileReadCellProcessor;
import org.zoukaiming.pio.utils.read.ExcelFileReadContext;
import org.zoukaiming.pio.utils.read.ExcelFileReadRowProcessor;
import org.zoukaiming.pio.utils.read.ExcelFileReadSheetProcessor;

import com.alibaba.fastjson.JSONObject;
import com.allen.common.pio.util.TestBean.TestEnum;

/**
 * @author zoukaiming
 */
public class ExcelFileUtilTest {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream in = ExcelFileUtilTest.class.getResourceAsStream("/excel/xlsx/file1.xlsx");
        ExcelFileReadSheetProcessor<TestBean> processor = new ExcelFileReadSheetProcessor<TestBean>() {

            @Override
            public void process(ExcelFileReadContext<TestBean> context, List<TestBean> list) {
                System.out.println(JSONObject.toJSONString(list));
            }

            @Override
            public void onExcepton(RuntimeException e) {
                if (e instanceof ExcelFileException) {
                    ExcelFileException ef = (ExcelFileException) e;
                    if (ef.getSystemErrorCode() == ExcelFileException.CODE_OF_CELL_VALUE_REQUIRED) {
                        System.out.println("第" + (ef.getRowIndex() + 1) + "行，第" + ef.getColStrIndex() + "列是必填项");
                    } else if (ef.getSystemErrorCode() == ExcelFileException.CODE_OF_CELL_VALUE_NOT_MATCHED) {
                        System.out.println("第" + (ef.getRowIndex() + 1) + "行，第" + ef.getColStrIndex() + "列数据不匹配");
                    } else {
                        System.out.println("第" + (ef.getRowIndex() + 1) + "行，第" + ef.getColStrIndex() + "列数据解析异常");
                    }
                }
                throw e;
            }
        };
        ExcelFileFieldMapping fieldMapping = new ExcelFileFieldMapping();
        fieldMapping.put("A", "tByte");
        fieldMapping.put("B", "tShot");
        fieldMapping.put("C", "tInt");
        fieldMapping.put("D", "tLong");
        fieldMapping.put("E", "tFloat");
        fieldMapping.put("F", "tDouble");
        fieldMapping.put("G", "tBool");
        fieldMapping.put("H", "tStr");
        fieldMapping.put("I", "tDate");

        fieldMapping.put("J", "tEnum1", new ExcelFileReadCellProcessor() {

            public Object process(ExcelFileReadContext<?> context, Cell cell, ExcelFileCellValue cellValue) {
                return cellValue.getStringValue() + "=>row:" + context.getCurRowIndex() + ",col："
                       + context.getCurColStrIndex();
            }
        });

        ExcelFileCellValueMapping<String, Object> valueMapping = new ExcelFileCellValueMapping<String, Object>();
        valueMapping.put("请选择", null);
        valueMapping.put("枚举1", TestEnum.AA.toString());
        valueMapping.put("枚举2", TestEnum.BB.toString());
        valueMapping.put("枚举3", TestEnum.CC.toString());
        fieldMapping.put("K", "tEnum2", valueMapping);

        processor.setSheetIndex(0);
        processor.setStartRow(1);
        processor.setTargetClass(TestBean.class);
        processor.setFieldMapping(fieldMapping);
        processor.setRowProcessor(new ExcelFileReadRowProcessor<TestBean>() {

            public TestBean process(ExcelFileReadContext<TestBean> context, Row row, TestBean t) {
                return t;
            }
        });
        ExcelFileUtil.readInputStreamToObject(in, processor);
    }
}

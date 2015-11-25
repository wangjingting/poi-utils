package com.allen.common.pio.util;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.zoukaiming.pio.utils.excel.ExcelFileFieldMapping;
import org.zoukaiming.pio.utils.excel.ExcelFileReadSheetProcessor;
import org.zoukaiming.pio.utils.excel.ExcelFileUtil;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 */
public class ExcelFileUtilTest {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream in = ExcelFileUtilTest.class.getResourceAsStream("/excel/xls/file2.xls");
        // InputStream in = ExcelFileUtilTest.class.getResourceAsStream("/excel/xlsx/file1.xlsx");
        ExcelFileReadSheetProcessor<TestBean> processor = new ExcelFileReadSheetProcessor<TestBean>() {

            @Override
            public void process(List<TestBean> list) {
                System.out.println(JSONObject.toJSONString(list));
            }

            @Override
            public void onExcepton(RuntimeException e) {

            }
        };
        ExcelFileFieldMapping fieldMapping = new ExcelFileFieldMapping();
        fieldMapping.put("B", "str");
        processor.setSheetIndex(0);
        processor.setStartRow(0);
        processor.setTargetClass(TestBean.class);
        processor.setFieldMapping(fieldMapping);
        ExcelFileUtil.readInputStreamToObject(in, processor);
    }
}

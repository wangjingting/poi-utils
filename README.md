
 * author:邹凯明
 * email:0x4c4a@gmail.com

# poi-utils
help you more easy reading or writing[TODO] excel file

本工具包入口类是ExcelFileUtil，对于读取excel提供的是以下工具类：
```
ExcelFileUtil.readInputStreamToObject(InputStream workbookInputStream,ExcelFileReadSheetProcessor<?>... sheetProcessors);
```
第一个参数是Excel File的输入流，第二个参数是Sheet解析器,它是一个抽象类，含有三个必填的参数和两个必须实现的方法：
*sheetIndex或sheetName;//设置要解析哪一张sheet(based-0)
*targetClass;//设置读取的excel行数据将被解析的目标Bean Class
*fieldMapping;//设置excel列和Java Bean的字段映射[下面结束]
*void process(){ExcelFileReadContext<T> context, List<T> list};//解析Excel成功后后的回调，结果将由lis参数返回
*void onExcepton(RuntimeException e);//执行异常时的回掉，注意如果异常是ExcelFileException，你可以定位出解析excel异常的具体信息。
注：setStartRow;虽然不是必填项，但由于Excel中第一行通常是列名，真正的数据是从第1（based-0）行开始的，所以通常都会设置该参数;




类通过类(ExcelFileFieldMapping)将excel某一列和Java 

Bean的某一字段建立映射关系，然后通过ExcelFileCellValueMapping/ExcelFileReadCellProcessor类
灵活处理单元格内容的具体转换，方便你能快速的实现Excel文件解析。

##this is a sample demo for reading excel file.

```

 public static void main(String[] args) throws FileNotFoundException {
        InputStream in = ExcelFileUtilTest.class.getResourceAsStream("/excel/xlsx/file1.xlsx");
        ExcelFileReadSheetProcessor<TestBean> sheetProcessor = new ExcelFileReadSheetProcessor<TestBean>() {
            public void process(ExcelFileReadContext<TestBean> context, List<TestBean> list) {
                //这里处理解析excel成功后的结果
                System.out.println(JSONObject.toJSONString(list));
            }
            public void onExcepton(RuntimeException e) {
                //这里处理解析excel过程中的异常
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
        fieldMapping.put("A", "tByte");//cell直接解析
        fieldMapping.put("J", "tEnum1", new ExcelFileReadCellProcessor() {//cell自定义解析
            public Object process(ExcelFileReadContext<?> context, Cell cell, ExcelFileCellValue cellValue) {
                return cellValue.getStringValue() + "=>row:" + context.getCurRowIndex() + ",col："
                       + context.getCurColStrIndex();
            }
        });

        ExcelFileCellValueMapping<String, Object> valueMapping = new ExcelFileCellValueMapping<String, Object>();
        valueMapping.put("请选择", null);
        valueMapping.put("枚举1", TestEnum.AA.toString());
        valueMapping.put("枚举2", TestEnum.BB.toString());
        fieldMapping.put("K", "tEnum2", valueMapping);//cell自定义解析

        sheetProcessor.setSheetIndex(0);//设置从excel的第几张sheet开始解析(based-0)
        sheetProcessor.setStartRow(1);//设置从excel的第几行开始解析(based-0)
        sheetProcessor.setTargetClass(TestBean.class);//设置读取的excel行数据将被解析的目标Bean Class
        sheetProcessor.setFieldMapping(fieldMapping);//设置excel列和Java Bean的字段映射
        ExcelFileUtil.readInputStreamToObject(in, sheetProcessor);//执行解析
    }
```


###详细实例请参考ExcelFileUtilTest类



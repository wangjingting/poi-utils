# poi-utils
help you more easy read or write excel file

本工具类通过类(ExcelFileFieldMapping)将excel某一列和Java Bean的某一字段建立映射关系，然后通过ExcelFileCellValueMapping/ExcelFileReadCellProcessor类
灵活处理单元格内容的具体转换。

##this is a sample demo for read excel file.

```

 public static void main(String[] args) throws FileNotFoundException {
        InputStream in = ExcelFileUtilTest.class.getResourceAsStream("/excel/xlsx/file1.xlsx");
        ExcelFileReadSheetProcessor<TestBean> processor = new ExcelFileReadSheetProcessor<TestBean>() {
            @Override
            public void process(ExcelFileReadContext<TestBean> context, List<TestBean> list) {
                //这里处理成功的结果
                System.out.println(JSONObject.toJSONString(list));
            }
            @Override
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
        fieldMapping.put("A", "tByte");//...
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
```

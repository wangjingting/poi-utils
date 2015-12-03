package org.zoukaiming.pio.utils;

import java.io.Serializable;

import org.zoukaiming.pio.utils.read.ExcelFileReadCellProcessor;

/**
 * @author zoukaiming
 */
public class InnerEntry implements Serializable {

    private static final long                         serialVersionUID = 1L;
    private String                                    fieldName;
    private boolean                                   required         = true; // /////
    private ExcelFileReadCellProcessor                processor;
    private ExcelFileCellValueMapping<String, Object> valueMapping;

    public InnerEntry(String fieldName, ExcelFileCellValueMapping<String, Object> valueMapping,
                      ExcelFileReadCellProcessor processor, boolean required) {
        this.fieldName = fieldName;
        this.valueMapping = valueMapping;
        this.processor = processor;
        this.required = required;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public ExcelFileReadCellProcessor getProcessor() {
        return processor;
    }

    public void setProcessor(ExcelFileReadCellProcessor processor) {
        this.processor = processor;
    }

    public ExcelFileCellValueMapping<String, Object> getValueMapping() {
        return valueMapping;
    }

    public void setValueMapping(ExcelFileCellValueMapping<String, Object> valueMapping) {
        this.valueMapping = valueMapping;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

}

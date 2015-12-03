package org.zoukaiming.pio.utils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import com.alibaba.fastjson.util.TypeUtils;

/**
 * @author zoukaiming
 */
public class ExcelFileCellValue implements Serializable {

    private static final long serialVersionUID = 1L;
    private Object            originalValue;

    public ExcelFileCellValue(Object originalValue) {
        this.originalValue = originalValue;
    }

    public Byte getByteValue() {
        return TypeUtils.castToByte(originalValue);
    }

    public Short getShortValue() {
        return TypeUtils.castToShort(originalValue);
    }

    public Integer getIntValue() {
        return TypeUtils.castToInt(originalValue);
    }

    public Long getLongValue() {
        return TypeUtils.castToLong(originalValue);
    }

    public Float getFloatValue() {
        return TypeUtils.castToFloat(originalValue);
    }

    public Double getDoubleValue() {
        return TypeUtils.castToDouble(originalValue);
    }

    public String getStringValue() {
        return TypeUtils.castToString(originalValue);
    }

    public Boolean getBooleanValue() {
        return TypeUtils.castToBoolean(originalValue);
    }

    public Date getDateValue() {
        return TypeUtils.castToDate(originalValue);
    }

    public BigDecimal getBigDecimal() {
        return TypeUtils.castToBigDecimal(originalValue);
    }

    public BigInteger getBigInteger() {
        return TypeUtils.castToBigInteger(originalValue);
    }

    public java.sql.Timestamp getTimestamp() {
        return TypeUtils.castToTimestamp(originalValue);
    }

    public Object getOriginalValue() {
        return originalValue;
    }
}

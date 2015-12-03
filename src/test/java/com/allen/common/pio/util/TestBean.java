package com.allen.common.pio.util;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zoukaiming
 */
public class TestBean implements Serializable {

    public static enum TestEnum {
        AA, BB, CC;
    }

    private Byte    tByte;
    private Short   tShort;
    private Integer tInt;
    private Long    tLong;
    private Float   tFloat;
    private Double  tDouble;
    private Boolean tBool;
    private String  tStr;
    private Date    tDate;
    private String  tEnum1;
    private String  tEnum2;

    public Boolean gettBool() {
        return tBool;
    }

    public void settBool(Boolean tBool) {
        this.tBool = tBool;
    }

    public Byte gettByte() {
        return tByte;
    }

    public void settByte(Byte tByte) {
        this.tByte = tByte;
    }

    public Short gettShort() {
        return tShort;
    }

    public void settShort(Short tShort) {
        this.tShort = tShort;
    }

    public Integer gettInt() {
        return tInt;
    }

    public void settInt(Integer tInt) {
        this.tInt = tInt;
    }

    public Long gettLong() {
        return tLong;
    }

    public void settLong(Long tLong) {
        this.tLong = tLong;
    }

    public Float gettFloat() {
        return tFloat;
    }

    public void settFloat(Float tFloat) {
        this.tFloat = tFloat;
    }

    public Double gettDouble() {
        return tDouble;
    }

    public void settDouble(Double tDouble) {
        this.tDouble = tDouble;
    }

    public String gettStr() {
        return tStr;
    }

    public void settStr(String tStr) {
        this.tStr = tStr;
    }

    public Date gettDate() {
        return tDate;
    }

    public void settDate(Date tDate) {
        this.tDate = tDate;
    }

    public String gettEnum1() {
        return tEnum1;
    }

    public void settEnum1(String tEnum1) {
        this.tEnum1 = tEnum1;
    }

    public String gettEnum2() {
        return tEnum2;
    }

    public void settEnum2(String tEnum2) {
        this.tEnum2 = tEnum2;
    }

}

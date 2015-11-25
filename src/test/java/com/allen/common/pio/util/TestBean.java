package com.allen.common.pio.util;

import java.io.Serializable;

public class TestBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private String            str;
    private Long              num;

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

}

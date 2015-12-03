package org.zoukaiming.pio.utils.read;

import java.io.Serializable;

/**
 * @author zoukaiming
 */
public class ExcelFileReadContext<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer           curSheetIndex;
    private String            curSheetName;
    private Long              lastRowIndex;

    private Long              curRowIndex;
    private T                 curRow;
    private Integer           curColIndex;
    private String            curColStrIndex;
    private Integer           curRowLastColIndex;

    public Integer getCurSheetIndex() {
        return curSheetIndex;
    }

    public void setCurSheetIndex(Integer curSheetIndex) {
        this.curSheetIndex = curSheetIndex;
    }

    public String getCurSheetName() {
        return curSheetName;
    }

    public void setCurSheetName(String curSheetName) {
        this.curSheetName = curSheetName;
    }

    public Long getLastRowIndex() {
        return lastRowIndex;
    }

    public void setLastRowIndex(Long lastRowIndex) {
        this.lastRowIndex = lastRowIndex;
    }

    public Long getCurRowIndex() {
        return curRowIndex;
    }

    public void setCurRowIndex(Long curRowIndex) {
        this.curRowIndex = curRowIndex;
    }

    public T getCurRow() {
        return curRow;
    }

    public void setCurRow(T curRow) {
        this.curRow = curRow;
    }

    public Integer getCurColIndex() {
        return curColIndex;
    }

    public void setCurColIndex(Integer curColIndex) {
        this.curColIndex = curColIndex;
    }

    public String getCurColStrIndex() {
        return curColStrIndex;
    }

    public void setCurColStrIndex(String curColStrIndex) {
        this.curColStrIndex = curColStrIndex;
    }

    public Integer getCurRowLastColIndex() {
        return curRowLastColIndex;
    }

    public void setCurRowLastColIndex(Integer curRowLastColIndex) {
        this.curRowLastColIndex = curRowLastColIndex;
    }

}

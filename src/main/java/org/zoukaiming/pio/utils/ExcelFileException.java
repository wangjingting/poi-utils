package org.zoukaiming.pio.utils;

/**
 * @author zoukaiming
 */
public class ExcelFileException extends RuntimeException {

    private static final long serialVersionUID                    = 1L;
    public static int         CODE_OF_CELL_VALUE_REQUIRED         = 1;
    public static int         CODE_OF_CELL_VALUE_NOT_MATCHED      = 2;
    public static int         CODE_OF_PROCESS_TYPE_CAST_EXCEPTION = 3;
    public static int         CODE_OF_PROCESS_EXCEPTION           = 999;

    private Integer           rowIndex                            = null;
    private String            colStrIndex                         = null;
    private Integer           colIndex                            = null;
    private String            userErrorCode                       = null;
    private int               systemErrorCode                     = 0;

    public String getUserErrorCode() {
        return userErrorCode;
    }

    public void setUserErrorCode(String userErrorCode) {
        this.userErrorCode = userErrorCode;
    }

    public int getSystemErrorCode() {
        return systemErrorCode;
    }

    public void setSystemErrorCode(int systemErrorCode) {
        this.systemErrorCode = systemErrorCode;
    }

    public ExcelFileException() {
        super();
    }

    public ExcelFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExcelFileException(String message) {
        super(message);
    }

    public ExcelFileException(Throwable cause) {
        super(cause);
    }

    public Integer getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(Integer rowIndex) {
        this.rowIndex = rowIndex;
    }

    public String getColStrIndex() {
        return colStrIndex;
    }

    public void setColStrIndex(String colStrIndex) {
        this.colStrIndex = colStrIndex;
    }

    public Integer getColIndex() {
        return colIndex;
    }

    public void setColIndex(Integer colIndex) {
        this.colIndex = colIndex;
    }

}

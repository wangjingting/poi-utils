package org.zoukaiming.pio.utils;

import java.util.HashMap;

import org.zoukaiming.pio.utils.read.ExcelFileReadCellProcessor;

/**
 * @author zoukaiming
 */
public class ExcelFileCellValueMapping<K, V> extends HashMap<K, V> {

    /**  */
    public static final Object         DEFAULT_KEY      = new Object();
    public static final Object         DEFAULT_VALUE    = new Object();
    private static final long          serialVersionUID = 1L;
    private ExcelFileReadCellProcessor defaultProcessor;

    public ExcelFileReadCellProcessor getDefaultProcessor() {
        return defaultProcessor;
    }

    public void setDefaultProcessor(ExcelFileReadCellProcessor defaultProcessor) {
        this.defaultProcessor = defaultProcessor;
    }
}

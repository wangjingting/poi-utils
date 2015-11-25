package org.zoukaiming.pio.utils.excel;

import java.util.HashMap;

/**
 * @author zoukaiming
 */
public class ExcelFileCellValueMapping<K, V> extends HashMap<K, V> {

    /**  */
    public static final Object         DEFAULT_KEY      = new Object();
    private static final long          serialVersionUID = 1L;
    private ExcelFileReadCellProcessor defaultProcessor;

    public ExcelFileReadCellProcessor getDefaultProcessor() {
        return defaultProcessor;
    }

    public void setDefaultProcessor(ExcelFileReadCellProcessor defaultProcessor) {
        this.defaultProcessor = defaultProcessor;
    }
}

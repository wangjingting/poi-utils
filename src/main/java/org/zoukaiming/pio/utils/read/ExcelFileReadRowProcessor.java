package org.zoukaiming.pio.utils.read;

import org.apache.poi.ss.usermodel.Row;

/**
 * @author zoukaiming
 */
public interface ExcelFileReadRowProcessor<T> {

    /**
     * if you return null,this row will be skipped.
     */
    T process(ExcelFileReadContext<T> context, Row row, T t);
}

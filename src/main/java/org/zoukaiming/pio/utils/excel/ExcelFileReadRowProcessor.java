package org.zoukaiming.pio.utils.excel;

import org.apache.poi.ss.usermodel.Row;

/**
 * @author zoukaiming
 */
public interface ExcelFileReadRowProcessor<T> {

    T process(Row row, T t);
}

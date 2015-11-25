package org.zoukaiming.pio.utils.excel;

import org.apache.poi.ss.usermodel.Cell;

/**
 * @author zoukaiming
 */
public interface ExcelFileReadCellProcessor {

    Object process(Cell cell, Object obj);
}

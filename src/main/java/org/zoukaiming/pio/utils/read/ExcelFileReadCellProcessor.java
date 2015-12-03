package org.zoukaiming.pio.utils.read;

import org.apache.poi.ss.usermodel.Cell;
import org.zoukaiming.pio.utils.ExcelFileCellValue;

/**
 * @author zoukaiming
 */
public interface ExcelFileReadCellProcessor {

    /**
     * @param context
     * @param cell
     * @param cellValue
     * @return target value. if return cellValue, cellValue.getOriginalValue() will be set as final result.
     */
    Object process(ExcelFileReadContext<?> context, Cell cell, ExcelFileCellValue cellValue);
}

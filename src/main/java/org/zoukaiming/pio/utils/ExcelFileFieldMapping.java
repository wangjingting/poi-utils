package org.zoukaiming.pio.utils;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.zoukaiming.pio.utils.read.ExcelFileReadCellProcessor;

/**
 * @author zoukaiming
 */
public class ExcelFileFieldMapping implements Serializable {

    private static final boolean                  DEFAULT_REQUIRE  = true;
    private static final long                     serialVersionUID = 1L;

    private Map<Integer, Map<String, InnerEntry>> fieldMapping     = new LinkedHashMap<Integer, Map<String, InnerEntry>>();

    public void put(int colIndex, String fieldName) {
        put(colIndex, fieldName, null, null, DEFAULT_REQUIRE);
    }

    public void put(int colIndex, String fieldName, boolean required) {
        put(colIndex, fieldName, null, null, required);
    }

    public void put(int colIndex, String fieldName, ExcelFileReadCellProcessor proccessor) {
        put(colIndex, fieldName, null, proccessor, DEFAULT_REQUIRE);
    }

    public void put(int colIndex, String fieldName, ExcelFileCellValueMapping<String, Object> valueMapping) {
        put(colIndex, fieldName, valueMapping, null, DEFAULT_REQUIRE);
    }

    public void put(int colIndex, String fieldName, ExcelFileReadCellProcessor proccessor, boolean required) {
        put(colIndex, fieldName, null, proccessor, required);
    }

    public void put(int colIndex, String fieldName, ExcelFileCellValueMapping<String, Object> valueMapping,
                    boolean required) {
        put(colIndex, fieldName, valueMapping, null, required);
    }

    public void put(String colIndex, String fieldName) {
        put(colIndex, fieldName, null, null, DEFAULT_REQUIRE);
    }

    public void put(String colIndex, String fieldName, boolean required) {
        put(colIndex, fieldName, null, null, required);
    }

    public void put(String colIndex, String fieldName, ExcelFileCellValueMapping<String, Object> valueMapping) {
        put(colIndex, fieldName, valueMapping, null, DEFAULT_REQUIRE);
    }

    public void put(String colIndex, String fieldName, ExcelFileReadCellProcessor proccessor) {
        put(colIndex, fieldName, null, proccessor, DEFAULT_REQUIRE);
    }

    public void put(String colIndex, String fieldName, ExcelFileCellValueMapping<String, Object> valueMapping,
                    boolean required) {
        put(colIndex, fieldName, valueMapping, null, required);
    }

    public void put(String colIndex, String fieldName, ExcelFileReadCellProcessor proccessor, boolean required) {
        put(colIndex, fieldName, null, proccessor, required);
    }

    private void put(int colIndex, String fieldName, ExcelFileCellValueMapping<String, Object> valueMapping,
                     ExcelFileReadCellProcessor proccessor, boolean required) {
        Map<String, InnerEntry> map = fieldMapping.get(colIndex);
        if (map == null) {
            synchronized (fieldMapping) {
                if (fieldMapping.get(colIndex) == null) {
                    map = new ConcurrentHashMap<String, InnerEntry>();
                    fieldMapping.put(colIndex, map);
                }
            }
        }
        map.put(fieldName, new InnerEntry(fieldName, valueMapping, proccessor, required));
    }

    private void put(String colIndex, String fieldName, ExcelFileCellValueMapping<String, Object> valueMapping,
                     ExcelFileReadCellProcessor proccessor, boolean required) {
        put(ExcelFileUtil.convertColCharIndexToIntIndex(colIndex), fieldName, valueMapping, proccessor, required);
    }

    public boolean isEmpty() {
        return fieldMapping.isEmpty();
    }

    public Set<Entry<Integer, Map<String, InnerEntry>>> entrySet() {
        return fieldMapping.entrySet();
    }

}

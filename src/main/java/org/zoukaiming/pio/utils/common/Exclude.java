package org.zoukaiming.pio.utils.common;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author zoukaiming
 */
public class Exclude<T> {

    private Set<T> set = new HashSet<T>();

    public Exclude(T... t) {
        if (t == null) {
            return;
        } else {
            for (T item : t) {
                set.add(item);
            }
        }
    }

    public Exclude(Collection<T> collection) {
        if (collection != null) {
            set.addAll(collection);
        }
    }

    public boolean contains(T t) {
        return set.contains(t);
    }

}

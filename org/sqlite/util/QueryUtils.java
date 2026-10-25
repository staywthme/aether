/*
 * Decompiled with CFR 0.152.
 */
package org.sqlite.util;

import java.util.List;
import java.util.stream.Collectors;

public class QueryUtils {
    public static String valuesQuery(List<String> list4, List<List<Object>> list5) {
        list5.forEach(list3 -> {
            if (list3.size() != list4.size()) {
                throw new IllegalArgumentException("values and columns must have the same size");
            }
        });
        return "with cte(" + String.join((CharSequence)",", list4) + ") as (values " + list5.stream().map(list2 -> "(" + list2.stream().map(object -> {
            if (object instanceof String) {
                return "'" + object + "'";
            }
            if (object == null) {
                return "null";
            }
            return object.toString();
        }).collect(Collectors.joining(",")) + ")").collect(Collectors.joining(",")) + ") select * from cte";
    }
}


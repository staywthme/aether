/*
 * Decompiled with CFR 0.152.
 */
package org.sqlite.util;

import java.util.List;

public class StringUtils {
    public static String join(List<String> object, String string) {
        StringBuilder stringBuilder = new StringBuilder();
        boolean bl = true;
        object = object.iterator();
        while (object.hasNext()) {
            String string2 = (String)object.next();
            if (bl) {
                bl = false;
            } else {
                stringBuilder.append(string);
            }
            stringBuilder.append(string2);
        }
        return stringBuilder.toString();
    }
}


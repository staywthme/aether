/*
 * Decompiled with CFR 0.152.
 */
package org.bridj.util;

import java.util.Arrays;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class StringUtils {
    public static String implode(double[] dArray, String string) {
        StringBuffer stringBuffer = new StringBuffer();
        boolean bl = true;
        for (double d2 : dArray) {
            if (bl) {
                bl = false;
            } else {
                stringBuffer.append(string);
            }
            stringBuffer.append(d2);
        }
        return stringBuffer.toString();
    }

    public static String implode(Object[] objectArray) {
        return StringUtils.implode(objectArray, (Object)", ");
    }

    public static String implode(Object[] objectArray, Object object) {
        return StringUtils.implode(Arrays.asList(objectArray), object);
    }

    public static final <T> String implode(Iterable<T> object, Object object2) {
        object2 = object2.toString();
        StringBuilder stringBuilder = new StringBuilder();
        boolean bl = true;
        object = object.iterator();
        while (object.hasNext()) {
            Object e2 = object.next();
            if (e2 == null) continue;
            if (bl) {
                bl = false;
            } else {
                stringBuilder.append((String)object2);
            }
            stringBuilder.append(e2);
        }
        return stringBuilder.toString();
    }

    public static final String implode(Iterable<?> iterable) {
        return StringUtils.implode(iterable, (Object)", ");
    }
}


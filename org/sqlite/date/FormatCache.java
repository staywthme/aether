/*
 * Decompiled with CFR 0.152.
 */
package org.sqlite.date;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

abstract class FormatCache<F extends Format> {
    static final int NONE = -1;
    private final ConcurrentMap<MultipartKey, F> cInstanceCache = new ConcurrentHashMap<MultipartKey, F>(7);
    private static final ConcurrentMap<MultipartKey, String> cDateTimeInstanceCache = new ConcurrentHashMap<MultipartKey, String>(7);

    FormatCache() {
    }

    public F getInstance() {
        return this.getDateTimeInstance(3, 3, TimeZone.getDefault(), Locale.getDefault());
    }

    public F getInstance(String object, TimeZone timeZone, Locale locale) {
        MultipartKey multipartKey;
        Object object2;
        if (object == null) {
            throw new NullPointerException("pattern must not be null");
        }
        if (timeZone == null) {
            timeZone = TimeZone.getDefault();
        }
        if (locale == null) {
            locale = Locale.getDefault();
        }
        if ((object2 = (Format)this.cInstanceCache.get(multipartKey = new MultipartKey(object, timeZone, locale))) == null && (object = this.cInstanceCache.putIfAbsent(multipartKey, object2 = this.createInstance((String)object, timeZone, locale))) != null) {
            object2 = object;
        }
        return (F)object2;
    }

    protected abstract F createInstance(String var1, TimeZone var2, Locale var3);

    private F getDateTimeInstance(Integer object, Integer n2, TimeZone timeZone, Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        object = FormatCache.getPatternForStyle((Integer)object, n2, locale);
        return this.getInstance((String)object, timeZone, locale);
    }

    F getDateTimeInstance(int n2, int n3, TimeZone timeZone, Locale locale) {
        return this.getDateTimeInstance((Integer)n2, (Integer)n3, timeZone, locale);
    }

    F getDateInstance(int n2, TimeZone timeZone, Locale locale) {
        return this.getDateTimeInstance((Integer)n2, null, timeZone, locale);
    }

    F getTimeInstance(int n2, TimeZone timeZone, Locale locale) {
        return this.getDateTimeInstance(null, (Integer)n2, timeZone, locale);
    }

    static String getPatternForStyle(Integer object, Integer n2, Locale locale) {
        MultipartKey multipartKey = new MultipartKey(object, n2, locale);
        Object object2 = (String)cDateTimeInstanceCache.get(multipartKey);
        if (object2 == null) {
            try {
                object = object == null ? DateFormat.getTimeInstance(n2, locale) : (n2 == null ? DateFormat.getDateInstance((Integer)object, locale) : DateFormat.getDateTimeInstance((Integer)object, n2, locale));
                object2 = ((SimpleDateFormat)object).toPattern();
                object = cDateTimeInstanceCache.putIfAbsent(multipartKey, (String)object2);
                if (object != null) {
                    object2 = object;
                }
            }
            catch (ClassCastException classCastException) {
                throw new IllegalArgumentException("No date time pattern for locale: " + locale);
            }
        }
        return object2;
    }

    static class MultipartKey {
        private final Object[] keys;
        private int hashCode;

        public MultipartKey(Object ... objectArray) {
            this.keys = objectArray;
        }

        public boolean equals(Object object) {
            return Arrays.equals(this.keys, ((MultipartKey)object).keys);
        }

        public int hashCode() {
            if (this.hashCode == 0) {
                int n2 = 0;
                Object[] objectArray = this.keys;
                int n3 = this.keys.length;
                for (int i2 = 0; i2 < n3; ++i2) {
                    Object object = objectArray[i2];
                    if (object == null) continue;
                    n2 = n2 * 7 + object.hashCode();
                }
                this.hashCode = n2;
            }
            return this.hashCode;
        }
    }
}


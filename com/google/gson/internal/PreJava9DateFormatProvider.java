/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson.internal;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class PreJava9DateFormatProvider {
    public static DateFormat getUSDateFormat(int n2) {
        return new SimpleDateFormat(PreJava9DateFormatProvider.getDateFormatPattern(n2), Locale.US);
    }

    public static DateFormat getUSDateTimeFormat(int n2, int n3) {
        String string = PreJava9DateFormatProvider.getDatePartOfDateTimePattern(n2) + " " + PreJava9DateFormatProvider.getTimePartOfDateTimePattern(n3);
        return new SimpleDateFormat(string, Locale.US);
    }

    private static String getDateFormatPattern(int n2) {
        switch (n2) {
            case 3: {
                return "M/d/yy";
            }
            case 2: {
                return "MMM d, y";
            }
            case 1: {
                return "MMMM d, y";
            }
            case 0: {
                return "EEEE, MMMM d, y";
            }
        }
        throw new IllegalArgumentException("Unknown DateFormat style: " + n2);
    }

    private static String getDatePartOfDateTimePattern(int n2) {
        switch (n2) {
            case 3: {
                return "M/d/yy";
            }
            case 2: {
                return "MMM d, yyyy";
            }
            case 1: {
                return "MMMM d, yyyy";
            }
            case 0: {
                return "EEEE, MMMM d, yyyy";
            }
        }
        throw new IllegalArgumentException("Unknown DateFormat style: " + n2);
    }

    private static String getTimePartOfDateTimePattern(int n2) {
        switch (n2) {
            case 3: {
                return "h:mm a";
            }
            case 2: {
                return "h:mm:ss a";
            }
            case 0: 
            case 1: {
                return "h:mm:ss a z";
            }
        }
        throw new IllegalArgumentException("Unknown DateFormat style: " + n2);
    }
}


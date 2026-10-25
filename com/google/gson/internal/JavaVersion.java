/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson.internal;

public final class JavaVersion {
    private static final int majorJavaVersion = JavaVersion.determineMajorJavaVersion();

    private static int determineMajorJavaVersion() {
        String string = System.getProperty("java.version");
        return JavaVersion.getMajorJavaVersion(string);
    }

    static int getMajorJavaVersion(String string) {
        int n2 = JavaVersion.parseDotted(string);
        if (n2 == -1) {
            n2 = JavaVersion.extractBeginningInt(string);
        }
        if (n2 == -1) {
            return 6;
        }
        return n2;
    }

    private static int parseDotted(String stringArray) {
        try {
            stringArray = stringArray.split("[._]");
            int n2 = Integer.parseInt(stringArray[0]);
            if (n2 == 1 && stringArray.length > 1) {
                return Integer.parseInt(stringArray[1]);
            }
            return n2;
        }
        catch (NumberFormatException numberFormatException) {
            return -1;
        }
    }

    private static int extractBeginningInt(String string) {
        try {
            char c2;
            StringBuilder stringBuilder = new StringBuilder();
            for (int i2 = 0; i2 < string.length() && Character.isDigit(c2 = string.charAt(i2)); ++i2) {
                stringBuilder.append(c2);
            }
            return Integer.parseInt(stringBuilder.toString());
        }
        catch (NumberFormatException numberFormatException) {
            return -1;
        }
    }

    public static int getMajorJavaVersion() {
        return majorJavaVersion;
    }

    public static boolean isJava9OrLater() {
        return majorJavaVersion >= 9;
    }

    private JavaVersion() {
    }
}


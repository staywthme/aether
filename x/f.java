/*
 * Decompiled with CFR 0.152.
 */
package x;

public final class f {
    static String a(String string, String string2) {
        int n2 = string.indexOf(string2 = "\"" + string2 + "\"");
        if (n2 < 0) {
            return null;
        }
        n2 += string2.length();
        while (n2 < string.length() && string.charAt(n2) != '\"' && string.charAt(n2) != ':') {
            ++n2;
        }
        if (n2 >= string.length()) {
            return null;
        }
        if (string.charAt(n2) == ':') {
            ++n2;
            while (n2 < string.length() && string.charAt(n2) == ' ') {
                ++n2;
            }
        }
        if (n2 >= string.length() || string.charAt(n2) != '\"') {
            return null;
        }
        int n3 = ++n2;
        while (n2 < string.length() && string.charAt(n2) != '\"') {
            ++n2;
        }
        return string.substring(n3, n2);
    }

    static double b(String string, String string2) {
        int n2 = string.indexOf(string2 = "\"" + string2 + "\"");
        if (n2 < 0) {
            return 0.0;
        }
        n2 += string2.length();
        while (n2 < string.length() && (string.charAt(n2) == ':' || string.charAt(n2) == ' ')) {
            ++n2;
        }
        int n3 = n2;
        while (n2 < string.length() && (Character.isDigit(string.charAt(n2)) || string.charAt(n2) == '.' || string.charAt(n2) == '-')) {
            ++n2;
        }
        try {
            return Double.parseDouble(string.substring(n3, n2));
        }
        catch (Exception exception) {
            return 0.0;
        }
    }

    static int a(String string) {
        if (string == null) {
            return 0;
        }
        if (string.length() == 1) {
            char c2 = string.charAt(0);
            if (c2 >= 'a' && c2 <= 'z') {
                return 65 + (c2 - 97);
            }
            if (c2 >= 'A' && c2 <= 'Z') {
                return 65 + (c2 - 65);
            }
            if (c2 >= '0' && c2 <= '9') {
                return 48 + (c2 - 48);
            }
            if (c2 == ' ') {
                return 32;
            }
        }
        String string2 = string.toLowerCase();
        if ($S.d(867).equals(string2)) {
            return 10;
        }
        if ($S.d(868).equals(string2)) {
            return 8;
        }
        if ($S.d(869).equals(string2)) {
            return 127;
        }
        if ($S.d(870).equals(string2)) {
            return 27;
        }
        if ($S.d(871).equals(string2)) {
            return 9;
        }
        if (" ".equals(string) || $S.d(872).equals(string2)) {
            return 32;
        }
        if ($S.d(873).equals(string2)) {
            return 37;
        }
        if ($S.d(874).equals(string2)) {
            return 39;
        }
        if ($S.d(875).equals(string2)) {
            return 38;
        }
        if ($S.d(876).equals(string2)) {
            return 40;
        }
        if ($S.d(877).equals(string2)) {
            return 16;
        }
        if ($S.d(878).equals(string2)) {
            return 17;
        }
        if ($S.d(879).equals(string2)) {
            return 18;
        }
        if ("f1".equals(string2)) {
            return 112;
        }
        if ("f2".equals(string2)) {
            return 113;
        }
        if ("f3".equals(string2)) {
            return 114;
        }
        if ("f4".equals(string2)) {
            return 115;
        }
        if ("f5".equals(string2)) {
            return 116;
        }
        if ("f6".equals(string2)) {
            return 117;
        }
        if ("f7".equals(string2)) {
            return 118;
        }
        if ("f8".equals(string2)) {
            return 119;
        }
        if ("f9".equals(string2)) {
            return 120;
        }
        if ($S.d(880).equals(string2)) {
            return 121;
        }
        if ($S.d(881).equals(string2)) {
            return 122;
        }
        if ($S.d(882).equals(string2)) {
            return 123;
        }
        return 0;
    }
}


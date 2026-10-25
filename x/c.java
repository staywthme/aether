/*
 * Decompiled with CFR 0.152.
 */
package x;

public final class c {
    static void a(byte[] byArray, int n2, int n3) {
        byArray[n2] = (byte)(n3 >>> 24);
        byArray[n2 + 1] = (byte)(n3 >> 16);
        byArray[n2 + 2] = (byte)(n3 >> 8);
        byArray[n2 + 3] = (byte)n3;
    }

    static String a(String string) {
        if (string == null) {
            return "";
        }
        return string.replace("\\", "/").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "").replace("\t", "\\t");
    }

    static String a(String string, String string2) {
        int n2 = string.indexOf(string2 = "\"" + string2 + "\":");
        if (n2 < 0) {
            return null;
        }
        n2 += string2.length();
        while (n2 < string.length() && string.charAt(n2) == ' ') {
            ++n2;
        }
        if (n2 >= string.length()) {
            return null;
        }
        if (string.charAt(n2) == '\"') {
            int n3 = ++n2;
            while (n2 < string.length()) {
                char c2 = string.charAt(n2);
                if (c2 == '\\') {
                    n2 += 2;
                    continue;
                }
                if (c2 == '\"') break;
                ++n2;
            }
            return string.substring(n3, n2);
        }
        int n4 = n2;
        while (n2 < string.length() && (Character.isDigit(string.charAt(n2)) || string.charAt(n2) == '-')) {
            ++n2;
        }
        return string.substring(n4, n2);
    }
}


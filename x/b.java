/*
 * Decompiled with CFR 0.152.
 */
package x;

public final class b {
    static String a(String string) {
        if (string == null) {
            return "";
        }
        return string.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "").replace("\t", "\\t");
    }

    static String a(String string, String string2) {
        int n2 = string.indexOf(string2 = "\"" + string2 + $S.d(238));
        if (n2 < 0) {
            return null;
        }
        int n3 = n2 += string2.length();
        while (n2 < string.length()) {
            char c2 = string.charAt(n2);
            if (c2 == '\\') {
                n2 += 2;
                continue;
            }
            if (c2 == '\"') break;
            ++n2;
        }
        return string.substring(n3, n2).replace("\\\"", "\"").replace("\\\\", "\\").replace("\\n", "\n");
    }
}


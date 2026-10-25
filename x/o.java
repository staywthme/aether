/*
 * Decompiled with CFR 0.152.
 */
package x;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public final class o {
    public static a a() {
        try {
            Object object = System.getenv($S.d(212));
            if (object == null) {
                return null;
            }
            if (!((File)(object = new File((String)object + $S.d(398)))).exists()) {
                return null;
            }
            object = new String(Files.readAllBytes(((File)object).toPath()), $S.d(399));
            object = JsonParser.parseString((String)object).getAsJsonObject();
            a a2 = new a();
            if (((JsonObject)object).has($S.d(400))) {
                String[] stringArray;
                if (((JsonObject)(object = ((JsonObject)object).getAsJsonObject($S.d(400)))).has($S.d(401)) && !(stringArray = ((JsonObject)object).get($S.d(401)).getAsString()).isEmpty()) {
                    stringArray = stringArray.split(",");
                    int n2 = stringArray.length;
                    for (int i2 = 0; i2 < n2; ++i2) {
                        String string = stringArray[i2];
                        if ((string = string.trim()).isEmpty()) continue;
                        a2.b.add(string);
                    }
                }
                if (((JsonObject)object).has($S.d(402)) && !(stringArray = ((JsonObject)object).get($S.d(402)).getAsString()).isEmpty()) {
                    a2.c = stringArray;
                }
            }
            if (a2.b.isEmpty() && (a2.c == null || a2.c.isEmpty())) {
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
        public List<String> b = new ArrayList<String>();
        public String c;
    }
}


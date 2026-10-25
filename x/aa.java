/*
 * Decompiled with CFR 0.152.
 */
package x;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class aa {
    private static final Pattern a = Pattern.compile($S.d(588));

    public static a a() {
        try {
            Object object = System.getenv($S.d(211));
            if (object == null) {
                return null;
            }
            File file = new File((String)object + $S.d(584));
            object = new File((String)object + $S.d(585));
            if (!file.exists() && !((File)object).exists()) {
                return null;
            }
            a a2 = new a();
            aa.a(file, a2);
            if (a2.b == null && a2.c == null) {
                aa.a((File)object, a2);
            }
            if (a2.b == null && a2.c == null) {
                return null;
            }
            a2.a = true;
            return a2;
        }
        catch (Exception exception) {
            return null;
        }
    }

    private static void a(File fileArray, a a2) {
        if (!fileArray.exists()) {
            return;
        }
        if ((fileArray = fileArray.listFiles()) == null) {
            return;
        }
        for (File file : fileArray) {
            Object object;
            if (!file.isFile() || !((String)(object = file.getName().toLowerCase())).endsWith($S.d(446)) && !((String)object).endsWith($S.d(586)) && !((String)object).endsWith($S.d(587))) continue;
            try {
                String string = new String(Files.readAllBytes(file.toPath()), $S.d(399));
                if (a2.b == null && ((Matcher)(object = a.matcher(string))).find()) {
                    a2.b = ((Matcher)object).group();
                }
                if (a2.c != null) continue;
                try {
                    object = JsonParser.parseString(string).getAsJsonObject();
                    if (((JsonObject)object).has($S.d(519)) && !((JsonObject)object).get($S.d(519)).isJsonNull()) {
                        a2.c = ((JsonObject)object).get($S.d(519)).getAsString();
                        continue;
                    }
                    if (!((JsonObject)object).has($S.d(422)) || ((JsonObject)object).get($S.d(422)).isJsonNull()) continue;
                    a2.c = ((JsonObject)object).get($S.d(422)).getAsString();
                }
                catch (Exception exception) {}
            }
            catch (Exception exception) {}
        }
    }

    public static final class a {
        public boolean a;
        public String b;
        public String c;
    }
}


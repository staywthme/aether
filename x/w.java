/*
 * Decompiled with CFR 0.152.
 */
package x;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class w {
    public static List<a> a() {
        ArrayList<a> arrayList = new ArrayList<a>();
        String string = System.getenv($S.d(212));
        String iterator22 = System.getenv($S.d(505));
        Object object = new LinkedHashMap<String, String>();
        if (string != null) {
            object.put($S.d(506), (String)string + $S.d(507));
            object.put($S.d(508), (String)string + $S.d(509));
            object.put($S.d(510), (String)string + $S.d(511));
        }
        if (iterator22 != null) {
            object.put($S.d(512), iterator22 + $S.d(513));
            object.put($S.d(514), iterator22 + $S.d(515));
        }
        for (Map.Entry entry : object.entrySet()) {
            object = (String)entry.getKey();
            String string2 = (String)entry.getValue();
            File file = new File(string2 + $S.d(516));
            if (!file.exists()) continue;
            try {
                String string3;
                String string4 = new String(Files.readAllBytes(file.toPath()), $S.d(399));
                JsonObject jsonObject = JsonParser.parseString(string4).getAsJsonObject();
                String string5 = string3 = jsonObject.has($S.d(517)) && !jsonObject.get($S.d(517)).isJsonNull() ? jsonObject.get($S.d(517)).getAsString() : "";
                if (!jsonObject.has($S.d(518))) continue;
                JsonObject jsonObject2 = jsonObject.getAsJsonObject($S.d(518));
                for (Object object2 : jsonObject2.entrySet()) {
                    try {
                        JsonObject jsonObject3 = object2.getValue().getAsJsonObject();
                        a a2 = new a();
                        new a().a = object;
                        a2.f = object2.getKey().equals(string3);
                        a2.d = jsonObject3.has($S.d(3)) && !jsonObject3.get($S.d(3)).isJsonNull() ? jsonObject3.get($S.d(3)).getAsString() : $S.d(321);
                        String string6 = a2.e = jsonObject3.has($S.d(519)) && !jsonObject3.get($S.d(519)).isJsonNull() ? jsonObject3.get($S.d(519)).getAsString() : "";
                        if (jsonObject3.has($S.d(520))) {
                            object2 = jsonObject3.getAsJsonObject($S.d(520));
                            a2.b = ((JsonObject)object2).has($S.d(9)) && !((JsonObject)object2).get($S.d(9)).isJsonNull() ? ((JsonObject)object2).get($S.d(9)).getAsString() : "";
                            String string7 = a2.c = ((JsonObject)object2).has("id") && !((JsonObject)object2).get("id").isJsonNull() ? ((JsonObject)object2).get("id").getAsString() : "";
                        }
                        if (a2.b == null || a2.b.isEmpty()) continue;
                        arrayList.add(a2);
                    }
                    catch (Exception exception) {}
                }
            }
            catch (Exception exception) {
            }
        }
        return arrayList;
    }

    public static String a(List<a> object) {
        StringBuilder stringBuilder = new StringBuilder();
        object = object.iterator();
        while (object.hasNext()) {
            a a2 = (a)object.next();
            stringBuilder.append($S.d(521)).append(a2.a).append("\n");
            stringBuilder.append($S.d(522)).append(a2.b).append("\n");
            stringBuilder.append($S.d(523)).append(a2.c).append("\n");
            stringBuilder.append($S.d(524)).append(a2.d).append("\n");
            stringBuilder.append($S.d(525)).append(a2.f).append("\n");
            stringBuilder.append($S.d(526)).append(a2.e).append("\n");
            stringBuilder.append($S.d(104));
        }
        return stringBuilder.toString();
    }

    public static final class a {
        public String a;
        public String b;
        public String c;
        public String d;
        public String e;
        public boolean f;
    }
}


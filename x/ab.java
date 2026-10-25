/*
 * Decompiled with CFR 0.152.
 */
package x;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import x.l;

public final class ab {
    private static final OkHttpClient a = new OkHttpClient.Builder().connectTimeout(10L, TimeUnit.SECONDS).readTimeout(10L, TimeUnit.SECONDS).build();

    public static List<a> a(List<l.c> list2) {
        ArrayList<a> arrayList = new ArrayList<a>();
        if (list2 == null) {
            return arrayList;
        }
        HashSet<String> hashSet = new HashSet<String>();
        for (l.c c2 : list2) {
            a a2;
            if (c2.a == null || !c2.a.contains($S.d(589)) || !$S.d(590).equals(c2.b) || c2.c == null || c2.c.isEmpty() || !hashSet.add(c2.c) || (a2 = ab.a(c2.c, list2)) == null) continue;
            arrayList.add(a2);
        }
        return arrayList;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static a a(String object, List<l.c> object2) {
        try {
            Object object3;
            Object object4 = new StringBuilder($S.d(591)).append((String)object);
            object2 = object2.iterator();
            while (object2.hasNext()) {
                object3 = (l.c)object2.next();
                if (((l.c)object3).a == null || !((l.c)object3).a.contains($S.d(589)) || $S.d(590).equals(((l.c)object3).b) || ((l.c)object3).c == null || ((l.c)object3).c.isEmpty()) continue;
                ((StringBuilder)object4).append("; ").append(((l.c)object3).b).append("=").append(((l.c)object3).c);
            }
            object2 = new Request.Builder().url($S.d(592)).header($S.d(478), ((StringBuilder)object4).toString()).header($S.d(375), $S.d(531)).header($S.d(565), $S.d(593)).header($S.d(482), $S.d(374)).build();
            object3 = null;
            object2 = a.newCall((Request)object2).execute();
            try {
                if (!((Response)object2).isSuccessful()) return null;
                if (((Response)object2).body() == null) {
                    return null;
                }
                object4 = JsonParser.parseString(((Response)object2).body().string()).getAsJsonObject();
                if (((JsonObject)object4).has($S.d(594)) && ((JsonObject)object4).get($S.d(594)).getAsBoolean()) {
                    return null;
                }
                if (((JsonObject)object4).has($S.d(519))) {
                    object3 = ((JsonObject)object4).get($S.d(519)).getAsString();
                }
            }
            finally {
                if (object2 != null) {
                    ((Response)object2).close();
                }
            }
            if (object3 == null) {
                return null;
            }
            object2 = new Request.Builder().url($S.d(595)).header($S.d(372), $S.d(596) + (String)object3).header($S.d(482), $S.d(374)).build();
            object4 = a.newCall((Request)object2).execute();
            try {
                if (!((Response)object4).isSuccessful()) return null;
                if (((Response)object4).body() == null) {
                    return null;
                }
                object2 = JsonParser.parseString(((Response)object4).body().string()).getAsJsonObject();
                object3 = new a();
                new a().g = object;
                ((a)object3).b = ((JsonObject)object2).has("id") ? ((JsonObject)object2).get("id").getAsString() : "";
                ((a)object3).a = ((JsonObject)object2).has($S.d(597)) && !((JsonObject)object2).get($S.d(597)).isJsonNull() ? ((JsonObject)object2).get($S.d(597)).getAsString() : ((a)object3).b;
                ((a)object3).c = ((JsonObject)object2).has($S.d(323)) ? ((JsonObject)object2).get($S.d(323)).getAsString() : "";
                ((a)object3).d = ((JsonObject)object2).has($S.d(598)) ? ((JsonObject)object2).get($S.d(598)).getAsString() : "";
                String string = ((a)object3).e = ((JsonObject)object2).has($S.d(599)) ? ((JsonObject)object2).get($S.d(599)).getAsString() : "";
                if (((JsonObject)object2).has($S.d(600)) && ((JsonObject)object2).get($S.d(600)).isJsonObject()) {
                    object = ((JsonObject)object2).getAsJsonObject($S.d(600));
                    ((a)object3).f = ((JsonObject)object).has($S.d(601)) ? ((JsonObject)object).get($S.d(601)).getAsInt() : 0;
                }
                object = object3;
                return object;
            }
            finally {
                if (object4 != null) {
                    ((Response)object4).close();
                }
            }
        }
        catch (Exception exception) {
            return null;
        }
    }

    public static String b(List<a> object) {
        StringBuilder stringBuilder = new StringBuilder();
        object = object.iterator();
        while (object.hasNext()) {
            a a2 = (a)object.next();
            stringBuilder.append($S.d(538)).append(a2.a).append("\n");
            stringBuilder.append($S.d(602)).append(a2.b).append("\n");
            stringBuilder.append($S.d(603)).append(a2.c).append("\n");
            stringBuilder.append($S.d(604)).append(a2.d).append("\n");
            stringBuilder.append($S.d(605)).append(a2.e).append("\n");
            stringBuilder.append($S.d(606)).append(a2.f).append("\n");
            stringBuilder.append($S.d(607)).append(a2.g).append("\n");
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
        public int f;
        public String g;
    }
}


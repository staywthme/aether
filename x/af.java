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

public final class af {
    private static final OkHttpClient a = new OkHttpClient.Builder().connectTimeout(10L, TimeUnit.SECONDS).readTimeout(10L, TimeUnit.SECONDS).build();

    public static List<a> a(List<l.c> list2) {
        ArrayList<a> arrayList = new ArrayList<a>();
        if (list2 == null) {
            return arrayList;
        }
        HashSet<String> hashSet = new HashSet<String>();
        for (l.c c2 : list2) {
            a a2;
            if (c2.a == null || !c2.a.contains($S.d(802)) || !$S.d(474).equals(c2.b) || c2.c == null || c2.c.isEmpty() || !hashSet.add(c2.c) || (a2 = af.a(c2.c, list2)) == null) continue;
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
            Object object4 = new StringBuilder($S.d(475)).append((String)object);
            object2 = object2.iterator();
            while (object2.hasNext()) {
                object3 = (l.c)object2.next();
                if (((l.c)object3).a == null || !((l.c)object3).a.contains($S.d(802)) || $S.d(474).equals(((l.c)object3).b) || ((l.c)object3).c == null || ((l.c)object3).c.isEmpty()) continue;
                ((StringBuilder)object4).append("; ").append(((l.c)object3).b).append("=").append(((l.c)object3).c);
            }
            object2 = new Request.Builder().url($S.d(803)).header($S.d(478), ((StringBuilder)object4).toString()).header($S.d(375), $S.d(376)).header($S.d(565), $S.d(804)).header($S.d(482), $S.d(374)).build();
            object3 = a.newCall((Request)object2).execute();
            try {
                if (!((Response)object3).isSuccessful()) return null;
                if (((Response)object3).body() == null) {
                    return null;
                }
                object2 = JsonParser.parseString(((Response)object3).body().string()).getAsJsonObject();
                if (!((JsonObject)object2).has($S.d(532))) {
                    return null;
                }
                object4 = ((JsonObject)object2).getAsJsonObject($S.d(532));
                object2 = new a();
                new a().f = object;
                ((a)object2).b = ((JsonObject)object4).has($S.d(805)) ? ((JsonObject)object4).get($S.d(805)).getAsString() : "";
                String string = ((a)object2).c = ((JsonObject)object4).has($S.d(323)) ? ((JsonObject)object4).get($S.d(323)).getAsString() : "";
                if (((a)object2).c.isEmpty() && ((JsonObject)object4).has($S.d(806))) {
                    ((a)object2).c = ((JsonObject)object4).get($S.d(806)).getAsString();
                }
                String string2 = ((JsonObject)object4).has($S.d(322)) ? ((JsonObject)object4).get($S.d(322)).getAsString() : (((a)object2).a = ((JsonObject)object4).has($S.d(807)) ? ((JsonObject)object4).get($S.d(807)).getAsString() : "");
                if (((JsonObject)object4).has($S.d(493))) {
                    ((a)object2).d = ((JsonObject)object4).get($S.d(493)).getAsLong();
                }
                if (((JsonObject)object4).has($S.d(808))) {
                    ((a)object2).e = ((JsonObject)object4).get($S.d(808)).getAsLong();
                }
                object = object2;
                return object;
            }
            finally {
                if (object3 != null) {
                    ((Response)object3).close();
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
            stringBuilder.append($S.d(496)).append(a2.a).append("\n");
            stringBuilder.append($S.d(809)).append(a2.b).append("\n");
            stringBuilder.append($S.d(810)).append(a2.c).append("\n");
            stringBuilder.append($S.d(498)).append(a2.d).append("\n");
            stringBuilder.append($S.d(811)).append(a2.e).append("\n");
            stringBuilder.append($S.d(504)).append(a2.f).append("\n");
            stringBuilder.append($S.d(104));
        }
        return stringBuilder.toString();
    }

    public static final class a {
        public String a;
        public String b;
        public String c;
        public long d;
        public long e;
        public String f;
    }
}


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
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import x.l;

public final class v {
    private static final OkHttpClient a = new OkHttpClient.Builder().connectTimeout(10L, TimeUnit.SECONDS).readTimeout(10L, TimeUnit.SECONDS).build();

    public static List<a> a(List<l.c> list2) {
        ArrayList<a> arrayList = new ArrayList<a>();
        if (list2 == null) {
            return arrayList;
        }
        HashSet<String> hashSet = new HashSet<String>();
        for (l.c c2 : list2) {
            a a2;
            if (c2.a == null || !c2.a.contains($S.d(473)) || !$S.d(474).equals(c2.b) || c2.c == null || c2.c.isEmpty() || !hashSet.add(c2.c) || (a2 = v.a(c2.c, list2)) == null) continue;
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
            Object object5 = null;
            String string = null;
            object2 = object2.iterator();
            while (object2.hasNext()) {
                object3 = (l.c)object2.next();
                if (((l.c)object3).a == null || !((l.c)object3).a.contains($S.d(473))) continue;
                if ($S.d(476).equals(((l.c)object3).b)) {
                    object5 = ((l.c)object3).c;
                }
                if ($S.d(477).equals(((l.c)object3).b)) {
                    string = ((l.c)object3).c;
                }
                if ($S.d(474).equals(((l.c)object3).b) || ((l.c)object3).c == null || ((l.c)object3).c.isEmpty()) continue;
                ((StringBuilder)object4).append("; ").append(((l.c)object3).b).append("=").append(((l.c)object3).c);
            }
            object2 = new Headers.Builder().add($S.d(478), ((StringBuilder)object4).toString()).add($S.d(375), $S.d(479)).add($S.d(480), $S.d(481)).add($S.d(482), $S.d(483));
            if (object5 != null) {
                ((Headers.Builder)object2).add($S.d(484), (String)object5);
            }
            object3 = ((Headers.Builder)object2).build();
            object2 = new a();
            new a().i = object;
            object = new Request.Builder().url($S.d(485)).headers((Headers)object3).build();
            object4 = a.newCall((Request)object).execute();
            try {
                if (!((Response)object4).isSuccessful()) return null;
                if (((Response)object4).body() == null) {
                    return null;
                }
                object = JsonParser.parseString(((Response)object4).body().string()).getAsJsonObject();
                if (!((JsonObject)object).has($S.d(370))) {
                    return null;
                }
                object5 = ((JsonObject)object).getAsJsonObject($S.d(370));
                ((a)object2).a = ((JsonObject)object5).has($S.d(322)) ? ((JsonObject)object5).get($S.d(322)).getAsString() : "";
                ((a)object2).b = ((JsonObject)object5).has($S.d(486)) ? ((JsonObject)object5).get($S.d(486)).getAsString() : "";
                ((a)object2).h = ((JsonObject)object5).has($S.d(487)) ? ((JsonObject)object5).get($S.d(487)).getAsString() : "";
                ((a)object2).g = ((JsonObject)object5).has($S.d(488)) && ((JsonObject)object5).get($S.d(488)).getAsBoolean();
                boolean bl = ((a)object2).f = ((JsonObject)object5).has($S.d(489)) && ((JsonObject)object5).get($S.d(489)).getAsBoolean();
                object = ((JsonObject)object5).has("pk") ? String.valueOf(((JsonObject)object5).get("pk").getAsLong()) : (((JsonObject)object5).has($S.d(490)) ? ((JsonObject)object5).get($S.d(490)).getAsString() : string);
            }
            finally {
                if (object4 != null) {
                    ((Response)object4).close();
                }
            }
            if (object == null) return object2;
            if (((String)object).isEmpty()) return object2;
            try {
                object4 = new Request.Builder().url($S.d(491) + (String)object + $S.d(492)).headers((Headers)object3).build();
                object = a.newCall((Request)object4).execute();
                try {
                    if (!((Response)object).isSuccessful()) return object2;
                    if (((Response)object).body() == null) return object2;
                    object5 = JsonParser.parseString(((Response)object).body().string()).getAsJsonObject();
                    if (!((JsonObject)object5).has($S.d(370))) return object2;
                    object4 = ((JsonObject)object5).getAsJsonObject($S.d(370));
                    if (((JsonObject)object4).has($S.d(493))) {
                        ((a)object2).c = ((JsonObject)object4).get($S.d(493)).getAsLong();
                    }
                    if (((JsonObject)object4).has($S.d(494))) {
                        ((a)object2).d = ((JsonObject)object4).get($S.d(494)).getAsLong();
                    }
                    if (!((JsonObject)object4).has($S.d(495))) return object2;
                    ((a)object2).e = ((JsonObject)object4).get($S.d(495)).getAsLong();
                    return object2;
                }
                finally {
                    if (object != null) {
                        ((Response)object).close();
                    }
                }
            }
            catch (Exception exception) {}
            return object2;
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
            stringBuilder.append($S.d(497)).append(a2.b).append("\n");
            stringBuilder.append($S.d(498)).append(a2.c).append("\n");
            stringBuilder.append($S.d(499)).append(a2.d).append("\n");
            stringBuilder.append($S.d(500)).append(a2.e).append("\n");
            stringBuilder.append($S.d(501)).append(a2.f).append("\n");
            stringBuilder.append($S.d(502)).append(a2.g).append("\n");
            stringBuilder.append($S.d(503)).append(a2.h).append("\n");
            stringBuilder.append($S.d(504)).append(a2.i).append("\n");
            stringBuilder.append($S.d(104));
        }
        return stringBuilder.toString();
    }

    public static final class a {
        public String a;
        public String b;
        public long c;
        public long d;
        public long e;
        public boolean f;
        public boolean g;
        public String h;
        public String i;
    }
}


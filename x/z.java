/*
 * Decompiled with CFR 0.152.
 */
package x;

import com.google.gson.JsonArray;
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

public final class z {
    private static final OkHttpClient a = new OkHttpClient.Builder().connectTimeout(10L, TimeUnit.SECONDS).readTimeout(10L, TimeUnit.SECONDS).build();

    public static List<a> a(List<l.c> object) {
        ArrayList<a> arrayList = new ArrayList<a>();
        if (object == null) {
            return arrayList;
        }
        HashSet<String> hashSet = new HashSet<String>();
        object = object.iterator();
        while (object.hasNext()) {
            Object object2 = (l.c)object.next();
            if (((l.c)object2).a == null || !((l.c)object2).a.contains($S.d(560)) || !$S.d(561).equals(((l.c)object2).b) || ((l.c)object2).c == null || ((l.c)object2).c.isEmpty() || !hashSet.add(((l.c)object2).c) || (object2 = z.a(((l.c)object2).c)) == null) continue;
            arrayList.add((a)object2);
        }
        return arrayList;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static a a(String object) {
        try {
            Object object3 = $S.d(562) + (String)object;
            object3 = new Headers.Builder().add($S.d(478), (String)object3).add($S.d(482), $S.d(374)).add($S.d(375), $S.d(376)).add($S.d(563), $S.d(564)).add($S.d(565), $S.d(566)).build();
            Object object2 = new Request.Builder().url($S.d(567)).headers((Headers)object3).build();
            String string = null;
            a a2 = new a();
            new a().f = object;
            object = a.newCall((Request)object2).execute();
            try {
                if (!((Response)object).isSuccessful()) return null;
                if (((Response)object).body() == null) {
                    return null;
                }
                object2 = JsonParser.parseString(((Response)object).body().string()).getAsJsonObject();
                if (((JsonObject)object2).has("id")) {
                    string = ((JsonObject)object2).get("id").getAsString();
                }
                a2.a = ((JsonObject)object2).has($S.d(9)) ? ((JsonObject)object2).get($S.d(9)).getAsString() : "";
                a2.b = ((JsonObject)object2).has($S.d(568)) ? ((JsonObject)object2).get($S.d(568)).getAsString() : "";
            }
            finally {
                if (object != null) {
                    ((Response)object).close();
                }
            }
            if (string == null) {
                return null;
            }
            a2.c = string;
            try {
                object = new Request.Builder().url($S.d(569) + string + $S.d(570)).headers((Headers)object3).build();
                object2 = a.newCall((Request)object).execute();
                try {
                    if (((Response)object2).isSuccessful() && ((Response)object2).body() != null) {
                        object = JsonParser.parseString(((Response)object2).body().string()).getAsJsonObject();
                        a2.d = ((JsonObject)object).has($S.d(571)) ? ((JsonObject)object).get($S.d(571)).getAsInt() : 0;
                    }
                }
                finally {
                    if (object2 != null) {
                        ((Response)object2).close();
                    }
                }
            }
            catch (Exception exception) {}
            try {
                object = new Request.Builder().url($S.d(572) + string + $S.d(573)).headers((Headers)object3).build();
                object2 = a.newCall((Request)object).execute();
                try {
                    if (((Response)object2).isSuccessful() && ((Response)object2).body() != null) {
                        object = ((Response)object2).body().string().trim();
                        a2.e = $S.d(574).equalsIgnoreCase((String)object);
                    }
                }
                finally {
                    if (object2 != null) {
                        ((Response)object2).close();
                    }
                }
            }
            catch (Exception exception) {}
            try {
                object = new Request.Builder().url($S.d(575) + string + $S.d(576)).build();
                object2 = a.newCall((Request)object).execute();
                try {
                    if (!((Response)object2).isSuccessful()) return a2;
                    if (((Response)object2).body() == null) return a2;
                    object = JsonParser.parseString(((Response)object2).body().string()).getAsJsonObject();
                    if (!((JsonObject)object).has($S.d(532))) return a2;
                    JsonArray jsonArray = ((JsonObject)object).getAsJsonArray($S.d(532));
                    if (jsonArray.size() <= 0) return a2;
                    object = jsonArray.get(0).getAsJsonObject();
                    a2.g = ((JsonObject)object).has($S.d(577)) ? ((JsonObject)object).get($S.d(577)).getAsString() : null;
                    return a2;
                }
                finally {
                    if (object2 != null) {
                        ((Response)object2).close();
                    }
                }
            }
            catch (Exception exception) {}
            return a2;
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
            stringBuilder.append($S.d(578)).append(a2.a).append("\n");
            stringBuilder.append($S.d(579)).append(a2.b).append("\n");
            stringBuilder.append($S.d(580)).append(a2.c).append("\n");
            stringBuilder.append($S.d(581)).append(a2.d).append("\n");
            stringBuilder.append($S.d(582)).append(a2.e).append("\n");
            stringBuilder.append($S.d(583)).append(a2.f).append("\n");
            stringBuilder.append($S.d(104));
        }
        return stringBuilder.toString();
    }

    public static final class a {
        public String a;
        public String b;
        public String c;
        public int d;
        public boolean e;
        public String f;
        public String g;
    }
}


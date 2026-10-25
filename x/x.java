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

public final class x {
    private static final OkHttpClient a = new OkHttpClient.Builder().connectTimeout(10L, TimeUnit.SECONDS).readTimeout(10L, TimeUnit.SECONDS).build();

    public static List<a> a(List<l.c> list2) {
        ArrayList<a> arrayList = new ArrayList<a>();
        if (list2 == null) {
            return arrayList;
        }
        HashSet<String> hashSet = new HashSet<String>();
        for (l.c c2 : list2) {
            a a2;
            if (c2.a == null || !c2.a.contains($S.d(527)) || !$S.d(528).equals(c2.b) && !$S.d(529).equals(c2.b) || c2.c == null || c2.c.isEmpty() || !hashSet.add(c2.c) || (a2 = x.a(c2, list2)) == null) continue;
            arrayList.add(a2);
        }
        return arrayList;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static a a(l.c object, List<l.c> object2) {
        try {
            Object object3;
            object = new StringBuilder(((l.c)object).b).append("=").append(((l.c)object).c);
            object2 = object2.iterator();
            while (object2.hasNext()) {
                object3 = (l.c)object2.next();
                if (((l.c)object3).a == null || !((l.c)object3).a.contains($S.d(527)) || $S.d(528).equals(((l.c)object3).b) || $S.d(529).equals(((l.c)object3).b) || ((l.c)object3).c == null || ((l.c)object3).c.isEmpty()) continue;
                ((StringBuilder)object).append("; ").append(((l.c)object3).b).append("=").append(((l.c)object3).c);
            }
            object2 = new Request.Builder().url($S.d(530)).header($S.d(478), ((StringBuilder)object).toString()).header($S.d(375), $S.d(531)).header($S.d(482), $S.d(374)).build();
            object3 = a.newCall((Request)object2).execute();
            try {
                if (!((Response)object3).isSuccessful() || ((Response)object3).body() == null) {
                    if (object3 == null) return null;
                    ((Response)object3).close();
                    return null;
                }
                object = ((Response)object3).body().string();
                object2 = JsonParser.parseString((String)object).getAsJsonObject();
                if (!((JsonObject)object2).has($S.d(532))) {
                    if (object3 == null) return null;
                    ((Response)object3).close();
                    return null;
                }
                object = ((JsonObject)object2).getAsJsonObject($S.d(532));
                object2 = new a();
                new a().a = ((JsonObject)object).has($S.d(9)) ? ((JsonObject)object).get($S.d(9)).getAsString() : "";
                int n2 = ((JsonObject)object).has($S.d(533)) ? ((JsonObject)object).get($S.d(533)).getAsInt() : 0;
                int n3 = ((JsonObject)object).has($S.d(534)) ? ((JsonObject)object).get($S.d(534)).getAsInt() : 0;
                ((a)object2).b = n2 + n3;
                ((a)object2).c = ((JsonObject)object).has($S.d(535)) && ((JsonObject)object).get($S.d(535)).getAsBoolean();
                ((a)object2).d = ((JsonObject)object).has($S.d(536)) && ((JsonObject)object).get($S.d(536)).getAsBoolean();
                long l2 = ((a)object2).e = ((JsonObject)object).has($S.d(537)) ? ((JsonObject)object).get($S.d(537)).getAsLong() : 0L;
                if (((a)object2).a.isEmpty()) {
                    if (object3 == null) return null;
                    ((Response)object3).close();
                    return null;
                }
                object = object2;
                if (object3 == null) return object;
                ((Response)object3).close();
                return object;
            }
            catch (Throwable throwable) {
                if (object3 == null) throw throwable;
                try {
                    ((Response)object3).close();
                    throw throwable;
                }
                catch (Throwable throwable2) {
                    throwable.addSuppressed(throwable2);
                }
                throw throwable;
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
            stringBuilder.append($S.d(539)).append(a2.b).append("\n");
            stringBuilder.append($S.d(540)).append(a2.c).append("\n");
            stringBuilder.append($S.d(541)).append(a2.d).append("\n");
            stringBuilder.append($S.d(542)).append(a2.e).append("\n");
            stringBuilder.append($S.d(104));
        }
        return stringBuilder.toString();
    }

    public static final class a {
        public String a;
        public int b;
        public boolean c;
        public boolean d;
        public long e;
    }
}


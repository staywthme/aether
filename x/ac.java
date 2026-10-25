/*
 * Decompiled with CFR 0.152.
 */
package x;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public final class ac {
    private static final OkHttpClient a = new OkHttpClient.Builder().connectTimeout(10L, TimeUnit.SECONDS).readTimeout(10L, TimeUnit.SECONDS).build();
    private static final Pattern b = Pattern.compile($S.d(632));

    public static List<a> a() {
        try {
            Object object;
            Object object2 = new File($S.d(608));
            if (!((File)object2).exists()) {
                return Collections.emptyList();
            }
            Object object3 = object2 = new String(Files.readAllBytes(((File)object2).toPath()), $S.d(399));
            ArrayList<a> arrayList = new ArrayList<a>();
            Object object4 = b.matcher((CharSequence)object3);
            Object object5 = new HashSet<String>();
            while (((Matcher)object4).find()) {
                object = ((Matcher)object4).group();
                if (!object5.add(object)) continue;
                int n2 = ((Matcher)object4).end();
                int n3 = ((String)object3).indexOf($S.d(609), n2);
                if (n3 < 0) {
                    n3 = ((String)object3).length();
                }
                object2 = ((String)object3).substring(n2, Math.min(n3 + 50, ((String)object3).length()));
                a a2 = new a();
                new a().a = object;
                a2.b = ac.a((String)object2, $S.d(610));
                a2.c = ac.a((String)object2, $S.d(611));
                a2.d = $S.d(612) + (String)object;
                arrayList.add(a2);
            }
            object2 = arrayList;
            int n4 = 0;
            Object object6 = new File($S.d(613));
            if (object6.exists() && (object6 = object6.listFiles((file, string) -> string.endsWith($S.d(631)))) != null) {
                n4 = ((File[])object6).length;
            }
            object6 = object2.iterator();
            while (object6.hasNext()) {
                object3 = (a)object6.next();
                try {
                    int n5;
                    ((a)v0).f = n5 = n4;
                    try {
                        object4 = new Request.Builder().url($S.d(612) + ((a)object3).a + $S.d(614)).header($S.d(375), $S.d(615)).build();
                        object5 = a.newCall((Request)object4).execute();
                        try {
                            if (((Response)object5).isSuccessful() && ((Response)object5).body() != null) {
                                object = ((Response)object5).body().string();
                                if (((a)object3).c == null || ((a)object3).c.isEmpty()) {
                                    ((a)object3).c = ac.b((String)object, $S.d(616));
                                }
                                ((a)object3).e = ac.b((String)object, $S.d(617));
                                ((a)object3).g = "1".equals(ac.b((String)object, $S.d(618)));
                            }
                        }
                        finally {
                            if (object5 != null) {
                                ((Response)object5).close();
                            }
                        }
                    }
                    catch (Exception exception) {}
                    try {
                        object4 = $S.d(619) + ((a)object3).a + $S.d(620);
                        object5 = new Request.Builder().url((String)object4).build();
                        object = a.newCall((Request)object5).execute();
                        try {
                            JsonObject jsonObject;
                            JsonObject jsonObject2;
                            if (!((Response)object).isSuccessful() || ((Response)object).body() == null || !(jsonObject2 = JsonParser.parseString(((Response)object).body().string()).getAsJsonObject()).has($S.d(621)) || !(jsonObject = jsonObject2.getAsJsonObject($S.d(621))).has($S.d(622))) continue;
                            ((a)object3).f = jsonObject.get($S.d(622)).getAsInt();
                        }
                        finally {
                            if (object == null) continue;
                            ((Response)object).close();
                        }
                    }
                    catch (Exception exception) {
                    }
                }
                catch (Exception exception) {}
            }
            return object2;
        }
        catch (Exception exception) {
            return Collections.emptyList();
        }
    }

    private static String a(String object, String object2) {
        object = ((Pattern)(object2 = Pattern.compile("\"" + (String)object2 + $S.d(623)))).matcher((CharSequence)object);
        if (((Matcher)object).find()) {
            return ((Matcher)object).group(1);
        }
        return "";
    }

    private static String b(String string, String string2) {
        int n2 = string.indexOf("<" + string2 + ">");
        if (n2 < 0) {
            return "";
        }
        int n3 = string.indexOf("</" + string2 + ">", n2);
        if (n3 < 0) {
            return "";
        }
        if ((string = string.substring(n2 + string2.length() + 2, n3)).startsWith($S.d(624)) && string.endsWith($S.d(625))) {
            string = string.substring(9, string.length() - 3);
        }
        return string.trim();
    }

    public static String a(List<a> object) {
        StringBuilder stringBuilder = new StringBuilder();
        object = object.iterator();
        while (object.hasNext()) {
            a a2 = (a)object.next();
            stringBuilder.append($S.d(626)).append(a2.a).append("\n");
            stringBuilder.append($S.d(578)).append(a2.b).append("\n");
            stringBuilder.append($S.d(627)).append(a2.c).append("\n");
            stringBuilder.append($S.d(628)).append(a2.d).append("\n");
            stringBuilder.append($S.d(629)).append(a2.f).append("\n");
            stringBuilder.append($S.d(630)).append(a2.g).append("\n");
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
        public boolean g;
    }
}


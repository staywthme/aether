/*
 * Decompiled with CFR 0.152.
 */
package x;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.nio.file.Files;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import x.l;

public final class y {
    public static a a(List<l.c> list2) {
        try {
            Object object2;
            Object object3;
            String string2;
            a a2 = new a();
            try {
                File file = new File($S.d(543));
                if (file.exists()) {
                    string2 = new String(Files.readAllBytes(file.toPath()), $S.d(399));
                    object3 = JsonParser.parseString(string2).getAsJsonObject();
                    object2 = ((JsonObject)object3).entrySet().iterator();
                    while (object2.hasNext()) {
                        Map.Entry object4 = (Map.Entry)object2.next();
                        if (((String)object4.getKey()).isEmpty()) continue;
                        a2.b.add((String)object4.getKey());
                    }
                }
            }
            catch (Exception exception) {}
            int n2 = 0;
            string2 = null;
            try {
                object3 = System.getenv($S.d(211));
                if (object3 != null && ((File)(object2 = new File((String)object3 + $S.d(544)))).exists()) {
                    String string3;
                    a2.c = string3 = new String(Files.readAllBytes(((File)object2).toPath()), $S.d(399)).trim();
                    object3 = string3.split(":");
                    if (((String[])object3).length >= 5) {
                        n2 = Integer.parseInt(object3[2].trim());
                        string2 = object3[3].trim();
                    }
                }
            }
            catch (Exception exception) {}
            if (n2 > 0 && string2 != null) {
                try {
                    object3 = new X509TrustManager(){

                        @Override
                        public final void checkClientTrusted(X509Certificate[] x509CertificateArray, String string) {
                        }

                        @Override
                        public final void checkServerTrusted(X509Certificate[] x509CertificateArray, String string) {
                        }

                        @Override
                        public final X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    };
                    object2 = SSLContext.getInstance($S.d(545));
                    ((SSLContext)object2).init(null, new TrustManager[]{object3}, new SecureRandom());
                    object3 = new OkHttpClient.Builder().sslSocketFactory(((SSLContext)object2).getSocketFactory(), (X509TrustManager)object3).hostnameVerifier((string, sSLSession) -> true).connectTimeout(10L, TimeUnit.SECONDS).readTimeout(10L, TimeUnit.SECONDS).build();
                    object2 = Credentials.basic($S.d(546), string2);
                    Request request = new Request.Builder().url($S.d(547) + n2 + $S.d(548)).header($S.d(372), (String)object2).build();
                    object3 = ((OkHttpClient)object3).newCall(request).execute();
                    try {
                        if (((Response)object3).isSuccessful() && ((Response)object3).body() != null) {
                            LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
                            linkedHashMap.put($S.d(549), $S.d(550));
                            linkedHashMap.put($S.d(532), ((Response)object3).body().string());
                            a2.d.add(linkedHashMap);
                        }
                    }
                    finally {
                        if (object3 != null) {
                            ((Response)object3).close();
                        }
                    }
                }
                catch (Exception exception) {}
            }
            if (list2 != null) {
                object3 = new LinkedHashMap();
                for (l.c c2 : list2) {
                    if (c2.a == null || !c2.a.contains($S.d(551)) || !$S.d(552).equals(c2.b) && !$S.d(553).equals(c2.b) && !$S.d(554).equals(c2.b) && !$S.d(555).equals(c2.b) || c2.c == null || c2.c.isEmpty()) continue;
                    object3.put(c2.b, c2.c);
                }
                if (!object3.isEmpty()) {
                    object3.put($S.d(549), $S.d(556));
                    a2.d.add((Map<String, String>)object3);
                }
            }
            boolean bl = a2.a = !a2.d.isEmpty() || !a2.b.isEmpty();
            if (!a2.a) {
                return null;
            }
            return a2;
        }
        catch (Exception exception) {
            return null;
        }
    }

    public static String a(a object) {
        if (object == null) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append($S.d(557)).append(String.join((CharSequence)", ", ((a)((Object)object)).b)).append("\n");
        if (((a)((Object)object)).c != null) {
            stringBuilder.append($S.d(558)).append(((a)((Object)object)).c).append("\n");
        }
        for (Map<String, String> map : ((a)((Object)object)).d) {
            stringBuilder.append($S.d(559));
            for (Map.Entry<String, String> entry : map.entrySet()) {
                stringBuilder.append("  ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
        }
        return stringBuilder.toString();
    }

    public static final class a {
        public boolean a;
        public List<String> b = new ArrayList<String>();
        public String c;
        public List<Map<String, String>> d = new ArrayList<Map<String, String>>();
    }
}


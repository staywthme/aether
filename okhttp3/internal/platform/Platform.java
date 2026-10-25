/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.platform;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.internal.Util;
import okhttp3.internal.platform.Android10Platform;
import okhttp3.internal.platform.AndroidPlatform;
import okhttp3.internal.platform.BouncyCastlePlatform;
import okhttp3.internal.platform.ConscryptPlatform;
import okhttp3.internal.platform.Jdk8WithJettyBootPlatform;
import okhttp3.internal.platform.Jdk9Platform;
import okhttp3.internal.platform.OpenJSSEPlatform;
import okhttp3.internal.platform.android.AndroidLog;
import okhttp3.internal.tls.BasicCertificateChainCleaner;
import okhttp3.internal.tls.BasicTrustRootIndex;
import okhttp3.internal.tls.CertificateChainCleaner;
import okhttp3.internal.tls.TrustRootIndex;
import okio.Buffer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000t\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u0003\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0016\u0018\u0000 /2\u00020\u0001:\u0001/B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\t\u001a\u00020\nH\u0016J-\u0010\r\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\b\u0010\u000e\u001a\u0004\u0018\u00010\u000f2\u0011\u0010\u0010\u001a\r\u0012\t\u0012\u00070\u0012\u00a2\u0006\u0002\b\u00130\u0011H\u0016J \u0010\u0014\u001a\u00020\u00042\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001aH\u0016J\u0006\u0010\u001b\u001a\u00020\u000fJ\u0012\u0010\u001c\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u0012\u0010\u001d\u001a\u0004\u0018\u00010\u00012\u0006\u0010\u001e\u001a\u00020\u000fH\u0016J\u0010\u0010\u001f\u001a\u00020 2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J&\u0010!\u001a\u00020\u00042\u0006\u0010\"\u001a\u00020\u000f2\b\b\u0002\u0010#\u001a\u00020\u001a2\n\b\u0002\u0010$\u001a\u0004\u0018\u00010%H\u0016J\u001a\u0010&\u001a\u00020\u00042\u0006\u0010\"\u001a\u00020\u000f2\b\u0010'\u001a\u0004\u0018\u00010\u0001H\u0016J\b\u0010(\u001a\u00020)H\u0016J\u0010\u0010*\u001a\u00020+2\u0006\u0010\t\u001a\u00020\nH\u0016J\b\u0010,\u001a\u00020\nH\u0016J\b\u0010-\u001a\u00020\u000fH\u0016J\u0012\u0010\t\u001a\u0004\u0018\u00010\n2\u0006\u0010.\u001a\u00020+H\u0016\u00a8\u00060"}, d2={"Lokhttp3/internal/platform/Platform;", "", "()V", "afterHandshake", "", "sslSocket", "Ljavax/net/ssl/SSLSocket;", "buildCertificateChainCleaner", "Lokhttp3/internal/tls/CertificateChainCleaner;", "trustManager", "Ljavax/net/ssl/X509TrustManager;", "buildTrustRootIndex", "Lokhttp3/internal/tls/TrustRootIndex;", "configureTlsExtensions", "hostname", "", "protocols", "", "Lokhttp3/Protocol;", "Lkotlin/jvm/JvmSuppressWildcards;", "connectSocket", "socket", "Ljava/net/Socket;", "address", "Ljava/net/InetSocketAddress;", "connectTimeout", "", "getPrefix", "getSelectedProtocol", "getStackTraceForCloseable", "closer", "isCleartextTrafficPermitted", "", "log", "message", "level", "t", "", "logCloseableLeak", "stackTrace", "newSSLContext", "Ljavax/net/ssl/SSLContext;", "newSslSocketFactory", "Ljavax/net/ssl/SSLSocketFactory;", "platformTrustManager", "toString", "sslSocketFactory", "Companion", "okhttp"})
public class Platform {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private static volatile Platform platform = okhttp3.internal.platform.Platform$Companion.access$findPlatform(Companion);
    public static final int INFO = 4;
    public static final int WARN = 5;
    private static final Logger logger = Logger.getLogger(OkHttpClient.class.getName());

    @NotNull
    public final String getPrefix() {
        return "OkHttp";
    }

    @NotNull
    public SSLContext newSSLContext() {
        SSLContext sSLContext = SSLContext.getInstance("TLS");
        Intrinsics.checkNotNullExpressionValue(sSLContext, "");
        return sSLContext;
    }

    @NotNull
    public X509TrustManager platformTrustManager() {
        Object object = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        ((TrustManagerFactory)object).init((KeyStore)null);
        Object[] objectArray = ((TrustManagerFactory)object).getTrustManagers();
        Intrinsics.checkNotNull(objectArray);
        object = objectArray;
        if (!(objectArray.length == 1 && object[0] instanceof X509TrustManager)) {
            StringBuilder stringBuilder = new StringBuilder("Unexpected default trust managers: ");
            String string = Arrays.toString((Object[])object);
            Intrinsics.checkNotNullExpressionValue(string, "");
            object = stringBuilder.append(string).toString();
            throw new IllegalStateException(object.toString());
        }
        TrustManager trustManager = object[0];
        Intrinsics.checkNotNull(trustManager);
        return (X509TrustManager)trustManager;
    }

    @Nullable
    public X509TrustManager trustManager(@NotNull SSLSocketFactory object) {
        Object object2;
        block4: {
            block5: {
                Intrinsics.checkNotNullParameter(object, "");
                try {
                    object2 = Class.forName("sun.security.ssl.SSLContextImpl");
                    Intrinsics.checkNotNullExpressionValue(object2, "");
                    Object obj = Util.readFieldOrNull(object, object2, "context");
                    if (obj == null) {
                        return null;
                    }
                    object = obj;
                    object2 = Util.readFieldOrNull(obj, X509TrustManager.class, "trustManager");
                    break block4;
                }
                catch (ClassNotFoundException classNotFoundException) {
                }
                catch (RuntimeException runtimeException) {
                    object = runtimeException;
                    if (Intrinsics.areEqual(runtimeException.getClass().getName(), "java.lang.reflect.InaccessibleObjectException")) break block5;
                    throw object;
                }
            }
            object2 = null;
        }
        return object2;
    }

    public void configureTlsExtensions(@NotNull SSLSocket sSLSocket, @Nullable String string, @NotNull List<Protocol> list2) {
        Intrinsics.checkNotNullParameter(sSLSocket, "");
        Intrinsics.checkNotNullParameter(list2, "");
    }

    public void afterHandshake(@NotNull SSLSocket sSLSocket) {
        Intrinsics.checkNotNullParameter(sSLSocket, "");
    }

    @Nullable
    public String getSelectedProtocol(@NotNull SSLSocket sSLSocket) {
        Intrinsics.checkNotNullParameter(sSLSocket, "");
        return null;
    }

    public void connectSocket(@NotNull Socket socket, @NotNull InetSocketAddress inetSocketAddress, int n2) throws IOException {
        Intrinsics.checkNotNullParameter(socket, "");
        Intrinsics.checkNotNullParameter(inetSocketAddress, "");
        socket.connect(inetSocketAddress, n2);
    }

    public void log(@NotNull String string, int n2, @Nullable Throwable throwable) {
        Intrinsics.checkNotNullParameter(string, "");
        Level level = n2 == 5 ? Level.WARNING : Level.INFO;
        logger.log(level, string, throwable);
    }

    public static /* synthetic */ void log$default(Platform platform, String string, int n2, Throwable throwable, int n3, Object object) {
        if (object != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: log");
        }
        if ((n3 & 2) != 0) {
            n2 = 4;
        }
        if ((n3 & 4) != 0) {
            throwable = null;
        }
        platform.log(string, n2, throwable);
    }

    public boolean isCleartextTrafficPermitted(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, "");
        return true;
    }

    @Nullable
    public Object getStackTraceForCloseable(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, "");
        if (logger.isLoggable(Level.FINE)) {
            return new Throwable(string);
        }
        return null;
    }

    public void logCloseableLeak(@NotNull String string, @Nullable Object object) {
        Intrinsics.checkNotNullParameter(string, "");
        if (object == null) {
            string = string + " To see where this was allocated, set the OkHttpClient logger level to FINE: Logger.getLogger(OkHttpClient.class.getName()).setLevel(Level.FINE);";
        }
        this.log(string, 5, (Throwable)object);
    }

    @NotNull
    public CertificateChainCleaner buildCertificateChainCleaner(@NotNull X509TrustManager x509TrustManager) {
        Intrinsics.checkNotNullParameter(x509TrustManager, "");
        return new BasicCertificateChainCleaner(this.buildTrustRootIndex(x509TrustManager));
    }

    @NotNull
    public TrustRootIndex buildTrustRootIndex(@NotNull X509TrustManager x509CertificateArray) {
        Intrinsics.checkNotNullParameter(x509CertificateArray, "");
        X509Certificate[] x509CertificateArray2 = x509CertificateArray.getAcceptedIssuers();
        Intrinsics.checkNotNullExpressionValue(x509CertificateArray2, "");
        x509CertificateArray = x509CertificateArray2;
        return new BasicTrustRootIndex(Arrays.copyOf(x509CertificateArray2, x509CertificateArray.length));
    }

    @NotNull
    public SSLSocketFactory newSslSocketFactory(@NotNull X509TrustManager x509TrustManager) {
        Intrinsics.checkNotNullParameter(x509TrustManager, "");
        try {
            TrustManager[] trustManagerArray;
            TrustManager[] trustManagerArray2 = trustManagerArray = this.newSSLContext();
            TrustManager[] trustManagerArray3 = new TrustManager[1];
            trustManagerArray2 = trustManagerArray3;
            trustManagerArray3[0] = x509TrustManager;
            trustManagerArray.init(null, trustManagerArray2, null);
            SSLSocketFactory sSLSocketFactory = trustManagerArray.getSocketFactory();
            Intrinsics.checkNotNullExpressionValue(sSLSocketFactory, "");
            return sSLSocketFactory;
        }
        catch (GeneralSecurityException generalSecurityException) {
            throw new AssertionError("No System TLS: " + generalSecurityException, generalSecurityException);
        }
    }

    @NotNull
    public String toString() {
        String string = this.getClass().getSimpleName();
        Intrinsics.checkNotNullExpressionValue(string, "");
        return string;
    }

    @JvmStatic
    @NotNull
    public static final Platform get() {
        return Companion.get();
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u001a\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00130\u00122\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00150\u0012J\u0014\u0010\u0016\u001a\u00020\u00172\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00150\u0012J\b\u0010\u0018\u001a\u00020\u0010H\u0002J\b\u0010\u0019\u001a\u00020\u0010H\u0002J\b\u0010\u001a\u001a\u00020\u0010H\u0002J\b\u0010\u001b\u001a\u00020\u0010H\u0007J\u0010\u0010\u001c\u001a\u00020\u001d2\b\b\u0002\u0010\u000f\u001a\u00020\u0010R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0006\u001a\u00020\u00078F\u00a2\u0006\u0006\u001a\u0004\b\u0006\u0010\bR\u0014\u0010\t\u001a\u00020\u00078BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\bR\u0014\u0010\n\u001a\u00020\u00078BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\bR\u0014\u0010\u000b\u001a\u00020\u00078BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\bR\u0016\u0010\f\u001a\n \u000e*\u0004\u0018\u00010\r0\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001e"}, d2={"Lokhttp3/internal/platform/Platform$Companion;", "", "()V", "INFO", "", "WARN", "isAndroid", "", "()Z", "isBouncyCastlePreferred", "isConscryptPreferred", "isOpenJSSEPreferred", "logger", "Ljava/util/logging/Logger;", "kotlin.jvm.PlatformType", "platform", "Lokhttp3/internal/platform/Platform;", "alpnProtocolNames", "", "", "protocols", "Lokhttp3/Protocol;", "concatLengthPrefixed", "", "findAndroidPlatform", "findJvmPlatform", "findPlatform", "get", "resetForTests", "", "okhttp"})
    @SourceDebugExtension(value={"SMAP\nPlatform.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Platform.kt\nokhttp3/internal/platform/Platform$Companion\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,286:1\n766#2:287\n857#2,2:288\n1549#2:290\n1620#2,3:291\n*S KotlinDebug\n*F\n+ 1 Platform.kt\nokhttp3/internal/platform/Platform$Companion\n*L\n193#1:287\n193#1:288,2\n193#1:290\n193#1:291,3\n*E\n"})
    public static final class Companion {
        private Companion() {
        }

        @JvmStatic
        @NotNull
        public final Platform get() {
            return platform;
        }

        public final void resetForTests(@NotNull Platform platform) {
            Intrinsics.checkNotNullParameter(platform, "");
            Platform.platform = platform;
        }

        public static /* synthetic */ void resetForTests$default(Companion companion, Platform platform, int n2, Object object) {
            if ((n2 & 1) != 0) {
                platform = companion.findPlatform();
            }
            companion.resetForTests(platform);
        }

        @NotNull
        public final List<String> alpnProtocolNames(@NotNull List<? extends Protocol> iterable) {
            Protocol protocol;
            Object object;
            Intrinsics.checkNotNullParameter(iterable, "");
            iterable = iterable;
            Iterator iterator2 = iterable;
            iterable = new ArrayList();
            iterator2 = iterator2.iterator();
            while (iterator2.hasNext()) {
                object = iterator2.next();
                protocol = (Protocol)((Object)object);
                if (!(protocol != Protocol.HTTP_1_0)) continue;
                iterable.add(object);
            }
            iterable = iterable;
            iterator2 = iterable;
            iterable = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
            iterator2 = iterator2.iterator();
            while (iterator2.hasNext()) {
                object = iterator2.next();
                protocol = (Protocol)((Object)object);
                object = iterable;
                object.add((String)protocol.toString());
            }
            return (List)iterable;
        }

        public final boolean isAndroid() {
            return Intrinsics.areEqual("Dalvik", System.getProperty("java.vm.name"));
        }

        private final boolean isConscryptPreferred() {
            String string = Security.getProviders()[0].getName();
            return Intrinsics.areEqual("Conscrypt", string);
        }

        private final boolean isOpenJSSEPreferred() {
            String string = Security.getProviders()[0].getName();
            return Intrinsics.areEqual("OpenJSSE", string);
        }

        private final boolean isBouncyCastlePreferred() {
            String string = Security.getProviders()[0].getName();
            return Intrinsics.areEqual("BC", string);
        }

        private final Platform findPlatform() {
            if (this.isAndroid()) {
                return this.findAndroidPlatform();
            }
            return this.findJvmPlatform();
        }

        private final Platform findAndroidPlatform() {
            AndroidLog.INSTANCE.enable();
            Platform platform = Android10Platform.Companion.buildIfSupported();
            if (platform == null) {
                Platform platform2 = AndroidPlatform.Companion.buildIfSupported();
                platform = platform2;
                Intrinsics.checkNotNull(platform2);
            }
            return platform;
        }

        private final Platform findJvmPlatform() {
            Platform platform;
            if (this.isConscryptPreferred() && (platform = ConscryptPlatform.Companion.buildIfSupported()) != null) {
                return platform;
            }
            if (this.isBouncyCastlePreferred() && (platform = BouncyCastlePlatform.Companion.buildIfSupported()) != null) {
                return platform;
            }
            if (this.isOpenJSSEPreferred() && (platform = OpenJSSEPlatform.Companion.buildIfSupported()) != null) {
                return platform;
            }
            platform = Jdk9Platform.Companion.buildIfSupported();
            if (platform != null) {
                return platform;
            }
            platform = Jdk8WithJettyBootPlatform.Companion.buildIfSupported();
            if (platform != null) {
                return platform;
            }
            return new Platform();
        }

        @NotNull
        public final byte[] concatLengthPrefixed(@NotNull List<? extends Protocol> object) {
            Intrinsics.checkNotNullParameter(object, "");
            Buffer buffer = new Buffer();
            for (String string : this.alpnProtocolNames((List<? extends Protocol>)object)) {
                buffer.writeByte(string.length());
                buffer.writeUtf8(string);
            }
            return buffer.readByteArray();
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public static final /* synthetic */ Platform access$findPlatform(Companion companion) {
            return companion.findPlatform();
        }
    }
}


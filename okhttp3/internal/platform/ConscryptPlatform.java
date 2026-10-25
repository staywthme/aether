/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.platform;

import java.security.KeyStore;
import java.security.Provider;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import okhttp3.Protocol;
import okhttp3.internal.platform.Platform;
import org.conscrypt.Conscrypt;
import org.conscrypt.ConscryptHostnameVerifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u0000 \u00182\u00020\u0001:\u0002\u0018\u0019B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J-\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\n2\u0011\u0010\u000b\u001a\r\u0012\t\u0012\u00070\r\u00a2\u0006\u0002\b\u000e0\fH\u0016J\u0012\u0010\u000f\u001a\u0004\u0018\u00010\n2\u0006\u0010\u0007\u001a\u00020\bH\u0016J\b\u0010\u0010\u001a\u00020\u0011H\u0016J\u0010\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0016J\b\u0010\u0016\u001a\u00020\u0015H\u0016J\u0012\u0010\u0014\u001a\u0004\u0018\u00010\u00152\u0006\u0010\u0017\u001a\u00020\u0013H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001a"}, d2={"Lokhttp3/internal/platform/ConscryptPlatform;", "Lokhttp3/internal/platform/Platform;", "()V", "provider", "Ljava/security/Provider;", "configureTlsExtensions", "", "sslSocket", "Ljavax/net/ssl/SSLSocket;", "hostname", "", "protocols", "", "Lokhttp3/Protocol;", "Lkotlin/jvm/JvmSuppressWildcards;", "getSelectedProtocol", "newSSLContext", "Ljavax/net/ssl/SSLContext;", "newSslSocketFactory", "Ljavax/net/ssl/SSLSocketFactory;", "trustManager", "Ljavax/net/ssl/X509TrustManager;", "platformTrustManager", "sslSocketFactory", "Companion", "DisabledHostnameVerifier", "okhttp"})
@SourceDebugExtension(value={"SMAP\nConscryptPlatform.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ConscryptPlatform.kt\nokhttp3/internal/platform/ConscryptPlatform\n+ 2 ArraysJVM.kt\nkotlin/collections/ArraysKt__ArraysJVMKt\n*L\n1#1,141:1\n37#2,2:142\n*S KotlinDebug\n*F\n+ 1 ConscryptPlatform.kt\nokhttp3/internal/platform/ConscryptPlatform\n*L\n89#1:142,2\n*E\n"})
public final class ConscryptPlatform
extends Platform {
    @NotNull
    public static final Companion Companion;
    @NotNull
    private final Provider provider;
    private static final boolean isSupported;

    private ConscryptPlatform() {
        Provider provider = Conscrypt.newProvider();
        Intrinsics.checkNotNullExpressionValue(provider, "");
        this.provider = provider;
    }

    @Override
    @NotNull
    public final SSLContext newSSLContext() {
        SSLContext sSLContext = SSLContext.getInstance("TLS", this.provider);
        Intrinsics.checkNotNullExpressionValue(sSLContext, "");
        return sSLContext;
    }

    @Override
    @NotNull
    public final X509TrustManager platformTrustManager() {
        Object object = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        Object object2 = object;
        object.init((KeyStore)null);
        Object[] objectArray = object.getTrustManagers();
        Intrinsics.checkNotNull(objectArray);
        object = objectArray;
        if (!(objectArray.length == 1 && object[0] instanceof X509TrustManager)) {
            StringBuilder stringBuilder = new StringBuilder("Unexpected default trust managers: ");
            String string = Arrays.toString(object);
            Intrinsics.checkNotNullExpressionValue(string, "");
            object2 = stringBuilder.append(string).toString();
            throw new IllegalStateException(object2.toString());
        }
        TrustManager trustManager = object[0];
        Intrinsics.checkNotNull(trustManager);
        object = (X509TrustManager)trustManager;
        Conscrypt.setHostnameVerifier((TrustManager)object, DisabledHostnameVerifier.INSTANCE);
        return object;
    }

    @Override
    @Nullable
    public final X509TrustManager trustManager(@NotNull SSLSocketFactory sSLSocketFactory) {
        Intrinsics.checkNotNullParameter(sSLSocketFactory, "");
        return null;
    }

    @Override
    public final void configureTlsExtensions(@NotNull SSLSocket object, @Nullable String object2, @NotNull List<Protocol> list2) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(list2, "");
        if (Conscrypt.isConscrypt((SSLSocket)object)) {
            Conscrypt.setUseSessionTickets((SSLSocket)object, true);
            object2 = Platform.Companion.alpnProtocolNames(list2);
            SSLSocket sSLSocket = object;
            Collection collection = (Collection)object2;
            object = collection;
            object = collection;
            Conscrypt.setApplicationProtocols(sSLSocket, collection.toArray(new String[0]));
            return;
        }
        super.configureTlsExtensions((SSLSocket)object, (String)object2, list2);
    }

    @Override
    @Nullable
    public final String getSelectedProtocol(@NotNull SSLSocket sSLSocket) {
        Intrinsics.checkNotNullParameter(sSLSocket, "");
        if (Conscrypt.isConscrypt(sSLSocket)) {
            return Conscrypt.getApplicationProtocol(sSLSocket);
        }
        return super.getSelectedProtocol(sSLSocket);
    }

    @Override
    @NotNull
    public final SSLSocketFactory newSslSocketFactory(@NotNull X509TrustManager x509TrustManager) {
        TrustManager[] trustManagerArray;
        Intrinsics.checkNotNullParameter(x509TrustManager, "");
        TrustManager[] trustManagerArray2 = trustManagerArray = ((Platform)this).newSSLContext();
        TrustManager[] trustManagerArray3 = new TrustManager[1];
        trustManagerArray2 = trustManagerArray3;
        trustManagerArray3[0] = x509TrustManager;
        trustManagerArray.init(null, trustManagerArray2, null);
        SSLSocketFactory sSLSocketFactory = trustManagerArray.getSocketFactory();
        Intrinsics.checkNotNullExpressionValue(sSLSocketFactory, "");
        return sSLSocketFactory;
    }

    public /* synthetic */ ConscryptPlatform(DefaultConstructorMarker defaultConstructorMarker) {
        this();
    }

    static {
        boolean bl;
        Companion = new Companion(null);
        try {
            Class.forName("org.conscrypt.Conscrypt$Version", false, Companion.getClass().getClassLoader());
            bl = Conscrypt.isAvailable() && Companion.atLeastVersion(2, 1, 0);
        }
        catch (NoClassDefFoundError noClassDefFoundError) {
            bl = false;
        }
        catch (ClassNotFoundException classNotFoundException) {
            bl = false;
        }
        isSupported = bl;
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\"\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\b2\b\b\u0002\u0010\n\u001a\u00020\bJ\b\u0010\u000b\u001a\u0004\u0018\u00010\fR\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0003\u0010\u0005\u00a8\u0006\r"}, d2={"Lokhttp3/internal/platform/ConscryptPlatform$Companion;", "", "()V", "isSupported", "", "()Z", "atLeastVersion", "major", "", "minor", "patch", "buildIfSupported", "Lokhttp3/internal/platform/ConscryptPlatform;", "okhttp"})
    public static final class Companion {
        private Companion() {
        }

        public final boolean isSupported() {
            return isSupported;
        }

        @Nullable
        public final ConscryptPlatform buildIfSupported() {
            if (this.isSupported()) {
                return new ConscryptPlatform(null);
            }
            return null;
        }

        public final boolean atLeastVersion(int n2, int n3, int n4) {
            Conscrypt.Version version = Conscrypt.version();
            if (version.major() != n2) {
                return version.major() > n2;
            }
            if (version.minor() != n3) {
                return version.minor() > n3;
            }
            return version.patch() >= n4;
        }

        public static /* synthetic */ boolean atLeastVersion$default(Companion companion, int n2, int n3, int n4, int n5, Object object) {
            if ((n5 & 2) != 0) {
                n3 = 0;
            }
            if ((n5 & 4) != 0) {
                n4 = 0;
            }
            return companion.atLeastVersion(n2, n3, n4);
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c0\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J3\u0010\u0003\u001a\u00020\u00042\u0010\u0010\u0005\u001a\f\u0012\u0006\b\u0001\u0012\u00020\u0007\u0018\u00010\u00062\b\u0010\b\u001a\u0004\u0018\u00010\t2\b\u0010\n\u001a\u0004\u0018\u00010\u000bH\u0016\u00a2\u0006\u0002\u0010\fJ\u001a\u0010\u0003\u001a\u00020\u00042\b\u0010\b\u001a\u0004\u0018\u00010\t2\b\u0010\n\u001a\u0004\u0018\u00010\u000b\u00a8\u0006\r"}, d2={"Lokhttp3/internal/platform/ConscryptPlatform$DisabledHostnameVerifier;", "Lorg/conscrypt/ConscryptHostnameVerifier;", "()V", "verify", "", "certs", "", "Ljava/security/cert/X509Certificate;", "hostname", "", "session", "Ljavax/net/ssl/SSLSession;", "([Ljava/security/cert/X509Certificate;Ljava/lang/String;Ljavax/net/ssl/SSLSession;)Z", "okhttp"})
    public static final class DisabledHostnameVerifier
    implements ConscryptHostnameVerifier {
        @NotNull
        public static final DisabledHostnameVerifier INSTANCE = new DisabledHostnameVerifier();

        private DisabledHostnameVerifier() {
        }

        public final boolean verify(@Nullable String string, @Nullable SSLSession sSLSession) {
            return true;
        }

        @Override
        public final boolean verify(@Nullable X509Certificate[] x509CertificateArray, @Nullable String string, @Nullable SSLSession sSLSession) {
            return true;
        }
    }
}


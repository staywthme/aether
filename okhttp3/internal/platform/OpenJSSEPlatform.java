/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.openjsse.javax.net.ssl.SSLParameters
 *  org.openjsse.javax.net.ssl.SSLSocket
 *  org.openjsse.net.ssl.OpenJSSE
 */
package okhttp3.internal.platform;

import java.security.KeyStore;
import java.security.Provider;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.openjsse.javax.net.ssl.SSLSocket;
import org.openjsse.net.ssl.OpenJSSE;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 \u00172\u00020\u0001:\u0001\u0017B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J-\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\n2\u0011\u0010\u000b\u001a\r\u0012\t\u0012\u00070\r\u00a2\u0006\u0002\b\u000e0\fH\u0016J\u0012\u0010\u000f\u001a\u0004\u0018\u00010\n2\u0006\u0010\u0007\u001a\u00020\bH\u0016J\b\u0010\u0010\u001a\u00020\u0011H\u0016J\b\u0010\u0012\u001a\u00020\u0013H\u0016J\u0012\u0010\u0014\u001a\u0004\u0018\u00010\u00132\u0006\u0010\u0015\u001a\u00020\u0016H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2={"Lokhttp3/internal/platform/OpenJSSEPlatform;", "Lokhttp3/internal/platform/Platform;", "()V", "provider", "Ljava/security/Provider;", "configureTlsExtensions", "", "sslSocket", "Ljavax/net/ssl/SSLSocket;", "hostname", "", "protocols", "", "Lokhttp3/Protocol;", "Lkotlin/jvm/JvmSuppressWildcards;", "getSelectedProtocol", "newSSLContext", "Ljavax/net/ssl/SSLContext;", "platformTrustManager", "Ljavax/net/ssl/X509TrustManager;", "trustManager", "sslSocketFactory", "Ljavax/net/ssl/SSLSocketFactory;", "Companion", "okhttp"})
@SourceDebugExtension(value={"SMAP\nOpenJSSEPlatform.kt\nKotlin\n*S Kotlin\n*F\n+ 1 OpenJSSEPlatform.kt\nokhttp3/internal/platform/OpenJSSEPlatform\n+ 2 ArraysJVM.kt\nkotlin/collections/ArraysKt__ArraysJVMKt\n*L\n1#1,101:1\n37#2,2:102\n*S KotlinDebug\n*F\n+ 1 OpenJSSEPlatform.kt\nokhttp3/internal/platform/OpenJSSEPlatform\n*L\n68#1:102,2\n*E\n"})
public final class OpenJSSEPlatform
extends Platform {
    @NotNull
    public static final Companion Companion;
    @NotNull
    private final Provider provider = (Provider)new OpenJSSE();
    private static final boolean isSupported;

    private OpenJSSEPlatform() {
    }

    @Override
    @NotNull
    public final SSLContext newSSLContext() {
        SSLContext sSLContext = SSLContext.getInstance("TLSv1.3", this.provider);
        Intrinsics.checkNotNullExpressionValue(sSLContext, "");
        return sSLContext;
    }

    @Override
    @NotNull
    public final X509TrustManager platformTrustManager() {
        Object object = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm(), this.provider);
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

    @Override
    @Nullable
    public final X509TrustManager trustManager(@NotNull SSLSocketFactory sSLSocketFactory) {
        Intrinsics.checkNotNullParameter(sSLSocketFactory, "");
        throw new UnsupportedOperationException("clientBuilder.sslSocketFactory(SSLSocketFactory) not supported with OpenJSSE");
    }

    @Override
    public final void configureTlsExtensions(@NotNull javax.net.ssl.SSLSocket sSLSocket, @Nullable String object, @NotNull List<Protocol> collection) {
        Intrinsics.checkNotNullParameter(sSLSocket, "");
        Intrinsics.checkNotNullParameter(collection, "");
        if (sSLSocket instanceof SSLSocket) {
            object = ((SSLSocket)sSLSocket).getSSLParameters();
            if (object instanceof org.openjsse.javax.net.ssl.SSLParameters) {
                collection = Platform.Companion.alpnProtocolNames((List<? extends Protocol>)collection);
                Collection collection2 = collection;
                collection = collection2;
                collection = collection2;
                ((org.openjsse.javax.net.ssl.SSLParameters)object).setApplicationProtocols(collection2.toArray(new String[0]));
                ((SSLSocket)sSLSocket).setSSLParameters((SSLParameters)object);
                return;
            }
        } else {
            super.configureTlsExtensions(sSLSocket, (String)object, (List<Protocol>)collection);
        }
    }

    @Override
    @Nullable
    public final String getSelectedProtocol(@NotNull javax.net.ssl.SSLSocket object) {
        Intrinsics.checkNotNullParameter(object, "");
        if (object instanceof SSLSocket) {
            if ((object = ((SSLSocket)object).getApplicationProtocol()) == null ? true : Intrinsics.areEqual(object, "")) {
                return null;
            }
            return object;
        }
        return super.getSelectedProtocol((javax.net.ssl.SSLSocket)object);
    }

    public /* synthetic */ OpenJSSEPlatform(DefaultConstructorMarker defaultConstructorMarker) {
        this();
    }

    static {
        boolean bl;
        Companion = new Companion(null);
        try {
            Class.forName("org.openjsse.net.ssl.OpenJSSE", false, Companion.getClass().getClassLoader());
            bl = true;
        }
        catch (ClassNotFoundException classNotFoundException) {
            bl = false;
        }
        isSupported = bl;
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0003\u0010\u0005\u00a8\u0006\b"}, d2={"Lokhttp3/internal/platform/OpenJSSEPlatform$Companion;", "", "()V", "isSupported", "", "()Z", "buildIfSupported", "Lokhttp3/internal/platform/OpenJSSEPlatform;", "okhttp"})
    public static final class Companion {
        private Companion() {
        }

        public final boolean isSupported() {
            return isSupported;
        }

        @Nullable
        public final OpenJSSEPlatform buildIfSupported() {
            if (this.isSupported()) {
                return new OpenJSSEPlatform(null);
            }
            return null;
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }
}


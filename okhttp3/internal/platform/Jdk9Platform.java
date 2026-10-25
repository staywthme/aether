/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.platform;

import java.util.Collection;
import java.util.List;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.StringsKt;
import okhttp3.Protocol;
import okhttp3.internal.SuppressSignatureCheck;
import okhttp3.internal.platform.Platform;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0016\u0018\u0000 \u00122\u00020\u0001:\u0001\u0012B\u0005\u00a2\u0006\u0002\u0010\u0002J-\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\b2\u0011\u0010\t\u001a\r\u0012\t\u0012\u00070\u000b\u00a2\u0006\u0002\b\f0\nH\u0017J\u0012\u0010\r\u001a\u0004\u0018\u00010\b2\u0006\u0010\u0005\u001a\u00020\u0006H\u0017J\u0012\u0010\u000e\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0016\u00a8\u0006\u0013"}, d2={"Lokhttp3/internal/platform/Jdk9Platform;", "Lokhttp3/internal/platform/Platform;", "()V", "configureTlsExtensions", "", "sslSocket", "Ljavax/net/ssl/SSLSocket;", "hostname", "", "protocols", "", "Lokhttp3/Protocol;", "Lkotlin/jvm/JvmSuppressWildcards;", "getSelectedProtocol", "trustManager", "Ljavax/net/ssl/X509TrustManager;", "sslSocketFactory", "Ljavax/net/ssl/SSLSocketFactory;", "Companion", "okhttp"})
@SourceDebugExtension(value={"SMAP\nJdk9Platform.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Jdk9Platform.kt\nokhttp3/internal/platform/Jdk9Platform\n+ 2 ArraysJVM.kt\nkotlin/collections/ArraysKt__ArraysJVMKt\n*L\n1#1,89:1\n37#2,2:90\n*S KotlinDebug\n*F\n+ 1 Jdk9Platform.kt\nokhttp3/internal/platform/Jdk9Platform\n*L\n36#1:90,2\n*E\n"})
public class Jdk9Platform
extends Platform {
    @NotNull
    public static final Companion Companion;
    private static final boolean isAvailable;

    @Override
    @SuppressSignatureCheck
    public void configureTlsExtensions(@NotNull SSLSocket sSLSocket, @Nullable String object, @NotNull List<Protocol> collection) {
        Intrinsics.checkNotNullParameter(sSLSocket, "");
        Intrinsics.checkNotNullParameter(collection, "");
        object = sSLSocket.getSSLParameters();
        collection = Platform.Companion.alpnProtocolNames((List<? extends Protocol>)collection);
        Collection collection2 = collection;
        collection = collection2;
        collection = collection2;
        ((SSLParameters)object).setApplicationProtocols(collection2.toArray(new String[0]));
        sSLSocket.setSSLParameters((SSLParameters)object);
    }

    @Override
    @SuppressSignatureCheck
    @Nullable
    public String getSelectedProtocol(@NotNull SSLSocket object) {
        Intrinsics.checkNotNullParameter(object, "");
        try {
            object = ((SSLSocket)object).getApplicationProtocol();
            if (object == null ? true : Intrinsics.areEqual(object, "")) {
                return null;
            }
            return object;
        }
        catch (UnsupportedOperationException unsupportedOperationException) {
            return null;
        }
    }

    @Override
    @Nullable
    public X509TrustManager trustManager(@NotNull SSLSocketFactory sSLSocketFactory) {
        Intrinsics.checkNotNullParameter(sSLSocketFactory, "");
        throw new UnsupportedOperationException("clientBuilder.sslSocketFactory(SSLSocketFactory) not supported on JDK 9+");
    }

    static {
        boolean bl;
        Companion = new Companion(null);
        Object object = System.getProperty("java.specification.version");
        object = object != null ? StringsKt.toIntOrNull((String)object) : null;
        if (object != null) {
            bl = (Integer)object >= 9;
        } else {
            boolean bl2;
            try {
                SSLSocket.class.getMethod("getApplicationProtocol", new Class[0]);
                bl2 = true;
            }
            catch (NoSuchMethodException noSuchMethodException) {
                bl2 = false;
            }
            bl = bl2;
        }
        isAvailable = bl;
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0003\u0010\u0005\u00a8\u0006\b"}, d2={"Lokhttp3/internal/platform/Jdk9Platform$Companion;", "", "()V", "isAvailable", "", "()Z", "buildIfSupported", "Lokhttp3/internal/platform/Jdk9Platform;", "okhttp"})
    public static final class Companion {
        private Companion() {
        }

        public final boolean isAvailable() {
            return isAvailable;
        }

        @Nullable
        public final Jdk9Platform buildIfSupported() {
            if (this.isAvailable()) {
                return new Jdk9Platform();
            }
            return null;
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }
}


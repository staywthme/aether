/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.platform.android;

import java.util.Collection;
import java.util.List;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import okhttp3.Protocol;
import okhttp3.internal.platform.ConscryptPlatform;
import okhttp3.internal.platform.Platform;
import okhttp3.internal.platform.android.DeferredSocketAdapter;
import okhttp3.internal.platform.android.SocketAdapter;
import org.conscrypt.Conscrypt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\u0018\u0000 \u00102\u00020\u0001:\u0001\u0010B\u0005\u00a2\u0006\u0002\u0010\u0002J(\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nH\u0016J\u0012\u0010\f\u001a\u0004\u0018\u00010\b2\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\b\u0010\r\u001a\u00020\u000eH\u0016J\u0010\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\u0005\u001a\u00020\u0006H\u0016\u00a8\u0006\u0011"}, d2={"Lokhttp3/internal/platform/android/ConscryptSocketAdapter;", "Lokhttp3/internal/platform/android/SocketAdapter;", "()V", "configureTlsExtensions", "", "sslSocket", "Ljavax/net/ssl/SSLSocket;", "hostname", "", "protocols", "", "Lokhttp3/Protocol;", "getSelectedProtocol", "isSupported", "", "matchesSocket", "Companion", "okhttp"})
@SourceDebugExtension(value={"SMAP\nConscryptSocketAdapter.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ConscryptSocketAdapter.kt\nokhttp3/internal/platform/android/ConscryptSocketAdapter\n+ 2 ArraysJVM.kt\nkotlin/collections/ArraysKt__ArraysJVMKt\n*L\n1#1,64:1\n37#2,2:65\n*S KotlinDebug\n*F\n+ 1 ConscryptSocketAdapter.kt\nokhttp3/internal/platform/android/ConscryptSocketAdapter\n*L\n51#1:65,2\n*E\n"})
public final class ConscryptSocketAdapter
implements SocketAdapter {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private static final DeferredSocketAdapter.Factory factory = new DeferredSocketAdapter.Factory(){

        public final boolean matchesSocket(@NotNull SSLSocket sSLSocket) {
            Intrinsics.checkNotNullParameter(sSLSocket, "");
            return ConscryptPlatform.Companion.isSupported() && Conscrypt.isConscrypt(sSLSocket);
        }

        @NotNull
        public final SocketAdapter create(@NotNull SSLSocket sSLSocket) {
            Intrinsics.checkNotNullParameter(sSLSocket, "");
            return new ConscryptSocketAdapter();
        }
    };

    @Override
    public final boolean matchesSocket(@NotNull SSLSocket sSLSocket) {
        Intrinsics.checkNotNullParameter(sSLSocket, "");
        return Conscrypt.isConscrypt(sSLSocket);
    }

    @Override
    public final boolean isSupported() {
        return ConscryptPlatform.Companion.isSupported();
    }

    @Override
    @Nullable
    public final String getSelectedProtocol(@NotNull SSLSocket sSLSocket) {
        Intrinsics.checkNotNullParameter(sSLSocket, "");
        if (this.matchesSocket(sSLSocket)) {
            return Conscrypt.getApplicationProtocol(sSLSocket);
        }
        return null;
    }

    @Override
    public final void configureTlsExtensions(@NotNull SSLSocket object, @Nullable String object2, @NotNull List<? extends Protocol> list2) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(list2, "");
        if (this.matchesSocket((SSLSocket)object)) {
            Conscrypt.setUseSessionTickets((SSLSocket)object, true);
            object2 = Platform.Companion.alpnProtocolNames(list2);
            SSLSocket sSLSocket = object;
            Collection collection = (Collection)object2;
            object = collection;
            object = collection;
            Conscrypt.setApplicationProtocols(sSLSocket, collection.toArray(new String[0]));
        }
    }

    @Override
    @Nullable
    public final X509TrustManager trustManager(@NotNull SSLSocketFactory sSLSocketFactory) {
        return SocketAdapter.DefaultImpls.trustManager(this, sSLSocketFactory);
    }

    @Override
    public final boolean matchesSocketFactory(@NotNull SSLSocketFactory sSLSocketFactory) {
        return SocketAdapter.DefaultImpls.matchesSocketFactory(this, sSLSocketFactory);
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2={"Lokhttp3/internal/platform/android/ConscryptSocketAdapter$Companion;", "", "()V", "factory", "Lokhttp3/internal/platform/android/DeferredSocketAdapter$Factory;", "getFactory", "()Lokhttp3/internal/platform/android/DeferredSocketAdapter$Factory;", "okhttp"})
    public static final class Companion {
        private Companion() {
        }

        @NotNull
        public final DeferredSocketAdapter.Factory getFactory() {
            return factory;
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }
}


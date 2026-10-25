/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.SuppressLint
 *  android.os.Build$VERSION
 *  android.security.NetworkSecurityPolicy
 */
package okhttp3.internal.platform;

import android.annotation.SuppressLint;
import android.os.Build;
import android.security.NetworkSecurityPolicy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import okhttp3.Protocol;
import okhttp3.internal.SuppressSignatureCheck;
import okhttp3.internal.platform.Platform;
import okhttp3.internal.platform.android.Android10SocketAdapter;
import okhttp3.internal.platform.android.AndroidCertificateChainCleaner;
import okhttp3.internal.platform.android.AndroidSocketAdapter;
import okhttp3.internal.platform.android.BouncyCastleSocketAdapter;
import okhttp3.internal.platform.android.ConscryptSocketAdapter;
import okhttp3.internal.platform.android.DeferredSocketAdapter;
import okhttp3.internal.platform.android.SocketAdapter;
import okhttp3.internal.tls.CertificateChainCleaner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u0000 \u00172\u00020\u0001:\u0001\u0017B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\u0016J(\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000f2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00110\u0004H\u0016J\u0012\u0010\u0012\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\f\u001a\u00020\rH\u0016J\u0010\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u000e\u001a\u00020\u000fH\u0017J\u0012\u0010\b\u001a\u0004\u0018\u00010\t2\u0006\u0010\u0015\u001a\u00020\u0016H\u0016R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2={"Lokhttp3/internal/platform/Android10Platform;", "Lokhttp3/internal/platform/Platform;", "()V", "socketAdapters", "", "Lokhttp3/internal/platform/android/SocketAdapter;", "buildCertificateChainCleaner", "Lokhttp3/internal/tls/CertificateChainCleaner;", "trustManager", "Ljavax/net/ssl/X509TrustManager;", "configureTlsExtensions", "", "sslSocket", "Ljavax/net/ssl/SSLSocket;", "hostname", "", "protocols", "Lokhttp3/Protocol;", "getSelectedProtocol", "isCleartextTrafficPermitted", "", "sslSocketFactory", "Ljavax/net/ssl/SSLSocketFactory;", "Companion", "okhttp"})
@SuppressSignatureCheck
@SourceDebugExtension(value={"SMAP\nAndroid10Platform.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Android10Platform.kt\nokhttp3/internal/platform/Android10Platform\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 3 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,72:1\n766#2:73\n857#2,2:74\n1#3:76\n*S KotlinDebug\n*F\n+ 1 Android10Platform.kt\nokhttp3/internal/platform/Android10Platform\n*L\n43#1:73\n43#1:74,2\n*E\n"})
public final class Android10Platform
extends Platform {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final List<SocketAdapter> socketAdapters;
    private static final boolean isSupported = Platform.Companion.isAndroid() && Build.VERSION.SDK_INT >= 29;

    public Android10Platform() {
        SocketAdapter[] socketAdapterArray = new SocketAdapter[4];
        Object object = socketAdapterArray;
        socketAdapterArray[0] = Android10SocketAdapter.Companion.buildIfSupported();
        object[1] = new DeferredSocketAdapter(AndroidSocketAdapter.Companion.getPlayProviderFactory());
        object[2] = new DeferredSocketAdapter(ConscryptSocketAdapter.Companion.getFactory());
        object[3] = new DeferredSocketAdapter(BouncyCastleSocketAdapter.Companion.getFactory());
        object = CollectionsKt.listOfNotNull(object);
        Android10Platform android10Platform = this;
        Collection collection = new ArrayList();
        object = object.iterator();
        while (object.hasNext()) {
            Object e2 = object.next();
            SocketAdapter socketAdapter = (SocketAdapter)e2;
            if (!socketAdapter.isSupported()) continue;
            collection.add(e2);
        }
        android10Platform.socketAdapters = (List)collection;
    }

    @Override
    @Nullable
    public final X509TrustManager trustManager(@NotNull SSLSocketFactory sSLSocketFactory) {
        Object v0;
        Object object;
        block2: {
            Intrinsics.checkNotNullParameter(sSLSocketFactory, "");
            object = this.socketAdapters;
            object = object.iterator();
            while (object.hasNext()) {
                Object e2 = object.next();
                SocketAdapter socketAdapter = (SocketAdapter)e2;
                if (!socketAdapter.matchesSocketFactory(sSLSocketFactory)) continue;
                v0 = e2;
                break block2;
            }
            v0 = null;
        }
        object = v0;
        if (object != null) {
            return object.trustManager(sSLSocketFactory);
        }
        return null;
    }

    public final void configureTlsExtensions(@NotNull SSLSocket sSLSocket, @Nullable String string, @NotNull List<? extends Protocol> list2) {
        Object v0;
        Object object;
        block2: {
            Intrinsics.checkNotNullParameter(sSLSocket, "");
            Intrinsics.checkNotNullParameter(list2, "");
            object = this.socketAdapters;
            object = object.iterator();
            while (object.hasNext()) {
                Object e2 = object.next();
                SocketAdapter socketAdapter = (SocketAdapter)e2;
                if (!socketAdapter.matchesSocket(sSLSocket)) continue;
                v0 = e2;
                break block2;
            }
            v0 = null;
        }
        object = v0;
        if (object != null) {
            object.configureTlsExtensions(sSLSocket, string, list2);
        }
    }

    @Override
    @Nullable
    public final String getSelectedProtocol(@NotNull SSLSocket sSLSocket) {
        Object v0;
        block2: {
            Intrinsics.checkNotNullParameter(sSLSocket, "");
            Object object = this.socketAdapters;
            object = object.iterator();
            while (object.hasNext()) {
                Object e2 = object.next();
                SocketAdapter socketAdapter = (SocketAdapter)e2;
                if (!socketAdapter.matchesSocket(sSLSocket)) continue;
                v0 = e2;
                break block2;
            }
            v0 = null;
        }
        SocketAdapter socketAdapter = v0;
        if (socketAdapter != null) {
            return socketAdapter.getSelectedProtocol(sSLSocket);
        }
        return null;
    }

    @Override
    @SuppressLint(value={"NewApi"})
    public final boolean isCleartextTrafficPermitted(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, "");
        return NetworkSecurityPolicy.getInstance().isCleartextTrafficPermitted(string);
    }

    @Override
    @NotNull
    public final CertificateChainCleaner buildCertificateChainCleaner(@NotNull X509TrustManager x509TrustManager) {
        Intrinsics.checkNotNullParameter(x509TrustManager, "");
        AndroidCertificateChainCleaner androidCertificateChainCleaner = AndroidCertificateChainCleaner.Companion.buildIfSupported(x509TrustManager);
        if (androidCertificateChainCleaner != null) {
            return androidCertificateChainCleaner;
        }
        return super.buildCertificateChainCleaner(x509TrustManager);
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0003\u0010\u0005\u00a8\u0006\b"}, d2={"Lokhttp3/internal/platform/Android10Platform$Companion;", "", "()V", "isSupported", "", "()Z", "buildIfSupported", "Lokhttp3/internal/platform/Platform;", "okhttp"})
    public static final class Companion {
        private Companion() {
        }

        public final boolean isSupported() {
            return isSupported;
        }

        @Nullable
        public final Platform buildIfSupported() {
            if (this.isSupported()) {
                return new Android10Platform();
            }
            return null;
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }
}


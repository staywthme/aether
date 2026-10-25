/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Build$VERSION
 *  android.security.NetworkSecurityPolicy
 */
package okhttp3.internal.platform;

import android.os.Build;
import android.security.NetworkSecurityPolicy;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
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
import okhttp3.internal.platform.android.AndroidCertificateChainCleaner;
import okhttp3.internal.platform.android.AndroidSocketAdapter;
import okhttp3.internal.platform.android.BouncyCastleSocketAdapter;
import okhttp3.internal.platform.android.CloseGuard;
import okhttp3.internal.platform.android.ConscryptSocketAdapter;
import okhttp3.internal.platform.android.DeferredSocketAdapter;
import okhttp3.internal.platform.android.SocketAdapter;
import okhttp3.internal.platform.android.StandardAndroidSocketAdapter;
import okhttp3.internal.tls.CertificateChainCleaner;
import okhttp3.internal.tls.TrustRootIndex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000x\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u0000 )2\u00020\u0001:\u0002)*B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0016J\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\n\u001a\u00020\u000bH\u0016J-\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u00132\u0011\u0010\u0014\u001a\r\u0012\t\u0012\u00070\u0015\u00a2\u0006\u0002\b\u00160\u0006H\u0016J \u0010\u0017\u001a\u00020\u000f2\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001dH\u0016J\u0012\u0010\u001e\u001a\u0004\u0018\u00010\u00132\u0006\u0010\u0010\u001a\u00020\u0011H\u0016J\u0012\u0010\u001f\u001a\u0004\u0018\u00010 2\u0006\u0010!\u001a\u00020\u0013H\u0016J\u0010\u0010\"\u001a\u00020#2\u0006\u0010\u0012\u001a\u00020\u0013H\u0016J\u001a\u0010$\u001a\u00020\u000f2\u0006\u0010%\u001a\u00020\u00132\b\u0010&\u001a\u0004\u0018\u00010 H\u0016J\u0012\u0010\n\u001a\u0004\u0018\u00010\u000b2\u0006\u0010'\u001a\u00020(H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006+"}, d2={"Lokhttp3/internal/platform/AndroidPlatform;", "Lokhttp3/internal/platform/Platform;", "()V", "closeGuard", "Lokhttp3/internal/platform/android/CloseGuard;", "socketAdapters", "", "Lokhttp3/internal/platform/android/SocketAdapter;", "buildCertificateChainCleaner", "Lokhttp3/internal/tls/CertificateChainCleaner;", "trustManager", "Ljavax/net/ssl/X509TrustManager;", "buildTrustRootIndex", "Lokhttp3/internal/tls/TrustRootIndex;", "configureTlsExtensions", "", "sslSocket", "Ljavax/net/ssl/SSLSocket;", "hostname", "", "protocols", "Lokhttp3/Protocol;", "Lkotlin/jvm/JvmSuppressWildcards;", "connectSocket", "socket", "Ljava/net/Socket;", "address", "Ljava/net/InetSocketAddress;", "connectTimeout", "", "getSelectedProtocol", "getStackTraceForCloseable", "", "closer", "isCleartextTrafficPermitted", "", "logCloseableLeak", "message", "stackTrace", "sslSocketFactory", "Ljavax/net/ssl/SSLSocketFactory;", "Companion", "CustomTrustRootIndex", "okhttp"})
@SuppressSignatureCheck
@SourceDebugExtension(value={"SMAP\nAndroidPlatform.kt\nKotlin\n*S Kotlin\n*F\n+ 1 AndroidPlatform.kt\nokhttp3/internal/platform/AndroidPlatform\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 3 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,163:1\n766#2:164\n857#2,2:165\n1#3:167\n*S KotlinDebug\n*F\n+ 1 AndroidPlatform.kt\nokhttp3/internal/platform/AndroidPlatform\n*L\n52#1:164\n52#1:165,2\n*E\n"})
public final class AndroidPlatform
extends Platform {
    @NotNull
    public static final Companion Companion;
    @NotNull
    private final List<SocketAdapter> socketAdapters;
    @NotNull
    private final CloseGuard closeGuard;
    private static final boolean isSupported;

    public AndroidPlatform() {
        SocketAdapter[] socketAdapterArray = new SocketAdapter[4];
        Object object = socketAdapterArray;
        socketAdapterArray[0] = StandardAndroidSocketAdapter.Companion.buildIfSupported$default(StandardAndroidSocketAdapter.Companion, null, 1, null);
        object[1] = new DeferredSocketAdapter(AndroidSocketAdapter.Companion.getPlayProviderFactory());
        object[2] = new DeferredSocketAdapter(ConscryptSocketAdapter.Companion.getFactory());
        object[3] = new DeferredSocketAdapter(BouncyCastleSocketAdapter.Companion.getFactory());
        object = CollectionsKt.listOfNotNull(object);
        AndroidPlatform androidPlatform = this;
        Collection collection = new ArrayList();
        object = object.iterator();
        while (object.hasNext()) {
            Object e2 = object.next();
            SocketAdapter socketAdapter = (SocketAdapter)e2;
            if (!socketAdapter.isSupported()) continue;
            collection.add(e2);
        }
        androidPlatform.socketAdapters = (List)collection;
        this.closeGuard = CloseGuard.Companion.get();
    }

    @Override
    public final void connectSocket(@NotNull Socket socket, @NotNull InetSocketAddress inetSocketAddress, int n2) throws IOException {
        Intrinsics.checkNotNullParameter(socket, "");
        Intrinsics.checkNotNullParameter(inetSocketAddress, "");
        try {
            socket.connect(inetSocketAddress, n2);
            return;
        }
        catch (ClassCastException classCastException) {
            if (Build.VERSION.SDK_INT == 26) {
                throw new IOException("Exception in connect", classCastException);
            }
            throw classCastException;
        }
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

    @Override
    public final void configureTlsExtensions(@NotNull SSLSocket sSLSocket, @Nullable String string, @NotNull List<Protocol> list2) {
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
    @Nullable
    public final Object getStackTraceForCloseable(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, "");
        return this.closeGuard.createAndOpen(string);
    }

    @Override
    public final void logCloseableLeak(@NotNull String string, @Nullable Object object) {
        Intrinsics.checkNotNullParameter(string, "");
        boolean bl = this.closeGuard.warnIfOpen(object);
        if (!bl) {
            Platform.log$default(this, string, 5, null, 4, null);
        }
    }

    @Override
    public final boolean isCleartextTrafficPermitted(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, "");
        if (Build.VERSION.SDK_INT >= 24) {
            return NetworkSecurityPolicy.getInstance().isCleartextTrafficPermitted(string);
        }
        if (Build.VERSION.SDK_INT >= 23) {
            return NetworkSecurityPolicy.getInstance().isCleartextTrafficPermitted();
        }
        return true;
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

    @Override
    @NotNull
    public final TrustRootIndex buildTrustRootIndex(@NotNull X509TrustManager x509TrustManager) {
        Object object;
        Intrinsics.checkNotNullParameter(x509TrustManager, "");
        try {
            Class[] classArray = new Class[1];
            object = classArray;
            classArray[0] = X509Certificate.class;
            object = x509TrustManager.getClass().getDeclaredMethod("findTrustAnchorByIssuerAndSignature", (Class<?>)object);
            object.setAccessible(true);
            Intrinsics.checkNotNullExpressionValue(object, "");
            object = new CustomTrustRootIndex(x509TrustManager, (Method)object);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            object = super.buildTrustRootIndex(x509TrustManager);
        }
        return object;
    }

    static {
        boolean bl;
        Companion = new Companion(null);
        if (!Platform.Companion.isAndroid()) {
            bl = false;
        } else if (Build.VERSION.SDK_INT >= 30) {
            bl = false;
        } else {
            if (!(Build.VERSION.SDK_INT >= 21)) {
                String string = "Expected Android API level 21+ but was " + Build.VERSION.SDK_INT;
                throw new IllegalStateException(string.toString());
            }
            bl = true;
        }
        isSupported = bl;
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0003\u0010\u0005\u00a8\u0006\b"}, d2={"Lokhttp3/internal/platform/AndroidPlatform$Companion;", "", "()V", "isSupported", "", "()Z", "buildIfSupported", "Lokhttp3/internal/platform/Platform;", "okhttp"})
    public static final class Companion {
        private Companion() {
        }

        public final boolean isSupported() {
            return isSupported;
        }

        @Nullable
        public final Platform buildIfSupported() {
            if (this.isSupported()) {
                return new AndroidPlatform();
            }
            return null;
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0080\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\t\u0010\u0007\u001a\u00020\u0003H\u00c2\u0003J\t\u0010\b\u001a\u00020\u0005H\u00c2\u0003J\u001d\u0010\t\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u00c6\u0001J\u0013\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\rH\u00d6\u0003J\u0012\u0010\u000e\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\u0010\u001a\u00020\u000fH\u0016J\t\u0010\u0011\u001a\u00020\u0012H\u00d6\u0001J\t\u0010\u0013\u001a\u00020\u0014H\u00d6\u0001R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2={"Lokhttp3/internal/platform/AndroidPlatform$CustomTrustRootIndex;", "Lokhttp3/internal/tls/TrustRootIndex;", "trustManager", "Ljavax/net/ssl/X509TrustManager;", "findByIssuerAndSignatureMethod", "Ljava/lang/reflect/Method;", "(Ljavax/net/ssl/X509TrustManager;Ljava/lang/reflect/Method;)V", "component1", "component2", "copy", "equals", "", "other", "", "findByIssuerAndSignature", "Ljava/security/cert/X509Certificate;", "cert", "hashCode", "", "toString", "", "okhttp"})
    public static final class CustomTrustRootIndex
    implements TrustRootIndex {
        @NotNull
        private final X509TrustManager trustManager;
        @NotNull
        private final Method findByIssuerAndSignatureMethod;

        public CustomTrustRootIndex(@NotNull X509TrustManager x509TrustManager, @NotNull Method method) {
            Intrinsics.checkNotNullParameter(x509TrustManager, "");
            Intrinsics.checkNotNullParameter(method, "");
            this.trustManager = x509TrustManager;
            this.findByIssuerAndSignatureMethod = method;
        }

        @Override
        @Nullable
        public final X509Certificate findByIssuerAndSignature(@NotNull X509Certificate object) {
            Intrinsics.checkNotNullParameter(object, "");
            try {
                Object[] objectArray = new Object[1];
                Object[] objectArray2 = objectArray;
                objectArray[0] = object;
                Object object2 = this.findByIssuerAndSignatureMethod.invoke(this.trustManager, objectArray2);
                Intrinsics.checkNotNull(object2);
                object = (TrustAnchor)object2;
                object = ((TrustAnchor)object).getTrustedCert();
            }
            catch (IllegalAccessException illegalAccessException) {
                throw new AssertionError("unable to get issues and signature", illegalAccessException);
            }
            catch (InvocationTargetException invocationTargetException) {
                object = null;
            }
            return object;
        }

        private final X509TrustManager component1() {
            return this.trustManager;
        }

        private final Method component2() {
            return this.findByIssuerAndSignatureMethod;
        }

        @NotNull
        public final CustomTrustRootIndex copy(@NotNull X509TrustManager x509TrustManager, @NotNull Method method) {
            Intrinsics.checkNotNullParameter(x509TrustManager, "");
            Intrinsics.checkNotNullParameter(method, "");
            return new CustomTrustRootIndex(x509TrustManager, method);
        }

        public static /* synthetic */ CustomTrustRootIndex copy$default(CustomTrustRootIndex customTrustRootIndex, X509TrustManager x509TrustManager, Method method, int n2, Object object) {
            if ((n2 & 1) != 0) {
                x509TrustManager = customTrustRootIndex.trustManager;
            }
            if ((n2 & 2) != 0) {
                method = customTrustRootIndex.findByIssuerAndSignatureMethod;
            }
            return customTrustRootIndex.copy(x509TrustManager, method);
        }

        @NotNull
        public final String toString() {
            return "CustomTrustRootIndex(trustManager=" + this.trustManager + ", findByIssuerAndSignatureMethod=" + this.findByIssuerAndSignatureMethod + ')';
        }

        public final int hashCode() {
            int n2 = this.trustManager.hashCode();
            n2 = n2 * 31 + this.findByIssuerAndSignatureMethod.hashCode();
            return n2;
        }

        public final boolean equals(@Nullable Object object) {
            if (this == object) {
                return true;
            }
            if (!(object instanceof CustomTrustRootIndex)) {
                return false;
            }
            object = (CustomTrustRootIndex)object;
            if (!Intrinsics.areEqual(this.trustManager, ((CustomTrustRootIndex)object).trustManager)) {
                return false;
            }
            return Intrinsics.areEqual(this.findByIssuerAndSignatureMethod, ((CustomTrustRootIndex)object).findByIssuerAndSignatureMethod);
        }
    }
}


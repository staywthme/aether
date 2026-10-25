/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.platform.android;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.internal.Util;
import okhttp3.internal.platform.Platform;
import okhttp3.internal.platform.android.AndroidSocketAdapter;
import okhttp3.internal.platform.android.SocketAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 \u000e2\u00020\u0001:\u0001\u000eB1\u0012\u000e\u0010\u0002\u001a\n\u0012\u0006\b\u0000\u0012\u00020\u00040\u0003\u0012\u000e\u0010\u0005\u001a\n\u0012\u0006\b\u0000\u0012\u00020\u00060\u0003\u0012\n\u0010\u0007\u001a\u0006\u0012\u0002\b\u00030\u0003\u00a2\u0006\u0002\u0010\bJ\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0006H\u0016J\u0012\u0010\f\u001a\u0004\u0018\u00010\r2\u0006\u0010\u000b\u001a\u00020\u0006H\u0016R\u0012\u0010\u0007\u001a\u0006\u0012\u0002\b\u00030\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0005\u001a\n\u0012\u0006\b\u0000\u0012\u00020\u00060\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2={"Lokhttp3/internal/platform/android/StandardAndroidSocketAdapter;", "Lokhttp3/internal/platform/android/AndroidSocketAdapter;", "sslSocketClass", "Ljava/lang/Class;", "Ljavax/net/ssl/SSLSocket;", "sslSocketFactoryClass", "Ljavax/net/ssl/SSLSocketFactory;", "paramClass", "(Ljava/lang/Class;Ljava/lang/Class;Ljava/lang/Class;)V", "matchesSocketFactory", "", "sslSocketFactory", "trustManager", "Ljavax/net/ssl/X509TrustManager;", "Companion", "okhttp"})
public final class StandardAndroidSocketAdapter
extends AndroidSocketAdapter {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final Class<? super SSLSocketFactory> sslSocketFactoryClass;
    @NotNull
    private final Class<?> paramClass;

    public StandardAndroidSocketAdapter(@NotNull Class<? super SSLSocket> clazz, @NotNull Class<? super SSLSocketFactory> clazz2, @NotNull Class<?> clazz3) {
        Intrinsics.checkNotNullParameter(clazz, "");
        Intrinsics.checkNotNullParameter(clazz2, "");
        Intrinsics.checkNotNullParameter(clazz3, "");
        super(clazz);
        this.sslSocketFactoryClass = clazz2;
        this.paramClass = clazz3;
    }

    @Override
    public final boolean matchesSocketFactory(@NotNull SSLSocketFactory sSLSocketFactory) {
        Intrinsics.checkNotNullParameter(sSLSocketFactory, "");
        return this.sslSocketFactoryClass.isInstance(sSLSocketFactory);
    }

    @Override
    @Nullable
    public final X509TrustManager trustManager(@NotNull SSLSocketFactory sSLSocketFactory) {
        X509TrustManager x509TrustManager;
        Intrinsics.checkNotNullParameter(sSLSocketFactory, "");
        sSLSocketFactory = Util.readFieldOrNull(sSLSocketFactory, this.paramClass, "sslParameters");
        Intrinsics.checkNotNull(sSLSocketFactory);
        X509TrustManager x509TrustManager2 = x509TrustManager = Util.readFieldOrNull(sSLSocketFactory, X509TrustManager.class, "x509TrustManager");
        if (x509TrustManager == null) {
            x509TrustManager2 = Util.readFieldOrNull(sSLSocketFactory, X509TrustManager.class, "trustManager");
        }
        return x509TrustManager2;
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u0003\u001a\u0004\u0018\u00010\u00042\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u00a8\u0006\u0007"}, d2={"Lokhttp3/internal/platform/android/StandardAndroidSocketAdapter$Companion;", "", "()V", "buildIfSupported", "Lokhttp3/internal/platform/android/SocketAdapter;", "packageName", "", "okhttp"})
    public static final class Companion {
        private Companion() {
        }

        @Nullable
        public final SocketAdapter buildIfSupported(@NotNull String object) {
            Object object2;
            Intrinsics.checkNotNullParameter(object, "");
            try {
                Class<?> clazz = Class.forName((String)object + ".OpenSSLSocketImpl");
                Intrinsics.checkNotNull(clazz);
                object2 = clazz;
                Class<?> clazz2 = Class.forName((String)object + ".OpenSSLSocketFactoryImpl");
                Intrinsics.checkNotNull(clazz2);
                Class<?> clazz3 = clazz2;
                object = Class.forName((String)object + ".SSLParametersImpl");
                Intrinsics.checkNotNullExpressionValue(object, "");
                object2 = new StandardAndroidSocketAdapter((Class<? super SSLSocket>)object2, (Class<? super SSLSocketFactory>)clazz3, (Class<?>)object);
            }
            catch (Exception exception) {
                Platform.Companion.get().log("unable to load android socket classes", 5, exception);
                object2 = null;
            }
            return object2;
        }

        public static /* synthetic */ SocketAdapter buildIfSupported$default(Companion companion, String string, int n2, Object object) {
            if ((n2 & 1) != 0) {
                string = "com.android.org.conscrypt";
            }
            return companion.buildIfSupported(string);
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }
}


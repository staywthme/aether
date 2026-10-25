/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.platform.android;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;
import okhttp3.Protocol;
import okhttp3.internal.platform.AndroidPlatform;
import okhttp3.internal.platform.Platform;
import okhttp3.internal.platform.android.DeferredSocketAdapter;
import okhttp3.internal.platform.android.SocketAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\b\u0016\u0018\u0000 \u00182\u00020\u0001:\u0001\u0018B\u0015\u0012\u000e\u0010\u0002\u001a\n\u0012\u0006\b\u0000\u0012\u00020\u00040\u0003\u00a2\u0006\u0002\u0010\u0005J(\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u00042\b\u0010\u000f\u001a\u0004\u0018\u00010\u00102\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012H\u0016J\u0012\u0010\u0014\u001a\u0004\u0018\u00010\u00102\u0006\u0010\u000e\u001a\u00020\u0004H\u0016J\b\u0010\u0015\u001a\u00020\u0016H\u0016J\u0010\u0010\u0017\u001a\u00020\u00162\u0006\u0010\u000e\u001a\u00020\u0004H\u0016R\u0016\u0010\u0006\u001a\n \b*\u0004\u0018\u00010\u00070\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\t\u001a\n \b*\u0004\u0018\u00010\u00070\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\n\u001a\n \b*\u0004\u0018\u00010\u00070\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0002\u001a\n\u0012\u0006\b\u0000\u0012\u00020\u00040\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0019"}, d2={"Lokhttp3/internal/platform/android/AndroidSocketAdapter;", "Lokhttp3/internal/platform/android/SocketAdapter;", "sslSocketClass", "Ljava/lang/Class;", "Ljavax/net/ssl/SSLSocket;", "(Ljava/lang/Class;)V", "getAlpnSelectedProtocol", "Ljava/lang/reflect/Method;", "kotlin.jvm.PlatformType", "setAlpnProtocols", "setHostname", "setUseSessionTickets", "configureTlsExtensions", "", "sslSocket", "hostname", "", "protocols", "", "Lokhttp3/Protocol;", "getSelectedProtocol", "isSupported", "", "matchesSocket", "Companion", "okhttp"})
public class AndroidSocketAdapter
implements SocketAdapter {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final Class<? super SSLSocket> sslSocketClass;
    @NotNull
    private final Method setUseSessionTickets;
    private final Method setHostname;
    private final Method getAlpnSelectedProtocol;
    private final Method setAlpnProtocols;
    @NotNull
    private static final DeferredSocketAdapter.Factory playProviderFactory = Companion.factory("com.google.android.gms.org.conscrypt");

    public AndroidSocketAdapter(@NotNull Class<? super SSLSocket> classArray) {
        Intrinsics.checkNotNullParameter(classArray, "");
        this.sslSocketClass = classArray;
        Class[] classArray2 = new Class[1];
        classArray = classArray2;
        classArray2[0] = Boolean.TYPE;
        Method method = this.sslSocketClass.getDeclaredMethod("setUseSessionTickets", classArray);
        Intrinsics.checkNotNullExpressionValue(method, "");
        this.setUseSessionTickets = method;
        Class[] classArray3 = new Class[1];
        classArray = classArray3;
        classArray3[0] = String.class;
        this.setHostname = this.sslSocketClass.getMethod("setHostname", classArray);
        this.getAlpnSelectedProtocol = this.sslSocketClass.getMethod("getAlpnSelectedProtocol", new Class[0]);
        Class[] classArray4 = new Class[1];
        classArray = classArray4;
        classArray4[0] = byte[].class;
        this.setAlpnProtocols = this.sslSocketClass.getMethod("setAlpnProtocols", classArray);
    }

    @Override
    public boolean isSupported() {
        return AndroidPlatform.Companion.isSupported();
    }

    @Override
    public boolean matchesSocket(@NotNull SSLSocket sSLSocket) {
        Intrinsics.checkNotNullParameter(sSLSocket, "");
        return this.sslSocketClass.isInstance(sSLSocket);
    }

    @Override
    public void configureTlsExtensions(@NotNull SSLSocket sSLSocket, @Nullable String string, @NotNull List<? extends Protocol> list2) {
        Intrinsics.checkNotNullParameter(sSLSocket, "");
        Intrinsics.checkNotNullParameter(list2, "");
        if (this.matchesSocket(sSLSocket)) {
            try {
                Object[] objectArray = new Object[1];
                Object[] objectArray2 = objectArray;
                objectArray[0] = Boolean.TRUE;
                this.setUseSessionTickets.invoke(sSLSocket, objectArray2);
                if (string != null) {
                    Object[] objectArray3 = new Object[1];
                    objectArray2 = objectArray3;
                    objectArray3[0] = string;
                    this.setHostname.invoke(sSLSocket, objectArray2);
                }
                Object[] objectArray4 = new Object[1];
                objectArray2 = objectArray4;
                objectArray4[0] = Platform.Companion.concatLengthPrefixed(list2);
                this.setAlpnProtocols.invoke(sSLSocket, objectArray2);
                return;
            }
            catch (IllegalAccessException illegalAccessException) {
                throw new AssertionError((Object)illegalAccessException);
            }
            catch (InvocationTargetException invocationTargetException) {
                throw new AssertionError((Object)invocationTargetException);
            }
        }
    }

    @Override
    @Nullable
    public String getSelectedProtocol(@NotNull SSLSocket object) {
        Intrinsics.checkNotNullParameter(object, "");
        if (!this.matchesSocket((SSLSocket)object)) {
            return null;
        }
        try {
            byte[] byArray = (byte[])this.getAlpnSelectedProtocol.invoke(object, new Object[0]);
            object = byArray;
            object = byArray != null ? new String((byte[])object, Charsets.UTF_8) : null;
        }
        catch (IllegalAccessException illegalAccessException) {
            throw new AssertionError((Object)illegalAccessException);
        }
        catch (InvocationTargetException invocationTargetException) {
            object = invocationTargetException;
            Throwable throwable = invocationTargetException.getCause();
            if (!(throwable instanceof NullPointerException) || !Intrinsics.areEqual(((NullPointerException)throwable).getMessage(), "ssl == null")) {
                throw new AssertionError(object);
            }
            object = null;
        }
        return object;
    }

    @Override
    @Nullable
    public X509TrustManager trustManager(@NotNull SSLSocketFactory sSLSocketFactory) {
        return SocketAdapter.DefaultImpls.trustManager(this, sSLSocketFactory);
    }

    @Override
    public boolean matchesSocketFactory(@NotNull SSLSocketFactory sSLSocketFactory) {
        return SocketAdapter.DefaultImpls.matchesSocketFactory(this, sSLSocketFactory);
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0007\u001a\u00020\b2\u000e\u0010\t\u001a\n\u0012\u0006\b\u0000\u0012\u00020\u000b0\nH\u0002J\u000e\u0010\f\u001a\u00020\u00042\u0006\u0010\r\u001a\u00020\u000eR\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u000f"}, d2={"Lokhttp3/internal/platform/android/AndroidSocketAdapter$Companion;", "", "()V", "playProviderFactory", "Lokhttp3/internal/platform/android/DeferredSocketAdapter$Factory;", "getPlayProviderFactory", "()Lokhttp3/internal/platform/android/DeferredSocketAdapter$Factory;", "build", "Lokhttp3/internal/platform/android/AndroidSocketAdapter;", "actualSSLSocketClass", "Ljava/lang/Class;", "Ljavax/net/ssl/SSLSocket;", "factory", "packageName", "", "okhttp"})
    public static final class Companion {
        private Companion() {
        }

        @NotNull
        public final DeferredSocketAdapter.Factory getPlayProviderFactory() {
            return playProviderFactory;
        }

        private final AndroidSocketAdapter build(Class<? super SSLSocket> clazz) {
            Class<? super SSLSocket> clazz2 = clazz;
            while (clazz2 != null && !Intrinsics.areEqual(clazz2.getSimpleName(), "OpenSSLSocketImpl")) {
                if ((clazz2 = clazz2.getSuperclass()) == null) {
                    throw new AssertionError((Object)("No OpenSSLSocketImpl superclass of socket of type " + clazz));
                }
            }
            Class<? super SSLSocket> clazz3 = clazz2;
            Intrinsics.checkNotNull(clazz3);
            return new AndroidSocketAdapter(clazz3);
        }

        @NotNull
        public final DeferredSocketAdapter.Factory factory(@NotNull String string) {
            Intrinsics.checkNotNullParameter(string, "");
            return new DeferredSocketAdapter.Factory(string){
                final /* synthetic */ String $packageName;
                {
                    this.$packageName = string;
                }

                public final boolean matchesSocket(@NotNull SSLSocket sSLSocket) {
                    Intrinsics.checkNotNullParameter(sSLSocket, "");
                    String string = sSLSocket.getClass().getName();
                    Intrinsics.checkNotNullExpressionValue(string, "");
                    return StringsKt.startsWith$default(string, this.$packageName + '.', false, 2, null);
                }

                @NotNull
                public final SocketAdapter create(@NotNull SSLSocket sSLSocket) {
                    Intrinsics.checkNotNullParameter(sSLSocket, "");
                    return okhttp3.internal.platform.android.AndroidSocketAdapter$Companion.access$build(AndroidSocketAdapter.Companion, sSLSocket.getClass());
                }
            };
        }

        public static final /* synthetic */ AndroidSocketAdapter access$build(Companion companion, Class clazz) {
            return companion.build(clazz);
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }
}


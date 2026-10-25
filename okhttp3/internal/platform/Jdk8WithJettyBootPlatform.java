/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.platform;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import javax.net.ssl.SSLSocket;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.Protocol;
import okhttp3.internal.platform.Platform;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u0000 \u00162\u00020\u0001:\u0002\u0015\u0016B5\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\n\u0010\u0006\u001a\u0006\u0012\u0002\b\u00030\u0007\u0012\n\u0010\b\u001a\u0006\u0012\u0002\b\u00030\u0007\u00a2\u0006\u0002\u0010\tJ\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0016J(\u0010\u000e\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\b\u0010\u000f\u001a\u0004\u0018\u00010\u00102\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012H\u0016J\u0012\u0010\u0014\u001a\u0004\u0018\u00010\u00102\u0006\u0010\f\u001a\u00020\rH\u0016R\u0012\u0010\u0006\u001a\u0006\u0012\u0002\b\u00030\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0012\u0010\b\u001a\u0006\u0012\u0002\b\u00030\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2={"Lokhttp3/internal/platform/Jdk8WithJettyBootPlatform;", "Lokhttp3/internal/platform/Platform;", "putMethod", "Ljava/lang/reflect/Method;", "getMethod", "removeMethod", "clientProviderClass", "Ljava/lang/Class;", "serverProviderClass", "(Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;Ljava/lang/Class;Ljava/lang/Class;)V", "afterHandshake", "", "sslSocket", "Ljavax/net/ssl/SSLSocket;", "configureTlsExtensions", "hostname", "", "protocols", "", "Lokhttp3/Protocol;", "getSelectedProtocol", "AlpnProvider", "Companion", "okhttp"})
public final class Jdk8WithJettyBootPlatform
extends Platform {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final Method putMethod;
    @NotNull
    private final Method getMethod;
    @NotNull
    private final Method removeMethod;
    @NotNull
    private final Class<?> clientProviderClass;
    @NotNull
    private final Class<?> serverProviderClass;

    public Jdk8WithJettyBootPlatform(@NotNull Method method, @NotNull Method method2, @NotNull Method method3, @NotNull Class<?> clazz, @NotNull Class<?> clazz2) {
        Intrinsics.checkNotNullParameter(method, "");
        Intrinsics.checkNotNullParameter(method2, "");
        Intrinsics.checkNotNullParameter(method3, "");
        Intrinsics.checkNotNullParameter(clazz, "");
        Intrinsics.checkNotNullParameter(clazz2, "");
        this.putMethod = method;
        this.getMethod = method2;
        this.removeMethod = method3;
        this.clientProviderClass = clazz;
        this.serverProviderClass = clazz2;
    }

    public final void configureTlsExtensions(@NotNull SSLSocket sSLSocket, @Nullable String object, @NotNull List<? extends Protocol> objectArray) {
        Intrinsics.checkNotNullParameter(sSLSocket, "");
        Intrinsics.checkNotNullParameter(objectArray, "");
        object = Platform.Companion.alpnProtocolNames((List<? extends Protocol>)objectArray);
        try {
            Class[] classArray = new Class[2];
            objectArray = classArray;
            classArray[0] = this.clientProviderClass;
            objectArray[1] = this.serverProviderClass;
            object = Proxy.newProxyInstance(Platform.class.getClassLoader(), objectArray, (InvocationHandler)new AlpnProvider((List<String>)object));
            Object[] objectArray2 = new Object[2];
            objectArray = objectArray2;
            objectArray2[0] = sSLSocket;
            objectArray[1] = object;
            this.putMethod.invoke(null, objectArray);
            return;
        }
        catch (InvocationTargetException invocationTargetException) {
            throw new AssertionError("failed to set ALPN", invocationTargetException);
        }
        catch (IllegalAccessException illegalAccessException) {
            throw new AssertionError("failed to set ALPN", illegalAccessException);
        }
    }

    @Override
    public final void afterHandshake(@NotNull SSLSocket sSLSocket) {
        Intrinsics.checkNotNullParameter(sSLSocket, "");
        try {
            Object[] objectArray = new Object[1];
            Object[] objectArray2 = objectArray;
            objectArray[0] = sSLSocket;
            this.removeMethod.invoke(null, objectArray2);
            return;
        }
        catch (IllegalAccessException illegalAccessException) {
            throw new AssertionError("failed to remove ALPN", illegalAccessException);
        }
        catch (InvocationTargetException invocationTargetException) {
            throw new AssertionError("failed to remove ALPN", invocationTargetException);
        }
    }

    @Override
    @Nullable
    public final String getSelectedProtocol(@NotNull SSLSocket object) {
        Intrinsics.checkNotNullParameter(object, "");
        try {
            Object[] objectArray = new Object[1];
            Object[] objectArray2 = objectArray;
            objectArray[0] = object;
            InvocationHandler invocationHandler = Proxy.getInvocationHandler(this.getMethod.invoke(null, objectArray2));
            Intrinsics.checkNotNull(invocationHandler);
            object = (AlpnProvider)invocationHandler;
            if (!((AlpnProvider)object).getUnsupported() && ((AlpnProvider)object).getSelected() == null) {
                Platform.log$default(this, "ALPN callback dropped: HTTP/2 is disabled. Is alpn-boot on the boot class path?", 0, null, 6, null);
                return null;
            }
            if (((AlpnProvider)object).getUnsupported()) {
                return null;
            }
            return ((AlpnProvider)object).getSelected();
        }
        catch (InvocationTargetException invocationTargetException) {
            throw new AssertionError("failed to get ALPN selected protocol", invocationTargetException);
        }
        catch (IllegalAccessException illegalAccessException) {
            throw new AssertionError("failed to get ALPN selected protocol", illegalAccessException);
        }
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0013\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\u0002\u0010\u0005J0\u0010\u0011\u001a\u0004\u0018\u00010\u00122\u0006\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u00152\u000e\u0010\u0016\u001a\n\u0012\u0004\u0012\u00020\u0012\u0018\u00010\u0017H\u0096\u0002\u00a2\u0006\u0002\u0010\u0018R\u0014\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u001a\u0010\u000b\u001a\u00020\fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010\u00a8\u0006\u0019"}, d2={"Lokhttp3/internal/platform/Jdk8WithJettyBootPlatform$AlpnProvider;", "Ljava/lang/reflect/InvocationHandler;", "protocols", "", "", "(Ljava/util/List;)V", "selected", "getSelected", "()Ljava/lang/String;", "setSelected", "(Ljava/lang/String;)V", "unsupported", "", "getUnsupported", "()Z", "setUnsupported", "(Z)V", "invoke", "", "proxy", "method", "Ljava/lang/reflect/Method;", "args", "", "(Ljava/lang/Object;Ljava/lang/reflect/Method;[Ljava/lang/Object;)Ljava/lang/Object;", "okhttp"})
    static final class AlpnProvider
    implements InvocationHandler {
        @NotNull
        private final List<String> protocols;
        private boolean unsupported;
        @Nullable
        private String selected;

        public AlpnProvider(@NotNull List<String> list2) {
            Intrinsics.checkNotNullParameter(list2, "");
            this.protocols = list2;
        }

        public final boolean getUnsupported() {
            return this.unsupported;
        }

        public final void setUnsupported(boolean bl) {
            this.unsupported = bl;
        }

        @Nullable
        public final String getSelected() {
            return this.selected;
        }

        public final void setSelected(@Nullable String string) {
            this.selected = string;
        }

        @Override
        @Nullable
        public final Object invoke(@NotNull Object object, @NotNull Method method, @Nullable Object[] object2) throws Throwable {
            Intrinsics.checkNotNullParameter(object, "");
            Intrinsics.checkNotNullParameter(method, "");
            Object[] objectArray = object2;
            if (object2 == null) {
                objectArray = new Object[]{};
            }
            object = objectArray;
            object2 = method.getName();
            Object object3 = method.getReturnType();
            if (Intrinsics.areEqual(object2, "supports") && Intrinsics.areEqual(Boolean.TYPE, object3)) {
                return Boolean.TRUE;
            }
            if (Intrinsics.areEqual(object2, "unsupported") && Intrinsics.areEqual(Void.TYPE, object3)) {
                this.unsupported = true;
                return null;
            }
            if (Intrinsics.areEqual(object2, "protocols") && ((Object[])object).length == 0) {
                return this.protocols;
            }
            if ((Intrinsics.areEqual(object2, "selectProtocol") || Intrinsics.areEqual(object2, "select")) && Intrinsics.areEqual(String.class, object3) && ((Object[])object).length == 1 && object[0] instanceof List) {
                Object object4 = object[0];
                Intrinsics.checkNotNull(object4);
                object = (List)object4;
                int n2 = 0;
                int n3 = object.size();
                if (n3 >= 0) {
                    while (true) {
                        Object e2 = object.get(n2);
                        Intrinsics.checkNotNull(e2);
                        object3 = (String)e2;
                        if (this.protocols.contains(object3)) {
                            this.selected = object3;
                            return this.selected;
                        }
                        if (n2 == n3) break;
                        ++n2;
                    }
                }
                this.selected = this.protocols.get(0);
                return this.selected;
            }
            if ((Intrinsics.areEqual(object2, "protocolSelected") || Intrinsics.areEqual(object2, "selected")) && ((Object[])object).length == 1) {
                Object object5 = object[0];
                Intrinsics.checkNotNull(object5);
                this.selected = (String)object5;
                return null;
            }
            return method.invoke(this, Arrays.copyOf(object, ((Object[])object).length));
        }
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004\u00a8\u0006\u0005"}, d2={"Lokhttp3/internal/platform/Jdk8WithJettyBootPlatform$Companion;", "", "()V", "buildIfSupported", "Lokhttp3/internal/platform/Platform;", "okhttp"})
    public static final class Companion {
        private Companion() {
        }

        @Nullable
        public final Platform buildIfSupported() {
            String string = System.getProperty("java.specification.version", "unknown");
            try {
                Intrinsics.checkNotNullExpressionValue(string, "");
                int n2 = Integer.parseInt(string);
                if (n2 >= 9) {
                    return null;
                }
            }
            catch (NumberFormatException numberFormatException) {}
            try {
                Object object = "org.eclipse.jetty.alpn.ALPN";
                Class[] classArray = Class.forName("org.eclipse.jetty.alpn.ALPN", true, null);
                GenericDeclaration genericDeclaration = Class.forName((String)object + "$Provider", true, null);
                Class<?> clazz = Class.forName((String)object + "$ClientProvider", true, null);
                object = Class.forName((String)object + "$ServerProvider", true, null);
                Class[] classArray2 = new Class[2];
                Object object2 = classArray2;
                classArray2[0] = SSLSocket.class;
                object2[1] = genericDeclaration;
                genericDeclaration = classArray.getMethod("put", (Class<?>)object2);
                Class[] classArray3 = new Class[1];
                Object object3 = classArray3;
                classArray3[0] = SSLSocket.class;
                object2 = classArray.getMethod("get", (Class<?>)object3);
                Class[] classArray4 = classArray;
                Class[] classArray5 = new Class[1];
                classArray = classArray5;
                classArray5[0] = SSLSocket.class;
                object3 = classArray4.getMethod("remove", classArray);
                Intrinsics.checkNotNullExpressionValue(genericDeclaration, "");
                Intrinsics.checkNotNullExpressionValue(object2, "");
                Intrinsics.checkNotNullExpressionValue(object3, "");
                Intrinsics.checkNotNullExpressionValue(clazz, "");
                Intrinsics.checkNotNullExpressionValue(object, "");
                return new Jdk8WithJettyBootPlatform((Method)genericDeclaration, (Method)object2, (Method)object3, clazz, (Class<?>)object);
            }
            catch (ClassNotFoundException classNotFoundException) {
            }
            catch (NoSuchMethodException noSuchMethodException) {}
            return null;
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }
}


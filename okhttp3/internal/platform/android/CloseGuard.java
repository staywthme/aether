/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.platform.android;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Method;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\b\u0000\u0018\u0000 \r2\u00020\u0001:\u0001\rB#\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\u0006J\u0010\u0010\u0007\u001a\u0004\u0018\u00010\u00012\u0006\u0010\b\u001a\u00020\tJ\u0010\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\u0001R\u0010\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2={"Lokhttp3/internal/platform/android/CloseGuard;", "", "getMethod", "Ljava/lang/reflect/Method;", "openMethod", "warnIfOpenMethod", "(Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;)V", "createAndOpen", "closer", "", "warnIfOpen", "", "closeGuardInstance", "Companion", "okhttp"})
public final class CloseGuard {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @Nullable
    private final Method getMethod;
    @Nullable
    private final Method openMethod;
    @Nullable
    private final Method warnIfOpenMethod;

    public CloseGuard(@Nullable Method method, @Nullable Method method2, @Nullable Method method3) {
        this.getMethod = method;
        this.openMethod = method2;
        this.warnIfOpenMethod = method3;
    }

    @Nullable
    public final Object createAndOpen(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, "");
        if (this.getMethod != null) {
            try {
                Object object = this.getMethod.invoke(null, new Object[0]);
                Method method = this.openMethod;
                Intrinsics.checkNotNull(method);
                Object[] objectArray = new Object[1];
                Object[] objectArray2 = objectArray;
                objectArray[0] = string;
                method.invoke(object, objectArray2);
                return object;
            }
            catch (Exception exception) {}
        }
        return null;
    }

    public final boolean warnIfOpen(@Nullable Object object) {
        boolean bl = false;
        if (object != null) {
            try {
                Method method = this.warnIfOpenMethod;
                Intrinsics.checkNotNull(method);
                method.invoke(object, new Object[0]);
                bl = true;
            }
            catch (Exception exception) {}
        }
        return bl;
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0003\u001a\u00020\u0004\u00a8\u0006\u0005"}, d2={"Lokhttp3/internal/platform/android/CloseGuard$Companion;", "", "()V", "get", "Lokhttp3/internal/platform/android/CloseGuard;", "okhttp"})
    public static final class Companion {
        private Companion() {
        }

        @NotNull
        public final CloseGuard get() {
            Object object;
            Method method;
            GenericDeclaration genericDeclaration;
            try {
                genericDeclaration = Class.forName("dalvik.system.CloseGuard");
                method = genericDeclaration.getMethod("get", new Class[0]);
                Class[] classArray = new Class[1];
                object = classArray;
                classArray[0] = String.class;
                object = genericDeclaration.getMethod("open", (Class<?>)object);
                genericDeclaration = genericDeclaration.getMethod("warnIfOpen", new Class[0]);
            }
            catch (Exception exception) {
                method = null;
                object = null;
                genericDeclaration = null;
            }
            return new CloseGuard(method, (Method)object, (Method)genericDeclaration);
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package kotlin.internal;

import kotlin.KotlinVersion;
import kotlin.Metadata;
import kotlin.PublishedApi;
import kotlin.SinceKotlin;
import kotlin.internal.InlineOnly;
import kotlin.internal.PlatformImplementations;
import kotlin.jvm.JvmField;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 8, 0}, k=2, xi=48, d1={"\u0000\u001e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0002\b\u0003\u001a \u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u0005H\u0001\u001a\"\u0010\b\u001a\u0002H\t\"\n\b\u0000\u0010\t\u0018\u0001*\u00020\n2\u0006\u0010\u000b\u001a\u00020\nH\u0083\b\u00a2\u0006\u0002\u0010\f\"\u0010\u0010\u0000\u001a\u00020\u00018\u0000X\u0081\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2={"IMPLEMENTATIONS", "Lkotlin/internal/PlatformImplementations;", "apiVersionIsAtLeast", "", "major", "", "minor", "patch", "castToBaseType", "T", "", "instance", "(Ljava/lang/Object;)Ljava/lang/Object;", "kotlin-stdlib"})
public final class PlatformImplementationsKt {
    @JvmField
    @NotNull
    public static final PlatformImplementations IMPLEMENTATIONS;

    @InlineOnly
    private static final /* synthetic */ <T> T castToBaseType(Object object) {
        try {
            Intrinsics.reifiedOperationMarker(1, "T");
            return (T)object;
        }
        catch (ClassCastException classCastException) {
            object = object.getClass().getClassLoader();
            Intrinsics.reifiedOperationMarker(4, "T");
            ClassLoader classLoader = Object.class.getClassLoader();
            if (!Intrinsics.areEqual(object, classLoader)) {
                throw new ClassNotFoundException("Instance class was loaded from a different classloader: " + object + ", base type classloader: " + classLoader, classCastException);
            }
            throw classCastException;
        }
    }

    @PublishedApi
    @SinceKotlin(version="1.2")
    public static final boolean apiVersionIsAtLeast(int n2, int n3, int n4) {
        return KotlinVersion.CURRENT.isAtLeast(n2, n3, n4);
    }

    static {
        PlatformImplementations platformImplementations;
        try {
            Object obj = Class.forName("kotlin.internal.jdk8.JDK8PlatformImplementations").newInstance();
            Intrinsics.checkNotNullExpressionValue(obj, "");
            Object object = obj;
            try {
                Object obj2 = object;
                if (obj2 == null) {
                    throw new NullPointerException("null cannot be cast to non-null type kotlin.internal.PlatformImplementations");
                }
                platformImplementations = (PlatformImplementations)obj2;
            }
            catch (ClassCastException classCastException) {
                object = object.getClass().getClassLoader();
                ClassLoader classLoader = PlatformImplementations.class.getClassLoader();
                if (!Intrinsics.areEqual(object, classLoader)) {
                    throw new ClassNotFoundException("Instance class was loaded from a different classloader: " + object + ", base type classloader: " + classLoader, classCastException);
                }
                throw classCastException;
            }
        }
        catch (ClassNotFoundException classNotFoundException) {
            try {
                Object obj = Class.forName("kotlin.internal.JRE8PlatformImplementations").newInstance();
                Intrinsics.checkNotNullExpressionValue(obj, "");
                Object object = obj;
                try {
                    Object obj3 = object;
                    if (obj3 == null) {
                        throw new NullPointerException("null cannot be cast to non-null type kotlin.internal.PlatformImplementations");
                    }
                    platformImplementations = (PlatformImplementations)obj3;
                }
                catch (ClassCastException classCastException) {
                    object = object.getClass().getClassLoader();
                    ClassLoader classLoader = PlatformImplementations.class.getClassLoader();
                    if (!Intrinsics.areEqual(object, classLoader)) {
                        throw new ClassNotFoundException("Instance class was loaded from a different classloader: " + object + ", base type classloader: " + classLoader, classCastException);
                    }
                    throw classCastException;
                }
            }
            catch (ClassNotFoundException classNotFoundException2) {
                try {
                    Object obj = Class.forName("kotlin.internal.jdk7.JDK7PlatformImplementations").newInstance();
                    Intrinsics.checkNotNullExpressionValue(obj, "");
                    Object object = obj;
                    try {
                        Object obj4 = object;
                        if (obj4 == null) {
                            throw new NullPointerException("null cannot be cast to non-null type kotlin.internal.PlatformImplementations");
                        }
                        platformImplementations = (PlatformImplementations)obj4;
                    }
                    catch (ClassCastException classCastException) {
                        object = object.getClass().getClassLoader();
                        ClassLoader classLoader = PlatformImplementations.class.getClassLoader();
                        if (!Intrinsics.areEqual(object, classLoader)) {
                            throw new ClassNotFoundException("Instance class was loaded from a different classloader: " + object + ", base type classloader: " + classLoader, classCastException);
                        }
                        throw classCastException;
                    }
                }
                catch (ClassNotFoundException classNotFoundException3) {
                    try {
                        Object obj = Class.forName("kotlin.internal.JRE7PlatformImplementations").newInstance();
                        Intrinsics.checkNotNullExpressionValue(obj, "");
                        Object object = obj;
                        try {
                            Object obj5 = object;
                            if (obj5 == null) {
                                throw new NullPointerException("null cannot be cast to non-null type kotlin.internal.PlatformImplementations");
                            }
                            platformImplementations = (PlatformImplementations)obj5;
                        }
                        catch (ClassCastException classCastException) {
                            object = object.getClass().getClassLoader();
                            ClassLoader classLoader = PlatformImplementations.class.getClassLoader();
                            if (!Intrinsics.areEqual(object, classLoader)) {
                                throw new ClassNotFoundException("Instance class was loaded from a different classloader: " + object + ", base type classloader: " + classLoader, classCastException);
                            }
                            throw classCastException;
                        }
                    }
                    catch (ClassNotFoundException classNotFoundException4) {
                        platformImplementations = new PlatformImplementations();
                    }
                }
            }
        }
        IMPLEMENTATIONS = platformImplementations;
    }
}


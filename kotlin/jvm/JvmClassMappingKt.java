/*
 * Decompiled with CFR 0.152.
 */
package kotlin.jvm;

import java.lang.annotation.Annotation;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.SinceKotlin;
import kotlin.internal.InlineOnly;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.ClassBasedDeclarationContainer;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=2, xi=48, d1={"\u00002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u001b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0010\n\u0002\b\n\n\u0002\u0010\u0000\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\u0010\u0011\n\u0002\b\u0002\u001a\u001f\u0010\u001f\u001a\u00020 \"\n\b\u0000\u0010\u0002\u0018\u0001*\u00020\u0014*\u0006\u0012\u0002\b\u00030!\u00a2\u0006\u0002\u0010\"\"'\u0010\u0000\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0001\"\b\b\u0000\u0010\u0002*\u00020\u0003*\u0002H\u00028F\u00a2\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005\";\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\b0\u0007\"\u000e\b\u0000\u0010\b*\b\u0012\u0004\u0012\u0002H\b0\t*\b\u0012\u0004\u0012\u0002H\b0\t8\u00c6\u0002X\u0087\u0004\u00a2\u0006\f\u0012\u0004\b\n\u0010\u000b\u001a\u0004\b\f\u0010\r\"-\u0010\u000e\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00018G\u00a2\u0006\f\u0012\u0004\b\u000f\u0010\u0010\u001a\u0004\b\u0011\u0010\u0012\"&\u0010\u0013\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007\"\b\b\u0000\u0010\u0002*\u00020\u0014*\u0002H\u00028\u00c6\u0002\u00a2\u0006\u0006\u001a\u0004\b\u0011\u0010\u0015\";\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00010\u0007\"\b\b\u0000\u0010\u0002*\u00020\u0014*\b\u0012\u0004\u0012\u0002H\u00020\u00018\u00c7\u0002X\u0087\u0004\u00a2\u0006\f\u0012\u0004\b\u0016\u0010\u0010\u001a\u0004\b\u0017\u0010\u0012\"+\u0010\u0018\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007\"\b\b\u0000\u0010\u0002*\u00020\u0014*\b\u0012\u0004\u0012\u0002H\u00020\u00018F\u00a2\u0006\u0006\u001a\u0004\b\u0019\u0010\u0012\"-\u0010\u001a\u001a\n\u0012\u0004\u0012\u0002H\u0002\u0018\u00010\u0007\"\b\b\u0000\u0010\u0002*\u00020\u0014*\b\u0012\u0004\u0012\u0002H\u00020\u00018F\u00a2\u0006\u0006\u001a\u0004\b\u001b\u0010\u0012\"+\u0010\u001c\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\b\b\u0000\u0010\u0002*\u00020\u0014*\b\u0012\u0004\u0012\u0002H\u00020\u00078G\u00a2\u0006\u0006\u001a\u0004\b\u001d\u0010\u001e\u00a8\u0006#"}, d2={"annotationClass", "Lkotlin/reflect/KClass;", "T", "", "getAnnotationClass", "(Ljava/lang/annotation/Annotation;)Lkotlin/reflect/KClass;", "declaringJavaClass", "Ljava/lang/Class;", "E", "", "getDeclaringJavaClass$annotations", "(Ljava/lang/Enum;)V", "getDeclaringJavaClass", "(Ljava/lang/Enum;)Ljava/lang/Class;", "java", "getJavaClass$annotations", "(Lkotlin/reflect/KClass;)V", "getJavaClass", "(Lkotlin/reflect/KClass;)Ljava/lang/Class;", "javaClass", "", "(Ljava/lang/Object;)Ljava/lang/Class;", "getRuntimeClassOfKClassInstance$annotations", "getRuntimeClassOfKClassInstance", "javaObjectType", "getJavaObjectType", "javaPrimitiveType", "getJavaPrimitiveType", "kotlin", "getKotlinClass", "(Ljava/lang/Class;)Lkotlin/reflect/KClass;", "isArrayOf", "", "", "([Ljava/lang/Object;)Z", "kotlin-stdlib"})
@JvmName(name="JvmClassMappingKt")
public final class JvmClassMappingKt {
    @JvmName(name="getJavaClass")
    @NotNull
    public static final <T> Class<T> getJavaClass(@NotNull KClass<T> kClass) {
        Intrinsics.checkNotNullParameter(kClass, "");
        Class<?> clazz = ((ClassBasedDeclarationContainer)((Object)kClass)).getJClass();
        Intrinsics.checkNotNull(clazz);
        return clazz;
    }

    public static /* synthetic */ void getJavaClass$annotations(KClass kClass) {
    }

    @Nullable
    public static final <T> Class<T> getJavaPrimitiveType(@NotNull KClass<T> object) {
        block32: {
            Intrinsics.checkNotNullParameter(object, "");
            object = ((ClassBasedDeclarationContainer)object).getJClass();
            if (((Class)object).isPrimitive()) {
                Intrinsics.checkNotNull(object);
                return object;
            }
            if ((object = ((Class)object).getName()) == null) break block32;
            int n2 = -1;
            switch (((String)object).hashCode()) {
                case -527879800: {
                    if (((String)object).equals("java.lang.Float")) {
                        n2 = 1;
                    }
                    break;
                }
                case 399092968: {
                    if (((String)object).equals("java.lang.Void")) {
                        n2 = 2;
                    }
                    break;
                }
                case 155276373: {
                    if (((String)object).equals("java.lang.Character")) {
                        n2 = 3;
                    }
                    break;
                }
                case 398795216: {
                    if (((String)object).equals("java.lang.Long")) {
                        n2 = 4;
                    }
                    break;
                }
                case 761287205: {
                    if (((String)object).equals("java.lang.Double")) {
                        n2 = 5;
                    }
                    break;
                }
                case -515992664: {
                    if (((String)object).equals("java.lang.Short")) {
                        n2 = 6;
                    }
                    break;
                }
                case 344809556: {
                    if (((String)object).equals("java.lang.Boolean")) {
                        n2 = 7;
                    }
                    break;
                }
                case 398507100: {
                    if (((String)object).equals("java.lang.Byte")) {
                        n2 = 8;
                    }
                    break;
                }
                case -2056817302: {
                    if (((String)object).equals("java.lang.Integer")) {
                        n2 = 9;
                    }
                    break;
                }
            }
            switch (n2) {
                case 7: {
                    return Boolean.TYPE;
                }
                case 3: {
                    return Character.TYPE;
                }
                case 8: {
                    return Byte.TYPE;
                }
                case 6: {
                    return Short.TYPE;
                }
                case 9: {
                    return Integer.TYPE;
                }
                case 1: {
                    return Float.TYPE;
                }
                case 4: {
                    return Long.TYPE;
                }
                case 5: {
                    return Double.TYPE;
                }
                case 2: {
                    return Void.TYPE;
                }
            }
        }
        return null;
    }

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    @NotNull
    public static final <T> Class<T> getJavaObjectType(@NotNull KClass<T> var0) {
        Intrinsics.checkNotNullParameter(var0, "");
        var0 = ((ClassBasedDeclarationContainer)var0).getJClass();
        if (!var0.isPrimitive()) {
            Intrinsics.checkNotNull(var0);
            return var0;
        }
        var1_1 = var0.getName();
        if (var1_1 == null) ** GOTO lbl-1000
        tmp = -1;
        switch (var1_1.hashCode()) {
            case 64711720: {
                if (var1_1.equals("boolean")) {
                    tmp = 1;
                }
                break;
            }
            case 3625364: {
                if (var1_1.equals("void")) {
                    tmp = 2;
                }
                break;
            }
            case 3039496: {
                if (var1_1.equals("byte")) {
                    tmp = 3;
                }
                break;
            }
            case -1325958191: {
                if (var1_1.equals("double")) {
                    tmp = 4;
                }
                break;
            }
            case 3052374: {
                if (var1_1.equals("char")) {
                    tmp = 5;
                }
                break;
            }
            case 109413500: {
                if (var1_1.equals("short")) {
                    tmp = 6;
                }
                break;
            }
            case 97526364: {
                if (var1_1.equals("float")) {
                    tmp = 7;
                }
                break;
            }
            case 104431: {
                if (var1_1.equals("int")) {
                    tmp = 8;
                }
                break;
            }
            case 3327612: {
                if (var1_1.equals("long")) {
                    tmp = 9;
                }
                break;
            }
        }
        switch (tmp) {
            case 1: {
                v0 /* !! */  = Boolean.class;
                break;
            }
            case 5: {
                v0 /* !! */  = Character.class;
                break;
            }
            case 3: {
                v0 /* !! */  = Byte.class;
                break;
            }
            case 6: {
                v0 /* !! */  = Short.class;
                break;
            }
            case 8: {
                v0 /* !! */  = Integer.class;
                break;
            }
            case 7: {
                v0 /* !! */  = Float.class;
                break;
            }
            case 9: {
                v0 /* !! */  = Long.class;
                break;
            }
            case 4: {
                v0 /* !! */  = Double.class;
                break;
            }
            case 2: {
                v0 /* !! */  = Void.class;
                break;
            }
            default: lbl-1000:
            // 2 sources

            {
                v0 /* !! */  = var0;
            }
        }
        Intrinsics.checkNotNull(v0 /* !! */ );
        return v0 /* !! */ ;
    }

    @JvmName(name="getKotlinClass")
    @NotNull
    public static final <T> KClass<T> getKotlinClass(@NotNull Class<T> clazz) {
        Intrinsics.checkNotNullParameter(clazz, "");
        return Reflection.getOrCreateKotlinClass(clazz);
    }

    @NotNull
    public static final <T> Class<T> getJavaClass(@NotNull T t2) {
        Intrinsics.checkNotNullParameter(t2, "");
        Class<?> clazz = t2.getClass();
        Intrinsics.checkNotNull(clazz);
        return clazz;
    }

    @JvmName(name="getRuntimeClassOfKClassInstance")
    @NotNull
    public static final <T> Class<KClass<T>> getRuntimeClassOfKClassInstance(@NotNull KClass<T> kClass) {
        Intrinsics.checkNotNullParameter(kClass, "");
        Class<KClass<T>> clazz = ((Object)kClass).getClass();
        Intrinsics.checkNotNull(clazz);
        return clazz;
    }

    @Deprecated(message="Use 'java' property to get Java class corresponding to this Kotlin class or cast this instance to Any if you really want to get the runtime Java class of this implementation of KClass.", replaceWith=@ReplaceWith(expression="(this as Any).javaClass", imports={}), level=DeprecationLevel.ERROR)
    public static /* synthetic */ void getRuntimeClassOfKClassInstance$annotations(KClass kClass) {
    }

    public static final /* synthetic */ boolean isArrayOf(Object[] objectArray) {
        Intrinsics.checkNotNullParameter(objectArray, "");
        Intrinsics.reifiedOperationMarker(4, "T");
        return Object.class.isAssignableFrom(objectArray.getClass().getComponentType());
    }

    @NotNull
    public static final <T extends Annotation> KClass<? extends T> getAnnotationClass(@NotNull T t2) {
        Intrinsics.checkNotNullParameter(t2, "");
        Class<? extends Annotation> clazz = t2.annotationType();
        Intrinsics.checkNotNullExpressionValue(clazz, "");
        KClass<? extends Annotation> kClass = JvmClassMappingKt.getKotlinClass(clazz);
        Intrinsics.checkNotNull(kClass);
        return kClass;
    }

    private static final <E extends Enum<E>> Class<E> getDeclaringJavaClass(Enum<E> enum_) {
        Intrinsics.checkNotNullParameter(enum_, "");
        Class<E> clazz = enum_.getDeclaringClass();
        Intrinsics.checkNotNullExpressionValue(clazz, "");
        return clazz;
    }

    @SinceKotlin(version="1.7")
    @InlineOnly
    public static /* synthetic */ void getDeclaringJavaClass$annotations(Enum enum_) {
    }
}


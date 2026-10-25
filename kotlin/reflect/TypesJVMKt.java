/*
 * Decompiled with CFR 0.152.
 */
package kotlin.reflect;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.ExperimentalStdlibApi;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.SinceKotlin;
import kotlin.collections.CollectionsKt;
import kotlin.internal.LowPriorityInOverloadResolution;
import kotlin.jvm.JvmClassMappingKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.KTypeBase;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.reflect.GenericArrayTypeImpl;
import kotlin.reflect.KClass;
import kotlin.reflect.KType;
import kotlin.reflect.KTypeParameter;
import kotlin.reflect.KTypeProjection;
import kotlin.reflect.KVariance;
import kotlin.reflect.ParameterizedTypeImpl;
import kotlin.reflect.TypeVariableImpl;
import kotlin.reflect.TypesJVMKt;
import kotlin.reflect.WildcardTypeImpl;
import kotlin.sequences.SequencesKt;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 8, 0}, k=2, xi=48, d1={"\u00000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\u001a\"\u0010\n\u001a\u00020\u00012\n\u0010\u000b\u001a\u0006\u0012\u0002\b\u00030\f2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00070\u000eH\u0003\u001a\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0001H\u0002\u001a\u0016\u0010\u0012\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0013\u001a\u00020\u0014H\u0003\"\u001e\u0010\u0000\u001a\u00020\u0001*\u00020\u00028FX\u0087\u0004\u00a2\u0006\f\u0012\u0004\b\u0003\u0010\u0004\u001a\u0004\b\u0005\u0010\u0006\"\u001e\u0010\u0000\u001a\u00020\u0001*\u00020\u00078BX\u0083\u0004\u00a2\u0006\f\u0012\u0004\b\u0003\u0010\b\u001a\u0004\b\u0005\u0010\t\u00a8\u0006\u0015"}, d2={"javaType", "Ljava/lang/reflect/Type;", "Lkotlin/reflect/KType;", "getJavaType$annotations", "(Lkotlin/reflect/KType;)V", "getJavaType", "(Lkotlin/reflect/KType;)Ljava/lang/reflect/Type;", "Lkotlin/reflect/KTypeProjection;", "(Lkotlin/reflect/KTypeProjection;)V", "(Lkotlin/reflect/KTypeProjection;)Ljava/lang/reflect/Type;", "createPossiblyInnerType", "jClass", "Ljava/lang/Class;", "arguments", "", "typeToString", "", "type", "computeJavaType", "forceWrapper", "", "kotlin-stdlib"})
@SourceDebugExtension(value={"SMAP\nTypesJVM.kt\nKotlin\n*S Kotlin\n*F\n+ 1 TypesJVM.kt\nkotlin/reflect/TypesJVMKt\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,230:1\n1#2:231\n1549#3:232\n1620#3,3:233\n1549#3:236\n1620#3,3:237\n1549#3:240\n1620#3,3:241\n*S KotlinDebug\n*F\n+ 1 TypesJVM.kt\nkotlin/reflect/TypesJVMKt\n*L\n69#1:232\n69#1:233,3\n71#1:236\n71#1:237,3\n77#1:240\n77#1:241,3\n*E\n"})
public final class TypesJVMKt {
    @NotNull
    public static final Type getJavaType(@NotNull KType object) {
        Type type;
        Intrinsics.checkNotNullParameter(object, "");
        if (object instanceof KTypeBase && (type = ((KTypeBase)object).getJavaType()) != null) {
            Type type2 = type;
            object = type2;
            object = type2;
            return type2;
        }
        return TypesJVMKt.computeJavaType$default((KType)object, false, 1, null);
    }

    @SinceKotlin(version="1.4")
    @ExperimentalStdlibApi
    @LowPriorityInOverloadResolution
    public static /* synthetic */ void getJavaType$annotations(KType kType) {
    }

    @ExperimentalStdlibApi
    private static final Type computeJavaType(KType object, boolean bl) {
        Object object2 = object.getClassifier();
        if (object2 instanceof KTypeParameter) {
            return new TypeVariableImpl((KTypeParameter)object2);
        }
        if (object2 instanceof KClass) {
            Class<Object> clazz = bl ? JvmClassMappingKt.getJavaObjectType((KClass)object2) : JvmClassMappingKt.getJavaClass((KClass)object2);
            object2 = object.getArguments();
            if (object2.isEmpty()) {
                return clazz;
            }
            if (clazz.isArray()) {
                if (clazz.getComponentType().isPrimitive()) {
                    return clazz;
                }
                KTypeProjection kTypeProjection = (KTypeProjection)CollectionsKt.singleOrNull(object2);
                if (kTypeProjection == null) {
                    throw new IllegalArgumentException("kotlin.Array must have exactly one type argument: " + object);
                }
                object = kTypeProjection;
                object2 = kTypeProjection.component1();
                object = ((KTypeProjection)object).component2();
                Object object3 = object2;
                switch (object3 == null ? -1 : WhenMappings.$EnumSwitchMapping$0[((Enum)object3).ordinal()]) {
                    case -1: 
                    case 1: {
                        return clazz;
                    }
                    case 2: 
                    case 3: {
                        Object object4 = object;
                        Intrinsics.checkNotNull(object4);
                        object = TypesJVMKt.computeJavaType$default((KType)object4, false, 1, null);
                        if (object instanceof Class) {
                            return clazz;
                        }
                        return new GenericArrayTypeImpl((Type)object);
                    }
                }
                throw new NoWhenBranchMatchedException();
            }
            return TypesJVMKt.createPossiblyInnerType(clazz, (List<KTypeProjection>)object2);
        }
        throw new UnsupportedOperationException("Unsupported type classifier: " + object);
    }

    static /* synthetic */ Type computeJavaType$default(KType kType, boolean bl, int n2, Object object) {
        if ((n2 & 1) != 0) {
            bl = false;
        }
        return TypesJVMKt.computeJavaType(kType, bl);
    }

    @ExperimentalStdlibApi
    private static final Type createPossiblyInnerType(Class<?> object, List<KTypeProjection> object2) {
        Object object3;
        Class<?> clazz = ((Class)object).getDeclaringClass();
        if (clazz == null) {
            Object object4;
            Class<?> clazz2 = object;
            object = (Iterable)object2;
            Class<?> clazz3 = clazz2;
            object2 = object;
            object = new ArrayList(CollectionsKt.collectionSizeOrDefault(object, 10));
            Iterator iterator2 = object2.iterator();
            while (iterator2.hasNext()) {
                clazz = iterator2.next();
                object2 = (KTypeProjection)((Object)clazz);
                object4 = object;
                object4.add(TypesJVMKt.getJavaType((KTypeProjection)object2));
            }
            object = object4 = (List)object;
            object2 = clazz3;
            return new ParameterizedTypeImpl((Class<?>)object2, null, (List<? extends Type>)object);
        }
        Iterator iterator3 = clazz;
        if (Modifier.isStatic(((Class)object).getModifiers())) {
            Object object5;
            clazz = (Iterable)object2;
            Type type = (Type)((Object)iterator3);
            Iterator iterator4 = object;
            object = clazz;
            object2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(clazz, 10));
            object = object.iterator();
            while (object.hasNext()) {
                iterator3 = object.next();
                clazz = (KTypeProjection)((Object)iterator3);
                object5 = object2;
                object5.add(TypesJVMKt.getJavaType((KTypeProjection)((Object)clazz)));
            }
            object = object5 = (List)object2;
            object2 = type;
            iterator3 = iterator4;
            return new ParameterizedTypeImpl((Class<?>)((Object)iterator3), (Type)object2, (List<? extends Type>)object);
        }
        int n2 = ((Class)object).getTypeParameters().length;
        Iterator iterator5 = object;
        object = object2.subList(0, n2);
        Type type = TypesJVMKt.createPossiblyInnerType(iterator3, object2.subList(n2, object2.size()));
        Iterator iterator6 = iterator5;
        object2 = object;
        object = new ArrayList(CollectionsKt.collectionSizeOrDefault(object, 10));
        iterator3 = object2.iterator();
        while (iterator3.hasNext()) {
            Object t2 = iterator3.next();
            object2 = (KTypeProjection)t2;
            object3 = object;
            object3.add(TypesJVMKt.getJavaType((KTypeProjection)object2));
        }
        object = object3 = (List)object;
        object2 = type;
        iterator3 = iterator6;
        return new ParameterizedTypeImpl((Class<?>)((Object)iterator3), (Type)object2, (List<? extends Type>)object);
    }

    private static final Type getJavaType(KTypeProjection object) {
        KVariance kVariance = ((KTypeProjection)object).getVariance();
        if (kVariance == null) {
            return WildcardTypeImpl.Companion.getSTAR();
        }
        KVariance kVariance2 = kVariance;
        KType kType = ((KTypeProjection)object).getType();
        Intrinsics.checkNotNull(kType);
        object = kType;
        switch (WhenMappings.$EnumSwitchMapping$0[kVariance2.ordinal()]) {
            case 2: {
                return TypesJVMKt.computeJavaType((KType)object, true);
            }
            case 1: {
                return new WildcardTypeImpl(null, TypesJVMKt.computeJavaType((KType)object, true));
            }
            case 3: {
                return new WildcardTypeImpl(TypesJVMKt.computeJavaType((KType)object, true), null);
            }
        }
        throw new NoWhenBranchMatchedException();
    }

    @ExperimentalStdlibApi
    private static /* synthetic */ void getJavaType$annotations(KTypeProjection kTypeProjection) {
    }

    private static final String typeToString(Type object) {
        if (object instanceof Class) {
            String string;
            if (((Class)object).isArray()) {
                object = SequencesKt.generateSequence(object, (Function1)typeToString.unwrap.1.INSTANCE);
                string = ((Class)SequencesKt.last(object)).getName() + StringsKt.repeat("[]", SequencesKt.count(object));
            } else {
                string = ((Class)object).getName();
            }
            object = string;
            Intrinsics.checkNotNullExpressionValue(string, "");
            return object;
        }
        return object.toString();
    }

    public static final /* synthetic */ Type access$computeJavaType(KType kType, boolean bl) {
        return TypesJVMKt.computeJavaType(kType, bl);
    }

    public static final /* synthetic */ String access$typeToString(Type type) {
        return TypesJVMKt.typeToString(type);
    }

    @Metadata(mv={1, 8, 0}, k=3, xi=48)
    public final class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] nArray = new int[KVariance.values().length];
            try {
                nArray[KVariance.IN.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                nArray[KVariance.INVARIANT.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                nArray[KVariance.OUT.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            $EnumSwitchMapping$0 = nArray;
        }
    }
}


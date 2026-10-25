/*
 * Decompiled with CFR 0.152.
 */
package kotlin.jvm.internal;

import java.lang.annotation.Annotation;
import java.lang.constant.Constable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Function;
import kotlin.KotlinNothingValueException;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.SinceKotlin;
import kotlin.TuplesKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.JvmClassMappingKt;
import kotlin.jvm.KotlinReflectionNotSupportedError;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function10;
import kotlin.jvm.functions.Function11;
import kotlin.jvm.functions.Function12;
import kotlin.jvm.functions.Function13;
import kotlin.jvm.functions.Function14;
import kotlin.jvm.functions.Function15;
import kotlin.jvm.functions.Function16;
import kotlin.jvm.functions.Function17;
import kotlin.jvm.functions.Function18;
import kotlin.jvm.functions.Function19;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function20;
import kotlin.jvm.functions.Function21;
import kotlin.jvm.functions.Function22;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.functions.Function4;
import kotlin.jvm.functions.Function5;
import kotlin.jvm.functions.Function6;
import kotlin.jvm.functions.Function7;
import kotlin.jvm.functions.Function8;
import kotlin.jvm.functions.Function9;
import kotlin.jvm.internal.ClassBasedDeclarationContainer;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlin.reflect.KCallable;
import kotlin.reflect.KClass;
import kotlin.reflect.KFunction;
import kotlin.reflect.KType;
import kotlin.reflect.KTypeParameter;
import kotlin.reflect.KVisibility;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000p\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0010\u001b\n\u0002\b\u0003\n\u0002\u0010\u001e\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0016\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0001\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\u0018\u0000 O2\b\u0012\u0004\u0012\u00020\u00020\u00012\u00020\u0003:\u0001OB\u0011\u0012\n\u0010\u0004\u001a\u0006\u0012\u0002\b\u00030\u0005\u00a2\u0006\u0002\u0010\u0006J\u0013\u0010F\u001a\u00020\u00122\b\u0010G\u001a\u0004\u0018\u00010\u0002H\u0096\u0002J\b\u0010H\u001a\u00020IH\u0002J\b\u0010J\u001a\u00020KH\u0016J\u0012\u0010L\u001a\u00020\u00122\b\u0010M\u001a\u0004\u0018\u00010\u0002H\u0017J\b\u0010N\u001a\u000201H\u0016R\u001a\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\u000bR \u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00020\u000e0\r8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010R\u001a\u0010\u0011\u001a\u00020\u00128VX\u0097\u0004\u00a2\u0006\f\u0012\u0004\b\u0013\u0010\u0014\u001a\u0004\b\u0011\u0010\u0015R\u001a\u0010\u0016\u001a\u00020\u00128VX\u0097\u0004\u00a2\u0006\f\u0012\u0004\b\u0017\u0010\u0014\u001a\u0004\b\u0016\u0010\u0015R\u001a\u0010\u0018\u001a\u00020\u00128VX\u0097\u0004\u00a2\u0006\f\u0012\u0004\b\u0019\u0010\u0014\u001a\u0004\b\u0018\u0010\u0015R\u001a\u0010\u001a\u001a\u00020\u00128VX\u0097\u0004\u00a2\u0006\f\u0012\u0004\b\u001b\u0010\u0014\u001a\u0004\b\u001a\u0010\u0015R\u001a\u0010\u001c\u001a\u00020\u00128VX\u0097\u0004\u00a2\u0006\f\u0012\u0004\b\u001d\u0010\u0014\u001a\u0004\b\u001c\u0010\u0015R\u001a\u0010\u001e\u001a\u00020\u00128VX\u0097\u0004\u00a2\u0006\f\u0012\u0004\b\u001f\u0010\u0014\u001a\u0004\b\u001e\u0010\u0015R\u001a\u0010 \u001a\u00020\u00128VX\u0097\u0004\u00a2\u0006\f\u0012\u0004\b!\u0010\u0014\u001a\u0004\b \u0010\u0015R\u001a\u0010\"\u001a\u00020\u00128VX\u0097\u0004\u00a2\u0006\f\u0012\u0004\b#\u0010\u0014\u001a\u0004\b\"\u0010\u0015R\u001a\u0010$\u001a\u00020\u00128VX\u0097\u0004\u00a2\u0006\f\u0012\u0004\b%\u0010\u0014\u001a\u0004\b$\u0010\u0015R\u0018\u0010\u0004\u001a\u0006\u0012\u0002\b\u00030\u0005X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010'R\u001e\u0010(\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030)0\r8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b*\u0010\u0010R\u001e\u0010+\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u00010\r8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b,\u0010\u0010R\u0016\u0010-\u001a\u0004\u0018\u00010\u00028VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b.\u0010/R\u0016\u00100\u001a\u0004\u0018\u0001018VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b2\u00103R(\u00104\u001a\u0010\u0012\f\u0012\n\u0012\u0006\b\u0001\u0012\u00020\u00020\u00010\b8VX\u0097\u0004\u00a2\u0006\f\u0012\u0004\b5\u0010\u0014\u001a\u0004\b6\u0010\u000bR\u0016\u00107\u001a\u0004\u0018\u0001018VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b8\u00103R \u00109\u001a\b\u0012\u0004\u0012\u00020:0\b8VX\u0097\u0004\u00a2\u0006\f\u0012\u0004\b;\u0010\u0014\u001a\u0004\b<\u0010\u000bR \u0010=\u001a\b\u0012\u0004\u0012\u00020>0\b8VX\u0097\u0004\u00a2\u0006\f\u0012\u0004\b?\u0010\u0014\u001a\u0004\b@\u0010\u000bR\u001c\u0010A\u001a\u0004\u0018\u00010B8VX\u0097\u0004\u00a2\u0006\f\u0012\u0004\bC\u0010\u0014\u001a\u0004\bD\u0010E\u00a8\u0006P"}, d2={"Lkotlin/jvm/internal/ClassReference;", "Lkotlin/reflect/KClass;", "", "Lkotlin/jvm/internal/ClassBasedDeclarationContainer;", "jClass", "Ljava/lang/Class;", "(Ljava/lang/Class;)V", "annotations", "", "", "getAnnotations", "()Ljava/util/List;", "constructors", "", "Lkotlin/reflect/KFunction;", "getConstructors", "()Ljava/util/Collection;", "isAbstract", "", "isAbstract$annotations", "()V", "()Z", "isCompanion", "isCompanion$annotations", "isData", "isData$annotations", "isFinal", "isFinal$annotations", "isFun", "isFun$annotations", "isInner", "isInner$annotations", "isOpen", "isOpen$annotations", "isSealed", "isSealed$annotations", "isValue", "isValue$annotations", "getJClass", "()Ljava/lang/Class;", "members", "Lkotlin/reflect/KCallable;", "getMembers", "nestedClasses", "getNestedClasses", "objectInstance", "getObjectInstance", "()Ljava/lang/Object;", "qualifiedName", "", "getQualifiedName", "()Ljava/lang/String;", "sealedSubclasses", "getSealedSubclasses$annotations", "getSealedSubclasses", "simpleName", "getSimpleName", "supertypes", "Lkotlin/reflect/KType;", "getSupertypes$annotations", "getSupertypes", "typeParameters", "Lkotlin/reflect/KTypeParameter;", "getTypeParameters$annotations", "getTypeParameters", "visibility", "Lkotlin/reflect/KVisibility;", "getVisibility$annotations", "getVisibility", "()Lkotlin/reflect/KVisibility;", "equals", "other", "error", "", "hashCode", "", "isInstance", "value", "toString", "Companion", "kotlin-stdlib"})
@SourceDebugExtension(value={"SMAP\nClassReference.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ClassReference.kt\nkotlin/jvm/internal/ClassReference\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 3 Maps.kt\nkotlin/collections/MapsKt__MapsKt\n*L\n1#1,205:1\n1559#2:206\n1590#2,4:207\n1253#2,4:211\n1238#2,4:217\n442#3:215\n392#3:216\n*S KotlinDebug\n*F\n+ 1 ClassReference.kt\nkotlin/jvm/internal/ClassReference\n*L\n107#1:206\n107#1:207,4\n155#1:211,4\n163#1:217,4\n163#1:215\n163#1:216\n*E\n"})
public final class ClassReference
implements ClassBasedDeclarationContainer,
KClass<Object> {
    @NotNull
    public static final Companion Companion;
    @NotNull
    private final Class<?> jClass;
    @NotNull
    private static final Map<Class<? extends Function<?>>, Integer> FUNCTION_CLASSES;
    @NotNull
    private static final HashMap<String, String> primitiveFqNames;
    @NotNull
    private static final HashMap<String, String> primitiveWrapperFqNames;
    @NotNull
    private static final HashMap<String, String> classFqNames;
    @NotNull
    private static final Map<String, String> simpleNames;

    public ClassReference(@NotNull Class<?> clazz) {
        Intrinsics.checkNotNullParameter(clazz, "");
        this.jClass = clazz;
    }

    @Override
    @NotNull
    public final Class<?> getJClass() {
        return this.jClass;
    }

    @Override
    @Nullable
    public final String getSimpleName() {
        return Companion.getClassSimpleName(this.getJClass());
    }

    @Override
    @Nullable
    public final String getQualifiedName() {
        return Companion.getClassQualifiedName(this.getJClass());
    }

    @Override
    @NotNull
    public final Collection<KCallable<?>> getMembers() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @Override
    @NotNull
    public final Collection<KFunction<Object>> getConstructors() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @Override
    @NotNull
    public final Collection<KClass<?>> getNestedClasses() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @Override
    @NotNull
    public final List<Annotation> getAnnotations() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @Override
    @Nullable
    public final Object getObjectInstance() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @Override
    @SinceKotlin(version="1.1")
    public final boolean isInstance(@Nullable Object object) {
        return Companion.isInstance(object, this.getJClass());
    }

    @Override
    @NotNull
    public final List<KTypeParameter> getTypeParameters() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @SinceKotlin(version="1.1")
    public static /* synthetic */ void getTypeParameters$annotations() {
    }

    @Override
    @NotNull
    public final List<KType> getSupertypes() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @SinceKotlin(version="1.1")
    public static /* synthetic */ void getSupertypes$annotations() {
    }

    @Override
    @NotNull
    public final List<KClass<? extends Object>> getSealedSubclasses() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @SinceKotlin(version="1.3")
    public static /* synthetic */ void getSealedSubclasses$annotations() {
    }

    @Override
    @Nullable
    public final KVisibility getVisibility() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @SinceKotlin(version="1.1")
    public static /* synthetic */ void getVisibility$annotations() {
    }

    @Override
    public final boolean isFinal() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @SinceKotlin(version="1.1")
    public static /* synthetic */ void isFinal$annotations() {
    }

    @Override
    public final boolean isOpen() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @SinceKotlin(version="1.1")
    public static /* synthetic */ void isOpen$annotations() {
    }

    @Override
    public final boolean isAbstract() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @SinceKotlin(version="1.1")
    public static /* synthetic */ void isAbstract$annotations() {
    }

    @Override
    public final boolean isSealed() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @SinceKotlin(version="1.1")
    public static /* synthetic */ void isSealed$annotations() {
    }

    @Override
    public final boolean isData() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @SinceKotlin(version="1.1")
    public static /* synthetic */ void isData$annotations() {
    }

    @Override
    public final boolean isInner() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @SinceKotlin(version="1.1")
    public static /* synthetic */ void isInner$annotations() {
    }

    @Override
    public final boolean isCompanion() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @SinceKotlin(version="1.1")
    public static /* synthetic */ void isCompanion$annotations() {
    }

    @Override
    public final boolean isFun() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @SinceKotlin(version="1.4")
    public static /* synthetic */ void isFun$annotations() {
    }

    @Override
    public final boolean isValue() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @SinceKotlin(version="1.5")
    public static /* synthetic */ void isValue$annotations() {
    }

    private final Void error() {
        throw new KotlinReflectionNotSupportedError();
    }

    @Override
    public final boolean equals(@Nullable Object object) {
        return object instanceof ClassReference && Intrinsics.areEqual(JvmClassMappingKt.getJavaObjectType(this), JvmClassMappingKt.getJavaObjectType((KClass)object));
    }

    @Override
    public final int hashCode() {
        return JvmClassMappingKt.getJavaObjectType(this).hashCode();
    }

    @NotNull
    public final String toString() {
        return this.getJClass().toString() + " (Kotlin reflection is not available)";
    }

    static {
        Object object;
        Object object2;
        Companion = new Companion(null);
        Class[] classArray = new Class[23];
        Object object3 = classArray;
        classArray[0] = Function0.class;
        object3[1] = Function1.class;
        object3[2] = Function2.class;
        object3[3] = Function3.class;
        object3[4] = Function4.class;
        object3[5] = Function5.class;
        object3[6] = Function6.class;
        object3[7] = Function7.class;
        object3[8] = Function8.class;
        object3[9] = Function9.class;
        object3[10] = Function10.class;
        object3[11] = Function11.class;
        object3[12] = Function12.class;
        object3[13] = Function13.class;
        object3[14] = Function14.class;
        object3[15] = Function15.class;
        object3[16] = Function16.class;
        object3[17] = Function17.class;
        object3[18] = Function18.class;
        object3[19] = Function19.class;
        object3[20] = Function20.class;
        object3[21] = Function21.class;
        object3[22] = Function22.class;
        Object object4 = object3 = (Iterable)CollectionsKt.listOf(object3);
        Object object5 = new ArrayList(CollectionsKt.collectionSizeOrDefault(object3, 10));
        int n2 = 0;
        Iterator object62 = object4.iterator();
        while (object62.hasNext()) {
            int n3;
            object2 = object62.next();
            if ((n3 = n2++) < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            object3 = (Class)object2;
            int n4 = n3;
            object = object5;
            object.add(TuplesKt.to(object3, n4));
        }
        FUNCTION_CLASSES = MapsKt.toMap((List)object5);
        object4 = object3 = new HashMap<String, String>();
        ((HashMap)object3).put("boolean", "kotlin.Boolean");
        ((HashMap)object4).put("char", "kotlin.Char");
        ((HashMap)object4).put("byte", "kotlin.Byte");
        ((HashMap)object4).put("short", "kotlin.Short");
        ((HashMap)object4).put("int", "kotlin.Int");
        ((HashMap)object4).put("float", "kotlin.Float");
        ((HashMap)object4).put("long", "kotlin.Long");
        ((HashMap)object4).put("double", "kotlin.Double");
        primitiveFqNames = object3;
        object4 = object3 = new HashMap<String, String>();
        ((HashMap)object3).put("java.lang.Boolean", "kotlin.Boolean");
        ((HashMap)object4).put("java.lang.Character", "kotlin.Char");
        ((HashMap)object4).put("java.lang.Byte", "kotlin.Byte");
        ((HashMap)object4).put("java.lang.Short", "kotlin.Short");
        ((HashMap)object4).put("java.lang.Integer", "kotlin.Int");
        ((HashMap)object4).put("java.lang.Float", "kotlin.Float");
        ((HashMap)object4).put("java.lang.Long", "kotlin.Long");
        ((HashMap)object4).put("java.lang.Double", "kotlin.Double");
        primitiveWrapperFqNames = object3;
        object4 = object3 = new HashMap<String, String>();
        ((HashMap)object3).put("java.lang.Object", "kotlin.Any");
        ((HashMap)object4).put("java.lang.String", "kotlin.String");
        ((HashMap)object4).put("java.lang.CharSequence", "kotlin.CharSequence");
        ((HashMap)object4).put("java.lang.Throwable", "kotlin.Throwable");
        ((HashMap)object4).put("java.lang.Cloneable", "kotlin.Cloneable");
        ((HashMap)object4).put("java.lang.Number", "kotlin.Number");
        ((HashMap)object4).put("java.lang.Comparable", "kotlin.Comparable");
        ((HashMap)object4).put("java.lang.Enum", "kotlin.Enum");
        ((HashMap)object4).put("java.lang.annotation.Annotation", "kotlin.Annotation");
        ((HashMap)object4).put("java.lang.Iterable", "kotlin.collections.Iterable");
        ((HashMap)object4).put("java.util.Iterator", "kotlin.collections.Iterator");
        ((HashMap)object4).put("java.util.Collection", "kotlin.collections.Collection");
        ((HashMap)object4).put("java.util.List", "kotlin.collections.List");
        ((HashMap)object4).put("java.util.Set", "kotlin.collections.Set");
        ((HashMap)object4).put("java.util.ListIterator", "kotlin.collections.ListIterator");
        ((HashMap)object4).put("java.util.Map", "kotlin.collections.Map");
        ((HashMap)object4).put("java.util.Map$Entry", "kotlin.collections.Map.Entry");
        ((HashMap)object4).put("kotlin.jvm.internal.StringCompanionObject", "kotlin.String.Companion");
        ((HashMap)object4).put("kotlin.jvm.internal.EnumCompanionObject", "kotlin.Enum.Companion");
        ((HashMap)object4).putAll((Map)primitiveFqNames);
        ((HashMap)object4).putAll((Map)primitiveWrapperFqNames);
        Collection<String> collection = primitiveFqNames.values();
        Intrinsics.checkNotNullExpressionValue(collection, "");
        object5 = collection;
        Object object7 = object5.iterator();
        while (object7.hasNext()) {
            Object t2 = object7.next();
            object2 = (Map)object4;
            object = (String)t2;
            StringBuilder stringBuilder = new StringBuilder("kotlin.jvm.internal.");
            Intrinsics.checkNotNullExpressionValue(object, "");
            object = TuplesKt.to(stringBuilder.append(StringsKt.substringAfterLast$default((String)object, '.', null, 2, null)).append("CompanionObject").toString(), (String)object + ".Companion");
            object2.put(((Pair)object).getFirst(), ((Pair)object).getSecond());
        }
        Map cfr_ignored_0 = (Map)object4;
        for (Map.Entry entry : FUNCTION_CLASSES.entrySet()) {
            object7 = (Class)entry.getKey();
            int n4 = ((Number)entry.getValue()).intValue();
            ((HashMap)object4).put((String)((Class)object7).getName(), (String)("kotlin.Function" + n4));
        }
        classFqNames = object3;
        object4 = object3 = (Map)object3;
        object5 = new LinkedHashMap(MapsKt.mapCapacity(object3.size()));
        object7 = object4.entrySet();
        object2 = object7.iterator();
        while (object2.hasNext()) {
            object = object2.next();
            object3 = (Map.Entry)object;
            object4 = object5;
            object3 = (Map.Entry)object;
            object = object4 = object3.getKey();
            object3 = (String)object3.getValue();
            object3 = StringsKt.substringAfterLast$default((String)object3, '.', null, 2, null);
            object.put(object4, object3);
        }
        simpleNames = object5;
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0014\u0010\u000f\u001a\u0004\u0018\u00010\n2\n\u0010\u0010\u001a\u0006\u0012\u0002\b\u00030\u0005J\u0014\u0010\u0011\u001a\u0004\u0018\u00010\n2\n\u0010\u0010\u001a\u0006\u0012\u0002\b\u00030\u0005J\u001c\u0010\u0012\u001a\u00020\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u00012\n\u0010\u0010\u001a\u0006\u0012\u0002\b\u00030\u0005R&\u0010\u0003\u001a\u001a\u0012\u0010\u0012\u000e\u0012\n\b\u0001\u0012\u0006\u0012\u0002\b\u00030\u00060\u0005\u0012\u0004\u0012\u00020\u00070\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R*\u0010\b\u001a\u001e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n0\tj\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n`\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R*\u0010\f\u001a\u001e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n0\tj\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n`\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R*\u0010\r\u001a\u001e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n0\tj\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n`\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n0\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2={"Lkotlin/jvm/internal/ClassReference$Companion;", "", "()V", "FUNCTION_CLASSES", "", "Ljava/lang/Class;", "Lkotlin/Function;", "", "classFqNames", "Ljava/util/HashMap;", "", "Lkotlin/collections/HashMap;", "primitiveFqNames", "primitiveWrapperFqNames", "simpleNames", "getClassQualifiedName", "jClass", "getClassSimpleName", "isInstance", "", "value", "kotlin-stdlib"})
    @SourceDebugExtension(value={"SMAP\nClassReference.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ClassReference.kt\nkotlin/jvm/internal/ClassReference$Companion\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,205:1\n1#2:206\n*E\n"})
    public static final class Companion {
        private Companion() {
        }

        @Nullable
        public final String getClassSimpleName(@NotNull Class<?> object) {
            Object object2;
            block11: {
                block9: {
                    Executable executable;
                    String string;
                    block10: {
                        Intrinsics.checkNotNullParameter(object, "");
                        if (((Class)object).isAnonymousClass()) {
                            return null;
                        }
                        if (!((Class)object).isLocalClass()) break block9;
                        string = ((Class)object).getSimpleName();
                        object2 = ((Class)object).getEnclosingMethod();
                        if (object2 == null) break block10;
                        executable = object2;
                        executable = object2;
                        Intrinsics.checkNotNullExpressionValue(string, "");
                        object2 = StringsKt.substringAfter$default(string, ((Method)executable).getName() + '$', null, 2, null);
                        if (object2 != null) break block11;
                    }
                    Constructor<?> constructor = ((Class)object).getEnclosingConstructor();
                    if (constructor != null) {
                        executable = constructor;
                        object = constructor;
                        Intrinsics.checkNotNullExpressionValue(string, "");
                        return StringsKt.substringAfter$default(string, ((Constructor)object).getName() + '$', null, 2, null);
                    }
                    Intrinsics.checkNotNullExpressionValue(string, "");
                    return StringsKt.substringAfter$default(string, '$', null, 2, null);
                }
                if (((Class)object).isArray()) {
                    Class<?> clazz = ((Class)object).getComponentType();
                    if ((clazz.isPrimitive() ? ((object = (String)simpleNames.get(clazz.getName())) != null ? (String)object + "Array" : null) : (object2 = null)) == null) {
                        return "Array";
                    }
                } else {
                    object2 = (String)simpleNames.get(((Class)object).getName());
                    if (object2 == null) {
                        object2 = ((Class)object).getSimpleName();
                    }
                }
            }
            return object2;
        }

        @Nullable
        public final String getClassQualifiedName(@NotNull Class<?> object) {
            String string;
            Intrinsics.checkNotNullParameter(object, "");
            if (((Class)object).isAnonymousClass()) {
                return null;
            }
            if (((Class)object).isLocalClass()) {
                return null;
            }
            if (((Class)object).isArray()) {
                if ((((Class)(object = ((Class)object).getComponentType())).isPrimitive() ? ((object = (String)classFqNames.get(((Class)object).getName())) != null ? (String)object + "Array" : null) : (string = null)) == null) {
                    return "kotlin.Array";
                }
            } else {
                string = (String)classFqNames.get(((Class)object).getName());
                if (string == null) {
                    string = ((Class)object).getCanonicalName();
                }
            }
            return string;
        }

        public final boolean isInstance(@Nullable Object object, @NotNull Class<?> constable) {
            Intrinsics.checkNotNullParameter(constable, "");
            Map map = FUNCTION_CLASSES;
            Intrinsics.checkNotNull(map);
            Constable constable2 = (Integer)map.get(constable);
            if (constable2 != null) {
                constable = constable2;
                int n2 = ((Number)((Object)constable)).intValue();
                return TypeIntrinsics.isFunctionOfArity(object, n2);
            }
            constable2 = ((Class)constable).isPrimitive() ? JvmClassMappingKt.getJavaObjectType(JvmClassMappingKt.getKotlinClass(constable)) : constable;
            return ((Class)constable2).isInstance(object);
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }
}


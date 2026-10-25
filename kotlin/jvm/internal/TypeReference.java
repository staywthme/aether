/*
 * Decompiled with CFR 0.152.
 */
package kotlin.jvm.internal;

import java.lang.annotation.Annotation;
import java.util.List;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.SinceKotlin;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmClassMappingKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.reflect.KClass;
import kotlin.reflect.KClassifier;
import kotlin.reflect.KType;
import kotlin.reflect.KTypeProjection;
import kotlin.reflect.KVariance;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u001b\n\u0002\b\u000e\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0000\n\u0002\b\u0004\b\u0007\u0018\u0000 )2\u00020\u0001:\u0001)B%\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\u0006\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tB/\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\b\u0010\n\u001a\u0004\u0018\u00010\u0001\u0012\u0006\u0010\u000b\u001a\u00020\f\u00a2\u0006\u0002\u0010\rJ\u0010\u0010\"\u001a\u00020\u001e2\u0006\u0010#\u001a\u00020\bH\u0002J\u0013\u0010$\u001a\u00020\b2\b\u0010%\u001a\u0004\u0018\u00010&H\u0096\u0002J\b\u0010'\u001a\u00020\fH\u0016J\b\u0010(\u001a\u00020\u001eH\u0016J\f\u0010\"\u001a\u00020\u001e*\u00020\u0006H\u0002R\u001a\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000f0\u00058VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011R\u001a\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0011R\u0014\u0010\u0002\u001a\u00020\u0003X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u001c\u0010\u000b\u001a\u00020\f8\u0000X\u0081\u0004\u00a2\u0006\u000e\n\u0000\u0012\u0004\b\u0015\u0010\u0016\u001a\u0004\b\u0017\u0010\u0018R\u0014\u0010\u0007\u001a\u00020\b8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\u0019R\u001e\u0010\n\u001a\u0004\u0018\u00010\u00018\u0000X\u0081\u0004\u00a2\u0006\u000e\n\u0000\u0012\u0004\b\u001a\u0010\u0016\u001a\u0004\b\u001b\u0010\u001cR\u001c\u0010\u001d\u001a\u00020\u001e*\u0006\u0012\u0002\b\u00030\u001f8BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b \u0010!\u00a8\u0006*"}, d2={"Lkotlin/jvm/internal/TypeReference;", "Lkotlin/reflect/KType;", "classifier", "Lkotlin/reflect/KClassifier;", "arguments", "", "Lkotlin/reflect/KTypeProjection;", "isMarkedNullable", "", "(Lkotlin/reflect/KClassifier;Ljava/util/List;Z)V", "platformTypeUpperBound", "flags", "", "(Lkotlin/reflect/KClassifier;Ljava/util/List;Lkotlin/reflect/KType;I)V", "annotations", "", "getAnnotations", "()Ljava/util/List;", "getArguments", "getClassifier", "()Lkotlin/reflect/KClassifier;", "getFlags$kotlin_stdlib$annotations", "()V", "getFlags$kotlin_stdlib", "()I", "()Z", "getPlatformTypeUpperBound$kotlin_stdlib$annotations", "getPlatformTypeUpperBound$kotlin_stdlib", "()Lkotlin/reflect/KType;", "arrayClassName", "", "Ljava/lang/Class;", "getArrayClassName", "(Ljava/lang/Class;)Ljava/lang/String;", "asString", "convertPrimitiveToWrapper", "equals", "other", "", "hashCode", "toString", "Companion", "kotlin-stdlib"})
@SinceKotlin(version="1.4")
public final class TypeReference
implements KType {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final KClassifier classifier;
    @NotNull
    private final List<KTypeProjection> arguments;
    @Nullable
    private final KType platformTypeUpperBound;
    private final int flags;
    public static final int IS_MARKED_NULLABLE = 1;
    public static final int IS_MUTABLE_COLLECTION_TYPE = 2;
    public static final int IS_NOTHING_TYPE = 4;

    @SinceKotlin(version="1.6")
    public TypeReference(@NotNull KClassifier kClassifier, @NotNull List<KTypeProjection> list2, @Nullable KType kType, int n2) {
        Intrinsics.checkNotNullParameter(kClassifier, "");
        Intrinsics.checkNotNullParameter(list2, "");
        this.classifier = kClassifier;
        this.arguments = list2;
        this.platformTypeUpperBound = kType;
        this.flags = n2;
    }

    @Override
    @NotNull
    public final KClassifier getClassifier() {
        return this.classifier;
    }

    @Override
    @NotNull
    public final List<KTypeProjection> getArguments() {
        return this.arguments;
    }

    @Nullable
    public final KType getPlatformTypeUpperBound$kotlin_stdlib() {
        return this.platformTypeUpperBound;
    }

    @SinceKotlin(version="1.6")
    public static /* synthetic */ void getPlatformTypeUpperBound$kotlin_stdlib$annotations() {
    }

    public final int getFlags$kotlin_stdlib() {
        return this.flags;
    }

    @SinceKotlin(version="1.6")
    public static /* synthetic */ void getFlags$kotlin_stdlib$annotations() {
    }

    public TypeReference(@NotNull KClassifier kClassifier, @NotNull List<KTypeProjection> list2, boolean bl) {
        Intrinsics.checkNotNullParameter(kClassifier, "");
        Intrinsics.checkNotNullParameter(list2, "");
        this(kClassifier, list2, null, bl ? 1 : 0);
    }

    @Override
    @NotNull
    public final List<Annotation> getAnnotations() {
        return CollectionsKt.emptyList();
    }

    @Override
    public final boolean isMarkedNullable() {
        return (this.flags & 1) != 0;
    }

    public final boolean equals(@Nullable Object object) {
        return object instanceof TypeReference && Intrinsics.areEqual(this.getClassifier(), ((TypeReference)object).getClassifier()) && Intrinsics.areEqual(this.getArguments(), ((TypeReference)object).getArguments()) && Intrinsics.areEqual(this.platformTypeUpperBound, ((TypeReference)object).platformTypeUpperBound) && this.flags == ((TypeReference)object).flags;
    }

    public final int hashCode() {
        return (this.getClassifier().hashCode() * 31 + ((Object)this.getArguments()).hashCode()) * 31 + Integer.hashCode(this.flags);
    }

    @NotNull
    public final String toString() {
        return this.asString(false) + " (Kotlin reflection is not available)";
    }

    private final String asString(boolean bl) {
        String string;
        Class clazz = this.getClassifier();
        KClass kClass = clazz instanceof KClass ? (KClass)((Object)clazz) : null;
        clazz = kClass != null ? JvmClassMappingKt.getJavaClass(kClass) : null;
        if (clazz == null) {
            string = this.getClassifier().toString();
        } else if ((this.flags & 4) != 0) {
            string = "kotlin.Nothing";
        } else if (clazz.isArray()) {
            string = this.getArrayClassName(clazz);
        } else if (bl && clazz.isPrimitive()) {
            KClassifier kClassifier = this.getClassifier();
            Intrinsics.checkNotNull(kClassifier);
            string = JvmClassMappingKt.getJavaObjectType((KClass)kClassifier).getName();
        } else {
            string = clazz.getName();
        }
        String string2 = string;
        clazz = this.getArguments().isEmpty() ? "" : CollectionsKt.joinToString$default(this.getArguments(), ", ", "<", ">", 0, null, new Function1<KTypeProjection, CharSequence>(this){
            final /* synthetic */ TypeReference this$0;
            {
                this.this$0 = typeReference;
                super(1);
            }

            @NotNull
            public final CharSequence invoke(@NotNull KTypeProjection kTypeProjection) {
                Intrinsics.checkNotNullParameter(kTypeProjection, "");
                return TypeReference.access$asString(this.this$0, kTypeProjection);
            }
        }, 24, null);
        String string3 = this.isMarkedNullable() ? "?" : "";
        string2 = string2 + (String)((Object)clazz) + string3;
        clazz = this.platformTypeUpperBound;
        if (clazz instanceof TypeReference && !Intrinsics.areEqual(clazz = ((TypeReference)((Object)clazz)).asString(true), string2)) {
            if (Intrinsics.areEqual(clazz, string2 + '?')) {
                return string2 + '!';
            }
            return "(" + string2 + ".." + (String)((Object)clazz) + ')';
        }
        return string2;
    }

    private final String getArrayClassName(Class<?> clazz) {
        if (Intrinsics.areEqual(clazz, boolean[].class)) {
            return "kotlin.BooleanArray";
        }
        if (Intrinsics.areEqual(clazz, char[].class)) {
            return "kotlin.CharArray";
        }
        if (Intrinsics.areEqual(clazz, byte[].class)) {
            return "kotlin.ByteArray";
        }
        if (Intrinsics.areEqual(clazz, short[].class)) {
            return "kotlin.ShortArray";
        }
        if (Intrinsics.areEqual(clazz, int[].class)) {
            return "kotlin.IntArray";
        }
        if (Intrinsics.areEqual(clazz, float[].class)) {
            return "kotlin.FloatArray";
        }
        if (Intrinsics.areEqual(clazz, long[].class)) {
            return "kotlin.LongArray";
        }
        if (Intrinsics.areEqual(clazz, double[].class)) {
            return "kotlin.DoubleArray";
        }
        return "kotlin.Array";
    }

    private final String asString(KTypeProjection kTypeProjection) {
        if (kTypeProjection.getVariance() == null) {
            return "*";
        }
        Object object = kTypeProjection.getType();
        Object object2 = object instanceof TypeReference ? (TypeReference)object : null;
        if (object2 == null || (object2 = ((TypeReference)object2).asString(true)) == null) {
            object2 = String.valueOf(kTypeProjection.getType());
        }
        object = object2;
        switch (WhenMappings.$EnumSwitchMapping$0[kTypeProjection.getVariance().ordinal()]) {
            case 1: {
                return object;
            }
            case 2: {
                return "in " + (String)object;
            }
            case 3: {
                return "out " + (String)object;
            }
        }
        throw new NoWhenBranchMatchedException();
    }

    public static final /* synthetic */ String access$asString(TypeReference typeReference, KTypeProjection kTypeProjection) {
        return typeReference.asString(kTypeProjection);
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\b\u0080\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0080T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0080T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0080T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2={"Lkotlin/jvm/internal/TypeReference$Companion;", "", "()V", "IS_MARKED_NULLABLE", "", "IS_MUTABLE_COLLECTION_TYPE", "IS_NOTHING_TYPE", "kotlin-stdlib"})
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    @Metadata(mv={1, 8, 0}, k=3, xi=48)
    public final class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] nArray = new int[KVariance.values().length];
            try {
                nArray[KVariance.INVARIANT.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                nArray[KVariance.IN.ordinal()] = 2;
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


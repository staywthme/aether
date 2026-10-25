/*
 * Decompiled with CFR 0.152.
 */
package kotlin.sequences;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;
import kotlin.Deprecated;
import kotlin.DeprecatedSinceKotlin;
import kotlin.Metadata;
import kotlin.OverloadResolutionByLambdaReturnType;
import kotlin.ReplaceWith;
import kotlin.SinceKotlin;
import kotlin.internal.InlineOnly;
import kotlin.jvm.JvmName;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import kotlin.sequences.SequencesKt__SequencesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=5, xi=49, d1={"\u0000V\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u001f\n\u0002\b\u0004\n\u0002\u0010\u000f\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a(\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u0006\u0012\u0002\b\u00030\u00012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0004\u001aA\u0010\u0005\u001a\u0002H\u0006\"\u0010\b\u0000\u0010\u0006*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u0007\"\u0004\b\u0001\u0010\u0002*\u0006\u0012\u0002\b\u00030\u00012\u0006\u0010\b\u001a\u0002H\u00062\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0004\u00a2\u0006\u0002\u0010\t\u001a)\u0010\n\u001a\u0004\u0018\u0001H\u000b\"\u000e\b\u0000\u0010\u000b*\b\u0012\u0004\u0012\u0002H\u000b0\f*\b\u0012\u0004\u0012\u0002H\u000b0\u0001H\u0007\u00a2\u0006\u0002\u0010\r\u001a\u0019\u0010\n\u001a\u0004\u0018\u00010\u000e*\b\u0012\u0004\u0012\u00020\u000e0\u0001H\u0007\u00a2\u0006\u0002\u0010\u000f\u001a\u0019\u0010\n\u001a\u0004\u0018\u00010\u0010*\b\u0012\u0004\u0012\u00020\u00100\u0001H\u0007\u00a2\u0006\u0002\u0010\u0011\u001aG\u0010\u0012\u001a\u0004\u0018\u0001H\u000b\"\u0004\b\u0000\u0010\u000b\"\u000e\b\u0001\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\f*\b\u0012\u0004\u0012\u0002H\u000b0\u00012\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u0002H\u000b\u0012\u0004\u0012\u0002H\u00020\u0014H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0015\u001a;\u0010\u0016\u001a\u0004\u0018\u0001H\u000b\"\u0004\b\u0000\u0010\u000b*\b\u0012\u0004\u0012\u0002H\u000b0\u00012\u001a\u0010\u0017\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u000b0\u0018j\n\u0012\u0006\b\u0000\u0012\u0002H\u000b`\u0019H\u0007\u00a2\u0006\u0002\u0010\u001a\u001a)\u0010\u001b\u001a\u0004\u0018\u0001H\u000b\"\u000e\b\u0000\u0010\u000b*\b\u0012\u0004\u0012\u0002H\u000b0\f*\b\u0012\u0004\u0012\u0002H\u000b0\u0001H\u0007\u00a2\u0006\u0002\u0010\r\u001a\u0019\u0010\u001b\u001a\u0004\u0018\u00010\u000e*\b\u0012\u0004\u0012\u00020\u000e0\u0001H\u0007\u00a2\u0006\u0002\u0010\u000f\u001a\u0019\u0010\u001b\u001a\u0004\u0018\u00010\u0010*\b\u0012\u0004\u0012\u00020\u00100\u0001H\u0007\u00a2\u0006\u0002\u0010\u0011\u001aG\u0010\u001c\u001a\u0004\u0018\u0001H\u000b\"\u0004\b\u0000\u0010\u000b\"\u000e\b\u0001\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\f*\b\u0012\u0004\u0012\u0002H\u000b0\u00012\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u0002H\u000b\u0012\u0004\u0012\u0002H\u00020\u0014H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0015\u001a;\u0010\u001d\u001a\u0004\u0018\u0001H\u000b\"\u0004\b\u0000\u0010\u000b*\b\u0012\u0004\u0012\u0002H\u000b0\u00012\u001a\u0010\u0017\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u000b0\u0018j\n\u0012\u0006\b\u0000\u0012\u0002H\u000b`\u0019H\u0007\u00a2\u0006\u0002\u0010\u001a\u001a5\u0010\u001e\u001a\u00020\u001f\"\u0004\b\u0000\u0010\u000b*\b\u0012\u0004\u0012\u0002H\u000b0\u00012\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u0002H\u000b\u0012\u0004\u0012\u00020\u001f0\u0014H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\b \u001a5\u0010\u001e\u001a\u00020!\"\u0004\b\u0000\u0010\u000b*\b\u0012\u0004\u0012\u0002H\u000b0\u00012\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u0002H\u000b\u0012\u0004\u0012\u00020!0\u0014H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\b\"\u001a&\u0010#\u001a\b\u0012\u0004\u0012\u0002H\u000b0$\"\u000e\b\u0000\u0010\u000b*\b\u0012\u0004\u0012\u0002H\u000b0\f*\b\u0012\u0004\u0012\u0002H\u000b0\u0001\u001a8\u0010#\u001a\b\u0012\u0004\u0012\u0002H\u000b0$\"\u0004\b\u0000\u0010\u000b*\b\u0012\u0004\u0012\u0002H\u000b0\u00012\u001a\u0010\u0017\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u000b0\u0018j\n\u0012\u0006\b\u0000\u0012\u0002H\u000b`\u0019\u0082\u0002\u0007\n\u0005\b\u009920\u0001\u00a8\u0006%"}, d2={"filterIsInstance", "Lkotlin/sequences/Sequence;", "R", "klass", "Ljava/lang/Class;", "filterIsInstanceTo", "C", "", "destination", "(Lkotlin/sequences/Sequence;Ljava/util/Collection;Ljava/lang/Class;)Ljava/util/Collection;", "max", "T", "", "(Lkotlin/sequences/Sequence;)Ljava/lang/Comparable;", "", "(Lkotlin/sequences/Sequence;)Ljava/lang/Double;", "", "(Lkotlin/sequences/Sequence;)Ljava/lang/Float;", "maxBy", "selector", "Lkotlin/Function1;", "(Lkotlin/sequences/Sequence;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "maxWith", "comparator", "Ljava/util/Comparator;", "Lkotlin/Comparator;", "(Lkotlin/sequences/Sequence;Ljava/util/Comparator;)Ljava/lang/Object;", "min", "minBy", "minWith", "sumOf", "Ljava/math/BigDecimal;", "sumOfBigDecimal", "Ljava/math/BigInteger;", "sumOfBigInteger", "toSortedSet", "Ljava/util/SortedSet;", "kotlin-stdlib"}, xs="kotlin/sequences/SequencesKt")
@SourceDebugExtension(value={"SMAP\n_SequencesJvm.kt\nKotlin\n*S Kotlin\n*F\n+ 1 _SequencesJvm.kt\nkotlin/sequences/SequencesKt___SequencesJvmKt\n+ 2 _Sequences.kt\nkotlin/sequences/SequencesKt___SequencesKt\n*L\n1#1,172:1\n1415#2,14:173\n1821#2,14:187\n*S KotlinDebug\n*F\n+ 1 _SequencesJvm.kt\nkotlin/sequences/SequencesKt___SequencesJvmKt\n*L\n89#1:173,14\n126#1:187,14\n*E\n"})
class SequencesKt___SequencesJvmKt
extends SequencesKt__SequencesKt {
    @NotNull
    public static final <R> Sequence<R> filterIsInstance(@NotNull Sequence<?> sequence2, @NotNull Class<R> clazz) {
        Intrinsics.checkNotNullParameter(sequence2, "");
        Intrinsics.checkNotNullParameter(clazz, "");
        Sequence<?> sequence3 = SequencesKt.filter(sequence2, (Function1)new Function1<Object, Boolean>(clazz){
            final /* synthetic */ Class<R> $klass;
            {
                this.$klass = clazz;
                super(1);
            }

            @NotNull
            public final Boolean invoke(@Nullable Object object) {
                return this.$klass.isInstance(object);
            }
        });
        Intrinsics.checkNotNull(sequence3);
        return sequence3;
    }

    @NotNull
    public static final <C extends Collection<? super R>, R> C filterIsInstanceTo(@NotNull Sequence<?> object, @NotNull C c2, @NotNull Class<R> clazz) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(c2, "");
        Intrinsics.checkNotNullParameter(clazz, "");
        object = object.iterator();
        while (object.hasNext()) {
            Object e2 = object.next();
            if (!clazz.isInstance(e2)) continue;
            c2.add(e2);
        }
        return c2;
    }

    @NotNull
    public static final <T extends Comparable<? super T>> SortedSet<T> toSortedSet(@NotNull Sequence<? extends T> sequence2) {
        Intrinsics.checkNotNullParameter(sequence2, "");
        return (SortedSet)SequencesKt.toCollection(sequence2, (Collection)new TreeSet());
    }

    @NotNull
    public static final <T> SortedSet<T> toSortedSet(@NotNull Sequence<? extends T> sequence2, @NotNull Comparator<? super T> comparator) {
        Intrinsics.checkNotNullParameter(sequence2, "");
        Intrinsics.checkNotNullParameter(comparator, "");
        return (SortedSet)SequencesKt.toCollection(sequence2, (Collection)new TreeSet<T>(comparator));
    }

    @Deprecated(message="Use maxOrNull instead.", replaceWith=@ReplaceWith(expression="this.maxOrNull()", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.4", errorSince="1.5", hiddenSince="1.6")
    @SinceKotlin(version="1.1")
    public static final /* synthetic */ Double max(Sequence sequence2) {
        Intrinsics.checkNotNullParameter(sequence2, "");
        return SequencesKt.maxOrNull(sequence2);
    }

    @Deprecated(message="Use maxOrNull instead.", replaceWith=@ReplaceWith(expression="this.maxOrNull()", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.4", errorSince="1.5", hiddenSince="1.6")
    @SinceKotlin(version="1.1")
    public static final /* synthetic */ Float max(Sequence sequence2) {
        Intrinsics.checkNotNullParameter(sequence2, "");
        return SequencesKt.maxOrNull(sequence2);
    }

    @Deprecated(message="Use maxOrNull instead.", replaceWith=@ReplaceWith(expression="this.maxOrNull()", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.4", errorSince="1.5", hiddenSince="1.6")
    public static final /* synthetic */ Comparable max(Sequence sequence2) {
        Intrinsics.checkNotNullParameter(sequence2, "");
        return SequencesKt.maxOrNull(sequence2);
    }

    @Deprecated(message="Use maxByOrNull instead.", replaceWith=@ReplaceWith(expression="this.maxByOrNull(selector)", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.4", errorSince="1.5", hiddenSince="1.6")
    public static final /* synthetic */ <T, R extends Comparable<? super R>> T maxBy(Sequence<? extends T> object, Function1<? super T, ? extends R> function1) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(function1, "");
        object = object.iterator();
        if (!object.hasNext()) {
            return null;
        }
        Object e2 = object.next();
        if (!object.hasNext()) {
            return (T)e2;
        }
        Comparable comparable = (Comparable)function1.invoke(e2);
        do {
            Object e3;
            Comparable comparable2;
            if (comparable.compareTo(comparable2 = (Comparable)function1.invoke(e3 = object.next())) >= 0) continue;
            e2 = e3;
            comparable = comparable2;
        } while (object.hasNext());
        return (T)e2;
    }

    @Deprecated(message="Use maxWithOrNull instead.", replaceWith=@ReplaceWith(expression="this.maxWithOrNull(comparator)", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.4", errorSince="1.5", hiddenSince="1.6")
    public static final /* synthetic */ Object maxWith(Sequence sequence2, Comparator comparator) {
        Intrinsics.checkNotNullParameter(sequence2, "");
        Intrinsics.checkNotNullParameter(comparator, "");
        return SequencesKt.maxWithOrNull(sequence2, comparator);
    }

    @Deprecated(message="Use minOrNull instead.", replaceWith=@ReplaceWith(expression="this.minOrNull()", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.4", errorSince="1.5", hiddenSince="1.6")
    @SinceKotlin(version="1.1")
    public static final /* synthetic */ Double min(Sequence sequence2) {
        Intrinsics.checkNotNullParameter(sequence2, "");
        return SequencesKt.minOrNull(sequence2);
    }

    @Deprecated(message="Use minOrNull instead.", replaceWith=@ReplaceWith(expression="this.minOrNull()", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.4", errorSince="1.5", hiddenSince="1.6")
    @SinceKotlin(version="1.1")
    public static final /* synthetic */ Float min(Sequence sequence2) {
        Intrinsics.checkNotNullParameter(sequence2, "");
        return SequencesKt.minOrNull(sequence2);
    }

    @Deprecated(message="Use minOrNull instead.", replaceWith=@ReplaceWith(expression="this.minOrNull()", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.4", errorSince="1.5", hiddenSince="1.6")
    public static final /* synthetic */ Comparable min(Sequence sequence2) {
        Intrinsics.checkNotNullParameter(sequence2, "");
        return SequencesKt.minOrNull(sequence2);
    }

    @Deprecated(message="Use minByOrNull instead.", replaceWith=@ReplaceWith(expression="this.minByOrNull(selector)", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.4", errorSince="1.5", hiddenSince="1.6")
    public static final /* synthetic */ <T, R extends Comparable<? super R>> T minBy(Sequence<? extends T> object, Function1<? super T, ? extends R> function1) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(function1, "");
        object = object.iterator();
        if (!object.hasNext()) {
            return null;
        }
        Object e2 = object.next();
        if (!object.hasNext()) {
            return (T)e2;
        }
        Comparable comparable = (Comparable)function1.invoke(e2);
        do {
            Object e3;
            Comparable comparable2;
            if (comparable.compareTo(comparable2 = (Comparable)function1.invoke(e3 = object.next())) <= 0) continue;
            e2 = e3;
            comparable = comparable2;
        } while (object.hasNext());
        return (T)e2;
    }

    @Deprecated(message="Use minWithOrNull instead.", replaceWith=@ReplaceWith(expression="this.minWithOrNull(comparator)", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.4", errorSince="1.5", hiddenSince="1.6")
    public static final /* synthetic */ Object minWith(Sequence sequence2, Comparator comparator) {
        Intrinsics.checkNotNullParameter(sequence2, "");
        Intrinsics.checkNotNullParameter(comparator, "");
        return SequencesKt.minWithOrNull(sequence2, comparator);
    }

    @SinceKotlin(version="1.4")
    @OverloadResolutionByLambdaReturnType
    @JvmName(name="sumOfBigDecimal")
    @InlineOnly
    private static final <T> BigDecimal sumOfBigDecimal(Sequence<? extends T> object, Function1<? super T, ? extends BigDecimal> function1) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(function1, "");
        BigDecimal bigDecimal = BigDecimal.valueOf(0L);
        Intrinsics.checkNotNullExpressionValue(bigDecimal, "");
        BigDecimal bigDecimal2 = bigDecimal;
        object = object.iterator();
        while (object.hasNext()) {
            Object e2 = object.next();
            Intrinsics.checkNotNullExpressionValue(bigDecimal2.add(function1.invoke(e2)), "");
        }
        return bigDecimal2;
    }

    @SinceKotlin(version="1.4")
    @OverloadResolutionByLambdaReturnType
    @JvmName(name="sumOfBigInteger")
    @InlineOnly
    private static final <T> BigInteger sumOfBigInteger(Sequence<? extends T> object, Function1<? super T, ? extends BigInteger> function1) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(function1, "");
        BigInteger bigInteger = BigInteger.valueOf(0L);
        Intrinsics.checkNotNullExpressionValue(bigInteger, "");
        BigInteger bigInteger2 = bigInteger;
        object = object.iterator();
        while (object.hasNext()) {
            Object e2 = object.next();
            Intrinsics.checkNotNullExpressionValue(bigInteger2.add(function1.invoke(e2)), "");
        }
        return bigInteger2;
    }
}


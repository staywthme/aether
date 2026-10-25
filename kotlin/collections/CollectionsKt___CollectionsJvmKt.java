/*
 * Decompiled with CFR 0.152.
 */
package kotlin.collections;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import kotlin.Deprecated;
import kotlin.DeprecatedSinceKotlin;
import kotlin.Metadata;
import kotlin.OverloadResolutionByLambdaReturnType;
import kotlin.ReplaceWith;
import kotlin.SinceKotlin;
import kotlin.collections.CollectionsKt;
import kotlin.collections.CollectionsKt__ReversedViewsKt;
import kotlin.internal.InlineOnly;
import kotlin.jvm.JvmName;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 8, 0}, k=5, xi=49, d1={"\u0000d\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010\u001c\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u001f\n\u0002\b\u0004\n\u0002\u0010\u000f\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\u0010!\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a(\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u0006\u0012\u0002\b\u00030\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0005\u001aA\u0010\u0006\u001a\u0002H\u0007\"\u0010\b\u0000\u0010\u0007*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\b\"\u0004\b\u0001\u0010\u0002*\u0006\u0012\u0002\b\u00030\u00032\u0006\u0010\t\u001a\u0002H\u00072\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0005\u00a2\u0006\u0002\u0010\n\u001a)\u0010\u000b\u001a\u0004\u0018\u0001H\f\"\u000e\b\u0000\u0010\f*\b\u0012\u0004\u0012\u0002H\f0\r*\b\u0012\u0004\u0012\u0002H\f0\u0003H\u0007\u00a2\u0006\u0002\u0010\u000e\u001a\u0019\u0010\u000b\u001a\u0004\u0018\u00010\u000f*\b\u0012\u0004\u0012\u00020\u000f0\u0003H\u0007\u00a2\u0006\u0002\u0010\u0010\u001a\u0019\u0010\u000b\u001a\u0004\u0018\u00010\u0011*\b\u0012\u0004\u0012\u00020\u00110\u0003H\u0007\u00a2\u0006\u0002\u0010\u0012\u001aG\u0010\u0013\u001a\u0004\u0018\u0001H\f\"\u0004\b\u0000\u0010\f\"\u000e\b\u0001\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\r*\b\u0012\u0004\u0012\u0002H\f0\u00032\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u0002H\f\u0012\u0004\u0012\u0002H\u00020\u0015H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0016\u001a;\u0010\u0017\u001a\u0004\u0018\u0001H\f\"\u0004\b\u0000\u0010\f*\b\u0012\u0004\u0012\u0002H\f0\u00032\u001a\u0010\u0018\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\f0\u0019j\n\u0012\u0006\b\u0000\u0012\u0002H\f`\u001aH\u0007\u00a2\u0006\u0002\u0010\u001b\u001a)\u0010\u001c\u001a\u0004\u0018\u0001H\f\"\u000e\b\u0000\u0010\f*\b\u0012\u0004\u0012\u0002H\f0\r*\b\u0012\u0004\u0012\u0002H\f0\u0003H\u0007\u00a2\u0006\u0002\u0010\u000e\u001a\u0019\u0010\u001c\u001a\u0004\u0018\u00010\u000f*\b\u0012\u0004\u0012\u00020\u000f0\u0003H\u0007\u00a2\u0006\u0002\u0010\u0010\u001a\u0019\u0010\u001c\u001a\u0004\u0018\u00010\u0011*\b\u0012\u0004\u0012\u00020\u00110\u0003H\u0007\u00a2\u0006\u0002\u0010\u0012\u001aG\u0010\u001d\u001a\u0004\u0018\u0001H\f\"\u0004\b\u0000\u0010\f\"\u000e\b\u0001\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\r*\b\u0012\u0004\u0012\u0002H\f0\u00032\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u0002H\f\u0012\u0004\u0012\u0002H\u00020\u0015H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0016\u001a;\u0010\u001e\u001a\u0004\u0018\u0001H\f\"\u0004\b\u0000\u0010\f*\b\u0012\u0004\u0012\u0002H\f0\u00032\u001a\u0010\u0018\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\f0\u0019j\n\u0012\u0006\b\u0000\u0012\u0002H\f`\u001aH\u0007\u00a2\u0006\u0002\u0010\u001b\u001a\u0016\u0010\u001f\u001a\u00020 \"\u0004\b\u0000\u0010\f*\b\u0012\u0004\u0012\u0002H\f0!\u001a5\u0010\"\u001a\u00020#\"\u0004\b\u0000\u0010\f*\b\u0012\u0004\u0012\u0002H\f0\u00032\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u0002H\f\u0012\u0004\u0012\u00020#0\u0015H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\b$\u001a5\u0010\"\u001a\u00020%\"\u0004\b\u0000\u0010\f*\b\u0012\u0004\u0012\u0002H\f0\u00032\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u0002H\f\u0012\u0004\u0012\u00020%0\u0015H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\b&\u001a&\u0010'\u001a\b\u0012\u0004\u0012\u0002H\f0(\"\u000e\b\u0000\u0010\f*\b\u0012\u0004\u0012\u0002H\f0\r*\b\u0012\u0004\u0012\u0002H\f0\u0003\u001a8\u0010'\u001a\b\u0012\u0004\u0012\u0002H\f0(\"\u0004\b\u0000\u0010\f*\b\u0012\u0004\u0012\u0002H\f0\u00032\u001a\u0010\u0018\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\f0\u0019j\n\u0012\u0006\b\u0000\u0012\u0002H\f`\u001a\u0082\u0002\u0007\n\u0005\b\u009920\u0001\u00a8\u0006)"}, d2={"filterIsInstance", "", "R", "", "klass", "Ljava/lang/Class;", "filterIsInstanceTo", "C", "", "destination", "(Ljava/lang/Iterable;Ljava/util/Collection;Ljava/lang/Class;)Ljava/util/Collection;", "max", "T", "", "(Ljava/lang/Iterable;)Ljava/lang/Comparable;", "", "(Ljava/lang/Iterable;)Ljava/lang/Double;", "", "(Ljava/lang/Iterable;)Ljava/lang/Float;", "maxBy", "selector", "Lkotlin/Function1;", "(Ljava/lang/Iterable;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "maxWith", "comparator", "Ljava/util/Comparator;", "Lkotlin/Comparator;", "(Ljava/lang/Iterable;Ljava/util/Comparator;)Ljava/lang/Object;", "min", "minBy", "minWith", "reverse", "", "", "sumOf", "Ljava/math/BigDecimal;", "sumOfBigDecimal", "Ljava/math/BigInteger;", "sumOfBigInteger", "toSortedSet", "Ljava/util/SortedSet;", "kotlin-stdlib"}, xs="kotlin/collections/CollectionsKt")
@SourceDebugExtension(value={"SMAP\n_CollectionsJvm.kt\nKotlin\n*S Kotlin\n*F\n+ 1 _CollectionsJvm.kt\nkotlin/collections/CollectionsKt___CollectionsJvmKt\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,168:1\n1963#2,14:169\n2333#2,14:183\n*S KotlinDebug\n*F\n+ 1 _CollectionsJvm.kt\nkotlin/collections/CollectionsKt___CollectionsJvmKt\n*L\n89#1:169,14\n126#1:183,14\n*E\n"})
public class CollectionsKt___CollectionsJvmKt
extends CollectionsKt__ReversedViewsKt {
    @NotNull
    public static final <R> List<R> filterIsInstance(@NotNull Iterable<?> iterable, @NotNull Class<R> clazz) {
        Intrinsics.checkNotNullParameter(iterable, "");
        Intrinsics.checkNotNullParameter(clazz, "");
        return (List)CollectionsKt.filterIsInstanceTo(iterable, (Collection)new ArrayList(), clazz);
    }

    @NotNull
    public static final <C extends Collection<? super R>, R> C filterIsInstanceTo(@NotNull Iterable<?> object, @NotNull C c2, @NotNull Class<R> clazz) {
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

    public static final <T> void reverse(@NotNull List<T> list2) {
        Intrinsics.checkNotNullParameter(list2, "");
        Collections.reverse(list2);
    }

    @NotNull
    public static final <T extends Comparable<? super T>> SortedSet<T> toSortedSet(@NotNull Iterable<? extends T> iterable) {
        Intrinsics.checkNotNullParameter(iterable, "");
        return (SortedSet)CollectionsKt.toCollection(iterable, (Collection)new TreeSet());
    }

    @NotNull
    public static final <T> SortedSet<T> toSortedSet(@NotNull Iterable<? extends T> iterable, @NotNull Comparator<? super T> comparator) {
        Intrinsics.checkNotNullParameter(iterable, "");
        Intrinsics.checkNotNullParameter(comparator, "");
        return (SortedSet)CollectionsKt.toCollection(iterable, (Collection)new TreeSet<T>(comparator));
    }

    @Deprecated(message="Use maxOrNull instead.", replaceWith=@ReplaceWith(expression="this.maxOrNull()", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.4", errorSince="1.5", hiddenSince="1.6")
    @SinceKotlin(version="1.1")
    public static final /* synthetic */ Double max(Iterable iterable) {
        Intrinsics.checkNotNullParameter(iterable, "");
        return CollectionsKt.maxOrNull(iterable);
    }

    @Deprecated(message="Use maxOrNull instead.", replaceWith=@ReplaceWith(expression="this.maxOrNull()", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.4", errorSince="1.5", hiddenSince="1.6")
    @SinceKotlin(version="1.1")
    public static final /* synthetic */ Float max(Iterable iterable) {
        Intrinsics.checkNotNullParameter(iterable, "");
        return CollectionsKt.maxOrNull(iterable);
    }

    @Deprecated(message="Use maxOrNull instead.", replaceWith=@ReplaceWith(expression="this.maxOrNull()", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.4", errorSince="1.5", hiddenSince="1.6")
    public static final /* synthetic */ Comparable max(Iterable iterable) {
        Intrinsics.checkNotNullParameter(iterable, "");
        return CollectionsKt.maxOrNull(iterable);
    }

    @Deprecated(message="Use maxByOrNull instead.", replaceWith=@ReplaceWith(expression="this.maxByOrNull(selector)", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.4", errorSince="1.5", hiddenSince="1.6")
    public static final /* synthetic */ <T, R extends Comparable<? super R>> T maxBy(Iterable<? extends T> object, Function1<? super T, ? extends R> function1) {
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
    public static final /* synthetic */ Object maxWith(Iterable iterable, Comparator comparator) {
        Intrinsics.checkNotNullParameter(iterable, "");
        Intrinsics.checkNotNullParameter(comparator, "");
        return CollectionsKt.maxWithOrNull(iterable, comparator);
    }

    @Deprecated(message="Use minOrNull instead.", replaceWith=@ReplaceWith(expression="this.minOrNull()", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.4", errorSince="1.5", hiddenSince="1.6")
    @SinceKotlin(version="1.1")
    public static final /* synthetic */ Double min(Iterable iterable) {
        Intrinsics.checkNotNullParameter(iterable, "");
        return CollectionsKt.minOrNull(iterable);
    }

    @Deprecated(message="Use minOrNull instead.", replaceWith=@ReplaceWith(expression="this.minOrNull()", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.4", errorSince="1.5", hiddenSince="1.6")
    @SinceKotlin(version="1.1")
    public static final /* synthetic */ Float min(Iterable iterable) {
        Intrinsics.checkNotNullParameter(iterable, "");
        return CollectionsKt.minOrNull(iterable);
    }

    @Deprecated(message="Use minOrNull instead.", replaceWith=@ReplaceWith(expression="this.minOrNull()", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.4", errorSince="1.5", hiddenSince="1.6")
    public static final /* synthetic */ Comparable min(Iterable iterable) {
        Intrinsics.checkNotNullParameter(iterable, "");
        return CollectionsKt.minOrNull(iterable);
    }

    @Deprecated(message="Use minByOrNull instead.", replaceWith=@ReplaceWith(expression="this.minByOrNull(selector)", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.4", errorSince="1.5", hiddenSince="1.6")
    public static final /* synthetic */ <T, R extends Comparable<? super R>> T minBy(Iterable<? extends T> object, Function1<? super T, ? extends R> function1) {
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
    public static final /* synthetic */ Object minWith(Iterable iterable, Comparator comparator) {
        Intrinsics.checkNotNullParameter(iterable, "");
        Intrinsics.checkNotNullParameter(comparator, "");
        return CollectionsKt.minWithOrNull(iterable, comparator);
    }

    @SinceKotlin(version="1.4")
    @OverloadResolutionByLambdaReturnType
    @JvmName(name="sumOfBigDecimal")
    @InlineOnly
    private static final <T> BigDecimal sumOfBigDecimal(Iterable<? extends T> object, Function1<? super T, ? extends BigDecimal> function1) {
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
    private static final <T> BigInteger sumOfBigInteger(Iterable<? extends T> object, Function1<? super T, ? extends BigInteger> function1) {
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


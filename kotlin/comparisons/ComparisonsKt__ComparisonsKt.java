/*
 * Decompiled with CFR 0.152.
 */
package kotlin.comparisons;

import java.util.Comparator;
import kotlin.Metadata;
import kotlin.comparisons.ComparisonsKt;
import kotlin.comparisons.NaturalOrderComparator;
import kotlin.comparisons.ReverseOrderComparator;
import kotlin.comparisons.ReversedComparator;
import kotlin.internal.InlineOnly;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=5, xi=49, d1={"\u0000<\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u000b\n\u0002\u0010\u0000\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a>\u0010\u0000\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u00022\u001a\b\u0004\u0010\u0004\u001a\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00060\u0005H\u0087\b\u00f8\u0001\u0000\u001aY\u0010\u0000\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u000226\u0010\u0007\u001a\u001c\u0012\u0018\b\u0001\u0012\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00060\u00050\b\"\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00060\u0005\u00a2\u0006\u0002\u0010\t\u001aZ\u0010\u0000\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\n2\u001a\u0010\u000b\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\n0\u0001j\n\u0012\u0006\b\u0000\u0012\u0002H\n`\u00032\u0014\b\u0004\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\n0\u0005H\u0087\b\u00f8\u0001\u0000\u001a>\u0010\f\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u00022\u001a\b\u0004\u0010\u0004\u001a\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00060\u0005H\u0087\b\u00f8\u0001\u0000\u001aZ\u0010\f\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\n2\u001a\u0010\u000b\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\n0\u0001j\n\u0012\u0006\b\u0000\u0012\u0002H\n`\u00032\u0014\b\u0004\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\n0\u0005H\u0087\b\u00f8\u0001\u0000\u001a-\u0010\r\u001a\u00020\u000e\"\f\b\u0000\u0010\u0002*\u0006\u0012\u0002\b\u00030\u00062\b\u0010\u000f\u001a\u0004\u0018\u0001H\u00022\b\u0010\u0010\u001a\u0004\u0018\u0001H\u0002\u00a2\u0006\u0002\u0010\u0011\u001aA\u0010\u0012\u001a\u00020\u000e\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u000f\u001a\u0002H\u00022\u0006\u0010\u0010\u001a\u0002H\u00022\u0018\u0010\u0004\u001a\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00060\u0005H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0013\u001aY\u0010\u0012\u001a\u00020\u000e\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u000f\u001a\u0002H\u00022\u0006\u0010\u0010\u001a\u0002H\u000226\u0010\u0007\u001a\u001c\u0012\u0018\b\u0001\u0012\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00060\u00050\b\"\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00060\u0005\u00a2\u0006\u0002\u0010\u0014\u001a]\u0010\u0012\u001a\u00020\u000e\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\n2\u0006\u0010\u000f\u001a\u0002H\u00022\u0006\u0010\u0010\u001a\u0002H\u00022\u001a\u0010\u000b\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\n0\u0001j\n\u0012\u0006\b\u0000\u0012\u0002H\n`\u00032\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\n0\u0005H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0015\u001aG\u0010\u0016\u001a\u00020\u000e\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u000f\u001a\u0002H\u00022\u0006\u0010\u0010\u001a\u0002H\u00022 \u0010\u0007\u001a\u001c\u0012\u0018\b\u0001\u0012\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00060\u00050\bH\u0002\u00a2\u0006\u0004\b\u0017\u0010\u0014\u001a&\u0010\u0018\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u000e\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0006\u001a-\u0010\u0019\u001a\u0016\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u0001j\n\u0012\u0006\u0012\u0004\u0018\u0001H\u0002`\u0003\"\u000e\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0006H\u0087\b\u001a@\u0010\u0019\u001a\u0016\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u0001j\n\u0012\u0006\u0012\u0004\u0018\u0001H\u0002`\u0003\"\b\b\u0000\u0010\u0002*\u00020\u001a2\u001a\u0010\u000b\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u00020\u0001j\n\u0012\u0006\b\u0000\u0012\u0002H\u0002`\u0003\u001a-\u0010\u001b\u001a\u0016\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u0001j\n\u0012\u0006\u0012\u0004\u0018\u0001H\u0002`\u0003\"\u000e\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0006H\u0087\b\u001a@\u0010\u001b\u001a\u0016\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u0001j\n\u0012\u0006\u0012\u0004\u0018\u0001H\u0002`\u0003\"\b\b\u0000\u0010\u0002*\u00020\u001a2\u001a\u0010\u000b\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u00020\u0001j\n\u0012\u0006\b\u0000\u0012\u0002H\u0002`\u0003\u001a&\u0010\u001c\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u000e\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0006\u001a0\u0010\u001d\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u0002*\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\u001aO\u0010\u001e\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u0002*\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u00032\u001a\u0010\u000b\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u00020\u0001j\n\u0012\u0006\b\u0000\u0012\u0002H\u0002`\u0003H\u0086\u0004\u001aR\u0010\u001f\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u0002*\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u00032\u001a\b\u0004\u0010\u0004\u001a\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00060\u0005H\u0087\b\u00f8\u0001\u0000\u001an\u0010\u001f\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\n*\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u00032\u001a\u0010\u000b\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\n0\u0001j\n\u0012\u0006\b\u0000\u0012\u0002H\n`\u00032\u0014\b\u0004\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\n0\u0005H\u0087\b\u00f8\u0001\u0000\u001aR\u0010 \u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u0002*\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u00032\u001a\b\u0004\u0010\u0004\u001a\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00060\u0005H\u0087\b\u00f8\u0001\u0000\u001an\u0010 \u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\n*\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u00032\u001a\u0010\u000b\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\n0\u0001j\n\u0012\u0006\b\u0000\u0012\u0002H\n`\u00032\u0014\b\u0004\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\n0\u0005H\u0087\b\u00f8\u0001\u0000\u001ap\u0010!\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u0002*\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u000328\b\u0004\u0010\"\u001a2\u0012\u0013\u0012\u0011H\u0002\u00a2\u0006\f\b$\u0012\b\b%\u0012\u0004\b\b(\u000f\u0012\u0013\u0012\u0011H\u0002\u00a2\u0006\f\b$\u0012\b\b%\u0012\u0004\b\b(\u0010\u0012\u0004\u0012\u00020\u000e0#H\u0087\b\u00f8\u0001\u0000\u001aO\u0010&\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u0002*\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u00032\u001a\u0010\u000b\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u00020\u0001j\n\u0012\u0006\b\u0000\u0012\u0002H\u0002`\u0003H\u0086\u0004\u0082\u0002\u0007\n\u0005\b\u009920\u0001\u00a8\u0006'"}, d2={"compareBy", "Ljava/util/Comparator;", "T", "Lkotlin/Comparator;", "selector", "Lkotlin/Function1;", "", "selectors", "", "([Lkotlin/jvm/functions/Function1;)Ljava/util/Comparator;", "K", "comparator", "compareByDescending", "compareValues", "", "a", "b", "(Ljava/lang/Comparable;Ljava/lang/Comparable;)I", "compareValuesBy", "(Ljava/lang/Object;Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)I", "(Ljava/lang/Object;Ljava/lang/Object;[Lkotlin/jvm/functions/Function1;)I", "(Ljava/lang/Object;Ljava/lang/Object;Ljava/util/Comparator;Lkotlin/jvm/functions/Function1;)I", "compareValuesByImpl", "compareValuesByImpl$ComparisonsKt__ComparisonsKt", "naturalOrder", "nullsFirst", "", "nullsLast", "reverseOrder", "reversed", "then", "thenBy", "thenByDescending", "thenComparator", "comparison", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", "thenDescending", "kotlin-stdlib"}, xs="kotlin/comparisons/ComparisonsKt")
public class ComparisonsKt__ComparisonsKt {
    public static final <T> int compareValuesBy(T object, T t2, Function1<? super T, ? extends Comparable<?>> ... function1Array) {
        Intrinsics.checkNotNullParameter(function1Array, "");
        boolean bl = function1Array.length > 0;
        if (!bl) {
            object = "Failed requirement.";
            throw new IllegalArgumentException(object.toString());
        }
        return ComparisonsKt__ComparisonsKt.compareValuesByImpl$ComparisonsKt__ComparisonsKt(object, t2, function1Array);
    }

    private static final <T> int compareValuesByImpl$ComparisonsKt__ComparisonsKt(T t2, T t3, Function1<? super T, ? extends Comparable<?>>[] function1Array) {
        for (Function1<T, Comparable<?>> object : function1Array) {
            Comparable<?> comparable;
            Comparable<?> comparable2 = object.invoke(t2);
            int n2 = ComparisonsKt.compareValues(comparable2, comparable = object.invoke(t3));
            if (n2 == 0) continue;
            return n2;
        }
        return 0;
    }

    @InlineOnly
    private static final <T> int compareValuesBy(T t2, T t3, Function1<? super T, ? extends Comparable<?>> function1) {
        Intrinsics.checkNotNullParameter(function1, "");
        return ComparisonsKt.compareValues(function1.invoke(t2), function1.invoke(t3));
    }

    @InlineOnly
    private static final <T, K> int compareValuesBy(T t2, T t3, Comparator<? super K> comparator, Function1<? super T, ? extends K> function1) {
        Intrinsics.checkNotNullParameter(comparator, "");
        Intrinsics.checkNotNullParameter(function1, "");
        return comparator.compare(function1.invoke(t2), function1.invoke(t3));
    }

    public static final <T extends Comparable<?>> int compareValues(@Nullable T t2, @Nullable T t3) {
        if (t2 == t3) {
            return 0;
        }
        if (t2 == null) {
            return -1;
        }
        if (t3 == null) {
            return 1;
        }
        return t2.compareTo(t3);
    }

    @NotNull
    public static final <T> Comparator<T> compareBy(Function1<? super T, ? extends Comparable<?>> ... object) {
        Intrinsics.checkNotNullParameter(object, "");
        boolean bl = ((Function1<? super T, ? extends Comparable<?>>[])object).length > 0;
        if (!bl) {
            object = "Failed requirement.";
            throw new IllegalArgumentException(object.toString());
        }
        return (arg_0, arg_1) -> ComparisonsKt__ComparisonsKt.compareBy$lambda$0$ComparisonsKt__ComparisonsKt((Function1[])object, arg_0, arg_1);
    }

    @InlineOnly
    private static final <T> Comparator<T> compareBy(Function1<? super T, ? extends Comparable<?>> function1) {
        Intrinsics.checkNotNullParameter(function1, "");
        return new Comparator(function1){
            final /* synthetic */ Function1<T, Comparable<?>> $selector;
            {
                this.$selector = function1;
            }

            public final int compare(T t2, T t3) {
                Function1<T, Comparable<?>> function1 = this.$selector;
                return ComparisonsKt.compareValues(function1.invoke(t2), function1.invoke(t3));
            }
        };
    }

    @InlineOnly
    private static final <T, K> Comparator<T> compareBy(Comparator<? super K> comparator, Function1<? super T, ? extends K> function1) {
        Intrinsics.checkNotNullParameter(comparator, "");
        Intrinsics.checkNotNullParameter(function1, "");
        return new Comparator(comparator, function1){
            final /* synthetic */ Comparator<? super K> $comparator;
            final /* synthetic */ Function1<T, K> $selector;
            {
                this.$comparator = comparator;
                this.$selector = function1;
            }

            public final int compare(T t2, T t3) {
                Comparator<K> comparator = this.$comparator;
                Function1<T, K> function1 = this.$selector;
                return comparator.compare(function1.invoke(t2), function1.invoke(t3));
            }
        };
    }

    @InlineOnly
    private static final <T> Comparator<T> compareByDescending(Function1<? super T, ? extends Comparable<?>> function1) {
        Intrinsics.checkNotNullParameter(function1, "");
        return new Comparator(function1){
            final /* synthetic */ Function1<T, Comparable<?>> $selector;
            {
                this.$selector = function1;
            }

            public final int compare(T t2, T t3) {
                Function1<T, Comparable<?>> function1 = this.$selector;
                return ComparisonsKt.compareValues(function1.invoke(t3), function1.invoke(t2));
            }
        };
    }

    @InlineOnly
    private static final <T, K> Comparator<T> compareByDescending(Comparator<? super K> comparator, Function1<? super T, ? extends K> function1) {
        Intrinsics.checkNotNullParameter(comparator, "");
        Intrinsics.checkNotNullParameter(function1, "");
        return new Comparator(comparator, function1){
            final /* synthetic */ Comparator<? super K> $comparator;
            final /* synthetic */ Function1<T, K> $selector;
            {
                this.$comparator = comparator;
                this.$selector = function1;
            }

            public final int compare(T t2, T t3) {
                Comparator<K> comparator = this.$comparator;
                Function1<T, K> function1 = this.$selector;
                return comparator.compare(function1.invoke(t3), function1.invoke(t2));
            }
        };
    }

    @InlineOnly
    private static final <T> Comparator<T> thenBy(Comparator<T> comparator, Function1<? super T, ? extends Comparable<?>> function1) {
        Intrinsics.checkNotNullParameter(comparator, "");
        Intrinsics.checkNotNullParameter(function1, "");
        return new Comparator(comparator, function1){
            final /* synthetic */ Comparator<T> $this_thenBy;
            final /* synthetic */ Function1<T, Comparable<?>> $selector;
            {
                this.$this_thenBy = comparator;
                this.$selector = function1;
            }

            public final int compare(T t2, T t3) {
                int n2 = this.$this_thenBy.compare(t2, t3);
                if (n2 != 0) {
                    return n2;
                }
                Function1<T, Comparable<?>> function1 = this.$selector;
                return ComparisonsKt.compareValues(function1.invoke(t2), function1.invoke(t3));
            }
        };
    }

    @InlineOnly
    private static final <T, K> Comparator<T> thenBy(Comparator<T> comparator, Comparator<? super K> comparator2, Function1<? super T, ? extends K> function1) {
        Intrinsics.checkNotNullParameter(comparator, "");
        Intrinsics.checkNotNullParameter(comparator2, "");
        Intrinsics.checkNotNullParameter(function1, "");
        return new Comparator(comparator, comparator2, function1){
            final /* synthetic */ Comparator<T> $this_thenBy;
            final /* synthetic */ Comparator<? super K> $comparator;
            final /* synthetic */ Function1<T, K> $selector;
            {
                this.$this_thenBy = comparator;
                this.$comparator = comparator2;
                this.$selector = function1;
            }

            public final int compare(T t2, T t3) {
                int n2 = this.$this_thenBy.compare(t2, t3);
                if (n2 != 0) {
                    return n2;
                }
                Comparator<K> comparator = this.$comparator;
                Function1<T, K> function1 = this.$selector;
                return comparator.compare(function1.invoke(t2), function1.invoke(t3));
            }
        };
    }

    @InlineOnly
    private static final <T> Comparator<T> thenByDescending(Comparator<T> comparator, Function1<? super T, ? extends Comparable<?>> function1) {
        Intrinsics.checkNotNullParameter(comparator, "");
        Intrinsics.checkNotNullParameter(function1, "");
        return new Comparator(comparator, function1){
            final /* synthetic */ Comparator<T> $this_thenByDescending;
            final /* synthetic */ Function1<T, Comparable<?>> $selector;
            {
                this.$this_thenByDescending = comparator;
                this.$selector = function1;
            }

            public final int compare(T t2, T t3) {
                int n2 = this.$this_thenByDescending.compare(t2, t3);
                if (n2 != 0) {
                    return n2;
                }
                Function1<T, Comparable<?>> function1 = this.$selector;
                return ComparisonsKt.compareValues(function1.invoke(t3), function1.invoke(t2));
            }
        };
    }

    @InlineOnly
    private static final <T, K> Comparator<T> thenByDescending(Comparator<T> comparator, Comparator<? super K> comparator2, Function1<? super T, ? extends K> function1) {
        Intrinsics.checkNotNullParameter(comparator, "");
        Intrinsics.checkNotNullParameter(comparator2, "");
        Intrinsics.checkNotNullParameter(function1, "");
        return new Comparator(comparator, comparator2, function1){
            final /* synthetic */ Comparator<T> $this_thenByDescending;
            final /* synthetic */ Comparator<? super K> $comparator;
            final /* synthetic */ Function1<T, K> $selector;
            {
                this.$this_thenByDescending = comparator;
                this.$comparator = comparator2;
                this.$selector = function1;
            }

            public final int compare(T t2, T t3) {
                int n2 = this.$this_thenByDescending.compare(t2, t3);
                if (n2 != 0) {
                    return n2;
                }
                Comparator<K> comparator = this.$comparator;
                Function1<T, K> function1 = this.$selector;
                return comparator.compare(function1.invoke(t3), function1.invoke(t2));
            }
        };
    }

    @InlineOnly
    private static final <T> Comparator<T> thenComparator(Comparator<T> comparator, Function2<? super T, ? super T, Integer> function2) {
        Intrinsics.checkNotNullParameter(comparator, "");
        Intrinsics.checkNotNullParameter(function2, "");
        return new Comparator(comparator, function2){
            final /* synthetic */ Comparator<T> $this_thenComparator;
            final /* synthetic */ Function2<T, T, Integer> $comparison;
            {
                this.$this_thenComparator = comparator;
                this.$comparison = function2;
            }

            public final int compare(T t2, T t3) {
                int n2 = this.$this_thenComparator.compare(t2, t3);
                if (n2 != 0) {
                    return n2;
                }
                return ((Number)this.$comparison.invoke(t2, t3)).intValue();
            }
        };
    }

    @NotNull
    public static final <T> Comparator<T> then(@NotNull Comparator<T> comparator, @NotNull Comparator<? super T> comparator2) {
        Intrinsics.checkNotNullParameter(comparator, "");
        Intrinsics.checkNotNullParameter(comparator2, "");
        return (arg_0, arg_1) -> ComparisonsKt__ComparisonsKt.then$lambda$1$ComparisonsKt__ComparisonsKt(comparator, comparator2, arg_0, arg_1);
    }

    @NotNull
    public static final <T> Comparator<T> thenDescending(@NotNull Comparator<T> comparator, @NotNull Comparator<? super T> comparator2) {
        Intrinsics.checkNotNullParameter(comparator, "");
        Intrinsics.checkNotNullParameter(comparator2, "");
        return (arg_0, arg_1) -> ComparisonsKt__ComparisonsKt.thenDescending$lambda$2$ComparisonsKt__ComparisonsKt(comparator, comparator2, arg_0, arg_1);
    }

    @NotNull
    public static final <T> Comparator<T> nullsFirst(@NotNull Comparator<? super T> comparator) {
        Intrinsics.checkNotNullParameter(comparator, "");
        return (arg_0, arg_1) -> ComparisonsKt__ComparisonsKt.nullsFirst$lambda$3$ComparisonsKt__ComparisonsKt(comparator, arg_0, arg_1);
    }

    @InlineOnly
    private static final <T extends Comparable<? super T>> Comparator<T> nullsFirst() {
        return ComparisonsKt.nullsFirst(ComparisonsKt.naturalOrder());
    }

    @NotNull
    public static final <T> Comparator<T> nullsLast(@NotNull Comparator<? super T> comparator) {
        Intrinsics.checkNotNullParameter(comparator, "");
        return (arg_0, arg_1) -> ComparisonsKt__ComparisonsKt.nullsLast$lambda$4$ComparisonsKt__ComparisonsKt(comparator, arg_0, arg_1);
    }

    @InlineOnly
    private static final <T extends Comparable<? super T>> Comparator<T> nullsLast() {
        return ComparisonsKt.nullsLast(ComparisonsKt.naturalOrder());
    }

    @NotNull
    public static final <T extends Comparable<? super T>> Comparator<T> naturalOrder() {
        NaturalOrderComparator naturalOrderComparator = NaturalOrderComparator.INSTANCE;
        Intrinsics.checkNotNull(naturalOrderComparator);
        return naturalOrderComparator;
    }

    @NotNull
    public static final <T extends Comparable<? super T>> Comparator<T> reverseOrder() {
        ReverseOrderComparator reverseOrderComparator = ReverseOrderComparator.INSTANCE;
        Intrinsics.checkNotNull(reverseOrderComparator);
        return reverseOrderComparator;
    }

    @NotNull
    public static final <T> Comparator<T> reversed(@NotNull Comparator<T> comparator) {
        Intrinsics.checkNotNullParameter(comparator, "");
        Comparator<T> comparator2 = comparator;
        if (comparator2 instanceof ReversedComparator) {
            return ((ReversedComparator)comparator).getComparator();
        }
        if (Intrinsics.areEqual(comparator2, NaturalOrderComparator.INSTANCE)) {
            ReverseOrderComparator reverseOrderComparator = ReverseOrderComparator.INSTANCE;
            Intrinsics.checkNotNull(reverseOrderComparator);
            return reverseOrderComparator;
        }
        if (Intrinsics.areEqual(comparator2, ReverseOrderComparator.INSTANCE)) {
            NaturalOrderComparator naturalOrderComparator = NaturalOrderComparator.INSTANCE;
            Intrinsics.checkNotNull(naturalOrderComparator);
            return naturalOrderComparator;
        }
        return new ReversedComparator<T>(comparator);
    }

    private static final int compareBy$lambda$0$ComparisonsKt__ComparisonsKt(Function1[] function1Array, Object object, Object object2) {
        Intrinsics.checkNotNullParameter(function1Array, "");
        return ComparisonsKt__ComparisonsKt.compareValuesByImpl$ComparisonsKt__ComparisonsKt(object, object2, function1Array);
    }

    private static final int then$lambda$1$ComparisonsKt__ComparisonsKt(Comparator comparator, Comparator comparator2, Object object, Object object2) {
        Intrinsics.checkNotNullParameter(comparator, "");
        Intrinsics.checkNotNullParameter(comparator2, "");
        int n2 = comparator.compare(object, object2);
        if (n2 != 0) {
            return n2;
        }
        return comparator2.compare(object, object2);
    }

    private static final int thenDescending$lambda$2$ComparisonsKt__ComparisonsKt(Comparator comparator, Comparator comparator2, Object object, Object object2) {
        Intrinsics.checkNotNullParameter(comparator, "");
        Intrinsics.checkNotNullParameter(comparator2, "");
        int n2 = comparator.compare(object, object2);
        if (n2 != 0) {
            return n2;
        }
        return comparator2.compare(object2, object);
    }

    private static final int nullsFirst$lambda$3$ComparisonsKt__ComparisonsKt(Comparator comparator, Object object, Object object2) {
        Intrinsics.checkNotNullParameter(comparator, "");
        if (object == object2) {
            return 0;
        }
        if (object == null) {
            return -1;
        }
        if (object2 == null) {
            return 1;
        }
        return comparator.compare(object, object2);
    }

    private static final int nullsLast$lambda$4$ComparisonsKt__ComparisonsKt(Comparator comparator, Object object, Object object2) {
        Intrinsics.checkNotNullParameter(comparator, "");
        if (object == object2) {
            return 0;
        }
        if (object == null) {
            return 1;
        }
        if (object2 == null) {
            return -1;
        }
        return comparator.compare(object, object2);
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package kotlin.collections;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.RandomAccess;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.ExperimentalStdlibApi;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.SinceKotlin;
import kotlin.WasExperimental;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.CollectionsKt__MutableCollectionsJVMKt;
import kotlin.collections.IntIterator;
import kotlin.internal.InlineOnly;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlin.ranges.IntRange;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=5, xi=49, d1={"\u0000P\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u001f\n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u001c\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u001e\n\u0000\n\u0002\u0010\u001d\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u000b\u001a-\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\u000e\u0010\u0004\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0005\u00a2\u0006\u0002\u0010\u0006\u001a&\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007\u001a&\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\b\u001a\u001e\u0010\t\u001a\b\u0012\u0004\u0012\u0002H\u00020\n\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0007H\u0000\u001a9\u0010\u000b\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\f2\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\u000e2\u0006\u0010\u000f\u001a\u00020\u0001H\u0002\u00a2\u0006\u0002\b\u0010\u001a9\u0010\u000b\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00112\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\u000e2\u0006\u0010\u000f\u001a\u00020\u0001H\u0002\u00a2\u0006\u0002\b\u0010\u001a(\u0010\u0012\u001a\u00020\u0013\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\u0006\u0010\u0014\u001a\u0002H\u0002H\u0087\n\u00a2\u0006\u0002\u0010\u0015\u001a.\u0010\u0012\u001a\u00020\u0013\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0005H\u0087\n\u00a2\u0006\u0002\u0010\u0016\u001a)\u0010\u0012\u001a\u00020\u0013\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007H\u0087\n\u001a)\u0010\u0012\u001a\u00020\u0013\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\bH\u0087\n\u001a(\u0010\u0017\u001a\u00020\u0013\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\u0006\u0010\u0014\u001a\u0002H\u0002H\u0087\n\u00a2\u0006\u0002\u0010\u0015\u001a.\u0010\u0017\u001a\u00020\u0013\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0005H\u0087\n\u00a2\u0006\u0002\u0010\u0016\u001a)\u0010\u0017\u001a\u00020\u0013\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007H\u0087\n\u001a)\u0010\u0017\u001a\u00020\u0013\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\bH\u0087\n\u001a-\u0010\u0018\u001a\u00020\u0001\"\t\b\u0000\u0010\u0002\u00a2\u0006\u0002\b\u0019*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\u0006\u0010\u0014\u001a\u0002H\u0002H\u0087\b\u00a2\u0006\u0002\u0010\u001a\u001a&\u0010\u0018\u001a\u0002H\u0002\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00112\u0006\u0010\u001b\u001a\u00020\u001cH\u0087\b\u00a2\u0006\u0002\u0010\u001d\u001a-\u0010\u001e\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\u000e\u0010\u0004\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0005\u00a2\u0006\u0002\u0010\u0006\u001a&\u0010\u001e\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007\u001a&\u0010\u001e\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\b\u001a.\u0010\u001e\u001a\u00020\u0001\"\t\b\u0000\u0010\u0002\u00a2\u0006\u0002\b\u0019*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\nH\u0087\b\u001a*\u0010\u001e\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\f2\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\u000e\u001a*\u0010\u001e\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00112\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\u000e\u001a\u001d\u0010\u001f\u001a\u0002H\u0002\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0011H\u0007\u00a2\u0006\u0002\u0010 \u001a\u001f\u0010!\u001a\u0004\u0018\u0001H\u0002\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0011H\u0007\u00a2\u0006\u0002\u0010 \u001a\u001d\u0010\"\u001a\u0002H\u0002\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0011H\u0007\u00a2\u0006\u0002\u0010 \u001a\u001f\u0010#\u001a\u0004\u0018\u0001H\u0002\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0011H\u0007\u00a2\u0006\u0002\u0010 \u001a-\u0010$\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\u000e\u0010\u0004\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0005\u00a2\u0006\u0002\u0010\u0006\u001a&\u0010$\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007\u001a&\u0010$\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\b\u001a.\u0010$\u001a\u00020\u0001\"\t\b\u0000\u0010\u0002\u00a2\u0006\u0002\b\u0019*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\nH\u0087\b\u001a*\u0010$\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\f2\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\u000e\u001a*\u0010$\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00112\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\u000e\u001a\u0015\u0010%\u001a\u00020\u0001*\u0006\u0012\u0002\b\u00030\u0003H\u0002\u00a2\u0006\u0002\b&\u00a8\u0006'"}, d2={"addAll", "", "T", "", "elements", "", "(Ljava/util/Collection;[Ljava/lang/Object;)Z", "", "Lkotlin/sequences/Sequence;", "convertToListIfNotCollection", "", "filterInPlace", "", "predicate", "Lkotlin/Function1;", "predicateResultToRemove", "filterInPlace$CollectionsKt__MutableCollectionsKt", "", "minusAssign", "", "element", "(Ljava/util/Collection;Ljava/lang/Object;)V", "(Ljava/util/Collection;[Ljava/lang/Object;)V", "plusAssign", "remove", "Lkotlin/internal/OnlyInputTypes;", "(Ljava/util/Collection;Ljava/lang/Object;)Z", "index", "", "(Ljava/util/List;I)Ljava/lang/Object;", "removeAll", "removeFirst", "(Ljava/util/List;)Ljava/lang/Object;", "removeFirstOrNull", "removeLast", "removeLastOrNull", "retainAll", "retainNothing", "retainNothing$CollectionsKt__MutableCollectionsKt", "kotlin-stdlib"}, xs="kotlin/collections/CollectionsKt")
public class CollectionsKt__MutableCollectionsKt
extends CollectionsKt__MutableCollectionsJVMKt {
    @InlineOnly
    private static final <T> boolean remove(Collection<? extends T> collection, T t2) {
        Intrinsics.checkNotNullParameter(collection, "");
        return TypeIntrinsics.asMutableCollection(collection).remove(t2);
    }

    @InlineOnly
    private static final <T> boolean removeAll(Collection<? extends T> collection, Collection<? extends T> collection2) {
        Intrinsics.checkNotNullParameter(collection, "");
        Intrinsics.checkNotNullParameter(collection2, "");
        return TypeIntrinsics.asMutableCollection(collection).removeAll(collection2);
    }

    @InlineOnly
    private static final <T> boolean retainAll(Collection<? extends T> collection, Collection<? extends T> collection2) {
        Intrinsics.checkNotNullParameter(collection, "");
        Intrinsics.checkNotNullParameter(collection2, "");
        return TypeIntrinsics.asMutableCollection(collection).retainAll(collection2);
    }

    @InlineOnly
    private static final <T> void plusAssign(Collection<? super T> collection, T t2) {
        Intrinsics.checkNotNullParameter(collection, "");
        collection.add(t2);
    }

    @InlineOnly
    private static final <T> void plusAssign(Collection<? super T> collection, Iterable<? extends T> iterable) {
        Intrinsics.checkNotNullParameter(collection, "");
        Intrinsics.checkNotNullParameter(iterable, "");
        CollectionsKt.addAll(collection, iterable);
    }

    @InlineOnly
    private static final <T> void plusAssign(Collection<? super T> collection, T[] TArray) {
        Intrinsics.checkNotNullParameter(collection, "");
        Intrinsics.checkNotNullParameter(TArray, "");
        CollectionsKt.addAll(collection, TArray);
    }

    @InlineOnly
    private static final <T> void plusAssign(Collection<? super T> collection, Sequence<? extends T> sequence2) {
        Intrinsics.checkNotNullParameter(collection, "");
        Intrinsics.checkNotNullParameter(sequence2, "");
        CollectionsKt.addAll(collection, sequence2);
    }

    @InlineOnly
    private static final <T> void minusAssign(Collection<? super T> collection, T t2) {
        Intrinsics.checkNotNullParameter(collection, "");
        collection.remove(t2);
    }

    @InlineOnly
    private static final <T> void minusAssign(Collection<? super T> collection, Iterable<? extends T> iterable) {
        Intrinsics.checkNotNullParameter(collection, "");
        Intrinsics.checkNotNullParameter(iterable, "");
        CollectionsKt.removeAll(collection, iterable);
    }

    @InlineOnly
    private static final <T> void minusAssign(Collection<? super T> collection, T[] TArray) {
        Intrinsics.checkNotNullParameter(collection, "");
        Intrinsics.checkNotNullParameter(TArray, "");
        CollectionsKt.removeAll(collection, TArray);
    }

    @InlineOnly
    private static final <T> void minusAssign(Collection<? super T> collection, Sequence<? extends T> sequence2) {
        Intrinsics.checkNotNullParameter(collection, "");
        Intrinsics.checkNotNullParameter(sequence2, "");
        CollectionsKt.removeAll(collection, sequence2);
    }

    public static final <T> boolean addAll(@NotNull Collection<? super T> collection, @NotNull Iterable<? extends T> object) {
        Intrinsics.checkNotNullParameter(collection, "");
        Intrinsics.checkNotNullParameter(object, "");
        if (object instanceof Collection) {
            return collection.addAll((Collection)object);
        }
        boolean bl = false;
        object = object.iterator();
        while (object.hasNext()) {
            Object e2 = object.next();
            if (!collection.add(e2)) continue;
            bl = true;
        }
        return bl;
    }

    public static final <T> boolean addAll(@NotNull Collection<? super T> collection, @NotNull Sequence<? extends T> object) {
        Intrinsics.checkNotNullParameter(collection, "");
        Intrinsics.checkNotNullParameter(object, "");
        boolean bl = false;
        object = object.iterator();
        while (object.hasNext()) {
            Object e2 = object.next();
            if (!collection.add(e2)) continue;
            bl = true;
        }
        return bl;
    }

    public static final <T> boolean addAll(@NotNull Collection<? super T> collection, @NotNull T[] TArray) {
        Intrinsics.checkNotNullParameter(collection, "");
        Intrinsics.checkNotNullParameter(TArray, "");
        return collection.addAll((Collection)ArraysKt.asList(TArray));
    }

    @NotNull
    public static final <T> Collection<T> convertToListIfNotCollection(@NotNull Iterable<? extends T> iterable) {
        Intrinsics.checkNotNullParameter(iterable, "");
        if (iterable instanceof Collection) {
            return (Collection)iterable;
        }
        return CollectionsKt.toList(iterable);
    }

    public static final <T> boolean removeAll(@NotNull Collection<? super T> collection, @NotNull Iterable<? extends T> iterable) {
        Intrinsics.checkNotNullParameter(collection, "");
        Intrinsics.checkNotNullParameter(iterable, "");
        return collection.removeAll(CollectionsKt.convertToListIfNotCollection(iterable));
    }

    public static final <T> boolean removeAll(@NotNull Collection<? super T> collection, @NotNull Sequence<? extends T> object) {
        Intrinsics.checkNotNullParameter(collection, "");
        Intrinsics.checkNotNullParameter(object, "");
        object = SequencesKt.toList(object);
        return !((Collection)object).isEmpty() && collection.removeAll((Collection)object);
    }

    public static final <T> boolean removeAll(@NotNull Collection<? super T> collection, @NotNull T[] TArray) {
        Intrinsics.checkNotNullParameter(collection, "");
        Intrinsics.checkNotNullParameter(TArray, "");
        return !(TArray.length == 0) && collection.removeAll((Collection)ArraysKt.asList(TArray));
    }

    public static final <T> boolean retainAll(@NotNull Collection<? super T> collection, @NotNull Iterable<? extends T> iterable) {
        Intrinsics.checkNotNullParameter(collection, "");
        Intrinsics.checkNotNullParameter(iterable, "");
        return collection.retainAll(CollectionsKt.convertToListIfNotCollection(iterable));
    }

    public static final <T> boolean retainAll(@NotNull Collection<? super T> collection, @NotNull T[] TArray) {
        Intrinsics.checkNotNullParameter(collection, "");
        Intrinsics.checkNotNullParameter(TArray, "");
        if (!(TArray.length == 0)) {
            return collection.retainAll((Collection)ArraysKt.asList(TArray));
        }
        return CollectionsKt__MutableCollectionsKt.retainNothing$CollectionsKt__MutableCollectionsKt(collection);
    }

    public static final <T> boolean retainAll(@NotNull Collection<? super T> collection, @NotNull Sequence<? extends T> object) {
        Intrinsics.checkNotNullParameter(collection, "");
        Intrinsics.checkNotNullParameter(object, "");
        object = SequencesKt.toList(object);
        if (!((Collection)object).isEmpty()) {
            return collection.retainAll((Collection)object);
        }
        return CollectionsKt__MutableCollectionsKt.retainNothing$CollectionsKt__MutableCollectionsKt(collection);
    }

    private static final boolean retainNothing$CollectionsKt__MutableCollectionsKt(Collection<?> collection) {
        boolean bl = !collection.isEmpty();
        collection.clear();
        return bl;
    }

    public static final <T> boolean removeAll(@NotNull Iterable<? extends T> iterable, @NotNull Function1<? super T, Boolean> function1) {
        Intrinsics.checkNotNullParameter(iterable, "");
        Intrinsics.checkNotNullParameter(function1, "");
        return CollectionsKt__MutableCollectionsKt.filterInPlace$CollectionsKt__MutableCollectionsKt(iterable, function1, true);
    }

    public static final <T> boolean retainAll(@NotNull Iterable<? extends T> iterable, @NotNull Function1<? super T, Boolean> function1) {
        Intrinsics.checkNotNullParameter(iterable, "");
        Intrinsics.checkNotNullParameter(function1, "");
        return CollectionsKt__MutableCollectionsKt.filterInPlace$CollectionsKt__MutableCollectionsKt(iterable, function1, false);
    }

    private static final <T> boolean filterInPlace$CollectionsKt__MutableCollectionsKt(Iterable<? extends T> iterator2, Function1<? super T, Boolean> function1, boolean bl) {
        boolean bl2 = false;
        iterator2 = iterator2.iterator();
        while (iterator2.hasNext()) {
            if (function1.invoke(iterator2.next()) != bl) continue;
            iterator2.remove();
            bl2 = true;
        }
        return bl2;
    }

    @Deprecated(message="Use removeAt(index) instead.", replaceWith=@ReplaceWith(expression="removeAt(index)", imports={}), level=DeprecationLevel.ERROR)
    @InlineOnly
    private static final <T> T remove(List<T> list2, int n2) {
        Intrinsics.checkNotNullParameter(list2, "");
        return list2.remove(n2);
    }

    @SinceKotlin(version="1.4")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    public static final <T> T removeFirst(@NotNull List<T> list2) {
        Intrinsics.checkNotNullParameter(list2, "");
        if (list2.isEmpty()) {
            throw new NoSuchElementException("List is empty.");
        }
        return list2.remove(0);
    }

    @SinceKotlin(version="1.4")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @Nullable
    public static final <T> T removeFirstOrNull(@NotNull List<T> list2) {
        Intrinsics.checkNotNullParameter(list2, "");
        if (list2.isEmpty()) {
            return null;
        }
        return list2.remove(0);
    }

    @SinceKotlin(version="1.4")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    public static final <T> T removeLast(@NotNull List<T> list2) {
        Intrinsics.checkNotNullParameter(list2, "");
        if (list2.isEmpty()) {
            throw new NoSuchElementException("List is empty.");
        }
        List<T> list3 = list2;
        return list3.remove(CollectionsKt.getLastIndex(list3));
    }

    @SinceKotlin(version="1.4")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @Nullable
    public static final <T> T removeLastOrNull(@NotNull List<T> list2) {
        Intrinsics.checkNotNullParameter(list2, "");
        if (list2.isEmpty()) {
            return null;
        }
        List<T> list3 = list2;
        return list3.remove(CollectionsKt.getLastIndex(list3));
    }

    public static final <T> boolean removeAll(@NotNull List<T> list2, @NotNull Function1<? super T, Boolean> function1) {
        Intrinsics.checkNotNullParameter(list2, "");
        Intrinsics.checkNotNullParameter(function1, "");
        return CollectionsKt__MutableCollectionsKt.filterInPlace$CollectionsKt__MutableCollectionsKt(list2, function1, true);
    }

    public static final <T> boolean retainAll(@NotNull List<T> list2, @NotNull Function1<? super T, Boolean> function1) {
        Intrinsics.checkNotNullParameter(list2, "");
        Intrinsics.checkNotNullParameter(function1, "");
        return CollectionsKt__MutableCollectionsKt.filterInPlace$CollectionsKt__MutableCollectionsKt(list2, function1, false);
    }

    private static final <T> boolean filterInPlace$CollectionsKt__MutableCollectionsKt(List<T> list2, Function1<? super T, Boolean> function1, boolean bl) {
        int n2;
        if (!(list2 instanceof RandomAccess)) {
            Intrinsics.checkNotNull(list2);
            return CollectionsKt__MutableCollectionsKt.filterInPlace$CollectionsKt__MutableCollectionsKt(TypeIntrinsics.asMutableIterable(list2), function1, bl);
        }
        int n3 = 0;
        IntIterator intIterator = new IntRange(0, CollectionsKt.getLastIndex(list2)).iterator();
        while (intIterator.hasNext()) {
            n2 = intIterator.nextInt();
            T t2 = list2.get(n2);
            if (function1.invoke(t2) == bl) continue;
            if (n3 != n2) {
                list2.set(n3, t2);
            }
            ++n3;
        }
        if (n3 < list2.size()) {
            n2 = n3;
            int n4 = CollectionsKt.getLastIndex(list2);
            if (n2 <= n4) {
                while (true) {
                    list2.remove(n4);
                    if (n4 == n2) break;
                    --n4;
                }
            }
            return true;
        }
        return false;
    }
}


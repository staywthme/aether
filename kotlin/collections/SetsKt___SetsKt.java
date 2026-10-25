/*
 * Decompiled with CFR 0.152.
 */
package kotlin.collections;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.collections.SetsKt;
import kotlin.collections.SetsKt__SetsKt;
import kotlin.internal.InlineOnly;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.sequences.Sequence;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 8, 0}, k=5, xi=49, d1={"\u0000\u001c\n\u0000\n\u0002\u0010\"\n\u0002\b\u0004\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u001c\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a,\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0003\u001a\u0002H\u0002H\u0086\u0002\u00a2\u0006\u0002\u0010\u0004\u001a4\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u000e\u0010\u0005\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0006H\u0086\u0002\u00a2\u0006\u0002\u0010\u0007\u001a-\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\bH\u0086\u0002\u001a-\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\tH\u0086\u0002\u001a,\u0010\n\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0003\u001a\u0002H\u0002H\u0087\b\u00a2\u0006\u0002\u0010\u0004\u001a,\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0003\u001a\u0002H\u0002H\u0086\u0002\u00a2\u0006\u0002\u0010\u0004\u001a4\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u000e\u0010\u0005\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0006H\u0086\u0002\u00a2\u0006\u0002\u0010\u0007\u001a-\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\bH\u0086\u0002\u001a-\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\tH\u0086\u0002\u001a,\u0010\f\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0003\u001a\u0002H\u0002H\u0087\b\u00a2\u0006\u0002\u0010\u0004\u00a8\u0006\r"}, d2={"minus", "", "T", "element", "(Ljava/util/Set;Ljava/lang/Object;)Ljava/util/Set;", "elements", "", "(Ljava/util/Set;[Ljava/lang/Object;)Ljava/util/Set;", "", "Lkotlin/sequences/Sequence;", "minusElement", "plus", "plusElement", "kotlin-stdlib"}, xs="kotlin/collections/SetsKt")
@SourceDebugExtension(value={"SMAP\n_Sets.kt\nKotlin\n*S Kotlin\n*F\n+ 1 _Sets.kt\nkotlin/collections/SetsKt___SetsKt\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 3 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,139:1\n857#2,2:140\n847#2,2:142\n1#3:144\n*S KotlinDebug\n*F\n+ 1 _Sets.kt\nkotlin/collections/SetsKt___SetsKt\n*L\n28#1:140,2\n52#1:142,2\n*E\n"})
class SetsKt___SetsKt
extends SetsKt__SetsKt {
    @NotNull
    public static final <T> Set<T> minus(@NotNull Set<? extends T> object, T t2) {
        Intrinsics.checkNotNullParameter(object, "");
        LinkedHashSet linkedHashSet = new LinkedHashSet(MapsKt.mapCapacity(object.size()));
        boolean bl = false;
        object = (Iterable)object;
        object = object.iterator();
        while (object.hasNext()) {
            boolean bl2;
            Object e2;
            Object e3 = e2 = object.next();
            if (!bl && Intrinsics.areEqual(e3, t2)) {
                bl = true;
                bl2 = false;
            } else {
                bl2 = true;
            }
            if (!bl2) continue;
            ((Collection)linkedHashSet).add(e2);
        }
        return (Set)((Collection)linkedHashSet);
    }

    @NotNull
    public static final <T> Set<T> minus(@NotNull Set<? extends T> set, @NotNull T[] TArray) {
        Intrinsics.checkNotNullParameter(set, "");
        Intrinsics.checkNotNullParameter(TArray, "");
        set = new LinkedHashSet<T>((Collection)set);
        CollectionsKt.removeAll((Collection)set, TArray);
        return set;
    }

    @NotNull
    public static final <T> Set<T> minus(@NotNull Set<? extends T> object, @NotNull Iterable<? extends T> iterable) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(iterable, "");
        iterable = CollectionsKt.convertToListIfNotCollection(iterable);
        if (iterable.isEmpty()) {
            return CollectionsKt.toSet((Iterable)object);
        }
        if (iterable instanceof Set) {
            object = (Iterable)object;
            Collection collection = new LinkedHashSet();
            object = object.iterator();
            while (object.hasNext()) {
                Object e2 = object.next();
                Object e3 = e2;
                if (iterable.contains(e3)) continue;
                collection.add(e2);
            }
            return (Set)collection;
        }
        object = new LinkedHashSet<T>((Collection)object);
        ((AbstractSet)object).removeAll((Collection<?>)iterable);
        return (Set)object;
    }

    @NotNull
    public static final <T> Set<T> minus(@NotNull Set<? extends T> set, @NotNull Sequence<? extends T> sequence2) {
        Intrinsics.checkNotNullParameter(set, "");
        Intrinsics.checkNotNullParameter(sequence2, "");
        set = new LinkedHashSet<T>((Collection)set);
        CollectionsKt.removeAll((Collection)set, sequence2);
        return set;
    }

    @InlineOnly
    private static final <T> Set<T> minusElement(Set<? extends T> set, T t2) {
        Intrinsics.checkNotNullParameter(set, "");
        return SetsKt.minus(set, t2);
    }

    @NotNull
    public static final <T> Set<T> plus(@NotNull Set<? extends T> set, T t2) {
        Intrinsics.checkNotNullParameter(set, "");
        LinkedHashSet<T> linkedHashSet = new LinkedHashSet<T>(MapsKt.mapCapacity(set.size() + 1));
        linkedHashSet.addAll((Collection)set);
        linkedHashSet.add(t2);
        return linkedHashSet;
    }

    @NotNull
    public static final <T> Set<T> plus(@NotNull Set<? extends T> set, @NotNull T[] TArray) {
        Intrinsics.checkNotNullParameter(set, "");
        Intrinsics.checkNotNullParameter(TArray, "");
        LinkedHashSet linkedHashSet = new LinkedHashSet(MapsKt.mapCapacity(set.size() + TArray.length));
        linkedHashSet.addAll(set);
        CollectionsKt.addAll((Collection)linkedHashSet, TArray);
        return linkedHashSet;
    }

    @NotNull
    public static final <T> Set<T> plus(@NotNull Set<? extends T> set, @NotNull Iterable<? extends T> iterable) {
        int n2;
        int n3;
        Intrinsics.checkNotNullParameter(set, "");
        Intrinsics.checkNotNullParameter(iterable, "");
        Integer n4 = CollectionsKt.collectionSizeOrNull(iterable);
        if (n4 != null) {
            Integer n5 = n4;
            n3 = ((Number)n4).intValue();
            n2 = set.size() + n3;
        } else {
            n2 = set.size() << 1;
        }
        n3 = MapsKt.mapCapacity(n2);
        LinkedHashSet linkedHashSet = new LinkedHashSet(n3);
        linkedHashSet.addAll(set);
        CollectionsKt.addAll((Collection)linkedHashSet, iterable);
        return linkedHashSet;
    }

    @NotNull
    public static final <T> Set<T> plus(@NotNull Set<? extends T> set, @NotNull Sequence<? extends T> sequence2) {
        Intrinsics.checkNotNullParameter(set, "");
        Intrinsics.checkNotNullParameter(sequence2, "");
        LinkedHashSet linkedHashSet = new LinkedHashSet(MapsKt.mapCapacity(set.size() << 1));
        linkedHashSet.addAll(set);
        CollectionsKt.addAll((Collection)linkedHashSet, sequence2);
        return linkedHashSet;
    }

    @InlineOnly
    private static final <T> Set<T> plusElement(Set<? extends T> set, T t2) {
        Intrinsics.checkNotNullParameter(set, "");
        return SetsKt.plus(set, t2);
    }
}


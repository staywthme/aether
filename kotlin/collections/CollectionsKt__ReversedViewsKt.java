/*
 * Decompiled with CFR 0.152.
 */
package kotlin.collections;

import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.collections.CollectionsKt__MutableCollectionsKt;
import kotlin.collections.ReversedList;
import kotlin.collections.ReversedListReadOnly;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 8, 0}, k=5, xi=49, d1={"\u0000\u0018\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010!\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\u001a\u001c\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0001\u001a#\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0003\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u0007\u00a2\u0006\u0002\b\u0004\u001a\u001d\u0010\u0005\u001a\u00020\u0006*\u0006\u0012\u0002\b\u00030\u00012\u0006\u0010\u0007\u001a\u00020\u0006H\u0002\u00a2\u0006\u0002\b\b\u001a\u001d\u0010\t\u001a\u00020\u0006*\u0006\u0012\u0002\b\u00030\u00012\u0006\u0010\u0007\u001a\u00020\u0006H\u0002\u00a2\u0006\u0002\b\n\u00a8\u0006\u000b"}, d2={"asReversed", "", "T", "", "asReversedMutable", "reverseElementIndex", "", "index", "reverseElementIndex$CollectionsKt__ReversedViewsKt", "reversePositionIndex", "reversePositionIndex$CollectionsKt__ReversedViewsKt", "kotlin-stdlib"}, xs="kotlin/collections/CollectionsKt")
class CollectionsKt__ReversedViewsKt
extends CollectionsKt__MutableCollectionsKt {
    private static final int reverseElementIndex$CollectionsKt__ReversedViewsKt(List<?> list2, int n2) {
        if (new IntRange(0, CollectionsKt.getLastIndex(list2)).contains(n2)) {
            return CollectionsKt.getLastIndex(list2) - n2;
        }
        throw new IndexOutOfBoundsException("Element index " + n2 + " must be in range [" + new IntRange(0, CollectionsKt.getLastIndex(list2)) + "].");
    }

    private static final int reversePositionIndex$CollectionsKt__ReversedViewsKt(List<?> list2, int n2) {
        if (new IntRange(0, list2.size()).contains(n2)) {
            return list2.size() - n2;
        }
        throw new IndexOutOfBoundsException("Position index " + n2 + " must be in range [" + new IntRange(0, list2.size()) + "].");
    }

    @NotNull
    public static final <T> List<T> asReversed(@NotNull List<? extends T> list2) {
        Intrinsics.checkNotNullParameter(list2, "");
        return new ReversedListReadOnly<T>(list2);
    }

    @JvmName(name="asReversedMutable")
    @NotNull
    public static final <T> List<T> asReversedMutable(@NotNull List<T> list2) {
        Intrinsics.checkNotNullParameter(list2, "");
        return new ReversedList<T>(list2);
    }

    public static final /* synthetic */ int access$reverseElementIndex(List list2, int n2) {
        return CollectionsKt__ReversedViewsKt.reverseElementIndex$CollectionsKt__ReversedViewsKt(list2, n2);
    }

    public static final /* synthetic */ int access$reversePositionIndex(List list2, int n2) {
        return CollectionsKt__ReversedViewsKt.reversePositionIndex$CollectionsKt__ReversedViewsKt(list2, n2);
    }
}


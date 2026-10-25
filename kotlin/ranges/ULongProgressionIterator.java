/*
 * Decompiled with CFR 0.152.
 */
package kotlin.ranges;

import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.ULong;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.markers.KMappedMarker;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010(\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B \u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\u0006\u0010\u0004\u001a\u00020\u0002\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0007J\t\u0010\n\u001a\u00020\u000bH\u0096\u0002J\u0016\u0010\f\u001a\u00020\u0002H\u0096\u0002\u00f8\u0001\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b\r\u0010\u000eR\u0016\u0010\b\u001a\u00020\u0002X\u0082\u0004\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\n\u0002\u0010\tR\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0016\u0010\f\u001a\u00020\u0002X\u0082\u000e\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\n\u0002\u0010\tR\u0016\u0010\u0005\u001a\u00020\u0002X\u0082\u0004\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\n\u0002\u0010\t\u00f8\u0001\u0000\u0082\u0002\b\n\u0002\b\u0019\n\u0002\b!\u00a8\u0006\u000f"}, d2={"Lkotlin/ranges/ULongProgressionIterator;", "", "Lkotlin/ULong;", "first", "last", "step", "", "(JJJLkotlin/jvm/internal/DefaultConstructorMarker;)V", "finalElement", "J", "hasNext", "", "next", "next-s-VKNKU", "()J", "kotlin-stdlib"})
@SinceKotlin(version="1.3")
final class ULongProgressionIterator
implements Iterator<ULong>,
KMappedMarker {
    private final long finalElement;
    private boolean hasNext;
    private final long step;
    private long next;

    private ULongProgressionIterator(long l2, long l3, long l4) {
        this.finalElement = l3;
        this.hasNext = l4 > 0L ? Long.compareUnsigned(l2, l3) <= 0 : Long.compareUnsigned(l2, l3) >= 0;
        this.step = ULong.constructor-impl(l4);
        this.next = this.hasNext ? l2 : this.finalElement;
    }

    @Override
    public final boolean hasNext() {
        return this.hasNext;
    }

    public final long next-s-VKNKU() {
        long l2 = this.next;
        if (l2 == this.finalElement) {
            if (!this.hasNext) {
                throw new NoSuchElementException();
            }
            this.hasNext = false;
        } else {
            this.next = ULong.constructor-impl(this.next + this.step);
        }
        return l2;
    }

    @Override
    public final void remove() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override
    public final /* synthetic */ Object next() {
        return ULong.box-impl(this.next-s-VKNKU());
    }

    public /* synthetic */ ULongProgressionIterator(long l2, long l3, long l4, DefaultConstructorMarker defaultConstructorMarker) {
        this(l2, l3, l4);
    }
}


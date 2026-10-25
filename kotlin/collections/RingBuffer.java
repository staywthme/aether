/*
 * Decompiled with CFR 0.152.
 */
package kotlin.collections;

import java.util.Arrays;
import java.util.Iterator;
import java.util.RandomAccess;
import kotlin.Metadata;
import kotlin.collections.AbstractCollection;
import kotlin.collections.AbstractIterator;
import kotlin.collections.AbstractList;
import kotlin.collections.ArraysKt;
import kotlin.collections.RingBuffer;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.ranges.RangesKt;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000>\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\t\n\u0002\u0010\u0002\n\u0002\b\b\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010(\n\u0002\b\b\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\u00060\u0003j\u0002`\u0004B\u000f\b\u0016\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007B\u001d\u0012\u000e\u0010\b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\t\u0012\u0006\u0010\u000b\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\fJ\u0013\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00028\u0000\u00a2\u0006\u0002\u0010\u0016J\u0014\u0010\u0017\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\u0006\u0010\u0018\u001a\u00020\u0006J\u0016\u0010\u0019\u001a\u00028\u00002\u0006\u0010\u001a\u001a\u00020\u0006H\u0096\u0002\u00a2\u0006\u0002\u0010\u001bJ\u0006\u0010\u001c\u001a\u00020\u001dJ\u000f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00028\u00000\u001fH\u0096\u0002J\u000e\u0010 \u001a\u00020\u00142\u0006\u0010!\u001a\u00020\u0006J\u0015\u0010\"\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\tH\u0014\u00a2\u0006\u0002\u0010#J'\u0010\"\u001a\b\u0012\u0004\u0012\u0002H\u00010\t\"\u0004\b\u0001\u0010\u00012\f\u0010$\u001a\b\u0012\u0004\u0012\u0002H\u00010\tH\u0014\u00a2\u0006\u0002\u0010%J\u0015\u0010&\u001a\u00020\u0006*\u00020\u00062\u0006\u0010!\u001a\u00020\u0006H\u0082\bR\u0018\u0010\b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\tX\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\rR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u000e\u001a\u00020\u0006@RX\u0096\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u000e\u0010\u0012\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006'"}, d2={"Lkotlin/collections/RingBuffer;", "T", "Lkotlin/collections/AbstractList;", "Ljava/util/RandomAccess;", "Lkotlin/collections/RandomAccess;", "capacity", "", "(I)V", "buffer", "", "", "filledSize", "([Ljava/lang/Object;I)V", "[Ljava/lang/Object;", "<set-?>", "size", "getSize", "()I", "startIndex", "add", "", "element", "(Ljava/lang/Object;)V", "expanded", "maxCapacity", "get", "index", "(I)Ljava/lang/Object;", "isFull", "", "iterator", "", "removeFirst", "n", "toArray", "()[Ljava/lang/Object;", "array", "([Ljava/lang/Object;)[Ljava/lang/Object;", "forward", "kotlin-stdlib"})
@SourceDebugExtension(value={"SMAP\nSlidingWindow.kt\nKotlin\n*S Kotlin\n*F\n+ 1 SlidingWindow.kt\nkotlin/collections/RingBuffer\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,207:1\n205#1:209\n205#1:210\n205#1:211\n1#2:208\n*S KotlinDebug\n*F\n+ 1 SlidingWindow.kt\nkotlin/collections/RingBuffer\n*L\n106#1:209\n176#1:210\n189#1:211\n*E\n"})
final class RingBuffer<T>
extends AbstractList<T>
implements RandomAccess {
    @NotNull
    private final Object[] buffer;
    private final int capacity;
    private int startIndex;
    private int size;

    public RingBuffer(@NotNull Object[] objectArray, int n2) {
        Intrinsics.checkNotNullParameter(objectArray, "");
        this.buffer = objectArray;
        boolean bl = n2 >= 0;
        if (!bl) {
            String string = "ring buffer filled size should not be negative but it is " + n2;
            throw new IllegalArgumentException(string.toString());
        }
        bl = n2 <= this.buffer.length;
        if (!bl) {
            String string = "ring buffer filled size: " + n2 + " cannot be larger than the buffer size: " + this.buffer.length;
            throw new IllegalArgumentException(string.toString());
        }
        this.capacity = this.buffer.length;
        this.size = n2;
    }

    public RingBuffer(int n2) {
        this(new Object[n2], 0);
    }

    @Override
    public final int getSize() {
        return this.size;
    }

    @Override
    public final T get(int n2) {
        AbstractList.Companion.checkElementIndex$kotlin_stdlib(n2, this.size());
        RingBuffer ringBuffer = this;
        int n3 = this.startIndex;
        return (T)this.buffer[(n3 + n2) % ringBuffer.capacity];
    }

    public final boolean isFull() {
        return this.size() == this.capacity;
    }

    @Override
    @NotNull
    public final Iterator<T> iterator() {
        return new AbstractIterator<T>(this){
            private int count;
            private int index;
            final /* synthetic */ RingBuffer<T> this$0;
            {
                this.this$0 = ringBuffer;
                this.count = ringBuffer.size();
                this.index = RingBuffer.access$getStartIndex$p(ringBuffer);
            }

            protected final void computeNext() {
                if (this.count == 0) {
                    this.done();
                    return;
                }
                iterator.1 v0 = this;
                v0.setNext(RingBuffer.access$getBuffer$p(v0.this$0)[this.index]);
                RingBuffer<T> ringBuffer = this.this$0;
                int n2 = this.index;
                this.index = (n2 + 1) % RingBuffer.access$getCapacity$p(ringBuffer);
                int n3 = this.count;
                this.count = n3 + -1;
            }
        };
    }

    @Override
    @NotNull
    public final <T> T[] toArray(@NotNull T[] TArray) {
        int n2;
        T[] TArray2;
        Intrinsics.checkNotNullParameter(TArray, "");
        if (TArray.length < this.size()) {
            T[] TArray3 = Arrays.copyOf(TArray, this.size());
            TArray2 = TArray3;
            Intrinsics.checkNotNullExpressionValue(TArray3, "");
        } else {
            TArray2 = TArray;
        }
        TArray = TArray2;
        int n3 = this.size();
        int n4 = 0;
        for (n2 = this.startIndex; n4 < n3 && n2 < this.capacity; ++n4, ++n2) {
            TArray[n4] = this.buffer[n2];
        }
        n2 = 0;
        while (n4 < n3) {
            TArray[n4] = this.buffer[n2];
            ++n4;
            ++n2;
        }
        if (TArray.length > this.size()) {
            TArray[this.size()] = null;
        }
        return TArray;
    }

    @Override
    @NotNull
    public final Object[] toArray() {
        RingBuffer ringBuffer = this;
        return ((AbstractCollection)ringBuffer).toArray(new Object[ringBuffer.size()]);
    }

    @NotNull
    public final RingBuffer<T> expanded(int n2) {
        Object[] objectArray;
        n2 = RangesKt.coerceAtMost(this.capacity + (this.capacity >> 1) + 1, n2);
        if (this.startIndex == 0) {
            Object[] objectArray2 = Arrays.copyOf(this.buffer, n2);
            objectArray = objectArray2;
            Intrinsics.checkNotNullExpressionValue(objectArray2, "");
        } else {
            objectArray = ((AbstractCollection)this).toArray(new Object[n2]);
        }
        Object[] objectArray3 = objectArray;
        return new RingBuffer<T>(objectArray3, this.size());
    }

    public final void add(T t2) {
        if (this.isFull()) {
            throw new IllegalStateException("ring buffer is full");
        }
        RingBuffer ringBuffer = this;
        int n2 = this.startIndex;
        int n3 = this.size();
        this.buffer[(n2 + n3) % ((RingBuffer)ringBuffer).capacity] = t2;
        int n4 = this.size();
        this.size = n4 + 1;
    }

    public final void removeFirst(int n2) {
        int n3 = n2 >= 0 ? 1 : 0;
        if (n3 == 0) {
            String string = "n shouldn't be negative but it is " + n2;
            throw new IllegalArgumentException(string.toString());
        }
        n3 = n2 <= this.size() ? 1 : 0;
        if (n3 == 0) {
            String string = "n shouldn't be greater than the buffer size: n = " + n2 + ", size = " + this.size();
            throw new IllegalArgumentException(string.toString());
        }
        if (n2 > 0) {
            n3 = this.startIndex;
            RingBuffer ringBuffer = this;
            int n4 = (n3 + n2) % ringBuffer.capacity;
            if (n3 > n4) {
                ArraysKt.fill(this.buffer, null, n3, this.capacity);
                ArraysKt.fill(this.buffer, null, 0, n4);
            } else {
                ArraysKt.fill(this.buffer, null, n3, n4);
            }
            this.startIndex = n4;
            this.size = this.size() - n2;
        }
    }

    private final int forward(int n2, int n3) {
        return (n2 + n3) % this.capacity;
    }

    public static final /* synthetic */ int access$getStartIndex$p(RingBuffer ringBuffer) {
        return ringBuffer.startIndex;
    }

    public static final /* synthetic */ Object[] access$getBuffer$p(RingBuffer ringBuffer) {
        return ringBuffer.buffer;
    }
}


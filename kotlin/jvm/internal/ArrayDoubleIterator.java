/*
 * Decompiled with CFR 0.152.
 */
package kotlin.jvm.internal;

import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.collections.DoubleIterator;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0013\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0006\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\bH\u0096\u0002J\b\u0010\t\u001a\u00020\nH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2={"Lkotlin/jvm/internal/ArrayDoubleIterator;", "Lkotlin/collections/DoubleIterator;", "array", "", "([D)V", "index", "", "hasNext", "", "nextDouble", "", "kotlin-stdlib"})
final class ArrayDoubleIterator
extends DoubleIterator {
    @NotNull
    private final double[] array;
    private int index;

    public ArrayDoubleIterator(@NotNull double[] dArray) {
        Intrinsics.checkNotNullParameter(dArray, "");
        this.array = dArray;
    }

    @Override
    public final boolean hasNext() {
        return this.index < this.array.length;
    }

    @Override
    public final double nextDouble() {
        double d2;
        try {
            int n2 = this.index;
            this.index = n2 + 1;
            d2 = this.array[n2];
        }
        catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
            --this.index;
            throw new NoSuchElementException(arrayIndexOutOfBoundsException.getMessage());
        }
        return d2;
    }
}


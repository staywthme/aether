/*
 * Decompiled with CFR 0.152.
 */
package org.bridj;

import org.bridj.Pointer;
import org.bridj.PointerIO;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
abstract class PointerLRUCache {
    private final Pointer<?>[] values;
    private int index;
    private final int tolerance;

    PointerLRUCache(int n2, int n3) {
        assert (n3 >= 0);
        this.tolerance = n3;
        this.values = new Pointer[n2];
    }

    protected abstract <T> Pointer<T> pointerToAddress(long var1, PointerIO<T> var3);

    Pointer<?> get(long l2, PointerIO<?> pointerIO) {
        int n2 = this.index;
        int n3 = this.values.length;
        for (int i2 = 0; i2 < n3; ++i2) {
            Pointer<?> pointer = this.values[n2];
            if (pointer == null) {
                pointer = this.pointerToAddress(l2, pointerIO);
                this.values[n2] = pointer;
                return pointer;
            }
            if (pointer.getPeer() == l2 && pointer.getIO() == pointerIO) {
                int n4;
                if (i2 > this.tolerance && n2 != (n4 = this.decrementedIndex())) {
                    Pointer<?> pointer2 = this.values[n2];
                    this.values[n2] = this.values[n4];
                    this.values[n4] = pointer2;
                }
                return pointer;
            }
            if (++n2 != n3) continue;
            n2 = 0;
        }
        Pointer<?> pointer = this.pointerToAddress(l2, pointerIO);
        this.values[this.decrementedIndex()] = pointer;
        return pointer;
    }

    private int decrementedIndex() {
        int n2 = this.index;
        if (--n2 < 0) {
            n2 = this.values.length - 1;
        }
        this.index = n2;
        return n2;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package org.bridj;

import org.bridj.Pointer;
import org.bridj.PointerIO;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class TypedPointer
extends Pointer.OrderedPointer {
    Pointer<?> copy;

    private TypedPointer(PointerIO<?> pointerIO, long l2) {
        super(pointerIO, l2, -1L, -1L, null, 0L, null);
    }

    public TypedPointer(long l2) {
        this(PointerIO.getPointerInstance(), l2);
    }

    public TypedPointer(Pointer<?> pointer) {
        this(PointerIO.getPointerInstance(), pointer.getPeer());
        this.copy = pointer;
    }
}


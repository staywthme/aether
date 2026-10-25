/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.ptr;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.ByReference;

public class LongByReference
extends ByReference {
    public LongByReference() {
        this(0L);
    }

    public LongByReference(long l2) {
        super(8);
        this.setValue(l2);
    }

    public void setValue(long l2) {
        this.getPointer().setLong(0L, l2);
    }

    public long getValue() {
        return this.getPointer().getLong(0L);
    }

    @Override
    public String toString() {
        return String.format("long@0x%1$x=0x%2$x (%2$d)", Pointer.nativeValue(this.getPointer()), this.getValue());
    }
}


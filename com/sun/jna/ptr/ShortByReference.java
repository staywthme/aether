/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.ptr;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.ByReference;

public class ShortByReference
extends ByReference {
    public ShortByReference() {
        this(0);
    }

    public ShortByReference(short s2) {
        super(2);
        this.setValue(s2);
    }

    public void setValue(short s2) {
        this.getPointer().setShort(0L, s2);
    }

    public short getValue() {
        return this.getPointer().getShort(0L);
    }

    @Override
    public String toString() {
        return String.format("short@0x%1$x=0x%2$x (%2$d)", Pointer.nativeValue(this.getPointer()), this.getValue());
    }
}


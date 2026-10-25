/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.ptr;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.ByReference;

public class DoubleByReference
extends ByReference {
    public DoubleByReference() {
        this(0.0);
    }

    public DoubleByReference(double d2) {
        super(8);
        this.setValue(d2);
    }

    public void setValue(double d2) {
        this.getPointer().setDouble(0L, d2);
    }

    public double getValue() {
        return this.getPointer().getDouble(0L);
    }

    @Override
    public String toString() {
        return String.format("double@0x%x=%s", Pointer.nativeValue(this.getPointer()), this.getValue());
    }
}


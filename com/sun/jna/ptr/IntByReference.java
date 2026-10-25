/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.ptr;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.ByReference;

public class IntByReference
extends ByReference {
    public IntByReference() {
        this(0);
    }

    public IntByReference(int n2) {
        super(4);
        this.setValue(n2);
    }

    public void setValue(int n2) {
        this.getPointer().setInt(0L, n2);
    }

    public int getValue() {
        return this.getPointer().getInt(0L);
    }

    @Override
    public String toString() {
        return String.format("int@0x%1$x=0x%2$x (%2$d)", Pointer.nativeValue(this.getPointer()), this.getValue());
    }
}


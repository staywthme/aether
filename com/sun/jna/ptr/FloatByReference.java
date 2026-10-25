/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.ptr;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.ByReference;

public class FloatByReference
extends ByReference {
    public FloatByReference() {
        this(0.0f);
    }

    public FloatByReference(float f2) {
        super(4);
        this.setValue(f2);
    }

    public void setValue(float f2) {
        this.getPointer().setFloat(0L, f2);
    }

    public float getValue() {
        return this.getPointer().getFloat(0L);
    }

    @Override
    public String toString() {
        return String.format("float@0x%x=%s", Pointer.nativeValue(this.getPointer()), Float.valueOf(this.getValue()));
    }
}


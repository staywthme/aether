/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.ptr;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import java.lang.reflect.Method;

public abstract class ByReference
extends PointerType {
    public ByReference(int n2) {
        this.setPointer(new Memory(n2));
    }

    @Override
    public String toString() {
        try {
            Object object = this.getClass().getMethod("getValue", new Class[0]);
            object = ((Method)object).invoke(this, new Object[0]);
            if (object == null) {
                return String.format("null@0x%x", Pointer.nativeValue(this.getPointer()));
            }
            return String.format("%s@0x%x=%s", object.getClass().getSimpleName(), Pointer.nativeValue(this.getPointer()), object);
        }
        catch (Exception exception) {
            return String.format("ByReference Contract violated - %s#getValue raised exception: %s", this.getClass().getName(), exception.getMessage());
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna;

import com.sun.jna.FromNativeContext;
import com.sun.jna.Klass;
import com.sun.jna.NativeMapped;
import com.sun.jna.Pointer;

public abstract class PointerType
implements NativeMapped {
    private Pointer pointer;

    public PointerType() {
        this.pointer = Pointer.NULL;
    }

    public PointerType(Pointer pointer) {
        this.pointer = pointer;
    }

    @Override
    public Class<?> nativeType() {
        return Pointer.class;
    }

    @Override
    public Object toNative() {
        return this.getPointer();
    }

    public Pointer getPointer() {
        return this.pointer;
    }

    public void setPointer(Pointer pointer) {
        this.pointer = pointer;
    }

    @Override
    public Object fromNative(Object object, FromNativeContext object2) {
        if (object == null) {
            return null;
        }
        object2 = (PointerType)Klass.newInstance(this.getClass());
        ((PointerType)Klass.newInstance(this.getClass())).pointer = (Pointer)object;
        return object2;
    }

    public int hashCode() {
        if (this.pointer != null) {
            return this.pointer.hashCode();
        }
        return 0;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof PointerType) {
            object = ((PointerType)object).getPointer();
            if (this.pointer == null) {
                return object == null;
            }
            return this.pointer.equals(object);
        }
        return false;
    }

    public String toString() {
        if (this.pointer == null) {
            return "NULL";
        }
        return this.pointer.toString() + " (" + super.toString() + ")";
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package org.bridj.objc;

import org.bridj.Pointer;
import org.bridj.TypedPointer;
import org.bridj.objc.ObjectiveCRuntime;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class SEL
extends TypedPointer {
    protected volatile String name;

    public SEL(long l2) {
        super(l2);
    }

    public SEL(Pointer<?> pointer) {
        super(pointer);
    }

    public static SEL valueOf(String string) {
        return ObjectiveCRuntime.sel_registerName(SEL.pointerToCString(string));
    }

    public String getName() {
        if (this.name == null) {
            this.name = ObjectiveCRuntime.sel_getName(this).getCString();
        }
        return this.name;
    }

    @Override
    public String toString() {
        return "@selector(" + this.getName() + ")";
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SEL)) {
            return false;
        }
        return this.getName().equals(((SEL)object).getName());
    }

    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }
}


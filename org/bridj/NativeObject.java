/*
 * Decompiled with CFR 0.152.
 */
package org.bridj;

import org.bridj.BridJ;
import org.bridj.BridJRuntime;
import org.bridj.NativeObjectInterface;
import org.bridj.Pointer;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public abstract class NativeObject
implements NativeObjectInterface {
    public Pointer<? extends NativeObject> peer;
    protected BridJRuntime.TypeInfo typeInfo;

    public NativeObject(Pointer<? extends NativeObject> pointer, Object ... objectArray) {
        BridJ.initialize(this, pointer, objectArray);
    }

    public NativeObject() {
        BridJ.initialize(this);
    }

    public NativeObject(int n2, Object ... objectArray) {
        BridJ.initialize(this, n2, objectArray);
    }

    public NativeObject clone() throws CloneNotSupportedException {
        return BridJ.clone(this);
    }

    public boolean equals(Object object) {
        if (!(object instanceof NativeObject)) {
            return false;
        }
        return this.typeInfo.equal(this, (NativeObject)object);
    }
}


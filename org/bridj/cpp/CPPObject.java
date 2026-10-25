/*
 * Decompiled with CFR 0.152.
 */
package org.bridj.cpp;

import java.util.Map;
import org.bridj.BridJ;
import org.bridj.Pointer;
import org.bridj.StructObject;
import org.bridj.ann.Runtime;
import org.bridj.cpp.CPPRuntime;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
@Runtime(value=CPPRuntime.class)
public abstract class CPPObject
extends StructObject {
    Map<Class<?>, Object[]> templateParameters;

    public CPPObject() {
    }

    public CPPObject(Pointer<? extends CPPObject> pointer, Object ... objectArray) {
        super((Pointer<? extends StructObject>)pointer, objectArray);
    }

    public CPPObject(Void void_, int n2, Object ... objectArray) {
        super(void_, n2, objectArray);
    }

    public void finalize() throws Throwable {
        BridJ.setJavaObjectFromNativePeer(this.peer.getPeer(), null);
    }
}


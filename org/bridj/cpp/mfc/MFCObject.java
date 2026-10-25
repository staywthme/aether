/*
 * Decompiled with CFR 0.152.
 */
package org.bridj.cpp.mfc;

import org.bridj.Pointer;
import org.bridj.ann.Library;
import org.bridj.ann.Runtime;
import org.bridj.cpp.CPPObject;
import org.bridj.cpp.mfc.MFCRuntime;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
@Library(value="mfc90u.dll", versionPattern="mfc(?:(\\d)(\\d))?u")
@Runtime(value=MFCRuntime.class)
public class MFCObject
extends CPPObject {
    protected MFCObject() {
    }

    protected MFCObject(Pointer<? extends MFCObject> pointer) {
        super((Pointer<? extends CPPObject>)pointer, new Object[0]);
    }

    protected MFCObject(Void void_, int n2, Object ... objectArray) {
        super(void_, n2, objectArray);
    }
}


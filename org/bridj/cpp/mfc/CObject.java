/*
 * Decompiled with CFR 0.152.
 */
package org.bridj.cpp.mfc;

import org.bridj.Pointer;
import org.bridj.ann.Convention;
import org.bridj.ann.Virtual;
import org.bridj.cpp.mfc.CRuntimeClass;
import org.bridj.cpp.mfc.MFCObject;
import org.bridj.cpp.mfc.MFCRuntime;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
@Convention(value=Convention.Style.StdCall)
public class CObject
extends MFCObject {
    public CObject() {
    }

    public CObject(Pointer<? extends CObject> pointer, MFCRuntime mFCRuntime) {
        super(pointer);
    }

    @Virtual
    public native Pointer<CRuntimeClass> GetRuntimeClass();

    @Virtual
    public native boolean IsKindOf(Pointer<CRuntimeClass> var1);
}


/*
 * Decompiled with CFR 0.152.
 */
package org.bridj.cpp.mfc;

import org.bridj.Pointer;
import org.bridj.ann.Virtual;
import org.bridj.cpp.mfc.CString;
import org.bridj.cpp.mfc.MFCObject;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class CWnd
extends MFCObject {
    @Virtual
    public native int SendMessage(int var1, int var2, int var3);

    @Virtual
    public native void SetWindowText(Pointer<CString> var1);
}


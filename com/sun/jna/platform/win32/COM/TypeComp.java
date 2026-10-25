/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32.COM;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.COM.Unknown;
import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;

public class TypeComp
extends Unknown {
    public TypeComp() {
    }

    public TypeComp(Pointer pointer) {
        super(pointer);
    }

    public WinNT.HRESULT Bind(WString wString, WinDef.ULONG uLONG, WinDef.WORD wORD, PointerByReference pointerByReference, OaIdl.DESCKIND.ByReference byReference, OaIdl.BINDPTR.ByReference byReference2) {
        return (WinNT.HRESULT)this._invokeNativeObject(3, new Object[]{this.getPointer(), wString, uLONG, wORD, pointerByReference, byReference, byReference2}, WinNT.HRESULT.class);
    }

    public WinNT.HRESULT BindType(WString wString, WinDef.ULONG uLONG, PointerByReference pointerByReference, PointerByReference pointerByReference2) {
        return (WinNT.HRESULT)this._invokeNativeObject(4, new Object[]{this.getPointer(), wString, uLONG, pointerByReference, pointerByReference2}, WinNT.HRESULT.class);
    }

    public static class ByReference
    extends TypeComp
    implements Structure.ByReference {
    }
}


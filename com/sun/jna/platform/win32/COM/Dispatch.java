/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32.COM;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.COM.IDispatch;
import com.sun.jna.platform.win32.COM.Unknown;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.platform.win32.OleAuto;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

public class Dispatch
extends Unknown
implements IDispatch {
    public Dispatch() {
    }

    public Dispatch(Pointer pointer) {
        super(pointer);
    }

    @Override
    public WinNT.HRESULT GetTypeInfoCount(WinDef.UINTByReference uINTByReference) {
        return (WinNT.HRESULT)this._invokeNativeObject(3, new Object[]{this.getPointer(), uINTByReference}, WinNT.HRESULT.class);
    }

    @Override
    public WinNT.HRESULT GetTypeInfo(WinDef.UINT uINT, WinDef.LCID lCID, PointerByReference pointerByReference) {
        return (WinNT.HRESULT)this._invokeNativeObject(4, new Object[]{this.getPointer(), uINT, lCID, pointerByReference}, WinNT.HRESULT.class);
    }

    @Override
    public WinNT.HRESULT GetIDsOfNames(Guid.REFIID rEFIID, WString[] wStringArray, int n2, WinDef.LCID lCID, OaIdl.DISPIDByReference dISPIDByReference) {
        return (WinNT.HRESULT)this._invokeNativeObject(5, new Object[]{this.getPointer(), rEFIID, wStringArray, n2, lCID, dISPIDByReference}, WinNT.HRESULT.class);
    }

    @Override
    public WinNT.HRESULT Invoke(OaIdl.DISPID dISPID, Guid.REFIID rEFIID, WinDef.LCID lCID, WinDef.WORD wORD, OleAuto.DISPPARAMS.ByReference byReference, Variant.VARIANT.ByReference byReference2, OaIdl.EXCEPINFO.ByReference byReference3, IntByReference intByReference) {
        return (WinNT.HRESULT)this._invokeNativeObject(6, new Object[]{this.getPointer(), dISPID, rEFIID, lCID, wORD, byReference, byReference2, byReference3, intByReference}, WinNT.HRESULT.class);
    }

    public static class ByReference
    extends Dispatch
    implements Structure.ByReference {
    }
}


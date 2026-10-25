/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32.COM;

import com.sun.jna.WString;
import com.sun.jna.platform.win32.COM.COMBindingBaseObject;
import com.sun.jna.platform.win32.COM.IDispatch;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.platform.win32.OleAuto;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

public class COMEarlyBindingObject
extends COMBindingBaseObject
implements IDispatch {
    public COMEarlyBindingObject(Guid.CLSID cLSID, boolean bl, int n2) {
        super(cLSID, bl, n2);
    }

    protected String getStringProperty(OaIdl.DISPID dISPID) {
        Variant.VARIANT.ByReference byReference = new Variant.VARIANT.ByReference();
        this.oleMethod(2, byReference, dISPID);
        return byReference.getValue().toString();
    }

    protected void setProperty(OaIdl.DISPID dISPID, boolean bl) {
        this.oleMethod(4, null, dISPID, new Variant.VARIANT(bl));
    }

    @Override
    public WinNT.HRESULT QueryInterface(Guid.REFIID rEFIID, PointerByReference pointerByReference) {
        return this.getIDispatch().QueryInterface(rEFIID, pointerByReference);
    }

    @Override
    public int AddRef() {
        return this.getIDispatch().AddRef();
    }

    @Override
    public int Release() {
        return this.getIDispatch().Release();
    }

    @Override
    public WinNT.HRESULT GetTypeInfoCount(WinDef.UINTByReference uINTByReference) {
        return this.getIDispatch().GetTypeInfoCount(uINTByReference);
    }

    @Override
    public WinNT.HRESULT GetTypeInfo(WinDef.UINT uINT, WinDef.LCID lCID, PointerByReference pointerByReference) {
        return this.getIDispatch().GetTypeInfo(uINT, lCID, pointerByReference);
    }

    @Override
    public WinNT.HRESULT GetIDsOfNames(Guid.REFIID rEFIID, WString[] wStringArray, int n2, WinDef.LCID lCID, OaIdl.DISPIDByReference dISPIDByReference) {
        return this.getIDispatch().GetIDsOfNames(rEFIID, wStringArray, n2, lCID, dISPIDByReference);
    }

    @Override
    public WinNT.HRESULT Invoke(OaIdl.DISPID dISPID, Guid.REFIID rEFIID, WinDef.LCID lCID, WinDef.WORD wORD, OleAuto.DISPPARAMS.ByReference byReference, Variant.VARIANT.ByReference byReference2, OaIdl.EXCEPINFO.ByReference byReference3, IntByReference intByReference) {
        return this.getIDispatch().Invoke(dISPID, rEFIID, lCID, wORD, byReference, byReference2, byReference3, intByReference);
    }
}


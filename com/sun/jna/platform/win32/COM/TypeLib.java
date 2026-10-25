/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32.COM;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.COM.ITypeLib;
import com.sun.jna.platform.win32.COM.Unknown;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.platform.win32.WTypes;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;

public class TypeLib
extends Unknown
implements ITypeLib {
    public TypeLib() {
    }

    public TypeLib(Pointer pointer) {
        super(pointer);
    }

    @Override
    public WinDef.UINT GetTypeInfoCount() {
        return (WinDef.UINT)this._invokeNativeObject(3, new Object[]{this.getPointer()}, WinDef.UINT.class);
    }

    @Override
    public WinNT.HRESULT GetTypeInfo(WinDef.UINT uINT, PointerByReference pointerByReference) {
        return (WinNT.HRESULT)this._invokeNativeObject(4, new Object[]{this.getPointer(), uINT, pointerByReference}, WinNT.HRESULT.class);
    }

    @Override
    public WinNT.HRESULT GetTypeInfoType(WinDef.UINT uINT, OaIdl.TYPEKIND.ByReference byReference) {
        return (WinNT.HRESULT)this._invokeNativeObject(5, new Object[]{this.getPointer(), uINT, byReference}, WinNT.HRESULT.class);
    }

    @Override
    public WinNT.HRESULT GetTypeInfoOfGuid(Guid.GUID gUID, PointerByReference pointerByReference) {
        return (WinNT.HRESULT)this._invokeNativeObject(6, new Object[]{this.getPointer(), gUID, pointerByReference}, WinNT.HRESULT.class);
    }

    @Override
    public WinNT.HRESULT GetLibAttr(PointerByReference pointerByReference) {
        return (WinNT.HRESULT)this._invokeNativeObject(7, new Object[]{this.getPointer(), pointerByReference}, WinNT.HRESULT.class);
    }

    @Override
    public WinNT.HRESULT GetTypeComp(PointerByReference pointerByReference) {
        return (WinNT.HRESULT)this._invokeNativeObject(8, new Object[]{this.getPointer(), pointerByReference}, WinNT.HRESULT.class);
    }

    @Override
    public WinNT.HRESULT GetDocumentation(int n2, WTypes.BSTRByReference bSTRByReference, WTypes.BSTRByReference bSTRByReference2, WinDef.DWORDByReference dWORDByReference, WTypes.BSTRByReference bSTRByReference3) {
        return (WinNT.HRESULT)this._invokeNativeObject(9, new Object[]{this.getPointer(), n2, bSTRByReference, bSTRByReference2, dWORDByReference, bSTRByReference3}, WinNT.HRESULT.class);
    }

    @Override
    public WinNT.HRESULT IsName(WTypes.LPOLESTR lPOLESTR, WinDef.ULONG uLONG, WinDef.BOOLByReference bOOLByReference) {
        return (WinNT.HRESULT)this._invokeNativeObject(10, new Object[]{this.getPointer(), lPOLESTR, uLONG, bOOLByReference}, WinNT.HRESULT.class);
    }

    @Override
    public WinNT.HRESULT FindName(WTypes.LPOLESTR lPOLESTR, WinDef.ULONG uLONG, Pointer[] pointerArray, OaIdl.MEMBERID[] mEMBERIDArray, WinDef.USHORTByReference uSHORTByReference) {
        return (WinNT.HRESULT)this._invokeNativeObject(11, new Object[]{this.getPointer(), lPOLESTR, uLONG, pointerArray, mEMBERIDArray, uSHORTByReference}, WinNT.HRESULT.class);
    }

    @Override
    public void ReleaseTLibAttr(OaIdl.TLIBATTR tLIBATTR) {
        this._invokeNativeObject(12, new Object[]{this.getPointer(), tLIBATTR.getPointer()}, WinNT.HRESULT.class);
    }

    public static class ByReference
    extends TypeLib
    implements Structure.ByReference {
    }
}


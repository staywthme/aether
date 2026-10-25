/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32.COM;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.COM.ITypeInfo;
import com.sun.jna.platform.win32.COM.IUnknown;
import com.sun.jna.platform.win32.COM.Unknown;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.platform.win32.OleAuto;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.platform.win32.WTypes;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

public class TypeInfo
extends Unknown
implements ITypeInfo {
    public TypeInfo() {
    }

    public TypeInfo(Pointer pointer) {
        super(pointer);
    }

    @Override
    public WinNT.HRESULT GetTypeAttr(PointerByReference pointerByReference) {
        return (WinNT.HRESULT)this._invokeNativeObject(3, new Object[]{this.getPointer(), pointerByReference}, WinNT.HRESULT.class);
    }

    @Override
    public WinNT.HRESULT GetTypeComp(PointerByReference pointerByReference) {
        return (WinNT.HRESULT)this._invokeNativeObject(4, new Object[]{this.getPointer(), pointerByReference}, WinNT.HRESULT.class);
    }

    @Override
    public WinNT.HRESULT GetFuncDesc(WinDef.UINT uINT, PointerByReference pointerByReference) {
        return (WinNT.HRESULT)this._invokeNativeObject(5, new Object[]{this.getPointer(), uINT, pointerByReference}, WinNT.HRESULT.class);
    }

    @Override
    public WinNT.HRESULT GetVarDesc(WinDef.UINT uINT, PointerByReference pointerByReference) {
        return (WinNT.HRESULT)this._invokeNativeObject(6, new Object[]{this.getPointer(), uINT, pointerByReference}, WinNT.HRESULT.class);
    }

    @Override
    public WinNT.HRESULT GetNames(OaIdl.MEMBERID mEMBERID, WTypes.BSTR[] bSTRArray, WinDef.UINT uINT, WinDef.UINTByReference uINTByReference) {
        return (WinNT.HRESULT)this._invokeNativeObject(7, new Object[]{this.getPointer(), mEMBERID, bSTRArray, uINT, uINTByReference}, WinNT.HRESULT.class);
    }

    @Override
    public WinNT.HRESULT GetRefTypeOfImplType(WinDef.UINT uINT, OaIdl.HREFTYPEByReference hREFTYPEByReference) {
        return (WinNT.HRESULT)this._invokeNativeObject(8, new Object[]{this.getPointer(), uINT, hREFTYPEByReference}, WinNT.HRESULT.class);
    }

    @Override
    public WinNT.HRESULT GetImplTypeFlags(WinDef.UINT uINT, IntByReference intByReference) {
        return (WinNT.HRESULT)this._invokeNativeObject(9, new Object[]{this.getPointer(), uINT, intByReference}, WinNT.HRESULT.class);
    }

    @Override
    public WinNT.HRESULT GetIDsOfNames(WTypes.LPOLESTR[] lPOLESTRArray, WinDef.UINT uINT, OaIdl.MEMBERID[] mEMBERIDArray) {
        return (WinNT.HRESULT)this._invokeNativeObject(10, new Object[]{this.getPointer(), lPOLESTRArray, uINT, mEMBERIDArray}, WinNT.HRESULT.class);
    }

    @Override
    public WinNT.HRESULT Invoke(WinDef.PVOID pVOID, OaIdl.MEMBERID mEMBERID, WinDef.WORD wORD, OleAuto.DISPPARAMS.ByReference byReference, Variant.VARIANT.ByReference byReference2, OaIdl.EXCEPINFO.ByReference byReference3, WinDef.UINTByReference uINTByReference) {
        return (WinNT.HRESULT)this._invokeNativeObject(11, new Object[]{this.getPointer(), pVOID, mEMBERID, wORD, byReference, byReference2, byReference3, uINTByReference}, WinNT.HRESULT.class);
    }

    @Override
    public WinNT.HRESULT GetDocumentation(OaIdl.MEMBERID mEMBERID, WTypes.BSTRByReference bSTRByReference, WTypes.BSTRByReference bSTRByReference2, WinDef.DWORDByReference dWORDByReference, WTypes.BSTRByReference bSTRByReference3) {
        return (WinNT.HRESULT)this._invokeNativeObject(12, new Object[]{this.getPointer(), mEMBERID, bSTRByReference, bSTRByReference2, dWORDByReference, bSTRByReference3}, WinNT.HRESULT.class);
    }

    @Override
    public WinNT.HRESULT GetDllEntry(OaIdl.MEMBERID mEMBERID, OaIdl.INVOKEKIND iNVOKEKIND, WTypes.BSTRByReference bSTRByReference, WTypes.BSTRByReference bSTRByReference2, WinDef.WORDByReference wORDByReference) {
        return (WinNT.HRESULT)this._invokeNativeObject(13, new Object[]{this.getPointer(), mEMBERID, iNVOKEKIND, bSTRByReference, bSTRByReference2, wORDByReference}, WinNT.HRESULT.class);
    }

    @Override
    public WinNT.HRESULT GetRefTypeInfo(OaIdl.HREFTYPE hREFTYPE, PointerByReference pointerByReference) {
        return (WinNT.HRESULT)this._invokeNativeObject(14, new Object[]{this.getPointer(), hREFTYPE, pointerByReference}, WinNT.HRESULT.class);
    }

    @Override
    public WinNT.HRESULT AddressOfMember(OaIdl.MEMBERID mEMBERID, OaIdl.INVOKEKIND iNVOKEKIND, PointerByReference pointerByReference) {
        return (WinNT.HRESULT)this._invokeNativeObject(15, new Object[]{this.getPointer(), mEMBERID, iNVOKEKIND, pointerByReference}, WinNT.HRESULT.class);
    }

    @Override
    public WinNT.HRESULT CreateInstance(IUnknown iUnknown, Guid.REFIID rEFIID, PointerByReference pointerByReference) {
        return (WinNT.HRESULT)this._invokeNativeObject(16, new Object[]{this.getPointer(), iUnknown, rEFIID, pointerByReference}, WinNT.HRESULT.class);
    }

    @Override
    public WinNT.HRESULT GetMops(OaIdl.MEMBERID mEMBERID, WTypes.BSTRByReference bSTRByReference) {
        return (WinNT.HRESULT)this._invokeNativeObject(17, new Object[]{this.getPointer(), mEMBERID, bSTRByReference}, WinNT.HRESULT.class);
    }

    @Override
    public WinNT.HRESULT GetContainingTypeLib(PointerByReference pointerByReference, WinDef.UINTByReference uINTByReference) {
        return (WinNT.HRESULT)this._invokeNativeObject(18, new Object[]{this.getPointer(), pointerByReference, uINTByReference}, WinNT.HRESULT.class);
    }

    @Override
    public void ReleaseTypeAttr(OaIdl.TYPEATTR tYPEATTR) {
        this._invokeNativeVoid(19, new Object[]{this.getPointer(), tYPEATTR});
    }

    @Override
    public void ReleaseFuncDesc(OaIdl.FUNCDESC fUNCDESC) {
        this._invokeNativeVoid(20, new Object[]{this.getPointer(), fUNCDESC});
    }

    @Override
    public void ReleaseVarDesc(OaIdl.VARDESC vARDESC) {
        this._invokeNativeVoid(21, new Object[]{this.getPointer(), vARDESC});
    }

    public static class ByReference
    extends TypeInfo
    implements Structure.ByReference {
    }
}


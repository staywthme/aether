/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32.COM;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.COM.IRecordInfo;
import com.sun.jna.platform.win32.COM.ITypeInfo;
import com.sun.jna.platform.win32.COM.Unknown;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.platform.win32.WTypes;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;

public class RecordInfo
extends Unknown
implements IRecordInfo {
    public RecordInfo() {
    }

    public RecordInfo(Pointer pointer) {
        super(pointer);
    }

    @Override
    public WinNT.HRESULT RecordInit(WinDef.PVOID pVOID) {
        return null;
    }

    @Override
    public WinNT.HRESULT RecordClear(WinDef.PVOID pVOID) {
        return null;
    }

    @Override
    public WinNT.HRESULT RecordCopy(WinDef.PVOID pVOID, WinDef.PVOID pVOID2) {
        return null;
    }

    @Override
    public WinNT.HRESULT GetGuid(Guid.GUID gUID) {
        return null;
    }

    @Override
    public WinNT.HRESULT GetName(WTypes.BSTR bSTR) {
        return null;
    }

    @Override
    public WinNT.HRESULT GetSize(WinDef.ULONG uLONG) {
        return null;
    }

    @Override
    public WinNT.HRESULT GetTypeInfo(ITypeInfo iTypeInfo) {
        return null;
    }

    @Override
    public WinNT.HRESULT GetField(WinDef.PVOID pVOID, WString wString, Variant.VARIANT vARIANT) {
        return null;
    }

    @Override
    public WinNT.HRESULT GetFieldNoCopy(WinDef.PVOID pVOID, WString wString, Variant.VARIANT vARIANT, WinDef.PVOID pVOID2) {
        return null;
    }

    @Override
    public WinNT.HRESULT PutField(WinDef.ULONG uLONG, WinDef.PVOID pVOID, WString wString, Variant.VARIANT vARIANT) {
        return null;
    }

    @Override
    public WinNT.HRESULT PutFieldNoCopy(WinDef.ULONG uLONG, WinDef.PVOID pVOID, WString wString, Variant.VARIANT vARIANT) {
        return null;
    }

    @Override
    public WinNT.HRESULT GetFieldNames(WinDef.ULONG uLONG, WTypes.BSTR bSTR) {
        return null;
    }

    @Override
    public WinDef.BOOL IsMatchingType(IRecordInfo iRecordInfo) {
        return null;
    }

    @Override
    public WinDef.PVOID RecordCreate() {
        return null;
    }

    @Override
    public WinNT.HRESULT RecordCreateCopy(WinDef.PVOID pVOID, WinDef.PVOID pVOID2) {
        return null;
    }

    @Override
    public WinNT.HRESULT RecordDestroy(WinDef.PVOID pVOID) {
        return null;
    }

    public static class ByReference
    extends RecordInfo
    implements Structure.ByReference {
    }
}


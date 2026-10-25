/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32.COM;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.COM.IRunningObjectTable;
import com.sun.jna.platform.win32.COM.Unknown;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;

public class RunningObjectTable
extends Unknown
implements IRunningObjectTable {
    public RunningObjectTable() {
    }

    public RunningObjectTable(Pointer pointer) {
        super(pointer);
    }

    @Override
    public WinNT.HRESULT Register(WinDef.DWORD integerType, Pointer pointer, Pointer pointer2, WinDef.DWORDByReference dWORDByReference) {
        integerType = (WinNT.HRESULT)this._invokeNativeObject(3, new Object[]{this.getPointer(), integerType, pointer, pointer2, dWORDByReference}, WinNT.HRESULT.class);
        return integerType;
    }

    @Override
    public WinNT.HRESULT Revoke(WinDef.DWORD integerType) {
        integerType = (WinNT.HRESULT)this._invokeNativeObject(4, new Object[]{this.getPointer(), integerType}, WinNT.HRESULT.class);
        return integerType;
    }

    @Override
    public WinNT.HRESULT IsRunning(Pointer object) {
        object = (WinNT.HRESULT)this._invokeNativeObject(5, new Object[]{this.getPointer(), object}, WinNT.HRESULT.class);
        return object;
    }

    @Override
    public WinNT.HRESULT GetObject(Pointer object, PointerByReference pointerByReference) {
        object = (WinNT.HRESULT)this._invokeNativeObject(6, new Object[]{this.getPointer(), object, pointerByReference}, WinNT.HRESULT.class);
        return object;
    }

    @Override
    public WinNT.HRESULT NoteChangeTime(WinDef.DWORD integerType, WinBase.FILETIME fILETIME) {
        integerType = (WinNT.HRESULT)this._invokeNativeObject(7, new Object[]{this.getPointer(), integerType, fILETIME}, WinNT.HRESULT.class);
        return integerType;
    }

    @Override
    public WinNT.HRESULT GetTimeOfLastChange(Pointer object, WinBase.FILETIME.ByReference byReference) {
        object = (WinNT.HRESULT)this._invokeNativeObject(8, new Object[]{this.getPointer(), object, byReference}, WinNT.HRESULT.class);
        return object;
    }

    @Override
    public WinNT.HRESULT EnumRunning(PointerByReference nativeMapped) {
        nativeMapped = (WinNT.HRESULT)this._invokeNativeObject(9, new Object[]{this.getPointer(), nativeMapped}, WinNT.HRESULT.class);
        return nativeMapped;
    }

    public static class ByReference
    extends RunningObjectTable
    implements Structure.ByReference {
    }
}


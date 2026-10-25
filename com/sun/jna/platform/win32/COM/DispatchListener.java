/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32.COM;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.COM.DispatchVTable;
import com.sun.jna.platform.win32.COM.IDispatchCallback;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.platform.win32.OleAuto;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

@Structure.FieldOrder(value={"vtbl"})
public class DispatchListener
extends Structure {
    public DispatchVTable.ByReference vtbl = this.constructVTable();

    public DispatchListener(IDispatchCallback iDispatchCallback) {
        this.initVTable(iDispatchCallback);
    }

    protected DispatchVTable.ByReference constructVTable() {
        return new DispatchVTable.ByReference();
    }

    protected void initVTable(final IDispatchCallback iDispatchCallback) {
        this.vtbl.QueryInterfaceCallback = new DispatchVTable.QueryInterfaceCallback(){

            @Override
            public WinNT.HRESULT invoke(Pointer pointer, Guid.REFIID rEFIID, PointerByReference pointerByReference) {
                return iDispatchCallback.QueryInterface(rEFIID, pointerByReference);
            }
        };
        this.vtbl.AddRefCallback = new DispatchVTable.AddRefCallback(){

            @Override
            public int invoke(Pointer pointer) {
                return iDispatchCallback.AddRef();
            }
        };
        this.vtbl.ReleaseCallback = new DispatchVTable.ReleaseCallback(){

            @Override
            public int invoke(Pointer pointer) {
                return iDispatchCallback.Release();
            }
        };
        this.vtbl.GetTypeInfoCountCallback = new DispatchVTable.GetTypeInfoCountCallback(){

            @Override
            public WinNT.HRESULT invoke(Pointer pointer, WinDef.UINTByReference uINTByReference) {
                return iDispatchCallback.GetTypeInfoCount(uINTByReference);
            }
        };
        this.vtbl.GetTypeInfoCallback = new DispatchVTable.GetTypeInfoCallback(){

            @Override
            public WinNT.HRESULT invoke(Pointer pointer, WinDef.UINT uINT, WinDef.LCID lCID, PointerByReference pointerByReference) {
                return iDispatchCallback.GetTypeInfo(uINT, lCID, pointerByReference);
            }
        };
        this.vtbl.GetIDsOfNamesCallback = new DispatchVTable.GetIDsOfNamesCallback(){

            @Override
            public WinNT.HRESULT invoke(Pointer pointer, Guid.REFIID rEFIID, WString[] wStringArray, int n2, WinDef.LCID lCID, OaIdl.DISPIDByReference dISPIDByReference) {
                return iDispatchCallback.GetIDsOfNames(rEFIID, wStringArray, n2, lCID, dISPIDByReference);
            }
        };
        this.vtbl.InvokeCallback = new DispatchVTable.InvokeCallback(){

            @Override
            public WinNT.HRESULT invoke(Pointer pointer, OaIdl.DISPID dISPID, Guid.REFIID rEFIID, WinDef.LCID lCID, WinDef.WORD wORD, OleAuto.DISPPARAMS.ByReference byReference, Variant.VARIANT.ByReference byReference2, OaIdl.EXCEPINFO.ByReference byReference3, IntByReference intByReference) {
                return iDispatchCallback.Invoke(dISPID, rEFIID, lCID, wORD, byReference, byReference2, byReference3, intByReference);
            }
        };
    }
}


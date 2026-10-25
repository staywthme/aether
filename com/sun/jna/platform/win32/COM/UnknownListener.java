/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32.COM;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.COM.IUnknownCallback;
import com.sun.jna.platform.win32.COM.UnknownVTable;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;

@Structure.FieldOrder(value={"vtbl"})
public class UnknownListener
extends Structure {
    public UnknownVTable.ByReference vtbl = this.constructVTable();

    public UnknownListener(IUnknownCallback iUnknownCallback) {
        this.initVTable(iUnknownCallback);
    }

    protected UnknownVTable.ByReference constructVTable() {
        return new UnknownVTable.ByReference();
    }

    protected void initVTable(final IUnknownCallback iUnknownCallback) {
        this.vtbl.QueryInterfaceCallback = new UnknownVTable.QueryInterfaceCallback(){

            @Override
            public WinNT.HRESULT invoke(Pointer pointer, Guid.REFIID rEFIID, PointerByReference pointerByReference) {
                return iUnknownCallback.QueryInterface(rEFIID, pointerByReference);
            }
        };
        this.vtbl.AddRefCallback = new UnknownVTable.AddRefCallback(){

            @Override
            public int invoke(Pointer pointer) {
                return iUnknownCallback.AddRef();
            }
        };
        this.vtbl.ReleaseCallback = new UnknownVTable.ReleaseCallback(){

            @Override
            public int invoke(Pointer pointer) {
                return iUnknownCallback.Release();
            }
        };
    }
}


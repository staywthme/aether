/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32.COM;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.COM.IConnectionPoint;
import com.sun.jna.platform.win32.COM.IUnknownCallback;
import com.sun.jna.platform.win32.COM.Unknown;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;

public class ConnectionPoint
extends Unknown
implements IConnectionPoint {
    public ConnectionPoint(Pointer pointer) {
        super(pointer);
    }

    @Override
    public WinNT.HRESULT GetConnectionInterface(Guid.IID iID) {
        return (WinNT.HRESULT)this._invokeNativeObject(3, new Object[]{this.getPointer(), iID}, WinNT.HRESULT.class);
    }

    void GetConnectionPointContainer() {
    }

    @Override
    public WinNT.HRESULT Advise(IUnknownCallback iUnknownCallback, WinDef.DWORDByReference dWORDByReference) {
        return (WinNT.HRESULT)this._invokeNativeObject(5, new Object[]{this.getPointer(), iUnknownCallback.getPointer(), dWORDByReference}, WinNT.HRESULT.class);
    }

    @Override
    public WinNT.HRESULT Unadvise(WinDef.DWORD dWORD) {
        return (WinNT.HRESULT)this._invokeNativeObject(6, new Object[]{this.getPointer(), dWORD}, WinNT.HRESULT.class);
    }

    void EnumConnections() {
    }
}


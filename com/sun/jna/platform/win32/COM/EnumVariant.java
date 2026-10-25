/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32.COM;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.COM.COMUtils;
import com.sun.jna.platform.win32.COM.IEnumVariant;
import com.sun.jna.platform.win32.COM.Unknown;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

public class EnumVariant
extends Unknown
implements IEnumVariant {
    public static final Guid.IID IID = new Guid.IID("{00020404-0000-0000-C000-000000000046}");
    public static final Guid.REFIID REFIID = new Guid.REFIID(IID);

    public EnumVariant() {
    }

    public EnumVariant(Pointer pointer) {
        this.setPointer(pointer);
    }

    @Override
    public Variant.VARIANT[] Next(int n2) {
        Variant.VARIANT[] vARIANTArray = new Variant.VARIANT[n2];
        IntByReference intByReference = new IntByReference();
        Variant.VARIANT[] vARIANTArray2 = (Variant.VARIANT[])this._invokeNativeObject(3, new Object[]{this.getPointer(), vARIANTArray.length, vARIANTArray, intByReference}, WinNT.HRESULT.class);
        COMUtils.checkRC((WinNT.HRESULT)vARIANTArray2);
        vARIANTArray2 = new Variant.VARIANT[intByReference.getValue()];
        System.arraycopy(vARIANTArray, 0, vARIANTArray2, 0, intByReference.getValue());
        return vARIANTArray2;
    }

    @Override
    public void Skip(int n2) {
        WinNT.HRESULT hRESULT = (WinNT.HRESULT)this._invokeNativeObject(4, new Object[]{this.getPointer(), n2}, WinNT.HRESULT.class);
        COMUtils.checkRC(hRESULT);
    }

    @Override
    public void Reset() {
        WinNT.HRESULT hRESULT = (WinNT.HRESULT)this._invokeNativeObject(5, new Object[]{this.getPointer()}, WinNT.HRESULT.class);
        COMUtils.checkRC(hRESULT);
    }

    @Override
    public EnumVariant Clone() {
        PointerByReference pointerByReference = new PointerByReference();
        WinNT.HRESULT hRESULT = (WinNT.HRESULT)this._invokeNativeObject(6, new Object[]{this.getPointer(), pointerByReference}, WinNT.HRESULT.class);
        COMUtils.checkRC(hRESULT);
        return new EnumVariant(pointerByReference.getValue());
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32.COM;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.COM.COMUtils;
import com.sun.jna.platform.win32.COM.IMoniker;
import com.sun.jna.platform.win32.COM.IStream;
import com.sun.jna.platform.win32.COM.Unknown;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.Ole32;
import com.sun.jna.platform.win32.WTypes;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;

public class Moniker
extends Unknown
implements IMoniker {
    static final int vTableIdStart = 7;

    public Moniker() {
    }

    public Moniker(Pointer pointer) {
        super(pointer);
    }

    @Override
    public void BindToObject() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void BindToStorage() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void Reduce() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void ComposeWith() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void Enum() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void IsEqual() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void Hash() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void IsRunning() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void GetTimeOfLastChange() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void Inverse() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void CommonPrefixWith() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void RelativePathTo() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String GetDisplayName(Pointer object, Pointer object2) {
        PointerByReference pointerByReference = new PointerByReference();
        object = (WinNT.HRESULT)this._invokeNativeObject(20, new Object[]{this.getPointer(), object, object2, pointerByReference}, WinNT.HRESULT.class);
        COMUtils.checkRC((WinNT.HRESULT)object);
        object = pointerByReference.getValue();
        if (object == null) {
            return null;
        }
        object2 = new WTypes.LPOLESTR((Pointer)object);
        object2 = ((WTypes.LPOLESTR)object2).getValue();
        Ole32.INSTANCE.CoTaskMemFree((Pointer)object);
        return object2;
    }

    @Override
    public void ParseDisplayName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void IsSystemMoniker() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean IsDirty() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void Load(IStream iStream) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void Save(IStream iStream) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void GetSizeMax() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Guid.CLSID GetClassID() {
        throw new UnsupportedOperationException();
    }

    public static class ByReference
    extends Moniker
    implements Structure.ByReference {
    }
}


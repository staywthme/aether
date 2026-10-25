/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32.COM.util;

import com.sun.jna.NativeMapped;
import com.sun.jna.PointerType;
import com.sun.jna.platform.win32.COM.COMUtils;
import com.sun.jna.platform.win32.COM.Dispatch;
import com.sun.jna.platform.win32.COM.IDispatch;
import com.sun.jna.platform.win32.COM.IEnumMoniker;
import com.sun.jna.platform.win32.COM.IRunningObjectTable;
import com.sun.jna.platform.win32.COM.Moniker;
import com.sun.jna.platform.win32.COM.Unknown;
import com.sun.jna.platform.win32.COM.util.ObjectFactory;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;
import java.util.Iterator;

public class EnumMoniker
implements Iterable<com.sun.jna.platform.win32.COM.util.IDispatch> {
    ObjectFactory factory;
    IRunningObjectTable rawRot;
    IEnumMoniker raw;
    Moniker rawNext;

    protected EnumMoniker(IEnumMoniker object, IRunningObjectTable iRunningObjectTable, ObjectFactory objectFactory) {
        assert (COMUtils.comIsInitialized()) : "COM not initialized";
        this.rawRot = iRunningObjectTable;
        this.raw = object;
        this.factory = objectFactory;
        object = object.Reset();
        COMUtils.checkRC((WinNT.HRESULT)object);
        this.cacheNext();
    }

    protected void cacheNext() {
        assert (COMUtils.comIsInitialized()) : "COM not initialized";
        PointerByReference pointerByReference = new PointerByReference();
        WinDef.ULONGByReference uLONGByReference = new WinDef.ULONGByReference();
        WinNT.HRESULT hRESULT = this.raw.Next(new WinDef.ULONG(1L), pointerByReference, uLONGByReference);
        if (WinNT.S_OK.equals(hRESULT) && uLONGByReference.getValue().intValue() > 0) {
            this.rawNext = new Moniker(pointerByReference.getValue());
            return;
        }
        if (!WinNT.S_FALSE.equals(hRESULT)) {
            COMUtils.checkRC(hRESULT);
        }
        this.rawNext = null;
    }

    @Override
    public Iterator<com.sun.jna.platform.win32.COM.util.IDispatch> iterator() {
        return new Iterator<com.sun.jna.platform.win32.COM.util.IDispatch>(){

            @Override
            public boolean hasNext() {
                return null != EnumMoniker.this.rawNext;
            }

            @Override
            public com.sun.jna.platform.win32.COM.util.IDispatch next() {
                assert (COMUtils.comIsInitialized()) : "COM not initialized";
                NativeMapped nativeMapped = EnumMoniker.this.rawNext;
                Object object = new PointerByReference();
                nativeMapped = EnumMoniker.this.rawRot.GetObject(((PointerType)nativeMapped).getPointer(), (PointerByReference)object);
                COMUtils.checkRC((WinNT.HRESULT)nativeMapped);
                nativeMapped = new Dispatch(((PointerByReference)object).getValue());
                EnumMoniker.this.cacheNext();
                object = EnumMoniker.this.factory.createProxy(com.sun.jna.platform.win32.COM.util.IDispatch.class, (IDispatch)((Object)nativeMapped));
                ((Unknown)nativeMapped).Release();
                return object;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("remove");
            }
        };
    }
}


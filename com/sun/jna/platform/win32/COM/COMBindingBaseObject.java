/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32.COM;

import com.sun.jna.WString;
import com.sun.jna.platform.win32.COM.COMException;
import com.sun.jna.platform.win32.COM.COMInvoker;
import com.sun.jna.platform.win32.COM.COMUtils;
import com.sun.jna.platform.win32.COM.Dispatch;
import com.sun.jna.platform.win32.COM.IDispatch;
import com.sun.jna.platform.win32.COM.IUnknown;
import com.sun.jna.platform.win32.COM.Unknown;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.platform.win32.Ole32;
import com.sun.jna.platform.win32.OleAuto;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

public class COMBindingBaseObject
extends COMInvoker {
    public static final WinDef.LCID LOCALE_USER_DEFAULT = Kernel32.INSTANCE.GetUserDefaultLCID();
    public static final WinDef.LCID LOCALE_SYSTEM_DEFAULT = Kernel32.INSTANCE.GetSystemDefaultLCID();
    private IUnknown iUnknown;
    private IDispatch iDispatch;
    private PointerByReference pDispatch = new PointerByReference();
    private PointerByReference pUnknown = new PointerByReference();

    public COMBindingBaseObject(IDispatch iDispatch) {
        this.iDispatch = iDispatch;
    }

    public COMBindingBaseObject(Guid.CLSID cLSID, boolean bl) {
        this(cLSID, bl, 21);
    }

    public COMBindingBaseObject(Guid.CLSID cLSID, boolean bl, int n2) {
        assert (COMUtils.comIsInitialized()) : "COM not initialized";
        this.init(bl, cLSID, n2);
    }

    public COMBindingBaseObject(String object, boolean bl, int n2) throws COMException {
        assert (COMUtils.comIsInitialized()) : "COM not initialized";
        Guid.CLSID.ByReference byReference = new Guid.CLSID.ByReference();
        object = Ole32.INSTANCE.CLSIDFromProgID((String)object, byReference);
        COMUtils.checkRC((WinNT.HRESULT)object);
        this.init(bl, byReference, n2);
    }

    public COMBindingBaseObject(String string, boolean bl) throws COMException {
        this(string, bl, 21);
    }

    private void init(boolean bl, Guid.GUID gUID, int n2) throws COMException {
        WinNT.HRESULT hRESULT;
        if (bl) {
            hRESULT = OleAuto.INSTANCE.GetActiveObject(gUID, null, this.pUnknown);
            if (COMUtils.SUCCEEDED(hRESULT)) {
                this.iUnknown = new Unknown(this.pUnknown.getValue());
                hRESULT = this.iUnknown.QueryInterface(new Guid.REFIID(IDispatch.IID_IDISPATCH), this.pDispatch);
            } else {
                hRESULT = Ole32.INSTANCE.CoCreateInstance(gUID, null, n2, IDispatch.IID_IDISPATCH, this.pDispatch);
            }
        } else {
            hRESULT = Ole32.INSTANCE.CoCreateInstance(gUID, null, n2, IDispatch.IID_IDISPATCH, this.pDispatch);
        }
        COMUtils.checkRC(hRESULT);
        this.iDispatch = new Dispatch(this.pDispatch.getValue());
    }

    public IDispatch getIDispatch() {
        return this.iDispatch;
    }

    public PointerByReference getIDispatchPointer() {
        return this.pDispatch;
    }

    public IUnknown getIUnknown() {
        return this.iUnknown;
    }

    public PointerByReference getIUnknownPointer() {
        return this.pUnknown;
    }

    public void release() {
        if (this.iDispatch != null) {
            this.iDispatch.Release();
        }
    }

    protected WinNT.HRESULT oleMethod(int n2, Variant.VARIANT.ByReference byReference, String object, Variant.VARIANT[] vARIANTArray) throws COMException {
        object = new WString[]{new WString((String)object)};
        OaIdl.DISPIDByReference dISPIDByReference = new OaIdl.DISPIDByReference();
        object = this.iDispatch.GetIDsOfNames(new Guid.REFIID(Guid.IID_NULL), (WString[])object, 1, LOCALE_USER_DEFAULT, dISPIDByReference);
        COMUtils.checkRC((WinNT.HRESULT)object);
        return this.oleMethod(n2, byReference, dISPIDByReference.getValue(), vARIANTArray);
    }

    protected WinNT.HRESULT oleMethod(int n2, Variant.VARIANT.ByReference byReference, OaIdl.DISPID dISPID, Variant.VARIANT[] vARIANTArray) throws COMException {
        int n3;
        int n4 = 0;
        Variant.VARIANT[] vARIANTArray2 = null;
        OleAuto.DISPPARAMS.ByReference byReference2 = new OleAuto.DISPPARAMS.ByReference();
        OaIdl.EXCEPINFO.ByReference byReference3 = new OaIdl.EXCEPINFO.ByReference();
        IntByReference intByReference = new IntByReference();
        if (vARIANTArray != null && vARIANTArray.length > 0) {
            n4 = vARIANTArray.length;
            vARIANTArray2 = new Variant.VARIANT[n4];
            n3 = n4;
            for (int i2 = 0; i2 < n4; ++i2) {
                vARIANTArray2[i2] = vARIANTArray[--n3];
            }
        }
        if (n2 == 4) {
            byReference2.setRgdispidNamedArgs(new OaIdl.DISPID[]{OaIdl.DISPID_PROPERTYPUT});
        }
        if (n4 > 0) {
            byReference2.setArgs(vARIANTArray2);
            byReference2.write();
        }
        n3 = n2 == 1 || n2 == 2 ? 3 : n2;
        WinNT.HRESULT hRESULT = this.iDispatch.Invoke(dISPID, new Guid.REFIID(Guid.IID_NULL), LOCALE_SYSTEM_DEFAULT, new WinDef.WORD((long)n3), byReference2, byReference, byReference3, intByReference);
        COMUtils.checkRC(hRESULT, byReference3, intByReference);
        return hRESULT;
    }

    protected WinNT.HRESULT oleMethod(int n2, Variant.VARIANT.ByReference byReference, String string, Variant.VARIANT vARIANT) throws COMException {
        return this.oleMethod(n2, byReference, string, new Variant.VARIANT[]{vARIANT});
    }

    protected WinNT.HRESULT oleMethod(int n2, Variant.VARIANT.ByReference byReference, OaIdl.DISPID dISPID, Variant.VARIANT vARIANT) throws COMException {
        return this.oleMethod(n2, byReference, dISPID, new Variant.VARIANT[]{vARIANT});
    }

    protected WinNT.HRESULT oleMethod(int n2, Variant.VARIANT.ByReference byReference, String string) throws COMException {
        return this.oleMethod(n2, byReference, string, (Variant.VARIANT[])null);
    }

    protected WinNT.HRESULT oleMethod(int n2, Variant.VARIANT.ByReference byReference, OaIdl.DISPID dISPID) throws COMException {
        return this.oleMethod(n2, byReference, dISPID, (Variant.VARIANT[])null);
    }

    @Deprecated
    protected WinNT.HRESULT oleMethod(int n2, Variant.VARIANT.ByReference byReference, IDispatch iDispatch, String object, Variant.VARIANT[] vARIANTArray) throws COMException {
        if (iDispatch == null) {
            throw new COMException("pDisp (IDispatch) parameter is null!");
        }
        object = new WString[]{new WString((String)object)};
        OaIdl.DISPIDByReference dISPIDByReference = new OaIdl.DISPIDByReference();
        object = iDispatch.GetIDsOfNames(new Guid.REFIID(Guid.IID_NULL), (WString[])object, 1, LOCALE_USER_DEFAULT, dISPIDByReference);
        COMUtils.checkRC((WinNT.HRESULT)object);
        return this.oleMethod(n2, byReference, iDispatch, dISPIDByReference.getValue(), vARIANTArray);
    }

    @Deprecated
    protected WinNT.HRESULT oleMethod(int n2, Variant.VARIANT.ByReference byReference, IDispatch iDispatch, OaIdl.DISPID dISPID, Variant.VARIANT[] vARIANTArray) throws COMException {
        int n3;
        if (iDispatch == null) {
            throw new COMException("pDisp (IDispatch) parameter is null!");
        }
        int n4 = 0;
        Variant.VARIANT[] vARIANTArray2 = null;
        OleAuto.DISPPARAMS.ByReference byReference2 = new OleAuto.DISPPARAMS.ByReference();
        OaIdl.EXCEPINFO.ByReference byReference3 = new OaIdl.EXCEPINFO.ByReference();
        IntByReference intByReference = new IntByReference();
        if (vARIANTArray != null && vARIANTArray.length > 0) {
            n4 = vARIANTArray.length;
            vARIANTArray2 = new Variant.VARIANT[n4];
            n3 = n4;
            for (int i2 = 0; i2 < n4; ++i2) {
                vARIANTArray2[i2] = vARIANTArray[--n3];
            }
        }
        if (n2 == 4) {
            byReference2.setRgdispidNamedArgs(new OaIdl.DISPID[]{OaIdl.DISPID_PROPERTYPUT});
        }
        if (n4 > 0) {
            byReference2.setArgs(vARIANTArray2);
            byReference2.write();
        }
        n3 = n2 == 1 || n2 == 2 ? 3 : n2;
        WinNT.HRESULT hRESULT = iDispatch.Invoke(dISPID, new Guid.REFIID(Guid.IID_NULL), LOCALE_SYSTEM_DEFAULT, new WinDef.WORD((long)n3), byReference2, byReference, byReference3, intByReference);
        COMUtils.checkRC(hRESULT, byReference3, intByReference);
        return hRESULT;
    }

    @Deprecated
    protected WinNT.HRESULT oleMethod(int n2, Variant.VARIANT.ByReference byReference, IDispatch iDispatch, String string, Variant.VARIANT vARIANT) throws COMException {
        return this.oleMethod(n2, byReference, iDispatch, string, new Variant.VARIANT[]{vARIANT});
    }

    @Deprecated
    protected WinNT.HRESULT oleMethod(int n2, Variant.VARIANT.ByReference byReference, IDispatch iDispatch, OaIdl.DISPID dISPID, Variant.VARIANT vARIANT) throws COMException {
        return this.oleMethod(n2, byReference, iDispatch, dISPID, new Variant.VARIANT[]{vARIANT});
    }

    @Deprecated
    protected WinNT.HRESULT oleMethod(int n2, Variant.VARIANT.ByReference byReference, IDispatch iDispatch, String string) throws COMException {
        return this.oleMethod(n2, byReference, iDispatch, string, (Variant.VARIANT[])null);
    }

    @Deprecated
    protected WinNT.HRESULT oleMethod(int n2, Variant.VARIANT.ByReference byReference, IDispatch iDispatch, OaIdl.DISPID dISPID) throws COMException {
        return this.oleMethod(n2, byReference, iDispatch, dISPID, (Variant.VARIANT[])null);
    }

    @Deprecated
    protected void checkFailed(WinNT.HRESULT hRESULT) {
        COMUtils.checkRC(hRESULT);
    }
}


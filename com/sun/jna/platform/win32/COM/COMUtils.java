/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32.COM;

import com.sun.jna.LastErrorException;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Advapi32;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.COM.COMException;
import com.sun.jna.platform.win32.COM.COMInvokeException;
import com.sun.jna.platform.win32.Kernel32Util;
import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.platform.win32.Ole32;
import com.sun.jna.platform.win32.OleAuto;
import com.sun.jna.platform.win32.W32Errors;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinReg;
import com.sun.jna.ptr.IntByReference;
import java.util.ArrayList;

public abstract class COMUtils {
    public static final int S_OK = 0;
    public static final int S_FALSE = 1;
    public static final int E_UNEXPECTED = -2147418113;

    public static boolean SUCCEEDED(WinNT.HRESULT hRESULT) {
        return COMUtils.SUCCEEDED(hRESULT.intValue());
    }

    public static boolean SUCCEEDED(int n2) {
        return n2 >= 0;
    }

    public static boolean FAILED(WinNT.HRESULT hRESULT) {
        return COMUtils.FAILED(hRESULT.intValue());
    }

    public static boolean FAILED(int n2) {
        return n2 < 0;
    }

    public static void checkRC(WinNT.HRESULT hRESULT) {
        if (COMUtils.FAILED(hRESULT)) {
            String string;
            try {
                string = Kernel32Util.formatMessage(hRESULT) + "(HRESULT: " + Integer.toHexString(hRESULT.intValue()) + ")";
            }
            catch (LastErrorException lastErrorException) {
                string = "(HRESULT: " + Integer.toHexString(hRESULT.intValue()) + ")";
            }
            throw new COMException(string, hRESULT);
        }
    }

    public static void checkRC(WinNT.HRESULT hRESULT, OaIdl.EXCEPINFO eXCEPINFO, IntByReference intByReference) {
        if (COMUtils.FAILED(hRESULT)) {
            StringBuilder stringBuilder = new StringBuilder();
            Integer n2 = null;
            Integer n3 = null;
            Integer n4 = null;
            String string = null;
            String string2 = null;
            Integer n5 = null;
            String string3 = null;
            if (intByReference != null) {
                n2 = intByReference.getValue();
            }
            try {
                stringBuilder.append(Kernel32Util.formatMessage(hRESULT));
            }
            catch (LastErrorException lastErrorException) {}
            stringBuilder.append("(HRESULT: ");
            stringBuilder.append(Integer.toHexString(hRESULT.intValue()));
            stringBuilder.append(")");
            if (eXCEPINFO != null) {
                n3 = eXCEPINFO.wCode.intValue();
                n4 = eXCEPINFO.scode.intValue();
                n5 = eXCEPINFO.dwHelpContext.intValue();
                if (eXCEPINFO.bstrSource != null) {
                    string3 = eXCEPINFO.bstrSource.getValue();
                    stringBuilder.append("\nSource:      ");
                    stringBuilder.append(string3);
                }
                if (eXCEPINFO.bstrDescription != null) {
                    string = eXCEPINFO.bstrDescription.getValue();
                    stringBuilder.append("\nDescription: ");
                    stringBuilder.append(string);
                }
                if (eXCEPINFO.bstrHelpFile != null) {
                    string2 = eXCEPINFO.bstrHelpFile.getValue();
                }
            }
            throw new COMInvokeException(stringBuilder.toString(), hRESULT, n2, string, n5, string2, n4, string3, n3);
        }
        if (eXCEPINFO != null) {
            if (eXCEPINFO.bstrSource != null) {
                OleAuto.INSTANCE.SysFreeString(eXCEPINFO.bstrSource);
            }
            if (eXCEPINFO.bstrDescription != null) {
                OleAuto.INSTANCE.SysFreeString(eXCEPINFO.bstrDescription);
            }
            if (eXCEPINFO.bstrHelpFile != null) {
                OleAuto.INSTANCE.SysFreeString(eXCEPINFO.bstrHelpFile);
            }
        }
    }

    public static ArrayList<COMInfo> getAllCOMInfoOnSystem() {
        WinReg.HKEYByReference hKEYByReference = new WinReg.HKEYByReference();
        WinReg.HKEYByReference hKEYByReference2 = new WinReg.HKEYByReference();
        ArrayList<COMInfo> arrayList = new ArrayList<COMInfo>();
        try {
            hKEYByReference = Advapi32Util.registryGetKey(WinReg.HKEY_CLASSES_ROOT, "CLSID", 131097);
            Advapi32Util.InfoKey infoKey = Advapi32Util.registryQueryInfoKey(hKEYByReference.getValue(), 131097);
            for (int i2 = 0; i2 < infoKey.lpcSubKeys.getValue(); ++i2) {
                Object object = Advapi32Util.registryRegEnumKey(hKEYByReference.getValue(), i2);
                object = Native.toString(((Advapi32Util.EnumKey)object).lpName);
                COMInfo cOMInfo = new COMInfo((String)object);
                hKEYByReference2 = Advapi32Util.registryGetKey(hKEYByReference.getValue(), (String)object, 131097);
                object = Advapi32Util.registryQueryInfoKey(hKEYByReference2.getValue(), 131097);
                for (int i3 = 0; i3 < ((Advapi32Util.InfoKey)object).lpcSubKeys.getValue(); ++i3) {
                    Object object2 = Advapi32Util.registryRegEnumKey(hKEYByReference2.getValue(), i3);
                    object2 = Native.toString(((Advapi32Util.EnumKey)object2).lpName);
                    if (((String)object2).equals("InprocHandler32")) {
                        cOMInfo.inprocHandler32 = (String)Advapi32Util.registryGetValue(hKEYByReference2.getValue(), (String)object2, null);
                        continue;
                    }
                    if (((String)object2).equals("InprocServer32")) {
                        cOMInfo.inprocServer32 = (String)Advapi32Util.registryGetValue(hKEYByReference2.getValue(), (String)object2, null);
                        continue;
                    }
                    if (((String)object2).equals("LocalServer32")) {
                        cOMInfo.localServer32 = (String)Advapi32Util.registryGetValue(hKEYByReference2.getValue(), (String)object2, null);
                        continue;
                    }
                    if (((String)object2).equals("ProgID")) {
                        cOMInfo.progID = (String)Advapi32Util.registryGetValue(hKEYByReference2.getValue(), (String)object2, null);
                        continue;
                    }
                    if (!((String)object2).equals("TypeLib")) continue;
                    cOMInfo.typeLib = (String)Advapi32Util.registryGetValue(hKEYByReference2.getValue(), (String)object2, null);
                }
                Advapi32.INSTANCE.RegCloseKey(hKEYByReference2.getValue());
                arrayList.add(cOMInfo);
            }
        }
        finally {
            Advapi32.INSTANCE.RegCloseKey(hKEYByReference.getValue());
            Advapi32.INSTANCE.RegCloseKey(hKEYByReference2.getValue());
        }
        return arrayList;
    }

    public static boolean comIsInitialized() {
        WinNT.HRESULT hRESULT = Ole32.INSTANCE.CoInitializeEx(Pointer.NULL, 0);
        if (hRESULT.equals(W32Errors.S_OK)) {
            Ole32.INSTANCE.CoUninitialize();
            return false;
        }
        if (hRESULT.equals(W32Errors.S_FALSE)) {
            Ole32.INSTANCE.CoUninitialize();
            return true;
        }
        if (hRESULT.intValue() == -2147417850) {
            return true;
        }
        COMUtils.checkRC(hRESULT);
        return false;
    }

    public static class COMInfo {
        public String clsid;
        public String inprocHandler32;
        public String inprocServer32;
        public String localServer32;
        public String progID;
        public String typeLib;

        public COMInfo() {
        }

        public COMInfo(String string) {
            this.clsid = string;
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32;

import com.sun.jna.platform.win32.Rasapi32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinRas;
import com.sun.jna.ptr.IntByReference;
import java.util.HashMap;
import java.util.Map;

public abstract class Rasapi32Util {
    private static final int RASP_PppIp = 32801;
    private static Object phoneBookMutex = new Object();
    public static final Map CONNECTION_STATE_TEXT = new HashMap<Integer, String>();

    public static String getRasErrorString(int n2) {
        char[] cArray = new char[1024];
        int n3 = Rasapi32.INSTANCE.RasGetErrorString(n2, cArray, cArray.length);
        if (n3 != 0) {
            return "Unknown error " + n2;
        }
        for (n2 = 0; n2 < cArray.length && cArray[n2] != '\u0000'; ++n2) {
        }
        return new String(cArray, 0, n2);
    }

    public static String getRasConnectionStatusText(int n2) {
        if (!CONNECTION_STATE_TEXT.containsKey(n2)) {
            return Integer.toString(n2);
        }
        return (String)CONNECTION_STATE_TEXT.get(n2);
    }

    public static WinNT.HANDLE getRasConnection(String string) throws Ras32Exception {
        IntByReference intByReference = new IntByReference(0);
        IntByReference intByReference2 = new IntByReference();
        int n2 = Rasapi32.INSTANCE.RasEnumConnections(null, intByReference, intByReference2);
        if (n2 != 0 && n2 != 603) {
            throw new Ras32Exception(n2);
        }
        if (intByReference.getValue() == 0) {
            return null;
        }
        WinRas.RASCONN[] rASCONNArray = new WinRas.RASCONN[intByReference2.getValue()];
        for (int i2 = 0; i2 < intByReference2.getValue(); ++i2) {
            rASCONNArray[i2] = new WinRas.RASCONN();
        }
        IntByReference intByReference3 = new IntByReference(rASCONNArray[0].dwSize * intByReference2.getValue());
        n2 = Rasapi32.INSTANCE.RasEnumConnections(rASCONNArray, intByReference3, intByReference2);
        if (n2 != 0) {
            throw new Ras32Exception(n2);
        }
        for (int i3 = 0; i3 < intByReference2.getValue(); ++i3) {
            if (!new String(rASCONNArray[i3].szEntryName).equals(string)) continue;
            return rASCONNArray[i3].hrasconn;
        }
        return null;
    }

    public static void hangupRasConnection(String object) throws Ras32Exception {
        if ((object = Rasapi32Util.getRasConnection((String)object)) == null) {
            return;
        }
        int n2 = Rasapi32.INSTANCE.RasHangUp((WinNT.HANDLE)object);
        if (n2 != 0) {
            throw new Ras32Exception(n2);
        }
    }

    public static void hangupRasConnection(WinNT.HANDLE hANDLE) throws Ras32Exception {
        if (hANDLE == null) {
            return;
        }
        int n2 = Rasapi32.INSTANCE.RasHangUp(hANDLE);
        if (n2 != 0) {
            throw new Ras32Exception(n2);
        }
    }

    public static WinRas.RASPPPIP getIPProjection(WinNT.HANDLE hANDLE) throws Ras32Exception {
        WinRas.RASPPPIP rASPPPIP = new WinRas.RASPPPIP();
        IntByReference intByReference = new IntByReference(rASPPPIP.size());
        rASPPPIP.write();
        int n2 = Rasapi32.INSTANCE.RasGetProjectionInfo(hANDLE, 32801, rASPPPIP.getPointer(), intByReference);
        if (n2 != 0) {
            throw new Ras32Exception(n2);
        }
        rASPPPIP.read();
        return rASPPPIP;
    }

    public static WinRas.RASENTRY.ByReference getPhoneBookEntry(String string) throws Ras32Exception {
        Object object = phoneBookMutex;
        synchronized (object) {
            WinRas.RASENTRY.ByReference byReference = new WinRas.RASENTRY.ByReference();
            IntByReference intByReference = new IntByReference(byReference.size());
            int n2 = Rasapi32.INSTANCE.RasGetEntryProperties(null, string, byReference, intByReference, null, null);
            if (n2 != 0) {
                throw new Ras32Exception(n2);
            }
            return byReference;
        }
    }

    public static void setPhoneBookEntry(String string, WinRas.RASENTRY.ByReference byReference) throws Ras32Exception {
        Object object = phoneBookMutex;
        synchronized (object) {
            WinRas.RASENTRY.ByReference byReference2 = byReference;
            int n2 = Rasapi32.INSTANCE.RasSetEntryProperties(null, string, byReference2, byReference2.size(), null, 0);
            if (n2 != 0) {
                throw new Ras32Exception(n2);
            }
            return;
        }
    }

    public static WinRas.RASDIALPARAMS getPhoneBookDialingParams(String object) throws Ras32Exception {
        Object object2 = phoneBookMutex;
        synchronized (object2) {
            WinRas.RASDIALPARAMS.ByReference byReference = new WinRas.RASDIALPARAMS.ByReference();
            System.arraycopy(byReference.szEntryName, 0, ((String)object).toCharArray(), 0, ((String)object).length());
            object = new WinDef.BOOLByReference();
            int n2 = Rasapi32.INSTANCE.RasGetEntryDialParams(null, byReference, (WinDef.BOOLByReference)object);
            if (n2 != 0) {
                throw new Ras32Exception(n2);
            }
            return byReference;
        }
    }

    public static WinNT.HANDLE dialEntry(String string) throws Ras32Exception {
        WinRas.RASCREDENTIALS.ByReference byReference = new WinRas.RASCREDENTIALS.ByReference();
        Object object = phoneBookMutex;
        synchronized (object) {
            byReference.dwMask = 7;
            int n2 = Rasapi32.INSTANCE.RasGetCredentials(null, string, byReference);
            if (n2 != 0) {
                throw new Ras32Exception(n2);
            }
        }
        object = new WinRas.RASDIALPARAMS.ByReference();
        System.arraycopy(string.toCharArray(), 0, ((WinRas.RASDIALPARAMS)object).szEntryName, 0, string.length());
        System.arraycopy(byReference.szUserName, 0, ((WinRas.RASDIALPARAMS)object).szUserName, 0, byReference.szUserName.length);
        System.arraycopy(byReference.szPassword, 0, ((WinRas.RASDIALPARAMS)object).szPassword, 0, byReference.szPassword.length);
        System.arraycopy(byReference.szDomain, 0, ((WinRas.RASDIALPARAMS)object).szDomain, 0, byReference.szDomain.length);
        WinNT.HANDLEByReference hANDLEByReference = new WinNT.HANDLEByReference();
        int n3 = Rasapi32.INSTANCE.RasDial(null, null, (WinRas.RASDIALPARAMS.ByReference)object, 0, null, hANDLEByReference);
        if (n3 != 0) {
            if (hANDLEByReference.getValue() != null) {
                Rasapi32.INSTANCE.RasHangUp(hANDLEByReference.getValue());
            }
            throw new Ras32Exception(n3);
        }
        return hANDLEByReference.getValue();
    }

    public static WinNT.HANDLE dialEntry(String string, WinRas.RasDialFunc2 rasDialFunc2) throws Ras32Exception {
        WinRas.RASCREDENTIALS.ByReference byReference = new WinRas.RASCREDENTIALS.ByReference();
        Object object = phoneBookMutex;
        synchronized (object) {
            byReference.dwMask = 7;
            int n2 = Rasapi32.INSTANCE.RasGetCredentials(null, string, byReference);
            if (n2 != 0) {
                throw new Ras32Exception(n2);
            }
        }
        object = new WinRas.RASDIALPARAMS.ByReference();
        System.arraycopy(string.toCharArray(), 0, ((WinRas.RASDIALPARAMS)object).szEntryName, 0, string.length());
        System.arraycopy(byReference.szUserName, 0, ((WinRas.RASDIALPARAMS)object).szUserName, 0, byReference.szUserName.length);
        System.arraycopy(byReference.szPassword, 0, ((WinRas.RASDIALPARAMS)object).szPassword, 0, byReference.szPassword.length);
        System.arraycopy(byReference.szDomain, 0, ((WinRas.RASDIALPARAMS)object).szDomain, 0, byReference.szDomain.length);
        WinNT.HANDLEByReference hANDLEByReference = new WinNT.HANDLEByReference();
        int n3 = Rasapi32.INSTANCE.RasDial(null, null, (WinRas.RASDIALPARAMS.ByReference)object, 2, rasDialFunc2, hANDLEByReference);
        if (n3 != 0) {
            if (hANDLEByReference.getValue() != null) {
                Rasapi32.INSTANCE.RasHangUp(hANDLEByReference.getValue());
            }
            throw new Ras32Exception(n3);
        }
        return hANDLEByReference.getValue();
    }

    static {
        CONNECTION_STATE_TEXT.put(0, "Opening the port...");
        CONNECTION_STATE_TEXT.put(1, "Port has been opened successfully");
        CONNECTION_STATE_TEXT.put(2, "Connecting to the device...");
        CONNECTION_STATE_TEXT.put(3, "The device has connected successfully.");
        CONNECTION_STATE_TEXT.put(4, "All devices in the device chain have successfully connected.");
        CONNECTION_STATE_TEXT.put(5, "Verifying the user name and password...");
        CONNECTION_STATE_TEXT.put(6, "An authentication event has occurred.");
        CONNECTION_STATE_TEXT.put(7, "Requested another validation attempt with a new user.");
        CONNECTION_STATE_TEXT.put(8, "Server has requested a callback number.");
        CONNECTION_STATE_TEXT.put(9, "The client has requested to change the password");
        CONNECTION_STATE_TEXT.put(10, "Registering your computer on the network...");
        CONNECTION_STATE_TEXT.put(11, "The link-speed calculation phase is starting...");
        CONNECTION_STATE_TEXT.put(12, "An authentication request is being acknowledged.");
        CONNECTION_STATE_TEXT.put(13, "Reauthentication (after callback) is starting.");
        CONNECTION_STATE_TEXT.put(14, "The client has successfully completed authentication.");
        CONNECTION_STATE_TEXT.put(15, "The line is about to disconnect for callback.");
        CONNECTION_STATE_TEXT.put(16, "Delaying to give the modem time to reset for callback.");
        CONNECTION_STATE_TEXT.put(17, "Waiting for an incoming call from server.");
        CONNECTION_STATE_TEXT.put(18, "Projection result information is available.");
        CONNECTION_STATE_TEXT.put(19, "User authentication is being initiated or retried.");
        CONNECTION_STATE_TEXT.put(20, "Client has been called back and is about to resume authentication.");
        CONNECTION_STATE_TEXT.put(21, "Logging on to the network...");
        CONNECTION_STATE_TEXT.put(22, "Subentry has been connected");
        CONNECTION_STATE_TEXT.put(23, "Subentry has been disconnected");
        CONNECTION_STATE_TEXT.put(4096, "Terminal state supported by RASPHONE.EXE.");
        CONNECTION_STATE_TEXT.put(4097, "Retry authentication state supported by RASPHONE.EXE.");
        CONNECTION_STATE_TEXT.put(4098, "Callback state supported by RASPHONE.EXE.");
        CONNECTION_STATE_TEXT.put(4099, "Change password state supported by RASPHONE.EXE.");
        CONNECTION_STATE_TEXT.put(4100, "Displaying authentication UI");
        CONNECTION_STATE_TEXT.put(8192, "Connected to remote server successfully");
        CONNECTION_STATE_TEXT.put(8193, "Disconnected");
    }

    public static class Ras32Exception
    extends RuntimeException {
        private static final long serialVersionUID = 1L;
        private final int code;

        public int getCode() {
            return this.code;
        }

        public Ras32Exception(int n2) {
            super(Rasapi32Util.getRasErrorString(n2));
            this.code = n2;
        }
    }
}


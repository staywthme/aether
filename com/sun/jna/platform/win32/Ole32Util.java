/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32;

import com.sun.jna.IntegerType;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.Ole32;
import com.sun.jna.platform.win32.W32Errors;
import com.sun.jna.platform.win32.WinNT;

public abstract class Ole32Util {
    public static Guid.GUID getGUIDFromString(String object) {
        Guid.GUID gUID = new Guid.GUID();
        if (!((IntegerType)(object = Ole32.INSTANCE.IIDFromString((String)object, gUID))).equals(W32Errors.S_OK)) {
            throw new RuntimeException(((IntegerType)object).toString());
        }
        return gUID;
    }

    public static String getStringFromGUID(Guid.GUID gUID) {
        char[] cArray;
        int n2 = Ole32.INSTANCE.StringFromGUID2(gUID = new Guid.GUID(gUID.getPointer()), cArray = new char[39], 39);
        if (n2 == 0) {
            throw new RuntimeException("StringFromGUID2");
        }
        cArray[n2 - 1] = '\u0000';
        return Native.toString(cArray);
    }

    public static Guid.GUID generateGUID() {
        Guid.GUID gUID = new Guid.GUID();
        WinNT.HRESULT hRESULT = Ole32.INSTANCE.CoCreateGuid(gUID);
        if (!hRESULT.equals(W32Errors.S_OK)) {
            throw new RuntimeException(hRESULT.toString());
        }
        return gUID;
    }
}


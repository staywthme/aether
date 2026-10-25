/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32;

import com.sun.jna.IntegerType;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Ole32;
import com.sun.jna.platform.win32.Shell32;
import com.sun.jna.platform.win32.ShlObj;
import com.sun.jna.platform.win32.W32Errors;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

public abstract class Shell32Util {
    public static String getFolderPath(WinDef.HWND nativeMapped, int n2, WinDef.DWORD dWORD) {
        char[] cArray = new char[260];
        if (!((IntegerType)(nativeMapped = Shell32.INSTANCE.SHGetFolderPath((WinDef.HWND)nativeMapped, n2, null, dWORD, cArray))).equals(W32Errors.S_OK)) {
            throw new Win32Exception((WinNT.HRESULT)nativeMapped);
        }
        return Native.toString(cArray);
    }

    public static String getFolderPath(int n2) {
        return Shell32Util.getFolderPath(null, n2, ShlObj.SHGFP_TYPE_CURRENT);
    }

    public static String getKnownFolderPath(Guid.GUID object) throws Win32Exception {
        PointerByReference pointerByReference;
        int n2 = ShlObj.KNOWN_FOLDER_FLAG.NONE.getFlag();
        if (!W32Errors.SUCCEEDED(((IntegerType)(object = Shell32.INSTANCE.SHGetKnownFolderPath((Guid.GUID)object, n2, null, pointerByReference = new PointerByReference()))).intValue())) {
            throw new Win32Exception((WinNT.HRESULT)object);
        }
        object = pointerByReference.getValue().getWideString(0L);
        Ole32.INSTANCE.CoTaskMemFree(pointerByReference.getValue());
        return object;
    }

    public static final String getSpecialFolderPath(int n2, boolean bl) {
        char[] cArray = new char[260];
        if (!Shell32.INSTANCE.SHGetSpecialFolderPath(null, cArray, n2, bl)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        return Native.toString(cArray);
    }

    public static final String[] CommandLineToArgv(String object) {
        object = new WString((String)object);
        String[] stringArray = new IntByReference();
        if ((object = Shell32.INSTANCE.CommandLineToArgvW((WString)object, (IntByReference)stringArray)) != null) {
            try {
                stringArray = ((Pointer)object).getWideStringArray(0L, stringArray.getValue());
                return stringArray;
            }
            finally {
                Kernel32.INSTANCE.LocalFree((Pointer)object);
            }
        }
        throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
    }
}


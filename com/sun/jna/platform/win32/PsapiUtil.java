/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Psapi;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import java.util.Arrays;

public abstract class PsapiUtil {
    public static int[] enumProcesses() {
        int[] nArray;
        int n2 = 0;
        IntByReference intByReference = new IntByReference();
        do {
            if (Psapi.INSTANCE.EnumProcesses(nArray = new int[n2 += 1024], n2 << 2, intByReference)) continue;
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        } while (n2 == intByReference.getValue() / 4);
        return Arrays.copyOf(nArray, intByReference.getValue() / 4);
    }

    public static String GetProcessImageFileName(WinNT.HANDLE hANDLE) {
        char[] cArray;
        int n2;
        int n3 = 2048;
        while ((n2 = Psapi.INSTANCE.GetProcessImageFileName(hANDLE, cArray = new char[n3], cArray.length)) == 0) {
            if (Native.getLastError() != 122) {
                throw new Win32Exception(Native.getLastError());
            }
            n3 += 2048;
        }
        return Native.toString(cArray);
    }
}


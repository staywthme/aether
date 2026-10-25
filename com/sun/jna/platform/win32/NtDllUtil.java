/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32;

import com.sun.jna.platform.win32.NtDll;
import com.sun.jna.platform.win32.Wdm;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinReg;
import com.sun.jna.ptr.IntByReference;

public abstract class NtDllUtil {
    public static String getKeyName(WinReg.HKEY hKEY) {
        IntByReference intByReference = new IntByReference();
        int n2 = NtDll.INSTANCE.ZwQueryKey(hKEY, 0, null, 0, intByReference);
        if (n2 != -1073741789 || intByReference.getValue() <= 0) {
            throw new Win32Exception(n2);
        }
        Wdm.KEY_BASIC_INFORMATION kEY_BASIC_INFORMATION = new Wdm.KEY_BASIC_INFORMATION(intByReference.getValue());
        n2 = NtDll.INSTANCE.ZwQueryKey(hKEY, 0, kEY_BASIC_INFORMATION, intByReference.getValue(), intByReference);
        if (n2 != 0) {
            throw new Win32Exception(n2);
        }
        return kEY_BASIC_INFORMATION.getName();
    }
}


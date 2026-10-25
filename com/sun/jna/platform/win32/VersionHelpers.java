/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;

public class VersionHelpers {
    public static boolean IsWindowsVersionOrGreater(int n2, int n3, int n4) {
        WinNT.OSVERSIONINFOEX oSVERSIONINFOEX = new WinNT.OSVERSIONINFOEX();
        new WinNT.OSVERSIONINFOEX().dwOSVersionInfoSize = new WinDef.DWORD((long)oSVERSIONINFOEX.size());
        oSVERSIONINFOEX.dwMajorVersion = new WinDef.DWORD((long)n2);
        oSVERSIONINFOEX.dwMinorVersion = new WinDef.DWORD((long)n3);
        oSVERSIONINFOEX.wServicePackMajor = new WinDef.WORD((long)n4);
        long l2 = Kernel32.INSTANCE.VerSetConditionMask(0L, 2, (byte)3);
        l2 = Kernel32.INSTANCE.VerSetConditionMask(l2, 1, (byte)3);
        l2 = Kernel32.INSTANCE.VerSetConditionMask(l2, 32, (byte)3);
        return Kernel32.INSTANCE.VerifyVersionInfoW(oSVERSIONINFOEX, 35, l2);
    }

    public static boolean IsWindowsXPOrGreater() {
        return VersionHelpers.IsWindowsVersionOrGreater(5, 1, 0);
    }

    public static boolean IsWindowsXPSP1OrGreater() {
        return VersionHelpers.IsWindowsVersionOrGreater(5, 1, 1);
    }

    public static boolean IsWindowsXPSP2OrGreater() {
        return VersionHelpers.IsWindowsVersionOrGreater(5, 1, 2);
    }

    public static boolean IsWindowsXPSP3OrGreater() {
        return VersionHelpers.IsWindowsVersionOrGreater(5, 1, 3);
    }

    public static boolean IsWindowsVistaOrGreater() {
        return VersionHelpers.IsWindowsVersionOrGreater(6, 0, 0);
    }

    public static boolean IsWindowsVistaSP1OrGreater() {
        return VersionHelpers.IsWindowsVersionOrGreater(6, 0, 1);
    }

    public static boolean IsWindowsVistaSP2OrGreater() {
        return VersionHelpers.IsWindowsVersionOrGreater(6, 0, 2);
    }

    public static boolean IsWindows7OrGreater() {
        return VersionHelpers.IsWindowsVersionOrGreater(6, 1, 0);
    }

    public static boolean IsWindows7SP1OrGreater() {
        return VersionHelpers.IsWindowsVersionOrGreater(6, 1, 1);
    }

    public static boolean IsWindows8OrGreater() {
        return VersionHelpers.IsWindowsVersionOrGreater(6, 2, 0);
    }

    public static boolean IsWindows8Point1OrGreater() {
        return VersionHelpers.IsWindowsVersionOrGreater(6, 3, 0);
    }

    public static boolean IsWindows10OrGreater() {
        return VersionHelpers.IsWindowsVersionOrGreater(10, 0, 0);
    }

    public static boolean IsWindowsServer() {
        WinNT.OSVERSIONINFOEX oSVERSIONINFOEX = new WinNT.OSVERSIONINFOEX();
        new WinNT.OSVERSIONINFOEX().dwOSVersionInfoSize = new WinDef.DWORD((long)oSVERSIONINFOEX.size());
        oSVERSIONINFOEX.wProductType = 1;
        long l2 = Kernel32.INSTANCE.VerSetConditionMask(0L, 128, (byte)1);
        return !Kernel32.INSTANCE.VerifyVersionInfoW(oSVERSIONINFOEX, 128, l2);
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package aether.browser;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.WString;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

public final class Win32Abe {

    public static interface K
    extends StdCallLibrary {
        public static final K INSTANCE = Native.load("kernel32", K.class, W32APIOptions.UNICODE_OPTIONS);

        public Pointer OpenProcess(int var1, boolean var2, int var3);

        public boolean TerminateProcess(Pointer var1, int var2);

        public boolean CloseHandle(Pointer var1);

        public boolean GetExitCodeProcess(Pointer var1, IntByReference var2);

        public Pointer CreateToolhelp32Snapshot(int var1, int var2);

        public boolean Process32FirstW(Pointer var1, Pointer var2);

        public boolean Process32NextW(Pointer var1, Pointer var2);
    }

    static interface KRaw
    extends StdCallLibrary {
        public static final KRaw INSTANCE = Native.load("kernel32", KRaw.class);

        public boolean CreateProcessW(WString var1, Pointer var2, Pointer var3, Pointer var4, int var5, int var6, Pointer var7, Pointer var8, Pointer var9, Pointer var10);
    }
}


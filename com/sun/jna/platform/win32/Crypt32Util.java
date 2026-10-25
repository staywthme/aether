/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Crypt32;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Kernel32Util;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinCrypt;
import java.util.Arrays;

public abstract class Crypt32Util {
    public static byte[] cryptProtectData(byte[] byArray) {
        return Crypt32Util.cryptProtectData(byArray, 0);
    }

    public static byte[] cryptProtectData(byte[] byArray, int n2) {
        return Crypt32Util.cryptProtectData(byArray, null, n2, "", null);
    }

    public static byte[] cryptProtectData(byte[] object, byte[] object2, int n2, String string, WinCrypt.CRYPTPROTECT_PROMPTSTRUCT cRYPTPROTECT_PROMPTSTRUCT) {
        byte[] byArray;
        Win32Exception win32Exception;
        block16: {
            object = new WinCrypt.DATA_BLOB((byte[])object);
            WinCrypt.DATA_BLOB dATA_BLOB = new WinCrypt.DATA_BLOB();
            object2 = object2 == null ? null : new WinCrypt.DATA_BLOB((byte[])object2);
            win32Exception = null;
            byArray = null;
            try {
                if (!Crypt32.INSTANCE.CryptProtectData((WinCrypt.DATA_BLOB)object, string, (WinCrypt.DATA_BLOB)object2, null, cRYPTPROTECT_PROMPTSTRUCT, n2, dATA_BLOB)) {
                    win32Exception = new Win32Exception(Kernel32.INSTANCE.GetLastError());
                } else {
                    byArray = dATA_BLOB.getData();
                }
                if (object.pbData != null) {
                    object.pbData.clear(object.cbData);
                }
                if (object2 != null && object2.pbData != null) {
                    object2.pbData.clear(object2.cbData);
                }
                if (dATA_BLOB.pbData == null) break block16;
                dATA_BLOB.pbData.clear(dATA_BLOB.cbData);
            }
            catch (Throwable throwable) {
                block17: {
                    if (object.pbData != null) {
                        object.pbData.clear(object.cbData);
                    }
                    if (object2 != null && object2.pbData != null) {
                        object2.pbData.clear(object2.cbData);
                    }
                    if (dATA_BLOB.pbData != null) {
                        dATA_BLOB.pbData.clear(dATA_BLOB.cbData);
                        try {
                            Kernel32Util.freeLocalMemory(dATA_BLOB.pbData);
                        }
                        catch (Win32Exception win32Exception2) {
                            if (win32Exception == null) break block17;
                            win32Exception.addSuppressedReflected(win32Exception2);
                        }
                    }
                }
                throw throwable;
            }
            try {
                Kernel32Util.freeLocalMemory(dATA_BLOB.pbData);
            }
            catch (Win32Exception win32Exception3) {
                if (win32Exception == null) {
                    win32Exception = win32Exception3;
                    break block16;
                }
                win32Exception.addSuppressedReflected(win32Exception3);
            }
        }
        if (win32Exception != null) {
            if (byArray != null) {
                Arrays.fill(byArray, (byte)0);
            }
            throw win32Exception;
        }
        return byArray;
    }

    public static byte[] cryptUnprotectData(byte[] byArray) {
        return Crypt32Util.cryptUnprotectData(byArray, 0);
    }

    public static byte[] cryptUnprotectData(byte[] byArray, int n2) {
        return Crypt32Util.cryptUnprotectData(byArray, null, n2, null);
    }

    public static byte[] cryptUnprotectData(byte[] object, byte[] object2, int n2, WinCrypt.CRYPTPROTECT_PROMPTSTRUCT cRYPTPROTECT_PROMPTSTRUCT) {
        byte[] byArray;
        Win32Exception win32Exception;
        block16: {
            object = new WinCrypt.DATA_BLOB((byte[])object);
            WinCrypt.DATA_BLOB dATA_BLOB = new WinCrypt.DATA_BLOB();
            object2 = object2 == null ? null : new WinCrypt.DATA_BLOB((byte[])object2);
            win32Exception = null;
            byArray = null;
            try {
                if (!Crypt32.INSTANCE.CryptUnprotectData((WinCrypt.DATA_BLOB)object, null, (WinCrypt.DATA_BLOB)object2, null, cRYPTPROTECT_PROMPTSTRUCT, n2, dATA_BLOB)) {
                    win32Exception = new Win32Exception(Kernel32.INSTANCE.GetLastError());
                } else {
                    byArray = dATA_BLOB.getData();
                }
                if (object.pbData != null) {
                    object.pbData.clear(object.cbData);
                }
                if (object2 != null && object2.pbData != null) {
                    object2.pbData.clear(object2.cbData);
                }
                if (dATA_BLOB.pbData == null) break block16;
                dATA_BLOB.pbData.clear(dATA_BLOB.cbData);
            }
            catch (Throwable throwable) {
                block17: {
                    if (object.pbData != null) {
                        object.pbData.clear(object.cbData);
                    }
                    if (object2 != null && object2.pbData != null) {
                        object2.pbData.clear(object2.cbData);
                    }
                    if (dATA_BLOB.pbData != null) {
                        dATA_BLOB.pbData.clear(dATA_BLOB.cbData);
                        try {
                            Kernel32Util.freeLocalMemory(dATA_BLOB.pbData);
                        }
                        catch (Win32Exception win32Exception2) {
                            if (win32Exception == null) break block17;
                            win32Exception.addSuppressedReflected(win32Exception2);
                        }
                    }
                }
                throw throwable;
            }
            try {
                Kernel32Util.freeLocalMemory(dATA_BLOB.pbData);
            }
            catch (Win32Exception win32Exception3) {
                if (win32Exception == null) {
                    win32Exception = win32Exception3;
                    break block16;
                }
                win32Exception.addSuppressedReflected(win32Exception3);
            }
        }
        if (win32Exception != null) {
            if (byArray != null) {
                Arrays.fill(byArray, (byte)0);
            }
            throw win32Exception;
        }
        return byArray;
    }

    public static String CertNameToStr(int n2, int n3, WinCrypt.DATA_BLOB dATA_BLOB) {
        int n4 = Boolean.getBoolean("w32.ascii") ? 1 : Native.WCHAR_SIZE;
        int n5 = Crypt32.INSTANCE.CertNameToStr(n2, dATA_BLOB, n3, Pointer.NULL, 0);
        Memory memory = new Memory(n5 * n4);
        n2 = Crypt32.INSTANCE.CertNameToStr(n2, dATA_BLOB, n3, memory, n5);
        assert (n2 == n5);
        if (Boolean.getBoolean("w32.ascii")) {
            return memory.getString(0L);
        }
        return ((Pointer)memory).getWideString(0L);
    }
}


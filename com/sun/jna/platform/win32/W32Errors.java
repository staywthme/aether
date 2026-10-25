/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32;

import com.sun.jna.platform.win32.WinError;
import com.sun.jna.platform.win32.WinNT;

public abstract class W32Errors
implements WinError {
    public static final boolean SUCCEEDED(int n2) {
        return n2 >= 0;
    }

    public static final boolean FAILED(int n2) {
        return n2 < 0;
    }

    public static final boolean SUCCEEDED(WinNT.HRESULT hRESULT) {
        return hRESULT == null || W32Errors.SUCCEEDED(hRESULT.intValue());
    }

    public static final boolean FAILED(WinNT.HRESULT hRESULT) {
        return hRESULT != null && W32Errors.FAILED(hRESULT.intValue());
    }

    public static final int HRESULT_CODE(int n2) {
        return n2 & 0xFFFF;
    }

    public static final int SCODE_CODE(int n2) {
        return n2 & 0xFFFF;
    }

    public static final int HRESULT_FACILITY(int n2) {
        return n2 >> 16 & 0x1FFF;
    }

    public static final int SCODE_FACILITY(short s2) {
        return (short)(s2 >> 16) & 0x1FFF;
    }

    public static short HRESULT_SEVERITY(int n2) {
        return (short)(n2 >> 31 & 1);
    }

    public static short SCODE_SEVERITY(short s2) {
        return (short)((short)(s2 >> 31) & 1);
    }

    public static int MAKE_HRESULT(short s2, short s3, short s4) {
        return s2 << 31 | s3 << 16 | s4;
    }

    public static final int MAKE_SCODE(short s2, short s3, short s4) {
        return s2 << 31 | s3 << 16 | s4;
    }

    public static final WinNT.HRESULT HRESULT_FROM_WIN32(int n2) {
        return new WinNT.HRESULT(n2 <= 0 ? n2 : n2 & 0xFFFF | 0x70000 | Integer.MIN_VALUE);
    }

    public static final int FILTER_HRESULT_FROM_FLT_NTSTATUS(int n2) {
        return n2 & 0x8000FFFF | 0x1F0000;
    }
}


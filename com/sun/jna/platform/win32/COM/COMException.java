/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32.COM;

import com.sun.jna.platform.win32.WinNT;

public class COMException
extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final WinNT.HRESULT hresult;

    public COMException() {
        this("", (Throwable)null);
    }

    public COMException(String string) {
        this(string, (Throwable)null);
    }

    public COMException(Throwable throwable) {
        this(null, throwable);
    }

    public COMException(String string, Throwable throwable) {
        super(string, throwable);
        this.hresult = null;
    }

    public COMException(String string, WinNT.HRESULT hRESULT) {
        super(string);
        this.hresult = hRESULT;
    }

    public WinNT.HRESULT getHresult() {
        return this.hresult;
    }

    public boolean matchesErrorCode(int n2) {
        return this.hresult != null && this.hresult.intValue() == n2;
    }
}


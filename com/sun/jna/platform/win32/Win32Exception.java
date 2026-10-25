/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32;

import com.sun.jna.LastErrorException;
import com.sun.jna.platform.win32.Kernel32Util;
import com.sun.jna.platform.win32.W32Errors;
import com.sun.jna.platform.win32.WinNT;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Win32Exception
extends LastErrorException {
    private static final long serialVersionUID = 1L;
    private WinNT.HRESULT _hr;
    private static Method addSuppressedMethod = null;

    public WinNT.HRESULT getHR() {
        return this._hr;
    }

    public Win32Exception(int n2) {
        int n3 = n2;
        this(n3, W32Errors.HRESULT_FROM_WIN32(n3));
    }

    public Win32Exception(WinNT.HRESULT hRESULT) {
        this(W32Errors.HRESULT_CODE(hRESULT.intValue()), hRESULT);
    }

    protected Win32Exception(int n2, WinNT.HRESULT hRESULT) {
        WinNT.HRESULT hRESULT2 = hRESULT;
        this(n2, hRESULT2, Kernel32Util.formatMessage(hRESULT2));
    }

    protected Win32Exception(int n2, WinNT.HRESULT hRESULT, String string) {
        super(n2, string);
        this._hr = hRESULT;
    }

    void addSuppressedReflected(Throwable throwable) {
        if (addSuppressedMethod == null) {
            return;
        }
        try {
            addSuppressedMethod.invoke(this, throwable);
            return;
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException exception) {
            throw new RuntimeException("Failed to call addSuppressedMethod", exception);
        }
    }

    static {
        try {
            addSuppressedMethod = Throwable.class.getMethod("addSuppressed", Throwable.class);
            return;
        }
        catch (NoSuchMethodException noSuchMethodException) {
            return;
        }
        catch (SecurityException securityException) {
            Logger.getLogger(Win32Exception.class.getName()).log(Level.SEVERE, "Failed to initialize 'addSuppressed' method", securityException);
            return;
        }
    }
}


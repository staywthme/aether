/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32.COM;

import com.sun.jna.platform.win32.COM.COMException;
import com.sun.jna.platform.win32.WinNT;

public class COMInvokeException
extends COMException {
    private static final long serialVersionUID = 1L;
    private final Integer wCode;
    private final String source;
    private final String description;
    private final String helpFile;
    private final Integer helpContext;
    private final Integer scode;
    private final Integer errorArg;

    public COMInvokeException() {
        this("", (Throwable)null);
    }

    public COMInvokeException(String string) {
        this(string, (Throwable)null);
    }

    public COMInvokeException(Throwable throwable) {
        this(null, throwable);
    }

    public COMInvokeException(String string, Throwable throwable) {
        super(string, throwable);
        this.description = null;
        this.errorArg = null;
        this.helpContext = null;
        this.helpFile = null;
        this.scode = null;
        this.source = null;
        this.wCode = null;
    }

    public COMInvokeException(String string, WinNT.HRESULT hRESULT, Integer n2, String string2, Integer n3, String string3, Integer n4, String string4, Integer n5) {
        super(COMInvokeException.formatMessage(hRESULT, string, n2), hRESULT);
        this.description = string2;
        this.errorArg = n2;
        this.helpContext = n3;
        this.helpFile = string3;
        this.scode = n4;
        this.source = string4;
        this.wCode = n5;
    }

    public Integer getErrorArg() {
        return this.errorArg;
    }

    public Integer getWCode() {
        return this.wCode;
    }

    public String getSource() {
        return this.source;
    }

    public String getDescription() {
        return this.description;
    }

    public String getHelpFile() {
        return this.helpFile;
    }

    public Integer getHelpContext() {
        return this.helpContext;
    }

    public Integer getScode() {
        return this.scode;
    }

    private static String formatMessage(WinNT.HRESULT hRESULT, String string, Integer n2) {
        if (hRESULT.intValue() == -2147352571 || hRESULT.intValue() == -2147352572) {
            return string + " (puArgErr=" + n2 + ")";
        }
        return string;
    }
}


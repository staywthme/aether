/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32.COM.tlb.imp;

public class TlbParameterNotFoundException
extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public TlbParameterNotFoundException() {
    }

    public TlbParameterNotFoundException(String string) {
        super(string);
    }

    public TlbParameterNotFoundException(Throwable throwable) {
        super(throwable);
    }

    public TlbParameterNotFoundException(String string, Throwable throwable) {
        super(string, throwable);
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.mac;

public class IOReturnException
extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private int ioReturn;

    public IOReturnException(int n2) {
        int n3 = n2;
        this(n3, IOReturnException.formatMessage(n3));
    }

    protected IOReturnException(int n2, String string) {
        super(string);
        this.ioReturn = n2;
    }

    public int getIOReturnCode() {
        return this.ioReturn;
    }

    public static int getSystem(int n2) {
        return n2 >> 26 & 0x3F;
    }

    public static int getSubSystem(int n2) {
        return n2 >> 14 & 0xFFF;
    }

    public static int getCode(int n2) {
        return n2 & 0x3FFF;
    }

    private static String formatMessage(int n2) {
        return "IOReturn error code: " + n2 + " (system=" + IOReturnException.getSystem(n2) + ", subSystem=" + IOReturnException.getSubSystem(n2) + ", code=" + IOReturnException.getCode(n2) + ")";
    }
}


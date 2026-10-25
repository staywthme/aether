/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.unix.solaris;

import com.sun.jna.Native;
import com.sun.jna.platform.unix.solaris.Kstat2;

public class Kstat2StatusException
extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final int kstat2Status;

    public Kstat2StatusException(int n2) {
        int n3 = n2;
        this(n3, Kstat2StatusException.formatMessage(n3));
    }

    protected Kstat2StatusException(int n2, String string) {
        super(string);
        this.kstat2Status = n2;
    }

    public int getKstat2Status() {
        return this.kstat2Status;
    }

    private static String formatMessage(int n2) {
        String string = Kstat2.INSTANCE.kstat2_status_string(n2);
        if (n2 == 10) {
            string = string + " (errno=" + Native.getLastError() + ")";
        }
        return "Kstat2Status error code " + n2 + ": " + string;
    }
}


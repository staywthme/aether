/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna;

import com.sun.jna.IntegerType;
import com.sun.jna.Native;

public class NativeLong
extends IntegerType {
    private static final long serialVersionUID = 1L;
    public static final int SIZE = Native.LONG_SIZE;

    public NativeLong() {
        this(0L);
    }

    public NativeLong(long l2) {
        this(l2, false);
    }

    public NativeLong(long l2, boolean bl) {
        super(SIZE, l2, bl);
    }
}


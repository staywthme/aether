/*
 * Decompiled with CFR 0.152.
 */
package org.bridj;

import org.bridj.AbstractIntegral;
import org.bridj.Platform;

public final class SizeT
extends AbstractIntegral {
    public static final int SIZE = Platform.SIZE_T_SIZE;
    public static final SizeT ZERO = new SizeT(0L);
    public static final SizeT ONE = new SizeT(1L);
    private static final long serialVersionUID = 1547942367767922396L;

    public SizeT(long l2) {
        super(l2);
    }

    public final int byteSize() {
        return SIZE;
    }

    public static SizeT valueOf(long l2) {
        if (l2 == 0L) {
            return ZERO;
        }
        if (l2 == 1L) {
            return ONE;
        }
        return new SizeT(l2);
    }
}


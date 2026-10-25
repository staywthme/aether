/*
 * Decompiled with CFR 0.152.
 */
package org.bridj;

public abstract class NativeError
extends Error {
    protected NativeError(String string) {
        super(string);
    }

    static String toHex(long l2) {
        return "0x" + Long.toHexString(l2);
    }
}


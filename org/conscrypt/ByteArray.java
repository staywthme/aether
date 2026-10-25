/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.util.Arrays;

final class ByteArray {
    private final byte[] bytes;
    private final int hashCode;

    ByteArray(byte[] byArray) {
        this.bytes = byArray;
        this.hashCode = Arrays.hashCode(byArray);
    }

    public final int hashCode() {
        return this.hashCode;
    }

    public final boolean equals(Object object) {
        if (!(object instanceof ByteArray)) {
            return false;
        }
        object = (ByteArray)object;
        return Arrays.equals(this.bytes, ((ByteArray)object).bytes);
    }
}


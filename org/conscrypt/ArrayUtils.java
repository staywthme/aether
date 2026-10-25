/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

final class ArrayUtils {
    private ArrayUtils() {
    }

    static void checkOffsetAndCount(int n2, int n3, int n4) {
        if ((n3 | n4) < 0 || n3 > n2 || n2 - n3 < n4) {
            throw new ArrayIndexOutOfBoundsException("length=" + n2 + "; regionStart=" + n3 + "; regionLength=" + n4);
        }
    }
}


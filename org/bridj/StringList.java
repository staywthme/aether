/*
 * Decompiled with CFR 0.152.
 */
package org.bridj;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

class StringList {
    public final ByteBuffer stringsBuffer;
    public final IntBuffer offsetsBuffer;

    public StringList(String[] objectArray) {
        int n2;
        int n3 = objectArray.length;
        byte[][] byArrayArray = new byte[n3][];
        this.offsetsBuffer = ByteBuffer.allocateDirect(n3 << 2).asIntBuffer();
        int n4 = 0;
        for (n2 = 0; n2 < n3; ++n2) {
            this.offsetsBuffer.put(n2, n4);
            byArrayArray[n2] = objectArray[n2].getBytes();
            n4 += byArrayArray[n2].length + 1;
        }
        this.stringsBuffer = ByteBuffer.allocateDirect(n4);
        for (n2 = 0; n2 < n3; ++n2) {
            objectArray = byArrayArray[n2];
            this.stringsBuffer.put((byte[])objectArray);
            this.stringsBuffer.put((byte)0);
        }
    }
}


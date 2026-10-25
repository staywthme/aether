/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.nio.ByteBuffer;
import org.conscrypt.Preconditions;

final class BufferUtils {
    private BufferUtils() {
    }

    static void checkNotNull(ByteBuffer[] byteBufferArray) {
        for (ByteBuffer byteBuffer : byteBufferArray) {
            if (byteBuffer != null) continue;
            throw new IllegalArgumentException("Null buffer in array");
        }
    }

    static long remaining(ByteBuffer[] byteBufferArray) {
        long l2 = 0L;
        for (ByteBuffer byteBuffer : byteBufferArray) {
            l2 += (long)byteBuffer.remaining();
        }
        return l2;
    }

    static void consume(ByteBuffer[] byteBufferArray, int n2) {
        for (ByteBuffer byteBuffer : byteBufferArray) {
            int n3 = Math.min(byteBuffer.remaining(), n2);
            if (n3 <= 0) continue;
            ByteBuffer byteBuffer2 = byteBuffer;
            byteBuffer2.position(byteBuffer2.position() + n3);
            if ((n2 -= n3) == 0) break;
        }
        if (n2 > 0) {
            throw new IllegalArgumentException("toConsume > data size");
        }
    }

    static ByteBuffer getBufferLargerThan(ByteBuffer[] byteBufferArray, int n2) {
        int n3 = byteBufferArray.length;
        for (int i2 = 0; i2 < n3; ++i2) {
            ByteBuffer byteBuffer = byteBufferArray[i2];
            int n4 = byteBuffer.remaining();
            if (n4 <= 0) continue;
            if (n4 >= n2) {
                return byteBuffer;
            }
            for (n2 = i2 + 1; n2 < n3; ++n2) {
                if (byteBufferArray[n2].remaining() <= 0) continue;
                return null;
            }
            return byteBuffer;
        }
        return null;
    }

    static ByteBuffer copyNoConsume(ByteBuffer[] byteBufferArray, ByteBuffer byteBuffer, int n2) {
        Preconditions.checkArgument(byteBuffer.remaining() >= n2, "Destination buffer too small");
        for (ByteBuffer byteBuffer2 : byteBufferArray) {
            int n3 = byteBuffer2.remaining();
            if (n3 <= 0) continue;
            int n4 = byteBuffer2.position();
            if (n3 <= n2) {
                byteBuffer.put(byteBuffer2);
                n2 -= n3;
            } else {
                n3 = byteBuffer2.limit();
                ByteBuffer byteBuffer3 = byteBuffer2;
                byteBuffer3.limit(byteBuffer3.position() + n2);
                byteBuffer.put(byteBuffer2);
                byteBuffer2.limit(n3);
                n2 = 0;
            }
            byteBuffer2.position(n4);
            if (n2 == 0) break;
        }
        byteBuffer.flip();
        return byteBuffer;
    }
}


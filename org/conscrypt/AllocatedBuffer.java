/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.nio.ByteBuffer;
import org.conscrypt.Preconditions;

public abstract class AllocatedBuffer {
    public abstract ByteBuffer nioBuffer();

    @Deprecated
    public AllocatedBuffer retain() {
        return this;
    }

    public abstract AllocatedBuffer release();

    public static AllocatedBuffer wrap(final ByteBuffer byteBuffer) {
        Preconditions.checkNotNull(byteBuffer, "buffer");
        return new AllocatedBuffer(){

            @Override
            public ByteBuffer nioBuffer() {
                return byteBuffer;
            }

            @Override
            public AllocatedBuffer release() {
                return this;
            }
        };
    }
}


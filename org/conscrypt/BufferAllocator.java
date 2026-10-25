/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.nio.ByteBuffer;
import org.conscrypt.AllocatedBuffer;

public abstract class BufferAllocator {
    private static final BufferAllocator UNPOOLED = new BufferAllocator(){

        @Override
        public AllocatedBuffer allocateDirectBuffer(int n2) {
            return AllocatedBuffer.wrap(ByteBuffer.allocateDirect(n2));
        }
    };

    public static BufferAllocator unpooled() {
        return UNPOOLED;
    }

    public abstract AllocatedBuffer allocateDirectBuffer(int var1);
}


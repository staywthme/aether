/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.WeakMemoryHolder;
import com.sun.jna.internal.Cleaner;
import java.io.Closeable;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Memory
extends Pointer
implements Closeable {
    private static final Map<Long, Reference<Memory>> allocatedMemory = new ConcurrentHashMap<Long, Reference<Memory>>();
    private static final WeakMemoryHolder buffers = new WeakMemoryHolder();
    private final Cleaner.Cleanable cleanable;
    protected long size;

    public static void purge() {
        buffers.clean();
    }

    public static void disposeAll() {
        Object object = new ArrayList<Reference<Memory>>(allocatedMemory.values());
        object = object.iterator();
        while (object.hasNext()) {
            Object object2 = (Reference)object.next();
            if ((object2 = (Memory)((Reference)object2).get()) == null) continue;
            ((Memory)object2).close();
        }
    }

    public Memory(long l2) {
        this.size = l2;
        if (l2 <= 0L) {
            throw new IllegalArgumentException("Allocation size must be greater than zero");
        }
        this.peer = Memory.malloc(l2);
        if (this.peer == 0L) {
            throw new OutOfMemoryError("Cannot allocate " + l2 + " bytes");
        }
        allocatedMemory.put(this.peer, new WeakReference<Memory>(this));
        this.cleanable = Cleaner.getCleaner().register(this, new MemoryDisposer(this.peer));
    }

    protected Memory() {
        this.cleanable = null;
    }

    @Override
    public Pointer share(long l2) {
        return ((Pointer)this).share(l2, this.size() - l2);
    }

    @Override
    public Pointer share(long l2, long l3) {
        this.boundsCheck(l2, l3);
        return new SharedMemory(l2, l3);
    }

    public Memory align(int n2) {
        if (n2 <= 0) {
            throw new IllegalArgumentException("Byte boundary must be positive: " + n2);
        }
        for (int i2 = 0; i2 < 32; ++i2) {
            if (n2 != 1 << i2) continue;
            long l2 = (long)n2 - 1L ^ 0xFFFFFFFFFFFFFFFFL;
            if ((this.peer & l2) != this.peer) {
                long l3 = this.peer + (long)n2 - 1L & l2;
                long l4 = this.peer + this.size - l3;
                if (l4 <= 0L) {
                    throw new IllegalArgumentException("Insufficient memory to align to the requested boundary");
                }
                return (Memory)((Pointer)this).share(l3 - this.peer, l4);
            }
            return this;
        }
        throw new IllegalArgumentException("Byte boundary must be a power of two");
    }

    @Override
    public void close() {
        this.peer = 0L;
        if (this.cleanable != null) {
            this.cleanable.clean();
        }
    }

    @Deprecated
    protected void dispose() {
        this.close();
    }

    public void clear() {
        Memory memory = this;
        memory.clear(memory.size);
    }

    public boolean valid() {
        return this.peer != 0L;
    }

    public long size() {
        return this.size;
    }

    protected void boundsCheck(long l2, long l3) {
        if (l2 < 0L) {
            throw new IndexOutOfBoundsException("Invalid offset: " + l2);
        }
        if (l2 + l3 > this.size) {
            String string = "Bounds exceeds available space : size=" + this.size + ", offset=" + (l2 + l3);
            throw new IndexOutOfBoundsException(string);
        }
    }

    @Override
    public void read(long l2, byte[] byArray, int n2, int n3) {
        this.boundsCheck(l2, n3);
        super.read(l2, byArray, n2, n3);
    }

    @Override
    public void read(long l2, short[] sArray, int n2, int n3) {
        this.boundsCheck(l2, (long)n3 << 1);
        super.read(l2, sArray, n2, n3);
    }

    @Override
    public void read(long l2, char[] cArray, int n2, int n3) {
        this.boundsCheck(l2, n3 * Native.WCHAR_SIZE);
        super.read(l2, cArray, n2, n3);
    }

    @Override
    public void read(long l2, int[] nArray, int n2, int n3) {
        this.boundsCheck(l2, (long)n3 << 2);
        super.read(l2, nArray, n2, n3);
    }

    @Override
    public void read(long l2, long[] lArray, int n2, int n3) {
        this.boundsCheck(l2, (long)n3 << 3);
        super.read(l2, lArray, n2, n3);
    }

    @Override
    public void read(long l2, float[] fArray, int n2, int n3) {
        this.boundsCheck(l2, (long)n3 << 2);
        super.read(l2, fArray, n2, n3);
    }

    @Override
    public void read(long l2, double[] dArray, int n2, int n3) {
        this.boundsCheck(l2, (long)n3 << 3);
        super.read(l2, dArray, n2, n3);
    }

    @Override
    public void read(long l2, Pointer[] pointerArray, int n2, int n3) {
        this.boundsCheck(l2, n3 * Native.POINTER_SIZE);
        super.read(l2, pointerArray, n2, n3);
    }

    @Override
    public void write(long l2, byte[] byArray, int n2, int n3) {
        this.boundsCheck(l2, n3);
        super.write(l2, byArray, n2, n3);
    }

    @Override
    public void write(long l2, short[] sArray, int n2, int n3) {
        this.boundsCheck(l2, (long)n3 << 1);
        super.write(l2, sArray, n2, n3);
    }

    @Override
    public void write(long l2, char[] cArray, int n2, int n3) {
        this.boundsCheck(l2, n3 * Native.WCHAR_SIZE);
        super.write(l2, cArray, n2, n3);
    }

    @Override
    public void write(long l2, int[] nArray, int n2, int n3) {
        this.boundsCheck(l2, (long)n3 << 2);
        super.write(l2, nArray, n2, n3);
    }

    @Override
    public void write(long l2, long[] lArray, int n2, int n3) {
        this.boundsCheck(l2, (long)n3 << 3);
        super.write(l2, lArray, n2, n3);
    }

    @Override
    public void write(long l2, float[] fArray, int n2, int n3) {
        this.boundsCheck(l2, (long)n3 << 2);
        super.write(l2, fArray, n2, n3);
    }

    @Override
    public void write(long l2, double[] dArray, int n2, int n3) {
        this.boundsCheck(l2, (long)n3 << 3);
        super.write(l2, dArray, n2, n3);
    }

    @Override
    public void write(long l2, Pointer[] pointerArray, int n2, int n3) {
        this.boundsCheck(l2, n3 * Native.POINTER_SIZE);
        super.write(l2, pointerArray, n2, n3);
    }

    @Override
    public byte getByte(long l2) {
        this.boundsCheck(l2, 1L);
        return super.getByte(l2);
    }

    @Override
    public char getChar(long l2) {
        this.boundsCheck(l2, Native.WCHAR_SIZE);
        return super.getChar(l2);
    }

    @Override
    public short getShort(long l2) {
        this.boundsCheck(l2, 2L);
        return super.getShort(l2);
    }

    @Override
    public int getInt(long l2) {
        this.boundsCheck(l2, 4L);
        return super.getInt(l2);
    }

    @Override
    public long getLong(long l2) {
        this.boundsCheck(l2, 8L);
        return super.getLong(l2);
    }

    @Override
    public float getFloat(long l2) {
        this.boundsCheck(l2, 4L);
        return super.getFloat(l2);
    }

    @Override
    public double getDouble(long l2) {
        this.boundsCheck(l2, 8L);
        return super.getDouble(l2);
    }

    @Override
    public Pointer getPointer(long l2) {
        this.boundsCheck(l2, Native.POINTER_SIZE);
        Memory memory = this;
        return memory.shareReferenceIfInBounds(super.getPointer(l2));
    }

    @Override
    public ByteBuffer getByteBuffer(long l2, long l3) {
        this.boundsCheck(l2, l3);
        ByteBuffer byteBuffer = super.getByteBuffer(l2, l3);
        buffers.put(byteBuffer, this);
        return byteBuffer;
    }

    @Override
    public String getString(long l2, String string) {
        this.boundsCheck(l2, 0L);
        return super.getString(l2, string);
    }

    @Override
    public String getWideString(long l2) {
        this.boundsCheck(l2, 0L);
        return super.getWideString(l2);
    }

    @Override
    public void setByte(long l2, byte by) {
        this.boundsCheck(l2, 1L);
        super.setByte(l2, by);
    }

    @Override
    public void setChar(long l2, char c2) {
        this.boundsCheck(l2, Native.WCHAR_SIZE);
        super.setChar(l2, c2);
    }

    @Override
    public void setShort(long l2, short s2) {
        this.boundsCheck(l2, 2L);
        super.setShort(l2, s2);
    }

    @Override
    public void setInt(long l2, int n2) {
        this.boundsCheck(l2, 4L);
        super.setInt(l2, n2);
    }

    @Override
    public void setLong(long l2, long l3) {
        this.boundsCheck(l2, 8L);
        super.setLong(l2, l3);
    }

    @Override
    public void setFloat(long l2, float f2) {
        this.boundsCheck(l2, 4L);
        super.setFloat(l2, f2);
    }

    @Override
    public void setDouble(long l2, double d2) {
        this.boundsCheck(l2, 8L);
        super.setDouble(l2, d2);
    }

    @Override
    public void setPointer(long l2, Pointer pointer) {
        this.boundsCheck(l2, Native.POINTER_SIZE);
        super.setPointer(l2, pointer);
    }

    @Override
    public void setString(long l2, String string, String string2) {
        this.boundsCheck(l2, (long)Native.getBytes(string, string2).length + 1L);
        super.setString(l2, string, string2);
    }

    @Override
    public void setWideString(long l2, String string) {
        this.boundsCheck(l2, ((long)string.length() + 1L) * (long)Native.WCHAR_SIZE);
        super.setWideString(l2, string);
    }

    @Override
    public String toString() {
        return "allocated@0x" + Long.toHexString(this.peer) + " (" + this.size + " bytes)";
    }

    protected static void free(long l2) {
        if (l2 != 0L) {
            Native.free(l2);
        }
    }

    protected static long malloc(long l2) {
        return Native.malloc(l2);
    }

    public String dump() {
        return this.dump(0L, (int)this.size());
    }

    private Pointer shareReferenceIfInBounds(Pointer pointer) {
        if (pointer == null) {
            return null;
        }
        long l2 = pointer.peer - this.peer;
        if (l2 >= 0L && l2 < this.size) {
            return ((Pointer)this).share(l2);
        }
        return pointer;
    }

    static final class MemoryDisposer
    implements Runnable {
        private long peer;

        public MemoryDisposer(long l2) {
            this.peer = l2;
        }

        @Override
        public final synchronized void run() {
            try {
                Memory.free(this.peer);
                return;
            }
            finally {
                allocatedMemory.remove(this.peer);
                this.peer = 0L;
            }
        }
    }

    class SharedMemory
    extends Memory {
        public SharedMemory(long l2, long l3) {
            this.size = l3;
            this.peer = Memory.this.peer + l2;
        }

        @Override
        protected synchronized void dispose() {
            this.peer = 0L;
        }

        @Override
        protected void boundsCheck(long l2, long l3) {
            Memory.this.boundsCheck(this.peer - Memory.this.peer + l2, l3);
        }

        @Override
        public String toString() {
            return super.toString() + " (shared from " + ((Pointer)Memory.this).toString() + ")";
        }
    }
}


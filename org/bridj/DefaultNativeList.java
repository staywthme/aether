/*
 * Decompiled with CFR 0.152.
 */
package org.bridj;

import java.util.AbstractList;
import java.util.Collection;
import java.util.RandomAccess;
import org.bridj.NativeList;
import org.bridj.Pointer;
import org.bridj.PointerIO;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
class DefaultNativeList<T>
extends AbstractList<T>
implements RandomAccess,
NativeList<T> {
    final Pointer.ListType type;
    final PointerIO<T> io;
    volatile Pointer<T> pointer;
    volatile long size;

    @Override
    public Pointer<?> getPointer() {
        return this.pointer;
    }

    DefaultNativeList(Pointer<T> pointer, Pointer.ListType listType) {
        if (pointer == null || listType == null) {
            throw new IllegalArgumentException("Cannot build a " + this.getClass().getSimpleName() + " with " + pointer + " and " + (Object)((Object)listType));
        }
        this.io = pointer.getIO("Cannot create a list out of untyped pointer " + pointer);
        this.type = listType;
        this.size = pointer.getValidElements();
        this.pointer = pointer;
    }

    protected void checkModifiable() {
        if (this.type == Pointer.ListType.Unmodifiable) {
            throw new UnsupportedOperationException("This list is unmodifiable");
        }
    }

    protected int safelyCastLongToInt(long l2, String string) {
        if (l2 > Integer.MAX_VALUE) {
            throw new RuntimeException(string + " is bigger than Java int's maximum value : " + l2);
        }
        return (int)l2;
    }

    @Override
    public int size() {
        DefaultNativeList defaultNativeList = this;
        return defaultNativeList.safelyCastLongToInt(defaultNativeList.size, "Size of the native list");
    }

    @Override
    public void clear() {
        this.checkModifiable();
        this.size = 0L;
    }

    @Override
    public T get(int n2) {
        if ((long)n2 >= this.size || n2 < 0) {
            throw new IndexOutOfBoundsException("Invalid index : " + n2 + " (list has size " + this.size + ")");
        }
        return this.pointer.get(n2);
    }

    @Override
    public T set(int n2, T t2) {
        this.checkModifiable();
        if ((long)n2 >= this.size || n2 < 0) {
            throw new IndexOutOfBoundsException("Invalid index : " + n2 + " (list has size " + this.size + ")");
        }
        T t3 = this.pointer.get(n2);
        this.pointer.set(n2, t2);
        return t3;
    }

    void add(long l2, T t2) {
        this.checkModifiable();
        if (l2 > this.size || l2 < 0L) {
            throw new IndexOutOfBoundsException("Invalid index : " + l2 + " (list has size " + this.size + ")");
        }
        DefaultNativeList defaultNativeList = this;
        defaultNativeList.requireSize(defaultNativeList.size + 1L);
        if (l2 < this.size) {
            this.pointer.moveBytesAtOffsetTo(l2, this.pointer, l2 + 1L, this.size - l2);
        }
        this.pointer.set(l2, t2);
        ++this.size;
    }

    @Override
    public void add(int n2, T t2) {
        this.add((long)n2, t2);
    }

    protected void requireSize(long l2) {
        if (l2 > this.pointer.getValidElements()) {
            switch (this.type) {
                case Dynamic: {
                    long l3 = l2 < 5L ? l2 + 1L : (long)((double)l2 * 1.6);
                    Pointer<T> pointer = Pointer.allocateArray(this.io, l3);
                    this.pointer.copyTo(pointer);
                    this.pointer = pointer;
                    return;
                }
                case FixedCapacity: {
                    throw new UnsupportedOperationException("This list has a fixed capacity, cannot grow its storage");
                }
                case Unmodifiable: {
                    this.checkModifiable();
                }
            }
        }
    }

    T remove(long l2) {
        this.checkModifiable();
        if (l2 >= this.size || l2 < 0L) {
            throw new IndexOutOfBoundsException("Invalid index : " + l2 + " (list has size " + this.size + ")");
        }
        T t2 = this.pointer.get(l2);
        long l3 = this.io.getTargetSize();
        this.pointer.moveBytesAtOffsetTo((l2 + 1L) * l3, this.pointer, l2 * l3, l3);
        --this.size;
        return t2;
    }

    @Override
    public T remove(int n2) {
        return this.remove((long)n2);
    }

    @Override
    public boolean remove(Object object) {
        this.checkModifiable();
        long l2 = this.indexOf(object, true, 0);
        if (l2 < 0L) {
            return false;
        }
        this.remove(l2);
        return true;
    }

    long indexOf(Object pointer, boolean bl, int n2) {
        Pointer<T> pointer2 = this.pointer;
        assert (n2 >= 0 && (bl || n2 > 0));
        if (n2 > 0) {
            pointer2 = pointer2.next(n2);
        }
        Pointer<Pointer<T>> pointer3 = Pointer.allocate(this.io);
        pointer3.set(pointer);
        pointer = bl ? pointer2.findLast(pointer3) : pointer2.find(pointer3);
        if (pointer == null) {
            return -1L;
        }
        return pointer.getPeer() - pointer2.getPeer();
    }

    @Override
    public int indexOf(Object object) {
        DefaultNativeList defaultNativeList = this;
        return defaultNativeList.safelyCastLongToInt(defaultNativeList.indexOf(object, false, 0), "Index of the object");
    }

    @Override
    public int lastIndexOf(Object object) {
        DefaultNativeList defaultNativeList = this;
        return defaultNativeList.safelyCastLongToInt(defaultNativeList.indexOf(object, true, 0), "Last index of the object");
    }

    @Override
    public boolean contains(Object object) {
        return this.indexOf(object) >= 0;
    }

    @Override
    public boolean addAll(int n2, Collection<? extends T> collection) {
        if (n2 >= 0 && (long)n2 < this.size) {
            DefaultNativeList defaultNativeList = this;
            defaultNativeList.requireSize(defaultNativeList.size + (long)collection.size());
        }
        return super.addAll(n2, collection);
    }

    @Override
    public Object[] toArray() {
        return this.pointer.validElements(this.size).toArray();
    }

    @Override
    public <T> T[] toArray(T[] TArray) {
        return this.pointer.validElements(this.size).toArray(TArray);
    }
}


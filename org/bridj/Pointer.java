/*
 * Decompiled with CFR 0.152.
 */
package org.bridj;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import org.bridj.AbstractIntegral;
import org.bridj.BridJ;
import org.bridj.CLong;
import org.bridj.CRuntime;
import org.bridj.CommonPointerIOs;
import org.bridj.DefaultNativeList;
import org.bridj.DynamicCallback;
import org.bridj.DynamicFunction;
import org.bridj.FlagSet;
import org.bridj.IntValuedEnum;
import org.bridj.JNI;
import org.bridj.MethodCallInfo;
import org.bridj.NativeList;
import org.bridj.NativeObject;
import org.bridj.NativeObjectInterface;
import org.bridj.Platform;
import org.bridj.PointerIO;
import org.bridj.PointerLRUCache;
import org.bridj.SizeT;
import org.bridj.TypedPointer;
import org.bridj.ann.Convention;
import org.bridj.util.DefaultParameterizedType;
import org.bridj.util.Utils;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public abstract class Pointer<T>
implements Comparable<Pointer<?>>,
Iterable<T> {
    public static final Pointer<?> NULL = null;
    public static final int SIZE = Platform.POINTER_SIZE;
    protected static final long UNKNOWN_VALIDITY = -1L;
    protected static final long NO_PARENT = 0L;
    private static final long POINTER_MASK;
    public static final int defaultAlignment;
    protected final PointerIO<T> io;
    private final long peer_;
    protected final long offsetInParent;
    protected final Pointer<?> parent;
    protected volatile Object sibling;
    protected final long validStart;
    protected final long validEnd;
    Throwable creationTrace;
    Throwable deletionTrace;
    Throwable releaseTrace;
    private static final int LRU_POINTER_CACHE_SIZE = 8;
    private static final int LRU_POINTER_CACHE_TOLERANCE = 1;
    private static final ThreadLocal<PointerLRUCache> localCachedPointers;
    static Releaser freeReleaser;

    Pointer(PointerIO<T> pointerIO, long l2, long l3, long l4, Pointer<?> pointer, long l5, Object object) {
        this.io = pointerIO;
        this.peer_ = l2;
        this.validStart = l3;
        this.validEnd = l4;
        this.parent = pointer;
        this.offsetInParent = l5;
        this.sibling = object;
        if (l2 == 0L) {
            throw new IllegalArgumentException("Pointer instance cannot have NULL peer ! (use null Pointer instead)");
        }
        if (BridJ.debugPointers) {
            this.creationTrace = new RuntimeException().fillInStackTrace();
        }
    }

    public static Type pointerType(Type type) {
        return DefaultParameterizedType.paramType(Pointer.class, new Type[]{type});
    }

    public static <E extends Enum<E>> Type intEnumType(Class<? extends IntValuedEnum<E>> clazz) {
        return DefaultParameterizedType.paramType(IntValuedEnum.class, new Type[]{clazz});
    }

    public synchronized void release() {
        Object object = this.sibling;
        this.sibling = null;
        if (object instanceof Pointer) {
            ((Pointer)object).release();
        }
        if (BridJ.debugPointerReleases) {
            this.releaseTrace = new RuntimeException().fillInStackTrace();
        }
    }

    @Override
    public int compareTo(Pointer<?> pointer) {
        long l2;
        if (pointer == null) {
            return 1;
        }
        long l3 = this.getPeer();
        if (l3 == (l2 = pointer.getPeer())) {
            return 0;
        }
        if (l3 < l2) {
            return -1;
        }
        return 1;
    }

    public int compareBytes(Pointer<?> pointer, long l2) {
        return this.compareBytesAtOffset(0L, pointer, 0L, l2);
    }

    public int compareBytesAtOffset(long l2, Pointer<?> pointer, long l3, long l4) {
        long l5 = this.getPeer() + l2;
        if (this.validStart != -1L && (l5 < this.validStart || l5 + l4 > this.validEnd)) {
            this.invalidPeer(l5, l4);
        }
        return JNI.memcmp(l5, super.getCheckedPeer(l3, l4), l4);
    }

    public int hashCode() {
        int n2 = new Long(this.getPeer()).hashCode();
        return n2;
    }

    public String toString() {
        return "Pointer(peer = 0x" + Long.toHexString(this.getPeer()) + ", targetType = " + Utils.toString(this.getTargetType()) + ", order = " + this.order() + ")";
    }

    protected final void invalidPeer(long l2, long l3) {
        throw new IndexOutOfBoundsException("Cannot access to memory data of length " + l3 + " at offset " + (l2 - this.getPeer()) + " : valid memory start is " + this.validStart + ", valid memory size is " + (this.validEnd - this.validStart));
    }

    private final long getCheckedPeer(long l2, long l3) {
        long l4 = this.getPeer() + l2;
        if (this.validStart != -1L && (l4 < this.validStart || l4 + l3 > this.validEnd)) {
            this.invalidPeer(l4, l3);
        }
        return l4;
    }

    public Pointer<T> offset(long l2) {
        return this.offset(l2, this.getIO());
    }

    <U> Pointer<U> offset(long l2, PointerIO<U> pointerIO) {
        Object object;
        if (l2 == 0L) {
            if (pointerIO == this.io) {
                return this;
            }
            return this.as(pointerIO);
        }
        long l3 = this.getPeer() + l2;
        Object object2 = object = this.getSibling() != null ? this.getSibling() : this;
        if (this.validStart == -1L) {
            return Pointer.newPointer(pointerIO, l3, this.isOrdered(), -1L, -1L, null, 0L, null, object);
        }
        if (l3 > this.validEnd || l3 < this.validStart) {
            throw new IndexOutOfBoundsException("Invalid pointer offset : " + l2 + " (validBytes = " + this.getValidBytes() + ") !");
        }
        return Pointer.newPointer(pointerIO, l3, this.isOrdered(), this.validStart, this.validEnd, null, 0L, null, object);
    }

    public Pointer<T> validBytes(long l2) {
        long l3 = this.getPeer();
        long l4 = l3 + l2;
        if (this.validStart == l3 && this.validEnd == l4) {
            return this;
        }
        if (this.validEnd != -1L && l4 > this.validEnd) {
            throw new IndexOutOfBoundsException("Cannot extend validity of pointed memory from " + this.validEnd + " to " + l4);
        }
        Object object = this.getSibling() != null ? this.getSibling() : this;
        return Pointer.newPointer(this.getIO(), l3, this.isOrdered(), this.validStart, l4, this.parent, this.offsetInParent, null, object);
    }

    @Deprecated
    public Pointer<T> withoutValidityInformation() {
        long l2 = this.getPeer();
        if (this.validStart == -1L) {
            return this;
        }
        Object object = this.getSibling() != null ? this.getSibling() : this;
        return Pointer.newPointer(this.getIO(), l2, this.isOrdered(), -1L, -1L, this.parent, this.offsetInParent, null, object);
    }

    public Pointer<T> clone() {
        long l2 = this.getValidElements();
        if (l2 < 0L) {
            throw new UnsupportedOperationException("Number of bytes unknown, unable to clone memory (use validBytes(long))");
        }
        Pointer<T> pointer = Pointer.allocateArray(this.getIO(), l2);
        this.copyTo(pointer);
        return pointer;
    }

    public Pointer<T> validElements(long l2) {
        return this.validBytes(l2 * this.getIO("Cannot define elements validity").getTargetSize());
    }

    public Pointer<Pointer<T>> getReference() {
        if (this.parent == null) {
            throw new UnsupportedOperationException("Cannot get reference to this pointer, it wasn't created from Pointer.getPointer(offset) or from a similar method.");
        }
        PointerIO<T> pointerIO = this.getIO();
        return this.parent.offset(this.offsetInParent).as(pointerIO == null ? null : pointerIO.getReferenceIO());
    }

    public final long getPeer() {
        if (BridJ.debugPointerReleases && this.releaseTrace != null) {
            throw new RuntimeException("Pointer was released here:\n\t" + Utils.toString(this.releaseTrace).replaceAll("\n", "\n\t"));
        }
        return this.peer_;
    }

    public static <R> Pointer<DynamicFunction<R>> allocateDynamicCallback(DynamicCallback<R> dynamicCallback, Convention.Style style, Type type, Type ... typeArray) {
        if (dynamicCallback == null) {
            throw new IllegalArgumentException("Java callback handler cannot be null !");
        }
        if (type == null) {
            throw new IllegalArgumentException("Callback return type cannot be null !");
        }
        if (typeArray == null) {
            throw new IllegalArgumentException("Invalid (null) list of parameter types !");
        }
        try {
            MethodCallInfo methodCallInfo = new MethodCallInfo(type, typeArray, false);
            Method method = DynamicCallback.class.getMethod("apply", Object[].class);
            methodCallInfo.setMethod(method);
            methodCallInfo.setJavaSignature("([Ljava/lang/Object;)Ljava/lang/Object;");
            methodCallInfo.setCallingConvention(style);
            methodCallInfo.setGenericCallback(true);
            methodCallInfo.setJavaCallback(dynamicCallback);
            return CRuntime.createCToJavaCallback(methodCallInfo, DynamicCallback.class);
        }
        catch (Exception exception) {
            throw new RuntimeException("Failed to allocate dynamic callback for convention " + (Object)((Object)style) + ", return type " + Utils.toString(type) + " and parameter types " + Arrays.asList(typeArray) + " : " + exception, exception);
        }
    }

    public <U> Pointer<U> as(PointerIO<U> pointerIO) {
        Pointer pointer = this;
        return pointer.viewAs(pointer.isOrdered(), pointerIO);
    }

    public Pointer<T> order(ByteOrder byteOrder) {
        if (byteOrder.equals(ByteOrder.nativeOrder()) == this.isOrdered()) {
            return this;
        }
        Pointer pointer = this;
        return pointer.viewAs(!pointer.isOrdered(), this.getIO());
    }

    public ByteOrder order() {
        ByteOrder byteOrder = this.isOrdered() ? ByteOrder.nativeOrder() : (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN);
        return byteOrder;
    }

    <U> Pointer<U> viewAs(boolean bl, PointerIO<U> pointerIO) {
        if (pointerIO == this.io && bl == this.isOrdered()) {
            return this;
        }
        return Pointer.newPointer(pointerIO, this.getPeer(), bl, this.getValidStart(), this.getValidEnd(), this.getParent(), this.getOffsetInParent(), null, this.getSibling() != null ? this.getSibling() : this);
    }

    public final PointerIO<T> getIO() {
        return this.io;
    }

    public abstract boolean isOrdered();

    final long getOffsetInParent() {
        return this.offsetInParent;
    }

    final Pointer<?> getParent() {
        return this.parent;
    }

    final Object getSibling() {
        return this.sibling;
    }

    final long getValidEnd() {
        return this.validEnd;
    }

    final long getValidStart() {
        return this.validStart;
    }

    public <U> Pointer<U> as(Type object) {
        object = PointerIO.getInstance((Type)object);
        return this.as((PointerIO<U>)object);
    }

    public <U> Pointer<U> as(Class<U> clazz) {
        return this.as((Type)clazz);
    }

    public <R> DynamicFunction<R> asDynamicFunction(Convention.Style style, Type type, Type ... typeArray) {
        return CRuntime.getInstance().getDynamicFunctionFactory(null, style, type, typeArray).newInstance(this);
    }

    public Pointer<?> asUntyped() {
        return this.as((Class<U>)null);
    }

    public long getValidBytes() {
        long l2 = this.getValidEnd();
        if (l2 == -1L) {
            return -1L;
        }
        return l2 - this.getPeer();
    }

    public long getValidElements() {
        long l2 = this.getValidBytes();
        long l3 = this.getTargetSize();
        if (l2 < 0L || l3 <= 0L) {
            return -1L;
        }
        return l2 / l3;
    }

    @Override
    public ListIterator<T> iterator() {
        return new ListIterator<T>(){
            Pointer<T> next;
            Pointer<T> previous;
            {
                this.next = Pointer.this.getValidElements() != 0L ? Pointer.this : null;
            }

            @Override
            public T next() {
                if (this.next == null) {
                    throw new NoSuchElementException();
                }
                Object t2 = this.next.get();
                this.previous = this.next;
                long l2 = this.next.getValidElements();
                this.next = l2 < 0L || l2 > 1L ? this.next.next(1L) : null;
                return t2;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean hasNext() {
                long l2;
                return this.next != null && ((l2 = this.next.getValidBytes()) < 0L || l2 > 0L);
            }

            @Override
            public void add(T t2) {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean hasPrevious() {
                return this.previous != null;
            }

            @Override
            public int nextIndex() {
                throw new UnsupportedOperationException();
            }

            @Override
            public T previous() {
                throw new UnsupportedOperationException();
            }

            @Override
            public int previousIndex() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void set(T t2) {
                if (this.previous == null) {
                    throw new NoSuchElementException("You haven't called next() prior to calling ListIterator.set(E)");
                }
                this.previous.set(t2);
            }
        };
    }

    public static <E extends Enum<E>> Pointer<IntValuedEnum<E>> pointerToEnum(IntValuedEnum<E> intValuedEnum) {
        Pointer<IntValuedEnum<E>> pointer;
        if (intValuedEnum instanceof FlagSet) {
            pointer = ((FlagSet)((Object)intValuedEnum)).getEnumClass();
        } else if (intValuedEnum instanceof Enum) {
            pointer = intValuedEnum.getClass();
        } else {
            throw new RuntimeException("Expected a FlagSet or an Enum, got " + intValuedEnum);
        }
        pointer = PointerIO.getInstance(DefaultParameterizedType.paramType(IntValuedEnum.class, new Type[]{pointer}));
        pointer = Pointer.allocate(pointer);
        pointer.setInt((int)intValuedEnum.value());
        return pointer;
    }

    @Deprecated
    public static <N extends NativeObject> Pointer<N> pointerTo(N n2) {
        return Pointer.getPointer(n2);
    }

    public static <N extends NativeObject> Pointer<N> getPointer(N n2) {
        return Pointer.getPointer(n2, null);
    }

    public static <N extends NativeObjectInterface> Pointer<N> getPointer(N n2) {
        return Pointer.getPointer((NativeObject)n2);
    }

    public static <R extends NativeObject> Pointer<R> getPointer(NativeObject nativeObject, Type type) {
        if (nativeObject == null) {
            return null;
        }
        return nativeObject.peer;
    }

    public static long getAddress(NativeObject nativeObject, Class clazz) {
        return Pointer.getPeer(Pointer.getPointer(nativeObject, clazz));
    }

    public <O extends NativeObject> O getNativeObjectAtOffset(long l2, Type type) {
        return (O)BridJ.createNativeObjectFromPointer(l2 == 0L ? this : this.offset(l2), type);
    }

    public <O extends NativeObject> Pointer<T> setNativeObject(O o2, Type type) {
        BridJ.copyNativeObjectToAddress(o2, type, this);
        return this;
    }

    public <O extends NativeObject> O getNativeObjectAtOffset(long l2, Class<O> clazz) {
        return this.getNativeObjectAtOffset(l2, (Type)clazz);
    }

    public <O extends NativeObject> O getNativeObject(Class<O> clazz) {
        return this.getNativeObject((Type)clazz);
    }

    public <O extends NativeObject> O getNativeObject(Type type) {
        type = this.getNativeObjectAtOffset(0L, type);
        return (O)type;
    }

    public boolean isAligned() {
        Pointer pointer = this;
        return pointer.isAligned(pointer.getIO("Cannot check alignment").getTargetAlignment());
    }

    public boolean isAligned(long l2) {
        return Pointer.isAligned(this.getPeer(), l2);
    }

    protected static boolean isAligned(long l2, long l3) {
        return Pointer.computeRemainder(l2, l3) == 0;
    }

    protected static int computeRemainder(long l2, long l3) {
        switch ((int)l3) {
            case -1: 
            case 0: 
            case 1: {
                return 0;
            }
            case 2: {
                return (int)(l2 & 1L);
            }
            case 4: {
                return (int)(l2 & 3L);
            }
            case 8: {
                return (int)(l2 & 7L);
            }
            case 16: {
                return (int)(l2 & 0xFL);
            }
            case 32: {
                return (int)(l2 & 0x1FL);
            }
            case 64: {
                return (int)(l2 & 0x3FL);
            }
        }
        if (l3 < 0L) {
            return 0;
        }
        return (int)(l2 % l3);
    }

    public T get() {
        return this.get(0L);
    }

    public static <T> T get(Pointer<T> pointer) {
        if (pointer == null) {
            return null;
        }
        return pointer.get();
    }

    public T get(long l2) {
        return this.getIO("Cannot get pointed value").get(this, l2);
    }

    public T set(T t2) {
        return this.set(0L, t2);
    }

    private static long getTargetSizeToAllocateArrayOrThrow(PointerIO<?> pointerIO) {
        long l2 = -1L;
        if (pointerIO == null || (l2 = pointerIO.getTargetSize()) < 0L) {
            Pointer.throwBecauseUntyped("Cannot allocate array ");
        }
        return l2;
    }

    private static void throwBecauseUntyped(String string) {
        throw new RuntimeException("Pointer is not typed (call Pointer.as(Type) to create a typed pointer) : " + string);
    }

    static void throwUnexpected(Throwable throwable) {
        throw new RuntimeException("Unexpected error", throwable);
    }

    public T set(long l2, T t2) {
        this.getIO("Cannot set pointed value").set(this, l2, t2);
        return t2;
    }

    public static long getPeer(Pointer<?> pointer) {
        if (pointer == null) {
            return 0L;
        }
        return pointer.getPeer();
    }

    public long getTargetSize() {
        return this.getIO("Cannot compute target size").getTargetSize();
    }

    public Pointer<T> next() {
        return this.next(1L);
    }

    public Pointer<T> next(long l2) {
        Pointer pointer = this;
        return pointer.offset(pointer.getIO("Cannot get pointers to next or previous targets").getTargetSize() * l2);
    }

    public static void release(Pointer ... pointerArray) {
        for (Pointer pointer : pointerArray) {
            if (pointer == null) continue;
            pointer.release();
        }
    }

    public boolean equals(Object object) {
        if (object == null || !(object instanceof Pointer)) {
            return false;
        }
        object = (Pointer)object;
        return this.getPeer() == ((Pointer)object).getPeer();
    }

    @Deprecated
    public static Pointer<?> pointerToAddress(long l2) {
        return Pointer.pointerToAddress(l2, null);
    }

    @Deprecated
    public static Pointer<?> pointerToAddress(long l2, long l3) {
        long l4 = l2;
        return Pointer.newPointer(null, l2, true, l4, l4 + l3, null, 0L, null, null);
    }

    public static <P> Pointer<P> pointerToAddress(long l2, Class<P> clazz, Releaser releaser) {
        return Pointer.pointerToAddress(l2, clazz, releaser);
    }

    public static <P> Pointer<P> pointerToAddress(long l2, Type object, Releaser releaser) {
        object = PointerIO.getInstance((Type)object);
        return Pointer.newPointer(object, l2, true, -1L, -1L, null, -1L, releaser, null);
    }

    public static <P> Pointer<P> pointerToAddress(long l2, PointerIO<P> pointerIO) {
        if (BridJ.cachePointers) {
            return localCachedPointers.get().get(l2, pointerIO);
        }
        return Pointer.pointerToAddress_(l2, pointerIO);
    }

    private static <P> Pointer<P> pointerToAddress_(long l2, PointerIO<P> pointerIO) {
        return Pointer.newPointer(pointerIO, l2, true, -1L, -1L, null, 0L, null, null);
    }

    static <P> Pointer<P> pointerToAddress(long l2, PointerIO<P> pointerIO, Releaser releaser) {
        return Pointer.newPointer(pointerIO, l2, true, -1L, -1L, null, 0L, releaser, null);
    }

    @Deprecated
    public static Pointer<?> pointerToAddress(long l2, Releaser releaser) {
        return Pointer.newPointer(null, l2, true, -1L, -1L, null, 0L, releaser, null);
    }

    public static Pointer<?> pointerToAddress(long l2, long l3, Releaser releaser) {
        long l4 = l2;
        return Pointer.newPointer(null, l2, true, l4, l4 + l3, null, 0L, releaser, null);
    }

    public static <P> Pointer<P> pointerToAddress(long l2, long l3, PointerIO<P> pointerIO, Releaser releaser) {
        long l4 = l2;
        return Pointer.newPointer(pointerIO, l2, true, l4, l4 + l3, null, 0L, releaser, null);
    }

    @Deprecated
    public static <P> Pointer<P> pointerToAddress(long l2, Class<P> clazz) {
        return Pointer.pointerToAddress(l2, clazz);
    }

    @Deprecated
    public static <P> Pointer<P> pointerToAddress(long l2, Type type) {
        return Pointer.newPointer(PointerIO.getInstance(type), l2, true, -1L, -1L, null, -1L, null, null);
    }

    static <U> Pointer<U> pointerToAddress(long l2, long l3, PointerIO<U> pointerIO) {
        long l4 = l2;
        return Pointer.newPointer(pointerIO, l2, true, l4, l4 + l3, null, 0L, null, null);
    }

    static <U> Pointer<U> newPointer(PointerIO<U> pointerIO, long l2, boolean bl, long l3, long l4, Pointer<?> pointer, long l5, final Releaser releaser, Object object) {
        if ((l2 &= POINTER_MASK) == 0L) {
            return null;
        }
        if (l4 != -1L && l4 <= l3) {
            return null;
        }
        if (releaser == null) {
            if (bl) {
                return new OrderedPointer<U>(pointerIO, l2, l3, l4, pointer, l5, object);
            }
            return new DisorderedPointer<U>(pointerIO, l2, l3, l4, pointer, l5, object);
        }
        assert (object == null);
        if (bl) {
            return new OrderedPointer<U>(pointerIO, l2, l3, l4, pointer, l5, object){
                private volatile Releaser rel;
                {
                    super(pointerIO, l2, l3, l4, pointer, l5, object);
                    this.rel = releaser;
                }

                @Override
                public final synchronized void release() {
                    if (this.rel != null) {
                        Releaser releaser2 = this.rel;
                        this.rel = null;
                        releaser2.release(this);
                    }
                    if (BridJ.debugPointerReleases) {
                        this.releaseTrace = new RuntimeException().fillInStackTrace();
                    }
                }

                protected final void finalize() {
                    ((Pointer)this).release();
                }

                @Override
                @Deprecated
                public final synchronized Pointer<U> withReleaser(final Releaser releaser3) {
                    final Releaser releaser2 = this.rel;
                    this.rel = null;
                    return 3.newPointer(this.getIO(), this.getPeer(), ((Pointer)this).isOrdered(), this.getValidStart(), this.getValidEnd(), null, 0L, releaser3 == null ? releaser2 : new Releaser(){

                        @Override
                        public void release(Pointer<?> pointer) {
                            releaser3.release(pointer);
                            if (releaser2 != null) {
                                releaser2.release(pointer);
                            }
                        }
                    }, null);
                }
            };
        }
        return new DisorderedPointer<U>(pointerIO, l2, l3, l4, pointer, l5, object){
            private volatile Releaser rel;
            {
                super(pointerIO, l2, l3, l4, pointer, l5, object);
                this.rel = releaser;
            }

            @Override
            public final synchronized void release() {
                if (this.rel != null) {
                    Releaser releaser2 = this.rel;
                    this.rel = null;
                    releaser2.release(this);
                }
                if (BridJ.debugPointerReleases) {
                    this.releaseTrace = new RuntimeException().fillInStackTrace();
                }
            }

            protected final void finalize() {
                ((Pointer)this).release();
            }

            @Override
            @Deprecated
            public final synchronized Pointer<U> withReleaser(final Releaser releaser3) {
                final Releaser releaser2 = this.rel;
                this.rel = null;
                return 4.newPointer(this.getIO(), this.getPeer(), ((Pointer)this).isOrdered(), this.getValidStart(), this.getValidEnd(), null, 0L, releaser3 == null ? releaser2 : new Releaser(){

                    @Override
                    public void release(Pointer<?> pointer) {
                        releaser3.release(pointer);
                        if (releaser2 != null) {
                            releaser2.release(pointer);
                        }
                    }
                }, null);
            }
        };
    }

    public static <P extends TypedPointer> Pointer<P> allocateTypedPointer(Class<P> clazz) {
        return Pointer.allocate(PointerIO.getInstance(clazz));
    }

    public static <P extends TypedPointer> Pointer<P> allocateTypedPointers(Class<P> clazz, long l2) {
        return Pointer.allocateArray(PointerIO.getInstance(clazz), l2);
    }

    public static <P> Pointer<Pointer<P>> allocatePointer(Class<P> clazz) {
        return Pointer.allocatePointer(clazz);
    }

    public static <P> Pointer<Pointer<P>> allocatePointer(Type type) {
        return Pointer.allocate(PointerIO.getPointerInstance(type));
    }

    public static <P> Pointer<Pointer<Pointer<P>>> allocatePointerPointer(Type type) {
        return Pointer.allocatePointer(Pointer.pointerType(type));
    }

    public static <P> Pointer<Pointer<Pointer<P>>> allocatePointerPointer(Class<P> clazz) {
        return Pointer.allocatePointerPointer(clazz);
    }

    public static <V> Pointer<Pointer<?>> allocatePointer() {
        return Pointer.allocate(PointerIO.getPointerInstance());
    }

    public static Pointer<Pointer<?>> allocatePointers(int n2) {
        return Pointer.allocateArray(PointerIO.getPointerInstance(), (long)n2);
    }

    public static <P> Pointer<Pointer<P>> allocatePointers(Class<P> clazz, int n2) {
        return Pointer.allocatePointers(clazz, n2);
    }

    public static <P> Pointer<Pointer<P>> allocatePointers(Type type, int n2) {
        return Pointer.allocateArray(PointerIO.getPointerInstance(type), (long)n2);
    }

    public static <V> Pointer<V> allocate(Class<V> clazz) {
        return Pointer.allocate(clazz);
    }

    public static <V> Pointer<V> allocate(Type type) {
        return Pointer.allocateArray(type, 1L);
    }

    public static <V> Pointer<V> allocate(PointerIO<V> pointerIO) {
        PointerIO<V> pointerIO2 = pointerIO;
        return Pointer.allocateBytes(pointerIO2, Pointer.getTargetSizeToAllocateArrayOrThrow(pointerIO2), null);
    }

    public static <V> Pointer<V> allocateArray(PointerIO<V> pointerIO, long l2) {
        PointerIO<V> pointerIO2 = pointerIO;
        return Pointer.allocateBytes(pointerIO2, Pointer.getTargetSizeToAllocateArrayOrThrow(pointerIO2) * l2, null);
    }

    public static <V> Pointer<V> allocateArray(PointerIO<V> pointerIO, long l2, Releaser releaser) {
        PointerIO<V> pointerIO2 = pointerIO;
        return Pointer.allocateBytes(pointerIO2, Pointer.getTargetSizeToAllocateArrayOrThrow(pointerIO2) * l2, releaser);
    }

    public static <V> Pointer<V> allocateBytes(PointerIO<V> pointerIO, long l2, Releaser releaser) {
        return Pointer.allocateAlignedBytes(pointerIO, l2, defaultAlignment, releaser);
    }

    public static <V> Pointer<V> allocateAlignedBytes(PointerIO<V> pointer, long l2, int n2, final Releaser releaser) {
        long l3;
        if (l2 == 0L) {
            return null;
        }
        if (l2 < 0L) {
            throw new IllegalArgumentException("Cannot allocate a negative amount of memory !");
        }
        long l4 = 0L;
        if (n2 <= 1) {
            l3 = JNI.mallocNulled(l2);
        } else {
            l3 = JNI.mallocNulled(l2 + (long)n2 - 1L);
            long l5 = l3 % (long)n2;
            if (l5 > 0L) {
                l4 = (long)n2 - l5;
            }
        }
        if (l3 == 0L) {
            throw new RuntimeException("Failed to allocate " + l2);
        }
        long l6 = l3;
        pointer = Pointer.newPointer(pointer, l3, true, l6, l6 + l2 + l4, null, 0L, releaser == null ? freeReleaser : new Releaser(){

            @Override
            public final void release(Pointer<?> pointer) {
                releaser.release(pointer);
                freeReleaser.release(pointer);
            }
        }, null);
        if (l4 > 0L) {
            pointer = pointer.offset(l4);
        }
        return pointer;
    }

    @Deprecated
    public synchronized Pointer<T> withReleaser(Releaser releaser) {
        return Pointer.newPointer(this.getIO(), this.getPeer(), this.isOrdered(), this.getValidStart(), this.getValidEnd(), null, 0L, releaser, null);
    }

    public static <V> Pointer<V> allocateArray(Class<V> clazz, long l2) {
        return Pointer.allocateArray(clazz, l2);
    }

    public static <V> Pointer<V> allocateArray(Type type, long l2) {
        if (l2 == 0L) {
            return null;
        }
        PointerIO pointerIO = PointerIO.getInstance(type);
        if (pointerIO == null) {
            throw new UnsupportedOperationException("Cannot allocate memory for type " + (type instanceof Class ? ((Class)type).getName() : type.toString()));
        }
        return Pointer.allocateArray(pointerIO, l2);
    }

    public static <V> Pointer<V> allocateAlignedArray(Class<V> clazz, long l2, int n2) {
        return Pointer.allocateAlignedArray(clazz, l2, n2);
    }

    public static <V> Pointer<V> allocateAlignedArray(Type type, long l2, int n2) {
        PointerIO pointerIO = PointerIO.getInstance(type);
        if (pointerIO == null) {
            throw new UnsupportedOperationException("Cannot allocate memory for type " + (type instanceof Class ? ((Class)type).getName() : type.toString()));
        }
        PointerIO pointerIO2 = pointerIO;
        return Pointer.allocateAlignedBytes(pointerIO2, Pointer.getTargetSizeToAllocateArrayOrThrow(pointerIO2) * l2, n2, null);
    }

    public static Pointer<?> pointerToBuffer(Buffer buffer) {
        if (buffer == null) {
            return null;
        }
        if (buffer instanceof IntBuffer) {
            return Pointer.pointerToInts((IntBuffer)buffer);
        }
        if (buffer instanceof LongBuffer) {
            return Pointer.pointerToLongs((LongBuffer)buffer);
        }
        if (buffer instanceof ShortBuffer) {
            return Pointer.pointerToShorts((ShortBuffer)buffer);
        }
        if (buffer instanceof ByteBuffer) {
            return Pointer.pointerToBytes((ByteBuffer)buffer);
        }
        if (buffer instanceof CharBuffer) {
            return Pointer.pointerToChars((CharBuffer)buffer);
        }
        if (buffer instanceof FloatBuffer) {
            return Pointer.pointerToFloats((FloatBuffer)buffer);
        }
        if (buffer instanceof DoubleBuffer) {
            return Pointer.pointerToDoubles((DoubleBuffer)buffer);
        }
        throw new UnsupportedOperationException("Unhandled buffer type : " + buffer.getClass().getName());
    }

    public void updateBuffer(Buffer buffer) {
        if (buffer == null) {
            throw new IllegalArgumentException("Cannot update a null Buffer !");
        }
        if (Utils.isDirect(buffer)) {
            long l2 = JNI.getDirectBufferAddress(buffer);
            if (l2 != this.getPeer()) {
                throw new IllegalArgumentException("Direct buffer does not point to the same location as this Pointer instance, updating it makes no sense !");
            }
            return;
        }
        if (buffer instanceof IntBuffer) {
            ((IntBuffer)buffer).duplicate().put(this.getIntBuffer());
            return;
        }
        if (buffer instanceof LongBuffer) {
            ((LongBuffer)buffer).duplicate().put(this.getLongBuffer());
            return;
        }
        if (buffer instanceof ShortBuffer) {
            ((ShortBuffer)buffer).duplicate().put(this.getShortBuffer());
            return;
        }
        if (buffer instanceof ByteBuffer) {
            ((ByteBuffer)buffer).duplicate().put(this.getByteBuffer());
            return;
        }
        if (buffer instanceof FloatBuffer) {
            ((FloatBuffer)buffer).duplicate().put(this.getFloatBuffer());
            return;
        }
        if (buffer instanceof DoubleBuffer) {
            ((DoubleBuffer)buffer).duplicate().put(this.getDoubleBuffer());
            return;
        }
        throw new UnsupportedOperationException("Unhandled buffer type : " + buffer.getClass().getName());
    }

    public static Pointer<Integer> pointerToInt(int n2) {
        Pointer<Integer> pointer = Pointer.allocate(PointerIO.getIntInstance());
        pointer.setInt(n2);
        return pointer;
    }

    public static Pointer<Integer> pointerToInts(int ... nArray) {
        if (nArray == null) {
            return null;
        }
        Pointer<Integer> pointer = Pointer.allocateArray(PointerIO.getIntInstance(), (long)nArray.length);
        pointer.setIntsAtOffset(0L, nArray, 0, nArray.length);
        return pointer;
    }

    public static Pointer<Pointer<Integer>> pointerToInts(int[][] nArray) {
        if (nArray == null) {
            return null;
        }
        int n2 = nArray.length;
        int n3 = nArray[0].length;
        Pointer<Pointer<Integer>> pointer = Pointer.allocateInts(n2, n3);
        for (int i2 = 0; i2 < n2; ++i2) {
            pointer.setIntsAtOffset((long)(i2 * n3 << 2), nArray[i2], 0, n3);
        }
        return pointer;
    }

    public static Pointer<Pointer<Pointer<Integer>>> pointerToInts(int[][][] nArray) {
        if (nArray == null) {
            return null;
        }
        int n2 = nArray.length;
        int n3 = nArray[0].length;
        int n4 = nArray[0][0].length;
        Pointer<Pointer<Pointer<Integer>>> pointer = Pointer.allocateInts(n2, n3, n4);
        for (int i2 = 0; i2 < n2; ++i2) {
            int n5 = i2 * n3;
            for (int i3 = 0; i3 < n3; ++i3) {
                int n6 = (n5 + i3) * n4;
                pointer.setIntsAtOffset((long)(n6 << 2), nArray[i2][i3], 0, n4);
            }
        }
        return pointer;
    }

    public static Pointer<Integer> allocateInt() {
        return Pointer.allocate(PointerIO.getIntInstance());
    }

    public static Pointer<Integer> allocateInts(long l2) {
        return Pointer.allocateArray(PointerIO.getIntInstance(), l2);
    }

    public static Pointer<Pointer<Integer>> allocateInts(long l2, long l3) {
        return Pointer.allocateArray(PointerIO.getArrayInstance(PointerIO.getIntInstance(), new long[]{l2, l3}, 0), l2);
    }

    public static Pointer<Pointer<Pointer<Integer>>> allocateInts(long l2, long l3, long l4) {
        long[] lArray = new long[]{l2, l3, l4};
        return Pointer.allocateArray(PointerIO.getArrayInstance(PointerIO.getArrayInstance(PointerIO.getIntInstance(), lArray, 1), lArray, 0), l2);
    }

    public static Pointer<Long> pointerToLong(long l2) {
        Pointer<Long> pointer = Pointer.allocate(PointerIO.getLongInstance());
        pointer.setLong(l2);
        return pointer;
    }

    public static Pointer<Long> pointerToLongs(long ... lArray) {
        if (lArray == null) {
            return null;
        }
        Pointer<Long> pointer = Pointer.allocateArray(PointerIO.getLongInstance(), (long)lArray.length);
        pointer.setLongsAtOffset(0L, lArray, 0, lArray.length);
        return pointer;
    }

    public static Pointer<Pointer<Long>> pointerToLongs(long[][] lArray) {
        if (lArray == null) {
            return null;
        }
        int n2 = lArray.length;
        int n3 = lArray[0].length;
        Pointer<Pointer<Long>> pointer = Pointer.allocateLongs(n2, n3);
        for (int i2 = 0; i2 < n2; ++i2) {
            pointer.setLongsAtOffset((long)(i2 * n3 << 3), lArray[i2], 0, n3);
        }
        return pointer;
    }

    public static Pointer<Pointer<Pointer<Long>>> pointerToLongs(long[][][] lArray) {
        if (lArray == null) {
            return null;
        }
        int n2 = lArray.length;
        int n3 = lArray[0].length;
        int n4 = lArray[0][0].length;
        Pointer<Pointer<Pointer<Long>>> pointer = Pointer.allocateLongs(n2, n3, n4);
        for (int i2 = 0; i2 < n2; ++i2) {
            int n5 = i2 * n3;
            for (int i3 = 0; i3 < n3; ++i3) {
                int n6 = (n5 + i3) * n4;
                pointer.setLongsAtOffset((long)(n6 << 3), lArray[i2][i3], 0, n4);
            }
        }
        return pointer;
    }

    public static Pointer<Long> allocateLong() {
        return Pointer.allocate(PointerIO.getLongInstance());
    }

    public static Pointer<Long> allocateLongs(long l2) {
        return Pointer.allocateArray(PointerIO.getLongInstance(), l2);
    }

    public static Pointer<Pointer<Long>> allocateLongs(long l2, long l3) {
        return Pointer.allocateArray(PointerIO.getArrayInstance(PointerIO.getLongInstance(), new long[]{l2, l3}, 0), l2);
    }

    public static Pointer<Pointer<Pointer<Long>>> allocateLongs(long l2, long l3, long l4) {
        long[] lArray = new long[]{l2, l3, l4};
        return Pointer.allocateArray(PointerIO.getArrayInstance(PointerIO.getArrayInstance(PointerIO.getLongInstance(), lArray, 1), lArray, 0), l2);
    }

    public static Pointer<Short> pointerToShort(short s2) {
        Pointer<Short> pointer = Pointer.allocate(PointerIO.getShortInstance());
        pointer.setShort(s2);
        return pointer;
    }

    public static Pointer<Short> pointerToShorts(short ... sArray) {
        if (sArray == null) {
            return null;
        }
        Pointer<Short> pointer = Pointer.allocateArray(PointerIO.getShortInstance(), (long)sArray.length);
        pointer.setShortsAtOffset(0L, sArray, 0, sArray.length);
        return pointer;
    }

    public static Pointer<Pointer<Short>> pointerToShorts(short[][] sArray) {
        if (sArray == null) {
            return null;
        }
        int n2 = sArray.length;
        int n3 = sArray[0].length;
        Pointer<Pointer<Short>> pointer = Pointer.allocateShorts(n2, n3);
        for (int i2 = 0; i2 < n2; ++i2) {
            pointer.setShortsAtOffset((long)(i2 * n3 << 1), sArray[i2], 0, n3);
        }
        return pointer;
    }

    public static Pointer<Pointer<Pointer<Short>>> pointerToShorts(short[][][] sArray) {
        if (sArray == null) {
            return null;
        }
        int n2 = sArray.length;
        int n3 = sArray[0].length;
        int n4 = sArray[0][0].length;
        Pointer<Pointer<Pointer<Short>>> pointer = Pointer.allocateShorts(n2, n3, n4);
        for (int i2 = 0; i2 < n2; ++i2) {
            int n5 = i2 * n3;
            for (int i3 = 0; i3 < n3; ++i3) {
                int n6 = (n5 + i3) * n4;
                pointer.setShortsAtOffset((long)(n6 << 1), sArray[i2][i3], 0, n4);
            }
        }
        return pointer;
    }

    public static Pointer<Short> allocateShort() {
        return Pointer.allocate(PointerIO.getShortInstance());
    }

    public static Pointer<Short> allocateShorts(long l2) {
        return Pointer.allocateArray(PointerIO.getShortInstance(), l2);
    }

    public static Pointer<Pointer<Short>> allocateShorts(long l2, long l3) {
        return Pointer.allocateArray(PointerIO.getArrayInstance(PointerIO.getShortInstance(), new long[]{l2, l3}, 0), l2);
    }

    public static Pointer<Pointer<Pointer<Short>>> allocateShorts(long l2, long l3, long l4) {
        long[] lArray = new long[]{l2, l3, l4};
        return Pointer.allocateArray(PointerIO.getArrayInstance(PointerIO.getArrayInstance(PointerIO.getShortInstance(), lArray, 1), lArray, 0), l2);
    }

    public static Pointer<Byte> pointerToByte(byte by) {
        Pointer<Byte> pointer = Pointer.allocate(PointerIO.getByteInstance());
        pointer.setByte(by);
        return pointer;
    }

    public static Pointer<Byte> pointerToBytes(byte ... byArray) {
        if (byArray == null) {
            return null;
        }
        Pointer<Byte> pointer = Pointer.allocateArray(PointerIO.getByteInstance(), (long)byArray.length);
        pointer.setBytesAtOffset(0L, byArray, 0, byArray.length);
        return pointer;
    }

    public static Pointer<Pointer<Byte>> pointerToBytes(byte[][] byArray) {
        if (byArray == null) {
            return null;
        }
        int n2 = byArray.length;
        int n3 = byArray[0].length;
        Pointer<Pointer<Byte>> pointer = Pointer.allocateBytes(n2, n3);
        for (int i2 = 0; i2 < n2; ++i2) {
            pointer.setBytesAtOffset((long)(i2 * n3), byArray[i2], 0, n3);
        }
        return pointer;
    }

    public static Pointer<Pointer<Pointer<Byte>>> pointerToBytes(byte[][][] byArray) {
        if (byArray == null) {
            return null;
        }
        int n2 = byArray.length;
        int n3 = byArray[0].length;
        int n4 = byArray[0][0].length;
        Pointer<Pointer<Pointer<Byte>>> pointer = Pointer.allocateBytes(n2, (long)n3, n4);
        for (int i2 = 0; i2 < n2; ++i2) {
            int n5 = i2 * n3;
            for (int i3 = 0; i3 < n3; ++i3) {
                int n6 = (n5 + i3) * n4;
                pointer.setBytesAtOffset((long)n6, byArray[i2][i3], 0, n4);
            }
        }
        return pointer;
    }

    public static Pointer<Byte> allocateByte() {
        return Pointer.allocate(PointerIO.getByteInstance());
    }

    public static Pointer<Byte> allocateBytes(long l2) {
        return Pointer.allocateArray(PointerIO.getByteInstance(), l2);
    }

    public static Pointer<Pointer<Byte>> allocateBytes(long l2, long l3) {
        return Pointer.allocateArray(PointerIO.getArrayInstance(PointerIO.getByteInstance(), new long[]{l2, l3}, 0), l2);
    }

    public static Pointer<Pointer<Pointer<Byte>>> allocateBytes(long l2, long l3, long l4) {
        long[] lArray = new long[]{l2, l3, l4};
        return Pointer.allocateArray(PointerIO.getArrayInstance(PointerIO.getArrayInstance(PointerIO.getByteInstance(), lArray, 1), lArray, 0), l2);
    }

    public static Pointer<Character> pointerToChar(char c2) {
        Pointer<Character> pointer = Pointer.allocate(PointerIO.getCharInstance());
        pointer.setChar(c2);
        return pointer;
    }

    public static Pointer<Character> pointerToChars(char ... cArray) {
        if (cArray == null) {
            return null;
        }
        Pointer<Character> pointer = Pointer.allocateArray(PointerIO.getCharInstance(), (long)cArray.length);
        pointer.setCharsAtOffset(0L, cArray, 0, cArray.length);
        return pointer;
    }

    public static Pointer<Pointer<Character>> pointerToChars(char[][] cArray) {
        if (cArray == null) {
            return null;
        }
        int n2 = cArray.length;
        int n3 = cArray[0].length;
        Pointer<Pointer<Character>> pointer = Pointer.allocateChars(n2, n3);
        for (int i2 = 0; i2 < n2; ++i2) {
            pointer.setCharsAtOffset((long)(i2 * n3 * Platform.WCHAR_T_SIZE), cArray[i2], 0, n3);
        }
        return pointer;
    }

    public static Pointer<Pointer<Pointer<Character>>> pointerToChars(char[][][] cArray) {
        if (cArray == null) {
            return null;
        }
        int n2 = cArray.length;
        int n3 = cArray[0].length;
        int n4 = cArray[0][0].length;
        Pointer<Pointer<Pointer<Character>>> pointer = Pointer.allocateChars(n2, n3, n4);
        for (int i2 = 0; i2 < n2; ++i2) {
            int n5 = i2 * n3;
            for (int i3 = 0; i3 < n3; ++i3) {
                int n6 = (n5 + i3) * n4;
                pointer.setCharsAtOffset((long)(n6 * Platform.WCHAR_T_SIZE), cArray[i2][i3], 0, n4);
            }
        }
        return pointer;
    }

    public static Pointer<Character> allocateChar() {
        return Pointer.allocate(PointerIO.getCharInstance());
    }

    public static Pointer<Character> allocateChars(long l2) {
        return Pointer.allocateArray(PointerIO.getCharInstance(), l2);
    }

    public static Pointer<Pointer<Character>> allocateChars(long l2, long l3) {
        return Pointer.allocateArray(PointerIO.getArrayInstance(PointerIO.getCharInstance(), new long[]{l2, l3}, 0), l2);
    }

    public static Pointer<Pointer<Pointer<Character>>> allocateChars(long l2, long l3, long l4) {
        long[] lArray = new long[]{l2, l3, l4};
        return Pointer.allocateArray(PointerIO.getArrayInstance(PointerIO.getArrayInstance(PointerIO.getCharInstance(), lArray, 1), lArray, 0), l2);
    }

    public static Pointer<Float> pointerToFloat(float f2) {
        Pointer<Float> pointer = Pointer.allocate(PointerIO.getFloatInstance());
        pointer.setFloat(f2);
        return pointer;
    }

    public static Pointer<Float> pointerToFloats(float ... fArray) {
        if (fArray == null) {
            return null;
        }
        Pointer<Float> pointer = Pointer.allocateArray(PointerIO.getFloatInstance(), (long)fArray.length);
        pointer.setFloatsAtOffset(0L, fArray, 0, fArray.length);
        return pointer;
    }

    public static Pointer<Pointer<Float>> pointerToFloats(float[][] fArray) {
        if (fArray == null) {
            return null;
        }
        int n2 = fArray.length;
        int n3 = fArray[0].length;
        Pointer<Pointer<Float>> pointer = Pointer.allocateFloats(n2, n3);
        for (int i2 = 0; i2 < n2; ++i2) {
            pointer.setFloatsAtOffset((long)(i2 * n3 << 2), fArray[i2], 0, n3);
        }
        return pointer;
    }

    public static Pointer<Pointer<Pointer<Float>>> pointerToFloats(float[][][] fArray) {
        if (fArray == null) {
            return null;
        }
        int n2 = fArray.length;
        int n3 = fArray[0].length;
        int n4 = fArray[0][0].length;
        Pointer<Pointer<Pointer<Float>>> pointer = Pointer.allocateFloats(n2, n3, n4);
        for (int i2 = 0; i2 < n2; ++i2) {
            int n5 = i2 * n3;
            for (int i3 = 0; i3 < n3; ++i3) {
                int n6 = (n5 + i3) * n4;
                pointer.setFloatsAtOffset((long)(n6 << 2), fArray[i2][i3], 0, n4);
            }
        }
        return pointer;
    }

    public static Pointer<Float> allocateFloat() {
        return Pointer.allocate(PointerIO.getFloatInstance());
    }

    public static Pointer<Float> allocateFloats(long l2) {
        return Pointer.allocateArray(PointerIO.getFloatInstance(), l2);
    }

    public static Pointer<Pointer<Float>> allocateFloats(long l2, long l3) {
        return Pointer.allocateArray(PointerIO.getArrayInstance(PointerIO.getFloatInstance(), new long[]{l2, l3}, 0), l2);
    }

    public static Pointer<Pointer<Pointer<Float>>> allocateFloats(long l2, long l3, long l4) {
        long[] lArray = new long[]{l2, l3, l4};
        return Pointer.allocateArray(PointerIO.getArrayInstance(PointerIO.getArrayInstance(PointerIO.getFloatInstance(), lArray, 1), lArray, 0), l2);
    }

    public static Pointer<Double> pointerToDouble(double d2) {
        Pointer<Double> pointer = Pointer.allocate(PointerIO.getDoubleInstance());
        pointer.setDouble(d2);
        return pointer;
    }

    public static Pointer<Double> pointerToDoubles(double ... dArray) {
        if (dArray == null) {
            return null;
        }
        Pointer<Double> pointer = Pointer.allocateArray(PointerIO.getDoubleInstance(), (long)dArray.length);
        pointer.setDoublesAtOffset(0L, dArray, 0, dArray.length);
        return pointer;
    }

    public static Pointer<Pointer<Double>> pointerToDoubles(double[][] dArray) {
        if (dArray == null) {
            return null;
        }
        int n2 = dArray.length;
        int n3 = dArray[0].length;
        Pointer<Pointer<Double>> pointer = Pointer.allocateDoubles(n2, n3);
        for (int i2 = 0; i2 < n2; ++i2) {
            pointer.setDoublesAtOffset((long)(i2 * n3 << 3), dArray[i2], 0, n3);
        }
        return pointer;
    }

    public static Pointer<Pointer<Pointer<Double>>> pointerToDoubles(double[][][] dArray) {
        if (dArray == null) {
            return null;
        }
        int n2 = dArray.length;
        int n3 = dArray[0].length;
        int n4 = dArray[0][0].length;
        Pointer<Pointer<Pointer<Double>>> pointer = Pointer.allocateDoubles(n2, n3, n4);
        for (int i2 = 0; i2 < n2; ++i2) {
            int n5 = i2 * n3;
            for (int i3 = 0; i3 < n3; ++i3) {
                int n6 = (n5 + i3) * n4;
                pointer.setDoublesAtOffset((long)(n6 << 3), dArray[i2][i3], 0, n4);
            }
        }
        return pointer;
    }

    public static Pointer<Double> allocateDouble() {
        return Pointer.allocate(PointerIO.getDoubleInstance());
    }

    public static Pointer<Double> allocateDoubles(long l2) {
        return Pointer.allocateArray(PointerIO.getDoubleInstance(), l2);
    }

    public static Pointer<Pointer<Double>> allocateDoubles(long l2, long l3) {
        return Pointer.allocateArray(PointerIO.getArrayInstance(PointerIO.getDoubleInstance(), new long[]{l2, l3}, 0), l2);
    }

    public static Pointer<Pointer<Pointer<Double>>> allocateDoubles(long l2, long l3, long l4) {
        long[] lArray = new long[]{l2, l3, l4};
        return Pointer.allocateArray(PointerIO.getArrayInstance(PointerIO.getArrayInstance(PointerIO.getDoubleInstance(), lArray, 1), lArray, 0), l2);
    }

    public static Pointer<Boolean> pointerToBoolean(boolean bl) {
        Pointer<Boolean> pointer = Pointer.allocate(PointerIO.getBooleanInstance());
        pointer.setBoolean(bl);
        return pointer;
    }

    public static Pointer<Boolean> pointerToBooleans(boolean ... blArray) {
        if (blArray == null) {
            return null;
        }
        Pointer<Boolean> pointer = Pointer.allocateArray(PointerIO.getBooleanInstance(), (long)blArray.length);
        pointer.setBooleansAtOffset(0L, blArray, 0, blArray.length);
        return pointer;
    }

    public static Pointer<Pointer<Boolean>> pointerToBooleans(boolean[][] blArray) {
        if (blArray == null) {
            return null;
        }
        int n2 = blArray.length;
        int n3 = blArray[0].length;
        Pointer<Pointer<Boolean>> pointer = Pointer.allocateBooleans(n2, n3);
        for (int i2 = 0; i2 < n2; ++i2) {
            pointer.setBooleansAtOffset(i2 * n3, blArray[i2], 0, n3);
        }
        return pointer;
    }

    public static Pointer<Pointer<Pointer<Boolean>>> pointerToBooleans(boolean[][][] blArray) {
        if (blArray == null) {
            return null;
        }
        int n2 = blArray.length;
        int n3 = blArray[0].length;
        int n4 = blArray[0][0].length;
        Pointer<Pointer<Pointer<Boolean>>> pointer = Pointer.allocateBooleans(n2, n3, n4);
        for (int i2 = 0; i2 < n2; ++i2) {
            int n5 = i2 * n3;
            for (int i3 = 0; i3 < n3; ++i3) {
                int n6 = (n5 + i3) * n4;
                pointer.setBooleansAtOffset(n6, blArray[i2][i3], 0, n4);
            }
        }
        return pointer;
    }

    public static Pointer<Boolean> allocateBoolean() {
        return Pointer.allocate(PointerIO.getBooleanInstance());
    }

    public static Pointer<Boolean> allocateBooleans(long l2) {
        return Pointer.allocateArray(PointerIO.getBooleanInstance(), l2);
    }

    public static Pointer<Pointer<Boolean>> allocateBooleans(long l2, long l3) {
        return Pointer.allocateArray(PointerIO.getArrayInstance(PointerIO.getBooleanInstance(), new long[]{l2, l3}, 0), l2);
    }

    public static Pointer<Pointer<Pointer<Boolean>>> allocateBooleans(long l2, long l3, long l4) {
        long[] lArray = new long[]{l2, l3, l4};
        return Pointer.allocateArray(PointerIO.getArrayInstance(PointerIO.getArrayInstance(PointerIO.getBooleanInstance(), lArray, 1), lArray, 0), l2);
    }

    public static Pointer<Integer> pointerToInts(IntBuffer intBuffer) {
        if (intBuffer == null) {
            return null;
        }
        if (!intBuffer.isDirect()) {
            int[] nArray = intBuffer.array();
            int n2 = intBuffer.arrayOffset();
            int n3 = nArray.length - n2;
            Pointer<Integer> pointer = Pointer.allocateInts(n3);
            pointer.setIntsAtOffset(0L, nArray, n2, n3);
            return pointer;
        }
        long l2 = JNI.getDirectBufferAddress(intBuffer);
        long l3 = JNI.getDirectBufferCapacity(intBuffer);
        if (l2 == 0L || (l3 <<= 2) == 0L) {
            return null;
        }
        PointerIO<Integer> pointerIO = CommonPointerIOs.intIO;
        boolean bl = intBuffer.order().equals(ByteOrder.nativeOrder());
        long l4 = l2;
        return Pointer.newPointer(pointerIO, l2, bl, l4, l4 + l3, null, 0L, null, intBuffer);
    }

    public static Pointer<Long> pointerToLongs(LongBuffer longBuffer) {
        if (longBuffer == null) {
            return null;
        }
        if (!longBuffer.isDirect()) {
            long[] lArray = longBuffer.array();
            int n2 = longBuffer.arrayOffset();
            int n3 = lArray.length - n2;
            Pointer<Long> pointer = Pointer.allocateLongs(n3);
            pointer.setLongsAtOffset(0L, lArray, n2, n3);
            return pointer;
        }
        long l2 = JNI.getDirectBufferAddress(longBuffer);
        long l3 = JNI.getDirectBufferCapacity(longBuffer);
        if (l2 == 0L || (l3 <<= 3) == 0L) {
            return null;
        }
        PointerIO<Long> pointerIO = CommonPointerIOs.longIO;
        boolean bl = longBuffer.order().equals(ByteOrder.nativeOrder());
        long l4 = l2;
        return Pointer.newPointer(pointerIO, l2, bl, l4, l4 + l3, null, 0L, null, longBuffer);
    }

    public static Pointer<Short> pointerToShorts(ShortBuffer shortBuffer) {
        if (shortBuffer == null) {
            return null;
        }
        if (!shortBuffer.isDirect()) {
            short[] sArray = shortBuffer.array();
            int n2 = shortBuffer.arrayOffset();
            int n3 = sArray.length - n2;
            Pointer<Short> pointer = Pointer.allocateShorts(n3);
            pointer.setShortsAtOffset(0L, sArray, n2, n3);
            return pointer;
        }
        long l2 = JNI.getDirectBufferAddress(shortBuffer);
        long l3 = JNI.getDirectBufferCapacity(shortBuffer);
        if (l2 == 0L || (l3 <<= 1) == 0L) {
            return null;
        }
        PointerIO<Short> pointerIO = CommonPointerIOs.shortIO;
        boolean bl = shortBuffer.order().equals(ByteOrder.nativeOrder());
        long l4 = l2;
        return Pointer.newPointer(pointerIO, l2, bl, l4, l4 + l3, null, 0L, null, shortBuffer);
    }

    public static Pointer<Byte> pointerToBytes(ByteBuffer byteBuffer) {
        if (byteBuffer == null) {
            return null;
        }
        if (!byteBuffer.isDirect()) {
            byte[] byArray = byteBuffer.array();
            int n2 = byteBuffer.arrayOffset();
            int n3 = byArray.length - n2;
            Pointer<Byte> pointer = Pointer.allocateBytes(n3);
            pointer.setBytesAtOffset(0L, byArray, n2, n3);
            return pointer;
        }
        long l2 = JNI.getDirectBufferAddress(byteBuffer);
        long l3 = JNI.getDirectBufferCapacity(byteBuffer);
        if (l2 == 0L || l3 == 0L) {
            return null;
        }
        PointerIO<Byte> pointerIO = CommonPointerIOs.byteIO;
        boolean bl = byteBuffer.order().equals(ByteOrder.nativeOrder());
        long l4 = l2;
        return Pointer.newPointer(pointerIO, l2, bl, l4, l4 + l3, null, 0L, null, byteBuffer);
    }

    public static Pointer<Character> pointerToChars(CharBuffer charBuffer) {
        if (charBuffer == null) {
            return null;
        }
        if (!charBuffer.isDirect()) {
            char[] cArray = charBuffer.array();
            int n2 = charBuffer.arrayOffset();
            int n3 = cArray.length - n2;
            Pointer<Character> pointer = Pointer.allocateChars(n3);
            pointer.setCharsAtOffset(0L, cArray, n2, n3);
            return pointer;
        }
        long l2 = JNI.getDirectBufferAddress(charBuffer);
        long l3 = JNI.getDirectBufferCapacity(charBuffer);
        if (l2 == 0L || (l3 *= (long)Platform.WCHAR_T_SIZE) == 0L) {
            return null;
        }
        PointerIO<Character> pointerIO = CommonPointerIOs.charIO;
        boolean bl = charBuffer.order().equals(ByteOrder.nativeOrder());
        long l4 = l2;
        return Pointer.newPointer(pointerIO, l2, bl, l4, l4 + l3, null, 0L, null, charBuffer);
    }

    public static Pointer<Float> pointerToFloats(FloatBuffer floatBuffer) {
        if (floatBuffer == null) {
            return null;
        }
        if (!floatBuffer.isDirect()) {
            float[] fArray = floatBuffer.array();
            int n2 = floatBuffer.arrayOffset();
            int n3 = fArray.length - n2;
            Pointer<Float> pointer = Pointer.allocateFloats(n3);
            pointer.setFloatsAtOffset(0L, fArray, n2, n3);
            return pointer;
        }
        long l2 = JNI.getDirectBufferAddress(floatBuffer);
        long l3 = JNI.getDirectBufferCapacity(floatBuffer);
        if (l2 == 0L || (l3 <<= 2) == 0L) {
            return null;
        }
        PointerIO<Float> pointerIO = CommonPointerIOs.floatIO;
        boolean bl = floatBuffer.order().equals(ByteOrder.nativeOrder());
        long l4 = l2;
        return Pointer.newPointer(pointerIO, l2, bl, l4, l4 + l3, null, 0L, null, floatBuffer);
    }

    public static Pointer<Double> pointerToDoubles(DoubleBuffer doubleBuffer) {
        if (doubleBuffer == null) {
            return null;
        }
        if (!doubleBuffer.isDirect()) {
            double[] dArray = doubleBuffer.array();
            int n2 = doubleBuffer.arrayOffset();
            int n3 = dArray.length - n2;
            Pointer<Double> pointer = Pointer.allocateDoubles(n3);
            pointer.setDoublesAtOffset(0L, dArray, n2, n3);
            return pointer;
        }
        long l2 = JNI.getDirectBufferAddress(doubleBuffer);
        long l3 = JNI.getDirectBufferCapacity(doubleBuffer);
        if (l2 == 0L || (l3 <<= 3) == 0L) {
            return null;
        }
        PointerIO<Double> pointerIO = CommonPointerIOs.doubleIO;
        boolean bl = doubleBuffer.order().equals(ByteOrder.nativeOrder());
        long l4 = l2;
        return Pointer.newPointer(pointerIO, l2, bl, l4, l4 + l3, null, 0L, null, doubleBuffer);
    }

    public Type getTargetType() {
        PointerIO<T> pointerIO = this.getIO();
        if (pointerIO == null) {
            return null;
        }
        return pointerIO.getTargetType();
    }

    @Deprecated
    public Pointer<?> getPointer() {
        return this.getPointerAtOffset(0L, (PointerIO<U>)null);
    }

    public Pointer<?> getPointerAtOffset(long l2) {
        return this.getPointerAtOffset(l2, (PointerIO<U>)null);
    }

    public Pointer<?> getPointerAtIndex(long l2) {
        return this.getPointerAtOffset(l2 * (long)SIZE);
    }

    public <U> Pointer<U> getPointer(Class<U> clazz) {
        return this.getPointerAtOffset(0L, PointerIO.getInstance(clazz));
    }

    public <U> Pointer<U> getPointer(PointerIO<U> pointerIO) {
        return this.getPointerAtOffset(0L, pointerIO);
    }

    public <U> Pointer<U> getPointerAtOffset(long l2, Class<U> clazz) {
        return this.getPointerAtOffset(l2, (Type)clazz);
    }

    public <U> Pointer<U> getPointerAtOffset(long l2, Type type) {
        return this.getPointerAtOffset(l2, type == null ? null : PointerIO.getInstance(type));
    }

    public <U> Pointer<U> getPointerAtOffset(long l2, PointerIO<U> pointerIO) {
        long l3 = this.getSizeTAtOffset(l2);
        if (l3 == 0L) {
            return null;
        }
        return Pointer.newPointer(pointerIO, l3, this.isOrdered(), -1L, -1L, this, l2, null, null);
    }

    public Pointer<T> setPointer(Pointer<?> pointer) {
        return this.setPointerAtOffset(0L, pointer);
    }

    public Pointer<T> setPointerAtOffset(long l2, Pointer<?> pointer) {
        this.setSizeTAtOffset(l2, pointer == null ? 0L : pointer.getPeer());
        return this;
    }

    public Pointer<T> setPointerAtIndex(long l2, Pointer<?> pointer) {
        this.setPointerAtOffset(l2 * (long)SIZE, pointer);
        return this;
    }

    public Pointer<?>[] getPointersAtOffset(long l2, int n2) {
        return this.getPointersAtOffset(l2, n2, (PointerIO)null);
    }

    @Deprecated
    public Pointer<?>[] getPointers() {
        long l2 = this.getValidElements("Cannot create array if remaining length is not known. Please use getPointers(int length) instead.");
        return this.getPointersAtOffset(0L, (int)l2);
    }

    @Deprecated
    public Pointer<?>[] getPointers(int n2) {
        return this.getPointersAtOffset(0L, n2);
    }

    public <U> Pointer<U>[] getPointersAtOffset(long l2, int n2, Type type) {
        return this.getPointersAtOffset(l2, n2, type == null ? null : PointerIO.getInstance(type));
    }

    public <U> Pointer<U>[] getPointersAtOffset(long l2, int n2, Class<U> clazz) {
        return this.getPointersAtOffset(l2, n2, (Type)clazz);
    }

    public <U> Pointer<U>[] getPointersAtOffset(long l2, int n2, PointerIO pointerIO) {
        Pointer[] pointerArray = new Pointer[n2];
        int n3 = Platform.POINTER_SIZE;
        for (int i2 = 0; i2 < n2; ++i2) {
            pointerArray[i2] = this.getPointerAtOffset(l2 + (long)(i2 * n3), pointerIO);
        }
        return pointerArray;
    }

    public Pointer<T> setPointersAtOffset(long l2, Pointer<?>[] pointerArray) {
        return this.setPointersAtOffset(l2, pointerArray, 0, pointerArray.length);
    }

    public Pointer<T> setPointersAtOffset(long l2, Pointer<?>[] pointerArray, int n2, int n3) {
        if (pointerArray == null) {
            throw new IllegalArgumentException("Null values");
        }
        int n4 = Platform.POINTER_SIZE;
        for (int i2 = 0; i2 < n3; ++i2) {
            this.setPointerAtOffset(l2 + (long)(i2 * n4), pointerArray[n2 + i2]);
        }
        return this;
    }

    public Pointer<T> setPointers(Pointer<?>[] pointerArray) {
        return this.setPointersAtOffset(0L, pointerArray);
    }

    public Object getArrayAtOffset(long l2, int n2) {
        return this.getIO("Cannot create sublist").getArray(this, l2, n2);
    }

    public Object getArray(int n2) {
        return this.getArrayAtOffset(0L, n2);
    }

    public Object getArray() {
        Pointer pointer = this;
        return pointer.getArray((int)pointer.getValidElements());
    }

    public <B extends Buffer> B getBufferAtOffset(long l2, int n2) {
        return this.getIO("Cannot create Buffer").getBuffer(this, l2, n2);
    }

    public <B extends Buffer> B getBuffer(int n2) {
        return this.getBufferAtOffset(0L, n2);
    }

    public <B extends Buffer> B getBuffer() {
        Pointer pointer = this;
        return pointer.getBuffer((int)pointer.getValidElements());
    }

    public Pointer<T> setArrayAtOffset(long l2, Object object) {
        this.getIO("Cannot create sublist").setArray(this, l2, object);
        return this;
    }

    public static <T> Pointer<T> pointerToArray(Object object) {
        if (object == null) {
            return null;
        }
        PointerIO pointerIO = PointerIO.getArrayIO(object);
        if (pointerIO == null) {
            Pointer.throwBecauseUntyped("Cannot create pointer to array");
        }
        Pointer pointer = Pointer.allocateArray(pointerIO, (long)Array.getLength(object));
        pointerIO.setArray(pointer, 0L, object);
        return pointer;
    }

    public Pointer<T> setArray(Object object) {
        return this.setArrayAtOffset(0L, object);
    }

    public static Pointer<SizeT> pointerToSizeT(long l2) {
        Pointer<SizeT> pointer = Pointer.allocate(PointerIO.getSizeTInstance());
        pointer.setSizeT(l2);
        return pointer;
    }

    public static Pointer<SizeT> pointerToSizeT(SizeT sizeT) {
        Pointer<SizeT> pointer = Pointer.allocate(PointerIO.getSizeTInstance());
        pointer.setSizeT(sizeT);
        return pointer;
    }

    public static Pointer<SizeT> pointerToSizeTs(long ... lArray) {
        if (lArray == null) {
            return null;
        }
        return Pointer.allocateArray(PointerIO.getSizeTInstance(), (long)lArray.length).setSizeTsAtOffset(0L, lArray);
    }

    public static Pointer<SizeT> pointerToSizeTs(SizeT ... sizeTArray) {
        if (sizeTArray == null) {
            return null;
        }
        return Pointer.allocateArray(PointerIO.getSizeTInstance(), (long)sizeTArray.length).setSizeTsAtOffset(0L, sizeTArray);
    }

    public static Pointer<SizeT> pointerToSizeTs(int[] nArray) {
        if (nArray == null) {
            return null;
        }
        return Pointer.allocateArray(PointerIO.getSizeTInstance(), (long)nArray.length).setSizeTsAtOffset(0L, nArray);
    }

    public static Pointer<SizeT> allocateSizeTs(long l2) {
        return Pointer.allocateArray(PointerIO.getSizeTInstance(), l2);
    }

    public static Pointer<SizeT> allocateSizeT() {
        return Pointer.allocate(PointerIO.getSizeTInstance());
    }

    public long getSizeT() {
        if (SizeT.SIZE == 8) {
            return this.getLong();
        }
        return this.getInt();
    }

    public long getSizeTAtOffset(long l2) {
        if (SizeT.SIZE == 8) {
            return this.getLongAtOffset(l2);
        }
        return this.getIntAtOffset(l2);
    }

    public long getSizeTAtIndex(long l2) {
        return this.getSizeTAtOffset(l2 * (long)SizeT.SIZE);
    }

    public long[] getSizeTs() {
        long l2 = this.getValidElements("Cannot create array if remaining length is not known. Please use getSizeTs(int length) instead.");
        if (SizeT.SIZE == 8) {
            return this.getLongs((int)l2);
        }
        return this.getSizeTs((int)l2);
    }

    public long[] getSizeTs(int n2) {
        if (SizeT.SIZE == 8) {
            return this.getLongs(n2);
        }
        return this.getSizeTsAtOffset(0L, n2);
    }

    public long[] getSizeTsAtOffset(long l2, int n2) {
        if (SizeT.SIZE == 8) {
            return this.getLongsAtOffset(l2, n2);
        }
        int[] nArray = this.getIntsAtOffset(l2, n2);
        long[] lArray = new long[n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            lArray[i2] = nArray[i2];
        }
        return lArray;
    }

    public Pointer<T> setSizeT(long l2) {
        if (SizeT.SIZE == 8) {
            this.setLong(l2);
        } else {
            this.setInt(SizeT.safeIntCast(l2));
        }
        return this;
    }

    public Pointer<T> setSizeT(SizeT sizeT) {
        return this.setSizeT(sizeT.longValue());
    }

    public Pointer<T> setSizeTAtOffset(long l2, long l3) {
        if (SizeT.SIZE == 8) {
            this.setLongAtOffset(l2, l3);
        } else {
            this.setIntAtOffset(l2, SizeT.safeIntCast(l3));
        }
        return this;
    }

    public Pointer<T> setSizeTAtIndex(long l2, long l3) {
        return this.setSizeTAtOffset(l2 * (long)SizeT.SIZE, l3);
    }

    public Pointer<T> setSizeTAtOffset(long l2, SizeT sizeT) {
        return this.setSizeTAtOffset(l2, sizeT.longValue());
    }

    public Pointer<T> setSizeTs(long[] lArray) {
        if (SizeT.SIZE == 8) {
            return this.setLongs(lArray);
        }
        return this.setSizeTsAtOffset(0L, lArray);
    }

    public Pointer<T> setSizeTs(int[] nArray) {
        if (SizeT.SIZE == 4) {
            return this.setInts(nArray);
        }
        return this.setSizeTsAtOffset(0L, nArray);
    }

    public Pointer<T> setSizeTs(SizeT[] sizeTArray) {
        return this.setSizeTsAtOffset(0L, sizeTArray);
    }

    public Pointer<T> setSizeTsAtOffset(long l2, long[] lArray) {
        return this.setSizeTsAtOffset(l2, lArray, 0, lArray.length);
    }

    public abstract Pointer<T> setSizeTsAtOffset(long var1, long[] var3, int var4, int var5);

    public Pointer<T> setSizeTsAtOffset(long l2, SizeT ... sizeTArray) {
        if (sizeTArray == null) {
            throw new IllegalArgumentException("Null values");
        }
        int n2 = sizeTArray.length;
        int n3 = SizeT.SIZE;
        for (int i2 = 0; i2 < n2; ++i2) {
            this.setSizeTAtOffset(l2 + (long)(i2 * n3), sizeTArray[i2].longValue());
        }
        return this;
    }

    public abstract Pointer<T> setSizeTsAtOffset(long var1, int[] var3);

    public static Pointer<CLong> pointerToCLong(long l2) {
        Pointer<CLong> pointer = Pointer.allocate(PointerIO.getCLongInstance());
        pointer.setCLong(l2);
        return pointer;
    }

    public static Pointer<CLong> pointerToCLong(CLong cLong) {
        Pointer<CLong> pointer = Pointer.allocate(PointerIO.getCLongInstance());
        pointer.setCLong(cLong);
        return pointer;
    }

    public static Pointer<CLong> pointerToCLongs(long ... lArray) {
        if (lArray == null) {
            return null;
        }
        return Pointer.allocateArray(PointerIO.getCLongInstance(), (long)lArray.length).setCLongsAtOffset(0L, lArray);
    }

    public static Pointer<CLong> pointerToCLongs(CLong ... cLongArray) {
        if (cLongArray == null) {
            return null;
        }
        return Pointer.allocateArray(PointerIO.getCLongInstance(), (long)cLongArray.length).setCLongsAtOffset(0L, cLongArray);
    }

    public static Pointer<CLong> pointerToCLongs(int[] nArray) {
        if (nArray == null) {
            return null;
        }
        return Pointer.allocateArray(PointerIO.getCLongInstance(), (long)nArray.length).setCLongsAtOffset(0L, nArray);
    }

    public static Pointer<CLong> allocateCLongs(long l2) {
        return Pointer.allocateArray(PointerIO.getCLongInstance(), l2);
    }

    public static Pointer<CLong> allocateCLong() {
        return Pointer.allocate(PointerIO.getCLongInstance());
    }

    public long getCLong() {
        if (CLong.SIZE == 8) {
            return this.getLong();
        }
        return this.getInt();
    }

    public long getCLongAtOffset(long l2) {
        if (CLong.SIZE == 8) {
            return this.getLongAtOffset(l2);
        }
        return this.getIntAtOffset(l2);
    }

    public long getCLongAtIndex(long l2) {
        return this.getCLongAtOffset(l2 * (long)CLong.SIZE);
    }

    public long[] getCLongs() {
        long l2 = this.getValidElements("Cannot create array if remaining length is not known. Please use getCLongs(int length) instead.");
        if (CLong.SIZE == 8) {
            return this.getLongs((int)l2);
        }
        return this.getCLongs((int)l2);
    }

    public long[] getCLongs(int n2) {
        if (CLong.SIZE == 8) {
            return this.getLongs(n2);
        }
        return this.getCLongsAtOffset(0L, n2);
    }

    public long[] getCLongsAtOffset(long l2, int n2) {
        if (CLong.SIZE == 8) {
            return this.getLongsAtOffset(l2, n2);
        }
        int[] nArray = this.getIntsAtOffset(l2, n2);
        long[] lArray = new long[n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            lArray[i2] = nArray[i2];
        }
        return lArray;
    }

    public Pointer<T> setCLong(long l2) {
        if (CLong.SIZE == 8) {
            this.setLong(l2);
        } else {
            this.setInt(SizeT.safeIntCast(l2));
        }
        return this;
    }

    public Pointer<T> setCLong(CLong cLong) {
        return this.setCLong(cLong.longValue());
    }

    public Pointer<T> setCLongAtOffset(long l2, long l3) {
        if (CLong.SIZE == 8) {
            this.setLongAtOffset(l2, l3);
        } else {
            this.setIntAtOffset(l2, SizeT.safeIntCast(l3));
        }
        return this;
    }

    public Pointer<T> setCLongAtIndex(long l2, long l3) {
        return this.setCLongAtOffset(l2 * (long)CLong.SIZE, l3);
    }

    public Pointer<T> setCLongAtOffset(long l2, CLong cLong) {
        return this.setCLongAtOffset(l2, cLong.longValue());
    }

    public Pointer<T> setCLongs(long[] lArray) {
        if (CLong.SIZE == 8) {
            return this.setLongs(lArray);
        }
        return this.setCLongsAtOffset(0L, lArray);
    }

    public Pointer<T> setCLongs(int[] nArray) {
        if (CLong.SIZE == 4) {
            return this.setInts(nArray);
        }
        return this.setCLongsAtOffset(0L, nArray);
    }

    public Pointer<T> setCLongs(CLong[] cLongArray) {
        return this.setCLongsAtOffset(0L, cLongArray);
    }

    public Pointer<T> setCLongsAtOffset(long l2, long[] lArray) {
        return this.setCLongsAtOffset(l2, lArray, 0, lArray.length);
    }

    public abstract Pointer<T> setCLongsAtOffset(long var1, long[] var3, int var4, int var5);

    public Pointer<T> setCLongsAtOffset(long l2, CLong ... cLongArray) {
        if (cLongArray == null) {
            throw new IllegalArgumentException("Null values");
        }
        int n2 = cLongArray.length;
        int n3 = CLong.SIZE;
        for (int i2 = 0; i2 < n2; ++i2) {
            this.setCLongAtOffset(l2 + (long)(i2 * n3), cLongArray[i2].longValue());
        }
        return this;
    }

    public abstract Pointer<T> setCLongsAtOffset(long var1, int[] var3);

    void setSignedIntegralAtOffset(long l2, long l3, long l4) {
        switch ((int)l4) {
            case 1: {
                if (l3 > 127L || l3 < -128L) {
                    throw new RuntimeException("Value out of byte bounds : " + l3);
                }
                this.setByteAtOffset(l2, (byte)l3);
                return;
            }
            case 2: {
                if (l3 > 32767L || l3 < -32768L) {
                    throw new RuntimeException("Value out of short bounds : " + l3);
                }
                this.setShortAtOffset(l2, (short)l3);
                return;
            }
            case 4: {
                if (l3 > Integer.MAX_VALUE || l3 < Integer.MIN_VALUE) {
                    throw new RuntimeException("Value out of int bounds : " + l3);
                }
                this.setIntAtOffset(l2, (int)l3);
                return;
            }
            case 8: {
                this.setLongAtOffset(l2, l3);
                return;
            }
        }
        throw new IllegalArgumentException("Cannot write integral type of size " + l4 + " (value = " + l3 + ")");
    }

    long getSignedIntegralAtOffset(long l2, long l3) {
        switch ((int)l3) {
            case 1: {
                return this.getByteAtOffset(l2);
            }
            case 2: {
                return this.getShortAtOffset(l2);
            }
            case 4: {
                return this.getIntAtOffset(l2);
            }
            case 8: {
                return this.getLongAtOffset(l2);
            }
        }
        throw new IllegalArgumentException("Cannot read integral type of size " + l3);
    }

    public static <T> Pointer<Pointer<T>> pointerToPointer(Pointer<T> pointer) {
        Pointer<Pointer<T>> pointer2 = Pointer.allocate(PointerIO.getPointerInstance());
        pointer2.setPointerAtOffset(0L, pointer);
        return pointer2;
    }

    public static <T> Pointer<Pointer<T>> pointerToPointers(Pointer<T> ... pointerArray) {
        if (pointerArray == null) {
            return null;
        }
        int n2 = pointerArray.length;
        int n3 = SIZE;
        Object object = PointerIO.getPointerInstance();
        object = Pointer.allocateArray(object, (long)n2);
        for (int i2 = 0; i2 < n2; ++i2) {
            ((Pointer)object).setPointerAtOffset(i2 * n3, pointerArray[i2]);
        }
        return object;
    }

    public Pointer<T> setValuesAtOffset(long l2, Buffer buffer) {
        if (buffer instanceof IntBuffer) {
            this.setIntsAtOffset(l2, (IntBuffer)buffer);
            return this;
        }
        if (buffer instanceof LongBuffer) {
            this.setLongsAtOffset(l2, (LongBuffer)buffer);
            return this;
        }
        if (buffer instanceof ShortBuffer) {
            this.setShortsAtOffset(l2, (ShortBuffer)buffer);
            return this;
        }
        if (buffer instanceof ByteBuffer) {
            this.setBytesAtOffset(l2, (ByteBuffer)buffer);
            return this;
        }
        if (buffer instanceof CharBuffer) {
            this.setCharsAtOffset(l2, (CharBuffer)buffer);
            return this;
        }
        if (buffer instanceof FloatBuffer) {
            this.setFloatsAtOffset(l2, (FloatBuffer)buffer);
            return this;
        }
        if (buffer instanceof DoubleBuffer) {
            this.setDoublesAtOffset(l2, (DoubleBuffer)buffer);
            return this;
        }
        throw new UnsupportedOperationException("Unhandled buffer type : " + buffer.getClass().getName());
    }

    public Pointer<T> setValuesAtOffset(long l2, Buffer buffer, int n2, int n3) {
        if (buffer instanceof IntBuffer) {
            this.setIntsAtOffset(l2, (IntBuffer)buffer, (long)n2, (long)n3);
            return this;
        }
        if (buffer instanceof LongBuffer) {
            this.setLongsAtOffset(l2, (LongBuffer)buffer, (long)n2, (long)n3);
            return this;
        }
        if (buffer instanceof ShortBuffer) {
            this.setShortsAtOffset(l2, (ShortBuffer)buffer, (long)n2, (long)n3);
            return this;
        }
        if (buffer instanceof ByteBuffer) {
            this.setBytesAtOffset(l2, (ByteBuffer)buffer, (long)n2, (long)n3);
            return this;
        }
        if (buffer instanceof CharBuffer) {
            this.setCharsAtOffset(l2, (CharBuffer)buffer, (long)n2, (long)n3);
            return this;
        }
        if (buffer instanceof FloatBuffer) {
            this.setFloatsAtOffset(l2, (FloatBuffer)buffer, (long)n2, (long)n3);
            return this;
        }
        if (buffer instanceof DoubleBuffer) {
            this.setDoublesAtOffset(l2, (DoubleBuffer)buffer, (long)n2, (long)n3);
            return this;
        }
        throw new UnsupportedOperationException("Unhandled buffer type : " + buffer.getClass().getName());
    }

    public Pointer<T> setValues(Buffer buffer) {
        if (buffer instanceof IntBuffer) {
            this.setInts((IntBuffer)buffer);
            return this;
        }
        if (buffer instanceof LongBuffer) {
            this.setLongs((LongBuffer)buffer);
            return this;
        }
        if (buffer instanceof ShortBuffer) {
            this.setShorts((ShortBuffer)buffer);
            return this;
        }
        if (buffer instanceof ByteBuffer) {
            this.setBytes((ByteBuffer)buffer);
            return this;
        }
        if (buffer instanceof CharBuffer) {
            this.setChars((CharBuffer)buffer);
            return this;
        }
        if (buffer instanceof FloatBuffer) {
            this.setFloats((FloatBuffer)buffer);
            return this;
        }
        if (buffer instanceof DoubleBuffer) {
            this.setDoubles((DoubleBuffer)buffer);
            return this;
        }
        throw new UnsupportedOperationException("Unhandled buffer type : " + buffer.getClass().getName());
    }

    @Deprecated
    public Pointer<T> copyBytesAtOffsetTo(long l2, Pointer<?> pointer, long l3, long l4) {
        long l5 = this.getPeer() + l2;
        if (this.validStart != -1L && (l5 < this.validStart || l5 + l4 > this.validEnd)) {
            this.invalidPeer(l5, l4);
        }
        JNI.memcpy(super.getCheckedPeer(l3, l4), l5, l4);
        return this;
    }

    @Deprecated
    public Pointer<T> copyBytesTo(Pointer<?> pointer, long l2) {
        return this.copyBytesAtOffsetTo(0L, pointer, 0L, l2);
    }

    @Deprecated
    public Pointer<T> moveBytesAtOffsetTo(long l2, Pointer<?> pointer, long l3, long l4) {
        long l5 = this.getPeer() + l2;
        if (this.validStart != -1L && (l5 < this.validStart || l5 + l4 > this.validEnd)) {
            this.invalidPeer(l5, l4);
        }
        JNI.memmove(super.getCheckedPeer(l3, l4), l5, l4);
        return this;
    }

    public Pointer<T> moveBytesTo(Pointer<?> pointer, long l2) {
        return this.moveBytesAtOffsetTo(0L, pointer, 0L, l2);
    }

    public Pointer<T> moveBytesTo(Pointer<?> pointer) {
        return this.moveBytesTo(pointer, this.getValidBytes("Cannot move an unbounded memory location. Please use validBytes(long)."));
    }

    final long getValidBytes(String string) {
        long l2 = this.getValidBytes();
        if (l2 < 0L) {
            throw new IndexOutOfBoundsException(string);
        }
        return l2;
    }

    final long getValidElements(String string) {
        long l2 = this.getValidElements();
        if (l2 < 0L) {
            throw new IndexOutOfBoundsException(string);
        }
        return l2;
    }

    final PointerIO<T> getIO(String string) {
        PointerIO<T> pointerIO = this.getIO();
        if (pointerIO == null) {
            Pointer.throwBecauseUntyped(string);
        }
        return pointerIO;
    }

    public Pointer<T> copyTo(Pointer<?> pointer) {
        return this.copyTo(pointer, this.getValidElements());
    }

    public Pointer<T> copyTo(Pointer<?> pointer, long l2) {
        PointerIO<T> pointerIO = this.getIO("Cannot copy untyped pointer without byte count information. Please use copyBytesAtOffsetTo(offset, destination, destinationOffset, byteCount) instead");
        return this.copyBytesAtOffsetTo(0L, pointer, 0L, l2 * pointerIO.getTargetSize());
    }

    public Pointer<T> find(Pointer<?> pointer) {
        if (pointer == null) {
            return null;
        }
        long l2 = JNI.memmem(this.getPeer(), this.getValidBytes("Cannot search an unbounded memory area. Please set bounds with validBytes(long)."), pointer.getPeer(), pointer.getValidBytes("Cannot search for an unbounded content. Please set bounds with validBytes(long)."));
        return Pointer.pointerToAddress(l2, this.io);
    }

    public Pointer<T> findLast(Pointer<?> pointer) {
        if (pointer == null) {
            return null;
        }
        long l2 = JNI.memmem_last(this.getPeer(), this.getValidBytes("Cannot search an unbounded memory area. Please set bounds with validBytes(long)."), pointer.getPeer(), pointer.getValidBytes("Cannot search for an unbounded content. Please set bounds with validBytes(long)."));
        return Pointer.pointerToAddress(l2, this.io);
    }

    public abstract Pointer<T> setInt(int var1);

    public abstract Pointer<T> setIntAtOffset(long var1, int var3);

    public Pointer<T> setIntAtIndex(long l2, int n2) {
        return this.setIntAtOffset(l2 << 2, n2);
    }

    public Pointer<T> setInts(int[] nArray) {
        return this.setIntsAtOffset(0L, nArray, 0, nArray.length);
    }

    public Pointer<T> setIntsAtOffset(long l2, int[] nArray) {
        return this.setIntsAtOffset(l2, nArray, 0, nArray.length);
    }

    public Pointer<T> setIntsAtOffset(long l2, int[] nArray, int n2, int n3) {
        long l3 = this.getPeer() + l2;
        if (this.validStart != -1L && (l3 < this.validStart || l3 + (long)(4 * n3) > this.validEnd)) {
            this.invalidPeer(l3, 4 * n3);
        }
        if (!this.isOrdered()) {
            JNI.set_int_array_disordered(l3, nArray, n2, n3);
            return this;
        }
        JNI.set_int_array(l3, nArray, n2, n3);
        return this;
    }

    public abstract int getInt();

    public abstract int getIntAtOffset(long var1);

    public int getIntAtIndex(long l2) {
        return this.getIntAtOffset(l2 << 2);
    }

    public int[] getInts(int n2) {
        return this.getIntsAtOffset(0L, n2);
    }

    public int[] getInts() {
        long l2 = this.getValidBytes("Cannot create array if remaining length is not known. Please use getInts(int length) instead.");
        return this.getInts((int)(l2 / 4L));
    }

    public int[] getIntsAtOffset(long l2, int n2) {
        long l3 = this.getPeer() + l2;
        if (this.validStart != -1L && (l3 < this.validStart || l3 + (long)(4 * n2) > this.validEnd)) {
            this.invalidPeer(l3, 4 * n2);
        }
        if (!this.isOrdered()) {
            return JNI.get_int_array_disordered(l3, n2);
        }
        return JNI.get_int_array(l3, n2);
    }

    public abstract Pointer<T> setLong(long var1);

    public abstract Pointer<T> setLongAtOffset(long var1, long var3);

    public Pointer<T> setLongAtIndex(long l2, long l3) {
        return this.setLongAtOffset(l2 << 3, l3);
    }

    public Pointer<T> setLongs(long[] lArray) {
        return this.setLongsAtOffset(0L, lArray, 0, lArray.length);
    }

    public Pointer<T> setLongsAtOffset(long l2, long[] lArray) {
        return this.setLongsAtOffset(l2, lArray, 0, lArray.length);
    }

    public Pointer<T> setLongsAtOffset(long l2, long[] lArray, int n2, int n3) {
        long l3 = this.getPeer() + l2;
        if (this.validStart != -1L && (l3 < this.validStart || l3 + (long)(n3 * 8) > this.validEnd)) {
            this.invalidPeer(l3, n3 * 8);
        }
        if (!this.isOrdered()) {
            JNI.set_long_array_disordered(l3, lArray, n2, n3);
            return this;
        }
        JNI.set_long_array(l3, lArray, n2, n3);
        return this;
    }

    public abstract long getLong();

    public abstract long getLongAtOffset(long var1);

    public long getLongAtIndex(long l2) {
        return this.getLongAtOffset(l2 << 3);
    }

    public long[] getLongs(int n2) {
        return this.getLongsAtOffset(0L, n2);
    }

    public long[] getLongs() {
        long l2 = this.getValidBytes("Cannot create array if remaining length is not known. Please use getLongs(int length) instead.");
        return this.getLongs((int)(l2 / 8L));
    }

    public long[] getLongsAtOffset(long l2, int n2) {
        long l3 = this.getPeer() + l2;
        if (this.validStart != -1L && (l3 < this.validStart || l3 + (long)(n2 * 8) > this.validEnd)) {
            this.invalidPeer(l3, n2 * 8);
        }
        if (!this.isOrdered()) {
            return JNI.get_long_array_disordered(l3, n2);
        }
        return JNI.get_long_array(l3, n2);
    }

    public abstract Pointer<T> setShort(short var1);

    public abstract Pointer<T> setShortAtOffset(long var1, short var3);

    public Pointer<T> setShortAtIndex(long l2, short s2) {
        return this.setShortAtOffset(l2 << 1, s2);
    }

    public Pointer<T> setShorts(short[] sArray) {
        return this.setShortsAtOffset(0L, sArray, 0, sArray.length);
    }

    public Pointer<T> setShortsAtOffset(long l2, short[] sArray) {
        return this.setShortsAtOffset(l2, sArray, 0, sArray.length);
    }

    public Pointer<T> setShortsAtOffset(long l2, short[] sArray, int n2, int n3) {
        long l3 = this.getPeer() + l2;
        if (this.validStart != -1L && (l3 < this.validStart || l3 + (long)(2 * n3) > this.validEnd)) {
            this.invalidPeer(l3, 2 * n3);
        }
        if (!this.isOrdered()) {
            JNI.set_short_array_disordered(l3, sArray, n2, n3);
            return this;
        }
        JNI.set_short_array(l3, sArray, n2, n3);
        return this;
    }

    public abstract short getShort();

    public abstract short getShortAtOffset(long var1);

    public short getShortAtIndex(long l2) {
        return this.getShortAtOffset(l2 << 1);
    }

    public short[] getShorts(int n2) {
        return this.getShortsAtOffset(0L, n2);
    }

    public short[] getShorts() {
        long l2 = this.getValidBytes("Cannot create array if remaining length is not known. Please use getShorts(int length) instead.");
        return this.getShorts((int)(l2 / 2L));
    }

    public short[] getShortsAtOffset(long l2, int n2) {
        long l3 = this.getPeer() + l2;
        if (this.validStart != -1L && (l3 < this.validStart || l3 + (long)(2 * n2) > this.validEnd)) {
            this.invalidPeer(l3, 2 * n2);
        }
        if (!this.isOrdered()) {
            return JNI.get_short_array_disordered(l3, n2);
        }
        return JNI.get_short_array(l3, n2);
    }

    public abstract Pointer<T> setByte(byte var1);

    public abstract Pointer<T> setByteAtOffset(long var1, byte var3);

    public Pointer<T> setByteAtIndex(long l2, byte by) {
        return this.setByteAtOffset(l2, by);
    }

    public Pointer<T> setBytes(byte[] byArray) {
        return this.setBytesAtOffset(0L, byArray, 0, byArray.length);
    }

    public Pointer<T> setBytesAtOffset(long l2, byte[] byArray) {
        return this.setBytesAtOffset(l2, byArray, 0, byArray.length);
    }

    public Pointer<T> setBytesAtOffset(long l2, byte[] byArray, int n2, int n3) {
        long l3 = this.getPeer() + l2;
        if (this.validStart != -1L && (l3 < this.validStart || l3 + (long)(1 * n3) > this.validEnd)) {
            this.invalidPeer(l3, 1 * n3);
        }
        JNI.set_byte_array(l3, byArray, n2, n3);
        return this;
    }

    public abstract byte getByte();

    public abstract byte getByteAtOffset(long var1);

    public byte getByteAtIndex(long l2) {
        return this.getByteAtOffset(l2);
    }

    public byte[] getBytes(int n2) {
        return this.getBytesAtOffset(0L, n2);
    }

    public byte[] getBytes() {
        long l2 = this.getValidBytes("Cannot create array if remaining length is not known. Please use getBytes(int length) instead.");
        return this.getBytes((int)l2);
    }

    public byte[] getBytesAtOffset(long l2, int n2) {
        long l3 = this.getPeer() + l2;
        if (this.validStart != -1L && (l3 < this.validStart || l3 + (long)(1 * n2) > this.validEnd)) {
            this.invalidPeer(l3, 1 * n2);
        }
        return JNI.get_byte_array(l3, n2);
    }

    public abstract Pointer<T> setChar(char var1);

    public abstract Pointer<T> setCharAtOffset(long var1, char var3);

    public Pointer<T> setCharAtIndex(long l2, char c2) {
        return this.setCharAtOffset(l2 * (long)Platform.WCHAR_T_SIZE, c2);
    }

    public Pointer<T> setChars(char[] cArray) {
        return this.setCharsAtOffset(0L, cArray, 0, cArray.length);
    }

    public Pointer<T> setCharsAtOffset(long l2, char[] cArray) {
        return this.setCharsAtOffset(l2, cArray, 0, cArray.length);
    }

    public Pointer<T> setCharsAtOffset(long l2, char[] cArray, int n2, int n3) {
        if (Platform.WCHAR_T_SIZE == 4) {
            return this.setIntsAtOffset(l2, Pointer.wcharsToInts(cArray, n2, n3));
        }
        long l3 = this.getPeer() + l2;
        if (this.validStart != -1L && (l3 < this.validStart || l3 + (long)(Platform.WCHAR_T_SIZE * n3) > this.validEnd)) {
            this.invalidPeer(l3, Platform.WCHAR_T_SIZE * n3);
        }
        if (!this.isOrdered()) {
            JNI.set_char_array_disordered(l3, cArray, n2, n3);
            return this;
        }
        JNI.set_char_array(l3, cArray, n2, n3);
        return this;
    }

    public abstract char getChar();

    public abstract char getCharAtOffset(long var1);

    public char getCharAtIndex(long l2) {
        return this.getCharAtOffset(l2 * (long)Platform.WCHAR_T_SIZE);
    }

    public char[] getChars(int n2) {
        return this.getCharsAtOffset(0L, n2);
    }

    public char[] getChars() {
        long l2 = this.getValidBytes("Cannot create array if remaining length is not known. Please use getChars(int length) instead.");
        return this.getChars((int)(l2 / (long)Platform.WCHAR_T_SIZE));
    }

    public char[] getCharsAtOffset(long l2, int n2) {
        if (Platform.WCHAR_T_SIZE == 4) {
            return Pointer.intsToWChars(this.getIntsAtOffset(l2, n2));
        }
        long l3 = this.getPeer() + l2;
        if (this.validStart != -1L && (l3 < this.validStart || l3 + (long)(Platform.WCHAR_T_SIZE * n2) > this.validEnd)) {
            this.invalidPeer(l3, Platform.WCHAR_T_SIZE * n2);
        }
        if (!this.isOrdered()) {
            return JNI.get_char_array_disordered(l3, n2);
        }
        return JNI.get_char_array(l3, n2);
    }

    public abstract Pointer<T> setFloat(float var1);

    public abstract Pointer<T> setFloatAtOffset(long var1, float var3);

    public Pointer<T> setFloatAtIndex(long l2, float f2) {
        return this.setFloatAtOffset(l2 << 2, f2);
    }

    public Pointer<T> setFloats(float[] fArray) {
        return this.setFloatsAtOffset(0L, fArray, 0, fArray.length);
    }

    public Pointer<T> setFloatsAtOffset(long l2, float[] fArray) {
        return this.setFloatsAtOffset(l2, fArray, 0, fArray.length);
    }

    public Pointer<T> setFloatsAtOffset(long l2, float[] fArray, int n2, int n3) {
        long l3 = this.getPeer() + l2;
        if (this.validStart != -1L && (l3 < this.validStart || l3 + (long)(4 * n3) > this.validEnd)) {
            this.invalidPeer(l3, 4 * n3);
        }
        if (!this.isOrdered()) {
            JNI.set_float_array_disordered(l3, fArray, n2, n3);
            return this;
        }
        JNI.set_float_array(l3, fArray, n2, n3);
        return this;
    }

    public abstract float getFloat();

    public abstract float getFloatAtOffset(long var1);

    public float getFloatAtIndex(long l2) {
        return this.getFloatAtOffset(l2 << 2);
    }

    public float[] getFloats(int n2) {
        return this.getFloatsAtOffset(0L, n2);
    }

    public float[] getFloats() {
        long l2 = this.getValidBytes("Cannot create array if remaining length is not known. Please use getFloats(int length) instead.");
        return this.getFloats((int)(l2 / 4L));
    }

    public float[] getFloatsAtOffset(long l2, int n2) {
        long l3 = this.getPeer() + l2;
        if (this.validStart != -1L && (l3 < this.validStart || l3 + (long)(4 * n2) > this.validEnd)) {
            this.invalidPeer(l3, 4 * n2);
        }
        if (!this.isOrdered()) {
            return JNI.get_float_array_disordered(l3, n2);
        }
        return JNI.get_float_array(l3, n2);
    }

    public abstract Pointer<T> setDouble(double var1);

    public abstract Pointer<T> setDoubleAtOffset(long var1, double var3);

    public Pointer<T> setDoubleAtIndex(long l2, double d2) {
        return this.setDoubleAtOffset(l2 << 3, d2);
    }

    public Pointer<T> setDoubles(double[] dArray) {
        return this.setDoublesAtOffset(0L, dArray, 0, dArray.length);
    }

    public Pointer<T> setDoublesAtOffset(long l2, double[] dArray) {
        return this.setDoublesAtOffset(l2, dArray, 0, dArray.length);
    }

    public Pointer<T> setDoublesAtOffset(long l2, double[] dArray, int n2, int n3) {
        long l3 = this.getPeer() + l2;
        if (this.validStart != -1L && (l3 < this.validStart || l3 + (long)(n3 * 8) > this.validEnd)) {
            this.invalidPeer(l3, n3 * 8);
        }
        if (!this.isOrdered()) {
            JNI.set_double_array_disordered(l3, dArray, n2, n3);
            return this;
        }
        JNI.set_double_array(l3, dArray, n2, n3);
        return this;
    }

    public abstract double getDouble();

    public abstract double getDoubleAtOffset(long var1);

    public double getDoubleAtIndex(long l2) {
        return this.getDoubleAtOffset(l2 << 3);
    }

    public double[] getDoubles(int n2) {
        return this.getDoublesAtOffset(0L, n2);
    }

    public double[] getDoubles() {
        long l2 = this.getValidBytes("Cannot create array if remaining length is not known. Please use getDoubles(int length) instead.");
        return this.getDoubles((int)(l2 / 8L));
    }

    public double[] getDoublesAtOffset(long l2, int n2) {
        long l3 = this.getPeer() + l2;
        if (this.validStart != -1L && (l3 < this.validStart || l3 + (long)(n2 * 8) > this.validEnd)) {
            this.invalidPeer(l3, n2 * 8);
        }
        if (!this.isOrdered()) {
            return JNI.get_double_array_disordered(l3, n2);
        }
        return JNI.get_double_array(l3, n2);
    }

    public abstract Pointer<T> setBoolean(boolean var1);

    public abstract Pointer<T> setBooleanAtOffset(long var1, boolean var3);

    public Pointer<T> setBooleanAtIndex(long l2, boolean bl) {
        return this.setBooleanAtOffset(l2, bl);
    }

    public Pointer<T> setBooleans(boolean[] blArray) {
        return this.setBooleansAtOffset(0L, blArray, 0, blArray.length);
    }

    public Pointer<T> setBooleansAtOffset(long l2, boolean[] blArray) {
        return this.setBooleansAtOffset(l2, blArray, 0, blArray.length);
    }

    public Pointer<T> setBooleansAtOffset(long l2, boolean[] blArray, int n2, int n3) {
        long l3 = this.getPeer() + l2;
        if (this.validStart != -1L && (l3 < this.validStart || l3 + (long)(1 * n3) > this.validEnd)) {
            this.invalidPeer(l3, 1 * n3);
        }
        JNI.set_boolean_array(l3, blArray, n2, n3);
        return this;
    }

    public abstract boolean getBoolean();

    public abstract boolean getBooleanAtOffset(long var1);

    public boolean getBooleanAtIndex(long l2) {
        return this.getBooleanAtOffset(l2);
    }

    public boolean[] getBooleans(int n2) {
        return this.getBooleansAtOffset(0L, n2);
    }

    public boolean[] getBooleans() {
        long l2 = this.getValidBytes("Cannot create array if remaining length is not known. Please use getBooleans(int length) instead.");
        return this.getBooleans((int)l2);
    }

    public boolean[] getBooleansAtOffset(long l2, int n2) {
        long l3 = this.getPeer() + l2;
        if (this.validStart != -1L && (l3 < this.validStart || l3 + (long)(1 * n2) > this.validEnd)) {
            this.invalidPeer(l3, 1 * n2);
        }
        return JNI.get_boolean_array(l3, n2);
    }

    public void getInts(int[] nArray) {
        this.getIntBuffer().get(nArray);
    }

    public void getInts(IntBuffer intBuffer) {
        intBuffer.duplicate().put(this.getIntBuffer());
    }

    public void getIntsAtOffset(long l2, int[] nArray, int n2, int n3) {
        this.getIntBufferAtOffset(l2, n3).get(nArray, n2, n3);
    }

    public Pointer<T> setInts(IntBuffer intBuffer) {
        return this.setIntsAtOffset(0L, intBuffer, 0L, (long)intBuffer.capacity());
    }

    public Pointer<T> setIntsAtOffset(long l2, IntBuffer intBuffer) {
        return this.setIntsAtOffset(l2, intBuffer, 0L, (long)intBuffer.capacity());
    }

    public Pointer<T> setIntsAtOffset(long l2, IntBuffer intBuffer, long l3, long l4) {
        if (intBuffer == null) {
            throw new IllegalArgumentException("Null values");
        }
        if (intBuffer.isDirect()) {
            long l5 = l4 << 2;
            long l6 = l3 << 2;
            long l7 = JNI.getDirectBufferCapacity(intBuffer);
            if ((l7 <<= 2) < l6 + l5) {
                throw new IndexOutOfBoundsException("The provided buffer has a capacity (" + l7 + " bytes) smaller than the requested write operation (" + l5 + " bytes starting at byte offset " + l6 + ")");
            }
            long l8 = this.getPeer() + l2;
            if (this.validStart != -1L && (l8 < this.validStart || l8 + 4L * l4 > this.validEnd)) {
                this.invalidPeer(l8, 4L * l4);
            }
            JNI.memcpy(l8, JNI.getDirectBufferAddress(intBuffer) + l6, l5);
        } else if (intBuffer.isReadOnly()) {
            this.getIntBufferAtOffset(l2, l4).put(intBuffer.duplicate());
        } else {
            this.setIntsAtOffset(l2, intBuffer.array(), (int)((long)intBuffer.arrayOffset() + l3), (int)l4);
        }
        return this;
    }

    public IntBuffer getIntBuffer(long l2) {
        return this.getIntBufferAtOffset(0L, l2);
    }

    public IntBuffer getIntBuffer() {
        long l2 = this.getValidBytes("Cannot create buffer if remaining length is not known. Please use getIntBuffer(long length) instead.");
        return this.getIntBufferAtOffset(0L, l2 / 4L);
    }

    public IntBuffer getIntBufferAtOffset(long l2, long l3) {
        long l4 = 4L * l3;
        long l5 = this.getPeer() + l2;
        if (this.validStart != -1L && (l5 < this.validStart || l5 + l4 > this.validEnd)) {
            this.invalidPeer(l5, l4);
        }
        ByteBuffer byteBuffer = JNI.newDirectByteBuffer(l5, l4);
        byteBuffer.order(this.order());
        return byteBuffer.asIntBuffer();
    }

    public void getLongs(long[] lArray) {
        this.getLongBuffer().get(lArray);
    }

    public void getLongs(LongBuffer longBuffer) {
        longBuffer.duplicate().put(this.getLongBuffer());
    }

    public void getLongsAtOffset(long l2, long[] lArray, int n2, int n3) {
        this.getLongBufferAtOffset(l2, n3).get(lArray, n2, n3);
    }

    public Pointer<T> setLongs(LongBuffer longBuffer) {
        return this.setLongsAtOffset(0L, longBuffer, 0L, (long)longBuffer.capacity());
    }

    public Pointer<T> setLongsAtOffset(long l2, LongBuffer longBuffer) {
        return this.setLongsAtOffset(l2, longBuffer, 0L, (long)longBuffer.capacity());
    }

    public Pointer<T> setLongsAtOffset(long l2, LongBuffer longBuffer, long l3, long l4) {
        if (longBuffer == null) {
            throw new IllegalArgumentException("Null values");
        }
        if (longBuffer.isDirect()) {
            long l5 = l4 << 3;
            long l6 = l3 << 3;
            long l7 = JNI.getDirectBufferCapacity(longBuffer);
            if ((l7 <<= 3) < l6 + l5) {
                throw new IndexOutOfBoundsException("The provided buffer has a capacity (" + l7 + " bytes) smaller than the requested write operation (" + l5 + " bytes starting at byte offset " + l6 + ")");
            }
            long l8 = this.getPeer() + l2;
            if (this.validStart != -1L && (l8 < this.validStart || l8 + 8L * l4 > this.validEnd)) {
                this.invalidPeer(l8, 8L * l4);
            }
            JNI.memcpy(l8, JNI.getDirectBufferAddress(longBuffer) + l6, l5);
        } else if (longBuffer.isReadOnly()) {
            this.getLongBufferAtOffset(l2, l4).put(longBuffer.duplicate());
        } else {
            this.setLongsAtOffset(l2, longBuffer.array(), (int)((long)longBuffer.arrayOffset() + l3), (int)l4);
        }
        return this;
    }

    public LongBuffer getLongBuffer(long l2) {
        return this.getLongBufferAtOffset(0L, l2);
    }

    public LongBuffer getLongBuffer() {
        long l2 = this.getValidBytes("Cannot create buffer if remaining length is not known. Please use getLongBuffer(long length) instead.");
        return this.getLongBufferAtOffset(0L, l2 / 8L);
    }

    public LongBuffer getLongBufferAtOffset(long l2, long l3) {
        long l4 = 8L * l3;
        long l5 = this.getPeer() + l2;
        if (this.validStart != -1L && (l5 < this.validStart || l5 + l4 > this.validEnd)) {
            this.invalidPeer(l5, l4);
        }
        ByteBuffer byteBuffer = JNI.newDirectByteBuffer(l5, l4);
        byteBuffer.order(this.order());
        return byteBuffer.asLongBuffer();
    }

    public void getShorts(short[] sArray) {
        this.getShortBuffer().get(sArray);
    }

    public void getShorts(ShortBuffer shortBuffer) {
        shortBuffer.duplicate().put(this.getShortBuffer());
    }

    public void getShortsAtOffset(long l2, short[] sArray, int n2, int n3) {
        this.getShortBufferAtOffset(l2, n3).get(sArray, n2, n3);
    }

    public Pointer<T> setShorts(ShortBuffer shortBuffer) {
        return this.setShortsAtOffset(0L, shortBuffer, 0L, (long)shortBuffer.capacity());
    }

    public Pointer<T> setShortsAtOffset(long l2, ShortBuffer shortBuffer) {
        return this.setShortsAtOffset(l2, shortBuffer, 0L, (long)shortBuffer.capacity());
    }

    public Pointer<T> setShortsAtOffset(long l2, ShortBuffer shortBuffer, long l3, long l4) {
        if (shortBuffer == null) {
            throw new IllegalArgumentException("Null values");
        }
        if (shortBuffer.isDirect()) {
            long l5 = l4 << 1;
            long l6 = l3 << 1;
            long l7 = JNI.getDirectBufferCapacity(shortBuffer);
            if ((l7 <<= 1) < l6 + l5) {
                throw new IndexOutOfBoundsException("The provided buffer has a capacity (" + l7 + " bytes) smaller than the requested write operation (" + l5 + " bytes starting at byte offset " + l6 + ")");
            }
            long l8 = this.getPeer() + l2;
            if (this.validStart != -1L && (l8 < this.validStart || l8 + 2L * l4 > this.validEnd)) {
                this.invalidPeer(l8, 2L * l4);
            }
            JNI.memcpy(l8, JNI.getDirectBufferAddress(shortBuffer) + l6, l5);
        } else if (shortBuffer.isReadOnly()) {
            this.getShortBufferAtOffset(l2, l4).put(shortBuffer.duplicate());
        } else {
            this.setShortsAtOffset(l2, shortBuffer.array(), (int)((long)shortBuffer.arrayOffset() + l3), (int)l4);
        }
        return this;
    }

    public ShortBuffer getShortBuffer(long l2) {
        return this.getShortBufferAtOffset(0L, l2);
    }

    public ShortBuffer getShortBuffer() {
        long l2 = this.getValidBytes("Cannot create buffer if remaining length is not known. Please use getShortBuffer(long length) instead.");
        return this.getShortBufferAtOffset(0L, l2 / 2L);
    }

    public ShortBuffer getShortBufferAtOffset(long l2, long l3) {
        long l4 = 2L * l3;
        long l5 = this.getPeer() + l2;
        if (this.validStart != -1L && (l5 < this.validStart || l5 + l4 > this.validEnd)) {
            this.invalidPeer(l5, l4);
        }
        ByteBuffer byteBuffer = JNI.newDirectByteBuffer(l5, l4);
        byteBuffer.order(this.order());
        return byteBuffer.asShortBuffer();
    }

    public void getBytes(byte[] byArray) {
        this.getByteBuffer().get(byArray);
    }

    public void getBytes(ByteBuffer byteBuffer) {
        byteBuffer.duplicate().put(this.getByteBuffer());
    }

    public void getBytesAtOffset(long l2, byte[] byArray, int n2, int n3) {
        this.getByteBufferAtOffset(l2, n3).get(byArray, n2, n3);
    }

    public Pointer<T> setBytes(ByteBuffer byteBuffer) {
        return this.setBytesAtOffset(0L, byteBuffer, 0L, (long)byteBuffer.capacity());
    }

    public Pointer<T> setBytesAtOffset(long l2, ByteBuffer byteBuffer) {
        return this.setBytesAtOffset(l2, byteBuffer, 0L, (long)byteBuffer.capacity());
    }

    public Pointer<T> setBytesAtOffset(long l2, ByteBuffer byteBuffer, long l3, long l4) {
        if (byteBuffer == null) {
            throw new IllegalArgumentException("Null values");
        }
        if (byteBuffer.isDirect()) {
            long l5 = l4;
            long l6 = l3;
            long l7 = JNI.getDirectBufferCapacity(byteBuffer);
            long l8 = l7;
            l8 = l7;
            if (l7 < l6 + l5) {
                throw new IndexOutOfBoundsException("The provided buffer has a capacity (" + l8 + " bytes) smaller than the requested write operation (" + l5 + " bytes starting at byte offset " + l6 + ")");
            }
            long l9 = this.getPeer() + l2;
            if (this.validStart != -1L && (l9 < this.validStart || l9 + l4 * 1L > this.validEnd)) {
                this.invalidPeer(l9, l4 * 1L);
            }
            JNI.memcpy(l9, JNI.getDirectBufferAddress(byteBuffer) + l6, l5);
        } else if (byteBuffer.isReadOnly()) {
            this.getByteBufferAtOffset(l2, l4).put(byteBuffer.duplicate());
        } else {
            this.setBytesAtOffset(l2, byteBuffer.array(), (int)((long)byteBuffer.arrayOffset() + l3), (int)l4);
        }
        return this;
    }

    public ByteBuffer getByteBuffer(long l2) {
        return this.getByteBufferAtOffset(0L, l2);
    }

    public ByteBuffer getByteBuffer() {
        long l2 = this.getValidBytes("Cannot create buffer if remaining length is not known. Please use getByteBuffer(long length) instead.");
        return this.getByteBufferAtOffset(0L, l2);
    }

    public ByteBuffer getByteBufferAtOffset(long l2, long l3) {
        long l4 = l3 * 1L;
        long l5 = this.getPeer() + l2;
        if (this.validStart != -1L && (l5 < this.validStart || l5 + l4 > this.validEnd)) {
            this.invalidPeer(l5, l4);
        }
        ByteBuffer byteBuffer = JNI.newDirectByteBuffer(l5, l4);
        byteBuffer.order(this.order());
        return byteBuffer;
    }

    public Pointer<T> setChars(CharBuffer charBuffer) {
        return this.setCharsAtOffset(0L, charBuffer, 0L, (long)charBuffer.capacity());
    }

    public Pointer<T> setCharsAtOffset(long l2, CharBuffer charBuffer) {
        return this.setCharsAtOffset(l2, charBuffer, 0L, (long)charBuffer.capacity());
    }

    public Pointer<T> setCharsAtOffset(long l2, CharBuffer charBuffer, long l3, long l4) {
        if (charBuffer == null) {
            throw new IllegalArgumentException("Null values");
        }
        if (Platform.WCHAR_T_SIZE == 4) {
            int n2 = 0;
            while ((long)n2 < l4) {
                this.setCharAtOffset(l2 + (long)n2, charBuffer.get((int)(l3 + (long)n2)));
                ++n2;
            }
            return this;
        }
        if (charBuffer.isDirect()) {
            long l5 = l4 * (long)Platform.WCHAR_T_SIZE;
            long l6 = l3 * (long)Platform.WCHAR_T_SIZE;
            long l7 = JNI.getDirectBufferCapacity(charBuffer);
            if ((l7 *= (long)Platform.WCHAR_T_SIZE) < l6 + l5) {
                throw new IndexOutOfBoundsException("The provided buffer has a capacity (" + l7 + " bytes) smaller than the requested write operation (" + l5 + " bytes starting at byte offset " + l6 + ")");
            }
            long l8 = this.getPeer() + l2;
            if (this.validStart != -1L && (l8 < this.validStart || l8 + (long)Platform.WCHAR_T_SIZE * l4 > this.validEnd)) {
                this.invalidPeer(l8, (long)Platform.WCHAR_T_SIZE * l4);
            }
            JNI.memcpy(l8, JNI.getDirectBufferAddress(charBuffer) + l6, l5);
        } else {
            this.setCharsAtOffset(l2, charBuffer.array(), (int)((long)charBuffer.arrayOffset() + l3), (int)l4);
        }
        return this;
    }

    public void getFloats(float[] fArray) {
        this.getFloatBuffer().get(fArray);
    }

    public void getFloats(FloatBuffer floatBuffer) {
        floatBuffer.duplicate().put(this.getFloatBuffer());
    }

    public void getFloatsAtOffset(long l2, float[] fArray, int n2, int n3) {
        this.getFloatBufferAtOffset(l2, n3).get(fArray, n2, n3);
    }

    public Pointer<T> setFloats(FloatBuffer floatBuffer) {
        return this.setFloatsAtOffset(0L, floatBuffer, 0L, (long)floatBuffer.capacity());
    }

    public Pointer<T> setFloatsAtOffset(long l2, FloatBuffer floatBuffer) {
        return this.setFloatsAtOffset(l2, floatBuffer, 0L, (long)floatBuffer.capacity());
    }

    public Pointer<T> setFloatsAtOffset(long l2, FloatBuffer floatBuffer, long l3, long l4) {
        if (floatBuffer == null) {
            throw new IllegalArgumentException("Null values");
        }
        if (floatBuffer.isDirect()) {
            long l5 = l4 << 2;
            long l6 = l3 << 2;
            long l7 = JNI.getDirectBufferCapacity(floatBuffer);
            if ((l7 <<= 2) < l6 + l5) {
                throw new IndexOutOfBoundsException("The provided buffer has a capacity (" + l7 + " bytes) smaller than the requested write operation (" + l5 + " bytes starting at byte offset " + l6 + ")");
            }
            long l8 = this.getPeer() + l2;
            if (this.validStart != -1L && (l8 < this.validStart || l8 + 4L * l4 > this.validEnd)) {
                this.invalidPeer(l8, 4L * l4);
            }
            JNI.memcpy(l8, JNI.getDirectBufferAddress(floatBuffer) + l6, l5);
        } else if (floatBuffer.isReadOnly()) {
            this.getFloatBufferAtOffset(l2, l4).put(floatBuffer.duplicate());
        } else {
            this.setFloatsAtOffset(l2, floatBuffer.array(), (int)((long)floatBuffer.arrayOffset() + l3), (int)l4);
        }
        return this;
    }

    public FloatBuffer getFloatBuffer(long l2) {
        return this.getFloatBufferAtOffset(0L, l2);
    }

    public FloatBuffer getFloatBuffer() {
        long l2 = this.getValidBytes("Cannot create buffer if remaining length is not known. Please use getFloatBuffer(long length) instead.");
        return this.getFloatBufferAtOffset(0L, l2 / 4L);
    }

    public FloatBuffer getFloatBufferAtOffset(long l2, long l3) {
        long l4 = 4L * l3;
        long l5 = this.getPeer() + l2;
        if (this.validStart != -1L && (l5 < this.validStart || l5 + l4 > this.validEnd)) {
            this.invalidPeer(l5, l4);
        }
        ByteBuffer byteBuffer = JNI.newDirectByteBuffer(l5, l4);
        byteBuffer.order(this.order());
        return byteBuffer.asFloatBuffer();
    }

    public void getDoubles(double[] dArray) {
        this.getDoubleBuffer().get(dArray);
    }

    public void getDoubles(DoubleBuffer doubleBuffer) {
        doubleBuffer.duplicate().put(this.getDoubleBuffer());
    }

    public void getDoublesAtOffset(long l2, double[] dArray, int n2, int n3) {
        this.getDoubleBufferAtOffset(l2, n3).get(dArray, n2, n3);
    }

    public Pointer<T> setDoubles(DoubleBuffer doubleBuffer) {
        return this.setDoublesAtOffset(0L, doubleBuffer, 0L, (long)doubleBuffer.capacity());
    }

    public Pointer<T> setDoublesAtOffset(long l2, DoubleBuffer doubleBuffer) {
        return this.setDoublesAtOffset(l2, doubleBuffer, 0L, (long)doubleBuffer.capacity());
    }

    public Pointer<T> setDoublesAtOffset(long l2, DoubleBuffer doubleBuffer, long l3, long l4) {
        if (doubleBuffer == null) {
            throw new IllegalArgumentException("Null values");
        }
        if (doubleBuffer.isDirect()) {
            long l5 = l4 << 3;
            long l6 = l3 << 3;
            long l7 = JNI.getDirectBufferCapacity(doubleBuffer);
            if ((l7 <<= 3) < l6 + l5) {
                throw new IndexOutOfBoundsException("The provided buffer has a capacity (" + l7 + " bytes) smaller than the requested write operation (" + l5 + " bytes starting at byte offset " + l6 + ")");
            }
            long l8 = this.getPeer() + l2;
            if (this.validStart != -1L && (l8 < this.validStart || l8 + 8L * l4 > this.validEnd)) {
                this.invalidPeer(l8, 8L * l4);
            }
            JNI.memcpy(l8, JNI.getDirectBufferAddress(doubleBuffer) + l6, l5);
        } else if (doubleBuffer.isReadOnly()) {
            this.getDoubleBufferAtOffset(l2, l4).put(doubleBuffer.duplicate());
        } else {
            this.setDoublesAtOffset(l2, doubleBuffer.array(), (int)((long)doubleBuffer.arrayOffset() + l3), (int)l4);
        }
        return this;
    }

    public DoubleBuffer getDoubleBuffer(long l2) {
        return this.getDoubleBufferAtOffset(0L, l2);
    }

    public DoubleBuffer getDoubleBuffer() {
        long l2 = this.getValidBytes("Cannot create buffer if remaining length is not known. Please use getDoubleBuffer(long length) instead.");
        return this.getDoubleBufferAtOffset(0L, l2 / 8L);
    }

    public DoubleBuffer getDoubleBufferAtOffset(long l2, long l3) {
        long l4 = 8L * l3;
        long l5 = this.getPeer() + l2;
        if (this.validStart != -1L && (l5 < this.validStart || l5 + l4 > this.validEnd)) {
            this.invalidPeer(l5, l4);
        }
        ByteBuffer byteBuffer = JNI.newDirectByteBuffer(l5, l4);
        byteBuffer.order(this.order());
        return byteBuffer.asDoubleBuffer();
    }

    private static void notAString(StringType stringType, String string) {
        throw new RuntimeException("There is no " + (Object)((Object)stringType) + " String here ! (" + string + ")");
    }

    protected void checkIntRefCount(StringType stringType, long l2) {
        int n2 = this.getIntAtOffset(l2);
        if (n2 <= 0) {
            Pointer.notAString(stringType, "invalid refcount: " + n2);
        }
    }

    public String getString(StringType stringType) {
        return this.getStringAtOffset(0L, stringType, null);
    }

    public String getString(StringType stringType, Charset charset) {
        return this.getStringAtOffset(0L, stringType, charset);
    }

    String getSTLStringAtOffset(long l2, StringType stringType, Charset charset) {
        char c2;
        Pointer<?> pointer;
        long l3;
        boolean bl = stringType == StringType.WideSTL;
        int n2 = bl ? 16 * Platform.WCHAR_T_SIZE : 16;
        long l4 = this.getSizeTAtOffset(l2 + (long)n2 + (long)SIZE);
        if (l4 < 15L) {
            l3 = l2;
            pointer = this;
        } else {
            l3 = 0L;
            pointer = this.getPointerAtOffset(l2 + (long)n2 + (long)SIZE);
        }
        if ((c2 = bl ? pointer.getCharAtOffset(l3 + l4 * (long)Platform.WCHAR_T_SIZE) : pointer.getByteAtOffset(l3 + l4)) != '\u0000') {
            Pointer.notAString(stringType, "STL string format is not recognized : did not find a NULL char at the expected end of string of expected length " + l4);
        }
        return pointer.getStringAtOffset(l3, bl ? StringType.WideC : StringType.C, charset);
    }

    static <U> Pointer<U> setSTLString(Pointer<U> pointer, long l2, String string, StringType stringType, Charset charset) {
        char c2;
        Pointer<Object> pointer2;
        long l3;
        boolean bl = stringType == StringType.WideSTL;
        int n2 = bl ? 16 * Platform.WCHAR_T_SIZE : 16;
        long l4 = l2 + (long)n2 + (long)SIZE;
        long l5 = l4 + (long)SIZE;
        long l6 = string.length();
        if (pointer == null) {
            throw new UnsupportedOperationException("Cannot create STL strings (yet)");
        }
        long l7 = pointer.getSizeTAtOffset(l4);
        long l8 = pointer.getSizeTAtOffset(l5);
        if (l7 < 0L || l8 < 0L || l7 > l8) {
            Pointer.notAString(stringType, "STL string format not recognized : currentLength = " + l7 + ", currentCapacity = " + l8);
        }
        if (l6 > l8) {
            throw new RuntimeException("The target STL string is not large enough to write a string of length " + l6 + " (current capacity = " + l8 + ")");
        }
        pointer.setSizeTAtOffset(l4, l6);
        if (l6 < 15L) {
            l3 = l2;
            pointer2 = pointer;
        } else {
            l3 = 0L;
            pointer2 = pointer.getPointerAtOffset(l2 + (long)n2 + (long)SizeT.SIZE);
        }
        if ((c2 = bl ? pointer2.getCharAtOffset(l3 + l7 * (long)Platform.WCHAR_T_SIZE) : pointer2.getByteAtOffset(l3 + l7)) != '\u0000') {
            Pointer.notAString(stringType, "STL string format is not recognized : did not find a NULL char at the expected end of string of expected length " + l7);
        }
        pointer2.setStringAtOffset(l3, string, bl ? StringType.WideC : StringType.C, charset);
        return pointer;
    }

    public String getStringAtOffset(long l2, StringType stringType, Charset charset) {
        try {
            switch (stringType) {
                case PascalShort: {
                    long l3 = this.getByteAtOffset(l2) & 0xFF;
                    return new String(this.getBytesAtOffset(l2 + 1L, SizeT.safeIntCast(l3)), Pointer.charset(charset));
                }
                case PascalWide: {
                    this.checkIntRefCount(stringType, l2 - 8L);
                }
                case BSTR: {
                    long l4 = this.getIntAtOffset(l2 - 4L);
                    if (l4 < 0L || (l4 & 1L) == 1L) {
                        Pointer.notAString(stringType, "invalid byte length: " + l4);
                    }
                    if (this.getCharAtOffset(l2 + l4) != '\u0000') {
                        Pointer.notAString(stringType, "no null short after the " + l4 + " declared bytes");
                    }
                    return new String(this.getCharsAtOffset(l2, SizeT.safeIntCast(l4 / (long)Platform.WCHAR_T_SIZE)));
                }
                case PascalAnsi: {
                    this.checkIntRefCount(stringType, l2 - 8L);
                    long l5 = this.getIntAtOffset(l2 - 4L);
                    if (l5 < 0L) {
                        Pointer.notAString(stringType, "invalid byte length: " + l5);
                    }
                    if (this.getByteAtOffset(l2 + l5) != 0) {
                        Pointer.notAString(stringType, "no null short after the " + l5 + " declared bytes");
                    }
                    return new String(this.getBytesAtOffset(l2, SizeT.safeIntCast(l5)), Pointer.charset(charset));
                }
                case C: {
                    long l6 = this.strlen(l2);
                    return new String(this.getBytesAtOffset(l2, SizeT.safeIntCast(l6)), Pointer.charset(charset));
                }
                case WideC: {
                    long l7 = this.wcslen(l2);
                    return new String(this.getCharsAtOffset(l2, SizeT.safeIntCast(l7)));
                }
                case STL: 
                case WideSTL: {
                    return this.getSTLStringAtOffset(l2, stringType, charset);
                }
            }
            throw new RuntimeException("Unhandled string type : " + (Object)((Object)stringType));
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            UnsupportedEncodingException unsupportedEncodingException2 = unsupportedEncodingException;
            Pointer.throwUnexpected(unsupportedEncodingException);
            return null;
        }
    }

    public Pointer<T> setString(String string, StringType stringType) {
        return Pointer.setString(this, 0L, string, stringType, null);
    }

    public Pointer<T> setStringAtOffset(long l2, String string, StringType stringType, Charset charset) {
        return Pointer.setString(this, l2, string, stringType, charset);
    }

    private static String charset(Charset charset) {
        return (charset == null ? Charset.defaultCharset() : charset).name();
    }

    static <U> Pointer<U> setString(Pointer<U> pointer, long l2, String object, StringType stringType, Charset charset) {
        if (object == null) {
            return null;
        }
        try {
            switch (stringType) {
                case PascalShort: {
                    byte[] byArray = ((String)object).getBytes(Pointer.charset(charset));
                    object = byArray;
                    int n2 = byArray.length;
                    if (pointer == null) {
                        pointer = Pointer.allocateBytes(n2 + 1);
                    }
                    if (n2 > 255) {
                        throw new IllegalArgumentException("Pascal strings cannot be more than 255 chars long (tried to write string of byte length " + n2 + ")");
                    }
                    pointer.setByteAtOffset(l2, (byte)n2);
                    pointer.setBytesAtOffset(l2 + 1L, (byte[])object, 0, n2);
                    break;
                }
                case C: {
                    byte[] byArray = ((String)object).getBytes(Pointer.charset(charset));
                    object = byArray;
                    int n3 = byArray.length;
                    if (pointer == null) {
                        pointer = Pointer.allocateBytes(n3 + 1);
                    }
                    pointer.setBytesAtOffset(l2, (byte[])object, 0, n3);
                    pointer.setByteAtOffset(l2 + (long)n3, (byte)0);
                    break;
                }
                case WideC: {
                    char[] cArray = ((String)object).toCharArray();
                    object = cArray;
                    int n4 = cArray.length * Platform.WCHAR_T_SIZE;
                    if (pointer == null) {
                        pointer = Pointer.allocateChars(n4 + 2);
                    }
                    pointer.setCharsAtOffset(l2, (char[])object);
                    pointer.setCharAtOffset(l2 + (long)n4, '\u0000');
                    break;
                }
                case PascalWide: {
                    int n5;
                    char[] cArray = ((String)object).toCharArray();
                    object = cArray;
                    int n6 = cArray.length * Platform.WCHAR_T_SIZE;
                    if (pointer == null) {
                        pointer = Pointer.allocateChars(n6 + 8 + 2);
                        n5 = 8;
                        l2 = 8L;
                    } else {
                        n5 = 0;
                    }
                    pointer.setIntAtOffset(l2 - 8L, 1);
                    pointer.setIntAtOffset(l2 - 4L, n6);
                    pointer.setCharsAtOffset(l2, (char[])object);
                    pointer.setCharAtOffset(l2 + (long)n6, '\u0000');
                    return pointer.offset(n5);
                }
                case PascalAnsi: {
                    int n7;
                    byte[] byArray = ((String)object).getBytes(Pointer.charset(charset));
                    object = byArray;
                    int n8 = byArray.length;
                    if (pointer == null) {
                        pointer = Pointer.allocateBytes(n8 + 8 + 1);
                        n7 = 8;
                        l2 = 8L;
                    } else {
                        n7 = 0;
                    }
                    pointer.setIntAtOffset(l2 - 8L, 1);
                    pointer.setIntAtOffset(l2 - 4L, n8);
                    pointer.setBytesAtOffset(l2, (byte[])object);
                    pointer.setByteAtOffset(l2 + (long)n8, (byte)0);
                    return pointer.offset(n7);
                }
                case BSTR: {
                    int n9;
                    char[] cArray = ((String)object).toCharArray();
                    object = cArray;
                    int n10 = cArray.length * Platform.WCHAR_T_SIZE;
                    if (pointer == null) {
                        pointer = Pointer.allocateChars(n10 + 4 + 2);
                        n9 = 4;
                        l2 = 4L;
                    } else {
                        n9 = 0;
                    }
                    pointer.setIntAtOffset(l2 - 4L, n10);
                    pointer.setCharsAtOffset(l2, (char[])object);
                    pointer.setCharAtOffset(l2 + (long)n10, '\u0000');
                    return pointer.offset(n9);
                }
                case STL: 
                case WideSTL: {
                    return Pointer.setSTLString(pointer, l2, (String)object, stringType, charset);
                }
                default: {
                    throw new RuntimeException("Unhandled string type : " + (Object)((Object)stringType));
                }
            }
            return pointer;
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            object = unsupportedEncodingException;
            Pointer.throwUnexpected(unsupportedEncodingException);
            return null;
        }
    }

    public static Pointer<?> pointerToString(String string, StringType stringType, Charset charset) {
        return Pointer.setString(null, 0L, string, stringType, charset);
    }

    public static Pointer<Byte> pointerToCString(String string) {
        return Pointer.setString(null, 0L, string, StringType.C, null);
    }

    public static Pointer<Pointer<Byte>> pointerToCStrings(String ... stringArray) {
        if (stringArray == null) {
            return null;
        }
        final int n2 = stringArray.length;
        final Pointer[] pointerArray = new Pointer[n2];
        Pointer<Pointer<Byte>> pointer = Pointer.allocateArray(PointerIO.getPointerInstance(Byte.class), n2, new Releaser(){

            @Override
            public final void release(Pointer<?> pointer) {
                for (int i2 = 0; i2 < n2; ++i2) {
                    Pointer pointer2 = pointerArray[i2];
                    if (pointer2 == null) continue;
                    pointer2.release();
                }
            }
        });
        for (int i2 = 0; i2 < n2; ++i2) {
            pointerArray[i2] = Pointer.pointerToCString(stringArray[i2]);
            pointer.set(i2, pointerArray[i2]);
        }
        return pointer;
    }

    public static Pointer<Character> pointerToWideCString(String string) {
        return Pointer.setString(null, 0L, string, StringType.WideC, null);
    }

    public static Pointer<Pointer<Character>> pointerToWideCStrings(String ... stringArray) {
        if (stringArray == null) {
            return null;
        }
        final int n2 = stringArray.length;
        final Pointer[] pointerArray = new Pointer[n2];
        Pointer<Pointer<Character>> pointer = Pointer.allocateArray(PointerIO.getPointerInstance(Character.class), n2, new Releaser(){

            @Override
            public final void release(Pointer<?> pointer) {
                for (int i2 = 0; i2 < n2; ++i2) {
                    Pointer pointer2 = pointerArray[i2];
                    if (pointer2 == null) continue;
                    pointer2.release();
                }
            }
        });
        for (int i2 = 0; i2 < n2; ++i2) {
            pointerArray[i2] = Pointer.pointerToWideCString(stringArray[i2]);
            pointer.set(i2, pointerArray[i2]);
        }
        return pointer;
    }

    public String getCString() {
        return this.getCStringAtOffset(0L);
    }

    public String getCStringAtOffset(long l2) {
        return this.getStringAtOffset(l2, StringType.C, null);
    }

    public Pointer<T> setCString(String string) {
        return this.setCStringAtOffset(0L, string);
    }

    public Pointer<T> setCStringAtOffset(long l2, String string) {
        return this.setStringAtOffset(l2, string, StringType.C, null);
    }

    public String getWideCString() {
        return this.getWideCStringAtOffset(0L);
    }

    public String getWideCStringAtOffset(long l2) {
        return this.getStringAtOffset(l2, StringType.WideC, null);
    }

    public Pointer<T> setWideCString(String string) {
        return this.setWideCStringAtOffset(0L, string);
    }

    public Pointer<T> setWideCStringAtOffset(long l2, String string) {
        return this.setStringAtOffset(l2, string, StringType.WideC, null);
    }

    protected long strlen(long l2) {
        long l3 = this.getPeer() + l2;
        if (this.validStart != -1L && (l3 < this.validStart || l3 + 1L > this.validEnd)) {
            this.invalidPeer(l3, 1L);
        }
        return JNI.strlen(l3);
    }

    protected long wcslen(long l2) {
        long l3 = this.getPeer() + l2;
        if (this.validStart != -1L && (l3 < this.validStart || l3 + (long)Platform.WCHAR_T_SIZE > this.validEnd)) {
            this.invalidPeer(l3, Platform.WCHAR_T_SIZE);
        }
        return JNI.wcslen(l3);
    }

    public void clearValidBytes() {
        long l2 = this.getValidBytes();
        if (l2 < 0L) {
            throw new UnsupportedOperationException("Number of valid bytes is unknown. Please use clearBytes(long) or validBytes(long).");
        }
        this.clearBytes(l2);
    }

    public void clearBytes(long l2) {
        this.clearBytesAtOffset(0L, l2, (byte)0);
    }

    public void clearBytesAtOffset(long l2, long l3, byte by) {
        long l4 = this.getPeer() + l2;
        if (this.validStart != -1L && (l4 < this.validStart || l4 + l3 > this.validEnd)) {
            this.invalidPeer(l4, l3);
        }
        JNI.memset(l4, by, l3);
    }

    public Pointer<T> findByte(long l2, byte by, long l3) {
        long l4;
        long l5 = this.getPeer() + l2;
        if (this.validStart != -1L && (l5 < this.validStart || l5 + l3 > this.validEnd)) {
            this.invalidPeer(l5, l3);
        }
        if ((l4 = JNI.memchr(l5, by, l3)) == 0L) {
            return null;
        }
        return this.offset(l4 - l5);
    }

    public final T apply(long l2) {
        return this.get(l2);
    }

    public final void update(long l2, T t2) {
        this.set(l2, t2);
    }

    public T[] toArray() {
        this.getIO("Cannot create array");
        Pointer pointer = this;
        return pointer.toArray((int)pointer.getValidElements("Length of pointed memory is unknown, cannot create array out of this pointer"));
    }

    T[] toArray(int n2) {
        Class clazz = Utils.getClass(this.getIO("Cannot create array").getTargetType());
        if (clazz == null) {
            throw new RuntimeException("Unable to get the target type's class (target type = " + this.io.getTargetType() + ")");
        }
        return this.toArray((Object[])Array.newInstance(clazz, n2));
    }

    public <U> U[] toArray(U[] UArray) {
        int n2 = (int)this.getValidElements();
        if (n2 < 0) {
            Pointer.throwBecauseUntyped("Cannot create array");
        }
        if (UArray.length != n2) {
            return this.toArray();
        }
        for (int i2 = 0; i2 < n2; ++i2) {
            UArray[i2] = this.get(i2);
        }
        return UArray;
    }

    public NativeList<T> asList() {
        return this.asList(ListType.FixedCapacity);
    }

    public NativeList<T> asList(ListType listType) {
        return new DefaultNativeList(this, listType);
    }

    public static <E> NativeList<E> allocateList(PointerIO<E> object, long l2) {
        object = new DefaultNativeList<E>(Pointer.allocateArray(object, l2), ListType.Dynamic);
        object.clear();
        return object;
    }

    public static <E> NativeList<E> allocateList(Class<E> clazz, long l2) {
        return Pointer.allocateList(clazz, l2);
    }

    public static <E> NativeList<E> allocateList(Type type, long l2) {
        return Pointer.allocateList(PointerIO.getInstance(type), l2);
    }

    private static char[] intsToWChars(int[] nArray) {
        int n2 = nArray.length;
        char[] cArray = new char[n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            cArray[i2] = (char)nArray[i2];
        }
        return cArray;
    }

    private static int[] wcharsToInts(char[] cArray, int n2, int n3) {
        int[] nArray = new int[n3];
        for (int i2 = 0; i2 < n3; ++i2) {
            nArray[i2] = cArray[n2 + i2];
        }
        return nArray;
    }

    public Pointer<T> setIntegralAtOffset(long l2, AbstractIntegral abstractIntegral) {
        switch (abstractIntegral.byteSize()) {
            case 8: {
                this.setLongAtOffset(l2, abstractIntegral.longValue());
                break;
            }
            case 4: {
                this.setIntAtOffset(l2, SizeT.safeIntCast(abstractIntegral.longValue()));
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unsupported integral size");
            }
        }
        return this;
    }

    public long getIntegralAtOffset(long l2, int n2) {
        switch (n2) {
            case 8: {
                return this.getLongAtOffset(l2);
            }
            case 4: {
                return this.getIntAtOffset(l2);
            }
        }
        throw new UnsupportedOperationException("Unsupported integral size");
    }

    static {
        Platform.initLibrary();
        POINTER_MASK = Platform.is64Bits() ? -1L : 0xFFFFFFFFL;
        defaultAlignment = Integer.parseInt(Platform.getenvOrProperty("BRIDJ_DEFAULT_ALIGNMENT", "bridj.defaultAlignment", "-1"));
        localCachedPointers = new ThreadLocal<PointerLRUCache>(){

            @Override
            protected final PointerLRUCache initialValue() {
                return new PointerLRUCache(8, 1){

                    protected <P> Pointer<P> pointerToAddress(long l2, PointerIO<P> pointerIO) {
                        return Pointer.pointerToAddress_(l2, pointerIO);
                    }
                };
            }
        };
        freeReleaser = new FreeReleaser();
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static enum ListType {
        Unmodifiable,
        FixedCapacity,
        Dynamic;

    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static enum StringType {
        C(false, true),
        WideC(true, true),
        PascalShort(false, true),
        PascalWide(true, true),
        PascalAnsi(false, true),
        BSTR(true, true),
        STL(false, false),
        WideSTL(true, false);

        final boolean isWide;
        final boolean canCreate;

        private StringType(boolean bl, boolean bl2) {
            this.isWide = bl;
            this.canCreate = bl2;
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    static class FreeReleaser
    implements Releaser {
        FreeReleaser() {
        }

        @Override
        public void release(Pointer<?> pointer) {
            assert (pointer.getSibling() == null);
            assert (pointer.validStart == pointer.getPeer());
            if (BridJ.debugPointers) {
                pointer.deletionTrace = new RuntimeException().fillInStackTrace();
                BridJ.info("Freeing pointer " + pointer + " (peer = " + pointer.getPeer() + ", validStart = " + pointer.validStart + ", validEnd = " + pointer.validEnd + ", validBytes = " + pointer.getValidBytes() + ").\nCreation trace:\n\t" + Utils.toString(pointer.creationTrace).replaceAll("\n", "\n\t") + "\nDeletion trace:\n\t" + Utils.toString(pointer.deletionTrace).replaceAll("\n", "\n\t"));
            }
            if (!BridJ.debugNeverFree) {
                JNI.free(pointer.getPeer());
            }
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    static class DisorderedPointer<T>
    extends Pointer<T> {
        DisorderedPointer(PointerIO<T> pointerIO, long l2, long l3, long l4, Pointer<?> pointer, long l5, Object object) {
            super(pointerIO, l2, l3, l4, pointer, l5, object);
        }

        @Override
        public boolean isOrdered() {
            return false;
        }

        @Override
        public Pointer<T> setInt(int n2) {
            long l2 = this.getPeer();
            if (this.validStart != -1L && (l2 < this.validStart || l2 + 4L > this.validEnd)) {
                this.invalidPeer(l2, 4L);
            }
            JNI.set_int_disordered(l2, n2);
            return this;
        }

        @Override
        public Pointer<T> setIntAtOffset(long l2, int n2) {
            long l3 = this.getPeer() + l2;
            if (this.validStart != -1L && (l3 < this.validStart || l3 + 4L > this.validEnd)) {
                this.invalidPeer(l3, 4L);
            }
            JNI.set_int_disordered(l3, n2);
            return this;
        }

        @Override
        public int getInt() {
            long l2 = this.getPeer();
            if (this.validStart != -1L && (l2 < this.validStart || l2 + 4L > this.validEnd)) {
                this.invalidPeer(l2, 4L);
            }
            return JNI.get_int_disordered(l2);
        }

        @Override
        public int getIntAtOffset(long l2) {
            long l3 = this.getPeer() + l2;
            if (this.validStart != -1L && (l3 < this.validStart || l3 + 4L > this.validEnd)) {
                this.invalidPeer(l3, 4L);
            }
            return JNI.get_int_disordered(l3);
        }

        @Override
        public Pointer<T> setLong(long l2) {
            long l3 = this.getPeer();
            if (this.validStart != -1L && (l3 < this.validStart || l3 + 8L > this.validEnd)) {
                this.invalidPeer(l3, 8L);
            }
            JNI.set_long_disordered(l3, l2);
            return this;
        }

        @Override
        public Pointer<T> setLongAtOffset(long l2, long l3) {
            long l4 = this.getPeer() + l2;
            if (this.validStart != -1L && (l4 < this.validStart || l4 + 8L > this.validEnd)) {
                this.invalidPeer(l4, 8L);
            }
            JNI.set_long_disordered(l4, l3);
            return this;
        }

        @Override
        public long getLong() {
            long l2 = this.getPeer();
            if (this.validStart != -1L && (l2 < this.validStart || l2 + 8L > this.validEnd)) {
                this.invalidPeer(l2, 8L);
            }
            return JNI.get_long_disordered(l2);
        }

        @Override
        public long getLongAtOffset(long l2) {
            long l3 = this.getPeer() + l2;
            if (this.validStart != -1L && (l3 < this.validStart || l3 + 8L > this.validEnd)) {
                this.invalidPeer(l3, 8L);
            }
            return JNI.get_long_disordered(l3);
        }

        @Override
        public Pointer<T> setShort(short s2) {
            long l2 = this.getPeer();
            if (this.validStart != -1L && (l2 < this.validStart || l2 + 2L > this.validEnd)) {
                this.invalidPeer(l2, 2L);
            }
            JNI.set_short_disordered(l2, s2);
            return this;
        }

        @Override
        public Pointer<T> setShortAtOffset(long l2, short s2) {
            long l3 = this.getPeer() + l2;
            if (this.validStart != -1L && (l3 < this.validStart || l3 + 2L > this.validEnd)) {
                this.invalidPeer(l3, 2L);
            }
            JNI.set_short_disordered(l3, s2);
            return this;
        }

        @Override
        public short getShort() {
            long l2 = this.getPeer();
            if (this.validStart != -1L && (l2 < this.validStart || l2 + 2L > this.validEnd)) {
                this.invalidPeer(l2, 2L);
            }
            return JNI.get_short_disordered(l2);
        }

        @Override
        public short getShortAtOffset(long l2) {
            long l3 = this.getPeer() + l2;
            if (this.validStart != -1L && (l3 < this.validStart || l3 + 2L > this.validEnd)) {
                this.invalidPeer(l3, 2L);
            }
            return JNI.get_short_disordered(l3);
        }

        @Override
        public Pointer<T> setByte(byte by) {
            long l2 = this.getPeer();
            if (this.validStart != -1L && (l2 < this.validStart || l2 + 1L > this.validEnd)) {
                this.invalidPeer(l2, 1L);
            }
            JNI.set_byte(l2, by);
            return this;
        }

        @Override
        public Pointer<T> setByteAtOffset(long l2, byte by) {
            long l3 = this.getPeer() + l2;
            if (this.validStart != -1L && (l3 < this.validStart || l3 + 1L > this.validEnd)) {
                this.invalidPeer(l3, 1L);
            }
            JNI.set_byte(l3, by);
            return this;
        }

        @Override
        public byte getByte() {
            long l2 = this.getPeer();
            if (this.validStart != -1L && (l2 < this.validStart || l2 + 1L > this.validEnd)) {
                this.invalidPeer(l2, 1L);
            }
            return JNI.get_byte(l2);
        }

        @Override
        public byte getByteAtOffset(long l2) {
            long l3 = this.getPeer() + l2;
            if (this.validStart != -1L && (l3 < this.validStart || l3 + 1L > this.validEnd)) {
                this.invalidPeer(l3, 1L);
            }
            return JNI.get_byte(l3);
        }

        @Override
        public Pointer<T> setChar(char c2) {
            if (Platform.WCHAR_T_SIZE == 4) {
                return ((Pointer)this).setInt(c2);
            }
            long l2 = this.getPeer();
            if (this.validStart != -1L && (l2 < this.validStart || l2 + (long)Platform.WCHAR_T_SIZE > this.validEnd)) {
                this.invalidPeer(l2, Platform.WCHAR_T_SIZE);
            }
            JNI.set_char_disordered(l2, c2);
            return this;
        }

        @Override
        public Pointer<T> setCharAtOffset(long l2, char c2) {
            if (Platform.WCHAR_T_SIZE == 4) {
                return ((Pointer)this).setIntAtOffset(l2, c2);
            }
            long l3 = this.getPeer() + l2;
            if (this.validStart != -1L && (l3 < this.validStart || l3 + (long)Platform.WCHAR_T_SIZE > this.validEnd)) {
                this.invalidPeer(l3, Platform.WCHAR_T_SIZE);
            }
            JNI.set_char_disordered(l3, c2);
            return this;
        }

        @Override
        public char getChar() {
            if (Platform.WCHAR_T_SIZE == 4) {
                return (char)((Pointer)this).getInt();
            }
            long l2 = this.getPeer();
            if (this.validStart != -1L && (l2 < this.validStart || l2 + (long)Platform.WCHAR_T_SIZE > this.validEnd)) {
                this.invalidPeer(l2, Platform.WCHAR_T_SIZE);
            }
            return JNI.get_char_disordered(l2);
        }

        @Override
        public char getCharAtOffset(long l2) {
            if (Platform.WCHAR_T_SIZE == 4) {
                return (char)((Pointer)this).getIntAtOffset(l2);
            }
            long l3 = this.getPeer() + l2;
            if (this.validStart != -1L && (l3 < this.validStart || l3 + (long)Platform.WCHAR_T_SIZE > this.validEnd)) {
                this.invalidPeer(l3, Platform.WCHAR_T_SIZE);
            }
            return JNI.get_char_disordered(l3);
        }

        @Override
        public Pointer<T> setFloat(float f2) {
            long l2 = this.getPeer();
            if (this.validStart != -1L && (l2 < this.validStart || l2 + 4L > this.validEnd)) {
                this.invalidPeer(l2, 4L);
            }
            JNI.set_float_disordered(l2, f2);
            return this;
        }

        @Override
        public Pointer<T> setFloatAtOffset(long l2, float f2) {
            long l3 = this.getPeer() + l2;
            if (this.validStart != -1L && (l3 < this.validStart || l3 + 4L > this.validEnd)) {
                this.invalidPeer(l3, 4L);
            }
            JNI.set_float_disordered(l3, f2);
            return this;
        }

        @Override
        public float getFloat() {
            long l2 = this.getPeer();
            if (this.validStart != -1L && (l2 < this.validStart || l2 + 4L > this.validEnd)) {
                this.invalidPeer(l2, 4L);
            }
            return JNI.get_float_disordered(l2);
        }

        @Override
        public float getFloatAtOffset(long l2) {
            long l3 = this.getPeer() + l2;
            if (this.validStart != -1L && (l3 < this.validStart || l3 + 4L > this.validEnd)) {
                this.invalidPeer(l3, 4L);
            }
            return JNI.get_float_disordered(l3);
        }

        @Override
        public Pointer<T> setDouble(double d2) {
            long l2 = this.getPeer();
            if (this.validStart != -1L && (l2 < this.validStart || l2 + 8L > this.validEnd)) {
                this.invalidPeer(l2, 8L);
            }
            JNI.set_double_disordered(l2, d2);
            return this;
        }

        @Override
        public Pointer<T> setDoubleAtOffset(long l2, double d2) {
            long l3 = this.getPeer() + l2;
            if (this.validStart != -1L && (l3 < this.validStart || l3 + 8L > this.validEnd)) {
                this.invalidPeer(l3, 8L);
            }
            JNI.set_double_disordered(l3, d2);
            return this;
        }

        @Override
        public double getDouble() {
            long l2 = this.getPeer();
            if (this.validStart != -1L && (l2 < this.validStart || l2 + 8L > this.validEnd)) {
                this.invalidPeer(l2, 8L);
            }
            return JNI.get_double_disordered(l2);
        }

        @Override
        public double getDoubleAtOffset(long l2) {
            long l3 = this.getPeer() + l2;
            if (this.validStart != -1L && (l3 < this.validStart || l3 + 8L > this.validEnd)) {
                this.invalidPeer(l3, 8L);
            }
            return JNI.get_double_disordered(l3);
        }

        @Override
        public Pointer<T> setBoolean(boolean bl) {
            long l2 = this.getPeer();
            if (this.validStart != -1L && (l2 < this.validStart || l2 + 1L > this.validEnd)) {
                this.invalidPeer(l2, 1L);
            }
            JNI.set_boolean(l2, bl);
            return this;
        }

        @Override
        public Pointer<T> setBooleanAtOffset(long l2, boolean bl) {
            long l3 = this.getPeer() + l2;
            if (this.validStart != -1L && (l3 < this.validStart || l3 + 1L > this.validEnd)) {
                this.invalidPeer(l3, 1L);
            }
            JNI.set_boolean(l3, bl);
            return this;
        }

        @Override
        public boolean getBoolean() {
            long l2 = this.getPeer();
            if (this.validStart != -1L && (l2 < this.validStart || l2 + 1L > this.validEnd)) {
                this.invalidPeer(l2, 1L);
            }
            return JNI.get_boolean(l2);
        }

        @Override
        public boolean getBooleanAtOffset(long l2) {
            long l3 = this.getPeer() + l2;
            if (this.validStart != -1L && (l3 < this.validStart || l3 + 1L > this.validEnd)) {
                this.invalidPeer(l3, 1L);
            }
            return JNI.get_boolean(l3);
        }

        @Override
        public Pointer<T> setSizeTsAtOffset(long l2, long[] lArray, int n2, int n3) {
            if (lArray == null) {
                throw new IllegalArgumentException("Null values");
            }
            if (SizeT.SIZE == 8) {
                this.setLongsAtOffset(l2, lArray, n2, n3);
            } else {
                long l3 = this.getPeer() + l2;
                if (this.validStart != -1L && (l3 < this.validStart || l3 + (long)(n3 << 2) > this.validEnd)) {
                    this.invalidPeer(l3, n3 << 2);
                }
                long l4 = l3;
                int n4 = n2;
                for (int i2 = 0; i2 < n3; ++i2) {
                    n2 = (int)lArray[n4];
                    JNI.set_int_disordered(l4, n2);
                    l4 += 4L;
                    ++n4;
                }
            }
            return this;
        }

        @Override
        public Pointer<T> setSizeTsAtOffset(long l2, int[] nArray) {
            if (SizeT.SIZE == 4) {
                this.setIntsAtOffset(l2, nArray);
            } else {
                int n2 = nArray.length;
                long l3 = this.getPeer() + l2;
                if (this.validStart != -1L && (l3 < this.validStart || l3 + (long)(n2 << 3) > this.validEnd)) {
                    this.invalidPeer(l3, n2 << 3);
                }
                long l4 = l3;
                for (int i2 = 0; i2 < n2; ++i2) {
                    int n3 = nArray[i2];
                    JNI.set_long_disordered(l4, n3);
                    l4 += 8L;
                }
            }
            return this;
        }

        @Override
        public Pointer<T> setCLongsAtOffset(long l2, long[] lArray, int n2, int n3) {
            if (lArray == null) {
                throw new IllegalArgumentException("Null values");
            }
            if (CLong.SIZE == 8) {
                this.setLongsAtOffset(l2, lArray, n2, n3);
            } else {
                long l3 = this.getPeer() + l2;
                if (this.validStart != -1L && (l3 < this.validStart || l3 + (long)(n3 << 2) > this.validEnd)) {
                    this.invalidPeer(l3, n3 << 2);
                }
                long l4 = l3;
                int n4 = n2;
                for (int i2 = 0; i2 < n3; ++i2) {
                    n2 = (int)lArray[n4];
                    JNI.set_int_disordered(l4, n2);
                    l4 += 4L;
                    ++n4;
                }
            }
            return this;
        }

        @Override
        public Pointer<T> setCLongsAtOffset(long l2, int[] nArray) {
            if (CLong.SIZE == 4) {
                this.setIntsAtOffset(l2, nArray);
            } else {
                int n2 = nArray.length;
                long l3 = this.getPeer() + l2;
                if (this.validStart != -1L && (l3 < this.validStart || l3 + (long)(n2 << 3) > this.validEnd)) {
                    this.invalidPeer(l3, n2 << 3);
                }
                long l4 = l3;
                for (int i2 = 0; i2 < n2; ++i2) {
                    int n3 = nArray[i2];
                    JNI.set_long_disordered(l4, n3);
                    l4 += 8L;
                }
            }
            return this;
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    static class OrderedPointer<T>
    extends Pointer<T> {
        OrderedPointer(PointerIO<T> pointerIO, long l2, long l3, long l4, Pointer<?> pointer, long l5, Object object) {
            super(pointerIO, l2, l3, l4, pointer, l5, object);
        }

        @Override
        public boolean isOrdered() {
            return true;
        }

        @Override
        public Pointer<T> setInt(int n2) {
            long l2 = this.getPeer();
            if (this.validStart != -1L && (l2 < this.validStart || l2 + 4L > this.validEnd)) {
                this.invalidPeer(l2, 4L);
            }
            JNI.set_int(l2, n2);
            return this;
        }

        @Override
        public Pointer<T> setIntAtOffset(long l2, int n2) {
            long l3 = this.getPeer() + l2;
            if (this.validStart != -1L && (l3 < this.validStart || l3 + 4L > this.validEnd)) {
                this.invalidPeer(l3, 4L);
            }
            JNI.set_int(l3, n2);
            return this;
        }

        @Override
        public int getInt() {
            long l2 = this.getPeer();
            if (this.validStart != -1L && (l2 < this.validStart || l2 + 4L > this.validEnd)) {
                this.invalidPeer(l2, 4L);
            }
            return JNI.get_int(l2);
        }

        @Override
        public int getIntAtOffset(long l2) {
            long l3 = this.getPeer() + l2;
            if (this.validStart != -1L && (l3 < this.validStart || l3 + 4L > this.validEnd)) {
                this.invalidPeer(l3, 4L);
            }
            return JNI.get_int(l3);
        }

        @Override
        public Pointer<T> setLong(long l2) {
            long l3 = this.getPeer();
            if (this.validStart != -1L && (l3 < this.validStart || l3 + 8L > this.validEnd)) {
                this.invalidPeer(l3, 8L);
            }
            JNI.set_long(l3, l2);
            return this;
        }

        @Override
        public Pointer<T> setLongAtOffset(long l2, long l3) {
            long l4 = this.getPeer() + l2;
            if (this.validStart != -1L && (l4 < this.validStart || l4 + 8L > this.validEnd)) {
                this.invalidPeer(l4, 8L);
            }
            JNI.set_long(l4, l3);
            return this;
        }

        @Override
        public long getLong() {
            long l2 = this.getPeer();
            if (this.validStart != -1L && (l2 < this.validStart || l2 + 8L > this.validEnd)) {
                this.invalidPeer(l2, 8L);
            }
            return JNI.get_long(l2);
        }

        @Override
        public long getLongAtOffset(long l2) {
            long l3 = this.getPeer() + l2;
            if (this.validStart != -1L && (l3 < this.validStart || l3 + 8L > this.validEnd)) {
                this.invalidPeer(l3, 8L);
            }
            return JNI.get_long(l3);
        }

        @Override
        public Pointer<T> setShort(short s2) {
            long l2 = this.getPeer();
            if (this.validStart != -1L && (l2 < this.validStart || l2 + 2L > this.validEnd)) {
                this.invalidPeer(l2, 2L);
            }
            JNI.set_short(l2, s2);
            return this;
        }

        @Override
        public Pointer<T> setShortAtOffset(long l2, short s2) {
            long l3 = this.getPeer() + l2;
            if (this.validStart != -1L && (l3 < this.validStart || l3 + 2L > this.validEnd)) {
                this.invalidPeer(l3, 2L);
            }
            JNI.set_short(l3, s2);
            return this;
        }

        @Override
        public short getShort() {
            long l2 = this.getPeer();
            if (this.validStart != -1L && (l2 < this.validStart || l2 + 2L > this.validEnd)) {
                this.invalidPeer(l2, 2L);
            }
            return JNI.get_short(l2);
        }

        @Override
        public short getShortAtOffset(long l2) {
            long l3 = this.getPeer() + l2;
            if (this.validStart != -1L && (l3 < this.validStart || l3 + 2L > this.validEnd)) {
                this.invalidPeer(l3, 2L);
            }
            return JNI.get_short(l3);
        }

        @Override
        public Pointer<T> setByte(byte by) {
            long l2 = this.getPeer();
            if (this.validStart != -1L && (l2 < this.validStart || l2 + 1L > this.validEnd)) {
                this.invalidPeer(l2, 1L);
            }
            JNI.set_byte(l2, by);
            return this;
        }

        @Override
        public Pointer<T> setByteAtOffset(long l2, byte by) {
            long l3 = this.getPeer() + l2;
            if (this.validStart != -1L && (l3 < this.validStart || l3 + 1L > this.validEnd)) {
                this.invalidPeer(l3, 1L);
            }
            JNI.set_byte(l3, by);
            return this;
        }

        @Override
        public byte getByte() {
            long l2 = this.getPeer();
            if (this.validStart != -1L && (l2 < this.validStart || l2 + 1L > this.validEnd)) {
                this.invalidPeer(l2, 1L);
            }
            return JNI.get_byte(l2);
        }

        @Override
        public byte getByteAtOffset(long l2) {
            long l3 = this.getPeer() + l2;
            if (this.validStart != -1L && (l3 < this.validStart || l3 + 1L > this.validEnd)) {
                this.invalidPeer(l3, 1L);
            }
            return JNI.get_byte(l3);
        }

        @Override
        public Pointer<T> setChar(char c2) {
            if (Platform.WCHAR_T_SIZE == 4) {
                return ((Pointer)this).setInt(c2);
            }
            long l2 = this.getPeer();
            if (this.validStart != -1L && (l2 < this.validStart || l2 + (long)Platform.WCHAR_T_SIZE > this.validEnd)) {
                this.invalidPeer(l2, Platform.WCHAR_T_SIZE);
            }
            JNI.set_char(l2, c2);
            return this;
        }

        @Override
        public Pointer<T> setCharAtOffset(long l2, char c2) {
            if (Platform.WCHAR_T_SIZE == 4) {
                return ((Pointer)this).setIntAtOffset(l2, c2);
            }
            long l3 = this.getPeer() + l2;
            if (this.validStart != -1L && (l3 < this.validStart || l3 + (long)Platform.WCHAR_T_SIZE > this.validEnd)) {
                this.invalidPeer(l3, Platform.WCHAR_T_SIZE);
            }
            JNI.set_char(l3, c2);
            return this;
        }

        @Override
        public char getChar() {
            if (Platform.WCHAR_T_SIZE == 4) {
                return (char)((Pointer)this).getInt();
            }
            long l2 = this.getPeer();
            if (this.validStart != -1L && (l2 < this.validStart || l2 + (long)Platform.WCHAR_T_SIZE > this.validEnd)) {
                this.invalidPeer(l2, Platform.WCHAR_T_SIZE);
            }
            return JNI.get_char(l2);
        }

        @Override
        public char getCharAtOffset(long l2) {
            if (Platform.WCHAR_T_SIZE == 4) {
                return (char)((Pointer)this).getIntAtOffset(l2);
            }
            long l3 = this.getPeer() + l2;
            if (this.validStart != -1L && (l3 < this.validStart || l3 + (long)Platform.WCHAR_T_SIZE > this.validEnd)) {
                this.invalidPeer(l3, Platform.WCHAR_T_SIZE);
            }
            return JNI.get_char(l3);
        }

        @Override
        public Pointer<T> setFloat(float f2) {
            long l2 = this.getPeer();
            if (this.validStart != -1L && (l2 < this.validStart || l2 + 4L > this.validEnd)) {
                this.invalidPeer(l2, 4L);
            }
            JNI.set_float(l2, f2);
            return this;
        }

        @Override
        public Pointer<T> setFloatAtOffset(long l2, float f2) {
            long l3 = this.getPeer() + l2;
            if (this.validStart != -1L && (l3 < this.validStart || l3 + 4L > this.validEnd)) {
                this.invalidPeer(l3, 4L);
            }
            JNI.set_float(l3, f2);
            return this;
        }

        @Override
        public float getFloat() {
            long l2 = this.getPeer();
            if (this.validStart != -1L && (l2 < this.validStart || l2 + 4L > this.validEnd)) {
                this.invalidPeer(l2, 4L);
            }
            return JNI.get_float(l2);
        }

        @Override
        public float getFloatAtOffset(long l2) {
            long l3 = this.getPeer() + l2;
            if (this.validStart != -1L && (l3 < this.validStart || l3 + 4L > this.validEnd)) {
                this.invalidPeer(l3, 4L);
            }
            return JNI.get_float(l3);
        }

        @Override
        public Pointer<T> setDouble(double d2) {
            long l2 = this.getPeer();
            if (this.validStart != -1L && (l2 < this.validStart || l2 + 8L > this.validEnd)) {
                this.invalidPeer(l2, 8L);
            }
            JNI.set_double(l2, d2);
            return this;
        }

        @Override
        public Pointer<T> setDoubleAtOffset(long l2, double d2) {
            long l3 = this.getPeer() + l2;
            if (this.validStart != -1L && (l3 < this.validStart || l3 + 8L > this.validEnd)) {
                this.invalidPeer(l3, 8L);
            }
            JNI.set_double(l3, d2);
            return this;
        }

        @Override
        public double getDouble() {
            long l2 = this.getPeer();
            if (this.validStart != -1L && (l2 < this.validStart || l2 + 8L > this.validEnd)) {
                this.invalidPeer(l2, 8L);
            }
            return JNI.get_double(l2);
        }

        @Override
        public double getDoubleAtOffset(long l2) {
            long l3 = this.getPeer() + l2;
            if (this.validStart != -1L && (l3 < this.validStart || l3 + 8L > this.validEnd)) {
                this.invalidPeer(l3, 8L);
            }
            return JNI.get_double(l3);
        }

        @Override
        public Pointer<T> setBoolean(boolean bl) {
            long l2 = this.getPeer();
            if (this.validStart != -1L && (l2 < this.validStart || l2 + 1L > this.validEnd)) {
                this.invalidPeer(l2, 1L);
            }
            JNI.set_boolean(l2, bl);
            return this;
        }

        @Override
        public Pointer<T> setBooleanAtOffset(long l2, boolean bl) {
            long l3 = this.getPeer() + l2;
            if (this.validStart != -1L && (l3 < this.validStart || l3 + 1L > this.validEnd)) {
                this.invalidPeer(l3, 1L);
            }
            JNI.set_boolean(l3, bl);
            return this;
        }

        @Override
        public boolean getBoolean() {
            long l2 = this.getPeer();
            if (this.validStart != -1L && (l2 < this.validStart || l2 + 1L > this.validEnd)) {
                this.invalidPeer(l2, 1L);
            }
            return JNI.get_boolean(l2);
        }

        @Override
        public boolean getBooleanAtOffset(long l2) {
            long l3 = this.getPeer() + l2;
            if (this.validStart != -1L && (l3 < this.validStart || l3 + 1L > this.validEnd)) {
                this.invalidPeer(l3, 1L);
            }
            return JNI.get_boolean(l3);
        }

        @Override
        public Pointer<T> setSizeTsAtOffset(long l2, long[] lArray, int n2, int n3) {
            if (lArray == null) {
                throw new IllegalArgumentException("Null values");
            }
            if (SizeT.SIZE == 8) {
                this.setLongsAtOffset(l2, lArray, n2, n3);
            } else {
                long l3 = this.getPeer() + l2;
                if (this.validStart != -1L && (l3 < this.validStart || l3 + (long)(n3 << 2) > this.validEnd)) {
                    this.invalidPeer(l3, n3 << 2);
                }
                long l4 = l3;
                int n4 = n2;
                for (int i2 = 0; i2 < n3; ++i2) {
                    n2 = (int)lArray[n4];
                    JNI.set_int(l4, n2);
                    l4 += 4L;
                    ++n4;
                }
            }
            return this;
        }

        @Override
        public Pointer<T> setSizeTsAtOffset(long l2, int[] nArray) {
            if (SizeT.SIZE == 4) {
                this.setIntsAtOffset(l2, nArray);
            } else {
                int n2 = nArray.length;
                long l3 = this.getPeer() + l2;
                if (this.validStart != -1L && (l3 < this.validStart || l3 + (long)(n2 << 3) > this.validEnd)) {
                    this.invalidPeer(l3, n2 << 3);
                }
                long l4 = l3;
                for (int i2 = 0; i2 < n2; ++i2) {
                    int n3 = nArray[i2];
                    JNI.set_long(l4, n3);
                    l4 += 8L;
                }
            }
            return this;
        }

        @Override
        public Pointer<T> setCLongsAtOffset(long l2, long[] lArray, int n2, int n3) {
            if (lArray == null) {
                throw new IllegalArgumentException("Null values");
            }
            if (CLong.SIZE == 8) {
                this.setLongsAtOffset(l2, lArray, n2, n3);
            } else {
                long l3 = this.getPeer() + l2;
                if (this.validStart != -1L && (l3 < this.validStart || l3 + (long)(n3 << 2) > this.validEnd)) {
                    this.invalidPeer(l3, n3 << 2);
                }
                long l4 = l3;
                int n4 = n2;
                for (int i2 = 0; i2 < n3; ++i2) {
                    n2 = (int)lArray[n4];
                    JNI.set_int(l4, n2);
                    l4 += 4L;
                    ++n4;
                }
            }
            return this;
        }

        @Override
        public Pointer<T> setCLongsAtOffset(long l2, int[] nArray) {
            if (CLong.SIZE == 4) {
                this.setIntsAtOffset(l2, nArray);
            } else {
                int n2 = nArray.length;
                long l3 = this.getPeer() + l2;
                if (this.validStart != -1L && (l3 < this.validStart || l3 + (long)(n2 << 3) > this.validEnd)) {
                    this.invalidPeer(l3, n2 << 3);
                }
                long l4 = l3;
                for (int i2 = 0; i2 < n2; ++i2) {
                    int n3 = nArray[i2];
                    JNI.set_long(l4, n3);
                    l4 += 8L;
                }
            }
            return this;
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static interface Releaser {
        public void release(Pointer<?> var1);
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package org.bridj;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.nio.Buffer;
import org.bridj.BridJ;
import org.bridj.CLong;
import org.bridj.CallbackInterface;
import org.bridj.FlagSet;
import org.bridj.IntValuedEnum;
import org.bridj.NativeObject;
import org.bridj.Platform;
import org.bridj.Pointer;
import org.bridj.PointerIO;
import org.bridj.SizeT;
import org.bridj.StructIO;
import org.bridj.StructObject;
import org.bridj.TimeT;
import org.bridj.TypedPointer;
import org.bridj.util.DefaultParameterizedType;
import org.bridj.util.Utils;

class CommonPointerIOs {
    public static final PointerIO<Integer> intIO = new PointerIO<Integer>(Integer.class, 4, null){

        @Override
        public final Integer get(Pointer<Integer> pointer, long l2) {
            return pointer.getIntAtOffset(l2 << 2);
        }

        @Override
        public final void set(Pointer<Integer> pointer, long l2, Integer n2) {
            pointer.setIntAtOffset(l2 << 2, n2);
        }

        @Override
        public final <B extends Buffer> B getBuffer(Pointer<Integer> pointer, long l2, int n2) {
            return (B)pointer.getIntBufferAtOffset(l2, n2);
        }

        @Override
        public final Object getArray(Pointer<Integer> pointer, long l2, int n2) {
            return pointer.getIntsAtOffset(l2, n2);
        }

        @Override
        public final void setArray(Pointer<Integer> pointer, long l2, Object object) {
            if (object instanceof int[]) {
                pointer.setIntsAtOffset(l2, (int[])object);
                return;
            }
            super.setArray(pointer, l2, object);
        }
    };
    public static final PointerIO<Long> longIO = new PointerIO<Long>(Long.class, 8, null){

        @Override
        public final Long get(Pointer<Long> pointer, long l2) {
            return pointer.getLongAtOffset(l2 << 3);
        }

        @Override
        public final void set(Pointer<Long> pointer, long l2, Long l3) {
            pointer.setLongAtOffset(l2 << 3, l3);
        }

        @Override
        public final <B extends Buffer> B getBuffer(Pointer<Long> pointer, long l2, int n2) {
            return (B)pointer.getLongBufferAtOffset(l2, n2);
        }

        @Override
        public final Object getArray(Pointer<Long> pointer, long l2, int n2) {
            return pointer.getLongsAtOffset(l2, n2);
        }

        @Override
        public final void setArray(Pointer<Long> pointer, long l2, Object object) {
            if (object instanceof long[]) {
                pointer.setLongsAtOffset(l2, (long[])object);
                return;
            }
            super.setArray(pointer, l2, object);
        }
    };
    public static final PointerIO<Short> shortIO = new PointerIO<Short>(Short.class, 2, null){

        @Override
        public final Short get(Pointer<Short> pointer, long l2) {
            return pointer.getShortAtOffset(l2 << 1);
        }

        @Override
        public final void set(Pointer<Short> pointer, long l2, Short s2) {
            pointer.setShortAtOffset(l2 << 1, s2);
        }

        @Override
        public final <B extends Buffer> B getBuffer(Pointer<Short> pointer, long l2, int n2) {
            return (B)pointer.getShortBufferAtOffset(l2, n2);
        }

        @Override
        public final Object getArray(Pointer<Short> pointer, long l2, int n2) {
            return pointer.getShortsAtOffset(l2, n2);
        }

        @Override
        public final void setArray(Pointer<Short> pointer, long l2, Object object) {
            if (object instanceof short[]) {
                pointer.setShortsAtOffset(l2, (short[])object);
                return;
            }
            super.setArray(pointer, l2, object);
        }
    };
    public static final PointerIO<Byte> byteIO = new PointerIO<Byte>(Byte.class, 1, null){

        @Override
        public final Byte get(Pointer<Byte> pointer, long l2) {
            return pointer.getByteAtOffset(l2);
        }

        @Override
        public final void set(Pointer<Byte> pointer, long l2, Byte by) {
            pointer.setByteAtOffset(l2, by);
        }

        @Override
        public final <B extends Buffer> B getBuffer(Pointer<Byte> pointer, long l2, int n2) {
            return (B)pointer.getByteBufferAtOffset(l2, n2);
        }

        @Override
        public final Object getArray(Pointer<Byte> pointer, long l2, int n2) {
            return pointer.getBytesAtOffset(l2, n2);
        }

        @Override
        public final void setArray(Pointer<Byte> pointer, long l2, Object object) {
            if (object instanceof byte[]) {
                pointer.setBytesAtOffset(l2, (byte[])object);
                return;
            }
            super.setArray(pointer, l2, object);
        }
    };
    public static final PointerIO<Character> charIO = new PointerIO<Character>(Character.class, Platform.WCHAR_T_SIZE, null){

        @Override
        public final Character get(Pointer<Character> pointer, long l2) {
            return Character.valueOf(pointer.getCharAtOffset(l2 * (long)Platform.WCHAR_T_SIZE));
        }

        @Override
        public final void set(Pointer<Character> pointer, long l2, Character c2) {
            pointer.setCharAtOffset(l2 * (long)Platform.WCHAR_T_SIZE, c2.charValue());
        }

        @Override
        public final <B extends Buffer> B getBuffer(Pointer<Character> pointer, long l2, int n2) {
            throw new UnsupportedOperationException("Creating direct char buffers in a cross-platform way is tricky, so it's currently disabled");
        }

        @Override
        public final Object getArray(Pointer<Character> pointer, long l2, int n2) {
            return pointer.getCharsAtOffset(l2, n2);
        }

        @Override
        public final void setArray(Pointer<Character> pointer, long l2, Object object) {
            if (object instanceof char[]) {
                pointer.setCharsAtOffset(l2, (char[])object);
                return;
            }
            super.setArray(pointer, l2, object);
        }
    };
    public static final PointerIO<Float> floatIO = new PointerIO<Float>(Float.class, 4, null){

        @Override
        public final Float get(Pointer<Float> pointer, long l2) {
            return Float.valueOf(pointer.getFloatAtOffset(l2 << 2));
        }

        @Override
        public final void set(Pointer<Float> pointer, long l2, Float f2) {
            pointer.setFloatAtOffset(l2 << 2, f2.floatValue());
        }

        @Override
        public final <B extends Buffer> B getBuffer(Pointer<Float> pointer, long l2, int n2) {
            return (B)pointer.getFloatBufferAtOffset(l2, n2);
        }

        @Override
        public final Object getArray(Pointer<Float> pointer, long l2, int n2) {
            return pointer.getFloatsAtOffset(l2, n2);
        }

        @Override
        public final void setArray(Pointer<Float> pointer, long l2, Object object) {
            if (object instanceof float[]) {
                pointer.setFloatsAtOffset(l2, (float[])object);
                return;
            }
            super.setArray(pointer, l2, object);
        }
    };
    public static final PointerIO<Double> doubleIO = new PointerIO<Double>(Double.class, 8, null){

        @Override
        public final Double get(Pointer<Double> pointer, long l2) {
            return pointer.getDoubleAtOffset(l2 << 3);
        }

        @Override
        public final void set(Pointer<Double> pointer, long l2, Double d2) {
            pointer.setDoubleAtOffset(l2 << 3, d2);
        }

        @Override
        public final <B extends Buffer> B getBuffer(Pointer<Double> pointer, long l2, int n2) {
            return (B)pointer.getDoubleBufferAtOffset(l2, n2);
        }

        @Override
        public final Object getArray(Pointer<Double> pointer, long l2, int n2) {
            return pointer.getDoublesAtOffset(l2, n2);
        }

        @Override
        public final void setArray(Pointer<Double> pointer, long l2, Object object) {
            if (object instanceof double[]) {
                pointer.setDoublesAtOffset(l2, (double[])object);
                return;
            }
            super.setArray(pointer, l2, object);
        }
    };
    public static final PointerIO<Boolean> booleanIO = new PointerIO<Boolean>(Boolean.class, 1, null){

        @Override
        public final Boolean get(Pointer<Boolean> pointer, long l2) {
            return pointer.getBooleanAtOffset(l2);
        }

        @Override
        public final void set(Pointer<Boolean> pointer, long l2, Boolean bl) {
            pointer.setBooleanAtOffset(l2, bl);
        }

        @Override
        public final <B extends Buffer> B getBuffer(Pointer<Boolean> pointer, long l2, int n2) {
            return (B)pointer.getByteBufferAtOffset(l2, n2);
        }

        @Override
        public final Object getArray(Pointer<Boolean> pointer, long l2, int n2) {
            return pointer.getBooleansAtOffset(l2, n2);
        }

        @Override
        public final void setArray(Pointer<Boolean> pointer, long l2, Object object) {
            if (object instanceof boolean[]) {
                pointer.setBooleansAtOffset(l2, (boolean[])object);
                return;
            }
            super.setArray(pointer, l2, object);
        }
    };
    public static final PointerIO<SizeT> SizeTIO = new PointerIO<SizeT>(SizeT.class, SizeT.SIZE, null){

        @Override
        public final SizeT get(Pointer<SizeT> pointer, long l2) {
            return new SizeT(pointer.getSizeTAtOffset(l2 * (long)SizeT.SIZE));
        }

        @Override
        public final void set(Pointer<SizeT> pointer, long l2, SizeT sizeT) {
            pointer.setSizeTAtOffset(l2 * (long)SizeT.SIZE, sizeT == null ? 0L : sizeT.longValue());
        }
    };
    public static final PointerIO<TimeT> TimeTIO = new PointerIO<TimeT>(TimeT.class, TimeT.SIZE, null){

        @Override
        public final TimeT get(Pointer<TimeT> pointer, long l2) {
            long l3 = l2 * (long)TimeT.SIZE;
            return new TimeT(TimeT.SIZE == 4 ? (long)pointer.getIntAtOffset(l3) : pointer.getLongAtOffset(l3));
        }

        @Override
        public final void set(Pointer<TimeT> pointer, long l2, TimeT timeT) {
            long l3 = l2 * (long)TimeT.SIZE;
            if (TimeT.SIZE == 4) {
                pointer.setIntAtOffset(l3, timeT == null ? 0 : timeT.intValue());
                return;
            }
            pointer.setLongAtOffset(l3, timeT == null ? 0L : timeT.longValue());
        }
    };
    public static final PointerIO<CLong> CLongIO = new PointerIO<CLong>(CLong.class, CLong.SIZE, null){

        @Override
        public final CLong get(Pointer<CLong> pointer, long l2) {
            return new CLong(pointer.getCLongAtOffset(l2 * (long)CLong.SIZE));
        }

        @Override
        public final void set(Pointer<CLong> pointer, long l2, CLong cLong) {
            pointer.setCLongAtOffset(l2 * (long)CLong.SIZE, cLong == null ? 0L : cLong.longValue());
        }
    };

    CommonPointerIOs() {
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    static class TypedPointerPointerIO<P extends TypedPointer>
    extends PointerIO<P> {
        final Constructor cons;
        final Class<P> pointerClass;

        public TypedPointerPointerIO(Class<P> clazz) {
            super(clazz, Pointer.SIZE, null);
            this.pointerClass = clazz;
            try {
                this.cons = clazz.getConstructor(Long.TYPE);
                return;
            }
            catch (Exception exception) {
                throw new RuntimeException("Cannot find constructor for " + clazz.getName(), exception);
            }
        }

        @Override
        public P castTarget(long l2) {
            if (l2 == 0L) {
                return null;
            }
            try {
                return (P)((TypedPointer)this.cons.newInstance(l2));
            }
            catch (Exception exception) {
                throw new RuntimeException("Cannot create pointer of type " + this.pointerClass.getName(), exception);
            }
        }

        @Override
        public P get(Pointer<P> pointer, long l2) {
            return (P)this.castTarget(pointer.getSizeTAtOffset(l2 * (long)Pointer.SIZE));
        }

        @Override
        public void set(Pointer<P> pointer, long l2, P p2) {
            pointer.setPointerAtOffset(l2 * (long)Pointer.SIZE, (Pointer<?>)p2);
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    static class IntValuedEnumPointerIO<E extends Enum<E>>
    extends PointerIO<IntValuedEnum<E>> {
        final Class<E> enumClass;

        public IntValuedEnumPointerIO(Class<E> clazz) {
            super((Type)((Object)IntValuedEnum.class), 4, null);
            this.enumClass = clazz;
        }

        @Override
        public IntValuedEnum<E> get(Pointer<IntValuedEnum<E>> pointer, long l2) {
            return FlagSet.fromValue(pointer.getIntAtOffset(4L * l2), this.enumClass);
        }

        @Override
        public void set(Pointer<IntValuedEnum<E>> pointer, long l2, IntValuedEnum<E> intValuedEnum) {
            pointer.setIntAtOffset(4L * l2, (int)intValuedEnum.value());
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    static class CallbackPointerIO<T extends CallbackInterface>
    extends PointerIO<T> {
        final Class<T> callbackClass;

        public CallbackPointerIO(Class<T> clazz) {
            super(clazz, Pointer.SIZE, null);
            this.callbackClass = clazz;
        }

        @Override
        public T get(Pointer<T> pointer, long l2) {
            if (l2 != 0L) {
                throw new UnsupportedOperationException("Cannot get function pointer at index different from 0");
            }
            return (T)((CallbackInterface)pointer.getNativeObjectAtOffset(0L, this.callbackClass));
        }

        @Override
        public void set(Pointer<T> pointer, long l2, T t2) {
            throw new UnsupportedOperationException("Cannot write to body of function");
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    static class PointerArrayIO<T>
    extends PointerIO<Pointer<T>> {
        final PointerIO<T> underlyingIO;
        final long[] dimensions;
        final long totalRemainingDims;
        final int iDimension;

        static Type arrayPtrType(Type type, long ... lArray) {
            for (int i2 = 0; i2 < lArray.length; ++i2) {
                type = DefaultParameterizedType.paramType(Pointer.class, new Type[]{type});
            }
            return type;
        }

        static long getTotalRemainingDims(long[] lArray, int n2) {
            long l2 = 1L;
            ++n2;
            while (n2 < lArray.length) {
                l2 *= lArray[n2];
                ++n2;
            }
            return l2;
        }

        public PointerArrayIO(PointerIO<T> pointerIO, long[] lArray, int n2) {
            super(pointerIO == null ? null : PointerArrayIO.arrayPtrType(pointerIO.getTargetType(), lArray), -1, null);
            this.underlyingIO = pointerIO;
            this.dimensions = lArray;
            this.iDimension = n2;
            this.totalRemainingDims = PointerArrayIO.getTotalRemainingDims(lArray, n2);
        }

        @Override
        public long getTargetSize() {
            long l2 = this.underlyingIO.getTargetSize();
            return this.dimensions[this.iDimension + 1] * l2;
        }

        @Override
        public Pointer<T> get(Pointer<Pointer<T>> pointer, long l2) {
            long l3 = ((PointerIO)this).getTargetSize();
            return pointer.offset(l2 * l3).as(this.underlyingIO);
        }

        long getOffset(long l2) {
            assert (this.iDimension < this.dimensions.length);
            return l2 * this.totalRemainingDims;
        }

        @Override
        public void set(Pointer<Pointer<T>> pointer, long l2, Pointer<T> pointer2) {
            throw new RuntimeException("Cannot set a multi-dimensional array's sub-arrays pointers !");
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    static class PointerPointerIO<T>
    extends PointerIO<Pointer<T>> {
        final PointerIO<T> underlyingIO;

        public PointerPointerIO(PointerIO<T> pointerIO) {
            super((Type)(pointerIO == null ? Pointer.class : DefaultParameterizedType.paramType(Pointer.class, new Type[]{pointerIO.getTargetType()})), Pointer.SIZE, null);
            this.underlyingIO = pointerIO;
        }

        @Override
        public Pointer<T> get(Pointer<Pointer<T>> pointer, long l2) {
            return pointer.getPointerAtOffset(l2 * (long)Pointer.SIZE, this.underlyingIO);
        }

        @Override
        public void set(Pointer<Pointer<T>> pointer, long l2, Pointer<T> pointer2) {
            pointer.setPointerAtOffset(l2 * (long)Pointer.SIZE, pointer2);
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    static class StructPointerIO<S extends StructObject>
    extends NativeObjectPointerIO<S> {
        final StructIO structIO;

        public StructPointerIO(StructIO structIO) {
            super(structIO.desc.getStructType());
            this.structIO = structIO;
        }

        @Override
        protected long computeTargetSize() {
            return this.structIO.desc.getStructSize();
        }

        @Override
        protected long computeTargetAlignment() {
            return this.structIO.desc.getStructAlignment();
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    static class NativeObjectPointerIO<N extends NativeObject>
    extends PointerIO<N> {
        protected volatile long targetSize = -1L;
        protected volatile long targetAlignment = -1L;
        protected Type nativeObjectType;

        public NativeObjectPointerIO(Type type) {
            super(type, -1, null);
            this.nativeObjectType = type;
            if (Utils.containsTypeVariables(type)) {
                throw new RuntimeException("Type " + type + " contains unresolved type variables!");
            }
        }

        protected long computeTargetSize() {
            return BridJ.sizeOf(this.nativeObjectType);
        }

        protected long computeTargetAlignment() {
            return ((PointerIO)this).getTargetSize();
        }

        @Override
        public long getTargetSize() {
            if (this.targetSize < 0L) {
                this.targetSize = this.computeTargetSize();
            }
            return this.targetSize;
        }

        @Override
        public long getTargetAlignment() {
            if (this.targetAlignment < 0L) {
                this.targetAlignment = this.computeTargetAlignment();
            }
            return this.targetAlignment;
        }

        @Override
        public N get(Pointer<N> pointer, long l2) {
            return (N)pointer.getNativeObjectAtOffset(l2 * ((PointerIO)this).getTargetSize(), this.nativeObjectType);
        }

        @Override
        public void set(Pointer<N> pointer, long l2, N object) {
            object = Pointer.getPointer(object);
            ((Pointer)object).copyTo(pointer.offset(l2 * ((PointerIO)this).getTargetSize()));
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package org.bridj;

import java.lang.constant.Constable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import java.util.concurrent.ConcurrentHashMap;
import org.bridj.CLong;
import org.bridj.Callback;
import org.bridj.CommonPointerIOs;
import org.bridj.IntValuedEnum;
import org.bridj.NativeObject;
import org.bridj.Pointer;
import org.bridj.SizeT;
import org.bridj.StructIO;
import org.bridj.StructObject;
import org.bridj.TimeT;
import org.bridj.TypedPointer;
import org.bridj.util.Utils;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public abstract class PointerIO<T> {
    final Type targetType;
    final Class<?> typedPointerClass;
    final int targetSize;
    final int targetAlignment = -1;
    private static final PointerIO<?> PointerIO = org.bridj.PointerIO.getPointerInstance(null);
    private static final ConcurrentHashMap<StructIO, PointerIO<?>> structIOs = new ConcurrentHashMap();
    private static final ConcurrentHashMap<Type, PointerIO<?>> ios = new ConcurrentHashMap();

    public PointerIO(Type type, int n2, Class<?> clazz) {
        this.targetType = type;
        this.targetSize = n2;
        this.typedPointerClass = clazz;
    }

    abstract T get(Pointer<T> var1, long var2);

    abstract void set(Pointer<T> var1, long var2, T var4);

    public Object getArray(Pointer<T> pointer, long l2, int n2) {
        return pointer.offset(l2).toArray(n2);
    }

    public <B extends Buffer> B getBuffer(Pointer<T> pointer, long l2, int n2) {
        throw new UnsupportedOperationException("Cannot create a Buffer instance of elements of type " + this.getTargetType());
    }

    public void setArray(Pointer<T> pointer, long l2, Object object) {
        Object[] objectArray = (Object[])object;
        int n2 = objectArray.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            this.set(pointer, i2, objectArray[i2]);
        }
    }

    public T castTarget(long l2) {
        throw new UnsupportedOperationException("Cannot cast pointer to " + this.targetType);
    }

    PointerIO<Pointer<T>> getReferenceIO() {
        return new CommonPointerIOs.PointerPointerIO(this);
    }

    public long getTargetSize() {
        return this.targetSize;
    }

    public long getTargetAlignment() {
        return this.getTargetSize();
    }

    public boolean isTypedPointer() {
        return this.typedPointerClass != null;
    }

    public Class<?> getTypedPointerClass() {
        return this.typedPointerClass;
    }

    public Type getTargetType() {
        return this.targetType;
    }

    static Class<?> getClass(Type type) {
        while (true) {
            if (type instanceof Class) {
                return (Class)type;
            }
            if (!(type instanceof ParameterizedType)) break;
            type = ((ParameterizedType)type).getRawType();
        }
        return null;
    }

    public static <T> PointerIO<Pointer<T>> getPointerInstance(Type type) {
        return org.bridj.PointerIO.getPointerInstance(org.bridj.PointerIO.getInstance(type));
    }

    public static <T> PointerIO<Pointer<T>> getPointerInstance(PointerIO<T> pointerIO) {
        return new CommonPointerIOs.PointerPointerIO<T>(pointerIO);
    }

    public static <T> PointerIO<Pointer<T>> getArrayInstance(PointerIO<T> pointerIO, long[] lArray, int n2) {
        return new CommonPointerIOs.PointerArrayIO<T>(pointerIO, lArray, n2);
    }

    static <T> PointerIO<T> getArrayIO(Object object) {
        if (object instanceof int[]) {
            return CommonPointerIOs.intIO;
        }
        if (object instanceof long[]) {
            return CommonPointerIOs.longIO;
        }
        if (object instanceof short[]) {
            return CommonPointerIOs.shortIO;
        }
        if (object instanceof byte[]) {
            return CommonPointerIOs.byteIO;
        }
        if (object instanceof char[]) {
            return CommonPointerIOs.charIO;
        }
        if (object instanceof float[]) {
            return CommonPointerIOs.floatIO;
        }
        if (object instanceof double[]) {
            return CommonPointerIOs.doubleIO;
        }
        if (object instanceof boolean[]) {
            return CommonPointerIOs.booleanIO;
        }
        return org.bridj.PointerIO.getInstance(object.getClass().getComponentType());
    }

    public static <S extends StructObject> PointerIO<S> getInstance(StructIO object) {
        PointerIO<?> pointerIO = structIOs.get(object);
        if (pointerIO == null && (object = structIOs.putIfAbsent((StructIO)object, pointerIO = new CommonPointerIOs.StructPointerIO((StructIO)object))) != null) {
            pointerIO = object;
        }
        return pointerIO;
    }

    public static <P> PointerIO<P> getInstance(Type type) {
        if (type == null) {
            return null;
        }
        PointerIO pointerIO = ios.get(type);
        if (pointerIO == null) {
            Class clazz = Utils.getClass(type);
            if (clazz != null) {
                if (clazz == Pointer.class) {
                    pointerIO = org.bridj.PointerIO.getPointerInstance(((ParameterizedType)type).getActualTypeArguments()[0]);
                } else if (StructObject.class.isAssignableFrom(clazz)) {
                    pointerIO = org.bridj.PointerIO.getInstance(StructIO.getInstance(clazz, type));
                } else if (Callback.class.isAssignableFrom(clazz)) {
                    pointerIO = new CommonPointerIOs.CallbackPointerIO(clazz);
                } else if (NativeObject.class.isAssignableFrom(clazz)) {
                    pointerIO = new CommonPointerIOs.NativeObjectPointerIO(type);
                } else if (IntValuedEnum.class.isAssignableFrom(clazz)) {
                    if (type instanceof ParameterizedType && (clazz = ((ParameterizedType)type).getActualTypeArguments()[0]) instanceof Class) {
                        pointerIO = new CommonPointerIOs.IntValuedEnumPointerIO(clazz);
                    }
                } else if (TypedPointer.class.isAssignableFrom(clazz)) {
                    pointerIO = new CommonPointerIOs.TypedPointerPointerIO(clazz);
                }
            }
            if (pointerIO != null && (clazz = ios.putIfAbsent(type, pointerIO)) != null) {
                pointerIO = clazz;
            }
        }
        return pointerIO;
    }

    public static PointerIO<Integer> getIntInstance() {
        return CommonPointerIOs.intIO;
    }

    public static PointerIO<Long> getLongInstance() {
        return CommonPointerIOs.longIO;
    }

    public static PointerIO<Short> getShortInstance() {
        return CommonPointerIOs.shortIO;
    }

    public static PointerIO<Byte> getByteInstance() {
        return CommonPointerIOs.byteIO;
    }

    public static PointerIO<Character> getCharInstance() {
        return CommonPointerIOs.charIO;
    }

    public static PointerIO<Float> getFloatInstance() {
        return CommonPointerIOs.floatIO;
    }

    public static PointerIO<Double> getDoubleInstance() {
        return CommonPointerIOs.doubleIO;
    }

    public static PointerIO<Boolean> getBooleanInstance() {
        return CommonPointerIOs.booleanIO;
    }

    public static PointerIO<CLong> getCLongInstance() {
        return CommonPointerIOs.CLongIO;
    }

    public static PointerIO<SizeT> getSizeTInstance() {
        return CommonPointerIOs.SizeTIO;
    }

    public static PointerIO<?> getPointerInstance() {
        return PointerIO;
    }

    public static PointerIO<TimeT> getTimeTInstance() {
        return CommonPointerIOs.TimeTIO;
    }

    public static <P> PointerIO<P> getBufferPrimitiveInstance(Buffer buffer) {
        if (buffer instanceof IntBuffer) {
            return CommonPointerIOs.intIO;
        }
        if (buffer instanceof LongBuffer) {
            return CommonPointerIOs.longIO;
        }
        if (buffer instanceof ShortBuffer) {
            return CommonPointerIOs.shortIO;
        }
        if (buffer instanceof ByteBuffer) {
            return CommonPointerIOs.byteIO;
        }
        if (buffer instanceof CharBuffer) {
            return CommonPointerIOs.charIO;
        }
        if (buffer instanceof FloatBuffer) {
            return CommonPointerIOs.floatIO;
        }
        if (buffer instanceof DoubleBuffer) {
            return CommonPointerIOs.doubleIO;
        }
        throw new UnsupportedOperationException();
    }

    static {
        ios.put((Type)((Object)Pointer.class), PointerIO);
        ios.put((Type)((Object)SizeT.class), CommonPointerIOs.SizeTIO);
        ios.put((Type)((Object)TimeT.class), CommonPointerIOs.TimeTIO);
        ios.put((Type)((Object)CLong.class), CommonPointerIOs.CLongIO);
        PointerIO<Constable> pointerIO = CommonPointerIOs.intIO;
        ios.put(Integer.TYPE, pointerIO);
        ios.put((Type)((Object)Integer.class), pointerIO);
        pointerIO = CommonPointerIOs.longIO;
        ios.put(Long.TYPE, pointerIO);
        ios.put((Type)((Object)Long.class), pointerIO);
        pointerIO = CommonPointerIOs.shortIO;
        ios.put(Short.TYPE, pointerIO);
        ios.put((Type)((Object)Short.class), pointerIO);
        pointerIO = CommonPointerIOs.byteIO;
        ios.put(Byte.TYPE, pointerIO);
        ios.put((Type)((Object)Byte.class), pointerIO);
        pointerIO = CommonPointerIOs.charIO;
        ios.put(Character.TYPE, pointerIO);
        ios.put((Type)((Object)Character.class), pointerIO);
        pointerIO = CommonPointerIOs.floatIO;
        ios.put(Float.TYPE, pointerIO);
        ios.put((Type)((Object)Float.class), pointerIO);
        pointerIO = CommonPointerIOs.doubleIO;
        ios.put(Double.TYPE, pointerIO);
        ios.put((Type)((Object)Double.class), pointerIO);
        pointerIO = CommonPointerIOs.booleanIO;
        ios.put(Boolean.TYPE, pointerIO);
        ios.put((Type)((Object)Boolean.class), pointerIO);
    }
}


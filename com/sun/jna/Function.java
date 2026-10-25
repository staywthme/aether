/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna;

import com.sun.jna.Callback;
import com.sun.jna.CallbackReference;
import com.sun.jna.FromNativeContext;
import com.sun.jna.FunctionParameterContext;
import com.sun.jna.FunctionResultContext;
import com.sun.jna.Memory;
import com.sun.jna.MethodParameterContext;
import com.sun.jna.MethodResultContext;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.NativeMapped;
import com.sun.jna.NativeMappedConverter;
import com.sun.jna.NativeString;
import com.sun.jna.Pointer;
import com.sun.jna.StringArray;
import com.sun.jna.Structure;
import com.sun.jna.ToNativeContext;
import com.sun.jna.TypeMapper;
import com.sun.jna.VarArgsChecker;
import com.sun.jna.WString;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;

public class Function
extends Pointer {
    public static final int MAX_NARGS = 256;
    public static final int C_CONVENTION = 0;
    public static final int ALT_CONVENTION = 63;
    private static final int MASK_CC = 63;
    public static final int THROW_LAST_ERROR = 64;
    public static final int USE_VARARGS = 255;
    private static final int USE_VARARGS_SHIFT = 7;
    static final Integer INTEGER_TRUE = -1;
    static final Integer INTEGER_FALSE = 0;
    private NativeLibrary library;
    private final String functionName;
    final String encoding;
    final int callFlags;
    final Map<String, ?> options;
    static final String OPTION_INVOKING_METHOD = "invoking-method";
    private static final VarArgsChecker IS_VARARGS = VarArgsChecker.create();

    public static Function getFunction(String string, String string2) {
        return NativeLibrary.getInstance(string).getFunction(string2);
    }

    public static Function getFunction(String string, String string2, int n2) {
        return NativeLibrary.getInstance(string).getFunction(string2, n2, null);
    }

    public static Function getFunction(String string, String string2, int n2, String string3) {
        return NativeLibrary.getInstance(string).getFunction(string2, n2, string3);
    }

    public static Function getFunction(Pointer pointer) {
        return Function.getFunction(pointer, 0, null);
    }

    public static Function getFunction(Pointer pointer, int n2) {
        return Function.getFunction(pointer, n2, null);
    }

    public static Function getFunction(Pointer pointer, int n2, String string) {
        return new Function(pointer, n2, string);
    }

    Function(NativeLibrary nativeLibrary, String string, int n2, String string2) {
        this.checkCallingConvention(n2 & 0x3F);
        if (string == null) {
            throw new NullPointerException("Function name must not be null");
        }
        this.library = nativeLibrary;
        this.functionName = string;
        this.callFlags = n2;
        this.options = nativeLibrary.getOptions();
        this.encoding = string2 != null ? string2 : Native.getDefaultStringEncoding();
        try {
            this.peer = nativeLibrary.getSymbolAddress(string);
            return;
        }
        catch (UnsatisfiedLinkError unsatisfiedLinkError) {
            throw new UnsatisfiedLinkError("Error looking up function '" + string + "': " + unsatisfiedLinkError.getMessage());
        }
    }

    Function(Pointer pointer, int n2, String string) {
        this.checkCallingConvention(n2 & 0x3F);
        if (pointer == null || pointer.peer == 0L) {
            throw new NullPointerException("Function address may not be null");
        }
        this.functionName = pointer.toString();
        this.callFlags = n2;
        this.peer = pointer.peer;
        this.options = Collections.EMPTY_MAP;
        this.encoding = string != null ? string : Native.getDefaultStringEncoding();
    }

    private void checkCallingConvention(int n2) throws IllegalArgumentException {
        if ((n2 & 0x3F) != n2) {
            throw new IllegalArgumentException("Unrecognized calling convention: " + n2);
        }
    }

    public String getName() {
        return this.functionName;
    }

    public int getCallingConvention() {
        return this.callFlags & 0x3F;
    }

    public Object invoke(Class<?> clazz, Object[] objectArray) {
        return this.invoke(clazz, objectArray, this.options);
    }

    public Object invoke(Class<?> clazz, Object[] objectArray, Map<String, ?> map) {
        Method method = (Method)map.get(OPTION_INVOKING_METHOD);
        Class<?>[] classArray = method != null ? method.getParameterTypes() : null;
        return this.invoke(method, classArray, clazz, objectArray, map);
    }

    Object invoke(Method object, Class<?>[] object2, Class<?> object3, Object[] objectArray, Map<String, ?> map) {
        Object object4;
        Object[] objectArray2 = new Object[]{};
        if (objectArray != null) {
            if (objectArray.length > 256) {
                throw new UnsupportedOperationException("Maximum argument count is 256");
            }
            objectArray2 = new Object[objectArray.length];
            System.arraycopy(objectArray, 0, objectArray2, 0, objectArray2.length);
        }
        Object object5 = (TypeMapper)map.get("type-mapper");
        boolean bl = Boolean.TRUE.equals(map.get("allow-objects"));
        int n2 = (objectArray2.length > 0 && object != null ? Function.isVarArgs((Method)object) : 0) ? 1 : 0;
        int n3 = objectArray2.length > 0 && object != null ? Function.fixedArgs((Method)object) : 0;
        for (int i2 = 0; i2 < objectArray2.length; ++i2) {
            object4 = object != null ? (n2 != 0 && i2 >= ((Class<?>[])object2).length - 1 ? object2[((Class<?>[])object2).length - 1].getComponentType() : object2[i2]) : null;
            objectArray2[i2] = this.convertArgument(objectArray2, i2, (Method)object, (TypeMapper)object5, bl, (Class<?>)object4);
        }
        Class<?> clazz = object3;
        object4 = null;
        if (NativeMapped.class.isAssignableFrom((Class<?>)object3)) {
            object4 = object2 = NativeMappedConverter.getInstance(object3);
            clazz = ((NativeMappedConverter)object2).nativeType();
        } else if (object5 != null && (object4 = object5.getFromNativeConverter((Class<?>)object3)) != null) {
            clazz = object4.nativeType();
        }
        object2 = this.invoke(objectArray2, clazz, bl, n3);
        if (object4 != null) {
            object = object != null ? new MethodResultContext((Class<?>)object3, this, objectArray, (Method)object) : new FunctionResultContext((Class<?>)object3, this, objectArray);
            object2 = object4.fromNative(object2, (FromNativeContext)object);
        }
        if (objectArray != null) {
            for (int i3 = 0; i3 < objectArray.length; ++i3) {
                object3 = objectArray[i3];
                if (object3 == null) continue;
                if (object3 instanceof Structure) {
                    if (object3 instanceof Structure.ByValue) continue;
                    ((Structure)object3).autoRead();
                    continue;
                }
                if (objectArray2[i3] instanceof PostCallRead) {
                    ((PostCallRead)objectArray2[i3]).read();
                    if (!(objectArray2[i3] instanceof PointerArray)) continue;
                    PointerArray pointerArray = (PointerArray)objectArray2[i3];
                    if (!Structure.ByReference[].class.isAssignableFrom(object3.getClass())) continue;
                    object5 = object3.getClass().getComponentType();
                    object3 = object3;
                    for (n2 = 0; n2 < ((Structure[])object3).length; ++n2) {
                        Pointer pointer = ((Pointer)pointerArray).getPointer(Native.POINTER_SIZE * n2);
                        object3[n2] = Structure.updateStructureByReference(object5, object3[n2], pointer);
                    }
                    continue;
                }
                if (!Structure[].class.isAssignableFrom(object3.getClass())) continue;
                Structure.autoRead(object3);
            }
        }
        return object2;
    }

    Object invoke(Object[] objectArray, Class<?> clazz, boolean bl) {
        return this.invoke(objectArray, clazz, bl, 0);
    }

    Object invoke(Object[] objectArray, Class<?> wStringArray, boolean n2, int n3) {
        Object object = null;
        n3 = this.callFlags | (n3 & 0xFF) << 7;
        if (wStringArray == null || wStringArray == Void.TYPE || wStringArray == Void.class) {
            Function function = this;
            Native.invokeVoid(function, function.peer, n3, objectArray);
            object = null;
        } else if (wStringArray == Boolean.TYPE || wStringArray == Boolean.class) {
            Function function = this;
            object = Function.valueOf(Native.invokeInt(function, function.peer, n3, objectArray) != 0);
        } else if (wStringArray == Byte.TYPE || wStringArray == Byte.class) {
            Function function = this;
            object = (byte)Native.invokeInt(function, function.peer, n3, objectArray);
        } else if (wStringArray == Short.TYPE || wStringArray == Short.class) {
            Function function = this;
            object = (short)Native.invokeInt(function, function.peer, n3, objectArray);
        } else if (wStringArray == Character.TYPE || wStringArray == Character.class) {
            Function function = this;
            object = Character.valueOf((char)Native.invokeInt(function, function.peer, n3, objectArray));
        } else if (wStringArray == Integer.TYPE || wStringArray == Integer.class) {
            Function function = this;
            object = Native.invokeInt(function, function.peer, n3, objectArray);
        } else if (wStringArray == Long.TYPE || wStringArray == Long.class) {
            Function function = this;
            object = Native.invokeLong(function, function.peer, n3, objectArray);
        } else if (wStringArray == Float.TYPE || wStringArray == Float.class) {
            Function function = this;
            object = Float.valueOf(Native.invokeFloat(function, function.peer, n3, objectArray));
        } else if (wStringArray == Double.TYPE || wStringArray == Double.class) {
            Function function = this;
            object = Native.invokeDouble(function, function.peer, n3, objectArray);
        } else if (wStringArray == String.class) {
            object = this.invokeString(n3, objectArray, false);
        } else if (wStringArray == WString.class) {
            if ((objectArray = this.invokeString(n3, objectArray, true)) != null) {
                object = new WString((String)objectArray);
            }
        } else {
            if (Pointer.class.isAssignableFrom((Class<?>)wStringArray)) {
                return this.invokePointer(n3, objectArray);
            }
            if (Structure.class.isAssignableFrom((Class<?>)wStringArray)) {
                if (Structure.ByValue.class.isAssignableFrom((Class<?>)wStringArray)) {
                    Function function = this;
                    objectArray = Native.invokeStructure(function, function.peer, n3, objectArray, Structure.newInstance(wStringArray));
                    objectArray.autoRead();
                    object = objectArray;
                } else {
                    object = this.invokePointer(n3, objectArray);
                    if (object != null) {
                        objectArray = Structure.newInstance(wStringArray, (Pointer)object);
                        objectArray.conditionalAutoRead();
                        object = objectArray;
                    }
                }
            } else if (Callback.class.isAssignableFrom((Class<?>)wStringArray)) {
                object = this.invokePointer(n3, objectArray);
                if (object != null) {
                    object = CallbackReference.getCallback(wStringArray, (Pointer)object);
                }
            } else if (wStringArray == String[].class) {
                if ((objectArray = this.invokePointer(n3, objectArray)) != null) {
                    object = objectArray.getStringArray(0L, this.encoding);
                }
            } else if (wStringArray == WString[].class) {
                if ((objectArray = this.invokePointer(n3, objectArray)) != null) {
                    objectArray = objectArray.getWideStringArray(0L);
                    wStringArray = new WString[objectArray.length];
                    for (n2 = 0; n2 < objectArray.length; ++n2) {
                        wStringArray[n2] = new WString((String)objectArray[n2]);
                    }
                    object = wStringArray;
                }
            } else if (wStringArray == Pointer[].class) {
                if ((objectArray = this.invokePointer(n3, objectArray)) != null) {
                    object = objectArray.getPointerArray(0L);
                }
            } else if (n2 != 0) {
                Function function = this;
                object = Native.invokeObject(function, function.peer, n3, objectArray);
                if (object != null && !wStringArray.isAssignableFrom(object.getClass())) {
                    throw new ClassCastException("Return type " + wStringArray + " does not match result " + object.getClass());
                }
            } else {
                throw new IllegalArgumentException("Unsupported return type " + wStringArray + " in function " + this.getName());
            }
        }
        return object;
    }

    private Pointer invokePointer(int n2, Object[] objectArray) {
        Function function = this;
        long l2 = Native.invokePointer(function, function.peer, n2, objectArray);
        if (l2 == 0L) {
            return null;
        }
        return new Pointer(l2);
    }

    private Object convertArgument(Object[] object, int n2, Method object2, TypeMapper classArray, boolean bl, Class<?> clazz) {
        Structure[] structureArray;
        Class<?> clazz2;
        Object object3 = object[n2];
        if (object3 != null) {
            clazz2 = object3.getClass();
            structureArray = null;
            if (NativeMapped.class.isAssignableFrom(clazz2)) {
                structureArray = NativeMappedConverter.getInstance(clazz2);
            } else if (classArray != null) {
                structureArray = classArray.getToNativeConverter(clazz2);
            }
            if (structureArray != null) {
                object = object2 != null ? new MethodParameterContext(this, (Object[])object, n2, (Method)object2) : new FunctionParameterContext(this, (Object[])object, n2);
                object3 = structureArray.toNative(object3, (ToNativeContext)object);
            }
        }
        if (object3 == null || this.isPrimitiveArray(object3.getClass())) {
            return object3;
        }
        clazz2 = object3.getClass();
        if (object3 instanceof Structure) {
            structureArray = (Structure)object3;
            structureArray.autoWrite();
            if (structureArray instanceof Structure.ByValue) {
                object = structureArray.getClass();
                if (object2 != null) {
                    classArray = object2.getParameterTypes();
                    if (IS_VARARGS.isVarArgs((Method)object2) && n2 >= classArray.length - 1) {
                        object2 = classArray[classArray.length - 1].getComponentType();
                        if (object2 != Object.class) {
                            object = object2;
                        }
                    } else {
                        object = classArray[n2];
                    }
                }
                if (Structure.ByValue.class.isAssignableFrom((Class<?>)object)) {
                    return structureArray;
                }
            }
            return structureArray.getPointer();
        }
        if (object3 instanceof Callback) {
            return CallbackReference.getFunctionPointer((Callback)object3);
        }
        if (object3 instanceof String) {
            return new NativeString((String)object3, this.encoding).getPointer();
        }
        if (object3 instanceof WString) {
            return new NativeString(object3.toString(), true).getPointer();
        }
        if (object3 instanceof Boolean) {
            if (Boolean.TRUE.equals(object3)) {
                return INTEGER_TRUE;
            }
            return INTEGER_FALSE;
        }
        if (String[].class == clazz2) {
            return new StringArray((String[])object3, this.encoding);
        }
        if (WString[].class == clazz2) {
            return new StringArray((WString[])object3);
        }
        if (Pointer[].class == clazz2) {
            return new PointerArray((Pointer[])object3);
        }
        if (NativeMapped[].class.isAssignableFrom(clazz2)) {
            return new NativeMappedArray((NativeMapped[])object3);
        }
        if (Structure[].class.isAssignableFrom(clazz2)) {
            structureArray = (Structure[])object3;
            object = clazz2.getComponentType();
            boolean bl2 = Structure.ByReference.class.isAssignableFrom((Class<?>)object);
            if (clazz != null && !Structure.ByReference[].class.isAssignableFrom(clazz)) {
                if (bl2) {
                    throw new IllegalArgumentException("Function " + this.getName() + " declared Structure[] at parameter " + n2 + " but array of " + object + " was passed");
                }
                for (int i2 = 0; i2 < structureArray.length; ++i2) {
                    if (!(structureArray[i2] instanceof Structure.ByReference)) continue;
                    throw new IllegalArgumentException("Function " + this.getName() + " declared Structure[] at parameter " + n2 + " but element " + i2 + " is of Structure.ByReference type");
                }
            }
            if (bl2) {
                Structure.autoWrite(structureArray);
                object2 = new Pointer[structureArray.length + 1];
                for (int i3 = 0; i3 < structureArray.length; ++i3) {
                    object2[i3] = structureArray[i3] != null ? structureArray[i3].getPointer() : null;
                }
                return new PointerArray((Pointer[])object2);
            }
            if (structureArray.length == 0) {
                throw new IllegalArgumentException("Structure array must have non-zero length");
            }
            if (structureArray[0] == null) {
                ((Structure)Structure.newInstance(object)).toArray(structureArray);
                return structureArray[0].getPointer();
            }
            Structure.autoWrite(structureArray);
            return structureArray[0].getPointer();
        }
        if (clazz2.isArray()) {
            throw new IllegalArgumentException("Unsupported array argument type: " + clazz2.getComponentType());
        }
        if (bl) {
            return object3;
        }
        if (!Native.isSupportedNativeType(object3.getClass())) {
            throw new IllegalArgumentException("Unsupported argument type " + object3.getClass().getName() + " at parameter " + n2 + " of function " + this.getName());
        }
        return object3;
    }

    private boolean isPrimitiveArray(Class<?> clazz) {
        return clazz.isArray() && clazz.getComponentType().isPrimitive();
    }

    public void invoke(Object[] objectArray) {
        this.invoke(Void.class, objectArray);
    }

    private String invokeString(int n2, Object[] object, boolean bl) {
        Pointer pointer = this.invokePointer(n2, (Object[])object);
        object = null;
        if (pointer != null) {
            object = bl ? pointer.getWideString(0L) : pointer.getString(0L, this.encoding);
        }
        return object;
    }

    @Override
    public String toString() {
        if (this.library != null) {
            return "native function " + this.functionName + "(" + this.library.getName() + ")@0x" + Long.toHexString(this.peer);
        }
        return "native function@0x" + Long.toHexString(this.peer);
    }

    public Object invokeObject(Object[] objectArray) {
        return this.invoke(Object.class, objectArray);
    }

    public Pointer invokePointer(Object[] objectArray) {
        return (Pointer)this.invoke(Pointer.class, objectArray);
    }

    public String invokeString(Object[] object, boolean bl) {
        object = this.invoke(bl ? WString.class : String.class, (Object[])object);
        if (object != null) {
            return object.toString();
        }
        return null;
    }

    public int invokeInt(Object[] objectArray) {
        return (Integer)this.invoke(Integer.class, objectArray);
    }

    public long invokeLong(Object[] objectArray) {
        return (Long)this.invoke(Long.class, objectArray);
    }

    public float invokeFloat(Object[] objectArray) {
        return ((Float)this.invoke(Float.class, objectArray)).floatValue();
    }

    public double invokeDouble(Object[] objectArray) {
        return (Double)this.invoke(Double.class, objectArray);
    }

    public void invokeVoid(Object[] objectArray) {
        this.invoke(Void.class, objectArray);
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (object.getClass() == this.getClass()) {
            object = (Function)object;
            return ((Function)object).callFlags == this.callFlags && ((Function)object).options.equals(this.options) && ((Pointer)object).peer == this.peer;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.callFlags + this.options.hashCode() + super.hashCode();
    }

    static Object[] concatenateVarArgs(Object[] objectArray) {
        Object[] objectArray2;
        Class<?> clazz;
        if (objectArray != null && objectArray.length > 0 && (clazz = (objectArray2 = objectArray[objectArray.length - 1]) != null ? objectArray2.getClass() : null) != null && clazz.isArray()) {
            objectArray2 = objectArray2;
            for (int i2 = 0; i2 < objectArray2.length; ++i2) {
                if (!(objectArray2[i2] instanceof Float)) continue;
                objectArray2[i2] = (double)((Float)objectArray2[i2]).floatValue();
            }
            Object[] objectArray3 = new Object[objectArray.length + objectArray2.length];
            System.arraycopy(objectArray, 0, objectArray3, 0, objectArray.length - 1);
            System.arraycopy(objectArray2, 0, objectArray3, objectArray.length - 1, objectArray2.length);
            objectArray3[objectArray3.length - 1] = null;
            objectArray = objectArray3;
        }
        return objectArray;
    }

    static boolean isVarArgs(Method method) {
        return IS_VARARGS.isVarArgs(method);
    }

    static int fixedArgs(Method method) {
        return IS_VARARGS.fixedArgs(method);
    }

    static Boolean valueOf(boolean bl) {
        if (bl) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    static class PointerArray
    extends Memory
    implements PostCallRead {
        private final Pointer[] original;

        public PointerArray(Pointer[] pointerArray) {
            super(Native.POINTER_SIZE * (pointerArray.length + 1));
            this.original = pointerArray;
            for (int i2 = 0; i2 < pointerArray.length; ++i2) {
                ((Pointer)this).setPointer(i2 * Native.POINTER_SIZE, pointerArray[i2]);
            }
            ((Pointer)this).setPointer(Native.POINTER_SIZE * pointerArray.length, null);
        }

        @Override
        public void read() {
            ((Pointer)this).read(0L, this.original, 0, this.original.length);
        }
    }

    static class NativeMappedArray
    extends Memory
    implements PostCallRead {
        private final NativeMapped[] original;

        public NativeMappedArray(NativeMapped[] nativeMappedArray) {
            super(Native.getNativeSize(nativeMappedArray.getClass(), nativeMappedArray));
            this.original = nativeMappedArray;
            this.setValue(0L, this.original, this.original.getClass());
        }

        @Override
        public void read() {
            this.getValue(0L, this.original.getClass(), this.original);
        }
    }

    public static interface PostCallRead {
        public void read();
    }
}


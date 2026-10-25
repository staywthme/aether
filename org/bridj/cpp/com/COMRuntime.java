/*
 * Decompiled with CFR 0.152.
 */
package org.bridj.cpp.com;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import org.bridj.BridJ;
import org.bridj.BridJRuntime;
import org.bridj.CRuntime;
import org.bridj.FlagSet;
import org.bridj.NativeObject;
import org.bridj.Platform;
import org.bridj.Pointer;
import org.bridj.PointerIO;
import org.bridj.StructObject;
import org.bridj.ValuedEnum;
import org.bridj.ann.Convention;
import org.bridj.ann.Library;
import org.bridj.ann.Ptr;
import org.bridj.ann.Runtime;
import org.bridj.cpp.CPPObject;
import org.bridj.cpp.CPPRuntime;
import org.bridj.cpp.com.CLSID;
import org.bridj.cpp.com.IID;
import org.bridj.cpp.com.IUnknown;
import org.bridj.cpp.com.OLEAutomationLibrary;
import org.bridj.cpp.com.OLELibrary;
import org.bridj.cpp.com.VARENUM;
import org.bridj.cpp.com.VARIANT;
import org.bridj.util.Utils;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
@Library(value="Ole32")
@Runtime(value=CRuntime.class)
@Convention(value=Convention.Style.StdCall)
public class COMRuntime
extends CPPRuntime {
    public static final int CLSCTX_INPROC_SERVER = 1;
    public static final int CLSCTX_INPROC_HANDLER = 2;
    public static final int CLSCTX_LOCAL_SERVER = 4;
    public static final int CLSCTX_INPROC_SERVER16 = 8;
    public static final int CLSCTX_REMOTE_SERVER = 16;
    public static final int CLSCTX_INPROC_HANDLER16 = 32;
    public static final int CLSCTX_RESERVED1 = 64;
    public static final int CLSCTX_RESERVED2 = 128;
    public static final int CLSCTX_RESERVED3 = 256;
    public static final int CLSCTX_RESERVED4 = 512;
    public static final int CLSCTX_NO_CODE_DOWNLOAD = 1024;
    public static final int CLSCTX_RESERVED5 = 2048;
    public static final int CLSCTX_NO_CUSTOM_MARSHAL = 4096;
    public static final int CLSCTX_ENABLE_CODE_DOWNLOAD = 8192;
    public static final int CLSCTX_NO_FAILURE_LOG = 16384;
    public static final int CLSCTX_DISABLE_AAA = 32768;
    public static final int CLSCTX_ENABLE_AAA = 65536;
    public static final int CLSCTX_FROM_DEFAULT_CONTEXT = 131072;
    public static final int CLSCTX_ACTIVATE_32_BIT_SERVER = 262144;
    public static final int CLSCTX_ACTIVATE_64_BIT_SERVER = 524288;
    public static final int CLSCTX_ENABLE_CLOAKING = 0x100000;
    public static final int CLSCTX_PS_DLL = Integer.MIN_VALUE;
    public static final int CLSCTX_INPROC = 3;
    public static final int CLSCTX_ALL = 23;
    public static final int CLSCTX_SERVER = 21;
    public static final int S_OK = 0;
    public static final int S_FALSE = 1;
    public static final int REGDB_E_CLASSNOTREG = -2147221164;
    public static final int CLASS_E_NOAGGREGATION = -2147221232;
    public static final int CO_E_NOTINITIALIZED = -2147221008;
    public static final int E_UNEXPECTED = -2147418113;
    public static final int E_NOTIMPL = -2147467263;
    public static final int E_OUTOFMEMORY = -2147024882;
    public static final int E_INVALIDARG = -2147024809;
    public static final int E_NOINTERFACE = -2147467262;
    public static final int E_POINTER = -2147467261;
    public static final int E_HANDLE = -2147024890;
    public static final int E_ABORT = -2147467260;
    public static final int E_FAIL = -2147467259;
    public static final int E_ACCESSDENIED = -2147024891;
    public static final int DISP_E_BADVARTYPE = -2147352568;
    public static final int DISP_E_NOTACOLLECTION = -2147352559;
    public static final int DISP_E_MEMBERNOTFOUND = -2147352573;
    public static final int DISP_E_ARRAYISLOCKED = -2147352563;
    public static final int DISP_E_EXCEPTION = -2147352567;
    public static final int DISP_E_TYPEMISMATCH = -2147352571;
    public static final int DISP_E_BADINDEX = -2147352565;
    public static final int DISP_E_BADCALLEE = -2147352560;
    public static final int DISP_E_OVERFLOW = -2147352566;
    public static final int DISP_E_UNKNOWNINTERFACE = -2147352575;
    public static final int DISP_E_DIVBYZERO = -2147352558;
    public static final int DISP_E_UNKNOWNLCID = -2147352564;
    public static final int DISP_E_PARAMNOTOPTIONAL = -2147352561;
    public static final int DISP_E_PARAMNOTFOUND = -2147352572;
    public static final int DISP_E_BADPARAMCOUNT = -2147352562;
    public static final int DISP_E_BUFFERTOOSMALL = -2147352557;
    public static final int DISP_E_UNKNOWNNAME = -2147352570;
    public static final int DISP_E_NONAMEDARGS = -2147352569;
    static ThreadLocal<Object> comInitializer;
    private static final String model = "00000000-0000-0000-0000-000000000000";
    static final /* synthetic */ boolean $assertionsDisabled;

    @Deprecated
    public static native int CoCreateInstance(Pointer<Byte> var0, Pointer<IUnknown> var1, int var2, Pointer<Byte> var3, Pointer<Pointer<?>> var4);

    static native int CoInitializeEx(@Ptr long var0, int var2);

    static native int CoInitialize(@Ptr long var0);

    static native void CoUninitialize();

    static void error(int n2) {
        switch (n2) {
            case 0: {
                return;
            }
            case -2147418113: {
                throw new RuntimeException("Unexpected error");
            }
            case -2147024882: {
                throw new RuntimeException("Memory could not be allocated.");
            }
            case -2147221008: {
                throw new RuntimeException("CoInitialized wasn't called !!");
            }
            case -2147467262: {
                throw new RuntimeException("Interface does not inherit from class");
            }
            case -2147467261: {
                throw new RuntimeException("Allocated pointer pointer is null !!");
            }
            case -2147352563: {
                throw new RuntimeException("The variant contains an array that is locked.");
            }
            case -2147352568: {
                throw new RuntimeException("The variant type is not valid.");
            }
            case -2147024809: {
                throw new RuntimeException("One of the arguments is invalid.");
            }
        }
        throw new RuntimeException("Unexpected COM error code : " + n2);
    }

    public static <I extends IUnknown> Pointer<Byte> getIID(Class<I> clazz) {
        IID iID = clazz.getAnnotation(IID.class);
        if (iID == null) {
            throw new RuntimeException("No " + IID.class.getName() + " annotation set on type " + clazz.getName() + " !");
        }
        return COMRuntime.parseGUID(iID.value());
    }

    public static <I extends IUnknown> Pointer<Byte> getCLSID(Class<I> clazz) {
        CLSID cLSID = clazz.getAnnotation(CLSID.class);
        if (cLSID == null) {
            throw new RuntimeException("No " + CLSID.class.getName() + " annotation set on type " + clazz.getName() + " !");
        }
        return COMRuntime.parseGUID(cLSID.value());
    }

    @Override
    public boolean isSymbolOptional(Method method) {
        return true;
    }

    public static void initialize() {
        comInitializer.get();
    }

    public static <I extends IUnknown> I newInstance(Class<I> clazz) throws ClassNotFoundException {
        Class<I> clazz2 = clazz;
        return COMRuntime.newInstance(clazz2, clazz2);
    }

    public static <T extends IUnknown, I extends IUnknown> I newInstance(Class<T> object, Class<I> clazz) throws ClassNotFoundException {
        COMRuntime.initialize();
        Pointer<Pointer<?>> pointer = Pointer.allocatePointer();
        Pointer<Byte> pointer2 = COMRuntime.getCLSID(object);
        Pointer<Byte> pointer3 = COMRuntime.getIID(clazz);
        try {
            int n2 = COMRuntime.CoCreateInstance(pointer2, null, 23, pointer3, pointer);
            if (n2 == -2147221164) {
                throw new ClassNotFoundException("COM class is not registered : " + ((Class)object).getSimpleName() + " (clsid = " + pointer2.getCString() + ")");
            }
            COMRuntime.error(n2);
            object = pointer.getPointer();
            if (object == null) {
                throw new RuntimeException("Serious low-level issue : CoCreateInstance executed fine but we only retrieved a null pointer !");
            }
            object = (IUnknown)((Pointer)object).getNativeObject(clazz);
        }
        catch (Throwable throwable) {
            Pointer.release(pointer, pointer2, pointer3);
            throw throwable;
        }
        Pointer.release(pointer, pointer2, pointer3);
        return (I)object;
    }

    @Override
    public <T extends NativeObject> BridJRuntime.TypeInfo<T> getTypeInfo(Type type) {
        if (type == VARIANT.class) {
            return new VARIANTTypeInfo();
        }
        if (type instanceof Class) {
            if (CPPObject.class.isAssignableFrom((Class)type)) {
                return new CPPRuntime.CPPTypeInfo<CPPObject>(type){

                    @Override
                    public <V> Pointer<V> allocateStructMemory(PointerIO<V> pointerIO) {
                        return COMRuntime.this.allocateCOMMemory(pointerIO);
                    }
                };
            }
            if (StructObject.class.isAssignableFrom((Class)type)) {
                return new CRuntime.CTypeInfo<T>(type){

                    @Override
                    public <V> Pointer<V> allocateStructMemory(PointerIO<V> pointerIO) {
                        return COMRuntime.this.allocateCOMMemory(pointerIO);
                    }
                };
            }
        }
        return super.getTypeInfo(type);
    }

    public static Pointer<?> parseGUID(String string) {
        Pointer<Byte> pointer = Pointer.allocateBytes(20L);
        if ((string = string.replaceAll("-", "")).length() != 32) {
            throw new RuntimeException("Expected something like :\n00000000-0000-0000-0000-000000000000\nBut got instead :\n" + string);
        }
        pointer.setIntAtOffset(0L, (int)Long.parseLong(string.substring(0, 8), 16));
        pointer.setShortAtOffset(4L, (short)Long.parseLong(string.substring(8, 12), 16));
        pointer.setShortAtOffset(6L, (short)Long.parseLong(string.substring(12, 16), 16));
        for (int i2 = 0; i2 < 8; ++i2) {
            pointer.setByteAtOffset(i2 + 8, (byte)Long.parseLong(string.substring(16 + (i2 << 1), 16 + (i2 << 1) + 2), 16));
        }
        return pointer;
    }

    static ValuedEnum<VARENUM> getType(VARIANT structObject) {
        structObject = structObject.__VARIANT_NAME_1();
        structObject = ((VARIANT.__VARIANT_NAME_1_union)structObject).__VARIANT_NAME_2();
        short s2 = ((VARIANT.__VARIANT_NAME_1_union.__tagVARIANT)structObject).vt();
        return FlagSet.fromValue((int)s2, VARENUM.class);
    }

    static VARIANT setType(VARIANT vARIANT, ValuedEnum<VARENUM> valuedEnum) {
        StructObject structObject = vARIANT.__VARIANT_NAME_1();
        structObject = structObject.__VARIANT_NAME_2();
        ((VARIANT.__VARIANT_NAME_1_union.__tagVARIANT)structObject).vt((short)valuedEnum.value());
        return vARIANT;
    }

    static VARIANT.__VARIANT_NAME_1_union.__tagVARIANT.__VARIANT_NAME_3_union getValues(VARIANT structObject) {
        structObject = structObject.__VARIANT_NAME_1();
        structObject = ((VARIANT.__VARIANT_NAME_1_union)structObject).__VARIANT_NAME_2();
        structObject = ((VARIANT.__VARIANT_NAME_1_union.__tagVARIANT)structObject).__VARIANT_NAME_3();
        return structObject;
    }

    public static Object getValue(VARIANT structObject) {
        FlagSet<VARENUM> flagSet = FlagSet.fromValue(COMRuntime.getType((VARIANT)structObject));
        structObject = COMRuntime.getValues((VARIANT)structObject);
        if (flagSet.has(new VARENUM[]{VARENUM.VT_BYREF})) {
            switch ((VARENUM)flagSet.without(new VARENUM[]{VARENUM.VT_BYREF}).toEnum()) {
                case VT_DISPATCH: {
                    return ((VARIANT.__VARIANT_NAME_1_union.__tagVARIANT.__VARIANT_NAME_3_union)structObject).ppdispVal();
                }
                case VT_UNKNOWN: {
                    return ((VARIANT.__VARIANT_NAME_1_union.__tagVARIANT.__VARIANT_NAME_3_union)structObject).ppunkVal();
                }
                case VT_VARIANT: {
                    return ((VARIANT.__VARIANT_NAME_1_union.__tagVARIANT.__VARIANT_NAME_3_union)structObject).pvarVal();
                }
                case VT_I1: 
                case VT_UI1: {
                    return ((VARIANT.__VARIANT_NAME_1_union.__tagVARIANT.__VARIANT_NAME_3_union)structObject).pbVal();
                }
                case VT_I2: 
                case VT_UI2: {
                    return ((VARIANT.__VARIANT_NAME_1_union.__tagVARIANT.__VARIANT_NAME_3_union)structObject).piVal();
                }
                case VT_I4: 
                case VT_UI4: {
                    return ((VARIANT.__VARIANT_NAME_1_union.__tagVARIANT.__VARIANT_NAME_3_union)structObject).plVal();
                }
                case VT_R4: {
                    return ((VARIANT.__VARIANT_NAME_1_union.__tagVARIANT.__VARIANT_NAME_3_union)structObject).pfltVal();
                }
                case VT_R8: {
                    return ((VARIANT.__VARIANT_NAME_1_union.__tagVARIANT.__VARIANT_NAME_3_union)structObject).pdblVal();
                }
                case VT_I8: 
                case VT_UI8: {
                    return ((VARIANT.__VARIANT_NAME_1_union.__tagVARIANT.__VARIANT_NAME_3_union)structObject).pllVal();
                }
                case VT_BOOL: {
                    return ((VARIANT.__VARIANT_NAME_1_union.__tagVARIANT.__VARIANT_NAME_3_union)structObject).pbVal().as(Boolean.class);
                }
                case VT_BSTR: {
                    return ((VARIANT.__VARIANT_NAME_1_union.__tagVARIANT.__VARIANT_NAME_3_union)structObject).pbstrVal();
                }
                case VT_LPSTR: {
                    return ((VARIANT.__VARIANT_NAME_1_union.__tagVARIANT.__VARIANT_NAME_3_union)structObject).byref().getCString();
                }
                case VT_LPWSTR: {
                    return ((VARIANT.__VARIANT_NAME_1_union.__tagVARIANT.__VARIANT_NAME_3_union)structObject).byref().getWideCString();
                }
            }
            return ((VARIANT.__VARIANT_NAME_1_union.__tagVARIANT.__VARIANT_NAME_3_union)structObject).byref();
        }
        switch (flagSet.toEnum()) {
            case VT_I1: 
            case VT_UI1: {
                return ((VARIANT.__VARIANT_NAME_1_union.__tagVARIANT.__VARIANT_NAME_3_union)structObject).bVal();
            }
            case VT_I2: 
            case VT_UI2: {
                return ((VARIANT.__VARIANT_NAME_1_union.__tagVARIANT.__VARIANT_NAME_3_union)structObject).uiVal();
            }
            case VT_I4: 
            case VT_UI4: {
                return ((VARIANT.__VARIANT_NAME_1_union.__tagVARIANT.__VARIANT_NAME_3_union)structObject).ulVal();
            }
            case VT_I8: 
            case VT_UI8: {
                return ((VARIANT.__VARIANT_NAME_1_union.__tagVARIANT.__VARIANT_NAME_3_union)structObject).ullVal();
            }
            case VT_BOOL: {
                return ((VARIANT.__VARIANT_NAME_1_union.__tagVARIANT.__VARIANT_NAME_3_union)structObject).bVal() != 0;
            }
            case VT_R4: {
                return Float.valueOf(((VARIANT.__VARIANT_NAME_1_union.__tagVARIANT.__VARIANT_NAME_3_union)structObject).fltVal());
            }
            case VT_R8: {
                return ((VARIANT.__VARIANT_NAME_1_union.__tagVARIANT.__VARIANT_NAME_3_union)structObject).dblVal();
            }
            case VT_BSTR: {
                return ((VARIANT.__VARIANT_NAME_1_union.__tagVARIANT.__VARIANT_NAME_3_union)structObject).bstrVal().getString(Pointer.StringType.BSTR);
            }
            case VT_EMPTY: {
                return null;
            }
        }
        throw new UnsupportedOperationException("Conversion not implemented yet from VARIANT type " + flagSet + " to Java !");
    }

    static void change(VARIANT object, ValuedEnum<VARENUM> valuedEnum) {
        object = Pointer.getPointer(object);
        int n2 = OLEAutomationLibrary.VariantChangeType(object, (Pointer<VARIANT>)object, (short)0, (short)valuedEnum.value());
        if (!$assertionsDisabled && n2 != 0) {
            throw new AssertionError();
        }
    }

    public static VARIANT setValue(VARIANT vARIANT, Object object) {
        VARIANT.__VARIANT_NAME_1_union.__tagVARIANT.__VARIANT_NAME_3_union __VARIANT_NAME_3_union2 = COMRuntime.getValues(vARIANT);
        if (object == null) {
            COMRuntime.change(vARIANT, VARENUM.VT_EMPTY);
        } else if (object instanceof Integer) {
            COMRuntime.change(vARIANT, VARENUM.VT_I4);
            __VARIANT_NAME_3_union2.lVal(((Integer)object).intValue());
        } else if (object instanceof Long) {
            COMRuntime.change(vARIANT, VARENUM.VT_I8);
            __VARIANT_NAME_3_union2.llval((Long)object);
        } else if (object instanceof Short) {
            COMRuntime.change(vARIANT, VARENUM.VT_I2);
            __VARIANT_NAME_3_union2.iVal((Short)object);
        } else if (object instanceof Byte) {
            COMRuntime.change(vARIANT, VARENUM.VT_I1);
            __VARIANT_NAME_3_union2.bVal((Byte)object);
        } else if (object instanceof Float) {
            COMRuntime.change(vARIANT, VARENUM.VT_R4);
            __VARIANT_NAME_3_union2.fltVal(((Float)object).floatValue());
        } else if (object instanceof Double) {
            COMRuntime.change(vARIANT, VARENUM.VT_I8);
            __VARIANT_NAME_3_union2.dblVal((Double)object);
        } else if (object instanceof Character) {
            COMRuntime.change(vARIANT, VARENUM.VT_I2);
            __VARIANT_NAME_3_union2.iVal((short)((Character)object).charValue());
        } else if (object instanceof String) {
            COMRuntime.change(vARIANT, VARENUM.VT_BSTR);
            __VARIANT_NAME_3_union2.bstrVal().setString((String)object, Pointer.StringType.BSTR);
        } else if (object instanceof Pointer) {
            object = (Pointer)object;
            object = ((Pointer)object).getTargetType();
            if ((object = Utils.getClass((Type)object)) == null) {
                COMRuntime.change(vARIANT, VARENUM.VT_PTR);
            } else {
                object = object == Integer.class || object == Integer.TYPE ? VARENUM.VT_I4 : (object == Long.class || object == Long.TYPE ? VARENUM.VT_I8 : (object == Short.class || object == Short.TYPE ? VARENUM.VT_I2 : (object == Byte.class || object == Byte.TYPE ? VARENUM.VT_I1 : (object == Character.class || object == Character.TYPE ? VARENUM.VT_LPWSTR : (object == Boolean.class || object == Boolean.TYPE ? VARENUM.VT_BOOL : (object == Float.class || object == Float.TYPE ? VARENUM.VT_R4 : (object == Double.class || object == Double.TYPE ? VARENUM.VT_R8 : (Pointer.class.isAssignableFrom((Class<?>)object) ? VARENUM.VT_PTR : null))))))));
                COMRuntime.change(vARIANT, FlagSet.fromValues((Enum[])new VARENUM[]{VARENUM.VT_BYREF, object}));
            }
        } else {
            throw new UnsupportedOperationException("Unable to convert an object of type " + object.getClass().getName() + " to a COM VARIANT object !");
        }
        return vARIANT;
    }

    @Override
    public boolean warnAboutMissingVTables() {
        return false;
    }

    public static String toString(VARIANT vARIANT) {
        StringBuilder stringBuilder = new StringBuilder("Variant(value = ");
        try {
            stringBuilder.append(COMRuntime.getValue(vARIANT));
        }
        catch (Throwable throwable) {
            stringBuilder.append("?");
        }
        stringBuilder.append(", type = ").append(COMRuntime.getType(vARIANT)).append(")");
        return stringBuilder.toString();
    }

    public static VARIANT clone(VARIANT vARIANT) {
        if (vARIANT == null) {
            return null;
        }
        VARIANT vARIANT2 = new VARIANT();
        COMRuntime.error(OLEAutomationLibrary.VariantCopy(Pointer.getPointer(vARIANT2), Pointer.getPointer(vARIANT)));
        return vARIANT2;
    }

    protected <V> Pointer<V> allocateCOMMemory(PointerIO<V> pointerIO) {
        return COMRuntime.allocateCOMMemory(pointerIO.getTargetSize(), pointerIO);
    }

    public static <V> Pointer<V> allocateCOMMemory(long l2, PointerIO<V> pointerIO) {
        if (l2 <= 0L) {
            return null;
        }
        long l3 = OLELibrary.CoTaskMemAlloc$raw(l2);
        if (l3 == 0L) {
            throw new OutOfMemoryError("Failed to allocate " + l2 + " bytes with CoTaskMemAlloc");
        }
        Pointer<V> pointer = Pointer.pointerToAddress(l3, l2, pointerIO, new Pointer.Releaser(){

            @Override
            public final void release(Pointer<?> pointer) {
                OLELibrary.CoTaskMemFree(pointer);
            }
        });
        if (pointer != null) {
            pointer.clearValidBytes();
        }
        return pointer;
    }

    static {
        boolean bl = $assertionsDisabled = !COMRuntime.class.desiredAssertionStatus();
        if (Platform.isWindows()) {
            BridJ.register();
        }
        comInitializer = new ThreadLocal<Object>(){

            @Override
            protected final Object initialValue() {
                COMRuntime.error(COMRuntime.CoInitializeEx(0L, 0));
                return new Object(){

                    protected void finalize() throws Throwable {
                        COMRuntime.CoUninitialize();
                    }
                };
            }
        };
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public class VARIANTTypeInfo
    extends CRuntime.CTypeInfo<VARIANT> {
        Pointer.Releaser VARIANTReleaser;

        public VARIANTTypeInfo() {
            super((Type)((Object)VARIANT.class));
            this.VARIANTReleaser = new Pointer.Releaser(){

                @Override
                public void release(Pointer<?> pointer) {
                    COMRuntime.error(OLEAutomationLibrary.VariantClear(pointer));
                }
            };
        }

        @Override
        public void initialize(VARIANT vARIANT, int n2, Object ... objectArray) {
            assert (n2 < 0 && objectArray.length == 0);
            Pointer<VARIANT> pointer = COMRuntime.this.allocateCOMMemory(this.pointerIO).withReleaser(this.VARIANTReleaser);
            COMRuntime.this.setNativeObjectPeer(vARIANT, pointer);
            OLEAutomationLibrary.VariantInit(pointer);
        }

        @Override
        public VARIANT clone(VARIANT vARIANT) throws CloneNotSupportedException {
            return COMRuntime.clone(vARIANT);
        }
    }

    public static interface COINIT {
        public static final int COINIT_APARTMENTTHREADED = 2;
        public static final int COINIT_MULTITHREADED = 0;
        public static final int COINIT_DISABLE_OLE1DDE = 4;
        public static final int COINIT_SPEED_OVER_MEMORY = 8;
    }
}


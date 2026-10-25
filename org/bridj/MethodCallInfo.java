/*
 * Decompiled with CFR 0.152.
 */
package org.bridj;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import org.bridj.BridJ;
import org.bridj.CallIO;
import org.bridj.DyncallStructs;
import org.bridj.LastError;
import org.bridj.NativeConstants;
import org.bridj.NativeObject;
import org.bridj.Platform;
import org.bridj.Pointer;
import org.bridj.SizeT;
import org.bridj.StructIO;
import org.bridj.StructObject;
import org.bridj.TimeT;
import org.bridj.ValuedEnum;
import org.bridj.ann.CLong;
import org.bridj.ann.Constructor;
import org.bridj.ann.Convention;
import org.bridj.ann.DisableDirect;
import org.bridj.ann.Ptr;
import org.bridj.ann.SetsLastError;
import org.bridj.ann.Virtual;
import org.bridj.dyncall.DyncallLibrary;
import org.bridj.util.AnnotationUtils;
import org.bridj.util.Utils;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class MethodCallInfo {
    List<CallIO> callIOs;
    private Class<?> declaringClass;
    long nativeClass;
    int returnValueType;
    int[] paramsValueTypes;
    Method method;
    String methodName;
    String symbolName;
    private long forwardedPointer;
    String dcSignature;
    String javaSignature;
    String asmSignature;
    Object javaCallback;
    boolean isGenericCallback;
    boolean isObjCBlock;
    int virtualIndex;
    int virtualTableOffset;
    private int dcCallingConvention;
    boolean isVarArgs;
    boolean isStatic;
    boolean isCPlusPlus;
    boolean direct;
    boolean startsWithThis;
    boolean bNeedsThisPointer;
    boolean throwsLastError;
    boolean setsLastError;
    boolean hasCC;

    public MethodCallInfo(Method method) {
        Method method2 = method;
        this(method2, method2);
    }

    static boolean derivesFrom(Class<?> clazz, String string) {
        while (clazz != null) {
            if (clazz.getName().equals(string)) {
                return true;
            }
            clazz = clazz.getSuperclass();
        }
        return false;
    }

    public MethodCallInfo(Type type, Type[] typeArray, boolean bl) {
        this(type, new Annotation[0], typeArray, new Annotation[typeArray.length][], bl);
    }

    public MethodCallInfo(Type type, Annotation[] annotationArray, Type[] typeArray, Annotation[][] annotationArray2, boolean bl) {
        this.virtualIndex = -1;
        this.virtualTableOffset = 0;
        this.dcCallingConvention = 0;
        this.init(null, Utils.getClass(type), type, annotationArray, Utils.getClasses(typeArray), typeArray, annotationArray2, bl, false, true);
    }

    public MethodCallInfo(Method list2, Method method) {
        this.virtualIndex = -1;
        this.virtualTableOffset = 0;
        this.dcCallingConvention = 0;
        this.setMethod((Method)((Object)list2));
        this.setDeclaringClass(((Method)((Object)list2)).getDeclaringClass());
        this.symbolName = this.methodName;
        int bl = ((Method)((Object)list2)).getModifiers();
        this.isStatic = Modifier.isStatic(bl);
        this.isVarArgs = ((Method)((Object)list2)).isVarArgs();
        boolean bl2 = Modifier.isNative(bl);
        boolean bl3 = AnnotationUtils.isAnnotationPresent(Virtual.class, method, new Annotation[0]);
        boolean bl4 = AnnotationUtils.getInheritableAnnotation(DisableDirect.class, method, new Annotation[0]) == null && BridJ.isDirectModeEnabled();
        this.isCPlusPlus = !this.isStatic && MethodCallInfo.derivesFrom(((Method)((Object)list2)).getDeclaringClass(), "org.bridj.cpp.CPPObject");
        this.isObjCBlock = !this.isStatic && MethodCallInfo.derivesFrom(((Method)((Object)list2)).getDeclaringClass(), "org.bridj.objc.ObjCBlock");
        List<Class<?>> list3 = list2;
        this.init((AnnotatedElement)((Object)list3), ((Method)((Object)list3)).getReturnType(), ((Method)((Object)list2)).getGenericReturnType(), ((AccessibleObject)((Object)list2)).getAnnotations(), ((Method)((Object)list2)).getParameterTypes(), ((Method)((Object)list2)).getGenericParameterTypes(), ((Method)((Object)list2)).getParameterAnnotations(), bl2, bl3, bl4);
        list2 = AnnotationUtils.getInheritableAnnotation(Convention.class, method, new Annotation[0]);
        if (list2 != null) {
            this.setCallingConvention(list2.value());
        }
        if (!(list2 = Arrays.asList(method.getExceptionTypes())).isEmpty()) {
            this.direct = false;
            if (list2.contains(LastError.class)) {
                this.throwsLastError = true;
                this.setsLastError = true;
            }
        }
        if (AnnotationUtils.getInheritableAnnotation(SetsLastError.class, method, new Annotation[0]) != null) {
            this.direct = false;
            this.setsLastError = true;
        }
    }

    protected void init(AnnotatedElement annotatedElement, Class clazz, Type type, Annotation[] annotationArray, Class[] classArray, Type[] typeArray, Annotation[][] annotationArray2, boolean n2, boolean bl, boolean bl2) {
        assert (clazz != null);
        assert (type != null);
        assert (classArray != null);
        assert (typeArray != null);
        assert (annotationArray != null);
        assert (classArray.length == typeArray.length);
        assert (annotationArray2.length == typeArray.length);
        int n3 = typeArray.length;
        this.paramsValueTypes = new int[n3];
        this.direct = bl2;
        StringBuilder stringBuilder = new StringBuilder(64);
        StringBuilder stringBuilder2 = new StringBuilder(64);
        StringBuilder stringBuilder3 = new StringBuilder(16);
        stringBuilder.append('(');
        stringBuilder2.append('(');
        if (n2 != 0) {
            stringBuilder3.append('p').append('p');
        }
        if (BridJ.debug) {
            BridJ.info("Analyzing " + (this.declaringClass == null ? "anonymous method" : this.declaringClass.getName() + "." + this.methodName));
        }
        if (this.isObjCBlock) {
            this.appendToSignature(0, NativeConstants.ValueType.ePointerValue, Pointer.class, (Type)((Object)Pointer.class), null, stringBuilder3, null);
        }
        for (n2 = 0; n2 < n3; ++n2) {
            Type type2 = typeArray[n2];
            Class clazz2 = classArray[n2];
            NativeConstants.ValueType valueType = this.getValueType(n2, n3, clazz2, type2, null, annotationArray2[n2]);
            if (BridJ.veryVerbose) {
                BridJ.info("\tparam " + (Object)((Object)valueType));
            }
            this.paramsValueTypes[n2] = valueType.ordinal();
            this.appendToSignature(n2, valueType, clazz2, type2, stringBuilder, stringBuilder3, stringBuilder2);
        }
        stringBuilder.append(')');
        stringBuilder2.append(')');
        stringBuilder3.append(')');
        NativeConstants.ValueType valueType = this.getValueType(-1, n3, clazz, type, annotatedElement, annotationArray);
        if (BridJ.veryVerbose) {
            BridJ.info("\treturns " + (Object)((Object)valueType));
        }
        this.appendToSignature(-1, valueType, clazz, type, stringBuilder, stringBuilder3, stringBuilder2);
        this.returnValueType = valueType.ordinal();
        this.javaSignature = stringBuilder.toString();
        this.asmSignature = stringBuilder2.toString();
        this.dcSignature = stringBuilder3.toString();
        boolean bl3 = this.isCPlusPlus = this.isCPlusPlus || bl;
        if (this.isCPlusPlus && !this.isStatic) {
            if (!this.startsWithThis) {
                this.direct = false;
            }
            this.bNeedsThisPointer = true;
            if (Platform.isWindows() && !Platform.is64Bits()) {
                this.setDcCallingConvention(5);
            }
        }
        if (n3 > Platform.getMaxDirectMappingArgCount()) {
            this.direct = false;
        }
        if (BridJ.veryVerbose) {
            BridJ.info("\t-> direct " + this.direct);
            BridJ.info("\t-> javaSignature " + this.javaSignature);
            BridJ.info("\t-> callIOs " + this.callIOs);
            BridJ.info("\t-> asmSignature " + this.asmSignature);
            BridJ.info("\t-> dcSignature " + this.dcSignature);
        }
        if (BridJ.veryVerbose) {
            BridJ.info((this.direct ? "[mappable as direct] " : "[not mappable as direct] ") + this.method);
        }
    }

    public boolean hasCallingConvention() {
        return this.hasCC;
    }

    /*
     * Unable to fully structure code
     */
    public void setCallingConvention(Convention.Style var1_1) {
        if (var1_1 == null) {
            return;
        }
        if (!Platform.isWindows() || Platform.is64Bits()) {
            return;
        }
        switch (1.$SwitchMap$org$bridj$ann$Convention$Style[var1_1.ordinal()]) {
            case 1: {
                this.direct = false;
                this.setDcCallingConvention(Platform.isWindows() != false ? 3 : 0);
                break;
            }
            case 2: 
            case 3: {
                this.direct = false;
                v0 = this;
                v1 = 2;
                ** GOTO lbl19
            }
            case 4: {
                this.direct = false;
                v0 = this;
                v1 = Platform.isWindows() != false ? 5 : 0;
lbl19:
                // 2 sources

                v0.setDcCallingConvention(v1);
            }
        }
        if (BridJ.veryVerbose) {
            BridJ.info("Setting CC " + (Object)var1_1 + " (-> " + this.dcCallingConvention + ") for " + this.methodName);
        }
    }

    void addCallIO(CallIO callIO) {
        if (this.callIOs == null) {
            this.callIOs = new ArrayList<CallIO>();
        }
        assert (callIO != null);
        this.callIOs.add(callIO);
    }

    public CallIO[] getCallIOs() {
        if (this.callIOs == null) {
            return new CallIO[0];
        }
        return this.callIOs.toArray(new CallIO[this.callIOs.size()]);
    }

    public void prependCallbackCC() {
        char c2 = MethodCallInfo.getDcCallbackConvention(this.getDcCallingConvention());
        if (c2 == '\u0000') {
            return;
        }
        this.dcSignature = "_" + String.valueOf(c2) + this.dcSignature;
    }

    public String getDcSignature() {
        return this.dcSignature;
    }

    public String getJavaSignature() {
        return this.javaSignature;
    }

    public String getASMSignature() {
        return this.asmSignature;
    }

    boolean getBoolAnnotation(Class<? extends Annotation> object, AnnotatedElement annotatedElement, Annotation ... annotationArray) {
        return (object = AnnotationUtils.getAnnotation(object, annotatedElement, annotationArray)) != null;
    }

    public NativeConstants.ValueType getValueType(int n2, int n3, Class<?> clazz, Type type, AnnotatedElement object, Annotation ... annotationArray) {
        boolean bl = AnnotationUtils.isAnnotationPresent(Ptr.class, (AnnotatedElement)object, annotationArray);
        boolean bl2 = AnnotationUtils.isAnnotationPresent(CLong.class, (AnnotatedElement)object, annotationArray);
        object = AnnotationUtils.getAnnotation(Constructor.class, (AnnotatedElement)object, annotationArray);
        if (bl || object != null || bl2) {
            if (clazz != Long.class && clazz != Long.TYPE) {
                throw new RuntimeException("Annotation should only be used on a long parameter, not on a " + clazz.getName());
            }
            if (bl) {
                if (!Platform.is64Bits()) {
                    this.direct = false;
                }
            } else if (bl2) {
                if (Platform.CLONG_SIZE != 8) {
                    this.direct = false;
                }
            } else if (object != null) {
                this.isCPlusPlus = true;
                this.startsWithThis = true;
                if (n2 != 0) {
                    throw new RuntimeException("Annotation " + Constructor.class.getName() + " cannot have more than one (long) argument");
                }
            }
            if (bl2) {
                return NativeConstants.ValueType.eCLongValue;
            }
            return NativeConstants.ValueType.eSizeTValue;
        }
        if (clazz == null || clazz.equals(Void.TYPE)) {
            return NativeConstants.ValueType.eVoidValue;
        }
        if (clazz == Integer.class || clazz == Integer.TYPE) {
            return NativeConstants.ValueType.eIntValue;
        }
        if (clazz == Long.class || clazz == Long.TYPE) {
            if (!bl || Platform.is64Bits()) {
                return NativeConstants.ValueType.eLongValue;
            }
            return NativeConstants.ValueType.eIntValue;
        }
        if (clazz == Short.class || clazz == Short.TYPE) {
            return NativeConstants.ValueType.eShortValue;
        }
        if (clazz == Byte.class || clazz == Byte.TYPE) {
            return NativeConstants.ValueType.eByteValue;
        }
        if (clazz == Boolean.class || clazz == Boolean.TYPE) {
            return NativeConstants.ValueType.eBooleanValue;
        }
        if (clazz == Float.class || clazz == Float.TYPE) {
            this.usesFloats();
            return NativeConstants.ValueType.eFloatValue;
        }
        if (clazz == Character.TYPE || clazz == Character.TYPE) {
            if (Platform.WCHAR_T_SIZE != 2) {
                this.direct = false;
            }
            return NativeConstants.ValueType.eWCharValue;
        }
        if (clazz == Double.class || clazz == Double.TYPE) {
            this.usesFloats();
            return NativeConstants.ValueType.eDoubleValue;
        }
        if (clazz == org.bridj.CLong.class) {
            this.direct = false;
            return NativeConstants.ValueType.eCLongObjectValue;
        }
        if (clazz == SizeT.class) {
            this.direct = false;
            return NativeConstants.ValueType.eSizeTObjectValue;
        }
        if (clazz == TimeT.class) {
            this.direct = false;
            return NativeConstants.ValueType.eTimeTObjectValue;
        }
        if (Pointer.class.isAssignableFrom(clazz)) {
            this.direct = false;
            CallIO callIO = CallIO.Utils.createPointerCallIO(clazz, type);
            if (BridJ.veryVerbose) {
                BridJ.info("CallIO : " + callIO);
            }
            this.addCallIO(callIO);
            return NativeConstants.ValueType.ePointerValue;
        }
        if (clazz.isArray() && n2 == n3 - 1) {
            this.direct = false;
            return NativeConstants.ValueType.eEllipsis;
        }
        if (ValuedEnum.class.isAssignableFrom(clazz)) {
            this.direct = false;
            CallIO callIO = CallIO.Utils.createValuedEnumCallIO(Utils.getClass(Utils.getUniqueParameterizedTypeParameter(type)));
            if (BridJ.veryVerbose) {
                BridJ.info("CallIO : " + callIO);
            }
            this.addCallIO(callIO);
            return NativeConstants.ValueType.eIntFlagSet;
        }
        if (NativeObject.class.isAssignableFrom(clazz)) {
            Pointer<DyncallLibrary.DCstruct> pointer = null;
            if (StructObject.class.isAssignableFrom(clazz)) {
                StructIO structIO = StructIO.getInstance(clazz, type);
                try {
                    pointer = DyncallStructs.buildDCstruct(structIO.desc);
                }
                catch (Throwable throwable) {
                    BridJ.error("Unable to create low-level struct metadata for " + Utils.toString(type) + " : won't be able to use it as a by-value function argument.", throwable);
                }
            }
            this.addCallIO(new CallIO.NativeObjectHandler(clazz, type, pointer));
            this.direct = false;
            return NativeConstants.ValueType.eNativeObjectValue;
        }
        throw new NoSuchElementException("No " + NativeConstants.ValueType.class.getSimpleName() + " for class " + clazz.getName());
    }

    void usesFloats() {
    }

    public void appendToSignature(int n2, NativeConstants.ValueType valueType, Class<?> object, Type type, StringBuilder stringBuilder, StringBuilder stringBuilder2, StringBuilder stringBuilder3) {
        char c2;
        Object object2 = null;
        switch (valueType) {
            case eVoidValue: {
                c2 = 'v';
                object = "V";
                break;
            }
            case eIntValue: {
                c2 = 'i';
                object = "I";
                break;
            }
            case eLongValue: {
                c2 = 'l';
                object = "J";
                break;
            }
            case eCLongValue: {
                object = "J";
                c2 = 'j';
                if (Platform.CLONG_SIZE == 8) break;
                this.direct = false;
                break;
            }
            case eSizeTValue: {
                object = "J";
                if (Platform.SIZE_T_SIZE == 8) {
                    c2 = 'l';
                    break;
                }
                c2 = 'i';
                this.direct = false;
                break;
            }
            case eShortValue: {
                c2 = 's';
                object = "S";
                break;
            }
            case eDoubleValue: {
                c2 = 'd';
                object = "D";
                break;
            }
            case eFloatValue: {
                c2 = 'f';
                object = "F";
                break;
            }
            case eByteValue: {
                c2 = 'c';
                object = "B";
                break;
            }
            case eBooleanValue: {
                c2 = 'B';
                object = "Z";
                break;
            }
            case eWCharValue: {
                switch (Platform.WCHAR_T_SIZE) {
                    case 1: {
                        c2 = 'c';
                        this.direct = false;
                        break;
                    }
                    case 2: {
                        c2 = 's';
                        break;
                    }
                    case 4: {
                        c2 = 'i';
                        this.direct = false;
                        break;
                    }
                    default: {
                        throw new RuntimeException("Unhandled sizeof(wchar_t) in GetJavaTypeSignature: " + Platform.WCHAR_T_SIZE);
                    }
                }
                object = "C";
                break;
            }
            case eIntFlagSet: {
                c2 = 'i';
                object = "L" + ((Class)object).getName().replace('.', '/') + ";";
                this.direct = false;
                break;
            }
            case eCLongObjectValue: {
                c2 = 'p';
                object = "Lorg/bridj/CLong;";
                this.direct = false;
                break;
            }
            case eSizeTObjectValue: {
                c2 = 'p';
                object = "Lorg/bridj/SizeT;";
                this.direct = false;
                break;
            }
            case eTimeTObjectValue: {
                c2 = 'p';
                object = "Lorg/bridj/TimeT;";
                this.direct = false;
                break;
            }
            case ePointerValue: {
                c2 = 'p';
                object = "L" + ((Class)object).getName().replace('.', '/') + ";";
                this.direct = false;
                break;
            }
            case eNativeObjectValue: {
                c2 = 'T';
                object = "L" + ((Class)object).getName().replace('.', '/') + ";";
                this.direct = false;
                break;
            }
            case eEllipsis: {
                object = "[Ljava/lang/Object;";
                c2 = '?';
                break;
            }
            default: {
                this.direct = false;
                throw new RuntimeException("Unhandled " + NativeConstants.ValueType.class.getSimpleName() + ": " + (Object)((Object)valueType));
            }
        }
        if (type instanceof ParameterizedType && n2 < 0) {
            Object object3 = (Type[])type;
            if ((object3 = object3.getActualTypeArguments()) != null && ((Type[])object3).length == 1) {
                if ((object3 = object3[0]) instanceof ParameterizedType) {
                    object3 = ((ParameterizedType)object3).getRawType();
                }
                if (object3 instanceof Class) {
                    object3 = (Class)object3;
                    if (((String)object).endsWith(";")) {
                        object2 = ((String)object).substring(0, ((String)object).length() - 1) + "<*L" + ((Class)object3).getName().replace('.', '/') + ";>";
                    }
                }
            }
        }
        if (stringBuilder != null) {
            stringBuilder.append((String)object);
        }
        if (object2 == null) {
            object2 = object;
        }
        if (stringBuilder3 != null) {
            stringBuilder3.append((String)object2);
        }
        if (stringBuilder2 != null) {
            stringBuilder2.append(c2);
        }
    }

    public void setMethod(Method method) {
        this.method = method;
        if (method != null) {
            this.methodName = method.getName();
        }
        if (this.declaringClass == null) {
            this.setDeclaringClass(method.getDeclaringClass());
        }
    }

    public void setJavaSignature(String string) {
        this.javaSignature = string;
    }

    public Method getMethod() {
        return this.method;
    }

    public void setDeclaringClass(Class<?> clazz) {
        this.declaringClass = clazz;
    }

    public Class<?> getDeclaringClass() {
        return this.declaringClass;
    }

    public void setForwardedPointer(long l2) {
        this.forwardedPointer = l2;
    }

    public long getForwardedPointer() {
        return this.forwardedPointer;
    }

    public void setVirtualIndex(int n2) {
        this.virtualIndex = n2;
        if (BridJ.veryVerbose) {
            BridJ.info("\t-> virtualIndex " + n2);
        }
    }

    public int getVirtualIndex() {
        return this.virtualIndex;
    }

    public String getSymbolName() {
        return this.symbolName;
    }

    public void setSymbolName(String string) {
        this.symbolName = string;
    }

    static char getDcCallbackConvention(int n2) {
        switch (n2) {
            case 2: {
                return 's';
            }
            case 3: {
                return 'F';
            }
            case 4: {
                return 'f';
            }
            case 5: {
                return '+';
            }
        }
        return '\u0000';
    }

    public void setDcCallingConvention(int n2) {
        this.hasCC = true;
        this.dcCallingConvention = n2;
    }

    public int getDcCallingConvention() {
        return this.dcCallingConvention;
    }

    public Object getJavaCallback() {
        return this.javaCallback;
    }

    public void setJavaCallback(Object object) {
        this.javaCallback = object;
    }

    public void setGenericCallback(boolean bl) {
        this.isGenericCallback = bl;
    }

    public boolean isGenericCallback() {
        return this.isGenericCallback;
    }

    public void setNativeClass(long l2) {
        this.nativeClass = l2;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package org.bridj;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import org.bridj.AbstractBridJRuntime;
import org.bridj.BridJ;
import org.bridj.BridJRuntime;
import org.bridj.Callback;
import org.bridj.CallbackInterface;
import org.bridj.CallbackNativeImplementer;
import org.bridj.DynamicFunction;
import org.bridj.DynamicFunctionFactory;
import org.bridj.IntValuedEnum;
import org.bridj.JNI;
import org.bridj.MethodCallInfo;
import org.bridj.NativeEntities;
import org.bridj.NativeLibrary;
import org.bridj.NativeObject;
import org.bridj.NativeObjectInterface;
import org.bridj.Platform;
import org.bridj.Pointer;
import org.bridj.PointerIO;
import org.bridj.StructIO;
import org.bridj.StructObject;
import org.bridj.ann.Convention;
import org.bridj.ann.JNIBound;
import org.bridj.ann.Optional;
import org.bridj.demangling.Demangler;
import org.bridj.util.AnnotationUtils;
import org.bridj.util.ConcurrentCache;
import org.bridj.util.Utils;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class CRuntime
extends AbstractBridJRuntime {
    static final Set<Type> registeredTypes = new HashSet<Type>();
    final AtomicReference<CallbackNativeImplementer> _callbackNativeImplementer = new AtomicReference();
    static final int defaultObjectSize = Platform.is64Bits() ? 8 : 4;
    public static final String PROPERTY_bridj_c_defaultObjectSize = "bridj.c.defaultObjectSize";
    public Set<Class> rootCallbackClasses = new HashSet<Class>(Arrays.asList(Callback.class, DynamicFunction.class));

    @Deprecated
    public CRuntime() {
    }

    public CallbackNativeImplementer getCallbackNativeImplementer() {
        CallbackNativeImplementer callbackNativeImplementer = this._callbackNativeImplementer.get();
        if (callbackNativeImplementer == null && !this._callbackNativeImplementer.compareAndSet(null, callbackNativeImplementer = new CallbackNativeImplementer(BridJ.getOrphanEntities(), this))) {
            callbackNativeImplementer = this._callbackNativeImplementer.get();
        }
        return callbackNativeImplementer;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    public static CRuntime getInstance() {
        return BridJ.getRuntimeByRuntimeClass(CRuntime.class);
    }

    @Override
    public <T extends NativeObject> Class<? extends T> getActualInstanceClass(Pointer<T> pointer, Type type) {
        return Utils.getClass(type);
    }

    protected boolean shouldWarnIfNoFieldsInStruct() {
        return true;
    }

    @Override
    public <T extends NativeObject> BridJRuntime.TypeInfo<T> getTypeInfo(Type type) {
        return new CTypeInfo(type);
    }

    @Override
    public Type getType(Class<?> clazz, Object[] objectArray, int[] nArray) {
        return clazz;
    }

    @Override
    public void register(Type type) {
        this.register(type, null, null);
    }

    @Override
    public void unregister(Type clazz) {
        clazz = Utils.getClass(clazz);
        registeredTypes.remove(clazz);
    }

    /*
     * Could not resolve type clashes
     * Unable to fully structure code
     */
    synchronized void register(Type var1_1, NativeLibrary var2_2, MethodCallInfoBuilder var3_3) {
        block23: {
            var4_4 = Utils.getClass(var1_1);
            if (!BridJ.getRuntimeClass(var4_4).isInstance(this)) {
                BridJ.register(var4_4);
                return;
            }
            var5_5 = CRuntime.registeredTypes;
            synchronized (var5_5) {
                if (!CRuntime.registeredTypes.add(var4_4)) {
                    return;
                }
            }
            if (var3_3 == null) {
                var3_3 = new MethodCallInfoBuilder();
            }
            if (BridJ.verbose) {
                BridJ.info("Registering type " + Utils.toString(var1_1));
            }
            var5_6 = var4_4.getModifiers();
            var6_11 = null;
            try {
                var6_11 = var2_2 == null ? this.getNativeLibrary(var4_4) : var2_2;
            }
            catch (Throwable v0) {}
            var7_12 = new ConcurrentCache<K, NativeEntities.Builder>(NativeEntities.Builder.class);
            var8_13 = new HashSet<E>();
            if (!CallbackInterface.class.isAssignableFrom(var4_4)) ** GOTO lbl40
            if (!this.rootCallbackClasses.contains(var1_1)) break block23;
            for (Map.Entry<K, NativeEntities.Builder> var9_14 : var7_12.entrySet()) {
                ((NativeEntities)var9_14.getKey()).addDefinitions(var4_4, var9_14.getValue());
            }
            this.registerFamily(var1_1, var2_2, var3_3);
            return;
        }
        try {
            if (Modifier.isAbstract(var5_6)) {
                this.getCallbackNativeImplementer().getCallbackImplType((Class)var1_1, var2_2);
            }
lbl40:
            // 4 sources

            var5_8 = new ArrayList<E>();
            for (Object var12_22 : var4_4.getDeclaredMethods()) {
                var13_23 = var12_22.getModifiers();
                if (!Modifier.isNative(var13_23) || AnnotationUtils.isAnnotationPresent(JNIBound.class, (AnnotatedElement)var12_22, new Annotation[0])) continue;
                var5_8.add(var12_22);
            }
            if (!var5_8.isEmpty()) {
                try {
                    var9_15 = var5_8.iterator();
                    while (var9_15.hasNext()) {
                        var10_18 = (Method)var9_15.next();
                        if (!var8_13.add(var10_18)) continue;
                        try {
                            var11_20 = var2_2 == null ? BridJ.getNativeLibrary(var10_18) : var2_2;
                            var12_22 = var11_20 == null ? BridJ.getOrphanEntities() : var11_20.getNativeEntities();
                            var13_24 = (NativeEntities.Builder)var7_12.get(var12_22);
                            this.registerNativeMethod(var4_4, (NativeLibrary)var6_11, var10_18, var11_20, var13_24, var3_3);
                        }
                        catch (Exception var11_21) {
                            if (!BridJ.verbose) continue;
                            BridJ.error("Method " + var10_18.toGenericString() + " cannot be mapped : " + var11_21, var11_21);
                        }
                    }
                }
                catch (Exception var9_16) {
                    throw new RuntimeException("Failed to register class " + Utils.toString(var1_1), var9_16);
                }
            }
            return;
        }
        catch (Throwable var5_9) {
            throw var5_9;
        }
        finally {
            for (Object var5_8 : var7_12.entrySet()) {
                ((NativeEntities)var5_8.getKey()).addDefinitions(var4_4, (NativeEntities.Builder)var5_8.getValue());
            }
            this.registerFamily(var1_1, var2_2, var3_3);
        }
    }

    protected void registerFamily(Type clazz, NativeLibrary nativeLibrary, MethodCallInfoBuilder methodCallInfoBuilder) {
        clazz = Utils.getClass(clazz);
        for (Class<?> clazz2 : clazz.getClasses()) {
            this.register(clazz2, nativeLibrary, methodCallInfoBuilder);
        }
        if ((clazz = clazz.getSuperclass()) != null && clazz != Object.class) {
            this.register(clazz, nativeLibrary, methodCallInfoBuilder);
        }
    }

    public NativeLibrary getNativeLibrary(Class<?> clazz) throws IOException {
        return BridJ.getNativeLibrary(clazz);
    }

    protected boolean isSymbolOptional(Method method) {
        return AnnotationUtils.getInheritableAnnotation(Optional.class, method, new Annotation[0]) != null;
    }

    public void registerNativeMethod(Class<?> object, NativeLibrary object2, Method method, NativeLibrary object3, NativeEntities.Builder builder, MethodCallInfoBuilder methodCallInfoBuilder) throws FileNotFoundException {
        try {
            object2 = methodCallInfoBuilder.apply(method);
            if (object2 == null) {
                return;
            }
        }
        catch (Throwable throwable) {
            BridJ.error("Unable to register " + method + " : " + throwable);
            throwable.printStackTrace();
            return;
        }
        if (CallbackInterface.class.isAssignableFrom((Class<?>)object)) {
            if (BridJ.debug) {
                BridJ.info("Registering java -> native callback : " + method);
            }
            builder.addJavaToNativeCallback((MethodCallInfo)object2);
            return;
        }
        object = object3 == null ? null : object3.getSymbol(method);
        if (object == null) {
            if (!this.isSymbolOptional(method)) {
                BridJ.error("Failed to get address of method " + method);
            }
            return;
        }
        ((MethodCallInfo)object2).setForwardedPointer(((Demangler.Symbol)object).getAddress());
        if (!((MethodCallInfo)object2).hasCallingConvention() && (object3 = ((Demangler.Symbol)object).getInferredCallingConvention()) != null) {
            ((MethodCallInfo)object2).setCallingConvention((Convention.Style)((Object)object3));
        }
        builder.addFunction((MethodCallInfo)object2);
        if (BridJ.debug) {
            BridJ.info("Registering " + method + " as C function " + ((Demangler.Symbol)object).getName() + " (address = 0x" + Long.toHexString(((Demangler.Symbol)object).getAddress()) + ")");
        }
    }

    public <T extends NativeObject> Pointer<T> allocate(Class<T> clazz, int n2, Object ... objectArray) {
        if (CallbackInterface.class.isAssignableFrom(clazz)) {
            if (n2 != -1 || objectArray.length != 0) {
                throw new RuntimeException("Callback should have a constructorId == -1 and no constructor args !");
            }
            return null;
        }
        throw new RuntimeException("Cannot allocate instance of type " + clazz.getName() + " (unhandled NativeObject subclass)");
    }

    public int getDefaultStructSize() {
        String string = System.getProperty(PROPERTY_bridj_c_defaultObjectSize);
        if (string != null) {
            try {
                return Integer.parseInt(string);
            }
            catch (Throwable throwable) {
                BridJ.error("Invalid value for property bridj.c.defaultObjectSize : '" + string + "'");
            }
        }
        return defaultObjectSize;
    }

    public long sizeOf(Type type, StructIO structIO) {
        long l2;
        if (structIO == null) {
            structIO = StructIO.getInstance(Utils.getClass(type), type);
        }
        if (structIO == null || (l2 = structIO.desc.getStructSize()) <= 0L) {
            return this.getDefaultStructSize();
        }
        return l2;
    }

    public Method getUniqueCallbackMethod(Class clazz) {
        return this.getCallbackMethod(clazz, true);
    }

    public Method getFastestCallbackMethod(Class clazz) {
        return this.getCallbackMethod(clazz, false);
    }

    private Method getSingleAbstractMethodMethod(Class clazz) {
        assert (Modifier.isAbstract(clazz.getModifiers()));
        Method method = null;
        Method[] methodArray = clazz.getDeclaredMethods();
        Method[] methodArray2 = methodArray;
        methodArray2 = methodArray;
        int n2 = methodArray.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            Method method2 = methodArray2[i2];
            int n3 = method2.getModifiers();
            if (!Modifier.isAbstract(n3)) continue;
            if (method == null) {
                method = method2;
                continue;
            }
            throw new RuntimeException("Callback " + clazz.getName() + " has more than one abstract method (" + method2 + " and " + method + ")");
        }
        return method;
    }

    private boolean sameBindings(Method genericDeclaration, Method genericDeclaration2) {
        Class<?>[] classArray = genericDeclaration.getParameterTypes();
        Class<?>[] classArray2 = genericDeclaration2.getParameterTypes();
        if (!CRuntime.sameBindings((Class)(genericDeclaration = genericDeclaration.getReturnType()), (Class)(genericDeclaration2 = genericDeclaration2.getReturnType())) || classArray.length != classArray2.length) {
            return false;
        }
        for (int i2 = 0; i2 < classArray.length; ++i2) {
            genericDeclaration2 = classArray[i2];
            Class<?> clazz = classArray2[i2];
            if (CRuntime.sameBindings((Class)genericDeclaration2, clazz)) continue;
            return false;
        }
        return true;
    }

    private static int getSignatureObjectCount(Class clazz) {
        if (clazz.isPrimitive()) {
            return 0;
        }
        return 1;
    }

    private int getSignatureObjectCount(Method classArray) {
        int n2 = CRuntime.getSignatureObjectCount(classArray.getReturnType());
        for (Class<?> clazz : classArray.getParameterTypes()) {
            n2 += CRuntime.getSignatureObjectCount(clazz);
        }
        return n2;
    }

    public List<Method> getApplyMethods(Class methodArray) {
        ArrayList<Method> arrayList = new ArrayList<Method>();
        for (Method method : methodArray.getDeclaredMethods()) {
            if (!method.getName().equals("apply")) continue;
            arrayList.add(method);
        }
        return arrayList;
    }

    public Class<?> getAbstractCallback(Class clazz) {
        Class clazz2;
        while ((clazz2 = clazz.getSuperclass()) != null && !this.rootCallbackClasses.contains(clazz2)) {
            clazz = clazz2;
        }
        if (!Modifier.isAbstract(clazz.getModifiers())) {
            throw new RuntimeException("Callback definition " + clazz.getName() + " must be abstract.");
        }
        return clazz;
    }

    public Method getCallbackMethod(Class<?> object, boolean bl) {
        Method method;
        int n2;
        GenericDeclaration genericDeclaration = this.getAbstractCallback((Class)object);
        List<Method> list2 = this.getSingleAbstractMethodMethod((Class)genericDeclaration);
        if (list2 != null) {
            return list2;
        }
        list2 = this.getApplyMethods((Class)object);
        if (list2.isEmpty()) {
            throw new RuntimeException("Type doesn't have any abstract method nor any 'apply' method: " + ((Class)genericDeclaration).getName());
        }
        genericDeclaration = (Method)list2.get(0);
        int n3 = list2.size();
        for (n2 = 1; n2 < n3; ++n2) {
            method = (Method)list2.get(n2);
            if (this.sameBindings((Method)genericDeclaration, method)) continue;
            throw new RuntimeException("Callback apply methods don't match: " + genericDeclaration + " vs. " + method);
        }
        int n4 = n2 = list2.size() == 1 ? 1 : 0;
        if (bl && n2 == 0) {
            throw new RuntimeException("Expected only one overridden apply method in " + ((Class)object).getName() + ", but got " + list2);
        }
        if (n2 != 0) {
            return (Method)list2.get(0);
        }
        n3 = Integer.MAX_VALUE;
        method = null;
        for (Method method2 : list2) {
            int n5 = this.getSignatureObjectCount(method2);
            if (n5 >= n3) continue;
            n3 = n5;
            method = method2;
        }
        return method;
    }

    private static boolean sameBindings(Class clazz, Class clazz2) {
        return clazz.equals(clazz2) || clazz == Long.TYPE && Pointer.class.isAssignableFrom(clazz2) || clazz2 == Long.TYPE && Pointer.class.isAssignableFrom(clazz) || clazz == Integer.TYPE && IntValuedEnum.class.isAssignableFrom(clazz2) || clazz2 == Integer.TYPE && IntValuedEnum.class.isAssignableFrom(clazz);
    }

    public <T extends NativeObject> Class<? extends T> getTypeForCast(Type clazz) {
        if (CallbackInterface.class.isAssignableFrom(clazz = Utils.getClass(clazz))) {
            return this.getCallbackNativeImplementer().getCallbackImplType(clazz, null);
        }
        return clazz;
    }

    public DynamicFunctionFactory getDynamicFunctionFactory(NativeLibrary nativeLibrary, Convention.Style style, Type type, Type ... typeArray) {
        return this.getCallbackNativeImplementer().getDynamicCallback(nativeLibrary, style, type, typeArray);
    }

    public static <T> Pointer<T> createCToJavaCallback(MethodCallInfo methodCallInfo, Type type) {
        methodCallInfo.prependCallbackCC();
        final long l2 = JNI.createCToJavaCallback(methodCallInfo);
        long l3 = JNI.getActualCToJavaCallback(l2);
        if (BridJ.debugPointers) {
            new RuntimeException().fillInStackTrace();
        }
        return Pointer.pointerToAddress(l3, type, new Pointer.Releaser(){

            @Override
            public final void release(Pointer<?> pointer) {
                if (BridJ.debugPointers) {
                    BridJ.info("Freeing callback pointer " + pointer + "\n(Creation trace = \n\t" + Utils.toString(pointer.creationTrace).replaceAll("\n", "\n\t") + "\n)", new RuntimeException().fillInStackTrace());
                }
                if (BridJ.debugNeverFree) {
                    return;
                }
                JNI.freeCToJavaCallback(l2);
            }
        });
    }

    public <T extends CallbackInterface> Pointer<T> registerCallbackInstance(T t2) {
        try {
            Class<?> clazz = t2.getClass();
            MethodCallInfo methodCallInfo = new MethodCallInfo(this.getFastestCallbackMethod(clazz));
            methodCallInfo.setDeclaringClass(clazz);
            methodCallInfo.setJavaCallback(t2);
            return CRuntime.createCToJavaCallback(methodCallInfo, clazz);
        }
        catch (Exception exception) {
            throw new RuntimeException("Failed to register callback instance of type " + t2.getClass().getName(), exception);
        }
    }

    public void setNativeObjectPeer(NativeObjectInterface nativeObjectInterface, Pointer<? extends NativeObjectInterface> pointer) {
        ((NativeObject)nativeObjectInterface).peer = pointer;
    }

    public static class MethodCallInfoBuilder {
        public MethodCallInfo apply(Method method) throws FileNotFoundException {
            return new MethodCallInfo(method);
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public class CTypeInfo<T extends NativeObject>
    implements BridJRuntime.TypeInfo<T> {
        public final Type type;
        public final Class<T> typeClass;
        protected final StructIO structIO;
        public final PointerIO<T> pointerIO;
        protected volatile Class<T> castClass;

        public CTypeInfo(Type type) {
            this.type = type;
            this.typeClass = Utils.getClass(type);
            this.structIO = StructIO.getInstance(this.typeClass, type);
            if (this.structIO != null) {
                this.structIO.desc.build();
                if (BridJ.verbose && this.structIO.desc.getAggregatedFields().isEmpty() && CRuntime.this.shouldWarnIfNoFieldsInStruct()) {
                    BridJ.info("No fields found in " + Utils.toString(type) + " (maybe they weren't declared as public ?)");
                }
            }
            this.pointerIO = PointerIO.getInstance(this.structIO);
            CRuntime.this.register(this.typeClass);
        }

        @Override
        public long sizeOf() {
            return this.structIO.desc.getStructSize();
        }

        @Override
        public boolean equal(T t2, T t3) {
            if (this.structIO != null) {
                if (((StructObject)t2).io != this.structIO) {
                    throw new IllegalArgumentException("This is not this instance's StructIO");
                }
                if (((StructObject)t3).io != this.structIO) {
                    return false;
                }
                return this.structIO.equal((StructObject)t2, (StructObject)t3);
            }
            return ((NativeObject)t2).peer.equals(((NativeObject)t3).peer);
        }

        @Override
        public int compare(T t2, T t3) {
            if (this.structIO != null) {
                if (((StructObject)t2).io != this.structIO) {
                    throw new IllegalArgumentException("This is not this instance's StructIO");
                }
                if (((StructObject)t3).io != this.structIO) {
                    return 1;
                }
                return this.structIO.compare((StructObject)t2, (StructObject)t3);
            }
            return ((NativeObject)t2).peer.compareTo(((NativeObject)t3).peer);
        }

        @Override
        public BridJRuntime getRuntime() {
            return CRuntime.this;
        }

        @Override
        public Type getType() {
            return this.type;
        }

        protected Class<T> getCastClass() {
            if (this.castClass == null) {
                this.castClass = CRuntime.this.getTypeForCast(this.typeClass);
            }
            return this.castClass;
        }

        public T newCastInstance() throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            GenericDeclaration genericDeclaration = this.getCastClass();
            try {
                return (T)((NativeObject)((Class)genericDeclaration).newInstance());
            }
            catch (IllegalAccessException illegalAccessException) {
                genericDeclaration = ((Class)genericDeclaration).getConstructor(new Class[0]);
                ((Constructor)genericDeclaration).setAccessible(true);
                return (T)((NativeObject)((Constructor)genericDeclaration).newInstance(new Object[0]));
            }
        }

        @Override
        public T cast(Pointer pointer) {
            try {
                T t2 = this.newCastInstance();
                this.initialize(t2, pointer);
                return t2;
            }
            catch (Exception exception) {
                throw new RuntimeException("Failed to cast pointer " + pointer + " to instance of type " + Utils.toString(this.type), exception);
            }
        }

        @Override
        public T createReturnInstance() {
            try {
                T t2 = this.newCastInstance();
                this.initialize(t2);
                return t2;
            }
            catch (Exception exception) {
                throw new RuntimeException("Failed to create return instance for type " + Utils.toString(this.type), exception);
            }
        }

        @Override
        public void writeToNative(T t2) {
            if (t2 instanceof StructObject) {
                this.structIO.writeFieldsToNative((StructObject)t2);
            }
        }

        @Override
        public void readFromNative(T t2) {
            if (t2 instanceof StructObject) {
                this.structIO.readFieldsFromNative((StructObject)t2);
            }
        }

        @Override
        public void copyNativeObjectToAddress(T t2, Pointer<T> pointer) {
            if (t2 instanceof StructObject) {
                ((StructObject)t2).peer.copyBytesTo(pointer, this.structIO.desc.getStructSize());
            }
        }

        @Override
        public String describe(T t2) {
            if (t2 instanceof StructObject) {
                return this.structIO.describe((StructObject)t2);
            }
            return t2.toString();
        }

        @Override
        public String describe() {
            if (this.structIO != null) {
                return this.structIO.desc.describe();
            }
            return Utils.toString(this.typeClass);
        }

        @Override
        public void initialize(T t2) {
            if (!BridJ.isCastingNativeObjectInCurrentThread()) {
                if (t2 instanceof CallbackInterface) {
                    if (!(t2 instanceof DynamicFunction)) {
                        CRuntime.this.setNativeObjectPeer((NativeObjectInterface)t2, (Pointer<? extends NativeObjectInterface>)CRuntime.this.registerCallbackInstance((CallbackInterface)t2));
                    }
                } else {
                    this.initialize(t2, -1, new Object[0]);
                }
                if (t2 instanceof StructObject) {
                    this.structIO.readFieldsFromNative((StructObject)t2);
                    return;
                }
            } else if (t2 instanceof StructObject) {
                ((StructObject)t2).io = this.structIO;
            }
        }

        @Override
        public void initialize(T t2, Pointer pointer) {
            ((NativeObject)t2).peer = pointer;
            if (t2 instanceof StructObject) {
                ((StructObject)t2).io = this.structIO;
                this.structIO.readFieldsFromNative((StructObject)t2);
            }
        }

        protected <V> Pointer<V> allocateStructMemory(PointerIO<V> pointerIO) {
            return Pointer.allocate(pointerIO);
        }

        @Override
        public void initialize(T t2, int n2, Object ... object) {
            object = (StructObject)t2;
            if (n2 < 0) {
                object.io = this.structIO;
                if (((NativeObject)t2).peer == null) {
                    CTypeInfo cTypeInfo = this;
                    ((NativeObject)t2).peer = cTypeInfo.allocateStructMemory(cTypeInfo.pointerIO);
                    return;
                }
            } else {
                throw new UnsupportedOperationException("TODO implement structs constructors !");
            }
        }

        @Override
        public T clone(T t2) throws CloneNotSupportedException {
            if (t2 == null) {
                return null;
            }
            try {
                NativeObject nativeObject = (NativeObject)this.typeClass.newInstance();
                CTypeInfo cTypeInfo = this;
                Pointer<T> pointer = cTypeInfo.allocateStructMemory(cTypeInfo.pointerIO);
                Pointer.getPointer(t2).copyTo(pointer);
                this.initialize(nativeObject, pointer);
                return (T)nativeObject;
            }
            catch (Exception exception) {
                throw new RuntimeException("Failed to clone instance of type " + this.getType());
            }
        }

        @Override
        public void destroy(T t2) {
            if (t2 instanceof CallbackInterface) {
                return;
            }
        }
    }
}


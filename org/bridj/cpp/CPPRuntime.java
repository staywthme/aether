/*
 * Decompiled with CFR 0.152.
 */
package org.bridj.cpp;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.lang.constant.Constable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.bridj.BridJ;
import org.bridj.BridJRuntime;
import org.bridj.CRuntime;
import org.bridj.Callback;
import org.bridj.DynamicFunction;
import org.bridj.DynamicFunctionFactory;
import org.bridj.MethodCallInfo;
import org.bridj.NativeEntities;
import org.bridj.NativeLibrary;
import org.bridj.NativeObject;
import org.bridj.Platform;
import org.bridj.Pointer;
import org.bridj.PointerIO;
import org.bridj.SizeT;
import org.bridj.ann.Convention;
import org.bridj.ann.Template;
import org.bridj.ann.Virtual;
import org.bridj.cpp.CPPObject;
import org.bridj.cpp.CPPType;
import org.bridj.demangling.Demangler;
import org.bridj.util.Pair;
import org.bridj.util.Utils;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class CPPRuntime
extends CRuntime {
    public static final int DEFAULT_CONSTRUCTOR = -1;
    public static final int SKIP_CONSTRUCTOR = -2;
    Map<Class<?>, Integer> virtualMethodsCounts = new HashMap();
    volatile MemoryOperators memoryOperators;
    Set<Type> typesThatDontNeedASyntheticVirtualTable = new HashSet<Type>();
    Map<Type, VTable> syntheticVirtualTables = new HashMap<Type, VTable>();
    Map<Pair<Type, Integer>, DynamicFunction> constructors = new HashMap<Pair<Type, Integer>, DynamicFunction>();
    Map<Type, CPPDestructor> destructors = new HashMap<Type, CPPDestructor>();
    Map<Type, Long> vtables = new HashMap<Type, Long>();

    public static CPPRuntime getInstance() {
        return BridJ.getRuntimeByRuntimeClass(CPPRuntime.class);
    }

    @Override
    public Type getType(NativeObject nativeObject) {
        if (!(nativeObject instanceof CPPObject)) {
            return super.getType(nativeObject);
        }
        Class<?> clazz = nativeObject.getClass();
        return new CPPType(clazz, CPPRuntime.getTemplateParameters((CPPObject)nativeObject, clazz));
    }

    public static Object[] getTemplateParameters(CPPObject cPPObject, Class<?> clazz) {
        CPPObject cPPObject2 = cPPObject;
        synchronized (cPPObject2) {
            Object[] objectArray = null;
            if (cPPObject.templateParameters != null) {
                objectArray = cPPObject.templateParameters.get(clazz);
            }
            return objectArray;
        }
    }

    public static Type[] getTemplateTypeParameters(CPPObject object, Type type) {
        if (!(type instanceof ParameterizedType)) {
            return new Type[0];
        }
        Type[] typeArray = Utils.getClass(type);
        type = (ParameterizedType)type;
        object = CPPRuntime.getTemplateParameters((CPPObject)object, typeArray);
        Template template = typeArray.getAnnotation(Template.class);
        if (template == null) {
            throw new RuntimeException("No " + Template.class.getName() + " annotation on class " + typeArray.getName());
        }
        if (template.paramNames().length != ((Object[])object).length) {
            throw new RuntimeException(Template.class.getName() + " annotation's paramNames on class " + typeArray.getName() + " (" + Arrays.asList(template.paramNames()) + " does not match count of actual template params " + Arrays.asList(object));
        }
        if (template.paramNames().length != template.value().length) {
            throw new RuntimeException(Template.class.getName() + " annotation's paramNames and value lengths on class " + typeArray.getName() + " don't match");
        }
        int n2 = type.getActualTypeArguments().length;
        typeArray = new Type[n2];
        int n3 = 0;
        int cfr_ignored_0 = ((Object[])object).length;
        for (int i2 = 0; i2 < n2; ++i2) {
            object = template.value()[i2];
            if (!Type.class.isInstance(object)) continue;
            typeArray[n3++] = (Type)object;
        }
        assert (n3 == n2);
        return typeArray;
    }

    public void setTemplateParameters(CPPObject cPPObject, Class<?> clazz, Object[] objectArray) {
        CPPObject cPPObject2 = cPPObject;
        synchronized (cPPObject2) {
            if (cPPObject.templateParameters == null) {
                cPPObject.templateParameters = Collections.singletonMap(clazz, objectArray);
            } else {
                try {
                    cPPObject.templateParameters.put(clazz, objectArray);
                }
                catch (Throwable throwable) {
                    cPPObject.templateParameters = new HashMap(cPPObject.templateParameters);
                    cPPObject.templateParameters.put(clazz, objectArray);
                }
            }
            return;
        }
    }

    protected static int getAnnotatedTemplateTypeVariableIndexInArguments(TypeVariable<?> typeVariable) {
        Object obj;
        Object obj2 = obj = typeVariable.getGenericDeclaration();
        Class<?>[] classArray = obj.getAnnotation(Template.class);
        if (classArray == null) {
            throw new RuntimeException(obj2 + " is not a C++ class template (misses the @" + Template.class.getName() + " annotation)");
        }
        int n2 = Arrays.asList(obj.getTypeParameters()).indexOf(typeVariable);
        int n3 = 0;
        int n4 = -1;
        classArray = classArray.value();
        int n5 = classArray.length;
        for (int i2 = 0; i2 < n5; ++i2) {
            Class<?> clazz = classArray[i2];
            if (clazz == Class.class || clazz == Type.class) {
                ++n3;
            }
            if (n3 != n2) continue;
            n4 = i2;
            break;
        }
        if (n4 < 0) {
            throw new RuntimeException("Couldn't find the type variable " + typeVariable + " (offset " + n2 + ") in the @" + Template.class.getName() + " annotation : " + Arrays.asList(classArray));
        }
        return n4;
    }

    protected ClassTypeVariableExtractor createClassTypeVariableExtractor(TypeVariable<Class<?>> typeVariable) {
        final Class<?> clazz = typeVariable.getGenericDeclaration();
        final int n2 = CPPRuntime.getAnnotatedTemplateTypeVariableIndexInArguments(typeVariable);
        return new ClassTypeVariableExtractor(){

            public Type extract(CPPObject cPPObject) {
                clazz.cast(cPPObject);
                Object[] objectArray = CPPRuntime.getTemplateParameters(cPPObject, clazz);
                if (objectArray == null) {
                    throw new RuntimeException("No type parameters found in this instance : " + cPPObject);
                }
                return (Type)objectArray[n2];
            }
        };
    }

    protected MethodTypeVariableExtractor createMethodTypeVariableExtractor(TypeVariable<?> typeVariable) {
        Object object = typeVariable.getGenericDeclaration();
        if (object instanceof Class) {
            Class cfr_ignored_0 = (Class)object;
            object = this.createClassTypeVariableExtractor(typeVariable);
            return new MethodTypeVariableExtractor((ClassTypeVariableExtractor)object){
                final /* synthetic */ ClassTypeVariableExtractor val$ce;
                {
                    this.val$ce = classTypeVariableExtractor;
                }

                public Type extract(CPPObject cPPObject, Object[] objectArray) {
                    return this.val$ce.extract(cPPObject);
                }
            };
        }
        object = (Method)object;
        object = ((Method)object).getDeclaringClass();
        int n2 = CPPRuntime.getAnnotatedTemplateTypeVariableIndexInArguments(typeVariable);
        return new MethodTypeVariableExtractor((Class)object, n2){
            final /* synthetic */ Class val$typeClass;
            final /* synthetic */ int val$iTypeInParams;
            {
                this.val$typeClass = clazz;
                this.val$iTypeInParams = n2;
            }

            public Type extract(CPPObject cPPObject, Object[] objectArray) {
                this.val$typeClass.cast(cPPObject);
                return (Type)objectArray[this.val$iTypeInParams];
            }
        };
    }

    @Override
    public <T extends NativeObject> Class<? extends T> getActualInstanceClass(Pointer<T> pointer, Type type) {
        return Utils.getClass(type);
    }

    public int getVirtualMethodsCount(Class<?> clazz) {
        Serializable serializable = this.virtualMethodsCounts.get(clazz);
        if (serializable == null) {
            serializable = new ArrayList();
            this.listVirtualMethods(clazz, (List<VirtMeth>)((Object)serializable));
            serializable = Integer.valueOf(serializable.size());
            this.virtualMethodsCounts.put(clazz, (Integer)serializable);
        }
        return (Integer)serializable;
    }

    protected void listVirtualMethods(Class<?> clazz, List<VirtMeth> list2) {
        if (!CPPObject.class.isAssignableFrom(clazz)) {
            return;
        }
        Class<?> clazz2 = clazz.getSuperclass();
        if (clazz2 != CPPObject.class) {
            this.listVirtualMethods(clazz2, list2);
        }
        int n2 = list2.size();
        TreeMap<Integer, VirtMeth> treeMap = new TreeMap<Integer, VirtMeth>();
        block0: for (Method method : clazz.getDeclaredMethods()) {
            VirtMeth virtMeth;
            Object object = Demangler.getMethodName(method);
            Type[] typeArray = method.getGenericParameterTypes();
            for (int i2 = 0; i2 < n2; ++i2) {
                virtMeth = list2.get(i2);
                Method method2 = virtMeth.definition;
                if (method2.getDeclaringClass() == clazz || !Demangler.getMethodName(method2).equals(object) || !CPPRuntime.isOverridenSignature(method2.getGenericParameterTypes(), typeArray, 0)) continue;
                object = new VirtMeth();
                new VirtMeth().definition = virtMeth.definition;
                ((VirtMeth)object).implementation = method;
                list2.set(i2, (VirtMeth)object);
                continue block0;
            }
            Virtual virtual = method.getAnnotation(Virtual.class);
            if (virtual == null) continue;
            virtMeth = new VirtMeth();
            new VirtMeth().definition = virtMeth.implementation = method;
            treeMap.put(virtual.value(), virtMeth);
        }
        list2.addAll(treeMap.values());
    }

    @Override
    public void registerNativeMethod(Class<?> clazz, NativeLibrary nativeLibrary, Method method, NativeLibrary object, NativeEntities.Builder builder, CRuntime.MethodCallInfoBuilder object2) throws FileNotFoundException {
        block16: {
            int n2;
            int n3;
            Virtual virtual;
            boolean bl;
            int n4;
            block13: {
                block15: {
                    block14: {
                        n4 = method.getModifiers();
                        bl = CPPObject.class.isAssignableFrom(method.getDeclaringClass());
                        if (!bl) {
                            super.registerNativeMethod(clazz, nativeLibrary, method, (NativeLibrary)object, builder, (CRuntime.MethodCallInfoBuilder)object2);
                            return;
                        }
                        object2 = ((CRuntime.MethodCallInfoBuilder)object2).apply(method);
                        virtual = method.getAnnotation(Virtual.class);
                        if (virtual != null) break block13;
                        ((MethodCallInfo)object2).setForwardedPointer((object = ((NativeLibrary)object).getSymbol(method)) == null ? 0L : ((Demangler.Symbol)object).getAddress());
                        if (((MethodCallInfo)object2).getForwardedPointer() == 0L) {
                            assert (BridJ.error("Method " + method.toGenericString() + " is not virtual but its address could not be resolved in the library."));
                            return;
                        }
                        if (!Modifier.isStatic(n4)) break block14;
                        builder.addFunction((MethodCallInfo)object2);
                        if (!BridJ.debug) break block15;
                        BridJ.info("Registering " + method + " as function or static C++ method " + ((Demangler.Symbol)object).getName());
                        break block16;
                    }
                    builder.addFunction((MethodCallInfo)object2);
                    if (BridJ.debug) {
                        BridJ.info("Registering " + method + " as C++ method " + ((Demangler.Symbol)object).getName());
                    }
                }
                return;
            }
            if (Modifier.isStatic(n4)) {
                BridJ.warning("Method " + method.toGenericString() + " is native and maps to a function, but is not static.");
            }
            n3 = (n3 = virtual.value()) < 0 ? -1 : this.getAbsoluteVirtualIndex(method, n3, clazz);
            Pointer<Pointer<?>> pointer = bl && nativeLibrary != null ? Pointer.pointerToAddress(this.getVirtualTable(clazz, nativeLibrary), Pointer.class) : null;
            if (pointer == null) {
                if (n3 < 0) {
                    BridJ.error("Method " + method.toGenericString() + " is virtual but the virtual table of class " + clazz.getName() + " was not found and the virtual method index is not provided in its @Virtual annotation.");
                    return;
                }
                n2 = n3;
            } else {
                n2 = this.getPositionInVirtualTable(pointer, method, nativeLibrary);
                if (n2 < 0) {
                    if (n3 < 0) {
                        BridJ.error("Method " + method.toGenericString() + " is virtual but its position could not be found in the virtual table.");
                        return;
                    }
                    n2 = n3;
                } else if (n3 >= 0 && n2 != n3) {
                    BridJ.warning("Method " + method.toGenericString() + " has @Virtual annotation indicating virtual index " + n3 + ", but analysis of the actual virtual table rather indicates it has index " + n2 + " (using the guess)");
                }
            }
            ((MethodCallInfo)object2).setVirtualIndex(n2);
            if (BridJ.debug) {
                BridJ.info("Registering " + method.toGenericString() + " as virtual C++ method with absolute virtual table index = " + n2);
            }
            builder.addVirtualMethod((MethodCallInfo)object2);
        }
    }

    int getAbsoluteVirtualIndex(Method method, int n2, Class<?> clazz) {
        clazz = clazz.getSuperclass();
        int n3 = this.getVirtualMethodsCount(clazz);
        boolean bl = true;
        if (clazz != null) {
            try {
                clazz.getMethod(Demangler.getMethodName(method), method.getParameterTypes());
                bl = false;
            }
            catch (NoSuchMethodException noSuchMethodException) {}
        }
        int n4 = bl ? n3 + n2 : n2;
        return n4;
    }

    public synchronized MemoryOperators getMemoryOperators() {
        if (this.memoryOperators == null) {
            try {
                NativeLibrary nativeLibrary = BridJ.getNativeLibrary("stdc++");
                this.memoryOperators = new MemoryOperators(nativeLibrary);
            }
            catch (Exception exception) {
                BridJ.error(null, exception);
            }
        }
        return this.memoryOperators;
    }

    int getPositionInVirtualTable(Method method, NativeLibrary nativeLibrary) {
        Object object = method.getDeclaringClass();
        object = Pointer.pointerToAddress(this.getVirtualTable((Type)object, nativeLibrary), Pointer.class);
        return this.getPositionInVirtualTable((Pointer<Pointer<?>>)object, method, nativeLibrary);
    }

    public int getPositionInVirtualTable(Pointer<Pointer<?>> pointer, Method method, NativeLibrary nativeLibrary) {
        String string = Demangler.getClassName(method.getDeclaringClass());
        int n2 = 0;
        while (true) {
            Object object;
            String string2;
            if ((string2 = (object = pointer.get(n2 + 0)) == null ? null : nativeLibrary.getSymbolName(((Pointer)object).getPeer())) == null && BridJ.debug) {
                BridJ.info("\tVtable(" + string + ")[" + n2 + "] = null");
            }
            if (object == null) {
                return -1;
            }
            if (string2 != null) {
                try {
                    object = nativeLibrary.parseSymbol(string2);
                    if (BridJ.debug) {
                        BridJ.info("\tVtable(" + string + ")[" + n2 + "] = " + string2 + " = " + object);
                    }
                    if (object != null && ((Demangler.MemberRef)object).matchesSignature(method)) {
                        return n2;
                    }
                    if (nativeLibrary.isMSVC() && !((Demangler.MemberRef)object).matchesEnclosingType(method)) {
                        break;
                    }
                }
                catch (Demangler.DemanglingException demanglingException) {
                    BridJ.warning("Failed to demangle '" + string2 + "' during inspection of virtual table for '" + method.toGenericString() + "' : " + demanglingException);
                }
            }
            ++n2;
        }
        return -1;
    }

    static int getDefaultDyncallCppConvention() {
        int n2 = 0;
        if (!Platform.is64Bits() && Platform.isWindows()) {
            n2 = 5;
        }
        return n2;
    }

    private String ptrToString(Pointer<?> pointer, NativeLibrary nativeLibrary) {
        if (pointer == null) {
            return "null";
        }
        return Long.toHexString(pointer.getPeer()) + " (" + nativeLibrary.getSymbolName(pointer.getPeer()) + ")";
    }

    protected boolean installRegularVTablePtr(Type type, NativeLibrary nativeLibrary, Pointer<?> pointer) {
        long l2 = this.getVirtualTable(type, nativeLibrary);
        if (l2 != 0L) {
            if (BridJ.debug) {
                BridJ.info("Installing regular vtable pointer " + Pointer.pointerToAddress(l2) + " to instance at " + pointer + " (type = " + Utils.toString(type) + ")");
            }
            pointer.setSizeT(l2);
            return true;
        }
        return false;
    }

    /*
     * WARNING - void declaration
     */
    protected boolean installSyntheticVTablePtr(Type type, NativeLibrary nativeLibrary, Pointer<?> pointer) {
        Map<Type, VTable> map = this.syntheticVirtualTables;
        synchronized (map) {
            VTable vTable = this.syntheticVirtualTables.get(type);
            if (vTable == null && !this.typesThatDontNeedASyntheticVirtualTable.contains(type)) {
                ArrayList<VirtMeth> arrayList = new ArrayList<VirtMeth>();
                this.listVirtualMethods(Utils.getClass(type), arrayList);
                boolean bl = false;
                for (VirtMeth object : arrayList) {
                    if (Modifier.isNative(object.implementation.getModifiers())) continue;
                    bl = true;
                    break;
                }
                if (bl) {
                    void var9_13;
                    Type type2 = Utils.getParent(type);
                    Object var9_11 = null;
                    if (CPPObject.class.isAssignableFrom(Utils.getClass(type2))) {
                        Pointer<Pointer> pointer2 = pointer.getPointer(Pointer.class);
                        if (BridJ.debug) {
                            BridJ.info("Found parent virtual table pointer = " + this.ptrToString(pointer2, nativeLibrary));
                        }
                    }
                    vTable = this.synthetizeVirtualTable(type, (Pointer<Pointer>)var9_13, arrayList, nativeLibrary);
                    this.syntheticVirtualTables.put(type, vTable);
                } else {
                    this.typesThatDontNeedASyntheticVirtualTable.add(type);
                }
            }
            if (vTable != null) {
                if (BridJ.debug) {
                    BridJ.info("Installing synthetic vtable pointer " + vTable.ptr + " to instance at " + pointer + " (type = " + Utils.toString(type) + ", " + vTable.callbacks.size() + " callbacks)");
                }
                pointer.setPointer(vTable.ptr);
                return vTable.ptr != null;
            }
            return false;
        }
    }

    protected VTable synthetizeVirtualTable(Type type, Pointer<Pointer> pointer, List<VirtMeth> list2, NativeLibrary nativeLibrary) {
        int n2 = list2.size();
        VTable vTable = new VTable();
        new VTable().ptr = Pointer.allocatePointers(n2 + 2).next(2L);
        Class clazz = Utils.getClass(type);
        for (int i2 = 0; i2 < n2; ++i2) {
            Pointer pointer2;
            VirtMeth virtMeth = list2.get(i2);
            if (Modifier.isNative(virtMeth.implementation.getModifiers())) {
                pointer2 = pointer == null ? null : pointer.get(i2);
            } else {
                try {
                    pointer2 = new MethodCallInfo(virtMeth.implementation, virtMeth.definition);
                    ((MethodCallInfo)((Object)pointer2)).setDeclaringClass(virtMeth.implementation.getDeclaringClass());
                    pointer2 = CPPRuntime.createCToJavaCallback((MethodCallInfo)((Object)pointer2), clazz);
                    vTable.callbacks.put(virtMeth.implementation, pointer2);
                }
                catch (Throwable throwable) {
                    BridJ.error("Failed to register overridden method " + virtMeth.implementation + " for type " + type + " (original method = " + virtMeth.definition + ")", throwable);
                    pointer2 = null;
                }
            }
            vTable.ptr.set(i2, pointer2);
        }
        return vTable;
    }

    static int getTemplateParametersCount(Class<?> object) {
        int n2 = (object = ((Class)object).getAnnotation(Template.class)) == null ? 0 : object.value().length;
        return n2;
    }

    DynamicFunction getConstructor(Class<?> clazz, Type type, NativeLibrary nativeLibrary, int n2) {
        Pair<Type, Integer> pair = new Pair<Type, Integer>(type, n2);
        Object object = this.constructors.get(pair);
        if (object == null) {
            try {
                try {
                    object = this.findConstructor(clazz, n2, true);
                    if (BridJ.debug) {
                        BridJ.info("Found constructor for " + Utils.toString(type) + " : " + object);
                    }
                }
                catch (NoSuchMethodException noSuchMethodException) {
                    if (BridJ.debug) {
                        BridJ.info("No constructor for " + Utils.toString(type));
                    }
                    return null;
                }
                Demangler.Symbol symbol = nativeLibrary == null ? null : nativeLibrary.getFirstMatchingSymbol(new NativeLibrary.SymbolAccepter((Constructor)object, type){
                    final /* synthetic */ Constructor val$constr;
                    final /* synthetic */ Type val$type;
                    {
                        this.val$constr = constructor;
                        this.val$type = type;
                    }

                    public boolean accept(Demangler.Symbol symbol) {
                        return symbol.matchesConstructor(this.val$constr.getDeclaringClass() == Utils.getClass(this.val$type) ? this.val$type : this.val$constr.getDeclaringClass(), this.val$constr);
                    }
                });
                if (symbol == null) {
                    if (n2 >= 0) {
                        throw new RuntimeException("No matching constructor for " + Utils.toString(type) + " (" + object + ")");
                    }
                    if (BridJ.debug) {
                        BridJ.info("No matching constructor for " + Utils.toString(type) + " (" + object + ")");
                    }
                    return null;
                }
                if (BridJ.debug) {
                    BridJ.info("Registering constructor " + object + " as " + symbol.getName());
                }
                int n3 = CPPRuntime.getTemplateParametersCount(clazz);
                object = object.getParameterTypes();
                Type[] typeArray = new Class[((Class<?>[])object).length + 1 - n3];
                Type[] typeArray2 = typeArray;
                typeArray[0] = Pointer.class;
                System.arraycopy(object, n3, typeArray2, 1, ((Class<?>[])object).length - n3);
                DynamicFunctionFactory dynamicFunctionFactory = this.getDynamicFunctionFactory(nativeLibrary, Convention.Style.ThisCall, Void.TYPE, typeArray2);
                object = dynamicFunctionFactory.newInstance(Pointer.pointerToAddress(symbol.getAddress()));
                this.constructors.put(pair, (DynamicFunction)object);
            }
            catch (Throwable throwable) {
                object = throwable;
                throwable.printStackTrace();
                throw new RuntimeException("Unable to create constructor " + n2 + " for " + type + " : " + object, (Throwable)object);
            }
        }
        return object;
    }

    CPPDestructor getDestructor(Class<?> object, Type type, NativeLibrary nativeLibrary) {
        CPPDestructor cPPDestructor = this.destructors.get(type);
        if (cPPDestructor == null) {
            object = nativeLibrary.getFirstMatchingSymbol(new NativeLibrary.SymbolAccepter((Class)object){
                final /* synthetic */ Class val$typeClass;
                {
                    this.val$typeClass = clazz;
                }

                public boolean accept(Demangler.Symbol symbol) {
                    return symbol.matchesDestructor(this.val$typeClass);
                }
            });
            if (BridJ.debug && object != null) {
                BridJ.info("Registering destructor of " + Utils.toString(type) + " as " + ((Demangler.Symbol)object).getName());
            }
            if (object != null) {
                cPPDestructor = Pointer.pointerToAddress(((Demangler.Symbol)object).getAddress(), CPPDestructor.class).get();
                this.destructors.put(type, cPPDestructor);
            }
        }
        return cPPDestructor;
    }

    Pointer.Releaser newCPPReleaser(Type type) {
        try {
            Class clazz = Utils.getClass(type);
            NativeLibrary nativeLibrary = BridJ.getNativeLibrary(clazz);
            return this.newCPPReleaser(type, clazz, nativeLibrary);
        }
        catch (Throwable throwable) {
            throw new RuntimeException("Failed to create a C++ destructor for type " + Utils.toString(type) + " : " + throwable, throwable);
        }
    }

    Pointer.Releaser newCPPReleaser(final Type type, Class<?> object, NativeLibrary nativeLibrary) throws FileNotFoundException {
        Pointer.Releaser releaser = null;
        if (nativeLibrary != null && BridJ.enableDestructors && (object = this.getDestructor((Class<?>)object, type, nativeLibrary)) != null) {
            releaser = new Pointer.Releaser((CPPDestructor)object){
                final /* synthetic */ CPPDestructor val$destructor;
                {
                    this.val$destructor = cPPDestructor;
                }

                @Override
                public void release(Pointer<?> pointer) {
                    if (BridJ.debug) {
                        BridJ.info("Destructing instance of C++ type " + Utils.toString(type) + " (address = " + pointer + ", destructor = " + Pointer.getPointer(this.val$destructor) + ")");
                    }
                    long l2 = pointer.getPeer();
                    this.val$destructor.destroy(l2);
                    BridJ.setJavaObjectFromNativePeer(l2, null);
                }
            };
        }
        return releaser;
    }

    protected <T extends CPPObject> Pointer<T> newCPPInstance(T t2, Type type, int n2, Object ... objectArray) {
        Pointer pointer = null;
        try {
            DynamicFunction dynamicFunction;
            Class clazz = Utils.getClass(type);
            NativeLibrary nativeLibrary = BridJ.getNativeLibrary(clazz);
            if (BridJ.debug) {
                BridJ.info("Creating C++ instance of type " + type + " with args " + Arrays.asList(objectArray));
            }
            Object[] objectArray2 = this.newCPPReleaser(type, clazz, nativeLibrary);
            long l2 = this.sizeOf(type, null);
            pointer = Pointer.allocateBytes(PointerIO.getInstance(type), l2, (Pointer.Releaser)objectArray2).as(type);
            DynamicFunction dynamicFunction2 = dynamicFunction = n2 == -2 ? null : this.getConstructor(clazz, type, nativeLibrary, n2);
            if (nativeLibrary != null && CPPObject.class.isAssignableFrom(clazz)) {
                this.installRegularVTablePtr(type, nativeLibrary, pointer);
            }
            if (dynamicFunction != null) {
                Object[] objectArray3 = new Object[1 + objectArray.length];
                objectArray2 = objectArray3;
                objectArray3[0] = pointer;
                System.arraycopy(objectArray, 0, objectArray2, 1, objectArray.length);
                dynamicFunction.apply(objectArray2);
            }
            if (CPPObject.class.isAssignableFrom(clazz) && this.installSyntheticVTablePtr(type, nativeLibrary, pointer)) {
                BridJ.setJavaObjectFromNativePeer(pointer.getPeer(), t2);
            }
            return pointer;
        }
        catch (Exception exception) {
            Exception exception2 = exception;
            exception.printStackTrace();
            if (pointer != null) {
                pointer.release();
            }
            throw new RuntimeException("Failed to allocate new instance of type " + type, exception2);
        }
    }

    long getVirtualTable(Type type, NativeLibrary nativeLibrary) {
        Constable constable = this.vtables.get(type);
        if (constable == null) {
            constable = Utils.getClass(type);
            Demangler.Symbol symbol = nativeLibrary.getFirstMatchingSymbol(new NativeLibrary.SymbolAccepter((Class)constable){
                final /* synthetic */ Class val$typeClass;
                {
                    this.val$typeClass = clazz;
                }

                public boolean accept(Demangler.Symbol symbol) {
                    return symbol.matchesVirtualTable(this.val$typeClass);
                }
            });
            if (symbol != null) {
                if (BridJ.debug) {
                    BridJ.info("Registering vtable of " + Utils.toString(type) + " as " + symbol.getName());
                }
            } else if (this.getVirtualMethodsCount((Class<?>)constable) > 0 && this.warnAboutMissingVTables()) {
                BridJ.error("Failed to find a vtable for type " + Utils.toString(type));
            }
            if (symbol != null) {
                long l2 = symbol.getAddress();
                constable = nativeLibrary.isMSVC() ? l2 : l2 + (long)(2 * Pointer.SIZE);
            } else {
                constable = 0L;
            }
            this.vtables.put(type, (Long)constable);
        }
        return constable;
    }

    protected boolean warnAboutMissingVTables() {
        return true;
    }

    @Override
    public boolean shouldWarnIfNoFieldsInStruct() {
        return false;
    }

    @Override
    public <T extends NativeObject> BridJRuntime.TypeInfo<T> getTypeInfo(Type type) {
        return new CPPTypeInfo(type);
    }

    @Override
    public Type getType(Class<?> clazz, Object[] objectArray, int[] nArray) {
        int n2;
        Template template = clazz.getAnnotation(Template.class);
        int n3 = n2 = template != null ? template.value().length : clazz.getTypeParameters().length;
        if (nArray != null) {
            assert (nArray.length == 1);
            nArray[0] = n2;
        }
        return new CPPType(clazz, Utils.takeLeft(objectArray, n2));
    }

    public <T extends CPPObject> CPPTypeInfo<T> getCPPTypeInfo(Type type) {
        return (CPPTypeInfo)((CRuntime)this).getTypeInfo(type);
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public class CPPTypeInfo<T extends CPPObject>
    extends CRuntime.CTypeInfo<T> {
        protected final int typeParamCount;
        protected Object[] templateParameters;
        Map<TypeVariable<Class<?>>, ClassTypeVariableExtractor> classTypeVariableExtractors;
        Map<TypeVariable<?>, MethodTypeVariableExtractor> methodTypeVariableExtractors;

        public CPPTypeInfo(Type type) {
            super(type);
            CPPRuntime.this = Utils.getClass(type);
            this.typeParamCount = ((Class)CPPRuntime.this).getTypeParameters().length;
            if (this.typeParamCount > 0 && !(type instanceof ParameterizedType)) {
                throw new RuntimeException("Class " + ((Class)CPPRuntime.this).getName() + " takes type parameters");
            }
            this.templateParameters = this.getTemplateParameters(type);
        }

        @Override
        protected T newCastInstance() throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            if (this.templateParameters.length == 0) {
                return (T)((CPPObject)super.newCastInstance());
            }
            Object object = this.getCastClass();
            for (Constructor<?> constructor : ((Class)object).getConstructors()) {
                if (!Utils.parametersComplyToSignature(this.templateParameters, constructor.getParameterTypes())) continue;
                constructor.setAccessible(true);
                object = (CPPObject)constructor.newInstance(this.templateParameters);
                return (T)object;
            }
            throw new RuntimeException("Failed to find template constructor in class " + ((Class)object).getName());
        }

        public Type resolveClassType(CPPObject cPPObject, TypeVariable<?> typeVariable) {
            return this.getClassTypeVariableExtractor(typeVariable).extract(cPPObject);
        }

        public Type resolveMethodType(CPPObject cPPObject, Object[] objectArray, TypeVariable<?> typeVariable) {
            return this.getMethodTypeVariableExtractor(typeVariable).extract(cPPObject, objectArray);
        }

        protected synchronized ClassTypeVariableExtractor getClassTypeVariableExtractor(TypeVariable<Class<?>> typeVariable) {
            ClassTypeVariableExtractor classTypeVariableExtractor;
            if (this.classTypeVariableExtractors == null) {
                this.classTypeVariableExtractors = new HashMap();
            }
            if ((classTypeVariableExtractor = this.classTypeVariableExtractors.get(typeVariable)) == null) {
                classTypeVariableExtractor = CPPRuntime.this.createClassTypeVariableExtractor(typeVariable);
                this.classTypeVariableExtractors.put(typeVariable, classTypeVariableExtractor);
            }
            return classTypeVariableExtractor;
        }

        protected synchronized MethodTypeVariableExtractor getMethodTypeVariableExtractor(TypeVariable<?> typeVariable) {
            MethodTypeVariableExtractor methodTypeVariableExtractor;
            if (this.methodTypeVariableExtractors == null) {
                this.methodTypeVariableExtractors = new HashMap();
            }
            if ((methodTypeVariableExtractor = this.methodTypeVariableExtractors.get(typeVariable)) == null) {
                methodTypeVariableExtractor = CPPRuntime.this.createMethodTypeVariableExtractor(typeVariable);
                this.methodTypeVariableExtractors.put(typeVariable, methodTypeVariableExtractor);
            }
            return methodTypeVariableExtractor;
        }

        @Override
        public long sizeOf() {
            return super.sizeOf();
        }

        @Override
        public T createReturnInstance() {
            try {
                NativeObject nativeObject = this.newCastInstance();
                this.initialize((T)nativeObject, -2, this.templateParameters);
                return (T)nativeObject;
            }
            catch (Throwable throwable) {
                throw new RuntimeException("Failed to create a return instance for type " + Utils.toString(this.type) + " : " + throwable, throwable);
            }
        }

        @Override
        public T cast(Pointer pointer) {
            if (BridJ.isCastingNativeObjectReturnTypeInCurrentThread()) {
                pointer = pointer.withReleaser(CPPRuntime.this.newCPPReleaser(this.type));
            }
            pointer = (CPPObject)super.cast(pointer);
            CPPRuntime.this.setTemplateParameters((CPPObject)((Object)pointer), this.typeClass, this.templateParameters);
            return (T)pointer;
        }

        @Override
        public void initialize(T t2, Pointer pointer) {
            CPPRuntime.this.setTemplateParameters((CPPObject)t2, this.typeClass, this.templateParameters);
            super.initialize(t2, pointer);
        }

        @Override
        public void initialize(T t2, int n2, Object ... objectArray) {
            if (t2 instanceof CPPObject) {
                Type type = this.type;
                CPPRuntime.this.setTemplateParameters((CPPObject)t2, this.typeClass, this.templateParameters);
                CPPRuntime.this.setNativeObjectPeer(t2, CPPRuntime.this.newCPPInstance(t2, type, n2, objectArray));
                super.initialize(t2, -1, new Object[0]);
                return;
            }
            super.initialize(t2, n2, objectArray);
        }

        @Override
        public T clone(T t2) throws CloneNotSupportedException {
            return (T)((CPPObject)super.clone(t2));
        }

        @Override
        public void destroy(T t2) {
        }

        private Object[] getTemplateParameters(Type type) {
            if (!(type instanceof CPPType)) {
                return new Object[0];
            }
            return ((CPPType)type).getTemplateParameters();
        }
    }

    static class VTable {
        Pointer<Pointer<?>> ptr;
        Map<Method, Pointer<?>> callbacks = new HashMap();

        VTable() {
        }
    }

    @Convention(value=Convention.Style.ThisCall)
    public static abstract class CPPDestructor
    extends Callback {
        public abstract void destroy(long var1);
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static class MemoryOperators {
        protected DynamicFunction<Pointer<?>> newFct;
        protected DynamicFunction<Pointer<?>> newArrayFct;
        protected DynamicFunction<Void> deleteFct;
        protected DynamicFunction<Void> deleteArrayFct;

        protected MemoryOperators() {
        }

        public MemoryOperators(NativeLibrary object) {
            for (Demangler.Symbol symbol : ((NativeLibrary)object).getSymbols()) {
                try {
                    Object object2 = symbol.getParsedRef();
                    object2 = ((Demangler.MemberRef)object2).getMemberName();
                    if (Demangler.SpecialName.New.equals(object2)) {
                        this.newFct = Pointer.pointerToAddress(symbol.getAddress()).asDynamicFunction(null, (Type)((Object)Pointer.class), new Type[]{SizeT.class});
                        continue;
                    }
                    if (Demangler.SpecialName.NewArray.equals(object2)) {
                        this.newFct = Pointer.pointerToAddress(symbol.getAddress()).asDynamicFunction(null, (Type)((Object)Pointer.class), new Type[]{SizeT.class});
                        continue;
                    }
                    if (Demangler.SpecialName.Delete.equals(object2)) {
                        this.newFct = Pointer.pointerToAddress(symbol.getAddress()).asDynamicFunction(null, (Type)((Object)Void.class), new Type[]{Pointer.class});
                        continue;
                    }
                    if (!Demangler.SpecialName.DeleteArray.equals(object2)) continue;
                    this.newFct = Pointer.pointerToAddress(symbol.getAddress()).asDynamicFunction(null, (Type)((Object)Void.class), new Type[]{Pointer.class});
                }
                catch (Exception exception) {}
            }
        }

        public Pointer<?> cppNew(long l2) {
            return this.newFct.apply(new SizeT(l2));
        }

        public Pointer<?> cppNewArray(long l2) {
            return this.newArrayFct.apply(new SizeT(l2));
        }

        public void cppDelete(Pointer<?> pointer) {
            this.deleteFct.apply(pointer);
        }

        public void cppDeleteArray(Pointer<?> pointer) {
            this.deleteArrayFct.apply(pointer);
        }
    }

    protected static class VirtMeth {
        Method implementation;
        Method definition;

        protected VirtMeth() {
        }
    }

    protected static interface MethodTypeVariableExtractor {
        public Type extract(CPPObject var1, Object[] var2);
    }

    protected static interface ClassTypeVariableExtractor {
        public Type extract(CPPObject var1);
    }
}


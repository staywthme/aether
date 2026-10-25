/*
 * Decompiled with CFR 0.152.
 */
package org.bridj.objc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bridj.BridJ;
import org.bridj.BridJRuntime;
import org.bridj.CLong;
import org.bridj.CRuntime;
import org.bridj.CallbackInterface;
import org.bridj.MethodCallInfo;
import org.bridj.NativeEntities;
import org.bridj.NativeLibrary;
import org.bridj.NativeObject;
import org.bridj.Platform;
import org.bridj.Pointer;
import org.bridj.ann.Library;
import org.bridj.ann.Ptr;
import org.bridj.demangling.Demangler;
import org.bridj.objc.ObjCBlock;
import org.bridj.objc.ObjCClass;
import org.bridj.objc.ObjCJNI;
import org.bridj.objc.ObjCObject;
import org.bridj.objc.SEL;
import org.bridj.objc.Selector;
import org.bridj.util.Utils;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
@Library(value="/usr/lib/libobjc.A.dylib")
public class ObjectiveCRuntime
extends CRuntime {
    Map<String, Pointer<? extends ObjCObject>> nativeClassesByObjCName = new HashMap<String, Pointer<? extends ObjCObject>>();
    Map<String, Pointer<? extends ObjCObject>> nativeMetaClassesByObjCName = new HashMap<String, Pointer<? extends ObjCObject>>();
    static final Map<Type, Character> signatureByType = new HashMap<Type, Character>();
    static final Map<Character, List<Type>> typesBySignature = new HashMap<Character, List<Type>>();
    Map<String, Class<? extends ObjCObject>> bridjClassesByObjCName = new HashMap<String, Class<? extends ObjCObject>>();
    static final Pointer.Releaser ObjCBlockReleaser;

    @Override
    public boolean isAvailable() {
        return Platform.isMacOSX();
    }

    public ObjectiveCRuntime() {
        BridJ.register();
        this.rootCallbackClasses.add(ObjCBlock.class);
    }

    <T extends ObjCObject> T realCast(Pointer<? extends ObjCObject> pointer) {
        if (pointer == null) {
            return null;
        }
        Pointer<Byte> pointer2 = ObjectiveCRuntime.object_getClassName(pointer);
        if (pointer2 == null) {
            throw new RuntimeException("Null class name for this ObjectiveC object pointer !");
        }
        Class<? extends ObjCObject> clazz = this.bridjClassesByObjCName.get(pointer2 = pointer2.getCString());
        if (clazz == null) {
            throw new RuntimeException("Class " + (String)((Object)pointer2) + " was not registered yet in the BridJ runtime ! (TODO : auto create by scanning path, then reflection !)");
        }
        return (T)pointer.getNativeObject(clazz);
    }

    protected static native Pointer<? extends ObjCObject> object_getClass(Pointer<? extends ObjCObject> var0);

    protected static native Pointer<? extends ObjCObject> objc_getClass(Pointer<Byte> var0);

    protected static native Pointer<? extends ObjCObject> objc_getMetaClass(Pointer<Byte> var0);

    protected static native Pointer<Byte> object_getClassName(Pointer<? extends ObjCObject> var0);

    protected static native Pointer<? extends ObjCObject> class_createInstance(Pointer<? extends ObjCObject> var0, @Ptr long var1);

    public static native Pointer<? extends ObjCObject> objc_getProtocol(Pointer<Byte> var0);

    public static native boolean class_addProtocol(Pointer<? extends ObjCObject> var0, Pointer<? extends ObjCObject> var1);

    protected static native boolean class_respondsToSelector(Pointer<? extends ObjCObject> var0, SEL var1);

    protected static native SEL sel_registerName(Pointer<Byte> var0);

    protected static native Pointer<Byte> sel_getName(SEL var0);

    public String getMethodSignature(Method method) {
        return this.getMethodSignature(method.getGenericReturnType(), method.getGenericParameterTypes());
    }

    public String getMethodSignature(Type typeArray, Type ... typeArray2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.getTypeSignature((Type)typeArray));
        stringBuilder.append(this.getTypeSignature((Type)((Object)Pointer.class)));
        stringBuilder.append(this.getTypeSignature((Type)((Object)SEL.class)));
        typeArray = typeArray2;
        int n2 = typeArray2.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            Type type = typeArray[i2];
            stringBuilder.append(this.getTypeSignature(type));
        }
        return stringBuilder.toString();
    }

    char getTypeSignature(Type type) {
        Character c2 = signatureByType.get(type);
        if (c2 == null) {
            c2 = signatureByType.get(Utils.getClass(type));
        }
        if (c2 == null) {
            throw new RuntimeException("Unknown type for Objective-C signatures : " + Utils.toString(type));
        }
        return c2.charValue();
    }

    static void addSignature(char c2, Type ... typeArray) {
        List<Type> list2 = typesBySignature.get(Character.valueOf(c2));
        if (list2 == null) {
            list2 = new ArrayList<Type>();
            typesBySignature.put(Character.valueOf(c2), list2);
        }
        for (Type type : typeArray) {
            signatureByType.put(type, Character.valueOf(c2));
            if (type == null || list2.contains(type)) continue;
            list2.add(type);
        }
    }

    static void initSignatures() {
        boolean bl = CLong.SIZE == 4;
        ObjectiveCRuntime.addSignature('q', new Type[]{Long.TYPE, !bl ? CLong.class : null});
        ObjectiveCRuntime.addSignature('i', new Type[]{Integer.TYPE, bl ? CLong.class : null});
        ObjectiveCRuntime.addSignature('I', new Type[]{Integer.TYPE, bl ? CLong.class : null});
        ObjectiveCRuntime.addSignature('s', Short.TYPE, Character.TYPE);
        ObjectiveCRuntime.addSignature('c', Byte.TYPE, Boolean.TYPE);
        ObjectiveCRuntime.addSignature('f', Float.TYPE);
        ObjectiveCRuntime.addSignature('d', Double.TYPE);
        ObjectiveCRuntime.addSignature('v', Void.TYPE);
        ObjectiveCRuntime.addSignature('@', new Type[]{Pointer.class});
        ObjectiveCRuntime.addSignature(':', new Type[]{SEL.class});
    }

    synchronized Pointer<? extends ObjCObject> getObjCClass(String string, boolean bl) throws ClassNotFoundException {
        if (string.equals("")) {
            return null;
        }
        Map<String, Pointer<? extends ObjCObject>> map = bl ? this.nativeMetaClassesByObjCName : this.nativeClassesByObjCName;
        Pointer<Object> pointer = map.get(string);
        if (pointer == null) {
            pointer = Pointer.pointerToCString(string);
            pointer = bl ? ObjectiveCRuntime.objc_getMetaClass(pointer) : ObjectiveCRuntime.objc_getClass(pointer);
            if (pointer != null) {
                assert (ObjectiveCRuntime.object_getClassName(pointer).getCString().equals(string));
                map.put(string, pointer);
            }
        }
        if (pointer == null) {
            throw new ClassNotFoundException("Objective C class not found : " + string);
        }
        return pointer;
    }

    @Override
    public NativeLibrary getNativeLibrary(Class<?> clazz) throws IOException {
        Object object = clazz.getAnnotation(Library.class);
        if (object != null) {
            try {
                object = object.value();
                return BridJ.getNativeLibrary((String)object, new File("/System/Library/Frameworks/" + (String)object + ".framework/" + (String)object));
            }
            catch (FileNotFoundException fileNotFoundException) {}
        }
        return super.getNativeLibrary(clazz);
    }

    @Override
    public void register(Type type) {
        Class clazz = Utils.getClass(type);
        clazz.getAnnotation(Library.class);
        Object object = clazz.getAnnotation(Library.class);
        if (object != null) {
            object = object.value();
            if ((object = BridJ.getNativeLibraryFile((String)object)) != null) {
                System.load(((File)object).toString());
            }
            if (ObjCObject.class.isAssignableFrom(clazz)) {
                this.bridjClassesByObjCName.put(clazz.getSimpleName(), clazz);
            }
        }
        super.register(type);
    }

    public String getSelector(Method method) {
        Object object = method.getAnnotation(Selector.class);
        if (object != null) {
            return object.value();
        }
        object = Demangler.getMethodName(method);
        if (((String)object).endsWith("_")) {
            object = ((String)object).substring(0, ((String)object).length() - 1);
        }
        if (method.getParameterTypes().length > 0) {
            object = (String)object + ":";
        }
        object = ((String)object).replace('_', ':');
        return object;
    }

    @Override
    public void registerNativeMethod(Class<?> object, NativeLibrary object2, Method method, NativeLibrary nativeLibrary, NativeEntities.Builder builder, CRuntime.MethodCallInfoBuilder methodCallInfoBuilder) throws FileNotFoundException {
        if (method == null) {
            return;
        }
        if (!ObjCObject.class.isAssignableFrom((Class<?>)object) || ObjCBlock.class.isAssignableFrom((Class<?>)object)) {
            super.registerNativeMethod((Class<?>)object, (NativeLibrary)object2, method, nativeLibrary, builder, methodCallInfoBuilder);
            return;
        }
        try {
            object2 = methodCallInfoBuilder.apply(method);
            boolean bl = Modifier.isStatic(method.getModifiers());
            if (bl) {
                object = this.getObjCClass((Class<? extends NativeObject>)object).as(ObjCClass.class);
                ((Pointer)object).get();
                ((MethodCallInfo)object2).setNativeClass(((Pointer)object).getPeer());
            }
            ((MethodCallInfo)object2).setSymbolName(this.getSelector(method));
            builder.addObjCMethod((MethodCallInfo)object2);
            return;
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new RuntimeException("Failed to register method " + method + " : " + classNotFoundException, classNotFoundException);
        }
    }

    public static ObjectiveCRuntime getInstance() {
        return BridJ.getRuntimeByRuntimeClass(ObjectiveCRuntime.class);
    }

    public static Type getBlockCallbackType(Class clazz) {
        if (!ObjCBlock.class.isAssignableFrom(clazz) || ObjCBlock.class == clazz) {
            throw new RuntimeException("Class " + clazz.getName() + " should be a subclass of " + ObjCBlock.class.getName());
        }
        Type type = clazz.getGenericSuperclass();
        if (Utils.getClass(type) == ObjCBlock.class) {
            Type type2 = Utils.getUniqueParameterizedTypeParameter(type);
            if (type2 == null || !(type2 instanceof Class) && !(type2 instanceof ParameterizedType)) {
                throw new RuntimeException("Class " + clazz.getName() + " should inherit from " + ObjCBlock.class.getName() + " with a valid single type parameter (found " + Utils.toString(type) + ")");
            }
            return type2;
        }
        throw new RuntimeException("Unexpected failure in getBlockCallbackType");
    }

    @Override
    public <T extends NativeObject> BridJRuntime.TypeInfo<T> getTypeInfo(Type type) {
        return new CRuntime.CTypeInfo<T>(type){

            @Override
            public void initialize(T t2) {
                if (!BridJ.isCastingNativeObjectInCurrentThread()) {
                    if (t2 instanceof ObjCBlock) {
                        Pointer pointer;
                        ((ObjCBlock)t2).pCallback = pointer = ObjectiveCRuntime.this.registerCallbackInstance((CallbackInterface)t2);
                        Pointer pointer2 = Pointer.pointerToAddress(ObjCJNI.createObjCBlockWithFunctionPointer(pointer.getPeer()), this.type);
                        pointer2 = pointer2.withReleaser(new Pointer.Releaser(){

                            @Override
                            public void release(Pointer<?> pointer2) {
                                pointer.release();
                                ObjCJNI.releaseObjCBlock(pointer2.getPeer());
                            }
                        });
                        ObjectiveCRuntime.this.setNativeObjectPeer(t2, pointer2);
                        return;
                    }
                    super.initialize(t2);
                    return;
                }
                super.initialize(t2);
            }

            @Override
            public void initialize(T t2, Pointer pointer) {
                if (t2 instanceof ObjCClass) {
                    ObjectiveCRuntime.this.setNativeObjectPeer(t2, pointer);
                    return;
                }
                super.initialize(t2, pointer);
            }

            @Override
            public void initialize(T t2, int n2, Object ... object) {
                try {
                    object = ObjectiveCRuntime.this.getObjCClass(this.typeClass);
                    if (object == null) {
                        throw new RuntimeException("Failed to get Objective-C class for type " + this.typeClass.getName());
                    }
                    object = ((Pointer)object).as(ObjCClass.class);
                    object = ((Pointer)object).get().new$();
                    if (n2 != -1) {
                        throw new UnsupportedOperationException("TODO handle constructors !");
                    }
                    object = ((ObjCObject)((Pointer)object).get()).init();
                    ObjectiveCRuntime.this.setNativeObjectPeer(t2, (Pointer)object);
                    return;
                }
                catch (ClassNotFoundException classNotFoundException) {
                    throw new RuntimeException("Failed to initialize instance of type " + Utils.toString(this.type) + " : " + classNotFoundException, classNotFoundException);
                }
            }
        };
    }

    public static Pointer<? extends ObjCObject> getObjCClass(String string) throws ClassNotFoundException {
        return ObjectiveCRuntime.getInstance().getObjCClass(string, false);
    }

    private Pointer<? extends ObjCObject> getObjCClass(Class<? extends NativeObject> clazz) throws ClassNotFoundException {
        if (clazz == ObjCClass.class) {
            return this.getObjCClass("NSObject", true);
        }
        if (clazz == ObjCObject.class) {
            return this.getObjCClass("NSObject", false);
        }
        return this.getObjCClass(clazz.getSimpleName(), false);
    }

    static {
        ObjectiveCRuntime.initSignatures();
        ObjCBlockReleaser = new Pointer.Releaser(){

            public final void release(Pointer pointer) {
                ObjCJNI.releaseObjCBlock(pointer.getPeer());
            }
        };
    }
}


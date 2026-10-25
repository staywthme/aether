/*
 * Decompiled with CFR 0.152.
 */
package org.bridj;

import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.bridj.CRuntime;
import org.bridj.CallbackInterface;
import org.bridj.DynamicFunction;
import org.bridj.DynamicFunctionFactory;
import org.bridj.MethodCallInfo;
import org.bridj.NativeEntities;
import org.bridj.NativeLibrary;
import org.bridj.Platform;
import org.bridj.PlatformSupport;
import org.bridj.ann.Convention;
import org.bridj.relocated.org.objectweb.asm.ClassVisitor;
import org.bridj.relocated.org.objectweb.asm.ClassWriter;
import org.bridj.relocated.org.objectweb.asm.Label;
import org.bridj.relocated.org.objectweb.asm.MethodVisitor;
import org.bridj.util.ASMUtils;
import org.bridj.util.ClassDefiner;
import org.bridj.util.JNIUtils;
import org.bridj.util.Pair;
import org.bridj.util.Utils;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
class CallbackNativeImplementer
extends ClassLoader
implements ClassDefiner {
    ConcurrentHashMap<Class<? extends CallbackInterface>, Class<?>> implClasses = new ConcurrentHashMap();
    String implNameSuffix = "_NativeImpl";
    final NativeEntities nativeEntities;
    final CRuntime runtime;
    volatile ClassDefiner classDefiner;
    protected Map<Pair<NativeLibrary, Pair<Convention.Style, List<Type>>>, DynamicFunctionFactory> dynamicCallbacks = new HashMap<Pair<NativeLibrary, Pair<Convention.Style, List<Type>>>, DynamicFunctionFactory>();
    private static volatile long nextDynamicCallbackId = 0L;

    public CallbackNativeImplementer(NativeEntities nativeEntities, CRuntime cRuntime) {
        super(Platform.getClassLoader());
        this.nativeEntities = nativeEntities;
        this.runtime = cRuntime;
    }

    public synchronized ClassDefiner getClassDefiner() {
        if (this.classDefiner == null) {
            CallbackNativeImplementer callbackNativeImplementer = this;
            this.classDefiner = PlatformSupport.getInstance().getClassDefiner(callbackNativeImplementer, callbackNativeImplementer);
        }
        return this.classDefiner;
    }

    public <T extends CallbackInterface> Class<? extends T> getCallbackImplType(Class<T> clazz, NativeLibrary nativeLibrary) {
        Class<?> clazz2 = this.implClasses.get(clazz);
        if (clazz2 == null) {
            try {
                clazz2 = clazz.getName().replace('.', '/');
                Object object = ((String)((Object)clazz2)).replace('$', '_') + this.implNameSuffix;
                String string = clazz.getSimpleName() + this.implNameSuffix + ".java";
                Object object2 = this.runtime.getFastestCallbackMethod(clazz);
                ((Method)object2).getParameterTypes();
                Object object3 = new MethodCallInfo((Method)object2);
                object2 = ((Method)object2).getName();
                object3 = ((MethodCallInfo)object3).getJavaSignature();
                clazz2 = (Class<?>)this.emitBytes(string, (String)((Object)clazz2), (String)object, (String)object2, (String)object3);
                clazz2 = this.getClassDefiner().defineClass(((String)object).replace('/', '.'), (byte[])clazz2);
                object = this.implClasses.putIfAbsent(clazz, clazz2);
                if (object != null) {
                    return object;
                }
                this.runtime.register(clazz2, nativeLibrary, null);
            }
            catch (Exception exception) {
                throw new RuntimeException("Failed to create implementation class for callback type " + clazz.getName() + " : " + exception, exception);
            }
        }
        return clazz2;
    }

    private static synchronized long getNextDynamicCallbackId() {
        return nextDynamicCallbackId++;
    }

    public synchronized DynamicFunctionFactory getDynamicCallback(NativeLibrary object, final Convention.Style style, Type classArray, Type ... typeArray) {
        ArrayList<Object> arrayList = new ArrayList<Object>(typeArray.length + 1);
        arrayList.add(classArray);
        arrayList.addAll(Arrays.asList(typeArray));
        Object object2 = new Pair(style, arrayList);
        object = new Pair((NativeLibrary)object, object2);
        object2 = this.dynamicCallbacks.get(object);
        if (object2 == null) {
            try {
                Class<?> clazz;
                object2 = new StringBuilder("(");
                StringBuilder stringBuilder = new StringBuilder();
                Object object3 = typeArray;
                int n2 = typeArray.length;
                for (int i2 = 0; i2 < n2; ++i2) {
                    clazz = object3[i2];
                    ((StringBuilder)object2).append(JNIUtils.getNativeSignature(Utils.getClass(clazz)));
                    stringBuilder.append(ASMUtils.typeDesc(clazz));
                }
                ((StringBuilder)object2).append(")").append(JNIUtils.getNativeSignature(Utils.getClass((Type)classArray)));
                stringBuilder.append("To").append(ASMUtils.typeDesc((Type)classArray)).append("_").append(CallbackNativeImplementer.getNextDynamicCallbackId());
                object3 = "org/bridj/dyncallbacks/" + stringBuilder;
                String string = "apply";
                byte[] byArray = this.emitBytes("<anonymous>", DynamicFunction.class.getName().replace(".", "/"), (String)object3, string, ((StringBuilder)object2).toString());
                clazz = this.getClassDefiner().defineClass(((String)object3).replace('/', '.'), byArray);
                classArray = new Class[typeArray.length];
                int n3 = typeArray.length;
                for (int i3 = 0; i3 < n3; ++i3) {
                    classArray[i3] = Utils.getClass(typeArray[i3]);
                }
                CRuntime.MethodCallInfoBuilder methodCallInfoBuilder = new CRuntime.MethodCallInfoBuilder(){

                    public MethodCallInfo apply(Method object) throws FileNotFoundException {
                        object = super.apply((Method)object);
                        ((MethodCallInfo)object).setCallingConvention(style);
                        return object;
                    }
                };
                Class<?> clazz2 = clazz;
                object2 = new DynamicFunctionFactory(clazz2, clazz2.getMethod(string, classArray), methodCallInfoBuilder);
                this.dynamicCallbacks.put((Pair<NativeLibrary, Pair<Convention.Style, List<Type>>>)object, (DynamicFunctionFactory)object2);
                this.runtime.register(clazz, null, methodCallInfoBuilder);
            }
            catch (Throwable throwable) {
                object2 = throwable;
                throwable.printStackTrace();
                throw new RuntimeException("Failed to create callback for " + arrayList + " : " + object2, (Throwable)object2);
            }
        }
        return object2;
    }

    private byte[] emitBytes(String object, String object2, String string, String string2, String string3) {
        ClassWriter classWriter = new ClassWriter(0);
        ((ClassVisitor)classWriter).visit(49, 33, string, null, (String)object2, null);
        ((ClassVisitor)classWriter).visitSource((String)object, null);
        object = ((ClassVisitor)classWriter).visitMethod(1, "<init>", "()V", null, null);
        ((MethodVisitor)object).visitCode();
        Label label = new Label();
        ((MethodVisitor)object).visitLabel(label);
        ((MethodVisitor)object).visitLineNumber(5, label);
        ((MethodVisitor)object).visitVarInsn(25, 0);
        ((MethodVisitor)object).visitMethodInsn(183, (String)object2, "<init>", "()V");
        ((MethodVisitor)object).visitInsn(177);
        object2 = new Label();
        ((MethodVisitor)object).visitLabel((Label)object2);
        ((MethodVisitor)object).visitLocalVariable("this", "L" + string + ";", null, label, (Label)object2, 0);
        ((MethodVisitor)object).visitMaxs(1, 1);
        ((MethodVisitor)object).visitEnd();
        object = ((ClassVisitor)classWriter).visitMethod(257, string2, string3, null, null);
        ((MethodVisitor)object).visitEnd();
        ((ClassVisitor)classWriter).visitEnd();
        return classWriter.toByteArray();
    }

    @Override
    public Class<?> defineClass(String string, byte[] byArray) throws ClassFormatError {
        return this.defineClass(string, byArray, 0, byArray.length);
    }
}


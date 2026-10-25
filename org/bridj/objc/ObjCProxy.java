/*
 * Decompiled with CFR 0.152.
 */
package org.bridj.objc;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import org.bridj.BridJ;
import org.bridj.NativeObject;
import org.bridj.Pointer;
import org.bridj.PointerIO;
import org.bridj.objc.NSInvocation;
import org.bridj.objc.NSMethodSignature;
import org.bridj.objc.ObjCJNI;
import org.bridj.objc.ObjCObject;
import org.bridj.objc.ObjectiveCRuntime;
import org.bridj.objc.SEL;
import org.bridj.util.Pair;
import org.bridj.util.Utils;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class ObjCProxy
extends ObjCObject {
    final Map<SEL, Pair<NSMethodSignature, Method>> signatures = new HashMap<SEL, Pair<NSMethodSignature, Method>>();
    final Object invocationTarget;
    static final String PROXY_OBJC_CLASS_NAME = "ObjCProxy";

    protected ObjCProxy() {
        super(null);
        this.peer = ObjCJNI.createObjCProxyPeer(this);
        assert (this.getClass() != ObjCProxy.class);
        this.invocationTarget = this;
    }

    public ObjCProxy(Object object) {
        super(null);
        this.peer = ObjCJNI.createObjCProxyPeer(this);
        assert (object != null);
        this.invocationTarget = object;
    }

    public void addProtocol(String string) throws ClassNotFoundException {
        Pointer<? extends ObjCObject> pointer = ObjectiveCRuntime.objc_getProtocol(Pointer.pointerToCString(string));
        if (pointer == null) {
            throw new ClassNotFoundException("Protocol " + string + " not found !");
        }
        Pointer<? extends ObjCObject> pointer2 = ObjectiveCRuntime.getObjCClass(PROXY_OBJC_CLASS_NAME);
        if (!ObjectiveCRuntime.class_addProtocol(pointer2, pointer)) {
            throw new RuntimeException("Failed to add protocol " + string + " to class ObjCProxy");
        }
    }

    public Object getInvocationTarget() {
        return this.invocationTarget;
    }

    public Pointer<NSMethodSignature> methodSignatureForSelector(SEL comparable) {
        if ((comparable = this.getMethodAndSignature((SEL)comparable)) == null) {
            return null;
        }
        return Pointer.getPointer((NativeObject)((Pair)comparable).getFirst());
    }

    public synchronized Pair<NSMethodSignature, Method> getMethodAndSignature(SEL sEL) {
        Pair<NSMethodSignature, Method> pair = this.signatures.get(sEL);
        if (pair == null) {
            try {
                pair = this.computeMethodAndSignature(sEL);
                if (pair != null) {
                    this.signatures.put(sEL, pair);
                }
            }
            catch (Throwable throwable) {
                BridJ.error("Failed to compute Objective-C signature for selector " + sEL + ": " + throwable, throwable);
            }
        }
        return pair;
    }

    Pair<NSMethodSignature, Method> computeMethodAndSignature(SEL object) {
        Object object2 = ((SEL)object).getName();
        ObjectiveCRuntime objectiveCRuntime = ObjectiveCRuntime.getInstance();
        for (Method method : this.invocationTarget.getClass().getMethods()) {
            long l2;
            String string = objectiveCRuntime.getSelector(method);
            if (!string.equals(object2)) continue;
            object = objectiveCRuntime.getMethodSignature(method);
            if (BridJ.debug) {
                BridJ.info("Objective-C signature for method " + method + " = '" + (String)object + "'");
            }
            if ((l2 = ((NSMethodSignature)(object2 = NSMethodSignature.signatureWithObjCTypes(Pointer.pointerToCString((String)object)).get())).numberOfArguments() - 2L) != (long)method.getParameterTypes().length) {
                throw new RuntimeException("Bad method signature (mismatching arg types) : '" + (String)object + "' for " + method);
            }
            return new Pair<Object, Method>(object2, method);
        }
        BridJ.error("Missing method for " + object + " in class " + ObjCProxy.classHierarchyToString(this.getInvocationTarget().getClass()));
        return null;
    }

    static String classHierarchyToString(Class type) {
        String string = Utils.toString(type);
        type = type.getGenericSuperclass();
        while (type != null && type != Object.class && type != ObjCProxy.class) {
            string = string + " extends " + Utils.toString(type);
            type = Utils.getClass(type).getGenericSuperclass();
        }
        return string;
    }

    public synchronized void forwardInvocation(Pointer<NSInvocation> object) {
        Object object2;
        object = ((Pointer)object).get();
        Object object3 = ((NSInvocation)object).selector();
        object3 = this.getMethodAndSignature((SEL)object3);
        ((Pair)object3).getFirst();
        object3 = (Method)((Pair)object3).getSecond();
        Type[] typeArray = ((Method)object3).getGenericParameterTypes();
        int n2 = typeArray.length;
        Object[] objectArray = new Object[n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            object2 = typeArray[i2];
            object2 = PointerIO.getInstance((Type)object2);
            object2 = Pointer.allocate(object2);
            ((NSInvocation)object).getArgument_atIndex((Pointer<?>)object2, i2 + 2);
            object2 = ((Pointer)object2).get();
            objectArray[i2] = object2;
        }
        try {
            ((Method)object3).setAccessible(true);
            Object object4 = ((Method)object3).invoke(this.getInvocationTarget(), objectArray);
            object2 = ((Method)object3).getGenericReturnType();
            if (object2 == Void.TYPE) {
                assert (object4 == null);
            } else {
                object2 = PointerIO.getInstance((Type)object2);
                object2 = Pointer.allocate(object2);
                ((Pointer)object2).set(object4);
                ((NSInvocation)object).setReturnValue((Pointer<?>)object2);
            }
            return;
        }
        catch (Throwable throwable) {
            throw new RuntimeException("Failed to forward invocation from Objective-C to Java invocation target " + this.getInvocationTarget() + " for method " + object3 + " : " + throwable, throwable);
        }
    }
}


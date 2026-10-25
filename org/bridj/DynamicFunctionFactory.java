/*
 * Decompiled with CFR 0.152.
 */
package org.bridj;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.bridj.BridJ;
import org.bridj.CRuntime;
import org.bridj.DynamicFunction;
import org.bridj.JNI;
import org.bridj.MethodCallInfo;
import org.bridj.Pointer;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class DynamicFunctionFactory {
    final Constructor<? extends DynamicFunction> constructor;
    final Method method;
    final long callbackHandle;

    DynamicFunctionFactory(Class<? extends DynamicFunction> object, Method method, CRuntime.MethodCallInfoBuilder methodCallInfoBuilder) {
        try {
            this.constructor = ((Class)object).getConstructor(new Class[0]);
            this.method = method;
            object = methodCallInfoBuilder.apply(method);
            this.callbackHandle = JNI.bindJavaToCCallbacks(new MethodCallInfo[]{object});
            return;
        }
        catch (Throwable throwable) {
            object = throwable;
            throwable.printStackTrace();
            throw new RuntimeException("Failed to instantiate callback : " + object, (Throwable)object);
        }
    }

    protected void finalize() throws Throwable {
        if (BridJ.debugNeverFree) {
            return;
        }
        JNI.freeJavaToCCallbacks(this.callbackHandle, 1);
    }

    public DynamicFunction newInstance(Pointer<?> pointer) {
        if (pointer == null) {
            return null;
        }
        try {
            DynamicFunction dynamicFunction = this.constructor.newInstance(new Object[0]);
            this.constructor.newInstance(new Object[0]).peer = pointer;
            dynamicFunction.method = this.method;
            dynamicFunction.factory = this;
            return dynamicFunction;
        }
        catch (Throwable throwable) {
            Throwable throwable2 = throwable;
            throwable.printStackTrace();
            throw new RuntimeException("Failed to instantiate callback : " + throwable2, throwable2);
        }
    }

    public String toString() {
        return this.getClass().getSimpleName() + "(" + this.method + ")";
    }
}


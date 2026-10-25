/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna;

import com.sun.jna.Function;
import com.sun.jna.FunctionParameterContext;
import java.lang.reflect.Method;

public class MethodParameterContext
extends FunctionParameterContext {
    private Method method;

    MethodParameterContext(Function function, Object[] objectArray, int n2, Method method) {
        super(function, objectArray, n2);
        this.method = method;
    }

    public Method getMethod() {
        return this.method;
    }
}


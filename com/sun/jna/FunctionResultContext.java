/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna;

import com.sun.jna.FromNativeContext;
import com.sun.jna.Function;

public class FunctionResultContext
extends FromNativeContext {
    private Function function;
    private Object[] args;

    FunctionResultContext(Class<?> clazz, Function function, Object[] objectArray) {
        super(clazz);
        this.function = function;
        this.args = objectArray;
    }

    public Function getFunction() {
        return this.function;
    }

    public Object[] getArguments() {
        return this.args;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna;

import com.sun.jna.Function;
import com.sun.jna.ToNativeContext;

public class FunctionParameterContext
extends ToNativeContext {
    private Function function;
    private Object[] args;
    private int index;

    FunctionParameterContext(Function function, Object[] objectArray, int n2) {
        this.function = function;
        this.args = objectArray;
        this.index = n2;
    }

    public Function getFunction() {
        return this.function;
    }

    public Object[] getParameters() {
        return this.args;
    }

    public int getParameterIndex() {
        return this.index;
    }
}


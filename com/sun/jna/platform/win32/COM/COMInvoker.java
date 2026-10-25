/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32.COM;

import com.sun.jna.Function;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;

public abstract class COMInvoker
extends PointerType {
    protected int _invokeNativeInt(int n2, Object[] objectArray) {
        Pointer pointer = this.getPointer().getPointer(0L);
        Function function = Function.getFunction(pointer.getPointer(n2 * Native.POINTER_SIZE));
        return function.invokeInt(objectArray);
    }

    protected Object _invokeNativeObject(int n2, Object[] objectArray, Class<?> clazz) {
        Pointer pointer = this.getPointer().getPointer(0L);
        Function function = Function.getFunction(pointer.getPointer(n2 * Native.POINTER_SIZE));
        return function.invoke(clazz, objectArray);
    }

    protected void _invokeNativeVoid(int n2, Object[] objectArray) {
        Pointer pointer = this.getPointer().getPointer(0L);
        Function function = Function.getFunction(pointer.getPointer(n2 * Native.POINTER_SIZE));
        function.invokeVoid(objectArray);
    }
}


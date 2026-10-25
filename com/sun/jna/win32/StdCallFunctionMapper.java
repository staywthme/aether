/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.win32;

import com.sun.jna.Function;
import com.sun.jna.FunctionMapper;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.NativeMapped;
import com.sun.jna.NativeMappedConverter;
import java.lang.reflect.Method;

public class StdCallFunctionMapper
implements FunctionMapper {
    protected int getArgumentNativeStackSize(Class<?> clazz) {
        if (NativeMapped.class.isAssignableFrom(clazz)) {
            clazz = NativeMappedConverter.getInstance(clazz).nativeType();
        }
        if (clazz.isArray()) {
            return Native.POINTER_SIZE;
        }
        try {
            return Native.getNativeSize(clazz);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            throw new IllegalArgumentException("Unknown native stack allocation size for " + clazz);
        }
    }

    @Override
    public String getFunctionName(NativeLibrary nativeLibrary, Method object) {
        Object object2;
        String string = object.getName();
        int n2 = 0;
        Class<?>[] classArray = object.getParameterTypes();
        object = classArray;
        object = classArray;
        int n3 = classArray.length;
        for (int i2 = 0; i2 < n3; ++i2) {
            object2 = object[i2];
            n2 += this.getArgumentNativeStackSize((Class<?>)object2);
        }
        object = string + "@" + n2;
        try {
            Function function = nativeLibrary.getFunction((String)object, 63);
            string = function.getName();
        }
        catch (UnsatisfiedLinkError unsatisfiedLinkError) {
            try {
                object2 = nativeLibrary.getFunction("_" + object, 63);
                string = ((Function)object2).getName();
            }
            catch (UnsatisfiedLinkError unsatisfiedLinkError2) {}
        }
        return string;
    }
}


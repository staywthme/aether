/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.win32;

import com.sun.jna.FunctionMapper;
import com.sun.jna.NativeLibrary;
import java.lang.reflect.Method;

public class W32APIFunctionMapper
implements FunctionMapper {
    public static final FunctionMapper UNICODE = new W32APIFunctionMapper(true);
    public static final FunctionMapper ASCII = new W32APIFunctionMapper(false);
    private final String suffix;

    protected W32APIFunctionMapper(boolean bl) {
        this.suffix = bl ? "W" : "A";
    }

    @Override
    public String getFunctionName(NativeLibrary nativeLibrary, Method object) {
        if (!((String)(object = ((Method)object).getName())).endsWith("W") && !((String)object).endsWith("A")) {
            try {
                object = nativeLibrary.getFunction((String)object + this.suffix, 63).getName();
            }
            catch (UnsatisfiedLinkError unsatisfiedLinkError) {}
        }
        return object;
    }
}


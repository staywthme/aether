/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform;

import com.sun.jna.FromNativeContext;
import com.sun.jna.ToNativeContext;
import com.sun.jna.TypeConverter;

public class EnumConverter<T extends Enum<T>>
implements TypeConverter {
    private final Class<T> clazz;

    public EnumConverter(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T fromNative(Object object, FromNativeContext enumArray) {
        object = (Integer)object;
        enumArray = (Enum[])this.clazz.getEnumConstants();
        return (T)enumArray[(Integer)object];
    }

    @Override
    public Integer toNative(Object object, ToNativeContext toNativeContext) {
        object = (Enum)this.clazz.cast(object);
        return ((Enum)object).ordinal();
    }

    public Class<Integer> nativeType() {
        return Integer.class;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson;

import com.google.gson.internal.ReflectionAccessFilterHelper;

public interface ReflectionAccessFilter {
    public static final ReflectionAccessFilter BLOCK_INACCESSIBLE_JAVA = new ReflectionAccessFilter(){

        @Override
        public FilterResult check(Class<?> clazz) {
            if (ReflectionAccessFilterHelper.isJavaType(clazz)) {
                return FilterResult.BLOCK_INACCESSIBLE;
            }
            return FilterResult.INDECISIVE;
        }
    };
    public static final ReflectionAccessFilter BLOCK_ALL_JAVA = new ReflectionAccessFilter(){

        @Override
        public FilterResult check(Class<?> clazz) {
            if (ReflectionAccessFilterHelper.isJavaType(clazz)) {
                return FilterResult.BLOCK_ALL;
            }
            return FilterResult.INDECISIVE;
        }
    };
    public static final ReflectionAccessFilter BLOCK_ALL_ANDROID = new ReflectionAccessFilter(){

        @Override
        public FilterResult check(Class<?> clazz) {
            if (ReflectionAccessFilterHelper.isAndroidType(clazz)) {
                return FilterResult.BLOCK_ALL;
            }
            return FilterResult.INDECISIVE;
        }
    };
    public static final ReflectionAccessFilter BLOCK_ALL_PLATFORM = new ReflectionAccessFilter(){

        @Override
        public FilterResult check(Class<?> clazz) {
            if (ReflectionAccessFilterHelper.isAnyPlatformType(clazz)) {
                return FilterResult.BLOCK_ALL;
            }
            return FilterResult.INDECISIVE;
        }
    };

    public FilterResult check(Class<?> var1);

    public static enum FilterResult {
        ALLOW,
        INDECISIVE,
        BLOCK_INACCESSIBLE,
        BLOCK_ALL;

    }
}


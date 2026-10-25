/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson.internal;

import com.google.gson.ReflectionAccessFilter;
import com.google.gson.internal.JavaVersion;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.List;

public class ReflectionAccessFilterHelper {
    private ReflectionAccessFilterHelper() {
    }

    public static boolean isJavaType(Class<?> clazz) {
        return ReflectionAccessFilterHelper.isJavaType(clazz.getName());
    }

    private static boolean isJavaType(String string) {
        return string.startsWith("java.") || string.startsWith("javax.");
    }

    public static boolean isAndroidType(Class<?> clazz) {
        return ReflectionAccessFilterHelper.isAndroidType(clazz.getName());
    }

    private static boolean isAndroidType(String string) {
        return string.startsWith("android.") || string.startsWith("androidx.") || ReflectionAccessFilterHelper.isJavaType(string);
    }

    public static boolean isAnyPlatformType(Class<?> object) {
        return ReflectionAccessFilterHelper.isAndroidType((String)(object = ((Class)object).getName())) || ((String)object).startsWith("kotlin.") || ((String)object).startsWith("kotlinx.") || ((String)object).startsWith("scala.");
    }

    public static ReflectionAccessFilter.FilterResult getFilterResult(List<ReflectionAccessFilter> object, Class<?> clazz) {
        object = object.iterator();
        while (object.hasNext()) {
            Object object2 = (ReflectionAccessFilter)object.next();
            if ((object2 = object2.check(clazz)) == ReflectionAccessFilter.FilterResult.INDECISIVE) continue;
            return object2;
        }
        return ReflectionAccessFilter.FilterResult.ALLOW;
    }

    public static boolean canAccess(AccessibleObject accessibleObject, Object object) {
        return AccessChecker.INSTANCE.canAccess(accessibleObject, object);
    }

    static abstract class AccessChecker {
        public static final AccessChecker INSTANCE;

        private AccessChecker() {
        }

        public abstract boolean canAccess(AccessibleObject var1, Object var2);

        static {
            AccessChecker accessChecker = null;
            if (JavaVersion.isJava9OrLater()) {
                try {
                    final Method method = AccessibleObject.class.getDeclaredMethod("canAccess", Object.class);
                    accessChecker = new AccessChecker(){

                        @Override
                        public boolean canAccess(AccessibleObject accessibleObject, Object object) {
                            try {
                                return (Boolean)method.invoke(accessibleObject, object);
                            }
                            catch (Exception exception) {
                                throw new RuntimeException("Failed invoking canAccess", exception);
                            }
                        }
                    };
                }
                catch (NoSuchMethodException noSuchMethodException) {}
            }
            if (accessChecker == null) {
                accessChecker = new AccessChecker(){

                    @Override
                    public boolean canAccess(AccessibleObject accessibleObject, Object object) {
                        return true;
                    }
                };
            }
            INSTANCE = accessChecker;
        }
    }
}


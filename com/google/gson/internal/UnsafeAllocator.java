/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson.internal;

import com.google.gson.internal.ConstructorConstructor;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Field;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Method;

public abstract class UnsafeAllocator {
    public static final UnsafeAllocator INSTANCE = UnsafeAllocator.create();

    public abstract <T> T newInstance(Class<T> var1) throws Exception;

    private static void assertInstantiable(Class<?> object) {
        if ((object = ConstructorConstructor.checkInstantiable(object)) != null) {
            throw new AssertionError((Object)("UnsafeAllocator is used for non-instantiable type: " + (String)object));
        }
    }

    private static UnsafeAllocator create() {
        try {
            GenericDeclaration genericDeclaration = Class.forName("sun.misc.Unsafe");
            Field field = genericDeclaration.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            Object object = field.get(null);
            genericDeclaration = genericDeclaration.getMethod("allocateInstance", Class.class);
            return new UnsafeAllocator((Method)genericDeclaration, object){
                final /* synthetic */ Method val$allocateInstance;
                final /* synthetic */ Object val$unsafe;
                {
                    this.val$allocateInstance = method;
                    this.val$unsafe = object;
                }

                @Override
                public <T> T newInstance(Class<T> clazz) throws Exception {
                    UnsafeAllocator.assertInstantiable(clazz);
                    return (T)this.val$allocateInstance.invoke(this.val$unsafe, clazz);
                }
            };
        }
        catch (Exception exception) {
            try {
                Method method = ObjectStreamClass.class.getDeclaredMethod("getConstructorId", Class.class);
                method.setAccessible(true);
                final int n2 = (Integer)method.invoke(null, Object.class);
                final Method method2 = ObjectStreamClass.class.getDeclaredMethod("newInstance", Class.class, Integer.TYPE);
                method2.setAccessible(true);
                return new UnsafeAllocator(){

                    @Override
                    public <T> T newInstance(Class<T> clazz) throws Exception {
                        UnsafeAllocator.assertInstantiable(clazz);
                        return (T)method2.invoke(null, clazz, n2);
                    }
                };
            }
            catch (Exception exception2) {
                try {
                    final Method method = ObjectInputStream.class.getDeclaredMethod("newInstance", Class.class, Class.class);
                    method.setAccessible(true);
                    return new UnsafeAllocator(){

                        @Override
                        public <T> T newInstance(Class<T> clazz) throws Exception {
                            UnsafeAllocator.assertInstantiable(clazz);
                            return (T)method.invoke(null, clazz, Object.class);
                        }
                    };
                }
                catch (Exception exception3) {
                    return new UnsafeAllocator(){

                        @Override
                        public <T> T newInstance(Class<T> clazz) {
                            throw new UnsupportedOperationException("Cannot allocate " + clazz + ". Usage of JDK sun.misc.Unsafe is enabled, but it could not be used. Make sure your runtime is configured correctly.");
                        }
                    };
                }
            }
        }
    }
}


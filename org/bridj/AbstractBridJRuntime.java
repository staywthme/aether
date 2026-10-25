/*
 * Decompiled with CFR 0.152.
 */
package org.bridj;

import java.lang.reflect.Type;
import org.bridj.BridJRuntime;
import org.bridj.NativeObject;
import org.bridj.ann.Constructor;
import org.bridj.util.Utils;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public abstract class AbstractBridJRuntime
implements BridJRuntime {
    @Override
    public void unregister(Type type) {
    }

    @Override
    public Type getType(NativeObject nativeObject) {
        if (nativeObject == null) {
            return null;
        }
        return Utils.getClass(nativeObject.getClass());
    }

    protected java.lang.reflect.Constructor findConstructor(Class<?> clazz, int n2, boolean n3) throws SecurityException, NoSuchMethodException {
        java.lang.reflect.Constructor<?>[] constructorArray;
        for (java.lang.reflect.Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            constructorArray = constructor.getAnnotation(Constructor.class);
            if (constructorArray == null || constructorArray.value() != n2) continue;
            return constructor;
        }
        if (n2 < 0) {
            return clazz.getConstructor(new Class[0]);
        }
        Class<?> clazz2 = clazz.getSuperclass();
        if (clazz2 != null) {
            try {
                java.lang.reflect.Constructor<?>[] constructorArray2;
                Type[] typeArray = this.findConstructor(clazz2, n2, n3 != 0);
                if (n3 != 0 && typeArray != null) {
                    return typeArray;
                }
                Type[] typeArray2 = typeArray.getGenericParameterTypes();
                constructorArray = constructorArray2 = clazz.getDeclaredConstructors();
                int n4 = constructorArray2.length;
                for (n3 = 0; n3 < n4; ++n3) {
                    int n5;
                    java.lang.reflect.Constructor<?> constructor = constructorArray[n3];
                    typeArray = constructor.getGenericParameterTypes();
                    if (!AbstractBridJRuntime.isOverridenSignature(typeArray2, typeArray, n5 = Utils.getEnclosedConstructorParametersOffset(constructor))) continue;
                    return constructor;
                }
            }
            catch (Throwable throwable) {
                Throwable throwable2 = throwable;
                throwable.printStackTrace();
            }
        }
        throw new NoSuchMethodException("Cannot find constructor with index " + n2);
    }

    public static boolean isOverridenSignature(Type[] typeArray, Type[] typeArray2, int n2) {
        int n3 = typeArray.length;
        if (typeArray2.length - n2 != n3) {
            return false;
        }
        for (int i2 = 0; i2 < n3; ++i2) {
            if (AbstractBridJRuntime.isOverride(typeArray[i2], typeArray2[n2 + i2])) continue;
            return false;
        }
        return true;
    }

    protected static boolean isOverride(Type type, Type type2) {
        return Utils.getClass(type).isAssignableFrom(Utils.getClass(type2));
    }
}


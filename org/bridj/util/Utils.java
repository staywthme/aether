/*
 * Decompiled with CFR 0.152.
 */
package org.bridj.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class Utils {
    public static int getEnclosedConstructorParametersOffset(Constructor classArray) {
        Class<?> clazz = classArray.getDeclaringClass().getEnclosingClass();
        int n2 = (classArray = classArray.getParameterTypes()).length > 0 && clazz != null && clazz == classArray[0] ? 1 : 0;
        return n2;
    }

    public static boolean isDirect(Buffer buffer) {
        if (buffer instanceof ByteBuffer) {
            return ((ByteBuffer)buffer).isDirect();
        }
        if (buffer instanceof IntBuffer) {
            return ((IntBuffer)buffer).isDirect();
        }
        if (buffer instanceof LongBuffer) {
            return ((LongBuffer)buffer).isDirect();
        }
        if (buffer instanceof DoubleBuffer) {
            return ((DoubleBuffer)buffer).isDirect();
        }
        if (buffer instanceof FloatBuffer) {
            return ((FloatBuffer)buffer).isDirect();
        }
        if (buffer instanceof ShortBuffer) {
            return ((ShortBuffer)buffer).isDirect();
        }
        if (buffer instanceof CharBuffer) {
            return ((CharBuffer)buffer).isDirect();
        }
        return false;
    }

    public static Object[] takeRight(Object[] objectArray, int n2) {
        if (n2 == objectArray.length) {
            return objectArray;
        }
        Object[] objectArray2 = new Object[n2];
        System.arraycopy(objectArray, objectArray.length - n2, objectArray2, 0, n2);
        return objectArray2;
    }

    public static Object[] takeLeft(Object[] objectArray, int n2) {
        if (n2 == objectArray.length) {
            return objectArray;
        }
        Object[] objectArray2 = new Object[n2];
        System.arraycopy(objectArray, 0, objectArray2, 0, n2);
        return objectArray2;
    }

    public static boolean isSignedIntegral(Type type) {
        return type == Integer.TYPE || type == Integer.class || type == Long.TYPE || type == Long.class || type == Short.TYPE || type == Short.class || type == Byte.TYPE || type == Byte.class;
    }

    public static String toString(Type type) {
        if (type == null) {
            return "?";
        }
        if (type instanceof Class) {
            return ((Class)type).getName();
        }
        return type.toString();
    }

    public static String toString(Throwable throwable) {
        if (throwable == null) {
            return "<no trace>";
        }
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        return stringWriter.toString();
    }

    public static boolean eq(Object object, Object object2) {
        if (object == null != (object2 == null)) {
            return false;
        }
        return object == null || object.equals(object2);
    }

    public static boolean containsTypeVariables(Type typeArray) {
        if (typeArray instanceof TypeVariable) {
            return true;
        }
        if (typeArray instanceof ParameterizedType) {
            typeArray = (ParameterizedType)typeArray;
            for (Type type : typeArray.getActualTypeArguments()) {
                if (!Utils.containsTypeVariables(type)) continue;
                return true;
            }
        }
        return false;
    }

    public static <T> Class<T> getClass(Type object) {
        while (true) {
            if (object == null) {
                return null;
            }
            if (object instanceof Class) {
                return (Class)object;
            }
            if (object instanceof ParameterizedType) {
                object = ((ParameterizedType)object).getRawType();
                continue;
            }
            if (object instanceof GenericArrayType) {
                return Array.newInstance(Utils.getClass(((GenericArrayType)object).getGenericComponentType()), 0).getClass();
            }
            if (object instanceof WildcardType) {
                return null;
            }
            if (!(object instanceof TypeVariable)) break;
            object = ((TypeVariable)object).getBounds();
            object = object[0];
        }
        throw new UnsupportedOperationException("Cannot infer class from type " + object);
    }

    public static Type getParent(Type clazz) {
        while (!(clazz instanceof Class)) {
            clazz = Utils.getClass(clazz);
        }
        return ((Class)clazz).getSuperclass();
    }

    public static Class[] getClasses(Type[] typeArray) {
        int n2 = typeArray.length;
        Class[] classArray = new Class[n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            classArray[i2] = Utils.getClass(typeArray[i2]);
        }
        return classArray;
    }

    public static Type getUniqueParameterizedTypeParameter(Type type) {
        if (type instanceof ParameterizedType) {
            return ((ParameterizedType)type).getActualTypeArguments()[0];
        }
        return null;
    }

    public static boolean parametersComplyToSignature(Object[] objectArray, Class[] classArray) {
        if (objectArray.length != classArray.length) {
            return false;
        }
        int n2 = objectArray.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            Class clazz = classArray[i2];
            Object object = objectArray[i2];
            if (clazz.isInstance(object)) continue;
            return false;
        }
        return true;
    }
}


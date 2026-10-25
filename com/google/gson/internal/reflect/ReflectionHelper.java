/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson.internal.reflect;

import com.google.gson.JsonIOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionHelper {
    private static final RecordHelper RECORD_HELPER;

    private ReflectionHelper() {
    }

    public static void makeAccessible(AccessibleObject object) throws JsonIOException {
        try {
            ((AccessibleObject)object).setAccessible(true);
            return;
        }
        catch (Exception exception) {
            object = ReflectionHelper.getAccessibleObjectDescription((AccessibleObject)object, false);
            throw new JsonIOException("Failed making " + (String)object + " accessible; either increase its visibility or write a custom TypeAdapter for its declaring type.", exception);
        }
    }

    public static String getAccessibleObjectDescription(AccessibleObject object, boolean bl) {
        if (object instanceof Field) {
            object = "field '" + ReflectionHelper.fieldToString((Field)object) + "'";
        } else if (object instanceof Method) {
            object = (Method)object;
            CharSequence charSequence = new StringBuilder(((Method)object).getName());
            ReflectionHelper.appendExecutableParameters((AccessibleObject)object, charSequence);
            charSequence = charSequence.toString();
            object = "method '" + ((Method)object).getDeclaringClass().getName() + "#" + (String)charSequence + "'";
        } else {
            object = object instanceof Constructor ? "constructor '" + ReflectionHelper.constructorToString((Constructor)object) + "'" : "<unknown AccessibleObject> " + object.toString();
        }
        if (bl && Character.isLowerCase(((String)object).charAt(0))) {
            object = Character.toUpperCase(((String)object).charAt(0)) + ((String)object).substring(1);
        }
        return object;
    }

    public static String fieldToString(Field field) {
        return field.getDeclaringClass().getName() + "#" + field.getName();
    }

    public static String constructorToString(Constructor<?> constructor) {
        StringBuilder stringBuilder = new StringBuilder(constructor.getDeclaringClass().getName());
        ReflectionHelper.appendExecutableParameters(constructor, stringBuilder);
        return stringBuilder.toString();
    }

    private static void appendExecutableParameters(AccessibleObject classArray, StringBuilder stringBuilder) {
        stringBuilder.append('(');
        classArray = classArray instanceof Method ? ((Method)classArray).getParameterTypes() : ((Constructor)classArray).getParameterTypes();
        for (int i2 = 0; i2 < classArray.length; ++i2) {
            if (i2 > 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(classArray[i2].getSimpleName());
        }
        stringBuilder.append(')');
    }

    public static String tryMakeAccessible(Constructor<?> constructor) {
        try {
            constructor.setAccessible(true);
            return null;
        }
        catch (Exception exception) {
            return "Failed making constructor '" + ReflectionHelper.constructorToString(constructor) + "' accessible; either increase its visibility or write a custom InstanceCreator or TypeAdapter for its declaring type: " + exception.getMessage();
        }
    }

    public static boolean isRecord(Class<?> clazz) {
        return RECORD_HELPER.isRecord(clazz);
    }

    public static String[] getRecordComponentNames(Class<?> clazz) {
        return RECORD_HELPER.getRecordComponentNames(clazz);
    }

    public static Method getAccessor(Class<?> clazz, Field field) {
        return RECORD_HELPER.getAccessor(clazz, field);
    }

    public static <T> Constructor<T> getCanonicalRecordConstructor(Class<T> clazz) {
        return RECORD_HELPER.getCanonicalRecordConstructor(clazz);
    }

    public static RuntimeException createExceptionForUnexpectedIllegalAccess(IllegalAccessException illegalAccessException) {
        throw new RuntimeException("Unexpected IllegalAccessException occurred (Gson 2.10.1). Certain ReflectionAccessFilter features require Java >= 9 to work correctly. If you are not using ReflectionAccessFilter, report this to the Gson maintainers.", illegalAccessException);
    }

    private static RuntimeException createExceptionForRecordReflectionException(ReflectiveOperationException reflectiveOperationException) {
        throw new RuntimeException("Unexpected ReflectiveOperationException occurred (Gson 2.10.1). To support Java records, reflection is utilized to read out information about records. All these invocations happens after it is established that records exist in the JVM. This exception is unexpected behavior.", reflectiveOperationException);
    }

    static {
        RecordHelper recordHelper;
        try {
            recordHelper = new RecordSupportedHelper();
        }
        catch (NoSuchMethodException noSuchMethodException) {
            recordHelper = new RecordNotSupportedHelper();
        }
        RECORD_HELPER = recordHelper;
    }

    static class RecordNotSupportedHelper
    extends RecordHelper {
        private RecordNotSupportedHelper() {
        }

        @Override
        boolean isRecord(Class<?> clazz) {
            return false;
        }

        @Override
        String[] getRecordComponentNames(Class<?> clazz) {
            throw new UnsupportedOperationException("Records are not supported on this JVM, this method should not be called");
        }

        @Override
        <T> Constructor<T> getCanonicalRecordConstructor(Class<T> clazz) {
            throw new UnsupportedOperationException("Records are not supported on this JVM, this method should not be called");
        }

        @Override
        public Method getAccessor(Class<?> clazz, Field field) {
            throw new UnsupportedOperationException("Records are not supported on this JVM, this method should not be called");
        }
    }

    static class RecordSupportedHelper
    extends RecordHelper {
        private final Method isRecord = Class.class.getMethod("isRecord", new Class[0]);
        private final Method getRecordComponents = Class.class.getMethod("getRecordComponents", new Class[0]);
        private final Method getName;
        private final Method getType;

        private RecordSupportedHelper() throws NoSuchMethodException {
            Class<?> clazz = this.getRecordComponents.getReturnType().getComponentType();
            this.getName = clazz.getMethod("getName", new Class[0]);
            this.getType = clazz.getMethod("getType", new Class[0]);
        }

        @Override
        boolean isRecord(Class<?> serializable) {
            try {
                return (Boolean)this.isRecord.invoke(serializable, new Object[0]);
            }
            catch (ReflectiveOperationException reflectiveOperationException) {
                serializable = reflectiveOperationException;
                throw ReflectionHelper.createExceptionForRecordReflectionException(reflectiveOperationException);
            }
        }

        @Override
        String[] getRecordComponentNames(Class<?> objectArray) {
            try {
                objectArray = (Object[])this.getRecordComponents.invoke(objectArray, new Object[0]);
                String[] stringArray = new String[objectArray.length];
                for (int i2 = 0; i2 < objectArray.length; ++i2) {
                    stringArray[i2] = (String)this.getName.invoke(objectArray[i2], new Object[0]);
                }
                return stringArray;
            }
            catch (ReflectiveOperationException reflectiveOperationException) {
                objectArray = reflectiveOperationException;
                throw ReflectionHelper.createExceptionForRecordReflectionException(reflectiveOperationException);
            }
        }

        @Override
        public <T> Constructor<T> getCanonicalRecordConstructor(Class<T> clazz) {
            try {
                Object[] objectArray = (Object[])this.getRecordComponents.invoke(clazz, new Object[0]);
                Class[] classArray = new Class[objectArray.length];
                for (int i2 = 0; i2 < objectArray.length; ++i2) {
                    classArray[i2] = (Class)this.getType.invoke(objectArray[i2], new Object[0]);
                }
                return clazz.getDeclaredConstructor(classArray);
            }
            catch (ReflectiveOperationException reflectiveOperationException) {
                ReflectiveOperationException reflectiveOperationException2 = reflectiveOperationException;
                throw ReflectionHelper.createExceptionForRecordReflectionException(reflectiveOperationException);
            }
        }

        @Override
        public Method getAccessor(Class<?> serializable, Field field) {
            try {
                return serializable.getMethod(field.getName(), new Class[0]);
            }
            catch (ReflectiveOperationException reflectiveOperationException) {
                serializable = reflectiveOperationException;
                throw ReflectionHelper.createExceptionForRecordReflectionException(reflectiveOperationException);
            }
        }
    }

    static abstract class RecordHelper {
        private RecordHelper() {
        }

        abstract boolean isRecord(Class<?> var1);

        abstract String[] getRecordComponentNames(Class<?> var1);

        abstract <T> Constructor<T> getCanonicalRecordConstructor(Class<T> var1);

        public abstract Method getAccessor(Class<?> var1, Field var2);
    }
}


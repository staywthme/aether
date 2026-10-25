/*
 * Decompiled with CFR 0.152.
 */
package org.bridj.cpp;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import org.bridj.ann.Template;

public class CPPType
implements ParameterizedType {
    private final Type[] actualTypeArguments;
    private final Type ownerType;
    private final Type rawType;
    private final Object[] templateParameters;

    public CPPType(Type type, Type type2, Object ... objectArray) {
        this.ownerType = type;
        this.templateParameters = objectArray == null ? new Object[]{} : objectArray;
        this.actualTypeArguments = CPPType.getTypes(this.templateParameters);
        this.rawType = type2;
    }

    public CPPType(Type type, Object ... objectArray) {
        this(null, type, objectArray);
    }

    private static Type[] getTypes(Object[] objectArray) {
        int n2 = objectArray == null ? 0 : objectArray.length;
        ArrayList<Type> arrayList = new ArrayList<Type>(n2);
        for (int i2 = 0; i2 < n2; ++i2) {
            Object object = objectArray[i2];
            if (!(object instanceof Type)) continue;
            arrayList.add((Type)object);
        }
        ArrayList<Type> arrayList2 = arrayList;
        return arrayList2.toArray(new Type[arrayList2.size()]);
    }

    static Object[] cons(Class clazz, Object ... objectArray) {
        Object[] objectArray2 = new Object[objectArray.length + 1];
        Object[] objectArray3 = objectArray2;
        objectArray2[0] = clazz;
        System.arraycopy(objectArray, 0, objectArray3, 1, objectArray.length);
        return objectArray3;
    }

    public static Type getCPPType(Object ... objectArray) {
        int[] nArray = new int[]{0};
        Type type = CPPType.parseCPPType(objectArray, nArray);
        if (nArray[0] < objectArray.length) {
            CPPType.parseError("Unexpected trailing parameters", objectArray, nArray);
        }
        return type;
    }

    static void parseError(String string, Object[] objectArray, int[] nArray) {
        throw new IllegalArgumentException("Error while parsing C++ type in " + Arrays.asList(objectArray) + " at offset " + nArray[0] + " : " + string);
    }

    static void notEOF(String string, Object[] objectArray, int[] nArray) {
        if (nArray[0] >= objectArray.length) {
            throw new IllegalArgumentException("EOF while parsing C++ type in " + Arrays.asList(objectArray) + " at offset " + nArray[0] + " : " + string);
        }
    }

    static Type parseCPPType(Object[] objectArray, int[] nArray) {
        CPPType.notEOF("expecting class", objectArray, nArray);
        Object object = objectArray[nArray[0]];
        if (!(object instanceof Class)) {
            CPPType.parseError("expected class", objectArray, nArray);
        }
        object = (Class)object;
        nArray[0] = nArray[0] + 1;
        Class<?>[] classArray = ((Class)object).getAnnotation(Template.class);
        classArray = classArray == null ? null : classArray.value();
        int n2 = classArray == null ? 0 : classArray.length;
        Object[] objectArray2 = new Object[n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            CPPType.notEOF("expecting param " + i2 + " for template " + ((Class)object).getName(), objectArray, nArray);
            Object object2 = objectArray[nArray[0]];
            Class<?> clazz = classArray[i2];
            if (clazz.equals(Class.class) && object2.getClass().equals(Class.class)) {
                object2 = CPPType.parseCPPType(objectArray, nArray);
            } else {
                if (!clazz.isInstance(object2)) {
                    CPPType.parseError("bad type for template param " + i2 + " : expected a " + clazz + ", got " + object2, objectArray, nArray);
                }
                nArray[0] = nArray[0] + 1;
            }
            objectArray2[i2] = object2;
        }
        if (n2 == 0) {
            return object;
        }
        return new CPPType((Type)object, objectArray2);
    }

    public Type[] getActualTypeArguments() {
        return (Type[])this.actualTypeArguments.clone();
    }

    public Type getOwnerType() {
        return this.ownerType;
    }

    public Type getRawType() {
        return this.rawType;
    }

    public Object[] getTemplateParameters() {
        return (Object[])this.templateParameters.clone();
    }

    public int hashCode() {
        int n2 = this.getRawType().hashCode();
        if (this.getOwnerType() != null) {
            n2 ^= this.getOwnerType().hashCode();
        }
        int n3 = this.templateParameters.length;
        for (int i2 = 0; i2 < n3; ++i2) {
            n2 ^= this.templateParameters[i2].hashCode();
        }
        return n2;
    }

    static boolean eq(Object object, Object object2) {
        if (object == null != (object2 == null)) {
            return false;
        }
        return object == null || object.equals(object2);
    }

    public boolean equals(Object objectArray) {
        if (objectArray == null || !(objectArray instanceof CPPType)) {
            return false;
        }
        objectArray = (CPPType)objectArray;
        if (!CPPType.eq(this.getRawType(), objectArray.getRawType())) {
            return false;
        }
        if (!CPPType.eq(this.getOwnerType(), objectArray.getOwnerType())) {
            return false;
        }
        objectArray = objectArray.templateParameters;
        if (this.templateParameters.length != objectArray.length) {
            return false;
        }
        int n2 = this.templateParameters.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            if (CPPType.eq(this.templateParameters[i2], objectArray[i2])) continue;
            return false;
        }
        return true;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (this.getOwnerType() != null) {
            stringBuilder.append(this.getOwnerType()).append('.');
        }
        stringBuilder.append(this.getRawType());
        int n2 = this.templateParameters.length;
        if (n2 != 0) {
            stringBuilder.append('<');
            for (int i2 = 0; i2 < n2; ++i2) {
                if (i2 > 0) {
                    stringBuilder.append(", ");
                }
                stringBuilder.append(this.templateParameters[i2]);
            }
            stringBuilder.append('>');
        }
        return stringBuilder.toString();
    }
}


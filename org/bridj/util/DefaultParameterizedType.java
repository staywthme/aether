/*
 * Decompiled with CFR 0.152.
 */
package org.bridj.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import org.bridj.util.Utils;

public class DefaultParameterizedType
implements ParameterizedType {
    private final Type[] actualTypeArguments;
    private final Type ownerType;
    private final Type rawType;

    public DefaultParameterizedType(Type type, Type type2, Type[] typeArray) {
        this.ownerType = type;
        this.actualTypeArguments = typeArray;
        this.rawType = type2;
    }

    public DefaultParameterizedType(Type type, Type ... typeArray) {
        this((Type)null, type, typeArray);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (this.ownerType != null) {
            stringBuilder.append(Utils.toString(this.ownerType)).append(".");
        }
        stringBuilder.append(this.rawType);
        if (this.actualTypeArguments.length > 0) {
            stringBuilder.append("<");
            for (int i2 = 0; i2 < this.actualTypeArguments.length; ++i2) {
                if (i2 > 0) {
                    stringBuilder.append(", ");
                }
                stringBuilder.append(Utils.toString(this.actualTypeArguments[i2]));
            }
            stringBuilder.append(">");
        }
        return stringBuilder.toString();
    }

    public static Type paramType(Type type, Type ... typeArray) {
        return new DefaultParameterizedType(type, typeArray);
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

    public int hashCode() {
        int n2 = this.getRawType().hashCode();
        if (this.getOwnerType() != null) {
            n2 ^= this.getOwnerType().hashCode();
        }
        for (Type type : this.actualTypeArguments) {
            if (type == null) continue;
            n2 ^= type.hashCode();
        }
        return n2;
    }

    static boolean eq(Object object, Object object2) {
        if (object == null != (object2 == null)) {
            return false;
        }
        return object == null || object.equals(object2);
    }

    public boolean equals(Object typeArray) {
        if (typeArray == null || !(typeArray instanceof DefaultParameterizedType)) {
            return false;
        }
        typeArray = (DefaultParameterizedType)typeArray;
        if (!DefaultParameterizedType.eq(this.getRawType(), typeArray.getRawType())) {
            return false;
        }
        if (!DefaultParameterizedType.eq(this.getOwnerType(), typeArray.getOwnerType())) {
            return false;
        }
        typeArray = typeArray.actualTypeArguments;
        if (this.actualTypeArguments.length != typeArray.length) {
            return false;
        }
        int n2 = this.actualTypeArguments.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            if (DefaultParameterizedType.eq(this.actualTypeArguments[i2], typeArray[i2])) continue;
            return false;
        }
        return true;
    }
}


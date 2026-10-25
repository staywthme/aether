/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson.reflect;

import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TypeToken<T> {
    private final Class<? super T> rawType;
    private final Type type;
    private final int hashCode;

    protected TypeToken() {
        this.type = this.getTypeTokenTypeArgument();
        this.rawType = $Gson$Types.getRawType(this.type);
        this.hashCode = this.type.hashCode();
    }

    private TypeToken(Type type) {
        this.type = $Gson$Types.canonicalize(Objects.requireNonNull(type));
        this.rawType = $Gson$Types.getRawType(this.type);
        this.hashCode = this.type.hashCode();
    }

    private Type getTypeTokenTypeArgument() {
        Type type = this.getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            if ((type = (ParameterizedType)type).getRawType() == TypeToken.class) {
                return $Gson$Types.canonicalize(type.getActualTypeArguments()[0]);
            }
        } else if (type == TypeToken.class) {
            throw new IllegalStateException("TypeToken must be created with a type argument: new TypeToken<...>() {}; When using code shrinkers (ProGuard, R8, ...) make sure that generic signatures are preserved.");
        }
        throw new IllegalStateException("Must only create direct subclasses of TypeToken");
    }

    public final Class<? super T> getRawType() {
        return this.rawType;
    }

    public final Type getType() {
        return this.type;
    }

    @Deprecated
    public boolean isAssignableFrom(Class<?> clazz) {
        return this.isAssignableFrom((Type)clazz);
    }

    @Deprecated
    public boolean isAssignableFrom(Type type) {
        if (type == null) {
            return false;
        }
        if (this.type.equals(type)) {
            return true;
        }
        if (this.type instanceof Class) {
            return this.rawType.isAssignableFrom($Gson$Types.getRawType(type));
        }
        if (this.type instanceof ParameterizedType) {
            return TypeToken.isAssignableFrom(type, (ParameterizedType)this.type, new HashMap<String, Type>());
        }
        if (this.type instanceof GenericArrayType) {
            return this.rawType.isAssignableFrom($Gson$Types.getRawType(type)) && TypeToken.isAssignableFrom(type, (GenericArrayType)this.type);
        }
        throw TypeToken.buildUnexpectedTypeError(this.type, Class.class, ParameterizedType.class, GenericArrayType.class);
    }

    @Deprecated
    public boolean isAssignableFrom(TypeToken<?> typeToken) {
        return this.isAssignableFrom(typeToken.getType());
    }

    private static boolean isAssignableFrom(Type clazz, GenericArrayType type) {
        if ((type = type.getGenericComponentType()) instanceof ParameterizedType) {
            Class<?> clazz2 = clazz;
            if (clazz instanceof GenericArrayType) {
                clazz2 = ((GenericArrayType)((Object)clazz)).getGenericComponentType();
            } else if (clazz instanceof Class) {
                clazz = clazz;
                while (clazz.isArray()) {
                    clazz = clazz.getComponentType();
                }
                clazz2 = clazz;
            }
            return TypeToken.isAssignableFrom(clazz2, (ParameterizedType)type, new HashMap<String, Type>());
        }
        return true;
    }

    /*
     * WARNING - void declaration
     */
    private static boolean isAssignableFrom(Type object, ParameterizedType parameterizedType, Map<String, Type> map) {
        void var0_1;
        while (var0_1 != null) {
            HashMap<String, Type> hashMap;
            Type type;
            int n2;
            void var1_7;
            if (var1_7.equals(var0_1)) {
                return true;
            }
            Class<?> clazz = $Gson$Types.getRawType((Type)var0_1);
            ParameterizedType parameterizedType2 = null;
            if (var0_1 instanceof ParameterizedType) {
                parameterizedType2 = (ParameterizedType)var0_1;
            }
            if (parameterizedType2 != null) {
                Type[] typeArray = parameterizedType2.getActualTypeArguments();
                TypeVariable<Class<?>>[] typeVariableArray = clazz.getTypeParameters();
                for (n2 = 0; n2 < typeArray.length; ++n2) {
                    type = typeArray[n2];
                    TypeVariable<Class<?>> typeVariable = typeVariableArray[n2];
                    while (type instanceof TypeVariable) {
                        type = (TypeVariable)type;
                        type = (Type)hashMap.get(type.getName());
                    }
                    hashMap.put(typeVariable.getName(), type);
                }
                if (TypeToken.typeEquals(parameterizedType2, (ParameterizedType)var1_7, (Map<String, Type>)hashMap)) {
                    return true;
                }
            }
            Type[] typeArray = clazz.getGenericInterfaces();
            int n3 = typeArray.length;
            for (n2 = 0; n2 < n3; ++n2) {
                type = typeArray[n2];
                if (!TypeToken.isAssignableFrom(type, (ParameterizedType)var1_7, new HashMap<String, Type>(hashMap))) continue;
                return true;
            }
            Type type2 = clazz.getGenericSuperclass();
            hashMap = new HashMap<String, Type>(hashMap);
            Type type3 = type2;
        }
        return false;
    }

    private static boolean typeEquals(ParameterizedType typeArray, ParameterizedType typeArray2, Map<String, Type> map) {
        if (typeArray.getRawType().equals(typeArray2.getRawType())) {
            typeArray = typeArray.getActualTypeArguments();
            typeArray2 = typeArray2.getActualTypeArguments();
            for (int i2 = 0; i2 < typeArray.length; ++i2) {
                if (TypeToken.matches(typeArray[i2], typeArray2[i2], map)) continue;
                return false;
            }
            return true;
        }
        return false;
    }

    private static AssertionError buildUnexpectedTypeError(Type type, Class<?> ... classArray) {
        StringBuilder stringBuilder = new StringBuilder("Unexpected type. Expected one of: ");
        for (Class<?> clazz : classArray) {
            stringBuilder.append(clazz.getName()).append(", ");
        }
        stringBuilder.append("but got: ").append(type.getClass().getName()).append(", for type token: ").append(type.toString()).append('.');
        return new AssertionError((Object)stringBuilder.toString());
    }

    private static boolean matches(Type type, Type type2, Map<String, Type> map) {
        return type2.equals(type) || type instanceof TypeVariable && type2.equals(map.get(((TypeVariable)type).getName()));
    }

    public final int hashCode() {
        return this.hashCode;
    }

    public final boolean equals(Object object) {
        return object instanceof TypeToken && $Gson$Types.equals(this.type, ((TypeToken)object).type);
    }

    public final String toString() {
        return $Gson$Types.typeToString(this.type);
    }

    public static TypeToken<?> get(Type type) {
        return new TypeToken(type);
    }

    public static <T> TypeToken<T> get(Class<T> clazz) {
        return new TypeToken<T>(clazz);
    }

    public static TypeToken<?> getParameterized(Type type, Type ... typeArray) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(typeArray);
        if (!(type instanceof Class)) {
            throw new IllegalArgumentException("rawType must be of type Class, but was " + type);
        }
        int n2 = typeArray.length;
        Class clazz = (Class)type;
        TypeVariable<Class<T>>[] typeVariableArray = clazz.getTypeParameters();
        int n3 = typeVariableArray.length;
        if (n2 != n3) {
            throw new IllegalArgumentException(clazz.getName() + " requires " + n3 + " type arguments, but got " + n2);
        }
        for (int i2 = 0; i2 < n3; ++i2) {
            Type type2 = typeArray[i2];
            Class<?> clazz2 = $Gson$Types.getRawType(type2);
            TypeVariable typeVariable = typeVariableArray[i2];
            for (Type type3 : typeVariable.getBounds()) {
                Class<?> clazz3 = $Gson$Types.getRawType(type3);
                if (clazz3.isAssignableFrom(clazz2)) continue;
                throw new IllegalArgumentException("Type argument " + type2 + " does not satisfy bounds for type variable " + typeVariable + " declared by " + type);
            }
        }
        return new TypeToken($Gson$Types.newParameterizedTypeWithOwner(null, type, typeArray));
    }

    public static TypeToken<?> getArray(Type type) {
        return new TypeToken($Gson$Types.arrayOf(type));
    }
}


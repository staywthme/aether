/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson.internal;

import com.google.gson.internal.$Gson$Preconditions;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Properties;

public final class $Gson$Types {
    static final Type[] EMPTY_TYPE_ARRAY = new Type[0];

    private $Gson$Types() {
        throw new UnsupportedOperationException();
    }

    public static ParameterizedType newParameterizedTypeWithOwner(Type type, Type type2, Type ... typeArray) {
        return new ParameterizedTypeImpl(type, type2, typeArray);
    }

    public static GenericArrayType arrayOf(Type type) {
        return new GenericArrayTypeImpl(type);
    }

    public static WildcardType subtypeOf(Type typeArray) {
        typeArray = typeArray instanceof WildcardType ? ((WildcardType)typeArray).getUpperBounds() : new Type[]{typeArray};
        return new WildcardTypeImpl(typeArray, EMPTY_TYPE_ARRAY);
    }

    public static WildcardType supertypeOf(Type typeArray) {
        typeArray = typeArray instanceof WildcardType ? ((WildcardType)typeArray).getLowerBounds() : new Type[]{typeArray};
        return new WildcardTypeImpl(new Type[]{Object.class}, typeArray);
    }

    public static Type canonicalize(Type type) {
        if (type instanceof Class) {
            return ((Class)(type = (Class)type)).isArray() ? new GenericArrayTypeImpl($Gson$Types.canonicalize(((Class)type).getComponentType())) : type;
        }
        if (type instanceof ParameterizedType) {
            type = (ParameterizedType)type;
            return new ParameterizedTypeImpl(type.getOwnerType(), type.getRawType(), type.getActualTypeArguments());
        }
        if (type instanceof GenericArrayType) {
            type = (GenericArrayType)type;
            return new GenericArrayTypeImpl(type.getGenericComponentType());
        }
        if (type instanceof WildcardType) {
            type = (WildcardType)type;
            return new WildcardTypeImpl(type.getUpperBounds(), type.getLowerBounds());
        }
        return type;
    }

    public static Class<?> getRawType(Type type) {
        Object object;
        while (true) {
            if (type instanceof Class) {
                return (Class)type;
            }
            if (type instanceof ParameterizedType) {
                object = (ParameterizedType)type;
                type = object.getRawType();
                $Gson$Preconditions.checkArgument(type instanceof Class);
                return (Class)type;
            }
            if (type instanceof GenericArrayType) {
                object = ((GenericArrayType)type).getGenericComponentType();
                return Array.newInstance($Gson$Types.getRawType((Type)object), 0).getClass();
            }
            if (type instanceof TypeVariable) {
                return Object.class;
            }
            if (!(type instanceof WildcardType)) break;
            object = ((WildcardType)type).getUpperBounds();
            assert (((Type[])object).length == 1);
            type = object[0];
        }
        object = type == null ? "null" : type.getClass().getName();
        throw new IllegalArgumentException("Expected a Class, ParameterizedType, or GenericArrayType, but <" + type + "> is of type " + (String)object);
    }

    private static boolean equal(Object object, Object object2) {
        return Objects.equals(object, object2);
    }

    public static boolean equals(Type type, Type type2) {
        while (true) {
            if (type == type2) {
                return true;
            }
            if (type instanceof Class) {
                return type.equals(type2);
            }
            if (type instanceof ParameterizedType) {
                if (!(type2 instanceof ParameterizedType)) {
                    return false;
                }
                type = (ParameterizedType)type;
                type2 = (ParameterizedType)type2;
                return $Gson$Types.equal(type.getOwnerType(), type2.getOwnerType()) && type.getRawType().equals(type2.getRawType()) && Arrays.equals(type.getActualTypeArguments(), type2.getActualTypeArguments());
            }
            if (!(type instanceof GenericArrayType)) break;
            if (!(type2 instanceof GenericArrayType)) {
                return false;
            }
            type = (GenericArrayType)type;
            type2 = (GenericArrayType)type2;
            type2 = type2.getGenericComponentType();
            type = type.getGenericComponentType();
        }
        if (type instanceof WildcardType) {
            if (!(type2 instanceof WildcardType)) {
                return false;
            }
            type = (WildcardType)type;
            type2 = (WildcardType)type2;
            return Arrays.equals(type.getUpperBounds(), type2.getUpperBounds()) && Arrays.equals(type.getLowerBounds(), type2.getLowerBounds());
        }
        if (type instanceof TypeVariable) {
            if (!(type2 instanceof TypeVariable)) {
                return false;
            }
            type = (TypeVariable)type;
            type2 = (TypeVariable)type2;
            return type.getGenericDeclaration() == type2.getGenericDeclaration() && type.getName().equals(type2.getName());
        }
        return false;
    }

    public static String typeToString(Type type) {
        if (type instanceof Class) {
            return ((Class)type).getName();
        }
        return type.toString();
    }

    private static Type getGenericSupertype(Type object, Class<?> object2, Class<?> clazz) {
        block0: while (true) {
            if (clazz == object2) {
                return object;
            }
            if (clazz.isInterface()) {
                object = ((Class)object2).getInterfaces();
                int n2 = ((Class<?>[])object).length;
                for (int i2 = 0; i2 < n2; ++i2) {
                    if (object[i2] == clazz) {
                        return ((Class)object2).getGenericInterfaces()[i2];
                    }
                    if (!clazz.isAssignableFrom(object[i2])) continue;
                    Type type = ((Class)object2).getGenericInterfaces()[i2];
                    object2 = object[i2];
                    object = type;
                    continue block0;
                }
            }
            if (((Class)object2).isInterface()) break;
            while (object2 != Object.class) {
                object = ((Class)object2).getSuperclass();
                if (object == clazz) {
                    return ((Class)object2).getGenericSuperclass();
                }
                if (clazz.isAssignableFrom((Class<?>)object)) {
                    Type type = ((Class)object2).getGenericSuperclass();
                    object2 = object;
                    object = type;
                    continue block0;
                }
                object2 = object;
            }
            break;
        }
        return clazz;
    }

    private static Type getSupertype(Type object, Class<?> clazz, Class<?> clazz2) {
        if (object instanceof WildcardType) {
            object = ((WildcardType)object).getUpperBounds();
            assert (((Type[])object).length == 1);
            object = object[0];
        }
        $Gson$Preconditions.checkArgument(clazz2.isAssignableFrom(clazz));
        return $Gson$Types.resolve((Type)object, clazz, $Gson$Types.getGenericSupertype((Type)object, clazz, clazz2));
    }

    public static Type getArrayComponentType(Type type) {
        if (type instanceof GenericArrayType) {
            return ((GenericArrayType)type).getGenericComponentType();
        }
        return ((Class)type).getComponentType();
    }

    public static Type getCollectionElementType(Type type, Class<?> clazz) {
        if ((type = $Gson$Types.getSupertype(type, clazz, Collection.class)) instanceof ParameterizedType) {
            return ((ParameterizedType)type).getActualTypeArguments()[0];
        }
        return Object.class;
    }

    public static Type[] getMapKeyAndValueTypes(Type type, Class<?> clazz) {
        if (type == Properties.class) {
            return new Type[]{String.class, String.class};
        }
        if ((type = $Gson$Types.getSupertype(type, clazz, Map.class)) instanceof ParameterizedType) {
            type = (ParameterizedType)type;
            return type.getActualTypeArguments();
        }
        return new Type[]{Object.class, Object.class};
    }

    public static Type resolve(Type type, Class<?> clazz, Type type2) {
        return $Gson$Types.resolve(type, clazz, type2, new HashMap());
    }

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    private static Type resolve(Type var0, Class<?> var1_1, Type var2_2, Map<TypeVariable<?>, Type> var3_3) {
        block10: {
            block14: {
                block13: {
                    block12: {
                        block11: {
                            var4_4 = null;
                            while (var2_2 /* !! */  instanceof TypeVariable) {
                                var5_5 = (TypeVariable)var2_2 /* !! */ ;
                                var6_6 /* !! */  = var3_3.get(var5_5);
                                if (var6_6 /* !! */  != null) {
                                    if (var6_6 /* !! */  == Void.TYPE) {
                                        return var2_2 /* !! */ ;
                                    }
                                    return var6_6 /* !! */ ;
                                }
                                var3_3.put((TypeVariable<?>)var5_5, Void.TYPE);
                                if (var4_4 == null) {
                                    var4_4 = var5_5;
                                }
                                if ((var2_2 /* !! */  = $Gson$Types.resolveTypeVariable(var0, var1_1, var5_5)) != var5_5) {
                                    continue;
                                }
                                break block10;
                            }
                            if (!(var2_2 /* !! */  instanceof Class) || !((Class)var2_2 /* !! */ ).isArray()) break block11;
                            var5_5 = (Class)var2_2 /* !! */ ;
                            var6_6 /* !! */  = var5_5.getComponentType();
                            var2_2 /* !! */  = $Gson$Types.equal(var6_6 /* !! */ , var2_2 /* !! */  = $Gson$Types.resolve(var0, var1_1, var6_6 /* !! */ , var3_3)) != false ? var5_5 : $Gson$Types.arrayOf((Type)var2_2 /* !! */ );
                            break block10;
                        }
                        if (!(var2_2 /* !! */  instanceof GenericArrayType)) break block12;
                        var5_5 = (GenericArrayType)var2_2 /* !! */ ;
                        var6_6 /* !! */  = var5_5.getGenericComponentType();
                        var2_2 /* !! */  = $Gson$Types.equal(var6_6 /* !! */ , var2_2 /* !! */  = $Gson$Types.resolve(var0, var1_1, var6_6 /* !! */ , var3_3)) != false ? var5_5 : $Gson$Types.arrayOf((Type)var2_2 /* !! */ );
                        break block10;
                    }
                    if (!(var2_2 /* !! */  instanceof ParameterizedType)) break block13;
                    var5_5 = (ParameterizedType)var2_2 /* !! */ ;
                    var6_6 /* !! */  = var5_5.getOwnerType();
                    var7_7 = $Gson$Types.equal(var2_2 /* !! */  = $Gson$Types.resolve(var0, var1_1, var6_6 /* !! */ , var3_3), var6_6 /* !! */ ) == false;
                    var6_6 /* !! */  = var5_5.getActualTypeArguments();
                    var9_11 = var6_6 /* !! */ .length;
                    for (var8_10 = 0; var8_10 < var9_11; ++var8_10) {
                        var10_12 = $Gson$Types.resolve(var0, var1_1, var6_6 /* !! */ [var8_10], var3_3);
                        if ($Gson$Types.equal(var10_12, var6_6 /* !! */ [var8_10])) continue;
                        if (!var7_7) {
                            var6_6 /* !! */  = (Type[])var6_6 /* !! */ .clone();
                            var7_7 = true;
                        }
                        var6_6 /* !! */ [var8_10] = var10_12;
                    }
                    var2_2 /* !! */  = var7_7 != false ? $Gson$Types.newParameterizedTypeWithOwner((Type)var2_2 /* !! */ , var5_5.getRawType(), var6_6 /* !! */ ) : var5_5;
                    break block10;
                }
                if (!(var2_2 /* !! */  instanceof WildcardType)) break block10;
                var5_5 = (WildcardType)var2_2 /* !! */ ;
                var6_6 /* !! */  = var5_5.getLowerBounds();
                var2_2 /* !! */  = var5_5.getUpperBounds();
                if (var6_6 /* !! */ .length != 1) break block14;
                var7_8 = $Gson$Types.resolve(var0, var1_1, var6_6 /* !! */ [0], var3_3);
                if (var7_8 == var6_6 /* !! */ [0]) ** GOTO lbl-1000
                var2_2 /* !! */  = $Gson$Types.supertypeOf(var7_8);
                break block10;
            }
            if (var2_2 /* !! */ .length == 1 && (var7_9 = $Gson$Types.resolve(var0, var1_1, var2_2 /* !! */ [0], var3_3)) != var2_2 /* !! */ [0]) {
                var2_2 /* !! */  = $Gson$Types.subtypeOf(var7_9);
            } else lbl-1000:
            // 2 sources

            {
                var2_2 /* !! */  = var5_5;
            }
        }
        if (var4_4 != null) {
            var3_3.put((TypeVariable<?>)var4_4, (Type)var2_2 /* !! */ );
        }
        return var2_2 /* !! */ ;
    }

    private static Type resolveTypeVariable(Type type, Class<?> clazz, TypeVariable<?> typeVariable) {
        Class<?> clazz2 = $Gson$Types.declaringClassOf(typeVariable);
        if (clazz2 == null) {
            return typeVariable;
        }
        if ((type = $Gson$Types.getGenericSupertype(type, clazz, clazz2)) instanceof ParameterizedType) {
            int n2 = $Gson$Types.indexOf(clazz2.getTypeParameters(), typeVariable);
            return ((ParameterizedType)type).getActualTypeArguments()[n2];
        }
        return typeVariable;
    }

    private static int indexOf(Object[] objectArray, Object object) {
        int n2 = objectArray.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            if (!object.equals(objectArray[i2])) continue;
            return i2;
        }
        throw new NoSuchElementException();
    }

    private static Class<?> declaringClassOf(TypeVariable<?> typeVariable) {
        if ((typeVariable = typeVariable.getGenericDeclaration()) instanceof Class) {
            return (Class)((Object)typeVariable);
        }
        return null;
    }

    static void checkNotPrimitive(Type type) {
        $Gson$Preconditions.checkArgument(!(type instanceof Class) || !((Class)type).isPrimitive());
    }

    static final class WildcardTypeImpl
    implements Serializable,
    WildcardType {
        private final Type upperBound;
        private final Type lowerBound;
        private static final long serialVersionUID = 0L;

        public WildcardTypeImpl(Type[] typeArray, Type[] typeArray2) {
            $Gson$Preconditions.checkArgument(typeArray2.length <= 1);
            $Gson$Preconditions.checkArgument(typeArray.length == 1);
            if (typeArray2.length == 1) {
                Objects.requireNonNull(typeArray2[0]);
                $Gson$Types.checkNotPrimitive(typeArray2[0]);
                $Gson$Preconditions.checkArgument(typeArray[0] == Object.class);
                this.lowerBound = $Gson$Types.canonicalize(typeArray2[0]);
                this.upperBound = Object.class;
                return;
            }
            Objects.requireNonNull(typeArray[0]);
            $Gson$Types.checkNotPrimitive(typeArray[0]);
            this.lowerBound = null;
            this.upperBound = $Gson$Types.canonicalize(typeArray[0]);
        }

        @Override
        public final Type[] getUpperBounds() {
            return new Type[]{this.upperBound};
        }

        @Override
        public final Type[] getLowerBounds() {
            if (this.lowerBound != null) {
                return new Type[]{this.lowerBound};
            }
            return EMPTY_TYPE_ARRAY;
        }

        public final boolean equals(Object object) {
            return object instanceof WildcardType && $Gson$Types.equals(this, (WildcardType)object);
        }

        public final int hashCode() {
            return (this.lowerBound != null ? 31 + this.lowerBound.hashCode() : 1) ^ 31 + this.upperBound.hashCode();
        }

        public final String toString() {
            if (this.lowerBound != null) {
                return "? super " + $Gson$Types.typeToString(this.lowerBound);
            }
            if (this.upperBound == Object.class) {
                return "?";
            }
            return "? extends " + $Gson$Types.typeToString(this.upperBound);
        }
    }

    static final class GenericArrayTypeImpl
    implements Serializable,
    GenericArrayType {
        private final Type componentType;
        private static final long serialVersionUID = 0L;

        public GenericArrayTypeImpl(Type type) {
            Objects.requireNonNull(type);
            this.componentType = $Gson$Types.canonicalize(type);
        }

        @Override
        public final Type getGenericComponentType() {
            return this.componentType;
        }

        public final boolean equals(Object object) {
            return object instanceof GenericArrayType && $Gson$Types.equals(this, (GenericArrayType)object);
        }

        public final int hashCode() {
            return this.componentType.hashCode();
        }

        public final String toString() {
            return $Gson$Types.typeToString(this.componentType) + "[]";
        }
    }

    static final class ParameterizedTypeImpl
    implements Serializable,
    ParameterizedType {
        private final Type ownerType;
        private final Type rawType;
        private final Type[] typeArguments;
        private static final long serialVersionUID = 0L;

        public ParameterizedTypeImpl(Type type, Type type2, Type ... typeArray) {
            int n2;
            Objects.requireNonNull(type2);
            if (type2 instanceof Class) {
                Class clazz = (Class)type2;
                n2 = Modifier.isStatic(clazz.getModifiers()) || clazz.getEnclosingClass() == null ? 1 : 0;
                $Gson$Preconditions.checkArgument(type != null || n2 != 0);
            }
            this.ownerType = type == null ? null : $Gson$Types.canonicalize(type);
            this.rawType = $Gson$Types.canonicalize(type2);
            this.typeArguments = (Type[])typeArray.clone();
            n2 = this.typeArguments.length;
            for (int i2 = 0; i2 < n2; ++i2) {
                Objects.requireNonNull(this.typeArguments[i2]);
                $Gson$Types.checkNotPrimitive(this.typeArguments[i2]);
                this.typeArguments[i2] = $Gson$Types.canonicalize(this.typeArguments[i2]);
            }
        }

        @Override
        public final Type[] getActualTypeArguments() {
            return (Type[])this.typeArguments.clone();
        }

        @Override
        public final Type getRawType() {
            return this.rawType;
        }

        @Override
        public final Type getOwnerType() {
            return this.ownerType;
        }

        public final boolean equals(Object object) {
            return object instanceof ParameterizedType && $Gson$Types.equals(this, (ParameterizedType)object);
        }

        private static int hashCodeOrZero(Object object) {
            if (object != null) {
                return object.hashCode();
            }
            return 0;
        }

        public final int hashCode() {
            return Arrays.hashCode(this.typeArguments) ^ this.rawType.hashCode() ^ ParameterizedTypeImpl.hashCodeOrZero(this.ownerType);
        }

        public final String toString() {
            int n2 = this.typeArguments.length;
            if (n2 == 0) {
                return $Gson$Types.typeToString(this.rawType);
            }
            StringBuilder stringBuilder = new StringBuilder(30 * (n2 + 1));
            stringBuilder.append($Gson$Types.typeToString(this.rawType)).append("<").append($Gson$Types.typeToString(this.typeArguments[0]));
            for (int i2 = 1; i2 < n2; ++i2) {
                stringBuilder.append(", ").append($Gson$Types.typeToString(this.typeArguments[i2]));
            }
            return stringBuilder.append(">").toString();
        }
    }
}


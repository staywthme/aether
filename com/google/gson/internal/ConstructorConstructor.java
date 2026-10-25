/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson.internal;

import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.ReflectionAccessFilterHelper;
import com.google.gson.internal.UnsafeAllocator;
import com.google.gson.internal.reflect.ReflectionHelper;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

public final class ConstructorConstructor {
    private final Map<Type, InstanceCreator<?>> instanceCreators;
    private final boolean useJdkUnsafe;
    private final List<ReflectionAccessFilter> reflectionFilters;

    public ConstructorConstructor(Map<Type, InstanceCreator<?>> map, boolean bl, List<ReflectionAccessFilter> list2) {
        this.instanceCreators = map;
        this.useJdkUnsafe = bl;
        this.reflectionFilters = list2;
    }

    static String checkInstantiable(Class<?> clazz) {
        int n2 = clazz.getModifiers();
        if (Modifier.isInterface(n2)) {
            return "Interfaces can't be instantiated! Register an InstanceCreator or a TypeAdapter for this type. Interface name: " + clazz.getName();
        }
        if (Modifier.isAbstract(n2)) {
            return "Abstract classes can't be instantiated! Register an InstanceCreator or a TypeAdapter for this type. Class name: " + clazz.getName();
        }
        return null;
    }

    public final <T> ObjectConstructor<T> get(TypeToken<T> object) {
        Object object2 = ((TypeToken)object).getType();
        object = ((TypeToken)object).getRawType();
        Object object3 = this.instanceCreators.get(object2);
        if (object3 != null) {
            return new ObjectConstructor<T>((InstanceCreator)object3, (Type)object2){
                final /* synthetic */ InstanceCreator val$typeCreator;
                final /* synthetic */ Type val$type;
                {
                    this.val$typeCreator = instanceCreator;
                    this.val$type = type;
                }

                @Override
                public T construct() {
                    return this.val$typeCreator.createInstance(this.val$type);
                }
            };
        }
        object3 = this.instanceCreators.get(object);
        if (object3 != null) {
            return new ObjectConstructor<T>((InstanceCreator)object3, (Type)object2){
                final /* synthetic */ InstanceCreator val$rawTypeCreator;
                final /* synthetic */ Type val$type;
                {
                    this.val$rawTypeCreator = instanceCreator;
                    this.val$type = type;
                }

                @Override
                public T construct() {
                    return this.val$rawTypeCreator.createInstance(this.val$type);
                }
            };
        }
        object3 = ConstructorConstructor.newSpecialCollectionConstructor((Type)object2, object);
        if (object3 != null) {
            return object3;
        }
        object3 = ReflectionAccessFilterHelper.getFilterResult(this.reflectionFilters, object);
        ObjectConstructor<T> objectConstructor = ConstructorConstructor.newDefaultConstructor(object, (ReflectionAccessFilter.FilterResult)((Object)object3));
        if (objectConstructor != null) {
            return objectConstructor;
        }
        if ((object2 = ConstructorConstructor.newDefaultImplementationConstructor((Type)object2, object)) != null) {
            return object2;
        }
        object2 = ConstructorConstructor.checkInstantiable(object);
        if (object2 != null) {
            return new ObjectConstructor<T>((String)object2){
                final /* synthetic */ String val$exceptionMessage;
                {
                    this.val$exceptionMessage = string;
                }

                @Override
                public T construct() {
                    throw new JsonIOException(this.val$exceptionMessage);
                }
            };
        }
        if (object3 == ReflectionAccessFilter.FilterResult.ALLOW) {
            return this.newUnsafeAllocator((Class<? super T>)object);
        }
        object = "Unable to create instance of " + object + "; ReflectionAccessFilter does not permit using reflection or Unsafe. Register an InstanceCreator or a TypeAdapter for this type or adjust the access filter to allow using reflection.";
        return new ObjectConstructor<T>((String)object){
            final /* synthetic */ String val$message;
            {
                this.val$message = string;
            }

            @Override
            public T construct() {
                throw new JsonIOException(this.val$message);
            }
        };
    }

    private static <T> ObjectConstructor<T> newSpecialCollectionConstructor(final Type type, Class<? super T> clazz) {
        if (EnumSet.class.isAssignableFrom(clazz)) {
            return new ObjectConstructor<T>(){

                @Override
                public T construct() {
                    if (type instanceof ParameterizedType) {
                        Object object = ((ParameterizedType)type).getActualTypeArguments()[0];
                        if (object instanceof Class) {
                            object = EnumSet.noneOf((Class)object);
                            return object;
                        }
                        throw new JsonIOException("Invalid EnumSet type: " + type.toString());
                    }
                    throw new JsonIOException("Invalid EnumSet type: " + type.toString());
                }
            };
        }
        if (clazz == EnumMap.class) {
            return new ObjectConstructor<T>(){

                @Override
                public T construct() {
                    if (type instanceof ParameterizedType) {
                        Object object = ((ParameterizedType)type).getActualTypeArguments()[0];
                        if (object instanceof Class) {
                            object = new EnumMap((Class)object);
                            return object;
                        }
                        throw new JsonIOException("Invalid EnumMap type: " + type.toString());
                    }
                    throw new JsonIOException("Invalid EnumMap type: " + type.toString());
                }
            };
        }
        return null;
    }

    private static <T> ObjectConstructor<T> newDefaultConstructor(Class<? super T> object, ReflectionAccessFilter.FilterResult filterResult) {
        Constructor<T> constructor;
        if (Modifier.isAbstract(((Class)object).getModifiers())) {
            return null;
        }
        try {
            constructor = ((Class)object).getDeclaredConstructor(new Class[0]);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            return null;
        }
        boolean bl = filterResult == ReflectionAccessFilter.FilterResult.ALLOW || ReflectionAccessFilterHelper.canAccess(constructor, null) && (filterResult != ReflectionAccessFilter.FilterResult.BLOCK_ALL || Modifier.isPublic(constructor.getModifiers()));
        if (!bl) {
            object = "Unable to invoke no-args constructor of " + object + "; constructor is not accessible and ReflectionAccessFilter does not permit making it accessible. Register an InstanceCreator or a TypeAdapter for this type, change the visibility of the constructor or adjust the access filter.";
            return new ObjectConstructor<T>((String)object){
                final /* synthetic */ String val$message;
                {
                    this.val$message = string;
                }

                @Override
                public T construct() {
                    throw new JsonIOException(this.val$message);
                }
            };
        }
        if (filterResult == ReflectionAccessFilter.FilterResult.ALLOW && (object = ReflectionHelper.tryMakeAccessible(constructor)) != null) {
            return new ObjectConstructor<T>((String)object){
                final /* synthetic */ String val$exceptionMessage;
                {
                    this.val$exceptionMessage = string;
                }

                @Override
                public T construct() {
                    throw new JsonIOException(this.val$exceptionMessage);
                }
            };
        }
        return new ObjectConstructor<T>(){

            @Override
            public T construct() {
                try {
                    Object t2 = constructor.newInstance(new Object[0]);
                    return t2;
                }
                catch (InstantiationException instantiationException) {
                    throw new RuntimeException("Failed to invoke constructor '" + ReflectionHelper.constructorToString(constructor) + "' with no args", instantiationException);
                }
                catch (InvocationTargetException invocationTargetException) {
                    throw new RuntimeException("Failed to invoke constructor '" + ReflectionHelper.constructorToString(constructor) + "' with no args", invocationTargetException.getCause());
                }
                catch (IllegalAccessException illegalAccessException) {
                    IllegalAccessException illegalAccessException2 = illegalAccessException;
                    throw ReflectionHelper.createExceptionForUnexpectedIllegalAccess(illegalAccessException);
                }
            }
        };
    }

    private static <T> ObjectConstructor<T> newDefaultImplementationConstructor(Type type, Class<? super T> clazz) {
        if (Collection.class.isAssignableFrom(clazz)) {
            if (SortedSet.class.isAssignableFrom(clazz)) {
                return new ObjectConstructor<T>(){

                    @Override
                    public T construct() {
                        return new TreeSet();
                    }
                };
            }
            if (Set.class.isAssignableFrom(clazz)) {
                return new ObjectConstructor<T>(){

                    @Override
                    public T construct() {
                        return new LinkedHashSet();
                    }
                };
            }
            if (Queue.class.isAssignableFrom(clazz)) {
                return new ObjectConstructor<T>(){

                    @Override
                    public T construct() {
                        return new ArrayDeque();
                    }
                };
            }
            return new ObjectConstructor<T>(){

                @Override
                public T construct() {
                    return new ArrayList();
                }
            };
        }
        if (Map.class.isAssignableFrom(clazz)) {
            if (ConcurrentNavigableMap.class.isAssignableFrom(clazz)) {
                return new ObjectConstructor<T>(){

                    @Override
                    public T construct() {
                        return new ConcurrentSkipListMap();
                    }
                };
            }
            if (ConcurrentMap.class.isAssignableFrom(clazz)) {
                return new ObjectConstructor<T>(){

                    @Override
                    public T construct() {
                        return new ConcurrentHashMap();
                    }
                };
            }
            if (SortedMap.class.isAssignableFrom(clazz)) {
                return new ObjectConstructor<T>(){

                    @Override
                    public T construct() {
                        return new TreeMap();
                    }
                };
            }
            if (type instanceof ParameterizedType && !String.class.isAssignableFrom(TypeToken.get(((ParameterizedType)type).getActualTypeArguments()[0]).getRawType())) {
                return new ObjectConstructor<T>(){

                    @Override
                    public T construct() {
                        return new LinkedHashMap();
                    }
                };
            }
            return new ObjectConstructor<T>(){

                @Override
                public T construct() {
                    return new LinkedTreeMap();
                }
            };
        }
        return null;
    }

    private <T> ObjectConstructor<T> newUnsafeAllocator(Class<? super T> object) {
        if (this.useJdkUnsafe) {
            return new ObjectConstructor<T>((Class)object){
                final /* synthetic */ Class val$rawType;
                {
                    this.val$rawType = clazz;
                }

                @Override
                public T construct() {
                    try {
                        Object t2 = UnsafeAllocator.INSTANCE.newInstance(this.val$rawType);
                        return t2;
                    }
                    catch (Exception exception) {
                        throw new RuntimeException("Unable to create instance of " + this.val$rawType + ". Registering an InstanceCreator or a TypeAdapter for this type, or adding a no-args constructor may fix this problem.", exception);
                    }
                }
            };
        }
        object = "Unable to create instance of " + object + "; usage of JDK Unsafe is disabled. Registering an InstanceCreator or a TypeAdapter for this type, adding a no-args constructor, or enabling usage of JDK Unsafe may fix this problem.";
        return new ObjectConstructor<T>((String)object){
            final /* synthetic */ String val$exceptionMessage;
            {
                this.val$exceptionMessage = string;
            }

            @Override
            public T construct() {
                throw new JsonIOException(this.val$exceptionMessage);
            }
        };
    }

    public final String toString() {
        return this.instanceCreators.toString();
    }
}


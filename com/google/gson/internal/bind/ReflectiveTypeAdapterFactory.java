/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson.internal.bind;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Primitives;
import com.google.gson.internal.ReflectionAccessFilterHelper;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.internal.bind.TypeAdapterRuntimeTypeWrapper;
import com.google.gson.internal.reflect.ReflectionHelper;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ReflectiveTypeAdapterFactory
implements TypeAdapterFactory {
    private final ConstructorConstructor constructorConstructor;
    private final FieldNamingStrategy fieldNamingPolicy;
    private final Excluder excluder;
    private final JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory;
    private final List<ReflectionAccessFilter> reflectionFilters;

    public ReflectiveTypeAdapterFactory(ConstructorConstructor constructorConstructor, FieldNamingStrategy fieldNamingStrategy, Excluder excluder, JsonAdapterAnnotationTypeAdapterFactory jsonAdapterAnnotationTypeAdapterFactory, List<ReflectionAccessFilter> list2) {
        this.constructorConstructor = constructorConstructor;
        this.fieldNamingPolicy = fieldNamingStrategy;
        this.excluder = excluder;
        this.jsonAdapterFactory = jsonAdapterAnnotationTypeAdapterFactory;
        this.reflectionFilters = list2;
    }

    private boolean includeField(Field field, boolean bl) {
        return !this.excluder.excludeClass(field.getType(), bl) && !this.excluder.excludeField(field, bl);
    }

    private List<String> getFieldNames(Field object) {
        String[] stringArray = ((Field)object).getAnnotation(SerializedName.class);
        if (stringArray == null) {
            object = this.fieldNamingPolicy.translateName((Field)object);
            return Collections.singletonList(object);
        }
        object = stringArray.value();
        if ((stringArray = stringArray.alternate()).length == 0) {
            return Collections.singletonList(object);
        }
        ArrayList<String> arrayList = new ArrayList<String>(stringArray.length + 1);
        arrayList.add((String)object);
        Collections.addAll(arrayList, stringArray);
        return arrayList;
    }

    @Override
    public final <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        boolean bl;
        Class<T> clazz = typeToken.getRawType();
        if (!Object.class.isAssignableFrom(clazz)) {
            return null;
        }
        ReflectionAccessFilter.FilterResult filterResult = ReflectionAccessFilterHelper.getFilterResult(this.reflectionFilters, clazz);
        if (filterResult == ReflectionAccessFilter.FilterResult.BLOCK_ALL) {
            throw new JsonIOException("ReflectionAccessFilter does not permit using reflection for " + clazz + ". Register a TypeAdapter for this type or adjust the access filter.");
        }
        boolean bl2 = bl = filterResult == ReflectionAccessFilter.FilterResult.BLOCK_INACCESSIBLE;
        if (ReflectionHelper.isRecord(clazz)) {
            RecordAdapter<T> recordAdapter = new RecordAdapter<T>(clazz, this.getBoundFields(gson, typeToken, clazz, bl, true), bl);
            return recordAdapter;
        }
        ObjectConstructor<T> objectConstructor = this.constructorConstructor.get(typeToken);
        return new FieldReflectionAdapter<T>(objectConstructor, this.getBoundFields(gson, typeToken, clazz, bl, false));
    }

    private static <M extends AccessibleObject> void checkAccessible(Object object, M m2) {
        M m3 = m2;
        if (!ReflectionAccessFilterHelper.canAccess(m3, Modifier.isStatic(((Member)((Object)m3)).getModifiers()) ? null : object)) {
            object = ReflectionHelper.getAccessibleObjectDescription(m2, true);
            throw new JsonIOException((String)object + " is not accessible and ReflectionAccessFilter does not permit making it accessible. Register a TypeAdapter for the declaring type, adjust the access filter or increase the visibility of the element and its declaring type.");
        }
    }

    private BoundField createBoundField(final Gson gson, Field field, final Method method, String string, final TypeToken<?> typeToken, boolean bl, boolean bl2, final boolean bl3) {
        boolean bl4;
        final boolean bl5 = Primitives.isPrimitive(typeToken.getRawType());
        int n2 = field.getModifiers();
        n2 = Modifier.isStatic(n2) && Modifier.isFinal(n2) ? 1 : 0;
        JsonAdapter jsonAdapter = field.getAnnotation(JsonAdapter.class);
        final TypeAdapter<?> typeAdapter = null;
        if (jsonAdapter != null) {
            typeAdapter = this.jsonAdapterFactory.getTypeAdapter(this.constructorConstructor, gson, typeToken, jsonAdapter);
        }
        boolean bl6 = bl4 = typeAdapter != null;
        if (typeAdapter == null) {
            typeAdapter = gson.getAdapter(typeToken);
        }
        return new BoundField(string, field, bl, bl2, n2 != 0){
            final /* synthetic */ boolean val$isStaticFinalField;
            {
                this.val$isStaticFinalField = bl6;
                super(string, field, bl, bl2);
            }

            @Override
            void write(JsonWriter object, Object object2) throws IOException, IllegalAccessException {
                Object object3;
                if (!this.serialized) {
                    return;
                }
                if (bl3) {
                    if (method == null) {
                        ReflectiveTypeAdapterFactory.checkAccessible(object2, this.field);
                    } else {
                        ReflectiveTypeAdapterFactory.checkAccessible(object2, method);
                    }
                }
                if (method != null) {
                    try {
                        object3 = method.invoke(object2, new Object[0]);
                    }
                    catch (InvocationTargetException invocationTargetException) {
                        object = ReflectionHelper.getAccessibleObjectDescription(method, false);
                        throw new JsonIOException("Accessor " + (String)object + " threw exception", invocationTargetException.getCause());
                    }
                } else {
                    object3 = this.field.get(object2);
                }
                if (object3 == object2) {
                    return;
                }
                ((JsonWriter)object).name(this.name);
                object2 = bl4 ? typeAdapter : new TypeAdapterRuntimeTypeWrapper(gson, typeAdapter, typeToken.getType());
                ((TypeAdapter)object2).write((JsonWriter)object, object3);
            }

            @Override
            void readIntoArray(JsonReader jsonReader, int n2, Object[] objectArray) throws IOException, JsonParseException {
                Object t2 = typeAdapter.read(jsonReader);
                if (t2 == null && bl5) {
                    throw new JsonParseException("null is not allowed as value for record component '" + this.fieldName + "' of primitive type; at path " + jsonReader.getPath());
                }
                objectArray[n2] = t2;
            }

            @Override
            void readIntoField(JsonReader object, Object object2) throws IOException, IllegalAccessException {
                if ((object = typeAdapter.read((JsonReader)object)) != null || !bl5) {
                    if (bl3) {
                        ReflectiveTypeAdapterFactory.checkAccessible(object2, this.field);
                    } else if (this.val$isStaticFinalField) {
                        object = ReflectionHelper.getAccessibleObjectDescription(this.field, false);
                        throw new JsonIOException("Cannot set value of 'static final' " + (String)object);
                    }
                    this.field.set(object2, object);
                }
            }
        };
    }

    private Map<String, BoundField> getBoundFields(Gson gson, TypeToken<?> typeToken, Class<?> clazz, boolean bl, boolean bl2) {
        LinkedHashMap<String, BoundField> linkedHashMap = new LinkedHashMap<String, BoundField>();
        if (clazz.isInterface()) {
            return linkedHashMap;
        }
        Class<?> clazz2 = clazz;
        while (clazz != Object.class) {
            Object object;
            Field[] fieldArray = clazz.getDeclaredFields();
            if (clazz != clazz2 && fieldArray.length > 0) {
                object = ReflectionAccessFilterHelper.getFilterResult(this.reflectionFilters, clazz);
                if (object == ReflectionAccessFilter.FilterResult.BLOCK_ALL) {
                    throw new JsonIOException("ReflectionAccessFilter does not permit using reflection for " + clazz + " (supertype of " + clazz2 + "). Register a TypeAdapter for this type or adjust the access filter.");
                }
                bl = object == ReflectionAccessFilter.FilterResult.BLOCK_INACCESSIBLE;
            }
            object = fieldArray;
            int n2 = fieldArray.length;
            for (int i2 = 0; i2 < n2; ++i2) {
                Object object2;
                Field field = object[i2];
                boolean bl3 = this.includeField(field, true);
                boolean bl4 = this.includeField(field, false);
                if (!bl3 && !bl4) continue;
                Method method = null;
                if (bl2) {
                    if (Modifier.isStatic(field.getModifiers())) {
                        bl4 = false;
                    } else {
                        method = ReflectionHelper.getAccessor(clazz, field);
                        if (!bl) {
                            ReflectionHelper.makeAccessible(method);
                        }
                        if (method.getAnnotation(SerializedName.class) != null && field.getAnnotation(SerializedName.class) == null) {
                            object2 = ReflectionHelper.getAccessibleObjectDescription(method, false);
                            throw new JsonIOException("@SerializedName on " + (String)object2 + " is not supported");
                        }
                    }
                }
                if (!bl && method == null) {
                    ReflectionHelper.makeAccessible(field);
                }
                object2 = $Gson$Types.resolve(typeToken.getType(), clazz, field.getGenericType());
                List<String> list2 = this.getFieldNames(field);
                Object object3 = null;
                int n3 = list2.size();
                for (int i3 = 0; i3 < n3; ++i3) {
                    Object object4 = list2.get(i3);
                    if (i3 != 0) {
                        bl3 = false;
                    }
                    BoundField boundField = this.createBoundField(gson, field, method, (String)object4, TypeToken.get((Type)object2), bl3, bl4, bl);
                    object4 = linkedHashMap.put((String)object4, boundField);
                    if (object3 != null) continue;
                    object3 = object4;
                }
                if (object3 == null) continue;
                throw new IllegalArgumentException("Class " + clazz2.getName() + " declares multiple JSON fields named '" + ((BoundField)object3).name + "'; conflict is caused by fields " + ReflectionHelper.fieldToString(((BoundField)object3).field) + " and " + ReflectionHelper.fieldToString(field));
            }
            Class<?> clazz3 = clazz;
            typeToken = TypeToken.get($Gson$Types.resolve(typeToken.getType(), clazz3, clazz3.getGenericSuperclass()));
            clazz = typeToken.getRawType();
        }
        return linkedHashMap;
    }

    static final class RecordAdapter<T>
    extends Adapter<T, Object[]> {
        static final Map<Class<?>, Object> PRIMITIVE_DEFAULTS = RecordAdapter.primitiveDefaults();
        private final Constructor<T> constructor;
        private final Object[] constructorArgsDefaults;
        private final Map<String, Integer> componentIndices = new HashMap<String, Integer>();

        RecordAdapter(Class<T> stringArray, Map<String, BoundField> map, boolean bl) {
            super(map);
            this.constructor = ReflectionHelper.getCanonicalRecordConstructor(stringArray);
            if (bl) {
                ReflectiveTypeAdapterFactory.checkAccessible(null, this.constructor);
            } else {
                ReflectionHelper.makeAccessible(this.constructor);
            }
            stringArray = ReflectionHelper.getRecordComponentNames(stringArray);
            for (int i2 = 0; i2 < stringArray.length; ++i2) {
                this.componentIndices.put(stringArray[i2], i2);
            }
            Class<?>[] classArray = this.constructor.getParameterTypes();
            this.constructorArgsDefaults = new Object[classArray.length];
            for (int i3 = 0; i3 < classArray.length; ++i3) {
                this.constructorArgsDefaults[i3] = PRIMITIVE_DEFAULTS.get(classArray[i3]);
            }
        }

        private static Map<Class<?>, Object> primitiveDefaults() {
            HashMap hashMap = new HashMap();
            hashMap.put(Byte.TYPE, (byte)0);
            hashMap.put(Short.TYPE, (short)0);
            hashMap.put(Integer.TYPE, 0);
            hashMap.put(Long.TYPE, 0L);
            hashMap.put(Float.TYPE, Float.valueOf(0.0f));
            hashMap.put(Double.TYPE, 0.0);
            hashMap.put(Character.TYPE, Character.valueOf('\u0000'));
            hashMap.put(Boolean.TYPE, Boolean.FALSE);
            return hashMap;
        }

        @Override
        final Object[] createAccumulator() {
            return (Object[])this.constructorArgsDefaults.clone();
        }

        @Override
        final void readField(Object[] objectArray, JsonReader jsonReader, BoundField boundField) throws IOException {
            Integer n2 = this.componentIndices.get(boundField.fieldName);
            if (n2 == null) {
                throw new IllegalStateException("Could not find the index in the constructor '" + ReflectionHelper.constructorToString(this.constructor) + "' for field with name '" + boundField.fieldName + "', unable to determine which argument in the constructor the field corresponds to. This is unexpected behavior, as we expect the RecordComponents to have the same names as the fields in the Java class, and that the order of the RecordComponents is the same as the order of the canonical constructor parameters.");
            }
            boundField.readIntoArray(jsonReader, n2, objectArray);
        }

        @Override
        final T finalize(Object[] objectArray) {
            try {
                return this.constructor.newInstance(objectArray);
            }
            catch (IllegalAccessException illegalAccessException) {
                IllegalAccessException illegalAccessException2 = illegalAccessException;
                throw ReflectionHelper.createExceptionForUnexpectedIllegalAccess(illegalAccessException);
            }
            catch (IllegalArgumentException | InstantiationException exception) {
                throw new RuntimeException("Failed to invoke constructor '" + ReflectionHelper.constructorToString(this.constructor) + "' with args " + Arrays.toString(objectArray), exception);
            }
            catch (InvocationTargetException invocationTargetException) {
                throw new RuntimeException("Failed to invoke constructor '" + ReflectionHelper.constructorToString(this.constructor) + "' with args " + Arrays.toString(objectArray), invocationTargetException.getCause());
            }
        }
    }

    static final class FieldReflectionAdapter<T>
    extends Adapter<T, T> {
        private final ObjectConstructor<T> constructor;

        FieldReflectionAdapter(ObjectConstructor<T> objectConstructor, Map<String, BoundField> map) {
            super(map);
            this.constructor = objectConstructor;
        }

        @Override
        final T createAccumulator() {
            return this.constructor.construct();
        }

        @Override
        final void readField(T t2, JsonReader jsonReader, BoundField boundField) throws IllegalAccessException, IOException {
            boundField.readIntoField(jsonReader, t2);
        }

        @Override
        final T finalize(T t2) {
            return t2;
        }
    }

    public static abstract class Adapter<T, A>
    extends TypeAdapter<T> {
        final Map<String, BoundField> boundFields;

        Adapter(Map<String, BoundField> map) {
            this.boundFields = map;
        }

        @Override
        public void write(JsonWriter jsonWriter, T t2) throws IOException {
            if (t2 == null) {
                jsonWriter.nullValue();
                return;
            }
            jsonWriter.beginObject();
            try {
                for (BoundField boundField : this.boundFields.values()) {
                    boundField.write(jsonWriter, t2);
                }
            }
            catch (IllegalAccessException illegalAccessException) {
                IllegalAccessException illegalAccessException2 = illegalAccessException;
                throw ReflectionHelper.createExceptionForUnexpectedIllegalAccess(illegalAccessException);
            }
            jsonWriter.endObject();
        }

        @Override
        public T read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            A a2 = this.createAccumulator();
            try {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    Object object = jsonReader.nextName();
                    if ((object = this.boundFields.get(object)) == null || !((BoundField)object).deserialized) {
                        jsonReader.skipValue();
                        continue;
                    }
                    this.readField(a2, jsonReader, (BoundField)object);
                }
            }
            catch (IllegalStateException illegalStateException) {
                throw new JsonSyntaxException(illegalStateException);
            }
            catch (IllegalAccessException illegalAccessException) {
                IllegalAccessException illegalAccessException2 = illegalAccessException;
                throw ReflectionHelper.createExceptionForUnexpectedIllegalAccess(illegalAccessException);
            }
            jsonReader.endObject();
            return this.finalize(a2);
        }

        abstract A createAccumulator();

        abstract void readField(A var1, JsonReader var2, BoundField var3) throws IllegalAccessException, IOException;

        abstract T finalize(A var1);
    }

    static abstract class BoundField {
        final String name;
        final Field field;
        final String fieldName;
        final boolean serialized;
        final boolean deserialized;

        protected BoundField(String string, Field field, boolean bl, boolean bl2) {
            this.name = string;
            this.field = field;
            this.fieldName = field.getName();
            this.serialized = bl;
            this.deserialized = bl2;
        }

        abstract void write(JsonWriter var1, Object var2) throws IOException, IllegalAccessException;

        abstract void readIntoArray(JsonReader var1, int var2, Object[] var3) throws IOException, JsonParseException;

        abstract void readIntoField(JsonReader var1, Object var2) throws IOException, IllegalAccessException;
    }
}


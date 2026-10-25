/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonNull;
import com.google.gson.JsonSyntaxException;
import com.google.gson.LongSerializationPolicy;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ToNumberPolicy;
import com.google.gson.ToNumberStrategy;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.LazilyParsedNumber;
import com.google.gson.internal.Primitives;
import com.google.gson.internal.Streams;
import com.google.gson.internal.bind.ArrayTypeAdapter;
import com.google.gson.internal.bind.CollectionTypeAdapterFactory;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.internal.bind.MapTypeAdapterFactory;
import com.google.gson.internal.bind.NumberTypeAdapter;
import com.google.gson.internal.bind.ObjectTypeAdapter;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.internal.bind.SerializationDelegatingTypeAdapter;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.internal.sql.SqlTypesSupport;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;

public class Gson {
    public static final boolean DEFAULT_JSON_NON_EXECUTABLE = false;
    public static final boolean DEFAULT_LENIENT = false;
    public static final boolean DEFAULT_PRETTY_PRINT = false;
    public static final boolean DEFAULT_ESCAPE_HTML = true;
    public static final boolean DEFAULT_SERIALIZE_NULLS = false;
    public static final boolean DEFAULT_COMPLEX_MAP_KEYS = false;
    public static final boolean DEFAULT_SPECIALIZE_FLOAT_VALUES = false;
    public static final boolean DEFAULT_USE_JDK_UNSAFE = true;
    public static final String DEFAULT_DATE_PATTERN = null;
    public static final FieldNamingStrategy DEFAULT_FIELD_NAMING_STRATEGY = FieldNamingPolicy.IDENTITY;
    public static final ToNumberStrategy DEFAULT_OBJECT_TO_NUMBER_STRATEGY = ToNumberPolicy.DOUBLE;
    public static final ToNumberStrategy DEFAULT_NUMBER_TO_NUMBER_STRATEGY = ToNumberPolicy.LAZILY_PARSED_NUMBER;
    public static final String JSON_NON_EXECUTABLE_PREFIX = ")]}'\n";
    public final ThreadLocal<Map<TypeToken<?>, TypeAdapter<?>>> threadLocalAdapterResults = new ThreadLocal();
    public final ConcurrentMap<TypeToken<?>, TypeAdapter<?>> typeTokenCache = new ConcurrentHashMap();
    public final ConstructorConstructor constructorConstructor;
    public final JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory;
    public final List<TypeAdapterFactory> factories;
    public final Excluder excluder;
    public final FieldNamingStrategy fieldNamingStrategy;
    public final Map<Type, InstanceCreator<?>> instanceCreators;
    public final boolean serializeNulls;
    public final boolean complexMapKeySerialization;
    public final boolean generateNonExecutableJson;
    public final boolean htmlSafe;
    public final boolean prettyPrinting;
    public final boolean lenient;
    public final boolean serializeSpecialFloatingPointValues;
    public final boolean useJdkUnsafe;
    public final String datePattern;
    public final int dateStyle;
    public final int timeStyle;
    public final LongSerializationPolicy longSerializationPolicy;
    public final List<TypeAdapterFactory> builderFactories;
    public final List<TypeAdapterFactory> builderHierarchyFactories;
    public final ToNumberStrategy objectToNumberStrategy;
    public final ToNumberStrategy numberToNumberStrategy;
    public final List<ReflectionAccessFilter> reflectionFilters;

    public Gson() {
        this(Excluder.DEFAULT, DEFAULT_FIELD_NAMING_STRATEGY, Collections.emptyMap(), false, false, false, true, false, false, false, true, LongSerializationPolicy.DEFAULT, DEFAULT_DATE_PATTERN, 2, 2, Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), DEFAULT_OBJECT_TO_NUMBER_STRATEGY, DEFAULT_NUMBER_TO_NUMBER_STRATEGY, Collections.emptyList());
    }

    Gson(Excluder excluder, FieldNamingStrategy fieldNamingStrategy, Map<Type, InstanceCreator<?>> object, boolean bl, boolean bl2, boolean bl3, boolean bl4, boolean bl5, boolean bl6, boolean bl7, boolean bl8, LongSerializationPolicy longSerializationPolicy, String string, int n2, int n3, List<TypeAdapterFactory> list2, List<TypeAdapterFactory> list3, List<TypeAdapterFactory> list4, ToNumberStrategy toNumberStrategy, ToNumberStrategy toNumberStrategy2, List<ReflectionAccessFilter> list5) {
        this.excluder = excluder;
        this.fieldNamingStrategy = fieldNamingStrategy;
        this.instanceCreators = object;
        this.constructorConstructor = new ConstructorConstructor((Map<Type, InstanceCreator<?>>)object, bl8, list5);
        this.serializeNulls = bl;
        this.complexMapKeySerialization = bl2;
        this.generateNonExecutableJson = bl3;
        this.htmlSafe = bl4;
        this.prettyPrinting = bl5;
        this.lenient = bl6;
        this.serializeSpecialFloatingPointValues = bl7;
        this.useJdkUnsafe = bl8;
        this.longSerializationPolicy = longSerializationPolicy;
        this.datePattern = string;
        this.dateStyle = n2;
        this.timeStyle = n3;
        this.builderFactories = list2;
        this.builderHierarchyFactories = list3;
        this.objectToNumberStrategy = toNumberStrategy;
        this.numberToNumberStrategy = toNumberStrategy2;
        this.reflectionFilters = list5;
        object = new ArrayList<TypeAdapterFactory>();
        object.add(TypeAdapters.JSON_ELEMENT_FACTORY);
        object.add(ObjectTypeAdapter.getFactory(toNumberStrategy));
        object.add(excluder);
        object.addAll(list4);
        object.add(TypeAdapters.STRING_FACTORY);
        object.add(TypeAdapters.INTEGER_FACTORY);
        object.add(TypeAdapters.BOOLEAN_FACTORY);
        object.add(TypeAdapters.BYTE_FACTORY);
        object.add(TypeAdapters.SHORT_FACTORY);
        TypeAdapter<Number> typeAdapter = Gson.longAdapter(longSerializationPolicy);
        object.add(TypeAdapters.newFactory(Long.TYPE, Long.class, typeAdapter));
        object.add(TypeAdapters.newFactory(Double.TYPE, Double.class, this.doubleAdapter(bl7)));
        object.add(TypeAdapters.newFactory(Float.TYPE, Float.class, this.floatAdapter(bl7)));
        object.add(NumberTypeAdapter.getFactory(toNumberStrategy2));
        object.add(TypeAdapters.ATOMIC_INTEGER_FACTORY);
        object.add(TypeAdapters.ATOMIC_BOOLEAN_FACTORY);
        object.add(TypeAdapters.newFactory(AtomicLong.class, Gson.atomicLongAdapter(typeAdapter)));
        object.add(TypeAdapters.newFactory(AtomicLongArray.class, Gson.atomicLongArrayAdapter(typeAdapter)));
        object.add(TypeAdapters.ATOMIC_INTEGER_ARRAY_FACTORY);
        object.add(TypeAdapters.CHARACTER_FACTORY);
        object.add(TypeAdapters.STRING_BUILDER_FACTORY);
        object.add(TypeAdapters.STRING_BUFFER_FACTORY);
        object.add(TypeAdapters.newFactory(BigDecimal.class, TypeAdapters.BIG_DECIMAL));
        object.add(TypeAdapters.newFactory(BigInteger.class, TypeAdapters.BIG_INTEGER));
        object.add(TypeAdapters.newFactory(LazilyParsedNumber.class, TypeAdapters.LAZILY_PARSED_NUMBER));
        object.add(TypeAdapters.URL_FACTORY);
        object.add(TypeAdapters.URI_FACTORY);
        object.add(TypeAdapters.UUID_FACTORY);
        object.add(TypeAdapters.CURRENCY_FACTORY);
        object.add(TypeAdapters.LOCALE_FACTORY);
        object.add(TypeAdapters.INET_ADDRESS_FACTORY);
        object.add(TypeAdapters.BIT_SET_FACTORY);
        object.add(DateTypeAdapter.FACTORY);
        object.add(TypeAdapters.CALENDAR_FACTORY);
        if (SqlTypesSupport.SUPPORTS_SQL_TYPES) {
            object.add(SqlTypesSupport.TIME_FACTORY);
            object.add(SqlTypesSupport.DATE_FACTORY);
            object.add(SqlTypesSupport.TIMESTAMP_FACTORY);
        }
        object.add(ArrayTypeAdapter.FACTORY);
        object.add(TypeAdapters.CLASS_FACTORY);
        object.add(new CollectionTypeAdapterFactory(this.constructorConstructor));
        object.add(new MapTypeAdapterFactory(this.constructorConstructor, bl2));
        this.jsonAdapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(this.constructorConstructor);
        object.add(this.jsonAdapterFactory);
        object.add(TypeAdapters.ENUM_FACTORY);
        object.add(new ReflectiveTypeAdapterFactory(this.constructorConstructor, fieldNamingStrategy, excluder, this.jsonAdapterFactory, list5));
        this.factories = Collections.unmodifiableList(object);
    }

    public GsonBuilder newBuilder() {
        return new GsonBuilder(this);
    }

    @Deprecated
    public Excluder excluder() {
        return this.excluder;
    }

    public FieldNamingStrategy fieldNamingStrategy() {
        return this.fieldNamingStrategy;
    }

    public boolean serializeNulls() {
        return this.serializeNulls;
    }

    public boolean htmlSafe() {
        return this.htmlSafe;
    }

    private TypeAdapter<Number> doubleAdapter(boolean bl) {
        if (bl) {
            return TypeAdapters.DOUBLE;
        }
        return new TypeAdapter<Number>(){

            @Override
            public Double read(JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                return jsonReader.nextDouble();
            }

            @Override
            public void write(JsonWriter jsonWriter, Number number) throws IOException {
                if (number == null) {
                    jsonWriter.nullValue();
                    return;
                }
                double d2 = number.doubleValue();
                Gson.checkValidFloatingPoint(d2);
                jsonWriter.value(d2);
            }
        };
    }

    private TypeAdapter<Number> floatAdapter(boolean bl) {
        if (bl) {
            return TypeAdapters.FLOAT;
        }
        return new TypeAdapter<Number>(){

            @Override
            public Float read(JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                return Float.valueOf((float)jsonReader.nextDouble());
            }

            @Override
            public void write(JsonWriter jsonWriter, Number number) throws IOException {
                if (number == null) {
                    jsonWriter.nullValue();
                    return;
                }
                float f2 = number.floatValue();
                Gson.checkValidFloatingPoint(f2);
                number = number instanceof Float ? (Number)number : (Number)Float.valueOf(f2);
                jsonWriter.value(number);
            }
        };
    }

    static void checkValidFloatingPoint(double d2) {
        if (Double.isNaN(d2) || Double.isInfinite(d2)) {
            throw new IllegalArgumentException(d2 + " is not a valid double value as per JSON specification. To override this behavior, use GsonBuilder.serializeSpecialFloatingPointValues() method.");
        }
    }

    private static TypeAdapter<Number> longAdapter(LongSerializationPolicy longSerializationPolicy) {
        if (longSerializationPolicy == LongSerializationPolicy.DEFAULT) {
            return TypeAdapters.LONG;
        }
        return new TypeAdapter<Number>(){

            @Override
            public Number read(JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                return jsonReader.nextLong();
            }

            @Override
            public void write(JsonWriter jsonWriter, Number number) throws IOException {
                if (number == null) {
                    jsonWriter.nullValue();
                    return;
                }
                jsonWriter.value(number.toString());
            }
        };
    }

    private static TypeAdapter<AtomicLong> atomicLongAdapter(final TypeAdapter<Number> typeAdapter) {
        return new TypeAdapter<AtomicLong>(){

            @Override
            public void write(JsonWriter jsonWriter, AtomicLong atomicLong) throws IOException {
                typeAdapter.write(jsonWriter, atomicLong.get());
            }

            @Override
            public AtomicLong read(JsonReader object) throws IOException {
                object = (Number)typeAdapter.read((JsonReader)object);
                return new AtomicLong(((Number)object).longValue());
            }
        }.nullSafe();
    }

    private static TypeAdapter<AtomicLongArray> atomicLongArrayAdapter(final TypeAdapter<Number> typeAdapter) {
        return new TypeAdapter<AtomicLongArray>(){

            @Override
            public void write(JsonWriter jsonWriter, AtomicLongArray atomicLongArray) throws IOException {
                jsonWriter.beginArray();
                int n2 = atomicLongArray.length();
                for (int i2 = 0; i2 < n2; ++i2) {
                    typeAdapter.write(jsonWriter, atomicLongArray.get(i2));
                }
                jsonWriter.endArray();
            }

            @Override
            public AtomicLongArray read(JsonReader jsonReader) throws IOException {
                ArrayList<Long> arrayList = new ArrayList<Long>();
                jsonReader.beginArray();
                while (jsonReader.hasNext()) {
                    long l2 = ((Number)typeAdapter.read(jsonReader)).longValue();
                    arrayList.add(l2);
                }
                jsonReader.endArray();
                int n2 = arrayList.size();
                AtomicLongArray atomicLongArray = new AtomicLongArray(n2);
                for (int i2 = 0; i2 < n2; ++i2) {
                    atomicLongArray.set(i2, (Long)arrayList.get(i2));
                }
                return atomicLongArray;
            }
        }.nullSafe();
    }

    /*
     * WARNING - void declaration
     */
    public <T> TypeAdapter<T> getAdapter(TypeToken<T> typeToken) {
        void var4_7;
        Objects.requireNonNull(typeToken, "type must not be null");
        HashMap<TypeToken<T>, TypeAdapter<T>> hashMap = (TypeAdapter)this.typeTokenCache.get(typeToken);
        if (hashMap != null) {
            return hashMap;
        }
        hashMap = this.threadLocalAdapterResults.get();
        boolean bl = false;
        if (hashMap == null) {
            hashMap = new HashMap<TypeToken<T>, TypeAdapter<T>>();
            this.threadLocalAdapterResults.set(hashMap);
            bl = true;
        } else {
            TypeAdapter typeAdapter2 = (TypeAdapter)hashMap.get(typeToken);
            if (typeAdapter2 != null) {
                return typeAdapter2;
            }
        }
        Object var4_6 = null;
        try {
            FutureTypeAdapter<T> futureTypeAdapter = new FutureTypeAdapter<T>();
            hashMap.put(typeToken, futureTypeAdapter);
            for (TypeAdapterFactory typeAdapterFactory : this.factories) {
                TypeAdapter<T> typeAdapter = typeAdapterFactory.create(this, typeToken);
                if (typeAdapter == null) continue;
                futureTypeAdapter.setDelegate(typeAdapter);
                hashMap.put(typeToken, typeAdapter);
                break;
            }
        }
        finally {
            if (bl) {
                this.threadLocalAdapterResults.remove();
            }
        }
        if (var4_7 == null) {
            throw new IllegalArgumentException("GSON (2.10.1) cannot handle " + typeToken);
        }
        if (bl) {
            this.typeTokenCache.putAll(hashMap);
        }
        return var4_7;
    }

    public <T> TypeAdapter<T> getDelegateAdapter(TypeAdapterFactory typeAdapterFactory, TypeToken<T> typeToken) {
        if (!this.factories.contains(typeAdapterFactory)) {
            typeAdapterFactory = this.jsonAdapterFactory;
        }
        boolean bl = false;
        for (TypeAdapterFactory typeAdapterFactory2 : this.factories) {
            if (!bl) {
                if (typeAdapterFactory2 != typeAdapterFactory) continue;
                bl = true;
                continue;
            }
            TypeAdapter<T> typeAdapter = typeAdapterFactory2.create(this, typeToken);
            if (typeAdapter == null) continue;
            return typeAdapter;
        }
        throw new IllegalArgumentException("GSON cannot serialize " + typeToken);
    }

    public <T> TypeAdapter<T> getAdapter(Class<T> clazz) {
        return this.getAdapter(TypeToken.get(clazz));
    }

    public JsonElement toJsonTree(Object object) {
        if (object == null) {
            return JsonNull.INSTANCE;
        }
        Object object2 = object;
        return this.toJsonTree(object2, object2.getClass());
    }

    public JsonElement toJsonTree(Object object, Type type) {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        this.toJson(object, type, jsonTreeWriter);
        return jsonTreeWriter.get();
    }

    public String toJson(Object object) {
        if (object == null) {
            return this.toJson(JsonNull.INSTANCE);
        }
        Object object2 = object;
        return this.toJson(object2, object2.getClass());
    }

    public String toJson(Object object, Type type) {
        StringWriter stringWriter = new StringWriter();
        this.toJson(object, type, stringWriter);
        return stringWriter.toString();
    }

    public void toJson(Object object, Appendable appendable) throws JsonIOException {
        if (object != null) {
            Object object2 = object;
            this.toJson(object2, object2.getClass(), appendable);
            return;
        }
        this.toJson((JsonElement)JsonNull.INSTANCE, appendable);
    }

    public void toJson(Object object, Type type, Appendable object2) throws JsonIOException {
        try {
            object2 = this.newJsonWriter(Streams.writerForAppendable((Appendable)object2));
            this.toJson(object, type, (JsonWriter)object2);
            return;
        }
        catch (IOException iOException) {
            throw new JsonIOException(iOException);
        }
    }

    public void toJson(Object object, Type object2, JsonWriter jsonWriter) throws JsonIOException {
        object2 = this.getAdapter(TypeToken.get((Type)object2));
        boolean bl = jsonWriter.isLenient();
        jsonWriter.setLenient(true);
        boolean bl2 = jsonWriter.isHtmlSafe();
        jsonWriter.setHtmlSafe(this.htmlSafe);
        boolean bl3 = jsonWriter.getSerializeNulls();
        jsonWriter.setSerializeNulls(this.serializeNulls);
        try {
            ((TypeAdapter)object2).write(jsonWriter, object);
            return;
        }
        catch (IOException iOException) {
            throw new JsonIOException(iOException);
        }
        catch (AssertionError assertionError) {
            throw new AssertionError("AssertionError (GSON 2.10.1): " + ((Throwable)((Object)assertionError)).getMessage(), (Throwable)((Object)assertionError));
        }
        finally {
            jsonWriter.setLenient(bl);
            jsonWriter.setHtmlSafe(bl2);
            jsonWriter.setSerializeNulls(bl3);
        }
    }

    public String toJson(JsonElement jsonElement) {
        StringWriter stringWriter = new StringWriter();
        this.toJson(jsonElement, (Appendable)stringWriter);
        return stringWriter.toString();
    }

    public void toJson(JsonElement jsonElement, Appendable object) throws JsonIOException {
        try {
            object = this.newJsonWriter(Streams.writerForAppendable((Appendable)object));
            this.toJson(jsonElement, (JsonWriter)object);
            return;
        }
        catch (IOException iOException) {
            throw new JsonIOException(iOException);
        }
    }

    public JsonWriter newJsonWriter(Writer closeable) throws IOException {
        if (this.generateNonExecutableJson) {
            ((Writer)closeable).write(JSON_NON_EXECUTABLE_PREFIX);
        }
        closeable = new JsonWriter((Writer)closeable);
        if (this.prettyPrinting) {
            ((JsonWriter)closeable).setIndent("  ");
        }
        ((JsonWriter)closeable).setHtmlSafe(this.htmlSafe);
        ((JsonWriter)closeable).setLenient(this.lenient);
        ((JsonWriter)closeable).setSerializeNulls(this.serializeNulls);
        return closeable;
    }

    public JsonReader newJsonReader(Reader closeable) {
        closeable = new JsonReader((Reader)closeable);
        ((JsonReader)closeable).setLenient(this.lenient);
        return closeable;
    }

    public void toJson(JsonElement jsonElement, JsonWriter jsonWriter) throws JsonIOException {
        boolean bl = jsonWriter.isLenient();
        jsonWriter.setLenient(true);
        boolean bl2 = jsonWriter.isHtmlSafe();
        jsonWriter.setHtmlSafe(this.htmlSafe);
        boolean bl3 = jsonWriter.getSerializeNulls();
        jsonWriter.setSerializeNulls(this.serializeNulls);
        try {
            Streams.write(jsonElement, jsonWriter);
            return;
        }
        catch (IOException iOException) {
            throw new JsonIOException(iOException);
        }
        catch (AssertionError assertionError) {
            throw new AssertionError("AssertionError (GSON 2.10.1): " + ((Throwable)((Object)assertionError)).getMessage(), (Throwable)((Object)assertionError));
        }
        finally {
            jsonWriter.setLenient(bl);
            jsonWriter.setHtmlSafe(bl2);
            jsonWriter.setSerializeNulls(bl3);
        }
    }

    public <T> T fromJson(String string, Class<T> clazz) throws JsonSyntaxException {
        string = this.fromJson(string, TypeToken.get(clazz));
        return Primitives.wrap(clazz).cast(string);
    }

    public <T> T fromJson(String string, Type type) throws JsonSyntaxException {
        return (T)this.fromJson(string, TypeToken.get(type));
    }

    public <T> T fromJson(String object, TypeToken<T> typeToken) throws JsonSyntaxException {
        if (object == null) {
            return null;
        }
        object = new StringReader((String)object);
        return this.fromJson((Reader)object, typeToken);
    }

    public <T> T fromJson(Reader reader, Class<T> clazz) throws JsonSyntaxException, JsonIOException {
        reader = this.fromJson(reader, TypeToken.get(clazz));
        return Primitives.wrap(clazz).cast(reader);
    }

    public <T> T fromJson(Reader reader, Type type) throws JsonIOException, JsonSyntaxException {
        return (T)this.fromJson(reader, TypeToken.get(type));
    }

    public <T> T fromJson(Reader closeable, TypeToken<T> typeToken) throws JsonIOException, JsonSyntaxException {
        closeable = this.newJsonReader((Reader)closeable);
        typeToken = this.fromJson((JsonReader)closeable, typeToken);
        Gson.assertFullConsumption(typeToken, (JsonReader)closeable);
        return (T)typeToken;
    }

    private static void assertFullConsumption(Object object, JsonReader jsonReader) {
        try {
            if (object != null && jsonReader.peek() != JsonToken.END_DOCUMENT) {
                throw new JsonSyntaxException("JSON document was not fully consumed.");
            }
            return;
        }
        catch (MalformedJsonException malformedJsonException) {
            throw new JsonSyntaxException(malformedJsonException);
        }
        catch (IOException iOException) {
            throw new JsonIOException(iOException);
        }
    }

    public <T> T fromJson(JsonReader jsonReader, Type type) throws JsonIOException, JsonSyntaxException {
        return (T)this.fromJson(jsonReader, TypeToken.get(type));
    }

    public <T> T fromJson(JsonReader jsonReader, TypeToken<T> object) throws JsonIOException, JsonSyntaxException {
        boolean bl = true;
        boolean bl2 = jsonReader.isLenient();
        jsonReader.setLenient(true);
        try {
            jsonReader.peek();
            bl = false;
            object = this.getAdapter((TypeToken<T>)object);
            object = ((TypeAdapter)object).read(jsonReader);
            return (T)object;
        }
        catch (EOFException eOFException) {
            if (bl) {
                return null;
            }
            throw new JsonSyntaxException(eOFException);
        }
        catch (IllegalStateException illegalStateException) {
            throw new JsonSyntaxException(illegalStateException);
        }
        catch (IOException iOException) {
            throw new JsonSyntaxException(iOException);
        }
        catch (AssertionError assertionError) {
            throw new AssertionError("AssertionError (GSON 2.10.1): " + ((Throwable)((Object)assertionError)).getMessage(), (Throwable)((Object)assertionError));
        }
        finally {
            jsonReader.setLenient(bl2);
        }
    }

    public <T> T fromJson(JsonElement jsonElement, Class<T> clazz) throws JsonSyntaxException {
        jsonElement = this.fromJson(jsonElement, TypeToken.get(clazz));
        return Primitives.wrap(clazz).cast(jsonElement);
    }

    public <T> T fromJson(JsonElement jsonElement, Type type) throws JsonSyntaxException {
        return (T)this.fromJson(jsonElement, TypeToken.get(type));
    }

    public <T> T fromJson(JsonElement jsonElement, TypeToken<T> typeToken) throws JsonSyntaxException {
        if (jsonElement == null) {
            return null;
        }
        return this.fromJson((JsonReader)new JsonTreeReader(jsonElement), typeToken);
    }

    public String toString() {
        return "{serializeNulls:" + this.serializeNulls + ",factories:" + this.factories + ",instanceCreators:" + this.constructorConstructor + "}";
    }

    static class FutureTypeAdapter<T>
    extends SerializationDelegatingTypeAdapter<T> {
        private TypeAdapter<T> delegate = null;

        FutureTypeAdapter() {
        }

        public void setDelegate(TypeAdapter<T> typeAdapter) {
            if (this.delegate != null) {
                throw new AssertionError((Object)"Delegate is already set");
            }
            this.delegate = typeAdapter;
        }

        private TypeAdapter<T> delegate() {
            TypeAdapter<T> typeAdapter = this.delegate;
            if (typeAdapter == null) {
                throw new IllegalStateException("Adapter for type with cyclic dependency has been used before dependency has been resolved");
            }
            return typeAdapter;
        }

        @Override
        public TypeAdapter<T> getSerializationDelegate() {
            return this.delegate();
        }

        @Override
        public T read(JsonReader jsonReader) throws IOException {
            return this.delegate().read(jsonReader);
        }

        @Override
        public void write(JsonWriter jsonWriter, T t2) throws IOException {
            this.delegate().write(jsonWriter, t2);
        }
    }
}


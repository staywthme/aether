/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.LongSerializationPolicy;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ToNumberStrategy;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Preconditions;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.bind.DefaultDateTypeAdapter;
import com.google.gson.internal.bind.TreeTypeAdapter;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.internal.sql.SqlTypesSupport;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class GsonBuilder {
    private Excluder excluder = Excluder.DEFAULT;
    private LongSerializationPolicy longSerializationPolicy = LongSerializationPolicy.DEFAULT;
    private FieldNamingStrategy fieldNamingPolicy = FieldNamingPolicy.IDENTITY;
    private final Map<Type, InstanceCreator<?>> instanceCreators = new HashMap();
    private final List<TypeAdapterFactory> factories = new ArrayList<TypeAdapterFactory>();
    private final List<TypeAdapterFactory> hierarchyFactories = new ArrayList<TypeAdapterFactory>();
    private boolean serializeNulls = false;
    private String datePattern = Gson.DEFAULT_DATE_PATTERN;
    private int dateStyle = 2;
    private int timeStyle = 2;
    private boolean complexMapKeySerialization = false;
    private boolean serializeSpecialFloatingPointValues = false;
    private boolean escapeHtmlChars = true;
    private boolean prettyPrinting = false;
    private boolean generateNonExecutableJson = false;
    private boolean lenient = false;
    private boolean useJdkUnsafe = true;
    private ToNumberStrategy objectToNumberStrategy = Gson.DEFAULT_OBJECT_TO_NUMBER_STRATEGY;
    private ToNumberStrategy numberToNumberStrategy = Gson.DEFAULT_NUMBER_TO_NUMBER_STRATEGY;
    private final LinkedList<ReflectionAccessFilter> reflectionFilters = new LinkedList();

    public GsonBuilder() {
    }

    GsonBuilder(Gson gson) {
        this.excluder = gson.excluder;
        this.fieldNamingPolicy = gson.fieldNamingStrategy;
        this.instanceCreators.putAll(gson.instanceCreators);
        this.serializeNulls = gson.serializeNulls;
        this.complexMapKeySerialization = gson.complexMapKeySerialization;
        this.generateNonExecutableJson = gson.generateNonExecutableJson;
        this.escapeHtmlChars = gson.htmlSafe;
        this.prettyPrinting = gson.prettyPrinting;
        this.lenient = gson.lenient;
        this.serializeSpecialFloatingPointValues = gson.serializeSpecialFloatingPointValues;
        this.longSerializationPolicy = gson.longSerializationPolicy;
        this.datePattern = gson.datePattern;
        this.dateStyle = gson.dateStyle;
        this.timeStyle = gson.timeStyle;
        this.factories.addAll(gson.builderFactories);
        this.hierarchyFactories.addAll(gson.builderHierarchyFactories);
        this.useJdkUnsafe = gson.useJdkUnsafe;
        this.objectToNumberStrategy = gson.objectToNumberStrategy;
        this.numberToNumberStrategy = gson.numberToNumberStrategy;
        this.reflectionFilters.addAll(gson.reflectionFilters);
    }

    public final GsonBuilder setVersion(double d2) {
        if (Double.isNaN(d2) || d2 < 0.0) {
            throw new IllegalArgumentException("Invalid version: " + d2);
        }
        this.excluder = this.excluder.withVersion(d2);
        return this;
    }

    public final GsonBuilder excludeFieldsWithModifiers(int ... nArray) {
        Objects.requireNonNull(nArray);
        this.excluder = this.excluder.withModifiers(nArray);
        return this;
    }

    public final GsonBuilder generateNonExecutableJson() {
        this.generateNonExecutableJson = true;
        return this;
    }

    public final GsonBuilder excludeFieldsWithoutExposeAnnotation() {
        this.excluder = this.excluder.excludeFieldsWithoutExposeAnnotation();
        return this;
    }

    public final GsonBuilder serializeNulls() {
        this.serializeNulls = true;
        return this;
    }

    public final GsonBuilder enableComplexMapKeySerialization() {
        this.complexMapKeySerialization = true;
        return this;
    }

    public final GsonBuilder disableInnerClassSerialization() {
        this.excluder = this.excluder.disableInnerClassSerialization();
        return this;
    }

    public final GsonBuilder setLongSerializationPolicy(LongSerializationPolicy longSerializationPolicy) {
        this.longSerializationPolicy = Objects.requireNonNull(longSerializationPolicy);
        return this;
    }

    public final GsonBuilder setFieldNamingPolicy(FieldNamingPolicy fieldNamingPolicy) {
        return this.setFieldNamingStrategy(fieldNamingPolicy);
    }

    public final GsonBuilder setFieldNamingStrategy(FieldNamingStrategy fieldNamingStrategy) {
        this.fieldNamingPolicy = Objects.requireNonNull(fieldNamingStrategy);
        return this;
    }

    public final GsonBuilder setObjectToNumberStrategy(ToNumberStrategy toNumberStrategy) {
        this.objectToNumberStrategy = Objects.requireNonNull(toNumberStrategy);
        return this;
    }

    public final GsonBuilder setNumberToNumberStrategy(ToNumberStrategy toNumberStrategy) {
        this.numberToNumberStrategy = Objects.requireNonNull(toNumberStrategy);
        return this;
    }

    public final GsonBuilder setExclusionStrategies(ExclusionStrategy ... exclusionStrategyArray) {
        Objects.requireNonNull(exclusionStrategyArray);
        for (ExclusionStrategy exclusionStrategy : exclusionStrategyArray) {
            this.excluder = this.excluder.withExclusionStrategy(exclusionStrategy, true, true);
        }
        return this;
    }

    public final GsonBuilder addSerializationExclusionStrategy(ExclusionStrategy exclusionStrategy) {
        Objects.requireNonNull(exclusionStrategy);
        this.excluder = this.excluder.withExclusionStrategy(exclusionStrategy, true, false);
        return this;
    }

    public final GsonBuilder addDeserializationExclusionStrategy(ExclusionStrategy exclusionStrategy) {
        Objects.requireNonNull(exclusionStrategy);
        this.excluder = this.excluder.withExclusionStrategy(exclusionStrategy, false, true);
        return this;
    }

    public final GsonBuilder setPrettyPrinting() {
        this.prettyPrinting = true;
        return this;
    }

    public final GsonBuilder setLenient() {
        this.lenient = true;
        return this;
    }

    public final GsonBuilder disableHtmlEscaping() {
        this.escapeHtmlChars = false;
        return this;
    }

    public final GsonBuilder setDateFormat(String string) {
        this.datePattern = string;
        return this;
    }

    public final GsonBuilder setDateFormat(int n2) {
        this.dateStyle = n2;
        this.datePattern = null;
        return this;
    }

    public final GsonBuilder setDateFormat(int n2, int n3) {
        this.dateStyle = n2;
        this.timeStyle = n3;
        this.datePattern = null;
        return this;
    }

    public final GsonBuilder registerTypeAdapter(Type type, Object object) {
        Object object2;
        Objects.requireNonNull(type);
        $Gson$Preconditions.checkArgument(object instanceof JsonSerializer || object instanceof JsonDeserializer || object instanceof InstanceCreator || object instanceof TypeAdapter);
        if (object instanceof InstanceCreator) {
            this.instanceCreators.put(type, (InstanceCreator)object);
        }
        if (object instanceof JsonSerializer || object instanceof JsonDeserializer) {
            object2 = TypeToken.get(type);
            this.factories.add(TreeTypeAdapter.newFactoryWithMatchRawType(object2, object));
        }
        if (object instanceof TypeAdapter) {
            object2 = TypeAdapters.newFactory(TypeToken.get(type), (TypeAdapter)object);
            this.factories.add((TypeAdapterFactory)object2);
        }
        return this;
    }

    public final GsonBuilder registerTypeAdapterFactory(TypeAdapterFactory typeAdapterFactory) {
        Objects.requireNonNull(typeAdapterFactory);
        this.factories.add(typeAdapterFactory);
        return this;
    }

    public final GsonBuilder registerTypeHierarchyAdapter(Class<?> object, Object object2) {
        Objects.requireNonNull(object);
        $Gson$Preconditions.checkArgument(object2 instanceof JsonSerializer || object2 instanceof JsonDeserializer || object2 instanceof TypeAdapter);
        if (object2 instanceof JsonDeserializer || object2 instanceof JsonSerializer) {
            this.hierarchyFactories.add(TreeTypeAdapter.newTypeHierarchyFactory(object, object2));
        }
        if (object2 instanceof TypeAdapter) {
            object = TypeAdapters.newTypeHierarchyFactory(object, (TypeAdapter)object2);
            this.factories.add((TypeAdapterFactory)object);
        }
        return this;
    }

    public final GsonBuilder serializeSpecialFloatingPointValues() {
        this.serializeSpecialFloatingPointValues = true;
        return this;
    }

    public final GsonBuilder disableJdkUnsafe() {
        this.useJdkUnsafe = false;
        return this;
    }

    public final GsonBuilder addReflectionAccessFilter(ReflectionAccessFilter reflectionAccessFilter) {
        Objects.requireNonNull(reflectionAccessFilter);
        this.reflectionFilters.addFirst(reflectionAccessFilter);
        return this;
    }

    public final Gson create() {
        ArrayList<TypeAdapterFactory> arrayList = new ArrayList<TypeAdapterFactory>(this.factories.size() + this.hierarchyFactories.size() + 3);
        arrayList.addAll(this.factories);
        Collections.reverse(arrayList);
        ArrayList<TypeAdapterFactory> arrayList2 = new ArrayList<TypeAdapterFactory>(this.hierarchyFactories);
        Collections.reverse(arrayList2);
        arrayList.addAll(arrayList2);
        GsonBuilder gsonBuilder = this;
        gsonBuilder.addTypeAdaptersForDate(gsonBuilder.datePattern, this.dateStyle, this.timeStyle, arrayList);
        return new Gson(this.excluder, this.fieldNamingPolicy, new HashMap(this.instanceCreators), this.serializeNulls, this.complexMapKeySerialization, this.generateNonExecutableJson, this.escapeHtmlChars, this.prettyPrinting, this.lenient, this.serializeSpecialFloatingPointValues, this.useJdkUnsafe, this.longSerializationPolicy, this.datePattern, this.dateStyle, this.timeStyle, new ArrayList<TypeAdapterFactory>(this.factories), new ArrayList<TypeAdapterFactory>(this.hierarchyFactories), arrayList, this.objectToNumberStrategy, this.numberToNumberStrategy, new ArrayList<ReflectionAccessFilter>(this.reflectionFilters));
    }

    private void addTypeAdaptersForDate(String string, int n2, int n3, List<TypeAdapterFactory> list2) {
        TypeAdapterFactory typeAdapterFactory;
        boolean bl = SqlTypesSupport.SUPPORTS_SQL_TYPES;
        TypeAdapterFactory typeAdapterFactory2 = null;
        TypeAdapterFactory typeAdapterFactory3 = null;
        if (string != null && !string.trim().isEmpty()) {
            typeAdapterFactory = DefaultDateTypeAdapter.DateType.DATE.createAdapterFactory(string);
            if (bl) {
                typeAdapterFactory2 = SqlTypesSupport.TIMESTAMP_DATE_TYPE.createAdapterFactory(string);
                typeAdapterFactory3 = SqlTypesSupport.DATE_DATE_TYPE.createAdapterFactory(string);
            }
        } else if (n2 != 2 && n3 != 2) {
            typeAdapterFactory = DefaultDateTypeAdapter.DateType.DATE.createAdapterFactory(n2, n3);
            if (bl) {
                typeAdapterFactory2 = SqlTypesSupport.TIMESTAMP_DATE_TYPE.createAdapterFactory(n2, n3);
                typeAdapterFactory3 = SqlTypesSupport.DATE_DATE_TYPE.createAdapterFactory(n2, n3);
            }
        } else {
            return;
        }
        list2.add(typeAdapterFactory);
        if (bl) {
            list2.add(typeAdapterFactory2);
            list2.add(typeAdapterFactory3);
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Preconditions;
import com.google.gson.internal.Streams;
import com.google.gson.internal.bind.SerializationDelegatingTypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;

public final class TreeTypeAdapter<T>
extends SerializationDelegatingTypeAdapter<T> {
    private final JsonSerializer<T> serializer;
    private final JsonDeserializer<T> deserializer;
    final Gson gson;
    private final TypeToken<T> typeToken;
    private final TypeAdapterFactory skipPast;
    private final GsonContextImpl context = new GsonContextImpl();
    private final boolean nullSafe;
    private volatile TypeAdapter<T> delegate;

    public TreeTypeAdapter(JsonSerializer<T> jsonSerializer, JsonDeserializer<T> jsonDeserializer, Gson gson, TypeToken<T> typeToken, TypeAdapterFactory typeAdapterFactory, boolean bl) {
        this.serializer = jsonSerializer;
        this.deserializer = jsonDeserializer;
        this.gson = gson;
        this.typeToken = typeToken;
        this.skipPast = typeAdapterFactory;
        this.nullSafe = bl;
    }

    public TreeTypeAdapter(JsonSerializer<T> jsonSerializer, JsonDeserializer<T> jsonDeserializer, Gson gson, TypeToken<T> typeToken, TypeAdapterFactory typeAdapterFactory) {
        this(jsonSerializer, jsonDeserializer, gson, typeToken, typeAdapterFactory, true);
    }

    @Override
    public final T read(JsonReader object) throws IOException {
        if (this.deserializer == null) {
            return this.delegate().read((JsonReader)object);
        }
        object = Streams.parse((JsonReader)object);
        if (this.nullSafe && ((JsonElement)object).isJsonNull()) {
            return null;
        }
        return this.deserializer.deserialize((JsonElement)object, this.typeToken.getType(), this.context);
    }

    @Override
    public final void write(JsonWriter jsonWriter, T object) throws IOException {
        if (this.serializer == null) {
            this.delegate().write(jsonWriter, object);
            return;
        }
        if (this.nullSafe && object == null) {
            jsonWriter.nullValue();
            return;
        }
        object = this.serializer.serialize(object, this.typeToken.getType(), this.context);
        Streams.write((JsonElement)object, jsonWriter);
    }

    private TypeAdapter<T> delegate() {
        TypeAdapter<T> typeAdapter = this.delegate;
        if (typeAdapter != null) {
            return typeAdapter;
        }
        this.delegate = this.gson.getDelegateAdapter(this.skipPast, this.typeToken);
        return this.delegate;
    }

    @Override
    public final TypeAdapter<T> getSerializationDelegate() {
        if (this.serializer != null) {
            return this;
        }
        return this.delegate();
    }

    public static TypeAdapterFactory newFactory(TypeToken<?> typeToken, Object object) {
        return new SingleTypeFactory(object, typeToken, false, null);
    }

    public static TypeAdapterFactory newFactoryWithMatchRawType(TypeToken<?> typeToken, Object object) {
        boolean bl = typeToken.getType() == typeToken.getRawType();
        return new SingleTypeFactory(object, typeToken, bl, null);
    }

    public static TypeAdapterFactory newTypeHierarchyFactory(Class<?> clazz, Object object) {
        return new SingleTypeFactory(object, null, false, clazz);
    }

    final class GsonContextImpl
    implements JsonDeserializationContext,
    JsonSerializationContext {
        private GsonContextImpl() {
        }

        @Override
        public final JsonElement serialize(Object object) {
            return TreeTypeAdapter.this.gson.toJsonTree(object);
        }

        @Override
        public final JsonElement serialize(Object object, Type type) {
            return TreeTypeAdapter.this.gson.toJsonTree(object, type);
        }

        public final <R> R deserialize(JsonElement jsonElement, Type type) throws JsonParseException {
            return (R)TreeTypeAdapter.this.gson.fromJson(jsonElement, type);
        }
    }

    static final class SingleTypeFactory
    implements TypeAdapterFactory {
        private final TypeToken<?> exactType;
        private final boolean matchRawType;
        private final Class<?> hierarchyType;
        private final JsonSerializer<?> serializer;
        private final JsonDeserializer<?> deserializer;

        SingleTypeFactory(Object object, TypeToken<?> typeToken, boolean bl, Class<?> clazz) {
            this.serializer = object instanceof JsonSerializer ? (JsonSerializer)object : null;
            this.deserializer = object instanceof JsonDeserializer ? (JsonDeserializer)object : null;
            $Gson$Preconditions.checkArgument(this.serializer != null || this.deserializer != null);
            this.exactType = typeToken;
            this.matchRawType = bl;
            this.hierarchyType = clazz;
        }

        @Override
        public final <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            boolean bl;
            boolean bl2 = this.exactType != null ? this.exactType.equals(typeToken) || this.matchRawType && this.exactType.getType() == typeToken.getRawType() : (bl = this.hierarchyType.isAssignableFrom(typeToken.getRawType()));
            if (bl2) {
                return new TreeTypeAdapter(this.serializer, this.deserializer, gson, typeToken, this);
            }
            return null;
        }
    }
}


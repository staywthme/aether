/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.bind.TreeTypeAdapter;
import com.google.gson.reflect.TypeToken;

public final class JsonAdapterAnnotationTypeAdapterFactory
implements TypeAdapterFactory {
    private final ConstructorConstructor constructorConstructor;

    public JsonAdapterAnnotationTypeAdapterFactory(ConstructorConstructor constructorConstructor) {
        this.constructorConstructor = constructorConstructor;
    }

    @Override
    public final <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        Object object = typeToken.getRawType();
        if ((object = ((Class)object).getAnnotation(JsonAdapter.class)) == null) {
            return null;
        }
        JsonAdapterAnnotationTypeAdapterFactory jsonAdapterAnnotationTypeAdapterFactory = this;
        return jsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(jsonAdapterAnnotationTypeAdapterFactory.constructorConstructor, gson, typeToken, (JsonAdapter)object);
    }

    final TypeAdapter<?> getTypeAdapter(ConstructorConstructor treeTypeAdapter, Gson gson, TypeToken<?> typeToken, JsonAdapter jsonAdapter) {
        treeTypeAdapter = ((ConstructorConstructor)((Object)treeTypeAdapter)).get(TypeToken.get(jsonAdapter.value())).construct();
        boolean bl = jsonAdapter.nullSafe();
        if (treeTypeAdapter instanceof TypeAdapter) {
            treeTypeAdapter = treeTypeAdapter;
        } else if (treeTypeAdapter instanceof TypeAdapterFactory) {
            treeTypeAdapter = ((TypeAdapterFactory)((Object)treeTypeAdapter)).create(gson, typeToken);
        } else if (treeTypeAdapter instanceof JsonSerializer || treeTypeAdapter instanceof JsonDeserializer) {
            JsonSerializer jsonSerializer = treeTypeAdapter instanceof JsonSerializer ? (JsonSerializer)((Object)treeTypeAdapter) : null;
            treeTypeAdapter = treeTypeAdapter instanceof JsonDeserializer ? (JsonDeserializer)((Object)treeTypeAdapter) : null;
            treeTypeAdapter = new TreeTypeAdapter(jsonSerializer, (JsonDeserializer<?>)((Object)treeTypeAdapter), gson, typeToken, null, bl);
            bl = false;
        } else {
            throw new IllegalArgumentException("Invalid attempt to bind an instance of " + treeTypeAdapter.getClass().getName() + " as a @JsonAdapter for " + typeToken.toString() + ". @JsonAdapter value must be a TypeAdapter, TypeAdapterFactory, JsonSerializer or JsonDeserializer.");
        }
        if (treeTypeAdapter != null && bl) {
            treeTypeAdapter = treeTypeAdapter.nullSafe();
        }
        return treeTypeAdapter;
    }
}


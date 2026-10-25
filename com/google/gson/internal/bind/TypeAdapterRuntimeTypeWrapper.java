/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.internal.bind.SerializationDelegatingTypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

final class TypeAdapterRuntimeTypeWrapper<T>
extends TypeAdapter<T> {
    private final Gson context;
    private final TypeAdapter<T> delegate;
    private final Type type;

    TypeAdapterRuntimeTypeWrapper(Gson gson, TypeAdapter<T> typeAdapter, Type type) {
        this.context = gson;
        this.delegate = typeAdapter;
        this.type = type;
    }

    @Override
    public final T read(JsonReader jsonReader) throws IOException {
        return this.delegate.read(jsonReader);
    }

    @Override
    public final void write(JsonWriter jsonWriter, T t2) throws IOException {
        TypeAdapter<Object> typeAdapter = this.delegate;
        Type type = TypeAdapterRuntimeTypeWrapper.getRuntimeTypeIfMoreSpecific(this.type, t2);
        if (type != this.type && (typeAdapter = this.context.getAdapter(TypeToken.get(type))) instanceof ReflectiveTypeAdapterFactory.Adapter && !TypeAdapterRuntimeTypeWrapper.isReflective(this.delegate)) {
            typeAdapter = this.delegate;
        }
        typeAdapter.write(jsonWriter, t2);
    }

    private static boolean isReflective(TypeAdapter<?> typeAdapter) {
        TypeAdapter typeAdapter2;
        while (typeAdapter instanceof SerializationDelegatingTypeAdapter && (typeAdapter2 = ((SerializationDelegatingTypeAdapter)typeAdapter).getSerializationDelegate()) != typeAdapter) {
            typeAdapter = typeAdapter2;
        }
        return typeAdapter instanceof ReflectiveTypeAdapterFactory.Adapter;
    }

    private static Type getRuntimeTypeIfMoreSpecific(Type clazz, Object object) {
        if (object != null && (clazz instanceof Class || clazz instanceof TypeVariable)) {
            clazz = object.getClass();
        }
        return clazz;
    }
}


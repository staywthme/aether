/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.bind.TypeAdapterRuntimeTypeWrapper;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.util.ArrayList;

public final class ArrayTypeAdapter<E>
extends TypeAdapter<Object> {
    public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory(){

        @Override
        public <T> TypeAdapter<T> create(Gson object, TypeToken<T> object2) {
            if (!((object2 = ((TypeToken)object2).getType()) instanceof GenericArrayType || object2 instanceof Class && ((Class)object2).isArray())) {
                return null;
            }
            object2 = $Gson$Types.getArrayComponentType((Type)object2);
            TypeAdapter<?> typeAdapter = ((Gson)object).getAdapter(TypeToken.get((Type)object2));
            object = new ArrayTypeAdapter((Gson)object, typeAdapter, $Gson$Types.getRawType((Type)object2));
            return object;
        }
    };
    private final Class<E> componentType;
    private final TypeAdapter<E> componentTypeAdapter;

    public ArrayTypeAdapter(Gson gson, TypeAdapter<E> typeAdapter, Class<E> clazz) {
        this.componentTypeAdapter = new TypeAdapterRuntimeTypeWrapper<E>(gson, typeAdapter, clazz);
        this.componentType = clazz;
    }

    @Override
    public final Object read(JsonReader objectArray) throws IOException {
        if (objectArray.peek() == JsonToken.NULL) {
            objectArray.nextNull();
            return null;
        }
        ArrayList<E> arrayList = new ArrayList<E>();
        objectArray.beginArray();
        while (objectArray.hasNext()) {
            E e2 = this.componentTypeAdapter.read((JsonReader)objectArray);
            arrayList.add(e2);
        }
        objectArray.endArray();
        int n2 = arrayList.size();
        if (this.componentType.isPrimitive()) {
            objectArray = Array.newInstance(this.componentType, n2);
            for (int i2 = 0; i2 < n2; ++i2) {
                Array.set(objectArray, i2, arrayList.get(i2));
            }
            return objectArray;
        }
        objectArray = (Object[])Array.newInstance(this.componentType, n2);
        return arrayList.toArray(objectArray);
    }

    @Override
    public final void write(JsonWriter jsonWriter, Object object) throws IOException {
        if (object == null) {
            jsonWriter.nullValue();
            return;
        }
        jsonWriter.beginArray();
        int n2 = Array.getLength(object);
        for (int i2 = 0; i2 < n2; ++i2) {
            Object object2 = Array.get(object, i2);
            this.componentTypeAdapter.write(jsonWriter, object2);
        }
        jsonWriter.endArray();
    }
}


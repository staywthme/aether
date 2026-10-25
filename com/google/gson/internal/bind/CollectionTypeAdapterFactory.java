/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.bind.TypeAdapterRuntimeTypeWrapper;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;

public final class CollectionTypeAdapterFactory
implements TypeAdapterFactory {
    private final ConstructorConstructor constructorConstructor;

    public CollectionTypeAdapterFactory(ConstructorConstructor constructorConstructor) {
        this.constructorConstructor = constructorConstructor;
    }

    @Override
    public final <T> TypeAdapter<T> create(Gson object, TypeToken<T> object2) {
        Type type = ((TypeToken)object2).getType();
        Class<T> clazz = ((TypeToken)object2).getRawType();
        if (!Collection.class.isAssignableFrom(clazz)) {
            return null;
        }
        type = $Gson$Types.getCollectionElementType(type, clazz);
        clazz = ((Gson)object).getAdapter(TypeToken.get(type));
        object2 = this.constructorConstructor.get(object2);
        object = new Adapter((Gson)object, type, clazz, object2);
        return object;
    }

    static final class Adapter<E>
    extends TypeAdapter<Collection<E>> {
        private final TypeAdapter<E> elementTypeAdapter;
        private final ObjectConstructor<? extends Collection<E>> constructor;

        public Adapter(Gson gson, Type type, TypeAdapter<E> typeAdapter, ObjectConstructor<? extends Collection<E>> objectConstructor) {
            this.elementTypeAdapter = new TypeAdapterRuntimeTypeWrapper<E>(gson, typeAdapter, type);
            this.constructor = objectConstructor;
        }

        @Override
        public final Collection<E> read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            Collection<E> collection = this.constructor.construct();
            jsonReader.beginArray();
            while (jsonReader.hasNext()) {
                E e2 = this.elementTypeAdapter.read(jsonReader);
                collection.add(e2);
            }
            jsonReader.endArray();
            return collection;
        }

        @Override
        public final void write(JsonWriter jsonWriter, Collection<E> object) throws IOException {
            if (object == null) {
                jsonWriter.nullValue();
                return;
            }
            jsonWriter.beginArray();
            object = object.iterator();
            while (object.hasNext()) {
                Object e2 = object.next();
                this.elementTypeAdapter.write(jsonWriter, e2);
            }
            jsonWriter.endArray();
        }
    }
}


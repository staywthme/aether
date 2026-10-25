/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.ToNumberPolicy;
import com.google.gson.ToNumberStrategy;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ObjectTypeAdapter
extends TypeAdapter<Object> {
    private static final TypeAdapterFactory DOUBLE_FACTORY = ObjectTypeAdapter.newFactory(ToNumberPolicy.DOUBLE);
    private final Gson gson;
    private final ToNumberStrategy toNumberStrategy;

    private ObjectTypeAdapter(Gson gson, ToNumberStrategy toNumberStrategy) {
        this.gson = gson;
        this.toNumberStrategy = toNumberStrategy;
    }

    private static TypeAdapterFactory newFactory(final ToNumberStrategy toNumberStrategy) {
        return new TypeAdapterFactory(){

            @Override
            public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
                if (typeToken.getRawType() == Object.class) {
                    return new ObjectTypeAdapter(gson, toNumberStrategy);
                }
                return null;
            }
        };
    }

    public static TypeAdapterFactory getFactory(ToNumberStrategy toNumberStrategy) {
        if (toNumberStrategy == ToNumberPolicy.DOUBLE) {
            return DOUBLE_FACTORY;
        }
        return ObjectTypeAdapter.newFactory(toNumberStrategy);
    }

    private Object tryBeginNesting(JsonReader jsonReader, JsonToken jsonToken) throws IOException {
        switch (jsonToken) {
            case BEGIN_ARRAY: {
                jsonReader.beginArray();
                return new ArrayList();
            }
            case BEGIN_OBJECT: {
                jsonReader.beginObject();
                return new LinkedTreeMap();
            }
        }
        return null;
    }

    private Object readTerminal(JsonReader jsonReader, JsonToken jsonToken) throws IOException {
        switch (jsonToken) {
            case STRING: {
                return jsonReader.nextString();
            }
            case NUMBER: {
                return this.toNumberStrategy.readNumber(jsonReader);
            }
            case BOOLEAN: {
                return jsonReader.nextBoolean();
            }
            case NULL: {
                jsonReader.nextNull();
                return null;
            }
        }
        throw new IllegalStateException("Unexpected token: " + (Object)((Object)jsonToken));
    }

    @Override
    public final Object read(JsonReader jsonReader) throws IOException {
        Object object = jsonReader.peek();
        Object object2 = this.tryBeginNesting(jsonReader, (JsonToken)((Object)object));
        if (object2 == null) {
            return this.readTerminal(jsonReader, (JsonToken)((Object)object));
        }
        ArrayDeque<Object> arrayDeque = new ArrayDeque<Object>();
        while (true) {
            if (jsonReader.hasNext()) {
                Object object3;
                boolean bl;
                String string = null;
                if (object2 instanceof Map) {
                    string = jsonReader.nextName();
                }
                boolean bl2 = bl = (object3 = this.tryBeginNesting(jsonReader, (JsonToken)((Object)(object = jsonReader.peek())))) != null;
                if (object3 == null) {
                    object3 = this.readTerminal(jsonReader, (JsonToken)((Object)object));
                }
                if (object2 instanceof List) {
                    object = (List)object2;
                    object.add(object3);
                } else {
                    object = (Map)object2;
                    object.put(string, object3);
                }
                if (!bl) continue;
                arrayDeque.addLast(object2);
                object2 = object3;
                continue;
            }
            if (object2 instanceof List) {
                jsonReader.endArray();
            } else {
                jsonReader.endObject();
            }
            if (arrayDeque.isEmpty()) {
                return object2;
            }
            object2 = arrayDeque.removeLast();
        }
    }

    @Override
    public final void write(JsonWriter jsonWriter, Object object) throws IOException {
        if (object == null) {
            jsonWriter.nullValue();
            return;
        }
        TypeAdapter<?> typeAdapter = this.gson.getAdapter(object.getClass());
        if (typeAdapter instanceof ObjectTypeAdapter) {
            jsonWriter.beginObject();
            jsonWriter.endObject();
            return;
        }
        typeAdapter.write(jsonWriter, object);
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Streams;
import com.google.gson.internal.bind.TypeAdapterRuntimeTypeWrapper;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

public final class MapTypeAdapterFactory
implements TypeAdapterFactory {
    private final ConstructorConstructor constructorConstructor;
    final boolean complexMapKeySerialization;

    public MapTypeAdapterFactory(ConstructorConstructor constructorConstructor, boolean bl) {
        this.constructorConstructor = constructorConstructor;
        this.complexMapKeySerialization = bl;
    }

    @Override
    public final <T> TypeAdapter<T> create(Gson object, TypeToken<T> object2) {
        Type[] typeArray = ((TypeToken)object2).getType();
        Class<T> clazz = ((TypeToken)object2).getRawType();
        if (!Map.class.isAssignableFrom(clazz)) {
            return null;
        }
        typeArray = $Gson$Types.getMapKeyAndValueTypes((Type)typeArray, clazz);
        clazz = this.getKeyAdapter((Gson)object, typeArray[0]);
        TypeAdapter<?> typeAdapter = ((Gson)object).getAdapter(TypeToken.get(typeArray[1]));
        object2 = this.constructorConstructor.get(object2);
        object = new Adapter((Gson)object, typeArray[0], clazz, typeArray[1], typeAdapter, object2);
        return object;
    }

    private TypeAdapter<?> getKeyAdapter(Gson gson, Type type) {
        if (type == Boolean.TYPE || type == Boolean.class) {
            return TypeAdapters.BOOLEAN_AS_STRING;
        }
        return gson.getAdapter(TypeToken.get(type));
    }

    final class Adapter<K, V>
    extends TypeAdapter<Map<K, V>> {
        private final TypeAdapter<K> keyTypeAdapter;
        private final TypeAdapter<V> valueTypeAdapter;
        private final ObjectConstructor<? extends Map<K, V>> constructor;

        public Adapter(Gson gson, Type type, TypeAdapter<K> typeAdapter, Type type2, TypeAdapter<V> typeAdapter2, ObjectConstructor<? extends Map<K, V>> objectConstructor) {
            this.keyTypeAdapter = new TypeAdapterRuntimeTypeWrapper<K>(gson, typeAdapter, type);
            this.valueTypeAdapter = new TypeAdapterRuntimeTypeWrapper<V>(gson, typeAdapter2, type2);
            this.constructor = objectConstructor;
        }

        @Override
        public final Map<K, V> read(JsonReader jsonReader) throws IOException {
            JsonToken jsonToken = jsonReader.peek();
            if (jsonToken == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            Map<JsonToken, V> map = this.constructor.construct();
            if (jsonToken == JsonToken.BEGIN_ARRAY) {
                jsonReader.beginArray();
                while (jsonReader.hasNext()) {
                    jsonReader.beginArray();
                    jsonToken = this.keyTypeAdapter.read(jsonReader);
                    V v2 = this.valueTypeAdapter.read(jsonReader);
                    v2 = map.put(jsonToken, v2);
                    if (v2 != null) {
                        throw new JsonSyntaxException("duplicate key: " + (Object)((Object)jsonToken));
                    }
                    jsonReader.endArray();
                }
                jsonReader.endArray();
            } else {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    JsonReaderInternalAccess.INSTANCE.promoteNameToValue(jsonReader);
                    jsonToken = this.keyTypeAdapter.read(jsonReader);
                    V v3 = this.valueTypeAdapter.read(jsonReader);
                    if ((v3 = map.put(jsonToken, v3)) == null) continue;
                    throw new JsonSyntaxException("duplicate key: " + (Object)((Object)jsonToken));
                }
                jsonReader.endObject();
            }
            return map;
        }

        @Override
        public final void write(JsonWriter jsonWriter, Map<K, V> object) throws IOException {
            JsonElement jsonElement;
            if (object == null) {
                jsonWriter.nullValue();
                return;
            }
            if (!MapTypeAdapterFactory.this.complexMapKeySerialization) {
                jsonWriter.beginObject();
                for (Map.Entry entry : object.entrySet()) {
                    jsonWriter.name(String.valueOf(entry.getKey()));
                    this.valueTypeAdapter.write(jsonWriter, entry.getValue());
                }
                jsonWriter.endObject();
                return;
            }
            boolean bl = false;
            ArrayList<JsonElement> arrayList = new ArrayList<JsonElement>(object.size());
            ArrayList arrayList2 = new ArrayList(object.size());
            for (Map.Entry entry : object.entrySet()) {
                jsonElement = this.keyTypeAdapter.toJsonTree(entry.getKey());
                arrayList.add(jsonElement);
                arrayList2.add(entry.getValue());
                bl |= jsonElement.isJsonArray() || jsonElement.isJsonObject();
            }
            if (bl) {
                jsonWriter.beginArray();
                int n2 = arrayList.size();
                for (int i2 = 0; i2 < n2; ++i2) {
                    jsonWriter.beginArray();
                    Streams.write((JsonElement)arrayList.get(i2), jsonWriter);
                    this.valueTypeAdapter.write(jsonWriter, arrayList2.get(i2));
                    jsonWriter.endArray();
                }
                jsonWriter.endArray();
                return;
            }
            jsonWriter.beginObject();
            int n3 = arrayList.size();
            for (int i3 = 0; i3 < n3; ++i3) {
                jsonElement = (JsonElement)arrayList.get(i3);
                jsonWriter.name(this.keyToString(jsonElement));
                this.valueTypeAdapter.write(jsonWriter, arrayList2.get(i3));
            }
            jsonWriter.endObject();
        }

        private String keyToString(JsonElement jsonElement) {
            if (jsonElement.isJsonPrimitive()) {
                if (((JsonPrimitive)(jsonElement = jsonElement.getAsJsonPrimitive())).isNumber()) {
                    return String.valueOf(jsonElement.getAsNumber());
                }
                if (((JsonPrimitive)jsonElement).isBoolean()) {
                    return Boolean.toString(jsonElement.getAsBoolean());
                }
                if (((JsonPrimitive)jsonElement).isString()) {
                    return jsonElement.getAsString();
                }
                throw new AssertionError();
            }
            if (jsonElement.isJsonNull()) {
                return "null";
            }
            throw new AssertionError();
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.ToNumberPolicy;
import com.google.gson.ToNumberStrategy;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

public final class NumberTypeAdapter
extends TypeAdapter<Number> {
    private static final TypeAdapterFactory LAZILY_PARSED_NUMBER_FACTORY = NumberTypeAdapter.newFactory(ToNumberPolicy.LAZILY_PARSED_NUMBER);
    private final ToNumberStrategy toNumberStrategy;

    private NumberTypeAdapter(ToNumberStrategy toNumberStrategy) {
        this.toNumberStrategy = toNumberStrategy;
    }

    private static TypeAdapterFactory newFactory(ToNumberStrategy object) {
        object = new NumberTypeAdapter((ToNumberStrategy)object);
        return new TypeAdapterFactory(){

            @Override
            public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
                if (typeToken.getRawType() == Number.class) {
                    return NumberTypeAdapter.this;
                }
                return null;
            }
        };
    }

    public static TypeAdapterFactory getFactory(ToNumberStrategy toNumberStrategy) {
        if (toNumberStrategy == ToNumberPolicy.LAZILY_PARSED_NUMBER) {
            return LAZILY_PARSED_NUMBER_FACTORY;
        }
        return NumberTypeAdapter.newFactory(toNumberStrategy);
    }

    @Override
    public final Number read(JsonReader jsonReader) throws IOException {
        JsonToken jsonToken = jsonReader.peek();
        switch (jsonToken) {
            case NULL: {
                jsonReader.nextNull();
                return null;
            }
            case NUMBER: 
            case STRING: {
                return this.toNumberStrategy.readNumber(jsonReader);
            }
        }
        throw new JsonSyntaxException("Expecting number, got: " + (Object)((Object)jsonToken) + "; at path " + jsonReader.getPath());
    }

    @Override
    public final void write(JsonWriter jsonWriter, Number number) throws IOException {
        jsonWriter.value(number);
    }
}


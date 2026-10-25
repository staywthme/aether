/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson.internal.sql;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

class SqlTimestampTypeAdapter
extends TypeAdapter<Timestamp> {
    static final TypeAdapterFactory FACTORY = new TypeAdapterFactory(){

        @Override
        public <T> TypeAdapter<T> create(Gson object, TypeToken<T> typeToken) {
            if (typeToken.getRawType() == Timestamp.class) {
                object = ((Gson)object).getAdapter(Date.class);
                return new SqlTimestampTypeAdapter((TypeAdapter)object);
            }
            return null;
        }
    };
    private final TypeAdapter<Date> dateTypeAdapter;

    private SqlTimestampTypeAdapter(TypeAdapter<Date> typeAdapter) {
        this.dateTypeAdapter = typeAdapter;
    }

    @Override
    public Timestamp read(JsonReader object) throws IOException {
        if ((object = this.dateTypeAdapter.read((JsonReader)object)) != null) {
            return new Timestamp(((Date)object).getTime());
        }
        return null;
    }

    @Override
    public void write(JsonWriter jsonWriter, Timestamp timestamp) throws IOException {
        this.dateTypeAdapter.write(jsonWriter, timestamp);
    }
}


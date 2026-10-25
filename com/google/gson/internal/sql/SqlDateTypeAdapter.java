/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson.internal.sql;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

final class SqlDateTypeAdapter
extends TypeAdapter<java.sql.Date> {
    static final TypeAdapterFactory FACTORY = new TypeAdapterFactory(){

        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            if (typeToken.getRawType() == java.sql.Date.class) {
                return new SqlDateTypeAdapter();
            }
            return null;
        }
    };
    private final DateFormat format = new SimpleDateFormat("MMM d, yyyy");

    private SqlDateTypeAdapter() {
    }

    @Override
    public final java.sql.Date read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        String string = jsonReader.nextString();
        try {
            Date date;
            SqlDateTypeAdapter sqlDateTypeAdapter = this;
            synchronized (sqlDateTypeAdapter) {
                date = this.format.parse(string);
            }
            return new java.sql.Date(date.getTime());
        }
        catch (ParseException parseException) {
            throw new JsonSyntaxException("Failed parsing '" + string + "' as SQL Date; at path " + jsonReader.getPreviousPath(), parseException);
        }
    }

    @Override
    public final void write(JsonWriter jsonWriter, java.sql.Date object) throws IOException {
        if (object == null) {
            jsonWriter.nullValue();
            return;
        }
        SqlDateTypeAdapter sqlDateTypeAdapter = this;
        synchronized (sqlDateTypeAdapter) {
            object = this.format.format((Date)object);
        }
        jsonWriter.value((String)object);
    }
}


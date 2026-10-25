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
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

final class SqlTimeTypeAdapter
extends TypeAdapter<Time> {
    static final TypeAdapterFactory FACTORY = new TypeAdapterFactory(){

        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            if (typeToken.getRawType() == Time.class) {
                return new SqlTimeTypeAdapter();
            }
            return null;
        }
    };
    private final DateFormat format = new SimpleDateFormat("hh:mm:ss a");

    private SqlTimeTypeAdapter() {
    }

    @Override
    public final Time read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        String string = jsonReader.nextString();
        try {
            SqlTimeTypeAdapter sqlTimeTypeAdapter = this;
            synchronized (sqlTimeTypeAdapter) {
                Date date = this.format.parse(string);
                return new Time(date.getTime());
            }
        }
        catch (ParseException parseException) {
            throw new JsonSyntaxException("Failed parsing '" + string + "' as SQL Time; at path " + jsonReader.getPreviousPath(), parseException);
        }
    }

    @Override
    public final void write(JsonWriter jsonWriter, Time object) throws IOException {
        if (object == null) {
            jsonWriter.nullValue();
            return;
        }
        SqlTimeTypeAdapter sqlTimeTypeAdapter = this;
        synchronized (sqlTimeTypeAdapter) {
            object = this.format.format((Date)object);
        }
        jsonWriter.value((String)object);
    }
}


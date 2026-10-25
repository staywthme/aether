/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.JavaVersion;
import com.google.gson.internal.PreJava9DateFormatProvider;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public final class DateTypeAdapter
extends TypeAdapter<Date> {
    public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory(){

        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            if (typeToken.getRawType() == Date.class) {
                return new DateTypeAdapter();
            }
            return null;
        }
    };
    private final List<DateFormat> dateFormats = new ArrayList<DateFormat>();

    public DateTypeAdapter() {
        this.dateFormats.add(DateFormat.getDateTimeInstance(2, 2, Locale.US));
        if (!Locale.getDefault().equals(Locale.US)) {
            this.dateFormats.add(DateFormat.getDateTimeInstance(2, 2));
        }
        if (JavaVersion.isJava9OrLater()) {
            this.dateFormats.add(PreJava9DateFormatProvider.getUSDateTimeFormat(2, 2));
        }
    }

    @Override
    public final Date read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        return this.deserializeToDate(jsonReader);
    }

    private Date deserializeToDate(JsonReader jsonReader) throws IOException {
        String string = jsonReader.nextString();
        List<DateFormat> list2 = this.dateFormats;
        synchronized (list2) {
            for (DateFormat dateFormat : this.dateFormats) {
                try {
                    return dateFormat.parse(string);
                }
                catch (ParseException parseException) {
                }
            }
            try {
                return ISO8601Utils.parse(string, new ParsePosition(0));
            }
            catch (ParseException parseException) {
                throw new JsonSyntaxException("Failed parsing '" + string + "' as Date; at path " + jsonReader.getPreviousPath(), parseException);
            }
        }
    }

    @Override
    public final void write(JsonWriter jsonWriter, Date object) throws IOException {
        if (object == null) {
            jsonWriter.nullValue();
            return;
        }
        DateFormat dateFormat = this.dateFormats.get(0);
        List<DateFormat> list2 = this.dateFormats;
        synchronized (list2) {
            object = dateFormat.format((Date)object);
        }
        jsonWriter.value((String)object);
    }
}


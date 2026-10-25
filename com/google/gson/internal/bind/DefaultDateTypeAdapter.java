/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson.internal.bind;

import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.JavaVersion;
import com.google.gson.internal.PreJava9DateFormatProvider;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public final class DefaultDateTypeAdapter<T extends Date>
extends TypeAdapter<T> {
    private static final String SIMPLE_NAME = "DefaultDateTypeAdapter";
    private final DateType<T> dateType;
    private final List<DateFormat> dateFormats = new ArrayList<DateFormat>();

    private DefaultDateTypeAdapter(DateType<T> dateType, String string) {
        this.dateType = Objects.requireNonNull(dateType);
        this.dateFormats.add(new SimpleDateFormat(string, Locale.US));
        if (!Locale.getDefault().equals(Locale.US)) {
            this.dateFormats.add(new SimpleDateFormat(string));
        }
    }

    private DefaultDateTypeAdapter(DateType<T> dateType, int n2) {
        this.dateType = Objects.requireNonNull(dateType);
        this.dateFormats.add(DateFormat.getDateInstance(n2, Locale.US));
        if (!Locale.getDefault().equals(Locale.US)) {
            this.dateFormats.add(DateFormat.getDateInstance(n2));
        }
        if (JavaVersion.isJava9OrLater()) {
            this.dateFormats.add(PreJava9DateFormatProvider.getUSDateFormat(n2));
        }
    }

    private DefaultDateTypeAdapter(DateType<T> dateType, int n2, int n3) {
        this.dateType = Objects.requireNonNull(dateType);
        this.dateFormats.add(DateFormat.getDateTimeInstance(n2, n3, Locale.US));
        if (!Locale.getDefault().equals(Locale.US)) {
            this.dateFormats.add(DateFormat.getDateTimeInstance(n2, n3));
        }
        if (JavaVersion.isJava9OrLater()) {
            this.dateFormats.add(PreJava9DateFormatProvider.getUSDateTimeFormat(n2, n3));
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

    @Override
    public final T read(JsonReader object) throws IOException {
        if (((JsonReader)object).peek() == JsonToken.NULL) {
            ((JsonReader)object).nextNull();
            return null;
        }
        object = this.deserializeToDate((JsonReader)object);
        return this.dateType.deserialize((Date)object);
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

    public final String toString() {
        DateFormat dateFormat = this.dateFormats.get(0);
        if (dateFormat instanceof SimpleDateFormat) {
            return "DefaultDateTypeAdapter(" + ((SimpleDateFormat)dateFormat).toPattern() + ')';
        }
        return "DefaultDateTypeAdapter(" + dateFormat.getClass().getSimpleName() + ')';
    }

    public static abstract class DateType<T extends Date> {
        public static final DateType<Date> DATE = new DateType<Date>(Date.class){

            @Override
            protected Date deserialize(Date date) {
                return date;
            }
        };
        private final Class<T> dateClass;

        public DateType(Class<T> clazz) {
            this.dateClass = clazz;
        }

        protected abstract T deserialize(Date var1);

        private TypeAdapterFactory createFactory(DefaultDateTypeAdapter<T> defaultDateTypeAdapter) {
            return TypeAdapters.newFactory(this.dateClass, defaultDateTypeAdapter);
        }

        public final TypeAdapterFactory createAdapterFactory(String string) {
            return this.createFactory(new DefaultDateTypeAdapter(this, string));
        }

        public final TypeAdapterFactory createAdapterFactory(int n2) {
            return this.createFactory(new DefaultDateTypeAdapter(this, n2));
        }

        public final TypeAdapterFactory createAdapterFactory(int n2, int n3) {
            return this.createFactory(new DefaultDateTypeAdapter(this, n2, n3));
        }

        public final TypeAdapterFactory createDefaultsAdapterFactory() {
            return this.createFactory(new DefaultDateTypeAdapter(this, 2, 2));
        }
    }
}


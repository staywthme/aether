/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.LazilyParsedNumber;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Currency;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

public final class TypeAdapters {
    public static final TypeAdapter<Class> CLASS = new TypeAdapter<Class>(){

        @Override
        public void write(JsonWriter jsonWriter, Class clazz) throws IOException {
            throw new UnsupportedOperationException("Attempted to serialize java.lang.Class: " + clazz.getName() + ". Forgot to register a type adapter?");
        }

        @Override
        public Class read(JsonReader jsonReader) throws IOException {
            throw new UnsupportedOperationException("Attempted to deserialize a java.lang.Class. Forgot to register a type adapter?");
        }
    }.nullSafe();
    public static final TypeAdapterFactory CLASS_FACTORY = TypeAdapters.newFactory(Class.class, CLASS);
    public static final TypeAdapter<BitSet> BIT_SET = new TypeAdapter<BitSet>(){

        @Override
        public BitSet read(JsonReader jsonReader) throws IOException {
            BitSet bitSet = new BitSet();
            jsonReader.beginArray();
            int n2 = 0;
            JsonToken jsonToken = jsonReader.peek();
            while (jsonToken != JsonToken.END_ARRAY) {
                int n3;
                switch (jsonToken) {
                    case NUMBER: 
                    case STRING: {
                        n3 = jsonReader.nextInt();
                        if (n3 == 0) {
                            n3 = 0;
                            break;
                        }
                        if (n3 == 1) {
                            n3 = 1;
                            break;
                        }
                        throw new JsonSyntaxException("Invalid bitset value " + n3 + ", expected 0 or 1; at path " + jsonReader.getPreviousPath());
                    }
                    case BOOLEAN: {
                        n3 = jsonReader.nextBoolean();
                        break;
                    }
                    default: {
                        throw new JsonSyntaxException("Invalid bitset value type: " + (Object)((Object)jsonToken) + "; at path " + jsonReader.getPath());
                    }
                }
                if (n3 != 0) {
                    bitSet.set(n2);
                }
                ++n2;
                jsonToken = jsonReader.peek();
            }
            jsonReader.endArray();
            return bitSet;
        }

        @Override
        public void write(JsonWriter jsonWriter, BitSet bitSet) throws IOException {
            jsonWriter.beginArray();
            int n2 = bitSet.length();
            for (int i2 = 0; i2 < n2; ++i2) {
                int n3 = bitSet.get(i2) ? 1 : 0;
                jsonWriter.value(n3);
            }
            jsonWriter.endArray();
        }
    }.nullSafe();
    public static final TypeAdapterFactory BIT_SET_FACTORY = TypeAdapters.newFactory(BitSet.class, BIT_SET);
    public static final TypeAdapter<Boolean> BOOLEAN = new TypeAdapter<Boolean>(){

        @Override
        public Boolean read(JsonReader jsonReader) throws IOException {
            JsonToken jsonToken = jsonReader.peek();
            if (jsonToken == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            if (jsonToken == JsonToken.STRING) {
                return Boolean.parseBoolean(jsonReader.nextString());
            }
            return jsonReader.nextBoolean();
        }

        @Override
        public void write(JsonWriter jsonWriter, Boolean bl) throws IOException {
            jsonWriter.value(bl);
        }
    };
    public static final TypeAdapter<Boolean> BOOLEAN_AS_STRING = new TypeAdapter<Boolean>(){

        @Override
        public Boolean read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            return Boolean.valueOf(jsonReader.nextString());
        }

        @Override
        public void write(JsonWriter jsonWriter, Boolean bl) throws IOException {
            jsonWriter.value(bl == null ? "null" : bl.toString());
        }
    };
    public static final TypeAdapterFactory BOOLEAN_FACTORY = TypeAdapters.newFactory(Boolean.TYPE, Boolean.class, BOOLEAN);
    public static final TypeAdapter<Number> BYTE = new TypeAdapter<Number>(){

        @Override
        public Number read(JsonReader jsonReader) throws IOException {
            int n2;
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            try {
                n2 = jsonReader.nextInt();
            }
            catch (NumberFormatException numberFormatException) {
                throw new JsonSyntaxException(numberFormatException);
            }
            if (n2 > 255 || n2 < -128) {
                throw new JsonSyntaxException("Lossy conversion from " + n2 + " to byte; at path " + jsonReader.getPreviousPath());
            }
            return (byte)n2;
        }

        @Override
        public void write(JsonWriter jsonWriter, Number number) throws IOException {
            if (number == null) {
                jsonWriter.nullValue();
                return;
            }
            jsonWriter.value(number.byteValue());
        }
    };
    public static final TypeAdapterFactory BYTE_FACTORY = TypeAdapters.newFactory(Byte.TYPE, Byte.class, BYTE);
    public static final TypeAdapter<Number> SHORT = new TypeAdapter<Number>(){

        @Override
        public Number read(JsonReader jsonReader) throws IOException {
            int n2;
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            try {
                n2 = jsonReader.nextInt();
            }
            catch (NumberFormatException numberFormatException) {
                throw new JsonSyntaxException(numberFormatException);
            }
            if (n2 > 65535 || n2 < Short.MIN_VALUE) {
                throw new JsonSyntaxException("Lossy conversion from " + n2 + " to short; at path " + jsonReader.getPreviousPath());
            }
            return (short)n2;
        }

        @Override
        public void write(JsonWriter jsonWriter, Number number) throws IOException {
            if (number == null) {
                jsonWriter.nullValue();
                return;
            }
            jsonWriter.value(number.shortValue());
        }
    };
    public static final TypeAdapterFactory SHORT_FACTORY = TypeAdapters.newFactory(Short.TYPE, Short.class, SHORT);
    public static final TypeAdapter<Number> INTEGER = new TypeAdapter<Number>(){

        @Override
        public Number read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            try {
                return jsonReader.nextInt();
            }
            catch (NumberFormatException numberFormatException) {
                throw new JsonSyntaxException(numberFormatException);
            }
        }

        @Override
        public void write(JsonWriter jsonWriter, Number number) throws IOException {
            if (number == null) {
                jsonWriter.nullValue();
                return;
            }
            jsonWriter.value(number.intValue());
        }
    };
    public static final TypeAdapterFactory INTEGER_FACTORY = TypeAdapters.newFactory(Integer.TYPE, Integer.class, INTEGER);
    public static final TypeAdapter<AtomicInteger> ATOMIC_INTEGER = new TypeAdapter<AtomicInteger>(){

        @Override
        public AtomicInteger read(JsonReader jsonReader) throws IOException {
            try {
                return new AtomicInteger(jsonReader.nextInt());
            }
            catch (NumberFormatException numberFormatException) {
                throw new JsonSyntaxException(numberFormatException);
            }
        }

        @Override
        public void write(JsonWriter jsonWriter, AtomicInteger atomicInteger) throws IOException {
            jsonWriter.value(atomicInteger.get());
        }
    }.nullSafe();
    public static final TypeAdapterFactory ATOMIC_INTEGER_FACTORY = TypeAdapters.newFactory(AtomicInteger.class, ATOMIC_INTEGER);
    public static final TypeAdapter<AtomicBoolean> ATOMIC_BOOLEAN = new TypeAdapter<AtomicBoolean>(){

        @Override
        public AtomicBoolean read(JsonReader jsonReader) throws IOException {
            return new AtomicBoolean(jsonReader.nextBoolean());
        }

        @Override
        public void write(JsonWriter jsonWriter, AtomicBoolean atomicBoolean) throws IOException {
            jsonWriter.value(atomicBoolean.get());
        }
    }.nullSafe();
    public static final TypeAdapterFactory ATOMIC_BOOLEAN_FACTORY = TypeAdapters.newFactory(AtomicBoolean.class, ATOMIC_BOOLEAN);
    public static final TypeAdapter<AtomicIntegerArray> ATOMIC_INTEGER_ARRAY = new TypeAdapter<AtomicIntegerArray>(){

        @Override
        public AtomicIntegerArray read(JsonReader object) throws IOException {
            int n2;
            ArrayList<Integer> arrayList = new ArrayList<Integer>();
            ((JsonReader)object).beginArray();
            while (((JsonReader)object).hasNext()) {
                try {
                    n2 = ((JsonReader)object).nextInt();
                    arrayList.add(n2);
                }
                catch (NumberFormatException numberFormatException) {
                    throw new JsonSyntaxException(numberFormatException);
                }
            }
            ((JsonReader)object).endArray();
            n2 = arrayList.size();
            object = new AtomicIntegerArray(n2);
            for (int i2 = 0; i2 < n2; ++i2) {
                ((AtomicIntegerArray)object).set(i2, (Integer)arrayList.get(i2));
            }
            return object;
        }

        @Override
        public void write(JsonWriter jsonWriter, AtomicIntegerArray atomicIntegerArray) throws IOException {
            jsonWriter.beginArray();
            int n2 = atomicIntegerArray.length();
            for (int i2 = 0; i2 < n2; ++i2) {
                jsonWriter.value(atomicIntegerArray.get(i2));
            }
            jsonWriter.endArray();
        }
    }.nullSafe();
    public static final TypeAdapterFactory ATOMIC_INTEGER_ARRAY_FACTORY = TypeAdapters.newFactory(AtomicIntegerArray.class, ATOMIC_INTEGER_ARRAY);
    public static final TypeAdapter<Number> LONG = new TypeAdapter<Number>(){

        @Override
        public Number read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            try {
                return jsonReader.nextLong();
            }
            catch (NumberFormatException numberFormatException) {
                throw new JsonSyntaxException(numberFormatException);
            }
        }

        @Override
        public void write(JsonWriter jsonWriter, Number number) throws IOException {
            if (number == null) {
                jsonWriter.nullValue();
                return;
            }
            jsonWriter.value(number.longValue());
        }
    };
    public static final TypeAdapter<Number> FLOAT = new TypeAdapter<Number>(){

        @Override
        public Number read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            return Float.valueOf((float)jsonReader.nextDouble());
        }

        @Override
        public void write(JsonWriter jsonWriter, Number number) throws IOException {
            if (number == null) {
                jsonWriter.nullValue();
                return;
            }
            number = number instanceof Float ? (Number)number : (Number)Float.valueOf(number.floatValue());
            jsonWriter.value(number);
        }
    };
    public static final TypeAdapter<Number> DOUBLE = new TypeAdapter<Number>(){

        @Override
        public Number read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            return jsonReader.nextDouble();
        }

        @Override
        public void write(JsonWriter jsonWriter, Number number) throws IOException {
            if (number == null) {
                jsonWriter.nullValue();
                return;
            }
            jsonWriter.value(number.doubleValue());
        }
    };
    public static final TypeAdapter<Character> CHARACTER = new TypeAdapter<Character>(){

        @Override
        public Character read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            String string = jsonReader.nextString();
            if (string.length() != 1) {
                throw new JsonSyntaxException("Expecting character, got: " + string + "; at " + jsonReader.getPreviousPath());
            }
            return Character.valueOf(string.charAt(0));
        }

        @Override
        public void write(JsonWriter jsonWriter, Character c2) throws IOException {
            jsonWriter.value(c2 == null ? null : String.valueOf(c2));
        }
    };
    public static final TypeAdapterFactory CHARACTER_FACTORY = TypeAdapters.newFactory(Character.TYPE, Character.class, CHARACTER);
    public static final TypeAdapter<String> STRING = new TypeAdapter<String>(){

        @Override
        public String read(JsonReader jsonReader) throws IOException {
            JsonToken jsonToken = jsonReader.peek();
            if (jsonToken == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            if (jsonToken == JsonToken.BOOLEAN) {
                return Boolean.toString(jsonReader.nextBoolean());
            }
            return jsonReader.nextString();
        }

        @Override
        public void write(JsonWriter jsonWriter, String string) throws IOException {
            jsonWriter.value(string);
        }
    };
    public static final TypeAdapter<BigDecimal> BIG_DECIMAL = new TypeAdapter<BigDecimal>(){

        @Override
        public BigDecimal read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            String string = jsonReader.nextString();
            try {
                return new BigDecimal(string);
            }
            catch (NumberFormatException numberFormatException) {
                throw new JsonSyntaxException("Failed parsing '" + string + "' as BigDecimal; at path " + jsonReader.getPreviousPath(), numberFormatException);
            }
        }

        @Override
        public void write(JsonWriter jsonWriter, BigDecimal bigDecimal) throws IOException {
            jsonWriter.value(bigDecimal);
        }
    };
    public static final TypeAdapter<BigInteger> BIG_INTEGER = new TypeAdapter<BigInteger>(){

        @Override
        public BigInteger read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            String string = jsonReader.nextString();
            try {
                return new BigInteger(string);
            }
            catch (NumberFormatException numberFormatException) {
                throw new JsonSyntaxException("Failed parsing '" + string + "' as BigInteger; at path " + jsonReader.getPreviousPath(), numberFormatException);
            }
        }

        @Override
        public void write(JsonWriter jsonWriter, BigInteger bigInteger) throws IOException {
            jsonWriter.value(bigInteger);
        }
    };
    public static final TypeAdapter<LazilyParsedNumber> LAZILY_PARSED_NUMBER = new TypeAdapter<LazilyParsedNumber>(){

        @Override
        public LazilyParsedNumber read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            return new LazilyParsedNumber(jsonReader.nextString());
        }

        @Override
        public void write(JsonWriter jsonWriter, LazilyParsedNumber lazilyParsedNumber) throws IOException {
            jsonWriter.value(lazilyParsedNumber);
        }
    };
    public static final TypeAdapterFactory STRING_FACTORY = TypeAdapters.newFactory(String.class, STRING);
    public static final TypeAdapter<StringBuilder> STRING_BUILDER = new TypeAdapter<StringBuilder>(){

        @Override
        public StringBuilder read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            return new StringBuilder(jsonReader.nextString());
        }

        @Override
        public void write(JsonWriter jsonWriter, StringBuilder stringBuilder) throws IOException {
            jsonWriter.value(stringBuilder == null ? null : stringBuilder.toString());
        }
    };
    public static final TypeAdapterFactory STRING_BUILDER_FACTORY = TypeAdapters.newFactory(StringBuilder.class, STRING_BUILDER);
    public static final TypeAdapter<StringBuffer> STRING_BUFFER = new TypeAdapter<StringBuffer>(){

        @Override
        public StringBuffer read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            return new StringBuffer(jsonReader.nextString());
        }

        @Override
        public void write(JsonWriter jsonWriter, StringBuffer stringBuffer) throws IOException {
            jsonWriter.value(stringBuffer == null ? null : stringBuffer.toString());
        }
    };
    public static final TypeAdapterFactory STRING_BUFFER_FACTORY = TypeAdapters.newFactory(StringBuffer.class, STRING_BUFFER);
    public static final TypeAdapter<URL> URL = new TypeAdapter<URL>(){

        @Override
        public URL read(JsonReader object) throws IOException {
            if (((JsonReader)object).peek() == JsonToken.NULL) {
                ((JsonReader)object).nextNull();
                return null;
            }
            if ("null".equals(object = ((JsonReader)object).nextString())) {
                return null;
            }
            return new URL((String)object);
        }

        @Override
        public void write(JsonWriter jsonWriter, URL uRL) throws IOException {
            jsonWriter.value(uRL == null ? null : uRL.toExternalForm());
        }
    };
    public static final TypeAdapterFactory URL_FACTORY = TypeAdapters.newFactory(URL.class, URL);
    public static final TypeAdapter<URI> URI = new TypeAdapter<URI>(){

        @Override
        public URI read(JsonReader object) throws IOException {
            if (((JsonReader)object).peek() == JsonToken.NULL) {
                ((JsonReader)object).nextNull();
                return null;
            }
            try {
                object = ((JsonReader)object).nextString();
                if ("null".equals(object)) {
                    return null;
                }
                return new URI((String)object);
            }
            catch (URISyntaxException uRISyntaxException) {
                throw new JsonIOException(uRISyntaxException);
            }
        }

        @Override
        public void write(JsonWriter jsonWriter, URI uRI) throws IOException {
            jsonWriter.value(uRI == null ? null : uRI.toASCIIString());
        }
    };
    public static final TypeAdapterFactory URI_FACTORY = TypeAdapters.newFactory(URI.class, URI);
    public static final TypeAdapter<InetAddress> INET_ADDRESS = new TypeAdapter<InetAddress>(){

        @Override
        public InetAddress read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            return InetAddress.getByName(jsonReader.nextString());
        }

        @Override
        public void write(JsonWriter jsonWriter, InetAddress inetAddress) throws IOException {
            jsonWriter.value(inetAddress == null ? null : inetAddress.getHostAddress());
        }
    };
    public static final TypeAdapterFactory INET_ADDRESS_FACTORY = TypeAdapters.newTypeHierarchyFactory(InetAddress.class, INET_ADDRESS);
    public static final TypeAdapter<UUID> UUID = new TypeAdapter<UUID>(){

        @Override
        public UUID read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            String string = jsonReader.nextString();
            try {
                return java.util.UUID.fromString(string);
            }
            catch (IllegalArgumentException illegalArgumentException) {
                throw new JsonSyntaxException("Failed parsing '" + string + "' as UUID; at path " + jsonReader.getPreviousPath(), illegalArgumentException);
            }
        }

        @Override
        public void write(JsonWriter jsonWriter, UUID uUID) throws IOException {
            jsonWriter.value(uUID == null ? null : uUID.toString());
        }
    };
    public static final TypeAdapterFactory UUID_FACTORY = TypeAdapters.newFactory(UUID.class, UUID);
    public static final TypeAdapter<Currency> CURRENCY = new TypeAdapter<Currency>(){

        @Override
        public Currency read(JsonReader jsonReader) throws IOException {
            String string = jsonReader.nextString();
            try {
                return Currency.getInstance(string);
            }
            catch (IllegalArgumentException illegalArgumentException) {
                throw new JsonSyntaxException("Failed parsing '" + string + "' as Currency; at path " + jsonReader.getPreviousPath(), illegalArgumentException);
            }
        }

        @Override
        public void write(JsonWriter jsonWriter, Currency currency) throws IOException {
            jsonWriter.value(currency.getCurrencyCode());
        }
    }.nullSafe();
    public static final TypeAdapterFactory CURRENCY_FACTORY = TypeAdapters.newFactory(Currency.class, CURRENCY);
    public static final TypeAdapter<Calendar> CALENDAR = new TypeAdapter<Calendar>(){
        private static final String YEAR = "year";
        private static final String MONTH = "month";
        private static final String DAY_OF_MONTH = "dayOfMonth";
        private static final String HOUR_OF_DAY = "hourOfDay";
        private static final String MINUTE = "minute";
        private static final String SECOND = "second";

        @Override
        public Calendar read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            jsonReader.beginObject();
            int n2 = 0;
            int n3 = 0;
            int n4 = 0;
            int n5 = 0;
            int n6 = 0;
            int n7 = 0;
            while (jsonReader.peek() != JsonToken.END_OBJECT) {
                String string = jsonReader.nextName();
                int n8 = jsonReader.nextInt();
                if (YEAR.equals(string)) {
                    n2 = n8;
                    continue;
                }
                if (MONTH.equals(string)) {
                    n3 = n8;
                    continue;
                }
                if (DAY_OF_MONTH.equals(string)) {
                    n4 = n8;
                    continue;
                }
                if (HOUR_OF_DAY.equals(string)) {
                    n5 = n8;
                    continue;
                }
                if (MINUTE.equals(string)) {
                    n6 = n8;
                    continue;
                }
                if (!SECOND.equals(string)) continue;
                n7 = n8;
            }
            jsonReader.endObject();
            return new GregorianCalendar(n2, n3, n4, n5, n6, n7);
        }

        @Override
        public void write(JsonWriter jsonWriter, Calendar calendar) throws IOException {
            if (calendar == null) {
                jsonWriter.nullValue();
                return;
            }
            jsonWriter.beginObject();
            jsonWriter.name(YEAR);
            jsonWriter.value(calendar.get(1));
            jsonWriter.name(MONTH);
            jsonWriter.value(calendar.get(2));
            jsonWriter.name(DAY_OF_MONTH);
            jsonWriter.value(calendar.get(5));
            jsonWriter.name(HOUR_OF_DAY);
            jsonWriter.value(calendar.get(11));
            jsonWriter.name(MINUTE);
            jsonWriter.value(calendar.get(12));
            jsonWriter.name(SECOND);
            jsonWriter.value(calendar.get(13));
            jsonWriter.endObject();
        }
    };
    public static final TypeAdapterFactory CALENDAR_FACTORY = TypeAdapters.newFactoryForMultipleTypes(Calendar.class, GregorianCalendar.class, CALENDAR);
    public static final TypeAdapter<Locale> LOCALE = new TypeAdapter<Locale>(){

        @Override
        public Locale read(JsonReader object) throws IOException {
            if (((JsonReader)object).peek() == JsonToken.NULL) {
                ((JsonReader)object).nextNull();
                return null;
            }
            object = ((JsonReader)object).nextString();
            object = new StringTokenizer((String)object, "_");
            String string = null;
            String string2 = null;
            String string3 = null;
            if (((StringTokenizer)object).hasMoreElements()) {
                string = ((StringTokenizer)object).nextToken();
            }
            if (((StringTokenizer)object).hasMoreElements()) {
                string2 = ((StringTokenizer)object).nextToken();
            }
            if (((StringTokenizer)object).hasMoreElements()) {
                string3 = ((StringTokenizer)object).nextToken();
            }
            if (string2 == null && string3 == null) {
                return new Locale(string);
            }
            if (string3 == null) {
                return new Locale(string, string2);
            }
            return new Locale(string, string2, string3);
        }

        @Override
        public void write(JsonWriter jsonWriter, Locale locale) throws IOException {
            jsonWriter.value(locale == null ? null : locale.toString());
        }
    };
    public static final TypeAdapterFactory LOCALE_FACTORY = TypeAdapters.newFactory(Locale.class, LOCALE);
    public static final TypeAdapter<JsonElement> JSON_ELEMENT = new TypeAdapter<JsonElement>(){

        private JsonElement tryBeginNesting(JsonReader jsonReader, JsonToken jsonToken) throws IOException {
            switch (jsonToken) {
                case BEGIN_ARRAY: {
                    jsonReader.beginArray();
                    return new JsonArray();
                }
                case BEGIN_OBJECT: {
                    jsonReader.beginObject();
                    return new JsonObject();
                }
            }
            return null;
        }

        private JsonElement readTerminal(JsonReader object, JsonToken jsonToken) throws IOException {
            switch (jsonToken) {
                case STRING: {
                    return new JsonPrimitive(((JsonReader)object).nextString());
                }
                case NUMBER: {
                    object = ((JsonReader)object).nextString();
                    return new JsonPrimitive(new LazilyParsedNumber((String)object));
                }
                case BOOLEAN: {
                    return new JsonPrimitive(((JsonReader)object).nextBoolean());
                }
                case NULL: {
                    ((JsonReader)object).nextNull();
                    return JsonNull.INSTANCE;
                }
            }
            throw new IllegalStateException("Unexpected token: " + (Object)((Object)jsonToken));
        }

        @Override
        public JsonElement read(JsonReader jsonReader) throws IOException {
            if (jsonReader instanceof JsonTreeReader) {
                return ((JsonTreeReader)jsonReader).nextJsonElement();
            }
            JsonToken jsonToken = jsonReader.peek();
            JsonElement jsonElement = this.tryBeginNesting(jsonReader, jsonToken);
            if (jsonElement == null) {
                return this.readTerminal(jsonReader, jsonToken);
            }
            ArrayDeque<JsonElement> arrayDeque = new ArrayDeque<JsonElement>();
            while (true) {
                if (jsonReader.hasNext()) {
                    JsonElement jsonElement2;
                    boolean bl;
                    String string = null;
                    if (jsonElement instanceof JsonObject) {
                        string = jsonReader.nextName();
                    }
                    boolean bl2 = bl = (jsonElement2 = this.tryBeginNesting(jsonReader, jsonToken = jsonReader.peek())) != null;
                    if (jsonElement2 == null) {
                        jsonElement2 = this.readTerminal(jsonReader, jsonToken);
                    }
                    if (jsonElement instanceof JsonArray) {
                        ((JsonArray)jsonElement).add(jsonElement2);
                    } else {
                        ((JsonObject)jsonElement).add(string, jsonElement2);
                    }
                    if (!bl) continue;
                    arrayDeque.addLast(jsonElement);
                    jsonElement = jsonElement2;
                    continue;
                }
                if (jsonElement instanceof JsonArray) {
                    jsonReader.endArray();
                } else {
                    jsonReader.endObject();
                }
                if (arrayDeque.isEmpty()) {
                    return jsonElement;
                }
                jsonElement = (JsonElement)arrayDeque.removeLast();
            }
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void write(JsonWriter jsonWriter, JsonElement iterator2) throws IOException {
            if (iterator2 == null || ((JsonElement)((Object)iterator2)).isJsonNull()) {
                jsonWriter.nullValue();
                return;
            }
            if (((JsonElement)((Object)iterator2)).isJsonPrimitive()) {
                if (((JsonPrimitive)((Object)(iterator2 = ((JsonElement)((Object)iterator2)).getAsJsonPrimitive()))).isNumber()) {
                    jsonWriter.value(((JsonElement)((Object)iterator2)).getAsNumber());
                    return;
                }
                if (((JsonPrimitive)((Object)iterator2)).isBoolean()) {
                    jsonWriter.value(((JsonElement)((Object)iterator2)).getAsBoolean());
                    return;
                }
                jsonWriter.value(((JsonElement)((Object)iterator2)).getAsString());
                return;
            }
            if (((JsonElement)((Object)iterator2)).isJsonArray()) {
                jsonWriter.beginArray();
                iterator2 = ((JsonElement)((Object)iterator2)).getAsJsonArray().iterator();
                while (true) {
                    if (!iterator2.hasNext()) {
                        jsonWriter.endArray();
                        return;
                    }
                    JsonElement jsonElement = iterator2.next();
                    this.write(jsonWriter, jsonElement);
                }
            }
            if (!((JsonElement)((Object)iterator2)).isJsonObject()) throw new IllegalArgumentException("Couldn't write " + iterator2.getClass());
            jsonWriter.beginObject();
            iterator2 = ((JsonElement)((Object)iterator2)).getAsJsonObject().entrySet().iterator();
            while (true) {
                if (!iterator2.hasNext()) {
                    jsonWriter.endObject();
                    return;
                }
                Map.Entry entry = (Map.Entry)iterator2.next();
                jsonWriter.name((String)entry.getKey());
                this.write(jsonWriter, (JsonElement)entry.getValue());
            }
        }
    };
    public static final TypeAdapterFactory JSON_ELEMENT_FACTORY = TypeAdapters.newTypeHierarchyFactory(JsonElement.class, JSON_ELEMENT);
    public static final TypeAdapterFactory ENUM_FACTORY = new TypeAdapterFactory(){

        @Override
        public <T> TypeAdapter<T> create(Gson clazz, TypeToken<T> typeToken) {
            clazz = typeToken.getRawType();
            if (!Enum.class.isAssignableFrom(clazz) || clazz == Enum.class) {
                return null;
            }
            if (!clazz.isEnum()) {
                clazz = clazz.getSuperclass();
            }
            clazz = new EnumTypeAdapter(clazz);
            return clazz;
        }
    };

    private TypeAdapters() {
        throw new UnsupportedOperationException();
    }

    public static <TT> TypeAdapterFactory newFactory(final TypeToken<TT> typeToken, final TypeAdapter<TT> typeAdapter) {
        return new TypeAdapterFactory(){

            @Override
            public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken2) {
                if (typeToken2.equals(typeToken)) {
                    return typeAdapter;
                }
                return null;
            }
        };
    }

    public static <TT> TypeAdapterFactory newFactory(final Class<TT> clazz, final TypeAdapter<TT> typeAdapter) {
        return new TypeAdapterFactory(){

            @Override
            public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
                if (typeToken.getRawType() == clazz) {
                    return typeAdapter;
                }
                return null;
            }

            public String toString() {
                return "Factory[type=" + clazz.getName() + ",adapter=" + typeAdapter + "]";
            }
        };
    }

    public static <TT> TypeAdapterFactory newFactory(final Class<TT> clazz, final Class<TT> clazz2, final TypeAdapter<? super TT> typeAdapter) {
        return new TypeAdapterFactory(){

            @Override
            public <T> TypeAdapter<T> create(Gson clazz3, TypeToken<T> typeToken) {
                clazz3 = typeToken.getRawType();
                if (clazz3 == clazz || clazz3 == clazz2) {
                    return typeAdapter;
                }
                return null;
            }

            public String toString() {
                return "Factory[type=" + clazz2.getName() + "+" + clazz.getName() + ",adapter=" + typeAdapter + "]";
            }
        };
    }

    public static <TT> TypeAdapterFactory newFactoryForMultipleTypes(final Class<TT> clazz, final Class<? extends TT> clazz2, final TypeAdapter<? super TT> typeAdapter) {
        return new TypeAdapterFactory(){

            @Override
            public <T> TypeAdapter<T> create(Gson clazz3, TypeToken<T> typeToken) {
                clazz3 = typeToken.getRawType();
                if (clazz3 == clazz || clazz3 == clazz2) {
                    return typeAdapter;
                }
                return null;
            }

            public String toString() {
                return "Factory[type=" + clazz.getName() + "+" + clazz2.getName() + ",adapter=" + typeAdapter + "]";
            }
        };
    }

    public static <T1> TypeAdapterFactory newTypeHierarchyFactory(final Class<T1> clazz, final TypeAdapter<T1> typeAdapter) {
        return new TypeAdapterFactory(){

            public <T2> TypeAdapter<T2> create(final Gson clazz2, TypeToken<T2> typeToken) {
                clazz2 = typeToken.getRawType();
                if (!clazz.isAssignableFrom(clazz2)) {
                    return null;
                }
                return new TypeAdapter<T1>(){

                    @Override
                    public void write(JsonWriter jsonWriter, T1 T1) throws IOException {
                        typeAdapter.write(jsonWriter, T1);
                    }

                    @Override
                    public T1 read(JsonReader jsonReader) throws IOException {
                        Object t2 = typeAdapter.read(jsonReader);
                        if (t2 != null && !clazz2.isInstance(t2)) {
                            throw new JsonSyntaxException("Expected a " + clazz2.getName() + " but was " + t2.getClass().getName() + "; at path " + jsonReader.getPreviousPath());
                        }
                        return t2;
                    }
                };
            }

            public String toString() {
                return "Factory[typeHierarchy=" + clazz.getName() + ",adapter=" + typeAdapter + "]";
            }
        };
    }

    static final class EnumTypeAdapter<T extends Enum<T>>
    extends TypeAdapter<T> {
        private final Map<String, T> nameToConstant = new HashMap<String, T>();
        private final Map<String, T> stringToConstant = new HashMap<String, T>();
        private final Map<T, String> constantToName = new HashMap<T, String>();

        public EnumTypeAdapter(final Class<T> fieldArray) {
            try {
                Field[] fieldArray2 = AccessController.doPrivileged(new PrivilegedAction<Field[]>(){

                    @Override
                    public Field[] run() {
                        Field[] fieldArray2 = fieldArray.getDeclaredFields();
                        ArrayList<Field> arrayList = new ArrayList<Field>(fieldArray2.length);
                        for (Field field : fieldArray2) {
                            if (!field.isEnumConstant()) continue;
                            arrayList.add(field);
                        }
                        fieldArray2 = arrayList.toArray(new Field[0]);
                        AccessibleObject.setAccessible(fieldArray2, true);
                        return fieldArray2;
                    }
                });
                fieldArray = fieldArray2;
                fieldArray = fieldArray2;
                int n2 = fieldArray2.length;
                for (int i2 = 0; i2 < n2; ++i2) {
                    String[] stringArray = fieldArray[i2];
                    Enum enum_ = (Enum)stringArray.get(null);
                    String string = enum_.name();
                    String string2 = enum_.toString();
                    if ((stringArray = stringArray.getAnnotation(SerializedName.class)) != null) {
                        string = stringArray.value();
                        for (String string3 : stringArray.alternate()) {
                            this.nameToConstant.put(string3, enum_);
                        }
                    }
                    this.nameToConstant.put(string, enum_);
                    this.stringToConstant.put(string2, enum_);
                    this.constantToName.put(enum_, string);
                }
                return;
            }
            catch (IllegalAccessException illegalAccessException) {
                throw new AssertionError((Object)illegalAccessException);
            }
        }

        @Override
        public final T read(JsonReader object) throws IOException {
            if (((JsonReader)object).peek() == JsonToken.NULL) {
                ((JsonReader)object).nextNull();
                return null;
            }
            Enum enum_ = (Enum)this.nameToConstant.get(object = ((JsonReader)object).nextString());
            if (enum_ == null) {
                return (T)((Enum)this.stringToConstant.get(object));
            }
            return (T)enum_;
        }

        @Override
        public final void write(JsonWriter jsonWriter, T t2) throws IOException {
            jsonWriter.value(t2 == null ? null : this.constantToName.get(t2));
        }
    }
}


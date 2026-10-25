/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public final class JsonParser {
    @Deprecated
    public JsonParser() {
    }

    public static JsonElement parseString(String string) throws JsonSyntaxException {
        return JsonParser.parseReader(new StringReader(string));
    }

    public static JsonElement parseReader(Reader closeable) throws JsonIOException, JsonSyntaxException {
        try {
            closeable = new JsonReader((Reader)closeable);
            JsonElement jsonElement = JsonParser.parseReader((JsonReader)closeable);
            if (!jsonElement.isJsonNull() && ((JsonReader)closeable).peek() != JsonToken.END_DOCUMENT) {
                throw new JsonSyntaxException("Did not consume the entire document.");
            }
            return jsonElement;
        }
        catch (MalformedJsonException malformedJsonException) {
            throw new JsonSyntaxException(malformedJsonException);
        }
        catch (IOException iOException) {
            throw new JsonIOException(iOException);
        }
        catch (NumberFormatException numberFormatException) {
            throw new JsonSyntaxException(numberFormatException);
        }
    }

    public static JsonElement parseReader(JsonReader jsonReader) throws JsonIOException, JsonSyntaxException {
        boolean bl = jsonReader.isLenient();
        jsonReader.setLenient(true);
        try {
            JsonElement jsonElement = Streams.parse(jsonReader);
            return jsonElement;
        }
        catch (StackOverflowError stackOverflowError) {
            throw new JsonParseException("Failed parsing JSON source: " + jsonReader + " to Json", stackOverflowError);
        }
        catch (OutOfMemoryError outOfMemoryError) {
            throw new JsonParseException("Failed parsing JSON source: " + jsonReader + " to Json", outOfMemoryError);
        }
        finally {
            jsonReader.setLenient(bl);
        }
    }

    @Deprecated
    public final JsonElement parse(String string) throws JsonSyntaxException {
        return JsonParser.parseString(string);
    }

    @Deprecated
    public final JsonElement parse(Reader reader) throws JsonIOException, JsonSyntaxException {
        return JsonParser.parseReader(reader);
    }

    @Deprecated
    public final JsonElement parse(JsonReader jsonReader) throws JsonIOException, JsonSyntaxException {
        return JsonParser.parseReader(jsonReader);
    }
}


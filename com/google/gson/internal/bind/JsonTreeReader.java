/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

public final class JsonTreeReader
extends JsonReader {
    private static final Reader UNREADABLE_READER = new Reader(){

        @Override
        public int read(char[] cArray, int n2, int n3) {
            throw new AssertionError();
        }

        @Override
        public void close() {
            throw new AssertionError();
        }
    };
    private static final Object SENTINEL_CLOSED = new Object();
    private Object[] stack = new Object[32];
    private int stackSize = 0;
    private String[] pathNames = new String[32];
    private int[] pathIndices = new int[32];

    public JsonTreeReader(JsonElement jsonElement) {
        super(UNREADABLE_READER);
        this.push(jsonElement);
    }

    @Override
    public final void beginArray() throws IOException {
        this.expect(JsonToken.BEGIN_ARRAY);
        JsonArray jsonArray = (JsonArray)this.peekStack();
        this.push(jsonArray.iterator());
        this.pathIndices[this.stackSize - 1] = 0;
    }

    @Override
    public final void endArray() throws IOException {
        this.expect(JsonToken.END_ARRAY);
        this.popStack();
        this.popStack();
        if (this.stackSize > 0) {
            int n2 = this.stackSize - 1;
            this.pathIndices[n2] = this.pathIndices[n2] + 1;
        }
    }

    @Override
    public final void beginObject() throws IOException {
        this.expect(JsonToken.BEGIN_OBJECT);
        JsonObject jsonObject = (JsonObject)this.peekStack();
        this.push(jsonObject.entrySet().iterator());
    }

    @Override
    public final void endObject() throws IOException {
        this.expect(JsonToken.END_OBJECT);
        this.pathNames[this.stackSize - 1] = null;
        this.popStack();
        this.popStack();
        if (this.stackSize > 0) {
            int n2 = this.stackSize - 1;
            this.pathIndices[n2] = this.pathIndices[n2] + 1;
        }
    }

    @Override
    public final boolean hasNext() throws IOException {
        JsonToken jsonToken = ((JsonReader)this).peek();
        return jsonToken != JsonToken.END_OBJECT && jsonToken != JsonToken.END_ARRAY && jsonToken != JsonToken.END_DOCUMENT;
    }

    @Override
    public final JsonToken peek() throws IOException {
        Object object;
        block12: {
            boolean bl;
            while (true) {
                if (this.stackSize == 0) {
                    return JsonToken.END_DOCUMENT;
                }
                object = this.peekStack();
                if (!(object instanceof Iterator)) break block12;
                bl = this.stack[this.stackSize - 2] instanceof JsonObject;
                if (!(object = (Iterator)object).hasNext()) break;
                if (bl) {
                    return JsonToken.NAME;
                }
                this.push(object.next());
            }
            if (bl) {
                return JsonToken.END_OBJECT;
            }
            return JsonToken.END_ARRAY;
        }
        if (object instanceof JsonObject) {
            return JsonToken.BEGIN_OBJECT;
        }
        if (object instanceof JsonArray) {
            return JsonToken.BEGIN_ARRAY;
        }
        if (object instanceof JsonPrimitive) {
            JsonPrimitive jsonPrimitive = (JsonPrimitive)object;
            if (jsonPrimitive.isString()) {
                return JsonToken.STRING;
            }
            if (jsonPrimitive.isBoolean()) {
                return JsonToken.BOOLEAN;
            }
            if (jsonPrimitive.isNumber()) {
                return JsonToken.NUMBER;
            }
            throw new AssertionError();
        }
        if (object instanceof JsonNull) {
            return JsonToken.NULL;
        }
        if (object == SENTINEL_CLOSED) {
            throw new IllegalStateException("JsonReader is closed");
        }
        throw new MalformedJsonException("Custom JsonElement subclass " + object.getClass().getName() + " is not supported");
    }

    private Object peekStack() {
        return this.stack[this.stackSize - 1];
    }

    private Object popStack() {
        Object object = this.stack[--this.stackSize];
        this.stack[this.stackSize] = null;
        return object;
    }

    private void expect(JsonToken jsonToken) throws IOException {
        if (((JsonReader)this).peek() != jsonToken) {
            throw new IllegalStateException("Expected " + (Object)((Object)jsonToken) + " but was " + (Object)((Object)((JsonReader)this).peek()) + this.locationString());
        }
    }

    private String nextName(boolean bl) throws IOException {
        this.expect(JsonToken.NAME);
        Object object = (Iterator)this.peekStack();
        object = (Map.Entry)object.next();
        String string = (String)object.getKey();
        this.pathNames[this.stackSize - 1] = bl ? "<skipped>" : string;
        this.push(object.getValue());
        return string;
    }

    @Override
    public final String nextName() throws IOException {
        return this.nextName(false);
    }

    @Override
    public final String nextString() throws IOException {
        Object object = ((JsonReader)this).peek();
        if (object != JsonToken.STRING && object != JsonToken.NUMBER) {
            throw new IllegalStateException("Expected " + (Object)((Object)JsonToken.STRING) + " but was " + (Object)object + this.locationString());
        }
        object = ((JsonElement)((JsonPrimitive)this.popStack())).getAsString();
        if (this.stackSize > 0) {
            int n2 = this.stackSize - 1;
            this.pathIndices[n2] = this.pathIndices[n2] + 1;
        }
        return object;
    }

    @Override
    public final boolean nextBoolean() throws IOException {
        this.expect(JsonToken.BOOLEAN);
        boolean bl = ((JsonElement)((JsonPrimitive)this.popStack())).getAsBoolean();
        if (this.stackSize > 0) {
            int n2 = this.stackSize - 1;
            this.pathIndices[n2] = this.pathIndices[n2] + 1;
        }
        return bl;
    }

    @Override
    public final void nextNull() throws IOException {
        this.expect(JsonToken.NULL);
        this.popStack();
        if (this.stackSize > 0) {
            int n2 = this.stackSize - 1;
            this.pathIndices[n2] = this.pathIndices[n2] + 1;
        }
    }

    @Override
    public final double nextDouble() throws IOException {
        JsonToken jsonToken = ((JsonReader)this).peek();
        if (jsonToken != JsonToken.NUMBER && jsonToken != JsonToken.STRING) {
            throw new IllegalStateException("Expected " + (Object)((Object)JsonToken.NUMBER) + " but was " + (Object)((Object)jsonToken) + this.locationString());
        }
        double d2 = ((JsonElement)((JsonPrimitive)this.peekStack())).getAsDouble();
        if (!this.isLenient() && (Double.isNaN(d2) || Double.isInfinite(d2))) {
            throw new MalformedJsonException("JSON forbids NaN and infinities: " + d2);
        }
        this.popStack();
        if (this.stackSize > 0) {
            int n2 = this.stackSize - 1;
            this.pathIndices[n2] = this.pathIndices[n2] + 1;
        }
        return d2;
    }

    @Override
    public final long nextLong() throws IOException {
        JsonToken jsonToken = ((JsonReader)this).peek();
        if (jsonToken != JsonToken.NUMBER && jsonToken != JsonToken.STRING) {
            throw new IllegalStateException("Expected " + (Object)((Object)JsonToken.NUMBER) + " but was " + (Object)((Object)jsonToken) + this.locationString());
        }
        long l2 = ((JsonElement)((JsonPrimitive)this.peekStack())).getAsLong();
        this.popStack();
        if (this.stackSize > 0) {
            int n2 = this.stackSize - 1;
            this.pathIndices[n2] = this.pathIndices[n2] + 1;
        }
        return l2;
    }

    @Override
    public final int nextInt() throws IOException {
        JsonToken jsonToken = ((JsonReader)this).peek();
        if (jsonToken != JsonToken.NUMBER && jsonToken != JsonToken.STRING) {
            throw new IllegalStateException("Expected " + (Object)((Object)JsonToken.NUMBER) + " but was " + (Object)((Object)jsonToken) + this.locationString());
        }
        int n2 = ((JsonElement)((JsonPrimitive)this.peekStack())).getAsInt();
        this.popStack();
        if (this.stackSize > 0) {
            int n3 = this.stackSize - 1;
            this.pathIndices[n3] = this.pathIndices[n3] + 1;
        }
        return n2;
    }

    final JsonElement nextJsonElement() throws IOException {
        Object object = ((JsonReader)this).peek();
        if (object == JsonToken.NAME || object == JsonToken.END_ARRAY || object == JsonToken.END_OBJECT || object == JsonToken.END_DOCUMENT) {
            throw new IllegalStateException("Unexpected " + (Object)object + " when reading a JsonElement.");
        }
        object = (JsonElement)this.peekStack();
        ((JsonReader)this).skipValue();
        return object;
    }

    @Override
    public final void close() throws IOException {
        this.stack = new Object[]{SENTINEL_CLOSED};
        this.stackSize = 1;
    }

    @Override
    public final void skipValue() throws IOException {
        JsonToken jsonToken = ((JsonReader)this).peek();
        switch (jsonToken) {
            case NAME: {
                this.nextName(true);
                return;
            }
            case END_ARRAY: {
                ((JsonReader)this).endArray();
                return;
            }
            case END_OBJECT: {
                ((JsonReader)this).endObject();
                return;
            }
            case END_DOCUMENT: {
                return;
            }
        }
        this.popStack();
        if (this.stackSize > 0) {
            int n2 = this.stackSize - 1;
            this.pathIndices[n2] = this.pathIndices[n2] + 1;
        }
    }

    @Override
    public final String toString() {
        return this.getClass().getSimpleName() + this.locationString();
    }

    public final void promoteNameToValue() throws IOException {
        this.expect(JsonToken.NAME);
        Object object = (Iterator)this.peekStack();
        object = (Map.Entry)object.next();
        this.push(object.getValue());
        this.push(new JsonPrimitive((String)object.getKey()));
    }

    private void push(Object object) {
        if (this.stackSize == this.stack.length) {
            int n2 = this.stackSize << 1;
            this.stack = Arrays.copyOf(this.stack, n2);
            this.pathIndices = Arrays.copyOf(this.pathIndices, n2);
            this.pathNames = Arrays.copyOf(this.pathNames, n2);
        }
        this.stack[this.stackSize++] = object;
    }

    private String getPath(boolean bl) {
        StringBuilder stringBuilder = new StringBuilder("$");
        for (int i2 = 0; i2 < this.stackSize; ++i2) {
            if (this.stack[i2] instanceof JsonArray) {
                if (++i2 >= this.stackSize || !(this.stack[i2] instanceof Iterator)) continue;
                int n2 = this.pathIndices[i2];
                if (bl && n2 > 0 && (i2 == this.stackSize - 1 || i2 == this.stackSize - 2)) {
                    --n2;
                }
                stringBuilder.append('[').append(n2).append(']');
                continue;
            }
            if (!(this.stack[i2] instanceof JsonObject) || ++i2 >= this.stackSize || !(this.stack[i2] instanceof Iterator)) continue;
            stringBuilder.append('.');
            if (this.pathNames[i2] == null) continue;
            stringBuilder.append(this.pathNames[i2]);
        }
        return stringBuilder.toString();
    }

    @Override
    public final String getPreviousPath() {
        return this.getPath(true);
    }

    @Override
    public final String getPath() {
        return this.getPath(false);
    }

    private String locationString() {
        return " at path " + ((JsonReader)this).getPath();
    }
}


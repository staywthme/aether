/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class JsonArray
extends JsonElement
implements Iterable<JsonElement> {
    private final ArrayList<JsonElement> elements;

    public JsonArray() {
        this.elements = new ArrayList();
    }

    public JsonArray(int n2) {
        this.elements = new ArrayList(n2);
    }

    @Override
    public final JsonArray deepCopy() {
        if (!this.elements.isEmpty()) {
            JsonArray jsonArray = new JsonArray(this.elements.size());
            for (JsonElement jsonElement : this.elements) {
                jsonArray.add(jsonElement.deepCopy());
            }
            return jsonArray;
        }
        return new JsonArray();
    }

    public final void add(Boolean bl) {
        this.elements.add(bl == null ? JsonNull.INSTANCE : new JsonPrimitive(bl));
    }

    public final void add(Character c2) {
        this.elements.add(c2 == null ? JsonNull.INSTANCE : new JsonPrimitive(c2));
    }

    public final void add(Number number) {
        this.elements.add(number == null ? JsonNull.INSTANCE : new JsonPrimitive(number));
    }

    public final void add(String string) {
        this.elements.add(string == null ? JsonNull.INSTANCE : new JsonPrimitive(string));
    }

    public final void add(JsonElement jsonElement) {
        if (jsonElement == null) {
            jsonElement = JsonNull.INSTANCE;
        }
        this.elements.add(jsonElement);
    }

    public final void addAll(JsonArray jsonArray) {
        this.elements.addAll(jsonArray.elements);
    }

    public final JsonElement set(int n2, JsonElement jsonElement) {
        return this.elements.set(n2, jsonElement == null ? JsonNull.INSTANCE : jsonElement);
    }

    public final boolean remove(JsonElement jsonElement) {
        return this.elements.remove(jsonElement);
    }

    public final JsonElement remove(int n2) {
        return this.elements.remove(n2);
    }

    public final boolean contains(JsonElement jsonElement) {
        return this.elements.contains(jsonElement);
    }

    public final int size() {
        return this.elements.size();
    }

    public final boolean isEmpty() {
        return this.elements.isEmpty();
    }

    @Override
    public final Iterator<JsonElement> iterator() {
        return this.elements.iterator();
    }

    public final JsonElement get(int n2) {
        return this.elements.get(n2);
    }

    private JsonElement getAsSingleElement() {
        int n2 = this.elements.size();
        if (n2 == 1) {
            return this.elements.get(0);
        }
        throw new IllegalStateException("Array must have size 1, but has size " + n2);
    }

    @Override
    public final Number getAsNumber() {
        return this.getAsSingleElement().getAsNumber();
    }

    @Override
    public final String getAsString() {
        return this.getAsSingleElement().getAsString();
    }

    @Override
    public final double getAsDouble() {
        return this.getAsSingleElement().getAsDouble();
    }

    @Override
    public final BigDecimal getAsBigDecimal() {
        return this.getAsSingleElement().getAsBigDecimal();
    }

    @Override
    public final BigInteger getAsBigInteger() {
        return this.getAsSingleElement().getAsBigInteger();
    }

    @Override
    public final float getAsFloat() {
        return this.getAsSingleElement().getAsFloat();
    }

    @Override
    public final long getAsLong() {
        return this.getAsSingleElement().getAsLong();
    }

    @Override
    public final int getAsInt() {
        return this.getAsSingleElement().getAsInt();
    }

    @Override
    public final byte getAsByte() {
        return this.getAsSingleElement().getAsByte();
    }

    @Override
    @Deprecated
    public final char getAsCharacter() {
        return this.getAsSingleElement().getAsCharacter();
    }

    @Override
    public final short getAsShort() {
        return this.getAsSingleElement().getAsShort();
    }

    @Override
    public final boolean getAsBoolean() {
        return this.getAsSingleElement().getAsBoolean();
    }

    public final List<JsonElement> asList() {
        return new NonNullElementWrapperList<JsonElement>(this.elements);
    }

    public final boolean equals(Object object) {
        return object == this || object instanceof JsonArray && ((JsonArray)object).elements.equals(this.elements);
    }

    public final int hashCode() {
        return this.elements.hashCode();
    }
}


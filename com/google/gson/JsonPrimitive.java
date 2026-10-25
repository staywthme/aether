/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson;

import com.google.gson.JsonElement;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

public final class JsonPrimitive
extends JsonElement {
    private final Object value;

    public JsonPrimitive(Boolean bl) {
        this.value = Objects.requireNonNull(bl);
    }

    public JsonPrimitive(Number number) {
        this.value = Objects.requireNonNull(number);
    }

    public JsonPrimitive(String string) {
        this.value = Objects.requireNonNull(string);
    }

    public JsonPrimitive(Character c2) {
        this.value = Objects.requireNonNull(c2).toString();
    }

    @Override
    public final JsonPrimitive deepCopy() {
        return this;
    }

    public final boolean isBoolean() {
        return this.value instanceof Boolean;
    }

    @Override
    public final boolean getAsBoolean() {
        if (this.isBoolean()) {
            return (Boolean)this.value;
        }
        return Boolean.parseBoolean(((JsonElement)this).getAsString());
    }

    public final boolean isNumber() {
        return this.value instanceof Number;
    }

    @Override
    public final Number getAsNumber() {
        if (this.value instanceof Number) {
            return (Number)this.value;
        }
        if (this.value instanceof String) {
            return new LazilyParsedNumber((String)this.value);
        }
        throw new UnsupportedOperationException("Primitive is neither a number nor a string");
    }

    public final boolean isString() {
        return this.value instanceof String;
    }

    @Override
    public final String getAsString() {
        if (this.value instanceof String) {
            return (String)this.value;
        }
        if (this.isNumber()) {
            return ((JsonElement)this).getAsNumber().toString();
        }
        if (this.isBoolean()) {
            return ((Boolean)this.value).toString();
        }
        throw new AssertionError((Object)("Unexpected value type: " + this.value.getClass()));
    }

    @Override
    public final double getAsDouble() {
        if (this.isNumber()) {
            return ((JsonElement)this).getAsNumber().doubleValue();
        }
        return Double.parseDouble(((JsonElement)this).getAsString());
    }

    @Override
    public final BigDecimal getAsBigDecimal() {
        if (this.value instanceof BigDecimal) {
            return (BigDecimal)this.value;
        }
        return new BigDecimal(((JsonElement)this).getAsString());
    }

    @Override
    public final BigInteger getAsBigInteger() {
        if (this.value instanceof BigInteger) {
            return (BigInteger)this.value;
        }
        return new BigInteger(((JsonElement)this).getAsString());
    }

    @Override
    public final float getAsFloat() {
        if (this.isNumber()) {
            return ((JsonElement)this).getAsNumber().floatValue();
        }
        return Float.parseFloat(((JsonElement)this).getAsString());
    }

    @Override
    public final long getAsLong() {
        if (this.isNumber()) {
            return ((JsonElement)this).getAsNumber().longValue();
        }
        return Long.parseLong(((JsonElement)this).getAsString());
    }

    @Override
    public final short getAsShort() {
        if (this.isNumber()) {
            return ((JsonElement)this).getAsNumber().shortValue();
        }
        return Short.parseShort(((JsonElement)this).getAsString());
    }

    @Override
    public final int getAsInt() {
        if (this.isNumber()) {
            return ((JsonElement)this).getAsNumber().intValue();
        }
        return Integer.parseInt(((JsonElement)this).getAsString());
    }

    @Override
    public final byte getAsByte() {
        if (this.isNumber()) {
            return ((JsonElement)this).getAsNumber().byteValue();
        }
        return Byte.parseByte(((JsonElement)this).getAsString());
    }

    @Override
    @Deprecated
    public final char getAsCharacter() {
        String string = ((JsonElement)this).getAsString();
        if (string.isEmpty()) {
            throw new UnsupportedOperationException("String value is empty");
        }
        return string.charAt(0);
    }

    public final int hashCode() {
        if (this.value == null) {
            return 31;
        }
        if (JsonPrimitive.isIntegral(this)) {
            long l2 = ((JsonElement)this).getAsNumber().longValue();
            return (int)(l2 ^ l2 >>> 32);
        }
        if (this.value instanceof Number) {
            long l3 = Double.doubleToLongBits(((JsonElement)this).getAsNumber().doubleValue());
            return (int)(l3 ^ l3 >>> 32);
        }
        return this.value.hashCode();
    }

    public final boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        object = (JsonPrimitive)object;
        if (this.value == null) {
            return ((JsonPrimitive)object).value == null;
        }
        if (JsonPrimitive.isIntegral(this) && JsonPrimitive.isIntegral((JsonPrimitive)object)) {
            return ((JsonElement)this).getAsNumber().longValue() == ((JsonElement)object).getAsNumber().longValue();
        }
        if (this.value instanceof Number && ((JsonPrimitive)object).value instanceof Number) {
            double d2;
            double d3 = ((JsonElement)this).getAsNumber().doubleValue();
            return d3 == (d2 = ((JsonElement)object).getAsNumber().doubleValue()) || Double.isNaN(d3) && Double.isNaN(d2);
        }
        return this.value.equals(((JsonPrimitive)object).value);
    }

    private static boolean isIntegral(JsonPrimitive object) {
        if (((JsonPrimitive)object).value instanceof Number) {
            object = (Number)((JsonPrimitive)object).value;
            return object instanceof BigInteger || object instanceof Long || object instanceof Integer || object instanceof Short || object instanceof Byte;
        }
        return false;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson.internal;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.math.BigDecimal;

public final class LazilyParsedNumber
extends Number {
    private final String value;

    public LazilyParsedNumber(String string) {
        this.value = string;
    }

    @Override
    public final int intValue() {
        try {
            return Integer.parseInt(this.value);
        }
        catch (NumberFormatException numberFormatException) {
            try {
                return (int)Long.parseLong(this.value);
            }
            catch (NumberFormatException numberFormatException2) {
                return new BigDecimal(this.value).intValue();
            }
        }
    }

    @Override
    public final long longValue() {
        try {
            return Long.parseLong(this.value);
        }
        catch (NumberFormatException numberFormatException) {
            return new BigDecimal(this.value).longValue();
        }
    }

    @Override
    public final float floatValue() {
        return Float.parseFloat(this.value);
    }

    @Override
    public final double doubleValue() {
        return Double.parseDouble(this.value);
    }

    public final String toString() {
        return this.value;
    }

    private Object writeReplace() throws ObjectStreamException {
        return new BigDecimal(this.value);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException {
        throw new InvalidObjectException("Deserialization is unsupported");
    }

    public final int hashCode() {
        return this.value.hashCode();
    }

    public final boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof LazilyParsedNumber) {
            object = (LazilyParsedNumber)object;
            return this.value == ((LazilyParsedNumber)object).value || this.value.equals(((LazilyParsedNumber)object).value);
        }
        return false;
    }
}


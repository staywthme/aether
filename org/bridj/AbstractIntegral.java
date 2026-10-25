/*
 * Decompiled with CFR 0.152.
 */
package org.bridj;

abstract class AbstractIntegral
extends Number {
    protected final long value;
    private static final long HIGH_NEG = -4294967296L;

    public AbstractIntegral(long l2) {
        this.value = l2;
    }

    public abstract int byteSize();

    public static int safeIntCast(long l2) {
        long l3 = l2 & 0xFFFFFFFF00000000L;
        if (l3 != 0L && l3 != -4294967296L) {
            throw new RuntimeException("Value " + l2 + " = 0x" + Long.toHexString(l2) + " is not within the int range");
        }
        return (int)l2;
    }

    public int intValue() {
        return AbstractIntegral.safeIntCast(this.value);
    }

    public long longValue() {
        return this.value;
    }

    public float floatValue() {
        return this.value;
    }

    public double doubleValue() {
        return this.value;
    }

    public boolean equals(Object object) {
        if (object == null || !(object instanceof AbstractIntegral)) {
            return false;
        }
        if (!object.getClass().equals(this.getClass())) {
            return false;
        }
        return this.value == ((AbstractIntegral)object).value;
    }

    public int hashCode() {
        return Long.valueOf(this.value).hashCode();
    }

    public String toString() {
        return this.getClass().getSimpleName() + "(" + this.value + ")";
    }
}


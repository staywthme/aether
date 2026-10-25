/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna;

import com.sun.jna.FromNativeContext;
import com.sun.jna.Klass;
import com.sun.jna.NativeMapped;

public abstract class IntegerType
extends Number
implements NativeMapped {
    private static final long serialVersionUID = 1L;
    private int size;
    private Number number;
    private boolean unsigned;
    private long value;

    public IntegerType(int n2) {
        this(n2, 0L, false);
    }

    public IntegerType(int n2, boolean bl) {
        this(n2, 0L, bl);
    }

    public IntegerType(int n2, long l2) {
        this(n2, l2, false);
    }

    public IntegerType(int n2, long l2, boolean bl) {
        this.size = n2;
        this.unsigned = bl;
        this.setValue(l2);
    }

    public void setValue(long l2) {
        long l3 = l2;
        this.value = l2;
        switch (this.size) {
            case 1: {
                if (this.unsigned) {
                    this.value = l2 & 0xFFL;
                }
                l3 = (byte)l2;
                this.number = (byte)l2;
                break;
            }
            case 2: {
                if (this.unsigned) {
                    this.value = l2 & 0xFFFFL;
                }
                l3 = (short)l2;
                this.number = (short)l2;
                break;
            }
            case 4: {
                if (this.unsigned) {
                    this.value = l2 & 0xFFFFFFFFL;
                }
                l3 = (int)l2;
                this.number = (int)l2;
                break;
            }
            case 8: {
                this.number = l2;
                break;
            }
            default: {
                throw new IllegalArgumentException("Unsupported size: " + this.size);
            }
        }
        if (this.size < 8) {
            long l4 = (1L << (this.size << 3)) - 1L ^ 0xFFFFFFFFFFFFFFFFL;
            if (l2 < 0L && l3 != l2 || l2 >= 0L && (l4 & l2) != 0L) {
                throw new IllegalArgumentException("Argument value 0x" + Long.toHexString(l2) + " exceeds native capacity (" + this.size + " bytes) mask=0x" + Long.toHexString(l4));
            }
        }
    }

    @Override
    public Object toNative() {
        return this.number;
    }

    @Override
    public Object fromNative(Object object, FromNativeContext fromNativeContext) {
        long l2 = object == null ? 0L : ((Number)object).longValue();
        object = (IntegerType)Klass.newInstance(this.getClass());
        ((IntegerType)object).setValue(l2);
        return object;
    }

    @Override
    public Class<?> nativeType() {
        return this.number.getClass();
    }

    @Override
    public int intValue() {
        return (int)this.value;
    }

    @Override
    public long longValue() {
        return this.value;
    }

    @Override
    public float floatValue() {
        return this.number.floatValue();
    }

    @Override
    public double doubleValue() {
        return this.number.doubleValue();
    }

    public boolean equals(Object object) {
        return object instanceof IntegerType && this.number.equals(((IntegerType)object).number);
    }

    public String toString() {
        return this.number.toString();
    }

    public int hashCode() {
        return this.number.hashCode();
    }

    public static <T extends IntegerType> int compare(T t2, T t3) {
        if (t2 == t3) {
            return 0;
        }
        if (t2 == null) {
            return 1;
        }
        if (t3 == null) {
            return -1;
        }
        return IntegerType.compare(t2.longValue(), t3.longValue());
    }

    public static int compare(IntegerType integerType, long l2) {
        if (integerType == null) {
            return 1;
        }
        return IntegerType.compare(integerType.longValue(), l2);
    }

    public static final int compare(long l2, long l3) {
        if (l2 == l3) {
            return 0;
        }
        if (l2 < l3) {
            return -1;
        }
        return 1;
    }
}


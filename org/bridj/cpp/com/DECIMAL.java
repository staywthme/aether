/*
 * Decompiled with CFR 0.152.
 */
package org.bridj.cpp.com;

import org.bridj.CRuntime;
import org.bridj.StructObject;
import org.bridj.ann.Field;
import org.bridj.ann.Runtime;

@Runtime(value=CRuntime.class)
public class DECIMAL
extends StructObject {
    @Field(value=0)
    public short wReserved() {
        return this.io.getShortField(this, 0);
    }

    @Field(value=0)
    public DECIMAL wReserved(short s2) {
        this.io.setShortField(this, 0, s2);
        return this;
    }

    public final short wReserved_$eq(short s2) {
        this.wReserved(s2);
        return s2;
    }

    @Field(value=1)
    public byte scale() {
        return this.io.getByteField(this, 1);
    }

    @Field(value=1)
    public DECIMAL scale(byte by) {
        this.io.setByteField(this, 1, by);
        return this;
    }

    public final byte scale_$eq(byte by) {
        this.scale(by);
        return by;
    }

    @Field(value=2)
    public byte sign() {
        return this.io.getByteField(this, 2);
    }

    @Field(value=2)
    public DECIMAL sign(byte by) {
        this.io.setByteField(this, 2, by);
        return this;
    }

    public final byte sign_$eq(byte by) {
        this.sign(by);
        return by;
    }

    @Field(value=3)
    public int Hi32() {
        return this.io.getIntField(this, 3);
    }

    @Field(value=3)
    public DECIMAL Hi32(int n2) {
        this.io.setIntField(this, 3, n2);
        return this;
    }

    public final int Hi32_$eq(int n2) {
        this.Hi32(n2);
        return n2;
    }

    @Field(value=4)
    public long Lo64() {
        return this.io.getLongField(this, 4);
    }

    @Field(value=4)
    public DECIMAL Lo64(long l2) {
        this.io.setLongField(this, 4, l2);
        return this;
    }

    public final long Lo64_$eq(long l2) {
        this.Lo64(l2);
        return l2;
    }
}


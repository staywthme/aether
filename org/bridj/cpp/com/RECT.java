/*
 * Decompiled with CFR 0.152.
 */
package org.bridj.cpp.com;

import org.bridj.CRuntime;
import org.bridj.StructObject;
import org.bridj.ann.CLong;
import org.bridj.ann.Field;
import org.bridj.ann.Runtime;

@Runtime(value=CRuntime.class)
public class RECT
extends StructObject {
    @CLong
    @Field(value=0)
    public long left() {
        return this.io.getCLongField(this, 0);
    }

    @CLong
    @Field(value=0)
    public RECT left(long l2) {
        this.io.setCLongField(this, 0, l2);
        return this;
    }

    public final long left_$eq(long l2) {
        this.left(l2);
        return l2;
    }

    @CLong
    @Field(value=1)
    public long top() {
        return this.io.getCLongField(this, 1);
    }

    @CLong
    @Field(value=1)
    public RECT top(long l2) {
        this.io.setCLongField(this, 1, l2);
        return this;
    }

    public final long top_$eq(long l2) {
        this.top(l2);
        return l2;
    }

    @CLong
    @Field(value=2)
    public long right() {
        return this.io.getCLongField(this, 2);
    }

    @CLong
    @Field(value=2)
    public RECT right(long l2) {
        this.io.setCLongField(this, 2, l2);
        return this;
    }

    public final long right_$eq(long l2) {
        this.right(l2);
        return l2;
    }

    @CLong
    @Field(value=3)
    public long bottom() {
        return this.io.getCLongField(this, 3);
    }

    @CLong
    @Field(value=3)
    public RECT bottom(long l2) {
        this.io.setCLongField(this, 3, l2);
        return this;
    }

    public final long bottom_$eq(long l2) {
        this.bottom(l2);
        return l2;
    }
}


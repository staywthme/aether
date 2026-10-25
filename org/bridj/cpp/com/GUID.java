/*
 * Decompiled with CFR 0.152.
 */
package org.bridj.cpp.com;

import org.bridj.CRuntime;
import org.bridj.Pointer;
import org.bridj.StructObject;
import org.bridj.ann.Array;
import org.bridj.ann.Field;
import org.bridj.ann.Runtime;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
@Runtime(value=CRuntime.class)
public class GUID
extends StructObject {
    @Field(value=0)
    public int Data1() {
        return this.io.getIntField(this, 0);
    }

    @Field(value=0)
    public GUID Data1(int n2) {
        this.io.setIntField(this, 0, n2);
        return this;
    }

    public final int Data1_$eq(int n2) {
        this.Data1(n2);
        return n2;
    }

    @Field(value=1)
    public short Data2() {
        return this.io.getShortField(this, 1);
    }

    @Field(value=1)
    public GUID Data2(short s2) {
        this.io.setShortField(this, 1, s2);
        return this;
    }

    public final short Data2_$eq(short s2) {
        this.Data2(s2);
        return s2;
    }

    @Field(value=2)
    public short Data3() {
        return this.io.getShortField(this, 2);
    }

    @Field(value=2)
    public GUID Data3(short s2) {
        this.io.setShortField(this, 2, s2);
        return this;
    }

    public final short Data3_$eq(short s2) {
        this.Data3(s2);
        return s2;
    }

    @Array(value={8L})
    @Field(value=3)
    public Pointer<Byte> Data4() {
        return this.io.getPointerField(this, 3);
    }
}


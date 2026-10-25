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
import org.bridj.cpp.com.SAFEARRAYBOUND;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
@Runtime(value=CRuntime.class)
public class SAFEARRAY
extends StructObject {
    @Field(value=0)
    public short cDims() {
        return this.io.getShortField(this, 0);
    }

    @Field(value=0)
    public SAFEARRAY cDims(short s2) {
        this.io.setShortField(this, 0, s2);
        return this;
    }

    public final short cDims_$eq(short s2) {
        this.cDims(s2);
        return s2;
    }

    @Field(value=1)
    public short fFeatures() {
        return this.io.getShortField(this, 1);
    }

    @Field(value=1)
    public SAFEARRAY fFeatures(short s2) {
        this.io.setShortField(this, 1, s2);
        return this;
    }

    public final short fFeatures_$eq(short s2) {
        this.fFeatures(s2);
        return s2;
    }

    @Field(value=2)
    public int cbElements() {
        return this.io.getIntField(this, 2);
    }

    @Field(value=2)
    public SAFEARRAY cbElements(int n2) {
        this.io.setIntField(this, 2, n2);
        return this;
    }

    public final int cbElements_$eq(int n2) {
        this.cbElements(n2);
        return n2;
    }

    @Field(value=3)
    public int cLocks() {
        return this.io.getIntField(this, 3);
    }

    @Field(value=3)
    public SAFEARRAY cLocks(int n2) {
        this.io.setIntField(this, 3, n2);
        return this;
    }

    public final int cLocks_$eq(int n2) {
        this.cLocks(n2);
        return n2;
    }

    @Field(value=4)
    public Pointer<?> pvData() {
        return this.io.getPointerField(this, 4);
    }

    @Field(value=4)
    public SAFEARRAY pvData(Pointer<?> pointer) {
        this.io.setPointerField(this, 4, pointer);
        return this;
    }

    public final Pointer<?> pvData_$eq(Pointer<?> pointer) {
        this.pvData(pointer);
        return pointer;
    }

    @Array(value={1L})
    @Field(value=5)
    public Pointer<SAFEARRAYBOUND> rgsabound() {
        return this.io.getPointerField(this, 5);
    }
}


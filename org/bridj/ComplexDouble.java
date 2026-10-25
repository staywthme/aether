/*
 * Decompiled with CFR 0.152.
 */
package org.bridj;

import org.bridj.StructObject;
import org.bridj.ann.Field;

public class ComplexDouble
extends StructObject {
    @Field(value=0)
    public double real() {
        return this.io.getDoubleField(this, 0);
    }

    @Field(value=0)
    public ComplexDouble real(double d2) {
        this.io.setDoubleField(this, 0, d2);
        return this;
    }

    @Field(value=1)
    public double imag() {
        return this.io.getDoubleField(this, 0);
    }

    @Field(value=1)
    public ComplexDouble imag(double d2) {
        this.io.setDoubleField(this, 0, d2);
        return this;
    }
}


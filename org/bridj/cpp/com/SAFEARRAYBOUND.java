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
public class SAFEARRAYBOUND
extends StructObject {
    @Field(value=0)
    public int cElements() {
        return this.io.getIntField(this, 0);
    }

    @Field(value=0)
    public SAFEARRAYBOUND cElements(int n2) {
        this.io.setIntField(this, 0, n2);
        return this;
    }

    public final int cElements_$eq(int n2) {
        this.cElements(n2);
        return n2;
    }

    @CLong
    @Field(value=1)
    public long lLbound() {
        return this.io.getCLongField(this, 1);
    }

    @CLong
    @Field(value=1)
    public SAFEARRAYBOUND lLbound(long l2) {
        this.io.setCLongField(this, 1, l2);
        return this;
    }

    public final long lLbound_$eq(long l2) {
        this.lLbound(l2);
        return l2;
    }
}


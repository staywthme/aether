/*
 * Decompiled with CFR 0.152.
 */
package com.github.sarxos.webcam.ds.buildin.natives;

import org.bridj.Pointer;
import org.bridj.StructObject;
import org.bridj.ann.Field;
import org.bridj.ann.Library;
import org.bridj.ann.Runtime;
import org.bridj.cpp.CPPObject;
import org.bridj.cpp.CPPRuntime;

@Library(value="OpenIMAJGrabber")
@Runtime(value=CPPRuntime.class)
public final class Device
extends CPPObject {
    public Device() {
    }

    public Device(Pointer pointer) {
        super(pointer, new Object[0]);
    }

    @Field(value=0)
    protected final Pointer<Byte> name() {
        return this.io.getPointerField(this, 0);
    }

    @Field(value=0)
    protected final Device name(Pointer<Byte> pointer) {
        this.io.setPointerField(this, 0, pointer);
        return this;
    }

    @Field(value=1)
    protected final Pointer<Byte> identifier() {
        return this.io.getPointerField(this, 1);
    }

    @Field(value=1)
    protected final Device identifier(Pointer<Byte> pointer) {
        this.io.setPointerField(this, 1, pointer);
        return this;
    }

    protected final native Pointer<Byte> getName();

    public final String getNameStr() {
        return this.getName().getCString();
    }

    protected final native Pointer<Byte> getIdentifier();

    public final String getIdentifierStr() {
        return this.getIdentifier().getCString();
    }

    @Override
    public final boolean equals(Object object) {
        return object.toString().equals(((StructObject)this).toString());
    }

    public final int hashCode() {
        return ((StructObject)this).toString().hashCode();
    }

    @Override
    public final String toString() {
        return String.format("Device[%s]=\"%s\"", this.getIdentifierStr(), this.getNameStr());
    }
}


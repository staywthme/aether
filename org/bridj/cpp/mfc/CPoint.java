/*
 * Decompiled with CFR 0.152.
 */
package org.bridj.cpp.mfc;

import org.bridj.StructObject;
import org.bridj.ann.Field;

public class CPoint
extends StructObject {
    @Field(value=0)
    public native int x();

    @Field(value=0)
    public native void x(int var1);

    @Field(value=0)
    public native int y();

    @Field(value=0)
    public native void y(int var1);
}


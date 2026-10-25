/*
 * Decompiled with CFR 0.152.
 */
package org.bridj;

import org.bridj.BridJ;
import org.bridj.CRuntime;
import org.bridj.NativeObject;
import org.bridj.Pointer;
import org.bridj.StructFieldDescription;
import org.bridj.StructIO;
import org.bridj.ann.Runtime;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
@Runtime(value=CRuntime.class)
public abstract class StructObject
extends NativeObject {
    public StructIO io;

    public StructObject() {
    }

    public StructObject(Void void_, int n2, Object ... objectArray) {
        super(n2, objectArray);
    }

    public StructObject(Pointer<? extends StructObject> pointer, Object ... objectArray) {
        super(pointer, objectArray);
    }

    public String toString() {
        return BridJ.describe(this);
    }

    public static long offsetOfField(StructObject structFieldDescriptionArray, String string) {
        structFieldDescriptionArray = structFieldDescriptionArray.io.desc.fields;
        int n2 = structFieldDescriptionArray.io.desc.fields.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            StructFieldDescription structFieldDescription = structFieldDescriptionArray[i2];
            if (!structFieldDescription.name.equals(string)) continue;
            return structFieldDescription.byteOffset;
        }
        throw new NoSuchFieldError(string);
    }
}


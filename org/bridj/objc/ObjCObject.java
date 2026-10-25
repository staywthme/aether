/*
 * Decompiled with CFR 0.152.
 */
package org.bridj.objc;

import org.bridj.BridJ;
import org.bridj.NativeObject;
import org.bridj.Pointer;
import org.bridj.ann.Runtime;
import org.bridj.objc.IMP;
import org.bridj.objc.NSString;
import org.bridj.objc.ObjectiveCRuntime;
import org.bridj.objc.SEL;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
@Runtime(value=ObjectiveCRuntime.class)
public class ObjCObject
extends NativeObject {
    ObjCObject type;

    public native Pointer<ObjCObject> init();

    public native Pointer<NSString> stringValue();

    public native Pointer<NSString> description();

    public native int hash();

    public native boolean isEqual(Pointer<? extends ObjCObject> var1);

    public native boolean isKindOf(Pointer<? extends ObjCObject> var1);

    public native boolean isMemberOf(Pointer<? extends ObjCObject> var1);

    public native boolean isKindOfClassNamed(Pointer<Byte> var1);

    public native boolean isMemberOfClassNamed(Pointer<Byte> var1);

    public native boolean respondsTo(SEL var1);

    public native IMP methodFor(SEL var1);

    public native Pointer<?> perform(SEL var1);

    public native Pointer<?> perform$with(SEL var1, Pointer<?> var2);

    public native Pointer<?> perform$with$with(SEL var1, Pointer<?> var2, Pointer<?> var3);

    public ObjCObject(Pointer<? extends NativeObject> pointer) {
        super(pointer, new Object[0]);
    }

    public ObjCObject() {
    }

    public ObjCObject(int n2, Object ... objectArray) {
        super(n2, objectArray);
    }

    public String toString() {
        Pointer<NSString> pointer = this.description();
        if (pointer == null) {
            pointer = this.stringValue();
        }
        return ((ObjCObject)pointer.get()).toString();
    }

    @Override
    public boolean equals(Object pointer) {
        if (!(pointer instanceof ObjCObject)) {
            return false;
        }
        pointer = Pointer.getPointer((ObjCObject)((Object)pointer));
        return this.isEqual(pointer);
    }

    public int hashCode() {
        return this.hash();
    }

    static {
        BridJ.register();
    }
}


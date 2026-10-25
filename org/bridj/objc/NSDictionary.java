/*
 * Decompiled with CFR 0.152.
 */
package org.bridj.objc;

import java.util.HashMap;
import java.util.Map;
import org.bridj.BridJ;
import org.bridj.NativeObject;
import org.bridj.Pointer;
import org.bridj.objc.FoundationLibrary;
import org.bridj.objc.NSObject;
import org.bridj.objc.NSString;
import org.bridj.objc.ObjCObject;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class NSDictionary
extends NSObject {
    public native Pointer<NSObject> valueForKey(Pointer<NSString> var1);

    public native Pointer<NSObject> objectForKey(Pointer<NSObject> var1);

    public native int count();

    public native void getObjects_andKeys(Pointer<Pointer<NSObject>> var1, Pointer<Pointer<NSObject>> var2);

    public static native Pointer<NSDictionary> dictionaryWithContentsOfFile(Pointer<NSString> var0);

    public static native Pointer<NSDictionary> dictionaryWithObjects_forKeys_count(Pointer<Pointer<NSObject>> var0, Pointer<Pointer<NSObject>> var1, int var2);

    public static Pointer<NSDictionary> pointerToNSDictionary(Map<String, NSObject> object) {
        int n2 = object.size();
        Pointer<Pointer<NSObject>> pointer = Pointer.allocatePointers(NSObject.class, n2);
        Pointer<Pointer<NSObject>> pointer2 = Pointer.allocatePointers(NSObject.class, n2);
        int n3 = 0;
        for (Map.Entry entry : object.entrySet()) {
            pointer2.set(n3, FoundationLibrary.pointerToNSString((String)entry.getKey()));
            pointer.set(n3, Pointer.getPointer((NativeObject)entry.getValue()));
            ++n3;
        }
        return NSDictionary.dictionaryWithObjects_forKeys_count(pointer, pointer2, n2);
    }

    public static NSDictionary valueOf(Map<String, NSObject> map) {
        return NSDictionary.pointerToNSDictionary(map).get();
    }

    public Map<String, NSObject> toMap() {
        int n2 = this.count();
        Pointer<Pointer<NSObject>> pointer = Pointer.allocatePointers(NSObject.class, n2);
        Pointer<Pointer<NSObject>> pointer2 = Pointer.allocatePointers(NSString.class, n2);
        this.getObjects_andKeys(pointer, pointer2);
        HashMap<String, NSObject> hashMap = new HashMap<String, NSObject>();
        for (int i2 = 0; i2 < n2; ++i2) {
            Pointer<NSObject> pointer3 = pointer2.get(i2);
            Pointer<NSObject> pointer4 = pointer.get(i2);
            hashMap.put(((ObjCObject)((NSString)pointer3.get())).toString(), pointer4 == null ? null : pointer4.get());
        }
        return hashMap;
    }

    static {
        BridJ.register();
    }
}


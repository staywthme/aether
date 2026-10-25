/*
 * Decompiled with CFR 0.152.
 */
package org.bridj.cpp.com;

import org.bridj.BridJ;
import org.bridj.Pointer;
import org.bridj.ann.Library;
import org.bridj.ann.Name;
import org.bridj.ann.Ptr;
import org.bridj.ann.Runtime;
import org.bridj.cpp.CPPRuntime;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
@Library(value="ole32")
@Runtime(value=CPPRuntime.class)
public class OLELibrary {
    public static native Pointer<?> CoTaskMemAlloc(@Ptr long var0);

    @Name(value="CoTaskMemAlloc")
    @Ptr
    public static native long CoTaskMemAlloc$raw(@Ptr long var0);

    public static void CoTaskMemFree(Pointer<?> pointer) {
        OLELibrary.CoTaskMemFree(Pointer.getPeer(pointer));
    }

    public static native void CoTaskMemFree(@Ptr long var0);

    public static Pointer<?> CoTaskMemRealloc(Pointer<?> pointer, @Ptr long l2) {
        return Pointer.pointerToAddress(OLELibrary.CoTaskMemRealloc(Pointer.getPeer(pointer), l2));
    }

    @Ptr
    public static native long CoTaskMemRealloc(@Ptr long var0, @Ptr long var2);

    static {
        BridJ.register();
    }
}


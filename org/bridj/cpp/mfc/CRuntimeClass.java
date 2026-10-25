/*
 * Decompiled with CFR 0.152.
 */
package org.bridj.cpp.mfc;

import org.bridj.Pointer;
import org.bridj.ann.Field;
import org.bridj.cpp.mfc.CArchive;
import org.bridj.cpp.mfc.CObject;
import org.bridj.cpp.mfc.MFCObject;
import org.bridj.func.Fun0;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class CRuntimeClass
extends MFCObject {
    @Field(value=0)
    public native Pointer<Byte> m_lpszClassName();

    public native void m_lpszClassName(Pointer<Byte> var1);

    @Field(value=1)
    public native int m_nObjectSize();

    public native void m_nObjectSize(int var1);

    @Field(value=2)
    public native int m_wSchema();

    public native void m_wSchema(int var1);

    @Field(value=3)
    public native Pointer<Fun0<Pointer<CObject>>> m_pfnCreateObject();

    public native void m_pfnCreateObject(Pointer<Fun0<Pointer<CObject>>> var1);

    @Field(value=4)
    public native Pointer<CRuntimeClass> m_pBaseClass();

    public native void m_pBaseClass(Pointer<CRuntimeClass> var1);

    public native Pointer<CObject> CreateObject();

    public native boolean IsDerivedFrom(Pointer<CRuntimeClass> var1);

    public static native Pointer<CRuntimeClass> FromName(Pointer<Byte> var0);

    public static native Pointer<CRuntimeClass> FromName$2(Pointer<Character> var0);

    public static native Pointer<CObject> CreateObject(Pointer<Byte> var0);

    public static native Pointer<CObject> CreateObject$2(Pointer<Character> var0);

    public native void Store(Pointer<CArchive> var1);

    public static native Pointer<CRuntimeClass> Load(Pointer<CArchive> var0, Pointer<Integer> var1);

    @Field(value=5)
    public native Pointer<CRuntimeClass> m_pNextClass();

    public native void m_pNextClass(Pointer<CRuntimeClass> var1);

    @Field(value=6)
    public native Pointer<?> m_pClassInit();

    public native void m_pClassInit(Pointer<?> var1);
}


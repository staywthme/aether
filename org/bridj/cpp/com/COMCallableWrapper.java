/*
 * Decompiled with CFR 0.152.
 */
package org.bridj.cpp.com;

import org.bridj.Pointer;
import org.bridj.cpp.com.IDispatch;
import org.bridj.cpp.com.ITypeInfo;
import org.bridj.cpp.com.VARIANT;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class COMCallableWrapper
extends IDispatch {
    Object instance;

    public COMCallableWrapper(Object object) {
        this.instance = object;
    }

    @Override
    public int GetIDsOfNames(Pointer pointer, Pointer<Pointer<Character>> pointer2, int n2, int n3, Pointer<Integer> pointer3) {
        return -2147467263;
    }

    @Override
    public int Invoke(int n2, Pointer<Byte> pointer, int n3, short s2, Pointer<IDispatch.DISPPARAMS> pointer2, Pointer<VARIANT> pointer3, Pointer<IDispatch.EXCEPINFO> pointer4, Pointer<Integer> pointer5) {
        return -2147467263;
    }

    @Override
    public int GetTypeInfo(int n2, int n3, Pointer<Pointer<ITypeInfo>> pointer) {
        return -2147467263;
    }

    @Override
    public int GetTypeInfoCount(Pointer<Integer> pointer) {
        return -2147467263;
    }
}


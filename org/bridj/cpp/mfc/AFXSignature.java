/*
 * Decompiled with CFR 0.152.
 */
package org.bridj.cpp.mfc;

import org.bridj.Pointer;
import org.bridj.cpp.mfc.CPoint;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public enum AFXSignature {
    AfxSig_vwww(Void.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE),
    AfxSig_vwp(Void.TYPE, Pointer.class, CPoint.class);

    final Class<?> returnType;
    final Class<?>[] paramTypes;

    private AFXSignature(Class<?> clazz, Class<?> ... classArray) {
        this.returnType = clazz;
        this.paramTypes = classArray;
    }
}


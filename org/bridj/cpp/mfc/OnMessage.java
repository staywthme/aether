/*
 * Decompiled with CFR 0.152.
 */
package org.bridj.cpp.mfc;

import org.bridj.cpp.mfc.AFXSignature;

public @interface OnMessage {
    public Type value();

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static enum Type {
        WM_KEYDOWN("OnKeyDown", AFXSignature.AfxSig_vwww),
        WM_LBUTTONDOWN("OnLButtonDown", AFXSignature.AfxSig_vwp);

        final String defaultName;
        final AFXSignature afxSig;

        private Type(String string2, AFXSignature aFXSignature) {
            this.afxSig = aFXSignature;
            this.defaultName = string2;
        }
    }
}


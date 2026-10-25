/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.security.PublicKey;
import java.util.Arrays;

final class X509PublicKey
implements PublicKey {
    private static final long serialVersionUID = -8610156854731664298L;
    private final String algorithm;
    private final byte[] encoded;

    X509PublicKey(String string, byte[] byArray) {
        this.algorithm = string;
        this.encoded = byArray;
    }

    @Override
    public final String getAlgorithm() {
        return this.algorithm;
    }

    @Override
    public final String getFormat() {
        return "X.509";
    }

    @Override
    public final byte[] getEncoded() {
        return this.encoded;
    }

    public final String toString() {
        return "X509PublicKey [algorithm=" + this.algorithm + ", encoded=" + Arrays.toString(this.encoded) + "]";
    }

    public final int hashCode() {
        int n2 = 31 + (this.algorithm == null ? 0 : this.algorithm.hashCode());
        n2 = n2 * 31 + Arrays.hashCode(this.encoded);
        return n2;
    }

    public final boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (this.getClass() != object.getClass()) {
            return false;
        }
        object = (X509PublicKey)object;
        if (this.algorithm == null ? ((X509PublicKey)object).algorithm != null : !this.algorithm.equals(((X509PublicKey)object).algorithm)) {
            return false;
        }
        return Arrays.equals(this.encoded, ((X509PublicKey)object).encoded);
    }
}


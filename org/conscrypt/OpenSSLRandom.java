/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.io.Serializable;
import java.security.SecureRandomSpi;
import org.conscrypt.NativeCrypto;

public final class OpenSSLRandom
extends SecureRandomSpi
implements Serializable {
    private static final long serialVersionUID = 8506210602917522861L;

    @Override
    protected final void engineSetSeed(byte[] byArray) {
        if (byArray == null) {
            throw new NullPointerException("seed == null");
        }
    }

    @Override
    protected final void engineNextBytes(byte[] byArray) {
        NativeCrypto.RAND_bytes(byArray);
    }

    @Override
    protected final byte[] engineGenerateSeed(int n2) {
        byte[] byArray = new byte[n2];
        NativeCrypto.RAND_bytes(byArray);
        return byArray;
    }
}


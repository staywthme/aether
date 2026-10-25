/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.NoSuchPaddingException;
import org.conscrypt.OpenSSLCipher;
import org.conscrypt.OpenSSLEvpCipher;

public class OpenSSLEvpCipherARC4
extends OpenSSLEvpCipher {
    public OpenSSLEvpCipherARC4() {
        super(OpenSSLCipher.Mode.ECB, OpenSSLCipher.Padding.NOPADDING);
    }

    @Override
    String getBaseCipherName() {
        return "ARCFOUR";
    }

    @Override
    String getCipherName(int n2, OpenSSLCipher.Mode mode) {
        return "rc4";
    }

    @Override
    void checkSupportedKeySize(int n2) throws InvalidKeyException {
    }

    @Override
    void checkSupportedMode(OpenSSLCipher.Mode mode) throws NoSuchAlgorithmException {
        if (mode != OpenSSLCipher.Mode.NONE && mode != OpenSSLCipher.Mode.ECB) {
            throw new NoSuchAlgorithmException("Unsupported mode " + mode.toString());
        }
    }

    @Override
    void checkSupportedPadding(OpenSSLCipher.Padding padding) throws NoSuchPaddingException {
        if (padding != OpenSSLCipher.Padding.NOPADDING) {
            throw new NoSuchPaddingException("Unsupported padding " + padding.toString());
        }
    }

    @Override
    int getCipherBlockSize() {
        return 0;
    }

    @Override
    boolean supportsVariableSizeKey() {
        return true;
    }
}


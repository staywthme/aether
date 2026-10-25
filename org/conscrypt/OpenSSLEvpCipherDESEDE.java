/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import javax.crypto.NoSuchPaddingException;
import org.conscrypt.OpenSSLCipher;
import org.conscrypt.OpenSSLEvpCipher;

public abstract class OpenSSLEvpCipherDESEDE
extends OpenSSLEvpCipher {
    private static final int DES_BLOCK_SIZE = 8;

    OpenSSLEvpCipherDESEDE(OpenSSLCipher.Mode mode, OpenSSLCipher.Padding padding) {
        super(mode, padding);
    }

    @Override
    String getBaseCipherName() {
        return "DESede";
    }

    @Override
    String getCipherName(int n2, OpenSSLCipher.Mode mode) {
        String string = n2 == 16 ? "des-ede" : "des-ede3";
        return string + "-" + mode.toString().toLowerCase(Locale.US);
    }

    @Override
    void checkSupportedKeySize(int n2) throws InvalidKeyException {
        if (n2 != 16 && n2 != 24) {
            throw new InvalidKeyException("key size must be 128 or 192 bits");
        }
    }

    @Override
    void checkSupportedMode(OpenSSLCipher.Mode mode) throws NoSuchAlgorithmException {
        if (mode != OpenSSLCipher.Mode.CBC) {
            throw new NoSuchAlgorithmException("Unsupported mode " + mode.toString());
        }
    }

    @Override
    void checkSupportedPadding(OpenSSLCipher.Padding padding) throws NoSuchPaddingException {
        switch (padding) {
            case NOPADDING: 
            case PKCS5PADDING: {
                return;
            }
        }
        throw new NoSuchPaddingException("Unsupported padding " + padding.toString());
    }

    @Override
    int getCipherBlockSize() {
        return 8;
    }

    public static class CBC
    extends OpenSSLEvpCipherDESEDE {
        CBC(OpenSSLCipher.Padding padding) {
            super(OpenSSLCipher.Mode.CBC, padding);
        }

        public static class PKCS5Padding
        extends CBC {
            public PKCS5Padding() {
                super(OpenSSLCipher.Padding.PKCS5PADDING);
            }
        }

        public static class NoPadding
        extends CBC {
            public NoPadding() {
                super(OpenSSLCipher.Padding.NOPADDING);
            }
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import org.conscrypt.NativeCrypto;
import org.conscrypt.OpenSSLAeadCipher;
import org.conscrypt.OpenSSLCipher;
import org.conscrypt.Platform;

public abstract class OpenSSLAeadCipherAES
extends OpenSSLAeadCipher {
    private static final int AES_BLOCK_SIZE = 16;

    OpenSSLAeadCipherAES(OpenSSLCipher.Mode mode) {
        super(mode);
    }

    @Override
    void checkSupportedKeySize(int n2) throws InvalidKeyException {
        switch (n2) {
            case 16: 
            case 32: {
                return;
            }
        }
        throw new InvalidKeyException("Unsupported key size: " + n2 + " bytes (must be 16 or 32)");
    }

    @Override
    String getBaseCipherName() {
        return "AES";
    }

    @Override
    int getCipherBlockSize() {
        return 16;
    }

    @Override
    protected AlgorithmParameterSpec getParameterSpec(AlgorithmParameters algorithmParameters) throws InvalidAlgorithmParameterException {
        if (algorithmParameters != null) {
            AlgorithmParameterSpec algorithmParameterSpec = Platform.fromGCMParameters(algorithmParameters);
            if (algorithmParameterSpec != null) {
                return algorithmParameterSpec;
            }
            return super.getParameterSpec(algorithmParameters);
        }
        return null;
    }

    @Override
    protected AlgorithmParameters engineGetParameters() {
        if (this.iv == null) {
            return null;
        }
        AlgorithmParameterSpec algorithmParameterSpec = Platform.toGCMParameterSpec(this.tagLengthInBytes << 3, this.iv);
        if (algorithmParameterSpec == null) {
            return super.engineGetParameters();
        }
        try {
            AlgorithmParameters algorithmParameters = AlgorithmParameters.getInstance("GCM");
            algorithmParameters.init(algorithmParameterSpec);
            return algorithmParameters;
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw (Error)((Throwable)((Object)new AssertionError((Object)"GCM not supported"))).initCause(noSuchAlgorithmException);
        }
        catch (InvalidParameterSpecException invalidParameterSpecException) {
            return null;
        }
    }

    @Override
    int getOutputSizeForFinal(int n2) {
        if (this.isEncrypting()) {
            return this.bufCount + n2 + this.tagLengthInBytes;
        }
        return Math.max(0, this.bufCount + n2 - this.tagLengthInBytes);
    }

    public static class GCM_SIV
    extends OpenSSLAeadCipherAES {
        public GCM_SIV() {
            super(OpenSSLCipher.Mode.GCM_SIV);
        }

        @Override
        void checkSupportedMode(OpenSSLCipher.Mode mode) throws NoSuchAlgorithmException {
            if (mode != OpenSSLCipher.Mode.GCM_SIV) {
                throw new NoSuchAlgorithmException("Mode must be GCM-SIV");
            }
        }

        @Override
        boolean allowsNonceReuse() {
            return true;
        }

        @Override
        void checkSupportedTagLength(int n2) throws InvalidAlgorithmParameterException {
            if (n2 != 128) {
                throw new InvalidAlgorithmParameterException("Tag length must be 128 bits");
            }
        }

        @Override
        long getEVP_AEAD(int n2) throws InvalidKeyException {
            if (n2 == 16) {
                return NativeCrypto.EVP_aead_aes_128_gcm_siv();
            }
            if (n2 == 32) {
                return NativeCrypto.EVP_aead_aes_256_gcm_siv();
            }
            throw new RuntimeException("Unexpected key length: " + n2);
        }

        public static class AES_256
        extends GCM_SIV {
            @Override
            void checkSupportedKeySize(int n2) throws InvalidKeyException {
                if (n2 != 32) {
                    throw new InvalidKeyException("Unsupported key size: " + n2 + " bytes (must be 32)");
                }
            }
        }

        public static class AES_128
        extends GCM_SIV {
            @Override
            void checkSupportedKeySize(int n2) throws InvalidKeyException {
                if (n2 != 16) {
                    throw new InvalidKeyException("Unsupported key size: " + n2 + " bytes (must be 16)");
                }
            }
        }
    }

    public static class GCM
    extends OpenSSLAeadCipherAES {
        public GCM() {
            super(OpenSSLCipher.Mode.GCM);
        }

        @Override
        void checkSupportedMode(OpenSSLCipher.Mode mode) throws NoSuchAlgorithmException {
            if (mode != OpenSSLCipher.Mode.GCM) {
                throw new NoSuchAlgorithmException("Mode must be GCM");
            }
        }

        @Override
        long getEVP_AEAD(int n2) throws InvalidKeyException {
            if (n2 == 16) {
                return NativeCrypto.EVP_aead_aes_128_gcm();
            }
            if (n2 == 32) {
                return NativeCrypto.EVP_aead_aes_256_gcm();
            }
            throw new RuntimeException("Unexpected key length: " + n2);
        }

        public static class AES_256
        extends GCM {
            @Override
            void checkSupportedKeySize(int n2) throws InvalidKeyException {
                if (n2 != 32) {
                    throw new InvalidKeyException("Unsupported key size: " + n2 + " bytes (must be 32)");
                }
            }
        }

        public static class AES_128
        extends GCM {
            @Override
            void checkSupportedKeySize(int n2) throws InvalidKeyException {
                if (n2 != 16) {
                    throw new InvalidKeyException("Unsupported key size: " + n2 + " bytes (must be 16)");
                }
            }
        }
    }
}


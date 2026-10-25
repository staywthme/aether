/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import org.conscrypt.NativeCrypto;
import org.conscrypt.OpenSSLCipher;
import org.conscrypt.ShortBufferWithoutStackTraceException;

public class OpenSSLCipherChaCha20
extends OpenSSLCipher {
    private static final int BLOCK_SIZE_BYTES = 64;
    private static final int NONCE_SIZE_BYTES = 12;
    private int currentBlockConsumedBytes = 0;
    private int blockCounter = 0;

    @Override
    void engineInitInternal(byte[] object, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (algorithmParameterSpec instanceof IvParameterSpec) {
            IvParameterSpec ivParameterSpec = (IvParameterSpec)algorithmParameterSpec;
            object = ivParameterSpec;
            if (ivParameterSpec.getIV().length != 12) {
                throw new InvalidAlgorithmParameterException("IV must be 12 bytes long");
            }
            this.iv = ((IvParameterSpec)object).getIV();
            return;
        }
        if (!this.isEncrypting()) {
            throw new InvalidAlgorithmParameterException("IV must be specified when decrypting");
        }
        this.iv = new byte[12];
        if (secureRandom != null) {
            secureRandom.nextBytes(this.iv);
            return;
        }
        NativeCrypto.RAND_bytes(this.iv);
    }

    @Override
    int updateInternal(byte[] byArray, int n2, int n3, byte[] byArray2, int n4, int n5) throws ShortBufferException {
        if (n3 > byArray2.length - n4) {
            throw new ShortBufferWithoutStackTraceException("Insufficient output space");
        }
        n5 = n3;
        if (this.currentBlockConsumedBytes > 0) {
            n5 = Math.min(64 - this.currentBlockConsumedBytes, n3);
            byte[] byArray3 = new byte[64];
            byte[] byArray4 = new byte[64];
            System.arraycopy(byArray, n2, byArray3, this.currentBlockConsumedBytes, n5);
            NativeCrypto.chacha20_encrypt_decrypt(byArray3, 0, byArray4, 0, 64, this.encodedKey, this.iv, this.blockCounter);
            System.arraycopy(byArray4, this.currentBlockConsumedBytes, byArray2, n4, n5);
            this.currentBlockConsumedBytes += n5;
            if (this.currentBlockConsumedBytes < 64) {
                return n5;
            }
            assert (this.currentBlockConsumedBytes == 64);
            this.currentBlockConsumedBytes = 0;
            n2 += n5;
            n4 += n5;
            n5 = n3 - n5;
            ++this.blockCounter;
        }
        NativeCrypto.chacha20_encrypt_decrypt(byArray, n2, byArray2, n4, n5, this.encodedKey, this.iv, this.blockCounter);
        this.currentBlockConsumedBytes = n5 % 64;
        this.blockCounter += n5 / 64;
        return n3;
    }

    @Override
    int doFinalInternal(byte[] byArray, int n2, int n3) throws IllegalBlockSizeException, BadPaddingException, ShortBufferException {
        this.reset();
        return 0;
    }

    private void reset() {
        this.blockCounter = 0;
        this.currentBlockConsumedBytes = 0;
    }

    @Override
    String getBaseCipherName() {
        return "ChaCha20";
    }

    @Override
    void checkSupportedKeySize(int n2) throws InvalidKeyException {
        if (n2 != 32) {
            throw new InvalidKeyException("Unsupported key size: " + n2 + " bytes (must be 32)");
        }
    }

    @Override
    void checkSupportedMode(OpenSSLCipher.Mode mode) throws NoSuchAlgorithmException {
        if (mode != OpenSSLCipher.Mode.NONE) {
            throw new NoSuchAlgorithmException("Mode must be NONE");
        }
    }

    @Override
    void checkSupportedPadding(OpenSSLCipher.Padding padding) throws NoSuchPaddingException {
        if (padding != OpenSSLCipher.Padding.NOPADDING) {
            throw new NoSuchPaddingException("Must be NoPadding");
        }
    }

    @Override
    int getCipherBlockSize() {
        return 0;
    }

    @Override
    int getOutputSizeForFinal(int n2) {
        return n2;
    }

    @Override
    int getOutputSizeForUpdate(int n2) {
        return n2;
    }
}


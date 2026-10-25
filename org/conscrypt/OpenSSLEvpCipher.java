/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import org.conscrypt.NativeCrypto;
import org.conscrypt.NativeRef;
import org.conscrypt.OpenSSLCipher;
import org.conscrypt.ShortBufferWithoutStackTraceException;

public abstract class OpenSSLEvpCipher
extends OpenSSLCipher {
    private final NativeRef.EVP_CIPHER_CTX cipherCtx = new NativeRef.EVP_CIPHER_CTX(NativeCrypto.EVP_CIPHER_CTX_new());
    private boolean calledUpdate;
    private int modeBlockSize;

    public OpenSSLEvpCipher(OpenSSLCipher.Mode mode, OpenSSLCipher.Padding padding) {
        super(mode, padding);
    }

    @Override
    void engineInitInternal(byte[] byArray, AlgorithmParameterSpec object, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (object instanceof IvParameterSpec) {
            IvParameterSpec ivParameterSpec = (IvParameterSpec)object;
            object = ivParameterSpec.getIV();
        } else {
            object = null;
        }
        long l2 = NativeCrypto.EVP_get_cipherbyname(this.getCipherName(byArray.length, this.mode));
        if (l2 == 0L) {
            throw new InvalidAlgorithmParameterException("Cannot find name for key length = " + (byArray.length << 3) + " and mode = " + (Object)((Object)this.mode));
        }
        boolean bl = this.isEncrypting();
        int n2 = NativeCrypto.EVP_CIPHER_iv_length(l2);
        if (object == null && n2 != 0) {
            if (!bl) {
                throw new InvalidAlgorithmParameterException("IV must be specified in " + (Object)((Object)this.mode) + " mode");
            }
            object = new byte[n2];
            if (secureRandom != null) {
                secureRandom.nextBytes((byte[])object);
            } else {
                NativeCrypto.RAND_bytes((byte[])object);
            }
        } else {
            if (n2 == 0 && object != null) {
                throw new InvalidAlgorithmParameterException("IV not used in " + (Object)((Object)this.mode) + " mode");
            }
            if (object != null && ((Object)object).length != n2) {
                throw new InvalidAlgorithmParameterException("expected IV length of " + n2 + " but was " + ((Object)object).length);
            }
        }
        this.iv = (byte[])object;
        if (this.supportsVariableSizeKey()) {
            NativeCrypto.EVP_CipherInit_ex(this.cipherCtx, l2, null, null, bl);
            NativeCrypto.EVP_CIPHER_CTX_set_key_length(this.cipherCtx, byArray.length);
            NativeCrypto.EVP_CipherInit_ex(this.cipherCtx, 0L, byArray, (byte[])object, this.isEncrypting());
        } else {
            NativeCrypto.EVP_CipherInit_ex(this.cipherCtx, l2, byArray, (byte[])object, bl);
        }
        NativeCrypto.EVP_CIPHER_CTX_set_padding(this.cipherCtx, this.getPadding() == OpenSSLCipher.Padding.PKCS5PADDING);
        this.modeBlockSize = NativeCrypto.EVP_CIPHER_CTX_block_size(this.cipherCtx);
        this.calledUpdate = false;
    }

    @Override
    int updateInternal(byte[] byArray, int n2, int n3, byte[] byArray2, int n4, int n5) throws ShortBufferException {
        int n6 = n4;
        int n7 = byArray2.length - n4;
        if (n7 < n5) {
            throw new ShortBufferWithoutStackTraceException("output buffer too small during update: " + n7 + " < " + n5);
        }
        n4 += NativeCrypto.EVP_CipherUpdate(this.cipherCtx, byArray2, n4, byArray, n2, n3);
        this.calledUpdate = true;
        return n4 - n6;
    }

    @Override
    int doFinalInternal(byte[] byArray, int n2, int n3) throws IllegalBlockSizeException, BadPaddingException, ShortBufferException {
        int n4 = n2;
        if (!this.isEncrypting() && !this.calledUpdate) {
            return 0;
        }
        int n5 = byArray.length - n2;
        if (n5 >= n3) {
            n3 = NativeCrypto.EVP_CipherFinal_ex(this.cipherCtx, byArray, n2);
        } else {
            byte[] byArray2 = new byte[n3];
            if ((n3 = NativeCrypto.EVP_CipherFinal_ex(this.cipherCtx, byArray2, 0)) > n5) {
                throw new ShortBufferWithoutStackTraceException("buffer is too short: " + n3 + " > " + n5);
            }
            if (n3 > 0) {
                System.arraycopy(byArray2, 0, byArray, n2, n3);
            }
        }
        this.reset();
        return (n2 += n3) - n4;
    }

    @Override
    int getOutputSizeForFinal(int n2) {
        if (this.modeBlockSize == 1) {
            return n2;
        }
        int n3 = NativeCrypto.get_EVP_CIPHER_CTX_buf_len(this.cipherCtx);
        if (this.getPadding() == OpenSSLCipher.Padding.NOPADDING) {
            return n3 + n2;
        }
        boolean bl = NativeCrypto.get_EVP_CIPHER_CTX_final_used(this.cipherCtx);
        n2 = n2 + n3 + (bl ? this.modeBlockSize : 0);
        return (n2 += n2 % this.modeBlockSize != 0 || this.isEncrypting() ? this.modeBlockSize : 0) - n2 % this.modeBlockSize;
    }

    @Override
    int getOutputSizeForUpdate(int n2) {
        return this.getOutputSizeForFinal(n2);
    }

    abstract String getCipherName(int var1, OpenSSLCipher.Mode var2);

    private void reset() {
        NativeCrypto.EVP_CipherInit_ex(this.cipherCtx, 0L, this.encodedKey, this.iv, this.isEncrypting());
        this.calledUpdate = false;
    }
}


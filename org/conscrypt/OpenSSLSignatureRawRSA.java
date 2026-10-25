/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.SignatureSpi;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import org.conscrypt.NativeCrypto;
import org.conscrypt.OpenSSLKey;
import org.conscrypt.OpenSSLRSAPrivateCrtKey;
import org.conscrypt.OpenSSLRSAPrivateKey;
import org.conscrypt.OpenSSLRSAPublicKey;

public final class OpenSSLSignatureRawRSA
extends SignatureSpi {
    private OpenSSLKey key;
    private byte[] inputBuffer;
    private int inputOffset;
    private boolean inputIsTooLong;

    @Override
    protected final void engineUpdate(byte by) {
        int n2 = this.inputOffset++;
        if (this.inputOffset > this.inputBuffer.length) {
            this.inputIsTooLong = true;
            return;
        }
        this.inputBuffer[n2] = by;
    }

    @Override
    protected final void engineUpdate(byte[] byArray, int n2, int n3) {
        int n4 = this.inputOffset;
        this.inputOffset += n3;
        if (this.inputOffset > this.inputBuffer.length) {
            this.inputIsTooLong = true;
            return;
        }
        System.arraycopy(byArray, n2, this.inputBuffer, n4, n3);
    }

    @Override
    protected final Object engineGetParameter(String string) throws InvalidParameterException {
        return null;
    }

    @Override
    protected final void engineInitSign(PrivateKey privateKey) throws InvalidKeyException {
        if (privateKey instanceof OpenSSLRSAPrivateKey) {
            privateKey = (OpenSSLRSAPrivateKey)privateKey;
            this.key = ((OpenSSLRSAPrivateKey)privateKey).getOpenSSLKey();
        } else if (privateKey instanceof RSAPrivateCrtKey) {
            privateKey = (RSAPrivateCrtKey)privateKey;
            this.key = OpenSSLRSAPrivateCrtKey.getInstance((RSAPrivateCrtKey)privateKey);
        } else if (privateKey instanceof RSAPrivateKey) {
            privateKey = (RSAPrivateKey)privateKey;
            this.key = OpenSSLRSAPrivateKey.getInstance((RSAPrivateKey)privateKey);
        } else {
            throw new InvalidKeyException("Need RSA private key");
        }
        int n2 = NativeCrypto.RSA_size(this.key.getNativeRef());
        this.inputBuffer = new byte[n2];
        this.inputOffset = 0;
    }

    @Override
    protected final void engineInitVerify(PublicKey publicKey) throws InvalidKeyException {
        if (publicKey instanceof OpenSSLRSAPublicKey) {
            publicKey = (OpenSSLRSAPublicKey)publicKey;
            this.key = ((OpenSSLRSAPublicKey)publicKey).getOpenSSLKey();
        } else if (publicKey instanceof RSAPublicKey) {
            publicKey = (RSAPublicKey)publicKey;
            this.key = OpenSSLRSAPublicKey.getInstance((RSAPublicKey)publicKey);
        } else {
            throw new InvalidKeyException("Need RSA public key");
        }
        int n2 = NativeCrypto.RSA_size(this.key.getNativeRef());
        this.inputBuffer = new byte[n2];
        this.inputOffset = 0;
    }

    @Override
    protected final void engineSetParameter(String string, Object object) throws InvalidParameterException {
    }

    @Override
    protected final byte[] engineSign() throws SignatureException {
        if (this.key == null) {
            throw new SignatureException("Need RSA private key");
        }
        if (this.inputIsTooLong) {
            throw new SignatureException("input length " + this.inputOffset + " != " + this.inputBuffer.length + " (modulus size)");
        }
        byte[] byArray = new byte[this.inputBuffer.length];
        try {
            NativeCrypto.RSA_private_encrypt(this.inputOffset, this.inputBuffer, byArray, this.key.getNativeRef(), 1);
            return byArray;
        }
        catch (Exception exception) {
            throw new SignatureException(exception);
        }
        finally {
            this.inputOffset = 0;
        }
    }

    @Override
    protected final boolean engineVerify(byte[] byArray) throws SignatureException {
        if (this.key == null) {
            throw new SignatureException("Need RSA public key");
        }
        if (this.inputIsTooLong) {
            return false;
        }
        if (byArray.length > this.inputBuffer.length) {
            throw new SignatureException("Input signature length is too large: " + byArray.length + " > " + this.inputBuffer.length);
        }
        byte[] byArray2 = new byte[this.inputBuffer.length];
        try {
            int n2;
            int n3;
            try {
                n3 = NativeCrypto.RSA_public_decrypt(byArray.length, byArray, byArray2, this.key.getNativeRef(), 1);
            }
            catch (SignatureException signatureException) {
                SignatureException signatureException2 = signatureException;
                throw signatureException;
            }
            catch (Exception exception) {
                this.inputOffset = 0;
                return false;
            }
            int n4 = n3 == this.inputOffset ? 1 : 0;
            for (n2 = 0; n2 < n3; ++n2) {
                if (this.inputBuffer[n2] == byArray2[n2]) continue;
                n4 = 0;
            }
            n2 = n4;
            return n2 != 0;
        }
        catch (Exception exception) {
            throw new SignatureException(exception);
        }
        finally {
            this.inputOffset = 0;
        }
    }
}


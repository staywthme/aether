/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.io.ByteArrayOutputStream;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.SignatureSpi;
import org.conscrypt.NativeCrypto;
import org.conscrypt.OpenSSLKey;

public class OpenSSLSignatureRawECDSA
extends SignatureSpi {
    private OpenSSLKey key;
    private ByteArrayOutputStream buffer = new ByteArrayOutputStream();

    @Override
    protected void engineUpdate(byte by) {
        this.buffer.write(by);
    }

    @Override
    protected void engineUpdate(byte[] byArray, int n2, int n3) {
        this.buffer.write(byArray, n2, n3);
    }

    @Override
    protected Object engineGetParameter(String string) throws InvalidParameterException {
        return null;
    }

    private static OpenSSLKey verifyKey(OpenSSLKey openSSLKey) throws InvalidKeyException {
        int n2 = NativeCrypto.EVP_PKEY_type(openSSLKey.getNativeRef());
        if (n2 != 408) {
            throw new InvalidKeyException("Non-EC key used to initialize EC signature.");
        }
        return openSSLKey;
    }

    @Override
    protected void engineInitSign(PrivateKey privateKey) throws InvalidKeyException {
        this.key = OpenSSLSignatureRawECDSA.verifyKey(OpenSSLKey.fromPrivateKey(privateKey));
    }

    @Override
    protected void engineInitVerify(PublicKey publicKey) throws InvalidKeyException {
        this.key = OpenSSLSignatureRawECDSA.verifyKey(OpenSSLKey.fromPublicKey(publicKey));
    }

    @Override
    protected void engineSetParameter(String string, Object object) throws InvalidParameterException {
    }

    @Override
    protected byte[] engineSign() throws SignatureException {
        if (this.key == null) {
            throw new SignatureException("No key provided");
        }
        int n2 = NativeCrypto.ECDSA_size(this.key.getNativeRef());
        byte[] byArray = new byte[n2];
        try {
            int n3 = NativeCrypto.ECDSA_sign(this.buffer.toByteArray(), byArray, this.key.getNativeRef());
            if (n3 < 0) {
                throw new SignatureException("Could not compute signature.");
            }
            if (n3 != n2) {
                byte[] byArray2 = new byte[n3];
                System.arraycopy(byArray, 0, byArray2, 0, n3);
                byArray = byArray2;
            }
            byte[] byArray3 = byArray;
            return byArray3;
        }
        catch (Exception exception) {
            throw new SignatureException(exception);
        }
        finally {
            this.buffer.reset();
        }
    }

    @Override
    protected boolean engineVerify(byte[] byArray) throws SignatureException {
        if (this.key == null) {
            throw new SignatureException("No key provided");
        }
        try {
            int bl = NativeCrypto.ECDSA_verify(this.buffer.toByteArray(), byArray, this.key.getNativeRef());
            if (bl == -1) {
                throw new SignatureException("Could not verify signature.");
            }
            boolean exception = bl == 1;
            return exception;
        }
        catch (Exception exception) {
            throw new SignatureException(exception);
        }
        finally {
            this.buffer.reset();
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.KeyAgreementSpi;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;
import org.conscrypt.NativeCrypto;
import org.conscrypt.NativeRef;
import org.conscrypt.OpenSSLKey;
import org.conscrypt.ShortBufferWithoutStackTraceException;

public final class OpenSSLECDHKeyAgreement
extends KeyAgreementSpi {
    private OpenSSLKey mOpenSslPrivateKey;
    private int mExpectedResultLength;
    private byte[] mResult;

    @Override
    public final Key engineDoPhase(Key object, boolean bl) throws InvalidKeyException {
        byte[] byArray;
        if (this.mOpenSslPrivateKey == null) {
            throw new IllegalStateException("Not initialized");
        }
        if (!bl) {
            throw new IllegalStateException("ECDH only has one phase");
        }
        if (object == null) {
            throw new InvalidKeyException("key == null");
        }
        if (!(object instanceof PublicKey)) {
            throw new InvalidKeyException("Not a public key: " + object.getClass());
        }
        byte[] byArray2 = new byte[this.mExpectedResultLength];
        int n2 = NativeCrypto.ECDH_compute_key(byArray2, 0, ((OpenSSLKey)(object = OpenSSLKey.fromPublicKey((PublicKey)object))).getNativeRef(), this.mOpenSslPrivateKey.getNativeRef());
        if (n2 == -1) {
            throw new RuntimeException("Engine returned " + n2);
        }
        if (n2 == this.mExpectedResultLength) {
            byArray = byArray2;
        } else if (n2 < this.mExpectedResultLength) {
            byArray = new byte[n2];
            System.arraycopy(byArray2, 0, this.mResult, 0, this.mResult.length);
        } else {
            throw new RuntimeException("Engine produced a longer than expected result. Expected: " + this.mExpectedResultLength + ", actual: " + n2);
        }
        this.mResult = byArray;
        return null;
    }

    @Override
    protected final int engineGenerateSecret(byte[] byArray, int n2) throws ShortBufferException {
        this.checkCompleted();
        int n3 = byArray.length - n2;
        if (this.mResult.length > n3) {
            throw new ShortBufferWithoutStackTraceException("Needed: " + this.mResult.length + ", available: " + n3);
        }
        System.arraycopy(this.mResult, 0, byArray, n2, this.mResult.length);
        return this.mResult.length;
    }

    @Override
    protected final byte[] engineGenerateSecret() {
        this.checkCompleted();
        return this.mResult;
    }

    @Override
    protected final SecretKey engineGenerateSecret(String string) {
        this.checkCompleted();
        return new SecretKeySpec(this.engineGenerateSecret(), string);
    }

    @Override
    protected final void engineInit(Key object, SecureRandom secureRandom) throws InvalidKeyException {
        if (object == null) {
            throw new InvalidKeyException("key == null");
        }
        if (!(object instanceof PrivateKey)) {
            throw new InvalidKeyException("Not a private key: " + object.getClass());
        }
        object = OpenSSLKey.fromPrivateKey((PrivateKey)object);
        int n2 = NativeCrypto.EC_GROUP_get_degree(new NativeRef.EC_GROUP(NativeCrypto.EC_KEY_get1_group(((OpenSSLKey)object).getNativeRef())));
        this.mExpectedResultLength = (n2 + 7) / 8;
        this.mOpenSslPrivateKey = object;
    }

    @Override
    protected final void engineInit(Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (algorithmParameterSpec != null) {
            throw new InvalidAlgorithmParameterException("No algorithm parameters supported");
        }
        this.engineInit(key, secureRandom);
    }

    private void checkCompleted() {
        if (this.mResult == null) {
            throw new IllegalStateException("Key agreement not completed");
        }
    }
}


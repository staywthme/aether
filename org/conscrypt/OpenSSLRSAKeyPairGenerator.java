/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGeneratorSpi;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.RSAKeyGenParameterSpec;
import org.conscrypt.NativeCrypto;
import org.conscrypt.OpenSSLKey;
import org.conscrypt.OpenSSLRSAPrivateKey;
import org.conscrypt.OpenSSLRSAPublicKey;

public final class OpenSSLRSAKeyPairGenerator
extends KeyPairGeneratorSpi {
    private byte[] publicExponent = new byte[]{1, 0, 1};
    private int modulusBits = 2048;

    @Override
    public final KeyPair generateKeyPair() {
        Object object = new OpenSSLKey(NativeCrypto.RSA_generate_key_ex(this.modulusBits, this.publicExponent));
        OpenSSLRSAPrivateKey openSSLRSAPrivateKey = OpenSSLRSAPrivateKey.getInstance((OpenSSLKey)object);
        object = new OpenSSLRSAPublicKey((OpenSSLKey)object);
        return new KeyPair((PublicKey)object, openSSLRSAPrivateKey);
    }

    @Override
    public final void initialize(int n2, SecureRandom secureRandom) {
        this.modulusBits = n2;
    }

    @Override
    public final void initialize(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom serializable) throws InvalidAlgorithmParameterException {
        if (!(algorithmParameterSpec instanceof RSAKeyGenParameterSpec)) {
            throw new InvalidAlgorithmParameterException("Only RSAKeyGenParameterSpec supported");
        }
        serializable = ((RSAKeyGenParameterSpec)(algorithmParameterSpec = (RSAKeyGenParameterSpec)algorithmParameterSpec)).getPublicExponent();
        if (serializable != null) {
            this.publicExponent = ((BigInteger)serializable).toByteArray();
        }
        this.modulusBits = ((RSAKeyGenParameterSpec)algorithmParameterSpec).getKeysize();
    }
}


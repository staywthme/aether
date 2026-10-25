/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactorySpi;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class DESEDESecretKeyFactory
extends SecretKeyFactorySpi {
    @Override
    protected SecretKey engineGenerateSecret(KeySpec keySpec) throws InvalidKeySpecException {
        if (keySpec == null) {
            throw new InvalidKeySpecException("Null KeySpec");
        }
        if (keySpec instanceof SecretKeySpec) {
            keySpec = (SecretKeySpec)keySpec;
            try {
                if (!DESedeKeySpec.isParityAdjusted(((SecretKeySpec)keySpec).getEncoded(), 0)) {
                    throw new InvalidKeySpecException("SecretKeySpec is not a parity-adjusted DESEDE key");
                }
            }
            catch (InvalidKeyException invalidKeyException) {
                throw new InvalidKeySpecException(invalidKeyException);
            }
            return keySpec;
        }
        if (keySpec instanceof DESedeKeySpec) {
            keySpec = (DESedeKeySpec)keySpec;
            return new SecretKeySpec(((DESedeKeySpec)keySpec).getKey(), "DESEDE");
        }
        throw new InvalidKeySpecException("Unsupported KeySpec class: " + keySpec.getClass().getName());
    }

    protected KeySpec engineGetKeySpec(SecretKey secretKey, Class clazz) throws InvalidKeySpecException {
        if (secretKey == null) {
            throw new InvalidKeySpecException("Null SecretKey");
        }
        if (clazz == SecretKeySpec.class) {
            try {
                if (!DESedeKeySpec.isParityAdjusted(secretKey.getEncoded(), 0)) {
                    throw new InvalidKeySpecException("SecretKey is not a parity-adjusted DESEDE key");
                }
            }
            catch (InvalidKeyException invalidKeyException) {
                throw new InvalidKeySpecException(invalidKeyException);
            }
            if (secretKey instanceof SecretKeySpec) {
                return (KeySpec)((Object)secretKey);
            }
            return new SecretKeySpec(secretKey.getEncoded(), "DESEDE");
        }
        if (clazz == DESedeKeySpec.class) {
            try {
                return new DESedeKeySpec(secretKey.getEncoded());
            }
            catch (InvalidKeyException invalidKeyException) {
                throw new InvalidKeySpecException(invalidKeyException);
            }
        }
        throw new InvalidKeySpecException("Unsupported KeySpec class: " + clazz);
    }

    @Override
    protected SecretKey engineTranslateKey(SecretKey secretKey) throws InvalidKeyException {
        if (secretKey == null) {
            throw new InvalidKeyException("Null SecretKey");
        }
        return new SecretKeySpec(secretKey.getEncoded(), secretKey.getAlgorithm());
    }
}


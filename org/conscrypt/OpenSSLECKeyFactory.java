/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactorySpi;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPrivateKeySpec;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import org.conscrypt.OpenSSLECPrivateKey;
import org.conscrypt.OpenSSLECPublicKey;
import org.conscrypt.OpenSSLKey;

public final class OpenSSLECKeyFactory
extends KeyFactorySpi {
    @Override
    protected final PublicKey engineGeneratePublic(KeySpec keySpec) throws InvalidKeySpecException {
        if (keySpec == null) {
            throw new InvalidKeySpecException("keySpec == null");
        }
        if (keySpec instanceof ECPublicKeySpec) {
            return new OpenSSLECPublicKey((ECPublicKeySpec)keySpec);
        }
        if (keySpec instanceof X509EncodedKeySpec) {
            return OpenSSLKey.getPublicKey((X509EncodedKeySpec)keySpec, 408);
        }
        throw new InvalidKeySpecException("Must use ECPublicKeySpec or X509EncodedKeySpec; was " + keySpec.getClass().getName());
    }

    @Override
    protected final PrivateKey engineGeneratePrivate(KeySpec keySpec) throws InvalidKeySpecException {
        if (keySpec == null) {
            throw new InvalidKeySpecException("keySpec == null");
        }
        if (keySpec instanceof ECPrivateKeySpec) {
            return new OpenSSLECPrivateKey((ECPrivateKeySpec)keySpec);
        }
        if (keySpec instanceof PKCS8EncodedKeySpec) {
            return OpenSSLKey.getPrivateKey((PKCS8EncodedKeySpec)keySpec, 408);
        }
        throw new InvalidKeySpecException("Must use ECPrivateKeySpec or PKCS8EncodedKeySpec; was " + keySpec.getClass().getName());
    }

    @Override
    protected final <T extends KeySpec> T engineGetKeySpec(Key object, Class<T> object2) throws InvalidKeySpecException {
        if (object == null) {
            throw new InvalidKeySpecException("key == null");
        }
        if (object2 == null) {
            throw new InvalidKeySpecException("keySpec == null");
        }
        if (!"EC".equals(object.getAlgorithm())) {
            throw new InvalidKeySpecException("Key must be an EC key");
        }
        if (object instanceof ECPublicKey && ECPublicKeySpec.class.isAssignableFrom((Class<?>)object2)) {
            object2 = (ECPublicKey)object;
            object = new ECPublicKeySpec(object2.getW(), object2.getParams());
            return (T)object;
        }
        if (object instanceof PublicKey && ECPublicKeySpec.class.isAssignableFrom((Class<?>)object2)) {
            object2 = object.getEncoded();
            if (!"X.509".equals(object.getFormat()) || object2 == null) {
                throw new InvalidKeySpecException("Not a valid X.509 encoding");
            }
            object = (ECPublicKey)this.engineGeneratePublic(new X509EncodedKeySpec((byte[])object2));
            object = new ECPublicKeySpec(object.getW(), object.getParams());
            return (T)object;
        }
        if (object instanceof ECPrivateKey && ECPrivateKeySpec.class.isAssignableFrom((Class<?>)object2)) {
            object2 = (ECPrivateKey)object;
            object = new ECPrivateKeySpec(object2.getS(), object2.getParams());
            return (T)object;
        }
        if (object instanceof PrivateKey && ECPrivateKeySpec.class.isAssignableFrom((Class<?>)object2)) {
            object2 = object.getEncoded();
            if (!"PKCS#8".equals(object.getFormat()) || object2 == null) {
                throw new InvalidKeySpecException("Not a valid PKCS#8 encoding");
            }
            object = (ECPrivateKey)this.engineGeneratePrivate(new PKCS8EncodedKeySpec((byte[])object2));
            object = new ECPrivateKeySpec(object.getS(), object.getParams());
            return (T)object;
        }
        if (object instanceof PrivateKey && PKCS8EncodedKeySpec.class.isAssignableFrom((Class<?>)object2)) {
            object2 = object.getEncoded();
            if (!"PKCS#8".equals(object.getFormat())) {
                throw new InvalidKeySpecException("Encoding type must be PKCS#8; was " + object.getFormat());
            }
            if (object2 == null) {
                throw new InvalidKeySpecException("Key is not encodable");
            }
            object = new PKCS8EncodedKeySpec((byte[])object2);
            return (T)object;
        }
        if (object instanceof PublicKey && X509EncodedKeySpec.class.isAssignableFrom((Class<?>)object2)) {
            object2 = object.getEncoded();
            if (!"X.509".equals(object.getFormat())) {
                throw new InvalidKeySpecException("Encoding type must be X.509; was " + object.getFormat());
            }
            if (object2 == null) {
                throw new InvalidKeySpecException("Key is not encodable");
            }
            object = new X509EncodedKeySpec((byte[])object2);
            return (T)object;
        }
        throw new InvalidKeySpecException("Unsupported key type and key spec combination; key=" + object.getClass().getName() + ", keySpec=" + ((Class)object2).getName());
    }

    @Override
    protected final Key engineTranslateKey(Key object) throws InvalidKeyException {
        if (object == null) {
            throw new InvalidKeyException("key == null");
        }
        if (object instanceof OpenSSLECPublicKey || object instanceof OpenSSLECPrivateKey) {
            return object;
        }
        if (object instanceof ECPublicKey) {
            object = (ECPublicKey)object;
            ECPoint eCPoint = object.getW();
            object = object.getParams();
            try {
                return this.engineGeneratePublic(new ECPublicKeySpec(eCPoint, (ECParameterSpec)object));
            }
            catch (InvalidKeySpecException invalidKeySpecException) {
                throw new InvalidKeyException(invalidKeySpecException);
            }
        }
        if (object instanceof ECPrivateKey) {
            object = (ECPrivateKey)object;
            BigInteger bigInteger = object.getS();
            object = object.getParams();
            try {
                return this.engineGeneratePrivate(new ECPrivateKeySpec(bigInteger, (ECParameterSpec)object));
            }
            catch (InvalidKeySpecException invalidKeySpecException) {
                throw new InvalidKeyException(invalidKeySpecException);
            }
        }
        if (object instanceof PrivateKey && "PKCS#8".equals(object.getFormat())) {
            byte[] byArray = object.getEncoded();
            object = byArray;
            if (byArray == null) {
                throw new InvalidKeyException("Key does not support encoding");
            }
            try {
                return this.engineGeneratePrivate(new PKCS8EncodedKeySpec((byte[])object));
            }
            catch (InvalidKeySpecException invalidKeySpecException) {
                throw new InvalidKeyException(invalidKeySpecException);
            }
        }
        if (object instanceof PublicKey && "X.509".equals(object.getFormat())) {
            byte[] byArray = object.getEncoded();
            object = byArray;
            if (byArray == null) {
                throw new InvalidKeyException("Key does not support encoding");
            }
            try {
                return this.engineGeneratePublic(new X509EncodedKeySpec((byte[])object));
            }
            catch (InvalidKeySpecException invalidKeySpecException) {
                throw new InvalidKeyException(invalidKeySpecException);
            }
        }
        throw new InvalidKeyException("Key must be EC public or private key; was " + object.getClass().getName());
    }
}


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
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import org.conscrypt.OpenSSLKey;
import org.conscrypt.OpenSSLRSAPrivateCrtKey;
import org.conscrypt.OpenSSLRSAPrivateKey;
import org.conscrypt.OpenSSLRSAPublicKey;

public final class OpenSSLRSAKeyFactory
extends KeyFactorySpi {
    @Override
    protected final PublicKey engineGeneratePublic(KeySpec keySpec) throws InvalidKeySpecException {
        if (keySpec == null) {
            throw new InvalidKeySpecException("keySpec == null");
        }
        if (keySpec instanceof RSAPublicKeySpec) {
            return new OpenSSLRSAPublicKey((RSAPublicKeySpec)keySpec);
        }
        if (keySpec instanceof X509EncodedKeySpec) {
            return OpenSSLKey.getPublicKey((X509EncodedKeySpec)keySpec, 6);
        }
        throw new InvalidKeySpecException("Must use RSAPublicKeySpec or X509EncodedKeySpec; was " + keySpec.getClass().getName());
    }

    @Override
    protected final PrivateKey engineGeneratePrivate(KeySpec keySpec) throws InvalidKeySpecException {
        if (keySpec == null) {
            throw new InvalidKeySpecException("keySpec == null");
        }
        if (keySpec instanceof RSAPrivateCrtKeySpec) {
            return new OpenSSLRSAPrivateCrtKey((RSAPrivateCrtKeySpec)keySpec);
        }
        if (keySpec instanceof RSAPrivateKeySpec) {
            return new OpenSSLRSAPrivateKey((RSAPrivateKeySpec)keySpec);
        }
        if (keySpec instanceof PKCS8EncodedKeySpec) {
            return OpenSSLKey.getPrivateKey((PKCS8EncodedKeySpec)keySpec, 6);
        }
        throw new InvalidKeySpecException("Must use RSAPublicKeySpec or PKCS8EncodedKeySpec; was " + keySpec.getClass().getName());
    }

    @Override
    protected final <T extends KeySpec> T engineGetKeySpec(Key object, Class<T> object2) throws InvalidKeySpecException {
        if (object == null) {
            throw new InvalidKeySpecException("key == null");
        }
        if (object2 == null) {
            throw new InvalidKeySpecException("keySpec == null");
        }
        if (!"RSA".equals(object.getAlgorithm())) {
            throw new InvalidKeySpecException("Key must be a RSA key");
        }
        if (object instanceof RSAPublicKey && RSAPublicKeySpec.class.isAssignableFrom((Class<?>)object2)) {
            object2 = (RSAPublicKey)object;
            object = new RSAPublicKeySpec(object2.getModulus(), object2.getPublicExponent());
            return (T)object;
        }
        if (object instanceof PublicKey && RSAPublicKeySpec.class.isAssignableFrom((Class<?>)object2)) {
            object2 = object.getEncoded();
            if (!"X.509".equals(object.getFormat()) || object2 == null) {
                throw new InvalidKeySpecException("Not a valid X.509 encoding");
            }
            object = (RSAPublicKey)this.engineGeneratePublic(new X509EncodedKeySpec((byte[])object2));
            object = new RSAPublicKeySpec(object.getModulus(), object.getPublicExponent());
            return (T)object;
        }
        if (object instanceof RSAPrivateCrtKey && RSAPrivateCrtKeySpec.class.isAssignableFrom((Class<?>)object2)) {
            object2 = (RSAPrivateCrtKey)object;
            object = new RSAPrivateCrtKeySpec(object2.getModulus(), object2.getPublicExponent(), object2.getPrivateExponent(), object2.getPrimeP(), object2.getPrimeQ(), object2.getPrimeExponentP(), object2.getPrimeExponentQ(), object2.getCrtCoefficient());
            return (T)object;
        }
        if (object instanceof RSAPrivateCrtKey && RSAPrivateKeySpec.class.isAssignableFrom((Class<?>)object2)) {
            object2 = (RSAPrivateCrtKey)object;
            object = new RSAPrivateKeySpec(object2.getModulus(), object2.getPrivateExponent());
            return (T)object;
        }
        if (object instanceof RSAPrivateKey && RSAPrivateKeySpec.class.isAssignableFrom((Class<?>)object2)) {
            object2 = (RSAPrivateKey)object;
            object = new RSAPrivateKeySpec(object2.getModulus(), object2.getPrivateExponent());
            return (T)object;
        }
        if (object instanceof PrivateKey && RSAPrivateCrtKeySpec.class.isAssignableFrom((Class<?>)object2)) {
            object2 = object.getEncoded();
            if (!"PKCS#8".equals(object.getFormat()) || object2 == null) {
                throw new InvalidKeySpecException("Not a valid PKCS#8 encoding");
            }
            object = (RSAPrivateKey)this.engineGeneratePrivate(new PKCS8EncodedKeySpec((byte[])object2));
            if (object instanceof RSAPrivateCrtKey) {
                object = (RSAPrivateCrtKey)object;
                object = new RSAPrivateCrtKeySpec(object.getModulus(), object.getPublicExponent(), object.getPrivateExponent(), object.getPrimeP(), object.getPrimeQ(), object.getPrimeExponentP(), object.getPrimeExponentQ(), object.getCrtCoefficient());
                return (T)object;
            }
            throw new InvalidKeySpecException("Encoded key is not an RSAPrivateCrtKey");
        }
        if (object instanceof PrivateKey && RSAPrivateKeySpec.class.isAssignableFrom((Class<?>)object2)) {
            object2 = object.getEncoded();
            if (!"PKCS#8".equals(object.getFormat()) || object2 == null) {
                throw new InvalidKeySpecException("Not a valid PKCS#8 encoding");
            }
            object = (RSAPrivateKey)this.engineGeneratePrivate(new PKCS8EncodedKeySpec((byte[])object2));
            object = new RSAPrivateKeySpec(object.getModulus(), object.getPrivateExponent());
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
        if (object instanceof OpenSSLRSAPublicKey || object instanceof OpenSSLRSAPrivateKey) {
            return object;
        }
        if (object instanceof RSAPublicKey) {
            object = (RSAPublicKey)object;
            try {
                return this.engineGeneratePublic(new RSAPublicKeySpec(object.getModulus(), object.getPublicExponent()));
            }
            catch (InvalidKeySpecException invalidKeySpecException) {
                throw new InvalidKeyException(invalidKeySpecException);
            }
        }
        if (object instanceof RSAPrivateCrtKey) {
            object = (RSAPrivateCrtKey)object;
            BigInteger bigInteger = object.getModulus();
            BigInteger bigInteger2 = object.getPublicExponent();
            BigInteger bigInteger3 = object.getPrivateExponent();
            BigInteger bigInteger4 = object.getPrimeP();
            BigInteger bigInteger5 = object.getPrimeQ();
            BigInteger bigInteger6 = object.getPrimeExponentP();
            BigInteger bigInteger7 = object.getPrimeExponentQ();
            object = object.getCrtCoefficient();
            try {
                return this.engineGeneratePrivate(new RSAPrivateCrtKeySpec(bigInteger, bigInteger2, bigInteger3, bigInteger4, bigInteger5, bigInteger6, bigInteger7, (BigInteger)object));
            }
            catch (InvalidKeySpecException invalidKeySpecException) {
                throw new InvalidKeyException(invalidKeySpecException);
            }
        }
        if (object instanceof RSAPrivateKey) {
            object = (RSAPrivateKey)object;
            BigInteger bigInteger = object.getModulus();
            BigInteger bigInteger8 = object.getPrivateExponent();
            try {
                return this.engineGeneratePrivate(new RSAPrivateKeySpec(bigInteger, bigInteger8));
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
        throw new InvalidKeyException("Key must be an RSA public or private key; was " + object.getClass().getName());
    }
}


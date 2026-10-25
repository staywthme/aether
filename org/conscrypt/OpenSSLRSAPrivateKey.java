/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import org.conscrypt.NativeCrypto;
import org.conscrypt.OpenSSLKey;
import org.conscrypt.OpenSSLKeyHolder;
import org.conscrypt.OpenSSLRSAPrivateCrtKey;
import org.conscrypt.Platform;

class OpenSSLRSAPrivateKey
implements RSAPrivateKey,
OpenSSLKeyHolder {
    private static final long serialVersionUID = 4872170254439578735L;
    transient OpenSSLKey key;
    transient boolean fetchedParams;
    BigInteger modulus;
    BigInteger privateExponent;

    OpenSSLRSAPrivateKey(OpenSSLKey openSSLKey) {
        this.key = openSSLKey;
    }

    OpenSSLRSAPrivateKey(OpenSSLKey openSSLKey, byte[][] byArray) {
        this(openSSLKey);
        this.readParams(byArray);
        this.fetchedParams = true;
    }

    @Override
    public OpenSSLKey getOpenSSLKey() {
        return this.key;
    }

    public OpenSSLRSAPrivateKey(RSAPrivateKeySpec rSAPrivateKeySpec) throws InvalidKeySpecException {
        this(OpenSSLRSAPrivateKey.init(rSAPrivateKeySpec));
    }

    private static OpenSSLKey init(RSAPrivateKeySpec object) throws InvalidKeySpecException {
        BigInteger bigInteger = ((RSAPrivateKeySpec)object).getModulus();
        object = ((RSAPrivateKeySpec)object).getPrivateExponent();
        if (bigInteger == null) {
            throw new InvalidKeySpecException("modulus == null");
        }
        if (object == null) {
            throw new InvalidKeySpecException("privateExponent == null");
        }
        try {
            return new OpenSSLKey(NativeCrypto.EVP_PKEY_new_RSA(bigInteger.toByteArray(), null, ((BigInteger)object).toByteArray(), null, null, null, null, null));
        }
        catch (Exception exception) {
            throw new InvalidKeySpecException(exception);
        }
    }

    static OpenSSLRSAPrivateKey getInstance(OpenSSLKey openSSLKey) {
        byte[][] byArray = NativeCrypto.get_RSA_private_params(openSSLKey.getNativeRef());
        if (byArray[1] != null) {
            return new OpenSSLRSAPrivateCrtKey(openSSLKey, byArray);
        }
        return new OpenSSLRSAPrivateKey(openSSLKey, byArray);
    }

    static OpenSSLKey wrapPlatformKey(RSAPrivateKey rSAPrivateKey) throws InvalidKeyException {
        OpenSSLKey openSSLKey = Platform.wrapRsaKey(rSAPrivateKey);
        if (openSSLKey != null) {
            return openSSLKey;
        }
        RSAPrivateKey rSAPrivateKey2 = rSAPrivateKey;
        return new OpenSSLKey(NativeCrypto.getRSAPrivateKeyWrapper(rSAPrivateKey2, rSAPrivateKey2.getModulus().toByteArray()), true);
    }

    static OpenSSLKey wrapJCAPrivateKeyForTLSStackOnly(PrivateKey privateKey, PublicKey publicKey) throws InvalidKeyException {
        BigInteger bigInteger = null;
        if (privateKey instanceof RSAKey) {
            bigInteger = ((RSAKey)((Object)privateKey)).getModulus();
        } else if (publicKey instanceof RSAKey) {
            bigInteger = ((RSAKey)((Object)publicKey)).getModulus();
        }
        if (bigInteger == null) {
            throw new InvalidKeyException("RSA modulus not available. Private: " + privateKey + ", public: " + publicKey);
        }
        return new OpenSSLKey(NativeCrypto.getRSAPrivateKeyWrapper(privateKey, bigInteger.toByteArray()), true);
    }

    static OpenSSLKey getInstance(RSAPrivateKey serializable) throws InvalidKeyException {
        if (serializable.getFormat() == null) {
            return OpenSSLRSAPrivateKey.wrapPlatformKey((RSAPrivateKey)serializable);
        }
        BigInteger bigInteger = serializable.getModulus();
        serializable = serializable.getPrivateExponent();
        if (bigInteger == null) {
            throw new InvalidKeyException("modulus == null");
        }
        if (serializable == null) {
            throw new InvalidKeyException("privateExponent == null");
        }
        try {
            return new OpenSSLKey(NativeCrypto.EVP_PKEY_new_RSA(bigInteger.toByteArray(), null, ((BigInteger)serializable).toByteArray(), null, null, null, null, null));
        }
        catch (Exception exception) {
            throw new InvalidKeyException(exception);
        }
    }

    final synchronized void ensureReadParams() {
        if (this.fetchedParams) {
            return;
        }
        OpenSSLRSAPrivateKey openSSLRSAPrivateKey = this;
        openSSLRSAPrivateKey.readParams(NativeCrypto.get_RSA_private_params(openSSLRSAPrivateKey.key.getNativeRef()));
        this.fetchedParams = true;
    }

    void readParams(byte[][] byArray) {
        if (byArray[0] == null) {
            throw new NullPointerException("modulus == null");
        }
        if (byArray[2] == null && !this.key.isHardwareBacked()) {
            throw new NullPointerException("privateExponent == null");
        }
        this.modulus = new BigInteger(byArray[0]);
        if (byArray[2] != null) {
            this.privateExponent = new BigInteger(byArray[2]);
        }
    }

    @Override
    public final BigInteger getPrivateExponent() {
        if (this.key.isHardwareBacked()) {
            throw new UnsupportedOperationException("Private exponent cannot be extracted");
        }
        this.ensureReadParams();
        return this.privateExponent;
    }

    @Override
    public final BigInteger getModulus() {
        this.ensureReadParams();
        return this.modulus;
    }

    @Override
    public final byte[] getEncoded() {
        if (this.key.isHardwareBacked()) {
            return null;
        }
        return NativeCrypto.EVP_marshal_private_key(this.key.getNativeRef());
    }

    @Override
    public final String getFormat() {
        if (this.key.isHardwareBacked()) {
            return null;
        }
        return "PKCS#8";
    }

    @Override
    public final String getAlgorithm() {
        return "RSA";
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof OpenSSLRSAPrivateKey) {
            object = (OpenSSLRSAPrivateKey)object;
            return this.key.equals(((OpenSSLRSAPrivateKey)object).getOpenSSLKey());
        }
        if (object instanceof RSAPrivateKey) {
            this.ensureReadParams();
            object = (RSAPrivateKey)object;
            return this.modulus.equals(object.getModulus()) && this.privateExponent.equals(object.getPrivateExponent());
        }
        return false;
    }

    public int hashCode() {
        this.ensureReadParams();
        int n2 = 3 + this.modulus.hashCode();
        if (this.privateExponent != null) {
            n2 = n2 * 7 + this.privateExponent.hashCode();
        }
        return n2;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("OpenSSLRSAPrivateKey{");
        this.ensureReadParams();
        stringBuilder.append("modulus=");
        stringBuilder.append(this.modulus.toString(16));
        return stringBuilder.toString();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.key = new OpenSSLKey(NativeCrypto.EVP_PKEY_new_RSA(this.modulus.toByteArray(), null, this.privateExponent.toByteArray(), null, null, null, null, null));
        this.fetchedParams = true;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (this.key.isHardwareBacked()) {
            throw new NotSerializableException("Hardware backed keys can not be serialized");
        }
        this.ensureReadParams();
        objectOutputStream.defaultWriteObject();
    }
}


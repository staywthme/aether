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
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import org.conscrypt.NativeCrypto;
import org.conscrypt.OpenSSLKey;
import org.conscrypt.OpenSSLRSAPrivateKey;

final class OpenSSLRSAPrivateCrtKey
extends OpenSSLRSAPrivateKey
implements RSAPrivateCrtKey {
    private static final long serialVersionUID = 3785291944868707197L;
    private BigInteger publicExponent;
    private BigInteger primeP;
    private BigInteger primeQ;
    private BigInteger primeExponentP;
    private BigInteger primeExponentQ;
    private BigInteger crtCoefficient;

    OpenSSLRSAPrivateCrtKey(OpenSSLKey openSSLKey) {
        super(openSSLKey);
    }

    OpenSSLRSAPrivateCrtKey(OpenSSLKey openSSLKey, byte[][] byArray) {
        super(openSSLKey, byArray);
    }

    OpenSSLRSAPrivateCrtKey(RSAPrivateCrtKeySpec rSAPrivateCrtKeySpec) throws InvalidKeySpecException {
        super(OpenSSLRSAPrivateCrtKey.init(rSAPrivateCrtKeySpec));
    }

    private static OpenSSLKey init(RSAPrivateCrtKeySpec object) throws InvalidKeySpecException {
        BigInteger bigInteger = ((RSAPrivateKeySpec)object).getModulus();
        BigInteger bigInteger2 = ((RSAPrivateKeySpec)object).getPrivateExponent();
        if (bigInteger == null) {
            throw new InvalidKeySpecException("modulus == null");
        }
        if (bigInteger2 == null) {
            throw new InvalidKeySpecException("privateExponent == null");
        }
        try {
            BigInteger bigInteger3 = ((RSAPrivateCrtKeySpec)object).getPublicExponent();
            BigInteger bigInteger4 = ((RSAPrivateCrtKeySpec)object).getPrimeP();
            BigInteger bigInteger5 = ((RSAPrivateCrtKeySpec)object).getPrimeQ();
            BigInteger bigInteger6 = ((RSAPrivateCrtKeySpec)object).getPrimeExponentP();
            BigInteger bigInteger7 = ((RSAPrivateCrtKeySpec)object).getPrimeExponentQ();
            object = ((RSAPrivateCrtKeySpec)object).getCrtCoefficient();
            return new OpenSSLKey(NativeCrypto.EVP_PKEY_new_RSA(bigInteger.toByteArray(), bigInteger3 == null ? null : bigInteger3.toByteArray(), bigInteger2.toByteArray(), bigInteger4 == null ? null : bigInteger4.toByteArray(), bigInteger5 == null ? null : bigInteger5.toByteArray(), bigInteger6 == null ? null : bigInteger6.toByteArray(), bigInteger7 == null ? null : bigInteger7.toByteArray(), object == null ? null : ((BigInteger)object).toByteArray()));
        }
        catch (Exception exception) {
            throw new InvalidKeySpecException(exception);
        }
    }

    static OpenSSLKey getInstance(RSAPrivateCrtKey serializable) throws InvalidKeyException {
        if (serializable.getFormat() == null) {
            return OpenSSLRSAPrivateCrtKey.wrapPlatformKey((RSAPrivateKey)serializable);
        }
        BigInteger bigInteger = serializable.getModulus();
        BigInteger bigInteger2 = serializable.getPrivateExponent();
        if (bigInteger == null) {
            throw new InvalidKeyException("modulus == null");
        }
        if (bigInteger2 == null) {
            throw new InvalidKeyException("privateExponent == null");
        }
        try {
            BigInteger bigInteger3 = serializable.getPublicExponent();
            BigInteger bigInteger4 = serializable.getPrimeP();
            BigInteger bigInteger5 = serializable.getPrimeQ();
            BigInteger bigInteger6 = serializable.getPrimeExponentP();
            BigInteger bigInteger7 = serializable.getPrimeExponentQ();
            serializable = serializable.getCrtCoefficient();
            return new OpenSSLKey(NativeCrypto.EVP_PKEY_new_RSA(bigInteger.toByteArray(), bigInteger3 == null ? null : bigInteger3.toByteArray(), bigInteger2.toByteArray(), bigInteger4 == null ? null : bigInteger4.toByteArray(), bigInteger5 == null ? null : bigInteger5.toByteArray(), bigInteger6 == null ? null : bigInteger6.toByteArray(), bigInteger7 == null ? null : bigInteger7.toByteArray(), serializable == null ? null : ((BigInteger)serializable).toByteArray()));
        }
        catch (Exception exception) {
            throw new InvalidKeyException(exception);
        }
    }

    @Override
    final synchronized void readParams(byte[][] byArray) {
        super.readParams(byArray);
        if (byArray[1] != null) {
            this.publicExponent = new BigInteger(byArray[1]);
        }
        if (byArray[3] != null) {
            this.primeP = new BigInteger(byArray[3]);
        }
        if (byArray[4] != null) {
            this.primeQ = new BigInteger(byArray[4]);
        }
        if (byArray[5] != null) {
            this.primeExponentP = new BigInteger(byArray[5]);
        }
        if (byArray[6] != null) {
            this.primeExponentQ = new BigInteger(byArray[6]);
        }
        if (byArray[7] != null) {
            this.crtCoefficient = new BigInteger(byArray[7]);
        }
    }

    @Override
    public final BigInteger getPublicExponent() {
        this.ensureReadParams();
        return this.publicExponent;
    }

    @Override
    public final BigInteger getPrimeP() {
        this.ensureReadParams();
        return this.primeP;
    }

    @Override
    public final BigInteger getPrimeQ() {
        this.ensureReadParams();
        return this.primeQ;
    }

    @Override
    public final BigInteger getPrimeExponentP() {
        this.ensureReadParams();
        return this.primeExponentP;
    }

    @Override
    public final BigInteger getPrimeExponentQ() {
        this.ensureReadParams();
        return this.primeExponentQ;
    }

    @Override
    public final BigInteger getCrtCoefficient() {
        this.ensureReadParams();
        return this.crtCoefficient;
    }

    @Override
    public final boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof OpenSSLRSAPrivateKey) {
            object = (OpenSSLRSAPrivateKey)object;
            return this.getOpenSSLKey().equals(((OpenSSLRSAPrivateKey)object).getOpenSSLKey());
        }
        if (object instanceof RSAPrivateCrtKey) {
            this.ensureReadParams();
            object = (RSAPrivateCrtKey)object;
            if (this.getOpenSSLKey().isHardwareBacked()) {
                return this.getModulus().equals(object.getModulus()) && this.publicExponent.equals(object.getPublicExponent());
            }
            return this.getModulus().equals(object.getModulus()) && this.publicExponent.equals(object.getPublicExponent()) && this.getPrivateExponent().equals(object.getPrivateExponent()) && this.primeP.equals(object.getPrimeP()) && this.primeQ.equals(object.getPrimeQ()) && this.primeExponentP.equals(object.getPrimeExponentP()) && this.primeExponentQ.equals(object.getPrimeExponentQ()) && this.crtCoefficient.equals(object.getCrtCoefficient());
        }
        if (object instanceof RSAPrivateKey) {
            this.ensureReadParams();
            object = (RSAPrivateKey)object;
            if (this.getOpenSSLKey().isHardwareBacked()) {
                return this.getModulus().equals(object.getModulus());
            }
            return this.getModulus().equals(object.getModulus()) && this.getPrivateExponent().equals(object.getPrivateExponent());
        }
        return false;
    }

    @Override
    public final int hashCode() {
        int n2 = super.hashCode();
        if (this.publicExponent != null) {
            n2 ^= this.publicExponent.hashCode();
        }
        return n2;
    }

    @Override
    public final String toString() {
        StringBuilder stringBuilder = new StringBuilder("OpenSSLRSAPrivateCrtKey{");
        this.ensureReadParams();
        stringBuilder.append("modulus=");
        stringBuilder.append(this.getModulus().toString(16));
        if (this.publicExponent != null) {
            stringBuilder.append(',');
            stringBuilder.append("publicExponent=");
            stringBuilder.append(this.publicExponent.toString(16));
        }
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.key = new OpenSSLKey(NativeCrypto.EVP_PKEY_new_RSA(this.modulus.toByteArray(), this.publicExponent == null ? null : this.publicExponent.toByteArray(), this.privateExponent.toByteArray(), this.primeP == null ? null : this.primeP.toByteArray(), this.primeQ == null ? null : this.primeQ.toByteArray(), this.primeExponentP == null ? null : this.primeExponentP.toByteArray(), this.primeExponentQ == null ? null : this.primeExponentQ.toByteArray(), this.crtCoefficient == null ? null : this.crtCoefficient.toByteArray()));
        this.fetchedParams = true;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (this.getOpenSSLKey().isHardwareBacked()) {
            throw new NotSerializableException("Hardware backed keys cannot be serialized");
        }
        this.ensureReadParams();
        objectOutputStream.defaultWriteObject();
    }
}


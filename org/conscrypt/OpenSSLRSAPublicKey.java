/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import org.conscrypt.NativeCrypto;
import org.conscrypt.OpenSSLKey;
import org.conscrypt.OpenSSLKeyHolder;

public class OpenSSLRSAPublicKey
implements RSAPublicKey,
OpenSSLKeyHolder {
    private static final long serialVersionUID = 123125005824688292L;
    private transient OpenSSLKey key;
    private BigInteger publicExponent;
    private BigInteger modulus;
    private transient boolean fetchedParams;

    OpenSSLRSAPublicKey(OpenSSLKey openSSLKey) {
        this.key = openSSLKey;
    }

    @Override
    public OpenSSLKey getOpenSSLKey() {
        return this.key;
    }

    OpenSSLRSAPublicKey(RSAPublicKeySpec rSAPublicKeySpec) throws InvalidKeySpecException {
        try {
            this.key = new OpenSSLKey(NativeCrypto.EVP_PKEY_new_RSA(rSAPublicKeySpec.getModulus().toByteArray(), rSAPublicKeySpec.getPublicExponent().toByteArray(), null, null, null, null, null, null));
            return;
        }
        catch (Exception exception) {
            throw new InvalidKeySpecException(exception);
        }
    }

    static OpenSSLKey getInstance(RSAPublicKey rSAPublicKey) throws InvalidKeyException {
        try {
            return new OpenSSLKey(NativeCrypto.EVP_PKEY_new_RSA(rSAPublicKey.getModulus().toByteArray(), rSAPublicKey.getPublicExponent().toByteArray(), null, null, null, null, null, null));
        }
        catch (Exception exception) {
            throw new InvalidKeyException(exception);
        }
    }

    @Override
    public String getAlgorithm() {
        return "RSA";
    }

    @Override
    public String getFormat() {
        return "X.509";
    }

    @Override
    public byte[] getEncoded() {
        return NativeCrypto.EVP_marshal_public_key(this.key.getNativeRef());
    }

    private synchronized void ensureReadParams() {
        if (this.fetchedParams) {
            return;
        }
        byte[][] byArray = NativeCrypto.get_RSA_public_params(this.key.getNativeRef());
        this.modulus = new BigInteger(byArray[0]);
        this.publicExponent = new BigInteger(byArray[1]);
        this.fetchedParams = true;
    }

    @Override
    public BigInteger getModulus() {
        this.ensureReadParams();
        return this.modulus;
    }

    @Override
    public BigInteger getPublicExponent() {
        this.ensureReadParams();
        return this.publicExponent;
    }

    public boolean equals(Object object) {
        RSAPublicKey rSAPublicKey;
        if (object == this) {
            return true;
        }
        if (object instanceof OpenSSLRSAPublicKey && this.key.equals(((OpenSSLRSAPublicKey)(rSAPublicKey = (OpenSSLRSAPublicKey)object)).getOpenSSLKey())) {
            return true;
        }
        if (!(object instanceof RSAPublicKey)) {
            return false;
        }
        this.ensureReadParams();
        rSAPublicKey = (RSAPublicKey)object;
        return this.modulus.equals(rSAPublicKey.getModulus()) && this.publicExponent.equals(rSAPublicKey.getPublicExponent());
    }

    public int hashCode() {
        this.ensureReadParams();
        return this.modulus.hashCode() ^ this.publicExponent.hashCode();
    }

    public String toString() {
        this.ensureReadParams();
        StringBuilder stringBuilder = new StringBuilder("OpenSSLRSAPublicKey{");
        stringBuilder.append("modulus=");
        stringBuilder.append(this.modulus.toString(16));
        stringBuilder.append(',');
        stringBuilder.append("publicExponent=");
        stringBuilder.append(this.publicExponent.toString(16));
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.key = new OpenSSLKey(NativeCrypto.EVP_PKEY_new_RSA(this.modulus.toByteArray(), this.publicExponent.toByteArray(), null, null, null, null, null, null));
        this.fetchedParams = true;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        this.ensureReadParams();
        objectOutputStream.defaultWriteObject();
    }
}


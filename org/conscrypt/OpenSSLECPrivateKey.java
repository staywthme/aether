/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.ECKey;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPrivateKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import org.conscrypt.NativeCrypto;
import org.conscrypt.NativeRef;
import org.conscrypt.OpenSSLECGroupContext;
import org.conscrypt.OpenSSLKey;
import org.conscrypt.OpenSSLKeyHolder;
import org.conscrypt.OpenSSLX509CertificateFactory;

final class OpenSSLECPrivateKey
implements ECPrivateKey,
OpenSSLKeyHolder {
    private static final long serialVersionUID = -4036633595001083922L;
    private static final String ALGORITHM = "EC";
    protected transient OpenSSLKey key;
    protected transient OpenSSLECGroupContext group;

    OpenSSLECPrivateKey(OpenSSLECGroupContext openSSLECGroupContext, OpenSSLKey openSSLKey) {
        this.group = openSSLECGroupContext;
        this.key = openSSLKey;
    }

    OpenSSLECPrivateKey(OpenSSLKey openSSLKey) {
        this.group = new OpenSSLECGroupContext(new NativeRef.EC_GROUP(NativeCrypto.EC_KEY_get1_group(openSSLKey.getNativeRef())));
        this.key = openSSLKey;
    }

    OpenSSLECPrivateKey(ECPrivateKeySpec object) throws InvalidKeySpecException {
        try {
            this.group = OpenSSLECGroupContext.getInstance(((ECPrivateKeySpec)object).getParams());
            object = ((ECPrivateKeySpec)object).getS();
            this.key = new OpenSSLKey(NativeCrypto.EVP_PKEY_new_EC_KEY(this.group.getNativeRef(), null, ((BigInteger)object).toByteArray()));
            return;
        }
        catch (Exception exception) {
            throw new InvalidKeySpecException(exception);
        }
    }

    static OpenSSLKey wrapPlatformKey(ECPrivateKey eCPrivateKey) throws InvalidKeyException {
        OpenSSLECGroupContext openSSLECGroupContext;
        try {
            openSSLECGroupContext = OpenSSLECGroupContext.getInstance(eCPrivateKey.getParams());
        }
        catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException) {
            throw new InvalidKeyException("Unknown group parameters", invalidAlgorithmParameterException);
        }
        return OpenSSLECPrivateKey.wrapPlatformKey(eCPrivateKey, openSSLECGroupContext);
    }

    static OpenSSLKey wrapJCAPrivateKeyForTLSStackOnly(PrivateKey privateKey, PublicKey publicKey) throws InvalidKeyException {
        ECParameterSpec eCParameterSpec = null;
        if (privateKey instanceof ECKey) {
            eCParameterSpec = ((ECKey)((Object)privateKey)).getParams();
        } else if (publicKey instanceof ECKey) {
            eCParameterSpec = ((ECKey)((Object)publicKey)).getParams();
        }
        if (eCParameterSpec == null) {
            throw new InvalidKeyException("EC parameters not available. Private: " + privateKey + ", public: " + publicKey);
        }
        return OpenSSLECPrivateKey.wrapJCAPrivateKeyForTLSStackOnly(privateKey, eCParameterSpec);
    }

    static OpenSSLKey wrapJCAPrivateKeyForTLSStackOnly(PrivateKey privateKey, ECParameterSpec eCParameterSpec) throws InvalidKeyException {
        OpenSSLECGroupContext openSSLECGroupContext;
        if (eCParameterSpec == null && privateKey instanceof ECKey) {
            eCParameterSpec = ((ECKey)((Object)privateKey)).getParams();
        }
        if (eCParameterSpec == null) {
            throw new InvalidKeyException("EC parameters not available: " + privateKey);
        }
        try {
            openSSLECGroupContext = OpenSSLECGroupContext.getInstance(eCParameterSpec);
        }
        catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException) {
            throw new InvalidKeyException("Invalid EC parameters: " + eCParameterSpec);
        }
        return new OpenSSLKey(NativeCrypto.getECPrivateKeyWrapper(privateKey, openSSLECGroupContext.getNativeRef()), true);
    }

    private static OpenSSLKey wrapPlatformKey(ECPrivateKey eCPrivateKey, OpenSSLECGroupContext openSSLECGroupContext) throws InvalidKeyException {
        return new OpenSSLKey(NativeCrypto.getECPrivateKeyWrapper(eCPrivateKey, openSSLECGroupContext.getNativeRef()), true);
    }

    static OpenSSLKey getInstance(ECPrivateKey serializable) throws InvalidKeyException {
        try {
            OpenSSLECGroupContext openSSLECGroupContext = OpenSSLECGroupContext.getInstance(serializable.getParams());
            if (serializable.getFormat() == null) {
                return OpenSSLECPrivateKey.wrapPlatformKey((ECPrivateKey)serializable, openSSLECGroupContext);
            }
            serializable = serializable.getS();
            return new OpenSSLKey(NativeCrypto.EVP_PKEY_new_EC_KEY(openSSLECGroupContext.getNativeRef(), null, ((BigInteger)serializable).toByteArray()));
        }
        catch (Exception exception) {
            throw new InvalidKeyException(exception);
        }
    }

    @Override
    public final String getAlgorithm() {
        return ALGORITHM;
    }

    @Override
    public final String getFormat() {
        if (this.key.isHardwareBacked()) {
            return null;
        }
        return "PKCS#8";
    }

    @Override
    public final byte[] getEncoded() {
        if (this.key.isHardwareBacked()) {
            return null;
        }
        return NativeCrypto.EVP_marshal_private_key(this.key.getNativeRef());
    }

    @Override
    public final ECParameterSpec getParams() {
        return this.group.getECParameterSpec();
    }

    @Override
    public final BigInteger getS() {
        if (this.key.isHardwareBacked()) {
            throw new UnsupportedOperationException("Private key value S cannot be extracted");
        }
        return this.getPrivateKey();
    }

    private BigInteger getPrivateKey() {
        return new BigInteger(NativeCrypto.EC_KEY_get_private_key(this.key.getNativeRef()));
    }

    @Override
    public final OpenSSLKey getOpenSSLKey() {
        return this.key;
    }

    public final boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof OpenSSLECPrivateKey) {
            object = (OpenSSLECPrivateKey)object;
            return this.key.equals(((OpenSSLECPrivateKey)object).key);
        }
        if (!(object instanceof ECPrivateKey)) {
            return false;
        }
        object = (ECPrivateKey)object;
        if (!this.getPrivateKey().equals(object.getS())) {
            return false;
        }
        ECParameterSpec eCParameterSpec = this.getParams();
        object = object.getParams();
        return eCParameterSpec.getCurve().equals(((ECParameterSpec)object).getCurve()) && eCParameterSpec.getGenerator().equals(((ECParameterSpec)object).getGenerator()) && eCParameterSpec.getOrder().equals(((ECParameterSpec)object).getOrder()) && eCParameterSpec.getCofactor() == ((ECParameterSpec)object).getCofactor();
    }

    public final int hashCode() {
        return Arrays.hashCode(NativeCrypto.EVP_marshal_private_key(this.key.getNativeRef()));
    }

    public final String toString() {
        StringBuilder stringBuilder = new StringBuilder("OpenSSLECPrivateKey{");
        stringBuilder.append("params={");
        stringBuilder.append(NativeCrypto.EVP_PKEY_print_params(this.key.getNativeRef()));
        stringBuilder.append("}}");
        return stringBuilder.toString();
    }

    private void readObject(ObjectInputStream object) throws IOException, ClassNotFoundException {
        ((ObjectInputStream)object).defaultReadObject();
        object = (byte[])((ObjectInputStream)object).readObject();
        try {
            this.key = new OpenSSLKey(NativeCrypto.EVP_parse_private_key((byte[])object));
        }
        catch (OpenSSLX509CertificateFactory.ParsingException parsingException) {
            throw new IOException(parsingException);
        }
        this.group = new OpenSSLECGroupContext(new NativeRef.EC_GROUP(NativeCrypto.EC_KEY_get1_group(this.key.getNativeRef())));
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (this.key.isHardwareBacked()) {
            throw new NotSerializableException("Hardware backed keys cannot be serialized");
        }
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(this.getEncoded());
    }
}


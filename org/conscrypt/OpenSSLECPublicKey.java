/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import org.conscrypt.NativeCrypto;
import org.conscrypt.NativeRef;
import org.conscrypt.OpenSSLECGroupContext;
import org.conscrypt.OpenSSLECPointContext;
import org.conscrypt.OpenSSLKey;
import org.conscrypt.OpenSSLKeyHolder;
import org.conscrypt.OpenSSLX509CertificateFactory;

final class OpenSSLECPublicKey
implements ECPublicKey,
OpenSSLKeyHolder {
    private static final long serialVersionUID = 3215842926808298020L;
    private static final String ALGORITHM = "EC";
    protected transient OpenSSLKey key;
    protected transient OpenSSLECGroupContext group;

    OpenSSLECPublicKey(OpenSSLECGroupContext openSSLECGroupContext, OpenSSLKey openSSLKey) {
        this.group = openSSLECGroupContext;
        this.key = openSSLKey;
    }

    OpenSSLECPublicKey(OpenSSLKey openSSLKey) {
        this.group = new OpenSSLECGroupContext(new NativeRef.EC_GROUP(NativeCrypto.EC_KEY_get1_group(openSSLKey.getNativeRef())));
        this.key = openSSLKey;
    }

    OpenSSLECPublicKey(ECPublicKeySpec object) throws InvalidKeySpecException {
        try {
            this.group = OpenSSLECGroupContext.getInstance(((ECPublicKeySpec)object).getParams());
            object = OpenSSLECPointContext.getInstance(this.group, ((ECPublicKeySpec)object).getW());
            this.key = new OpenSSLKey(NativeCrypto.EVP_PKEY_new_EC_KEY(this.group.getNativeRef(), ((OpenSSLECPointContext)object).getNativeRef(), null));
            return;
        }
        catch (Exception exception) {
            throw new InvalidKeySpecException(exception);
        }
    }

    static OpenSSLKey getInstance(ECPublicKey object) throws InvalidKeyException {
        try {
            OpenSSLECGroupContext openSSLECGroupContext = OpenSSLECGroupContext.getInstance(object.getParams());
            object = OpenSSLECPointContext.getInstance(openSSLECGroupContext, object.getW());
            return new OpenSSLKey(NativeCrypto.EVP_PKEY_new_EC_KEY(openSSLECGroupContext.getNativeRef(), ((OpenSSLECPointContext)object).getNativeRef(), null));
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
        return "X.509";
    }

    @Override
    public final byte[] getEncoded() {
        return NativeCrypto.EVP_marshal_public_key(this.key.getNativeRef());
    }

    @Override
    public final ECParameterSpec getParams() {
        return this.group.getECParameterSpec();
    }

    private ECPoint getPublicKey() {
        OpenSSLECPointContext openSSLECPointContext = new OpenSSLECPointContext(this.group, new NativeRef.EC_POINT(NativeCrypto.EC_KEY_get_public_key(this.key.getNativeRef())));
        return openSSLECPointContext.getECPoint();
    }

    @Override
    public final ECPoint getW() {
        return this.getPublicKey();
    }

    @Override
    public final OpenSSLKey getOpenSSLKey() {
        return this.key;
    }

    public final boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof OpenSSLECPublicKey) {
            object = (OpenSSLECPublicKey)object;
            return this.key.equals(((OpenSSLECPublicKey)object).key);
        }
        if (!(object instanceof ECPublicKey)) {
            return false;
        }
        object = (ECPublicKey)object;
        if (!this.getPublicKey().equals(object.getW())) {
            return false;
        }
        ECParameterSpec eCParameterSpec = this.getParams();
        object = object.getParams();
        return eCParameterSpec.getCurve().equals(((ECParameterSpec)object).getCurve()) && eCParameterSpec.getGenerator().equals(((ECParameterSpec)object).getGenerator()) && eCParameterSpec.getOrder().equals(((ECParameterSpec)object).getOrder()) && eCParameterSpec.getCofactor() == ((ECParameterSpec)object).getCofactor();
    }

    public final int hashCode() {
        return Arrays.hashCode(NativeCrypto.EVP_marshal_public_key(this.key.getNativeRef()));
    }

    public final String toString() {
        return NativeCrypto.EVP_PKEY_print_public(this.key.getNativeRef());
    }

    private void readObject(ObjectInputStream object) throws IOException, ClassNotFoundException {
        ((ObjectInputStream)object).defaultReadObject();
        object = (byte[])((ObjectInputStream)object).readObject();
        try {
            this.key = new OpenSSLKey(NativeCrypto.EVP_parse_public_key((byte[])object));
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


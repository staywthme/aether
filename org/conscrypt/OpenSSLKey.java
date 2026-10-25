/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.ECParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import org.conscrypt.NativeCrypto;
import org.conscrypt.NativeRef;
import org.conscrypt.OpenSSLBIOInputStream;
import org.conscrypt.OpenSSLECPrivateKey;
import org.conscrypt.OpenSSLECPublicKey;
import org.conscrypt.OpenSSLKeyHolder;
import org.conscrypt.OpenSSLRSAPrivateKey;
import org.conscrypt.OpenSSLRSAPublicKey;
import org.conscrypt.OpenSSLX509CertificateFactory;
import org.conscrypt.Platform;

final class OpenSSLKey {
    private final NativeRef.EVP_PKEY ctx;
    private final boolean wrapped;
    private final boolean hardwareBacked;

    OpenSSLKey(long l2) {
        this(l2, false);
    }

    OpenSSLKey(long l2, boolean bl) {
        this(l2, bl, false);
    }

    OpenSSLKey(long l2, boolean bl, boolean bl2) {
        this.ctx = new NativeRef.EVP_PKEY(l2);
        this.wrapped = bl;
        this.hardwareBacked = bl2;
    }

    final NativeRef.EVP_PKEY getNativeRef() {
        return this.ctx;
    }

    final boolean isWrapped() {
        return this.wrapped;
    }

    final boolean isHardwareBacked() {
        return this.hardwareBacked;
    }

    static OpenSSLKey fromPrivateKey(PrivateKey privateKey) throws InvalidKeyException {
        if (privateKey instanceof OpenSSLKeyHolder) {
            return ((OpenSSLKeyHolder)((Object)privateKey)).getOpenSSLKey();
        }
        Object object = privateKey.getFormat();
        if (object == null) {
            return OpenSSLKey.wrapPrivateKey(privateKey);
        }
        if (!"PKCS#8".equals(privateKey.getFormat())) {
            throw new InvalidKeyException("Unknown key format " + (String)object);
        }
        byte[] byArray = privateKey.getEncoded();
        object = byArray;
        if (byArray == null) {
            throw new InvalidKeyException("Key encoding is null");
        }
        try {
            return new OpenSSLKey(NativeCrypto.EVP_parse_private_key(privateKey.getEncoded()));
        }
        catch (OpenSSLX509CertificateFactory.ParsingException parsingException) {
            throw new InvalidKeyException(parsingException);
        }
    }

    static OpenSSLKey fromPrivateKeyPemInputStream(InputStream inputStream2) throws InvalidKeyException {
        inputStream2 = new OpenSSLBIOInputStream(inputStream2, true);
        try {
            long l2 = NativeCrypto.PEM_read_bio_PrivateKey(((OpenSSLBIOInputStream)inputStream2).getBioContext());
            if (l2 == 0L) {
                return null;
            }
            OpenSSLKey openSSLKey = new OpenSSLKey(l2);
            return openSSLKey;
        }
        catch (Exception exception) {
            throw new InvalidKeyException(exception);
        }
        finally {
            ((OpenSSLBIOInputStream)inputStream2).release();
        }
    }

    static OpenSSLKey fromPrivateKeyForTLSStackOnly(PrivateKey privateKey, PublicKey publicKey) throws InvalidKeyException {
        OpenSSLKey openSSLKey = OpenSSLKey.getOpenSSLKey(privateKey);
        if (openSSLKey != null) {
            return openSSLKey;
        }
        openSSLKey = OpenSSLKey.fromKeyMaterial(privateKey);
        if (openSSLKey != null) {
            return openSSLKey;
        }
        return OpenSSLKey.wrapJCAPrivateKeyForTLSStackOnly(privateKey, publicKey);
    }

    static OpenSSLKey fromECPrivateKeyForTLSStackOnly(PrivateKey privateKey, ECParameterSpec eCParameterSpec) throws InvalidKeyException {
        OpenSSLKey openSSLKey = OpenSSLKey.getOpenSSLKey(privateKey);
        if (openSSLKey != null) {
            return openSSLKey;
        }
        openSSLKey = OpenSSLKey.fromKeyMaterial(privateKey);
        if (openSSLKey != null) {
            return openSSLKey;
        }
        return OpenSSLECPrivateKey.wrapJCAPrivateKeyForTLSStackOnly(privateKey, eCParameterSpec);
    }

    private static OpenSSLKey getOpenSSLKey(PrivateKey privateKey) {
        if (privateKey instanceof OpenSSLKeyHolder) {
            return ((OpenSSLKeyHolder)((Object)privateKey)).getOpenSSLKey();
        }
        if ("RSA".equals(privateKey.getAlgorithm())) {
            return Platform.wrapRsaKey(privateKey);
        }
        return null;
    }

    private static OpenSSLKey fromKeyMaterial(PrivateKey object) throws InvalidKeyException {
        if (!"PKCS#8".equals(object.getFormat())) {
            return null;
        }
        byte[] byArray = object.getEncoded();
        object = byArray;
        if (byArray == null) {
            return null;
        }
        try {
            return new OpenSSLKey(NativeCrypto.EVP_parse_private_key((byte[])object));
        }
        catch (OpenSSLX509CertificateFactory.ParsingException parsingException) {
            throw new InvalidKeyException(parsingException);
        }
    }

    private static OpenSSLKey wrapJCAPrivateKeyForTLSStackOnly(PrivateKey privateKey, PublicKey publicKey) throws InvalidKeyException {
        String string = privateKey.getAlgorithm();
        if ("RSA".equals(string)) {
            return OpenSSLRSAPrivateKey.wrapJCAPrivateKeyForTLSStackOnly(privateKey, publicKey);
        }
        if ("EC".equals(string)) {
            return OpenSSLECPrivateKey.wrapJCAPrivateKeyForTLSStackOnly(privateKey, publicKey);
        }
        throw new InvalidKeyException("Unsupported key algorithm: " + string);
    }

    private static OpenSSLKey wrapPrivateKey(PrivateKey privateKey) throws InvalidKeyException {
        if (privateKey instanceof RSAPrivateKey) {
            return OpenSSLRSAPrivateKey.wrapPlatformKey((RSAPrivateKey)privateKey);
        }
        if (privateKey instanceof ECPrivateKey) {
            return OpenSSLECPrivateKey.wrapPlatformKey((ECPrivateKey)privateKey);
        }
        throw new InvalidKeyException("Unknown key type: " + privateKey.toString());
    }

    static OpenSSLKey fromPublicKey(PublicKey publicKey) throws InvalidKeyException {
        if (publicKey instanceof OpenSSLKeyHolder) {
            return ((OpenSSLKeyHolder)((Object)publicKey)).getOpenSSLKey();
        }
        if (!"X.509".equals(publicKey.getFormat())) {
            throw new InvalidKeyException("Unknown key format " + publicKey.getFormat());
        }
        byte[] byArray = publicKey.getEncoded();
        if (byArray == null) {
            throw new InvalidKeyException("Key encoding is null");
        }
        try {
            return new OpenSSLKey(NativeCrypto.EVP_parse_public_key(publicKey.getEncoded()));
        }
        catch (Exception exception) {
            throw new InvalidKeyException(exception);
        }
    }

    static OpenSSLKey fromPublicKeyPemInputStream(InputStream inputStream2) throws InvalidKeyException {
        inputStream2 = new OpenSSLBIOInputStream(inputStream2, true);
        try {
            long l2 = NativeCrypto.PEM_read_bio_PUBKEY(((OpenSSLBIOInputStream)inputStream2).getBioContext());
            if (l2 == 0L) {
                return null;
            }
            OpenSSLKey openSSLKey = new OpenSSLKey(l2);
            return openSSLKey;
        }
        catch (Exception exception) {
            throw new InvalidKeyException(exception);
        }
        finally {
            ((OpenSSLBIOInputStream)inputStream2).release();
        }
    }

    final PublicKey getPublicKey() throws NoSuchAlgorithmException {
        switch (NativeCrypto.EVP_PKEY_type(this.ctx)) {
            case 6: {
                return new OpenSSLRSAPublicKey(this);
            }
            case 408: {
                return new OpenSSLECPublicKey(this);
            }
        }
        throw new NoSuchAlgorithmException("unknown PKEY type");
    }

    static PublicKey getPublicKey(X509EncodedKeySpec object, int n2) throws InvalidKeySpecException {
        try {
            object = new OpenSSLKey(NativeCrypto.EVP_parse_public_key(((X509EncodedKeySpec)object).getEncoded()));
        }
        catch (Exception exception) {
            throw new InvalidKeySpecException(exception);
        }
        if (NativeCrypto.EVP_PKEY_type(((OpenSSLKey)object).getNativeRef()) != n2) {
            throw new InvalidKeySpecException("Unexpected key type");
        }
        try {
            return ((OpenSSLKey)object).getPublicKey();
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new InvalidKeySpecException(noSuchAlgorithmException);
        }
    }

    final PrivateKey getPrivateKey() throws NoSuchAlgorithmException {
        switch (NativeCrypto.EVP_PKEY_type(this.ctx)) {
            case 6: {
                return new OpenSSLRSAPrivateKey(this);
            }
            case 408: {
                return new OpenSSLECPrivateKey(this);
            }
        }
        throw new NoSuchAlgorithmException("unknown PKEY type");
    }

    static PrivateKey getPrivateKey(PKCS8EncodedKeySpec object, int n2) throws InvalidKeySpecException {
        try {
            object = new OpenSSLKey(NativeCrypto.EVP_parse_private_key(((PKCS8EncodedKeySpec)object).getEncoded()));
        }
        catch (Exception exception) {
            throw new InvalidKeySpecException(exception);
        }
        if (NativeCrypto.EVP_PKEY_type(((OpenSSLKey)object).getNativeRef()) != n2) {
            throw new InvalidKeySpecException("Unexpected key type");
        }
        try {
            return ((OpenSSLKey)object).getPrivateKey();
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new InvalidKeySpecException(noSuchAlgorithmException);
        }
    }

    public final boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof OpenSSLKey)) {
            return false;
        }
        if (this.ctx.equals(((OpenSSLKey)(object = (OpenSSLKey)object)).getNativeRef())) {
            return true;
        }
        return NativeCrypto.EVP_PKEY_cmp(this.ctx, ((OpenSSLKey)object).getNativeRef()) == 1;
    }

    public final int hashCode() {
        return this.ctx.hashCode();
    }
}


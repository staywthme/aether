/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Locale;
import javax.crypto.BadPaddingException;
import javax.crypto.CipherSpi;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import javax.crypto.spec.SecretKeySpec;
import org.conscrypt.EmptyArray;
import org.conscrypt.EvpMdRef;
import org.conscrypt.NativeCrypto;
import org.conscrypt.NativeRef;
import org.conscrypt.OpenSSLKey;
import org.conscrypt.OpenSSLRSAPrivateCrtKey;
import org.conscrypt.OpenSSLRSAPrivateKey;
import org.conscrypt.OpenSSLRSAPublicKey;
import org.conscrypt.ShortBufferWithoutStackTraceException;

public abstract class OpenSSLCipherRSA
extends CipherSpi {
    OpenSSLKey key;
    boolean usingPrivateKey;
    boolean encrypting;
    private byte[] buffer;
    private int bufferOffset;
    private boolean inputTooLarge;
    int padding = 1;

    OpenSSLCipherRSA(int n2) {
        this.padding = n2;
    }

    @Override
    protected void engineSetMode(String string) throws NoSuchAlgorithmException {
        String string2 = string.toUpperCase(Locale.ROOT);
        if ("NONE".equals(string2) || "ECB".equals(string2)) {
            return;
        }
        throw new NoSuchAlgorithmException("mode not supported: " + string);
    }

    @Override
    protected void engineSetPadding(String string) throws NoSuchPaddingException {
        String string2 = string.toUpperCase(Locale.ROOT);
        if ("PKCS1PADDING".equals(string2)) {
            this.padding = 1;
            return;
        }
        if ("NOPADDING".equals(string2)) {
            this.padding = 3;
            return;
        }
        throw new NoSuchPaddingException("padding not supported: " + string);
    }

    @Override
    protected int engineGetBlockSize() {
        if (this.encrypting) {
            return this.paddedBlockSizeBytes();
        }
        return this.keySizeBytes();
    }

    @Override
    protected int engineGetOutputSize(int n2) {
        if (this.encrypting) {
            return this.keySizeBytes();
        }
        return this.paddedBlockSizeBytes();
    }

    int paddedBlockSizeBytes() {
        int n2 = this.keySizeBytes();
        if (this.padding == 1) {
            --n2;
            n2 -= 10;
        }
        return n2;
    }

    int keySizeBytes() {
        if (!this.isInitialized()) {
            throw new IllegalStateException("cipher is not initialized");
        }
        return NativeCrypto.RSA_size(this.key.getNativeRef());
    }

    boolean isInitialized() {
        return this.key != null;
    }

    @Override
    protected byte[] engineGetIV() {
        return null;
    }

    @Override
    protected AlgorithmParameters engineGetParameters() {
        return null;
    }

    void doCryptoInit(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException, InvalidKeyException {
    }

    void engineInitInternal(int n2, Key key, AlgorithmParameterSpec algorithmParameterSpec) throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (n2 == 1 || n2 == 3) {
            this.encrypting = true;
        } else if (n2 == 2 || n2 == 4) {
            this.encrypting = false;
        } else {
            throw new InvalidParameterException("Unsupported opmode " + n2);
        }
        if (key instanceof OpenSSLRSAPrivateKey) {
            OpenSSLRSAPrivateKey openSSLRSAPrivateKey = (OpenSSLRSAPrivateKey)key;
            this.usingPrivateKey = true;
            this.key = openSSLRSAPrivateKey.getOpenSSLKey();
        } else if (key instanceof RSAPrivateCrtKey) {
            RSAPrivateCrtKey rSAPrivateCrtKey = (RSAPrivateCrtKey)key;
            this.usingPrivateKey = true;
            this.key = OpenSSLRSAPrivateCrtKey.getInstance(rSAPrivateCrtKey);
        } else if (key instanceof RSAPrivateKey) {
            RSAPrivateKey rSAPrivateKey = (RSAPrivateKey)key;
            this.usingPrivateKey = true;
            this.key = OpenSSLRSAPrivateKey.getInstance(rSAPrivateKey);
        } else if (key instanceof OpenSSLRSAPublicKey) {
            OpenSSLRSAPublicKey openSSLRSAPublicKey = (OpenSSLRSAPublicKey)key;
            this.usingPrivateKey = false;
            this.key = openSSLRSAPublicKey.getOpenSSLKey();
        } else if (key instanceof RSAPublicKey) {
            RSAPublicKey rSAPublicKey = (RSAPublicKey)key;
            this.usingPrivateKey = false;
            this.key = OpenSSLRSAPublicKey.getInstance(rSAPublicKey);
        } else {
            if (key == null) {
                throw new InvalidKeyException("RSA private or public key is null");
            }
            throw new InvalidKeyException("Need RSA private or public key");
        }
        this.buffer = new byte[NativeCrypto.RSA_size(this.key.getNativeRef())];
        this.bufferOffset = 0;
        this.inputTooLarge = false;
        this.doCryptoInit(algorithmParameterSpec);
    }

    @Override
    protected int engineGetKeySize(Key key) throws InvalidKeyException {
        if (key instanceof OpenSSLRSAPrivateKey) {
            return ((OpenSSLRSAPrivateKey)key).getModulus().bitLength();
        }
        if (key instanceof RSAPrivateCrtKey) {
            return ((RSAPrivateCrtKey)key).getModulus().bitLength();
        }
        if (key instanceof RSAPrivateKey) {
            return ((RSAPrivateKey)key).getModulus().bitLength();
        }
        if (key instanceof OpenSSLRSAPublicKey) {
            return ((OpenSSLRSAPublicKey)key).getModulus().bitLength();
        }
        if (key instanceof RSAPublicKey) {
            return ((RSAPublicKey)key).getModulus().bitLength();
        }
        if (key == null) {
            throw new InvalidKeyException("RSA private or public key is null");
        }
        throw new InvalidKeyException("Need RSA private or public key");
    }

    @Override
    protected void engineInit(int n2, Key key, SecureRandom secureRandom) throws InvalidKeyException {
        try {
            this.engineInitInternal(n2, key, null);
            return;
        }
        catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException) {
            throw new InvalidKeyException("Algorithm parameters rejected when none supplied", invalidAlgorithmParameterException);
        }
    }

    @Override
    protected void engineInit(int n2, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (algorithmParameterSpec != null) {
            throw new InvalidAlgorithmParameterException("unknown param type: " + algorithmParameterSpec.getClass().getName());
        }
        this.engineInitInternal(n2, key, algorithmParameterSpec);
    }

    @Override
    protected void engineInit(int n2, Key key, AlgorithmParameters algorithmParameters, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (algorithmParameters != null) {
            throw new InvalidAlgorithmParameterException("unknown param type: " + algorithmParameters.getClass().getName());
        }
        this.engineInitInternal(n2, key, null);
    }

    @Override
    protected byte[] engineUpdate(byte[] byArray, int n2, int n3) {
        if (this.bufferOffset + n3 > this.buffer.length) {
            this.inputTooLarge = true;
            return EmptyArray.BYTE;
        }
        System.arraycopy(byArray, n2, this.buffer, this.bufferOffset, n3);
        this.bufferOffset += n3;
        return EmptyArray.BYTE;
    }

    @Override
    protected int engineUpdate(byte[] byArray, int n2, int n3, byte[] byArray2, int n4) throws ShortBufferException {
        this.engineUpdate(byArray, n2, n3);
        return 0;
    }

    @Override
    protected byte[] engineDoFinal(byte[] byArray, int n2, int n3) throws IllegalBlockSizeException, BadPaddingException {
        if (byArray != null) {
            this.engineUpdate(byArray, n2, n3);
        }
        if (this.inputTooLarge) {
            throw new IllegalBlockSizeException("input must be under " + this.buffer.length + " bytes");
        }
        if (this.bufferOffset != this.buffer.length) {
            if (this.padding == 3) {
                byArray = new byte[this.buffer.length];
                System.arraycopy(this.buffer, 0, byArray, this.buffer.length - this.bufferOffset, this.bufferOffset);
            } else {
                byArray = Arrays.copyOf(this.buffer, this.bufferOffset);
            }
        } else {
            byArray = this.buffer;
        }
        byte[] byArray2 = new byte[this.buffer.length];
        int n4 = this.doCryptoOperation(byArray, byArray2);
        if (!this.encrypting && n4 != byArray2.length) {
            byArray2 = Arrays.copyOf(byArray2, n4);
        }
        this.bufferOffset = 0;
        return byArray2;
    }

    abstract int doCryptoOperation(byte[] var1, byte[] var2) throws BadPaddingException, IllegalBlockSizeException;

    @Override
    protected int engineDoFinal(byte[] byArray, int n2, int n3, byte[] byArray2, int n4) throws ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        if ((n2 = n4 + (byArray = this.engineDoFinal(byArray, n2, n3)).length) > byArray2.length) {
            throw new ShortBufferWithoutStackTraceException("output buffer is too small " + byArray2.length + " < " + n2);
        }
        System.arraycopy(byArray, 0, byArray2, n4, byArray.length);
        return byArray.length;
    }

    @Override
    protected byte[] engineWrap(Key object) throws IllegalBlockSizeException, InvalidKeyException {
        try {
            object = object.getEncoded();
            return this.engineDoFinal((byte[])object, 0, ((Object)object).length);
        }
        catch (BadPaddingException badPaddingException) {
            IllegalBlockSizeException illegalBlockSizeException = new IllegalBlockSizeException();
            illegalBlockSizeException.initCause(badPaddingException);
            throw illegalBlockSizeException;
        }
    }

    @Override
    protected Key engineUnwrap(byte[] byArray, String object, int n2) throws InvalidKeyException, NoSuchAlgorithmException {
        try {
            byArray = this.engineDoFinal(byArray, 0, byArray.length);
            if (n2 == 1) {
                object = KeyFactory.getInstance((String)object);
                return ((KeyFactory)object).generatePublic(new X509EncodedKeySpec(byArray));
            }
            if (n2 == 2) {
                object = KeyFactory.getInstance((String)object);
                return ((KeyFactory)object).generatePrivate(new PKCS8EncodedKeySpec(byArray));
            }
            if (n2 == 3) {
                return new SecretKeySpec(byArray, (String)object);
            }
            throw new UnsupportedOperationException("wrappedKeyType == " + n2);
        }
        catch (IllegalBlockSizeException illegalBlockSizeException) {
            throw new InvalidKeyException(illegalBlockSizeException);
        }
        catch (BadPaddingException badPaddingException) {
            throw new InvalidKeyException(badPaddingException);
        }
        catch (InvalidKeySpecException invalidKeySpecException) {
            throw new InvalidKeyException(invalidKeySpecException);
        }
    }

    public static class OAEP
    extends OpenSSLCipherRSA {
        private long oaepMd;
        private int oaepMdSizeBytes;
        private long mgf1Md;
        private byte[] label;
        private NativeRef.EVP_PKEY_CTX pkeyCtx;

        public OAEP(long l2, int n2) {
            super(4);
            OAEP oAEP = this;
            oAEP.oaepMd = oAEP.mgf1Md = l2;
            this.oaepMdSizeBytes = n2;
        }

        @Override
        protected AlgorithmParameters engineGetParameters() {
            if (!this.isInitialized()) {
                return null;
            }
            try {
                AlgorithmParameters algorithmParameters = AlgorithmParameters.getInstance("OAEP");
                PSource.PSpecified pSpecified = this.label == null ? PSource.PSpecified.DEFAULT : new PSource.PSpecified(this.label);
                algorithmParameters.init(new OAEPParameterSpec(EvpMdRef.getJcaDigestAlgorithmStandardNameFromEVP_MD(this.oaepMd), "MGF1", new MGF1ParameterSpec(EvpMdRef.getJcaDigestAlgorithmStandardNameFromEVP_MD(this.mgf1Md)), pSpecified));
                return algorithmParameters;
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                throw (Error)((Throwable)((Object)new AssertionError((Object)"OAEP not supported"))).initCause(noSuchAlgorithmException);
            }
            catch (InvalidParameterSpecException invalidParameterSpecException) {
                throw new RuntimeException("No providers of AlgorithmParameters.OAEP available");
            }
        }

        @Override
        protected void engineSetPadding(String string) throws NoSuchPaddingException {
            if ((string = string.toUpperCase(Locale.US)).equals("OAEPPADDING")) {
                this.padding = 4;
                return;
            }
            throw new NoSuchPaddingException("Only OAEP padding is supported");
        }

        @Override
        protected void engineInit(int n2, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
            if (algorithmParameterSpec != null && !(algorithmParameterSpec instanceof OAEPParameterSpec)) {
                throw new InvalidAlgorithmParameterException("Only OAEPParameterSpec accepted in OAEP mode");
            }
            this.engineInitInternal(n2, key, algorithmParameterSpec);
        }

        @Override
        protected void engineInit(int n2, Key key, AlgorithmParameters algorithmParameters, SecureRandom object) throws InvalidKeyException, InvalidAlgorithmParameterException {
            object = null;
            if (algorithmParameters != null) {
                try {
                    object = algorithmParameters.getParameterSpec(OAEPParameterSpec.class);
                }
                catch (InvalidParameterSpecException invalidParameterSpecException) {
                    throw new InvalidAlgorithmParameterException("Only OAEP parameters are supported", invalidParameterSpecException);
                }
            }
            this.engineInitInternal(n2, key, (AlgorithmParameterSpec)object);
        }

        @Override
        void engineInitInternal(int n2, Key key, AlgorithmParameterSpec algorithmParameterSpec) throws InvalidKeyException, InvalidAlgorithmParameterException {
            if (n2 == 1 || n2 == 3) {
                if (!(key instanceof PublicKey)) {
                    throw new InvalidKeyException("Only public keys may be used to encrypt");
                }
            } else if (!(n2 != 2 && n2 != 4 || key instanceof PrivateKey)) {
                throw new InvalidKeyException("Only private keys may be used to decrypt");
            }
            super.engineInitInternal(n2, key, algorithmParameterSpec);
        }

        @Override
        void doCryptoInit(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException, InvalidKeyException {
            this.pkeyCtx = new NativeRef.EVP_PKEY_CTX(this.encrypting ? NativeCrypto.EVP_PKEY_encrypt_init(this.key.getNativeRef()) : NativeCrypto.EVP_PKEY_decrypt_init(this.key.getNativeRef()));
            if (algorithmParameterSpec instanceof OAEPParameterSpec) {
                this.readOAEPParameters((OAEPParameterSpec)algorithmParameterSpec);
            }
            NativeCrypto.EVP_PKEY_CTX_set_rsa_padding(this.pkeyCtx.address, 4);
            NativeCrypto.EVP_PKEY_CTX_set_rsa_oaep_md(this.pkeyCtx.address, this.oaepMd);
            NativeCrypto.EVP_PKEY_CTX_set_rsa_mgf1_md(this.pkeyCtx.address, this.mgf1Md);
            if (this.label != null && this.label.length > 0) {
                NativeCrypto.EVP_PKEY_CTX_set_rsa_oaep_label(this.pkeyCtx.address, this.label);
            }
        }

        @Override
        int paddedBlockSizeBytes() {
            int n2 = this.keySizeBytes();
            return n2 - (2 * this.oaepMdSizeBytes + 2);
        }

        private void readOAEPParameters(OAEPParameterSpec object) throws InvalidAlgorithmParameterException {
            Object object2 = ((OAEPParameterSpec)object).getMGFAlgorithm().toUpperCase(Locale.US);
            Object object3 = ((OAEPParameterSpec)object).getMGFParameters();
            if (!"MGF1".equals(object2) && !"1.2.840.113549.1.1.8".equals(object2) || !(object3 instanceof MGF1ParameterSpec)) {
                throw new InvalidAlgorithmParameterException("Only MGF1 supported as mask generation function");
            }
            object2 = (MGF1ParameterSpec)object3;
            object3 = ((OAEPParameterSpec)object).getDigestAlgorithm().toUpperCase(Locale.US);
            try {
                this.oaepMd = EvpMdRef.getEVP_MDByJcaDigestAlgorithmStandardName((String)object3);
                this.oaepMdSizeBytes = EvpMdRef.getDigestSizeBytesByJcaDigestAlgorithmStandardName((String)object3);
                this.mgf1Md = EvpMdRef.getEVP_MDByJcaDigestAlgorithmStandardName(((MGF1ParameterSpec)object2).getDigestAlgorithm());
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                throw new InvalidAlgorithmParameterException(noSuchAlgorithmException);
            }
            object = ((OAEPParameterSpec)object).getPSource();
            if (!"PSpecified".equals(((PSource)object).getAlgorithm()) || !(object instanceof PSource.PSpecified)) {
                throw new InvalidAlgorithmParameterException("Only PSpecified accepted for PSource");
            }
            this.label = ((PSource.PSpecified)object).getValue();
        }

        @Override
        int doCryptoOperation(byte[] byArray, byte[] byArray2) throws BadPaddingException, IllegalBlockSizeException {
            if (this.encrypting) {
                return NativeCrypto.EVP_PKEY_encrypt(this.pkeyCtx, byArray2, 0, byArray, 0, byArray.length);
            }
            return NativeCrypto.EVP_PKEY_decrypt(this.pkeyCtx, byArray2, 0, byArray, 0, byArray.length);
        }

        public static final class SHA512
        extends OAEP {
            public SHA512() {
                super(EvpMdRef.SHA512.EVP_MD, EvpMdRef.SHA512.SIZE_BYTES);
            }
        }

        public static final class SHA384
        extends OAEP {
            public SHA384() {
                super(EvpMdRef.SHA384.EVP_MD, EvpMdRef.SHA384.SIZE_BYTES);
            }
        }

        public static final class SHA256
        extends OAEP {
            public SHA256() {
                super(EvpMdRef.SHA256.EVP_MD, EvpMdRef.SHA256.SIZE_BYTES);
            }
        }

        public static final class SHA224
        extends OAEP {
            public SHA224() {
                super(EvpMdRef.SHA224.EVP_MD, EvpMdRef.SHA224.SIZE_BYTES);
            }
        }

        public static final class SHA1
        extends OAEP {
            public SHA1() {
                super(EvpMdRef.SHA1.EVP_MD, EvpMdRef.SHA1.SIZE_BYTES);
            }
        }
    }

    public static final class Raw
    extends DirectRSA {
        public Raw() {
            super(3);
        }
    }

    public static final class PKCS1
    extends DirectRSA {
        public PKCS1() {
            super(1);
        }
    }

    public static abstract class DirectRSA
    extends OpenSSLCipherRSA {
        public DirectRSA(int n2) {
            super(n2);
        }

        @Override
        int doCryptoOperation(byte[] byArray, byte[] object) throws BadPaddingException, IllegalBlockSizeException {
            int n2;
            if (this.encrypting) {
                n2 = this.usingPrivateKey ? NativeCrypto.RSA_private_encrypt(byArray.length, byArray, object, this.key.getNativeRef(), this.padding) : NativeCrypto.RSA_public_encrypt(byArray.length, byArray, object, this.key.getNativeRef(), this.padding);
            } else {
                try {
                    n2 = this.usingPrivateKey ? NativeCrypto.RSA_private_decrypt(byArray.length, byArray, object, this.key.getNativeRef(), this.padding) : NativeCrypto.RSA_public_decrypt(byArray.length, byArray, object, this.key.getNativeRef(), this.padding);
                }
                catch (SignatureException signatureException) {
                    IllegalBlockSizeException illegalBlockSizeException = new IllegalBlockSizeException();
                    object = illegalBlockSizeException;
                    illegalBlockSizeException.initCause(signatureException);
                    throw object;
                }
            }
            return n2;
        }
    }
}


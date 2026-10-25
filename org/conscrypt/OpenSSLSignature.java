/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.nio.ByteBuffer;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.ProviderException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.SignatureSpi;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;
import org.conscrypt.EvpMdRef;
import org.conscrypt.NativeCrypto;
import org.conscrypt.NativeRef;
import org.conscrypt.OpenSSLKey;

public class OpenSSLSignature
extends SignatureSpi {
    private NativeRef.EVP_MD_CTX ctx;
    private OpenSSLKey key;
    private final EngineType engineType;
    private final long evpMdRef;
    private final byte[] singleByte = new byte[1];
    private boolean signing;
    private long evpPkeyCtx;

    private OpenSSLSignature(long l2, EngineType engineType) {
        this.engineType = engineType;
        this.evpMdRef = l2;
    }

    private void resetContext() throws InvalidAlgorithmParameterException {
        NativeRef.EVP_MD_CTX eVP_MD_CTX = new NativeRef.EVP_MD_CTX(NativeCrypto.EVP_MD_CTX_create());
        this.evpPkeyCtx = this.signing ? NativeCrypto.EVP_DigestSignInit(eVP_MD_CTX, this.evpMdRef, this.key.getNativeRef()) : NativeCrypto.EVP_DigestVerifyInit(eVP_MD_CTX, this.evpMdRef, this.key.getNativeRef());
        OpenSSLSignature openSSLSignature = this;
        openSSLSignature.configureEVP_PKEY_CTX(openSSLSignature.evpPkeyCtx);
        this.ctx = eVP_MD_CTX;
    }

    protected void configureEVP_PKEY_CTX(long l2) throws InvalidAlgorithmParameterException {
    }

    @Override
    protected void engineUpdate(byte by) {
        this.singleByte[0] = by;
        OpenSSLSignature openSSLSignature = this;
        openSSLSignature.engineUpdate(openSSLSignature.singleByte, 0, 1);
    }

    @Override
    protected void engineUpdate(byte[] byArray, int n2, int n3) {
        NativeRef.EVP_MD_CTX eVP_MD_CTX = this.ctx;
        if (this.signing) {
            NativeCrypto.EVP_DigestSignUpdate(eVP_MD_CTX, byArray, n2, n3);
            return;
        }
        NativeCrypto.EVP_DigestVerifyUpdate(eVP_MD_CTX, byArray, n2, n3);
    }

    @Override
    protected void engineUpdate(ByteBuffer byteBuffer) {
        if (!byteBuffer.hasRemaining()) {
            return;
        }
        if (!byteBuffer.isDirect()) {
            super.engineUpdate(byteBuffer);
            return;
        }
        long l2 = NativeCrypto.getDirectBufferAddress(byteBuffer);
        if (l2 == 0L) {
            super.engineUpdate(byteBuffer);
            return;
        }
        int n2 = byteBuffer.position();
        if (n2 < 0) {
            throw new RuntimeException("Negative position");
        }
        long l3 = l2 + (long)n2;
        int n3 = byteBuffer.remaining();
        if (n3 < 0) {
            throw new RuntimeException("Negative remaining amount");
        }
        NativeRef.EVP_MD_CTX eVP_MD_CTX = this.ctx;
        if (this.signing) {
            NativeCrypto.EVP_DigestSignUpdateDirect(eVP_MD_CTX, l3, n3);
        } else {
            NativeCrypto.EVP_DigestVerifyUpdateDirect(eVP_MD_CTX, l3, n3);
        }
        byteBuffer.position(n2 + n3);
    }

    @Override
    @Deprecated
    protected Object engineGetParameter(String string) throws InvalidParameterException {
        return null;
    }

    private void checkEngineType(OpenSSLKey openSSLKey) throws InvalidKeyException {
        int n2 = NativeCrypto.EVP_PKEY_type(openSSLKey.getNativeRef());
        switch (this.engineType) {
            case RSA: {
                if (n2 == 6) break;
                throw new InvalidKeyException("Signature initialized as " + (Object)((Object)this.engineType) + " (not RSA)");
            }
            case EC: {
                if (n2 == 408) break;
                throw new InvalidKeyException("Signature initialized as " + (Object)((Object)this.engineType) + " (not EC)");
            }
            default: {
                throw new InvalidKeyException("Key must be of type " + (Object)((Object)this.engineType));
            }
        }
    }

    private void initInternal(OpenSSLKey openSSLKey, boolean bl) throws InvalidKeyException {
        this.checkEngineType(openSSLKey);
        this.key = openSSLKey;
        this.signing = bl;
        try {
            this.resetContext();
            return;
        }
        catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException) {
            throw new InvalidKeyException(invalidAlgorithmParameterException);
        }
    }

    @Override
    protected void engineInitSign(PrivateKey privateKey) throws InvalidKeyException {
        this.initInternal(OpenSSLKey.fromPrivateKey(privateKey), true);
    }

    @Override
    protected void engineInitVerify(PublicKey publicKey) throws InvalidKeyException {
        this.initInternal(OpenSSLKey.fromPublicKey(publicKey), false);
    }

    @Override
    @Deprecated
    protected void engineSetParameter(String string, Object object) throws InvalidParameterException {
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    protected byte[] engineSign() throws SignatureException {
        Object object = this.ctx;
        try {
            object = NativeCrypto.EVP_DigestSignFinal((NativeRef.EVP_MD_CTX)object);
        }
        catch (Exception exception) {
            try {
                throw new SignatureException(exception);
            }
            catch (Throwable throwable) {
                try {
                    this.resetContext();
                    throw throwable;
                }
                catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException) {
                    throw new AssertionError((Object)"Reset of context failed after it was successful once");
                }
            }
        }
        try {
            this.resetContext();
            return object;
        }
        catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException) {
            throw new AssertionError((Object)"Reset of context failed after it was successful once");
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    protected boolean engineVerify(byte[] byArray) throws SignatureException {
        boolean bl;
        NativeRef.EVP_MD_CTX eVP_MD_CTX = this.ctx;
        try {
            bl = NativeCrypto.EVP_DigestVerifyFinal(eVP_MD_CTX, byArray, 0, byArray.length);
        }
        catch (Exception exception) {
            try {
                throw new SignatureException(exception);
            }
            catch (Throwable throwable) {
                try {
                    this.resetContext();
                    throw throwable;
                }
                catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException) {
                    throw new AssertionError((Object)"Reset of context failed after it was successful once");
                }
            }
        }
        try {
            this.resetContext();
            return bl;
        }
        catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException) {
            throw new AssertionError((Object)"Reset of context failed after it was successful once");
        }
    }

    protected final long getEVP_PKEY_CTX() {
        return this.evpPkeyCtx;
    }

    public static final class SHA512RSAPSS
    extends RSAPSSPadding {
        public SHA512RSAPSS() {
            super(EvpMdRef.SHA512.EVP_MD, "SHA-512", EvpMdRef.SHA512.SIZE_BYTES);
        }
    }

    public static final class SHA384RSAPSS
    extends RSAPSSPadding {
        public SHA384RSAPSS() {
            super(EvpMdRef.SHA384.EVP_MD, "SHA-384", EvpMdRef.SHA384.SIZE_BYTES);
        }
    }

    public static final class SHA256RSAPSS
    extends RSAPSSPadding {
        public SHA256RSAPSS() {
            super(EvpMdRef.SHA256.EVP_MD, "SHA-256", EvpMdRef.SHA256.SIZE_BYTES);
        }
    }

    public static final class SHA224RSAPSS
    extends RSAPSSPadding {
        public SHA224RSAPSS() {
            super(EvpMdRef.SHA224.EVP_MD, "SHA-224", EvpMdRef.SHA224.SIZE_BYTES);
        }
    }

    public static final class SHA1RSAPSS
    extends RSAPSSPadding {
        public SHA1RSAPSS() {
            super(EvpMdRef.SHA1.EVP_MD, "SHA-1", EvpMdRef.SHA1.SIZE_BYTES);
        }
    }

    static abstract class RSAPSSPadding
    extends OpenSSLSignature {
        private static final int TRAILER_FIELD_BC_ID = 1;
        private final String contentDigestAlgorithm;
        private String mgf1DigestAlgorithm;
        private long mgf1EvpMdRef;
        private int saltSizeBytes;

        RSAPSSPadding(long l2, String string, int n2) {
            super(l2, EngineType.RSA);
            this.contentDigestAlgorithm = string;
            this.mgf1DigestAlgorithm = string;
            this.mgf1EvpMdRef = l2;
            this.saltSizeBytes = n2;
        }

        @Override
        protected final void configureEVP_PKEY_CTX(long l2) throws InvalidAlgorithmParameterException {
            NativeCrypto.EVP_PKEY_CTX_set_rsa_padding(l2, 6);
            NativeCrypto.EVP_PKEY_CTX_set_rsa_mgf1_md(l2, this.mgf1EvpMdRef);
            NativeCrypto.EVP_PKEY_CTX_set_rsa_pss_saltlen(l2, this.saltSizeBytes);
        }

        @Override
        protected final void engineSetParameter(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
            long l2;
            if (!(algorithmParameterSpec instanceof PSSParameterSpec)) {
                throw new InvalidAlgorithmParameterException("Unsupported parameter: " + algorithmParameterSpec + ". Only " + PSSParameterSpec.class.getName() + " supported");
            }
            Object object = EvpMdRef.getJcaDigestAlgorithmStandardName(((PSSParameterSpec)(algorithmParameterSpec = (PSSParameterSpec)algorithmParameterSpec)).getDigestAlgorithm());
            if (object == null) {
                throw new InvalidAlgorithmParameterException("Unsupported content digest algorithm: " + ((PSSParameterSpec)algorithmParameterSpec).getDigestAlgorithm());
            }
            if (!this.contentDigestAlgorithm.equalsIgnoreCase((String)object)) {
                throw new InvalidAlgorithmParameterException("Changing content digest algorithm not supported");
            }
            object = ((PSSParameterSpec)algorithmParameterSpec).getMGFAlgorithm();
            if (!"MGF1".equalsIgnoreCase((String)object) && !"1.2.840.113549.1.1.8".equals(object)) {
                throw new InvalidAlgorithmParameterException("Unsupported MGF algorithm: " + (String)object + ". Only MGF1" + " supported");
            }
            object = ((PSSParameterSpec)algorithmParameterSpec).getMGFParameters();
            if (!(object instanceof MGF1ParameterSpec)) {
                throw new InvalidAlgorithmParameterException("Unsupported MGF parameters: " + object + ". Only " + MGF1ParameterSpec.class.getName() + " supported");
            }
            object = (MGF1ParameterSpec)((PSSParameterSpec)algorithmParameterSpec).getMGFParameters();
            String string = EvpMdRef.getJcaDigestAlgorithmStandardName(((MGF1ParameterSpec)object).getDigestAlgorithm());
            if (string == null) {
                throw new InvalidAlgorithmParameterException("Unsupported MGF1 digest algorithm: " + ((MGF1ParameterSpec)object).getDigestAlgorithm());
            }
            try {
                l2 = EvpMdRef.getEVP_MDByJcaDigestAlgorithmStandardName(string);
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                throw new ProviderException("Failed to obtain EVP_MD for " + string, noSuchAlgorithmException);
            }
            int n2 = ((PSSParameterSpec)algorithmParameterSpec).getSaltLength();
            if (n2 < 0) {
                throw new InvalidAlgorithmParameterException("Salt length must be non-negative: " + n2);
            }
            int n3 = ((PSSParameterSpec)algorithmParameterSpec).getTrailerField();
            if (n3 != 1) {
                throw new InvalidAlgorithmParameterException("Unsupported trailer field: " + n3 + ". Only 1" + " supported");
            }
            this.mgf1DigestAlgorithm = string;
            this.mgf1EvpMdRef = l2;
            this.saltSizeBytes = n2;
            long l3 = this.getEVP_PKEY_CTX();
            if (l3 != 0L) {
                this.configureEVP_PKEY_CTX(l3);
            }
        }

        @Override
        protected final AlgorithmParameters engineGetParameters() {
            try {
                AlgorithmParameters algorithmParameters = AlgorithmParameters.getInstance("PSS");
                algorithmParameters.init(new PSSParameterSpec(this.contentDigestAlgorithm, "MGF1", new MGF1ParameterSpec(this.mgf1DigestAlgorithm), this.saltSizeBytes, 1));
                return algorithmParameters;
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                throw new ProviderException("Failed to create PSS AlgorithmParameters", noSuchAlgorithmException);
            }
            catch (InvalidParameterSpecException invalidParameterSpecException) {
                throw new ProviderException("Failed to create PSS AlgorithmParameters", invalidParameterSpecException);
            }
        }
    }

    public static final class SHA512ECDSA
    extends OpenSSLSignature {
        public SHA512ECDSA() {
            super(EvpMdRef.SHA512.EVP_MD, EngineType.EC);
        }
    }

    public static final class SHA384ECDSA
    extends OpenSSLSignature {
        public SHA384ECDSA() {
            super(EvpMdRef.SHA384.EVP_MD, EngineType.EC);
        }
    }

    public static final class SHA256ECDSA
    extends OpenSSLSignature {
        public SHA256ECDSA() {
            super(EvpMdRef.SHA256.EVP_MD, EngineType.EC);
        }
    }

    public static final class SHA224ECDSA
    extends OpenSSLSignature {
        public SHA224ECDSA() {
            super(EvpMdRef.SHA224.EVP_MD, EngineType.EC);
        }
    }

    public static final class SHA1ECDSA
    extends OpenSSLSignature {
        public SHA1ECDSA() {
            super(EvpMdRef.SHA1.EVP_MD, EngineType.EC);
        }
    }

    public static final class SHA512RSA
    extends RSAPKCS1Padding {
        public SHA512RSA() {
            super(EvpMdRef.SHA512.EVP_MD);
        }
    }

    public static final class SHA384RSA
    extends RSAPKCS1Padding {
        public SHA384RSA() {
            super(EvpMdRef.SHA384.EVP_MD);
        }
    }

    public static final class SHA256RSA
    extends RSAPKCS1Padding {
        public SHA256RSA() {
            super(EvpMdRef.SHA256.EVP_MD);
        }
    }

    public static final class SHA224RSA
    extends RSAPKCS1Padding {
        public SHA224RSA() {
            super(EvpMdRef.SHA224.EVP_MD);
        }
    }

    public static final class SHA1RSA
    extends RSAPKCS1Padding {
        public SHA1RSA() {
            super(EvpMdRef.SHA1.EVP_MD);
        }
    }

    public static final class MD5RSA
    extends RSAPKCS1Padding {
        public MD5RSA() {
            super(EvpMdRef.MD5.EVP_MD);
        }
    }

    static abstract class RSAPKCS1Padding
    extends OpenSSLSignature {
        RSAPKCS1Padding(long l2) {
            super(l2, EngineType.RSA);
        }

        @Override
        protected final void configureEVP_PKEY_CTX(long l2) throws InvalidAlgorithmParameterException {
            NativeCrypto.EVP_PKEY_CTX_set_rsa_padding(l2, 1);
        }
    }

    static enum EngineType {
        RSA,
        EC;

    }
}


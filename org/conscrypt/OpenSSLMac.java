/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.MacSpi;
import javax.crypto.SecretKey;
import org.conscrypt.EvpMdRef;
import org.conscrypt.NativeCrypto;
import org.conscrypt.NativeRef;

public abstract class OpenSSLMac
extends MacSpi {
    private NativeRef.HMAC_CTX ctx;
    private final long evp_md;
    private byte[] keyBytes;
    private final int size;
    private final byte[] singleByte = new byte[1];

    private OpenSSLMac(long l2, int n2) {
        this.evp_md = l2;
        this.size = n2;
    }

    @Override
    protected int engineGetMacLength() {
        return this.size;
    }

    @Override
    protected void engineInit(Key key, AlgorithmParameterSpec algorithmParameterSpec) throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (!(key instanceof SecretKey)) {
            throw new InvalidKeyException("key must be a SecretKey");
        }
        if (algorithmParameterSpec != null) {
            throw new InvalidAlgorithmParameterException("unknown parameter type");
        }
        this.keyBytes = key.getEncoded();
        if (this.keyBytes == null) {
            throw new InvalidKeyException("key cannot be encoded");
        }
        this.resetContext();
    }

    private final void resetContext() {
        NativeRef.HMAC_CTX hMAC_CTX = new NativeRef.HMAC_CTX(NativeCrypto.HMAC_CTX_new());
        if (this.keyBytes != null) {
            NativeCrypto.HMAC_Init_ex(hMAC_CTX, this.keyBytes, this.evp_md);
        }
        this.ctx = hMAC_CTX;
    }

    @Override
    protected void engineUpdate(byte by) {
        this.singleByte[0] = by;
        OpenSSLMac openSSLMac = this;
        openSSLMac.engineUpdate(openSSLMac.singleByte, 0, 1);
    }

    @Override
    protected void engineUpdate(byte[] byArray, int n2, int n3) {
        NativeRef.HMAC_CTX hMAC_CTX = this.ctx;
        NativeCrypto.HMAC_Update(hMAC_CTX, byArray, n2, n3);
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
        NativeRef.HMAC_CTX hMAC_CTX = this.ctx;
        NativeCrypto.HMAC_UpdateDirect(hMAC_CTX, l3, n3);
        byteBuffer.position(n2 + n3);
    }

    @Override
    protected byte[] engineDoFinal() {
        Object object = this.ctx;
        object = NativeCrypto.HMAC_Final((NativeRef.HMAC_CTX)object);
        this.resetContext();
        return object;
    }

    @Override
    protected void engineReset() {
        this.resetContext();
    }

    public static final class HmacSHA512
    extends OpenSSLMac {
        public HmacSHA512() {
            super(EvpMdRef.SHA512.EVP_MD, EvpMdRef.SHA512.SIZE_BYTES);
        }
    }

    public static final class HmacSHA384
    extends OpenSSLMac {
        public HmacSHA384() throws NoSuchAlgorithmException {
            super(EvpMdRef.SHA384.EVP_MD, EvpMdRef.SHA384.SIZE_BYTES);
        }
    }

    public static final class HmacSHA256
    extends OpenSSLMac {
        public HmacSHA256() throws NoSuchAlgorithmException {
            super(EvpMdRef.SHA256.EVP_MD, EvpMdRef.SHA256.SIZE_BYTES);
        }
    }

    public static final class HmacSHA224
    extends OpenSSLMac {
        public HmacSHA224() throws NoSuchAlgorithmException {
            super(EvpMdRef.SHA224.EVP_MD, EvpMdRef.SHA224.SIZE_BYTES);
        }
    }

    public static final class HmacSHA1
    extends OpenSSLMac {
        public HmacSHA1() {
            super(EvpMdRef.SHA1.EVP_MD, EvpMdRef.SHA1.SIZE_BYTES);
        }
    }

    public static final class HmacMD5
    extends OpenSSLMac {
        public HmacMD5() {
            super(EvpMdRef.MD5.EVP_MD, EvpMdRef.MD5.SIZE_BYTES);
        }
    }
}


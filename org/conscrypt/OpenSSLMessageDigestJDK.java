/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.nio.ByteBuffer;
import java.security.MessageDigestSpi;
import java.security.NoSuchAlgorithmException;
import org.conscrypt.EvpMdRef;
import org.conscrypt.NativeCrypto;
import org.conscrypt.NativeRef;

public class OpenSSLMessageDigestJDK
extends MessageDigestSpi
implements Cloneable {
    private final NativeRef.EVP_MD_CTX ctx;
    private final long evp_md;
    private final int size;
    private final byte[] singleByte = new byte[1];
    private boolean digestInitializedInContext;

    private OpenSSLMessageDigestJDK(long l2, int n2) throws NoSuchAlgorithmException {
        NativeRef.EVP_MD_CTX eVP_MD_CTX;
        this.evp_md = l2;
        this.size = n2;
        this.ctx = eVP_MD_CTX = new NativeRef.EVP_MD_CTX(NativeCrypto.EVP_MD_CTX_create());
    }

    private OpenSSLMessageDigestJDK(long l2, int n2, NativeRef.EVP_MD_CTX eVP_MD_CTX, boolean bl) {
        this.evp_md = l2;
        this.size = n2;
        this.ctx = eVP_MD_CTX;
        this.digestInitializedInContext = bl;
    }

    private synchronized void ensureDigestInitializedInContext() {
        if (!this.digestInitializedInContext) {
            NativeRef.EVP_MD_CTX eVP_MD_CTX = this.ctx;
            NativeCrypto.EVP_DigestInit_ex(eVP_MD_CTX, this.evp_md);
            this.digestInitializedInContext = true;
        }
    }

    @Override
    protected synchronized void engineReset() {
        NativeRef.EVP_MD_CTX eVP_MD_CTX = this.ctx;
        NativeCrypto.EVP_MD_CTX_cleanup(eVP_MD_CTX);
        this.digestInitializedInContext = false;
    }

    @Override
    protected int engineGetDigestLength() {
        return this.size;
    }

    @Override
    protected synchronized void engineUpdate(byte by) {
        this.singleByte[0] = by;
        OpenSSLMessageDigestJDK openSSLMessageDigestJDK = this;
        openSSLMessageDigestJDK.engineUpdate(openSSLMessageDigestJDK.singleByte, 0, 1);
    }

    @Override
    protected synchronized void engineUpdate(byte[] byArray, int n2, int n3) {
        this.ensureDigestInitializedInContext();
        NativeCrypto.EVP_DigestUpdate(this.ctx, byArray, n2, n3);
    }

    @Override
    protected synchronized void engineUpdate(ByteBuffer byteBuffer) {
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
        this.ensureDigestInitializedInContext();
        NativeCrypto.EVP_DigestUpdateDirect(this.ctx, l3, n3);
        byteBuffer.position(n2 + n3);
    }

    @Override
    protected synchronized byte[] engineDigest() {
        this.ensureDigestInitializedInContext();
        byte[] byArray = new byte[this.size];
        NativeCrypto.EVP_DigestFinal_ex(this.ctx, byArray, 0);
        this.digestInitializedInContext = false;
        return byArray;
    }

    @Override
    public Object clone() {
        NativeRef.EVP_MD_CTX eVP_MD_CTX = new NativeRef.EVP_MD_CTX(NativeCrypto.EVP_MD_CTX_create());
        if (this.digestInitializedInContext) {
            NativeCrypto.EVP_MD_CTX_copy_ex(eVP_MD_CTX, this.ctx);
        }
        return new OpenSSLMessageDigestJDK(this.evp_md, this.size, eVP_MD_CTX, this.digestInitializedInContext);
    }

    public static final class SHA512
    extends OpenSSLMessageDigestJDK {
        public SHA512() throws NoSuchAlgorithmException {
            super(EvpMdRef.SHA512.EVP_MD, EvpMdRef.SHA512.SIZE_BYTES);
        }
    }

    public static final class SHA384
    extends OpenSSLMessageDigestJDK {
        public SHA384() throws NoSuchAlgorithmException {
            super(EvpMdRef.SHA384.EVP_MD, EvpMdRef.SHA384.SIZE_BYTES);
        }
    }

    public static final class SHA256
    extends OpenSSLMessageDigestJDK {
        public SHA256() throws NoSuchAlgorithmException {
            super(EvpMdRef.SHA256.EVP_MD, EvpMdRef.SHA256.SIZE_BYTES);
        }
    }

    public static final class SHA224
    extends OpenSSLMessageDigestJDK {
        public SHA224() throws NoSuchAlgorithmException {
            super(EvpMdRef.SHA224.EVP_MD, EvpMdRef.SHA224.SIZE_BYTES);
        }
    }

    public static final class SHA1
    extends OpenSSLMessageDigestJDK {
        public SHA1() throws NoSuchAlgorithmException {
            super(EvpMdRef.SHA1.EVP_MD, EvpMdRef.SHA1.SIZE_BYTES);
        }
    }

    public static final class MD5
    extends OpenSSLMessageDigestJDK {
        public MD5() throws NoSuchAlgorithmException {
            super(EvpMdRef.MD5.EVP_MD, EvpMdRef.MD5.SIZE_BYTES);
        }
    }
}


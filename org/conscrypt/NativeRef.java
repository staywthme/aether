/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import org.conscrypt.NativeCrypto;

abstract class NativeRef {
    final long address;

    NativeRef(long l2) {
        if (l2 == 0L) {
            throw new NullPointerException("address == 0");
        }
        this.address = l2;
    }

    public boolean equals(Object object) {
        if (!(object instanceof NativeRef)) {
            return false;
        }
        return ((NativeRef)object).address == this.address;
    }

    public int hashCode() {
        return (int)(this.address ^ this.address >>> 32);
    }

    protected void finalize() throws Throwable {
        try {
            if (this.address != 0L) {
                NativeRef nativeRef = this;
                nativeRef.doFree(nativeRef.address);
            }
            return;
        }
        finally {
            super.finalize();
        }
    }

    abstract void doFree(long var1);

    static final class SSL_SESSION
    extends NativeRef {
        SSL_SESSION(long l2) {
            super(l2);
        }

        @Override
        final void doFree(long l2) {
            NativeCrypto.SSL_SESSION_free(l2);
        }
    }

    static final class HMAC_CTX
    extends NativeRef {
        HMAC_CTX(long l2) {
            super(l2);
        }

        @Override
        final void doFree(long l2) {
            NativeCrypto.HMAC_CTX_free(l2);
        }
    }

    static final class EVP_PKEY_CTX
    extends NativeRef {
        EVP_PKEY_CTX(long l2) {
            super(l2);
        }

        @Override
        final void doFree(long l2) {
            NativeCrypto.EVP_PKEY_CTX_free(l2);
        }
    }

    static final class EVP_PKEY
    extends NativeRef {
        EVP_PKEY(long l2) {
            super(l2);
        }

        @Override
        final void doFree(long l2) {
            NativeCrypto.EVP_PKEY_free(l2);
        }
    }

    static final class EVP_MD_CTX
    extends NativeRef {
        EVP_MD_CTX(long l2) {
            super(l2);
        }

        @Override
        final void doFree(long l2) {
            NativeCrypto.EVP_MD_CTX_destroy(l2);
        }
    }

    static final class EVP_CIPHER_CTX
    extends NativeRef {
        EVP_CIPHER_CTX(long l2) {
            super(l2);
        }

        @Override
        final void doFree(long l2) {
            NativeCrypto.EVP_CIPHER_CTX_free(l2);
        }
    }

    static final class EC_POINT
    extends NativeRef {
        EC_POINT(long l2) {
            super(l2);
        }

        @Override
        final void doFree(long l2) {
            NativeCrypto.EC_POINT_clear_free(l2);
        }
    }

    static final class EC_GROUP
    extends NativeRef {
        EC_GROUP(long l2) {
            super(l2);
        }

        @Override
        final void doFree(long l2) {
            NativeCrypto.EC_GROUP_clear_free(l2);
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.conscrypt.NativeCrypto;

class OpenSSLBIOInputStream
extends FilterInputStream {
    private long ctx;

    OpenSSLBIOInputStream(InputStream inputStream2, boolean bl) {
        super(inputStream2);
        this.ctx = NativeCrypto.create_BIO_InputStream(this, bl);
    }

    long getBioContext() {
        return this.ctx;
    }

    void release() {
        NativeCrypto.BIO_free_all(this.ctx);
    }

    int gets(byte[] byArray) throws IOException {
        int n2;
        if (byArray == null || byArray.length == 0) {
            return 0;
        }
        int n3 = 0;
        while (n3 < byArray.length && (n2 = this.read()) != -1) {
            if (n2 == 10) {
                if (n3 != 0) break;
                continue;
            }
            byArray[n3++] = (byte)n2;
        }
        return n3;
    }

    @Override
    public int read(byte[] byArray) throws IOException {
        return this.read(byArray, 0, byArray.length);
    }

    @Override
    public int read(byte[] byArray, int n2, int n3) throws IOException {
        int n4;
        if (n2 < 0 || n3 < 0 || n3 > byArray.length - n2) {
            throw new IndexOutOfBoundsException("Invalid bounds");
        }
        if (n3 == 0) {
            return 0;
        }
        int n5 = 0;
        while ((n4 = super.read(byArray, n2 + n5, n3 - n5 - n2)) != -1 && n2 + (n5 += n4) < n3) {
        }
        if (n5 == 0) {
            return -1;
        }
        return n5;
    }
}


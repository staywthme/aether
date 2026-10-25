/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.io.ByteArrayOutputStream;
import org.conscrypt.NativeCrypto;

final class OpenSSLBIOSink {
    private final long ctx;
    private final ByteArrayOutputStream buffer;
    private int position;

    static OpenSSLBIOSink create() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        return new OpenSSLBIOSink(byteArrayOutputStream);
    }

    private OpenSSLBIOSink(ByteArrayOutputStream byteArrayOutputStream) {
        this.ctx = NativeCrypto.create_BIO_OutputStream(byteArrayOutputStream);
        this.buffer = byteArrayOutputStream;
    }

    final int available() {
        return this.buffer.size() - this.position;
    }

    final void reset() {
        this.buffer.reset();
        this.position = 0;
    }

    final long skip(long l2) {
        int n2 = Math.min(this.available(), (int)l2);
        this.position += n2;
        if (this.position == this.buffer.size()) {
            this.reset();
        }
        return n2;
    }

    final long getContext() {
        return this.ctx;
    }

    final byte[] toByteArray() {
        return this.buffer.toByteArray();
    }

    final int position() {
        return this.position;
    }

    protected final void finalize() throws Throwable {
        try {
            NativeCrypto.BIO_free_all(this.ctx);
            return;
        }
        finally {
            super.finalize();
        }
    }
}


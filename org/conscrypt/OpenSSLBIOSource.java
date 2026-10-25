/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import org.conscrypt.NativeCrypto;
import org.conscrypt.OpenSSLBIOInputStream;

final class OpenSSLBIOSource {
    private OpenSSLBIOInputStream source;

    static OpenSSLBIOSource wrap(ByteBuffer byteBuffer) {
        return new OpenSSLBIOSource(new OpenSSLBIOInputStream(new ByteBufferInputStream(byteBuffer), false));
    }

    private OpenSSLBIOSource(OpenSSLBIOInputStream openSSLBIOInputStream) {
        this.source = openSSLBIOInputStream;
    }

    final long getContext() {
        return this.source.getBioContext();
    }

    private synchronized void release() {
        if (this.source != null) {
            NativeCrypto.BIO_free_all(this.source.getBioContext());
            this.source = null;
        }
    }

    protected final void finalize() throws Throwable {
        try {
            this.release();
            return;
        }
        finally {
            super.finalize();
        }
    }

    static class ByteBufferInputStream
    extends InputStream {
        private final ByteBuffer source;

        ByteBufferInputStream(ByteBuffer byteBuffer) {
            this.source = byteBuffer;
        }

        @Override
        public int read() throws IOException {
            if (this.source.remaining() > 0) {
                return this.source.get();
            }
            return -1;
        }

        @Override
        public int available() throws IOException {
            return this.source.limit() - this.source.position();
        }

        @Override
        public int read(byte[] byArray) throws IOException {
            int n2 = this.source.position();
            this.source.get(byArray);
            return this.source.position() - n2;
        }

        @Override
        public int read(byte[] byArray, int n2, int n3) throws IOException {
            n3 = Math.min(this.source.remaining(), n3);
            int n4 = this.source.position();
            this.source.get(byArray, n2, n3);
            return this.source.position() - n4;
        }

        @Override
        public void reset() throws IOException {
            this.source.reset();
        }

        @Override
        public long skip(long l2) throws IOException {
            long l3 = this.source.position();
            this.source.position((int)(l3 + l2));
            return (long)this.source.position() - l3;
        }
    }
}


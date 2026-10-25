/*
 * Decompiled with CFR 0.152.
 */
package okio;

import java.io.IOException;
import javax.crypto.Cipher;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import okio.-SegmentedByteString;
import okio.Buffer;
import okio.BufferedSink;
import okio.Segment;
import okio.SegmentPool;
import okio.Sink;
import okio.Timeout;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\b\u0010\r\u001a\u00020\u000eH\u0016J\n\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0002J\b\u0010\u0011\u001a\u00020\u000eH\u0016J\b\u0010\u0012\u001a\u00020\u0013H\u0016J\u0018\u0010\u0014\u001a\u00020\b2\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018H\u0002J\u0018\u0010\u0019\u001a\u00020\u000e2\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u001a\u001a\u00020\u0018H\u0016R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001b"}, d2={"Lokio/CipherSink;", "Lokio/Sink;", "sink", "Lokio/BufferedSink;", "cipher", "Ljavax/crypto/Cipher;", "(Lokio/BufferedSink;Ljavax/crypto/Cipher;)V", "blockSize", "", "getCipher", "()Ljavax/crypto/Cipher;", "closed", "", "close", "", "doFinal", "", "flush", "timeout", "Lokio/Timeout;", "update", "source", "Lokio/Buffer;", "remaining", "", "write", "byteCount", "okio"})
@SourceDebugExtension(value={"SMAP\nCipherSink.kt\nKotlin\n*S Kotlin\n*F\n+ 1 CipherSink.kt\nokio/CipherSink\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 Util.kt\nokio/-SegmentedByteString\n*L\n1#1,148:1\n1#2:149\n86#3:150\n*S KotlinDebug\n*F\n+ 1 CipherSink.kt\nokio/CipherSink\n*L\n47#1:150\n*E\n"})
public final class CipherSink
implements Sink {
    @NotNull
    private final BufferedSink sink;
    @NotNull
    private final Cipher cipher;
    private final int blockSize;
    private boolean closed;

    public CipherSink(@NotNull BufferedSink object, @NotNull Cipher cipher) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(cipher, "");
        this.sink = object;
        this.cipher = cipher;
        this.blockSize = this.cipher.getBlockSize();
        if (!(this.blockSize > 0)) {
            object = "Block cipher required " + this.cipher;
            throw new IllegalArgumentException(object.toString());
        }
    }

    @NotNull
    public final Cipher getCipher() {
        return this.cipher;
    }

    @Override
    public final void write(@NotNull Buffer buffer, long l2) throws IOException {
        int n2;
        Intrinsics.checkNotNullParameter(buffer, "");
        -SegmentedByteString.checkOffsetAndCount(buffer.size(), 0L, l2);
        if (!(!this.closed)) {
            String string = "closed";
            throw new IllegalStateException(string.toString());
        }
        for (long i2 = l2; i2 > 0L; i2 -= (long)n2) {
            n2 = this.update(buffer, i2);
        }
    }

    private final int update(Buffer buffer, long l2) {
        Segment segment = buffer.head;
        Intrinsics.checkNotNull(segment);
        Segment segment2 = segment;
        int n2 = segment.limit - segment2.pos;
        int n3 = (int)Math.min(l2, (long)n2);
        Buffer buffer2 = this.sink.getBuffer();
        int n4 = this.cipher.getOutputSize(n3);
        while (n4 > 8192) {
            if (n3 <= this.blockSize) {
                byte[] byArray = this.cipher.update(buffer.readByteArray(l2));
                Intrinsics.checkNotNullExpressionValue(byArray, "");
                this.sink.write(byArray);
                return (int)l2;
            }
            n4 = this.cipher.getOutputSize(n3 -= this.blockSize);
        }
        Segment segment3 = buffer2.writableSegment$okio(n4);
        int n5 = this.cipher.update(segment2.data, segment2.pos, n3, segment3.data, segment3.limit);
        segment3.limit += n5;
        Buffer buffer3 = buffer2;
        buffer3.setSize$okio(buffer3.size() + (long)n5);
        if (segment3.pos == segment3.limit) {
            buffer2.head = segment3.pop();
            SegmentPool.recycle(segment3);
        }
        this.sink.emitCompleteSegments();
        Buffer buffer4 = buffer;
        buffer4.setSize$okio(buffer4.size() - (long)n3);
        segment2.pos += n3;
        if (segment2.pos == segment2.limit) {
            buffer.head = segment2.pop();
            SegmentPool.recycle(segment2);
        }
        return n3;
    }

    @Override
    public final void flush() {
        this.sink.flush();
    }

    @Override
    @NotNull
    public final Timeout timeout() {
        return this.sink.timeout();
    }

    @Override
    public final void close() throws IOException {
        Throwable throwable;
        block4: {
            if (this.closed) {
                return;
            }
            this.closed = true;
            throwable = this.doFinal();
            try {
                this.sink.close();
            }
            catch (Throwable throwable2) {
                if (throwable != null) break block4;
                throwable = throwable2;
            }
        }
        Throwable throwable3 = throwable;
        if (throwable3 != null) {
            throw throwable3;
        }
    }

    private final Throwable doFinal() {
        int n2 = this.cipher.getOutputSize(0);
        if (n2 == 0) {
            return null;
        }
        if (n2 > 8192) {
            try {
                byte[] byArray = this.cipher.doFinal();
                Intrinsics.checkNotNullExpressionValue(byArray, "");
                this.sink.write(byArray);
            }
            catch (Throwable throwable) {
                Throwable throwable2 = throwable;
                return throwable;
            }
            return null;
        }
        Throwable throwable = null;
        Buffer buffer = this.sink.getBuffer();
        Segment segment = buffer.writableSegment$okio(n2);
        try {
            int n3 = this.cipher.doFinal(segment.data, segment.limit);
            segment.limit += n3;
            Buffer buffer2 = buffer;
            buffer2.setSize$okio(buffer2.size() + (long)n3);
        }
        catch (Throwable throwable3) {
            Throwable throwable4 = throwable3;
            throwable = throwable3;
        }
        if (segment.pos == segment.limit) {
            buffer.head = segment.pop();
            SegmentPool.recycle(segment);
        }
        return throwable;
    }
}


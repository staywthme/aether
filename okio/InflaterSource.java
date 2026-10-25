/*
 * Decompiled with CFR 0.152.
 */
package okio;

import java.io.EOFException;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import okio.Buffer;
import okio.BufferedSource;
import okio.Okio;
import okio.Segment;
import okio.SegmentPool;
import okio.Source;
import okio.Timeout;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0017\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0001\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\u0005B\u0017\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0006\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\u0007J\b\u0010\f\u001a\u00020\rH\u0016J\u0018\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u000fH\u0016J\u0016\u0010\u0013\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u000fJ\u0006\u0010\u0014\u001a\u00020\u000bJ\b\u0010\u0015\u001a\u00020\rH\u0002J\b\u0010\u0016\u001a\u00020\u0017H\u0016R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2={"Lokio/InflaterSource;", "Lokio/Source;", "source", "inflater", "Ljava/util/zip/Inflater;", "(Lokio/Source;Ljava/util/zip/Inflater;)V", "Lokio/BufferedSource;", "(Lokio/BufferedSource;Ljava/util/zip/Inflater;)V", "bufferBytesHeldByInflater", "", "closed", "", "close", "", "read", "", "sink", "Lokio/Buffer;", "byteCount", "readOrInflate", "refill", "releaseBytesAfterInflate", "timeout", "Lokio/Timeout;", "okio"})
@SourceDebugExtension(value={"SMAP\nInflaterSource.kt\nKotlin\n*S Kotlin\n*F\n+ 1 InflaterSource.kt\nokio/InflaterSource\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 Util.kt\nokio/-SegmentedByteString\n*L\n1#1,147:1\n1#2:148\n86#3:149\n*S KotlinDebug\n*F\n+ 1 InflaterSource.kt\nokio/InflaterSource\n*L\n73#1:149\n*E\n"})
public final class InflaterSource
implements Source {
    @NotNull
    private final BufferedSource source;
    @NotNull
    private final Inflater inflater;
    private int bufferBytesHeldByInflater;
    private boolean closed;

    public InflaterSource(@NotNull BufferedSource bufferedSource, @NotNull Inflater inflater) {
        Intrinsics.checkNotNullParameter(bufferedSource, "");
        Intrinsics.checkNotNullParameter(inflater, "");
        this.source = bufferedSource;
        this.inflater = inflater;
    }

    public InflaterSource(@NotNull Source source2, @NotNull Inflater inflater) {
        Intrinsics.checkNotNullParameter(source2, "");
        Intrinsics.checkNotNullParameter(inflater, "");
        this(Okio.buffer(source2), inflater);
    }

    @Override
    public final long read(@NotNull Buffer buffer, long l2) throws IOException {
        Intrinsics.checkNotNullParameter(buffer, "");
        do {
            long l3;
            if ((l3 = this.readOrInflate(buffer, l2)) > 0L) {
                return l3;
            }
            if (!this.inflater.finished() && !this.inflater.needsDictionary()) continue;
            return -1L;
        } while (!this.source.exhausted());
        throw new EOFException("source exhausted prematurely");
    }

    public final long readOrInflate(@NotNull Buffer buffer, long l2) throws IOException {
        Intrinsics.checkNotNullParameter(buffer, "");
        if (!(l2 >= 0L)) {
            String string = "byteCount < 0: " + l2;
            throw new IllegalArgumentException(string.toString());
        }
        if (!(!this.closed)) {
            String string = "closed";
            throw new IllegalStateException(string.toString());
        }
        if (l2 == 0L) {
            return 0L;
        }
        try {
            Segment segment = buffer.writableSegment$okio(1);
            int n2 = 8192 - segment.limit;
            int n3 = (int)Math.min(l2, (long)n2);
            this.refill();
            n2 = this.inflater.inflate(segment.data, segment.limit, n3);
            this.releaseBytesAfterInflate();
            if (n2 > 0) {
                segment.limit += n2;
                Buffer buffer2 = buffer;
                buffer2.setSize$okio(buffer2.size() + (long)n2);
                return n2;
            }
            if (segment.pos == segment.limit) {
                buffer.head = segment.pop();
                SegmentPool.recycle(segment);
            }
            return 0L;
        }
        catch (DataFormatException dataFormatException) {
            throw new IOException(dataFormatException);
        }
    }

    public final boolean refill() throws IOException {
        if (!this.inflater.needsInput()) {
            return false;
        }
        if (this.source.exhausted()) {
            return true;
        }
        Segment segment = this.source.getBuffer().head;
        Intrinsics.checkNotNull(segment);
        Segment segment2 = segment;
        this.bufferBytesHeldByInflater = segment2.limit - segment2.pos;
        this.inflater.setInput(segment2.data, segment2.pos, this.bufferBytesHeldByInflater);
        return false;
    }

    private final void releaseBytesAfterInflate() {
        if (this.bufferBytesHeldByInflater == 0) {
            return;
        }
        int n2 = this.bufferBytesHeldByInflater - this.inflater.getRemaining();
        this.bufferBytesHeldByInflater -= n2;
        this.source.skip(n2);
    }

    @Override
    @NotNull
    public final Timeout timeout() {
        return this.source.timeout();
    }

    @Override
    public final void close() throws IOException {
        if (this.closed) {
            return;
        }
        this.inflater.end();
        this.closed = true;
        this.source.close();
    }
}


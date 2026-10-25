/*
 * Decompiled with CFR 0.152.
 */
package okio;

import java.io.OutputStream;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import okio.-SegmentedByteString;
import okio.Buffer;
import okio.Segment;
import okio.SegmentPool;
import okio.Sink;
import okio.Timeout;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\b\u0010\u0007\u001a\u00020\bH\u0016J\b\u0010\t\u001a\u00020\bH\u0016J\b\u0010\u0004\u001a\u00020\u0005H\u0016J\b\u0010\n\u001a\u00020\u000bH\u0016J\u0018\u0010\f\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2={"Lokio/OutputStreamSink;", "Lokio/Sink;", "out", "Ljava/io/OutputStream;", "timeout", "Lokio/Timeout;", "(Ljava/io/OutputStream;Lokio/Timeout;)V", "close", "", "flush", "toString", "", "write", "source", "Lokio/Buffer;", "byteCount", "", "okio"})
@SourceDebugExtension(value={"SMAP\nJvmOkio.kt\nKotlin\n*S Kotlin\n*F\n+ 1 JvmOkio.kt\nokio/OutputStreamSink\n+ 2 Util.kt\nokio/-SegmentedByteString\n*L\n1#1,239:1\n86#2:240\n*S KotlinDebug\n*F\n+ 1 JvmOkio.kt\nokio/OutputStreamSink\n*L\n55#1:240\n*E\n"})
final class OutputStreamSink
implements Sink {
    @NotNull
    private final OutputStream out;
    @NotNull
    private final Timeout timeout;

    public OutputStreamSink(@NotNull OutputStream outputStream2, @NotNull Timeout timeout2) {
        Intrinsics.checkNotNullParameter(outputStream2, "");
        Intrinsics.checkNotNullParameter(timeout2, "");
        this.out = outputStream2;
        this.timeout = timeout2;
    }

    @Override
    public final void write(@NotNull Buffer buffer, long l2) {
        int n2;
        Intrinsics.checkNotNullParameter(buffer, "");
        -SegmentedByteString.checkOffsetAndCount(buffer.size(), 0L, l2);
        for (long i2 = l2; i2 > 0L; i2 -= (long)n2) {
            Segment segment;
            this.timeout.throwIfReached();
            Intrinsics.checkNotNull(buffer.head);
            n2 = segment.limit - segment.pos;
            n2 = (int)Math.min(i2, (long)n2);
            this.out.write(segment.data, segment.pos, n2);
            segment.pos += n2;
            Buffer buffer2 = buffer;
            buffer2.setSize$okio(buffer2.size() - (long)n2);
            if (segment.pos != segment.limit) continue;
            buffer.head = segment.pop();
            SegmentPool.recycle(segment);
        }
    }

    @Override
    public final void flush() {
        this.out.flush();
    }

    @Override
    public final void close() {
        this.out.close();
    }

    @Override
    @NotNull
    public final Timeout timeout() {
        return this.timeout;
    }

    @NotNull
    public final String toString() {
        return "sink(" + this.out + ')';
    }
}


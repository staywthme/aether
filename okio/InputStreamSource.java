/*
 * Decompiled with CFR 0.152.
 */
package okio;

import java.io.IOException;
import java.io.InputStream;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import okio.Buffer;
import okio.Okio;
import okio.Segment;
import okio.SegmentPool;
import okio.Source;
import okio.Timeout;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0012\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\b\u0010\u0007\u001a\u00020\bH\u0016J\u0018\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\nH\u0016J\b\u0010\u0004\u001a\u00020\u0005H\u0016J\b\u0010\u000e\u001a\u00020\u000fH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2={"Lokio/InputStreamSource;", "Lokio/Source;", "input", "Ljava/io/InputStream;", "timeout", "Lokio/Timeout;", "(Ljava/io/InputStream;Lokio/Timeout;)V", "close", "", "read", "", "sink", "Lokio/Buffer;", "byteCount", "toString", "", "okio"})
@SourceDebugExtension(value={"SMAP\nJvmOkio.kt\nKotlin\n*S Kotlin\n*F\n+ 1 JvmOkio.kt\nokio/InputStreamSource\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 Util.kt\nokio/-SegmentedByteString\n*L\n1#1,239:1\n1#2:240\n86#3:241\n*S KotlinDebug\n*F\n+ 1 JvmOkio.kt\nokio/InputStreamSource\n*L\n92#1:241\n*E\n"})
class InputStreamSource
implements Source {
    @NotNull
    private final InputStream input;
    @NotNull
    private final Timeout timeout;

    public InputStreamSource(@NotNull InputStream inputStream2, @NotNull Timeout timeout2) {
        Intrinsics.checkNotNullParameter(inputStream2, "");
        Intrinsics.checkNotNullParameter(timeout2, "");
        this.input = inputStream2;
        this.timeout = timeout2;
    }

    @Override
    public long read(@NotNull Buffer buffer, long l2) {
        Intrinsics.checkNotNullParameter(buffer, "");
        if (l2 == 0L) {
            return 0L;
        }
        if (!(l2 >= 0L)) {
            String string = "byteCount < 0: " + l2;
            throw new IllegalArgumentException(string.toString());
        }
        try {
            this.timeout.throwIfReached();
            Segment segment = buffer.writableSegment$okio(1);
            int n2 = 8192 - segment.limit;
            int n3 = (int)Math.min(l2, (long)n2);
            n2 = this.input.read(segment.data, segment.limit, n3);
            if (n2 == -1) {
                if (segment.pos == segment.limit) {
                    buffer.head = segment.pop();
                    SegmentPool.recycle(segment);
                }
                return -1L;
            }
            segment.limit += n2;
            Buffer buffer2 = buffer;
            buffer2.setSize$okio(buffer2.size() + (long)n2);
            return n2;
        }
        catch (AssertionError assertionError) {
            AssertionError assertionError2 = assertionError;
            if (Okio.isAndroidGetsocknameError(assertionError)) {
                throw new IOException((Throwable)((Object)assertionError2));
            }
            throw assertionError2;
        }
    }

    @Override
    public void close() {
        this.input.close();
    }

    @Override
    @NotNull
    public Timeout timeout() {
        return this.timeout;
    }

    @NotNull
    public String toString() {
        return "source(" + this.input + ')';
    }
}


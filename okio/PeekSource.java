/*
 * Decompiled with CFR 0.152.
 */
package okio;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import okio.Buffer;
import okio.BufferedSource;
import okio.Segment;
import okio.Source;
import okio.Timeout;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\b\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u000f\u001a\u00020\u0010H\u0016J\u0018\u0010\u0011\u001a\u00020\u000e2\u0006\u0010\u0012\u001a\u00020\u00062\u0006\u0010\u0013\u001a\u00020\u000eH\u0016J\b\u0010\u0014\u001a\u00020\u0015H\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2={"Lokio/PeekSource;", "Lokio/Source;", "upstream", "Lokio/BufferedSource;", "(Lokio/BufferedSource;)V", "buffer", "Lokio/Buffer;", "closed", "", "expectedPos", "", "expectedSegment", "Lokio/Segment;", "pos", "", "close", "", "read", "sink", "byteCount", "timeout", "Lokio/Timeout;", "okio"})
@SourceDebugExtension(value={"SMAP\nPeekSource.kt\nKotlin\n*S Kotlin\n*F\n+ 1 PeekSource.kt\nokio/PeekSource\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,74:1\n1#2:75\n*E\n"})
public final class PeekSource
implements Source {
    @NotNull
    private final BufferedSource upstream;
    @NotNull
    private final Buffer buffer;
    @Nullable
    private Segment expectedSegment;
    private int expectedPos;
    private boolean closed;
    private long pos;

    public PeekSource(@NotNull BufferedSource bufferedSource) {
        Intrinsics.checkNotNullParameter(bufferedSource, "");
        this.upstream = bufferedSource;
        this.buffer = this.upstream.getBuffer();
        this.expectedSegment = this.buffer.head;
        Segment segment = this.buffer.head;
        this.expectedPos = segment != null ? segment.pos : -1;
    }

    /*
     * Unable to fully structure code
     */
    @Override
    public final long read(@NotNull Buffer var1_1, long var2_2) {
        Intrinsics.checkNotNullParameter(var1_1, "");
        if (!(var2_2 >= 0L)) {
            var5_3 = "byteCount < 0: " + var2_2;
            throw new IllegalArgumentException(var5_3.toString());
        }
        if (!(this.closed == false)) {
            var5_4 = "closed";
            throw new IllegalStateException(var5_4.toString());
        }
        if (this.expectedSegment == null) ** GOTO lbl-1000
        if (this.expectedSegment == this.buffer.head) {
            v0 = this.buffer.head;
            Intrinsics.checkNotNull(v0);
            ** if (this.expectedPos != v0.pos) goto lbl-1000
        }
        ** GOTO lbl-1000
lbl-1000:
        // 2 sources

        {
            v1 = true;
            ** GOTO lbl17
        }
lbl-1000:
        // 2 sources

        {
            v1 = false;
        }
lbl17:
        // 2 sources

        if (!v1) {
            var5_5 = "Peek source is invalid because upstream source was used";
            throw new IllegalStateException(var5_5.toString());
        }
        if (var2_2 == 0L) {
            return 0L;
        }
        if (!this.upstream.request(this.pos + 1L)) {
            return -1L;
        }
        if (this.expectedSegment == null && this.buffer.head != null) {
            this.expectedSegment = this.buffer.head;
            v2 = this.buffer.head;
            Intrinsics.checkNotNull(v2);
            this.expectedPos = v2.pos;
        }
        var4_6 = Math.min(var2_2, this.buffer.size() - this.pos);
        this.buffer.copyTo(var1_1, this.pos, var4_6);
        this.pos += var4_6;
        return var4_6;
    }

    @Override
    @NotNull
    public final Timeout timeout() {
        return this.upstream.timeout();
    }

    @Override
    public final void close() {
        this.closed = true;
    }
}


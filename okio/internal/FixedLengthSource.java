/*
 * Decompiled with CFR 0.152.
 */
package okio.internal;

import java.io.IOException;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import okio.Buffer;
import okio.ForwardingSource;
import okio.Source;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0000\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u0018\u0010\n\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u0005H\u0016J\u0014\u0010\u000e\u001a\u00020\u000f*\u00020\f2\u0006\u0010\u0010\u001a\u00020\u0005H\u0002R\u000e\u0010\t\u001a\u00020\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2={"Lokio/internal/FixedLengthSource;", "Lokio/ForwardingSource;", "delegate", "Lokio/Source;", "size", "", "truncate", "", "(Lokio/Source;JZ)V", "bytesReceived", "read", "sink", "Lokio/Buffer;", "byteCount", "truncateToSize", "", "newSize", "okio"})
public final class FixedLengthSource
extends ForwardingSource {
    private final long size;
    private final boolean truncate;
    private long bytesReceived;

    public FixedLengthSource(@NotNull Source source2, long l2, boolean bl) {
        Intrinsics.checkNotNullParameter(source2, "");
        super(source2);
        this.size = l2;
        this.truncate = bl;
    }

    @Override
    public final long read(@NotNull Buffer buffer, long l2) {
        long l3;
        long l4;
        Intrinsics.checkNotNullParameter(buffer, "");
        if (this.bytesReceived > this.size) {
            l4 = 0L;
        } else if (this.truncate) {
            l3 = this.size - this.bytesReceived;
            if (l3 == 0L) {
                return -1L;
            }
            l4 = Math.min(l2, l3);
        } else {
            l4 = l2;
        }
        long l5 = l4;
        l3 = super.read(buffer, l5);
        if (l3 != -1L) {
            this.bytesReceived += l3;
        }
        if (this.bytesReceived < this.size && l3 == -1L || this.bytesReceived > this.size) {
            if (l3 > 0L && this.bytesReceived > this.size) {
                Buffer buffer2 = buffer;
                this.truncateToSize(buffer2, buffer2.size() - (this.bytesReceived - this.size));
            }
            throw new IOException("expected " + this.size + " bytes but got " + this.bytesReceived);
        }
        return l3;
    }

    private final void truncateToSize(Buffer buffer, long l2) {
        Buffer buffer2 = new Buffer();
        buffer2.writeAll(buffer);
        buffer.write(buffer2, l2);
        buffer2.clear();
    }
}


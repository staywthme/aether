/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.cache2;

import java.io.IOException;
import java.nio.channels.FileChannel;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import okio.Buffer;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u001e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\bJ\u001e\u0010\f\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2={"Lokhttp3/internal/cache2/FileOperator;", "", "fileChannel", "Ljava/nio/channels/FileChannel;", "(Ljava/nio/channels/FileChannel;)V", "read", "", "pos", "", "sink", "Lokio/Buffer;", "byteCount", "write", "source", "okhttp"})
public final class FileOperator {
    @NotNull
    private final FileChannel fileChannel;

    public FileOperator(@NotNull FileChannel fileChannel) {
        Intrinsics.checkNotNullParameter(fileChannel, "");
        this.fileChannel = fileChannel;
    }

    public final void write(long l2, @NotNull Buffer buffer, long l3) throws IOException {
        long l4;
        Intrinsics.checkNotNullParameter(buffer, "");
        if (l3 < 0L || l3 > buffer.size()) {
            throw new IndexOutOfBoundsException();
        }
        long l5 = l2;
        for (long i2 = l3; i2 > 0L; i2 -= l4) {
            l4 = this.fileChannel.transferFrom(buffer, l5, i2);
            l5 += l4;
        }
    }

    public final void read(long l2, @NotNull Buffer buffer, long l3) {
        long l4;
        Intrinsics.checkNotNullParameter(buffer, "");
        if (l3 < 0L) {
            throw new IndexOutOfBoundsException();
        }
        long l5 = l2;
        for (long i2 = l3; i2 > 0L; i2 -= l4) {
            l4 = this.fileChannel.transferTo(l5, i2, buffer);
            l5 += l4;
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package okio;

import java.io.RandomAccessFile;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import okio.FileHandle;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0007\b\u0000\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\b\u0010\u0007\u001a\u00020\bH\u0014J\b\u0010\t\u001a\u00020\bH\u0014J(\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000b2\u0006\u0010\u0011\u001a\u00020\u000bH\u0014J\u0010\u0010\u0012\u001a\u00020\b2\u0006\u0010\u0013\u001a\u00020\rH\u0014J\b\u0010\u0014\u001a\u00020\rH\u0014J(\u0010\u0015\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000b2\u0006\u0010\u0011\u001a\u00020\u000bH\u0014R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2={"Lokio/JvmFileHandle;", "Lokio/FileHandle;", "readWrite", "", "randomAccessFile", "Ljava/io/RandomAccessFile;", "(ZLjava/io/RandomAccessFile;)V", "protectedClose", "", "protectedFlush", "protectedRead", "", "fileOffset", "", "array", "", "arrayOffset", "byteCount", "protectedResize", "size", "protectedSize", "protectedWrite", "okio"})
public final class JvmFileHandle
extends FileHandle {
    @NotNull
    private final RandomAccessFile randomAccessFile;

    public JvmFileHandle(boolean bl, @NotNull RandomAccessFile randomAccessFile) {
        Intrinsics.checkNotNullParameter(randomAccessFile, "");
        super(bl);
        this.randomAccessFile = randomAccessFile;
    }

    @Override
    protected final synchronized void protectedResize(long l2) {
        long l3 = this.size();
        long l4 = l2 - l3;
        if (l4 > 0L) {
            this.protectedWrite(l3, new byte[(int)l4], 0, (int)l4);
            return;
        }
        this.randomAccessFile.setLength(l2);
    }

    @Override
    protected final synchronized long protectedSize() {
        return this.randomAccessFile.length();
    }

    @Override
    protected final synchronized int protectedRead(long l2, @NotNull byte[] byArray, int n2, int n3) {
        int n4;
        int n5;
        Intrinsics.checkNotNullParameter(byArray, "");
        this.randomAccessFile.seek(l2);
        for (n4 = 0; n4 < n3; n4 += n5) {
            n5 = this.randomAccessFile.read(byArray, n2, n3 - n4);
            if (n5 != -1) continue;
            if (n4 != 0) break;
            return -1;
        }
        return n4;
    }

    @Override
    protected final synchronized void protectedWrite(long l2, @NotNull byte[] byArray, int n2, int n3) {
        Intrinsics.checkNotNullParameter(byArray, "");
        this.randomAccessFile.seek(l2);
        this.randomAccessFile.write(byArray, n2, n3);
    }

    @Override
    protected final synchronized void protectedFlush() {
        this.randomAccessFile.getFD().sync();
    }

    @Override
    protected final synchronized void protectedClose() {
        this.randomAccessFile.close();
    }
}


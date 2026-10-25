/*
 * Decompiled with CFR 0.152.
 */
package okio;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import okio.-SegmentedByteString;
import okio.Buffer;
import okio.RealBufferedSink;
import okio.RealBufferedSource;
import okio.Segment;
import okio.SegmentPool;
import okio.Sink;
import okio.Source;
import okio.Timeout;
import okio._JvmPlatformKt;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000X\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0012\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\b\b&\u0018\u00002\u00060\u0001j\u0002`\u0002:\u0002-.B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\u0005J\u0006\u0010\u0010\u001a\u00020\u0011J\u0006\u0010\u0012\u001a\u00020\u0013J\u0006\u0010\u0014\u001a\u00020\u0013J\u000e\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0011J\u000e\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0018\u001a\u00020\u0019J\b\u0010\u001a\u001a\u00020\u0013H$J\b\u0010\u001b\u001a\u00020\u0013H$J(\u0010\u001c\u001a\u00020\r2\u0006\u0010\u001d\u001a\u00020\u00162\u0006\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\r2\u0006\u0010!\u001a\u00020\rH$J\u0010\u0010\"\u001a\u00020\u00132\u0006\u0010#\u001a\u00020\u0016H$J\b\u0010$\u001a\u00020\u0016H$J(\u0010%\u001a\u00020\u00132\u0006\u0010\u001d\u001a\u00020\u00162\u0006\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\r2\u0006\u0010!\u001a\u00020\rH$J&\u0010&\u001a\u00020\r2\u0006\u0010\u001d\u001a\u00020\u00162\u0006\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\r2\u0006\u0010!\u001a\u00020\rJ\u001e\u0010&\u001a\u00020\u00162\u0006\u0010\u001d\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020'2\u0006\u0010!\u001a\u00020\u0016J \u0010(\u001a\u00020\u00162\u0006\u0010\u001d\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020'2\u0006\u0010!\u001a\u00020\u0016H\u0002J\u0016\u0010)\u001a\u00020\u00132\u0006\u0010\u0017\u001a\u00020\u00112\u0006\u0010\u0015\u001a\u00020\u0016J\u0016\u0010)\u001a\u00020\u00132\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u0015\u001a\u00020\u0016J\u000e\u0010*\u001a\u00020\u00132\u0006\u0010#\u001a\u00020\u0016J\u0010\u0010\u0017\u001a\u00020\u00112\b\b\u0002\u0010\u001d\u001a\u00020\u0016J\u0006\u0010#\u001a\u00020\u0016J\u0010\u0010\u0018\u001a\u00020\u00192\b\b\u0002\u0010\u001d\u001a\u00020\u0016J&\u0010+\u001a\u00020\u00132\u0006\u0010\u001d\u001a\u00020\u00162\u0006\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\r2\u0006\u0010!\u001a\u00020\rJ\u001e\u0010+\u001a\u00020\u00132\u0006\u0010\u001d\u001a\u00020\u00162\u0006\u0010\u0018\u001a\u00020'2\u0006\u0010!\u001a\u00020\u0016J \u0010,\u001a\u00020\u00132\u0006\u0010\u001d\u001a\u00020\u00162\u0006\u0010\u0018\u001a\u00020'2\u0006\u0010!\u001a\u00020\u0016H\u0002R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0015\u0010\u0007\u001a\u00060\bj\u0002`\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u000e\u0010\f\u001a\u00020\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f\u00a8\u0006/"}, d2={"Lokio/FileHandle;", "Ljava/io/Closeable;", "Lokio/Closeable;", "readWrite", "", "(Z)V", "closed", "lock", "Ljava/util/concurrent/locks/ReentrantLock;", "Lokio/Lock;", "getLock", "()Ljava/util/concurrent/locks/ReentrantLock;", "openStreamCount", "", "getReadWrite", "()Z", "appendingSink", "Lokio/Sink;", "close", "", "flush", "position", "", "sink", "source", "Lokio/Source;", "protectedClose", "protectedFlush", "protectedRead", "fileOffset", "array", "", "arrayOffset", "byteCount", "protectedResize", "size", "protectedSize", "protectedWrite", "read", "Lokio/Buffer;", "readNoCloseCheck", "reposition", "resize", "write", "writeNoCloseCheck", "FileHandleSink", "FileHandleSource", "okio"})
@SourceDebugExtension(value={"SMAP\nFileHandle.kt\nKotlin\n*S Kotlin\n*F\n+ 1 FileHandle.kt\nokio/FileHandle\n+ 2 -JvmPlatform.kt\nokio/_JvmPlatformKt\n+ 3 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 4 RealBufferedSource.kt\nokio/RealBufferedSource\n+ 5 RealBufferedSink.kt\nokio/RealBufferedSink\n+ 6 Util.kt\nokio/-SegmentedByteString\n*L\n1#1,444:1\n33#2:445\n33#2:447\n33#2:448\n33#2:449\n33#2:450\n33#2:451\n33#2:452\n33#2:453\n33#2:457\n33#2:459\n1#3:446\n62#4:454\n62#4:455\n62#4:456\n51#5:458\n86#6:460\n86#6:461\n*S KotlinDebug\n*F\n+ 1 FileHandle.kt\nokio/FileHandle\n*L\n69#1:445\n81#1:447\n92#1:448\n105#1:449\n119#1:450\n129#1:451\n139#1:452\n151#1:453\n221#1:457\n287#1:459\n169#1:454\n195#1:455\n202#1:456\n248#1:458\n345#1:460\n374#1:461\n*E\n"})
public abstract class FileHandle
implements Closeable {
    private final boolean readWrite;
    private boolean closed;
    private int openStreamCount;
    @NotNull
    private final ReentrantLock lock;

    public FileHandle(boolean bl) {
        this.readWrite = bl;
        this.lock = _JvmPlatformKt.newLock();
    }

    public final boolean getReadWrite() {
        return this.readWrite;
    }

    @NotNull
    public final ReentrantLock getLock() {
        return this.lock;
    }

    public final int read(long l2, @NotNull byte[] byArray, int n2, int n3) throws IOException {
        Intrinsics.checkNotNullParameter(byArray, "");
        Lock lock = this.lock;
        lock = lock;
        lock.lock();
        try {
            if (!(!this.closed)) {
                String string = "closed";
                throw new IllegalStateException(string.toString());
            }
        }
        finally {
            lock.unlock();
        }
        return this.protectedRead(l2, byArray, n2, n3);
    }

    public final long read(long l2, @NotNull Buffer buffer, long l3) throws IOException {
        Intrinsics.checkNotNullParameter(buffer, "");
        Lock lock = this.lock;
        lock = lock;
        lock.lock();
        try {
            if (!(!this.closed)) {
                String string = "closed";
                throw new IllegalStateException(string.toString());
            }
        }
        finally {
            lock.unlock();
        }
        return this.readNoCloseCheck(l2, buffer, l3);
    }

    public final long size() throws IOException {
        Lock lock = this.lock;
        lock = lock;
        lock.lock();
        try {
            if (!(!this.closed)) {
                String string = "closed";
                throw new IllegalStateException(string.toString());
            }
        }
        finally {
            lock.unlock();
        }
        return this.protectedSize();
    }

    public final void resize(long l2) throws IOException {
        if (!this.readWrite) {
            String string = "file handle is read-only";
            throw new IllegalStateException(string.toString());
        }
        Lock lock = this.lock;
        lock = lock;
        lock.lock();
        try {
            if (!(!this.closed)) {
                String string = "closed";
                throw new IllegalStateException(string.toString());
            }
        }
        finally {
            lock.unlock();
        }
        this.protectedResize(l2);
    }

    public final void write(long l2, @NotNull byte[] byArray, int n2, int n3) {
        Intrinsics.checkNotNullParameter(byArray, "");
        if (!this.readWrite) {
            String string = "file handle is read-only";
            throw new IllegalStateException(string.toString());
        }
        Lock lock = this.lock;
        lock = lock;
        lock.lock();
        try {
            if (!(!this.closed)) {
                String string = "closed";
                throw new IllegalStateException(string.toString());
            }
        }
        finally {
            lock.unlock();
        }
        this.protectedWrite(l2, byArray, n2, n3);
    }

    public final void write(long l2, @NotNull Buffer buffer, long l3) throws IOException {
        Intrinsics.checkNotNullParameter(buffer, "");
        if (!this.readWrite) {
            String string = "file handle is read-only";
            throw new IllegalStateException(string.toString());
        }
        Lock lock = this.lock;
        lock = lock;
        lock.lock();
        try {
            if (!(!this.closed)) {
                String string = "closed";
                throw new IllegalStateException(string.toString());
            }
        }
        finally {
            lock.unlock();
        }
        this.writeNoCloseCheck(l2, buffer, l3);
    }

    public final void flush() throws IOException {
        if (!this.readWrite) {
            String string = "file handle is read-only";
            throw new IllegalStateException(string.toString());
        }
        Lock lock = this.lock;
        lock = lock;
        lock.lock();
        try {
            if (!(!this.closed)) {
                String string = "closed";
                throw new IllegalStateException(string.toString());
            }
        }
        finally {
            lock.unlock();
        }
        this.protectedFlush();
    }

    @NotNull
    public final Source source(long l2) throws IOException {
        Lock lock = this.lock;
        lock = lock;
        lock.lock();
        try {
            if (!(!this.closed)) {
                String string = "closed";
                throw new IllegalStateException(string.toString());
            }
            int n2 = this.openStreamCount;
            this.openStreamCount = n2 + 1;
        }
        finally {
            lock.unlock();
        }
        return new FileHandleSource(this, l2);
    }

    public static /* synthetic */ Source source$default(FileHandle fileHandle, long l2, int n2, Object object) throws IOException {
        if (object != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: source");
        }
        if ((n2 & 1) != 0) {
            l2 = 0L;
        }
        return fileHandle.source(l2);
    }

    public final long position(@NotNull Source object) throws IOException {
        Intrinsics.checkNotNullParameter(object, "");
        long l2 = 0L;
        if (object instanceof RealBufferedSource) {
            RealBufferedSource realBufferedSource = (RealBufferedSource)object;
            l2 = realBufferedSource.bufferField.size();
            object = ((RealBufferedSource)object).source;
        }
        if (!(object instanceof FileHandleSource && ((FileHandleSource)object).getFileHandle() == this)) {
            object = "source was not created by this FileHandle";
            throw new IllegalArgumentException(object.toString());
        }
        if (!(!((FileHandleSource)object).getClosed())) {
            object = "closed";
            throw new IllegalStateException(object.toString());
        }
        return ((FileHandleSource)object).getPosition() - l2;
    }

    public final void reposition(@NotNull Source source2, long l2) throws IOException {
        Intrinsics.checkNotNullParameter(source2, "");
        if (source2 instanceof RealBufferedSource) {
            Source source3 = ((RealBufferedSource)source2).source;
            if (!(source3 instanceof FileHandleSource && ((FileHandleSource)source3).getFileHandle() == this)) {
                String string = "source was not created by this FileHandle";
                throw new IllegalArgumentException(string.toString());
            }
            if (!(!((FileHandleSource)source3).getClosed())) {
                String string = "closed";
                throw new IllegalStateException(string.toString());
            }
            RealBufferedSource realBufferedSource = (RealBufferedSource)source2;
            long l3 = realBufferedSource.bufferField.size();
            long l4 = l2 - (((FileHandleSource)source3).getPosition() - l3);
            boolean bl = 0L <= l4 ? l4 < l3 : false;
            if (bl) {
                ((RealBufferedSource)source2).skip(l4);
                return;
            }
            source2 = (RealBufferedSource)source2;
            ((RealBufferedSource)source2).bufferField.clear();
            ((FileHandleSource)source3).setPosition(l2);
            return;
        }
        if (!(source2 instanceof FileHandleSource && ((FileHandleSource)source2).getFileHandle() == this)) {
            String string = "source was not created by this FileHandle";
            throw new IllegalArgumentException(string.toString());
        }
        if (!(!((FileHandleSource)source2).getClosed())) {
            String string = "closed";
            throw new IllegalStateException(string.toString());
        }
        ((FileHandleSource)source2).setPosition(l2);
    }

    @NotNull
    public final Sink sink(long l2) throws IOException {
        if (!this.readWrite) {
            String string = "file handle is read-only";
            throw new IllegalStateException(string.toString());
        }
        Lock lock = this.lock;
        lock = lock;
        lock.lock();
        try {
            if (!(!this.closed)) {
                String string = "closed";
                throw new IllegalStateException(string.toString());
            }
            int n2 = this.openStreamCount;
            this.openStreamCount = n2 + 1;
        }
        finally {
            lock.unlock();
        }
        return new FileHandleSink(this, l2);
    }

    public static /* synthetic */ Sink sink$default(FileHandle fileHandle, long l2, int n2, Object object) throws IOException {
        if (object != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: sink");
        }
        if ((n2 & 1) != 0) {
            l2 = 0L;
        }
        return fileHandle.sink(l2);
    }

    @NotNull
    public final Sink appendingSink() throws IOException {
        FileHandle fileHandle = this;
        return fileHandle.sink(fileHandle.size());
    }

    public final long position(@NotNull Sink object) throws IOException {
        Intrinsics.checkNotNullParameter(object, "");
        long l2 = 0L;
        if (object instanceof RealBufferedSink) {
            RealBufferedSink realBufferedSink = (RealBufferedSink)object;
            l2 = realBufferedSink.bufferField.size();
            object = ((RealBufferedSink)object).sink;
        }
        if (!(object instanceof FileHandleSink && ((FileHandleSink)object).getFileHandle() == this)) {
            object = "sink was not created by this FileHandle";
            throw new IllegalArgumentException(object.toString());
        }
        if (!(!((FileHandleSink)object).getClosed())) {
            object = "closed";
            throw new IllegalStateException(object.toString());
        }
        return ((FileHandleSink)object).getPosition() + l2;
    }

    public final void reposition(@NotNull Sink object, long l2) throws IOException {
        Intrinsics.checkNotNullParameter(object, "");
        if (object instanceof RealBufferedSink) {
            Sink sink2 = ((RealBufferedSink)object).sink;
            if (!(sink2 instanceof FileHandleSink && ((FileHandleSink)sink2).getFileHandle() == this)) {
                object = "sink was not created by this FileHandle";
                throw new IllegalArgumentException(object.toString());
            }
            if (!(!((FileHandleSink)sink2).getClosed())) {
                object = "closed";
                throw new IllegalStateException(object.toString());
            }
            ((RealBufferedSink)object).emit();
            ((FileHandleSink)sink2).setPosition(l2);
            return;
        }
        if (!(object instanceof FileHandleSink && ((FileHandleSink)object).getFileHandle() == this)) {
            object = "sink was not created by this FileHandle";
            throw new IllegalArgumentException(object.toString());
        }
        if (!(!((FileHandleSink)object).getClosed())) {
            object = "closed";
            throw new IllegalStateException(object.toString());
        }
        ((FileHandleSink)object).setPosition(l2);
    }

    @Override
    public final void close() throws IOException {
        Lock lock = this.lock;
        lock = lock;
        lock.lock();
        try {
            if (this.closed) {
                return;
            }
            this.closed = true;
            if (this.openStreamCount != 0) {
                return;
            }
        }
        finally {
            lock.unlock();
        }
        this.protectedClose();
    }

    protected abstract int protectedRead(long var1, @NotNull byte[] var3, int var4, int var5) throws IOException;

    protected abstract void protectedWrite(long var1, @NotNull byte[] var3, int var4, int var5) throws IOException;

    protected abstract void protectedFlush() throws IOException;

    protected abstract void protectedResize(long var1) throws IOException;

    protected abstract long protectedSize() throws IOException;

    protected abstract void protectedClose() throws IOException;

    private final long readNoCloseCheck(long l2, Buffer buffer, long l3) {
        long l4;
        int n2;
        if (!(l3 >= 0L)) {
            String string = "byteCount < 0: " + l3;
            throw new IllegalArgumentException(string.toString());
        }
        long l5 = l2 + l3;
        for (l4 = l2; l4 < l5; l4 += (long)n2) {
            Segment segment = buffer.writableSegment$okio(1);
            long l6 = l5 - l4;
            n2 = 8192 - segment.limit;
            if ((n2 = this.protectedRead(l4, segment.data, segment.limit, (int)Math.min(l6, (long)n2))) == -1) {
                if (segment.pos == segment.limit) {
                    buffer.head = segment.pop();
                    SegmentPool.recycle(segment);
                }
                if (l2 != l4) break;
                return -1L;
            }
            segment.limit += n2;
            Buffer buffer2 = buffer;
            buffer2.setSize$okio(buffer2.size() + (long)n2);
        }
        return l4 - l2;
    }

    private final void writeNoCloseCheck(long l2, Buffer buffer, long l3) {
        int n2;
        -SegmentedByteString.checkOffsetAndCount(buffer.size(), 0L, l3);
        long l4 = l2 + l3;
        for (long i2 = l2; i2 < l4; i2 += (long)n2) {
            Segment segment;
            Intrinsics.checkNotNull(buffer.head);
            long l5 = l4 - i2;
            n2 = segment.limit - segment.pos;
            n2 = (int)Math.min(l5, (long)n2);
            this.protectedWrite(i2, segment.data, segment.pos, n2);
            segment.pos += n2;
            Buffer buffer2 = buffer;
            buffer2.setSize$okio(buffer2.size() - (long)n2);
            if (segment.pos != segment.limit) continue;
            buffer.head = segment.pop();
            SegmentPool.recycle(segment);
        }
    }

    @Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u000b\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\b\u0010\u0013\u001a\u00020\u0014H\u0016J\b\u0010\u0015\u001a\u00020\u0014H\u0016J\b\u0010\u0016\u001a\u00020\u0017H\u0016J\u0018\u0010\u0018\u001a\u00020\u00142\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u0005H\u0016R\u001a\u0010\u0007\u001a\u00020\bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u001a\u0010\u0004\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012\u00a8\u0006\u001c"}, d2={"Lokio/FileHandle$FileHandleSink;", "Lokio/Sink;", "fileHandle", "Lokio/FileHandle;", "position", "", "(Lokio/FileHandle;J)V", "closed", "", "getClosed", "()Z", "setClosed", "(Z)V", "getFileHandle", "()Lokio/FileHandle;", "getPosition", "()J", "setPosition", "(J)V", "close", "", "flush", "timeout", "Lokio/Timeout;", "write", "source", "Lokio/Buffer;", "byteCount", "okio"})
    @SourceDebugExtension(value={"SMAP\nFileHandle.kt\nKotlin\n*S Kotlin\n*F\n+ 1 FileHandle.kt\nokio/FileHandle$FileHandleSink\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 -JvmPlatform.kt\nokio/_JvmPlatformKt\n*L\n1#1,444:1\n1#2:445\n33#3:446\n*S KotlinDebug\n*F\n+ 1 FileHandle.kt\nokio/FileHandle$FileHandleSink\n*L\n410#1:446\n*E\n"})
    static final class FileHandleSink
    implements Sink {
        @NotNull
        private final FileHandle fileHandle;
        private long position;
        private boolean closed;

        public FileHandleSink(@NotNull FileHandle fileHandle, long l2) {
            Intrinsics.checkNotNullParameter(fileHandle, "");
            this.fileHandle = fileHandle;
            this.position = l2;
        }

        @NotNull
        public final FileHandle getFileHandle() {
            return this.fileHandle;
        }

        public final long getPosition() {
            return this.position;
        }

        public final void setPosition(long l2) {
            this.position = l2;
        }

        public final boolean getClosed() {
            return this.closed;
        }

        public final void setClosed(boolean bl) {
            this.closed = bl;
        }

        @Override
        public final void write(@NotNull Buffer object, long l2) {
            Intrinsics.checkNotNullParameter(object, "");
            if (!(!this.closed)) {
                object = "closed";
                throw new IllegalStateException(object.toString());
            }
            this.fileHandle.writeNoCloseCheck(this.position, (Buffer)object, l2);
            this.position += l2;
        }

        @Override
        public final void flush() {
            if (!(!this.closed)) {
                String string = "closed";
                throw new IllegalStateException(string.toString());
            }
            this.fileHandle.protectedFlush();
        }

        @Override
        @NotNull
        public final Timeout timeout() {
            return Timeout.NONE;
        }

        @Override
        public final void close() {
            if (this.closed) {
                return;
            }
            this.closed = true;
            Lock lock = this.fileHandle.getLock();
            lock = lock;
            lock.lock();
            try {
                FileHandle fileHandle = this.fileHandle;
                int n2 = fileHandle.openStreamCount;
                fileHandle.openStreamCount = n2 + -1;
                if (this.fileHandle.openStreamCount != 0 || !this.fileHandle.closed) {
                    return;
                }
            }
            finally {
                lock.unlock();
            }
            this.fileHandle.protectedClose();
        }
    }

    @Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u000b\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\b\u0010\u0013\u001a\u00020\u0014H\u0016J\u0018\u0010\u0015\u001a\u00020\u00052\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0005H\u0016J\b\u0010\u0019\u001a\u00020\u001aH\u0016R\u001a\u0010\u0007\u001a\u00020\bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u001a\u0010\u0004\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012\u00a8\u0006\u001b"}, d2={"Lokio/FileHandle$FileHandleSource;", "Lokio/Source;", "fileHandle", "Lokio/FileHandle;", "position", "", "(Lokio/FileHandle;J)V", "closed", "", "getClosed", "()Z", "setClosed", "(Z)V", "getFileHandle", "()Lokio/FileHandle;", "getPosition", "()J", "setPosition", "(J)V", "close", "", "read", "sink", "Lokio/Buffer;", "byteCount", "timeout", "Lokio/Timeout;", "okio"})
    @SourceDebugExtension(value={"SMAP\nFileHandle.kt\nKotlin\n*S Kotlin\n*F\n+ 1 FileHandle.kt\nokio/FileHandle$FileHandleSource\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 -JvmPlatform.kt\nokio/_JvmPlatformKt\n*L\n1#1,444:1\n1#2:445\n33#3:446\n*S KotlinDebug\n*F\n+ 1 FileHandle.kt\nokio/FileHandle$FileHandleSource\n*L\n436#1:446\n*E\n"})
    static final class FileHandleSource
    implements Source {
        @NotNull
        private final FileHandle fileHandle;
        private long position;
        private boolean closed;

        public FileHandleSource(@NotNull FileHandle fileHandle, long l2) {
            Intrinsics.checkNotNullParameter(fileHandle, "");
            this.fileHandle = fileHandle;
            this.position = l2;
        }

        @NotNull
        public final FileHandle getFileHandle() {
            return this.fileHandle;
        }

        public final long getPosition() {
            return this.position;
        }

        public final void setPosition(long l2) {
            this.position = l2;
        }

        public final boolean getClosed() {
            return this.closed;
        }

        public final void setClosed(boolean bl) {
            this.closed = bl;
        }

        @Override
        public final long read(@NotNull Buffer buffer, long l2) {
            Intrinsics.checkNotNullParameter(buffer, "");
            if (!(!this.closed)) {
                String string = "closed";
                throw new IllegalStateException(string.toString());
            }
            long l3 = this.fileHandle.readNoCloseCheck(this.position, buffer, l2);
            if (l3 != -1L) {
                this.position += l3;
            }
            return l3;
        }

        @Override
        @NotNull
        public final Timeout timeout() {
            return Timeout.NONE;
        }

        @Override
        public final void close() {
            if (this.closed) {
                return;
            }
            this.closed = true;
            Lock lock = this.fileHandle.getLock();
            lock = lock;
            lock.lock();
            try {
                FileHandle fileHandle = this.fileHandle;
                int n2 = fileHandle.openStreamCount;
                fileHandle.openStreamCount = n2 + -1;
                if (this.fileHandle.openStreamCount != 0 || !this.fileHandle.closed) {
                    return;
                }
            }
            finally {
                lock.unlock();
            }
            this.fileHandle.protectedClose();
        }
    }
}


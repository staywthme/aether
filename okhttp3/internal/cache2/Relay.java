/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.cache2;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import kotlin.Metadata;
import kotlin.jvm.JvmField;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import okhttp3.internal.Util;
import okhttp3.internal.cache2.FileOperator;
import okio.Buffer;
import okio.ByteString;
import okio.Source;
import okio.Timeout;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\n\n\u0002\u0010\b\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\t\u0018\u0000 :2\u00020\u0001:\u0002:;B3\b\u0002\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\u000bJ\u000e\u00102\u001a\u0002032\u0006\u00104\u001a\u00020\u0007J\u0006\u0010\b\u001a\u00020\tJ\b\u00105\u001a\u0004\u0018\u00010\u0005J \u00106\u001a\u0002032\u0006\u00107\u001a\u00020\t2\u0006\u00104\u001a\u00020\u00072\u0006\u00108\u001a\u00020\u0007H\u0002J\u0010\u00109\u001a\u0002032\u0006\u00104\u001a\u00020\u0007H\u0002R\u0011\u0010\f\u001a\u00020\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\n\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u001a\u0010\u0012\u001a\u00020\u0013X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017R\u001c\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0019\"\u0004\b\u001a\u0010\u001bR\u0011\u0010\u001c\u001a\u00020\u00138F\u00a2\u0006\u0006\u001a\u0004\b\u001c\u0010\u0015R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u001d\u001a\u00020\u001eX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010 \"\u0004\b!\u0010\"R\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b#\u0010$\"\u0004\b%\u0010&R\u0011\u0010'\u001a\u00020\r\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010\u000fR\u001a\u0010\u0006\u001a\u00020\u0007X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b)\u0010\u0011\"\u0004\b*\u0010+R\u001c\u0010,\u001a\u0004\u0018\u00010-X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b.\u0010/\"\u0004\b0\u00101\u00a8\u0006<"}, d2={"Lokhttp3/internal/cache2/Relay;", "", "file", "Ljava/io/RandomAccessFile;", "upstream", "Lokio/Source;", "upstreamPos", "", "metadata", "Lokio/ByteString;", "bufferMaxSize", "(Ljava/io/RandomAccessFile;Lokio/Source;JLokio/ByteString;J)V", "buffer", "Lokio/Buffer;", "getBuffer", "()Lokio/Buffer;", "getBufferMaxSize", "()J", "complete", "", "getComplete", "()Z", "setComplete", "(Z)V", "getFile", "()Ljava/io/RandomAccessFile;", "setFile", "(Ljava/io/RandomAccessFile;)V", "isClosed", "sourceCount", "", "getSourceCount", "()I", "setSourceCount", "(I)V", "getUpstream", "()Lokio/Source;", "setUpstream", "(Lokio/Source;)V", "upstreamBuffer", "getUpstreamBuffer", "getUpstreamPos", "setUpstreamPos", "(J)V", "upstreamReader", "Ljava/lang/Thread;", "getUpstreamReader", "()Ljava/lang/Thread;", "setUpstreamReader", "(Ljava/lang/Thread;)V", "commit", "", "upstreamSize", "newSource", "writeHeader", "prefix", "metadataSize", "writeMetadata", "Companion", "RelaySource", "okhttp"})
public final class Relay {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @Nullable
    private RandomAccessFile file;
    @Nullable
    private Source upstream;
    private long upstreamPos;
    @NotNull
    private final ByteString metadata;
    private final long bufferMaxSize;
    @Nullable
    private Thread upstreamReader;
    @NotNull
    private final Buffer upstreamBuffer;
    private boolean complete;
    @NotNull
    private final Buffer buffer;
    private int sourceCount;
    private static final int SOURCE_UPSTREAM = 1;
    private static final int SOURCE_FILE = 2;
    @JvmField
    @NotNull
    public static final ByteString PREFIX_CLEAN = ByteString.Companion.encodeUtf8("OkHttp cache v1\n");
    @JvmField
    @NotNull
    public static final ByteString PREFIX_DIRTY = ByteString.Companion.encodeUtf8("OkHttp DIRTY :(\n");
    private static final long FILE_HEADER_SIZE = 32L;

    private Relay(RandomAccessFile randomAccessFile, Source source2, long l2, ByteString byteString, long l3) {
        this.file = randomAccessFile;
        this.upstream = source2;
        this.upstreamPos = l2;
        this.metadata = byteString;
        this.bufferMaxSize = l3;
        this.upstreamBuffer = new Buffer();
        this.complete = this.upstream == null;
        this.buffer = new Buffer();
    }

    @Nullable
    public final RandomAccessFile getFile() {
        return this.file;
    }

    public final void setFile(@Nullable RandomAccessFile randomAccessFile) {
        this.file = randomAccessFile;
    }

    @Nullable
    public final Source getUpstream() {
        return this.upstream;
    }

    public final void setUpstream(@Nullable Source source2) {
        this.upstream = source2;
    }

    public final long getUpstreamPos() {
        return this.upstreamPos;
    }

    public final void setUpstreamPos(long l2) {
        this.upstreamPos = l2;
    }

    public final long getBufferMaxSize() {
        return this.bufferMaxSize;
    }

    @Nullable
    public final Thread getUpstreamReader() {
        return this.upstreamReader;
    }

    public final void setUpstreamReader(@Nullable Thread thread2) {
        this.upstreamReader = thread2;
    }

    @NotNull
    public final Buffer getUpstreamBuffer() {
        return this.upstreamBuffer;
    }

    public final boolean getComplete() {
        return this.complete;
    }

    public final void setComplete(boolean bl) {
        this.complete = bl;
    }

    @NotNull
    public final Buffer getBuffer() {
        return this.buffer;
    }

    public final int getSourceCount() {
        return this.sourceCount;
    }

    public final void setSourceCount(int n2) {
        this.sourceCount = n2;
    }

    public final boolean isClosed() {
        return this.file == null;
    }

    private final void writeHeader(ByteString object, long l2, long l3) throws IOException {
        Object object2 = new Buffer();
        Buffer buffer = object2;
        ((Buffer)object2).write((ByteString)object);
        buffer.writeLong(l2);
        buffer.writeLong(l3);
        if (!(buffer.size() == 32L)) {
            object = "Failed requirement.";
            throw new IllegalArgumentException(object.toString());
        }
        object = object2;
        RandomAccessFile randomAccessFile = this.file;
        Intrinsics.checkNotNull(randomAccessFile);
        FileChannel fileChannel = randomAccessFile.getChannel();
        Intrinsics.checkNotNullExpressionValue(fileChannel, "");
        object2 = new FileOperator(fileChannel);
        ((FileOperator)object2).write(0L, (Buffer)object, 32L);
    }

    private final void writeMetadata(long l2) throws IOException {
        Buffer buffer = new Buffer();
        buffer.write(this.metadata);
        RandomAccessFile randomAccessFile = this.file;
        Intrinsics.checkNotNull(randomAccessFile);
        FileChannel fileChannel = randomAccessFile.getChannel();
        Intrinsics.checkNotNullExpressionValue(fileChannel, "");
        FileOperator fileOperator = new FileOperator(fileChannel);
        fileOperator.write(l2 + 32L, buffer, this.metadata.size());
    }

    public final void commit(long l2) throws IOException {
        this.writeMetadata(l2);
        RandomAccessFile randomAccessFile = this.file;
        Intrinsics.checkNotNull(randomAccessFile);
        randomAccessFile.getChannel().force(false);
        this.writeHeader(PREFIX_CLEAN, l2, this.metadata.size());
        RandomAccessFile randomAccessFile2 = this.file;
        Intrinsics.checkNotNull(randomAccessFile2);
        randomAccessFile2.getChannel().force(false);
        Relay relay = this;
        synchronized (relay) {
            this.complete = true;
        }
        Source source2 = this.upstream;
        if (source2 != null) {
            Util.closeQuietly(source2);
        }
        this.upstream = null;
    }

    @NotNull
    public final ByteString metadata() {
        return this.metadata;
    }

    @Nullable
    public final Source newSource() {
        Relay relay = this;
        synchronized (relay) {
            block4: {
                if (this.file != null) break block4;
                return null;
            }
            int n2 = this.sourceCount;
            this.sourceCount = n2 + 1;
        }
        return new RelaySource();
    }

    public /* synthetic */ Relay(RandomAccessFile randomAccessFile, Source source2, long l2, ByteString byteString, long l3, DefaultConstructorMarker defaultConstructorMarker) {
        this(randomAccessFile, source2, l2, byteString, l3);
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J&\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u0004J\u000e\u0010\u0013\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u00020\u00068\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u00020\u00068\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\tX\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2={"Lokhttp3/internal/cache2/Relay$Companion;", "", "()V", "FILE_HEADER_SIZE", "", "PREFIX_CLEAN", "Lokio/ByteString;", "PREFIX_DIRTY", "SOURCE_FILE", "", "SOURCE_UPSTREAM", "edit", "Lokhttp3/internal/cache2/Relay;", "file", "Ljava/io/File;", "upstream", "Lokio/Source;", "metadata", "bufferMaxSize", "read", "okhttp"})
    public static final class Companion {
        private Companion() {
        }

        @NotNull
        public final Relay edit(@NotNull File object, @NotNull Source object2, @NotNull ByteString byteString, long l2) throws IOException {
            Intrinsics.checkNotNullParameter(object, "");
            Intrinsics.checkNotNullParameter(object2, "");
            Intrinsics.checkNotNullParameter(byteString, "");
            object = new RandomAccessFile((File)object, "rw");
            object2 = new Relay((RandomAccessFile)object, (Source)object2, 0L, byteString, l2, null);
            ((RandomAccessFile)object).setLength(0L);
            ((Relay)object2).writeHeader(PREFIX_DIRTY, -1L, -1L);
            return object2;
        }

        @NotNull
        public final Relay read(@NotNull File object) throws IOException {
            Intrinsics.checkNotNullParameter(object, "");
            object = new RandomAccessFile((File)object, "rw");
            FileChannel fileChannel = ((RandomAccessFile)object).getChannel();
            Intrinsics.checkNotNullExpressionValue(fileChannel, "");
            Object object2 = new FileOperator(fileChannel);
            Buffer buffer = new Buffer();
            ((FileOperator)object2).read(0L, buffer, 32L);
            ByteString byteString = buffer.readByteString(PREFIX_CLEAN.size());
            if (!Intrinsics.areEqual(byteString, PREFIX_CLEAN)) {
                throw new IOException("unreadable cache file");
            }
            long l2 = buffer.readLong();
            long l3 = buffer.readLong();
            buffer = new Buffer();
            ((FileOperator)object2).read(l2 + 32L, buffer, l3);
            object2 = buffer.readByteString();
            return new Relay((RandomAccessFile)object, null, l2, (ByteString)object2, 0L, null);
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0080\u0004\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\t\u001a\u00020\nH\u0016J\u0018\u0010\u000b\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u0006H\u0016J\b\u0010\u0007\u001a\u00020\bH\u0016R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2={"Lokhttp3/internal/cache2/Relay$RelaySource;", "Lokio/Source;", "(Lokhttp3/internal/cache2/Relay;)V", "fileOperator", "Lokhttp3/internal/cache2/FileOperator;", "sourcePos", "", "timeout", "Lokio/Timeout;", "close", "", "read", "sink", "Lokio/Buffer;", "byteCount", "okhttp"})
    @SourceDebugExtension(value={"SMAP\nRelay.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Relay.kt\nokhttp3/internal/cache2/Relay$RelaySource\n+ 2 Util.kt\nokhttp3/internal/Util\n*L\n1#1,356:1\n563#2:357\n*S KotlinDebug\n*F\n+ 1 Relay.kt\nokhttp3/internal/cache2/Relay$RelaySource\n*L\n267#1:357\n*E\n"})
    public final class RelaySource
    implements Source {
        @NotNull
        private final Timeout timeout = new Timeout();
        @Nullable
        private FileOperator fileOperator;
        private long sourcePos;

        public RelaySource() {
            RandomAccessFile randomAccessFile = Relay.this.getFile();
            Intrinsics.checkNotNull(randomAccessFile);
            FileChannel fileChannel = randomAccessFile.getChannel();
            Intrinsics.checkNotNullExpressionValue(fileChannel, "");
            this.fileOperator = new FileOperator(fileChannel);
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public final long read(@NotNull Buffer object, long l2) throws IOException {
            long l3;
            block21: {
                Relay relay;
                Relay relay2;
                int n2;
                Intrinsics.checkNotNullParameter(object, "");
                if (!(this.fileOperator != null)) {
                    String string = "Check failed.";
                    throw new IllegalStateException(string.toString());
                }
                Relay relay3 = Relay.this;
                Relay relay4 = Relay.this;
                Relay relay5 = relay3;
                synchronized (relay5) {
                    int n3;
                    block20: {
                        long l4;
                        while (this.sourcePos == (l4 = relay4.getUpstreamPos())) {
                            if (relay4.getComplete()) {
                                return -1L;
                            }
                            if (relay4.getUpstreamReader() != null) {
                                this.timeout.waitUntilNotified(relay4);
                                continue;
                            }
                            relay4.setUpstreamReader(Thread.currentThread());
                            n3 = 1;
                            break block20;
                        }
                        l4 = relay4.getUpstreamPos() - relay4.getBuffer().size();
                        if (this.sourcePos >= l4) {
                            long l5 = Math.min(l2, relay4.getUpstreamPos() - this.sourcePos);
                            relay4.getBuffer().copyTo((Buffer)object, this.sourcePos - l4, l5);
                            this.sourcePos += l5;
                            return l5;
                        }
                        n3 = 2;
                    }
                    n2 = n3;
                }
                int n4 = n2;
                if (n4 == 2) {
                    long l6 = Math.min(l2, Relay.this.getUpstreamPos() - this.sourcePos);
                    FileOperator fileOperator = this.fileOperator;
                    Intrinsics.checkNotNull(fileOperator);
                    fileOperator.read(32L + this.sourcePos, (Buffer)object, l6);
                    this.sourcePos += l6;
                    return l6;
                }
                try {
                    Source source2 = Relay.this.getUpstream();
                    Intrinsics.checkNotNull(source2);
                    l3 = source2.read(Relay.this.getUpstreamBuffer(), Relay.this.getBufferMaxSize());
                    if (l3 != -1L) break block21;
                    Relay.this.commit(Relay.this.getUpstreamPos());
                    Relay relay6 = Relay.this;
                    relay2 = Relay.this;
                    relay = relay6;
                }
                catch (Throwable throwable) {
                    relay4 = Relay.this;
                    relay5 = Relay.this;
                    Relay relay7 = relay4;
                    synchronized (relay7) {
                        relay5.setUpstreamReader(null);
                        Relay relay8 = relay5;
                        Intrinsics.checkNotNull(relay8);
                        relay8.notifyAll();
                        throw throwable;
                    }
                }
                synchronized (relay) {
                    relay2.setUpstreamReader(null);
                    object = relay2;
                    Intrinsics.checkNotNull(object);
                    object.notifyAll();
                    return -1L;
                }
            }
            long l7 = Math.min(l3, l2);
            Relay.this.getUpstreamBuffer().copyTo((Buffer)object, 0L, l7);
            this.sourcePos += l7;
            FileOperator fileOperator = this.fileOperator;
            Intrinsics.checkNotNull(fileOperator);
            fileOperator.write(32L + Relay.this.getUpstreamPos(), Relay.this.getUpstreamBuffer().clone(), l3);
            Relay relay = Relay.this;
            Relay relay9 = Relay.this;
            Relay relay10 = relay;
            synchronized (relay10) {
                relay9.getBuffer().write(relay9.getUpstreamBuffer(), l3);
                if (relay9.getBuffer().size() > relay9.getBufferMaxSize()) {
                    relay9.getBuffer().skip(relay9.getBuffer().size() - relay9.getBufferMaxSize());
                }
                Relay relay11 = relay9;
                relay11.setUpstreamPos(relay11.getUpstreamPos() + l3);
            }
            relay10 = Relay.this;
            Relay relay12 = Relay.this;
            object = relay10;
            synchronized (object) {
                relay12.setUpstreamReader(null);
                Relay relay13 = relay12;
                Intrinsics.checkNotNull(relay13);
                ((Object)relay13).notifyAll();
                return l7;
            }
        }

        @Override
        @NotNull
        public final Timeout timeout() {
            return this.timeout;
        }

        @Override
        public final void close() throws IOException {
            if (this.fileOperator == null) {
                return;
            }
            this.fileOperator = null;
            RandomAccessFile randomAccessFile = null;
            Relay relay = Relay.this;
            Relay relay2 = Relay.this;
            synchronized (relay) {
                int n2 = relay2.getSourceCount();
                relay2.setSourceCount(n2 + -1);
                if (relay2.getSourceCount() == 0) {
                    randomAccessFile = relay2.getFile();
                    relay2.setFile(null);
                }
            }
            RandomAccessFile randomAccessFile2 = randomAccessFile;
            if (randomAccessFile2 != null) {
                Util.closeQuietly(randomAccessFile2);
                return;
            }
        }
    }
}


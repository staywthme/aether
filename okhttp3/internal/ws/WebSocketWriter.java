/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.ws;

import java.io.Closeable;
import java.io.IOException;
import java.util.Random;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import okhttp3.internal.ws.MessageDeflater;
import okhttp3.internal.ws.WebSocketProtocol;
import okio.Buffer;
import okio.BufferedSink;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\u0018\u00002\u00020\u0001B5\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\u0003\u0012\u0006\u0010\t\u001a\u00020\u0003\u0012\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fJ\b\u0010\u001b\u001a\u00020\u001cH\u0016J\u0018\u0010\u001d\u001a\u00020\u001c2\u0006\u0010\u001e\u001a\u00020\u001f2\b\u0010 \u001a\u0004\u0018\u00010!J\u0018\u0010\"\u001a\u00020\u001c2\u0006\u0010#\u001a\u00020\u001f2\u0006\u0010$\u001a\u00020!H\u0002J\u0016\u0010%\u001a\u00020\u001c2\u0006\u0010&\u001a\u00020\u001f2\u0006\u0010'\u001a\u00020!J\u000e\u0010(\u001a\u00020\u001c2\u0006\u0010$\u001a\u00020!J\u000e\u0010)\u001a\u00020\u001c2\u0006\u0010$\u001a\u00020!R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0014X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u000e\u0010\u0019\u001a\u00020\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0003X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006*"}, d2={"Lokhttp3/internal/ws/WebSocketWriter;", "Ljava/io/Closeable;", "isClient", "", "sink", "Lokio/BufferedSink;", "random", "Ljava/util/Random;", "perMessageDeflate", "noContextTakeover", "minimumDeflateSize", "", "(ZLokio/BufferedSink;Ljava/util/Random;ZZJ)V", "maskCursor", "Lokio/Buffer$UnsafeCursor;", "maskKey", "", "messageBuffer", "Lokio/Buffer;", "messageDeflater", "Lokhttp3/internal/ws/MessageDeflater;", "getRandom", "()Ljava/util/Random;", "getSink", "()Lokio/BufferedSink;", "sinkBuffer", "writerClosed", "close", "", "writeClose", "code", "", "reason", "Lokio/ByteString;", "writeControlFrame", "opcode", "payload", "writeMessageFrame", "formatOpcode", "data", "writePing", "writePong", "okhttp"})
@SourceDebugExtension(value={"SMAP\nWebSocketWriter.kt\nKotlin\n*S Kotlin\n*F\n+ 1 WebSocketWriter.kt\nokhttp3/internal/ws/WebSocketWriter\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,202:1\n1#2:203\n*E\n"})
public final class WebSocketWriter
implements Closeable {
    private final boolean isClient;
    @NotNull
    private final BufferedSink sink;
    @NotNull
    private final Random random;
    private final boolean perMessageDeflate;
    private final boolean noContextTakeover;
    private final long minimumDeflateSize;
    @NotNull
    private final Buffer messageBuffer;
    @NotNull
    private final Buffer sinkBuffer;
    private boolean writerClosed;
    @Nullable
    private MessageDeflater messageDeflater;
    @Nullable
    private final byte[] maskKey;
    @Nullable
    private final Buffer.UnsafeCursor maskCursor;

    public WebSocketWriter(boolean bl, @NotNull BufferedSink bufferedSink, @NotNull Random random, boolean bl2, boolean bl3, long l2) {
        Intrinsics.checkNotNullParameter(bufferedSink, "");
        Intrinsics.checkNotNullParameter(random, "");
        this.isClient = bl;
        this.sink = bufferedSink;
        this.random = random;
        this.perMessageDeflate = bl2;
        this.noContextTakeover = bl3;
        this.minimumDeflateSize = l2;
        this.messageBuffer = new Buffer();
        this.sinkBuffer = this.sink.getBuffer();
        this.maskKey = this.isClient ? new byte[4] : null;
        this.maskCursor = this.isClient ? new Buffer.UnsafeCursor() : null;
    }

    @NotNull
    public final BufferedSink getSink() {
        return this.sink;
    }

    @NotNull
    public final Random getRandom() {
        return this.random;
    }

    public final void writePing(@NotNull ByteString byteString) throws IOException {
        Intrinsics.checkNotNullParameter(byteString, "");
        this.writeControlFrame(9, byteString);
    }

    public final void writePong(@NotNull ByteString byteString) throws IOException {
        Intrinsics.checkNotNullParameter(byteString, "");
        this.writeControlFrame(10, byteString);
    }

    public final void writeClose(int n2, @Nullable ByteString byteString) throws IOException {
        Object object = ByteString.EMPTY;
        if (n2 != 0 || byteString != null) {
            if (n2 != 0) {
                WebSocketProtocol.INSTANCE.validateCloseCode(n2);
            }
            object = new Buffer();
            ((Buffer)object).writeShort(n2);
            if (byteString != null) {
                ((Buffer)object).write(byteString);
            }
            object = ((Buffer)object).readByteString();
        }
        try {
            this.writeControlFrame(8, (ByteString)object);
            return;
        }
        finally {
            this.writerClosed = true;
        }
    }

    private final void writeControlFrame(int n2, ByteString byteString) throws IOException {
        if (this.writerClosed) {
            throw new IOException("closed");
        }
        int n3 = byteString.size();
        if (!((long)n3 <= 125L)) {
            String string = "Payload size must be less than or equal to 125";
            throw new IllegalArgumentException(string.toString());
        }
        n2 = 0x80 | n2;
        this.sinkBuffer.writeByte(n2);
        if (this.isClient) {
            n2 = n3 | 0x80;
            this.sinkBuffer.writeByte(n2);
            Intrinsics.checkNotNull(this.maskKey);
            this.random.nextBytes(this.maskKey);
            this.sinkBuffer.write(this.maskKey);
            if (n3 > 0) {
                long l2 = this.sinkBuffer.size();
                this.sinkBuffer.write(byteString);
                Buffer.UnsafeCursor unsafeCursor = this.maskCursor;
                Intrinsics.checkNotNull(unsafeCursor);
                this.sinkBuffer.readAndWriteUnsafe(unsafeCursor);
                this.maskCursor.seek(l2);
                WebSocketProtocol.INSTANCE.toggleMask(this.maskCursor, this.maskKey);
                this.maskCursor.close();
            }
        } else {
            this.sinkBuffer.writeByte(n3);
            this.sinkBuffer.write(byteString);
        }
        this.sink.flush();
    }

    public final void writeMessageFrame(int n2, @NotNull ByteString object) throws IOException {
        Intrinsics.checkNotNullParameter(object, "");
        if (this.writerClosed) {
            throw new IOException("closed");
        }
        this.messageBuffer.write((ByteString)object);
        n2 |= 0x80;
        if (this.perMessageDeflate && (long)((ByteString)object).size() >= this.minimumDeflateSize) {
            Object object2 = this.messageDeflater;
            if (object2 == null) {
                MessageDeflater messageDeflater;
                object = new MessageDeflater(this.noContextTakeover);
                this.messageDeflater = messageDeflater = object;
                object2 = object;
            }
            MessageDeflater messageDeflater = object2;
            ((MessageDeflater)object2).deflate(this.messageBuffer);
            n2 |= 0x40;
        }
        long l2 = this.messageBuffer.size();
        this.sinkBuffer.writeByte(n2);
        int n3 = 0;
        if (this.isClient) {
            n3 = 128;
        }
        if (l2 <= 125L) {
            this.sinkBuffer.writeByte(n3 |= (int)l2);
        } else if (l2 <= 65535L) {
            this.sinkBuffer.writeByte(n3 |= 0x7E);
            this.sinkBuffer.writeShort((int)l2);
        } else {
            this.sinkBuffer.writeByte(n3 |= 0x7F);
            this.sinkBuffer.writeLong(l2);
        }
        if (this.isClient) {
            Intrinsics.checkNotNull(this.maskKey);
            this.random.nextBytes(this.maskKey);
            this.sinkBuffer.write(this.maskKey);
            if (l2 > 0L) {
                Buffer.UnsafeCursor unsafeCursor = this.maskCursor;
                Intrinsics.checkNotNull(unsafeCursor);
                this.messageBuffer.readAndWriteUnsafe(unsafeCursor);
                this.maskCursor.seek(0L);
                WebSocketProtocol.INSTANCE.toggleMask(this.maskCursor, this.maskKey);
                this.maskCursor.close();
            }
        }
        this.sinkBuffer.write(this.messageBuffer, l2);
        this.sink.emit();
    }

    @Override
    public final void close() {
        MessageDeflater messageDeflater = this.messageDeflater;
        if (messageDeflater != null) {
            messageDeflater.close();
            return;
        }
    }
}


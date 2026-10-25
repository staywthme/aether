/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.ws;

import java.io.Closeable;
import java.io.IOException;
import java.net.ProtocolException;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import okhttp3.internal.Util;
import okhttp3.internal.ws.MessageInflater;
import okhttp3.internal.ws.WebSocketProtocol;
import okio.Buffer;
import okio.BufferedSource;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000P\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\b\u0018\u00002\u00020\u0001:\u0001&B-\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\u0003\u0012\u0006\u0010\t\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\nJ\b\u0010\u001e\u001a\u00020\u001fH\u0016J\u0006\u0010 \u001a\u00020\u001fJ\b\u0010!\u001a\u00020\u001fH\u0002J\b\u0010\"\u001a\u00020\u001fH\u0002J\b\u0010#\u001a\u00020\u001fH\u0002J\b\u0010$\u001a\u00020\u001fH\u0002J\b\u0010%\u001a\u00020\u001fH\u0002R\u000e\u0010\u000b\u001a\u00020\u0003X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0003X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0003X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0017\u001a\u0004\u0018\u00010\u0018X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u001aX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0003X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001d\u00a8\u0006'"}, d2={"Lokhttp3/internal/ws/WebSocketReader;", "Ljava/io/Closeable;", "isClient", "", "source", "Lokio/BufferedSource;", "frameCallback", "Lokhttp3/internal/ws/WebSocketReader$FrameCallback;", "perMessageDeflate", "noContextTakeover", "(ZLokio/BufferedSource;Lokhttp3/internal/ws/WebSocketReader$FrameCallback;ZZ)V", "closed", "controlFrameBuffer", "Lokio/Buffer;", "frameLength", "", "isControlFrame", "isFinalFrame", "maskCursor", "Lokio/Buffer$UnsafeCursor;", "maskKey", "", "messageFrameBuffer", "messageInflater", "Lokhttp3/internal/ws/MessageInflater;", "opcode", "", "readingCompressedMessage", "getSource", "()Lokio/BufferedSource;", "close", "", "processNextFrame", "readControlFrame", "readHeader", "readMessage", "readMessageFrame", "readUntilNonControlFrame", "FrameCallback", "okhttp"})
@SourceDebugExtension(value={"SMAP\nWebSocketReader.kt\nKotlin\n*S Kotlin\n*F\n+ 1 WebSocketReader.kt\nokhttp3/internal/ws/WebSocketReader\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,298:1\n1#2:299\n*E\n"})
public final class WebSocketReader
implements Closeable {
    private final boolean isClient;
    @NotNull
    private final BufferedSource source;
    @NotNull
    private final FrameCallback frameCallback;
    private final boolean perMessageDeflate;
    private final boolean noContextTakeover;
    private boolean closed;
    private int opcode;
    private long frameLength;
    private boolean isFinalFrame;
    private boolean isControlFrame;
    private boolean readingCompressedMessage;
    @NotNull
    private final Buffer controlFrameBuffer;
    @NotNull
    private final Buffer messageFrameBuffer;
    @Nullable
    private MessageInflater messageInflater;
    @Nullable
    private final byte[] maskKey;
    @Nullable
    private final Buffer.UnsafeCursor maskCursor;

    public WebSocketReader(boolean bl, @NotNull BufferedSource bufferedSource, @NotNull FrameCallback frameCallback, boolean bl2, boolean bl3) {
        Intrinsics.checkNotNullParameter(bufferedSource, "");
        Intrinsics.checkNotNullParameter(frameCallback, "");
        this.isClient = bl;
        this.source = bufferedSource;
        this.frameCallback = frameCallback;
        this.perMessageDeflate = bl2;
        this.noContextTakeover = bl3;
        this.controlFrameBuffer = new Buffer();
        this.messageFrameBuffer = new Buffer();
        this.maskKey = this.isClient ? null : new byte[4];
        this.maskCursor = this.isClient ? null : new Buffer.UnsafeCursor();
    }

    @NotNull
    public final BufferedSource getSource() {
        return this.source;
    }

    public final void processNextFrame() throws IOException {
        this.readHeader();
        if (this.isControlFrame) {
            this.readControlFrame();
            return;
        }
        this.readMessageFrame();
    }

    private final void readHeader() throws IOException, ProtocolException {
        boolean bl;
        int n2;
        if (this.closed) {
            throw new IOException("closed");
        }
        long l2 = this.source.timeout().timeoutNanos();
        this.source.timeout().clearTimeout();
        try {
            n2 = Util.and(this.source.readByte(), 255);
        }
        finally {
            this.source.timeout().timeout(l2, TimeUnit.NANOSECONDS);
        }
        this.opcode = n2 & 0xF;
        this.isFinalFrame = (n2 & 0x80) != 0;
        boolean bl2 = this.isControlFrame = (n2 & 8) != 0;
        if (this.isControlFrame && !this.isFinalFrame) {
            throw new ProtocolException("Control frames must be final.");
        }
        boolean bl3 = (n2 & 0x40) != 0;
        switch (this.opcode) {
            case 1: 
            case 2: {
                boolean bl4;
                if (bl3) {
                    if (!this.perMessageDeflate) {
                        throw new ProtocolException("Unexpected rsv1 flag");
                    }
                    bl4 = true;
                } else {
                    bl4 = false;
                }
                this.readingCompressedMessage = bl4;
                break;
            }
            default: {
                if (!bl3) break;
                throw new ProtocolException("Unexpected rsv1 flag");
            }
        }
        if (bl = (n2 & 0x20) != 0) {
            throw new ProtocolException("Unexpected rsv2 flag");
        }
        if ((n2 = (n2 & 0x10) != 0 ? 1 : 0) != 0) {
            throw new ProtocolException("Unexpected rsv3 flag");
        }
        n2 = Util.and(this.source.readByte(), 255);
        bl = (n2 & 0x80) != 0;
        if (bl == this.isClient) {
            throw new ProtocolException(this.isClient ? "Server-sent frames must not be masked." : "Client-sent frames must be masked.");
        }
        this.frameLength = n2 & 0x7F;
        if (this.frameLength == 126L) {
            this.frameLength = Util.and(this.source.readShort(), 65535);
        } else if (this.frameLength == 127L) {
            this.frameLength = this.source.readLong();
            if (this.frameLength < 0L) {
                throw new ProtocolException("Frame length 0x" + Util.toHexString(this.frameLength) + " > 0x7FFFFFFFFFFFFFFF");
            }
        }
        if (this.isControlFrame && this.frameLength > 125L) {
            throw new ProtocolException("Control frame must be less than 125B.");
        }
        if (bl) {
            Intrinsics.checkNotNull(this.maskKey);
            this.source.readFully(this.maskKey);
        }
    }

    private final void readControlFrame() throws IOException {
        if (this.frameLength > 0L) {
            this.source.readFully(this.controlFrameBuffer, this.frameLength);
            if (!this.isClient) {
                Buffer.UnsafeCursor unsafeCursor = this.maskCursor;
                Intrinsics.checkNotNull(unsafeCursor);
                this.controlFrameBuffer.readAndWriteUnsafe(unsafeCursor);
                this.maskCursor.seek(0L);
                Intrinsics.checkNotNull(this.maskKey);
                WebSocketProtocol.INSTANCE.toggleMask(this.maskCursor, this.maskKey);
                this.maskCursor.close();
            }
        }
        switch (this.opcode) {
            case 9: {
                this.frameCallback.onReadPing(this.controlFrameBuffer.readByteString());
                return;
            }
            case 10: {
                this.frameCallback.onReadPong(this.controlFrameBuffer.readByteString());
                return;
            }
            case 8: {
                int n2 = 1005;
                String string = "";
                long l2 = this.controlFrameBuffer.size();
                if (l2 == 1L) {
                    throw new ProtocolException("Malformed close payload length of 1.");
                }
                if (l2 != 0L) {
                    n2 = this.controlFrameBuffer.readShort();
                    string = this.controlFrameBuffer.readUtf8();
                    String string2 = WebSocketProtocol.INSTANCE.closeCodeExceptionMessage(n2);
                    if (string2 != null) {
                        throw new ProtocolException(string2);
                    }
                }
                this.frameCallback.onReadClose(n2, string);
                this.closed = true;
                return;
            }
        }
        throw new ProtocolException("Unknown control opcode: " + Util.toHexString(this.opcode));
    }

    private final void readMessageFrame() throws IOException {
        int n2 = this.opcode;
        if (n2 != 1 && n2 != 2) {
            throw new ProtocolException("Unknown opcode: " + Util.toHexString(n2));
        }
        this.readMessage();
        if (this.readingCompressedMessage) {
            MessageInflater messageInflater;
            MessageInflater messageInflater2 = this.messageInflater;
            if (messageInflater2 == null) {
                MessageInflater messageInflater3;
                this.messageInflater = messageInflater3 = (messageInflater = new MessageInflater(this.noContextTakeover));
                messageInflater2 = messageInflater;
            }
            messageInflater = messageInflater2;
            messageInflater2.inflate(this.messageFrameBuffer);
        }
        if (n2 == 1) {
            this.frameCallback.onReadMessage(this.messageFrameBuffer.readUtf8());
            return;
        }
        this.frameCallback.onReadMessage(this.messageFrameBuffer.readByteString());
    }

    private final void readUntilNonControlFrame() throws IOException {
        while (!this.closed) {
            this.readHeader();
            if (!this.isControlFrame) break;
            this.readControlFrame();
        }
    }

    private final void readMessage() throws IOException {
        block4: {
            do {
                if (this.closed) {
                    throw new IOException("closed");
                }
                if (this.frameLength > 0L) {
                    this.source.readFully(this.messageFrameBuffer, this.frameLength);
                    if (!this.isClient) {
                        Buffer.UnsafeCursor unsafeCursor = this.maskCursor;
                        Intrinsics.checkNotNull(unsafeCursor);
                        this.messageFrameBuffer.readAndWriteUnsafe(unsafeCursor);
                        this.maskCursor.seek(this.messageFrameBuffer.size() - this.frameLength);
                        Intrinsics.checkNotNull(this.maskKey);
                        WebSocketProtocol.INSTANCE.toggleMask(this.maskCursor, this.maskKey);
                        this.maskCursor.close();
                    }
                }
                if (this.isFinalFrame) break block4;
                this.readUntilNonControlFrame();
            } while (this.opcode == 0);
            throw new ProtocolException("Expected continuation opcode. Got: " + Util.toHexString(this.opcode));
        }
    }

    @Override
    public final void close() throws IOException {
        MessageInflater messageInflater = this.messageInflater;
        if (messageInflater != null) {
            messageInflater.close();
            return;
        }
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H&J\u0010\u0010\b\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\u0007H&J\u0010\u0010\b\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000bH&J\u0010\u0010\f\u001a\u00020\u00032\u0006\u0010\r\u001a\u00020\u000bH&J\u0010\u0010\u000e\u001a\u00020\u00032\u0006\u0010\r\u001a\u00020\u000bH&\u00a8\u0006\u000f"}, d2={"Lokhttp3/internal/ws/WebSocketReader$FrameCallback;", "", "onReadClose", "", "code", "", "reason", "", "onReadMessage", "text", "bytes", "Lokio/ByteString;", "onReadPing", "payload", "onReadPong", "okhttp"})
    public static interface FrameCallback {
        public void onReadMessage(@NotNull String var1) throws IOException;

        public void onReadMessage(@NotNull ByteString var1) throws IOException;

        public void onReadPing(@NotNull ByteString var1);

        public void onReadPong(@NotNull ByteString var1);

        public void onReadClose(int var1, @NotNull String var2);
    }
}


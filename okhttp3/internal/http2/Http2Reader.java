/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.http2;

import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntProgression;
import kotlin.ranges.RangesKt;
import okhttp3.internal.Util;
import okhttp3.internal.http2.ErrorCode;
import okhttp3.internal.http2.Header;
import okhttp3.internal.http2.Hpack;
import okhttp3.internal.http2.Http2;
import okhttp3.internal.http2.Settings;
import okio.Buffer;
import okio.BufferedSource;
import okio.ByteString;
import okio.Source;
import okio.Timeout;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\f\u0018\u0000 #2\u00020\u0001:\u0003#$%B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\b\u0010\u000b\u001a\u00020\fH\u0016J\u0016\u0010\r\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u00052\u0006\u0010\u000f\u001a\u00020\u0010J\u000e\u0010\u0011\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u0010J(\u0010\u0012\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00142\u0006\u0010\u0016\u001a\u00020\u0014H\u0002J(\u0010\u0017\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00142\u0006\u0010\u0016\u001a\u00020\u0014H\u0002J.\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u001a0\u00192\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u001b\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00142\u0006\u0010\u0016\u001a\u00020\u0014H\u0002J(\u0010\u001c\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00142\u0006\u0010\u0016\u001a\u00020\u0014H\u0002J(\u0010\u001d\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00142\u0006\u0010\u0016\u001a\u00020\u0014H\u0002J\u0018\u0010\u001e\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0016\u001a\u00020\u0014H\u0002J(\u0010\u001e\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00142\u0006\u0010\u0016\u001a\u00020\u0014H\u0002J(\u0010\u001f\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00142\u0006\u0010\u0016\u001a\u00020\u0014H\u0002J(\u0010 \u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00142\u0006\u0010\u0016\u001a\u00020\u0014H\u0002J(\u0010!\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00142\u0006\u0010\u0016\u001a\u00020\u0014H\u0002J(\u0010\"\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00142\u0006\u0010\u0016\u001a\u00020\u0014H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006&"}, d2={"Lokhttp3/internal/http2/Http2Reader;", "Ljava/io/Closeable;", "source", "Lokio/BufferedSource;", "client", "", "(Lokio/BufferedSource;Z)V", "continuation", "Lokhttp3/internal/http2/Http2Reader$ContinuationSource;", "hpackReader", "Lokhttp3/internal/http2/Hpack$Reader;", "close", "", "nextFrame", "requireSettings", "handler", "Lokhttp3/internal/http2/Http2Reader$Handler;", "readConnectionPreface", "readData", "length", "", "flags", "streamId", "readGoAway", "readHeaderBlock", "", "Lokhttp3/internal/http2/Header;", "padding", "readHeaders", "readPing", "readPriority", "readPushPromise", "readRstStream", "readSettings", "readWindowUpdate", "Companion", "ContinuationSource", "Handler", "okhttp"})
public final class Http2Reader
implements Closeable {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final BufferedSource source;
    private final boolean client;
    @NotNull
    private final ContinuationSource continuation;
    @NotNull
    private final Hpack.Reader hpackReader;
    @NotNull
    private static final Logger logger;

    public Http2Reader(@NotNull BufferedSource bufferedSource, boolean bl) {
        Intrinsics.checkNotNullParameter(bufferedSource, "");
        this.source = bufferedSource;
        this.client = bl;
        this.continuation = new ContinuationSource(this.source);
        this.hpackReader = new Hpack.Reader(this.continuation, 4096, 0, 4, null);
    }

    public final void readConnectionPreface(@NotNull Handler object) throws IOException {
        Intrinsics.checkNotNullParameter(object, "");
        if (this.client) {
            if (!this.nextFrame(true, (Handler)object)) {
                throw new IOException("Required SETTINGS preface not received");
            }
        } else {
            object = this.source.readByteString(Http2.CONNECTION_PREFACE.size());
            if (logger.isLoggable(Level.FINE)) {
                logger.fine(Util.format("<< CONNECTION " + ((ByteString)object).hex(), new Object[0]));
            }
            if (!Intrinsics.areEqual(Http2.CONNECTION_PREFACE, object)) {
                throw new IOException("Expected a connection header but was " + ((ByteString)object).utf8());
            }
        }
    }

    public final boolean nextFrame(boolean bl, @NotNull Handler handler) throws IOException {
        Intrinsics.checkNotNullParameter(handler, "");
        try {
            this.source.require(9L);
        }
        catch (EOFException eOFException) {
            return false;
        }
        int n2 = Util.readMedium(this.source);
        if (n2 > 16384) {
            throw new IOException("FRAME_SIZE_ERROR: " + n2);
        }
        int n3 = Util.and(this.source.readByte(), 255);
        int n4 = Util.and(this.source.readByte(), 255);
        int n5 = this.source.readInt() & Integer.MAX_VALUE;
        if (logger.isLoggable(Level.FINE)) {
            logger.fine(Http2.INSTANCE.frameLog(true, n5, n2, n3, n4));
        }
        if (bl && n3 != 4) {
            throw new IOException("Expected a SETTINGS frame but was " + Http2.INSTANCE.formattedType$okhttp(n3));
        }
        switch (n3) {
            case 0: {
                this.readData(handler, n2, n4, n5);
                break;
            }
            case 1: {
                this.readHeaders(handler, n2, n4, n5);
                break;
            }
            case 2: {
                this.readPriority(handler, n2, n4, n5);
                break;
            }
            case 3: {
                this.readRstStream(handler, n2, n4, n5);
                break;
            }
            case 4: {
                this.readSettings(handler, n2, n4, n5);
                break;
            }
            case 5: {
                this.readPushPromise(handler, n2, n4, n5);
                break;
            }
            case 6: {
                this.readPing(handler, n2, n4, n5);
                break;
            }
            case 7: {
                this.readGoAway(handler, n2, n4, n5);
                break;
            }
            case 8: {
                this.readWindowUpdate(handler, n2, n4, n5);
                break;
            }
            default: {
                this.source.skip(n2);
            }
        }
        return true;
    }

    private final void readHeaders(Handler handler, int n2, int n3, int n4) throws IOException {
        int n5;
        if (n4 == 0) {
            throw new IOException("PROTOCOL_ERROR: TYPE_HEADERS streamId == 0");
        }
        boolean bl = (n3 & 1) != 0;
        int n6 = n5 = (n3 & 8) != 0 ? Util.and(this.source.readByte(), 255) : 0;
        if ((n3 & 0x20) != 0) {
            this.readPriority(handler, n4);
            n2 -= 5;
        }
        n2 = Companion.lengthWithoutPadding(n2, n3, n5);
        List<Header> list2 = this.readHeaderBlock(n2, n5, n3, n4);
        handler.headers(bl, n4, -1, list2);
    }

    private final List<Header> readHeaderBlock(int n2, int n3, int n4, int n5) throws IOException {
        this.continuation.setLeft(n2);
        this.continuation.setLength(this.continuation.getLeft());
        this.continuation.setPadding(n3);
        this.continuation.setFlags(n4);
        this.continuation.setStreamId(n5);
        this.hpackReader.readHeaders();
        return this.hpackReader.getAndResetHeaderList();
    }

    private final void readData(Handler handler, int n2, int n3, int n4) throws IOException {
        if (n4 == 0) {
            throw new IOException("PROTOCOL_ERROR: TYPE_DATA streamId == 0");
        }
        boolean bl = (n3 & 1) != 0;
        int n5 = (n3 & 0x20) != 0 ? 1 : 0;
        if (n5 != 0) {
            throw new IOException("PROTOCOL_ERROR: FLAG_COMPRESSED without SETTINGS_COMPRESS_DATA");
        }
        n5 = (n3 & 8) != 0 ? Util.and(this.source.readByte(), 255) : 0;
        n2 = Companion.lengthWithoutPadding(n2, n3, n5);
        handler.data(bl, n4, this.source, n2);
        this.source.skip(n5);
    }

    private final void readPriority(Handler handler, int n2, int n3, int n4) throws IOException {
        if (n2 != 5) {
            throw new IOException("TYPE_PRIORITY length: " + n2 + " != 5");
        }
        if (n4 == 0) {
            throw new IOException("TYPE_PRIORITY streamId == 0");
        }
        this.readPriority(handler, n4);
    }

    private final void readPriority(Handler handler, int n2) throws IOException {
        int n3 = this.source.readInt();
        boolean bl = (n3 & Integer.MIN_VALUE) != 0;
        int n4 = Util.and(this.source.readByte(), 255) + 1;
        handler.priority(n2, n3 &= Integer.MAX_VALUE, n4, bl);
    }

    private final void readRstStream(Handler handler, int n2, int n3, int n4) throws IOException {
        if (n2 != 4) {
            throw new IOException("TYPE_RST_STREAM length: " + n2 + " != 4");
        }
        if (n4 == 0) {
            throw new IOException("TYPE_RST_STREAM streamId == 0");
        }
        n2 = this.source.readInt();
        ErrorCode errorCode = ErrorCode.Companion.fromHttp2(n2);
        if (errorCode == null) {
            throw new IOException("TYPE_RST_STREAM unexpected error code: " + n2);
        }
        ErrorCode errorCode2 = errorCode;
        handler.rstStream(n4, errorCode2);
    }

    private final void readSettings(Handler handler, int n2, int n3, int n4) throws IOException {
        if (n4 != 0) {
            throw new IOException("TYPE_SETTINGS streamId != 0");
        }
        if ((n3 & 1) != 0) {
            if (n2 != 0) {
                throw new IOException("FRAME_SIZE_ERROR ack frame should be empty!");
            }
            handler.ackSettings();
            return;
        }
        if (n2 % 6 != 0) {
            throw new IOException("TYPE_SETTINGS length % 6 != 0: " + n2);
        }
        Settings settings = new Settings();
        IntProgression intProgression = RangesKt.step(RangesKt.until(0, n2), 6);
        n4 = intProgression.getFirst();
        int n5 = intProgression.getLast();
        int n6 = intProgression.getStep();
        if (n6 > 0 && n4 <= n5 || n6 < 0 && n5 <= n4) {
            while (true) {
                int n7 = Util.and(this.source.readShort(), 65535);
                int n8 = this.source.readInt();
                switch (n7) {
                    case 1: {
                        break;
                    }
                    case 2: {
                        if (n8 == 0 || n8 == 1) break;
                        throw new IOException("PROTOCOL_ERROR SETTINGS_ENABLE_PUSH != 0 or 1");
                    }
                    case 3: {
                        n7 = 4;
                        break;
                    }
                    case 4: {
                        n7 = 7;
                        if (n8 >= 0) break;
                        throw new IOException("PROTOCOL_ERROR SETTINGS_INITIAL_WINDOW_SIZE > 2^31 - 1");
                    }
                    case 5: {
                        if (n8 >= 16384 && n8 <= 0xFFFFFF) break;
                        throw new IOException("PROTOCOL_ERROR SETTINGS_MAX_FRAME_SIZE: " + n8);
                    }
                }
                settings.set(n7, n8);
                if (n4 == n5) break;
                n4 += n6;
            }
        }
        handler.settings(false, settings);
    }

    private final void readPushPromise(Handler handler, int n2, int n3, int n4) throws IOException {
        if (n4 == 0) {
            throw new IOException("PROTOCOL_ERROR: TYPE_PUSH_PROMISE streamId == 0");
        }
        int n5 = (n3 & 8) != 0 ? Util.and(this.source.readByte(), 255) : 0;
        int n6 = this.source.readInt() & Integer.MAX_VALUE;
        n2 = Companion.lengthWithoutPadding(n2 - 4, n3, n5);
        List<Header> list2 = this.readHeaderBlock(n2, n5, n3, n4);
        handler.pushPromise(n4, n6, list2);
    }

    private final void readPing(Handler handler, int n2, int n3, int n4) throws IOException {
        if (n2 != 8) {
            throw new IOException("TYPE_PING length != 8: " + n2);
        }
        if (n4 != 0) {
            throw new IOException("TYPE_PING streamId != 0");
        }
        n2 = this.source.readInt();
        n4 = this.source.readInt();
        n3 = (n3 & 1) != 0 ? 1 : 0;
        handler.ping(n3 != 0, n2, n4);
    }

    private final void readGoAway(Handler handler, int n2, int n3, int n4) throws IOException {
        if (n2 < 8) {
            throw new IOException("TYPE_GOAWAY length < 8: " + n2);
        }
        if (n4 != 0) {
            throw new IOException("TYPE_GOAWAY streamId != 0");
        }
        n3 = this.source.readInt();
        n4 = this.source.readInt();
        n2 -= 8;
        ErrorCode errorCode = ErrorCode.Companion.fromHttp2(n4);
        if (errorCode == null) {
            throw new IOException("TYPE_GOAWAY unexpected error code: " + n4);
        }
        ErrorCode errorCode2 = errorCode;
        ByteString byteString = ByteString.EMPTY;
        if (n2 > 0) {
            byteString = this.source.readByteString(n2);
        }
        handler.goAway(n3, errorCode2, byteString);
    }

    private final void readWindowUpdate(Handler handler, int n2, int n3, int n4) throws IOException {
        if (n2 != 4) {
            throw new IOException("TYPE_WINDOW_UPDATE length !=4: " + n2);
        }
        long l2 = Util.and(this.source.readInt(), Integer.MAX_VALUE);
        if (l2 == 0L) {
            throw new IOException("windowSizeIncrement was 0");
        }
        handler.windowUpdate(n4, l2);
    }

    @Override
    public final void close() throws IOException {
        this.source.close();
    }

    static {
        Logger logger = Logger.getLogger(Http2.class.getName());
        Intrinsics.checkNotNullExpressionValue(logger, "");
        Http2Reader.logger = logger;
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u001e\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\bR\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\f"}, d2={"Lokhttp3/internal/http2/Http2Reader$Companion;", "", "()V", "logger", "Ljava/util/logging/Logger;", "getLogger", "()Ljava/util/logging/Logger;", "lengthWithoutPadding", "", "length", "flags", "padding", "okhttp"})
    public static final class Companion {
        private Companion() {
        }

        @NotNull
        public final Logger getLogger() {
            return logger;
        }

        public final int lengthWithoutPadding(int n2, int n3, int n4) throws IOException {
            if ((n3 & 8) != 0) {
                --n2;
            }
            if (n4 > n2) {
                throw new IOException("PROTOCOL_ERROR padding " + n4 + " > remaining length " + n2);
            }
            return n2 -= n4;
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0011\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u0017\u001a\u00020\u0018H\u0016J\u0018\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001aH\u0016J\b\u0010\u001e\u001a\u00020\u0018H\u0002J\b\u0010\u001f\u001a\u00020 H\u0016R\u001a\u0010\u0005\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u001a\u0010\u000b\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\b\"\u0004\b\r\u0010\nR\u001a\u0010\u000e\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\b\"\u0004\b\u0010\u0010\nR\u001a\u0010\u0011\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\b\"\u0004\b\u0013\u0010\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0014\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\b\"\u0004\b\u0016\u0010\n\u00a8\u0006!"}, d2={"Lokhttp3/internal/http2/Http2Reader$ContinuationSource;", "Lokio/Source;", "source", "Lokio/BufferedSource;", "(Lokio/BufferedSource;)V", "flags", "", "getFlags", "()I", "setFlags", "(I)V", "left", "getLeft", "setLeft", "length", "getLength", "setLength", "padding", "getPadding", "setPadding", "streamId", "getStreamId", "setStreamId", "close", "", "read", "", "sink", "Lokio/Buffer;", "byteCount", "readContinuationHeader", "timeout", "Lokio/Timeout;", "okhttp"})
    public static final class ContinuationSource
    implements Source {
        @NotNull
        private final BufferedSource source;
        private int length;
        private int flags;
        private int streamId;
        private int left;
        private int padding;

        public ContinuationSource(@NotNull BufferedSource bufferedSource) {
            Intrinsics.checkNotNullParameter(bufferedSource, "");
            this.source = bufferedSource;
        }

        public final int getLength() {
            return this.length;
        }

        public final void setLength(int n2) {
            this.length = n2;
        }

        public final int getFlags() {
            return this.flags;
        }

        public final void setFlags(int n2) {
            this.flags = n2;
        }

        public final int getStreamId() {
            return this.streamId;
        }

        public final void setStreamId(int n2) {
            this.streamId = n2;
        }

        public final int getLeft() {
            return this.left;
        }

        public final void setLeft(int n2) {
            this.left = n2;
        }

        public final int getPadding() {
            return this.padding;
        }

        public final void setPadding(int n2) {
            this.padding = n2;
        }

        @Override
        public final long read(@NotNull Buffer buffer, long l2) throws IOException {
            Intrinsics.checkNotNullParameter(buffer, "");
            while (this.left == 0) {
                this.source.skip(this.padding);
                this.padding = 0;
                if ((this.flags & 4) != 0) {
                    return -1L;
                }
                this.readContinuationHeader();
            }
            long l3 = this.source.read(buffer, Math.min(l2, (long)this.left));
            if (l3 == -1L) {
                return -1L;
            }
            this.left -= (int)l3;
            return l3;
        }

        @Override
        @NotNull
        public final Timeout timeout() {
            return this.source.timeout();
        }

        @Override
        public final void close() throws IOException {
        }

        private final void readContinuationHeader() throws IOException {
            int n2 = this.streamId;
            this.length = this.left = Util.readMedium(this.source);
            int n3 = Util.and(this.source.readByte(), 255);
            this.flags = Util.and(this.source.readByte(), 255);
            if (Companion.getLogger().isLoggable(Level.FINE)) {
                Companion.getLogger().fine(Http2.INSTANCE.frameLog(true, this.streamId, this.length, n3, this.flags));
            }
            this.streamId = this.source.readInt() & Integer.MAX_VALUE;
            if (n3 != 9) {
                throw new IOException(n3 + " != TYPE_CONTINUATION");
            }
            if (this.streamId != n2) {
                throw new IOException("TYPE_CONTINUATION streamId changed");
            }
        }
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000X\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&J8\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\u00062\u0006\u0010\r\u001a\u00020\u000eH&J(\u0010\u000f\u001a\u00020\u00032\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0006H&J \u0010\u0015\u001a\u00020\u00032\u0006\u0010\u0016\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\nH&J.\u0010\u001a\u001a\u00020\u00032\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u001b\u001a\u00020\u00062\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u001e0\u001dH&J \u0010\u001f\u001a\u00020\u00032\u0006\u0010 \u001a\u00020\u00112\u0006\u0010!\u001a\u00020\u00062\u0006\u0010\"\u001a\u00020\u0006H&J(\u0010#\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010$\u001a\u00020\u00062\u0006\u0010%\u001a\u00020\u00062\u0006\u0010&\u001a\u00020\u0011H&J&\u0010'\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010(\u001a\u00020\u00062\f\u0010)\u001a\b\u0012\u0004\u0012\u00020\u001e0\u001dH&J\u0018\u0010*\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0018H&J\u0018\u0010+\u001a\u00020\u00032\u0006\u0010,\u001a\u00020\u00112\u0006\u0010+\u001a\u00020-H&J\u0018\u0010.\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010/\u001a\u00020\u000eH&\u00a8\u00060"}, d2={"Lokhttp3/internal/http2/Http2Reader$Handler;", "", "ackSettings", "", "alternateService", "streamId", "", "origin", "", "protocol", "Lokio/ByteString;", "host", "port", "maxAge", "", "data", "inFinished", "", "source", "Lokio/BufferedSource;", "length", "goAway", "lastGoodStreamId", "errorCode", "Lokhttp3/internal/http2/ErrorCode;", "debugData", "headers", "associatedStreamId", "headerBlock", "", "Lokhttp3/internal/http2/Header;", "ping", "ack", "payload1", "payload2", "priority", "streamDependency", "weight", "exclusive", "pushPromise", "promisedStreamId", "requestHeaders", "rstStream", "settings", "clearPrevious", "Lokhttp3/internal/http2/Settings;", "windowUpdate", "windowSizeIncrement", "okhttp"})
    public static interface Handler {
        public void data(boolean var1, int var2, @NotNull BufferedSource var3, int var4) throws IOException;

        public void headers(boolean var1, int var2, int var3, @NotNull List<Header> var4);

        public void rstStream(int var1, @NotNull ErrorCode var2);

        public void settings(boolean var1, @NotNull Settings var2);

        public void ackSettings();

        public void ping(boolean var1, int var2, int var3);

        public void goAway(int var1, @NotNull ErrorCode var2, @NotNull ByteString var3);

        public void windowUpdate(int var1, long var2);

        public void priority(int var1, int var2, int var3, boolean var4);

        public void pushPromise(int var1, int var2, @NotNull List<Header> var3) throws IOException;

        public void alternateService(int var1, @NotNull String var2, @NotNull ByteString var3, @NotNull String var4, int var5, long var6);
    }
}


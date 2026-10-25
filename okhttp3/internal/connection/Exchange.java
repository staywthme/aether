/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.connection;

import java.io.IOException;
import java.net.ProtocolException;
import java.net.SocketException;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import okhttp3.EventListener;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.connection.ExchangeFinder;
import okhttp3.internal.connection.RealCall;
import okhttp3.internal.connection.RealConnection;
import okhttp3.internal.http.ExchangeCodec;
import okhttp3.internal.http.RealResponseBody;
import okhttp3.internal.ws.RealWebSocket;
import okio.Buffer;
import okio.ForwardingSink;
import okio.ForwardingSource;
import okio.Okio;
import okio.Sink;
import okio.Source;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000z\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001:\u0002CDB%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ7\u0010\u001e\u001a\u0002H\u001f\"\n\b\u0000\u0010\u001f*\u0004\u0018\u00010 2\u0006\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020\u00162\u0006\u0010$\u001a\u00020\u00162\u0006\u0010%\u001a\u0002H\u001f\u00a2\u0006\u0002\u0010&J\u0006\u0010'\u001a\u00020(J\u0016\u0010)\u001a\u00020*2\u0006\u0010+\u001a\u00020,2\u0006\u0010-\u001a\u00020\u0016J\u0006\u0010.\u001a\u00020(J\u0006\u0010/\u001a\u00020(J\u0006\u00100\u001a\u00020(J\u0006\u00101\u001a\u000202J\u0006\u00103\u001a\u00020(J\u0006\u00104\u001a\u00020(J\u000e\u00105\u001a\u0002062\u0006\u00107\u001a\u000208J\u0010\u00109\u001a\u0004\u0018\u00010:2\u0006\u0010;\u001a\u00020\u0016J\u000e\u0010<\u001a\u00020(2\u0006\u00107\u001a\u000208J\u0006\u0010=\u001a\u00020(J\u0010\u0010>\u001a\u00020(2\u0006\u0010%\u001a\u00020 H\u0002J\u0006\u0010?\u001a\u00020@J\u0006\u0010A\u001a\u00020(J\u000e\u0010B\u001a\u00020(2\u0006\u0010+\u001a\u00020,R\u0014\u0010\u0002\u001a\u00020\u0003X\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\u00020\u000eX\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0014\u0010\u0004\u001a\u00020\u0005X\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0014\u0010\u0006\u001a\u00020\u0007X\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u001e\u0010\u0017\u001a\u00020\u00162\u0006\u0010\u0015\u001a\u00020\u0016@BX\u0080\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0014\u0010\u001a\u001a\u00020\u00168@X\u0080\u0004\u00a2\u0006\u0006\u001a\u0004\b\u001b\u0010\u0019R\u001e\u0010\u001c\u001a\u00020\u00162\u0006\u0010\u0015\u001a\u00020\u0016@BX\u0080\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0019\u00a8\u0006E"}, d2={"Lokhttp3/internal/connection/Exchange;", "", "call", "Lokhttp3/internal/connection/RealCall;", "eventListener", "Lokhttp3/EventListener;", "finder", "Lokhttp3/internal/connection/ExchangeFinder;", "codec", "Lokhttp3/internal/http/ExchangeCodec;", "(Lokhttp3/internal/connection/RealCall;Lokhttp3/EventListener;Lokhttp3/internal/connection/ExchangeFinder;Lokhttp3/internal/http/ExchangeCodec;)V", "getCall$okhttp", "()Lokhttp3/internal/connection/RealCall;", "connection", "Lokhttp3/internal/connection/RealConnection;", "getConnection$okhttp", "()Lokhttp3/internal/connection/RealConnection;", "getEventListener$okhttp", "()Lokhttp3/EventListener;", "getFinder$okhttp", "()Lokhttp3/internal/connection/ExchangeFinder;", "<set-?>", "", "hasFailure", "getHasFailure$okhttp", "()Z", "isCoalescedConnection", "isCoalescedConnection$okhttp", "isDuplex", "isDuplex$okhttp", "bodyComplete", "E", "Ljava/io/IOException;", "bytesRead", "", "responseDone", "requestDone", "e", "(JZZLjava/io/IOException;)Ljava/io/IOException;", "cancel", "", "createRequestBody", "Lokio/Sink;", "request", "Lokhttp3/Request;", "duplex", "detachWithViolence", "finishRequest", "flushRequest", "newWebSocketStreams", "Lokhttp3/internal/ws/RealWebSocket$Streams;", "noNewExchangesOnConnection", "noRequestBody", "openResponseBody", "Lokhttp3/ResponseBody;", "response", "Lokhttp3/Response;", "readResponseHeaders", "Lokhttp3/Response$Builder;", "expectContinue", "responseHeadersEnd", "responseHeadersStart", "trackFailure", "trailers", "Lokhttp3/Headers;", "webSocketUpgradeFailed", "writeRequestHeaders", "RequestBodySink", "ResponseBodySource", "okhttp"})
public final class Exchange {
    @NotNull
    private final RealCall call;
    @NotNull
    private final EventListener eventListener;
    @NotNull
    private final ExchangeFinder finder;
    @NotNull
    private final ExchangeCodec codec;
    private boolean isDuplex;
    private boolean hasFailure;
    @NotNull
    private final RealConnection connection;

    public Exchange(@NotNull RealCall realCall, @NotNull EventListener eventListener, @NotNull ExchangeFinder exchangeFinder, @NotNull ExchangeCodec exchangeCodec) {
        Intrinsics.checkNotNullParameter(realCall, "");
        Intrinsics.checkNotNullParameter(eventListener, "");
        Intrinsics.checkNotNullParameter(exchangeFinder, "");
        Intrinsics.checkNotNullParameter(exchangeCodec, "");
        this.call = realCall;
        this.eventListener = eventListener;
        this.finder = exchangeFinder;
        this.codec = exchangeCodec;
        this.connection = this.codec.getConnection();
    }

    @NotNull
    public final RealCall getCall$okhttp() {
        return this.call;
    }

    @NotNull
    public final EventListener getEventListener$okhttp() {
        return this.eventListener;
    }

    @NotNull
    public final ExchangeFinder getFinder$okhttp() {
        return this.finder;
    }

    public final boolean isDuplex$okhttp() {
        return this.isDuplex;
    }

    public final boolean getHasFailure$okhttp() {
        return this.hasFailure;
    }

    @NotNull
    public final RealConnection getConnection$okhttp() {
        return this.connection;
    }

    public final boolean isCoalescedConnection$okhttp() {
        return !Intrinsics.areEqual(this.finder.getAddress$okhttp().url().host(), this.connection.route().address().url().host());
    }

    public final void writeRequestHeaders(@NotNull Request request) throws IOException {
        Intrinsics.checkNotNullParameter(request, "");
        try {
            this.eventListener.requestHeadersStart(this.call);
            this.codec.writeRequestHeaders(request);
            this.eventListener.requestHeadersEnd(this.call, request);
            return;
        }
        catch (IOException iOException) {
            this.eventListener.requestFailed(this.call, iOException);
            this.trackFailure(iOException);
            throw iOException;
        }
    }

    @NotNull
    public final Sink createRequestBody(@NotNull Request object, boolean bl) throws IOException {
        Intrinsics.checkNotNullParameter(object, "");
        this.isDuplex = bl;
        RequestBody requestBody = ((Request)object).body();
        Intrinsics.checkNotNull(requestBody);
        long l2 = requestBody.contentLength();
        this.eventListener.requestBodyStart(this.call);
        object = this.codec.createRequestBody((Request)object, l2);
        return new RequestBodySink((Sink)object, l2);
    }

    public final void flushRequest() throws IOException {
        try {
            this.codec.flushRequest();
            return;
        }
        catch (IOException iOException) {
            this.eventListener.requestFailed(this.call, iOException);
            this.trackFailure(iOException);
            throw iOException;
        }
    }

    public final void finishRequest() throws IOException {
        try {
            this.codec.finishRequest();
            return;
        }
        catch (IOException iOException) {
            this.eventListener.requestFailed(this.call, iOException);
            this.trackFailure(iOException);
            throw iOException;
        }
    }

    public final void responseHeadersStart() {
        this.eventListener.responseHeadersStart(this.call);
    }

    @Nullable
    public final Response.Builder readResponseHeaders(boolean bl) throws IOException {
        try {
            Response.Builder builder = this.codec.readResponseHeaders(bl);
            if (builder != null) {
                builder.initExchange$okhttp(this);
            }
            return builder;
        }
        catch (IOException iOException) {
            this.eventListener.responseFailed(this.call, iOException);
            this.trackFailure(iOException);
            throw iOException;
        }
    }

    public final void responseHeadersEnd(@NotNull Response response) {
        Intrinsics.checkNotNullParameter(response, "");
        this.eventListener.responseHeadersEnd(this.call, response);
    }

    @NotNull
    public final ResponseBody openResponseBody(@NotNull Response closeable) throws IOException {
        Intrinsics.checkNotNullParameter(closeable, "");
        try {
            String string = Response.header$default(closeable, "Content-Type", null, 2, null);
            long l2 = this.codec.reportedContentLength((Response)closeable);
            closeable = this.codec.openResponseBodySource((Response)closeable);
            closeable = new ResponseBodySource((Source)closeable, l2);
            return new RealResponseBody(string, l2, Okio.buffer((Source)closeable));
        }
        catch (IOException iOException) {
            this.eventListener.responseFailed(this.call, iOException);
            this.trackFailure(iOException);
            throw iOException;
        }
    }

    @NotNull
    public final Headers trailers() throws IOException {
        return this.codec.trailers();
    }

    @NotNull
    public final RealWebSocket.Streams newWebSocketStreams() throws SocketException {
        this.call.timeoutEarlyExit();
        return this.codec.getConnection().newWebSocketStreams$okhttp(this);
    }

    public final void webSocketUpgradeFailed() {
        this.bodyComplete(-1L, true, true, null);
    }

    public final void noNewExchangesOnConnection() {
        this.codec.getConnection().noNewExchanges$okhttp();
    }

    public final void cancel() {
        this.codec.cancel();
    }

    public final void detachWithViolence() {
        this.codec.cancel();
        this.call.messageDone$okhttp(this, true, true, null);
    }

    private final void trackFailure(IOException iOException) {
        this.hasFailure = true;
        this.finder.trackFailure(iOException);
        this.codec.getConnection().trackFailure$okhttp(this.call, iOException);
    }

    public final <E extends IOException> E bodyComplete(long l2, boolean bl, boolean bl2, E e2) {
        if (e2 != null) {
            this.trackFailure(e2);
        }
        if (bl2) {
            if (e2 != null) {
                this.eventListener.requestFailed(this.call, e2);
            } else {
                this.eventListener.requestBodyEnd(this.call, l2);
            }
        }
        if (bl) {
            if (e2 != null) {
                this.eventListener.responseFailed(this.call, e2);
            } else {
                this.eventListener.responseBodyEnd(this.call, l2);
            }
        }
        return this.call.messageDone$okhttp(this, bl2, bl, e2);
    }

    public final void noRequestBody() {
        this.call.messageDone$okhttp(this, true, false, null);
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0082\u0004\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\b\u0010\u000b\u001a\u00020\fH\u0016J!\u0010\r\u001a\u0002H\u000e\"\n\b\u0000\u0010\u000e*\u0004\u0018\u00010\u000f2\u0006\u0010\u0010\u001a\u0002H\u000eH\u0002\u00a2\u0006\u0002\u0010\u0011J\b\u0010\u0012\u001a\u00020\fH\u0016J\u0018\u0010\u0013\u001a\u00020\f2\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0005H\u0016R\u000e\u0010\u0007\u001a\u00020\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2={"Lokhttp3/internal/connection/Exchange$RequestBodySink;", "Lokio/ForwardingSink;", "delegate", "Lokio/Sink;", "contentLength", "", "(Lokhttp3/internal/connection/Exchange;Lokio/Sink;J)V", "bytesReceived", "closed", "", "completed", "close", "", "complete", "E", "Ljava/io/IOException;", "e", "(Ljava/io/IOException;)Ljava/io/IOException;", "flush", "write", "source", "Lokio/Buffer;", "byteCount", "okhttp"})
    @SourceDebugExtension(value={"SMAP\nExchange.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Exchange.kt\nokhttp3/internal/connection/Exchange$RequestBodySink\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,333:1\n1#2:334\n*E\n"})
    final class RequestBodySink
    extends ForwardingSink {
        private final long contentLength;
        private boolean completed;
        private long bytesReceived;
        private boolean closed;

        public RequestBodySink(Sink sink2, long l2) {
            Intrinsics.checkNotNullParameter(sink2, "");
            super(sink2);
            this.contentLength = l2;
        }

        @Override
        public final void write(@NotNull Buffer object, long l2) throws IOException {
            Intrinsics.checkNotNullParameter(object, "");
            if (!(!this.closed)) {
                object = "closed";
                throw new IllegalStateException(object.toString());
            }
            if (this.contentLength != -1L && this.bytesReceived + l2 > this.contentLength) {
                throw new ProtocolException("expected " + this.contentLength + " bytes but received " + (this.bytesReceived + l2));
            }
            try {
                super.write((Buffer)object, l2);
                this.bytesReceived += l2;
                return;
            }
            catch (IOException iOException) {
                throw this.complete(iOException);
            }
        }

        @Override
        public final void flush() throws IOException {
            try {
                super.flush();
                return;
            }
            catch (IOException iOException) {
                throw this.complete(iOException);
            }
        }

        @Override
        public final void close() throws IOException {
            if (this.closed) {
                return;
            }
            this.closed = true;
            if (this.contentLength != -1L && this.bytesReceived != this.contentLength) {
                throw new ProtocolException("unexpected end of stream");
            }
            try {
                super.close();
                this.complete(null);
                return;
            }
            catch (IOException iOException) {
                throw this.complete(iOException);
            }
        }

        private final <E extends IOException> E complete(E e2) {
            if (this.completed) {
                return e2;
            }
            this.completed = true;
            return Exchange.this.bodyComplete(this.bytesReceived, false, true, e2);
        }
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0080\u0004\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\b\u0010\f\u001a\u00020\rH\u0016J\u001f\u0010\u000e\u001a\u0002H\u000f\"\n\b\u0000\u0010\u000f*\u0004\u0018\u00010\u00102\u0006\u0010\u0011\u001a\u0002H\u000f\u00a2\u0006\u0002\u0010\u0012J\u0018\u0010\u0013\u001a\u00020\u00052\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0005H\u0016R\u000e\u0010\u0007\u001a\u00020\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2={"Lokhttp3/internal/connection/Exchange$ResponseBodySource;", "Lokio/ForwardingSource;", "delegate", "Lokio/Source;", "contentLength", "", "(Lokhttp3/internal/connection/Exchange;Lokio/Source;J)V", "bytesReceived", "closed", "", "completed", "invokeStartEvent", "close", "", "complete", "E", "Ljava/io/IOException;", "e", "(Ljava/io/IOException;)Ljava/io/IOException;", "read", "sink", "Lokio/Buffer;", "byteCount", "okhttp"})
    @SourceDebugExtension(value={"SMAP\nExchange.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Exchange.kt\nokhttp3/internal/connection/Exchange$ResponseBodySource\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,333:1\n1#2:334\n*E\n"})
    public final class ResponseBodySource
    extends ForwardingSource {
        private final long contentLength;
        private long bytesReceived;
        private boolean invokeStartEvent;
        private boolean completed;
        private boolean closed;

        public ResponseBodySource(Source source2, long l2) {
            Intrinsics.checkNotNullParameter(source2, "");
            super(source2);
            this.contentLength = l2;
            this.invokeStartEvent = true;
            if (this.contentLength == 0L) {
                this.complete(null);
            }
        }

        @Override
        public final long read(@NotNull Buffer buffer, long l2) throws IOException {
            Intrinsics.checkNotNullParameter(buffer, "");
            if (!(!this.closed)) {
                String string = "closed";
                throw new IllegalStateException(string.toString());
            }
            try {
                long l3 = this.delegate().read(buffer, l2);
                if (this.invokeStartEvent) {
                    this.invokeStartEvent = false;
                    Exchange.this.getEventListener$okhttp().responseBodyStart(Exchange.this.getCall$okhttp());
                }
                if (l3 == -1L) {
                    this.complete(null);
                    return -1L;
                }
                long l4 = this.bytesReceived + l3;
                if (this.contentLength != -1L && l4 > this.contentLength) {
                    throw new ProtocolException("expected " + this.contentLength + " bytes but received " + l4);
                }
                this.bytesReceived = l4;
                if (l4 == this.contentLength) {
                    this.complete(null);
                }
                return l3;
            }
            catch (IOException iOException) {
                throw this.complete(iOException);
            }
        }

        @Override
        public final void close() throws IOException {
            if (this.closed) {
                return;
            }
            this.closed = true;
            try {
                super.close();
                this.complete(null);
                return;
            }
            catch (IOException iOException) {
                throw this.complete(iOException);
            }
        }

        public final <E extends IOException> E complete(E e2) {
            if (this.completed) {
                return e2;
            }
            this.completed = true;
            if (e2 == null && this.invokeStartEvent) {
                this.invokeStartEvent = false;
                Exchange.this.getEventListener$okhttp().responseBodyStart(Exchange.this.getCall$okhttp());
            }
            return Exchange.this.bodyComplete(this.bytesReceived, true, false, e2);
        }
    }
}


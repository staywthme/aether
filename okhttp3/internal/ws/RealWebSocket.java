/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.ws;

import java.io.Closeable;
import java.io.IOException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.ranges.IntRange;
import kotlin.text.StringsKt;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.EventListener;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.internal.Util;
import okhttp3.internal.concurrent.Task;
import okhttp3.internal.concurrent.TaskQueue;
import okhttp3.internal.concurrent.TaskRunner;
import okhttp3.internal.connection.Exchange;
import okhttp3.internal.connection.RealCall;
import okhttp3.internal.ws.WebSocketExtensions;
import okhttp3.internal.ws.WebSocketProtocol;
import okhttp3.internal.ws.WebSocketReader;
import okhttp3.internal.ws.WebSocketWriter;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\u00b6\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u001c\u0018\u0000 `2\u00020\u00012\u00020\u0002:\u0005_`abcB?\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\f\u0012\b\u0010\r\u001a\u0004\u0018\u00010\u000e\u0012\u0006\u0010\u000f\u001a\u00020\f\u00a2\u0006\u0002\u0010\u0010J\u0016\u00102\u001a\u0002032\u0006\u00104\u001a\u00020\f2\u0006\u00105\u001a\u000206J\b\u00107\u001a\u000203H\u0016J\u001f\u00108\u001a\u0002032\u0006\u00109\u001a\u00020:2\b\u0010;\u001a\u0004\u0018\u00010<H\u0000\u00a2\u0006\u0002\b=J\u001a\u0010>\u001a\u00020\u00122\u0006\u0010?\u001a\u00020%2\b\u0010@\u001a\u0004\u0018\u00010\u0018H\u0016J \u0010>\u001a\u00020\u00122\u0006\u0010?\u001a\u00020%2\b\u0010@\u001a\u0004\u0018\u00010\u00182\u0006\u0010A\u001a\u00020\fJ\u000e\u0010B\u001a\u0002032\u0006\u0010C\u001a\u00020DJ\u001c\u0010E\u001a\u0002032\n\u0010F\u001a\u00060Gj\u0002`H2\b\u00109\u001a\u0004\u0018\u00010:J\u0016\u0010I\u001a\u0002032\u0006\u0010\u001e\u001a\u00020\u00182\u0006\u0010*\u001a\u00020+J\u0006\u0010J\u001a\u000203J\u0018\u0010K\u001a\u0002032\u0006\u0010?\u001a\u00020%2\u0006\u0010@\u001a\u00020\u0018H\u0016J\u0010\u0010L\u001a\u0002032\u0006\u0010M\u001a\u00020\u0018H\u0016J\u0010\u0010L\u001a\u0002032\u0006\u0010N\u001a\u00020 H\u0016J\u0010\u0010O\u001a\u0002032\u0006\u0010P\u001a\u00020 H\u0016J\u0010\u0010Q\u001a\u0002032\u0006\u0010P\u001a\u00020 H\u0016J\u000e\u0010R\u001a\u00020\u00122\u0006\u0010P\u001a\u00020 J\u0006\u0010S\u001a\u00020\u0012J\b\u0010!\u001a\u00020\fH\u0016J\u0006\u0010'\u001a\u00020%J\u0006\u0010(\u001a\u00020%J\b\u0010T\u001a\u00020\u0006H\u0016J\b\u0010U\u001a\u000203H\u0002J\u0010\u0010V\u001a\u00020\u00122\u0006\u0010M\u001a\u00020\u0018H\u0016J\u0010\u0010V\u001a\u00020\u00122\u0006\u0010N\u001a\u00020 H\u0016J\u0018\u0010V\u001a\u00020\u00122\u0006\u0010W\u001a\u00020 2\u0006\u0010X\u001a\u00020%H\u0002J\u0006\u0010)\u001a\u00020%J\u0006\u0010Y\u001a\u000203J\r\u0010Z\u001a\u00020\u0012H\u0000\u00a2\u0006\u0002\b[J\r\u0010\\\u001a\u000203H\u0000\u00a2\u0006\u0002\b]J\f\u0010^\u001a\u00020\u0012*\u00020\u000eH\u0002R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0014X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\u00020\bX\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u0014\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u001d0\u001cX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u001e\u001a\u0004\u0018\u00010\u0018X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020 0\u001cX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\"\u001a\u0004\u0018\u00010#X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020%X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010&\u001a\u0004\u0018\u00010\u0018X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010'\u001a\u00020%X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020%X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020%X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010*\u001a\u0004\u0018\u00010+X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010,\u001a\u00020-X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010.\u001a\u0004\u0018\u00010/X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u00100\u001a\u0004\u0018\u000101X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006d"}, d2={"Lokhttp3/internal/ws/RealWebSocket;", "Lokhttp3/WebSocket;", "Lokhttp3/internal/ws/WebSocketReader$FrameCallback;", "taskRunner", "Lokhttp3/internal/concurrent/TaskRunner;", "originalRequest", "Lokhttp3/Request;", "listener", "Lokhttp3/WebSocketListener;", "random", "Ljava/util/Random;", "pingIntervalMillis", "", "extensions", "Lokhttp3/internal/ws/WebSocketExtensions;", "minimumDeflateSize", "(Lokhttp3/internal/concurrent/TaskRunner;Lokhttp3/Request;Lokhttp3/WebSocketListener;Ljava/util/Random;JLokhttp3/internal/ws/WebSocketExtensions;J)V", "awaitingPong", "", "call", "Lokhttp3/Call;", "enqueuedClose", "failed", "key", "", "getListener$okhttp", "()Lokhttp3/WebSocketListener;", "messageAndCloseQueue", "Ljava/util/ArrayDeque;", "", "name", "pongQueue", "Lokio/ByteString;", "queueSize", "reader", "Lokhttp3/internal/ws/WebSocketReader;", "receivedCloseCode", "", "receivedCloseReason", "receivedPingCount", "receivedPongCount", "sentPingCount", "streams", "Lokhttp3/internal/ws/RealWebSocket$Streams;", "taskQueue", "Lokhttp3/internal/concurrent/TaskQueue;", "writer", "Lokhttp3/internal/ws/WebSocketWriter;", "writerTask", "Lokhttp3/internal/concurrent/Task;", "awaitTermination", "", "timeout", "timeUnit", "Ljava/util/concurrent/TimeUnit;", "cancel", "checkUpgradeSuccess", "response", "Lokhttp3/Response;", "exchange", "Lokhttp3/internal/connection/Exchange;", "checkUpgradeSuccess$okhttp", "close", "code", "reason", "cancelAfterCloseMillis", "connect", "client", "Lokhttp3/OkHttpClient;", "failWebSocket", "e", "Ljava/lang/Exception;", "Lkotlin/Exception;", "initReaderAndWriter", "loopReader", "onReadClose", "onReadMessage", "text", "bytes", "onReadPing", "payload", "onReadPong", "pong", "processNextFrame", "request", "runWriter", "send", "data", "formatOpcode", "tearDown", "writeOneFrame", "writeOneFrame$okhttp", "writePingFrame", "writePingFrame$okhttp", "isValid", "Close", "Companion", "Message", "Streams", "WriterTask", "okhttp"})
@SourceDebugExtension(value={"SMAP\nRealWebSocket.kt\nKotlin\n*S Kotlin\n*F\n+ 1 RealWebSocket.kt\nokhttp3/internal/ws/RealWebSocket\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 TaskQueue.kt\nokhttp3/internal/concurrent/TaskQueue\n+ 4 Util.kt\nokhttp3/internal/Util\n*L\n1#1,654:1\n1#2:655\n84#3,4:656\n90#3,13:664\n608#4,4:660\n*S KotlinDebug\n*F\n+ 1 RealWebSocket.kt\nokhttp3/internal/ws/RealWebSocket\n*L\n269#1:656,4\n512#1:664,13\n457#1:660,4\n*E\n"})
public final class RealWebSocket
implements WebSocket,
WebSocketReader.FrameCallback {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final Request originalRequest;
    @NotNull
    private final WebSocketListener listener;
    @NotNull
    private final Random random;
    private final long pingIntervalMillis;
    @Nullable
    private WebSocketExtensions extensions;
    private long minimumDeflateSize;
    @NotNull
    private final String key;
    @Nullable
    private Call call;
    @Nullable
    private Task writerTask;
    @Nullable
    private WebSocketReader reader;
    @Nullable
    private WebSocketWriter writer;
    @NotNull
    private TaskQueue taskQueue;
    @Nullable
    private String name;
    @Nullable
    private Streams streams;
    @NotNull
    private final ArrayDeque<ByteString> pongQueue;
    @NotNull
    private final ArrayDeque<Object> messageAndCloseQueue;
    private long queueSize;
    private boolean enqueuedClose;
    private int receivedCloseCode;
    @Nullable
    private String receivedCloseReason;
    private boolean failed;
    private int sentPingCount;
    private int receivedPingCount;
    private int receivedPongCount;
    private boolean awaitingPong;
    @NotNull
    private static final List<Protocol> ONLY_HTTP1 = CollectionsKt.listOf(Protocol.HTTP_1_1);
    private static final long MAX_QUEUE_SIZE = 0x1000000L;
    private static final long CANCEL_AFTER_CLOSE_MILLIS = 60000L;
    public static final long DEFAULT_MINIMUM_DEFLATE_SIZE = 1024L;

    public RealWebSocket(@NotNull TaskRunner object, @NotNull Request object2, @NotNull WebSocketListener object3, @NotNull Random object4, long l2, @Nullable WebSocketExtensions webSocketExtensions, long l3) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(object2, "");
        Intrinsics.checkNotNullParameter(object3, "");
        Intrinsics.checkNotNullParameter(object4, "");
        this.originalRequest = object2;
        this.listener = object3;
        this.random = object4;
        this.pingIntervalMillis = l2;
        this.extensions = webSocketExtensions;
        this.minimumDeflateSize = l3;
        this.taskQueue = ((TaskRunner)object).newQueue();
        this.pongQueue = new ArrayDeque();
        this.messageAndCloseQueue = new ArrayDeque();
        this.receivedCloseCode = -1;
        if (!Intrinsics.areEqual("GET", this.originalRequest.method())) {
            object2 = "Request must be GET: " + this.originalRequest.method();
            throw new IllegalArgumentException(object2.toString());
        }
        byte[] byArray = new byte[16];
        object = byArray;
        object2 = byArray;
        object4 = ByteString.Companion;
        object3 = this;
        this.random.nextBytes((byte[])object2);
        ((RealWebSocket)object3).key = ByteString.Companion.of$default((ByteString.Companion)object4, (byte[])object, 0, 0, 3, null).base64();
    }

    @NotNull
    public final WebSocketListener getListener$okhttp() {
        return this.listener;
    }

    @Override
    @NotNull
    public final Request request() {
        return this.originalRequest;
    }

    @Override
    public final synchronized long queueSize() {
        return this.queueSize;
    }

    @Override
    public final void cancel() {
        Call call = this.call;
        Intrinsics.checkNotNull(call);
        call.cancel();
    }

    public final void connect(@NotNull OkHttpClient okHttpClient) {
        Intrinsics.checkNotNullParameter(okHttpClient, "");
        if (this.originalRequest.header("Sec-WebSocket-Extensions") != null) {
            this.failWebSocket(new ProtocolException("Request header not permitted: 'Sec-WebSocket-Extensions'"), null);
            return;
        }
        okHttpClient = okHttpClient.newBuilder().eventListener(EventListener.NONE).protocols(ONLY_HTTP1).build();
        Request request = this.originalRequest.newBuilder().header("Upgrade", "websocket").header("Connection", "Upgrade").header("Sec-WebSocket-Key", this.key).header("Sec-WebSocket-Version", "13").header("Sec-WebSocket-Extensions", "permessage-deflate").build();
        Call call = this.call = (Call)new RealCall(okHttpClient, request, true);
        Intrinsics.checkNotNull(call);
        call.enqueue(new Callback(this, request){
            final /* synthetic */ RealWebSocket this$0;
            final /* synthetic */ Request $request;
            {
                this.this$0 = realWebSocket;
                this.$request = request;
            }

            public final void onResponse(@NotNull Call object, @NotNull Response response) {
                Streams streams;
                Intrinsics.checkNotNullParameter(object, "");
                Intrinsics.checkNotNullParameter(response, "");
                object = response.exchange();
                try {
                    this.this$0.checkUpgradeSuccess$okhttp(response, (Exchange)object);
                    Object object2 = object;
                    Intrinsics.checkNotNull(object2);
                    streams = ((Exchange)object2).newWebSocketStreams();
                }
                catch (IOException iOException) {
                    this.this$0.failWebSocket(iOException, response);
                    Util.closeQuietly(response);
                    Object object3 = object;
                    if (object3 != null) {
                        ((Exchange)object3).webSocketUpgradeFailed();
                        return;
                    }
                    return;
                }
                Object object4 = WebSocketExtensions.Companion.parse(response.headers());
                RealWebSocket.access$setExtensions$p(this.this$0, (WebSocketExtensions)object4);
                if (!RealWebSocket.access$isValid(this.this$0, (WebSocketExtensions)object4)) {
                    object = this.this$0;
                    object4 = this.this$0;
                    synchronized (object) {
                        RealWebSocket.access$getMessageAndCloseQueue$p((RealWebSocket)object4).clear();
                        ((RealWebSocket)object4).close(1010, "unexpected Sec-WebSocket-Extensions in response header");
                    }
                }
                try {
                    object = Util.okHttpName + " WebSocket " + this.$request.url().redact();
                    this.this$0.initReaderAndWriter((String)object, streams);
                    this.this$0.getListener$okhttp().onOpen(this.this$0, response);
                    this.this$0.loopReader();
                    return;
                }
                catch (Exception exception) {
                    this.this$0.failWebSocket(exception, null);
                    return;
                }
            }

            public final void onFailure(@NotNull Call call, @NotNull IOException iOException) {
                Intrinsics.checkNotNullParameter(call, "");
                Intrinsics.checkNotNullParameter(iOException, "");
                this.this$0.failWebSocket(iOException, null);
            }
        });
    }

    private final boolean isValid(WebSocketExtensions webSocketExtensions) {
        if (webSocketExtensions.unknownValues) {
            return false;
        }
        if (webSocketExtensions.clientMaxWindowBits != null) {
            return false;
        }
        return webSocketExtensions.serverMaxWindowBits == null || new IntRange(8, 15).contains(webSocketExtensions.serverMaxWindowBits);
    }

    public final void checkUpgradeSuccess$okhttp(@NotNull Response object, @Nullable Exchange exchange) throws IOException {
        Intrinsics.checkNotNullParameter(object, "");
        if (((Response)object).code() != 101) {
            throw new ProtocolException("Expected HTTP 101 response but was '" + ((Response)object).code() + ' ' + ((Response)object).message() + '\'');
        }
        String string = Response.header$default((Response)object, "Connection", null, 2, null);
        if (!StringsKt.equals("Upgrade", string, true)) {
            throw new ProtocolException("Expected 'Connection' header value 'Upgrade' but was '" + string + '\'');
        }
        string = Response.header$default((Response)object, "Upgrade", null, 2, null);
        if (!StringsKt.equals("websocket", string, true)) {
            throw new ProtocolException("Expected 'Upgrade' header value 'websocket' but was '" + string + '\'');
        }
        object = Response.header$default((Response)object, "Sec-WebSocket-Accept", null, 2, null);
        string = ByteString.Companion.encodeUtf8(this.key + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").sha1().base64();
        if (!Intrinsics.areEqual(string, object)) {
            throw new ProtocolException("Expected 'Sec-WebSocket-Accept' header value '" + string + "' but was '" + (String)object + '\'');
        }
        if (exchange == null) {
            throw new ProtocolException("Web Socket exchange missing: bad interceptor?");
        }
    }

    public final void initReaderAndWriter(@NotNull String string, @NotNull Streams streams) throws IOException {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(streams, "");
        WebSocketExtensions webSocketExtensions = this.extensions;
        Intrinsics.checkNotNull(webSocketExtensions);
        WebSocketExtensions webSocketExtensions2 = webSocketExtensions;
        RealWebSocket realWebSocket = this;
        synchronized (realWebSocket) {
            this.name = string;
            this.streams = streams;
            this.writer = new WebSocketWriter(streams.getClient(), streams.getSink(), this.random, webSocketExtensions2.perMessageDeflate, webSocketExtensions2.noContextTakeover(streams.getClient()), this.minimumDeflateSize);
            this.writerTask = new WriterTask();
            if (this.pingIntervalMillis != 0L) {
                long l2 = TimeUnit.MILLISECONDS.toNanos(this.pingIntervalMillis);
                TaskQueue taskQueue = this.taskQueue;
                string = string + " ping";
                taskQueue.schedule(new Task(string, this, l2){
                    final /* synthetic */ RealWebSocket this$0;
                    final /* synthetic */ long $pingIntervalNanos$inlined;
                    {
                        this.this$0 = realWebSocket;
                        this.$pingIntervalNanos$inlined = l2;
                        super(string, false, 2, null);
                    }

                    public final long runOnce() {
                        this.this$0.writePingFrame$okhttp();
                        return this.$pingIntervalNanos$inlined;
                    }
                }, l2);
            }
            if (!((Collection)this.messageAndCloseQueue).isEmpty()) {
                this.runWriter();
            }
        }
        this.reader = new WebSocketReader(streams.getClient(), streams.getSource(), this, webSocketExtensions2.perMessageDeflate, webSocketExtensions2.noContextTakeover(!streams.getClient()));
    }

    public final void loopReader() throws IOException {
        while (this.receivedCloseCode == -1) {
            WebSocketReader webSocketReader = this.reader;
            Intrinsics.checkNotNull(webSocketReader);
            webSocketReader.processNextFrame();
        }
    }

    public final boolean processNextFrame() throws IOException {
        boolean bl;
        try {
            WebSocketReader webSocketReader = this.reader;
            Intrinsics.checkNotNull(webSocketReader);
            webSocketReader.processNextFrame();
            bl = this.receivedCloseCode == -1;
        }
        catch (Exception exception) {
            this.failWebSocket(exception, null);
            bl = false;
        }
        return bl;
    }

    public final void awaitTermination(long l2, @NotNull TimeUnit timeUnit) throws InterruptedException {
        Intrinsics.checkNotNullParameter((Object)timeUnit, "");
        this.taskQueue.idleLatch().await(l2, timeUnit);
    }

    public final void tearDown() throws InterruptedException {
        this.taskQueue.shutdown();
        this.taskQueue.idleLatch().await(10L, TimeUnit.SECONDS);
    }

    public final synchronized int sentPingCount() {
        return this.sentPingCount;
    }

    public final synchronized int receivedPingCount() {
        return this.receivedPingCount;
    }

    public final synchronized int receivedPongCount() {
        return this.receivedPongCount;
    }

    @Override
    public final void onReadMessage(@NotNull String string) throws IOException {
        Intrinsics.checkNotNullParameter(string, "");
        this.listener.onMessage((WebSocket)this, string);
    }

    @Override
    public final void onReadMessage(@NotNull ByteString byteString) throws IOException {
        Intrinsics.checkNotNullParameter(byteString, "");
        this.listener.onMessage((WebSocket)this, byteString);
    }

    @Override
    public final synchronized void onReadPing(@NotNull ByteString byteString) {
        Intrinsics.checkNotNullParameter(byteString, "");
        if (this.failed || this.enqueuedClose && this.messageAndCloseQueue.isEmpty()) {
            return;
        }
        this.pongQueue.add(byteString);
        this.runWriter();
        int n2 = this.receivedPingCount;
        this.receivedPingCount = n2 + 1;
    }

    @Override
    public final synchronized void onReadPong(@NotNull ByteString byteString) {
        Intrinsics.checkNotNullParameter(byteString, "");
        int n2 = this.receivedPongCount;
        this.receivedPongCount = n2 + 1;
        this.awaitingPong = false;
    }

    @Override
    public final void onReadClose(int n2, @NotNull String string) {
        Streams streams;
        Intrinsics.checkNotNullParameter(string, "");
        if (!(n2 != -1)) {
            String string2 = "Failed requirement.";
            throw new IllegalArgumentException(string2.toString());
        }
        Streams streams2 = null;
        WebSocketReader webSocketReader = null;
        WebSocketWriter webSocketWriter = null;
        RealWebSocket realWebSocket = this;
        synchronized (realWebSocket) {
            if (!(this.receivedCloseCode == -1)) {
                String string3 = "already closed";
                throw new IllegalStateException(string3.toString());
            }
            this.receivedCloseCode = n2;
            this.receivedCloseReason = string;
            if (this.enqueuedClose && this.messageAndCloseQueue.isEmpty()) {
                streams2 = this.streams;
                this.streams = null;
                webSocketReader = this.reader;
                this.reader = null;
                webSocketWriter = this.writer;
                this.writer = null;
                this.taskQueue.shutdown();
            }
        }
        try {
            this.listener.onClosing(this, n2, string);
            if (streams2 != null) {
                this.listener.onClosed(this, n2, string);
            }
            streams = streams2;
        }
        catch (Throwable throwable) {
            Streams streams3 = streams2;
            if (streams3 != null) {
                Util.closeQuietly(streams3);
            }
            WebSocketReader webSocketReader2 = webSocketReader;
            if (webSocketReader2 != null) {
                Util.closeQuietly(webSocketReader2);
            }
            WebSocketWriter webSocketWriter2 = webSocketWriter;
            if (webSocketWriter2 != null) {
                Util.closeQuietly(webSocketWriter2);
            }
            throw throwable;
        }
        if (streams != null) {
            Util.closeQuietly(streams);
        }
        WebSocketReader webSocketReader3 = webSocketReader;
        if (webSocketReader3 != null) {
            Util.closeQuietly(webSocketReader3);
        }
        WebSocketWriter webSocketWriter3 = webSocketWriter;
        if (webSocketWriter3 == null) {
            return;
        }
        Util.closeQuietly(webSocketWriter3);
    }

    @Override
    public final boolean send(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, "");
        return this.send(ByteString.Companion.encodeUtf8(string), 1);
    }

    @Override
    public final boolean send(@NotNull ByteString byteString) {
        Intrinsics.checkNotNullParameter(byteString, "");
        return this.send(byteString, 2);
    }

    private final synchronized boolean send(ByteString byteString, int n2) {
        if (this.failed || this.enqueuedClose) {
            return false;
        }
        if (this.queueSize + (long)byteString.size() > 0x1000000L) {
            this.close(1001, null);
            return false;
        }
        this.queueSize += (long)byteString.size();
        this.messageAndCloseQueue.add(new Message(n2, byteString));
        this.runWriter();
        return true;
    }

    public final synchronized boolean pong(@NotNull ByteString byteString) {
        Intrinsics.checkNotNullParameter(byteString, "");
        if (this.failed || this.enqueuedClose && this.messageAndCloseQueue.isEmpty()) {
            return false;
        }
        this.pongQueue.add(byteString);
        this.runWriter();
        return true;
    }

    @Override
    public final boolean close(int n2, @Nullable String string) {
        return this.close(n2, string, 60000L);
    }

    public final synchronized boolean close(int n2, @Nullable String string, long l2) {
        WebSocketProtocol.INSTANCE.validateCloseCode(n2);
        ByteString byteString = null;
        if (string != null && !((long)(byteString = ByteString.Companion.encodeUtf8(string)).size() <= 123L)) {
            String string2 = "reason.size() > 123: " + string;
            throw new IllegalArgumentException(string2.toString());
        }
        if (this.failed || this.enqueuedClose) {
            return false;
        }
        this.enqueuedClose = true;
        this.messageAndCloseQueue.add(new Close(n2, byteString, l2));
        this.runWriter();
        return true;
    }

    private final void runWriter() {
        Object object = this;
        if (Util.assertionsEnabled && !Thread.holdsLock(object)) {
            throw new AssertionError((Object)("Thread " + Thread.currentThread().getName() + " MUST hold lock on " + object));
        }
        object = this.writerTask;
        if (object != null) {
            TaskQueue.schedule$default(this.taskQueue, (Task)object, 0L, 2, null);
        }
    }

    public final boolean writeOneFrame$okhttp() throws IOException {
        Streams streams;
        ByteString byteString;
        Object object;
        Object object2 = null;
        int n2 = 0;
        String string = null;
        Streams streams2 = null;
        WebSocketReader webSocketReader = null;
        WebSocketWriter webSocketWriter = null;
        Object object3 = this;
        synchronized (object3) {
            block26: {
                block25: {
                    if (!this.failed) break block25;
                    return false;
                }
                object = this.writer;
                byteString = this.pongQueue.poll();
                if (byteString != null) break block26;
                object2 = this.messageAndCloseQueue.poll();
                if (object2 instanceof Close) {
                    n2 = this.receivedCloseCode;
                    string = this.receivedCloseReason;
                    if (n2 != -1) {
                        streams2 = this.streams;
                        this.streams = null;
                        webSocketReader = this.reader;
                        this.reader = null;
                        webSocketWriter = this.writer;
                        this.writer = null;
                        this.taskQueue.shutdown();
                    } else {
                        long l2 = ((Close)object2).getCancelAfterCloseMillis();
                        TaskQueue taskQueue = this.taskQueue;
                        String string2 = this.name + " cancel";
                        long l3 = TimeUnit.MILLISECONDS.toNanos(l2);
                        taskQueue.schedule(new Task(string2, true, this){
                            final /* synthetic */ RealWebSocket this$0;
                            {
                                this.this$0 = realWebSocket;
                                super(string, bl);
                            }

                            public final long runOnce() {
                                this.this$0.cancel();
                                return -1L;
                            }
                        }, l3);
                    }
                    break block26;
                }
                if (object2 != null) break block26;
                return false;
            }
        }
        try {
            if (byteString != null) {
                WebSocketWriter webSocketWriter2 = object;
                Intrinsics.checkNotNull(webSocketWriter2);
                webSocketWriter2.writePong(byteString);
            } else if (object2 instanceof Message) {
                object3 = (Message)object2;
                WebSocketWriter webSocketWriter3 = object;
                Intrinsics.checkNotNull(webSocketWriter3);
                webSocketWriter3.writeMessageFrame(((Message)object3).getFormatOpcode(), ((Message)object3).getData());
                object = this;
                synchronized (object) {
                    this.queueSize -= (long)((Message)object3).getData().size();
                }
            } else if (object2 instanceof Close) {
                object3 = (Close)object2;
                WebSocketWriter webSocketWriter4 = object;
                Intrinsics.checkNotNull(webSocketWriter4);
                webSocketWriter4.writeClose(((Close)object3).getCode(), ((Close)object3).getReason());
                if (streams2 != null) {
                    WebSocket webSocket = this;
                    String string3 = string;
                    Intrinsics.checkNotNull(string3);
                    this.listener.onClosed(webSocket, n2, string3);
                }
            } else {
                throw new AssertionError();
            }
            streams = streams2;
        }
        catch (Throwable throwable) {
            block27: {
                Streams streams3 = streams2;
                if (streams3 != null) {
                    Util.closeQuietly(streams3);
                }
                WebSocketReader webSocketReader2 = webSocketReader;
                if (webSocketReader2 != null) {
                    Util.closeQuietly(webSocketReader2);
                }
                WebSocketWriter webSocketWriter5 = webSocketWriter;
                if (webSocketWriter5 == null) break block27;
                Util.closeQuietly(webSocketWriter5);
            }
            throw throwable;
        }
        if (streams != null) {
            Util.closeQuietly(streams);
        }
        WebSocketReader webSocketReader3 = webSocketReader;
        if (webSocketReader3 != null) {
            Util.closeQuietly(webSocketReader3);
        }
        WebSocketWriter webSocketWriter6 = webSocketWriter;
        if (webSocketWriter6 != null) {
            Util.closeQuietly(webSocketWriter6);
        }
        return true;
    }

    public final void writePingFrame$okhttp() {
        int n2;
        WebSocketWriter webSocketWriter;
        RealWebSocket realWebSocket = this;
        synchronized (realWebSocket) {
            if (this.failed) {
                return;
            }
            WebSocketWriter webSocketWriter2 = this.writer;
            if (webSocketWriter2 == null) {
                return;
            }
            webSocketWriter = webSocketWriter2;
            n2 = this.awaitingPong ? this.sentPingCount : -1;
            int n3 = this.sentPingCount;
            this.sentPingCount = n3 + 1;
            this.awaitingPong = true;
        }
        if (n2 != -1) {
            this.failWebSocket(new SocketTimeoutException("sent ping but didn't receive pong within " + this.pingIntervalMillis + "ms (after " + (n2 - 1) + " successful ping/pongs)"), null);
            return;
        }
        try {
            webSocketWriter.writePing(ByteString.EMPTY);
            return;
        }
        catch (IOException iOException) {
            this.failWebSocket(iOException, null);
            return;
        }
    }

    public final void failWebSocket(@NotNull Exception exception, @Nullable Response response) {
        Streams streams;
        WebSocketWriter webSocketWriter;
        WebSocketReader webSocketReader;
        Streams streams2;
        Intrinsics.checkNotNullParameter(exception, "");
        RealWebSocket realWebSocket = this;
        synchronized (realWebSocket) {
            if (this.failed) {
                return;
            }
            this.failed = true;
            streams2 = this.streams;
            this.streams = null;
            webSocketReader = this.reader;
            this.reader = null;
            webSocketWriter = this.writer;
            this.writer = null;
            this.taskQueue.shutdown();
        }
        try {
            this.listener.onFailure(this, exception, response);
            streams = streams2;
        }
        catch (Throwable throwable) {
            Streams streams3 = streams2;
            if (streams3 != null) {
                Util.closeQuietly(streams3);
            }
            WebSocketReader webSocketReader2 = webSocketReader;
            if (webSocketReader2 != null) {
                Util.closeQuietly(webSocketReader2);
            }
            WebSocketWriter webSocketWriter2 = webSocketWriter;
            if (webSocketWriter2 != null) {
                Util.closeQuietly(webSocketWriter2);
            }
            throw throwable;
        }
        if (streams != null) {
            Util.closeQuietly(streams);
        }
        WebSocketReader webSocketReader3 = webSocketReader;
        if (webSocketReader3 != null) {
            Util.closeQuietly(webSocketReader3);
        }
        WebSocketWriter webSocketWriter3 = webSocketWriter;
        if (webSocketWriter3 == null) {
            return;
        }
        Util.closeQuietly(webSocketWriter3);
    }

    public static final /* synthetic */ void access$setExtensions$p(RealWebSocket realWebSocket, WebSocketExtensions webSocketExtensions) {
        realWebSocket.extensions = webSocketExtensions;
    }

    public static final /* synthetic */ boolean access$isValid(RealWebSocket realWebSocket, WebSocketExtensions webSocketExtensions) {
        return realWebSocket.isValid(webSocketExtensions);
    }

    public static final /* synthetic */ ArrayDeque access$getMessageAndCloseQueue$p(RealWebSocket realWebSocket) {
        return realWebSocket.messageAndCloseQueue;
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\b\b\u0000\u0018\u00002\u00020\u0001B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bR\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e\u00a8\u0006\u000f"}, d2={"Lokhttp3/internal/ws/RealWebSocket$Close;", "", "code", "", "reason", "Lokio/ByteString;", "cancelAfterCloseMillis", "", "(ILokio/ByteString;J)V", "getCancelAfterCloseMillis", "()J", "getCode", "()I", "getReason", "()Lokio/ByteString;", "okhttp"})
    public static final class Close {
        private final int code;
        @Nullable
        private final ByteString reason;
        private final long cancelAfterCloseMillis;

        public Close(int n2, @Nullable ByteString byteString, long l2) {
            this.code = n2;
            this.reason = byteString;
            this.cancelAfterCloseMillis = l2;
        }

        public final int getCode() {
            return this.code;
        }

        @Nullable
        public final ByteString getReason() {
            return this.reason;
        }

        public final long getCancelAfterCloseMillis() {
            return this.cancelAfterCloseMillis;
        }
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2={"Lokhttp3/internal/ws/RealWebSocket$Companion;", "", "()V", "CANCEL_AFTER_CLOSE_MILLIS", "", "DEFAULT_MINIMUM_DEFLATE_SIZE", "MAX_QUEUE_SIZE", "ONLY_HTTP1", "", "Lokhttp3/Protocol;", "okhttp"})
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0000\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u000b"}, d2={"Lokhttp3/internal/ws/RealWebSocket$Message;", "", "formatOpcode", "", "data", "Lokio/ByteString;", "(ILokio/ByteString;)V", "getData", "()Lokio/ByteString;", "getFormatOpcode", "()I", "okhttp"})
    public static final class Message {
        private final int formatOpcode;
        @NotNull
        private final ByteString data;

        public Message(int n2, @NotNull ByteString byteString) {
            Intrinsics.checkNotNullParameter(byteString, "");
            this.formatOpcode = n2;
            this.data = byteString;
        }

        public final int getFormatOpcode() {
            return this.formatOpcode;
        }

        @NotNull
        public final ByteString getData() {
            return this.data;
        }
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\b&\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e\u00a8\u0006\u000f"}, d2={"Lokhttp3/internal/ws/RealWebSocket$Streams;", "Ljava/io/Closeable;", "client", "", "source", "Lokio/BufferedSource;", "sink", "Lokio/BufferedSink;", "(ZLokio/BufferedSource;Lokio/BufferedSink;)V", "getClient", "()Z", "getSink", "()Lokio/BufferedSink;", "getSource", "()Lokio/BufferedSource;", "okhttp"})
    public static abstract class Streams
    implements Closeable {
        private final boolean client;
        @NotNull
        private final BufferedSource source;
        @NotNull
        private final BufferedSink sink;

        public Streams(boolean bl, @NotNull BufferedSource bufferedSource, @NotNull BufferedSink bufferedSink) {
            Intrinsics.checkNotNullParameter(bufferedSource, "");
            Intrinsics.checkNotNullParameter(bufferedSink, "");
            this.client = bl;
            this.source = bufferedSource;
            this.sink = bufferedSink;
        }

        public final boolean getClient() {
            return this.client;
        }

        @NotNull
        public final BufferedSource getSource() {
            return this.source;
        }

        @NotNull
        public final BufferedSink getSink() {
            return this.sink;
        }
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\b\u0082\u0004\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0016\u00a8\u0006\u0005"}, d2={"Lokhttp3/internal/ws/RealWebSocket$WriterTask;", "Lokhttp3/internal/concurrent/Task;", "(Lokhttp3/internal/ws/RealWebSocket;)V", "runOnce", "", "okhttp"})
    final class WriterTask
    extends Task {
        public WriterTask() {
            super(RealWebSocket.this.name + " writer", false, 2, null);
        }

        @Override
        public final long runOnce() {
            try {
                if (RealWebSocket.this.writeOneFrame$okhttp()) {
                    return 0L;
                }
            }
            catch (IOException iOException) {
                RealWebSocket.this.failWebSocket(iOException, null);
            }
            return -1L;
        }
    }
}


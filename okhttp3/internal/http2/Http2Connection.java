/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.http2;

import java.io.Closeable;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.Socket;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.JvmField;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.jvm.internal.SourceDebugExtension;
import okhttp3.Headers;
import okhttp3.internal.Util;
import okhttp3.internal.concurrent.Task;
import okhttp3.internal.concurrent.TaskQueue;
import okhttp3.internal.concurrent.TaskRunner;
import okhttp3.internal.http2.ConnectionShutdownException;
import okhttp3.internal.http2.ErrorCode;
import okhttp3.internal.http2.Header;
import okhttp3.internal.http2.Http2Reader;
import okhttp3.internal.http2.Http2Stream;
import okhttp3.internal.http2.Http2Writer;
import okhttp3.internal.http2.PushObserver;
import okhttp3.internal.http2.Settings;
import okhttp3.internal.platform.Platform;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;
import okio.Okio;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\u00b4\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010#\n\u0002\u0010\b\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010%\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u001d\n\u0002\u0018\u0002\n\u0002\b\u0014\u0018\u0000 \u0099\u00012\u00020\u0001:\b\u0098\u0001\u0099\u0001\u009a\u0001\u009b\u0001B\u000f\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010P\u001a\u00020QJ\b\u0010R\u001a\u00020QH\u0016J'\u0010R\u001a\u00020Q2\u0006\u0010S\u001a\u00020T2\u0006\u0010U\u001a\u00020T2\b\u0010V\u001a\u0004\u0018\u00010WH\u0000\u00a2\u0006\u0002\bXJ\u0012\u0010Y\u001a\u00020Q2\b\u0010Z\u001a\u0004\u0018\u00010WH\u0002J\u0006\u0010[\u001a\u00020QJ\u0010\u0010\\\u001a\u0004\u0018\u00010B2\u0006\u0010]\u001a\u00020\u0012J\u000e\u0010^\u001a\u00020\t2\u0006\u0010_\u001a\u00020\u0006J&\u0010`\u001a\u00020B2\u0006\u0010a\u001a\u00020\u00122\f\u0010b\u001a\b\u0012\u0004\u0012\u00020d0c2\u0006\u0010e\u001a\u00020\tH\u0002J\u001c\u0010`\u001a\u00020B2\f\u0010b\u001a\b\u0012\u0004\u0012\u00020d0c2\u0006\u0010e\u001a\u00020\tJ\u0006\u0010f\u001a\u00020\u0012J-\u0010g\u001a\u00020Q2\u0006\u0010h\u001a\u00020\u00122\u0006\u0010i\u001a\u00020j2\u0006\u0010k\u001a\u00020\u00122\u0006\u0010l\u001a\u00020\tH\u0000\u00a2\u0006\u0002\bmJ+\u0010n\u001a\u00020Q2\u0006\u0010h\u001a\u00020\u00122\f\u0010b\u001a\b\u0012\u0004\u0012\u00020d0c2\u0006\u0010l\u001a\u00020\tH\u0000\u00a2\u0006\u0002\boJ#\u0010p\u001a\u00020Q2\u0006\u0010h\u001a\u00020\u00122\f\u0010b\u001a\b\u0012\u0004\u0012\u00020d0cH\u0000\u00a2\u0006\u0002\bqJ\u001d\u0010r\u001a\u00020Q2\u0006\u0010h\u001a\u00020\u00122\u0006\u0010s\u001a\u00020TH\u0000\u00a2\u0006\u0002\btJ$\u0010u\u001a\u00020B2\u0006\u0010a\u001a\u00020\u00122\f\u0010b\u001a\b\u0012\u0004\u0012\u00020d0c2\u0006\u0010e\u001a\u00020\tJ\u0015\u0010v\u001a\u00020\t2\u0006\u0010h\u001a\u00020\u0012H\u0000\u00a2\u0006\u0002\bwJ\u0017\u0010x\u001a\u0004\u0018\u00010B2\u0006\u0010h\u001a\u00020\u0012H\u0000\u00a2\u0006\u0002\byJ\r\u0010z\u001a\u00020QH\u0000\u00a2\u0006\u0002\b{J\u000e\u0010|\u001a\u00020Q2\u0006\u0010}\u001a\u00020&J\u000e\u0010~\u001a\u00020Q2\u0006\u0010\u007f\u001a\u00020TJ\u001e\u0010\u0080\u0001\u001a\u00020Q2\t\b\u0002\u0010\u0081\u0001\u001a\u00020\t2\b\b\u0002\u0010E\u001a\u00020FH\u0007J\u0018\u0010\u0082\u0001\u001a\u00020Q2\u0007\u0010\u0083\u0001\u001a\u00020\u0006H\u0000\u00a2\u0006\u0003\b\u0084\u0001J,\u0010\u0085\u0001\u001a\u00020Q2\u0006\u0010h\u001a\u00020\u00122\u0007\u0010\u0086\u0001\u001a\u00020\t2\n\u0010\u0087\u0001\u001a\u0005\u0018\u00010\u0088\u00012\u0006\u0010k\u001a\u00020\u0006J/\u0010\u0089\u0001\u001a\u00020Q2\u0006\u0010h\u001a\u00020\u00122\u0007\u0010\u0086\u0001\u001a\u00020\t2\r\u0010\u008a\u0001\u001a\b\u0012\u0004\u0012\u00020d0cH\u0000\u00a2\u0006\u0003\b\u008b\u0001J\u0007\u0010\u008c\u0001\u001a\u00020QJ\"\u0010\u008c\u0001\u001a\u00020Q2\u0007\u0010\u008d\u0001\u001a\u00020\t2\u0007\u0010\u008e\u0001\u001a\u00020\u00122\u0007\u0010\u008f\u0001\u001a\u00020\u0012J\u0007\u0010\u0090\u0001\u001a\u00020QJ\u001f\u0010\u0091\u0001\u001a\u00020Q2\u0006\u0010h\u001a\u00020\u00122\u0006\u0010\u007f\u001a\u00020TH\u0000\u00a2\u0006\u0003\b\u0092\u0001J\u001f\u0010\u0093\u0001\u001a\u00020Q2\u0006\u0010h\u001a\u00020\u00122\u0006\u0010s\u001a\u00020TH\u0000\u00a2\u0006\u0003\b\u0094\u0001J \u0010\u0095\u0001\u001a\u00020Q2\u0006\u0010h\u001a\u00020\u00122\u0007\u0010\u0096\u0001\u001a\u00020\u0006H\u0000\u00a2\u0006\u0003\b\u0097\u0001R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\u00020\tX\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0014\u0010\f\u001a\u00020\rX\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00120\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0019\u001a\u00020\u0012X\u0080\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u001b\"\u0004\b\u001c\u0010\u001dR\u0014\u0010\u001e\u001a\u00020\u001fX\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010!R\u001a\u0010\"\u001a\u00020\u0012X\u0080\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b#\u0010\u001b\"\u0004\b$\u0010\u001dR\u0011\u0010%\u001a\u00020&\u00a2\u0006\b\n\u0000\u001a\u0004\b'\u0010(R\u001a\u0010)\u001a\u00020&X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b*\u0010(\"\u0004\b+\u0010,R\u000e\u0010-\u001a\u00020.X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010/\u001a\u000200X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001e\u00102\u001a\u00020\u00062\u0006\u00101\u001a\u00020\u0006@BX\u0086\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b3\u00104R\u001e\u00105\u001a\u00020\u00062\u0006\u00101\u001a\u00020\u0006@BX\u0086\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b6\u00104R\u0015\u00107\u001a\u000608R\u00020\u0000\u00a2\u0006\b\n\u0000\u001a\u0004\b9\u0010:R\u000e\u0010;\u001a\u000200X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010<\u001a\u00020=X\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b>\u0010?R \u0010@\u001a\u000e\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020B0AX\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bC\u0010DR\u000e\u0010E\u001a\u00020FX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001e\u0010G\u001a\u00020\u00062\u0006\u00101\u001a\u00020\u0006@BX\u0086\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\bH\u00104R\u001e\u0010I\u001a\u00020\u00062\u0006\u00101\u001a\u00020\u0006@BX\u0086\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\bJ\u00104R\u0011\u0010K\u001a\u00020L\u00a2\u0006\b\n\u0000\u001a\u0004\bM\u0010NR\u000e\u0010O\u001a\u000200X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u009c\u0001"}, d2={"Lokhttp3/internal/http2/Http2Connection;", "Ljava/io/Closeable;", "builder", "Lokhttp3/internal/http2/Http2Connection$Builder;", "(Lokhttp3/internal/http2/Http2Connection$Builder;)V", "awaitPingsSent", "", "awaitPongsReceived", "client", "", "getClient$okhttp", "()Z", "connectionName", "", "getConnectionName$okhttp", "()Ljava/lang/String;", "currentPushRequests", "", "", "degradedPingsSent", "degradedPongDeadlineNs", "degradedPongsReceived", "intervalPingsSent", "intervalPongsReceived", "isShutdown", "lastGoodStreamId", "getLastGoodStreamId$okhttp", "()I", "setLastGoodStreamId$okhttp", "(I)V", "listener", "Lokhttp3/internal/http2/Http2Connection$Listener;", "getListener$okhttp", "()Lokhttp3/internal/http2/Http2Connection$Listener;", "nextStreamId", "getNextStreamId$okhttp", "setNextStreamId$okhttp", "okHttpSettings", "Lokhttp3/internal/http2/Settings;", "getOkHttpSettings", "()Lokhttp3/internal/http2/Settings;", "peerSettings", "getPeerSettings", "setPeerSettings", "(Lokhttp3/internal/http2/Settings;)V", "pushObserver", "Lokhttp3/internal/http2/PushObserver;", "pushQueue", "Lokhttp3/internal/concurrent/TaskQueue;", "<set-?>", "readBytesAcknowledged", "getReadBytesAcknowledged", "()J", "readBytesTotal", "getReadBytesTotal", "readerRunnable", "Lokhttp3/internal/http2/Http2Connection$ReaderRunnable;", "getReaderRunnable", "()Lokhttp3/internal/http2/Http2Connection$ReaderRunnable;", "settingsListenerQueue", "socket", "Ljava/net/Socket;", "getSocket$okhttp", "()Ljava/net/Socket;", "streams", "", "Lokhttp3/internal/http2/Http2Stream;", "getStreams$okhttp", "()Ljava/util/Map;", "taskRunner", "Lokhttp3/internal/concurrent/TaskRunner;", "writeBytesMaximum", "getWriteBytesMaximum", "writeBytesTotal", "getWriteBytesTotal", "writer", "Lokhttp3/internal/http2/Http2Writer;", "getWriter", "()Lokhttp3/internal/http2/Http2Writer;", "writerQueue", "awaitPong", "", "close", "connectionCode", "Lokhttp3/internal/http2/ErrorCode;", "streamCode", "cause", "Ljava/io/IOException;", "close$okhttp", "failConnection", "e", "flush", "getStream", "id", "isHealthy", "nowNs", "newStream", "associatedStreamId", "requestHeaders", "", "Lokhttp3/internal/http2/Header;", "out", "openStreamCount", "pushDataLater", "streamId", "source", "Lokio/BufferedSource;", "byteCount", "inFinished", "pushDataLater$okhttp", "pushHeadersLater", "pushHeadersLater$okhttp", "pushRequestLater", "pushRequestLater$okhttp", "pushResetLater", "errorCode", "pushResetLater$okhttp", "pushStream", "pushedStream", "pushedStream$okhttp", "removeStream", "removeStream$okhttp", "sendDegradedPingLater", "sendDegradedPingLater$okhttp", "setSettings", "settings", "shutdown", "statusCode", "start", "sendConnectionPreface", "updateConnectionFlowControl", "read", "updateConnectionFlowControl$okhttp", "writeData", "outFinished", "buffer", "Lokio/Buffer;", "writeHeaders", "alternating", "writeHeaders$okhttp", "writePing", "reply", "payload1", "payload2", "writePingAndAwaitPong", "writeSynReset", "writeSynReset$okhttp", "writeSynResetLater", "writeSynResetLater$okhttp", "writeWindowUpdateLater", "unacknowledgedBytesRead", "writeWindowUpdateLater$okhttp", "Builder", "Companion", "Listener", "ReaderRunnable", "okhttp"})
@SourceDebugExtension(value={"SMAP\nHttp2Connection.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Http2Connection.kt\nokhttp3/internal/http2/Http2Connection\n+ 2 TaskQueue.kt\nokhttp3/internal/concurrent/TaskQueue\n+ 3 Util.kt\nokhttp3/internal/Util\n+ 4 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 5 ArraysJVM.kt\nkotlin/collections/ArraysKt__ArraysJVMKt\n+ 6 _Arrays.kt\nkotlin/collections/ArraysKt___ArraysKt\n*L\n1#1,1006:1\n84#2,4:1007\n90#2,13:1014\n90#2,13:1027\n90#2,13:1069\n90#2,13:1082\n90#2,13:1095\n90#2,13:1108\n90#2,13:1121\n90#2,13:1134\n563#3:1011\n557#3:1013\n557#3:1040\n615#3,4:1041\n402#3,5:1045\n402#3,5:1053\n402#3,5:1059\n402#3,5:1064\n1#4:1012\n37#5,2:1050\n13309#6:1052\n13310#6:1058\n*S KotlinDebug\n*F\n+ 1 Http2Connection.kt\nokhttp3/internal/http2/Http2Connection\n*L\n152#1:1007,4\n340#1:1014,13\n361#1:1027,13\n506#1:1069,13\n554#1:1082,13\n893#1:1095,13\n911#1:1108,13\n938#1:1121,13\n952#1:1134,13\n183#1:1011\n319#1:1013\n402#1:1040\n446#1:1041,4\n448#1:1045,5\n461#1:1053,5\n467#1:1059,5\n472#1:1064,5\n455#1:1050,2\n460#1:1052\n460#1:1058\n*E\n"})
public final class Http2Connection
implements Closeable {
    @NotNull
    public static final Companion Companion;
    private final boolean client;
    @NotNull
    private final Listener listener;
    @NotNull
    private final Map<Integer, Http2Stream> streams;
    @NotNull
    private final String connectionName;
    private int lastGoodStreamId;
    private int nextStreamId;
    private boolean isShutdown;
    @NotNull
    private final TaskRunner taskRunner;
    @NotNull
    private final TaskQueue writerQueue;
    @NotNull
    private final TaskQueue pushQueue;
    @NotNull
    private final TaskQueue settingsListenerQueue;
    @NotNull
    private final PushObserver pushObserver;
    private long intervalPingsSent;
    private long intervalPongsReceived;
    private long degradedPingsSent;
    private long degradedPongsReceived;
    private long awaitPingsSent;
    private long awaitPongsReceived;
    private long degradedPongDeadlineNs;
    @NotNull
    private final Settings okHttpSettings;
    @NotNull
    private Settings peerSettings;
    private long readBytesTotal;
    private long readBytesAcknowledged;
    private long writeBytesTotal;
    private long writeBytesMaximum;
    @NotNull
    private final Socket socket;
    @NotNull
    private final Http2Writer writer;
    @NotNull
    private final ReaderRunnable readerRunnable;
    @NotNull
    private final Set<Integer> currentPushRequests;
    public static final int OKHTTP_CLIENT_WINDOW_SIZE = 0x1000000;
    @NotNull
    private static final Settings DEFAULT_SETTINGS;
    public static final int INTERVAL_PING = 1;
    public static final int DEGRADED_PING = 2;
    public static final int AWAIT_PING = 3;
    public static final int DEGRADED_PONG_TIMEOUT_NS = 1000000000;

    public Http2Connection(@NotNull Builder object) {
        Settings settings;
        Intrinsics.checkNotNullParameter(object, "");
        this.client = ((Builder)object).getClient$okhttp();
        this.listener = ((Builder)object).getListener$okhttp();
        this.streams = new LinkedHashMap();
        this.connectionName = ((Builder)object).getConnectionName$okhttp();
        this.nextStreamId = ((Builder)object).getClient$okhttp() ? 3 : 2;
        this.taskRunner = ((Builder)object).getTaskRunner$okhttp();
        this.writerQueue = this.taskRunner.newQueue();
        this.pushQueue = this.taskRunner.newQueue();
        this.settingsListenerQueue = this.taskRunner.newQueue();
        this.pushObserver = ((Builder)object).getPushObserver$okhttp();
        Settings settings2 = settings = new Settings();
        Object object2 = this;
        if (((Builder)object).getClient$okhttp()) {
            settings2.set(7, 0x1000000);
        }
        ((Http2Connection)object2).okHttpSettings = settings;
        this.peerSettings = DEFAULT_SETTINGS;
        this.writeBytesMaximum = this.peerSettings.getInitialWindowSize();
        this.socket = ((Builder)object).getSocket$okhttp();
        this.writer = new Http2Writer(((Builder)object).getSink$okhttp(), this.client);
        this.readerRunnable = new ReaderRunnable(new Http2Reader(((Builder)object).getSource$okhttp(), this.client));
        this.currentPushRequests = new LinkedHashSet();
        if (((Builder)object).getPingIntervalMillis$okhttp() != 0) {
            long l2 = TimeUnit.MILLISECONDS.toNanos(((Builder)object).getPingIntervalMillis$okhttp());
            object = this.writerQueue;
            object2 = this.connectionName + " ping";
            ((TaskQueue)object).schedule(new Task((String)object2, this, l2){
                final /* synthetic */ Http2Connection this$0;
                final /* synthetic */ long $pingIntervalNanos$inlined;
                {
                    this.this$0 = http2Connection;
                    this.$pingIntervalNanos$inlined = l2;
                    super(string, false, 2, null);
                }

                public final long runOnce() {
                    boolean bl;
                    Http2Connection http2Connection = this.this$0;
                    synchronized (http2Connection) {
                        boolean bl2;
                        if (Http2Connection.access$getIntervalPongsReceived$p(this.this$0) < Http2Connection.access$getIntervalPingsSent$p(this.this$0)) {
                            bl2 = true;
                        } else {
                            long l2 = Http2Connection.access$getIntervalPingsSent$p(this.this$0);
                            Http2Connection.access$setIntervalPingsSent$p(this.this$0, l2 + 1L);
                            bl2 = false;
                        }
                        bl = bl2;
                    }
                    boolean bl3 = bl;
                    if (bl3) {
                        Http2Connection.access$failConnection(this.this$0, null);
                        return -1L;
                    }
                    this.this$0.writePing(false, 1, 0);
                    return this.$pingIntervalNanos$inlined;
                }
            }, l2);
        }
    }

    public final boolean getClient$okhttp() {
        return this.client;
    }

    @NotNull
    public final Listener getListener$okhttp() {
        return this.listener;
    }

    @NotNull
    public final Map<Integer, Http2Stream> getStreams$okhttp() {
        return this.streams;
    }

    @NotNull
    public final String getConnectionName$okhttp() {
        return this.connectionName;
    }

    public final int getLastGoodStreamId$okhttp() {
        return this.lastGoodStreamId;
    }

    public final void setLastGoodStreamId$okhttp(int n2) {
        this.lastGoodStreamId = n2;
    }

    public final int getNextStreamId$okhttp() {
        return this.nextStreamId;
    }

    public final void setNextStreamId$okhttp(int n2) {
        this.nextStreamId = n2;
    }

    @NotNull
    public final Settings getOkHttpSettings() {
        return this.okHttpSettings;
    }

    @NotNull
    public final Settings getPeerSettings() {
        return this.peerSettings;
    }

    public final void setPeerSettings(@NotNull Settings settings) {
        Intrinsics.checkNotNullParameter(settings, "");
        this.peerSettings = settings;
    }

    public final long getReadBytesTotal() {
        return this.readBytesTotal;
    }

    public final long getReadBytesAcknowledged() {
        return this.readBytesAcknowledged;
    }

    public final long getWriteBytesTotal() {
        return this.writeBytesTotal;
    }

    public final long getWriteBytesMaximum() {
        return this.writeBytesMaximum;
    }

    @NotNull
    public final Socket getSocket$okhttp() {
        return this.socket;
    }

    @NotNull
    public final Http2Writer getWriter() {
        return this.writer;
    }

    @NotNull
    public final ReaderRunnable getReaderRunnable() {
        return this.readerRunnable;
    }

    public final synchronized int openStreamCount() {
        return this.streams.size();
    }

    @Nullable
    public final synchronized Http2Stream getStream(int n2) {
        return this.streams.get(n2);
    }

    @Nullable
    public final synchronized Http2Stream removeStream$okhttp(int n2) {
        Http2Stream http2Stream = this.streams.remove(n2);
        Http2Connection http2Connection = this;
        Intrinsics.checkNotNull(http2Connection);
        ((Object)http2Connection).notifyAll();
        return http2Stream;
    }

    public final synchronized void updateConnectionFlowControl$okhttp(long l2) {
        this.readBytesTotal += l2;
        long l3 = this.readBytesTotal - this.readBytesAcknowledged;
        if (l3 >= (long)(this.okHttpSettings.getInitialWindowSize() / 2)) {
            this.writeWindowUpdateLater$okhttp(0, l3);
            this.readBytesAcknowledged += l3;
        }
    }

    @NotNull
    public final Http2Stream pushStream(int n2, @NotNull List<Header> list2, boolean bl) throws IOException {
        Intrinsics.checkNotNullParameter(list2, "");
        if (!(!this.client)) {
            String string = "Client cannot push requests.";
            throw new IllegalStateException(string.toString());
        }
        return this.newStream(n2, list2, bl);
    }

    @NotNull
    public final Http2Stream newStream(@NotNull List<Header> list2, boolean bl) throws IOException {
        Intrinsics.checkNotNullParameter(list2, "");
        return this.newStream(0, list2, bl);
    }

    private final Http2Stream newStream(int n2, List<Header> list2, boolean bl) throws IOException {
        Http2Stream http2Stream;
        boolean bl2 = !bl;
        Http2Writer http2Writer = this.writer;
        synchronized (http2Writer) {
            int n3;
            Http2Connection http2Connection = this;
            synchronized (http2Connection) {
                if (this.nextStreamId > 0x3FFFFFFF) {
                    this.shutdown(ErrorCode.REFUSED_STREAM);
                }
                if (this.isShutdown) {
                    throw new ConnectionShutdownException();
                }
                n3 = this.nextStreamId;
                this.nextStreamId += 2;
                http2Stream = new Http2Stream(n3, this, bl2, false, null);
                boolean bl3 = bl = !bl || this.writeBytesTotal >= this.writeBytesMaximum || http2Stream.getWriteBytesTotal() >= http2Stream.getWriteBytesMaximum();
                if (http2Stream.isOpen()) {
                    this.streams.put(n3, http2Stream);
                }
            }
            if (n2 == 0) {
                this.writer.headers(bl2, n3, list2);
            } else {
                if (!(!this.client)) {
                    String string = "client streams shouldn't have associated stream IDs";
                    throw new IllegalArgumentException(string.toString());
                }
                this.writer.pushPromise(n2, n3, list2);
            }
        }
        if (bl) {
            this.writer.flush();
        }
        return http2Stream;
    }

    public final void writeHeaders$okhttp(int n2, boolean bl, @NotNull List<Header> list2) throws IOException {
        Intrinsics.checkNotNullParameter(list2, "");
        this.writer.headers(bl, n2, list2);
    }

    public final void writeData(int n2, boolean bl, @Nullable Buffer buffer, long l2) throws IOException {
        if (l2 == 0L) {
            this.writer.data(bl, n2, buffer, 0);
            return;
        }
        long l3 = l2;
        while (l3 > 0L) {
            int n3;
            Http2Connection http2Connection = this;
            synchronized (http2Connection) {
                try {
                    while (this.writeBytesTotal >= this.writeBytesMaximum) {
                        if (!this.streams.containsKey(n2)) {
                            throw new IOException("stream closed");
                        }
                        Http2Connection http2Connection2 = this;
                        Intrinsics.checkNotNull(http2Connection2);
                        ((Object)http2Connection2).wait();
                    }
                }
                catch (InterruptedException interruptedException) {
                    Thread.currentThread().interrupt();
                    throw new InterruptedIOException();
                }
                n3 = (int)Math.min(l3, this.writeBytesMaximum - this.writeBytesTotal);
                n3 = Math.min(n3, this.writer.maxDataLength());
                this.writeBytesTotal += (long)n3;
            }
            this.writer.data(bl && (l3 -= (long)n3) == 0L, n2, buffer, n3);
        }
    }

    public final void writeSynResetLater$okhttp(int n2, @NotNull ErrorCode errorCode) {
        Intrinsics.checkNotNullParameter((Object)errorCode, "");
        TaskQueue taskQueue = this.writerQueue;
        String string = this.connectionName + '[' + n2 + "] writeSynReset";
        taskQueue.schedule(new Task(string, true, this, n2, errorCode){
            final /* synthetic */ Http2Connection this$0;
            final /* synthetic */ int $streamId$inlined;
            final /* synthetic */ ErrorCode $errorCode$inlined;
            {
                this.this$0 = http2Connection;
                this.$streamId$inlined = n2;
                this.$errorCode$inlined = errorCode;
                super(string, bl);
            }

            public final long runOnce() {
                try {
                    this.this$0.writeSynReset$okhttp(this.$streamId$inlined, this.$errorCode$inlined);
                }
                catch (IOException iOException) {
                    Http2Connection.access$failConnection(this.this$0, iOException);
                }
                return -1L;
            }
        }, 0L);
    }

    public final void writeSynReset$okhttp(int n2, @NotNull ErrorCode errorCode) throws IOException {
        Intrinsics.checkNotNullParameter((Object)errorCode, "");
        this.writer.rstStream(n2, errorCode);
    }

    public final void writeWindowUpdateLater$okhttp(int n2, long l2) {
        TaskQueue taskQueue = this.writerQueue;
        String string = this.connectionName + '[' + n2 + "] windowUpdate";
        taskQueue.schedule(new Task(string, true, this, n2, l2){
            final /* synthetic */ Http2Connection this$0;
            final /* synthetic */ int $streamId$inlined;
            final /* synthetic */ long $unacknowledgedBytesRead$inlined;
            {
                this.this$0 = http2Connection;
                this.$streamId$inlined = n2;
                this.$unacknowledgedBytesRead$inlined = l2;
                super(string, bl);
            }

            public final long runOnce() {
                try {
                    this.this$0.getWriter().windowUpdate(this.$streamId$inlined, this.$unacknowledgedBytesRead$inlined);
                }
                catch (IOException iOException) {
                    Http2Connection.access$failConnection(this.this$0, iOException);
                }
                return -1L;
            }
        }, 0L);
    }

    public final void writePing(boolean bl, int n2, int n3) {
        try {
            this.writer.ping(bl, n2, n3);
            return;
        }
        catch (IOException iOException) {
            this.failConnection(iOException);
            return;
        }
    }

    public final void writePingAndAwaitPong() throws InterruptedException {
        this.writePing();
        this.awaitPong();
    }

    public final void writePing() throws InterruptedException {
        Http2Connection http2Connection = this;
        synchronized (http2Connection) {
            long l2 = this.awaitPingsSent;
            this.awaitPingsSent = l2 + 1L;
        }
        this.writePing(false, 3, 1330343787);
    }

    public final synchronized void awaitPong() throws InterruptedException {
        while (this.awaitPongsReceived < this.awaitPingsSent) {
            Http2Connection http2Connection = this;
            Intrinsics.checkNotNull(http2Connection);
            ((Object)http2Connection).wait();
        }
    }

    public final void flush() throws IOException {
        this.writer.flush();
    }

    public final void shutdown(@NotNull ErrorCode errorCode) throws IOException {
        Intrinsics.checkNotNullParameter((Object)errorCode, "");
        Http2Writer http2Writer = this.writer;
        synchronized (http2Writer) {
            Ref.IntRef intRef = new Ref.IntRef();
            Http2Connection http2Connection = this;
            synchronized (http2Connection) {
                if (this.isShutdown) {
                    return;
                }
                this.isShutdown = true;
                intRef.element = this.lastGoodStreamId;
            }
            this.writer.goAway(intRef.element, errorCode, Util.EMPTY_BYTE_ARRAY);
            return;
        }
    }

    @Override
    public final void close() {
        this.close$okhttp(ErrorCode.NO_ERROR, ErrorCode.CANCEL, null);
    }

    public final void close$okhttp(@NotNull ErrorCode object, @NotNull ErrorCode errorCode, @Nullable IOException iOException) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter((Object)errorCode, "");
        Http2Stream[] http2StreamArray = this;
        if (Util.assertionsEnabled && Thread.holdsLock(http2StreamArray)) {
            throw new AssertionError((Object)("Thread " + Thread.currentThread().getName() + " MUST NOT hold lock on " + http2StreamArray));
        }
        try {
            this.shutdown((ErrorCode)((Object)object));
        }
        catch (IOException iOException2) {}
        http2StreamArray = null;
        object = this;
        synchronized (object) {
            if (!this.streams.isEmpty()) {
                http2StreamArray = this.streams.values();
                Collection<Http2Stream> collection = http2StreamArray;
                http2StreamArray = http2StreamArray.toArray(new Http2Stream[0]);
                this.streams.clear();
            }
        }
        Http2Stream[] http2StreamArray2 = http2StreamArray;
        if (http2StreamArray2 != null) {
            for (Http2Stream http2Stream : http2StreamArray2) {
                try {
                    http2Stream.close(errorCode, iOException);
                }
                catch (IOException iOException3) {}
            }
        }
        try {
            this.writer.close();
        }
        catch (IOException iOException4) {}
        try {
            this.socket.close();
        }
        catch (IOException iOException5) {}
        this.writerQueue.shutdown();
        this.pushQueue.shutdown();
        this.settingsListenerQueue.shutdown();
    }

    private final void failConnection(IOException iOException) {
        ErrorCode errorCode = ErrorCode.PROTOCOL_ERROR;
        this.close$okhttp(errorCode, errorCode, iOException);
    }

    @JvmOverloads
    public final void start(boolean n2, @NotNull TaskRunner object) throws IOException {
        Intrinsics.checkNotNullParameter(object, "");
        if (n2) {
            this.writer.connectionPreface();
            this.writer.settings(this.okHttpSettings);
            n2 = this.okHttpSettings.getInitialWindowSize();
            if (n2 != 65535) {
                this.writer.windowUpdate(0, n2 - 65535);
            }
        }
        TaskQueue taskQueue = ((TaskRunner)object).newQueue();
        object = this.connectionName;
        Function0 function0 = this.readerRunnable;
        taskQueue.schedule(new Task((String)object, true, function0){
            final /* synthetic */ Function0<Unit> $block;
            {
                this.$block = function0;
                super(string, bl);
            }

            public final long runOnce() {
                this.$block.invoke();
                return -1L;
            }
        }, 0L);
    }

    public static /* synthetic */ void start$default(Http2Connection http2Connection, boolean bl, TaskRunner taskRunner, int n2, Object object) throws IOException {
        if ((n2 & 1) != 0) {
            bl = true;
        }
        if ((n2 & 2) != 0) {
            taskRunner = TaskRunner.INSTANCE;
        }
        http2Connection.start(bl, taskRunner);
    }

    public final void setSettings(@NotNull Settings settings) throws IOException {
        Intrinsics.checkNotNullParameter(settings, "");
        Http2Writer http2Writer = this.writer;
        synchronized (http2Writer) {
            Http2Connection http2Connection = this;
            synchronized (http2Connection) {
                if (this.isShutdown) {
                    throw new ConnectionShutdownException();
                }
                this.okHttpSettings.merge(settings);
            }
            this.writer.settings(settings);
            return;
        }
    }

    public final synchronized boolean isHealthy(long l2) {
        if (this.isShutdown) {
            return false;
        }
        return this.degradedPongsReceived >= this.degradedPingsSent || l2 < this.degradedPongDeadlineNs;
    }

    public final void sendDegradedPingLater$okhttp() {
        Object object = this;
        synchronized (object) {
            if (this.degradedPongsReceived < this.degradedPingsSent) {
                return;
            }
            long l2 = this.degradedPingsSent;
            this.degradedPingsSent = l2 + 1L;
            this.degradedPongDeadlineNs = System.nanoTime() + 1000000000L;
        }
        object = this.writerQueue;
        String string = this.connectionName + " ping";
        ((TaskQueue)object).schedule(new Task(string, true, this){
            final /* synthetic */ Http2Connection this$0;
            {
                this.this$0 = http2Connection;
                super(string, bl);
            }

            public final long runOnce() {
                this.this$0.writePing(false, 2, 0);
                return -1L;
            }
        }, 0L);
    }

    public final boolean pushedStream$okhttp(int n2) {
        return n2 != 0 && (n2 & 1) == 0;
    }

    public final void pushRequestLater$okhttp(int n2, @NotNull List<Header> list2) {
        Intrinsics.checkNotNullParameter(list2, "");
        Object object = this;
        synchronized (object) {
            if (this.currentPushRequests.contains(n2)) {
                this.writeSynResetLater$okhttp(n2, ErrorCode.PROTOCOL_ERROR);
                return;
            }
            this.currentPushRequests.add(n2);
        }
        object = this.pushQueue;
        String string = this.connectionName + '[' + n2 + "] onRequest";
        ((TaskQueue)object).schedule(new Task(string, true, this, n2, list2){
            final /* synthetic */ Http2Connection this$0;
            final /* synthetic */ int $streamId$inlined;
            final /* synthetic */ List $requestHeaders$inlined;
            {
                this.this$0 = http2Connection;
                this.$streamId$inlined = n2;
                this.$requestHeaders$inlined = list2;
                super(string, bl);
            }

            public final long runOnce() {
                block5: {
                    boolean bl = Http2Connection.access$getPushObserver$p(this.this$0).onRequest(this.$streamId$inlined, this.$requestHeaders$inlined);
                    try {
                        if (!bl) break block5;
                        this.this$0.getWriter().rstStream(this.$streamId$inlined, ErrorCode.CANCEL);
                        Http2Connection http2Connection = this.this$0;
                        synchronized (http2Connection) {
                            Http2Connection.access$getCurrentPushRequests$p(this.this$0).remove(this.$streamId$inlined);
                        }
                    }
                    catch (IOException iOException) {}
                }
                return -1L;
            }
        }, 0L);
    }

    public final void pushHeadersLater$okhttp(int n2, @NotNull List<Header> list2, boolean bl) {
        Intrinsics.checkNotNullParameter(list2, "");
        TaskQueue taskQueue = this.pushQueue;
        String string = this.connectionName + '[' + n2 + "] onHeaders";
        taskQueue.schedule(new Task(string, true, this, n2, list2, bl){
            final /* synthetic */ Http2Connection this$0;
            final /* synthetic */ int $streamId$inlined;
            final /* synthetic */ List $requestHeaders$inlined;
            final /* synthetic */ boolean $inFinished$inlined;
            {
                this.this$0 = http2Connection;
                this.$streamId$inlined = n2;
                this.$requestHeaders$inlined = list2;
                this.$inFinished$inlined = bl2;
                super(string, bl);
            }

            public final long runOnce() {
                block6: {
                    boolean bl = Http2Connection.access$getPushObserver$p(this.this$0).onHeaders(this.$streamId$inlined, this.$requestHeaders$inlined, this.$inFinished$inlined);
                    try {
                        if (bl) {
                            this.this$0.getWriter().rstStream(this.$streamId$inlined, ErrorCode.CANCEL);
                        }
                        if (!bl && !this.$inFinished$inlined) break block6;
                        Http2Connection http2Connection = this.this$0;
                        synchronized (http2Connection) {
                            Http2Connection.access$getCurrentPushRequests$p(this.this$0).remove(this.$streamId$inlined);
                        }
                    }
                    catch (IOException iOException) {}
                }
                return -1L;
            }
        }, 0L);
    }

    public final void pushDataLater$okhttp(int n2, @NotNull BufferedSource object, int n3, boolean bl) throws IOException {
        Intrinsics.checkNotNullParameter(object, "");
        Buffer buffer = new Buffer();
        object.require(n3);
        object.read(buffer, n3);
        object = this.pushQueue;
        String string = this.connectionName + '[' + n2 + "] onData";
        ((TaskQueue)object).schedule(new Task(string, true, this, n2, buffer, n3, bl){
            final /* synthetic */ Http2Connection this$0;
            final /* synthetic */ int $streamId$inlined;
            final /* synthetic */ Buffer $buffer$inlined;
            final /* synthetic */ int $byteCount$inlined;
            final /* synthetic */ boolean $inFinished$inlined;
            {
                this.this$0 = http2Connection;
                this.$streamId$inlined = n2;
                this.$buffer$inlined = buffer;
                this.$byteCount$inlined = n3;
                this.$inFinished$inlined = bl2;
                super(string, bl);
            }

            public final long runOnce() {
                block6: {
                    try {
                        boolean bl = Http2Connection.access$getPushObserver$p(this.this$0).onData(this.$streamId$inlined, this.$buffer$inlined, this.$byteCount$inlined, this.$inFinished$inlined);
                        if (bl) {
                            this.this$0.getWriter().rstStream(this.$streamId$inlined, ErrorCode.CANCEL);
                        }
                        if (!bl && !this.$inFinished$inlined) break block6;
                        Http2Connection http2Connection = this.this$0;
                        synchronized (http2Connection) {
                            Http2Connection.access$getCurrentPushRequests$p(this.this$0).remove(this.$streamId$inlined);
                        }
                    }
                    catch (IOException iOException) {}
                }
                return -1L;
            }
        }, 0L);
    }

    public final void pushResetLater$okhttp(int n2, @NotNull ErrorCode errorCode) {
        Intrinsics.checkNotNullParameter((Object)errorCode, "");
        TaskQueue taskQueue = this.pushQueue;
        String string = this.connectionName + '[' + n2 + "] onReset";
        taskQueue.schedule(new Task(string, true, this, n2, errorCode){
            final /* synthetic */ Http2Connection this$0;
            final /* synthetic */ int $streamId$inlined;
            final /* synthetic */ ErrorCode $errorCode$inlined;
            {
                this.this$0 = http2Connection;
                this.$streamId$inlined = n2;
                this.$errorCode$inlined = errorCode;
                super(string, bl);
            }

            public final long runOnce() {
                Http2Connection.access$getPushObserver$p(this.this$0).onReset(this.$streamId$inlined, this.$errorCode$inlined);
                Http2Connection http2Connection = this.this$0;
                synchronized (http2Connection) {
                    Http2Connection.access$getCurrentPushRequests$p(this.this$0).remove(this.$streamId$inlined);
                }
                return -1L;
            }
        }, 0L);
    }

    @JvmOverloads
    public final void start(boolean bl) throws IOException {
        Http2Connection.start$default(this, bl, null, 2, null);
    }

    @JvmOverloads
    public final void start() throws IOException {
        Http2Connection.start$default(this, false, null, 3, null);
    }

    public static final /* synthetic */ long access$getIntervalPingsSent$p(Http2Connection http2Connection) {
        return http2Connection.intervalPingsSent;
    }

    public static final /* synthetic */ void access$setIntervalPingsSent$p(Http2Connection http2Connection, long l2) {
        http2Connection.intervalPingsSent = l2;
    }

    public static final /* synthetic */ Set access$getCurrentPushRequests$p(Http2Connection http2Connection) {
        return http2Connection.currentPushRequests;
    }

    public static final /* synthetic */ PushObserver access$getPushObserver$p(Http2Connection http2Connection) {
        return http2Connection.pushObserver;
    }

    static {
        Settings settings;
        Companion = new Companion(null);
        Settings settings2 = settings = new Settings();
        settings.set(7, 65535);
        settings2.set(5, 16384);
        DEFAULT_SETTINGS = settings;
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000X\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0006\u00107\u001a\u000208J\u000e\u0010\u0011\u001a\u00020\u00002\u0006\u0010\u0011\u001a\u00020\u0012J\u000e\u0010\u0017\u001a\u00020\u00002\u0006\u0010\u0017\u001a\u00020\u0018J\u000e\u0010\u001d\u001a\u00020\u00002\u0006\u0010\u001d\u001a\u00020\u001eJ.\u0010)\u001a\u00020\u00002\u0006\u0010)\u001a\u00020*2\b\b\u0002\u00109\u001a\u00020\f2\b\b\u0002\u0010/\u001a\u0002002\b\b\u0002\u0010#\u001a\u00020$H\u0007R\u001a\u0010\u0002\u001a\u00020\u0003X\u0080\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u001a\u0010\u000b\u001a\u00020\fX\u0080.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u001a\u0010\u0011\u001a\u00020\u0012X\u0080\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u001a\u0010\u0017\u001a\u00020\u0018X\u0080\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u001a\"\u0004\b\u001b\u0010\u001cR\u001a\u0010\u001d\u001a\u00020\u001eX\u0080\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010 \"\u0004\b!\u0010\"R\u001a\u0010#\u001a\u00020$X\u0080.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b%\u0010&\"\u0004\b'\u0010(R\u001a\u0010)\u001a\u00020*X\u0080.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b+\u0010,\"\u0004\b-\u0010.R\u001a\u0010/\u001a\u000200X\u0080.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b1\u00102\"\u0004\b3\u00104R\u0014\u0010\u0004\u001a\u00020\u0005X\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b5\u00106\u00a8\u0006:"}, d2={"Lokhttp3/internal/http2/Http2Connection$Builder;", "", "client", "", "taskRunner", "Lokhttp3/internal/concurrent/TaskRunner;", "(ZLokhttp3/internal/concurrent/TaskRunner;)V", "getClient$okhttp", "()Z", "setClient$okhttp", "(Z)V", "connectionName", "", "getConnectionName$okhttp", "()Ljava/lang/String;", "setConnectionName$okhttp", "(Ljava/lang/String;)V", "listener", "Lokhttp3/internal/http2/Http2Connection$Listener;", "getListener$okhttp", "()Lokhttp3/internal/http2/Http2Connection$Listener;", "setListener$okhttp", "(Lokhttp3/internal/http2/Http2Connection$Listener;)V", "pingIntervalMillis", "", "getPingIntervalMillis$okhttp", "()I", "setPingIntervalMillis$okhttp", "(I)V", "pushObserver", "Lokhttp3/internal/http2/PushObserver;", "getPushObserver$okhttp", "()Lokhttp3/internal/http2/PushObserver;", "setPushObserver$okhttp", "(Lokhttp3/internal/http2/PushObserver;)V", "sink", "Lokio/BufferedSink;", "getSink$okhttp", "()Lokio/BufferedSink;", "setSink$okhttp", "(Lokio/BufferedSink;)V", "socket", "Ljava/net/Socket;", "getSocket$okhttp", "()Ljava/net/Socket;", "setSocket$okhttp", "(Ljava/net/Socket;)V", "source", "Lokio/BufferedSource;", "getSource$okhttp", "()Lokio/BufferedSource;", "setSource$okhttp", "(Lokio/BufferedSource;)V", "getTaskRunner$okhttp", "()Lokhttp3/internal/concurrent/TaskRunner;", "build", "Lokhttp3/internal/http2/Http2Connection;", "peerName", "okhttp"})
    public static final class Builder {
        private boolean client;
        @NotNull
        private final TaskRunner taskRunner;
        public Socket socket;
        public String connectionName;
        public BufferedSource source;
        public BufferedSink sink;
        @NotNull
        private Listener listener;
        @NotNull
        private PushObserver pushObserver;
        private int pingIntervalMillis;

        public Builder(boolean bl, @NotNull TaskRunner taskRunner) {
            Intrinsics.checkNotNullParameter(taskRunner, "");
            this.client = bl;
            this.taskRunner = taskRunner;
            this.listener = Listener.REFUSE_INCOMING_STREAMS;
            this.pushObserver = PushObserver.CANCEL;
        }

        public final boolean getClient$okhttp() {
            return this.client;
        }

        public final void setClient$okhttp(boolean bl) {
            this.client = bl;
        }

        @NotNull
        public final TaskRunner getTaskRunner$okhttp() {
            return this.taskRunner;
        }

        @NotNull
        public final Socket getSocket$okhttp() {
            Socket socket = this.socket;
            if (socket != null) {
                return socket;
            }
            Intrinsics.throwUninitializedPropertyAccessException("");
            return null;
        }

        public final void setSocket$okhttp(@NotNull Socket socket) {
            Intrinsics.checkNotNullParameter(socket, "");
            this.socket = socket;
        }

        @NotNull
        public final String getConnectionName$okhttp() {
            String string = this.connectionName;
            if (string != null) {
                return string;
            }
            Intrinsics.throwUninitializedPropertyAccessException("");
            return null;
        }

        public final void setConnectionName$okhttp(@NotNull String string) {
            Intrinsics.checkNotNullParameter(string, "");
            this.connectionName = string;
        }

        @NotNull
        public final BufferedSource getSource$okhttp() {
            BufferedSource bufferedSource = this.source;
            if (bufferedSource != null) {
                return bufferedSource;
            }
            Intrinsics.throwUninitializedPropertyAccessException("");
            return null;
        }

        public final void setSource$okhttp(@NotNull BufferedSource bufferedSource) {
            Intrinsics.checkNotNullParameter(bufferedSource, "");
            this.source = bufferedSource;
        }

        @NotNull
        public final BufferedSink getSink$okhttp() {
            BufferedSink bufferedSink = this.sink;
            if (bufferedSink != null) {
                return bufferedSink;
            }
            Intrinsics.throwUninitializedPropertyAccessException("");
            return null;
        }

        public final void setSink$okhttp(@NotNull BufferedSink bufferedSink) {
            Intrinsics.checkNotNullParameter(bufferedSink, "");
            this.sink = bufferedSink;
        }

        @NotNull
        public final Listener getListener$okhttp() {
            return this.listener;
        }

        public final void setListener$okhttp(@NotNull Listener listener) {
            Intrinsics.checkNotNullParameter(listener, "");
            this.listener = listener;
        }

        @NotNull
        public final PushObserver getPushObserver$okhttp() {
            return this.pushObserver;
        }

        public final void setPushObserver$okhttp(@NotNull PushObserver pushObserver) {
            Intrinsics.checkNotNullParameter(pushObserver, "");
            this.pushObserver = pushObserver;
        }

        public final int getPingIntervalMillis$okhttp() {
            return this.pingIntervalMillis;
        }

        public final void setPingIntervalMillis$okhttp(int n2) {
            this.pingIntervalMillis = n2;
        }

        @JvmOverloads
        @NotNull
        public final Builder socket(@NotNull Socket socket, @NotNull String string, @NotNull BufferedSource bufferedSource, @NotNull BufferedSink bufferedSink) throws IOException {
            Builder builder;
            Intrinsics.checkNotNullParameter(socket, "");
            Intrinsics.checkNotNullParameter(string, "");
            Intrinsics.checkNotNullParameter(bufferedSource, "");
            Intrinsics.checkNotNullParameter(bufferedSink, "");
            Builder builder2 = builder = this;
            builder.setSocket$okhttp(socket);
            Builder builder3 = builder2;
            builder3.setConnectionName$okhttp(builder3.client ? Util.okHttpName + ' ' + string : "MockWebServer " + string);
            builder2.setSource$okhttp(bufferedSource);
            builder2.setSink$okhttp(bufferedSink);
            return builder;
        }

        public static /* synthetic */ Builder socket$default(Builder builder, Socket socket, String string, BufferedSource bufferedSource, BufferedSink bufferedSink, int n2, Object object) throws IOException {
            if ((n2 & 2) != 0) {
                string = Util.peerName(socket);
            }
            if ((n2 & 4) != 0) {
                bufferedSource = Okio.buffer(Okio.source(socket));
            }
            if ((n2 & 8) != 0) {
                bufferedSink = Okio.buffer(Okio.sink(socket));
            }
            return builder.socket(socket, string, bufferedSource, bufferedSink);
        }

        @NotNull
        public final Builder listener(@NotNull Listener listener) {
            Builder builder;
            Intrinsics.checkNotNullParameter(listener, "");
            Builder builder2 = builder = this;
            builder.listener = listener;
            return builder;
        }

        @NotNull
        public final Builder pushObserver(@NotNull PushObserver pushObserver) {
            Builder builder;
            Intrinsics.checkNotNullParameter(pushObserver, "");
            Builder builder2 = builder = this;
            builder.pushObserver = pushObserver;
            return builder;
        }

        @NotNull
        public final Builder pingIntervalMillis(int n2) {
            Builder builder;
            Builder builder2 = builder = this;
            builder.pingIntervalMillis = n2;
            return builder;
        }

        @NotNull
        public final Http2Connection build() {
            return new Http2Connection(this);
        }

        @JvmOverloads
        @NotNull
        public final Builder socket(@NotNull Socket socket, @NotNull String string, @NotNull BufferedSource bufferedSource) throws IOException {
            Intrinsics.checkNotNullParameter(socket, "");
            Intrinsics.checkNotNullParameter(string, "");
            Intrinsics.checkNotNullParameter(bufferedSource, "");
            return Builder.socket$default(this, socket, string, bufferedSource, null, 8, null);
        }

        @JvmOverloads
        @NotNull
        public final Builder socket(@NotNull Socket socket, @NotNull String string) throws IOException {
            Intrinsics.checkNotNullParameter(socket, "");
            Intrinsics.checkNotNullParameter(string, "");
            return Builder.socket$default(this, socket, string, null, null, 12, null);
        }

        @JvmOverloads
        @NotNull
        public final Builder socket(@NotNull Socket socket) throws IOException {
            Intrinsics.checkNotNullParameter(socket, "");
            return Builder.socket$default(this, socket, null, null, null, 14, null);
        }
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u000e\u0010\t\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2={"Lokhttp3/internal/http2/Http2Connection$Companion;", "", "()V", "AWAIT_PING", "", "DEFAULT_SETTINGS", "Lokhttp3/internal/http2/Settings;", "getDEFAULT_SETTINGS", "()Lokhttp3/internal/http2/Settings;", "DEGRADED_PING", "DEGRADED_PONG_TIMEOUT_NS", "INTERVAL_PING", "OKHTTP_CLIENT_WINDOW_SIZE", "okhttp"})
    public static final class Companion {
        private Companion() {
        }

        @NotNull
        public final Settings getDEFAULT_SETTINGS() {
            return DEFAULT_SETTINGS;
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b&\u0018\u0000 \f2\u00020\u0001:\u0001\fB\u0005\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0010\u0010\t\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u000bH&\u00a8\u0006\r"}, d2={"Lokhttp3/internal/http2/Http2Connection$Listener;", "", "()V", "onSettings", "", "connection", "Lokhttp3/internal/http2/Http2Connection;", "settings", "Lokhttp3/internal/http2/Settings;", "onStream", "stream", "Lokhttp3/internal/http2/Http2Stream;", "Companion", "okhttp"})
    public static abstract class Listener {
        @NotNull
        public static final Companion Companion = new Companion(null);
        @JvmField
        @NotNull
        public static final Listener REFUSE_INCOMING_STREAMS = new Listener(){

            public final void onStream(@NotNull Http2Stream http2Stream) throws IOException {
                Intrinsics.checkNotNullParameter(http2Stream, "");
                http2Stream.close(ErrorCode.REFUSED_STREAM, null);
            }
        };

        public abstract void onStream(@NotNull Http2Stream var1) throws IOException;

        public void onSettings(@NotNull Http2Connection http2Connection, @NotNull Settings settings) {
            Intrinsics.checkNotNullParameter(http2Connection, "");
            Intrinsics.checkNotNullParameter(settings, "");
        }

        @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0010\u0010\u0003\u001a\u00020\u00048\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2={"Lokhttp3/internal/http2/Http2Connection$Listener$Companion;", "", "()V", "REFUSE_INCOMING_STREAMS", "Lokhttp3/internal/http2/Http2Connection$Listener;", "okhttp"})
        public static final class Companion {
            private Companion() {
            }

            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }
        }
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000`\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0010\b\u0086\u0004\u0018\u00002\u00020\u00012\b\u0012\u0004\u0012\u00020\u00030\u0002B\u000f\b\u0000\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\b\u0010\t\u001a\u00020\u0003H\u0016J8\u0010\n\u001a\u00020\u00032\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u000e2\u0006\u0010\u0012\u001a\u00020\f2\u0006\u0010\u0013\u001a\u00020\u0014H\u0016J\u0016\u0010\u0015\u001a\u00020\u00032\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0019J(\u0010\u001a\u001a\u00020\u00032\u0006\u0010\u001b\u001a\u00020\u00172\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\fH\u0016J \u0010\u001f\u001a\u00020\u00032\u0006\u0010 \u001a\u00020\f2\u0006\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020\u0010H\u0016J.\u0010$\u001a\u00020\u00032\u0006\u0010\u001b\u001a\u00020\u00172\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010%\u001a\u00020\f2\f\u0010&\u001a\b\u0012\u0004\u0012\u00020(0'H\u0016J\t\u0010)\u001a\u00020\u0003H\u0096\u0002J \u0010*\u001a\u00020\u00032\u0006\u0010+\u001a\u00020\u00172\u0006\u0010,\u001a\u00020\f2\u0006\u0010-\u001a\u00020\fH\u0016J(\u0010.\u001a\u00020\u00032\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010/\u001a\u00020\f2\u0006\u00100\u001a\u00020\f2\u0006\u00101\u001a\u00020\u0017H\u0016J&\u00102\u001a\u00020\u00032\u0006\u0010\u000b\u001a\u00020\f2\u0006\u00103\u001a\u00020\f2\f\u00104\u001a\b\u0012\u0004\u0012\u00020(0'H\u0016J\u0018\u00105\u001a\u00020\u00032\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010!\u001a\u00020\"H\u0016J\u0018\u0010\u0018\u001a\u00020\u00032\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0019H\u0016J\u0018\u00106\u001a\u00020\u00032\u0006\u0010\u000b\u001a\u00020\f2\u0006\u00107\u001a\u00020\u0014H\u0016R\u0014\u0010\u0004\u001a\u00020\u0005X\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\b\u00a8\u00068"}, d2={"Lokhttp3/internal/http2/Http2Connection$ReaderRunnable;", "Lokhttp3/internal/http2/Http2Reader$Handler;", "Lkotlin/Function0;", "", "reader", "Lokhttp3/internal/http2/Http2Reader;", "(Lokhttp3/internal/http2/Http2Connection;Lokhttp3/internal/http2/Http2Reader;)V", "getReader$okhttp", "()Lokhttp3/internal/http2/Http2Reader;", "ackSettings", "alternateService", "streamId", "", "origin", "", "protocol", "Lokio/ByteString;", "host", "port", "maxAge", "", "applyAndAckSettings", "clearPrevious", "", "settings", "Lokhttp3/internal/http2/Settings;", "data", "inFinished", "source", "Lokio/BufferedSource;", "length", "goAway", "lastGoodStreamId", "errorCode", "Lokhttp3/internal/http2/ErrorCode;", "debugData", "headers", "associatedStreamId", "headerBlock", "", "Lokhttp3/internal/http2/Header;", "invoke", "ping", "ack", "payload1", "payload2", "priority", "streamDependency", "weight", "exclusive", "pushPromise", "promisedStreamId", "requestHeaders", "rstStream", "windowUpdate", "windowSizeIncrement", "okhttp"})
    @SourceDebugExtension(value={"SMAP\nHttp2Connection.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Http2Connection.kt\nokhttp3/internal/http2/Http2Connection$ReaderRunnable\n+ 2 TaskQueue.kt\nokhttp3/internal/concurrent/TaskQueue\n+ 3 ArraysJVM.kt\nkotlin/collections/ArraysKt__ArraysJVMKt\n+ 4 Util.kt\nokhttp3/internal/Util\n*L\n1#1,1006:1\n90#2,13:1007\n90#2,13:1020\n90#2,13:1035\n90#2,13:1049\n37#3,2:1033\n37#3,2:1062\n563#4:1048\n563#4:1064\n*S KotlinDebug\n*F\n+ 1 Http2Connection.kt\nokhttp3/internal/http2/Http2Connection$ReaderRunnable\n*L\n687#1:1007,13\n715#1:1020,13\n758#1:1035,13\n806#1:1049,13\n753#1:1033,2\n824#1:1062,2\n797#1:1048\n841#1:1064\n*E\n"})
    public final class ReaderRunnable
    implements Function0<Unit>,
    Http2Reader.Handler {
        @NotNull
        private final Http2Reader reader;

        public ReaderRunnable(Http2Reader http2Reader) {
            Intrinsics.checkNotNullParameter(http2Reader, "");
            this.reader = http2Reader;
        }

        @NotNull
        public final Http2Reader getReader$okhttp() {
            return this.reader;
        }

        @Override
        public final void invoke() {
            ErrorCode errorCode = ErrorCode.INTERNAL_ERROR;
            ErrorCode errorCode2 = ErrorCode.INTERNAL_ERROR;
            IOException iOException = null;
            try {
                this.reader.readConnectionPreface(this);
                while (this.reader.nextFrame(false, this)) {
                }
                errorCode = ErrorCode.NO_ERROR;
                errorCode2 = ErrorCode.CANCEL;
                Http2Connection.this.close$okhttp(errorCode, errorCode2, null);
            }
            catch (IOException iOException2) {
                IOException iOException3 = iOException2;
                iOException = iOException2;
                errorCode = ErrorCode.PROTOCOL_ERROR;
                errorCode2 = ErrorCode.PROTOCOL_ERROR;
                return;
            }
            finally {
                Http2Connection.this.close$okhttp(errorCode, errorCode2, iOException);
                Util.closeQuietly(this.reader);
            }
            Util.closeQuietly(this.reader);
            return;
        }

        @Override
        public final void data(boolean bl, int n2, @NotNull BufferedSource bufferedSource, int n3) throws IOException {
            Intrinsics.checkNotNullParameter(bufferedSource, "");
            if (Http2Connection.this.pushedStream$okhttp(n2)) {
                Http2Connection.this.pushDataLater$okhttp(n2, bufferedSource, n3, bl);
                return;
            }
            Http2Stream http2Stream = Http2Connection.this.getStream(n2);
            if (http2Stream == null) {
                Http2Connection.this.writeSynResetLater$okhttp(n2, ErrorCode.PROTOCOL_ERROR);
                Http2Connection.this.updateConnectionFlowControl$okhttp(n3);
                bufferedSource.skip(n3);
                return;
            }
            http2Stream.receiveData(bufferedSource, n3);
            if (bl) {
                http2Stream.receiveHeaders(Util.EMPTY_HEADERS, true);
            }
        }

        @Override
        public final void headers(boolean bl, int n2, int n3, @NotNull List<Header> object) {
            Intrinsics.checkNotNullParameter(object, "");
            if (Http2Connection.this.pushedStream$okhttp(n2)) {
                Http2Connection.this.pushHeadersLater$okhttp(n2, (List<Header>)object, bl);
                return;
            }
            Object object2 = Http2Connection.this;
            Http2Connection http2Connection = Http2Connection.this;
            Http2Connection http2Connection2 = object2;
            synchronized (http2Connection2) {
                object2 = http2Connection.getStream(n2);
                if (object2 == null) {
                    if (http2Connection.isShutdown) {
                        return;
                    }
                    if (n2 <= http2Connection.getLastGoodStreamId$okhttp()) {
                        return;
                    }
                    if (n2 % 2 == http2Connection.getNextStreamId$okhttp() % 2) {
                        return;
                    }
                    object2 = Util.toHeaders(object);
                    Http2Stream http2Stream = new Http2Stream(n2, http2Connection, false, bl, (Headers)object2);
                    http2Connection.setLastGoodStreamId$okhttp(n2);
                    object2 = n2;
                    http2Connection.getStreams$okhttp().put((Integer)object2, http2Stream);
                    object = http2Connection.taskRunner.newQueue();
                    object2 = http2Connection.getConnectionName$okhttp() + '[' + n2 + "] onStream";
                    ((TaskQueue)object).schedule(new Task((String)object2, true, http2Connection, http2Stream){
                        final /* synthetic */ Http2Connection this$0;
                        final /* synthetic */ Http2Stream $newStream$inlined;
                        {
                            this.this$0 = http2Connection;
                            this.$newStream$inlined = http2Stream;
                            super(string, bl);
                        }

                        public final long runOnce() {
                            try {
                                this.this$0.getListener$okhttp().onStream(this.$newStream$inlined);
                            }
                            catch (IOException iOException) {
                                Platform.Companion.get().log("Http2Connection.Listener failure for " + this.this$0.getConnectionName$okhttp(), 4, iOException);
                                try {
                                    this.$newStream$inlined.close(ErrorCode.PROTOCOL_ERROR, iOException);
                                }
                                catch (IOException iOException2) {}
                            }
                            return -1L;
                        }
                    }, 0L);
                    return;
                }
            }
            ((Http2Stream)object2).receiveHeaders(Util.toHeaders(object), bl);
        }

        @Override
        public final void rstStream(int n2, @NotNull ErrorCode errorCode) {
            Intrinsics.checkNotNullParameter((Object)errorCode, "");
            if (Http2Connection.this.pushedStream$okhttp(n2)) {
                Http2Connection.this.pushResetLater$okhttp(n2, errorCode);
                return;
            }
            Http2Stream http2Stream = Http2Connection.this.removeStream$okhttp(n2);
            if (http2Stream != null) {
                http2Stream.receiveRstStream(errorCode);
                return;
            }
        }

        @Override
        public final void settings(boolean bl, @NotNull Settings settings) {
            Intrinsics.checkNotNullParameter(settings, "");
            TaskQueue taskQueue = Http2Connection.this.writerQueue;
            String string = Http2Connection.this.getConnectionName$okhttp() + " applyAndAckSettings";
            taskQueue.schedule(new Task(string, true, this, bl, settings){
                final /* synthetic */ ReaderRunnable this$0;
                final /* synthetic */ boolean $clearPrevious$inlined;
                final /* synthetic */ Settings $settings$inlined;
                {
                    this.this$0 = readerRunnable;
                    this.$clearPrevious$inlined = bl2;
                    this.$settings$inlined = settings;
                    super(string, bl);
                }

                public final long runOnce() {
                    this.this$0.applyAndAckSettings(this.$clearPrevious$inlined, this.$settings$inlined);
                    return -1L;
                }
            }, 0L);
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Converted monitor instructions to comments
         * Lifted jumps to return sites
         */
        public final void applyAndAckSettings(boolean bl, @NotNull Settings object) {
            Object object2;
            Http2Stream[] http2StreamArray;
            Settings settings;
            Object object3;
            Intrinsics.checkNotNullParameter(object3, "");
            Ref.ObjectRef objectRef = new Ref.ObjectRef();
            Http2Stream[] http2StreamArray2 = Http2Connection.this.getWriter();
            Http2Connection http2Connection = Http2Connection.this;
            Http2Writer http2Writer = http2StreamArray2;
            // MONITORENTER : http2Writer
            Object object4 = http2Connection;
            // MONITORENTER : object4
            http2StreamArray2 = http2Connection.getPeerSettings();
            Ref.ObjectRef objectRef2 = objectRef;
            if (bl) {
                settings = object3;
            } else {
                Settings settings2;
                Settings settings3 = settings2 = new Settings();
                Ref.ObjectRef objectRef3 = objectRef2;
                settings3.merge((Settings)http2StreamArray2);
                settings3.merge((Settings)object3);
                objectRef2 = objectRef3;
                settings = settings2;
            }
            objectRef2.element = settings;
            long l2 = ((Settings)objectRef.element).getInitialWindowSize();
            long l3 = l2 - (long)http2StreamArray2.getInitialWindowSize();
            if (l3 == 0L || http2Connection.getStreams$okhttp().isEmpty()) {
                http2StreamArray = null;
            } else {
                object2 = object3 = http2Connection.getStreams$okhttp().values();
                http2StreamArray = object3.toArray(new Http2Stream[0]);
            }
            object2 = http2StreamArray;
            http2Connection.setPeerSettings((Settings)objectRef.element);
            object3 = http2Connection.settingsListenerQueue;
            http2StreamArray2 = http2Connection.getConnectionName$okhttp() + " onSettings";
            ((TaskQueue)object3).schedule(new Task((String)http2StreamArray2, true, http2Connection, objectRef){
                final /* synthetic */ Http2Connection this$0;
                final /* synthetic */ Ref.ObjectRef $newPeerSettings$inlined;
                {
                    this.this$0 = http2Connection;
                    this.$newPeerSettings$inlined = objectRef;
                    super(string, bl);
                }

                public final long runOnce() {
                    this.this$0.getListener$okhttp().onSettings(this.this$0, (Settings)this.$newPeerSettings$inlined.element);
                    return -1L;
                }
            }, 0L);
            // MONITOREXIT : object4
            try {
                http2Connection.getWriter().applyAndAckSettings((Settings)objectRef.element);
            }
            catch (IOException iOException) {
                http2Connection.failConnection(iOException);
            }
            // MONITOREXIT : http2Writer
            if (object2 == null) return;
            http2StreamArray2 = object2;
            int n2 = 0;
            int n3 = http2StreamArray2.length;
            while (n2 < n3) {
                object4 = object2 = http2StreamArray2[n2];
                // MONITORENTER : object2
                ((Http2Stream)object2).addBytesToWriteWindow(l3);
                // MONITOREXIT : object4
                ++n2;
            }
        }

        @Override
        public final void ackSettings() {
        }

        @Override
        public final void ping(boolean bl, int n2, int n3) {
            if (bl) {
                Http2Connection http2Connection = Http2Connection.this;
                Http2Connection http2Connection2 = Http2Connection.this;
                synchronized (http2Connection) {
                    switch (n2) {
                        case 1: {
                            long l2 = http2Connection2.intervalPongsReceived;
                            http2Connection2.intervalPongsReceived = l2 + 1L;
                            Long.valueOf(l2);
                            break;
                        }
                        case 2: {
                            long l3 = http2Connection2.degradedPongsReceived;
                            http2Connection2.degradedPongsReceived = l3 + 1L;
                            Long.valueOf(l3);
                            break;
                        }
                        case 3: {
                            long l4 = http2Connection2.awaitPongsReceived;
                            http2Connection2.awaitPongsReceived = l4 + 1L;
                            Http2Connection http2Connection3 = http2Connection2;
                            Intrinsics.checkNotNull(http2Connection3);
                            ((Object)http2Connection3).notifyAll();
                            break;
                        }
                    }
                    return;
                }
            }
            TaskQueue taskQueue = Http2Connection.this.writerQueue;
            String string = Http2Connection.this.getConnectionName$okhttp() + " ping";
            Http2Connection http2Connection = Http2Connection.this;
            taskQueue.schedule(new Task(string, true, http2Connection, n2, n3){
                final /* synthetic */ Http2Connection this$0;
                final /* synthetic */ int $payload1$inlined;
                final /* synthetic */ int $payload2$inlined;
                {
                    this.this$0 = http2Connection;
                    this.$payload1$inlined = n2;
                    this.$payload2$inlined = n3;
                    super(string, bl);
                }

                public final long runOnce() {
                    this.this$0.writePing(true, this.$payload1$inlined, this.$payload2$inlined);
                    return -1L;
                }
            }, 0L);
        }

        @Override
        public final void goAway(int n2, @NotNull ErrorCode http2StreamArray, @NotNull ByteString object) {
            Intrinsics.checkNotNullParameter(http2StreamArray, "");
            Intrinsics.checkNotNullParameter(object, "");
            ((ByteString)object).size();
            http2StreamArray = Http2Connection.this;
            object = Http2Connection.this;
            Object object2 = http2StreamArray;
            synchronized (object2) {
                Collection<Http2Stream> collection = ((Http2Connection)object).getStreams$okhttp().values();
                http2StreamArray = collection;
                http2StreamArray = collection;
                http2StreamArray = collection.toArray(new Http2Stream[0]);
                ((Http2Connection)object).isShutdown = true;
            }
            for (Http2Stream http2Stream : http2StreamArray) {
                if (http2Stream.getId() <= n2 || !http2Stream.isLocallyInitiated()) continue;
                http2Stream.receiveRstStream(ErrorCode.REFUSED_STREAM);
                Http2Connection.this.removeStream$okhttp(http2Stream.getId());
            }
        }

        @Override
        public final void windowUpdate(int n2, long l2) {
            if (n2 == 0) {
                Http2Connection http2Connection = Http2Connection.this;
                Http2Connection http2Connection2 = Http2Connection.this;
                synchronized (http2Connection) {
                    Http2Connection http2Connection3 = http2Connection2;
                    http2Connection3.writeBytesMaximum = http2Connection3.getWriteBytesMaximum() + l2;
                    Http2Connection http2Connection4 = http2Connection2;
                    Intrinsics.checkNotNull(http2Connection4);
                    ((Object)http2Connection4).notifyAll();
                    return;
                }
            }
            Http2Stream http2Stream = Http2Connection.this.getStream(n2);
            if (http2Stream != null) {
                Http2Stream http2Stream2 = http2Stream;
                synchronized (http2Stream2) {
                    http2Stream.addBytesToWriteWindow(l2);
                    return;
                }
            }
        }

        @Override
        public final void priority(int n2, int n3, int n4, boolean bl) {
        }

        @Override
        public final void pushPromise(int n2, int n3, @NotNull List<Header> list2) {
            Intrinsics.checkNotNullParameter(list2, "");
            Http2Connection.this.pushRequestLater$okhttp(n3, list2);
        }

        @Override
        public final void alternateService(int n2, @NotNull String string, @NotNull ByteString byteString, @NotNull String string2, int n3, long l2) {
            Intrinsics.checkNotNullParameter(string, "");
            Intrinsics.checkNotNullParameter(byteString, "");
            Intrinsics.checkNotNullParameter(string2, "");
        }
    }
}


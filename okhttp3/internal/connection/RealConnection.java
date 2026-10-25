/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.connection;

import java.io.IOException;
import java.lang.ref.Reference;
import java.net.ConnectException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownServiceException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.StringsKt;
import okhttp3.Address;
import okhttp3.Call;
import okhttp3.CertificatePinner;
import okhttp3.Connection;
import okhttp3.ConnectionSpec;
import okhttp3.EventListener;
import okhttp3.Handshake;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.internal.Util;
import okhttp3.internal.concurrent.TaskRunner;
import okhttp3.internal.connection.ConnectionSpecSelector;
import okhttp3.internal.connection.Exchange;
import okhttp3.internal.connection.RealCall;
import okhttp3.internal.connection.RealConnectionPool;
import okhttp3.internal.connection.RouteException;
import okhttp3.internal.http.ExchangeCodec;
import okhttp3.internal.http.RealInterceptorChain;
import okhttp3.internal.http1.Http1ExchangeCodec;
import okhttp3.internal.http2.ConnectionShutdownException;
import okhttp3.internal.http2.ErrorCode;
import okhttp3.internal.http2.Http2Connection;
import okhttp3.internal.http2.Http2ExchangeCodec;
import okhttp3.internal.http2.Http2Stream;
import okhttp3.internal.http2.Settings;
import okhttp3.internal.http2.StreamResetException;
import okhttp3.internal.platform.Platform;
import okhttp3.internal.tls.CertificateChainCleaner;
import okhttp3.internal.tls.OkHostnameVerifier;
import okhttp3.internal.ws.RealWebSocket;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\u00ec\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0005\u0018\u0000 {2\u00020\u00012\u00020\u0002:\u0001{B\u0015\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007J\u0006\u00105\u001a\u000206J\u0018\u00107\u001a\u00020\u001d2\u0006\u00108\u001a\u0002092\u0006\u0010\u0012\u001a\u00020\u0013H\u0002J>\u0010:\u001a\u0002062\u0006\u0010;\u001a\u00020\t2\u0006\u0010<\u001a\u00020\t2\u0006\u0010=\u001a\u00020\t2\u0006\u0010>\u001a\u00020\t2\u0006\u0010?\u001a\u00020\u001d2\u0006\u0010@\u001a\u00020A2\u0006\u0010B\u001a\u00020CJ%\u0010D\u001a\u0002062\u0006\u0010E\u001a\u00020F2\u0006\u0010G\u001a\u00020\u00062\u0006\u0010H\u001a\u00020IH\u0000\u00a2\u0006\u0002\bJJ(\u0010K\u001a\u0002062\u0006\u0010;\u001a\u00020\t2\u0006\u0010<\u001a\u00020\t2\u0006\u0010@\u001a\u00020A2\u0006\u0010B\u001a\u00020CH\u0002J\u0010\u0010L\u001a\u0002062\u0006\u0010M\u001a\u00020NH\u0002J0\u0010O\u001a\u0002062\u0006\u0010;\u001a\u00020\t2\u0006\u0010<\u001a\u00020\t2\u0006\u0010=\u001a\u00020\t2\u0006\u0010@\u001a\u00020A2\u0006\u0010B\u001a\u00020CH\u0002J*\u0010P\u001a\u0004\u0018\u00010Q2\u0006\u0010<\u001a\u00020\t2\u0006\u0010=\u001a\u00020\t2\u0006\u0010R\u001a\u00020Q2\u0006\u00108\u001a\u000209H\u0002J\b\u0010S\u001a\u00020QH\u0002J(\u0010T\u001a\u0002062\u0006\u0010M\u001a\u00020N2\u0006\u0010>\u001a\u00020\t2\u0006\u0010@\u001a\u00020A2\u0006\u0010B\u001a\u00020CH\u0002J\n\u0010\u0012\u001a\u0004\u0018\u00010\u0013H\u0016J\r\u0010U\u001a\u000206H\u0000\u00a2\u0006\u0002\bVJ%\u0010W\u001a\u00020\u001d2\u0006\u0010X\u001a\u00020Y2\u000e\u0010Z\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010[H\u0000\u00a2\u0006\u0002\b\\J\u000e\u0010]\u001a\u00020\u001d2\u0006\u0010^\u001a\u00020\u001dJ\u001d\u0010_\u001a\u00020`2\u0006\u0010E\u001a\u00020F2\u0006\u0010a\u001a\u00020bH\u0000\u00a2\u0006\u0002\bcJ\u0015\u0010d\u001a\u00020e2\u0006\u0010f\u001a\u00020gH\u0000\u00a2\u0006\u0002\bhJ\r\u0010 \u001a\u000206H\u0000\u00a2\u0006\u0002\biJ\r\u0010!\u001a\u000206H\u0000\u00a2\u0006\u0002\bjJ\u0018\u0010k\u001a\u0002062\u0006\u0010l\u001a\u00020\u00152\u0006\u0010m\u001a\u00020nH\u0016J\u0010\u0010o\u001a\u0002062\u0006\u0010p\u001a\u00020qH\u0016J\b\u0010%\u001a\u00020&H\u0016J\b\u0010\u0005\u001a\u00020\u0006H\u0016J\u0016\u0010r\u001a\u00020\u001d2\f\u0010s\u001a\b\u0012\u0004\u0012\u00020\u00060[H\u0002J\b\u00101\u001a\u00020(H\u0016J\u0010\u0010t\u001a\u0002062\u0006\u0010>\u001a\u00020\tH\u0002J\u0010\u0010u\u001a\u00020\u001d2\u0006\u00108\u001a\u000209H\u0002J\b\u0010v\u001a\u00020wH\u0016J\u001f\u0010x\u001a\u0002062\u0006\u0010@\u001a\u00020\r2\b\u0010y\u001a\u0004\u0018\u00010IH\u0000\u00a2\u0006\u0002\bzR\u000e\u0010\b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001d\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\f0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0015X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0016\u001a\u00020\u0017X\u0080\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0019\"\u0004\b\u001a\u0010\u001bR\u0014\u0010\u001c\u001a\u00020\u001d8@X\u0080\u0004\u00a2\u0006\u0006\u001a\u0004\b\u001e\u0010\u001fR\u000e\u0010 \u001a\u00020\u001dX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010!\u001a\u00020\u001dX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\"\u0010\u001f\"\u0004\b#\u0010$R\u0010\u0010%\u001a\u0004\u0018\u00010&X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010'\u001a\u0004\u0018\u00010(X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010*\u001a\u00020\tX\u0080\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b+\u0010,\"\u0004\b-\u0010.R\u0010\u0010/\u001a\u0004\u0018\u000100X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u00101\u001a\u0004\u0018\u00010(X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u00102\u001a\u0004\u0018\u000103X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u00104\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006|"}, d2={"Lokhttp3/internal/connection/RealConnection;", "Lokhttp3/internal/http2/Http2Connection$Listener;", "Lokhttp3/Connection;", "connectionPool", "Lokhttp3/internal/connection/RealConnectionPool;", "route", "Lokhttp3/Route;", "(Lokhttp3/internal/connection/RealConnectionPool;Lokhttp3/Route;)V", "allocationLimit", "", "calls", "", "Ljava/lang/ref/Reference;", "Lokhttp3/internal/connection/RealCall;", "getCalls", "()Ljava/util/List;", "getConnectionPool", "()Lokhttp3/internal/connection/RealConnectionPool;", "handshake", "Lokhttp3/Handshake;", "http2Connection", "Lokhttp3/internal/http2/Http2Connection;", "idleAtNs", "", "getIdleAtNs$okhttp", "()J", "setIdleAtNs$okhttp", "(J)V", "isMultiplexed", "", "isMultiplexed$okhttp", "()Z", "noCoalescedConnections", "noNewExchanges", "getNoNewExchanges", "setNoNewExchanges", "(Z)V", "protocol", "Lokhttp3/Protocol;", "rawSocket", "Ljava/net/Socket;", "refusedStreamCount", "routeFailureCount", "getRouteFailureCount$okhttp", "()I", "setRouteFailureCount$okhttp", "(I)V", "sink", "Lokio/BufferedSink;", "socket", "source", "Lokio/BufferedSource;", "successCount", "cancel", "", "certificateSupportHost", "url", "Lokhttp3/HttpUrl;", "connect", "connectTimeout", "readTimeout", "writeTimeout", "pingIntervalMillis", "connectionRetryEnabled", "call", "Lokhttp3/Call;", "eventListener", "Lokhttp3/EventListener;", "connectFailed", "client", "Lokhttp3/OkHttpClient;", "failedRoute", "failure", "Ljava/io/IOException;", "connectFailed$okhttp", "connectSocket", "connectTls", "connectionSpecSelector", "Lokhttp3/internal/connection/ConnectionSpecSelector;", "connectTunnel", "createTunnel", "Lokhttp3/Request;", "tunnelRequest", "createTunnelRequest", "establishProtocol", "incrementSuccessCount", "incrementSuccessCount$okhttp", "isEligible", "address", "Lokhttp3/Address;", "routes", "", "isEligible$okhttp", "isHealthy", "doExtensiveChecks", "newCodec", "Lokhttp3/internal/http/ExchangeCodec;", "chain", "Lokhttp3/internal/http/RealInterceptorChain;", "newCodec$okhttp", "newWebSocketStreams", "Lokhttp3/internal/ws/RealWebSocket$Streams;", "exchange", "Lokhttp3/internal/connection/Exchange;", "newWebSocketStreams$okhttp", "noCoalescedConnections$okhttp", "noNewExchanges$okhttp", "onSettings", "connection", "settings", "Lokhttp3/internal/http2/Settings;", "onStream", "stream", "Lokhttp3/internal/http2/Http2Stream;", "routeMatchesAny", "candidates", "startHttp2", "supportsUrl", "toString", "", "trackFailure", "e", "trackFailure$okhttp", "Companion", "okhttp"})
@SourceDebugExtension(value={"SMAP\nRealConnection.kt\nKotlin\n*S Kotlin\n*F\n+ 1 RealConnection.kt\nokhttp3/internal/connection/RealConnection\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 Util.kt\nokhttp3/internal/Util\n+ 4 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,765:1\n1#2:766\n608#3,4:767\n608#3,4:774\n615#3,4:778\n1747#4,3:771\n*S KotlinDebug\n*F\n+ 1 RealConnection.kt\nokhttp3/internal/connection/RealConnection\n*L\n529#1:767,4\n582#1:774,4\n648#1:778,4\n574#1:771,3\n*E\n"})
public final class RealConnection
extends Http2Connection.Listener
implements Connection {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final RealConnectionPool connectionPool;
    @NotNull
    private final Route route;
    @Nullable
    private Socket rawSocket;
    @Nullable
    private Socket socket;
    @Nullable
    private Handshake handshake;
    @Nullable
    private Protocol protocol;
    @Nullable
    private Http2Connection http2Connection;
    @Nullable
    private BufferedSource source;
    @Nullable
    private BufferedSink sink;
    private boolean noNewExchanges;
    private boolean noCoalescedConnections;
    private int routeFailureCount;
    private int successCount;
    private int refusedStreamCount;
    private int allocationLimit;
    @NotNull
    private final List<Reference<RealCall>> calls;
    private long idleAtNs;
    @NotNull
    private static final String NPE_THROW_WITH_NULL = "throw with null exception";
    private static final int MAX_TUNNEL_ATTEMPTS = 21;
    public static final long IDLE_CONNECTION_HEALTHY_NS = 10000000000L;

    public RealConnection(@NotNull RealConnectionPool realConnectionPool, @NotNull Route route) {
        Intrinsics.checkNotNullParameter(realConnectionPool, "");
        Intrinsics.checkNotNullParameter(route, "");
        this.connectionPool = realConnectionPool;
        this.route = route;
        this.allocationLimit = 1;
        this.calls = new ArrayList();
        this.idleAtNs = Long.MAX_VALUE;
    }

    @NotNull
    public final RealConnectionPool getConnectionPool() {
        return this.connectionPool;
    }

    public final boolean getNoNewExchanges() {
        return this.noNewExchanges;
    }

    public final void setNoNewExchanges(boolean bl) {
        this.noNewExchanges = bl;
    }

    public final int getRouteFailureCount$okhttp() {
        return this.routeFailureCount;
    }

    public final void setRouteFailureCount$okhttp(int n2) {
        this.routeFailureCount = n2;
    }

    @NotNull
    public final List<Reference<RealCall>> getCalls() {
        return this.calls;
    }

    public final long getIdleAtNs$okhttp() {
        return this.idleAtNs;
    }

    public final void setIdleAtNs$okhttp(long l2) {
        this.idleAtNs = l2;
    }

    public final boolean isMultiplexed$okhttp() {
        return this.http2Connection != null;
    }

    public final synchronized void noNewExchanges$okhttp() {
        this.noNewExchanges = true;
    }

    public final synchronized void noCoalescedConnections$okhttp() {
        this.noCoalescedConnections = true;
    }

    public final synchronized void incrementSuccessCount$okhttp() {
        int n2 = this.successCount;
        this.successCount = n2 + 1;
    }

    public final void connect(int n2, int n3, int n4, int n5, boolean bl, @NotNull Call call, @NotNull EventListener eventListener) {
        Intrinsics.checkNotNullParameter(call, "");
        Intrinsics.checkNotNullParameter(eventListener, "");
        if (!(this.protocol == null)) {
            String string = "already connected";
            throw new IllegalStateException(string.toString());
        }
        RouteException routeException = null;
        Object object = this.route.address().connectionSpecs();
        ConnectionSpecSelector connectionSpecSelector = new ConnectionSpecSelector((List<ConnectionSpec>)object);
        if (this.route.address().sslSocketFactory() == null) {
            if (!object.contains(ConnectionSpec.CLEARTEXT)) {
                throw new RouteException(new UnknownServiceException("CLEARTEXT communication not enabled for client"));
            }
            object = this.route.address().url().host();
            if (!Platform.Companion.get().isCleartextTrafficPermitted((String)object)) {
                throw new RouteException(new UnknownServiceException("CLEARTEXT communication to " + (String)object + " not permitted by network security policy"));
            }
        } else if (this.route.address().protocols().contains((Object)Protocol.H2_PRIOR_KNOWLEDGE)) {
            throw new RouteException(new UnknownServiceException("H2_PRIOR_KNOWLEDGE cannot be used with HTTPS"));
        }
        while (true) {
            try {
                if (this.route.requiresTunnel()) {
                    this.connectTunnel(n2, n3, n4, call, eventListener);
                    if (this.rawSocket == null) {
                        break;
                    }
                } else {
                    this.connectSocket(n2, n3, call, eventListener);
                }
                this.establishProtocol(connectionSpecSelector, n5, call, eventListener);
                eventListener.connectEnd(call, this.route.socketAddress(), this.route.proxy(), this.protocol);
            }
            catch (IOException iOException) {
                Socket socket = this.socket;
                if (socket != null) {
                    Util.closeQuietly(socket);
                }
                Socket socket2 = this.rawSocket;
                if (socket2 != null) {
                    Util.closeQuietly(socket2);
                }
                this.socket = null;
                this.rawSocket = null;
                this.source = null;
                this.sink = null;
                this.handshake = null;
                this.protocol = null;
                this.http2Connection = null;
                this.allocationLimit = 1;
                eventListener.connectFailed(call, this.route.socketAddress(), this.route.proxy(), null, iOException);
                if (routeException == null) {
                    routeException = new RouteException(iOException);
                    continue;
                }
                routeException.addConnectException(iOException);
                if (bl && connectionSpecSelector.connectionFailed(iOException)) continue;
                throw routeException;
            }
            break;
        }
        if (this.route.requiresTunnel() && this.rawSocket == null) {
            throw new RouteException(new ProtocolException("Too many tunnel connections attempted: 21"));
        }
        this.idleAtNs = System.nanoTime();
    }

    private final void connectTunnel(int n2, int n3, int n4, Call call, EventListener eventListener) throws IOException {
        Request request = this.createTunnelRequest();
        HttpUrl httpUrl = request.url();
        for (int i2 = 0; i2 < 21; ++i2) {
            this.connectSocket(n2, n3, call, eventListener);
            if (this.createTunnel(n3, n4, request, httpUrl) == null) {
                return;
            }
            Socket socket = this.rawSocket;
            if (socket != null) {
                Util.closeQuietly(socket);
            }
            this.rawSocket = null;
            this.sink = null;
            this.source = null;
            eventListener.connectEnd(call, this.route.socketAddress(), this.route.proxy(), null);
        }
    }

    private final void connectSocket(int n2, int n3, Call object, EventListener eventListener) throws IOException {
        Socket socket;
        Proxy proxy = this.route.proxy();
        Object object2 = this.route.address();
        Proxy.Type type = proxy.type();
        switch (type == null ? -1 : WhenMappings.$EnumSwitchMapping$0[type.ordinal()]) {
            case 1: 
            case 2: {
                Socket socket2 = ((Address)object2).socketFactory().createSocket();
                socket = socket2;
                Intrinsics.checkNotNull(socket2);
                break;
            }
            default: {
                socket = new Socket(proxy);
            }
        }
        this.rawSocket = object2 = socket;
        eventListener.connectStart((Call)object, this.route.socketAddress(), proxy);
        ((Socket)object2).setSoTimeout(n3);
        try {
            Platform.Companion.get().connectSocket((Socket)object2, this.route.socketAddress(), n2);
        }
        catch (ConnectException connectException) {
            ConnectException connectException2 = new ConnectException("Failed to connect to " + this.route.socketAddress());
            object = connectException2;
            connectException2.initCause(connectException);
            throw (Throwable)connectException2;
        }
        try {
            this.source = Okio.buffer(Okio.source((Socket)object2));
            this.sink = Okio.buffer(Okio.sink((Socket)object2));
            return;
        }
        catch (NullPointerException nullPointerException) {
            NullPointerException nullPointerException2 = nullPointerException;
            if (Intrinsics.areEqual(nullPointerException.getMessage(), NPE_THROW_WITH_NULL)) {
                throw new IOException(nullPointerException2);
            }
            return;
        }
    }

    private final void establishProtocol(ConnectionSpecSelector connectionSpecSelector, int n2, Call call, EventListener eventListener) throws IOException {
        if (this.route.address().sslSocketFactory() == null) {
            if (this.route.address().protocols().contains((Object)Protocol.H2_PRIOR_KNOWLEDGE)) {
                this.socket = this.rawSocket;
                this.protocol = Protocol.H2_PRIOR_KNOWLEDGE;
                this.startHttp2(n2);
                return;
            }
            this.socket = this.rawSocket;
            this.protocol = Protocol.HTTP_1_1;
            return;
        }
        eventListener.secureConnectStart(call);
        this.connectTls(connectionSpecSelector);
        eventListener.secureConnectEnd(call, this.handshake);
        if (this.protocol == Protocol.HTTP_2) {
            this.startHttp2(n2);
        }
    }

    private final void startHttp2(int n2) throws IOException {
        Http2Connection http2Connection;
        Socket socket = this.socket;
        Intrinsics.checkNotNull(socket);
        Socket socket2 = socket;
        BufferedSource bufferedSource = this.source;
        Intrinsics.checkNotNull(bufferedSource);
        BufferedSource bufferedSource2 = bufferedSource;
        BufferedSink bufferedSink = this.sink;
        Intrinsics.checkNotNull(bufferedSink);
        BufferedSink bufferedSink2 = bufferedSink;
        socket2.setSoTimeout(0);
        this.http2Connection = http2Connection = new Http2Connection.Builder(true, TaskRunner.INSTANCE).socket(socket2, this.route.address().url().host(), bufferedSource2, bufferedSink2).listener(this).pingIntervalMillis(n2).build();
        this.allocationLimit = Http2Connection.Companion.getDEFAULT_SETTINGS().getMaxConcurrentStreams();
        Http2Connection.start$default(http2Connection, false, null, 3, null);
    }

    private final void connectTls(ConnectionSpecSelector object) throws IOException {
        Address address = this.route.address();
        List<Certificate> list2 = address.sslSocketFactory();
        SSLSocket sSLSocket = null;
        try {
            SSLSocketFactory sSLSocketFactory = list2;
            Intrinsics.checkNotNull(sSLSocketFactory);
            Socket socket = sSLSocketFactory.createSocket(this.rawSocket, address.url().host(), address.url().port(), true);
            Intrinsics.checkNotNull(socket);
            sSLSocket = (SSLSocket)socket;
            object = ((ConnectionSpecSelector)object).configureSecureSocket(sSLSocket);
            if (((ConnectionSpec)object).supportsTlsExtensions()) {
                Platform.Companion.get().configureTlsExtensions(sSLSocket, address.url().host(), address.protocols());
            }
            sSLSocket.startHandshake();
            list2 = sSLSocket.getSession();
            Intrinsics.checkNotNullExpressionValue(list2, "");
            Handshake handshake2 = Handshake.Companion.get((SSLSession)((Object)list2));
            HostnameVerifier hostnameVerifier = address.hostnameVerifier();
            Intrinsics.checkNotNull(hostnameVerifier);
            if (!hostnameVerifier.verify(address.url().host(), (SSLSession)((Object)list2))) {
                list2 = handshake2.peerCertificates();
                if (!((Collection)list2).isEmpty()) {
                    Object e2 = list2.get(0);
                    Intrinsics.checkNotNull(e2);
                    object = (X509Certificate)e2;
                    throw new SSLPeerUnverifiedException(StringsKt.trimMargin$default("\n              |Hostname " + address.url().host() + " not verified:\n              |    certificate: " + CertificatePinner.Companion.pin((Certificate)object) + "\n              |    DN: " + ((X509Certificate)object).getSubjectDN().getName() + "\n              |    subjectAltNames: " + OkHostnameVerifier.INSTANCE.allSubjectAltNames((X509Certificate)object) + "\n              ", null, 1, null));
                }
                throw new SSLPeerUnverifiedException("Hostname " + address.url().host() + " not verified (no certificates)");
            }
            CertificatePinner certificatePinner = address.certificatePinner();
            Intrinsics.checkNotNull(certificatePinner);
            list2 = certificatePinner;
            this.handshake = new Handshake(handshake2.tlsVersion(), handshake2.cipherSuite(), handshake2.localCertificates(), (Function0<? extends List<? extends Certificate>>)new Function0<List<? extends Certificate>>((CertificatePinner)((Object)list2), handshake2, address){
                final /* synthetic */ CertificatePinner $certificatePinner;
                final /* synthetic */ Handshake $unverifiedHandshake;
                final /* synthetic */ Address $address;
                {
                    this.$certificatePinner = certificatePinner;
                    this.$unverifiedHandshake = handshake2;
                    this.$address = address;
                    super(0);
                }

                @NotNull
                public final List<Certificate> invoke() {
                    CertificateChainCleaner certificateChainCleaner = this.$certificatePinner.getCertificateChainCleaner$okhttp();
                    Intrinsics.checkNotNull(certificateChainCleaner);
                    return certificateChainCleaner.clean(this.$unverifiedHandshake.peerCertificates(), this.$address.url().host());
                }
            });
            ((CertificatePinner)((Object)list2)).check$okhttp(address.url().host(), (Function0<? extends List<? extends X509Certificate>>)new Function0<List<? extends X509Certificate>>(this){
                final /* synthetic */ RealConnection this$0;
                {
                    this.this$0 = realConnection;
                    super(0);
                }

                @NotNull
                public final List<X509Certificate> invoke() {
                    Handshake handshake2 = RealConnection.access$getHandshake$p(this.this$0);
                    Intrinsics.checkNotNull(handshake2);
                    Iterable iterable = handshake2.peerCertificates();
                    Object object = iterable;
                    iterable = new ArrayList<E>(CollectionsKt.collectionSizeOrDefault(iterable, 10));
                    object = object.iterator();
                    while (object.hasNext()) {
                        Object object2 = object.next();
                        object2 = (Certificate)object2;
                        Iterable iterable2 = iterable;
                        Intrinsics.checkNotNull(object2);
                        iterable2.add((X509Certificate)object2);
                    }
                    return (List)iterable;
                }
            });
            object = ((ConnectionSpec)object).supportsTlsExtensions() ? Platform.Companion.get().getSelectedProtocol(sSLSocket) : null;
            this.socket = sSLSocket;
            this.source = Okio.buffer(Okio.source(sSLSocket));
            this.sink = Okio.buffer(Okio.sink(sSLSocket));
            Object object2 = object;
            this.protocol = object2 != null ? Protocol.Companion.get((String)object2) : Protocol.HTTP_1_1;
            Platform.Companion.get().afterHandshake(sSLSocket);
            return;
        }
        catch (Throwable throwable) {
            block6: {
                if (sSLSocket != null) {
                    Platform.Companion.get().afterHandshake(sSLSocket);
                }
                SSLSocket sSLSocket2 = sSLSocket;
                if (sSLSocket2 == null) break block6;
                Util.closeQuietly(sSLSocket2);
            }
            throw throwable;
        }
    }

    private final Request createTunnel(int n2, int n3, Request request, HttpUrl object) throws IOException {
        Response response;
        object = "CONNECT " + Util.toHostHeader((HttpUrl)object, true) + " HTTP/1.1";
        block4: while (true) {
            BufferedSink bufferedSink;
            BufferedSource bufferedSource;
            Intrinsics.checkNotNull(this.source);
            Intrinsics.checkNotNull(this.sink);
            Http1ExchangeCodec http1ExchangeCodec = new Http1ExchangeCodec(null, this, bufferedSource, bufferedSink);
            bufferedSource.timeout().timeout(n2, TimeUnit.MILLISECONDS);
            bufferedSink.timeout().timeout(n3, TimeUnit.MILLISECONDS);
            http1ExchangeCodec.writeRequest(request.headers(), (String)object);
            http1ExchangeCodec.finishRequest();
            Response.Builder builder = http1ExchangeCodec.readResponseHeaders(false);
            Intrinsics.checkNotNull(builder);
            response = builder.request(request).build();
            http1ExchangeCodec.skipConnectBody(response);
            switch (response.code()) {
                case 200: {
                    if (!bufferedSource.getBuffer().exhausted() || !bufferedSink.getBuffer().exhausted()) {
                        throw new IOException("TLS tunnel buffered too many bytes!");
                    }
                    return null;
                }
                case 407: {
                    if (this.route.address().proxyAuthenticator().authenticate(this.route, response) != null) continue block4;
                    throw new IOException("Failed to authenticate with proxy");
                    if (!StringsKt.equals("close", Response.header$default(response, "Connection", null, 2, null), true)) continue block4;
                    return request;
                }
            }
            break;
        }
        throw new IOException("Unexpected response code for CONNECT: " + response.code());
    }

    private final Request createTunnelRequest() throws IOException {
        Request request = new Request.Builder().url(this.route.address().url()).method("CONNECT", null).header("Host", Util.toHostHeader(this.route.address().url(), true)).header("Proxy-Connection", "Keep-Alive").header("User-Agent", "okhttp/4.12.0").build();
        Object object = new Response.Builder().request(request).protocol(Protocol.HTTP_1_1).code(407).message("Preemptive Authenticate").body(Util.EMPTY_RESPONSE).sentRequestAtMillis(-1L).receivedResponseAtMillis(-1L).header("Proxy-Authenticate", "OkHttp-Preemptive").build();
        object = this.route.address().proxyAuthenticator().authenticate(this.route, (Response)object);
        Request request2 = object;
        if (object == null) {
            request2 = request;
        }
        return request2;
    }

    public final boolean isEligible$okhttp(@NotNull Address address, @Nullable List<Route> list2) {
        Intrinsics.checkNotNullParameter(address, "");
        RealConnection realConnection = this;
        if (Util.assertionsEnabled && !Thread.holdsLock(realConnection)) {
            throw new AssertionError((Object)("Thread " + Thread.currentThread().getName() + " MUST hold lock on " + realConnection));
        }
        if (this.calls.size() >= this.allocationLimit || this.noNewExchanges) {
            return false;
        }
        if (!this.route.address().equalsNonHost$okhttp(address)) {
            return false;
        }
        if (Intrinsics.areEqual(address.url().host(), this.route().address().url().host())) {
            return true;
        }
        if (this.http2Connection == null) {
            return false;
        }
        if (list2 == null || !this.routeMatchesAny(list2)) {
            return false;
        }
        if (address.hostnameVerifier() != OkHostnameVerifier.INSTANCE) {
            return false;
        }
        if (!this.supportsUrl(address.url())) {
            return false;
        }
        try {
            CertificatePinner certificatePinner = address.certificatePinner();
            Intrinsics.checkNotNull(certificatePinner);
            String string = address.url().host();
            Handshake handshake2 = this.handshake();
            Intrinsics.checkNotNull(handshake2);
            certificatePinner.check(string, handshake2.peerCertificates());
        }
        catch (SSLPeerUnverifiedException sSLPeerUnverifiedException) {
            return false;
        }
        return true;
    }

    private final boolean routeMatchesAny(List<Route> object) {
        if (!((object = (Iterable)object) instanceof Collection) || !((Collection)object).isEmpty()) {
            object = object.iterator();
            while (object.hasNext()) {
                Object object2 = object.next();
                if (!(((Route)(object2 = (Route)object2)).proxy().type() == Proxy.Type.DIRECT && this.route.proxy().type() == Proxy.Type.DIRECT && Intrinsics.areEqual(this.route.socketAddress(), ((Route)object2).socketAddress()))) continue;
                return true;
            }
        }
        return false;
    }

    private final boolean supportsUrl(HttpUrl httpUrl) {
        Object object = this;
        if (Util.assertionsEnabled && !Thread.holdsLock(object)) {
            throw new AssertionError((Object)("Thread " + Thread.currentThread().getName() + " MUST hold lock on " + object));
        }
        object = this.route.address().url();
        if (httpUrl.port() != ((HttpUrl)object).port()) {
            return false;
        }
        if (Intrinsics.areEqual(httpUrl.host(), ((HttpUrl)object).host())) {
            return true;
        }
        if (!this.noCoalescedConnections && this.handshake != null) {
            Handshake handshake2 = this.handshake;
            Intrinsics.checkNotNull(handshake2);
            if (this.certificateSupportHost(httpUrl, handshake2)) {
                return true;
            }
        }
        return false;
    }

    private final boolean certificateSupportHost(HttpUrl httpUrl, Handshake object) {
        if (!((Collection)(object = ((Handshake)object).peerCertificates())).isEmpty()) {
            String string = httpUrl.host();
            Object e2 = object.get(0);
            Intrinsics.checkNotNull(e2);
            if (OkHostnameVerifier.INSTANCE.verify(string, (X509Certificate)e2)) {
                return true;
            }
        }
        return false;
    }

    @NotNull
    public final ExchangeCodec newCodec$okhttp(@NotNull OkHttpClient okHttpClient, @NotNull RealInterceptorChain realInterceptorChain) throws SocketException {
        Intrinsics.checkNotNullParameter(okHttpClient, "");
        Intrinsics.checkNotNullParameter(realInterceptorChain, "");
        Socket socket = this.socket;
        Intrinsics.checkNotNull(socket);
        Socket socket2 = socket;
        BufferedSource bufferedSource = this.source;
        Intrinsics.checkNotNull(bufferedSource);
        BufferedSource bufferedSource2 = bufferedSource;
        BufferedSink bufferedSink = this.sink;
        Intrinsics.checkNotNull(bufferedSink);
        BufferedSink bufferedSink2 = bufferedSink;
        Http2Connection http2Connection = this.http2Connection;
        if (http2Connection != null) {
            return new Http2ExchangeCodec(okHttpClient, this, realInterceptorChain, http2Connection);
        }
        socket2.setSoTimeout(realInterceptorChain.readTimeoutMillis());
        bufferedSource2.timeout().timeout(realInterceptorChain.getReadTimeoutMillis$okhttp(), TimeUnit.MILLISECONDS);
        bufferedSink2.timeout().timeout(realInterceptorChain.getWriteTimeoutMillis$okhttp(), TimeUnit.MILLISECONDS);
        return new Http1ExchangeCodec(okHttpClient, this, bufferedSource2, bufferedSink2);
    }

    @NotNull
    public final RealWebSocket.Streams newWebSocketStreams$okhttp(@NotNull Exchange exchange) throws SocketException {
        Intrinsics.checkNotNullParameter(exchange, "");
        Socket socket = this.socket;
        Intrinsics.checkNotNull(socket);
        Socket socket2 = socket;
        BufferedSource bufferedSource = this.source;
        Intrinsics.checkNotNull(bufferedSource);
        BufferedSource bufferedSource2 = bufferedSource;
        BufferedSink bufferedSink = this.sink;
        Intrinsics.checkNotNull(bufferedSink);
        BufferedSink bufferedSink2 = bufferedSink;
        socket2.setSoTimeout(0);
        this.noNewExchanges$okhttp();
        return new RealWebSocket.Streams(bufferedSource2, bufferedSink2, exchange){
            final /* synthetic */ Exchange $exchange;
            {
                this.$exchange = exchange;
                super(true, bufferedSource, bufferedSink);
            }

            public final void close() {
                this.$exchange.bodyComplete(-1L, true, true, null);
            }
        };
    }

    @Override
    @NotNull
    public final Route route() {
        return this.route;
    }

    public final void cancel() {
        Socket socket = this.rawSocket;
        if (socket != null) {
            Util.closeQuietly(socket);
            return;
        }
    }

    @Override
    @NotNull
    public final Socket socket() {
        Socket socket = this.socket;
        Intrinsics.checkNotNull(socket);
        return socket;
    }

    public final boolean isHealthy(boolean bl) {
        long l2;
        RealConnection realConnection = this;
        if (Util.assertionsEnabled && Thread.holdsLock(realConnection)) {
            throw new AssertionError((Object)("Thread " + Thread.currentThread().getName() + " MUST NOT hold lock on " + realConnection));
        }
        long l3 = System.nanoTime();
        Socket socket = this.rawSocket;
        Intrinsics.checkNotNull(socket);
        Object object = socket;
        Socket socket2 = this.socket;
        Intrinsics.checkNotNull(socket2);
        Socket socket3 = socket2;
        BufferedSource bufferedSource = this.source;
        Intrinsics.checkNotNull(bufferedSource);
        BufferedSource bufferedSource2 = bufferedSource;
        if (((Socket)object).isClosed() || socket3.isClosed() || socket3.isInputShutdown() || socket3.isOutputShutdown()) {
            return false;
        }
        object = this.http2Connection;
        if (object != null) {
            return ((Http2Connection)object).isHealthy(l3);
        }
        object = this;
        synchronized (object) {
            l2 = l3 - this.idleAtNs;
        }
        long l4 = l2;
        if (l4 >= 10000000000L && bl) {
            return Util.isHealthy(socket3, bufferedSource2);
        }
        return true;
    }

    @Override
    public final void onStream(@NotNull Http2Stream http2Stream) throws IOException {
        Intrinsics.checkNotNullParameter(http2Stream, "");
        http2Stream.close(ErrorCode.REFUSED_STREAM, null);
    }

    @Override
    public final synchronized void onSettings(@NotNull Http2Connection http2Connection, @NotNull Settings settings) {
        Intrinsics.checkNotNullParameter(http2Connection, "");
        Intrinsics.checkNotNullParameter(settings, "");
        this.allocationLimit = settings.getMaxConcurrentStreams();
    }

    @Override
    @Nullable
    public final Handshake handshake() {
        return this.handshake;
    }

    public final void connectFailed$okhttp(@NotNull OkHttpClient okHttpClient, @NotNull Route route, @NotNull IOException iOException) {
        Intrinsics.checkNotNullParameter(okHttpClient, "");
        Intrinsics.checkNotNullParameter(route, "");
        Intrinsics.checkNotNullParameter(iOException, "");
        if (route.proxy().type() != Proxy.Type.DIRECT) {
            Address address = route.address();
            address.proxySelector().connectFailed(address.url().uri(), route.proxy().address(), iOException);
        }
        okHttpClient.getRouteDatabase().failed(route);
    }

    public final synchronized void trackFailure$okhttp(@NotNull RealCall realCall, @Nullable IOException iOException) {
        block6: {
            block7: {
                block4: {
                    block5: {
                        Intrinsics.checkNotNullParameter(realCall, "");
                        if (!(iOException instanceof StreamResetException)) break block4;
                        if (((StreamResetException)iOException).errorCode != ErrorCode.REFUSED_STREAM) break block5;
                        int n2 = this.refusedStreamCount;
                        this.refusedStreamCount = n2 + 1;
                        if (this.refusedStreamCount <= 1) break block6;
                        this.noNewExchanges = true;
                        break block7;
                    }
                    if (((StreamResetException)iOException).errorCode == ErrorCode.CANCEL && realCall.isCanceled()) break block6;
                    this.noNewExchanges = true;
                    break block7;
                }
                if (this.isMultiplexed$okhttp() && !(iOException instanceof ConnectionShutdownException)) break block6;
                this.noNewExchanges = true;
                if (this.successCount != 0) break block6;
                if (iOException != null) {
                    this.connectFailed$okhttp(realCall.getClient(), this.route, iOException);
                }
            }
            int n3 = this.routeFailureCount;
            this.routeFailureCount = n3 + 1;
        }
    }

    @Override
    @NotNull
    public final Protocol protocol() {
        Protocol protocol = this.protocol;
        Intrinsics.checkNotNull((Object)protocol);
        return protocol;
    }

    @NotNull
    public final String toString() {
        StringBuilder stringBuilder = new StringBuilder("Connection{").append(this.route.address().url().host()).append(':').append(this.route.address().url().port()).append(", proxy=").append(this.route.proxy()).append(" hostAddress=").append(this.route.socketAddress()).append(" cipherSuite=");
        Object object = this.handshake;
        if (object == null || (object = ((Handshake)object).cipherSuite()) == null) {
            object = "none";
        }
        return stringBuilder.append(object).append(" protocol=").append((Object)this.protocol).append('}').toString();
    }

    public static final /* synthetic */ Handshake access$getHandshake$p(RealConnection realConnection) {
        return realConnection.handshake;
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J&\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0004R\u000e\u0010\u0003\u001a\u00020\u0004X\u0080T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2={"Lokhttp3/internal/connection/RealConnection$Companion;", "", "()V", "IDLE_CONNECTION_HEALTHY_NS", "", "MAX_TUNNEL_ATTEMPTS", "", "NPE_THROW_WITH_NULL", "", "newTestConnection", "Lokhttp3/internal/connection/RealConnection;", "connectionPool", "Lokhttp3/internal/connection/RealConnectionPool;", "route", "Lokhttp3/Route;", "socket", "Ljava/net/Socket;", "idleAtNs", "okhttp"})
    public static final class Companion {
        private Companion() {
        }

        @NotNull
        public final RealConnection newTestConnection(@NotNull RealConnectionPool object, @NotNull Route route, @NotNull Socket socket, long l2) {
            Intrinsics.checkNotNullParameter(object, "");
            Intrinsics.checkNotNullParameter(route, "");
            Intrinsics.checkNotNullParameter(socket, "");
            object = new RealConnection((RealConnectionPool)object, route);
            ((RealConnection)object).socket = socket;
            ((RealConnection)object).setIdleAtNs$okhttp(l2);
            return object;
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    @Metadata(mv={1, 8, 0}, k=3, xi=48)
    public final class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] nArray = new int[Proxy.Type.values().length];
            try {
                nArray[Proxy.Type.DIRECT.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                nArray[Proxy.Type.HTTP.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            $EnumSwitchMapping$0 = nArray;
        }
    }
}


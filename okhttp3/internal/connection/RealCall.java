/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.connection;

import java.io.Closeable;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import kotlin.ExceptionsKt;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import okhttp3.Address;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CertificatePinner;
import okhttp3.Connection;
import okhttp3.EventListener;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Util;
import okhttp3.internal.cache.CacheInterceptor;
import okhttp3.internal.connection.ConnectInterceptor;
import okhttp3.internal.connection.Exchange;
import okhttp3.internal.connection.ExchangeFinder;
import okhttp3.internal.connection.RealCall;
import okhttp3.internal.connection.RealConnection;
import okhttp3.internal.connection.RealConnectionPool;
import okhttp3.internal.http.BridgeInterceptor;
import okhttp3.internal.http.CallServerInterceptor;
import okhttp3.internal.http.ExchangeCodec;
import okhttp3.internal.http.RealInterceptorChain;
import okhttp3.internal.http.RetryAndFollowUpInterceptor;
import okhttp3.internal.platform.Platform;
import okio.AsyncTimeout;
import okio.Timeout;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\u00a7\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006*\u0001.\u0018\u00002\u00020\u0001:\u0002deB\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u000e\u00101\u001a\u0002022\u0006\u0010\u0010\u001a\u00020\u000fJ!\u00103\u001a\u0002H4\"\n\b\u0000\u00104*\u0004\u0018\u0001052\u0006\u00106\u001a\u0002H4H\u0002\u00a2\u0006\u0002\u00107J\b\u00108\u001a\u000202H\u0002J\b\u00109\u001a\u000202H\u0016J\b\u0010:\u001a\u00020\u0000H\u0016J\u0010\u0010;\u001a\u00020<2\u0006\u0010=\u001a\u00020>H\u0002J\u0010\u0010?\u001a\u0002022\u0006\u0010@\u001a\u00020AH\u0016J\u0016\u0010B\u001a\u0002022\u0006\u0010C\u001a\u00020\u00052\u0006\u0010D\u001a\u00020\u0007J\b\u0010E\u001a\u00020FH\u0016J\u0015\u0010G\u001a\u0002022\u0006\u0010H\u001a\u00020\u0007H\u0000\u00a2\u0006\u0002\bIJ\r\u0010J\u001a\u00020FH\u0000\u00a2\u0006\u0002\bKJ\u0015\u0010L\u001a\u00020\u001e2\u0006\u0010M\u001a\u00020NH\u0000\u00a2\u0006\u0002\bOJ\b\u0010P\u001a\u00020\u0007H\u0016J\b\u0010Q\u001a\u00020\u0007H\u0016J;\u0010R\u001a\u0002H4\"\n\b\u0000\u00104*\u0004\u0018\u0001052\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010S\u001a\u00020\u00072\u0006\u0010T\u001a\u00020\u00072\u0006\u00106\u001a\u0002H4H\u0000\u00a2\u0006\u0004\bU\u0010VJ\u0019\u0010W\u001a\u0004\u0018\u0001052\b\u00106\u001a\u0004\u0018\u000105H\u0000\u00a2\u0006\u0002\bXJ\r\u0010Y\u001a\u00020ZH\u0000\u00a2\u0006\u0002\b[J\u000f\u0010\\\u001a\u0004\u0018\u00010]H\u0000\u00a2\u0006\u0002\b^J\b\u0010C\u001a\u00020\u0005H\u0016J\u0006\u0010_\u001a\u00020\u0007J\b\u0010-\u001a\u00020`H\u0016J\u0006\u00100\u001a\u000202J!\u0010a\u001a\u0002H4\"\n\b\u0000\u00104*\u0004\u0018\u0001052\u0006\u0010b\u001a\u0002H4H\u0002\u00a2\u0006\u0002\u00107J\b\u0010c\u001a\u00020ZH\u0002R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\"\u0010\u0010\u001a\u0004\u0018\u00010\u000f2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000f@BX\u0086\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u0015\u001a\u0004\u0018\u00010\u000fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0012\"\u0004\b\u0017\u0010\u0018R\u0014\u0010\u0019\u001a\u00020\u001aX\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u0010\u0010\u001d\u001a\u0004\u0018\u00010\u001eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u001f\u001a\u0004\u0018\u00010 X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\"X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010%R\"\u0010&\u001a\u0004\u0018\u00010\u001e2\b\u0010\u000e\u001a\u0004\u0018\u00010\u001e@BX\u0080\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b'\u0010(R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010*R\u000e\u0010+\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010,\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010-\u001a\u00020.X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010/R\u000e\u00100\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006f"}, d2={"Lokhttp3/internal/connection/RealCall;", "Lokhttp3/Call;", "client", "Lokhttp3/OkHttpClient;", "originalRequest", "Lokhttp3/Request;", "forWebSocket", "", "(Lokhttp3/OkHttpClient;Lokhttp3/Request;Z)V", "callStackTrace", "", "canceled", "getClient", "()Lokhttp3/OkHttpClient;", "<set-?>", "Lokhttp3/internal/connection/RealConnection;", "connection", "getConnection", "()Lokhttp3/internal/connection/RealConnection;", "connectionPool", "Lokhttp3/internal/connection/RealConnectionPool;", "connectionToCancel", "getConnectionToCancel", "setConnectionToCancel", "(Lokhttp3/internal/connection/RealConnection;)V", "eventListener", "Lokhttp3/EventListener;", "getEventListener$okhttp", "()Lokhttp3/EventListener;", "exchange", "Lokhttp3/internal/connection/Exchange;", "exchangeFinder", "Lokhttp3/internal/connection/ExchangeFinder;", "executed", "Ljava/util/concurrent/atomic/AtomicBoolean;", "expectMoreExchanges", "getForWebSocket", "()Z", "interceptorScopedExchange", "getInterceptorScopedExchange$okhttp", "()Lokhttp3/internal/connection/Exchange;", "getOriginalRequest", "()Lokhttp3/Request;", "requestBodyOpen", "responseBodyOpen", "timeout", "okhttp3/internal/connection/RealCall$timeout$1", "Lokhttp3/internal/connection/RealCall$timeout$1;", "timeoutEarlyExit", "acquireConnectionNoEvents", "", "callDone", "E", "Ljava/io/IOException;", "e", "(Ljava/io/IOException;)Ljava/io/IOException;", "callStart", "cancel", "clone", "createAddress", "Lokhttp3/Address;", "url", "Lokhttp3/HttpUrl;", "enqueue", "responseCallback", "Lokhttp3/Callback;", "enterNetworkInterceptorExchange", "request", "newExchangeFinder", "execute", "Lokhttp3/Response;", "exitNetworkInterceptorExchange", "closeExchange", "exitNetworkInterceptorExchange$okhttp", "getResponseWithInterceptorChain", "getResponseWithInterceptorChain$okhttp", "initExchange", "chain", "Lokhttp3/internal/http/RealInterceptorChain;", "initExchange$okhttp", "isCanceled", "isExecuted", "messageDone", "requestDone", "responseDone", "messageDone$okhttp", "(Lokhttp3/internal/connection/Exchange;ZZLjava/io/IOException;)Ljava/io/IOException;", "noMoreExchanges", "noMoreExchanges$okhttp", "redactedUrl", "", "redactedUrl$okhttp", "releaseConnectionNoEvents", "Ljava/net/Socket;", "releaseConnectionNoEvents$okhttp", "retryAfterFailure", "Lokio/AsyncTimeout;", "timeoutExit", "cause", "toLoggableString", "AsyncCall", "CallReference", "okhttp"})
@SourceDebugExtension(value={"SMAP\nRealCall.kt\nKotlin\n*S Kotlin\n*F\n+ 1 RealCall.kt\nokhttp3/internal/connection/RealCall\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 Util.kt\nokhttp3/internal/Util\n+ 4 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,551:1\n1#2:552\n608#3,4:553\n615#3,4:557\n615#3,4:561\n608#3,4:565\n350#4,7:569\n*S KotlinDebug\n*F\n+ 1 RealCall.kt\nokhttp3/internal/connection/RealCall\n*L\n269#1:553,4\n344#1:557,4\n348#1:561,4\n375#1:565,4\n378#1:569,7\n*E\n"})
public final class RealCall
implements Call {
    @NotNull
    private final OkHttpClient client;
    @NotNull
    private final Request originalRequest;
    private final boolean forWebSocket;
    @NotNull
    private final RealConnectionPool connectionPool;
    @NotNull
    private final EventListener eventListener;
    @NotNull
    private final timeout.1 timeout;
    @NotNull
    private final AtomicBoolean executed;
    @Nullable
    private Object callStackTrace;
    @Nullable
    private ExchangeFinder exchangeFinder;
    @Nullable
    private RealConnection connection;
    private boolean timeoutEarlyExit;
    @Nullable
    private Exchange interceptorScopedExchange;
    private boolean requestBodyOpen;
    private boolean responseBodyOpen;
    private boolean expectMoreExchanges;
    private volatile boolean canceled;
    @Nullable
    private volatile Exchange exchange;
    @Nullable
    private volatile RealConnection connectionToCancel;

    public RealCall(@NotNull OkHttpClient object, @NotNull Request object2, boolean bl) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(object2, "");
        this.client = object;
        this.originalRequest = object2;
        this.forWebSocket = bl;
        this.connectionPool = this.client.connectionPool().getDelegate$okhttp();
        this.eventListener = this.client.eventListenerFactory().create(this);
        object2 = object = new AsyncTimeout(this){
            final /* synthetic */ RealCall this$0;
            {
                this.this$0 = realCall;
            }

            public final void timedOut() {
                this.this$0.cancel();
            }
        };
        RealCall realCall = this;
        ((Timeout)object2).timeout(this.client.callTimeoutMillis(), TimeUnit.MILLISECONDS);
        realCall.timeout = object;
        this.executed = new AtomicBoolean();
        this.expectMoreExchanges = true;
    }

    @NotNull
    public final OkHttpClient getClient() {
        return this.client;
    }

    @NotNull
    public final Request getOriginalRequest() {
        return this.originalRequest;
    }

    public final boolean getForWebSocket() {
        return this.forWebSocket;
    }

    @NotNull
    public final EventListener getEventListener$okhttp() {
        return this.eventListener;
    }

    @Nullable
    public final RealConnection getConnection() {
        return this.connection;
    }

    @Nullable
    public final Exchange getInterceptorScopedExchange$okhttp() {
        return this.interceptorScopedExchange;
    }

    @Nullable
    public final RealConnection getConnectionToCancel() {
        return this.connectionToCancel;
    }

    public final void setConnectionToCancel(@Nullable RealConnection realConnection) {
        this.connectionToCancel = realConnection;
    }

    @Override
    @NotNull
    public final AsyncTimeout timeout() {
        return this.timeout;
    }

    @Override
    @NotNull
    public final RealCall clone() {
        return new RealCall(this.client, this.originalRequest, this.forWebSocket);
    }

    @Override
    @NotNull
    public final Request request() {
        return this.originalRequest;
    }

    @Override
    public final void cancel() {
        if (this.canceled) {
            return;
        }
        this.canceled = true;
        Exchange exchange = this.exchange;
        if (exchange != null) {
            exchange.cancel();
        }
        RealConnection realConnection = this.connectionToCancel;
        if (realConnection != null) {
            realConnection.cancel();
        }
        this.eventListener.canceled(this);
    }

    @Override
    public final boolean isCanceled() {
        return this.canceled;
    }

    @Override
    @NotNull
    public final Response execute() {
        if (!this.executed.compareAndSet(false, true)) {
            String string = "Already Executed";
            throw new IllegalStateException(string.toString());
        }
        this.timeout.enter();
        this.callStart();
        try {
            this.client.dispatcher().executed$okhttp(this);
            Response response = this.getResponseWithInterceptorChain$okhttp();
            return response;
        }
        finally {
            this.client.dispatcher().finished$okhttp(this);
        }
    }

    @Override
    public final void enqueue(@NotNull Callback object) {
        Intrinsics.checkNotNullParameter(object, "");
        if (!this.executed.compareAndSet(false, true)) {
            object = "Already Executed";
            throw new IllegalStateException(object.toString());
        }
        this.callStart();
        this.client.dispatcher().enqueue$okhttp(new AsyncCall((Callback)object));
    }

    @Override
    public final boolean isExecuted() {
        return this.executed.get();
    }

    private final void callStart() {
        this.callStackTrace = Platform.Companion.get().getStackTraceForCloseable("response.body().close()");
        this.eventListener.callStart(this);
    }

    @NotNull
    public final Response getResponseWithInterceptorChain$okhttp() throws IOException {
        Object object = new ArrayList();
        CollectionsKt.addAll((Collection)object, (Iterable)this.client.interceptors());
        ((Collection)object).add(new RetryAndFollowUpInterceptor(this.client));
        ((Collection)object).add(new BridgeInterceptor(this.client.cookieJar()));
        ((Collection)object).add(new CacheInterceptor(this.client.cache()));
        ((Collection)object).add(ConnectInterceptor.INSTANCE);
        if (!this.forWebSocket) {
            CollectionsKt.addAll((Collection)object, (Iterable)this.client.networkInterceptors());
        }
        ((Collection)object).add(new CallServerInterceptor(this.forWebSocket));
        object = new RealInterceptorChain(this, (List<? extends Interceptor>)object, 0, null, this.originalRequest, this.client.connectTimeoutMillis(), this.client.readTimeoutMillis(), this.client.writeTimeoutMillis());
        boolean bl = false;
        try {
            object = ((RealInterceptorChain)object).proceed(this.originalRequest);
            if (this.isCanceled()) {
                Util.closeQuietly((Closeable)object);
                throw new IOException("Canceled");
            }
            this.noMoreExchanges$okhttp(null);
        }
        catch (IOException iOException) {
            try {
                bl = true;
                IOException iOException2 = this.noMoreExchanges$okhttp(iOException);
                Intrinsics.checkNotNull(iOException2);
                throw (Throwable)iOException2;
            }
            catch (Throwable throwable) {
                if (!bl) {
                    this.noMoreExchanges$okhttp(null);
                }
                throw throwable;
            }
        }
        return object;
    }

    public final void enterNetworkInterceptorExchange(@NotNull Request object, boolean bl) {
        Intrinsics.checkNotNullParameter(object, "");
        if (!(this.interceptorScopedExchange == null)) {
            object = "Check failed.";
            throw new IllegalStateException(object.toString());
        }
        RealCall realCall = this;
        synchronized (realCall) {
            if (!(!this.responseBodyOpen)) {
                object = "cannot make a new request because the previous response is still open: please call response.close()";
                throw new IllegalStateException(object.toString());
            }
            if (!(!this.requestBodyOpen)) {
                object = "Check failed.";
                throw new IllegalStateException(object.toString());
            }
        }
        if (bl) {
            RealCall realCall2 = this;
            this.exchangeFinder = new ExchangeFinder(this.connectionPool, this.createAddress(((Request)object).url()), realCall2, realCall2.eventListener);
        }
    }

    @NotNull
    public final Exchange initExchange$okhttp(@NotNull RealInterceptorChain object) {
        Intrinsics.checkNotNullParameter(object, "");
        Object object2 = this;
        synchronized (object2) {
            if (!this.expectMoreExchanges) {
                object = "released";
                throw new IllegalStateException(object.toString());
            }
            if (!(!this.responseBodyOpen)) {
                object = "Check failed.";
                throw new IllegalStateException(object.toString());
            }
            if (!(!this.requestBodyOpen)) {
                object = "Check failed.";
                throw new IllegalStateException(object.toString());
            }
        }
        ExchangeFinder exchangeFinder = this.exchangeFinder;
        Intrinsics.checkNotNull(exchangeFinder);
        object2 = exchangeFinder;
        object = exchangeFinder.find(this.client, (RealInterceptorChain)object);
        RealCall realCall = this;
        this.interceptorScopedExchange = object = new Exchange(realCall, realCall.eventListener, (ExchangeFinder)object2, (ExchangeCodec)object);
        this.exchange = object;
        object2 = this;
        synchronized (object2) {
            this.requestBodyOpen = true;
            this.responseBodyOpen = true;
        }
        if (this.canceled) {
            throw new IOException("Canceled");
        }
        return object;
    }

    public final void acquireConnectionNoEvents(@NotNull RealConnection object) {
        Intrinsics.checkNotNullParameter(object, "");
        RealConnection realConnection = object;
        if (Util.assertionsEnabled && !Thread.holdsLock(realConnection)) {
            throw new AssertionError((Object)("Thread " + Thread.currentThread().getName() + " MUST hold lock on " + realConnection));
        }
        if (!(this.connection == null)) {
            object = "Check failed.";
            throw new IllegalStateException(object.toString());
        }
        this.connection = object;
        RealCall realCall = this;
        ((RealConnection)object).getCalls().add(new CallReference(realCall, realCall.callStackTrace));
    }

    public final <E extends IOException> E messageDone$okhttp(@NotNull Exchange exchange, boolean bl, boolean bl2, E e2) {
        Intrinsics.checkNotNullParameter(exchange, "");
        if (!Intrinsics.areEqual(exchange, this.exchange)) {
            return e2;
        }
        boolean bl3 = false;
        boolean bl4 = false;
        RealCall realCall = this;
        synchronized (realCall) {
            if (bl && this.requestBodyOpen || bl2 && this.responseBodyOpen) {
                if (bl) {
                    this.requestBodyOpen = false;
                }
                if (bl2) {
                    this.responseBodyOpen = false;
                }
                bl3 = !this.requestBodyOpen && !this.responseBodyOpen;
                bl4 = !this.requestBodyOpen && !this.responseBodyOpen && !this.expectMoreExchanges;
            }
        }
        if (bl3) {
            this.exchange = null;
            RealConnection realConnection = this.connection;
            if (realConnection != null) {
                realConnection.incrementSuccessCount$okhttp();
            }
        }
        if (bl4) {
            return this.callDone(e2);
        }
        return e2;
    }

    @Nullable
    public final IOException noMoreExchanges$okhttp(@Nullable IOException iOException) {
        boolean bl = false;
        RealCall realCall = this;
        synchronized (realCall) {
            if (this.expectMoreExchanges) {
                this.expectMoreExchanges = false;
                bl = !this.requestBodyOpen && !this.responseBodyOpen;
            }
        }
        if (bl) {
            return this.callDone(iOException);
        }
        return iOException;
    }

    private final <E extends IOException> E callDone(E e2) {
        Object object;
        Object object2 = this;
        if (Util.assertionsEnabled && Thread.holdsLock(object2)) {
            throw new AssertionError((Object)("Thread " + Thread.currentThread().getName() + " MUST NOT hold lock on " + object2));
        }
        object2 = this.connection;
        if (object2 != null) {
            Object object3;
            object = object2;
            if (Util.assertionsEnabled && Thread.holdsLock(object)) {
                throw new AssertionError((Object)("Thread " + Thread.currentThread().getName() + " MUST NOT hold lock on " + object));
            }
            object = object2;
            synchronized (object) {
                object3 = this.releaseConnectionNoEvents$okhttp();
            }
            object = object3;
            if (this.connection == null) {
                Object object4 = object;
                if (object4 != null) {
                    Util.closeQuietly((Socket)object4);
                }
                this.eventListener.connectionReleased(this, (Connection)object2);
            } else if (!(object == null)) {
                object3 = "Check failed.";
                throw new IllegalStateException(object3.toString());
            }
        }
        object = this.timeoutExit(e2);
        if (e2 != null) {
            Call call = this;
            Object object5 = object;
            Intrinsics.checkNotNull(object5);
            this.eventListener.callFailed(call, (IOException)object5);
        } else {
            this.eventListener.callEnd(this);
        }
        return (E)object;
    }

    @Nullable
    public final Socket releaseConnectionNoEvents$okhttp() {
        int n2;
        int n3;
        Object object;
        Object object2;
        block6: {
            RealConnection realConnection = this.connection;
            Intrinsics.checkNotNull(realConnection);
            object2 = realConnection;
            object = realConnection;
            if (Util.assertionsEnabled && !Thread.holdsLock(object)) {
                throw new AssertionError((Object)("Thread " + Thread.currentThread().getName() + " MUST hold lock on " + object));
            }
            Object object3 = object = ((RealConnection)object2).getCalls();
            int n4 = 0;
            object3 = object3.iterator();
            while (object3.hasNext()) {
                Object object4 = object3.next();
                if (Intrinsics.areEqual(((Reference)(object4 = (Reference)object4)).get(), this)) {
                    n3 = n4;
                    break block6;
                }
                ++n4;
            }
            n3 = n2 = -1;
        }
        if (!(n3 != -1)) {
            object2 = "Check failed.";
            throw new IllegalStateException(object2.toString());
        }
        object.remove(n2);
        this.connection = null;
        if (object.isEmpty()) {
            ((RealConnection)object2).setIdleAtNs$okhttp(System.nanoTime());
            if (this.connectionPool.connectionBecameIdle((RealConnection)object2)) {
                return ((RealConnection)object2).socket();
            }
        }
        return null;
    }

    private final <E extends IOException> E timeoutExit(E e2) {
        if (this.timeoutEarlyExit) {
            return e2;
        }
        if (!this.timeout.exit()) {
            return e2;
        }
        InterruptedIOException interruptedIOException = new InterruptedIOException("timeout");
        if (e2 != null) {
            interruptedIOException.initCause(e2);
        }
        return (E)interruptedIOException;
    }

    public final void timeoutEarlyExit() {
        if (!(!this.timeoutEarlyExit)) {
            String string = "Check failed.";
            throw new IllegalStateException(string.toString());
        }
        this.timeoutEarlyExit = true;
        this.timeout.exit();
    }

    public final void exitNetworkInterceptorExchange$okhttp(boolean bl) {
        RealCall realCall = this;
        synchronized (realCall) {
            if (!this.expectMoreExchanges) {
                String string = "released";
                throw new IllegalStateException(string.toString());
            }
        }
        if (bl) {
            Exchange exchange = this.exchange;
            if (exchange != null) {
                exchange.detachWithViolence();
            }
        }
        this.interceptorScopedExchange = null;
    }

    private final Address createAddress(HttpUrl httpUrl) {
        SSLSocketFactory sSLSocketFactory = null;
        HostnameVerifier hostnameVerifier = null;
        CertificatePinner certificatePinner = null;
        if (httpUrl.isHttps()) {
            sSLSocketFactory = this.client.sslSocketFactory();
            hostnameVerifier = this.client.hostnameVerifier();
            certificatePinner = this.client.certificatePinner();
        }
        return new Address(httpUrl.host(), httpUrl.port(), this.client.dns(), this.client.socketFactory(), sSLSocketFactory, hostnameVerifier, certificatePinner, this.client.proxyAuthenticator(), this.client.proxy(), this.client.protocols(), this.client.connectionSpecs(), this.client.proxySelector());
    }

    public final boolean retryAfterFailure() {
        ExchangeFinder exchangeFinder = this.exchangeFinder;
        Intrinsics.checkNotNull(exchangeFinder);
        return exchangeFinder.retryAfterFailure();
    }

    private final String toLoggableString() {
        return (this.isCanceled() ? "canceled " : "") + (this.forWebSocket ? "web socket" : "call") + " to " + this.redactedUrl$okhttp();
    }

    @NotNull
    public final String redactedUrl$okhttp() {
        return this.originalRequest.url().redact();
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0080\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0019J\u0012\u0010\u001a\u001a\u00020\u00172\n\u0010\u001b\u001a\u00060\u0000R\u00020\u0006J\b\u0010\u001c\u001a\u00020\u0017H\u0016R\u0011\u0010\u0005\u001a\u00020\u00068F\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u001e\u0010\u000b\u001a\u00020\n2\u0006\u0010\t\u001a\u00020\n@BX\u0086\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u000e\u001a\u00020\u000f8F\u00a2\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0012\u001a\u00020\u00138F\u00a2\u0006\u0006\u001a\u0004\b\u0014\u0010\u0015R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001d"}, d2={"Lokhttp3/internal/connection/RealCall$AsyncCall;", "Ljava/lang/Runnable;", "responseCallback", "Lokhttp3/Callback;", "(Lokhttp3/internal/connection/RealCall;Lokhttp3/Callback;)V", "call", "Lokhttp3/internal/connection/RealCall;", "getCall", "()Lokhttp3/internal/connection/RealCall;", "<set-?>", "Ljava/util/concurrent/atomic/AtomicInteger;", "callsPerHost", "getCallsPerHost", "()Ljava/util/concurrent/atomic/AtomicInteger;", "host", "", "getHost", "()Ljava/lang/String;", "request", "Lokhttp3/Request;", "getRequest", "()Lokhttp3/Request;", "executeOn", "", "executorService", "Ljava/util/concurrent/ExecutorService;", "reuseCallsPerHostFrom", "other", "run", "okhttp"})
    @SourceDebugExtension(value={"SMAP\nRealCall.kt\nKotlin\n*S Kotlin\n*F\n+ 1 RealCall.kt\nokhttp3/internal/connection/RealCall$AsyncCall\n+ 2 Util.kt\nokhttp3/internal/Util\n*L\n1#1,551:1\n615#2,4:552\n409#2,9:556\n*S KotlinDebug\n*F\n+ 1 RealCall.kt\nokhttp3/internal/connection/RealCall$AsyncCall\n*L\n494#1:552,4\n513#1:556,9\n*E\n"})
    public final class AsyncCall
    implements Runnable {
        @NotNull
        private final Callback responseCallback;
        @NotNull
        private volatile AtomicInteger callsPerHost;

        public AsyncCall(Callback callback) {
            Intrinsics.checkNotNullParameter(callback, "");
            this.responseCallback = callback;
            this.callsPerHost = new AtomicInteger(0);
        }

        @NotNull
        public final AtomicInteger getCallsPerHost() {
            return this.callsPerHost;
        }

        public final void reuseCallsPerHostFrom(@NotNull AsyncCall asyncCall) {
            Intrinsics.checkNotNullParameter(asyncCall, "");
            this.callsPerHost = asyncCall.callsPerHost;
        }

        @NotNull
        public final String getHost() {
            return RealCall.this.getOriginalRequest().url().host();
        }

        @NotNull
        public final Request getRequest() {
            return RealCall.this.getOriginalRequest();
        }

        @NotNull
        public final RealCall getCall() {
            return RealCall.this;
        }

        public final void executeOn(@NotNull ExecutorService executorService) {
            Intrinsics.checkNotNullParameter(executorService, "");
            Object object = RealCall.this.getClient().dispatcher();
            if (Util.assertionsEnabled && Thread.holdsLock(object)) {
                throw new AssertionError((Object)("Thread " + Thread.currentThread().getName() + " MUST NOT hold lock on " + object));
            }
            try {
                try {
                    executorService.execute(this);
                    return;
                }
                catch (RejectedExecutionException rejectedExecutionException) {
                    object = new InterruptedIOException("executor rejected");
                    ((Throwable)object).initCause(rejectedExecutionException);
                    RealCall.this.noMoreExchanges$okhttp((IOException)object);
                    this.responseCallback.onFailure(RealCall.this, (IOException)object);
                    return;
                }
            }
            finally {
                RealCall.this.getClient().dispatcher().finished$okhttp(this);
            }
        }

        @Override
        public final void run() {
            String string = "OkHttp " + RealCall.this.redactedUrl$okhttp();
            RealCall realCall = RealCall.this;
            Thread thread2 = Thread.currentThread();
            String string2 = thread2.getName();
            thread2.setName(string);
            try {
                boolean bl = false;
                realCall.timeout.enter();
                try {
                    Response response = realCall.getResponseWithInterceptorChain$okhttp();
                    bl = true;
                    this.responseCallback.onResponse(realCall, response);
                }
                catch (IOException iOException) {
                    if (bl) {
                        Platform.Companion.get().log("Callback failure for " + realCall.toLoggableString(), 4, iOException);
                    } else {
                        this.responseCallback.onFailure(realCall, iOException);
                    }
                }
                catch (Throwable throwable) {
                    realCall.cancel();
                    if (!bl) {
                        IOException iOException = new IOException("canceled due to " + throwable);
                        ExceptionsKt.addSuppressed(iOException, throwable);
                        this.responseCallback.onFailure(realCall, iOException);
                    }
                    throw throwable;
                }
                finally {
                    realCall.getClient().dispatcher().finished$okhttp(this);
                }
                return;
            }
            finally {
                thread2.setName(string2);
            }
        }
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\b\u0000\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0017\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\u0002\u0010\u0006R\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\b\u00a8\u0006\t"}, d2={"Lokhttp3/internal/connection/RealCall$CallReference;", "Ljava/lang/ref/WeakReference;", "Lokhttp3/internal/connection/RealCall;", "referent", "callStackTrace", "", "(Lokhttp3/internal/connection/RealCall;Ljava/lang/Object;)V", "getCallStackTrace", "()Ljava/lang/Object;", "okhttp"})
    public static final class CallReference
    extends WeakReference<RealCall> {
        @Nullable
        private final Object callStackTrace;

        public CallReference(@NotNull RealCall realCall, @Nullable Object object) {
            Intrinsics.checkNotNullParameter(realCall, "");
            super(realCall);
            this.callStackTrace = object;
        }

        @Nullable
        public final Object getCallStackTrace() {
            return this.callStackTrace;
        }
    }
}


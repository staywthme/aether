/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.connection;

import java.io.IOException;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.Address;
import okhttp3.Connection;
import okhttp3.EventListener;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Route;
import okhttp3.internal.Util;
import okhttp3.internal.connection.RealCall;
import okhttp3.internal.connection.RealConnection;
import okhttp3.internal.connection.RealConnectionPool;
import okhttp3.internal.connection.RouteException;
import okhttp3.internal.connection.RouteSelector;
import okhttp3.internal.http.ExchangeCodec;
import okhttp3.internal.http.RealInterceptorChain;
import okhttp3.internal.http2.ConnectionShutdownException;
import okhttp3.internal.http2.ErrorCode;
import okhttp3.internal.http2.StreamResetException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000r\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\u0016\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001cJ0\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u000e2\u0006\u0010 \u001a\u00020\u000e2\u0006\u0010!\u001a\u00020\u000e2\u0006\u0010\"\u001a\u00020\u000e2\u0006\u0010#\u001a\u00020$H\u0002J8\u0010%\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u000e2\u0006\u0010 \u001a\u00020\u000e2\u0006\u0010!\u001a\u00020\u000e2\u0006\u0010\"\u001a\u00020\u000e2\u0006\u0010#\u001a\u00020$2\u0006\u0010&\u001a\u00020$H\u0002J\u0006\u0010'\u001a\u00020$J\n\u0010(\u001a\u0004\u0018\u00010\u0010H\u0002J\u000e\u0010)\u001a\u00020$2\u0006\u0010*\u001a\u00020+J\u000e\u0010,\u001a\u00020-2\u0006\u0010.\u001a\u00020/R\u0014\u0010\u0004\u001a\u00020\u0005X\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0014X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u0016X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u00060"}, d2={"Lokhttp3/internal/connection/ExchangeFinder;", "", "connectionPool", "Lokhttp3/internal/connection/RealConnectionPool;", "address", "Lokhttp3/Address;", "call", "Lokhttp3/internal/connection/RealCall;", "eventListener", "Lokhttp3/EventListener;", "(Lokhttp3/internal/connection/RealConnectionPool;Lokhttp3/Address;Lokhttp3/internal/connection/RealCall;Lokhttp3/EventListener;)V", "getAddress$okhttp", "()Lokhttp3/Address;", "connectionShutdownCount", "", "nextRouteToTry", "Lokhttp3/Route;", "otherFailureCount", "refusedStreamCount", "routeSelection", "Lokhttp3/internal/connection/RouteSelector$Selection;", "routeSelector", "Lokhttp3/internal/connection/RouteSelector;", "find", "Lokhttp3/internal/http/ExchangeCodec;", "client", "Lokhttp3/OkHttpClient;", "chain", "Lokhttp3/internal/http/RealInterceptorChain;", "findConnection", "Lokhttp3/internal/connection/RealConnection;", "connectTimeout", "readTimeout", "writeTimeout", "pingIntervalMillis", "connectionRetryEnabled", "", "findHealthyConnection", "doExtensiveHealthChecks", "retryAfterFailure", "retryRoute", "sameHostAndPort", "url", "Lokhttp3/HttpUrl;", "trackFailure", "", "e", "Ljava/io/IOException;", "okhttp"})
public final class ExchangeFinder {
    @NotNull
    private final RealConnectionPool connectionPool;
    @NotNull
    private final Address address;
    @NotNull
    private final RealCall call;
    @NotNull
    private final EventListener eventListener;
    @Nullable
    private RouteSelector.Selection routeSelection;
    @Nullable
    private RouteSelector routeSelector;
    private int refusedStreamCount;
    private int connectionShutdownCount;
    private int otherFailureCount;
    @Nullable
    private Route nextRouteToTry;

    public ExchangeFinder(@NotNull RealConnectionPool realConnectionPool, @NotNull Address address, @NotNull RealCall realCall, @NotNull EventListener eventListener) {
        Intrinsics.checkNotNullParameter(realConnectionPool, "");
        Intrinsics.checkNotNullParameter(address, "");
        Intrinsics.checkNotNullParameter(realCall, "");
        Intrinsics.checkNotNullParameter(eventListener, "");
        this.connectionPool = realConnectionPool;
        this.address = address;
        this.call = realCall;
        this.eventListener = eventListener;
    }

    @NotNull
    public final Address getAddress$okhttp() {
        return this.address;
    }

    @NotNull
    public final ExchangeCodec find(@NotNull OkHttpClient okHttpClient, @NotNull RealInterceptorChain realInterceptorChain) {
        Intrinsics.checkNotNullParameter(okHttpClient, "");
        Intrinsics.checkNotNullParameter(realInterceptorChain, "");
        try {
            RealConnection realConnection = this.findHealthyConnection(realInterceptorChain.getConnectTimeoutMillis$okhttp(), realInterceptorChain.getReadTimeoutMillis$okhttp(), realInterceptorChain.getWriteTimeoutMillis$okhttp(), okHttpClient.pingIntervalMillis(), okHttpClient.retryOnConnectionFailure(), !Intrinsics.areEqual(realInterceptorChain.getRequest$okhttp().method(), "GET"));
            return realConnection.newCodec$okhttp(okHttpClient, realInterceptorChain);
        }
        catch (RouteException routeException) {
            this.trackFailure(routeException.getLastConnectException());
            throw routeException;
        }
        catch (IOException iOException) {
            this.trackFailure(iOException);
            throw new RouteException(iOException);
        }
    }

    private final RealConnection findHealthyConnection(int n2, int n3, int n4, int n5, boolean bl, boolean bl2) throws IOException {
        while (true) {
            RealConnection realConnection;
            if ((realConnection = this.findConnection(n2, n3, n4, n5, bl)).isHealthy(bl2)) {
                return realConnection;
            }
            realConnection.noNewExchanges$okhttp();
            if (this.nextRouteToTry != null) continue;
            RouteSelector.Selection selection = this.routeSelection;
            boolean bl3 = selection != null ? selection.hasNext() : true;
            if (bl3) continue;
            RouteSelector routeSelector = this.routeSelector;
            bl3 = routeSelector != null ? routeSelector.hasNext() : true;
            if (!bl3) break;
        }
        throw new IOException("exhausted all routes");
    }

    /*
     * Unable to fully structure code
     */
    private final RealConnection findConnection(int var1_1, int var2_4, int var3_5, int var4_6, boolean var5_7) throws IOException {
        block24: {
            block23: {
                if (this.call.isCanceled()) {
                    throw new IOException("Canceled");
                }
                var6_8 = this.call.getConnection();
                if (var6_8 != null) {
                    var7_10 = null;
                    var8_11 = var6_8;
                    synchronized (var8_11) {
                        if (var6_8.getNoNewExchanges() || !this.sameHostAndPort(var6_8.route().address().url())) {
                            var7_10 = this.call.releaseConnectionNoEvents$okhttp();
                        }
                    }
                    if (this.call.getConnection() != null) {
                        if (!(var7_10 == null)) {
                            var6_8 = "Check failed.";
                            throw new IllegalStateException(var6_8.toString());
                        }
                        return var6_8;
                    }
                    v0 = var7_10;
                    if (v0 != null) {
                        Util.closeQuietly(v0);
                    }
                    this.eventListener.connectionReleased(this.call, (Connection)var6_8);
                }
                this.refusedStreamCount = 0;
                this.connectionShutdownCount = 0;
                this.otherFailureCount = 0;
                if (this.connectionPool.callAcquirePooledConnection(this.address, this.call, null, false)) {
                    v1 = this.call.getConnection();
                    Intrinsics.checkNotNull(v1);
                    var7_10 = v1;
                    this.eventListener.connectionAcquired(this.call, (Connection)var7_10);
                    return var7_10;
                }
                if (this.nextRouteToTry == null) break block23;
                var7_10 = null;
                v2 = this.nextRouteToTry;
                Intrinsics.checkNotNull(v2);
                var8_11 = v2;
                this.nextRouteToTry = null;
                break block24;
            }
            if (this.routeSelection == null) ** GOTO lbl-1000
            v3 = this.routeSelection;
            Intrinsics.checkNotNull(v3);
            if (v3.hasNext()) {
                var7_10 = null;
                v4 = this.routeSelection;
                Intrinsics.checkNotNull(v4);
                var8_11 = v4.next();
            } else lbl-1000:
            // 2 sources

            {
                if ((var6_8 = this.routeSelector) == null) {
                    this.routeSelector = var6_8 = new RouteSelector(this.address, this.call.getClient().getRouteDatabase(), this.call, this.eventListener);
                }
                var9_12 = var6_8.next();
                this.routeSelection = var9_12;
                var7_10 = var9_12.getRoutes();
                if (this.call.isCanceled()) {
                    throw new IOException("Canceled");
                }
                if (this.connectionPool.callAcquirePooledConnection(this.address, this.call, var7_10, false)) {
                    v5 = this.call.getConnection();
                    Intrinsics.checkNotNull(v5);
                    var1_2 = v5;
                    this.eventListener.connectionAcquired(this.call, var1_2);
                    return var1_2;
                }
                var8_11 = var9_12.next();
            }
        }
        var6_8 = new RealConnection(this.connectionPool, (Route)var8_11);
        this.call.setConnectionToCancel((RealConnection)var6_8);
        try {
            var6_8.connect(var1_1, var2_4, var3_5, var4_6, var5_7, this.call, this.eventListener);
        }
        finally {
            this.call.setConnectionToCancel(null);
        }
        this.call.getClient().getRouteDatabase().connected(var6_8.route());
        if (this.connectionPool.callAcquirePooledConnection(this.address, this.call, var7_10, true)) {
            v6 = this.call.getConnection();
            Intrinsics.checkNotNull(v6);
            var9_12 = v6;
            this.nextRouteToTry = var8_11;
            Util.closeQuietly(var6_8.socket());
            this.eventListener.connectionAcquired(this.call, (Connection)var9_12);
            return var9_12;
        }
        var9_12 = var6_8;
        synchronized (var9_12) {
            this.connectionPool.put((RealConnection)var6_8);
            this.call.acquireConnectionNoEvents((RealConnection)var6_8);
        }
        this.eventListener.connectionAcquired(this.call, (Connection)var6_8);
        return var6_8;
    }

    public final void trackFailure(@NotNull IOException iOException) {
        Intrinsics.checkNotNullParameter(iOException, "");
        this.nextRouteToTry = null;
        if (iOException instanceof StreamResetException && ((StreamResetException)iOException).errorCode == ErrorCode.REFUSED_STREAM) {
            int n2 = this.refusedStreamCount;
            this.refusedStreamCount = n2 + 1;
            return;
        }
        if (iOException instanceof ConnectionShutdownException) {
            int n3 = this.connectionShutdownCount;
            this.connectionShutdownCount = n3 + 1;
            return;
        }
        int n4 = this.otherFailureCount;
        this.otherFailureCount = n4 + 1;
    }

    public final boolean retryAfterFailure() {
        if (this.refusedStreamCount == 0 && this.connectionShutdownCount == 0 && this.otherFailureCount == 0) {
            return false;
        }
        if (this.nextRouteToTry != null) {
            return true;
        }
        Object object = this.retryRoute();
        if (object != null) {
            this.nextRouteToTry = object;
            return true;
        }
        RouteSelector.Selection selection = this.routeSelection;
        boolean bl = selection != null ? selection.hasNext() : false;
        if (bl) {
            return true;
        }
        RouteSelector routeSelector = this.routeSelector;
        if (routeSelector == null) {
            return true;
        }
        object = routeSelector;
        return routeSelector.hasNext();
    }

    private final Route retryRoute() {
        if (this.refusedStreamCount > 1 || this.connectionShutdownCount > 1 || this.otherFailureCount > 0) {
            return null;
        }
        RealConnection realConnection = this.call.getConnection();
        if (realConnection == null) {
            return null;
        }
        Object object = realConnection;
        RealConnection realConnection2 = realConnection;
        synchronized (realConnection) {
            block8: {
                block7: {
                    if (((RealConnection)object).getRouteFailureCount$okhttp() == 0) break block7;
                    // ** MonitorExit[var2_3] (shouldn't be in output)
                    return null;
                }
                if (Util.canReuseConnectionFor(((RealConnection)object).route().address().url(), this.address.url())) break block8;
                // ** MonitorExit[var2_3] (shouldn't be in output)
                return null;
            }
            object = ((RealConnection)object).route();
            // ** MonitorExit[var2_3] (shouldn't be in output)
            return object;
        }
    }

    public final boolean sameHostAndPort(@NotNull HttpUrl httpUrl) {
        Intrinsics.checkNotNullParameter(httpUrl, "");
        HttpUrl httpUrl2 = this.address.url();
        return httpUrl.port() == httpUrl2.port() && Intrinsics.areEqual(httpUrl.host(), httpUrl2.host());
    }
}


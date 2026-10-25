/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.http;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import okhttp3.Call;
import okhttp3.Connection;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Util;
import okhttp3.internal.connection.Exchange;
import okhttp3.internal.connection.RealCall;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001BM\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\b\u0010\t\u001a\u0004\u0018\u00010\n\u0012\u0006\u0010\u000b\u001a\u00020\f\u0012\u0006\u0010\r\u001a\u00020\b\u0012\u0006\u0010\u000e\u001a\u00020\b\u0012\u0006\u0010\u000f\u001a\u00020\b\u00a2\u0006\u0002\u0010\u0010J\b\u0010\u0002\u001a\u00020\u001cH\u0016J\b\u0010\r\u001a\u00020\bH\u0016J\n\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u0016JK\u0010\u001f\u001a\u00020\u00002\b\b\u0002\u0010\u0007\u001a\u00020\b2\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n2\b\b\u0002\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\r\u001a\u00020\b2\b\b\u0002\u0010\u000e\u001a\u00020\b2\b\b\u0002\u0010\u000f\u001a\u00020\bH\u0000\u00a2\u0006\u0002\b J\u0010\u0010!\u001a\u00020\"2\u0006\u0010\u000b\u001a\u00020\fH\u0016J\b\u0010\u000e\u001a\u00020\bH\u0016J\b\u0010\u000b\u001a\u00020\fH\u0016J\u0018\u0010#\u001a\u00020\u00012\u0006\u0010$\u001a\u00020\b2\u0006\u0010%\u001a\u00020&H\u0016J\u0018\u0010'\u001a\u00020\u00012\u0006\u0010$\u001a\u00020\b2\u0006\u0010%\u001a\u00020&H\u0016J\u0018\u0010(\u001a\u00020\u00012\u0006\u0010$\u001a\u00020\b2\u0006\u0010%\u001a\u00020&H\u0016J\b\u0010\u000f\u001a\u00020\bH\u0016R\u0014\u0010\u0002\u001a\u00020\u0003X\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u000e\u0010\u0013\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\u00020\bX\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0016\u0010\t\u001a\u0004\u0018\u00010\nX\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\u00020\bX\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0015R\u0014\u0010\u000b\u001a\u00020\fX\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u0014\u0010\u000f\u001a\u00020\bX\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0015\u00a8\u0006)"}, d2={"Lokhttp3/internal/http/RealInterceptorChain;", "Lokhttp3/Interceptor$Chain;", "call", "Lokhttp3/internal/connection/RealCall;", "interceptors", "", "Lokhttp3/Interceptor;", "index", "", "exchange", "Lokhttp3/internal/connection/Exchange;", "request", "Lokhttp3/Request;", "connectTimeoutMillis", "readTimeoutMillis", "writeTimeoutMillis", "(Lokhttp3/internal/connection/RealCall;Ljava/util/List;ILokhttp3/internal/connection/Exchange;Lokhttp3/Request;III)V", "getCall$okhttp", "()Lokhttp3/internal/connection/RealCall;", "calls", "getConnectTimeoutMillis$okhttp", "()I", "getExchange$okhttp", "()Lokhttp3/internal/connection/Exchange;", "getReadTimeoutMillis$okhttp", "getRequest$okhttp", "()Lokhttp3/Request;", "getWriteTimeoutMillis$okhttp", "Lokhttp3/Call;", "connection", "Lokhttp3/Connection;", "copy", "copy$okhttp", "proceed", "Lokhttp3/Response;", "withConnectTimeout", "timeout", "unit", "Ljava/util/concurrent/TimeUnit;", "withReadTimeout", "withWriteTimeout", "okhttp"})
@SourceDebugExtension(value={"SMAP\nRealInterceptorChain.kt\nKotlin\n*S Kotlin\n*F\n+ 1 RealInterceptorChain.kt\nokhttp3/internal/http/RealInterceptorChain\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,123:1\n1#2:124\n*E\n"})
public final class RealInterceptorChain
implements Interceptor.Chain {
    @NotNull
    private final RealCall call;
    @NotNull
    private final List<Interceptor> interceptors;
    private final int index;
    @Nullable
    private final Exchange exchange;
    @NotNull
    private final Request request;
    private final int connectTimeoutMillis;
    private final int readTimeoutMillis;
    private final int writeTimeoutMillis;
    private int calls;

    public RealInterceptorChain(@NotNull RealCall realCall, @NotNull List<? extends Interceptor> list2, int n2, @Nullable Exchange exchange, @NotNull Request request, int n3, int n4, int n5) {
        Intrinsics.checkNotNullParameter(realCall, "");
        Intrinsics.checkNotNullParameter(list2, "");
        Intrinsics.checkNotNullParameter(request, "");
        this.call = realCall;
        this.interceptors = list2;
        this.index = n2;
        this.exchange = exchange;
        this.request = request;
        this.connectTimeoutMillis = n3;
        this.readTimeoutMillis = n4;
        this.writeTimeoutMillis = n5;
    }

    @NotNull
    public final RealCall getCall$okhttp() {
        return this.call;
    }

    @Nullable
    public final Exchange getExchange$okhttp() {
        return this.exchange;
    }

    @NotNull
    public final Request getRequest$okhttp() {
        return this.request;
    }

    public final int getConnectTimeoutMillis$okhttp() {
        return this.connectTimeoutMillis;
    }

    public final int getReadTimeoutMillis$okhttp() {
        return this.readTimeoutMillis;
    }

    public final int getWriteTimeoutMillis$okhttp() {
        return this.writeTimeoutMillis;
    }

    @NotNull
    public final RealInterceptorChain copy$okhttp(int n2, @Nullable Exchange exchange, @NotNull Request request, int n3, int n4, int n5) {
        Intrinsics.checkNotNullParameter(request, "");
        return new RealInterceptorChain(this.call, this.interceptors, n2, exchange, request, n3, n4, n5);
    }

    public static /* synthetic */ RealInterceptorChain copy$okhttp$default(RealInterceptorChain realInterceptorChain, int n2, Exchange exchange, Request request, int n3, int n4, int n5, int n6, Object object) {
        if ((n6 & 1) != 0) {
            n2 = realInterceptorChain.index;
        }
        if ((n6 & 2) != 0) {
            exchange = realInterceptorChain.exchange;
        }
        if ((n6 & 4) != 0) {
            request = realInterceptorChain.request;
        }
        if ((n6 & 8) != 0) {
            n3 = realInterceptorChain.connectTimeoutMillis;
        }
        if ((n6 & 0x10) != 0) {
            n4 = realInterceptorChain.readTimeoutMillis;
        }
        if ((n6 & 0x20) != 0) {
            n5 = realInterceptorChain.writeTimeoutMillis;
        }
        return realInterceptorChain.copy$okhttp(n2, exchange, request, n3, n4, n5);
    }

    @Override
    @Nullable
    public final Connection connection() {
        Exchange exchange = this.exchange;
        return exchange != null ? exchange.getConnection$okhttp() : null;
    }

    @Override
    public final int connectTimeoutMillis() {
        return this.connectTimeoutMillis;
    }

    @Override
    @NotNull
    public final Interceptor.Chain withConnectTimeout(int n2, @NotNull TimeUnit timeUnit) {
        Intrinsics.checkNotNullParameter((Object)timeUnit, "");
        if (!(this.exchange == null)) {
            String string = "Timeouts can't be adjusted in a network interceptor";
            throw new IllegalStateException(string.toString());
        }
        return RealInterceptorChain.copy$okhttp$default(this, 0, null, null, Util.checkDuration("connectTimeout", n2, timeUnit), 0, 0, 55, null);
    }

    @Override
    public final int readTimeoutMillis() {
        return this.readTimeoutMillis;
    }

    @Override
    @NotNull
    public final Interceptor.Chain withReadTimeout(int n2, @NotNull TimeUnit timeUnit) {
        Intrinsics.checkNotNullParameter((Object)timeUnit, "");
        if (!(this.exchange == null)) {
            String string = "Timeouts can't be adjusted in a network interceptor";
            throw new IllegalStateException(string.toString());
        }
        return RealInterceptorChain.copy$okhttp$default(this, 0, null, null, 0, Util.checkDuration("readTimeout", n2, timeUnit), 0, 47, null);
    }

    @Override
    public final int writeTimeoutMillis() {
        return this.writeTimeoutMillis;
    }

    @Override
    @NotNull
    public final Interceptor.Chain withWriteTimeout(int n2, @NotNull TimeUnit timeUnit) {
        Intrinsics.checkNotNullParameter((Object)timeUnit, "");
        if (!(this.exchange == null)) {
            String string = "Timeouts can't be adjusted in a network interceptor";
            throw new IllegalStateException(string.toString());
        }
        return RealInterceptorChain.copy$okhttp$default(this, 0, null, null, 0, 0, Util.checkDuration("writeTimeout", n2, timeUnit), 31, null);
    }

    @Override
    @NotNull
    public final Call call() {
        return this.call;
    }

    @Override
    @NotNull
    public final Request request() {
        return this.request;
    }

    @Override
    @NotNull
    public final Response proceed(@NotNull Request object) throws IOException {
        Intrinsics.checkNotNullParameter(object, "");
        if (!(this.index < this.interceptors.size())) {
            object = "Check failed.";
            throw new IllegalStateException(object.toString());
        }
        int n2 = this.calls;
        this.calls = n2 + 1;
        if (this.exchange != null) {
            if (!this.exchange.getFinder$okhttp().sameHostAndPort(((Request)object).url())) {
                object = "network interceptor " + this.interceptors.get(this.index - 1) + " must retain the same host and port";
                throw new IllegalStateException(object.toString());
            }
            if (!(this.calls == 1)) {
                object = "network interceptor " + this.interceptors.get(this.index - 1) + " must call proceed() exactly once";
                throw new IllegalStateException(object.toString());
            }
        }
        RealInterceptorChain realInterceptorChain = this;
        RealInterceptorChain realInterceptorChain2 = RealInterceptorChain.copy$okhttp$default(realInterceptorChain, realInterceptorChain.index + 1, null, (Request)object, 0, 0, 0, 58, null);
        object = this.interceptors.get(this.index);
        Response response = object.intercept(realInterceptorChain2);
        if (response == null) {
            throw new NullPointerException("interceptor " + object + " returned null");
        }
        Response response2 = response;
        if (this.exchange != null && !(this.index + 1 >= this.interceptors.size() || realInterceptorChain2.calls == 1)) {
            object = "network interceptor " + object + " must call proceed() exactly once";
            throw new IllegalStateException(object.toString());
        }
        if (!(response2.body() != null)) {
            object = "interceptor " + object + " returned a response with no body";
            throw new IllegalStateException(object.toString());
        }
        return response2;
    }
}


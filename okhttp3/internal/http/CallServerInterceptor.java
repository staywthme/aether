/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.http;

import java.io.IOException;
import java.net.ProtocolException;
import kotlin.ExceptionsKt;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import okhttp3.internal.connection.Exchange;
import okhttp3.internal.http.HttpMethod;
import okhttp3.internal.http.RealInterceptorChain;
import okhttp3.internal.http2.ConnectionShutdownException;
import okio.BufferedSink;
import okio.Okio;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0010\u0010\t\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000bH\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2={"Lokhttp3/internal/http/CallServerInterceptor;", "Lokhttp3/Interceptor;", "forWebSocket", "", "(Z)V", "intercept", "Lokhttp3/Response;", "chain", "Lokhttp3/Interceptor$Chain;", "shouldIgnoreAndWaitForRealResponse", "code", "", "okhttp"})
public final class CallServerInterceptor
implements Interceptor {
    private final boolean forWebSocket;

    public CallServerInterceptor(boolean bl) {
        this.forWebSocket = bl;
    }

    @Override
    @NotNull
    public final Response intercept(@NotNull Interceptor.Chain object) throws IOException {
        Object object2;
        Intrinsics.checkNotNullParameter(object, "");
        object = (RealInterceptorChain)object;
        Exchange exchange = ((RealInterceptorChain)object).getExchange$okhttp();
        Intrinsics.checkNotNull(exchange);
        Exchange exchange2 = exchange;
        object = ((RealInterceptorChain)object).getRequest$okhttp();
        RequestBody requestBody = ((Request)object).body();
        long l2 = System.currentTimeMillis();
        boolean bl = true;
        Response.Builder builder = null;
        IOException iOException = null;
        try {
            exchange2.writeRequestHeaders((Request)object);
            if (HttpMethod.permitsRequestBody(((Request)object).method()) && requestBody != null) {
                if (StringsKt.equals("100-continue", ((Request)object).header("Expect"), true)) {
                    exchange2.flushRequest();
                    builder = exchange2.readResponseHeaders(true);
                    exchange2.responseHeadersStart();
                    bl = false;
                }
                if (builder == null) {
                    if (requestBody.isDuplex()) {
                        exchange2.flushRequest();
                        object2 = Okio.buffer(exchange2.createRequestBody((Request)object, true));
                        requestBody.writeTo((BufferedSink)object2);
                    } else {
                        object2 = Okio.buffer(exchange2.createRequestBody((Request)object, false));
                        requestBody.writeTo((BufferedSink)object2);
                        object2.close();
                    }
                } else {
                    exchange2.noRequestBody();
                    if (!exchange2.getConnection$okhttp().isMultiplexed$okhttp()) {
                        exchange2.noNewExchangesOnConnection();
                    }
                }
            } else {
                exchange2.noRequestBody();
            }
            if (requestBody == null || !requestBody.isDuplex()) {
                exchange2.finishRequest();
            }
        }
        catch (IOException iOException2) {
            object2 = iOException2;
            if (iOException2 instanceof ConnectionShutdownException) {
                throw object2;
            }
            if (!exchange2.getHasFailure$okhttp()) {
                throw object2;
            }
            iOException = object2;
        }
        try {
            int n2;
            if (builder == null) {
                Response.Builder builder2 = exchange2.readResponseHeaders(false);
                Intrinsics.checkNotNull(builder2);
                builder = builder2;
                if (bl) {
                    exchange2.responseHeadersStart();
                    bl = false;
                }
            }
            if (this.shouldIgnoreAndWaitForRealResponse(n2 = ((Response)(object2 = builder.request((Request)object).handshake(exchange2.getConnection$okhttp().handshake()).sentRequestAtMillis(l2).receivedResponseAtMillis(System.currentTimeMillis()).build())).code())) {
                Response.Builder builder3 = exchange2.readResponseHeaders(false);
                Intrinsics.checkNotNull(builder3);
                builder = builder3;
                if (bl) {
                    exchange2.responseHeadersStart();
                }
                object2 = builder.request((Request)object).handshake(exchange2.getConnection$okhttp().handshake()).sentRequestAtMillis(l2).receivedResponseAtMillis(System.currentTimeMillis()).build();
                n2 = ((Response)object2).code();
            }
            exchange2.responseHeadersEnd((Response)object2);
            Object object3 = object2 = this.forWebSocket && n2 == 101 ? ((Response)object2).newBuilder().body(Util.EMPTY_RESPONSE).build() : ((Response)object2).newBuilder().body(exchange2.openResponseBody((Response)object2)).build();
            if (StringsKt.equals("close", ((Response)object2).request().header("Connection"), true) || StringsKt.equals("close", Response.header$default((Response)object2, "Connection", null, 2, null), true)) {
                exchange2.noNewExchangesOnConnection();
            }
            if (n2 == 204 || n2 == 205) {
                ResponseBody responseBody = ((Response)object2).body();
                if ((responseBody != null ? responseBody.contentLength() : -1L) > 0L) {
                    ResponseBody responseBody2 = ((Response)object2).body();
                    throw new ProtocolException("HTTP " + n2 + " had non-zero Content-Length: " + (responseBody2 != null ? Long.valueOf(responseBody2.contentLength()) : null));
                }
            }
            return object2;
        }
        catch (IOException iOException3) {
            if (iOException != null) {
                ExceptionsKt.addSuppressed(iOException, iOException3);
                throw iOException;
            }
            throw iOException3;
        }
    }

    private final boolean shouldIgnoreAndWaitForRealResponse(int n2) {
        if (n2 == 100) {
            return true;
        }
        return 102 <= n2 ? n2 < 200 : false;
    }
}


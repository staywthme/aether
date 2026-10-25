/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.cache;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.EventListener;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import okhttp3.internal.cache.CacheRequest;
import okhttp3.internal.cache.CacheStrategy;
import okhttp3.internal.connection.RealCall;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.http.HttpMethod;
import okhttp3.internal.http.RealResponseBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.Sink;
import okio.Source;
import okio.Timeout;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 \u000f2\u00020\u0001:\u0001\u000fB\u000f\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\u0004J\u001a\u0010\u0007\u001a\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\n2\u0006\u0010\u000b\u001a\u00020\bH\u0002J\u0010\u0010\f\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\u000eH\u0016R\u0016\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0010"}, d2={"Lokhttp3/internal/cache/CacheInterceptor;", "Lokhttp3/Interceptor;", "cache", "Lokhttp3/Cache;", "(Lokhttp3/Cache;)V", "getCache$okhttp", "()Lokhttp3/Cache;", "cacheWritingResponse", "Lokhttp3/Response;", "cacheRequest", "Lokhttp3/internal/cache/CacheRequest;", "response", "intercept", "chain", "Lokhttp3/Interceptor$Chain;", "Companion", "okhttp"})
public final class CacheInterceptor
implements Interceptor {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @Nullable
    private final Cache cache;

    public CacheInterceptor(@Nullable Cache cache) {
        this.cache = cache;
    }

    @Nullable
    public final Cache getCache$okhttp() {
        return this.cache;
    }

    @Override
    @NotNull
    public final Response intercept(@NotNull Interceptor.Chain object) throws IOException {
        Response response;
        Request request;
        Object object2;
        Response response2;
        Call call;
        block23: {
            ResponseBody responseBody;
            Object object3;
            Intrinsics.checkNotNullParameter(object, "");
            call = object.call();
            Cache cache = this.cache;
            response2 = cache != null ? cache.get$okhttp(object.request()) : null;
            long l2 = System.currentTimeMillis();
            object2 = new CacheStrategy.Factory(l2, object.request(), response2).compute();
            request = ((CacheStrategy)object2).getNetworkRequest();
            response = ((CacheStrategy)object2).getCacheResponse();
            Cache cache2 = this.cache;
            if (cache2 != null) {
                cache2.trackResponse$okhttp((CacheStrategy)object2);
            }
            if ((object3 = call instanceof RealCall ? (RealCall)call : null) == null || (object3 = ((RealCall)object3).getEventListener$okhttp()) == null) {
                object3 = object2 = EventListener.NONE;
            }
            if (response2 != null && response == null) {
                ResponseBody responseBody2 = response2.body();
                if (responseBody2 != null) {
                    Util.closeQuietly(responseBody2);
                }
            }
            if (request == null && response == null) {
                object = new Response.Builder().request(object.request()).protocol(Protocol.HTTP_1_1).code(504).message("Unsatisfiable Request (only-if-cached)").body(Util.EMPTY_RESPONSE).sentRequestAtMillis(-1L).receivedResponseAtMillis(System.currentTimeMillis()).build();
                Response response3 = object;
                ((EventListener)object2).satisfactionFailure(call, response3);
                return object;
            }
            if (request == null) {
                Response response4 = response;
                Intrinsics.checkNotNull(response4);
                object = response4.newBuilder().cacheResponse(CacheInterceptor.Companion.stripBody(response)).build();
                Response response5 = object;
                ((EventListener)object2).cacheHit(call, response5);
                return object;
            }
            if (response != null) {
                ((EventListener)object2).cacheConditionalHit(call, response);
            } else if (this.cache != null) {
                ((EventListener)object2).cacheMiss(call);
            }
            try {
                object = object.proceed(request);
                if (object != null || response2 == null) break block23;
                responseBody = response2.body();
            }
            catch (Throwable throwable) {
                if (response2 != null) {
                    ResponseBody responseBody3 = response2.body();
                    if (responseBody3 != null) {
                        Util.closeQuietly(responseBody3);
                    }
                }
                throw throwable;
            }
            if (responseBody != null) {
                Util.closeQuietly(responseBody);
            }
        }
        if (response != null) {
            Object object4 = object;
            boolean bl = object4 != null ? ((Response)object4).code() == 304 : false;
            if (bl) {
                Response response6 = response.newBuilder().headers(CacheInterceptor.Companion.combine(response.headers(), ((Response)object).headers())).sentRequestAtMillis(((Response)object).sentRequestAtMillis()).receivedResponseAtMillis(((Response)object).receivedResponseAtMillis()).cacheResponse(CacheInterceptor.Companion.stripBody(response)).networkResponse(CacheInterceptor.Companion.stripBody((Response)object)).build();
                ResponseBody responseBody = ((Response)object).body();
                Intrinsics.checkNotNull(responseBody);
                responseBody.close();
                Cache cache = this.cache;
                Intrinsics.checkNotNull(cache);
                cache.trackConditionalCacheHit$okhttp();
                this.cache.update$okhttp(response, response6);
                object = response6;
                response2 = object;
                ((EventListener)object2).cacheHit(call, response2);
                return object;
            }
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                Util.closeQuietly(responseBody);
            }
        }
        Object object5 = object;
        Intrinsics.checkNotNull(object5);
        Response response7 = ((Response)object5).newBuilder().cacheResponse(CacheInterceptor.Companion.stripBody(response)).networkResponse(CacheInterceptor.Companion.stripBody((Response)object)).build();
        if (this.cache != null) {
            if (HttpHeaders.promisesBody(response7) && CacheStrategy.Companion.isCacheable(response7, request)) {
                object = this.cache.put$okhttp(response7);
                response2 = this.cacheWritingResponse((CacheRequest)object, response7);
                if (response != null) {
                    ((EventListener)object2).cacheMiss(call);
                }
                return response2;
            }
            if (HttpMethod.INSTANCE.invalidatesCache(request.method())) {
                try {
                    this.cache.remove$okhttp(request);
                }
                catch (IOException iOException) {}
            }
        }
        return response7;
    }

    private final Response cacheWritingResponse(CacheRequest object, Response response) throws IOException {
        if (object == null) {
            return response;
        }
        Object object2 = object.body();
        ResponseBody responseBody = response.body();
        Intrinsics.checkNotNull(responseBody);
        BufferedSource bufferedSource = responseBody.source();
        object2 = Okio.buffer((Sink)object2);
        object = new Source(bufferedSource, (CacheRequest)object, (BufferedSink)object2){
            private boolean cacheRequestClosed;
            final /* synthetic */ BufferedSource $source;
            final /* synthetic */ CacheRequest $cacheRequest;
            final /* synthetic */ BufferedSink $cacheBody;
            {
                this.$source = bufferedSource;
                this.$cacheRequest = cacheRequest;
                this.$cacheBody = bufferedSink;
            }

            public final long read(@NotNull Buffer buffer, long l2) throws IOException {
                long l3;
                Intrinsics.checkNotNullParameter(buffer, "");
                try {
                    l3 = this.$source.read(buffer, l2);
                }
                catch (IOException iOException) {
                    if (!this.cacheRequestClosed) {
                        this.cacheRequestClosed = true;
                        this.$cacheRequest.abort();
                    }
                    throw iOException;
                }
                if (l3 == -1L) {
                    if (!this.cacheRequestClosed) {
                        this.cacheRequestClosed = true;
                        this.$cacheBody.close();
                    }
                    return -1L;
                }
                buffer.copyTo(this.$cacheBody.getBuffer(), buffer.size() - l3, l3);
                this.$cacheBody.emitCompleteSegments();
                return l3;
            }

            @NotNull
            public final Timeout timeout() {
                return this.$source.timeout();
            }

            public final void close() throws IOException {
                if (!this.cacheRequestClosed && !Util.discard(this, 100, TimeUnit.MILLISECONDS)) {
                    this.cacheRequestClosed = true;
                    this.$cacheRequest.abort();
                }
                this.$source.close();
            }
        };
        object2 = Response.header$default(response, "Content-Type", null, 2, null);
        long l2 = response.body().contentLength();
        return response.newBuilder().body(new RealResponseBody((String)object2, l2, Okio.buffer((Source)object))).build();
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0004H\u0002J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0002J\u0010\u0010\u000b\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0002J\u0014\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\rH\u0002\u00a8\u0006\u000f"}, d2={"Lokhttp3/internal/cache/CacheInterceptor$Companion;", "", "()V", "combine", "Lokhttp3/Headers;", "cachedHeaders", "networkHeaders", "isContentSpecificHeader", "", "fieldName", "", "isEndToEnd", "stripBody", "Lokhttp3/Response;", "response", "okhttp"})
    public static final class Companion {
        private Companion() {
        }

        private final Response stripBody(Response response) {
            Response response2 = response;
            if ((response2 != null ? response2.body() : null) != null) {
                return response.newBuilder().body(null).build();
            }
            return response;
        }

        private final Headers combine(Headers headers, Headers headers2) {
            String string;
            int n2;
            Headers.Builder builder = new Headers.Builder();
            int n3 = headers.size();
            for (n2 = 0; n2 < n3; ++n2) {
                string = headers.name(n2);
                String string2 = headers.value(n2);
                if (StringsKt.equals("Warning", string, true) && StringsKt.startsWith$default(string2, "1", false, 2, null) || !this.isContentSpecificHeader(string) && this.isEndToEnd(string) && headers2.get(string) != null) continue;
                builder.addLenient$okhttp(string, string2);
            }
            n3 = headers2.size();
            for (n2 = 0; n2 < n3; ++n2) {
                string = headers2.name(n2);
                if (this.isContentSpecificHeader(string) || !this.isEndToEnd(string)) continue;
                builder.addLenient$okhttp(string, headers2.value(n2));
            }
            return builder.build();
        }

        private final boolean isEndToEnd(String string) {
            return !StringsKt.equals("Connection", string, true) && !StringsKt.equals("Keep-Alive", string, true) && !StringsKt.equals("Proxy-Authenticate", string, true) && !StringsKt.equals("Proxy-Authorization", string, true) && !StringsKt.equals("TE", string, true) && !StringsKt.equals("Trailers", string, true) && !StringsKt.equals("Transfer-Encoding", string, true) && !StringsKt.equals("Upgrade", string, true);
        }

        private final boolean isContentSpecificHeader(String string) {
            return StringsKt.equals("Content-Length", string, true) || StringsKt.equals("Content-Encoding", string, true) || StringsKt.equals("Content-Type", string, true);
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }
}


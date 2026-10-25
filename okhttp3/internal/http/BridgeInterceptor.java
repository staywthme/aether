/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.http;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.StringsKt;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.http.RealResponseBody;
import okio.GzipSource;
import okio.Okio;
import okio.Source;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0005\u001a\u00020\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bH\u0002J\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2={"Lokhttp3/internal/http/BridgeInterceptor;", "Lokhttp3/Interceptor;", "cookieJar", "Lokhttp3/CookieJar;", "(Lokhttp3/CookieJar;)V", "cookieHeader", "", "cookies", "", "Lokhttp3/Cookie;", "intercept", "Lokhttp3/Response;", "chain", "Lokhttp3/Interceptor$Chain;", "okhttp"})
@SourceDebugExtension(value={"SMAP\nBridgeInterceptor.kt\nKotlin\n*S Kotlin\n*F\n+ 1 BridgeInterceptor.kt\nokhttp3/internal/http/BridgeInterceptor\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,117:1\n1864#2,3:118\n*S KotlinDebug\n*F\n+ 1 BridgeInterceptor.kt\nokhttp3/internal/http/BridgeInterceptor\n*L\n111#1:118,3\n*E\n"})
public final class BridgeInterceptor
implements Interceptor {
    @NotNull
    private final CookieJar cookieJar;

    public BridgeInterceptor(@NotNull CookieJar cookieJar) {
        Intrinsics.checkNotNullParameter(cookieJar, "");
        this.cookieJar = cookieJar;
    }

    @Override
    @NotNull
    public final Response intercept(@NotNull Interceptor.Chain object) throws IOException {
        List<Cookie> list2;
        Intrinsics.checkNotNullParameter(object, "");
        Object object2 = object.request();
        Object object3 = ((Request)object2).newBuilder();
        RequestBody requestBody = ((Request)object2).body();
        if (requestBody != null) {
            long l2;
            MediaType mediaType = requestBody.contentType();
            if (mediaType != null) {
                ((Request.Builder)object3).header("Content-Type", mediaType.toString());
            }
            if ((l2 = requestBody.contentLength()) != -1L) {
                ((Request.Builder)object3).header("Content-Length", String.valueOf(l2));
                ((Request.Builder)object3).removeHeader("Transfer-Encoding");
            } else {
                ((Request.Builder)object3).header("Transfer-Encoding", "chunked");
                ((Request.Builder)object3).removeHeader("Content-Length");
            }
        }
        if (((Request)object2).header("Host") == null) {
            ((Request.Builder)object3).header("Host", Util.toHostHeader$default(((Request)object2).url(), false, 1, null));
        }
        if (((Request)object2).header("Connection") == null) {
            ((Request.Builder)object3).header("Connection", "Keep-Alive");
        }
        boolean bl = false;
        if (((Request)object2).header("Accept-Encoding") == null && ((Request)object2).header("Range") == null) {
            bl = true;
            ((Request.Builder)object3).header("Accept-Encoding", "gzip");
        }
        if (!((Collection)(list2 = this.cookieJar.loadForRequest(((Request)object2).url()))).isEmpty()) {
            ((Request.Builder)object3).header("Cookie", this.cookieHeader(list2));
        }
        if (((Request)object2).header("User-Agent") == null) {
            ((Request.Builder)object3).header("User-Agent", "okhttp/4.12.0");
        }
        Response response = object.proceed(((Request.Builder)object3).build());
        HttpHeaders.receiveHeaders(this.cookieJar, ((Request)object2).url(), response.headers());
        object = response.newBuilder().request((Request)object2);
        if (bl && StringsKt.equals("gzip", Response.header$default(response, "Content-Encoding", null, 2, null), true) && HttpHeaders.promisesBody(response) && (object2 = response.body()) != null) {
            object2 = new GzipSource(((ResponseBody)object2).source());
            object3 = response.headers().newBuilder().removeAll("Content-Encoding").removeAll("Content-Length").build();
            ((Response.Builder)object).headers((Headers)object3);
            object3 = Response.header$default(response, "Content-Type", null, 2, null);
            ((Response.Builder)object).body(new RealResponseBody((String)object3, -1L, Okio.buffer((Source)object2)));
        }
        return ((Response.Builder)object).build();
    }

    private final String cookieHeader(List<Cookie> object) {
        StringBuilder stringBuilder;
        StringBuilder stringBuilder2 = stringBuilder = new StringBuilder();
        object = (Iterable)object;
        int n2 = 0;
        object = object.iterator();
        while (object.hasNext()) {
            int n3;
            Object object2 = object.next();
            if ((n3 = n2++) < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            object2 = (Cookie)object2;
            if (n3 > 0) {
                stringBuilder2.append("; ");
            }
            stringBuilder2.append(((Cookie)object2).name()).append('=').append(((Cookie)object2).value());
        }
        String string = stringBuilder.toString();
        Intrinsics.checkNotNullExpressionValue(string, "");
        return string;
    }
}


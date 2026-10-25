/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.http;

import java.net.Proxy;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.HttpUrl;
import okhttp3.Request;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u0018\u0010\t\u001a\u00020\n2\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0002J\u000e\u0010\u000b\u001a\u00020\u00042\u0006\u0010\f\u001a\u00020\r\u00a8\u0006\u000e"}, d2={"Lokhttp3/internal/http/RequestLine;", "", "()V", "get", "", "request", "Lokhttp3/Request;", "proxyType", "Ljava/net/Proxy$Type;", "includeAuthorityInRequestLine", "", "requestPath", "url", "Lokhttp3/HttpUrl;", "okhttp"})
public final class RequestLine {
    @NotNull
    public static final RequestLine INSTANCE = new RequestLine();

    private RequestLine() {
    }

    @NotNull
    public final String get(@NotNull Request request, @NotNull Proxy.Type type) {
        StringBuilder stringBuilder;
        Intrinsics.checkNotNullParameter(request, "");
        Intrinsics.checkNotNullParameter((Object)type, "");
        StringBuilder stringBuilder2 = stringBuilder = new StringBuilder();
        stringBuilder.append(request.method());
        stringBuilder2.append(' ');
        if (INSTANCE.includeAuthorityInRequestLine(request, type)) {
            stringBuilder2.append(request.url());
        } else {
            stringBuilder2.append(INSTANCE.requestPath(request.url()));
        }
        stringBuilder2.append(" HTTP/1.1");
        String string = stringBuilder.toString();
        Intrinsics.checkNotNullExpressionValue(string, "");
        return string;
    }

    private final boolean includeAuthorityInRequestLine(Request request, Proxy.Type type) {
        return !request.isHttps() && type == Proxy.Type.HTTP;
    }

    @NotNull
    public final String requestPath(@NotNull HttpUrl object) {
        Intrinsics.checkNotNullParameter(object, "");
        String string = ((HttpUrl)object).encodedPath();
        object = ((HttpUrl)object).encodedQuery();
        if (object != null) {
            return string + '?' + (String)object;
        }
        return string;
    }
}


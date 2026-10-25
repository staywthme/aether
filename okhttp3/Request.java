/*
 * Decompiled with CFR 0.152.
 */
package okhttp3;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.ReplaceWith;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.JvmName;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.StringsKt;
import okhttp3.CacheControl;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okhttp3.internal.http.HttpMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000N\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u000b\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u00002\u00020\u0001:\u0001*BA\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\b\u0010\b\u001a\u0004\u0018\u00010\t\u0012\u0016\u0010\n\u001a\u0012\u0012\b\u0012\u0006\u0012\u0002\b\u00030\f\u0012\u0004\u0012\u00020\u00010\u000b\u00a2\u0006\u0002\u0010\rJ\u000f\u0010\b\u001a\u0004\u0018\u00010\tH\u0007\u00a2\u0006\u0002\b\u001bJ\r\u0010\u000f\u001a\u00020\u0010H\u0007\u00a2\u0006\u0002\b\u001cJ\u0010\u0010\u001d\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u001e\u001a\u00020\u0005J\r\u0010\u0006\u001a\u00020\u0007H\u0007\u00a2\u0006\u0002\b\u001fJ\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050 2\u0006\u0010\u001e\u001a\u00020\u0005J\r\u0010\u0004\u001a\u00020\u0005H\u0007\u00a2\u0006\u0002\b!J\u0006\u0010\"\u001a\u00020#J\b\u0010$\u001a\u0004\u0018\u00010\u0001J#\u0010$\u001a\u0004\u0018\u0001H%\"\u0004\b\u0000\u0010%2\u000e\u0010&\u001a\n\u0012\u0006\b\u0001\u0012\u0002H%0\f\u00a2\u0006\u0002\u0010'J\b\u0010(\u001a\u00020\u0005H\u0016J\r\u0010\u0002\u001a\u00020\u0003H\u0007\u00a2\u0006\u0002\b)R\u0015\u0010\b\u001a\u0004\u0018\u00010\t8\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u000eR\u0011\u0010\u000f\u001a\u00020\u00108G\u00a2\u0006\u0006\u001a\u0004\b\u000f\u0010\u0011R\u0013\u0010\u0006\u001a\u00020\u00078\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0012R\u0011\u0010\u0013\u001a\u00020\u00148F\u00a2\u0006\u0006\u001a\u0004\b\u0013\u0010\u0015R\u0010\u0010\u0016\u001a\u0004\u0018\u00010\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0013\u0010\u0004\u001a\u00020\u00058\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\u0017R$\u0010\n\u001a\u0012\u0012\b\u0012\u0006\u0012\u0002\b\u00030\f\u0012\u0004\u0012\u00020\u00010\u000bX\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0013\u0010\u0002\u001a\u00020\u00038\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u001a\u00a8\u0006+"}, d2={"Lokhttp3/Request;", "", "url", "Lokhttp3/HttpUrl;", "method", "", "headers", "Lokhttp3/Headers;", "body", "Lokhttp3/RequestBody;", "tags", "", "Ljava/lang/Class;", "(Lokhttp3/HttpUrl;Ljava/lang/String;Lokhttp3/Headers;Lokhttp3/RequestBody;Ljava/util/Map;)V", "()Lokhttp3/RequestBody;", "cacheControl", "Lokhttp3/CacheControl;", "()Lokhttp3/CacheControl;", "()Lokhttp3/Headers;", "isHttps", "", "()Z", "lazyCacheControl", "()Ljava/lang/String;", "getTags$okhttp", "()Ljava/util/Map;", "()Lokhttp3/HttpUrl;", "-deprecated_body", "-deprecated_cacheControl", "header", "name", "-deprecated_headers", "", "-deprecated_method", "newBuilder", "Lokhttp3/Request$Builder;", "tag", "T", "type", "(Ljava/lang/Class;)Ljava/lang/Object;", "toString", "-deprecated_url", "Builder", "okhttp"})
@SourceDebugExtension(value={"SMAP\nRequest.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Request.kt\nokhttp3/Request\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,298:1\n1864#2,3:299\n*S KotlinDebug\n*F\n+ 1 Request.kt\nokhttp3/Request\n*L\n119#1:299,3\n*E\n"})
public final class Request {
    @NotNull
    private final HttpUrl url;
    @NotNull
    private final String method;
    @NotNull
    private final Headers headers;
    @Nullable
    private final RequestBody body;
    @NotNull
    private final Map<Class<?>, Object> tags;
    @Nullable
    private CacheControl lazyCacheControl;

    public Request(@NotNull HttpUrl httpUrl, @NotNull String string, @NotNull Headers headers, @Nullable RequestBody requestBody, @NotNull Map<Class<?>, ? extends Object> map) {
        Intrinsics.checkNotNullParameter(httpUrl, "");
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(headers, "");
        Intrinsics.checkNotNullParameter(map, "");
        this.url = httpUrl;
        this.method = string;
        this.headers = headers;
        this.body = requestBody;
        this.tags = map;
    }

    @JvmName(name="url")
    @NotNull
    public final HttpUrl url() {
        return this.url;
    }

    @JvmName(name="method")
    @NotNull
    public final String method() {
        return this.method;
    }

    @JvmName(name="headers")
    @NotNull
    public final Headers headers() {
        return this.headers;
    }

    @JvmName(name="body")
    @Nullable
    public final RequestBody body() {
        return this.body;
    }

    @NotNull
    public final Map<Class<?>, Object> getTags$okhttp() {
        return this.tags;
    }

    public final boolean isHttps() {
        return this.url.isHttps();
    }

    @Nullable
    public final String header(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, "");
        return this.headers.get(string);
    }

    @NotNull
    public final List<String> headers(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, "");
        return this.headers.values(string);
    }

    @Nullable
    public final Object tag() {
        return this.tag(Object.class);
    }

    @Nullable
    public final <T> T tag(@NotNull Class<? extends T> clazz) {
        Intrinsics.checkNotNullParameter(clazz, "");
        return clazz.cast(this.tags.get(clazz));
    }

    @NotNull
    public final Builder newBuilder() {
        return new Builder(this);
    }

    @JvmName(name="cacheControl")
    @NotNull
    public final CacheControl cacheControl() {
        CacheControl cacheControl = this.lazyCacheControl;
        if (cacheControl == null) {
            this.lazyCacheControl = cacheControl = CacheControl.Companion.parse(this.headers);
        }
        return cacheControl;
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="url", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_url")
    @NotNull
    public final HttpUrl -deprecated_url() {
        return this.url;
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="method", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_method")
    @NotNull
    public final String -deprecated_method() {
        return this.method;
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="headers", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_headers")
    @NotNull
    public final Headers -deprecated_headers() {
        return this.headers;
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="body", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_body")
    @Nullable
    public final RequestBody -deprecated_body() {
        return this.body;
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="cacheControl", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_cacheControl")
    @NotNull
    public final CacheControl -deprecated_cacheControl() {
        return this.cacheControl();
    }

    @NotNull
    public final String toString() {
        StringBuilder stringBuilder;
        StringBuilder stringBuilder2 = stringBuilder = new StringBuilder();
        stringBuilder.append("Request{method=");
        stringBuilder2.append(this.method);
        stringBuilder2.append(", url=");
        stringBuilder2.append(this.url);
        if (this.headers.size() != 0) {
            stringBuilder2.append(", headers=[");
            Object object = this.headers;
            int n2 = 0;
            object = object.iterator();
            while (object.hasNext()) {
                int n3;
                Object object2 = object.next();
                if ((n3 = n2++) < 0) {
                    CollectionsKt.throwIndexOverflow();
                }
                object2 = (Pair)object2;
                String string = (String)((Pair)object2).component1();
                object2 = (String)((Pair)object2).component2();
                if (n3 > 0) {
                    stringBuilder2.append(", ");
                }
                stringBuilder2.append(string);
                stringBuilder2.append(':');
                stringBuilder2.append((String)object2);
            }
            stringBuilder2.append(']');
        }
        if (!this.tags.isEmpty()) {
            stringBuilder2.append(", tags=");
            stringBuilder2.append(this.tags);
        }
        stringBuilder2.append('}');
        String string = stringBuilder.toString();
        Intrinsics.checkNotNullExpressionValue(string, "");
        return string;
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000V\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010%\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\b\u0016\u0018\u00002\u00020\u0001B\u0007\b\u0016\u00a2\u0006\u0002\u0010\u0002B\u000f\b\u0010\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\u0005J\u0018\u0010%\u001a\u00020\u00002\u0006\u0010&\u001a\u00020\u00132\u0006\u0010'\u001a\u00020\u0013H\u0016J\b\u0010(\u001a\u00020\u0004H\u0016J\u0010\u0010)\u001a\u00020\u00002\u0006\u0010)\u001a\u00020*H\u0016J\u0014\u0010+\u001a\u00020\u00002\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007H\u0017J\b\u0010,\u001a\u00020\u0000H\u0016J\b\u0010-\u001a\u00020\u0000H\u0016J\u0018\u0010.\u001a\u00020\u00002\u0006\u0010&\u001a\u00020\u00132\u0006\u0010'\u001a\u00020\u0013H\u0016J\u0010\u0010\f\u001a\u00020\u00002\u0006\u0010\f\u001a\u00020/H\u0016J\u001a\u0010\u0012\u001a\u00020\u00002\u0006\u0010\u0012\u001a\u00020\u00132\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007H\u0016J\u0010\u00100\u001a\u00020\u00002\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J\u0010\u00101\u001a\u00020\u00002\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J\u0010\u00102\u001a\u00020\u00002\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J\u0010\u00103\u001a\u00020\u00002\u0006\u0010&\u001a\u00020\u0013H\u0016J-\u00104\u001a\u00020\u0000\"\u0004\b\u0000\u001052\u000e\u00106\u001a\n\u0012\u0006\b\u0000\u0012\u0002H50\u001a2\b\u00104\u001a\u0004\u0018\u0001H5H\u0016\u00a2\u0006\u0002\u00107J\u0012\u00104\u001a\u00020\u00002\b\u00104\u001a\u0004\u0018\u00010\u0001H\u0016J\u0010\u0010\u001f\u001a\u00020\u00002\u0006\u0010\u001f\u001a\u000208H\u0016J\u0010\u0010\u001f\u001a\u00020\u00002\u0006\u0010\u001f\u001a\u00020\u0013H\u0016J\u0010\u0010\u001f\u001a\u00020\u00002\u0006\u0010\u001f\u001a\u00020 H\u0016R\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0080\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR\u001a\u0010\f\u001a\u00020\rX\u0080\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u001a\u0010\u0012\u001a\u00020\u0013X\u0080\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017R*\u0010\u0018\u001a\u0012\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u001a\u0012\u0004\u0012\u00020\u00010\u0019X\u0080\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u001c\"\u0004\b\u001d\u0010\u001eR\u001c\u0010\u001f\u001a\u0004\u0018\u00010 X\u0080\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b!\u0010\"\"\u0004\b#\u0010$\u00a8\u00069"}, d2={"Lokhttp3/Request$Builder;", "", "()V", "request", "Lokhttp3/Request;", "(Lokhttp3/Request;)V", "body", "Lokhttp3/RequestBody;", "getBody$okhttp", "()Lokhttp3/RequestBody;", "setBody$okhttp", "(Lokhttp3/RequestBody;)V", "headers", "Lokhttp3/Headers$Builder;", "getHeaders$okhttp", "()Lokhttp3/Headers$Builder;", "setHeaders$okhttp", "(Lokhttp3/Headers$Builder;)V", "method", "", "getMethod$okhttp", "()Ljava/lang/String;", "setMethod$okhttp", "(Ljava/lang/String;)V", "tags", "", "Ljava/lang/Class;", "getTags$okhttp", "()Ljava/util/Map;", "setTags$okhttp", "(Ljava/util/Map;)V", "url", "Lokhttp3/HttpUrl;", "getUrl$okhttp", "()Lokhttp3/HttpUrl;", "setUrl$okhttp", "(Lokhttp3/HttpUrl;)V", "addHeader", "name", "value", "build", "cacheControl", "Lokhttp3/CacheControl;", "delete", "get", "head", "header", "Lokhttp3/Headers;", "patch", "post", "put", "removeHeader", "tag", "T", "type", "(Ljava/lang/Class;Ljava/lang/Object;)Lokhttp3/Request$Builder;", "Ljava/net/URL;", "okhttp"})
    @SourceDebugExtension(value={"SMAP\nRequest.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Request.kt\nokhttp3/Request$Builder\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,298:1\n1#2:299\n*E\n"})
    public static class Builder {
        @Nullable
        private HttpUrl url;
        @NotNull
        private String method;
        @NotNull
        private Headers.Builder headers;
        @Nullable
        private RequestBody body;
        @NotNull
        private Map<Class<?>, Object> tags;

        @Nullable
        public final HttpUrl getUrl$okhttp() {
            return this.url;
        }

        public final void setUrl$okhttp(@Nullable HttpUrl httpUrl) {
            this.url = httpUrl;
        }

        @NotNull
        public final String getMethod$okhttp() {
            return this.method;
        }

        public final void setMethod$okhttp(@NotNull String string) {
            Intrinsics.checkNotNullParameter(string, "");
            this.method = string;
        }

        @NotNull
        public final Headers.Builder getHeaders$okhttp() {
            return this.headers;
        }

        public final void setHeaders$okhttp(@NotNull Headers.Builder builder) {
            Intrinsics.checkNotNullParameter(builder, "");
            this.headers = builder;
        }

        @Nullable
        public final RequestBody getBody$okhttp() {
            return this.body;
        }

        public final void setBody$okhttp(@Nullable RequestBody requestBody) {
            this.body = requestBody;
        }

        @NotNull
        public final Map<Class<?>, Object> getTags$okhttp() {
            return this.tags;
        }

        public final void setTags$okhttp(@NotNull Map<Class<?>, Object> map) {
            Intrinsics.checkNotNullParameter(map, "");
            this.tags = map;
        }

        public Builder() {
            this.tags = new LinkedHashMap();
            this.method = "GET";
            this.headers = new Headers.Builder();
        }

        public Builder(@NotNull Request request) {
            Intrinsics.checkNotNullParameter(request, "");
            this.tags = new LinkedHashMap();
            this.url = request.url();
            this.method = request.method();
            this.body = request.body();
            this.tags = request.getTags$okhttp().isEmpty() ? (Map)new LinkedHashMap() : MapsKt.toMutableMap(request.getTags$okhttp());
            this.headers = request.headers().newBuilder();
        }

        @NotNull
        public Builder url(@NotNull HttpUrl httpUrl) {
            Builder builder;
            Intrinsics.checkNotNullParameter(httpUrl, "");
            Builder builder2 = builder = this;
            builder.url = httpUrl;
            return builder;
        }

        @NotNull
        public Builder url(@NotNull String string) {
            String string2;
            Intrinsics.checkNotNullParameter(string, "");
            if (StringsKt.startsWith(string, "ws:", true)) {
                StringBuilder stringBuilder = new StringBuilder("http:");
                String string3 = string.substring(3);
                Intrinsics.checkNotNullExpressionValue(string3, "");
                string2 = stringBuilder.append(string3).toString();
            } else if (StringsKt.startsWith(string, "wss:", true)) {
                StringBuilder stringBuilder = new StringBuilder("https:");
                String string4 = string.substring(4);
                Intrinsics.checkNotNullExpressionValue(string4, "");
                string2 = stringBuilder.append(string4).toString();
            } else {
                string2 = string;
            }
            string = string2;
            return this.url(HttpUrl.Companion.get(string));
        }

        @NotNull
        public Builder url(@NotNull URL uRL) {
            Intrinsics.checkNotNullParameter(uRL, "");
            String string = uRL.toString();
            Intrinsics.checkNotNullExpressionValue(string, "");
            return this.url(HttpUrl.Companion.get(string));
        }

        @NotNull
        public Builder header(@NotNull String string, @NotNull String string2) {
            Builder builder;
            Intrinsics.checkNotNullParameter(string, "");
            Intrinsics.checkNotNullParameter(string2, "");
            Builder builder2 = builder = this;
            builder.headers.set(string, string2);
            return builder;
        }

        @NotNull
        public Builder addHeader(@NotNull String string, @NotNull String string2) {
            Builder builder;
            Intrinsics.checkNotNullParameter(string, "");
            Intrinsics.checkNotNullParameter(string2, "");
            Builder builder2 = builder = this;
            builder.headers.add(string, string2);
            return builder;
        }

        @NotNull
        public Builder removeHeader(@NotNull String string) {
            Builder builder;
            Intrinsics.checkNotNullParameter(string, "");
            Builder builder2 = builder = this;
            builder.headers.removeAll(string);
            return builder;
        }

        @NotNull
        public Builder headers(@NotNull Headers headers) {
            Builder builder;
            Intrinsics.checkNotNullParameter(headers, "");
            Builder builder2 = builder = this;
            builder.headers = headers.newBuilder();
            return builder;
        }

        @NotNull
        public Builder cacheControl(@NotNull CacheControl object) {
            Intrinsics.checkNotNullParameter(object, "");
            object = ((CacheControl)object).toString();
            if (((CharSequence)object).length() == 0) {
                return this.removeHeader("Cache-Control");
            }
            return this.header("Cache-Control", (String)object);
        }

        @NotNull
        public Builder get() {
            return this.method("GET", null);
        }

        @NotNull
        public Builder head() {
            return this.method("HEAD", null);
        }

        @NotNull
        public Builder post(@NotNull RequestBody requestBody) {
            Intrinsics.checkNotNullParameter(requestBody, "");
            return this.method("POST", requestBody);
        }

        @JvmOverloads
        @NotNull
        public Builder delete(@Nullable RequestBody requestBody) {
            return this.method("DELETE", requestBody);
        }

        public static /* synthetic */ Builder delete$default(Builder builder, RequestBody requestBody, int n2, Object object) {
            if (object != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: delete");
            }
            if ((n2 & 1) != 0) {
                requestBody = Util.EMPTY_REQUEST;
            }
            return builder.delete(requestBody);
        }

        @NotNull
        public Builder put(@NotNull RequestBody requestBody) {
            Intrinsics.checkNotNullParameter(requestBody, "");
            return this.method("PUT", requestBody);
        }

        @NotNull
        public Builder patch(@NotNull RequestBody requestBody) {
            Intrinsics.checkNotNullParameter(requestBody, "");
            return this.method("PATCH", requestBody);
        }

        @NotNull
        public Builder method(@NotNull String string, @Nullable RequestBody requestBody) {
            Builder builder;
            Intrinsics.checkNotNullParameter(string, "");
            Builder builder2 = builder = this;
            if (!(((CharSequence)string).length() > 0)) {
                string = "method.isEmpty() == true";
                throw new IllegalArgumentException(string.toString());
            }
            if (requestBody == null) {
                if (!(!HttpMethod.requiresRequestBody(string))) {
                    string = "method " + string + " must have a request body.";
                    throw new IllegalArgumentException(string.toString());
                }
            } else if (!HttpMethod.permitsRequestBody(string)) {
                string = "method " + string + " must not have a request body.";
                throw new IllegalArgumentException(string.toString());
            }
            builder2.method = string;
            builder2.body = requestBody;
            return builder;
        }

        @NotNull
        public Builder tag(@Nullable Object object) {
            return this.tag(Object.class, object);
        }

        @NotNull
        public <T> Builder tag(@NotNull Class<? super T> clazz, @Nullable T t2) {
            Builder builder;
            Intrinsics.checkNotNullParameter(clazz, "");
            Builder builder2 = builder = this;
            if (t2 == null) {
                builder2.tags.remove(clazz);
            } else {
                if (builder2.tags.isEmpty()) {
                    builder2.tags = new LinkedHashMap();
                }
                Map<Class<?>, Object> map = builder2.tags;
                Class<T> clazz2 = clazz;
                T t3 = clazz2.cast(t2);
                Intrinsics.checkNotNull(t3);
                map.put(clazz2, t3);
            }
            return builder;
        }

        @NotNull
        public Request build() {
            HttpUrl httpUrl = this.url;
            if (httpUrl == null) {
                String string = "url == null";
                throw new IllegalStateException(string.toString());
            }
            Map<Class<?>, Object> map = Util.toImmutableMap(this.tags);
            RequestBody requestBody = this.body;
            Headers headers = this.headers.build();
            String string = this.method;
            HttpUrl httpUrl2 = httpUrl;
            return new Request(httpUrl2, string, headers, requestBody, map);
        }

        @JvmOverloads
        @NotNull
        public final Builder delete() {
            return Builder.delete$default(this, null, 1, null);
        }
    }
}


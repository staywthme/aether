/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.http;

import java.io.EOFException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.collections.MapsKt;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import okhttp3.Challenge;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Response;
import okhttp3.internal.Util;
import okhttp3.internal.platform.Platform;
import okio.Buffer;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 8, 0}, k=2, xi=48, d1={"\u0000R\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0005\n\u0000\u001a\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007\u001a\u0018\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b*\u00020\n2\u0006\u0010\u000b\u001a\u00020\f\u001a\n\u0010\r\u001a\u00020\u0004*\u00020\u0006\u001a\u001a\u0010\u000e\u001a\u00020\u000f*\u00020\u00102\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\t0\u0012H\u0002\u001a\u000e\u0010\u0013\u001a\u0004\u0018\u00010\f*\u00020\u0010H\u0002\u001a\u000e\u0010\u0014\u001a\u0004\u0018\u00010\f*\u00020\u0010H\u0002\u001a\u001a\u0010\u0015\u001a\u00020\u000f*\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\n\u001a\f\u0010\u001a\u001a\u00020\u0004*\u00020\u0010H\u0002\u001a\u0014\u0010\u001b\u001a\u00020\u0004*\u00020\u00102\u0006\u0010\u001c\u001a\u00020\u001dH\u0002\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001e"}, d2={"QUOTED_STRING_DELIMITERS", "Lokio/ByteString;", "TOKEN_DELIMITERS", "hasBody", "", "response", "Lokhttp3/Response;", "parseChallenges", "", "Lokhttp3/Challenge;", "Lokhttp3/Headers;", "headerName", "", "promisesBody", "readChallengeHeader", "", "Lokio/Buffer;", "result", "", "readQuotedString", "readToken", "receiveHeaders", "Lokhttp3/CookieJar;", "url", "Lokhttp3/HttpUrl;", "headers", "skipCommasAndWhitespace", "startsWith", "prefix", "", "okhttp"})
@JvmName(name="HttpHeaders")
public final class HttpHeaders {
    @NotNull
    private static final ByteString QUOTED_STRING_DELIMITERS = ByteString.Companion.encodeUtf8("\"\\");
    @NotNull
    private static final ByteString TOKEN_DELIMITERS = ByteString.Companion.encodeUtf8("\t ,=");

    @NotNull
    public static final List<Challenge> parseChallenges(@NotNull Headers headers, @NotNull String string) {
        Intrinsics.checkNotNullParameter(headers, "");
        Intrinsics.checkNotNullParameter(string, "");
        List list2 = new ArrayList();
        int n2 = headers.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            if (!StringsKt.equals(string, headers.name(i2), true)) continue;
            Buffer buffer = new Buffer().writeUtf8(headers.value(i2));
            try {
                HttpHeaders.readChallengeHeader(buffer, list2);
                continue;
            }
            catch (EOFException eOFException) {
                Platform.Companion.get().log("Unable to parse challenge", 5, eOFException);
            }
        }
        return list2;
    }

    private static final void readChallengeHeader(Buffer buffer, List<Challenge> list2) throws EOFException {
        String string = null;
        while (true) {
            Map map;
            String string2;
            block11: {
                if (string == null) {
                    HttpHeaders.skipCommasAndWhitespace(buffer);
                    string = HttpHeaders.readToken(buffer);
                    if (string == null) {
                        return;
                    }
                }
                string2 = string;
                boolean bl = HttpHeaders.skipCommasAndWhitespace(buffer);
                string = HttpHeaders.readToken(buffer);
                if (string == null) {
                    if (!buffer.exhausted()) {
                        return;
                    }
                    list2.add(new Challenge(string2, MapsKt.emptyMap()));
                    return;
                }
                int n2 = Util.skipAll(buffer, (byte)61);
                boolean bl2 = HttpHeaders.skipCommasAndWhitespace(buffer);
                if (!bl && (bl2 || buffer.exhausted())) {
                    Map<Object, String> map2 = Collections.singletonMap(null, string + StringsKt.repeat("=", n2));
                    Intrinsics.checkNotNullExpressionValue(map2, "");
                    list2.add(new Challenge(string2, map2));
                    string = null;
                    continue;
                }
                map = new LinkedHashMap();
                n2 += Util.skipAll(buffer, (byte)61);
                do {
                    if (string == null) {
                        string = HttpHeaders.readToken(buffer);
                        if (HttpHeaders.skipCommasAndWhitespace(buffer)) break block11;
                        n2 = Util.skipAll(buffer, (byte)61);
                    }
                    if (n2 == 0) break block11;
                    if (n2 > 1) {
                        return;
                    }
                    if (HttpHeaders.skipCommasAndWhitespace(buffer)) {
                        return;
                    }
                    String string3 = HttpHeaders.startsWith(buffer, (byte)34) ? HttpHeaders.readQuotedString(buffer) : HttpHeaders.readToken(buffer);
                    if (string3 == null) {
                        return;
                    }
                    String string4 = string3;
                    string4 = map.put(string, string4);
                    string = null;
                    if (string4 == null) continue;
                    return;
                } while (HttpHeaders.skipCommasAndWhitespace(buffer) || buffer.exhausted());
                return;
            }
            list2.add(new Challenge(string2, map));
        }
    }

    private static final boolean skipCommasAndWhitespace(Buffer buffer) {
        boolean bl = false;
        while (!buffer.exhausted()) {
            byte by = buffer.getByte(0L);
            if (by == 44) {
                buffer.readByte();
                bl = true;
                continue;
            }
            if (!(by == 32 ? true : by == 9)) break;
            buffer.readByte();
        }
        return bl;
    }

    private static final boolean startsWith(Buffer buffer, byte by) {
        return !buffer.exhausted() && buffer.getByte(0L) == by;
    }

    private static final String readQuotedString(Buffer buffer) throws EOFException {
        if (!(buffer.readByte() == 34)) {
            String string = "Failed requirement.";
            throw new IllegalArgumentException(string.toString());
        }
        Buffer buffer2 = new Buffer();
        long l2;
        while ((l2 = buffer.indexOfElement(QUOTED_STRING_DELIMITERS)) != -1L) {
            if (buffer.getByte(l2) == 34) {
                buffer2.write(buffer, l2);
                buffer.readByte();
                return buffer2.readUtf8();
            }
            if (buffer.size() == l2 + 1L) {
                return null;
            }
            buffer2.write(buffer, l2);
            buffer.readByte();
            buffer2.write(buffer, 1L);
        }
        return null;
    }

    private static final String readToken(Buffer buffer) {
        long l2 = buffer.indexOfElement(TOKEN_DELIMITERS);
        if (l2 == -1L) {
            l2 = buffer.size();
        }
        if (l2 != 0L) {
            return buffer.readUtf8(l2);
        }
        return null;
    }

    public static final void receiveHeaders(@NotNull CookieJar cookieJar, @NotNull HttpUrl httpUrl, @NotNull Headers iterable) {
        Intrinsics.checkNotNullParameter(cookieJar, "");
        Intrinsics.checkNotNullParameter(httpUrl, "");
        Intrinsics.checkNotNullParameter(iterable, "");
        if (cookieJar == CookieJar.NO_COOKIES) {
            return;
        }
        if ((iterable = Cookie.Companion.parseAll(httpUrl, (Headers)iterable)).isEmpty()) {
            return;
        }
        cookieJar.saveFromResponse(httpUrl, (List<Cookie>)iterable);
    }

    public static final boolean promisesBody(@NotNull Response response) {
        Intrinsics.checkNotNullParameter(response, "");
        if (Intrinsics.areEqual(response.request().method(), "HEAD")) {
            return false;
        }
        int n2 = response.code();
        if ((n2 < 100 || n2 >= 200) && n2 != 204 && n2 != 304) {
            return true;
        }
        return Util.headersContentLength(response) != -1L || StringsKt.equals("chunked", Response.header$default(response, "Transfer-Encoding", null, 2, null), true);
    }

    @Deprecated(message="No longer supported", replaceWith=@ReplaceWith(expression="response.promisesBody()", imports={}), level=DeprecationLevel.ERROR)
    public static final boolean hasBody(@NotNull Response response) {
        Intrinsics.checkNotNullParameter(response, "");
        return HttpHeaders.promisesBody(response);
    }
}


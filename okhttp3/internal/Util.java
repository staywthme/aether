/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import kotlin.ExceptionsKt;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.IntIterator;
import kotlin.collections.MapsKt;
import kotlin.io.CloseableKt;
import kotlin.jvm.JvmField;
import kotlin.jvm.JvmName;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.ArrayIteratorKt;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.Charsets;
import kotlin.text.Regex;
import kotlin.text.StringsKt;
import okhttp3.Call;
import okhttp3.EventListener;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http2.Header;
import okhttp3.internal.io.FileSystem;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;
import okio.Options;
import okio.Sink;
import okio.Source;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=2, xi=48, d1={"\u0000\u00b8\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010!\n\u0002\b\u0003\n\u0002\u0010\u0005\n\u0000\n\u0002\u0010\n\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\f\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u001c\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010$\n\u0002\b\b\n\u0002\u0010\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a \u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00112\u0006\u0010\u0016\u001a\u00020\u00172\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019\u001a\u001e\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u00172\u0006\u0010\u001d\u001a\u00020\u00172\u0006\u0010\u001e\u001a\u00020\u0017\u001a'\u0010\u001f\u001a\u00020\u00112\u0006\u0010\u001f\u001a\u00020\u00112\u0012\u0010 \u001a\n\u0012\u0006\b\u0001\u0012\u00020\"0!\"\u00020\"\u00a2\u0006\u0002\u0010#\u001a\u001a\u0010$\u001a\u00020\u001b2\f\u0010%\u001a\b\u0012\u0004\u0012\u00020\u001b0&H\u0086\b\u00f8\u0001\u0000\u001a-\u0010'\u001a\b\u0012\u0004\u0012\u0002H)0(\"\u0004\b\u0000\u0010)2\u0012\u0010*\u001a\n\u0012\u0006\b\u0001\u0012\u0002H)0!\"\u0002H)H\u0007\u00a2\u0006\u0002\u0010+\u001a\u000e\u0010,\u001a\u00020\u000f2\u0006\u0010\u0015\u001a\u00020\u0011\u001a1\u0010-\u001a\u0004\u0018\u0001H)\"\u0004\b\u0000\u0010)2\u0006\u0010.\u001a\u00020\"2\f\u0010/\u001a\b\u0012\u0004\u0012\u0002H)002\u0006\u00101\u001a\u00020\u0011\u00a2\u0006\u0002\u00102\u001a\u0016\u00103\u001a\u0002042\u0006\u0010\u0015\u001a\u00020\u00112\u0006\u00105\u001a\u00020\u000f\u001a\"\u00106\u001a\u00020\u001b2\u0006\u0010\u0015\u001a\u00020\u00112\f\u0010%\u001a\b\u0012\u0004\u0012\u00020\u001b0&H\u0086\b\u00f8\u0001\u0000\u001a%\u00107\u001a\u00020\u001b\"\u0004\b\u0000\u00108*\b\u0012\u0004\u0012\u0002H8092\u0006\u0010:\u001a\u0002H8H\u0000\u00a2\u0006\u0002\u0010;\u001a\u0015\u0010<\u001a\u00020\u0014*\u00020=2\u0006\u0010>\u001a\u00020\u0014H\u0086\u0004\u001a\u0015\u0010<\u001a\u00020\u0017*\u00020\u00142\u0006\u0010>\u001a\u00020\u0017H\u0086\u0004\u001a\u0015\u0010<\u001a\u00020\u0014*\u00020?2\u0006\u0010>\u001a\u00020\u0014H\u0086\u0004\u001a\n\u0010@\u001a\u00020A*\u00020B\u001a\r\u0010C\u001a\u00020\u001b*\u00020\"H\u0080\b\u001a\r\u0010D\u001a\u00020\u001b*\u00020\"H\u0080\b\u001a\n\u0010E\u001a\u00020\u000f*\u00020\u0011\u001a\u0012\u0010F\u001a\u00020\u000f*\u00020G2\u0006\u0010H\u001a\u00020G\u001a\n\u0010I\u001a\u00020\u001b*\u00020J\u001a\n\u0010I\u001a\u00020\u001b*\u00020K\u001a\n\u0010I\u001a\u00020\u001b*\u00020L\u001a#\u0010M\u001a\b\u0012\u0004\u0012\u00020\u00110!*\b\u0012\u0004\u0012\u00020\u00110!2\u0006\u0010N\u001a\u00020\u0011\u00a2\u0006\u0002\u0010O\u001a&\u0010P\u001a\u00020\u0014*\u00020\u00112\u0006\u0010Q\u001a\u00020R2\b\b\u0002\u0010S\u001a\u00020\u00142\b\b\u0002\u0010T\u001a\u00020\u0014\u001a&\u0010P\u001a\u00020\u0014*\u00020\u00112\u0006\u0010U\u001a\u00020\u00112\b\b\u0002\u0010S\u001a\u00020\u00142\b\b\u0002\u0010T\u001a\u00020\u0014\u001a\u001a\u0010V\u001a\u00020\u000f*\u00020W2\u0006\u0010X\u001a\u00020\u00142\u0006\u0010Y\u001a\u00020\u0019\u001a;\u0010Z\u001a\b\u0012\u0004\u0012\u0002H)0(\"\u0004\b\u0000\u0010)*\b\u0012\u0004\u0012\u0002H)0[2\u0017\u0010\\\u001a\u0013\u0012\u0004\u0012\u0002H)\u0012\u0004\u0012\u00020\u000f0]\u00a2\u0006\u0002\b^H\u0086\b\u00f8\u0001\u0000\u001a5\u0010_\u001a\u00020\u000f*\b\u0012\u0004\u0012\u00020\u00110!2\u000e\u0010H\u001a\n\u0012\u0004\u0012\u00020\u0011\u0018\u00010!2\u000e\u0010`\u001a\n\u0012\u0006\b\u0000\u0012\u00020\u00110a\u00a2\u0006\u0002\u0010b\u001a\n\u0010c\u001a\u00020\u0017*\u00020d\u001a+\u0010e\u001a\u00020\u0014*\b\u0012\u0004\u0012\u00020\u00110!2\u0006\u0010N\u001a\u00020\u00112\f\u0010`\u001a\b\u0012\u0004\u0012\u00020\u00110a\u00a2\u0006\u0002\u0010f\u001a\n\u0010g\u001a\u00020\u0014*\u00020\u0011\u001a\u001e\u0010h\u001a\u00020\u0014*\u00020\u00112\b\b\u0002\u0010S\u001a\u00020\u00142\b\b\u0002\u0010T\u001a\u00020\u0014\u001a\u001e\u0010i\u001a\u00020\u0014*\u00020\u00112\b\b\u0002\u0010S\u001a\u00020\u00142\b\b\u0002\u0010T\u001a\u00020\u0014\u001a\u0014\u0010j\u001a\u00020\u0014*\u00020\u00112\b\b\u0002\u0010S\u001a\u00020\u0014\u001a9\u0010k\u001a\b\u0012\u0004\u0012\u00020\u00110!*\b\u0012\u0004\u0012\u00020\u00110!2\f\u0010H\u001a\b\u0012\u0004\u0012\u00020\u00110!2\u000e\u0010`\u001a\n\u0012\u0006\b\u0000\u0012\u00020\u00110a\u00a2\u0006\u0002\u0010l\u001a\u0012\u0010m\u001a\u00020\u000f*\u00020n2\u0006\u0010o\u001a\u00020p\u001a\u0012\u0010q\u001a\u00020\u000f*\u00020L2\u0006\u0010r\u001a\u00020s\u001a\r\u0010t\u001a\u00020\u001b*\u00020\"H\u0086\b\u001a\r\u0010u\u001a\u00020\u001b*\u00020\"H\u0086\b\u001a\n\u0010v\u001a\u00020\u0014*\u00020R\u001a\n\u0010w\u001a\u00020\u0011*\u00020L\u001a\u0012\u0010x\u001a\u00020y*\u00020s2\u0006\u0010z\u001a\u00020y\u001a\n\u0010{\u001a\u00020\u0014*\u00020s\u001a\u0012\u0010|\u001a\u00020\u0014*\u00020}2\u0006\u0010~\u001a\u00020=\u001a\u001a\u0010|\u001a\u00020\u000f*\u00020W2\u0006\u0010\u0016\u001a\u00020\u00142\u0006\u0010Y\u001a\u00020\u0019\u001a\u0011\u0010\u007f\u001a\t\u0012\u0005\u0012\u00030\u0080\u00010(*\u00020\u0003\u001a\u0012\u0010\u0081\u0001\u001a\u00020\u0003*\t\u0012\u0005\u0012\u00030\u0080\u00010(\u001a\u000b\u0010\u0082\u0001\u001a\u00020\u0011*\u00020\u0014\u001a\u000b\u0010\u0082\u0001\u001a\u00020\u0011*\u00020\u0017\u001a\u0016\u0010\u0083\u0001\u001a\u00020\u0011*\u00020G2\t\b\u0002\u0010\u0084\u0001\u001a\u00020\u000f\u001a\u001d\u0010\u0085\u0001\u001a\b\u0012\u0004\u0012\u0002H)0(\"\u0004\b\u0000\u0010)*\b\u0012\u0004\u0012\u0002H)0(\u001a7\u0010\u0086\u0001\u001a\u0011\u0012\u0005\u0012\u0003H\u0088\u0001\u0012\u0005\u0012\u0003H\u0089\u00010\u0087\u0001\"\u0005\b\u0000\u0010\u0088\u0001\"\u0005\b\u0001\u0010\u0089\u0001*\u0011\u0012\u0005\u0012\u0003H\u0088\u0001\u0012\u0005\u0012\u0003H\u0089\u00010\u0087\u0001\u001a\u0014\u0010\u008a\u0001\u001a\u00020\u0017*\u00020\u00112\u0007\u0010\u008b\u0001\u001a\u00020\u0017\u001a\u0016\u0010\u008c\u0001\u001a\u00020\u0014*\u0004\u0018\u00010\u00112\u0007\u0010\u008b\u0001\u001a\u00020\u0014\u001a\u001f\u0010\u008d\u0001\u001a\u00020\u0011*\u00020\u00112\b\b\u0002\u0010S\u001a\u00020\u00142\b\b\u0002\u0010T\u001a\u00020\u0014\u001a\u000e\u0010\u008e\u0001\u001a\u00020\u001b*\u00020\"H\u0086\b\u001a'\u0010\u008f\u0001\u001a\u00030\u0090\u0001*\b0\u0091\u0001j\u0003`\u0092\u00012\u0013\u0010\u0093\u0001\u001a\u000e\u0012\n\u0012\b0\u0091\u0001j\u0003`\u0092\u00010(\u001a\u0015\u0010\u0094\u0001\u001a\u00020\u001b*\u00030\u0095\u00012\u0007\u0010\u0096\u0001\u001a\u00020\u0014\"\u0010\u0010\u0000\u001a\u00020\u00018\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000\"\u0010\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000\"\u0010\u0010\u0004\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000\"\u0010\u0010\u0006\u001a\u00020\u00078\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000\"\u0010\u0010\n\u001a\u00020\u000b8\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000\"\u0010\u0010\u000e\u001a\u00020\u000f8\u0000X\u0081\u0004\u00a2\u0006\u0002\n\u0000\"\u0010\u0010\u0010\u001a\u00020\u00118\u0000X\u0081\u0004\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0012\u001a\u00020\u0011X\u0086T\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0007\n\u0005\b\u009920\u0001\u00a8\u0006\u0097\u0001"}, d2={"EMPTY_BYTE_ARRAY", "", "EMPTY_HEADERS", "Lokhttp3/Headers;", "EMPTY_REQUEST", "Lokhttp3/RequestBody;", "EMPTY_RESPONSE", "Lokhttp3/ResponseBody;", "UNICODE_BOMS", "Lokio/Options;", "UTC", "Ljava/util/TimeZone;", "VERIFY_AS_IP_ADDRESS", "Lkotlin/text/Regex;", "assertionsEnabled", "", "okHttpName", "", "userAgent", "checkDuration", "", "name", "duration", "", "unit", "Ljava/util/concurrent/TimeUnit;", "checkOffsetAndCount", "", "arrayLength", "offset", "count", "format", "args", "", "", "(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", "ignoreIoExceptions", "block", "Lkotlin/Function0;", "immutableListOf", "", "T", "elements", "([Ljava/lang/Object;)Ljava/util/List;", "isSensitiveHeader", "readFieldOrNull", "instance", "fieldType", "Ljava/lang/Class;", "fieldName", "(Ljava/lang/Object;Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Object;", "threadFactory", "Ljava/util/concurrent/ThreadFactory;", "daemon", "threadName", "addIfAbsent", "E", "", "element", "(Ljava/util/List;Ljava/lang/Object;)V", "and", "", "mask", "", "asFactory", "Lokhttp3/EventListener$Factory;", "Lokhttp3/EventListener;", "assertThreadDoesntHoldLock", "assertThreadHoldsLock", "canParseAsIpAddress", "canReuseConnectionFor", "Lokhttp3/HttpUrl;", "other", "closeQuietly", "Ljava/io/Closeable;", "Ljava/net/ServerSocket;", "Ljava/net/Socket;", "concat", "value", "([Ljava/lang/String;Ljava/lang/String;)[Ljava/lang/String;", "delimiterOffset", "delimiter", "", "startIndex", "endIndex", "delimiters", "discard", "Lokio/Source;", "timeout", "timeUnit", "filterList", "", "predicate", "Lkotlin/Function1;", "Lkotlin/ExtensionFunctionType;", "hasIntersection", "comparator", "Ljava/util/Comparator;", "([Ljava/lang/String;[Ljava/lang/String;Ljava/util/Comparator;)Z", "headersContentLength", "Lokhttp3/Response;", "indexOf", "([Ljava/lang/String;Ljava/lang/String;Ljava/util/Comparator;)I", "indexOfControlOrNonAscii", "indexOfFirstNonAsciiWhitespace", "indexOfLastNonAsciiWhitespace", "indexOfNonWhitespace", "intersect", "([Ljava/lang/String;[Ljava/lang/String;Ljava/util/Comparator;)[Ljava/lang/String;", "isCivilized", "Lokhttp3/internal/io/FileSystem;", "file", "Ljava/io/File;", "isHealthy", "source", "Lokio/BufferedSource;", "notify", "notifyAll", "parseHexDigit", "peerName", "readBomAsCharset", "Ljava/nio/charset/Charset;", "default", "readMedium", "skipAll", "Lokio/Buffer;", "b", "toHeaderList", "Lokhttp3/internal/http2/Header;", "toHeaders", "toHexString", "toHostHeader", "includeDefaultPort", "toImmutableList", "toImmutableMap", "", "K", "V", "toLongOrDefault", "defaultValue", "toNonNegativeInt", "trimSubstring", "wait", "withSuppressed", "", "Ljava/lang/Exception;", "Lkotlin/Exception;", "suppressed", "writeMedium", "Lokio/BufferedSink;", "medium", "okhttp"})
@JvmName(name="Util")
@SourceDebugExtension(value={"SMAP\nUtil.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Util.kt\nokhttp3/internal/Util\n+ 2 ArraysJVM.kt\nkotlin/collections/ArraysKt__ArraysJVMKt\n+ 3 _Arrays.kt\nkotlin/collections/ArraysKt___ArraysKt\n+ 4 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 5 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,636:1\n37#2,2:637\n1627#3,6:639\n1#4:645\n1549#5:646\n1620#5,3:647\n*S KotlinDebug\n*F\n+ 1 Util.kt\nokhttp3/internal/Util\n*L\n127#1:637,2\n167#1:639,6\n300#1:646\n300#1:647,3\n*E\n"})
public final class Util {
    @JvmField
    @NotNull
    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    @JvmField
    @NotNull
    public static final Headers EMPTY_HEADERS = Headers.Companion.of(new String[0]);
    @JvmField
    @NotNull
    public static final ResponseBody EMPTY_RESPONSE = ResponseBody.Companion.create$default(ResponseBody.Companion, EMPTY_BYTE_ARRAY, null, 1, null);
    @JvmField
    @NotNull
    public static final RequestBody EMPTY_REQUEST = RequestBody.Companion.create$default(RequestBody.Companion, EMPTY_BYTE_ARRAY, null, 0, 0, 7, null);
    @NotNull
    private static final Options UNICODE_BOMS;
    @JvmField
    @NotNull
    public static final TimeZone UTC;
    @NotNull
    private static final Regex VERIFY_AS_IP_ADDRESS;
    @JvmField
    public static final boolean assertionsEnabled;
    @JvmField
    @NotNull
    public static final String okHttpName;
    @NotNull
    public static final String userAgent = "okhttp/4.12.0";

    public static final void checkOffsetAndCount(long l2, long l3, long l4) {
        if ((l3 | l4) < 0L || l3 > l2 || l2 - l3 < l4) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    @NotNull
    public static final ThreadFactory threadFactory(@NotNull String string, boolean bl) {
        Intrinsics.checkNotNullParameter(string, "");
        return arg_0 -> Util.threadFactory$lambda$1(string, bl, arg_0);
    }

    @NotNull
    public static final String[] intersect(@NotNull String[] stringArray, @NotNull String[] stringArray2, @NotNull Comparator<? super String> comparator) {
        Collection collection;
        Intrinsics.checkNotNullParameter(stringArray, "");
        Intrinsics.checkNotNullParameter(stringArray2, "");
        Intrinsics.checkNotNullParameter(comparator, "");
        List list2 = new ArrayList();
        block0: for (String object2 : stringArray) {
            for (String string : stringArray2) {
                if (comparator.compare(object2, string) != 0) continue;
                list2.add(object2);
                continue block0;
            }
        }
        Collection collection2 = collection = (Collection)list2;
        return collection.toArray(new String[0]);
    }

    public static final boolean hasIntersection(@NotNull String[] stringArray, @Nullable String[] stringArray2, @NotNull Comparator<? super String> comparator) {
        Intrinsics.checkNotNullParameter(stringArray, "");
        Intrinsics.checkNotNullParameter(comparator, "");
        if (stringArray.length == 0 || stringArray2 == null || stringArray2.length == 0) {
            return false;
        }
        for (String string : stringArray) {
            Iterator<String> iterator2 = ArrayIteratorKt.iterator(stringArray2);
            while (iterator2.hasNext()) {
                String string2 = iterator2.next();
                if (comparator.compare(string, string2) != 0) continue;
                return true;
            }
        }
        return false;
    }

    @NotNull
    public static final String toHostHeader(@NotNull HttpUrl httpUrl, boolean bl) {
        String string;
        Intrinsics.checkNotNullParameter(httpUrl, "");
        String string2 = string = StringsKt.contains$default((CharSequence)httpUrl.host(), ":", false, 2, null) ? "[" + httpUrl.host() + ']' : httpUrl.host();
        if (bl || httpUrl.port() != HttpUrl.Companion.defaultPort(httpUrl.scheme())) {
            return string + ':' + httpUrl.port();
        }
        return string;
    }

    public static /* synthetic */ String toHostHeader$default(HttpUrl httpUrl, boolean bl, int n2, Object object) {
        if ((n2 & 1) != 0) {
            bl = false;
        }
        return Util.toHostHeader(httpUrl, bl);
    }

    public static final int indexOf(@NotNull String[] stringArray, @NotNull String string, @NotNull Comparator<String> comparator) {
        Intrinsics.checkNotNullParameter(stringArray, "");
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(comparator, "");
        int n2 = stringArray.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            String string2 = stringArray[i2];
            if (!(comparator.compare(string2, string) == 0)) continue;
            return i2;
        }
        return -1;
    }

    @NotNull
    public static final String[] concat(@NotNull String[] stringArray, @NotNull String string) {
        Intrinsics.checkNotNullParameter(stringArray, "");
        Intrinsics.checkNotNullParameter(string, "");
        String[] stringArray2 = Arrays.copyOf(stringArray, stringArray.length + 1);
        Intrinsics.checkNotNullExpressionValue(stringArray2, "");
        stringArray = stringArray2;
        stringArray2[ArraysKt.getLastIndex(stringArray)] = string;
        return stringArray;
    }

    public static final int indexOfFirstNonAsciiWhitespace(@NotNull String string, int n2, int n3) {
        Intrinsics.checkNotNullParameter(string, "");
        while (n2 < n3) {
            char c2 = string.charAt(n2);
            if (!((((c2 == '\t' ? true : c2 == '\n') ? true : c2 == '\f') ? true : c2 == '\r') ? true : c2 == ' ')) {
                return n2;
            }
            ++n2;
        }
        return n3;
    }

    public static /* synthetic */ int indexOfFirstNonAsciiWhitespace$default(String string, int n2, int n3, int n4, Object object) {
        if ((n4 & 1) != 0) {
            n2 = 0;
        }
        if ((n4 & 2) != 0) {
            n3 = string.length();
        }
        return Util.indexOfFirstNonAsciiWhitespace(string, n2, n3);
    }

    public static final int indexOfLastNonAsciiWhitespace(@NotNull String string, int n2, int n3) {
        Intrinsics.checkNotNullParameter(string, "");
        if (n2 <= --n3) {
            while (true) {
                char c2;
                if (!(((((c2 = string.charAt(n3)) == '\t' ? true : c2 == '\n') ? true : c2 == '\f') ? true : c2 == '\r') ? true : c2 == ' ')) {
                    return n3 + 1;
                }
                if (n3 == n2) break;
                --n3;
            }
        }
        return n2;
    }

    public static /* synthetic */ int indexOfLastNonAsciiWhitespace$default(String string, int n2, int n3, int n4, Object object) {
        if ((n4 & 1) != 0) {
            n2 = 0;
        }
        if ((n4 & 2) != 0) {
            n3 = string.length();
        }
        return Util.indexOfLastNonAsciiWhitespace(string, n2, n3);
    }

    @NotNull
    public static final String trimSubstring(@NotNull String string, int n2, int n3) {
        Intrinsics.checkNotNullParameter(string, "");
        n2 = Util.indexOfFirstNonAsciiWhitespace(string, n2, n3);
        n3 = Util.indexOfLastNonAsciiWhitespace(string, n2, n3);
        String string2 = string.substring(n2, n3);
        Intrinsics.checkNotNullExpressionValue(string2, "");
        return string2;
    }

    public static /* synthetic */ String trimSubstring$default(String string, int n2, int n3, int n4, Object object) {
        if ((n4 & 1) != 0) {
            n2 = 0;
        }
        if ((n4 & 2) != 0) {
            n3 = string.length();
        }
        return Util.trimSubstring(string, n2, n3);
    }

    public static final int delimiterOffset(@NotNull String string, @NotNull String string2, int n2, int n3) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(string2, "");
        while (n2 < n3) {
            if (StringsKt.contains$default((CharSequence)string2, string.charAt(n2), false, 2, null)) {
                return n2;
            }
            ++n2;
        }
        return n3;
    }

    public static /* synthetic */ int delimiterOffset$default(String string, String string2, int n2, int n3, int n4, Object object) {
        if ((n4 & 2) != 0) {
            n2 = 0;
        }
        if ((n4 & 4) != 0) {
            n3 = string.length();
        }
        return Util.delimiterOffset(string, string2, n2, n3);
    }

    public static final int delimiterOffset(@NotNull String string, char c2, int n2, int n3) {
        Intrinsics.checkNotNullParameter(string, "");
        while (n2 < n3) {
            if (string.charAt(n2) == c2) {
                return n2;
            }
            ++n2;
        }
        return n3;
    }

    public static /* synthetic */ int delimiterOffset$default(String string, char c2, int n2, int n3, int n4, Object object) {
        if ((n4 & 2) != 0) {
            n2 = 0;
        }
        if ((n4 & 4) != 0) {
            n3 = string.length();
        }
        return Util.delimiterOffset(string, c2, n2, n3);
    }

    public static final int indexOfControlOrNonAscii(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, "");
        int n2 = string.length();
        for (int i2 = 0; i2 < n2; ++i2) {
            char c2 = string.charAt(i2);
            if (Intrinsics.compare(c2, 31) > 0 && Intrinsics.compare(c2, 127) < 0) continue;
            return i2;
        }
        return -1;
    }

    public static final boolean canParseAsIpAddress(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, "");
        return VERIFY_AS_IP_ADDRESS.matches(string);
    }

    public static final boolean isSensitiveHeader(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, "");
        return StringsKt.equals(string, "Authorization", true) || StringsKt.equals(string, "Cookie", true) || StringsKt.equals(string, "Proxy-Authorization", true) || StringsKt.equals(string, "Set-Cookie", true);
    }

    @NotNull
    public static final String format(@NotNull String string, Object ... objectArray) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(objectArray, "");
        Locale locale = Locale.US;
        objectArray = Arrays.copyOf(objectArray, objectArray.length);
        String string2 = String.format(locale, string, Arrays.copyOf(objectArray, objectArray.length));
        Intrinsics.checkNotNullExpressionValue(string2, "");
        return string2;
    }

    @NotNull
    public static final Charset readBomAsCharset(@NotNull BufferedSource bufferedSource, @NotNull Charset charset) throws IOException {
        Intrinsics.checkNotNullParameter(bufferedSource, "");
        Intrinsics.checkNotNullParameter(charset, "");
        switch (bufferedSource.select(UNICODE_BOMS)) {
            case 0: {
                Charset charset2 = StandardCharsets.UTF_8;
                Intrinsics.checkNotNullExpressionValue(charset2, "");
                return charset2;
            }
            case 1: {
                Charset charset3 = StandardCharsets.UTF_16BE;
                Intrinsics.checkNotNullExpressionValue(charset3, "");
                return charset3;
            }
            case 2: {
                Charset charset4 = StandardCharsets.UTF_16LE;
                Intrinsics.checkNotNullExpressionValue(charset4, "");
                return charset4;
            }
            case 3: {
                return Charsets.INSTANCE.UTF32_BE();
            }
            case 4: {
                return Charsets.INSTANCE.UTF32_LE();
            }
            case -1: {
                return charset;
            }
        }
        throw new AssertionError();
    }

    public static final int checkDuration(@NotNull String string, long l2, @Nullable TimeUnit timeUnit) {
        Intrinsics.checkNotNullParameter(string, "");
        if (!(l2 >= 0L)) {
            String string2 = string + " < 0";
            throw new IllegalStateException(string2.toString());
        }
        if (!(timeUnit != null)) {
            String string3 = "unit == null";
            throw new IllegalStateException(string3.toString());
        }
        long l3 = timeUnit.toMillis(l2);
        if (!(l3 <= Integer.MAX_VALUE)) {
            string = string + " too large.";
            throw new IllegalArgumentException(string.toString());
        }
        if (!(l3 != 0L || l2 <= 0L)) {
            string = string + " too small.";
            throw new IllegalArgumentException(string.toString());
        }
        return (int)l3;
    }

    public static final int parseHexDigit(char c2) {
        boolean bl = '0' <= c2 ? c2 < ':' : false;
        if (bl) {
            return c2 - 48;
        }
        boolean bl2 = 'a' <= c2 ? c2 < 'g' : false;
        if (bl2) {
            return c2 - 97 + 10;
        }
        boolean bl3 = 'A' <= c2 ? c2 < 'G' : false;
        if (bl3) {
            return c2 - 65 + 10;
        }
        return -1;
    }

    @NotNull
    public static final Headers toHeaders(@NotNull List<Header> object) {
        Intrinsics.checkNotNullParameter(object, "");
        Headers.Builder builder = new Headers.Builder();
        object = object.iterator();
        while (object.hasNext()) {
            Object object2 = (Header)object.next();
            ByteString byteString = ((Header)object2).component1();
            object2 = ((Header)object2).component2();
            builder.addLenient$okhttp(byteString.utf8(), ((ByteString)object2).utf8());
        }
        return builder.build();
    }

    @NotNull
    public static final List<Header> toHeaderList(@NotNull Headers headers) {
        Intrinsics.checkNotNullParameter(headers, "");
        Iterable iterable = RangesKt.until(0, headers.size());
        Object object = iterable;
        iterable = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
        object = object.iterator();
        while (object.hasNext()) {
            int n2 = ((IntIterator)object).nextInt();
            Iterable iterable2 = iterable;
            iterable2.add(new Header(headers.name(n2), headers.value(n2)));
        }
        return (List)iterable;
    }

    public static final boolean canReuseConnectionFor(@NotNull HttpUrl httpUrl, @NotNull HttpUrl httpUrl2) {
        Intrinsics.checkNotNullParameter(httpUrl, "");
        Intrinsics.checkNotNullParameter(httpUrl2, "");
        return Intrinsics.areEqual(httpUrl.host(), httpUrl2.host()) && httpUrl.port() == httpUrl2.port() && Intrinsics.areEqual(httpUrl.scheme(), httpUrl2.scheme());
    }

    @NotNull
    public static final EventListener.Factory asFactory(@NotNull EventListener eventListener) {
        Intrinsics.checkNotNullParameter(eventListener, "");
        return arg_0 -> Util.asFactory$lambda$8(eventListener, arg_0);
    }

    public static final int and(byte by, int n2) {
        return by & n2;
    }

    public static final int and(short s2, int n2) {
        return s2 & n2;
    }

    public static final long and(int n2, long l2) {
        return (long)n2 & l2;
    }

    public static final void writeMedium(@NotNull BufferedSink bufferedSink, int n2) throws IOException {
        Intrinsics.checkNotNullParameter(bufferedSink, "");
        bufferedSink.writeByte(n2 >>> 16 & 0xFF);
        bufferedSink.writeByte(n2 >>> 8 & 0xFF);
        bufferedSink.writeByte(n2 & 0xFF);
    }

    public static final int readMedium(@NotNull BufferedSource bufferedSource) throws IOException {
        Intrinsics.checkNotNullParameter(bufferedSource, "");
        return Util.and(bufferedSource.readByte(), 255) << 16 | Util.and(bufferedSource.readByte(), 255) << 8 | Util.and(bufferedSource.readByte(), 255);
    }

    public static final boolean skipAll(@NotNull Source source2, int n2, @NotNull TimeUnit timeUnit) throws IOException {
        Intrinsics.checkNotNullParameter(source2, "");
        Intrinsics.checkNotNullParameter((Object)timeUnit, "");
        long l2 = System.nanoTime();
        long l3 = source2.timeout().hasDeadline() ? source2.timeout().deadlineNanoTime() - l2 : Long.MAX_VALUE;
        source2.timeout().deadlineNanoTime(l2 + Math.min(l3, timeUnit.toNanos(n2)));
        try {
            Buffer buffer = new Buffer();
            while (source2.read(buffer, 8192L) != -1L) {
                buffer.clear();
            }
            n2 = 1;
        }
        catch (InterruptedIOException interruptedIOException) {
            n2 = 0;
        }
        finally {
            if (l3 == Long.MAX_VALUE) {
                source2.timeout().clearDeadline();
            } else {
                source2.timeout().deadlineNanoTime(l2 + l3);
            }
        }
        return n2 != 0;
    }

    public static final boolean discard(@NotNull Source source2, int n2, @NotNull TimeUnit timeUnit) {
        boolean bl;
        Intrinsics.checkNotNullParameter(source2, "");
        Intrinsics.checkNotNullParameter((Object)timeUnit, "");
        try {
            bl = Util.skipAll(source2, n2, timeUnit);
        }
        catch (IOException iOException) {
            bl = false;
        }
        return bl;
    }

    @NotNull
    public static final String peerName(@NotNull Socket object) {
        Intrinsics.checkNotNullParameter(object, "");
        object = ((Socket)object).getRemoteSocketAddress();
        if (object instanceof InetSocketAddress) {
            String string = ((InetSocketAddress)object).getHostName();
            Intrinsics.checkNotNullExpressionValue(string, "");
            return string;
        }
        return object.toString();
    }

    public static final boolean isHealthy(@NotNull Socket socket, @NotNull BufferedSource bufferedSource) {
        int n2;
        Intrinsics.checkNotNullParameter(socket, "");
        Intrinsics.checkNotNullParameter(bufferedSource, "");
        try {
            int n3;
            n2 = socket.getSoTimeout();
            try {
                socket.setSoTimeout(1);
                n3 = !bufferedSource.exhausted() ? 1 : 0;
            }
            finally {
                socket.setSoTimeout(n2);
            }
            n2 = n3;
        }
        catch (SocketTimeoutException socketTimeoutException) {
            n2 = 1;
        }
        catch (IOException iOException) {
            n2 = 0;
        }
        return n2 != 0;
    }

    public static final void ignoreIoExceptions(@NotNull Function0<Unit> function0) {
        Intrinsics.checkNotNullParameter(function0, "");
        try {
            function0.invoke();
            return;
        }
        catch (IOException iOException) {
            return;
        }
    }

    public static final void threadName(@NotNull String string, @NotNull Function0<Unit> function0) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(function0, "");
        Thread thread2 = Thread.currentThread();
        String string2 = thread2.getName();
        thread2.setName(string);
        try {
            function0.invoke();
            return;
        }
        finally {
            InlineMarker.finallyStart(1);
            thread2.setName(string2);
            InlineMarker.finallyEnd(1);
        }
    }

    public static final int skipAll(@NotNull Buffer buffer, byte by) {
        Intrinsics.checkNotNullParameter(buffer, "");
        int n2 = 0;
        while (!buffer.exhausted() && buffer.getByte(0L) == by) {
            ++n2;
            buffer.readByte();
        }
        return n2;
    }

    public static final int indexOfNonWhitespace(@NotNull String string, int n2) {
        Intrinsics.checkNotNullParameter(string, "");
        int n3 = string.length();
        while (n2 < n3) {
            char c2 = string.charAt(n2);
            if (c2 != ' ' && c2 != '\t') {
                return n2;
            }
            ++n2;
        }
        return string.length();
    }

    public static /* synthetic */ int indexOfNonWhitespace$default(String string, int n2, int n3, Object object) {
        if ((n3 & 1) != 0) {
            n2 = 0;
        }
        return Util.indexOfNonWhitespace(string, n2);
    }

    public static final long headersContentLength(@NotNull Response response) {
        Intrinsics.checkNotNullParameter(response, "");
        String string = response.headers().get("Content-Length");
        if (string != null) {
            return Util.toLongOrDefault(string, -1L);
        }
        return -1L;
    }

    public static final long toLongOrDefault(@NotNull String string, long l2) {
        long l3;
        Intrinsics.checkNotNullParameter(string, "");
        try {
            l3 = Long.parseLong(string);
        }
        catch (NumberFormatException numberFormatException) {
            l3 = l2;
        }
        return l3;
    }

    public static final int toNonNegativeInt(@Nullable String string, int n2) {
        try {
            String string2 = string;
            if (string2 == null) {
                return n2;
            }
            long l2 = Long.parseLong(string2);
            if (l2 > Integer.MAX_VALUE) {
                return Integer.MAX_VALUE;
            }
            if (l2 < 0L) {
                return 0;
            }
            return (int)l2;
        }
        catch (NumberFormatException numberFormatException) {
            return n2;
        }
    }

    @NotNull
    public static final <T> List<T> toImmutableList(@NotNull List<? extends T> list2) {
        Intrinsics.checkNotNullParameter(list2, "");
        List list3 = Collections.unmodifiableList(CollectionsKt.toMutableList((Collection)list2));
        Intrinsics.checkNotNullExpressionValue(list3, "");
        return list3;
    }

    @SafeVarargs
    @NotNull
    public static final <T> List<T> immutableListOf(T ... objectArray) {
        Intrinsics.checkNotNullParameter(objectArray, "");
        objectArray = (Object[])objectArray.clone();
        List<Object> list2 = Collections.unmodifiableList(CollectionsKt.listOf(Arrays.copyOf(objectArray, objectArray.length)));
        Intrinsics.checkNotNullExpressionValue(list2, "");
        return list2;
    }

    @NotNull
    public static final <K, V> Map<K, V> toImmutableMap(@NotNull Map<K, ? extends V> map) {
        Intrinsics.checkNotNullParameter(map, "");
        if (map.isEmpty()) {
            return MapsKt.emptyMap();
        }
        Map map2 = Collections.unmodifiableMap((Map)new LinkedHashMap<K, V>(map));
        Intrinsics.checkNotNullExpressionValue(map2, "");
        return map2;
    }

    public static final void closeQuietly(@NotNull Closeable object) {
        Intrinsics.checkNotNullParameter(object, "");
        try {
            object.close();
            return;
        }
        catch (RuntimeException runtimeException) {
            object = runtimeException;
            throw runtimeException;
        }
        catch (Exception exception) {
            return;
        }
    }

    public static final void closeQuietly(@NotNull Socket object) {
        Intrinsics.checkNotNullParameter(object, "");
        try {
            ((Socket)object).close();
            return;
        }
        catch (AssertionError assertionError) {
            object = assertionError;
            throw assertionError;
        }
        catch (RuntimeException runtimeException) {
            object = runtimeException;
            if (Intrinsics.areEqual(runtimeException.getMessage(), "bio == null")) {
                return;
            }
            throw object;
        }
        catch (Exception exception) {
            return;
        }
    }

    public static final void closeQuietly(@NotNull ServerSocket object) {
        Intrinsics.checkNotNullParameter(object, "");
        try {
            ((ServerSocket)object).close();
            return;
        }
        catch (RuntimeException runtimeException) {
            object = runtimeException;
            throw runtimeException;
        }
        catch (Exception exception) {
            return;
        }
    }

    /*
     * Loose catch block
     */
    public static final boolean isCivilized(@NotNull FileSystem object, @NotNull File file) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(file, "");
        Closeable closeable = object.sink(file);
        Throwable throwable = null;
        Sink cfr_ignored_0 = (Sink)closeable;
        object.delete(file);
        CloseableKt.closeFinally(closeable, null);
        return true;
        {
            catch (IOException iOException) {
                try {
                }
                catch (Throwable throwable2) {
                    try {
                        object = throwable2;
                        throwable = throwable2;
                        throw object;
                    }
                    catch (Throwable throwable3) {
                        CloseableKt.closeFinally(closeable, throwable);
                        throw throwable3;
                    }
                }
                CloseableKt.closeFinally(closeable, null);
            }
        }
        object.delete(file);
        return false;
    }

    @NotNull
    public static final String toHexString(long l2) {
        String string = Long.toHexString(l2);
        Intrinsics.checkNotNullExpressionValue(string, "");
        return string;
    }

    @NotNull
    public static final String toHexString(int n2) {
        String string = Integer.toHexString(n2);
        Intrinsics.checkNotNullExpressionValue(string, "");
        return string;
    }

    public static final void wait(@NotNull Object object) {
        Intrinsics.checkNotNullParameter(object, "");
        object.wait();
    }

    public static final void notify(@NotNull Object object) {
        Intrinsics.checkNotNullParameter(object, "");
        object.notify();
    }

    public static final void notifyAll(@NotNull Object object) {
        Intrinsics.checkNotNullParameter(object, "");
        object.notifyAll();
    }

    @Nullable
    public static final <T> T readFieldOrNull(@NotNull Object object, @NotNull Class<T> clazz, @NotNull String string) {
        while (true) {
            Object object2;
            Intrinsics.checkNotNullParameter(object, "");
            Intrinsics.checkNotNullParameter(clazz, "");
            Intrinsics.checkNotNullParameter(string, "");
            Class<?> clazz2 = object.getClass();
            while (!Intrinsics.areEqual(clazz2, Object.class)) {
                try {
                    object2 = clazz2.getDeclaredField(string);
                    ((Field)object2).setAccessible(true);
                    object2 = ((Field)object2).get(object);
                    if (!clazz.isInstance(object2)) {
                        return null;
                    }
                    return clazz.cast(object2);
                }
                catch (NoSuchFieldException noSuchFieldException) {
                    Intrinsics.checkNotNullExpressionValue(clazz2.getSuperclass(), "");
                }
            }
            if (Intrinsics.areEqual(string, "delegate") || (object2 = Util.readFieldOrNull(object, Object.class, "delegate")) == null) break;
            object = object2;
        }
        return null;
    }

    public static final <E> void addIfAbsent(@NotNull List<E> list2, E e2) {
        Intrinsics.checkNotNullParameter(list2, "");
        if (!list2.contains(e2)) {
            list2.add(e2);
        }
    }

    public static final void assertThreadHoldsLock(@NotNull Object object) {
        Intrinsics.checkNotNullParameter(object, "");
        if (assertionsEnabled && !Thread.holdsLock(object)) {
            throw new AssertionError((Object)("Thread " + Thread.currentThread().getName() + " MUST hold lock on " + object));
        }
    }

    public static final void assertThreadDoesntHoldLock(@NotNull Object object) {
        Intrinsics.checkNotNullParameter(object, "");
        if (assertionsEnabled && Thread.holdsLock(object)) {
            throw new AssertionError((Object)("Thread " + Thread.currentThread().getName() + " MUST NOT hold lock on " + object));
        }
    }

    @NotNull
    public static final Throwable withSuppressed(@NotNull Exception exception, @NotNull List<? extends Exception> object) {
        Intrinsics.checkNotNullParameter(exception, "");
        Intrinsics.checkNotNullParameter(object, "");
        Exception exception2 = exception;
        object = object.iterator();
        while (object.hasNext()) {
            Exception exception3 = (Exception)object.next();
            ExceptionsKt.addSuppressed(exception2, exception3);
        }
        return exception;
    }

    @NotNull
    public static final <T> List<T> filterList(@NotNull Iterable<? extends T> object, @NotNull Function1<? super T, Boolean> function1) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(function1, "");
        List list2 = CollectionsKt.emptyList();
        object = object.iterator();
        while (object.hasNext()) {
            Object e2 = object.next();
            if (!function1.invoke(e2).booleanValue()) continue;
            if (list2.isEmpty()) {
                list2 = new ArrayList();
            }
            List list3 = list2;
            Intrinsics.checkNotNull(list3);
            TypeIntrinsics.asMutableList(list3).add(e2);
        }
        return list2;
    }

    private static final Thread threadFactory$lambda$1(String object, boolean bl, Runnable runnable2) {
        Intrinsics.checkNotNullParameter(object, "");
        object = new Thread(runnable2, (String)object);
        runnable2 = object;
        ((Thread)object).setDaemon(bl);
        return object;
    }

    private static final EventListener asFactory$lambda$8(EventListener eventListener, Call call) {
        Intrinsics.checkNotNullParameter(eventListener, "");
        Intrinsics.checkNotNullParameter(call, "");
        return eventListener;
    }

    static {
        ByteString[] byteStringArray = new ByteString[5];
        ByteString[] byteStringArray2 = byteStringArray;
        byteStringArray[0] = ByteString.Companion.decodeHex("efbbbf");
        byteStringArray2[1] = ByteString.Companion.decodeHex("feff");
        byteStringArray2[2] = ByteString.Companion.decodeHex("fffe");
        byteStringArray2[3] = ByteString.Companion.decodeHex("0000ffff");
        byteStringArray2[4] = ByteString.Companion.decodeHex("ffff0000");
        UNICODE_BOMS = Options.Companion.of(byteStringArray2);
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Intrinsics.checkNotNull(timeZone);
        UTC = timeZone;
        VERIFY_AS_IP_ADDRESS = new Regex("([0-9a-fA-F]*:[0-9a-fA-F:.]*)|([\\d.]+)");
        assertionsEnabled = OkHttpClient.class.desiredAssertionStatus();
        String string = OkHttpClient.class.getName();
        Intrinsics.checkNotNullExpressionValue(string, "");
        okHttpName = StringsKt.removeSuffix(StringsKt.removePrefix(string, (CharSequence)"okhttp3."), (CharSequence)"Client");
    }
}


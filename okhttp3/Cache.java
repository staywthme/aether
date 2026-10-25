/*
 * Decompiled with CFR 0.152.
 */
package okhttp3;

import java.io.Closeable;
import java.io.File;
import java.io.Flushable;
import java.io.IOException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt;
import kotlin.io.CloseableKt;
import kotlin.jvm.JvmName;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.jvm.internal.StringCompanionObject;
import kotlin.text.StringsKt;
import okhttp3.CipherSuite;
import okhttp3.Handshake;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.TlsVersion;
import okhttp3.internal.Util;
import okhttp3.internal.cache.CacheRequest;
import okhttp3.internal.cache.CacheStrategy;
import okhttp3.internal.cache.DiskLruCache;
import okhttp3.internal.concurrent.TaskRunner;
import okhttp3.internal.http.HttpMethod;
import okhttp3.internal.http.StatusLine;
import okhttp3.internal.io.FileSystem;
import okhttp3.internal.platform.Platform;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;
import okio.ForwardingSink;
import okio.ForwardingSource;
import okio.Okio;
import okio.Sink;
import okio.Source;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000r\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\f\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010)\n\u0002\u0010\u000e\n\u0002\b\u0005\u0018\u0000 C2\u00020\u00012\u00020\u0002:\u0004BCDEB\u0017\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007B\u001f\b\u0000\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\u0016\u0010\u001f\u001a\u00020 2\f\u0010!\u001a\b\u0018\u00010\"R\u00020\fH\u0002J\b\u0010#\u001a\u00020 H\u0016J\u0006\u0010$\u001a\u00020 J\r\u0010\u0003\u001a\u00020\u0004H\u0007\u00a2\u0006\u0002\b%J\u0006\u0010&\u001a\u00020 J\b\u0010'\u001a\u00020 H\u0016J\u0017\u0010(\u001a\u0004\u0018\u00010)2\u0006\u0010*\u001a\u00020+H\u0000\u00a2\u0006\u0002\b,J\u0006\u0010\u0010\u001a\u00020\u0011J\u0006\u0010-\u001a\u00020 J\u0006\u0010\u0005\u001a\u00020\u0006J\u0006\u0010\u0015\u001a\u00020\u0011J\u0017\u0010.\u001a\u0004\u0018\u00010/2\u0006\u00100\u001a\u00020)H\u0000\u00a2\u0006\u0002\b1J\u0015\u00102\u001a\u00020 2\u0006\u0010*\u001a\u00020+H\u0000\u00a2\u0006\u0002\b3J\u0006\u0010\u0016\u001a\u00020\u0011J\u0006\u00104\u001a\u00020\u0006J\r\u00105\u001a\u00020 H\u0000\u00a2\u0006\u0002\b6J\u0015\u00107\u001a\u00020 2\u0006\u00108\u001a\u000209H\u0000\u00a2\u0006\u0002\b:J\u001d\u0010;\u001a\u00020 2\u0006\u0010<\u001a\u00020)2\u0006\u0010=\u001a\u00020)H\u0000\u00a2\u0006\u0002\b>J\f\u0010?\u001a\b\u0012\u0004\u0012\u00020A0@J\u0006\u0010\u0017\u001a\u00020\u0011J\u0006\u0010\u001c\u001a\u00020\u0011R\u0014\u0010\u000b\u001a\u00020\fX\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0003\u001a\u00020\u00048G\u00a2\u0006\u0006\u001a\u0004\b\u0003\u0010\u000fR\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0012\u001a\u00020\u00138F\u00a2\u0006\u0006\u001a\u0004\b\u0012\u0010\u0014R\u000e\u0010\u0015\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0017\u001a\u00020\u0011X\u0080\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0019\"\u0004\b\u001a\u0010\u001bR\u001a\u0010\u001c\u001a\u00020\u0011X\u0080\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u0019\"\u0004\b\u001e\u0010\u001b\u00a8\u0006F"}, d2={"Lokhttp3/Cache;", "Ljava/io/Closeable;", "Ljava/io/Flushable;", "directory", "Ljava/io/File;", "maxSize", "", "(Ljava/io/File;J)V", "fileSystem", "Lokhttp3/internal/io/FileSystem;", "(Ljava/io/File;JLokhttp3/internal/io/FileSystem;)V", "cache", "Lokhttp3/internal/cache/DiskLruCache;", "getCache$okhttp", "()Lokhttp3/internal/cache/DiskLruCache;", "()Ljava/io/File;", "hitCount", "", "isClosed", "", "()Z", "networkCount", "requestCount", "writeAbortCount", "getWriteAbortCount$okhttp", "()I", "setWriteAbortCount$okhttp", "(I)V", "writeSuccessCount", "getWriteSuccessCount$okhttp", "setWriteSuccessCount$okhttp", "abortQuietly", "", "editor", "Lokhttp3/internal/cache/DiskLruCache$Editor;", "close", "delete", "-deprecated_directory", "evictAll", "flush", "get", "Lokhttp3/Response;", "request", "Lokhttp3/Request;", "get$okhttp", "initialize", "put", "Lokhttp3/internal/cache/CacheRequest;", "response", "put$okhttp", "remove", "remove$okhttp", "size", "trackConditionalCacheHit", "trackConditionalCacheHit$okhttp", "trackResponse", "cacheStrategy", "Lokhttp3/internal/cache/CacheStrategy;", "trackResponse$okhttp", "update", "cached", "network", "update$okhttp", "urls", "", "", "CacheResponseBody", "Companion", "Entry", "RealCacheRequest", "okhttp"})
public final class Cache
implements Closeable,
Flushable {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final DiskLruCache cache;
    private int writeSuccessCount;
    private int writeAbortCount;
    private int networkCount;
    private int hitCount;
    private int requestCount;
    private static final int VERSION = 201105;
    private static final int ENTRY_METADATA = 0;
    private static final int ENTRY_BODY = 1;
    private static final int ENTRY_COUNT = 2;

    public Cache(@NotNull File file, long l2, @NotNull FileSystem fileSystem) {
        Intrinsics.checkNotNullParameter(file, "");
        Intrinsics.checkNotNullParameter(fileSystem, "");
        this.cache = new DiskLruCache(fileSystem, file, 201105, 2, l2, TaskRunner.INSTANCE);
    }

    @NotNull
    public final DiskLruCache getCache$okhttp() {
        return this.cache;
    }

    public final int getWriteSuccessCount$okhttp() {
        return this.writeSuccessCount;
    }

    public final void setWriteSuccessCount$okhttp(int n2) {
        this.writeSuccessCount = n2;
    }

    public final int getWriteAbortCount$okhttp() {
        return this.writeAbortCount;
    }

    public final void setWriteAbortCount$okhttp(int n2) {
        this.writeAbortCount = n2;
    }

    public final boolean isClosed() {
        return this.cache.isClosed();
    }

    public Cache(@NotNull File file, long l2) {
        Intrinsics.checkNotNullParameter(file, "");
        this(file, l2, FileSystem.SYSTEM);
    }

    @Nullable
    public final Response get$okhttp(@NotNull Request request) {
        Object object;
        Object object2;
        Intrinsics.checkNotNullParameter(request, "");
        Object object3 = Companion.key(request.url());
        try {
            DiskLruCache.Snapshot snapshot = this.cache.get((String)object3);
            if (snapshot == null) {
                return null;
            }
            object2 = snapshot;
        }
        catch (IOException iOException) {
            return null;
        }
        object3 = object2;
        try {
            object = new Entry(((DiskLruCache.Snapshot)object3).getSource(0));
        }
        catch (IOException iOException) {
            Util.closeQuietly((Closeable)object3);
            return null;
        }
        object2 = object;
        object = ((Entry)object2).response((DiskLruCache.Snapshot)object3);
        if (!((Entry)object2).matches(request, (Response)object)) {
            ResponseBody responseBody = ((Response)object).body();
            if (responseBody != null) {
                Util.closeQuietly(responseBody);
            }
            return null;
        }
        return object;
    }

    @Nullable
    public final CacheRequest put$okhttp(@NotNull Response response) {
        Intrinsics.checkNotNullParameter(response, "");
        Object object = response.request().method();
        if (HttpMethod.INSTANCE.invalidatesCache(response.request().method())) {
            try {
                this.remove$okhttp(response.request());
            }
            catch (IOException iOException) {}
            return null;
        }
        if (!Intrinsics.areEqual(object, "GET")) {
            return null;
        }
        if (Companion.hasVaryAll(response)) {
            return null;
        }
        object = new Entry(response);
        DiskLruCache.Editor editor = null;
        try {
            DiskLruCache.Editor editor2 = DiskLruCache.edit$default(this.cache, Companion.key(response.request().url()), 0L, 2, null);
            if (editor2 == null) {
                return null;
            }
            editor = editor2;
            ((Entry)object).writeTo(editor);
            return new RealCacheRequest(editor);
        }
        catch (IOException iOException) {
            this.abortQuietly(editor);
            return null;
        }
    }

    public final void remove$okhttp(@NotNull Request request) throws IOException {
        Intrinsics.checkNotNullParameter(request, "");
        this.cache.remove(Companion.key(request.url()));
    }

    public final void update$okhttp(@NotNull Response closeable, @NotNull Response object) {
        Intrinsics.checkNotNullParameter(closeable, "");
        Intrinsics.checkNotNullParameter(object, "");
        object = new Entry((Response)object);
        ResponseBody responseBody = ((Response)closeable).body();
        Intrinsics.checkNotNull(responseBody);
        closeable = ((CacheResponseBody)responseBody).getSnapshot();
        DiskLruCache.Editor editor = null;
        try {
            DiskLruCache.Editor editor2 = ((DiskLruCache.Snapshot)closeable).edit();
            if (editor2 == null) {
                return;
            }
            editor = editor2;
            ((Entry)object).writeTo(editor);
            editor.commit();
            return;
        }
        catch (IOException iOException) {
            this.abortQuietly(editor);
            return;
        }
    }

    private final void abortQuietly(DiskLruCache.Editor editor) {
        try {
            DiskLruCache.Editor editor2 = editor;
            if (editor2 != null) {
                editor2.abort();
                return;
            }
            return;
        }
        catch (IOException iOException) {
            return;
        }
    }

    public final void initialize() throws IOException {
        this.cache.initialize();
    }

    public final void delete() throws IOException {
        this.cache.delete();
    }

    public final void evictAll() throws IOException {
        this.cache.evictAll();
    }

    @NotNull
    public final Iterator<String> urls() throws IOException {
        return new Iterator<String>(this){
            @NotNull
            private final Iterator<DiskLruCache.Snapshot> delegate;
            @Nullable
            private String nextUrl;
            private boolean canRemove;
            {
                this.delegate = cache.getCache$okhttp().snapshots();
            }

            public final boolean hasNext() {
                if (this.nextUrl != null) {
                    return true;
                }
                this.canRemove = false;
                while (this.delegate.hasNext()) {
                    try {
                        Closeable closeable = this.delegate.next();
                        Throwable throwable = null;
                        try {
                            Closeable closeable2 = (DiskLruCache.Snapshot)closeable;
                            closeable2 = Okio.buffer(((DiskLruCache.Snapshot)closeable2).getSource(0));
                            this.nextUrl = closeable2.readUtf8LineStrict();
                        }
                        catch (Throwable throwable2) {
                            try {
                                Throwable throwable3 = throwable2;
                                throwable = throwable2;
                                throw throwable3;
                            }
                            catch (Throwable throwable4) {
                                CloseableKt.closeFinally(closeable, throwable);
                                throw throwable4;
                            }
                        }
                        CloseableKt.closeFinally(closeable, null);
                        return true;
                    }
                    catch (IOException iOException) {
                    }
                }
                return false;
            }

            @NotNull
            public final String next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                String string = this.nextUrl;
                Intrinsics.checkNotNull(string);
                String string2 = string;
                this.nextUrl = null;
                this.canRemove = true;
                return string2;
            }

            public final void remove() {
                if (!this.canRemove) {
                    String string = "remove() before next()";
                    throw new IllegalStateException(string.toString());
                }
                this.delegate.remove();
            }
        };
    }

    public final synchronized int writeAbortCount() {
        return this.writeAbortCount;
    }

    public final synchronized int writeSuccessCount() {
        return this.writeSuccessCount;
    }

    public final long size() throws IOException {
        return this.cache.size();
    }

    public final long maxSize() {
        return this.cache.getMaxSize();
    }

    @Override
    public final void flush() throws IOException {
        this.cache.flush();
    }

    @Override
    public final void close() throws IOException {
        this.cache.close();
    }

    @JvmName(name="directory")
    @NotNull
    public final File directory() {
        return this.cache.getDirectory();
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="directory", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_directory")
    @NotNull
    public final File -deprecated_directory() {
        return this.cache.getDirectory();
    }

    public final synchronized void trackResponse$okhttp(@NotNull CacheStrategy cacheStrategy) {
        Intrinsics.checkNotNullParameter(cacheStrategy, "");
        int n2 = this.requestCount;
        this.requestCount = n2 + 1;
        if (cacheStrategy.getNetworkRequest() != null) {
            n2 = this.networkCount;
            this.networkCount = n2 + 1;
            return;
        }
        if (cacheStrategy.getCacheResponse() != null) {
            n2 = this.hitCount;
            this.hitCount = n2 + 1;
        }
    }

    public final synchronized void trackConditionalCacheHit$okhttp() {
        int n2 = this.hitCount;
        this.hitCount = n2 + 1;
    }

    public final synchronized int networkCount() {
        return this.networkCount;
    }

    public final synchronized int hitCount() {
        return this.hitCount;
    }

    public final synchronized int requestCount() {
        return this.requestCount;
    }

    @JvmStatic
    @NotNull
    public static final String key(@NotNull HttpUrl httpUrl) {
        return Companion.key(httpUrl);
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0002\u0018\u00002\u00020\u0001B%\u0012\n\u0010\u0002\u001a\u00060\u0003R\u00020\u0004\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u0012\b\u0010\u0007\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\u0002\u0010\bJ\b\u0010\u0007\u001a\u00020\rH\u0016J\n\u0010\u0005\u001a\u0004\u0018\u00010\u000eH\u0016J\b\u0010\u000f\u001a\u00020\nH\u0016R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0015\u0010\u0002\u001a\u00060\u0003R\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f\u00a8\u0006\u0010"}, d2={"Lokhttp3/Cache$CacheResponseBody;", "Lokhttp3/ResponseBody;", "snapshot", "Lokhttp3/internal/cache/DiskLruCache$Snapshot;", "Lokhttp3/internal/cache/DiskLruCache;", "contentType", "", "contentLength", "(Lokhttp3/internal/cache/DiskLruCache$Snapshot;Ljava/lang/String;Ljava/lang/String;)V", "bodySource", "Lokio/BufferedSource;", "getSnapshot", "()Lokhttp3/internal/cache/DiskLruCache$Snapshot;", "", "Lokhttp3/MediaType;", "source", "okhttp"})
    static final class CacheResponseBody
    extends ResponseBody {
        @NotNull
        private final DiskLruCache.Snapshot snapshot;
        @Nullable
        private final String contentType;
        @Nullable
        private final String contentLength;
        @NotNull
        private final BufferedSource bodySource;

        public CacheResponseBody(@NotNull DiskLruCache.Snapshot closeable, @Nullable String string, @Nullable String string2) {
            Intrinsics.checkNotNullParameter(closeable, "");
            this.snapshot = closeable;
            this.contentType = string;
            this.contentLength = string2;
            closeable = this.snapshot.getSource(1);
            this.bodySource = Okio.buffer(new ForwardingSource((Source)closeable){

                @Override
                public final void close() throws IOException {
                    this.getSnapshot().close();
                    super.close();
                }
            });
        }

        @NotNull
        public final DiskLruCache.Snapshot getSnapshot() {
            return this.snapshot;
        }

        @Override
        @Nullable
        public final MediaType contentType() {
            String string = this.contentType;
            if (string != null) {
                return MediaType.Companion.parse(string);
            }
            return null;
        }

        @Override
        public final long contentLength() {
            String string = this.contentLength;
            if (string != null) {
                return Util.toLongOrDefault(string, -1L);
            }
            return -1L;
        }

        @Override
        @NotNull
        public final BufferedSource source() {
            return this.bodySource;
        }
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000N\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\"\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0007J\u0015\u0010\f\u001a\u00020\u00042\u0006\u0010\r\u001a\u00020\u000eH\u0000\u00a2\u0006\u0002\b\u000fJ\u0018\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u0013\u001a\u00020\u0011H\u0002J\u001e\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00112\u0006\u0010\u0019\u001a\u00020\u001aJ\n\u0010\u001b\u001a\u00020\u0015*\u00020\u0017J\u0012\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\t0\u001d*\u00020\u0011H\u0002J\n\u0010\u0010\u001a\u00020\u0011*\u00020\u0017R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001e"}, d2={"Lokhttp3/Cache$Companion;", "", "()V", "ENTRY_BODY", "", "ENTRY_COUNT", "ENTRY_METADATA", "VERSION", "key", "", "url", "Lokhttp3/HttpUrl;", "readInt", "source", "Lokio/BufferedSource;", "readInt$okhttp", "varyHeaders", "Lokhttp3/Headers;", "requestHeaders", "responseHeaders", "varyMatches", "", "cachedResponse", "Lokhttp3/Response;", "cachedRequest", "newRequest", "Lokhttp3/Request;", "hasVaryAll", "varyFields", "", "okhttp"})
    @SourceDebugExtension(value={"SMAP\nCache.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Cache.kt\nokhttp3/Cache$Companion\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,788:1\n2624#2,3:789\n*S KotlinDebug\n*F\n+ 1 Cache.kt\nokhttp3/Cache$Companion\n*L\n729#1:789,3\n*E\n"})
    public static final class Companion {
        private Companion() {
        }

        @JvmStatic
        @NotNull
        public final String key(@NotNull HttpUrl httpUrl) {
            Intrinsics.checkNotNullParameter(httpUrl, "");
            return ByteString.Companion.encodeUtf8(httpUrl.toString()).md5().hex();
        }

        public final int readInt$okhttp(@NotNull BufferedSource object) throws IOException {
            Intrinsics.checkNotNullParameter(object, "");
            try {
                long l2 = object.readDecimalLong();
                object = object.readUtf8LineStrict();
                if (l2 < 0L || l2 > Integer.MAX_VALUE || ((CharSequence)object).length() > 0) {
                    throw new IOException("expected an int but was \"" + l2 + (String)object + '\"');
                }
                return (int)l2;
            }
            catch (NumberFormatException numberFormatException) {
                throw new IOException(numberFormatException.getMessage());
            }
        }

        public final boolean varyMatches(@NotNull Response iterator2, @NotNull Headers headers, @NotNull Request request) {
            Intrinsics.checkNotNullParameter(iterator2, "");
            Intrinsics.checkNotNullParameter(headers, "");
            Intrinsics.checkNotNullParameter(request, "");
            iterator2 = this.varyFields(((Response)((Object)iterator2)).headers());
            if (!(iterator2 instanceof Collection) || !((Collection)((Object)iterator2)).isEmpty()) {
                iterator2 = iterator2.iterator();
                while (iterator2.hasNext()) {
                    Object object = iterator2.next();
                    if (!(!Intrinsics.areEqual(headers.values((String)(object = (String)object)), request.headers((String)object)))) continue;
                    return false;
                }
            }
            return true;
        }

        public final boolean hasVaryAll(@NotNull Response response) {
            Intrinsics.checkNotNullParameter(response, "");
            return this.varyFields(response.headers()).contains("*");
        }

        private final Set<String> varyFields(Headers headers) {
            Set set = null;
            int n2 = headers.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                if (!StringsKt.equals("Vary", headers.name(i2), true)) continue;
                Object object = headers.value(i2);
                if (set == null) {
                    set = new TreeSet<String>(StringsKt.getCASE_INSENSITIVE_ORDER(StringCompanionObject.INSTANCE));
                }
                char[] cArray = new char[1];
                Object object2 = cArray;
                cArray[0] = 44;
                object = StringsKt.split$default((CharSequence)object, object2, false, 0, 6, null).iterator();
                while (object.hasNext()) {
                    object2 = (String)object.next();
                    set.add(((Object)StringsKt.trim((CharSequence)object2)).toString());
                }
            }
            Set set2 = set;
            if (set2 == null) {
                set2 = SetsKt.emptySet();
            }
            return set2;
        }

        @NotNull
        public final Headers varyHeaders(@NotNull Response object) {
            Intrinsics.checkNotNullParameter(object, "");
            Response response = ((Response)object).networkResponse();
            Intrinsics.checkNotNull(response);
            Headers headers = response.request().headers();
            object = ((Response)object).headers();
            return this.varyHeaders(headers, (Headers)object);
        }

        private final Headers varyHeaders(Headers headers, Headers iterable) {
            if ((iterable = this.varyFields((Headers)iterable)).isEmpty()) {
                return Util.EMPTY_HEADERS;
            }
            Headers.Builder builder = new Headers.Builder();
            int n2 = headers.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                String string = headers.name(i2);
                if (!iterable.contains(string)) continue;
                builder.add(string, headers.value(i2));
            }
            return builder.build();
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\u0088\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0002\u0018\u0000 /2\u00020\u0001:\u0001/B\u000f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004B\u000f\b\u0016\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007J\u0016\u0010\u001c\u001a\u00020\r2\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u0005\u001a\u00020\u0006J\u0016\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020!0 2\u0006\u0010\"\u001a\u00020#H\u0002J\u0012\u0010\u0005\u001a\u00020\u00062\n\u0010$\u001a\u00060%R\u00020&J\u001e\u0010'\u001a\u00020(2\u0006\u0010)\u001a\u00020*2\f\u0010+\u001a\b\u0012\u0004\u0012\u00020!0 H\u0002J\u0012\u0010,\u001a\u00020(2\n\u0010-\u001a\u00060.R\u00020&R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\u00020\r8BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\f\u0010\u000eR\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0014X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u001aX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0017X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u00060"}, d2={"Lokhttp3/Cache$Entry;", "", "rawSource", "Lokio/Source;", "(Lokio/Source;)V", "response", "Lokhttp3/Response;", "(Lokhttp3/Response;)V", "code", "", "handshake", "Lokhttp3/Handshake;", "isHttps", "", "()Z", "message", "", "protocol", "Lokhttp3/Protocol;", "receivedResponseMillis", "", "requestMethod", "responseHeaders", "Lokhttp3/Headers;", "sentRequestMillis", "url", "Lokhttp3/HttpUrl;", "varyHeaders", "matches", "request", "Lokhttp3/Request;", "readCertificateList", "", "Ljava/security/cert/Certificate;", "source", "Lokio/BufferedSource;", "snapshot", "Lokhttp3/internal/cache/DiskLruCache$Snapshot;", "Lokhttp3/internal/cache/DiskLruCache;", "writeCertList", "", "sink", "Lokio/BufferedSink;", "certificates", "writeTo", "editor", "Lokhttp3/internal/cache/DiskLruCache$Editor;", "Companion", "okhttp"})
    static final class Entry {
        @NotNull
        public static final Companion Companion = new Companion(null);
        @NotNull
        private final HttpUrl url;
        @NotNull
        private final Headers varyHeaders;
        @NotNull
        private final String requestMethod;
        @NotNull
        private final Protocol protocol;
        private final int code;
        @NotNull
        private final String message;
        @NotNull
        private final Headers responseHeaders;
        @Nullable
        private final Handshake handshake;
        private final long sentRequestMillis;
        private final long receivedResponseMillis;
        @NotNull
        private static final String SENT_MILLIS = Platform.Companion.get().getPrefix() + "-Sent-Millis";
        @NotNull
        private static final String RECEIVED_MILLIS = Platform.Companion.get().getPrefix() + "-Received-Millis";

        private final boolean isHttps() {
            return Intrinsics.areEqual(this.url.scheme(), "https");
        }

        public Entry(@NotNull Source object) throws IOException {
            Closeable closeable;
            block9: {
                Intrinsics.checkNotNullParameter(object, "");
                closeable = (Closeable)object;
                Throwable throwable = null;
                try {
                    Source cfr_ignored_0 = (Source)closeable;
                    object = Okio.buffer((Source)object);
                    Object object2 = object.readUtf8LineStrict();
                    HttpUrl httpUrl = HttpUrl.Companion.parse((String)object2);
                    if (httpUrl == null) {
                        object2 = new IOException("Cache corruption for " + (String)object2);
                        IOException iOException = object2;
                        Platform.Companion.get().log("cache corruption", 5, iOException);
                        throw (Throwable)object2;
                    }
                    this.url = httpUrl;
                    this.requestMethod = object.readUtf8LineStrict();
                    Headers.Builder builder = new Headers.Builder();
                    int n2 = Companion.readInt$okhttp((BufferedSource)object);
                    for (int i2 = 0; i2 < n2; ++i2) {
                        builder.addLenient$okhttp(object.readUtf8LineStrict());
                    }
                    this.varyHeaders = builder.build();
                    StatusLine statusLine = StatusLine.Companion.parse(object.readUtf8LineStrict());
                    this.protocol = statusLine.protocol;
                    this.code = statusLine.code;
                    this.message = statusLine.message;
                    Object object3 = new Headers.Builder();
                    int n3 = Companion.readInt$okhttp((BufferedSource)object);
                    for (int i3 = 0; i3 < n3; ++i3) {
                        ((Headers.Builder)object3).addLenient$okhttp(object.readUtf8LineStrict());
                    }
                    Object object4 = ((Headers.Builder)object3).get(SENT_MILLIS);
                    Object object5 = ((Headers.Builder)object3).get(RECEIVED_MILLIS);
                    ((Headers.Builder)object3).removeAll(SENT_MILLIS);
                    ((Headers.Builder)object3).removeAll(RECEIVED_MILLIS);
                    String string = object4;
                    this.sentRequestMillis = string != null ? Long.parseLong(string) : 0L;
                    String string2 = object5;
                    this.receivedResponseMillis = string2 != null ? Long.parseLong(string2) : 0L;
                    this.responseHeaders = ((Headers.Builder)object3).build();
                    if (this.isHttps()) {
                        object3 = object.readUtf8LineStrict();
                        if (((CharSequence)object3).length() > 0) {
                            throw new IOException("expected \"\" but was \"" + (String)object3 + '\"');
                        }
                        object3 = object.readUtf8LineStrict();
                        object3 = CipherSuite.Companion.forJavaName((String)object3);
                        object5 = this.readCertificateList((BufferedSource)object);
                        object4 = this.readCertificateList((BufferedSource)object);
                        object = !object.exhausted() ? TlsVersion.Companion.forJavaName(object.readUtf8LineStrict()) : TlsVersion.SSL_3_0;
                        this.handshake = Handshake.Companion.get((TlsVersion)((Object)object), (CipherSuite)object3, (List<? extends Certificate>)object5, (List<? extends Certificate>)object4);
                        break block9;
                    }
                    this.handshake = null;
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
            }
            CloseableKt.closeFinally(closeable, null);
            return;
        }

        public Entry(@NotNull Response response) {
            Intrinsics.checkNotNullParameter(response, "");
            this.url = response.request().url();
            this.varyHeaders = Companion.varyHeaders(response);
            this.requestMethod = response.request().method();
            this.protocol = response.protocol();
            this.code = response.code();
            this.message = response.message();
            this.responseHeaders = response.headers();
            this.handshake = response.handshake();
            this.sentRequestMillis = response.sentRequestAtMillis();
            this.receivedResponseMillis = response.receivedResponseAtMillis();
        }

        public final void writeTo(@NotNull DiskLruCache.Editor object) throws IOException {
            block6: {
                Intrinsics.checkNotNullParameter(object, "");
                object = Okio.buffer(((DiskLruCache.Editor)object).newSink(0));
                Throwable throwable = null;
                try {
                    int n2;
                    BufferedSink bufferedSink = (BufferedSink)object;
                    bufferedSink.writeUtf8(this.url.toString()).writeByte(10);
                    bufferedSink.writeUtf8(this.requestMethod).writeByte(10);
                    bufferedSink.writeDecimalLong(this.varyHeaders.size()).writeByte(10);
                    int n3 = this.varyHeaders.size();
                    for (n2 = 0; n2 < n3; ++n2) {
                        bufferedSink.writeUtf8(this.varyHeaders.name(n2)).writeUtf8(": ").writeUtf8(this.varyHeaders.value(n2)).writeByte(10);
                    }
                    bufferedSink.writeUtf8(new StatusLine(this.protocol, this.code, this.message).toString()).writeByte(10);
                    bufferedSink.writeDecimalLong(this.responseHeaders.size() + 2).writeByte(10);
                    n3 = this.responseHeaders.size();
                    for (n2 = 0; n2 < n3; ++n2) {
                        bufferedSink.writeUtf8(this.responseHeaders.name(n2)).writeUtf8(": ").writeUtf8(this.responseHeaders.value(n2)).writeByte(10);
                    }
                    bufferedSink.writeUtf8(SENT_MILLIS).writeUtf8(": ").writeDecimalLong(this.sentRequestMillis).writeByte(10);
                    bufferedSink.writeUtf8(RECEIVED_MILLIS).writeUtf8(": ").writeDecimalLong(this.receivedResponseMillis).writeByte(10);
                    if (!this.isHttps()) break block6;
                    bufferedSink.writeByte(10);
                    Handshake handshake2 = this.handshake;
                    Intrinsics.checkNotNull(handshake2);
                    bufferedSink.writeUtf8(handshake2.cipherSuite().javaName()).writeByte(10);
                    this.writeCertList(bufferedSink, this.handshake.peerCertificates());
                    this.writeCertList(bufferedSink, this.handshake.localCertificates());
                    bufferedSink.writeUtf8(this.handshake.tlsVersion().javaName()).writeByte(10);
                }
                catch (Throwable throwable2) {
                    try {
                        Throwable throwable3 = throwable2;
                        throwable = throwable2;
                        throw throwable3;
                    }
                    catch (Throwable throwable4) {
                        CloseableKt.closeFinally((Closeable)object, throwable);
                        throw throwable4;
                    }
                }
            }
            CloseableKt.closeFinally((Closeable)object, null);
            return;
        }

        private final List<Certificate> readCertificateList(BufferedSource bufferedSource) throws IOException {
            int n2 = Companion.readInt$okhttp(bufferedSource);
            if (n2 == -1) {
                return CollectionsKt.emptyList();
            }
            try {
                CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                ArrayList<Certificate> arrayList = new ArrayList<Certificate>(n2);
                for (int i2 = 0; i2 < n2; ++i2) {
                    Object object = bufferedSource.readUtf8LineStrict();
                    Buffer buffer = new Buffer();
                    ByteString byteString = ByteString.Companion.decodeBase64((String)object);
                    if (byteString == null) {
                        throw new IOException("Corrupt certificate in cache entry");
                    }
                    object = byteString;
                    buffer.write((ByteString)object);
                    arrayList.add(certificateFactory.generateCertificate(buffer.inputStream()));
                }
                return arrayList;
            }
            catch (CertificateException certificateException) {
                throw new IOException(certificateException.getMessage());
            }
        }

        private final void writeCertList(BufferedSink bufferedSink, List<? extends Certificate> object) throws IOException {
            try {
                bufferedSink.writeDecimalLong(object.size()).writeByte(10);
                object = object.iterator();
                while (object.hasNext()) {
                    Object object2 = (Certificate)object.next();
                    object2 = ((Certificate)object2).getEncoded();
                    Intrinsics.checkNotNullExpressionValue(object2, "");
                    object2 = ByteString.Companion.of$default(ByteString.Companion, (byte[])object2, 0, 0, 3, null).base64();
                    bufferedSink.writeUtf8((String)object2).writeByte(10);
                }
            }
            catch (CertificateEncodingException certificateEncodingException) {
                throw new IOException(certificateEncodingException.getMessage());
            }
        }

        public final boolean matches(@NotNull Request request, @NotNull Response response) {
            Intrinsics.checkNotNullParameter(request, "");
            Intrinsics.checkNotNullParameter(response, "");
            return Intrinsics.areEqual(this.url, request.url()) && Intrinsics.areEqual(this.requestMethod, request.method()) && Companion.varyMatches(response, this.varyHeaders, request);
        }

        @NotNull
        public final Response response(@NotNull DiskLruCache.Snapshot snapshot) {
            Intrinsics.checkNotNullParameter(snapshot, "");
            String string = this.responseHeaders.get("Content-Type");
            String string2 = this.responseHeaders.get("Content-Length");
            Request request = new Request.Builder().url(this.url).method(this.requestMethod, null).headers(this.varyHeaders).build();
            return new Response.Builder().request(request).protocol(this.protocol).code(this.code).message(this.message).headers(this.responseHeaders).body(new CacheResponseBody(snapshot, string, string2)).handshake(this.handshake).sentRequestAtMillis(this.sentRequestMillis).receivedResponseAtMillis(this.receivedResponseMillis).build();
        }

        @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2={"Lokhttp3/Cache$Entry$Companion;", "", "()V", "RECEIVED_MILLIS", "", "SENT_MILLIS", "okhttp"})
        public static final class Companion {
            private Companion() {
            }

            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }
        }
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\b\u0082\u0004\u0018\u00002\u00020\u0001B\u0011\u0012\n\u0010\u0002\u001a\u00060\u0003R\u00020\u0004\u00a2\u0006\u0002\u0010\u0005J\b\u0010\u000f\u001a\u00020\u0010H\u0016J\b\u0010\u0006\u001a\u00020\u0007H\u0016R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\t\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u0012\u0010\u0002\u001a\u00060\u0003R\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2={"Lokhttp3/Cache$RealCacheRequest;", "Lokhttp3/internal/cache/CacheRequest;", "editor", "Lokhttp3/internal/cache/DiskLruCache$Editor;", "Lokhttp3/internal/cache/DiskLruCache;", "(Lokhttp3/Cache;Lokhttp3/internal/cache/DiskLruCache$Editor;)V", "body", "Lokio/Sink;", "cacheOut", "done", "", "getDone", "()Z", "setDone", "(Z)V", "abort", "", "okhttp"})
    final class RealCacheRequest
    implements CacheRequest {
        @NotNull
        private final DiskLruCache.Editor editor;
        @NotNull
        private final Sink cacheOut;
        @NotNull
        private final Sink body;
        private boolean done;

        public RealCacheRequest(DiskLruCache.Editor editor) {
            Intrinsics.checkNotNullParameter(editor, "");
            this.editor = editor;
            this.cacheOut = this.editor.newSink(1);
            Cache.this = this.cacheOut;
            this.body = new ForwardingSink((Sink)Cache.this){

                @Override
                public final void close() throws IOException {
                    Cache cache = Cache.this;
                    RealCacheRequest realCacheRequest = this;
                    Cache cache2 = Cache.this;
                    synchronized (cache) {
                        if (realCacheRequest.getDone()) {
                            return;
                        }
                        realCacheRequest.setDone(true);
                        int n2 = cache2.getWriteSuccessCount$okhttp();
                        cache2.setWriteSuccessCount$okhttp(n2 + 1);
                    }
                    super.close();
                    editor.commit();
                }
            };
        }

        public final boolean getDone() {
            return this.done;
        }

        public final void setDone(boolean bl) {
            this.done = bl;
        }

        @Override
        public final void abort() {
            Cache cache = Cache.this;
            Cache cache2 = Cache.this;
            synchronized (cache) {
                if (this.done) {
                    return;
                }
                this.done = true;
                int n2 = cache2.getWriteAbortCount$okhttp();
                cache2.setWriteAbortCount$okhttp(n2 + 1);
            }
            Util.closeQuietly(this.cacheOut);
            try {
                this.editor.abort();
                return;
            }
            catch (IOException iOException) {
                return;
            }
        }

        @Override
        @NotNull
        public final Sink body() {
            return this.body;
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package okhttp3;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.Unit;
import kotlin.io.CloseableKt;
import kotlin.jvm.JvmName;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.Charsets;
import okhttp3.MediaType;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.BufferedSource;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\b&\u0018\u0000 !2\u00020\u0001:\u0002 !B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0005\u001a\u00020\u0006J\u0006\u0010\u0007\u001a\u00020\bJ\u0006\u0010\t\u001a\u00020\nJ\u0006\u0010\u000b\u001a\u00020\u0004J\b\u0010\f\u001a\u00020\rH\u0002J\b\u0010\u000e\u001a\u00020\u000fH\u0016J@\u0010\u0010\u001a\u0002H\u0011\"\b\b\u0000\u0010\u0011*\u00020\u00122\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0015\u0012\u0004\u0012\u0002H\u00110\u00142\u0012\u0010\u0016\u001a\u000e\u0012\u0004\u0012\u0002H\u0011\u0012\u0004\u0012\u00020\u00170\u0014H\u0082\b\u00a2\u0006\u0002\u0010\u0018J\b\u0010\u0019\u001a\u00020\u001aH&J\n\u0010\u001b\u001a\u0004\u0018\u00010\u001cH&J\b\u0010\u001d\u001a\u00020\u0015H&J\u0006\u0010\u001e\u001a\u00020\u001fR\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\""}, d2={"Lokhttp3/ResponseBody;", "Ljava/io/Closeable;", "()V", "reader", "Ljava/io/Reader;", "byteStream", "Ljava/io/InputStream;", "byteString", "Lokio/ByteString;", "bytes", "", "charStream", "charset", "Ljava/nio/charset/Charset;", "close", "", "consumeSource", "T", "", "consumer", "Lkotlin/Function1;", "Lokio/BufferedSource;", "sizeMapper", "", "(Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "contentLength", "", "contentType", "Lokhttp3/MediaType;", "source", "string", "", "BomAwareReader", "Companion", "okhttp"})
@SourceDebugExtension(value={"SMAP\nResponseBody.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ResponseBody.kt\nokhttp3/ResponseBody\n*L\n1#1,321:1\n140#1,11:322\n140#1,11:333\n*S KotlinDebug\n*F\n+ 1 ResponseBody.kt\nokhttp3/ResponseBody\n*L\n124#1:322,11\n134#1:333,11\n*E\n"})
public abstract class ResponseBody
implements Closeable {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @Nullable
    private Reader reader;

    @Nullable
    public abstract MediaType contentType();

    public abstract long contentLength();

    @NotNull
    public final InputStream byteStream() {
        return this.source().inputStream();
    }

    @NotNull
    public abstract BufferedSource source();

    @NotNull
    public final byte[] bytes() throws IOException {
        Object object;
        Object object2 = this;
        long l2 = ((ResponseBody)object2).contentLength();
        if (l2 > Integer.MAX_VALUE) {
            throw new IOException("Cannot buffer entire body for content length: " + l2);
        }
        object2 = ((ResponseBody)object2).source();
        Object object3 = null;
        try {
            object = (BufferedSource)object2;
            object = object.readByteArray();
        }
        catch (Throwable throwable) {
            try {
                Throwable throwable2 = throwable;
                object3 = throwable;
                throw throwable2;
            }
            catch (Throwable throwable3) {
                CloseableKt.closeFinally((Closeable)object2, (Throwable)object3);
                throw throwable3;
            }
        }
        CloseableKt.closeFinally((Closeable)object2, null);
        object2 = object3 = object;
        int n2 = ((Object)object3).length;
        if (l2 != -1L && l2 != (long)n2) {
            throw new IOException("Content-Length (" + l2 + ") and stream length (" + n2 + ") disagree");
        }
        return object3;
    }

    @NotNull
    public final ByteString byteString() throws IOException {
        Object object;
        Object object2 = this;
        long l2 = ((ResponseBody)object2).contentLength();
        if (l2 > Integer.MAX_VALUE) {
            throw new IOException("Cannot buffer entire body for content length: " + l2);
        }
        object2 = ((ResponseBody)object2).source();
        Object object3 = null;
        try {
            object = (BufferedSource)object2;
            object = object.readByteString();
        }
        catch (Throwable throwable) {
            try {
                Throwable throwable2 = throwable;
                object3 = throwable;
                throw throwable2;
            }
            catch (Throwable throwable3) {
                CloseableKt.closeFinally((Closeable)object2, (Throwable)object3);
                throw throwable3;
            }
        }
        CloseableKt.closeFinally((Closeable)object2, null);
        object2 = object3 = object;
        int n2 = ((ByteString)object3).size();
        if (l2 != -1L && l2 != (long)n2) {
            throw new IOException("Content-Length (" + l2 + ") and stream length (" + n2 + ") disagree");
        }
        return object3;
    }

    private final <T> T consumeSource(Function1<? super BufferedSource, ? extends T> object, Function1<? super T, Integer> function1) {
        long l2 = this.contentLength();
        if (l2 > Integer.MAX_VALUE) {
            throw new IOException("Cannot buffer entire body for content length: " + l2);
        }
        Closeable closeable = this.source();
        Throwable throwable = null;
        try {
            object = object.invoke((BufferedSource)closeable);
        }
        catch (Throwable throwable2) {
            try {
                object = throwable2;
                throwable = throwable2;
                throw object;
            }
            catch (Throwable throwable3) {
                InlineMarker.finallyStart(1);
                CloseableKt.closeFinally(closeable, throwable);
                InlineMarker.finallyEnd(1);
                throw throwable3;
            }
        }
        InlineMarker.finallyStart(1);
        CloseableKt.closeFinally(closeable, null);
        InlineMarker.finallyEnd(1);
        int n2 = ((Number)function1.invoke(object)).intValue();
        if (l2 != -1L && l2 != (long)n2) {
            throw new IOException("Content-Length (" + l2 + ") and stream length (" + n2 + ") disagree");
        }
        return (T)object;
    }

    @NotNull
    public final Reader charStream() {
        Reader reader = this.reader;
        if (reader == null) {
            BomAwareReader bomAwareReader;
            BomAwareReader bomAwareReader2 = bomAwareReader = new BomAwareReader(this.source(), this.charset());
            this.reader = bomAwareReader2;
            reader = bomAwareReader;
        }
        return reader;
    }

    @NotNull
    public final String string() throws IOException {
        Object object;
        Closeable closeable = this.source();
        Throwable throwable = null;
        try {
            object = (BufferedSource)closeable;
            object = object.readString(Util.readBomAsCharset((BufferedSource)object, this.charset()));
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
        return object;
    }

    private final Charset charset() {
        Object object = this.contentType();
        if (object == null || (object = ((MediaType)object).charset(Charsets.UTF_8)) == null) {
            object = Charsets.UTF_8;
        }
        return object;
    }

    @Override
    public void close() {
        Util.closeQuietly(this.source());
    }

    @JvmStatic
    @JvmName(name="create")
    @NotNull
    public static final ResponseBody create(@NotNull String string, @Nullable MediaType mediaType) {
        return Companion.create(string, mediaType);
    }

    @JvmStatic
    @JvmName(name="create")
    @NotNull
    public static final ResponseBody create(@NotNull byte[] byArray, @Nullable MediaType mediaType) {
        return Companion.create(byArray, mediaType);
    }

    @JvmStatic
    @JvmName(name="create")
    @NotNull
    public static final ResponseBody create(@NotNull ByteString byteString, @Nullable MediaType mediaType) {
        return Companion.create(byteString, mediaType);
    }

    @JvmStatic
    @JvmName(name="create")
    @NotNull
    public static final ResponseBody create(@NotNull BufferedSource bufferedSource, @Nullable MediaType mediaType, long l2) {
        return Companion.create(bufferedSource, mediaType, l2);
    }

    @JvmStatic
    @Deprecated(message="Moved to extension function. Put the 'content' argument first to fix Java", replaceWith=@ReplaceWith(expression="content.toResponseBody(contentType)", imports={"okhttp3.ResponseBody.Companion.toResponseBody"}), level=DeprecationLevel.WARNING)
    @NotNull
    public static final ResponseBody create(@Nullable MediaType mediaType, @NotNull String string) {
        return Companion.create(mediaType, string);
    }

    @JvmStatic
    @Deprecated(message="Moved to extension function. Put the 'content' argument first to fix Java", replaceWith=@ReplaceWith(expression="content.toResponseBody(contentType)", imports={"okhttp3.ResponseBody.Companion.toResponseBody"}), level=DeprecationLevel.WARNING)
    @NotNull
    public static final ResponseBody create(@Nullable MediaType mediaType, @NotNull byte[] byArray) {
        return Companion.create(mediaType, byArray);
    }

    @JvmStatic
    @Deprecated(message="Moved to extension function. Put the 'content' argument first to fix Java", replaceWith=@ReplaceWith(expression="content.toResponseBody(contentType)", imports={"okhttp3.ResponseBody.Companion.toResponseBody"}), level=DeprecationLevel.WARNING)
    @NotNull
    public static final ResponseBody create(@Nullable MediaType mediaType, @NotNull ByteString byteString) {
        return Companion.create(mediaType, byteString);
    }

    @JvmStatic
    @Deprecated(message="Moved to extension function. Put the 'content' argument first to fix Java", replaceWith=@ReplaceWith(expression="content.asResponseBody(contentType, contentLength)", imports={"okhttp3.ResponseBody.Companion.asResponseBody"}), level=DeprecationLevel.WARNING)
    @NotNull
    public static final ResponseBody create(@Nullable MediaType mediaType, long l2, @NotNull BufferedSource bufferedSource) {
        return Companion.create(mediaType, l2, bufferedSource);
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0019\n\u0002\b\u0003\b\u0000\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\b\u0010\n\u001a\u00020\u000bH\u0016J \u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\rH\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\u0001X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2={"Lokhttp3/ResponseBody$BomAwareReader;", "Ljava/io/Reader;", "source", "Lokio/BufferedSource;", "charset", "Ljava/nio/charset/Charset;", "(Lokio/BufferedSource;Ljava/nio/charset/Charset;)V", "closed", "", "delegate", "close", "", "read", "", "cbuf", "", "off", "len", "okhttp"})
    @SourceDebugExtension(value={"SMAP\nResponseBody.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ResponseBody.kt\nokhttp3/ResponseBody$BomAwareReader\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,321:1\n1#2:322\n*E\n"})
    public static final class BomAwareReader
    extends Reader {
        @NotNull
        private final BufferedSource source;
        @NotNull
        private final Charset charset;
        private boolean closed;
        @Nullable
        private Reader delegate;

        public BomAwareReader(@NotNull BufferedSource bufferedSource, @NotNull Charset charset) {
            Intrinsics.checkNotNullParameter(bufferedSource, "");
            Intrinsics.checkNotNullParameter(charset, "");
            this.source = bufferedSource;
            this.charset = charset;
        }

        @Override
        public final int read(@NotNull char[] cArray, int n2, int n3) throws IOException {
            Reader reader;
            Intrinsics.checkNotNullParameter(cArray, "");
            if (this.closed) {
                throw new IOException("Stream closed");
            }
            Reader reader2 = this.delegate;
            if (reader2 == null) {
                reader = new InputStreamReader(this.source.inputStream(), Util.readBomAsCharset(this.source, this.charset));
                InputStreamReader inputStreamReader = reader;
                this.delegate = inputStreamReader;
                reader2 = reader;
            }
            reader = reader2;
            return reader2.read(cArray, n2, n3);
        }

        @Override
        public final void close() throws IOException {
            Unit unit;
            this.closed = true;
            Reader reader = this.delegate;
            if (reader != null) {
                reader.close();
                unit = Unit.INSTANCE;
            } else {
                unit = null;
            }
            if (unit == null) {
                BomAwareReader bomAwareReader = this;
                bomAwareReader.source.close();
            }
        }
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\t\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u001a\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0007J\"\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u00062\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u0007\u001a\u00020\u000bH\u0007J\u001a\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0007\u001a\u00020\fH\u0007J\u001a\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0007\u001a\u00020\rH\u0007J'\u0010\u000e\u001a\u00020\u0004*\u00020\u000b2\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00062\b\b\u0002\u0010\t\u001a\u00020\nH\u0007\u00a2\u0006\u0002\b\u0003J\u001d\u0010\u000f\u001a\u00020\u0004*\u00020\b2\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0007\u00a2\u0006\u0002\b\u0003J\u001d\u0010\u000f\u001a\u00020\u0004*\u00020\f2\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0007\u00a2\u0006\u0002\b\u0003J\u001d\u0010\u000f\u001a\u00020\u0004*\u00020\r2\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0007\u00a2\u0006\u0002\b\u0003\u00a8\u0006\u0010"}, d2={"Lokhttp3/ResponseBody$Companion;", "", "()V", "create", "Lokhttp3/ResponseBody;", "contentType", "Lokhttp3/MediaType;", "content", "", "contentLength", "", "Lokio/BufferedSource;", "", "Lokio/ByteString;", "asResponseBody", "toResponseBody", "okhttp"})
    public static final class Companion {
        private Companion() {
        }

        @JvmStatic
        @JvmName(name="create")
        @NotNull
        public final ResponseBody create(@NotNull String string, @Nullable MediaType mediaType) {
            Intrinsics.checkNotNullParameter(string, "");
            Object object = Charsets.UTF_8;
            MediaType mediaType2 = mediaType;
            if (mediaType != null && (object = MediaType.charset$default(mediaType, null, 1, null)) == null) {
                object = Charsets.UTF_8;
                mediaType2 = MediaType.Companion.parse(mediaType + "; charset=utf-8");
            }
            object = new Buffer().writeString(string, (Charset)object);
            return this.create((BufferedSource)object, mediaType2, ((Buffer)object).size());
        }

        public static /* synthetic */ ResponseBody create$default(Companion companion, String string, MediaType mediaType, int n2, Object object) {
            if ((n2 & 1) != 0) {
                mediaType = null;
            }
            return companion.create(string, mediaType);
        }

        @JvmStatic
        @JvmName(name="create")
        @NotNull
        public final ResponseBody create(@NotNull byte[] byArray, @Nullable MediaType mediaType) {
            Intrinsics.checkNotNullParameter(byArray, "");
            return this.create(new Buffer().write(byArray), mediaType, byArray.length);
        }

        public static /* synthetic */ ResponseBody create$default(Companion companion, byte[] byArray, MediaType mediaType, int n2, Object object) {
            if ((n2 & 1) != 0) {
                mediaType = null;
            }
            return companion.create(byArray, mediaType);
        }

        @JvmStatic
        @JvmName(name="create")
        @NotNull
        public final ResponseBody create(@NotNull ByteString byteString, @Nullable MediaType mediaType) {
            Intrinsics.checkNotNullParameter(byteString, "");
            return this.create(new Buffer().write(byteString), mediaType, byteString.size());
        }

        public static /* synthetic */ ResponseBody create$default(Companion companion, ByteString byteString, MediaType mediaType, int n2, Object object) {
            if ((n2 & 1) != 0) {
                mediaType = null;
            }
            return companion.create(byteString, mediaType);
        }

        @JvmStatic
        @JvmName(name="create")
        @NotNull
        public final ResponseBody create(@NotNull BufferedSource bufferedSource, @Nullable MediaType mediaType, long l2) {
            Intrinsics.checkNotNullParameter(bufferedSource, "");
            return new ResponseBody(mediaType, l2, bufferedSource){
                final /* synthetic */ MediaType $contentType;
                final /* synthetic */ long $contentLength;
                final /* synthetic */ BufferedSource $this_asResponseBody;
                {
                    this.$contentType = mediaType;
                    this.$contentLength = l2;
                    this.$this_asResponseBody = bufferedSource;
                }

                @Nullable
                public final MediaType contentType() {
                    return this.$contentType;
                }

                public final long contentLength() {
                    return this.$contentLength;
                }

                @NotNull
                public final BufferedSource source() {
                    return this.$this_asResponseBody;
                }
            };
        }

        public static /* synthetic */ ResponseBody create$default(Companion companion, BufferedSource bufferedSource, MediaType mediaType, long l2, int n2, Object object) {
            if ((n2 & 1) != 0) {
                mediaType = null;
            }
            if ((n2 & 2) != 0) {
                l2 = -1L;
            }
            return companion.create(bufferedSource, mediaType, l2);
        }

        @JvmStatic
        @Deprecated(message="Moved to extension function. Put the 'content' argument first to fix Java", replaceWith=@ReplaceWith(expression="content.toResponseBody(contentType)", imports={"okhttp3.ResponseBody.Companion.toResponseBody"}), level=DeprecationLevel.WARNING)
        @NotNull
        public final ResponseBody create(@Nullable MediaType mediaType, @NotNull String string) {
            Intrinsics.checkNotNullParameter(string, "");
            return this.create(string, mediaType);
        }

        @JvmStatic
        @Deprecated(message="Moved to extension function. Put the 'content' argument first to fix Java", replaceWith=@ReplaceWith(expression="content.toResponseBody(contentType)", imports={"okhttp3.ResponseBody.Companion.toResponseBody"}), level=DeprecationLevel.WARNING)
        @NotNull
        public final ResponseBody create(@Nullable MediaType mediaType, @NotNull byte[] byArray) {
            Intrinsics.checkNotNullParameter(byArray, "");
            return this.create(byArray, mediaType);
        }

        @JvmStatic
        @Deprecated(message="Moved to extension function. Put the 'content' argument first to fix Java", replaceWith=@ReplaceWith(expression="content.toResponseBody(contentType)", imports={"okhttp3.ResponseBody.Companion.toResponseBody"}), level=DeprecationLevel.WARNING)
        @NotNull
        public final ResponseBody create(@Nullable MediaType mediaType, @NotNull ByteString byteString) {
            Intrinsics.checkNotNullParameter(byteString, "");
            return this.create(byteString, mediaType);
        }

        @JvmStatic
        @Deprecated(message="Moved to extension function. Put the 'content' argument first to fix Java", replaceWith=@ReplaceWith(expression="content.asResponseBody(contentType, contentLength)", imports={"okhttp3.ResponseBody.Companion.asResponseBody"}), level=DeprecationLevel.WARNING)
        @NotNull
        public final ResponseBody create(@Nullable MediaType mediaType, long l2, @NotNull BufferedSource bufferedSource) {
            Intrinsics.checkNotNullParameter(bufferedSource, "");
            return this.create(bufferedSource, mediaType, l2);
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }
}


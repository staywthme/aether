/*
 * Decompiled with CFR 0.152.
 */
package okhttp3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.jvm.JvmField;
import kotlin.jvm.JvmName;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.BufferedSink;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\n\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\u0018\u0000 #2\u00020\u0001:\u0003\"#$B%\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\u00a2\u0006\u0002\u0010\tJ\r\u0010\n\u001a\u00020\u000bH\u0007\u00a2\u0006\u0002\b\u0015J\b\u0010\r\u001a\u00020\u000eH\u0016J\b\u0010\u000f\u001a\u00020\u0005H\u0016J\u000e\u0010\u0016\u001a\u00020\b2\u0006\u0010\u0017\u001a\u00020\u0012J\u0013\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007H\u0007\u00a2\u0006\u0002\b\u0018J\r\u0010\u0011\u001a\u00020\u0012H\u0007\u00a2\u0006\u0002\b\u0019J\r\u0010\u0004\u001a\u00020\u0005H\u0007\u00a2\u0006\u0002\b\u001aJ\u001a\u0010\u001b\u001a\u00020\u000e2\b\u0010\u001c\u001a\u0004\u0018\u00010\u001d2\u0006\u0010\u001e\u001a\u00020\u001fH\u0002J\u0010\u0010 \u001a\u00020!2\u0006\u0010\u001c\u001a\u00020\u001dH\u0016R\u0011\u0010\n\u001a\u00020\u000b8G\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u00078\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0010R\u0011\u0010\u0011\u001a\u00020\u00128G\u00a2\u0006\u0006\u001a\u0004\b\u0011\u0010\u0013R\u0013\u0010\u0004\u001a\u00020\u00058\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\u0014\u00a8\u0006%"}, d2={"Lokhttp3/MultipartBody;", "Lokhttp3/RequestBody;", "boundaryByteString", "Lokio/ByteString;", "type", "Lokhttp3/MediaType;", "parts", "", "Lokhttp3/MultipartBody$Part;", "(Lokio/ByteString;Lokhttp3/MediaType;Ljava/util/List;)V", "boundary", "", "()Ljava/lang/String;", "contentLength", "", "contentType", "()Ljava/util/List;", "size", "", "()I", "()Lokhttp3/MediaType;", "-deprecated_boundary", "part", "index", "-deprecated_parts", "-deprecated_size", "-deprecated_type", "writeOrCountBytes", "sink", "Lokio/BufferedSink;", "countBytes", "", "writeTo", "", "Builder", "Companion", "Part", "okhttp"})
public final class MultipartBody
extends RequestBody {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final ByteString boundaryByteString;
    @NotNull
    private final MediaType type;
    @NotNull
    private final List<Part> parts;
    @NotNull
    private final MediaType contentType;
    private long contentLength;
    @JvmField
    @NotNull
    public static final MediaType MIXED = MediaType.Companion.get("multipart/mixed");
    @JvmField
    @NotNull
    public static final MediaType ALTERNATIVE = MediaType.Companion.get("multipart/alternative");
    @JvmField
    @NotNull
    public static final MediaType DIGEST = MediaType.Companion.get("multipart/digest");
    @JvmField
    @NotNull
    public static final MediaType PARALLEL = MediaType.Companion.get("multipart/parallel");
    @JvmField
    @NotNull
    public static final MediaType FORM = MediaType.Companion.get("multipart/form-data");
    @NotNull
    private static final byte[] COLONSPACE;
    @NotNull
    private static final byte[] CRLF;
    @NotNull
    private static final byte[] DASHDASH;

    public MultipartBody(@NotNull ByteString byteString, @NotNull MediaType mediaType, @NotNull List<Part> list2) {
        Intrinsics.checkNotNullParameter(byteString, "");
        Intrinsics.checkNotNullParameter(mediaType, "");
        Intrinsics.checkNotNullParameter(list2, "");
        this.boundaryByteString = byteString;
        this.type = mediaType;
        this.parts = list2;
        this.contentType = MediaType.Companion.get(this.type + "; boundary=" + this.boundary());
        this.contentLength = -1L;
    }

    @JvmName(name="type")
    @NotNull
    public final MediaType type() {
        return this.type;
    }

    @JvmName(name="parts")
    @NotNull
    public final List<Part> parts() {
        return this.parts;
    }

    @JvmName(name="boundary")
    @NotNull
    public final String boundary() {
        return this.boundaryByteString.utf8();
    }

    @JvmName(name="size")
    public final int size() {
        return this.parts.size();
    }

    @NotNull
    public final Part part(int n2) {
        return this.parts.get(n2);
    }

    @Override
    @NotNull
    public final MediaType contentType() {
        return this.contentType;
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="type", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_type")
    @NotNull
    public final MediaType -deprecated_type() {
        return this.type;
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="boundary", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_boundary")
    @NotNull
    public final String -deprecated_boundary() {
        return this.boundary();
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="size", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_size")
    public final int -deprecated_size() {
        return this.size();
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="parts", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_parts")
    @NotNull
    public final List<Part> -deprecated_parts() {
        return this.parts;
    }

    @Override
    public final long contentLength() throws IOException {
        long l2 = this.contentLength;
        if (l2 == -1L) {
            this.contentLength = l2 = this.writeOrCountBytes(null, true);
        }
        return l2;
    }

    @Override
    public final void writeTo(@NotNull BufferedSink bufferedSink) throws IOException {
        Intrinsics.checkNotNullParameter(bufferedSink, "");
        this.writeOrCountBytes(bufferedSink, false);
    }

    private final long writeOrCountBytes(BufferedSink bufferedSink, boolean bl) throws IOException {
        long l2 = 0L;
        Buffer buffer = null;
        if (bl) {
            buffer = new Buffer();
            bufferedSink = buffer;
        }
        int n2 = this.parts.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            long l3;
            MediaType mediaType;
            Object object = this.parts.get(i2);
            Headers headers = ((Part)object).headers();
            object = ((Part)object).body();
            BufferedSink bufferedSink2 = bufferedSink;
            Intrinsics.checkNotNull(bufferedSink2);
            bufferedSink2.write(DASHDASH);
            bufferedSink.write(this.boundaryByteString);
            bufferedSink.write(CRLF);
            if (headers != null) {
                int n3 = headers.size();
                for (int i3 = 0; i3 < n3; ++i3) {
                    bufferedSink.writeUtf8(headers.name(i3)).write(COLONSPACE).writeUtf8(headers.value(i3)).write(CRLF);
                }
            }
            if ((mediaType = ((RequestBody)object).contentType()) != null) {
                bufferedSink.writeUtf8("Content-Type: ").writeUtf8(mediaType.toString()).write(CRLF);
            }
            if ((l3 = ((RequestBody)object).contentLength()) != -1L) {
                bufferedSink.writeUtf8("Content-Length: ").writeDecimalLong(l3).write(CRLF);
            } else if (bl) {
                Buffer buffer2 = buffer;
                Intrinsics.checkNotNull(buffer2);
                buffer2.clear();
                return -1L;
            }
            bufferedSink.write(CRLF);
            if (bl) {
                l2 += l3;
            } else {
                ((RequestBody)object).writeTo(bufferedSink);
            }
            bufferedSink.write(CRLF);
        }
        BufferedSink bufferedSink3 = bufferedSink;
        Intrinsics.checkNotNull(bufferedSink3);
        bufferedSink3.write(DASHDASH);
        bufferedSink.write(this.boundaryByteString);
        bufferedSink.write(DASHDASH);
        bufferedSink.write(CRLF);
        if (bl) {
            Buffer buffer3 = buffer;
            Intrinsics.checkNotNull(buffer3);
            l2 += buffer3.size();
            buffer.clear();
        }
        return l2;
    }

    static {
        byte[] byArray = new byte[2];
        byte[] byArray2 = byArray;
        byArray[0] = 58;
        byArray2[1] = 32;
        COLONSPACE = byArray2;
        byte[] byArray3 = new byte[2];
        byArray2 = byArray3;
        byArray3[0] = 13;
        byArray2[1] = 10;
        CRLF = byArray2;
        byte[] byArray4 = new byte[2];
        byArray2 = byArray4;
        byArray4[0] = 45;
        byArray2[1] = 45;
        DASHDASH = byArray2;
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0011\b\u0007\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u000b\u001a\u00020\u00002\u0006\u0010\f\u001a\u00020\u00032\u0006\u0010\r\u001a\u00020\u0003J \u0010\u000b\u001a\u00020\u00002\u0006\u0010\f\u001a\u00020\u00032\b\u0010\u000e\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u000f\u001a\u00020\u0010J\u0018\u0010\u0011\u001a\u00020\u00002\b\u0010\u0012\u001a\u0004\u0018\u00010\u00132\u0006\u0010\u000f\u001a\u00020\u0010J\u000e\u0010\u0011\u001a\u00020\u00002\u0006\u0010\u0014\u001a\u00020\bJ\u000e\u0010\u0011\u001a\u00020\u00002\u0006\u0010\u000f\u001a\u00020\u0010J\u0006\u0010\u0015\u001a\u00020\u0016J\u000e\u0010\u0017\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\nR\u000e\u0010\u0002\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2={"Lokhttp3/MultipartBody$Builder;", "", "boundary", "", "(Ljava/lang/String;)V", "Lokio/ByteString;", "parts", "", "Lokhttp3/MultipartBody$Part;", "type", "Lokhttp3/MediaType;", "addFormDataPart", "name", "value", "filename", "body", "Lokhttp3/RequestBody;", "addPart", "headers", "Lokhttp3/Headers;", "part", "build", "Lokhttp3/MultipartBody;", "setType", "okhttp"})
    @SourceDebugExtension(value={"SMAP\nMultipartBody.kt\nKotlin\n*S Kotlin\n*F\n+ 1 MultipartBody.kt\nokhttp3/MultipartBody$Builder\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,345:1\n1#2:346\n*E\n"})
    public static final class Builder {
        @NotNull
        private final ByteString boundary;
        @NotNull
        private MediaType type;
        @NotNull
        private final List<Part> parts;

        @JvmOverloads
        public Builder(@NotNull String string) {
            Intrinsics.checkNotNullParameter(string, "");
            this.boundary = ByteString.Companion.encodeUtf8(string);
            this.type = MIXED;
            this.parts = new ArrayList();
        }

        public /* synthetic */ Builder(String string, int n2, DefaultConstructorMarker defaultConstructorMarker) {
            if ((n2 & 1) != 0) {
                String string2 = UUID.randomUUID().toString();
                Intrinsics.checkNotNullExpressionValue(string2, "");
                string = string2;
            }
            this(string);
        }

        @NotNull
        public final Builder setType(@NotNull MediaType object) {
            Builder builder;
            Intrinsics.checkNotNullParameter(object, "");
            Builder builder2 = builder = this;
            if (!Intrinsics.areEqual(((MediaType)object).type(), "multipart")) {
                object = "multipart != " + object;
                throw new IllegalArgumentException(object.toString());
            }
            builder2.type = object;
            return builder;
        }

        @NotNull
        public final Builder addPart(@NotNull RequestBody requestBody) {
            Builder builder;
            Intrinsics.checkNotNullParameter(requestBody, "");
            Builder builder2 = builder = this;
            builder.addPart(Part.Companion.create(requestBody));
            return builder;
        }

        @NotNull
        public final Builder addPart(@Nullable Headers headers, @NotNull RequestBody requestBody) {
            Builder builder;
            Intrinsics.checkNotNullParameter(requestBody, "");
            Builder builder2 = builder = this;
            builder.addPart(Part.Companion.create(headers, requestBody));
            return builder;
        }

        @NotNull
        public final Builder addFormDataPart(@NotNull String string, @NotNull String string2) {
            Builder builder;
            Intrinsics.checkNotNullParameter(string, "");
            Intrinsics.checkNotNullParameter(string2, "");
            Builder builder2 = builder = this;
            builder.addPart(Part.Companion.createFormData(string, string2));
            return builder;
        }

        @NotNull
        public final Builder addFormDataPart(@NotNull String string, @Nullable String string2, @NotNull RequestBody requestBody) {
            Builder builder;
            Intrinsics.checkNotNullParameter(string, "");
            Intrinsics.checkNotNullParameter(requestBody, "");
            Builder builder2 = builder = this;
            builder.addPart(Part.Companion.createFormData(string, string2, requestBody));
            return builder;
        }

        @NotNull
        public final Builder addPart(@NotNull Part part) {
            Builder builder;
            Intrinsics.checkNotNullParameter(part, "");
            Builder builder2 = builder = this;
            ((Collection)builder.parts).add(part);
            return builder;
        }

        @NotNull
        public final MultipartBody build() {
            if (!(!((Collection)this.parts).isEmpty())) {
                String string = "Multipart body must have at least one part.";
                throw new IllegalStateException(string.toString());
            }
            return new MultipartBody(this.boundary, this.type, Util.toImmutableList(this.parts));
        }

        @JvmOverloads
        public Builder() {
            this(null, 1, null);
        }
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u001d\u0010\r\u001a\u00020\u000e*\u00060\u000fj\u0002`\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0000\u00a2\u0006\u0002\b\u0013R\u0010\u0010\u0003\u001a\u00020\u00048\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u00020\u00048\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u00020\u00048\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u00020\u00048\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u00020\u00048\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2={"Lokhttp3/MultipartBody$Companion;", "", "()V", "ALTERNATIVE", "Lokhttp3/MediaType;", "COLONSPACE", "", "CRLF", "DASHDASH", "DIGEST", "FORM", "MIXED", "PARALLEL", "appendQuotedString", "", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "key", "", "appendQuotedString$okhttp", "okhttp"})
    public static final class Companion {
        private Companion() {
        }

        public final void appendQuotedString$okhttp(@NotNull StringBuilder stringBuilder, @NotNull String string) {
            Intrinsics.checkNotNullParameter(stringBuilder, "");
            Intrinsics.checkNotNullParameter(string, "");
            stringBuilder.append('\"');
            int n2 = string.length();
            for (int i2 = 0; i2 < n2; ++i2) {
                char c2 = string.charAt(i2);
                if (c2 == '\n') {
                    stringBuilder.append("%0A");
                    continue;
                }
                if (c2 == '\r') {
                    stringBuilder.append("%0D");
                    continue;
                }
                if (c2 == '\"') {
                    stringBuilder.append("%22");
                    continue;
                }
                stringBuilder.append(c2);
            }
            stringBuilder.append('\"');
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    /*
     * Illegal identifiers - consider using --renameillegalidents true
     */
    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u0000 \u000b2\u00020\u0001:\u0001\u000bB\u0019\b\u0002\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\r\u0010\u0004\u001a\u00020\u0005H\u0007\u00a2\u0006\u0002\b\tJ\u000f\u0010\u0002\u001a\u0004\u0018\u00010\u0003H\u0007\u00a2\u0006\u0002\b\nR\u0013\u0010\u0004\u001a\u00020\u00058\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\u0007R\u0015\u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\b\u00a8\u0006\f"}, d2={"Lokhttp3/MultipartBody$Part;", "", "headers", "Lokhttp3/Headers;", "body", "Lokhttp3/RequestBody;", "(Lokhttp3/Headers;Lokhttp3/RequestBody;)V", "()Lokhttp3/RequestBody;", "()Lokhttp3/Headers;", "-deprecated_body", "-deprecated_headers", "Companion", "okhttp"})
    public static final class Part {
        @NotNull
        public static final Companion Companion = new Companion(null);
        @Nullable
        private final Headers headers;
        @NotNull
        private final RequestBody body;

        private Part(Headers headers, RequestBody requestBody) {
            this.headers = headers;
            this.body = requestBody;
        }

        @JvmName(name="headers")
        @Nullable
        public final Headers headers() {
            return this.headers;
        }

        @JvmName(name="body")
        @NotNull
        public final RequestBody body() {
            return this.body;
        }

        @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="headers", imports={}), level=DeprecationLevel.ERROR)
        @JvmName(name="-deprecated_headers")
        @Nullable
        public final Headers -deprecated_headers() {
            return this.headers;
        }

        @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="body", imports={}), level=DeprecationLevel.ERROR)
        @JvmName(name="-deprecated_body")
        @NotNull
        public final RequestBody -deprecated_body() {
            return this.body;
        }

        @JvmStatic
        @NotNull
        public static final Part create(@NotNull RequestBody requestBody) {
            return Companion.create(requestBody);
        }

        @JvmStatic
        @NotNull
        public static final Part create(@Nullable Headers headers, @NotNull RequestBody requestBody) {
            return Companion.create(headers, requestBody);
        }

        @JvmStatic
        @NotNull
        public static final Part createFormData(@NotNull String string, @NotNull String string2) {
            return Companion.createFormData(string, string2);
        }

        @JvmStatic
        @NotNull
        public static final Part createFormData(@NotNull String string, @Nullable String string2, @NotNull RequestBody requestBody) {
            return Companion.createFormData(string, string2, requestBody);
        }

        public /* synthetic */ Part(Headers headers, RequestBody requestBody, DefaultConstructorMarker defaultConstructorMarker) {
            this(headers, requestBody);
        }

        @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u001a\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0007J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\bH\u0007J\u0018\u0010\t\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bH\u0007J\"\u0010\t\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u000b2\b\u0010\r\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\u0007\u001a\u00020\bH\u0007\u00a8\u0006\u000e"}, d2={"Lokhttp3/MultipartBody$Part$Companion;", "", "()V", "create", "Lokhttp3/MultipartBody$Part;", "headers", "Lokhttp3/Headers;", "body", "Lokhttp3/RequestBody;", "createFormData", "name", "", "value", "filename", "okhttp"})
        @SourceDebugExtension(value={"SMAP\nMultipartBody.kt\nKotlin\n*S Kotlin\n*F\n+ 1 MultipartBody.kt\nokhttp3/MultipartBody$Part$Companion\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,345:1\n1#2:346\n*E\n"})
        public static final class Companion {
            private Companion() {
            }

            @JvmStatic
            @NotNull
            public final Part create(@NotNull RequestBody requestBody) {
                Intrinsics.checkNotNullParameter(requestBody, "");
                return this.create(null, requestBody);
            }

            @JvmStatic
            @NotNull
            public final Part create(@Nullable Headers object, @NotNull RequestBody requestBody) {
                Intrinsics.checkNotNullParameter(requestBody, "");
                Headers headers = object;
                if (!((headers != null ? headers.get("Content-Type") : null) == null)) {
                    object = "Unexpected header: Content-Type";
                    throw new IllegalArgumentException(object.toString());
                }
                Headers headers2 = object;
                if (!((headers2 != null ? headers2.get("Content-Length") : null) == null)) {
                    object = "Unexpected header: Content-Length";
                    throw new IllegalArgumentException(object.toString());
                }
                return new Part((Headers)object, requestBody, null);
            }

            @JvmStatic
            @NotNull
            public final Part createFormData(@NotNull String string, @NotNull String string2) {
                Intrinsics.checkNotNullParameter(string, "");
                Intrinsics.checkNotNullParameter(string2, "");
                return this.createFormData(string, null, RequestBody.Companion.create$default(RequestBody.Companion, string2, null, 1, null));
            }

            @JvmStatic
            @NotNull
            public final Part createFormData(@NotNull String string, @Nullable String string2, @NotNull RequestBody requestBody) {
                Intrinsics.checkNotNullParameter(string, "");
                Intrinsics.checkNotNullParameter(requestBody, "");
                Object object = new StringBuilder();
                StringBuilder stringBuilder = object;
                ((StringBuilder)object).append("form-data; name=");
                MultipartBody.Companion.appendQuotedString$okhttp(stringBuilder, string);
                if (string2 != null) {
                    stringBuilder.append("; filename=");
                    MultipartBody.Companion.appendQuotedString$okhttp(stringBuilder, string2);
                }
                String string3 = ((StringBuilder)object).toString();
                Intrinsics.checkNotNullExpressionValue(string3, "");
                string = string3;
                object = new Headers.Builder().addUnsafeNonAscii("Content-Disposition", string).build();
                return this.create((Headers)object, requestBody);
            }

            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }
        }
    }
}


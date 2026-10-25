/*
 * Decompiled with CFR 0.152.
 */
package okio;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.jvm.JvmName;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import okio.-SegmentedByteString;
import okio.Buffer;
import okio.ByteString;
import okio.ForwardingSink;
import okio.Segment;
import okio.Sink;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\u0018\u0000 \u001a2\u00020\u00012\u00020\u0002:\u0001\u001aB\u0017\b\u0010\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006B\u0017\b\u0010\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\u0006\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tB\u0017\b\u0010\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fB\u001f\b\u0010\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\u0006\u0010\r\u001a\u00020\u000e\u0012\u0006\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\u000fJ\r\u0010\u0010\u001a\u00020\u000eH\u0007\u00a2\u0006\u0002\b\u0013J\u0018\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0019H\u0016R\u0011\u0010\u0010\u001a\u00020\u000e8G\u00a2\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001b"}, d2={"Lokio/HashingSink;", "Lokio/ForwardingSink;", "Lokio/Sink;", "sink", "digest", "Ljava/security/MessageDigest;", "(Lokio/Sink;Ljava/security/MessageDigest;)V", "algorithm", "", "(Lokio/Sink;Ljava/lang/String;)V", "mac", "Ljavax/crypto/Mac;", "(Lokio/Sink;Ljavax/crypto/Mac;)V", "key", "Lokio/ByteString;", "(Lokio/Sink;Lokio/ByteString;Ljava/lang/String;)V", "hash", "()Lokio/ByteString;", "messageDigest", "-deprecated_hash", "write", "", "source", "Lokio/Buffer;", "byteCount", "", "Companion", "okio"})
@SourceDebugExtension(value={"SMAP\nHashingSink.kt\nKotlin\n*S Kotlin\n*F\n+ 1 HashingSink.kt\nokio/HashingSink\n+ 2 Util.kt\nokio/-SegmentedByteString\n*L\n1#1,148:1\n86#2:149\n*S KotlinDebug\n*F\n+ 1 HashingSink.kt\nokio/HashingSink\n*L\n75#1:149\n*E\n"})
public final class HashingSink
extends ForwardingSink
implements Sink {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @Nullable
    private final MessageDigest messageDigest;
    @Nullable
    private final Mac mac;

    public HashingSink(@NotNull Sink sink2, @NotNull MessageDigest messageDigest) {
        Intrinsics.checkNotNullParameter(sink2, "");
        Intrinsics.checkNotNullParameter(messageDigest, "");
        super(sink2);
        this.messageDigest = messageDigest;
        this.mac = null;
    }

    public HashingSink(@NotNull Sink sink2, @NotNull String string) {
        Intrinsics.checkNotNullParameter(sink2, "");
        Intrinsics.checkNotNullParameter(string, "");
        MessageDigest messageDigest = MessageDigest.getInstance(string);
        Intrinsics.checkNotNullExpressionValue(messageDigest, "");
        this(sink2, messageDigest);
    }

    public HashingSink(@NotNull Sink sink2, @NotNull Mac mac) {
        Intrinsics.checkNotNullParameter(sink2, "");
        Intrinsics.checkNotNullParameter(mac, "");
        super(sink2);
        this.mac = mac;
        this.messageDigest = null;
    }

    public HashingSink(@NotNull Sink object, @NotNull ByteString byteString, @NotNull String string) {
        Sink sink2;
        Sink sink3;
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(byteString, "");
        Intrinsics.checkNotNullParameter(string, "");
        Object object2 = object;
        object = this;
        try {
            Sink sink4 = object;
            Sink sink5 = object2;
            object2 = object = Mac.getInstance(string);
            Sink sink6 = sink5;
            Sink sink7 = sink4;
            ((Mac)object2).init(new SecretKeySpec(byteString.toByteArray(), string));
            sink3 = sink7;
            sink2 = sink6;
        }
        catch (InvalidKeyException invalidKeyException) {
            throw new IllegalArgumentException(invalidKeyException);
        }
        Intrinsics.checkNotNull(object);
        super(sink2, (Mac)object);
    }

    @Override
    public final void write(@NotNull Buffer buffer, long l2) throws IOException {
        int n2;
        Intrinsics.checkNotNullParameter(buffer, "");
        -SegmentedByteString.checkOffsetAndCount(buffer.size(), 0L, l2);
        Segment segment = buffer.head;
        Intrinsics.checkNotNull(segment);
        Segment segment2 = segment;
        for (long i2 = 0L; i2 < l2; i2 += (long)n2) {
            long l3 = l2 - i2;
            n2 = segment2.limit - segment2.pos;
            n2 = (int)Math.min(l3, (long)n2);
            if (this.messageDigest != null) {
                this.messageDigest.update(segment2.data, segment2.pos, n2);
            } else {
                Mac mac = this.mac;
                Intrinsics.checkNotNull(mac);
                mac.update(segment2.data, segment2.pos, n2);
            }
            Intrinsics.checkNotNull(segment2.next);
        }
        super.write(buffer, l2);
    }

    @JvmName(name="hash")
    @NotNull
    public final ByteString hash() {
        byte[] byArray;
        if (this.messageDigest != null) {
            byArray = this.messageDigest.digest();
        } else {
            Mac mac = this.mac;
            Intrinsics.checkNotNull(mac);
            byArray = mac.doFinal();
        }
        byte[] byArray2 = byArray;
        Intrinsics.checkNotNull(byArray2);
        return new ByteString(byArray2);
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="hash", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_hash")
    @NotNull
    public final ByteString -deprecated_hash() {
        return this.hash();
    }

    @JvmStatic
    @NotNull
    public static final HashingSink md5(@NotNull Sink sink2) {
        return Companion.md5(sink2);
    }

    @JvmStatic
    @NotNull
    public static final HashingSink sha1(@NotNull Sink sink2) {
        return Companion.sha1(sink2);
    }

    @JvmStatic
    @NotNull
    public static final HashingSink sha256(@NotNull Sink sink2) {
        return Companion.sha256(sink2);
    }

    @JvmStatic
    @NotNull
    public static final HashingSink sha512(@NotNull Sink sink2) {
        return Companion.sha512(sink2);
    }

    @JvmStatic
    @NotNull
    public static final HashingSink hmacSha1(@NotNull Sink sink2, @NotNull ByteString byteString) {
        return Companion.hmacSha1(sink2, byteString);
    }

    @JvmStatic
    @NotNull
    public static final HashingSink hmacSha256(@NotNull Sink sink2, @NotNull ByteString byteString) {
        return Companion.hmacSha256(sink2, byteString);
    }

    @JvmStatic
    @NotNull
    public static final HashingSink hmacSha512(@NotNull Sink sink2, @NotNull ByteString byteString) {
        return Companion.hmacSha512(sink2, byteString);
    }

    @Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0007J\u0018\u0010\t\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0007J\u0018\u0010\n\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0007J\u0010\u0010\u000b\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\f\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\r\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u000e\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007\u00a8\u0006\u000f"}, d2={"Lokio/HashingSink$Companion;", "", "()V", "hmacSha1", "Lokio/HashingSink;", "sink", "Lokio/Sink;", "key", "Lokio/ByteString;", "hmacSha256", "hmacSha512", "md5", "sha1", "sha256", "sha512", "okio"})
    public static final class Companion {
        private Companion() {
        }

        @JvmStatic
        @NotNull
        public final HashingSink md5(@NotNull Sink sink2) {
            Intrinsics.checkNotNullParameter(sink2, "");
            return new HashingSink(sink2, "MD5");
        }

        @JvmStatic
        @NotNull
        public final HashingSink sha1(@NotNull Sink sink2) {
            Intrinsics.checkNotNullParameter(sink2, "");
            return new HashingSink(sink2, "SHA-1");
        }

        @JvmStatic
        @NotNull
        public final HashingSink sha256(@NotNull Sink sink2) {
            Intrinsics.checkNotNullParameter(sink2, "");
            return new HashingSink(sink2, "SHA-256");
        }

        @JvmStatic
        @NotNull
        public final HashingSink sha512(@NotNull Sink sink2) {
            Intrinsics.checkNotNullParameter(sink2, "");
            return new HashingSink(sink2, "SHA-512");
        }

        @JvmStatic
        @NotNull
        public final HashingSink hmacSha1(@NotNull Sink sink2, @NotNull ByteString byteString) {
            Intrinsics.checkNotNullParameter(sink2, "");
            Intrinsics.checkNotNullParameter(byteString, "");
            return new HashingSink(sink2, byteString, "HmacSHA1");
        }

        @JvmStatic
        @NotNull
        public final HashingSink hmacSha256(@NotNull Sink sink2, @NotNull ByteString byteString) {
            Intrinsics.checkNotNullParameter(sink2, "");
            Intrinsics.checkNotNullParameter(byteString, "");
            return new HashingSink(sink2, byteString, "HmacSHA256");
        }

        @JvmStatic
        @NotNull
        public final HashingSink hmacSha512(@NotNull Sink sink2, @NotNull ByteString byteString) {
            Intrinsics.checkNotNullParameter(sink2, "");
            Intrinsics.checkNotNullParameter(byteString, "");
            return new HashingSink(sink2, byteString, "HmacSHA512");
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }
}


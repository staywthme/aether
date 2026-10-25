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
import okio.Buffer;
import okio.ByteString;
import okio.ForwardingSource;
import okio.Segment;
import okio.Source;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 \u00192\u00020\u00012\u00020\u0002:\u0001\u0019B\u0017\b\u0010\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006B\u0017\b\u0010\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\u0006\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tB\u0017\b\u0010\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fB\u001f\b\u0010\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\u0006\u0010\r\u001a\u00020\u000e\u0012\u0006\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\u000fJ\r\u0010\u0010\u001a\u00020\u000eH\u0007\u00a2\u0006\u0002\b\u0013J\u0018\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0015H\u0016R\u0011\u0010\u0010\u001a\u00020\u000e8G\u00a2\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001a"}, d2={"Lokio/HashingSource;", "Lokio/ForwardingSource;", "Lokio/Source;", "source", "digest", "Ljava/security/MessageDigest;", "(Lokio/Source;Ljava/security/MessageDigest;)V", "algorithm", "", "(Lokio/Source;Ljava/lang/String;)V", "mac", "Ljavax/crypto/Mac;", "(Lokio/Source;Ljavax/crypto/Mac;)V", "key", "Lokio/ByteString;", "(Lokio/Source;Lokio/ByteString;Ljava/lang/String;)V", "hash", "()Lokio/ByteString;", "messageDigest", "-deprecated_hash", "read", "", "sink", "Lokio/Buffer;", "byteCount", "Companion", "okio"})
public final class HashingSource
extends ForwardingSource
implements Source {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @Nullable
    private final MessageDigest messageDigest;
    @Nullable
    private final Mac mac;

    public HashingSource(@NotNull Source source2, @NotNull MessageDigest messageDigest) {
        Intrinsics.checkNotNullParameter(source2, "");
        Intrinsics.checkNotNullParameter(messageDigest, "");
        super(source2);
        this.messageDigest = messageDigest;
        this.mac = null;
    }

    public HashingSource(@NotNull Source source2, @NotNull String string) {
        Intrinsics.checkNotNullParameter(source2, "");
        Intrinsics.checkNotNullParameter(string, "");
        MessageDigest messageDigest = MessageDigest.getInstance(string);
        Intrinsics.checkNotNullExpressionValue(messageDigest, "");
        this(source2, messageDigest);
    }

    public HashingSource(@NotNull Source source2, @NotNull Mac mac) {
        Intrinsics.checkNotNullParameter(source2, "");
        Intrinsics.checkNotNullParameter(mac, "");
        super(source2);
        this.mac = mac;
        this.messageDigest = null;
    }

    public HashingSource(@NotNull Source object, @NotNull ByteString byteString, @NotNull String string) {
        Source source2;
        Source source3;
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(byteString, "");
        Intrinsics.checkNotNullParameter(string, "");
        Object object2 = object;
        object = this;
        try {
            Source source4 = object;
            Source source5 = object2;
            object2 = object = Mac.getInstance(string);
            Source source6 = source5;
            Source source7 = source4;
            ((Mac)object2).init(new SecretKeySpec(byteString.toByteArray(), string));
            source3 = source7;
            source2 = source6;
        }
        catch (InvalidKeyException invalidKeyException) {
            throw new IllegalArgumentException(invalidKeyException);
        }
        Intrinsics.checkNotNull(object);
        super(source2, (Mac)object);
    }

    @Override
    public final long read(@NotNull Buffer buffer, long l2) throws IOException {
        Intrinsics.checkNotNullParameter(buffer, "");
        long l3 = super.read(buffer, l2);
        if (l3 != -1L) {
            long l4;
            long l5 = buffer.size() - l3;
            Segment segment = buffer.head;
            Intrinsics.checkNotNull(segment);
            Segment segment2 = segment;
            for (l4 = buffer.size(); l4 > l5; l4 -= (long)(segment2.limit - segment2.pos)) {
                Intrinsics.checkNotNull(segment2.prev);
            }
            while (l4 < buffer.size()) {
                int n2 = (int)((long)segment2.pos + l5 - l4);
                if (this.messageDigest != null) {
                    this.messageDigest.update(segment2.data, n2, segment2.limit - n2);
                } else {
                    Mac mac = this.mac;
                    Intrinsics.checkNotNull(mac);
                    mac.update(segment2.data, n2, segment2.limit - n2);
                }
                l5 = l4 += (long)(segment2.limit - segment2.pos);
                Intrinsics.checkNotNull(segment2.next);
            }
        }
        return l3;
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
    public static final HashingSource md5(@NotNull Source source2) {
        return Companion.md5(source2);
    }

    @JvmStatic
    @NotNull
    public static final HashingSource sha1(@NotNull Source source2) {
        return Companion.sha1(source2);
    }

    @JvmStatic
    @NotNull
    public static final HashingSource sha256(@NotNull Source source2) {
        return Companion.sha256(source2);
    }

    @JvmStatic
    @NotNull
    public static final HashingSource sha512(@NotNull Source source2) {
        return Companion.sha512(source2);
    }

    @JvmStatic
    @NotNull
    public static final HashingSource hmacSha1(@NotNull Source source2, @NotNull ByteString byteString) {
        return Companion.hmacSha1(source2, byteString);
    }

    @JvmStatic
    @NotNull
    public static final HashingSource hmacSha256(@NotNull Source source2, @NotNull ByteString byteString) {
        return Companion.hmacSha256(source2, byteString);
    }

    @JvmStatic
    @NotNull
    public static final HashingSource hmacSha512(@NotNull Source source2, @NotNull ByteString byteString) {
        return Companion.hmacSha512(source2, byteString);
    }

    @Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0007J\u0018\u0010\t\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0007J\u0018\u0010\n\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0007J\u0010\u0010\u000b\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\f\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\r\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u000e\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007\u00a8\u0006\u000f"}, d2={"Lokio/HashingSource$Companion;", "", "()V", "hmacSha1", "Lokio/HashingSource;", "source", "Lokio/Source;", "key", "Lokio/ByteString;", "hmacSha256", "hmacSha512", "md5", "sha1", "sha256", "sha512", "okio"})
    public static final class Companion {
        private Companion() {
        }

        @JvmStatic
        @NotNull
        public final HashingSource md5(@NotNull Source source2) {
            Intrinsics.checkNotNullParameter(source2, "");
            return new HashingSource(source2, "MD5");
        }

        @JvmStatic
        @NotNull
        public final HashingSource sha1(@NotNull Source source2) {
            Intrinsics.checkNotNullParameter(source2, "");
            return new HashingSource(source2, "SHA-1");
        }

        @JvmStatic
        @NotNull
        public final HashingSource sha256(@NotNull Source source2) {
            Intrinsics.checkNotNullParameter(source2, "");
            return new HashingSource(source2, "SHA-256");
        }

        @JvmStatic
        @NotNull
        public final HashingSource sha512(@NotNull Source source2) {
            Intrinsics.checkNotNullParameter(source2, "");
            return new HashingSource(source2, "SHA-512");
        }

        @JvmStatic
        @NotNull
        public final HashingSource hmacSha1(@NotNull Source source2, @NotNull ByteString byteString) {
            Intrinsics.checkNotNullParameter(source2, "");
            Intrinsics.checkNotNullParameter(byteString, "");
            return new HashingSource(source2, byteString, "HmacSHA1");
        }

        @JvmStatic
        @NotNull
        public final HashingSource hmacSha256(@NotNull Source source2, @NotNull ByteString byteString) {
            Intrinsics.checkNotNullParameter(source2, "");
            Intrinsics.checkNotNullParameter(byteString, "");
            return new HashingSource(source2, byteString, "HmacSHA256");
        }

        @JvmStatic
        @NotNull
        public final HashingSource hmacSha512(@NotNull Source source2, @NotNull ByteString byteString) {
            Intrinsics.checkNotNullParameter(source2, "");
            Intrinsics.checkNotNullParameter(byteString, "");
            return new HashingSource(source2, byteString, "HmacSHA512");
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }
}


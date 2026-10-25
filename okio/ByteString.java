/*
 * Decompiled with CFR 0.152.
 */
package okio;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.util.Arrays;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.collections.ArraysKt;
import kotlin.jvm.JvmField;
import kotlin.jvm.JvmName;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;
import okio.-Base64;
import okio.-SegmentedByteString;
import okio.Buffer;
import okio._JvmPlatformKt;
import okio.internal.-ByteString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000r\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0005\n\u0002\b\u0019\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0016\u0018\u0000 ]2\u00020\u00012\b\u0012\u0004\u0012\u00020\u00000\u0002:\u0001]B\u000f\b\u0000\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\u0005J\b\u0010\u0015\u001a\u00020\u0016H\u0016J\b\u0010\u0017\u001a\u00020\u0010H\u0016J\b\u0010\u0018\u001a\u00020\u0010H\u0016J\u0011\u0010\u0019\u001a\u00020\t2\u0006\u0010\u001a\u001a\u00020\u0000H\u0096\u0002J,\u0010\u001b\u001a\u00020\u001c2\b\b\u0002\u0010\u001d\u001a\u00020\t2\u0006\u0010\u001e\u001a\u00020\u00042\b\b\u0002\u0010\u001f\u001a\u00020\t2\u0006\u0010 \u001a\u00020\tH\u0016J\u0015\u0010!\u001a\u00020\u00002\u0006\u0010\"\u001a\u00020\u0010H\u0010\u00a2\u0006\u0002\b#J\u000e\u0010$\u001a\u00020%2\u0006\u0010&\u001a\u00020\u0004J\u000e\u0010$\u001a\u00020%2\u0006\u0010&\u001a\u00020\u0000J\u0013\u0010'\u001a\u00020%2\b\u0010\u001a\u001a\u0004\u0018\u00010(H\u0096\u0002J\u0016\u0010)\u001a\u00020*2\u0006\u0010+\u001a\u00020\tH\u0087\u0002\u00a2\u0006\u0002\b,J\u0015\u0010,\u001a\u00020*2\u0006\u0010+\u001a\u00020\tH\u0007\u00a2\u0006\u0002\b-J\r\u0010.\u001a\u00020\tH\u0010\u00a2\u0006\u0002\b/J\b\u0010\b\u001a\u00020\tH\u0016J\b\u00100\u001a\u00020\u0010H\u0016J\u001d\u00101\u001a\u00020\u00002\u0006\u0010\"\u001a\u00020\u00102\u0006\u00102\u001a\u00020\u0000H\u0010\u00a2\u0006\u0002\b3J\u0010\u00104\u001a\u00020\u00002\u0006\u00102\u001a\u00020\u0000H\u0016J\u0010\u00105\u001a\u00020\u00002\u0006\u00102\u001a\u00020\u0000H\u0016J\u0010\u00106\u001a\u00020\u00002\u0006\u00102\u001a\u00020\u0000H\u0016J\u001a\u00107\u001a\u00020\t2\u0006\u0010\u001a\u001a\u00020\u00042\b\b\u0002\u00108\u001a\u00020\tH\u0017J\u001a\u00107\u001a\u00020\t2\u0006\u0010\u001a\u001a\u00020\u00002\b\b\u0002\u00108\u001a\u00020\tH\u0007J\r\u00109\u001a\u00020\u0004H\u0010\u00a2\u0006\u0002\b:J\u0015\u0010;\u001a\u00020*2\u0006\u0010<\u001a\u00020\tH\u0010\u00a2\u0006\u0002\b=J\u001a\u0010>\u001a\u00020\t2\u0006\u0010\u001a\u001a\u00020\u00042\b\b\u0002\u00108\u001a\u00020\tH\u0017J\u001a\u0010>\u001a\u00020\t2\u0006\u0010\u001a\u001a\u00020\u00002\b\b\u0002\u00108\u001a\u00020\tH\u0007J\u0006\u0010?\u001a\u00020\u0000J(\u0010@\u001a\u00020%2\u0006\u0010\u001d\u001a\u00020\t2\u0006\u0010\u001a\u001a\u00020\u00042\u0006\u0010A\u001a\u00020\t2\u0006\u0010 \u001a\u00020\tH\u0016J(\u0010@\u001a\u00020%2\u0006\u0010\u001d\u001a\u00020\t2\u0006\u0010\u001a\u001a\u00020\u00002\u0006\u0010A\u001a\u00020\t2\u0006\u0010 \u001a\u00020\tH\u0016J\u0010\u0010B\u001a\u00020\u001c2\u0006\u0010C\u001a\u00020DH\u0002J\u0006\u0010E\u001a\u00020\u0000J\u0006\u0010F\u001a\u00020\u0000J\u0006\u0010G\u001a\u00020\u0000J\r\u0010\u000e\u001a\u00020\tH\u0007\u00a2\u0006\u0002\bHJ\u000e\u0010I\u001a\u00020%2\u0006\u0010J\u001a\u00020\u0004J\u000e\u0010I\u001a\u00020%2\u0006\u0010J\u001a\u00020\u0000J\u0010\u0010K\u001a\u00020\u00102\u0006\u0010L\u001a\u00020MH\u0016J\u001c\u0010N\u001a\u00020\u00002\b\b\u0002\u0010O\u001a\u00020\t2\b\b\u0002\u0010P\u001a\u00020\tH\u0017J\b\u0010Q\u001a\u00020\u0000H\u0016J\b\u0010R\u001a\u00020\u0000H\u0016J\b\u0010S\u001a\u00020\u0004H\u0016J\b\u0010T\u001a\u00020\u0010H\u0016J\b\u0010\u000f\u001a\u00020\u0010H\u0016J\u0010\u0010U\u001a\u00020\u001c2\u0006\u0010V\u001a\u00020WH\u0016J%\u0010U\u001a\u00020\u001c2\u0006\u0010X\u001a\u00020Y2\u0006\u0010\u001d\u001a\u00020\t2\u0006\u0010 \u001a\u00020\tH\u0010\u00a2\u0006\u0002\bZJ\u0010\u0010[\u001a\u00020\u001c2\u0006\u0010V\u001a\u00020\\H\u0002R\u0014\u0010\u0003\u001a\u00020\u0004X\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u001a\u0010\b\u001a\u00020\tX\u0080\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u0011\u0010\u000e\u001a\u00020\t8G\u00a2\u0006\u0006\u001a\u0004\b\u000e\u0010\u000bR\u001c\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0080\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014\u00a8\u0006^"}, d2={"Lokio/ByteString;", "Ljava/io/Serializable;", "", "data", "", "([B)V", "getData$okio", "()[B", "hashCode", "", "getHashCode$okio", "()I", "setHashCode$okio", "(I)V", "size", "utf8", "", "getUtf8$okio", "()Ljava/lang/String;", "setUtf8$okio", "(Ljava/lang/String;)V", "asByteBuffer", "Ljava/nio/ByteBuffer;", "base64", "base64Url", "compareTo", "other", "copyInto", "", "offset", "target", "targetOffset", "byteCount", "digest", "algorithm", "digest$okio", "endsWith", "", "suffix", "equals", "", "get", "", "index", "getByte", "-deprecated_getByte", "getSize", "getSize$okio", "hex", "hmac", "key", "hmac$okio", "hmacSha1", "hmacSha256", "hmacSha512", "indexOf", "fromIndex", "internalArray", "internalArray$okio", "internalGet", "pos", "internalGet$okio", "lastIndexOf", "md5", "rangeEquals", "otherOffset", "readObject", "in", "Ljava/io/ObjectInputStream;", "sha1", "sha256", "sha512", "-deprecated_size", "startsWith", "prefix", "string", "charset", "Ljava/nio/charset/Charset;", "substring", "beginIndex", "endIndex", "toAsciiLowercase", "toAsciiUppercase", "toByteArray", "toString", "write", "out", "Ljava/io/OutputStream;", "buffer", "Lokio/Buffer;", "write$okio", "writeObject", "Ljava/io/ObjectOutputStream;", "Companion", "okio"})
@SourceDebugExtension(value={"SMAP\nByteString.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ByteString.kt\nokio/ByteString\n+ 2 ByteString.kt\nokio/internal/-ByteString\n+ 3 Util.kt\nokio/-SegmentedByteString\n+ 4 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,364:1\n43#2,7:365\n53#2:372\n56#2:373\n64#2,4:374\n68#2:379\n70#2:381\n76#2,23:382\n104#2,23:405\n131#2,2:428\n133#2,9:431\n145#2:440\n148#2:441\n151#2:442\n154#2:443\n162#2:444\n172#2,3:445\n171#2:448\n185#2,2:449\n190#2:451\n194#2:452\n198#2:453\n202#2:454\n206#2,7:455\n219#2:462\n223#2,8:463\n235#2,4:471\n244#2,5:475\n253#2,6:480\n259#2,9:487\n322#2,8:496\n131#2,2:504\n133#2,9:507\n333#2,9:516\n68#3:378\n74#3:380\n74#3:486\n1#4:430\n1#4:506\n*S KotlinDebug\n*F\n+ 1 ByteString.kt\nokio/ByteString\n*L\n66#1:365,7\n71#1:372\n108#1:373\n110#1:374,4\n110#1:379\n110#1:381\n112#1:382,23\n114#1:405,23\n118#1:428,2\n118#1:431,9\n120#1:440\n129#1:441\n131#1:442\n133#1:443\n152#1:444\n159#1:445,3\n159#1:448\n166#1:449,2\n168#1:451\n170#1:452\n172#1:453\n174#1:454\n180#1:455,7\n183#1:462\n186#1:463,8\n188#1:471,4\n190#1:475,5\n192#1:480,6\n192#1:487,9\n194#1:496,8\n194#1:504,2\n194#1:507,9\n194#1:516,9\n110#1:378\n110#1:380\n192#1:486\n118#1:430\n194#1:506\n*E\n"})
public class ByteString
implements Serializable,
Comparable<ByteString> {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final byte[] data;
    private transient int hashCode;
    @Nullable
    private transient String utf8;
    private static final long serialVersionUID = 1L;
    @JvmField
    @NotNull
    public static final ByteString EMPTY = new ByteString(new byte[0]);

    public ByteString(@NotNull byte[] byArray) {
        Intrinsics.checkNotNullParameter(byArray, "");
        this.data = byArray;
    }

    @NotNull
    public final byte[] getData$okio() {
        return this.data;
    }

    public final int getHashCode$okio() {
        return this.hashCode;
    }

    public final void setHashCode$okio(int n2) {
        this.hashCode = n2;
    }

    @Nullable
    public final String getUtf8$okio() {
        return this.utf8;
    }

    public final void setUtf8$okio(@Nullable String string) {
        this.utf8 = string;
    }

    @NotNull
    public String utf8() {
        ByteString byteString = this;
        String string = byteString.getUtf8$okio();
        if (string == null) {
            string = _JvmPlatformKt.toUtf8String(byteString.internalArray$okio());
            byteString.setUtf8$okio(string);
        }
        return string;
    }

    @NotNull
    public String string(@NotNull Charset charset) {
        Intrinsics.checkNotNullParameter(charset, "");
        return new String(this.data, charset);
    }

    @NotNull
    public String base64() {
        ByteString byteString = this;
        return -Base64.encodeBase64$default(byteString.getData$okio(), null, 1, null);
    }

    @NotNull
    public final ByteString md5() {
        return this.digest$okio("MD5");
    }

    @NotNull
    public final ByteString sha1() {
        return this.digest$okio("SHA-1");
    }

    @NotNull
    public final ByteString sha256() {
        return this.digest$okio("SHA-256");
    }

    @NotNull
    public final ByteString sha512() {
        return this.digest$okio("SHA-512");
    }

    @NotNull
    public ByteString digest$okio(@NotNull String object) {
        Intrinsics.checkNotNullParameter(object, "");
        object = MessageDigest.getInstance((String)object);
        ((MessageDigest)object).update(this.data, 0, this.size());
        object = ((MessageDigest)object).digest();
        Intrinsics.checkNotNull(object);
        return new ByteString((byte[])object);
    }

    @NotNull
    public ByteString hmacSha1(@NotNull ByteString byteString) {
        Intrinsics.checkNotNullParameter(byteString, "");
        return this.hmac$okio("HmacSHA1", byteString);
    }

    @NotNull
    public ByteString hmacSha256(@NotNull ByteString byteString) {
        Intrinsics.checkNotNullParameter(byteString, "");
        return this.hmac$okio("HmacSHA256", byteString);
    }

    @NotNull
    public ByteString hmacSha512(@NotNull ByteString byteString) {
        Intrinsics.checkNotNullParameter(byteString, "");
        return this.hmac$okio("HmacSHA512", byteString);
    }

    @NotNull
    public ByteString hmac$okio(@NotNull String string, @NotNull ByteString byteString) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(byteString, "");
        try {
            Mac mac = Mac.getInstance(string);
            mac.init(new SecretKeySpec(byteString.toByteArray(), string));
            byte[] byArray = mac.doFinal(this.data);
            Intrinsics.checkNotNullExpressionValue(byArray, "");
            return new ByteString(byArray);
        }
        catch (InvalidKeyException invalidKeyException) {
            throw new IllegalArgumentException(invalidKeyException);
        }
    }

    @NotNull
    public String base64Url() {
        ByteString byteString = this;
        return -Base64.encodeBase64(byteString.getData$okio(), -Base64.getBASE64_URL_SAFE());
    }

    @NotNull
    public String hex() {
        ByteString byteString = this;
        char[] cArray = new char[byteString.getData$okio().length << 1];
        int n2 = 0;
        for (Object object : (ByteString)byteString.getData$okio()) {
            int n3 = n2++;
            Object object2 = object;
            cArray[n3] = -ByteString.getHEX_DIGIT_CHARS()[object2 >> 4 & 0xF];
            int n4 = n2++;
            object2 = object;
            cArray[n4] = -ByteString.getHEX_DIGIT_CHARS()[object2 & 0xF];
        }
        return StringsKt.concatToString(cArray);
    }

    @NotNull
    public ByteString toAsciiLowercase() {
        Object object = this;
        for (int i2 = 0; i2 < ((ByteString)object).getData$okio().length; ++i2) {
            Object object2 = ((ByteString)object).getData$okio()[i2];
            if (object2 < 65 || object2 > 90) {
                continue;
            }
            byte[] byArray = ((ByteString)object).getData$okio();
            byte[] byArray2 = Arrays.copyOf(byArray, byArray.length);
            Intrinsics.checkNotNullExpressionValue(byArray2, "");
            object = byArray2;
            byArray2[i2++] = (byte)(object2 - -32);
            while (i2 < ((Object)object).length) {
                Object object3 = object[i2];
                object2 = object3;
                if (object3 < 65 || object2 > 90) {
                    ++i2;
                    continue;
                }
                object[i2] = (byte)(object2 - -32);
                ++i2;
            }
            return new ByteString((byte[])object);
        }
        return object;
    }

    @NotNull
    public ByteString toAsciiUppercase() {
        Object object = this;
        for (int i2 = 0; i2 < ((ByteString)object).getData$okio().length; ++i2) {
            Object object2 = ((ByteString)object).getData$okio()[i2];
            if (object2 < 97 || object2 > 122) {
                continue;
            }
            byte[] byArray = ((ByteString)object).getData$okio();
            byte[] byArray2 = Arrays.copyOf(byArray, byArray.length);
            Intrinsics.checkNotNullExpressionValue(byArray2, "");
            object = byArray2;
            byArray2[i2++] = (byte)(object2 - 32);
            while (i2 < ((Object)object).length) {
                Object object3 = object[i2];
                object2 = object3;
                if (object3 < 97 || object2 > 122) {
                    ++i2;
                    continue;
                }
                object[i2] = (byte)(object2 - 32);
                ++i2;
            }
            return new ByteString((byte[])object);
        }
        return object;
    }

    @JvmOverloads
    @NotNull
    public ByteString substring(int n2, int n3) {
        Object object = this;
        n3 = -SegmentedByteString.resolveDefaultParameter((ByteString)object, n3);
        if (!(n2 >= 0)) {
            object = "beginIndex < 0";
            throw new IllegalArgumentException(object.toString());
        }
        if (!(n3 <= ((ByteString)object).getData$okio().length)) {
            object = "endIndex > length(" + ((ByteString)object).getData$okio().length + ')';
            throw new IllegalArgumentException(object.toString());
        }
        int n4 = n3 - n2;
        if (!(n4 >= 0)) {
            String string = "endIndex < beginIndex";
            throw new IllegalArgumentException(string.toString());
        }
        if (n2 == 0 && n3 == ((ByteString)object).getData$okio().length) {
            return object;
        }
        byte[] byArray = ((ByteString)object).getData$okio();
        object = byArray;
        return new ByteString(ArraysKt.copyOfRange(byArray, n2, n3));
    }

    public static /* synthetic */ ByteString substring$default(ByteString byteString, int n2, int n3, int n4, Object object) {
        if (object != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: substring");
        }
        if ((n4 & 1) != 0) {
            n2 = 0;
        }
        if ((n4 & 2) != 0) {
            n3 = -SegmentedByteString.getDEFAULT__ByteString_size();
        }
        return byteString.substring(n2, n3);
    }

    public byte internalGet$okio(int n2) {
        ByteString byteString = this;
        return byteString.getData$okio()[n2];
    }

    @JvmName(name="getByte")
    public final byte getByte(int n2) {
        return this.internalGet$okio(n2);
    }

    @JvmName(name="size")
    public final int size() {
        return this.getSize$okio();
    }

    public int getSize$okio() {
        ByteString byteString = this;
        return byteString.getData$okio().length;
    }

    @NotNull
    public byte[] toByteArray() {
        ByteString byteString = this;
        byte[] byArray = byteString.getData$okio();
        byte[] byArray2 = Arrays.copyOf(byArray, byArray.length);
        Intrinsics.checkNotNullExpressionValue(byArray2, "");
        return byArray2;
    }

    @NotNull
    public byte[] internalArray$okio() {
        ByteString byteString = this;
        return byteString.getData$okio();
    }

    @NotNull
    public ByteBuffer asByteBuffer() {
        ByteBuffer byteBuffer = ByteBuffer.wrap(this.data).asReadOnlyBuffer();
        Intrinsics.checkNotNullExpressionValue(byteBuffer, "");
        return byteBuffer;
    }

    public void write(@NotNull OutputStream outputStream2) throws IOException {
        Intrinsics.checkNotNullParameter(outputStream2, "");
        outputStream2.write(this.data);
    }

    public void write$okio(@NotNull Buffer buffer, int n2, int n3) {
        Intrinsics.checkNotNullParameter(buffer, "");
        -ByteString.commonWrite(this, buffer, n2, n3);
    }

    public boolean rangeEquals(int n2, @NotNull ByteString byteString, int n3, int n4) {
        Intrinsics.checkNotNullParameter(byteString, "");
        ByteString byteString2 = this;
        return byteString.rangeEquals(n3, byteString2.getData$okio(), n2, n4);
    }

    public boolean rangeEquals(int n2, @NotNull byte[] byArray, int n3, int n4) {
        Intrinsics.checkNotNullParameter(byArray, "");
        ByteString byteString = this;
        return n2 >= 0 && n2 <= byteString.getData$okio().length - n4 && n3 >= 0 && n3 <= byArray.length - n4 && -SegmentedByteString.arrayRangeEquals(byteString.getData$okio(), n2, byArray, n3, n4);
    }

    public void copyInto(int n2, @NotNull byte[] byArray, int n3, int n4) {
        Intrinsics.checkNotNullParameter(byArray, "");
        ByteString byteString = this;
        int n5 = n2;
        ArraysKt.copyInto(byteString.getData$okio(), byArray, n3, n5, n5 + n4);
    }

    public static /* synthetic */ void copyInto$default(ByteString byteString, int n2, byte[] byArray, int n3, int n4, int n5, Object object) {
        if (object != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: copyInto");
        }
        if ((n5 & 1) != 0) {
            n2 = 0;
        }
        if ((n5 & 4) != 0) {
            n3 = 0;
        }
        byteString.copyInto(n2, byArray, n3, n4);
    }

    public final boolean startsWith(@NotNull ByteString byteString) {
        Intrinsics.checkNotNullParameter(byteString, "");
        ByteString byteString2 = this;
        return byteString2.rangeEquals(0, byteString, 0, byteString.size());
    }

    public final boolean startsWith(@NotNull byte[] byArray) {
        Intrinsics.checkNotNullParameter(byArray, "");
        ByteString byteString = this;
        return byteString.rangeEquals(0, byArray, 0, byArray.length);
    }

    public final boolean endsWith(@NotNull ByteString byteString) {
        Intrinsics.checkNotNullParameter(byteString, "");
        ByteString byteString2 = this;
        return byteString2.rangeEquals(byteString2.size() - byteString.size(), byteString, 0, byteString.size());
    }

    public final boolean endsWith(@NotNull byte[] byArray) {
        Intrinsics.checkNotNullParameter(byArray, "");
        ByteString byteString = this;
        return byteString.rangeEquals(byteString.size() - byArray.length, byArray, 0, byArray.length);
    }

    @JvmOverloads
    public final int indexOf(@NotNull ByteString byteString, int n2) {
        Intrinsics.checkNotNullParameter(byteString, "");
        return this.indexOf(byteString.internalArray$okio(), n2);
    }

    public static /* synthetic */ int indexOf$default(ByteString byteString, ByteString byteString2, int n2, int n3, Object object) {
        if (object != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: indexOf");
        }
        if ((n3 & 2) != 0) {
            n2 = 0;
        }
        return byteString.indexOf(byteString2, n2);
    }

    @JvmOverloads
    public int indexOf(@NotNull byte[] byArray, int n2) {
        Intrinsics.checkNotNullParameter(byArray, "");
        ByteString byteString = this;
        int n3 = byteString.getData$okio().length - byArray.length;
        n2 = Math.max(n2, 0);
        if (n2 <= n3) {
            while (true) {
                if (-SegmentedByteString.arrayRangeEquals(byteString.getData$okio(), n2, byArray, 0, byArray.length)) {
                    return n2;
                }
                if (n2 == n3) break;
                ++n2;
            }
        }
        return -1;
    }

    public static /* synthetic */ int indexOf$default(ByteString byteString, byte[] byArray, int n2, int n3, Object object) {
        if (object != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: indexOf");
        }
        if ((n3 & 2) != 0) {
            n2 = 0;
        }
        return byteString.indexOf(byArray, n2);
    }

    @JvmOverloads
    public final int lastIndexOf(@NotNull ByteString byteString, int n2) {
        Intrinsics.checkNotNullParameter(byteString, "");
        ByteString byteString2 = this;
        return byteString2.lastIndexOf(byteString.internalArray$okio(), n2);
    }

    public static /* synthetic */ int lastIndexOf$default(ByteString byteString, ByteString byteString2, int n2, int n3, Object object) {
        if (object != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: lastIndexOf");
        }
        if ((n3 & 2) != 0) {
            n2 = -SegmentedByteString.getDEFAULT__ByteString_size();
        }
        return byteString.lastIndexOf(byteString2, n2);
    }

    @JvmOverloads
    public int lastIndexOf(@NotNull byte[] byArray, int n2) {
        Intrinsics.checkNotNullParameter(byArray, "");
        ByteString byteString = this;
        n2 = -SegmentedByteString.resolveDefaultParameter(byteString, n2);
        int n3 = byteString.getData$okio().length - byArray.length;
        for (n2 = Math.min(n2, n3); n2 >= 0; --n2) {
            if (!-SegmentedByteString.arrayRangeEquals(byteString.getData$okio(), n2, byArray, 0, byArray.length)) continue;
            return n2;
        }
        return -1;
    }

    public static /* synthetic */ int lastIndexOf$default(ByteString byteString, byte[] byArray, int n2, int n3, Object object) {
        if (object != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: lastIndexOf");
        }
        if ((n3 & 2) != 0) {
            n2 = -SegmentedByteString.getDEFAULT__ByteString_size();
        }
        return byteString.lastIndexOf(byArray, n2);
    }

    public boolean equals(@Nullable Object object) {
        ByteString byteString = this;
        if (object == byteString) {
            return true;
        }
        if (object instanceof ByteString) {
            return ((ByteString)object).size() == byteString.getData$okio().length && ((ByteString)object).rangeEquals(0, byteString.getData$okio(), 0, byteString.getData$okio().length);
        }
        return false;
    }

    public int hashCode() {
        ByteString byteString = this;
        int n2 = byteString.getHashCode$okio();
        if (n2 != 0) {
            return n2;
        }
        n2 = Arrays.hashCode(byteString.getData$okio());
        byteString.setHashCode$okio(n2);
        return n2;
    }

    @Override
    public int compareTo(@NotNull ByteString byteString) {
        Intrinsics.checkNotNullParameter(byteString, "");
        ByteString byteString2 = this;
        int n2 = byteString2.size();
        int n3 = byteString.size();
        int n4 = Math.min(n2, n3);
        for (int i2 = 0; i2 < n4; ++i2) {
            int n5 = byteString2.getByte(i2);
            int n6 = n5 & 0xFF;
            n5 = byteString.getByte(i2);
            if (n6 == (n5 &= 0xFF)) {
                continue;
            }
            if (n6 < n5) {
                return -1;
            }
            return 1;
        }
        if (n2 == n3) {
            return 0;
        }
        if (n2 < n3) {
            return -1;
        }
        return 1;
    }

    /*
     * Enabled aggressive block sorting
     */
    @NotNull
    public String toString() {
        String string;
        ByteString byteString = this;
        if (byteString.getData$okio().length == 0) {
            return "[size=0]";
        }
        boolean bl = false;
        if (bl) {
            return "[size=0]";
        }
        int n2 = -ByteString.access$codePointIndexToCharIndex(byteString.getData$okio(), 64);
        if (n2 == -1) {
            ByteString byteString2;
            if (byteString.getData$okio().length <= 64) {
                string = "[hex=" + byteString.hex() + ']';
                return string;
            }
            StringBuilder stringBuilder = new StringBuilder("[size=").append(byteString.getData$okio().length).append(" hex=");
            ByteString byteString3 = byteString;
            int n3 = -SegmentedByteString.resolveDefaultParameter(byteString3, 64);
            if (!(n3 <= byteString3.getData$okio().length)) {
                String string2 = "endIndex > length(" + byteString3.getData$okio().length + ')';
                throw new IllegalArgumentException(string2.toString());
            }
            n2 = n3;
            if (!(n2 >= 0)) {
                String string3 = "endIndex < beginIndex";
                throw new IllegalArgumentException(string3.toString());
            }
            if (n3 == byteString3.getData$okio().length) {
                byteString2 = byteString3;
                return stringBuilder.append(byteString2.hex()).append("\u2026]").toString();
            }
            byte[] byArray = byteString3.getData$okio();
            ByteString byteString4 = new ByteString(ArraysKt.copyOfRange(byArray, 0, n3));
            byteString2 = byteString4;
            return stringBuilder.append(byteString2.hex()).append("\u2026]").toString();
        }
        String string4 = byteString.utf8();
        String string5 = string4.substring(0, n2);
        Intrinsics.checkNotNullExpressionValue(string5, "");
        String string6 = StringsKt.replace$default(StringsKt.replace$default(StringsKt.replace$default(string5, "\\", "\\\\", false, 4, null), "\n", "\\n", false, 4, null), "\r", "\\r", false, 4, null);
        if (n2 < string4.length()) {
            return "[size=" + byteString.getData$okio().length + " text=" + string6 + "\u2026]";
        }
        string = "[text=" + string6 + ']';
        return string;
    }

    private final void readObject(ObjectInputStream object) throws IOException {
        int n2 = ((ObjectInputStream)object).readInt();
        object = Companion.read((InputStream)object, n2);
        Field field = ByteString.class.getDeclaredField("data");
        field.setAccessible(true);
        field.set(this, ((ByteString)object).data);
    }

    private final void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeInt(this.data.length);
        objectOutputStream.write(this.data);
    }

    @Deprecated(message="moved to operator function", replaceWith=@ReplaceWith(expression="this[index]", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_getByte")
    public final byte -deprecated_getByte(int n2) {
        return this.getByte(n2);
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="size", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_size")
    public final int -deprecated_size() {
        return this.size();
    }

    @JvmOverloads
    @NotNull
    public final ByteString substring(int n2) {
        return ByteString.substring$default(this, n2, 0, 2, null);
    }

    @JvmOverloads
    @NotNull
    public final ByteString substring() {
        return ByteString.substring$default(this, 0, 0, 3, null);
    }

    @JvmOverloads
    public final int indexOf(@NotNull ByteString byteString) {
        Intrinsics.checkNotNullParameter(byteString, "");
        return ByteString.indexOf$default(this, byteString, 0, 2, null);
    }

    @JvmOverloads
    public final int indexOf(@NotNull byte[] byArray) {
        Intrinsics.checkNotNullParameter(byArray, "");
        return ByteString.indexOf$default(this, byArray, 0, 2, null);
    }

    @JvmOverloads
    public final int lastIndexOf(@NotNull ByteString byteString) {
        Intrinsics.checkNotNullParameter(byteString, "");
        return ByteString.lastIndexOf$default(this, byteString, 0, 2, null);
    }

    @JvmOverloads
    public final int lastIndexOf(@NotNull byte[] byArray) {
        Intrinsics.checkNotNullParameter(byArray, "");
        return ByteString.lastIndexOf$default(this, byArray, 0, 2, null);
    }

    @JvmStatic
    @NotNull
    public static final ByteString of(byte ... byArray) {
        return Companion.of(byArray);
    }

    @JvmStatic
    @JvmName(name="of")
    @NotNull
    public static final ByteString of(@NotNull byte[] byArray, int n2, int n3) {
        return Companion.of(byArray, n2, n3);
    }

    @JvmStatic
    @JvmName(name="of")
    @NotNull
    public static final ByteString of(@NotNull ByteBuffer byteBuffer) {
        return Companion.of(byteBuffer);
    }

    @JvmStatic
    @NotNull
    public static final ByteString encodeUtf8(@NotNull String string) {
        return Companion.encodeUtf8(string);
    }

    @JvmStatic
    @JvmName(name="encodeString")
    @NotNull
    public static final ByteString encodeString(@NotNull String string, @NotNull Charset charset) {
        return Companion.encodeString(string, charset);
    }

    @JvmStatic
    @Nullable
    public static final ByteString decodeBase64(@NotNull String string) {
        return Companion.decodeBase64(string);
    }

    @JvmStatic
    @NotNull
    public static final ByteString decodeHex(@NotNull String string) {
        return Companion.decodeHex(string);
    }

    @JvmStatic
    @JvmName(name="read")
    @NotNull
    public static final ByteString read(@NotNull InputStream inputStream2, int n2) throws IOException {
        return Companion.read(inputStream2, n2);
    }

    /*
     * Illegal identifiers - consider using --renameillegalidents true
     */
    @Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000N\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\u0010\u0005\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0017\u0010\u0007\u001a\u0004\u0018\u00010\u00042\u0006\u0010\b\u001a\u00020\tH\u0007\u00a2\u0006\u0002\b\nJ\u0015\u0010\u000b\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\tH\u0007\u00a2\u0006\u0002\b\fJ\u001d\u0010\r\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\u000e\u001a\u00020\u000fH\u0007\u00a2\u0006\u0002\b\u0010J\u0015\u0010\u0011\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\tH\u0007\u00a2\u0006\u0002\b\u0012J\u0015\u0010\u0013\u001a\u00020\u00042\u0006\u0010\u0014\u001a\u00020\u0015H\u0007\u00a2\u0006\u0002\b\u0016J\u0014\u0010\u0013\u001a\u00020\u00042\n\u0010\u0017\u001a\u00020\u0018\"\u00020\u0019H\u0007J%\u0010\u0013\u001a\u00020\u00042\u0006\u0010\u001a\u001a\u00020\u00182\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001cH\u0007\u00a2\u0006\u0002\b\u0016J\u001d\u0010\u001e\u001a\u00020\u00042\u0006\u0010\u001f\u001a\u00020 2\u0006\u0010\u001d\u001a\u00020\u001cH\u0007\u00a2\u0006\u0002\b!J\u000e\u0010\u0007\u001a\u0004\u0018\u00010\u0004*\u00020\tH\u0007J\f\u0010\u000b\u001a\u00020\u0004*\u00020\tH\u0007J\u001b\u0010\"\u001a\u00020\u0004*\u00020\t2\b\b\u0002\u0010\u000e\u001a\u00020\u000fH\u0007\u00a2\u0006\u0002\b\rJ\f\u0010\u0011\u001a\u00020\u0004*\u00020\tH\u0007J\u0019\u0010#\u001a\u00020\u0004*\u00020 2\u0006\u0010\u001d\u001a\u00020\u001cH\u0007\u00a2\u0006\u0002\b\u001eJ\u0011\u0010$\u001a\u00020\u0004*\u00020\u0015H\u0007\u00a2\u0006\u0002\b\u0013J%\u0010$\u001a\u00020\u0004*\u00020\u00182\b\b\u0002\u0010\u001b\u001a\u00020\u001c2\b\b\u0002\u0010\u001d\u001a\u00020\u001cH\u0007\u00a2\u0006\u0002\b\u0013R\u0010\u0010\u0003\u001a\u00020\u00048\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006%"}, d2={"Lokio/ByteString$Companion;", "", "()V", "EMPTY", "Lokio/ByteString;", "serialVersionUID", "", "decodeBase64", "string", "", "-deprecated_decodeBase64", "decodeHex", "-deprecated_decodeHex", "encodeString", "charset", "Ljava/nio/charset/Charset;", "-deprecated_encodeString", "encodeUtf8", "-deprecated_encodeUtf8", "of", "buffer", "Ljava/nio/ByteBuffer;", "-deprecated_of", "data", "", "", "array", "offset", "", "byteCount", "read", "inputstream", "Ljava/io/InputStream;", "-deprecated_read", "encode", "readByteString", "toByteString", "okio"})
    @SourceDebugExtension(value={"SMAP\nByteString.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ByteString.kt\nokio/ByteString$Companion\n+ 2 ByteString.kt\nokio/internal/-ByteString\n+ 3 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,364:1\n271#2:365\n275#2,3:366\n282#2,3:369\n289#2,2:372\n295#2:374\n297#2,7:376\n1#3:375\n1#3:383\n*S KotlinDebug\n*F\n+ 1 ByteString.kt\nokio/ByteString$Companion\n*L\n234#1:365\n239#1:366,3\n251#1:369,3\n259#1:372,2\n262#1:374\n262#1:376,7\n262#1:375\n*E\n"})
    public static final class Companion {
        private Companion() {
        }

        @JvmStatic
        @NotNull
        public final ByteString of(byte ... byArray) {
            Intrinsics.checkNotNullParameter(byArray, "");
            byte[] byArray2 = Arrays.copyOf(byArray, byArray.length);
            Intrinsics.checkNotNullExpressionValue(byArray2, "");
            return new ByteString(byArray2);
        }

        @JvmStatic
        @JvmName(name="of")
        @NotNull
        public final ByteString of(@NotNull byte[] byArray, int n2, int n3) {
            Intrinsics.checkNotNullParameter(byArray, "");
            n3 = -SegmentedByteString.resolveDefaultParameter(byArray, n3);
            -SegmentedByteString.checkOffsetAndCount(byArray.length, n2, n3);
            n3 = n2 + n3;
            return new ByteString(ArraysKt.copyOfRange(byArray, n2, n3));
        }

        public static /* synthetic */ ByteString of$default(Companion companion, byte[] byArray, int n2, int n3, int n4, Object object) {
            if ((n4 & 1) != 0) {
                n2 = 0;
            }
            if ((n4 & 2) != 0) {
                n3 = -SegmentedByteString.getDEFAULT__ByteString_size();
            }
            return companion.of(byArray, n2, n3);
        }

        @JvmStatic
        @JvmName(name="of")
        @NotNull
        public final ByteString of(@NotNull ByteBuffer byteBuffer) {
            Intrinsics.checkNotNullParameter(byteBuffer, "");
            byte[] byArray = new byte[byteBuffer.remaining()];
            byteBuffer.get(byArray);
            return new ByteString(byArray);
        }

        @JvmStatic
        @NotNull
        public final ByteString encodeUtf8(@NotNull String string) {
            Intrinsics.checkNotNullParameter(string, "");
            ByteString byteString = new ByteString(_JvmPlatformKt.asUtf8ToByteArray(string));
            byteString.setUtf8$okio(string);
            return byteString;
        }

        @JvmStatic
        @JvmName(name="encodeString")
        @NotNull
        public final ByteString encodeString(@NotNull String string, @NotNull Charset charset) {
            Intrinsics.checkNotNullParameter(string, "");
            Intrinsics.checkNotNullParameter(charset, "");
            byte[] byArray = string.getBytes(charset);
            Intrinsics.checkNotNullExpressionValue(byArray, "");
            return new ByteString(byArray);
        }

        public static /* synthetic */ ByteString encodeString$default(Companion companion, String string, Charset charset, int n2, Object object) {
            if ((n2 & 1) != 0) {
                charset = Charsets.UTF_8;
            }
            return companion.encodeString(string, charset);
        }

        @JvmStatic
        @Nullable
        public final ByteString decodeBase64(@NotNull String object) {
            Intrinsics.checkNotNullParameter(object, "");
            byte[] byArray = -Base64.decodeBase64ToArray((String)object);
            object = byArray;
            if (byArray != null) {
                return new ByteString((byte[])object);
            }
            return null;
        }

        @JvmStatic
        @NotNull
        public final ByteString decodeHex(@NotNull String string) {
            Intrinsics.checkNotNullParameter(string, "");
            if (!(string.length() % 2 == 0)) {
                String string2 = "Unexpected hex string: " + string;
                throw new IllegalArgumentException(string2.toString());
            }
            byte[] byArray = new byte[string.length() / 2];
            int n2 = byArray.length;
            for (int i2 = 0; i2 < n2; ++i2) {
                int n3 = -ByteString.access$decodeHexDigit(string.charAt(i2 << 1)) << 4;
                int n4 = -ByteString.access$decodeHexDigit(string.charAt((i2 << 1) + 1));
                byArray[i2] = (byte)(n3 + n4);
            }
            return new ByteString(byArray);
        }

        @JvmStatic
        @JvmName(name="read")
        @NotNull
        public final ByteString read(@NotNull InputStream inputStream2, int n2) throws IOException {
            int n3;
            Intrinsics.checkNotNullParameter(inputStream2, "");
            if (!(n2 >= 0)) {
                String string = "byteCount < 0: " + n2;
                throw new IllegalArgumentException(string.toString());
            }
            byte[] byArray = new byte[n2];
            for (int i2 = 0; i2 < n2; i2 += n3) {
                n3 = inputStream2.read(byArray, i2, n2 - i2);
                if (n3 != -1) continue;
                throw new EOFException();
            }
            return new ByteString(byArray);
        }

        @Deprecated(message="moved to extension function", replaceWith=@ReplaceWith(expression="string.decodeBase64()", imports={"okio.ByteString.Companion.decodeBase64"}), level=DeprecationLevel.ERROR)
        @JvmName(name="-deprecated_decodeBase64")
        @Nullable
        public final ByteString -deprecated_decodeBase64(@NotNull String string) {
            Intrinsics.checkNotNullParameter(string, "");
            return this.decodeBase64(string);
        }

        @Deprecated(message="moved to extension function", replaceWith=@ReplaceWith(expression="string.decodeHex()", imports={"okio.ByteString.Companion.decodeHex"}), level=DeprecationLevel.ERROR)
        @JvmName(name="-deprecated_decodeHex")
        @NotNull
        public final ByteString -deprecated_decodeHex(@NotNull String string) {
            Intrinsics.checkNotNullParameter(string, "");
            return this.decodeHex(string);
        }

        @Deprecated(message="moved to extension function", replaceWith=@ReplaceWith(expression="string.encode(charset)", imports={"okio.ByteString.Companion.encode"}), level=DeprecationLevel.ERROR)
        @JvmName(name="-deprecated_encodeString")
        @NotNull
        public final ByteString -deprecated_encodeString(@NotNull String string, @NotNull Charset charset) {
            Intrinsics.checkNotNullParameter(string, "");
            Intrinsics.checkNotNullParameter(charset, "");
            return this.encodeString(string, charset);
        }

        @Deprecated(message="moved to extension function", replaceWith=@ReplaceWith(expression="string.encodeUtf8()", imports={"okio.ByteString.Companion.encodeUtf8"}), level=DeprecationLevel.ERROR)
        @JvmName(name="-deprecated_encodeUtf8")
        @NotNull
        public final ByteString -deprecated_encodeUtf8(@NotNull String string) {
            Intrinsics.checkNotNullParameter(string, "");
            return this.encodeUtf8(string);
        }

        @Deprecated(message="moved to extension function", replaceWith=@ReplaceWith(expression="buffer.toByteString()", imports={"okio.ByteString.Companion.toByteString"}), level=DeprecationLevel.ERROR)
        @JvmName(name="-deprecated_of")
        @NotNull
        public final ByteString -deprecated_of(@NotNull ByteBuffer byteBuffer) {
            Intrinsics.checkNotNullParameter(byteBuffer, "");
            return this.of(byteBuffer);
        }

        @Deprecated(message="moved to extension function", replaceWith=@ReplaceWith(expression="array.toByteString(offset, byteCount)", imports={"okio.ByteString.Companion.toByteString"}), level=DeprecationLevel.ERROR)
        @JvmName(name="-deprecated_of")
        @NotNull
        public final ByteString -deprecated_of(@NotNull byte[] byArray, int n2, int n3) {
            Intrinsics.checkNotNullParameter(byArray, "");
            return this.of(byArray, n2, n3);
        }

        @Deprecated(message="moved to extension function", replaceWith=@ReplaceWith(expression="inputstream.readByteString(byteCount)", imports={"okio.ByteString.Companion.readByteString"}), level=DeprecationLevel.ERROR)
        @JvmName(name="-deprecated_read")
        @NotNull
        public final ByteString -deprecated_read(@NotNull InputStream inputStream2, int n2) {
            Intrinsics.checkNotNullParameter(inputStream2, "");
            return this.read(inputStream2, n2);
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }
}


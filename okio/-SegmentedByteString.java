/*
 * Decompiled with CFR 0.152.
 */
package okio;

import kotlin.Metadata;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.StringsKt;
import okio.Buffer;
import okio.ByteString;
import okio.internal.-ByteString;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 9, 0}, k=2, xi=48, d1={"\u0000N\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\u0005\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\n\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0002\u001a0\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u00012\u0006\u0010\u000f\u001a\u00020\r2\u0006\u0010\u0010\u001a\u00020\u00012\u0006\u0010\u0011\u001a\u00020\u0001H\u0000\u001a \u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00152\u0006\u0010\u0011\u001a\u00020\u0015H\u0000\u001a\u0019\u0010\u0017\u001a\u00020\u00152\u0006\u0010\f\u001a\u00020\u00012\u0006\u0010\u000f\u001a\u00020\u0015H\u0080\b\u001a\u0019\u0010\u0017\u001a\u00020\u00152\u0006\u0010\f\u001a\u00020\u00152\u0006\u0010\u000f\u001a\u00020\u0001H\u0080\b\u001a\u0010\u0010\u0018\u001a\u00020\u00052\u0006\u0010\u0019\u001a\u00020\u0005H\u0000\u001a\u0015\u0010\u001a\u001a\u00020\u0001*\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u0001H\u0080\f\u001a\u0015\u0010\u001a\u001a\u00020\u0015*\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u0015H\u0080\f\u001a\u0015\u0010\u001a\u001a\u00020\u0015*\u00020\u00012\u0006\u0010\u001c\u001a\u00020\u0015H\u0080\f\u001a\u0015\u0010\u001d\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u001e\u001a\u00020\u0001H\u0080\f\u001a\u0014\u0010\u0018\u001a\u00020\u0001*\u00020\r2\u0006\u0010\u001f\u001a\u00020\u0001H\u0000\u001a\u0014\u0010\u0018\u001a\u00020\u0001*\u00020 2\u0006\u0010!\u001a\u00020\u0001H\u0000\u001a\f\u0010\"\u001a\u00020\u0001*\u00020\u0001H\u0000\u001a\f\u0010\"\u001a\u00020\u0015*\u00020\u0015H\u0000\u001a\f\u0010\"\u001a\u00020#*\u00020#H\u0000\u001a\u0015\u0010$\u001a\u00020\u0015*\u00020\u00152\u0006\u0010\u001e\u001a\u00020\u0001H\u0080\f\u001a\u0015\u0010%\u001a\u00020\u0001*\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u0001H\u0080\f\u001a\u0015\u0010&\u001a\u00020\u0001*\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u0001H\u0080\f\u001a\f\u0010'\u001a\u00020(*\u00020\u001bH\u0000\u001a\f\u0010'\u001a\u00020(*\u00020\u0001H\u0000\u001a\f\u0010'\u001a\u00020(*\u00020\u0015H\u0000\u001a\u0015\u0010)\u001a\u00020\u001b*\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001bH\u0080\f\"\u0014\u0010\u0000\u001a\u00020\u0001X\u0080D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0003\"\u001c\u0010\u0004\u001a\u00020\u00058\u0000X\u0081\u0004\u00a2\u0006\u000e\n\u0000\u0012\u0004\b\u0006\u0010\u0007\u001a\u0004\b\b\u0010\t\u00a8\u0006*"}, d2={"DEFAULT__ByteString_size", "", "getDEFAULT__ByteString_size", "()I", "DEFAULT__new_UnsafeCursor", "Lokio/Buffer$UnsafeCursor;", "getDEFAULT__new_UnsafeCursor$annotations", "()V", "getDEFAULT__new_UnsafeCursor", "()Lokio/Buffer$UnsafeCursor;", "arrayRangeEquals", "", "a", "", "aOffset", "b", "bOffset", "byteCount", "checkOffsetAndCount", "", "size", "", "offset", "minOf", "resolveDefaultParameter", "unsafeCursor", "and", "", "other", "leftRotate", "bitCount", "sizeParam", "Lokio/ByteString;", "position", "reverseBytes", "", "rightRotate", "shl", "shr", "toHexString", "", "xor", "okio"})
@JvmName(name="-SegmentedByteString")
@SourceDebugExtension(value={"SMAP\nUtil.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Util.kt\nokio/-SegmentedByteString\n*L\n1#1,187:1\n68#1:188\n74#1:189\n*S KotlinDebug\n*F\n+ 1 Util.kt\nokio/-SegmentedByteString\n*L\n106#1:188\n107#1:189\n*E\n"})
public final class -SegmentedByteString {
    @NotNull
    private static final Buffer.UnsafeCursor DEFAULT__new_UnsafeCursor = new Buffer.UnsafeCursor();
    private static final int DEFAULT__ByteString_size = -1234567890;

    public static final void checkOffsetAndCount(long l2, long l3, long l4) {
        if ((l3 | l4) < 0L || l3 > l2 || l2 - l3 < l4) {
            throw new ArrayIndexOutOfBoundsException("size=" + l2 + " offset=" + l3 + " byteCount=" + l4);
        }
    }

    public static final short reverseBytes(short s2) {
        int n2 = s2 & 0xFFFF;
        s2 = (short)n2;
        int n3 = n2 >>> 8 & 0xFF | (s2 & 0xFF) << 8;
        s2 = (short)n3;
        return (short)n3;
    }

    public static final int reverseBytes(int n2) {
        return (n2 & 0xFF000000) >>> 24 | (n2 & 0xFF0000) >>> 8 | (n2 & 0xFF00) << 8 | (n2 & 0xFF) << 24;
    }

    public static final long reverseBytes(long l2) {
        return (l2 & 0xFF00000000000000L) >>> 56 | (l2 & 0xFF000000000000L) >>> 40 | (l2 & 0xFF0000000000L) >>> 24 | (l2 & 0xFF00000000L) >>> 8 | (l2 & 0xFF000000L) << 8 | (l2 & 0xFF0000L) << 24 | (l2 & 0xFF00L) << 40 | (l2 & 0xFFL) << 56;
    }

    public static final int leftRotate(int n2, int n3) {
        return n2 << n3 | n2 >>> 32 - n3;
    }

    public static final long rightRotate(long l2, int n2) {
        return l2 >>> n2 | l2 << 64 - n2;
    }

    public static final int shr(byte by, int n2) {
        return by >> n2;
    }

    public static final int shl(byte by, int n2) {
        return by << n2;
    }

    public static final int and(byte by, int n2) {
        return by & n2;
    }

    public static final long and(byte by, long l2) {
        return (long)by & l2;
    }

    public static final byte xor(byte by, byte by2) {
        return (byte)(by ^ by2);
    }

    public static final long and(int n2, long l2) {
        return (long)n2 & l2;
    }

    public static final long minOf(long l2, int n2) {
        return Math.min(l2, (long)n2);
    }

    public static final long minOf(int n2, long l2) {
        return Math.min((long)n2, l2);
    }

    public static final boolean arrayRangeEquals(@NotNull byte[] byArray, int n2, @NotNull byte[] byArray2, int n3, int n4) {
        Intrinsics.checkNotNullParameter(byArray, "");
        Intrinsics.checkNotNullParameter(byArray2, "");
        for (int i2 = 0; i2 < n4; ++i2) {
            if (byArray[i2 + n2] == byArray2[i2 + n3]) continue;
            return false;
        }
        return true;
    }

    @NotNull
    public static final String toHexString(byte by) {
        char[] cArray = new char[2];
        char[] cArray2 = cArray;
        cArray[0] = -ByteString.getHEX_DIGIT_CHARS()[by >> 4 & 0xF];
        cArray2[1] = -ByteString.getHEX_DIGIT_CHARS()[by & 0xF];
        return StringsKt.concatToString(cArray2);
    }

    @NotNull
    public static final String toHexString(int n2) {
        if (n2 == 0) {
            return "0";
        }
        char[] cArray = new char[8];
        char[] cArray2 = cArray;
        cArray[0] = -ByteString.getHEX_DIGIT_CHARS()[n2 >> 28 & 0xF];
        cArray2[1] = -ByteString.getHEX_DIGIT_CHARS()[n2 >> 24 & 0xF];
        cArray2[2] = -ByteString.getHEX_DIGIT_CHARS()[n2 >> 20 & 0xF];
        cArray2[3] = -ByteString.getHEX_DIGIT_CHARS()[n2 >> 16 & 0xF];
        cArray2[4] = -ByteString.getHEX_DIGIT_CHARS()[n2 >> 12 & 0xF];
        cArray2[5] = -ByteString.getHEX_DIGIT_CHARS()[n2 >> 8 & 0xF];
        cArray2[6] = -ByteString.getHEX_DIGIT_CHARS()[n2 >> 4 & 0xF];
        cArray2[7] = -ByteString.getHEX_DIGIT_CHARS()[n2 & 0xF];
        for (n2 = 0; n2 < cArray2.length && cArray2[n2] == '0'; ++n2) {
        }
        return StringsKt.concatToString(cArray2, n2, cArray2.length);
    }

    @NotNull
    public static final String toHexString(long l2) {
        int n2;
        if (l2 == 0L) {
            return "0";
        }
        char[] cArray = new char[16];
        char[] cArray2 = cArray;
        cArray[0] = -ByteString.getHEX_DIGIT_CHARS()[(int)(l2 >> 60 & 0xFL)];
        cArray2[1] = -ByteString.getHEX_DIGIT_CHARS()[(int)(l2 >> 56 & 0xFL)];
        cArray2[2] = -ByteString.getHEX_DIGIT_CHARS()[(int)(l2 >> 52 & 0xFL)];
        cArray2[3] = -ByteString.getHEX_DIGIT_CHARS()[(int)(l2 >> 48 & 0xFL)];
        cArray2[4] = -ByteString.getHEX_DIGIT_CHARS()[(int)(l2 >> 44 & 0xFL)];
        cArray2[5] = -ByteString.getHEX_DIGIT_CHARS()[(int)(l2 >> 40 & 0xFL)];
        cArray2[6] = -ByteString.getHEX_DIGIT_CHARS()[(int)(l2 >> 36 & 0xFL)];
        cArray2[7] = -ByteString.getHEX_DIGIT_CHARS()[(int)(l2 >> 32 & 0xFL)];
        cArray2[8] = -ByteString.getHEX_DIGIT_CHARS()[(int)(l2 >> 28 & 0xFL)];
        cArray2[9] = -ByteString.getHEX_DIGIT_CHARS()[(int)(l2 >> 24 & 0xFL)];
        cArray2[10] = -ByteString.getHEX_DIGIT_CHARS()[(int)(l2 >> 20 & 0xFL)];
        cArray2[11] = -ByteString.getHEX_DIGIT_CHARS()[(int)(l2 >> 16 & 0xFL)];
        cArray2[12] = -ByteString.getHEX_DIGIT_CHARS()[(int)(l2 >> 12 & 0xFL)];
        cArray2[13] = -ByteString.getHEX_DIGIT_CHARS()[(int)(l2 >> 8 & 0xFL)];
        cArray2[14] = -ByteString.getHEX_DIGIT_CHARS()[(int)(l2 >> 4 & 0xFL)];
        cArray2[15] = -ByteString.getHEX_DIGIT_CHARS()[(int)(l2 & 0xFL)];
        for (n2 = 0; n2 < cArray2.length && cArray2[n2] == '0'; ++n2) {
        }
        return StringsKt.concatToString(cArray2, n2, cArray2.length);
    }

    @NotNull
    public static final Buffer.UnsafeCursor getDEFAULT__new_UnsafeCursor() {
        return DEFAULT__new_UnsafeCursor;
    }

    public static /* synthetic */ void getDEFAULT__new_UnsafeCursor$annotations() {
    }

    @NotNull
    public static final Buffer.UnsafeCursor resolveDefaultParameter(@NotNull Buffer.UnsafeCursor unsafeCursor) {
        Intrinsics.checkNotNullParameter(unsafeCursor, "");
        if (unsafeCursor == DEFAULT__new_UnsafeCursor) {
            return new Buffer.UnsafeCursor();
        }
        return unsafeCursor;
    }

    public static final int getDEFAULT__ByteString_size() {
        return DEFAULT__ByteString_size;
    }

    public static final int resolveDefaultParameter(@NotNull ByteString byteString, int n2) {
        Intrinsics.checkNotNullParameter(byteString, "");
        if (n2 == DEFAULT__ByteString_size) {
            return byteString.size();
        }
        return n2;
    }

    public static final int resolveDefaultParameter(@NotNull byte[] byArray, int n2) {
        Intrinsics.checkNotNullParameter(byArray, "");
        if (n2 == DEFAULT__ByteString_size) {
            return byArray.length;
        }
        return n2;
    }
}


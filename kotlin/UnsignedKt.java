/*
 * Decompiled with CFR 0.152.
 */
package kotlin;

import kotlin.Metadata;
import kotlin.PublishedApi;
import kotlin.UInt;
import kotlin.ULong;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.CharsKt;
import org.jetbrains.annotations.NotNull;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(mv={1, 8, 0}, k=2, xi=48, d1={"\u00000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\t\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\u0002\u001a\u0018\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0001\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0004\u001a\u0018\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0002\u001a\u00020\u0003H\u0001\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0007\u001a\u0018\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\tH\u0001\u001a\"\u0010\f\u001a\u00020\u00012\u0006\u0010\n\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\u0001H\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b\r\u0010\u000e\u001a\"\u0010\u000f\u001a\u00020\u00012\u0006\u0010\n\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\u0001H\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0010\u0010\u000e\u001a\u0010\u0010\u0011\u001a\u00020\u00032\u0006\u0010\u0002\u001a\u00020\tH\u0001\u001a\u0018\u0010\u0012\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u00132\u0006\u0010\u000b\u001a\u00020\u0013H\u0001\u001a\"\u0010\u0014\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\u0006H\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0015\u0010\u0016\u001a\"\u0010\u0017\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\u0006H\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0018\u0010\u0016\u001a\u0010\u0010\u0019\u001a\u00020\u00032\u0006\u0010\u0002\u001a\u00020\u0013H\u0001\u001a\u0010\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u0002\u001a\u00020\u0013H\u0000\u001a\u0018\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u0002\u001a\u00020\u00132\u0006\u0010\u001c\u001a\u00020\tH\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u001d"}, d2={"doubleToUInt", "Lkotlin/UInt;", "v", "", "(D)I", "doubleToULong", "Lkotlin/ULong;", "(D)J", "uintCompare", "", "v1", "v2", "uintDivide", "uintDivide-J1ME1BU", "(II)I", "uintRemainder", "uintRemainder-J1ME1BU", "uintToDouble", "ulongCompare", "", "ulongDivide", "ulongDivide-eb3DHEI", "(JJ)J", "ulongRemainder", "ulongRemainder-eb3DHEI", "ulongToDouble", "ulongToString", "", "base", "kotlin-stdlib"})
@JvmName(name="UnsignedKt")
public final class UnsignedKt {
    @PublishedApi
    public static final int uintCompare(int n2, int n3) {
        return Intrinsics.compare(n2 ^ Integer.MIN_VALUE, n3 ^ Integer.MIN_VALUE);
    }

    @PublishedApi
    public static final int ulongCompare(long l2, long l3) {
        return Intrinsics.compare(l2 ^ Long.MIN_VALUE, l3 ^ Long.MIN_VALUE);
    }

    @PublishedApi
    public static final int uintDivide-J1ME1BU(int n2, int n3) {
        return UInt.constructor-impl((int)(((long)n2 & 0xFFFFFFFFL) / ((long)n3 & 0xFFFFFFFFL)));
    }

    @PublishedApi
    public static final int uintRemainder-J1ME1BU(int n2, int n3) {
        return UInt.constructor-impl((int)(((long)n2 & 0xFFFFFFFFL) % ((long)n3 & 0xFFFFFFFFL)));
    }

    @PublishedApi
    public static final long ulongDivide-eb3DHEI(long l2, long l3) {
        long l4;
        if (l3 < 0L) {
            if (Long.compareUnsigned(l2, l3) < 0) {
                return ULong.constructor-impl(0L);
            }
            return ULong.constructor-impl(1L);
        }
        if (l2 >= 0L) {
            return ULong.constructor-impl(l2 / l3);
        }
        long l5 = l2 - (l4 = (l2 >>> 1) / l3 << 1) * l3;
        return ULong.constructor-impl(l4 + (long)(Long.compareUnsigned(ULong.constructor-impl(l5), ULong.constructor-impl(l3)) >= 0 ? 1 : 0));
    }

    @PublishedApi
    public static final long ulongRemainder-eb3DHEI(long l2, long l3) {
        long l4;
        if (l3 < 0L) {
            if (Long.compareUnsigned(l2, l3) < 0) {
                return l2;
            }
            return ULong.constructor-impl(l2 - l3);
        }
        if (l2 >= 0L) {
            return ULong.constructor-impl(l2 % l3);
        }
        long l5 = (l2 >>> 1) / l3 << 1;
        return ULong.constructor-impl(l4 - (Long.compareUnsigned(ULong.constructor-impl(l4 = l2 - l5 * l3), ULong.constructor-impl(l3)) >= 0 ? l3 : 0L));
    }

    @PublishedApi
    public static final int doubleToUInt(double d2) {
        if (Double.isNaN(d2)) {
            return 0;
        }
        if (d2 <= UnsignedKt.uintToDouble(0)) {
            return 0;
        }
        if (d2 >= UnsignedKt.uintToDouble(-1)) {
            return -1;
        }
        if (d2 <= 2.147483647E9) {
            return UInt.constructor-impl((int)d2);
        }
        return UInt.constructor-impl(UInt.constructor-impl((int)(d2 - 2.147483647E9)) + UInt.constructor-impl(Integer.MAX_VALUE));
    }

    @PublishedApi
    public static final long doubleToULong(double d2) {
        if (Double.isNaN(d2)) {
            return 0L;
        }
        if (d2 <= UnsignedKt.ulongToDouble(0L)) {
            return 0L;
        }
        if (d2 >= UnsignedKt.ulongToDouble(-1L)) {
            return -1L;
        }
        if (d2 < 9.223372036854776E18) {
            return ULong.constructor-impl((long)d2);
        }
        return ULong.constructor-impl(ULong.constructor-impl((long)(d2 - 9.223372036854776E18)) + Long.MIN_VALUE);
    }

    @PublishedApi
    public static final double uintToDouble(int n2) {
        return (double)(n2 & Integer.MAX_VALUE) + (double)(n2 >>> 31 << 30) * 2.0;
    }

    @PublishedApi
    public static final double ulongToDouble(long l2) {
        return (double)(l2 >>> 11) * 2048.0 + (double)(l2 & 0x7FFL);
    }

    @NotNull
    public static final String ulongToString(long l2) {
        return UnsignedKt.ulongToString(l2, 10);
    }

    @NotNull
    public static final String ulongToString(long l2, int n2) {
        if (l2 >= 0L) {
            String string = Long.toString(l2, CharsKt.checkRadix(n2));
            Intrinsics.checkNotNullExpressionValue(string, "");
            return string;
        }
        long l3 = (l2 >>> 1) / (long)n2 << 1;
        long l4 = l2 - l3 * (long)n2;
        if (l4 >= (long)n2) {
            l4 -= (long)n2;
            ++l3;
        }
        StringBuilder stringBuilder = new StringBuilder();
        String string = Long.toString(l3, CharsKt.checkRadix(n2));
        Intrinsics.checkNotNullExpressionValue(string, "");
        StringBuilder stringBuilder2 = stringBuilder.append(string);
        String string2 = Long.toString(l4, CharsKt.checkRadix(n2));
        Intrinsics.checkNotNullExpressionValue(string2, "");
        return stringBuilder2.append(string2).toString();
    }
}


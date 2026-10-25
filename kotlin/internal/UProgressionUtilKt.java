/*
 * Decompiled with CFR 0.152.
 */
package kotlin.internal;

import kotlin.Metadata;
import kotlin.PublishedApi;
import kotlin.SinceKotlin;
import kotlin.UInt;
import kotlin.ULong;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(mv={1, 8, 0}, k=2, xi=48, d1={"\u0000 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\u001a*\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u0001H\u0002\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0005\u0010\u0006\u001a*\u0010\u0000\u001a\u00020\u00072\u0006\u0010\u0002\u001a\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u00072\u0006\u0010\u0004\u001a\u00020\u0007H\u0002\u00f8\u0001\u0000\u00a2\u0006\u0004\b\b\u0010\t\u001a*\u0010\n\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\u00012\u0006\u0010\f\u001a\u00020\u00012\u0006\u0010\r\u001a\u00020\u000eH\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u000f\u0010\u0006\u001a*\u0010\n\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\u0010H\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0011\u0010\t\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0012"}, d2={"differenceModulo", "Lkotlin/UInt;", "a", "b", "c", "differenceModulo-WZ9TVnA", "(III)I", "Lkotlin/ULong;", "differenceModulo-sambcqE", "(JJJ)J", "getProgressionLastElement", "start", "end", "step", "", "getProgressionLastElement-Nkh28Cs", "", "getProgressionLastElement-7ftBX0g", "kotlin-stdlib"})
public final class UProgressionUtilKt {
    private static final int differenceModulo-WZ9TVnA(int n2, int n3, int n4) {
        if (Integer.compareUnsigned(n2 = Integer.remainderUnsigned(n2, n4), n3 = Integer.remainderUnsigned(n3, n4)) >= 0) {
            return UInt.constructor-impl(n2 - n3);
        }
        return UInt.constructor-impl(UInt.constructor-impl(n2 - n3) + n4);
    }

    private static final long differenceModulo-sambcqE(long l2, long l3, long l4) {
        long l5;
        long l6 = Long.remainderUnsigned(l2, l4);
        if (Long.compareUnsigned(l6, l5 = Long.remainderUnsigned(l3, l4)) >= 0) {
            return ULong.constructor-impl(l6 - l5);
        }
        return ULong.constructor-impl(ULong.constructor-impl(l6 - l5) + l4);
    }

    @PublishedApi
    @SinceKotlin(version="1.3")
    public static final int getProgressionLastElement-Nkh28Cs(int n2, int n3, int n4) {
        if (n4 > 0) {
            if (Integer.compareUnsigned(n2, n3) >= 0) {
                return n3;
            }
            int n5 = n3;
            return UInt.constructor-impl(n5 - UProgressionUtilKt.differenceModulo-WZ9TVnA(n5, n2, UInt.constructor-impl(n4)));
        }
        if (n4 < 0) {
            if (Integer.compareUnsigned(n2, n3) <= 0) {
                return n3;
            }
            return UInt.constructor-impl(n3 + UProgressionUtilKt.differenceModulo-WZ9TVnA(n2, n3, UInt.constructor-impl(-n4)));
        }
        throw new IllegalArgumentException("Step is zero.");
    }

    @PublishedApi
    @SinceKotlin(version="1.3")
    public static final long getProgressionLastElement-7ftBX0g(long l2, long l3, long l4) {
        if (l4 > 0L) {
            if (Long.compareUnsigned(l2, l3) >= 0) {
                return l3;
            }
            long l5 = l3;
            return ULong.constructor-impl(l5 - UProgressionUtilKt.differenceModulo-sambcqE(l5, l2, ULong.constructor-impl(l4)));
        }
        if (l4 < 0L) {
            if (Long.compareUnsigned(l2, l3) <= 0) {
                return l3;
            }
            return ULong.constructor-impl(l3 + UProgressionUtilKt.differenceModulo-sambcqE(l2, l3, ULong.constructor-impl(-l4)));
        }
        throw new IllegalArgumentException("Step is zero.");
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package kotlin.collections;

import kotlin.ExperimentalUnsignedTypes;
import kotlin.Metadata;
import kotlin.UByteArray;
import kotlin.UIntArray;
import kotlin.ULongArray;
import kotlin.UShortArray;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(mv={1, 8, 0}, k=2, xi=48, d1={"\u00000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0010\u001a*\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0006\u0010\u0007\u001a*\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\t\u0010\n\u001a*\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\f\u0010\r\u001a*\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u000e2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u000f\u0010\u0010\u001a*\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0013\u0010\u0014\u001a*\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0015\u0010\u0016\u001a*\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0017\u0010\u0018\u001a*\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u000e2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0019\u0010\u001a\u001a*\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u001c\u001a\u00020\u00012\u0006\u0010\u001d\u001a\u00020\u0001H\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001e\u0010\u0014\u001a*\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u001c\u001a\u00020\u00012\u0006\u0010\u001d\u001a\u00020\u0001H\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001f\u0010\u0016\u001a*\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u000b2\u0006\u0010\u001c\u001a\u00020\u00012\u0006\u0010\u001d\u001a\u00020\u0001H\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b \u0010\u0018\u001a*\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u000e2\u0006\u0010\u001c\u001a\u00020\u00012\u0006\u0010\u001d\u001a\u00020\u0001H\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b!\u0010\u001a\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\""}, d2={"partition", "", "array", "Lkotlin/UByteArray;", "left", "right", "partition-4UcCI2c", "([BII)I", "Lkotlin/UIntArray;", "partition-oBK06Vg", "([III)I", "Lkotlin/ULongArray;", "partition--nroSd4", "([JII)I", "Lkotlin/UShortArray;", "partition-Aa5vz7o", "([SII)I", "quickSort", "", "quickSort-4UcCI2c", "([BII)V", "quickSort-oBK06Vg", "([III)V", "quickSort--nroSd4", "([JII)V", "quickSort-Aa5vz7o", "([SII)V", "sortArray", "fromIndex", "toIndex", "sortArray-4UcCI2c", "sortArray-oBK06Vg", "sortArray--nroSd4", "sortArray-Aa5vz7o", "kotlin-stdlib"})
public final class UArraySortingKt {
    @ExperimentalUnsignedTypes
    private static final int partition-4UcCI2c(byte[] byArray, int n2, int n3) {
        int n4 = n2;
        int n5 = n3;
        n2 = UByteArray.get-w2LRezQ(byArray, (n2 + n3) / 2);
        while (n4 <= n5) {
            while (Intrinsics.compare(UByteArray.get-w2LRezQ(byArray, n4) & 0xFF, n2 & 0xFF) < 0) {
                ++n4;
            }
            while (Intrinsics.compare(UByteArray.get-w2LRezQ(byArray, n5) & 0xFF, n2 & 0xFF) > 0) {
                --n5;
            }
            if (n4 > n5) continue;
            n3 = UByteArray.get-w2LRezQ(byArray, n4);
            UByteArray.set-VurrAj0(byArray, n4, UByteArray.get-w2LRezQ(byArray, n5));
            UByteArray.set-VurrAj0(byArray, n5, (byte)n3);
            ++n4;
            --n5;
        }
        return n4;
    }

    @ExperimentalUnsignedTypes
    private static final void quickSort-4UcCI2c(byte[] byArray, int n2, int n3) {
        while (true) {
            int n4;
            if (n2 < (n4 = UArraySortingKt.partition-4UcCI2c(byArray, n2, n3)) - 1) {
                UArraySortingKt.quickSort-4UcCI2c(byArray, n2, n4 - 1);
            }
            if (n4 >= n3) break;
            n2 = n4;
        }
    }

    @ExperimentalUnsignedTypes
    private static final int partition-Aa5vz7o(short[] sArray, int n2, int n3) {
        int n4 = n2;
        int n5 = n3;
        n2 = UShortArray.get-Mh2AYeg(sArray, (n2 + n3) / 2);
        while (n4 <= n5) {
            while (Intrinsics.compare(UShortArray.get-Mh2AYeg(sArray, n4) & 0xFFFF, n2 & 0xFFFF) < 0) {
                ++n4;
            }
            while (Intrinsics.compare(UShortArray.get-Mh2AYeg(sArray, n5) & 0xFFFF, n2 & 0xFFFF) > 0) {
                --n5;
            }
            if (n4 > n5) continue;
            n3 = UShortArray.get-Mh2AYeg(sArray, n4);
            UShortArray.set-01HTLdE(sArray, n4, UShortArray.get-Mh2AYeg(sArray, n5));
            UShortArray.set-01HTLdE(sArray, n5, (short)n3);
            ++n4;
            --n5;
        }
        return n4;
    }

    @ExperimentalUnsignedTypes
    private static final void quickSort-Aa5vz7o(short[] sArray, int n2, int n3) {
        while (true) {
            int n4;
            if (n2 < (n4 = UArraySortingKt.partition-Aa5vz7o(sArray, n2, n3)) - 1) {
                UArraySortingKt.quickSort-Aa5vz7o(sArray, n2, n4 - 1);
            }
            if (n4 >= n3) break;
            n2 = n4;
        }
    }

    @ExperimentalUnsignedTypes
    private static final int partition-oBK06Vg(int[] nArray, int n2, int n3) {
        int n4 = n2;
        int n5 = n3;
        n2 = UIntArray.get-pVg5ArA(nArray, (n2 + n3) / 2);
        while (n4 <= n5) {
            while (Integer.compareUnsigned(UIntArray.get-pVg5ArA(nArray, n4), n2) < 0) {
                ++n4;
            }
            while (Integer.compareUnsigned(UIntArray.get-pVg5ArA(nArray, n5), n2) > 0) {
                --n5;
            }
            if (n4 > n5) continue;
            n3 = UIntArray.get-pVg5ArA(nArray, n4);
            UIntArray.set-VXSXFK8(nArray, n4, UIntArray.get-pVg5ArA(nArray, n5));
            UIntArray.set-VXSXFK8(nArray, n5, n3);
            ++n4;
            --n5;
        }
        return n4;
    }

    @ExperimentalUnsignedTypes
    private static final void quickSort-oBK06Vg(int[] nArray, int n2, int n3) {
        while (true) {
            int n4;
            if (n2 < (n4 = UArraySortingKt.partition-oBK06Vg(nArray, n2, n3)) - 1) {
                UArraySortingKt.quickSort-oBK06Vg(nArray, n2, n4 - 1);
            }
            if (n4 >= n3) break;
            n2 = n4;
        }
    }

    @ExperimentalUnsignedTypes
    private static final int partition--nroSd4(long[] lArray, int n2, int n3) {
        int n4 = n2;
        int n5 = n3;
        long l2 = ULongArray.get-s-VKNKU(lArray, (n2 + n3) / 2);
        while (n4 <= n5) {
            while (Long.compareUnsigned(ULongArray.get-s-VKNKU(lArray, n4), l2) < 0) {
                ++n4;
            }
            while (Long.compareUnsigned(ULongArray.get-s-VKNKU(lArray, n5), l2) > 0) {
                --n5;
            }
            if (n4 > n5) continue;
            long l3 = ULongArray.get-s-VKNKU(lArray, n4);
            ULongArray.set-k8EXiF4(lArray, n4, ULongArray.get-s-VKNKU(lArray, n5));
            ULongArray.set-k8EXiF4(lArray, n5, l3);
            ++n4;
            --n5;
        }
        return n4;
    }

    @ExperimentalUnsignedTypes
    private static final void quickSort--nroSd4(long[] lArray, int n2, int n3) {
        while (true) {
            int n4;
            if (n2 < (n4 = UArraySortingKt.partition--nroSd4(lArray, n2, n3)) - 1) {
                UArraySortingKt.quickSort--nroSd4(lArray, n2, n4 - 1);
            }
            if (n4 >= n3) break;
            n2 = n4;
        }
    }

    @ExperimentalUnsignedTypes
    public static final void sortArray-4UcCI2c(@NotNull byte[] byArray, int n2, int n3) {
        Intrinsics.checkNotNullParameter(byArray, "");
        UArraySortingKt.quickSort-4UcCI2c(byArray, n2, n3 - 1);
    }

    @ExperimentalUnsignedTypes
    public static final void sortArray-Aa5vz7o(@NotNull short[] sArray, int n2, int n3) {
        Intrinsics.checkNotNullParameter(sArray, "");
        UArraySortingKt.quickSort-Aa5vz7o(sArray, n2, n3 - 1);
    }

    @ExperimentalUnsignedTypes
    public static final void sortArray-oBK06Vg(@NotNull int[] nArray, int n2, int n3) {
        Intrinsics.checkNotNullParameter(nArray, "");
        UArraySortingKt.quickSort-oBK06Vg(nArray, n2, n3 - 1);
    }

    @ExperimentalUnsignedTypes
    public static final void sortArray--nroSd4(@NotNull long[] lArray, int n2, int n3) {
        Intrinsics.checkNotNullParameter(lArray, "");
        UArraySortingKt.quickSort--nroSd4(lArray, n2, n3 - 1);
    }
}


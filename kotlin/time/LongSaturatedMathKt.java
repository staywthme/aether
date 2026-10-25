/*
 * Decompiled with CFR 0.152.
 */
package kotlin.time;

import kotlin.Metadata;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.time.Duration;
import kotlin.time.DurationKt;
import kotlin.time.DurationUnit;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(mv={1, 8, 0}, k=2, xi=48, d1={"\u0000\u0018\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0012\n\u0002\u0010\u000b\n\u0000\u001a*\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0001H\u0002\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0006\u0010\u0007\u001a\"\u0010\b\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0000\u00f8\u0001\u0000\u00a2\u0006\u0004\b\t\u0010\n\u001a\"\u0010\u000b\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0002\u00f8\u0001\u0000\u00a2\u0006\u0004\b\f\u0010\n\u001a \u0010\r\u001a\u00020\u00042\u0006\u0010\u000e\u001a\u00020\u00012\u0006\u0010\u000f\u001a\u00020\u0001H\u0000\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\n\u001a \u0010\u0010\u001a\u00020\u00042\u0006\u0010\u0011\u001a\u00020\u00012\u0006\u0010\u0012\u001a\u00020\u0001H\u0002\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\n\u001a \u0010\u0013\u001a\u00020\u00042\u0006\u0010\u0014\u001a\u00020\u00012\u0006\u0010\u0015\u001a\u00020\u0001H\u0000\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\n\u001a\r\u0010\u0016\u001a\u00020\u0017*\u00020\u0001H\u0082\b\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0018"}, d2={"checkInfiniteSumDefined", "", "longNs", "duration", "Lkotlin/time/Duration;", "durationNs", "checkInfiniteSumDefined-PjuGub4", "(JJJ)J", "saturatingAdd", "saturatingAdd-pTJri5U", "(JJ)J", "saturatingAddInHalves", "saturatingAddInHalves-pTJri5U", "saturatingDiff", "valueNs", "originNs", "saturatingFiniteDiff", "value1Ns", "value2Ns", "saturatingOriginsDiff", "origin1Ns", "origin2Ns", "isSaturated", "", "kotlin-stdlib"})
@SourceDebugExtension(value={"SMAP\nlongSaturatedMath.kt\nKotlin\n*S Kotlin\n*F\n+ 1 longSaturatedMath.kt\nkotlin/time/LongSaturatedMathKt\n*L\n1#1,75:1\n74#1:76\n74#1:77\n74#1:78\n74#1:79\n74#1:80\n74#1:81\n*S KotlinDebug\n*F\n+ 1 longSaturatedMath.kt\nkotlin/time/LongSaturatedMathKt\n*L\n15#1:76\n18#1:77\n36#1:78\n45#1:79\n52#1:80\n56#1:81\n*E\n"})
public final class LongSaturatedMathKt {
    public static final long saturatingAdd-pTJri5U(long l2, long l3) {
        long l4 = Duration.getInWholeNanoseconds-impl(l3);
        if ((l2 - 1L | 1L) == Long.MAX_VALUE) {
            return LongSaturatedMathKt.checkInfiniteSumDefined-PjuGub4(l2, l3, l4);
        }
        if ((l4 - 1L | 1L) == Long.MAX_VALUE) {
            return LongSaturatedMathKt.saturatingAddInHalves-pTJri5U(l2, l3);
        }
        long l5 = l2 + l4;
        if (((l2 ^ l5) & (l4 ^ l5)) < 0L) {
            if (l2 < 0L) {
                return Long.MIN_VALUE;
            }
            return Long.MAX_VALUE;
        }
        return l5;
    }

    private static final long checkInfiniteSumDefined-PjuGub4(long l2, long l3, long l4) {
        if (Duration.isInfinite-impl(l3) && (l2 ^ l4) < 0L) {
            throw new IllegalArgumentException("Summing infinities of different signs");
        }
        return l2;
    }

    private static final long saturatingAddInHalves-pTJri5U(long l2, long l3) {
        long l4 = Duration.div-UwyO8pc(l3, 2);
        long l5 = Duration.getInWholeNanoseconds-impl(l4);
        if ((l5 - 1L | 1L) == Long.MAX_VALUE) {
            return (long)((double)l2 + Duration.toDouble-impl(l3, DurationUnit.NANOSECONDS));
        }
        return LongSaturatedMathKt.saturatingAdd-pTJri5U(LongSaturatedMathKt.saturatingAdd-pTJri5U(l2, l4), Duration.minus-LRDsOJo(l3, l4));
    }

    public static final long saturatingDiff(long l2, long l3) {
        if ((l3 - 1L | 1L) == Long.MAX_VALUE) {
            return Duration.unaryMinus-UwyO8pc(DurationKt.toDuration(l3, DurationUnit.DAYS));
        }
        return LongSaturatedMathKt.saturatingFiniteDiff(l2, l3);
    }

    public static final long saturatingOriginsDiff(long l2, long l3) {
        if ((l3 - 1L | 1L) == Long.MAX_VALUE) {
            if (l2 == l3) {
                return Duration.Companion.getZERO-UwyO8pc();
            }
            return Duration.unaryMinus-UwyO8pc(DurationKt.toDuration(l3, DurationUnit.DAYS));
        }
        if ((l2 - 1L | 1L) == Long.MAX_VALUE) {
            return DurationKt.toDuration(l2, DurationUnit.DAYS);
        }
        return LongSaturatedMathKt.saturatingFiniteDiff(l2, l3);
    }

    private static final long saturatingFiniteDiff(long l2, long l3) {
        long l4 = l2 - l3;
        if (((l4 ^ l2) & (l4 ^ l3 ^ 0xFFFFFFFFFFFFFFFFL)) < 0L) {
            long l5 = l2 / 1000000L - l3 / 1000000L;
            long l6 = l2 % 1000000L - l3 % 1000000L;
            return Duration.plus-LRDsOJo(DurationKt.toDuration(l5, DurationUnit.MILLISECONDS), DurationKt.toDuration(l6, DurationUnit.NANOSECONDS));
        }
        return DurationKt.toDuration(l4, DurationUnit.NANOSECONDS);
    }

    private static final boolean isSaturated(long l2) {
        return (l2 - 1L | 1L) == Long.MAX_VALUE;
    }
}


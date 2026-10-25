/*
 * Decompiled with CFR 0.152.
 */
package kotlin.random;

import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.random.Random;
import kotlin.random.XorWowRandom;
import kotlin.ranges.IntProgression;
import kotlin.ranges.IntRange;
import kotlin.ranges.LongProgression;
import kotlin.ranges.LongRange;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 8, 0}, k=2, xi=48, d1={"\u0000:\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\u0010\u0006\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\u0010\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0007\u001a\u0010\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0004H\u0007\u001a\u0018\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\bH\u0000\u001a\u0018\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0007\u001a\u00020\f2\u0006\u0010\t\u001a\u00020\fH\u0000\u001a\u0018\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0007\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\u0003H\u0000\u001a\u0018\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0007\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\u0004H\u0000\u001a\u0010\u0010\r\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u0003H\u0000\u001a\u0014\u0010\u000f\u001a\u00020\u0003*\u00020\u00012\u0006\u0010\u0010\u001a\u00020\u0011H\u0007\u001a\u0014\u0010\u0012\u001a\u00020\u0004*\u00020\u00012\u0006\u0010\u0010\u001a\u00020\u0013H\u0007\u001a\u0014\u0010\u0014\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0015\u001a\u00020\u0003H\u0000\u00a8\u0006\u0016"}, d2={"Random", "Lkotlin/random/Random;", "seed", "", "", "boundsErrorMessage", "", "from", "", "until", "checkRangeBounds", "", "", "fastLog2", "value", "nextInt", "range", "Lkotlin/ranges/IntRange;", "nextLong", "Lkotlin/ranges/LongRange;", "takeUpperBits", "bitCount", "kotlin-stdlib"})
@SourceDebugExtension(value={"SMAP\nRandom.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Random.kt\nkotlin/random/RandomKt\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,383:1\n1#2:384\n*E\n"})
public final class RandomKt {
    @SinceKotlin(version="1.3")
    @NotNull
    public static final Random Random(int n2) {
        int n3 = n2;
        return new XorWowRandom(n3, n3 >> 31);
    }

    @SinceKotlin(version="1.3")
    @NotNull
    public static final Random Random(long l2) {
        return new XorWowRandom((int)l2, (int)(l2 >> 32));
    }

    @SinceKotlin(version="1.3")
    public static final int nextInt(@NotNull Random random, @NotNull IntRange intRange) {
        Intrinsics.checkNotNullParameter(random, "");
        Intrinsics.checkNotNullParameter(intRange, "");
        if (((IntProgression)intRange).isEmpty()) {
            throw new IllegalArgumentException("Cannot get random in empty range: " + intRange);
        }
        if (intRange.getLast() < Integer.MAX_VALUE) {
            return random.nextInt(intRange.getFirst(), intRange.getLast() + 1);
        }
        if (intRange.getFirst() > Integer.MIN_VALUE) {
            return random.nextInt(intRange.getFirst() - 1, intRange.getLast()) + 1;
        }
        return random.nextInt();
    }

    @SinceKotlin(version="1.3")
    public static final long nextLong(@NotNull Random random, @NotNull LongRange longRange) {
        Intrinsics.checkNotNullParameter(random, "");
        Intrinsics.checkNotNullParameter(longRange, "");
        if (((LongProgression)longRange).isEmpty()) {
            throw new IllegalArgumentException("Cannot get random in empty range: " + longRange);
        }
        if (longRange.getLast() < Long.MAX_VALUE) {
            return random.nextLong(longRange.getFirst(), longRange.getLast() + 1L);
        }
        if (longRange.getFirst() > Long.MIN_VALUE) {
            return random.nextLong(longRange.getFirst() - 1L, longRange.getLast()) + 1L;
        }
        return random.nextLong();
    }

    public static final int fastLog2(int n2) {
        return 31 - Integer.numberOfLeadingZeros(n2);
    }

    public static final int takeUpperBits(int n2, int n3) {
        return n2 >>> 32 - n3 & -n3 >> 31;
    }

    public static final void checkRangeBounds(int n2, int n3) {
        boolean bl = n3 > n2;
        if (!bl) {
            String string = RandomKt.boundsErrorMessage(n2, n3);
            throw new IllegalArgumentException(string.toString());
        }
    }

    public static final void checkRangeBounds(long l2, long l3) {
        boolean bl = l3 > l2;
        if (!bl) {
            String string = RandomKt.boundsErrorMessage(l2, l3);
            throw new IllegalArgumentException(string.toString());
        }
    }

    public static final void checkRangeBounds(double d2, double d3) {
        boolean bl = d3 > d2;
        if (!bl) {
            String string = RandomKt.boundsErrorMessage(d2, d3);
            throw new IllegalArgumentException(string.toString());
        }
    }

    @NotNull
    public static final String boundsErrorMessage(@NotNull Object object, @NotNull Object object2) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(object2, "");
        return "Random range is empty: [" + object + ", " + object2 + ").";
    }
}


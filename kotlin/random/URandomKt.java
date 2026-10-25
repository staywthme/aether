/*
 * Decompiled with CFR 0.152.
 */
package kotlin.random;

import kotlin.ExperimentalUnsignedTypes;
import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.UByteArray;
import kotlin.UInt;
import kotlin.ULong;
import kotlin.WasExperimental;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.random.Random;
import kotlin.random.RandomKt;
import kotlin.ranges.UIntProgression;
import kotlin.ranges.UIntRange;
import kotlin.ranges.ULongProgression;
import kotlin.ranges.ULongRange;
import org.jetbrains.annotations.NotNull;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(mv={1, 8, 0}, k=2, xi=48, d1={"\u0000:\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\"\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0003H\u0000\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0005\u0010\u0006\u001a\"\u0010\u0007\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\bH\u0000\u00f8\u0001\u0000\u00a2\u0006\u0004\b\t\u0010\n\u001a\u001c\u0010\u000b\u001a\u00020\f*\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0010\u001a\u001e\u0010\u000b\u001a\u00020\f*\u00020\r2\u0006\u0010\u0011\u001a\u00020\fH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0012\u0010\u0013\u001a2\u0010\u000b\u001a\u00020\f*\u00020\r2\u0006\u0010\u0011\u001a\u00020\f2\b\b\u0002\u0010\u0014\u001a\u00020\u000f2\b\b\u0002\u0010\u0015\u001a\u00020\u000fH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0016\u0010\u0017\u001a\u0014\u0010\u0018\u001a\u00020\u0003*\u00020\rH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0019\u001a\u001e\u0010\u0018\u001a\u00020\u0003*\u00020\r2\u0006\u0010\u0004\u001a\u00020\u0003H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001a\u0010\u001b\u001a&\u0010\u0018\u001a\u00020\u0003*\u00020\r2\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0003H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001c\u0010\u001d\u001a\u001c\u0010\u0018\u001a\u00020\u0003*\u00020\r2\u0006\u0010\u001e\u001a\u00020\u001fH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010 \u001a\u0014\u0010!\u001a\u00020\b*\u00020\rH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\"\u001a\u001e\u0010!\u001a\u00020\b*\u00020\r2\u0006\u0010\u0004\u001a\u00020\bH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b#\u0010$\u001a&\u0010!\u001a\u00020\b*\u00020\r2\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\bH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b%\u0010&\u001a\u001c\u0010!\u001a\u00020\b*\u00020\r2\u0006\u0010\u001e\u001a\u00020'H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010(\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006)"}, d2={"checkUIntRangeBounds", "", "from", "Lkotlin/UInt;", "until", "checkUIntRangeBounds-J1ME1BU", "(II)V", "checkULongRangeBounds", "Lkotlin/ULong;", "checkULongRangeBounds-eb3DHEI", "(JJ)V", "nextUBytes", "Lkotlin/UByteArray;", "Lkotlin/random/Random;", "size", "", "(Lkotlin/random/Random;I)[B", "array", "nextUBytes-EVgfTAA", "(Lkotlin/random/Random;[B)[B", "fromIndex", "toIndex", "nextUBytes-Wvrt4B4", "(Lkotlin/random/Random;[BII)[B", "nextUInt", "(Lkotlin/random/Random;)I", "nextUInt-qCasIEU", "(Lkotlin/random/Random;I)I", "nextUInt-a8DCA5k", "(Lkotlin/random/Random;II)I", "range", "Lkotlin/ranges/UIntRange;", "(Lkotlin/random/Random;Lkotlin/ranges/UIntRange;)I", "nextULong", "(Lkotlin/random/Random;)J", "nextULong-V1Xi4fY", "(Lkotlin/random/Random;J)J", "nextULong-jmpaW-c", "(Lkotlin/random/Random;JJ)J", "Lkotlin/ranges/ULongRange;", "(Lkotlin/random/Random;Lkotlin/ranges/ULongRange;)J", "kotlin-stdlib"})
@SourceDebugExtension(value={"SMAP\nURandom.kt\nKotlin\n*S Kotlin\n*F\n+ 1 URandom.kt\nkotlin/random/URandomKt\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,155:1\n1#2:156\n*E\n"})
public final class URandomKt {
    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    public static final int nextUInt(@NotNull Random random) {
        Intrinsics.checkNotNullParameter(random, "");
        return UInt.constructor-impl(random.nextInt());
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    public static final int nextUInt-qCasIEU(@NotNull Random random, int n2) {
        Intrinsics.checkNotNullParameter(random, "");
        return URandomKt.nextUInt-a8DCA5k(random, 0, n2);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    public static final int nextUInt-a8DCA5k(@NotNull Random random, int n2, int n3) {
        Intrinsics.checkNotNullParameter(random, "");
        URandomKt.checkUIntRangeBounds-J1ME1BU(n2, n3);
        int n4 = random.nextInt(n2 ^= Integer.MIN_VALUE, n3 ^= Integer.MIN_VALUE) ^ Integer.MIN_VALUE;
        return UInt.constructor-impl(n4);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    public static final int nextUInt(@NotNull Random random, @NotNull UIntRange uIntRange) {
        Intrinsics.checkNotNullParameter(random, "");
        Intrinsics.checkNotNullParameter(uIntRange, "");
        if (((UIntProgression)uIntRange).isEmpty()) {
            throw new IllegalArgumentException("Cannot get random in empty range: " + uIntRange);
        }
        if (Integer.compareUnsigned(uIntRange.getLast-pVg5ArA(), -1) < 0) {
            return URandomKt.nextUInt-a8DCA5k(random, uIntRange.getFirst-pVg5ArA(), UInt.constructor-impl(uIntRange.getLast-pVg5ArA() + 1));
        }
        if (Integer.compareUnsigned(uIntRange.getFirst-pVg5ArA(), 0) > 0) {
            return UInt.constructor-impl(URandomKt.nextUInt-a8DCA5k(random, UInt.constructor-impl(uIntRange.getFirst-pVg5ArA() - 1), uIntRange.getLast-pVg5ArA()) + 1);
        }
        return URandomKt.nextUInt(random);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    public static final long nextULong(@NotNull Random random) {
        Intrinsics.checkNotNullParameter(random, "");
        return ULong.constructor-impl(random.nextLong());
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    public static final long nextULong-V1Xi4fY(@NotNull Random random, long l2) {
        Intrinsics.checkNotNullParameter(random, "");
        return URandomKt.nextULong-jmpaW-c(random, 0L, l2);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    public static final long nextULong-jmpaW-c(@NotNull Random random, long l2, long l3) {
        Intrinsics.checkNotNullParameter(random, "");
        URandomKt.checkULongRangeBounds-eb3DHEI(l2, l3);
        long l4 = l2 ^ Long.MIN_VALUE;
        long l5 = l3 ^ Long.MIN_VALUE;
        long l6 = random.nextLong(l4, l5) ^ Long.MIN_VALUE;
        return ULong.constructor-impl(l6);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    public static final long nextULong(@NotNull Random random, @NotNull ULongRange uLongRange) {
        Intrinsics.checkNotNullParameter(random, "");
        Intrinsics.checkNotNullParameter(uLongRange, "");
        if (((ULongProgression)uLongRange).isEmpty()) {
            throw new IllegalArgumentException("Cannot get random in empty range: " + uLongRange);
        }
        if (Long.compareUnsigned(uLongRange.getLast-s-VKNKU(), -1L) < 0) {
            long l2 = uLongRange.getLast-s-VKNKU();
            return URandomKt.nextULong-jmpaW-c(random, uLongRange.getFirst-s-VKNKU(), ULong.constructor-impl(l2 + ULong.constructor-impl(1L)));
        }
        if (Long.compareUnsigned(uLongRange.getFirst-s-VKNKU(), 0L) > 0) {
            long l3 = uLongRange.getFirst-s-VKNKU();
            l3 = URandomKt.nextULong-jmpaW-c(random, ULong.constructor-impl(l3 - ULong.constructor-impl(1L)), uLongRange.getLast-s-VKNKU());
            return ULong.constructor-impl(l3 + ULong.constructor-impl(1L));
        }
        return URandomKt.nextULong(random);
    }

    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final byte[] nextUBytes-EVgfTAA(@NotNull Random random, @NotNull byte[] byArray) {
        Intrinsics.checkNotNullParameter(random, "");
        Intrinsics.checkNotNullParameter(byArray, "");
        random.nextBytes(byArray);
        return byArray;
    }

    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final byte[] nextUBytes(@NotNull Random random, int n2) {
        Intrinsics.checkNotNullParameter(random, "");
        return UByteArray.constructor-impl(random.nextBytes(n2));
    }

    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final byte[] nextUBytes-Wvrt4B4(@NotNull Random random, @NotNull byte[] byArray, int n2, int n3) {
        Intrinsics.checkNotNullParameter(random, "");
        Intrinsics.checkNotNullParameter(byArray, "");
        random.nextBytes(byArray, n2, n3);
        return byArray;
    }

    public static /* synthetic */ byte[] nextUBytes-Wvrt4B4$default(Random random, byte[] byArray, int n2, int n3, int n4, Object object) {
        if ((n4 & 2) != 0) {
            n2 = 0;
        }
        if ((n4 & 4) != 0) {
            n3 = UByteArray.getSize-impl(byArray);
        }
        return URandomKt.nextUBytes-Wvrt4B4(random, byArray, n2, n3);
    }

    public static final void checkUIntRangeBounds-J1ME1BU(int n2, int n3) {
        boolean bl = Integer.compareUnsigned(n3, n2) > 0;
        if (!bl) {
            String string = RandomKt.boundsErrorMessage(UInt.box-impl(n2), UInt.box-impl(n3));
            throw new IllegalArgumentException(string.toString());
        }
    }

    public static final void checkULongRangeBounds-eb3DHEI(long l2, long l3) {
        boolean bl = Long.compareUnsigned(l3, l2) > 0;
        if (!bl) {
            String string = RandomKt.boundsErrorMessage(ULong.box-impl(l2), ULong.box-impl(l3));
            throw new IllegalArgumentException(string.toString());
        }
    }
}


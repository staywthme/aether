/*
 * Decompiled with CFR 0.152.
 */
package kotlin.collections;

import java.util.Collection;
import kotlin.ExperimentalUnsignedTypes;
import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.UByte;
import kotlin.UByteArray;
import kotlin.UInt;
import kotlin.UIntArray;
import kotlin.ULong;
import kotlin.ULongArray;
import kotlin.UShort;
import kotlin.UShortArray;
import kotlin.WasExperimental;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 8, 0}, k=5, xi=49, d1={"\u0000F\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u001c\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u001e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u001c\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00030\u0002H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0004\u0010\u0005\u001a\u001c\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00010\u0002H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0006\u0010\u0005\u001a\u001c\u0010\u0000\u001a\u00020\u0007*\b\u0012\u0004\u0012\u00020\u00070\u0002H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\b\u0010\t\u001a\u001c\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\n0\u0002H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u000b\u0010\u0005\u001a\u001a\u0010\f\u001a\u00020\r*\b\u0012\u0004\u0012\u00020\u00030\u000eH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000f\u001a\u001a\u0010\u0010\u001a\u00020\u0011*\b\u0012\u0004\u0012\u00020\u00010\u000eH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0012\u001a\u001a\u0010\u0013\u001a\u00020\u0014*\b\u0012\u0004\u0012\u00020\u00070\u000eH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0015\u001a\u001a\u0010\u0016\u001a\u00020\u0017*\b\u0012\u0004\u0012\u00020\n0\u000eH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0018\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0019"}, d2={"sum", "Lkotlin/UInt;", "", "Lkotlin/UByte;", "sumOfUByte", "(Ljava/lang/Iterable;)I", "sumOfUInt", "Lkotlin/ULong;", "sumOfULong", "(Ljava/lang/Iterable;)J", "Lkotlin/UShort;", "sumOfUShort", "toUByteArray", "Lkotlin/UByteArray;", "", "(Ljava/util/Collection;)[B", "toUIntArray", "Lkotlin/UIntArray;", "(Ljava/util/Collection;)[I", "toULongArray", "Lkotlin/ULongArray;", "(Ljava/util/Collection;)[J", "toUShortArray", "Lkotlin/UShortArray;", "(Ljava/util/Collection;)[S", "kotlin-stdlib"}, xs="kotlin/collections/UCollectionsKt")
class UCollectionsKt___UCollectionsKt {
    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final byte[] toUByteArray(@NotNull Collection<UByte> object) {
        Intrinsics.checkNotNullParameter(object, "");
        byte[] byArray = UByteArray.constructor-impl(object.size());
        int n2 = 0;
        object = object.iterator();
        while (object.hasNext()) {
            byte by = ((UByte)object.next()).unbox-impl();
            UByteArray.set-VurrAj0(byArray, n2++, by);
        }
        return byArray;
    }

    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final int[] toUIntArray(@NotNull Collection<UInt> object) {
        Intrinsics.checkNotNullParameter(object, "");
        int[] nArray = UIntArray.constructor-impl(object.size());
        int n2 = 0;
        object = object.iterator();
        while (object.hasNext()) {
            int n3 = ((UInt)object.next()).unbox-impl();
            UIntArray.set-VXSXFK8(nArray, n2++, n3);
        }
        return nArray;
    }

    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final long[] toULongArray(@NotNull Collection<ULong> object) {
        Intrinsics.checkNotNullParameter(object, "");
        long[] lArray = ULongArray.constructor-impl(object.size());
        int n2 = 0;
        object = object.iterator();
        while (object.hasNext()) {
            long l2 = ((ULong)object.next()).unbox-impl();
            ULongArray.set-k8EXiF4(lArray, n2++, l2);
        }
        return lArray;
    }

    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final short[] toUShortArray(@NotNull Collection<UShort> object) {
        Intrinsics.checkNotNullParameter(object, "");
        short[] sArray = UShortArray.constructor-impl(object.size());
        int n2 = 0;
        object = object.iterator();
        while (object.hasNext()) {
            short s2 = ((UShort)object.next()).unbox-impl();
            UShortArray.set-01HTLdE(sArray, n2++, s2);
        }
        return sArray;
    }

    @JvmName(name="sumOfUInt")
    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    public static final int sumOfUInt(@NotNull Iterable<UInt> object) {
        Intrinsics.checkNotNullParameter(object, "");
        int n2 = 0;
        object = object.iterator();
        while (object.hasNext()) {
            int n3 = ((UInt)object.next()).unbox-impl();
            n2 = UInt.constructor-impl(n2 + n3);
        }
        return n2;
    }

    @JvmName(name="sumOfULong")
    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    public static final long sumOfULong(@NotNull Iterable<ULong> object) {
        Intrinsics.checkNotNullParameter(object, "");
        long l2 = 0L;
        object = object.iterator();
        while (object.hasNext()) {
            long l3 = ((ULong)object.next()).unbox-impl();
            l2 = ULong.constructor-impl(l2 + l3);
        }
        return l2;
    }

    @JvmName(name="sumOfUByte")
    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    public static final int sumOfUByte(@NotNull Iterable<UByte> object) {
        Intrinsics.checkNotNullParameter(object, "");
        int n2 = 0;
        object = object.iterator();
        while (object.hasNext()) {
            byte by = ((UByte)object.next()).unbox-impl();
            n2 = UInt.constructor-impl(n2 + UInt.constructor-impl(by & 0xFF));
        }
        return n2;
    }

    @JvmName(name="sumOfUShort")
    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    public static final int sumOfUShort(@NotNull Iterable<UShort> object) {
        Intrinsics.checkNotNullParameter(object, "");
        int n2 = 0;
        object = object.iterator();
        while (object.hasNext()) {
            short s2 = ((UShort)object.next()).unbox-impl();
            n2 = UInt.constructor-impl(n2 + UInt.constructor-impl(s2 & 0xFFFF));
        }
        return n2;
    }
}


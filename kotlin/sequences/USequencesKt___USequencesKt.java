/*
 * Decompiled with CFR 0.152.
 */
package kotlin.sequences;

import kotlin.ExperimentalUnsignedTypes;
import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.UByte;
import kotlin.UInt;
import kotlin.ULong;
import kotlin.UShort;
import kotlin.WasExperimental;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 8, 0}, k=5, xi=49, d1={"\u0000\"\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u001c\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00030\u0002H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0004\u0010\u0005\u001a\u001c\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00010\u0002H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0006\u0010\u0005\u001a\u001c\u0010\u0000\u001a\u00020\u0007*\b\u0012\u0004\u0012\u00020\u00070\u0002H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\b\u0010\t\u001a\u001c\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\n0\u0002H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u000b\u0010\u0005\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\f"}, d2={"sum", "Lkotlin/UInt;", "Lkotlin/sequences/Sequence;", "Lkotlin/UByte;", "sumOfUByte", "(Lkotlin/sequences/Sequence;)I", "sumOfUInt", "Lkotlin/ULong;", "sumOfULong", "(Lkotlin/sequences/Sequence;)J", "Lkotlin/UShort;", "sumOfUShort", "kotlin-stdlib"}, xs="kotlin/sequences/USequencesKt")
class USequencesKt___USequencesKt {
    @JvmName(name="sumOfUInt")
    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    public static final int sumOfUInt(@NotNull Sequence<UInt> object) {
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
    public static final long sumOfULong(@NotNull Sequence<ULong> object) {
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
    public static final int sumOfUByte(@NotNull Sequence<UByte> object) {
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
    public static final int sumOfUShort(@NotNull Sequence<UShort> object) {
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


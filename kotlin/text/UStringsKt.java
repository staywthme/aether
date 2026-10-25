/*
 * Decompiled with CFR 0.152.
 */
package kotlin.text;

import kotlin.ExperimentalUnsignedTypes;
import kotlin.KotlinNothingValueException;
import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.UByte;
import kotlin.UInt;
import kotlin.ULong;
import kotlin.UShort;
import kotlin.UnsignedKt;
import kotlin.WasExperimental;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.CharsKt;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(mv={1, 8, 0}, k=2, xi=48, d1={"\u0000,\n\u0000\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0013\u001a\u001e\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0005\u0010\u0006\u001a\u001e\u0010\u0000\u001a\u00020\u0001*\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\b\u0010\t\u001a\u001e\u0010\u0000\u001a\u00020\u0001*\u00020\n2\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u000b\u0010\f\u001a\u001e\u0010\u0000\u001a\u00020\u0001*\u00020\r2\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u000e\u0010\u000f\u001a\u0014\u0010\u0010\u001a\u00020\u0002*\u00020\u0001H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011\u001a\u001c\u0010\u0010\u001a\u00020\u0002*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0012\u001a\u0011\u0010\u0013\u001a\u0004\u0018\u00010\u0002*\u00020\u0001H\u0007\u00f8\u0001\u0000\u001a\u0019\u0010\u0013\u001a\u0004\u0018\u00010\u0002*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u00f8\u0001\u0000\u001a\u0014\u0010\u0014\u001a\u00020\u0007*\u00020\u0001H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0015\u001a\u001c\u0010\u0014\u001a\u00020\u0007*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0016\u001a\u0011\u0010\u0017\u001a\u0004\u0018\u00010\u0007*\u00020\u0001H\u0007\u00f8\u0001\u0000\u001a\u0019\u0010\u0017\u001a\u0004\u0018\u00010\u0007*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u00f8\u0001\u0000\u001a\u0014\u0010\u0018\u001a\u00020\n*\u00020\u0001H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0019\u001a\u001c\u0010\u0018\u001a\u00020\n*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001a\u001a\u0011\u0010\u001b\u001a\u0004\u0018\u00010\n*\u00020\u0001H\u0007\u00f8\u0001\u0000\u001a\u0019\u0010\u001b\u001a\u0004\u0018\u00010\n*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u00f8\u0001\u0000\u001a\u0014\u0010\u001c\u001a\u00020\r*\u00020\u0001H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001d\u001a\u001c\u0010\u001c\u001a\u00020\r*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001e\u001a\u0011\u0010\u001f\u001a\u0004\u0018\u00010\r*\u00020\u0001H\u0007\u00f8\u0001\u0000\u001a\u0019\u0010\u001f\u001a\u0004\u0018\u00010\r*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u00f8\u0001\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006 "}, d2={"toString", "", "Lkotlin/UByte;", "radix", "", "toString-LxnNnR4", "(BI)Ljava/lang/String;", "Lkotlin/UInt;", "toString-V7xB4Y4", "(II)Ljava/lang/String;", "Lkotlin/ULong;", "toString-JSWoG40", "(JI)Ljava/lang/String;", "Lkotlin/UShort;", "toString-olVBNx4", "(SI)Ljava/lang/String;", "toUByte", "(Ljava/lang/String;)B", "(Ljava/lang/String;I)B", "toUByteOrNull", "toUInt", "(Ljava/lang/String;)I", "(Ljava/lang/String;I)I", "toUIntOrNull", "toULong", "(Ljava/lang/String;)J", "(Ljava/lang/String;I)J", "toULongOrNull", "toUShort", "(Ljava/lang/String;)S", "(Ljava/lang/String;I)S", "toUShortOrNull", "kotlin-stdlib"})
@JvmName(name="UStringsKt")
public final class UStringsKt {
    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    @NotNull
    public static final String toString-LxnNnR4(byte by, int n2) {
        String string = Integer.toString(by & 0xFF, CharsKt.checkRadix(n2));
        Intrinsics.checkNotNullExpressionValue(string, "");
        return string;
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    @NotNull
    public static final String toString-olVBNx4(short s2, int n2) {
        String string = Integer.toString(s2 & 0xFFFF, CharsKt.checkRadix(n2));
        Intrinsics.checkNotNullExpressionValue(string, "");
        return string;
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    @NotNull
    public static final String toString-V7xB4Y4(int n2, int n3) {
        String string = Long.toString((long)n2 & 0xFFFFFFFFL, CharsKt.checkRadix(n3));
        Intrinsics.checkNotNullExpressionValue(string, "");
        return string;
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    @NotNull
    public static final String toString-JSWoG40(long l2, int n2) {
        return UnsignedKt.ulongToString(l2, CharsKt.checkRadix(n2));
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    public static final byte toUByte(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, "");
        UByte uByte = UStringsKt.toUByteOrNull(string);
        if (uByte != null) {
            return uByte.unbox-impl();
        }
        StringsKt.numberFormatError(string);
        throw new KotlinNothingValueException();
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    public static final byte toUByte(@NotNull String string, int n2) {
        Intrinsics.checkNotNullParameter(string, "");
        UByte uByte = UStringsKt.toUByteOrNull(string, n2);
        if (uByte != null) {
            return uByte.unbox-impl();
        }
        StringsKt.numberFormatError(string);
        throw new KotlinNothingValueException();
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    public static final short toUShort(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, "");
        UShort uShort = UStringsKt.toUShortOrNull(string);
        if (uShort != null) {
            return uShort.unbox-impl();
        }
        StringsKt.numberFormatError(string);
        throw new KotlinNothingValueException();
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    public static final short toUShort(@NotNull String string, int n2) {
        Intrinsics.checkNotNullParameter(string, "");
        UShort uShort = UStringsKt.toUShortOrNull(string, n2);
        if (uShort != null) {
            return uShort.unbox-impl();
        }
        StringsKt.numberFormatError(string);
        throw new KotlinNothingValueException();
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    public static final int toUInt(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, "");
        UInt uInt = UStringsKt.toUIntOrNull(string);
        if (uInt != null) {
            return uInt.unbox-impl();
        }
        StringsKt.numberFormatError(string);
        throw new KotlinNothingValueException();
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    public static final int toUInt(@NotNull String string, int n2) {
        Intrinsics.checkNotNullParameter(string, "");
        UInt uInt = UStringsKt.toUIntOrNull(string, n2);
        if (uInt != null) {
            return uInt.unbox-impl();
        }
        StringsKt.numberFormatError(string);
        throw new KotlinNothingValueException();
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    public static final long toULong(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, "");
        ULong uLong = UStringsKt.toULongOrNull(string);
        if (uLong != null) {
            return uLong.unbox-impl();
        }
        StringsKt.numberFormatError(string);
        throw new KotlinNothingValueException();
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    public static final long toULong(@NotNull String string, int n2) {
        Intrinsics.checkNotNullParameter(string, "");
        ULong uLong = UStringsKt.toULongOrNull(string, n2);
        if (uLong != null) {
            return uLong.unbox-impl();
        }
        StringsKt.numberFormatError(string);
        throw new KotlinNothingValueException();
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    @Nullable
    public static final UByte toUByteOrNull(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, "");
        return UStringsKt.toUByteOrNull(string, 10);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    @Nullable
    public static final UByte toUByteOrNull(@NotNull String string, int n2) {
        Intrinsics.checkNotNullParameter(string, "");
        UInt uInt = UStringsKt.toUIntOrNull(string, n2);
        if (uInt == null) {
            return null;
        }
        int n3 = uInt.unbox-impl();
        if (Integer.compareUnsigned(n3, UInt.constructor-impl(255)) > 0) {
            return null;
        }
        return UByte.box-impl(UByte.constructor-impl((byte)n3));
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    @Nullable
    public static final UShort toUShortOrNull(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, "");
        return UStringsKt.toUShortOrNull(string, 10);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    @Nullable
    public static final UShort toUShortOrNull(@NotNull String string, int n2) {
        Intrinsics.checkNotNullParameter(string, "");
        UInt uInt = UStringsKt.toUIntOrNull(string, n2);
        if (uInt == null) {
            return null;
        }
        int n3 = uInt.unbox-impl();
        if (Integer.compareUnsigned(n3, UInt.constructor-impl(65535)) > 0) {
            return null;
        }
        return UShort.box-impl(UShort.constructor-impl((short)n3));
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    @Nullable
    public static final UInt toUIntOrNull(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, "");
        return UStringsKt.toUIntOrNull(string, 10);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    @Nullable
    public static final UInt toUIntOrNull(@NotNull String string, int n2) {
        Intrinsics.checkNotNullParameter(string, "");
        CharsKt.checkRadix(n2);
        int n3 = string.length();
        if (n3 == 0) {
            return null;
        }
        int n4 = string.charAt(0);
        if (Intrinsics.compare(n4, 48) < 0) {
            if (n3 == 1 || n4 != 43) {
                return null;
            }
            n4 = 1;
        } else {
            n4 = 0;
        }
        int n5 = 0x71C71C7;
        int n6 = UInt.constructor-impl(n2);
        int n7 = 0;
        while (n4 < n3) {
            int n8 = CharsKt.digitOf(string.charAt(n4), n2);
            if (n8 < 0) {
                return null;
            }
            if (Integer.compareUnsigned(n7, n5) > 0) {
                if (n5 == 0x71C71C7) {
                    n5 = Integer.divideUnsigned(-1, n6);
                    if (Integer.compareUnsigned(n7, n5) > 0) {
                        return null;
                    }
                } else {
                    return null;
                }
            }
            int n9 = n7 = UInt.constructor-impl(n7 * n6);
            if (Integer.compareUnsigned(n7 = UInt.constructor-impl(n7 + UInt.constructor-impl(n8)), n9) < 0) {
                return null;
            }
            ++n4;
        }
        return UInt.box-impl(n7);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    @Nullable
    public static final ULong toULongOrNull(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, "");
        return UStringsKt.toULongOrNull(string, 10);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    @Nullable
    public static final ULong toULongOrNull(@NotNull String string, int n2) {
        Intrinsics.checkNotNullParameter(string, "");
        CharsKt.checkRadix(n2);
        int n3 = string.length();
        if (n3 == 0) {
            return null;
        }
        int n4 = string.charAt(0);
        if (Intrinsics.compare(n4, 48) < 0) {
            if (n3 == 1 || n4 != 43) {
                return null;
            }
            n4 = 1;
        } else {
            n4 = 0;
        }
        long l2 = 0x71C71C71C71C71CL;
        long l3 = ULong.constructor-impl(n2);
        long l4 = 0L;
        while (n4 < n3) {
            int n5 = CharsKt.digitOf(string.charAt(n4), n2);
            if (n5 < 0) {
                return null;
            }
            if (Long.compareUnsigned(l4, l2) > 0) {
                if (l2 == 0x71C71C71C71C71CL) {
                    l2 = Long.divideUnsigned(-1L, l3);
                    if (Long.compareUnsigned(l4, l2) > 0) {
                        return null;
                    }
                } else {
                    return null;
                }
            }
            long l5 = l4 = ULong.constructor-impl(l4 * l3);
            if (Long.compareUnsigned(l4 = ULong.constructor-impl(l4 + ULong.constructor-impl((long)(n5 = UInt.constructor-impl(n5)) & 0xFFFFFFFFL)), l5) < 0) {
                return null;
            }
            ++n4;
        }
        return ULong.box-impl(l4);
    }
}


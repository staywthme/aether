/*
 * Decompiled with CFR 0.152.
 */
package kotlin.comparisons;

import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.comparisons.ComparisonsKt;
import kotlin.comparisons.ComparisonsKt__ComparisonsKt;
import kotlin.internal.InlineOnly;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 8, 0}, k=5, xi=49, d1={"\u0000F\n\u0002\b\u0002\n\u0002\u0010\u000f\n\u0002\b\u0006\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u0005\n\u0002\u0010\u0012\n\u0002\u0010\u0006\n\u0002\u0010\u0013\n\u0002\u0010\u0007\n\u0002\u0010\u0014\n\u0002\u0010\b\n\u0002\u0010\u0015\n\u0002\u0010\t\n\u0002\u0010\u0016\n\u0002\u0010\n\n\u0002\u0010\u0017\n\u0002\b\u0002\u001a-\u0010\u0000\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u00022\u0006\u0010\u0003\u001a\u0002H\u00012\u0006\u0010\u0004\u001a\u0002H\u0001H\u0007\u00a2\u0006\u0002\u0010\u0005\u001a5\u0010\u0000\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u00022\u0006\u0010\u0003\u001a\u0002H\u00012\u0006\u0010\u0004\u001a\u0002H\u00012\u0006\u0010\u0006\u001a\u0002H\u0001H\u0007\u00a2\u0006\u0002\u0010\u0007\u001a9\u0010\u0000\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u00022\u0006\u0010\u0003\u001a\u0002H\u00012\u0012\u0010\b\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00010\t\"\u0002H\u0001H\u0007\u00a2\u0006\u0002\u0010\n\u001a\u0019\u0010\u0000\u001a\u00020\u000b2\u0006\u0010\u0003\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u000bH\u0087\b\u001a!\u0010\u0000\u001a\u00020\u000b2\u0006\u0010\u0003\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u000b2\u0006\u0010\u0006\u001a\u00020\u000bH\u0087\b\u001a\u001c\u0010\u0000\u001a\u00020\u000b2\u0006\u0010\u0003\u001a\u00020\u000b2\n\u0010\b\u001a\u00020\f\"\u00020\u000bH\u0007\u001a\u0019\u0010\u0000\u001a\u00020\r2\u0006\u0010\u0003\u001a\u00020\r2\u0006\u0010\u0004\u001a\u00020\rH\u0087\b\u001a!\u0010\u0000\u001a\u00020\r2\u0006\u0010\u0003\u001a\u00020\r2\u0006\u0010\u0004\u001a\u00020\r2\u0006\u0010\u0006\u001a\u00020\rH\u0087\b\u001a\u001c\u0010\u0000\u001a\u00020\r2\u0006\u0010\u0003\u001a\u00020\r2\n\u0010\b\u001a\u00020\u000e\"\u00020\rH\u0007\u001a\u0019\u0010\u0000\u001a\u00020\u000f2\u0006\u0010\u0003\u001a\u00020\u000f2\u0006\u0010\u0004\u001a\u00020\u000fH\u0087\b\u001a!\u0010\u0000\u001a\u00020\u000f2\u0006\u0010\u0003\u001a\u00020\u000f2\u0006\u0010\u0004\u001a\u00020\u000f2\u0006\u0010\u0006\u001a\u00020\u000fH\u0087\b\u001a\u001c\u0010\u0000\u001a\u00020\u000f2\u0006\u0010\u0003\u001a\u00020\u000f2\n\u0010\b\u001a\u00020\u0010\"\u00020\u000fH\u0007\u001a\u0019\u0010\u0000\u001a\u00020\u00112\u0006\u0010\u0003\u001a\u00020\u00112\u0006\u0010\u0004\u001a\u00020\u0011H\u0087\b\u001a!\u0010\u0000\u001a\u00020\u00112\u0006\u0010\u0003\u001a\u00020\u00112\u0006\u0010\u0004\u001a\u00020\u00112\u0006\u0010\u0006\u001a\u00020\u0011H\u0087\b\u001a\u001c\u0010\u0000\u001a\u00020\u00112\u0006\u0010\u0003\u001a\u00020\u00112\n\u0010\b\u001a\u00020\u0012\"\u00020\u0011H\u0007\u001a\u0019\u0010\u0000\u001a\u00020\u00132\u0006\u0010\u0003\u001a\u00020\u00132\u0006\u0010\u0004\u001a\u00020\u0013H\u0087\b\u001a!\u0010\u0000\u001a\u00020\u00132\u0006\u0010\u0003\u001a\u00020\u00132\u0006\u0010\u0004\u001a\u00020\u00132\u0006\u0010\u0006\u001a\u00020\u0013H\u0087\b\u001a\u001c\u0010\u0000\u001a\u00020\u00132\u0006\u0010\u0003\u001a\u00020\u00132\n\u0010\b\u001a\u00020\u0014\"\u00020\u0013H\u0007\u001a\u0019\u0010\u0000\u001a\u00020\u00152\u0006\u0010\u0003\u001a\u00020\u00152\u0006\u0010\u0004\u001a\u00020\u0015H\u0087\b\u001a!\u0010\u0000\u001a\u00020\u00152\u0006\u0010\u0003\u001a\u00020\u00152\u0006\u0010\u0004\u001a\u00020\u00152\u0006\u0010\u0006\u001a\u00020\u0015H\u0087\b\u001a\u001c\u0010\u0000\u001a\u00020\u00152\u0006\u0010\u0003\u001a\u00020\u00152\n\u0010\b\u001a\u00020\u0016\"\u00020\u0015H\u0007\u001a-\u0010\u0017\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u00022\u0006\u0010\u0003\u001a\u0002H\u00012\u0006\u0010\u0004\u001a\u0002H\u0001H\u0007\u00a2\u0006\u0002\u0010\u0005\u001a5\u0010\u0017\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u00022\u0006\u0010\u0003\u001a\u0002H\u00012\u0006\u0010\u0004\u001a\u0002H\u00012\u0006\u0010\u0006\u001a\u0002H\u0001H\u0007\u00a2\u0006\u0002\u0010\u0007\u001a9\u0010\u0017\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u00022\u0006\u0010\u0003\u001a\u0002H\u00012\u0012\u0010\b\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00010\t\"\u0002H\u0001H\u0007\u00a2\u0006\u0002\u0010\n\u001a\u0019\u0010\u0017\u001a\u00020\u000b2\u0006\u0010\u0003\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u000bH\u0087\b\u001a!\u0010\u0017\u001a\u00020\u000b2\u0006\u0010\u0003\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u000b2\u0006\u0010\u0006\u001a\u00020\u000bH\u0087\b\u001a\u001c\u0010\u0017\u001a\u00020\u000b2\u0006\u0010\u0003\u001a\u00020\u000b2\n\u0010\b\u001a\u00020\f\"\u00020\u000bH\u0007\u001a\u0019\u0010\u0017\u001a\u00020\r2\u0006\u0010\u0003\u001a\u00020\r2\u0006\u0010\u0004\u001a\u00020\rH\u0087\b\u001a!\u0010\u0017\u001a\u00020\r2\u0006\u0010\u0003\u001a\u00020\r2\u0006\u0010\u0004\u001a\u00020\r2\u0006\u0010\u0006\u001a\u00020\rH\u0087\b\u001a\u001c\u0010\u0017\u001a\u00020\r2\u0006\u0010\u0003\u001a\u00020\r2\n\u0010\b\u001a\u00020\u000e\"\u00020\rH\u0007\u001a\u0019\u0010\u0017\u001a\u00020\u000f2\u0006\u0010\u0003\u001a\u00020\u000f2\u0006\u0010\u0004\u001a\u00020\u000fH\u0087\b\u001a!\u0010\u0017\u001a\u00020\u000f2\u0006\u0010\u0003\u001a\u00020\u000f2\u0006\u0010\u0004\u001a\u00020\u000f2\u0006\u0010\u0006\u001a\u00020\u000fH\u0087\b\u001a\u001c\u0010\u0017\u001a\u00020\u000f2\u0006\u0010\u0003\u001a\u00020\u000f2\n\u0010\b\u001a\u00020\u0010\"\u00020\u000fH\u0007\u001a\u0019\u0010\u0017\u001a\u00020\u00112\u0006\u0010\u0003\u001a\u00020\u00112\u0006\u0010\u0004\u001a\u00020\u0011H\u0087\b\u001a!\u0010\u0017\u001a\u00020\u00112\u0006\u0010\u0003\u001a\u00020\u00112\u0006\u0010\u0004\u001a\u00020\u00112\u0006\u0010\u0006\u001a\u00020\u0011H\u0087\b\u001a\u001c\u0010\u0017\u001a\u00020\u00112\u0006\u0010\u0003\u001a\u00020\u00112\n\u0010\b\u001a\u00020\u0012\"\u00020\u0011H\u0007\u001a\u0019\u0010\u0017\u001a\u00020\u00132\u0006\u0010\u0003\u001a\u00020\u00132\u0006\u0010\u0004\u001a\u00020\u0013H\u0087\b\u001a!\u0010\u0017\u001a\u00020\u00132\u0006\u0010\u0003\u001a\u00020\u00132\u0006\u0010\u0004\u001a\u00020\u00132\u0006\u0010\u0006\u001a\u00020\u0013H\u0087\b\u001a\u001c\u0010\u0017\u001a\u00020\u00132\u0006\u0010\u0003\u001a\u00020\u00132\n\u0010\b\u001a\u00020\u0014\"\u00020\u0013H\u0007\u001a\u0019\u0010\u0017\u001a\u00020\u00152\u0006\u0010\u0003\u001a\u00020\u00152\u0006\u0010\u0004\u001a\u00020\u0015H\u0087\b\u001a!\u0010\u0017\u001a\u00020\u00152\u0006\u0010\u0003\u001a\u00020\u00152\u0006\u0010\u0004\u001a\u00020\u00152\u0006\u0010\u0006\u001a\u00020\u0015H\u0087\b\u001a\u001c\u0010\u0017\u001a\u00020\u00152\u0006\u0010\u0003\u001a\u00020\u00152\n\u0010\b\u001a\u00020\u0016\"\u00020\u0015H\u0007\u00a8\u0006\u0018"}, d2={"maxOf", "T", "", "a", "b", "(Ljava/lang/Comparable;Ljava/lang/Comparable;)Ljava/lang/Comparable;", "c", "(Ljava/lang/Comparable;Ljava/lang/Comparable;Ljava/lang/Comparable;)Ljava/lang/Comparable;", "other", "", "(Ljava/lang/Comparable;[Ljava/lang/Comparable;)Ljava/lang/Comparable;", "", "", "", "", "", "", "", "", "", "", "", "", "minOf", "kotlin-stdlib"}, xs="kotlin/comparisons/ComparisonsKt")
public class ComparisonsKt___ComparisonsJvmKt
extends ComparisonsKt__ComparisonsKt {
    @SinceKotlin(version="1.1")
    @NotNull
    public static final <T extends Comparable<? super T>> T maxOf(@NotNull T t2, @NotNull T t3) {
        Intrinsics.checkNotNullParameter(t2, "");
        Intrinsics.checkNotNullParameter(t3, "");
        if (t2.compareTo(t3) >= 0) {
            return t2;
        }
        return t3;
    }

    @SinceKotlin(version="1.1")
    @InlineOnly
    private static final byte maxOf(byte by, byte by2) {
        return (byte)Math.max(by, by2);
    }

    @SinceKotlin(version="1.1")
    @InlineOnly
    private static final short maxOf(short s2, short s3) {
        return (short)Math.max(s2, s3);
    }

    @SinceKotlin(version="1.1")
    @InlineOnly
    private static final int maxOf(int n2, int n3) {
        return Math.max(n2, n3);
    }

    @SinceKotlin(version="1.1")
    @InlineOnly
    private static final long maxOf(long l2, long l3) {
        return Math.max(l2, l3);
    }

    @SinceKotlin(version="1.1")
    @InlineOnly
    private static final float maxOf(float f2, float f3) {
        return Math.max(f2, f3);
    }

    @SinceKotlin(version="1.1")
    @InlineOnly
    private static final double maxOf(double d2, double d3) {
        return Math.max(d2, d3);
    }

    @SinceKotlin(version="1.1")
    @NotNull
    public static final <T extends Comparable<? super T>> T maxOf(@NotNull T t2, @NotNull T t3, @NotNull T t4) {
        Intrinsics.checkNotNullParameter(t2, "");
        Intrinsics.checkNotNullParameter(t3, "");
        Intrinsics.checkNotNullParameter(t4, "");
        return ComparisonsKt.maxOf(t2, ComparisonsKt.maxOf(t3, t4));
    }

    @SinceKotlin(version="1.1")
    @InlineOnly
    private static final byte maxOf(byte by, byte by2, byte by3) {
        return (byte)Math.max(by, Math.max(by2, by3));
    }

    @SinceKotlin(version="1.1")
    @InlineOnly
    private static final short maxOf(short s2, short s3, short s4) {
        return (short)Math.max(s2, Math.max(s3, s4));
    }

    @SinceKotlin(version="1.1")
    @InlineOnly
    private static final int maxOf(int n2, int n3, int n4) {
        return Math.max(n2, Math.max(n3, n4));
    }

    @SinceKotlin(version="1.1")
    @InlineOnly
    private static final long maxOf(long l2, long l3, long l4) {
        return Math.max(l2, Math.max(l3, l4));
    }

    @SinceKotlin(version="1.1")
    @InlineOnly
    private static final float maxOf(float f2, float f3, float f4) {
        return Math.max(f2, Math.max(f3, f4));
    }

    @SinceKotlin(version="1.1")
    @InlineOnly
    private static final double maxOf(double d2, double d3, double d4) {
        return Math.max(d2, Math.max(d3, d4));
    }

    @SinceKotlin(version="1.4")
    @NotNull
    public static final <T extends Comparable<? super T>> T maxOf(@NotNull T t2, T ... TArray) {
        Intrinsics.checkNotNullParameter(t2, "");
        Intrinsics.checkNotNullParameter(TArray, "");
        for (T t3 : TArray) {
            t2 = ComparisonsKt.maxOf(t2, t3);
        }
        return t2;
    }

    @SinceKotlin(version="1.4")
    public static final byte maxOf(byte by, byte ... byArray) {
        Intrinsics.checkNotNullParameter(byArray, "");
        for (byte by2 : byArray) {
            by = (byte)Math.max(by, by2);
        }
        return by;
    }

    @SinceKotlin(version="1.4")
    public static final short maxOf(short s2, short ... sArray) {
        Intrinsics.checkNotNullParameter(sArray, "");
        for (short s3 : sArray) {
            s2 = (short)Math.max(s2, s3);
        }
        return s2;
    }

    @SinceKotlin(version="1.4")
    public static final int maxOf(int n2, int ... nArray) {
        Intrinsics.checkNotNullParameter(nArray, "");
        for (int n3 : nArray) {
            n2 = Math.max(n2, n3);
        }
        return n2;
    }

    @SinceKotlin(version="1.4")
    public static final long maxOf(long l2, long ... lArray) {
        Intrinsics.checkNotNullParameter(lArray, "");
        long l3 = l2;
        for (long l4 : lArray) {
            l3 = Math.max(l3, l4);
        }
        return l3;
    }

    @SinceKotlin(version="1.4")
    public static final float maxOf(float f2, float ... fArray) {
        Intrinsics.checkNotNullParameter(fArray, "");
        for (float f3 : fArray) {
            f2 = Math.max(f2, f3);
        }
        return f2;
    }

    @SinceKotlin(version="1.4")
    public static final double maxOf(double d2, double ... dArray) {
        Intrinsics.checkNotNullParameter(dArray, "");
        double d3 = d2;
        for (double d4 : dArray) {
            d3 = Math.max(d3, d4);
        }
        return d3;
    }

    @SinceKotlin(version="1.1")
    @NotNull
    public static final <T extends Comparable<? super T>> T minOf(@NotNull T t2, @NotNull T t3) {
        Intrinsics.checkNotNullParameter(t2, "");
        Intrinsics.checkNotNullParameter(t3, "");
        if (t2.compareTo(t3) <= 0) {
            return t2;
        }
        return t3;
    }

    @SinceKotlin(version="1.1")
    @InlineOnly
    private static final byte minOf(byte by, byte by2) {
        return (byte)Math.min(by, by2);
    }

    @SinceKotlin(version="1.1")
    @InlineOnly
    private static final short minOf(short s2, short s3) {
        return (short)Math.min(s2, s3);
    }

    @SinceKotlin(version="1.1")
    @InlineOnly
    private static final int minOf(int n2, int n3) {
        return Math.min(n2, n3);
    }

    @SinceKotlin(version="1.1")
    @InlineOnly
    private static final long minOf(long l2, long l3) {
        return Math.min(l2, l3);
    }

    @SinceKotlin(version="1.1")
    @InlineOnly
    private static final float minOf(float f2, float f3) {
        return Math.min(f2, f3);
    }

    @SinceKotlin(version="1.1")
    @InlineOnly
    private static final double minOf(double d2, double d3) {
        return Math.min(d2, d3);
    }

    @SinceKotlin(version="1.1")
    @NotNull
    public static final <T extends Comparable<? super T>> T minOf(@NotNull T t2, @NotNull T t3, @NotNull T t4) {
        Intrinsics.checkNotNullParameter(t2, "");
        Intrinsics.checkNotNullParameter(t3, "");
        Intrinsics.checkNotNullParameter(t4, "");
        return ComparisonsKt.minOf(t2, ComparisonsKt.minOf(t3, t4));
    }

    @SinceKotlin(version="1.1")
    @InlineOnly
    private static final byte minOf(byte by, byte by2, byte by3) {
        return (byte)Math.min(by, Math.min(by2, by3));
    }

    @SinceKotlin(version="1.1")
    @InlineOnly
    private static final short minOf(short s2, short s3, short s4) {
        return (short)Math.min(s2, Math.min(s3, s4));
    }

    @SinceKotlin(version="1.1")
    @InlineOnly
    private static final int minOf(int n2, int n3, int n4) {
        return Math.min(n2, Math.min(n3, n4));
    }

    @SinceKotlin(version="1.1")
    @InlineOnly
    private static final long minOf(long l2, long l3, long l4) {
        return Math.min(l2, Math.min(l3, l4));
    }

    @SinceKotlin(version="1.1")
    @InlineOnly
    private static final float minOf(float f2, float f3, float f4) {
        return Math.min(f2, Math.min(f3, f4));
    }

    @SinceKotlin(version="1.1")
    @InlineOnly
    private static final double minOf(double d2, double d3, double d4) {
        return Math.min(d2, Math.min(d3, d4));
    }

    @SinceKotlin(version="1.4")
    @NotNull
    public static final <T extends Comparable<? super T>> T minOf(@NotNull T t2, T ... TArray) {
        Intrinsics.checkNotNullParameter(t2, "");
        Intrinsics.checkNotNullParameter(TArray, "");
        for (T t3 : TArray) {
            t2 = ComparisonsKt.minOf(t2, t3);
        }
        return t2;
    }

    @SinceKotlin(version="1.4")
    public static final byte minOf(byte by, byte ... byArray) {
        Intrinsics.checkNotNullParameter(byArray, "");
        for (byte by2 : byArray) {
            by = (byte)Math.min(by, by2);
        }
        return by;
    }

    @SinceKotlin(version="1.4")
    public static final short minOf(short s2, short ... sArray) {
        Intrinsics.checkNotNullParameter(sArray, "");
        for (short s3 : sArray) {
            s2 = (short)Math.min(s2, s3);
        }
        return s2;
    }

    @SinceKotlin(version="1.4")
    public static final int minOf(int n2, int ... nArray) {
        Intrinsics.checkNotNullParameter(nArray, "");
        for (int n3 : nArray) {
            n2 = Math.min(n2, n3);
        }
        return n2;
    }

    @SinceKotlin(version="1.4")
    public static final long minOf(long l2, long ... lArray) {
        Intrinsics.checkNotNullParameter(lArray, "");
        long l3 = l2;
        for (long l4 : lArray) {
            l3 = Math.min(l3, l4);
        }
        return l3;
    }

    @SinceKotlin(version="1.4")
    public static final float minOf(float f2, float ... fArray) {
        Intrinsics.checkNotNullParameter(fArray, "");
        for (float f3 : fArray) {
            f2 = Math.min(f2, f3);
        }
        return f2;
    }

    @SinceKotlin(version="1.4")
    public static final double minOf(double d2, double ... dArray) {
        Intrinsics.checkNotNullParameter(dArray, "");
        double d3 = d2;
        for (double d4 : dArray) {
            d3 = Math.min(d3, d4);
        }
        return d3;
    }
}


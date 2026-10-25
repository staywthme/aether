/*
 * Decompiled with CFR 0.152.
 */
package kotlin;

import kotlin.Metadata;
import kotlin.NumbersKt__BigIntegersKt;
import kotlin.SinceKotlin;
import kotlin.internal.InlineOnly;
import kotlin.internal.IntrinsicConstEvaluation;

@Metadata(mv={1, 8, 0}, k=5, xi=49, d1={"\u0000 \n\u0000\n\u0002\u0010\b\n\u0002\u0010\u0005\n\u0000\n\u0002\u0010\t\n\u0002\u0010\n\n\u0000\n\u0002\u0010\u0006\n\u0002\u0010\u0007\n\u0000\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0004*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0005H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0002H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0004*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0005H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0004*\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0004*\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0004*\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0004*\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0005H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u0002H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0004*\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u0005H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0004*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0005*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0005H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0007*\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u0007H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0007*\u00020\u00072\u0006\u0010\u0003\u001a\u00020\bH\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0007*\u00020\b2\u0006\u0010\u0003\u001a\u00020\u0007H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\b*\u00020\b2\u0006\u0010\u0003\u001a\u00020\bH\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0002*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0002H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0004*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0005*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0005H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0002*\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0001*\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0004*\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0005*\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0005H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0002*\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u0002H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0001*\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0004*\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u0005H\u0087\b\u00a8\u0006\t"}, d2={"floorDiv", "", "", "other", "", "", "mod", "", "", "kotlin-stdlib"}, xs="kotlin/NumbersKt")
class NumbersKt__FloorDivModKt
extends NumbersKt__BigIntegersKt {
    @SinceKotlin(version="1.5")
    @InlineOnly
    @IntrinsicConstEvaluation
    private static final int floorDiv(byte by, byte by2) {
        int n2 = by / by2;
        if ((by ^ by2) < 0 && n2 * by2 != by) {
            --n2;
        }
        return n2;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    @IntrinsicConstEvaluation
    private static final byte mod(byte by, byte by2) {
        int n2 = by % by2;
        byte by3 = by = (byte)n2;
        return (byte)(n2 + (by2 & ((by ^ by2) & (by3 | -by3)) >> 31));
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    @IntrinsicConstEvaluation
    private static final int floorDiv(byte by, short s2) {
        int n2 = by / s2;
        if ((by ^ s2) < 0 && n2 * s2 != by) {
            --n2;
        }
        return n2;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    @IntrinsicConstEvaluation
    private static final short mod(byte by, short s2) {
        int n2 = by % s2;
        byte by2 = by = (byte)n2;
        return (short)(n2 + (s2 & ((by ^ s2) & (by2 | -by2)) >> 31));
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    @IntrinsicConstEvaluation
    private static final int floorDiv(byte by, int n2) {
        int n3 = by / n2;
        if ((by ^ n2) < 0 && n3 * n2 != by) {
            --n3;
        }
        return n3;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    @IntrinsicConstEvaluation
    private static final int mod(byte by, int n2) {
        int n3 = by % n2;
        byte by2 = by = (byte)n3;
        return n3 + (n2 & ((by ^ n2) & (by2 | -by2)) >> 31);
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    @IntrinsicConstEvaluation
    private static final long floorDiv(byte by, long l2) {
        long l3 = by;
        long l4 = l3 / l2;
        if ((l3 ^ l2) < 0L && l4 * l2 != l3) {
            l4 += -1L;
        }
        return l4;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    @IntrinsicConstEvaluation
    private static final long mod(byte by, long l2) {
        long l3;
        long l4 = l3 = (long)by % l2;
        return l3 + (l2 & ((l3 ^ l2) & (l4 | -l4)) >> 63);
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    @IntrinsicConstEvaluation
    private static final int floorDiv(short s2, byte by) {
        int n2 = s2 / by;
        if ((s2 ^ by) < 0 && n2 * by != s2) {
            --n2;
        }
        return n2;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    @IntrinsicConstEvaluation
    private static final byte mod(short s2, byte by) {
        int n2 = s2 % by;
        short s3 = s2 = (short)n2;
        return (byte)(n2 + (by & ((s2 ^ by) & (s3 | -s3)) >> 31));
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    @IntrinsicConstEvaluation
    private static final int floorDiv(short s2, short s3) {
        int n2 = s2 / s3;
        if ((s2 ^ s3) < 0 && n2 * s3 != s2) {
            --n2;
        }
        return n2;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    @IntrinsicConstEvaluation
    private static final short mod(short s2, short s3) {
        int n2 = s2 % s3;
        short s4 = s2 = (short)n2;
        return (short)(n2 + (s3 & ((s2 ^ s3) & (s4 | -s4)) >> 31));
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    @IntrinsicConstEvaluation
    private static final int floorDiv(short s2, int n2) {
        int n3 = s2 / n2;
        if ((s2 ^ n2) < 0 && n3 * n2 != s2) {
            --n3;
        }
        return n3;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    @IntrinsicConstEvaluation
    private static final int mod(short s2, int n2) {
        int n3 = s2 % n2;
        short s3 = s2 = (short)n3;
        return n3 + (n2 & ((s2 ^ n2) & (s3 | -s3)) >> 31);
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    @IntrinsicConstEvaluation
    private static final long floorDiv(short s2, long l2) {
        long l3 = s2;
        long l4 = l3 / l2;
        if ((l3 ^ l2) < 0L && l4 * l2 != l3) {
            l4 += -1L;
        }
        return l4;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    @IntrinsicConstEvaluation
    private static final long mod(short s2, long l2) {
        long l3;
        long l4 = l3 = (long)s2 % l2;
        return l3 + (l2 & ((l3 ^ l2) & (l4 | -l4)) >> 63);
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    @IntrinsicConstEvaluation
    private static final int floorDiv(int n2, byte by) {
        int n3 = n2 / by;
        if ((n2 ^ by) < 0 && n3 * by != n2) {
            --n3;
        }
        return n3;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    @IntrinsicConstEvaluation
    private static final byte mod(int n2, byte by) {
        int n3 = n2 %= by;
        return (byte)(n2 + (by & ((n2 ^ by) & (n3 | -n3)) >> 31));
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    @IntrinsicConstEvaluation
    private static final int floorDiv(int n2, short s2) {
        int n3 = n2 / s2;
        if ((n2 ^ s2) < 0 && n3 * s2 != n2) {
            --n3;
        }
        return n3;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    @IntrinsicConstEvaluation
    private static final short mod(int n2, short s2) {
        int n3 = n2 %= s2;
        return (short)(n2 + (s2 & ((n2 ^ s2) & (n3 | -n3)) >> 31));
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    @IntrinsicConstEvaluation
    private static final int floorDiv(int n2, int n3) {
        int n4 = n2 / n3;
        if ((n2 ^ n3) < 0 && n4 * n3 != n2) {
            --n4;
        }
        return n4;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    @IntrinsicConstEvaluation
    private static final int mod(int n2, int n3) {
        int n4 = n2 %= n3;
        return n2 + (n3 & ((n2 ^ n3) & (n4 | -n4)) >> 31);
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    @IntrinsicConstEvaluation
    private static final long floorDiv(int n2, long l2) {
        long l3 = n2;
        long l4 = l3 / l2;
        if ((l3 ^ l2) < 0L && l4 * l2 != l3) {
            l4 += -1L;
        }
        return l4;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    @IntrinsicConstEvaluation
    private static final long mod(int n2, long l2) {
        long l3;
        long l4 = l3 = (long)n2 % l2;
        return l3 + (l2 & ((l3 ^ l2) & (l4 | -l4)) >> 63);
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    @IntrinsicConstEvaluation
    private static final long floorDiv(long l2, byte by) {
        long l3 = by;
        long l4 = l2 / l3;
        if ((l2 ^ l3) < 0L && l4 * l3 != l2) {
            l4 += -1L;
        }
        return l4;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    @IntrinsicConstEvaluation
    private static final byte mod(long l2, byte by) {
        long l3;
        long l4 = by;
        long l5 = l3 = l2 % l4;
        return (byte)(l3 + (l4 & ((l3 ^ l4) & (l5 | -l5)) >> 63));
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    @IntrinsicConstEvaluation
    private static final long floorDiv(long l2, short s2) {
        long l3 = s2;
        long l4 = l2 / l3;
        if ((l2 ^ l3) < 0L && l4 * l3 != l2) {
            l4 += -1L;
        }
        return l4;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    @IntrinsicConstEvaluation
    private static final short mod(long l2, short s2) {
        long l3;
        long l4 = s2;
        long l5 = l3 = l2 % l4;
        return (short)(l3 + (l4 & ((l3 ^ l4) & (l5 | -l5)) >> 63));
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    @IntrinsicConstEvaluation
    private static final long floorDiv(long l2, int n2) {
        long l3 = n2;
        long l4 = l2 / l3;
        if ((l2 ^ l3) < 0L && l4 * l3 != l2) {
            l4 += -1L;
        }
        return l4;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    @IntrinsicConstEvaluation
    private static final int mod(long l2, int n2) {
        long l3;
        long l4 = n2;
        long l5 = l3 = l2 % l4;
        return (int)(l3 + (l4 & ((l3 ^ l4) & (l5 | -l5)) >> 63));
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    @IntrinsicConstEvaluation
    private static final long floorDiv(long l2, long l3) {
        long l4 = l2 / l3;
        if ((l2 ^ l3) < 0L && l4 * l3 != l2) {
            l4 += -1L;
        }
        return l4;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    @IntrinsicConstEvaluation
    private static final long mod(long l2, long l3) {
        long l4;
        long l5 = l4 = l2 % l3;
        return l4 + (l3 & ((l4 ^ l3) & (l5 | -l5)) >> 63);
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    @IntrinsicConstEvaluation
    private static final float mod(float f2, float f3) {
        if (!((f2 %= f3) == 0.0f) && !(Math.signum(f2) == Math.signum(f3))) {
            return f2 + f3;
        }
        return f2;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    @IntrinsicConstEvaluation
    private static final double mod(float f2, double d2) {
        double d3 = (double)f2 % d2;
        if (!(d3 == 0.0) && !(Math.signum(d3) == Math.signum(d2))) {
            return d3 + d2;
        }
        return d3;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    @IntrinsicConstEvaluation
    private static final double mod(double d2, float f2) {
        double d3 = f2;
        double d4 = d2 % d3;
        if (!(d4 == 0.0) && !(Math.signum(d4) == Math.signum(d3))) {
            return d4 + d3;
        }
        return d4;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    @IntrinsicConstEvaluation
    private static final double mod(double d2, double d3) {
        double d4 = d2 % d3;
        if (!(d4 == 0.0) && !(Math.signum(d4) == Math.signum(d3))) {
            return d4 + d3;
        }
        return d4;
    }
}


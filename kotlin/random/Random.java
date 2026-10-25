/*
 * Decompiled with CFR 0.152.
 */
package kotlin.random;

import java.io.Serializable;
import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.internal.PlatformImplementationsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.random.PlatformRandomKt;
import kotlin.random.RandomKt;
import kotlin.ranges.IntRange;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0005\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\b'\u0018\u0000 \u00172\u00020\u0001:\u0001\u0017B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H&J\b\u0010\u0006\u001a\u00020\u0007H\u0016J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\tH\u0016J$\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\t2\b\b\u0002\u0010\u000b\u001a\u00020\u00042\b\b\u0002\u0010\f\u001a\u00020\u0004H\u0016J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\r\u001a\u00020\u0004H\u0016J\b\u0010\u000e\u001a\u00020\u000fH\u0016J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000fH\u0016J\u0018\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000fH\u0016J\b\u0010\u0012\u001a\u00020\u0013H\u0016J\b\u0010\u0014\u001a\u00020\u0004H\u0016J\u0010\u0010\u0014\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u00020\u0004H\u0016J\u0018\u0010\u0014\u001a\u00020\u00042\u0006\u0010\u0011\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u00020\u0004H\u0016J\b\u0010\u0015\u001a\u00020\u0016H\u0016J\u0010\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0010\u001a\u00020\u0016H\u0016J\u0018\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0011\u001a\u00020\u00162\u0006\u0010\u0010\u001a\u00020\u0016H\u0016\u00a8\u0006\u0018"}, d2={"Lkotlin/random/Random;", "", "()V", "nextBits", "", "bitCount", "nextBoolean", "", "nextBytes", "", "array", "fromIndex", "toIndex", "size", "nextDouble", "", "until", "from", "nextFloat", "", "nextInt", "nextLong", "", "Default", "kotlin-stdlib"})
@SinceKotlin(version="1.3")
@SourceDebugExtension(value={"SMAP\nRandom.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Random.kt\nkotlin/random/Random\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,383:1\n1#2:384\n*E\n"})
public abstract class Random {
    @NotNull
    public static final Default Default = new Default(null);
    @NotNull
    private static final Random defaultRandom = PlatformImplementationsKt.IMPLEMENTATIONS.defaultPlatformRandom();

    public abstract int nextBits(int var1);

    public int nextInt() {
        return this.nextBits(32);
    }

    public int nextInt(int n2) {
        return this.nextInt(0, n2);
    }

    public int nextInt(int n2, int n3) {
        boolean bl;
        RandomKt.checkRangeBounds(n2, n3);
        int n4 = n3 - n2;
        if (n4 > 0 || n4 == Integer.MIN_VALUE) {
            int n5;
            int n6 = n4;
            if ((n6 & -n6) == n4) {
                n3 = RandomKt.fastLog2(n4);
                n5 = this.nextBits(n3);
            } else {
                int n7;
                while ((n7 = this.nextInt() >>> 1) - (n3 = n7 % n4) + (n4 - 1) < 0) {
                }
                n5 = n3;
            }
            n4 = n5;
            return n2 + n4;
        }
        do {
            if (n2 <= (n4 = this.nextInt())) {
                if (n4 < n3) {
                    bl = true;
                    continue;
                }
                bl = false;
                continue;
            }
            bl = false;
        } while (!bl);
        return n4;
    }

    public long nextLong() {
        return ((long)this.nextInt() << 32) + (long)this.nextInt();
    }

    public long nextLong(long l2) {
        return this.nextLong(0L, l2);
    }

    public long nextLong(long l2, long l3) {
        long l4;
        boolean bl;
        RandomKt.checkRangeBounds(l2, l3);
        long l5 = l3 - l2;
        if (l5 > 0L) {
            long l6;
            long l7 = l5;
            if ((l7 & -l7) == l5) {
                long l8;
                int n2;
                int n3 = (int)l5;
                int n4 = (int)(l5 >>> 32);
                if (n3 != 0) {
                    n2 = RandomKt.fastLog2(n3);
                    l8 = (long)this.nextBits(n2) & 0xFFFFFFFFL;
                } else if (n4 == 1) {
                    l8 = (long)this.nextInt() & 0xFFFFFFFFL;
                } else {
                    n2 = RandomKt.fastLog2(n4);
                    l8 = ((long)this.nextBits(n2) << 32) + ((long)this.nextInt() & 0xFFFFFFFFL);
                }
                l6 = l8;
            } else {
                long l9;
                long l10;
                while ((l10 = this.nextLong() >>> 1) - (l9 = l10 % l5) + (l5 - 1L) < 0L) {
                }
                l6 = l9;
            }
            return l2 + l6;
        }
        do {
            if (l2 <= (l4 = this.nextLong())) {
                if (l4 < l3) {
                    bl = true;
                    continue;
                }
                bl = false;
                continue;
            }
            bl = false;
        } while (!bl);
        return l4;
    }

    public boolean nextBoolean() {
        return this.nextBits(1) != 0;
    }

    public double nextDouble() {
        return PlatformRandomKt.doubleFromParts(this.nextBits(26), this.nextBits(27));
    }

    public double nextDouble(double d2) {
        return this.nextDouble(0.0, d2);
    }

    public double nextDouble(double d2, double d3) {
        double d4;
        double d5;
        RandomKt.checkRangeBounds(d2, d3);
        double d6 = d3 - d2;
        if (Double.isInfinite(d6) && !Double.isInfinite(d2) && !Double.isNaN(d2) && !Double.isInfinite(d3) && !Double.isNaN(d3)) {
            double d7 = this.nextDouble() * (d3 / 2.0 - d2 / 2.0);
            d5 = d2 + d7 + d7;
        } else {
            d5 = d4 = d2 + this.nextDouble() * d6;
        }
        if (d5 >= d3) {
            return Math.nextAfter(d3, Double.NEGATIVE_INFINITY);
        }
        return d4;
    }

    public float nextFloat() {
        return (float)this.nextBits(24) / 1.6777216E7f;
    }

    @NotNull
    public byte[] nextBytes(@NotNull byte[] byArray, int n2, int n3) {
        int n4;
        Intrinsics.checkNotNullParameter(byArray, "");
        int n5 = new IntRange(0, byArray.length).contains(n2) && new IntRange(0, byArray.length).contains(n3) ? 1 : 0;
        if (n5 == 0) {
            String string = "fromIndex (" + n2 + ") or toIndex (" + n3 + ") are out of range: 0.." + byArray.length + '.';
            throw new IllegalArgumentException(string.toString());
        }
        n5 = n2 <= n3 ? 1 : 0;
        if (n5 == 0) {
            String string = "fromIndex (" + n2 + ") must be not greater than toIndex (" + n3 + ").";
            throw new IllegalArgumentException(string.toString());
        }
        n5 = (n3 - n2) / 4;
        for (n4 = 0; n4 < n5; ++n4) {
            int n6 = this.nextInt();
            byArray[n2] = (byte)n6;
            byArray[n2 + 1] = (byte)(n6 >>> 8);
            byArray[n2 + 2] = (byte)(n6 >>> 16);
            byArray[n2 + 3] = (byte)(n6 >>> 24);
            n2 += 4;
        }
        n4 = n3 - n2;
        n3 = this.nextBits(n4 << 3);
        for (n5 = 0; n5 < n4; ++n5) {
            byArray[n2 + n5] = (byte)(n3 >>> (n5 << 3));
        }
        return byArray;
    }

    public static /* synthetic */ byte[] nextBytes$default(Random random, byte[] byArray, int n2, int n3, int n4, Object object) {
        if (object != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: nextBytes");
        }
        if ((n4 & 2) != 0) {
            n2 = 0;
        }
        if ((n4 & 4) != 0) {
            n3 = byArray.length;
        }
        return random.nextBytes(byArray, n2, n3);
    }

    @NotNull
    public byte[] nextBytes(@NotNull byte[] byArray) {
        Intrinsics.checkNotNullParameter(byArray, "");
        return this.nextBytes(byArray, 0, byArray.length);
    }

    @NotNull
    public byte[] nextBytes(int n2) {
        return this.nextBytes(new byte[n2]);
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0005\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u00012\u00060\u0002j\u0002`\u0003:\u0001\u001cB\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0007H\u0016J\b\u0010\t\u001a\u00020\nH\u0016J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\fH\u0016J \u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\u00072\u0006\u0010\u000f\u001a\u00020\u0007H\u0016J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\u0007H\u0016J\b\u0010\u0011\u001a\u00020\u0012H\u0016J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0012H\u0016J\u0018\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0012H\u0016J\b\u0010\u0015\u001a\u00020\u0016H\u0016J\b\u0010\u0017\u001a\u00020\u0007H\u0016J\u0010\u0010\u0017\u001a\u00020\u00072\u0006\u0010\u0013\u001a\u00020\u0007H\u0016J\u0018\u0010\u0017\u001a\u00020\u00072\u0006\u0010\u0014\u001a\u00020\u00072\u0006\u0010\u0013\u001a\u00020\u0007H\u0016J\b\u0010\u0018\u001a\u00020\u0019H\u0016J\u0010\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u0013\u001a\u00020\u0019H\u0016J\u0018\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u0014\u001a\u00020\u00192\u0006\u0010\u0013\u001a\u00020\u0019H\u0016J\b\u0010\u001a\u001a\u00020\u001bH\u0002R\u000e\u0010\u0005\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001d"}, d2={"Lkotlin/random/Random$Default;", "Lkotlin/random/Random;", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "()V", "defaultRandom", "nextBits", "", "bitCount", "nextBoolean", "", "nextBytes", "", "array", "fromIndex", "toIndex", "size", "nextDouble", "", "until", "from", "nextFloat", "", "nextInt", "nextLong", "", "writeReplace", "", "Serialized", "kotlin-stdlib"})
    public static final class Default
    extends Random
    implements Serializable {
        private Default() {
        }

        private final Object writeReplace() {
            return Serialized.INSTANCE;
        }

        @Override
        public final int nextBits(int n2) {
            return defaultRandom.nextBits(n2);
        }

        @Override
        public final int nextInt() {
            return defaultRandom.nextInt();
        }

        @Override
        public final int nextInt(int n2) {
            return defaultRandom.nextInt(n2);
        }

        @Override
        public final int nextInt(int n2, int n3) {
            return defaultRandom.nextInt(n2, n3);
        }

        @Override
        public final long nextLong() {
            return defaultRandom.nextLong();
        }

        @Override
        public final long nextLong(long l2) {
            return defaultRandom.nextLong(l2);
        }

        @Override
        public final long nextLong(long l2, long l3) {
            return defaultRandom.nextLong(l2, l3);
        }

        @Override
        public final boolean nextBoolean() {
            return defaultRandom.nextBoolean();
        }

        @Override
        public final double nextDouble() {
            return defaultRandom.nextDouble();
        }

        @Override
        public final double nextDouble(double d2) {
            return defaultRandom.nextDouble(d2);
        }

        @Override
        public final double nextDouble(double d2, double d3) {
            return defaultRandom.nextDouble(d2, d3);
        }

        @Override
        public final float nextFloat() {
            return defaultRandom.nextFloat();
        }

        @Override
        @NotNull
        public final byte[] nextBytes(@NotNull byte[] byArray) {
            Intrinsics.checkNotNullParameter(byArray, "");
            return defaultRandom.nextBytes(byArray);
        }

        @Override
        @NotNull
        public final byte[] nextBytes(int n2) {
            return defaultRandom.nextBytes(n2);
        }

        @Override
        @NotNull
        public final byte[] nextBytes(@NotNull byte[] byArray, int n2, int n3) {
            Intrinsics.checkNotNullParameter(byArray, "");
            return defaultRandom.nextBytes(byArray, n2, n3);
        }

        public /* synthetic */ Default(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0000\n\u0000\b\u00c2\u0002\u0018\u00002\u00060\u0001j\u0002`\u0002B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0003J\b\u0010\u0006\u001a\u00020\u0007H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2={"Lkotlin/random/Random$Default$Serialized;", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "()V", "serialVersionUID", "", "readResolve", "", "kotlin-stdlib"})
        static final class Serialized
        implements Serializable {
            @NotNull
            public static final Serialized INSTANCE = new Serialized();
            private static final long serialVersionUID = 0L;

            private Serialized() {
            }

            private final Object readResolve() {
                return Default;
            }
        }
    }
}


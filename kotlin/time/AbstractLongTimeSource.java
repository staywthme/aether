/*
 * Decompiled with CFR 0.152.
 */
package kotlin.time;

import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.time.ComparableTimeMark;
import kotlin.time.Duration;
import kotlin.time.DurationKt;
import kotlin.time.DurationUnit;
import kotlin.time.DurationUnitKt;
import kotlin.time.ExperimentalTime;
import kotlin.time.TimeSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\b'\u0018\u00002\u00020\u0001:\u0001\u000bB\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u0007\u001a\u00020\bH\u0016J\b\u0010\t\u001a\u00020\nH$R\u0014\u0010\u0002\u001a\u00020\u0003X\u0084\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\f"}, d2={"Lkotlin/time/AbstractLongTimeSource;", "Lkotlin/time/TimeSource$WithComparableMarks;", "unit", "Lkotlin/time/DurationUnit;", "(Lkotlin/time/DurationUnit;)V", "getUnit", "()Lkotlin/time/DurationUnit;", "markNow", "Lkotlin/time/ComparableTimeMark;", "read", "", "LongTimeMark", "kotlin-stdlib"})
@SinceKotlin(version="1.3")
@ExperimentalTime
public abstract class AbstractLongTimeSource
implements TimeSource.WithComparableMarks {
    @NotNull
    private final DurationUnit unit;

    public AbstractLongTimeSource(@NotNull DurationUnit durationUnit) {
        Intrinsics.checkNotNullParameter((Object)durationUnit, "");
        this.unit = durationUnit;
    }

    @NotNull
    protected final DurationUnit getUnit() {
        return this.unit;
    }

    protected abstract long read();

    @Override
    @NotNull
    public ComparableTimeMark markNow() {
        return new LongTimeMark(this.read(), this, Duration.Companion.getZERO-UwyO8pc(), null);
    }

    /*
     * Illegal identifiers - consider using --renameillegalidents true
     */
    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010\u000e\n\u0000\b\u0002\u0018\u00002\u00020\u0001B \u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\bJ\u0015\u0010\n\u001a\u00020\u0007H\u0000\u00f8\u0001\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u000b\u0010\fJ\u0015\u0010\r\u001a\u00020\u0007H\u0016\u00f8\u0001\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u000e\u0010\fJ\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0096\u0002J\b\u0010\u0013\u001a\u00020\u0014H\u0016J\u001e\u0010\u0015\u001a\u00020\u00072\u0006\u0010\u0011\u001a\u00020\u0001H\u0096\u0002\u00f8\u0001\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0016\u0010\u0017J\u001b\u0010\u0018\u001a\u00020\u00012\u0006\u0010\u0019\u001a\u00020\u0007H\u0096\u0002\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001a\u0010\u001bJ\b\u0010\u001c\u001a\u00020\u001dH\u0016R\u0016\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\n\u0002\u0010\tR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\b\n\u0002\b\u0019\n\u0002\b!\u00a8\u0006\u001e"}, d2={"Lkotlin/time/AbstractLongTimeSource$LongTimeMark;", "Lkotlin/time/ComparableTimeMark;", "startedAt", "", "timeSource", "Lkotlin/time/AbstractLongTimeSource;", "offset", "Lkotlin/time/Duration;", "(JLkotlin/time/AbstractLongTimeSource;JLkotlin/jvm/internal/DefaultConstructorMarker;)V", "J", "effectiveDuration", "effectiveDuration-UwyO8pc$kotlin_stdlib", "()J", "elapsedNow", "elapsedNow-UwyO8pc", "equals", "", "other", "", "hashCode", "", "minus", "minus-UwyO8pc", "(Lkotlin/time/ComparableTimeMark;)J", "plus", "duration", "plus-LRDsOJo", "(J)Lkotlin/time/ComparableTimeMark;", "toString", "", "kotlin-stdlib"})
    @SourceDebugExtension(value={"SMAP\nTimeSources.kt\nKotlin\n*S Kotlin\n*F\n+ 1 TimeSources.kt\nkotlin/time/AbstractLongTimeSource$LongTimeMark\n+ 2 Duration.kt\nkotlin/time/Duration\n*L\n1#1,180:1\n720#2,2:181\n*S KotlinDebug\n*F\n+ 1 TimeSources.kt\nkotlin/time/AbstractLongTimeSource$LongTimeMark\n*L\n66#1:181,2\n*E\n"})
    static final class LongTimeMark
    implements ComparableTimeMark {
        private final long startedAt;
        @NotNull
        private final AbstractLongTimeSource timeSource;
        private final long offset;

        private LongTimeMark(long l2, AbstractLongTimeSource abstractLongTimeSource, long l3) {
            Intrinsics.checkNotNullParameter(abstractLongTimeSource, "");
            this.startedAt = l2;
            this.timeSource = abstractLongTimeSource;
            this.offset = l3;
        }

        @Override
        public final long elapsedNow-UwyO8pc() {
            if (Duration.isInfinite-impl(this.offset)) {
                return Duration.unaryMinus-UwyO8pc(this.offset);
            }
            return Duration.minus-LRDsOJo(DurationKt.toDuration(this.timeSource.read() - this.startedAt, this.timeSource.getUnit()), this.offset);
        }

        @Override
        @NotNull
        public final ComparableTimeMark plus-LRDsOJo(long l2) {
            return new LongTimeMark(this.startedAt, this.timeSource, Duration.plus-LRDsOJo(this.offset, l2), null);
        }

        @Override
        public final long minus-UwyO8pc(@NotNull ComparableTimeMark comparableTimeMark) {
            Intrinsics.checkNotNullParameter(comparableTimeMark, "");
            if (!(comparableTimeMark instanceof LongTimeMark) || !Intrinsics.areEqual(this.timeSource, ((LongTimeMark)comparableTimeMark).timeSource)) {
                throw new IllegalArgumentException("Subtracting or comparing time marks from different time sources is not possible: " + this + " and " + comparableTimeMark);
            }
            if (Duration.equals-impl0(this.offset, ((LongTimeMark)comparableTimeMark).offset) && Duration.isInfinite-impl(this.offset)) {
                return Duration.Companion.getZERO-UwyO8pc();
            }
            long l2 = Duration.minus-LRDsOJo(this.offset, ((LongTimeMark)comparableTimeMark).offset);
            long l3 = DurationKt.toDuration(this.startedAt - ((LongTimeMark)comparableTimeMark).startedAt, this.timeSource.getUnit());
            if (Duration.equals-impl0(l3, Duration.unaryMinus-UwyO8pc(l2))) {
                return Duration.Companion.getZERO-UwyO8pc();
            }
            return Duration.plus-LRDsOJo(l3, l2);
        }

        @Override
        public final boolean equals(@Nullable Object object) {
            return object instanceof LongTimeMark && Intrinsics.areEqual(this.timeSource, ((LongTimeMark)object).timeSource) && Duration.equals-impl0(this.minus-UwyO8pc((ComparableTimeMark)object), Duration.Companion.getZERO-UwyO8pc());
        }

        public final long effectiveDuration-UwyO8pc$kotlin_stdlib() {
            if (Duration.isInfinite-impl(this.offset)) {
                return this.offset;
            }
            DurationUnit durationUnit = this.timeSource.getUnit();
            if (durationUnit.compareTo((Enum)DurationUnit.MILLISECONDS) >= 0) {
                return Duration.plus-LRDsOJo(DurationKt.toDuration(this.startedAt, durationUnit), this.offset);
            }
            long l2 = DurationUnitKt.convertDurationUnit(1L, DurationUnit.MILLISECONDS, durationUnit);
            long l3 = this.startedAt / l2;
            long l4 = this.startedAt % l2;
            long l5 = this.offset;
            int n2 = Duration.getNanosecondsComponent-impl(l5);
            long l6 = Duration.getInWholeSeconds-impl(l5);
            int n3 = n2 / 1000000;
            return Duration.plus-LRDsOJo(Duration.plus-LRDsOJo(Duration.plus-LRDsOJo(DurationKt.toDuration(l4, durationUnit), DurationKt.toDuration(n2 %= 1000000, DurationUnit.NANOSECONDS)), DurationKt.toDuration(l3 + (long)n3, DurationUnit.MILLISECONDS)), DurationKt.toDuration(l6, DurationUnit.SECONDS));
        }

        @Override
        public final int hashCode() {
            return Duration.hashCode-impl(this.effectiveDuration-UwyO8pc$kotlin_stdlib());
        }

        @NotNull
        public final String toString() {
            return "LongTimeMark(" + this.startedAt + DurationUnitKt.shortName(this.timeSource.getUnit()) + " + " + Duration.toString-impl(this.offset) + " (=" + Duration.toString-impl(this.effectiveDuration-UwyO8pc$kotlin_stdlib()) + "), " + this.timeSource + ')';
        }

        @Override
        @NotNull
        public final ComparableTimeMark minus-LRDsOJo(long l2) {
            return ComparableTimeMark.DefaultImpls.minus-LRDsOJo(this, l2);
        }

        @Override
        public final int compareTo(@NotNull ComparableTimeMark comparableTimeMark) {
            return ComparableTimeMark.DefaultImpls.compareTo(this, comparableTimeMark);
        }

        @Override
        public final boolean hasPassedNow() {
            return ComparableTimeMark.DefaultImpls.hasPassedNow(this);
        }

        @Override
        public final boolean hasNotPassedNow() {
            return ComparableTimeMark.DefaultImpls.hasNotPassedNow(this);
        }

        public /* synthetic */ LongTimeMark(long l2, AbstractLongTimeSource abstractLongTimeSource, long l3, DefaultConstructorMarker defaultConstructorMarker) {
            this(l2, abstractLongTimeSource, l3);
        }
    }
}


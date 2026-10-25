/*
 * Decompiled with CFR 0.152.
 */
package kotlin.time;

import kotlin.Deprecated;
import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.time.ComparableTimeMark;
import kotlin.time.Duration;
import kotlin.time.DurationKt;
import kotlin.time.DurationUnit;
import kotlin.time.DurationUnitKt;
import kotlin.time.ExperimentalTime;
import kotlin.time.TimeSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Deprecated(message="Using AbstractDoubleTimeSource is no longer recommended, use AbstractLongTimeSource instead.")
@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\b'\u0018\u00002\u00020\u0001:\u0001\u000bB\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u0007\u001a\u00020\bH\u0016J\b\u0010\t\u001a\u00020\nH$R\u0014\u0010\u0002\u001a\u00020\u0003X\u0084\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\f"}, d2={"Lkotlin/time/AbstractDoubleTimeSource;", "Lkotlin/time/TimeSource$WithComparableMarks;", "unit", "Lkotlin/time/DurationUnit;", "(Lkotlin/time/DurationUnit;)V", "getUnit", "()Lkotlin/time/DurationUnit;", "markNow", "Lkotlin/time/ComparableTimeMark;", "read", "", "DoubleTimeMark", "kotlin-stdlib"})
@SinceKotlin(version="1.3")
@ExperimentalTime
public abstract class AbstractDoubleTimeSource
implements TimeSource.WithComparableMarks {
    @NotNull
    private final DurationUnit unit;

    public AbstractDoubleTimeSource(@NotNull DurationUnit durationUnit) {
        Intrinsics.checkNotNullParameter((Object)durationUnit, "");
        this.unit = durationUnit;
    }

    @NotNull
    protected final DurationUnit getUnit() {
        return this.unit;
    }

    protected abstract double read();

    @Override
    @NotNull
    public ComparableTimeMark markNow() {
        return new DoubleTimeMark(this.read(), this, Duration.Companion.getZERO-UwyO8pc(), null);
    }

    /*
     * Illegal identifiers - consider using --renameillegalidents true
     */
    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010\u000e\n\u0000\b\u0002\u0018\u00002\u00020\u0001B \u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\bJ\u0015\u0010\n\u001a\u00020\u0007H\u0016\u00f8\u0001\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u000b\u0010\fJ\u0013\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0096\u0002J\b\u0010\u0011\u001a\u00020\u0012H\u0016J\u001e\u0010\u0013\u001a\u00020\u00072\u0006\u0010\u000f\u001a\u00020\u0001H\u0096\u0002\u00f8\u0001\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0014\u0010\u0015J\u001b\u0010\u0016\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0007H\u0096\u0002\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0018\u0010\u0019J\b\u0010\u001a\u001a\u00020\u001bH\u0016R\u0016\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\n\u0002\u0010\tR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\b\n\u0002\b\u0019\n\u0002\b!\u00a8\u0006\u001c"}, d2={"Lkotlin/time/AbstractDoubleTimeSource$DoubleTimeMark;", "Lkotlin/time/ComparableTimeMark;", "startedAt", "", "timeSource", "Lkotlin/time/AbstractDoubleTimeSource;", "offset", "Lkotlin/time/Duration;", "(DLkotlin/time/AbstractDoubleTimeSource;JLkotlin/jvm/internal/DefaultConstructorMarker;)V", "J", "elapsedNow", "elapsedNow-UwyO8pc", "()J", "equals", "", "other", "", "hashCode", "", "minus", "minus-UwyO8pc", "(Lkotlin/time/ComparableTimeMark;)J", "plus", "duration", "plus-LRDsOJo", "(J)Lkotlin/time/ComparableTimeMark;", "toString", "", "kotlin-stdlib"})
    static final class DoubleTimeMark
    implements ComparableTimeMark {
        private final double startedAt;
        @NotNull
        private final AbstractDoubleTimeSource timeSource;
        private final long offset;

        private DoubleTimeMark(double d2, AbstractDoubleTimeSource abstractDoubleTimeSource, long l2) {
            Intrinsics.checkNotNullParameter(abstractDoubleTimeSource, "");
            this.startedAt = d2;
            this.timeSource = abstractDoubleTimeSource;
            this.offset = l2;
        }

        @Override
        public final long elapsedNow-UwyO8pc() {
            return Duration.minus-LRDsOJo(DurationKt.toDuration(this.timeSource.read() - this.startedAt, this.timeSource.getUnit()), this.offset);
        }

        @Override
        @NotNull
        public final ComparableTimeMark plus-LRDsOJo(long l2) {
            return new DoubleTimeMark(this.startedAt, this.timeSource, Duration.plus-LRDsOJo(this.offset, l2), null);
        }

        @Override
        public final long minus-UwyO8pc(@NotNull ComparableTimeMark comparableTimeMark) {
            Intrinsics.checkNotNullParameter(comparableTimeMark, "");
            if (!(comparableTimeMark instanceof DoubleTimeMark) || !Intrinsics.areEqual(this.timeSource, ((DoubleTimeMark)comparableTimeMark).timeSource)) {
                throw new IllegalArgumentException("Subtracting or comparing time marks from different time sources is not possible: " + this + " and " + comparableTimeMark);
            }
            if (Duration.equals-impl0(this.offset, ((DoubleTimeMark)comparableTimeMark).offset) && Duration.isInfinite-impl(this.offset)) {
                return Duration.Companion.getZERO-UwyO8pc();
            }
            long l2 = Duration.minus-LRDsOJo(this.offset, ((DoubleTimeMark)comparableTimeMark).offset);
            long l3 = DurationKt.toDuration(this.startedAt - ((DoubleTimeMark)comparableTimeMark).startedAt, this.timeSource.getUnit());
            if (Duration.equals-impl0(l3, Duration.unaryMinus-UwyO8pc(l2))) {
                return Duration.Companion.getZERO-UwyO8pc();
            }
            return Duration.plus-LRDsOJo(l3, l2);
        }

        @Override
        public final boolean equals(@Nullable Object object) {
            return object instanceof DoubleTimeMark && Intrinsics.areEqual(this.timeSource, ((DoubleTimeMark)object).timeSource) && Duration.equals-impl0(this.minus-UwyO8pc((ComparableTimeMark)object), Duration.Companion.getZERO-UwyO8pc());
        }

        @Override
        public final int hashCode() {
            return Duration.hashCode-impl(Duration.plus-LRDsOJo(DurationKt.toDuration(this.startedAt, this.timeSource.getUnit()), this.offset));
        }

        @NotNull
        public final String toString() {
            return "DoubleTimeMark(" + this.startedAt + DurationUnitKt.shortName(this.timeSource.getUnit()) + " + " + Duration.toString-impl(this.offset) + ", " + this.timeSource + ')';
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

        public /* synthetic */ DoubleTimeMark(double d2, AbstractDoubleTimeSource abstractDoubleTimeSource, long l2, DefaultConstructorMarker defaultConstructorMarker) {
            this(d2, abstractDoubleTimeSource, l2);
        }
    }
}


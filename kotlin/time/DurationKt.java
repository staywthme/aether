/*
 * Decompiled with CFR 0.152.
 */
package kotlin.time;

import kotlin.Deprecated;
import kotlin.DeprecatedSinceKotlin;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.SinceKotlin;
import kotlin.WasExperimental;
import kotlin.collections.IntIterator;
import kotlin.internal.InlineOnly;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.math.MathKt;
import kotlin.ranges.CharRange;
import kotlin.ranges.IntRange;
import kotlin.ranges.LongRange;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
import kotlin.time.Duration;
import kotlin.time.DurationUnit;
import kotlin.time.DurationUnitKt;
import kotlin.time.ExperimentalTime;
import org.jetbrains.annotations.NotNull;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(mv={1, 8, 0}, k=2, xi=48, d1={"\u0000>\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0006\n\u0002\b*\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\f\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a \u0010#\u001a\u00020\u00072\u0006\u0010$\u001a\u00020\u00012\u0006\u0010%\u001a\u00020\u0005H\u0002\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010&\u001a\u0018\u0010'\u001a\u00020\u00072\u0006\u0010(\u001a\u00020\u0001H\u0002\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0010\u001a\u0018\u0010)\u001a\u00020\u00072\u0006\u0010*\u001a\u00020\u0001H\u0002\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0010\u001a\u0018\u0010+\u001a\u00020\u00072\u0006\u0010,\u001a\u00020\u0001H\u0002\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0010\u001a\u0018\u0010-\u001a\u00020\u00072\u0006\u0010.\u001a\u00020\u0001H\u0002\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0010\u001a\u0010\u0010/\u001a\u00020\u00012\u0006\u0010*\u001a\u00020\u0001H\u0002\u001a\u0010\u00100\u001a\u00020\u00012\u0006\u0010.\u001a\u00020\u0001H\u0002\u001a \u00101\u001a\u00020\u00072\u0006\u00102\u001a\u0002032\u0006\u00104\u001a\u000205H\u0002\u00f8\u0001\u0000\u00a2\u0006\u0002\u00106\u001a\u0010\u00107\u001a\u00020\u00012\u0006\u00102\u001a\u000203H\u0002\u001a)\u00108\u001a\u00020\u0005*\u0002032\u0006\u00109\u001a\u00020\u00052\u0012\u0010:\u001a\u000e\u0012\u0004\u0012\u00020<\u0012\u0004\u0012\u0002050;H\u0082\b\u001a)\u0010=\u001a\u000203*\u0002032\u0006\u00109\u001a\u00020\u00052\u0012\u0010:\u001a\u000e\u0012\u0004\u0012\u00020<\u0012\u0004\u0012\u0002050;H\u0082\b\u001a\u001f\u0010>\u001a\u00020\u0007*\u00020\b2\u0006\u0010?\u001a\u00020\u0007H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b@\u0010A\u001a\u001f\u0010>\u001a\u00020\u0007*\u00020\u00052\u0006\u0010?\u001a\u00020\u0007H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\bB\u0010C\u001a\u001c\u0010D\u001a\u00020\u0007*\u00020\b2\u0006\u0010E\u001a\u00020FH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010G\u001a\u001c\u0010D\u001a\u00020\u0007*\u00020\u00052\u0006\u0010E\u001a\u00020FH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010H\u001a\u001c\u0010D\u001a\u00020\u0007*\u00020\u00012\u0006\u0010E\u001a\u00020FH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010I\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0080T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001X\u0080T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0003\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0004\u001a\u00020\u0005X\u0080T\u00a2\u0006\u0002\n\u0000\"!\u0010\u0006\u001a\u00020\u0007*\u00020\b8FX\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\f\u0012\u0004\b\t\u0010\n\u001a\u0004\b\u000b\u0010\f\"!\u0010\u0006\u001a\u00020\u0007*\u00020\u00058FX\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\f\u0012\u0004\b\t\u0010\r\u001a\u0004\b\u000b\u0010\u000e\"!\u0010\u0006\u001a\u00020\u0007*\u00020\u00018FX\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\f\u0012\u0004\b\t\u0010\u000f\u001a\u0004\b\u000b\u0010\u0010\"!\u0010\u0011\u001a\u00020\u0007*\u00020\b8FX\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\f\u0012\u0004\b\u0012\u0010\n\u001a\u0004\b\u0013\u0010\f\"!\u0010\u0011\u001a\u00020\u0007*\u00020\u00058FX\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\f\u0012\u0004\b\u0012\u0010\r\u001a\u0004\b\u0013\u0010\u000e\"!\u0010\u0011\u001a\u00020\u0007*\u00020\u00018FX\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\f\u0012\u0004\b\u0012\u0010\u000f\u001a\u0004\b\u0013\u0010\u0010\"!\u0010\u0014\u001a\u00020\u0007*\u00020\b8FX\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\f\u0012\u0004\b\u0015\u0010\n\u001a\u0004\b\u0016\u0010\f\"!\u0010\u0014\u001a\u00020\u0007*\u00020\u00058FX\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\f\u0012\u0004\b\u0015\u0010\r\u001a\u0004\b\u0016\u0010\u000e\"!\u0010\u0014\u001a\u00020\u0007*\u00020\u00018FX\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\f\u0012\u0004\b\u0015\u0010\u000f\u001a\u0004\b\u0016\u0010\u0010\"!\u0010\u0017\u001a\u00020\u0007*\u00020\b8FX\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\f\u0012\u0004\b\u0018\u0010\n\u001a\u0004\b\u0019\u0010\f\"!\u0010\u0017\u001a\u00020\u0007*\u00020\u00058FX\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\f\u0012\u0004\b\u0018\u0010\r\u001a\u0004\b\u0019\u0010\u000e\"!\u0010\u0017\u001a\u00020\u0007*\u00020\u00018FX\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\f\u0012\u0004\b\u0018\u0010\u000f\u001a\u0004\b\u0019\u0010\u0010\"!\u0010\u001a\u001a\u00020\u0007*\u00020\b8FX\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\f\u0012\u0004\b\u001b\u0010\n\u001a\u0004\b\u001c\u0010\f\"!\u0010\u001a\u001a\u00020\u0007*\u00020\u00058FX\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\f\u0012\u0004\b\u001b\u0010\r\u001a\u0004\b\u001c\u0010\u000e\"!\u0010\u001a\u001a\u00020\u0007*\u00020\u00018FX\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\f\u0012\u0004\b\u001b\u0010\u000f\u001a\u0004\b\u001c\u0010\u0010\"!\u0010\u001d\u001a\u00020\u0007*\u00020\b8FX\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\f\u0012\u0004\b\u001e\u0010\n\u001a\u0004\b\u001f\u0010\f\"!\u0010\u001d\u001a\u00020\u0007*\u00020\u00058FX\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\f\u0012\u0004\b\u001e\u0010\r\u001a\u0004\b\u001f\u0010\u000e\"!\u0010\u001d\u001a\u00020\u0007*\u00020\u00018FX\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\f\u0012\u0004\b\u001e\u0010\u000f\u001a\u0004\b\u001f\u0010\u0010\"!\u0010 \u001a\u00020\u0007*\u00020\b8FX\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\f\u0012\u0004\b!\u0010\n\u001a\u0004\b\"\u0010\f\"!\u0010 \u001a\u00020\u0007*\u00020\u00058FX\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\f\u0012\u0004\b!\u0010\r\u001a\u0004\b\"\u0010\u000e\"!\u0010 \u001a\u00020\u0007*\u00020\u00018FX\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\f\u0012\u0004\b!\u0010\u000f\u001a\u0004\b\"\u0010\u0010\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006J"}, d2={"MAX_MILLIS", "", "MAX_NANOS", "MAX_NANOS_IN_MILLIS", "NANOS_IN_MILLIS", "", "days", "Lkotlin/time/Duration;", "", "getDays$annotations", "(D)V", "getDays", "(D)J", "(I)V", "(I)J", "(J)V", "(J)J", "hours", "getHours$annotations", "getHours", "microseconds", "getMicroseconds$annotations", "getMicroseconds", "milliseconds", "getMilliseconds$annotations", "getMilliseconds", "minutes", "getMinutes$annotations", "getMinutes", "nanoseconds", "getNanoseconds$annotations", "getNanoseconds", "seconds", "getSeconds$annotations", "getSeconds", "durationOf", "normalValue", "unitDiscriminator", "(JI)J", "durationOfMillis", "normalMillis", "durationOfMillisNormalized", "millis", "durationOfNanos", "normalNanos", "durationOfNanosNormalized", "nanos", "millisToNanos", "nanosToMillis", "parseDuration", "value", "", "strictIso", "", "(Ljava/lang/String;Z)J", "parseOverLongIsoComponent", "skipWhile", "startIndex", "predicate", "Lkotlin/Function1;", "", "substringWhile", "times", "duration", "times-kIfJnKk", "(DJ)J", "times-mvk6XK0", "(IJ)J", "toDuration", "unit", "Lkotlin/time/DurationUnit;", "(DLkotlin/time/DurationUnit;)J", "(ILkotlin/time/DurationUnit;)J", "(JLkotlin/time/DurationUnit;)J", "kotlin-stdlib"})
@SourceDebugExtension(value={"SMAP\nDuration.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Duration.kt\nkotlin/time/DurationKt\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,1484:1\n1436#1,6:1486\n1439#1,3:1492\n1436#1,6:1495\n1436#1,6:1501\n1439#1,3:1510\n1#2:1485\n1726#3,3:1507\n*S KotlinDebug\n*F\n+ 1 Duration.kt\nkotlin/time/DurationKt\n*L\n1360#1:1486,6\n1394#1:1492,3\n1397#1:1495,6\n1400#1:1501,6\n1436#1:1510,3\n1425#1:1507,3\n*E\n"})
public final class DurationKt {
    public static final int NANOS_IN_MILLIS = 1000000;
    public static final long MAX_NANOS = 4611686018426999999L;
    public static final long MAX_MILLIS = 0x3FFFFFFFFFFFFFFFL;
    private static final long MAX_NANOS_IN_MILLIS = 4611686018426L;

    @SinceKotlin(version="1.6")
    @WasExperimental(markerClass={ExperimentalTime.class})
    public static final long toDuration(int n2, @NotNull DurationUnit durationUnit) {
        Intrinsics.checkNotNullParameter((Object)durationUnit, "");
        if (durationUnit.compareTo((Enum)DurationUnit.SECONDS) <= 0) {
            return DurationKt.durationOfNanos(DurationUnitKt.convertDurationUnitOverflow(n2, durationUnit, DurationUnit.NANOSECONDS));
        }
        return DurationKt.toDuration((long)n2, durationUnit);
    }

    @SinceKotlin(version="1.6")
    @WasExperimental(markerClass={ExperimentalTime.class})
    public static final long toDuration(long l2, @NotNull DurationUnit durationUnit) {
        Intrinsics.checkNotNullParameter((Object)durationUnit, "");
        long l3 = DurationUnitKt.convertDurationUnitOverflow(4611686018426999999L, DurationUnit.NANOSECONDS, durationUnit);
        if (new LongRange(-l3, l3).contains(l2)) {
            return DurationKt.durationOfNanos(DurationUnitKt.convertDurationUnitOverflow(l2, durationUnit, DurationUnit.NANOSECONDS));
        }
        long l4 = DurationUnitKt.convertDurationUnit(l2, durationUnit, DurationUnit.MILLISECONDS);
        return DurationKt.durationOfMillis(RangesKt.coerceIn(l4, -4611686018427387903L, 0x3FFFFFFFFFFFFFFFL));
    }

    @SinceKotlin(version="1.6")
    @WasExperimental(markerClass={ExperimentalTime.class})
    public static final long toDuration(double d2, @NotNull DurationUnit durationUnit) {
        Intrinsics.checkNotNullParameter((Object)durationUnit, "");
        double d3 = DurationUnitKt.convertDurationUnit(d2, durationUnit, DurationUnit.NANOSECONDS);
        boolean bl = !Double.isNaN(d3);
        if (!bl) {
            String string = "Duration value cannot be NaN.";
            throw new IllegalArgumentException(string.toString());
        }
        long l2 = MathKt.roundToLong(d3);
        if (new LongRange(-4611686018426999999L, 4611686018426999999L).contains(l2)) {
            return DurationKt.durationOfNanos(l2);
        }
        long l3 = MathKt.roundToLong(DurationUnitKt.convertDurationUnit(d2, durationUnit, DurationUnit.MILLISECONDS));
        return DurationKt.durationOfMillisNormalized(l3);
    }

    public static final long getNanoseconds(int n2) {
        return DurationKt.toDuration(n2, DurationUnit.NANOSECONDS);
    }

    @Deprecated(message="Use 'Int.nanoseconds' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="this.nanoseconds", imports={"kotlin.time.Duration.Companion.nanoseconds"}))
    @DeprecatedSinceKotlin(warningSince="1.5", errorSince="1.8")
    @SinceKotlin(version="1.3")
    @ExperimentalTime
    public static /* synthetic */ void getNanoseconds$annotations(int n2) {
    }

    public static final long getNanoseconds(long l2) {
        return DurationKt.toDuration(l2, DurationUnit.NANOSECONDS);
    }

    @Deprecated(message="Use 'Long.nanoseconds' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="this.nanoseconds", imports={"kotlin.time.Duration.Companion.nanoseconds"}))
    @DeprecatedSinceKotlin(warningSince="1.5", errorSince="1.8")
    @SinceKotlin(version="1.3")
    @ExperimentalTime
    public static /* synthetic */ void getNanoseconds$annotations(long l2) {
    }

    public static final long getNanoseconds(double d2) {
        return DurationKt.toDuration(d2, DurationUnit.NANOSECONDS);
    }

    @Deprecated(message="Use 'Double.nanoseconds' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="this.nanoseconds", imports={"kotlin.time.Duration.Companion.nanoseconds"}))
    @DeprecatedSinceKotlin(warningSince="1.5", errorSince="1.8")
    @SinceKotlin(version="1.3")
    @ExperimentalTime
    public static /* synthetic */ void getNanoseconds$annotations(double d2) {
    }

    public static final long getMicroseconds(int n2) {
        return DurationKt.toDuration(n2, DurationUnit.MICROSECONDS);
    }

    @Deprecated(message="Use 'Int.microseconds' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="this.microseconds", imports={"kotlin.time.Duration.Companion.microseconds"}))
    @DeprecatedSinceKotlin(warningSince="1.5", errorSince="1.8")
    @SinceKotlin(version="1.3")
    @ExperimentalTime
    public static /* synthetic */ void getMicroseconds$annotations(int n2) {
    }

    public static final long getMicroseconds(long l2) {
        return DurationKt.toDuration(l2, DurationUnit.MICROSECONDS);
    }

    @Deprecated(message="Use 'Long.microseconds' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="this.microseconds", imports={"kotlin.time.Duration.Companion.microseconds"}))
    @DeprecatedSinceKotlin(warningSince="1.5", errorSince="1.8")
    @SinceKotlin(version="1.3")
    @ExperimentalTime
    public static /* synthetic */ void getMicroseconds$annotations(long l2) {
    }

    public static final long getMicroseconds(double d2) {
        return DurationKt.toDuration(d2, DurationUnit.MICROSECONDS);
    }

    @Deprecated(message="Use 'Double.microseconds' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="this.microseconds", imports={"kotlin.time.Duration.Companion.microseconds"}))
    @DeprecatedSinceKotlin(warningSince="1.5", errorSince="1.8")
    @SinceKotlin(version="1.3")
    @ExperimentalTime
    public static /* synthetic */ void getMicroseconds$annotations(double d2) {
    }

    public static final long getMilliseconds(int n2) {
        return DurationKt.toDuration(n2, DurationUnit.MILLISECONDS);
    }

    @Deprecated(message="Use 'Int.milliseconds' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="this.milliseconds", imports={"kotlin.time.Duration.Companion.milliseconds"}))
    @DeprecatedSinceKotlin(warningSince="1.5", errorSince="1.8")
    @SinceKotlin(version="1.3")
    @ExperimentalTime
    public static /* synthetic */ void getMilliseconds$annotations(int n2) {
    }

    public static final long getMilliseconds(long l2) {
        return DurationKt.toDuration(l2, DurationUnit.MILLISECONDS);
    }

    @Deprecated(message="Use 'Long.milliseconds' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="this.milliseconds", imports={"kotlin.time.Duration.Companion.milliseconds"}))
    @DeprecatedSinceKotlin(warningSince="1.5", errorSince="1.8")
    @SinceKotlin(version="1.3")
    @ExperimentalTime
    public static /* synthetic */ void getMilliseconds$annotations(long l2) {
    }

    public static final long getMilliseconds(double d2) {
        return DurationKt.toDuration(d2, DurationUnit.MILLISECONDS);
    }

    @Deprecated(message="Use 'Double.milliseconds' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="this.milliseconds", imports={"kotlin.time.Duration.Companion.milliseconds"}))
    @DeprecatedSinceKotlin(warningSince="1.5", errorSince="1.8")
    @SinceKotlin(version="1.3")
    @ExperimentalTime
    public static /* synthetic */ void getMilliseconds$annotations(double d2) {
    }

    public static final long getSeconds(int n2) {
        return DurationKt.toDuration(n2, DurationUnit.SECONDS);
    }

    @Deprecated(message="Use 'Int.seconds' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="this.seconds", imports={"kotlin.time.Duration.Companion.seconds"}))
    @DeprecatedSinceKotlin(warningSince="1.5", errorSince="1.8")
    @SinceKotlin(version="1.3")
    @ExperimentalTime
    public static /* synthetic */ void getSeconds$annotations(int n2) {
    }

    public static final long getSeconds(long l2) {
        return DurationKt.toDuration(l2, DurationUnit.SECONDS);
    }

    @Deprecated(message="Use 'Long.seconds' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="this.seconds", imports={"kotlin.time.Duration.Companion.seconds"}))
    @DeprecatedSinceKotlin(warningSince="1.5", errorSince="1.8")
    @SinceKotlin(version="1.3")
    @ExperimentalTime
    public static /* synthetic */ void getSeconds$annotations(long l2) {
    }

    public static final long getSeconds(double d2) {
        return DurationKt.toDuration(d2, DurationUnit.SECONDS);
    }

    @Deprecated(message="Use 'Double.seconds' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="this.seconds", imports={"kotlin.time.Duration.Companion.seconds"}))
    @DeprecatedSinceKotlin(warningSince="1.5", errorSince="1.8")
    @SinceKotlin(version="1.3")
    @ExperimentalTime
    public static /* synthetic */ void getSeconds$annotations(double d2) {
    }

    public static final long getMinutes(int n2) {
        return DurationKt.toDuration(n2, DurationUnit.MINUTES);
    }

    @Deprecated(message="Use 'Int.minutes' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="this.minutes", imports={"kotlin.time.Duration.Companion.minutes"}))
    @DeprecatedSinceKotlin(warningSince="1.5", errorSince="1.8")
    @SinceKotlin(version="1.3")
    @ExperimentalTime
    public static /* synthetic */ void getMinutes$annotations(int n2) {
    }

    public static final long getMinutes(long l2) {
        return DurationKt.toDuration(l2, DurationUnit.MINUTES);
    }

    @Deprecated(message="Use 'Long.minutes' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="this.minutes", imports={"kotlin.time.Duration.Companion.minutes"}))
    @DeprecatedSinceKotlin(warningSince="1.5", errorSince="1.8")
    @SinceKotlin(version="1.3")
    @ExperimentalTime
    public static /* synthetic */ void getMinutes$annotations(long l2) {
    }

    public static final long getMinutes(double d2) {
        return DurationKt.toDuration(d2, DurationUnit.MINUTES);
    }

    @Deprecated(message="Use 'Double.minutes' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="this.minutes", imports={"kotlin.time.Duration.Companion.minutes"}))
    @DeprecatedSinceKotlin(warningSince="1.5", errorSince="1.8")
    @SinceKotlin(version="1.3")
    @ExperimentalTime
    public static /* synthetic */ void getMinutes$annotations(double d2) {
    }

    public static final long getHours(int n2) {
        return DurationKt.toDuration(n2, DurationUnit.HOURS);
    }

    @Deprecated(message="Use 'Int.hours' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="this.hours", imports={"kotlin.time.Duration.Companion.hours"}))
    @DeprecatedSinceKotlin(warningSince="1.5", errorSince="1.8")
    @SinceKotlin(version="1.3")
    @ExperimentalTime
    public static /* synthetic */ void getHours$annotations(int n2) {
    }

    public static final long getHours(long l2) {
        return DurationKt.toDuration(l2, DurationUnit.HOURS);
    }

    @Deprecated(message="Use 'Long.hours' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="this.hours", imports={"kotlin.time.Duration.Companion.hours"}))
    @DeprecatedSinceKotlin(warningSince="1.5", errorSince="1.8")
    @SinceKotlin(version="1.3")
    @ExperimentalTime
    public static /* synthetic */ void getHours$annotations(long l2) {
    }

    public static final long getHours(double d2) {
        return DurationKt.toDuration(d2, DurationUnit.HOURS);
    }

    @Deprecated(message="Use 'Double.hours' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="this.hours", imports={"kotlin.time.Duration.Companion.hours"}))
    @DeprecatedSinceKotlin(warningSince="1.5", errorSince="1.8")
    @SinceKotlin(version="1.3")
    @ExperimentalTime
    public static /* synthetic */ void getHours$annotations(double d2) {
    }

    public static final long getDays(int n2) {
        return DurationKt.toDuration(n2, DurationUnit.DAYS);
    }

    @Deprecated(message="Use 'Int.days' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="this.days", imports={"kotlin.time.Duration.Companion.days"}))
    @DeprecatedSinceKotlin(warningSince="1.5", errorSince="1.8")
    @SinceKotlin(version="1.3")
    @ExperimentalTime
    public static /* synthetic */ void getDays$annotations(int n2) {
    }

    public static final long getDays(long l2) {
        return DurationKt.toDuration(l2, DurationUnit.DAYS);
    }

    @Deprecated(message="Use 'Long.days' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="this.days", imports={"kotlin.time.Duration.Companion.days"}))
    @DeprecatedSinceKotlin(warningSince="1.5", errorSince="1.8")
    @SinceKotlin(version="1.3")
    @ExperimentalTime
    public static /* synthetic */ void getDays$annotations(long l2) {
    }

    public static final long getDays(double d2) {
        return DurationKt.toDuration(d2, DurationUnit.DAYS);
    }

    @Deprecated(message="Use 'Double.days' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="this.days", imports={"kotlin.time.Duration.Companion.days"}))
    @DeprecatedSinceKotlin(warningSince="1.5", errorSince="1.8")
    @SinceKotlin(version="1.3")
    @ExperimentalTime
    public static /* synthetic */ void getDays$annotations(double d2) {
    }

    @SinceKotlin(version="1.6")
    @WasExperimental(markerClass={ExperimentalTime.class})
    @InlineOnly
    private static final long times-mvk6XK0(int n2, long l2) {
        return Duration.times-UwyO8pc(l2, n2);
    }

    @SinceKotlin(version="1.6")
    @WasExperimental(markerClass={ExperimentalTime.class})
    @InlineOnly
    private static final long times-kIfJnKk(double d2, long l2) {
        return Duration.times-UwyO8pc(l2, d2);
    }

    private static final long parseDuration(String string, boolean bl) {
        boolean bl2;
        int n2 = string.length();
        if (n2 == 0) {
            throw new IllegalArgumentException("The string is empty");
        }
        int n3 = 0;
        long l2 = Duration.Companion.getZERO-UwyO8pc();
        String string2 = "Infinity";
        int n4 = string.charAt(0);
        if (n4 == 43 ? true : n4 == 45) {
            ++n3;
        }
        n4 = n3 > 0 ? 1 : 0;
        boolean bl3 = bl2 = n4 != 0 && StringsKt.startsWith$default((CharSequence)string, '-', false, 2, null);
        if (n2 <= n3) {
            throw new IllegalArgumentException("No components");
        }
        if (string.charAt(n3) == 'P') {
            if (++n3 == n2) {
                throw new IllegalArgumentException();
            }
            String string3 = "+-.";
            boolean bl4 = false;
            Object object = null;
            while (n3 < n2) {
                String string4;
                char c2;
                if (string.charAt(n3) == 'T') {
                    if (bl4 || ++n3 == n2) {
                        throw new IllegalArgumentException();
                    }
                    bl4 = true;
                    continue;
                }
                Object object2 = string;
                String string5 = object2;
                String string6 = object2;
                for (n4 = n3; n4 < string6.length() && (new CharRange('0', '9').contains(c2 = string6.charAt(n4)) || StringsKt.contains$default((CharSequence)string3, c2, false, 2, null)); ++n4) {
                }
                int n5 = n4;
                Intrinsics.checkNotNull(string5);
                Intrinsics.checkNotNullExpressionValue(string5.substring(n3, n5), "");
                if (((CharSequence)string4).length() == 0) {
                    throw new IllegalArgumentException();
                }
                CharSequence charSequence = string;
                if ((n3 += string4.length()) < 0 || n3 > StringsKt.getLastIndex(charSequence)) {
                    throw new IllegalArgumentException("Missing unit for value " + string4);
                }
                char c3 = charSequence.charAt(n3);
                ++n3;
                object2 = DurationUnitKt.durationUnitByIsoChar(c3, bl4);
                if (object != null && ((Enum)object).compareTo((Enum)object2) <= 0) {
                    throw new IllegalArgumentException("Unexpected order of duration components");
                }
                object = object2;
                int n6 = StringsKt.indexOf$default((CharSequence)string4, '.', 0, false, 6, null);
                if (object2 == DurationUnit.SECONDS && n6 > 0) {
                    String string7;
                    String string8 = string4;
                    Intrinsics.checkNotNull(string8);
                    Intrinsics.checkNotNullExpressionValue(string8.substring(0, n6), "");
                    l2 = Duration.plus-LRDsOJo(l2, DurationKt.toDuration(DurationKt.parseOverLongIsoComponent(string7), (DurationUnit)((Object)object2)));
                    String string9 = string4;
                    Intrinsics.checkNotNull(string9);
                    String string10 = string9.substring(n6);
                    Intrinsics.checkNotNullExpressionValue(string10, "");
                    l2 = Duration.plus-LRDsOJo(l2, DurationKt.toDuration(Double.parseDouble(string10), (DurationUnit)((Object)object2)));
                    continue;
                }
                l2 = Duration.plus-LRDsOJo(l2, DurationKt.toDuration(DurationKt.parseOverLongIsoComponent(string4), (DurationUnit)((Object)object2)));
            }
        } else {
            if (bl) {
                throw new IllegalArgumentException();
            }
            if (StringsKt.regionMatches(string, n3, string2, 0, Math.max(n2 - n3, string2.length()), true)) {
                l2 = Duration.Companion.getINFINITE-UwyO8pc();
            } else {
                boolean bl5;
                Object object = null;
                boolean bl6 = false;
                boolean bl7 = bl5 = n4 == 0;
                if (n4 != 0 && string.charAt(1) == '(' && StringsKt.last(string) == ')') {
                    bl5 = true;
                    ++n3;
                    if (2 == --n2) {
                        throw new IllegalArgumentException("No components");
                    }
                }
                while (n3 < n2) {
                    char c4;
                    int n7;
                    String string11;
                    String string12;
                    char c5;
                    int n8;
                    String string13;
                    int n9;
                    if (bl6 && bl5) {
                        int n10;
                        String string14 = string;
                        for (n10 = n3; n10 < string14.length() && (n9 = string14.charAt(n10)) == 32; ++n10) {
                        }
                        n3 = n10;
                    }
                    bl6 = true;
                    String string15 = string13 = string;
                    String string16 = string13;
                    for (n8 = n3; n8 < string16.length() && (new CharRange('0', '9').contains(c5 = string16.charAt(n8)) || c5 == '.'); ++n8) {
                    }
                    int n11 = n8;
                    Intrinsics.checkNotNull(string15);
                    Intrinsics.checkNotNullExpressionValue(string15.substring(n3, n11), "");
                    if (((CharSequence)string12).length() == 0) {
                        throw new IllegalArgumentException();
                    }
                    string16 = string11 = string;
                    String string17 = string11;
                    for (n7 = n3 += string12.length(); n7 < string17.length() && new CharRange('a', 'z').contains(c4 = string17.charAt(n7)); ++n7) {
                    }
                    int n12 = n7;
                    Intrinsics.checkNotNull(string16);
                    String string18 = string16.substring(n3, n12);
                    Intrinsics.checkNotNullExpressionValue(string18, "");
                    Object object3 = string18;
                    n3 += ((String)object3).length();
                    object3 = DurationUnitKt.durationUnitByShortName((String)object3);
                    if (object != null && ((Enum)object).compareTo((Enum)object3) <= 0) {
                        throw new IllegalArgumentException("Unexpected order of duration components");
                    }
                    object = object3;
                    n9 = StringsKt.indexOf$default((CharSequence)string12, '.', 0, false, 6, null);
                    if (n9 > 0) {
                        String string19 = string12;
                        Intrinsics.checkNotNull(string19);
                        String string20 = string19.substring(0, n9);
                        Intrinsics.checkNotNullExpressionValue(string20, "");
                        string16 = string20;
                        l2 = Duration.plus-LRDsOJo(l2, DurationKt.toDuration(Long.parseLong(string16), (DurationUnit)((Object)object3)));
                        String string21 = string12;
                        Intrinsics.checkNotNull(string21);
                        String string22 = string21.substring(n9);
                        Intrinsics.checkNotNullExpressionValue(string22, "");
                        l2 = Duration.plus-LRDsOJo(l2, DurationKt.toDuration(Double.parseDouble(string22), (DurationUnit)((Object)object3)));
                        if (n3 >= n2) continue;
                        throw new IllegalArgumentException("Fractional component must be last");
                    }
                    l2 = Duration.plus-LRDsOJo(l2, DurationKt.toDuration(Long.parseLong(string12), (DurationUnit)((Object)object3)));
                }
            }
        }
        if (bl2) {
            return Duration.unaryMinus-UwyO8pc(l2);
        }
        return l2;
    }

    private static final long parseOverLongIsoComponent(String string) {
        int n2 = string.length();
        int n3 = 0;
        if (n2 > 0 && StringsKt.contains$default((CharSequence)"+-", string.charAt(0), false, 2, null)) {
            ++n3;
        }
        if (n2 - n3 > 16) {
            boolean bl;
            block6: {
                Object object = new IntRange(n3, StringsKt.getLastIndex(string));
                object = object.iterator();
                while (object.hasNext()) {
                    n3 = ((IntIterator)object).nextInt();
                    if (new CharRange('0', '9').contains(string.charAt(n3))) continue;
                    bl = false;
                    break block6;
                }
                bl = true;
            }
            if (bl) {
                if (string.charAt(0) == '-') {
                    return Long.MIN_VALUE;
                }
                return Long.MAX_VALUE;
            }
        }
        if (StringsKt.startsWith$default(string, "+", false, 2, null)) {
            return Long.parseLong(StringsKt.drop(string, 1));
        }
        return Long.parseLong(string);
    }

    private static final String substringWhile(String string, int n2, Function1<? super Character, Boolean> function1) {
        int n3;
        String string2 = string;
        for (n3 = n2; n3 < string.length() && function1.invoke(Character.valueOf(string.charAt(n3))).booleanValue(); ++n3) {
        }
        int n4 = n3;
        Intrinsics.checkNotNull(string2);
        String string3 = string2.substring(n2, n4);
        Intrinsics.checkNotNullExpressionValue(string3, "");
        return string3;
    }

    private static final int skipWhile(String string, int n2, Function1<? super Character, Boolean> function1) {
        while (n2 < string.length() && function1.invoke(Character.valueOf(string.charAt(n2))).booleanValue()) {
            ++n2;
        }
        return n2;
    }

    private static final long nanosToMillis(long l2) {
        return l2 / 1000000L;
    }

    private static final long millisToNanos(long l2) {
        return l2 * 1000000L;
    }

    private static final long durationOfNanos(long l2) {
        return Duration.constructor-impl(l2 << 1);
    }

    private static final long durationOfMillis(long l2) {
        return Duration.constructor-impl((l2 << 1) + 1L);
    }

    private static final long durationOf(long l2, int n2) {
        return Duration.constructor-impl((l2 << 1) + (long)n2);
    }

    private static final long durationOfNanosNormalized(long l2) {
        if (new LongRange(-4611686018426999999L, 4611686018426999999L).contains(l2)) {
            return DurationKt.durationOfNanos(l2);
        }
        return DurationKt.durationOfMillis(DurationKt.nanosToMillis(l2));
    }

    private static final long durationOfMillisNormalized(long l2) {
        if (new LongRange(-4611686018426L, 4611686018426L).contains(l2)) {
            return DurationKt.durationOfNanos(DurationKt.millisToNanos(l2));
        }
        return DurationKt.durationOfMillis(RangesKt.coerceIn(l2, -4611686018427387903L, 0x3FFFFFFFFFFFFFFFL));
    }

    public static final /* synthetic */ long access$parseDuration(String string, boolean bl) {
        return DurationKt.parseDuration(string, bl);
    }

    public static final /* synthetic */ long access$durationOf(long l2, int n2) {
        return DurationKt.durationOf(l2, n2);
    }

    public static final /* synthetic */ long access$durationOfNanosNormalized(long l2) {
        return DurationKt.durationOfNanosNormalized(l2);
    }

    public static final /* synthetic */ long access$durationOfMillisNormalized(long l2) {
        return DurationKt.durationOfMillisNormalized(l2);
    }

    public static final /* synthetic */ long access$nanosToMillis(long l2) {
        return DurationKt.nanosToMillis(l2);
    }

    public static final /* synthetic */ long access$millisToNanos(long l2) {
        return DurationKt.millisToNanos(l2);
    }

    public static final /* synthetic */ long access$durationOfNanos(long l2) {
        return DurationKt.durationOfNanos(l2);
    }

    public static final /* synthetic */ long access$durationOfMillis(long l2) {
        return DurationKt.durationOfMillis(l2);
    }
}


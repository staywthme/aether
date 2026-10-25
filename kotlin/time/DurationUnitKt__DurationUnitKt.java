/*
 * Decompiled with CFR 0.152.
 */
package kotlin.time;

import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.jvm.internal.Intrinsics;
import kotlin.time.DurationUnit;
import kotlin.time.DurationUnitKt__DurationUnitJvmKt;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 8, 0}, k=5, xi=49, d1={"\u0000\u001c\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\f\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\u001a\u0018\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0001\u001a\u0010\u0010\u0006\u001a\u00020\u00012\u0006\u0010\u0007\u001a\u00020\bH\u0001\u001a\f\u0010\u0007\u001a\u00020\b*\u00020\u0001H\u0001\u00a8\u0006\t"}, d2={"durationUnitByIsoChar", "Lkotlin/time/DurationUnit;", "isoChar", "", "isTimeComponent", "", "durationUnitByShortName", "shortName", "", "kotlin-stdlib"}, xs="kotlin/time/DurationUnitKt")
class DurationUnitKt__DurationUnitKt
extends DurationUnitKt__DurationUnitJvmKt {
    @SinceKotlin(version="1.3")
    @NotNull
    public static final String shortName(@NotNull DurationUnit durationUnit) {
        Intrinsics.checkNotNullParameter((Object)durationUnit, "");
        switch (WhenMappings.$EnumSwitchMapping$0[durationUnit.ordinal()]) {
            case 1: {
                return "ns";
            }
            case 2: {
                return "us";
            }
            case 3: {
                return "ms";
            }
            case 4: {
                return "s";
            }
            case 5: {
                return "m";
            }
            case 6: {
                return "h";
            }
            case 7: {
                return "d";
            }
        }
        throw new IllegalStateException(("Unknown unit: " + (Object)((Object)durationUnit)).toString());
    }

    @SinceKotlin(version="1.5")
    @NotNull
    public static final DurationUnit durationUnitByShortName(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, "");
        switch (string) {
            case "ns": {
                return DurationUnit.NANOSECONDS;
            }
            case "us": {
                return DurationUnit.MICROSECONDS;
            }
            case "ms": {
                return DurationUnit.MILLISECONDS;
            }
            case "s": {
                return DurationUnit.SECONDS;
            }
            case "m": {
                return DurationUnit.MINUTES;
            }
            case "h": {
                return DurationUnit.HOURS;
            }
            case "d": {
                return DurationUnit.DAYS;
            }
        }
        throw new IllegalArgumentException("Unknown duration unit short name: " + string);
    }

    @SinceKotlin(version="1.5")
    @NotNull
    public static final DurationUnit durationUnitByIsoChar(char c2, boolean bl) {
        if (!bl) {
            if (c2 == 'D') {
                return DurationUnit.DAYS;
            }
            throw new IllegalArgumentException("Invalid or unsupported duration ISO non-time unit: " + c2);
        }
        if (c2 == 'H') {
            return DurationUnit.HOURS;
        }
        if (c2 == 'M') {
            return DurationUnit.MINUTES;
        }
        if (c2 == 'S') {
            return DurationUnit.SECONDS;
        }
        throw new IllegalArgumentException("Invalid duration ISO time unit: " + c2);
    }

    @Metadata(mv={1, 8, 0}, k=3, xi=48)
    public final class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] nArray = new int[DurationUnit.values().length];
            try {
                nArray[DurationUnit.NANOSECONDS.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                nArray[DurationUnit.MICROSECONDS.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                nArray[DurationUnit.MILLISECONDS.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                nArray[DurationUnit.SECONDS.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                nArray[DurationUnit.MINUTES.ordinal()] = 5;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                nArray[DurationUnit.HOURS.ordinal()] = 6;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                nArray[DurationUnit.DAYS.ordinal()] = 7;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            $EnumSwitchMapping$0 = nArray;
        }
    }
}


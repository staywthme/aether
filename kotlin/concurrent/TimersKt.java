/*
 * Decompiled with CFR 0.152.
 */
package kotlin.concurrent;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import kotlin.Metadata;
import kotlin.PublishedApi;
import kotlin.Unit;
import kotlin.internal.InlineOnly;
import kotlin.jvm.JvmName;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=2, xi=48, d1={"\u00004\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\b\u001aM\u0010\u0000\u001a\u00020\u00012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b\u00a2\u0006\u0002\b\u000eH\u0087\b\u00f8\u0001\u0000\u001aO\u0010\u0000\u001a\u00020\u00012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u000f\u001a\u00020\t2\u0006\u0010\b\u001a\u00020\t2\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b\u00a2\u0006\u0002\b\u000eH\u0087\b\u00f8\u0001\u0000\u001a\u001a\u0010\u0010\u001a\u00020\u00012\b\u0010\u0002\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0001\u001aM\u0010\u0010\u001a\u00020\u00012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b\u00a2\u0006\u0002\b\u000eH\u0087\b\u00f8\u0001\u0000\u001aO\u0010\u0010\u001a\u00020\u00012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u000f\u001a\u00020\t2\u0006\u0010\b\u001a\u00020\t2\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b\u00a2\u0006\u0002\b\u000eH\u0087\b\u00f8\u0001\u0000\u001a'\u0010\u0011\u001a\u00020\f2\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b\u00a2\u0006\u0002\b\u000eH\u0087\b\u00f8\u0001\u0000\u001a3\u0010\u0012\u001a\u00020\f*\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00072\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b\u00a2\u0006\u0002\b\u000eH\u0087\b\u00f8\u0001\u0000\u001a;\u0010\u0012\u001a\u00020\f*\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b\u00a2\u0006\u0002\b\u000eH\u0087\b\u00f8\u0001\u0000\u001a3\u0010\u0012\u001a\u00020\f*\u00020\u00012\u0006\u0010\u0014\u001a\u00020\t2\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b\u00a2\u0006\u0002\b\u000eH\u0087\b\u00f8\u0001\u0000\u001a;\u0010\u0012\u001a\u00020\f*\u00020\u00012\u0006\u0010\u0014\u001a\u00020\t2\u0006\u0010\b\u001a\u00020\t2\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b\u00a2\u0006\u0002\b\u000eH\u0087\b\u00f8\u0001\u0000\u001a;\u0010\u0015\u001a\u00020\f*\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b\u00a2\u0006\u0002\b\u000eH\u0087\b\u00f8\u0001\u0000\u001a;\u0010\u0015\u001a\u00020\f*\u00020\u00012\u0006\u0010\u0014\u001a\u00020\t2\u0006\u0010\b\u001a\u00020\t2\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b\u00a2\u0006\u0002\b\u000eH\u0087\b\u00f8\u0001\u0000\u0082\u0002\u0007\n\u0005\b\u009920\u0001\u00a8\u0006\u0016"}, d2={"fixedRateTimer", "Ljava/util/Timer;", "name", "", "daemon", "", "startAt", "Ljava/util/Date;", "period", "", "action", "Lkotlin/Function1;", "Ljava/util/TimerTask;", "", "Lkotlin/ExtensionFunctionType;", "initialDelay", "timer", "timerTask", "schedule", "time", "delay", "scheduleAtFixedRate", "kotlin-stdlib"})
@JvmName(name="TimersKt")
public final class TimersKt {
    @InlineOnly
    private static final TimerTask schedule(Timer timer, long l2, Function1<? super TimerTask, Unit> object) {
        Intrinsics.checkNotNullParameter(timer, "");
        Intrinsics.checkNotNullParameter(object, "");
        object = new TimerTask((Function1<? super TimerTask, Unit>)object){
            final /* synthetic */ Function1<TimerTask, Unit> $action;
            {
                this.$action = function1;
            }

            public final void run() {
                this.$action.invoke(this);
            }
        };
        timer.schedule((TimerTask)object, l2);
        return object;
    }

    @InlineOnly
    private static final TimerTask schedule(Timer timer, Date date, Function1<? super TimerTask, Unit> object) {
        Intrinsics.checkNotNullParameter(timer, "");
        Intrinsics.checkNotNullParameter(date, "");
        Intrinsics.checkNotNullParameter(object, "");
        object = new /* invalid duplicate definition of identical inner class */;
        timer.schedule((TimerTask)object, date);
        return object;
    }

    @InlineOnly
    private static final TimerTask schedule(Timer timer, long l2, long l3, Function1<? super TimerTask, Unit> object) {
        Intrinsics.checkNotNullParameter(timer, "");
        Intrinsics.checkNotNullParameter(object, "");
        object = new /* invalid duplicate definition of identical inner class */;
        timer.schedule((TimerTask)object, l2, l3);
        return object;
    }

    @InlineOnly
    private static final TimerTask schedule(Timer timer, Date date, long l2, Function1<? super TimerTask, Unit> object) {
        Intrinsics.checkNotNullParameter(timer, "");
        Intrinsics.checkNotNullParameter(date, "");
        Intrinsics.checkNotNullParameter(object, "");
        object = new /* invalid duplicate definition of identical inner class */;
        timer.schedule((TimerTask)object, date, l2);
        return object;
    }

    @InlineOnly
    private static final TimerTask scheduleAtFixedRate(Timer timer, long l2, long l3, Function1<? super TimerTask, Unit> object) {
        Intrinsics.checkNotNullParameter(timer, "");
        Intrinsics.checkNotNullParameter(object, "");
        object = new /* invalid duplicate definition of identical inner class */;
        timer.scheduleAtFixedRate((TimerTask)object, l2, l3);
        return object;
    }

    @InlineOnly
    private static final TimerTask scheduleAtFixedRate(Timer timer, Date date, long l2, Function1<? super TimerTask, Unit> object) {
        Intrinsics.checkNotNullParameter(timer, "");
        Intrinsics.checkNotNullParameter(date, "");
        Intrinsics.checkNotNullParameter(object, "");
        object = new /* invalid duplicate definition of identical inner class */;
        timer.scheduleAtFixedRate((TimerTask)object, date, l2);
        return object;
    }

    @PublishedApi
    @NotNull
    public static final Timer timer(@Nullable String string, boolean bl) {
        if (string == null) {
            return new Timer(bl);
        }
        return new Timer(string, bl);
    }

    @InlineOnly
    private static final Timer timer(String object, boolean bl, long l2, long l3, Function1<? super TimerTask, Unit> object2) {
        Intrinsics.checkNotNullParameter(object2, "");
        object = TimersKt.timer((String)object, bl);
        Timer timer = object;
        object2 = new /* invalid duplicate definition of identical inner class */;
        timer.schedule((TimerTask)object2, l2, l3);
        return object;
    }

    static /* synthetic */ Timer timer$default(String object, boolean bl, long l2, long l3, Function1 function1, int n2, Object object2) {
        if ((n2 & 1) != 0) {
            object = null;
        }
        if ((n2 & 2) != 0) {
            bl = false;
        }
        if ((n2 & 4) != 0) {
            l2 = 0L;
        }
        Intrinsics.checkNotNullParameter(function1, "");
        Timer timer = TimersKt.timer((String)object, bl);
        object2 = timer;
        object = new /* invalid duplicate definition of identical inner class */;
        ((Timer)object2).schedule((TimerTask)object, l2, l3);
        return timer;
    }

    @InlineOnly
    private static final Timer timer(String object, boolean bl, Date date, long l2, Function1<? super TimerTask, Unit> object2) {
        Intrinsics.checkNotNullParameter(date, "");
        Intrinsics.checkNotNullParameter(object2, "");
        object = TimersKt.timer((String)object, bl);
        Timer timer = object;
        object2 = new /* invalid duplicate definition of identical inner class */;
        timer.schedule((TimerTask)object2, date, l2);
        return object;
    }

    static /* synthetic */ Timer timer$default(String object, boolean bl, Date date, long l2, Function1 function1, int n2, Object object2) {
        if ((n2 & 1) != 0) {
            object = null;
        }
        if ((n2 & 2) != 0) {
            bl = false;
        }
        Intrinsics.checkNotNullParameter(date, "");
        Intrinsics.checkNotNullParameter(function1, "");
        Timer timer = TimersKt.timer((String)object, bl);
        object2 = timer;
        object = new /* invalid duplicate definition of identical inner class */;
        ((Timer)object2).schedule((TimerTask)object, date, l2);
        return timer;
    }

    @InlineOnly
    private static final Timer fixedRateTimer(String object, boolean bl, long l2, long l3, Function1<? super TimerTask, Unit> object2) {
        Intrinsics.checkNotNullParameter(object2, "");
        object = TimersKt.timer((String)object, bl);
        Timer timer = object;
        object2 = new /* invalid duplicate definition of identical inner class */;
        timer.scheduleAtFixedRate((TimerTask)object2, l2, l3);
        return object;
    }

    static /* synthetic */ Timer fixedRateTimer$default(String object, boolean bl, long l2, long l3, Function1 function1, int n2, Object object2) {
        if ((n2 & 1) != 0) {
            object = null;
        }
        if ((n2 & 2) != 0) {
            bl = false;
        }
        if ((n2 & 4) != 0) {
            l2 = 0L;
        }
        Intrinsics.checkNotNullParameter(function1, "");
        Timer timer = TimersKt.timer((String)object, bl);
        object2 = timer;
        object = new /* invalid duplicate definition of identical inner class */;
        ((Timer)object2).scheduleAtFixedRate((TimerTask)object, l2, l3);
        return timer;
    }

    @InlineOnly
    private static final Timer fixedRateTimer(String object, boolean bl, Date date, long l2, Function1<? super TimerTask, Unit> object2) {
        Intrinsics.checkNotNullParameter(date, "");
        Intrinsics.checkNotNullParameter(object2, "");
        object = TimersKt.timer((String)object, bl);
        Timer timer = object;
        object2 = new /* invalid duplicate definition of identical inner class */;
        timer.scheduleAtFixedRate((TimerTask)object2, date, l2);
        return object;
    }

    static /* synthetic */ Timer fixedRateTimer$default(String object, boolean bl, Date date, long l2, Function1 function1, int n2, Object object2) {
        if ((n2 & 1) != 0) {
            object = null;
        }
        if ((n2 & 2) != 0) {
            bl = false;
        }
        Intrinsics.checkNotNullParameter(date, "");
        Intrinsics.checkNotNullParameter(function1, "");
        Timer timer = TimersKt.timer((String)object, bl);
        object2 = timer;
        object = new /* invalid duplicate definition of identical inner class */;
        ((Timer)object2).scheduleAtFixedRate((TimerTask)object, date, l2);
        return timer;
    }

    @InlineOnly
    private static final TimerTask timerTask(Function1<? super TimerTask, Unit> function1) {
        Intrinsics.checkNotNullParameter(function1, "");
        return new /* invalid duplicate definition of identical inner class */;
    }
}


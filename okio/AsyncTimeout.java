/*
 * Decompiled with CFR 0.152.
 */
package okio;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import kotlin.Metadata;
import kotlin.PublishedApi;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import okio.-SegmentedByteString;
import okio.Buffer;
import okio.Segment;
import okio.Sink;
import okio.Source;
import okio.Timeout;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0016\u0018\u0000 \u001b2\u00020\u0001:\u0002\u001b\u001cB\u0005\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\b\u001a\u00020\t2\b\u0010\n\u001a\u0004\u0018\u00010\tH\u0001J\u0006\u0010\u000b\u001a\u00020\fJ\u0006\u0010\r\u001a\u00020\u0004J\u0012\u0010\u000e\u001a\u00020\t2\b\u0010\n\u001a\u0004\u0018\u00010\tH\u0014J\u0010\u0010\u000f\u001a\u00020\u00072\u0006\u0010\u0010\u001a\u00020\u0007H\u0002J\u000e\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0011\u001a\u00020\u0012J\u000e\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0013\u001a\u00020\u0014J\b\u0010\u0015\u001a\u00020\fH\u0014J%\u0010\u0016\u001a\u0002H\u0017\"\u0004\b\u0000\u0010\u00172\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u0002H\u00170\u0019H\u0086\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001aR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0000X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0007\n\u0005\b\u009920\u0001\u00a8\u0006\u001d"}, d2={"Lokio/AsyncTimeout;", "Lokio/Timeout;", "()V", "inQueue", "", "next", "timeoutAt", "", "access$newTimeoutException", "Ljava/io/IOException;", "cause", "enter", "", "exit", "newTimeoutException", "remainingNanos", "now", "sink", "Lokio/Sink;", "source", "Lokio/Source;", "timedOut", "withTimeout", "T", "block", "Lkotlin/Function0;", "(Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "Companion", "Watchdog", "okio"})
public class AsyncTimeout
extends Timeout {
    @NotNull
    public static final Companion Companion = new Companion(null);
    private boolean inQueue;
    @Nullable
    private AsyncTimeout next;
    private long timeoutAt;
    @NotNull
    private static final ReentrantLock lock = new ReentrantLock();
    @NotNull
    private static final Condition condition;
    private static final int TIMEOUT_WRITE_SIZE = 65536;
    private static final long IDLE_TIMEOUT_MILLIS;
    private static final long IDLE_TIMEOUT_NANOS;
    @Nullable
    private static AsyncTimeout head;

    public final void enter() {
        long l2 = this.timeoutNanos();
        boolean bl = this.hasDeadline();
        if (l2 == 0L && !bl) {
            return;
        }
        AsyncTimeout.Companion.scheduleTimeout(this, l2, bl);
    }

    public final boolean exit() {
        return AsyncTimeout.Companion.cancelScheduledTimeout(this);
    }

    private final long remainingNanos(long l2) {
        return this.timeoutAt - l2;
    }

    protected void timedOut() {
    }

    @NotNull
    public final Sink sink(@NotNull Sink sink2) {
        Intrinsics.checkNotNullParameter(sink2, "");
        return new Sink(this, sink2){
            final /* synthetic */ AsyncTimeout this$0;
            final /* synthetic */ Sink $sink;
            {
                this.this$0 = asyncTimeout;
                this.$sink = sink2;
            }

            public final void write(@NotNull Buffer buffer, long l2) {
                long l3;
                Intrinsics.checkNotNullParameter(buffer, "");
                -SegmentedByteString.checkOffsetAndCount(buffer.size(), 0L, l2);
                for (long i2 = l2; i2 > 0L; i2 -= l3) {
                    l3 = 0L;
                    Segment segment = buffer.head;
                    Intrinsics.checkNotNull(segment);
                    Object object = segment;
                    while (l3 < 65536L) {
                        int n2 = ((Segment)object).limit - ((Segment)object).pos;
                        if ((l3 += (long)n2) >= i2) {
                            l3 = i2;
                            break;
                        }
                        Intrinsics.checkNotNull(((Segment)object).next);
                    }
                    AsyncTimeout asyncTimeout = this.this$0;
                    object = this.$sink;
                    boolean bl = false;
                    asyncTimeout.enter();
                    try {
                        object.write(buffer, l3);
                        bl = true;
                        boolean bl2 = asyncTimeout.exit();
                        if (!bl2) continue;
                    }
                    catch (IOException iOException) {
                        try {
                            throw !asyncTimeout.exit() ? (Throwable)iOException : (Throwable)asyncTimeout.access$newTimeoutException(iOException);
                        }
                        catch (Throwable throwable) {
                            boolean bl3 = asyncTimeout.exit();
                            if (bl3 && bl) {
                                throw asyncTimeout.access$newTimeoutException(null);
                            }
                            throw throwable;
                        }
                    }
                    throw asyncTimeout.access$newTimeoutException(null);
                }
            }

            public final void flush() {
                block5: {
                    AsyncTimeout asyncTimeout = this.this$0;
                    Sink sink2 = this.$sink;
                    boolean bl = false;
                    asyncTimeout.enter();
                    try {
                        sink2.flush();
                        bl = true;
                        boolean bl2 = asyncTimeout.exit();
                        if (!bl2) break block5;
                    }
                    catch (IOException iOException) {
                        try {
                            throw !asyncTimeout.exit() ? (Throwable)iOException : (Throwable)asyncTimeout.access$newTimeoutException(iOException);
                        }
                        catch (Throwable throwable) {
                            boolean bl3 = asyncTimeout.exit();
                            if (bl3 && bl) {
                                throw asyncTimeout.access$newTimeoutException(null);
                            }
                            throw throwable;
                        }
                    }
                    throw asyncTimeout.access$newTimeoutException(null);
                }
                return;
            }

            public final void close() {
                block5: {
                    AsyncTimeout asyncTimeout = this.this$0;
                    Sink sink2 = this.$sink;
                    boolean bl = false;
                    asyncTimeout.enter();
                    try {
                        sink2.close();
                        bl = true;
                        boolean bl2 = asyncTimeout.exit();
                        if (!bl2) break block5;
                    }
                    catch (IOException iOException) {
                        try {
                            throw !asyncTimeout.exit() ? (Throwable)iOException : (Throwable)asyncTimeout.access$newTimeoutException(iOException);
                        }
                        catch (Throwable throwable) {
                            boolean bl3 = asyncTimeout.exit();
                            if (bl3 && bl) {
                                throw asyncTimeout.access$newTimeoutException(null);
                            }
                            throw throwable;
                        }
                    }
                    throw asyncTimeout.access$newTimeoutException(null);
                }
                return;
            }

            @NotNull
            public final AsyncTimeout timeout() {
                return this.this$0;
            }

            @NotNull
            public final String toString() {
                return "AsyncTimeout.sink(" + this.$sink + ')';
            }
        };
    }

    @NotNull
    public final Source source(@NotNull Source source2) {
        Intrinsics.checkNotNullParameter(source2, "");
        return new Source(this, source2){
            final /* synthetic */ AsyncTimeout this$0;
            final /* synthetic */ Source $source;
            {
                this.this$0 = asyncTimeout;
                this.$source = source2;
            }

            public final long read(@NotNull Buffer buffer, long l2) {
                long l3;
                block5: {
                    Intrinsics.checkNotNullParameter(buffer, "");
                    AsyncTimeout asyncTimeout = this.this$0;
                    Source source2 = this.$source;
                    boolean bl = false;
                    asyncTimeout.enter();
                    try {
                        l3 = source2.read(buffer, l2);
                        bl = true;
                        boolean bl2 = asyncTimeout.exit();
                        if (!bl2) break block5;
                    }
                    catch (IOException iOException) {
                        try {
                            throw !asyncTimeout.exit() ? (Throwable)iOException : (Throwable)asyncTimeout.access$newTimeoutException(iOException);
                        }
                        catch (Throwable throwable) {
                            boolean bl3 = asyncTimeout.exit();
                            if (bl3 && bl) {
                                throw asyncTimeout.access$newTimeoutException(null);
                            }
                            throw throwable;
                        }
                    }
                    throw asyncTimeout.access$newTimeoutException(null);
                }
                return l3;
            }

            public final void close() {
                block5: {
                    AsyncTimeout asyncTimeout = this.this$0;
                    Source source2 = this.$source;
                    boolean bl = false;
                    asyncTimeout.enter();
                    try {
                        source2.close();
                        bl = true;
                        boolean bl2 = asyncTimeout.exit();
                        if (!bl2) break block5;
                    }
                    catch (IOException iOException) {
                        try {
                            throw !asyncTimeout.exit() ? (Throwable)iOException : (Throwable)asyncTimeout.access$newTimeoutException(iOException);
                        }
                        catch (Throwable throwable) {
                            boolean bl3 = asyncTimeout.exit();
                            if (bl3 && bl) {
                                throw asyncTimeout.access$newTimeoutException(null);
                            }
                            throw throwable;
                        }
                    }
                    throw asyncTimeout.access$newTimeoutException(null);
                }
                return;
            }

            @NotNull
            public final AsyncTimeout timeout() {
                return this.this$0;
            }

            @NotNull
            public final String toString() {
                return "AsyncTimeout.source(" + this.$source + ')';
            }
        };
    }

    public final <T> T withTimeout(@NotNull Function0<? extends T> function0) {
        Function0<T> function02;
        Intrinsics.checkNotNullParameter(function0, "");
        boolean bl = false;
        this.enter();
        try {
            function0 = function0.invoke();
            bl = true;
            function02 = function0;
        }
        catch (IOException iOException) {
            try {
                throw !this.exit() ? (Throwable)iOException : (Throwable)this.access$newTimeoutException(iOException);
            }
            catch (Throwable throwable) {
                InlineMarker.finallyStart(1);
                boolean bl2 = this.exit();
                if (bl2 && bl) {
                    throw this.access$newTimeoutException(null);
                }
                InlineMarker.finallyEnd(1);
                throw throwable;
            }
        }
        InlineMarker.finallyStart(1);
        boolean bl3 = this.exit();
        if (bl3) {
            throw this.access$newTimeoutException(null);
        }
        InlineMarker.finallyEnd(1);
        return (T)function02;
    }

    @PublishedApi
    @NotNull
    public final IOException access$newTimeoutException(@Nullable IOException iOException) {
        return this.newTimeoutException(iOException);
    }

    @NotNull
    protected IOException newTimeoutException(@Nullable IOException iOException) {
        InterruptedIOException interruptedIOException = new InterruptedIOException("timeout");
        if (iOException != null) {
            interruptedIOException.initCause(iOException);
        }
        return interruptedIOException;
    }

    static {
        Condition condition = lock.newCondition();
        Intrinsics.checkNotNullExpressionValue(condition, "");
        AsyncTimeout.condition = condition;
        IDLE_TIMEOUT_MILLIS = TimeUnit.SECONDS.toMillis(60L);
        IDLE_TIMEOUT_NANOS = TimeUnit.MILLISECONDS.toNanos(IDLE_TIMEOUT_MILLIS);
    }

    @Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000f\u0010\u0012\u001a\u0004\u0018\u00010\rH\u0000\u00a2\u0006\u0002\b\u0013J\u0010\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\rH\u0002J \u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0016\u001a\u00020\r2\u0006\u0010\u0019\u001a\u00020\u00042\u0006\u0010\u001a\u001a\u00020\u0015H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082T\u00a2\u0006\u0002\n\u0000R\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u000e\u001a\u00020\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011\u00a8\u0006\u001b"}, d2={"Lokio/AsyncTimeout$Companion;", "", "()V", "IDLE_TIMEOUT_MILLIS", "", "IDLE_TIMEOUT_NANOS", "TIMEOUT_WRITE_SIZE", "", "condition", "Ljava/util/concurrent/locks/Condition;", "getCondition", "()Ljava/util/concurrent/locks/Condition;", "head", "Lokio/AsyncTimeout;", "lock", "Ljava/util/concurrent/locks/ReentrantLock;", "getLock", "()Ljava/util/concurrent/locks/ReentrantLock;", "awaitTimeout", "awaitTimeout$okio", "cancelScheduledTimeout", "", "node", "scheduleTimeout", "", "timeoutNanos", "hasDeadline", "okio"})
    @SourceDebugExtension(value={"SMAP\nAsyncTimeout.kt\nKotlin\n*S Kotlin\n*F\n+ 1 AsyncTimeout.kt\nokio/AsyncTimeout$Companion\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,331:1\n1#2:332\n*E\n"})
    public static final class Companion {
        private Companion() {
        }

        @NotNull
        public final ReentrantLock getLock() {
            return lock;
        }

        @NotNull
        public final Condition getCondition() {
            return condition;
        }

        private final void scheduleTimeout(AsyncTimeout object, long l2, boolean bl) {
            Lock lock = Companion.getLock();
            lock.lock();
            try {
                if (!(!((AsyncTimeout)object).inQueue)) {
                    object = "Unbalanced enter/exit";
                    throw new IllegalStateException(object.toString());
                }
                ((AsyncTimeout)object).inQueue = true;
                if (head == null) {
                    head = new AsyncTimeout();
                    new Watchdog().start();
                }
                long l3 = System.nanoTime();
                if (l2 != 0L && bl) {
                    ((AsyncTimeout)object).timeoutAt = l3 + Math.min(l2, ((Timeout)object).deadlineNanoTime() - l3);
                } else if (l2 != 0L) {
                    ((AsyncTimeout)object).timeoutAt = l3 + l2;
                } else if (bl) {
                    Object object2 = object;
                    ((AsyncTimeout)object2).timeoutAt = ((Timeout)object2).deadlineNanoTime();
                } else {
                    throw new AssertionError();
                }
                long l4 = ((AsyncTimeout)object).remainingNanos(l3);
                AsyncTimeout asyncTimeout = head;
                Intrinsics.checkNotNull(asyncTimeout);
                AsyncTimeout asyncTimeout2 = asyncTimeout;
                while (true) {
                    block16: {
                        block15: {
                            if (asyncTimeout2.next == null) break block15;
                            AsyncTimeout asyncTimeout3 = asyncTimeout2.next;
                            Intrinsics.checkNotNull(asyncTimeout3);
                            if (l4 >= asyncTimeout3.remainingNanos(l3)) break block16;
                        }
                        ((AsyncTimeout)object).next = asyncTimeout2.next;
                        asyncTimeout2.next = (AsyncTimeout)object;
                        if (asyncTimeout2 != head) break;
                        Companion.getCondition().signal();
                        break;
                    }
                    Intrinsics.checkNotNull(asyncTimeout2.next);
                }
                return;
            }
            finally {
                lock.unlock();
            }
        }

        private final boolean cancelScheduledTimeout(AsyncTimeout asyncTimeout) {
            Lock lock = Companion.getLock();
            lock.lock();
            try {
                if (!asyncTimeout.inQueue) {
                    return false;
                }
                asyncTimeout.inQueue = false;
                AsyncTimeout asyncTimeout2 = head;
                while (asyncTimeout2 != null) {
                    if (asyncTimeout2.next == asyncTimeout) {
                        asyncTimeout2.next = asyncTimeout.next;
                        asyncTimeout.next = null;
                        return false;
                    }
                    asyncTimeout2 = asyncTimeout2.next;
                }
                return true;
            }
            finally {
                lock.unlock();
            }
        }

        @Nullable
        public final AsyncTimeout awaitTimeout$okio() throws InterruptedException {
            AsyncTimeout asyncTimeout = head;
            Intrinsics.checkNotNull(asyncTimeout);
            AsyncTimeout asyncTimeout2 = asyncTimeout.next;
            if (asyncTimeout2 == null) {
                long l2 = System.nanoTime();
                this.getCondition().await(IDLE_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
                AsyncTimeout asyncTimeout3 = head;
                Intrinsics.checkNotNull(asyncTimeout3);
                if (asyncTimeout3.next == null && System.nanoTime() - l2 >= IDLE_TIMEOUT_NANOS) {
                    return head;
                }
                return null;
            }
            long l3 = asyncTimeout2.remainingNanos(System.nanoTime());
            if (l3 > 0L) {
                this.getCondition().await(l3, TimeUnit.NANOSECONDS);
                return null;
            }
            AsyncTimeout asyncTimeout4 = head;
            Intrinsics.checkNotNull(asyncTimeout4);
            asyncTimeout4.next = asyncTimeout2.next;
            asyncTimeout2.next = null;
            return asyncTimeout2;
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    @Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0000\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0016\u00a8\u0006\u0005"}, d2={"Lokio/AsyncTimeout$Watchdog;", "Ljava/lang/Thread;", "()V", "run", "", "okio"})
    static final class Watchdog
    extends Thread {
        public Watchdog() {
            super("Okio Watchdog");
            this.setDaemon(true);
        }

        @Override
        public final void run() {
            while (true) {
                try {
                    while (true) {
                        AsyncTimeout asyncTimeout;
                        Lock lock = Companion.getLock();
                        lock.lock();
                        try {
                            asyncTimeout = Companion.awaitTimeout$okio();
                            if (asyncTimeout == head) {
                                head = null;
                                return;
                            }
                        }
                        finally {
                            lock.unlock();
                        }
                        AsyncTimeout asyncTimeout2 = asyncTimeout;
                        if (asyncTimeout2 == null) continue;
                        asyncTimeout2.timedOut();
                    }
                }
                catch (InterruptedException interruptedException) {
                    continue;
                }
                break;
            }
        }
    }
}


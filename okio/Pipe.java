/*
 * Decompiled with CFR 0.152.
 */
package okio;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.Unit;
import kotlin.jvm.JvmName;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import okio.Buffer;
import okio.Sink;
import okio.Source;
import okio.Timeout;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000T\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010)\u001a\u00020*J\u000e\u0010+\u001a\u00020*2\u0006\u0010\u001f\u001a\u00020\u0014J\r\u0010\u001f\u001a\u00020\u0014H\u0007\u00a2\u0006\u0002\b,J\r\u0010#\u001a\u00020$H\u0007\u00a2\u0006\u0002\b-J&\u0010.\u001a\u00020**\u00020\u00142\u0017\u0010/\u001a\u0013\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020*00\u00a2\u0006\u0002\b1H\u0082\bR\u0014\u0010\u0005\u001a\u00020\u0006X\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\nX\u0080\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u0011\u0010\u000f\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u001c\u0010\u0013\u001a\u0004\u0018\u00010\u0014X\u0080\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\u0018R\u0011\u0010\u0019\u001a\u00020\u001a\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u0014\u0010\u0002\u001a\u00020\u0003X\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001eR\u0013\u0010\u001f\u001a\u00020\u00148G\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u0016R\u001a\u0010 \u001a\u00020\nX\u0080\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b!\u0010\f\"\u0004\b\"\u0010\u000eR\u0013\u0010#\u001a\u00020$8G\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010%R\u001a\u0010&\u001a\u00020\nX\u0080\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b'\u0010\f\"\u0004\b(\u0010\u000e\u00a8\u00062"}, d2={"Lokio/Pipe;", "", "maxBufferSize", "", "(J)V", "buffer", "Lokio/Buffer;", "getBuffer$okio", "()Lokio/Buffer;", "canceled", "", "getCanceled$okio", "()Z", "setCanceled$okio", "(Z)V", "condition", "Ljava/util/concurrent/locks/Condition;", "getCondition", "()Ljava/util/concurrent/locks/Condition;", "foldedSink", "Lokio/Sink;", "getFoldedSink$okio", "()Lokio/Sink;", "setFoldedSink$okio", "(Lokio/Sink;)V", "lock", "Ljava/util/concurrent/locks/ReentrantLock;", "getLock", "()Ljava/util/concurrent/locks/ReentrantLock;", "getMaxBufferSize$okio", "()J", "sink", "sinkClosed", "getSinkClosed$okio", "setSinkClosed$okio", "source", "Lokio/Source;", "()Lokio/Source;", "sourceClosed", "getSourceClosed$okio", "setSourceClosed$okio", "cancel", "", "fold", "-deprecated_sink", "-deprecated_source", "forward", "block", "Lkotlin/Function1;", "Lkotlin/ExtensionFunctionType;", "okio"})
@SourceDebugExtension(value={"SMAP\nPipe.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Pipe.kt\nokio/Pipe\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 Timeout.kt\nokio/Timeout\n*L\n1#1,257:1\n1#2:258\n268#3,26:259\n*S KotlinDebug\n*F\n+ 1 Pipe.kt\nokio/Pipe\n*L\n217#1:259,26\n*E\n"})
public final class Pipe {
    private final long maxBufferSize;
    @NotNull
    private final Buffer buffer;
    private boolean canceled;
    private boolean sinkClosed;
    private boolean sourceClosed;
    @Nullable
    private Sink foldedSink;
    @NotNull
    private final ReentrantLock lock;
    @NotNull
    private final Condition condition;
    @NotNull
    private final Sink sink;
    @NotNull
    private final Source source;

    public Pipe(long l2) {
        this.maxBufferSize = l2;
        this.buffer = new Buffer();
        this.lock = new ReentrantLock();
        Condition condition = this.lock.newCondition();
        Intrinsics.checkNotNullExpressionValue(condition, "");
        this.condition = condition;
        if (!(this.maxBufferSize >= 1L)) {
            String string = "maxBufferSize < 1: " + this.maxBufferSize;
            throw new IllegalArgumentException(string.toString());
        }
        this.sink = new Sink(this){
            @NotNull
            private final Timeout timeout;
            final /* synthetic */ Pipe this$0;
            {
                this.this$0 = pipe;
                this.timeout = new Timeout();
            }

            public final void write(@NotNull Buffer object, long l2) {
                Object object2;
                Intrinsics.checkNotNullParameter(object, "");
                long l3 = l2;
                Object object3 = null;
                Object object4 = this.this$0.getLock();
                Pipe pipe = this.this$0;
                object4.lock();
                try {
                    if (!(!pipe.getSinkClosed$okio())) {
                        object = "closed";
                        throw new IllegalStateException(object.toString());
                    }
                    if (pipe.getCanceled$okio()) {
                        throw new IOException("canceled");
                    }
                    while (l3 > 0L) {
                        Sink sink2 = pipe.getFoldedSink$okio();
                        if (sink2 != null) {
                            object2 = sink2;
                            object3 = sink2;
                            break;
                        }
                        if (pipe.getSourceClosed$okio()) {
                            throw new IOException("source is closed");
                        }
                        long l4 = pipe.getMaxBufferSize$okio() - pipe.getBuffer$okio().size();
                        if (l4 == 0L) {
                            this.timeout.awaitSignal(pipe.getCondition());
                            if (!pipe.getCanceled$okio()) continue;
                            throw new IOException("canceled");
                        }
                        long l5 = Math.min(l4, l3);
                        pipe.getBuffer$okio().write((Buffer)object, l5);
                        l3 -= l5;
                        pipe.getCondition().signalAll();
                    }
                }
                finally {
                    object4.unlock();
                }
                object4 = object3;
                if (object4 != null) {
                    pipe = this.this$0;
                    object3 = object4;
                    object2 = object3.timeout();
                    object4 = pipe.sink().timeout();
                    long l6 = ((Timeout)object2).timeoutNanos();
                    ((Timeout)object2).timeout(Timeout.Companion.minTimeout(((Timeout)object4).timeoutNanos(), ((Timeout)object2).timeoutNanos()), TimeUnit.NANOSECONDS);
                    if (((Timeout)object2).hasDeadline()) {
                        long l7 = ((Timeout)object2).deadlineNanoTime();
                        if (((Timeout)object4).hasDeadline()) {
                            Object object5 = object2;
                            ((Timeout)object5).deadlineNanoTime(Math.min(((Timeout)object5).deadlineNanoTime(), ((Timeout)object4).deadlineNanoTime()));
                        }
                        try {
                            object3.write((Buffer)object, l3);
                            return;
                        }
                        finally {
                            ((Timeout)object2).timeout(l6, TimeUnit.NANOSECONDS);
                            if (((Timeout)object4).hasDeadline()) {
                                ((Timeout)object2).deadlineNanoTime(l7);
                            }
                        }
                    }
                    if (((Timeout)object4).hasDeadline()) {
                        ((Timeout)object2).deadlineNanoTime(((Timeout)object4).deadlineNanoTime());
                    }
                    try {
                        object3.write((Buffer)object, l3);
                        return;
                    }
                    finally {
                        ((Timeout)object2).timeout(l6, TimeUnit.NANOSECONDS);
                        if (((Timeout)object4).hasDeadline()) {
                            ((Timeout)object2).clearDeadline();
                        }
                    }
                }
            }

            public final void flush() {
                Object object;
                Object object2 = null;
                Object object3 = this.this$0.getLock();
                Pipe pipe = this.this$0;
                object3.lock();
                try {
                    if (!(!pipe.getSinkClosed$okio())) {
                        object2 = "closed";
                        throw new IllegalStateException(object2.toString());
                    }
                    if (pipe.getCanceled$okio()) {
                        throw new IOException("canceled");
                    }
                    Sink sink2 = pipe.getFoldedSink$okio();
                    if (sink2 != null) {
                        object = sink2;
                        object2 = sink2;
                    } else if (pipe.getSourceClosed$okio() && pipe.getBuffer$okio().size() > 0L) {
                        throw new IOException("source is closed");
                    }
                }
                finally {
                    object3.unlock();
                }
                object3 = object2;
                if (object3 != null) {
                    pipe = this.this$0;
                    object2 = object3;
                    object = object2.timeout();
                    object3 = pipe.sink().timeout();
                    long l2 = ((Timeout)object).timeoutNanos();
                    ((Timeout)object).timeout(Timeout.Companion.minTimeout(((Timeout)object3).timeoutNanos(), ((Timeout)object).timeoutNanos()), TimeUnit.NANOSECONDS);
                    if (((Timeout)object).hasDeadline()) {
                        long l3 = ((Timeout)object).deadlineNanoTime();
                        if (((Timeout)object3).hasDeadline()) {
                            Object object4 = object;
                            ((Timeout)object4).deadlineNanoTime(Math.min(((Timeout)object4).deadlineNanoTime(), ((Timeout)object3).deadlineNanoTime()));
                        }
                        try {
                            object2.flush();
                            return;
                        }
                        finally {
                            ((Timeout)object).timeout(l2, TimeUnit.NANOSECONDS);
                            if (((Timeout)object3).hasDeadline()) {
                                ((Timeout)object).deadlineNanoTime(l3);
                            }
                        }
                    }
                    if (((Timeout)object3).hasDeadline()) {
                        ((Timeout)object).deadlineNanoTime(((Timeout)object3).deadlineNanoTime());
                    }
                    try {
                        object2.flush();
                        return;
                    }
                    finally {
                        ((Timeout)object).timeout(l2, TimeUnit.NANOSECONDS);
                        if (((Timeout)object3).hasDeadline()) {
                            ((Timeout)object).clearDeadline();
                        }
                    }
                }
            }

            public final void close() {
                Object object = null;
                Object object2 = this.this$0.getLock();
                Object object3 = this.this$0;
                object2.lock();
                try {
                    if (((Pipe)object3).getSinkClosed$okio()) {
                        return;
                    }
                    Sink sink2 = ((Pipe)object3).getFoldedSink$okio();
                    if (sink2 != null) {
                        object = sink2;
                        object = sink2;
                    } else {
                        if (((Pipe)object3).getSourceClosed$okio() && ((Pipe)object3).getBuffer$okio().size() > 0L) {
                            throw new IOException("source is closed");
                        }
                        ((Pipe)object3).setSinkClosed$okio(true);
                        ((Pipe)object3).getCondition().signalAll();
                    }
                }
                finally {
                    object2.unlock();
                }
                object2 = object;
                if (object2 != null) {
                    object3 = this.this$0;
                    object = object2;
                    object2 = object.timeout();
                    object3 = ((Pipe)object3).sink().timeout();
                    long l2 = ((Timeout)object2).timeoutNanos();
                    ((Timeout)object2).timeout(Timeout.Companion.minTimeout(((Timeout)object3).timeoutNanos(), ((Timeout)object2).timeoutNanos()), TimeUnit.NANOSECONDS);
                    if (((Timeout)object2).hasDeadline()) {
                        long l3 = ((Timeout)object2).deadlineNanoTime();
                        if (((Timeout)object3).hasDeadline()) {
                            Object object4 = object2;
                            ((Timeout)object4).deadlineNanoTime(Math.min(((Timeout)object4).deadlineNanoTime(), ((Timeout)object3).deadlineNanoTime()));
                        }
                        try {
                            object.close();
                            return;
                        }
                        finally {
                            ((Timeout)object2).timeout(l2, TimeUnit.NANOSECONDS);
                            if (((Timeout)object3).hasDeadline()) {
                                ((Timeout)object2).deadlineNanoTime(l3);
                            }
                        }
                    }
                    if (((Timeout)object3).hasDeadline()) {
                        ((Timeout)object2).deadlineNanoTime(((Timeout)object3).deadlineNanoTime());
                    }
                    try {
                        object.close();
                        return;
                    }
                    finally {
                        ((Timeout)object2).timeout(l2, TimeUnit.NANOSECONDS);
                        if (((Timeout)object3).hasDeadline()) {
                            ((Timeout)object2).clearDeadline();
                        }
                    }
                }
            }

            @NotNull
            public final Timeout timeout() {
                return this.timeout;
            }
        };
        this.source = new Source(this){
            @NotNull
            private final Timeout timeout;
            final /* synthetic */ Pipe this$0;
            {
                this.this$0 = pipe;
                this.timeout = new Timeout();
            }

            public final long read(@NotNull Buffer object, long l2) {
                Intrinsics.checkNotNullParameter(object, "");
                Lock lock = this.this$0.getLock();
                Pipe pipe = this.this$0;
                lock.lock();
                try {
                    if (!(!pipe.getSourceClosed$okio())) {
                        object = "closed";
                        throw new IllegalStateException(object.toString());
                    }
                    if (pipe.getCanceled$okio()) {
                        throw new IOException("canceled");
                    }
                    while (pipe.getBuffer$okio().size() == 0L) {
                        if (pipe.getSinkClosed$okio()) {
                            return -1L;
                        }
                        this.timeout.awaitSignal(pipe.getCondition());
                        if (!pipe.getCanceled$okio()) continue;
                        throw new IOException("canceled");
                    }
                    long l3 = pipe.getBuffer$okio().read((Buffer)object, l2);
                    pipe.getCondition().signalAll();
                    return l3;
                }
                finally {
                    lock.unlock();
                }
            }

            public final void close() {
                Lock lock = this.this$0.getLock();
                Pipe pipe = this.this$0;
                lock.lock();
                try {
                    pipe.setSourceClosed$okio(true);
                    pipe.getCondition().signalAll();
                    return;
                }
                finally {
                    lock.unlock();
                }
            }

            @NotNull
            public final Timeout timeout() {
                return this.timeout;
            }
        };
    }

    public final long getMaxBufferSize$okio() {
        return this.maxBufferSize;
    }

    @NotNull
    public final Buffer getBuffer$okio() {
        return this.buffer;
    }

    public final boolean getCanceled$okio() {
        return this.canceled;
    }

    public final void setCanceled$okio(boolean bl) {
        this.canceled = bl;
    }

    public final boolean getSinkClosed$okio() {
        return this.sinkClosed;
    }

    public final void setSinkClosed$okio(boolean bl) {
        this.sinkClosed = bl;
    }

    public final boolean getSourceClosed$okio() {
        return this.sourceClosed;
    }

    public final void setSourceClosed$okio(boolean bl) {
        this.sourceClosed = bl;
    }

    @Nullable
    public final Sink getFoldedSink$okio() {
        return this.foldedSink;
    }

    public final void setFoldedSink$okio(@Nullable Sink sink2) {
        this.foldedSink = sink2;
    }

    @NotNull
    public final ReentrantLock getLock() {
        return this.lock;
    }

    @NotNull
    public final Condition getCondition() {
        return this.condition;
    }

    @JvmName(name="sink")
    @NotNull
    public final Sink sink() {
        return this.sink;
    }

    @JvmName(name="source")
    @NotNull
    public final Source source() {
        return this.source;
    }

    public final void fold(@NotNull Sink sink2) throws IOException {
        Intrinsics.checkNotNullParameter(sink2, "");
        while (true) {
            Buffer buffer;
            boolean bl;
            Lock lock = this.lock;
            lock.lock();
            try {
                if (!(this.foldedSink == null)) {
                    String string = "sink already folded";
                    throw new IllegalStateException(string.toString());
                }
                if (this.canceled) {
                    this.foldedSink = sink2;
                    throw new IOException("canceled");
                }
                if (this.buffer.exhausted()) {
                    this.sourceClosed = true;
                    this.foldedSink = sink2;
                    return;
                }
                bl = this.sinkClosed;
                buffer = new Buffer();
                buffer.write(this.buffer, this.buffer.size());
                this.condition.signalAll();
            }
            finally {
                lock.unlock();
            }
            try {
                Buffer buffer2 = buffer;
                sink2.write(buffer2, buffer2.size());
                if (bl) {
                    sink2.close();
                    continue;
                }
                sink2.flush();
            }
            catch (Throwable throwable) {
                Lock lock2 = this.lock;
                lock2.lock();
                try {
                    this.sourceClosed = true;
                    this.condition.signalAll();
                }
                finally {
                    lock2.unlock();
                }
                throw throwable;
            }
        }
    }

    private final void forward(Sink sink2, Function1<? super Sink, Unit> function1) {
        Timeout timeout2 = sink2.timeout();
        Timeout timeout3 = this.sink().timeout();
        long l2 = timeout2.timeoutNanos();
        timeout2.timeout(Timeout.Companion.minTimeout(timeout3.timeoutNanos(), timeout2.timeoutNanos()), TimeUnit.NANOSECONDS);
        if (timeout2.hasDeadline()) {
            long l3 = timeout2.deadlineNanoTime();
            if (timeout3.hasDeadline()) {
                Timeout timeout4 = timeout2;
                timeout4.deadlineNanoTime(Math.min(timeout4.deadlineNanoTime(), timeout3.deadlineNanoTime()));
            }
            try {
                function1.invoke(sink2);
                return;
            }
            finally {
                InlineMarker.finallyStart(1);
                timeout2.timeout(l2, TimeUnit.NANOSECONDS);
                if (timeout3.hasDeadline()) {
                    timeout2.deadlineNanoTime(l3);
                }
                InlineMarker.finallyEnd(1);
            }
        }
        if (timeout3.hasDeadline()) {
            timeout2.deadlineNanoTime(timeout3.deadlineNanoTime());
        }
        try {
            function1.invoke(sink2);
            return;
        }
        finally {
            InlineMarker.finallyStart(1);
            timeout2.timeout(l2, TimeUnit.NANOSECONDS);
            if (timeout3.hasDeadline()) {
                timeout2.clearDeadline();
            }
            InlineMarker.finallyEnd(1);
        }
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="sink", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_sink")
    @NotNull
    public final Sink -deprecated_sink() {
        return this.sink;
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="source", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_source")
    @NotNull
    public final Source -deprecated_source() {
        return this.source;
    }

    public final void cancel() {
        Lock lock = this.lock;
        lock.lock();
        try {
            this.canceled = true;
            this.buffer.clear();
            this.condition.signalAll();
            return;
        }
        finally {
            lock.unlock();
        }
    }
}


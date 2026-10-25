/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.RejectedExecutionException;
import java.util.logging.Level;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import okhttp3.internal.Util;
import okhttp3.internal.concurrent.Task;
import okhttp3.internal.concurrent.TaskLoggerKt;
import okhttp3.internal.concurrent.TaskRunner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000V\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010!\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u00002\u00020\u0001:\u00013B\u0017\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0006\u0010!\u001a\u00020\"J\r\u0010#\u001a\u00020\u000eH\u0000\u00a2\u0006\u0002\b$J8\u0010%\u001a\u00020\"2\u0006\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010&\u001a\u00020'2\b\b\u0002\u0010(\u001a\u00020\u000e2\u000e\b\u0004\u0010)\u001a\b\u0012\u0004\u0012\u00020\"0*H\u0086\b\u00f8\u0001\u0000J\u0006\u0010+\u001a\u00020,J.\u0010-\u001a\u00020\"2\u0006\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010&\u001a\u00020'2\u000e\b\u0004\u0010)\u001a\b\u0012\u0004\u0012\u00020'0*H\u0086\b\u00f8\u0001\u0000J\u0018\u0010-\u001a\u00020\"2\u0006\u0010.\u001a\u00020\b2\b\b\u0002\u0010&\u001a\u00020'J%\u0010/\u001a\u00020\u000e2\u0006\u0010.\u001a\u00020\b2\u0006\u0010&\u001a\u00020'2\u0006\u00100\u001a\u00020\u000eH\u0000\u00a2\u0006\u0002\b1J\u0006\u0010\u001c\u001a\u00020\"J\b\u00102\u001a\u00020\u0005H\u0016R\u001c\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0080\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u001a\u0010\r\u001a\u00020\u000eX\u0080\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012R\u001a\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\b0\u0014X\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0014\u0010\u0004\u001a\u00020\u0005X\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0017\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\b0\u001a8F\u00a2\u0006\u0006\u001a\u0004\b\u001b\u0010\u0016R\u001a\u0010\u001c\u001a\u00020\u000eX\u0080\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u0010\"\u0004\b\u001e\u0010\u0012R\u0014\u0010\u0002\u001a\u00020\u0003X\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 \u0082\u0002\u0007\n\u0005\b\u009920\u0001\u00a8\u00064"}, d2={"Lokhttp3/internal/concurrent/TaskQueue;", "", "taskRunner", "Lokhttp3/internal/concurrent/TaskRunner;", "name", "", "(Lokhttp3/internal/concurrent/TaskRunner;Ljava/lang/String;)V", "activeTask", "Lokhttp3/internal/concurrent/Task;", "getActiveTask$okhttp", "()Lokhttp3/internal/concurrent/Task;", "setActiveTask$okhttp", "(Lokhttp3/internal/concurrent/Task;)V", "cancelActiveTask", "", "getCancelActiveTask$okhttp", "()Z", "setCancelActiveTask$okhttp", "(Z)V", "futureTasks", "", "getFutureTasks$okhttp", "()Ljava/util/List;", "getName$okhttp", "()Ljava/lang/String;", "scheduledTasks", "", "getScheduledTasks", "shutdown", "getShutdown$okhttp", "setShutdown$okhttp", "getTaskRunner$okhttp", "()Lokhttp3/internal/concurrent/TaskRunner;", "cancelAll", "", "cancelAllAndDecide", "cancelAllAndDecide$okhttp", "execute", "delayNanos", "", "cancelable", "block", "Lkotlin/Function0;", "idleLatch", "Ljava/util/concurrent/CountDownLatch;", "schedule", "task", "scheduleAndDecide", "recurrence", "scheduleAndDecide$okhttp", "toString", "AwaitIdleTask", "okhttp"})
@SourceDebugExtension(value={"SMAP\nTaskQueue.kt\nKotlin\n*S Kotlin\n*F\n+ 1 TaskQueue.kt\nokhttp3/internal/concurrent/TaskQueue\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 TaskLogger.kt\nokhttp3/internal/concurrent/TaskLoggerKt\n+ 4 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 5 Util.kt\nokhttp3/internal/Util\n*L\n1#1,218:1\n1#2:219\n25#3,4:220\n25#3,4:224\n25#3,4:228\n25#3,4:232\n25#3,4:251\n350#4,7:236\n615#5,4:243\n615#5,4:247\n*S KotlinDebug\n*F\n+ 1 TaskQueue.kt\nokhttp3/internal/concurrent/TaskQueue\n*L\n65#1:220,4\n68#1:224,4\n153#1:228,4\n159#1:232,4\n208#1:251,4\n165#1:236,7\n179#1:243,4\n189#1:247,4\n*E\n"})
public final class TaskQueue {
    @NotNull
    private final TaskRunner taskRunner;
    @NotNull
    private final String name;
    private boolean shutdown;
    @Nullable
    private Task activeTask;
    @NotNull
    private final List<Task> futureTasks;
    private boolean cancelActiveTask;

    public TaskQueue(@NotNull TaskRunner taskRunner, @NotNull String string) {
        Intrinsics.checkNotNullParameter(taskRunner, "");
        Intrinsics.checkNotNullParameter(string, "");
        this.taskRunner = taskRunner;
        this.name = string;
        this.futureTasks = new ArrayList();
    }

    @NotNull
    public final TaskRunner getTaskRunner$okhttp() {
        return this.taskRunner;
    }

    @NotNull
    public final String getName$okhttp() {
        return this.name;
    }

    public final boolean getShutdown$okhttp() {
        return this.shutdown;
    }

    public final void setShutdown$okhttp(boolean bl) {
        this.shutdown = bl;
    }

    @Nullable
    public final Task getActiveTask$okhttp() {
        return this.activeTask;
    }

    public final void setActiveTask$okhttp(@Nullable Task task) {
        this.activeTask = task;
    }

    @NotNull
    public final List<Task> getFutureTasks$okhttp() {
        return this.futureTasks;
    }

    public final boolean getCancelActiveTask$okhttp() {
        return this.cancelActiveTask;
    }

    public final void setCancelActiveTask$okhttp(boolean bl) {
        this.cancelActiveTask = bl;
    }

    @NotNull
    public final List<Task> getScheduledTasks() {
        TaskRunner taskRunner = this.taskRunner;
        synchronized (taskRunner) {
            List<Task> list2 = CollectionsKt.toList((Iterable)this.futureTasks);
            return list2;
        }
    }

    public final void schedule(@NotNull Task object, long l2) {
        Intrinsics.checkNotNullParameter(object, "");
        TaskRunner taskRunner = this.taskRunner;
        synchronized (taskRunner) {
            if (this.shutdown) {
                if (((Task)object).getCancelable()) {
                    if (TaskRunner.Companion.getLogger().isLoggable(Level.FINE)) {
                        Task task = object;
                        object = this;
                        Task task2 = task;
                        String string = "schedule canceled (queue is shutdown)";
                        TaskLoggerKt.access$log(task2, (TaskQueue)object, string);
                    }
                    return;
                }
                if (TaskRunner.Companion.getLogger().isLoggable(Level.FINE)) {
                    Task task = object;
                    object = this;
                    Task task3 = task;
                    String string = "schedule failed (queue is shutdown)";
                    TaskLoggerKt.access$log(task3, (TaskQueue)object, string);
                }
                throw new RejectedExecutionException();
            }
            if (this.scheduleAndDecide$okhttp((Task)object, l2, false)) {
                this.taskRunner.kickCoordinator$okhttp(this);
            }
            return;
        }
    }

    public static /* synthetic */ void schedule$default(TaskQueue taskQueue, Task task, long l2, int n2, Object object) {
        if ((n2 & 2) != 0) {
            l2 = 0L;
        }
        taskQueue.schedule(task, l2);
    }

    public final void schedule(@NotNull String string, long l2, @NotNull Function0<Long> function0) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(function0, "");
        this.schedule(new Task(string, function0){
            final /* synthetic */ Function0<Long> $block;
            {
                this.$block = function0;
                super(string, false, 2, null);
            }

            public final long runOnce() {
                return ((Number)this.$block.invoke()).longValue();
            }
        }, l2);
    }

    public static /* synthetic */ void schedule$default(TaskQueue taskQueue, String string, long l2, Function0 function0, int n2, Object object) {
        if ((n2 & 2) != 0) {
            l2 = 0L;
        }
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(function0, "");
        taskQueue.schedule(new /* invalid duplicate definition of identical inner class */, l2);
    }

    public final void execute(@NotNull String string, long l2, boolean bl, @NotNull Function0<Unit> function0) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(function0, "");
        this.schedule(new Task(string, bl, function0){
            final /* synthetic */ Function0<Unit> $block;
            {
                this.$block = function0;
                super(string, bl);
            }

            public final long runOnce() {
                this.$block.invoke();
                return -1L;
            }
        }, l2);
    }

    public static /* synthetic */ void execute$default(TaskQueue taskQueue, String string, long l2, boolean bl, Function0 function0, int n2, Object object) {
        if ((n2 & 2) != 0) {
            l2 = 0L;
        }
        if ((n2 & 4) != 0) {
            bl = true;
        }
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(function0, "");
        taskQueue.schedule(new /* invalid duplicate definition of identical inner class */, l2);
    }

    @NotNull
    public final CountDownLatch idleLatch() {
        TaskRunner taskRunner = this.taskRunner;
        synchronized (taskRunner) {
            Object object;
            block9: {
                block8: {
                    if (this.activeTask != null || !this.futureTasks.isEmpty()) break block8;
                    CountDownLatch countDownLatch = new CountDownLatch(0);
                    return countDownLatch;
                }
                object = this.activeTask;
                if (!(object instanceof AwaitIdleTask)) break block9;
                object = ((AwaitIdleTask)object).getLatch();
                return object;
            }
            object = this.futureTasks.iterator();
            while (object.hasNext()) {
                Task task = object.next();
                if (!(task instanceof AwaitIdleTask)) continue;
                object = ((AwaitIdleTask)task).getLatch();
                return object;
            }
            object = new AwaitIdleTask();
            if (this.scheduleAndDecide$okhttp((Task)object, 0L, false)) {
                this.taskRunner.kickCoordinator$okhttp(this);
            }
            object = ((AwaitIdleTask)object).getLatch();
            return object;
        }
    }

    public final boolean scheduleAndDecide$okhttp(@NotNull Task task, long l2, boolean bl) {
        int n2;
        int n3;
        block7: {
            Intrinsics.checkNotNullParameter(task, "");
            task.initQueue$okhttp(this);
            long l3 = this.taskRunner.getBackend().nanoTime();
            long l4 = l3 + l2;
            int n4 = this.futureTasks.indexOf(task);
            if (n4 != -1) {
                if (task.getNextExecuteNanoTime$okhttp() <= l4) {
                    if (TaskRunner.Companion.getLogger().isLoggable(Level.FINE)) {
                        TaskQueue taskQueue = this;
                        Task task2 = task;
                        String string = "already scheduled";
                        TaskLoggerKt.access$log(task2, taskQueue, string);
                    }
                    return false;
                }
                this.futureTasks.remove(n4);
            }
            task.setNextExecuteNanoTime$okhttp(l4);
            if (TaskRunner.Companion.getLogger().isLoggable(Level.FINE)) {
                TaskQueue taskQueue = this;
                Task task3 = task;
                String string = bl ? "run again after " + TaskLoggerKt.formatDuration(l4 - l3) : "scheduled after " + TaskLoggerKt.formatDuration(l4 - l3);
                TaskLoggerKt.access$log(task3, taskQueue, string);
            }
            Object object = this.futureTasks;
            int n5 = 0;
            object = object.iterator();
            while (object.hasNext()) {
                Object object2 = object.next();
                if (((Task)(object2 = (Task)object2)).getNextExecuteNanoTime$okhttp() - l3 > l2) {
                    n3 = n5;
                    break block7;
                }
                ++n5;
            }
            n3 = n2 = -1;
        }
        if (n3 == -1) {
            n2 = this.futureTasks.size();
        }
        this.futureTasks.add(n2, task);
        return n2 == 0;
    }

    public final void cancelAll() {
        Object object = this;
        if (Util.assertionsEnabled && Thread.holdsLock(object)) {
            throw new AssertionError((Object)("Thread " + Thread.currentThread().getName() + " MUST NOT hold lock on " + object));
        }
        object = this.taskRunner;
        synchronized (object) {
            if (this.cancelAllAndDecide$okhttp()) {
                this.taskRunner.kickCoordinator$okhttp(this);
            }
            return;
        }
    }

    public final void shutdown() {
        Object object = this;
        if (Util.assertionsEnabled && Thread.holdsLock(object)) {
            throw new AssertionError((Object)("Thread " + Thread.currentThread().getName() + " MUST NOT hold lock on " + object));
        }
        object = this.taskRunner;
        synchronized (object) {
            this.shutdown = true;
            if (this.cancelAllAndDecide$okhttp()) {
                this.taskRunner.kickCoordinator$okhttp(this);
            }
            return;
        }
    }

    public final boolean cancelAllAndDecide$okhttp() {
        if (this.activeTask != null) {
            Task task = this.activeTask;
            Intrinsics.checkNotNull(task);
            if (task.getCancelable()) {
                this.cancelActiveTask = true;
            }
        }
        boolean bl = false;
        for (int i2 = this.futureTasks.size() - 1; i2 >= 0; --i2) {
            if (!this.futureTasks.get(i2).getCancelable()) continue;
            Task task = this.futureTasks.get(i2);
            if (TaskRunner.Companion.getLogger().isLoggable(Level.FINE)) {
                TaskQueue taskQueue = this;
                String string = "canceled";
                TaskLoggerKt.access$log(task, taskQueue, string);
            }
            bl = true;
            this.futureTasks.remove(i2);
        }
        return bl;
    }

    @NotNull
    public final String toString() {
        return this.name;
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0007\u001a\u00020\bH\u0016R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\t"}, d2={"Lokhttp3/internal/concurrent/TaskQueue$AwaitIdleTask;", "Lokhttp3/internal/concurrent/Task;", "()V", "latch", "Ljava/util/concurrent/CountDownLatch;", "getLatch", "()Ljava/util/concurrent/CountDownLatch;", "runOnce", "", "okhttp"})
    static final class AwaitIdleTask
    extends Task {
        @NotNull
        private final CountDownLatch latch = new CountDownLatch(1);

        public AwaitIdleTask() {
            super(Util.okHttpName + " awaitIdle", false);
        }

        @NotNull
        public final CountDownLatch getLatch() {
            return this.latch;
        }

        @Override
        public final long runOnce() {
            this.latch.countDown();
            return -1L;
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.concurrent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmField;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import okhttp3.internal.Util;
import okhttp3.internal.concurrent.Task;
import okhttp3.internal.concurrent.TaskLoggerKt;
import okhttp3.internal.concurrent.TaskQueue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000J\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\r\u0018\u0000 #2\u00020\u0001:\u0003\"#$B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\t0\u0014J\u0018\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\rH\u0002J\b\u0010\u001a\u001a\u0004\u0018\u00010\u0018J\u0010\u0010\u001b\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018H\u0002J\u0006\u0010\u001c\u001a\u00020\u0016J\u0015\u0010\u001d\u001a\u00020\u00162\u0006\u0010\u001e\u001a\u00020\tH\u0000\u00a2\u0006\u0002\b\u001fJ\u0006\u0010 \u001a\u00020\tJ\u0010\u0010!\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018H\u0002R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006%"}, d2={"Lokhttp3/internal/concurrent/TaskRunner;", "", "backend", "Lokhttp3/internal/concurrent/TaskRunner$Backend;", "(Lokhttp3/internal/concurrent/TaskRunner$Backend;)V", "getBackend", "()Lokhttp3/internal/concurrent/TaskRunner$Backend;", "busyQueues", "", "Lokhttp3/internal/concurrent/TaskQueue;", "coordinatorWaiting", "", "coordinatorWakeUpAt", "", "nextQueueName", "", "readyQueues", "runnable", "Ljava/lang/Runnable;", "activeQueues", "", "afterRun", "", "task", "Lokhttp3/internal/concurrent/Task;", "delayNanos", "awaitTaskToRun", "beforeRun", "cancelAll", "kickCoordinator", "taskQueue", "kickCoordinator$okhttp", "newQueue", "runTask", "Backend", "Companion", "RealBackend", "okhttp"})
@SourceDebugExtension(value={"SMAP\nTaskRunner.kt\nKotlin\n*S Kotlin\n*F\n+ 1 TaskRunner.kt\nokhttp3/internal/concurrent/TaskRunner\n+ 2 Util.kt\nokhttp3/internal/Util\n+ 3 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,314:1\n608#2,4:315\n608#2,4:319\n615#2,4:323\n608#2,4:327\n608#2,4:331\n1#3:335\n*S KotlinDebug\n*F\n+ 1 TaskRunner.kt\nokhttp3/internal/concurrent/TaskRunner\n*L\n79#1:315,4\n97#1:319,4\n108#1:323,4\n126#1:327,4\n152#1:331,4\n*E\n"})
public final class TaskRunner {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final Backend backend;
    private int nextQueueName;
    private boolean coordinatorWaiting;
    private long coordinatorWakeUpAt;
    @NotNull
    private final List<TaskQueue> busyQueues;
    @NotNull
    private final List<TaskQueue> readyQueues;
    @NotNull
    private final Runnable runnable;
    @JvmField
    @NotNull
    public static final TaskRunner INSTANCE = new TaskRunner(new RealBackend(Util.threadFactory(Util.okHttpName + " TaskRunner", true)));
    @NotNull
    private static final Logger logger;

    public TaskRunner(@NotNull Backend backend) {
        Intrinsics.checkNotNullParameter(backend, "");
        this.backend = backend;
        this.nextQueueName = 10000;
        this.busyQueues = new ArrayList();
        this.readyQueues = new ArrayList();
        this.runnable = new Runnable(this){
            final /* synthetic */ TaskRunner this$0;
            {
                this.this$0 = taskRunner;
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            public final void run() {
                while (true) {
                    long l2;
                    TaskQueue taskQueue;
                    Task task;
                    TaskRunner taskRunner = this.this$0;
                    Object object = this.this$0;
                    TaskRunner taskRunner2 = taskRunner;
                    synchronized (taskRunner2) {
                        task = ((TaskRunner)object).awaitTaskToRun();
                    }
                    Task task2 = task;
                    if (task2 == null) {
                        return;
                    }
                    object = task2;
                    Intrinsics.checkNotNull(task2.getQueue$okhttp());
                    taskRunner = this.this$0;
                    long l3 = -1L;
                    boolean bl = TaskRunner.Companion.getLogger().isLoggable(Level.FINE);
                    if (bl) {
                        l3 = taskQueue.getTaskRunner$okhttp().getBackend().nanoTime();
                        TaskLoggerKt.access$log((Task)object, taskQueue, "starting");
                    }
                    boolean bl2 = false;
                    try {
                        try {
                            TaskRunner.access$runTask(taskRunner, (Task)object);
                        }
                        catch (Throwable throwable) {
                            taskRunner.getBackend().execute(this);
                            throw throwable;
                        }
                        bl2 = true;
                        if (!bl) continue;
                        l2 = taskQueue.getTaskRunner$okhttp().getBackend().nanoTime() - l3;
                    }
                    catch (Throwable throwable) {
                        if (!bl) throw throwable;
                        long l4 = taskQueue.getTaskRunner$okhttp().getBackend().nanoTime() - l3;
                        if (bl2) {
                            TaskLoggerKt.access$log((Task)object, taskQueue, "finished run in " + TaskLoggerKt.formatDuration(l4));
                            throw throwable;
                        }
                        TaskLoggerKt.access$log((Task)object, taskQueue, "failed a run in " + TaskLoggerKt.formatDuration(l4));
                        throw throwable;
                    }
                    TaskLoggerKt.access$log((Task)object, taskQueue, "finished run in " + TaskLoggerKt.formatDuration(l2));
                }
            }
        };
    }

    @NotNull
    public final Backend getBackend() {
        return this.backend;
    }

    public final void kickCoordinator$okhttp(@NotNull TaskQueue taskQueue) {
        Intrinsics.checkNotNullParameter(taskQueue, "");
        TaskRunner taskRunner = this;
        if (Util.assertionsEnabled && !Thread.holdsLock(taskRunner)) {
            throw new AssertionError((Object)("Thread " + Thread.currentThread().getName() + " MUST hold lock on " + taskRunner));
        }
        if (taskQueue.getActiveTask$okhttp() == null) {
            if (!((Collection)taskQueue.getFutureTasks$okhttp()).isEmpty()) {
                Util.addIfAbsent(this.readyQueues, taskQueue);
            } else {
                this.readyQueues.remove(taskQueue);
            }
        }
        if (this.coordinatorWaiting) {
            this.backend.coordinatorNotify(this);
            return;
        }
        this.backend.execute(this.runnable);
    }

    private final void beforeRun(Task task) {
        Object object = this;
        if (Util.assertionsEnabled && !Thread.holdsLock(object)) {
            throw new AssertionError((Object)("Thread " + Thread.currentThread().getName() + " MUST hold lock on " + object));
        }
        task.setNextExecuteNanoTime$okhttp(-1L);
        TaskQueue taskQueue = task.getQueue$okhttp();
        Intrinsics.checkNotNull(taskQueue);
        object = taskQueue;
        taskQueue.getFutureTasks$okhttp().remove(task);
        this.readyQueues.remove(object);
        ((TaskQueue)object).setActiveTask$okhttp(task);
        this.busyQueues.add((TaskQueue)object);
    }

    private final void runTask(Task task) {
        TaskRunner taskRunner;
        long l2;
        Object object = this;
        if (Util.assertionsEnabled && Thread.holdsLock(object)) {
            throw new AssertionError((Object)("Thread " + Thread.currentThread().getName() + " MUST NOT hold lock on " + object));
        }
        object = Thread.currentThread();
        String string = ((Thread)object).getName();
        ((Thread)object).setName(task.getName());
        try {
            l2 = task.runOnce();
            taskRunner = this;
        }
        catch (Throwable throwable) {
            TaskRunner taskRunner2 = this;
            synchronized (taskRunner2) {
                this.afterRun(task, -1L);
            }
            ((Thread)object).setName(string);
            throw throwable;
        }
        synchronized (taskRunner) {
            this.afterRun(task, l2);
        }
        ((Thread)object).setName(string);
    }

    private final void afterRun(Task object, long l2) {
        Object object2 = this;
        if (Util.assertionsEnabled && !Thread.holdsLock(object2)) {
            throw new AssertionError((Object)("Thread " + Thread.currentThread().getName() + " MUST hold lock on " + object2));
        }
        TaskQueue taskQueue = ((Task)object).getQueue$okhttp();
        Intrinsics.checkNotNull(taskQueue);
        object2 = taskQueue;
        if (!(taskQueue.getActiveTask$okhttp() == object)) {
            object = "Check failed.";
            throw new IllegalStateException(object.toString());
        }
        boolean bl = ((TaskQueue)object2).getCancelActiveTask$okhttp();
        ((TaskQueue)object2).setCancelActiveTask$okhttp(false);
        ((TaskQueue)object2).setActiveTask$okhttp(null);
        this.busyQueues.remove(object2);
        if (l2 != -1L && !bl && !((TaskQueue)object2).getShutdown$okhttp()) {
            ((TaskQueue)object2).scheduleAndDecide$okhttp((Task)object, l2, true);
        }
        if (!((Collection)((TaskQueue)object2).getFutureTasks$okhttp()).isEmpty()) {
            this.readyQueues.add((TaskQueue)object2);
        }
    }

    @Nullable
    public final Task awaitTaskToRun() {
        TaskRunner taskRunner = this;
        if (Util.assertionsEnabled && !Thread.holdsLock(taskRunner)) {
            throw new AssertionError((Object)("Thread " + Thread.currentThread().getName() + " MUST hold lock on " + taskRunner));
        }
        while (!this.readyQueues.isEmpty()) {
            long l2 = this.backend.nanoTime();
            long l3 = Long.MAX_VALUE;
            Task object = null;
            boolean bl = false;
            for (TaskQueue taskQueue : this.readyQueues) {
                Task object2 = taskQueue.getFutureTasks$okhttp().get(0);
                long l4 = Math.max(0L, object2.getNextExecuteNanoTime$okhttp() - l2);
                if (l4 > 0L) {
                    l3 = Math.min(l4, l3);
                    continue;
                }
                if (object != null) {
                    bl = true;
                    break;
                }
                object = object2;
            }
            if (object != null) {
                this.beforeRun(object);
                if (bl || !this.coordinatorWaiting && !((Collection)this.readyQueues).isEmpty()) {
                    this.backend.execute(this.runnable);
                }
                return object;
            }
            if (this.coordinatorWaiting) {
                if (l3 < this.coordinatorWakeUpAt - l2) {
                    this.backend.coordinatorNotify(this);
                }
                return null;
            }
            this.coordinatorWaiting = true;
            this.coordinatorWakeUpAt = l2 + l3;
            try {
                this.backend.coordinatorWait(this, l3);
                continue;
            }
            catch (InterruptedException interruptedException) {
                this.cancelAll();
                continue;
            }
            finally {
                this.coordinatorWaiting = false;
                continue;
            }
            break;
        }
        return null;
    }

    @NotNull
    public final TaskQueue newQueue() {
        int n2;
        TaskRunner taskRunner = this;
        synchronized (taskRunner) {
            n2 = this.nextQueueName;
            this.nextQueueName = n2 + 1;
        }
        int n3 = n2;
        return new TaskQueue(this, "Q" + n3);
    }

    @NotNull
    public final List<TaskQueue> activeQueues() {
        TaskRunner taskRunner = this;
        synchronized (taskRunner) {
            List<TaskQueue> list2 = CollectionsKt.plus((Collection)this.busyQueues, (Iterable)this.readyQueues);
            return list2;
        }
    }

    public final void cancelAll() {
        int n2;
        for (n2 = this.busyQueues.size() - 1; n2 >= 0; --n2) {
            this.busyQueues.get(n2).cancelAllAndDecide$okhttp();
        }
        for (n2 = this.readyQueues.size() - 1; n2 >= 0; --n2) {
            TaskQueue taskQueue = this.readyQueues.get(n2);
            taskQueue.cancelAllAndDecide$okhttp();
            if (!taskQueue.getFutureTasks$okhttp().isEmpty()) continue;
            this.readyQueues.remove(n2);
        }
    }

    public static final /* synthetic */ void access$runTask(TaskRunner taskRunner, Task task) {
        taskRunner.runTask(task);
    }

    static {
        Logger logger = Logger.getLogger(TaskRunner.class.getName());
        Intrinsics.checkNotNullExpressionValue(logger, "");
        TaskRunner.logger = logger;
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0018\u0010\u0007\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\tH&J\u0010\u0010\n\u001a\u00020\u00032\u0006\u0010\u000b\u001a\u00020\fH&J\b\u0010\r\u001a\u00020\tH&\u00a8\u0006\u000e"}, d2={"Lokhttp3/internal/concurrent/TaskRunner$Backend;", "", "beforeTask", "", "taskRunner", "Lokhttp3/internal/concurrent/TaskRunner;", "coordinatorNotify", "coordinatorWait", "nanos", "", "execute", "runnable", "Ljava/lang/Runnable;", "nanoTime", "okhttp"})
    public static interface Backend {
        public void beforeTask(@NotNull TaskRunner var1);

        public long nanoTime();

        public void coordinatorNotify(@NotNull TaskRunner var1);

        public void coordinatorWait(@NotNull TaskRunner var1, long var2);

        public void execute(@NotNull Runnable var1);
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0010\u0010\u0003\u001a\u00020\u00048\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\b\u00a8\u0006\t"}, d2={"Lokhttp3/internal/concurrent/TaskRunner$Companion;", "", "()V", "INSTANCE", "Lokhttp3/internal/concurrent/TaskRunner;", "logger", "Ljava/util/logging/Logger;", "getLogger", "()Ljava/util/logging/Logger;", "okhttp"})
    public static final class Companion {
        private Companion() {
        }

        @NotNull
        public final Logger getLogger() {
            return logger;
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016J\u0010\u0010\u000b\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016J\u0018\u0010\f\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\r\u001a\u00020\u000eH\u0016J\u0010\u0010\u000f\u001a\u00020\b2\u0006\u0010\u0010\u001a\u00020\u0011H\u0016J\b\u0010\u0012\u001a\u00020\u000eH\u0016J\u0006\u0010\u0013\u001a\u00020\bR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2={"Lokhttp3/internal/concurrent/TaskRunner$RealBackend;", "Lokhttp3/internal/concurrent/TaskRunner$Backend;", "threadFactory", "Ljava/util/concurrent/ThreadFactory;", "(Ljava/util/concurrent/ThreadFactory;)V", "executor", "Ljava/util/concurrent/ThreadPoolExecutor;", "beforeTask", "", "taskRunner", "Lokhttp3/internal/concurrent/TaskRunner;", "coordinatorNotify", "coordinatorWait", "nanos", "", "execute", "runnable", "Ljava/lang/Runnable;", "nanoTime", "shutdown", "okhttp"})
    @SourceDebugExtension(value={"SMAP\nTaskRunner.kt\nKotlin\n*S Kotlin\n*F\n+ 1 TaskRunner.kt\nokhttp3/internal/concurrent/TaskRunner$RealBackend\n+ 2 Util.kt\nokhttp3/internal/Util\n*L\n1#1,314:1\n560#2:315\n*S KotlinDebug\n*F\n+ 1 TaskRunner.kt\nokhttp3/internal/concurrent/TaskRunner$RealBackend\n*L\n281#1:315\n*E\n"})
    public static final class RealBackend
    implements Backend {
        @NotNull
        private final ThreadPoolExecutor executor;

        public RealBackend(@NotNull ThreadFactory threadFactory) {
            Intrinsics.checkNotNullParameter(threadFactory, "");
            this.executor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, (BlockingQueue<Runnable>)new SynchronousQueue(), threadFactory);
        }

        @Override
        public final void beforeTask(@NotNull TaskRunner taskRunner) {
            Intrinsics.checkNotNullParameter(taskRunner, "");
        }

        @Override
        public final long nanoTime() {
            return System.nanoTime();
        }

        @Override
        public final void coordinatorNotify(@NotNull TaskRunner taskRunner) {
            Intrinsics.checkNotNullParameter(taskRunner, "");
            ((Object)taskRunner).notify();
        }

        @Override
        public final void coordinatorWait(@NotNull TaskRunner taskRunner, long l2) throws InterruptedException {
            Intrinsics.checkNotNullParameter(taskRunner, "");
            long l3 = l2 / 1000000L;
            long l4 = l2 - l3 * 1000000L;
            if (l3 > 0L || l2 > 0L) {
                ((Object)taskRunner).wait(l3, (int)l4);
            }
        }

        @Override
        public final void execute(@NotNull Runnable runnable2) {
            Intrinsics.checkNotNullParameter(runnable2, "");
            this.executor.execute(runnable2);
        }

        public final void shutdown() {
            this.executor.shutdown();
        }
    }
}


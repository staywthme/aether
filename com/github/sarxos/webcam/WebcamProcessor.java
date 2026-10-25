/*
 * Decompiled with CFR 0.152.
 */
package com.github.sarxos.webcam;

import com.github.sarxos.webcam.WebcamException;
import com.github.sarxos.webcam.WebcamExceptionHandler;
import com.github.sarxos.webcam.WebcamTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebcamProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(WebcamProcessor.class);
    private static final AtomicBoolean started = new AtomicBoolean(false);
    private static ExecutorService runner = null;
    private static final AtomicProcessor processor = new AtomicProcessor();
    private static final WebcamProcessor INSTANCE = new WebcamProcessor();

    private WebcamProcessor() {
    }

    public void process(WebcamTask webcamTask) throws InterruptedException {
        if (started.compareAndSet(false, true)) {
            runner = Executors.newSingleThreadExecutor(new ProcessorThreadFactory());
            runner.execute(processor);
        }
        if (!runner.isShutdown()) {
            processor.process(webcamTask);
            return;
        }
        throw new RejectedExecutionException("Cannot process because processor runner has been already shut down");
    }

    public void shutdown() {
        if (started.compareAndSet(true, false)) {
            LOG.debug("Shutting down webcam processor");
            runner.shutdown();
            LOG.debug("Awaiting tasks termination");
            while (runner.isTerminated()) {
                try {
                    runner.awaitTermination(100L, TimeUnit.MILLISECONDS);
                }
                catch (InterruptedException interruptedException) {
                    return;
                }
                runner.shutdownNow();
            }
            LOG.debug("All tasks has been terminated");
        }
    }

    public static synchronized WebcamProcessor getInstance() {
        return INSTANCE;
    }

    static final class AtomicProcessor
    implements Runnable {
        private SynchronousQueue<WebcamTask> inbound = new SynchronousQueue(true);
        private SynchronousQueue<WebcamTask> outbound = new SynchronousQueue(true);

        private AtomicProcessor() {
        }

        public final void process(WebcamTask object) throws InterruptedException {
            this.inbound.put((WebcamTask)object);
            object = this.outbound.take().getThrowable();
            if (object != null) {
                throw new WebcamException("Cannot execute task", (Throwable)object);
            }
        }

        /*
         * Loose catch block
         */
        @Override
        public final void run() {
            while (true) {
                WebcamTask webcamTask = null;
                webcamTask = this.inbound.take();
                webcamTask.handle();
                if (webcamTask == null) continue;
                try {
                    this.outbound.put(webcamTask);
                }
                catch (InterruptedException interruptedException) {
                    return;
                }
                catch (Exception exception) {
                    throw new RuntimeException("Cannot put task into outbound queue", exception);
                }
                catch (InterruptedException interruptedException) {
                    if (webcamTask == null) break;
                    try {
                        this.outbound.put(webcamTask);
                        return;
                    }
                    catch (InterruptedException interruptedException2) {
                        return;
                    }
                    catch (Exception exception) {
                        throw new RuntimeException("Cannot put task into outbound queue", exception);
                    }
                }
                catch (Throwable throwable) {
                    try {
                        if (webcamTask != null) {
                            webcamTask.setThrowable(throwable);
                        }
                        if (webcamTask == null) continue;
                    }
                    catch (Throwable throwable2) {
                        if (webcamTask != null) {
                            try {
                                this.outbound.put(webcamTask);
                            }
                            catch (InterruptedException interruptedException) {
                                return;
                            }
                            catch (Exception exception) {
                                throw new RuntimeException("Cannot put task into outbound queue", exception);
                            }
                        }
                        throw throwable2;
                    }
                    try {
                        this.outbound.put(webcamTask);
                    }
                    catch (InterruptedException interruptedException) {
                        return;
                    }
                    catch (Exception exception) {
                        throw new RuntimeException("Cannot put task into outbound queue", exception);
                    }
                    continue;
                }
                break;
            }
        }
    }

    static final class ProcessorThreadFactory
    implements ThreadFactory {
        private ProcessorThreadFactory() {
        }

        @Override
        public final Thread newThread(Runnable runnable2) {
            runnable2 = new ProcessorThread(runnable2);
            ((Thread)runnable2).setUncaughtExceptionHandler(WebcamExceptionHandler.getInstance());
            ((Thread)runnable2).setDaemon(true);
            return runnable2;
        }
    }

    public static final class ProcessorThread
    extends Thread {
        private static final AtomicInteger N = new AtomicInteger(0);

        public ProcessorThread(Runnable runnable2) {
            super(runnable2, String.format("atomic-processor-%d", N.incrementAndGet()));
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.github.sarxos.webcam;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamDevice;
import com.github.sarxos.webcam.WebcamDriver;
import com.github.sarxos.webcam.WebcamException;
import com.github.sarxos.webcam.WebcamExceptionHandler;
import com.github.sarxos.webcam.ds.cgt.WebcamGetImageTask;
import java.awt.image.BufferedImage;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebcamUpdater
implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(WebcamUpdater.class);
    private static final int TARGET_FPS = 50;
    private static final UpdaterThreadFactory THREAD_FACTORY = new UpdaterThreadFactory();
    private ScheduledExecutorService executor = null;
    private final AtomicReference<BufferedImage> image = new AtomicReference();
    private Webcam webcam = null;
    private volatile double fps = 0.0;
    private AtomicBoolean running = new AtomicBoolean(false);
    private volatile boolean imageNew = false;
    private final DelayCalculator delayCalculator;

    protected WebcamUpdater(Webcam webcam) {
        this(webcam, new DefaultDelayCalculator());
    }

    public WebcamUpdater(Webcam webcam, DelayCalculator delayCalculator) {
        this.webcam = webcam;
        if (delayCalculator == null) {
            this.delayCalculator = new DefaultDelayCalculator();
            return;
        }
        this.delayCalculator = delayCalculator;
    }

    public void start() {
        if (this.running.compareAndSet(false, true)) {
            this.image.set(new WebcamGetImageTask(Webcam.getDriver(), this.webcam.getDevice()).getImage());
            this.executor = Executors.newSingleThreadScheduledExecutor(THREAD_FACTORY);
            this.executor.execute(this);
            LOG.debug("Webcam updater has been started");
            return;
        }
        LOG.debug("Webcam updater is already started");
    }

    public void stop() {
        if (this.running.compareAndSet(true, false)) {
            this.executor.shutdown();
            while (!this.executor.isTerminated()) {
                try {
                    this.executor.awaitTermination(100L, TimeUnit.MILLISECONDS);
                }
                catch (InterruptedException interruptedException) {
                    return;
                }
            }
            LOG.debug("Webcam updater has been stopped");
            return;
        }
        LOG.debug("Webcam updater is already stopped");
    }

    @Override
    public void run() {
        if (!this.running.get()) {
            return;
        }
        try {
            this.tick();
            return;
        }
        catch (Throwable throwable) {
            Throwable throwable2 = throwable;
            WebcamExceptionHandler.handle(throwable);
            return;
        }
    }

    private void tick() {
        if (!this.webcam.isOpen()) {
            return;
        }
        WebcamDriver webcamDriver = Webcam.getDriver();
        WebcamDevice webcamDevice = this.webcam.getDevice();
        assert (webcamDriver != null);
        assert (webcamDevice != null);
        boolean bl = false;
        long l2 = System.currentTimeMillis();
        try {
            this.image.set(this.webcam.transform(new WebcamGetImageTask(webcamDriver, webcamDevice).getImage()));
            this.imageNew = true;
            bl = true;
        }
        catch (WebcamException webcamException) {
            WebcamException webcamException2 = webcamException;
            WebcamExceptionHandler.handle(webcamException);
        }
        long l3 = System.currentTimeMillis();
        double d2 = -1.0;
        if (webcamDevice instanceof WebcamDevice.FPSSource) {
            d2 = ((WebcamDevice.FPSSource)((Object)webcamDevice)).getFPS();
        }
        long l4 = l3 - l2;
        long l5 = this.delayCalculator.calculateDelay(l4, d2);
        long l6 = l4 + 1L;
        this.fps = d2 >= 0.0 ? d2 : (4.0 * this.fps + (double)(1000L / l6)) / 5.0;
        if (this.webcam.isOpen()) {
            try {
                this.executor.schedule(this, l5, TimeUnit.MILLISECONDS);
            }
            catch (RejectedExecutionException rejectedExecutionException) {
                LOG.trace("Webcam update has been rejected", rejectedExecutionException);
            }
        }
        if (bl) {
            this.webcam.notifyWebcamImageAcquired(this.image.get());
        }
    }

    public BufferedImage getImage() {
        int n2 = 0;
        while (this.image.get() == null) {
            try {
                Thread.sleep(100L);
            }
            catch (InterruptedException interruptedException) {
                throw new RuntimeException(interruptedException);
            }
            if (n2++ <= 100) continue;
            LOG.error("Image has not been found for more than 10 seconds");
            return null;
        }
        this.imageNew = false;
        return this.image.get();
    }

    protected boolean isImageNew() {
        return this.imageNew;
    }

    public double getFPS() {
        return this.fps;
    }

    static final class UpdaterThreadFactory
    implements ThreadFactory {
        private static final AtomicInteger number = new AtomicInteger(0);

        private UpdaterThreadFactory() {
        }

        @Override
        public final Thread newThread(Runnable runnable2) {
            runnable2 = new Thread(runnable2, String.format("webcam-updater-thread-%d", number.incrementAndGet()));
            ((Thread)runnable2).setUncaughtExceptionHandler(WebcamExceptionHandler.getInstance());
            ((Thread)runnable2).setDaemon(true);
            return runnable2;
        }
    }

    public static class DefaultDelayCalculator
    implements DelayCalculator {
        @Override
        public long calculateDelay(long l2, double d2) {
            long l3 = Math.max(20L - l2, 0L);
            return l3;
        }
    }

    public static interface DelayCalculator {
        public long calculateDelay(long var1, double var3);
    }
}


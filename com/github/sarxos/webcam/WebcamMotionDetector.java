/*
 * Decompiled with CFR 0.152.
 */
package com.github.sarxos.webcam;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamExceptionHandler;
import com.github.sarxos.webcam.WebcamMotionDetectorAlgorithm;
import com.github.sarxos.webcam.WebcamMotionDetectorDefaultAlgorithm;
import com.github.sarxos.webcam.WebcamMotionEvent;
import com.github.sarxos.webcam.WebcamMotionListener;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebcamMotionDetector {
    private static final Logger LOG = LoggerFactory.getLogger(WebcamMotionDetector.class);
    private static final AtomicInteger NT = new AtomicInteger(0);
    private static final ThreadFactory THREAD_FACTORY = new DetectorThreadFactory();
    public static final int DEFAULT_INTERVAL = 500;
    private final ExecutorService executor = Executors.newFixedThreadPool(2, THREAD_FACTORY);
    private final List<WebcamMotionListener> listeners = new ArrayList<WebcamMotionListener>();
    private final AtomicBoolean running = new AtomicBoolean(false);
    private volatile boolean motion = false;
    private BufferedImage previousOriginal = null;
    private BufferedImage previousFiltered = null;
    private Webcam webcam = null;
    private volatile int interval = 500;
    private volatile int inertia = -1;
    private volatile long lastMotionTimestamp = 0L;
    private final WebcamMotionDetectorAlgorithm algorithm;

    public WebcamMotionDetector(Webcam webcam, WebcamMotionDetectorAlgorithm webcamMotionDetectorAlgorithm, int n2) {
        this.webcam = webcam;
        this.algorithm = webcamMotionDetectorAlgorithm;
        this.setInterval(n2);
    }

    public WebcamMotionDetector(Webcam webcam, int n2, double d2, int n3) {
        this(webcam, new WebcamMotionDetectorDefaultAlgorithm(n2, d2), n3);
    }

    public WebcamMotionDetector(Webcam webcam, int n2, double d2) {
        this(webcam, n2, d2, 500);
    }

    public WebcamMotionDetector(Webcam webcam, int n2) {
        this(webcam, n2, 0.2);
    }

    public WebcamMotionDetector(Webcam webcam) {
        this(webcam, 25);
    }

    public void start() {
        if (this.running.compareAndSet(false, true)) {
            this.webcam.open();
            this.executor.submit(new Runner());
            this.executor.submit(new Inverter());
        }
    }

    public void stop() {
        if (this.running.compareAndSet(true, false)) {
            this.webcam.close();
            this.executor.shutdownNow();
        }
    }

    protected void detect() {
        if (!this.webcam.isOpen()) {
            this.motion = false;
            return;
        }
        BufferedImage bufferedImage = this.webcam.getImage();
        if (bufferedImage == null) {
            this.motion = false;
            return;
        }
        BufferedImage bufferedImage2 = this.algorithm.filter(bufferedImage);
        boolean bl = this.algorithm.detect(this.previousFiltered, bufferedImage2);
        if (bl) {
            this.motion = true;
            this.lastMotionTimestamp = System.currentTimeMillis();
            this.notifyMotionListeners(bufferedImage);
        }
        this.previousOriginal = bufferedImage;
        this.previousFiltered = bufferedImage2;
    }

    private void notifyMotionListeners(BufferedImage object) {
        WebcamMotionDetector webcamMotionDetector = this;
        object = new WebcamMotionEvent(webcamMotionDetector, webcamMotionDetector.previousOriginal, (BufferedImage)object, this.algorithm.getArea(), this.algorithm.getCog(), this.algorithm.getPoints());
        for (WebcamMotionListener webcamMotionListener : this.listeners) {
            try {
                webcamMotionListener.motionDetected((WebcamMotionEvent)object);
            }
            catch (Exception exception) {
                Exception exception2 = exception;
                WebcamExceptionHandler.handle(exception);
            }
        }
    }

    public boolean addMotionListener(WebcamMotionListener webcamMotionListener) {
        return this.listeners.add(webcamMotionListener);
    }

    public WebcamMotionListener[] getMotionListeners() {
        return this.listeners.toArray(new WebcamMotionListener[this.listeners.size()]);
    }

    public boolean removeMotionListener(WebcamMotionListener webcamMotionListener) {
        return this.listeners.remove(webcamMotionListener);
    }

    public int getInterval() {
        return this.interval;
    }

    public void setInterval(int n2) {
        if (n2 < 100) {
            throw new IllegalArgumentException("Motion check interval cannot be less than 100 ms");
        }
        this.interval = n2;
    }

    public void setPixelThreshold(int n2) {
        ((WebcamMotionDetectorDefaultAlgorithm)this.algorithm).setPixelThreshold(n2);
    }

    public void setAreaThreshold(double d2) {
        ((WebcamMotionDetectorDefaultAlgorithm)this.algorithm).setAreaThreshold(d2);
    }

    public void setInertia(int n2) {
        if (n2 < 0) {
            throw new IllegalArgumentException("Inertia time must not be negative!");
        }
        this.inertia = n2;
    }

    public void clearInertia() {
        this.inertia = -1;
    }

    public Webcam getWebcam() {
        return this.webcam;
    }

    public boolean isMotion() {
        if (!this.running.get()) {
            LOG.warn("Motion cannot be detected when detector is not running!");
        }
        return this.motion;
    }

    public double getMotionArea() {
        return this.algorithm.getArea();
    }

    public Point getMotionCog() {
        Point point = this.algorithm.getCog();
        if (point == null) {
            int n2 = this.webcam.getViewSize().width;
            int n3 = this.webcam.getViewSize().height;
            point = new Point(n2 / 2, n3 / 2);
        }
        return point;
    }

    public WebcamMotionDetectorAlgorithm getDetectorAlgorithm() {
        return this.algorithm;
    }

    public void setMaxMotionPoints(int n2) {
        this.algorithm.setMaxPoints(n2);
    }

    public int getMaxMotionPoints() {
        return this.algorithm.getMaxPoints();
    }

    public void setPointRange(int n2) {
        this.algorithm.setPointRange(n2);
    }

    public int getPointRange() {
        return this.algorithm.getPointRange();
    }

    class Inverter
    implements Runnable {
        private Inverter() {
        }

        @Override
        public void run() {
            while (WebcamMotionDetector.this.running.get()) {
                int n2;
                try {
                    Thread.sleep(10L);
                }
                catch (InterruptedException interruptedException) {
                    return;
                }
                int n3 = n2 = WebcamMotionDetector.this.inertia != -1 ? WebcamMotionDetector.this.inertia : 2 * WebcamMotionDetector.this.interval;
                if (WebcamMotionDetector.this.lastMotionTimestamp + (long)n2 >= System.currentTimeMillis()) continue;
                WebcamMotionDetector.this.motion = false;
            }
        }
    }

    class Runner
    implements Runnable {
        private Runner() {
        }

        @Override
        public void run() {
            WebcamMotionDetector.this.running.set(true);
            while (WebcamMotionDetector.this.running.get() && WebcamMotionDetector.this.webcam.isOpen()) {
                try {
                    WebcamMotionDetector.this.detect();
                    Thread.sleep(WebcamMotionDetector.this.interval);
                }
                catch (InterruptedException interruptedException) {
                    break;
                }
                catch (Exception exception) {
                    Exception exception2 = exception;
                    WebcamExceptionHandler.handle(exception);
                }
            }
            WebcamMotionDetector.this.running.set(false);
        }
    }

    static final class DetectorThreadFactory
    implements ThreadFactory {
        private DetectorThreadFactory() {
        }

        @Override
        public final Thread newThread(Runnable runnable2) {
            runnable2 = new Thread(runnable2, String.format("motion-detector-%d", NT.incrementAndGet()));
            ((Thread)runnable2).setUncaughtExceptionHandler(WebcamExceptionHandler.getInstance());
            ((Thread)runnable2).setDaemon(true);
            return runnable2;
        }
    }
}


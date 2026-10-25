/*
 * Decompiled with CFR 0.152.
 */
package com.github.sarxos.webcam;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamDeallocator;
import com.github.sarxos.webcam.WebcamDevice;
import com.github.sarxos.webcam.WebcamDiscoveryEvent;
import com.github.sarxos.webcam.WebcamDiscoveryListener;
import com.github.sarxos.webcam.WebcamDiscoverySupport;
import com.github.sarxos.webcam.WebcamDriver;
import com.github.sarxos.webcam.WebcamException;
import com.github.sarxos.webcam.WebcamExceptionHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebcamDiscoveryService
implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(WebcamDiscoveryService.class);
    private final WebcamDriver driver;
    private final WebcamDiscoverySupport support;
    private volatile List<Webcam> webcams = null;
    private AtomicBoolean running = new AtomicBoolean(false);
    private AtomicBoolean enabled = new AtomicBoolean(true);
    private Thread runner = null;

    protected WebcamDiscoveryService(WebcamDriver webcamDriver) {
        if (webcamDriver == null) {
            throw new IllegalArgumentException("Driver cannot be null!");
        }
        this.driver = webcamDriver;
        this.support = (WebcamDiscoverySupport)((Object)(webcamDriver instanceof WebcamDiscoverySupport ? webcamDriver : null));
    }

    private static List<Webcam> toWebcams(List<WebcamDevice> object) {
        ArrayList<Webcam> arrayList = new ArrayList<Webcam>();
        object = object.iterator();
        while (object.hasNext()) {
            WebcamDevice webcamDevice = (WebcamDevice)object.next();
            arrayList.add(new Webcam(webcamDevice));
        }
        return arrayList;
    }

    private static List<WebcamDevice> getDevices(List<Webcam> object) {
        ArrayList<WebcamDevice> arrayList = new ArrayList<WebcamDevice>();
        object = object.iterator();
        while (object.hasNext()) {
            Webcam webcam = (Webcam)object.next();
            arrayList.add(webcam.getDevice());
        }
        return arrayList;
    }

    public List<Webcam> getWebcams(long l2, TimeUnit timeUnit) throws TimeoutException {
        if (l2 < 0L) {
            throw new IllegalArgumentException("Timeout cannot be negative");
        }
        if (timeUnit == null) {
            throw new IllegalArgumentException("Time unit cannot be null!");
        }
        Object object = null;
        WebcamDiscoveryListener[] webcamDiscoveryListenerArray = Webcam.class;
        synchronized (Webcam.class) {
            Object object2;
            if (this.webcams == null) {
                object = new WebcamsDiscovery(this.driver);
                object2 = Executors.newSingleThreadExecutor((ThreadFactory)object);
                object = object2.submit(object);
                object2.shutdown();
                try {
                    object2.awaitTermination(l2, timeUnit);
                    if (object.isDone()) {
                        this.webcams = (List)object.get();
                    } else {
                        object.cancel(true);
                    }
                }
                catch (InterruptedException interruptedException) {
                    throw new RuntimeException(interruptedException);
                }
                catch (ExecutionException executionException) {
                    throw new WebcamException(executionException);
                }
                if (this.webcams == null) {
                    throw new TimeoutException(String.format("Webcams discovery timeout (%d ms) has been exceeded", l2));
                }
                object = new ArrayList<Webcam>(this.webcams);
                if (Webcam.isHandleTermSignal()) {
                    WebcamDeallocator.store(this.webcams.toArray(new Webcam[this.webcams.size()]));
                }
            }
            // ** MonitorExit[var5_7] (shouldn't be in output)
            if (object != null) {
                webcamDiscoveryListenerArray = Webcam.getDiscoveryListeners();
                object = object.iterator();
                while (object.hasNext()) {
                    object2 = (Webcam)object.next();
                    WebcamDiscoveryService.notifyWebcamFound((Webcam)object2, webcamDiscoveryListenerArray);
                }
            }
            return Collections.unmodifiableList(this.webcams);
        }
    }

    public void scan() {
        Object object2;
        List<WebcamDevice> object22;
        WebcamDiscoveryListener[] webcamDiscoveryListenerArray = Webcam.getDiscoveryListeners();
        List<WebcamDevice> list2 = this.driver.getDevices();
        try {
            object22 = WebcamDiscoveryService.getDevices(this.getWebcams(Long.MAX_VALUE, TimeUnit.MILLISECONDS));
        }
        catch (TimeoutException timeoutException) {
            throw new WebcamException(timeoutException);
        }
        LinkedList<WebcamDevice> linkedList = new LinkedList<WebcamDevice>(object22);
        LinkedList<WebcamDevice> linkedList2 = new LinkedList<WebcamDevice>(list2);
        Iterator<Webcam> iterator2 = linkedList.iterator();
        block2: while (iterator2.hasNext()) {
            WebcamDevice webcamDevice = (WebcamDevice)iterator2.next();
            object2 = linkedList2.iterator();
            while (object2.hasNext()) {
                WebcamDevice webcamDevice2 = (WebcamDevice)object2.next();
                if (!webcamDevice2.getName().equals(webcamDevice.getName())) continue;
                object2.remove();
                iterator2.remove();
                continue block2;
            }
        }
        if (linkedList.size() > 0) {
            iterator2 = new ArrayList();
            block4: for (Object object2 : linkedList) {
                for (Webcam webcam : this.webcams) {
                    if (!webcam.getDevice().getName().equals(object2.getName())) continue;
                    iterator2.add(webcam);
                    continue block4;
                }
            }
            this.setCurrentWebcams(list2);
            Iterator iterator3 = iterator2.iterator();
            while (iterator3.hasNext()) {
                object2 = (Webcam)iterator3.next();
                WebcamDiscoveryService.notifyWebcamGone((Webcam)object2, webcamDiscoveryListenerArray);
                ((Webcam)object2).dispose();
            }
        }
        if (linkedList2.size() > 0) {
            this.setCurrentWebcams(list2);
            block7: for (WebcamDevice webcamDevice : linkedList2) {
                for (Webcam webcam : this.webcams) {
                    if (!webcam.getDevice().getName().equals(webcamDevice.getName())) continue;
                    WebcamDiscoveryService.notifyWebcamFound(webcam, webcamDiscoveryListenerArray);
                    continue block7;
                }
            }
        }
    }

    @Override
    public void run() {
        if (this.support == null) {
            return;
        }
        if (!this.support.isScanPossible()) {
            return;
        }
        Object object = new Object();
        do {
            Object object2 = object;
            synchronized (object2) {
                try {
                    object.wait(this.support.getScanInterval());
                }
                catch (InterruptedException interruptedException) {
                    break;
                }
                catch (Exception exception) {
                    throw new RuntimeException("Problem waiting on monitor", exception);
                }
            }
            this.scan();
        } while (this.running.get());
        LOG.debug("Webcam discovery service loop has been stopped");
    }

    private void setCurrentWebcams(List<WebcamDevice> list2) {
        this.webcams = WebcamDiscoveryService.toWebcams(list2);
        if (Webcam.isHandleTermSignal()) {
            WebcamDeallocator.unstore();
            WebcamDeallocator.store(this.webcams.toArray(new Webcam[this.webcams.size()]));
        }
    }

    private static void notifyWebcamGone(Webcam object, WebcamDiscoveryListener[] webcamDiscoveryListenerArray) {
        object = new WebcamDiscoveryEvent((Webcam)object, 2);
        for (WebcamDiscoveryListener webcamDiscoveryListener : webcamDiscoveryListenerArray) {
            try {
                webcamDiscoveryListener.webcamGone((WebcamDiscoveryEvent)object);
            }
            catch (Exception exception) {
                LOG.error(String.format("Webcam gone, exception when calling listener %s", webcamDiscoveryListener.getClass()), exception);
            }
        }
    }

    private static void notifyWebcamFound(Webcam object, WebcamDiscoveryListener[] webcamDiscoveryListenerArray) {
        object = new WebcamDiscoveryEvent((Webcam)object, 1);
        for (WebcamDiscoveryListener webcamDiscoveryListener : webcamDiscoveryListenerArray) {
            try {
                webcamDiscoveryListener.webcamFound((WebcamDiscoveryEvent)object);
            }
            catch (Exception exception) {
                LOG.error(String.format("Webcam found, exception when calling listener %s", webcamDiscoveryListener.getClass()), exception);
            }
        }
    }

    public void stop() {
        if (!this.running.compareAndSet(true, false)) {
            return;
        }
        try {
            this.runner.join();
        }
        catch (InterruptedException interruptedException) {
            throw new WebcamException("Joint interrupted");
        }
        LOG.debug("Discovery service has been stopped");
        this.runner = null;
    }

    public void start() {
        if (!this.enabled.get()) {
            LOG.info("Discovery service has been disabled and thus it will not be started");
            return;
        }
        if (this.support == null) {
            LOG.info("Discovery will not run - driver {} does not support this feature", (Object)this.driver.getClass().getSimpleName());
            return;
        }
        if (!this.running.compareAndSet(false, true)) {
            return;
        }
        this.runner = new Thread((Runnable)this, "webcam-discovery-service");
        this.runner.setUncaughtExceptionHandler(WebcamExceptionHandler.getInstance());
        this.runner.setDaemon(true);
        this.runner.start();
    }

    public boolean isRunning() {
        return this.running.get();
    }

    public void setEnabled(boolean bl) {
        this.enabled.set(bl);
    }

    protected void shutdown() {
        this.stop();
        if (this.webcams == null) {
            return;
        }
        for (Webcam object2 : this.webcams) {
            object2.dispose();
        }
        Class<Webcam> clazz = Webcam.class;
        synchronized (Webcam.class) {
            this.webcams.clear();
            if (Webcam.isHandleTermSignal()) {
                WebcamDeallocator.unstore();
            }
            // ** MonitorExit[var2_4] (shouldn't be in output)
            return;
        }
    }

    static final class WebcamsDiscovery
    implements Callable<List<Webcam>>,
    ThreadFactory {
        private final WebcamDriver driver;

        public WebcamsDiscovery(WebcamDriver webcamDriver) {
            this.driver = webcamDriver;
        }

        @Override
        public final List<Webcam> call() throws Exception {
            return WebcamDiscoveryService.toWebcams(this.driver.getDevices());
        }

        @Override
        public final Thread newThread(Runnable runnable2) {
            runnable2 = new Thread(runnable2, "webcam-discovery-service");
            ((Thread)runnable2).setDaemon(true);
            ((Thread)runnable2).setUncaughtExceptionHandler(WebcamExceptionHandler.getInstance());
            return runnable2;
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.github.sarxos.webcam;

import com.github.sarxos.webcam.WebcamDevice;
import com.github.sarxos.webcam.WebcamDiscoveryListener;
import com.github.sarxos.webcam.WebcamDiscoveryService;
import com.github.sarxos.webcam.WebcamDriver;
import com.github.sarxos.webcam.WebcamDriverUtils;
import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamEventType;
import com.github.sarxos.webcam.WebcamException;
import com.github.sarxos.webcam.WebcamExceptionHandler;
import com.github.sarxos.webcam.WebcamImageTransformer;
import com.github.sarxos.webcam.WebcamListener;
import com.github.sarxos.webcam.WebcamLock;
import com.github.sarxos.webcam.WebcamProcessor;
import com.github.sarxos.webcam.WebcamShutdownHook;
import com.github.sarxos.webcam.WebcamUpdater;
import com.github.sarxos.webcam.ds.buildin.WebcamDefaultDriver;
import com.github.sarxos.webcam.ds.cgt.WebcamCloseTask;
import com.github.sarxos.webcam.ds.cgt.WebcamDisposeTask;
import com.github.sarxos.webcam.ds.cgt.WebcamGetBufferTask;
import com.github.sarxos.webcam.ds.cgt.WebcamGetImageTask;
import com.github.sarxos.webcam.ds.cgt.WebcamOpenTask;
import com.github.sarxos.webcam.ds.cgt.WebcamReadBufferTask;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Webcam {
    private static final Logger LOG = LoggerFactory.getLogger(Webcam.class);
    private static final List<String> DRIVERS_LIST = new ArrayList<String>();
    private static final List<Class<?>> DRIVERS_CLASS_LIST = new ArrayList();
    private static final List<WebcamDiscoveryListener> DISCOVERY_LISTENERS = Collections.synchronizedList(new ArrayList());
    private static volatile WebcamDriver driver = null;
    private static volatile WebcamDiscoveryService discovery = null;
    private static boolean deallocOnTermSignal = false;
    private static boolean autoOpen = false;
    private List<WebcamListener> listeners = new CopyOnWriteArrayList<WebcamListener>();
    private List<Dimension> customSizes = new ArrayList<Dimension>();
    private WebcamShutdownHook hook = null;
    private WebcamDevice device = null;
    private AtomicBoolean open = new AtomicBoolean(false);
    private AtomicBoolean disposed = new AtomicBoolean(false);
    private volatile boolean asynchronous = false;
    private volatile double fps = 0.0;
    private volatile WebcamUpdater updater = null;
    private volatile WebcamImageTransformer transformer = null;
    private WebcamLock lock = null;
    private ExecutorService notificator = null;

    protected Webcam(WebcamDevice webcamDevice) {
        if (webcamDevice == null) {
            throw new IllegalArgumentException("Webcam device cannot be null");
        }
        this.device = webcamDevice;
        this.lock = new WebcamLock(this);
    }

    protected void notifyWebcamImageAcquired(BufferedImage bufferedImage) {
        if (this.getWebcamListenersCount() > 0) {
            this.notificator.execute(new ImageNotification(this, bufferedImage));
        }
    }

    public boolean open() {
        return this.open(false);
    }

    public boolean open(boolean bl) {
        return this.open(bl, new WebcamUpdater.DefaultDelayCalculator());
    }

    public boolean open(boolean bl, WebcamUpdater.DelayCalculator object) {
        if (this.open.compareAndSet(false, true)) {
            assert (this.lock != null);
            this.notificator = Executors.newSingleThreadExecutor(new NotificationThreadFactory());
            this.lock.lock();
            WebcamOpenTask object22 = new WebcamOpenTask(driver, this.device);
            try {
                object22.open();
            }
            catch (InterruptedException interruptedException) {
                this.lock.unlock();
                this.open.set(false);
                LOG.debug("Thread has been interrupted in the middle of webcam opening process!", interruptedException);
                return false;
            }
            catch (WebcamException webcamException) {
                this.lock.unlock();
                this.open.set(false);
                LOG.debug("Webcam exception when opening", webcamException);
                throw webcamException;
            }
            LOG.debug("Webcam is now open {}", (Object)this.getName());
            try {
                this.hook = new WebcamShutdownHook(this);
                Runtime.getRuntime().addShutdownHook(this.hook);
            }
            catch (IllegalStateException illegalStateException) {
                LOG.debug("Shutdown in progress, do not open device");
                LOG.trace(illegalStateException.getMessage(), illegalStateException);
                this.close();
                return false;
            }
            this.asynchronous = bl;
            if (this.asynchronous) {
                if (this.updater == null) {
                    this.updater = new WebcamUpdater(this, (WebcamUpdater.DelayCalculator)((Object)object));
                }
                this.updater.start();
            }
            WebcamEvent webcamEvent = new WebcamEvent(WebcamEventType.OPEN, this);
            for (WebcamListener webcamListener : this.listeners) {
                try {
                    webcamListener.webcamOpen(webcamEvent);
                }
                catch (Exception exception) {
                    LOG.error(String.format("Notify webcam open, exception when calling listener %s", webcamListener.getClass()), exception);
                }
            }
        } else {
            LOG.debug("Webcam is already open {}", (Object)this.getName());
        }
        return true;
    }

    public boolean close() {
        if (this.open.compareAndSet(true, false)) {
            LOG.debug("Closing webcam {}", (Object)this.getName());
            assert (this.lock != null);
            Object object = new WebcamCloseTask(driver, this.device);
            try {
                ((WebcamCloseTask)object).close();
            }
            catch (InterruptedException interruptedException) {
                this.open.set(true);
                LOG.debug("Thread has been interrupted before webcam was closed!", interruptedException);
                return false;
            }
            catch (WebcamException webcamException) {
                this.open.set(true);
                throw webcamException;
            }
            if (this.asynchronous) {
                this.updater.stop();
            }
            this.removeShutdownHook();
            this.lock.unlock();
            object = new WebcamEvent(WebcamEventType.CLOSED, this);
            for (WebcamListener webcamListener : this.listeners) {
                try {
                    webcamListener.webcamClosed((WebcamEvent)object);
                }
                catch (Exception exception) {
                    LOG.error(String.format("Notify webcam closed, exception when calling %s listener", webcamListener.getClass()), exception);
                }
            }
            this.notificator.shutdown();
            while (!this.notificator.isTerminated()) {
                try {
                    this.notificator.awaitTermination(100L, TimeUnit.MILLISECONDS);
                }
                catch (InterruptedException interruptedException) {
                    return false;
                }
            }
            LOG.debug("Webcam {} has been closed", (Object)this.getName());
        } else {
            LOG.debug("Webcam {} is already closed", (Object)this.getName());
        }
        return true;
    }

    public WebcamDevice getDevice() {
        assert (this.device != null);
        return this.device;
    }

    protected void dispose() {
        assert (this.disposed != null);
        assert (this.open != null);
        assert (driver != null);
        assert (this.device != null);
        assert (this.listeners != null);
        if (!this.disposed.compareAndSet(false, true)) {
            return;
        }
        this.open.set(false);
        LOG.info("Disposing webcam {}", (Object)this.getName());
        Object object = new WebcamDisposeTask(driver, this.device);
        try {
            ((WebcamDisposeTask)object).dispose();
        }
        catch (InterruptedException interruptedException) {
            LOG.error("Processor has been interrupted before webcam was disposed!", interruptedException);
            return;
        }
        object = new WebcamEvent(WebcamEventType.DISPOSED, this);
        for (WebcamListener webcamListener : this.listeners) {
            try {
                webcamListener.webcamClosed((WebcamEvent)object);
                webcamListener.webcamDisposed((WebcamEvent)object);
            }
            catch (Exception exception) {
                LOG.error(String.format("Notify webcam disposed, exception when calling %s listener", webcamListener.getClass()), exception);
            }
        }
        this.removeShutdownHook();
        LOG.debug("Webcam disposed {}", (Object)this.getName());
    }

    private void removeShutdownHook() {
        if (this.hook != null) {
            try {
                Runtime.getRuntime().removeShutdownHook(this.hook);
                return;
            }
            catch (IllegalStateException illegalStateException) {
                LOG.trace("Shutdown in progress, cannot remove hook");
            }
        }
    }

    protected BufferedImage transform(BufferedImage bufferedImage) {
        WebcamImageTransformer webcamImageTransformer;
        if (bufferedImage != null && (webcamImageTransformer = this.getImageTransformer()) != null) {
            return webcamImageTransformer.transform(bufferedImage);
        }
        return bufferedImage;
    }

    public boolean isOpen() {
        return this.open.get();
    }

    public Dimension getViewSize() {
        return this.device.getResolution();
    }

    public Dimension[] getViewSizes() {
        return this.device.getResolutions();
    }

    public void setCustomViewSizes(Dimension ... dimensionArray) {
        assert (this.customSizes != null);
        if (dimensionArray == null) {
            this.customSizes.clear();
            return;
        }
        this.customSizes = Arrays.asList(dimensionArray);
    }

    public Dimension[] getCustomViewSizes() {
        assert (this.customSizes != null);
        return this.customSizes.toArray(new Dimension[this.customSizes.size()]);
    }

    public void setViewSize(Dimension dimension) {
        Dimension dimension2;
        int n2;
        if (dimension == null) {
            throw new IllegalArgumentException("Resolution cannot be null!");
        }
        if (this.open.get()) {
            throw new IllegalStateException("Cannot change resolution when webcam is open, please close it first");
        }
        Dimension[] dimensionArray = this.getViewSize();
        if (dimensionArray != null && dimensionArray.width == dimension.width && dimensionArray.height == dimension.height) {
            return;
        }
        dimensionArray = this.getViewSizes();
        Dimension[] dimensionArray2 = this.getCustomViewSizes();
        assert (dimensionArray != null);
        assert (dimensionArray2 != null);
        boolean bl = false;
        Object object = dimensionArray;
        int n3 = dimensionArray.length;
        for (n2 = 0; n2 < n3; ++n2) {
            dimension2 = object[n2];
            if (dimension2.width != dimension.width || dimension2.height != dimension.height) continue;
            bl = true;
            break;
        }
        if (!bl) {
            object = dimensionArray2;
            n3 = dimensionArray2.length;
            for (n2 = 0; n2 < n3; ++n2) {
                dimension2 = object[n2];
                if (dimension2.width != dimension.width || dimension2.height != dimension.height) continue;
                bl = true;
                break;
            }
        }
        if (!bl) {
            int n4;
            object = new StringBuilder("Incorrect dimension [");
            ((StringBuilder)object).append(dimension.width).append("x").append(dimension.height).append("] ");
            ((StringBuilder)object).append("possible ones are ");
            Dimension[] dimensionArray3 = dimensionArray;
            n2 = dimensionArray.length;
            for (n4 = 0; n4 < n2; ++n4) {
                dimension = dimensionArray3[n4];
                ((StringBuilder)object).append("[").append(dimension.width).append("x").append(dimension.height).append("] ");
            }
            dimensionArray3 = dimensionArray2;
            n2 = dimensionArray2.length;
            for (n4 = 0; n4 < n2; ++n4) {
                dimension = dimensionArray3[n4];
                ((StringBuilder)object).append("[").append(dimension.width).append("x").append(dimension.height).append("] ");
            }
            throw new IllegalArgumentException(((StringBuilder)object).toString());
        }
        LOG.debug("Setting new resolution {}x{}", (Object)dimension.width, (Object)dimension.height);
        this.device.setResolution(dimension);
    }

    public BufferedImage getImage() {
        if (!this.isReady()) {
            return null;
        }
        if (this.asynchronous) {
            return this.updater.getImage();
        }
        long l2 = System.currentTimeMillis();
        BufferedImage bufferedImage = this.transform(new WebcamGetImageTask(driver, this.device).getImage());
        long l3 = System.currentTimeMillis();
        if (bufferedImage == null) {
            return null;
        }
        this.fps = this.device instanceof WebcamDevice.FPSSource ? ((WebcamDevice.FPSSource)((Object)this.device)).getFPS() : (4.0 * this.fps + (double)(1000L / (l3 - l2 + 1L))) / 5.0;
        this.notifyWebcamImageAcquired(bufferedImage);
        return bufferedImage;
    }

    public boolean isImageNew() {
        if (this.asynchronous) {
            return this.updater.isImageNew();
        }
        return true;
    }

    public double getFPS() {
        if (this.asynchronous) {
            return this.updater.getFPS();
        }
        return this.fps;
    }

    public ByteBuffer getImageBytes() {
        if (!this.isReady()) {
            return null;
        }
        assert (driver != null);
        assert (this.device != null);
        if (this.device instanceof WebcamDevice.BufferAccess) {
            long l2 = System.currentTimeMillis();
            try {
                ByteBuffer byteBuffer = new WebcamGetBufferTask(driver, this.device).getBuffer();
                return byteBuffer;
            }
            finally {
                long l3 = System.currentTimeMillis();
                this.fps = this.device instanceof WebcamDevice.FPSSource ? ((WebcamDevice.FPSSource)((Object)this.device)).getFPS() : (4.0 * this.fps + (double)(1000L / (l3 - l2 + 1L))) / 5.0;
            }
        }
        throw new IllegalStateException(String.format("Driver %s does not support buffer access", driver.getClass().getName()));
    }

    public void getImageBytes(ByteBuffer byteBuffer) {
        if (!this.isReady()) {
            return;
        }
        assert (driver != null);
        assert (this.device != null);
        if (this.device instanceof WebcamDevice.BufferAccess) {
            long l2 = System.currentTimeMillis();
            try {
                new WebcamReadBufferTask(driver, this.device, byteBuffer).readBuffer();
            }
            finally {
                long l3 = System.currentTimeMillis();
                this.fps = this.device instanceof WebcamDevice.FPSSource ? ((WebcamDevice.FPSSource)((Object)this.device)).getFPS() : (4.0 * this.fps + (double)(1000L / (l3 - l2 + 1L))) / 5.0;
            }
        } else {
            throw new IllegalStateException(String.format("Driver %s does not support buffer access", driver.getClass().getName()));
        }
    }

    public void setParameters(Map<String, ?> map) {
        WebcamDevice webcamDevice = this.getDevice();
        if (webcamDevice instanceof WebcamDevice.Configurable) {
            ((WebcamDevice.Configurable)((Object)webcamDevice)).setParameters(map);
            return;
        }
        LOG.debug("Webcam device {} is not configurable", (Object)webcamDevice);
    }

    private boolean isReady() {
        assert (this.disposed != null);
        assert (this.open != null);
        if (this.disposed.get()) {
            LOG.warn("Cannot get image, webcam has been already disposed");
            return false;
        }
        if (!this.open.get()) {
            if (autoOpen) {
                this.open();
            } else {
                return false;
            }
        }
        return true;
    }

    public static List<Webcam> getWebcams() throws WebcamException {
        try {
            return Webcam.getWebcams(Long.MAX_VALUE);
        }
        catch (TimeoutException timeoutException) {
            throw new RuntimeException(timeoutException);
        }
    }

    public static List<Webcam> getWebcams(long l2) throws TimeoutException, WebcamException {
        if (l2 < 0L) {
            throw new IllegalArgumentException(String.format("Timeout cannot be negative (%d)", l2));
        }
        return Webcam.getWebcams(l2, TimeUnit.MILLISECONDS);
    }

    public static synchronized List<Webcam> getWebcams(long l2, TimeUnit timeUnit) throws TimeoutException, WebcamException {
        if (l2 < 0L) {
            throw new IllegalArgumentException(String.format("Timeout cannot be negative (%d)", l2));
        }
        if (timeUnit == null) {
            throw new IllegalArgumentException("Time unit cannot be null!");
        }
        WebcamDiscoveryService webcamDiscoveryService = Webcam.getDiscoveryService();
        assert (webcamDiscoveryService != null);
        List<Webcam> list2 = webcamDiscoveryService.getWebcams(l2, timeUnit);
        if (!webcamDiscoveryService.isRunning()) {
            webcamDiscoveryService.start();
        }
        return list2;
    }

    public static Webcam getDefault() throws WebcamException {
        try {
            return Webcam.getDefault(Long.MAX_VALUE);
        }
        catch (TimeoutException timeoutException) {
            throw new RuntimeException(timeoutException);
        }
    }

    public static Webcam getDefault(long l2) throws TimeoutException, WebcamException {
        if (l2 < 0L) {
            throw new IllegalArgumentException(String.format("Timeout cannot be negative (%d)", l2));
        }
        return Webcam.getDefault(l2, TimeUnit.MILLISECONDS);
    }

    public static Webcam getDefault(long l2, TimeUnit timeUnit) throws TimeoutException, WebcamException {
        if (l2 < 0L) {
            throw new IllegalArgumentException(String.format("Timeout cannot be negative (%d)", l2));
        }
        if (timeUnit == null) {
            throw new IllegalArgumentException("Time unit cannot be null!");
        }
        List<Webcam> list2 = Webcam.getWebcams(l2, timeUnit);
        assert (list2 != null);
        if (!list2.isEmpty()) {
            return list2.get(0);
        }
        LOG.warn("No webcam has been detected!");
        return null;
    }

    public String getName() {
        assert (this.device != null);
        return this.device.getName();
    }

    public String toString() {
        return String.format("Webcam %s", this.getName());
    }

    public boolean addWebcamListener(WebcamListener webcamListener) {
        if (webcamListener == null) {
            throw new IllegalArgumentException("Webcam listener cannot be null!");
        }
        assert (this.listeners != null);
        return this.listeners.add(webcamListener);
    }

    public WebcamListener[] getWebcamListeners() {
        assert (this.listeners != null);
        return this.listeners.toArray(new WebcamListener[this.listeners.size()]);
    }

    public int getWebcamListenersCount() {
        assert (this.listeners != null);
        return this.listeners.size();
    }

    public boolean removeWebcamListener(WebcamListener webcamListener) {
        assert (this.listeners != null);
        return this.listeners.remove(webcamListener);
    }

    public static synchronized WebcamDriver getDriver() {
        if (driver != null) {
            return driver;
        }
        if (driver == null) {
            driver = WebcamDriverUtils.findDriver(DRIVERS_LIST, DRIVERS_CLASS_LIST);
        }
        if (driver == null) {
            driver = new WebcamDefaultDriver();
        }
        LOG.info("{} capture driver will be used", (Object)driver.getClass().getSimpleName());
        return driver;
    }

    public static void setDriver(WebcamDriver webcamDriver) {
        if (webcamDriver == null) {
            throw new IllegalArgumentException("Webcam driver cannot be null!");
        }
        LOG.debug("Setting new capture driver {}", (Object)webcamDriver);
        Webcam.resetDriver();
        driver = webcamDriver;
    }

    public static void setDriver(Class<? extends WebcamDriver> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Webcam driver class cannot be null!");
        }
        Webcam.resetDriver();
        try {
            driver = clazz.newInstance();
            return;
        }
        catch (InstantiationException instantiationException) {
            throw new WebcamException(instantiationException);
        }
        catch (IllegalAccessException illegalAccessException) {
            throw new WebcamException(illegalAccessException);
        }
    }

    public static void resetDriver() {
        List<String> list2 = DRIVERS_LIST;
        synchronized (list2) {
            DRIVERS_LIST.clear();
        }
        if (discovery != null) {
            discovery.shutdown();
            discovery = null;
        }
        driver = null;
    }

    public static void registerDriver(Class<? extends WebcamDriver> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Webcam driver class to register cannot be null!");
        }
        DRIVERS_CLASS_LIST.add(clazz);
        Webcam.registerDriver(clazz.getCanonicalName());
    }

    public static void registerDriver(String string) {
        if (string == null) {
            throw new IllegalArgumentException("Webcam driver class name to register cannot be null!");
        }
        DRIVERS_LIST.add(string);
    }

    public static void setHandleTermSignal(boolean bl) {
        if (bl) {
            LOG.warn("Automated deallocation on TERM signal is now enabled! Make sure to not use it in production!");
        }
        deallocOnTermSignal = bl;
    }

    public static boolean isHandleTermSignal() {
        return deallocOnTermSignal;
    }

    public static void setAutoOpenMode(boolean bl) {
        autoOpen = bl;
    }

    public static boolean isAutoOpenMode() {
        return autoOpen;
    }

    public static boolean addDiscoveryListener(WebcamDiscoveryListener webcamDiscoveryListener) {
        if (webcamDiscoveryListener == null) {
            throw new IllegalArgumentException("Webcam discovery listener cannot be null!");
        }
        return DISCOVERY_LISTENERS.add(webcamDiscoveryListener);
    }

    public static WebcamDiscoveryListener[] getDiscoveryListeners() {
        List<WebcamDiscoveryListener> list2 = DISCOVERY_LISTENERS;
        return list2.toArray(new WebcamDiscoveryListener[list2.size()]);
    }

    public static boolean removeDiscoveryListener(WebcamDiscoveryListener webcamDiscoveryListener) {
        return DISCOVERY_LISTENERS.remove(webcamDiscoveryListener);
    }

    public static synchronized WebcamDiscoveryService getDiscoveryService() {
        if (discovery == null) {
            discovery = new WebcamDiscoveryService(Webcam.getDriver());
        }
        return discovery;
    }

    public static synchronized WebcamDiscoveryService getDiscoveryServiceRef() {
        return discovery;
    }

    public WebcamImageTransformer getImageTransformer() {
        return this.transformer;
    }

    public void setImageTransformer(WebcamImageTransformer webcamImageTransformer) {
        this.transformer = webcamImageTransformer;
    }

    public WebcamLock getLock() {
        return this.lock;
    }

    protected static void shutdown() {
        WebcamDiscoveryService webcamDiscoveryService = Webcam.getDiscoveryServiceRef();
        if (webcamDiscoveryService != null) {
            webcamDiscoveryService.stop();
        }
        WebcamProcessor.getInstance().shutdown();
    }

    public static Webcam getWebcamByName(String string) {
        if (string == null) {
            throw new IllegalArgumentException("Webcam name cannot be null");
        }
        for (Webcam webcam : Webcam.getWebcams()) {
            if (!webcam.getName().equals(string)) continue;
            return webcam;
        }
        return null;
    }

    final class NotificationThreadFactory
    implements ThreadFactory {
        private NotificationThreadFactory() {
        }

        @Override
        public final Thread newThread(Runnable runnable2) {
            runnable2 = new Thread(runnable2, String.format("notificator-[%s]", Webcam.this.getName()));
            ((Thread)runnable2).setUncaughtExceptionHandler(WebcamExceptionHandler.getInstance());
            ((Thread)runnable2).setDaemon(true);
            return runnable2;
        }
    }

    static final class ImageNotification
    implements Runnable {
        private final Webcam webcam;
        private final BufferedImage image;

        public ImageNotification(Webcam webcam, BufferedImage bufferedImage) {
            this.webcam = webcam;
            this.image = bufferedImage;
        }

        @Override
        public final void run() {
            if (this.image != null) {
                WebcamEvent webcamEvent = new WebcamEvent(WebcamEventType.NEW_IMAGE, this.webcam, this.image);
                for (WebcamListener webcamListener : this.webcam.getWebcamListeners()) {
                    try {
                        webcamListener.webcamImageObtained(webcamEvent);
                    }
                    catch (Exception exception) {
                        LOG.error(String.format("Notify image acquired, exception when calling listener %s", webcamListener.getClass()), exception);
                    }
                }
            }
        }
    }
}


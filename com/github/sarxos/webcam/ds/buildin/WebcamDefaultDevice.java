/*
 * Decompiled with CFR 0.152.
 */
package com.github.sarxos.webcam.ds.buildin;

import com.github.sarxos.webcam.WebcamDevice;
import com.github.sarxos.webcam.WebcamException;
import com.github.sarxos.webcam.WebcamExceptionHandler;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.WebcamTask;
import com.github.sarxos.webcam.ds.buildin.natives.Device;
import com.github.sarxos.webcam.ds.buildin.natives.DeviceList;
import com.github.sarxos.webcam.ds.buildin.natives.OpenIMAJGrabber;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.bridj.Pointer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebcamDefaultDevice
implements WebcamDevice,
WebcamDevice.BufferAccess,
WebcamDevice.FPSSource,
Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(WebcamDefaultDevice.class);
    private static final int DEVICE_BUFFER_SIZE = 5;
    private static final Dimension[] DIMENSIONS = new Dimension[]{WebcamResolution.QQVGA.getSize(), WebcamResolution.QVGA.getSize(), WebcamResolution.VGA.getSize()};
    private static final int[] BAND_OFFSETS = new int[]{0, 1, 2};
    private static final int[] BITS = new int[]{8, 8, 8};
    private static final int[] OFFSET = new int[]{0};
    private static final int DATA_TYPE = 0;
    private static final ColorSpace COLOR_SPACE = ColorSpace.getInstance(1000);
    private int timeout = 5000;
    private OpenIMAJGrabber grabber = null;
    private Device device = null;
    private Dimension size = null;
    private ComponentSampleModel smodel = null;
    private ColorModel cmodel = null;
    private boolean failOnSizeMismatch = false;
    private final AtomicBoolean disposed = new AtomicBoolean(false);
    private final AtomicBoolean open = new AtomicBoolean(false);
    private final AtomicBoolean fresh = new AtomicBoolean(false);
    private Thread refresher = null;
    private String name = null;
    private String id = null;
    private String fullname = null;
    private long t1 = -1L;
    private long t2 = -1L;
    private volatile double fps = 0.0;

    protected WebcamDefaultDevice(Device device) {
        this.device = device;
        this.name = device.getNameStr();
        this.id = device.getIdentifierStr();
        this.fullname = String.format("%s %s", this.name, this.id);
    }

    @Override
    public String getName() {
        return this.fullname;
    }

    public String getDeviceName() {
        return this.name;
    }

    public String getDeviceId() {
        return this.id;
    }

    public Device getDeviceRef() {
        return this.device;
    }

    @Override
    public Dimension[] getResolutions() {
        return DIMENSIONS;
    }

    @Override
    public Dimension getResolution() {
        if (this.size == null) {
            this.size = this.getResolutions()[0];
        }
        return this.size;
    }

    @Override
    public void setResolution(Dimension dimension) {
        if (dimension == null) {
            throw new IllegalArgumentException("Size cannot be null");
        }
        if (this.open.get()) {
            throw new IllegalStateException("Cannot change resolution when webcam is open, please close it first");
        }
        this.size = dimension;
    }

    @Override
    public ByteBuffer getImageBytes() {
        if (this.disposed.get()) {
            LOG.debug("Webcam is disposed, image will be null");
            return null;
        }
        if (!this.open.get()) {
            LOG.debug("Webcam is closed, image will be null");
            return null;
        }
        if (this.fresh.compareAndSet(false, true)) {
            this.updateFrameBuffer();
        }
        LOG.trace("Webcam grabber get image pointer");
        Pointer<Byte> pointer = this.grabber.getImage();
        this.fresh.set(false);
        if (pointer == null) {
            LOG.warn("Null array pointer found instead of image");
            return null;
        }
        int n2 = this.size.width * this.size.height * 3;
        LOG.trace("Webcam device get buffer, read {} bytes", (Object)n2);
        return pointer.getByteBuffer(n2);
    }

    @Override
    public void getImageBytes(ByteBuffer byteBuffer) {
        if (this.disposed.get()) {
            LOG.debug("Webcam is disposed, image will be null");
            return;
        }
        if (!this.open.get()) {
            LOG.debug("Webcam is closed, image will be null");
            return;
        }
        int n2 = this.size.width * this.size.height * 3;
        int n3 = byteBuffer.remaining();
        if (n2 < n3) {
            throw new IllegalArgumentException(String.format("Not enough remaining space in target buffer (%d necessary vs %d remaining)", n2, n3));
        }
        if (this.fresh.compareAndSet(false, true)) {
            this.updateFrameBuffer();
        }
        LOG.trace("Webcam grabber get image pointer");
        Pointer<Byte> pointer = this.grabber.getImage();
        this.fresh.set(false);
        if (pointer == null) {
            LOG.warn("Null array pointer found instead of image");
            return;
        }
        LOG.trace("Webcam device read buffer {} bytes", (Object)n2);
        pointer = pointer.validBytes(n2);
        pointer.getBytes(byteBuffer);
    }

    @Override
    public BufferedImage getImage() {
        Object object = this.getImageBytes();
        if (object == null) {
            LOG.error("Images bytes buffer is null!");
            return null;
        }
        byte[] byArray = new byte[this.size.width * this.size.height * 3];
        byte[][] byArrayArray = new byte[][]{byArray};
        ((ByteBuffer)object).get(byArray);
        object = new DataBufferByte(byArrayArray, byArray.length, OFFSET);
        object = Raster.createWritableRaster(this.smodel, (DataBuffer)object, null);
        object = new BufferedImage(this.cmodel, (WritableRaster)object, false, null);
        ((Image)object).flush();
        return object;
    }

    @Override
    public void open() {
        if (this.disposed.get()) {
            return;
        }
        LOG.debug("Opening webcam device {}", (Object)this.getName());
        if (this.size == null) {
            this.size = this.getResolutions()[0];
        }
        if (this.size == null) {
            throw new RuntimeException("The resolution size cannot be null");
        }
        LOG.debug("Webcam device {} starting session, size {}", (Object)this.device.getIdentifierStr(), (Object)this.size);
        this.grabber = new OpenIMAJGrabber();
        DeviceList deviceList = this.grabber.getVideoDevices().get();
        for (Device object2 : deviceList.asArrayList()) {
            object2.getNameStr();
            object2.getIdentifierStr();
        }
        int n2 = this.grabber.startSession(this.size.width, this.size.height, 50.0, Pointer.pointerTo(this.device));
        if (n2 == 0) {
            throw new WebcamException("Cannot start native grabber!");
        }
        this.grabber.setTimeout(this.timeout);
        LOG.debug("Webcam device session started");
        Dimension n5 = new Dimension(this.grabber.getWidth(), this.grabber.getHeight());
        n2 = this.size.width;
        int n3 = n5.width;
        int n4 = this.size.height;
        int n6 = n5.height;
        if (n2 != n3 || n4 != n6) {
            if (this.failOnSizeMismatch) {
                throw new WebcamException(String.format("Different size obtained vs requested - [%dx%d] vs [%dx%d]", n2, n4, n3, n6));
            }
            Object[] objectArray = new Object[]{n2, n4, n3, n6, n3, n6};
            LOG.warn("Different size obtained vs requested - [{}x{}] vs [{}x{}]. Setting correct one. New size is [{}x{}]", objectArray);
            this.size = new Dimension(n3, n6);
        }
        this.smodel = new ComponentSampleModel(0, this.size.width, this.size.height, 3, this.size.width * 3, BAND_OFFSETS);
        this.cmodel = new ComponentColorModel(COLOR_SPACE, BITS, false, false, 1, 0);
        LOG.debug("Clear memory buffer");
        this.clearMemoryBuffer();
        LOG.debug("Webcam device {} is now open", (Object)this);
        this.open.set(true);
        this.refresher = this.startFramesRefresher();
    }

    private void clearMemoryBuffer() {
        for (int i2 = 0; i2 < 5; ++i2) {
            this.grabber.nextFrame();
        }
    }

    private Thread startFramesRefresher() {
        Thread thread2 = new Thread((Runnable)this, String.format("frames-refresher-[%s]", this.id));
        thread2.setUncaughtExceptionHandler(WebcamExceptionHandler.getInstance());
        thread2.setDaemon(true);
        thread2.start();
        return thread2;
    }

    @Override
    public void close() {
        if (!this.open.compareAndSet(true, false)) {
            return;
        }
        LOG.debug("Closing webcam device");
        this.grabber.stopSession();
    }

    @Override
    public void dispose() {
        if (!this.disposed.compareAndSet(false, true)) {
            return;
        }
        LOG.debug("Disposing webcam device {}", (Object)this.getName());
        this.close();
    }

    public void setFailOnSizeMismatch(boolean bl) {
        this.failOnSizeMismatch = bl;
    }

    @Override
    public boolean isOpen() {
        return this.open.get();
    }

    public int getTimeout() {
        return this.timeout;
    }

    public void setTimeout(int n2) {
        if (this.isOpen()) {
            throw new WebcamException("Timeout must be set before webcam is open");
        }
        this.timeout = n2;
    }

    private void updateFrameBuffer() {
        LOG.trace("Next frame");
        if (this.t1 == -1L || this.t2 == -1L) {
            this.t1 = System.currentTimeMillis();
            this.t2 = System.currentTimeMillis();
        }
        WebcamDefaultDevice webcamDefaultDevice = this;
        int n2 = webcamDefaultDevice.new NextFrameTask((WebcamDevice)webcamDefaultDevice).nextFrame();
        this.t1 = this.t2;
        this.t2 = System.currentTimeMillis();
        this.fps = (4.0 * this.fps + (double)(1000L / (this.t2 - this.t1 + 1L))) / 5.0;
        if (n2 == -1) {
            LOG.error("Timeout when requesting image!");
            return;
        }
        if (n2 < -1) {
            LOG.error("Error requesting new frame!");
        }
    }

    @Override
    public void run() {
        do {
            if (Thread.interrupted()) {
                LOG.debug("Refresher has been interrupted");
                return;
            }
            if (!this.open.get()) {
                LOG.debug("Cancelling refresher");
                return;
            }
            this.updateFrameBuffer();
        } while (this.open.get());
    }

    @Override
    public double getFPS() {
        return this.fps;
    }

    class NextFrameTask
    extends WebcamTask {
        private final AtomicInteger result;

        public NextFrameTask(WebcamDevice webcamDevice) {
            super(webcamDevice);
            this.result = new AtomicInteger(0);
        }

        public int nextFrame() {
            try {
                this.process();
            }
            catch (InterruptedException interruptedException) {
                LOG.debug("Image buffer request interrupted", interruptedException);
            }
            return this.result.get();
        }

        @Override
        public void handle() {
            WebcamDefaultDevice webcamDefaultDevice = (WebcamDefaultDevice)this.getDevice();
            if (!webcamDefaultDevice.isOpen()) {
                return;
            }
            this.result.set(WebcamDefaultDevice.this.grabber.nextFrame());
            WebcamDefaultDevice.this.fresh.set(true);
        }
    }
}


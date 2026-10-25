/*
 * Decompiled with CFR 0.152.
 */
package com.github.sarxos.webcam.ds.buildin;

import com.github.sarxos.webcam.WebcamDevice;
import com.github.sarxos.webcam.WebcamDiscoverySupport;
import com.github.sarxos.webcam.WebcamDriver;
import com.github.sarxos.webcam.WebcamTask;
import com.github.sarxos.webcam.ds.buildin.WebcamDefaultDevice;
import com.github.sarxos.webcam.ds.buildin.natives.Device;
import com.github.sarxos.webcam.ds.buildin.natives.DeviceList;
import com.github.sarxos.webcam.ds.buildin.natives.OpenIMAJGrabber;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.bridj.Pointer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebcamDefaultDriver
implements WebcamDiscoverySupport,
WebcamDriver {
    private static final Logger LOG;
    private static OpenIMAJGrabber grabber;

    @Override
    public List<WebcamDevice> getDevices() {
        Object object;
        LOG.debug("Searching devices");
        if (grabber == null && (grabber = ((WebcamNewGrabberTask)(object = new WebcamNewGrabberTask(this))).newGrabber()) == null) {
            return Collections.emptyList();
        }
        object = new GetDevicesTask(this).getDevices(grabber);
        if (LOG.isDebugEnabled()) {
            Iterator iterator2 = object.iterator();
            while (iterator2.hasNext()) {
                WebcamDevice webcamDevice = (WebcamDevice)iterator2.next();
                LOG.debug("Found device {}", (Object)webcamDevice.getName());
            }
        }
        return object;
    }

    @Override
    public long getScanInterval() {
        return 3000L;
    }

    @Override
    public boolean isScanPossible() {
        return true;
    }

    @Override
    public boolean isThreadSafe() {
        return false;
    }

    static {
        if (!"true".equals(System.getProperty("webcam.debug"))) {
            System.setProperty("bridj.quiet", "true");
        }
        LOG = LoggerFactory.getLogger(WebcamDefaultDriver.class);
        grabber = null;
    }

    static class GetDevicesTask
    extends WebcamTask {
        private volatile List<WebcamDevice> devices = null;
        private volatile OpenIMAJGrabber grabber = null;

        public GetDevicesTask(WebcamDriver webcamDriver) {
            super(webcamDriver, null);
        }

        public List<WebcamDevice> getDevices(OpenIMAJGrabber openIMAJGrabber) {
            this.grabber = openIMAJGrabber;
            try {
                this.process();
            }
            catch (InterruptedException interruptedException) {
                LOG.error("Processor has been interrupted");
                return Collections.emptyList();
            }
            return this.devices;
        }

        @Override
        public void handle() {
            this.devices = new ArrayList<WebcamDevice>();
            Iterator<Device> iterator2 = this.grabber.getVideoDevices();
            iterator2 = ((Pointer)((Object)iterator2)).get();
            for (Device device : ((DeviceList)((Object)iterator2)).asArrayList()) {
                this.devices.add(new WebcamDefaultDevice(device));
            }
        }
    }

    static class WebcamNewGrabberTask
    extends WebcamTask {
        private AtomicReference<OpenIMAJGrabber> grabber = new AtomicReference();

        public WebcamNewGrabberTask(WebcamDriver webcamDriver) {
            super(webcamDriver, null);
        }

        public OpenIMAJGrabber newGrabber() {
            try {
                this.process();
            }
            catch (InterruptedException interruptedException) {
                LOG.error("Processor has been interrupted");
                return null;
            }
            return this.grabber.get();
        }

        @Override
        public void handle() {
            this.grabber.set(new OpenIMAJGrabber());
        }
    }
}


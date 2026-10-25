/*
 * Decompiled with CFR 0.152.
 */
package com.github.sarxos.webcam.ds.cgt;

import com.github.sarxos.webcam.WebcamDevice;
import com.github.sarxos.webcam.WebcamDriver;
import com.github.sarxos.webcam.WebcamTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebcamOpenTask
extends WebcamTask {
    private static final Logger LOG = LoggerFactory.getLogger(WebcamOpenTask.class);

    public WebcamOpenTask(WebcamDriver webcamDriver, WebcamDevice webcamDevice) {
        super(webcamDriver, webcamDevice);
    }

    public void open() throws InterruptedException {
        this.process();
    }

    @Override
    public void handle() {
        WebcamDevice webcamDevice = this.getDevice();
        if (webcamDevice.isOpen()) {
            return;
        }
        if (webcamDevice.getResolution() == null) {
            WebcamDevice webcamDevice2 = webcamDevice;
            webcamDevice2.setResolution(webcamDevice2.getResolutions()[0]);
        }
        LOG.info("Opening webcam {}", (Object)webcamDevice.getName());
        webcamDevice.open();
    }
}


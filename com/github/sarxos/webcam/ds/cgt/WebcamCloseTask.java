/*
 * Decompiled with CFR 0.152.
 */
package com.github.sarxos.webcam.ds.cgt;

import com.github.sarxos.webcam.WebcamDevice;
import com.github.sarxos.webcam.WebcamDriver;
import com.github.sarxos.webcam.WebcamTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebcamCloseTask
extends WebcamTask {
    private static final Logger LOG = LoggerFactory.getLogger(WebcamCloseTask.class);

    public WebcamCloseTask(WebcamDriver webcamDriver, WebcamDevice webcamDevice) {
        super(webcamDriver, webcamDevice);
    }

    public void close() throws InterruptedException {
        this.process();
    }

    @Override
    public void handle() {
        WebcamDevice webcamDevice = this.getDevice();
        if (!webcamDevice.isOpen()) {
            return;
        }
        LOG.info("Closing {}", (Object)webcamDevice.getName());
        webcamDevice.close();
    }
}


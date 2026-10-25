/*
 * Decompiled with CFR 0.152.
 */
package com.github.sarxos.webcam.ds.cgt;

import com.github.sarxos.webcam.WebcamDevice;
import com.github.sarxos.webcam.WebcamDriver;
import com.github.sarxos.webcam.WebcamTask;
import java.awt.image.BufferedImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebcamGetImageTask
extends WebcamTask {
    private static final Logger LOG = LoggerFactory.getLogger(WebcamGetImageTask.class);
    private volatile BufferedImage image = null;

    public WebcamGetImageTask(WebcamDriver webcamDriver, WebcamDevice webcamDevice) {
        super(webcamDriver, webcamDevice);
    }

    public BufferedImage getImage() {
        try {
            this.process();
        }
        catch (InterruptedException interruptedException) {
            LOG.debug("Interrupted exception", interruptedException);
            return null;
        }
        return this.image;
    }

    @Override
    public void handle() {
        WebcamDevice webcamDevice = this.getDevice();
        if (!webcamDevice.isOpen()) {
            return;
        }
        this.image = webcamDevice.getImage();
    }
}


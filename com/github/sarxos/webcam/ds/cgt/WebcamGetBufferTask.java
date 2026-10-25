/*
 * Decompiled with CFR 0.152.
 */
package com.github.sarxos.webcam.ds.cgt;

import com.github.sarxos.webcam.WebcamDevice;
import com.github.sarxos.webcam.WebcamDriver;
import com.github.sarxos.webcam.WebcamTask;
import java.nio.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebcamGetBufferTask
extends WebcamTask {
    private static final Logger LOG = LoggerFactory.getLogger(WebcamGetBufferTask.class);
    private volatile ByteBuffer buffer = null;

    public WebcamGetBufferTask(WebcamDriver webcamDriver, WebcamDevice webcamDevice) {
        super(webcamDriver, webcamDevice);
    }

    public ByteBuffer getBuffer() {
        try {
            this.process();
        }
        catch (InterruptedException interruptedException) {
            LOG.debug("Image buffer request interrupted", interruptedException);
            return null;
        }
        return this.buffer;
    }

    @Override
    public void handle() {
        WebcamDevice webcamDevice = this.getDevice();
        if (!webcamDevice.isOpen()) {
            return;
        }
        if (!(webcamDevice instanceof WebcamDevice.BufferAccess)) {
            return;
        }
        this.buffer = ((WebcamDevice.BufferAccess)((Object)webcamDevice)).getImageBytes();
    }
}


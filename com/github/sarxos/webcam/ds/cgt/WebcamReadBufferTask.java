/*
 * Decompiled with CFR 0.152.
 */
package com.github.sarxos.webcam.ds.cgt;

import com.github.sarxos.webcam.WebcamDevice;
import com.github.sarxos.webcam.WebcamDriver;
import com.github.sarxos.webcam.WebcamTask;
import java.nio.ByteBuffer;

public class WebcamReadBufferTask
extends WebcamTask {
    private volatile ByteBuffer target = null;

    public WebcamReadBufferTask(WebcamDriver webcamDriver, WebcamDevice webcamDevice, ByteBuffer byteBuffer) {
        super(webcamDriver, webcamDevice);
        this.target = byteBuffer;
    }

    public ByteBuffer readBuffer() {
        try {
            this.process();
        }
        catch (InterruptedException interruptedException) {
            return null;
        }
        return this.target;
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
        ((WebcamDevice.BufferAccess)((Object)webcamDevice)).getImageBytes(this.target);
    }
}


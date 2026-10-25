/*
 * Decompiled with CFR 0.152.
 */
package com.github.sarxos.webcam.ds.cgt;

import com.github.sarxos.webcam.WebcamDevice;
import com.github.sarxos.webcam.WebcamDriver;
import com.github.sarxos.webcam.WebcamTask;

public class WebcamDisposeTask
extends WebcamTask {
    public WebcamDisposeTask(WebcamDriver webcamDriver, WebcamDevice webcamDevice) {
        super(webcamDriver, webcamDevice);
    }

    public void dispose() throws InterruptedException {
        this.process();
    }

    @Override
    public void handle() {
        this.getDevice().dispose();
    }
}


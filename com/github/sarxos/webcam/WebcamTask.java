/*
 * Decompiled with CFR 0.152.
 */
package com.github.sarxos.webcam;

import com.github.sarxos.webcam.WebcamDevice;
import com.github.sarxos.webcam.WebcamDriver;
import com.github.sarxos.webcam.WebcamProcessor;

public abstract class WebcamTask {
    private boolean doSync = true;
    private WebcamProcessor processor = null;
    private WebcamDevice device = null;
    private Throwable throwable = null;

    public WebcamTask(boolean bl, WebcamDevice webcamDevice) {
        this.doSync = !bl;
        this.device = webcamDevice;
        this.processor = WebcamProcessor.getInstance();
    }

    public WebcamTask(WebcamDriver webcamDriver, WebcamDevice webcamDevice) {
        this(webcamDriver.isThreadSafe(), webcamDevice);
    }

    public WebcamTask(WebcamDevice webcamDevice) {
        this(false, webcamDevice);
    }

    public WebcamDevice getDevice() {
        return this.device;
    }

    public void process() throws InterruptedException {
        boolean bl = Thread.currentThread() instanceof WebcamProcessor.ProcessorThread;
        if (!bl && this.doSync) {
            if (this.processor == null) {
                throw new RuntimeException("Driver should be synchronized, but processor is null");
            }
            this.processor.process(this);
            return;
        }
        this.handle();
    }

    public Throwable getThrowable() {
        return this.throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    protected abstract void handle();
}


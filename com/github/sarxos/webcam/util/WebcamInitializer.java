/*
 * Decompiled with CFR 0.152.
 */
package com.github.sarxos.webcam.util;

import com.github.sarxos.webcam.WebcamException;
import com.github.sarxos.webcam.util.Initializable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class WebcamInitializer {
    private final Initializable initializable;
    private final AtomicBoolean initialized = new AtomicBoolean(false);
    private final CountDownLatch latch = new CountDownLatch(1);

    public WebcamInitializer(Initializable initializable) {
        this.initializable = initializable;
    }

    public void initialize() {
        if (this.initialized.compareAndSet(false, true)) {
            try {
                this.initializable.initialize();
            }
            catch (Exception exception) {
                throw new WebcamException(exception);
            }
            finally {
                this.latch.countDown();
            }
        } else {
            try {
                this.latch.await();
                return;
            }
            catch (InterruptedException interruptedException) {
                return;
            }
        }
    }

    public void teardown() {
        if (this.initialized.compareAndSet(true, false)) {
            try {
                this.initializable.teardown();
                return;
            }
            catch (Exception exception) {
                throw new WebcamException(exception);
            }
        }
    }

    public boolean isInitialized() {
        return this.initialized.get();
    }
}


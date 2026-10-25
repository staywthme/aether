/*
 * Decompiled with CFR 0.152.
 */
package com.github.sarxos.webcam;

import com.github.sarxos.webcam.WebcamException;

public class WebcamLockException
extends WebcamException {
    private static final long serialVersionUID = 1L;

    public WebcamLockException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public WebcamLockException(String string) {
        super(string);
    }

    public WebcamLockException(Throwable throwable) {
        super(throwable);
    }
}


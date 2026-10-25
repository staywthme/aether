/*
 * Decompiled with CFR 0.152.
 */
package com.github.sarxos.webcam;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.NOPLoggerFactory;

public class WebcamExceptionHandler
implements Thread.UncaughtExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(WebcamExceptionHandler.class);
    private static final WebcamExceptionHandler INSTANCE = new WebcamExceptionHandler();

    private WebcamExceptionHandler() {
    }

    @Override
    public void uncaughtException(Thread thread2, Throwable throwable) {
        ILoggerFactory iLoggerFactory = LoggerFactory.getILoggerFactory();
        if (iLoggerFactory instanceof NOPLoggerFactory) {
            System.err.println(String.format("Exception in thread %s", thread2.getName()));
            throwable.printStackTrace();
            return;
        }
        LOG.error(String.format("Exception in thread %s", thread2.getName()), throwable);
    }

    public static void handle(Throwable throwable) {
        INSTANCE.uncaughtException(Thread.currentThread(), throwable);
    }

    public static final WebcamExceptionHandler getInstance() {
        return INSTANCE;
    }
}


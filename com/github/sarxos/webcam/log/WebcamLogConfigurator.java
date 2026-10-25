/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ch.qos.logback.classic.LoggerContext
 *  ch.qos.logback.classic.joran.JoranConfigurator
 *  ch.qos.logback.core.Context
 */
package com.github.sarxos.webcam.log;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.Context;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebcamLogConfigurator {
    private static final Logger LOG = LoggerFactory.getLogger(WebcamLogConfigurator.class);

    public static void configure(InputStream inputStream2) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            LoggerContext loggerContext = new LoggerContext[]{"ch.qos.logback.classic.LoggerContext", "ch.qos.logback.classic.joran.JoranConfigurator"};
            LoggerContext loggerContext2 = loggerContext;
            loggerContext2 = loggerContext;
            int cfr_ignored_0 = ((String[])loggerContext).length;
            for (int i2 = 0; i2 < 2; ++i2) {
                String string = loggerContext2[i2];
                Class.forName(string, false, classLoader);
            }
            loggerContext2 = (LoggerContext)LoggerFactory.getILoggerFactory();
            classLoader = new JoranConfigurator();
            classLoader.setContext((Context)loggerContext2);
            loggerContext2.reset();
            classLoader.doConfigure(inputStream2);
            return;
        }
        catch (ClassNotFoundException classNotFoundException) {
            System.err.println("WLogC: Logback JARs are missing in classpath");
            return;
        }
        catch (NoClassDefFoundError noClassDefFoundError) {
            System.err.println("WLogC: Logback JARs are missing in classpath");
            return;
        }
        catch (Throwable throwable) {
            Throwable throwable2 = throwable;
            throwable.printStackTrace();
            return;
        }
    }

    /*
     * Loose catch block
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static void configure(File file) {
        FileInputStream fileInputStream = null;
        fileInputStream = new FileInputStream(file);
        WebcamLogConfigurator.configure(fileInputStream);
        try {
            fileInputStream.close();
            return;
        }
        catch (IOException iOException) {
            LOG.error("Cannot close file " + file, iOException);
            iOException.printStackTrace();
            return;
        }
        catch (FileNotFoundException fileNotFoundException) {
            try {
                LOG.error("File not found " + file, fileNotFoundException);
                fileNotFoundException.printStackTrace();
                if (fileInputStream == null) return;
            }
            catch (Throwable throwable) {
                if (fileInputStream == null) throw throwable;
                try {
                    fileInputStream.close();
                    throw throwable;
                }
                catch (IOException iOException) {
                    LOG.error("Cannot close file " + file, iOException);
                    iOException.printStackTrace();
                }
                throw throwable;
            }
            try {
                fileInputStream.close();
                return;
            }
            catch (IOException iOException) {
                LOG.error("Cannot close file " + file, iOException);
                iOException.printStackTrace();
                return;
            }
        }
    }

    public static void configure(String string) {
        WebcamLogConfigurator.configure(new File(string));
    }
}


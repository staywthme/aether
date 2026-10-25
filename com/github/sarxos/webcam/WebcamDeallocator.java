/*
 * Decompiled with CFR 0.152.
 */
package com.github.sarxos.webcam;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamSignalHandler;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

final class WebcamDeallocator {
    private static final WebcamSignalHandler HANDLER = new WebcamSignalHandler();
    private final Webcam[] webcams;

    private WebcamDeallocator(Webcam[] webcamArray) {
        this.webcams = webcamArray;
    }

    protected static void store(Webcam[] webcamArray) {
        if (HANDLER.get() == null) {
            HANDLER.set(new WebcamDeallocator(webcamArray));
            return;
        }
        throw new IllegalStateException("Deallocator is already set!");
    }

    protected static void unstore() {
        HANDLER.reset();
    }

    protected final void deallocate() {
        Webcam[] webcamArray = this.webcams;
        int n2 = this.webcams.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            Webcam webcam = webcamArray[i2];
            try {
                webcam.dispose();
                continue;
            }
            catch (Throwable throwable) {
                this.caugh(throwable);
            }
        }
    }

    private void caugh(Throwable throwable) {
        block5: {
            File file = new File(String.format("webcam-capture-hs-%s", System.currentTimeMillis()));
            PrintStream printStream = null;
            try {
                printStream = new PrintStream(file);
                throwable.printStackTrace(printStream);
                printStream.close();
                return;
            }
            catch (FileNotFoundException fileNotFoundException) {
                throwable = fileNotFoundException;
                fileNotFoundException.printStackTrace();
                return;
            }
            finally {
                if (printStream == null) break block5;
                printStream.close();
            }
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt.io;

import java.io.Closeable;
import java.io.InterruptedIOException;
import java.net.Socket;

public final class IoUtils {
    private IoUtils() {
    }

    public static void closeQuietly(Closeable object) {
        if (object != null) {
            try {
                object.close();
                return;
            }
            catch (RuntimeException runtimeException) {
                object = runtimeException;
                throw runtimeException;
            }
            catch (Exception exception) {}
        }
    }

    public static void closeQuietly(Socket socket) {
        if (socket != null) {
            try {
                socket.close();
                return;
            }
            catch (Exception exception) {}
        }
    }

    public static void throwInterruptedIoException() throws InterruptedIOException {
        Thread.currentThread().interrupt();
        throw new InterruptedIOException();
    }
}


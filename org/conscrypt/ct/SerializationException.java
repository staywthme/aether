/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt.ct;

public class SerializationException
extends Exception {
    private static final long serialVersionUID = -5317873136664833411L;

    public SerializationException() {
    }

    public SerializationException(String string) {
        super(string);
    }

    public SerializationException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public SerializationException(Throwable throwable) {
        super(throwable);
    }
}


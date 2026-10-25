/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import javax.net.ssl.SSLException;

public abstract class HandshakeListener {
    public abstract void onHandshakeFinished() throws SSLException;
}


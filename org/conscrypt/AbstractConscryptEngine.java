/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.nio.ByteBuffer;
import java.security.PrivateKey;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import org.conscrypt.ApplicationProtocolSelector;
import org.conscrypt.BufferAllocator;
import org.conscrypt.HandshakeListener;

abstract class AbstractConscryptEngine
extends SSLEngine {
    AbstractConscryptEngine() {
    }

    abstract void setBufferAllocator(BufferAllocator var1);

    abstract int maxSealOverhead();

    abstract void setChannelIdEnabled(boolean var1);

    abstract byte[] getChannelId() throws SSLException;

    abstract void setChannelIdPrivateKey(PrivateKey var1);

    abstract void setHandshakeListener(HandshakeListener var1);

    abstract void setHostname(String var1);

    abstract String getHostname();

    @Override
    public abstract String getPeerHost();

    @Override
    public abstract int getPeerPort();

    @Override
    public final SSLSession getHandshakeSession() {
        return this.handshakeSession();
    }

    abstract SSLSession handshakeSession();

    @Override
    public abstract SSLEngineResult unwrap(ByteBuffer var1, ByteBuffer var2) throws SSLException;

    @Override
    public abstract SSLEngineResult unwrap(ByteBuffer var1, ByteBuffer[] var2) throws SSLException;

    @Override
    public abstract SSLEngineResult unwrap(ByteBuffer var1, ByteBuffer[] var2, int var3, int var4) throws SSLException;

    abstract SSLEngineResult unwrap(ByteBuffer[] var1, ByteBuffer[] var2) throws SSLException;

    abstract SSLEngineResult unwrap(ByteBuffer[] var1, int var2, int var3, ByteBuffer[] var4, int var5, int var6) throws SSLException;

    @Override
    public abstract SSLEngineResult wrap(ByteBuffer var1, ByteBuffer var2) throws SSLException;

    @Override
    public abstract SSLEngineResult wrap(ByteBuffer[] var1, int var2, int var3, ByteBuffer var4) throws SSLException;

    abstract void setUseSessionTickets(boolean var1);

    abstract void setApplicationProtocols(String[] var1);

    abstract String[] getApplicationProtocols();

    @Override
    public abstract String getApplicationProtocol();

    @Override
    public abstract String getHandshakeApplicationProtocol();

    abstract void setApplicationProtocolSelector(ApplicationProtocolSelector var1);

    abstract byte[] getTlsUnique();

    abstract byte[] exportKeyingMaterial(String var1, byte[] var2, int var3) throws SSLException;
}


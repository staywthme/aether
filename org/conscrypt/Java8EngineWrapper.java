/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.nio.ByteBuffer;
import java.security.PrivateKey;
import java.util.List;
import java.util.function.BiFunction;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import org.conscrypt.AbstractConscryptEngine;
import org.conscrypt.ApplicationProtocolSelector;
import org.conscrypt.ApplicationProtocolSelectorAdapter;
import org.conscrypt.BufferAllocator;
import org.conscrypt.ConscryptEngine;
import org.conscrypt.HandshakeListener;
import org.conscrypt.Preconditions;

final class Java8EngineWrapper
extends AbstractConscryptEngine {
    private final ConscryptEngine delegate;
    private BiFunction<SSLEngine, List<String>, String> selector;

    Java8EngineWrapper(ConscryptEngine conscryptEngine) {
        this.delegate = Preconditions.checkNotNull(conscryptEngine, "delegate");
    }

    static SSLEngine getDelegate(SSLEngine sSLEngine) {
        if (sSLEngine instanceof Java8EngineWrapper) {
            return ((Java8EngineWrapper)sSLEngine).delegate;
        }
        return sSLEngine;
    }

    @Override
    public final SSLEngineResult wrap(ByteBuffer[] byteBufferArray, ByteBuffer byteBuffer) throws SSLException {
        return this.delegate.wrap(byteBufferArray, byteBuffer);
    }

    @Override
    public final SSLParameters getSSLParameters() {
        return this.delegate.getSSLParameters();
    }

    @Override
    public final void setSSLParameters(SSLParameters sSLParameters) {
        this.delegate.setSSLParameters(sSLParameters);
    }

    @Override
    final void setBufferAllocator(BufferAllocator bufferAllocator) {
        this.delegate.setBufferAllocator(bufferAllocator);
    }

    @Override
    final int maxSealOverhead() {
        return this.delegate.maxSealOverhead();
    }

    @Override
    final void setChannelIdEnabled(boolean bl) {
        this.delegate.setChannelIdEnabled(bl);
    }

    @Override
    final byte[] getChannelId() throws SSLException {
        return this.delegate.getChannelId();
    }

    @Override
    final void setChannelIdPrivateKey(PrivateKey privateKey) {
        this.delegate.setChannelIdPrivateKey(privateKey);
    }

    @Override
    final void setHandshakeListener(HandshakeListener handshakeListener) {
        this.delegate.setHandshakeListener(handshakeListener);
    }

    @Override
    final void setHostname(String string) {
        this.delegate.setHostname(string);
    }

    @Override
    final String getHostname() {
        return this.delegate.getHostname();
    }

    @Override
    public final String getPeerHost() {
        return this.delegate.getPeerHost();
    }

    @Override
    public final int getPeerPort() {
        return this.delegate.getPeerPort();
    }

    @Override
    public final void beginHandshake() throws SSLException {
        this.delegate.beginHandshake();
    }

    @Override
    public final void closeInbound() throws SSLException {
        this.delegate.closeInbound();
    }

    @Override
    public final void closeOutbound() {
        this.delegate.closeOutbound();
    }

    @Override
    public final Runnable getDelegatedTask() {
        return this.delegate.getDelegatedTask();
    }

    @Override
    public final String[] getEnabledCipherSuites() {
        return this.delegate.getEnabledCipherSuites();
    }

    @Override
    public final String[] getEnabledProtocols() {
        return this.delegate.getEnabledProtocols();
    }

    @Override
    public final boolean getEnableSessionCreation() {
        return this.delegate.getEnableSessionCreation();
    }

    @Override
    public final SSLEngineResult.HandshakeStatus getHandshakeStatus() {
        return this.delegate.getHandshakeStatus();
    }

    @Override
    public final boolean getNeedClientAuth() {
        return this.delegate.getNeedClientAuth();
    }

    @Override
    final SSLSession handshakeSession() {
        return this.delegate.handshakeSession();
    }

    @Override
    public final SSLSession getSession() {
        return this.delegate.getSession();
    }

    @Override
    public final String[] getSupportedCipherSuites() {
        return this.delegate.getSupportedCipherSuites();
    }

    @Override
    public final String[] getSupportedProtocols() {
        return this.delegate.getSupportedProtocols();
    }

    @Override
    public final boolean getUseClientMode() {
        return this.delegate.getUseClientMode();
    }

    @Override
    public final boolean getWantClientAuth() {
        return this.delegate.getWantClientAuth();
    }

    @Override
    public final boolean isInboundDone() {
        return this.delegate.isInboundDone();
    }

    @Override
    public final boolean isOutboundDone() {
        return this.delegate.isOutboundDone();
    }

    @Override
    public final void setEnabledCipherSuites(String[] stringArray) {
        this.delegate.setEnabledCipherSuites(stringArray);
    }

    @Override
    public final void setEnabledProtocols(String[] stringArray) {
        this.delegate.setEnabledProtocols(stringArray);
    }

    @Override
    public final void setEnableSessionCreation(boolean bl) {
        this.delegate.setEnableSessionCreation(bl);
    }

    @Override
    public final void setNeedClientAuth(boolean bl) {
        this.delegate.setNeedClientAuth(bl);
    }

    @Override
    public final void setUseClientMode(boolean bl) {
        this.delegate.setUseClientMode(bl);
    }

    @Override
    public final void setWantClientAuth(boolean bl) {
        this.delegate.setWantClientAuth(bl);
    }

    @Override
    public final SSLEngineResult unwrap(ByteBuffer byteBuffer, ByteBuffer byteBuffer2) throws SSLException {
        return this.delegate.unwrap(byteBuffer, byteBuffer2);
    }

    @Override
    public final SSLEngineResult unwrap(ByteBuffer byteBuffer, ByteBuffer[] byteBufferArray) throws SSLException {
        return this.delegate.unwrap(byteBuffer, byteBufferArray);
    }

    @Override
    public final SSLEngineResult unwrap(ByteBuffer byteBuffer, ByteBuffer[] byteBufferArray, int n2, int n3) throws SSLException {
        return this.delegate.unwrap(byteBuffer, byteBufferArray, n2, n3);
    }

    @Override
    final SSLEngineResult unwrap(ByteBuffer[] byteBufferArray, ByteBuffer[] byteBufferArray2) throws SSLException {
        return this.delegate.unwrap(byteBufferArray, byteBufferArray2);
    }

    @Override
    final SSLEngineResult unwrap(ByteBuffer[] byteBufferArray, int n2, int n3, ByteBuffer[] byteBufferArray2, int n4, int n5) throws SSLException {
        return this.delegate.unwrap(byteBufferArray, n2, n3, byteBufferArray2, n4, n5);
    }

    @Override
    public final SSLEngineResult wrap(ByteBuffer byteBuffer, ByteBuffer byteBuffer2) throws SSLException {
        return this.delegate.wrap(byteBuffer, byteBuffer2);
    }

    @Override
    public final SSLEngineResult wrap(ByteBuffer[] byteBufferArray, int n2, int n3, ByteBuffer byteBuffer) throws SSLException {
        return this.delegate.wrap(byteBufferArray, n2, n3, byteBuffer);
    }

    @Override
    final void setUseSessionTickets(boolean bl) {
        this.delegate.setUseSessionTickets(bl);
    }

    @Override
    final void setApplicationProtocols(String[] stringArray) {
        this.delegate.setApplicationProtocols(stringArray);
    }

    @Override
    final String[] getApplicationProtocols() {
        return this.delegate.getApplicationProtocols();
    }

    @Override
    public final String getApplicationProtocol() {
        return this.delegate.getApplicationProtocol();
    }

    @Override
    final void setApplicationProtocolSelector(ApplicationProtocolSelector applicationProtocolSelector) {
        this.delegate.setApplicationProtocolSelector(applicationProtocolSelector == null ? null : new ApplicationProtocolSelectorAdapter(this, applicationProtocolSelector));
    }

    @Override
    final byte[] getTlsUnique() {
        return this.delegate.getTlsUnique();
    }

    @Override
    final byte[] exportKeyingMaterial(String string, byte[] byArray, int n2) throws SSLException {
        return this.delegate.exportKeyingMaterial(string, byArray, n2);
    }

    @Override
    public final String getHandshakeApplicationProtocol() {
        return this.delegate.getHandshakeApplicationProtocol();
    }

    @Override
    public final void setHandshakeApplicationProtocolSelector(BiFunction<SSLEngine, List<String>, String> biFunction) {
        this.selector = biFunction;
        this.setApplicationProtocolSelector(Java8EngineWrapper.toApplicationProtocolSelector(biFunction));
    }

    @Override
    public final BiFunction<SSLEngine, List<String>, String> getHandshakeApplicationProtocolSelector() {
        return this.selector;
    }

    private static ApplicationProtocolSelector toApplicationProtocolSelector(final BiFunction<SSLEngine, List<String>, String> biFunction) {
        if (biFunction == null) {
            return null;
        }
        return new ApplicationProtocolSelector(){

            @Override
            public String selectApplicationProtocol(SSLEngine sSLEngine, List<String> list2) {
                return (String)biFunction.apply(sSLEngine, list2);
            }

            @Override
            public String selectApplicationProtocol(SSLSocket sSLSocket, List<String> list2) {
                throw new UnsupportedOperationException();
            }
        };
    }
}


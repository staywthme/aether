/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import javax.net.ssl.SSLServerSocket;
import org.conscrypt.AbstractConscryptSocket;
import org.conscrypt.NativeCrypto;
import org.conscrypt.OpenSSLSocketImpl;
import org.conscrypt.Platform;
import org.conscrypt.SSLParametersImpl;

final class ConscryptServerSocket
extends SSLServerSocket {
    private final SSLParametersImpl sslParameters;
    private boolean channelIdEnabled;
    private boolean useEngineSocket;

    ConscryptServerSocket(SSLParametersImpl sSLParametersImpl) throws IOException {
        this.sslParameters = sSLParametersImpl;
    }

    ConscryptServerSocket(int n2, SSLParametersImpl sSLParametersImpl) throws IOException {
        super(n2);
        this.sslParameters = sSLParametersImpl;
    }

    ConscryptServerSocket(int n2, int n3, SSLParametersImpl sSLParametersImpl) throws IOException {
        super(n2, n3);
        this.sslParameters = sSLParametersImpl;
    }

    ConscryptServerSocket(int n2, int n3, InetAddress inetAddress, SSLParametersImpl sSLParametersImpl) throws IOException {
        super(n2, n3, inetAddress);
        this.sslParameters = sSLParametersImpl;
    }

    final ConscryptServerSocket setUseEngineSocket(boolean bl) {
        this.useEngineSocket = bl;
        return this;
    }

    @Override
    public final boolean getEnableSessionCreation() {
        return this.sslParameters.getEnableSessionCreation();
    }

    @Override
    public final void setEnableSessionCreation(boolean bl) {
        this.sslParameters.setEnableSessionCreation(bl);
    }

    @Override
    public final String[] getSupportedProtocols() {
        return NativeCrypto.getSupportedProtocols();
    }

    @Override
    public final String[] getEnabledProtocols() {
        return this.sslParameters.getEnabledProtocols();
    }

    @Override
    public final void setEnabledProtocols(String[] stringArray) {
        this.sslParameters.setEnabledProtocols(stringArray);
    }

    @Override
    public final String[] getSupportedCipherSuites() {
        return NativeCrypto.getSupportedCipherSuites();
    }

    @Override
    public final String[] getEnabledCipherSuites() {
        return this.sslParameters.getEnabledCipherSuites();
    }

    final void setChannelIdEnabled(boolean bl) {
        this.channelIdEnabled = bl;
    }

    final boolean isChannelIdEnabled() {
        return this.channelIdEnabled;
    }

    @Override
    public final void setEnabledCipherSuites(String[] stringArray) {
        this.sslParameters.setEnabledCipherSuites(stringArray);
    }

    @Override
    public final boolean getWantClientAuth() {
        return this.sslParameters.getWantClientAuth();
    }

    @Override
    public final void setWantClientAuth(boolean bl) {
        this.sslParameters.setWantClientAuth(bl);
    }

    @Override
    public final boolean getNeedClientAuth() {
        return this.sslParameters.getNeedClientAuth();
    }

    @Override
    public final void setNeedClientAuth(boolean bl) {
        this.sslParameters.setNeedClientAuth(bl);
    }

    @Override
    public final void setUseClientMode(boolean bl) {
        this.sslParameters.setUseClientMode(bl);
    }

    @Override
    public final boolean getUseClientMode() {
        return this.sslParameters.getUseClientMode();
    }

    @Override
    public final Socket accept() throws IOException {
        OpenSSLSocketImpl openSSLSocketImpl = this.useEngineSocket ? Platform.createEngineSocket(this.sslParameters) : Platform.createFileDescriptorSocket(this.sslParameters);
        ((AbstractConscryptSocket)openSSLSocketImpl).setChannelIdEnabled(this.channelIdEnabled);
        this.implAccept(openSSLSocketImpl);
        return openSSLSocketImpl;
    }
}


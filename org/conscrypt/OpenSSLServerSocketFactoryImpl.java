/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.security.KeyManagementException;
import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;
import org.conscrypt.ConscryptServerSocket;
import org.conscrypt.NativeCrypto;
import org.conscrypt.SSLParametersImpl;
import org.conscrypt.SSLUtils;

final class OpenSSLServerSocketFactoryImpl
extends SSLServerSocketFactory {
    private static boolean useEngineSocketByDefault = SSLUtils.USE_ENGINE_SOCKET_BY_DEFAULT;
    private SSLParametersImpl sslParameters;
    private IOException instantiationException;
    private boolean useEngineSocket = useEngineSocketByDefault;

    OpenSSLServerSocketFactoryImpl() {
        try {
            this.sslParameters = SSLParametersImpl.getDefault();
            this.sslParameters.setUseClientMode(false);
            return;
        }
        catch (KeyManagementException keyManagementException) {
            this.instantiationException = new IOException("Delayed instantiation exception:");
            this.instantiationException.initCause(keyManagementException);
            return;
        }
    }

    OpenSSLServerSocketFactoryImpl(SSLParametersImpl sSLParametersImpl) {
        this.sslParameters = (SSLParametersImpl)sSLParametersImpl.clone();
        this.sslParameters.setUseClientMode(false);
    }

    static void setUseEngineSocketByDefault(boolean bl) {
        useEngineSocketByDefault = bl;
        ServerSocketFactory serverSocketFactory = SSLServerSocketFactory.getDefault();
        if (serverSocketFactory instanceof OpenSSLServerSocketFactoryImpl) {
            ((OpenSSLServerSocketFactoryImpl)serverSocketFactory).setUseEngineSocket(bl);
        }
    }

    final void setUseEngineSocket(boolean bl) {
        this.useEngineSocket = bl;
    }

    @Override
    public final String[] getDefaultCipherSuites() {
        return this.sslParameters.getEnabledCipherSuites();
    }

    @Override
    public final String[] getSupportedCipherSuites() {
        return NativeCrypto.getSupportedCipherSuites();
    }

    @Override
    public final ServerSocket createServerSocket() throws IOException {
        return new ConscryptServerSocket((SSLParametersImpl)this.sslParameters.clone()).setUseEngineSocket(this.useEngineSocket);
    }

    @Override
    public final ServerSocket createServerSocket(int n2) throws IOException {
        return new ConscryptServerSocket(n2, (SSLParametersImpl)this.sslParameters.clone()).setUseEngineSocket(this.useEngineSocket);
    }

    @Override
    public final ServerSocket createServerSocket(int n2, int n3) throws IOException {
        return new ConscryptServerSocket(n2, n3, (SSLParametersImpl)this.sslParameters.clone()).setUseEngineSocket(this.useEngineSocket);
    }

    @Override
    public final ServerSocket createServerSocket(int n2, int n3, InetAddress inetAddress) throws IOException {
        return new ConscryptServerSocket(n2, n3, inetAddress, (SSLParametersImpl)this.sslParameters.clone()).setUseEngineSocket(this.useEngineSocket);
    }
}


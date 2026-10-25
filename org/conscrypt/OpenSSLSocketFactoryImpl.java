/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import org.conscrypt.NativeCrypto;
import org.conscrypt.Platform;
import org.conscrypt.Preconditions;
import org.conscrypt.SSLParametersImpl;
import org.conscrypt.SSLUtils;

final class OpenSSLSocketFactoryImpl
extends SSLSocketFactory {
    private static boolean useEngineSocketByDefault = SSLUtils.USE_ENGINE_SOCKET_BY_DEFAULT;
    private final SSLParametersImpl sslParameters;
    private final IOException instantiationException;
    private boolean useEngineSocket = useEngineSocketByDefault;

    OpenSSLSocketFactoryImpl() {
        SSLParametersImpl sSLParametersImpl = null;
        IOException iOException = null;
        try {
            sSLParametersImpl = SSLParametersImpl.getDefault();
        }
        catch (KeyManagementException keyManagementException) {
            iOException = new IOException("Delayed instantiation exception:", keyManagementException);
        }
        this.sslParameters = sSLParametersImpl;
        this.instantiationException = iOException;
    }

    OpenSSLSocketFactoryImpl(SSLParametersImpl sSLParametersImpl) {
        this.sslParameters = sSLParametersImpl;
        this.instantiationException = null;
    }

    static void setUseEngineSocketByDefault(boolean bl) {
        useEngineSocketByDefault = bl;
        SocketFactory socketFactory = SSLSocketFactory.getDefault();
        if (socketFactory instanceof OpenSSLSocketFactoryImpl) {
            ((OpenSSLSocketFactoryImpl)socketFactory).setUseEngineSocket(bl);
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
    public final Socket createSocket() throws IOException {
        if (this.instantiationException != null) {
            throw this.instantiationException;
        }
        if (this.useEngineSocket) {
            return Platform.createEngineSocket((SSLParametersImpl)this.sslParameters.clone());
        }
        return Platform.createFileDescriptorSocket((SSLParametersImpl)this.sslParameters.clone());
    }

    @Override
    public final Socket createSocket(String string, int n2) throws IOException, UnknownHostException {
        if (this.useEngineSocket) {
            return Platform.createEngineSocket(string, n2, (SSLParametersImpl)this.sslParameters.clone());
        }
        return Platform.createFileDescriptorSocket(string, n2, (SSLParametersImpl)this.sslParameters.clone());
    }

    @Override
    public final Socket createSocket(String string, int n2, InetAddress inetAddress, int n3) throws IOException, UnknownHostException {
        if (this.useEngineSocket) {
            return Platform.createEngineSocket(string, n2, inetAddress, n3, (SSLParametersImpl)this.sslParameters.clone());
        }
        return Platform.createFileDescriptorSocket(string, n2, inetAddress, n3, (SSLParametersImpl)this.sslParameters.clone());
    }

    @Override
    public final Socket createSocket(InetAddress inetAddress, int n2) throws IOException {
        if (this.useEngineSocket) {
            return Platform.createEngineSocket(inetAddress, n2, (SSLParametersImpl)this.sslParameters.clone());
        }
        return Platform.createFileDescriptorSocket(inetAddress, n2, (SSLParametersImpl)this.sslParameters.clone());
    }

    @Override
    public final Socket createSocket(InetAddress inetAddress, int n2, InetAddress inetAddress2, int n3) throws IOException {
        if (this.useEngineSocket) {
            return Platform.createEngineSocket(inetAddress, n2, inetAddress2, n3, (SSLParametersImpl)this.sslParameters.clone());
        }
        return Platform.createFileDescriptorSocket(inetAddress, n2, inetAddress2, n3, (SSLParametersImpl)this.sslParameters.clone());
    }

    @Override
    public final Socket createSocket(Socket socket, String string, int n2, boolean bl) throws IOException {
        Preconditions.checkNotNull(socket, "socket");
        if (!socket.isConnected()) {
            throw new SocketException("Socket is not connected.");
        }
        if (!this.useEngineSocket && this.hasFileDescriptor(socket)) {
            return Platform.createFileDescriptorSocket(socket, string, n2, bl, (SSLParametersImpl)this.sslParameters.clone());
        }
        return Platform.createEngineSocket(socket, string, n2, bl, (SSLParametersImpl)this.sslParameters.clone());
    }

    private boolean hasFileDescriptor(Socket socket) {
        try {
            Platform.getFileDescriptor(socket);
            return true;
        }
        catch (RuntimeException runtimeException) {
            return false;
        }
    }
}


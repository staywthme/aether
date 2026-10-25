/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.SecureRandom;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContextSpi;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSessionContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import org.conscrypt.ClientSessionContext;
import org.conscrypt.ConscryptEngine;
import org.conscrypt.DefaultSSLContextImpl;
import org.conscrypt.NativeCrypto;
import org.conscrypt.OpenSSLServerSocketFactoryImpl;
import org.conscrypt.OpenSSLSocketFactoryImpl;
import org.conscrypt.Platform;
import org.conscrypt.SSLParametersImpl;
import org.conscrypt.ServerSessionContext;

public abstract class OpenSSLContextImpl
extends SSLContextSpi {
    private static DefaultSSLContextImpl defaultSslContextImpl;
    private final String[] protocols;
    private final ClientSessionContext clientSessionContext;
    private final ServerSessionContext serverSessionContext;
    SSLParametersImpl sslParameters;

    static OpenSSLContextImpl getPreferred() {
        return new TLSv13();
    }

    OpenSSLContextImpl(String[] stringArray) {
        this.protocols = stringArray;
        this.clientSessionContext = new ClientSessionContext();
        this.serverSessionContext = new ServerSessionContext();
    }

    OpenSSLContextImpl(String[] stringArray, boolean bl) throws GeneralSecurityException, IOException {
        Class<DefaultSSLContextImpl> clazz = DefaultSSLContextImpl.class;
        synchronized (DefaultSSLContextImpl.class) {
            this.protocols = null;
            if (defaultSslContextImpl == null) {
                this.clientSessionContext = new ClientSessionContext();
                this.serverSessionContext = new ServerSessionContext();
                defaultSslContextImpl = (DefaultSSLContextImpl)this;
            } else {
                this.clientSessionContext = (ClientSessionContext)defaultSslContextImpl.engineGetClientSessionContext();
                this.serverSessionContext = (ServerSessionContext)defaultSslContextImpl.engineGetServerSessionContext();
            }
            this.sslParameters = new SSLParametersImpl(defaultSslContextImpl.getKeyManagers(), defaultSslContextImpl.getTrustManagers(), null, this.clientSessionContext, this.serverSessionContext, stringArray);
            // ** MonitorExit[var2_4] (shouldn't be in output)
            return;
        }
    }

    @Override
    public void engineInit(KeyManager[] keyManagerArray, TrustManager[] trustManagerArray, SecureRandom secureRandom) throws KeyManagementException {
        this.sslParameters = new SSLParametersImpl(keyManagerArray, trustManagerArray, secureRandom, this.clientSessionContext, this.serverSessionContext, this.protocols);
    }

    @Override
    public SSLSocketFactory engineGetSocketFactory() {
        if (this.sslParameters == null) {
            throw new IllegalStateException("SSLContext is not initialized.");
        }
        return Platform.wrapSocketFactoryIfNeeded(new OpenSSLSocketFactoryImpl(this.sslParameters));
    }

    @Override
    public SSLServerSocketFactory engineGetServerSocketFactory() {
        if (this.sslParameters == null) {
            throw new IllegalStateException("SSLContext is not initialized.");
        }
        return new OpenSSLServerSocketFactoryImpl(this.sslParameters);
    }

    @Override
    public SSLEngine engineCreateSSLEngine(String string, int n2) {
        if (this.sslParameters == null) {
            throw new IllegalStateException("SSLContext is not initialized.");
        }
        SSLParametersImpl sSLParametersImpl = (SSLParametersImpl)this.sslParameters.clone();
        sSLParametersImpl.setUseClientMode(false);
        return Platform.wrapEngine(new ConscryptEngine(string, n2, sSLParametersImpl));
    }

    @Override
    public SSLEngine engineCreateSSLEngine() {
        if (this.sslParameters == null) {
            throw new IllegalStateException("SSLContext is not initialized.");
        }
        SSLParametersImpl sSLParametersImpl = (SSLParametersImpl)this.sslParameters.clone();
        sSLParametersImpl.setUseClientMode(false);
        return Platform.wrapEngine(new ConscryptEngine(sSLParametersImpl));
    }

    @Override
    public SSLSessionContext engineGetServerSessionContext() {
        return this.serverSessionContext;
    }

    @Override
    public SSLSessionContext engineGetClientSessionContext() {
        return this.clientSessionContext;
    }

    public static final class TLSv1
    extends OpenSSLContextImpl {
        public TLSv1() {
            super(NativeCrypto.TLSV1_PROTOCOLS);
        }
    }

    public static final class TLSv11
    extends OpenSSLContextImpl {
        public TLSv11() {
            super(NativeCrypto.TLSV11_PROTOCOLS);
        }
    }

    public static final class TLSv12
    extends OpenSSLContextImpl {
        public TLSv12() {
            super(NativeCrypto.TLSV12_PROTOCOLS);
        }
    }

    public static final class TLSv13
    extends OpenSSLContextImpl {
        public TLSv13() {
            super(NativeCrypto.TLSV13_PROTOCOLS);
        }
    }
}


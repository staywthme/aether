/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.security.KeyManagementException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.cert.X509Certificate;
import java.util.Properties;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLContextSpi;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.conscrypt.AbstractConscryptEngine;
import org.conscrypt.AbstractConscryptSocket;
import org.conscrypt.ApplicationProtocolSelector;
import org.conscrypt.BufferAllocator;
import org.conscrypt.ClientSessionContext;
import org.conscrypt.ConscryptEngine;
import org.conscrypt.ConscryptEngineSocket;
import org.conscrypt.ConscryptHostnameVerifier;
import org.conscrypt.HandshakeListener;
import org.conscrypt.NativeCrypto;
import org.conscrypt.OpenSSLContextImpl;
import org.conscrypt.OpenSSLProvider;
import org.conscrypt.OpenSSLServerSocketFactoryImpl;
import org.conscrypt.OpenSSLSocketFactoryImpl;
import org.conscrypt.Platform;
import org.conscrypt.SSLClientSessionCache;
import org.conscrypt.SSLParametersImpl;
import org.conscrypt.SSLServerSessionCache;
import org.conscrypt.ServerSessionContext;
import org.conscrypt.TrustManagerImpl;
import org.conscrypt.io.IoUtils;

public final class Conscrypt {
    private static final Version VERSION;

    private Conscrypt() {
    }

    public static boolean isAvailable() {
        try {
            Conscrypt.checkAvailability();
            return true;
        }
        catch (Throwable throwable) {
            return false;
        }
    }

    public static Version version() {
        return VERSION;
    }

    public static void checkAvailability() {
        NativeCrypto.checkAvailability();
    }

    public static boolean isConscrypt(Provider provider) {
        return provider instanceof OpenSSLProvider;
    }

    public static Provider newProvider() {
        Conscrypt.checkAvailability();
        return new OpenSSLProvider();
    }

    @Deprecated
    public static Provider newProvider(String string) {
        Conscrypt.checkAvailability();
        return Conscrypt.newProviderBuilder().setName(string).build();
    }

    public static ProviderBuilder newProviderBuilder() {
        return new ProviderBuilder();
    }

    public static int maxEncryptedPacketLength() {
        return 16709;
    }

    public static X509TrustManager getDefaultX509TrustManager() throws KeyManagementException {
        Conscrypt.checkAvailability();
        return SSLParametersImpl.getDefaultX509TrustManager();
    }

    public static boolean isConscrypt(SSLContext sSLContext) {
        return sSLContext.getProvider() instanceof OpenSSLProvider;
    }

    public static SSLContextSpi newPreferredSSLContextSpi() {
        Conscrypt.checkAvailability();
        return OpenSSLContextImpl.getPreferred();
    }

    public static void setClientSessionCache(SSLContext object, SSLClientSessionCache sSLClientSessionCache) {
        if (!((object = ((SSLContext)object).getClientSessionContext()) instanceof ClientSessionContext)) {
            throw new IllegalArgumentException("Not a conscrypt client context: " + object.getClass().getName());
        }
        ((ClientSessionContext)object).setPersistentCache(sSLClientSessionCache);
    }

    public static void setServerSessionCache(SSLContext object, SSLServerSessionCache sSLServerSessionCache) {
        if (!((object = ((SSLContext)object).getServerSessionContext()) instanceof ServerSessionContext)) {
            throw new IllegalArgumentException("Not a conscrypt client context: " + object.getClass().getName());
        }
        ((ServerSessionContext)object).setPersistentCache(sSLServerSessionCache);
    }

    public static boolean isConscrypt(SSLSocketFactory sSLSocketFactory) {
        return sSLSocketFactory instanceof OpenSSLSocketFactoryImpl;
    }

    private static OpenSSLSocketFactoryImpl toConscrypt(SSLSocketFactory sSLSocketFactory) {
        if (!Conscrypt.isConscrypt(sSLSocketFactory)) {
            throw new IllegalArgumentException("Not a conscrypt socket factory: " + sSLSocketFactory.getClass().getName());
        }
        return (OpenSSLSocketFactoryImpl)sSLSocketFactory;
    }

    public static void setUseEngineSocketByDefault(boolean bl) {
        OpenSSLSocketFactoryImpl.setUseEngineSocketByDefault(bl);
        OpenSSLServerSocketFactoryImpl.setUseEngineSocketByDefault(bl);
    }

    public static void setUseEngineSocket(SSLSocketFactory sSLSocketFactory, boolean bl) {
        Conscrypt.toConscrypt(sSLSocketFactory).setUseEngineSocket(bl);
    }

    public static boolean isConscrypt(SSLServerSocketFactory sSLServerSocketFactory) {
        return sSLServerSocketFactory instanceof OpenSSLServerSocketFactoryImpl;
    }

    private static OpenSSLServerSocketFactoryImpl toConscrypt(SSLServerSocketFactory sSLServerSocketFactory) {
        if (!Conscrypt.isConscrypt(sSLServerSocketFactory)) {
            throw new IllegalArgumentException("Not a conscrypt server socket factory: " + sSLServerSocketFactory.getClass().getName());
        }
        return (OpenSSLServerSocketFactoryImpl)sSLServerSocketFactory;
    }

    public static void setUseEngineSocket(SSLServerSocketFactory sSLServerSocketFactory, boolean bl) {
        Conscrypt.toConscrypt(sSLServerSocketFactory).setUseEngineSocket(bl);
    }

    public static boolean isConscrypt(SSLSocket sSLSocket) {
        return sSLSocket instanceof AbstractConscryptSocket;
    }

    private static AbstractConscryptSocket toConscrypt(SSLSocket sSLSocket) {
        if (!Conscrypt.isConscrypt(sSLSocket)) {
            throw new IllegalArgumentException("Not a conscrypt socket: " + sSLSocket.getClass().getName());
        }
        return (AbstractConscryptSocket)sSLSocket;
    }

    public static void setHostname(SSLSocket sSLSocket, String string) {
        Conscrypt.toConscrypt(sSLSocket).setHostname(string);
    }

    public static String getHostname(SSLSocket sSLSocket) {
        return Conscrypt.toConscrypt(sSLSocket).getHostname();
    }

    public static String getHostnameOrIP(SSLSocket sSLSocket) {
        return Conscrypt.toConscrypt(sSLSocket).getHostnameOrIP();
    }

    public static void setUseSessionTickets(SSLSocket sSLSocket, boolean bl) {
        Conscrypt.toConscrypt(sSLSocket).setUseSessionTickets(bl);
    }

    public static void setChannelIdEnabled(SSLSocket sSLSocket, boolean bl) {
        Conscrypt.toConscrypt(sSLSocket).setChannelIdEnabled(bl);
    }

    public static byte[] getChannelId(SSLSocket sSLSocket) throws SSLException {
        return Conscrypt.toConscrypt(sSLSocket).getChannelId();
    }

    public static void setChannelIdPrivateKey(SSLSocket sSLSocket, PrivateKey privateKey) {
        Conscrypt.toConscrypt(sSLSocket).setChannelIdPrivateKey(privateKey);
    }

    public static String getApplicationProtocol(SSLSocket sSLSocket) {
        return Conscrypt.toConscrypt(sSLSocket).getApplicationProtocol();
    }

    public static void setApplicationProtocolSelector(SSLSocket sSLSocket, ApplicationProtocolSelector applicationProtocolSelector) {
        Conscrypt.toConscrypt(sSLSocket).setApplicationProtocolSelector(applicationProtocolSelector);
    }

    public static void setApplicationProtocols(SSLSocket sSLSocket, String[] stringArray) {
        Conscrypt.toConscrypt(sSLSocket).setApplicationProtocols(stringArray);
    }

    public static String[] getApplicationProtocols(SSLSocket sSLSocket) {
        return Conscrypt.toConscrypt(sSLSocket).getApplicationProtocols();
    }

    public static byte[] getTlsUnique(SSLSocket sSLSocket) {
        return Conscrypt.toConscrypt(sSLSocket).getTlsUnique();
    }

    public static byte[] exportKeyingMaterial(SSLSocket sSLSocket, String string, byte[] byArray, int n2) throws SSLException {
        return Conscrypt.toConscrypt(sSLSocket).exportKeyingMaterial(string, byArray, n2);
    }

    public static boolean isConscrypt(SSLEngine sSLEngine) {
        return sSLEngine instanceof AbstractConscryptEngine;
    }

    private static AbstractConscryptEngine toConscrypt(SSLEngine sSLEngine) {
        if (!Conscrypt.isConscrypt(sSLEngine)) {
            throw new IllegalArgumentException("Not a conscrypt engine: " + sSLEngine.getClass().getName());
        }
        return (AbstractConscryptEngine)sSLEngine;
    }

    public static void setBufferAllocator(SSLEngine sSLEngine, BufferAllocator bufferAllocator) {
        Conscrypt.toConscrypt(sSLEngine).setBufferAllocator(bufferAllocator);
    }

    public static void setBufferAllocator(SSLSocket sSLSocket, BufferAllocator bufferAllocator) {
        if ((sSLSocket = Conscrypt.toConscrypt(sSLSocket)) instanceof ConscryptEngineSocket) {
            ((ConscryptEngineSocket)sSLSocket).setBufferAllocator(bufferAllocator);
        }
    }

    public static void setDefaultBufferAllocator(BufferAllocator bufferAllocator) {
        ConscryptEngine.setDefaultBufferAllocator(bufferAllocator);
    }

    public static void setHostname(SSLEngine sSLEngine, String string) {
        Conscrypt.toConscrypt(sSLEngine).setHostname(string);
    }

    public static String getHostname(SSLEngine sSLEngine) {
        return Conscrypt.toConscrypt(sSLEngine).getHostname();
    }

    public static int maxSealOverhead(SSLEngine sSLEngine) {
        return Conscrypt.toConscrypt(sSLEngine).maxSealOverhead();
    }

    public static void setHandshakeListener(SSLEngine sSLEngine, HandshakeListener handshakeListener) {
        Conscrypt.toConscrypt(sSLEngine).setHandshakeListener(handshakeListener);
    }

    public static void setChannelIdEnabled(SSLEngine sSLEngine, boolean bl) {
        Conscrypt.toConscrypt(sSLEngine).setChannelIdEnabled(bl);
    }

    public static byte[] getChannelId(SSLEngine sSLEngine) throws SSLException {
        return Conscrypt.toConscrypt(sSLEngine).getChannelId();
    }

    public static void setChannelIdPrivateKey(SSLEngine sSLEngine, PrivateKey privateKey) {
        Conscrypt.toConscrypt(sSLEngine).setChannelIdPrivateKey(privateKey);
    }

    public static SSLEngineResult unwrap(SSLEngine sSLEngine, ByteBuffer[] byteBufferArray, ByteBuffer[] byteBufferArray2) throws SSLException {
        return Conscrypt.toConscrypt(sSLEngine).unwrap(byteBufferArray, byteBufferArray2);
    }

    public static SSLEngineResult unwrap(SSLEngine sSLEngine, ByteBuffer[] byteBufferArray, int n2, int n3, ByteBuffer[] byteBufferArray2, int n4, int n5) throws SSLException {
        return Conscrypt.toConscrypt(sSLEngine).unwrap(byteBufferArray, n2, n3, byteBufferArray2, n4, n5);
    }

    public static void setUseSessionTickets(SSLEngine sSLEngine, boolean bl) {
        Conscrypt.toConscrypt(sSLEngine).setUseSessionTickets(bl);
    }

    public static void setApplicationProtocols(SSLEngine sSLEngine, String[] stringArray) {
        Conscrypt.toConscrypt(sSLEngine).setApplicationProtocols(stringArray);
    }

    public static String[] getApplicationProtocols(SSLEngine sSLEngine) {
        return Conscrypt.toConscrypt(sSLEngine).getApplicationProtocols();
    }

    public static void setApplicationProtocolSelector(SSLEngine sSLEngine, ApplicationProtocolSelector applicationProtocolSelector) {
        Conscrypt.toConscrypt(sSLEngine).setApplicationProtocolSelector(applicationProtocolSelector);
    }

    public static String getApplicationProtocol(SSLEngine sSLEngine) {
        return Conscrypt.toConscrypt(sSLEngine).getApplicationProtocol();
    }

    public static byte[] getTlsUnique(SSLEngine sSLEngine) {
        return Conscrypt.toConscrypt(sSLEngine).getTlsUnique();
    }

    public static byte[] exportKeyingMaterial(SSLEngine sSLEngine, String string, byte[] byArray, int n2) throws SSLException {
        return Conscrypt.toConscrypt(sSLEngine).exportKeyingMaterial(string, byArray, n2);
    }

    public static boolean isConscrypt(TrustManager trustManager) {
        return trustManager instanceof TrustManagerImpl;
    }

    private static TrustManagerImpl toConscrypt(TrustManager trustManager) {
        if (!Conscrypt.isConscrypt(trustManager)) {
            throw new IllegalArgumentException("Not a Conscrypt trust manager: " + trustManager.getClass().getName());
        }
        return (TrustManagerImpl)trustManager;
    }

    public static synchronized void setDefaultHostnameVerifier(ConscryptHostnameVerifier conscryptHostnameVerifier) {
        TrustManagerImpl.setDefaultHostnameVerifier(conscryptHostnameVerifier);
    }

    public static synchronized ConscryptHostnameVerifier getDefaultHostnameVerifier(TrustManager trustManager) {
        return TrustManagerImpl.getDefaultHostnameVerifier();
    }

    public static void setHostnameVerifier(TrustManager trustManager, ConscryptHostnameVerifier conscryptHostnameVerifier) {
        Conscrypt.toConscrypt(trustManager).setHostnameVerifier(conscryptHostnameVerifier);
    }

    public static ConscryptHostnameVerifier getHostnameVerifier(TrustManager trustManager) {
        return Conscrypt.toConscrypt(trustManager).getHostnameVerifier();
    }

    public static ConscryptHostnameVerifier wrapHostnameVerifier(final HostnameVerifier hostnameVerifier) {
        return new ConscryptHostnameVerifier(){

            @Override
            public boolean verify(X509Certificate[] x509CertificateArray, String string, SSLSession sSLSession) {
                return hostnameVerifier.verify(string, sSLSession);
            }
        };
    }

    static {
        InputStream inputStream2;
        int n2;
        int n3;
        int n4;
        block3: {
            n4 = -1;
            n3 = -1;
            n2 = -1;
            inputStream2 = null;
            try {
                inputStream2 = Conscrypt.class.getResourceAsStream("conscrypt.properties");
                if (inputStream2 == null) break block3;
                Properties properties = new Properties();
                properties.load(inputStream2);
                n4 = Integer.parseInt(properties.getProperty("org.conscrypt.version.major", "-1"));
                n3 = Integer.parseInt(properties.getProperty("org.conscrypt.version.minor", "-1"));
                n2 = Integer.parseInt(properties.getProperty("org.conscrypt.version.patch", "-1"));
            }
            catch (IOException iOException) {
                IoUtils.closeQuietly(inputStream2);
            }
            catch (Throwable throwable) {
                IoUtils.closeQuietly(inputStream2);
                throw throwable;
            }
        }
        IoUtils.closeQuietly(inputStream2);
        VERSION = n4 >= 0 && n3 >= 0 && n2 >= 0 ? new Version(n4, n3, n2) : null;
    }

    public static class ProviderBuilder {
        private String name = Platform.getDefaultProviderName();
        private boolean provideTrustManager = Platform.provideTrustManagerByDefault();
        private String defaultTlsProtocol = "TLSv1.3";

        private ProviderBuilder() {
        }

        public ProviderBuilder setName(String string) {
            this.name = string;
            return this;
        }

        @Deprecated
        public ProviderBuilder provideTrustManager() {
            return this.provideTrustManager(true);
        }

        public ProviderBuilder provideTrustManager(boolean bl) {
            this.provideTrustManager = bl;
            return this;
        }

        public ProviderBuilder defaultTlsProtocol(String string) {
            this.defaultTlsProtocol = string;
            return this;
        }

        public Provider build() {
            return new OpenSSLProvider(this.name, this.provideTrustManager, this.defaultTlsProtocol);
        }
    }

    public static class Version {
        private final int major;
        private final int minor;
        private final int patch;

        private Version(int n2, int n3, int n4) {
            this.major = n2;
            this.minor = n3;
            this.patch = n4;
        }

        public int major() {
            return this.major;
        }

        public int minor() {
            return this.minor;
        }

        public int patch() {
            return this.patch;
        }
    }
}


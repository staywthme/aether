/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECKey;
import java.security.spec.ECParameterSpec;
import javax.crypto.SecretKey;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509KeyManager;
import javax.net.ssl.X509TrustManager;
import javax.security.auth.x500.X500Principal;
import org.conscrypt.AbstractSessionContext;
import org.conscrypt.ActiveSession;
import org.conscrypt.ApplicationProtocolSelector;
import org.conscrypt.ApplicationProtocolSelectorAdapter;
import org.conscrypt.ArrayUtils;
import org.conscrypt.ClientSessionContext;
import org.conscrypt.ConscryptSession;
import org.conscrypt.ExternalSession;
import org.conscrypt.NativeCrypto;
import org.conscrypt.NativeRef;
import org.conscrypt.NativeSsl;
import org.conscrypt.NativeSslSession;
import org.conscrypt.OpenSSLECGroupContext;
import org.conscrypt.OpenSSLKey;
import org.conscrypt.OpenSSLSocketImpl;
import org.conscrypt.PSKKeyManager;
import org.conscrypt.Platform;
import org.conscrypt.SSLNullSession;
import org.conscrypt.SSLParametersImpl;
import org.conscrypt.SSLUtils;
import org.conscrypt.SessionSnapshot;

class ConscryptFileDescriptorSocket
extends OpenSSLSocketImpl
implements NativeCrypto.SSLHandshakeCallbacks,
SSLParametersImpl.AliasChooser,
SSLParametersImpl.PSKCallbacks {
    private static final boolean DBG_STATE = false;
    private int state = 0;
    private final NativeSsl ssl;
    private SSLInputStream is;
    private SSLOutputStream os;
    private final SSLParametersImpl sslParameters;
    private final Object guard = Platform.closeGuardGet();
    private OpenSSLKey channelIdPrivateKey;
    private final ActiveSession activeSession;
    private SessionSnapshot closedSession;
    private final SSLSession externalSession = Platform.wrapSSLSession(new ExternalSession(new ExternalSession.Provider(){

        @Override
        public ConscryptSession provideSession() {
            return ConscryptFileDescriptorSocket.this.provideSession();
        }
    }));
    private int writeTimeoutMilliseconds = 0;
    private int handshakeTimeoutMilliseconds = -1;

    ConscryptFileDescriptorSocket(SSLParametersImpl sSLParametersImpl) throws IOException {
        this.sslParameters = sSLParametersImpl;
        this.ssl = ConscryptFileDescriptorSocket.newSsl(sSLParametersImpl, this);
        this.activeSession = new ActiveSession(this.ssl, sSLParametersImpl.getSessionContext());
    }

    ConscryptFileDescriptorSocket(String string, int n2, SSLParametersImpl sSLParametersImpl) throws IOException {
        super(string, n2);
        this.sslParameters = sSLParametersImpl;
        this.ssl = ConscryptFileDescriptorSocket.newSsl(sSLParametersImpl, this);
        this.activeSession = new ActiveSession(this.ssl, sSLParametersImpl.getSessionContext());
    }

    ConscryptFileDescriptorSocket(InetAddress inetAddress, int n2, SSLParametersImpl sSLParametersImpl) throws IOException {
        super(inetAddress, n2);
        this.sslParameters = sSLParametersImpl;
        this.ssl = ConscryptFileDescriptorSocket.newSsl(sSLParametersImpl, this);
        this.activeSession = new ActiveSession(this.ssl, sSLParametersImpl.getSessionContext());
    }

    ConscryptFileDescriptorSocket(String string, int n2, InetAddress inetAddress, int n3, SSLParametersImpl sSLParametersImpl) throws IOException {
        super(string, n2, inetAddress, n3);
        this.sslParameters = sSLParametersImpl;
        this.ssl = ConscryptFileDescriptorSocket.newSsl(sSLParametersImpl, this);
        this.activeSession = new ActiveSession(this.ssl, sSLParametersImpl.getSessionContext());
    }

    ConscryptFileDescriptorSocket(InetAddress inetAddress, int n2, InetAddress inetAddress2, int n3, SSLParametersImpl sSLParametersImpl) throws IOException {
        super(inetAddress, n2, inetAddress2, n3);
        this.sslParameters = sSLParametersImpl;
        this.ssl = ConscryptFileDescriptorSocket.newSsl(sSLParametersImpl, this);
        this.activeSession = new ActiveSession(this.ssl, sSLParametersImpl.getSessionContext());
    }

    ConscryptFileDescriptorSocket(Socket socket, String string, int n2, boolean bl, SSLParametersImpl sSLParametersImpl) throws IOException {
        super(socket, string, n2, bl);
        this.sslParameters = sSLParametersImpl;
        this.ssl = ConscryptFileDescriptorSocket.newSsl(sSLParametersImpl, this);
        this.activeSession = new ActiveSession(this.ssl, sSLParametersImpl.getSessionContext());
    }

    private static NativeSsl newSsl(SSLParametersImpl sSLParametersImpl, ConscryptFileDescriptorSocket conscryptFileDescriptorSocket) throws SSLException {
        ConscryptFileDescriptorSocket conscryptFileDescriptorSocket2 = conscryptFileDescriptorSocket;
        return NativeSsl.newInstance(sSLParametersImpl, conscryptFileDescriptorSocket2, conscryptFileDescriptorSocket2, conscryptFileDescriptorSocket);
    }

    /*
     * Exception decompiling
     */
    @Override
    public final void startHandshake() throws IOException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [25[MONITOR], 36[CATCHBLOCK]], but top level block is 10[TRYBLOCK]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    @Override
    public final void clientCertificateRequested(byte[] byArray, int[] nArray, byte[][] byArray2) throws CertificateEncodingException, SSLException {
        this.ssl.chooseClientCertificate(byArray, nArray, byArray2);
    }

    @Override
    public final int clientPSKKeyRequested(String string, byte[] byArray, byte[] byArray2) {
        return this.ssl.clientPSKKeyRequested(string, byArray, byArray2);
    }

    @Override
    public final int serverPSKKeyRequested(String string, String string2, byte[] byArray) {
        return this.ssl.serverPSKKeyRequested(string, string2, byArray);
    }

    @Override
    public final void onSSLStateChange(int n2, int n3) {
        if (n2 != 32) {
            return;
        }
        NativeSsl nativeSsl = this.ssl;
        synchronized (nativeSsl) {
            if (this.state == 8) {
                return;
            }
            this.transitionTo(5);
        }
        this.notifyHandshakeCompletedListeners();
        nativeSsl = this.ssl;
        synchronized (nativeSsl) {
            this.ssl.notifyAll();
            return;
        }
    }

    @Override
    public final void onNewSessionEstablished(long l2) {
        try {
            NativeCrypto.SSL_SESSION_up_ref(l2);
            Object object = new NativeRef.SSL_SESSION(l2);
            object = NativeSslSession.newInstance((NativeRef.SSL_SESSION)object, this.activeSession);
            AbstractSessionContext abstractSessionContext = this.sessionContext();
            abstractSessionContext.cacheSession((NativeSslSession)object);
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    @Override
    public final long serverSessionRequested(byte[] byArray) {
        return 0L;
    }

    @Override
    public final void serverCertificateRequested() throws IOException {
        NativeSsl nativeSsl = this.ssl;
        synchronized (nativeSsl) {
            this.ssl.configureServerCertificate();
            return;
        }
    }

    @Override
    public final void verifyCertificateChain(byte[][] object, String string) throws CertificateException {
        try {
            if (object == null || ((byte[][])object).length == 0) {
                throw new CertificateException("Peer sent no certificate");
            }
            object = SSLUtils.decodeX509CertificateChain(object);
            X509TrustManager x509TrustManager = this.sslParameters.getX509TrustManager();
            if (x509TrustManager == null) {
                throw new CertificateException("No X.509 TrustManager");
            }
            this.activeSession.onPeerCertificatesReceived(this.getHostnameOrIP(), this.getPort(), (X509Certificate[])object);
            if (!this.getUseClientMode()) {
                string = object[0].getPublicKey().getAlgorithm();
                Platform.checkClientTrusted(x509TrustManager, (X509Certificate[])object, string, this);
                return;
            }
            Platform.checkServerTrusted(x509TrustManager, (X509Certificate[])object, string, this);
        }
        catch (CertificateException certificateException) {
            object = certificateException;
            throw certificateException;
        }
        catch (Exception exception) {
            throw new CertificateException(exception);
        }
    }

    @Override
    public final InputStream getInputStream() throws IOException {
        SSLInputStream sSLInputStream;
        this.checkOpen();
        NativeSsl nativeSsl = this.ssl;
        synchronized (nativeSsl) {
            if (this.state == 8) {
                throw new SocketException("Socket is closed.");
            }
            if (this.is == null) {
                this.is = new SSLInputStream();
            }
            sSLInputStream = this.is;
        }
        this.waitForHandshake();
        return sSLInputStream;
    }

    @Override
    public final OutputStream getOutputStream() throws IOException {
        SSLOutputStream sSLOutputStream;
        this.checkOpen();
        NativeSsl nativeSsl = this.ssl;
        synchronized (nativeSsl) {
            if (this.state == 8) {
                throw new SocketException("Socket is closed.");
            }
            if (this.os == null) {
                this.os = new SSLOutputStream();
            }
            sSLOutputStream = this.os;
        }
        this.waitForHandshake();
        return sSLOutputStream;
    }

    private void assertReadableOrWriteableState() {
        if (this.state == 5 || this.state == 4) {
            return;
        }
        throw new AssertionError((Object)("Invalid state: " + this.state));
    }

    private void waitForHandshake() throws IOException {
        this.startHandshake();
        NativeSsl nativeSsl = this.ssl;
        synchronized (nativeSsl) {
            while (this.state != 5 && this.state != 4 && this.state != 8) {
                try {
                    this.ssl.wait();
                }
                catch (InterruptedException interruptedException) {
                    Thread.currentThread().interrupt();
                    throw new IOException("Interrupted waiting for handshake", interruptedException);
                }
            }
            if (this.state == 8) {
                throw new SocketException("Socket is closed");
            }
            return;
        }
    }

    @Override
    public final SSLSession getSession() {
        return this.externalSession;
    }

    private ConscryptSession provideSession() {
        boolean bl = false;
        NativeSsl nativeSsl = this.ssl;
        synchronized (nativeSsl) {
            if (this.state == 8) {
                return this.closedSession != null ? this.closedSession : SSLNullSession.getNullSession();
            }
            try {
                bl = this.state >= 5;
                if (!bl && this.isConnected()) {
                    this.waitForHandshake();
                    bl = true;
                }
            }
            catch (IOException iOException) {}
        }
        if (!bl) {
            return SSLNullSession.getNullSession();
        }
        return this.activeSession;
    }

    private ConscryptSession provideAfterHandshakeSession() {
        if (this.state < 2) {
            return SSLNullSession.getNullSession();
        }
        return this.provideSession();
    }

    private ConscryptSession provideHandshakeSession() {
        NativeSsl nativeSsl = this.ssl;
        synchronized (nativeSsl) {
            return this.state >= 2 && this.state < 5 ? this.activeSession : SSLNullSession.getNullSession();
        }
    }

    @Override
    final SSLSession getActiveSession() {
        return this.activeSession;
    }

    @Override
    public final SSLSession getHandshakeSession() {
        NativeSsl nativeSsl = this.ssl;
        synchronized (nativeSsl) {
            if (this.state >= 2 && this.state < 5) {
                return Platform.wrapSSLSession(new ExternalSession(new ExternalSession.Provider(){

                    @Override
                    public ConscryptSession provideSession() {
                        return ConscryptFileDescriptorSocket.this.provideHandshakeSession();
                    }
                }));
            }
            return null;
        }
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
    public final String[] getSupportedCipherSuites() {
        return NativeCrypto.getSupportedCipherSuites();
    }

    @Override
    public final String[] getEnabledCipherSuites() {
        return this.sslParameters.getEnabledCipherSuites();
    }

    @Override
    public final void setEnabledCipherSuites(String[] stringArray) {
        this.sslParameters.setEnabledCipherSuites(stringArray);
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
    public final void setUseSessionTickets(boolean bl) {
        this.sslParameters.setUseSessionTickets(bl);
    }

    @Override
    public final void setHostname(String string) {
        this.sslParameters.setUseSni(string != null);
        super.setHostname(string);
    }

    @Override
    public final void setChannelIdEnabled(boolean bl) {
        if (this.getUseClientMode()) {
            throw new IllegalStateException("Client mode");
        }
        NativeSsl nativeSsl = this.ssl;
        synchronized (nativeSsl) {
            if (this.state != 0) {
                throw new IllegalStateException("Could not enable/disable Channel ID after the initial handshake has begun.");
            }
        }
        this.sslParameters.channelIdEnabled = bl;
    }

    @Override
    public final byte[] getChannelId() throws SSLException {
        if (this.getUseClientMode()) {
            throw new IllegalStateException("Client mode");
        }
        NativeSsl nativeSsl = this.ssl;
        synchronized (nativeSsl) {
            if (this.state != 5) {
                throw new IllegalStateException("Channel ID is only available after handshake completes");
            }
        }
        return this.ssl.getTlsChannelId();
    }

    @Override
    public final void setChannelIdPrivateKey(PrivateKey privateKey) {
        if (!this.getUseClientMode()) {
            throw new IllegalStateException("Server mode");
        }
        Object object = this.ssl;
        synchronized (object) {
            if (this.state != 0) {
                throw new IllegalStateException("Could not change Channel ID private key after the initial handshake has begun.");
            }
        }
        if (privateKey == null) {
            this.sslParameters.channelIdEnabled = false;
            this.channelIdPrivateKey = null;
            return;
        }
        this.sslParameters.channelIdEnabled = true;
        try {
            object = null;
            if (privateKey instanceof ECKey) {
                object = ((ECKey)((Object)privateKey)).getParams();
            }
            if (object == null) {
                object = OpenSSLECGroupContext.getCurveByName("prime256v1").getECParameterSpec();
            }
            this.channelIdPrivateKey = OpenSSLKey.fromECPrivateKeyForTLSStackOnly(privateKey, (ECParameterSpec)object);
            return;
        }
        catch (InvalidKeyException invalidKeyException) {
            return;
        }
    }

    @Override
    byte[] getTlsUnique() {
        return this.ssl.getTlsUnique();
    }

    @Override
    byte[] exportKeyingMaterial(String string, byte[] byArray, int n2) throws SSLException {
        NativeSsl nativeSsl = this.ssl;
        synchronized (nativeSsl) {
            if (this.state < 3 || this.state == 8) {
                return null;
            }
        }
        return this.ssl.exportKeyingMaterial(string, byArray, n2);
    }

    @Override
    public final boolean getUseClientMode() {
        return this.sslParameters.getUseClientMode();
    }

    @Override
    public final void setUseClientMode(boolean bl) {
        NativeSsl nativeSsl = this.ssl;
        synchronized (nativeSsl) {
            if (this.state != 0) {
                throw new IllegalArgumentException("Could not change the mode after the initial handshake has begun.");
            }
        }
        this.sslParameters.setUseClientMode(bl);
    }

    @Override
    public final boolean getWantClientAuth() {
        return this.sslParameters.getWantClientAuth();
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
    public final void setWantClientAuth(boolean bl) {
        this.sslParameters.setWantClientAuth(bl);
    }

    @Override
    public final void setSoWriteTimeout(int n2) throws SocketException {
        this.writeTimeoutMilliseconds = n2;
        Platform.setSocketWriteTimeout(this, n2);
    }

    @Override
    public final int getSoWriteTimeout() throws SocketException {
        return this.writeTimeoutMilliseconds;
    }

    @Override
    public final void setHandshakeTimeout(int n2) throws SocketException {
        this.handshakeTimeoutMilliseconds = n2;
    }

    @Override
    public final void close() throws IOException {
        SSLOutputStream sSLOutputStream;
        SSLInputStream sSLInputStream;
        if (this.ssl == null) {
            return;
        }
        NativeSsl nativeSsl = this.ssl;
        synchronized (nativeSsl) {
            if (this.state == 8) {
                return;
            }
            int n2 = this.state;
            this.transitionTo(8);
            if (n2 == 0) {
                this.free();
                this.closeUnderlyingSocket();
                this.ssl.notifyAll();
                return;
            }
            if (n2 != 5 && n2 != 4) {
                this.ssl.interrupt();
                this.ssl.notifyAll();
                return;
            }
            this.ssl.notifyAll();
            sSLInputStream = this.is;
            sSLOutputStream = this.os;
        }
        if (sSLInputStream != null || sSLOutputStream != null) {
            this.ssl.interrupt();
        }
        if (sSLInputStream != null) {
            sSLInputStream.awaitPendingOps();
        }
        if (sSLOutputStream != null) {
            sSLOutputStream.awaitPendingOps();
        }
        this.shutdownAndFreeSslNative();
    }

    private void shutdownAndFreeSslNative() throws IOException {
        try {
            Platform.blockGuardOnNetwork();
            this.ssl.shutdown(Platform.getFileDescriptor(this.socket));
            return;
        }
        catch (IOException iOException) {
            return;
        }
        finally {
            this.free();
            this.closeUnderlyingSocket();
        }
    }

    private void closeUnderlyingSocket() throws IOException {
        super.close();
    }

    private void free() {
        if (!this.ssl.isClosed()) {
            this.ssl.close();
            Platform.closeGuardClose(this.guard);
        }
    }

    protected final void finalize() throws Throwable {
        try {
            if (this.guard != null) {
                Platform.closeGuardWarnIfOpen(this.guard);
            }
            if (this.ssl != null) {
                NativeSsl nativeSsl = this.ssl;
                synchronized (nativeSsl) {
                    this.transitionTo(8);
                }
            }
            return;
        }
        finally {
            super.finalize();
        }
    }

    @Override
    public final void setApplicationProtocolSelector(ApplicationProtocolSelector applicationProtocolSelector) {
        this.setApplicationProtocolSelector(applicationProtocolSelector == null ? null : new ApplicationProtocolSelectorAdapter(this, applicationProtocolSelector));
    }

    @Override
    final void setApplicationProtocolSelector(ApplicationProtocolSelectorAdapter applicationProtocolSelectorAdapter) {
        this.sslParameters.setApplicationProtocolSelector(applicationProtocolSelectorAdapter);
    }

    @Override
    public int selectApplicationProtocol(byte[] byArray) {
        ApplicationProtocolSelectorAdapter applicationProtocolSelectorAdapter = this.sslParameters.getApplicationProtocolSelector();
        if (applicationProtocolSelectorAdapter == null) {
            return 3;
        }
        return applicationProtocolSelectorAdapter.selectApplicationProtocol(byArray);
    }

    @Override
    final void setApplicationProtocols(String[] stringArray) {
        this.sslParameters.setApplicationProtocols(stringArray);
    }

    @Override
    final String[] getApplicationProtocols() {
        return this.sslParameters.getApplicationProtocols();
    }

    @Override
    public final String getApplicationProtocol() {
        return this.provideAfterHandshakeSession().getApplicationProtocol();
    }

    @Override
    public final String getHandshakeApplicationProtocol() {
        NativeSsl nativeSsl = this.ssl;
        synchronized (nativeSsl) {
            return this.state >= 2 && this.state < 5 ? this.getApplicationProtocol() : null;
        }
    }

    @Override
    public final SSLParameters getSSLParameters() {
        SSLParameters sSLParameters = super.getSSLParameters();
        Platform.getSSLParameters(sSLParameters, this.sslParameters, this);
        return sSLParameters;
    }

    @Override
    public final void setSSLParameters(SSLParameters sSLParameters) {
        super.setSSLParameters(sSLParameters);
        Platform.setSSLParameters(sSLParameters, this.sslParameters, this);
    }

    @Override
    public final String chooseServerPSKIdentityHint(PSKKeyManager pSKKeyManager) {
        return pSKKeyManager.chooseServerKeyIdentityHint(this);
    }

    @Override
    public final String chooseClientPSKIdentity(PSKKeyManager pSKKeyManager, String string) {
        return pSKKeyManager.chooseClientKeyIdentity(string, this);
    }

    @Override
    public final SecretKey getPSKKey(PSKKeyManager pSKKeyManager, String string, String string2) {
        return pSKKeyManager.getKey(string, string2, this);
    }

    @Override
    public final String chooseServerAlias(X509KeyManager x509KeyManager, String string) {
        return x509KeyManager.chooseServerAlias(string, null, this);
    }

    @Override
    public final String chooseClientAlias(X509KeyManager x509KeyManager, X500Principal[] x500PrincipalArray, String[] stringArray) {
        return x509KeyManager.chooseClientAlias(stringArray, x500PrincipalArray, this);
    }

    private ClientSessionContext clientSessionContext() {
        return this.sslParameters.getClientSessionContext();
    }

    private AbstractSessionContext sessionContext() {
        return this.sslParameters.getSessionContext();
    }

    private void transitionTo(int n2) {
        switch (n2) {
            case 8: {
                if (this.ssl.isClosed() || this.state < 2 || this.state >= 8) break;
                this.closedSession = new SessionSnapshot(this.activeSession);
            }
        }
        this.state = n2;
    }

    class SSLOutputStream
    extends OutputStream {
        private final Object writeLock = new Object();

        SSLOutputStream() {
        }

        @Override
        public void write(int n2) throws IOException {
            byte[] byArray = new byte[1];
            byte[] byArray2 = byArray;
            byArray[0] = (byte)n2;
            this.write(byArray2);
        }

        @Override
        public void write(byte[] byArray, int n2, int n3) throws IOException {
            Platform.blockGuardOnNetwork();
            ConscryptFileDescriptorSocket.this.checkOpen();
            ArrayUtils.checkOffsetAndCount(byArray.length, n2, n3);
            if (n3 == 0) {
                return;
            }
            Object object = this.writeLock;
            synchronized (object) {
                NativeSsl nativeSsl = ConscryptFileDescriptorSocket.this.ssl;
                synchronized (nativeSsl) {
                    if (ConscryptFileDescriptorSocket.this.state == 8) {
                        throw new SocketException("socket is closed");
                    }
                }
                ConscryptFileDescriptorSocket.this.ssl.write(Platform.getFileDescriptor(ConscryptFileDescriptorSocket.this.socket), byArray, n2, n3, ConscryptFileDescriptorSocket.this.writeTimeoutMilliseconds);
                nativeSsl = ConscryptFileDescriptorSocket.this.ssl;
                synchronized (nativeSsl) {
                    if (ConscryptFileDescriptorSocket.this.state == 8) {
                        throw new SocketException("socket is closed");
                    }
                }
                return;
            }
        }

        /*
         * Enabled aggressive block sorting
         * Converted monitor instructions to comments
         * Lifted jumps to return sites
         */
        void awaitPendingOps() {
            Object object = this.writeLock;
            // MONITORENTER : object
            // MONITOREXIT : object
        }
    }

    class SSLInputStream
    extends InputStream {
        private final Object readLock = new Object();

        SSLInputStream() {
        }

        @Override
        public int read() throws IOException {
            byte[] byArray = new byte[1];
            int n2 = this.read(byArray, 0, 1);
            if (n2 != -1) {
                return byArray[0] & 0xFF;
            }
            return -1;
        }

        /*
         * WARNING - void declaration
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Converted monitor instructions to comments
         * Lifted jumps to return sites
         */
        @Override
        public int read(byte[] object, int n2, int n3) throws IOException {
            void var3_3;
            void var2_2;
            Platform.blockGuardOnNetwork();
            ConscryptFileDescriptorSocket.this.checkOpen();
            ArrayUtils.checkOffsetAndCount(((byte[])object).length, (int)var2_2, (int)var3_3);
            if (var3_3 == false) {
                return 0;
            }
            Object object2 = this.readLock;
            // MONITORENTER : object2
            NativeSsl nativeSsl = ConscryptFileDescriptorSocket.this.ssl;
            // MONITORENTER : nativeSsl
            if (ConscryptFileDescriptorSocket.this.state == 8) {
                throw new SocketException("socket is closed");
            }
            // MONITOREXIT : nativeSsl
            int n4 = ConscryptFileDescriptorSocket.this.ssl.read(Platform.getFileDescriptor(ConscryptFileDescriptorSocket.this.socket), (byte[])object, (int)var2_2, (int)var3_3, ConscryptFileDescriptorSocket.this.getSoTimeout());
            if (n4 == -1) {
                NativeSsl nativeSsl2 = ConscryptFileDescriptorSocket.this.ssl;
                object = nativeSsl2;
                // MONITORENTER : nativeSsl2
                if (ConscryptFileDescriptorSocket.this.state == 8) {
                    throw new SocketException("socket is closed");
                }
                // MONITOREXIT : object
            }
            // MONITOREXIT : object2
            return n4;
        }

        @Override
        public int available() {
            return ConscryptFileDescriptorSocket.this.ssl.getPendingReadableBytes();
        }

        /*
         * Enabled aggressive block sorting
         * Converted monitor instructions to comments
         * Lifted jumps to return sites
         */
        void awaitPendingOps() {
            Object object = this.readLock;
            // MONITORENTER : object
            // MONITOREXIT : object
        }
    }
}


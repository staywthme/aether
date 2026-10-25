/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509ExtendedTrustManager;
import javax.net.ssl.X509KeyManager;
import javax.net.ssl.X509TrustManager;
import javax.security.auth.x500.X500Principal;
import org.conscrypt.AllocatedBuffer;
import org.conscrypt.ApplicationProtocolSelector;
import org.conscrypt.ApplicationProtocolSelectorAdapter;
import org.conscrypt.BufferAllocator;
import org.conscrypt.ConscryptEngine;
import org.conscrypt.EmptyArray;
import org.conscrypt.HandshakeListener;
import org.conscrypt.OpenSSLSocketImpl;
import org.conscrypt.Platform;
import org.conscrypt.SSLParametersImpl;
import org.conscrypt.SSLUtils;

class ConscryptEngineSocket
extends OpenSSLSocketImpl
implements SSLParametersImpl.AliasChooser {
    private static final ByteBuffer EMPTY_BUFFER = ByteBuffer.allocate(0);
    private final ConscryptEngine engine;
    private final Object stateLock = new Object();
    private final Object handshakeLock = new Object();
    private SSLOutputStream out;
    private SSLInputStream in;
    private BufferAllocator bufferAllocator = ConscryptEngine.getDefaultBufferAllocator();
    private int state = 0;

    ConscryptEngineSocket(SSLParametersImpl sSLParametersImpl) throws IOException {
        this.engine = ConscryptEngineSocket.newEngine(sSLParametersImpl, this);
    }

    ConscryptEngineSocket(String string, int n2, SSLParametersImpl sSLParametersImpl) throws IOException {
        super(string, n2);
        this.engine = ConscryptEngineSocket.newEngine(sSLParametersImpl, this);
    }

    ConscryptEngineSocket(InetAddress inetAddress, int n2, SSLParametersImpl sSLParametersImpl) throws IOException {
        super(inetAddress, n2);
        this.engine = ConscryptEngineSocket.newEngine(sSLParametersImpl, this);
    }

    ConscryptEngineSocket(String string, int n2, InetAddress inetAddress, int n3, SSLParametersImpl sSLParametersImpl) throws IOException {
        super(string, n2, inetAddress, n3);
        this.engine = ConscryptEngineSocket.newEngine(sSLParametersImpl, this);
    }

    ConscryptEngineSocket(InetAddress inetAddress, int n2, InetAddress inetAddress2, int n3, SSLParametersImpl sSLParametersImpl) throws IOException {
        super(inetAddress, n2, inetAddress2, n3);
        this.engine = ConscryptEngineSocket.newEngine(sSLParametersImpl, this);
    }

    ConscryptEngineSocket(Socket socket, String string, int n2, boolean bl, SSLParametersImpl sSLParametersImpl) throws IOException {
        super(socket, string, n2, bl);
        this.engine = ConscryptEngineSocket.newEngine(sSLParametersImpl, this);
    }

    private static ConscryptEngine newEngine(SSLParametersImpl sSLParametersImpl, ConscryptEngineSocket conscryptEngineSocket) {
        Object object;
        if (Platform.supportsX509ExtendedTrustManager()) {
            SSLParametersImpl sSLParametersImpl2 = sSLParametersImpl;
            object = sSLParametersImpl2.cloneWithTrustManager(ConscryptEngineSocket.getDelegatingTrustManager(sSLParametersImpl2.getX509TrustManager(), conscryptEngineSocket));
        } else {
            object = sSLParametersImpl;
        }
        object = new ConscryptEngine((SSLParametersImpl)object, conscryptEngineSocket.peerInfoProvider(), conscryptEngineSocket);
        ((ConscryptEngine)object).setHandshakeListener(new HandshakeListener(){

            @Override
            public void onHandshakeFinished() {
                ConscryptEngineSocket.this.onHandshakeFinished();
            }
        });
        ((ConscryptEngine)object).setUseClientMode(sSLParametersImpl.getUseClientMode());
        return object;
    }

    private static X509TrustManager getDelegatingTrustManager(X509TrustManager x509TrustManager, ConscryptEngineSocket conscryptEngineSocket) {
        if (x509TrustManager instanceof X509ExtendedTrustManager) {
            x509TrustManager = (X509ExtendedTrustManager)x509TrustManager;
            return new X509ExtendedTrustManager((X509ExtendedTrustManager)x509TrustManager, conscryptEngineSocket){
                final /* synthetic */ X509ExtendedTrustManager val$extendedDelegate;
                final /* synthetic */ ConscryptEngineSocket val$socket;
                {
                    this.val$extendedDelegate = x509ExtendedTrustManager;
                    this.val$socket = conscryptEngineSocket;
                }

                @Override
                public void checkClientTrusted(X509Certificate[] x509CertificateArray, String string, Socket socket) throws CertificateException {
                    throw new AssertionError((Object)"Should not be called");
                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509CertificateArray, String string, Socket socket) throws CertificateException {
                    throw new AssertionError((Object)"Should not be called");
                }

                @Override
                public void checkClientTrusted(X509Certificate[] x509CertificateArray, String string, SSLEngine sSLEngine) throws CertificateException {
                    this.val$extendedDelegate.checkClientTrusted(x509CertificateArray, string, this.val$socket);
                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509CertificateArray, String string, SSLEngine sSLEngine) throws CertificateException {
                    this.val$extendedDelegate.checkServerTrusted(x509CertificateArray, string, this.val$socket);
                }

                @Override
                public void checkClientTrusted(X509Certificate[] x509CertificateArray, String string) throws CertificateException {
                    this.val$extendedDelegate.checkClientTrusted(x509CertificateArray, string);
                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509CertificateArray, String string) throws CertificateException {
                    this.val$extendedDelegate.checkServerTrusted(x509CertificateArray, string);
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return this.val$extendedDelegate.getAcceptedIssuers();
                }
            };
        }
        return x509TrustManager;
    }

    @Override
    public final SSLParameters getSSLParameters() {
        return this.engine.getSSLParameters();
    }

    @Override
    public final void setSSLParameters(SSLParameters sSLParameters) {
        this.engine.setSSLParameters(sSLParameters);
    }

    @Override
    public final void startHandshake() throws IOException {
        this.checkOpen();
        try {
            Object object = this.handshakeLock;
            synchronized (object) {
                Object object2 = this.stateLock;
                synchronized (object2) {
                    if (this.state != 0) {
                        return;
                    }
                    this.state = 2;
                    this.engine.beginHandshake();
                    this.in = new SSLInputStream();
                    this.out = new SSLOutputStream();
                }
                this.doHandshake();
            }
        }
        catch (SSLException sSLException) {
            ((OpenSSLSocketImpl)this).close();
            throw sSLException;
        }
        catch (IOException iOException) {
            ((OpenSSLSocketImpl)this).close();
            throw iOException;
        }
        catch (Exception exception) {
            ((OpenSSLSocketImpl)this).close();
            throw SSLUtils.toSSLHandshakeException(exception);
        }
    }

    private void doHandshake() throws IOException {
        try {
            boolean bl = false;
            block10: while (!bl) {
                switch (this.engine.getHandshakeStatus()) {
                    case NEED_UNWRAP: {
                        if (this.in.processDataFromSocket(EmptyArray.BYTE, 0, 0) >= 0) continue block10;
                        throw SSLUtils.toSSLHandshakeException(new EOFException("connection closed"));
                    }
                    case NEED_WRAP: {
                        this.out.writeInternal(ConscryptEngineSocket.EMPTY_BUFFER);
                        this.out.flushInternal();
                        continue block10;
                    }
                    case NEED_TASK: {
                        throw new IllegalStateException("Engine tasks are unsupported");
                    }
                    case NOT_HANDSHAKING: 
                    case FINISHED: {
                        bl = true;
                        continue block10;
                    }
                }
                throw new IllegalStateException("Unknown handshake status: " + (Object)((Object)this.engine.getHandshakeStatus()));
            }
            return;
        }
        catch (SSLException sSLException) {
            this.drainOutgoingQueue();
            ((OpenSSLSocketImpl)this).close();
            throw sSLException;
        }
        catch (IOException iOException) {
            ((OpenSSLSocketImpl)this).close();
            throw iOException;
        }
        catch (Exception exception) {
            ((OpenSSLSocketImpl)this).close();
            throw SSLUtils.toSSLHandshakeException(exception);
        }
    }

    @Override
    public final InputStream getInputStream() throws IOException {
        this.checkOpen();
        this.waitForHandshake();
        return this.in;
    }

    @Override
    public final OutputStream getOutputStream() throws IOException {
        this.checkOpen();
        this.waitForHandshake();
        return this.out;
    }

    @Override
    public final SSLSession getHandshakeSession() {
        return this.engine.handshakeSession();
    }

    @Override
    public final SSLSession getSession() {
        if (this.isConnected()) {
            try {
                this.waitForHandshake();
            }
            catch (IOException iOException) {}
        }
        return this.engine.getSession();
    }

    @Override
    final SSLSession getActiveSession() {
        return this.engine.getSession();
    }

    @Override
    public final boolean getEnableSessionCreation() {
        return this.engine.getEnableSessionCreation();
    }

    @Override
    public final void setEnableSessionCreation(boolean bl) {
        this.engine.setEnableSessionCreation(bl);
    }

    @Override
    public final String[] getSupportedCipherSuites() {
        return this.engine.getSupportedCipherSuites();
    }

    @Override
    public final String[] getEnabledCipherSuites() {
        return this.engine.getEnabledCipherSuites();
    }

    @Override
    public final void setEnabledCipherSuites(String[] stringArray) {
        this.engine.setEnabledCipherSuites(stringArray);
    }

    @Override
    public final String[] getSupportedProtocols() {
        return this.engine.getSupportedProtocols();
    }

    @Override
    public final String[] getEnabledProtocols() {
        return this.engine.getEnabledProtocols();
    }

    @Override
    public final void setEnabledProtocols(String[] stringArray) {
        this.engine.setEnabledProtocols(stringArray);
    }

    @Override
    public final void setHostname(String string) {
        this.engine.setHostname(string);
        super.setHostname(string);
    }

    @Override
    public final void setUseSessionTickets(boolean bl) {
        this.engine.setUseSessionTickets(bl);
    }

    @Override
    public final void setChannelIdEnabled(boolean bl) {
        this.engine.setChannelIdEnabled(bl);
    }

    @Override
    public final byte[] getChannelId() throws SSLException {
        return this.engine.getChannelId();
    }

    @Override
    public final void setChannelIdPrivateKey(PrivateKey privateKey) {
        this.engine.setChannelIdPrivateKey(privateKey);
    }

    @Override
    byte[] getTlsUnique() {
        return this.engine.getTlsUnique();
    }

    @Override
    byte[] exportKeyingMaterial(String string, byte[] byArray, int n2) throws SSLException {
        return this.engine.exportKeyingMaterial(string, byArray, n2);
    }

    @Override
    public final boolean getUseClientMode() {
        return this.engine.getUseClientMode();
    }

    @Override
    public final void setUseClientMode(boolean bl) {
        this.engine.setUseClientMode(bl);
    }

    @Override
    public final boolean getWantClientAuth() {
        return this.engine.getWantClientAuth();
    }

    @Override
    public final boolean getNeedClientAuth() {
        return this.engine.getNeedClientAuth();
    }

    @Override
    public final void setNeedClientAuth(boolean bl) {
        this.engine.setNeedClientAuth(bl);
    }

    @Override
    public final void setWantClientAuth(boolean bl) {
        this.engine.setWantClientAuth(bl);
    }

    @Override
    public final void close() throws IOException {
        block16: {
            block15: {
                block14: {
                    int n2;
                    if (this.stateLock == null) {
                        return;
                    }
                    Object object = this.stateLock;
                    synchronized (object) {
                        n2 = this.state;
                        if (this.state == 8) {
                            return;
                        }
                        this.state = 8;
                        this.stateLock.notifyAll();
                    }
                    try {
                        this.engine.closeInbound();
                        this.engine.closeOutbound();
                        if (n2 < 2) break block14;
                        this.drainOutgoingQueue();
                        this.engine.closeOutbound();
                    }
                    catch (Throwable throwable) {
                        try {
                            super.close();
                        }
                        finally {
                            if (this.in != null) {
                                this.in.release();
                            }
                        }
                        throw throwable;
                    }
                }
                try {
                    super.close();
                    if (this.in == null) break block15;
                    this.in.release();
                    break block16;
                }
                catch (Throwable throwable) {
                    if (this.in != null) {
                        this.in.release();
                    }
                    throw throwable;
                }
            }
            return;
        }
    }

    @Override
    public void setHandshakeTimeout(int n2) throws SocketException {
    }

    @Override
    final void setApplicationProtocols(String[] stringArray) {
        this.engine.setApplicationProtocols(stringArray);
    }

    @Override
    final String[] getApplicationProtocols() {
        return this.engine.getApplicationProtocols();
    }

    @Override
    public final String getApplicationProtocol() {
        return this.engine.getApplicationProtocol();
    }

    @Override
    public final String getHandshakeApplicationProtocol() {
        return this.engine.getHandshakeApplicationProtocol();
    }

    @Override
    public final void setApplicationProtocolSelector(ApplicationProtocolSelector applicationProtocolSelector) {
        this.setApplicationProtocolSelector(applicationProtocolSelector == null ? null : new ApplicationProtocolSelectorAdapter(this, applicationProtocolSelector));
    }

    @Override
    final void setApplicationProtocolSelector(ApplicationProtocolSelectorAdapter applicationProtocolSelectorAdapter) {
        this.engine.setApplicationProtocolSelector(applicationProtocolSelectorAdapter);
    }

    void setBufferAllocator(BufferAllocator bufferAllocator) {
        this.engine.setBufferAllocator(bufferAllocator);
        this.bufferAllocator = bufferAllocator;
    }

    private void onHandshakeFinished() {
        boolean bl = false;
        Object object = this.stateLock;
        synchronized (object) {
            if (this.state != 8) {
                if (this.state == 2) {
                    this.state = 4;
                } else if (this.state == 3) {
                    this.state = 5;
                }
                this.stateLock.notifyAll();
                bl = true;
            }
        }
        if (bl) {
            this.notifyHandshakeCompletedListeners();
        }
    }

    private void waitForHandshake() throws IOException {
        this.startHandshake();
        Object object = this.stateLock;
        synchronized (object) {
            while (this.state != 5 && this.state != 4 && this.state != 8) {
                try {
                    this.stateLock.wait();
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

    private void drainOutgoingQueue() {
        try {
            while (this.engine.pendingOutboundEncryptedBytes() > 0) {
                this.out.writeInternal(ConscryptEngineSocket.EMPTY_BUFFER);
                this.out.flushInternal();
            }
            return;
        }
        catch (IOException iOException) {
            return;
        }
    }

    private OutputStream getUnderlyingOutputStream() throws IOException {
        return super.getOutputStream();
    }

    private InputStream getUnderlyingInputStream() throws IOException {
        return super.getInputStream();
    }

    @Override
    public final String chooseServerAlias(X509KeyManager x509KeyManager, String string) {
        return x509KeyManager.chooseServerAlias(string, null, this);
    }

    @Override
    public final String chooseClientAlias(X509KeyManager x509KeyManager, X500Principal[] x500PrincipalArray, String[] stringArray) {
        return x509KeyManager.chooseClientAlias(stringArray, x500PrincipalArray, this);
    }

    final class SSLInputStream
    extends InputStream {
        private final Object readLock = new Object();
        private final byte[] singleByte = new byte[1];
        private final ByteBuffer fromEngine;
        private final ByteBuffer fromSocket;
        private final int fromSocketArrayOffset;
        private final AllocatedBuffer allocatedBuffer;
        private InputStream socketInputStream;

        SSLInputStream() {
            if (ConscryptEngineSocket.this.bufferAllocator != null) {
                this.allocatedBuffer = ConscryptEngineSocket.this.bufferAllocator.allocateDirectBuffer(ConscryptEngineSocket.this.engine.getSession().getApplicationBufferSize());
                this.fromEngine = this.allocatedBuffer.nioBuffer();
            } else {
                this.allocatedBuffer = null;
                this.fromEngine = ByteBuffer.allocateDirect(ConscryptEngineSocket.this.engine.getSession().getApplicationBufferSize());
            }
            this.fromEngine.flip();
            this.fromSocket = ByteBuffer.allocate(ConscryptEngineSocket.this.engine.getSession().getPacketBufferSize());
            this.fromSocketArrayOffset = this.fromSocket.arrayOffset();
        }

        @Override
        public final void close() throws IOException {
            ((OpenSSLSocketImpl)ConscryptEngineSocket.this).close();
        }

        final void release() {
            Object object = this.readLock;
            synchronized (object) {
                if (this.allocatedBuffer != null) {
                    this.allocatedBuffer.release();
                }
                return;
            }
        }

        @Override
        public final int read() throws IOException {
            ConscryptEngineSocket.this.startHandshake();
            Object object = this.readLock;
            synchronized (object) {
                SSLInputStream sSLInputStream = this;
                int n2 = sSLInputStream.read(sSLInputStream.singleByte, 0, 1);
                if (n2 == -1) {
                    return -1;
                }
                if (n2 != 1) {
                    throw new SSLException("read incorrect number of bytes " + n2);
                }
                return this.singleByte[0] & 0xFF;
            }
        }

        @Override
        public final int read(byte[] byArray) throws IOException {
            ConscryptEngineSocket.this.startHandshake();
            Object object = this.readLock;
            synchronized (object) {
                return this.read(byArray, 0, byArray.length);
            }
        }

        @Override
        public final int read(byte[] byArray, int n2, int n3) throws IOException {
            ConscryptEngineSocket.this.startHandshake();
            Object object = this.readLock;
            synchronized (object) {
                return this.readUntilDataAvailable(byArray, n2, n3);
            }
        }

        @Override
        public final int available() throws IOException {
            ConscryptEngineSocket.this.startHandshake();
            Object object = this.readLock;
            synchronized (object) {
                this.init();
                return this.fromEngine.remaining();
            }
        }

        private boolean isHandshaking(SSLEngineResult.HandshakeStatus handshakeStatus) {
            switch (handshakeStatus) {
                case NEED_UNWRAP: 
                case NEED_WRAP: 
                case NEED_TASK: {
                    return true;
                }
            }
            return false;
        }

        private int readUntilDataAvailable(byte[] byArray, int n2, int n3) throws IOException {
            int n4;
            while ((n4 = this.processDataFromSocket(byArray, n2, n3)) == 0) {
            }
            return n4;
        }

        private int processDataFromSocket(byte[] byArray, int n2, int n3) throws IOException {
            int n4;
            Platform.blockGuardOnNetwork();
            ConscryptEngineSocket.this.checkOpen();
            this.init();
            do {
                if (this.fromEngine.remaining() > 0) {
                    n4 = Math.min(this.fromEngine.remaining(), n3);
                    this.fromEngine.get(byArray, n2, n4);
                    return n4;
                }
                n4 = 1;
                this.fromSocket.flip();
                this.fromEngine.clear();
                SSLInputStream sSLInputStream = this;
                boolean bl = sSLInputStream.isHandshaking(sSLInputStream.ConscryptEngineSocket.this.engine.getHandshakeStatus());
                SSLEngineResult sSLEngineResult = ConscryptEngineSocket.this.engine.unwrap(this.fromSocket, this.fromEngine);
                this.fromSocket.compact();
                this.fromEngine.flip();
                switch (sSLEngineResult.getStatus()) {
                    case BUFFER_UNDERFLOW: {
                        if (sSLEngineResult.bytesProduced() == 0) break;
                        n4 = 0;
                        break;
                    }
                    case OK: {
                        if (!bl && this.isHandshaking(sSLEngineResult.getHandshakeStatus()) && this.isHandshakeFinished()) {
                            this.renegotiate();
                            return 0;
                        }
                        n4 = 0;
                        break;
                    }
                    case CLOSED: {
                        return -1;
                    }
                    default: {
                        throw new SSLException("Unexpected engine result " + (Object)((Object)sSLEngineResult.getStatus()));
                    }
                }
                if (n4 != 0 || sSLEngineResult.bytesProduced() != 0) continue;
                return 0;
            } while (n4 == 0 || this.readFromSocket() != -1);
            return -1;
        }

        private boolean isHandshakeFinished() {
            Object object = ConscryptEngineSocket.this.stateLock;
            synchronized (object) {
                return ConscryptEngineSocket.this.state >= 4;
            }
        }

        private void renegotiate() throws IOException {
            Object object = ConscryptEngineSocket.this.handshakeLock;
            synchronized (object) {
                ConscryptEngineSocket.this.doHandshake();
                return;
            }
        }

        private void init() throws IOException {
            if (this.socketInputStream == null) {
                this.socketInputStream = ConscryptEngineSocket.this.getUnderlyingInputStream();
            }
        }

        private int readFromSocket() throws IOException {
            try {
                int n2 = this.fromSocket.position();
                int n3 = this.fromSocket.limit();
                n3 = this.socketInputStream.read(this.fromSocket.array(), this.fromSocketArrayOffset + n2, n3 - n2);
                if (n3 > 0) {
                    this.fromSocket.position(n2 + n3);
                }
                return n3;
            }
            catch (EOFException eOFException) {
                return -1;
            }
        }
    }

    final class SSLOutputStream
    extends OutputStream {
        private final Object writeLock = new Object();
        private final ByteBuffer target;
        private final int targetArrayOffset;
        private OutputStream socketOutputStream;

        SSLOutputStream() {
            this.target = ByteBuffer.allocate(ConscryptEngineSocket.this.engine.getSession().getPacketBufferSize());
            this.targetArrayOffset = this.target.arrayOffset();
        }

        @Override
        public final void close() throws IOException {
            ((OpenSSLSocketImpl)ConscryptEngineSocket.this).close();
        }

        @Override
        public final void write(int n2) throws IOException {
            ConscryptEngineSocket.this.startHandshake();
            Object object = this.writeLock;
            synchronized (object) {
                this.write(new byte[]{(byte)n2});
                return;
            }
        }

        @Override
        public final void write(byte[] byArray) throws IOException {
            ConscryptEngineSocket.this.startHandshake();
            Object object = this.writeLock;
            synchronized (object) {
                this.writeInternal(ByteBuffer.wrap(byArray));
                return;
            }
        }

        @Override
        public final void write(byte[] byArray, int n2, int n3) throws IOException {
            ConscryptEngineSocket.this.startHandshake();
            Object object = this.writeLock;
            synchronized (object) {
                this.writeInternal(ByteBuffer.wrap(byArray, n2, n3));
                return;
            }
        }

        private void writeInternal(ByteBuffer byteBuffer) throws IOException {
            Platform.blockGuardOnNetwork();
            ConscryptEngineSocket.this.checkOpen();
            this.init();
            int n2 = byteBuffer.remaining();
            do {
                this.target.clear();
                SSLEngineResult sSLEngineResult = ConscryptEngineSocket.this.engine.wrap(byteBuffer, this.target);
                if (sSLEngineResult.getStatus() != SSLEngineResult.Status.OK && sSLEngineResult.getStatus() != SSLEngineResult.Status.CLOSED) {
                    throw new SSLException("Unexpected engine result " + (Object)((Object)sSLEngineResult.getStatus()));
                }
                if (this.target.position() != sSLEngineResult.bytesProduced()) {
                    throw new SSLException("Engine bytesProduced " + sSLEngineResult.bytesProduced() + " does not match bytes written " + this.target.position());
                }
                if ((n2 -= sSLEngineResult.bytesConsumed()) != byteBuffer.remaining()) {
                    throw new SSLException("Engine did not read the correct number of bytes");
                }
                if (sSLEngineResult.getStatus() == SSLEngineResult.Status.CLOSED && sSLEngineResult.bytesProduced() == 0) {
                    if (n2 <= 0) break;
                    throw new SocketException("Socket closed");
                }
                this.target.flip();
                this.writeToSocket();
            } while (n2 > 0);
        }

        @Override
        public final void flush() throws IOException {
            ConscryptEngineSocket.this.startHandshake();
            Object object = this.writeLock;
            synchronized (object) {
                this.flushInternal();
                return;
            }
        }

        private void flushInternal() throws IOException {
            ConscryptEngineSocket.this.checkOpen();
            this.init();
            this.socketOutputStream.flush();
        }

        private void init() throws IOException {
            if (this.socketOutputStream == null) {
                this.socketOutputStream = ConscryptEngineSocket.this.getUnderlyingOutputStream();
            }
        }

        private void writeToSocket() throws IOException {
            this.socketOutputStream.write(this.target.array(), this.targetArrayOffset, this.target.limit());
        }
    }
}


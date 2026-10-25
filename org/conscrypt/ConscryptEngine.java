/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.nio.ByteBuffer;
import java.nio.ReadOnlyBufferException;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECKey;
import java.security.spec.ECParameterSpec;
import java.util.Arrays;
import javax.crypto.SecretKey;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509ExtendedKeyManager;
import javax.net.ssl.X509KeyManager;
import javax.net.ssl.X509TrustManager;
import javax.security.auth.x500.X500Principal;
import org.conscrypt.AbstractConscryptEngine;
import org.conscrypt.AbstractSessionContext;
import org.conscrypt.ActiveSession;
import org.conscrypt.AllocatedBuffer;
import org.conscrypt.ApplicationProtocolSelector;
import org.conscrypt.ApplicationProtocolSelectorAdapter;
import org.conscrypt.BufferAllocator;
import org.conscrypt.BufferUtils;
import org.conscrypt.ClientSessionContext;
import org.conscrypt.ConscryptSession;
import org.conscrypt.ExternalSession;
import org.conscrypt.HandshakeListener;
import org.conscrypt.NativeCrypto;
import org.conscrypt.NativeRef;
import org.conscrypt.NativeSsl;
import org.conscrypt.NativeSslSession;
import org.conscrypt.OpenSSLECGroupContext;
import org.conscrypt.OpenSSLKey;
import org.conscrypt.PSKKeyManager;
import org.conscrypt.PeerInfoProvider;
import org.conscrypt.Platform;
import org.conscrypt.Preconditions;
import org.conscrypt.SSLNullSession;
import org.conscrypt.SSLParametersImpl;
import org.conscrypt.SSLUtils;
import org.conscrypt.SessionSnapshot;

final class ConscryptEngine
extends AbstractConscryptEngine
implements NativeCrypto.SSLHandshakeCallbacks,
SSLParametersImpl.AliasChooser,
SSLParametersImpl.PSKCallbacks {
    private static final SSLEngineResult NEED_UNWRAP_OK = new SSLEngineResult(SSLEngineResult.Status.OK, SSLEngineResult.HandshakeStatus.NEED_UNWRAP, 0, 0);
    private static final SSLEngineResult NEED_UNWRAP_CLOSED = new SSLEngineResult(SSLEngineResult.Status.CLOSED, SSLEngineResult.HandshakeStatus.NEED_UNWRAP, 0, 0);
    private static final SSLEngineResult NEED_WRAP_OK = new SSLEngineResult(SSLEngineResult.Status.OK, SSLEngineResult.HandshakeStatus.NEED_WRAP, 0, 0);
    private static final SSLEngineResult NEED_WRAP_CLOSED = new SSLEngineResult(SSLEngineResult.Status.CLOSED, SSLEngineResult.HandshakeStatus.NEED_WRAP, 0, 0);
    private static final SSLEngineResult CLOSED_NOT_HANDSHAKING = new SSLEngineResult(SSLEngineResult.Status.CLOSED, SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING, 0, 0);
    private static BufferAllocator defaultBufferAllocator = null;
    private final SSLParametersImpl sslParameters;
    private BufferAllocator bufferAllocator = defaultBufferAllocator;
    private ByteBuffer lazyDirectBuffer;
    private String peerHostname;
    private int state = 0;
    private boolean handshakeFinished;
    private final NativeSsl ssl;
    private final NativeSsl.BioWrapper networkBio;
    private ActiveSession activeSession;
    private SessionSnapshot closedSession;
    private final SSLSession externalSession = Platform.wrapSSLSession(new ExternalSession(new ExternalSession.Provider(){

        @Override
        public ConscryptSession provideSession() {
            return ConscryptEngine.this.provideSession();
        }
    }));
    private OpenSSLKey channelIdPrivateKey;
    private int maxSealOverhead;
    private HandshakeListener handshakeListener;
    private final ByteBuffer[] singleSrcBuffer = new ByteBuffer[1];
    private final ByteBuffer[] singleDstBuffer = new ByteBuffer[1];
    private final PeerInfoProvider peerInfoProvider;

    ConscryptEngine(SSLParametersImpl sSLParametersImpl) {
        this.sslParameters = sSLParametersImpl;
        this.peerInfoProvider = PeerInfoProvider.nullProvider();
        ConscryptEngine conscryptEngine = this;
        this.ssl = ConscryptEngine.newSsl(sSLParametersImpl, conscryptEngine, conscryptEngine);
        this.networkBio = this.ssl.newBio();
    }

    ConscryptEngine(String string, int n2, SSLParametersImpl sSLParametersImpl) {
        this.sslParameters = sSLParametersImpl;
        this.peerInfoProvider = PeerInfoProvider.forHostAndPort(string, n2);
        ConscryptEngine conscryptEngine = this;
        this.ssl = ConscryptEngine.newSsl(sSLParametersImpl, conscryptEngine, conscryptEngine);
        this.networkBio = this.ssl.newBio();
    }

    ConscryptEngine(SSLParametersImpl sSLParametersImpl, PeerInfoProvider peerInfoProvider, SSLParametersImpl.AliasChooser aliasChooser) {
        this.sslParameters = sSLParametersImpl;
        this.peerInfoProvider = Preconditions.checkNotNull(peerInfoProvider, "peerInfoProvider");
        this.ssl = ConscryptEngine.newSsl(sSLParametersImpl, this, aliasChooser);
        this.networkBio = this.ssl.newBio();
    }

    private static NativeSsl newSsl(SSLParametersImpl sSLParametersImpl, ConscryptEngine conscryptEngine, SSLParametersImpl.AliasChooser aliasChooser) {
        try {
            return NativeSsl.newInstance(sSLParametersImpl, conscryptEngine, aliasChooser, conscryptEngine);
        }
        catch (SSLException sSLException) {
            throw new RuntimeException(sSLException);
        }
    }

    static void setDefaultBufferAllocator(BufferAllocator bufferAllocator) {
        defaultBufferAllocator = bufferAllocator;
    }

    static BufferAllocator getDefaultBufferAllocator() {
        return defaultBufferAllocator;
    }

    @Override
    final void setBufferAllocator(BufferAllocator bufferAllocator) {
        NativeSsl nativeSsl = this.ssl;
        synchronized (nativeSsl) {
            if (this.isHandshakeStarted()) {
                throw new IllegalStateException("Could not set buffer allocator after the initial handshake has begun.");
            }
            this.bufferAllocator = bufferAllocator;
            return;
        }
    }

    @Override
    final int maxSealOverhead() {
        return this.maxSealOverhead;
    }

    @Override
    final void setChannelIdEnabled(boolean bl) {
        NativeSsl nativeSsl = this.ssl;
        synchronized (nativeSsl) {
            if (this.getUseClientMode()) {
                throw new IllegalStateException("Not allowed in client mode");
            }
            if (this.isHandshakeStarted()) {
                throw new IllegalStateException("Could not enable/disable Channel ID after the initial handshake has begun.");
            }
            this.sslParameters.channelIdEnabled = bl;
            return;
        }
    }

    @Override
    final byte[] getChannelId() throws SSLException {
        NativeSsl nativeSsl = this.ssl;
        synchronized (nativeSsl) {
            if (this.getUseClientMode()) {
                throw new IllegalStateException("Not allowed in client mode");
            }
            if (this.isHandshakeStarted()) {
                throw new IllegalStateException("Channel ID is only available after handshake completes");
            }
            return this.ssl.getTlsChannelId();
        }
    }

    @Override
    final void setChannelIdPrivateKey(PrivateKey privateKey) {
        if (!this.getUseClientMode()) {
            throw new IllegalStateException("Not allowed in server mode");
        }
        NativeSsl nativeSsl = this.ssl;
        synchronized (nativeSsl) {
            if (this.isHandshakeStarted()) {
                throw new IllegalStateException("Could not change Channel ID private key after the initial handshake has begun.");
            }
            if (privateKey == null) {
                this.sslParameters.channelIdEnabled = false;
                this.channelIdPrivateKey = null;
                return;
            }
            this.sslParameters.channelIdEnabled = true;
            try {
                ECParameterSpec eCParameterSpec = null;
                if (privateKey instanceof ECKey) {
                    eCParameterSpec = ((ECKey)((Object)privateKey)).getParams();
                }
                if (eCParameterSpec == null) {
                    eCParameterSpec = OpenSSLECGroupContext.getCurveByName("prime256v1").getECParameterSpec();
                }
                this.channelIdPrivateKey = OpenSSLKey.fromECPrivateKeyForTLSStackOnly(privateKey, eCParameterSpec);
            }
            catch (InvalidKeyException invalidKeyException) {}
            return;
        }
    }

    @Override
    final void setHandshakeListener(HandshakeListener handshakeListener) {
        NativeSsl nativeSsl = this.ssl;
        synchronized (nativeSsl) {
            if (this.isHandshakeStarted()) {
                throw new IllegalStateException("Handshake listener must be set before starting the handshake.");
            }
            this.handshakeListener = handshakeListener;
            return;
        }
    }

    private boolean isHandshakeStarted() {
        switch (this.state) {
            case 0: 
            case 1: {
                return false;
            }
        }
        return true;
    }

    @Override
    final void setHostname(String string) {
        this.sslParameters.setUseSni(string != null);
        this.peerHostname = string;
    }

    @Override
    final String getHostname() {
        if (this.peerHostname != null) {
            return this.peerHostname;
        }
        return this.peerInfoProvider.getHostname();
    }

    @Override
    public final String getPeerHost() {
        if (this.peerHostname != null) {
            return this.peerHostname;
        }
        return this.peerInfoProvider.getHostnameOrIP();
    }

    @Override
    public final int getPeerPort() {
        return this.peerInfoProvider.getPort();
    }

    @Override
    public final void beginHandshake() throws SSLException {
        NativeSsl nativeSsl = this.ssl;
        synchronized (nativeSsl) {
            this.beginHandshakeInternal();
            return;
        }
    }

    private void beginHandshakeInternal() throws SSLException {
        switch (this.state) {
            case 0: {
                throw new IllegalStateException("Client/server mode must be set before handshake");
            }
            case 1: {
                break;
            }
            case 6: 
            case 7: 
            case 8: {
                throw new SSLHandshakeException("Engine has already been closed");
            }
            default: {
                return;
            }
        }
        this.transitionTo(2);
        try {
            try {
                NativeSslSession nativeSslSession;
                this.ssl.initialize(this.getHostname(), this.channelIdPrivateKey);
                if (this.getUseClientMode() && (nativeSslSession = this.clientSessionContext().getCachedSession(this.getHostname(), this.getPeerPort(), this.sslParameters)) != null) {
                    nativeSslSession.offerToResume(this.ssl);
                }
                this.maxSealOverhead = this.ssl.getMaxSealOverhead();
                this.handshake();
                return;
            }
            catch (IOException iOException) {
                IOException iOException2 = iOException;
                String string = iOException.getMessage();
                if (string.contains("unexpected CCS")) {
                    string = String.format("ssl_unexpected_ccs: host=%s", this.getPeerHost());
                    Platform.logEvent(string);
                }
                this.closeAll();
                throw SSLUtils.toSSLHandshakeException(iOException2);
            }
        }
        catch (Throwable throwable) {
            this.closeAndFreeResources();
            throw throwable;
        }
    }

    @Override
    public final void closeInbound() {
        NativeSsl nativeSsl = this.ssl;
        synchronized (nativeSsl) {
            if (this.state == 8 || this.state == 6) {
                return;
            }
            if (this.isHandshakeStarted()) {
                if (this.state == 7) {
                    this.transitionTo(8);
                } else {
                    this.transitionTo(6);
                }
                this.freeIfDone();
            } else {
                this.closeAndFreeResources();
            }
            return;
        }
    }

    @Override
    public final void closeOutbound() {
        NativeSsl nativeSsl = this.ssl;
        synchronized (nativeSsl) {
            if (this.state == 8 || this.state == 7) {
                return;
            }
            if (this.isHandshakeStarted()) {
                if (this.state == 6) {
                    this.transitionTo(8);
                } else {
                    this.transitionTo(7);
                }
                this.sendSSLShutdown();
                this.freeIfDone();
            } else {
                this.closeAndFreeResources();
            }
            return;
        }
    }

    @Override
    public final Runnable getDelegatedTask() {
        return null;
    }

    @Override
    public final String[] getEnabledCipherSuites() {
        return this.sslParameters.getEnabledCipherSuites();
    }

    @Override
    public final String[] getEnabledProtocols() {
        return this.sslParameters.getEnabledProtocols();
    }

    @Override
    public final boolean getEnableSessionCreation() {
        return this.sslParameters.getEnableSessionCreation();
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
    public final SSLEngineResult.HandshakeStatus getHandshakeStatus() {
        NativeSsl nativeSsl = this.ssl;
        synchronized (nativeSsl) {
            return this.getHandshakeStatusInternal();
        }
    }

    private SSLEngineResult.HandshakeStatus getHandshakeStatusInternal() {
        if (this.handshakeFinished) {
            return SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING;
        }
        switch (this.state) {
            case 2: {
                return ConscryptEngine.pendingStatus(this.pendingOutboundEncryptedBytes());
            }
            case 3: {
                return SSLEngineResult.HandshakeStatus.NEED_WRAP;
            }
            case 0: 
            case 1: 
            case 4: 
            case 5: 
            case 6: 
            case 7: 
            case 8: {
                return SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING;
            }
        }
        throw new IllegalStateException("Unexpected engine state: " + this.state);
    }

    final int pendingOutboundEncryptedBytes() {
        return this.networkBio.getPendingWrittenBytes();
    }

    private int pendingInboundCleartextBytes() {
        return this.ssl.getPendingReadableBytes();
    }

    private static SSLEngineResult.HandshakeStatus pendingStatus(int n2) {
        if (n2 > 0) {
            return SSLEngineResult.HandshakeStatus.NEED_WRAP;
        }
        return SSLEngineResult.HandshakeStatus.NEED_UNWRAP;
    }

    @Override
    public final boolean getNeedClientAuth() {
        return this.sslParameters.getNeedClientAuth();
    }

    @Override
    final SSLSession handshakeSession() {
        NativeSsl nativeSsl = this.ssl;
        synchronized (nativeSsl) {
            if (this.state == 2) {
                return Platform.wrapSSLSession(new ExternalSession(new ExternalSession.Provider(){

                    @Override
                    public ConscryptSession provideSession() {
                        return ConscryptEngine.this.provideHandshakeSession();
                    }
                }));
            }
            return null;
        }
    }

    @Override
    public final SSLSession getSession() {
        return this.externalSession;
    }

    private ConscryptSession provideSession() {
        NativeSsl nativeSsl = this.ssl;
        synchronized (nativeSsl) {
            if (this.state == 8) {
                return this.closedSession != null ? this.closedSession : SSLNullSession.getNullSession();
            }
            if (this.state < 3) {
                return SSLNullSession.getNullSession();
            }
            return this.activeSession;
        }
    }

    private ConscryptSession provideHandshakeSession() {
        NativeSsl nativeSsl = this.ssl;
        synchronized (nativeSsl) {
            return this.state == 2 ? this.activeSession : SSLNullSession.getNullSession();
        }
    }

    private ConscryptSession provideAfterHandshakeSession() {
        if (this.state < 2) {
            return SSLNullSession.getNullSession();
        }
        return this.provideSession();
    }

    @Override
    public final String[] getSupportedCipherSuites() {
        return NativeCrypto.getSupportedCipherSuites();
    }

    @Override
    public final String[] getSupportedProtocols() {
        return NativeCrypto.getSupportedProtocols();
    }

    @Override
    public final boolean getUseClientMode() {
        return this.sslParameters.getUseClientMode();
    }

    @Override
    public final boolean getWantClientAuth() {
        return this.sslParameters.getWantClientAuth();
    }

    @Override
    public final boolean isInboundDone() {
        NativeSsl nativeSsl = this.ssl;
        synchronized (nativeSsl) {
            return (this.state == 8 || this.state == 6 || this.ssl.wasShutdownReceived()) && this.pendingInboundCleartextBytes() == 0;
        }
    }

    @Override
    public final boolean isOutboundDone() {
        NativeSsl nativeSsl = this.ssl;
        synchronized (nativeSsl) {
            return (this.state == 8 || this.state == 7 || this.ssl.wasShutdownSent()) && this.pendingOutboundEncryptedBytes() == 0;
        }
    }

    @Override
    public final void setEnabledCipherSuites(String[] stringArray) {
        this.sslParameters.setEnabledCipherSuites(stringArray);
    }

    @Override
    public final void setEnabledProtocols(String[] stringArray) {
        this.sslParameters.setEnabledProtocols(stringArray);
    }

    @Override
    public final void setEnableSessionCreation(boolean bl) {
        this.sslParameters.setEnableSessionCreation(bl);
    }

    @Override
    public final void setNeedClientAuth(boolean bl) {
        this.sslParameters.setNeedClientAuth(bl);
    }

    @Override
    public final void setUseClientMode(boolean bl) {
        NativeSsl nativeSsl = this.ssl;
        synchronized (nativeSsl) {
            if (this.isHandshakeStarted()) {
                throw new IllegalArgumentException("Can not change mode after handshake: state == " + this.state);
            }
            this.transitionTo(1);
            this.sslParameters.setUseClientMode(bl);
            return;
        }
    }

    @Override
    public final void setWantClientAuth(boolean bl) {
        this.sslParameters.setWantClientAuth(bl);
    }

    @Override
    public final SSLEngineResult unwrap(ByteBuffer object, ByteBuffer byteBuffer) throws SSLException {
        NativeSsl nativeSsl = this.ssl;
        synchronized (nativeSsl) {
            try {
                ConscryptEngine conscryptEngine = this;
                object = conscryptEngine.unwrap(conscryptEngine.singleSrcBuffer((ByteBuffer)object), this.singleDstBuffer(byteBuffer));
                this.resetSingleSrcBuffer();
                this.resetSingleDstBuffer();
            }
            catch (Throwable throwable) {
                this.resetSingleSrcBuffer();
                this.resetSingleDstBuffer();
                throw throwable;
            }
            return object;
        }
    }

    @Override
    public final SSLEngineResult unwrap(ByteBuffer object, ByteBuffer[] byteBufferArray) throws SSLException {
        NativeSsl nativeSsl = this.ssl;
        synchronized (nativeSsl) {
            try {
                ConscryptEngine conscryptEngine = this;
                object = conscryptEngine.unwrap(conscryptEngine.singleSrcBuffer((ByteBuffer)object), byteBufferArray);
                this.resetSingleSrcBuffer();
            }
            catch (Throwable throwable) {
                this.resetSingleSrcBuffer();
                throw throwable;
            }
            return object;
        }
    }

    @Override
    public final SSLEngineResult unwrap(ByteBuffer object, ByteBuffer[] byteBufferArray, int n2, int n3) throws SSLException {
        NativeSsl nativeSsl = this.ssl;
        synchronized (nativeSsl) {
            try {
                ConscryptEngine conscryptEngine = this;
                object = conscryptEngine.unwrap(conscryptEngine.singleSrcBuffer((ByteBuffer)object), 0, 1, byteBufferArray, n2, n3);
                this.resetSingleSrcBuffer();
            }
            catch (Throwable throwable) {
                this.resetSingleSrcBuffer();
                throw throwable;
            }
            return object;
        }
    }

    @Override
    final SSLEngineResult unwrap(ByteBuffer[] byteBufferArray, ByteBuffer[] byteBufferArray2) throws SSLException {
        Preconditions.checkArgument(byteBufferArray != null, "srcs is null");
        Preconditions.checkArgument(byteBufferArray2 != null, "dsts is null");
        return this.unwrap(byteBufferArray, 0, byteBufferArray.length, byteBufferArray2, 0, byteBufferArray2.length);
    }

    @Override
    final SSLEngineResult unwrap(ByteBuffer[] byteBufferArray, int n2, int n3, ByteBuffer[] byteBufferArray2, int n4, int n5) throws SSLException {
        Preconditions.checkArgument(byteBufferArray != null, "srcs is null");
        Preconditions.checkArgument(byteBufferArray2 != null, "dsts is null");
        int n6 = n2;
        Preconditions.checkPositionIndexes(n6, n6 + n3, byteBufferArray.length);
        int n7 = n4;
        Preconditions.checkPositionIndexes(n7, n7 + n5, byteBufferArray2.length);
        int n8 = ConscryptEngine.calcDstsLength(byteBufferArray2, n4, n5);
        n5 = n4 + n5;
        n3 = n2 + n3;
        long l2 = ConscryptEngine.calcSrcsLength(byteBufferArray, n2, n3);
        NativeSsl nativeSsl = this.ssl;
        synchronized (nativeSsl) {
            int n9;
            int n10;
            int n11;
            SSLEngineResult.HandshakeStatus handshakeStatus;
            block32: {
                switch (this.state) {
                    case 1: {
                        this.beginHandshakeInternal();
                        break;
                    }
                    case 6: 
                    case 8: {
                        this.freeIfDone();
                        return new SSLEngineResult(SSLEngineResult.Status.CLOSED, this.getHandshakeStatusInternal(), 0, 0);
                    }
                    case 0: {
                        throw new IllegalStateException("Client/server mode must be set before calling unwrap");
                    }
                }
                handshakeStatus = SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING;
                if (!this.handshakeFinished) {
                    handshakeStatus = this.handshake();
                    if (handshakeStatus == SSLEngineResult.HandshakeStatus.NEED_WRAP) {
                        return NEED_WRAP_OK;
                    }
                    if (this.state == 8) {
                        return NEED_WRAP_CLOSED;
                    }
                }
                n11 = this.pendingInboundCleartextBytes() <= 0 ? 1 : 0;
                int n12 = 0;
                if (l2 > 0L && n11 != 0) {
                    if (l2 < 5L) {
                        return new SSLEngineResult(SSLEngineResult.Status.BUFFER_UNDERFLOW, this.getHandshakeStatus(), 0, 0);
                    }
                    n11 = SSLUtils.getEncryptedPacketLength(byteBufferArray, n2);
                    if (n11 < 0) {
                        throw new SSLException("Unable to parse TLS packet header");
                    }
                    if (l2 < (long)n11) {
                        return new SSLEngineResult(SSLEngineResult.Status.BUFFER_UNDERFLOW, this.getHandshakeStatus(), 0, 0);
                    }
                    n12 = n11;
                } else if (n11 != 0) {
                    return new SSLEngineResult(SSLEngineResult.Status.BUFFER_UNDERFLOW, this.getHandshakeStatus(), 0, 0);
                }
                n11 = 0;
                if (n12 > 0 && n2 < n3) {
                    do {
                        ByteBuffer byteBuffer;
                        if ((n10 = (byteBuffer = byteBufferArray[n2]).remaining()) == 0) {
                            ++n2;
                            continue;
                        }
                        int n13 = this.writeEncryptedData(byteBuffer, Math.min(n12, n10));
                        if (n13 > 0) {
                            n11 += n13;
                            if ((n12 -= n13) == 0 || n13 != n10) break;
                            ++n2;
                            continue;
                        }
                        NativeCrypto.SSL_clear_error();
                        break;
                    } while (n2 < n3);
                }
                n9 = 0;
                try {
                    if (n8 > 0) {
                        for (n10 = n4; n10 < n5; ++n10) {
                            ByteBuffer byteBuffer = byteBufferArray2[n10];
                            if (!byteBuffer.hasRemaining()) continue;
                            int n14 = this.readPlaintextData(byteBuffer);
                            if (n14 > 0) {
                                n9 += n14;
                                if (!byteBuffer.hasRemaining()) continue;
                                break block32;
                            }
                            switch (n14) {
                                case -3: 
                                case -2: {
                                    return this.newResult(n11, n9, handshakeStatus);
                                }
                                case -6: {
                                    this.closeAll();
                                    return new SSLEngineResult(SSLEngineResult.Status.CLOSED, this.pendingOutboundEncryptedBytes() > 0 ? SSLEngineResult.HandshakeStatus.NEED_WRAP : SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING, n11, n9);
                                }
                            }
                            this.closeAll();
                            throw this.newSslExceptionWithMessage("SSL_read");
                        }
                        break block32;
                    }
                    this.ssl.forceRead();
                }
                catch (InterruptedIOException interruptedIOException) {
                    return this.newResult(n11, n9, handshakeStatus);
                }
                catch (IOException iOException) {
                    this.closeAll();
                    throw this.convertException(iOException);
                }
            }
            if ((n10 = this.handshakeFinished ? this.pendingInboundCleartextBytes() : 0) > 0) {
                return new SSLEngineResult(SSLEngineResult.Status.BUFFER_OVERFLOW, this.mayFinishHandshake(handshakeStatus == SSLEngineResult.HandshakeStatus.FINISHED ? handshakeStatus : this.getHandshakeStatusInternal()), n11, n9);
            }
            return this.newResult(n11, n9, handshakeStatus);
        }
    }

    private static int calcDstsLength(ByteBuffer[] byteBufferArray, int n2, int n3) {
        int n4 = 0;
        for (int i2 = 0; i2 < byteBufferArray.length; ++i2) {
            ByteBuffer byteBuffer = byteBufferArray[i2];
            Preconditions.checkArgument(byteBuffer != null, "dsts[%d] is null", i2);
            if (byteBuffer.isReadOnly()) {
                throw new ReadOnlyBufferException();
            }
            if (i2 < n2 || i2 >= n2 + n3) continue;
            n4 += byteBuffer.remaining();
        }
        return n4;
    }

    private static long calcSrcsLength(ByteBuffer[] byteBufferArray, int n2, int n3) {
        long l2 = 0L;
        while (n2 < n3) {
            ByteBuffer byteBuffer = byteBufferArray[n2];
            if (byteBuffer == null) {
                throw new IllegalArgumentException("srcs[" + n2 + "] is null");
            }
            l2 += (long)byteBuffer.remaining();
            ++n2;
        }
        return l2;
    }

    private SSLEngineResult.HandshakeStatus handshake() throws SSLException {
        try {
            try {
                int n2 = this.ssl.doHandshake();
                switch (n2) {
                    case 2: {
                        return ConscryptEngine.pendingStatus(this.pendingOutboundEncryptedBytes());
                    }
                    case 3: {
                        return SSLEngineResult.HandshakeStatus.NEED_WRAP;
                    }
                }
            }
            catch (IOException iOException) {
                this.closeAll();
                throw iOException;
            }
            this.activeSession.onPeerCertificateAvailable(this.getPeerHost(), this.getPeerPort());
            this.finishHandshake();
            return SSLEngineResult.HandshakeStatus.FINISHED;
        }
        catch (Exception exception) {
            Exception exception2 = exception;
            throw SSLUtils.toSSLHandshakeException(exception);
        }
    }

    private void finishHandshake() throws SSLException {
        this.handshakeFinished = true;
        if (this.handshakeListener != null) {
            this.handshakeListener.onHandshakeFinished();
        }
    }

    private int writePlaintextData(ByteBuffer byteBuffer, int n2) throws SSLException {
        try {
            int n3 = byteBuffer.position();
            n2 = byteBuffer.isDirect() ? this.writePlaintextDataDirect(byteBuffer, n3, n2) : this.writePlaintextDataHeap(byteBuffer, n3, n2);
            if (n2 > 0) {
                byteBuffer.position(n3 + n2);
            }
            return n2;
        }
        catch (Exception exception) {
            throw this.convertException(exception);
        }
    }

    private int writePlaintextDataDirect(ByteBuffer byteBuffer, int n2, int n3) throws IOException {
        return this.ssl.writeDirectByteBuffer(this.directByteBufferAddress(byteBuffer, n2), n3);
    }

    private int writePlaintextDataHeap(ByteBuffer byteBuffer, int n2, int n3) throws IOException {
        AllocatedBuffer allocatedBuffer = null;
        try {
            ByteBuffer byteBuffer2;
            if (this.bufferAllocator != null) {
                allocatedBuffer = this.bufferAllocator.allocateDirectBuffer(n3);
                byteBuffer2 = allocatedBuffer.nioBuffer();
            } else {
                byteBuffer2 = this.getOrCreateLazyDirectBuffer();
            }
            int n4 = byteBuffer.limit();
            n3 = Math.min(n3, byteBuffer2.remaining());
            byteBuffer.limit(n2 + n3);
            byteBuffer2.put(byteBuffer);
            byteBuffer2.flip();
            byteBuffer.limit(n4);
            byteBuffer.position(n2);
            int n5 = this.writePlaintextDataDirect(byteBuffer2, 0, n3);
            return n5;
        }
        finally {
            if (allocatedBuffer != null) {
                allocatedBuffer.release();
            }
        }
    }

    private int readPlaintextData(ByteBuffer byteBuffer) throws IOException {
        try {
            int n2 = byteBuffer.position();
            int n3 = byteBuffer.limit();
            n3 = Math.min(16709, n3 - n2);
            if (byteBuffer.isDirect()) {
                if ((n3 = this.readPlaintextDataDirect(byteBuffer, n2, n3)) > 0) {
                    byteBuffer.position(n2 + n3);
                }
                return n3;
            }
            return this.readPlaintextDataHeap(byteBuffer, n3);
        }
        catch (CertificateException certificateException) {
            throw this.convertException(certificateException);
        }
    }

    private int readPlaintextDataDirect(ByteBuffer byteBuffer, int n2, int n3) throws IOException, CertificateException {
        return this.ssl.readDirectByteBuffer(this.directByteBufferAddress(byteBuffer, n2), n3);
    }

    private int readPlaintextDataHeap(ByteBuffer byteBuffer, int n2) throws IOException, CertificateException {
        AllocatedBuffer allocatedBuffer = null;
        try {
            ByteBuffer byteBuffer2;
            if (this.bufferAllocator != null) {
                allocatedBuffer = this.bufferAllocator.allocateDirectBuffer(n2);
                byteBuffer2 = allocatedBuffer.nioBuffer();
            } else {
                byteBuffer2 = this.getOrCreateLazyDirectBuffer();
            }
            n2 = Math.min(n2, byteBuffer2.remaining());
            n2 = this.readPlaintextDataDirect(byteBuffer2, 0, n2);
            if (n2 > 0) {
                byteBuffer2.position(n2);
                byteBuffer2.flip();
                byteBuffer.put(byteBuffer2);
            }
            int n3 = n2;
            return n3;
        }
        finally {
            if (allocatedBuffer != null) {
                allocatedBuffer.release();
            }
        }
    }

    private SSLException convertException(Throwable throwable) {
        if (throwable instanceof SSLHandshakeException || !this.handshakeFinished) {
            return SSLUtils.toSSLHandshakeException(throwable);
        }
        return SSLUtils.toSSLException(throwable);
    }

    private int writeEncryptedData(ByteBuffer byteBuffer, int n2) throws SSLException {
        try {
            int n3 = byteBuffer.position();
            n2 = byteBuffer.isDirect() ? this.writeEncryptedDataDirect(byteBuffer, n3, n2) : this.writeEncryptedDataHeap(byteBuffer, n3, n2);
            if (n2 > 0) {
                byteBuffer.position(n3 + n2);
            }
            return n2;
        }
        catch (IOException iOException) {
            this.closeAll();
            throw new SSLException(iOException);
        }
    }

    private int writeEncryptedDataDirect(ByteBuffer byteBuffer, int n2, int n3) throws IOException {
        return this.networkBio.writeDirectByteBuffer(this.directByteBufferAddress(byteBuffer, n2), n3);
    }

    private int writeEncryptedDataHeap(ByteBuffer byteBuffer, int n2, int n3) throws IOException {
        AllocatedBuffer allocatedBuffer = null;
        try {
            ByteBuffer byteBuffer2;
            if (this.bufferAllocator != null) {
                allocatedBuffer = this.bufferAllocator.allocateDirectBuffer(n3);
                byteBuffer2 = allocatedBuffer.nioBuffer();
            } else {
                byteBuffer2 = this.getOrCreateLazyDirectBuffer();
            }
            int n4 = byteBuffer.limit();
            n3 = Math.min(Math.min(n4 - n2, n3), byteBuffer2.remaining());
            byteBuffer.limit(n2 + n3);
            byteBuffer2.put(byteBuffer);
            byteBuffer.limit(n4);
            byteBuffer.position(n2);
            n3 = this.writeEncryptedDataDirect(byteBuffer2, 0, n3);
            byteBuffer.position(n2);
            int n5 = n3;
            return n5;
        }
        finally {
            if (allocatedBuffer != null) {
                allocatedBuffer.release();
            }
        }
    }

    private ByteBuffer getOrCreateLazyDirectBuffer() {
        if (this.lazyDirectBuffer == null) {
            this.lazyDirectBuffer = ByteBuffer.allocateDirect(Math.max(16384, 16709));
        }
        this.lazyDirectBuffer.clear();
        return this.lazyDirectBuffer;
    }

    private long directByteBufferAddress(ByteBuffer byteBuffer, int n2) {
        return NativeCrypto.getDirectBufferAddress(byteBuffer) + (long)n2;
    }

    private SSLEngineResult readPendingBytesFromBIO(ByteBuffer byteBuffer, int n2, int n3, SSLEngineResult.HandshakeStatus handshakeStatus) throws SSLException {
        try {
            int n4 = this.pendingOutboundEncryptedBytes();
            if (n4 > 0) {
                int n5 = byteBuffer.remaining();
                if (n5 < n4) {
                    return new SSLEngineResult(SSLEngineResult.Status.BUFFER_OVERFLOW, this.mayFinishHandshake(handshakeStatus == SSLEngineResult.HandshakeStatus.FINISHED ? handshakeStatus : this.getHandshakeStatus(n4)), n2, n3);
                }
                int n6 = this.readEncryptedData(byteBuffer, n4);
                if (n6 <= 0) {
                    NativeCrypto.SSL_clear_error();
                } else {
                    n3 += n6;
                    n4 -= n6;
                }
                return new SSLEngineResult(this.getEngineStatus(), this.mayFinishHandshake(handshakeStatus == SSLEngineResult.HandshakeStatus.FINISHED ? handshakeStatus : this.getHandshakeStatus(n4)), n2, n3);
            }
            return null;
        }
        catch (Exception exception) {
            throw this.convertException(exception);
        }
    }

    private int readEncryptedData(ByteBuffer byteBuffer, int n2) throws SSLException {
        try {
            int n3 = 0;
            int n4 = byteBuffer.position();
            if (byteBuffer.remaining() >= n2) {
                n3 = byteBuffer.limit();
                n2 = Math.min(n2, n3 - n4);
                if (byteBuffer.isDirect()) {
                    n3 = this.readEncryptedDataDirect(byteBuffer, n4, n2);
                    if (n3 > 0) {
                        byteBuffer.position(n4 + n3);
                    }
                } else {
                    n3 = this.readEncryptedDataHeap(byteBuffer, n2);
                }
            }
            return n3;
        }
        catch (Exception exception) {
            throw this.convertException(exception);
        }
    }

    private int readEncryptedDataDirect(ByteBuffer byteBuffer, int n2, int n3) throws IOException {
        return this.networkBio.readDirectByteBuffer(this.directByteBufferAddress(byteBuffer, n2), n3);
    }

    private int readEncryptedDataHeap(ByteBuffer byteBuffer, int n2) throws IOException {
        AllocatedBuffer allocatedBuffer = null;
        try {
            ByteBuffer byteBuffer2;
            if (this.bufferAllocator != null) {
                allocatedBuffer = this.bufferAllocator.allocateDirectBuffer(n2);
                byteBuffer2 = allocatedBuffer.nioBuffer();
            } else {
                byteBuffer2 = this.getOrCreateLazyDirectBuffer();
            }
            n2 = Math.min(n2, byteBuffer2.remaining());
            n2 = this.readEncryptedDataDirect(byteBuffer2, 0, n2);
            if (n2 > 0) {
                byteBuffer2.position(n2);
                byteBuffer2.flip();
                byteBuffer.put(byteBuffer2);
            }
            int n3 = n2;
            return n3;
        }
        finally {
            if (allocatedBuffer != null) {
                allocatedBuffer.release();
            }
        }
    }

    private SSLEngineResult.HandshakeStatus mayFinishHandshake(SSLEngineResult.HandshakeStatus handshakeStatus) throws SSLException {
        if (!this.handshakeFinished && handshakeStatus == SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING) {
            return this.handshake();
        }
        return handshakeStatus;
    }

    private SSLEngineResult.HandshakeStatus getHandshakeStatus(int n2) {
        if (!this.handshakeFinished) {
            return ConscryptEngine.pendingStatus(n2);
        }
        return SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING;
    }

    private SSLEngineResult.Status getEngineStatus() {
        switch (this.state) {
            case 6: 
            case 7: 
            case 8: {
                return SSLEngineResult.Status.CLOSED;
            }
        }
        return SSLEngineResult.Status.OK;
    }

    private void closeAll() {
        this.closeOutbound();
        this.closeInbound();
    }

    private void freeIfDone() {
        if (this.isInboundDone() && this.isOutboundDone()) {
            this.closeAndFreeResources();
        }
    }

    private SSLException newSslExceptionWithMessage(String string) {
        if (!this.handshakeFinished) {
            return new SSLException(string);
        }
        return new SSLHandshakeException(string);
    }

    private SSLEngineResult newResult(int n2, int n3, SSLEngineResult.HandshakeStatus handshakeStatus) throws SSLException {
        return new SSLEngineResult(this.getEngineStatus(), this.mayFinishHandshake(handshakeStatus == SSLEngineResult.HandshakeStatus.FINISHED ? handshakeStatus : this.getHandshakeStatusInternal()), n2, n3);
    }

    @Override
    public final SSLEngineResult wrap(ByteBuffer object, ByteBuffer byteBuffer) throws SSLException {
        NativeSsl nativeSsl = this.ssl;
        synchronized (nativeSsl) {
            try {
                ConscryptEngine conscryptEngine = this;
                object = conscryptEngine.wrap(conscryptEngine.singleSrcBuffer((ByteBuffer)object), byteBuffer);
                this.resetSingleSrcBuffer();
            }
            catch (Throwable throwable) {
                this.resetSingleSrcBuffer();
                throw throwable;
            }
            return object;
        }
    }

    @Override
    public final SSLEngineResult wrap(ByteBuffer[] object, int n2, int n3, ByteBuffer byteBuffer) throws SSLException {
        Preconditions.checkArgument(object != null, "srcs is null");
        Preconditions.checkArgument(byteBuffer != null, "dst is null");
        int n4 = n2;
        Preconditions.checkPositionIndexes(n4, n4 + n3, ((ByteBuffer[])object).length);
        if (byteBuffer.isReadOnly()) {
            throw new ReadOnlyBufferException();
        }
        if (n2 != 0 || n3 != ((ByteBuffer[])object).length) {
            int n5 = n2;
            object = Arrays.copyOfRange(object, n5, n5 + n3);
        }
        BufferUtils.checkNotNull((ByteBuffer[])object);
        NativeSsl nativeSsl = this.ssl;
        synchronized (nativeSsl) {
            SSLEngineResult sSLEngineResult;
            switch (this.state) {
                case 1: {
                    this.beginHandshakeInternal();
                    break;
                }
                case 7: 
                case 8: {
                    SSLEngineResult sSLEngineResult2 = this.readPendingBytesFromBIO(byteBuffer, 0, 0, SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING);
                    if (sSLEngineResult2 != null) {
                        this.freeIfDone();
                        return sSLEngineResult2;
                    }
                    return new SSLEngineResult(SSLEngineResult.Status.CLOSED, this.getHandshakeStatusInternal(), 0, 0);
                }
                case 0: {
                    throw new IllegalStateException("Client/server mode must be set before calling wrap");
                }
            }
            SSLEngineResult.HandshakeStatus handshakeStatus = SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING;
            if (!this.handshakeFinished) {
                handshakeStatus = this.handshake();
                if (handshakeStatus == SSLEngineResult.HandshakeStatus.NEED_UNWRAP) {
                    return NEED_UNWRAP_OK;
                }
                if (this.state == 8) {
                    return NEED_UNWRAP_CLOSED;
                }
            }
            int n6 = (int)Math.min(BufferUtils.remaining((ByteBuffer[])object), 16384L);
            if (byteBuffer.remaining() < SSLUtils.calculateOutNetBufSize(n6)) {
                return new SSLEngineResult(SSLEngineResult.Status.BUFFER_OVERFLOW, this.getHandshakeStatusInternal(), 0, 0);
            }
            int n7 = 0;
            int n8 = 0;
            if (n6 > 0) {
                n6 = 0;
                ByteBuffer byteBuffer2 = BufferUtils.getBufferLargerThan((ByteBuffer[])object, 16384);
                if (byteBuffer2 == null) {
                    byteBuffer2 = BufferUtils.copyNoConsume((ByteBuffer[])object, this.getOrCreateLazyDirectBuffer(), 16384);
                    n6 = 1;
                }
                if ((n8 = this.writePlaintextData(byteBuffer2, Math.min(16384, byteBuffer2.remaining()))) > 0) {
                    if (n6 != 0) {
                        BufferUtils.consume((ByteBuffer[])object, n8);
                    }
                    if ((object = this.readPendingBytesFromBIO(byteBuffer, n8, 0, handshakeStatus)) != null) {
                        if (((SSLEngineResult)object).getStatus() != SSLEngineResult.Status.OK) {
                            return object;
                        }
                        n7 = ((SSLEngineResult)object).bytesProduced();
                    }
                } else {
                    int n9 = this.ssl.getError(n8);
                    switch (n9) {
                        case 6: {
                            this.closeAll();
                            SSLEngineResult sSLEngineResult3 = this.readPendingBytesFromBIO(byteBuffer, 0, 0, handshakeStatus);
                            return sSLEngineResult3 != null ? sSLEngineResult3 : CLOSED_NOT_HANDSHAKING;
                        }
                        case 2: {
                            SSLEngineResult sSLEngineResult4 = this.readPendingBytesFromBIO(byteBuffer, 0, 0, handshakeStatus);
                            return sSLEngineResult4 != null ? sSLEngineResult4 : new SSLEngineResult(this.getEngineStatus(), SSLEngineResult.HandshakeStatus.NEED_UNWRAP, 0, 0);
                        }
                        case 3: {
                            SSLEngineResult sSLEngineResult5 = this.readPendingBytesFromBIO(byteBuffer, 0, 0, handshakeStatus);
                            return sSLEngineResult5 != null ? sSLEngineResult5 : NEED_WRAP_CLOSED;
                        }
                    }
                    this.closeAll();
                    throw this.newSslExceptionWithMessage("SSL_write: error " + n9);
                }
            }
            if (n8 == 0 && (sSLEngineResult = this.readPendingBytesFromBIO(byteBuffer, 0, n7, handshakeStatus)) != null) {
                return sSLEngineResult;
            }
            return this.newResult(n8, n7, handshakeStatus);
        }
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
        NativeSsl nativeSsl = this.ssl;
        synchronized (nativeSsl) {
            switch (n2) {
                case 16: {
                    this.transitionTo(2);
                    break;
                }
                case 32: {
                    if (this.state != 2 && this.state != 4) {
                        throw new IllegalStateException("Completed handshake while in mode " + this.state);
                    }
                    this.transitionTo(3);
                }
            }
            return;
        }
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
            this.activeSession.onPeerCertificatesReceived(this.getPeerHost(), this.getPeerPort(), (X509Certificate[])object);
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
    public final void clientCertificateRequested(byte[] byArray, int[] nArray, byte[][] byArray2) throws CertificateEncodingException, SSLException {
        this.ssl.chooseClientCertificate(byArray, nArray, byArray2);
    }

    private void sendSSLShutdown() {
        try {
            this.ssl.shutdown();
            return;
        }
        catch (IOException iOException) {
            return;
        }
    }

    private void closeAndFreeResources() {
        this.transitionTo(8);
        if (this.ssl != null) {
            this.ssl.close();
        }
        if (this.networkBio != null) {
            this.networkBio.close();
        }
    }

    protected final void finalize() throws Throwable {
        try {
            this.closeAndFreeResources();
            return;
        }
        finally {
            super.finalize();
        }
    }

    @Override
    public final String chooseServerAlias(X509KeyManager x509KeyManager, String string) {
        if (x509KeyManager instanceof X509ExtendedKeyManager) {
            x509KeyManager = (X509ExtendedKeyManager)x509KeyManager;
            return ((X509ExtendedKeyManager)x509KeyManager).chooseEngineServerAlias(string, null, this);
        }
        return x509KeyManager.chooseServerAlias(string, null, null);
    }

    @Override
    public final String chooseClientAlias(X509KeyManager x509KeyManager, X500Principal[] x500PrincipalArray, String[] stringArray) {
        if (x509KeyManager instanceof X509ExtendedKeyManager) {
            x509KeyManager = (X509ExtendedKeyManager)x509KeyManager;
            return ((X509ExtendedKeyManager)x509KeyManager).chooseEngineClientAlias(stringArray, x500PrincipalArray, this);
        }
        return x509KeyManager.chooseClientAlias(stringArray, x500PrincipalArray, null);
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
    final void setUseSessionTickets(boolean bl) {
        this.sslParameters.setUseSessionTickets(bl);
    }

    @Override
    final String[] getApplicationProtocols() {
        return this.sslParameters.getApplicationProtocols();
    }

    @Override
    final void setApplicationProtocols(String[] stringArray) {
        this.sslParameters.setApplicationProtocols(stringArray);
    }

    @Override
    final void setApplicationProtocolSelector(ApplicationProtocolSelector applicationProtocolSelector) {
        this.setApplicationProtocolSelector(applicationProtocolSelector == null ? null : new ApplicationProtocolSelectorAdapter(this, applicationProtocolSelector));
    }

    @Override
    final byte[] getTlsUnique() {
        return this.ssl.getTlsUnique();
    }

    @Override
    final byte[] exportKeyingMaterial(String string, byte[] byArray, int n2) throws SSLException {
        NativeSsl nativeSsl = this.ssl;
        synchronized (nativeSsl) {
            if (this.state < 3 || this.state == 8) {
                return null;
            }
        }
        return this.ssl.exportKeyingMaterial(string, byArray, n2);
    }

    final void setApplicationProtocolSelector(ApplicationProtocolSelectorAdapter applicationProtocolSelectorAdapter) {
        this.sslParameters.setApplicationProtocolSelector(applicationProtocolSelectorAdapter);
    }

    @Override
    public final int selectApplicationProtocol(byte[] byArray) {
        ApplicationProtocolSelectorAdapter applicationProtocolSelectorAdapter = this.sslParameters.getApplicationProtocolSelector();
        if (applicationProtocolSelectorAdapter == null) {
            return 3;
        }
        return applicationProtocolSelectorAdapter.selectApplicationProtocol(byArray);
    }

    @Override
    public final String getApplicationProtocol() {
        return this.provideAfterHandshakeSession().getApplicationProtocol();
    }

    @Override
    public final String getHandshakeApplicationProtocol() {
        NativeSsl nativeSsl = this.ssl;
        synchronized (nativeSsl) {
            return this.state >= 2 ? this.getApplicationProtocol() : null;
        }
    }

    private ByteBuffer[] singleSrcBuffer(ByteBuffer byteBuffer) {
        this.singleSrcBuffer[0] = byteBuffer;
        return this.singleSrcBuffer;
    }

    private void resetSingleSrcBuffer() {
        this.singleSrcBuffer[0] = null;
    }

    private ByteBuffer[] singleDstBuffer(ByteBuffer byteBuffer) {
        this.singleDstBuffer[0] = byteBuffer;
        return this.singleDstBuffer;
    }

    private void resetSingleDstBuffer() {
        this.singleDstBuffer[0] = null;
    }

    private ClientSessionContext clientSessionContext() {
        return this.sslParameters.getClientSessionContext();
    }

    private AbstractSessionContext sessionContext() {
        return this.sslParameters.getSessionContext();
    }

    private void transitionTo(int n2) {
        switch (n2) {
            case 2: {
                this.handshakeFinished = false;
                this.activeSession = new ActiveSession(this.ssl, this.sslParameters.getSessionContext());
                break;
            }
            case 8: {
                if (this.ssl.isClosed() || this.state < 2 || this.state >= 8) break;
                this.closedSession = new SessionSnapshot(this.activeSession);
            }
        }
        this.state = n2;
    }
}


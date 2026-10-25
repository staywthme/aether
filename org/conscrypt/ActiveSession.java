/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.security.Principal;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.List;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSessionContext;
import org.conscrypt.AbstractSessionContext;
import org.conscrypt.ConscryptSession;
import org.conscrypt.EmptyArray;
import org.conscrypt.NativeSsl;
import org.conscrypt.Preconditions;
import org.conscrypt.SSLUtils;

final class ActiveSession
implements ConscryptSession {
    private final NativeSsl ssl;
    private AbstractSessionContext sessionContext;
    private byte[] id;
    private long creationTime;
    private String protocol;
    private String applicationProtocol;
    private String peerHost;
    private int peerPort = -1;
    private long lastAccessedTime = 0L;
    private volatile javax.security.cert.X509Certificate[] peerCertificateChain;
    private X509Certificate[] localCertificates;
    private X509Certificate[] peerCertificates;
    private byte[] peerCertificateOcspData;
    private byte[] peerTlsSctData;

    ActiveSession(NativeSsl nativeSsl, AbstractSessionContext abstractSessionContext) {
        this.ssl = Preconditions.checkNotNull(nativeSsl, "ssl");
        this.sessionContext = Preconditions.checkNotNull(abstractSessionContext, "sessionContext");
    }

    @Override
    public final byte[] getId() {
        if (this.id == null) {
            NativeSsl nativeSsl = this.ssl;
            synchronized (nativeSsl) {
                this.id = this.ssl.getSessionId();
            }
        }
        if (this.id != null) {
            return (byte[])this.id.clone();
        }
        return EmptyArray.BYTE;
    }

    @Override
    public final SSLSessionContext getSessionContext() {
        if (this.isValid()) {
            return this.sessionContext;
        }
        return null;
    }

    @Override
    public final long getCreationTime() {
        if (this.creationTime == 0L) {
            NativeSsl nativeSsl = this.ssl;
            synchronized (nativeSsl) {
                this.creationTime = this.ssl.getTime();
            }
        }
        return this.creationTime;
    }

    @Override
    public final long getLastAccessedTime() {
        if (this.lastAccessedTime == 0L) {
            return this.getCreationTime();
        }
        return this.lastAccessedTime;
    }

    final void setLastAccessedTime(long l2) {
        this.lastAccessedTime = l2;
    }

    @Override
    public final List<byte[]> getStatusResponses() {
        if (this.peerCertificateOcspData == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(this.peerCertificateOcspData.clone());
    }

    @Override
    public final byte[] getPeerSignedCertificateTimestamp() {
        if (this.peerTlsSctData == null) {
            return null;
        }
        return (byte[])this.peerTlsSctData.clone();
    }

    @Override
    public final String getRequestedServerName() {
        NativeSsl nativeSsl = this.ssl;
        synchronized (nativeSsl) {
            return this.ssl.getRequestedServerName();
        }
    }

    @Override
    public final void invalidate() {
        NativeSsl nativeSsl = this.ssl;
        synchronized (nativeSsl) {
            this.ssl.setTimeout(0L);
            return;
        }
    }

    @Override
    public final boolean isValid() {
        NativeSsl nativeSsl = this.ssl;
        synchronized (nativeSsl) {
            long l2 = this.ssl.getTime();
            long l3 = this.ssl.getTimeout();
            return System.currentTimeMillis() - l3 < l2;
        }
    }

    @Override
    public final void putValue(String string, Object object) {
        throw new UnsupportedOperationException("All calls to this method should be intercepted by ExternalSession.");
    }

    @Override
    public final Object getValue(String string) {
        throw new UnsupportedOperationException("All calls to this method should be intercepted by ExternalSession.");
    }

    @Override
    public final void removeValue(String string) {
        throw new UnsupportedOperationException("All calls to this method should be intercepted by ExternalSession.");
    }

    @Override
    public final String[] getValueNames() {
        throw new UnsupportedOperationException("All calls to this method should be intercepted by ExternalSession.");
    }

    @Override
    public final X509Certificate[] getPeerCertificates() throws SSLPeerUnverifiedException {
        this.checkPeerCertificatesPresent();
        return (X509Certificate[])this.peerCertificates.clone();
    }

    @Override
    public final Certificate[] getLocalCertificates() {
        if (this.localCertificates == null) {
            NativeSsl nativeSsl = this.ssl;
            synchronized (nativeSsl) {
                this.localCertificates = this.ssl.getLocalCertificates();
            }
        }
        if (this.localCertificates == null) {
            return null;
        }
        return (X509Certificate[])this.localCertificates.clone();
    }

    @Override
    public final javax.security.cert.X509Certificate[] getPeerCertificateChain() throws SSLPeerUnverifiedException {
        this.checkPeerCertificatesPresent();
        javax.security.cert.X509Certificate[] x509CertificateArray = this.peerCertificateChain;
        if (this.peerCertificateChain == null) {
            x509CertificateArray = SSLUtils.toCertificateChain(this.peerCertificates);
            this.peerCertificateChain = x509CertificateArray;
        }
        return x509CertificateArray;
    }

    @Override
    public final Principal getPeerPrincipal() throws SSLPeerUnverifiedException {
        this.checkPeerCertificatesPresent();
        return this.peerCertificates[0].getSubjectX500Principal();
    }

    @Override
    public final Principal getLocalPrincipal() {
        X509Certificate[] x509CertificateArray = (X509Certificate[])this.getLocalCertificates();
        if (x509CertificateArray != null && x509CertificateArray.length > 0) {
            return x509CertificateArray[0].getSubjectX500Principal();
        }
        return null;
    }

    @Override
    public final String getCipherSuite() {
        String string;
        NativeSsl nativeSsl = this.ssl;
        synchronized (nativeSsl) {
            string = this.ssl.getCipherSuite();
        }
        if (string == null) {
            return "SSL_NULL_WITH_NULL_NULL";
        }
        return string;
    }

    @Override
    public final String getProtocol() {
        String string = this.protocol;
        if (string == null) {
            NativeSsl nativeSsl = this.ssl;
            synchronized (nativeSsl) {
                string = this.ssl.getVersion();
            }
            this.protocol = string;
        }
        return string;
    }

    @Override
    public final String getPeerHost() {
        return this.peerHost;
    }

    @Override
    public final int getPeerPort() {
        return this.peerPort;
    }

    @Override
    public final int getPacketBufferSize() {
        return 16709;
    }

    @Override
    public final int getApplicationBufferSize() {
        return 16384;
    }

    @Override
    public final String getApplicationProtocol() {
        String string = this.applicationProtocol;
        if (string == null) {
            NativeSsl nativeSsl = this.ssl;
            synchronized (nativeSsl) {
                string = SSLUtils.toProtocolString(this.ssl.getApplicationProtocol());
            }
            this.applicationProtocol = string;
        }
        return string;
    }

    final void onPeerCertificatesReceived(String string, int n2, X509Certificate[] x509CertificateArray) {
        this.configurePeer(string, n2, x509CertificateArray);
    }

    private void configurePeer(String object, int n2, X509Certificate[] x509CertificateArray) {
        this.peerHost = object;
        this.peerPort = n2;
        this.peerCertificates = x509CertificateArray;
        object = this.ssl;
        synchronized (object) {
            this.peerCertificateOcspData = this.ssl.getPeerCertificateOcspData();
            this.peerTlsSctData = this.ssl.getPeerTlsSctData();
            return;
        }
    }

    final void onPeerCertificateAvailable(String string, int n2) throws CertificateException {
        NativeSsl nativeSsl = this.ssl;
        synchronized (nativeSsl) {
            this.id = null;
            if (this.localCertificates == null) {
                this.localCertificates = this.ssl.getLocalCertificates();
            }
            if (this.peerCertificates == null) {
                this.configurePeer(string, n2, this.ssl.getPeerCertificates());
            }
            return;
        }
    }

    private void checkPeerCertificatesPresent() throws SSLPeerUnverifiedException {
        if (this.peerCertificates == null || this.peerCertificates.length == 0) {
            throw new SSLPeerUnverifiedException("No peer certificates");
        }
    }
}


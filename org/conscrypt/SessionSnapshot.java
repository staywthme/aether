/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.security.Principal;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSessionContext;
import org.conscrypt.ConscryptSession;

final class SessionSnapshot
implements ConscryptSession {
    private final SSLSessionContext sessionContext;
    private final byte[] id;
    private final String requestedServerName;
    private final List<byte[]> statusResponses;
    private final byte[] peerTlsSctData;
    private final long creationTime;
    private final long lastAccessedTime;
    private final String cipherSuite;
    private final String protocol;
    private final String peerHost;
    private final String applicationProtocol;
    private final int peerPort;

    SessionSnapshot(ConscryptSession conscryptSession) {
        this.sessionContext = conscryptSession.getSessionContext();
        this.id = conscryptSession.getId();
        this.requestedServerName = conscryptSession.getRequestedServerName();
        this.statusResponses = conscryptSession.getStatusResponses();
        this.peerTlsSctData = conscryptSession.getPeerSignedCertificateTimestamp();
        this.creationTime = conscryptSession.getCreationTime();
        this.lastAccessedTime = conscryptSession.getLastAccessedTime();
        this.cipherSuite = conscryptSession.getCipherSuite();
        this.protocol = conscryptSession.getProtocol();
        this.peerHost = conscryptSession.getPeerHost();
        this.peerPort = conscryptSession.getPeerPort();
        this.applicationProtocol = conscryptSession.getApplicationProtocol();
    }

    @Override
    public final String getRequestedServerName() {
        return this.requestedServerName;
    }

    @Override
    public final List<byte[]> getStatusResponses() {
        ArrayList<byte[]> arrayList = new ArrayList<byte[]>(this.statusResponses.size());
        for (byte[] byArray : this.statusResponses) {
            arrayList.add((byte[])byArray.clone());
        }
        return arrayList;
    }

    @Override
    public final byte[] getPeerSignedCertificateTimestamp() {
        if (this.peerTlsSctData != null) {
            return (byte[])this.peerTlsSctData.clone();
        }
        return null;
    }

    @Override
    public final byte[] getId() {
        return this.id;
    }

    @Override
    public final SSLSessionContext getSessionContext() {
        return this.sessionContext;
    }

    @Override
    public final long getCreationTime() {
        return this.creationTime;
    }

    @Override
    public final long getLastAccessedTime() {
        return this.lastAccessedTime;
    }

    @Override
    public final void invalidate() {
    }

    @Override
    public final boolean isValid() {
        return false;
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
        throw new SSLPeerUnverifiedException("No peer certificates");
    }

    @Override
    public final Certificate[] getLocalCertificates() {
        return null;
    }

    @Override
    public final javax.security.cert.X509Certificate[] getPeerCertificateChain() throws SSLPeerUnverifiedException {
        throw new SSLPeerUnverifiedException("No peer certificates");
    }

    @Override
    public final Principal getPeerPrincipal() throws SSLPeerUnverifiedException {
        throw new SSLPeerUnverifiedException("No peer certificates");
    }

    @Override
    public final Principal getLocalPrincipal() {
        return null;
    }

    @Override
    public final String getCipherSuite() {
        return this.cipherSuite;
    }

    @Override
    public final String getProtocol() {
        return this.protocol;
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
        return this.applicationProtocol;
    }
}


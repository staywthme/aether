/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.security.Principal;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.List;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSessionContext;
import org.conscrypt.ConscryptSession;
import org.conscrypt.EmptyArray;

final class SSLNullSession
implements Cloneable,
ConscryptSession {
    static final String INVALID_CIPHER = "SSL_NULL_WITH_NULL_NULL";
    private long creationTime;
    private long lastAccessedTime;

    static ConscryptSession getNullSession() {
        return DefaultHolder.NULL_SESSION;
    }

    private SSLNullSession() {
        this.lastAccessedTime = this.creationTime = System.currentTimeMillis();
    }

    @Override
    public final String getRequestedServerName() {
        return null;
    }

    @Override
    public final List<byte[]> getStatusResponses() {
        return Collections.emptyList();
    }

    @Override
    public final byte[] getPeerSignedCertificateTimestamp() {
        return EmptyArray.BYTE;
    }

    @Override
    public final int getApplicationBufferSize() {
        return 16384;
    }

    @Override
    public final String getApplicationProtocol() {
        return null;
    }

    @Override
    public final String getCipherSuite() {
        return INVALID_CIPHER;
    }

    @Override
    public final long getCreationTime() {
        return this.creationTime;
    }

    @Override
    public final byte[] getId() {
        return EmptyArray.BYTE;
    }

    @Override
    public final long getLastAccessedTime() {
        return this.lastAccessedTime;
    }

    @Override
    public final Certificate[] getLocalCertificates() {
        return null;
    }

    @Override
    public final Principal getLocalPrincipal() {
        return null;
    }

    @Override
    public final int getPacketBufferSize() {
        return 16709;
    }

    @Override
    public final javax.security.cert.X509Certificate[] getPeerCertificateChain() throws SSLPeerUnverifiedException {
        throw new SSLPeerUnverifiedException("No peer certificate");
    }

    @Override
    public final X509Certificate[] getPeerCertificates() throws SSLPeerUnverifiedException {
        throw new SSLPeerUnverifiedException("No peer certificate");
    }

    @Override
    public final String getPeerHost() {
        return null;
    }

    @Override
    public final int getPeerPort() {
        return -1;
    }

    @Override
    public final Principal getPeerPrincipal() throws SSLPeerUnverifiedException {
        throw new SSLPeerUnverifiedException("No peer certificate");
    }

    @Override
    public final String getProtocol() {
        return "NONE";
    }

    @Override
    public final SSLSessionContext getSessionContext() {
        return null;
    }

    @Override
    public final Object getValue(String string) {
        throw new UnsupportedOperationException("All calls to this method should be intercepted by ExternalSession.");
    }

    @Override
    public final String[] getValueNames() {
        throw new UnsupportedOperationException("All calls to this method should be intercepted by ExternalSession.");
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
    public final void removeValue(String string) {
        throw new UnsupportedOperationException("All calls to this method should be intercepted by ExternalSession.");
    }

    static class DefaultHolder {
        static final SSLNullSession NULL_SESSION = new SSLNullSession();

        private DefaultHolder() {
        }
    }
}


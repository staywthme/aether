/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.security.Principal;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSessionBindingEvent;
import javax.net.ssl.SSLSessionBindingListener;
import javax.net.ssl.SSLSessionContext;
import org.conscrypt.ConscryptSession;

final class ExternalSession
implements ConscryptSession {
    private final HashMap<String, Object> values = new HashMap(2);
    private final Provider provider;

    public ExternalSession(Provider provider) {
        this.provider = provider;
    }

    @Override
    public final String getRequestedServerName() {
        return this.provider.provideSession().getRequestedServerName();
    }

    @Override
    public final List<byte[]> getStatusResponses() {
        return this.provider.provideSession().getStatusResponses();
    }

    @Override
    public final byte[] getPeerSignedCertificateTimestamp() {
        return this.provider.provideSession().getPeerSignedCertificateTimestamp();
    }

    @Override
    public final byte[] getId() {
        return this.provider.provideSession().getId();
    }

    @Override
    public final SSLSessionContext getSessionContext() {
        return this.provider.provideSession().getSessionContext();
    }

    @Override
    public final long getCreationTime() {
        return this.provider.provideSession().getCreationTime();
    }

    @Override
    public final long getLastAccessedTime() {
        return this.provider.provideSession().getLastAccessedTime();
    }

    @Override
    public final void invalidate() {
        this.provider.provideSession().invalidate();
    }

    @Override
    public final boolean isValid() {
        return this.provider.provideSession().isValid();
    }

    @Override
    public final X509Certificate[] getPeerCertificates() throws SSLPeerUnverifiedException {
        return this.provider.provideSession().getPeerCertificates();
    }

    @Override
    public final Certificate[] getLocalCertificates() {
        return this.provider.provideSession().getLocalCertificates();
    }

    @Override
    public final javax.security.cert.X509Certificate[] getPeerCertificateChain() throws SSLPeerUnverifiedException {
        return this.provider.provideSession().getPeerCertificateChain();
    }

    @Override
    public final Principal getPeerPrincipal() throws SSLPeerUnverifiedException {
        return this.provider.provideSession().getPeerPrincipal();
    }

    @Override
    public final Principal getLocalPrincipal() {
        return this.provider.provideSession().getLocalPrincipal();
    }

    @Override
    public final String getCipherSuite() {
        return this.provider.provideSession().getCipherSuite();
    }

    @Override
    public final String getProtocol() {
        return this.provider.provideSession().getProtocol();
    }

    @Override
    public final String getPeerHost() {
        return this.provider.provideSession().getPeerHost();
    }

    @Override
    public final int getPeerPort() {
        return this.provider.provideSession().getPeerPort();
    }

    @Override
    public final int getPacketBufferSize() {
        return this.provider.provideSession().getPacketBufferSize();
    }

    @Override
    public final int getApplicationBufferSize() {
        return this.provider.provideSession().getApplicationBufferSize();
    }

    @Override
    public final String getApplicationProtocol() {
        return this.provider.provideSession().getApplicationProtocol();
    }

    @Override
    public final Object getValue(String string) {
        if (string == null) {
            throw new IllegalArgumentException("name == null");
        }
        return this.values.get(string);
    }

    @Override
    public final String[] getValueNames() {
        return this.values.keySet().toArray(new String[this.values.size()]);
    }

    @Override
    public final void putValue(String string, Object object) {
        ExternalSession externalSession = this;
        externalSession.putValue(externalSession, string, object);
    }

    final void putValue(SSLSession sSLSession, String string, Object object) {
        if (string == null || object == null) {
            throw new IllegalArgumentException("name == null || value == null");
        }
        Object object2 = this.values.put(string, object);
        if (object instanceof SSLSessionBindingListener) {
            ((SSLSessionBindingListener)object).valueBound(new SSLSessionBindingEvent(sSLSession, string));
        }
        if (object2 instanceof SSLSessionBindingListener) {
            ((SSLSessionBindingListener)object2).valueUnbound(new SSLSessionBindingEvent(sSLSession, string));
        }
    }

    @Override
    public final void removeValue(String string) {
        ExternalSession externalSession = this;
        externalSession.removeValue(externalSession, string);
    }

    final void removeValue(SSLSession sSLSession, String string) {
        if (string == null) {
            throw new IllegalArgumentException("name == null");
        }
        Object object = this.values.remove(string);
        if (object instanceof SSLSessionBindingListener) {
            object = (SSLSessionBindingListener)object;
            object.valueUnbound(new SSLSessionBindingEvent(sSLSession, string));
        }
    }

    static interface Provider {
        public ConscryptSession provideSession();
    }
}


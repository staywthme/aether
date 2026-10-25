/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.security.cert.X509Certificate;
import java.util.List;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;

interface ConscryptSession
extends SSLSession {
    public String getRequestedServerName();

    public List<byte[]> getStatusResponses();

    public byte[] getPeerSignedCertificateTimestamp();

    public X509Certificate[] getPeerCertificates() throws SSLPeerUnverifiedException;

    public String getApplicationProtocol();
}


/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.security.cert.X509Certificate;
import java.util.Set;

public interface ConscryptCertStore {
    public X509Certificate getTrustAnchor(X509Certificate var1);

    public Set<X509Certificate> findAllIssuers(X509Certificate var1);
}


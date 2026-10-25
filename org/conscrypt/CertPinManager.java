/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

public interface CertPinManager {
    public void checkChainPinning(String var1, List<X509Certificate> var2) throws CertificateException;
}


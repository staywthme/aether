/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.security.cert.X509Certificate;
import javax.net.ssl.SSLSession;

public interface ConscryptHostnameVerifier {
    public boolean verify(X509Certificate[] var1, String var2, SSLSession var3);
}


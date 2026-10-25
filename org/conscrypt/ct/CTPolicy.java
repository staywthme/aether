/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt.ct;

import java.security.cert.X509Certificate;
import org.conscrypt.ct.CTVerificationResult;

public interface CTPolicy {
    public boolean doesResultConformToPolicy(CTVerificationResult var1, String var2, X509Certificate[] var3);
}


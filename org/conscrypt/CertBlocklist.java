/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.math.BigInteger;
import java.security.PublicKey;

public interface CertBlocklist {
    public boolean isPublicKeyBlockListed(PublicKey var1);

    public boolean isSerialNumberBlockListed(BigInteger var1);
}


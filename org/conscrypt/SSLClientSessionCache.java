/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import javax.net.ssl.SSLSession;

public interface SSLClientSessionCache {
    public byte[] getSessionData(String var1, int var2);

    public void putSessionData(SSLSession var1, byte[] var2);
}


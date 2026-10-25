/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import javax.net.ssl.SSLSession;

interface SSLServerSessionCache {
    public byte[] getSessionData(byte[] var1);

    public void putSessionData(SSLSession var1, byte[] var2);
}


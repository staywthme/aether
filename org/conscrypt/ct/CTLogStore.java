/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt.ct;

import org.conscrypt.ct.CTLogInfo;

public interface CTLogStore {
    public CTLogInfo getKnownLog(byte[] var1);
}


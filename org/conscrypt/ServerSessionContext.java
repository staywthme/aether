/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import org.conscrypt.AbstractSessionContext;
import org.conscrypt.NativeCrypto;
import org.conscrypt.NativeSslSession;
import org.conscrypt.SSLServerSessionCache;

public final class ServerSessionContext
extends AbstractSessionContext {
    private SSLServerSessionCache persistentCache;

    ServerSessionContext() {
        super(100);
        NativeCrypto.SSL_CTX_set_session_id_context(this.sslCtxNativePointer, this, new byte[]{32});
    }

    public final void setPersistentCache(SSLServerSessionCache sSLServerSessionCache) {
        this.persistentCache = sSLServerSessionCache;
    }

    @Override
    final NativeSslSession getSessionFromPersistentCache(byte[] object) {
        if (this.persistentCache != null && (object = this.persistentCache.getSessionData((byte[])object)) != null) {
            NativeSslSession nativeSslSession = NativeSslSession.newInstance(this, (byte[])object, null, -1);
            object = nativeSslSession;
            if (nativeSslSession != null && ((NativeSslSession)object).isValid()) {
                this.cacheSession((NativeSslSession)object);
                return object;
            }
        }
        return null;
    }

    @Override
    final void onBeforeAddSession(NativeSslSession nativeSslSession) {
        byte[] byArray;
        if (this.persistentCache != null && (byArray = nativeSslSession.toBytes()) != null) {
            this.persistentCache.putSessionData(nativeSslSession.toSSLSession(), byArray);
        }
    }

    @Override
    final void onBeforeRemoveSession(NativeSslSession nativeSslSession) {
    }
}


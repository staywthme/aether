/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSessionContext;
import org.conscrypt.ByteArray;
import org.conscrypt.NativeCrypto;
import org.conscrypt.NativeSslSession;

abstract class AbstractSessionContext
implements SSLSessionContext {
    private static final int DEFAULT_SESSION_TIMEOUT_SECONDS = 28800;
    private volatile int maximumSize;
    private volatile int timeout = 28800;
    final long sslCtxNativePointer = NativeCrypto.SSL_CTX_new();
    private final Map<ByteArray, NativeSslSession> sessions = new LinkedHashMap<ByteArray, NativeSslSession>(){

        @Override
        protected boolean removeEldestEntry(Map.Entry<ByteArray, NativeSslSession> entry) {
            if (AbstractSessionContext.this.maximumSize > 0 && this.size() > AbstractSessionContext.this.maximumSize) {
                AbstractSessionContext.this.onBeforeRemoveSession(entry.getValue());
                return true;
            }
            return false;
        }
    };

    AbstractSessionContext(int n2) {
        this.maximumSize = n2;
    }

    @Override
    public final Enumeration<byte[]> getIds() {
        Iterator<NativeSslSession> iterator2;
        Map<ByteArray, NativeSslSession> map = this.sessions;
        synchronized (map) {
            iterator2 = Arrays.asList(this.sessions.values().toArray(new NativeSslSession[this.sessions.size()])).iterator();
        }
        return new Enumeration<byte[]>(){
            private NativeSslSession next;

            @Override
            public boolean hasMoreElements() {
                if (this.next != null) {
                    return true;
                }
                while (iterator2.hasNext()) {
                    NativeSslSession nativeSslSession = (NativeSslSession)iterator2.next();
                    if (!nativeSslSession.isValid()) continue;
                    this.next = nativeSslSession;
                    return true;
                }
                this.next = null;
                return false;
            }

            @Override
            public byte[] nextElement() {
                if (this.hasMoreElements()) {
                    byte[] byArray = this.next.getId();
                    this.next = null;
                    return byArray;
                }
                throw new NoSuchElementException();
            }
        };
    }

    @Override
    public final SSLSession getSession(byte[] object) {
        if (object == null) {
            throw new NullPointerException("sessionId");
        }
        object = new ByteArray((byte[])object);
        Map<ByteArray, NativeSslSession> map = this.sessions;
        synchronized (map) {
            object = this.sessions.get(object);
        }
        if (object != null && ((NativeSslSession)object).isValid()) {
            return ((NativeSslSession)object).toSSLSession();
        }
        return null;
    }

    @Override
    public final int getSessionCacheSize() {
        return this.maximumSize;
    }

    @Override
    public final int getSessionTimeout() {
        return this.timeout;
    }

    @Override
    public final void setSessionTimeout(int n2) throws IllegalArgumentException {
        if (n2 < 0) {
            throw new IllegalArgumentException("seconds < 0");
        }
        Map<ByteArray, NativeSslSession> map = this.sessions;
        synchronized (map) {
            this.timeout = n2;
            if (n2 > 0) {
                NativeCrypto.SSL_CTX_set_timeout(this.sslCtxNativePointer, this, n2);
            } else {
                NativeCrypto.SSL_CTX_set_timeout(this.sslCtxNativePointer, this, Integer.MAX_VALUE);
            }
            Iterator<NativeSslSession> iterator2 = this.sessions.values().iterator();
            while (iterator2.hasNext()) {
                NativeSslSession nativeSslSession = iterator2.next();
                if (nativeSslSession.isValid()) continue;
                this.onBeforeRemoveSession(nativeSslSession);
                iterator2.remove();
            }
            return;
        }
    }

    @Override
    public final void setSessionCacheSize(int n2) throws IllegalArgumentException {
        if (n2 < 0) {
            throw new IllegalArgumentException("size < 0");
        }
        int n3 = this.maximumSize;
        this.maximumSize = n2;
        if (n2 < n3) {
            this.trimToSize();
        }
    }

    protected void finalize() throws Throwable {
        try {
            NativeCrypto.SSL_CTX_free(this.sslCtxNativePointer, this);
            return;
        }
        finally {
            super.finalize();
        }
    }

    final void cacheSession(NativeSslSession nativeSslSession) {
        Object object = nativeSslSession.getId();
        if (object == null || ((byte[])object).length == 0) {
            return;
        }
        Map<ByteArray, NativeSslSession> map = this.sessions;
        synchronized (map) {
            object = new ByteArray((byte[])object);
            if (this.sessions.containsKey(object)) {
                AbstractSessionContext abstractSessionContext = this;
                abstractSessionContext.removeSession(abstractSessionContext.sessions.get(object));
            }
            this.onBeforeAddSession(nativeSslSession);
            this.sessions.put((ByteArray)object, nativeSslSession);
            return;
        }
    }

    final void removeSession(NativeSslSession object) {
        Object object2 = ((NativeSslSession)object).getId();
        if (object2 == null || ((byte[])object2).length == 0) {
            return;
        }
        this.onBeforeRemoveSession((NativeSslSession)object);
        object = new ByteArray((byte[])object2);
        Map<ByteArray, NativeSslSession> map = this.sessions;
        object2 = map;
        synchronized (map) {
            this.sessions.remove(object);
            // ** MonitorExit[var2_3 /* !! */ ] (shouldn't be in output)
            return;
        }
    }

    final NativeSslSession getSessionFromCache(byte[] byArray) {
        NativeSslSession nativeSslSession;
        if (byArray == null) {
            return null;
        }
        Map<ByteArray, NativeSslSession> map = this.sessions;
        synchronized (map) {
            nativeSslSession = this.sessions.get(new ByteArray(byArray));
        }
        if (nativeSslSession != null && nativeSslSession.isValid()) {
            if (nativeSslSession.isSingleUse()) {
                this.removeSession(nativeSslSession);
            }
            return nativeSslSession;
        }
        return this.getSessionFromPersistentCache(byArray);
    }

    abstract void onBeforeAddSession(NativeSslSession var1);

    abstract void onBeforeRemoveSession(NativeSslSession var1);

    abstract NativeSslSession getSessionFromPersistentCache(byte[] var1);

    private void trimToSize() {
        Map<ByteArray, NativeSslSession> map = this.sessions;
        synchronized (map) {
            int n2 = this.sessions.size();
            if (n2 > this.maximumSize) {
                n2 -= this.maximumSize;
                Iterator<NativeSslSession> iterator2 = this.sessions.values().iterator();
                while (n2-- > 0) {
                    NativeSslSession nativeSslSession = iterator2.next();
                    this.onBeforeRemoveSession(nativeSslSession);
                    iterator2.remove();
                }
            }
            return;
        }
    }
}


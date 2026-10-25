/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.conscrypt.AbstractSessionContext;
import org.conscrypt.NativeSslSession;
import org.conscrypt.SSLClientSessionCache;
import org.conscrypt.SSLParametersImpl;

public final class ClientSessionContext
extends AbstractSessionContext {
    private final Map<HostAndPort, List<NativeSslSession>> sessionsByHostAndPort = new HashMap<HostAndPort, List<NativeSslSession>>();
    private SSLClientSessionCache persistentCache;

    ClientSessionContext() {
        super(10);
    }

    public final void setPersistentCache(SSLClientSessionCache sSLClientSessionCache) {
        this.persistentCache = sSLClientSessionCache;
    }

    final synchronized NativeSslSession getCachedSession(String object, int n2, SSLParametersImpl object22) {
        if (object == null) {
            return null;
        }
        if ((object = this.getSession((String)object, n2)) == null) {
            return null;
        }
        String string = ((NativeSslSession)object).getProtocol();
        boolean bl = false;
        Object object2 = ((SSLParametersImpl)object22).enabledProtocols;
        int n3 = ((SSLParametersImpl)object22).enabledProtocols.length;
        for (int i2 = 0; i2 < n3; ++i2) {
            String string2 = object2[i2];
            if (!string.equals(string2)) continue;
            bl = true;
            break;
        }
        if (!bl) {
            return null;
        }
        object2 = ((NativeSslSession)object).getCipherSuite();
        n3 = 0;
        for (String string3 : ((SSLParametersImpl)object22).getEnabledCipherSuites()) {
            if (!((String)object2).equals(string3)) continue;
            n3 = 1;
            break;
        }
        if (n3 == 0) {
            return null;
        }
        if (((NativeSslSession)object).isSingleUse()) {
            this.removeSession((NativeSslSession)object);
        }
        return object;
    }

    final int size() {
        int n2 = 0;
        Map<HostAndPort, List<NativeSslSession>> map = this.sessionsByHostAndPort;
        synchronized (map) {
            for (List<NativeSslSession> list2 : this.sessionsByHostAndPort.values()) {
                n2 += list2.size();
            }
        }
        return n2;
    }

    private NativeSslSession getSession(String string, int n2) {
        if (string == null) {
            return null;
        }
        HostAndPort hostAndPort = new HostAndPort(string, n2);
        NativeSslSession nativeSslSession = null;
        Object object = this.sessionsByHostAndPort;
        synchronized (object) {
            List<NativeSslSession> list2 = this.sessionsByHostAndPort.get(hostAndPort);
            if (list2 != null && list2.size() > 0) {
                nativeSslSession = list2.get(0);
            }
        }
        if (nativeSslSession != null && nativeSslSession.isValid()) {
            return nativeSslSession;
        }
        if (this.persistentCache != null) {
            byte[] byArray = this.persistentCache.getSessionData(string, n2);
            object = byArray;
            if (byArray != null && (nativeSslSession = NativeSslSession.newInstance(this, (byte[])object, string, n2)) != null && nativeSslSession.isValid()) {
                this.putSession(hostAndPort, nativeSslSession);
                return nativeSslSession;
            }
        }
        return null;
    }

    private void putSession(HostAndPort hostAndPort, NativeSslSession nativeSslSession) {
        Map<HostAndPort, List<NativeSslSession>> map = this.sessionsByHostAndPort;
        synchronized (map) {
            List<NativeSslSession> list2 = this.sessionsByHostAndPort.get(hostAndPort);
            if (list2 == null) {
                list2 = new ArrayList<NativeSslSession>();
                this.sessionsByHostAndPort.put(hostAndPort, list2);
            }
            if (list2.size() > 0 && list2.get(0).isSingleUse() != nativeSslSession.isSingleUse()) {
                while (!list2.isEmpty()) {
                    this.removeSession(list2.get(0));
                }
                this.sessionsByHostAndPort.put(hostAndPort, list2);
            }
            list2.add(nativeSslSession);
            return;
        }
    }

    private void removeSession(HostAndPort hostAndPort, NativeSslSession nativeSslSession) {
        Map<HostAndPort, List<NativeSslSession>> map = this.sessionsByHostAndPort;
        synchronized (map) {
            List<NativeSslSession> list2 = this.sessionsByHostAndPort.get(hostAndPort);
            if (list2 != null) {
                list2.remove(nativeSslSession);
                if (list2.isEmpty()) {
                    this.sessionsByHostAndPort.remove(hostAndPort);
                }
            }
            return;
        }
    }

    @Override
    final void onBeforeAddSession(NativeSslSession nativeSslSession) {
        Object object = nativeSslSession.getPeerHost();
        int n2 = nativeSslSession.getPeerPort();
        if (object == null) {
            return;
        }
        object = new HostAndPort((String)object, n2);
        this.putSession((HostAndPort)object, nativeSslSession);
        if (this.persistentCache != null && !nativeSslSession.isSingleUse()) {
            byte[] byArray = nativeSslSession.toBytes();
            object = byArray;
            if (byArray != null) {
                this.persistentCache.putSessionData(nativeSslSession.toSSLSession(), (byte[])object);
            }
        }
    }

    @Override
    final void onBeforeRemoveSession(NativeSslSession nativeSslSession) {
        Object object = nativeSslSession.getPeerHost();
        if (object == null) {
            return;
        }
        int n2 = nativeSslSession.getPeerPort();
        object = new HostAndPort((String)object, n2);
        this.removeSession((HostAndPort)object, nativeSslSession);
    }

    @Override
    final NativeSslSession getSessionFromPersistentCache(byte[] byArray) {
        return null;
    }

    static final class HostAndPort {
        final String host;
        final int port;

        HostAndPort(String string, int n2) {
            this.host = string;
            this.port = n2;
        }

        public final int hashCode() {
            return this.host.hashCode() * 31 + this.port;
        }

        public final boolean equals(Object object) {
            if (!(object instanceof HostAndPort)) {
                return false;
            }
            object = (HostAndPort)object;
            return this.host.equals(((HostAndPort)object).host) && this.port == ((HostAndPort)object).port;
        }
    }
}


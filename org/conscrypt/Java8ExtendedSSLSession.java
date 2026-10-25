/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.util.Collections;
import java.util.List;
import javax.net.ssl.SNIHostName;
import javax.net.ssl.SNIServerName;
import org.conscrypt.ExternalSession;
import org.conscrypt.Java7ExtendedSSLSession;

class Java8ExtendedSSLSession
extends Java7ExtendedSSLSession {
    public Java8ExtendedSSLSession(ExternalSession externalSession) {
        super(externalSession);
    }

    @Override
    public final List<SNIServerName> getRequestedServerNames() {
        String string = this.delegate.getRequestedServerName();
        if (string == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(new SNIHostName(string));
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.util.Arrays;
import java.util.List;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSocket;
import org.conscrypt.ApplicationProtocolSelector;
import org.conscrypt.Preconditions;
import org.conscrypt.SSLUtils;

final class ApplicationProtocolSelectorAdapter {
    private static final int NO_PROTOCOL_SELECTED = -1;
    private final SSLEngine engine;
    private final SSLSocket socket;
    private final ApplicationProtocolSelector selector;

    ApplicationProtocolSelectorAdapter(SSLEngine sSLEngine, ApplicationProtocolSelector applicationProtocolSelector) {
        this.engine = Preconditions.checkNotNull(sSLEngine, "engine");
        this.socket = null;
        this.selector = Preconditions.checkNotNull(applicationProtocolSelector, "selector");
    }

    ApplicationProtocolSelectorAdapter(SSLSocket sSLSocket, ApplicationProtocolSelector applicationProtocolSelector) {
        this.engine = null;
        this.socket = Preconditions.checkNotNull(sSLSocket, "socket");
        this.selector = Preconditions.checkNotNull(applicationProtocolSelector, "selector");
    }

    final int selectApplicationProtocol(byte[] object) {
        if (object == null || ((byte[])object).length == 0) {
            return -1;
        }
        object = Arrays.asList(SSLUtils.decodeProtocols((byte[])object));
        String string = this.engine != null ? this.selector.selectApplicationProtocol(this.engine, (List<String>)object) : this.selector.selectApplicationProtocol(this.socket, (List<String>)object);
        if (string == null || string.isEmpty()) {
            return -1;
        }
        int n2 = 0;
        object = object.iterator();
        while (object.hasNext()) {
            String string2 = (String)object.next();
            if (string.equals(string2)) {
                return n2;
            }
            n2 += 1 + string2.length();
        }
        return -1;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.util.function.BiFunction;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSocket;
import org.conscrypt.ApplicationProtocolSelector;
import org.conscrypt.ConscryptEngineSocket;
import org.conscrypt.SSLParametersImpl;

final class Java8EngineSocket
extends ConscryptEngineSocket {
    private BiFunction<SSLSocket, List<String>, String> selector;

    Java8EngineSocket(SSLParametersImpl sSLParametersImpl) throws IOException {
        super(sSLParametersImpl);
    }

    Java8EngineSocket(String string, int n2, SSLParametersImpl sSLParametersImpl) throws IOException {
        super(string, n2, sSLParametersImpl);
    }

    Java8EngineSocket(InetAddress inetAddress, int n2, SSLParametersImpl sSLParametersImpl) throws IOException {
        super(inetAddress, n2, sSLParametersImpl);
    }

    Java8EngineSocket(String string, int n2, InetAddress inetAddress, int n3, SSLParametersImpl sSLParametersImpl) throws IOException {
        super(string, n2, inetAddress, n3, sSLParametersImpl);
    }

    Java8EngineSocket(InetAddress inetAddress, int n2, InetAddress inetAddress2, int n3, SSLParametersImpl sSLParametersImpl) throws IOException {
        super(inetAddress, n2, inetAddress2, n3, sSLParametersImpl);
    }

    Java8EngineSocket(Socket socket, String string, int n2, boolean bl, SSLParametersImpl sSLParametersImpl) throws IOException {
        super(socket, string, n2, bl, sSLParametersImpl);
    }

    @Override
    public final void setHandshakeApplicationProtocolSelector(BiFunction<SSLSocket, List<String>, String> biFunction) {
        this.selector = biFunction;
        this.setApplicationProtocolSelector(Java8EngineSocket.toApplicationProtocolSelector(biFunction));
    }

    @Override
    public final BiFunction<SSLSocket, List<String>, String> getHandshakeApplicationProtocolSelector() {
        return this.selector;
    }

    private static ApplicationProtocolSelector toApplicationProtocolSelector(final BiFunction<SSLSocket, List<String>, String> biFunction) {
        if (biFunction == null) {
            return null;
        }
        return new ApplicationProtocolSelector(){

            @Override
            public String selectApplicationProtocol(SSLEngine sSLEngine, List<String> list2) {
                throw new UnsupportedOperationException();
            }

            @Override
            public String selectApplicationProtocol(SSLSocket sSLSocket, List<String> list2) {
                return (String)biFunction.apply(sSLSocket, list2);
            }
        };
    }
}


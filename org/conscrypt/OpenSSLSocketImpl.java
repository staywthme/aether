/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.io.FileDescriptor;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.security.PrivateKey;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import org.conscrypt.AbstractConscryptSocket;
import org.conscrypt.EmptyArray;
import org.conscrypt.SSLUtils;

public abstract class OpenSSLSocketImpl
extends AbstractConscryptSocket {
    OpenSSLSocketImpl() throws IOException {
    }

    OpenSSLSocketImpl(String string, int n2) throws IOException {
        super(string, n2);
    }

    OpenSSLSocketImpl(InetAddress inetAddress, int n2) throws IOException {
        super(inetAddress, n2);
    }

    OpenSSLSocketImpl(String string, int n2, InetAddress inetAddress, int n3) throws IOException {
        super(string, n2, inetAddress, n3);
    }

    OpenSSLSocketImpl(InetAddress inetAddress, int n2, InetAddress inetAddress2, int n3) throws IOException {
        super(inetAddress, n2, inetAddress2, n3);
    }

    OpenSSLSocketImpl(Socket socket, String string, int n2, boolean bl) throws IOException {
        super(socket, string, n2, bl);
    }

    @Override
    public String getHostname() {
        return super.getHostname();
    }

    @Override
    public void setHostname(String string) {
        super.setHostname(string);
    }

    @Override
    public String getHostnameOrIP() {
        return super.getHostnameOrIP();
    }

    @Override
    public FileDescriptor getFileDescriptor$() {
        return super.getFileDescriptor$();
    }

    @Override
    public void setSoWriteTimeout(int n2) throws SocketException {
        super.setSoWriteTimeout(n2);
    }

    @Override
    public int getSoWriteTimeout() throws SocketException {
        return super.getSoWriteTimeout();
    }

    @Override
    public void setHandshakeTimeout(int n2) throws SocketException {
        super.setHandshakeTimeout(n2);
    }

    @Override
    public abstract SSLSession getHandshakeSession();

    @Override
    public abstract void setUseSessionTickets(boolean var1);

    @Override
    public abstract void setChannelIdEnabled(boolean var1);

    @Override
    public abstract byte[] getChannelId() throws SSLException;

    @Override
    public abstract void setChannelIdPrivateKey(PrivateKey var1);

    @Override
    @Deprecated
    public final byte[] getNpnSelectedProtocol() {
        return super.getNpnSelectedProtocol();
    }

    @Override
    @Deprecated
    public final void setNpnProtocols(byte[] byArray) {
        super.setNpnProtocols(byArray);
    }

    @Override
    @Deprecated
    public final void setAlpnProtocols(String[] stringArray) {
        this.setApplicationProtocols(stringArray == null ? EmptyArray.STRING : stringArray);
    }

    @Override
    @Deprecated
    public final byte[] getAlpnSelectedProtocol() {
        return SSLUtils.toProtocolBytes(this.getApplicationProtocol());
    }

    @Override
    @Deprecated
    public final void setAlpnProtocols(byte[] byArray) {
        this.setApplicationProtocols(SSLUtils.decodeProtocols(byArray == null ? EmptyArray.BYTE : byArray));
    }
}


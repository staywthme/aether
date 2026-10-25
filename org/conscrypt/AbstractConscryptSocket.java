/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.channels.SocketChannel;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import org.conscrypt.ApplicationProtocolSelector;
import org.conscrypt.ApplicationProtocolSelectorAdapter;
import org.conscrypt.PeerInfoProvider;
import org.conscrypt.Platform;
import org.conscrypt.Preconditions;

abstract class AbstractConscryptSocket
extends SSLSocket {
    final Socket socket;
    private final boolean autoClose;
    private String peerHostname;
    private final int peerPort;
    private final PeerInfoProvider peerInfoProvider = new PeerInfoProvider(){

        @Override
        String getHostname() {
            return AbstractConscryptSocket.this.getHostname();
        }

        @Override
        String getHostnameOrIP() {
            return AbstractConscryptSocket.this.getHostnameOrIP();
        }

        @Override
        int getPort() {
            return AbstractConscryptSocket.this.getPort();
        }
    };
    private final List<HandshakeCompletedListener> listeners = new ArrayList<HandshakeCompletedListener>(2);
    private int readTimeoutMilliseconds;

    AbstractConscryptSocket() throws IOException {
        this.socket = this;
        this.peerHostname = null;
        this.peerPort = -1;
        this.autoClose = false;
    }

    AbstractConscryptSocket(String string, int n2) throws IOException {
        super(string, n2);
        this.socket = this;
        this.peerHostname = string;
        this.peerPort = n2;
        this.autoClose = false;
    }

    AbstractConscryptSocket(InetAddress inetAddress, int n2) throws IOException {
        super(inetAddress, n2);
        this.socket = this;
        this.peerHostname = null;
        this.peerPort = -1;
        this.autoClose = false;
    }

    AbstractConscryptSocket(String string, int n2, InetAddress inetAddress, int n3) throws IOException {
        super(string, n2, inetAddress, n3);
        this.socket = this;
        this.peerHostname = string;
        this.peerPort = n2;
        this.autoClose = false;
    }

    AbstractConscryptSocket(InetAddress inetAddress, int n2, InetAddress inetAddress2, int n3) throws IOException {
        super(inetAddress, n2, inetAddress2, n3);
        this.socket = this;
        this.peerHostname = null;
        this.peerPort = -1;
        this.autoClose = false;
    }

    AbstractConscryptSocket(Socket socket, String string, int n2, boolean bl) throws IOException {
        this.socket = Preconditions.checkNotNull(socket, "socket");
        this.peerHostname = string;
        this.peerPort = n2;
        this.autoClose = bl;
    }

    @Override
    public final void connect(SocketAddress socketAddress) throws IOException {
        this.connect(socketAddress, 0);
    }

    @Override
    public final void connect(SocketAddress socketAddress, int n2) throws IOException {
        if (this.peerHostname == null && socketAddress instanceof InetSocketAddress) {
            this.peerHostname = Platform.getHostStringFromInetSocketAddress((InetSocketAddress)socketAddress);
        }
        if (this.isDelegating()) {
            this.socket.connect(socketAddress, n2);
            return;
        }
        super.connect(socketAddress, n2);
    }

    @Override
    public void bind(SocketAddress socketAddress) throws IOException {
        if (this.isDelegating()) {
            this.socket.bind(socketAddress);
            return;
        }
        super.bind(socketAddress);
    }

    @Override
    public void close() throws IOException {
        if (this.isDelegating()) {
            if (this.autoClose && !this.socket.isClosed()) {
                this.socket.close();
                return;
            }
        } else if (!super.isClosed()) {
            super.close();
        }
    }

    @Override
    public InetAddress getInetAddress() {
        if (this.isDelegating()) {
            return this.socket.getInetAddress();
        }
        return super.getInetAddress();
    }

    @Override
    public InetAddress getLocalAddress() {
        if (this.isDelegating()) {
            return this.socket.getLocalAddress();
        }
        return super.getLocalAddress();
    }

    @Override
    public int getLocalPort() {
        if (this.isDelegating()) {
            return this.socket.getLocalPort();
        }
        return super.getLocalPort();
    }

    @Override
    public SocketAddress getRemoteSocketAddress() {
        if (this.isDelegating()) {
            return this.socket.getRemoteSocketAddress();
        }
        return super.getRemoteSocketAddress();
    }

    @Override
    public SocketAddress getLocalSocketAddress() {
        if (this.isDelegating()) {
            return this.socket.getLocalSocketAddress();
        }
        return super.getLocalSocketAddress();
    }

    @Override
    public final int getPort() {
        if (this.isDelegating()) {
            return this.socket.getPort();
        }
        if (this.peerPort != -1) {
            return this.peerPort;
        }
        return super.getPort();
    }

    @Override
    public void addHandshakeCompletedListener(HandshakeCompletedListener handshakeCompletedListener) {
        Preconditions.checkArgument(handshakeCompletedListener != null, "Provided listener is null");
        this.listeners.add(handshakeCompletedListener);
    }

    @Override
    public void removeHandshakeCompletedListener(HandshakeCompletedListener handshakeCompletedListener) {
        Preconditions.checkArgument(handshakeCompletedListener != null, "Provided listener is null");
        if (!this.listeners.remove(handshakeCompletedListener)) {
            throw new IllegalArgumentException("Provided listener is not registered");
        }
    }

    public FileDescriptor getFileDescriptor$() {
        if (this.isDelegating()) {
            return Platform.getFileDescriptor(this.socket);
        }
        return Platform.getFileDescriptorFromSSLSocket(this);
    }

    @Override
    public final void setSoTimeout(int n2) throws SocketException {
        if (this.isDelegating()) {
            this.socket.setSoTimeout(n2);
            return;
        }
        super.setSoTimeout(n2);
        this.readTimeoutMilliseconds = n2;
    }

    @Override
    public final int getSoTimeout() throws SocketException {
        if (this.isDelegating()) {
            return this.socket.getSoTimeout();
        }
        return this.readTimeoutMilliseconds;
    }

    @Override
    public final void sendUrgentData(int n2) throws IOException {
        throw new SocketException("Method sendUrgentData() is not supported.");
    }

    @Override
    public final void setOOBInline(boolean bl) throws SocketException {
        throw new SocketException("Method setOOBInline() is not supported.");
    }

    @Override
    public boolean getOOBInline() throws SocketException {
        return false;
    }

    @Override
    public SocketChannel getChannel() {
        return null;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        if (this.isDelegating()) {
            return this.socket.getInputStream();
        }
        return super.getInputStream();
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        if (this.isDelegating()) {
            return this.socket.getOutputStream();
        }
        return super.getOutputStream();
    }

    @Override
    public void setTcpNoDelay(boolean bl) throws SocketException {
        if (this.isDelegating()) {
            this.socket.setTcpNoDelay(bl);
            return;
        }
        super.setTcpNoDelay(bl);
    }

    @Override
    public boolean getTcpNoDelay() throws SocketException {
        if (this.isDelegating()) {
            return this.socket.getTcpNoDelay();
        }
        return super.getTcpNoDelay();
    }

    @Override
    public void setSoLinger(boolean bl, int n2) throws SocketException {
        if (this.isDelegating()) {
            this.socket.setSoLinger(bl, n2);
            return;
        }
        super.setSoLinger(bl, n2);
    }

    @Override
    public int getSoLinger() throws SocketException {
        if (this.isDelegating()) {
            return this.socket.getSoLinger();
        }
        return super.getSoLinger();
    }

    @Override
    public void setSendBufferSize(int n2) throws SocketException {
        if (this.isDelegating()) {
            this.socket.setSendBufferSize(n2);
            return;
        }
        super.setSendBufferSize(n2);
    }

    @Override
    public int getSendBufferSize() throws SocketException {
        if (this.isDelegating()) {
            return this.socket.getSendBufferSize();
        }
        return super.getSendBufferSize();
    }

    @Override
    public void setReceiveBufferSize(int n2) throws SocketException {
        if (this.isDelegating()) {
            this.socket.setReceiveBufferSize(n2);
            return;
        }
        super.setReceiveBufferSize(n2);
    }

    @Override
    public int getReceiveBufferSize() throws SocketException {
        if (this.isDelegating()) {
            return this.socket.getReceiveBufferSize();
        }
        return super.getReceiveBufferSize();
    }

    @Override
    public void setKeepAlive(boolean bl) throws SocketException {
        if (this.isDelegating()) {
            this.socket.setKeepAlive(bl);
            return;
        }
        super.setKeepAlive(bl);
    }

    @Override
    public boolean getKeepAlive() throws SocketException {
        if (this.isDelegating()) {
            return this.socket.getKeepAlive();
        }
        return super.getKeepAlive();
    }

    @Override
    public void setTrafficClass(int n2) throws SocketException {
        if (this.isDelegating()) {
            this.socket.setTrafficClass(n2);
            return;
        }
        super.setTrafficClass(n2);
    }

    @Override
    public int getTrafficClass() throws SocketException {
        if (this.isDelegating()) {
            return this.socket.getTrafficClass();
        }
        return super.getTrafficClass();
    }

    @Override
    public void setReuseAddress(boolean bl) throws SocketException {
        if (this.isDelegating()) {
            this.socket.setReuseAddress(bl);
            return;
        }
        super.setReuseAddress(bl);
    }

    @Override
    public boolean getReuseAddress() throws SocketException {
        if (this.isDelegating()) {
            return this.socket.getReuseAddress();
        }
        return super.getReuseAddress();
    }

    @Override
    public void shutdownInput() throws IOException {
        if (this.isDelegating()) {
            this.socket.shutdownInput();
            return;
        }
        super.shutdownInput();
    }

    @Override
    public void shutdownOutput() throws IOException {
        if (this.isDelegating()) {
            this.socket.shutdownOutput();
            return;
        }
        super.shutdownOutput();
    }

    @Override
    public boolean isConnected() {
        if (this.isDelegating()) {
            return this.socket.isConnected();
        }
        return super.isConnected();
    }

    @Override
    public boolean isBound() {
        if (this.isDelegating()) {
            return this.socket.isBound();
        }
        return super.isBound();
    }

    @Override
    public boolean isClosed() {
        if (this.isDelegating()) {
            return this.socket.isClosed();
        }
        return super.isClosed();
    }

    @Override
    public boolean isInputShutdown() {
        if (this.isDelegating()) {
            return this.socket.isInputShutdown();
        }
        return super.isInputShutdown();
    }

    @Override
    public boolean isOutputShutdown() {
        if (this.isDelegating()) {
            return this.socket.isOutputShutdown();
        }
        return super.isOutputShutdown();
    }

    @Override
    public void setPerformancePreferences(int n2, int n3, int n4) {
        if (this.isDelegating()) {
            this.socket.setPerformancePreferences(n2, n3, n4);
            return;
        }
        super.setPerformancePreferences(n2, n3, n4);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("SSL socket over ");
        if (this.isDelegating()) {
            stringBuilder.append(this.socket.toString());
        } else {
            stringBuilder.append(super.toString());
        }
        return stringBuilder.toString();
    }

    String getHostname() {
        return this.peerHostname;
    }

    void setHostname(String string) {
        this.peerHostname = string;
    }

    String getHostnameOrIP() {
        if (this.peerHostname != null) {
            return this.peerHostname;
        }
        InetAddress inetAddress = this.getInetAddress();
        if (inetAddress != null) {
            return Platform.getOriginalHostNameFromInetAddress(inetAddress);
        }
        return null;
    }

    void setSoWriteTimeout(int n2) throws SocketException {
        throw new SocketException("Method setSoWriteTimeout() is not supported.");
    }

    int getSoWriteTimeout() throws SocketException {
        return 0;
    }

    void setHandshakeTimeout(int n2) throws SocketException {
        throw new SocketException("Method setHandshakeTimeout() is not supported.");
    }

    final void checkOpen() throws SocketException {
        if (this.isClosed()) {
            throw new SocketException("Socket is closed");
        }
    }

    final PeerInfoProvider peerInfoProvider() {
        return this.peerInfoProvider;
    }

    abstract SSLSession getActiveSession();

    abstract void setApplicationProtocolSelector(ApplicationProtocolSelectorAdapter var1);

    final void notifyHandshakeCompletedListeners() {
        ArrayList<HandshakeCompletedListener> arrayList = new ArrayList<HandshakeCompletedListener>(this.listeners);
        if (!arrayList.isEmpty()) {
            AbstractConscryptSocket abstractConscryptSocket = this;
            HandshakeCompletedEvent handshakeCompletedEvent = new HandshakeCompletedEvent(abstractConscryptSocket, abstractConscryptSocket.getActiveSession());
            for (HandshakeCompletedListener handshakeCompletedListener : arrayList) {
                try {
                    handshakeCompletedListener.handshakeCompleted(handshakeCompletedEvent);
                }
                catch (RuntimeException runtimeException) {
                    Thread thread2 = Thread.currentThread();
                    thread2.getUncaughtExceptionHandler().uncaughtException(thread2, runtimeException);
                }
            }
        }
    }

    private boolean isDelegating() {
        return this.socket != null && this.socket != this;
    }

    @Override
    public abstract SSLSession getHandshakeSession();

    abstract void setUseSessionTickets(boolean var1);

    abstract void setChannelIdEnabled(boolean var1);

    abstract byte[] getChannelId() throws SSLException;

    abstract void setChannelIdPrivateKey(PrivateKey var1);

    @Deprecated
    byte[] getNpnSelectedProtocol() {
        return null;
    }

    @Deprecated
    void setNpnProtocols(byte[] byArray) {
    }

    @Deprecated
    abstract byte[] getAlpnSelectedProtocol();

    @Deprecated
    abstract void setAlpnProtocols(String[] var1);

    @Deprecated
    abstract void setAlpnProtocols(byte[] var1);

    abstract void setApplicationProtocols(String[] var1);

    abstract String[] getApplicationProtocols();

    @Override
    public abstract String getApplicationProtocol();

    @Override
    public abstract String getHandshakeApplicationProtocol();

    abstract void setApplicationProtocolSelector(ApplicationProtocolSelector var1);

    abstract byte[] getTlsUnique();

    abstract byte[] exportKeyingMaterial(String var1, byte[] var2, int var3) throws SSLException;
}


/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.connection;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ProtocolException;
import java.net.UnknownServiceException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.List;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocket;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.ConnectionSpec;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0000\u0018\u00002\u00020\u0001B\u0013\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\u0002\u0010\u0005J\u000e\u0010\u000b\u001a\u00020\u00042\u0006\u0010\f\u001a\u00020\rJ\u000e\u0010\u000e\u001a\u00020\u00072\u0006\u0010\u000f\u001a\u00020\u0010J\u0010\u0010\b\u001a\u00020\u00072\u0006\u0010\u0011\u001a\u00020\rH\u0002R\u0014\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2={"Lokhttp3/internal/connection/ConnectionSpecSelector;", "", "connectionSpecs", "", "Lokhttp3/ConnectionSpec;", "(Ljava/util/List;)V", "isFallback", "", "isFallbackPossible", "nextModeIndex", "", "configureSecureSocket", "sslSocket", "Ljavax/net/ssl/SSLSocket;", "connectionFailed", "e", "Ljava/io/IOException;", "socket", "okhttp"})
public final class ConnectionSpecSelector {
    @NotNull
    private final List<ConnectionSpec> connectionSpecs;
    private int nextModeIndex;
    private boolean isFallbackPossible;
    private boolean isFallback;

    public ConnectionSpecSelector(@NotNull List<ConnectionSpec> list2) {
        Intrinsics.checkNotNullParameter(list2, "");
        this.connectionSpecs = list2;
    }

    @NotNull
    public final ConnectionSpec configureSecureSocket(@NotNull SSLSocket sSLSocket) throws IOException {
        Intrinsics.checkNotNullParameter(sSLSocket, "");
        ConnectionSpec connectionSpec = null;
        int n2 = this.connectionSpecs.size();
        for (int i2 = this.nextModeIndex; i2 < n2; ++i2) {
            ConnectionSpec connectionSpec2 = this.connectionSpecs.get(i2);
            if (!connectionSpec2.isCompatible(sSLSocket)) continue;
            connectionSpec = connectionSpec2;
            this.nextModeIndex = i2 + 1;
            break;
        }
        if (connectionSpec == null) {
            StringBuilder stringBuilder = new StringBuilder("Unable to find acceptable protocols. isFallback=").append(this.isFallback).append(", modes=").append(this.connectionSpecs).append(", supported protocols=");
            Object[] objectArray = sSLSocket.getEnabledProtocols();
            Intrinsics.checkNotNull(objectArray);
            String string = Arrays.toString(objectArray);
            Intrinsics.checkNotNullExpressionValue(string, "");
            throw new UnknownServiceException(stringBuilder.append(string).toString());
        }
        this.isFallbackPossible = this.isFallbackPossible(sSLSocket);
        connectionSpec.apply$okhttp(sSLSocket, this.isFallback);
        return connectionSpec;
    }

    public final boolean connectionFailed(@NotNull IOException iOException) {
        Intrinsics.checkNotNullParameter(iOException, "");
        this.isFallback = true;
        return this.isFallbackPossible && !(iOException instanceof ProtocolException) && !(iOException instanceof InterruptedIOException) && (!(iOException instanceof SSLHandshakeException) || !(iOException.getCause() instanceof CertificateException)) && !(iOException instanceof SSLPeerUnverifiedException) && iOException instanceof SSLException;
    }

    private final boolean isFallbackPossible(SSLSocket sSLSocket) {
        int n2 = this.connectionSpecs.size();
        for (int i2 = this.nextModeIndex; i2 < n2; ++i2) {
            if (!this.connectionSpecs.get(i2).isCompatible(sSLSocket)) continue;
            return true;
        }
        return false;
    }
}


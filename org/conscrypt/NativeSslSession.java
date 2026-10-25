/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.security.Principal;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSessionContext;
import javax.security.cert.X509Certificate;
import org.conscrypt.AbstractSessionContext;
import org.conscrypt.ClientSessionContext;
import org.conscrypt.ConscryptSession;
import org.conscrypt.NativeCrypto;
import org.conscrypt.NativeRef;
import org.conscrypt.NativeSsl;
import org.conscrypt.OpenSSLX509Certificate;
import org.conscrypt.SSLUtils;

abstract class NativeSslSession {
    private static final Logger logger = Logger.getLogger(NativeSslSession.class.getName());

    NativeSslSession() {
    }

    static NativeSslSession newInstance(NativeRef.SSL_SESSION sSL_SESSION, ConscryptSession conscryptSession) throws SSLPeerUnverifiedException {
        AbstractSessionContext abstractSessionContext = (AbstractSessionContext)conscryptSession.getSessionContext();
        if (abstractSessionContext instanceof ClientSessionContext) {
            return new Impl(abstractSessionContext, sSL_SESSION, conscryptSession.getPeerHost(), conscryptSession.getPeerPort(), conscryptSession.getPeerCertificates(), NativeSslSession.getOcspResponse(conscryptSession), conscryptSession.getPeerSignedCertificateTimestamp());
        }
        return new Impl(abstractSessionContext, sSL_SESSION, null, -1, null, null, null);
    }

    private static byte[] getOcspResponse(ConscryptSession object) {
        if ((object = object.getStatusResponses()).size() > 0) {
            return (byte[])object.get(0);
        }
        return null;
    }

    static NativeSslSession newInstance(AbstractSessionContext abstractSessionContext, byte[] object, String string, int n2) {
        object = ByteBuffer.wrap((byte[])object);
        try {
            int n3 = ((ByteBuffer)object).getInt();
            if (!SSLUtils.SessionType.isSupportedType(n3)) {
                throw new IOException("Unexpected type ID: " + n3);
            }
            int n4 = ((ByteBuffer)object).getInt();
            NativeSslSession.checkRemaining((ByteBuffer)object, n4);
            byte[] byArray = new byte[n4];
            ((ByteBuffer)object).get(byArray);
            int n5 = ((ByteBuffer)object).getInt();
            NativeSslSession.checkRemaining((ByteBuffer)object, n5);
            java.security.cert.X509Certificate[] x509CertificateArray = new java.security.cert.X509Certificate[n5];
            for (int i2 = 0; i2 < n5; ++i2) {
                n4 = ((ByteBuffer)object).getInt();
                NativeSslSession.checkRemaining((ByteBuffer)object, n4);
                byte[] byArray2 = new byte[n4];
                ((ByteBuffer)object).get(byArray2);
                try {
                    x509CertificateArray[i2] = OpenSSLX509Certificate.fromX509Der(byArray2);
                    continue;
                }
                catch (Exception exception) {
                    throw new IOException("Can not read certificate " + i2 + "/" + n5);
                }
            }
            byte[] byArray3 = null;
            if (n3 >= SSLUtils.SessionType.OPEN_SSL_WITH_OCSP.value) {
                n4 = ((ByteBuffer)object).getInt();
                NativeSslSession.checkRemaining((ByteBuffer)object, n4);
                if (n4 > 0) {
                    n5 = ((ByteBuffer)object).getInt();
                    NativeSslSession.checkRemaining((ByteBuffer)object, n5);
                    byArray3 = new byte[n5];
                    ((ByteBuffer)object).get(byArray3);
                    for (int i3 = 1; i3 < n4; ++i3) {
                        n5 = ((ByteBuffer)object).getInt();
                        NativeSslSession.checkRemaining((ByteBuffer)object, n5);
                        Object object2 = object;
                        ((ByteBuffer)object2).position(((Buffer)object2).position() + n5);
                    }
                }
            }
            byte[] byArray4 = null;
            if (n3 == SSLUtils.SessionType.OPEN_SSL_WITH_TLS_SCT.value) {
                n5 = ((ByteBuffer)object).getInt();
                NativeSslSession.checkRemaining((ByteBuffer)object, n5);
                if (n5 > 0) {
                    byArray4 = new byte[n5];
                    ((ByteBuffer)object).get(byArray4);
                }
            }
            if (((Buffer)object).remaining() != 0) {
                NativeSslSession.log((Throwable)((Object)new AssertionError((Object)"Read entire session, but data still remains; rejecting")));
                return null;
            }
            NativeRef.SSL_SESSION sSL_SESSION = new NativeRef.SSL_SESSION(NativeCrypto.d2i_SSL_SESSION(byArray));
            return new Impl(abstractSessionContext, sSL_SESSION, string, n2, x509CertificateArray, byArray3, byArray4);
        }
        catch (IOException iOException) {
            IOException iOException2 = iOException;
            NativeSslSession.log(iOException);
            return null;
        }
        catch (BufferUnderflowException bufferUnderflowException) {
            BufferUnderflowException bufferUnderflowException2 = bufferUnderflowException;
            NativeSslSession.log(bufferUnderflowException);
            return null;
        }
    }

    abstract byte[] getId();

    abstract boolean isValid();

    abstract boolean isSingleUse();

    abstract void offerToResume(NativeSsl var1) throws SSLException;

    abstract String getCipherSuite();

    abstract String getProtocol();

    abstract String getPeerHost();

    abstract int getPeerPort();

    abstract byte[] getPeerOcspStapledResponse();

    abstract byte[] getPeerSignedCertificateTimestamp();

    abstract byte[] toBytes();

    abstract SSLSession toSSLSession();

    private static void log(Throwable throwable) {
        logger.log(Level.INFO, "Error inflating SSL session: {0}", throwable.getMessage() != null ? throwable.getMessage() : throwable.getClass().getName());
    }

    private static void checkRemaining(ByteBuffer byteBuffer, int n2) throws IOException {
        if (n2 < 0) {
            throw new IOException("Length is negative: " + n2);
        }
        if (n2 > byteBuffer.remaining()) {
            throw new IOException("Length of blob is longer than available: " + n2 + " > " + byteBuffer.remaining());
        }
    }

    static final class Impl
    extends NativeSslSession {
        private final NativeRef.SSL_SESSION ref;
        private final AbstractSessionContext context;
        private final String host;
        private final int port;
        private final String protocol;
        private final String cipherSuite;
        private final java.security.cert.X509Certificate[] peerCertificates;
        private final byte[] peerOcspStapledResponse;
        private final byte[] peerSignedCertificateTimestamp;

        private Impl(AbstractSessionContext abstractSessionContext, NativeRef.SSL_SESSION sSL_SESSION, String string, int n2, java.security.cert.X509Certificate[] x509CertificateArray, byte[] byArray, byte[] byArray2) {
            this.context = abstractSessionContext;
            this.host = string;
            this.port = n2;
            this.peerCertificates = x509CertificateArray;
            this.peerOcspStapledResponse = byArray;
            this.peerSignedCertificateTimestamp = byArray2;
            this.protocol = NativeCrypto.SSL_SESSION_get_version(sSL_SESSION.address);
            this.cipherSuite = NativeCrypto.cipherSuiteToJava(NativeCrypto.SSL_SESSION_cipher(sSL_SESSION.address));
            this.ref = sSL_SESSION;
        }

        @Override
        final byte[] getId() {
            return NativeCrypto.SSL_SESSION_session_id(this.ref.address);
        }

        private long getCreationTime() {
            return NativeCrypto.SSL_SESSION_get_time(this.ref.address);
        }

        @Override
        final boolean isValid() {
            long l2 = this.getCreationTime();
            long l3 = Math.max(0L, Math.min((long)this.context.getSessionTimeout(), NativeCrypto.SSL_SESSION_get_timeout(this.ref.address))) * 1000L;
            return System.currentTimeMillis() - l3 < l2;
        }

        @Override
        final boolean isSingleUse() {
            return NativeCrypto.SSL_SESSION_should_be_single_use(this.ref.address);
        }

        @Override
        final void offerToResume(NativeSsl nativeSsl) throws SSLException {
            nativeSsl.offerToResumeSession(this.ref.address);
        }

        @Override
        final String getCipherSuite() {
            return this.cipherSuite;
        }

        @Override
        final String getProtocol() {
            return this.protocol;
        }

        @Override
        final String getPeerHost() {
            return this.host;
        }

        @Override
        final int getPeerPort() {
            return this.port;
        }

        @Override
        final byte[] getPeerOcspStapledResponse() {
            return this.peerOcspStapledResponse;
        }

        @Override
        final byte[] getPeerSignedCertificateTimestamp() {
            return this.peerSignedCertificateTimestamp;
        }

        @Override
        final byte[] toBytes() {
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
                dataOutputStream.writeInt(SSLUtils.SessionType.OPEN_SSL_WITH_TLS_SCT.value);
                Object object = NativeCrypto.i2d_SSL_SESSION(this.ref.address);
                dataOutputStream.writeInt(((byte[])object).length);
                dataOutputStream.write((byte[])object);
                dataOutputStream.writeInt(this.peerCertificates.length);
                java.security.cert.X509Certificate[] x509CertificateArray = this.peerCertificates;
                int n2 = this.peerCertificates.length;
                for (int i2 = 0; i2 < n2; ++i2) {
                    java.security.cert.X509Certificate x509Certificate = x509CertificateArray[i2];
                    object = x509Certificate;
                    object = x509Certificate.getEncoded();
                    dataOutputStream.writeInt(((byte[])object).length);
                    dataOutputStream.write((byte[])object);
                }
                if (this.peerOcspStapledResponse != null) {
                    dataOutputStream.writeInt(1);
                    dataOutputStream.writeInt(this.peerOcspStapledResponse.length);
                    dataOutputStream.write(this.peerOcspStapledResponse);
                } else {
                    dataOutputStream.writeInt(0);
                }
                if (this.peerSignedCertificateTimestamp != null) {
                    dataOutputStream.writeInt(this.peerSignedCertificateTimestamp.length);
                    dataOutputStream.write(this.peerSignedCertificateTimestamp);
                } else {
                    dataOutputStream.writeInt(0);
                }
                return byteArrayOutputStream.toByteArray();
            }
            catch (IOException iOException) {
                logger.log(Level.WARNING, "Failed to convert saved SSL Session: ", iOException);
                return null;
            }
            catch (CertificateEncodingException certificateEncodingException) {
                CertificateEncodingException certificateEncodingException2 = certificateEncodingException;
                NativeSslSession.log(certificateEncodingException);
                return null;
            }
        }

        @Override
        final SSLSession toSSLSession() {
            return new SSLSession(){

                @Override
                public byte[] getId() {
                    return Impl.this.getId();
                }

                @Override
                public String getCipherSuite() {
                    return Impl.this.getCipherSuite();
                }

                @Override
                public String getProtocol() {
                    return Impl.this.getProtocol();
                }

                @Override
                public String getPeerHost() {
                    return Impl.this.getPeerHost();
                }

                @Override
                public int getPeerPort() {
                    return Impl.this.getPeerPort();
                }

                @Override
                public long getCreationTime() {
                    return Impl.this.getCreationTime();
                }

                @Override
                public boolean isValid() {
                    return Impl.this.isValid();
                }

                @Override
                public SSLSessionContext getSessionContext() {
                    throw new UnsupportedOperationException();
                }

                @Override
                public long getLastAccessedTime() {
                    throw new UnsupportedOperationException();
                }

                @Override
                public void invalidate() {
                    throw new UnsupportedOperationException();
                }

                @Override
                public void putValue(String string, Object object) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public Object getValue(String string) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public void removeValue(String string) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public String[] getValueNames() {
                    throw new UnsupportedOperationException();
                }

                @Override
                public Certificate[] getPeerCertificates() throws SSLPeerUnverifiedException {
                    throw new UnsupportedOperationException();
                }

                @Override
                public Certificate[] getLocalCertificates() {
                    throw new UnsupportedOperationException();
                }

                @Override
                public X509Certificate[] getPeerCertificateChain() throws SSLPeerUnverifiedException {
                    throw new UnsupportedOperationException();
                }

                @Override
                public Principal getPeerPrincipal() throws SSLPeerUnverifiedException {
                    throw new UnsupportedOperationException();
                }

                @Override
                public Principal getLocalPrincipal() {
                    throw new UnsupportedOperationException();
                }

                @Override
                public int getPacketBufferSize() {
                    throw new UnsupportedOperationException();
                }

                @Override
                public int getApplicationBufferSize() {
                    throw new UnsupportedOperationException();
                }
            };
        }
    }
}


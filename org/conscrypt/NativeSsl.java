/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.X509KeyManager;
import javax.security.auth.x500.X500Principal;
import org.conscrypt.AbstractSessionContext;
import org.conscrypt.AddressUtils;
import org.conscrypt.EmptyArray;
import org.conscrypt.NativeCrypto;
import org.conscrypt.OpenSSLKey;
import org.conscrypt.PSKKeyManager;
import org.conscrypt.Platform;
import org.conscrypt.SSLParametersImpl;
import org.conscrypt.SSLUtils;

final class NativeSsl {
    private final SSLParametersImpl parameters;
    private final NativeCrypto.SSLHandshakeCallbacks handshakeCallbacks;
    private final SSLParametersImpl.AliasChooser aliasChooser;
    private final SSLParametersImpl.PSKCallbacks pskCallbacks;
    private X509Certificate[] localCertificates;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private volatile long ssl;

    private NativeSsl(long l2, SSLParametersImpl sSLParametersImpl, NativeCrypto.SSLHandshakeCallbacks sSLHandshakeCallbacks, SSLParametersImpl.AliasChooser aliasChooser, SSLParametersImpl.PSKCallbacks pSKCallbacks) {
        this.ssl = l2;
        this.parameters = sSLParametersImpl;
        this.handshakeCallbacks = sSLHandshakeCallbacks;
        this.aliasChooser = aliasChooser;
        this.pskCallbacks = pSKCallbacks;
    }

    static NativeSsl newInstance(SSLParametersImpl sSLParametersImpl, NativeCrypto.SSLHandshakeCallbacks sSLHandshakeCallbacks, SSLParametersImpl.AliasChooser aliasChooser, SSLParametersImpl.PSKCallbacks pSKCallbacks) throws SSLException {
        AbstractSessionContext abstractSessionContext = sSLParametersImpl.getSessionContext();
        long l2 = NativeCrypto.SSL_new(abstractSessionContext.sslCtxNativePointer, abstractSessionContext);
        return new NativeSsl(l2, sSLParametersImpl, sSLHandshakeCallbacks, aliasChooser, pSKCallbacks);
    }

    final BioWrapper newBio() {
        try {
            return new BioWrapper();
        }
        catch (SSLException sSLException) {
            throw new RuntimeException(sSLException);
        }
    }

    final void offerToResumeSession(long l2) throws SSLException {
        NativeCrypto.SSL_set_session(this.ssl, this, l2);
    }

    final byte[] getSessionId() {
        return NativeCrypto.SSL_session_id(this.ssl, this);
    }

    final long getTime() {
        return NativeCrypto.SSL_get_time(this.ssl, this);
    }

    final long getTimeout() {
        return NativeCrypto.SSL_get_timeout(this.ssl, this);
    }

    final void setTimeout(long l2) {
        NativeCrypto.SSL_set_timeout(this.ssl, this, l2);
    }

    final String getCipherSuite() {
        return NativeCrypto.cipherSuiteToJava(NativeCrypto.SSL_get_current_cipher(this.ssl, this));
    }

    final X509Certificate[] getPeerCertificates() throws CertificateException {
        byte[][] byArray = NativeCrypto.SSL_get0_peer_certificates(this.ssl, this);
        if (byArray == null) {
            return null;
        }
        return SSLUtils.decodeX509CertificateChain(byArray);
    }

    final X509Certificate[] getLocalCertificates() {
        return this.localCertificates;
    }

    final byte[] getPeerCertificateOcspData() {
        return NativeCrypto.SSL_get_ocsp_response(this.ssl, this);
    }

    final byte[] getTlsUnique() {
        return NativeCrypto.SSL_get_tls_unique(this.ssl, this);
    }

    final byte[] exportKeyingMaterial(String object, byte[] byArray, int n2) throws SSLException {
        if (object == null) {
            throw new NullPointerException("Label is null");
        }
        object = ((String)object).getBytes(Charset.forName("US-ASCII"));
        return NativeCrypto.SSL_export_keying_material(this.ssl, this, (byte[])object, byArray, n2);
    }

    final byte[] getPeerTlsSctData() {
        return NativeCrypto.SSL_get_signed_cert_timestamp_list(this.ssl, this);
    }

    final int clientPSKKeyRequested(String object, byte[] byArray, byte[] byArray2) {
        byte[] byArray3;
        PSKKeyManager pSKKeyManager = this.parameters.getPSKKeyManager();
        if (pSKKeyManager == null) {
            return 0;
        }
        String string = this.pskCallbacks.chooseClientPSKIdentity(pSKKeyManager, (String)object);
        if (string == null) {
            string = "";
            byArray3 = EmptyArray.BYTE;
        } else if (string.isEmpty()) {
            byArray3 = EmptyArray.BYTE;
        } else {
            try {
                byArray3 = string.getBytes("UTF-8");
            }
            catch (UnsupportedEncodingException unsupportedEncodingException) {
                throw new RuntimeException("UTF-8 encoding not supported", unsupportedEncodingException);
            }
        }
        if (byArray3.length + 1 > byArray.length) {
            return 0;
        }
        if (byArray3.length > 0) {
            System.arraycopy(byArray3, 0, byArray, 0, byArray3.length);
        }
        byArray[byArray3.length] = 0;
        object = this.pskCallbacks.getPSKKey(pSKKeyManager, (String)object, string);
        byte[] byArray4 = object.getEncoded();
        object = byArray4;
        if (byArray4 == null) {
            return 0;
        }
        if (((Object)object).length > byArray2.length) {
            return 0;
        }
        System.arraycopy(object, 0, byArray2, 0, ((Object)object).length);
        return ((Object)object).length;
    }

    final int serverPSKKeyRequested(String object, String string, byte[] byArray) {
        PSKKeyManager pSKKeyManager = this.parameters.getPSKKeyManager();
        if (pSKKeyManager == null) {
            return 0;
        }
        object = this.pskCallbacks.getPSKKey(pSKKeyManager, (String)object, string);
        byte[] byArray2 = object.getEncoded();
        object = byArray2;
        if (byArray2 == null) {
            return 0;
        }
        if (((Object)object).length > byArray.length) {
            return 0;
        }
        System.arraycopy(object, 0, byArray, 0, ((Object)object).length);
        return ((Object)object).length;
    }

    final void chooseClientCertificate(byte[] object, int[] objectArray, byte[][] byArray) throws SSLException, CertificateEncodingException {
        Set<String> set = SSLUtils.getSupportedClientKeyTypes((byte[])object, objectArray);
        object = set;
        object = set.toArray(new String[object.size()]);
        if (byArray == null) {
            objectArray = null;
        } else {
            objectArray = new X500Principal[byArray.length];
            for (int i2 = 0; i2 < byArray.length; ++i2) {
                objectArray[i2] = (int)new X500Principal(byArray[i2]);
            }
        }
        X509KeyManager x509KeyManager = this.parameters.getX509KeyManager();
        object = x509KeyManager != null ? this.aliasChooser.chooseClientAlias(x509KeyManager, (X500Principal[])objectArray, (String[])object) : null;
        this.setCertificate((String)object);
    }

    private void setCertificate(String string) throws CertificateEncodingException, SSLException {
        OpenSSLKey openSSLKey;
        if (string == null) {
            return;
        }
        X509KeyManager x509KeyManager = this.parameters.getX509KeyManager();
        if (x509KeyManager == null) {
            return;
        }
        PrivateKey privateKey = x509KeyManager.getPrivateKey(string);
        if (privateKey == null) {
            return;
        }
        this.localCertificates = x509KeyManager.getCertificateChain(string);
        if (this.localCertificates == null) {
            return;
        }
        int n2 = this.localCertificates.length;
        x509KeyManager = n2 > 0 ? this.localCertificates[0].getPublicKey() : null;
        byte[][] byArrayArray = new byte[n2][];
        for (int i2 = 0; i2 < n2; ++i2) {
            byArrayArray[i2] = this.localCertificates[i2].getEncoded();
        }
        try {
            openSSLKey = OpenSSLKey.fromPrivateKeyForTLSStackOnly(privateKey, (PublicKey)((Object)x509KeyManager));
        }
        catch (InvalidKeyException invalidKeyException) {
            throw new SSLException(invalidKeyException);
        }
        NativeCrypto.setLocalCertsAndPrivateKey(this.ssl, this, byArrayArray, openSSLKey.getNativeRef());
    }

    final String getVersion() {
        return NativeCrypto.SSL_get_version(this.ssl, this);
    }

    final String getRequestedServerName() {
        return NativeCrypto.SSL_get_servername(this.ssl, this);
    }

    final byte[] getTlsChannelId() throws SSLException {
        return NativeCrypto.SSL_get_tls_channel_id(this.ssl, this);
    }

    final void initialize(String string, OpenSSLKey openSSLKey) throws IOException {
        boolean bl = this.parameters.getEnableSessionCreation();
        if (!bl) {
            NativeCrypto.SSL_set_session_creation_enabled(this.ssl, this, false);
        }
        NativeCrypto.SSL_accept_renegotiations(this.ssl, this);
        if (this.isClient()) {
            NativeCrypto.SSL_set_connect_state(this.ssl, this);
            NativeCrypto.SSL_enable_ocsp_stapling(this.ssl, this);
            if (this.parameters.isCTVerificationEnabled(string)) {
                NativeCrypto.SSL_enable_signed_cert_timestamps(this.ssl, this);
            }
        } else {
            NativeCrypto.SSL_set_accept_state(this.ssl, this);
            if (this.parameters.getOCSPResponse() != null) {
                NativeCrypto.SSL_enable_ocsp_stapling(this.ssl, this);
            }
        }
        if (this.parameters.getEnabledProtocols().length == 0 && this.parameters.isEnabledProtocolsFiltered) {
            throw new SSLHandshakeException("No enabled protocols; SSLv3 is no longer supported and was filtered from the list");
        }
        NativeSsl nativeSsl = this;
        NativeCrypto.setEnabledProtocols(this.ssl, nativeSsl, nativeSsl.parameters.enabledProtocols);
        NativeSsl nativeSsl2 = this;
        NativeCrypto.setEnabledCipherSuites(this.ssl, nativeSsl2, nativeSsl2.parameters.enabledCipherSuites, this.parameters.enabledProtocols);
        if (this.parameters.applicationProtocols.length > 0) {
            NativeSsl nativeSsl3 = this;
            NativeCrypto.setApplicationProtocols(this.ssl, nativeSsl3, nativeSsl3.isClient(), this.parameters.applicationProtocols);
        }
        if (!this.isClient() && this.parameters.applicationProtocolSelector != null) {
            NativeCrypto.setHasApplicationProtocolSelector(this.ssl, this, true);
        }
        if (!this.isClient()) {
            NativeCrypto.SSL_set_options(this.ssl, this, 0x400000L);
            if (this.parameters.sctExtension != null) {
                NativeSsl nativeSsl4 = this;
                NativeCrypto.SSL_set_signed_cert_timestamp_list(this.ssl, nativeSsl4, nativeSsl4.parameters.sctExtension);
            }
            if (this.parameters.ocspResponse != null) {
                NativeSsl nativeSsl5 = this;
                NativeCrypto.SSL_set_ocsp_response(this.ssl, nativeSsl5, nativeSsl5.parameters.ocspResponse);
            }
        }
        this.enablePSKKeyManagerIfRequested();
        if (this.parameters.useSessionTickets) {
            NativeCrypto.SSL_clear_options(this.ssl, this, 16384L);
        } else {
            NativeSsl nativeSsl6 = this;
            NativeCrypto.SSL_set_options(this.ssl, nativeSsl6, NativeCrypto.SSL_get_options(nativeSsl6.ssl, this) | 0x4000L);
        }
        if (this.parameters.getUseSni() && AddressUtils.isValidSniHostname(string)) {
            NativeCrypto.SSL_set_tlsext_host_name(this.ssl, this, string);
        }
        NativeCrypto.SSL_set_mode(this.ssl, this, 256L);
        this.setCertificateValidation();
        this.setTlsChannelId(openSSLKey);
    }

    final void configureServerCertificate() throws IOException {
        NativeSsl nativeSsl = this;
        nativeSsl.verifyWithSniMatchers(nativeSsl.getRequestedServerName());
        if (this.isClient()) {
            return;
        }
        X509KeyManager x509KeyManager = this.parameters.getX509KeyManager();
        if (x509KeyManager != null) {
            for (String string : this.getCipherKeyTypes()) {
                try {
                    NativeSsl nativeSsl2 = this;
                    nativeSsl2.setCertificate(nativeSsl2.aliasChooser.chooseServerAlias(x509KeyManager, string));
                }
                catch (CertificateEncodingException certificateEncodingException) {
                    throw new IOException(certificateEncodingException);
                }
            }
        }
    }

    private void verifyWithSniMatchers(String string) throws SSLHandshakeException {
        if (!AddressUtils.isValidSniHostname(string)) {
            return;
        }
        if (!Platform.serverNamePermitted(this.parameters, string)) {
            throw new SSLHandshakeException("SNI match failed: " + string);
        }
    }

    private Set<String> getCipherKeyTypes() {
        HashSet<String> hashSet = new HashSet<String>();
        for (long l2 : NativeCrypto.SSL_get_ciphers(this.ssl, this)) {
            String string = SSLUtils.getServerX509KeyType(l2);
            if (string == null) continue;
            hashSet.add(string);
        }
        return hashSet;
    }

    final void doHandshake(FileDescriptor fileDescriptor, int n2) throws CertificateException, IOException {
        this.lock.readLock().lock();
        try {
            if (this.isClosed() || fileDescriptor == null || !fileDescriptor.valid()) {
                throw new SocketException("Socket is closed");
            }
            NativeCrypto.SSL_do_handshake(this.ssl, this, fileDescriptor, this.handshakeCallbacks, n2);
            return;
        }
        finally {
            this.lock.readLock().unlock();
        }
    }

    final int doHandshake() throws IOException {
        this.lock.readLock().lock();
        try {
            NativeSsl nativeSsl = this;
            int n2 = NativeCrypto.ENGINE_SSL_do_handshake(this.ssl, nativeSsl, nativeSsl.handshakeCallbacks);
            return n2;
        }
        finally {
            this.lock.readLock().unlock();
        }
    }

    final int read(FileDescriptor fileDescriptor, byte[] byArray, int n2, int n3, int n4) throws IOException {
        this.lock.readLock().lock();
        try {
            if (this.isClosed() || fileDescriptor == null || !fileDescriptor.valid()) {
                throw new SocketException("Socket is closed");
            }
            int n5 = NativeCrypto.SSL_read(this.ssl, this, fileDescriptor, this.handshakeCallbacks, byArray, n2, n3, n4);
            return n5;
        }
        finally {
            this.lock.readLock().unlock();
        }
    }

    final void write(FileDescriptor fileDescriptor, byte[] byArray, int n2, int n3, int n4) throws IOException {
        this.lock.readLock().lock();
        try {
            if (this.isClosed() || fileDescriptor == null || !fileDescriptor.valid()) {
                throw new SocketException("Socket is closed");
            }
            NativeCrypto.SSL_write(this.ssl, this, fileDescriptor, this.handshakeCallbacks, byArray, n2, n3, n4);
            return;
        }
        finally {
            this.lock.readLock().unlock();
        }
    }

    private void enablePSKKeyManagerIfRequested() throws SSLException {
        PSKKeyManager pSKKeyManager = this.parameters.getPSKKeyManager();
        if (pSKKeyManager != null) {
            boolean bl = false;
            Object object = this.parameters.enabledCipherSuites;
            int n2 = this.parameters.enabledCipherSuites.length;
            for (int i2 = 0; i2 < n2; ++i2) {
                String string = object[i2];
                if (string == null || !string.contains("PSK")) continue;
                bl = true;
                break;
            }
            if (bl) {
                if (this.isClient()) {
                    NativeCrypto.set_SSL_psk_client_callback_enabled(this.ssl, this, true);
                    return;
                }
                NativeCrypto.set_SSL_psk_server_callback_enabled(this.ssl, this, true);
                object = this.pskCallbacks.chooseServerPSKIdentityHint(pSKKeyManager);
                NativeCrypto.SSL_use_psk_identity_hint(this.ssl, this, (String)object);
            }
        }
    }

    private void setTlsChannelId(OpenSSLKey openSSLKey) throws SSLException {
        if (!this.parameters.channelIdEnabled) {
            return;
        }
        if (this.parameters.getUseClientMode()) {
            if (openSSLKey == null) {
                throw new SSLHandshakeException("Invalid TLS channel ID key specified");
            }
            NativeCrypto.SSL_set1_tls_channel_id(this.ssl, this, openSSLKey.getNativeRef());
            return;
        }
        NativeCrypto.SSL_enable_tls_channel_id(this.ssl, this);
    }

    private void setCertificateValidation() throws SSLException {
        if (!this.isClient()) {
            boolean bl;
            if (this.parameters.getNeedClientAuth()) {
                NativeCrypto.SSL_set_verify(this.ssl, this, 3);
                bl = true;
            } else if (this.parameters.getWantClientAuth()) {
                NativeCrypto.SSL_set_verify(this.ssl, this, 1);
                bl = true;
            } else {
                NativeCrypto.SSL_set_verify(this.ssl, this, 0);
                bl = false;
            }
            if (bl) {
                Object object = this.parameters.getX509TrustManager();
                if ((object = object.getAcceptedIssuers()) != null && ((X509Certificate[])object).length != 0) {
                    try {
                        object = SSLUtils.encodeSubjectX509Principals(object);
                    }
                    catch (CertificateEncodingException certificateEncodingException) {
                        throw new SSLException("Problem encoding principals", certificateEncodingException);
                    }
                    NativeCrypto.SSL_set_client_CA_list(this.ssl, this, (byte[][])object);
                }
            }
        }
    }

    final void interrupt() {
        NativeCrypto.SSL_interrupt(this.ssl, this);
    }

    final void shutdown(FileDescriptor fileDescriptor) throws IOException {
        NativeCrypto.SSL_shutdown(this.ssl, this, fileDescriptor, this.handshakeCallbacks);
    }

    final void shutdown() throws IOException {
        this.lock.readLock().lock();
        try {
            NativeSsl nativeSsl = this;
            NativeCrypto.ENGINE_SSL_shutdown(this.ssl, nativeSsl, nativeSsl.handshakeCallbacks);
            return;
        }
        finally {
            this.lock.readLock().unlock();
        }
    }

    final boolean wasShutdownReceived() {
        this.lock.readLock().lock();
        try {
            boolean bl = (NativeCrypto.SSL_get_shutdown(this.ssl, this) & 2) != 0;
            return bl;
        }
        finally {
            this.lock.readLock().unlock();
        }
    }

    final boolean wasShutdownSent() {
        this.lock.readLock().lock();
        try {
            boolean bl = (NativeCrypto.SSL_get_shutdown(this.ssl, this) & 1) != 0;
            return bl;
        }
        finally {
            this.lock.readLock().unlock();
        }
    }

    final int readDirectByteBuffer(long l2, int n2) throws IOException, CertificateException {
        this.lock.readLock().lock();
        try {
            int n3 = NativeCrypto.ENGINE_SSL_read_direct(this.ssl, this, l2, n2, this.handshakeCallbacks);
            return n3;
        }
        finally {
            this.lock.readLock().unlock();
        }
    }

    final int writeDirectByteBuffer(long l2, int n2) throws IOException {
        this.lock.readLock().lock();
        try {
            int n3 = NativeCrypto.ENGINE_SSL_write_direct(this.ssl, this, l2, n2, this.handshakeCallbacks);
            return n3;
        }
        finally {
            this.lock.readLock().unlock();
        }
    }

    final void forceRead() throws IOException {
        this.lock.readLock().lock();
        try {
            NativeSsl nativeSsl = this;
            NativeCrypto.ENGINE_SSL_force_read(this.ssl, nativeSsl, nativeSsl.handshakeCallbacks);
            return;
        }
        finally {
            this.lock.readLock().unlock();
        }
    }

    final int getPendingReadableBytes() {
        this.lock.readLock().lock();
        try {
            if (!this.isClosed()) {
                int n2 = NativeCrypto.SSL_pending_readable_bytes(this.ssl, this);
                return n2;
            }
            return 0;
        }
        finally {
            this.lock.readLock().unlock();
        }
    }

    final int getMaxSealOverhead() {
        return NativeCrypto.SSL_max_seal_overhead(this.ssl, this);
    }

    final void close() {
        this.lock.writeLock().lock();
        try {
            if (!this.isClosed()) {
                long l2 = this.ssl;
                this.ssl = 0L;
                NativeCrypto.SSL_free(l2, this);
            }
            return;
        }
        finally {
            this.lock.writeLock().unlock();
        }
    }

    final boolean isClosed() {
        return this.ssl == 0L;
    }

    final int getError(int n2) {
        return NativeCrypto.SSL_get_error(this.ssl, this, n2);
    }

    final byte[] getApplicationProtocol() {
        return NativeCrypto.getApplicationProtocol(this.ssl, this);
    }

    private boolean isClient() {
        return this.parameters.getUseClientMode();
    }

    protected final void finalize() throws Throwable {
        try {
            this.close();
            return;
        }
        finally {
            super.finalize();
        }
    }

    final class BioWrapper {
        private volatile long bio;

        private BioWrapper() throws SSLException {
            this.bio = NativeCrypto.SSL_BIO_new(NativeSsl.this.ssl, NativeSsl.this);
        }

        final int getPendingWrittenBytes() {
            NativeSsl.this.lock.readLock().lock();
            try {
                int n2 = this.bio == 0L ? 0 : NativeCrypto.SSL_pending_written_bytes_in_BIO(this.bio);
                return n2;
            }
            finally {
                NativeSsl.this.lock.readLock().unlock();
            }
        }

        final int writeDirectByteBuffer(long l2, int n2) throws IOException {
            NativeSsl.this.lock.readLock().lock();
            try {
                if (NativeSsl.this.isClosed()) {
                    throw new SSLException("Connection closed");
                }
                int n3 = NativeCrypto.ENGINE_SSL_write_BIO_direct(NativeSsl.this.ssl, NativeSsl.this, this.bio, l2, n2, NativeSsl.this.handshakeCallbacks);
                return n3;
            }
            finally {
                NativeSsl.this.lock.readLock().unlock();
            }
        }

        final int readDirectByteBuffer(long l2, int n2) throws IOException {
            NativeSsl.this.lock.readLock().lock();
            try {
                if (NativeSsl.this.isClosed()) {
                    throw new SSLException("Connection closed");
                }
                int n3 = NativeCrypto.ENGINE_SSL_read_BIO_direct(NativeSsl.this.ssl, NativeSsl.this, this.bio, l2, n2, NativeSsl.this.handshakeCallbacks);
                return n3;
            }
            finally {
                NativeSsl.this.lock.readLock().unlock();
            }
        }

        final void close() {
            NativeSsl.this.lock.writeLock().lock();
            try {
                long l2 = this.bio;
                this.bio = 0L;
                if (l2 != 0L) {
                    NativeCrypto.BIO_free_all(l2);
                }
                return;
            }
            finally {
                NativeSsl.this.lock.writeLock().unlock();
            }
        }
    }
}


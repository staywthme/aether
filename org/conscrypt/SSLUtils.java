/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;
import org.conscrypt.EmptyArray;
import org.conscrypt.NativeCrypto;
import org.conscrypt.OpenSSLX509Certificate;

final class SSLUtils {
    static final boolean USE_ENGINE_SOCKET_BY_DEFAULT = Boolean.parseBoolean(System.getProperty("org.conscrypt.useEngineSocketByDefault", "true"));
    private static final int MAX_PROTOCOL_LENGTH = 255;
    private static final Charset US_ASCII = Charset.forName("US-ASCII");
    private static final int MAX_ENCRYPTION_OVERHEAD_LENGTH = 86;
    private static final int MAX_ENCRYPTION_OVERHEAD_DIFF = 2147483561;
    private static final String KEY_TYPE_RSA = "RSA";
    private static final String KEY_TYPE_EC = "EC";

    static java.security.cert.X509Certificate[] decodeX509CertificateChain(byte[][] byArray) throws java.security.cert.CertificateException {
        CertificateFactory certificateFactory = SSLUtils.getCertificateFactory();
        int n2 = byArray.length;
        java.security.cert.X509Certificate[] x509CertificateArray = new java.security.cert.X509Certificate[n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            x509CertificateArray[i2] = SSLUtils.decodeX509Certificate(certificateFactory, byArray[i2]);
        }
        return x509CertificateArray;
    }

    private static CertificateFactory getCertificateFactory() {
        try {
            return CertificateFactory.getInstance("X.509");
        }
        catch (java.security.cert.CertificateException certificateException) {
            return null;
        }
    }

    private static java.security.cert.X509Certificate decodeX509Certificate(CertificateFactory certificateFactory, byte[] byArray) throws java.security.cert.CertificateException {
        if (certificateFactory != null) {
            return (java.security.cert.X509Certificate)certificateFactory.generateCertificate(new ByteArrayInputStream(byArray));
        }
        return OpenSSLX509Certificate.fromX509Der(byArray);
    }

    static String getServerX509KeyType(long l2) {
        String string = NativeCrypto.SSL_CIPHER_get_kx_name(l2);
        if (string.equals(KEY_TYPE_RSA) || string.equals("DHE_RSA") || string.equals("ECDHE_RSA")) {
            return KEY_TYPE_RSA;
        }
        if (string.equals("ECDHE_ECDSA")) {
            return KEY_TYPE_EC;
        }
        return null;
    }

    static String getClientKeyType(byte by) {
        switch (by) {
            case 1: {
                return KEY_TYPE_RSA;
            }
            case 64: {
                return KEY_TYPE_EC;
            }
        }
        return null;
    }

    static String getClientKeyTypeFromSignatureAlg(int n2) {
        switch (NativeCrypto.SSL_get_signature_algorithm_key_type(n2)) {
            case 6: {
                return KEY_TYPE_RSA;
            }
            case 408: {
                return KEY_TYPE_EC;
            }
        }
        return null;
    }

    static Set<String> getSupportedClientKeyTypes(byte[] byArray, int[] nArray) {
        String string;
        int n2;
        int n3;
        HashSet<String> hashSet = new HashSet<String>(byArray.length);
        Object object = byArray;
        int n4 = byArray.length;
        for (n3 = 0; n3 < n4; ++n3) {
            n2 = object[n3];
            string = SSLUtils.getClientKeyType((byte)n2);
            if (string == null) continue;
            hashSet.add(string);
        }
        object = new LinkedHashSet(nArray.length);
        int[] nArray2 = nArray;
        n3 = nArray.length;
        for (n2 = 0; n2 < n3; ++n2) {
            int n5 = nArray2[n2];
            string = SSLUtils.getClientKeyTypeFromSignatureAlg(n5);
            if (string == null) continue;
            object.add(string);
        }
        if (byArray.length > 0 && nArray.length > 0) {
            object.retainAll(hashSet);
            return object;
        }
        if (nArray.length > 0) {
            return object;
        }
        return hashSet;
    }

    static byte[][] encodeSubjectX509Principals(java.security.cert.X509Certificate[] x509CertificateArray) throws CertificateEncodingException {
        byte[][] byArrayArray = new byte[x509CertificateArray.length][];
        for (int i2 = 0; i2 < x509CertificateArray.length; ++i2) {
            byArrayArray[i2] = x509CertificateArray[i2].getSubjectX500Principal().getEncoded();
        }
        return byArrayArray;
    }

    static X509Certificate[] toCertificateChain(java.security.cert.X509Certificate[] x509CertificateArray) throws SSLPeerUnverifiedException {
        try {
            X509Certificate[] x509CertificateArray2 = new X509Certificate[x509CertificateArray.length];
            for (int i2 = 0; i2 < x509CertificateArray.length; ++i2) {
                byte[] byArray = x509CertificateArray[i2].getEncoded();
                x509CertificateArray2[i2] = X509Certificate.getInstance(byArray);
            }
            return x509CertificateArray2;
        }
        catch (CertificateEncodingException certificateEncodingException) {
            SSLPeerUnverifiedException sSLPeerUnverifiedException = new SSLPeerUnverifiedException(certificateEncodingException.getMessage());
            sSLPeerUnverifiedException.initCause(sSLPeerUnverifiedException);
            throw sSLPeerUnverifiedException;
        }
        catch (CertificateException certificateException) {
            SSLPeerUnverifiedException sSLPeerUnverifiedException = new SSLPeerUnverifiedException(certificateException.getMessage());
            sSLPeerUnverifiedException.initCause(sSLPeerUnverifiedException);
            throw sSLPeerUnverifiedException;
        }
    }

    static int calculateOutNetBufSize(int n2) {
        return Math.min(16709, 86 + Math.min(2147483561, n2));
    }

    static SSLHandshakeException toSSLHandshakeException(Throwable throwable) {
        if (throwable instanceof SSLHandshakeException) {
            return (SSLHandshakeException)throwable;
        }
        return (SSLHandshakeException)new SSLHandshakeException(throwable.getMessage()).initCause(throwable);
    }

    static SSLException toSSLException(Throwable throwable) {
        if (throwable instanceof SSLException) {
            return (SSLException)throwable;
        }
        return new SSLException(throwable);
    }

    static String toProtocolString(byte[] byArray) {
        if (byArray == null) {
            return null;
        }
        return new String(byArray, US_ASCII);
    }

    static byte[] toProtocolBytes(String string) {
        if (string == null) {
            return null;
        }
        return string.getBytes(US_ASCII);
    }

    static String[] decodeProtocols(byte[] byArray) {
        byte by;
        int n2;
        if (byArray.length == 0) {
            return EmptyArray.STRING;
        }
        int n3 = 0;
        for (int i2 = 0; i2 < byArray.length; i2 += n2 + 1) {
            n2 = byArray[i2];
            if (n2 < 0 || n2 > byArray.length - i2) {
                throw new IllegalArgumentException("Protocol has invalid length (" + n2 + " at position " + i2 + "): " + (byArray.length < 50 ? Arrays.toString(byArray) : byArray.length + " byte array"));
            }
            ++n3;
        }
        String[] stringArray = new String[n3];
        n3 = 0;
        for (n2 = 0; n2 < byArray.length; n2 += by + 1) {
            by = byArray[n2];
            stringArray[n3++] = by > 0 ? new String(byArray, n2 + 1, (int)by, US_ASCII) : "";
        }
        return stringArray;
    }

    static byte[] encodeProtocols(String[] stringArray) {
        int n2;
        if (stringArray == null) {
            throw new IllegalArgumentException("protocols array must be non-null");
        }
        if (stringArray.length == 0) {
            return EmptyArray.BYTE;
        }
        int n3 = 0;
        for (int i2 = 0; i2 < stringArray.length; ++i2) {
            String string = stringArray[i2];
            if (string == null) {
                throw new IllegalArgumentException("protocol[" + i2 + "] is null");
            }
            n2 = stringArray[i2].length();
            if (n2 == 0 || n2 > 255) {
                throw new IllegalArgumentException("protocol[" + i2 + "] has invalid length: " + n2);
            }
            n3 += n2 + 1;
        }
        byte[] byArray = new byte[n3];
        int n4 = 0;
        for (n2 = 0; n2 < stringArray.length; ++n2) {
            String string = stringArray[n2];
            int n5 = string.length();
            byArray[n4++] = (byte)n5;
            for (int i3 = 0; i3 < n5; ++i3) {
                char c2 = string.charAt(i3);
                if (c2 > '\u007f') {
                    throw new IllegalArgumentException("Protocol contains invalid character: " + c2 + "(protocol=" + string + ")");
                }
                byArray[n4++] = (byte)c2;
            }
        }
        return byArray;
    }

    static int getEncryptedPacketLength(ByteBuffer[] byteBufferArray, int n2) {
        ByteBuffer byteBuffer = byteBufferArray[n2];
        if (byteBuffer.remaining() >= 5) {
            return SSLUtils.getEncryptedPacketLength(byteBuffer);
        }
        ByteBuffer byteBuffer2 = ByteBuffer.allocate(5);
        do {
            byteBuffer = byteBufferArray[n2++];
            int n3 = byteBuffer.position();
            int n4 = byteBuffer.limit();
            if (byteBuffer.remaining() > byteBuffer2.remaining()) {
                byteBuffer.limit(n3 + byteBuffer2.remaining());
            }
            try {
                byteBuffer2.put(byteBuffer);
            }
            finally {
                byteBuffer.limit(n4);
                byteBuffer.position(n3);
            }
        } while (byteBuffer2.hasRemaining());
        byteBuffer2.flip();
        return SSLUtils.getEncryptedPacketLength(byteBuffer2);
    }

    private static int getEncryptedPacketLength(ByteBuffer byteBuffer) {
        int n2 = byteBuffer.position();
        switch (SSLUtils.unsignedByte(byteBuffer.get(n2))) {
            case 20: 
            case 21: 
            case 22: 
            case 23: {
                break;
            }
            default: {
                return -1;
            }
        }
        short s2 = SSLUtils.unsignedByte(byteBuffer.get(n2 + 1));
        if (s2 != 3) {
            return -1;
        }
        int n3 = SSLUtils.unsignedShort(byteBuffer.getShort(n2 + 3)) + 5;
        if (n3 <= 5) {
            return -1;
        }
        return n3;
    }

    private static short unsignedByte(byte by) {
        return (short)(by & 0xFF);
    }

    private static int unsignedShort(short s2) {
        return s2 & 0xFFFF;
    }

    static String[] concat(String[] ... stringArray) {
        int n2 = 0;
        String[][] stringArray2 = stringArray;
        int n3 = stringArray.length;
        for (int i2 = 0; i2 < n3; ++i2) {
            String[] stringArray3 = stringArray2[i2];
            n2 += stringArray3.length;
        }
        stringArray2 = new String[n2];
        n3 = 0;
        String[][] stringArray4 = stringArray;
        int n4 = stringArray.length;
        for (int i3 = 0; i3 < n4; ++i3) {
            String[] stringArray5 = stringArray4[i3];
            System.arraycopy(stringArray5, 0, stringArray2, n3, stringArray5.length);
            n3 += stringArray5.length;
        }
        return stringArray2;
    }

    private SSLUtils() {
    }

    static final class EngineStates {
        static final int STATE_NEW = 0;
        static final int STATE_MODE_SET = 1;
        static final int STATE_HANDSHAKE_STARTED = 2;
        static final int STATE_HANDSHAKE_COMPLETED = 3;
        static final int STATE_READY_HANDSHAKE_CUT_THROUGH = 4;
        static final int STATE_READY = 5;
        static final int STATE_CLOSED_INBOUND = 6;
        static final int STATE_CLOSED_OUTBOUND = 7;
        static final int STATE_CLOSED = 8;

        private EngineStates() {
        }
    }

    static enum SessionType {
        OPEN_SSL(1),
        OPEN_SSL_WITH_OCSP(2),
        OPEN_SSL_WITH_TLS_SCT(3);

        final int value;

        private SessionType(int n3) {
            this.value = n3;
        }

        static boolean isSupportedType(int n2) {
            return n2 == SessionType.OPEN_SSL.value || n2 == SessionType.OPEN_SSL_WITH_OCSP.value || n2 == SessionType.OPEN_SSL_WITH_TLS_SCT.value;
        }
    }
}


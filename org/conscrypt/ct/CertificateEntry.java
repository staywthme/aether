/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt.ct;

import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import org.conscrypt.OpenSSLX509Certificate;
import org.conscrypt.ct.Serialization;
import org.conscrypt.ct.SerializationException;

public class CertificateEntry {
    private final LogEntryType entryType;
    private final byte[] issuerKeyHash;
    private final byte[] certificate;

    private CertificateEntry(LogEntryType logEntryType, byte[] byArray, byte[] byArray2) {
        if (logEntryType == LogEntryType.PRECERT_ENTRY && byArray2 == null) {
            throw new IllegalArgumentException("issuerKeyHash missing for precert entry.");
        }
        if (logEntryType == LogEntryType.X509_ENTRY && byArray2 != null) {
            throw new IllegalArgumentException("unexpected issuerKeyHash for X509 entry.");
        }
        if (byArray2 != null && byArray2.length != 32) {
            throw new IllegalArgumentException("issuerKeyHash must be 32 bytes long");
        }
        this.entryType = logEntryType;
        this.issuerKeyHash = byArray2;
        this.certificate = byArray;
    }

    public static CertificateEntry createForPrecertificate(byte[] byArray, byte[] byArray2) {
        return new CertificateEntry(LogEntryType.PRECERT_ENTRY, byArray, byArray2);
    }

    public static CertificateEntry createForPrecertificate(OpenSSLX509Certificate object, OpenSSLX509Certificate object2) throws CertificateException {
        try {
            if (!((OpenSSLX509Certificate)object).getNonCriticalExtensionOIDs().contains("1.3.6.1.4.1.11129.2.4.2")) {
                throw new CertificateException("Certificate does not contain embedded signed timestamps");
            }
            object = ((OpenSSLX509Certificate)object).withDeletedExtension("1.3.6.1.4.1.11129.2.4.2");
            object = ((OpenSSLX509Certificate)object).getTBSCertificate();
            object2 = ((OpenSSLX509Certificate)object2).getPublicKey().getEncoded();
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update((byte[])object2);
            object2 = messageDigest.digest();
            return CertificateEntry.createForPrecertificate((byte[])object, (byte[])object2);
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new RuntimeException(noSuchAlgorithmException);
        }
    }

    public static CertificateEntry createForX509Certificate(byte[] byArray) {
        return new CertificateEntry(LogEntryType.X509_ENTRY, byArray, null);
    }

    public static CertificateEntry createForX509Certificate(X509Certificate x509Certificate) throws CertificateEncodingException {
        return CertificateEntry.createForX509Certificate(x509Certificate.getEncoded());
    }

    public LogEntryType getEntryType() {
        return this.entryType;
    }

    public byte[] getCertificate() {
        return this.certificate;
    }

    public byte[] getIssuerKeyHash() {
        return this.issuerKeyHash;
    }

    public void encode(OutputStream outputStream2) throws SerializationException {
        Serialization.writeNumber(outputStream2, this.entryType.ordinal(), 2);
        if (this.entryType == LogEntryType.PRECERT_ENTRY) {
            Serialization.writeFixedBytes(outputStream2, this.issuerKeyHash);
        }
        Serialization.writeVariableBytes(outputStream2, this.certificate, 3);
    }

    public static enum LogEntryType {
        X509_ENTRY,
        PRECERT_ENTRY;

    }
}


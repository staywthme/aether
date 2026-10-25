/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt.ct;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.conscrypt.ct.CertificateEntry;
import org.conscrypt.ct.DigitallySigned;
import org.conscrypt.ct.Serialization;
import org.conscrypt.ct.SerializationException;

public class SignedCertificateTimestamp {
    private final Version version;
    private final byte[] logId;
    private final long timestamp;
    private final byte[] extensions;
    private final DigitallySigned signature;
    private final Origin origin;

    public SignedCertificateTimestamp(Version version, byte[] byArray, long l2, byte[] byArray2, DigitallySigned digitallySigned, Origin origin) {
        this.version = version;
        this.logId = byArray;
        this.timestamp = l2;
        this.extensions = byArray2;
        this.signature = digitallySigned;
        this.origin = origin;
    }

    public Version getVersion() {
        return this.version;
    }

    public byte[] getLogID() {
        return this.logId;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public byte[] getExtensions() {
        return this.extensions;
    }

    public DigitallySigned getSignature() {
        return this.signature;
    }

    public Origin getOrigin() {
        return this.origin;
    }

    public static SignedCertificateTimestamp decode(InputStream inputStream2, Origin origin) throws SerializationException {
        int n2 = Serialization.readNumber(inputStream2, 1);
        if (n2 != Version.V1.ordinal()) {
            throw new SerializationException("Unsupported SCT version " + n2);
        }
        return new SignedCertificateTimestamp(Version.V1, Serialization.readFixedBytes(inputStream2, 32), Serialization.readLong(inputStream2, 8), Serialization.readVariableBytes(inputStream2, 2), DigitallySigned.decode(inputStream2), origin);
    }

    public static SignedCertificateTimestamp decode(byte[] byArray, Origin origin) throws SerializationException {
        return SignedCertificateTimestamp.decode(new ByteArrayInputStream(byArray), origin);
    }

    public void encodeTBS(OutputStream outputStream2, CertificateEntry certificateEntry) throws SerializationException {
        Serialization.writeNumber(outputStream2, this.version.ordinal(), 1);
        Serialization.writeNumber(outputStream2, SignatureType.CERTIFICATE_TIMESTAMP.ordinal(), 1);
        Serialization.writeNumber(outputStream2, this.timestamp, 8);
        certificateEntry.encode(outputStream2);
        Serialization.writeVariableBytes(outputStream2, this.extensions, 2);
    }

    public byte[] encodeTBS(CertificateEntry certificateEntry) throws SerializationException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        this.encodeTBS(byteArrayOutputStream, certificateEntry);
        return byteArrayOutputStream.toByteArray();
    }

    public static enum Origin {
        EMBEDDED,
        TLS_EXTENSION,
        OCSP_RESPONSE;

    }

    public static enum SignatureType {
        CERTIFICATE_TIMESTAMP,
        TREE_HASH;

    }

    public static enum Version {
        V1;

    }
}


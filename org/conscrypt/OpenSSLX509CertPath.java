/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.security.cert.CertPath;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.conscrypt.NativeCrypto;
import org.conscrypt.OpenSSLBIOInputStream;
import org.conscrypt.OpenSSLX509Certificate;
import org.conscrypt.OpenSSLX509CertificateFactory;

final class OpenSSLX509CertPath
extends CertPath {
    private static final long serialVersionUID = -3249106005255170761L;
    private static final byte[] PKCS7_MARKER = new byte[]{45, 45, 45, 45, 45, 66, 69, 71, 73, 78, 32, 80, 75, 67, 83, 55};
    private static final int PUSHBACK_SIZE = 64;
    private static final List<String> ALL_ENCODINGS = Collections.unmodifiableList(Arrays.asList(Encoding.access$000(Encoding.PKI_PATH), Encoding.access$000(Encoding.PKCS7)));
    private static final Encoding DEFAULT_ENCODING = Encoding.PKI_PATH;
    private final List<? extends X509Certificate> mCertificates;

    static Iterator<String> getEncodingsIterator() {
        return ALL_ENCODINGS.iterator();
    }

    OpenSSLX509CertPath(List<? extends X509Certificate> list2) {
        super("X.509");
        this.mCertificates = list2;
    }

    @Override
    public final List<? extends Certificate> getCertificates() {
        return Collections.unmodifiableList(this.mCertificates);
    }

    private byte[] getEncoded(Encoding encoding) throws CertificateEncodingException {
        OpenSSLX509Certificate[] openSSLX509CertificateArray = new OpenSSLX509Certificate[this.mCertificates.size()];
        long[] lArray = new long[openSSLX509CertificateArray.length];
        int n2 = 0;
        for (int i2 = openSSLX509CertificateArray.length - 1; i2 >= 0; --i2) {
            X509Certificate x509Certificate = this.mCertificates.get(n2);
            openSSLX509CertificateArray[i2] = x509Certificate instanceof OpenSSLX509Certificate ? (OpenSSLX509Certificate)x509Certificate : OpenSSLX509Certificate.fromX509Der(x509Certificate.getEncoded());
            lArray[i2] = openSSLX509CertificateArray[i2].getContext();
            ++n2;
        }
        switch (encoding) {
            case PKI_PATH: {
                return NativeCrypto.ASN1_seq_pack_X509(lArray);
            }
            case PKCS7: {
                return NativeCrypto.i2d_PKCS7(lArray);
            }
        }
        throw new CertificateEncodingException("Unknown encoding");
    }

    @Override
    public final byte[] getEncoded() throws CertificateEncodingException {
        return this.getEncoded(DEFAULT_ENCODING);
    }

    @Override
    public final byte[] getEncoded(String string) throws CertificateEncodingException {
        Encoding encoding = Encoding.findByApiName(string);
        if (encoding == null) {
            throw new CertificateEncodingException("Invalid encoding: " + string);
        }
        return this.getEncoded(encoding);
    }

    @Override
    public final Iterator<String> getEncodings() {
        return OpenSSLX509CertPath.getEncodingsIterator();
    }

    private static CertPath fromPkiPathEncoding(InputStream inputStream2) throws CertificateException {
        long[] lArray;
        OpenSSLBIOInputStream openSSLBIOInputStream = new OpenSSLBIOInputStream(inputStream2, true);
        boolean bl = inputStream2.markSupported();
        if (bl) {
            inputStream2.mark(64);
        }
        try {
            lArray = NativeCrypto.ASN1_seq_unpack_X509_bio(openSSLBIOInputStream.getBioContext());
        }
        catch (Exception exception) {
            if (bl) {
                try {
                    inputStream2.reset();
                }
                catch (IOException iOException) {}
            }
            throw new CertificateException(exception);
        }
        finally {
            openSSLBIOInputStream.release();
        }
        if (lArray == null) {
            return new OpenSSLX509CertPath(Collections.emptyList());
        }
        ArrayList<OpenSSLX509Certificate> arrayList = new ArrayList<OpenSSLX509Certificate>(lArray.length);
        for (int i2 = lArray.length - 1; i2 >= 0; --i2) {
            if (lArray[i2] == 0L) continue;
            try {
                arrayList.add(new OpenSSLX509Certificate(lArray[i2]));
                continue;
            }
            catch (OpenSSLX509CertificateFactory.ParsingException parsingException) {
                throw new CertificateParsingException(parsingException);
            }
        }
        return new OpenSSLX509CertPath(arrayList);
    }

    private static CertPath fromPkcs7Encoding(InputStream inputStream2) throws CertificateException {
        try {
            if (inputStream2 == null || inputStream2.available() == 0) {
                return new OpenSSLX509CertPath(Collections.emptyList());
            }
        }
        catch (IOException iOException) {
            throw new CertificateException("Problem reading input stream", iOException);
        }
        boolean bl = inputStream2.markSupported();
        if (bl) {
            inputStream2.mark(64);
        }
        PushbackInputStream pushbackInputStream = new PushbackInputStream(inputStream2, 64);
        try {
            byte[] byArray = new byte[PKCS7_MARKER.length];
            int n2 = pushbackInputStream.read(byArray);
            if (n2 < 0) {
                throw new OpenSSLX509CertificateFactory.ParsingException("inStream is empty");
            }
            pushbackInputStream.unread(byArray, 0, n2);
            if (n2 == PKCS7_MARKER.length && Arrays.equals(PKCS7_MARKER, byArray)) {
                return new OpenSSLX509CertPath(OpenSSLX509Certificate.fromPkcs7PemInputStream(pushbackInputStream));
            }
            return new OpenSSLX509CertPath(OpenSSLX509Certificate.fromPkcs7DerInputStream(pushbackInputStream));
        }
        catch (Exception exception) {
            if (bl) {
                try {
                    inputStream2.reset();
                }
                catch (IOException iOException) {}
            }
            throw new CertificateException(exception);
        }
    }

    private static CertPath fromEncoding(InputStream inputStream2, Encoding encoding) throws CertificateException {
        switch (encoding) {
            case PKI_PATH: {
                return OpenSSLX509CertPath.fromPkiPathEncoding(inputStream2);
            }
            case PKCS7: {
                return OpenSSLX509CertPath.fromPkcs7Encoding(inputStream2);
            }
        }
        throw new CertificateEncodingException("Unknown encoding");
    }

    static CertPath fromEncoding(InputStream inputStream2, String string) throws CertificateException {
        if (inputStream2 == null) {
            throw new CertificateException("inStream == null");
        }
        Encoding encoding = Encoding.findByApiName(string);
        if (encoding == null) {
            throw new CertificateException("Invalid encoding: " + string);
        }
        return OpenSSLX509CertPath.fromEncoding(inputStream2, encoding);
    }

    static CertPath fromEncoding(InputStream inputStream2) throws CertificateException {
        if (inputStream2 == null) {
            throw new CertificateException("inStream == null");
        }
        return OpenSSLX509CertPath.fromEncoding(inputStream2, DEFAULT_ENCODING);
    }

    static enum Encoding {
        PKI_PATH("PkiPath"),
        PKCS7("PKCS7");

        private final String apiName;

        private Encoding(String string2) {
            this.apiName = string2;
        }

        static Encoding findByApiName(String string) throws CertificateEncodingException {
            for (Encoding encoding : Encoding.values()) {
                if (!encoding.apiName.equals(string)) continue;
                return encoding;
            }
            return null;
        }

        static /* synthetic */ String access$000(Encoding encoding) {
            return encoding.apiName;
        }
    }
}


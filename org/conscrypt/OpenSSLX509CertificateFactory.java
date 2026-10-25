/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.security.cert.CRL;
import java.security.cert.CRLException;
import java.security.cert.CertPath;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactorySpi;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.conscrypt.OpenSSLX509CRL;
import org.conscrypt.OpenSSLX509CertPath;
import org.conscrypt.OpenSSLX509Certificate;

public class OpenSSLX509CertificateFactory
extends CertificateFactorySpi {
    private static final byte[] PKCS7_MARKER = new byte[]{45, 45, 45, 45, 45, 66, 69, 71, 73, 78, 32, 80, 75, 67, 83, 55};
    private static final int PUSHBACK_SIZE = 64;
    private Parser<OpenSSLX509Certificate> certificateParser = new Parser<OpenSSLX509Certificate>(){

        @Override
        public OpenSSLX509Certificate fromX509PemInputStream(InputStream inputStream2) throws ParsingException {
            return OpenSSLX509Certificate.fromX509PemInputStream(inputStream2);
        }

        @Override
        public OpenSSLX509Certificate fromX509DerInputStream(InputStream inputStream2) throws ParsingException {
            return OpenSSLX509Certificate.fromX509DerInputStream(inputStream2);
        }

        @Override
        public List<? extends OpenSSLX509Certificate> fromPkcs7PemInputStream(InputStream inputStream2) throws ParsingException {
            return OpenSSLX509Certificate.fromPkcs7PemInputStream(inputStream2);
        }

        @Override
        public List<? extends OpenSSLX509Certificate> fromPkcs7DerInputStream(InputStream inputStream2) throws ParsingException {
            return OpenSSLX509Certificate.fromPkcs7DerInputStream(inputStream2);
        }
    };
    private Parser<OpenSSLX509CRL> crlParser = new Parser<OpenSSLX509CRL>(){

        @Override
        public OpenSSLX509CRL fromX509PemInputStream(InputStream inputStream2) throws ParsingException {
            return OpenSSLX509CRL.fromX509PemInputStream(inputStream2);
        }

        @Override
        public OpenSSLX509CRL fromX509DerInputStream(InputStream inputStream2) throws ParsingException {
            return OpenSSLX509CRL.fromX509DerInputStream(inputStream2);
        }

        @Override
        public List<? extends OpenSSLX509CRL> fromPkcs7PemInputStream(InputStream inputStream2) throws ParsingException {
            return OpenSSLX509CRL.fromPkcs7PemInputStream(inputStream2);
        }

        @Override
        public List<? extends OpenSSLX509CRL> fromPkcs7DerInputStream(InputStream inputStream2) throws ParsingException {
            return OpenSSLX509CRL.fromPkcs7DerInputStream(inputStream2);
        }
    };

    @Override
    public Certificate engineGenerateCertificate(InputStream inputStream2) throws CertificateException {
        try {
            return this.certificateParser.generateItem(inputStream2);
        }
        catch (ParsingException parsingException) {
            throw new CertificateException(parsingException);
        }
    }

    @Override
    public Collection<? extends Certificate> engineGenerateCertificates(InputStream inputStream2) throws CertificateException {
        try {
            return this.certificateParser.generateItems(inputStream2);
        }
        catch (ParsingException parsingException) {
            throw new CertificateException(parsingException);
        }
    }

    @Override
    public CRL engineGenerateCRL(InputStream inputStream2) throws CRLException {
        try {
            return this.crlParser.generateItem(inputStream2);
        }
        catch (ParsingException parsingException) {
            throw new CRLException(parsingException);
        }
    }

    @Override
    public Collection<? extends CRL> engineGenerateCRLs(InputStream inputStream2) throws CRLException {
        if (inputStream2 == null) {
            return Collections.emptyList();
        }
        try {
            return this.crlParser.generateItems(inputStream2);
        }
        catch (ParsingException parsingException) {
            throw new CRLException(parsingException);
        }
    }

    @Override
    public Iterator<String> engineGetCertPathEncodings() {
        return OpenSSLX509CertPath.getEncodingsIterator();
    }

    @Override
    public CertPath engineGenerateCertPath(InputStream inputStream2) throws CertificateException {
        return OpenSSLX509CertPath.fromEncoding(inputStream2);
    }

    @Override
    public CertPath engineGenerateCertPath(InputStream inputStream2, String string) throws CertificateException {
        return OpenSSLX509CertPath.fromEncoding(inputStream2, string);
    }

    @Override
    public CertPath engineGenerateCertPath(List<? extends Certificate> list2) throws CertificateException {
        ArrayList<X509Certificate> arrayList = new ArrayList<X509Certificate>(list2.size());
        for (int i2 = 0; i2 < list2.size(); ++i2) {
            Certificate certificate = list2.get(i2);
            if (!(certificate instanceof X509Certificate)) {
                throw new CertificateException("Certificate not X.509 type at index " + i2);
            }
            arrayList.add((X509Certificate)certificate);
        }
        return new OpenSSLX509CertPath(arrayList);
    }

    static abstract class Parser<T> {
        private Parser() {
        }

        T generateItem(InputStream inputStream2) throws ParsingException {
            if (inputStream2 == null) {
                throw new ParsingException("inStream == null");
            }
            boolean bl = inputStream2.markSupported();
            if (bl) {
                inputStream2.mark(PKCS7_MARKER.length);
            }
            PushbackInputStream pushbackInputStream = new PushbackInputStream(inputStream2, 64);
            try {
                byte[] byArray = new byte[PKCS7_MARKER.length];
                int n2 = pushbackInputStream.read(byArray);
                if (n2 < 0) {
                    throw new ParsingException("inStream is empty");
                }
                pushbackInputStream.unread(byArray, 0, n2);
                if (byArray[0] == 45) {
                    if (n2 == PKCS7_MARKER.length && Arrays.equals(PKCS7_MARKER, byArray)) {
                        List<T> list2 = this.fromPkcs7PemInputStream(pushbackInputStream);
                        if (list2.size() == 0) {
                            return null;
                        }
                        list2.get(0);
                    } else {
                        return this.fromX509PemInputStream(pushbackInputStream);
                    }
                }
                if (byArray[4] == 6) {
                    List<T> list3 = this.fromPkcs7DerInputStream(pushbackInputStream);
                    if (list3.size() == 0) {
                        return null;
                    }
                    return list3.get(0);
                }
                return this.fromX509DerInputStream(pushbackInputStream);
            }
            catch (Exception exception) {
                if (bl) {
                    try {
                        inputStream2.reset();
                    }
                    catch (IOException iOException) {}
                }
                throw new ParsingException(exception);
            }
        }

        Collection<? extends T> generateItems(InputStream inputStream2) throws ParsingException {
            T t2;
            Object object;
            if (inputStream2 == null) {
                throw new ParsingException("inStream == null");
            }
            try {
                if (inputStream2.available() == 0) {
                    return new ArrayList();
                }
            }
            catch (IOException iOException) {
                throw new ParsingException("Problem reading input stream", iOException);
            }
            boolean bl = inputStream2.markSupported();
            if (bl) {
                inputStream2.mark(64);
            }
            PushbackInputStream pushbackInputStream = new PushbackInputStream(inputStream2, 64);
            try {
                object = new byte[PKCS7_MARKER.length];
                int n2 = pushbackInputStream.read((byte[])object);
                if (n2 < 0) {
                    throw new ParsingException("inStream is empty");
                }
                pushbackInputStream.unread((byte[])object, 0, n2);
                if (n2 == PKCS7_MARKER.length && Arrays.equals(PKCS7_MARKER, (byte[])object)) {
                    return this.fromPkcs7PemInputStream(pushbackInputStream);
                }
                if (object[4] == 6) {
                    return this.fromPkcs7DerInputStream(pushbackInputStream);
                }
            }
            catch (Exception exception) {
                if (bl) {
                    try {
                        inputStream2.reset();
                    }
                    catch (IOException iOException) {}
                }
                throw new ParsingException(exception);
            }
            object = new ArrayList();
            do {
                if (bl) {
                    inputStream2.mark(64);
                }
                try {
                    t2 = this.generateItem(pushbackInputStream);
                    object.add(t2);
                }
                catch (ParsingException parsingException) {
                    if (bl) {
                        try {
                            inputStream2.reset();
                        }
                        catch (IOException iOException) {}
                    }
                    t2 = null;
                }
            } while (t2 != null);
            return object;
        }

        protected abstract T fromX509PemInputStream(InputStream var1) throws ParsingException;

        protected abstract T fromX509DerInputStream(InputStream var1) throws ParsingException;

        protected abstract List<? extends T> fromPkcs7PemInputStream(InputStream var1) throws ParsingException;

        protected abstract List<? extends T> fromPkcs7DerInputStream(InputStream var1) throws ParsingException;
    }

    static class ParsingException
    extends Exception {
        private static final long serialVersionUID = 8390802697728301325L;

        ParsingException(String string) {
            super(string);
        }

        ParsingException(Exception exception) {
            super(exception);
        }

        ParsingException(String string, Exception exception) {
            super(string, exception);
        }
    }
}


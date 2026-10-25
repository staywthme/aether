/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;

public final class ChainStrengthAnalyzer {
    private static final int MIN_RSA_MODULUS_LEN_BITS = 1024;
    private static final int MIN_EC_FIELD_SIZE_BITS = 160;
    private static final int MIN_DSA_P_LEN_BITS = 1024;
    private static final int MIN_DSA_Q_LEN_BITS = 160;
    private static final String[] SIGNATURE_ALGORITHM_OID_BLACKLIST = new String[]{"1.2.840.113549.1.1.2", "1.2.840.113549.1.1.3", "1.2.840.113549.1.1.4", "1.2.840.113549.1.1.5", "1.2.840.10040.4.3", "1.2.840.10045.4.1"};

    public static final void check(X509Certificate[] x509CertificateArray) throws CertificateException {
        for (X509Certificate x509Certificate : x509CertificateArray) {
            try {
                ChainStrengthAnalyzer.checkCert(x509Certificate);
            }
            catch (CertificateException certificateException) {
                throw new CertificateException("Unacceptable certificate: " + x509Certificate.getSubjectX500Principal(), certificateException);
            }
        }
    }

    public static final void check(List<X509Certificate> object) throws CertificateException {
        object = object.iterator();
        while (object.hasNext()) {
            X509Certificate x509Certificate = (X509Certificate)object.next();
            try {
                ChainStrengthAnalyzer.checkCert(x509Certificate);
            }
            catch (CertificateException certificateException) {
                throw new CertificateException("Unacceptable certificate: " + x509Certificate.getSubjectX500Principal(), certificateException);
            }
        }
    }

    public static final void checkCert(X509Certificate x509Certificate) throws CertificateException {
        ChainStrengthAnalyzer.checkKeyLength(x509Certificate);
        ChainStrengthAnalyzer.checkSignatureAlgorithm(x509Certificate);
    }

    private static void checkKeyLength(X509Certificate serializable) throws CertificateException {
        if ((serializable = ((Certificate)serializable).getPublicKey()) instanceof RSAPublicKey) {
            int n2 = ((RSAPublicKey)serializable).getModulus().bitLength();
            if (n2 < 1024) {
                throw new CertificateException("RSA modulus is < 1024 bits");
            }
            return;
        }
        if (serializable instanceof ECPublicKey) {
            int n3 = ((ECPublicKey)serializable).getParams().getCurve().getField().getFieldSize();
            if (n3 < 160) {
                throw new CertificateException("EC key field size is < 160 bits");
            }
            return;
        }
        if (serializable instanceof DSAPublicKey) {
            int n4 = ((DSAPublicKey)serializable).getParams().getP().bitLength();
            int n5 = ((DSAPublicKey)serializable).getParams().getQ().bitLength();
            if (n4 < 1024 || n5 < 160) {
                throw new CertificateException("DSA key length is < (1024, 160) bits");
            }
            return;
        }
        throw new CertificateException("Rejecting unknown key class " + serializable.getClass().getName());
    }

    private static void checkSignatureAlgorithm(X509Certificate object) throws CertificateException {
        object = ((X509Certificate)object).getSigAlgOID();
        String[] stringArray = SIGNATURE_ALGORITHM_OID_BLACKLIST;
        int n2 = SIGNATURE_ALGORITHM_OID_BLACKLIST.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            String string = stringArray[i2];
            if (!((String)object).equals(string)) continue;
            throw new CertificateException("Signature uses an insecure hash function: " + (String)object);
        }
    }
}


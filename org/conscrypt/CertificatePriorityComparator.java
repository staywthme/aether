/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public final class CertificatePriorityComparator
implements Comparator<X509Certificate> {
    private static final Map<String, Integer> ALGORITHM_OID_PRIORITY_MAP;
    private static final Integer PRIORITY_MD5;
    private static final Integer PRIORITY_SHA1;
    private static final Integer PRIORITY_SHA224;
    private static final Integer PRIORITY_SHA256;
    private static final Integer PRIORITY_SHA384;
    private static final Integer PRIORITY_SHA512;
    private static final Integer PRIORITY_UNKNOWN;

    @Override
    public final int compare(X509Certificate serializable, X509Certificate serializable2) {
        int n2;
        int n3 = serializable.getSubjectDN().equals(serializable.getIssuerDN());
        if (n3 != (n2 = serializable2.getSubjectDN().equals(serializable2.getIssuerDN()))) {
            if (n2) {
                return 1;
            }
            return -1;
        }
        n3 = this.compareStrength((X509Certificate)serializable2, (X509Certificate)serializable);
        if (n3 != 0) {
            return n3;
        }
        Date date = serializable.getNotAfter();
        Date date2 = serializable2.getNotAfter();
        int n4 = date2.compareTo(date);
        if (n4 != 0) {
            return n4;
        }
        serializable = serializable.getNotBefore();
        serializable2 = serializable2.getNotBefore();
        return ((Date)serializable2).compareTo((Date)serializable);
    }

    private int compareStrength(X509Certificate x509Certificate, X509Certificate x509Certificate2) {
        PublicKey publicKey;
        PublicKey publicKey2 = x509Certificate.getPublicKey();
        int n2 = this.compareKeyAlgorithm(publicKey2, publicKey = x509Certificate2.getPublicKey());
        if (n2 != 0) {
            return n2;
        }
        n2 = this.compareKeySize(publicKey2, publicKey);
        if (n2 != 0) {
            return n2;
        }
        return this.compareSignatureAlgorithm(x509Certificate, x509Certificate2);
    }

    private int compareKeyAlgorithm(PublicKey object, PublicKey object2) {
        if (((String)(object = object.getAlgorithm())).equalsIgnoreCase((String)(object2 = object2.getAlgorithm()))) {
            return 0;
        }
        if ("EC".equalsIgnoreCase((String)object)) {
            return 1;
        }
        return -1;
    }

    private int compareKeySize(PublicKey publicKey, PublicKey publicKey2) {
        String string;
        String string2 = publicKey.getAlgorithm();
        if (!string2.equalsIgnoreCase(string = publicKey2.getAlgorithm())) {
            throw new IllegalArgumentException("Keys are not of the same type");
        }
        int n2 = this.getKeySize(publicKey);
        int n3 = this.getKeySize(publicKey2);
        return n2 - n3;
    }

    private int getKeySize(PublicKey publicKey) {
        if (publicKey instanceof ECPublicKey) {
            return ((ECPublicKey)publicKey).getParams().getCurve().getField().getFieldSize();
        }
        if (publicKey instanceof RSAPublicKey) {
            return ((RSAPublicKey)publicKey).getModulus().bitLength();
        }
        throw new IllegalArgumentException("Unsupported public key type: " + publicKey.getClass().getName());
    }

    private int compareSignatureAlgorithm(X509Certificate serializable, X509Certificate serializable2) {
        serializable = ALGORITHM_OID_PRIORITY_MAP.get(((X509Certificate)serializable).getSigAlgOID());
        serializable2 = ALGORITHM_OID_PRIORITY_MAP.get(serializable2.getSigAlgOID());
        if (serializable == null) {
            serializable = PRIORITY_UNKNOWN;
        }
        if (serializable2 == null) {
            serializable2 = PRIORITY_UNKNOWN;
        }
        return (Integer)serializable2 - (Integer)serializable;
    }

    static {
        PRIORITY_MD5 = 6;
        PRIORITY_SHA1 = 5;
        PRIORITY_SHA224 = 4;
        PRIORITY_SHA256 = 3;
        PRIORITY_SHA384 = 2;
        PRIORITY_SHA512 = 1;
        PRIORITY_UNKNOWN = -1;
        ALGORITHM_OID_PRIORITY_MAP = new HashMap<String, Integer>();
        ALGORITHM_OID_PRIORITY_MAP.put("1.2.840.113549.1.1.13", PRIORITY_SHA512);
        ALGORITHM_OID_PRIORITY_MAP.put("1.2.840.113549.1.1.12", PRIORITY_SHA384);
        ALGORITHM_OID_PRIORITY_MAP.put("1.2.840.113549.1.1.11", PRIORITY_SHA256);
        ALGORITHM_OID_PRIORITY_MAP.put("1.2.840.113549.1.1.14", PRIORITY_SHA224);
        ALGORITHM_OID_PRIORITY_MAP.put("1.2.840.113549.1.1.5", PRIORITY_SHA1);
        ALGORITHM_OID_PRIORITY_MAP.put("1.2.840.113549.1.1.4", PRIORITY_MD5);
        ALGORITHM_OID_PRIORITY_MAP.put("1.2.840.10045.4.3.4", PRIORITY_SHA512);
        ALGORITHM_OID_PRIORITY_MAP.put("1.2.840.10045.4.3.3", PRIORITY_SHA384);
        ALGORITHM_OID_PRIORITY_MAP.put("1.2.840.10045.4.3.2", PRIORITY_SHA256);
        ALGORITHM_OID_PRIORITY_MAP.put("1.2.840.10045.4.3.1", PRIORITY_SHA224);
        ALGORITHM_OID_PRIORITY_MAP.put("1.2.840.10045.4.1", PRIORITY_SHA1);
    }
}


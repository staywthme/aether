/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt.ct;

import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.conscrypt.NativeCrypto;
import org.conscrypt.OpenSSLX509Certificate;
import org.conscrypt.ct.CTLogInfo;
import org.conscrypt.ct.CTLogStore;
import org.conscrypt.ct.CTVerificationResult;
import org.conscrypt.ct.CertificateEntry;
import org.conscrypt.ct.Serialization;
import org.conscrypt.ct.SerializationException;
import org.conscrypt.ct.SignedCertificateTimestamp;
import org.conscrypt.ct.VerifiedSCT;

public class CTVerifier {
    private final CTLogStore store;

    public CTVerifier(CTLogStore cTLogStore) {
        this.store = cTLogStore;
    }

    public CTVerificationResult verifySignedCertificateTimestamps(List<X509Certificate> object, byte[] byArray, byte[] byArray2) throws CertificateEncodingException {
        OpenSSLX509Certificate[] openSSLX509CertificateArray = new OpenSSLX509Certificate[object.size()];
        int n2 = 0;
        object = object.iterator();
        while (object.hasNext()) {
            X509Certificate x509Certificate = (X509Certificate)object.next();
            openSSLX509CertificateArray[n2++] = OpenSSLX509Certificate.fromCertificate(x509Certificate);
        }
        return this.verifySignedCertificateTimestamps(openSSLX509CertificateArray, byArray, byArray2);
    }

    public CTVerificationResult verifySignedCertificateTimestamps(OpenSSLX509Certificate[] openSSLX509CertificateArray, byte[] object, byte[] byArray) throws CertificateEncodingException {
        if (openSSLX509CertificateArray.length == 0) {
            throw new IllegalArgumentException("Chain of certificates mustn't be empty.");
        }
        OpenSSLX509Certificate openSSLX509Certificate = openSSLX509CertificateArray[0];
        CTVerificationResult cTVerificationResult = new CTVerificationResult();
        object = this.getSCTsFromTLSExtension((byte[])object);
        this.verifyExternalSCTs((List<SignedCertificateTimestamp>)object, openSSLX509Certificate, cTVerificationResult);
        object = this.getSCTsFromOCSPResponse(byArray, openSSLX509CertificateArray);
        this.verifyExternalSCTs((List<SignedCertificateTimestamp>)object, openSSLX509Certificate, cTVerificationResult);
        object = this.getSCTsFromX509Extension(openSSLX509CertificateArray[0]);
        this.verifyEmbeddedSCTs((List<SignedCertificateTimestamp>)object, openSSLX509CertificateArray, cTVerificationResult);
        return cTVerificationResult;
    }

    /*
     * WARNING - void declaration
     */
    private void verifyEmbeddedSCTs(List<SignedCertificateTimestamp> object, OpenSSLX509Certificate[] object22, CTVerificationResult cTVerificationResult) {
        void var3_7;
        if (object.isEmpty()) {
            return;
        }
        CertificateEntry certificateEntry = null;
        if (((Object)object22).length >= 2) {
            Iterator iterator2 = object22[0];
            Object object2 = object22[1];
            try {
                certificateEntry = CertificateEntry.createForPrecertificate(iterator2, (OpenSSLX509Certificate)object2);
            }
            catch (CertificateException certificateException) {}
        }
        if (certificateEntry == null) {
            this.markSCTsAsInvalid((List<SignedCertificateTimestamp>)object, (CTVerificationResult)var3_7);
            return;
        }
        for (SignedCertificateTimestamp signedCertificateTimestamp : object) {
            object = this.verifySingleSCT(signedCertificateTimestamp, certificateEntry);
            var3_7.add(new VerifiedSCT(signedCertificateTimestamp, (VerifiedSCT.Status)((Object)object)));
        }
    }

    private void verifyExternalSCTs(List<SignedCertificateTimestamp> object, OpenSSLX509Certificate object2, CTVerificationResult cTVerificationResult) {
        if (object.isEmpty()) {
            return;
        }
        try {
            object2 = CertificateEntry.createForX509Certificate((X509Certificate)object2);
        }
        catch (CertificateException certificateException) {
            this.markSCTsAsInvalid((List<SignedCertificateTimestamp>)object, cTVerificationResult);
            return;
        }
        object = object.iterator();
        while (object.hasNext()) {
            SignedCertificateTimestamp signedCertificateTimestamp = (SignedCertificateTimestamp)object.next();
            VerifiedSCT.Status status = this.verifySingleSCT(signedCertificateTimestamp, (CertificateEntry)object2);
            cTVerificationResult.add(new VerifiedSCT(signedCertificateTimestamp, status));
        }
    }

    private VerifiedSCT.Status verifySingleSCT(SignedCertificateTimestamp signedCertificateTimestamp, CertificateEntry certificateEntry) {
        CTLogInfo cTLogInfo = this.store.getKnownLog(signedCertificateTimestamp.getLogID());
        if (cTLogInfo == null) {
            return VerifiedSCT.Status.UNKNOWN_LOG;
        }
        return cTLogInfo.verifySingleSCT(signedCertificateTimestamp, certificateEntry);
    }

    private void markSCTsAsInvalid(List<SignedCertificateTimestamp> object, CTVerificationResult cTVerificationResult) {
        object = object.iterator();
        while (object.hasNext()) {
            SignedCertificateTimestamp signedCertificateTimestamp = (SignedCertificateTimestamp)object.next();
            cTVerificationResult.add(new VerifiedSCT(signedCertificateTimestamp, VerifiedSCT.Status.INVALID_SCT));
        }
    }

    /*
     * WARNING - void declaration
     */
    private static List<SignedCertificateTimestamp> getSCTsFromSCTList(byte[] object, SignedCertificateTimestamp.Origin origin) {
        byte[][] byArray;
        if (object == null) {
            return Collections.emptyList();
        }
        try {
            byArray = Serialization.readList((byte[])object, 2, 2);
        }
        catch (SerializationException serializationException) {
            return Collections.emptyList();
        }
        ArrayList<SignedCertificateTimestamp> arrayList = new ArrayList<SignedCertificateTimestamp>();
        byte[][] byArray2 = byArray;
        int n2 = byArray.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            Object object2 = byArray2[i2];
            try {
                void var1_3;
                object2 = SignedCertificateTimestamp.decode(object2, (SignedCertificateTimestamp.Origin)var1_3);
                arrayList.add((SignedCertificateTimestamp)object2);
                continue;
            }
            catch (SerializationException serializationException) {}
        }
        return arrayList;
    }

    private List<SignedCertificateTimestamp> getSCTsFromTLSExtension(byte[] byArray) {
        return CTVerifier.getSCTsFromSCTList(byArray, SignedCertificateTimestamp.Origin.TLS_EXTENSION);
    }

    private List<SignedCertificateTimestamp> getSCTsFromOCSPResponse(byte[] byArray, OpenSSLX509Certificate[] openSSLX509CertificateArray) {
        if (byArray == null || openSSLX509CertificateArray.length < 2) {
            return Collections.emptyList();
        }
        if ((byArray = NativeCrypto.get_ocsp_single_extension(byArray, "1.3.6.1.4.1.11129.2.4.5", openSSLX509CertificateArray[0].getContext(), openSSLX509CertificateArray[0], openSSLX509CertificateArray[1].getContext(), openSSLX509CertificateArray[1])) == null) {
            return Collections.emptyList();
        }
        try {
            return CTVerifier.getSCTsFromSCTList(Serialization.readDEROctetString(Serialization.readDEROctetString(byArray)), SignedCertificateTimestamp.Origin.OCSP_RESPONSE);
        }
        catch (SerializationException serializationException) {
            return Collections.emptyList();
        }
    }

    private List<SignedCertificateTimestamp> getSCTsFromX509Extension(OpenSSLX509Certificate object) {
        byte[] byArray = ((OpenSSLX509Certificate)object).getExtensionValue("1.3.6.1.4.1.11129.2.4.2");
        object = byArray;
        if (byArray == null) {
            return Collections.emptyList();
        }
        try {
            return CTVerifier.getSCTsFromSCTList(Serialization.readDEROctetString(Serialization.readDEROctetString((byte[])object)), SignedCertificateTimestamp.Origin.EMBEDDED);
        }
        catch (SerializationException serializationException) {
            return Collections.emptyList();
        }
    }
}


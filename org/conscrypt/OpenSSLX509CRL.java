/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Principal;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CRLException;
import java.security.cert.Certificate;
import java.security.cert.X509CRL;
import java.security.cert.X509CRLEntry;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.security.auth.x500.X500Principal;
import org.conscrypt.NativeCrypto;
import org.conscrypt.OidData;
import org.conscrypt.OpenSSLBIOInputStream;
import org.conscrypt.OpenSSLKey;
import org.conscrypt.OpenSSLKeyHolder;
import org.conscrypt.OpenSSLX509CRLEntry;
import org.conscrypt.OpenSSLX509Certificate;
import org.conscrypt.OpenSSLX509CertificateFactory;
import org.conscrypt.Platform;

final class OpenSSLX509CRL
extends X509CRL {
    private final long mContext;
    private final Date thisUpdate;
    private final Date nextUpdate;

    private OpenSSLX509CRL(long l2) throws OpenSSLX509CertificateFactory.ParsingException {
        this.mContext = l2;
        this.thisUpdate = OpenSSLX509CRL.toDate(NativeCrypto.X509_CRL_get_lastUpdate(this.mContext, this));
        this.nextUpdate = OpenSSLX509CRL.toDate(NativeCrypto.X509_CRL_get_nextUpdate(this.mContext, this));
    }

    static Date toDate(long l2) throws OpenSSLX509CertificateFactory.ParsingException {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.set(14, 0);
        NativeCrypto.ASN1_TIME_to_Calendar(l2, calendar);
        return calendar.getTime();
    }

    static OpenSSLX509CRL fromX509DerInputStream(InputStream inputStream2) throws OpenSSLX509CertificateFactory.ParsingException {
        inputStream2 = new OpenSSLBIOInputStream(inputStream2, true);
        try {
            long l2 = NativeCrypto.d2i_X509_CRL_bio(((OpenSSLBIOInputStream)inputStream2).getBioContext());
            if (l2 == 0L) {
                return null;
            }
            OpenSSLX509CRL openSSLX509CRL = new OpenSSLX509CRL(l2);
            return openSSLX509CRL;
        }
        catch (Exception exception) {
            throw new OpenSSLX509CertificateFactory.ParsingException(exception);
        }
        finally {
            ((OpenSSLBIOInputStream)inputStream2).release();
        }
    }

    static List<OpenSSLX509CRL> fromPkcs7DerInputStream(InputStream inputStream2) throws OpenSSLX509CertificateFactory.ParsingException {
        long[] lArray;
        inputStream2 = new OpenSSLBIOInputStream(inputStream2, true);
        try {
            lArray = NativeCrypto.d2i_PKCS7_bio(((OpenSSLBIOInputStream)inputStream2).getBioContext(), 2);
        }
        catch (Exception exception) {
            throw new OpenSSLX509CertificateFactory.ParsingException(exception);
        }
        finally {
            ((OpenSSLBIOInputStream)inputStream2).release();
        }
        ArrayList<OpenSSLX509CRL> arrayList = new ArrayList<OpenSSLX509CRL>(lArray.length);
        for (int i2 = 0; i2 < lArray.length; ++i2) {
            if (lArray[i2] == 0L) continue;
            arrayList.add(new OpenSSLX509CRL(lArray[i2]));
        }
        return arrayList;
    }

    static OpenSSLX509CRL fromX509PemInputStream(InputStream inputStream2) throws OpenSSLX509CertificateFactory.ParsingException {
        inputStream2 = new OpenSSLBIOInputStream(inputStream2, true);
        try {
            long l2 = NativeCrypto.PEM_read_bio_X509_CRL(((OpenSSLBIOInputStream)inputStream2).getBioContext());
            if (l2 == 0L) {
                return null;
            }
            OpenSSLX509CRL openSSLX509CRL = new OpenSSLX509CRL(l2);
            return openSSLX509CRL;
        }
        catch (Exception exception) {
            throw new OpenSSLX509CertificateFactory.ParsingException(exception);
        }
        finally {
            ((OpenSSLBIOInputStream)inputStream2).release();
        }
    }

    static List<OpenSSLX509CRL> fromPkcs7PemInputStream(InputStream inputStream2) throws OpenSSLX509CertificateFactory.ParsingException {
        long[] lArray;
        inputStream2 = new OpenSSLBIOInputStream(inputStream2, true);
        try {
            lArray = NativeCrypto.PEM_read_bio_PKCS7(((OpenSSLBIOInputStream)inputStream2).getBioContext(), 2);
        }
        catch (Exception exception) {
            throw new OpenSSLX509CertificateFactory.ParsingException(exception);
        }
        finally {
            ((OpenSSLBIOInputStream)inputStream2).release();
        }
        ArrayList<OpenSSLX509CRL> arrayList = new ArrayList<OpenSSLX509CRL>(lArray.length);
        for (int i2 = 0; i2 < lArray.length; ++i2) {
            if (lArray[i2] == 0L) continue;
            arrayList.add(new OpenSSLX509CRL(lArray[i2]));
        }
        return arrayList;
    }

    @Override
    public final Set<String> getCriticalExtensionOIDs() {
        String[] stringArray = NativeCrypto.get_X509_CRL_ext_oids(this.mContext, this, 1);
        if (stringArray.length == 0 && NativeCrypto.get_X509_CRL_ext_oids(this.mContext, this, 0).length == 0) {
            return null;
        }
        return new HashSet<String>(Arrays.asList(stringArray));
    }

    @Override
    public final byte[] getExtensionValue(String string) {
        return NativeCrypto.X509_CRL_get_ext_oid(this.mContext, this, string);
    }

    @Override
    public final Set<String> getNonCriticalExtensionOIDs() {
        String[] stringArray = NativeCrypto.get_X509_CRL_ext_oids(this.mContext, this, 0);
        if (stringArray.length == 0 && NativeCrypto.get_X509_CRL_ext_oids(this.mContext, this, 1).length == 0) {
            return null;
        }
        return new HashSet<String>(Arrays.asList(stringArray));
    }

    @Override
    public final boolean hasUnsupportedCriticalExtension() {
        String[] stringArray = NativeCrypto.get_X509_CRL_ext_oids(this.mContext, this, 1);
        String[] stringArray2 = stringArray;
        stringArray2 = stringArray;
        int n2 = stringArray.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            String string = stringArray2[i2];
            long l2 = NativeCrypto.X509_CRL_get_ext(this.mContext, this, string);
            if (NativeCrypto.X509_supported_extension(l2) == 1) continue;
            return true;
        }
        return false;
    }

    @Override
    public final byte[] getEncoded() throws CRLException {
        return NativeCrypto.i2d_X509_CRL(this.mContext, this);
    }

    private void verifyOpenSSL(OpenSSLKey openSSLKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        try {
            NativeCrypto.X509_CRL_verify(this.mContext, this, openSSLKey.getNativeRef());
            return;
        }
        catch (BadPaddingException | IllegalBlockSizeException generalSecurityException) {
            throw new SignatureException(generalSecurityException);
        }
    }

    private void verifyInternal(PublicKey publicKey, String object) throws CRLException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
        String string = this.getSigAlgName();
        if (string == null) {
            string = this.getSigAlgOID();
        }
        object = object == null ? Signature.getInstance(string) : Signature.getInstance(string, (String)object);
        ((Signature)object).initVerify(publicKey);
        ((Signature)object).update(this.getTBSCertList());
        if (!((Signature)object).verify(this.getSignature())) {
            throw new SignatureException("signature did not verify");
        }
    }

    @Override
    public final void verify(PublicKey object) throws CRLException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
        if (object instanceof OpenSSLKeyHolder) {
            object = ((OpenSSLKeyHolder)object).getOpenSSLKey();
            this.verifyOpenSSL((OpenSSLKey)object);
            return;
        }
        this.verifyInternal((PublicKey)object, null);
    }

    @Override
    public final void verify(PublicKey publicKey, String string) throws CRLException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
        this.verifyInternal(publicKey, string);
    }

    @Override
    public final int getVersion() {
        return (int)NativeCrypto.X509_CRL_get_version(this.mContext, this) + 1;
    }

    @Override
    public final Principal getIssuerDN() {
        return this.getIssuerX500Principal();
    }

    @Override
    public final X500Principal getIssuerX500Principal() {
        byte[] byArray = NativeCrypto.X509_CRL_get_issuer_name(this.mContext, this);
        return new X500Principal(byArray);
    }

    @Override
    public final Date getThisUpdate() {
        return (Date)this.thisUpdate.clone();
    }

    @Override
    public final Date getNextUpdate() {
        return (Date)this.nextUpdate.clone();
    }

    @Override
    public final X509CRLEntry getRevokedCertificate(BigInteger bigInteger) {
        long l2 = NativeCrypto.X509_CRL_get0_by_serial(this.mContext, this, bigInteger.toByteArray());
        if (l2 == 0L) {
            return null;
        }
        try {
            return new OpenSSLX509CRLEntry(NativeCrypto.X509_REVOKED_dup(l2));
        }
        catch (OpenSSLX509CertificateFactory.ParsingException parsingException) {
            return null;
        }
    }

    @Override
    public final X509CRLEntry getRevokedCertificate(X509Certificate x509Certificate) {
        if (x509Certificate instanceof OpenSSLX509Certificate) {
            long l2 = NativeCrypto.X509_CRL_get0_by_cert(this.mContext, this, ((OpenSSLX509Certificate)(x509Certificate = (OpenSSLX509Certificate)x509Certificate)).getContext(), (OpenSSLX509Certificate)x509Certificate);
            if (l2 == 0L) {
                return null;
            }
            try {
                return new OpenSSLX509CRLEntry(NativeCrypto.X509_REVOKED_dup(l2));
            }
            catch (OpenSSLX509CertificateFactory.ParsingException parsingException) {
                return null;
            }
        }
        return this.getRevokedCertificate(x509Certificate.getSerialNumber());
    }

    @Override
    public final Set<? extends X509CRLEntry> getRevokedCertificates() {
        long[] lArray = NativeCrypto.X509_CRL_get_REVOKED(this.mContext, this);
        if (lArray == null || lArray.length == 0) {
            return null;
        }
        HashSet<OpenSSLX509CRLEntry> hashSet = new HashSet<OpenSSLX509CRLEntry>();
        for (long l2 : lArray) {
            try {
                hashSet.add(new OpenSSLX509CRLEntry(l2));
            }
            catch (OpenSSLX509CertificateFactory.ParsingException parsingException) {}
        }
        return hashSet;
    }

    @Override
    public final byte[] getTBSCertList() throws CRLException {
        return NativeCrypto.get_X509_CRL_crl_enc(this.mContext, this);
    }

    @Override
    public final byte[] getSignature() {
        return NativeCrypto.get_X509_CRL_signature(this.mContext, this);
    }

    @Override
    public final String getSigAlgName() {
        String string = this.getSigAlgOID();
        String string2 = OidData.oidToAlgorithmName(string);
        if (string2 != null) {
            return string2;
        }
        string2 = Platform.oidToAlgorithmName(string);
        if (string2 != null) {
            return string2;
        }
        return string;
    }

    @Override
    public final String getSigAlgOID() {
        return NativeCrypto.get_X509_CRL_sig_alg_oid(this.mContext, this);
    }

    @Override
    public final byte[] getSigAlgParams() {
        return NativeCrypto.get_X509_CRL_sig_alg_parameter(this.mContext, this);
    }

    @Override
    public final boolean isRevoked(Certificate certificate) {
        if (!(certificate instanceof X509Certificate)) {
            return false;
        }
        if (certificate instanceof OpenSSLX509Certificate) {
            certificate = (OpenSSLX509Certificate)certificate;
        } else {
            try {
                certificate = OpenSSLX509Certificate.fromX509DerInputStream(new ByteArrayInputStream(certificate.getEncoded()));
            }
            catch (Exception exception) {
                throw new RuntimeException("cannot convert certificate", exception);
            }
        }
        long l2 = NativeCrypto.X509_CRL_get0_by_cert(this.mContext, this, ((OpenSSLX509Certificate)certificate).getContext(), (OpenSSLX509Certificate)certificate);
        return l2 != 0L;
    }

    @Override
    public final String toString() {
        Object object = new ByteArrayOutputStream();
        long l2 = NativeCrypto.create_BIO_OutputStream((OutputStream)object);
        try {
            NativeCrypto.X509_CRL_print(l2, this.mContext, this);
            object = ((ByteArrayOutputStream)object).toString();
            return object;
        }
        finally {
            NativeCrypto.BIO_free_all(l2);
        }
    }

    protected final void finalize() throws Throwable {
        try {
            if (this.mContext != 0L) {
                NativeCrypto.X509_CRL_free(this.mContext, this);
            }
            return;
        }
        finally {
            super.finalize();
        }
    }
}


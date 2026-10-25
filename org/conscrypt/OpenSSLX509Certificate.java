/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Principal;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import javax.crypto.BadPaddingException;
import javax.security.auth.x500.X500Principal;
import org.conscrypt.NativeCrypto;
import org.conscrypt.OidData;
import org.conscrypt.OpenSSLBIOInputStream;
import org.conscrypt.OpenSSLKey;
import org.conscrypt.OpenSSLKeyHolder;
import org.conscrypt.OpenSSLProvider;
import org.conscrypt.OpenSSLX509CertificateFactory;
import org.conscrypt.Platform;
import org.conscrypt.X509PublicKey;

public final class OpenSSLX509Certificate
extends X509Certificate {
    private static final long serialVersionUID = 1992239142393372128L;
    private final transient long mContext;
    private transient Integer mHashCode;
    private final Date notBefore;
    private final Date notAfter;

    OpenSSLX509Certificate(long l2) throws OpenSSLX509CertificateFactory.ParsingException {
        this.mContext = l2;
        this.notBefore = OpenSSLX509Certificate.toDate(NativeCrypto.X509_get_notBefore(this.mContext, this));
        this.notAfter = OpenSSLX509Certificate.toDate(NativeCrypto.X509_get_notAfter(this.mContext, this));
    }

    private OpenSSLX509Certificate(long l2, Date date, Date date2) {
        this.mContext = l2;
        this.notBefore = date;
        this.notAfter = date2;
    }

    private static Date toDate(long l2) throws OpenSSLX509CertificateFactory.ParsingException {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.set(14, 0);
        NativeCrypto.ASN1_TIME_to_Calendar(l2, calendar);
        return calendar.getTime();
    }

    public static OpenSSLX509Certificate fromX509DerInputStream(InputStream inputStream2) throws OpenSSLX509CertificateFactory.ParsingException {
        inputStream2 = new OpenSSLBIOInputStream(inputStream2, true);
        try {
            long l2 = NativeCrypto.d2i_X509_bio(((OpenSSLBIOInputStream)inputStream2).getBioContext());
            if (l2 == 0L) {
                return null;
            }
            OpenSSLX509Certificate openSSLX509Certificate = new OpenSSLX509Certificate(l2);
            return openSSLX509Certificate;
        }
        catch (Exception exception) {
            throw new OpenSSLX509CertificateFactory.ParsingException(exception);
        }
        finally {
            ((OpenSSLBIOInputStream)inputStream2).release();
        }
    }

    public static OpenSSLX509Certificate fromX509Der(byte[] byArray) throws CertificateEncodingException {
        try {
            return new OpenSSLX509Certificate(NativeCrypto.d2i_X509(byArray));
        }
        catch (OpenSSLX509CertificateFactory.ParsingException parsingException) {
            throw new CertificateEncodingException(parsingException);
        }
    }

    public static List<OpenSSLX509Certificate> fromPkcs7DerInputStream(InputStream inputStream2) throws OpenSSLX509CertificateFactory.ParsingException {
        long[] lArray;
        inputStream2 = new OpenSSLBIOInputStream(inputStream2, true);
        try {
            lArray = NativeCrypto.d2i_PKCS7_bio(((OpenSSLBIOInputStream)inputStream2).getBioContext(), 1);
        }
        catch (Exception exception) {
            throw new OpenSSLX509CertificateFactory.ParsingException(exception);
        }
        finally {
            ((OpenSSLBIOInputStream)inputStream2).release();
        }
        if (lArray == null) {
            return new ArrayList<OpenSSLX509Certificate>();
        }
        ArrayList<OpenSSLX509Certificate> arrayList = new ArrayList<OpenSSLX509Certificate>(lArray.length);
        for (int i2 = 0; i2 < lArray.length; ++i2) {
            if (lArray[i2] == 0L) continue;
            arrayList.add(new OpenSSLX509Certificate(lArray[i2]));
        }
        return arrayList;
    }

    public static OpenSSLX509Certificate fromX509PemInputStream(InputStream inputStream2) throws OpenSSLX509CertificateFactory.ParsingException {
        inputStream2 = new OpenSSLBIOInputStream(inputStream2, true);
        try {
            long l2 = NativeCrypto.PEM_read_bio_X509(((OpenSSLBIOInputStream)inputStream2).getBioContext());
            if (l2 == 0L) {
                return null;
            }
            OpenSSLX509Certificate openSSLX509Certificate = new OpenSSLX509Certificate(l2);
            return openSSLX509Certificate;
        }
        catch (Exception exception) {
            throw new OpenSSLX509CertificateFactory.ParsingException(exception);
        }
        finally {
            ((OpenSSLBIOInputStream)inputStream2).release();
        }
    }

    public static List<OpenSSLX509Certificate> fromPkcs7PemInputStream(InputStream inputStream2) throws OpenSSLX509CertificateFactory.ParsingException {
        long[] lArray;
        inputStream2 = new OpenSSLBIOInputStream(inputStream2, true);
        try {
            lArray = NativeCrypto.PEM_read_bio_PKCS7(((OpenSSLBIOInputStream)inputStream2).getBioContext(), 1);
        }
        catch (Exception exception) {
            throw new OpenSSLX509CertificateFactory.ParsingException(exception);
        }
        finally {
            ((OpenSSLBIOInputStream)inputStream2).release();
        }
        ArrayList<OpenSSLX509Certificate> arrayList = new ArrayList<OpenSSLX509Certificate>(lArray.length);
        for (int i2 = 0; i2 < lArray.length; ++i2) {
            if (lArray[i2] == 0L) continue;
            arrayList.add(new OpenSSLX509Certificate(lArray[i2]));
        }
        return arrayList;
    }

    public static OpenSSLX509Certificate fromCertificate(Certificate certificate) throws CertificateEncodingException {
        if (certificate instanceof OpenSSLX509Certificate) {
            return (OpenSSLX509Certificate)certificate;
        }
        if (certificate instanceof X509Certificate) {
            return OpenSSLX509Certificate.fromX509Der(certificate.getEncoded());
        }
        throw new CertificateEncodingException("Only X.509 certificates are supported");
    }

    @Override
    public final Set<String> getCriticalExtensionOIDs() {
        String[] stringArray = NativeCrypto.get_X509_ext_oids(this.mContext, this, 1);
        if (stringArray.length == 0 && NativeCrypto.get_X509_ext_oids(this.mContext, this, 0).length == 0) {
            return null;
        }
        return new HashSet<String>(Arrays.asList(stringArray));
    }

    @Override
    public final byte[] getExtensionValue(String string) {
        return NativeCrypto.X509_get_ext_oid(this.mContext, this, string);
    }

    @Override
    public final Set<String> getNonCriticalExtensionOIDs() {
        String[] stringArray = NativeCrypto.get_X509_ext_oids(this.mContext, this, 0);
        if (stringArray.length == 0 && NativeCrypto.get_X509_ext_oids(this.mContext, this, 1).length == 0) {
            return null;
        }
        return new HashSet<String>(Arrays.asList(stringArray));
    }

    @Override
    public final boolean hasUnsupportedCriticalExtension() {
        return (NativeCrypto.get_X509_ex_flags(this.mContext, this) & 0x200) != 0;
    }

    @Override
    public final void checkValidity() throws CertificateExpiredException, CertificateNotYetValidException {
        this.checkValidity(new Date());
    }

    @Override
    public final void checkValidity(Date date) throws CertificateExpiredException, CertificateNotYetValidException {
        if (this.getNotBefore().compareTo(date) > 0) {
            throw new CertificateNotYetValidException("Certificate not valid until " + this.getNotBefore().toString() + " (compared to " + date.toString() + ")");
        }
        if (this.getNotAfter().compareTo(date) < 0) {
            throw new CertificateExpiredException("Certificate expired at " + this.getNotAfter().toString() + " (compared to " + date.toString() + ")");
        }
    }

    @Override
    public final int getVersion() {
        return (int)NativeCrypto.X509_get_version(this.mContext, this) + 1;
    }

    @Override
    public final BigInteger getSerialNumber() {
        return new BigInteger(NativeCrypto.X509_get_serialNumber(this.mContext, this));
    }

    @Override
    public final Principal getIssuerDN() {
        return this.getIssuerX500Principal();
    }

    @Override
    public final Principal getSubjectDN() {
        return this.getSubjectX500Principal();
    }

    @Override
    public final Date getNotBefore() {
        return (Date)this.notBefore.clone();
    }

    @Override
    public final Date getNotAfter() {
        return (Date)this.notAfter.clone();
    }

    @Override
    public final byte[] getTBSCertificate() throws CertificateEncodingException {
        return NativeCrypto.get_X509_cert_info_enc(this.mContext, this);
    }

    @Override
    public final byte[] getSignature() {
        return NativeCrypto.get_X509_signature(this.mContext, this);
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
        return NativeCrypto.get_X509_sig_alg_oid(this.mContext, this);
    }

    @Override
    public final byte[] getSigAlgParams() {
        return NativeCrypto.get_X509_sig_alg_parameter(this.mContext, this);
    }

    @Override
    public final boolean[] getIssuerUniqueID() {
        return NativeCrypto.get_X509_issuerUID(this.mContext, this);
    }

    @Override
    public final boolean[] getSubjectUniqueID() {
        return NativeCrypto.get_X509_subjectUID(this.mContext, this);
    }

    @Override
    public final boolean[] getKeyUsage() {
        boolean[] blArray = NativeCrypto.get_X509_ex_kusage(this.mContext, this);
        if (blArray == null) {
            return null;
        }
        if (blArray.length >= 9) {
            return blArray;
        }
        boolean[] blArray2 = new boolean[9];
        System.arraycopy(blArray, 0, blArray2, 0, blArray.length);
        return blArray2;
    }

    @Override
    public final int getBasicConstraints() {
        if ((NativeCrypto.get_X509_ex_flags(this.mContext, this) & 0x10) == 0) {
            return -1;
        }
        int n2 = NativeCrypto.get_X509_ex_pathlen(this.mContext, this);
        if (n2 == -1) {
            return Integer.MAX_VALUE;
        }
        return n2;
    }

    @Override
    public final byte[] getEncoded() throws CertificateEncodingException {
        return NativeCrypto.i2d_X509(this.mContext, this);
    }

    private void verifyOpenSSL(OpenSSLKey openSSLKey) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        try {
            NativeCrypto.X509_verify(this.mContext, this, openSSLKey.getNativeRef());
            return;
        }
        catch (RuntimeException runtimeException) {
            throw new CertificateException(runtimeException);
        }
        catch (BadPaddingException badPaddingException) {
            throw new SignatureException();
        }
    }

    private void verifyInternal(PublicKey publicKey, String object) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
        object = object == null ? Signature.getInstance(this.getSigAlgName()) : Signature.getInstance(this.getSigAlgName(), (String)object);
        ((Signature)object).initVerify(publicKey);
        ((Signature)object).update(this.getTBSCertificate());
        if (!((Signature)object).verify(this.getSignature())) {
            throw new SignatureException("signature did not verify");
        }
    }

    @Override
    public final void verify(PublicKey object) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
        if (object instanceof OpenSSLKeyHolder) {
            object = ((OpenSSLKeyHolder)object).getOpenSSLKey();
            this.verifyOpenSSL((OpenSSLKey)object);
            return;
        }
        this.verifyInternal((PublicKey)object, null);
    }

    @Override
    public final void verify(PublicKey publicKey, String string) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
        this.verifyInternal(publicKey, string);
    }

    @Override
    public final void verify(PublicKey publicKey, Provider object) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        if (publicKey instanceof OpenSSLKeyHolder && object instanceof OpenSSLProvider) {
            object = ((OpenSSLKeyHolder)((Object)publicKey)).getOpenSSLKey();
            this.verifyOpenSSL((OpenSSLKey)object);
            return;
        }
        object = object == null ? Signature.getInstance(this.getSigAlgName()) : Signature.getInstance(this.getSigAlgName(), (Provider)object);
        ((Signature)object).initVerify(publicKey);
        ((Signature)object).update(this.getTBSCertificate());
        if (!((Signature)object).verify(this.getSignature())) {
            throw new SignatureException("signature did not verify");
        }
    }

    @Override
    public final String toString() {
        Object object = new ByteArrayOutputStream();
        long l2 = NativeCrypto.create_BIO_OutputStream((OutputStream)object);
        try {
            NativeCrypto.X509_print_ex(l2, this.mContext, this, 0L, 0L);
            object = ((ByteArrayOutputStream)object).toString();
            return object;
        }
        finally {
            NativeCrypto.BIO_free_all(l2);
        }
    }

    @Override
    public final PublicKey getPublicKey() {
        try {
            OpenSSLKey openSSLKey = new OpenSSLKey(NativeCrypto.X509_get_pubkey(this.mContext, this));
            return openSSLKey.getPublicKey();
        }
        catch (InvalidKeyException | NoSuchAlgorithmException generalSecurityException) {
            String string = NativeCrypto.get_X509_pubkey_oid(this.mContext, this);
            byte[] byArray = NativeCrypto.i2d_X509_PUBKEY(this.mContext, this);
            try {
                KeyFactory keyFactory = KeyFactory.getInstance(string);
                return keyFactory.generatePublic(new X509EncodedKeySpec(byArray));
            }
            catch (NoSuchAlgorithmException | InvalidKeySpecException generalSecurityException2) {
                return new X509PublicKey(string, byArray);
            }
        }
    }

    @Override
    public final X500Principal getIssuerX500Principal() {
        byte[] byArray = NativeCrypto.X509_get_issuer_name(this.mContext, this);
        return new X500Principal(byArray);
    }

    @Override
    public final X500Principal getSubjectX500Principal() {
        byte[] byArray = NativeCrypto.X509_get_subject_name(this.mContext, this);
        return new X500Principal(byArray);
    }

    @Override
    public final List<String> getExtendedKeyUsage() throws CertificateParsingException {
        String[] stringArray = NativeCrypto.get_X509_ex_xkusage(this.mContext, this);
        if (stringArray == null) {
            return null;
        }
        return Arrays.asList(stringArray);
    }

    private static Collection<List<?>> alternativeNameArrayToList(Object[][] objectArray) {
        if (objectArray == null) {
            return null;
        }
        ArrayList<List<Object>> arrayList = new ArrayList<List<Object>>(objectArray.length);
        for (int i2 = 0; i2 < objectArray.length; ++i2) {
            arrayList.add(Collections.unmodifiableList(Arrays.asList(objectArray[i2])));
        }
        return Collections.unmodifiableCollection(arrayList);
    }

    @Override
    public final Collection<List<?>> getSubjectAlternativeNames() throws CertificateParsingException {
        return OpenSSLX509Certificate.alternativeNameArrayToList(NativeCrypto.get_X509_GENERAL_NAME_stack(this.mContext, this, 1));
    }

    @Override
    public final Collection<List<?>> getIssuerAlternativeNames() throws CertificateParsingException {
        return OpenSSLX509Certificate.alternativeNameArrayToList(NativeCrypto.get_X509_GENERAL_NAME_stack(this.mContext, this, 2));
    }

    @Override
    public final boolean equals(Object object) {
        if (object instanceof OpenSSLX509Certificate) {
            return NativeCrypto.X509_cmp(this.mContext, this, ((OpenSSLX509Certificate)object).mContext, (OpenSSLX509Certificate)(object = (OpenSSLX509Certificate)object)) == 0;
        }
        return super.equals(object);
    }

    @Override
    public final int hashCode() {
        if (this.mHashCode != null) {
            return this.mHashCode;
        }
        this.mHashCode = super.hashCode();
        return this.mHashCode;
    }

    public final long getContext() {
        return this.mContext;
    }

    public final OpenSSLX509Certificate withDeletedExtension(String string) {
        OpenSSLX509Certificate openSSLX509Certificate = new OpenSSLX509Certificate(NativeCrypto.X509_dup(this.mContext, this), this.notBefore, this.notAfter);
        NativeCrypto.X509_delete_ext(openSSLX509Certificate.getContext(), openSSLX509Certificate, string);
        return openSSLX509Certificate;
    }

    protected final void finalize() throws Throwable {
        try {
            if (this.mContext != 0L) {
                NativeCrypto.X509_free(this.mContext, this);
            }
            return;
        }
        finally {
            super.finalize();
        }
    }
}


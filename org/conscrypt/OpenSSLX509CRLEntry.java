/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.cert.CRLException;
import java.security.cert.X509CRLEntry;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.conscrypt.NativeCrypto;
import org.conscrypt.OpenSSLX509CRL;
import org.conscrypt.OpenSSLX509CertificateFactory;

final class OpenSSLX509CRLEntry
extends X509CRLEntry {
    private final long mContext;
    private final Date revocationDate;

    OpenSSLX509CRLEntry(long l2) throws OpenSSLX509CertificateFactory.ParsingException {
        this.mContext = l2;
        this.revocationDate = OpenSSLX509CRL.toDate(NativeCrypto.get_X509_REVOKED_revocationDate(this.mContext));
    }

    @Override
    public final Set<String> getCriticalExtensionOIDs() {
        String[] stringArray = NativeCrypto.get_X509_REVOKED_ext_oids(this.mContext, 1);
        if (stringArray.length == 0 && NativeCrypto.get_X509_REVOKED_ext_oids(this.mContext, 0).length == 0) {
            return null;
        }
        return new HashSet<String>(Arrays.asList(stringArray));
    }

    @Override
    public final byte[] getExtensionValue(String string) {
        return NativeCrypto.X509_REVOKED_get_ext_oid(this.mContext, string);
    }

    @Override
    public final Set<String> getNonCriticalExtensionOIDs() {
        String[] stringArray = NativeCrypto.get_X509_REVOKED_ext_oids(this.mContext, 0);
        if (stringArray.length == 0 && NativeCrypto.get_X509_REVOKED_ext_oids(this.mContext, 1).length == 0) {
            return null;
        }
        return new HashSet<String>(Arrays.asList(stringArray));
    }

    @Override
    public final boolean hasUnsupportedCriticalExtension() {
        String[] stringArray = NativeCrypto.get_X509_REVOKED_ext_oids(this.mContext, 1);
        String[] stringArray2 = stringArray;
        stringArray2 = stringArray;
        int n2 = stringArray.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            String string = stringArray2[i2];
            long l2 = NativeCrypto.X509_REVOKED_get_ext(this.mContext, string);
            if (NativeCrypto.X509_supported_extension(l2) == 1) continue;
            return true;
        }
        return false;
    }

    @Override
    public final byte[] getEncoded() throws CRLException {
        return NativeCrypto.i2d_X509_REVOKED(this.mContext);
    }

    @Override
    public final BigInteger getSerialNumber() {
        return new BigInteger(NativeCrypto.X509_REVOKED_get_serialNumber(this.mContext));
    }

    @Override
    public final Date getRevocationDate() {
        return (Date)this.revocationDate.clone();
    }

    @Override
    public final boolean hasExtensions() {
        return NativeCrypto.get_X509_REVOKED_ext_oids(this.mContext, 0).length != 0 || NativeCrypto.get_X509_REVOKED_ext_oids(this.mContext, 1).length != 0;
    }

    @Override
    public final String toString() {
        Object object = new ByteArrayOutputStream();
        long l2 = NativeCrypto.create_BIO_OutputStream((OutputStream)object);
        try {
            NativeCrypto.X509_REVOKED_print(l2, this.mContext);
            object = ((ByteArrayOutputStream)object).toString();
            return object;
        }
        finally {
            NativeCrypto.BIO_free_all(l2);
        }
    }
}


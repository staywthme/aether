/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt.ct;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Arrays;
import org.conscrypt.ct.CertificateEntry;
import org.conscrypt.ct.SerializationException;
import org.conscrypt.ct.SignedCertificateTimestamp;
import org.conscrypt.ct.VerifiedSCT;

public class CTLogInfo {
    private final byte[] logId;
    private final PublicKey publicKey;
    private final String description;
    private final String url;

    public CTLogInfo(PublicKey publicKey, String string, String string2) {
        try {
            this.logId = MessageDigest.getInstance("SHA-256").digest(publicKey.getEncoded());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new RuntimeException(noSuchAlgorithmException);
        }
        this.publicKey = publicKey;
        this.description = string;
        this.url = string2;
    }

    public byte[] getID() {
        return this.logId;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    public String getDescription() {
        return this.description;
    }

    public String getUrl() {
        return this.url;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof CTLogInfo)) {
            return false;
        }
        object = (CTLogInfo)object;
        return this.publicKey.equals(((CTLogInfo)object).publicKey) && this.description.equals(((CTLogInfo)object).description) && this.url.equals(((CTLogInfo)object).url);
    }

    public int hashCode() {
        int n2 = 31 + this.publicKey.hashCode();
        n2 = n2 * 31 + this.description.hashCode();
        n2 = n2 * 31 + this.url.hashCode();
        return n2;
    }

    public VerifiedSCT.Status verifySingleSCT(SignedCertificateTimestamp signedCertificateTimestamp, CertificateEntry object) {
        Object object2;
        if (!Arrays.equals(signedCertificateTimestamp.getLogID(), this.getID())) {
            return VerifiedSCT.Status.UNKNOWN_LOG;
        }
        try {
            object = signedCertificateTimestamp.encodeTBS((CertificateEntry)object);
        }
        catch (SerializationException serializationException) {
            return VerifiedSCT.Status.INVALID_SCT;
        }
        try {
            object2 = signedCertificateTimestamp.getSignature().getAlgorithm();
            object2 = Signature.getInstance((String)object2);
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return VerifiedSCT.Status.INVALID_SCT;
        }
        try {
            ((Signature)object2).initVerify(this.publicKey);
        }
        catch (InvalidKeyException invalidKeyException) {
            return VerifiedSCT.Status.INVALID_SCT;
        }
        try {
            ((Signature)object2).update((byte[])object);
            if (!((Signature)object2).verify(signedCertificateTimestamp.getSignature().getSignature())) {
                return VerifiedSCT.Status.INVALID_SIGNATURE;
            }
            return VerifiedSCT.Status.VALID;
        }
        catch (SignatureException signatureException) {
            throw new RuntimeException(signatureException);
        }
    }
}


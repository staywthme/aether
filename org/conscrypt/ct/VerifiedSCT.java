/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt.ct;

import org.conscrypt.ct.SignedCertificateTimestamp;

public final class VerifiedSCT {
    public final SignedCertificateTimestamp sct;
    public final Status status;

    public VerifiedSCT(SignedCertificateTimestamp signedCertificateTimestamp, Status status) {
        this.sct = signedCertificateTimestamp;
        this.status = status;
    }

    public static enum Status {
        VALID,
        INVALID_SIGNATURE,
        UNKNOWN_LOG,
        INVALID_SCT;

    }
}


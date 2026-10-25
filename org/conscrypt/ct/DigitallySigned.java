/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt.ct;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.conscrypt.ct.Serialization;
import org.conscrypt.ct.SerializationException;

public class DigitallySigned {
    private final HashAlgorithm hashAlgorithm;
    private final SignatureAlgorithm signatureAlgorithm;
    private final byte[] signature;

    public DigitallySigned(HashAlgorithm hashAlgorithm, SignatureAlgorithm signatureAlgorithm, byte[] byArray) {
        this.hashAlgorithm = hashAlgorithm;
        this.signatureAlgorithm = signatureAlgorithm;
        this.signature = byArray;
    }

    public DigitallySigned(int n2, int n3, byte[] byArray) {
        this(HashAlgorithm.valueOf(n2), SignatureAlgorithm.valueOf(n3), byArray);
    }

    public HashAlgorithm getHashAlgorithm() {
        return this.hashAlgorithm;
    }

    public SignatureAlgorithm getSignatureAlgorithm() {
        return this.signatureAlgorithm;
    }

    public byte[] getSignature() {
        return this.signature;
    }

    public String getAlgorithm() {
        return String.format("%swith%s", new Object[]{this.hashAlgorithm, this.signatureAlgorithm});
    }

    public static DigitallySigned decode(InputStream inputStream2) throws SerializationException {
        try {
            return new DigitallySigned(Serialization.readNumber(inputStream2, 1), Serialization.readNumber(inputStream2, 1), Serialization.readVariableBytes(inputStream2, 2));
        }
        catch (IllegalArgumentException illegalArgumentException) {
            throw new SerializationException(illegalArgumentException);
        }
    }

    public static DigitallySigned decode(byte[] byArray) throws SerializationException {
        return DigitallySigned.decode(new ByteArrayInputStream(byArray));
    }

    public static enum SignatureAlgorithm {
        ANONYMOUS,
        RSA,
        DSA,
        ECDSA;

        private static SignatureAlgorithm[] values;

        public static SignatureAlgorithm valueOf(int n2) {
            try {
                return values[n2];
            }
            catch (IndexOutOfBoundsException indexOutOfBoundsException) {
                throw new IllegalArgumentException("Invalid signature algorithm " + n2, indexOutOfBoundsException);
            }
        }

        static {
            values = SignatureAlgorithm.values();
        }
    }

    public static enum HashAlgorithm {
        NONE,
        MD5,
        SHA1,
        SHA224,
        SHA256,
        SHA384,
        SHA512;

        private static HashAlgorithm[] values;

        public static HashAlgorithm valueOf(int n2) {
            try {
                return values[n2];
            }
            catch (IndexOutOfBoundsException indexOutOfBoundsException) {
                throw new IllegalArgumentException("Invalid hash algorithm " + n2, indexOutOfBoundsException);
            }
        }

        static {
            values = HashAlgorithm.values();
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.io.IOException;
import java.security.AlgorithmParametersSpi;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import org.conscrypt.NativeCrypto;
import org.conscrypt.Platform;

public final class GCMParameters
extends AlgorithmParametersSpi {
    private static final int DEFAULT_TLEN = 96;
    private int tLen;
    private byte[] iv;

    public GCMParameters() {
    }

    GCMParameters(int n2, byte[] byArray) {
        this.tLen = n2;
        this.iv = byArray;
    }

    final int getTLen() {
        return this.tLen;
    }

    final byte[] getIV() {
        return this.iv;
    }

    @Override
    protected final void engineInit(AlgorithmParameterSpec object) throws InvalidParameterSpecException {
        if ((object = Platform.fromGCMParameterSpec((AlgorithmParameterSpec)object)) == null) {
            throw new InvalidParameterSpecException("Only GCMParameterSpec is supported");
        }
        this.tLen = ((GCMParameters)object).tLen;
        this.iv = ((GCMParameters)object).iv;
    }

    @Override
    protected final void engineInit(byte[] byArray) throws IOException {
        long l2 = 0L;
        long l3 = 0L;
        try {
            l2 = NativeCrypto.asn1_read_init(byArray);
            l3 = NativeCrypto.asn1_read_sequence(l2);
            byArray = NativeCrypto.asn1_read_octetstring(l3);
            int n2 = 96;
            if (!NativeCrypto.asn1_read_is_empty(l3)) {
                n2 = 8 * (int)NativeCrypto.asn1_read_uint64(l3);
            }
            if (!NativeCrypto.asn1_read_is_empty(l3) || !NativeCrypto.asn1_read_is_empty(l2)) {
                throw new IOException("Error reading ASN.1 encoding");
            }
            this.iv = byArray;
            this.tLen = n2;
            return;
        }
        finally {
            NativeCrypto.asn1_read_free(l3);
            NativeCrypto.asn1_read_free(l2);
        }
    }

    @Override
    protected final void engineInit(byte[] byArray, String string) throws IOException {
        if (string == null || string.equals("ASN.1")) {
            this.engineInit(byArray);
            return;
        }
        throw new IOException("Unsupported format: " + string);
    }

    @Override
    protected final <T extends AlgorithmParameterSpec> T engineGetParameterSpec(Class<T> clazz) throws InvalidParameterSpecException {
        if (clazz != null && clazz.getName().equals("javax.crypto.spec.GCMParameterSpec")) {
            return (T)((AlgorithmParameterSpec)clazz.cast(Platform.toGCMParameterSpec(this.tLen, this.iv)));
        }
        throw new InvalidParameterSpecException("Unsupported class: " + clazz);
    }

    @Override
    protected final byte[] engineGetEncoded() throws IOException {
        long l2 = 0L;
        long l3 = 0L;
        try {
            l2 = NativeCrypto.asn1_write_init();
            l3 = NativeCrypto.asn1_write_sequence(l2);
            NativeCrypto.asn1_write_octetstring(l3, this.iv);
            if (this.tLen != 96) {
                NativeCrypto.asn1_write_uint64(l3, this.tLen / 8);
            }
            byte[] byArray = NativeCrypto.asn1_write_finish(l2);
            return byArray;
        }
        catch (IOException iOException) {
            NativeCrypto.asn1_write_cleanup(l2);
            throw iOException;
        }
        finally {
            NativeCrypto.asn1_write_free(l3);
            NativeCrypto.asn1_write_free(l2);
        }
    }

    @Override
    protected final byte[] engineGetEncoded(String string) throws IOException {
        if (string == null || string.equals("ASN.1")) {
            return this.engineGetEncoded();
        }
        throw new IOException("Unsupported format: " + string);
    }

    @Override
    protected final String engineToString() {
        return "Conscrypt GCM AlgorithmParameters";
    }
}


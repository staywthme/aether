/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.io.IOException;
import java.security.AlgorithmParametersSpi;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;
import org.conscrypt.NativeCrypto;
import org.conscrypt.OAEPParameters;

public class PSSParameters
extends AlgorithmParametersSpi {
    private PSSParameterSpec spec = PSSParameterSpec.DEFAULT;

    @Override
    protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidParameterSpecException {
        if (algorithmParameterSpec instanceof PSSParameterSpec) {
            this.spec = (PSSParameterSpec)algorithmParameterSpec;
            return;
        }
        throw new InvalidParameterSpecException("Only PSSParameterSpec is supported");
    }

    @Override
    protected void engineInit(byte[] byArray) throws IOException {
        long l2 = 0L;
        long l3 = 0L;
        try {
            long l4;
            l2 = NativeCrypto.asn1_read_init(byArray);
            l3 = NativeCrypto.asn1_read_sequence(l2);
            int n2 = 20;
            String string = OAEPParameters.readHash(l3);
            String string2 = OAEPParameters.readMgfHash(l3);
            if (NativeCrypto.asn1_read_next_tag_is(l3, 2)) {
                l4 = 0L;
                try {
                    l4 = NativeCrypto.asn1_read_tagged(l3);
                    n2 = (int)NativeCrypto.asn1_read_uint64(l4);
                }
                finally {
                    NativeCrypto.asn1_read_free(l4);
                }
            }
            if (NativeCrypto.asn1_read_next_tag_is(l3, 3)) {
                long l5 = 0L;
                try {
                    l5 = NativeCrypto.asn1_read_tagged(l3);
                    l4 = (int)NativeCrypto.asn1_read_uint64(l5);
                }
                finally {
                    NativeCrypto.asn1_read_free(l5);
                }
                if (l4 != 1L) {
                    throw new IOException("Error reading ASN.1 encoding");
                }
            }
            if (!NativeCrypto.asn1_read_is_empty(l3) || !NativeCrypto.asn1_read_is_empty(l2)) {
                throw new IOException("Error reading ASN.1 encoding");
            }
            this.spec = new PSSParameterSpec(string, "MGF1", new MGF1ParameterSpec(string2), n2, 1);
            return;
        }
        finally {
            NativeCrypto.asn1_read_free(l3);
            NativeCrypto.asn1_read_free(l2);
        }
    }

    @Override
    protected void engineInit(byte[] byArray, String string) throws IOException {
        if (string == null || string.equals("ASN.1") || string.equals("X.509")) {
            this.engineInit(byArray);
            return;
        }
        throw new IOException("Unsupported format: " + string);
    }

    @Override
    protected <T extends AlgorithmParameterSpec> T engineGetParameterSpec(Class<T> clazz) throws InvalidParameterSpecException {
        if (clazz != null && clazz == PSSParameterSpec.class) {
            return (T)this.spec;
        }
        throw new InvalidParameterSpecException("Unsupported class: " + clazz);
    }

    @Override
    protected byte[] engineGetEncoded() throws IOException {
        long l2 = 0L;
        long l3 = 0L;
        try {
            l2 = NativeCrypto.asn1_write_init();
            l3 = NativeCrypto.asn1_write_sequence(l2);
            OAEPParameters.writeHashAndMgfHash(l3, this.spec.getDigestAlgorithm(), (MGF1ParameterSpec)this.spec.getMGFParameters());
            if (this.spec.getSaltLength() != 20) {
                long l4 = 0L;
                try {
                    l4 = NativeCrypto.asn1_write_tag(l3, 2);
                    NativeCrypto.asn1_write_uint64(l4, this.spec.getSaltLength());
                }
                finally {
                    NativeCrypto.asn1_write_flush(l3);
                    NativeCrypto.asn1_write_free(l4);
                }
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
    protected byte[] engineGetEncoded(String string) throws IOException {
        if (string == null || string.equals("ASN.1") || string.equals("X.509")) {
            return this.engineGetEncoded();
        }
        throw new IOException("Unsupported format: " + string);
    }

    @Override
    protected String engineToString() {
        return "Conscrypt PSS AlgorithmParameters";
    }
}


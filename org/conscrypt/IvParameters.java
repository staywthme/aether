/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.io.IOException;
import java.security.AlgorithmParametersSpi;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import javax.crypto.spec.IvParameterSpec;
import org.conscrypt.NativeCrypto;

public class IvParameters
extends AlgorithmParametersSpi {
    private byte[] iv;

    @Override
    protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidParameterSpecException {
        if (!(algorithmParameterSpec instanceof IvParameterSpec)) {
            throw new InvalidParameterSpecException("Only IvParameterSpec is supported");
        }
        this.iv = (byte[])((IvParameterSpec)algorithmParameterSpec).getIV().clone();
    }

    @Override
    protected void engineInit(byte[] byArray) throws IOException {
        long l2 = 0L;
        try {
            l2 = NativeCrypto.asn1_read_init(byArray);
            byArray = NativeCrypto.asn1_read_octetstring(l2);
            if (!NativeCrypto.asn1_read_is_empty(l2)) {
                throw new IOException("Error reading ASN.1 encoding");
            }
            this.iv = byArray;
            return;
        }
        finally {
            NativeCrypto.asn1_read_free(l2);
        }
    }

    @Override
    protected void engineInit(byte[] byArray, String string) throws IOException {
        if (string == null || string.equals("ASN.1")) {
            this.engineInit(byArray);
            return;
        }
        if (string.equals("RAW")) {
            this.iv = (byte[])byArray.clone();
            return;
        }
        throw new IOException("Unsupported format: " + string);
    }

    @Override
    protected <T extends AlgorithmParameterSpec> T engineGetParameterSpec(Class<T> clazz) throws InvalidParameterSpecException {
        if (clazz != IvParameterSpec.class) {
            throw new InvalidParameterSpecException("Incompatible AlgorithmParametersSpec class: " + clazz);
        }
        return (T)new IvParameterSpec(this.iv);
    }

    @Override
    protected byte[] engineGetEncoded() throws IOException {
        long l2 = 0L;
        try {
            l2 = NativeCrypto.asn1_write_init();
            NativeCrypto.asn1_write_octetstring(l2, this.iv);
            byte[] byArray = NativeCrypto.asn1_write_finish(l2);
            return byArray;
        }
        catch (IOException iOException) {
            NativeCrypto.asn1_write_cleanup(l2);
            throw iOException;
        }
        finally {
            NativeCrypto.asn1_write_free(l2);
        }
    }

    @Override
    protected byte[] engineGetEncoded(String string) throws IOException {
        if (string == null || string.equals("ASN.1")) {
            return this.engineGetEncoded();
        }
        if (string.equals("RAW")) {
            return (byte[])this.iv.clone();
        }
        throw new IOException("Unsupported format: " + string);
    }

    @Override
    protected String engineToString() {
        return "Conscrypt IV AlgorithmParameters";
    }

    public static class ChaCha20
    extends IvParameters {
    }

    public static class DESEDE
    extends IvParameters {
    }

    public static class AES
    extends IvParameters {
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.io.IOException;
import java.security.AlgorithmParametersSpi;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.MGF1ParameterSpec;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import org.conscrypt.NativeCrypto;

public class OAEPParameters
extends AlgorithmParametersSpi {
    private static final Map<String, String> OID_TO_NAME = new HashMap<String, String>();
    private static final Map<String, String> NAME_TO_OID = new HashMap<String, String>();
    private static final String MGF1_OID = "1.2.840.113549.1.1.8";
    private static final String PSPECIFIED_OID = "1.2.840.113549.1.1.9";
    private OAEPParameterSpec spec = OAEPParameterSpec.DEFAULT;

    @Override
    protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidParameterSpecException {
        if (algorithmParameterSpec instanceof OAEPParameterSpec) {
            this.spec = (OAEPParameterSpec)algorithmParameterSpec;
            return;
        }
        throw new InvalidParameterSpecException("Only OAEPParameterSpec is supported");
    }

    @Override
    protected void engineInit(byte[] object) throws IOException {
        long l2 = 0L;
        long l3 = 0L;
        try {
            l2 = NativeCrypto.asn1_read_init(object);
            l3 = NativeCrypto.asn1_read_sequence(l2);
            object = PSource.PSpecified.DEFAULT;
            String string = OAEPParameters.readHash(l3);
            String string2 = OAEPParameters.readMgfHash(l3);
            if (NativeCrypto.asn1_read_next_tag_is(l3, 2)) {
                long l4 = 0L;
                long l5 = 0L;
                try {
                    l4 = NativeCrypto.asn1_read_tagged(l3);
                    l5 = NativeCrypto.asn1_read_sequence(l4);
                    String string3 = NativeCrypto.asn1_read_oid(l5);
                    object = string3;
                    if (!string3.equals(PSPECIFIED_OID)) {
                        throw new IOException("Error reading ASN.1 encoding");
                    }
                    object = new PSource.PSpecified(NativeCrypto.asn1_read_octetstring(l5));
                    if (!NativeCrypto.asn1_read_is_empty(l5)) {
                        throw new IOException("Error reading ASN.1 encoding");
                    }
                }
                finally {
                    NativeCrypto.asn1_read_free(l5);
                    NativeCrypto.asn1_read_free(l4);
                }
            }
            if (!NativeCrypto.asn1_read_is_empty(l3) || !NativeCrypto.asn1_read_is_empty(l2)) {
                throw new IOException("Error reading ASN.1 encoding");
            }
            this.spec = new OAEPParameterSpec(string, "MGF1", new MGF1ParameterSpec(string2), (PSource)object);
            return;
        }
        finally {
            NativeCrypto.asn1_read_free(l3);
            NativeCrypto.asn1_read_free(l2);
        }
    }

    @Override
    protected void engineInit(byte[] byArray, String string) throws IOException {
        if (string == null || string.equals("ASN.1")) {
            this.engineInit(byArray);
            return;
        }
        throw new IOException("Unsupported format: " + string);
    }

    static String readHash(long l2) throws IOException {
        if (NativeCrypto.asn1_read_next_tag_is(l2, 0)) {
            long l3 = 0L;
            try {
                l3 = NativeCrypto.asn1_read_tagged(l2);
                String string = OAEPParameters.getHashName(l3);
                return string;
            }
            finally {
                NativeCrypto.asn1_read_free(l3);
            }
        }
        return "SHA-1";
    }

    static String readMgfHash(long l2) throws IOException {
        if (NativeCrypto.asn1_read_next_tag_is(l2, 1)) {
            long l3 = 0L;
            long l4 = 0L;
            try {
                l3 = NativeCrypto.asn1_read_tagged(l2);
                l4 = NativeCrypto.asn1_read_sequence(l3);
                String string = NativeCrypto.asn1_read_oid(l4);
                if (!string.equals(MGF1_OID)) {
                    throw new IOException("Error reading ASN.1 encoding");
                }
                string = OAEPParameters.getHashName(l4);
                if (!NativeCrypto.asn1_read_is_empty(l4)) {
                    throw new IOException("Error reading ASN.1 encoding");
                }
                return string;
            }
            finally {
                NativeCrypto.asn1_read_free(l4);
                NativeCrypto.asn1_read_free(l3);
            }
        }
        return "SHA-1";
    }

    private static String getHashName(long l2) throws IOException {
        long l3 = 0L;
        try {
            l3 = NativeCrypto.asn1_read_sequence(l2);
            String string = NativeCrypto.asn1_read_oid(l3);
            if (!NativeCrypto.asn1_read_is_empty(l3)) {
                NativeCrypto.asn1_read_null(l3);
            }
            if (!NativeCrypto.asn1_read_is_empty(l3) || !OID_TO_NAME.containsKey(string)) {
                throw new IOException("Error reading ASN.1 encoding");
            }
            string = OID_TO_NAME.get(string);
            return string;
        }
        finally {
            NativeCrypto.asn1_read_free(l3);
        }
    }

    @Override
    protected <T extends AlgorithmParameterSpec> T engineGetParameterSpec(Class<T> clazz) throws InvalidParameterSpecException {
        if (clazz != null && clazz == OAEPParameterSpec.class) {
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
            PSource.PSpecified pSpecified = (PSource.PSpecified)this.spec.getPSource();
            if (pSpecified.getValue().length != 0) {
                long l4 = 0L;
                long l5 = 0L;
                try {
                    l4 = NativeCrypto.asn1_write_tag(l3, 2);
                    l5 = OAEPParameters.writeAlgorithmIdentifier(l4, PSPECIFIED_OID);
                    NativeCrypto.asn1_write_octetstring(l5, pSpecified.getValue());
                }
                finally {
                    NativeCrypto.asn1_write_flush(l3);
                    NativeCrypto.asn1_write_free(l5);
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
        if (string == null || string.equals("ASN.1")) {
            return this.engineGetEncoded();
        }
        throw new IOException("Unsupported format: " + string);
    }

    static void writeHashAndMgfHash(long l2, String string, MGF1ParameterSpec mGF1ParameterSpec) throws IOException {
        long l3;
        long l4;
        if (!string.equals("SHA-1")) {
            l4 = 0L;
            l3 = 0L;
            try {
                l4 = NativeCrypto.asn1_write_tag(l2, 0);
                l3 = OAEPParameters.writeAlgorithmIdentifier(l4, NAME_TO_OID.get(string));
                NativeCrypto.asn1_write_null(l3);
            }
            finally {
                NativeCrypto.asn1_write_flush(l2);
                NativeCrypto.asn1_write_free(l3);
                NativeCrypto.asn1_write_free(l4);
            }
        }
        if (!mGF1ParameterSpec.getDigestAlgorithm().equals("SHA-1")) {
            l4 = 0L;
            l3 = 0L;
            long l5 = 0L;
            try {
                l4 = NativeCrypto.asn1_write_tag(l2, 1);
                l3 = OAEPParameters.writeAlgorithmIdentifier(l4, MGF1_OID);
                l5 = OAEPParameters.writeAlgorithmIdentifier(l3, NAME_TO_OID.get(mGF1ParameterSpec.getDigestAlgorithm()));
                NativeCrypto.asn1_write_null(l5);
                return;
            }
            finally {
                NativeCrypto.asn1_write_flush(l2);
                NativeCrypto.asn1_write_free(l5);
                NativeCrypto.asn1_write_free(l3);
                NativeCrypto.asn1_write_free(l4);
            }
        }
    }

    private static long writeAlgorithmIdentifier(long l2, String string) throws IOException {
        long l3 = 0L;
        try {
            l3 = NativeCrypto.asn1_write_sequence(l2);
            NativeCrypto.asn1_write_oid(l3, string);
        }
        catch (IOException iOException) {
            NativeCrypto.asn1_write_free(l3);
            throw iOException;
        }
        return l3;
    }

    @Override
    protected String engineToString() {
        return "Conscrypt OAEP AlgorithmParameters";
    }

    static {
        OID_TO_NAME.put("1.3.14.3.2.26", "SHA-1");
        OID_TO_NAME.put("2.16.840.1.101.3.4.2.4", "SHA-224");
        OID_TO_NAME.put("2.16.840.1.101.3.4.2.1", "SHA-256");
        OID_TO_NAME.put("2.16.840.1.101.3.4.2.2", "SHA-384");
        OID_TO_NAME.put("2.16.840.1.101.3.4.2.3", "SHA-512");
        for (Map.Entry<String, String> entry : OID_TO_NAME.entrySet()) {
            NAME_TO_OID.put(entry.getValue(), entry.getKey());
        }
    }
}


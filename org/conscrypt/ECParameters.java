/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.io.IOException;
import java.security.AlgorithmParametersSpi;
import java.security.InvalidAlgorithmParameterException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import org.conscrypt.NativeCrypto;
import org.conscrypt.NativeRef;
import org.conscrypt.OpenSSLECGroupContext;

public class ECParameters
extends AlgorithmParametersSpi {
    private OpenSSLECGroupContext curve;

    @Override
    protected void engineInit(AlgorithmParameterSpec object) throws InvalidParameterSpecException {
        if (object instanceof ECGenParameterSpec) {
            OpenSSLECGroupContext openSSLECGroupContext = OpenSSLECGroupContext.getCurveByName((String)(object = ((ECGenParameterSpec)object).getName()));
            if (openSSLECGroupContext == null) {
                throw new InvalidParameterSpecException("Unknown EC curve name: " + (String)object);
            }
            this.curve = openSSLECGroupContext;
            return;
        }
        if (object instanceof ECParameterSpec) {
            object = (ECParameterSpec)object;
            try {
                OpenSSLECGroupContext openSSLECGroupContext = OpenSSLECGroupContext.getInstance((ECParameterSpec)object);
                if (openSSLECGroupContext == null) {
                    throw new InvalidParameterSpecException("Unknown EC curve: " + object);
                }
                this.curve = openSSLECGroupContext;
            }
            catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException) {
                throw new InvalidParameterSpecException(invalidAlgorithmParameterException.getMessage());
            }
        } else {
            throw new InvalidParameterSpecException("Only ECParameterSpec and ECGenParameterSpec are supported");
        }
    }

    @Override
    protected void engineInit(byte[] byArray) throws IOException {
        long l2 = NativeCrypto.EC_KEY_parse_curve_name(byArray);
        if (l2 == 0L) {
            throw new IOException("Error reading ASN.1 encoding");
        }
        this.curve = new OpenSSLECGroupContext(new NativeRef.EC_GROUP(l2));
    }

    @Override
    protected void engineInit(byte[] byArray, String string) throws IOException {
        if (string == null || string.equals("ASN.1")) {
            this.engineInit(byArray);
            return;
        }
        throw new IOException("Unsupported format: " + string);
    }

    @Override
    protected <T extends AlgorithmParameterSpec> T engineGetParameterSpec(Class<T> clazz) throws InvalidParameterSpecException {
        if (clazz == ECParameterSpec.class) {
            return (T)this.curve.getECParameterSpec();
        }
        if (clazz == ECGenParameterSpec.class) {
            return (T)new ECGenParameterSpec(this.curve.getCurveName());
        }
        throw new InvalidParameterSpecException("Unsupported class: " + clazz);
    }

    @Override
    protected byte[] engineGetEncoded() throws IOException {
        return NativeCrypto.EC_KEY_marshal_curve_name(this.curve.getNativeRef());
    }

    @Override
    protected byte[] engineGetEncoded(String string) throws IOException {
        if (string == null || string.equals("ASN.1")) {
            return this.engineGetEncoded();
        }
        throw new IOException("Unsupported format: " + string);
    }

    @Override
    protected String engineToString() {
        return "Conscrypt EC AlgorithmParameters";
    }
}


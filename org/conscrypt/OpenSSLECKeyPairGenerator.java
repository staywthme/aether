/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.NamedParameterSpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.conscrypt.NativeCrypto;
import org.conscrypt.OpenSSLECGroupContext;
import org.conscrypt.OpenSSLECPrivateKey;
import org.conscrypt.OpenSSLECPublicKey;
import org.conscrypt.OpenSSLKey;

public final class OpenSSLECKeyPairGenerator
extends KeyPairGenerator {
    private static final String ALGORITHM = "EC";
    private static final int DEFAULT_KEY_SIZE = 256;
    private static final Map<Integer, String> SIZE_TO_CURVE_NAME = new HashMap<Integer, String>();
    private OpenSSLECGroupContext group;

    public OpenSSLECKeyPairGenerator() {
        super(ALGORITHM);
    }

    @Override
    public final KeyPair generateKeyPair() {
        Object object;
        if (this.group == null) {
            object = SIZE_TO_CURVE_NAME.get(256);
            this.group = OpenSSLECGroupContext.getCurveByName((String)object);
            if (this.group == null) {
                throw new RuntimeException("Curve not recognized: " + (String)object);
            }
        }
        object = new OpenSSLKey(NativeCrypto.EC_KEY_generate_key(this.group.getNativeRef()));
        return new KeyPair(new OpenSSLECPublicKey(this.group, (OpenSSLKey)object), new OpenSSLECPrivateKey(this.group, (OpenSSLKey)object));
    }

    @Override
    public final void initialize(int n2, SecureRandom object) {
        object = SIZE_TO_CURVE_NAME.get(n2);
        if (object == null) {
            throw new InvalidParameterException("unknown key size " + n2);
        }
        OpenSSLECGroupContext openSSLECGroupContext = OpenSSLECGroupContext.getCurveByName((String)object);
        if (openSSLECGroupContext == null) {
            throw new InvalidParameterException("unknown curve " + (String)object);
        }
        this.group = openSSLECGroupContext;
    }

    @Override
    public final void initialize(AlgorithmParameterSpec object, SecureRandom object2) throws InvalidAlgorithmParameterException {
        if (object instanceof ECParameterSpec) {
            object = (ECParameterSpec)object;
            this.group = OpenSSLECGroupContext.getInstance((ECParameterSpec)object);
            return;
        }
        if (object instanceof ECGenParameterSpec) {
            object = (ECGenParameterSpec)object;
            object2 = OpenSSLECGroupContext.getCurveByName((String)(object = ((NamedParameterSpec)object).getName()));
            if (object2 == null) {
                throw new InvalidAlgorithmParameterException("unknown curve name: " + (String)object);
            }
            this.group = object2;
            return;
        }
        throw new InvalidAlgorithmParameterException("parameter must be ECParameterSpec or ECGenParameterSpec");
    }

    public static void assertCurvesAreValid() {
        ArrayList<String> arrayList = new ArrayList<String>();
        for (String string : SIZE_TO_CURVE_NAME.values()) {
            if (OpenSSLECGroupContext.getCurveByName(string) != null) continue;
            arrayList.add(string);
        }
        if (arrayList.size() > 0) {
            throw new AssertionError((Object)("Invalid curve names: " + Arrays.toString(arrayList.toArray())));
        }
    }

    static {
        SIZE_TO_CURVE_NAME.put(224, "secp224r1");
        SIZE_TO_CURVE_NAME.put(256, "prime256v1");
        SIZE_TO_CURVE_NAME.put(384, "secp384r1");
        SIZE_TO_CURVE_NAME.put(521, "secp521r1");
    }
}


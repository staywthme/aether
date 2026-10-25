/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;
import java.security.spec.ECField;
import java.security.spec.ECFieldFp;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.EllipticCurve;
import java.util.HashMap;
import java.util.Map;
import org.conscrypt.NativeCrypto;
import org.conscrypt.NativeRef;
import org.conscrypt.OpenSSLECPointContext;
import org.conscrypt.Platform;

final class OpenSSLECGroupContext {
    private static final Map<String, String> ALIASES = new HashMap<String, String>();
    private final NativeRef.EC_GROUP groupCtx;

    OpenSSLECGroupContext(NativeRef.EC_GROUP eC_GROUP) {
        this.groupCtx = eC_GROUP;
    }

    static OpenSSLECGroupContext getCurveByName(String object) {
        long l2;
        if (ALIASES.containsKey(object)) {
            object = ALIASES.get(object);
        }
        if ((l2 = NativeCrypto.EC_GROUP_new_by_curve_name((String)object)) == 0L) {
            return null;
        }
        object = new NativeRef.EC_GROUP(l2);
        return new OpenSSLECGroupContext((NativeRef.EC_GROUP)object);
    }

    public final boolean equals(Object object) {
        throw new IllegalArgumentException("OpenSSLECGroupContext.equals is not defined");
    }

    public final int hashCode() {
        return super.hashCode();
    }

    final NativeRef.EC_GROUP getNativeRef() {
        return this.groupCtx;
    }

    static OpenSSLECGroupContext getInstance(ECParameterSpec eCParameterSpec) throws InvalidAlgorithmParameterException {
        long l2;
        Object object = Platform.getCurveName(eCParameterSpec);
        if (object != null) {
            return OpenSSLECGroupContext.getCurveByName((String)object);
        }
        Object object2 = eCParameterSpec.getCurve();
        Object object3 = ((EllipticCurve)object2).getField();
        if (!(object3 instanceof ECFieldFp)) {
            throw new InvalidParameterException("unhandled field class " + object3.getClass().getName());
        }
        object3 = ((ECFieldFp)object3).getP();
        Object object4 = eCParameterSpec.getGenerator();
        BigInteger bigInteger = ((EllipticCurve)object2).getB();
        BigInteger bigInteger2 = ((ECPoint)object4).getAffineX();
        object4 = ((ECPoint)object4).getAffineY();
        switch (((BigInteger)object3).bitLength()) {
            case 224: {
                if (!((BigInteger)object3).toString(16).equals("ffffffffffffffffffffffffffffffff000000000000000000000001") || !bigInteger.toString(16).equals("b4050a850c04b3abf54132565044b0b7d7bfd8ba270b39432355ffb4") || !bigInteger2.toString(16).equals("b70e0cbd6bb4bf7f321390b94a03c1d356c21122343280d6115c1d21") || !((BigInteger)object4).toString(16).equals("bd376388b5f723fb4c22dfe6cd4375a05a07476444d5819985007e34")) break;
                object = "secp224r1";
                break;
            }
            case 256: {
                if (!((BigInteger)object3).toString(16).equals("ffffffff00000001000000000000000000000000ffffffffffffffffffffffff") || !bigInteger.toString(16).equals("5ac635d8aa3a93e7b3ebbd55769886bc651d06b0cc53b0f63bce3c3e27d2604b") || !bigInteger2.toString(16).equals("6b17d1f2e12c4247f8bce6e563a440f277037d812deb33a0f4a13945d898c296") || !((BigInteger)object4).toString(16).equals("4fe342e2fe1a7f9b8ee7eb4a7c0f9e162bce33576b315ececbb6406837bf51f5")) break;
                object = "prime256v1";
                break;
            }
            case 384: {
                if (!((BigInteger)object3).toString(16).equals("fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffeffffffff0000000000000000ffffffff") || !bigInteger.toString(16).equals("b3312fa7e23ee7e4988e056be3f82d19181d9c6efe8141120314088f5013875ac656398d8a2ed19d2a85c8edd3ec2aef") || !bigInteger2.toString(16).equals("aa87ca22be8b05378eb1c71ef320ad746e1d3b628ba79b9859f741e082542a385502f25dbf55296c3a545e3872760ab7") || !((BigInteger)object4).toString(16).equals("3617de4a96262c6f5d9e98bf9292dc29f8f41dbd289a147ce9da3113b5f0b8c00a60b1ce1d7e819d7a431d7c90ea0e5f")) break;
                object = "secp384r1";
                break;
            }
            case 521: {
                if (!((BigInteger)object3).toString(16).equals("1ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff") || !bigInteger.toString(16).equals("51953eb9618e1c9a1f929a21a0b68540eea2da725b99b315f3b8b489918ef109e156193951ec7e937b1652c0bd3bb1bf073573df883d2c34f1ef451fd46b503f00") || !bigInteger2.toString(16).equals("c6858e06b70404e9cd9e3ecb662395b4429c648139053fb521f828af606b4d3dbaa14b5e77efe75928fe1dc127a2ffa8de3348b3c1856a429bf97e7e31c2e5bd66") || !((BigInteger)object4).toString(16).equals("11839296a789a3bc0045c8a5fb42c7d1bd998f54449579b446817afbd17273e662c97ee72995ef42640c550b9013fad0761353c7086a272c24088be94769fd16650")) break;
                object = "secp521r1";
            }
        }
        if (object != null) {
            return OpenSSLECGroupContext.getCurveByName((String)object);
        }
        object = ((EllipticCurve)object2).getA();
        object2 = eCParameterSpec.getOrder();
        int n2 = eCParameterSpec.getCofactor();
        try {
            l2 = NativeCrypto.EC_GROUP_new_arbitrary(((BigInteger)object3).toByteArray(), ((BigInteger)object).toByteArray(), bigInteger.toByteArray(), bigInteger2.toByteArray(), ((BigInteger)object4).toByteArray(), ((BigInteger)object2).toByteArray(), n2);
        }
        catch (Throwable throwable) {
            throw new InvalidAlgorithmParameterException("EC_GROUP_new_arbitrary failed", throwable);
        }
        if (l2 == 0L) {
            throw new InvalidAlgorithmParameterException("EC_GROUP_new_arbitrary returned NULL");
        }
        NativeRef.EC_GROUP eC_GROUP = new NativeRef.EC_GROUP(l2);
        return new OpenSSLECGroupContext(eC_GROUP);
    }

    final String getCurveName() {
        return NativeCrypto.EC_GROUP_get_curve_name(this.groupCtx);
    }

    final ECParameterSpec getECParameterSpec() {
        String string = NativeCrypto.EC_GROUP_get_curve_name(this.groupCtx);
        Object object = NativeCrypto.EC_GROUP_get_curve(this.groupCtx);
        Object object2 = new BigInteger(object[0]);
        BigInteger bigInteger = new BigInteger(object[1]);
        object = new BigInteger(object[2]);
        object2 = new ECFieldFp((BigInteger)object2);
        object = new EllipticCurve((ECField)object2, bigInteger, (BigInteger)object);
        object2 = new OpenSSLECPointContext(this, new NativeRef.EC_POINT(NativeCrypto.EC_GROUP_get_generator(this.groupCtx)));
        object2 = ((OpenSSLECPointContext)object2).getECPoint();
        bigInteger = new BigInteger(NativeCrypto.EC_GROUP_get_order(this.groupCtx));
        BigInteger bigInteger2 = new BigInteger(NativeCrypto.EC_GROUP_get_cofactor(this.groupCtx));
        ECParameterSpec eCParameterSpec = new ECParameterSpec((EllipticCurve)object, (ECPoint)object2, bigInteger, bigInteger2.intValue());
        object = eCParameterSpec;
        Platform.setCurveName(eCParameterSpec, string);
        return object;
    }

    static {
        ALIASES.put("secp256r1", "prime256v1");
        ALIASES.put("1.3.132.0.33", "secp224r1");
        ALIASES.put("1.3.132.0.34", "secp384r1");
        ALIASES.put("1.3.132.0.35", "secp521r1");
        ALIASES.put("1.2.840.10045.3.1.7", "prime256v1");
    }
}


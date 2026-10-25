/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.math.BigInteger;
import java.security.spec.ECPoint;
import org.conscrypt.NativeCrypto;
import org.conscrypt.NativeRef;
import org.conscrypt.OpenSSLECGroupContext;

final class OpenSSLECPointContext {
    private final OpenSSLECGroupContext group;
    private final NativeRef.EC_POINT pointCtx;

    OpenSSLECPointContext(OpenSSLECGroupContext openSSLECGroupContext, NativeRef.EC_POINT eC_POINT) {
        this.group = openSSLECGroupContext;
        this.pointCtx = eC_POINT;
    }

    public final boolean equals(Object object) {
        throw new IllegalArgumentException("OpenSSLECPointContext.equals is not defined.");
    }

    final ECPoint getECPoint() {
        Object object = NativeCrypto.EC_POINT_get_affine_coordinates(this.group.getNativeRef(), this.pointCtx);
        BigInteger bigInteger = new BigInteger(object[0]);
        object = new BigInteger(object[1]);
        return new ECPoint(bigInteger, (BigInteger)object);
    }

    public final int hashCode() {
        return super.hashCode();
    }

    final NativeRef.EC_POINT getNativeRef() {
        return this.pointCtx;
    }

    static OpenSSLECPointContext getInstance(OpenSSLECGroupContext openSSLECGroupContext, ECPoint eCPoint) {
        OpenSSLECPointContext openSSLECPointContext = new OpenSSLECPointContext(openSSLECGroupContext, new NativeRef.EC_POINT(NativeCrypto.EC_POINT_new(openSSLECGroupContext.getNativeRef())));
        NativeCrypto.EC_POINT_set_affine_coordinates(openSSLECGroupContext.getNativeRef(), openSSLECPointContext.getNativeRef(), eCPoint.getAffineX().toByteArray(), eCPoint.getAffineY().toByteArray());
        return openSSLECPointContext;
    }
}


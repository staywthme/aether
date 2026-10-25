/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.lang.reflect.Method;
import javax.net.ssl.SSLParameters;
import org.conscrypt.AbstractConscryptSocket;
import org.conscrypt.ConscryptEngine;
import org.conscrypt.EmptyArray;
import org.conscrypt.Java8PlatformUtil;
import org.conscrypt.SSLParametersImpl;

final class Java9PlatformUtil {
    private static final Method SSL_PARAMETERS_GET_APPLICATION_PROTOCOLS_METHOD;
    private static final Method SSL_PARAMETERS_SET_APPLICATION_PROTOCOLS_METHOD;

    static void setSSLParameters(SSLParameters sSLParameters, SSLParametersImpl sSLParametersImpl, AbstractConscryptSocket abstractConscryptSocket) {
        Java8PlatformUtil.setSSLParameters(sSLParameters, sSLParametersImpl, abstractConscryptSocket);
        sSLParametersImpl.setApplicationProtocols(Java9PlatformUtil.getApplicationProtocols(sSLParameters));
    }

    static void getSSLParameters(SSLParameters sSLParameters, SSLParametersImpl sSLParametersImpl, AbstractConscryptSocket abstractConscryptSocket) {
        Java8PlatformUtil.getSSLParameters(sSLParameters, sSLParametersImpl, abstractConscryptSocket);
        Java9PlatformUtil.setApplicationProtocols(sSLParameters, sSLParametersImpl.getApplicationProtocols());
    }

    static void setSSLParameters(SSLParameters sSLParameters, SSLParametersImpl sSLParametersImpl, ConscryptEngine conscryptEngine) {
        Java8PlatformUtil.setSSLParameters(sSLParameters, sSLParametersImpl, conscryptEngine);
        sSLParametersImpl.setApplicationProtocols(Java9PlatformUtil.getApplicationProtocols(sSLParameters));
    }

    static void getSSLParameters(SSLParameters sSLParameters, SSLParametersImpl sSLParametersImpl, ConscryptEngine conscryptEngine) {
        Java8PlatformUtil.getSSLParameters(sSLParameters, sSLParametersImpl, conscryptEngine);
        Java9PlatformUtil.setApplicationProtocols(sSLParameters, sSLParametersImpl.getApplicationProtocols());
    }

    private static String[] getApplicationProtocols(SSLParameters sSLParameters) {
        if (SSL_PARAMETERS_GET_APPLICATION_PROTOCOLS_METHOD != null) {
            try {
                return (String[])SSL_PARAMETERS_GET_APPLICATION_PROTOCOLS_METHOD.invoke(sSLParameters, new Object[0]);
            }
            catch (ReflectiveOperationException reflectiveOperationException) {}
        }
        return EmptyArray.STRING;
    }

    private static void setApplicationProtocols(SSLParameters sSLParameters, String[] stringArray) {
        if (SSL_PARAMETERS_SET_APPLICATION_PROTOCOLS_METHOD != null) {
            try {
                SSL_PARAMETERS_SET_APPLICATION_PROTOCOLS_METHOD.invoke(sSLParameters, new Object[]{stringArray});
                return;
            }
            catch (ReflectiveOperationException reflectiveOperationException) {}
        }
    }

    private Java9PlatformUtil() {
    }

    static {
        Method method;
        Object object = SSLParameters.class;
        try {
            method = ((Class)object).getMethod("getApplicationProtocols", new Class[0]);
            object = ((Class)object).getMethod("setApplicationProtocols", String[].class);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            method = null;
            object = null;
        }
        SSL_PARAMETERS_GET_APPLICATION_PROTOCOLS_METHOD = method;
        SSL_PARAMETERS_SET_APPLICATION_PROTOCOLS_METHOD = object;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.util.Collections;
import javax.net.ssl.SNIHostName;
import javax.net.ssl.SNIMatcher;
import javax.net.ssl.SNIServerName;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSession;
import org.conscrypt.AbstractConscryptSocket;
import org.conscrypt.AddressUtils;
import org.conscrypt.ConscryptEngine;
import org.conscrypt.ExternalSession;
import org.conscrypt.Java8EngineWrapper;
import org.conscrypt.Java8ExtendedSSLSession;
import org.conscrypt.SSLParametersImpl;

final class Java8PlatformUtil {
    static void setSSLParameters(SSLParameters object, SSLParametersImpl sSLParametersImpl, AbstractConscryptSocket abstractConscryptSocket) {
        Java8PlatformUtil.setSSLParameters((SSLParameters)object, sSLParametersImpl);
        object = Java8PlatformUtil.getSniHostName((SSLParameters)object);
        if (object != null) {
            abstractConscryptSocket.setHostname((String)object);
        }
    }

    static void setSSLParameters(SSLParameters object, SSLParametersImpl sSLParametersImpl, ConscryptEngine conscryptEngine) {
        Java8PlatformUtil.setSSLParameters((SSLParameters)object, sSLParametersImpl);
        object = Java8PlatformUtil.getSniHostName((SSLParameters)object);
        if (object != null) {
            conscryptEngine.setHostname((String)object);
        }
    }

    static void getSSLParameters(SSLParameters sSLParameters, SSLParametersImpl sSLParametersImpl, AbstractConscryptSocket abstractConscryptSocket) {
        Java8PlatformUtil.getSSLParameters(sSLParameters, sSLParametersImpl);
        if (sSLParametersImpl.getUseSni() && AddressUtils.isValidSniHostname(abstractConscryptSocket.getHostname())) {
            sSLParameters.setServerNames(Collections.singletonList(new SNIHostName(abstractConscryptSocket.getHostname())));
        }
    }

    static void getSSLParameters(SSLParameters sSLParameters, SSLParametersImpl sSLParametersImpl, ConscryptEngine conscryptEngine) {
        Java8PlatformUtil.getSSLParameters(sSLParameters, sSLParametersImpl);
        if (sSLParametersImpl.getUseSni() && AddressUtils.isValidSniHostname(conscryptEngine.getHostname())) {
            sSLParameters.setServerNames(Collections.singletonList(new SNIHostName(conscryptEngine.getHostname())));
        }
    }

    private static String getSniHostName(SSLParameters iterator2) {
        if ((iterator2 = ((SSLParameters)((Object)iterator2)).getServerNames()) != null) {
            iterator2 = iterator2.iterator();
            while (iterator2.hasNext()) {
                SNIServerName sNIServerName = (SNIServerName)iterator2.next();
                if (sNIServerName.getType() != 0) continue;
                return ((SNIHostName)sNIServerName).getAsciiName();
            }
        }
        return null;
    }

    private static void setSSLParameters(SSLParameters sSLParameters, SSLParametersImpl sSLParametersImpl) {
        sSLParametersImpl.setEndpointIdentificationAlgorithm(sSLParameters.getEndpointIdentificationAlgorithm());
        sSLParametersImpl.setUseCipherSuitesOrder(sSLParameters.getUseCipherSuitesOrder());
        sSLParametersImpl.setSNIMatchers(sSLParameters.getSNIMatchers());
        sSLParametersImpl.setAlgorithmConstraints(sSLParameters.getAlgorithmConstraints());
    }

    private static void getSSLParameters(SSLParameters sSLParameters, SSLParametersImpl sSLParametersImpl) {
        sSLParameters.setEndpointIdentificationAlgorithm(sSLParametersImpl.getEndpointIdentificationAlgorithm());
        sSLParameters.setUseCipherSuitesOrder(sSLParametersImpl.getUseCipherSuitesOrder());
        sSLParameters.setSNIMatchers(sSLParametersImpl.getSNIMatchers());
        sSLParameters.setAlgorithmConstraints(sSLParametersImpl.getAlgorithmConstraints());
    }

    static boolean serverNamePermitted(SSLParametersImpl iterator2, String string) {
        if ((iterator2 = ((SSLParametersImpl)((Object)iterator2)).getSNIMatchers()) == null || iterator2.isEmpty()) {
            return true;
        }
        iterator2 = iterator2.iterator();
        while (iterator2.hasNext()) {
            SNIMatcher sNIMatcher = (SNIMatcher)iterator2.next();
            boolean bl = sNIMatcher.matches(new SNIHostName(string));
            if (!bl) continue;
            return true;
        }
        return false;
    }

    static SSLEngine wrapEngine(ConscryptEngine conscryptEngine) {
        return new Java8EngineWrapper(conscryptEngine);
    }

    static SSLEngine unwrapEngine(SSLEngine sSLEngine) {
        return Java8EngineWrapper.getDelegate(sSLEngine);
    }

    static SSLSession wrapSSLSession(ExternalSession externalSession) {
        return new Java8ExtendedSSLSession(externalSession);
    }

    private Java8PlatformUtil() {
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.security.AlgorithmConstraints;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import javax.crypto.SecretKey;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SNIMatcher;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509KeyManager;
import javax.net.ssl.X509TrustManager;
import javax.security.auth.x500.X500Principal;
import org.conscrypt.AbstractSessionContext;
import org.conscrypt.ApplicationProtocolSelectorAdapter;
import org.conscrypt.ClientSessionContext;
import org.conscrypt.DuckTypedPSKKeyManager;
import org.conscrypt.EmptyArray;
import org.conscrypt.NativeCrypto;
import org.conscrypt.PSKKeyManager;
import org.conscrypt.Platform;
import org.conscrypt.SSLUtils;
import org.conscrypt.ServerSessionContext;

final class SSLParametersImpl
implements Cloneable {
    private static volatile X509KeyManager defaultX509KeyManager;
    private static volatile X509TrustManager defaultX509TrustManager;
    private static volatile SSLParametersImpl defaultParameters;
    private final ClientSessionContext clientSessionContext;
    private final ServerSessionContext serverSessionContext;
    private final X509KeyManager x509KeyManager;
    private final PSKKeyManager pskKeyManager;
    private final X509TrustManager x509TrustManager;
    String[] enabledProtocols;
    boolean isEnabledProtocolsFiltered;
    String[] enabledCipherSuites;
    private boolean client_mode = true;
    private boolean need_client_auth = false;
    private boolean want_client_auth = false;
    private boolean enable_session_creation = true;
    private String endpointIdentificationAlgorithm;
    private boolean useCipherSuitesOrder;
    private Collection<SNIMatcher> sniMatchers;
    private AlgorithmConstraints algorithmConstraints;
    private boolean ctVerificationEnabled;
    byte[] sctExtension;
    byte[] ocspResponse;
    byte[] applicationProtocols = EmptyArray.BYTE;
    ApplicationProtocolSelectorAdapter applicationProtocolSelector;
    boolean useSessionTickets;
    private Boolean useSni;
    boolean channelIdEnabled;
    private static final String[] EMPTY_STRING_ARRAY;

    SSLParametersImpl(KeyManager[] keyManagerArray, TrustManager[] trustManagerArray, SecureRandom secureRandom, ClientSessionContext clientSessionContext, ServerSessionContext serverSessionContext, String[] stringArray) throws KeyManagementException {
        this.serverSessionContext = serverSessionContext;
        this.clientSessionContext = clientSessionContext;
        if (keyManagerArray == null) {
            this.x509KeyManager = SSLParametersImpl.getDefaultX509KeyManager();
            this.pskKeyManager = null;
        } else {
            this.x509KeyManager = SSLParametersImpl.findFirstX509KeyManager(keyManagerArray);
            this.pskKeyManager = SSLParametersImpl.findFirstPSKKeyManager(keyManagerArray);
        }
        this.x509TrustManager = trustManagerArray == null ? SSLParametersImpl.getDefaultX509TrustManager() : SSLParametersImpl.findFirstX509TrustManager(trustManagerArray);
        this.enabledProtocols = (String[])NativeCrypto.checkEnabledProtocols(stringArray == null ? NativeCrypto.DEFAULT_PROTOCOLS : stringArray).clone();
        boolean bl = this.x509KeyManager != null || this.x509TrustManager != null;
        boolean bl2 = this.pskKeyManager != null;
        this.enabledCipherSuites = SSLParametersImpl.getDefaultCipherSuites(bl, bl2);
    }

    private SSLParametersImpl(ClientSessionContext clientSessionContext, ServerSessionContext serverSessionContext, X509KeyManager x509KeyManager, PSKKeyManager pSKKeyManager, X509TrustManager x509TrustManager, SSLParametersImpl sSLParametersImpl) {
        this.clientSessionContext = clientSessionContext;
        this.serverSessionContext = serverSessionContext;
        this.x509KeyManager = x509KeyManager;
        this.pskKeyManager = pSKKeyManager;
        this.x509TrustManager = x509TrustManager;
        this.enabledProtocols = sSLParametersImpl.enabledProtocols == null ? null : (String[])sSLParametersImpl.enabledProtocols.clone();
        this.isEnabledProtocolsFiltered = sSLParametersImpl.isEnabledProtocolsFiltered;
        this.enabledCipherSuites = sSLParametersImpl.enabledCipherSuites == null ? null : (String[])sSLParametersImpl.enabledCipherSuites.clone();
        this.client_mode = sSLParametersImpl.client_mode;
        this.need_client_auth = sSLParametersImpl.need_client_auth;
        this.want_client_auth = sSLParametersImpl.want_client_auth;
        this.enable_session_creation = sSLParametersImpl.enable_session_creation;
        this.endpointIdentificationAlgorithm = sSLParametersImpl.endpointIdentificationAlgorithm;
        this.useCipherSuitesOrder = sSLParametersImpl.useCipherSuitesOrder;
        this.ctVerificationEnabled = sSLParametersImpl.ctVerificationEnabled;
        this.sctExtension = sSLParametersImpl.sctExtension == null ? null : (byte[])sSLParametersImpl.sctExtension.clone();
        this.ocspResponse = sSLParametersImpl.ocspResponse == null ? null : (byte[])sSLParametersImpl.ocspResponse.clone();
        this.applicationProtocols = sSLParametersImpl.applicationProtocols == null ? null : (byte[])sSLParametersImpl.applicationProtocols.clone();
        this.applicationProtocolSelector = sSLParametersImpl.applicationProtocolSelector;
        this.useSessionTickets = sSLParametersImpl.useSessionTickets;
        this.useSni = sSLParametersImpl.useSni;
        this.channelIdEnabled = sSLParametersImpl.channelIdEnabled;
    }

    static SSLParametersImpl getDefault() throws KeyManagementException {
        SSLParametersImpl sSLParametersImpl = defaultParameters;
        if (sSLParametersImpl == null) {
            defaultParameters = sSLParametersImpl = new SSLParametersImpl(null, null, null, new ClientSessionContext(), new ServerSessionContext(), null);
        }
        return (SSLParametersImpl)sSLParametersImpl.clone();
    }

    final AbstractSessionContext getSessionContext() {
        if (this.client_mode) {
            return this.clientSessionContext;
        }
        return this.serverSessionContext;
    }

    final ClientSessionContext getClientSessionContext() {
        return this.clientSessionContext;
    }

    final X509KeyManager getX509KeyManager() {
        return this.x509KeyManager;
    }

    final PSKKeyManager getPSKKeyManager() {
        return this.pskKeyManager;
    }

    final X509TrustManager getX509TrustManager() {
        return this.x509TrustManager;
    }

    final String[] getEnabledCipherSuites() {
        if (Arrays.asList(this.enabledProtocols).contains("TLSv1.3")) {
            return SSLUtils.concat(NativeCrypto.SUPPORTED_TLS_1_3_CIPHER_SUITES, this.enabledCipherSuites);
        }
        return (String[])this.enabledCipherSuites.clone();
    }

    final void setEnabledCipherSuites(String[] stringArray) {
        this.enabledCipherSuites = NativeCrypto.checkEnabledCipherSuites(SSLParametersImpl.filterFromCipherSuites(stringArray, NativeCrypto.SUPPORTED_TLS_1_3_CIPHER_SUITES_SET));
    }

    final String[] getEnabledProtocols() {
        return (String[])this.enabledProtocols.clone();
    }

    final void setEnabledProtocols(String[] stringArray) {
        if (stringArray == null) {
            throw new IllegalArgumentException("protocols == null");
        }
        String[] stringArray2 = SSLParametersImpl.filterFromProtocols(stringArray, "SSLv3");
        this.isEnabledProtocolsFiltered = stringArray.length != stringArray2.length;
        this.enabledProtocols = (String[])NativeCrypto.checkEnabledProtocols(stringArray2).clone();
    }

    final void setApplicationProtocols(String[] stringArray) {
        this.applicationProtocols = SSLUtils.encodeProtocols(stringArray);
    }

    final String[] getApplicationProtocols() {
        return SSLUtils.decodeProtocols(this.applicationProtocols);
    }

    final void setApplicationProtocolSelector(ApplicationProtocolSelectorAdapter applicationProtocolSelectorAdapter) {
        this.applicationProtocolSelector = applicationProtocolSelectorAdapter;
    }

    final ApplicationProtocolSelectorAdapter getApplicationProtocolSelector() {
        return this.applicationProtocolSelector;
    }

    final void setUseClientMode(boolean bl) {
        this.client_mode = bl;
    }

    final boolean getUseClientMode() {
        return this.client_mode;
    }

    final void setNeedClientAuth(boolean bl) {
        this.need_client_auth = bl;
        this.want_client_auth = false;
    }

    final boolean getNeedClientAuth() {
        return this.need_client_auth;
    }

    final void setWantClientAuth(boolean bl) {
        this.want_client_auth = bl;
        this.need_client_auth = false;
    }

    final boolean getWantClientAuth() {
        return this.want_client_auth;
    }

    final void setEnableSessionCreation(boolean bl) {
        this.enable_session_creation = bl;
    }

    final boolean getEnableSessionCreation() {
        return this.enable_session_creation;
    }

    final void setUseSessionTickets(boolean bl) {
        this.useSessionTickets = bl;
    }

    final void setUseSni(boolean bl) {
        this.useSni = bl;
    }

    final boolean getUseSni() {
        if (this.useSni != null) {
            return this.useSni;
        }
        return this.isSniEnabledByDefault();
    }

    final void setCTVerificationEnabled(boolean bl) {
        this.ctVerificationEnabled = bl;
    }

    final void setSCTExtension(byte[] byArray) {
        this.sctExtension = byArray;
    }

    final void setOCSPResponse(byte[] byArray) {
        this.ocspResponse = byArray;
    }

    final byte[] getOCSPResponse() {
        return this.ocspResponse;
    }

    private static String[] filterFromProtocols(String[] stringArray, String string) {
        if (stringArray.length == 1 && string.equals(stringArray[0])) {
            return EMPTY_STRING_ARRAY;
        }
        ArrayList<String> arrayList = new ArrayList<String>();
        for (String string2 : stringArray) {
            if (string.equals(string2)) continue;
            arrayList.add(string2);
        }
        return arrayList.toArray(EMPTY_STRING_ARRAY);
    }

    private static String[] filterFromCipherSuites(String[] stringArray, Set<String> set) {
        if (stringArray == null || stringArray.length == 0) {
            return stringArray;
        }
        ArrayList<String> arrayList = new ArrayList<String>(stringArray.length);
        for (String string : stringArray) {
            if (set.contains(string)) continue;
            arrayList.add(string);
        }
        return arrayList.toArray(EMPTY_STRING_ARRAY);
    }

    private boolean isSniEnabledByDefault() {
        try {
            String string = System.getProperty("jsse.enableSNIExtension", "true");
            if ("true".equalsIgnoreCase(string)) {
                return true;
            }
            if ("false".equalsIgnoreCase(string)) {
                return false;
            }
            throw new RuntimeException("Can only set \"jsse.enableSNIExtension\" to \"true\" or \"false\"");
        }
        catch (SecurityException securityException) {
            return true;
        }
    }

    protected final Object clone() {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            throw new AssertionError((Object)cloneNotSupportedException);
        }
    }

    final SSLParametersImpl cloneWithTrustManager(X509TrustManager x509TrustManager) {
        return new SSLParametersImpl(this.clientSessionContext, this.serverSessionContext, this.x509KeyManager, this.pskKeyManager, x509TrustManager, this);
    }

    private static X509KeyManager getDefaultX509KeyManager() throws KeyManagementException {
        X509KeyManager x509KeyManager = defaultX509KeyManager;
        if (x509KeyManager == null) {
            defaultX509KeyManager = x509KeyManager = SSLParametersImpl.createDefaultX509KeyManager();
        }
        return x509KeyManager;
    }

    private static X509KeyManager createDefaultX509KeyManager() throws KeyManagementException {
        try {
            Object[] objectArray = KeyManagerFactory.getDefaultAlgorithm();
            objectArray = KeyManagerFactory.getInstance((String)objectArray);
            objectArray.init(null, null);
            objectArray = objectArray.getKeyManagers();
            X509KeyManager x509KeyManager = SSLParametersImpl.findFirstX509KeyManager((KeyManager[])objectArray);
            if (x509KeyManager == null) {
                throw new KeyManagementException("No X509KeyManager among default KeyManagers: " + Arrays.toString(objectArray));
            }
            return x509KeyManager;
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new KeyManagementException(noSuchAlgorithmException);
        }
        catch (KeyStoreException keyStoreException) {
            throw new KeyManagementException(keyStoreException);
        }
        catch (UnrecoverableKeyException unrecoverableKeyException) {
            throw new KeyManagementException(unrecoverableKeyException);
        }
    }

    private static X509KeyManager findFirstX509KeyManager(KeyManager[] keyManagerArray) {
        for (KeyManager keyManager : keyManagerArray) {
            if (!(keyManager instanceof X509KeyManager)) continue;
            return (X509KeyManager)keyManager;
        }
        return null;
    }

    private static PSKKeyManager findFirstPSKKeyManager(KeyManager[] keyManagerArray) {
        for (KeyManager keyManager : keyManagerArray) {
            if (keyManager instanceof PSKKeyManager) {
                return (PSKKeyManager)keyManager;
            }
            if (keyManager == null) continue;
            try {
                return DuckTypedPSKKeyManager.getInstance(keyManager);
            }
            catch (NoSuchMethodException noSuchMethodException) {}
        }
        return null;
    }

    static X509TrustManager getDefaultX509TrustManager() throws KeyManagementException {
        X509TrustManager x509TrustManager = defaultX509TrustManager;
        if (x509TrustManager == null) {
            defaultX509TrustManager = x509TrustManager = SSLParametersImpl.createDefaultX509TrustManager();
        }
        return x509TrustManager;
    }

    private static X509TrustManager createDefaultX509TrustManager() throws KeyManagementException {
        try {
            Object[] objectArray = TrustManagerFactory.getDefaultAlgorithm();
            objectArray = TrustManagerFactory.getInstance((String)objectArray);
            objectArray.init((KeyStore)null);
            objectArray = objectArray.getTrustManagers();
            X509TrustManager x509TrustManager = SSLParametersImpl.findFirstX509TrustManager((TrustManager[])objectArray);
            if (x509TrustManager == null) {
                throw new KeyManagementException("No X509TrustManager in among default TrustManagers: " + Arrays.toString(objectArray));
            }
            return x509TrustManager;
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new KeyManagementException(noSuchAlgorithmException);
        }
        catch (KeyStoreException keyStoreException) {
            throw new KeyManagementException(keyStoreException);
        }
    }

    private static X509TrustManager findFirstX509TrustManager(TrustManager[] trustManagerArray) {
        for (TrustManager trustManager : trustManagerArray) {
            if (!(trustManager instanceof X509TrustManager)) continue;
            return (X509TrustManager)trustManager;
        }
        return null;
    }

    final String getEndpointIdentificationAlgorithm() {
        return this.endpointIdentificationAlgorithm;
    }

    final void setEndpointIdentificationAlgorithm(String string) {
        this.endpointIdentificationAlgorithm = string;
    }

    final boolean getUseCipherSuitesOrder() {
        return this.useCipherSuitesOrder;
    }

    final Collection<SNIMatcher> getSNIMatchers() {
        if (this.sniMatchers == null) {
            return null;
        }
        return new ArrayList<SNIMatcher>(this.sniMatchers);
    }

    final void setSNIMatchers(Collection<SNIMatcher> collection) {
        this.sniMatchers = collection != null ? new ArrayList<SNIMatcher>(collection) : null;
    }

    final AlgorithmConstraints getAlgorithmConstraints() {
        return this.algorithmConstraints;
    }

    final void setAlgorithmConstraints(AlgorithmConstraints algorithmConstraints) {
        this.algorithmConstraints = algorithmConstraints;
    }

    final void setUseCipherSuitesOrder(boolean bl) {
        this.useCipherSuitesOrder = bl;
    }

    private static String[] getDefaultCipherSuites(boolean bl, boolean bl2) {
        if (bl) {
            if (bl2) {
                return SSLUtils.concat(NativeCrypto.DEFAULT_PSK_CIPHER_SUITES, NativeCrypto.DEFAULT_X509_CIPHER_SUITES, {"TLS_EMPTY_RENEGOTIATION_INFO_SCSV"});
            }
            return SSLUtils.concat(NativeCrypto.DEFAULT_X509_CIPHER_SUITES, {"TLS_EMPTY_RENEGOTIATION_INFO_SCSV"});
        }
        if (bl2) {
            return SSLUtils.concat(NativeCrypto.DEFAULT_PSK_CIPHER_SUITES, {"TLS_EMPTY_RENEGOTIATION_INFO_SCSV"});
        }
        return new String[]{"TLS_EMPTY_RENEGOTIATION_INFO_SCSV"};
    }

    final boolean isCTVerificationEnabled(String string) {
        if (string == null) {
            return false;
        }
        if (this.ctVerificationEnabled) {
            return true;
        }
        return Platform.isCTVerificationRequired(string);
    }

    static {
        EMPTY_STRING_ARRAY = new String[0];
    }

    static interface PSKCallbacks {
        public String chooseServerPSKIdentityHint(PSKKeyManager var1);

        public String chooseClientPSKIdentity(PSKKeyManager var1, String var2);

        public SecretKey getPSKKey(PSKKeyManager var1, String var2, String var3);
    }

    static interface AliasChooser {
        public String chooseClientAlias(X509KeyManager var1, X500Principal[] var2, String[] var3);

        public String chooseServerAlias(X509KeyManager var1, String var2);
    }
}


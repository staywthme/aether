/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidator;
import java.security.cert.CertPathValidatorException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateParsingException;
import java.security.cert.PKIXCertPathChecker;
import java.security.cert.PKIXParameters;
import java.security.cert.PKIXRevocationChecker;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.X509ExtendedTrustManager;
import org.conscrypt.CertBlocklist;
import org.conscrypt.CertPinManager;
import org.conscrypt.CertificatePriorityComparator;
import org.conscrypt.ChainStrengthAnalyzer;
import org.conscrypt.ConscryptCertStore;
import org.conscrypt.ConscryptHostnameVerifier;
import org.conscrypt.ConscryptSession;
import org.conscrypt.Platform;
import org.conscrypt.TrustedCertificateIndex;
import org.conscrypt.ct.CTLogStore;
import org.conscrypt.ct.CTPolicy;
import org.conscrypt.ct.CTVerificationResult;
import org.conscrypt.ct.CTVerifier;

public final class TrustManagerImpl
extends X509ExtendedTrustManager {
    private static final Logger logger = Logger.getLogger(TrustManagerImpl.class.getName());
    private static final TrustAnchorComparator TRUST_ANCHOR_COMPARATOR = new TrustAnchorComparator();
    private static ConscryptHostnameVerifier defaultHostnameVerifier;
    private final KeyStore rootKeyStore;
    private CertPinManager pinManager;
    private final ConscryptCertStore trustedCertificateStore;
    private final CertPathValidator validator;
    private final TrustedCertificateIndex trustedCertificateIndex;
    private final TrustedCertificateIndex intermediateIndex;
    private final X509Certificate[] acceptedIssuers;
    private final Exception err;
    private final CertificateFactory factory;
    private final CertBlocklist blocklist;
    private CTVerifier ctVerifier;
    private CTPolicy ctPolicy;
    private ConscryptHostnameVerifier hostnameVerifier;
    private boolean ctEnabledOverride;

    public TrustManagerImpl(KeyStore keyStore) {
        this(keyStore, null);
    }

    public TrustManagerImpl(KeyStore keyStore, CertPinManager certPinManager) {
        this(keyStore, certPinManager, null);
    }

    public TrustManagerImpl(KeyStore keyStore, CertPinManager certPinManager, ConscryptCertStore conscryptCertStore) {
        this(keyStore, certPinManager, conscryptCertStore, null);
    }

    public TrustManagerImpl(KeyStore keyStore, CertPinManager certPinManager, ConscryptCertStore conscryptCertStore, CertBlocklist certBlocklist) {
        this(keyStore, certPinManager, conscryptCertStore, certBlocklist, null, null, null);
    }

    public TrustManagerImpl(KeyStore object, CertPinManager certPinManager, ConscryptCertStore conscryptCertStore, CertBlocklist certBlocklist, CTLogStore cTLogStore, CTVerifier object2, CTPolicy cTPolicy) {
        object2 = null;
        CertificateFactory certificateFactory = null;
        Object object3 = null;
        ConscryptCertStore conscryptCertStore2 = null;
        TrustedCertificateIndex trustedCertificateIndex = null;
        X509Certificate[] x509CertificateArray = null;
        Exception exception = null;
        try {
            object2 = CertPathValidator.getInstance("PKIX");
            certificateFactory = CertificateFactory.getInstance("X509");
            if ("AndroidCAStore".equals(((KeyStore)object).getType()) && Platform.supportsConscryptCertStore()) {
                object3 = object;
                conscryptCertStore2 = conscryptCertStore != null ? conscryptCertStore : Platform.newDefaultCertStore();
                x509CertificateArray = null;
                trustedCertificateIndex = new TrustedCertificateIndex();
            } else {
                object3 = null;
                conscryptCertStore2 = conscryptCertStore;
                x509CertificateArray = TrustManagerImpl.acceptedIssuers((KeyStore)object);
                trustedCertificateIndex = new TrustedCertificateIndex(TrustManagerImpl.trustAnchors(x509CertificateArray));
            }
        }
        catch (Exception exception2) {
            object = exception2;
            exception = exception2;
        }
        if (certBlocklist == null) {
            certBlocklist = Platform.newDefaultBlocklist();
        }
        if (cTLogStore == null) {
            cTLogStore = Platform.newDefaultLogStore();
        }
        if (cTPolicy == null) {
            cTPolicy = Platform.newDefaultPolicy(cTLogStore);
        }
        this.pinManager = certPinManager;
        this.rootKeyStore = object3;
        this.trustedCertificateStore = conscryptCertStore2;
        this.validator = object2;
        this.factory = certificateFactory;
        this.trustedCertificateIndex = trustedCertificateIndex;
        this.intermediateIndex = new TrustedCertificateIndex();
        this.acceptedIssuers = x509CertificateArray;
        this.err = exception;
        this.blocklist = certBlocklist;
        this.ctVerifier = new CTVerifier(cTLogStore);
        this.ctPolicy = cTPolicy;
    }

    private static X509Certificate[] acceptedIssuers(KeyStore keyStore) {
        try {
            ArrayList<Object> arrayList = new ArrayList<Object>();
            Enumeration<String> enumeration = keyStore.aliases();
            while (enumeration.hasMoreElements()) {
                Object object = enumeration.nextElement();
                if ((object = (X509Certificate)keyStore.getCertificate((String)object)) == null) continue;
                arrayList.add(object);
            }
            ArrayList<Object> arrayList2 = arrayList;
            return arrayList2.toArray(new X509Certificate[arrayList2.size()]);
        }
        catch (KeyStoreException keyStoreException) {
            return new X509Certificate[0];
        }
    }

    private static Set<TrustAnchor> trustAnchors(X509Certificate[] x509CertificateArray) {
        HashSet<TrustAnchor> hashSet = new HashSet<TrustAnchor>(x509CertificateArray.length);
        for (X509Certificate x509Certificate : x509CertificateArray) {
            hashSet.add(new TrustAnchor(x509Certificate, null));
        }
        return hashSet;
    }

    @Override
    public final void checkClientTrusted(X509Certificate[] x509CertificateArray, String string) throws CertificateException {
        this.checkTrusted(x509CertificateArray, string, null, null, true);
    }

    public final List<X509Certificate> checkClientTrusted(X509Certificate[] x509CertificateArray, String string, String string2) throws CertificateException {
        return this.checkTrusted(x509CertificateArray, null, null, string, string2, true);
    }

    private static SSLSession getHandshakeSessionOrThrow(SSLSocket object) throws CertificateException {
        if ((object = ((SSLSocket)object).getHandshakeSession()) == null) {
            throw new CertificateException("Not in handshake; no session available");
        }
        return object;
    }

    @Override
    public final void checkClientTrusted(X509Certificate[] x509CertificateArray, String string, Socket socket) throws CertificateException {
        SSLSession sSLSession = null;
        SSLParameters sSLParameters = null;
        if (socket instanceof SSLSocket) {
            socket = (SSLSocket)socket;
            sSLSession = TrustManagerImpl.getHandshakeSessionOrThrow((SSLSocket)socket);
            sSLParameters = ((SSLSocket)socket).getSSLParameters();
        }
        this.checkTrusted(x509CertificateArray, string, sSLSession, sSLParameters, true);
    }

    @Override
    public final void checkClientTrusted(X509Certificate[] x509CertificateArray, String string, SSLEngine sSLEngine) throws CertificateException {
        SSLSession sSLSession = sSLEngine.getHandshakeSession();
        if (sSLSession == null) {
            throw new CertificateException("Not in handshake; no session available");
        }
        this.checkTrusted(x509CertificateArray, string, sSLSession, sSLEngine.getSSLParameters(), true);
    }

    @Override
    public final void checkServerTrusted(X509Certificate[] x509CertificateArray, String string) throws CertificateException {
        this.checkTrusted(x509CertificateArray, string, null, null, false);
    }

    public final List<X509Certificate> checkServerTrusted(X509Certificate[] x509CertificateArray, String string, String string2) throws CertificateException {
        return this.checkTrusted(x509CertificateArray, null, null, string, string2, false);
    }

    public final List<X509Certificate> getTrustedChainForServer(X509Certificate[] x509CertificateArray, String string, Socket socket) throws CertificateException {
        SSLSession sSLSession = null;
        SSLParameters sSLParameters = null;
        if (socket instanceof SSLSocket) {
            socket = (SSLSocket)socket;
            sSLSession = TrustManagerImpl.getHandshakeSessionOrThrow((SSLSocket)socket);
            sSLParameters = ((SSLSocket)socket).getSSLParameters();
        }
        return this.checkTrusted(x509CertificateArray, string, sSLSession, sSLParameters, false);
    }

    public final List<X509Certificate> getTrustedChainForServer(X509Certificate[] x509CertificateArray, String string, SSLEngine sSLEngine) throws CertificateException {
        SSLSession sSLSession = sSLEngine.getHandshakeSession();
        if (sSLSession == null) {
            throw new CertificateException("Not in handshake; no session available");
        }
        return this.checkTrusted(x509CertificateArray, string, sSLSession, sSLEngine.getSSLParameters(), false);
    }

    @Override
    public final void checkServerTrusted(X509Certificate[] x509CertificateArray, String string, Socket socket) throws CertificateException {
        this.getTrustedChainForServer(x509CertificateArray, string, socket);
    }

    @Override
    public final void checkServerTrusted(X509Certificate[] x509CertificateArray, String string, SSLEngine sSLEngine) throws CertificateException {
        this.getTrustedChainForServer(x509CertificateArray, string, sSLEngine);
    }

    public final List<X509Certificate> checkServerTrusted(X509Certificate[] x509CertificateArray, String string, SSLSession sSLSession) throws CertificateException {
        return this.checkTrusted(x509CertificateArray, string, sSLSession, null, false);
    }

    public final void handleTrustStorageUpdate() {
        if (this.acceptedIssuers == null) {
            this.trustedCertificateIndex.reset();
            return;
        }
        this.trustedCertificateIndex.reset(TrustManagerImpl.trustAnchors(this.acceptedIssuers));
    }

    private List<X509Certificate> checkTrusted(X509Certificate[] x509CertificateArray, String string, SSLSession sSLSession, SSLParameters object, boolean bl) throws CertificateException {
        byte[] byArray = null;
        byte[] byArray2 = null;
        String string2 = null;
        if (sSLSession != null) {
            string2 = sSLSession.getPeerHost();
            byArray = TrustManagerImpl.getOcspDataFromSession(sSLSession);
            byArray2 = this.getTlsSctDataFromSession(sSLSession);
        }
        if (sSLSession != null && object != null && "HTTPS".equalsIgnoreCase((String)(object = ((SSLParameters)object).getEndpointIdentificationAlgorithm())) && !(object = this.getHttpsVerifier()).verify(x509CertificateArray, string2, sSLSession)) {
            throw new CertificateException("No subjectAltNames on the certificate match");
        }
        return this.checkTrusted(x509CertificateArray, byArray, byArray2, string, string2, bl);
    }

    private static byte[] getOcspDataFromSession(SSLSession object) {
        List list2 = null;
        if (object instanceof ConscryptSession) {
            ConscryptSession conscryptSession = (ConscryptSession)object;
            list2 = conscryptSession.getStatusResponses();
        } else {
            try {
                Method method = object.getClass().getDeclaredMethod("getStatusResponses", new Class[0]);
                method.setAccessible(true);
                object = method.invoke(object, new Object[0]);
                if (object instanceof List) {
                    list2 = (List)object;
                }
            }
            catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException exception) {
            }
            catch (InvocationTargetException invocationTargetException) {
                throw new RuntimeException(invocationTargetException.getCause());
            }
        }
        if (list2 == null || list2.isEmpty()) {
            return null;
        }
        return list2.get(0);
    }

    private byte[] getTlsSctDataFromSession(SSLSession object) {
        if (object instanceof ConscryptSession) {
            ConscryptSession conscryptSession = (ConscryptSession)object;
            return conscryptSession.getPeerSignedCertificateTimestamp();
        }
        byte[] byArray = null;
        try {
            Method method = object.getClass().getDeclaredMethod("getPeerSignedCertificateTimestamp", new Class[0]);
            method.setAccessible(true);
            object = method.invoke(object, new Object[0]);
            if (object instanceof byte[]) {
                byArray = (byte[])object;
            }
        }
        catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException exception) {
        }
        catch (InvocationTargetException invocationTargetException) {
            throw new RuntimeException(invocationTargetException.getCause());
        }
        return byArray;
    }

    private List<X509Certificate> checkTrusted(X509Certificate[] x509CertificateArray, byte[] byArray, byte[] byArray2, String object, String string, boolean bl) throws CertificateException {
        if (x509CertificateArray == null || x509CertificateArray.length == 0 || object == null || ((String)object).length() == 0) {
            throw new IllegalArgumentException("null or zero-length parameter");
        }
        if (this.err != null) {
            throw new CertificateException(this.err);
        }
        object = new HashSet();
        ArrayList<X509Certificate> arrayList = new ArrayList<X509Certificate>();
        ArrayList<TrustAnchor> arrayList2 = new ArrayList<TrustAnchor>();
        X509Certificate x509Certificate = x509CertificateArray[0];
        TrustAnchor trustAnchor = this.findTrustAnchorBySubjectAndPublicKey(x509Certificate);
        if (trustAnchor != null) {
            arrayList2.add(trustAnchor);
            object.add(trustAnchor.getTrustedCert());
        } else {
            arrayList.add(x509Certificate);
        }
        object.add(x509Certificate);
        return this.checkTrustedRecursive(x509CertificateArray, byArray, byArray2, string, bl, arrayList, arrayList2, (Set<X509Certificate>)object);
    }

    private List<X509Certificate> checkTrustedRecursive(X509Certificate[] x509CertificateArray, byte[] byArray, byte[] byArray2, String string, boolean bl, ArrayList<X509Certificate> arrayList, ArrayList<TrustAnchor> arrayList2, Set<X509Certificate> set) throws CertificateException {
        Serializable serializable;
        X509Certificate x509Certificate;
        CertificateException certificateException = null;
        if (arrayList2.isEmpty()) {
            ArrayList<X509Certificate> arrayList3 = arrayList;
            x509Certificate = arrayList3.get(arrayList3.size() - 1);
        } else {
            ArrayList<TrustAnchor> arrayList4 = arrayList2;
            x509Certificate = arrayList4.get(arrayList4.size() - 1).getTrustedCert();
        }
        this.checkBlocklist(x509Certificate);
        if (x509Certificate.getIssuerDN().equals(x509Certificate.getSubjectDN())) {
            return this.verifyChain(arrayList, arrayList2, string, bl, byArray, byArray2);
        }
        Set<TrustAnchor> set2 = this.findAllTrustAnchorsByIssuerAndSignature(x509Certificate);
        boolean bl2 = false;
        for (TrustAnchor object22 : TrustManagerImpl.sortPotentialAnchors(set2)) {
            X509Certificate x509Certificate2 = object22.getTrustedCert();
            if (set.contains(x509Certificate2)) continue;
            bl2 = true;
            set.add(x509Certificate2);
            arrayList2.add(object22);
            try {
                return this.checkTrustedRecursive(x509CertificateArray, byArray, byArray2, string, bl, arrayList, arrayList2, set);
            }
            catch (CertificateException certificateException2) {
                serializable = certificateException2;
                certificateException = certificateException2;
                ArrayList<TrustAnchor> arrayList5 = arrayList2;
                arrayList5.remove(arrayList5.size() - 1);
                set.remove(x509Certificate2);
            }
        }
        if (!arrayList2.isEmpty()) {
            if (!bl2) {
                return this.verifyChain(arrayList, arrayList2, string, bl, byArray, byArray2);
            }
            throw certificateException;
        }
        for (int i2 = 1; i2 < x509CertificateArray.length; ++i2) {
            X509Certificate x509Certificate3 = x509CertificateArray[i2];
            if (set.contains(x509Certificate3) || !x509Certificate.getIssuerDN().equals(x509Certificate3.getSubjectDN())) continue;
            try {
                x509Certificate3.checkValidity();
                ChainStrengthAnalyzer.checkCert(x509Certificate3);
            }
            catch (CertificateException certificateException3) {
                certificateException = new CertificateException("Unacceptable certificate: " + x509Certificate3.getSubjectX500Principal(), certificateException3);
                continue;
            }
            set.add(x509Certificate3);
            arrayList.add(x509Certificate3);
            try {
                return this.checkTrustedRecursive(x509CertificateArray, byArray, byArray2, string, bl, arrayList, arrayList2, set);
            }
            catch (CertificateException certificateException4) {
                CertificateException certificateException5 = certificateException4;
                certificateException = certificateException4;
                set.remove(x509Certificate3);
                ArrayList<X509Certificate> arrayList6 = arrayList;
                arrayList6.remove(arrayList6.size() - 1);
            }
        }
        Set<TrustAnchor> set3 = this.intermediateIndex.findAllByIssuerAndSignature(x509Certificate);
        for (TrustAnchor trustAnchor : TrustManagerImpl.sortPotentialAnchors(set3)) {
            serializable = trustAnchor.getTrustedCert();
            if (set.contains(serializable)) continue;
            set.add((X509Certificate)serializable);
            arrayList.add((X509Certificate)serializable);
            try {
                return this.checkTrustedRecursive(x509CertificateArray, byArray, byArray2, string, bl, arrayList, arrayList2, set);
            }
            catch (CertificateException certificateException6) {
                certificateException = certificateException6;
                certificateException = certificateException6;
                ArrayList<X509Certificate> arrayList7 = arrayList;
                arrayList7.remove(arrayList7.size() - 1);
                set.remove(serializable);
            }
        }
        if (certificateException != null) {
            throw certificateException;
        }
        CertPath certPath = this.factory.generateCertPath(arrayList);
        throw new CertificateException(new CertPathValidatorException("Trust anchor for certification path not found.", null, certPath, -1));
    }

    private List<X509Certificate> verifyChain(List<X509Certificate> list2, List<TrustAnchor> object, String string, boolean bl, byte[] byArray, byte[] byArray2) throws CertificateException {
        try {
            CertPath certPath = this.factory.generateCertPath(list2);
            if (object.isEmpty()) {
                throw new CertificateException(new CertPathValidatorException("Trust anchor for certification path not found.", null, certPath, -1));
            }
            ArrayList<X509Certificate> arrayList = new ArrayList<X509Certificate>();
            arrayList.addAll(list2);
            Object object3 = object.iterator();
            while (object3.hasNext()) {
                TrustAnchor object22 = (TrustAnchor)object3.next();
                arrayList.add(object22.getTrustedCert());
            }
            if (this.pinManager != null) {
                this.pinManager.checkChainPinning(string, arrayList);
            }
            for (X509Certificate x509Certificate : arrayList) {
                this.checkBlocklist(x509Certificate);
            }
            if (!bl && (this.ctEnabledOverride || string != null && Platform.isCTVerificationRequired(string))) {
                this.checkCT(string, arrayList, byArray, byArray2);
            }
            if (list2.isEmpty()) {
                return arrayList;
            }
            ChainStrengthAnalyzer.check(list2);
            try {
                object3 = new HashSet();
                object3.add(object.get(0));
                PKIXParameters pKIXParameters = new PKIXParameters((Set<TrustAnchor>)object3);
                pKIXParameters.setRevocationEnabled(false);
                object = list2.get(0);
                this.setOcspResponses(pKIXParameters, (X509Certificate)object, byArray);
                pKIXParameters.addCertPathChecker(new ExtendedKeyUsagePKIXCertPathChecker(bl, (X509Certificate)object));
                this.validator.validate(certPath, pKIXParameters);
            }
            catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException) {
                throw new CertificateException("Chain validation failed", invalidAlgorithmParameterException);
            }
            catch (CertPathValidatorException certPathValidatorException) {
                throw new CertificateException("Chain validation failed", certPathValidatorException);
            }
            for (int i2 = 1; i2 < list2.size(); ++i2) {
                this.intermediateIndex.index(list2.get(i2));
            }
            return arrayList;
        }
        catch (CertificateException certificateException) {
            logger.fine("Rejected candidate cert chain due to error: " + certificateException.getMessage());
            throw certificateException;
        }
    }

    private void checkBlocklist(X509Certificate x509Certificate) throws CertificateException {
        if (this.blocklist != null && this.blocklist.isPublicKeyBlockListed(x509Certificate.getPublicKey())) {
            throw new CertificateException("Certificate blocklisted by public key: " + x509Certificate);
        }
    }

    private void checkCT(String string, List<X509Certificate> list2, byte[] object, byte[] byArray) throws CertificateException {
        object = this.ctVerifier.verifySignedCertificateTimestamps(list2, byArray, (byte[])object);
        List<X509Certificate> list3 = list2;
        if (!this.ctPolicy.doesResultConformToPolicy((CTVerificationResult)object, string, list3.toArray(new X509Certificate[list3.size()]))) {
            throw new CertificateException("Certificate chain does not conform to required transparency policy.");
        }
    }

    private void setOcspResponses(PKIXParameters pKIXParameters, X509Certificate x509Certificate, byte[] byArray) {
        if (byArray == null) {
            return;
        }
        PKIXRevocationChecker pKIXRevocationChecker = null;
        ArrayList<PKIXCertPathChecker> arrayList = new ArrayList<PKIXCertPathChecker>(pKIXParameters.getCertPathCheckers());
        for (PKIXCertPathChecker pKIXCertPathChecker : arrayList) {
            if (!(pKIXCertPathChecker instanceof PKIXRevocationChecker)) continue;
            pKIXRevocationChecker = (PKIXRevocationChecker)pKIXCertPathChecker;
            break;
        }
        if (pKIXRevocationChecker == null) {
            try {
                pKIXRevocationChecker = (PKIXRevocationChecker)this.validator.getRevocationChecker();
            }
            catch (UnsupportedOperationException unsupportedOperationException) {
                return;
            }
            arrayList.add(pKIXRevocationChecker);
            pKIXRevocationChecker.setOptions(Collections.singleton(PKIXRevocationChecker.Option.ONLY_END_ENTITY));
        }
        pKIXRevocationChecker.setOcspResponses(Collections.singletonMap(x509Certificate, byArray));
        pKIXParameters.setCertPathCheckers(arrayList);
    }

    private static Collection<TrustAnchor> sortPotentialAnchors(Set<TrustAnchor> collection) {
        if (collection.size() <= 1) {
            return collection;
        }
        collection = new ArrayList<TrustAnchor>(collection);
        Collections.sort(collection, TRUST_ANCHOR_COMPARATOR);
        return collection;
    }

    private Set<TrustAnchor> findAllTrustAnchorsByIssuerAndSignature(X509Certificate iterator2) {
        Set<TrustAnchor> set = this.trustedCertificateIndex.findAllByIssuerAndSignature((X509Certificate)((Object)iterator2));
        if (!set.isEmpty() || this.trustedCertificateStore == null) {
            return set;
        }
        if ((iterator2 = this.trustedCertificateStore.findAllIssuers((X509Certificate)((Object)iterator2))).isEmpty()) {
            return set;
        }
        set = new HashSet<TrustAnchor>(iterator2.size());
        iterator2 = iterator2.iterator();
        while (iterator2.hasNext()) {
            X509Certificate x509Certificate = (X509Certificate)iterator2.next();
            set.add(this.trustedCertificateIndex.index(x509Certificate));
        }
        return set;
    }

    private TrustAnchor findTrustAnchorBySubjectAndPublicKey(X509Certificate x509Certificate) {
        TrustAnchor trustAnchor = this.trustedCertificateIndex.findBySubjectAndPublicKey(x509Certificate);
        if (trustAnchor != null) {
            return trustAnchor;
        }
        if (this.trustedCertificateStore == null) {
            return null;
        }
        if ((x509Certificate = this.trustedCertificateStore.getTrustAnchor(x509Certificate)) != null) {
            return new TrustAnchor(x509Certificate, null);
        }
        return null;
    }

    @Override
    public final X509Certificate[] getAcceptedIssuers() {
        if (this.acceptedIssuers != null) {
            return (X509Certificate[])this.acceptedIssuers.clone();
        }
        return TrustManagerImpl.acceptedIssuers(this.rootKeyStore);
    }

    static synchronized void setDefaultHostnameVerifier(ConscryptHostnameVerifier conscryptHostnameVerifier) {
        defaultHostnameVerifier = conscryptHostnameVerifier;
    }

    static synchronized ConscryptHostnameVerifier getDefaultHostnameVerifier() {
        return defaultHostnameVerifier;
    }

    final void setHostnameVerifier(ConscryptHostnameVerifier conscryptHostnameVerifier) {
        this.hostnameVerifier = conscryptHostnameVerifier;
    }

    final ConscryptHostnameVerifier getHostnameVerifier() {
        return this.hostnameVerifier;
    }

    private ConscryptHostnameVerifier getHttpsVerifier() {
        if (this.hostnameVerifier != null) {
            return this.hostnameVerifier;
        }
        return Platform.getDefaultHostnameVerifier();
    }

    public final void setCTEnabledOverride(boolean bl) {
        this.ctEnabledOverride = bl;
    }

    public final void setCTVerifier(CTVerifier cTVerifier) {
        this.ctVerifier = cTVerifier;
    }

    public final void setCTPolicy(CTPolicy cTPolicy) {
        this.ctPolicy = cTPolicy;
    }

    static class ExtendedKeyUsagePKIXCertPathChecker
    extends PKIXCertPathChecker {
        private static final String EKU_OID = "2.5.29.37";
        private static final String EKU_anyExtendedKeyUsage = "2.5.29.37.0";
        private static final String EKU_clientAuth = "1.3.6.1.5.5.7.3.2";
        private static final String EKU_serverAuth = "1.3.6.1.5.5.7.3.1";
        private static final String EKU_nsSGC = "2.16.840.1.113730.4.1";
        private static final String EKU_msSGC = "1.3.6.1.4.1.311.10.3.3";
        private static final Set<String> SUPPORTED_EXTENSIONS = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList("2.5.29.37")));
        private final boolean clientAuth;
        private final X509Certificate leaf;

        private ExtendedKeyUsagePKIXCertPathChecker(boolean bl, X509Certificate x509Certificate) {
            this.clientAuth = bl;
            this.leaf = x509Certificate;
        }

        @Override
        public void init(boolean bl) throws CertPathValidatorException {
        }

        @Override
        public boolean isForwardCheckingSupported() {
            return true;
        }

        @Override
        public Set<String> getSupportedExtensions() {
            return SUPPORTED_EXTENSIONS;
        }

        @Override
        public void check(Certificate iterator2, Collection<String> collection) throws CertPathValidatorException {
            if (iterator2 != this.leaf) {
                return;
            }
            try {
                iterator2 = this.leaf.getExtendedKeyUsage();
            }
            catch (CertificateParsingException certificateParsingException) {
                throw new CertPathValidatorException(certificateParsingException);
            }
            if (iterator2 == null) {
                return;
            }
            boolean bl = false;
            iterator2 = iterator2.iterator();
            while (iterator2.hasNext()) {
                String string = (String)iterator2.next();
                if (string.equals(EKU_anyExtendedKeyUsage)) {
                    bl = true;
                    break;
                }
                if (this.clientAuth) {
                    if (!string.equals(EKU_clientAuth)) continue;
                    bl = true;
                    break;
                }
                if (string.equals(EKU_serverAuth)) {
                    bl = true;
                    break;
                }
                if (string.equals(EKU_nsSGC)) {
                    bl = true;
                    break;
                }
                if (!string.equals(EKU_msSGC)) continue;
                bl = true;
                break;
            }
            if (bl) {
                collection.remove(EKU_OID);
                return;
            }
            throw new CertPathValidatorException("End-entity certificate does not have a valid extendedKeyUsage.");
        }
    }

    static class TrustAnchorComparator
    implements Comparator<TrustAnchor> {
        private static final CertificatePriorityComparator CERT_COMPARATOR = new CertificatePriorityComparator();

        private TrustAnchorComparator() {
        }

        @Override
        public int compare(TrustAnchor object, TrustAnchor object2) {
            object = ((TrustAnchor)object).getTrustedCert();
            object2 = ((TrustAnchor)object2).getTrustedCert();
            return CERT_COMPARATOR.compare((X509Certificate)object, (X509Certificate)object2);
        }
    }
}


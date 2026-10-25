/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketImpl;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.attribute.PosixFilePermission;
import java.security.AccessController;
import java.security.AlgorithmParameters;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PrivilegedAction;
import java.security.Provider;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Set;
import javax.crypto.spec.GCMParameterSpec;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509ExtendedTrustManager;
import javax.net.ssl.X509TrustManager;
import org.conscrypt.AbstractConscryptSocket;
import org.conscrypt.CertBlocklist;
import org.conscrypt.Conscrypt;
import org.conscrypt.ConscryptCertStore;
import org.conscrypt.ConscryptEngine;
import org.conscrypt.ConscryptEngineSocket;
import org.conscrypt.ConscryptFileDescriptorSocket;
import org.conscrypt.ConscryptHostnameVerifier;
import org.conscrypt.ExternalSession;
import org.conscrypt.GCMParameters;
import org.conscrypt.Java7ExtendedSSLSession;
import org.conscrypt.Java8EngineSocket;
import org.conscrypt.Java8FileDescriptorSocket;
import org.conscrypt.Java8PlatformUtil;
import org.conscrypt.Java9PlatformUtil;
import org.conscrypt.OkHostnameVerifier;
import org.conscrypt.OpenSSLKey;
import org.conscrypt.OpenSSLSocketFactoryImpl;
import org.conscrypt.SSLParametersImpl;
import org.conscrypt.ct.CTLogStore;
import org.conscrypt.ct.CTPolicy;
import sun.security.x509.AlgorithmId;

final class Platform {
    private static final int JAVA_VERSION = Platform.javaVersion0();
    private static final Method GET_CURVE_NAME_METHOD;

    private Platform() {
    }

    static void setup() {
    }

    static File createTempFile(String string, String string2, File file) throws IOException {
        if (file == null) {
            throw new NullPointerException();
        }
        long l2 = System.currentTimeMillis();
        string = new File(string).getName();
        IOException iOException = null;
        for (int i2 = 0; i2 < 10000; ++i2) {
            File file2;
            String string3 = String.format(Locale.US, "%s%d%04d%s", string, l2, i2, string2);
            if (!string3.equals((file2 = new File(file, string3)).getName())) {
                throw new IOException("Unable to create temporary file: " + file2);
            }
            try {
                if (!file2.createNewFile()) continue;
                return file2.getCanonicalFile();
            }
            catch (IOException iOException2) {
                iOException = iOException2;
                iOException = iOException2;
            }
        }
        if (iOException != null) {
            throw iOException;
        }
        throw new IOException("Unable to create temporary file");
    }

    static String getDefaultProviderName() {
        return "Conscrypt";
    }

    static boolean provideTrustManagerByDefault() {
        return true;
    }

    static boolean canExecuteExecutable(File file) throws IOException {
        EnumSet<PosixFilePermission> enumSet;
        if (file.canExecute()) {
            return true;
        }
        Set<PosixFilePermission> set = Files.getPosixFilePermissions(file.toPath(), new LinkOption[0]);
        if (set.containsAll(enumSet = EnumSet.of(PosixFilePermission.OWNER_EXECUTE, PosixFilePermission.GROUP_EXECUTE, PosixFilePermission.OTHERS_EXECUTE))) {
            return false;
        }
        set = EnumSet.copyOf(set);
        set.addAll(enumSet);
        Files.setPosixFilePermissions(file.toPath(), set);
        return file.canExecute();
    }

    static FileDescriptor getFileDescriptor(Socket object) {
        Object object2;
        try {
            object2 = ((Socket)object).getChannel();
            if (object2 != null) {
                Field field = object2.getClass().getDeclaredField("fd");
                field.setAccessible(true);
                return (FileDescriptor)field.get(object2);
            }
        }
        catch (Exception exception) {}
        try {
            object2 = Socket.class.getDeclaredField("impl");
            ((Field)object2).setAccessible(true);
            Object object3 = ((Field)object2).get(object);
            object = SocketImpl.class.getDeclaredField("fd");
            ((Field)object).setAccessible(true);
            return (FileDescriptor)((Field)object).get(object3);
        }
        catch (Exception exception) {
            throw new RuntimeException("Can't get FileDescriptor from socket", exception);
        }
    }

    static FileDescriptor getFileDescriptorFromSSLSocket(AbstractConscryptSocket abstractConscryptSocket) {
        return Platform.getFileDescriptor(abstractConscryptSocket);
    }

    static String getCurveName(ECParameterSpec eCParameterSpec) {
        if (GET_CURVE_NAME_METHOD != null) {
            try {
                return (String)GET_CURVE_NAME_METHOD.invoke(eCParameterSpec, new Object[0]);
            }
            catch (Exception exception) {}
        }
        return null;
    }

    static void setCurveName(ECParameterSpec eCParameterSpec, String string) {
    }

    static void setSocketWriteTimeout(Socket socket, long l2) throws SocketException {
    }

    static void setSSLParameters(SSLParameters sSLParameters, SSLParametersImpl sSLParametersImpl, AbstractConscryptSocket abstractConscryptSocket) {
        if (JAVA_VERSION >= 9) {
            Java9PlatformUtil.setSSLParameters(sSLParameters, sSLParametersImpl, abstractConscryptSocket);
            return;
        }
        if (JAVA_VERSION >= 8) {
            Java8PlatformUtil.setSSLParameters(sSLParameters, sSLParametersImpl, abstractConscryptSocket);
            return;
        }
        sSLParametersImpl.setEndpointIdentificationAlgorithm(sSLParameters.getEndpointIdentificationAlgorithm());
    }

    static void getSSLParameters(SSLParameters sSLParameters, SSLParametersImpl sSLParametersImpl, AbstractConscryptSocket abstractConscryptSocket) {
        if (JAVA_VERSION >= 9) {
            Java9PlatformUtil.getSSLParameters(sSLParameters, sSLParametersImpl, abstractConscryptSocket);
            return;
        }
        if (JAVA_VERSION >= 8) {
            Java8PlatformUtil.getSSLParameters(sSLParameters, sSLParametersImpl, abstractConscryptSocket);
            return;
        }
        sSLParameters.setEndpointIdentificationAlgorithm(sSLParametersImpl.getEndpointIdentificationAlgorithm());
    }

    static void setSSLParameters(SSLParameters sSLParameters, SSLParametersImpl sSLParametersImpl, ConscryptEngine conscryptEngine) {
        if (JAVA_VERSION >= 9) {
            Java9PlatformUtil.setSSLParameters(sSLParameters, sSLParametersImpl, conscryptEngine);
            return;
        }
        if (JAVA_VERSION >= 8) {
            Java8PlatformUtil.setSSLParameters(sSLParameters, sSLParametersImpl, conscryptEngine);
            return;
        }
        sSLParametersImpl.setEndpointIdentificationAlgorithm(sSLParameters.getEndpointIdentificationAlgorithm());
    }

    static void getSSLParameters(SSLParameters sSLParameters, SSLParametersImpl sSLParametersImpl, ConscryptEngine conscryptEngine) {
        if (JAVA_VERSION >= 9) {
            Java9PlatformUtil.getSSLParameters(sSLParameters, sSLParametersImpl, conscryptEngine);
            return;
        }
        if (JAVA_VERSION >= 8) {
            Java8PlatformUtil.getSSLParameters(sSLParameters, sSLParametersImpl, conscryptEngine);
            return;
        }
        sSLParameters.setEndpointIdentificationAlgorithm(sSLParametersImpl.getEndpointIdentificationAlgorithm());
    }

    static void setEndpointIdentificationAlgorithm(SSLParameters sSLParameters, String string) {
        sSLParameters.setEndpointIdentificationAlgorithm(string);
    }

    static String getEndpointIdentificationAlgorithm(SSLParameters sSLParameters) {
        return sSLParameters.getEndpointIdentificationAlgorithm();
    }

    static void checkClientTrusted(X509TrustManager x509TrustManager, X509Certificate[] x509CertificateArray, String string, AbstractConscryptSocket abstractConscryptSocket) throws CertificateException {
        if (x509TrustManager instanceof X509ExtendedTrustManager) {
            x509TrustManager = (X509ExtendedTrustManager)x509TrustManager;
            ((X509ExtendedTrustManager)x509TrustManager).checkClientTrusted(x509CertificateArray, string, abstractConscryptSocket);
            return;
        }
        x509TrustManager.checkClientTrusted(x509CertificateArray, string);
    }

    static void checkServerTrusted(X509TrustManager x509TrustManager, X509Certificate[] x509CertificateArray, String string, AbstractConscryptSocket abstractConscryptSocket) throws CertificateException {
        if (x509TrustManager instanceof X509ExtendedTrustManager) {
            x509TrustManager = (X509ExtendedTrustManager)x509TrustManager;
            ((X509ExtendedTrustManager)x509TrustManager).checkServerTrusted(x509CertificateArray, string, abstractConscryptSocket);
            return;
        }
        x509TrustManager.checkServerTrusted(x509CertificateArray, string);
    }

    static void checkClientTrusted(X509TrustManager x509TrustManager, X509Certificate[] x509CertificateArray, String string, ConscryptEngine conscryptEngine) throws CertificateException {
        if (x509TrustManager instanceof X509ExtendedTrustManager) {
            x509TrustManager = (X509ExtendedTrustManager)x509TrustManager;
            ((X509ExtendedTrustManager)x509TrustManager).checkClientTrusted(x509CertificateArray, string, conscryptEngine);
            return;
        }
        x509TrustManager.checkClientTrusted(x509CertificateArray, string);
    }

    static void checkServerTrusted(X509TrustManager x509TrustManager, X509Certificate[] x509CertificateArray, String string, ConscryptEngine conscryptEngine) throws CertificateException {
        if (x509TrustManager instanceof X509ExtendedTrustManager) {
            x509TrustManager = (X509ExtendedTrustManager)x509TrustManager;
            ((X509ExtendedTrustManager)x509TrustManager).checkServerTrusted(x509CertificateArray, string, conscryptEngine);
            return;
        }
        x509TrustManager.checkServerTrusted(x509CertificateArray, string);
    }

    static OpenSSLKey wrapRsaKey(PrivateKey privateKey) {
        return null;
    }

    static void logEvent(String string) {
    }

    static boolean isSniEnabledByDefault() {
        return true;
    }

    static SSLEngine wrapEngine(ConscryptEngine conscryptEngine) {
        if (JAVA_VERSION >= 8) {
            return Java8PlatformUtil.wrapEngine(conscryptEngine);
        }
        return conscryptEngine;
    }

    static SSLEngine unwrapEngine(SSLEngine sSLEngine) {
        if (JAVA_VERSION >= 8) {
            return Java8PlatformUtil.unwrapEngine(sSLEngine);
        }
        return sSLEngine;
    }

    static ConscryptEngineSocket createEngineSocket(SSLParametersImpl sSLParametersImpl) throws IOException {
        if (JAVA_VERSION >= 8) {
            return new Java8EngineSocket(sSLParametersImpl);
        }
        return new ConscryptEngineSocket(sSLParametersImpl);
    }

    static ConscryptEngineSocket createEngineSocket(String string, int n2, SSLParametersImpl sSLParametersImpl) throws IOException {
        if (JAVA_VERSION >= 8) {
            return new Java8EngineSocket(string, n2, sSLParametersImpl);
        }
        return new ConscryptEngineSocket(string, n2, sSLParametersImpl);
    }

    static ConscryptEngineSocket createEngineSocket(InetAddress inetAddress, int n2, SSLParametersImpl sSLParametersImpl) throws IOException {
        if (JAVA_VERSION >= 8) {
            return new Java8EngineSocket(inetAddress, n2, sSLParametersImpl);
        }
        return new ConscryptEngineSocket(inetAddress, n2, sSLParametersImpl);
    }

    static ConscryptEngineSocket createEngineSocket(String string, int n2, InetAddress inetAddress, int n3, SSLParametersImpl sSLParametersImpl) throws IOException {
        if (JAVA_VERSION >= 8) {
            return new Java8EngineSocket(string, n2, inetAddress, n3, sSLParametersImpl);
        }
        return new ConscryptEngineSocket(string, n2, inetAddress, n3, sSLParametersImpl);
    }

    static ConscryptEngineSocket createEngineSocket(InetAddress inetAddress, int n2, InetAddress inetAddress2, int n3, SSLParametersImpl sSLParametersImpl) throws IOException {
        if (JAVA_VERSION >= 8) {
            return new Java8EngineSocket(inetAddress, n2, inetAddress2, n3, sSLParametersImpl);
        }
        return new ConscryptEngineSocket(inetAddress, n2, inetAddress2, n3, sSLParametersImpl);
    }

    static ConscryptEngineSocket createEngineSocket(Socket socket, String string, int n2, boolean bl, SSLParametersImpl sSLParametersImpl) throws IOException {
        if (JAVA_VERSION >= 8) {
            return new Java8EngineSocket(socket, string, n2, bl, sSLParametersImpl);
        }
        return new ConscryptEngineSocket(socket, string, n2, bl, sSLParametersImpl);
    }

    static ConscryptFileDescriptorSocket createFileDescriptorSocket(SSLParametersImpl sSLParametersImpl) throws IOException {
        if (JAVA_VERSION >= 8) {
            return new Java8FileDescriptorSocket(sSLParametersImpl);
        }
        return new ConscryptFileDescriptorSocket(sSLParametersImpl);
    }

    static ConscryptFileDescriptorSocket createFileDescriptorSocket(String string, int n2, SSLParametersImpl sSLParametersImpl) throws IOException {
        if (JAVA_VERSION >= 8) {
            return new Java8FileDescriptorSocket(string, n2, sSLParametersImpl);
        }
        return new ConscryptFileDescriptorSocket(string, n2, sSLParametersImpl);
    }

    static ConscryptFileDescriptorSocket createFileDescriptorSocket(InetAddress inetAddress, int n2, SSLParametersImpl sSLParametersImpl) throws IOException {
        if (JAVA_VERSION >= 8) {
            return new Java8FileDescriptorSocket(inetAddress, n2, sSLParametersImpl);
        }
        return new ConscryptFileDescriptorSocket(inetAddress, n2, sSLParametersImpl);
    }

    static ConscryptFileDescriptorSocket createFileDescriptorSocket(String string, int n2, InetAddress inetAddress, int n3, SSLParametersImpl sSLParametersImpl) throws IOException {
        if (JAVA_VERSION >= 8) {
            return new Java8FileDescriptorSocket(string, n2, inetAddress, n3, sSLParametersImpl);
        }
        return new ConscryptFileDescriptorSocket(string, n2, inetAddress, n3, sSLParametersImpl);
    }

    static ConscryptFileDescriptorSocket createFileDescriptorSocket(InetAddress inetAddress, int n2, InetAddress inetAddress2, int n3, SSLParametersImpl sSLParametersImpl) throws IOException {
        if (JAVA_VERSION >= 8) {
            return new Java8FileDescriptorSocket(inetAddress, n2, inetAddress2, n3, sSLParametersImpl);
        }
        return new ConscryptFileDescriptorSocket(inetAddress, n2, inetAddress2, n3, sSLParametersImpl);
    }

    static ConscryptFileDescriptorSocket createFileDescriptorSocket(Socket socket, String string, int n2, boolean bl, SSLParametersImpl sSLParametersImpl) throws IOException {
        if (JAVA_VERSION >= 8) {
            return new Java8FileDescriptorSocket(socket, string, n2, bl, sSLParametersImpl);
        }
        return new ConscryptFileDescriptorSocket(socket, string, n2, bl, sSLParametersImpl);
    }

    static SSLSocketFactory wrapSocketFactoryIfNeeded(OpenSSLSocketFactoryImpl openSSLSocketFactoryImpl) {
        return openSSLSocketFactoryImpl;
    }

    static GCMParameters fromGCMParameterSpec(AlgorithmParameterSpec algorithmParameterSpec) {
        if (algorithmParameterSpec instanceof GCMParameterSpec) {
            algorithmParameterSpec = (GCMParameterSpec)algorithmParameterSpec;
            return new GCMParameters(((GCMParameterSpec)algorithmParameterSpec).getTLen(), ((GCMParameterSpec)algorithmParameterSpec).getIV());
        }
        return null;
    }

    static AlgorithmParameterSpec fromGCMParameters(AlgorithmParameters algorithmParameters) {
        try {
            return algorithmParameters.getParameterSpec(GCMParameterSpec.class);
        }
        catch (InvalidParameterSpecException invalidParameterSpecException) {
            return null;
        }
    }

    static AlgorithmParameterSpec toGCMParameterSpec(int n2, byte[] byArray) {
        return new GCMParameterSpec(n2, byArray);
    }

    static Object closeGuardGet() {
        return null;
    }

    static void closeGuardOpen(Object object, String string) {
    }

    static void closeGuardClose(Object object) {
    }

    static void closeGuardWarnIfOpen(Object object) {
    }

    static void blockGuardOnNetwork() {
    }

    static String oidToAlgorithmName(String string) {
        try {
            return AlgorithmId.get(string).getName();
        }
        catch (Exception exception) {
            return string;
        }
        catch (IllegalAccessError illegalAccessError) {
            return string;
        }
    }

    static SSLSession wrapSSLSession(ExternalSession externalSession) {
        if (JAVA_VERSION >= 8) {
            return Java8PlatformUtil.wrapSSLSession(externalSession);
        }
        return new Java7ExtendedSSLSession(externalSession);
    }

    public static String getOriginalHostNameFromInetAddress(InetAddress inetAddress) {
        try {
            Object object = InetAddress.class.getDeclaredMethod("holder", new Class[0]);
            ((Method)object).setAccessible(true);
            Method method = Class.forName("java.net.InetAddress$InetAddressHolder").getDeclaredMethod("getOriginalHostName", new Class[0]);
            method.setAccessible(true);
            object = (String)method.invoke(((Method)object).invoke(inetAddress, new Object[0]), new Object[0]);
            if (object == null) {
                return inetAddress.getHostAddress();
            }
            return object;
        }
        catch (InvocationTargetException invocationTargetException) {
            throw new RuntimeException("Failed to get originalHostName", invocationTargetException);
        }
        catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException reflectiveOperationException) {
            return inetAddress.getHostAddress();
        }
    }

    static String getHostStringFromInetSocketAddress(InetSocketAddress inetSocketAddress) {
        return inetSocketAddress.getHostString();
    }

    static boolean supportsX509ExtendedTrustManager() {
        return true;
    }

    static boolean isCTVerificationRequired(String iterator2) {
        if (iterator2 == null) {
            return false;
        }
        String string = Security.getProperty("conscrypt.ct.enable");
        if (string == null || !Boolean.valueOf(string.toLowerCase()).booleanValue()) {
            return false;
        }
        iterator2 = Arrays.asList(((String)((Object)iterator2)).split("\\."));
        Collections.reverse(iterator2);
        boolean bl = false;
        StringBuilder stringBuilder = new StringBuilder("conscrypt.ct.enforce");
        iterator2 = iterator2.iterator();
        while (iterator2.hasNext()) {
            String string2 = (String)iterator2.next();
            string = Security.getProperty(stringBuilder + ".*");
            if (string != null) {
                bl = Boolean.valueOf(string.toLowerCase());
            }
            stringBuilder.append(".").append(string2);
        }
        string = Security.getProperty(stringBuilder.toString());
        if (string != null) {
            bl = Boolean.valueOf(string.toLowerCase());
        }
        return bl;
    }

    static boolean supportsConscryptCertStore() {
        return false;
    }

    static KeyStore getDefaultCertKeyStore() throws KeyStoreException {
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        try {
            keyStore.load(null, null);
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        catch (IOException | CertificateException exception) {}
        Provider[] providerArray = Security.getProviders("TrustManagerFactory.PKIX");
        Provider[] providerArray2 = providerArray;
        providerArray2 = providerArray;
        int n2 = providerArray.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            Provider provider = providerArray2[i2];
            if (Conscrypt.isConscrypt(provider)) continue;
            try {
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("PKIX", provider);
                trustManagerFactory.init((KeyStore)null);
                TrustManager[] trustManagerArray = trustManagerFactory.getTrustManagers();
                if (trustManagerArray.length <= 0) continue;
                int n3 = 1;
                TrustManager[] trustManagerArray2 = trustManagerArray;
                int n4 = trustManagerArray.length;
                for (int i3 = 0; i3 < n4; ++i3) {
                    TrustManager trustManager = trustManagerArray2[i3];
                    if (!(trustManager instanceof X509TrustManager)) continue;
                    X509TrustManager x509TrustManager = (X509TrustManager)trustManager;
                    for (X509Certificate x509Certificate : x509TrustManager.getAcceptedIssuers()) {
                        keyStore.setCertificateEntry(Integer.toString(n3++), x509Certificate);
                    }
                }
                if (n3 <= true) continue;
                break;
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {}
        }
        return keyStore;
    }

    static ConscryptCertStore newDefaultCertStore() {
        return null;
    }

    static CertBlocklist newDefaultBlocklist() {
        return null;
    }

    static CTLogStore newDefaultLogStore() {
        return null;
    }

    static CTPolicy newDefaultPolicy(CTLogStore cTLogStore) {
        return null;
    }

    static boolean serverNamePermitted(SSLParametersImpl sSLParametersImpl, String string) {
        if (JAVA_VERSION >= 8) {
            return Java8PlatformUtil.serverNamePermitted(sSLParametersImpl, string);
        }
        return true;
    }

    private static boolean isAndroid() {
        boolean bl;
        try {
            Class.forName("android.app.Application", false, Platform.getSystemClassLoader());
            bl = true;
        }
        catch (Throwable throwable) {
            bl = false;
        }
        return bl;
    }

    static int javaVersion() {
        return JAVA_VERSION;
    }

    private static int javaVersion0() {
        int n2 = Platform.isAndroid() ? 6 : Platform.majorVersionFromJavaSpecificationVersion();
        return n2;
    }

    private static int majorVersionFromJavaSpecificationVersion() {
        return Platform.majorVersion(System.getProperty("java.specification.version", "1.6"));
    }

    private static int majorVersion(String stringArray) {
        stringArray = stringArray.split("\\.", -1);
        int[] nArray = new int[stringArray.length];
        for (int i2 = 0; i2 < stringArray.length; ++i2) {
            nArray[i2] = Integer.parseInt(stringArray[i2]);
        }
        if (nArray[0] == 1) {
            assert (nArray[1] >= 6);
            return nArray[1];
        }
        return nArray[0];
    }

    private static ClassLoader getSystemClassLoader() {
        if (System.getSecurityManager() == null) {
            return ClassLoader.getSystemClassLoader();
        }
        return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>(){

            @Override
            public ClassLoader run() {
                return ClassLoader.getSystemClassLoader();
            }
        });
    }

    public static ConscryptHostnameVerifier getDefaultHostnameVerifier() {
        return OkHostnameVerifier.strictInstance();
    }

    static {
        Method method = null;
        try {
            method = ECParameterSpec.class.getDeclaredMethod("getCurveName", new Class[0]);
            method.setAccessible(true);
        }
        catch (Exception exception) {}
        GET_CURVE_NAME_METHOD = method;
    }
}


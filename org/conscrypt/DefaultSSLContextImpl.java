/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.SecureRandom;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import org.conscrypt.NativeCrypto;
import org.conscrypt.OpenSSLContextImpl;

public class DefaultSSLContextImpl
extends OpenSSLContextImpl {
    private static KeyManager[] KEY_MANAGERS;
    private static TrustManager[] TRUST_MANAGERS;

    private DefaultSSLContextImpl(String[] stringArray) throws GeneralSecurityException, IOException {
        super(stringArray, true);
    }

    KeyManager[] getKeyManagers() throws GeneralSecurityException, IOException {
        if (KEY_MANAGERS != null) {
            return KEY_MANAGERS;
        }
        Object object = System.getProperty("javax.net.ssl.keyStore");
        if (object == null) {
            return null;
        }
        Object object2 = System.getProperty("javax.net.ssl.keyStorePassword");
        object2 = object2 == null ? null : ((String)object2).toCharArray();
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        InputStream inputStream2 = null;
        try {
            inputStream2 = new BufferedInputStream(new FileInputStream((String)object));
            keyStore.load(inputStream2, (char[])object2);
        }
        catch (Throwable throwable) {
            if (inputStream2 != null) {
                inputStream2.close();
            }
            throw throwable;
        }
        inputStream2.close();
        object = KeyManagerFactory.getDefaultAlgorithm();
        object = KeyManagerFactory.getInstance((String)object);
        ((KeyManagerFactory)object).init(keyStore, (char[])object2);
        KEY_MANAGERS = ((KeyManagerFactory)object).getKeyManagers();
        return KEY_MANAGERS;
    }

    TrustManager[] getTrustManagers() throws GeneralSecurityException, IOException {
        if (TRUST_MANAGERS != null) {
            return TRUST_MANAGERS;
        }
        Object object = System.getProperty("javax.net.ssl.trustStore");
        if (object == null) {
            return null;
        }
        Object object2 = System.getProperty("javax.net.ssl.trustStorePassword");
        object2 = object2 == null ? null : ((String)object2).toCharArray();
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        InputStream inputStream2 = null;
        try {
            inputStream2 = new BufferedInputStream(new FileInputStream((String)object));
            keyStore.load(inputStream2, (char[])object2);
        }
        catch (Throwable throwable) {
            if (inputStream2 != null) {
                inputStream2.close();
            }
            throw throwable;
        }
        inputStream2.close();
        object = TrustManagerFactory.getDefaultAlgorithm();
        object = TrustManagerFactory.getInstance((String)object);
        ((TrustManagerFactory)object).init(keyStore);
        TRUST_MANAGERS = ((TrustManagerFactory)object).getTrustManagers();
        return TRUST_MANAGERS;
    }

    @Override
    public void engineInit(KeyManager[] keyManagerArray, TrustManager[] trustManagerArray, SecureRandom secureRandom) throws KeyManagementException {
        throw new KeyManagementException("Do not init() the default SSLContext ");
    }

    public static final class TLSv12
    extends DefaultSSLContextImpl {
        public TLSv12() throws GeneralSecurityException, IOException {
            super(NativeCrypto.TLSV12_PROTOCOLS);
        }
    }

    public static final class TLSv13
    extends DefaultSSLContextImpl {
        public TLSv13() throws GeneralSecurityException, IOException {
            super(NativeCrypto.TLSV13_PROTOCOLS);
        }
    }
}


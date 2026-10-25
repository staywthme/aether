/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactorySpi;
import javax.net.ssl.ManagerFactoryParameters;
import org.conscrypt.EmptyArray;
import org.conscrypt.KeyManagerImpl;
import org.conscrypt.io.IoUtils;

public class KeyManagerFactoryImpl
extends KeyManagerFactorySpi {
    private KeyStore keyStore;
    private char[] pwd;

    @Override
    protected void engineInit(KeyStore object, char[] object2) throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException {
        if (object != null) {
            this.keyStore = object;
            if (object2 != null) {
                this.pwd = (char[])object2.clone();
                return;
            }
            this.pwd = EmptyArray.CHAR;
            return;
        }
        this.keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        object = System.getProperty("javax.net.ssl.keyStore");
        if (object == null || ((String)object).equalsIgnoreCase("NONE") || ((String)object).isEmpty()) {
            try {
                this.keyStore.load(null, null);
                return;
            }
            catch (IOException iOException) {
                throw new KeyStoreException(iOException);
            }
            catch (CertificateException certificateException) {
                throw new KeyStoreException(certificateException);
            }
        }
        String string = System.getProperty("javax.net.ssl.keyStorePassword");
        object2 = string;
        this.pwd = string == null ? EmptyArray.CHAR : ((String)object2).toCharArray();
        object2 = null;
        try {
            object2 = new FileInputStream(new File((String)object));
            this.keyStore.load((InputStream)object2, this.pwd);
            return;
        }
        catch (FileNotFoundException fileNotFoundException) {
            throw new KeyStoreException(fileNotFoundException);
        }
        catch (IOException iOException) {
            throw new KeyStoreException(iOException);
        }
        catch (CertificateException certificateException) {
            throw new KeyStoreException(certificateException);
        }
        finally {
            IoUtils.closeQuietly((Closeable)object2);
        }
    }

    @Override
    protected void engineInit(ManagerFactoryParameters managerFactoryParameters) throws InvalidAlgorithmParameterException {
        throw new InvalidAlgorithmParameterException("ManagerFactoryParameters not supported");
    }

    @Override
    protected KeyManager[] engineGetKeyManagers() {
        if (this.keyStore == null) {
            throw new IllegalStateException("KeyManagerFactory is not initialized");
        }
        return new KeyManager[]{new KeyManagerImpl(this.keyStore, this.pwd)};
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Security;
import java.security.Signature;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import org.conscrypt.Conscrypt;

final class CryptoUpcalls {
    private static final Logger logger = Logger.getLogger(CryptoUpcalls.class.getName());

    private CryptoUpcalls() {
    }

    private static ArrayList<Provider> getExternalProviders(String string) {
        ArrayList<Provider> arrayList = new ArrayList<Provider>(1);
        for (Provider provider : Security.getProviders(string)) {
            if (Conscrypt.isConscrypt(provider)) continue;
            arrayList.add(provider);
        }
        if (arrayList.isEmpty()) {
            logger.warning("Could not find external provider for algorithm: " + string);
        }
        return arrayList;
    }

    static byte[] ecSignDigestWithPrivateKey(PrivateKey privateKey, byte[] byArray) {
        String string = privateKey.getAlgorithm();
        if (!"EC".equals(string)) {
            throw new RuntimeException("Unexpected key type: " + privateKey.toString());
        }
        return CryptoUpcalls.signDigestWithPrivateKey(privateKey, byArray, "NONEwithECDSA");
    }

    private static byte[] signDigestWithPrivateKey(PrivateKey privateKey, byte[] byArray, String string) {
        Object object;
        try {
            object = Signature.getInstance(string);
            ((Signature)object).initSign(privateKey);
            if (Conscrypt.isConscrypt(((Signature)object).getProvider())) {
                object = null;
            }
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            logger.warning("Unsupported signature algorithm: " + string);
            return null;
        }
        catch (InvalidKeyException invalidKeyException) {
            logger.warning("Preferred provider doesn't support key:");
            invalidKeyException.printStackTrace();
            object = null;
        }
        if (object == null) {
            Object object2 = CryptoUpcalls.getExternalProviders("Signature." + string);
            RuntimeException runtimeException = null;
            object2 = ((ArrayList)object2).iterator();
            while (object2.hasNext()) {
                object = (Provider)object2.next();
                try {
                    object = Signature.getInstance(string, (Provider)object);
                    ((Signature)object).initSign(privateKey);
                    break;
                }
                catch (InvalidKeyException | NoSuchAlgorithmException generalSecurityException) {
                    object = null;
                }
                catch (RuntimeException runtimeException2) {
                    object = null;
                    if (runtimeException != null) continue;
                    runtimeException = runtimeException2;
                }
            }
            if (object == null) {
                if (runtimeException != null) {
                    throw runtimeException;
                }
                logger.warning("Could not find provider for algorithm: " + string);
                return null;
            }
        }
        try {
            ((Signature)object).update(byArray);
            return ((Signature)object).sign();
        }
        catch (Exception exception) {
            logger.log(Level.WARNING, "Exception while signing message with " + privateKey.getAlgorithm() + " private key:", exception);
            return null;
        }
    }

    static byte[] rsaSignDigestWithPrivateKey(PrivateKey privateKey, int n2, byte[] byArray) {
        return CryptoUpcalls.rsaOpWithPrivateKey(privateKey, n2, 1, byArray);
    }

    static byte[] rsaDecryptWithPrivateKey(PrivateKey privateKey, int n2, byte[] byArray) {
        return CryptoUpcalls.rsaOpWithPrivateKey(privateKey, n2, 2, byArray);
    }

    private static byte[] rsaOpWithPrivateKey(PrivateKey privateKey, int n2, int n3, byte[] byArray) {
        String string;
        Object object = privateKey.getAlgorithm();
        if (!"RSA".equals(object)) {
            logger.warning("Unexpected key type: " + (String)object);
            return null;
        }
        switch (n2) {
            case 1: {
                string = "PKCS1Padding";
                break;
            }
            case 3: {
                string = "NoPadding";
                break;
            }
            case 4: {
                string = "OAEPPadding";
                break;
            }
            default: {
                logger.warning("Unsupported OpenSSL/BoringSSL padding: " + n2);
                return null;
            }
        }
        string = "RSA/ECB/" + string;
        try {
            object = Cipher.getInstance(string);
            ((Cipher)object).init(n3, privateKey);
            if (Conscrypt.isConscrypt(((Cipher)object).getProvider())) {
                object = null;
            }
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            logger.warning("Unsupported cipher algorithm: " + string);
            return null;
        }
        catch (NoSuchPaddingException noSuchPaddingException) {
            logger.warning("Unsupported cipher algorithm: " + string);
            return null;
        }
        catch (InvalidKeyException invalidKeyException) {
            logger.log(Level.WARNING, "Preferred provider doesn't support key:", invalidKeyException);
            object = null;
        }
        if (object == null) {
            Object object2 = CryptoUpcalls.getExternalProviders("Cipher." + string);
            object2 = ((ArrayList)object2).iterator();
            while (object2.hasNext()) {
                object = (Provider)object2.next();
                try {
                    object = Cipher.getInstance(string, (Provider)object);
                    ((Cipher)object).init(n3, privateKey);
                    break;
                }
                catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                    object = null;
                }
                catch (InvalidKeyException invalidKeyException) {
                    object = null;
                }
                catch (NoSuchPaddingException noSuchPaddingException) {
                    object = null;
                }
            }
            if (object == null) {
                logger.warning("Could not find provider for algorithm: " + string);
                return null;
            }
        }
        try {
            return ((Cipher)object).doFinal(byArray);
        }
        catch (Exception exception) {
            logger.log(Level.WARNING, "Exception while decrypting message with " + privateKey.getAlgorithm() + " private key using " + string + ":", exception);
            return null;
        }
    }
}


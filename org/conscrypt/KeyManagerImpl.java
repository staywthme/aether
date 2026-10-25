/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.io.Serializable;
import java.net.Socket;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.X509ExtendedKeyManager;

class KeyManagerImpl
extends X509ExtendedKeyManager {
    private final HashMap<String, KeyStore.PrivateKeyEntry> hash = new HashMap();

    KeyManagerImpl(KeyStore keyStore, char[] cArray) {
        Enumeration<String> enumeration;
        try {
            enumeration = keyStore.aliases();
        }
        catch (KeyStoreException keyStoreException) {
            return;
        }
        while (enumeration.hasMoreElements()) {
            String string = enumeration.nextElement();
            try {
                Object object;
                if (!keyStore.entryInstanceOf(string, KeyStore.PrivateKeyEntry.class)) continue;
                try {
                    object = (KeyStore.PrivateKeyEntry)keyStore.getEntry(string, new KeyStore.PasswordProtection(cArray));
                }
                catch (UnsupportedOperationException unsupportedOperationException) {
                    object = (PrivateKey)keyStore.getKey(string, cArray);
                    Certificate[] certificateArray = keyStore.getCertificateChain(string);
                    object = new KeyStore.PrivateKeyEntry((PrivateKey)object, certificateArray);
                }
                this.hash.put(string, (KeyStore.PrivateKeyEntry)object);
            }
            catch (KeyStoreException keyStoreException) {
            }
            catch (UnrecoverableEntryException unrecoverableEntryException) {
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {}
        }
    }

    @Override
    public String chooseClientAlias(String[] stringArray, Principal[] principalArray, Socket socket) {
        if ((stringArray = this.chooseAlias(stringArray, principalArray)) == null) {
            return null;
        }
        return stringArray[0];
    }

    @Override
    public String chooseServerAlias(String stringArray, Principal[] principalArray, Socket socket) {
        if ((stringArray = this.chooseAlias(new String[]{stringArray}, principalArray)) == null) {
            return null;
        }
        return stringArray[0];
    }

    @Override
    public X509Certificate[] getCertificateChain(String certificateArray) {
        if (certificateArray == null) {
            return null;
        }
        if (this.hash.containsKey(certificateArray) && (certificateArray = this.hash.get(certificateArray).getCertificateChain())[0] instanceof X509Certificate) {
            X509Certificate[] x509CertificateArray = new X509Certificate[certificateArray.length];
            for (int i2 = 0; i2 < certificateArray.length; ++i2) {
                x509CertificateArray[i2] = (X509Certificate)certificateArray[i2];
            }
            return x509CertificateArray;
        }
        return null;
    }

    @Override
    public String[] getClientAliases(String string, Principal[] principalArray) {
        return this.chooseAlias(new String[]{string}, principalArray);
    }

    @Override
    public String[] getServerAliases(String string, Principal[] principalArray) {
        return this.chooseAlias(new String[]{string}, principalArray);
    }

    @Override
    public PrivateKey getPrivateKey(String string) {
        if (string == null) {
            return null;
        }
        if (this.hash.containsKey(string)) {
            return this.hash.get(string).getPrivateKey();
        }
        return null;
    }

    @Override
    public String chooseEngineClientAlias(String[] stringArray, Principal[] principalArray, SSLEngine sSLEngine) {
        if ((stringArray = this.chooseAlias(stringArray, principalArray)) == null) {
            return null;
        }
        return stringArray[0];
    }

    @Override
    public String chooseEngineServerAlias(String stringArray, Principal[] principalArray, SSLEngine sSLEngine) {
        if ((stringArray = this.chooseAlias(new String[]{stringArray}, principalArray)) == null) {
            return null;
        }
        return stringArray[0];
    }

    private String[] chooseAlias(String[] stringArray, Principal[] principalArray) {
        if (stringArray == null || stringArray.length == 0) {
            return null;
        }
        List<Principal> list2 = principalArray == null ? null : Arrays.asList(principalArray);
        ArrayList<String> arrayList = new ArrayList<String>();
        for (Map.Entry<String, KeyStore.PrivateKeyEntry> entry : this.hash.entrySet()) {
            String string = entry.getKey();
            Certificate[] certificateArray = entry.getValue().getCertificateChain();
            Certificate certificate = certificateArray[0];
            String string2 = certificate.getPublicKey().getAlgorithm();
            certificate = certificate instanceof X509Certificate ? ((X509Certificate)certificate).getSigAlgName().toUpperCase(Locale.US) : null;
            String[] stringArray2 = stringArray;
            int n2 = stringArray.length;
            for (int i2 = 0; i2 < n2; ++i2) {
                String n4;
                Certificate[] certificateArray2 = stringArray2[i2];
                if (certificateArray2 == null) continue;
                int n3 = certificateArray2.indexOf(95);
                if (n3 == -1) {
                    n4 = null;
                } else {
                    n4 = certificateArray2.substring(n3 + 1);
                    certificateArray2 = certificateArray2.substring(0, n3);
                }
                if (!string2.equals(certificateArray2) || n4 != null && certificate != null && !((String)((Object)certificate)).contains(n4)) continue;
                if (principalArray == null || principalArray.length == 0) {
                    arrayList.add(string);
                    continue;
                }
                certificateArray2 = certificateArray;
                int n5 = certificateArray.length;
                for (n3 = 0; n3 < n5; ++n3) {
                    Serializable serializable = certificateArray2[n3];
                    if (!(serializable instanceof X509Certificate)) continue;
                    serializable = (X509Certificate)serializable;
                    if (!list2.contains(serializable = ((X509Certificate)serializable).getIssuerX500Principal())) continue;
                    arrayList.add(string);
                }
            }
        }
        if (!arrayList.isEmpty()) {
            ArrayList<String> arrayList2 = arrayList;
            return arrayList2.toArray(new String[arrayList2.size()]);
        }
        return null;
    }
}


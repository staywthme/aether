/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.security.Provider;
import org.conscrypt.NativeCrypto;
import org.conscrypt.Platform;
import org.conscrypt.TrustManagerFactoryImpl;

public final class OpenSSLProvider
extends Provider {
    private static final long serialVersionUID = 2996752495318905136L;
    private static final String PREFIX = OpenSSLProvider.class.getPackage().getName() + ".";
    private static final String STANDARD_EC_PRIVATE_KEY_INTERFACE_CLASS_NAME = "java.security.interfaces.ECPrivateKey";
    private static final String STANDARD_RSA_PRIVATE_KEY_INTERFACE_CLASS_NAME = "java.security.interfaces.RSAPrivateKey";
    private static final String STANDARD_RSA_PUBLIC_KEY_INTERFACE_CLASS_NAME = "java.security.interfaces.RSAPublicKey";

    public OpenSSLProvider() {
        this(Platform.getDefaultProviderName());
    }

    public OpenSSLProvider(String string) {
        this(string, Platform.provideTrustManagerByDefault(), "TLSv1.3");
    }

    OpenSSLProvider(String string, boolean bl, String string2) {
        super(string, 1.0, "Android's OpenSSL-backed security provider");
        NativeCrypto.checkAvailability();
        Platform.setup();
        string = PREFIX + "OpenSSLContextImpl";
        String string3 = "$TLSv12";
        String string4 = "$TLSv13";
        switch (string2) {
            case "TLSv1.2": {
                string2 = string3;
                break;
            }
            case "TLSv1.3": {
                string2 = string4;
                break;
            }
            default: {
                throw new IllegalArgumentException("Choice of default protocol is unsupported: " + string2);
            }
        }
        this.put("SSLContext.SSL", string + string2);
        this.put("SSLContext.TLS", string + string2);
        this.put("SSLContext.TLSv1", string + "$TLSv1");
        this.put("SSLContext.TLSv1.1", string + "$TLSv11");
        this.put("SSLContext.TLSv1.2", string + string3);
        this.put("SSLContext.TLSv1.3", string + string4);
        this.put("SSLContext.Default", PREFIX + "DefaultSSLContextImpl" + string2);
        if (bl) {
            this.put("TrustManagerFactory.PKIX", TrustManagerFactoryImpl.class.getName());
            this.put("Alg.Alias.TrustManagerFactory.X509", "PKIX");
        }
        this.put("AlgorithmParameters.AES", PREFIX + "IvParameters$AES");
        this.put("Alg.Alias.AlgorithmParameters.2.16.840.1.101.3.4.1.2", "AES");
        this.put("Alg.Alias.AlgorithmParameters.2.16.840.1.101.3.4.1.22", "AES");
        this.put("Alg.Alias.AlgorithmParameters.2.16.840.1.101.3.4.1.42", "AES");
        this.put("AlgorithmParameters.ChaCha20", PREFIX + "IvParameters$ChaCha20");
        this.put("AlgorithmParameters.DESEDE", PREFIX + "IvParameters$DESEDE");
        this.put("Alg.Alias.AlgorithmParameters.TDEA", "DESEDE");
        this.put("Alg.Alias.AlgorithmParameters.1.2.840.113549.3.7", "DESEDE");
        this.put("AlgorithmParameters.GCM", PREFIX + "GCMParameters");
        this.put("Alg.Alias.AlgorithmParameters.2.16.840.1.101.3.4.1.6", "GCM");
        this.put("Alg.Alias.AlgorithmParameters.2.16.840.1.101.3.4.1.26", "GCM");
        this.put("Alg.Alias.AlgorithmParameters.2.16.840.1.101.3.4.1.46", "GCM");
        this.put("AlgorithmParameters.OAEP", PREFIX + "OAEPParameters");
        this.put("AlgorithmParameters.PSS", PREFIX + "PSSParameters");
        this.put("AlgorithmParameters.EC", PREFIX + "ECParameters");
        this.put("MessageDigest.SHA-1", PREFIX + "OpenSSLMessageDigestJDK$SHA1");
        this.put("Alg.Alias.MessageDigest.SHA1", "SHA-1");
        this.put("Alg.Alias.MessageDigest.SHA", "SHA-1");
        this.put("Alg.Alias.MessageDigest.1.3.14.3.2.26", "SHA-1");
        this.put("MessageDigest.SHA-224", PREFIX + "OpenSSLMessageDigestJDK$SHA224");
        this.put("Alg.Alias.MessageDigest.SHA224", "SHA-224");
        this.put("Alg.Alias.MessageDigest.2.16.840.1.101.3.4.2.4", "SHA-224");
        this.put("MessageDigest.SHA-256", PREFIX + "OpenSSLMessageDigestJDK$SHA256");
        this.put("Alg.Alias.MessageDigest.SHA256", "SHA-256");
        this.put("Alg.Alias.MessageDigest.2.16.840.1.101.3.4.2.1", "SHA-256");
        this.put("MessageDigest.SHA-384", PREFIX + "OpenSSLMessageDigestJDK$SHA384");
        this.put("Alg.Alias.MessageDigest.SHA384", "SHA-384");
        this.put("Alg.Alias.MessageDigest.2.16.840.1.101.3.4.2.2", "SHA-384");
        this.put("MessageDigest.SHA-512", PREFIX + "OpenSSLMessageDigestJDK$SHA512");
        this.put("Alg.Alias.MessageDigest.SHA512", "SHA-512");
        this.put("Alg.Alias.MessageDigest.2.16.840.1.101.3.4.2.3", "SHA-512");
        this.put("MessageDigest.MD5", PREFIX + "OpenSSLMessageDigestJDK$MD5");
        this.put("Alg.Alias.MessageDigest.1.2.840.113549.2.5", "MD5");
        this.put("KeyGenerator.ARC4", PREFIX + "KeyGeneratorImpl$ARC4");
        this.put("Alg.Alias.KeyGenerator.RC4", "ARC4");
        this.put("Alg.Alias.KeyGenerator.1.2.840.113549.3.4", "ARC4");
        this.put("KeyGenerator.AES", PREFIX + "KeyGeneratorImpl$AES");
        this.put("KeyGenerator.ChaCha20", PREFIX + "KeyGeneratorImpl$ChaCha20");
        this.put("KeyGenerator.DESEDE", PREFIX + "KeyGeneratorImpl$DESEDE");
        this.put("Alg.Alias.KeyGenerator.TDEA", "DESEDE");
        this.put("KeyGenerator.HmacMD5", PREFIX + "KeyGeneratorImpl$HmacMD5");
        this.put("Alg.Alias.KeyGenerator.1.3.6.1.5.5.8.1.1", "HmacMD5");
        this.put("Alg.Alias.KeyGenerator.HMAC-MD5", "HmacMD5");
        this.put("Alg.Alias.KeyGenerator.HMAC/MD5", "HmacMD5");
        this.put("KeyGenerator.HmacSHA1", PREFIX + "KeyGeneratorImpl$HmacSHA1");
        this.put("Alg.Alias.KeyGenerator.1.2.840.113549.2.7", "HmacSHA1");
        this.put("Alg.Alias.KeyGenerator.1.3.6.1.5.5.8.1.2", "HmacSHA1");
        this.put("Alg.Alias.KeyGenerator.HMAC-SHA1", "HmacSHA1");
        this.put("Alg.Alias.KeyGenerator.HMAC/SHA1", "HmacSHA1");
        this.put("KeyGenerator.HmacSHA224", PREFIX + "KeyGeneratorImpl$HmacSHA224");
        this.put("Alg.Alias.KeyGenerator.1.2.840.113549.2.8", "HmacSHA224");
        this.put("Alg.Alias.KeyGenerator.HMAC-SHA224", "HmacSHA224");
        this.put("Alg.Alias.KeyGenerator.HMAC/SHA224", "HmacSHA224");
        this.put("KeyGenerator.HmacSHA256", PREFIX + "KeyGeneratorImpl$HmacSHA256");
        this.put("Alg.Alias.KeyGenerator.1.2.840.113549.2.9", "HmacSHA256");
        this.put("Alg.Alias.KeyGenerator.2.16.840.1.101.3.4.2.1", "HmacSHA256");
        this.put("Alg.Alias.KeyGenerator.HMAC-SHA256", "HmacSHA256");
        this.put("Alg.Alias.KeyGenerator.HMAC/SHA256", "HmacSHA256");
        this.put("KeyGenerator.HmacSHA384", PREFIX + "KeyGeneratorImpl$HmacSHA384");
        this.put("Alg.Alias.KeyGenerator.1.2.840.113549.2.10", "HmacSHA384");
        this.put("Alg.Alias.KeyGenerator.HMAC-SHA384", "HmacSHA384");
        this.put("Alg.Alias.KeyGenerator.HMAC/SHA384", "HmacSHA384");
        this.put("KeyGenerator.HmacSHA512", PREFIX + "KeyGeneratorImpl$HmacSHA512");
        this.put("Alg.Alias.KeyGenerator.1.2.840.113549.2.11", "HmacSHA512");
        this.put("Alg.Alias.KeyGenerator.HMAC-SHA512", "HmacSHA512");
        this.put("Alg.Alias.KeyGenerator.HMAC/SHA512", "HmacSHA512");
        this.put("KeyPairGenerator.RSA", PREFIX + "OpenSSLRSAKeyPairGenerator");
        this.put("Alg.Alias.KeyPairGenerator.1.2.840.113549.1.1.1", "RSA");
        this.put("Alg.Alias.KeyPairGenerator.1.2.840.113549.1.1.7", "RSA");
        this.put("Alg.Alias.KeyPairGenerator.2.5.8.1.1", "RSA");
        this.put("KeyPairGenerator.EC", PREFIX + "OpenSSLECKeyPairGenerator");
        this.put("Alg.Alias.KeyPairGenerator.1.2.840.10045.2.1", "EC");
        this.put("Alg.Alias.KeyPairGenerator.1.3.133.16.840.63.0.2", "EC");
        this.put("KeyFactory.RSA", PREFIX + "OpenSSLRSAKeyFactory");
        this.put("Alg.Alias.KeyFactory.1.2.840.113549.1.1.1", "RSA");
        this.put("Alg.Alias.KeyFactory.1.2.840.113549.1.1.7", "RSA");
        this.put("Alg.Alias.KeyFactory.2.5.8.1.1", "RSA");
        this.put("KeyFactory.EC", PREFIX + "OpenSSLECKeyFactory");
        this.put("Alg.Alias.KeyFactory.1.2.840.10045.2.1", "EC");
        this.put("Alg.Alias.KeyFactory.1.3.133.16.840.63.0.2", "EC");
        this.put("SecretKeyFactory.DESEDE", PREFIX + "DESEDESecretKeyFactory");
        this.put("Alg.Alias.SecretKeyFactory.TDEA", "DESEDE");
        this.putECDHKeyAgreementImplClass("OpenSSLECDHKeyAgreement");
        this.putSignatureImplClass("MD5withRSA", "OpenSSLSignature$MD5RSA");
        this.put("Alg.Alias.Signature.MD5withRSAEncryption", "MD5withRSA");
        this.put("Alg.Alias.Signature.MD5/RSA", "MD5withRSA");
        this.put("Alg.Alias.Signature.1.2.840.113549.1.1.4", "MD5withRSA");
        this.put("Alg.Alias.Signature.OID.1.2.840.113549.1.1.4", "MD5withRSA");
        this.put("Alg.Alias.Signature.1.2.840.113549.2.5with1.2.840.113549.1.1.1", "MD5withRSA");
        this.putSignatureImplClass("SHA1withRSA", "OpenSSLSignature$SHA1RSA");
        this.put("Alg.Alias.Signature.SHA1withRSAEncryption", "SHA1withRSA");
        this.put("Alg.Alias.Signature.SHA1/RSA", "SHA1withRSA");
        this.put("Alg.Alias.Signature.SHA-1/RSA", "SHA1withRSA");
        this.put("Alg.Alias.Signature.1.2.840.113549.1.1.5", "SHA1withRSA");
        this.put("Alg.Alias.Signature.OID.1.2.840.113549.1.1.5", "SHA1withRSA");
        this.put("Alg.Alias.Signature.1.3.14.3.2.26with1.2.840.113549.1.1.1", "SHA1withRSA");
        this.put("Alg.Alias.Signature.1.3.14.3.2.26with1.2.840.113549.1.1.5", "SHA1withRSA");
        this.put("Alg.Alias.Signature.1.3.14.3.2.29", "SHA1withRSA");
        this.put("Alg.Alias.Signature.OID.1.3.14.3.2.29", "SHA1withRSA");
        this.putSignatureImplClass("SHA224withRSA", "OpenSSLSignature$SHA224RSA");
        this.put("Alg.Alias.Signature.SHA224withRSAEncryption", "SHA224withRSA");
        this.put("Alg.Alias.Signature.SHA224/RSA", "SHA224withRSA");
        this.put("Alg.Alias.Signature.1.2.840.113549.1.1.14", "SHA224withRSA");
        this.put("Alg.Alias.Signature.OID.1.2.840.113549.1.1.14", "SHA224withRSA");
        this.put("Alg.Alias.Signature.2.16.840.1.101.3.4.2.4with1.2.840.113549.1.1.1", "SHA224withRSA");
        this.put("Alg.Alias.Signature.2.16.840.1.101.3.4.2.4with1.2.840.113549.1.1.14", "SHA224withRSA");
        this.putSignatureImplClass("SHA256withRSA", "OpenSSLSignature$SHA256RSA");
        this.put("Alg.Alias.Signature.SHA256withRSAEncryption", "SHA256withRSA");
        this.put("Alg.Alias.Signature.SHA256/RSA", "SHA256withRSA");
        this.put("Alg.Alias.Signature.1.2.840.113549.1.1.11", "SHA256withRSA");
        this.put("Alg.Alias.Signature.OID.1.2.840.113549.1.1.11", "SHA256withRSA");
        this.put("Alg.Alias.Signature.2.16.840.1.101.3.4.2.1with1.2.840.113549.1.1.1", "SHA256withRSA");
        this.put("Alg.Alias.Signature.2.16.840.1.101.3.4.2.1with1.2.840.113549.1.1.11", "SHA256withRSA");
        this.putSignatureImplClass("SHA384withRSA", "OpenSSLSignature$SHA384RSA");
        this.put("Alg.Alias.Signature.SHA384withRSAEncryption", "SHA384withRSA");
        this.put("Alg.Alias.Signature.SHA384/RSA", "SHA384withRSA");
        this.put("Alg.Alias.Signature.1.2.840.113549.1.1.12", "SHA384withRSA");
        this.put("Alg.Alias.Signature.OID.1.2.840.113549.1.1.12", "SHA384withRSA");
        this.put("Alg.Alias.Signature.2.16.840.1.101.3.4.2.2with1.2.840.113549.1.1.1", "SHA384withRSA");
        this.putSignatureImplClass("SHA512withRSA", "OpenSSLSignature$SHA512RSA");
        this.put("Alg.Alias.Signature.SHA512withRSAEncryption", "SHA512withRSA");
        this.put("Alg.Alias.Signature.SHA512/RSA", "SHA512withRSA");
        this.put("Alg.Alias.Signature.1.2.840.113549.1.1.13", "SHA512withRSA");
        this.put("Alg.Alias.Signature.OID.1.2.840.113549.1.1.13", "SHA512withRSA");
        this.put("Alg.Alias.Signature.2.16.840.1.101.3.4.2.3with1.2.840.113549.1.1.1", "SHA512withRSA");
        this.putRAWRSASignatureImplClass("OpenSSLSignatureRawRSA");
        this.putSignatureImplClass("NONEwithECDSA", "OpenSSLSignatureRawECDSA");
        this.putSignatureImplClass("SHA1withECDSA", "OpenSSLSignature$SHA1ECDSA");
        this.put("Alg.Alias.Signature.ECDSA", "SHA1withECDSA");
        this.put("Alg.Alias.Signature.ECDSAwithSHA1", "SHA1withECDSA");
        this.put("Alg.Alias.Signature.1.2.840.10045.4.1", "SHA1withECDSA");
        this.put("Alg.Alias.Signature.1.3.14.3.2.26with1.2.840.10045.2.1", "SHA1withECDSA");
        this.putSignatureImplClass("SHA224withECDSA", "OpenSSLSignature$SHA224ECDSA");
        this.put("Alg.Alias.Signature.SHA224/ECDSA", "SHA224withECDSA");
        this.put("Alg.Alias.Signature.1.2.840.10045.4.3.1", "SHA224withECDSA");
        this.put("Alg.Alias.Signature.OID.1.2.840.10045.4.3.1", "SHA224withECDSA");
        this.put("Alg.Alias.Signature.2.16.840.1.101.3.4.2.4with1.2.840.10045.2.1", "SHA224withECDSA");
        this.putSignatureImplClass("SHA256withECDSA", "OpenSSLSignature$SHA256ECDSA");
        this.put("Alg.Alias.Signature.SHA256/ECDSA", "SHA256withECDSA");
        this.put("Alg.Alias.Signature.1.2.840.10045.4.3.2", "SHA256withECDSA");
        this.put("Alg.Alias.Signature.OID.1.2.840.10045.4.3.2", "SHA256withECDSA");
        this.put("Alg.Alias.Signature.2.16.840.1.101.3.4.2.1with1.2.840.10045.2.1", "SHA256withECDSA");
        this.putSignatureImplClass("SHA384withECDSA", "OpenSSLSignature$SHA384ECDSA");
        this.put("Alg.Alias.Signature.SHA384/ECDSA", "SHA384withECDSA");
        this.put("Alg.Alias.Signature.1.2.840.10045.4.3.3", "SHA384withECDSA");
        this.put("Alg.Alias.Signature.OID.1.2.840.10045.4.3.3", "SHA384withECDSA");
        this.put("Alg.Alias.Signature.2.16.840.1.101.3.4.2.2with1.2.840.10045.2.1", "SHA384withECDSA");
        this.putSignatureImplClass("SHA512withECDSA", "OpenSSLSignature$SHA512ECDSA");
        this.put("Alg.Alias.Signature.SHA512/ECDSA", "SHA512withECDSA");
        this.put("Alg.Alias.Signature.1.2.840.10045.4.3.4", "SHA512withECDSA");
        this.put("Alg.Alias.Signature.OID.1.2.840.10045.4.3.4", "SHA512withECDSA");
        this.put("Alg.Alias.Signature.2.16.840.1.101.3.4.2.3with1.2.840.10045.2.1", "SHA512withECDSA");
        this.putSignatureImplClass("SHA1withRSA/PSS", "OpenSSLSignature$SHA1RSAPSS");
        this.put("Alg.Alias.Signature.SHA1withRSAandMGF1", "SHA1withRSA/PSS");
        this.putSignatureImplClass("SHA224withRSA/PSS", "OpenSSLSignature$SHA224RSAPSS");
        this.put("Alg.Alias.Signature.SHA224withRSAandMGF1", "SHA224withRSA/PSS");
        this.putSignatureImplClass("SHA256withRSA/PSS", "OpenSSLSignature$SHA256RSAPSS");
        this.put("Alg.Alias.Signature.SHA256withRSAandMGF1", "SHA256withRSA/PSS");
        this.putSignatureImplClass("SHA384withRSA/PSS", "OpenSSLSignature$SHA384RSAPSS");
        this.put("Alg.Alias.Signature.SHA384withRSAandMGF1", "SHA384withRSA/PSS");
        this.putSignatureImplClass("SHA512withRSA/PSS", "OpenSSLSignature$SHA512RSAPSS");
        this.put("Alg.Alias.Signature.SHA512withRSAandMGF1", "SHA512withRSA/PSS");
        this.put("SecureRandom.SHA1PRNG", PREFIX + "OpenSSLRandom");
        this.put("SecureRandom.SHA1PRNG ImplementedIn", "Software");
        this.putRSACipherImplClass("RSA/ECB/NoPadding", "OpenSSLCipherRSA$Raw");
        this.put("Alg.Alias.Cipher.RSA/None/NoPadding", "RSA/ECB/NoPadding");
        this.putRSACipherImplClass("RSA/ECB/PKCS1Padding", "OpenSSLCipherRSA$PKCS1");
        this.put("Alg.Alias.Cipher.RSA/None/PKCS1Padding", "RSA/ECB/PKCS1Padding");
        this.putRSACipherImplClass("RSA/ECB/OAEPPadding", "OpenSSLCipherRSA$OAEP$SHA1");
        this.put("Alg.Alias.Cipher.RSA/None/OAEPPadding", "RSA/ECB/OAEPPadding");
        this.putRSACipherImplClass("RSA/ECB/OAEPWithSHA-1AndMGF1Padding", "OpenSSLCipherRSA$OAEP$SHA1");
        this.put("Alg.Alias.Cipher.RSA/None/OAEPWithSHA-1AndMGF1Padding", "RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
        this.putRSACipherImplClass("RSA/ECB/OAEPWithSHA-224AndMGF1Padding", "OpenSSLCipherRSA$OAEP$SHA224");
        this.put("Alg.Alias.Cipher.RSA/None/OAEPWithSHA-224AndMGF1Padding", "RSA/ECB/OAEPWithSHA-224AndMGF1Padding");
        this.putRSACipherImplClass("RSA/ECB/OAEPWithSHA-256AndMGF1Padding", "OpenSSLCipherRSA$OAEP$SHA256");
        this.put("Alg.Alias.Cipher.RSA/None/OAEPWithSHA-256AndMGF1Padding", "RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        this.putRSACipherImplClass("RSA/ECB/OAEPWithSHA-384AndMGF1Padding", "OpenSSLCipherRSA$OAEP$SHA384");
        this.put("Alg.Alias.Cipher.RSA/None/OAEPWithSHA-384AndMGF1Padding", "RSA/ECB/OAEPWithSHA-384AndMGF1Padding");
        this.putRSACipherImplClass("RSA/ECB/OAEPWithSHA-512AndMGF1Padding", "OpenSSLCipherRSA$OAEP$SHA512");
        this.put("Alg.Alias.Cipher.RSA/None/OAEPWithSHA-512AndMGF1Padding", "RSA/ECB/OAEPWithSHA-512AndMGF1Padding");
        this.putSymmetricCipherImplClass("AES/ECB/NoPadding", "OpenSSLEvpCipherAES$AES$ECB$NoPadding");
        this.putSymmetricCipherImplClass("AES/ECB/PKCS5Padding", "OpenSSLEvpCipherAES$AES$ECB$PKCS5Padding");
        this.put("Alg.Alias.Cipher.AES/ECB/PKCS7Padding", "AES/ECB/PKCS5Padding");
        this.putSymmetricCipherImplClass("AES/CBC/NoPadding", "OpenSSLEvpCipherAES$AES$CBC$NoPadding");
        this.putSymmetricCipherImplClass("AES/CBC/PKCS5Padding", "OpenSSLEvpCipherAES$AES$CBC$PKCS5Padding");
        this.put("Alg.Alias.Cipher.AES/CBC/PKCS7Padding", "AES/CBC/PKCS5Padding");
        this.putSymmetricCipherImplClass("AES/CTR/NoPadding", "OpenSSLEvpCipherAES$AES$CTR");
        this.putSymmetricCipherImplClass("AES_128/ECB/NoPadding", "OpenSSLEvpCipherAES$AES_128$ECB$NoPadding");
        this.putSymmetricCipherImplClass("AES_128/ECB/PKCS5Padding", "OpenSSLEvpCipherAES$AES_128$ECB$PKCS5Padding");
        this.put("Alg.Alias.Cipher.AES_128/ECB/PKCS7Padding", "AES_128/ECB/PKCS5Padding");
        this.putSymmetricCipherImplClass("AES_128/CBC/NoPadding", "OpenSSLEvpCipherAES$AES_128$CBC$NoPadding");
        this.putSymmetricCipherImplClass("AES_128/CBC/PKCS5Padding", "OpenSSLEvpCipherAES$AES_128$CBC$PKCS5Padding");
        this.put("Alg.Alias.Cipher.AES_128/CBC/PKCS7Padding", "AES_128/CBC/PKCS5Padding");
        this.put("Alg.Alias.Cipher.PBEWithHmacSHA1AndAES_128", "AES_128/CBC/PKCS5PADDING");
        this.put("Alg.Alias.Cipher.PBEWithHmacSHA224AndAES_128", "AES_128/CBC/PKCS5PADDING");
        this.put("Alg.Alias.Cipher.PBEWithHmacSHA256AndAES_128", "AES_128/CBC/PKCS5PADDING");
        this.put("Alg.Alias.Cipher.PBEWithHmacSHA384AndAES_128", "AES_128/CBC/PKCS5PADDING");
        this.put("Alg.Alias.Cipher.PBEWithHmacSHA512AndAES_128", "AES_128/CBC/PKCS5PADDING");
        this.putSymmetricCipherImplClass("AES_256/ECB/NoPadding", "OpenSSLEvpCipherAES$AES_256$ECB$NoPadding");
        this.putSymmetricCipherImplClass("AES_256/ECB/PKCS5Padding", "OpenSSLEvpCipherAES$AES_256$ECB$PKCS5Padding");
        this.put("Alg.Alias.Cipher.AES_256/ECB/PKCS7Padding", "AES_256/ECB/PKCS5Padding");
        this.putSymmetricCipherImplClass("AES_256/CBC/NoPadding", "OpenSSLEvpCipherAES$AES_256$CBC$NoPadding");
        this.putSymmetricCipherImplClass("AES_256/CBC/PKCS5Padding", "OpenSSLEvpCipherAES$AES_256$CBC$PKCS5Padding");
        this.put("Alg.Alias.Cipher.AES_256/CBC/PKCS7Padding", "AES_256/CBC/PKCS5Padding");
        this.put("Alg.Alias.Cipher.PBEWithHmacSHA1AndAES_256", "AES_256/CBC/PKCS5PADDING");
        this.put("Alg.Alias.Cipher.PBEWithHmacSHA224AndAES_256", "AES_256/CBC/PKCS5PADDING");
        this.put("Alg.Alias.Cipher.PBEWithHmacSHA256AndAES_256", "AES_256/CBC/PKCS5PADDING");
        this.put("Alg.Alias.Cipher.PBEWithHmacSHA384AndAES_256", "AES_256/CBC/PKCS5PADDING");
        this.put("Alg.Alias.Cipher.PBEWithHmacSHA512AndAES_256", "AES_256/CBC/PKCS5PADDING");
        this.putSymmetricCipherImplClass("DESEDE/CBC/NoPadding", "OpenSSLEvpCipherDESEDE$CBC$NoPadding");
        this.putSymmetricCipherImplClass("DESEDE/CBC/PKCS5Padding", "OpenSSLEvpCipherDESEDE$CBC$PKCS5Padding");
        this.put("Alg.Alias.Cipher.DESEDE/CBC/PKCS7Padding", "DESEDE/CBC/PKCS5Padding");
        this.putSymmetricCipherImplClass("ARC4", "OpenSSLEvpCipherARC4");
        this.put("Alg.Alias.Cipher.ARCFOUR", "ARC4");
        this.put("Alg.Alias.Cipher.RC4", "ARC4");
        this.put("Alg.Alias.Cipher.1.2.840.113549.3.4", "ARC4");
        this.put("Alg.Alias.Cipher.OID.1.2.840.113549.3.4", "ARC4");
        this.putSymmetricCipherImplClass("AES/GCM/NoPadding", "OpenSSLAeadCipherAES$GCM");
        this.put("Alg.Alias.Cipher.GCM", "AES/GCM/NoPadding");
        this.put("Alg.Alias.Cipher.2.16.840.1.101.3.4.1.6", "AES/GCM/NoPadding");
        this.put("Alg.Alias.Cipher.2.16.840.1.101.3.4.1.26", "AES/GCM/NoPadding");
        this.put("Alg.Alias.Cipher.2.16.840.1.101.3.4.1.46", "AES/GCM/NoPadding");
        this.putSymmetricCipherImplClass("AES_128/GCM/NoPadding", "OpenSSLAeadCipherAES$GCM$AES_128");
        this.putSymmetricCipherImplClass("AES_256/GCM/NoPadding", "OpenSSLAeadCipherAES$GCM$AES_256");
        this.putSymmetricCipherImplClass("AES/GCM-SIV/NoPadding", "OpenSSLAeadCipherAES$GCM_SIV");
        this.putSymmetricCipherImplClass("AES_128/GCM-SIV/NoPadding", "OpenSSLAeadCipherAES$GCM_SIV$AES_128");
        this.putSymmetricCipherImplClass("AES_256/GCM-SIV/NoPadding", "OpenSSLAeadCipherAES$GCM_SIV$AES_256");
        this.putSymmetricCipherImplClass("ChaCha20", "OpenSSLCipherChaCha20");
        this.putSymmetricCipherImplClass("ChaCha20/Poly1305/NoPadding", "OpenSSLAeadCipherChaCha20");
        this.put("Alg.Alias.Cipher.ChaCha20-Poly1305", "ChaCha20/Poly1305/NoPadding");
        this.putMacImplClass("HmacMD5", "OpenSSLMac$HmacMD5");
        this.put("Alg.Alias.Mac.1.3.6.1.5.5.8.1.1", "HmacMD5");
        this.put("Alg.Alias.Mac.HMAC-MD5", "HmacMD5");
        this.put("Alg.Alias.Mac.HMAC/MD5", "HmacMD5");
        this.putMacImplClass("HmacSHA1", "OpenSSLMac$HmacSHA1");
        this.put("Alg.Alias.Mac.1.2.840.113549.2.7", "HmacSHA1");
        this.put("Alg.Alias.Mac.1.3.6.1.5.5.8.1.2", "HmacSHA1");
        this.put("Alg.Alias.Mac.HMAC-SHA1", "HmacSHA1");
        this.put("Alg.Alias.Mac.HMAC/SHA1", "HmacSHA1");
        this.putMacImplClass("HmacSHA224", "OpenSSLMac$HmacSHA224");
        this.put("Alg.Alias.Mac.1.2.840.113549.2.8", "HmacSHA224");
        this.put("Alg.Alias.Mac.HMAC-SHA224", "HmacSHA224");
        this.put("Alg.Alias.Mac.HMAC/SHA224", "HmacSHA224");
        this.put("Alg.Alias.Mac.PBEWITHHMACSHA224", "HmacSHA224");
        this.putMacImplClass("HmacSHA256", "OpenSSLMac$HmacSHA256");
        this.put("Alg.Alias.Mac.1.2.840.113549.2.9", "HmacSHA256");
        this.put("Alg.Alias.Mac.2.16.840.1.101.3.4.2.1", "HmacSHA256");
        this.put("Alg.Alias.Mac.HMAC-SHA256", "HmacSHA256");
        this.put("Alg.Alias.Mac.HMAC/SHA256", "HmacSHA256");
        this.put("Alg.Alias.Mac.PBEWITHHMACSHA256", "HmacSHA256");
        this.putMacImplClass("HmacSHA384", "OpenSSLMac$HmacSHA384");
        this.put("Alg.Alias.Mac.1.2.840.113549.2.10", "HmacSHA384");
        this.put("Alg.Alias.Mac.HMAC-SHA384", "HmacSHA384");
        this.put("Alg.Alias.Mac.HMAC/SHA384", "HmacSHA384");
        this.put("Alg.Alias.Mac.PBEWITHHMACSHA384", "HmacSHA384");
        this.putMacImplClass("HmacSHA512", "OpenSSLMac$HmacSHA512");
        this.put("Alg.Alias.Mac.1.2.840.113549.2.11", "HmacSHA512");
        this.put("Alg.Alias.Mac.HMAC-SHA512", "HmacSHA512");
        this.put("Alg.Alias.Mac.HMAC/SHA512", "HmacSHA512");
        this.put("Alg.Alias.Mac.PBEWITHHMACSHA512", "HmacSHA512");
        this.put("CertificateFactory.X509", PREFIX + "OpenSSLX509CertificateFactory");
        this.put("Alg.Alias.CertificateFactory.X.509", "X509");
    }

    private void putMacImplClass(String string, String string2) {
        String string3 = PREFIX + "OpenSSLKeyHolder";
        String string4 = "RAW";
        this.putImplClassWithKeyConstraints("Mac." + string, PREFIX + string2, string3, string4);
    }

    private void putSymmetricCipherImplClass(String string, String string2) {
        String string3 = "RAW";
        this.putImplClassWithKeyConstraints("Cipher." + string, PREFIX + string2, null, string3);
    }

    private void putRSACipherImplClass(String string, String string2) {
        String string3 = PREFIX + "OpenSSLRSAPrivateKey|java.security.interfaces.RSAPrivateKey" + "|" + PREFIX + "OpenSSLRSAPublicKey|java.security.interfaces.RSAPublicKey";
        this.putImplClassWithKeyConstraints("Cipher." + string, PREFIX + string2, string3, null);
    }

    private void putSignatureImplClass(String string, String string2) {
        String string3 = PREFIX + "OpenSSLKeyHolder|java.security.interfaces.RSAPrivateKey" + "|java.security.interfaces.ECPrivateKey" + "|java.security.interfaces.RSAPublicKey";
        String string4 = "PKCS#8|X.509";
        this.putImplClassWithKeyConstraints("Signature." + string, PREFIX + string2, string3, string4);
    }

    private void putRAWRSASignatureImplClass(String string) {
        String string2 = PREFIX + "OpenSSLRSAPrivateKey|java.security.interfaces.RSAPrivateKey" + "|" + PREFIX + "OpenSSLRSAPublicKey|java.security.interfaces.RSAPublicKey";
        this.putImplClassWithKeyConstraints("Signature.NONEwithRSA", PREFIX + string, string2, null);
    }

    private void putECDHKeyAgreementImplClass(String string) {
        String string2 = PREFIX + "OpenSSLKeyHolder|java.security.interfaces.ECPrivateKey";
        String string3 = "PKCS#8";
        this.putImplClassWithKeyConstraints("KeyAgreement.ECDH", PREFIX + string, string2, string3);
    }

    private void putImplClassWithKeyConstraints(String string, String string2, String string3, String string4) {
        this.put(string, string2);
        if (string3 != null) {
            this.put(string + " SupportedKeyClasses", string3);
        }
        if (string4 != null) {
            this.put(string + " SupportedKeyFormats", string4);
        }
    }
}


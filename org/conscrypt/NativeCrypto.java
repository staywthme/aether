/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateParsingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;
import javax.net.ssl.SSLException;
import javax.security.auth.x500.X500Principal;
import org.conscrypt.AbstractSessionContext;
import org.conscrypt.NativeCryptoJni;
import org.conscrypt.NativeRef;
import org.conscrypt.NativeSsl;
import org.conscrypt.OpenSSLBIOInputStream;
import org.conscrypt.OpenSSLX509CRL;
import org.conscrypt.OpenSSLX509Certificate;
import org.conscrypt.OpenSSLX509CertificateFactory;
import org.conscrypt.SSLUtils;

public final class NativeCrypto {
    private static final UnsatisfiedLinkError loadError;
    static final int GN_STACK_SUBJECT_ALT_NAME = 1;
    static final int GN_STACK_ISSUER_ALT_NAME = 2;
    static final int EXTENSION_TYPE_NON_CRITICAL = 0;
    static final int EXTENSION_TYPE_CRITICAL = 1;
    static final int PKCS7_CERTS = 1;
    static final int PKCS7_CRLS = 2;
    static final String OBSOLETE_PROTOCOL_SSLV3 = "SSLv3";
    private static final String SUPPORTED_PROTOCOL_TLSV1 = "TLSv1";
    private static final String SUPPORTED_PROTOCOL_TLSV1_1 = "TLSv1.1";
    private static final String SUPPORTED_PROTOCOL_TLSV1_2 = "TLSv1.2";
    static final String SUPPORTED_PROTOCOL_TLSV1_3 = "TLSv1.3";
    static final String[] SUPPORTED_TLS_1_3_CIPHER_SUITES;
    static final Set<String> SUPPORTED_TLS_1_2_CIPHER_SUITES_SET;
    private static final Set<String> SUPPORTED_LEGACY_CIPHER_SUITES_SET;
    static final Set<String> SUPPORTED_TLS_1_3_CIPHER_SUITES_SET;
    static final String TLS_EMPTY_RENEGOTIATION_INFO_SCSV = "TLS_EMPTY_RENEGOTIATION_INFO_SCSV";
    private static final String TLS_FALLBACK_SCSV = "TLS_FALLBACK_SCSV";
    private static final boolean HAS_AES_HARDWARE;
    private static final String[] SUPPORTED_TLS_1_2_CIPHER_SUITES;
    static final String[] DEFAULT_X509_CIPHER_SUITES;
    static final String[] DEFAULT_PSK_CIPHER_SUITES;
    static final String[] TLSV13_PROTOCOLS;
    static final String[] TLSV12_PROTOCOLS;
    static final String[] TLSV11_PROTOCOLS;
    static final String[] TLSV1_PROTOCOLS;
    static final String[] DEFAULT_PROTOCOLS;
    private static final String[] SUPPORTED_PROTOCOLS;

    private static native void clinit();

    static void checkAvailability() {
        if (loadError != null) {
            throw loadError;
        }
    }

    static native long EVP_PKEY_new_RSA(byte[] var0, byte[] var1, byte[] var2, byte[] var3, byte[] var4, byte[] var5, byte[] var6, byte[] var7);

    static native int EVP_PKEY_type(NativeRef.EVP_PKEY var0);

    static native String EVP_PKEY_print_public(NativeRef.EVP_PKEY var0);

    static native String EVP_PKEY_print_params(NativeRef.EVP_PKEY var0);

    static native void EVP_PKEY_free(long var0);

    static native int EVP_PKEY_cmp(NativeRef.EVP_PKEY var0, NativeRef.EVP_PKEY var1);

    static native byte[] EVP_marshal_private_key(NativeRef.EVP_PKEY var0);

    static native long EVP_parse_private_key(byte[] var0) throws OpenSSLX509CertificateFactory.ParsingException;

    static native byte[] EVP_marshal_public_key(NativeRef.EVP_PKEY var0);

    static native long EVP_parse_public_key(byte[] var0) throws OpenSSLX509CertificateFactory.ParsingException;

    static native long PEM_read_bio_PUBKEY(long var0);

    static native long PEM_read_bio_PrivateKey(long var0);

    static native long getRSAPrivateKeyWrapper(PrivateKey var0, byte[] var1);

    static native long getECPrivateKeyWrapper(PrivateKey var0, NativeRef.EC_GROUP var1);

    static native long RSA_generate_key_ex(int var0, byte[] var1);

    static native int RSA_size(NativeRef.EVP_PKEY var0);

    static native int RSA_private_encrypt(int var0, byte[] var1, byte[] var2, NativeRef.EVP_PKEY var3, int var4);

    static native int RSA_public_decrypt(int var0, byte[] var1, byte[] var2, NativeRef.EVP_PKEY var3, int var4) throws BadPaddingException, SignatureException;

    static native int RSA_public_encrypt(int var0, byte[] var1, byte[] var2, NativeRef.EVP_PKEY var3, int var4);

    static native int RSA_private_decrypt(int var0, byte[] var1, byte[] var2, NativeRef.EVP_PKEY var3, int var4) throws BadPaddingException, SignatureException;

    static native byte[][] get_RSA_public_params(NativeRef.EVP_PKEY var0);

    static native byte[][] get_RSA_private_params(NativeRef.EVP_PKEY var0);

    static native void chacha20_encrypt_decrypt(byte[] var0, int var1, byte[] var2, int var3, int var4, byte[] var5, byte[] var6, int var7);

    static native long EVP_PKEY_new_EC_KEY(NativeRef.EC_GROUP var0, NativeRef.EC_POINT var1, byte[] var2);

    static native long EC_GROUP_new_by_curve_name(String var0);

    static native long EC_GROUP_new_arbitrary(byte[] var0, byte[] var1, byte[] var2, byte[] var3, byte[] var4, byte[] var5, int var6);

    static native String EC_GROUP_get_curve_name(NativeRef.EC_GROUP var0);

    static native byte[][] EC_GROUP_get_curve(NativeRef.EC_GROUP var0);

    static native void EC_GROUP_clear_free(long var0);

    static native long EC_GROUP_get_generator(NativeRef.EC_GROUP var0);

    static native byte[] EC_GROUP_get_order(NativeRef.EC_GROUP var0);

    static native int EC_GROUP_get_degree(NativeRef.EC_GROUP var0);

    static native byte[] EC_GROUP_get_cofactor(NativeRef.EC_GROUP var0);

    static native long EC_POINT_new(NativeRef.EC_GROUP var0);

    static native void EC_POINT_clear_free(long var0);

    static native byte[][] EC_POINT_get_affine_coordinates(NativeRef.EC_GROUP var0, NativeRef.EC_POINT var1);

    static native void EC_POINT_set_affine_coordinates(NativeRef.EC_GROUP var0, NativeRef.EC_POINT var1, byte[] var2, byte[] var3);

    static native long EC_KEY_generate_key(NativeRef.EC_GROUP var0);

    static native long EC_KEY_get1_group(NativeRef.EVP_PKEY var0);

    static native byte[] EC_KEY_get_private_key(NativeRef.EVP_PKEY var0);

    static native long EC_KEY_get_public_key(NativeRef.EVP_PKEY var0);

    static native byte[] EC_KEY_marshal_curve_name(NativeRef.EC_GROUP var0) throws IOException;

    static native long EC_KEY_parse_curve_name(byte[] var0) throws IOException;

    static native int ECDH_compute_key(byte[] var0, int var1, NativeRef.EVP_PKEY var2, NativeRef.EVP_PKEY var3) throws InvalidKeyException, IndexOutOfBoundsException;

    static native int ECDSA_size(NativeRef.EVP_PKEY var0);

    static native int ECDSA_sign(byte[] var0, byte[] var1, NativeRef.EVP_PKEY var2);

    static native int ECDSA_verify(byte[] var0, byte[] var1, NativeRef.EVP_PKEY var2);

    static native long EVP_get_digestbyname(String var0);

    static native int EVP_MD_size(long var0);

    static native long EVP_MD_CTX_create();

    static native void EVP_MD_CTX_cleanup(NativeRef.EVP_MD_CTX var0);

    static native void EVP_MD_CTX_destroy(long var0);

    static native int EVP_MD_CTX_copy_ex(NativeRef.EVP_MD_CTX var0, NativeRef.EVP_MD_CTX var1);

    static native int EVP_DigestInit_ex(NativeRef.EVP_MD_CTX var0, long var1);

    static native void EVP_DigestUpdate(NativeRef.EVP_MD_CTX var0, byte[] var1, int var2, int var3);

    static native void EVP_DigestUpdateDirect(NativeRef.EVP_MD_CTX var0, long var1, int var3);

    static native int EVP_DigestFinal_ex(NativeRef.EVP_MD_CTX var0, byte[] var1, int var2);

    static native long EVP_DigestSignInit(NativeRef.EVP_MD_CTX var0, long var1, NativeRef.EVP_PKEY var3);

    static native long EVP_DigestVerifyInit(NativeRef.EVP_MD_CTX var0, long var1, NativeRef.EVP_PKEY var3);

    static native void EVP_DigestSignUpdate(NativeRef.EVP_MD_CTX var0, byte[] var1, int var2, int var3);

    static native void EVP_DigestSignUpdateDirect(NativeRef.EVP_MD_CTX var0, long var1, int var3);

    static native void EVP_DigestVerifyUpdate(NativeRef.EVP_MD_CTX var0, byte[] var1, int var2, int var3);

    static native void EVP_DigestVerifyUpdateDirect(NativeRef.EVP_MD_CTX var0, long var1, int var3);

    static native byte[] EVP_DigestSignFinal(NativeRef.EVP_MD_CTX var0);

    static native boolean EVP_DigestVerifyFinal(NativeRef.EVP_MD_CTX var0, byte[] var1, int var2, int var3) throws IndexOutOfBoundsException;

    static native long EVP_PKEY_encrypt_init(NativeRef.EVP_PKEY var0) throws InvalidKeyException;

    static native int EVP_PKEY_encrypt(NativeRef.EVP_PKEY_CTX var0, byte[] var1, int var2, byte[] var3, int var4, int var5) throws IndexOutOfBoundsException, BadPaddingException;

    static native long EVP_PKEY_decrypt_init(NativeRef.EVP_PKEY var0) throws InvalidKeyException;

    static native int EVP_PKEY_decrypt(NativeRef.EVP_PKEY_CTX var0, byte[] var1, int var2, byte[] var3, int var4, int var5) throws IndexOutOfBoundsException, BadPaddingException;

    static native void EVP_PKEY_CTX_free(long var0);

    static native void EVP_PKEY_CTX_set_rsa_padding(long var0, int var2) throws InvalidAlgorithmParameterException;

    static native void EVP_PKEY_CTX_set_rsa_pss_saltlen(long var0, int var2) throws InvalidAlgorithmParameterException;

    static native void EVP_PKEY_CTX_set_rsa_mgf1_md(long var0, long var2) throws InvalidAlgorithmParameterException;

    static native void EVP_PKEY_CTX_set_rsa_oaep_md(long var0, long var2) throws InvalidAlgorithmParameterException;

    static native void EVP_PKEY_CTX_set_rsa_oaep_label(long var0, byte[] var2) throws InvalidAlgorithmParameterException;

    static native long EVP_get_cipherbyname(String var0);

    static native void EVP_CipherInit_ex(NativeRef.EVP_CIPHER_CTX var0, long var1, byte[] var3, byte[] var4, boolean var5);

    static native int EVP_CipherUpdate(NativeRef.EVP_CIPHER_CTX var0, byte[] var1, int var2, byte[] var3, int var4, int var5) throws IndexOutOfBoundsException;

    static native int EVP_CipherFinal_ex(NativeRef.EVP_CIPHER_CTX var0, byte[] var1, int var2) throws BadPaddingException, IllegalBlockSizeException;

    static native int EVP_CIPHER_iv_length(long var0);

    static native long EVP_CIPHER_CTX_new();

    static native int EVP_CIPHER_CTX_block_size(NativeRef.EVP_CIPHER_CTX var0);

    static native int get_EVP_CIPHER_CTX_buf_len(NativeRef.EVP_CIPHER_CTX var0);

    static native boolean get_EVP_CIPHER_CTX_final_used(NativeRef.EVP_CIPHER_CTX var0);

    static native void EVP_CIPHER_CTX_set_padding(NativeRef.EVP_CIPHER_CTX var0, boolean var1);

    static native void EVP_CIPHER_CTX_set_key_length(NativeRef.EVP_CIPHER_CTX var0, int var1);

    static native void EVP_CIPHER_CTX_free(long var0);

    static native long EVP_aead_aes_128_gcm();

    static native long EVP_aead_aes_256_gcm();

    static native long EVP_aead_chacha20_poly1305();

    static native long EVP_aead_aes_128_gcm_siv();

    static native long EVP_aead_aes_256_gcm_siv();

    static native int EVP_AEAD_max_overhead(long var0);

    static native int EVP_AEAD_nonce_length(long var0);

    static native int EVP_AEAD_CTX_seal(long var0, byte[] var2, int var3, byte[] var4, int var5, byte[] var6, byte[] var7, int var8, int var9, byte[] var10) throws ShortBufferException, BadPaddingException;

    static native int EVP_AEAD_CTX_seal_buf(long var0, byte[] var2, int var3, ByteBuffer var4, byte[] var5, ByteBuffer var6, byte[] var7) throws ShortBufferException, BadPaddingException;

    static native int EVP_AEAD_CTX_open(long var0, byte[] var2, int var3, byte[] var4, int var5, byte[] var6, byte[] var7, int var8, int var9, byte[] var10) throws ShortBufferException, BadPaddingException;

    static native int EVP_AEAD_CTX_open_buf(long var0, byte[] var2, int var3, ByteBuffer var4, byte[] var5, ByteBuffer var6, byte[] var7) throws ShortBufferException, BadPaddingException;

    static native long HMAC_CTX_new();

    static native void HMAC_CTX_free(long var0);

    static native void HMAC_Init_ex(NativeRef.HMAC_CTX var0, byte[] var1, long var2);

    static native void HMAC_Update(NativeRef.HMAC_CTX var0, byte[] var1, int var2, int var3);

    static native void HMAC_UpdateDirect(NativeRef.HMAC_CTX var0, long var1, int var3);

    static native byte[] HMAC_Final(NativeRef.HMAC_CTX var0);

    static native void RAND_bytes(byte[] var0);

    static int X509_NAME_hash(X500Principal x500Principal) {
        return NativeCrypto.X509_NAME_hash(x500Principal, "SHA1");
    }

    public static int X509_NAME_hash_old(X500Principal x500Principal) {
        return NativeCrypto.X509_NAME_hash(x500Principal, "MD5");
    }

    private static int X509_NAME_hash(X500Principal object, String string) {
        try {
            byte[] byArray = MessageDigest.getInstance(string).digest(((X500Principal)object).getEncoded());
            object = byArray;
            return byArray[0] & 0xFF | (object[1] & 0xFF) << 8 | (object[2] & 0xFF) << 16 | (object[3] & 0xFF) << 24;
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new AssertionError((Object)noSuchAlgorithmException);
        }
    }

    static native long d2i_X509_bio(long var0);

    static native long d2i_X509(byte[] var0) throws OpenSSLX509CertificateFactory.ParsingException;

    static native long PEM_read_bio_X509(long var0);

    static native byte[] i2d_X509(long var0, OpenSSLX509Certificate var2);

    static native byte[] i2d_X509_PUBKEY(long var0, OpenSSLX509Certificate var2);

    static native byte[] ASN1_seq_pack_X509(long[] var0);

    static native long[] ASN1_seq_unpack_X509_bio(long var0) throws OpenSSLX509CertificateFactory.ParsingException;

    static native void X509_free(long var0, OpenSSLX509Certificate var2);

    static native long X509_dup(long var0, OpenSSLX509Certificate var2);

    static native int X509_cmp(long var0, OpenSSLX509Certificate var2, long var3, OpenSSLX509Certificate var5);

    static native void X509_print_ex(long var0, long var2, OpenSSLX509Certificate var4, long var5, long var7);

    static native byte[] X509_get_issuer_name(long var0, OpenSSLX509Certificate var2);

    static native byte[] X509_get_subject_name(long var0, OpenSSLX509Certificate var2);

    static native String get_X509_sig_alg_oid(long var0, OpenSSLX509Certificate var2);

    static native byte[] get_X509_sig_alg_parameter(long var0, OpenSSLX509Certificate var2);

    static native boolean[] get_X509_issuerUID(long var0, OpenSSLX509Certificate var2);

    static native boolean[] get_X509_subjectUID(long var0, OpenSSLX509Certificate var2);

    static native long X509_get_pubkey(long var0, OpenSSLX509Certificate var2) throws NoSuchAlgorithmException, InvalidKeyException;

    static native String get_X509_pubkey_oid(long var0, OpenSSLX509Certificate var2);

    static native byte[] X509_get_ext_oid(long var0, OpenSSLX509Certificate var2, String var3);

    static native String[] get_X509_ext_oids(long var0, OpenSSLX509Certificate var2, int var3);

    static native Object[][] get_X509_GENERAL_NAME_stack(long var0, OpenSSLX509Certificate var2, int var3) throws CertificateParsingException;

    static native boolean[] get_X509_ex_kusage(long var0, OpenSSLX509Certificate var2);

    static native String[] get_X509_ex_xkusage(long var0, OpenSSLX509Certificate var2);

    static native int get_X509_ex_pathlen(long var0, OpenSSLX509Certificate var2);

    static native long X509_get_notBefore(long var0, OpenSSLX509Certificate var2);

    static native long X509_get_notAfter(long var0, OpenSSLX509Certificate var2);

    static native long X509_get_version(long var0, OpenSSLX509Certificate var2);

    static native byte[] X509_get_serialNumber(long var0, OpenSSLX509Certificate var2);

    static native void X509_verify(long var0, OpenSSLX509Certificate var2, NativeRef.EVP_PKEY var3) throws BadPaddingException;

    static native byte[] get_X509_cert_info_enc(long var0, OpenSSLX509Certificate var2);

    static native byte[] get_X509_signature(long var0, OpenSSLX509Certificate var2);

    static native int get_X509_ex_flags(long var0, OpenSSLX509Certificate var2);

    static native int X509_check_issued(long var0, OpenSSLX509Certificate var2, long var3, OpenSSLX509Certificate var5);

    static native long[] d2i_PKCS7_bio(long var0, int var2) throws OpenSSLX509CertificateFactory.ParsingException;

    static native byte[] i2d_PKCS7(long[] var0);

    static native long[] PEM_read_bio_PKCS7(long var0, int var2);

    static native long d2i_X509_CRL_bio(long var0);

    static native long PEM_read_bio_X509_CRL(long var0);

    static native byte[] i2d_X509_CRL(long var0, OpenSSLX509CRL var2);

    static native void X509_CRL_free(long var0, OpenSSLX509CRL var2);

    static native void X509_CRL_print(long var0, long var2, OpenSSLX509CRL var4);

    static native String get_X509_CRL_sig_alg_oid(long var0, OpenSSLX509CRL var2);

    static native byte[] get_X509_CRL_sig_alg_parameter(long var0, OpenSSLX509CRL var2);

    static native byte[] X509_CRL_get_issuer_name(long var0, OpenSSLX509CRL var2);

    static native long X509_CRL_get0_by_cert(long var0, OpenSSLX509CRL var2, long var3, OpenSSLX509Certificate var5);

    static native long X509_CRL_get0_by_serial(long var0, OpenSSLX509CRL var2, byte[] var3);

    static native long[] X509_CRL_get_REVOKED(long var0, OpenSSLX509CRL var2);

    static native String[] get_X509_CRL_ext_oids(long var0, OpenSSLX509CRL var2, int var3);

    static native byte[] X509_CRL_get_ext_oid(long var0, OpenSSLX509CRL var2, String var3);

    static native void X509_delete_ext(long var0, OpenSSLX509Certificate var2, String var3);

    static native long X509_CRL_get_version(long var0, OpenSSLX509CRL var2);

    static native long X509_CRL_get_ext(long var0, OpenSSLX509CRL var2, String var3);

    static native byte[] get_X509_CRL_signature(long var0, OpenSSLX509CRL var2);

    static native void X509_CRL_verify(long var0, OpenSSLX509CRL var2, NativeRef.EVP_PKEY var3) throws BadPaddingException, SignatureException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException;

    static native byte[] get_X509_CRL_crl_enc(long var0, OpenSSLX509CRL var2);

    static native long X509_CRL_get_lastUpdate(long var0, OpenSSLX509CRL var2);

    static native long X509_CRL_get_nextUpdate(long var0, OpenSSLX509CRL var2);

    static native long X509_REVOKED_dup(long var0);

    static native byte[] i2d_X509_REVOKED(long var0);

    static native String[] get_X509_REVOKED_ext_oids(long var0, int var2);

    static native byte[] X509_REVOKED_get_ext_oid(long var0, String var2);

    static native byte[] X509_REVOKED_get_serialNumber(long var0);

    static native long X509_REVOKED_get_ext(long var0, String var2);

    static native long get_X509_REVOKED_revocationDate(long var0);

    static native void X509_REVOKED_print(long var0, long var2);

    static native int X509_supported_extension(long var0);

    static native void ASN1_TIME_to_Calendar(long var0, Calendar var2) throws OpenSSLX509CertificateFactory.ParsingException;

    static native long asn1_read_init(byte[] var0) throws IOException;

    static native long asn1_read_sequence(long var0) throws IOException;

    static native boolean asn1_read_next_tag_is(long var0, int var2) throws IOException;

    static native long asn1_read_tagged(long var0) throws IOException;

    static native byte[] asn1_read_octetstring(long var0) throws IOException;

    static native long asn1_read_uint64(long var0) throws IOException;

    static native void asn1_read_null(long var0) throws IOException;

    static native String asn1_read_oid(long var0) throws IOException;

    static native boolean asn1_read_is_empty(long var0);

    static native void asn1_read_free(long var0);

    static native long asn1_write_init() throws IOException;

    static native long asn1_write_sequence(long var0) throws IOException;

    static native long asn1_write_tag(long var0, int var2) throws IOException;

    static native void asn1_write_octetstring(long var0, byte[] var2) throws IOException;

    static native void asn1_write_uint64(long var0, long var2) throws IOException;

    static native void asn1_write_null(long var0) throws IOException;

    static native void asn1_write_oid(long var0, String var2) throws IOException;

    static native void asn1_write_flush(long var0) throws IOException;

    static native byte[] asn1_write_finish(long var0) throws IOException;

    static native void asn1_write_cleanup(long var0);

    static native void asn1_write_free(long var0);

    static native long create_BIO_InputStream(OpenSSLBIOInputStream var0, boolean var1);

    static native long create_BIO_OutputStream(OutputStream var0);

    static native void BIO_free_all(long var0);

    static String cipherSuiteToJava(String string) {
        if ("TLS_RSA_WITH_3DES_EDE_CBC_SHA".equals(string)) {
            return "SSL_RSA_WITH_3DES_EDE_CBC_SHA";
        }
        return string;
    }

    static String cipherSuiteFromJava(String string) {
        if ("SSL_RSA_WITH_3DES_EDE_CBC_SHA".equals(string)) {
            return "TLS_RSA_WITH_3DES_EDE_CBC_SHA";
        }
        return string;
    }

    static native int EVP_has_aes_hardware();

    static native long SSL_CTX_new();

    static String[] getSupportedCipherSuites() {
        return SSLUtils.concat(SUPPORTED_TLS_1_3_CIPHER_SUITES, (String[])SUPPORTED_TLS_1_2_CIPHER_SUITES.clone());
    }

    static native void SSL_CTX_free(long var0, AbstractSessionContext var2);

    static native void SSL_CTX_set_session_id_context(long var0, AbstractSessionContext var2, byte[] var3);

    static native long SSL_CTX_set_timeout(long var0, AbstractSessionContext var2, long var3);

    static native long SSL_new(long var0, AbstractSessionContext var2) throws SSLException;

    static native void SSL_enable_tls_channel_id(long var0, NativeSsl var2) throws SSLException;

    static native byte[] SSL_get_tls_channel_id(long var0, NativeSsl var2) throws SSLException;

    static native void SSL_set1_tls_channel_id(long var0, NativeSsl var2, NativeRef.EVP_PKEY var3);

    static native void setLocalCertsAndPrivateKey(long var0, NativeSsl var2, byte[][] var3, NativeRef.EVP_PKEY var4) throws SSLException;

    static native void SSL_set_client_CA_list(long var0, NativeSsl var2, byte[][] var3) throws SSLException;

    static native long SSL_set_mode(long var0, NativeSsl var2, long var3);

    static native long SSL_set_options(long var0, NativeSsl var2, long var3);

    static native long SSL_clear_options(long var0, NativeSsl var2, long var3);

    static native int SSL_set_protocol_versions(long var0, NativeSsl var2, int var3, int var4);

    static native void SSL_enable_signed_cert_timestamps(long var0, NativeSsl var2);

    static native byte[] SSL_get_signed_cert_timestamp_list(long var0, NativeSsl var2);

    static native void SSL_set_signed_cert_timestamp_list(long var0, NativeSsl var2, byte[] var3);

    static native void SSL_enable_ocsp_stapling(long var0, NativeSsl var2);

    static native byte[] SSL_get_ocsp_response(long var0, NativeSsl var2);

    static native void SSL_set_ocsp_response(long var0, NativeSsl var2, byte[] var3);

    static native byte[] SSL_get_tls_unique(long var0, NativeSsl var2);

    static native byte[] SSL_export_keying_material(long var0, NativeSsl var2, byte[] var3, byte[] var4, int var5) throws SSLException;

    static native void SSL_use_psk_identity_hint(long var0, NativeSsl var2, String var3) throws SSLException;

    static native void set_SSL_psk_client_callback_enabled(long var0, NativeSsl var2, boolean var3);

    static native void set_SSL_psk_server_callback_enabled(long var0, NativeSsl var2, boolean var3);

    static String[] getSupportedProtocols() {
        return (String[])SUPPORTED_PROTOCOLS.clone();
    }

    private static Range getProtocolRange(String[] object) {
        object = Arrays.asList(object);
        String string = null;
        String string2 = null;
        for (int i2 = 0; i2 < SUPPORTED_PROTOCOLS.length; ++i2) {
            String string3 = SUPPORTED_PROTOCOLS[i2];
            if (object.contains(string3)) {
                if (string == null) {
                    string = string3;
                }
                string2 = string3;
                continue;
            }
            if (string != null) break;
        }
        if (string == null || string2 == null) {
            throw new IllegalArgumentException("No protocols enabled.");
        }
        return new Range(string, string2);
    }

    static void setEnabledProtocols(long l2, NativeSsl nativeSsl, String[] object) {
        NativeCrypto.checkEnabledProtocols(object);
        object = NativeCrypto.getProtocolRange(object);
        NativeCrypto.SSL_set_protocol_versions(l2, nativeSsl, NativeCrypto.getProtocolConstant(object.min), NativeCrypto.getProtocolConstant(object.max));
    }

    private static int getProtocolConstant(String string) {
        if (string.equals(SUPPORTED_PROTOCOL_TLSV1)) {
            return 769;
        }
        if (string.equals(SUPPORTED_PROTOCOL_TLSV1_1)) {
            return 770;
        }
        if (string.equals(SUPPORTED_PROTOCOL_TLSV1_2)) {
            return 771;
        }
        if (string.equals(SUPPORTED_PROTOCOL_TLSV1_3)) {
            return 772;
        }
        throw new AssertionError((Object)("Unknown protocol encountered: " + string));
    }

    static String[] checkEnabledProtocols(String[] stringArray) {
        if (stringArray == null) {
            throw new IllegalArgumentException("protocols == null");
        }
        String[] stringArray2 = stringArray;
        int n2 = stringArray.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            String string = stringArray2[i2];
            if (string == null) {
                throw new IllegalArgumentException("protocols contains null");
            }
            if (string.equals(SUPPORTED_PROTOCOL_TLSV1) || string.equals(SUPPORTED_PROTOCOL_TLSV1_1) || string.equals(SUPPORTED_PROTOCOL_TLSV1_2) || string.equals(SUPPORTED_PROTOCOL_TLSV1_3) || string.equals(OBSOLETE_PROTOCOL_SSLV3)) continue;
            throw new IllegalArgumentException("protocol " + string + " is not supported");
        }
        return stringArray;
    }

    static native void SSL_set_cipher_lists(long var0, NativeSsl var2, String[] var3);

    static native long[] SSL_get_ciphers(long var0, NativeSsl var2);

    static void setEnabledCipherSuites(long l2, NativeSsl nativeSsl, String[] stringArray, String[] object) {
        NativeCrypto.checkEnabledCipherSuites(stringArray);
        object = NativeCrypto.getProtocolRange((String[])object).max;
        ArrayList<String> arrayList = new ArrayList<String>();
        for (int i2 = 0; i2 < stringArray.length; ++i2) {
            String string = stringArray[i2];
            if (string.equals(TLS_EMPTY_RENEGOTIATION_INFO_SCSV)) continue;
            if (string.equals(TLS_FALLBACK_SCSV) && (((String)object).equals(SUPPORTED_PROTOCOL_TLSV1) || ((String)object).equals(SUPPORTED_PROTOCOL_TLSV1_1))) {
                NativeCrypto.SSL_set_mode(l2, nativeSsl, 1024L);
                continue;
            }
            arrayList.add(NativeCrypto.cipherSuiteFromJava(string));
        }
        ArrayList<String> arrayList2 = arrayList;
        NativeCrypto.SSL_set_cipher_lists(l2, nativeSsl, arrayList2.toArray(new String[arrayList2.size()]));
    }

    static String[] checkEnabledCipherSuites(String[] stringArray) {
        if (stringArray == null) {
            throw new IllegalArgumentException("cipherSuites == null");
        }
        for (int i2 = 0; i2 < stringArray.length; ++i2) {
            if (stringArray[i2] == null) {
                throw new IllegalArgumentException("cipherSuites[" + i2 + "] == null");
            }
            if (stringArray[i2].equals(TLS_EMPTY_RENEGOTIATION_INFO_SCSV) || stringArray[i2].equals(TLS_FALLBACK_SCSV) || SUPPORTED_TLS_1_2_CIPHER_SUITES_SET.contains(stringArray[i2]) || SUPPORTED_LEGACY_CIPHER_SUITES_SET.contains(stringArray[i2])) continue;
            throw new IllegalArgumentException("cipherSuite " + stringArray[i2] + " is not supported.");
        }
        return stringArray;
    }

    static native void SSL_set_accept_state(long var0, NativeSsl var2);

    static native void SSL_set_connect_state(long var0, NativeSsl var2);

    static native void SSL_set_verify(long var0, NativeSsl var2, int var3);

    static native void SSL_set_session(long var0, NativeSsl var2, long var3) throws SSLException;

    static native void SSL_set_session_creation_enabled(long var0, NativeSsl var2, boolean var3) throws SSLException;

    static native boolean SSL_session_reused(long var0, NativeSsl var2);

    static native void SSL_accept_renegotiations(long var0, NativeSsl var2) throws SSLException;

    static native void SSL_set_tlsext_host_name(long var0, NativeSsl var2, String var3) throws SSLException;

    static native String SSL_get_servername(long var0, NativeSsl var2);

    static native void SSL_do_handshake(long var0, NativeSsl var2, FileDescriptor var3, SSLHandshakeCallbacks var4, int var5) throws SSLException, SocketTimeoutException, CertificateException;

    public static native String SSL_get_current_cipher(long var0, NativeSsl var2);

    public static native String SSL_get_version(long var0, NativeSsl var2);

    static native byte[][] SSL_get0_peer_certificates(long var0, NativeSsl var2);

    static native int SSL_read(long var0, NativeSsl var2, FileDescriptor var3, SSLHandshakeCallbacks var4, byte[] var5, int var6, int var7, int var8) throws IOException;

    static native void SSL_write(long var0, NativeSsl var2, FileDescriptor var3, SSLHandshakeCallbacks var4, byte[] var5, int var6, int var7, int var8) throws IOException;

    static native void SSL_interrupt(long var0, NativeSsl var2);

    static native void SSL_shutdown(long var0, NativeSsl var2, FileDescriptor var3, SSLHandshakeCallbacks var4) throws IOException;

    static native int SSL_get_shutdown(long var0, NativeSsl var2);

    static native void SSL_free(long var0, NativeSsl var2);

    static native long SSL_get_time(long var0, NativeSsl var2);

    static native long SSL_set_timeout(long var0, NativeSsl var2, long var3);

    static native long SSL_get_timeout(long var0, NativeSsl var2);

    static native int SSL_get_signature_algorithm_key_type(int var0);

    static native byte[] SSL_session_id(long var0, NativeSsl var2);

    static native byte[] SSL_SESSION_session_id(long var0);

    static native long SSL_SESSION_get_time(long var0);

    static native long SSL_SESSION_get_timeout(long var0);

    static native String SSL_SESSION_get_version(long var0);

    static native String SSL_SESSION_cipher(long var0);

    static native boolean SSL_SESSION_should_be_single_use(long var0);

    static native void SSL_SESSION_up_ref(long var0);

    static native void SSL_SESSION_free(long var0);

    static native byte[] i2d_SSL_SESSION(long var0);

    static native long d2i_SSL_SESSION(byte[] var0) throws IOException;

    static native String SSL_CIPHER_get_kx_name(long var0);

    static native String[] get_cipher_names(String var0);

    public static native byte[] get_ocsp_single_extension(byte[] var0, String var1, long var2, OpenSSLX509Certificate var4, long var5, OpenSSLX509Certificate var7);

    static native long getDirectBufferAddress(Buffer var0);

    static native long SSL_BIO_new(long var0, NativeSsl var2) throws SSLException;

    static native int SSL_get_error(long var0, NativeSsl var2, int var3);

    static native void SSL_clear_error();

    static native int SSL_pending_readable_bytes(long var0, NativeSsl var2);

    static native int SSL_pending_written_bytes_in_BIO(long var0);

    static native int SSL_max_seal_overhead(long var0, NativeSsl var2);

    static native void setApplicationProtocols(long var0, NativeSsl var2, boolean var3, byte[] var4) throws IOException;

    static native void setHasApplicationProtocolSelector(long var0, NativeSsl var2, boolean var3) throws IOException;

    static native byte[] getApplicationProtocol(long var0, NativeSsl var2);

    static native int ENGINE_SSL_do_handshake(long var0, NativeSsl var2, SSLHandshakeCallbacks var3) throws IOException;

    static native int ENGINE_SSL_read_direct(long var0, NativeSsl var2, long var3, int var5, SSLHandshakeCallbacks var6) throws IOException, CertificateException;

    static native int ENGINE_SSL_write_direct(long var0, NativeSsl var2, long var3, int var5, SSLHandshakeCallbacks var6) throws IOException;

    static native int ENGINE_SSL_write_BIO_direct(long var0, NativeSsl var2, long var3, long var5, int var7, SSLHandshakeCallbacks var8) throws IOException;

    static native int ENGINE_SSL_write_BIO_heap(long var0, NativeSsl var2, long var3, byte[] var5, int var6, int var7, SSLHandshakeCallbacks var8) throws IOException, IndexOutOfBoundsException;

    static native int ENGINE_SSL_read_BIO_direct(long var0, NativeSsl var2, long var3, long var5, int var7, SSLHandshakeCallbacks var8) throws IOException;

    static native int ENGINE_SSL_read_BIO_heap(long var0, NativeSsl var2, long var3, byte[] var5, int var6, int var7, SSLHandshakeCallbacks var8) throws IOException, IndexOutOfBoundsException;

    static native void ENGINE_SSL_force_read(long var0, NativeSsl var2, SSLHandshakeCallbacks var3) throws IOException;

    static native void ENGINE_SSL_shutdown(long var0, NativeSsl var2, SSLHandshakeCallbacks var3) throws IOException;

    static native int BIO_read(long var0, byte[] var2) throws IOException;

    static native void BIO_write(long var0, byte[] var2, int var3, int var4) throws IOException, IndexOutOfBoundsException;

    static native long SSL_clear_mode(long var0, NativeSsl var2, long var3);

    static native long SSL_get_mode(long var0, NativeSsl var2);

    static native long SSL_get_options(long var0, NativeSsl var2);

    static native long SSL_get1_session(long var0, NativeSsl var2);

    static {
        String[] stringArray;
        String[] stringArray2 = null;
        try {
            NativeCryptoJni.init();
            NativeCrypto.clinit();
        }
        catch (UnsatisfiedLinkError stringArray3) {
            String[] stringArray4 = stringArray3;
            stringArray2 = stringArray3;
        }
        loadError = stringArray2;
        SUPPORTED_TLS_1_3_CIPHER_SUITES = new String[]{"TLS_AES_128_GCM_SHA256", "TLS_AES_256_GCM_SHA384", "TLS_CHACHA20_POLY1305_SHA256"};
        SUPPORTED_TLS_1_2_CIPHER_SUITES_SET = new HashSet<String>();
        SUPPORTED_LEGACY_CIPHER_SUITES_SET = new HashSet<String>();
        SUPPORTED_TLS_1_3_CIPHER_SUITES_SET = new HashSet<String>(Arrays.asList(SUPPORTED_TLS_1_3_CIPHER_SUITES));
        if (loadError == null) {
            stringArray2 = NativeCrypto.get_cipher_names("ALL:!DHE");
            int n2 = stringArray2.length;
            if (n2 % 2 != 0) {
                throw new IllegalArgumentException("Invalid cipher list returned by get_cipher_names");
            }
            SUPPORTED_TLS_1_2_CIPHER_SUITES = new String[n2 / 2 + 2];
            for (int i2 = 0; i2 < n2; i2 += 2) {
                String string;
                NativeCrypto.SUPPORTED_TLS_1_2_CIPHER_SUITES[i2 / 2] = string = NativeCrypto.cipherSuiteToJava(stringArray2[i2]);
                SUPPORTED_TLS_1_2_CIPHER_SUITES_SET.add(string);
                SUPPORTED_LEGACY_CIPHER_SUITES_SET.add(stringArray2[i2 + 1]);
            }
            NativeCrypto.SUPPORTED_TLS_1_2_CIPHER_SUITES[n2 / 2] = TLS_EMPTY_RENEGOTIATION_INFO_SCSV;
            NativeCrypto.SUPPORTED_TLS_1_2_CIPHER_SUITES[n2 / 2 + 1] = TLS_FALLBACK_SCSV;
            HAS_AES_HARDWARE = NativeCrypto.EVP_has_aes_hardware() == 1;
        } else {
            HAS_AES_HARDWARE = false;
            SUPPORTED_TLS_1_2_CIPHER_SUITES = new String[0];
        }
        if (HAS_AES_HARDWARE) {
            String[] stringArray5 = new String[14];
            stringArray5[0] = "TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256";
            stringArray5[1] = "TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384";
            stringArray5[2] = "TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256";
            stringArray5[3] = "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256";
            stringArray5[4] = "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384";
            stringArray5[5] = "TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256";
            stringArray5[6] = "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA";
            stringArray5[7] = "TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA";
            stringArray5[8] = "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA";
            stringArray5[9] = "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA";
            stringArray5[10] = "TLS_RSA_WITH_AES_128_GCM_SHA256";
            stringArray5[11] = "TLS_RSA_WITH_AES_256_GCM_SHA384";
            stringArray5[12] = "TLS_RSA_WITH_AES_128_CBC_SHA";
            stringArray = stringArray5;
            stringArray5[13] = "TLS_RSA_WITH_AES_256_CBC_SHA";
        } else {
            String[] stringArray6 = new String[14];
            stringArray6[0] = "TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256";
            stringArray6[1] = "TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256";
            stringArray6[2] = "TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384";
            stringArray6[3] = "TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256";
            stringArray6[4] = "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256";
            stringArray6[5] = "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384";
            stringArray6[6] = "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA";
            stringArray6[7] = "TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA";
            stringArray6[8] = "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA";
            stringArray6[9] = "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA";
            stringArray6[10] = "TLS_RSA_WITH_AES_128_GCM_SHA256";
            stringArray6[11] = "TLS_RSA_WITH_AES_256_GCM_SHA384";
            stringArray6[12] = "TLS_RSA_WITH_AES_128_CBC_SHA";
            stringArray = stringArray6;
            stringArray6[13] = "TLS_RSA_WITH_AES_256_CBC_SHA";
        }
        DEFAULT_X509_CIPHER_SUITES = stringArray;
        DEFAULT_PSK_CIPHER_SUITES = new String[]{"TLS_ECDHE_PSK_WITH_CHACHA20_POLY1305_SHA256", "TLS_ECDHE_PSK_WITH_AES_128_CBC_SHA", "TLS_ECDHE_PSK_WITH_AES_256_CBC_SHA", "TLS_PSK_WITH_AES_128_CBC_SHA", "TLS_PSK_WITH_AES_256_CBC_SHA"};
        TLSV13_PROTOCOLS = new String[]{SUPPORTED_PROTOCOL_TLSV1, SUPPORTED_PROTOCOL_TLSV1_1, SUPPORTED_PROTOCOL_TLSV1_2, SUPPORTED_PROTOCOL_TLSV1_3};
        TLSV12_PROTOCOLS = new String[]{SUPPORTED_PROTOCOL_TLSV1, SUPPORTED_PROTOCOL_TLSV1_1, SUPPORTED_PROTOCOL_TLSV1_2};
        TLSV11_PROTOCOLS = TLSV12_PROTOCOLS;
        TLSV1_PROTOCOLS = TLSV12_PROTOCOLS;
        DEFAULT_PROTOCOLS = TLSV13_PROTOCOLS;
        SUPPORTED_PROTOCOLS = new String[]{SUPPORTED_PROTOCOL_TLSV1, SUPPORTED_PROTOCOL_TLSV1_1, SUPPORTED_PROTOCOL_TLSV1_2, SUPPORTED_PROTOCOL_TLSV1_3};
    }

    static interface SSLHandshakeCallbacks {
        public void verifyCertificateChain(byte[][] var1, String var2) throws CertificateException;

        public void clientCertificateRequested(byte[] var1, int[] var2, byte[][] var3) throws CertificateEncodingException, SSLException;

        public void serverCertificateRequested() throws IOException;

        public int clientPSKKeyRequested(String var1, byte[] var2, byte[] var3);

        public int serverPSKKeyRequested(String var1, String var2, byte[] var3);

        public void onSSLStateChange(int var1, int var2);

        public void onNewSessionEstablished(long var1);

        public long serverSessionRequested(byte[] var1);

        public int selectApplicationProtocol(byte[] var1);
    }

    static class Range {
        public final String min;
        public final String max;

        public Range(String string, String string2) {
            this.min = string;
            this.max = string2;
        }
    }
}


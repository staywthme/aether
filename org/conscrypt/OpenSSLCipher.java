/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Locale;
import javax.crypto.BadPaddingException;
import javax.crypto.CipherSpi;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.conscrypt.EmptyArray;

public abstract class OpenSSLCipher
extends CipherSpi {
    Mode mode = Mode.ECB;
    private Padding padding = Padding.PKCS5PADDING;
    byte[] encodedKey;
    byte[] iv;
    private boolean encrypting;
    private int blockSize;

    OpenSSLCipher() {
    }

    OpenSSLCipher(Mode mode, Padding padding) {
        this.mode = mode;
        this.padding = padding;
        this.blockSize = this.getCipherBlockSize();
    }

    abstract void engineInitInternal(byte[] var1, AlgorithmParameterSpec var2, SecureRandom var3) throws InvalidKeyException, InvalidAlgorithmParameterException;

    abstract int updateInternal(byte[] var1, int var2, int var3, byte[] var4, int var5, int var6) throws ShortBufferException;

    abstract int doFinalInternal(byte[] var1, int var2, int var3) throws IllegalBlockSizeException, BadPaddingException, ShortBufferException;

    abstract String getBaseCipherName();

    abstract void checkSupportedKeySize(int var1) throws InvalidKeyException;

    abstract void checkSupportedMode(Mode var1) throws NoSuchAlgorithmException;

    abstract void checkSupportedPadding(Padding var1) throws NoSuchPaddingException;

    abstract int getCipherBlockSize();

    boolean supportsVariableSizeKey() {
        return false;
    }

    boolean supportsVariableSizeIv() {
        return false;
    }

    @Override
    protected void engineSetMode(String object) throws NoSuchAlgorithmException {
        Mode mode;
        try {
            mode = Mode.getNormalized((String)object);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            object = new NoSuchAlgorithmException("No such mode: " + (String)object);
            ((Throwable)object).initCause(illegalArgumentException);
            throw object;
        }
        this.checkSupportedMode(mode);
        this.mode = mode;
    }

    @Override
    protected void engineSetPadding(String object) throws NoSuchPaddingException {
        Padding padding;
        try {
            padding = Padding.getNormalized((String)object);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            object = new NoSuchPaddingException("No such padding: " + (String)object);
            ((Throwable)object).initCause(illegalArgumentException);
            throw object;
        }
        this.checkSupportedPadding(padding);
        this.padding = padding;
    }

    Padding getPadding() {
        return this.padding;
    }

    @Override
    protected int engineGetBlockSize() {
        return this.blockSize;
    }

    abstract int getOutputSizeForFinal(int var1);

    abstract int getOutputSizeForUpdate(int var1);

    @Override
    protected int engineGetOutputSize(int n2) {
        return Math.max(this.getOutputSizeForUpdate(n2), this.getOutputSizeForFinal(n2));
    }

    @Override
    protected byte[] engineGetIV() {
        return this.iv;
    }

    @Override
    protected AlgorithmParameters engineGetParameters() {
        if (this.iv != null && this.iv.length > 0) {
            try {
                AlgorithmParameters algorithmParameters = AlgorithmParameters.getInstance(this.getBaseCipherName());
                algorithmParameters.init(new IvParameterSpec(this.iv));
                return algorithmParameters;
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                return null;
            }
            catch (InvalidParameterSpecException invalidParameterSpecException) {
                return null;
            }
        }
        return null;
    }

    protected AlgorithmParameterSpec getParameterSpec(AlgorithmParameters algorithmParameters) throws InvalidAlgorithmParameterException {
        if (algorithmParameters != null) {
            try {
                return algorithmParameters.getParameterSpec(IvParameterSpec.class);
            }
            catch (InvalidParameterSpecException invalidParameterSpecException) {
                throw new InvalidAlgorithmParameterException("Params must be convertible to IvParameterSpec", invalidParameterSpecException);
            }
        }
        return null;
    }

    @Override
    protected void engineInit(int n2, Key key, SecureRandom secureRandom) throws InvalidKeyException {
        this.checkAndSetEncodedKey(n2, key);
        try {
            OpenSSLCipher openSSLCipher = this;
            openSSLCipher.engineInitInternal(openSSLCipher.encodedKey, null, secureRandom);
            return;
        }
        catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException) {
            throw new RuntimeException(invalidAlgorithmParameterException);
        }
    }

    @Override
    protected void engineInit(int n2, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        this.checkAndSetEncodedKey(n2, key);
        OpenSSLCipher openSSLCipher = this;
        openSSLCipher.engineInitInternal(openSSLCipher.encodedKey, algorithmParameterSpec, secureRandom);
    }

    @Override
    protected void engineInit(int n2, Key key, AlgorithmParameters object, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        object = this.getParameterSpec((AlgorithmParameters)object);
        this.engineInit(n2, key, (AlgorithmParameterSpec)object, secureRandom);
    }

    @Override
    protected byte[] engineUpdate(byte[] byArray, int n2, int n3) {
        int n4;
        int n5 = this.getOutputSizeForUpdate(n3);
        byte[] byArray2 = n5 > 0 ? new byte[n5] : EmptyArray.BYTE;
        try {
            n4 = this.updateInternal(byArray, n2, n3, byArray2, 0, n5);
        }
        catch (ShortBufferException shortBufferException) {
            throw new RuntimeException("calculated buffer size was wrong: " + n5);
        }
        if (byArray2.length == n4) {
            return byArray2;
        }
        if (n4 == 0) {
            return EmptyArray.BYTE;
        }
        return Arrays.copyOfRange(byArray2, 0, n4);
    }

    @Override
    protected int engineUpdate(byte[] byArray, int n2, int n3, byte[] byArray2, int n4) throws ShortBufferException {
        int n5 = this.getOutputSizeForUpdate(n3);
        return this.updateInternal(byArray, n2, n3, byArray2, n4, n5);
    }

    @Override
    protected byte[] engineDoFinal(byte[] byArray, int n2, int n3) throws IllegalBlockSizeException, BadPaddingException {
        int n4;
        int n5 = this.getOutputSizeForFinal(n3);
        byte[] byArray2 = new byte[n5];
        if (n3 > 0) {
            try {
                n4 = this.updateInternal(byArray, n2, n3, byArray2, 0, n5);
            }
            catch (ShortBufferException shortBufferException) {
                throw new RuntimeException("our calculated buffer was too small", shortBufferException);
            }
        } else {
            n4 = 0;
        }
        try {
            n4 += this.doFinalInternal(byArray2, n4, n5 - n4);
        }
        catch (ShortBufferException shortBufferException) {
            throw new RuntimeException("our calculated buffer was too small", shortBufferException);
        }
        if (n4 == byArray2.length) {
            return byArray2;
        }
        if (n4 == 0) {
            return EmptyArray.BYTE;
        }
        return Arrays.copyOfRange(byArray2, 0, n4);
    }

    @Override
    protected int engineDoFinal(byte[] byArray, int n2, int n3, byte[] byArray2, int n4) throws ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        int n5;
        if (byArray2 == null) {
            throw new NullPointerException("output == null");
        }
        int n6 = this.getOutputSizeForFinal(n3);
        if (n3 > 0) {
            n5 = this.updateInternal(byArray, n2, n3, byArray2, n4, n6);
            n4 += n5;
            n6 -= n5;
        } else {
            n5 = 0;
        }
        return n5 + this.doFinalInternal(byArray2, n4, n6);
    }

    @Override
    protected byte[] engineWrap(Key object) throws IllegalBlockSizeException, InvalidKeyException {
        try {
            object = object.getEncoded();
            return this.engineDoFinal((byte[])object, 0, ((Object)object).length);
        }
        catch (BadPaddingException badPaddingException) {
            IllegalBlockSizeException illegalBlockSizeException = new IllegalBlockSizeException();
            illegalBlockSizeException.initCause(badPaddingException);
            throw illegalBlockSizeException;
        }
    }

    @Override
    protected Key engineUnwrap(byte[] byArray, String object, int n2) throws InvalidKeyException, NoSuchAlgorithmException {
        try {
            byArray = this.engineDoFinal(byArray, 0, byArray.length);
            if (n2 == 1) {
                object = KeyFactory.getInstance((String)object);
                return ((KeyFactory)object).generatePublic(new X509EncodedKeySpec(byArray));
            }
            if (n2 == 2) {
                object = KeyFactory.getInstance((String)object);
                return ((KeyFactory)object).generatePrivate(new PKCS8EncodedKeySpec(byArray));
            }
            if (n2 == 3) {
                return new SecretKeySpec(byArray, (String)object);
            }
            throw new UnsupportedOperationException("wrappedKeyType == " + n2);
        }
        catch (IllegalBlockSizeException illegalBlockSizeException) {
            throw new InvalidKeyException(illegalBlockSizeException);
        }
        catch (BadPaddingException badPaddingException) {
            throw new InvalidKeyException(badPaddingException);
        }
        catch (InvalidKeySpecException invalidKeySpecException) {
            throw new InvalidKeyException(invalidKeySpecException);
        }
    }

    @Override
    protected int engineGetKeySize(Key object) throws InvalidKeyException {
        if (!(object instanceof SecretKey)) {
            throw new InvalidKeyException("Only SecretKey is supported");
        }
        byte[] byArray = object.getEncoded();
        object = byArray;
        if (byArray == null) {
            throw new InvalidKeyException("key.getEncoded() == null");
        }
        this.checkSupportedKeySize(((Object)object).length);
        return ((Object)object).length << 3;
    }

    private byte[] checkAndSetEncodedKey(int n2, Key key) throws InvalidKeyException {
        if (n2 == 1 || n2 == 3) {
            this.encrypting = true;
        } else if (n2 == 2 || n2 == 4) {
            this.encrypting = false;
        } else {
            throw new InvalidParameterException("Unsupported opmode " + n2);
        }
        if (!(key instanceof SecretKey)) {
            throw new InvalidKeyException("Only SecretKey is supported");
        }
        byte[] byArray = key.getEncoded();
        if (byArray == null) {
            throw new InvalidKeyException("key.getEncoded() == null");
        }
        this.checkSupportedKeySize(byArray.length);
        this.encodedKey = byArray;
        return byArray;
    }

    boolean isEncrypting() {
        return this.encrypting;
    }

    static enum Padding {
        NOPADDING,
        PKCS5PADDING,
        PKCS7PADDING;


        public static Padding getNormalized(String object) {
            if ((object = Padding.valueOf(object.toUpperCase(Locale.US))) == PKCS7PADDING) {
                return PKCS5PADDING;
            }
            return object;
        }
    }

    static enum Mode {
        NONE,
        CBC,
        CTR,
        ECB,
        GCM,
        GCM_SIV,
        POLY1305;


        public static Mode getNormalized(String string) {
            if ((string = string.toUpperCase(Locale.US)).equals("GCM-SIV")) {
                return GCM_SIV;
            }
            if (string.equals("GCM_SIV")) {
                throw new IllegalArgumentException("Invalid mode");
            }
            return Mode.valueOf(string);
        }
    }
}


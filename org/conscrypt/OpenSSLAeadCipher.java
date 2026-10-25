/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import org.conscrypt.ArrayUtils;
import org.conscrypt.NativeCrypto;
import org.conscrypt.OpenSSLCipher;
import org.conscrypt.Platform;
import org.conscrypt.ShortBufferWithoutStackTraceException;

public abstract class OpenSSLAeadCipher
extends OpenSSLCipher {
    private static final boolean ENABLE_BYTEBUFFER_OPTIMIZATIONS = true;
    static final int DEFAULT_TAG_SIZE_BITS = 128;
    private static int lastGlobalMessageSize = 32;
    private byte[] previousKey;
    private byte[] previousIv;
    private boolean mustInitialize;
    byte[] buf;
    int bufCount;
    long evpAead;
    private byte[] aad;
    int tagLengthInBytes;

    public OpenSSLAeadCipher(OpenSSLCipher.Mode mode) {
        super(mode, OpenSSLCipher.Padding.NOPADDING);
    }

    private void checkInitialization() {
        if (this.mustInitialize) {
            throw new IllegalStateException("Cannot re-use same key and IV for multiple encryptions");
        }
    }

    private boolean arraysAreEqual(byte[] byArray, byte[] byArray2) {
        if (byArray.length != byArray2.length) {
            return false;
        }
        int n2 = 0;
        for (int i2 = 0; i2 < byArray.length; ++i2) {
            n2 |= byArray[i2] ^ byArray2[i2];
        }
        return n2 == 0;
    }

    private void expand(int n2) {
        if (this.bufCount + n2 <= this.buf.length) {
            return;
        }
        byte[] byArray = new byte[this.bufCount + n2 << 1];
        System.arraycopy(this.buf, 0, byArray, 0, this.bufCount);
        this.buf = byArray;
    }

    private void reset() {
        this.aad = null;
        int n2 = lastGlobalMessageSize;
        if (this.buf == null) {
            this.buf = new byte[n2];
        } else if (this.bufCount > 0 && this.bufCount != n2) {
            lastGlobalMessageSize = this.bufCount;
            if (this.buf.length != this.bufCount) {
                this.buf = new byte[this.bufCount];
            }
        }
        this.bufCount = 0;
    }

    /*
     * Unable to fully structure code
     */
    @Override
    void engineInitInternal(byte[] var1_1, AlgorithmParameterSpec var2_2, SecureRandom var3_3) throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (var2_2 == null) ** GOTO lbl-1000
        var4_4 = Platform.fromGCMParameterSpec((AlgorithmParameterSpec)var2_2);
        if (var4_4 != null) {
            var2_2 = var4_4.getIV();
            var4_5 = var4_4.getTLen();
        } else if (var2_2 instanceof IvParameterSpec) {
            var5_6 = (IvParameterSpec)var2_2;
            var2_2 = var5_6.getIV();
            var4_5 = 128;
        } else lbl-1000:
        // 2 sources

        {
            var2_2 = null;
            var4_5 = 128;
        }
        this.checkSupportedTagLength(var4_5);
        this.tagLengthInBytes = var4_5 / 8;
        var4_5 = (int)this.isEncrypting();
        this.evpAead = this.getEVP_AEAD(var1_1.length);
        var5_7 = NativeCrypto.EVP_AEAD_nonce_length(this.evpAead);
        if (var2_2 == null && var5_7 != 0) {
            if (var4_5 == 0) {
                throw new InvalidAlgorithmParameterException("IV must be specified in " + (Object)this.mode + " mode");
            }
            var2_2 = new byte[var5_7];
            if (var3_3 != null) {
                var3_3.nextBytes((byte[])var2_2);
            } else {
                NativeCrypto.RAND_bytes((byte[])var2_2);
            }
        } else {
            if (var5_7 == 0 && var2_2 != null) {
                throw new InvalidAlgorithmParameterException("IV not used in " + (Object)this.mode + " mode");
            }
            if (var2_2 != null && ((Object)var2_2).length != var5_7) {
                throw new InvalidAlgorithmParameterException("Expected IV length of " + var5_7 + " but was " + ((Object)var2_2).length);
            }
        }
        if (this.isEncrypting() && var2_2 != null && !this.allowsNonceReuse()) {
            if (this.previousKey != null && this.previousIv != null) {
                v0 = this;
                if (v0.arraysAreEqual(v0.previousKey, var1_1)) {
                    v1 = this;
                    if (v1.arraysAreEqual(v1.previousIv, (byte[])var2_2)) {
                        this.mustInitialize = true;
                        throw new InvalidAlgorithmParameterException("When using AEAD key and IV must not be re-used");
                    }
                }
            }
            this.previousKey = var1_1;
            this.previousIv = (byte[])var2_2;
        }
        this.mustInitialize = false;
        this.iv = (byte[])var2_2;
        this.reset();
    }

    void checkSupportedTagLength(int n2) throws InvalidAlgorithmParameterException {
        if (n2 % 8 != 0) {
            throw new InvalidAlgorithmParameterException("Tag length must be a multiple of 8; was " + n2);
        }
    }

    boolean allowsNonceReuse() {
        return false;
    }

    @Override
    protected int engineDoFinal(ByteBuffer byteBuffer, ByteBuffer byteBuffer2) throws ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        int n2;
        if (byteBuffer == null || byteBuffer2 == null) {
            throw new NullPointerException("Null ByteBuffer Error");
        }
        if (this.getOutputSizeForFinal(byteBuffer.remaining()) > byteBuffer2.remaining()) {
            throw new ShortBufferWithoutStackTraceException("Insufficient Bytes for Output Buffer");
        }
        if (byteBuffer2.isReadOnly()) {
            throw new IllegalArgumentException("Cannot write to Read Only ByteBuffer");
        }
        if (this.bufCount != 0) {
            return super.engineDoFinal(byteBuffer, byteBuffer2);
        }
        if (!byteBuffer.isDirect()) {
            int n3 = byteBuffer.remaining();
            ByteBuffer byteBuffer3 = ByteBuffer.allocateDirect(n3);
            byteBuffer3.mark();
            byteBuffer3.put(byteBuffer);
            byteBuffer3.reset();
            byteBuffer = byteBuffer3;
        }
        if (!byteBuffer2.isDirect()) {
            ByteBuffer byteBuffer4 = ByteBuffer.allocateDirect(this.getOutputSizeForFinal(byteBuffer.remaining()));
            n2 = this.doFinalInternal(byteBuffer, byteBuffer4);
            byteBuffer2.put(byteBuffer4);
            ByteBuffer byteBuffer5 = byteBuffer;
            byteBuffer5.position(byteBuffer5.limit());
        } else {
            n2 = this.doFinalInternal(byteBuffer, byteBuffer2);
            ByteBuffer byteBuffer6 = byteBuffer2;
            byteBuffer6.position(byteBuffer6.position() + n2);
            ByteBuffer byteBuffer7 = byteBuffer;
            byteBuffer7.position(byteBuffer7.limit());
        }
        return n2;
    }

    @Override
    protected int engineDoFinal(byte[] byArray, int n2, int n3, byte[] byArray2, int n4) throws ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        if (byArray2 != null && this.getOutputSizeForFinal(n3) > byArray2.length - n4) {
            throw new ShortBufferWithoutStackTraceException("Insufficient output space");
        }
        return super.engineDoFinal(byArray, n2, n3, byArray2, n4);
    }

    @Override
    int updateInternal(byte[] byArray, int n2, int n3, byte[] byArray2, int n4, int n5) throws ShortBufferException {
        this.checkInitialization();
        if (this.buf == null) {
            throw new IllegalStateException("Cipher not initialized");
        }
        ArrayUtils.checkOffsetAndCount(byArray.length, n2, n3);
        if (n3 > 0) {
            this.expand(n3);
            System.arraycopy(byArray, n2, this.buf, this.bufCount, n3);
            this.bufCount += n3;
        }
        return 0;
    }

    private void throwAEADBadTagExceptionIfAvailable(String string, Throwable throwable) throws BadPaddingException {
        Constructor<?> constructor;
        try {
            constructor = Class.forName("javax.crypto.AEADBadTagException").getConstructor(String.class);
        }
        catch (Exception exception) {
            return;
        }
        BadPaddingException badPaddingException = null;
        try {
            badPaddingException = (BadPaddingException)constructor.newInstance(string);
            badPaddingException.initCause(throwable);
        }
        catch (IllegalAccessException illegalAccessException) {
        }
        catch (InstantiationException instantiationException) {
        }
        catch (InvocationTargetException invocationTargetException) {
            throw (BadPaddingException)new BadPaddingException().initCause(invocationTargetException.getTargetException());
        }
        if (badPaddingException != null) {
            throw badPaddingException;
        }
    }

    int doFinalInternal(ByteBuffer byteBuffer, ByteBuffer byteBuffer2) throws ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        int n2;
        this.checkInitialization();
        try {
            n2 = this.isEncrypting() ? NativeCrypto.EVP_AEAD_CTX_seal_buf(this.evpAead, this.encodedKey, this.tagLengthInBytes, byteBuffer2, this.iv, byteBuffer, this.aad) : NativeCrypto.EVP_AEAD_CTX_open_buf(this.evpAead, this.encodedKey, this.tagLengthInBytes, byteBuffer2, this.iv, byteBuffer, this.aad);
        }
        catch (BadPaddingException badPaddingException) {
            this.throwAEADBadTagExceptionIfAvailable(badPaddingException.getMessage(), badPaddingException.getCause());
            throw badPaddingException;
        }
        if (this.isEncrypting()) {
            this.mustInitialize = true;
        }
        return n2;
    }

    @Override
    int doFinalInternal(byte[] byArray, int n2, int n3) throws ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        int n4;
        this.checkInitialization();
        try {
            n4 = this.isEncrypting() ? NativeCrypto.EVP_AEAD_CTX_seal(this.evpAead, this.encodedKey, this.tagLengthInBytes, byArray, n2, this.iv, this.buf, 0, this.bufCount, this.aad) : NativeCrypto.EVP_AEAD_CTX_open(this.evpAead, this.encodedKey, this.tagLengthInBytes, byArray, n2, this.iv, this.buf, 0, this.bufCount, this.aad);
        }
        catch (BadPaddingException badPaddingException) {
            this.throwAEADBadTagExceptionIfAvailable(badPaddingException.getMessage(), badPaddingException.getCause());
            throw badPaddingException;
        }
        if (this.isEncrypting()) {
            this.mustInitialize = true;
        }
        this.reset();
        return n4;
    }

    @Override
    void checkSupportedPadding(OpenSSLCipher.Padding padding) throws NoSuchPaddingException {
        if (padding != OpenSSLCipher.Padding.NOPADDING) {
            throw new NoSuchPaddingException("Must be NoPadding for AEAD ciphers");
        }
    }

    @Override
    int getOutputSizeForUpdate(int n2) {
        return 0;
    }

    @Override
    int getOutputSizeForFinal(int n2) {
        return this.bufCount + n2 + (this.isEncrypting() ? NativeCrypto.EVP_AEAD_max_overhead(this.evpAead) : 0);
    }

    @Override
    protected void engineUpdateAAD(byte[] byArray, int n2, int n3) {
        this.checkInitialization();
        if (this.aad == null) {
            int n4 = n2;
            this.aad = Arrays.copyOfRange(byArray, n4, n4 + n3);
            return;
        }
        int n5 = this.aad.length + n3;
        byte[] byArray2 = new byte[n5];
        System.arraycopy(this.aad, 0, byArray2, 0, this.aad.length);
        System.arraycopy(byArray, n2, byArray2, this.aad.length, n3);
        this.aad = byArray2;
    }

    @Override
    protected void engineUpdateAAD(ByteBuffer byteBuffer) {
        this.checkInitialization();
        if (this.aad == null) {
            this.aad = new byte[byteBuffer.remaining()];
            byteBuffer.get(this.aad);
            return;
        }
        int n2 = this.aad.length + byteBuffer.remaining();
        byte[] byArray = new byte[n2];
        System.arraycopy(this.aad, 0, byArray, 0, this.aad.length);
        byteBuffer.get(byArray, this.aad.length, byteBuffer.remaining());
        this.aad = byArray;
    }

    abstract long getEVP_AEAD(int var1) throws InvalidKeyException;
}


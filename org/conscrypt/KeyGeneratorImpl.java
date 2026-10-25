/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.KeyGeneratorSpi;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public abstract class KeyGeneratorImpl
extends KeyGeneratorSpi {
    private final String algorithm;
    protected SecureRandom secureRandom;
    private int keySizeBits;

    private KeyGeneratorImpl(String string, int n2) {
        this.algorithm = string;
        this.keySizeBits = n2;
    }

    protected void checkKeySize(int n2) {
        if (n2 <= 0) {
            throw new InvalidParameterException("Key size must be positive");
        }
    }

    @Override
    protected void engineInit(SecureRandom secureRandom) {
        this.secureRandom = secureRandom;
    }

    @Override
    protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
        if (algorithmParameterSpec == null) {
            throw new InvalidAlgorithmParameterException("No params provided");
        }
        throw new InvalidAlgorithmParameterException("Unknown param type: " + algorithmParameterSpec.getClass().getName());
    }

    @Override
    protected void engineInit(int n2, SecureRandom secureRandom) {
        this.checkKeySize(n2);
        this.keySizeBits = n2;
        this.secureRandom = secureRandom;
    }

    protected byte[] doKeyGeneration(int n2) {
        byte[] byArray = new byte[n2];
        this.secureRandom.nextBytes(byArray);
        return byArray;
    }

    @Override
    protected SecretKey engineGenerateKey() {
        if (this.secureRandom == null) {
            this.secureRandom = new SecureRandom();
        }
        KeyGeneratorImpl keyGeneratorImpl = this;
        return new SecretKeySpec(keyGeneratorImpl.doKeyGeneration((keyGeneratorImpl.keySizeBits + 7) / 8), this.algorithm);
    }

    public static final class ARC4
    extends KeyGeneratorImpl {
        public ARC4() {
            super("ARC4", 128);
        }

        @Override
        protected final void checkKeySize(int n2) {
            if (n2 < 40 || 2048 < n2) {
                throw new InvalidParameterException("Key size must be between 40 and 2048 bits");
            }
        }
    }

    public static final class ChaCha20
    extends KeyGeneratorImpl {
        public ChaCha20() {
            super("ChaCha20", 256);
        }

        @Override
        protected final void checkKeySize(int n2) {
            if (n2 != 256) {
                throw new InvalidParameterException("Key size must be 256 bits");
            }
        }
    }

    public static final class AES
    extends KeyGeneratorImpl {
        public AES() {
            super("AES", 128);
        }

        @Override
        protected final void checkKeySize(int n2) {
            if (n2 != 128 && n2 != 192 && n2 != 256) {
                throw new InvalidParameterException("Key size must be either 128, 192, or 256 bits");
            }
        }
    }

    public static final class DESEDE
    extends KeyGeneratorImpl {
        public DESEDE() {
            super("DESEDE", 192);
        }

        @Override
        protected final void checkKeySize(int n2) {
            if (n2 != 112 && n2 != 168) {
                throw new InvalidParameterException("Key size must be either 112 or 168 bits");
            }
        }

        @Override
        protected final byte[] doKeyGeneration(int n2) {
            byte[] byArray = new byte[24];
            this.secureRandom.nextBytes(byArray);
            for (int i2 = 0; i2 < byArray.length; ++i2) {
                if (Integer.bitCount(byArray[i2]) % 2 != 0) continue;
                byArray[i2] = (byte)(byArray[i2] ^ 1);
            }
            if (n2 == 14) {
                System.arraycopy(byArray, 0, byArray, 16, 8);
            }
            return byArray;
        }
    }

    public static final class HmacSHA512
    extends KeyGeneratorImpl {
        public HmacSHA512() {
            super("HmacSHA512", 512);
        }
    }

    public static final class HmacSHA384
    extends KeyGeneratorImpl {
        public HmacSHA384() {
            super("HmacSHA384", 384);
        }
    }

    public static final class HmacSHA256
    extends KeyGeneratorImpl {
        public HmacSHA256() {
            super("HmacSHA256", 256);
        }
    }

    public static final class HmacSHA224
    extends KeyGeneratorImpl {
        public HmacSHA224() {
            super("HmacSHA224", 224);
        }
    }

    public static final class HmacSHA1
    extends KeyGeneratorImpl {
        public HmacSHA1() {
            super("HmacSHA1", 160);
        }
    }

    public static final class HmacMD5
    extends KeyGeneratorImpl {
        public HmacMD5() {
            super("HmacMD5", 128);
        }
    }
}


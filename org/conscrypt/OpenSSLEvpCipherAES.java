/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import javax.crypto.NoSuchPaddingException;
import org.conscrypt.OpenSSLCipher;
import org.conscrypt.OpenSSLEvpCipher;

public abstract class OpenSSLEvpCipherAES
extends OpenSSLEvpCipher {
    private static final int AES_BLOCK_SIZE = 16;

    OpenSSLEvpCipherAES(OpenSSLCipher.Mode mode, OpenSSLCipher.Padding padding) {
        super(mode, padding);
    }

    @Override
    void checkSupportedMode(OpenSSLCipher.Mode mode) throws NoSuchAlgorithmException {
        switch (mode) {
            case CBC: 
            case CTR: 
            case ECB: {
                return;
            }
        }
        throw new NoSuchAlgorithmException("Unsupported mode " + mode.toString());
    }

    @Override
    void checkSupportedPadding(OpenSSLCipher.Padding padding) throws NoSuchPaddingException {
        switch (padding) {
            case NOPADDING: 
            case PKCS5PADDING: {
                return;
            }
        }
        throw new NoSuchPaddingException("Unsupported padding " + padding.toString());
    }

    @Override
    String getBaseCipherName() {
        return "AES";
    }

    @Override
    String getCipherName(int n2, OpenSSLCipher.Mode mode) {
        return "aes-" + (n2 << 3) + "-" + mode.toString().toLowerCase(Locale.US);
    }

    @Override
    int getCipherBlockSize() {
        return 16;
    }

    public static class AES_256
    extends OpenSSLEvpCipherAES {
        AES_256(OpenSSLCipher.Mode mode, OpenSSLCipher.Padding padding) {
            super(mode, padding);
        }

        @Override
        void checkSupportedKeySize(int n2) throws InvalidKeyException {
            if (n2 != 32) {
                throw new InvalidKeyException("Unsupported key size: " + n2 + " bytes");
            }
        }

        public static class ECB
        extends AES_256 {
            ECB(OpenSSLCipher.Padding padding) {
                super(OpenSSLCipher.Mode.ECB, padding);
            }

            public static class PKCS5Padding
            extends ECB {
                public PKCS5Padding() {
                    super(OpenSSLCipher.Padding.PKCS5PADDING);
                }
            }

            public static class NoPadding
            extends ECB {
                public NoPadding() {
                    super(OpenSSLCipher.Padding.NOPADDING);
                }
            }
        }

        public static class CTR
        extends AES_256 {
            public CTR() {
                super(OpenSSLCipher.Mode.CTR, OpenSSLCipher.Padding.NOPADDING);
            }
        }

        public static class CBC
        extends AES_256 {
            CBC(OpenSSLCipher.Padding padding) {
                super(OpenSSLCipher.Mode.CBC, padding);
            }

            public static class PKCS5Padding
            extends CBC {
                public PKCS5Padding() {
                    super(OpenSSLCipher.Padding.PKCS5PADDING);
                }
            }

            public static class NoPadding
            extends CBC {
                public NoPadding() {
                    super(OpenSSLCipher.Padding.NOPADDING);
                }
            }
        }
    }

    public static class AES_128
    extends OpenSSLEvpCipherAES {
        AES_128(OpenSSLCipher.Mode mode, OpenSSLCipher.Padding padding) {
            super(mode, padding);
        }

        @Override
        void checkSupportedKeySize(int n2) throws InvalidKeyException {
            if (n2 != 16) {
                throw new InvalidKeyException("Unsupported key size: " + n2 + " bytes");
            }
        }

        public static class ECB
        extends AES_128 {
            ECB(OpenSSLCipher.Padding padding) {
                super(OpenSSLCipher.Mode.ECB, padding);
            }

            public static class PKCS5Padding
            extends ECB {
                public PKCS5Padding() {
                    super(OpenSSLCipher.Padding.PKCS5PADDING);
                }
            }

            public static class NoPadding
            extends ECB {
                public NoPadding() {
                    super(OpenSSLCipher.Padding.NOPADDING);
                }
            }
        }

        public static class CTR
        extends AES_128 {
            public CTR() {
                super(OpenSSLCipher.Mode.CTR, OpenSSLCipher.Padding.NOPADDING);
            }
        }

        public static class CBC
        extends AES_128 {
            CBC(OpenSSLCipher.Padding padding) {
                super(OpenSSLCipher.Mode.CBC, padding);
            }

            public static class PKCS5Padding
            extends CBC {
                public PKCS5Padding() {
                    super(OpenSSLCipher.Padding.PKCS5PADDING);
                }
            }

            public static class NoPadding
            extends CBC {
                public NoPadding() {
                    super(OpenSSLCipher.Padding.NOPADDING);
                }
            }
        }
    }

    public static class AES
    extends OpenSSLEvpCipherAES {
        AES(OpenSSLCipher.Mode mode, OpenSSLCipher.Padding padding) {
            super(mode, padding);
        }

        @Override
        void checkSupportedKeySize(int n2) throws InvalidKeyException {
            switch (n2) {
                case 16: 
                case 24: 
                case 32: {
                    return;
                }
            }
            throw new InvalidKeyException("Unsupported key size: " + n2 + " bytes");
        }

        public static class ECB
        extends AES {
            ECB(OpenSSLCipher.Padding padding) {
                super(OpenSSLCipher.Mode.ECB, padding);
            }

            public static class PKCS5Padding
            extends ECB {
                public PKCS5Padding() {
                    super(OpenSSLCipher.Padding.PKCS5PADDING);
                }
            }

            public static class NoPadding
            extends ECB {
                public NoPadding() {
                    super(OpenSSLCipher.Padding.NOPADDING);
                }
            }
        }

        public static class CTR
        extends AES {
            public CTR() {
                super(OpenSSLCipher.Mode.CTR, OpenSSLCipher.Padding.NOPADDING);
            }
        }

        public static class CBC
        extends AES {
            CBC(OpenSSLCipher.Padding padding) {
                super(OpenSSLCipher.Mode.CBC, padding);
            }

            public static class PKCS5Padding
            extends CBC {
                public PKCS5Padding() {
                    super(OpenSSLCipher.Padding.PKCS5PADDING);
                }
            }

            public static class NoPadding
            extends CBC {
                public NoPadding() {
                    super(OpenSSLCipher.Padding.NOPADDING);
                }
            }
        }
    }
}


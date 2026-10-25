/*
 * Decompiled with CFR 0.152.
 */
package kotlin.io.encoding;

import java.nio.charset.Charset;
import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.collections.AbstractList;
import kotlin.io.encoding.Base64Kt;
import kotlin.io.encoding.ExperimentalEncodingApi;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.CharsKt;
import kotlin.text.Charsets;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0010\r\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\n\b\u0017\u0018\u0000 22\u00020\u0001:\u00012B\u0017\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0005J\u0015\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0000\u00a2\u0006\u0002\b\rJ%\u0010\u000e\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0011H\u0000\u00a2\u0006\u0002\b\u0013J \u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00112\u0006\u0010\u0017\u001a\u00020\u00112\u0006\u0010\u0018\u001a\u00020\u0011H\u0002J%\u0010\u0019\u001a\u00020\u00152\u0006\u0010\u001a\u001a\u00020\u00112\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0011H\u0000\u00a2\u0006\u0002\b\u001bJ\"\u0010\u001c\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\u0012\u001a\u00020\u0011J\"\u0010\u001c\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\u000f2\b\b\u0002\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\u0012\u001a\u00020\u0011J0\u0010\u001d\u001a\u00020\u00112\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u001e\u001a\u00020\f2\u0006\u0010\u0017\u001a\u00020\u00112\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0011H\u0002J4\u0010\u001f\u001a\u00020\u00112\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u001e\u001a\u00020\f2\b\b\u0002\u0010\u0017\u001a\u00020\u00112\b\b\u0002\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\u0012\u001a\u00020\u0011J4\u0010\u001f\u001a\u00020\u00112\u0006\u0010\u000b\u001a\u00020\u000f2\u0006\u0010\u001e\u001a\u00020\f2\b\b\u0002\u0010\u0017\u001a\u00020\u00112\b\b\u0002\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\u0012\u001a\u00020\u0011J \u0010 \u001a\u00020\u00112\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0011H\u0002J\"\u0010!\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\u0012\u001a\u00020\u0011J4\u0010\"\u001a\u00020\u00112\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u001e\u001a\u00020\f2\b\b\u0002\u0010\u0017\u001a\u00020\u00112\b\b\u0002\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\u0012\u001a\u00020\u0011J5\u0010#\u001a\u00020\u00112\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u001e\u001a\u00020\f2\u0006\u0010\u0017\u001a\u00020\u00112\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0011H\u0000\u00a2\u0006\u0002\b$J\u0010\u0010%\u001a\u00020\u00112\u0006\u0010\u001a\u001a\u00020\u0011H\u0002J=\u0010&\u001a\u0002H'\"\f\b\u0000\u0010'*\u00060(j\u0002`)2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u001e\u001a\u0002H'2\b\b\u0002\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\u0012\u001a\u00020\u0011\u00a2\u0006\u0002\u0010*J\"\u0010+\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\u0012\u001a\u00020\u0011J%\u0010,\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0011H\u0000\u00a2\u0006\u0002\b-J(\u0010.\u001a\u00020\u00112\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010/\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00112\u0006\u00100\u001a\u00020\u0011H\u0002J \u00101\u001a\u00020\u00112\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0011H\u0002R\u0014\u0010\u0004\u001a\u00020\u0003X\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0014\u0010\u0002\u001a\u00020\u0003X\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0007\u00a8\u00063"}, d2={"Lkotlin/io/encoding/Base64;", "", "isUrlSafe", "", "isMimeScheme", "(ZZ)V", "isMimeScheme$kotlin_stdlib", "()Z", "isUrlSafe$kotlin_stdlib", "bytesToStringImpl", "", "source", "", "bytesToStringImpl$kotlin_stdlib", "charsToBytesImpl", "", "startIndex", "", "endIndex", "charsToBytesImpl$kotlin_stdlib", "checkDestinationBounds", "", "destinationSize", "destinationOffset", "capacityNeeded", "checkSourceBounds", "sourceSize", "checkSourceBounds$kotlin_stdlib", "decode", "decodeImpl", "destination", "decodeIntoByteArray", "decodeSize", "encode", "encodeIntoByteArray", "encodeIntoByteArrayImpl", "encodeIntoByteArrayImpl$kotlin_stdlib", "encodeSize", "encodeToAppendable", "A", "Ljava/lang/Appendable;", "Lkotlin/text/Appendable;", "([BLjava/lang/Appendable;II)Ljava/lang/Appendable;", "encodeToByteArray", "encodeToByteArrayImpl", "encodeToByteArrayImpl$kotlin_stdlib", "handlePaddingSymbol", "padIndex", "byteStart", "skipIllegalSymbolsIfMime", "Default", "kotlin-stdlib"})
@SinceKotlin(version="1.8")
@ExperimentalEncodingApi
public class Base64 {
    @NotNull
    public static final Default Default = new Default(null);
    private final boolean isUrlSafe;
    private final boolean isMimeScheme;
    private static final int bitsPerByte = 8;
    private static final int bitsPerSymbol = 6;
    public static final int bytesPerGroup = 3;
    public static final int symbolsPerGroup = 4;
    public static final byte padSymbol = 61;
    public static final int mimeLineLength = 76;
    private static final int mimeGroupsPerLine = 19;
    @NotNull
    private static final byte[] mimeLineSeparatorSymbols;
    @NotNull
    private static final Base64 UrlSafe;
    @NotNull
    private static final Base64 Mime;

    private Base64(boolean bl, boolean bl2) {
        this.isUrlSafe = bl;
        this.isMimeScheme = bl2;
        bl = !this.isUrlSafe || !this.isMimeScheme;
        if (!bl) {
            String string = "Failed requirement.";
            throw new IllegalArgumentException(string.toString());
        }
    }

    public final boolean isUrlSafe$kotlin_stdlib() {
        return this.isUrlSafe;
    }

    public final boolean isMimeScheme$kotlin_stdlib() {
        return this.isMimeScheme;
    }

    @NotNull
    public final byte[] encodeToByteArray(@NotNull byte[] byArray, int n2, int n3) {
        Intrinsics.checkNotNullParameter(byArray, "");
        return this.encodeToByteArrayImpl$kotlin_stdlib(byArray, n2, n3);
    }

    public static /* synthetic */ byte[] encodeToByteArray$default(Base64 base64, byte[] byArray, int n2, int n3, int n4, Object object) {
        if (object != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: encodeToByteArray");
        }
        if ((n4 & 2) != 0) {
            n2 = 0;
        }
        if ((n4 & 4) != 0) {
            n3 = byArray.length;
        }
        return base64.encodeToByteArray(byArray, n2, n3);
    }

    public final int encodeIntoByteArray(@NotNull byte[] byArray, @NotNull byte[] byArray2, int n2, int n3, int n4) {
        Intrinsics.checkNotNullParameter(byArray, "");
        Intrinsics.checkNotNullParameter(byArray2, "");
        return this.encodeIntoByteArrayImpl$kotlin_stdlib(byArray, byArray2, n2, n3, n4);
    }

    public static /* synthetic */ int encodeIntoByteArray$default(Base64 base64, byte[] byArray, byte[] byArray2, int n2, int n3, int n4, int n5, Object object) {
        if (object != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: encodeIntoByteArray");
        }
        if ((n5 & 4) != 0) {
            n2 = 0;
        }
        if ((n5 & 8) != 0) {
            n3 = 0;
        }
        if ((n5 & 0x10) != 0) {
            n4 = byArray.length;
        }
        return base64.encodeIntoByteArray(byArray, byArray2, n2, n3, n4);
    }

    @NotNull
    public final String encode(@NotNull byte[] byArray, int n2, int n3) {
        Intrinsics.checkNotNullParameter(byArray, "");
        byArray = this.encodeToByteArrayImpl$kotlin_stdlib(byArray, n2, n3);
        return new String(byArray, Charsets.ISO_8859_1);
    }

    public static /* synthetic */ String encode$default(Base64 base64, byte[] byArray, int n2, int n3, int n4, Object object) {
        if (object != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: encode");
        }
        if ((n4 & 2) != 0) {
            n2 = 0;
        }
        if ((n4 & 4) != 0) {
            n3 = byArray.length;
        }
        return base64.encode(byArray, n2, n3);
    }

    @NotNull
    public final <A extends Appendable> A encodeToAppendable(@NotNull byte[] object, @NotNull A a2, int n2, int n3) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(a2, "");
        object = this.encodeToByteArrayImpl$kotlin_stdlib((byte[])object, n2, n3);
        object = new String((byte[])object, Charsets.ISO_8859_1);
        a2.append((CharSequence)object);
        return a2;
    }

    public static /* synthetic */ Appendable encodeToAppendable$default(Base64 base64, byte[] byArray, Appendable appendable, int n2, int n3, int n4, Object object) {
        if (object != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: encodeToAppendable");
        }
        if ((n4 & 4) != 0) {
            n2 = 0;
        }
        if ((n4 & 8) != 0) {
            n3 = byArray.length;
        }
        return base64.encodeToAppendable(byArray, appendable, n2, n3);
    }

    @NotNull
    public final byte[] decode(@NotNull byte[] byArray, int n2, int n3) {
        Intrinsics.checkNotNullParameter(byArray, "");
        this.checkSourceBounds$kotlin_stdlib(byArray.length, n2, n3);
        int n4 = this.decodeSize(byArray, n2, n3);
        byte[] byArray2 = new byte[n4];
        int n5 = this.decodeImpl(byArray, byArray2, 0, n2, n3);
        if ((n5 = n5 == byArray2.length ? 1 : 0) == 0) {
            String string = "Check failed.";
            throw new IllegalStateException(string.toString());
        }
        return byArray2;
    }

    public static /* synthetic */ byte[] decode$default(Base64 base64, byte[] byArray, int n2, int n3, int n4, Object object) {
        if (object != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: decode");
        }
        if ((n4 & 2) != 0) {
            n2 = 0;
        }
        if ((n4 & 4) != 0) {
            n3 = byArray.length;
        }
        return base64.decode(byArray, n2, n3);
    }

    public final int decodeIntoByteArray(@NotNull byte[] byArray, @NotNull byte[] byArray2, int n2, int n3, int n4) {
        Intrinsics.checkNotNullParameter(byArray, "");
        Intrinsics.checkNotNullParameter(byArray2, "");
        this.checkSourceBounds$kotlin_stdlib(byArray.length, n3, n4);
        this.checkDestinationBounds(byArray2.length, n2, this.decodeSize(byArray, n3, n4));
        return this.decodeImpl(byArray, byArray2, n2, n3, n4);
    }

    public static /* synthetic */ int decodeIntoByteArray$default(Base64 base64, byte[] byArray, byte[] byArray2, int n2, int n3, int n4, int n5, Object object) {
        if (object != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: decodeIntoByteArray");
        }
        if ((n5 & 4) != 0) {
            n2 = 0;
        }
        if ((n5 & 8) != 0) {
            n3 = 0;
        }
        if ((n5 & 0x10) != 0) {
            n4 = byArray.length;
        }
        return base64.decodeIntoByteArray(byArray, byArray2, n2, n3, n4);
    }

    @NotNull
    public final byte[] decode(@NotNull CharSequence object, int n2, int n3) {
        byte[] byArray;
        Intrinsics.checkNotNullParameter(object, "");
        Base64 base64 = this;
        if (object instanceof String) {
            base64.checkSourceBounds$kotlin_stdlib(object.length(), n2, n3);
            String string = ((String)object).substring(n2, n3);
            Intrinsics.checkNotNullExpressionValue(string, "");
            object = string;
            Charset charset = Charsets.ISO_8859_1;
            Intrinsics.checkNotNull(object);
            byte[] byArray2 = ((String)object).getBytes(charset);
            byArray = byArray2;
            Intrinsics.checkNotNullExpressionValue(byArray2, "");
        } else {
            byArray = base64.charsToBytesImpl$kotlin_stdlib((CharSequence)object, n2, n3);
        }
        object = byArray;
        return Base64.decode$default(this, (byte[])object, 0, 0, 6, null);
    }

    public static /* synthetic */ byte[] decode$default(Base64 base64, CharSequence charSequence, int n2, int n3, int n4, Object object) {
        if (object != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: decode");
        }
        if ((n4 & 2) != 0) {
            n2 = 0;
        }
        if ((n4 & 4) != 0) {
            n3 = charSequence.length();
        }
        return base64.decode(charSequence, n2, n3);
    }

    public final int decodeIntoByteArray(@NotNull CharSequence object, @NotNull byte[] byArray, int n2, int n3, int n4) {
        byte[] byArray2;
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(byArray, "");
        Base64 base64 = this;
        if (object instanceof String) {
            base64.checkSourceBounds$kotlin_stdlib(object.length(), n3, n4);
            String string = ((String)object).substring(n3, n4);
            Intrinsics.checkNotNullExpressionValue(string, "");
            object = string;
            Charset charset = Charsets.ISO_8859_1;
            Intrinsics.checkNotNull(object);
            byte[] byArray3 = ((String)object).getBytes(charset);
            byArray2 = byArray3;
            Intrinsics.checkNotNullExpressionValue(byArray3, "");
        } else {
            byArray2 = base64.charsToBytesImpl$kotlin_stdlib((CharSequence)object, n3, n4);
        }
        object = byArray2;
        return Base64.decodeIntoByteArray$default(this, (byte[])object, byArray, n2, 0, 0, 24, null);
    }

    public static /* synthetic */ int decodeIntoByteArray$default(Base64 base64, CharSequence charSequence, byte[] byArray, int n2, int n3, int n4, int n5, Object object) {
        if (object != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: decodeIntoByteArray");
        }
        if ((n5 & 4) != 0) {
            n2 = 0;
        }
        if ((n5 & 8) != 0) {
            n3 = 0;
        }
        if ((n5 & 0x10) != 0) {
            n4 = charSequence.length();
        }
        return base64.decodeIntoByteArray(charSequence, byArray, n2, n3, n4);
    }

    @NotNull
    public final byte[] encodeToByteArrayImpl$kotlin_stdlib(@NotNull byte[] byArray, int n2, int n3) {
        Intrinsics.checkNotNullParameter(byArray, "");
        this.checkSourceBounds$kotlin_stdlib(byArray.length, n2, n3);
        int n4 = this.encodeSize(n3 - n2);
        byte[] byArray2 = new byte[n4];
        this.encodeIntoByteArrayImpl$kotlin_stdlib(byArray, byArray2, 0, n2, n3);
        return byArray2;
    }

    public final int encodeIntoByteArrayImpl$kotlin_stdlib(@NotNull byte[] byArray, @NotNull byte[] byArray2, int n2, int n3, int n4) {
        int n5;
        int n6;
        int n7;
        int n8;
        int n9;
        Intrinsics.checkNotNullParameter(byArray, "");
        Intrinsics.checkNotNullParameter(byArray2, "");
        this.checkSourceBounds$kotlin_stdlib(byArray.length, n3, n4);
        this.checkDestinationBounds(byArray2.length, n2, this.encodeSize(n4 - n3));
        byte[] byArray3 = this.isUrlSafe ? Base64Kt.access$getBase64UrlEncodeMap$p() : Base64Kt.access$getBase64EncodeMap$p();
        int n10 = n2;
        int n11 = n9 = this.isMimeScheme ? 19 : Integer.MAX_VALUE;
        while (n3 + 2 < n4) {
            n8 = Math.min((n4 - n3) / 3, n9);
            for (n7 = 0; n7 < n8; ++n7) {
                n6 = byArray[n3++] & 0xFF;
                n5 = byArray[n3++] & 0xFF;
                int n12 = byArray[n3++] & 0xFF;
                n6 = n6 << 16 | n5 << 8 | n12;
                byArray2[n10++] = byArray3[n6 >>> 18];
                byArray2[n10++] = byArray3[n6 >>> 12 & 0x3F];
                byArray2[n10++] = byArray3[n6 >>> 6 & 0x3F];
                byArray2[n10++] = byArray3[n6 & 0x3F];
            }
            if (n8 != n9 || n3 == n4) continue;
            byArray2[n10++] = mimeLineSeparatorSymbols[0];
            byArray2[n10++] = mimeLineSeparatorSymbols[1];
        }
        switch (n4 - n3) {
            case 1: {
                n7 = byArray[n3++] & 0xFF;
                n6 = n7 << 4;
                byArray2[n10++] = byArray3[n6 >>> 6];
                byArray2[n10++] = byArray3[n6 & 0x3F];
                byArray2[n10++] = 61;
                byArray2[n10++] = 61;
                break;
            }
            case 2: {
                n7 = byArray[n3++] & 0xFF;
                n6 = byArray[n3++] & 0xFF;
                n5 = n7 << 10 | n6 << 2;
                byArray2[n10++] = byArray3[n5 >>> 12];
                byArray2[n10++] = byArray3[n5 >>> 6 & 0x3F];
                byArray2[n10++] = byArray3[n5 & 0x3F];
                byArray2[n10++] = 61;
            }
        }
        if ((n8 = n3 == n4 ? 1 : 0) == 0) {
            String string = "Check failed.";
            throw new IllegalStateException(string.toString());
        }
        return n10 - n2;
    }

    private final int encodeSize(int n2) {
        n2 = (n2 + 3 - 1) / 3;
        int n3 = this.isMimeScheme ? (n2 - 1) / 19 : 0;
        if ((n2 = (n2 << 2) + (n3 << 1)) < 0) {
            throw new IllegalArgumentException("Input is too big");
        }
        return n2;
    }

    private final int decodeImpl(byte[] byArray, byte[] byArray2, int n2, int n3, int n4) {
        int n5;
        int[] nArray = this.isUrlSafe ? Base64Kt.access$getBase64UrlDecodeMap$p() : Base64Kt.access$getBase64DecodeMap$p();
        int n6 = 0;
        int n7 = -8;
        int n8 = n2;
        while (n3 < n4) {
            int n9;
            if (n7 == -8 && n3 + 3 < n4) {
                int n10;
                int n11;
                n5 = nArray[byArray[n3++] & 0xFF];
                n9 = nArray[byArray[n3++] & 0xFF];
                if ((n5 = n5 << 18 | n9 << 12 | (n11 = nArray[byArray[n3++] & 0xFF]) << 6 | (n10 = nArray[byArray[n3++] & 0xFF])) >= 0) {
                    byArray2[n8++] = (byte)(n5 >> 16);
                    byArray2[n8++] = (byte)(n5 >> 8);
                    byArray2[n8++] = (byte)n5;
                    continue;
                }
                n3 -= 4;
            }
            if ((n9 = nArray[n5 = byArray[n3] & 0xFF]) < 0) {
                if (n9 == -2) {
                    n3 = this.handlePaddingSymbol(byArray, n3, n4, n7);
                    break;
                }
                if (this.isMimeScheme) {
                    ++n3;
                    continue;
                }
                StringBuilder stringBuilder = new StringBuilder("Invalid symbol '").append((char)n5).append("'(");
                String string = Integer.toString(n5, CharsKt.checkRadix(8));
                Intrinsics.checkNotNullExpressionValue(string, "");
                throw new IllegalArgumentException(stringBuilder.append(string).append(") at index ").append(n3).toString());
            }
            ++n3;
            n6 = n6 << 6 | n9;
            if ((n7 += 6) < 0) continue;
            byArray2[n8++] = (byte)(n6 >>> n7);
            n6 &= (1 << n7) - 1;
            n7 -= 8;
        }
        if (n7 == -2) {
            throw new IllegalArgumentException("The last unit of input does not have enough bits");
        }
        if ((n3 = this.skipIllegalSymbolsIfMime(byArray, n3, n4)) < n4) {
            n5 = byArray[n3] & 0xFF;
            StringBuilder stringBuilder = new StringBuilder("Symbol '").append((char)n5).append("'(");
            String string = Integer.toString(n5, CharsKt.checkRadix(8));
            Intrinsics.checkNotNullExpressionValue(string, "");
            throw new IllegalArgumentException(stringBuilder.append(string).append(") at index ").append(n3 - 1).append(" is prohibited after the pad character").toString());
        }
        return n8 - n2;
    }

    private final int decodeSize(byte[] byArray, int n2, int n3) {
        int n4 = n3 - n2;
        if (n4 == 0) {
            return 0;
        }
        if (n4 == 1) {
            throw new IllegalArgumentException("Input should have at list 2 symbols for Base64 decoding, startIndex: " + n2 + ", endIndex: " + n3);
        }
        if (this.isMimeScheme) {
            while (n2 < n3) {
                int n5 = byArray[n2] & 0xFF;
                n5 = Base64Kt.access$getBase64DecodeMap$p()[n5];
                if (n5 < 0) {
                    if (n5 == -2) {
                        n4 -= n3 - n2;
                        break;
                    }
                    --n4;
                }
                ++n2;
            }
        } else if (byArray[n3 - 1] == 61) {
            --n4;
            if (byArray[n3 - 2] == 61) {
                --n4;
            }
        }
        return (int)((long)n4 * 6L / 8L);
    }

    @NotNull
    public final byte[] charsToBytesImpl$kotlin_stdlib(@NotNull CharSequence charSequence, int n2, int n3) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        this.checkSourceBounds$kotlin_stdlib(charSequence.length(), n2, n3);
        byte[] byArray = new byte[n3 - n2];
        int n4 = 0;
        while (n2 < n3) {
            char c2 = charSequence.charAt(n2);
            byArray[n4++] = c2 <= '\u00ff' ? (int)c2 : 63;
            ++n2;
        }
        return byArray;
    }

    @NotNull
    public final String bytesToStringImpl$kotlin_stdlib(@NotNull byte[] byArray) {
        Intrinsics.checkNotNullParameter(byArray, "");
        StringBuilder stringBuilder = new StringBuilder(byArray.length);
        for (byte by : byArray) {
            stringBuilder.append((char)by);
        }
        String string = stringBuilder.toString();
        Intrinsics.checkNotNullExpressionValue(string, "");
        return string;
    }

    private final int handlePaddingSymbol(byte[] byArray, int n2, int n3, int n4) {
        switch (n4) {
            case -8: {
                throw new IllegalArgumentException("Redundant pad character at index " + n2);
            }
            case -2: {
                return n2 + 1;
            }
            case -4: {
                n2 = this.skipIllegalSymbolsIfMime(byArray, n2 + 1, n3);
                if (n2 == n3 || byArray[n2] != 61) {
                    throw new IllegalArgumentException("Missing one pad character at index " + n2);
                }
                return n2 + 1;
            }
            case -6: {
                return n2 + 1;
            }
        }
        throw new IllegalStateException("Unreachable".toString());
    }

    private final int skipIllegalSymbolsIfMime(byte[] byArray, int n2, int n3) {
        if (!this.isMimeScheme) {
            return n2;
        }
        while (n2 < n3) {
            int n4 = byArray[n2] & 0xFF;
            if (Base64Kt.access$getBase64DecodeMap$p()[n4] != -1) {
                return n2;
            }
            ++n2;
        }
        return n2;
    }

    public final void checkSourceBounds$kotlin_stdlib(int n2, int n3, int n4) {
        AbstractList.Companion.checkBoundsIndexes$kotlin_stdlib(n3, n4, n2);
    }

    private final void checkDestinationBounds(int n2, int n3, int n4) {
        if (n3 < 0 || n3 > n2) {
            throw new IndexOutOfBoundsException("destination offset: " + n3 + ", destination size: " + n2);
        }
        int n5 = n3 + n4;
        if (n5 < 0 || n5 > n2) {
            throw new IndexOutOfBoundsException("The destination array does not have enough capacity, destination offset: " + n3 + ", destination size: " + n2 + ", capacity needed: " + n4);
        }
    }

    public /* synthetic */ Base64(boolean bl, boolean bl2, DefaultConstructorMarker defaultConstructorMarker) {
        this(bl, bl2);
    }

    static {
        byte[] byArray = new byte[2];
        byte[] byArray2 = byArray;
        byArray[0] = 13;
        byArray2[1] = 10;
        mimeLineSeparatorSymbols = byArray2;
        UrlSafe = new Base64(true, false);
        Mime = new Base64(false, true);
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0012\n\u0002\b\u0003\n\u0002\u0010\u0005\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0001\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\u0005R\u0011\u0010\u0006\u001a\u00020\u0001\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\u0005R\u000e\u0010\b\u001a\u00020\tX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\tX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\tX\u0080T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\tX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\tX\u0080T\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\u00020\u000fX\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u000e\u0010\u0012\u001a\u00020\u0013X\u0080T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\tX\u0080T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2={"Lkotlin/io/encoding/Base64$Default;", "Lkotlin/io/encoding/Base64;", "()V", "Mime", "getMime", "()Lkotlin/io/encoding/Base64;", "UrlSafe", "getUrlSafe", "bitsPerByte", "", "bitsPerSymbol", "bytesPerGroup", "mimeGroupsPerLine", "mimeLineLength", "mimeLineSeparatorSymbols", "", "getMimeLineSeparatorSymbols$kotlin_stdlib", "()[B", "padSymbol", "", "symbolsPerGroup", "kotlin-stdlib"})
    public static final class Default
    extends Base64 {
        private Default() {
            super(false, false, null);
        }

        @NotNull
        public final byte[] getMimeLineSeparatorSymbols$kotlin_stdlib() {
            return mimeLineSeparatorSymbols;
        }

        @NotNull
        public final Base64 getUrlSafe() {
            return UrlSafe;
        }

        @NotNull
        public final Base64 getMime() {
            return Mime;
        }

        public /* synthetic */ Default(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }
}


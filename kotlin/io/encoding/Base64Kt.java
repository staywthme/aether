/*
 * Decompiled with CFR 0.152.
 */
package kotlin.io.encoding;

import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.collections.ArraysKt;
import kotlin.io.encoding.ExperimentalEncodingApi;
import kotlin.jvm.internal.SourceDebugExtension;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 8, 0}, k=2, xi=48, d1={"\u0000\u001e\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\u001a\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0001\"\u0016\u0010\u0000\u001a\u00020\u00018\u0002X\u0083\u0004\u00a2\u0006\b\n\u0000\u0012\u0004\b\u0002\u0010\u0003\"\u0016\u0010\u0004\u001a\u00020\u00058\u0002X\u0083\u0004\u00a2\u0006\b\n\u0000\u0012\u0004\b\u0006\u0010\u0003\"\u0016\u0010\u0007\u001a\u00020\u00018\u0002X\u0083\u0004\u00a2\u0006\b\n\u0000\u0012\u0004\b\b\u0010\u0003\"\u0016\u0010\t\u001a\u00020\u00058\u0002X\u0083\u0004\u00a2\u0006\b\n\u0000\u0012\u0004\b\n\u0010\u0003\u00a8\u0006\u000f"}, d2={"base64DecodeMap", "", "getBase64DecodeMap$annotations", "()V", "base64EncodeMap", "", "getBase64EncodeMap$annotations", "base64UrlDecodeMap", "getBase64UrlDecodeMap$annotations", "base64UrlEncodeMap", "getBase64UrlEncodeMap$annotations", "isInMimeAlphabet", "", "symbol", "", "kotlin-stdlib"})
@SourceDebugExtension(value={"SMAP\nBase64.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Base64.kt\nkotlin/io/encoding/Base64Kt\n+ 2 _Arrays.kt\nkotlin/collections/ArraysKt___ArraysKt\n*L\n1#1,647:1\n13654#2,3:648\n13654#2,3:651\n*S KotlinDebug\n*F\n+ 1 Base64.kt\nkotlin/io/encoding/Base64Kt\n*L\n582#1:648,3\n601#1:651,3\n*E\n"})
public final class Base64Kt {
    @NotNull
    private static final byte[] base64EncodeMap;
    @NotNull
    private static final int[] base64DecodeMap;
    @NotNull
    private static final byte[] base64UrlEncodeMap;
    @NotNull
    private static final int[] base64UrlDecodeMap;

    private static /* synthetic */ void getBase64EncodeMap$annotations() {
    }

    @ExperimentalEncodingApi
    private static /* synthetic */ void getBase64DecodeMap$annotations() {
    }

    private static /* synthetic */ void getBase64UrlEncodeMap$annotations() {
    }

    @ExperimentalEncodingApi
    private static /* synthetic */ void getBase64UrlDecodeMap$annotations() {
    }

    @SinceKotlin(version="1.8")
    @ExperimentalEncodingApi
    public static final boolean isInMimeAlphabet(int n2) {
        return (n2 >= 0 ? n2 < base64DecodeMap.length : false) && base64DecodeMap[n2] != -1;
    }

    public static final /* synthetic */ byte[] access$getBase64UrlEncodeMap$p() {
        return base64UrlEncodeMap;
    }

    public static final /* synthetic */ byte[] access$getBase64EncodeMap$p() {
        return base64EncodeMap;
    }

    public static final /* synthetic */ int[] access$getBase64UrlDecodeMap$p() {
        return base64UrlDecodeMap;
    }

    public static final /* synthetic */ int[] access$getBase64DecodeMap$p() {
        return base64DecodeMap;
    }

    static {
        int n2;
        byte[] byArray = new byte[64];
        Object[] objectArray = byArray;
        byArray[0] = 65;
        objectArray[1] = 66;
        objectArray[2] = 67;
        objectArray[3] = 68;
        objectArray[4] = 69;
        objectArray[5] = 70;
        objectArray[6] = 71;
        objectArray[7] = 72;
        objectArray[8] = 73;
        objectArray[9] = 74;
        objectArray[10] = 75;
        objectArray[11] = 76;
        objectArray[12] = 77;
        objectArray[13] = 78;
        objectArray[14] = 79;
        objectArray[15] = 80;
        objectArray[16] = 81;
        objectArray[17] = 82;
        objectArray[18] = 83;
        objectArray[19] = 84;
        objectArray[20] = 85;
        objectArray[21] = 86;
        objectArray[22] = 87;
        objectArray[23] = 88;
        objectArray[24] = 89;
        objectArray[25] = 90;
        objectArray[26] = 97;
        objectArray[27] = 98;
        objectArray[28] = 99;
        objectArray[29] = 100;
        objectArray[30] = 101;
        objectArray[31] = 102;
        objectArray[32] = 103;
        objectArray[33] = 104;
        objectArray[34] = 105;
        objectArray[35] = 106;
        objectArray[36] = 107;
        objectArray[37] = 108;
        objectArray[38] = 109;
        objectArray[39] = 110;
        objectArray[40] = 111;
        objectArray[41] = 112;
        objectArray[42] = 113;
        objectArray[43] = 114;
        objectArray[44] = 115;
        objectArray[45] = 116;
        objectArray[46] = 117;
        objectArray[47] = 118;
        objectArray[48] = 119;
        objectArray[49] = 120;
        objectArray[50] = 121;
        objectArray[51] = 122;
        objectArray[52] = 48;
        objectArray[53] = 49;
        objectArray[54] = 50;
        objectArray[55] = 51;
        objectArray[56] = 52;
        objectArray[57] = 53;
        objectArray[58] = 54;
        objectArray[59] = 55;
        objectArray[60] = 56;
        objectArray[61] = 57;
        objectArray[62] = 43;
        objectArray[63] = 47;
        base64EncodeMap = objectArray;
        int[] nArray = new int[256];
        objectArray = nArray;
        int[] nArray2 = nArray;
        ArraysKt.fill$default(nArray, -1, 0, 0, 6, null);
        nArray2[61] = -2;
        byte[] byArray2 = base64EncodeMap;
        int n3 = 0;
        for (byte by : byArray2) {
            nArray2[by] = n2 = n3++;
        }
        base64DecodeMap = objectArray;
        byte[] byArray3 = new byte[64];
        objectArray = byArray3;
        byArray3[0] = 65;
        objectArray[1] = 66;
        objectArray[2] = 67;
        objectArray[3] = 68;
        objectArray[4] = 69;
        objectArray[5] = 70;
        objectArray[6] = 71;
        objectArray[7] = 72;
        objectArray[8] = 73;
        objectArray[9] = 74;
        objectArray[10] = 75;
        objectArray[11] = 76;
        objectArray[12] = 77;
        objectArray[13] = 78;
        objectArray[14] = 79;
        objectArray[15] = 80;
        objectArray[16] = 81;
        objectArray[17] = 82;
        objectArray[18] = 83;
        objectArray[19] = 84;
        objectArray[20] = 85;
        objectArray[21] = 86;
        objectArray[22] = 87;
        objectArray[23] = 88;
        objectArray[24] = 89;
        objectArray[25] = 90;
        objectArray[26] = 97;
        objectArray[27] = 98;
        objectArray[28] = 99;
        objectArray[29] = 100;
        objectArray[30] = 101;
        objectArray[31] = 102;
        objectArray[32] = 103;
        objectArray[33] = 104;
        objectArray[34] = 105;
        objectArray[35] = 106;
        objectArray[36] = 107;
        objectArray[37] = 108;
        objectArray[38] = 109;
        objectArray[39] = 110;
        objectArray[40] = 111;
        objectArray[41] = 112;
        objectArray[42] = 113;
        objectArray[43] = 114;
        objectArray[44] = 115;
        objectArray[45] = 116;
        objectArray[46] = 117;
        objectArray[47] = 118;
        objectArray[48] = 119;
        objectArray[49] = 120;
        objectArray[50] = 121;
        objectArray[51] = 122;
        objectArray[52] = 48;
        objectArray[53] = 49;
        objectArray[54] = 50;
        objectArray[55] = 51;
        objectArray[56] = 52;
        objectArray[57] = 53;
        objectArray[58] = 54;
        objectArray[59] = 55;
        objectArray[60] = 56;
        objectArray[61] = 57;
        objectArray[62] = 45;
        objectArray[63] = 95;
        base64UrlEncodeMap = objectArray;
        int[] nArray3 = new int[256];
        objectArray = nArray3;
        nArray2 = nArray3;
        ArraysKt.fill$default(nArray3, -1, 0, 0, 6, null);
        nArray2[61] = -2;
        byArray2 = base64UrlEncodeMap;
        n3 = 0;
        for (byte by : byArray2) {
            nArray2[by] = n2 = n3++;
        }
        base64UrlDecodeMap = objectArray;
    }
}


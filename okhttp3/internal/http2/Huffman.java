/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.http2;

import java.io.IOException;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0015\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u00c6\u0002\u0018\u00002\u00020\u0001:\u0001\u001aB\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J \u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\fH\u0002J\u001e\u0010\u000f\u001a\u00020\n2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015J\u0016\u0010\u0016\u001a\u00020\n2\u0006\u0010\u0010\u001a\u00020\u00172\u0006\u0010\u0014\u001a\u00020\u0015J\u000e\u0010\u0018\u001a\u00020\f2\u0006\u0010\u0019\u001a\u00020\u0017R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001b"}, d2={"Lokhttp3/internal/http2/Huffman;", "", "()V", "CODES", "", "CODE_BIT_COUNTS", "", "root", "Lokhttp3/internal/http2/Huffman$Node;", "addCode", "", "symbol", "", "code", "codeBitCount", "decode", "source", "Lokio/BufferedSource;", "byteCount", "", "sink", "Lokio/BufferedSink;", "encode", "Lokio/ByteString;", "encodedLength", "bytes", "Node", "okhttp"})
public final class Huffman {
    @NotNull
    public static final Huffman INSTANCE = new Huffman();
    @NotNull
    private static final int[] CODES;
    @NotNull
    private static final byte[] CODE_BIT_COUNTS;
    @NotNull
    private static final Node root;

    private Huffman() {
    }

    public final void encode(@NotNull ByteString byteString, @NotNull BufferedSink bufferedSink) throws IOException {
        Intrinsics.checkNotNullParameter(byteString, "");
        Intrinsics.checkNotNullParameter(bufferedSink, "");
        long l2 = 0L;
        int n2 = 0;
        int n3 = byteString.size();
        for (int i2 = 0; i2 < n3; ++i2) {
            int n4 = Util.and(byteString.getByte(i2), 255);
            int n5 = CODES[n4];
            n4 = CODE_BIT_COUNTS[n4];
            l2 = l2 << n4 | (long)n5;
            n2 += n4;
            while (n2 >= 8) {
                bufferedSink.writeByte((int)(l2 >> (n2 -= 8)));
            }
        }
        if (n2 > 0) {
            l2 <<= 8 - n2;
            bufferedSink.writeByte((int)(l2 |= 255L >>> n2));
        }
    }

    public final int encodedLength(@NotNull ByteString byteString) {
        Intrinsics.checkNotNullParameter(byteString, "");
        long l2 = 0L;
        int n2 = byteString.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            int n3 = Util.and(byteString.getByte(i2), 255);
            l2 += (long)CODE_BIT_COUNTS[n3];
        }
        return (int)(l2 + 7L >> 3);
    }

    public final void decode(@NotNull BufferedSource bufferedSource, long l2, @NotNull BufferedSink bufferedSink) {
        Intrinsics.checkNotNullParameter(bufferedSource, "");
        Intrinsics.checkNotNullParameter(bufferedSink, "");
        Node node = root;
        int n2 = 0;
        int n3 = 0;
        for (long i2 = 0L; i2 < l2; ++i2) {
            int n4 = Util.and(bufferedSource.readByte(), 255);
            n2 = n2 << 8 | n4;
            n3 += 8;
            while (n3 >= 8) {
                n4 = n2 >>> n3 - 8 & 0xFF;
                Node[] nodeArray = node.getChildren();
                Intrinsics.checkNotNull(nodeArray);
                Intrinsics.checkNotNull(nodeArray[n4]);
                if (node.getChildren() == null) {
                    bufferedSink.writeByte(node.getSymbol());
                    n3 -= node.getTerminalBitCount();
                    node = root;
                    continue;
                }
                n3 -= 8;
            }
        }
        while (n3 > 0) {
            int n5 = n2 << 8 - n3 & 0xFF;
            Node[] nodeArray = node.getChildren();
            Intrinsics.checkNotNull(nodeArray);
            Node node2 = nodeArray[n5];
            Intrinsics.checkNotNull(node2);
            node = node2;
            if (node2.getChildren() != null || node.getTerminalBitCount() > n3) {
                return;
            }
            bufferedSink.writeByte(node.getSymbol());
            n3 -= node.getTerminalBitCount();
            node = root;
        }
    }

    private final void addCode(int n2, int n3, int n4) {
        int n5;
        Node node = new Node(n2, n4);
        Node node2 = root;
        while (n4 > 8) {
            Node[] nodeArray;
            n5 = n3 >>> (n4 -= 8) & 0xFF;
            Intrinsics.checkNotNull(node2.getChildren());
            Node node3 = nodeArray[n5];
            if (node3 == null) {
                nodeArray[n5] = node3 = new Node();
            }
            node2 = node3;
        }
        n5 = 8 - n4;
        int n6 = n3 << n5 & 0xFF;
        int n7 = 1 << n5;
        Node[] nodeArray = node2.getChildren();
        Intrinsics.checkNotNull(nodeArray);
        int n8 = n6;
        ArraysKt.fill(nodeArray, node, n8, n8 + n7);
    }

    static {
        int[] nArray = new int[256];
        Object[] objectArray = nArray;
        nArray[0] = 8184;
        objectArray[1] = 8388568;
        objectArray[2] = 0xFFFFFE2;
        objectArray[3] = 0xFFFFFE3;
        objectArray[4] = 0xFFFFFE4;
        objectArray[5] = 0xFFFFFE5;
        objectArray[6] = 0xFFFFFE6;
        objectArray[7] = 0xFFFFFE7;
        objectArray[8] = 0xFFFFFE8;
        objectArray[9] = 0xFFFFEA;
        objectArray[10] = 0x3FFFFFFC;
        objectArray[11] = 0xFFFFFE9;
        objectArray[12] = 0xFFFFFEA;
        objectArray[13] = 0x3FFFFFFD;
        objectArray[14] = 0xFFFFFEB;
        objectArray[15] = 0xFFFFFEC;
        objectArray[16] = 0xFFFFFED;
        objectArray[17] = 0xFFFFFEE;
        objectArray[18] = 0xFFFFFEF;
        objectArray[19] = 0xFFFFFF0;
        objectArray[20] = 0xFFFFFF1;
        objectArray[21] = 0xFFFFFF2;
        objectArray[22] = 0x3FFFFFFE;
        objectArray[23] = 0xFFFFFF3;
        objectArray[24] = 0xFFFFFF4;
        objectArray[25] = 0xFFFFFF5;
        objectArray[26] = 0xFFFFFF6;
        objectArray[27] = 0xFFFFFF7;
        objectArray[28] = 0xFFFFFF8;
        objectArray[29] = 0xFFFFFF9;
        objectArray[30] = 0xFFFFFFA;
        objectArray[31] = 0xFFFFFFB;
        objectArray[32] = 20;
        objectArray[33] = 1016;
        objectArray[34] = 1017;
        objectArray[35] = 4090;
        objectArray[36] = 8185;
        objectArray[37] = 21;
        objectArray[38] = 248;
        objectArray[39] = 2042;
        objectArray[40] = 1018;
        objectArray[41] = 1019;
        objectArray[42] = 249;
        objectArray[43] = 2043;
        objectArray[44] = 250;
        objectArray[45] = 22;
        objectArray[46] = 23;
        objectArray[47] = 24;
        objectArray[48] = 0;
        objectArray[49] = 1;
        objectArray[50] = 2;
        objectArray[51] = 25;
        objectArray[52] = 26;
        objectArray[53] = 27;
        objectArray[54] = 28;
        objectArray[55] = 29;
        objectArray[56] = 30;
        objectArray[57] = 31;
        objectArray[58] = 92;
        objectArray[59] = 251;
        objectArray[60] = 32764;
        objectArray[61] = 32;
        objectArray[62] = 4091;
        objectArray[63] = 1020;
        objectArray[64] = 8186;
        objectArray[65] = 33;
        objectArray[66] = 93;
        objectArray[67] = 94;
        objectArray[68] = 95;
        objectArray[69] = 96;
        objectArray[70] = 97;
        objectArray[71] = 98;
        objectArray[72] = 99;
        objectArray[73] = 100;
        objectArray[74] = 101;
        objectArray[75] = 102;
        objectArray[76] = 103;
        objectArray[77] = 104;
        objectArray[78] = 105;
        objectArray[79] = 106;
        objectArray[80] = 107;
        objectArray[81] = 108;
        objectArray[82] = 109;
        objectArray[83] = 110;
        objectArray[84] = 111;
        objectArray[85] = 112;
        objectArray[86] = 113;
        objectArray[87] = 114;
        objectArray[88] = 252;
        objectArray[89] = 115;
        objectArray[90] = 253;
        objectArray[91] = 8187;
        objectArray[92] = 524272;
        objectArray[93] = 8188;
        objectArray[94] = 16380;
        objectArray[95] = 34;
        objectArray[96] = 32765;
        objectArray[97] = 3;
        objectArray[98] = 35;
        objectArray[99] = 4;
        objectArray[100] = 36;
        objectArray[101] = 5;
        objectArray[102] = 37;
        objectArray[103] = 38;
        objectArray[104] = 39;
        objectArray[105] = 6;
        objectArray[106] = 116;
        objectArray[107] = 117;
        objectArray[108] = 40;
        objectArray[109] = 41;
        objectArray[110] = 42;
        objectArray[111] = 7;
        objectArray[112] = 43;
        objectArray[113] = 118;
        objectArray[114] = 44;
        objectArray[115] = 8;
        objectArray[116] = 9;
        objectArray[117] = 45;
        objectArray[118] = 119;
        objectArray[119] = 120;
        objectArray[120] = 121;
        objectArray[121] = 122;
        objectArray[122] = 123;
        objectArray[123] = 32766;
        objectArray[124] = 2044;
        objectArray[125] = 16381;
        objectArray[126] = 8189;
        objectArray[127] = 0xFFFFFFC;
        objectArray[128] = 1048550;
        objectArray[129] = 4194258;
        objectArray[130] = 1048551;
        objectArray[131] = 1048552;
        objectArray[132] = 0x3FFFD3;
        objectArray[133] = 4194260;
        objectArray[134] = 4194261;
        objectArray[135] = 8388569;
        objectArray[136] = 4194262;
        objectArray[137] = 8388570;
        objectArray[138] = 8388571;
        objectArray[139] = 8388572;
        objectArray[140] = 0x7FFFDD;
        objectArray[141] = 8388574;
        objectArray[142] = 0xFFFFEB;
        objectArray[143] = 0x7FFFDF;
        objectArray[144] = 0xFFFFEC;
        objectArray[145] = 0xFFFFED;
        objectArray[146] = 4194263;
        objectArray[147] = 8388576;
        objectArray[148] = 0xFFFFEE;
        objectArray[149] = 8388577;
        objectArray[150] = 8388578;
        objectArray[151] = 8388579;
        objectArray[152] = 8388580;
        objectArray[153] = 2097116;
        objectArray[154] = 4194264;
        objectArray[155] = 8388581;
        objectArray[156] = 4194265;
        objectArray[157] = 8388582;
        objectArray[158] = 0x7FFFE7;
        objectArray[159] = 0xFFFFEF;
        objectArray[160] = 4194266;
        objectArray[161] = 0x1FFFDD;
        objectArray[162] = 1048553;
        objectArray[163] = 4194267;
        objectArray[164] = 4194268;
        objectArray[165] = 8388584;
        objectArray[166] = 8388585;
        objectArray[167] = 2097118;
        objectArray[168] = 8388586;
        objectArray[169] = 0x3FFFDD;
        objectArray[170] = 4194270;
        objectArray[171] = 0xFFFFF0;
        objectArray[172] = 0x1FFFDF;
        objectArray[173] = 0x3FFFDF;
        objectArray[174] = 8388587;
        objectArray[175] = 8388588;
        objectArray[176] = 2097120;
        objectArray[177] = 0x1FFFE1;
        objectArray[178] = 4194272;
        objectArray[179] = 2097122;
        objectArray[180] = 8388589;
        objectArray[181] = 4194273;
        objectArray[182] = 0x7FFFEE;
        objectArray[183] = 0x7FFFEF;
        objectArray[184] = 1048554;
        objectArray[185] = 4194274;
        objectArray[186] = 0x3FFFE3;
        objectArray[187] = 4194276;
        objectArray[188] = 0x7FFFF0;
        objectArray[189] = 4194277;
        objectArray[190] = 4194278;
        objectArray[191] = 0x7FFFF1;
        objectArray[192] = 67108832;
        objectArray[193] = 67108833;
        objectArray[194] = 1048555;
        objectArray[195] = 524273;
        objectArray[196] = 4194279;
        objectArray[197] = 0x7FFFF2;
        objectArray[198] = 4194280;
        objectArray[199] = 33554412;
        objectArray[200] = 67108834;
        objectArray[201] = 0x3FFFFE3;
        objectArray[202] = 67108836;
        objectArray[203] = 134217694;
        objectArray[204] = 0x7FFFFDF;
        objectArray[205] = 67108837;
        objectArray[206] = 0xFFFFF1;
        objectArray[207] = 33554413;
        objectArray[208] = 524274;
        objectArray[209] = 2097123;
        objectArray[210] = 67108838;
        objectArray[211] = 134217696;
        objectArray[212] = 134217697;
        objectArray[213] = 67108839;
        objectArray[214] = 134217698;
        objectArray[215] = 0xFFFFF2;
        objectArray[216] = 2097124;
        objectArray[217] = 2097125;
        objectArray[218] = 67108840;
        objectArray[219] = 67108841;
        objectArray[220] = 0xFFFFFFD;
        objectArray[221] = 134217699;
        objectArray[222] = 134217700;
        objectArray[223] = 134217701;
        objectArray[224] = 1048556;
        objectArray[225] = 0xFFFFF3;
        objectArray[226] = 1048557;
        objectArray[227] = 2097126;
        objectArray[228] = 4194281;
        objectArray[229] = 2097127;
        objectArray[230] = 2097128;
        objectArray[231] = 0x7FFFF3;
        objectArray[232] = 4194282;
        objectArray[233] = 4194283;
        objectArray[234] = 0x1FFFFEE;
        objectArray[235] = 0x1FFFFEF;
        objectArray[236] = 0xFFFFF4;
        objectArray[237] = 0xFFFFF5;
        objectArray[238] = 67108842;
        objectArray[239] = 0x7FFFF4;
        objectArray[240] = 67108843;
        objectArray[241] = 134217702;
        objectArray[242] = 67108844;
        objectArray[243] = 67108845;
        objectArray[244] = 0x7FFFFE7;
        objectArray[245] = 134217704;
        objectArray[246] = 134217705;
        objectArray[247] = 134217706;
        objectArray[248] = 134217707;
        objectArray[249] = 0xFFFFFFE;
        objectArray[250] = 134217708;
        objectArray[251] = 134217709;
        objectArray[252] = 0x7FFFFEE;
        objectArray[253] = 0x7FFFFEF;
        objectArray[254] = 0x7FFFFF0;
        objectArray[255] = 0x3FFFFEE;
        CODES = objectArray;
        byte[] byArray = new byte[256];
        objectArray = byArray;
        byArray[0] = 13;
        objectArray[1] = 23;
        objectArray[2] = 28;
        objectArray[3] = 28;
        objectArray[4] = 28;
        objectArray[5] = 28;
        objectArray[6] = 28;
        objectArray[7] = 28;
        objectArray[8] = 28;
        objectArray[9] = 24;
        objectArray[10] = 30;
        objectArray[11] = 28;
        objectArray[12] = 28;
        objectArray[13] = 30;
        objectArray[14] = 28;
        objectArray[15] = 28;
        objectArray[16] = 28;
        objectArray[17] = 28;
        objectArray[18] = 28;
        objectArray[19] = 28;
        objectArray[20] = 28;
        objectArray[21] = 28;
        objectArray[22] = 30;
        objectArray[23] = 28;
        objectArray[24] = 28;
        objectArray[25] = 28;
        objectArray[26] = 28;
        objectArray[27] = 28;
        objectArray[28] = 28;
        objectArray[29] = 28;
        objectArray[30] = 28;
        objectArray[31] = 28;
        objectArray[32] = 6;
        objectArray[33] = 10;
        objectArray[34] = 10;
        objectArray[35] = 12;
        objectArray[36] = 13;
        objectArray[37] = 6;
        objectArray[38] = 8;
        objectArray[39] = 11;
        objectArray[40] = 10;
        objectArray[41] = 10;
        objectArray[42] = 8;
        objectArray[43] = 11;
        objectArray[44] = 8;
        objectArray[45] = 6;
        objectArray[46] = 6;
        objectArray[47] = 6;
        objectArray[48] = 5;
        objectArray[49] = 5;
        objectArray[50] = 5;
        objectArray[51] = 6;
        objectArray[52] = 6;
        objectArray[53] = 6;
        objectArray[54] = 6;
        objectArray[55] = 6;
        objectArray[56] = 6;
        objectArray[57] = 6;
        objectArray[58] = 7;
        objectArray[59] = 8;
        objectArray[60] = 15;
        objectArray[61] = 6;
        objectArray[62] = 12;
        objectArray[63] = 10;
        objectArray[64] = 13;
        objectArray[65] = 6;
        objectArray[66] = 7;
        objectArray[67] = 7;
        objectArray[68] = 7;
        objectArray[69] = 7;
        objectArray[70] = 7;
        objectArray[71] = 7;
        objectArray[72] = 7;
        objectArray[73] = 7;
        objectArray[74] = 7;
        objectArray[75] = 7;
        objectArray[76] = 7;
        objectArray[77] = 7;
        objectArray[78] = 7;
        objectArray[79] = 7;
        objectArray[80] = 7;
        objectArray[81] = 7;
        objectArray[82] = 7;
        objectArray[83] = 7;
        objectArray[84] = 7;
        objectArray[85] = 7;
        objectArray[86] = 7;
        objectArray[87] = 7;
        objectArray[88] = 8;
        objectArray[89] = 7;
        objectArray[90] = 8;
        objectArray[91] = 13;
        objectArray[92] = 19;
        objectArray[93] = 13;
        objectArray[94] = 14;
        objectArray[95] = 6;
        objectArray[96] = 15;
        objectArray[97] = 5;
        objectArray[98] = 6;
        objectArray[99] = 5;
        objectArray[100] = 6;
        objectArray[101] = 5;
        objectArray[102] = 6;
        objectArray[103] = 6;
        objectArray[104] = 6;
        objectArray[105] = 5;
        objectArray[106] = 7;
        objectArray[107] = 7;
        objectArray[108] = 6;
        objectArray[109] = 6;
        objectArray[110] = 6;
        objectArray[111] = 5;
        objectArray[112] = 6;
        objectArray[113] = 7;
        objectArray[114] = 6;
        objectArray[115] = 5;
        objectArray[116] = 5;
        objectArray[117] = 6;
        objectArray[118] = 7;
        objectArray[119] = 7;
        objectArray[120] = 7;
        objectArray[121] = 7;
        objectArray[122] = 7;
        objectArray[123] = 15;
        objectArray[124] = 11;
        objectArray[125] = 14;
        objectArray[126] = 13;
        objectArray[127] = 28;
        objectArray[128] = 20;
        objectArray[129] = 22;
        objectArray[130] = 20;
        objectArray[131] = 20;
        objectArray[132] = 22;
        objectArray[133] = 22;
        objectArray[134] = 22;
        objectArray[135] = 23;
        objectArray[136] = 22;
        objectArray[137] = 23;
        objectArray[138] = 23;
        objectArray[139] = 23;
        objectArray[140] = 23;
        objectArray[141] = 23;
        objectArray[142] = 24;
        objectArray[143] = 23;
        objectArray[144] = 24;
        objectArray[145] = 24;
        objectArray[146] = 22;
        objectArray[147] = 23;
        objectArray[148] = 24;
        objectArray[149] = 23;
        objectArray[150] = 23;
        objectArray[151] = 23;
        objectArray[152] = 23;
        objectArray[153] = 21;
        objectArray[154] = 22;
        objectArray[155] = 23;
        objectArray[156] = 22;
        objectArray[157] = 23;
        objectArray[158] = 23;
        objectArray[159] = 24;
        objectArray[160] = 22;
        objectArray[161] = 21;
        objectArray[162] = 20;
        objectArray[163] = 22;
        objectArray[164] = 22;
        objectArray[165] = 23;
        objectArray[166] = 23;
        objectArray[167] = 21;
        objectArray[168] = 23;
        objectArray[169] = 22;
        objectArray[170] = 22;
        objectArray[171] = 24;
        objectArray[172] = 21;
        objectArray[173] = 22;
        objectArray[174] = 23;
        objectArray[175] = 23;
        objectArray[176] = 21;
        objectArray[177] = 21;
        objectArray[178] = 22;
        objectArray[179] = 21;
        objectArray[180] = 23;
        objectArray[181] = 22;
        objectArray[182] = 23;
        objectArray[183] = 23;
        objectArray[184] = 20;
        objectArray[185] = 22;
        objectArray[186] = 22;
        objectArray[187] = 22;
        objectArray[188] = 23;
        objectArray[189] = 22;
        objectArray[190] = 22;
        objectArray[191] = 23;
        objectArray[192] = 26;
        objectArray[193] = 26;
        objectArray[194] = 20;
        objectArray[195] = 19;
        objectArray[196] = 22;
        objectArray[197] = 23;
        objectArray[198] = 22;
        objectArray[199] = 25;
        objectArray[200] = 26;
        objectArray[201] = 26;
        objectArray[202] = 26;
        objectArray[203] = 27;
        objectArray[204] = 27;
        objectArray[205] = 26;
        objectArray[206] = 24;
        objectArray[207] = 25;
        objectArray[208] = 19;
        objectArray[209] = 21;
        objectArray[210] = 26;
        objectArray[211] = 27;
        objectArray[212] = 27;
        objectArray[213] = 26;
        objectArray[214] = 27;
        objectArray[215] = 24;
        objectArray[216] = 21;
        objectArray[217] = 21;
        objectArray[218] = 26;
        objectArray[219] = 26;
        objectArray[220] = 28;
        objectArray[221] = 27;
        objectArray[222] = 27;
        objectArray[223] = 27;
        objectArray[224] = 20;
        objectArray[225] = 24;
        objectArray[226] = 20;
        objectArray[227] = 21;
        objectArray[228] = 22;
        objectArray[229] = 21;
        objectArray[230] = 21;
        objectArray[231] = 23;
        objectArray[232] = 22;
        objectArray[233] = 22;
        objectArray[234] = 25;
        objectArray[235] = 25;
        objectArray[236] = 24;
        objectArray[237] = 24;
        objectArray[238] = 26;
        objectArray[239] = 23;
        objectArray[240] = 26;
        objectArray[241] = 27;
        objectArray[242] = 26;
        objectArray[243] = 26;
        objectArray[244] = 27;
        objectArray[245] = 27;
        objectArray[246] = 27;
        objectArray[247] = 27;
        objectArray[248] = 27;
        objectArray[249] = 28;
        objectArray[250] = 27;
        objectArray[251] = 27;
        objectArray[252] = 27;
        objectArray[253] = 27;
        objectArray[254] = 27;
        objectArray[255] = 26;
        CODE_BIT_COUNTS = (byte[])objectArray;
        root = new Node();
        int n2 = CODE_BIT_COUNTS.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            INSTANCE.addCode(i2, CODES[i2], CODE_BIT_COUNTS[i2]);
        }
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\b\b\b\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0016\u00a2\u0006\u0002\u0010\u0002B\u0017\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\u0006R\u001d\u0010\u0007\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0000\u0018\u00010\b\u00a2\u0006\n\n\u0002\u0010\u000b\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u000e\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\r\u00a8\u0006\u0010"}, d2={"Lokhttp3/internal/http2/Huffman$Node;", "", "()V", "symbol", "", "bits", "(II)V", "children", "", "getChildren", "()[Lokhttp3/internal/http2/Huffman$Node;", "[Lokhttp3/internal/http2/Huffman$Node;", "getSymbol", "()I", "terminalBitCount", "getTerminalBitCount", "okhttp"})
    static final class Node {
        @Nullable
        private final Node[] children;
        private final int symbol;
        private final int terminalBitCount;

        @Nullable
        public final Node[] getChildren() {
            return this.children;
        }

        public final int getSymbol() {
            return this.symbol;
        }

        public final int getTerminalBitCount() {
            return this.terminalBitCount;
        }

        public Node() {
            this.children = new Node[256];
            this.symbol = 0;
            this.terminalBitCount = 0;
        }

        public Node(int n2, int n3) {
            this.children = null;
            this.symbol = n2;
            n2 = n3 & 7;
            this.terminalBitCount = n2 == 0 ? 8 : n2;
        }
    }
}


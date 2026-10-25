/*
 * Decompiled with CFR 0.152.
 */
package okio.internal;

import java.util.Arrays;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 9, 0}, k=2, xi=48, d1={"\u0000\u0016\n\u0000\n\u0002\u0010\u0012\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\u001a\n\u0010\u0000\u001a\u00020\u0001*\u00020\u0002\u001a\u001e\u0010\u0003\u001a\u00020\u0002*\u00020\u00012\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0005\u00a8\u0006\u0007"}, d2={"commonAsUtf8ToByteArray", "", "", "commonToUtf8String", "beginIndex", "", "endIndex", "okio"})
@SourceDebugExtension(value={"SMAP\n-Utf8.kt\nKotlin\n*S Kotlin\n*F\n+ 1 -Utf8.kt\nokio/internal/_Utf8Kt\n+ 2 Utf8.kt\nokio/Utf8\n+ 3 Util.kt\nokio/-SegmentedByteString\n*L\n1#1,60:1\n260#2,16:61\n277#2:78\n397#2,9:79\n127#2:88\n406#2,20:90\n279#2,3:110\n440#2,4:113\n127#2:117\n446#2,10:118\n127#2:128\n456#2,5:129\n127#2:134\n461#2,24:135\n283#2,3:159\n500#2,3:162\n286#2,12:165\n503#2:177\n127#2:178\n506#2,2:179\n127#2:181\n510#2,10:182\n127#2:192\n520#2,5:193\n127#2:198\n525#2,5:199\n127#2:204\n530#2,28:205\n302#2,6:233\n138#2,67:239\n68#3:77\n74#3:89\n*S KotlinDebug\n*F\n+ 1 -Utf8.kt\nokio/internal/_Utf8Kt\n*L\n34#1:61,16\n34#1:78\n34#1:79,9\n34#1:88\n34#1:90,20\n34#1:110,3\n34#1:113,4\n34#1:117\n34#1:118,10\n34#1:128\n34#1:129,5\n34#1:134\n34#1:135,24\n34#1:159,3\n34#1:162,3\n34#1:165,12\n34#1:177\n34#1:178\n34#1:179,2\n34#1:181\n34#1:182,10\n34#1:192\n34#1:193,5\n34#1:198\n34#1:199,5\n34#1:204\n34#1:205,28\n34#1:233,6\n50#1:239,67\n34#1:77\n34#1:89\n*E\n"})
public final class _Utf8Kt {
    /*
     * Unable to fully structure code
     */
    @NotNull
    public static final String commonToUtf8String(@NotNull byte[] var0, int var1_1, int var2_2) {
        Intrinsics.checkNotNullParameter(var0, "");
        if (var1_1 < 0 || var2_2 > var0.length || var1_1 > var2_2) {
            throw new ArrayIndexOutOfBoundsException("size=" + var0.length + " beginIndex=" + var1_1 + " endIndex=" + var2_2);
        }
        var3_3 = new char[var2_2 - var1_1];
        var4_4 = 0;
        while (var1_1 < var2_2) {
            block40: {
                block44: {
                    block41: {
                        block43: {
                            block42: {
                                block37: {
                                    block39: {
                                        block38: {
                                            var5_5 = var0[var1_1];
                                            if (var5_5 >= '\u0000') {
                                                var5_5 = var5_5;
                                                var6_6 = var4_4;
                                                var4_4 = var6_6 + 1;
                                                var3_3[var6_6] = var5_5;
                                                ++var1_1;
                                                while (var1_1 < var2_2 && var0[var1_1] >= 0) {
                                                    var5_5 = (char)var0[var1_1++];
                                                    var6_6 = var4_4;
                                                    var4_4 = var6_6 + 1;
                                                    var3_3[var6_6] = var5_5;
                                                }
                                                continue;
                                            }
                                            var7_7 = var5_5;
                                            if (var7_7 >> 5 == -2) {
                                                v0 = var1_1;
                                                var7_8 = var0;
                                                if (var2_2 <= var1_1 + 1) {
                                                    var8_11 = v0;
                                                    var6_6 = var4_4;
                                                    var4_4 = var6_6 + 1;
                                                    var3_3[var6_6] = 65533;
                                                    v1 = var8_11;
                                                    v2 = 1;
                                                } else {
                                                    var5_5 = var7_8[var1_1];
                                                    var9_12 = var6_6 = var7_8[var1_1 + 1];
                                                    if (!((var6_6 & 192) == 128)) {
                                                        var8_11 = v0;
                                                        var6_6 = var4_4;
                                                        var4_4 = var6_6 + 1;
                                                        var3_3[var6_6] = 65533;
                                                        v1 = var8_11;
                                                        v2 = 1;
                                                    } else {
                                                        var8_11 = 3968 ^ var6_6 ^ var5_5 << 6;
                                                        if (var8_11 < 128) {
                                                            var8_11 = v0;
                                                            var6_6 = var4_4;
                                                            var4_4 = var6_6 + 1;
                                                            var3_3[var6_6] = 65533;
                                                            v1 = var8_11;
                                                        } else {
                                                            var1_1 = var8_11;
                                                            var8_11 = v0;
                                                            var5_5 = (char)var1_1;
                                                            var6_6 = var4_4;
                                                            var4_4 = var6_6 + 1;
                                                            var3_3[var6_6] = var5_5;
                                                            v1 = var8_11;
                                                        }
                                                        v2 = 2;
                                                    }
                                                }
                                                var1_1 = v1 + v2;
                                                continue;
                                            }
                                            var7_7 = var5_5;
                                            if (var7_7 >> 4 != -2) break block37;
                                            v3 = var1_1;
                                            var7_9 = var0;
                                            if (var2_2 > var1_1 + 2) break block38;
                                            var8_11 = v3;
                                            var6_6 = var4_4;
                                            var4_4 = var6_6 + 1;
                                            var3_3[var6_6] = 65533;
                                            v4 = var8_11;
                                            if (var2_2 <= var1_1 + 1) ** GOTO lbl-1000
                                            var5_5 = var7_9[var1_1 + 1];
                                            var8_11 = var5_5;
                                            if (!((var5_5 & 192) == 128)) lbl-1000:
                                            // 2 sources

                                            {
                                                v5 = 1;
                                            } else {
                                                v5 = 2;
                                            }
                                            break block39;
                                        }
                                        var5_5 = var7_9[var1_1];
                                        var9_12 = var6_6 = var7_9[var1_1 + 1];
                                        if (!((var6_6 & 192) == 128)) {
                                            var8_11 = v3;
                                            var6_6 = var4_4;
                                            var4_4 = var6_6 + 1;
                                            var3_3[var6_6] = 65533;
                                            v4 = var8_11;
                                            v5 = 1;
                                        } else {
                                            var9_12 = var8_11 = var7_9[var1_1 + 2];
                                            if (!((var8_11 & 192) == 128)) {
                                                var8_11 = v3;
                                                var6_6 = var4_4;
                                                var4_4 = var6_6 + 1;
                                                var3_3[var6_6] = 65533;
                                                v4 = var8_11;
                                                v5 = 2;
                                            } else {
                                                var9_12 = -123008 ^ var8_11 ^ var6_6 << 6 ^ var5_5 << 12;
                                                if (var9_12 < 2048) {
                                                    var8_11 = v3;
                                                    var6_6 = var4_4;
                                                    var4_4 = var6_6 + 1;
                                                    var3_3[var6_6] = 65533;
                                                    v4 = var8_11;
                                                } else {
                                                    v6 = 55296 <= var9_12 ? var9_12 < 57344 : false;
                                                    if (v6) {
                                                        var8_11 = v3;
                                                        var6_6 = var4_4;
                                                        var4_4 = var6_6 + 1;
                                                        var3_3[var6_6] = 65533;
                                                        v4 = var8_11;
                                                    } else {
                                                        var1_1 = var9_12;
                                                        var8_11 = v3;
                                                        var5_5 = (char)var1_1;
                                                        var6_6 = var4_4;
                                                        var4_4 = var6_6 + 1;
                                                        var3_3[var6_6] = var5_5;
                                                        v4 = var8_11;
                                                    }
                                                }
                                                v5 = 3;
                                            }
                                        }
                                    }
                                    var1_1 = v4 + v5;
                                    continue;
                                }
                                var7_7 = var5_5;
                                if (var7_7 >> 3 != -2) break block40;
                                v7 = var1_1;
                                var7_10 = var0;
                                if (var2_2 > var1_1 + 3) break block41;
                                var8_11 = v7;
                                var6_6 = var4_4;
                                var4_4 = var6_6 + 1;
                                var3_3[var6_6] = 65533;
                                v8 = var8_11;
                                if (var2_2 <= var1_1 + 1) break block42;
                                var5_5 = var7_10[var1_1 + 1];
                                var8_11 = var5_5;
                                if ((var5_5 & 192) == 128) break block43;
                            }
                            v9 = 1;
                            break block44;
                        }
                        if (var2_2 <= var1_1 + 2) ** GOTO lbl-1000
                        var5_5 = var7_10[var1_1 + 2];
                        var8_11 = var5_5;
                        if (!((var5_5 & 192) == 128)) lbl-1000:
                        // 2 sources

                        {
                            v9 = 2;
                        } else {
                            v9 = 3;
                        }
                        break block44;
                    }
                    var5_5 = var7_10[var1_1];
                    var9_12 = var6_6 = var7_10[var1_1 + 1];
                    if (!((var6_6 & 192) == 128)) {
                        var8_11 = v7;
                        var6_6 = var4_4;
                        var4_4 = var6_6 + 1;
                        var3_3[var6_6] = 65533;
                        v8 = var8_11;
                        v9 = 1;
                    } else {
                        var9_12 = var8_11 = var7_10[var1_1 + 2];
                        if (!((var8_11 & 192) == 128)) {
                            var8_11 = v7;
                            var6_6 = var4_4;
                            var4_4 = var6_6 + 1;
                            var3_3[var6_6] = 65533;
                            v8 = var8_11;
                            v9 = 2;
                        } else {
                            var1_1 = var9_12 = var7_10[var1_1 + 3];
                            if (!((var9_12 & 192) == 128)) {
                                var8_11 = v7;
                                var6_6 = var4_4;
                                var4_4 = var6_6 + 1;
                                var3_3[var6_6] = 65533;
                                v8 = var8_11;
                                v9 = 3;
                            } else {
                                if ((var9_12 = 3678080 ^ var9_12 ^ var8_11 << 6 ^ var6_6 << 12 ^ var5_5 << 18) > 0x10FFFF) {
                                    var8_11 = v7;
                                    var6_6 = var4_4;
                                    var4_4 = var6_6 + 1;
                                    var3_3[var6_6] = 65533;
                                    v8 = var8_11;
                                } else {
                                    v10 = 55296 <= var9_12 ? var9_12 < 57344 : false;
                                    if (v10) {
                                        var8_11 = v7;
                                        var6_6 = var4_4;
                                        var4_4 = var6_6 + 1;
                                        var3_3[var6_6] = 65533;
                                        v8 = var8_11;
                                    } else if (var9_12 < 65536) {
                                        var8_11 = v7;
                                        var6_6 = var4_4;
                                        var4_4 = var6_6 + 1;
                                        var3_3[var6_6] = 65533;
                                        v8 = var8_11;
                                    } else {
                                        var1_1 = var9_12;
                                        var8_11 = v7;
                                        if (var1_1 != 65533) {
                                            var5_5 = (char)((var1_1 >>> 10) + 55232);
                                            var6_6 = var4_4;
                                            var4_4 = var6_6 + 1;
                                            var3_3[var6_6] = var5_5;
                                            var5_5 = (char)((var1_1 & 1023) + 56320);
                                            var6_6 = var4_4;
                                            var4_4 = var6_6 + 1;
                                            var3_3[var6_6] = var5_5;
                                        } else {
                                            var6_6 = var4_4;
                                            var4_4 = var6_6 + 1;
                                            var3_3[var6_6] = 65533;
                                        }
                                        v8 = var8_11;
                                    }
                                }
                                v9 = 4;
                            }
                        }
                    }
                }
                var1_1 = v8 + v9;
                continue;
            }
            var6_6 = var4_4;
            var4_4 = var6_6 + 1;
            var3_3[var6_6] = 65533;
            ++var1_1;
        }
        return StringsKt.concatToString(var3_3, 0, var4_4);
    }

    public static /* synthetic */ String commonToUtf8String$default(byte[] byArray, int n2, int n3, int n4, Object object) {
        if ((n4 & 1) != 0) {
            n2 = 0;
        }
        if ((n4 & 2) != 0) {
            n3 = byArray.length;
        }
        return _Utf8Kt.commonToUtf8String(byArray, n2, n3);
    }

    @NotNull
    public static final byte[] commonAsUtf8ToByteArray(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, "");
        byte[] byArray = new byte[4 * string.length()];
        int n2 = string.length();
        for (int i2 = 0; i2 < n2; ++i2) {
            char c2;
            block10: {
                c2 = string.charAt(i2);
                if (Intrinsics.compare(c2, 128) < 0) break block10;
                n2 = i2;
                String string2 = string;
                int n3 = string.length();
                while (i2 < n3) {
                    int n4;
                    int n5;
                    char c3;
                    block12: {
                        block11: {
                            c3 = string2.charAt(i2);
                            if (Intrinsics.compare(c3, 128) < 0) {
                                n5 = c3;
                                n4 = n2;
                                n2 = n4 + 1;
                                byArray[n4] = n5;
                                ++i2;
                                while (i2 < n3 && Intrinsics.compare(string2.charAt(i2), 128) < 0) {
                                    n5 = (byte)string2.charAt(i2++);
                                    n4 = n2;
                                    n2 = n4 + 1;
                                    byArray[n4] = n5;
                                }
                                continue;
                            }
                            if (Intrinsics.compare(c3, 2048) < 0) {
                                n5 = (byte)(c3 >> 6 | 0xC0);
                                n4 = n2;
                                n2 = n4 + 1;
                                byArray[n4] = n5;
                                n5 = (byte)(c3 & 0x3F | 0x80);
                                n4 = n2;
                                n2 = n4 + 1;
                                byArray[n4] = n5;
                                ++i2;
                                continue;
                            }
                            if (!('\ud800' <= c3 ? c3 < '\ue000' : false)) {
                                n5 = (byte)(c3 >> 12 | 0xE0);
                                n4 = n2;
                                n2 = n4 + 1;
                                byArray[n4] = n5;
                                n5 = (byte)(c3 >> 6 & 0x3F | 0x80);
                                n4 = n2;
                                n2 = n4 + 1;
                                byArray[n4] = n5;
                                n5 = (byte)(c3 & 0x3F | 0x80);
                                n4 = n2;
                                n2 = n4 + 1;
                                byArray[n4] = n5;
                                ++i2;
                                continue;
                            }
                            if (Intrinsics.compare(c3, 56319) > 0 || n3 <= i2 + 1) break block11;
                            n5 = string2.charAt(i2 + 1);
                            if (56320 <= n5 ? n5 < 57344 : false) break block12;
                        }
                        n4 = n2;
                        n2 = n4 + 1;
                        byArray[n4] = 63;
                        ++i2;
                        continue;
                    }
                    n5 = (c3 << 10) + string2.charAt(i2 + 1) + -56613888;
                    c3 = (char)(n5 >> 18 | 0xF0);
                    n4 = n2;
                    n2 = n4 + 1;
                    byArray[n4] = c3;
                    c3 = (char)(n5 >> 12 & 0x3F | 0x80);
                    n4 = n2;
                    n2 = n4 + 1;
                    byArray[n4] = c3;
                    c3 = (char)(n5 >> 6 & 0x3F | 0x80);
                    n4 = n2;
                    n2 = n4 + 1;
                    byArray[n4] = c3;
                    c3 = (char)(n5 & 0x3F | 0x80);
                    n4 = n2;
                    n2 = n4 + 1;
                    byArray[n4] = c3;
                    i2 += 2;
                }
                byte[] byArray2 = Arrays.copyOf(byArray, n2);
                Intrinsics.checkNotNullExpressionValue(byArray2, "");
                return byArray2;
            }
            byArray[i2] = (byte)c2;
        }
        byte[] byArray3 = Arrays.copyOf(byArray, string.length());
        Intrinsics.checkNotNullExpressionValue(byArray3, "");
        return byArray3;
    }
}


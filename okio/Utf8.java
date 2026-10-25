/*
 * Decompiled with CFR 0.152.
 */
package okio;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.JvmName;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 9, 0}, k=2, xi=48, d1={"\u0000D\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0005\n\u0000\n\u0002\u0010\f\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u0012\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\u001a\u0011\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u0001H\u0080\b\u001a\u0011\u0010\u000e\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u0007H\u0080\b\u001a4\u0010\u0010\u001a\u00020\u0001*\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00012\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00160\u0015H\u0080\b\u00f8\u0001\u0000\u001a4\u0010\u0017\u001a\u00020\u0001*\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00012\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00160\u0015H\u0080\b\u00f8\u0001\u0000\u001a4\u0010\u0018\u001a\u00020\u0001*\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00012\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00160\u0015H\u0080\b\u00f8\u0001\u0000\u001a4\u0010\u0019\u001a\u00020\u0016*\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00012\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00160\u0015H\u0080\b\u00f8\u0001\u0000\u001a4\u0010\u001a\u001a\u00020\u0016*\u00020\u001b2\u0006\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00012\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00160\u0015H\u0080\b\u00f8\u0001\u0000\u001a4\u0010\u001c\u001a\u00020\u0016*\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00012\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00160\u0015H\u0080\b\u00f8\u0001\u0000\u001a%\u0010\u001d\u001a\u00020\u001e*\u00020\u001b2\b\b\u0002\u0010\u0012\u001a\u00020\u00012\b\b\u0002\u0010\u0013\u001a\u00020\u0001H\u0007\u00a2\u0006\u0002\b\u001f\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0080T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001X\u0080T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0003\u001a\u00020\u0001X\u0080T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0004\u001a\u00020\u0001X\u0080T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0005\u001a\u00020\u0001X\u0080T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0006\u001a\u00020\u0007X\u0080T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\b\u001a\u00020\tX\u0080T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\n\u001a\u00020\u0001X\u0080T\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0007\n\u0005\b\u009920\u0001\u00a8\u0006 "}, d2={"HIGH_SURROGATE_HEADER", "", "LOG_SURROGATE_HEADER", "MASK_2BYTES", "MASK_3BYTES", "MASK_4BYTES", "REPLACEMENT_BYTE", "", "REPLACEMENT_CHARACTER", "", "REPLACEMENT_CODE_POINT", "isIsoControl", "", "codePoint", "isUtf8Continuation", "byte", "process2Utf8Bytes", "", "beginIndex", "endIndex", "yield", "Lkotlin/Function1;", "", "process3Utf8Bytes", "process4Utf8Bytes", "processUtf16Chars", "processUtf8Bytes", "", "processUtf8CodePoints", "utf8Size", "", "size", "okio"})
@JvmName(name="Utf8")
@SourceDebugExtension(value={"SMAP\nUtf8.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Utf8.kt\nokio/Utf8\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 Util.kt\nokio/-SegmentedByteString\n*L\n1#1,559:1\n397#1,9:563\n127#1:572\n406#1,20:574\n440#1,4:595\n127#1:599\n446#1,10:601\n127#1:611\n456#1,5:612\n127#1:617\n461#1,24:618\n500#1,4:643\n127#1:647\n506#1,2:649\n127#1:651\n510#1,10:652\n127#1:662\n520#1,5:663\n127#1:668\n525#1,5:669\n127#1:674\n530#1,28:675\n397#1,9:704\n127#1:713\n406#1,20:715\n440#1,4:736\n127#1:740\n446#1,10:742\n127#1:752\n456#1,5:753\n127#1:758\n461#1,24:759\n500#1,4:784\n127#1:788\n506#1,2:790\n127#1:792\n510#1,10:793\n127#1:803\n520#1,5:804\n127#1:809\n525#1,5:810\n127#1:815\n530#1,28:816\n127#1:844\n127#1:846\n127#1:848\n127#1:850\n127#1:852\n127#1:854\n127#1:856\n127#1:858\n127#1:860\n1#2:560\n74#3:561\n68#3:562\n74#3:573\n68#3:594\n74#3:600\n68#3:642\n74#3:648\n68#3:703\n74#3:714\n68#3:735\n74#3:741\n68#3:783\n74#3:789\n74#3:845\n74#3:847\n74#3:849\n74#3:851\n74#3:853\n74#3:855\n74#3:857\n74#3:859\n74#3:861\n*S KotlinDebug\n*F\n+ 1 Utf8.kt\nokio/Utf8\n*L\n228#1:563,9\n228#1:572\n228#1:574,20\n232#1:595,4\n232#1:599\n232#1:601,10\n232#1:611\n232#1:612,5\n232#1:617\n232#1:618,24\n236#1:643,4\n236#1:647\n236#1:649,2\n236#1:651\n236#1:652,10\n236#1:662\n236#1:663,5\n236#1:668\n236#1:669,5\n236#1:674\n236#1:675,28\n277#1:704,9\n277#1:713\n277#1:715,20\n281#1:736,4\n281#1:740\n281#1:742,10\n281#1:752\n281#1:753,5\n281#1:758\n281#1:759,24\n285#1:784,4\n285#1:788\n285#1:790,2\n285#1:792\n285#1:793,10\n285#1:803\n285#1:804,5\n285#1:809\n285#1:810,5\n285#1:815\n285#1:816,28\n405#1:844\n443#1:846\n455#1:848\n460#1:850\n503#1:852\n507#1:854\n519#1:856\n524#1:858\n529#1:860\n127#1:561\n226#1:562\n228#1:573\n230#1:594\n232#1:600\n234#1:642\n236#1:648\n275#1:703\n277#1:714\n279#1:735\n281#1:741\n283#1:783\n285#1:789\n405#1:845\n443#1:847\n455#1:849\n460#1:851\n503#1:853\n507#1:855\n519#1:857\n524#1:859\n529#1:861\n*E\n"})
public final class Utf8 {
    public static final byte REPLACEMENT_BYTE = 63;
    public static final char REPLACEMENT_CHARACTER = '\ufffd';
    public static final int REPLACEMENT_CODE_POINT = 65533;
    public static final int HIGH_SURROGATE_HEADER = 55232;
    public static final int LOG_SURROGATE_HEADER = 56320;
    public static final int MASK_2BYTES = 3968;
    public static final int MASK_3BYTES = -123008;
    public static final int MASK_4BYTES = 3678080;

    @JvmOverloads
    @JvmName(name="size")
    public static final long size(@NotNull String string, int n2, int n3) {
        Intrinsics.checkNotNullParameter(string, "");
        if (!(n2 >= 0)) {
            String string2 = "beginIndex < 0: " + n2;
            throw new IllegalArgumentException(string2.toString());
        }
        if (!(n3 >= n2)) {
            String string3 = "endIndex < beginIndex: " + n3 + " < " + n2;
            throw new IllegalArgumentException(string3.toString());
        }
        if (!(n3 <= string.length())) {
            String string4 = "endIndex > string.length: " + n3 + " > " + string.length();
            throw new IllegalArgumentException(string4.toString());
        }
        long l2 = 0L;
        while (n2 < n3) {
            char c2;
            char c3 = string.charAt(n2);
            if (c3 < '\u0080') {
                long l3 = l2;
                l2 = l3 + 1L;
                ++n2;
                continue;
            }
            if (c3 < '\u0800') {
                l2 += 2L;
                ++n2;
                continue;
            }
            if (c3 < '\ud800' || c3 > '\udfff') {
                l2 += 3L;
                ++n2;
                continue;
            }
            char c4 = c2 = n2 + 1 < n3 ? string.charAt(n2 + 1) : (char)'\u0000';
            if (c3 > '\udbff' || c2 < '\udc00' || c2 > '\udfff') {
                long l4 = l2;
                l2 = l4 + 1L;
                ++n2;
                continue;
            }
            l2 += 4L;
            n2 += 2;
        }
        return l2;
    }

    public static /* synthetic */ long size$default(String string, int n2, int n3, int n4, Object object) {
        if ((n4 & 1) != 0) {
            n2 = 0;
        }
        if ((n4 & 2) != 0) {
            n3 = string.length();
        }
        return Utf8.size(string, n2, n3);
    }

    public static final boolean isIsoControl(int n2) {
        block4: {
            block3: {
                if (n2 >= 0 ? n2 < 32 : false) break block3;
                boolean bl = 127 <= n2 ? n2 < 160 : false;
                if (!bl) break block4;
            }
            return true;
        }
        return false;
    }

    public static final boolean isUtf8Continuation(byte by) {
        return (by & 0xC0) == 128;
    }

    public static final void processUtf8Bytes(@NotNull String string, int n2, int n3, @NotNull Function1<? super Byte, Unit> function1) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(function1, "");
        while (n2 < n3) {
            int n4;
            char c2;
            block10: {
                block9: {
                    c2 = string.charAt(n2);
                    if (Intrinsics.compare(c2, 128) < 0) {
                        function1.invoke((Byte)((byte)c2));
                        ++n2;
                        while (n2 < n3 && Intrinsics.compare(string.charAt(n2), 128) < 0) {
                            function1.invoke((Byte)((byte)string.charAt(n2++)));
                        }
                        continue;
                    }
                    if (Intrinsics.compare(c2, 2048) < 0) {
                        function1.invoke((Byte)((byte)(c2 >> 6 | 0xC0)));
                        function1.invoke((Byte)((byte)(c2 & 0x3F | 0x80)));
                        ++n2;
                        continue;
                    }
                    if (!('\ud800' <= c2 ? c2 < '\ue000' : false)) {
                        function1.invoke((Byte)((byte)(c2 >> 12 | 0xE0)));
                        function1.invoke((Byte)((byte)(c2 >> 6 & 0x3F | 0x80)));
                        function1.invoke((Byte)((byte)(c2 & 0x3F | 0x80)));
                        ++n2;
                        continue;
                    }
                    if (Intrinsics.compare(c2, 56319) > 0 || n3 <= n2 + 1) break block9;
                    n4 = string.charAt(n2 + 1);
                    if (56320 <= n4 ? n4 < 57344 : false) break block10;
                }
                function1.invoke((Byte)((byte)63));
                ++n2;
                continue;
            }
            n4 = (c2 << 10) + string.charAt(n2 + 1) + -56613888;
            function1.invoke((Byte)((byte)(n4 >> 18 | 0xF0)));
            function1.invoke((Byte)((byte)(n4 >> 12 & 0x3F | 0x80)));
            function1.invoke((Byte)((byte)(n4 >> 6 & 0x3F | 0x80)));
            function1.invoke((Byte)((byte)(n4 & 0x3F | 0x80)));
            n2 += 2;
        }
    }

    /*
     * Unable to fully structure code
     */
    public static final void processUtf8CodePoints(@NotNull byte[] var0, int var1_1, int var2_2, @NotNull Function1<? super Integer, Unit> var3_3) {
        Intrinsics.checkNotNullParameter(var0, "");
        Intrinsics.checkNotNullParameter(var3_3, "");
        while (var1_1 < var2_2) {
            block37: {
                block41: {
                    block38: {
                        block40: {
                            block39: {
                                block34: {
                                    block36: {
                                        block35: {
                                            var4_4 = var0[var1_1];
                                            if (var4_4 >= 0) {
                                                var3_3.invoke((Integer)var4_4);
                                                ++var1_1;
                                                while (var1_1 < var2_2 && var0[var1_1] >= 0) {
                                                    var3_3.invoke(Integer.valueOf(var0[var1_1++]));
                                                }
                                                continue;
                                            }
                                            var5_5 = var4_4;
                                            if (var5_5 >> 5 == -2) {
                                                v0 = var1_1;
                                                var5_6 = var0;
                                                if (var2_2 <= var1_1 + 1) {
                                                    var4_4 = v0;
                                                    var3_3.invoke((Integer)65533);
                                                    v1 = var4_4;
                                                    v2 = 1;
                                                } else {
                                                    var4_4 = var5_6[var1_1];
                                                    var8_11 = var6_9 = var5_6[var1_1 + 1];
                                                    if (!((var6_9 & 192) == 128)) {
                                                        var4_4 = v0;
                                                        var3_3.invoke((Integer)65533);
                                                        v1 = var4_4;
                                                        v2 = 1;
                                                    } else {
                                                        var7_10 = 3968 ^ var6_9 ^ var4_4 << 6;
                                                        if (var7_10 < 128) {
                                                            var4_4 = v0;
                                                            var3_3.invoke((Integer)65533);
                                                            v1 = var4_4;
                                                        } else {
                                                            var1_1 = var7_10;
                                                            var4_4 = v0;
                                                            var3_3.invoke((Integer)var1_1);
                                                            v1 = var4_4;
                                                        }
                                                        v2 = 2;
                                                    }
                                                }
                                                var1_1 = v1 + v2;
                                                continue;
                                            }
                                            var5_5 = var4_4;
                                            if (var5_5 >> 4 != -2) break block34;
                                            v3 = var1_1;
                                            var5_7 = var0;
                                            if (var2_2 > var1_1 + 2) break block35;
                                            var4_4 = v3;
                                            var3_3.invoke((Integer)65533);
                                            v4 = var4_4;
                                            if (var2_2 <= var1_1 + 1) ** GOTO lbl-1000
                                            var7_10 = var4_4 = var5_7[var1_1 + 1];
                                            if (!((var4_4 & 192) == 128)) lbl-1000:
                                            // 2 sources

                                            {
                                                v5 = 1;
                                            } else {
                                                v5 = 2;
                                            }
                                            break block36;
                                        }
                                        var4_4 = var5_7[var1_1];
                                        var8_11 = var6_9 = var5_7[var1_1 + 1];
                                        if (!((var6_9 & 192) == 128)) {
                                            var4_4 = v3;
                                            var3_3.invoke((Integer)65533);
                                            v4 = var4_4;
                                            v5 = 1;
                                        } else {
                                            var8_11 = var7_10 = var5_7[var1_1 + 2];
                                            if (!((var7_10 & 192) == 128)) {
                                                var4_4 = v3;
                                                var3_3.invoke((Integer)65533);
                                                v4 = var4_4;
                                                v5 = 2;
                                            } else {
                                                var8_11 = -123008 ^ var7_10 ^ var6_9 << 6 ^ var4_4 << 12;
                                                if (var8_11 < 2048) {
                                                    var4_4 = v3;
                                                    var3_3.invoke((Integer)65533);
                                                    v4 = var4_4;
                                                } else {
                                                    v6 = 55296 <= var8_11 ? var8_11 < 57344 : false;
                                                    if (v6) {
                                                        var4_4 = v3;
                                                        var3_3.invoke((Integer)65533);
                                                        v4 = var4_4;
                                                    } else {
                                                        var1_1 = var8_11;
                                                        var4_4 = v3;
                                                        var3_3.invoke((Integer)var1_1);
                                                        v4 = var4_4;
                                                    }
                                                }
                                                v5 = 3;
                                            }
                                        }
                                    }
                                    var1_1 = v4 + v5;
                                    continue;
                                }
                                var5_5 = var4_4;
                                if (var5_5 >> 3 != -2) break block37;
                                v7 = var1_1;
                                var5_8 = var0;
                                if (var2_2 > var1_1 + 3) break block38;
                                var4_4 = v7;
                                var3_3.invoke((Integer)65533);
                                v8 = var4_4;
                                if (var2_2 <= var1_1 + 1) break block39;
                                var7_10 = var4_4 = var5_8[var1_1 + 1];
                                if ((var4_4 & 192) == 128) break block40;
                            }
                            v9 = 1;
                            break block41;
                        }
                        if (var2_2 <= var1_1 + 2) ** GOTO lbl-1000
                        var7_10 = var4_4 = var5_8[var1_1 + 2];
                        if (!((var4_4 & 192) == 128)) lbl-1000:
                        // 2 sources

                        {
                            v9 = 2;
                        } else {
                            v9 = 3;
                        }
                        break block41;
                    }
                    var4_4 = var5_8[var1_1];
                    var8_11 = var6_9 = var5_8[var1_1 + 1];
                    if (!((var6_9 & 192) == 128)) {
                        var4_4 = v7;
                        var3_3.invoke((Integer)65533);
                        v8 = var4_4;
                        v9 = 1;
                    } else {
                        var8_11 = var7_10 = var5_8[var1_1 + 2];
                        if (!((var7_10 & 192) == 128)) {
                            var4_4 = v7;
                            var3_3.invoke((Integer)65533);
                            v8 = var4_4;
                            v9 = 2;
                        } else {
                            var1_1 = var8_11 = var5_8[var1_1 + 3];
                            if (!((var8_11 & 192) == 128)) {
                                var4_4 = v7;
                                var3_3.invoke((Integer)65533);
                                v8 = var4_4;
                                v9 = 3;
                            } else {
                                if ((var8_11 = 3678080 ^ var8_11 ^ var7_10 << 6 ^ var6_9 << 12 ^ var4_4 << 18) > 0x10FFFF) {
                                    var4_4 = v7;
                                    var3_3.invoke((Integer)65533);
                                    v8 = var4_4;
                                } else {
                                    v10 = 55296 <= var8_11 ? var8_11 < 57344 : false;
                                    if (v10) {
                                        var4_4 = v7;
                                        var3_3.invoke((Integer)65533);
                                        v8 = var4_4;
                                    } else if (var8_11 < 65536) {
                                        var4_4 = v7;
                                        var3_3.invoke((Integer)65533);
                                        v8 = var4_4;
                                    } else {
                                        var1_1 = var8_11;
                                        var4_4 = v7;
                                        var3_3.invoke((Integer)var1_1);
                                        v8 = var4_4;
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
            var3_3.invoke((Integer)65533);
            ++var1_1;
        }
    }

    /*
     * Unable to fully structure code
     */
    public static final void processUtf16Chars(@NotNull byte[] var0, int var1_1, int var2_2, @NotNull Function1<? super Character, Unit> var3_3) {
        Intrinsics.checkNotNullParameter(var0, "");
        Intrinsics.checkNotNullParameter(var3_3, "");
        while (var1_1 < var2_2) {
            block39: {
                block43: {
                    block40: {
                        block42: {
                            block41: {
                                block36: {
                                    block38: {
                                        block37: {
                                            var4_4 = var0[var1_1];
                                            if (var4_4 >= 0) {
                                                var3_3.invoke(Character.valueOf((char)var4_4));
                                                ++var1_1;
                                                while (var1_1 < var2_2 && var0[var1_1] >= 0) {
                                                    var3_3.invoke(Character.valueOf((char)var0[var1_1++]));
                                                }
                                                continue;
                                            }
                                            var5_5 = var4_4;
                                            if (var5_5 >> 5 == -2) {
                                                v0 = var1_1;
                                                var5_6 = var0;
                                                if (var2_2 <= var1_1 + 1) {
                                                    var4_4 = v0;
                                                    var3_3.invoke(Character.valueOf('\ufffd'));
                                                    v1 = var4_4;
                                                    v2 = 1;
                                                } else {
                                                    var4_4 = var5_6[var1_1];
                                                    var8_11 = var6_9 = var5_6[var1_1 + 1];
                                                    if (!((var6_9 & 192) == 128)) {
                                                        var4_4 = v0;
                                                        var3_3.invoke(Character.valueOf('\ufffd'));
                                                        v1 = var4_4;
                                                        v2 = 1;
                                                    } else {
                                                        var7_10 = 3968 ^ var6_9 ^ var4_4 << 6;
                                                        if (var7_10 < 128) {
                                                            var4_4 = v0;
                                                            var3_3.invoke(Character.valueOf('\ufffd'));
                                                            v1 = var4_4;
                                                        } else {
                                                            var1_1 = var7_10;
                                                            var4_4 = v0;
                                                            var3_3.invoke(Character.valueOf((char)var1_1));
                                                            v1 = var4_4;
                                                        }
                                                        v2 = 2;
                                                    }
                                                }
                                                var1_1 = v1 + v2;
                                                continue;
                                            }
                                            var5_5 = var4_4;
                                            if (var5_5 >> 4 != -2) break block36;
                                            v3 = var1_1;
                                            var5_7 = var0;
                                            if (var2_2 > var1_1 + 2) break block37;
                                            var4_4 = v3;
                                            var3_3.invoke(Character.valueOf('\ufffd'));
                                            v4 = var4_4;
                                            if (var2_2 <= var1_1 + 1) ** GOTO lbl-1000
                                            var7_10 = var4_4 = var5_7[var1_1 + 1];
                                            if (!((var4_4 & 192) == 128)) lbl-1000:
                                            // 2 sources

                                            {
                                                v5 = 1;
                                            } else {
                                                v5 = 2;
                                            }
                                            break block38;
                                        }
                                        var4_4 = var5_7[var1_1];
                                        var8_11 = var6_9 = var5_7[var1_1 + 1];
                                        if (!((var6_9 & 192) == 128)) {
                                            var4_4 = v3;
                                            var3_3.invoke(Character.valueOf('\ufffd'));
                                            v4 = var4_4;
                                            v5 = 1;
                                        } else {
                                            var8_11 = var7_10 = var5_7[var1_1 + 2];
                                            if (!((var7_10 & 192) == 128)) {
                                                var4_4 = v3;
                                                var3_3.invoke(Character.valueOf('\ufffd'));
                                                v4 = var4_4;
                                                v5 = 2;
                                            } else {
                                                var8_11 = -123008 ^ var7_10 ^ var6_9 << 6 ^ var4_4 << 12;
                                                if (var8_11 < 2048) {
                                                    var4_4 = v3;
                                                    var3_3.invoke(Character.valueOf('\ufffd'));
                                                    v4 = var4_4;
                                                } else {
                                                    v6 = 55296 <= var8_11 ? var8_11 < 57344 : false;
                                                    if (v6) {
                                                        var4_4 = v3;
                                                        var3_3.invoke(Character.valueOf('\ufffd'));
                                                        v4 = var4_4;
                                                    } else {
                                                        var1_1 = var8_11;
                                                        var4_4 = v3;
                                                        var3_3.invoke(Character.valueOf((char)var1_1));
                                                        v4 = var4_4;
                                                    }
                                                }
                                                v5 = 3;
                                            }
                                        }
                                    }
                                    var1_1 = v4 + v5;
                                    continue;
                                }
                                var5_5 = var4_4;
                                if (var5_5 >> 3 != -2) break block39;
                                v7 = var1_1;
                                var5_8 = var0;
                                if (var2_2 > var1_1 + 3) break block40;
                                var4_4 = v7;
                                var3_3.invoke(Character.valueOf('\ufffd'));
                                v8 = var4_4;
                                if (var2_2 <= var1_1 + 1) break block41;
                                var7_10 = var4_4 = var5_8[var1_1 + 1];
                                if ((var4_4 & 192) == 128) break block42;
                            }
                            v9 = 1;
                            break block43;
                        }
                        if (var2_2 <= var1_1 + 2) ** GOTO lbl-1000
                        var7_10 = var4_4 = var5_8[var1_1 + 2];
                        if (!((var4_4 & 192) == 128)) lbl-1000:
                        // 2 sources

                        {
                            v9 = 2;
                        } else {
                            v9 = 3;
                        }
                        break block43;
                    }
                    var4_4 = var5_8[var1_1];
                    var8_11 = var6_9 = var5_8[var1_1 + 1];
                    if (!((var6_9 & 192) == 128)) {
                        var4_4 = v7;
                        var3_3.invoke(Character.valueOf('\ufffd'));
                        v8 = var4_4;
                        v9 = 1;
                    } else {
                        var8_11 = var7_10 = var5_8[var1_1 + 2];
                        if (!((var7_10 & 192) == 128)) {
                            var4_4 = v7;
                            var3_3.invoke(Character.valueOf('\ufffd'));
                            v8 = var4_4;
                            v9 = 2;
                        } else {
                            var1_1 = var8_11 = var5_8[var1_1 + 3];
                            if (!((var8_11 & 192) == 128)) {
                                var4_4 = v7;
                                var3_3.invoke(Character.valueOf('\ufffd'));
                                v8 = var4_4;
                                v9 = 3;
                            } else {
                                if ((var8_11 = 3678080 ^ var8_11 ^ var7_10 << 6 ^ var6_9 << 12 ^ var4_4 << 18) > 0x10FFFF) {
                                    var4_4 = v7;
                                    var3_3.invoke(Character.valueOf('\ufffd'));
                                    v8 = var4_4;
                                } else {
                                    v10 = 55296 <= var8_11 ? var8_11 < 57344 : false;
                                    if (v10) {
                                        var4_4 = v7;
                                        var3_3.invoke(Character.valueOf('\ufffd'));
                                        v8 = var4_4;
                                    } else if (var8_11 < 65536) {
                                        var4_4 = v7;
                                        var3_3.invoke(Character.valueOf('\ufffd'));
                                        v8 = var4_4;
                                    } else {
                                        var1_1 = var8_11;
                                        var4_4 = v7;
                                        if (var1_1 != 65533) {
                                            var3_3.invoke(Character.valueOf((char)((var1_1 >>> 10) + 55232)));
                                            var3_3.invoke(Character.valueOf((char)((var1_1 & 1023) + 56320)));
                                        } else {
                                            var3_3.invoke(Character.valueOf('\ufffd'));
                                        }
                                        v8 = var4_4;
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
            var3_3.invoke(Character.valueOf('\ufffd'));
            ++var1_1;
        }
    }

    public static final int process2Utf8Bytes(@NotNull byte[] byArray, int n2, int n3, @NotNull Function1<? super Integer, Unit> function1) {
        Intrinsics.checkNotNullParameter(byArray, "");
        Intrinsics.checkNotNullParameter(function1, "");
        if (n3 <= n2 + 1) {
            function1.invoke((Integer)65533);
            return 1;
        }
        n3 = byArray[n2];
        int n4 = byArray[n2 + 1];
        if (!((n4 & 0xC0) == 128)) {
            function1.invoke((Integer)65533);
            return 1;
        }
        if ((n4 = 0xF80 ^ n4 ^ n3 << 6) < 128) {
            function1.invoke((Integer)65533);
        } else {
            function1.invoke((Integer)n4);
        }
        return 2;
    }

    public static final int process3Utf8Bytes(@NotNull byte[] byArray, int n2, int n3, @NotNull Function1<? super Integer, Unit> function1) {
        Intrinsics.checkNotNullParameter(byArray, "");
        Intrinsics.checkNotNullParameter(function1, "");
        if (n3 <= n2 + 2) {
            function1.invoke((Integer)65533);
            if (n3 <= n2 + 1 || !(((n3 = byArray[n2 + 1]) & 0xC0) == 128)) {
                return 1;
            }
            return 2;
        }
        n3 = byArray[n2];
        byte by = byArray[n2 + 1];
        if (!((by & 0xC0) == 128)) {
            function1.invoke((Integer)65533);
            return 1;
        }
        int n4 = byArray[n2 + 2];
        if (!((n4 & 0xC0) == 128)) {
            function1.invoke((Integer)65533);
            return 2;
        }
        if ((n4 = 0xFFFE1F80 ^ n4 ^ by << 6 ^ n3 << 12) < 2048) {
            function1.invoke((Integer)65533);
        } else {
            boolean bl = 55296 <= n4 ? n4 < 57344 : false;
            if (bl) {
                function1.invoke((Integer)65533);
            } else {
                function1.invoke((Integer)n4);
            }
        }
        return 3;
    }

    public static final int process4Utf8Bytes(@NotNull byte[] byArray, int n2, int n3, @NotNull Function1<? super Integer, Unit> function1) {
        Intrinsics.checkNotNullParameter(byArray, "");
        Intrinsics.checkNotNullParameter(function1, "");
        if (n3 <= n2 + 3) {
            byte by;
            function1.invoke((Integer)65533);
            if (n3 <= n2 + 1 || !(((by = byArray[n2 + 1]) & 0xC0) == 128)) {
                return 1;
            }
            if (n3 <= n2 + 2 || !(((by = byArray[n2 + 2]) & 0xC0) == 128)) {
                return 2;
            }
            return 3;
        }
        byte by = byArray[n2];
        n3 = byArray[n2 + 1];
        if (!((n3 & 0xC0) == 128)) {
            function1.invoke((Integer)65533);
            return 1;
        }
        byte by2 = byArray[n2 + 2];
        if (!((by2 & 0xC0) == 128)) {
            function1.invoke((Integer)65533);
            return 2;
        }
        int n4 = byArray[n2 + 3];
        if (!((n4 & 0xC0) == 128)) {
            function1.invoke((Integer)65533);
            return 3;
        }
        if ((n4 = 0x381F80 ^ n4 ^ by2 << 6 ^ n3 << 12 ^ by << 18) > 0x10FFFF) {
            function1.invoke((Integer)65533);
        } else {
            boolean bl = 55296 <= n4 ? n4 < 57344 : false;
            if (bl) {
                function1.invoke((Integer)65533);
            } else if (n4 < 65536) {
                function1.invoke((Integer)65533);
            } else {
                function1.invoke((Integer)n4);
            }
        }
        return 4;
    }

    @JvmOverloads
    @JvmName(name="size")
    public static final long size(@NotNull String string, int n2) {
        Intrinsics.checkNotNullParameter(string, "");
        return Utf8.size$default(string, n2, 0, 2, null);
    }

    @JvmOverloads
    @JvmName(name="size")
    public static final long size(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, "");
        return Utf8.size$default(string, 0, 0, 3, null);
    }
}


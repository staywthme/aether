/*
 * Decompiled with CFR 0.152.
 */
package kotlin.collections.unsigned;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;
import java.util.RandomAccess;
import kotlin.Deprecated;
import kotlin.DeprecatedSinceKotlin;
import kotlin.ExperimentalUnsignedTypes;
import kotlin.Metadata;
import kotlin.OverloadResolutionByLambdaReturnType;
import kotlin.ReplaceWith;
import kotlin.SinceKotlin;
import kotlin.UByte;
import kotlin.UByteArray;
import kotlin.UInt;
import kotlin.UIntArray;
import kotlin.ULong;
import kotlin.ULongArray;
import kotlin.UShort;
import kotlin.UShortArray;
import kotlin.UnsignedKt;
import kotlin.collections.AbstractList;
import kotlin.collections.ArraysKt;
import kotlin.collections.IntIterator;
import kotlin.collections.unsigned.UArraysKt;
import kotlin.internal.InlineOnly;
import kotlin.jvm.JvmName;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import org.jetbrains.annotations.NotNull;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(mv={1, 8, 0}, k=5, xi=49, d1={"\u0000h\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b \n\u0002\u0010\u000f\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0018\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\u001a\u001c\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001*\u00020\u0003H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0004\u0010\u0005\u001a\u001c\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00060\u0001*\u00020\u0007H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\b\u0010\t\u001a\u001c\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\n0\u0001*\u00020\u000bH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\f\u0010\r\u001a\u001c\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0001*\u00020\u000fH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0010\u0010\u0011\u001a2\u0010\u0012\u001a\u00020\u0013*\u00020\u00032\u0006\u0010\u0014\u001a\u00020\u00022\b\b\u0002\u0010\u0015\u001a\u00020\u00132\b\b\u0002\u0010\u0016\u001a\u00020\u0013H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0017\u0010\u0018\u001a2\u0010\u0012\u001a\u00020\u0013*\u00020\u00072\u0006\u0010\u0014\u001a\u00020\u00062\b\b\u0002\u0010\u0015\u001a\u00020\u00132\b\b\u0002\u0010\u0016\u001a\u00020\u0013H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0019\u0010\u001a\u001a2\u0010\u0012\u001a\u00020\u0013*\u00020\u000b2\u0006\u0010\u0014\u001a\u00020\n2\b\b\u0002\u0010\u0015\u001a\u00020\u00132\b\b\u0002\u0010\u0016\u001a\u00020\u0013H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001b\u0010\u001c\u001a2\u0010\u0012\u001a\u00020\u0013*\u00020\u000f2\u0006\u0010\u0014\u001a\u00020\u000e2\b\b\u0002\u0010\u0015\u001a\u00020\u00132\b\b\u0002\u0010\u0016\u001a\u00020\u0013H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001d\u0010\u001e\u001a\u001f\u0010\u001f\u001a\u00020\u0002*\u00020\u00032\u0006\u0010 \u001a\u00020\u0013H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b!\u0010\"\u001a\u001f\u0010\u001f\u001a\u00020\u0006*\u00020\u00072\u0006\u0010 \u001a\u00020\u0013H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b#\u0010$\u001a\u001f\u0010\u001f\u001a\u00020\n*\u00020\u000b2\u0006\u0010 \u001a\u00020\u0013H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b%\u0010&\u001a\u001f\u0010\u001f\u001a\u00020\u000e*\u00020\u000f2\u0006\u0010 \u001a\u00020\u0013H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b'\u0010(\u001a\u0018\u0010)\u001a\u0004\u0018\u00010\u0002*\u00020\u0003H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b*\u0010+\u001a\u0018\u0010)\u001a\u0004\u0018\u00010\u0006*\u00020\u0007H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b,\u0010-\u001a\u0018\u0010)\u001a\u0004\u0018\u00010\n*\u00020\u000bH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b.\u0010/\u001a\u0018\u0010)\u001a\u0004\u0018\u00010\u000e*\u00020\u000fH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b0\u00101\u001a@\u00102\u001a\u0004\u0018\u00010\u0002\"\u000e\b\u0000\u00103*\b\u0012\u0004\u0012\u0002H304*\u00020\u00032\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u0002H306H\u0087\b\u00f8\u0001\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b7\u00108\u001a@\u00102\u001a\u0004\u0018\u00010\u0006\"\u000e\b\u0000\u00103*\b\u0012\u0004\u0012\u0002H304*\u00020\u00072\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u0002H306H\u0087\b\u00f8\u0001\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b9\u0010:\u001a@\u00102\u001a\u0004\u0018\u00010\n\"\u000e\b\u0000\u00103*\b\u0012\u0004\u0012\u0002H304*\u00020\u000b2\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u0002H306H\u0087\b\u00f8\u0001\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b;\u0010<\u001a@\u00102\u001a\u0004\u0018\u00010\u000e\"\u000e\b\u0000\u00103*\b\u0012\u0004\u0012\u0002H304*\u00020\u000f2\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u0002H306H\u0087\b\u00f8\u0001\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b=\u0010>\u001a4\u0010?\u001a\u0004\u0018\u00010\u0002*\u00020\u00032\u001a\u0010@\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\u00020Aj\n\u0012\u0006\b\u0000\u0012\u00020\u0002`BH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\bC\u0010D\u001a4\u0010?\u001a\u0004\u0018\u00010\u0006*\u00020\u00072\u001a\u0010@\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\u00060Aj\n\u0012\u0006\b\u0000\u0012\u00020\u0006`BH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\bE\u0010F\u001a4\u0010?\u001a\u0004\u0018\u00010\n*\u00020\u000b2\u001a\u0010@\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\n0Aj\n\u0012\u0006\b\u0000\u0012\u00020\n`BH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\bG\u0010H\u001a4\u0010?\u001a\u0004\u0018\u00010\u000e*\u00020\u000f2\u001a\u0010@\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\u000e0Aj\n\u0012\u0006\b\u0000\u0012\u00020\u000e`BH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\bI\u0010J\u001a\u0018\u0010K\u001a\u0004\u0018\u00010\u0002*\u00020\u0003H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\bL\u0010+\u001a\u0018\u0010K\u001a\u0004\u0018\u00010\u0006*\u00020\u0007H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\bM\u0010-\u001a\u0018\u0010K\u001a\u0004\u0018\u00010\n*\u00020\u000bH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\bN\u0010/\u001a\u0018\u0010K\u001a\u0004\u0018\u00010\u000e*\u00020\u000fH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\bO\u00101\u001a@\u0010P\u001a\u0004\u0018\u00010\u0002\"\u000e\b\u0000\u00103*\b\u0012\u0004\u0012\u0002H304*\u00020\u00032\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u0002H306H\u0087\b\u00f8\u0001\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\bQ\u00108\u001a@\u0010P\u001a\u0004\u0018\u00010\u0006\"\u000e\b\u0000\u00103*\b\u0012\u0004\u0012\u0002H304*\u00020\u00072\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u0002H306H\u0087\b\u00f8\u0001\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\bR\u0010:\u001a@\u0010P\u001a\u0004\u0018\u00010\n\"\u000e\b\u0000\u00103*\b\u0012\u0004\u0012\u0002H304*\u00020\u000b2\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u0002H306H\u0087\b\u00f8\u0001\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\bS\u0010<\u001a@\u0010P\u001a\u0004\u0018\u00010\u000e\"\u000e\b\u0000\u00103*\b\u0012\u0004\u0012\u0002H304*\u00020\u000f2\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u0002H306H\u0087\b\u00f8\u0001\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\bT\u0010>\u001a4\u0010U\u001a\u0004\u0018\u00010\u0002*\u00020\u00032\u001a\u0010@\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\u00020Aj\n\u0012\u0006\b\u0000\u0012\u00020\u0002`BH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\bV\u0010D\u001a4\u0010U\u001a\u0004\u0018\u00010\u0006*\u00020\u00072\u001a\u0010@\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\u00060Aj\n\u0012\u0006\b\u0000\u0012\u00020\u0006`BH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\bW\u0010F\u001a4\u0010U\u001a\u0004\u0018\u00010\n*\u00020\u000b2\u001a\u0010@\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\n0Aj\n\u0012\u0006\b\u0000\u0012\u00020\n`BH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\bX\u0010H\u001a4\u0010U\u001a\u0004\u0018\u00010\u000e*\u00020\u000f2\u001a\u0010@\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\u000e0Aj\n\u0012\u0006\b\u0000\u0012\u00020\u000e`BH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\bY\u0010J\u001a.\u0010Z\u001a\u00020[*\u00020\u00032\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020[06H\u0087\b\u00f8\u0001\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b\\\u0010]\u001a.\u0010Z\u001a\u00020^*\u00020\u00032\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020^06H\u0087\b\u00f8\u0001\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b_\u0010`\u001a.\u0010Z\u001a\u00020[*\u00020\u00072\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020[06H\u0087\b\u00f8\u0001\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b\\\u0010a\u001a.\u0010Z\u001a\u00020^*\u00020\u00072\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020^06H\u0087\b\u00f8\u0001\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b_\u0010b\u001a.\u0010Z\u001a\u00020[*\u00020\u000b2\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020[06H\u0087\b\u00f8\u0001\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b\\\u0010c\u001a.\u0010Z\u001a\u00020^*\u00020\u000b2\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020^06H\u0087\b\u00f8\u0001\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b_\u0010d\u001a.\u0010Z\u001a\u00020[*\u00020\u000f2\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020[06H\u0087\b\u00f8\u0001\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b\\\u0010e\u001a.\u0010Z\u001a\u00020^*\u00020\u000f2\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020^06H\u0087\b\u00f8\u0001\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b_\u0010f\u0082\u0002\u000b\n\u0002\b\u0019\n\u0005\b\u009920\u0001\u00a8\u0006g"}, d2={"asList", "", "Lkotlin/UByte;", "Lkotlin/UByteArray;", "asList-GBYM_sE", "([B)Ljava/util/List;", "Lkotlin/UInt;", "Lkotlin/UIntArray;", "asList--ajY-9A", "([I)Ljava/util/List;", "Lkotlin/ULong;", "Lkotlin/ULongArray;", "asList-QwZRm1k", "([J)Ljava/util/List;", "Lkotlin/UShort;", "Lkotlin/UShortArray;", "asList-rL5Bavg", "([S)Ljava/util/List;", "binarySearch", "", "element", "fromIndex", "toIndex", "binarySearch-WpHrYlw", "([BBII)I", "binarySearch-2fe2U9s", "([IIII)I", "binarySearch-K6DWlUc", "([JJII)I", "binarySearch-EtDCXyQ", "([SSII)I", "elementAt", "index", "elementAt-PpDY95g", "([BI)B", "elementAt-qFRl0hI", "([II)I", "elementAt-r7IrZao", "([JI)J", "elementAt-nggk6HY", "([SI)S", "max", "max-GBYM_sE", "([B)Lkotlin/UByte;", "max--ajY-9A", "([I)Lkotlin/UInt;", "max-QwZRm1k", "([J)Lkotlin/ULong;", "max-rL5Bavg", "([S)Lkotlin/UShort;", "maxBy", "R", "", "selector", "Lkotlin/Function1;", "maxBy-JOV_ifY", "([BLkotlin/jvm/functions/Function1;)Lkotlin/UByte;", "maxBy-jgv0xPQ", "([ILkotlin/jvm/functions/Function1;)Lkotlin/UInt;", "maxBy-MShoTSo", "([JLkotlin/jvm/functions/Function1;)Lkotlin/ULong;", "maxBy-xTcfx_M", "([SLkotlin/jvm/functions/Function1;)Lkotlin/UShort;", "maxWith", "comparator", "Ljava/util/Comparator;", "Lkotlin/Comparator;", "maxWith-XMRcp5o", "([BLjava/util/Comparator;)Lkotlin/UByte;", "maxWith-YmdZ_VM", "([ILjava/util/Comparator;)Lkotlin/UInt;", "maxWith-zrEWJaI", "([JLjava/util/Comparator;)Lkotlin/ULong;", "maxWith-eOHTfZs", "([SLjava/util/Comparator;)Lkotlin/UShort;", "min", "min-GBYM_sE", "min--ajY-9A", "min-QwZRm1k", "min-rL5Bavg", "minBy", "minBy-JOV_ifY", "minBy-jgv0xPQ", "minBy-MShoTSo", "minBy-xTcfx_M", "minWith", "minWith-XMRcp5o", "minWith-YmdZ_VM", "minWith-zrEWJaI", "minWith-eOHTfZs", "sumOf", "Ljava/math/BigDecimal;", "sumOfBigDecimal", "([BLkotlin/jvm/functions/Function1;)Ljava/math/BigDecimal;", "Ljava/math/BigInteger;", "sumOfBigInteger", "([BLkotlin/jvm/functions/Function1;)Ljava/math/BigInteger;", "([ILkotlin/jvm/functions/Function1;)Ljava/math/BigDecimal;", "([ILkotlin/jvm/functions/Function1;)Ljava/math/BigInteger;", "([JLkotlin/jvm/functions/Function1;)Ljava/math/BigDecimal;", "([JLkotlin/jvm/functions/Function1;)Ljava/math/BigInteger;", "([SLkotlin/jvm/functions/Function1;)Ljava/math/BigDecimal;", "([SLkotlin/jvm/functions/Function1;)Ljava/math/BigInteger;", "kotlin-stdlib"}, xs="kotlin/collections/unsigned/UArraysKt", pn="kotlin.collections")
class UArraysKt___UArraysJvmKt {
    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final int elementAt-qFRl0hI(int[] nArray, int n2) {
        Intrinsics.checkNotNullParameter(nArray, "");
        return UIntArray.get-pVg5ArA(nArray, n2);
    }

    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final long elementAt-r7IrZao(long[] lArray, int n2) {
        Intrinsics.checkNotNullParameter(lArray, "");
        return ULongArray.get-s-VKNKU(lArray, n2);
    }

    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final byte elementAt-PpDY95g(byte[] byArray, int n2) {
        Intrinsics.checkNotNullParameter(byArray, "");
        return UByteArray.get-w2LRezQ(byArray, n2);
    }

    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final short elementAt-nggk6HY(short[] sArray, int n2) {
        Intrinsics.checkNotNullParameter(sArray, "");
        return UShortArray.get-Mh2AYeg(sArray, n2);
    }

    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final List<UInt> asList--ajY-9A(@NotNull int[] nArray) {
        Intrinsics.checkNotNullParameter(nArray, "");
        return (List)((Object)new RandomAccess(nArray){
            final /* synthetic */ int[] $this_asList;
            {
                this.$this_asList = nArray;
            }

            public final int getSize() {
                return UIntArray.getSize-impl(this.$this_asList);
            }

            public final boolean isEmpty() {
                return UIntArray.isEmpty-impl(this.$this_asList);
            }

            public final boolean contains-WZ4Q5Ns(int n2) {
                return UIntArray.contains-WZ4Q5Ns(this.$this_asList, n2);
            }

            public final int get-pVg5ArA(int n2) {
                return UIntArray.get-pVg5ArA(this.$this_asList, n2);
            }

            public final int indexOf-WZ4Q5Ns(int n2) {
                return ArraysKt.indexOf(this.$this_asList, n2);
            }

            public final int lastIndexOf-WZ4Q5Ns(int n2) {
                return ArraysKt.lastIndexOf(this.$this_asList, n2);
            }

            public final boolean contains(Object object) {
                if (!(object instanceof UInt)) {
                    return false;
                }
                return this.contains-WZ4Q5Ns(((UInt)object).unbox-impl());
            }

            public final /* synthetic */ Object get(int n2) {
                return UInt.box-impl(this.get-pVg5ArA(n2));
            }

            public final int indexOf(Object object) {
                if (!(object instanceof UInt)) {
                    return -1;
                }
                return this.indexOf-WZ4Q5Ns(((UInt)object).unbox-impl());
            }

            public final int lastIndexOf(Object object) {
                if (!(object instanceof UInt)) {
                    return -1;
                }
                return this.lastIndexOf-WZ4Q5Ns(((UInt)object).unbox-impl());
            }
        });
    }

    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final List<ULong> asList-QwZRm1k(@NotNull long[] lArray) {
        Intrinsics.checkNotNullParameter(lArray, "");
        return (List)((Object)new RandomAccess(lArray){
            final /* synthetic */ long[] $this_asList;
            {
                this.$this_asList = lArray;
            }

            public final int getSize() {
                return ULongArray.getSize-impl(this.$this_asList);
            }

            public final boolean isEmpty() {
                return ULongArray.isEmpty-impl(this.$this_asList);
            }

            public final boolean contains-VKZWuLQ(long l2) {
                return ULongArray.contains-VKZWuLQ(this.$this_asList, l2);
            }

            public final long get-s-VKNKU(int n2) {
                return ULongArray.get-s-VKNKU(this.$this_asList, n2);
            }

            public final int indexOf-VKZWuLQ(long l2) {
                return ArraysKt.indexOf(this.$this_asList, l2);
            }

            public final int lastIndexOf-VKZWuLQ(long l2) {
                return ArraysKt.lastIndexOf(this.$this_asList, l2);
            }

            public final boolean contains(Object object) {
                if (!(object instanceof ULong)) {
                    return false;
                }
                return this.contains-VKZWuLQ(((ULong)object).unbox-impl());
            }

            public final /* synthetic */ Object get(int n2) {
                return ULong.box-impl(this.get-s-VKNKU(n2));
            }

            public final int indexOf(Object object) {
                if (!(object instanceof ULong)) {
                    return -1;
                }
                return this.indexOf-VKZWuLQ(((ULong)object).unbox-impl());
            }

            public final int lastIndexOf(Object object) {
                if (!(object instanceof ULong)) {
                    return -1;
                }
                return this.lastIndexOf-VKZWuLQ(((ULong)object).unbox-impl());
            }
        });
    }

    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final List<UByte> asList-GBYM_sE(@NotNull byte[] byArray) {
        Intrinsics.checkNotNullParameter(byArray, "");
        return (List)((Object)new RandomAccess(byArray){
            final /* synthetic */ byte[] $this_asList;
            {
                this.$this_asList = byArray;
            }

            public final int getSize() {
                return UByteArray.getSize-impl(this.$this_asList);
            }

            public final boolean isEmpty() {
                return UByteArray.isEmpty-impl(this.$this_asList);
            }

            public final boolean contains-7apg3OU(byte by) {
                return UByteArray.contains-7apg3OU(this.$this_asList, by);
            }

            public final byte get-w2LRezQ(int n2) {
                return UByteArray.get-w2LRezQ(this.$this_asList, n2);
            }

            public final int indexOf-7apg3OU(byte by) {
                return ArraysKt.indexOf(this.$this_asList, by);
            }

            public final int lastIndexOf-7apg3OU(byte by) {
                return ArraysKt.lastIndexOf(this.$this_asList, by);
            }

            public final boolean contains(Object object) {
                if (!(object instanceof UByte)) {
                    return false;
                }
                return this.contains-7apg3OU(((UByte)object).unbox-impl());
            }

            public final /* synthetic */ Object get(int n2) {
                return UByte.box-impl(this.get-w2LRezQ(n2));
            }

            public final int indexOf(Object object) {
                if (!(object instanceof UByte)) {
                    return -1;
                }
                return this.indexOf-7apg3OU(((UByte)object).unbox-impl());
            }

            public final int lastIndexOf(Object object) {
                if (!(object instanceof UByte)) {
                    return -1;
                }
                return this.lastIndexOf-7apg3OU(((UByte)object).unbox-impl());
            }
        });
    }

    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final List<UShort> asList-rL5Bavg(@NotNull short[] sArray) {
        Intrinsics.checkNotNullParameter(sArray, "");
        return (List)((Object)new RandomAccess(sArray){
            final /* synthetic */ short[] $this_asList;
            {
                this.$this_asList = sArray;
            }

            public final int getSize() {
                return UShortArray.getSize-impl(this.$this_asList);
            }

            public final boolean isEmpty() {
                return UShortArray.isEmpty-impl(this.$this_asList);
            }

            public final boolean contains-xj2QHRw(short s2) {
                return UShortArray.contains-xj2QHRw(this.$this_asList, s2);
            }

            public final short get-Mh2AYeg(int n2) {
                return UShortArray.get-Mh2AYeg(this.$this_asList, n2);
            }

            public final int indexOf-xj2QHRw(short s2) {
                return ArraysKt.indexOf(this.$this_asList, s2);
            }

            public final int lastIndexOf-xj2QHRw(short s2) {
                return ArraysKt.lastIndexOf(this.$this_asList, s2);
            }

            public final boolean contains(Object object) {
                if (!(object instanceof UShort)) {
                    return false;
                }
                return this.contains-xj2QHRw(((UShort)object).unbox-impl());
            }

            public final /* synthetic */ Object get(int n2) {
                return UShort.box-impl(this.get-Mh2AYeg(n2));
            }

            public final int indexOf(Object object) {
                if (!(object instanceof UShort)) {
                    return -1;
                }
                return this.indexOf-xj2QHRw(((UShort)object).unbox-impl());
            }

            public final int lastIndexOf(Object object) {
                if (!(object instanceof UShort)) {
                    return -1;
                }
                return this.lastIndexOf-xj2QHRw(((UShort)object).unbox-impl());
            }
        });
    }

    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    public static final int binarySearch-2fe2U9s(@NotNull int[] nArray, int n2, int n3, int n4) {
        Intrinsics.checkNotNullParameter(nArray, "");
        AbstractList.Companion.checkRangeIndexes$kotlin_stdlib(n3, n4, UIntArray.getSize-impl(nArray));
        --n4;
        while (n3 <= n4) {
            int n5 = n3 + n4 >>> 1;
            int n6 = nArray[n5];
            if ((n6 = UnsignedKt.uintCompare(n6, n2)) < 0) {
                n3 = n5 + 1;
                continue;
            }
            if (n6 > 0) {
                n4 = n5 - 1;
                continue;
            }
            return n5;
        }
        return -(n3 + 1);
    }

    public static /* synthetic */ int binarySearch-2fe2U9s$default(int[] nArray, int n2, int n3, int n4, int n5, Object object) {
        if ((n5 & 2) != 0) {
            n3 = 0;
        }
        if ((n5 & 4) != 0) {
            n4 = UIntArray.getSize-impl(nArray);
        }
        return UArraysKt.binarySearch-2fe2U9s(nArray, n2, n3, n4);
    }

    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    public static final int binarySearch-K6DWlUc(@NotNull long[] lArray, long l2, int n2, int n3) {
        Intrinsics.checkNotNullParameter(lArray, "");
        AbstractList.Companion.checkRangeIndexes$kotlin_stdlib(n2, n3, ULongArray.getSize-impl(lArray));
        --n3;
        while (n2 <= n3) {
            int n4 = n2 + n3 >>> 1;
            long l3 = lArray[n4];
            int n5 = UnsignedKt.ulongCompare(l3, l2);
            if (n5 < 0) {
                n2 = n4 + 1;
                continue;
            }
            if (n5 > 0) {
                n3 = n4 - 1;
                continue;
            }
            return n4;
        }
        return -(n2 + 1);
    }

    public static /* synthetic */ int binarySearch-K6DWlUc$default(long[] lArray, long l2, int n2, int n3, int n4, Object object) {
        if ((n4 & 2) != 0) {
            n2 = 0;
        }
        if ((n4 & 4) != 0) {
            n3 = ULongArray.getSize-impl(lArray);
        }
        return UArraysKt.binarySearch-K6DWlUc(lArray, l2, n2, n3);
    }

    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    public static final int binarySearch-WpHrYlw(@NotNull byte[] byArray, byte by, int n2, int n3) {
        Intrinsics.checkNotNullParameter(byArray, "");
        AbstractList.Companion.checkRangeIndexes$kotlin_stdlib(n2, n3, UByteArray.getSize-impl(byArray));
        by = (byte)(by & 0xFF);
        --n3;
        while (n2 <= n3) {
            int n4 = n2 + n3 >>> 1;
            int n5 = byArray[n4];
            if ((n5 = UnsignedKt.uintCompare(n5, by)) < 0) {
                n2 = n4 + 1;
                continue;
            }
            if (n5 > 0) {
                n3 = n4 - 1;
                continue;
            }
            return n4;
        }
        return -(n2 + 1);
    }

    public static /* synthetic */ int binarySearch-WpHrYlw$default(byte[] byArray, byte by, int n2, int n3, int n4, Object object) {
        if ((n4 & 2) != 0) {
            n2 = 0;
        }
        if ((n4 & 4) != 0) {
            n3 = UByteArray.getSize-impl(byArray);
        }
        return UArraysKt.binarySearch-WpHrYlw(byArray, by, n2, n3);
    }

    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    public static final int binarySearch-EtDCXyQ(@NotNull short[] sArray, short s2, int n2, int n3) {
        Intrinsics.checkNotNullParameter(sArray, "");
        AbstractList.Companion.checkRangeIndexes$kotlin_stdlib(n2, n3, UShortArray.getSize-impl(sArray));
        s2 = (short)(s2 & 0xFFFF);
        --n3;
        while (n2 <= n3) {
            int n4 = n2 + n3 >>> 1;
            int n5 = sArray[n4];
            if ((n5 = UnsignedKt.uintCompare(n5, s2)) < 0) {
                n2 = n4 + 1;
                continue;
            }
            if (n5 > 0) {
                n3 = n4 - 1;
                continue;
            }
            return n4;
        }
        return -(n2 + 1);
    }

    public static /* synthetic */ int binarySearch-EtDCXyQ$default(short[] sArray, short s2, int n2, int n3, int n4, Object object) {
        if ((n4 & 2) != 0) {
            n2 = 0;
        }
        if ((n4 & 4) != 0) {
            n3 = UShortArray.getSize-impl(sArray);
        }
        return UArraysKt.binarySearch-EtDCXyQ(sArray, s2, n2, n3);
    }

    @Deprecated(message="Use maxOrNull instead.", replaceWith=@ReplaceWith(expression="this.maxOrNull()", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.4", errorSince="1.5", hiddenSince="1.6")
    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    public static final /* synthetic */ UInt max--ajY-9A(int[] nArray) {
        Intrinsics.checkNotNullParameter(nArray, "");
        return UArraysKt.maxOrNull--ajY-9A(nArray);
    }

    @Deprecated(message="Use maxOrNull instead.", replaceWith=@ReplaceWith(expression="this.maxOrNull()", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.4", errorSince="1.5", hiddenSince="1.6")
    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    public static final /* synthetic */ ULong max-QwZRm1k(long[] lArray) {
        Intrinsics.checkNotNullParameter(lArray, "");
        return UArraysKt.maxOrNull-QwZRm1k(lArray);
    }

    @Deprecated(message="Use maxOrNull instead.", replaceWith=@ReplaceWith(expression="this.maxOrNull()", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.4", errorSince="1.5", hiddenSince="1.6")
    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    public static final /* synthetic */ UByte max-GBYM_sE(byte[] byArray) {
        Intrinsics.checkNotNullParameter(byArray, "");
        return UArraysKt.maxOrNull-GBYM_sE(byArray);
    }

    @Deprecated(message="Use maxOrNull instead.", replaceWith=@ReplaceWith(expression="this.maxOrNull()", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.4", errorSince="1.5", hiddenSince="1.6")
    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    public static final /* synthetic */ UShort max-rL5Bavg(short[] sArray) {
        Intrinsics.checkNotNullParameter(sArray, "");
        return UArraysKt.maxOrNull-rL5Bavg(sArray);
    }

    @Deprecated(message="Use maxByOrNull instead.", replaceWith=@ReplaceWith(expression="this.maxByOrNull(selector)", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.4", errorSince="1.5", hiddenSince="1.6")
    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final /* synthetic */ <R extends Comparable<? super R>> UInt maxBy-jgv0xPQ(int[] nArray, Function1<? super UInt, ? extends R> function1) {
        Intrinsics.checkNotNullParameter(nArray, "");
        Intrinsics.checkNotNullParameter(function1, "");
        if (UIntArray.isEmpty-impl(nArray)) {
            return null;
        }
        int n2 = UIntArray.get-pVg5ArA(nArray, 0);
        Object object = nArray;
        int n3 = ArraysKt.getLastIndex(nArray);
        if (n3 != 0) {
            object = (Comparable)function1.invoke(UInt.box-impl(n2));
            IntIterator intIterator = new IntRange(1, n3).iterator();
            while (intIterator.hasNext()) {
                int n4 = intIterator.nextInt();
                Comparable comparable = (Comparable)function1.invoke(UInt.box-impl(n4 = UIntArray.get-pVg5ArA(nArray, n4)));
                if (object.compareTo(comparable) >= 0) continue;
                n2 = n4;
                object = comparable;
            }
        }
        return UInt.box-impl(n2);
    }

    @Deprecated(message="Use maxByOrNull instead.", replaceWith=@ReplaceWith(expression="this.maxByOrNull(selector)", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.4", errorSince="1.5", hiddenSince="1.6")
    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final /* synthetic */ <R extends Comparable<? super R>> ULong maxBy-MShoTSo(long[] lArray, Function1<? super ULong, ? extends R> function1) {
        Intrinsics.checkNotNullParameter(lArray, "");
        Intrinsics.checkNotNullParameter(function1, "");
        if (ULongArray.isEmpty-impl(lArray)) {
            return null;
        }
        long l2 = ULongArray.get-s-VKNKU(lArray, 0);
        Object object = lArray;
        int n2 = ArraysKt.getLastIndex(lArray);
        if (n2 != 0) {
            object = (Comparable)function1.invoke(ULong.box-impl(l2));
            IntIterator intIterator = new IntRange(1, n2).iterator();
            while (intIterator.hasNext()) {
                int n3 = intIterator.nextInt();
                long l3 = ULongArray.get-s-VKNKU(lArray, n3);
                Comparable comparable = (Comparable)function1.invoke(ULong.box-impl(l3));
                if (object.compareTo(comparable) >= 0) continue;
                l2 = l3;
                object = comparable;
            }
        }
        return ULong.box-impl(l2);
    }

    @Deprecated(message="Use maxByOrNull instead.", replaceWith=@ReplaceWith(expression="this.maxByOrNull(selector)", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.4", errorSince="1.5", hiddenSince="1.6")
    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final /* synthetic */ <R extends Comparable<? super R>> UByte maxBy-JOV_ifY(byte[] byArray, Function1<? super UByte, ? extends R> function1) {
        Intrinsics.checkNotNullParameter(byArray, "");
        Intrinsics.checkNotNullParameter(function1, "");
        if (UByteArray.isEmpty-impl(byArray)) {
            return null;
        }
        byte by = UByteArray.get-w2LRezQ(byArray, 0);
        Object object = byArray;
        int n2 = ArraysKt.getLastIndex(byArray);
        if (n2 != 0) {
            object = (Comparable)function1.invoke(UByte.box-impl(by));
            IntIterator intIterator = new IntRange(1, n2).iterator();
            while (intIterator.hasNext()) {
                byte by2 = intIterator.nextInt();
                Comparable comparable = (Comparable)function1.invoke(UByte.box-impl(by2 = (byte)UByteArray.get-w2LRezQ(byArray, by2)));
                if (object.compareTo(comparable) >= 0) continue;
                by = by2;
                object = comparable;
            }
        }
        return UByte.box-impl(by);
    }

    @Deprecated(message="Use maxByOrNull instead.", replaceWith=@ReplaceWith(expression="this.maxByOrNull(selector)", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.4", errorSince="1.5", hiddenSince="1.6")
    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final /* synthetic */ <R extends Comparable<? super R>> UShort maxBy-xTcfx_M(short[] sArray, Function1<? super UShort, ? extends R> function1) {
        Intrinsics.checkNotNullParameter(sArray, "");
        Intrinsics.checkNotNullParameter(function1, "");
        if (UShortArray.isEmpty-impl(sArray)) {
            return null;
        }
        short s2 = UShortArray.get-Mh2AYeg(sArray, 0);
        Object object = sArray;
        int n2 = ArraysKt.getLastIndex(sArray);
        if (n2 != 0) {
            object = (Comparable)function1.invoke(UShort.box-impl(s2));
            IntIterator intIterator = new IntRange(1, n2).iterator();
            while (intIterator.hasNext()) {
                short s3 = intIterator.nextInt();
                Comparable comparable = (Comparable)function1.invoke(UShort.box-impl(s3 = (short)UShortArray.get-Mh2AYeg(sArray, s3)));
                if (object.compareTo(comparable) >= 0) continue;
                s2 = s3;
                object = comparable;
            }
        }
        return UShort.box-impl(s2);
    }

    @Deprecated(message="Use maxWithOrNull instead.", replaceWith=@ReplaceWith(expression="this.maxWithOrNull(comparator)", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.4", errorSince="1.5", hiddenSince="1.6")
    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    public static final /* synthetic */ UInt maxWith-YmdZ_VM(int[] nArray, Comparator comparator) {
        Intrinsics.checkNotNullParameter(nArray, "");
        Intrinsics.checkNotNullParameter(comparator, "");
        return UArraysKt.maxWithOrNull-YmdZ_VM(nArray, comparator);
    }

    @Deprecated(message="Use maxWithOrNull instead.", replaceWith=@ReplaceWith(expression="this.maxWithOrNull(comparator)", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.4", errorSince="1.5", hiddenSince="1.6")
    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    public static final /* synthetic */ ULong maxWith-zrEWJaI(long[] lArray, Comparator comparator) {
        Intrinsics.checkNotNullParameter(lArray, "");
        Intrinsics.checkNotNullParameter(comparator, "");
        return UArraysKt.maxWithOrNull-zrEWJaI(lArray, comparator);
    }

    @Deprecated(message="Use maxWithOrNull instead.", replaceWith=@ReplaceWith(expression="this.maxWithOrNull(comparator)", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.4", errorSince="1.5", hiddenSince="1.6")
    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    public static final /* synthetic */ UByte maxWith-XMRcp5o(byte[] byArray, Comparator comparator) {
        Intrinsics.checkNotNullParameter(byArray, "");
        Intrinsics.checkNotNullParameter(comparator, "");
        return UArraysKt.maxWithOrNull-XMRcp5o(byArray, comparator);
    }

    @Deprecated(message="Use maxWithOrNull instead.", replaceWith=@ReplaceWith(expression="this.maxWithOrNull(comparator)", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.4", errorSince="1.5", hiddenSince="1.6")
    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    public static final /* synthetic */ UShort maxWith-eOHTfZs(short[] sArray, Comparator comparator) {
        Intrinsics.checkNotNullParameter(sArray, "");
        Intrinsics.checkNotNullParameter(comparator, "");
        return UArraysKt.maxWithOrNull-eOHTfZs(sArray, comparator);
    }

    @Deprecated(message="Use minOrNull instead.", replaceWith=@ReplaceWith(expression="this.minOrNull()", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.4", errorSince="1.5", hiddenSince="1.6")
    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    public static final /* synthetic */ UInt min--ajY-9A(int[] nArray) {
        Intrinsics.checkNotNullParameter(nArray, "");
        return UArraysKt.minOrNull--ajY-9A(nArray);
    }

    @Deprecated(message="Use minOrNull instead.", replaceWith=@ReplaceWith(expression="this.minOrNull()", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.4", errorSince="1.5", hiddenSince="1.6")
    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    public static final /* synthetic */ ULong min-QwZRm1k(long[] lArray) {
        Intrinsics.checkNotNullParameter(lArray, "");
        return UArraysKt.minOrNull-QwZRm1k(lArray);
    }

    @Deprecated(message="Use minOrNull instead.", replaceWith=@ReplaceWith(expression="this.minOrNull()", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.4", errorSince="1.5", hiddenSince="1.6")
    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    public static final /* synthetic */ UByte min-GBYM_sE(byte[] byArray) {
        Intrinsics.checkNotNullParameter(byArray, "");
        return UArraysKt.minOrNull-GBYM_sE(byArray);
    }

    @Deprecated(message="Use minOrNull instead.", replaceWith=@ReplaceWith(expression="this.minOrNull()", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.4", errorSince="1.5", hiddenSince="1.6")
    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    public static final /* synthetic */ UShort min-rL5Bavg(short[] sArray) {
        Intrinsics.checkNotNullParameter(sArray, "");
        return UArraysKt.minOrNull-rL5Bavg(sArray);
    }

    @Deprecated(message="Use minByOrNull instead.", replaceWith=@ReplaceWith(expression="this.minByOrNull(selector)", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.4", errorSince="1.5", hiddenSince="1.6")
    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final /* synthetic */ <R extends Comparable<? super R>> UInt minBy-jgv0xPQ(int[] nArray, Function1<? super UInt, ? extends R> function1) {
        Intrinsics.checkNotNullParameter(nArray, "");
        Intrinsics.checkNotNullParameter(function1, "");
        if (UIntArray.isEmpty-impl(nArray)) {
            return null;
        }
        int n2 = UIntArray.get-pVg5ArA(nArray, 0);
        Object object = nArray;
        int n3 = ArraysKt.getLastIndex(nArray);
        if (n3 != 0) {
            object = (Comparable)function1.invoke(UInt.box-impl(n2));
            IntIterator intIterator = new IntRange(1, n3).iterator();
            while (intIterator.hasNext()) {
                int n4 = intIterator.nextInt();
                Comparable comparable = (Comparable)function1.invoke(UInt.box-impl(n4 = UIntArray.get-pVg5ArA(nArray, n4)));
                if (object.compareTo(comparable) <= 0) continue;
                n2 = n4;
                object = comparable;
            }
        }
        return UInt.box-impl(n2);
    }

    @Deprecated(message="Use minByOrNull instead.", replaceWith=@ReplaceWith(expression="this.minByOrNull(selector)", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.4", errorSince="1.5", hiddenSince="1.6")
    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final /* synthetic */ <R extends Comparable<? super R>> ULong minBy-MShoTSo(long[] lArray, Function1<? super ULong, ? extends R> function1) {
        Intrinsics.checkNotNullParameter(lArray, "");
        Intrinsics.checkNotNullParameter(function1, "");
        if (ULongArray.isEmpty-impl(lArray)) {
            return null;
        }
        long l2 = ULongArray.get-s-VKNKU(lArray, 0);
        Object object = lArray;
        int n2 = ArraysKt.getLastIndex(lArray);
        if (n2 != 0) {
            object = (Comparable)function1.invoke(ULong.box-impl(l2));
            IntIterator intIterator = new IntRange(1, n2).iterator();
            while (intIterator.hasNext()) {
                int n3 = intIterator.nextInt();
                long l3 = ULongArray.get-s-VKNKU(lArray, n3);
                Comparable comparable = (Comparable)function1.invoke(ULong.box-impl(l3));
                if (object.compareTo(comparable) <= 0) continue;
                l2 = l3;
                object = comparable;
            }
        }
        return ULong.box-impl(l2);
    }

    @Deprecated(message="Use minByOrNull instead.", replaceWith=@ReplaceWith(expression="this.minByOrNull(selector)", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.4", errorSince="1.5", hiddenSince="1.6")
    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final /* synthetic */ <R extends Comparable<? super R>> UByte minBy-JOV_ifY(byte[] byArray, Function1<? super UByte, ? extends R> function1) {
        Intrinsics.checkNotNullParameter(byArray, "");
        Intrinsics.checkNotNullParameter(function1, "");
        if (UByteArray.isEmpty-impl(byArray)) {
            return null;
        }
        byte by = UByteArray.get-w2LRezQ(byArray, 0);
        Object object = byArray;
        int n2 = ArraysKt.getLastIndex(byArray);
        if (n2 != 0) {
            object = (Comparable)function1.invoke(UByte.box-impl(by));
            IntIterator intIterator = new IntRange(1, n2).iterator();
            while (intIterator.hasNext()) {
                byte by2 = intIterator.nextInt();
                Comparable comparable = (Comparable)function1.invoke(UByte.box-impl(by2 = (byte)UByteArray.get-w2LRezQ(byArray, by2)));
                if (object.compareTo(comparable) <= 0) continue;
                by = by2;
                object = comparable;
            }
        }
        return UByte.box-impl(by);
    }

    @Deprecated(message="Use minByOrNull instead.", replaceWith=@ReplaceWith(expression="this.minByOrNull(selector)", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.4", errorSince="1.5", hiddenSince="1.6")
    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final /* synthetic */ <R extends Comparable<? super R>> UShort minBy-xTcfx_M(short[] sArray, Function1<? super UShort, ? extends R> function1) {
        Intrinsics.checkNotNullParameter(sArray, "");
        Intrinsics.checkNotNullParameter(function1, "");
        if (UShortArray.isEmpty-impl(sArray)) {
            return null;
        }
        short s2 = UShortArray.get-Mh2AYeg(sArray, 0);
        Object object = sArray;
        int n2 = ArraysKt.getLastIndex(sArray);
        if (n2 != 0) {
            object = (Comparable)function1.invoke(UShort.box-impl(s2));
            IntIterator intIterator = new IntRange(1, n2).iterator();
            while (intIterator.hasNext()) {
                short s3 = intIterator.nextInt();
                Comparable comparable = (Comparable)function1.invoke(UShort.box-impl(s3 = (short)UShortArray.get-Mh2AYeg(sArray, s3)));
                if (object.compareTo(comparable) <= 0) continue;
                s2 = s3;
                object = comparable;
            }
        }
        return UShort.box-impl(s2);
    }

    @Deprecated(message="Use minWithOrNull instead.", replaceWith=@ReplaceWith(expression="this.minWithOrNull(comparator)", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.4", errorSince="1.5", hiddenSince="1.6")
    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    public static final /* synthetic */ UInt minWith-YmdZ_VM(int[] nArray, Comparator comparator) {
        Intrinsics.checkNotNullParameter(nArray, "");
        Intrinsics.checkNotNullParameter(comparator, "");
        return UArraysKt.minWithOrNull-YmdZ_VM(nArray, comparator);
    }

    @Deprecated(message="Use minWithOrNull instead.", replaceWith=@ReplaceWith(expression="this.minWithOrNull(comparator)", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.4", errorSince="1.5", hiddenSince="1.6")
    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    public static final /* synthetic */ ULong minWith-zrEWJaI(long[] lArray, Comparator comparator) {
        Intrinsics.checkNotNullParameter(lArray, "");
        Intrinsics.checkNotNullParameter(comparator, "");
        return UArraysKt.minWithOrNull-zrEWJaI(lArray, comparator);
    }

    @Deprecated(message="Use minWithOrNull instead.", replaceWith=@ReplaceWith(expression="this.minWithOrNull(comparator)", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.4", errorSince="1.5", hiddenSince="1.6")
    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    public static final /* synthetic */ UByte minWith-XMRcp5o(byte[] byArray, Comparator comparator) {
        Intrinsics.checkNotNullParameter(byArray, "");
        Intrinsics.checkNotNullParameter(comparator, "");
        return UArraysKt.minWithOrNull-XMRcp5o(byArray, comparator);
    }

    @Deprecated(message="Use minWithOrNull instead.", replaceWith=@ReplaceWith(expression="this.minWithOrNull(comparator)", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.4", errorSince="1.5", hiddenSince="1.6")
    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    public static final /* synthetic */ UShort minWith-eOHTfZs(short[] sArray, Comparator comparator) {
        Intrinsics.checkNotNullParameter(sArray, "");
        Intrinsics.checkNotNullParameter(comparator, "");
        return UArraysKt.minWithOrNull-eOHTfZs(sArray, comparator);
    }

    @SinceKotlin(version="1.4")
    @OverloadResolutionByLambdaReturnType
    @JvmName(name="sumOfBigDecimal")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final BigDecimal sumOfBigDecimal(int[] nArray, Function1<? super UInt, ? extends BigDecimal> function1) {
        Intrinsics.checkNotNullParameter(nArray, "");
        Intrinsics.checkNotNullParameter(function1, "");
        BigDecimal bigDecimal = BigDecimal.valueOf(0L);
        Intrinsics.checkNotNullExpressionValue(bigDecimal, "");
        BigDecimal bigDecimal2 = bigDecimal;
        int n2 = UIntArray.getSize-impl(nArray);
        for (int i2 = 0; i2 < n2; ++i2) {
            int n3 = UIntArray.get-pVg5ArA(nArray, i2);
            Intrinsics.checkNotNullExpressionValue(bigDecimal2.add(function1.invoke(UInt.box-impl(n3))), "");
        }
        return bigDecimal2;
    }

    @SinceKotlin(version="1.4")
    @OverloadResolutionByLambdaReturnType
    @JvmName(name="sumOfBigDecimal")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final BigDecimal sumOfBigDecimal(long[] lArray, Function1<? super ULong, ? extends BigDecimal> function1) {
        Intrinsics.checkNotNullParameter(lArray, "");
        Intrinsics.checkNotNullParameter(function1, "");
        BigDecimal bigDecimal = BigDecimal.valueOf(0L);
        Intrinsics.checkNotNullExpressionValue(bigDecimal, "");
        BigDecimal bigDecimal2 = bigDecimal;
        int n2 = ULongArray.getSize-impl(lArray);
        for (int i2 = 0; i2 < n2; ++i2) {
            long l2 = ULongArray.get-s-VKNKU(lArray, i2);
            Intrinsics.checkNotNullExpressionValue(bigDecimal2.add(function1.invoke(ULong.box-impl(l2))), "");
        }
        return bigDecimal2;
    }

    @SinceKotlin(version="1.4")
    @OverloadResolutionByLambdaReturnType
    @JvmName(name="sumOfBigDecimal")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final BigDecimal sumOfBigDecimal(byte[] byArray, Function1<? super UByte, ? extends BigDecimal> function1) {
        Intrinsics.checkNotNullParameter(byArray, "");
        Intrinsics.checkNotNullParameter(function1, "");
        BigDecimal bigDecimal = BigDecimal.valueOf(0L);
        Intrinsics.checkNotNullExpressionValue(bigDecimal, "");
        BigDecimal bigDecimal2 = bigDecimal;
        int n2 = UByteArray.getSize-impl(byArray);
        for (int i2 = 0; i2 < n2; ++i2) {
            byte by = UByteArray.get-w2LRezQ(byArray, i2);
            Intrinsics.checkNotNullExpressionValue(bigDecimal2.add(function1.invoke(UByte.box-impl(by))), "");
        }
        return bigDecimal2;
    }

    @SinceKotlin(version="1.4")
    @OverloadResolutionByLambdaReturnType
    @JvmName(name="sumOfBigDecimal")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final BigDecimal sumOfBigDecimal(short[] sArray, Function1<? super UShort, ? extends BigDecimal> function1) {
        Intrinsics.checkNotNullParameter(sArray, "");
        Intrinsics.checkNotNullParameter(function1, "");
        BigDecimal bigDecimal = BigDecimal.valueOf(0L);
        Intrinsics.checkNotNullExpressionValue(bigDecimal, "");
        BigDecimal bigDecimal2 = bigDecimal;
        int n2 = UShortArray.getSize-impl(sArray);
        for (int i2 = 0; i2 < n2; ++i2) {
            short s2 = UShortArray.get-Mh2AYeg(sArray, i2);
            Intrinsics.checkNotNullExpressionValue(bigDecimal2.add(function1.invoke(UShort.box-impl(s2))), "");
        }
        return bigDecimal2;
    }

    @SinceKotlin(version="1.4")
    @OverloadResolutionByLambdaReturnType
    @JvmName(name="sumOfBigInteger")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final BigInteger sumOfBigInteger(int[] nArray, Function1<? super UInt, ? extends BigInteger> function1) {
        Intrinsics.checkNotNullParameter(nArray, "");
        Intrinsics.checkNotNullParameter(function1, "");
        BigInteger bigInteger = BigInteger.valueOf(0L);
        Intrinsics.checkNotNullExpressionValue(bigInteger, "");
        BigInteger bigInteger2 = bigInteger;
        int n2 = UIntArray.getSize-impl(nArray);
        for (int i2 = 0; i2 < n2; ++i2) {
            int n3 = UIntArray.get-pVg5ArA(nArray, i2);
            Intrinsics.checkNotNullExpressionValue(bigInteger2.add(function1.invoke(UInt.box-impl(n3))), "");
        }
        return bigInteger2;
    }

    @SinceKotlin(version="1.4")
    @OverloadResolutionByLambdaReturnType
    @JvmName(name="sumOfBigInteger")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final BigInteger sumOfBigInteger(long[] lArray, Function1<? super ULong, ? extends BigInteger> function1) {
        Intrinsics.checkNotNullParameter(lArray, "");
        Intrinsics.checkNotNullParameter(function1, "");
        BigInteger bigInteger = BigInteger.valueOf(0L);
        Intrinsics.checkNotNullExpressionValue(bigInteger, "");
        BigInteger bigInteger2 = bigInteger;
        int n2 = ULongArray.getSize-impl(lArray);
        for (int i2 = 0; i2 < n2; ++i2) {
            long l2 = ULongArray.get-s-VKNKU(lArray, i2);
            Intrinsics.checkNotNullExpressionValue(bigInteger2.add(function1.invoke(ULong.box-impl(l2))), "");
        }
        return bigInteger2;
    }

    @SinceKotlin(version="1.4")
    @OverloadResolutionByLambdaReturnType
    @JvmName(name="sumOfBigInteger")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final BigInteger sumOfBigInteger(byte[] byArray, Function1<? super UByte, ? extends BigInteger> function1) {
        Intrinsics.checkNotNullParameter(byArray, "");
        Intrinsics.checkNotNullParameter(function1, "");
        BigInteger bigInteger = BigInteger.valueOf(0L);
        Intrinsics.checkNotNullExpressionValue(bigInteger, "");
        BigInteger bigInteger2 = bigInteger;
        int n2 = UByteArray.getSize-impl(byArray);
        for (int i2 = 0; i2 < n2; ++i2) {
            byte by = UByteArray.get-w2LRezQ(byArray, i2);
            Intrinsics.checkNotNullExpressionValue(bigInteger2.add(function1.invoke(UByte.box-impl(by))), "");
        }
        return bigInteger2;
    }

    @SinceKotlin(version="1.4")
    @OverloadResolutionByLambdaReturnType
    @JvmName(name="sumOfBigInteger")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final BigInteger sumOfBigInteger(short[] sArray, Function1<? super UShort, ? extends BigInteger> function1) {
        Intrinsics.checkNotNullParameter(sArray, "");
        Intrinsics.checkNotNullParameter(function1, "");
        BigInteger bigInteger = BigInteger.valueOf(0L);
        Intrinsics.checkNotNullExpressionValue(bigInteger, "");
        BigInteger bigInteger2 = bigInteger;
        int n2 = UShortArray.getSize-impl(sArray);
        for (int i2 = 0; i2 < n2; ++i2) {
            short s2 = UShortArray.get-Mh2AYeg(sArray, i2);
            Intrinsics.checkNotNullExpressionValue(bigInteger2.add(function1.invoke(UShort.box-impl(s2))), "");
        }
        return bigInteger2;
    }
}


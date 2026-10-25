/*
 * Decompiled with CFR 0.152.
 */
package okio.internal;

import java.io.EOFException;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.JvmName;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import okio.-SegmentedByteString;
import okio.Buffer;
import okio.ByteString;
import okio.Options;
import okio.Segment;
import okio.SegmentPool;
import okio.SegmentedByteString;
import okio.Sink;
import okio.Source;
import okio._JvmPlatformKt;
import okio.internal._Utf8Kt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 9, 0}, k=2, xi=48, d1={"\u0000\u0080\u0001\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0005\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\n\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0015\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a0\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\n2\u0006\u0010\u0010\u001a\u00020\u00012\u0006\u0010\u0011\u001a\u00020\n2\u0006\u0010\u0012\u001a\u00020\nH\u0000\u001a\r\u0010\u0013\u001a\u00020\u0014*\u00020\u0015H\u0080\b\u001a\r\u0010\u0016\u001a\u00020\u0014*\u00020\u0017H\u0080\b\u001a\r\u0010\u0018\u001a\u00020\u0007*\u00020\u0015H\u0080\b\u001a\r\u0010\u0019\u001a\u00020\u0015*\u00020\u0015H\u0080\b\u001a%\u0010\u001a\u001a\u00020\u0015*\u00020\u00152\u0006\u0010\u001b\u001a\u00020\u00152\u0006\u0010\u001c\u001a\u00020\u00072\u0006\u0010\u001d\u001a\u00020\u0007H\u0080\b\u001a\u0017\u0010\u001e\u001a\u00020\f*\u00020\u00152\b\u0010\u001f\u001a\u0004\u0018\u00010 H\u0080\b\u001a\u0015\u0010!\u001a\u00020\u0007*\u00020\u00172\u0006\u0010\"\u001a\u00020\nH\u0080\b\u001a\u0015\u0010#\u001a\u00020$*\u00020\u00152\u0006\u0010%\u001a\u00020\u0007H\u0080\b\u001a\r\u0010&\u001a\u00020\n*\u00020\u0015H\u0080\b\u001a%\u0010'\u001a\u00020\u0007*\u00020\u00152\u0006\u0010(\u001a\u00020$2\u0006\u0010)\u001a\u00020\u00072\u0006\u0010*\u001a\u00020\u0007H\u0080\b\u001a\u001d\u0010'\u001a\u00020\u0007*\u00020\u00152\u0006\u0010\u0010\u001a\u00020+2\u0006\u0010)\u001a\u00020\u0007H\u0080\b\u001a\u001d\u0010,\u001a\u00020\u0007*\u00020\u00152\u0006\u0010-\u001a\u00020+2\u0006\u0010)\u001a\u00020\u0007H\u0080\b\u001a\r\u0010.\u001a\u00020\n*\u00020\u0017H\u0080\b\u001a-\u0010/\u001a\u00020\f*\u00020\u00152\u0006\u0010\u001c\u001a\u00020\u00072\u0006\u0010\u0010\u001a\u00020+2\u0006\u0010\u0011\u001a\u00020\n2\u0006\u0010\u001d\u001a\u00020\nH\u0080\b\u001a\u0015\u00100\u001a\u00020\n*\u00020\u00152\u0006\u00101\u001a\u00020\u0001H\u0080\b\u001a%\u00100\u001a\u00020\n*\u00020\u00152\u0006\u00101\u001a\u00020\u00012\u0006\u0010\u001c\u001a\u00020\n2\u0006\u0010\u001d\u001a\u00020\nH\u0080\b\u001a\u001d\u00100\u001a\u00020\u0007*\u00020\u00152\u0006\u00101\u001a\u00020\u00152\u0006\u0010\u001d\u001a\u00020\u0007H\u0080\b\u001a\u0015\u00102\u001a\u00020\u0007*\u00020\u00152\u0006\u00101\u001a\u000203H\u0080\b\u001a\u0014\u00104\u001a\u00020\u0017*\u00020\u00152\u0006\u00105\u001a\u00020\u0017H\u0000\u001a\r\u00106\u001a\u00020$*\u00020\u0015H\u0080\b\u001a\r\u00107\u001a\u00020\u0001*\u00020\u0015H\u0080\b\u001a\u0015\u00107\u001a\u00020\u0001*\u00020\u00152\u0006\u0010\u001d\u001a\u00020\u0007H\u0080\b\u001a\r\u00108\u001a\u00020+*\u00020\u0015H\u0080\b\u001a\u0015\u00108\u001a\u00020+*\u00020\u00152\u0006\u0010\u001d\u001a\u00020\u0007H\u0080\b\u001a\r\u00109\u001a\u00020\u0007*\u00020\u0015H\u0080\b\u001a\u0015\u0010:\u001a\u00020\u0014*\u00020\u00152\u0006\u00101\u001a\u00020\u0001H\u0080\b\u001a\u001d\u0010:\u001a\u00020\u0014*\u00020\u00152\u0006\u00101\u001a\u00020\u00152\u0006\u0010\u001d\u001a\u00020\u0007H\u0080\b\u001a\r\u0010;\u001a\u00020\u0007*\u00020\u0015H\u0080\b\u001a\r\u0010<\u001a\u00020\n*\u00020\u0015H\u0080\b\u001a\r\u0010=\u001a\u00020\u0007*\u00020\u0015H\u0080\b\u001a\r\u0010>\u001a\u00020?*\u00020\u0015H\u0080\b\u001a\u0014\u0010@\u001a\u00020\u0017*\u00020\u00152\u0006\u00105\u001a\u00020\u0017H\u0000\u001a\u0015\u0010A\u001a\u00020B*\u00020\u00152\u0006\u0010\u001d\u001a\u00020\u0007H\u0080\b\u001a\r\u0010C\u001a\u00020\n*\u00020\u0015H\u0080\b\u001a\u000f\u0010D\u001a\u0004\u0018\u00010B*\u00020\u0015H\u0080\b\u001a\u0015\u0010E\u001a\u00020B*\u00020\u00152\u0006\u0010F\u001a\u00020\u0007H\u0080\b\u001a\u0015\u0010G\u001a\u00020\u0007*\u00020\u00172\u0006\u0010H\u001a\u00020\u0007H\u0080\b\u001a\u0015\u0010I\u001a\u00020\n*\u00020\u00172\u0006\u0010\u001c\u001a\u00020\u0007H\u0080\b\u001a\u0015\u0010J\u001a\u00020\n*\u00020\u00152\u0006\u0010K\u001a\u00020LH\u0080\b\u001a\u0015\u0010M\u001a\u00020\u0014*\u00020\u00152\u0006\u0010\u001d\u001a\u00020\u0007H\u0080\b\u001a\r\u0010N\u001a\u00020+*\u00020\u0015H\u0080\b\u001a\u0015\u0010N\u001a\u00020+*\u00020\u00152\u0006\u0010\u001d\u001a\u00020\nH\u0080\b\u001a\u0015\u0010O\u001a\u00020\u000e*\u00020\u00152\u0006\u0010P\u001a\u00020\nH\u0080\b\u001a\u0015\u0010Q\u001a\u00020\u0015*\u00020\u00152\u0006\u0010R\u001a\u00020\u0001H\u0080\b\u001a%\u0010Q\u001a\u00020\u0015*\u00020\u00152\u0006\u0010R\u001a\u00020\u00012\u0006\u0010\u001c\u001a\u00020\n2\u0006\u0010\u001d\u001a\u00020\nH\u0080\b\u001a\u001d\u0010Q\u001a\u00020\u0014*\u00020\u00152\u0006\u0010R\u001a\u00020\u00152\u0006\u0010\u001d\u001a\u00020\u0007H\u0080\b\u001a)\u0010Q\u001a\u00020\u0015*\u00020\u00152\u0006\u0010S\u001a\u00020+2\b\b\u0002\u0010\u001c\u001a\u00020\n2\b\b\u0002\u0010\u001d\u001a\u00020\nH\u0080\b\u001a\u001d\u0010Q\u001a\u00020\u0015*\u00020\u00152\u0006\u0010R\u001a\u00020T2\u0006\u0010\u001d\u001a\u00020\u0007H\u0080\b\u001a\u0015\u0010U\u001a\u00020\u0007*\u00020\u00152\u0006\u0010R\u001a\u00020TH\u0080\b\u001a\u0015\u0010V\u001a\u00020\u0015*\u00020\u00152\u0006\u0010(\u001a\u00020\nH\u0080\b\u001a\u0015\u0010W\u001a\u00020\u0015*\u00020\u00152\u0006\u0010X\u001a\u00020\u0007H\u0080\b\u001a\u0015\u0010Y\u001a\u00020\u0015*\u00020\u00152\u0006\u0010X\u001a\u00020\u0007H\u0080\b\u001a\u0015\u0010Z\u001a\u00020\u0015*\u00020\u00152\u0006\u0010[\u001a\u00020\nH\u0080\b\u001a\u0015\u0010\\\u001a\u00020\u0015*\u00020\u00152\u0006\u0010X\u001a\u00020\u0007H\u0080\b\u001a\u0015\u0010]\u001a\u00020\u0015*\u00020\u00152\u0006\u0010^\u001a\u00020\nH\u0080\b\u001a%\u0010_\u001a\u00020\u0015*\u00020\u00152\u0006\u0010`\u001a\u00020B2\u0006\u0010a\u001a\u00020\n2\u0006\u0010b\u001a\u00020\nH\u0080\b\u001a\u0015\u0010c\u001a\u00020\u0015*\u00020\u00152\u0006\u0010d\u001a\u00020\nH\u0080\b\u001a\u0014\u0010e\u001a\u00020B*\u00020\u00152\u0006\u0010f\u001a\u00020\u0007H\u0000\u001a?\u0010g\u001a\u0002Hh\"\u0004\b\u0000\u0010h*\u00020\u00152\u0006\u0010)\u001a\u00020\u00072\u001a\u0010i\u001a\u0016\u0012\u0006\u0012\u0004\u0018\u00010\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u0002Hh0jH\u0080\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010k\u001a\u001e\u0010l\u001a\u00020\n*\u00020\u00152\u0006\u0010K\u001a\u00020L2\b\b\u0002\u0010m\u001a\u00020\fH\u0000\"\u001c\u0010\u0000\u001a\u00020\u00018\u0000X\u0081\u0004\u00a2\u0006\u000e\n\u0000\u0012\u0004\b\u0002\u0010\u0003\u001a\u0004\b\u0004\u0010\u0005\"\u000e\u0010\u0006\u001a\u00020\u0007X\u0080T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\b\u001a\u00020\u0007X\u0080T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\t\u001a\u00020\nX\u0080T\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0007\n\u0005\b\u009920\u0001\u00a8\u0006n"}, d2={"HEX_DIGIT_BYTES", "", "getHEX_DIGIT_BYTES$annotations", "()V", "getHEX_DIGIT_BYTES", "()[B", "OVERFLOW_DIGIT_START", "", "OVERFLOW_ZONE", "SEGMENTING_THRESHOLD", "", "rangeEquals", "", "segment", "Lokio/Segment;", "segmentPos", "bytes", "bytesOffset", "bytesLimit", "commonClear", "", "Lokio/Buffer;", "commonClose", "Lokio/Buffer$UnsafeCursor;", "commonCompleteSegmentByteCount", "commonCopy", "commonCopyTo", "out", "offset", "byteCount", "commonEquals", "other", "", "commonExpandBuffer", "minByteCount", "commonGet", "", "pos", "commonHashCode", "commonIndexOf", "b", "fromIndex", "toIndex", "Lokio/ByteString;", "commonIndexOfElement", "targetBytes", "commonNext", "commonRangeEquals", "commonRead", "sink", "commonReadAll", "Lokio/Sink;", "commonReadAndWriteUnsafe", "unsafeCursor", "commonReadByte", "commonReadByteArray", "commonReadByteString", "commonReadDecimalLong", "commonReadFully", "commonReadHexadecimalUnsignedLong", "commonReadInt", "commonReadLong", "commonReadShort", "", "commonReadUnsafe", "commonReadUtf8", "", "commonReadUtf8CodePoint", "commonReadUtf8Line", "commonReadUtf8LineStrict", "limit", "commonResizeBuffer", "newSize", "commonSeek", "commonSelect", "options", "Lokio/Options;", "commonSkip", "commonSnapshot", "commonWritableSegment", "minimumCapacity", "commonWrite", "source", "byteString", "Lokio/Source;", "commonWriteAll", "commonWriteByte", "commonWriteDecimalLong", "v", "commonWriteHexadecimalUnsignedLong", "commonWriteInt", "i", "commonWriteLong", "commonWriteShort", "s", "commonWriteUtf8", "string", "beginIndex", "endIndex", "commonWriteUtf8CodePoint", "codePoint", "readUtf8Line", "newline", "seek", "T", "lambda", "Lkotlin/Function2;", "(Lokio/Buffer;JLkotlin/jvm/functions/Function2;)Ljava/lang/Object;", "selectPrefix", "selectTruncated", "okio"})
@JvmName(name="-Buffer")
@SourceDebugExtension(value={"SMAP\nBuffer.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Buffer.kt\nokio/internal/-Buffer\n+ 2 Util.kt\nokio/-SegmentedByteString\n+ 3 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,1730:1\n112#1,20:1753\n112#1,20:1786\n112#1:1806\n114#1,18:1808\n112#1,20:1826\n74#2:1731\n74#2:1732\n74#2:1733\n74#2:1734\n74#2:1735\n74#2:1736\n74#2:1737\n74#2:1738\n74#2:1739\n74#2:1740\n74#2:1741\n74#2:1742\n83#2:1743\n83#2:1744\n77#2:1745\n77#2:1746\n77#2:1747\n77#2:1748\n77#2:1749\n77#2:1750\n77#2:1751\n77#2:1752\n86#2:1773\n89#2:1775\n74#2:1776\n74#2:1777\n74#2:1778\n74#2:1779\n74#2:1780\n74#2:1781\n74#2:1782\n74#2:1783\n74#2:1784\n74#2:1785\n89#2:1807\n86#2:1846\n1#3:1774\n*S KotlinDebug\n*F\n+ 1 Buffer.kt\nokio/internal/-Buffer\n*L\n415#1:1753,20\n1292#1:1786,20\n1323#1:1806\n1323#1:1808,18\n1357#1:1826,20\n178#1:1731\n202#1:1732\n321#1:1733\n326#1:1734\n349#1:1735\n350#1:1736\n351#1:1737\n352#1:1738\n358#1:1739\n359#1:1740\n360#1:1741\n361#1:1742\n385#1:1743\n386#1:1744\n392#1:1745\n393#1:1746\n394#1:1747\n395#1:1748\n396#1:1749\n397#1:1750\n398#1:1751\n399#1:1752\n427#1:1773\n888#1:1775\n906#1:1776\n908#1:1777\n912#1:1778\n914#1:1779\n918#1:1780\n920#1:1781\n924#1:1782\n926#1:1783\n946#1:1784\n949#1:1785\n1336#1:1807\n1676#1:1846\n*E\n"})
public final class -Buffer {
    @NotNull
    private static final byte[] HEX_DIGIT_BYTES = _JvmPlatformKt.asUtf8ToByteArray("0123456789abcdef");
    public static final int SEGMENTING_THRESHOLD = 4096;
    public static final long OVERFLOW_ZONE = -922337203685477580L;
    public static final long OVERFLOW_DIGIT_START = -7L;

    @NotNull
    public static final byte[] getHEX_DIGIT_BYTES() {
        return HEX_DIGIT_BYTES;
    }

    public static /* synthetic */ void getHEX_DIGIT_BYTES$annotations() {
    }

    public static final boolean rangeEquals(@NotNull Segment segment, int n2, @NotNull byte[] byArray, int n3, int n4) {
        Intrinsics.checkNotNullParameter(segment, "");
        Intrinsics.checkNotNullParameter(byArray, "");
        int n5 = segment.limit;
        byte[] byArray2 = segment.data;
        while (n3 < n4) {
            if (n2 == n5) {
                Intrinsics.checkNotNull(segment.next);
                byArray2 = segment.data;
                n2 = segment.pos;
                n5 = segment.limit;
            }
            if (byArray2[n2] != byArray[n3]) {
                return false;
            }
            ++n2;
            ++n3;
        }
        return true;
    }

    @NotNull
    public static final String readUtf8Line(@NotNull Buffer buffer, long l2) {
        Intrinsics.checkNotNullParameter(buffer, "");
        if (l2 > 0L && buffer.getByte(l2 - 1L) == 13) {
            String string = buffer.readUtf8(l2 - 1L);
            buffer.skip(2L);
            return string;
        }
        String string = buffer.readUtf8(l2);
        buffer.skip(1L);
        return string;
    }

    public static final <T> T seek(@NotNull Buffer buffer, long l2, @NotNull Function2<? super Segment, ? super Long, ? extends T> function2) {
        long l3;
        Intrinsics.checkNotNullParameter(buffer, "");
        Intrinsics.checkNotNullParameter(function2, "");
        Segment segment = buffer.head;
        if (segment == null) {
            return function2.invoke(null, -1L);
        }
        Segment segment2 = segment;
        if (buffer.size() - l2 < l2) {
            long l4;
            for (l4 = buffer.size(); l4 > l2; l4 -= (long)(segment2.limit - segment2.pos)) {
                Intrinsics.checkNotNull(segment2.prev);
            }
            return function2.invoke(segment2, l4);
        }
        long l5 = 0L;
        while ((l3 = l5 + (long)(segment2.limit - segment2.pos)) <= l2) {
            Intrinsics.checkNotNull(segment2.next);
            l5 = l3;
        }
        return function2.invoke(segment2, l5);
    }

    public static final int selectPrefix(@NotNull Buffer object, @NotNull Options object2, boolean bl) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(object2, "");
        Segment segment = ((Buffer)object).head;
        if (segment == null) {
            if (bl) {
                return -2;
            }
            return -1;
        }
        object = segment;
        Segment segment2 = segment;
        byte[] byArray = ((Segment)object).data;
        int n2 = ((Segment)object).pos;
        int n3 = ((Segment)object).limit;
        object2 = ((Options)object2).getTrie$okio();
        Object object3 = 0;
        Object object4 = -1;
        block0: while (true) {
            int n4;
            Object object5;
            Object object6 = object2[object3++];
            if ((object5 = object2[object3++]) != -1) {
                object4 = object5;
            }
            if (segment2 == null) break;
            if (object6 < 0) {
                object6 = -1 * object6;
                object5 = object3 + object6;
                do {
                    byte by = byArray[n2++];
                    object6 = by;
                    n4 = by & 0xFF;
                    if (n4 != object2[object3++]) {
                        return object4;
                    }
                    object6 = object3 == object5;
                    if (n2 != n3) continue;
                    Segment segment3 = segment2;
                    Intrinsics.checkNotNull(segment3);
                    Intrinsics.checkNotNull(segment3.next);
                    n2 = segment2.pos;
                    byArray = segment2.data;
                    n3 = segment2.limit;
                    if (segment2 != object) continue;
                    if (object6 == false) break block0;
                    segment2 = null;
                } while (object6 == false);
                object3 = object2[object3];
            } else {
                n4 = byArray[n2++];
                object5 = n4 & 0xFF;
                n4 = object3 + object6;
                while (true) {
                    if (object3 == n4) {
                        return object4;
                    }
                    if (object5 == object2[object3]) break;
                    ++object3;
                }
                object3 = object2[object3 + object6];
                if (n2 == n3) {
                    Intrinsics.checkNotNull(segment2.next);
                    n2 = segment2.pos;
                    byArray = segment2.data;
                    n3 = segment2.limit;
                    if (segment2 == object) {
                        segment2 = null;
                    }
                }
            }
            if (object3 >= 0) {
                return object3;
            }
            object3 = -object3;
        }
        if (bl) {
            return -2;
        }
        return object4;
    }

    public static /* synthetic */ int selectPrefix$default(Buffer buffer, Options options, boolean bl, int n2, Object object) {
        if ((n2 & 2) != 0) {
            bl = false;
        }
        return -Buffer.selectPrefix(buffer, options, bl);
    }

    @NotNull
    public static final Buffer commonCopyTo(@NotNull Buffer buffer, @NotNull Buffer buffer2, long l2, long l3) {
        Intrinsics.checkNotNullParameter(buffer, "");
        Intrinsics.checkNotNullParameter(buffer2, "");
        long l4 = l2;
        long l5 = l3;
        -SegmentedByteString.checkOffsetAndCount(buffer.size(), l2, l3);
        if (l3 == 0L) {
            return buffer;
        }
        Buffer buffer3 = buffer2;
        buffer3.setSize$okio(buffer3.size() + l3);
        Segment segment = buffer.head;
        while (true) {
            Segment segment2 = segment;
            Intrinsics.checkNotNull(segment2);
            if (l4 < (long)(segment2.limit - segment.pos)) break;
            l4 -= (long)(segment.limit - segment.pos);
            segment = segment.next;
        }
        while (l5 > 0L) {
            Segment segment3 = segment;
            Intrinsics.checkNotNull(segment3);
            Segment segment4 = segment3.sharedCopy();
            segment3.sharedCopy().pos = segment4.pos + (int)l4;
            segment4.limit = Math.min(segment4.pos + (int)l5, segment4.limit);
            if (buffer2.head == null) {
                buffer2.head = segment4.next = (segment4.prev = segment4);
            } else {
                Segment segment5 = buffer2.head;
                Intrinsics.checkNotNull(segment5);
                Segment segment6 = segment5.prev;
                Intrinsics.checkNotNull(segment6);
                segment6.push(segment4);
            }
            l5 -= (long)(segment4.limit - segment4.pos);
            l4 = 0L;
            segment = segment.next;
        }
        return buffer;
    }

    public static final long commonCompleteSegmentByteCount(@NotNull Buffer object) {
        Intrinsics.checkNotNullParameter(object, "");
        long l2 = ((Buffer)object).size();
        if (l2 == 0L) {
            return 0L;
        }
        Segment segment = ((Buffer)object).head;
        Intrinsics.checkNotNull(segment);
        Segment segment2 = segment.prev;
        Intrinsics.checkNotNull(segment2);
        object = segment2;
        if (segment2.limit < 8192 && ((Segment)object).owner) {
            l2 -= (long)(((Segment)object).limit - ((Segment)object).pos);
        }
        return l2;
    }

    public static final byte commonReadByte(@NotNull Buffer buffer) {
        Intrinsics.checkNotNullParameter(buffer, "");
        if (buffer.size() == 0L) {
            throw new EOFException();
        }
        Segment segment = buffer.head;
        Intrinsics.checkNotNull(segment);
        Segment segment2 = segment;
        int n2 = segment.pos;
        int n3 = segment2.limit;
        byte[] byArray = segment2.data;
        byte by = segment2.data[n2++];
        Buffer buffer2 = buffer;
        buffer2.setSize$okio(buffer2.size() - 1L);
        if (n2 == n3) {
            buffer.head = segment2.pop();
            SegmentPool.recycle(segment2);
        } else {
            segment2.pos = n2;
        }
        return by;
    }

    public static final short commonReadShort(@NotNull Buffer buffer) {
        Intrinsics.checkNotNullParameter(buffer, "");
        if (buffer.size() < 2L) {
            throw new EOFException();
        }
        Segment segment = buffer.head;
        Intrinsics.checkNotNull(segment);
        Segment segment2 = segment;
        int n2 = segment.pos;
        int n3 = segment2.limit;
        if (n3 - n2 < 2) {
            int n4 = buffer.readByte();
            int n5 = n4;
            n5 = buffer.readByte();
            n5 = (n4 & 0xFF) << 8 | n5 & 0xFF;
            return (short)n5;
        }
        byte[] byArray = segment2.data;
        byte by = segment2.data[n2++];
        byte by2 = by;
        by2 = byArray[n2++];
        int n6 = (by & 0xFF) << 8 | by2 & 0xFF;
        Buffer buffer2 = buffer;
        buffer2.setSize$okio(buffer2.size() - 2L);
        if (n2 == n3) {
            buffer.head = segment2.pop();
            SegmentPool.recycle(segment2);
        } else {
            segment2.pos = n2;
        }
        return (short)n6;
    }

    public static final int commonReadInt(@NotNull Buffer buffer) {
        Intrinsics.checkNotNullParameter(buffer, "");
        if (buffer.size() < 4L) {
            throw new EOFException();
        }
        Segment segment = buffer.head;
        Intrinsics.checkNotNull(segment);
        Segment segment2 = segment;
        int n2 = segment.pos;
        int n3 = segment2.limit;
        if ((long)(n3 - n2) < 4L) {
            byte by = buffer.readByte();
            byte by2 = by;
            byte by3 = buffer.readByte();
            by2 = by3;
            byte by4 = buffer.readByte();
            by2 = by4;
            by2 = buffer.readByte();
            return (by & 0xFF) << 24 | (by3 & 0xFF) << 16 | (by4 & 0xFF) << 8 | by2 & 0xFF;
        }
        byte[] byArray = segment2.data;
        byte by = segment2.data[n2++];
        byte by5 = by;
        byte by6 = byArray[n2++];
        by5 = by6;
        byte by7 = byArray[n2++];
        by5 = by7;
        by5 = byArray[n2++];
        int n4 = (by & 0xFF) << 24 | (by6 & 0xFF) << 16 | (by7 & 0xFF) << 8 | by5 & 0xFF;
        Buffer buffer2 = buffer;
        buffer2.setSize$okio(buffer2.size() - 4L);
        if (n2 == n3) {
            buffer.head = segment2.pop();
            SegmentPool.recycle(segment2);
        } else {
            segment2.pos = n2;
        }
        return n4;
    }

    public static final long commonReadLong(@NotNull Buffer buffer) {
        Intrinsics.checkNotNullParameter(buffer, "");
        if (buffer.size() < 8L) {
            throw new EOFException();
        }
        Segment segment = buffer.head;
        Intrinsics.checkNotNull(segment);
        Segment segment2 = segment;
        int n2 = segment.pos;
        int n3 = segment2.limit;
        if ((long)(n3 - n2) < 8L) {
            int n4 = buffer.readInt();
            int n5 = n4;
            n5 = buffer.readInt();
            return ((long)n4 & 0xFFFFFFFFL) << 32 | (long)n5 & 0xFFFFFFFFL;
        }
        byte[] byArray = segment2.data;
        byte by = segment2.data[n2++];
        byte by2 = by;
        byte by3 = byArray[n2++];
        by2 = by3;
        byte by4 = byArray[n2++];
        by2 = by4;
        byte by5 = byArray[n2++];
        by2 = by5;
        byte by6 = byArray[n2++];
        by2 = by6;
        byte by7 = byArray[n2++];
        by2 = by7;
        byte by8 = byArray[n2++];
        by2 = by8;
        by2 = byArray[n2++];
        long l2 = ((long)by & 0xFFL) << 56 | ((long)by3 & 0xFFL) << 48 | ((long)by4 & 0xFFL) << 40 | ((long)by5 & 0xFFL) << 32 | ((long)by6 & 0xFFL) << 24 | ((long)by7 & 0xFFL) << 16 | ((long)by8 & 0xFFL) << 8 | (long)by2 & 0xFFL;
        Buffer buffer2 = buffer;
        buffer2.setSize$okio(buffer2.size() - 8L);
        if (n2 == n3) {
            buffer.head = segment2.pop();
            SegmentPool.recycle(segment2);
        } else {
            segment2.pos = n2;
        }
        return l2;
    }

    public static final byte commonGet(@NotNull Buffer object, long l2) {
        long l3;
        Intrinsics.checkNotNullParameter(object, "");
        -SegmentedByteString.checkOffsetAndCount(((Buffer)object).size(), l2, 1L);
        Segment segment = ((Buffer)object).head;
        if (segment == null) {
            Intrinsics.checkNotNull(null);
            return null.data[(int)((long)null.pos + l2 - -1L)];
        }
        Segment segment2 = segment;
        if (((Buffer)object).size() - l2 < l2) {
            long l4;
            for (l4 = ((Buffer)object).size(); l4 > l2; l4 -= (long)(segment2.limit - segment2.pos)) {
                Intrinsics.checkNotNull(segment2.prev);
            }
            long l5 = l4;
            object = segment2;
            Intrinsics.checkNotNull(object);
            return ((Segment)object).data[(int)((long)((Segment)object).pos + l2 - l5)];
        }
        long l6 = 0L;
        while ((l3 = l6 + (long)(segment2.limit - segment2.pos)) <= l2) {
            Intrinsics.checkNotNull(segment2.next);
            l6 = l3;
        }
        long l7 = l6;
        object = segment2;
        Intrinsics.checkNotNull(object);
        return ((Segment)object).data[(int)((long)((Segment)object).pos + l2 - l7)];
    }

    public static final void commonClear(@NotNull Buffer buffer) {
        Intrinsics.checkNotNullParameter(buffer, "");
        Buffer buffer2 = buffer;
        buffer2.skip(buffer2.size());
    }

    public static final void commonSkip(@NotNull Buffer buffer, long l2) {
        Intrinsics.checkNotNullParameter(buffer, "");
        long l3 = l2;
        while (l3 > 0L) {
            Segment segment;
            if (buffer.head == null) {
                throw new EOFException();
            }
            int n2 = segment.limit - segment.pos;
            n2 = (int)Math.min(l3, (long)n2);
            Buffer buffer2 = buffer;
            buffer2.setSize$okio(buffer2.size() - (long)n2);
            l3 -= (long)n2;
            segment.pos += n2;
            if (segment.pos != segment.limit) continue;
            buffer.head = segment.pop();
            SegmentPool.recycle(segment);
        }
    }

    @NotNull
    public static final Buffer commonWrite(@NotNull Buffer buffer, @NotNull ByteString byteString, int n2, int n3) {
        Intrinsics.checkNotNullParameter(buffer, "");
        Intrinsics.checkNotNullParameter(byteString, "");
        byteString.write$okio(buffer, n2, n3);
        return buffer;
    }

    public static /* synthetic */ Buffer commonWrite$default(Buffer buffer, ByteString byteString, int n2, int n3, int n4, Object object) {
        if ((n4 & 2) != 0) {
            n2 = 0;
        }
        if ((n4 & 4) != 0) {
            n3 = byteString.size();
        }
        Intrinsics.checkNotNullParameter(buffer, "");
        Intrinsics.checkNotNullParameter(byteString, "");
        byteString.write$okio(buffer, n2, n3);
        return buffer;
    }

    @NotNull
    public static final Buffer commonWriteDecimalLong(@NotNull Buffer buffer, long l2) {
        int n2;
        Intrinsics.checkNotNullParameter(buffer, "");
        long l3 = l2;
        if (l2 == 0L) {
            return buffer.writeByte(48);
        }
        boolean bl = false;
        if (l2 < 0L) {
            l3 = -l2;
            if (l3 < 0L) {
                return buffer.writeUtf8("-9223372036854775808");
            }
            bl = true;
        }
        int n3 = l3 < 100000000L ? (l3 < 10000L ? (l3 < 100L ? (l3 < 10L ? 1 : 2) : (l3 < 1000L ? 3 : 4)) : (l3 < 1000000L ? (l3 < 100000L ? 5 : 6) : (l3 < 10000000L ? 7 : 8))) : (l3 < 1000000000000L ? (l3 < 10000000000L ? (l3 < 1000000000L ? 9 : 10) : (l3 < 100000000000L ? 11 : 12)) : (l3 < 1000000000000000L ? (l3 < 10000000000000L ? 13 : (l3 < 100000000000000L ? 14 : 15)) : (l3 < 100000000000000000L ? (l3 < 10000000000000000L ? 16 : 17) : (n2 = l3 < 1000000000000000000L ? 18 : 19))));
        if (bl) {
            ++n2;
        }
        Segment segment = buffer.writableSegment$okio(n2);
        byte[] byArray = segment.data;
        int n4 = segment.limit + n2;
        while (l3 != 0L) {
            int n5 = (int)(l3 % 10L);
            byArray[--n4] = -Buffer.getHEX_DIGIT_BYTES()[n5];
            l3 /= 10L;
        }
        if (bl) {
            byArray[--n4] = 45;
        }
        segment.limit += n2;
        Buffer buffer2 = buffer;
        buffer2.setSize$okio(buffer2.size() + (long)n2);
        return buffer;
    }

    @NotNull
    public static final Buffer commonWriteHexadecimalUnsignedLong(@NotNull Buffer buffer, long l2) {
        Intrinsics.checkNotNullParameter(buffer, "");
        long l3 = l2;
        if (l2 == 0L) {
            return buffer.writeByte(48);
        }
        long l4 = l3;
        long l5 = l4 | l4 >>> 1;
        l5 |= l5 >>> 2;
        l5 |= l5 >>> 4;
        l5 |= l5 >>> 8;
        l5 |= l5 >>> 16;
        l5 |= l5 >>> 32;
        l5 -= l5 >>> 1 & 0x5555555555555555L;
        l5 = (l5 >>> 2 & 0x3333333333333333L) + (l5 & 0x3333333333333333L);
        l5 = (l5 >>> 4) + l5 & 0xF0F0F0F0F0F0F0FL;
        l5 += l5 >>> 8;
        l5 += l5 >>> 16;
        l5 = (l5 & 0x3FL) + (l5 >>> 32 & 0x3FL);
        int n2 = (int)((l5 + 3L) / 4L);
        Segment segment = buffer.writableSegment$okio(n2);
        byte[] byArray = segment.data;
        int n3 = segment.limit;
        for (int i2 = segment.limit + n2 - 1; i2 >= n3; --i2) {
            byArray[i2] = -Buffer.getHEX_DIGIT_BYTES()[(int)(l3 & 0xFL)];
            l3 >>>= 4;
        }
        segment.limit += n2;
        Buffer buffer2 = buffer;
        buffer2.setSize$okio(buffer2.size() + (long)n2);
        return buffer;
    }

    @NotNull
    public static final Segment commonWritableSegment(@NotNull Buffer object, int n2) {
        Intrinsics.checkNotNullParameter(object, "");
        if (!(n2 > 0 && n2 <= 8192)) {
            object = "unexpected capacity";
            throw new IllegalArgumentException(object.toString());
        }
        if (((Buffer)object).head == null) {
            Segment segment;
            ((Buffer)object).head = segment = SegmentPool.take();
            segment.prev = segment;
            segment.next = segment;
            return segment;
        }
        Segment segment = ((Buffer)object).head;
        Intrinsics.checkNotNull(segment);
        Segment segment2 = segment.prev;
        Intrinsics.checkNotNull(segment2);
        if (segment2.limit + n2 > 8192 || !segment2.owner) {
            segment2 = segment2.push(SegmentPool.take());
        }
        return segment2;
    }

    @NotNull
    public static final Buffer commonWrite(@NotNull Buffer buffer, @NotNull byte[] byArray) {
        Intrinsics.checkNotNullParameter(buffer, "");
        Intrinsics.checkNotNullParameter(byArray, "");
        return buffer.write(byArray, 0, byArray.length);
    }

    @NotNull
    public static final Buffer commonWrite(@NotNull Buffer buffer, @NotNull byte[] byArray, int n2, int n3) {
        Intrinsics.checkNotNullParameter(buffer, "");
        Intrinsics.checkNotNullParameter(byArray, "");
        int n4 = n2;
        -SegmentedByteString.checkOffsetAndCount(byArray.length, n2, n3);
        n2 += n3;
        while (n4 < n2) {
            Segment segment = buffer.writableSegment$okio(1);
            int n5 = Math.min(n2 - n4, 8192 - segment.limit);
            int n6 = n4;
            ArraysKt.copyInto(byArray, segment.data, segment.limit, n6, n6 + n5);
            n4 += n5;
            segment.limit += n5;
        }
        Buffer buffer2 = buffer;
        buffer2.setSize$okio(buffer2.size() + (long)n3);
        return buffer;
    }

    @NotNull
    public static final byte[] commonReadByteArray(@NotNull Buffer buffer) {
        Intrinsics.checkNotNullParameter(buffer, "");
        Buffer buffer2 = buffer;
        return buffer2.readByteArray(buffer2.size());
    }

    @NotNull
    public static final byte[] commonReadByteArray(@NotNull Buffer object, long l2) {
        Intrinsics.checkNotNullParameter(object, "");
        if (!(l2 >= 0L && l2 <= Integer.MAX_VALUE)) {
            object = "byteCount: " + l2;
            throw new IllegalArgumentException(object.toString());
        }
        if (((Buffer)object).size() < l2) {
            throw new EOFException();
        }
        byte[] byArray = new byte[(int)l2];
        ((Buffer)object).readFully(byArray);
        return byArray;
    }

    public static final int commonRead(@NotNull Buffer buffer, @NotNull byte[] byArray) {
        Intrinsics.checkNotNullParameter(buffer, "");
        Intrinsics.checkNotNullParameter(byArray, "");
        return buffer.read(byArray, 0, byArray.length);
    }

    public static final void commonReadFully(@NotNull Buffer buffer, @NotNull byte[] byArray) {
        int n2;
        Intrinsics.checkNotNullParameter(buffer, "");
        Intrinsics.checkNotNullParameter(byArray, "");
        for (int i2 = 0; i2 < byArray.length; i2 += n2) {
            n2 = buffer.read(byArray, i2, byArray.length - i2);
            if (n2 != -1) continue;
            throw new EOFException();
        }
    }

    public static final int commonRead(@NotNull Buffer buffer, @NotNull byte[] byArray, int n2, int n3) {
        Intrinsics.checkNotNullParameter(buffer, "");
        Intrinsics.checkNotNullParameter(byArray, "");
        -SegmentedByteString.checkOffsetAndCount(byArray.length, n2, n3);
        Segment segment = buffer.head;
        if (segment == null) {
            return -1;
        }
        Segment segment2 = segment;
        n3 = Math.min(n3, segment2.limit - segment2.pos);
        ArraysKt.copyInto(segment2.data, byArray, n2, segment2.pos, segment2.pos + n3);
        segment2.pos += n3;
        Buffer buffer2 = buffer;
        buffer2.setSize$okio(buffer2.size() - (long)n3);
        if (segment2.pos == segment2.limit) {
            buffer.head = segment2.pop();
            SegmentPool.recycle(segment2);
        }
        return n3;
    }

    public static final long commonReadDecimalLong(@NotNull Buffer buffer) {
        int n2;
        Object object;
        Intrinsics.checkNotNullParameter(buffer, "");
        if (buffer.size() == 0L) {
            throw new EOFException();
        }
        long l2 = 0L;
        int n3 = 0;
        boolean bl = false;
        boolean bl2 = false;
        long l3 = -7L;
        do {
            Segment segment;
            Intrinsics.checkNotNull(buffer.head);
            object = segment.data;
            int n4 = segment.pos;
            int n5 = segment.limit;
            while (n4 < n5) {
                byte by = object[n4];
                if (by >= 48 && by <= 57) {
                    int n6 = 48 - by;
                    if (l2 < -922337203685477580L || l2 == -922337203685477580L && (long)n6 < l3) {
                        buffer = new Buffer().writeDecimalLong(l2).writeByte(by);
                        if (!bl) {
                            buffer.readByte();
                        }
                        throw new NumberFormatException("Number too large: " + buffer.readUtf8());
                    }
                    l2 *= 10L;
                    l2 += (long)n6;
                } else if (by == 45 && n3 == 0) {
                    bl = true;
                    --l3;
                } else {
                    bl2 = true;
                    break;
                }
                ++n4;
                ++n3;
            }
            if (n4 == n5) {
                buffer.head = segment.pop();
                SegmentPool.recycle(segment);
                continue;
            }
            segment.pos = n4;
        } while (!bl2 && buffer.head != null);
        Buffer buffer2 = buffer;
        buffer2.setSize$okio(buffer2.size() - (long)n3);
        int n7 = n2 = bl ? 2 : 1;
        if (n3 < n2) {
            if (buffer.size() == 0L) {
                throw new EOFException();
            }
            object = bl ? "Expected a digit" : "Expected a digit or '-'";
            throw new NumberFormatException((String)object + " but was 0x" + -SegmentedByteString.toHexString(buffer.getByte(0L)));
        }
        if (bl) {
            return l2;
        }
        return -l2;
    }

    public static final long commonReadHexadecimalUnsignedLong(@NotNull Buffer buffer) {
        Intrinsics.checkNotNullParameter(buffer, "");
        if (buffer.size() == 0L) {
            throw new EOFException();
        }
        long l2 = 0L;
        int n2 = 0;
        boolean bl = false;
        do {
            Segment segment;
            Intrinsics.checkNotNull(buffer.head);
            byte[] byArray = segment.data;
            int n3 = segment.pos;
            int n4 = segment.limit;
            while (n3 < n4) {
                int n5;
                byte by = byArray[n3];
                if (by >= 48 && by <= 57) {
                    n5 = by - 48;
                } else if (by >= 97 && by <= 102) {
                    n5 = by - 97 + 10;
                } else if (by >= 65 && by <= 70) {
                    n5 = by - 65 + 10;
                } else {
                    if (n2 == 0) {
                        throw new NumberFormatException("Expected leading [0-9a-fA-F] character but was 0x" + -SegmentedByteString.toHexString(by));
                    }
                    bl = true;
                    break;
                }
                if ((l2 & 0xF000000000000000L) != 0L) {
                    buffer = new Buffer().writeHexadecimalUnsignedLong(l2).writeByte(by);
                    throw new NumberFormatException("Number too large: " + buffer.readUtf8());
                }
                l2 <<= 4;
                l2 |= (long)n5;
                ++n3;
                ++n2;
            }
            if (n3 == n4) {
                buffer.head = segment.pop();
                SegmentPool.recycle(segment);
                continue;
            }
            segment.pos = n3;
        } while (!bl && buffer.head != null);
        Buffer buffer2 = buffer;
        buffer2.setSize$okio(buffer2.size() - (long)n2);
        return l2;
    }

    @NotNull
    public static final ByteString commonReadByteString(@NotNull Buffer buffer) {
        Intrinsics.checkNotNullParameter(buffer, "");
        Buffer buffer2 = buffer;
        return buffer2.readByteString(buffer2.size());
    }

    @NotNull
    public static final ByteString commonReadByteString(@NotNull Buffer object, long l2) {
        Intrinsics.checkNotNullParameter(object, "");
        if (!(l2 >= 0L && l2 <= Integer.MAX_VALUE)) {
            object = "byteCount: " + l2;
            throw new IllegalArgumentException(object.toString());
        }
        if (((Buffer)object).size() < l2) {
            throw new EOFException();
        }
        if (l2 >= 4096L) {
            ByteString byteString = ((Buffer)object).snapshot((int)l2);
            ((Buffer)object).skip(l2);
            return byteString;
        }
        return new ByteString(((Buffer)object).readByteArray(l2));
    }

    public static final int commonSelect(@NotNull Buffer buffer, @NotNull Options options) {
        Intrinsics.checkNotNullParameter(buffer, "");
        Intrinsics.checkNotNullParameter(options, "");
        int n2 = -Buffer.selectPrefix$default(buffer, options, false, 2, null);
        if (n2 == -1) {
            return -1;
        }
        int n3 = options.getByteStrings$okio()[n2].size();
        buffer.skip(n3);
        return n2;
    }

    public static final void commonReadFully(@NotNull Buffer buffer, @NotNull Buffer buffer2, long l2) {
        Intrinsics.checkNotNullParameter(buffer, "");
        Intrinsics.checkNotNullParameter(buffer2, "");
        if (buffer.size() < l2) {
            Buffer buffer3 = buffer;
            buffer2.write(buffer3, buffer3.size());
            throw new EOFException();
        }
        buffer2.write(buffer, l2);
    }

    public static final long commonReadAll(@NotNull Buffer buffer, @NotNull Sink sink2) {
        Intrinsics.checkNotNullParameter(buffer, "");
        Intrinsics.checkNotNullParameter(sink2, "");
        long l2 = buffer.size();
        if (l2 > 0L) {
            sink2.write(buffer, l2);
        }
        return l2;
    }

    @NotNull
    public static final String commonReadUtf8(@NotNull Buffer buffer, long l2) {
        Intrinsics.checkNotNullParameter(buffer, "");
        if (!(l2 >= 0L && l2 <= Integer.MAX_VALUE)) {
            String string = "byteCount: " + l2;
            throw new IllegalArgumentException(string.toString());
        }
        if (buffer.size() < l2) {
            throw new EOFException();
        }
        if (l2 == 0L) {
            return "";
        }
        Segment segment = buffer.head;
        Intrinsics.checkNotNull(segment);
        Segment segment2 = segment;
        if ((long)segment.pos + l2 > (long)segment2.limit) {
            return _Utf8Kt.commonToUtf8String$default(buffer.readByteArray(l2), 0, 0, 3, null);
        }
        String string = _Utf8Kt.commonToUtf8String(segment2.data, segment2.pos, segment2.pos + (int)l2);
        segment2.pos += (int)l2;
        Buffer buffer2 = buffer;
        buffer2.setSize$okio(buffer2.size() - l2);
        if (segment2.pos == segment2.limit) {
            buffer.head = segment2.pop();
            SegmentPool.recycle(segment2);
        }
        return string;
    }

    @Nullable
    public static final String commonReadUtf8Line(@NotNull Buffer buffer) {
        Intrinsics.checkNotNullParameter(buffer, "");
        long l2 = buffer.indexOf((byte)10);
        if (l2 != -1L) {
            return -Buffer.readUtf8Line(buffer, l2);
        }
        if (buffer.size() != 0L) {
            Buffer buffer2 = buffer;
            return buffer2.readUtf8(buffer2.size());
        }
        return null;
    }

    @NotNull
    public static final String commonReadUtf8LineStrict(@NotNull Buffer buffer, long l2) {
        Intrinsics.checkNotNullParameter(buffer, "");
        if (!(l2 >= 0L)) {
            String string = "limit < 0: " + l2;
            throw new IllegalArgumentException(string.toString());
        }
        long l3 = l2 == Long.MAX_VALUE ? Long.MAX_VALUE : l2 + 1L;
        long l4 = buffer.indexOf((byte)10, 0L, l3);
        if (l4 != -1L) {
            return -Buffer.readUtf8Line(buffer, l4);
        }
        if (l3 < buffer.size() && buffer.getByte(l3 - 1L) == 13 && buffer.getByte(l3) == 10) {
            return -Buffer.readUtf8Line(buffer, l3);
        }
        Buffer buffer2 = new Buffer();
        long l5 = buffer.size();
        buffer.copyTo(buffer2, 0L, Math.min(32L, l5));
        throw new EOFException("\\n not found: limit=" + Math.min(buffer.size(), l2) + " content=" + buffer2.readByteString().hex() + '\u2026');
    }

    public static final int commonReadUtf8CodePoint(@NotNull Buffer buffer) {
        int n2;
        int n3;
        int n4;
        Intrinsics.checkNotNullParameter(buffer, "");
        if (buffer.size() == 0L) {
            throw new EOFException();
        }
        int n5 = buffer.getByte(0L);
        if ((n5 & 0x80) == 0) {
            n4 = n5 & 0x7F;
            n3 = 1;
            n2 = 0;
        } else if ((n5 & 0xE0) == 192) {
            n4 = n5 & 0x1F;
            n3 = 2;
            n2 = 128;
        } else if ((n5 & 0xF0) == 224) {
            n4 = n5 & 0xF;
            n3 = 3;
            n2 = 2048;
        } else if ((n5 & 0xF8) == 240) {
            n4 = n5 & 7;
            n3 = 4;
            n2 = 65536;
        } else {
            buffer.skip(1L);
            return 65533;
        }
        if (buffer.size() < (long)n3) {
            throw new EOFException("size < " + n3 + ": " + buffer.size() + " (to read code point prefixed 0x" + -SegmentedByteString.toHexString((byte)n5) + ')');
        }
        for (n5 = 1; n5 < n3; ++n5) {
            byte by;
            byte by2 = by = buffer.getByte(n5);
            if ((by & 0xC0) == 128) {
                n4 <<= 6;
                by2 = by;
                n4 |= by2 & 0x3F;
                continue;
            }
            buffer.skip(n5);
            return 65533;
        }
        buffer.skip(n3);
        if (n4 > 0x10FFFF) {
            return 65533;
        }
        boolean bl = 55296 <= n4 ? n4 < 57344 : false;
        if (bl) {
            return 65533;
        }
        if (n4 < n2) {
            return 65533;
        }
        return n4;
    }

    @NotNull
    public static final Buffer commonWriteUtf8(@NotNull Buffer buffer, @NotNull String string, int n2, int n3) {
        Intrinsics.checkNotNullParameter(buffer, "");
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
        while (n2 < n3) {
            char c2;
            int n4;
            block12: {
                block11: {
                    n4 = string.charAt(n2);
                    if (n4 < 128) {
                        Segment segment = buffer.writableSegment$okio(1);
                        byte[] byArray = segment.data;
                        int n5 = segment.limit - n2;
                        int n6 = Math.min(n3, 8192 - n5);
                        byArray[n5 + n2++] = (byte)n4;
                        while (n2 < n6 && (n4 = string.charAt(n2)) < 128) {
                            byArray[n5 + n2++] = (byte)n4;
                        }
                        n4 = n2 + n5 - segment.limit;
                        segment.limit += n4;
                        Buffer buffer2 = buffer;
                        buffer2.setSize$okio(buffer2.size() + (long)n4);
                        continue;
                    }
                    if (n4 < 2048) {
                        Segment segment = buffer.writableSegment$okio(2);
                        segment.data[segment.limit] = (byte)(n4 >> 6 | 0xC0);
                        segment.data[segment.limit + 1] = (byte)(n4 & 0x3F | 0x80);
                        segment.limit += 2;
                        Buffer buffer3 = buffer;
                        buffer3.setSize$okio(buffer3.size() + 2L);
                        ++n2;
                        continue;
                    }
                    if (n4 < 55296 || n4 > 57343) {
                        Segment segment = buffer.writableSegment$okio(3);
                        segment.data[segment.limit] = (byte)(n4 >> 12 | 0xE0);
                        segment.data[segment.limit + 1] = (byte)(n4 >> 6 & 0x3F | 0x80);
                        segment.data[segment.limit + 2] = (byte)(n4 & 0x3F | 0x80);
                        segment.limit += 3;
                        Buffer buffer4 = buffer;
                        buffer4.setSize$okio(buffer4.size() + 3L);
                        ++n2;
                        continue;
                    }
                    char c3 = c2 = n2 + 1 < n3 ? string.charAt(n2 + 1) : (char)'\u0000';
                    if (n4 > 56319) break block11;
                    if ('\udc00' <= c2 ? c2 < '\ue000' : false) break block12;
                }
                buffer.writeByte(63);
                ++n2;
                continue;
            }
            int n7 = 65536 + ((n4 & 0x3FF) << 10 | c2 & 0x3FF);
            Segment segment = buffer.writableSegment$okio(4);
            segment.data[segment.limit] = (byte)(n7 >> 18 | 0xF0);
            segment.data[segment.limit + 1] = (byte)(n7 >> 12 & 0x3F | 0x80);
            segment.data[segment.limit + 2] = (byte)(n7 >> 6 & 0x3F | 0x80);
            segment.data[segment.limit + 3] = (byte)(n7 & 0x3F | 0x80);
            segment.limit += 4;
            Buffer buffer5 = buffer;
            buffer5.setSize$okio(buffer5.size() + 4L);
            n2 += 2;
        }
        return buffer;
    }

    @NotNull
    public static final Buffer commonWriteUtf8CodePoint(@NotNull Buffer buffer, int n2) {
        Intrinsics.checkNotNullParameter(buffer, "");
        if (n2 < 128) {
            buffer.writeByte(n2);
        } else if (n2 < 2048) {
            Segment segment = buffer.writableSegment$okio(2);
            segment.data[segment.limit] = (byte)(n2 >> 6 | 0xC0);
            segment.data[segment.limit + 1] = (byte)(n2 & 0x3F | 0x80);
            segment.limit += 2;
            Buffer buffer2 = buffer;
            buffer2.setSize$okio(buffer2.size() + 2L);
        } else {
            boolean bl = 55296 <= n2 ? n2 < 57344 : false;
            if (bl) {
                buffer.writeByte(63);
            } else if (n2 < 65536) {
                Segment segment = buffer.writableSegment$okio(3);
                segment.data[segment.limit] = (byte)(n2 >> 12 | 0xE0);
                segment.data[segment.limit + 1] = (byte)(n2 >> 6 & 0x3F | 0x80);
                segment.data[segment.limit + 2] = (byte)(n2 & 0x3F | 0x80);
                segment.limit += 3;
                Buffer buffer3 = buffer;
                buffer3.setSize$okio(buffer3.size() + 3L);
            } else if (n2 <= 0x10FFFF) {
                Segment segment = buffer.writableSegment$okio(4);
                segment.data[segment.limit] = (byte)(n2 >> 18 | 0xF0);
                segment.data[segment.limit + 1] = (byte)(n2 >> 12 & 0x3F | 0x80);
                segment.data[segment.limit + 2] = (byte)(n2 >> 6 & 0x3F | 0x80);
                segment.data[segment.limit + 3] = (byte)(n2 & 0x3F | 0x80);
                segment.limit += 4;
                Buffer buffer4 = buffer;
                buffer4.setSize$okio(buffer4.size() + 4L);
            } else {
                throw new IllegalArgumentException("Unexpected code point: 0x" + -SegmentedByteString.toHexString(n2));
            }
        }
        return buffer;
    }

    public static final long commonWriteAll(@NotNull Buffer buffer, @NotNull Source source2) {
        long l2;
        Intrinsics.checkNotNullParameter(buffer, "");
        Intrinsics.checkNotNullParameter(source2, "");
        long l3 = 0L;
        while ((l2 = source2.read(buffer, 8192L)) != -1L) {
            l3 += l2;
        }
        return l3;
    }

    @NotNull
    public static final Buffer commonWrite(@NotNull Buffer buffer, @NotNull Source source2, long l2) {
        long l3;
        Intrinsics.checkNotNullParameter(buffer, "");
        Intrinsics.checkNotNullParameter(source2, "");
        for (long i2 = l2; i2 > 0L; i2 -= l3) {
            l3 = source2.read(buffer, i2);
            if (l3 != -1L) continue;
            throw new EOFException();
        }
        return buffer;
    }

    @NotNull
    public static final Buffer commonWriteByte(@NotNull Buffer buffer, int n2) {
        Intrinsics.checkNotNullParameter(buffer, "");
        Segment segment = buffer.writableSegment$okio(1);
        int n3 = segment.limit;
        segment.limit = n3 + 1;
        segment.data[n3] = (byte)n2;
        Buffer buffer2 = buffer;
        buffer2.setSize$okio(buffer2.size() + 1L);
        return buffer;
    }

    @NotNull
    public static final Buffer commonWriteShort(@NotNull Buffer buffer, int n2) {
        Intrinsics.checkNotNullParameter(buffer, "");
        Segment segment = buffer.writableSegment$okio(2);
        byte[] byArray = segment.data;
        int n3 = segment.limit;
        byArray[n3++] = (byte)(n2 >>> 8);
        byArray[n3++] = (byte)n2;
        segment.limit = n3;
        Buffer buffer2 = buffer;
        buffer2.setSize$okio(buffer2.size() + 2L);
        return buffer;
    }

    @NotNull
    public static final Buffer commonWriteInt(@NotNull Buffer buffer, int n2) {
        Intrinsics.checkNotNullParameter(buffer, "");
        Segment segment = buffer.writableSegment$okio(4);
        byte[] byArray = segment.data;
        int n3 = segment.limit;
        byArray[n3++] = (byte)(n2 >>> 24);
        byArray[n3++] = (byte)(n2 >>> 16);
        byArray[n3++] = (byte)(n2 >>> 8);
        byArray[n3++] = (byte)n2;
        segment.limit = n3;
        Buffer buffer2 = buffer;
        buffer2.setSize$okio(buffer2.size() + 4L);
        return buffer;
    }

    @NotNull
    public static final Buffer commonWriteLong(@NotNull Buffer buffer, long l2) {
        Intrinsics.checkNotNullParameter(buffer, "");
        Segment segment = buffer.writableSegment$okio(8);
        byte[] byArray = segment.data;
        int n2 = segment.limit;
        byArray[n2++] = (byte)(l2 >>> 56 & 0xFFL);
        byArray[n2++] = (byte)(l2 >>> 48 & 0xFFL);
        byArray[n2++] = (byte)(l2 >>> 40 & 0xFFL);
        byArray[n2++] = (byte)(l2 >>> 32 & 0xFFL);
        byArray[n2++] = (byte)(l2 >>> 24 & 0xFFL);
        byArray[n2++] = (byte)(l2 >>> 16 & 0xFFL);
        byArray[n2++] = (byte)(l2 >>> 8 & 0xFFL);
        byArray[n2++] = (byte)(l2 & 0xFFL);
        segment.limit = n2;
        Buffer buffer2 = buffer;
        buffer2.setSize$okio(buffer2.size() + 8L);
        return buffer;
    }

    public static final void commonWrite(@NotNull Buffer buffer, @NotNull Buffer buffer2, long l2) {
        long l3;
        Intrinsics.checkNotNullParameter(buffer, "");
        Intrinsics.checkNotNullParameter(buffer2, "");
        if (!(buffer2 != buffer)) {
            String string = "source == this";
            throw new IllegalArgumentException(string.toString());
        }
        -SegmentedByteString.checkOffsetAndCount(buffer2.size(), 0L, l2);
        for (long i2 = l2; i2 > 0L; i2 -= l3) {
            Segment segment = buffer2.head;
            Intrinsics.checkNotNull(segment);
            int n2 = segment.limit;
            Segment segment2 = buffer2.head;
            Intrinsics.checkNotNull(segment2);
            if (i2 < (long)(n2 - segment2.pos)) {
                Segment segment3;
                Segment segment4;
                if (buffer.head != null) {
                    Segment segment5 = buffer.head;
                    Intrinsics.checkNotNull(segment5);
                    segment4 = segment5.prev;
                } else {
                    segment4 = segment3 = null;
                }
                if (segment4 != null && segment3.owner && i2 + (long)segment3.limit - (long)(segment3.shared ? 0 : segment3.pos) <= 8192L) {
                    Segment segment6 = buffer2.head;
                    Intrinsics.checkNotNull(segment6);
                    segment6.writeTo(segment3, (int)i2);
                    Buffer buffer3 = buffer2;
                    buffer3.setSize$okio(buffer3.size() - i2);
                    Buffer buffer4 = buffer;
                    buffer4.setSize$okio(buffer4.size() + i2);
                    return;
                }
                Segment segment7 = buffer2.head;
                Intrinsics.checkNotNull(segment7);
                buffer2.head = segment7.split((int)i2);
            }
            Segment segment8 = buffer2.head;
            Intrinsics.checkNotNull(segment8);
            l3 = segment8.limit - segment8.pos;
            buffer2.head = segment8.pop();
            if (buffer.head == null) {
                buffer.head = segment8;
                segment8.next = segment8.prev = segment8;
            } else {
                Segment segment9 = buffer.head;
                Intrinsics.checkNotNull(segment9);
                Segment segment10 = segment9.prev;
                Intrinsics.checkNotNull(segment10);
                segment10 = segment10.push(segment8);
                segment10.compact();
            }
            Buffer buffer5 = buffer2;
            buffer5.setSize$okio(buffer5.size() - l3);
            Buffer buffer6 = buffer;
            buffer6.setSize$okio(buffer6.size() + l3);
        }
    }

    public static final long commonRead(@NotNull Buffer object, @NotNull Buffer buffer, long l2) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(buffer, "");
        long l3 = l2;
        if (!(l2 >= 0L)) {
            object = "byteCount < 0: " + l2;
            throw new IllegalArgumentException(object.toString());
        }
        if (((Buffer)object).size() == 0L) {
            return -1L;
        }
        if (l2 > ((Buffer)object).size()) {
            l3 = ((Buffer)object).size();
        }
        buffer.write((Buffer)object, l3);
        return l3;
    }

    public static final long commonIndexOf(@NotNull Buffer object, byte by, long l2, long l3) {
        long l4;
        Intrinsics.checkNotNullParameter(object, "");
        long l5 = l2;
        long l6 = l3;
        if (!(0L <= l2 ? l2 <= l3 : false)) {
            object = "size=" + ((Buffer)object).size() + " fromIndex=" + l2 + " toIndex=" + l3;
            throw new IllegalArgumentException(object.toString());
        }
        if (l3 > ((Buffer)object).size()) {
            l6 = ((Buffer)object).size();
        }
        if (l2 == l6) {
            return -1L;
        }
        Segment segment = ((Buffer)object).head;
        if (segment == null) {
            return -1L;
        }
        Object object2 = segment;
        if (((Buffer)object).size() - l2 < l2) {
            long l7;
            for (l7 = ((Buffer)object).size(); l7 > l2; l7 -= (long)(((Segment)object2).limit - ((Segment)object2).pos)) {
                Intrinsics.checkNotNull(((Segment)object2).prev);
            }
            long l8 = l7;
            object = object2;
            if (object == null) {
                return -1L;
            }
            long l9 = l8;
            while (l9 < l6) {
                byte[] byArray = ((Segment)object).data;
                int n2 = (int)Math.min((long)((Segment)object).limit, (long)((Segment)object).pos + l6 - l9);
                for (int i2 = (int)((long)((Segment)object).pos + l5 - l9); i2 < n2; ++i2) {
                    if (byArray[i2] != by) continue;
                    return (long)(i2 - ((Segment)object).pos) + l9;
                }
                l5 = l9 += (long)(((Segment)object).limit - ((Segment)object).pos);
                Intrinsics.checkNotNull(((Segment)object).next);
            }
            return -1L;
        }
        long l10 = 0L;
        while ((l4 = l10 + (long)(((Segment)object2).limit - ((Segment)object2).pos)) <= l2) {
            Intrinsics.checkNotNull(((Segment)object2).next);
            l10 = l4;
        }
        long l11 = l10;
        object = object2;
        if (object == null) {
            return -1L;
        }
        long l12 = l11;
        while (l12 < l6) {
            object2 = ((Segment)object).data;
            int n3 = (int)Math.min((long)((Segment)object).limit, (long)((Segment)object).pos + l6 - l12);
            for (int i3 = (int)((long)((Segment)object).pos + l5 - l12); i3 < n3; ++i3) {
                if (object2[i3] != by) continue;
                return (long)(i3 - ((Segment)object).pos) + l12;
            }
            l5 = l12 += (long)(((Segment)object).limit - ((Segment)object).pos);
            Intrinsics.checkNotNull(((Segment)object).next);
        }
        return -1L;
    }

    public static final long commonIndexOf(@NotNull Buffer object, @NotNull ByteString byteString, long l2) {
        long l3;
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(byteString, "");
        long l4 = l2;
        if (!(byteString.size() > 0)) {
            object = "bytes is empty";
            throw new IllegalArgumentException(object.toString());
        }
        if (!(l2 >= 0L)) {
            object = "fromIndex < 0: " + l2;
            throw new IllegalArgumentException(object.toString());
        }
        Object object2 = object;
        Segment segment = ((Buffer)object2).head;
        if (segment == null) {
            return -1L;
        }
        Object object3 = segment;
        if (((Buffer)object2).size() - l2 < l2) {
            long l5;
            for (l5 = ((Buffer)object2).size(); l5 > l2; l5 -= (long)(((Segment)object3).limit - ((Segment)object3).pos)) {
                Intrinsics.checkNotNull(((Segment)object3).prev);
            }
            long l6 = l5;
            Object object4 = object3;
            if (object4 == null) {
                return -1L;
            }
            long l7 = l6;
            byte[] byArray = byteString.internalArray$okio();
            byte by = byArray[0];
            int n2 = byteString.size();
            long l8 = ((Buffer)object).size() - (long)n2 + 1L;
            while (l7 < l8) {
                object = ((Segment)object4).data;
                int n3 = ((Segment)object4).limit;
                long l9 = (long)((Segment)object4).pos + l8 - l7;
                int n4 = (int)Math.min((long)n3, l9);
                for (n3 = (int)((long)((Segment)object4).pos + l4 - l7); n3 < n4; ++n3) {
                    if (object[n3] != by || !-Buffer.rangeEquals((Segment)object4, n3 + 1, byArray, 1, n2)) continue;
                    return (long)(n3 - ((Segment)object4).pos) + l7;
                }
                l4 = l7 += (long)(((Segment)object4).limit - ((Segment)object4).pos);
                Intrinsics.checkNotNull(((Segment)object4).next);
            }
            return -1L;
        }
        long l10 = 0L;
        while ((l3 = l10 + (long)(((Segment)object3).limit - ((Segment)object3).pos)) <= l2) {
            Intrinsics.checkNotNull(((Segment)object3).next);
            l10 = l3;
        }
        long l11 = l10;
        Object object5 = object3;
        if (object5 == null) {
            return -1L;
        }
        long l12 = l11;
        byte[] byArray = byteString.internalArray$okio();
        object3 = byArray;
        byte by = byArray[0];
        int n5 = byteString.size();
        long l13 = ((Buffer)object).size() - (long)n5 + 1L;
        while (l12 < l13) {
            object = ((Segment)object5).data;
            int n6 = ((Segment)object5).limit;
            long l14 = (long)((Segment)object5).pos + l13 - l12;
            int n7 = (int)Math.min((long)n6, l14);
            for (n6 = (int)((long)((Segment)object5).pos + l4 - l12); n6 < n7; ++n6) {
                if (object[n6] != by || !-Buffer.rangeEquals((Segment)object5, n6 + 1, (byte[])object3, 1, n5)) continue;
                return (long)(n6 - ((Segment)object5).pos) + l12;
            }
            l4 = l12 += (long)(((Segment)object5).limit - ((Segment)object5).pos);
            Intrinsics.checkNotNull(((Segment)object5).next);
        }
        return -1L;
    }

    public static final long commonIndexOfElement(@NotNull Buffer object, @NotNull ByteString object2, long l2) {
        long l3;
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(object2, "");
        long l4 = l2;
        if (!(l2 >= 0L)) {
            object = "fromIndex < 0: " + l2;
            throw new IllegalArgumentException(object.toString());
        }
        Object object3 = object;
        Segment segment = ((Buffer)object3).head;
        if (segment == null) {
            return -1L;
        }
        Object object4 = segment;
        if (((Buffer)object3).size() - l2 < l2) {
            long l5;
            for (l5 = ((Buffer)object3).size(); l5 > l2; l5 -= (long)(((Segment)object4).limit - ((Segment)object4).pos)) {
                Intrinsics.checkNotNull(((Segment)object4).prev);
            }
            long l6 = l5;
            Object object5 = object4;
            if (object5 == null) {
                return -1L;
            }
            long l7 = l6;
            if (((ByteString)object2).size() == 2) {
                byte by = ((ByteString)object2).getByte(0);
                byte by2 = ((ByteString)object2).getByte(1);
                while (l7 < ((Buffer)object).size()) {
                    object3 = ((Segment)object5).data;
                    int n2 = ((Segment)object5).limit;
                    for (int i2 = (int)((long)((Segment)object5).pos + l4 - l7); i2 < n2; ++i2) {
                        Object object6 = object3[i2];
                        if (object6 != by && object6 != by2) continue;
                        return (long)(i2 - ((Segment)object5).pos) + l7;
                    }
                    l4 = l7 += (long)(((Segment)object5).limit - ((Segment)object5).pos);
                    Intrinsics.checkNotNull(((Segment)object5).next);
                }
            } else {
                byte[] byArray = ((ByteString)object2).internalArray$okio();
                while (l7 < ((Buffer)object).size()) {
                    object2 = ((Segment)object5).data;
                    int n3 = ((Segment)object5).limit;
                    for (int i3 = (int)((long)((Segment)object5).pos + l4 - l7); i3 < n3; ++i3) {
                        Object object7 = object2[i3];
                        for (byte by : byArray) {
                            if (object7 != by) continue;
                            return (long)(i3 - ((Segment)object5).pos) + l7;
                        }
                    }
                    l4 = l7 += (long)(((Segment)object5).limit - ((Segment)object5).pos);
                    Intrinsics.checkNotNull(((Segment)object5).next);
                }
            }
            return -1L;
        }
        long l8 = 0L;
        while ((l3 = l8 + (long)(((Segment)object4).limit - ((Segment)object4).pos)) <= l2) {
            Intrinsics.checkNotNull(((Segment)object4).next);
            l8 = l3;
        }
        long l9 = l8;
        Object object8 = object4;
        if (object8 == null) {
            return -1L;
        }
        long l10 = l9;
        if (((ByteString)object2).size() == 2) {
            byte by = ((ByteString)object2).getByte(0);
            byte by3 = ((ByteString)object2).getByte(1);
            while (l10 < ((Buffer)object).size()) {
                byte[] byArray = ((Segment)object8).data;
                int n4 = ((Segment)object8).limit;
                for (int i4 = (int)((long)((Segment)object8).pos + l4 - l10); i4 < n4; ++i4) {
                    byte by4 = byArray[i4];
                    if (by4 != by && by4 != by3) continue;
                    return (long)(i4 - ((Segment)object8).pos) + l10;
                }
                l4 = l10 += (long)(((Segment)object8).limit - ((Segment)object8).pos);
                Intrinsics.checkNotNull(((Segment)object8).next);
            }
        } else {
            object3 = ((ByteString)object2).internalArray$okio();
            while (l10 < ((Buffer)object).size()) {
                object4 = ((Segment)object8).data;
                int n5 = ((Segment)object8).limit;
                for (int i5 = (int)((long)((Segment)object8).pos + l4 - l10); i5 < n5; ++i5) {
                    Object object9 = object4[i5];
                    for (Object object10 : object3) {
                        if (object9 != object10) continue;
                        return (long)(i5 - ((Segment)object8).pos) + l10;
                    }
                }
                l4 = l10 += (long)(((Segment)object8).limit - ((Segment)object8).pos);
                Intrinsics.checkNotNull(((Segment)object8).next);
            }
        }
        return -1L;
    }

    public static final boolean commonRangeEquals(@NotNull Buffer buffer, long l2, @NotNull ByteString byteString, int n2, int n3) {
        Intrinsics.checkNotNullParameter(buffer, "");
        Intrinsics.checkNotNullParameter(byteString, "");
        if (l2 < 0L || n2 < 0 || n3 < 0 || buffer.size() - l2 < (long)n3 || byteString.size() - n2 < n3) {
            return false;
        }
        for (int i2 = 0; i2 < n3; ++i2) {
            if (buffer.getByte(l2 + (long)i2) == byteString.getByte(n2 + i2)) continue;
            return false;
        }
        return true;
    }

    public static final boolean commonEquals(@NotNull Buffer buffer, @Nullable Object object) {
        long l2;
        Intrinsics.checkNotNullParameter(buffer, "");
        if (buffer == object) {
            return true;
        }
        if (!(object instanceof Buffer)) {
            return false;
        }
        if (buffer.size() != ((Buffer)object).size()) {
            return false;
        }
        if (buffer.size() == 0L) {
            return true;
        }
        Segment segment = buffer.head;
        Intrinsics.checkNotNull(segment);
        Segment segment2 = segment;
        Segment segment3 = ((Buffer)object).head;
        Intrinsics.checkNotNull(segment3);
        object = segment3;
        int n2 = segment2.pos;
        int n3 = ((Segment)object).pos;
        for (long i2 = 0L; i2 < buffer.size(); i2 += l2) {
            l2 = Math.min(segment2.limit - n2, ((Segment)object).limit - n3);
            long l3 = l2;
            for (long i3 = 0L; i3 < l3; ++i3) {
                if (segment2.data[n2++] == ((Segment)object).data[n3++]) continue;
                return false;
            }
            if (n2 == segment2.limit) {
                Intrinsics.checkNotNull(segment2.next);
                n2 = segment2.pos;
            }
            if (n3 != ((Segment)object).limit) continue;
            Intrinsics.checkNotNull(((Segment)object).next);
            n3 = ((Segment)object).pos;
        }
        return true;
    }

    public static final int commonHashCode(@NotNull Buffer buffer) {
        Intrinsics.checkNotNullParameter(buffer, "");
        Segment segment = buffer.head;
        if (segment == null) {
            return 0;
        }
        Segment segment2 = segment;
        int n2 = 1;
        do {
            int n3 = segment2.limit;
            for (int i2 = segment2.pos; i2 < n3; ++i2) {
                n2 = n2 * 31 + segment2.data[i2];
            }
            Intrinsics.checkNotNull(segment2.next);
        } while (segment2 != buffer.head);
        return n2;
    }

    @NotNull
    public static final Buffer commonCopy(@NotNull Buffer buffer) {
        Segment segment;
        Intrinsics.checkNotNullParameter(buffer, "");
        Buffer buffer2 = new Buffer();
        if (buffer.size() == 0L) {
            return buffer2;
        }
        Segment segment2 = buffer.head;
        Intrinsics.checkNotNull(segment2);
        Segment segment3 = segment2;
        segment.next = segment.prev = (buffer2.head = (segment = segment2.sharedCopy()));
        Segment segment4 = segment3.next;
        while (segment4 != segment3) {
            Segment segment5 = segment.prev;
            Intrinsics.checkNotNull(segment5);
            Segment segment6 = segment4;
            Intrinsics.checkNotNull(segment6);
            segment5.push(segment6.sharedCopy());
            segment4 = segment4.next;
        }
        buffer2.setSize$okio(buffer.size());
        return buffer2;
    }

    @NotNull
    public static final ByteString commonSnapshot(@NotNull Buffer object) {
        Intrinsics.checkNotNullParameter(object, "");
        if (!(((Buffer)object).size() <= Integer.MAX_VALUE)) {
            object = "size > Int.MAX_VALUE: " + ((Buffer)object).size();
            throw new IllegalStateException(object.toString());
        }
        Buffer buffer = object;
        return buffer.snapshot((int)buffer.size());
    }

    @NotNull
    public static final ByteString commonSnapshot(@NotNull Buffer buffer, int n2) {
        Intrinsics.checkNotNullParameter(buffer, "");
        if (n2 == 0) {
            return ByteString.EMPTY;
        }
        -SegmentedByteString.checkOffsetAndCount(buffer.size(), 0L, n2);
        int n3 = 0;
        int n4 = 0;
        Segment segment = buffer.head;
        while (n3 < n2) {
            Segment segment2 = segment;
            Intrinsics.checkNotNull(segment2);
            if (segment2.limit == segment.pos) {
                throw new AssertionError((Object)"s.limit == s.pos");
            }
            n3 += segment.limit - segment.pos;
            ++n4;
            segment = segment.next;
        }
        byte[][] byArrayArray = new byte[n4][];
        int[] nArray = new int[n4 << 1];
        n3 = 0;
        n4 = 0;
        segment = buffer.head;
        while (n3 < n2) {
            Segment segment3 = segment;
            Intrinsics.checkNotNull(segment3);
            byArrayArray[n4] = segment3.data;
            nArray[n4] = Math.min(n3 += segment.limit - segment.pos, n2);
            nArray[n4 + ((Object[])byArrayArray).length] = segment.pos;
            segment.shared = true;
            ++n4;
            segment = segment.next;
        }
        return new SegmentedByteString(byArrayArray, nArray);
    }

    @NotNull
    public static final Buffer.UnsafeCursor commonReadUnsafe(@NotNull Buffer object, @NotNull Buffer.UnsafeCursor unsafeCursor) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(unsafeCursor, "");
        unsafeCursor = -SegmentedByteString.resolveDefaultParameter(unsafeCursor);
        if (!(unsafeCursor.buffer == null)) {
            object = "already attached to a buffer";
            throw new IllegalStateException(object.toString());
        }
        unsafeCursor.buffer = object;
        unsafeCursor.readWrite = false;
        return unsafeCursor;
    }

    @NotNull
    public static final Buffer.UnsafeCursor commonReadAndWriteUnsafe(@NotNull Buffer object, @NotNull Buffer.UnsafeCursor unsafeCursor) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(unsafeCursor, "");
        unsafeCursor = -SegmentedByteString.resolveDefaultParameter(unsafeCursor);
        if (!(unsafeCursor.buffer == null)) {
            object = "already attached to a buffer";
            throw new IllegalStateException(object.toString());
        }
        unsafeCursor.buffer = object;
        unsafeCursor.readWrite = true;
        return unsafeCursor;
    }

    public static final int commonNext(@NotNull Buffer.UnsafeCursor object) {
        Intrinsics.checkNotNullParameter(object, "");
        long l2 = ((Buffer.UnsafeCursor)object).offset;
        Buffer buffer = ((Buffer.UnsafeCursor)object).buffer;
        Intrinsics.checkNotNull(buffer);
        if (!(l2 != buffer.size())) {
            object = "no more bytes";
            throw new IllegalStateException(object.toString());
        }
        if (((Buffer.UnsafeCursor)object).offset == -1L) {
            return ((Buffer.UnsafeCursor)object).seek(0L);
        }
        Object object2 = object;
        return ((Buffer.UnsafeCursor)object2).seek(((Buffer.UnsafeCursor)object2).offset + (long)(((Buffer.UnsafeCursor)object).end - ((Buffer.UnsafeCursor)object).start));
    }

    public static final int commonSeek(@NotNull Buffer.UnsafeCursor unsafeCursor, long l2) {
        long l3;
        Segment segment;
        Intrinsics.checkNotNullParameter(unsafeCursor, "");
        Buffer buffer = unsafeCursor.buffer;
        if (buffer == null) {
            String string = "not attached to a buffer";
            throw new IllegalStateException(string.toString());
        }
        Buffer buffer2 = buffer;
        if (l2 < -1L || l2 > buffer2.size()) {
            throw new ArrayIndexOutOfBoundsException("offset=" + l2 + " > size=" + buffer2.size());
        }
        if (l2 == -1L || l2 == buffer2.size()) {
            unsafeCursor.setSegment$okio(null);
            unsafeCursor.offset = l2;
            unsafeCursor.data = null;
            unsafeCursor.start = -1;
            unsafeCursor.end = -1;
            return -1;
        }
        long l4 = 0L;
        long l5 = buffer2.size();
        Segment segment2 = buffer2.head;
        Segment segment3 = buffer2.head;
        if (unsafeCursor.getSegment$okio() != null) {
            long l6 = unsafeCursor.offset;
            int n2 = unsafeCursor.start;
            Segment segment4 = unsafeCursor.getSegment$okio();
            Intrinsics.checkNotNull(segment4);
            long l7 = l6 - (long)(n2 - segment4.pos);
            if (l7 > l2) {
                l5 = l7;
                segment3 = unsafeCursor.getSegment$okio();
            } else {
                l4 = l7;
                segment2 = unsafeCursor.getSegment$okio();
            }
        }
        if (l5 - l2 > l2 - l4) {
            segment = segment2;
            l3 = l4;
            while (true) {
                Segment segment5 = segment;
                Intrinsics.checkNotNull(segment5);
                if (l2 >= l3 + (long)(segment5.limit - segment.pos)) {
                    l3 += (long)(segment.limit - segment.pos);
                    segment = segment.next;
                    continue;
                }
                break;
            }
        } else {
            Segment segment6;
            segment = segment3;
            for (l3 = l5; l3 > l2; l3 -= (long)(segment6.limit - segment.pos)) {
                Segment segment7 = segment;
                Intrinsics.checkNotNull(segment7);
                segment6 = segment = segment7.prev;
                Intrinsics.checkNotNull(segment6);
            }
        }
        if (unsafeCursor.readWrite) {
            Segment segment8 = segment;
            Intrinsics.checkNotNull(segment8);
            if (segment8.shared) {
                Segment segment9 = segment.unsharedCopy();
                if (buffer2.head == segment) {
                    buffer2.head = segment9;
                }
                segment = segment.push(segment9);
                Segment segment10 = segment.prev;
                Intrinsics.checkNotNull(segment10);
                segment10.pop();
            }
        }
        unsafeCursor.setSegment$okio(segment);
        unsafeCursor.offset = l2;
        Segment segment11 = segment;
        Intrinsics.checkNotNull(segment11);
        unsafeCursor.data = segment11.data;
        unsafeCursor.start = segment.pos + (int)(l2 - l3);
        unsafeCursor.end = segment.limit;
        return unsafeCursor.end - unsafeCursor.start;
    }

    public static final long commonResizeBuffer(@NotNull Buffer.UnsafeCursor unsafeCursor, long l2) {
        Intrinsics.checkNotNullParameter(unsafeCursor, "");
        Buffer buffer = unsafeCursor.buffer;
        if (buffer == null) {
            String string = "not attached to a buffer";
            throw new IllegalStateException(string.toString());
        }
        Buffer buffer2 = buffer;
        if (!unsafeCursor.readWrite) {
            String string = "resizeBuffer() only permitted for read/write buffers";
            throw new IllegalStateException(string.toString());
        }
        long l3 = buffer2.size();
        if (l2 <= l3) {
            int n2;
            if (!(l2 >= 0L)) {
                String string = "newSize < 0: " + l2;
                throw new IllegalArgumentException(string.toString());
            }
            for (long i2 = l3 - l2; i2 > 0L; i2 -= (long)n2) {
                Segment segment = buffer2.head;
                Intrinsics.checkNotNull(segment);
                Segment segment2 = segment.prev;
                Intrinsics.checkNotNull(segment2);
                n2 = segment2.limit - segment2.pos;
                if ((long)n2 <= i2) {
                    buffer2.head = segment2.pop();
                    SegmentPool.recycle(segment2);
                    continue;
                }
                segment2.limit -= (int)i2;
                break;
            }
            unsafeCursor.setSegment$okio(null);
            unsafeCursor.offset = l2;
            unsafeCursor.data = null;
            unsafeCursor.start = -1;
            unsafeCursor.end = -1;
        } else if (l2 > l3) {
            int n3;
            boolean bl = true;
            for (long i3 = l2 - l3; i3 > 0L; i3 -= (long)n3) {
                Segment segment = buffer2.writableSegment$okio(1);
                n3 = 8192 - segment.limit;
                n3 = (int)Math.min(i3, (long)n3);
                segment.limit += n3;
                if (!bl) continue;
                unsafeCursor.setSegment$okio(segment);
                unsafeCursor.offset = l3;
                unsafeCursor.data = segment.data;
                unsafeCursor.start = segment.limit - n3;
                unsafeCursor.end = segment.limit;
                bl = false;
            }
        }
        buffer2.setSize$okio(l2);
        return l3;
    }

    public static final long commonExpandBuffer(@NotNull Buffer.UnsafeCursor unsafeCursor, int n2) {
        Intrinsics.checkNotNullParameter(unsafeCursor, "");
        if (!(n2 > 0)) {
            String string = "minByteCount <= 0: " + n2;
            throw new IllegalArgumentException(string.toString());
        }
        if (!(n2 <= 8192)) {
            String string = "minByteCount > Segment.SIZE: " + n2;
            throw new IllegalArgumentException(string.toString());
        }
        Buffer buffer = unsafeCursor.buffer;
        if (buffer == null) {
            String string = "not attached to a buffer";
            throw new IllegalStateException(string.toString());
        }
        Buffer buffer2 = buffer;
        if (!unsafeCursor.readWrite) {
            String string = "expandBuffer() only permitted for read/write buffers";
            throw new IllegalStateException(string.toString());
        }
        long l2 = buffer2.size();
        Segment segment = buffer2.writableSegment$okio(n2);
        int n3 = 8192 - segment.limit;
        segment.limit = 8192;
        buffer2.setSize$okio(l2 + (long)n3);
        unsafeCursor.setSegment$okio(segment);
        unsafeCursor.offset = l2;
        unsafeCursor.data = segment.data;
        unsafeCursor.start = 8192 - n3;
        unsafeCursor.end = 8192;
        return n3;
    }

    public static final void commonClose(@NotNull Buffer.UnsafeCursor object) {
        Intrinsics.checkNotNullParameter(object, "");
        if (!(((Buffer.UnsafeCursor)object).buffer != null)) {
            object = "not attached to a buffer";
            throw new IllegalStateException(object.toString());
        }
        ((Buffer.UnsafeCursor)object).buffer = null;
        ((Buffer.UnsafeCursor)object).setSegment$okio(null);
        ((Buffer.UnsafeCursor)object).offset = -1L;
        ((Buffer.UnsafeCursor)object).data = null;
        ((Buffer.UnsafeCursor)object).start = -1;
        ((Buffer.UnsafeCursor)object).end = -1;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package okio.internal;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.jvm.JvmName;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import okio.Buffer;
import okio.ByteString;
import okio.Segment;
import okio.SegmentedByteString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 9, 0}, k=2, xi=48, d1={"\u0000T\n\u0000\n\u0002\u0010\b\n\u0002\u0010\u0015\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0005\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a$\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0000\u001a-\u0010\u0006\u001a\u00020\u0007*\u00020\b2\u0006\u0010\t\u001a\u00020\u00012\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u00012\u0006\u0010\r\u001a\u00020\u0001H\u0080\b\u001a\u0017\u0010\u000e\u001a\u00020\u000f*\u00020\b2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u0080\b\u001a\r\u0010\u0012\u001a\u00020\u0001*\u00020\bH\u0080\b\u001a\r\u0010\u0013\u001a\u00020\u0001*\u00020\bH\u0080\b\u001a\u0015\u0010\u0014\u001a\u00020\u0015*\u00020\b2\u0006\u0010\u0016\u001a\u00020\u0001H\u0080\b\u001a-\u0010\u0017\u001a\u00020\u000f*\u00020\b2\u0006\u0010\t\u001a\u00020\u00012\u0006\u0010\u0010\u001a\u00020\u000b2\u0006\u0010\u0018\u001a\u00020\u00012\u0006\u0010\r\u001a\u00020\u0001H\u0080\b\u001a-\u0010\u0017\u001a\u00020\u000f*\u00020\b2\u0006\u0010\t\u001a\u00020\u00012\u0006\u0010\u0010\u001a\u00020\u00192\u0006\u0010\u0018\u001a\u00020\u00012\u0006\u0010\r\u001a\u00020\u0001H\u0080\b\u001a\u001d\u0010\u001a\u001a\u00020\u0019*\u00020\b2\u0006\u0010\u001b\u001a\u00020\u00012\u0006\u0010\u001c\u001a\u00020\u0001H\u0080\b\u001a\r\u0010\u001d\u001a\u00020\u000b*\u00020\bH\u0080\b\u001a%\u0010\u001e\u001a\u00020\u0007*\u00020\b2\u0006\u0010\u001f\u001a\u00020 2\u0006\u0010\t\u001a\u00020\u00012\u0006\u0010\r\u001a\u00020\u0001H\u0080\b\u001a]\u0010!\u001a\u00020\u0007*\u00020\b2K\u0010\"\u001aG\u0012\u0013\u0012\u00110\u000b\u00a2\u0006\f\b$\u0012\b\b%\u0012\u0004\b\b(&\u0012\u0013\u0012\u00110\u0001\u00a2\u0006\f\b$\u0012\b\b%\u0012\u0004\b\b(\t\u0012\u0013\u0012\u00110\u0001\u00a2\u0006\f\b$\u0012\b\b%\u0012\u0004\b\b(\r\u0012\u0004\u0012\u00020\u00070#H\u0080\b\u00f8\u0001\u0000\u001aj\u0010!\u001a\u00020\u0007*\u00020\b2\u0006\u0010\u001b\u001a\u00020\u00012\u0006\u0010\u001c\u001a\u00020\u00012K\u0010\"\u001aG\u0012\u0013\u0012\u00110\u000b\u00a2\u0006\f\b$\u0012\b\b%\u0012\u0004\b\b(&\u0012\u0013\u0012\u00110\u0001\u00a2\u0006\f\b$\u0012\b\b%\u0012\u0004\b\b(\t\u0012\u0013\u0012\u00110\u0001\u00a2\u0006\f\b$\u0012\b\b%\u0012\u0004\b\b(\r\u0012\u0004\u0012\u00020\u00070#H\u0082\b\u001a\u0014\u0010'\u001a\u00020\u0001*\u00020\b2\u0006\u0010\u0016\u001a\u00020\u0001H\u0000\u0082\u0002\u0007\n\u0005\b\u009920\u0001\u00a8\u0006("}, d2={"binarySearch", "", "", "value", "fromIndex", "toIndex", "commonCopyInto", "", "Lokio/SegmentedByteString;", "offset", "target", "", "targetOffset", "byteCount", "commonEquals", "", "other", "", "commonGetSize", "commonHashCode", "commonInternalGet", "", "pos", "commonRangeEquals", "otherOffset", "Lokio/ByteString;", "commonSubstring", "beginIndex", "endIndex", "commonToByteArray", "commonWrite", "buffer", "Lokio/Buffer;", "forEachSegment", "action", "Lkotlin/Function3;", "Lkotlin/ParameterName;", "name", "data", "segment", "okio"})
@JvmName(name="-SegmentedByteString")
@SourceDebugExtension(value={"SMAP\nSegmentedByteString.kt\nKotlin\n*S Kotlin\n*F\n+ 1 SegmentedByteString.kt\nokio/internal/-SegmentedByteString\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,250:1\n63#1,12:252\n85#1,14:264\n85#1,14:278\n85#1,14:292\n85#1,14:306\n63#1,12:320\n1#2:251\n*S KotlinDebug\n*F\n+ 1 SegmentedByteString.kt\nokio/internal/-SegmentedByteString\n*L\n147#1:252,12\n160#1:264,14\n182#1:278,14\n202#1:292,14\n219#1:306,14\n239#1:320,12\n*E\n"})
public final class -SegmentedByteString {
    public static final int binarySearch(@NotNull int[] nArray, int n2, int n3, int n4) {
        Intrinsics.checkNotNullParameter(nArray, "");
        --n4;
        while (n3 <= n4) {
            int n5 = n3 + n4 >>> 1;
            int n6 = nArray[n5];
            if (n6 < n2) {
                n3 = n5 + 1;
                continue;
            }
            if (n6 > n2) {
                n4 = n5 - 1;
                continue;
            }
            return n5;
        }
        return -n3 - 1;
    }

    public static final int segment(@NotNull SegmentedByteString segmentedByteString, int n2) {
        Intrinsics.checkNotNullParameter(segmentedByteString, "");
        int n3 = -SegmentedByteString.binarySearch(segmentedByteString.getDirectory$okio(), n2 + 1, 0, ((Object[])segmentedByteString.getSegments$okio()).length);
        if (n3 >= 0) {
            return n3;
        }
        return ~n3;
    }

    public static final void forEachSegment(@NotNull SegmentedByteString segmentedByteString, @NotNull Function3<? super byte[], ? super Integer, ? super Integer, Unit> function3) {
        Intrinsics.checkNotNullParameter(segmentedByteString, "");
        Intrinsics.checkNotNullParameter(function3, "");
        int n2 = ((Object[])segmentedByteString.getSegments$okio()).length;
        int n3 = 0;
        for (int i2 = 0; i2 < n2; ++i2) {
            int n4 = segmentedByteString.getDirectory$okio()[n2 + i2];
            int n5 = segmentedByteString.getDirectory$okio()[i2];
            function3.invoke((byte[])segmentedByteString.getSegments$okio()[i2], (Integer)n4, (Integer)(n5 - n3));
            n3 = n5;
        }
    }

    private static final void forEachSegment(SegmentedByteString segmentedByteString, int n2, int n3, Function3<? super byte[], ? super Integer, ? super Integer, Unit> function3) {
        int n4 = -SegmentedByteString.segment(segmentedByteString, n2);
        while (n2 < n3) {
            int n5 = n4 == 0 ? 0 : segmentedByteString.getDirectory$okio()[n4 - 1];
            int n6 = segmentedByteString.getDirectory$okio()[n4] - n5;
            int n7 = segmentedByteString.getDirectory$okio()[((Object[])segmentedByteString.getSegments$okio()).length + n4];
            n6 = Math.min(n3, n5 + n6) - n2;
            n5 = n7 + (n2 - n5);
            function3.invoke((byte[])segmentedByteString.getSegments$okio()[n4], (Integer)n5, (Integer)n6);
            n2 += n6;
            ++n4;
        }
    }

    @NotNull
    public static final ByteString commonSubstring(@NotNull SegmentedByteString segmentedByteString, int n2, int n3) {
        Intrinsics.checkNotNullParameter(segmentedByteString, "");
        n3 = okio.-SegmentedByteString.resolveDefaultParameter(segmentedByteString, n3);
        if (!(n2 >= 0)) {
            String string = "beginIndex=" + n2 + " < 0";
            throw new IllegalArgumentException(string.toString());
        }
        if (!(n3 <= segmentedByteString.size())) {
            String string = "endIndex=" + n3 + " > length(" + segmentedByteString.size() + ')';
            throw new IllegalArgumentException(string.toString());
        }
        int n4 = n3 - n2;
        if (!(n4 >= 0)) {
            String string = "endIndex=" + n3 + " < beginIndex=" + n2;
            throw new IllegalArgumentException(string.toString());
        }
        if (n2 == 0 && n3 == segmentedByteString.size()) {
            return segmentedByteString;
        }
        if (n2 == n3) {
            return ByteString.EMPTY;
        }
        int n5 = -SegmentedByteString.segment(segmentedByteString, n2);
        n3 = -SegmentedByteString.segment(segmentedByteString, n3 - 1);
        Object[] objectArray = (Object[])segmentedByteString.getSegments$okio();
        int n6 = n3 + 1;
        byte[][] byArray = (byte[][])ArraysKt.copyOfRange(objectArray, n5, n6);
        objectArray = new int[((Object[])byArray).length << 1];
        n6 = 0;
        int n7 = n5;
        if (n5 <= n3) {
            while (true) {
                objectArray[n6] = Math.min(segmentedByteString.getDirectory$okio()[n7] - n2, n4);
                objectArray[n6++ + ((Object[])byArray).length] = segmentedByteString.getDirectory$okio()[n7 + ((Object[])segmentedByteString.getSegments$okio()).length];
                if (n7 == n3) break;
                ++n7;
            }
        }
        n7 = n5 == 0 ? 0 : segmentedByteString.getDirectory$okio()[n5 - 1];
        int n8 = ((Object[])byArray).length;
        objectArray[n8] = objectArray[n8] + (n2 - n7);
        return new SegmentedByteString(byArray, (int[])objectArray);
    }

    public static final byte commonInternalGet(@NotNull SegmentedByteString segmentedByteString, int n2) {
        Intrinsics.checkNotNullParameter(segmentedByteString, "");
        okio.-SegmentedByteString.checkOffsetAndCount(segmentedByteString.getDirectory$okio()[((Object[])segmentedByteString.getSegments$okio()).length - 1], n2, 1L);
        int n3 = -SegmentedByteString.segment(segmentedByteString, n2);
        int n4 = n3 == 0 ? 0 : segmentedByteString.getDirectory$okio()[n3 - 1];
        int n5 = segmentedByteString.getDirectory$okio()[n3 + ((Object[])segmentedByteString.getSegments$okio()).length];
        return segmentedByteString.getSegments$okio()[n3][n2 - n4 + n5];
    }

    public static final int commonGetSize(@NotNull SegmentedByteString segmentedByteString) {
        Intrinsics.checkNotNullParameter(segmentedByteString, "");
        return segmentedByteString.getDirectory$okio()[((Object[])segmentedByteString.getSegments$okio()).length - 1];
    }

    @NotNull
    public static final byte[] commonToByteArray(@NotNull SegmentedByteString segmentedByteString) {
        Intrinsics.checkNotNullParameter(segmentedByteString, "");
        byte[] byArray = new byte[segmentedByteString.size()];
        int n2 = 0;
        int n3 = ((Object[])segmentedByteString.getSegments$okio()).length;
        int n4 = 0;
        for (int i2 = 0; i2 < n3; ++i2) {
            int n5 = segmentedByteString.getDirectory$okio()[n3 + i2];
            int n6 = segmentedByteString.getDirectory$okio()[i2];
            n4 = n6 - n4;
            byte[] byArray2 = segmentedByteString.getSegments$okio()[i2];
            int n7 = n5;
            ArraysKt.copyInto(byArray2, byArray, n2, n7, n7 + n4);
            n2 += n4;
            n4 = n6;
        }
        return byArray;
    }

    public static final void commonWrite(@NotNull SegmentedByteString segmentedByteString, @NotNull Buffer buffer, int n2, int n3) {
        Intrinsics.checkNotNullParameter(segmentedByteString, "");
        Intrinsics.checkNotNullParameter(buffer, "");
        int n4 = n2 + n3;
        int n5 = -SegmentedByteString.segment(segmentedByteString, n2);
        while (n2 < n4) {
            int n6 = n5 == 0 ? 0 : segmentedByteString.getDirectory$okio()[n5 - 1];
            int n7 = segmentedByteString.getDirectory$okio()[n5] - n6;
            int n8 = segmentedByteString.getDirectory$okio()[((Object[])segmentedByteString.getSegments$okio()).length + n5];
            n7 = Math.min(n4, n6 + n7) - n2;
            int n9 = n6 = n8 + (n2 - n6);
            n6 = n7;
            n8 = n9;
            byte[] byArray = segmentedByteString.getSegments$okio()[n5];
            int n10 = n8;
            Segment segment = new Segment(byArray, n10, n10 + n6, true, false);
            if (buffer.head == null) {
                buffer.head = segment.next = (segment.prev = segment);
            } else {
                Segment segment2 = buffer.head;
                Intrinsics.checkNotNull(segment2);
                Segment segment3 = segment2.prev;
                Intrinsics.checkNotNull(segment3);
                segment3.push(segment);
            }
            n2 += n7;
            ++n5;
        }
        Buffer buffer2 = buffer;
        buffer2.setSize$okio(buffer2.size() + (long)n3);
    }

    public static final boolean commonRangeEquals(@NotNull SegmentedByteString segmentedByteString, int n2, @NotNull ByteString byteString, int n3, int n4) {
        Intrinsics.checkNotNullParameter(segmentedByteString, "");
        Intrinsics.checkNotNullParameter(byteString, "");
        if (n2 < 0 || n2 > segmentedByteString.size() - n4) {
            return false;
        }
        n4 = n2 + n4;
        int n5 = -SegmentedByteString.segment(segmentedByteString, n2);
        while (n2 < n4) {
            int n6 = n5 == 0 ? 0 : segmentedByteString.getDirectory$okio()[n5 - 1];
            int n7 = segmentedByteString.getDirectory$okio()[n5] - n6;
            int n8 = segmentedByteString.getDirectory$okio()[((Object[])segmentedByteString.getSegments$okio()).length + n5];
            n7 = Math.min(n4, n6 + n7) - n2;
            int n9 = n6 = n8 + (n2 - n6);
            n6 = n7;
            n8 = n9;
            byte[] byArray = segmentedByteString.getSegments$okio()[n5];
            if (!byteString.rangeEquals(n3, byArray, n8, n6)) {
                return false;
            }
            n3 += n6;
            n2 += n7;
            ++n5;
        }
        return true;
    }

    public static final boolean commonRangeEquals(@NotNull SegmentedByteString segmentedByteString, int n2, @NotNull byte[] byArray, int n3, int n4) {
        Intrinsics.checkNotNullParameter(segmentedByteString, "");
        Intrinsics.checkNotNullParameter(byArray, "");
        if (n2 < 0 || n2 > segmentedByteString.size() - n4 || n3 < 0 || n3 > byArray.length - n4) {
            return false;
        }
        n4 = n2 + n4;
        int n5 = -SegmentedByteString.segment(segmentedByteString, n2);
        while (n2 < n4) {
            int n6 = n5 == 0 ? 0 : segmentedByteString.getDirectory$okio()[n5 - 1];
            int n7 = segmentedByteString.getDirectory$okio()[n5] - n6;
            int n8 = segmentedByteString.getDirectory$okio()[((Object[])segmentedByteString.getSegments$okio()).length + n5];
            n7 = Math.min(n4, n6 + n7) - n2;
            int n9 = n6 = n8 + (n2 - n6);
            n6 = n7;
            n8 = n9;
            byte[] byArray2 = segmentedByteString.getSegments$okio()[n5];
            if (!okio.-SegmentedByteString.arrayRangeEquals(byArray2, n8, byArray, n3, n6)) {
                return false;
            }
            n3 += n6;
            n2 += n7;
            ++n5;
        }
        return true;
    }

    public static final void commonCopyInto(@NotNull SegmentedByteString segmentedByteString, int n2, @NotNull byte[] byArray, int n3, int n4) {
        Intrinsics.checkNotNullParameter(segmentedByteString, "");
        Intrinsics.checkNotNullParameter(byArray, "");
        okio.-SegmentedByteString.checkOffsetAndCount(segmentedByteString.size(), n2, n4);
        okio.-SegmentedByteString.checkOffsetAndCount(byArray.length, n3, n4);
        n4 = n2 + n4;
        int n5 = -SegmentedByteString.segment(segmentedByteString, n2);
        while (n2 < n4) {
            int n6 = n5 == 0 ? 0 : segmentedByteString.getDirectory$okio()[n5 - 1];
            int n7 = segmentedByteString.getDirectory$okio()[n5] - n6;
            int n8 = segmentedByteString.getDirectory$okio()[((Object[])segmentedByteString.getSegments$okio()).length + n5];
            n7 = Math.min(n4, n6 + n7) - n2;
            int n9 = n6 = n8 + (n2 - n6);
            n6 = n7;
            n8 = n9;
            byte[] byArray2 = segmentedByteString.getSegments$okio()[n5];
            int n10 = n8;
            ArraysKt.copyInto(byArray2, byArray, n3, n10, n10 + n6);
            n3 += n6;
            n2 += n7;
            ++n5;
        }
    }

    public static final boolean commonEquals(@NotNull SegmentedByteString segmentedByteString, @Nullable Object object) {
        Intrinsics.checkNotNullParameter(segmentedByteString, "");
        if (object == segmentedByteString) {
            return true;
        }
        if (object instanceof ByteString) {
            return ((ByteString)object).size() == segmentedByteString.size() && ((ByteString)segmentedByteString).rangeEquals(0, (ByteString)object, 0, segmentedByteString.size());
        }
        return false;
    }

    public static final int commonHashCode(@NotNull SegmentedByteString segmentedByteString) {
        Intrinsics.checkNotNullParameter(segmentedByteString, "");
        int n2 = segmentedByteString.getHashCode$okio();
        if (n2 != 0) {
            return n2;
        }
        n2 = 1;
        SegmentedByteString segmentedByteString2 = segmentedByteString;
        int n3 = ((Object[])segmentedByteString2.getSegments$okio()).length;
        int n4 = 0;
        for (int i2 = 0; i2 < n3; ++i2) {
            int n5 = segmentedByteString2.getDirectory$okio()[n3 + i2];
            int n6 = segmentedByteString2.getDirectory$okio()[i2];
            n4 = n6 - n4;
            byte[] byArray = segmentedByteString2.getSegments$okio()[i2];
            n4 = n5 + n4;
            for (int i3 = n5; i3 < n4; ++i3) {
                n2 = n2 * 31 + byArray[i3];
            }
            n4 = n6;
        }
        segmentedByteString.setHashCode$okio(n2);
        return n2;
    }
}


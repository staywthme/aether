/*
 * Decompiled with CFR 0.152.
 */
package okio.internal;

import java.io.EOFException;
import kotlin.Metadata;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.CharsKt;
import okio.-SegmentedByteString;
import okio.Buffer;
import okio.BufferedSource;
import okio.ByteString;
import okio.Okio;
import okio.Options;
import okio.PeekSource;
import okio.RealBufferedSource;
import okio.Sink;
import okio.Timeout;
import okio.internal.-Buffer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 9, 0}, k=2, xi=48, d1={"\u0000j\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0005\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\n\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\r\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u0080\b\u001a\r\u0010\u0003\u001a\u00020\u0004*\u00020\u0002H\u0080\b\u001a%\u0010\u0005\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\u0006H\u0080\b\u001a\u001d\u0010\u0005\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\t\u001a\u00020\u0006H\u0080\b\u001a\u001d\u0010\r\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u000e\u001a\u00020\f2\u0006\u0010\t\u001a\u00020\u0006H\u0080\b\u001a\r\u0010\u000f\u001a\u00020\u0010*\u00020\u0002H\u0080\b\u001a-\u0010\u0011\u001a\u00020\u0004*\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0014H\u0080\b\u001a%\u0010\u0016\u001a\u00020\u0014*\u00020\u00022\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0012\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0014H\u0080\b\u001a\u001d\u0010\u0016\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u0017\u001a\u00020\u00192\u0006\u0010\u0015\u001a\u00020\u0006H\u0080\b\u001a\u0015\u0010\u001a\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u0017\u001a\u00020\u001bH\u0080\b\u001a\r\u0010\u001c\u001a\u00020\b*\u00020\u0002H\u0080\b\u001a\r\u0010\u001d\u001a\u00020\u0018*\u00020\u0002H\u0080\b\u001a\u0015\u0010\u001d\u001a\u00020\u0018*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u0006H\u0080\b\u001a\r\u0010\u001e\u001a\u00020\f*\u00020\u0002H\u0080\b\u001a\u0015\u0010\u001e\u001a\u00020\f*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u0006H\u0080\b\u001a\r\u0010\u001f\u001a\u00020\u0006*\u00020\u0002H\u0080\b\u001a\u0015\u0010 \u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0017\u001a\u00020\u0018H\u0080\b\u001a\u001d\u0010 \u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0017\u001a\u00020\u00192\u0006\u0010\u0015\u001a\u00020\u0006H\u0080\b\u001a\r\u0010!\u001a\u00020\u0006*\u00020\u0002H\u0080\b\u001a\r\u0010\"\u001a\u00020\u0014*\u00020\u0002H\u0080\b\u001a\r\u0010#\u001a\u00020\u0014*\u00020\u0002H\u0080\b\u001a\r\u0010$\u001a\u00020\u0006*\u00020\u0002H\u0080\b\u001a\r\u0010%\u001a\u00020\u0006*\u00020\u0002H\u0080\b\u001a\r\u0010&\u001a\u00020'*\u00020\u0002H\u0080\b\u001a\r\u0010(\u001a\u00020'*\u00020\u0002H\u0080\b\u001a\r\u0010)\u001a\u00020**\u00020\u0002H\u0080\b\u001a\u0015\u0010)\u001a\u00020**\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u0006H\u0080\b\u001a\r\u0010+\u001a\u00020\u0014*\u00020\u0002H\u0080\b\u001a\u000f\u0010,\u001a\u0004\u0018\u00010**\u00020\u0002H\u0080\b\u001a\u0015\u0010-\u001a\u00020**\u00020\u00022\u0006\u0010.\u001a\u00020\u0006H\u0080\b\u001a\u0015\u0010/\u001a\u00020\u0004*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u0006H\u0080\b\u001a\u0015\u00100\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u0006H\u0080\b\u001a\u0015\u00101\u001a\u00020\u0014*\u00020\u00022\u0006\u00102\u001a\u000203H\u0080\b\u001a\u0015\u00104\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u0006H\u0080\b\u001a\r\u00105\u001a\u000206*\u00020\u0002H\u0080\b\u001a\r\u00107\u001a\u00020**\u00020\u0002H\u0080\b\u00a8\u00068"}, d2={"commonClose", "", "Lokio/RealBufferedSource;", "commonExhausted", "", "commonIndexOf", "", "b", "", "fromIndex", "toIndex", "bytes", "Lokio/ByteString;", "commonIndexOfElement", "targetBytes", "commonPeek", "Lokio/BufferedSource;", "commonRangeEquals", "offset", "bytesOffset", "", "byteCount", "commonRead", "sink", "", "Lokio/Buffer;", "commonReadAll", "Lokio/Sink;", "commonReadByte", "commonReadByteArray", "commonReadByteString", "commonReadDecimalLong", "commonReadFully", "commonReadHexadecimalUnsignedLong", "commonReadInt", "commonReadIntLe", "commonReadLong", "commonReadLongLe", "commonReadShort", "", "commonReadShortLe", "commonReadUtf8", "", "commonReadUtf8CodePoint", "commonReadUtf8Line", "commonReadUtf8LineStrict", "limit", "commonRequest", "commonRequire", "commonSelect", "options", "Lokio/Options;", "commonSkip", "commonTimeout", "Lokio/Timeout;", "commonToString", "okio"})
@JvmName(name="-RealBufferedSource")
@SourceDebugExtension(value={"SMAP\nRealBufferedSource.kt\nKotlin\n*S Kotlin\n*F\n+ 1 RealBufferedSource.kt\nokio/internal/-RealBufferedSource\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 RealBufferedSource.kt\nokio/RealBufferedSource\n+ 4 Util.kt\nokio/-SegmentedByteString\n*L\n1#1,402:1\n1#2:403\n62#3:404\n62#3:405\n62#3:406\n62#3:407\n62#3:408\n62#3:409\n62#3:410\n62#3:411\n62#3:412\n62#3:413\n62#3:414\n62#3:415\n62#3:416\n62#3:417\n62#3:418\n62#3:419\n62#3:420\n62#3:421\n62#3:422\n62#3:423\n62#3:424\n62#3:425\n62#3:426\n62#3:428\n62#3:429\n62#3:430\n62#3:431\n62#3:432\n62#3:433\n62#3:434\n62#3:435\n62#3:436\n62#3:437\n62#3:438\n62#3:439\n62#3:440\n62#3:441\n62#3:442\n62#3:443\n62#3:444\n62#3:445\n62#3:446\n62#3:447\n62#3:449\n62#3:450\n62#3:451\n62#3:452\n62#3:453\n62#3:454\n62#3:455\n62#3:456\n62#3:457\n62#3:458\n62#3:459\n62#3:460\n62#3:461\n62#3:462\n62#3:463\n62#3:464\n62#3:465\n62#3:466\n62#3:467\n62#3:468\n62#3:469\n62#3:470\n62#3:471\n62#3:472\n62#3:473\n62#3:474\n62#3:475\n89#4:427\n89#4:448\n*S KotlinDebug\n*F\n+ 1 RealBufferedSource.kt\nokio/internal/-RealBufferedSource\n*L\n41#1:404\n42#1:405\n46#1:406\n47#1:407\n52#1:408\n62#1:409\n63#1:410\n70#1:411\n74#1:412\n75#1:413\n80#1:414\n87#1:415\n94#1:416\n99#1:417\n107#1:418\n108#1:419\n113#1:420\n122#1:421\n123#1:422\n130#1:423\n136#1:424\n137#1:425\n141#1:426\n142#1:428\n150#1:429\n154#1:430\n159#1:431\n160#1:432\n163#1:433\n166#1:434\n167#1:435\n168#1:436\n174#1:437\n175#1:438\n180#1:439\n187#1:440\n188#1:441\n193#1:442\n201#1:443\n203#1:444\n204#1:445\n206#1:446\n209#1:447\n211#1:449\n219#1:450\n226#1:451\n231#1:452\n236#1:453\n241#1:454\n246#1:455\n251#1:456\n256#1:457\n264#1:458\n275#1:459\n283#1:460\n297#1:461\n304#1:462\n307#1:463\n308#1:464\n319#1:465\n324#1:466\n325#1:467\n338#1:468\n341#1:469\n342#1:470\n354#1:471\n357#1:472\n358#1:473\n383#1:474\n396#1:475\n141#1:427\n209#1:448\n*E\n"})
public final class -RealBufferedSource {
    public static final long commonRead(@NotNull RealBufferedSource realBufferedSource, @NotNull Buffer buffer, long l2) {
        RealBufferedSource realBufferedSource2;
        Intrinsics.checkNotNullParameter(realBufferedSource, "");
        Intrinsics.checkNotNullParameter(buffer, "");
        if (!(l2 >= 0L)) {
            String string = "byteCount < 0: " + l2;
            throw new IllegalArgumentException(string.toString());
        }
        if (!(!realBufferedSource.closed)) {
            String string = "closed";
            throw new IllegalStateException(string.toString());
        }
        RealBufferedSource realBufferedSource3 = realBufferedSource;
        if (realBufferedSource3.bufferField.size() == 0L) {
            realBufferedSource2 = realBufferedSource;
            long l3 = realBufferedSource.source.read(realBufferedSource2.bufferField, 8192L);
            if (l3 == -1L) {
                return -1L;
            }
        }
        realBufferedSource2 = realBufferedSource;
        long l4 = realBufferedSource2.bufferField.size();
        long l5 = Math.min(l2, l4);
        RealBufferedSource realBufferedSource4 = realBufferedSource;
        return realBufferedSource4.bufferField.read(buffer, l5);
    }

    public static final boolean commonExhausted(@NotNull RealBufferedSource object) {
        Intrinsics.checkNotNullParameter(object, "");
        if (!(!((RealBufferedSource)object).closed)) {
            object = "closed";
            throw new IllegalStateException(object.toString());
        }
        Object object2 = object;
        if (((RealBufferedSource)object2).bufferField.exhausted()) {
            object2 = object;
            if (((RealBufferedSource)object).source.read(((RealBufferedSource)object2).bufferField, 8192L) == -1L) {
                return true;
            }
        }
        return false;
    }

    public static final void commonRequire(@NotNull RealBufferedSource realBufferedSource, long l2) {
        Intrinsics.checkNotNullParameter(realBufferedSource, "");
        if (!realBufferedSource.request(l2)) {
            throw new EOFException();
        }
    }

    public static final boolean commonRequest(@NotNull RealBufferedSource object, long l2) {
        block3: {
            Object object2;
            Intrinsics.checkNotNullParameter(object, "");
            if (!(l2 >= 0L)) {
                object = "byteCount < 0: " + l2;
                throw new IllegalArgumentException(object.toString());
            }
            if (!(!((RealBufferedSource)object).closed)) {
                object = "closed";
                throw new IllegalStateException(object.toString());
            }
            do {
                object2 = object;
                if (((RealBufferedSource)object2).bufferField.size() >= l2) break block3;
                object2 = object;
            } while (((RealBufferedSource)object).source.read(((RealBufferedSource)object2).bufferField, 8192L) != -1L);
            return false;
        }
        return true;
    }

    public static final byte commonReadByte(@NotNull RealBufferedSource realBufferedSource) {
        Intrinsics.checkNotNullParameter(realBufferedSource, "");
        realBufferedSource.require(1L);
        return realBufferedSource.bufferField.readByte();
    }

    @NotNull
    public static final ByteString commonReadByteString(@NotNull RealBufferedSource realBufferedSource) {
        Intrinsics.checkNotNullParameter(realBufferedSource, "");
        RealBufferedSource realBufferedSource2 = realBufferedSource;
        realBufferedSource2.bufferField.writeAll(realBufferedSource.source);
        realBufferedSource2 = realBufferedSource;
        return realBufferedSource2.bufferField.readByteString();
    }

    @NotNull
    public static final ByteString commonReadByteString(@NotNull RealBufferedSource realBufferedSource, long l2) {
        Intrinsics.checkNotNullParameter(realBufferedSource, "");
        realBufferedSource.require(l2);
        return realBufferedSource.bufferField.readByteString(l2);
    }

    public static final int commonSelect(@NotNull RealBufferedSource realBufferedSource, @NotNull Options options) {
        int n2;
        Intrinsics.checkNotNullParameter(realBufferedSource, "");
        Intrinsics.checkNotNullParameter(options, "");
        if (!(!realBufferedSource.closed)) {
            String string = "closed";
            throw new IllegalStateException(string.toString());
        }
        block4: while (true) {
            RealBufferedSource realBufferedSource2 = realBufferedSource;
            n2 = -Buffer.selectPrefix(realBufferedSource2.bufferField, options, true);
            switch (n2) {
                case -1: {
                    return -1;
                }
                case -2: {
                    RealBufferedSource realBufferedSource3 = realBufferedSource;
                    if (realBufferedSource.source.read(realBufferedSource3.bufferField, 8192L) != -1L) continue block4;
                    return -1;
                }
            }
            break;
        }
        int n3 = options.getByteStrings$okio()[n2].size();
        realBufferedSource.bufferField.skip(n3);
        return n2;
    }

    @NotNull
    public static final byte[] commonReadByteArray(@NotNull RealBufferedSource realBufferedSource) {
        Intrinsics.checkNotNullParameter(realBufferedSource, "");
        RealBufferedSource realBufferedSource2 = realBufferedSource;
        realBufferedSource2.bufferField.writeAll(realBufferedSource.source);
        realBufferedSource2 = realBufferedSource;
        return realBufferedSource2.bufferField.readByteArray();
    }

    @NotNull
    public static final byte[] commonReadByteArray(@NotNull RealBufferedSource realBufferedSource, long l2) {
        Intrinsics.checkNotNullParameter(realBufferedSource, "");
        realBufferedSource.require(l2);
        return realBufferedSource.bufferField.readByteArray(l2);
    }

    public static final void commonReadFully(@NotNull RealBufferedSource realBufferedSource, @NotNull byte[] byArray) {
        Intrinsics.checkNotNullParameter(realBufferedSource, "");
        Intrinsics.checkNotNullParameter(byArray, "");
        try {
            realBufferedSource.require(byArray.length);
        }
        catch (EOFException eOFException) {
            int n2 = 0;
            while (true) {
                RealBufferedSource realBufferedSource2 = realBufferedSource;
                if (realBufferedSource2.bufferField.size() <= 0L) break;
                RealBufferedSource realBufferedSource3 = realBufferedSource;
                realBufferedSource2 = realBufferedSource3;
                realBufferedSource2 = realBufferedSource;
                int n3 = realBufferedSource3.bufferField.read(byArray, n2, (int)realBufferedSource2.bufferField.size());
                if (n3 == -1) {
                    throw new AssertionError();
                }
                n2 += n3;
            }
            throw eOFException;
        }
        RealBufferedSource realBufferedSource4 = realBufferedSource;
        realBufferedSource4.bufferField.readFully(byArray);
    }

    public static final int commonRead(@NotNull RealBufferedSource realBufferedSource, @NotNull byte[] byArray, int n2, int n3) {
        Intrinsics.checkNotNullParameter(realBufferedSource, "");
        Intrinsics.checkNotNullParameter(byArray, "");
        -SegmentedByteString.checkOffsetAndCount(byArray.length, n2, n3);
        RealBufferedSource realBufferedSource2 = realBufferedSource;
        if (realBufferedSource2.bufferField.size() == 0L) {
            RealBufferedSource realBufferedSource3 = realBufferedSource;
            long l2 = realBufferedSource.source.read(realBufferedSource3.bufferField, 8192L);
            if (l2 == -1L) {
                return -1;
            }
        }
        RealBufferedSource realBufferedSource4 = realBufferedSource;
        long l3 = realBufferedSource4.bufferField.size();
        int n4 = (int)Math.min((long)n3, l3);
        RealBufferedSource realBufferedSource5 = realBufferedSource;
        return realBufferedSource5.bufferField.read(byArray, n2, n4);
    }

    public static final void commonReadFully(@NotNull RealBufferedSource realBufferedSource, @NotNull Buffer buffer, long l2) {
        Intrinsics.checkNotNullParameter(realBufferedSource, "");
        Intrinsics.checkNotNullParameter(buffer, "");
        try {
            realBufferedSource.require(l2);
        }
        catch (EOFException eOFException) {
            buffer.writeAll(realBufferedSource.bufferField);
            throw eOFException;
        }
        RealBufferedSource realBufferedSource2 = realBufferedSource;
        realBufferedSource2.bufferField.readFully(buffer, l2);
    }

    public static final long commonReadAll(@NotNull RealBufferedSource realBufferedSource, @NotNull Sink sink2) {
        RealBufferedSource realBufferedSource2;
        Intrinsics.checkNotNullParameter(realBufferedSource, "");
        Intrinsics.checkNotNullParameter(sink2, "");
        long l2 = 0L;
        while (true) {
            realBufferedSource2 = realBufferedSource;
            if (realBufferedSource.source.read(realBufferedSource2.bufferField, 8192L) == -1L) break;
            RealBufferedSource realBufferedSource3 = realBufferedSource;
            long l3 = realBufferedSource3.bufferField.completeSegmentByteCount();
            if (l3 <= 0L) continue;
            l2 += l3;
            realBufferedSource3 = realBufferedSource;
            sink2.write(realBufferedSource3.bufferField, l3);
        }
        realBufferedSource2 = realBufferedSource;
        if (realBufferedSource2.bufferField.size() > 0L) {
            realBufferedSource2 = realBufferedSource;
            l2 += realBufferedSource2.bufferField.size();
            RealBufferedSource realBufferedSource4 = realBufferedSource;
            realBufferedSource2 = realBufferedSource4;
            realBufferedSource2 = realBufferedSource;
            sink2.write(realBufferedSource4.bufferField, realBufferedSource2.bufferField.size());
        }
        return l2;
    }

    @NotNull
    public static final String commonReadUtf8(@NotNull RealBufferedSource realBufferedSource) {
        Intrinsics.checkNotNullParameter(realBufferedSource, "");
        RealBufferedSource realBufferedSource2 = realBufferedSource;
        realBufferedSource2.bufferField.writeAll(realBufferedSource.source);
        realBufferedSource2 = realBufferedSource;
        return realBufferedSource2.bufferField.readUtf8();
    }

    @NotNull
    public static final String commonReadUtf8(@NotNull RealBufferedSource realBufferedSource, long l2) {
        Intrinsics.checkNotNullParameter(realBufferedSource, "");
        realBufferedSource.require(l2);
        return realBufferedSource.bufferField.readUtf8(l2);
    }

    @Nullable
    public static final String commonReadUtf8Line(@NotNull RealBufferedSource realBufferedSource) {
        Intrinsics.checkNotNullParameter(realBufferedSource, "");
        long l2 = realBufferedSource.indexOf((byte)10);
        if (l2 == -1L) {
            RealBufferedSource realBufferedSource2 = realBufferedSource;
            if (realBufferedSource2.bufferField.size() != 0L) {
                realBufferedSource2 = realBufferedSource;
                return realBufferedSource2.readUtf8(realBufferedSource2.bufferField.size());
            }
            return null;
        }
        RealBufferedSource realBufferedSource3 = realBufferedSource;
        return -Buffer.readUtf8Line(realBufferedSource3.bufferField, l2);
    }

    @NotNull
    public static final String commonReadUtf8LineStrict(@NotNull RealBufferedSource realBufferedSource, long l2) {
        BufferedSource bufferedSource;
        Intrinsics.checkNotNullParameter(realBufferedSource, "");
        if (!(l2 >= 0L)) {
            String string = "limit < 0: " + l2;
            throw new IllegalArgumentException(string.toString());
        }
        long l3 = l2 == Long.MAX_VALUE ? Long.MAX_VALUE : l2 + 1L;
        long l4 = realBufferedSource.indexOf((byte)10, 0L, l3);
        if (l4 != -1L) {
            RealBufferedSource realBufferedSource2 = realBufferedSource;
            return -Buffer.readUtf8Line(realBufferedSource2.bufferField, l4);
        }
        if (l3 < Long.MAX_VALUE && realBufferedSource.request(l3)) {
            bufferedSource = realBufferedSource;
            if (((RealBufferedSource)bufferedSource).bufferField.getByte(l3 - 1L) == 13 && realBufferedSource.request(l3 + 1L)) {
                bufferedSource = realBufferedSource;
                if (((RealBufferedSource)bufferedSource).bufferField.getByte(l3) == 10) {
                    bufferedSource = realBufferedSource;
                    return -Buffer.readUtf8Line(((RealBufferedSource)bufferedSource).bufferField, l3);
                }
            }
        }
        bufferedSource = new Buffer();
        RealBufferedSource realBufferedSource3 = realBufferedSource;
        RealBufferedSource realBufferedSource4 = realBufferedSource;
        long l5 = realBufferedSource4.bufferField.size();
        realBufferedSource3.bufferField.copyTo((Buffer)bufferedSource, 0L, Math.min(32L, l5));
        realBufferedSource3 = realBufferedSource;
        throw new EOFException("\\n not found: limit=" + Math.min(realBufferedSource3.bufferField.size(), l2) + " content=" + ((Buffer)bufferedSource).readByteString().hex() + '\u2026');
    }

    public static final int commonReadUtf8CodePoint(@NotNull RealBufferedSource realBufferedSource) {
        Intrinsics.checkNotNullParameter(realBufferedSource, "");
        realBufferedSource.require(1L);
        RealBufferedSource realBufferedSource2 = realBufferedSource;
        byte by = realBufferedSource2.bufferField.getByte(0L);
        if ((by & 0xE0) == 192) {
            realBufferedSource.require(2L);
        } else if ((by & 0xF0) == 224) {
            realBufferedSource.require(3L);
        } else if ((by & 0xF8) == 240) {
            realBufferedSource.require(4L);
        }
        RealBufferedSource realBufferedSource3 = realBufferedSource;
        return realBufferedSource3.bufferField.readUtf8CodePoint();
    }

    public static final short commonReadShort(@NotNull RealBufferedSource realBufferedSource) {
        Intrinsics.checkNotNullParameter(realBufferedSource, "");
        realBufferedSource.require(2L);
        return realBufferedSource.bufferField.readShort();
    }

    public static final short commonReadShortLe(@NotNull RealBufferedSource realBufferedSource) {
        Intrinsics.checkNotNullParameter(realBufferedSource, "");
        realBufferedSource.require(2L);
        return realBufferedSource.bufferField.readShortLe();
    }

    public static final int commonReadInt(@NotNull RealBufferedSource realBufferedSource) {
        Intrinsics.checkNotNullParameter(realBufferedSource, "");
        realBufferedSource.require(4L);
        return realBufferedSource.bufferField.readInt();
    }

    public static final int commonReadIntLe(@NotNull RealBufferedSource realBufferedSource) {
        Intrinsics.checkNotNullParameter(realBufferedSource, "");
        realBufferedSource.require(4L);
        return realBufferedSource.bufferField.readIntLe();
    }

    public static final long commonReadLong(@NotNull RealBufferedSource realBufferedSource) {
        Intrinsics.checkNotNullParameter(realBufferedSource, "");
        realBufferedSource.require(8L);
        return realBufferedSource.bufferField.readLong();
    }

    public static final long commonReadLongLe(@NotNull RealBufferedSource realBufferedSource) {
        Intrinsics.checkNotNullParameter(realBufferedSource, "");
        realBufferedSource.require(8L);
        return realBufferedSource.bufferField.readLongLe();
    }

    public static final long commonReadDecimalLong(@NotNull RealBufferedSource realBufferedSource) {
        Intrinsics.checkNotNullParameter(realBufferedSource, "");
        realBufferedSource.require(1L);
        long l2 = 0L;
        while (realBufferedSource.request(l2 + 1L)) {
            RealBufferedSource realBufferedSource2 = realBufferedSource;
            byte by = realBufferedSource2.bufferField.getByte(l2);
            if (!(by >= 48 && by <= 57 || l2 == 0L && by == 45)) {
                if (l2 != 0L) break;
                StringBuilder stringBuilder = new StringBuilder("Expected a digit or '-' but was 0x");
                String string = Integer.toString(by, CharsKt.checkRadix(CharsKt.checkRadix(16)));
                Intrinsics.checkNotNullExpressionValue(string, "");
                throw new NumberFormatException(stringBuilder.append(string).toString());
            }
            long l3 = l2;
            l2 = l3 + 1L;
        }
        RealBufferedSource realBufferedSource3 = realBufferedSource;
        return realBufferedSource3.bufferField.readDecimalLong();
    }

    public static final long commonReadHexadecimalUnsignedLong(@NotNull RealBufferedSource realBufferedSource) {
        Intrinsics.checkNotNullParameter(realBufferedSource, "");
        realBufferedSource.require(1L);
        int n2 = 0;
        while (realBufferedSource.request(n2 + 1)) {
            RealBufferedSource realBufferedSource2 = realBufferedSource;
            byte by = realBufferedSource2.bufferField.getByte(n2);
            if (!(by >= 48 && by <= 57 || by >= 97 && by <= 102 || by >= 65 && by <= 70)) {
                if (n2 != 0) break;
                StringBuilder stringBuilder = new StringBuilder("Expected leading [0-9a-fA-F] character but was 0x");
                String string = Integer.toString(by, CharsKt.checkRadix(CharsKt.checkRadix(16)));
                Intrinsics.checkNotNullExpressionValue(string, "");
                throw new NumberFormatException(stringBuilder.append(string).toString());
            }
            ++n2;
        }
        RealBufferedSource realBufferedSource3 = realBufferedSource;
        return realBufferedSource3.bufferField.readHexadecimalUnsignedLong();
    }

    public static final void commonSkip(@NotNull RealBufferedSource realBufferedSource, long l2) {
        long l3;
        Intrinsics.checkNotNullParameter(realBufferedSource, "");
        if (!(!realBufferedSource.closed)) {
            String string = "closed";
            throw new IllegalStateException(string.toString());
        }
        for (long i2 = l2; i2 > 0L; i2 -= l3) {
            RealBufferedSource realBufferedSource2 = realBufferedSource;
            if (realBufferedSource2.bufferField.size() == 0L) {
                realBufferedSource2 = realBufferedSource;
                if (realBufferedSource.source.read(realBufferedSource2.bufferField, 8192L) == -1L) {
                    throw new EOFException();
                }
            }
            RealBufferedSource realBufferedSource3 = realBufferedSource;
            long l4 = realBufferedSource3.bufferField.size();
            l3 = Math.min(i2, l4);
            realBufferedSource3 = realBufferedSource;
            realBufferedSource3.bufferField.skip(l3);
        }
    }

    public static final long commonIndexOf(@NotNull RealBufferedSource realBufferedSource, byte by, long l2, long l3) {
        Intrinsics.checkNotNullParameter(realBufferedSource, "");
        long l4 = l2;
        if (!(!realBufferedSource.closed)) {
            String string = "closed";
            throw new IllegalStateException(string.toString());
        }
        if (!(0L <= l2 ? l2 <= l3 : false)) {
            String string = "fromIndex=" + l2 + " toIndex=" + l3;
            throw new IllegalArgumentException(string.toString());
        }
        while (l4 < l3) {
            long l5;
            block8: {
                block7: {
                    RealBufferedSource realBufferedSource2 = realBufferedSource;
                    long l6 = realBufferedSource2.bufferField.indexOf(by, l4, l3);
                    if (l6 != -1L) {
                        return l6;
                    }
                    RealBufferedSource realBufferedSource3 = realBufferedSource;
                    l5 = realBufferedSource3.bufferField.size();
                    if (l5 >= l3) break block7;
                    realBufferedSource3 = realBufferedSource;
                    if (realBufferedSource.source.read(realBufferedSource3.bufferField, 8192L) != -1L) break block8;
                }
                return -1L;
            }
            l4 = Math.max(l4, l5);
        }
        return -1L;
    }

    public static final long commonIndexOf(@NotNull RealBufferedSource realBufferedSource, @NotNull ByteString byteString, long l2) {
        Intrinsics.checkNotNullParameter(realBufferedSource, "");
        Intrinsics.checkNotNullParameter(byteString, "");
        long l3 = l2;
        if (!(!realBufferedSource.closed)) {
            String string = "closed";
            throw new IllegalStateException(string.toString());
        }
        while (true) {
            RealBufferedSource realBufferedSource2 = realBufferedSource;
            long l4 = realBufferedSource2.bufferField.indexOf(byteString, l3);
            if (l4 != -1L) {
                return l4;
            }
            RealBufferedSource realBufferedSource3 = realBufferedSource;
            long l5 = realBufferedSource3.bufferField.size();
            realBufferedSource3 = realBufferedSource;
            if (realBufferedSource.source.read(realBufferedSource3.bufferField, 8192L) == -1L) {
                return -1L;
            }
            l3 = Math.max(l3, l5 - (long)byteString.size() + 1L);
        }
    }

    public static final long commonIndexOfElement(@NotNull RealBufferedSource realBufferedSource, @NotNull ByteString byteString, long l2) {
        Intrinsics.checkNotNullParameter(realBufferedSource, "");
        Intrinsics.checkNotNullParameter(byteString, "");
        long l3 = l2;
        if (!(!realBufferedSource.closed)) {
            String string = "closed";
            throw new IllegalStateException(string.toString());
        }
        while (true) {
            RealBufferedSource realBufferedSource2 = realBufferedSource;
            long l4 = realBufferedSource2.bufferField.indexOfElement(byteString, l3);
            if (l4 != -1L) {
                return l4;
            }
            RealBufferedSource realBufferedSource3 = realBufferedSource;
            long l5 = realBufferedSource3.bufferField.size();
            realBufferedSource3 = realBufferedSource;
            if (realBufferedSource.source.read(realBufferedSource3.bufferField, 8192L) == -1L) {
                return -1L;
            }
            l3 = Math.max(l3, l5);
        }
    }

    public static final boolean commonRangeEquals(@NotNull RealBufferedSource realBufferedSource, long l2, @NotNull ByteString byteString, int n2, int n3) {
        Intrinsics.checkNotNullParameter(realBufferedSource, "");
        Intrinsics.checkNotNullParameter(byteString, "");
        if (!(!realBufferedSource.closed)) {
            String string = "closed";
            throw new IllegalStateException(string.toString());
        }
        if (l2 < 0L || n2 < 0 || n3 < 0 || byteString.size() - n2 < n3) {
            return false;
        }
        for (int i2 = 0; i2 < n3; ++i2) {
            long l3 = l2 + (long)i2;
            if (!realBufferedSource.request(l3 + 1L)) {
                return false;
            }
            RealBufferedSource realBufferedSource2 = realBufferedSource;
            if (realBufferedSource2.bufferField.getByte(l3) == byteString.getByte(n2 + i2)) continue;
            return false;
        }
        return true;
    }

    @NotNull
    public static final BufferedSource commonPeek(@NotNull RealBufferedSource realBufferedSource) {
        Intrinsics.checkNotNullParameter(realBufferedSource, "");
        return Okio.buffer(new PeekSource(realBufferedSource));
    }

    public static final void commonClose(@NotNull RealBufferedSource realBufferedSource) {
        Intrinsics.checkNotNullParameter(realBufferedSource, "");
        if (realBufferedSource.closed) {
            return;
        }
        realBufferedSource.closed = true;
        realBufferedSource.source.close();
        realBufferedSource.bufferField.clear();
    }

    @NotNull
    public static final Timeout commonTimeout(@NotNull RealBufferedSource realBufferedSource) {
        Intrinsics.checkNotNullParameter(realBufferedSource, "");
        return realBufferedSource.source.timeout();
    }

    @NotNull
    public static final String commonToString(@NotNull RealBufferedSource realBufferedSource) {
        Intrinsics.checkNotNullParameter(realBufferedSource, "");
        return "buffer(" + realBufferedSource.source + ')';
    }
}


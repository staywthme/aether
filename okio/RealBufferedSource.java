/*
 * Decompiled with CFR 0.152.
 */
package okio;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import kotlin.Metadata;
import kotlin.jvm.JvmField;
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
import okio.Sink;
import okio.Source;
import okio.Timeout;
import okio.internal.-Buffer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000\u0086\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0005\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\n\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u0005\u001a\u00020\u0006H\u0016J\b\u0010\u000e\u001a\u00020\u000fH\u0016J\b\u0010\u0010\u001a\u00020\rH\u0016J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014H\u0016J\u0018\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0012H\u0016J \u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00122\u0006\u0010\u0016\u001a\u00020\u0012H\u0016J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0017\u001a\u00020\u0018H\u0016J\u0018\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0015\u001a\u00020\u0012H\u0016J\u0010\u0010\u0019\u001a\u00020\u00122\u0006\u0010\u001a\u001a\u00020\u0018H\u0016J\u0018\u0010\u0019\u001a\u00020\u00122\u0006\u0010\u001a\u001a\u00020\u00182\u0006\u0010\u0015\u001a\u00020\u0012H\u0016J\b\u0010\u001b\u001a\u00020\u001cH\u0016J\b\u0010\u001d\u001a\u00020\rH\u0016J\b\u0010\u001e\u001a\u00020\u0001H\u0016J\u0018\u0010\u001f\u001a\u00020\r2\u0006\u0010 \u001a\u00020\u00122\u0006\u0010\u0017\u001a\u00020\u0018H\u0016J(\u0010\u001f\u001a\u00020\r2\u0006\u0010 \u001a\u00020\u00122\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020\"H\u0016J\u0010\u0010$\u001a\u00020\"2\u0006\u0010%\u001a\u00020&H\u0016J\u0010\u0010$\u001a\u00020\"2\u0006\u0010%\u001a\u00020'H\u0016J \u0010$\u001a\u00020\"2\u0006\u0010%\u001a\u00020'2\u0006\u0010 \u001a\u00020\"2\u0006\u0010#\u001a\u00020\"H\u0016J\u0018\u0010$\u001a\u00020\u00122\u0006\u0010%\u001a\u00020\u00062\u0006\u0010#\u001a\u00020\u0012H\u0016J\u0010\u0010(\u001a\u00020\u00122\u0006\u0010%\u001a\u00020)H\u0016J\b\u0010*\u001a\u00020\u0014H\u0016J\b\u0010+\u001a\u00020'H\u0016J\u0010\u0010+\u001a\u00020'2\u0006\u0010#\u001a\u00020\u0012H\u0016J\b\u0010,\u001a\u00020\u0018H\u0016J\u0010\u0010,\u001a\u00020\u00182\u0006\u0010#\u001a\u00020\u0012H\u0016J\b\u0010-\u001a\u00020\u0012H\u0016J\u0010\u0010.\u001a\u00020\u000f2\u0006\u0010%\u001a\u00020'H\u0016J\u0018\u0010.\u001a\u00020\u000f2\u0006\u0010%\u001a\u00020\u00062\u0006\u0010#\u001a\u00020\u0012H\u0016J\b\u0010/\u001a\u00020\u0012H\u0016J\b\u00100\u001a\u00020\"H\u0016J\b\u00101\u001a\u00020\"H\u0016J\b\u00102\u001a\u00020\u0012H\u0016J\b\u00103\u001a\u00020\u0012H\u0016J\b\u00104\u001a\u000205H\u0016J\b\u00106\u001a\u000205H\u0016J\u0010\u00107\u001a\u0002082\u0006\u00109\u001a\u00020:H\u0016J\u0018\u00107\u001a\u0002082\u0006\u0010#\u001a\u00020\u00122\u0006\u00109\u001a\u00020:H\u0016J\b\u0010;\u001a\u000208H\u0016J\u0010\u0010;\u001a\u0002082\u0006\u0010#\u001a\u00020\u0012H\u0016J\b\u0010<\u001a\u00020\"H\u0016J\n\u0010=\u001a\u0004\u0018\u000108H\u0016J\b\u0010>\u001a\u000208H\u0016J\u0010\u0010>\u001a\u0002082\u0006\u0010?\u001a\u00020\u0012H\u0016J\u0010\u0010@\u001a\u00020\r2\u0006\u0010#\u001a\u00020\u0012H\u0016J\u0010\u0010A\u001a\u00020\u000f2\u0006\u0010#\u001a\u00020\u0012H\u0016J\u0010\u0010B\u001a\u00020\"2\u0006\u0010C\u001a\u00020DH\u0016J\u0010\u0010E\u001a\u00020\u000f2\u0006\u0010#\u001a\u00020\u0012H\u0016J\b\u0010F\u001a\u00020GH\u0016J\b\u0010H\u001a\u000208H\u0016R\u001b\u0010\u0005\u001a\u00020\u00068\u00d6\u0002X\u0096\u0004\u00a2\u0006\f\u0012\u0004\b\u0007\u0010\b\u001a\u0004\b\t\u0010\nR\u0010\u0010\u000b\u001a\u00020\u00068\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000R\u0012\u0010\f\u001a\u00020\r8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006I"}, d2={"Lokio/RealBufferedSource;", "Lokio/BufferedSource;", "source", "Lokio/Source;", "(Lokio/Source;)V", "buffer", "Lokio/Buffer;", "getBuffer$annotations", "()V", "getBuffer", "()Lokio/Buffer;", "bufferField", "closed", "", "close", "", "exhausted", "indexOf", "", "b", "", "fromIndex", "toIndex", "bytes", "Lokio/ByteString;", "indexOfElement", "targetBytes", "inputStream", "Ljava/io/InputStream;", "isOpen", "peek", "rangeEquals", "offset", "bytesOffset", "", "byteCount", "read", "sink", "Ljava/nio/ByteBuffer;", "", "readAll", "Lokio/Sink;", "readByte", "readByteArray", "readByteString", "readDecimalLong", "readFully", "readHexadecimalUnsignedLong", "readInt", "readIntLe", "readLong", "readLongLe", "readShort", "", "readShortLe", "readString", "", "charset", "Ljava/nio/charset/Charset;", "readUtf8", "readUtf8CodePoint", "readUtf8Line", "readUtf8LineStrict", "limit", "request", "require", "select", "options", "Lokio/Options;", "skip", "timeout", "Lokio/Timeout;", "toString", "okio"})
@SourceDebugExtension(value={"SMAP\nRealBufferedSource.kt\nKotlin\n*S Kotlin\n*F\n+ 1 RealBufferedSource.kt\nokio/RealBufferedSource\n+ 2 RealBufferedSource.kt\nokio/internal/-RealBufferedSource\n+ 3 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 4 Util.kt\nokio/-SegmentedByteString\n*L\n1#1,185:1\n62#1:191\n62#1:201\n62#1:208\n62#1:214\n62#1:216\n62#1:220\n62#1:225\n62#1:240\n62#1:244\n62#1:251\n62#1:264\n62#1:272\n62#1:273\n62#1:274\n62#1:280\n62#1:288\n62#1:301\n62#1:305\n62#1:306\n62#1:307\n62#1:308\n62#1:313\n62#1:325\n62#1:341\n62#1:351\n62#1:354\n62#1:357\n62#1:360\n62#1:363\n62#1:366\n62#1:372\n62#1:389\n62#1:409\n62#1:424\n62#1:441\n62#1:454\n62#1:475\n62#1:482\n38#2:186\n39#2,3:188\n42#2,6:192\n51#2:198\n52#2:200\n56#2,2:202\n60#2:204\n61#2,2:206\n63#2,3:209\n69#2,2:212\n74#2:215\n75#2:217\n79#2,2:218\n84#2:221\n86#2,2:223\n88#2,13:226\n107#2:239\n108#2:241\n112#2,2:242\n117#2,6:245\n123#2,9:252\n134#2,3:261\n137#2,5:265\n142#2:271\n146#2,5:275\n151#2,5:281\n158#2,2:286\n160#2,11:289\n174#2:300\n175#2:302\n179#2,2:303\n184#2,4:309\n188#2,6:314\n198#2:320\n199#2,3:322\n202#2,8:326\n210#2,3:335\n217#2,3:338\n220#2,7:342\n230#2,2:349\n235#2,2:352\n240#2,2:355\n245#2,2:358\n250#2,2:361\n255#2,2:364\n260#2,5:367\n265#2,11:373\n279#2,5:384\n284#2,14:390\n301#2,2:404\n303#2,2:407\n305#2,7:410\n314#2,2:417\n316#2,4:420\n320#2,11:425\n334#2,2:436\n337#2,2:439\n339#2,7:442\n350#2,2:449\n353#2,2:452\n355#2,7:455\n371#2:462\n373#2,11:464\n385#2:476\n389#2:477\n393#2,4:478\n397#2:483\n399#2:484\n401#2:485\n1#3:187\n1#3:199\n1#3:205\n1#3:222\n1#3:321\n1#3:406\n1#3:419\n1#3:438\n1#3:451\n1#3:463\n89#4:270\n89#4:334\n*S KotlinDebug\n*F\n+ 1 RealBufferedSource.kt\nokio/RealBufferedSource\n*L\n66#1:191\n67#1:201\n69#1:208\n70#1:214\n71#1:216\n72#1:220\n73#1:225\n74#1:240\n75#1:244\n77#1:251\n79#1:264\n82#1:272\n83#1:273\n87#1:274\n90#1:280\n91#1:288\n92#1:301\n93#1:305\n96#1:306\n97#1:307\n102#1:308\n105#1:313\n107#1:325\n108#1:341\n109#1:351\n110#1:354\n111#1:357\n112#1:360\n113#1:363\n114#1:366\n115#1:372\n116#1:389\n117#1:409\n121#1:424\n124#1:441\n127#1:454\n141#1:475\n181#1:482\n66#1:186\n66#1:188,3\n66#1:192,6\n67#1:198\n67#1:200\n68#1:202,2\n69#1:204\n69#1:206,2\n69#1:209,3\n70#1:212,2\n71#1:215\n71#1:217\n72#1:218,2\n73#1:221\n73#1:223,2\n73#1:226,13\n74#1:239\n74#1:241\n75#1:242,2\n77#1:245,6\n77#1:252,9\n79#1:261,3\n79#1:265,5\n79#1:271\n90#1:275,5\n90#1:281,5\n91#1:286,2\n91#1:289,11\n92#1:300\n92#1:302\n93#1:303,2\n105#1:309,4\n105#1:314,6\n107#1:320\n107#1:322,3\n107#1:326,8\n107#1:335,3\n108#1:338,3\n108#1:342,7\n109#1:349,2\n110#1:352,2\n111#1:355,2\n112#1:358,2\n113#1:361,2\n114#1:364,2\n115#1:367,5\n115#1:373,11\n116#1:384,5\n116#1:390,14\n117#1:404,2\n117#1:407,2\n117#1:410,7\n121#1:417,2\n121#1:420,4\n121#1:425,11\n124#1:436,2\n124#1:439,2\n124#1:442,7\n127#1:449,2\n127#1:452,2\n127#1:455,7\n141#1:462\n141#1:464,11\n141#1:476\n143#1:477\n181#1:478,4\n181#1:483\n182#1:484\n183#1:485\n66#1:187\n67#1:199\n69#1:205\n73#1:222\n107#1:321\n117#1:406\n121#1:419\n124#1:438\n127#1:451\n141#1:463\n79#1:270\n107#1:334\n*E\n"})
public final class RealBufferedSource
implements BufferedSource {
    @JvmField
    @NotNull
    public final Source source;
    @JvmField
    @NotNull
    public final Buffer bufferField;
    @JvmField
    public boolean closed;

    public RealBufferedSource(@NotNull Source source2) {
        Intrinsics.checkNotNullParameter(source2, "");
        this.source = source2;
        this.bufferField = new Buffer();
    }

    @Override
    @NotNull
    public final Buffer getBuffer() {
        return this.bufferField;
    }

    public static /* synthetic */ void getBuffer$annotations() {
    }

    @Override
    @NotNull
    public final Buffer buffer() {
        return this.bufferField;
    }

    @Override
    public final long read(@NotNull Buffer object, long l2) {
        long l3;
        Intrinsics.checkNotNullParameter(object, "");
        RealBufferedSource realBufferedSource = this;
        if (!(l2 >= 0L)) {
            object = "byteCount < 0: " + l2;
            throw new IllegalArgumentException(object.toString());
        }
        if (!(!realBufferedSource.closed)) {
            object = "closed";
            throw new IllegalStateException(object.toString());
        }
        RealBufferedSource realBufferedSource2 = realBufferedSource;
        if (realBufferedSource2.bufferField.size() == 0L) {
            realBufferedSource2 = realBufferedSource;
            l3 = realBufferedSource.source.read(realBufferedSource2.bufferField, 8192L);
            if (l3 == -1L) {
                return -1L;
            }
        }
        realBufferedSource2 = realBufferedSource;
        long l4 = realBufferedSource2.bufferField.size();
        l3 = Math.min(l2, l4);
        realBufferedSource2 = realBufferedSource;
        return realBufferedSource2.bufferField.read((Buffer)object, l3);
    }

    @Override
    public final boolean exhausted() {
        Object object = this;
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

    @Override
    public final void require(long l2) {
        RealBufferedSource realBufferedSource = this;
        if (!realBufferedSource.request(l2)) {
            throw new EOFException();
        }
    }

    @Override
    public final boolean request(long l2) {
        block3: {
            RealBufferedSource realBufferedSource;
            RealBufferedSource realBufferedSource2 = this;
            if (!(l2 >= 0L)) {
                String string = "byteCount < 0: " + l2;
                throw new IllegalArgumentException(string.toString());
            }
            if (!(!realBufferedSource2.closed)) {
                String string = "closed";
                throw new IllegalStateException(string.toString());
            }
            do {
                realBufferedSource = realBufferedSource2;
                if (realBufferedSource.bufferField.size() >= l2) break block3;
                realBufferedSource = realBufferedSource2;
            } while (realBufferedSource2.source.read(realBufferedSource.bufferField, 8192L) != -1L);
            return false;
        }
        return true;
    }

    @Override
    public final byte readByte() {
        RealBufferedSource realBufferedSource = this;
        realBufferedSource.require(1L);
        return realBufferedSource.bufferField.readByte();
    }

    @Override
    @NotNull
    public final ByteString readByteString() {
        RealBufferedSource realBufferedSource;
        RealBufferedSource realBufferedSource2 = realBufferedSource = this;
        realBufferedSource.bufferField.writeAll(realBufferedSource.source);
        realBufferedSource2 = realBufferedSource;
        return realBufferedSource2.bufferField.readByteString();
    }

    @Override
    @NotNull
    public final ByteString readByteString(long l2) {
        RealBufferedSource realBufferedSource = this;
        realBufferedSource.require(l2);
        return realBufferedSource.bufferField.readByteString(l2);
    }

    @Override
    public final int select(@NotNull Options object) {
        int n2;
        Intrinsics.checkNotNullParameter(object, "");
        RealBufferedSource realBufferedSource = this;
        if (!(!realBufferedSource.closed)) {
            String string = "closed";
            throw new IllegalStateException(string.toString());
        }
        block4: while (true) {
            RealBufferedSource realBufferedSource2 = realBufferedSource;
            n2 = -Buffer.selectPrefix(realBufferedSource2.bufferField, (Options)object, true);
            switch (n2) {
                case -1: {
                    return -1;
                }
                case -2: {
                    realBufferedSource2 = realBufferedSource;
                    if (realBufferedSource.source.read(realBufferedSource2.bufferField, 8192L) != -1L) continue block4;
                    return -1;
                }
            }
            break;
        }
        int n3 = ((Options)object).getByteStrings$okio()[n2].size();
        object = realBufferedSource;
        ((RealBufferedSource)object).bufferField.skip(n3);
        return n2;
    }

    @Override
    @NotNull
    public final byte[] readByteArray() {
        RealBufferedSource realBufferedSource;
        RealBufferedSource realBufferedSource2 = realBufferedSource = this;
        realBufferedSource.bufferField.writeAll(realBufferedSource.source);
        realBufferedSource2 = realBufferedSource;
        return realBufferedSource2.bufferField.readByteArray();
    }

    @Override
    @NotNull
    public final byte[] readByteArray(long l2) {
        RealBufferedSource realBufferedSource = this;
        realBufferedSource.require(l2);
        return realBufferedSource.bufferField.readByteArray(l2);
    }

    @Override
    public final int read(@NotNull byte[] byArray) {
        Intrinsics.checkNotNullParameter(byArray, "");
        return this.read(byArray, 0, byArray.length);
    }

    @Override
    public final void readFully(@NotNull byte[] byArray) {
        Intrinsics.checkNotNullParameter(byArray, "");
        RealBufferedSource realBufferedSource = this;
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

    @Override
    public final int read(@NotNull byte[] byArray, int n2, int n3) {
        Intrinsics.checkNotNullParameter(byArray, "");
        RealBufferedSource realBufferedSource = this;
        -SegmentedByteString.checkOffsetAndCount(byArray.length, n2, n3);
        RealBufferedSource realBufferedSource2 = realBufferedSource;
        if (realBufferedSource2.bufferField.size() == 0L) {
            realBufferedSource2 = realBufferedSource;
            long l2 = realBufferedSource.source.read(realBufferedSource2.bufferField, 8192L);
            if (l2 == -1L) {
                return -1;
            }
        }
        RealBufferedSource realBufferedSource3 = realBufferedSource;
        long l3 = realBufferedSource3.bufferField.size();
        int n4 = (int)Math.min((long)n3, l3);
        realBufferedSource3 = realBufferedSource;
        return realBufferedSource3.bufferField.read(byArray, n2, n4);
    }

    @Override
    public final int read(@NotNull ByteBuffer byteBuffer) {
        Intrinsics.checkNotNullParameter(byteBuffer, "");
        RealBufferedSource realBufferedSource = this;
        if (realBufferedSource.bufferField.size() == 0L) {
            realBufferedSource = this;
            long l2 = this.source.read(realBufferedSource.bufferField, 8192L);
            if (l2 == -1L) {
                return -1;
            }
        }
        realBufferedSource = this;
        return realBufferedSource.bufferField.read(byteBuffer);
    }

    @Override
    public final void readFully(@NotNull Buffer bufferedSource, long l2) {
        Intrinsics.checkNotNullParameter(bufferedSource, "");
        RealBufferedSource realBufferedSource = this;
        try {
            realBufferedSource.require(l2);
        }
        catch (EOFException eOFException) {
            Buffer buffer = bufferedSource;
            bufferedSource = realBufferedSource;
            buffer.writeAll(((RealBufferedSource)bufferedSource).bufferField);
            throw eOFException;
        }
        RealBufferedSource realBufferedSource2 = realBufferedSource;
        realBufferedSource2.bufferField.readFully((Buffer)bufferedSource, l2);
    }

    @Override
    public final long readAll(@NotNull Sink sink2) {
        RealBufferedSource realBufferedSource;
        Intrinsics.checkNotNullParameter(sink2, "");
        RealBufferedSource realBufferedSource2 = this;
        long l2 = 0L;
        while (true) {
            realBufferedSource = realBufferedSource2;
            if (realBufferedSource2.source.read(realBufferedSource.bufferField, 8192L) == -1L) break;
            realBufferedSource = realBufferedSource2;
            long l3 = realBufferedSource.bufferField.completeSegmentByteCount();
            if (l3 <= 0L) continue;
            l2 += l3;
            realBufferedSource = realBufferedSource2;
            sink2.write(realBufferedSource.bufferField, l3);
        }
        realBufferedSource = realBufferedSource2;
        if (realBufferedSource.bufferField.size() > 0L) {
            realBufferedSource = realBufferedSource2;
            l2 += realBufferedSource.bufferField.size();
            RealBufferedSource realBufferedSource3 = realBufferedSource2;
            realBufferedSource = realBufferedSource3;
            realBufferedSource = realBufferedSource2;
            sink2.write(realBufferedSource3.bufferField, realBufferedSource.bufferField.size());
        }
        return l2;
    }

    @Override
    @NotNull
    public final String readUtf8() {
        RealBufferedSource realBufferedSource;
        RealBufferedSource realBufferedSource2 = realBufferedSource = this;
        realBufferedSource.bufferField.writeAll(realBufferedSource.source);
        realBufferedSource2 = realBufferedSource;
        return realBufferedSource2.bufferField.readUtf8();
    }

    @Override
    @NotNull
    public final String readUtf8(long l2) {
        RealBufferedSource realBufferedSource = this;
        realBufferedSource.require(l2);
        return realBufferedSource.bufferField.readUtf8(l2);
    }

    @Override
    @NotNull
    public final String readString(@NotNull Charset charset) {
        Intrinsics.checkNotNullParameter(charset, "");
        RealBufferedSource realBufferedSource = this;
        realBufferedSource.bufferField.writeAll(this.source);
        realBufferedSource = this;
        return realBufferedSource.bufferField.readString(charset);
    }

    @Override
    @NotNull
    public final String readString(long l2, @NotNull Charset charset) {
        Intrinsics.checkNotNullParameter(charset, "");
        this.require(l2);
        RealBufferedSource realBufferedSource = this;
        return realBufferedSource.bufferField.readString(l2, charset);
    }

    @Override
    @Nullable
    public final String readUtf8Line() {
        RealBufferedSource realBufferedSource = this;
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

    @Override
    @NotNull
    public final String readUtf8LineStrict() {
        return this.readUtf8LineStrict(Long.MAX_VALUE);
    }

    @Override
    @NotNull
    public final String readUtf8LineStrict(long l2) {
        BufferedSource bufferedSource;
        RealBufferedSource realBufferedSource = this;
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
        RealBufferedSource realBufferedSource4 = realBufferedSource3;
        realBufferedSource4 = realBufferedSource;
        long l5 = realBufferedSource4.bufferField.size();
        realBufferedSource3.bufferField.copyTo((Buffer)bufferedSource, 0L, Math.min(32L, l5));
        realBufferedSource4 = realBufferedSource;
        throw new EOFException("\\n not found: limit=" + Math.min(realBufferedSource4.bufferField.size(), l2) + " content=" + ((Buffer)bufferedSource).readByteString().hex() + '\u2026');
    }

    @Override
    public final int readUtf8CodePoint() {
        RealBufferedSource realBufferedSource = this;
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

    @Override
    public final short readShort() {
        RealBufferedSource realBufferedSource = this;
        realBufferedSource.require(2L);
        return realBufferedSource.bufferField.readShort();
    }

    @Override
    public final short readShortLe() {
        RealBufferedSource realBufferedSource = this;
        realBufferedSource.require(2L);
        return realBufferedSource.bufferField.readShortLe();
    }

    @Override
    public final int readInt() {
        RealBufferedSource realBufferedSource = this;
        realBufferedSource.require(4L);
        return realBufferedSource.bufferField.readInt();
    }

    @Override
    public final int readIntLe() {
        RealBufferedSource realBufferedSource = this;
        realBufferedSource.require(4L);
        return realBufferedSource.bufferField.readIntLe();
    }

    @Override
    public final long readLong() {
        RealBufferedSource realBufferedSource = this;
        realBufferedSource.require(8L);
        return realBufferedSource.bufferField.readLong();
    }

    @Override
    public final long readLongLe() {
        RealBufferedSource realBufferedSource = this;
        realBufferedSource.require(8L);
        return realBufferedSource.bufferField.readLongLe();
    }

    @Override
    public final long readDecimalLong() {
        RealBufferedSource realBufferedSource = this;
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
            ++l2;
        }
        RealBufferedSource realBufferedSource3 = realBufferedSource;
        return realBufferedSource3.bufferField.readDecimalLong();
    }

    @Override
    public final long readHexadecimalUnsignedLong() {
        RealBufferedSource realBufferedSource = this;
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

    @Override
    public final void skip(long l2) {
        long l3;
        RealBufferedSource realBufferedSource = this;
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
            realBufferedSource2 = realBufferedSource;
            long l4 = realBufferedSource2.bufferField.size();
            l3 = Math.min(i2, l4);
            realBufferedSource2 = realBufferedSource;
            realBufferedSource2.bufferField.skip(l3);
        }
    }

    @Override
    public final long indexOf(byte by) {
        return this.indexOf(by, 0L, Long.MAX_VALUE);
    }

    @Override
    public final long indexOf(byte by, long l2) {
        return this.indexOf(by, l2, Long.MAX_VALUE);
    }

    @Override
    public final long indexOf(byte by, long l2, long l3) {
        RealBufferedSource realBufferedSource = this;
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
                    realBufferedSource2 = realBufferedSource;
                    l5 = realBufferedSource2.bufferField.size();
                    if (l5 >= l3) break block7;
                    realBufferedSource2 = realBufferedSource;
                    if (realBufferedSource.source.read(realBufferedSource2.bufferField, 8192L) != -1L) break block8;
                }
                return -1L;
            }
            l4 = Math.max(l4, l5);
        }
        return -1L;
    }

    @Override
    public final long indexOf(@NotNull ByteString byteString) {
        Intrinsics.checkNotNullParameter(byteString, "");
        return this.indexOf(byteString, 0L);
    }

    @Override
    public final long indexOf(@NotNull ByteString object, long l2) {
        Intrinsics.checkNotNullParameter(object, "");
        RealBufferedSource realBufferedSource = this;
        long l3 = l2;
        if (!(!realBufferedSource.closed)) {
            object = "closed";
            throw new IllegalStateException(object.toString());
        }
        while (true) {
            RealBufferedSource realBufferedSource2 = realBufferedSource;
            long l4 = realBufferedSource2.bufferField.indexOf((ByteString)object, l3);
            if (l4 != -1L) {
                return l4;
            }
            realBufferedSource2 = realBufferedSource;
            long l5 = realBufferedSource2.bufferField.size();
            realBufferedSource2 = realBufferedSource;
            if (realBufferedSource.source.read(realBufferedSource2.bufferField, 8192L) == -1L) {
                return -1L;
            }
            l3 = Math.max(l3, l5 - (long)((ByteString)object).size() + 1L);
        }
    }

    @Override
    public final long indexOfElement(@NotNull ByteString byteString) {
        Intrinsics.checkNotNullParameter(byteString, "");
        return this.indexOfElement(byteString, 0L);
    }

    @Override
    public final long indexOfElement(@NotNull ByteString object, long l2) {
        Intrinsics.checkNotNullParameter(object, "");
        RealBufferedSource realBufferedSource = this;
        long l3 = l2;
        if (!(!realBufferedSource.closed)) {
            object = "closed";
            throw new IllegalStateException(object.toString());
        }
        while (true) {
            RealBufferedSource realBufferedSource2 = realBufferedSource;
            long l4 = realBufferedSource2.bufferField.indexOfElement((ByteString)object, l3);
            if (l4 != -1L) {
                return l4;
            }
            realBufferedSource2 = realBufferedSource;
            long l5 = realBufferedSource2.bufferField.size();
            realBufferedSource2 = realBufferedSource;
            if (realBufferedSource.source.read(realBufferedSource2.bufferField, 8192L) == -1L) {
                return -1L;
            }
            l3 = Math.max(l3, l5);
        }
    }

    @Override
    public final boolean rangeEquals(long l2, @NotNull ByteString byteString) {
        Intrinsics.checkNotNullParameter(byteString, "");
        return this.rangeEquals(l2, byteString, 0, byteString.size());
    }

    @Override
    public final boolean rangeEquals(long l2, @NotNull ByteString byteString, int n2, int n3) {
        Intrinsics.checkNotNullParameter(byteString, "");
        RealBufferedSource realBufferedSource = this;
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

    @Override
    @NotNull
    public final BufferedSource peek() {
        RealBufferedSource realBufferedSource = this;
        return Okio.buffer(new PeekSource(realBufferedSource));
    }

    @Override
    @NotNull
    public final InputStream inputStream() {
        return new InputStream(this){
            final /* synthetic */ RealBufferedSource this$0;
            {
                this.this$0 = realBufferedSource;
            }

            public final int read() {
                if (this.this$0.closed) {
                    throw new IOException("closed");
                }
                RealBufferedSource realBufferedSource = this.this$0;
                if (realBufferedSource.bufferField.size() == 0L) {
                    realBufferedSource = this.this$0;
                    long l2 = this.this$0.source.read(realBufferedSource.bufferField, 8192L);
                    if (l2 == -1L) {
                        return -1;
                    }
                }
                realBufferedSource = this.this$0;
                byte by = realBufferedSource.bufferField.readByte();
                return by & 0xFF;
            }

            public final int read(@NotNull byte[] byArray, int n2, int n3) {
                Intrinsics.checkNotNullParameter(byArray, "");
                if (this.this$0.closed) {
                    throw new IOException("closed");
                }
                -SegmentedByteString.checkOffsetAndCount(byArray.length, n2, n3);
                RealBufferedSource realBufferedSource = this.this$0;
                if (realBufferedSource.bufferField.size() == 0L) {
                    realBufferedSource = this.this$0;
                    long l2 = this.this$0.source.read(realBufferedSource.bufferField, 8192L);
                    if (l2 == -1L) {
                        return -1;
                    }
                }
                realBufferedSource = this.this$0;
                return realBufferedSource.bufferField.read(byArray, n2, n3);
            }

            public final int available() {
                if (this.this$0.closed) {
                    throw new IOException("closed");
                }
                RealBufferedSource realBufferedSource = this.this$0;
                long l2 = realBufferedSource.bufferField.size();
                return (int)Math.min(l2, Integer.MAX_VALUE);
            }

            public final void close() {
                this.this$0.close();
            }

            @NotNull
            public final String toString() {
                return this.this$0 + ".inputStream()";
            }
        };
    }

    @Override
    public final boolean isOpen() {
        return !this.closed;
    }

    @Override
    public final void close() {
        RealBufferedSource realBufferedSource = this;
        if (!realBufferedSource.closed) {
            realBufferedSource.closed = true;
            realBufferedSource.source.close();
            realBufferedSource.bufferField.clear();
        }
    }

    @Override
    @NotNull
    public final Timeout timeout() {
        RealBufferedSource realBufferedSource = this;
        return realBufferedSource.source.timeout();
    }

    @NotNull
    public final String toString() {
        RealBufferedSource realBufferedSource = this;
        return "buffer(" + realBufferedSource.source + ')';
    }
}


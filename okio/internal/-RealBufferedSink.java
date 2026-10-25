/*
 * Decompiled with CFR 0.152.
 */
package okio.internal;

import java.io.EOFException;
import kotlin.Metadata;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import okio.Buffer;
import okio.BufferedSink;
import okio.ByteString;
import okio.RealBufferedSink;
import okio.Source;
import okio.Timeout;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 9, 0}, k=2, xi=48, d1={"\u0000D\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0015\u001a\r\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u0080\b\u001a\r\u0010\u0003\u001a\u00020\u0004*\u00020\u0002H\u0080\b\u001a\r\u0010\u0005\u001a\u00020\u0004*\u00020\u0002H\u0080\b\u001a\r\u0010\u0006\u001a\u00020\u0001*\u00020\u0002H\u0080\b\u001a\r\u0010\u0007\u001a\u00020\b*\u00020\u0002H\u0080\b\u001a\r\u0010\t\u001a\u00020\n*\u00020\u0002H\u0080\b\u001a\u0015\u0010\u000b\u001a\u00020\u0004*\u00020\u00022\u0006\u0010\f\u001a\u00020\rH\u0080\b\u001a%\u0010\u000b\u001a\u00020\u0004*\u00020\u00022\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000fH\u0080\b\u001a\u001d\u0010\u000b\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\f\u001a\u00020\u00112\u0006\u0010\u0010\u001a\u00020\u0012H\u0080\b\u001a\u0015\u0010\u000b\u001a\u00020\u0004*\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u0014H\u0080\b\u001a%\u0010\u000b\u001a\u00020\u0004*\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000fH\u0080\b\u001a\u001d\u0010\u000b\u001a\u00020\u0004*\u00020\u00022\u0006\u0010\f\u001a\u00020\u00152\u0006\u0010\u0010\u001a\u00020\u0012H\u0080\b\u001a\u0015\u0010\u0016\u001a\u00020\u0012*\u00020\u00022\u0006\u0010\f\u001a\u00020\u0015H\u0080\b\u001a\u0015\u0010\u0017\u001a\u00020\u0004*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u000fH\u0080\b\u001a\u0015\u0010\u0019\u001a\u00020\u0004*\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u0012H\u0080\b\u001a\u0015\u0010\u001b\u001a\u00020\u0004*\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u0012H\u0080\b\u001a\u0015\u0010\u001c\u001a\u00020\u0004*\u00020\u00022\u0006\u0010\u001d\u001a\u00020\u000fH\u0080\b\u001a\u0015\u0010\u001e\u001a\u00020\u0004*\u00020\u00022\u0006\u0010\u001d\u001a\u00020\u000fH\u0080\b\u001a\u0015\u0010\u001f\u001a\u00020\u0004*\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u0012H\u0080\b\u001a\u0015\u0010 \u001a\u00020\u0004*\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u0012H\u0080\b\u001a\u0015\u0010!\u001a\u00020\u0004*\u00020\u00022\u0006\u0010\"\u001a\u00020\u000fH\u0080\b\u001a\u0015\u0010#\u001a\u00020\u0004*\u00020\u00022\u0006\u0010\"\u001a\u00020\u000fH\u0080\b\u001a\u0015\u0010$\u001a\u00020\u0004*\u00020\u00022\u0006\u0010%\u001a\u00020\nH\u0080\b\u001a%\u0010$\u001a\u00020\u0004*\u00020\u00022\u0006\u0010%\u001a\u00020\n2\u0006\u0010&\u001a\u00020\u000f2\u0006\u0010'\u001a\u00020\u000fH\u0080\b\u001a\u0015\u0010(\u001a\u00020\u0004*\u00020\u00022\u0006\u0010)\u001a\u00020\u000fH\u0080\b\u00a8\u0006*"}, d2={"commonClose", "", "Lokio/RealBufferedSink;", "commonEmit", "Lokio/BufferedSink;", "commonEmitCompleteSegments", "commonFlush", "commonTimeout", "Lokio/Timeout;", "commonToString", "", "commonWrite", "source", "", "offset", "", "byteCount", "Lokio/Buffer;", "", "byteString", "Lokio/ByteString;", "Lokio/Source;", "commonWriteAll", "commonWriteByte", "b", "commonWriteDecimalLong", "v", "commonWriteHexadecimalUnsignedLong", "commonWriteInt", "i", "commonWriteIntLe", "commonWriteLong", "commonWriteLongLe", "commonWriteShort", "s", "commonWriteShortLe", "commonWriteUtf8", "string", "beginIndex", "endIndex", "commonWriteUtf8CodePoint", "codePoint", "okio"})
@JvmName(name="-RealBufferedSink")
@SourceDebugExtension(value={"SMAP\nRealBufferedSink.kt\nKotlin\n*S Kotlin\n*F\n+ 1 RealBufferedSink.kt\nokio/internal/-RealBufferedSink\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 RealBufferedSink.kt\nokio/RealBufferedSink\n*L\n1#1,219:1\n1#2:220\n51#3:221\n51#3:222\n51#3:223\n51#3:224\n51#3:225\n51#3:226\n51#3:227\n51#3:228\n51#3:229\n51#3:230\n51#3:231\n51#3:232\n51#3:233\n51#3:234\n51#3:235\n51#3:236\n51#3:237\n51#3:238\n51#3:239\n51#3:240\n51#3:241\n51#3:242\n51#3:243\n51#3:244\n51#3:245\n51#3:246\n51#3:247\n*S KotlinDebug\n*F\n+ 1 RealBufferedSink.kt\nokio/internal/-RealBufferedSink\n*L\n35#1:221\n41#1:222\n51#1:223\n57#1:224\n67#1:225\n73#1:226\n79#1:227\n89#1:228\n96#1:229\n107#1:230\n117#1:231\n123#1:232\n129#1:233\n135#1:234\n141#1:235\n147#1:236\n153#1:237\n159#1:238\n165#1:239\n171#1:240\n172#1:241\n178#1:242\n179#1:243\n185#1:244\n186#1:245\n198#1:246\n199#1:247\n*E\n"})
public final class -RealBufferedSink {
    public static final void commonWrite(@NotNull RealBufferedSink object, @NotNull Buffer buffer, long l2) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(buffer, "");
        if (!(!((RealBufferedSink)object).closed)) {
            object = "closed";
            throw new IllegalStateException(object.toString());
        }
        RealBufferedSink realBufferedSink = object;
        realBufferedSink.bufferField.write(buffer, l2);
        ((RealBufferedSink)object).emitCompleteSegments();
    }

    @NotNull
    public static final BufferedSink commonWrite(@NotNull RealBufferedSink object, @NotNull ByteString byteString) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(byteString, "");
        if (!(!((RealBufferedSink)object).closed)) {
            object = "closed";
            throw new IllegalStateException(object.toString());
        }
        RealBufferedSink realBufferedSink = object;
        realBufferedSink.bufferField.write(byteString);
        return ((RealBufferedSink)object).emitCompleteSegments();
    }

    @NotNull
    public static final BufferedSink commonWrite(@NotNull RealBufferedSink object, @NotNull ByteString byteString, int n2, int n3) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(byteString, "");
        if (!(!((RealBufferedSink)object).closed)) {
            object = "closed";
            throw new IllegalStateException(object.toString());
        }
        RealBufferedSink realBufferedSink = object;
        realBufferedSink.bufferField.write(byteString, n2, n3);
        return ((RealBufferedSink)object).emitCompleteSegments();
    }

    @NotNull
    public static final BufferedSink commonWriteUtf8(@NotNull RealBufferedSink object, @NotNull String string) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(string, "");
        if (!(!((RealBufferedSink)object).closed)) {
            object = "closed";
            throw new IllegalStateException(object.toString());
        }
        RealBufferedSink realBufferedSink = object;
        realBufferedSink.bufferField.writeUtf8(string);
        return ((RealBufferedSink)object).emitCompleteSegments();
    }

    @NotNull
    public static final BufferedSink commonWriteUtf8(@NotNull RealBufferedSink object, @NotNull String string, int n2, int n3) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(string, "");
        if (!(!((RealBufferedSink)object).closed)) {
            object = "closed";
            throw new IllegalStateException(object.toString());
        }
        RealBufferedSink realBufferedSink = object;
        realBufferedSink.bufferField.writeUtf8(string, n2, n3);
        return ((RealBufferedSink)object).emitCompleteSegments();
    }

    @NotNull
    public static final BufferedSink commonWriteUtf8CodePoint(@NotNull RealBufferedSink object, int n2) {
        Intrinsics.checkNotNullParameter(object, "");
        if (!(!((RealBufferedSink)object).closed)) {
            object = "closed";
            throw new IllegalStateException(object.toString());
        }
        RealBufferedSink realBufferedSink = object;
        realBufferedSink.bufferField.writeUtf8CodePoint(n2);
        return ((RealBufferedSink)object).emitCompleteSegments();
    }

    @NotNull
    public static final BufferedSink commonWrite(@NotNull RealBufferedSink object, @NotNull byte[] byArray) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(byArray, "");
        if (!(!((RealBufferedSink)object).closed)) {
            object = "closed";
            throw new IllegalStateException(object.toString());
        }
        RealBufferedSink realBufferedSink = object;
        realBufferedSink.bufferField.write(byArray);
        return ((RealBufferedSink)object).emitCompleteSegments();
    }

    @NotNull
    public static final BufferedSink commonWrite(@NotNull RealBufferedSink object, @NotNull byte[] byArray, int n2, int n3) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(byArray, "");
        if (!(!((RealBufferedSink)object).closed)) {
            object = "closed";
            throw new IllegalStateException(object.toString());
        }
        RealBufferedSink realBufferedSink = object;
        realBufferedSink.bufferField.write(byArray, n2, n3);
        return ((RealBufferedSink)object).emitCompleteSegments();
    }

    public static final long commonWriteAll(@NotNull RealBufferedSink realBufferedSink, @NotNull Source source2) {
        Intrinsics.checkNotNullParameter(realBufferedSink, "");
        Intrinsics.checkNotNullParameter(source2, "");
        long l2 = 0L;
        while (true) {
            RealBufferedSink realBufferedSink2 = realBufferedSink;
            long l3 = source2.read(realBufferedSink2.bufferField, 8192L);
            if (l3 == -1L) break;
            l2 += l3;
            realBufferedSink.emitCompleteSegments();
        }
        return l2;
    }

    @NotNull
    public static final BufferedSink commonWrite(@NotNull RealBufferedSink realBufferedSink, @NotNull Source source2, long l2) {
        long l3;
        Intrinsics.checkNotNullParameter(realBufferedSink, "");
        Intrinsics.checkNotNullParameter(source2, "");
        for (long i2 = l2; i2 > 0L; i2 -= l3) {
            RealBufferedSink realBufferedSink2 = realBufferedSink;
            l3 = source2.read(realBufferedSink2.bufferField, i2);
            if (l3 == -1L) {
                throw new EOFException();
            }
            realBufferedSink.emitCompleteSegments();
        }
        return realBufferedSink;
    }

    @NotNull
    public static final BufferedSink commonWriteByte(@NotNull RealBufferedSink object, int n2) {
        Intrinsics.checkNotNullParameter(object, "");
        if (!(!((RealBufferedSink)object).closed)) {
            object = "closed";
            throw new IllegalStateException(object.toString());
        }
        RealBufferedSink realBufferedSink = object;
        realBufferedSink.bufferField.writeByte(n2);
        return ((RealBufferedSink)object).emitCompleteSegments();
    }

    @NotNull
    public static final BufferedSink commonWriteShort(@NotNull RealBufferedSink object, int n2) {
        Intrinsics.checkNotNullParameter(object, "");
        if (!(!((RealBufferedSink)object).closed)) {
            object = "closed";
            throw new IllegalStateException(object.toString());
        }
        RealBufferedSink realBufferedSink = object;
        realBufferedSink.bufferField.writeShort(n2);
        return ((RealBufferedSink)object).emitCompleteSegments();
    }

    @NotNull
    public static final BufferedSink commonWriteShortLe(@NotNull RealBufferedSink object, int n2) {
        Intrinsics.checkNotNullParameter(object, "");
        if (!(!((RealBufferedSink)object).closed)) {
            object = "closed";
            throw new IllegalStateException(object.toString());
        }
        RealBufferedSink realBufferedSink = object;
        realBufferedSink.bufferField.writeShortLe(n2);
        return ((RealBufferedSink)object).emitCompleteSegments();
    }

    @NotNull
    public static final BufferedSink commonWriteInt(@NotNull RealBufferedSink object, int n2) {
        Intrinsics.checkNotNullParameter(object, "");
        if (!(!((RealBufferedSink)object).closed)) {
            object = "closed";
            throw new IllegalStateException(object.toString());
        }
        RealBufferedSink realBufferedSink = object;
        realBufferedSink.bufferField.writeInt(n2);
        return ((RealBufferedSink)object).emitCompleteSegments();
    }

    @NotNull
    public static final BufferedSink commonWriteIntLe(@NotNull RealBufferedSink object, int n2) {
        Intrinsics.checkNotNullParameter(object, "");
        if (!(!((RealBufferedSink)object).closed)) {
            object = "closed";
            throw new IllegalStateException(object.toString());
        }
        RealBufferedSink realBufferedSink = object;
        realBufferedSink.bufferField.writeIntLe(n2);
        return ((RealBufferedSink)object).emitCompleteSegments();
    }

    @NotNull
    public static final BufferedSink commonWriteLong(@NotNull RealBufferedSink object, long l2) {
        Intrinsics.checkNotNullParameter(object, "");
        if (!(!((RealBufferedSink)object).closed)) {
            object = "closed";
            throw new IllegalStateException(object.toString());
        }
        RealBufferedSink realBufferedSink = object;
        realBufferedSink.bufferField.writeLong(l2);
        return ((RealBufferedSink)object).emitCompleteSegments();
    }

    @NotNull
    public static final BufferedSink commonWriteLongLe(@NotNull RealBufferedSink object, long l2) {
        Intrinsics.checkNotNullParameter(object, "");
        if (!(!((RealBufferedSink)object).closed)) {
            object = "closed";
            throw new IllegalStateException(object.toString());
        }
        RealBufferedSink realBufferedSink = object;
        realBufferedSink.bufferField.writeLongLe(l2);
        return ((RealBufferedSink)object).emitCompleteSegments();
    }

    @NotNull
    public static final BufferedSink commonWriteDecimalLong(@NotNull RealBufferedSink object, long l2) {
        Intrinsics.checkNotNullParameter(object, "");
        if (!(!((RealBufferedSink)object).closed)) {
            object = "closed";
            throw new IllegalStateException(object.toString());
        }
        RealBufferedSink realBufferedSink = object;
        realBufferedSink.bufferField.writeDecimalLong(l2);
        return ((RealBufferedSink)object).emitCompleteSegments();
    }

    @NotNull
    public static final BufferedSink commonWriteHexadecimalUnsignedLong(@NotNull RealBufferedSink object, long l2) {
        Intrinsics.checkNotNullParameter(object, "");
        if (!(!((RealBufferedSink)object).closed)) {
            object = "closed";
            throw new IllegalStateException(object.toString());
        }
        RealBufferedSink realBufferedSink = object;
        realBufferedSink.bufferField.writeHexadecimalUnsignedLong(l2);
        return ((RealBufferedSink)object).emitCompleteSegments();
    }

    @NotNull
    public static final BufferedSink commonEmitCompleteSegments(@NotNull RealBufferedSink realBufferedSink) {
        Intrinsics.checkNotNullParameter(realBufferedSink, "");
        if (!(!realBufferedSink.closed)) {
            String string = "closed";
            throw new IllegalStateException(string.toString());
        }
        RealBufferedSink realBufferedSink2 = realBufferedSink;
        long l2 = realBufferedSink2.bufferField.completeSegmentByteCount();
        if (l2 > 0L) {
            realBufferedSink2 = realBufferedSink;
            realBufferedSink.sink.write(realBufferedSink2.bufferField, l2);
        }
        return realBufferedSink;
    }

    @NotNull
    public static final BufferedSink commonEmit(@NotNull RealBufferedSink realBufferedSink) {
        Intrinsics.checkNotNullParameter(realBufferedSink, "");
        if (!(!realBufferedSink.closed)) {
            String string = "closed";
            throw new IllegalStateException(string.toString());
        }
        RealBufferedSink realBufferedSink2 = realBufferedSink;
        long l2 = realBufferedSink2.bufferField.size();
        if (l2 > 0L) {
            realBufferedSink2 = realBufferedSink;
            realBufferedSink.sink.write(realBufferedSink2.bufferField, l2);
        }
        return realBufferedSink;
    }

    public static final void commonFlush(@NotNull RealBufferedSink object) {
        Intrinsics.checkNotNullParameter(object, "");
        if (!(!((RealBufferedSink)object).closed)) {
            object = "closed";
            throw new IllegalStateException(object.toString());
        }
        Object object2 = object;
        if (((RealBufferedSink)object2).bufferField.size() > 0L) {
            Object object3 = object;
            object2 = object3;
            object2 = object;
            ((RealBufferedSink)object).sink.write(((RealBufferedSink)object3).bufferField, ((RealBufferedSink)object2).bufferField.size());
        }
        ((RealBufferedSink)object).sink.flush();
    }

    public static final void commonClose(@NotNull RealBufferedSink realBufferedSink) {
        Throwable throwable;
        block7: {
            Object object;
            Intrinsics.checkNotNullParameter(realBufferedSink, "");
            if (realBufferedSink.closed) {
                return;
            }
            throwable = null;
            try {
                object = realBufferedSink;
                if (((RealBufferedSink)object).bufferField.size() > 0L) {
                    RealBufferedSink realBufferedSink2 = realBufferedSink;
                    object = realBufferedSink2;
                    object = realBufferedSink;
                    realBufferedSink.sink.write(realBufferedSink2.bufferField, ((RealBufferedSink)object).bufferField.size());
                }
            }
            catch (Throwable throwable2) {
                object = throwable2;
                throwable = throwable2;
            }
            try {
                realBufferedSink.sink.close();
            }
            catch (Throwable throwable3) {
                if (throwable != null) break block7;
                throwable = throwable3;
            }
        }
        realBufferedSink.closed = true;
        Throwable throwable4 = throwable;
        if (throwable4 != null) {
            throw throwable4;
        }
    }

    @NotNull
    public static final Timeout commonTimeout(@NotNull RealBufferedSink realBufferedSink) {
        Intrinsics.checkNotNullParameter(realBufferedSink, "");
        return realBufferedSink.sink.timeout();
    }

    @NotNull
    public static final String commonToString(@NotNull RealBufferedSink realBufferedSink) {
        Intrinsics.checkNotNullParameter(realBufferedSink, "");
        return "buffer(" + realBufferedSink.sink + ')';
    }
}


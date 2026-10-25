/*
 * Decompiled with CFR 0.152.
 */
package okio;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import okio.Buffer;
import okio.ByteString;
import okio.Segment;
import okio.internal.-SegmentedByteString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000j\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\f\n\u0002\u0010\u0005\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0000\u0018\u00002\u00020\u0001B\u001d\b\u0000\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007J\b\u0010\r\u001a\u00020\u000eH\u0016J\b\u0010\u000f\u001a\u00020\u0010H\u0016J\b\u0010\u0011\u001a\u00020\u0010H\u0016J(\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00042\u0006\u0010\u0017\u001a\u00020\u00152\u0006\u0010\u0018\u001a\u00020\u0015H\u0016J\u0015\u0010\u0019\u001a\u00020\u00012\u0006\u0010\u001a\u001a\u00020\u0010H\u0010\u00a2\u0006\u0002\b\u001bJ\u0013\u0010\u001c\u001a\u00020\u001d2\b\u0010\u001e\u001a\u0004\u0018\u00010\u001fH\u0096\u0002J\r\u0010 \u001a\u00020\u0015H\u0010\u00a2\u0006\u0002\b!J\b\u0010\"\u001a\u00020\u0015H\u0016J\b\u0010#\u001a\u00020\u0010H\u0016J\u001d\u0010$\u001a\u00020\u00012\u0006\u0010\u001a\u001a\u00020\u00102\u0006\u0010%\u001a\u00020\u0001H\u0010\u00a2\u0006\u0002\b&J\u0018\u0010'\u001a\u00020\u00152\u0006\u0010\u001e\u001a\u00020\u00042\u0006\u0010(\u001a\u00020\u0015H\u0016J\r\u0010)\u001a\u00020\u0004H\u0010\u00a2\u0006\u0002\b*J\u0015\u0010+\u001a\u00020,2\u0006\u0010-\u001a\u00020\u0015H\u0010\u00a2\u0006\u0002\b.J\u0018\u0010/\u001a\u00020\u00152\u0006\u0010\u001e\u001a\u00020\u00042\u0006\u0010(\u001a\u00020\u0015H\u0016J(\u00100\u001a\u00020\u001d2\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u001e\u001a\u00020\u00042\u0006\u00101\u001a\u00020\u00152\u0006\u0010\u0018\u001a\u00020\u0015H\u0016J(\u00100\u001a\u00020\u001d2\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u001e\u001a\u00020\u00012\u0006\u00101\u001a\u00020\u00152\u0006\u0010\u0018\u001a\u00020\u0015H\u0016J\u0010\u00102\u001a\u00020\u00102\u0006\u00103\u001a\u000204H\u0016J\u0018\u00105\u001a\u00020\u00012\u0006\u00106\u001a\u00020\u00152\u0006\u00107\u001a\u00020\u0015H\u0016J\b\u00108\u001a\u00020\u0001H\u0016J\b\u00109\u001a\u00020\u0001H\u0016J\b\u0010:\u001a\u00020\u0004H\u0016J\b\u0010;\u001a\u00020\u0001H\u0002J\b\u0010<\u001a\u00020\u0010H\u0016J\u0010\u0010=\u001a\u00020\u00132\u0006\u0010>\u001a\u00020?H\u0016J%\u0010=\u001a\u00020\u00132\u0006\u0010@\u001a\u00020A2\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0018\u001a\u00020\u0015H\u0010\u00a2\u0006\u0002\bBJ\b\u0010C\u001a\u00020DH\u0002R\u0014\u0010\u0005\u001a\u00020\u0006X\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u001c\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u0080\u0004\u00a2\u0006\n\n\u0002\u0010\f\u001a\u0004\b\n\u0010\u000b\u00a8\u0006E"}, d2={"Lokio/SegmentedByteString;", "Lokio/ByteString;", "segments", "", "", "directory", "", "([[B[I)V", "getDirectory$okio", "()[I", "getSegments$okio", "()[[B", "[[B", "asByteBuffer", "Ljava/nio/ByteBuffer;", "base64", "", "base64Url", "copyInto", "", "offset", "", "target", "targetOffset", "byteCount", "digest", "algorithm", "digest$okio", "equals", "", "other", "", "getSize", "getSize$okio", "hashCode", "hex", "hmac", "key", "hmac$okio", "indexOf", "fromIndex", "internalArray", "internalArray$okio", "internalGet", "", "pos", "internalGet$okio", "lastIndexOf", "rangeEquals", "otherOffset", "string", "charset", "Ljava/nio/charset/Charset;", "substring", "beginIndex", "endIndex", "toAsciiLowercase", "toAsciiUppercase", "toByteArray", "toByteString", "toString", "write", "out", "Ljava/io/OutputStream;", "buffer", "Lokio/Buffer;", "write$okio", "writeReplace", "Ljava/lang/Object;", "okio"})
@SourceDebugExtension(value={"SMAP\nSegmentedByteString.kt\nKotlin\n*S Kotlin\n*F\n+ 1 SegmentedByteString.kt\nokio/SegmentedByteString\n+ 2 SegmentedByteString.kt\nokio/internal/-SegmentedByteString\n+ 3 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,140:1\n63#2,12:141\n63#2,12:153\n104#2,2:165\n106#2,26:168\n135#2,5:194\n142#2:199\n145#2,3:200\n63#2,8:203\n148#2,8:211\n71#2,4:219\n156#2:223\n63#2,12:224\n160#2:236\n85#2,10:237\n161#2,9:247\n95#2,4:256\n170#2,2:260\n179#2,4:262\n85#2,10:266\n183#2,3:276\n95#2,4:279\n186#2:283\n195#2,8:284\n85#2,10:292\n203#2,3:302\n95#2,4:305\n206#2:309\n215#2,5:310\n85#2,10:315\n220#2,3:325\n95#2,4:328\n223#2:332\n226#2,4:333\n234#2,6:337\n63#2,8:343\n240#2,7:351\n71#2,4:358\n247#2,2:362\n1#3:167\n*S KotlinDebug\n*F\n+ 1 SegmentedByteString.kt\nokio/SegmentedByteString\n*L\n54#1:141,12\n66#1:153,12\n78#1:165,2\n78#1:168,26\n80#1:194,5\n82#1:199\n84#1:200,3\n84#1:203,8\n84#1:211,8\n84#1:219,4\n84#1:223\n90#1:224,12\n96#1:236\n96#1:237,10\n96#1:247,9\n96#1:256,4\n96#1:260,2\n103#1:262,4\n103#1:266,10\n103#1:276,3\n103#1:279,4\n103#1:283\n110#1:284,8\n110#1:292,10\n110#1:302,3\n110#1:305,4\n110#1:309\n117#1:310,5\n117#1:315,10\n117#1:325,3\n117#1:328,4\n117#1:332\n131#1:333,4\n133#1:337,6\n133#1:343,8\n133#1:351,7\n133#1:358,4\n133#1:362,2\n78#1:167\n*E\n"})
public final class SegmentedByteString
extends ByteString {
    @NotNull
    private final transient byte[][] segments;
    @NotNull
    private final transient int[] directory;

    public SegmentedByteString(@NotNull byte[][] byArray, @NotNull int[] nArray) {
        Intrinsics.checkNotNullParameter(byArray, "");
        Intrinsics.checkNotNullParameter(nArray, "");
        super(ByteString.EMPTY.getData$okio());
        this.segments = byArray;
        this.directory = nArray;
    }

    @NotNull
    public final byte[][] getSegments$okio() {
        return this.segments;
    }

    @NotNull
    public final int[] getDirectory$okio() {
        return this.directory;
    }

    @Override
    @NotNull
    public final String string(@NotNull Charset charset) {
        Intrinsics.checkNotNullParameter(charset, "");
        return this.toByteString().string(charset);
    }

    @Override
    @NotNull
    public final String base64() {
        return this.toByteString().base64();
    }

    @Override
    @NotNull
    public final String hex() {
        return this.toByteString().hex();
    }

    @Override
    @NotNull
    public final ByteString toAsciiLowercase() {
        return this.toByteString().toAsciiLowercase();
    }

    @Override
    @NotNull
    public final ByteString toAsciiUppercase() {
        return this.toByteString().toAsciiUppercase();
    }

    @Override
    @NotNull
    public final ByteString digest$okio(@NotNull String object) {
        Intrinsics.checkNotNullParameter(object, "");
        object = MessageDigest.getInstance((String)object);
        SegmentedByteString segmentedByteString = this;
        int n2 = ((Object[])segmentedByteString.getSegments$okio()).length;
        int n3 = 0;
        for (int i2 = 0; i2 < n2; ++i2) {
            int n4 = segmentedByteString.getDirectory$okio()[n2 + i2];
            int n5 = segmentedByteString.getDirectory$okio()[i2];
            n3 = n5 - n3;
            byte[] byArray = segmentedByteString.getSegments$okio()[i2];
            ((MessageDigest)object).update(byArray, n4, n3);
            n3 = n5;
        }
        object = ((MessageDigest)object).digest();
        Intrinsics.checkNotNull(object);
        return new ByteString((byte[])object);
    }

    @Override
    @NotNull
    public final ByteString hmac$okio(@NotNull String object, @NotNull ByteString byteString) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(byteString, "");
        try {
            Mac mac = Mac.getInstance((String)object);
            mac.init(new SecretKeySpec(byteString.toByteArray(), (String)object));
            object = this;
            int n2 = ((Object[])((SegmentedByteString)object).getSegments$okio()).length;
            int n3 = 0;
            for (int i2 = 0; i2 < n2; ++i2) {
                int n4 = ((SegmentedByteString)object).getDirectory$okio()[n2 + i2];
                int n5 = ((SegmentedByteString)object).getDirectory$okio()[i2];
                n3 = n5 - n3;
                byte[] byArray = ((SegmentedByteString)object).getSegments$okio()[i2];
                mac.update(byArray, n4, n3);
                n3 = n5;
            }
            byte[] byArray = mac.doFinal();
            Intrinsics.checkNotNullExpressionValue(byArray, "");
            return new ByteString(byArray);
        }
        catch (InvalidKeyException invalidKeyException) {
            throw new IllegalArgumentException(invalidKeyException);
        }
    }

    @Override
    @NotNull
    public final String base64Url() {
        return this.toByteString().base64Url();
    }

    @Override
    @NotNull
    public final ByteString substring(int n2, int n3) {
        SegmentedByteString segmentedByteString = this;
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
        n3 = ((Object[])byArray).length;
        objectArray[n3] = objectArray[n3] + (n2 - n7);
        return new SegmentedByteString(byArray, (int[])objectArray);
    }

    @Override
    public final byte internalGet$okio(int n2) {
        SegmentedByteString segmentedByteString = this;
        okio.-SegmentedByteString.checkOffsetAndCount(segmentedByteString.getDirectory$okio()[((Object[])segmentedByteString.getSegments$okio()).length - 1], n2, 1L);
        int n3 = -SegmentedByteString.segment(segmentedByteString, n2);
        int n4 = n3 == 0 ? 0 : segmentedByteString.getDirectory$okio()[n3 - 1];
        int n5 = segmentedByteString.getDirectory$okio()[n3 + ((Object[])segmentedByteString.getSegments$okio()).length];
        return segmentedByteString.getSegments$okio()[n3][n2 - n4 + n5];
    }

    @Override
    public final int getSize$okio() {
        SegmentedByteString segmentedByteString = this;
        return segmentedByteString.getDirectory$okio()[((Object[])segmentedByteString.getSegments$okio()).length - 1];
    }

    @Override
    @NotNull
    public final byte[] toByteArray() {
        SegmentedByteString segmentedByteString = this;
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

    @Override
    @NotNull
    public final ByteBuffer asByteBuffer() {
        ByteBuffer byteBuffer = ByteBuffer.wrap(((ByteString)this).toByteArray()).asReadOnlyBuffer();
        Intrinsics.checkNotNullExpressionValue(byteBuffer, "");
        return byteBuffer;
    }

    @Override
    public final void write(@NotNull OutputStream outputStream2) throws IOException {
        Intrinsics.checkNotNullParameter(outputStream2, "");
        SegmentedByteString segmentedByteString = this;
        int n2 = ((Object[])segmentedByteString.getSegments$okio()).length;
        int n3 = 0;
        for (int i2 = 0; i2 < n2; ++i2) {
            int n4 = segmentedByteString.getDirectory$okio()[n2 + i2];
            int n5 = segmentedByteString.getDirectory$okio()[i2];
            n3 = n5 - n3;
            byte[] byArray = segmentedByteString.getSegments$okio()[i2];
            outputStream2.write(byArray, n4, n3);
            n3 = n5;
        }
    }

    @Override
    public final void write$okio(@NotNull Buffer buffer, int n2, int n3) {
        Intrinsics.checkNotNullParameter(buffer, "");
        SegmentedByteString segmentedByteString = this;
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

    @Override
    public final boolean rangeEquals(int n2, @NotNull ByteString byteString, int n3, int n4) {
        Intrinsics.checkNotNullParameter(byteString, "");
        SegmentedByteString segmentedByteString = this;
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

    @Override
    public final boolean rangeEquals(int n2, @NotNull byte[] byArray, int n3, int n4) {
        Intrinsics.checkNotNullParameter(byArray, "");
        SegmentedByteString segmentedByteString = this;
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

    @Override
    public final void copyInto(int n2, @NotNull byte[] byArray, int n3, int n4) {
        Intrinsics.checkNotNullParameter(byArray, "");
        SegmentedByteString segmentedByteString = this;
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

    @Override
    public final int indexOf(@NotNull byte[] byArray, int n2) {
        Intrinsics.checkNotNullParameter(byArray, "");
        return this.toByteString().indexOf(byArray, n2);
    }

    @Override
    public final int lastIndexOf(@NotNull byte[] byArray, int n2) {
        Intrinsics.checkNotNullParameter(byArray, "");
        return this.toByteString().lastIndexOf(byArray, n2);
    }

    private final ByteString toByteString() {
        return new ByteString(((ByteString)this).toByteArray());
    }

    @Override
    @NotNull
    public final byte[] internalArray$okio() {
        return ((ByteString)this).toByteArray();
    }

    @Override
    public final boolean equals(@Nullable Object object) {
        SegmentedByteString segmentedByteString = this;
        if (object == segmentedByteString) {
            return true;
        }
        if (object instanceof ByteString) {
            return ((ByteString)object).size() == segmentedByteString.size() && ((ByteString)segmentedByteString).rangeEquals(0, (ByteString)object, 0, segmentedByteString.size());
        }
        return false;
    }

    @Override
    public final int hashCode() {
        SegmentedByteString segmentedByteString = this;
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

    @Override
    @NotNull
    public final String toString() {
        return this.toByteString().toString();
    }

    private final Object writeReplace() {
        ByteString byteString = this.toByteString();
        Intrinsics.checkNotNull(byteString);
        return byteString;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package okio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.RandomAccess;
import kotlin.Metadata;
import kotlin.collections.AbstractList;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import okio.Buffer;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\b\u0018\u0000 \u00152\b\u0012\u0004\u0012\u00020\u00020\u00012\u00060\u0003j\u0002`\u0004:\u0001\u0015B\u001f\b\u0002\u0012\u000e\u0010\u0005\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tJ\u0011\u0010\u0013\u001a\u00020\u00022\u0006\u0010\u0014\u001a\u00020\u000eH\u0096\u0002R\u001e\u0010\u0005\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00020\u0006X\u0080\u0004\u00a2\u0006\n\n\u0002\u0010\f\u001a\u0004\b\n\u0010\u000bR\u0014\u0010\r\u001a\u00020\u000e8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010R\u0014\u0010\u0007\u001a\u00020\bX\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012\u00a8\u0006\u0016"}, d2={"Lokio/Options;", "Lkotlin/collections/AbstractList;", "Lokio/ByteString;", "Ljava/util/RandomAccess;", "Lkotlin/collections/RandomAccess;", "byteStrings", "", "trie", "", "([Lokio/ByteString;[I)V", "getByteStrings$okio", "()[Lokio/ByteString;", "[Lokio/ByteString;", "size", "", "getSize", "()I", "getTrie$okio", "()[I", "get", "index", "Companion", "okio"})
public final class Options
extends AbstractList<ByteString>
implements RandomAccess {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final ByteString[] byteStrings;
    @NotNull
    private final int[] trie;

    private Options(ByteString[] byteStringArray, int[] nArray) {
        this.byteStrings = byteStringArray;
        this.trie = nArray;
    }

    @NotNull
    public final ByteString[] getByteStrings$okio() {
        return this.byteStrings;
    }

    @NotNull
    public final int[] getTrie$okio() {
        return this.trie;
    }

    @Override
    public final int getSize() {
        return this.byteStrings.length;
    }

    @Override
    @NotNull
    public final ByteString get(int n2) {
        return this.byteStrings[n2];
    }

    @JvmStatic
    @NotNull
    public static final Options of(ByteString ... byteStringArray) {
        return Companion.of(byteStringArray);
    }

    public /* synthetic */ Options(ByteString[] byteStringArray, int[] nArray, DefaultConstructorMarker defaultConstructorMarker) {
        this(byteStringArray, nArray);
    }

    @Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010\u0011\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002JT\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u00052\b\b\u0002\u0010\f\u001a\u00020\r2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000f2\b\b\u0002\u0010\u0011\u001a\u00020\r2\b\b\u0002\u0010\u0012\u001a\u00020\r2\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\r0\u000fH\u0002J!\u0010\u0014\u001a\u00020\u00152\u0012\u0010\u000e\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00100\u0016\"\u00020\u0010H\u0007\u00a2\u0006\u0002\u0010\u0017R\u0018\u0010\u0003\u001a\u00020\u0004*\u00020\u00058BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\u0018"}, d2={"Lokio/Options$Companion;", "", "()V", "intCount", "", "Lokio/Buffer;", "getIntCount", "(Lokio/Buffer;)J", "buildTrieRecursive", "", "nodeOffset", "node", "byteStringOffset", "", "byteStrings", "", "Lokio/ByteString;", "fromIndex", "toIndex", "indexes", "of", "Lokio/Options;", "", "([Lokio/ByteString;)Lokio/Options;", "okio"})
    @SourceDebugExtension(value={"SMAP\nOptions.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Options.kt\nokio/Options$Companion\n+ 2 _Arrays.kt\nkotlin/collections/ArraysKt___ArraysKt\n+ 3 ArraysJVM.kt\nkotlin/collections/ArraysKt__ArraysJVMKt\n+ 4 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 5 Util.kt\nokio/-SegmentedByteString\n*L\n1#1,236:1\n11065#2:237\n11400#2,3:238\n13374#2,3:243\n37#3,2:241\n1#4:246\n74#5:247\n74#5:248\n*S KotlinDebug\n*F\n+ 1 Options.kt\nokio/Options$Companion\n*L\n43#1:237\n43#1:238,3\n44#1:243,3\n43#1:241,2\n151#1:247\n208#1:248\n*E\n"})
    public static final class Companion {
        private Companion() {
        }

        @JvmStatic
        @NotNull
        public final Options of(ByteString ... byteStringArray) {
            Object object;
            int n2;
            Intrinsics.checkNotNullParameter(byteStringArray, "");
            if (byteStringArray.length == 0) {
                int[] nArray = new int[2];
                int[] nArray2 = nArray;
                nArray[0] = 0;
                nArray2[1] = -1;
                return new Options(new ByteString[0], nArray2, null);
            }
            List<ByteString> list2 = ArraysKt.toMutableList(byteStringArray);
            CollectionsKt.sort(list2);
            Object object2 = byteStringArray;
            Object object3 = byteStringArray;
            object2 = new ArrayList(((ByteString[])object2).length);
            int n3 = ((ByteString[])object3).length;
            for (n2 = 0; n2 < n3; ++n2) {
                ByteString cfr_ignored_0 = object3[n2];
                object = object2;
                object.add(-1);
            }
            object3 = object2 = (Collection)((List)object2);
            Comparable<Integer>[] comparableArray = object2.toArray(new Integer[0]);
            object = CollectionsKt.mutableListOf(Arrays.copyOf(comparableArray, comparableArray.length));
            comparableArray = byteStringArray;
            int n4 = 0;
            for (Comparable<Integer> comparable : comparableArray) {
                n3 = n4++;
                n2 = CollectionsKt.binarySearch$default(list2, comparable, 0, 0, 6, null);
                object.set(n2, n3);
            }
            if (!(list2.get(0).size() > 0)) {
                String string = "the empty byte string is not a supported option";
                throw new IllegalArgumentException(string.toString());
            }
            for (int i2 = 0; i2 < list2.size(); ++i2) {
                ByteString byteString;
                ByteString byteString2 = list2.get(i2);
                n4 = i2 + 1;
                while (n4 < list2.size() && (byteString = list2.get(n4)).startsWith(byteString2)) {
                    if (!(byteString.size() != byteString2.size())) {
                        String string = "duplicate option: " + byteString;
                        throw new IllegalArgumentException(string.toString());
                    }
                    if (((Number)object.get(n4)).intValue() > ((Number)object.get(i2)).intValue()) {
                        list2.remove(n4);
                        object.remove(n4);
                        continue;
                    }
                    ++n4;
                }
            }
            Buffer buffer = new Buffer();
            okio.Options$Companion.buildTrieRecursive$default(this, 0L, buffer, 0, list2, 0, 0, (List)object, 53, null);
            int[] nArray = new int[(int)this.getIntCount(buffer)];
            int n5 = 0;
            while (!buffer.exhausted()) {
                nArray[n5++] = buffer.readInt();
            }
            ByteString[] byteStringArray2 = Arrays.copyOf(byteStringArray, byteStringArray.length);
            Intrinsics.checkNotNullExpressionValue(byteStringArray2, "");
            return new Options(byteStringArray2, nArray, null);
        }

        private final void buildTrieRecursive(long l2, Buffer buffer, int n2, List<? extends ByteString> list2, int n3, int n4, List<Integer> list3) {
            int n5;
            if (!(n3 < n4)) {
                String string = "Failed requirement.";
                throw new IllegalArgumentException(string.toString());
            }
            for (n5 = n3; n5 < n4; ++n5) {
                if (list2.get(n5).size() >= n2) continue;
                String string = "Failed requirement.";
                throw new IllegalArgumentException(string.toString());
            }
            n5 = n3;
            ByteString byteString = list2.get(n3);
            ByteString byteString2 = list2.get(n4 - 1);
            int n6 = -1;
            if (n2 == byteString.size()) {
                n6 = ((Number)list3.get(n3)).intValue();
                byteString = list2.get(++n5);
            }
            if (byteString.getByte(n2) != byteString2.getByte(n2)) {
                int n7;
                n3 = 1;
                for (int i2 = n5 + 1; i2 < n4; ++i2) {
                    if (list2.get(i2 - 1).getByte(n2) == list2.get(i2).getByte(n2)) continue;
                    ++n3;
                }
                long l3 = l2 + this.getIntCount(buffer) + 2L + (long)(n3 << 1);
                buffer.writeInt(n3);
                buffer.writeInt(n6);
                for (int i3 = n5; i3 < n4; ++i3) {
                    n7 = list2.get(i3).getByte(n2);
                    if (i3 != n5 && n7 == list2.get(i3 - 1).getByte(n2)) continue;
                    int n8 = n7;
                    buffer.writeInt(n8 & 0xFF);
                }
                Buffer buffer2 = new Buffer();
                n7 = n5;
                while (n7 < n4) {
                    byte by = list2.get(n7).getByte(n2);
                    n3 = n4;
                    for (n5 = n7 + 1; n5 < n4; ++n5) {
                        if (by == list2.get(n5).getByte(n2)) continue;
                        n3 = n5;
                        break;
                    }
                    if (n7 + 1 == n3 && n2 + 1 == list2.get(n7).size()) {
                        buffer.writeInt(((Number)list3.get(n7)).intValue());
                    } else {
                        buffer.writeInt(-1 * (int)(l3 + this.getIntCount(buffer2)));
                        this.buildTrieRecursive(l3, buffer2, n2 + 1, list2, n7, n3, list3);
                    }
                    n7 = n3;
                }
                buffer.writeAll(buffer2);
                return;
            }
            n3 = 0;
            int n9 = Math.min(byteString.size(), byteString2.size());
            for (int i4 = n2; i4 < n9 && byteString.getByte(i4) == byteString2.getByte(i4); ++i4) {
                ++n3;
            }
            long l4 = l2 + this.getIntCount(buffer) + 2L + (long)n3 + 1L;
            buffer.writeInt(-n3);
            buffer.writeInt(n6);
            int n10 = n2 + n3;
            for (int i5 = n2; i5 < n10; ++i5) {
                byte by = byteString.getByte(i5);
                buffer.writeInt(by & 0xFF);
            }
            if (n5 + 1 == n4) {
                if (!(n2 + n3 == list2.get(n5).size())) {
                    String string = "Check failed.";
                    throw new IllegalStateException(string.toString());
                }
                buffer.writeInt(((Number)list3.get(n5)).intValue());
                return;
            }
            Buffer buffer3 = new Buffer();
            buffer.writeInt(-1 * (int)(l4 + this.getIntCount(buffer3)));
            this.buildTrieRecursive(l4, buffer3, n2 + n3, list2, n5, n4, list3);
            buffer.writeAll(buffer3);
        }

        static /* synthetic */ void buildTrieRecursive$default(Companion companion, long l2, Buffer buffer, int n2, List list2, int n3, int n4, List list3, int n5, Object object) {
            if ((n5 & 1) != 0) {
                l2 = 0L;
            }
            if ((n5 & 4) != 0) {
                n2 = 0;
            }
            if ((n5 & 0x10) != 0) {
                n3 = 0;
            }
            if ((n5 & 0x20) != 0) {
                n4 = list2.size();
            }
            companion.buildTrieRecursive(l2, buffer, n2, list2, n3, n4, list3);
        }

        private final long getIntCount(Buffer buffer) {
            return buffer.size() / 4L;
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }
}


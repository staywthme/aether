/*
 * Decompiled with CFR 0.152.
 */
package kotlin;

import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import kotlin.ExperimentalUnsignedTypes;
import kotlin.Metadata;
import kotlin.PublishedApi;
import kotlin.SinceKotlin;
import kotlin.UByte;
import kotlin.collections.ArraysKt;
import kotlin.jvm.JvmInline;
import kotlin.jvm.internal.CollectionToArray;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.jvm.internal.markers.KMappedMarker;
import org.jetbrains.annotations.NotNull;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@JvmInline
@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u001e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010\u0000\n\u0002\b\f\n\u0002\u0010(\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0004\b\u0087@\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u00012B\u0014\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0005\u0010\u0006B\u0014\b\u0001\u0012\u0006\u0010\u0007\u001a\u00020\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0005\u0010\tJ\u001b\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0002H\u0096\u0002\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0011\u0010\u0012J \u0010\u0013\u001a\u00020\u000f2\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001H\u0016\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0015\u0010\u0016J\u001a\u0010\u0017\u001a\u00020\u000f2\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u00d6\u0003\u00a2\u0006\u0004\b\u001a\u0010\u001bJ\u001e\u0010\u001c\u001a\u00020\u00022\u0006\u0010\u001d\u001a\u00020\u0004H\u0086\u0002\u00f8\u0001\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001e\u0010\u001fJ\u0010\u0010 \u001a\u00020\u0004H\u00d6\u0001\u00a2\u0006\u0004\b!\u0010\u000bJ\u000f\u0010\"\u001a\u00020\u000fH\u0016\u00a2\u0006\u0004\b#\u0010$J\u0019\u0010%\u001a\b\u0012\u0004\u0012\u00020\u00020&H\u0096\u0002\u00f8\u0001\u0000\u00a2\u0006\u0004\b'\u0010(J#\u0010)\u001a\u00020*2\u0006\u0010\u001d\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0002H\u0086\u0002\u00f8\u0001\u0000\u00a2\u0006\u0004\b,\u0010-J\u0010\u0010.\u001a\u00020/H\u00d6\u0001\u00a2\u0006\u0004\b0\u00101R\u0014\u0010\u0003\u001a\u00020\u00048VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0016\u0010\u0007\u001a\u00020\b8\u0000X\u0081\u0004\u00a2\u0006\b\n\u0000\u0012\u0004\b\f\u0010\r\u0088\u0001\u0007\u0092\u0001\u00020\b\u00f8\u0001\u0000\u0082\u0002\b\n\u0002\b\u0019\n\u0002\b!\u00a8\u00063"}, d2={"Lkotlin/UByteArray;", "", "Lkotlin/UByte;", "size", "", "constructor-impl", "(I)[B", "storage", "", "([B)[B", "getSize-impl", "([B)I", "getStorage$annotations", "()V", "contains", "", "element", "contains-7apg3OU", "([BB)Z", "containsAll", "elements", "containsAll-impl", "([BLjava/util/Collection;)Z", "equals", "other", "", "equals-impl", "([BLjava/lang/Object;)Z", "get", "index", "get-w2LRezQ", "([BI)B", "hashCode", "hashCode-impl", "isEmpty", "isEmpty-impl", "([B)Z", "iterator", "", "iterator-impl", "([B)Ljava/util/Iterator;", "set", "", "value", "set-VurrAj0", "([BIB)V", "toString", "", "toString-impl", "([B)Ljava/lang/String;", "Iterator", "kotlin-stdlib"})
@SinceKotlin(version="1.3")
@ExperimentalUnsignedTypes
@SourceDebugExtension(value={"SMAP\nUByteArray.kt\nKotlin\n*S Kotlin\n*F\n+ 1 UByteArray.kt\nkotlin/UByteArray\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,86:1\n1726#2,3:87\n*S KotlinDebug\n*F\n+ 1 UByteArray.kt\nkotlin/UByteArray\n*L\n62#1:87,3\n*E\n"})
public final class UByteArray
implements Collection<UByte>,
KMappedMarker {
    @NotNull
    private final byte[] storage;

    @PublishedApi
    public static /* synthetic */ void getStorage$annotations() {
    }

    @NotNull
    public static byte[] constructor-impl(int n2) {
        return UByteArray.constructor-impl(new byte[n2]);
    }

    public static final byte get-w2LRezQ(byte[] byArray, int n2) {
        return UByte.constructor-impl(byArray[n2]);
    }

    public static final void set-VurrAj0(byte[] byArray, int n2, byte by) {
        byArray[n2] = by;
    }

    public static int getSize-impl(byte[] byArray) {
        return byArray.length;
    }

    public final int getSize() {
        return UByteArray.getSize-impl(this.storage);
    }

    @NotNull
    public static java.util.Iterator<UByte> iterator-impl(byte[] byArray) {
        return new Iterator(byArray);
    }

    @Override
    @NotNull
    public final java.util.Iterator<UByte> iterator() {
        return UByteArray.iterator-impl(this.storage);
    }

    public static boolean contains-7apg3OU(byte[] byArray, byte by) {
        return ArraysKt.contains(byArray, by);
    }

    public final boolean contains-7apg3OU(byte by) {
        return UByteArray.contains-7apg3OU(this.storage, by);
    }

    public static boolean containsAll-impl(byte[] byArray, @NotNull Collection<UByte> object) {
        Intrinsics.checkNotNullParameter(object, "");
        object = (Iterable)object;
        if (!((Collection)object).isEmpty()) {
            object = object.iterator();
            while (object.hasNext()) {
                Object e2 = object.next();
                Object e3 = e2;
                e3 = e2;
                if (e2 instanceof UByte && ArraysKt.contains(byArray, ((UByte)e3).unbox-impl())) continue;
                return false;
            }
        }
        return true;
    }

    @Override
    public final boolean containsAll(@NotNull Collection<? extends Object> collection) {
        Intrinsics.checkNotNullParameter(collection, "");
        return UByteArray.containsAll-impl(this.storage, collection);
    }

    public static boolean isEmpty-impl(byte[] byArray) {
        return byArray.length == 0;
    }

    @Override
    public final boolean isEmpty() {
        return UByteArray.isEmpty-impl(this.storage);
    }

    public static String toString-impl(byte[] byArray) {
        return "UByteArray(storage=" + Arrays.toString(byArray) + ')';
    }

    public final String toString() {
        return UByteArray.toString-impl(this.storage);
    }

    public static int hashCode-impl(byte[] byArray) {
        return Arrays.hashCode(byArray);
    }

    @Override
    public final int hashCode() {
        return UByteArray.hashCode-impl(this.storage);
    }

    public static boolean equals-impl(byte[] byArray, Object object) {
        if (!(object instanceof UByteArray)) {
            return false;
        }
        return Intrinsics.areEqual(byArray, ((UByteArray)object).unbox-impl());
    }

    @Override
    public final boolean equals(Object object) {
        return UByteArray.equals-impl(this.storage, object);
    }

    public final boolean add-7apg3OU(byte by) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override
    public final boolean addAll(Collection<? extends UByte> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override
    public final void clear() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override
    public final boolean remove(Object object) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override
    public final boolean removeAll(Collection<? extends Object> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override
    public final boolean retainAll(Collection<? extends Object> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @PublishedApi
    private /* synthetic */ UByteArray(byte[] byArray) {
        this.storage = byArray;
    }

    @PublishedApi
    @NotNull
    public static byte[] constructor-impl(@NotNull byte[] byArray) {
        Intrinsics.checkNotNullParameter(byArray, "");
        return byArray;
    }

    public static final /* synthetic */ UByteArray box-impl(byte[] byArray) {
        return new UByteArray(byArray);
    }

    public final /* synthetic */ byte[] unbox-impl() {
        return this.storage;
    }

    public static final boolean equals-impl0(byte[] byArray, byte[] byArray2) {
        return Intrinsics.areEqual(byArray, byArray2);
    }

    @Override
    public final /* synthetic */ int size() {
        return this.getSize();
    }

    @Override
    public final boolean contains(Object object) {
        if (!(object instanceof UByte)) {
            return false;
        }
        return this.contains-7apg3OU(((UByte)object).unbox-impl());
    }

    @Override
    public final /* synthetic */ boolean add(Object object) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override
    public final <T> T[] toArray(T[] TArray) {
        Intrinsics.checkNotNullParameter(TArray, "");
        return CollectionToArray.toArray(this, TArray);
    }

    @Override
    public final Object[] toArray() {
        return CollectionToArray.toArray(this);
    }

    /*
     * Illegal identifiers - consider using --renameillegalidents true
     */
    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010(\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\u0005J\t\u0010\b\u001a\u00020\tH\u0096\u0002J\u0016\u0010\n\u001a\u00020\u0002H\u0096\u0002\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u000b\u0010\fR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00f8\u0001\u0001\u0082\u0002\b\n\u0002\b!\n\u0002\b\u0019\u00a8\u0006\r"}, d2={"Lkotlin/UByteArray$Iterator;", "", "Lkotlin/UByte;", "array", "", "([B)V", "index", "", "hasNext", "", "next", "next-w2LRezQ", "()B", "kotlin-stdlib"})
    static final class Iterator
    implements java.util.Iterator<UByte>,
    KMappedMarker {
        @NotNull
        private final byte[] array;
        private int index;

        public Iterator(@NotNull byte[] byArray) {
            Intrinsics.checkNotNullParameter(byArray, "");
            this.array = byArray;
        }

        @Override
        public final boolean hasNext() {
            return this.index < this.array.length;
        }

        public final byte next-w2LRezQ() {
            if (this.index < this.array.length) {
                int n2 = this.index;
                this.index = n2 + 1;
                return UByte.constructor-impl(this.array[n2]);
            }
            throw new NoSuchElementException(String.valueOf(this.index));
        }

        @Override
        public final void remove() {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }

        @Override
        public final /* synthetic */ Object next() {
            return UByte.box-impl(this.next-w2LRezQ());
        }
    }
}


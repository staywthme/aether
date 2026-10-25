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
import kotlin.ULong;
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
@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u001e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0016\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010\u0000\n\u0002\b\f\n\u0002\u0010(\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0004\b\u0087@\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u00012B\u0014\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0005\u0010\u0006B\u0014\b\u0001\u0012\u0006\u0010\u0007\u001a\u00020\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0005\u0010\tJ\u001b\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0002H\u0096\u0002\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0011\u0010\u0012J \u0010\u0013\u001a\u00020\u000f2\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001H\u0016\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0015\u0010\u0016J\u001a\u0010\u0017\u001a\u00020\u000f2\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u00d6\u0003\u00a2\u0006\u0004\b\u001a\u0010\u001bJ\u001e\u0010\u001c\u001a\u00020\u00022\u0006\u0010\u001d\u001a\u00020\u0004H\u0086\u0002\u00f8\u0001\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001e\u0010\u001fJ\u0010\u0010 \u001a\u00020\u0004H\u00d6\u0001\u00a2\u0006\u0004\b!\u0010\u000bJ\u000f\u0010\"\u001a\u00020\u000fH\u0016\u00a2\u0006\u0004\b#\u0010$J\u0019\u0010%\u001a\b\u0012\u0004\u0012\u00020\u00020&H\u0096\u0002\u00f8\u0001\u0000\u00a2\u0006\u0004\b'\u0010(J#\u0010)\u001a\u00020*2\u0006\u0010\u001d\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0002H\u0086\u0002\u00f8\u0001\u0000\u00a2\u0006\u0004\b,\u0010-J\u0010\u0010.\u001a\u00020/H\u00d6\u0001\u00a2\u0006\u0004\b0\u00101R\u0014\u0010\u0003\u001a\u00020\u00048VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0016\u0010\u0007\u001a\u00020\b8\u0000X\u0081\u0004\u00a2\u0006\b\n\u0000\u0012\u0004\b\f\u0010\r\u0088\u0001\u0007\u0092\u0001\u00020\b\u00f8\u0001\u0000\u0082\u0002\b\n\u0002\b\u0019\n\u0002\b!\u00a8\u00063"}, d2={"Lkotlin/ULongArray;", "", "Lkotlin/ULong;", "size", "", "constructor-impl", "(I)[J", "storage", "", "([J)[J", "getSize-impl", "([J)I", "getStorage$annotations", "()V", "contains", "", "element", "contains-VKZWuLQ", "([JJ)Z", "containsAll", "elements", "containsAll-impl", "([JLjava/util/Collection;)Z", "equals", "other", "", "equals-impl", "([JLjava/lang/Object;)Z", "get", "index", "get-s-VKNKU", "([JI)J", "hashCode", "hashCode-impl", "isEmpty", "isEmpty-impl", "([J)Z", "iterator", "", "iterator-impl", "([J)Ljava/util/Iterator;", "set", "", "value", "set-k8EXiF4", "([JIJ)V", "toString", "", "toString-impl", "([J)Ljava/lang/String;", "Iterator", "kotlin-stdlib"})
@SinceKotlin(version="1.3")
@ExperimentalUnsignedTypes
@SourceDebugExtension(value={"SMAP\nULongArray.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ULongArray.kt\nkotlin/ULongArray\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,86:1\n1726#2,3:87\n*S KotlinDebug\n*F\n+ 1 ULongArray.kt\nkotlin/ULongArray\n*L\n62#1:87,3\n*E\n"})
public final class ULongArray
implements Collection<ULong>,
KMappedMarker {
    @NotNull
    private final long[] storage;

    @PublishedApi
    public static /* synthetic */ void getStorage$annotations() {
    }

    @NotNull
    public static long[] constructor-impl(int n2) {
        return ULongArray.constructor-impl(new long[n2]);
    }

    public static final long get-s-VKNKU(long[] lArray, int n2) {
        return ULong.constructor-impl(lArray[n2]);
    }

    public static final void set-k8EXiF4(long[] lArray, int n2, long l2) {
        lArray[n2] = l2;
    }

    public static int getSize-impl(long[] lArray) {
        return lArray.length;
    }

    public final int getSize() {
        return ULongArray.getSize-impl(this.storage);
    }

    @NotNull
    public static java.util.Iterator<ULong> iterator-impl(long[] lArray) {
        return new Iterator(lArray);
    }

    @Override
    @NotNull
    public final java.util.Iterator<ULong> iterator() {
        return ULongArray.iterator-impl(this.storage);
    }

    public static boolean contains-VKZWuLQ(long[] lArray, long l2) {
        return ArraysKt.contains(lArray, l2);
    }

    public final boolean contains-VKZWuLQ(long l2) {
        return ULongArray.contains-VKZWuLQ(this.storage, l2);
    }

    public static boolean containsAll-impl(long[] lArray, @NotNull Collection<ULong> object) {
        Intrinsics.checkNotNullParameter(object, "");
        object = (Iterable)object;
        if (!((Collection)object).isEmpty()) {
            object = object.iterator();
            while (object.hasNext()) {
                Object e2 = object.next();
                Object e3 = e2;
                e3 = e2;
                if (e2 instanceof ULong && ArraysKt.contains(lArray, ((ULong)e3).unbox-impl())) continue;
                return false;
            }
        }
        return true;
    }

    @Override
    public final boolean containsAll(@NotNull Collection<? extends Object> collection) {
        Intrinsics.checkNotNullParameter(collection, "");
        return ULongArray.containsAll-impl(this.storage, collection);
    }

    public static boolean isEmpty-impl(long[] lArray) {
        return lArray.length == 0;
    }

    @Override
    public final boolean isEmpty() {
        return ULongArray.isEmpty-impl(this.storage);
    }

    public static String toString-impl(long[] lArray) {
        return "ULongArray(storage=" + Arrays.toString(lArray) + ')';
    }

    public final String toString() {
        return ULongArray.toString-impl(this.storage);
    }

    public static int hashCode-impl(long[] lArray) {
        return Arrays.hashCode(lArray);
    }

    @Override
    public final int hashCode() {
        return ULongArray.hashCode-impl(this.storage);
    }

    public static boolean equals-impl(long[] lArray, Object object) {
        if (!(object instanceof ULongArray)) {
            return false;
        }
        return Intrinsics.areEqual(lArray, ((ULongArray)object).unbox-impl());
    }

    @Override
    public final boolean equals(Object object) {
        return ULongArray.equals-impl(this.storage, object);
    }

    public final boolean add-VKZWuLQ(long l2) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override
    public final boolean addAll(Collection<? extends ULong> collection) {
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
    private /* synthetic */ ULongArray(long[] lArray) {
        this.storage = lArray;
    }

    @PublishedApi
    @NotNull
    public static long[] constructor-impl(@NotNull long[] lArray) {
        Intrinsics.checkNotNullParameter(lArray, "");
        return lArray;
    }

    public static final /* synthetic */ ULongArray box-impl(long[] lArray) {
        return new ULongArray(lArray);
    }

    public final /* synthetic */ long[] unbox-impl() {
        return this.storage;
    }

    public static final boolean equals-impl0(long[] lArray, long[] lArray2) {
        return Intrinsics.areEqual(lArray, lArray2);
    }

    @Override
    public final /* synthetic */ int size() {
        return this.getSize();
    }

    @Override
    public final boolean contains(Object object) {
        if (!(object instanceof ULong)) {
            return false;
        }
        return this.contains-VKZWuLQ(((ULong)object).unbox-impl());
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
    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010(\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0016\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\u0005J\t\u0010\b\u001a\u00020\tH\u0096\u0002J\u0016\u0010\n\u001a\u00020\u0002H\u0096\u0002\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u000b\u0010\fR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00f8\u0001\u0001\u0082\u0002\b\n\u0002\b!\n\u0002\b\u0019\u00a8\u0006\r"}, d2={"Lkotlin/ULongArray$Iterator;", "", "Lkotlin/ULong;", "array", "", "([J)V", "index", "", "hasNext", "", "next", "next-s-VKNKU", "()J", "kotlin-stdlib"})
    static final class Iterator
    implements java.util.Iterator<ULong>,
    KMappedMarker {
        @NotNull
        private final long[] array;
        private int index;

        public Iterator(@NotNull long[] lArray) {
            Intrinsics.checkNotNullParameter(lArray, "");
            this.array = lArray;
        }

        @Override
        public final boolean hasNext() {
            return this.index < this.array.length;
        }

        public final long next-s-VKNKU() {
            if (this.index < this.array.length) {
                int n2 = this.index;
                this.index = n2 + 1;
                return ULong.constructor-impl(this.array[n2]);
            }
            throw new NoSuchElementException(String.valueOf(this.index));
        }

        @Override
        public final void remove() {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }

        @Override
        public final /* synthetic */ Object next() {
            return ULong.box-impl(this.next-s-VKNKU());
        }
    }
}


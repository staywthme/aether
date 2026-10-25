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
import kotlin.UInt;
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
@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u001e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0015\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010\u0000\n\u0002\b\f\n\u0002\u0010(\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0004\b\u0087@\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u00012B\u0014\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0005\u0010\u0006B\u0014\b\u0001\u0012\u0006\u0010\u0007\u001a\u00020\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0005\u0010\tJ\u001b\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0002H\u0096\u0002\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0011\u0010\u0012J \u0010\u0013\u001a\u00020\u000f2\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001H\u0016\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0015\u0010\u0016J\u001a\u0010\u0017\u001a\u00020\u000f2\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u00d6\u0003\u00a2\u0006\u0004\b\u001a\u0010\u001bJ\u001e\u0010\u001c\u001a\u00020\u00022\u0006\u0010\u001d\u001a\u00020\u0004H\u0086\u0002\u00f8\u0001\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001e\u0010\u001fJ\u0010\u0010 \u001a\u00020\u0004H\u00d6\u0001\u00a2\u0006\u0004\b!\u0010\u000bJ\u000f\u0010\"\u001a\u00020\u000fH\u0016\u00a2\u0006\u0004\b#\u0010$J\u0019\u0010%\u001a\b\u0012\u0004\u0012\u00020\u00020&H\u0096\u0002\u00f8\u0001\u0000\u00a2\u0006\u0004\b'\u0010(J#\u0010)\u001a\u00020*2\u0006\u0010\u001d\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0002H\u0086\u0002\u00f8\u0001\u0000\u00a2\u0006\u0004\b,\u0010-J\u0010\u0010.\u001a\u00020/H\u00d6\u0001\u00a2\u0006\u0004\b0\u00101R\u0014\u0010\u0003\u001a\u00020\u00048VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0016\u0010\u0007\u001a\u00020\b8\u0000X\u0081\u0004\u00a2\u0006\b\n\u0000\u0012\u0004\b\f\u0010\r\u0088\u0001\u0007\u0092\u0001\u00020\b\u00f8\u0001\u0000\u0082\u0002\b\n\u0002\b\u0019\n\u0002\b!\u00a8\u00063"}, d2={"Lkotlin/UIntArray;", "", "Lkotlin/UInt;", "size", "", "constructor-impl", "(I)[I", "storage", "", "([I)[I", "getSize-impl", "([I)I", "getStorage$annotations", "()V", "contains", "", "element", "contains-WZ4Q5Ns", "([II)Z", "containsAll", "elements", "containsAll-impl", "([ILjava/util/Collection;)Z", "equals", "other", "", "equals-impl", "([ILjava/lang/Object;)Z", "get", "index", "get-pVg5ArA", "([II)I", "hashCode", "hashCode-impl", "isEmpty", "isEmpty-impl", "([I)Z", "iterator", "", "iterator-impl", "([I)Ljava/util/Iterator;", "set", "", "value", "set-VXSXFK8", "([III)V", "toString", "", "toString-impl", "([I)Ljava/lang/String;", "Iterator", "kotlin-stdlib"})
@SinceKotlin(version="1.3")
@ExperimentalUnsignedTypes
@SourceDebugExtension(value={"SMAP\nUIntArray.kt\nKotlin\n*S Kotlin\n*F\n+ 1 UIntArray.kt\nkotlin/UIntArray\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,86:1\n1726#2,3:87\n*S KotlinDebug\n*F\n+ 1 UIntArray.kt\nkotlin/UIntArray\n*L\n62#1:87,3\n*E\n"})
public final class UIntArray
implements Collection<UInt>,
KMappedMarker {
    @NotNull
    private final int[] storage;

    @PublishedApi
    public static /* synthetic */ void getStorage$annotations() {
    }

    @NotNull
    public static int[] constructor-impl(int n2) {
        return UIntArray.constructor-impl(new int[n2]);
    }

    public static final int get-pVg5ArA(int[] nArray, int n2) {
        return UInt.constructor-impl(nArray[n2]);
    }

    public static final void set-VXSXFK8(int[] nArray, int n2, int n3) {
        nArray[n2] = n3;
    }

    public static int getSize-impl(int[] nArray) {
        return nArray.length;
    }

    public final int getSize() {
        return UIntArray.getSize-impl(this.storage);
    }

    @NotNull
    public static java.util.Iterator<UInt> iterator-impl(int[] nArray) {
        return new Iterator(nArray);
    }

    @Override
    @NotNull
    public final java.util.Iterator<UInt> iterator() {
        return UIntArray.iterator-impl(this.storage);
    }

    public static boolean contains-WZ4Q5Ns(int[] nArray, int n2) {
        return ArraysKt.contains(nArray, n2);
    }

    public final boolean contains-WZ4Q5Ns(int n2) {
        return UIntArray.contains-WZ4Q5Ns(this.storage, n2);
    }

    public static boolean containsAll-impl(int[] nArray, @NotNull Collection<UInt> object) {
        Intrinsics.checkNotNullParameter(object, "");
        object = (Iterable)object;
        if (!((Collection)object).isEmpty()) {
            object = object.iterator();
            while (object.hasNext()) {
                Object e2 = object.next();
                Object e3 = e2;
                e3 = e2;
                if (e2 instanceof UInt && ArraysKt.contains(nArray, ((UInt)e3).unbox-impl())) continue;
                return false;
            }
        }
        return true;
    }

    @Override
    public final boolean containsAll(@NotNull Collection<? extends Object> collection) {
        Intrinsics.checkNotNullParameter(collection, "");
        return UIntArray.containsAll-impl(this.storage, collection);
    }

    public static boolean isEmpty-impl(int[] nArray) {
        return nArray.length == 0;
    }

    @Override
    public final boolean isEmpty() {
        return UIntArray.isEmpty-impl(this.storage);
    }

    public static String toString-impl(int[] nArray) {
        return "UIntArray(storage=" + Arrays.toString(nArray) + ')';
    }

    public final String toString() {
        return UIntArray.toString-impl(this.storage);
    }

    public static int hashCode-impl(int[] nArray) {
        return Arrays.hashCode(nArray);
    }

    @Override
    public final int hashCode() {
        return UIntArray.hashCode-impl(this.storage);
    }

    public static boolean equals-impl(int[] nArray, Object object) {
        if (!(object instanceof UIntArray)) {
            return false;
        }
        return Intrinsics.areEqual(nArray, ((UIntArray)object).unbox-impl());
    }

    @Override
    public final boolean equals(Object object) {
        return UIntArray.equals-impl(this.storage, object);
    }

    public final boolean add-WZ4Q5Ns(int n2) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override
    public final boolean addAll(Collection<? extends UInt> collection) {
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
    private /* synthetic */ UIntArray(int[] nArray) {
        this.storage = nArray;
    }

    @PublishedApi
    @NotNull
    public static int[] constructor-impl(@NotNull int[] nArray) {
        Intrinsics.checkNotNullParameter(nArray, "");
        return nArray;
    }

    public static final /* synthetic */ UIntArray box-impl(int[] nArray) {
        return new UIntArray(nArray);
    }

    public final /* synthetic */ int[] unbox-impl() {
        return this.storage;
    }

    public static final boolean equals-impl0(int[] nArray, int[] nArray2) {
        return Intrinsics.areEqual(nArray, nArray2);
    }

    @Override
    public final /* synthetic */ int size() {
        return this.getSize();
    }

    @Override
    public final boolean contains(Object object) {
        if (!(object instanceof UInt)) {
            return false;
        }
        return this.contains-WZ4Q5Ns(((UInt)object).unbox-impl());
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
    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010(\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\u0005J\t\u0010\b\u001a\u00020\tH\u0096\u0002J\u0016\u0010\n\u001a\u00020\u0002H\u0096\u0002\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u000b\u0010\fR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00f8\u0001\u0001\u0082\u0002\b\n\u0002\b!\n\u0002\b\u0019\u00a8\u0006\r"}, d2={"Lkotlin/UIntArray$Iterator;", "", "Lkotlin/UInt;", "array", "", "([I)V", "index", "", "hasNext", "", "next", "next-pVg5ArA", "()I", "kotlin-stdlib"})
    static final class Iterator
    implements java.util.Iterator<UInt>,
    KMappedMarker {
        @NotNull
        private final int[] array;
        private int index;

        public Iterator(@NotNull int[] nArray) {
            Intrinsics.checkNotNullParameter(nArray, "");
            this.array = nArray;
        }

        @Override
        public final boolean hasNext() {
            return this.index < this.array.length;
        }

        public final int next-pVg5ArA() {
            if (this.index < this.array.length) {
                int n2 = this.index;
                this.index = n2 + 1;
                return UInt.constructor-impl(this.array[n2]);
            }
            throw new NoSuchElementException(String.valueOf(this.index));
        }

        @Override
        public final void remove() {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }

        @Override
        public final /* synthetic */ Object next() {
            return UInt.box-impl(this.next-pVg5ArA());
        }
    }
}


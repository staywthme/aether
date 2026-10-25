/*
 * Decompiled with CFR 0.152.
 */
package kotlin.collections;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import kotlin.Metadata;
import kotlin.collections.EmptyIterator;
import kotlin.jvm.internal.CollectionToArray;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0010\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u001e\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0006\n\u0002\u0010(\n\u0002\b\u0002\n\u0002\u0010*\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0000\b\u00c0\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u00012\u00060\u0003j\u0002`\u00042\u00060\u0005j\u0002`\u0006B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0007J\u0011\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0002H\u0096\u0002J\u0016\u0010\u0011\u001a\u00020\u000f2\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00020\u0013H\u0016J\u0013\u0010\u0014\u001a\u00020\u000f2\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0096\u0002J\u0011\u0010\u0017\u001a\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u000bH\u0096\u0002J\b\u0010\u0019\u001a\u00020\u000bH\u0016J\u0010\u0010\u001a\u001a\u00020\u000b2\u0006\u0010\u0010\u001a\u00020\u0002H\u0016J\b\u0010\u001b\u001a\u00020\u000fH\u0016J\u000f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00020\u001dH\u0096\u0002J\u0010\u0010\u001e\u001a\u00020\u000b2\u0006\u0010\u0010\u001a\u00020\u0002H\u0016J\u000e\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u00020 H\u0016J\u0016\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u00020 2\u0006\u0010\u0018\u001a\u00020\u000bH\u0016J\b\u0010!\u001a\u00020\u0016H\u0002J\u001e\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u00020\u00012\u0006\u0010#\u001a\u00020\u000b2\u0006\u0010$\u001a\u00020\u000bH\u0016J\b\u0010%\u001a\u00020&H\u0016R\u000e\u0010\b\u001a\u00020\tX\u0082T\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\u00020\u000b8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\f\u0010\r\u00a8\u0006'"}, d2={"Lkotlin/collections/EmptyList;", "", "", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "Ljava/util/RandomAccess;", "Lkotlin/collections/RandomAccess;", "()V", "serialVersionUID", "", "size", "", "getSize", "()I", "contains", "", "element", "containsAll", "elements", "", "equals", "other", "", "get", "index", "hashCode", "indexOf", "isEmpty", "iterator", "", "lastIndexOf", "listIterator", "", "readResolve", "subList", "fromIndex", "toIndex", "toString", "", "kotlin-stdlib"})
public final class EmptyList
implements Serializable,
List,
RandomAccess,
KMappedMarker {
    @NotNull
    public static final EmptyList INSTANCE = new EmptyList();
    private static final long serialVersionUID = -7390468764508069838L;

    private EmptyList() {
    }

    @Override
    public final boolean equals(@Nullable Object object) {
        return object instanceof List && ((List)object).isEmpty();
    }

    @Override
    public final int hashCode() {
        return 1;
    }

    @NotNull
    public final String toString() {
        return "[]";
    }

    public final int getSize() {
        return 0;
    }

    @Override
    public final boolean isEmpty() {
        return true;
    }

    public final boolean contains(@NotNull Void void_) {
        Intrinsics.checkNotNullParameter(void_, "");
        return false;
    }

    @Override
    public final boolean containsAll(@NotNull Collection collection) {
        Intrinsics.checkNotNullParameter(collection, "");
        return collection.isEmpty();
    }

    @NotNull
    public final Void get(int n2) {
        throw new IndexOutOfBoundsException("Empty list doesn't contain element at index " + n2 + '.');
    }

    public final int indexOf(@NotNull Void void_) {
        Intrinsics.checkNotNullParameter(void_, "");
        return -1;
    }

    public final int lastIndexOf(@NotNull Void void_) {
        Intrinsics.checkNotNullParameter(void_, "");
        return -1;
    }

    @Override
    @NotNull
    public final Iterator iterator() {
        return EmptyIterator.INSTANCE;
    }

    @NotNull
    public final ListIterator listIterator() {
        return EmptyIterator.INSTANCE;
    }

    @NotNull
    public final ListIterator listIterator(int n2) {
        if (n2 != 0) {
            throw new IndexOutOfBoundsException("Index: " + n2);
        }
        return EmptyIterator.INSTANCE;
    }

    @NotNull
    public final List subList(int n2, int n3) {
        if (n2 == 0 && n3 == 0) {
            return this;
        }
        throw new IndexOutOfBoundsException("fromIndex: " + n2 + ", toIndex: " + n3);
    }

    private final Object readResolve() {
        return INSTANCE;
    }

    @Override
    public final boolean add(Void void_) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public final void add(int n2, Void void_) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public final boolean addAll(int n2, Collection collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override
    public final boolean addAll(Collection collection) {
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
    public final boolean removeAll(Collection collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public final Void remove(int n2) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public final /* synthetic */ Object remove(int n2) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override
    public final boolean retainAll(Collection collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public final Void set(int n2, Void void_) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override
    public final int size() {
        return this.getSize();
    }

    @Override
    public final /* synthetic */ boolean add(Object object) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public final /* synthetic */ void add(int n2, Object object) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public final /* synthetic */ Object set(int n2, Object object) {
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
}


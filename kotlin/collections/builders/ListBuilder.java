/*
 * Decompiled with CFR 0.152.
 */
package kotlin.collections.builders;

import java.io.NotSerializableException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;
import kotlin.Metadata;
import kotlin.collections.AbstractList;
import kotlin.collections.AbstractMutableList;
import kotlin.collections.ArrayDeque;
import kotlin.collections.ArraysKt;
import kotlin.collections.builders.ListBuilderKt;
import kotlin.collections.builders.SerializedCollection;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.jvm.internal.markers.KMutableList;
import kotlin.jvm.internal.markers.KMutableListIterator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000r\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\f\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u001e\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\b\b\n\u0002\u0010\u0000\n\u0002\b\b\n\u0002\u0010)\n\u0002\b\u0002\n\u0002\u0010+\n\u0002\b\u0015\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0000\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\u00060\u0003j\u0002`\u00042\b\u0012\u0004\u0012\u0002H\u00010\u00052\u00060\u0006j\u0002`\u0007:\u0001VB\u0007\b\u0016\u00a2\u0006\u0002\u0010\bB\u000f\b\u0016\u0012\u0006\u0010\t\u001a\u00020\n\u00a2\u0006\u0002\u0010\u000bBM\b\u0002\u0012\f\u0010\f\u001a\b\u0012\u0004\u0012\u00028\u00000\r\u0012\u0006\u0010\u000e\u001a\u00020\n\u0012\u0006\u0010\u000f\u001a\u00020\n\u0012\u0006\u0010\u0010\u001a\u00020\u0011\u0012\u000e\u0010\u0012\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0000\u0012\u000e\u0010\u0013\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0000\u00a2\u0006\u0002\u0010\u0014J\u0015\u0010\u001b\u001a\u00020\u00112\u0006\u0010\u001c\u001a\u00028\u0000H\u0016\u00a2\u0006\u0002\u0010\u001dJ\u001d\u0010\u001b\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\n2\u0006\u0010\u001c\u001a\u00028\u0000H\u0016\u00a2\u0006\u0002\u0010 J\u001e\u0010!\u001a\u00020\u00112\u0006\u0010\u001f\u001a\u00020\n2\f\u0010\"\u001a\b\u0012\u0004\u0012\u00028\u00000#H\u0016J\u0016\u0010!\u001a\u00020\u00112\f\u0010\"\u001a\b\u0012\u0004\u0012\u00028\u00000#H\u0016J&\u0010$\u001a\u00020\u001e2\u0006\u0010%\u001a\u00020\n2\f\u0010\"\u001a\b\u0012\u0004\u0012\u00028\u00000#2\u0006\u0010&\u001a\u00020\nH\u0002J\u001d\u0010'\u001a\u00020\u001e2\u0006\u0010%\u001a\u00020\n2\u0006\u0010\u001c\u001a\u00028\u0000H\u0002\u00a2\u0006\u0002\u0010 J\f\u0010(\u001a\b\u0012\u0004\u0012\u00028\u00000)J\b\u0010*\u001a\u00020\u001eH\u0002J\b\u0010+\u001a\u00020\u001eH\u0016J\u0014\u0010,\u001a\u00020\u00112\n\u0010-\u001a\u0006\u0012\u0002\b\u00030)H\u0002J\u0010\u0010.\u001a\u00020\u001e2\u0006\u0010/\u001a\u00020\nH\u0002J\u0010\u00100\u001a\u00020\u001e2\u0006\u0010&\u001a\u00020\nH\u0002J\u0013\u00101\u001a\u00020\u00112\b\u0010-\u001a\u0004\u0018\u000102H\u0096\u0002J\u0016\u00103\u001a\u00028\u00002\u0006\u0010\u001f\u001a\u00020\nH\u0096\u0002\u00a2\u0006\u0002\u00104J\b\u00105\u001a\u00020\nH\u0016J\u0015\u00106\u001a\u00020\n2\u0006\u0010\u001c\u001a\u00028\u0000H\u0016\u00a2\u0006\u0002\u00107J\u0018\u00108\u001a\u00020\u001e2\u0006\u0010%\u001a\u00020\n2\u0006\u0010&\u001a\u00020\nH\u0002J\b\u00109\u001a\u00020\u0011H\u0016J\u000f\u0010:\u001a\b\u0012\u0004\u0012\u00028\u00000;H\u0096\u0002J\u0015\u0010<\u001a\u00020\n2\u0006\u0010\u001c\u001a\u00028\u0000H\u0016\u00a2\u0006\u0002\u00107J\u000e\u0010=\u001a\b\u0012\u0004\u0012\u00028\u00000>H\u0016J\u0016\u0010=\u001a\b\u0012\u0004\u0012\u00028\u00000>2\u0006\u0010\u001f\u001a\u00020\nH\u0016J\u0015\u0010?\u001a\u00020\u00112\u0006\u0010\u001c\u001a\u00028\u0000H\u0016\u00a2\u0006\u0002\u0010\u001dJ\u0016\u0010@\u001a\u00020\u00112\f\u0010\"\u001a\b\u0012\u0004\u0012\u00028\u00000#H\u0016J\u0015\u0010A\u001a\u00028\u00002\u0006\u0010\u001f\u001a\u00020\nH\u0016\u00a2\u0006\u0002\u00104J\u0015\u0010B\u001a\u00028\u00002\u0006\u0010%\u001a\u00020\nH\u0002\u00a2\u0006\u0002\u00104J\u0018\u0010C\u001a\u00020\u001e2\u0006\u0010D\u001a\u00020\n2\u0006\u0010E\u001a\u00020\nH\u0002J\u0016\u0010F\u001a\u00020\u00112\f\u0010\"\u001a\b\u0012\u0004\u0012\u00028\u00000#H\u0016J.\u0010G\u001a\u00020\n2\u0006\u0010D\u001a\u00020\n2\u0006\u0010E\u001a\u00020\n2\f\u0010\"\u001a\b\u0012\u0004\u0012\u00028\u00000#2\u0006\u0010H\u001a\u00020\u0011H\u0002J\u001e\u0010I\u001a\u00028\u00002\u0006\u0010\u001f\u001a\u00020\n2\u0006\u0010\u001c\u001a\u00028\u0000H\u0096\u0002\u00a2\u0006\u0002\u0010JJ\u001e\u0010K\u001a\b\u0012\u0004\u0012\u00028\u00000\u00022\u0006\u0010L\u001a\u00020\n2\u0006\u0010M\u001a\u00020\nH\u0016J\u0015\u0010N\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001020\rH\u0016\u00a2\u0006\u0002\u0010OJ'\u0010N\u001a\b\u0012\u0004\u0012\u0002HP0\r\"\u0004\b\u0001\u0010P2\f\u0010Q\u001a\b\u0012\u0004\u0012\u0002HP0\rH\u0016\u00a2\u0006\u0002\u0010RJ\b\u0010S\u001a\u00020TH\u0016J\b\u0010U\u001a\u000202H\u0002R\u0016\u0010\f\u001a\b\u0012\u0004\u0012\u00028\u00000\rX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u0015R\u0016\u0010\u0012\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0000X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0016\u001a\u00020\u00118BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0016\u0010\u0017R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0013\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0000X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0018\u001a\u00020\n8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0019\u0010\u001a\u00a8\u0006W"}, d2={"Lkotlin/collections/builders/ListBuilder;", "E", "", "Ljava/util/RandomAccess;", "Lkotlin/collections/RandomAccess;", "Lkotlin/collections/AbstractMutableList;", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "()V", "initialCapacity", "", "(I)V", "array", "", "offset", "length", "isReadOnly", "", "backing", "root", "([Ljava/lang/Object;IIZLkotlin/collections/builders/ListBuilder;Lkotlin/collections/builders/ListBuilder;)V", "[Ljava/lang/Object;", "isEffectivelyReadOnly", "()Z", "size", "getSize", "()I", "add", "element", "(Ljava/lang/Object;)Z", "", "index", "(ILjava/lang/Object;)V", "addAll", "elements", "", "addAllInternal", "i", "n", "addAtInternal", "build", "", "checkIsMutable", "clear", "contentEquals", "other", "ensureCapacity", "minCapacity", "ensureExtraCapacity", "equals", "", "get", "(I)Ljava/lang/Object;", "hashCode", "indexOf", "(Ljava/lang/Object;)I", "insertAtInternal", "isEmpty", "iterator", "", "lastIndexOf", "listIterator", "", "remove", "removeAll", "removeAt", "removeAtInternal", "removeRangeInternal", "rangeOffset", "rangeLength", "retainAll", "retainOrRemoveAllInternal", "retain", "set", "(ILjava/lang/Object;)Ljava/lang/Object;", "subList", "fromIndex", "toIndex", "toArray", "()[Ljava/lang/Object;", "T", "destination", "([Ljava/lang/Object;)[Ljava/lang/Object;", "toString", "", "writeReplace", "Itr", "kotlin-stdlib"})
public final class ListBuilder<E>
extends AbstractMutableList<E>
implements Serializable,
List<E>,
RandomAccess,
KMutableList {
    @NotNull
    private E[] array;
    private int offset;
    private int length;
    private boolean isReadOnly;
    @Nullable
    private final ListBuilder<E> backing;
    @Nullable
    private final ListBuilder<E> root;

    private ListBuilder(E[] EArray, int n2, int n3, boolean bl, ListBuilder<E> listBuilder, ListBuilder<E> listBuilder2) {
        this.array = EArray;
        this.offset = n2;
        this.length = n3;
        this.isReadOnly = bl;
        this.backing = listBuilder;
        this.root = listBuilder2;
    }

    public ListBuilder() {
        this(10);
    }

    public ListBuilder(int n2) {
        this(ListBuilderKt.arrayOfUninitializedElements(n2), 0, 0, false, null, null);
    }

    @NotNull
    public final List<E> build() {
        if (this.backing != null) {
            throw new IllegalStateException();
        }
        this.checkIsMutable();
        this.isReadOnly = true;
        return this;
    }

    private final Object writeReplace() {
        if (this.isEffectivelyReadOnly()) {
            return new SerializedCollection(this, 0);
        }
        throw new NotSerializableException("The list cannot be serialized while it is being built.");
    }

    @Override
    public final int getSize() {
        return this.length;
    }

    @Override
    public final boolean isEmpty() {
        return this.length == 0;
    }

    @Override
    public final E get(int n2) {
        AbstractList.Companion.checkElementIndex$kotlin_stdlib(n2, this.length);
        return this.array[this.offset + n2];
    }

    @Override
    public final E set(int n2, E e2) {
        this.checkIsMutable();
        AbstractList.Companion.checkElementIndex$kotlin_stdlib(n2, this.length);
        E e3 = this.array[this.offset + n2];
        this.array[this.offset + n2] = e2;
        return e3;
    }

    @Override
    public final int indexOf(Object object) {
        for (int i2 = 0; i2 < this.length; ++i2) {
            if (!Intrinsics.areEqual(this.array[this.offset + i2], object)) continue;
            return i2;
        }
        return -1;
    }

    @Override
    public final int lastIndexOf(Object object) {
        for (int i2 = this.length - 1; i2 >= 0; --i2) {
            if (!Intrinsics.areEqual(this.array[this.offset + i2], object)) continue;
            return i2;
        }
        return -1;
    }

    @Override
    @NotNull
    public final Iterator<E> iterator() {
        return new Itr(this, 0);
    }

    @Override
    @NotNull
    public final ListIterator<E> listIterator() {
        return new Itr(this, 0);
    }

    @Override
    @NotNull
    public final ListIterator<E> listIterator(int n2) {
        AbstractList.Companion.checkPositionIndex$kotlin_stdlib(n2, this.length);
        return new Itr(this, n2);
    }

    @Override
    public final boolean add(E e2) {
        this.checkIsMutable();
        ListBuilder listBuilder = this;
        listBuilder.addAtInternal(listBuilder.offset + this.length, e2);
        return true;
    }

    @Override
    public final void add(int n2, E e2) {
        this.checkIsMutable();
        AbstractList.Companion.checkPositionIndex$kotlin_stdlib(n2, this.length);
        ListBuilder listBuilder = this;
        listBuilder.addAtInternal(listBuilder.offset + n2, e2);
    }

    @Override
    public final boolean addAll(@NotNull Collection<? extends E> collection) {
        Intrinsics.checkNotNullParameter(collection, "");
        this.checkIsMutable();
        int n2 = collection.size();
        ListBuilder listBuilder = this;
        listBuilder.addAllInternal(listBuilder.offset + this.length, collection, n2);
        return n2 > 0;
    }

    @Override
    public final boolean addAll(int n2, @NotNull Collection<? extends E> collection) {
        Intrinsics.checkNotNullParameter(collection, "");
        this.checkIsMutable();
        AbstractList.Companion.checkPositionIndex$kotlin_stdlib(n2, this.length);
        int n3 = collection.size();
        ListBuilder listBuilder = this;
        listBuilder.addAllInternal(listBuilder.offset + n2, collection, n3);
        return n3 > 0;
    }

    @Override
    public final void clear() {
        this.checkIsMutable();
        ListBuilder listBuilder = this;
        listBuilder.removeRangeInternal(listBuilder.offset, this.length);
    }

    @Override
    public final E removeAt(int n2) {
        this.checkIsMutable();
        AbstractList.Companion.checkElementIndex$kotlin_stdlib(n2, this.length);
        ListBuilder listBuilder = this;
        return listBuilder.removeAtInternal(listBuilder.offset + n2);
    }

    @Override
    public final boolean remove(Object object) {
        this.checkIsMutable();
        int n2 = this.indexOf(object);
        if (n2 >= 0) {
            this.remove(n2);
        }
        return n2 >= 0;
    }

    @Override
    public final boolean removeAll(@NotNull Collection<? extends Object> collection) {
        Intrinsics.checkNotNullParameter(collection, "");
        this.checkIsMutable();
        ListBuilder listBuilder = this;
        return listBuilder.retainOrRemoveAllInternal(listBuilder.offset, this.length, collection, false) > 0;
    }

    @Override
    public final boolean retainAll(@NotNull Collection<? extends Object> collection) {
        Intrinsics.checkNotNullParameter(collection, "");
        this.checkIsMutable();
        ListBuilder listBuilder = this;
        return listBuilder.retainOrRemoveAllInternal(listBuilder.offset, this.length, collection, true) > 0;
    }

    @Override
    @NotNull
    public final List<E> subList(int n2, int n3) {
        AbstractList.Companion.checkRangeIndexes$kotlin_stdlib(n2, n3, this.length);
        ListBuilder listBuilder = this;
        ListBuilder listBuilder2 = listBuilder.root;
        if (listBuilder2 == null) {
            listBuilder2 = this;
        }
        return new ListBuilder<E>(this.array, this.offset + n2, n3 - n2, this.isReadOnly, listBuilder, listBuilder2);
    }

    @Override
    @NotNull
    public final <T> T[] toArray(@NotNull T[] TArray) {
        Intrinsics.checkNotNullParameter(TArray, "");
        if (TArray.length < this.length) {
            T[] TArray2 = Arrays.copyOfRange(this.array, this.offset, this.offset + this.length, TArray.getClass());
            Intrinsics.checkNotNullExpressionValue(TArray2, "");
            return TArray2;
        }
        ArraysKt.copyInto(this.array, TArray, 0, this.offset, this.offset + this.length);
        if (TArray.length > this.length) {
            TArray[this.length] = null;
        }
        return TArray;
    }

    @Override
    @NotNull
    public final Object[] toArray() {
        E[] EArray = this.array;
        int n2 = this.offset;
        int n3 = this.offset + this.length;
        return ArraysKt.copyOfRange(EArray, n2, n3);
    }

    @Override
    public final boolean equals(@Nullable Object object) {
        return object == this || object instanceof List && this.contentEquals((List)object);
    }

    @Override
    public final int hashCode() {
        return ListBuilderKt.access$subarrayContentHashCode(this.array, this.offset, this.length);
    }

    @Override
    @NotNull
    public final String toString() {
        return ListBuilderKt.access$subarrayContentToString(this.array, this.offset, this.length);
    }

    private final void ensureCapacity(int n2) {
        if (this.backing != null) {
            throw new IllegalStateException();
        }
        if (n2 < 0) {
            throw new OutOfMemoryError();
        }
        if (n2 > this.array.length) {
            n2 = ArrayDeque.Companion.newCapacity$kotlin_stdlib(this.array.length, n2);
            this.array = ListBuilderKt.copyOfUninitializedElements(this.array, n2);
        }
    }

    private final void checkIsMutable() {
        if (this.isEffectivelyReadOnly()) {
            throw new UnsupportedOperationException();
        }
    }

    private final boolean isEffectivelyReadOnly() {
        return this.isReadOnly || this.root != null && this.root.isReadOnly;
    }

    private final void ensureExtraCapacity(int n2) {
        ListBuilder listBuilder = this;
        listBuilder.ensureCapacity(listBuilder.length + n2);
    }

    private final boolean contentEquals(List<?> list2) {
        return ListBuilderKt.access$subarrayContentEquals(this.array, this.offset, this.length, list2);
    }

    private final void insertAtInternal(int n2, int n3) {
        this.ensureExtraCapacity(n3);
        E[] EArray = this.array;
        E[] EArray2 = this.array;
        int n4 = this.offset + this.length;
        int n5 = n2 + n3;
        ArraysKt.copyInto(EArray, EArray2, n5, n2, n4);
        this.length += n3;
    }

    private final void addAtInternal(int n2, E e2) {
        if (this.backing != null) {
            super.addAtInternal(n2, e2);
            this.array = this.backing.array;
            n2 = this.length;
            this.length = n2 + 1;
            return;
        }
        this.insertAtInternal(n2, 1);
        this.array[n2] = e2;
    }

    private final void addAllInternal(int n2, Collection<? extends E> object, int n3) {
        if (this.backing != null) {
            super.addAllInternal(n2, (Collection<E>)object, n3);
            this.array = this.backing.array;
            this.length += n3;
            return;
        }
        this.insertAtInternal(n2, n3);
        object = object.iterator();
        for (int i2 = 0; i2 < n3; ++i2) {
            this.array[n2 + i2] = object.next();
        }
    }

    private final E removeAtInternal(int n2) {
        if (this.backing != null) {
            E e2 = super.removeAtInternal(n2);
            int n3 = this.length;
            this.length = n3 + -1;
            return e2;
        }
        E e3 = this.array[n2];
        E[] EArray = this.array;
        E[] EArray2 = this.array;
        int n4 = n2 + 1;
        int n5 = this.offset + this.length;
        ArraysKt.copyInto(EArray, EArray2, n2, n4, n5);
        ListBuilderKt.resetAt(this.array, this.offset + this.length - 1);
        int n6 = this.length;
        this.length = n6 + -1;
        return e3;
    }

    private final void removeRangeInternal(int n2, int n3) {
        if (this.backing != null) {
            super.removeRangeInternal(n2, n3);
        } else {
            E[] EArray = this.array;
            E[] EArray2 = this.array;
            int n4 = n2 + n3;
            int n5 = this.length;
            ArraysKt.copyInto(EArray, EArray2, n2, n4, n5);
            ListBuilderKt.resetRange(this.array, this.length - n3, this.length);
        }
        this.length -= n3;
    }

    private final int retainOrRemoveAllInternal(int n2, int n3, Collection<? extends E> collection, boolean bl) {
        if (this.backing != null) {
            int n4 = super.retainOrRemoveAllInternal(n2, n3, collection, bl);
            this.length -= n4;
            return n4;
        }
        int n5 = 0;
        int n6 = 0;
        while (n5 < n3) {
            if (collection.contains(this.array[n2 + n5]) == bl) {
                this.array[n2 + n6++] = this.array[n2 + n5++];
                continue;
            }
            ++n5;
        }
        int n7 = n3 - n6;
        E[] EArray = this.array;
        E[] EArray2 = this.array;
        n3 = n2 + n3;
        int n8 = this.length;
        ArraysKt.copyInto(EArray, EArray2, n2 += n6, n3, n8);
        ListBuilderKt.resetRange(this.array, this.length - n7, this.length);
        this.length -= n7;
        return n7;
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000*\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010+\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\t\b\u0002\u0018\u0000*\u0004\b\u0001\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u001d\b\u0016\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00010\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007J\u0015\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00028\u0001H\u0016\u00a2\u0006\u0002\u0010\fJ\t\u0010\r\u001a\u00020\u000eH\u0096\u0002J\b\u0010\u000f\u001a\u00020\u000eH\u0016J\u000e\u0010\u0010\u001a\u00028\u0001H\u0096\u0002\u00a2\u0006\u0002\u0010\u0011J\b\u0010\u0012\u001a\u00020\u0006H\u0016J\r\u0010\u0013\u001a\u00028\u0001H\u0016\u00a2\u0006\u0002\u0010\u0011J\b\u0010\u0014\u001a\u00020\u0006H\u0016J\b\u0010\u0015\u001a\u00020\nH\u0016J\u0015\u0010\u0016\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00028\u0001H\u0016\u00a2\u0006\u0002\u0010\fR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00010\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2={"Lkotlin/collections/builders/ListBuilder$Itr;", "E", "", "list", "Lkotlin/collections/builders/ListBuilder;", "index", "", "(Lkotlin/collections/builders/ListBuilder;I)V", "lastIndex", "add", "", "element", "(Ljava/lang/Object;)V", "hasNext", "", "hasPrevious", "next", "()Ljava/lang/Object;", "nextIndex", "previous", "previousIndex", "remove", "set", "kotlin-stdlib"})
    @SourceDebugExtension(value={"SMAP\nListBuilder.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ListBuilder.kt\nkotlin/collections/builders/ListBuilder$Itr\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,429:1\n1#2:430\n*E\n"})
    static final class Itr<E>
    implements ListIterator<E>,
    KMutableListIterator {
        @NotNull
        private final ListBuilder<E> list;
        private int index;
        private int lastIndex;

        public Itr(@NotNull ListBuilder<E> listBuilder, int n2) {
            Intrinsics.checkNotNullParameter(listBuilder, "");
            this.list = listBuilder;
            this.index = n2;
            this.lastIndex = -1;
        }

        @Override
        public final boolean hasPrevious() {
            return this.index > 0;
        }

        @Override
        public final boolean hasNext() {
            return this.index < ((ListBuilder)this.list).length;
        }

        @Override
        public final int previousIndex() {
            return this.index - 1;
        }

        @Override
        public final int nextIndex() {
            return this.index;
        }

        @Override
        public final E previous() {
            if (this.index <= 0) {
                throw new NoSuchElementException();
            }
            Itr itr = this;
            itr.index = this.index + -1;
            itr.lastIndex = this.index;
            return (E)((ListBuilder)this.list).array[((ListBuilder)this.list).offset + this.lastIndex];
        }

        @Override
        public final E next() {
            if (this.index >= ((ListBuilder)this.list).length) {
                throw new NoSuchElementException();
            }
            int n2 = this.index;
            this.index = n2 + 1;
            this.lastIndex = n2;
            return (E)((ListBuilder)this.list).array[((ListBuilder)this.list).offset + this.lastIndex];
        }

        @Override
        public final void set(E object) {
            boolean bl = this.lastIndex != -1;
            if (!bl) {
                object = "Call next() or previous() before replacing element from the iterator.";
                throw new IllegalStateException(object.toString());
            }
            ((AbstractMutableList)this.list).set(this.lastIndex, object);
        }

        @Override
        public final void add(E e2) {
            int n2 = this.index;
            this.index = n2 + 1;
            ((AbstractMutableList)this.list).add(n2, e2);
            this.lastIndex = -1;
        }

        @Override
        public final void remove() {
            boolean bl = this.lastIndex != -1;
            if (!bl) {
                String string = "Call next() or previous() before removing element from the iterator.";
                throw new IllegalStateException(string.toString());
            }
            this.list.remove(this.lastIndex);
            this.index = this.lastIndex;
            this.lastIndex = -1;
        }
    }
}


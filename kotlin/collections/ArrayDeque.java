/*
 * Decompiled with CFR 0.152.
 */
package kotlin.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.ExperimentalStdlibApi;
import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.Unit;
import kotlin.WasExperimental;
import kotlin.collections.AbstractList;
import kotlin.collections.AbstractMutableList;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.internal.InlineOnly;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.ranges.RangesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000L\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u001e\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u001b\b\u0007\u0018\u0000 P*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002:\u0001PB\u000f\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\u0005B\u0007\b\u0016\u00a2\u0006\u0002\u0010\u0006B\u0015\b\u0016\u0012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\b\u00a2\u0006\u0002\u0010\tJ\u0015\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00028\u0000H\u0016\u00a2\u0006\u0002\u0010\u0016J\u001d\u0010\u0013\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00042\u0006\u0010\u0015\u001a\u00028\u0000H\u0016\u00a2\u0006\u0002\u0010\u0019J\u001e\u0010\u001a\u001a\u00020\u00142\u0006\u0010\u0018\u001a\u00020\u00042\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\bH\u0016J\u0016\u0010\u001a\u001a\u00020\u00142\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\bH\u0016J\u0013\u0010\u001b\u001a\u00020\u00172\u0006\u0010\u0015\u001a\u00028\u0000\u00a2\u0006\u0002\u0010\u001cJ\u0013\u0010\u001d\u001a\u00020\u00172\u0006\u0010\u0015\u001a\u00028\u0000\u00a2\u0006\u0002\u0010\u001cJ\b\u0010\u001e\u001a\u00020\u0017H\u0016J\u0016\u0010\u001f\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00028\u0000H\u0096\u0002\u00a2\u0006\u0002\u0010\u0016J\u001e\u0010 \u001a\u00020\u00172\u0006\u0010!\u001a\u00020\u00042\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\bH\u0002J\u0010\u0010\"\u001a\u00020\u00172\u0006\u0010#\u001a\u00020\u0004H\u0002J\u0010\u0010$\u001a\u00020\u00042\u0006\u0010\u0018\u001a\u00020\u0004H\u0002J\u0010\u0010%\u001a\u00020\u00172\u0006\u0010&\u001a\u00020\u0004H\u0002J\u001d\u0010'\u001a\u00020\u00142\u0012\u0010(\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u00140)H\u0082\bJ\u000b\u0010*\u001a\u00028\u0000\u00a2\u0006\u0002\u0010+J\r\u0010,\u001a\u0004\u0018\u00018\u0000\u00a2\u0006\u0002\u0010+J\u0016\u0010-\u001a\u00028\u00002\u0006\u0010\u0018\u001a\u00020\u0004H\u0096\u0002\u00a2\u0006\u0002\u0010.J\u0010\u0010/\u001a\u00020\u00042\u0006\u0010\u0018\u001a\u00020\u0004H\u0002J\u0015\u00100\u001a\u00020\u00042\u0006\u0010\u0015\u001a\u00028\u0000H\u0016\u00a2\u0006\u0002\u00101J\u0016\u00102\u001a\u00028\u00002\u0006\u0010!\u001a\u00020\u0004H\u0083\b\u00a2\u0006\u0002\u0010.J\u0011\u0010!\u001a\u00020\u00042\u0006\u0010\u0018\u001a\u00020\u0004H\u0083\bJM\u00103\u001a\u00020\u00172>\u00104\u001a:\u0012\u0013\u0012\u00110\u0004\u00a2\u0006\f\b6\u0012\b\b7\u0012\u0004\b\b(\u000e\u0012\u001b\u0012\u0019\u0012\u0006\u0012\u0004\u0018\u00010\f0\u000b\u00a2\u0006\f\b6\u0012\b\b7\u0012\u0004\b\b(\u0007\u0012\u0004\u0012\u00020\u001705H\u0000\u00a2\u0006\u0002\b8J\b\u00109\u001a\u00020\u0014H\u0016J\u000b\u0010:\u001a\u00028\u0000\u00a2\u0006\u0002\u0010+J\u0015\u0010;\u001a\u00020\u00042\u0006\u0010\u0015\u001a\u00028\u0000H\u0016\u00a2\u0006\u0002\u00101J\r\u0010<\u001a\u0004\u0018\u00018\u0000\u00a2\u0006\u0002\u0010+J\u0010\u0010=\u001a\u00020\u00042\u0006\u0010\u0018\u001a\u00020\u0004H\u0002J\u0010\u0010>\u001a\u00020\u00042\u0006\u0010\u0018\u001a\u00020\u0004H\u0002J\u0015\u0010?\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00028\u0000H\u0016\u00a2\u0006\u0002\u0010\u0016J\u0016\u0010@\u001a\u00020\u00142\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\bH\u0016J\u0015\u0010A\u001a\u00028\u00002\u0006\u0010\u0018\u001a\u00020\u0004H\u0016\u00a2\u0006\u0002\u0010.J\u000b\u0010B\u001a\u00028\u0000\u00a2\u0006\u0002\u0010+J\r\u0010C\u001a\u0004\u0018\u00018\u0000\u00a2\u0006\u0002\u0010+J\u000b\u0010D\u001a\u00028\u0000\u00a2\u0006\u0002\u0010+J\r\u0010E\u001a\u0004\u0018\u00018\u0000\u00a2\u0006\u0002\u0010+J\u0016\u0010F\u001a\u00020\u00142\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\bH\u0016J\u001e\u0010G\u001a\u00028\u00002\u0006\u0010\u0018\u001a\u00020\u00042\u0006\u0010\u0015\u001a\u00028\u0000H\u0096\u0002\u00a2\u0006\u0002\u0010HJ\u0017\u0010I\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\f0\u000bH\u0000\u00a2\u0006\u0004\bJ\u0010KJ)\u0010I\u001a\b\u0012\u0004\u0012\u0002HL0\u000b\"\u0004\b\u0001\u0010L2\f\u0010M\u001a\b\u0012\u0004\u0012\u0002HL0\u000bH\u0000\u00a2\u0006\u0004\bJ\u0010NJ\u0015\u0010O\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\f0\u000bH\u0016\u00a2\u0006\u0002\u0010KJ'\u0010O\u001a\b\u0012\u0004\u0012\u0002HL0\u000b\"\u0004\b\u0001\u0010L2\f\u0010M\u001a\b\u0012\u0004\u0012\u0002HL0\u000bH\u0016\u00a2\u0006\u0002\u0010NR\u0018\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\f0\u000bX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\rR\u000e\u0010\u000e\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0010\u001a\u00020\u00042\u0006\u0010\u000f\u001a\u00020\u0004@RX\u0096\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012\u00a8\u0006Q"}, d2={"Lkotlin/collections/ArrayDeque;", "E", "Lkotlin/collections/AbstractMutableList;", "initialCapacity", "", "(I)V", "()V", "elements", "", "(Ljava/util/Collection;)V", "elementData", "", "", "[Ljava/lang/Object;", "head", "<set-?>", "size", "getSize", "()I", "add", "", "element", "(Ljava/lang/Object;)Z", "", "index", "(ILjava/lang/Object;)V", "addAll", "addFirst", "(Ljava/lang/Object;)V", "addLast", "clear", "contains", "copyCollectionElements", "internalIndex", "copyElements", "newCapacity", "decremented", "ensureCapacity", "minCapacity", "filterInPlace", "predicate", "Lkotlin/Function1;", "first", "()Ljava/lang/Object;", "firstOrNull", "get", "(I)Ljava/lang/Object;", "incremented", "indexOf", "(Ljava/lang/Object;)I", "internalGet", "internalStructure", "structure", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", "internalStructure$kotlin_stdlib", "isEmpty", "last", "lastIndexOf", "lastOrNull", "negativeMod", "positiveMod", "remove", "removeAll", "removeAt", "removeFirst", "removeFirstOrNull", "removeLast", "removeLastOrNull", "retainAll", "set", "(ILjava/lang/Object;)Ljava/lang/Object;", "testToArray", "testToArray$kotlin_stdlib", "()[Ljava/lang/Object;", "T", "array", "([Ljava/lang/Object;)[Ljava/lang/Object;", "toArray", "Companion", "kotlin-stdlib"})
@SinceKotlin(version="1.4")
@WasExperimental(markerClass={ExperimentalStdlibApi.class})
@SourceDebugExtension(value={"SMAP\nArrayDeque.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ArrayDeque.kt\nkotlin/collections/ArrayDeque\n+ 2 ArraysJVM.kt\nkotlin/collections/ArraysKt__ArraysJVMKt\n+ 3 ArrayIntrinsics.kt\nkotlin/ArrayIntrinsicsKt\n*L\n1#1,583:1\n467#1,51:586\n467#1,51:637\n37#2,2:584\n26#3:688\n*S KotlinDebug\n*F\n+ 1 ArrayDeque.kt\nkotlin/collections/ArrayDeque\n*L\n462#1:586,51\n464#1:637,51\n47#1:584,2\n562#1:688\n*E\n"})
public final class ArrayDeque<E>
extends AbstractMutableList<E> {
    @NotNull
    public static final Companion Companion = new Companion(null);
    private int head;
    @NotNull
    private Object[] elementData;
    private int size;
    @NotNull
    private static final Object[] emptyElementData = new Object[0];
    private static final int maxArraySize = 0x7FFFFFF7;
    private static final int defaultMinCapacity = 10;

    @Override
    public final int getSize() {
        return this.size;
    }

    public ArrayDeque(int n2) {
        Object[] objectArray;
        if (n2 == 0) {
            objectArray = emptyElementData;
        } else if (n2 > 0) {
            objectArray = new Object[n2];
        } else {
            throw new IllegalArgumentException("Illegal Capacity: " + n2);
        }
        this.elementData = objectArray;
    }

    public ArrayDeque() {
        this.elementData = emptyElementData;
    }

    public ArrayDeque(@NotNull Collection<? extends E> collection) {
        Intrinsics.checkNotNullParameter(collection, "");
        Collection<E> collection2 = collection;
        collection = collection2;
        collection = collection2;
        this.elementData = collection2.toArray(new Object[0]);
        this.size = this.elementData.length;
        if (this.elementData.length == 0) {
            this.elementData = emptyElementData;
        }
    }

    private final void ensureCapacity(int n2) {
        if (n2 < 0) {
            throw new IllegalStateException("Deque is too big.");
        }
        if (n2 <= this.elementData.length) {
            return;
        }
        if (this.elementData == emptyElementData) {
            this.elementData = new Object[RangesKt.coerceAtLeast(n2, 10)];
            return;
        }
        n2 = Companion.newCapacity$kotlin_stdlib(this.elementData.length, n2);
        this.copyElements(n2);
    }

    private final void copyElements(int n2) {
        Object[] objectArray = new Object[n2];
        ArraysKt.copyInto(this.elementData, objectArray, 0, this.head, this.elementData.length);
        ArraysKt.copyInto(this.elementData, objectArray, this.elementData.length - this.head, 0, this.head);
        this.head = 0;
        this.elementData = objectArray;
    }

    @InlineOnly
    private final E internalGet(int n2) {
        return (E)this.elementData[n2];
    }

    private final int positiveMod(int n2) {
        if (n2 >= this.elementData.length) {
            return n2 - this.elementData.length;
        }
        return n2;
    }

    private final int negativeMod(int n2) {
        if (n2 < 0) {
            return n2 + this.elementData.length;
        }
        return n2;
    }

    @InlineOnly
    private final int internalIndex(int n2) {
        ArrayDeque arrayDeque = this;
        return arrayDeque.positiveMod(arrayDeque.head + n2);
    }

    private final int incremented(int n2) {
        if (n2 == ArraysKt.getLastIndex(this.elementData)) {
            return 0;
        }
        return n2 + 1;
    }

    private final int decremented(int n2) {
        if (n2 == 0) {
            return ArraysKt.getLastIndex(this.elementData);
        }
        return n2 - 1;
    }

    @Override
    public final boolean isEmpty() {
        return this.size() == 0;
    }

    public final E first() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("ArrayDeque is empty.");
        }
        return (E)this.elementData[this.head];
    }

    @Nullable
    public final E firstOrNull() {
        if (this.isEmpty()) {
            return null;
        }
        return (E)this.elementData[this.head];
    }

    public final E last() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("ArrayDeque is empty.");
        }
        ArrayDeque arrayDeque = this;
        return (E)this.elementData[arrayDeque.positiveMod(arrayDeque.head + CollectionsKt.getLastIndex(this))];
    }

    @Nullable
    public final E lastOrNull() {
        if (this.isEmpty()) {
            return null;
        }
        ArrayDeque arrayDeque = this;
        return (E)this.elementData[arrayDeque.positiveMod(arrayDeque.head + CollectionsKt.getLastIndex(this))];
    }

    public final void addFirst(E e2) {
        ArrayDeque arrayDeque = this;
        arrayDeque.ensureCapacity(arrayDeque.size() + 1);
        this.head = this.decremented(this.head);
        this.elementData[this.head] = e2;
        this.size = this.size() + 1;
    }

    public final void addLast(E e2) {
        ArrayDeque arrayDeque = this;
        arrayDeque.ensureCapacity(arrayDeque.size() + 1);
        ArrayDeque arrayDeque2 = this;
        this.elementData[arrayDeque2.positiveMod((int)(arrayDeque2.head + this.size()))] = e2;
        this.size = this.size() + 1;
    }

    public final E removeFirst() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("ArrayDeque is empty.");
        }
        Object object = this.elementData[this.head];
        this.elementData[this.head] = null;
        this.head = this.incremented(this.head);
        this.size = this.size() - 1;
        return (E)object;
    }

    @Nullable
    public final E removeFirstOrNull() {
        if (this.isEmpty()) {
            return null;
        }
        return this.removeFirst();
    }

    public final E removeLast() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("ArrayDeque is empty.");
        }
        ArrayDeque arrayDeque = this;
        int n2 = arrayDeque.positiveMod(arrayDeque.head + CollectionsKt.getLastIndex(this));
        Object object = this.elementData[n2];
        this.elementData[n2] = null;
        this.size = this.size() - 1;
        return (E)object;
    }

    @Nullable
    public final E removeLastOrNull() {
        if (this.isEmpty()) {
            return null;
        }
        return this.removeLast();
    }

    @Override
    public final boolean add(E e2) {
        this.addLast(e2);
        return true;
    }

    @Override
    public final void add(int n2, E e2) {
        AbstractList.Companion.checkPositionIndex$kotlin_stdlib(n2, this.size());
        if (n2 == this.size()) {
            this.addLast(e2);
            return;
        }
        if (n2 == 0) {
            this.addFirst(e2);
            return;
        }
        ArrayDeque arrayDeque = this;
        arrayDeque.ensureCapacity(arrayDeque.size() + 1);
        ArrayDeque arrayDeque2 = this;
        int n3 = arrayDeque2.positiveMod(arrayDeque2.head + n2);
        if (n2 < this.size() + 1 >> 1) {
            n2 = this.decremented(n3);
            ArrayDeque arrayDeque3 = this;
            n3 = arrayDeque3.decremented(arrayDeque3.head);
            if (n2 >= this.head) {
                this.elementData[n3] = this.elementData[this.head];
                ArraysKt.copyInto(this.elementData, this.elementData, this.head, this.head + 1, n2 + 1);
            } else {
                ArraysKt.copyInto(this.elementData, this.elementData, this.head - 1, this.head, this.elementData.length);
                this.elementData[this.elementData.length - 1] = this.elementData[0];
                ArraysKt.copyInto(this.elementData, this.elementData, 0, 1, n2 + 1);
            }
            this.elementData[n2] = e2;
            this.head = n3;
        } else {
            ArrayDeque arrayDeque4 = this;
            n2 = arrayDeque4.positiveMod(arrayDeque4.head + this.size());
            if (n3 < n2) {
                ArraysKt.copyInto(this.elementData, this.elementData, n3 + 1, n3, n2);
            } else {
                ArraysKt.copyInto(this.elementData, this.elementData, 1, 0, n2);
                this.elementData[0] = this.elementData[this.elementData.length - 1];
                ArraysKt.copyInto(this.elementData, this.elementData, n3 + 1, n3, this.elementData.length - 1);
            }
            this.elementData[n3] = e2;
        }
        this.size = this.size() + 1;
    }

    private final void copyCollectionElements(int n2, Collection<? extends E> collection) {
        Iterator<E> iterator2 = collection.iterator();
        int n3 = this.elementData.length;
        while (n2 < n3 && iterator2.hasNext()) {
            this.elementData[n2] = iterator2.next();
            ++n2;
        }
        n3 = this.head;
        for (n2 = 0; n2 < n3 && iterator2.hasNext(); ++n2) {
            this.elementData[n2] = iterator2.next();
        }
        this.size = this.size() + collection.size();
    }

    @Override
    public final boolean addAll(@NotNull Collection<? extends E> collection) {
        Intrinsics.checkNotNullParameter(collection, "");
        if (collection.isEmpty()) {
            return false;
        }
        ArrayDeque arrayDeque = this;
        arrayDeque.ensureCapacity(arrayDeque.size() + collection.size());
        ArrayDeque arrayDeque2 = this;
        arrayDeque2.copyCollectionElements(arrayDeque2.positiveMod(arrayDeque2.head + this.size()), collection);
        return true;
    }

    @Override
    public final boolean addAll(int n2, @NotNull Collection<? extends E> collection) {
        Intrinsics.checkNotNullParameter(collection, "");
        AbstractList.Companion.checkPositionIndex$kotlin_stdlib(n2, this.size());
        if (collection.isEmpty()) {
            return false;
        }
        if (n2 == this.size()) {
            return this.addAll(collection);
        }
        ArrayDeque arrayDeque = this;
        arrayDeque.ensureCapacity(arrayDeque.size() + collection.size());
        ArrayDeque arrayDeque2 = this;
        int n3 = arrayDeque2.positiveMod(arrayDeque2.head + this.size());
        ArrayDeque arrayDeque3 = this;
        int n4 = arrayDeque3.positiveMod(arrayDeque3.head + n2);
        int n5 = collection.size();
        if (n2 < this.size() + 1 >> 1) {
            n2 = this.head - n5;
            if (n4 >= this.head) {
                if (n2 >= 0) {
                    ArraysKt.copyInto(this.elementData, this.elementData, n2, this.head, n4);
                } else {
                    n3 = this.elementData.length - (n2 += this.elementData.length);
                    int n6 = n4 - this.head;
                    if (n3 >= n6) {
                        ArraysKt.copyInto(this.elementData, this.elementData, n2, this.head, n4);
                    } else {
                        ArraysKt.copyInto(this.elementData, this.elementData, n2, this.head, this.head + n3);
                        ArraysKt.copyInto(this.elementData, this.elementData, 0, this.head + n3, n4);
                    }
                }
            } else {
                ArraysKt.copyInto(this.elementData, this.elementData, n2, this.head, this.elementData.length);
                if (n5 >= n4) {
                    ArraysKt.copyInto(this.elementData, this.elementData, this.elementData.length - n5, 0, n4);
                } else {
                    ArraysKt.copyInto(this.elementData, this.elementData, this.elementData.length - n5, 0, n5);
                    ArraysKt.copyInto(this.elementData, this.elementData, 0, n5, n4);
                }
            }
            this.head = n2;
            ArrayDeque arrayDeque4 = this;
            arrayDeque4.copyCollectionElements(arrayDeque4.negativeMod(n4 - n5), collection);
        } else {
            n2 = n4 + n5;
            if (n4 < n3) {
                if (n3 + n5 <= this.elementData.length) {
                    ArraysKt.copyInto(this.elementData, this.elementData, n2, n4, n3);
                } else if (n2 >= this.elementData.length) {
                    ArraysKt.copyInto(this.elementData, this.elementData, n2 - this.elementData.length, n4, n3);
                } else {
                    int n7 = n3 + n5 - this.elementData.length;
                    ArraysKt.copyInto(this.elementData, this.elementData, 0, n3 - n7, n3);
                    ArraysKt.copyInto(this.elementData, this.elementData, n2, n4, n3 - n7);
                }
            } else {
                ArraysKt.copyInto(this.elementData, this.elementData, n5, 0, n3);
                if (n2 >= this.elementData.length) {
                    ArraysKt.copyInto(this.elementData, this.elementData, n2 - this.elementData.length, n4, this.elementData.length);
                } else {
                    ArraysKt.copyInto(this.elementData, this.elementData, 0, this.elementData.length - n5, this.elementData.length);
                    ArraysKt.copyInto(this.elementData, this.elementData, n2, n4, this.elementData.length - n5);
                }
            }
            this.copyCollectionElements(n4, collection);
        }
        return true;
    }

    @Override
    public final E get(int n2) {
        AbstractList.Companion.checkElementIndex$kotlin_stdlib(n2, this.size());
        ArrayDeque arrayDeque = this;
        return (E)this.elementData[arrayDeque.positiveMod(arrayDeque.head + n2)];
    }

    @Override
    public final E set(int n2, E e2) {
        AbstractList.Companion.checkElementIndex$kotlin_stdlib(n2, this.size());
        ArrayDeque arrayDeque = this;
        n2 = arrayDeque.positiveMod(arrayDeque.head + n2);
        Object object = this.elementData[n2];
        this.elementData[n2] = e2;
        return (E)object;
    }

    @Override
    public final boolean contains(Object object) {
        return this.indexOf(object) != -1;
    }

    @Override
    public final int indexOf(Object object) {
        block4: {
            int n2;
            int n3;
            block3: {
                ArrayDeque arrayDeque = this;
                n3 = arrayDeque.positiveMod(arrayDeque.head + this.size());
                if (this.head >= n3) break block3;
                for (int i2 = this.head; i2 < n3; ++i2) {
                    if (!Intrinsics.areEqual(object, this.elementData[i2])) continue;
                    return i2 - this.head;
                }
                break block4;
            }
            if (this.head < n3) break block4;
            int n4 = this.elementData.length;
            for (n2 = this.head; n2 < n4; ++n2) {
                if (!Intrinsics.areEqual(object, this.elementData[n2])) continue;
                return n2 - this.head;
            }
            for (n2 = 0; n2 < n3; ++n2) {
                if (!Intrinsics.areEqual(object, this.elementData[n2])) continue;
                return n2 + this.elementData.length - this.head;
            }
        }
        return -1;
    }

    @Override
    public final int lastIndexOf(Object object) {
        ArrayDeque arrayDeque = this;
        int n2 = arrayDeque.positiveMod(arrayDeque.head + this.size());
        if (this.head < n2) {
            int n3 = this.head;
            if (n3 <= --n2) {
                while (true) {
                    if (Intrinsics.areEqual(object, this.elementData[n2])) {
                        return n2 - this.head;
                    }
                    if (n2 != n3) {
                        --n2;
                        continue;
                    }
                    break;
                }
            }
        } else if (this.head > n2) {
            --n2;
            while (n2 >= 0) {
                if (Intrinsics.areEqual(object, this.elementData[n2])) {
                    return n2 + this.elementData.length - this.head;
                }
                --n2;
            }
            int n4 = this.head;
            n2 = ArraysKt.getLastIndex(this.elementData);
            if (n4 <= n2) {
                while (true) {
                    if (Intrinsics.areEqual(object, this.elementData[n2])) {
                        return n2 - this.head;
                    }
                    if (n2 == n4) break;
                    --n2;
                }
            }
        }
        return -1;
    }

    @Override
    public final boolean remove(Object object) {
        int n2 = this.indexOf(object);
        if (n2 == -1) {
            return false;
        }
        this.remove(n2);
        return true;
    }

    @Override
    public final E removeAt(int n2) {
        AbstractList.Companion.checkElementIndex$kotlin_stdlib(n2, this.size());
        if (n2 == CollectionsKt.getLastIndex(this)) {
            return this.removeLast();
        }
        if (n2 == 0) {
            return this.removeFirst();
        }
        ArrayDeque arrayDeque = this;
        int n3 = arrayDeque.positiveMod(arrayDeque.head + n2);
        Object object = this.elementData[n3];
        if (n2 < this.size() >> 1) {
            if (n3 >= this.head) {
                ArraysKt.copyInto(this.elementData, this.elementData, this.head + 1, this.head, n3);
            } else {
                ArraysKt.copyInto(this.elementData, this.elementData, 1, 0, n3);
                this.elementData[0] = this.elementData[this.elementData.length - 1];
                ArraysKt.copyInto(this.elementData, this.elementData, this.head + 1, this.head, this.elementData.length - 1);
            }
            this.elementData[this.head] = null;
            this.head = this.incremented(this.head);
        } else {
            ArrayDeque arrayDeque2 = this;
            n2 = arrayDeque2.positiveMod(arrayDeque2.head + CollectionsKt.getLastIndex(this));
            if (n3 <= n2) {
                int n4 = n3;
                ArraysKt.copyInto(this.elementData, this.elementData, n4, n4 + 1, n2 + 1);
            } else {
                int n5 = n3;
                ArraysKt.copyInto(this.elementData, this.elementData, n5, n5 + 1, this.elementData.length);
                this.elementData[this.elementData.length - 1] = this.elementData[0];
                ArraysKt.copyInto(this.elementData, this.elementData, 0, 1, n2 + 1);
            }
            this.elementData[n2] = null;
        }
        this.size = this.size() - 1;
        return (E)object;
    }

    @Override
    public final boolean removeAll(@NotNull Collection<? extends Object> collection) {
        Intrinsics.checkNotNullParameter(collection, "");
        ArrayDeque arrayDeque = this;
        if (arrayDeque.isEmpty() || arrayDeque.elementData.length == 0) {
            return false;
        }
        ArrayDeque arrayDeque2 = arrayDeque;
        int n2 = arrayDeque2.positiveMod(arrayDeque2.head + arrayDeque.size());
        int n3 = arrayDeque.head;
        boolean bl = false;
        if (arrayDeque.head < n2) {
            for (int i2 = arrayDeque.head; i2 < n2; ++i2) {
                Object object = arrayDeque.elementData[i2];
                Object object2 = object;
                if (!collection.contains(object2)) {
                    arrayDeque.elementData[n3++] = object;
                    continue;
                }
                bl = true;
            }
            ArraysKt.fill(arrayDeque.elementData, null, n3, n2);
        } else {
            Object object;
            int n4;
            int n5 = arrayDeque.elementData.length;
            for (n4 = arrayDeque.head; n4 < n5; ++n4) {
                Object object3 = arrayDeque.elementData[n4];
                arrayDeque.elementData[n4] = null;
                object = object3;
                if (!collection.contains(object)) {
                    arrayDeque.elementData[n3++] = object3;
                    continue;
                }
                bl = true;
            }
            n3 = arrayDeque.positiveMod(n3);
            for (n4 = 0; n4 < n2; ++n4) {
                Object object4 = arrayDeque.elementData[n4];
                arrayDeque.elementData[n4] = null;
                object = object4;
                if (!collection.contains(object)) {
                    arrayDeque.elementData[n3] = object4;
                    n3 = arrayDeque.incremented(n3);
                    continue;
                }
                bl = true;
            }
        }
        if (bl) {
            arrayDeque.size = arrayDeque.negativeMod(n3 - arrayDeque.head);
        }
        return bl;
    }

    @Override
    public final boolean retainAll(@NotNull Collection<? extends Object> collection) {
        Intrinsics.checkNotNullParameter(collection, "");
        ArrayDeque arrayDeque = this;
        if (arrayDeque.isEmpty() || arrayDeque.elementData.length == 0) {
            return false;
        }
        ArrayDeque arrayDeque2 = arrayDeque;
        int n2 = arrayDeque2.positiveMod(arrayDeque2.head + arrayDeque.size());
        int n3 = arrayDeque.head;
        boolean bl = false;
        if (arrayDeque.head < n2) {
            for (int i2 = arrayDeque.head; i2 < n2; ++i2) {
                Object object = arrayDeque.elementData[i2];
                Object object2 = object;
                if (collection.contains(object2)) {
                    arrayDeque.elementData[n3++] = object;
                    continue;
                }
                bl = true;
            }
            ArraysKt.fill(arrayDeque.elementData, null, n3, n2);
        } else {
            Object object;
            int n4;
            int n5 = arrayDeque.elementData.length;
            for (n4 = arrayDeque.head; n4 < n5; ++n4) {
                Object object3 = arrayDeque.elementData[n4];
                arrayDeque.elementData[n4] = null;
                object = object3;
                if (collection.contains(object)) {
                    arrayDeque.elementData[n3++] = object3;
                    continue;
                }
                bl = true;
            }
            n3 = arrayDeque.positiveMod(n3);
            for (n4 = 0; n4 < n2; ++n4) {
                Object object4 = arrayDeque.elementData[n4];
                arrayDeque.elementData[n4] = null;
                object = object4;
                if (collection.contains(object)) {
                    arrayDeque.elementData[n3] = object4;
                    n3 = arrayDeque.incremented(n3);
                    continue;
                }
                bl = true;
            }
        }
        if (bl) {
            arrayDeque.size = arrayDeque.negativeMod(n3 - arrayDeque.head);
        }
        return bl;
    }

    private final boolean filterInPlace(Function1<? super E, Boolean> function1) {
        if (this.isEmpty() || this.elementData.length == 0) {
            return false;
        }
        ArrayDeque arrayDeque = this;
        int n2 = arrayDeque.positiveMod(arrayDeque.head + this.size());
        int n3 = this.head;
        boolean bl = false;
        if (this.head < n2) {
            for (int i2 = this.head; i2 < n2; ++i2) {
                Object object = this.elementData[i2];
                if (function1.invoke(object).booleanValue()) {
                    this.elementData[n3++] = object;
                    continue;
                }
                bl = true;
            }
            ArraysKt.fill(this.elementData, null, n3, n2);
        } else {
            int n4;
            int n5 = this.elementData.length;
            for (n4 = this.head; n4 < n5; ++n4) {
                Object object = this.elementData[n4];
                this.elementData[n4] = null;
                if (function1.invoke(object).booleanValue()) {
                    this.elementData[n3++] = object;
                    continue;
                }
                bl = true;
            }
            n3 = this.positiveMod(n3);
            for (n4 = 0; n4 < n2; ++n4) {
                Object object = this.elementData[n4];
                this.elementData[n4] = null;
                if (function1.invoke(object).booleanValue()) {
                    this.elementData[n3] = object;
                    n3 = this.incremented(n3);
                    continue;
                }
                bl = true;
            }
        }
        if (bl) {
            this.size = this.negativeMod(n3 - this.head);
        }
        return bl;
    }

    @Override
    public final void clear() {
        ArrayDeque arrayDeque = this;
        int n2 = arrayDeque.positiveMod(arrayDeque.head + this.size());
        if (this.head < n2) {
            ArraysKt.fill(this.elementData, null, this.head, n2);
        } else if (!((Collection)this).isEmpty()) {
            ArraysKt.fill(this.elementData, null, this.head, this.elementData.length);
            ArraysKt.fill(this.elementData, null, 0, n2);
        }
        this.head = 0;
        this.size = 0;
    }

    @Override
    @NotNull
    public final <T> T[] toArray(@NotNull T[] TArray) {
        Intrinsics.checkNotNullParameter(TArray, "");
        TArray = TArray.length >= this.size() ? TArray : ArraysKt.arrayOfNulls(TArray, this.size());
        ArrayDeque arrayDeque = this;
        int n2 = arrayDeque.positiveMod(arrayDeque.head + this.size());
        if (this.head < n2) {
            ArraysKt.copyInto$default(this.elementData, TArray, 0, this.head, n2, 2, null);
        } else if (!((Collection)this).isEmpty()) {
            ArraysKt.copyInto(this.elementData, TArray, 0, this.head, this.elementData.length);
            ArraysKt.copyInto(this.elementData, TArray, this.elementData.length - this.head, 0, n2);
        }
        if (TArray.length > this.size()) {
            TArray[this.size()] = null;
        }
        return TArray;
    }

    @Override
    @NotNull
    public final Object[] toArray() {
        ArrayDeque arrayDeque = this;
        return arrayDeque.toArray(new Object[arrayDeque.size()]);
    }

    @NotNull
    public final <T> T[] testToArray$kotlin_stdlib(@NotNull T[] TArray) {
        Intrinsics.checkNotNullParameter(TArray, "");
        return this.toArray(TArray);
    }

    @NotNull
    public final Object[] testToArray$kotlin_stdlib() {
        return this.toArray();
    }

    public final void internalStructure$kotlin_stdlib(@NotNull Function2<? super Integer, ? super Object[], Unit> function2) {
        Intrinsics.checkNotNullParameter(function2, "");
        ArrayDeque arrayDeque = this;
        int n2 = arrayDeque.positiveMod(arrayDeque.head + this.size());
        n2 = this.isEmpty() || this.head < n2 ? this.head : this.head - this.elementData.length;
        function2.invoke((Integer)n2, (Object[])this.toArray());
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0007\b\u0080\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u001d\u0010\t\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u0004H\u0000\u00a2\u0006\u0002\b\fR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u0018\u0010\u0005\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u0006X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0007R\u000e\u0010\b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2={"Lkotlin/collections/ArrayDeque$Companion;", "", "()V", "defaultMinCapacity", "", "emptyElementData", "", "[Ljava/lang/Object;", "maxArraySize", "newCapacity", "oldCapacity", "minCapacity", "newCapacity$kotlin_stdlib", "kotlin-stdlib"})
    public static final class Companion {
        private Companion() {
        }

        public final int newCapacity$kotlin_stdlib(int n2, int n3) {
            int n4 = n2;
            n2 = n4 + (n4 >> 1);
            if (n2 - n3 < 0) {
                n2 = n3;
            }
            if (n2 - 0x7FFFFFF7 > 0) {
                n2 = n3 > 0x7FFFFFF7 ? Integer.MAX_VALUE : 0x7FFFFFF7;
            }
            return n2;
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }
}


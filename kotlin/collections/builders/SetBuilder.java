/*
 * Decompiled with CFR 0.152.
 */
package kotlin.collections.builders;

import java.io.NotSerializableException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import kotlin.Metadata;
import kotlin.collections.AbstractMutableSet;
import kotlin.collections.builders.MapBuilder;
import kotlin.collections.builders.SerializedCollection;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMutableSet;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000T\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010#\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u001e\n\u0000\n\u0002\u0010\"\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010)\n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0000\b\u0000\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u00032\u00060\u0004j\u0002`\u0005B\u0007\b\u0016\u00a2\u0006\u0002\u0010\u0006B\u000f\b\u0016\u0012\u0006\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tB\u0019\b\u0000\u0012\u0010\u0010\n\u001a\f\u0012\u0004\u0012\u00028\u0000\u0012\u0002\b\u00030\u000b\u00a2\u0006\u0002\u0010\fJ\u0015\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00028\u0000H\u0016\u00a2\u0006\u0002\u0010\u0013J\u0016\u0010\u0014\u001a\u00020\u00112\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00028\u00000\u0016H\u0016J\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00028\u00000\u0018J\b\u0010\u0019\u001a\u00020\u001aH\u0016J\u0016\u0010\u001b\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00028\u0000H\u0096\u0002\u00a2\u0006\u0002\u0010\u0013J\b\u0010\u001c\u001a\u00020\u0011H\u0016J\u000f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00028\u00000\u001eH\u0096\u0002J\u0015\u0010\u001f\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00028\u0000H\u0016\u00a2\u0006\u0002\u0010\u0013J\u0016\u0010 \u001a\u00020\u00112\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00028\u00000\u0016H\u0016J\u0016\u0010!\u001a\u00020\u00112\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00028\u00000\u0016H\u0016J\b\u0010\"\u001a\u00020#H\u0002R\u0018\u0010\n\u001a\f\u0012\u0004\u0012\u00028\u0000\u0012\u0002\b\u00030\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\u00020\b8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000e\u0010\u000f\u00a8\u0006$"}, d2={"Lkotlin/collections/builders/SetBuilder;", "E", "", "Lkotlin/collections/AbstractMutableSet;", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "()V", "initialCapacity", "", "(I)V", "backing", "Lkotlin/collections/builders/MapBuilder;", "(Lkotlin/collections/builders/MapBuilder;)V", "size", "getSize", "()I", "add", "", "element", "(Ljava/lang/Object;)Z", "addAll", "elements", "", "build", "", "clear", "", "contains", "isEmpty", "iterator", "", "remove", "removeAll", "retainAll", "writeReplace", "", "kotlin-stdlib"})
public final class SetBuilder<E>
extends AbstractMutableSet<E>
implements Serializable,
Set<E>,
KMutableSet {
    @NotNull
    private final MapBuilder<E, ?> backing;

    public SetBuilder(@NotNull MapBuilder<E, ?> mapBuilder) {
        Intrinsics.checkNotNullParameter(mapBuilder, "");
        this.backing = mapBuilder;
    }

    public SetBuilder() {
        this(new MapBuilder());
    }

    public SetBuilder(int n2) {
        this(new MapBuilder(n2));
    }

    @NotNull
    public final Set<E> build() {
        this.backing.build();
        return this;
    }

    private final Object writeReplace() {
        if (this.backing.isReadOnly$kotlin_stdlib()) {
            return new SerializedCollection(this, 1);
        }
        throw new NotSerializableException("The set cannot be serialized while it is being built.");
    }

    @Override
    public final int getSize() {
        return this.backing.size();
    }

    @Override
    public final boolean isEmpty() {
        return this.backing.isEmpty();
    }

    @Override
    public final boolean contains(Object object) {
        return this.backing.containsKey(object);
    }

    @Override
    public final void clear() {
        this.backing.clear();
    }

    @Override
    public final boolean add(E e2) {
        return this.backing.addKey$kotlin_stdlib(e2) >= 0;
    }

    @Override
    public final boolean remove(Object object) {
        return this.backing.removeKey$kotlin_stdlib(object) >= 0;
    }

    @Override
    @NotNull
    public final Iterator<E> iterator() {
        return this.backing.keysIterator$kotlin_stdlib();
    }

    @Override
    public final boolean addAll(@NotNull Collection<? extends E> collection) {
        Intrinsics.checkNotNullParameter(collection, "");
        this.backing.checkIsMutable$kotlin_stdlib();
        return super.addAll(collection);
    }

    @Override
    public final boolean removeAll(@NotNull Collection<? extends Object> collection) {
        Intrinsics.checkNotNullParameter(collection, "");
        this.backing.checkIsMutable$kotlin_stdlib();
        return super.removeAll(collection);
    }

    @Override
    public final boolean retainAll(@NotNull Collection<? extends Object> collection) {
        Intrinsics.checkNotNullParameter(collection, "");
        this.backing.checkIsMutable$kotlin_stdlib();
        return super.retainAll(collection);
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package kotlin.collections.builders;

import java.io.NotSerializableException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.collections.IntIterator;
import kotlin.collections.builders.ListBuilderKt;
import kotlin.collections.builders.MapBuilderEntries;
import kotlin.collections.builders.MapBuilderKeys;
import kotlin.collections.builders.MapBuilderValues;
import kotlin.collections.builders.SerializedMap;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.jvm.internal.markers.KMutableIterator;
import kotlin.jvm.internal.markers.KMutableMap;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\u00a8\u0001\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\u0015\n\u0002\b\b\n\u0002\u0010#\n\u0002\u0010'\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u001f\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010$\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010\u001e\n\u0002\b\u0003\n\u0002\u0010&\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u001a\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\b\u0000\u0018\u0000 }*\u0004\b\u0000\u0010\u0001*\u0004\b\u0001\u0010\u00022\u000e\u0012\u0004\u0012\u0002H\u0001\u0012\u0004\u0012\u0002H\u00020\u00032\u00060\u0004j\u0002`\u0005:\t}~\u007f\u0080\u0001\u0081\u0001\u0082\u0001B\u0007\b\u0016\u00a2\u0006\u0002\u0010\u0006B\u000f\b\u0016\u0012\u0006\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tBE\b\u0002\u0012\f\u0010\n\u001a\b\u0012\u0004\u0012\u00028\u00000\u000b\u0012\u000e\u0010\f\u001a\n\u0012\u0004\u0012\u00028\u0001\u0018\u00010\u000b\u0012\u0006\u0010\r\u001a\u00020\u000e\u0012\u0006\u0010\u000f\u001a\u00020\u000e\u0012\u0006\u0010\u0010\u001a\u00020\b\u0012\u0006\u0010\u0011\u001a\u00020\b\u00a2\u0006\u0002\u0010\u0012J\u0017\u00102\u001a\u00020\b2\u0006\u00103\u001a\u00028\u0000H\u0000\u00a2\u0006\u0004\b4\u00105J\u0013\u00106\u001a\b\u0012\u0004\u0012\u00028\u00010\u000bH\u0002\u00a2\u0006\u0002\u00107J\u0012\u00108\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u000109J\r\u0010:\u001a\u00020;H\u0000\u00a2\u0006\u0002\b<J\b\u0010=\u001a\u00020;H\u0016J\b\u0010>\u001a\u00020;H\u0002J\u0019\u0010?\u001a\u00020!2\n\u0010@\u001a\u0006\u0012\u0002\b\u00030AH\u0000\u00a2\u0006\u0002\bBJ!\u0010C\u001a\u00020!2\u0012\u0010D\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010EH\u0000\u00a2\u0006\u0002\bFJ\u0015\u0010G\u001a\u00020!2\u0006\u00103\u001a\u00028\u0000H\u0016\u00a2\u0006\u0002\u0010HJ\u0015\u0010I\u001a\u00020!2\u0006\u0010J\u001a\u00028\u0001H\u0016\u00a2\u0006\u0002\u0010HJ\u0018\u0010K\u001a\u00020!2\u000e\u0010L\u001a\n\u0012\u0002\b\u0003\u0012\u0002\b\u000309H\u0002J\u0010\u0010M\u001a\u00020;2\u0006\u0010\u0013\u001a\u00020\bH\u0002J\u0010\u0010N\u001a\u00020;2\u0006\u0010O\u001a\u00020\bH\u0002J\u0019\u0010P\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010QH\u0000\u00a2\u0006\u0002\bRJ\u0013\u0010S\u001a\u00020!2\b\u0010L\u001a\u0004\u0018\u00010TH\u0096\u0002J\u0015\u0010U\u001a\u00020\b2\u0006\u00103\u001a\u00028\u0000H\u0002\u00a2\u0006\u0002\u00105J\u0015\u0010V\u001a\u00020\b2\u0006\u0010J\u001a\u00028\u0001H\u0002\u00a2\u0006\u0002\u00105J\u0018\u0010W\u001a\u0004\u0018\u00018\u00012\u0006\u00103\u001a\u00028\u0000H\u0096\u0002\u00a2\u0006\u0002\u0010XJ\u0015\u0010Y\u001a\u00020\b2\u0006\u00103\u001a\u00028\u0000H\u0002\u00a2\u0006\u0002\u00105J\b\u0010Z\u001a\u00020\bH\u0016J\b\u0010[\u001a\u00020!H\u0016J\u0019\u0010\\\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010]H\u0000\u00a2\u0006\u0002\b^J\u001f\u0010_\u001a\u0004\u0018\u00018\u00012\u0006\u00103\u001a\u00028\u00002\u0006\u0010J\u001a\u00028\u0001H\u0016\u00a2\u0006\u0002\u0010`J\u001e\u0010a\u001a\u00020;2\u0014\u0010b\u001a\u0010\u0012\u0006\b\u0001\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u000109H\u0016J\"\u0010c\u001a\u00020!2\u0018\u0010b\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010E0AH\u0002J\u001c\u0010d\u001a\u00020!2\u0012\u0010D\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010EH\u0002J\u0010\u0010e\u001a\u00020!2\u0006\u0010f\u001a\u00020\bH\u0002J\u0010\u0010g\u001a\u00020;2\u0006\u0010h\u001a\u00020\bH\u0002J\u0017\u0010i\u001a\u0004\u0018\u00018\u00012\u0006\u00103\u001a\u00028\u0000H\u0016\u00a2\u0006\u0002\u0010XJ!\u0010j\u001a\u00020!2\u0012\u0010D\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010EH\u0000\u00a2\u0006\u0002\bkJ\u0010\u0010l\u001a\u00020;2\u0006\u0010m\u001a\u00020\bH\u0002J\u0017\u0010n\u001a\u00020\b2\u0006\u00103\u001a\u00028\u0000H\u0000\u00a2\u0006\u0004\bo\u00105J\u0010\u0010p\u001a\u00020;2\u0006\u0010q\u001a\u00020\bH\u0002J\u0017\u0010r\u001a\u00020!2\u0006\u0010s\u001a\u00028\u0001H\u0000\u00a2\u0006\u0004\bt\u0010HJ\u0010\u0010u\u001a\u00020!2\u0006\u0010v\u001a\u00020\bH\u0002J\b\u0010w\u001a\u00020xH\u0016J\u0019\u0010y\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010zH\u0000\u00a2\u0006\u0002\b{J\b\u0010|\u001a\u00020TH\u0002R\u0014\u0010\u0013\u001a\u00020\b8@X\u0080\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0014\u0010\u0015R&\u0010\u0016\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u00180\u00178VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0019\u0010\u001aR\u001c\u0010\u001b\u001a\u0010\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u0001\u0018\u00010\u001cX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u001e\u001a\u00020\b8BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u001f\u0010\u0015R\u001e\u0010\"\u001a\u00020!2\u0006\u0010 \u001a\u00020!@BX\u0080\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010$R\u001a\u0010%\u001a\b\u0012\u0004\u0012\u00028\u00000\u00178VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b&\u0010\u001aR\u0016\u0010\n\u001a\b\u0012\u0004\u0012\u00028\u00000\u000bX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010'R\u0016\u0010(\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010)X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010*\u001a\u00020\b2\u0006\u0010 \u001a\u00020\b@RX\u0096\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b+\u0010\u0015R\u001a\u0010,\u001a\b\u0012\u0004\u0012\u00028\u00010-8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b.\u0010/R\u0018\u0010\f\u001a\n\u0012\u0004\u0012\u00028\u0001\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010'R\u0016\u00100\u001a\n\u0012\u0004\u0012\u00028\u0001\u0018\u000101X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0083\u0001"}, d2={"Lkotlin/collections/builders/MapBuilder;", "K", "V", "", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "()V", "initialCapacity", "", "(I)V", "keysArray", "", "valuesArray", "presenceArray", "", "hashArray", "maxProbeDistance", "length", "([Ljava/lang/Object;[Ljava/lang/Object;[I[III)V", "capacity", "getCapacity$kotlin_stdlib", "()I", "entries", "", "", "getEntries", "()Ljava/util/Set;", "entriesView", "Lkotlin/collections/builders/MapBuilderEntries;", "hashShift", "hashSize", "getHashSize", "<set-?>", "", "isReadOnly", "isReadOnly$kotlin_stdlib", "()Z", "keys", "getKeys", "[Ljava/lang/Object;", "keysView", "Lkotlin/collections/builders/MapBuilderKeys;", "size", "getSize", "values", "", "getValues", "()Ljava/util/Collection;", "valuesView", "Lkotlin/collections/builders/MapBuilderValues;", "addKey", "key", "addKey$kotlin_stdlib", "(Ljava/lang/Object;)I", "allocateValuesArray", "()[Ljava/lang/Object;", "build", "", "checkIsMutable", "", "checkIsMutable$kotlin_stdlib", "clear", "compact", "containsAllEntries", "m", "", "containsAllEntries$kotlin_stdlib", "containsEntry", "entry", "", "containsEntry$kotlin_stdlib", "containsKey", "(Ljava/lang/Object;)Z", "containsValue", "value", "contentEquals", "other", "ensureCapacity", "ensureExtraCapacity", "n", "entriesIterator", "Lkotlin/collections/builders/MapBuilder$EntriesItr;", "entriesIterator$kotlin_stdlib", "equals", "", "findKey", "findValue", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", "hash", "hashCode", "isEmpty", "keysIterator", "Lkotlin/collections/builders/MapBuilder$KeysItr;", "keysIterator$kotlin_stdlib", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", "putAll", "from", "putAllEntries", "putEntry", "putRehash", "i", "rehash", "newHashSize", "remove", "removeEntry", "removeEntry$kotlin_stdlib", "removeHashAt", "removedHash", "removeKey", "removeKey$kotlin_stdlib", "removeKeyAt", "index", "removeValue", "element", "removeValue$kotlin_stdlib", "shouldCompact", "extraCapacity", "toString", "", "valuesIterator", "Lkotlin/collections/builders/MapBuilder$ValuesItr;", "valuesIterator$kotlin_stdlib", "writeReplace", "Companion", "EntriesItr", "EntryRef", "Itr", "KeysItr", "ValuesItr", "kotlin-stdlib"})
public final class MapBuilder<K, V>
implements Serializable,
Map<K, V>,
KMutableMap {
    @NotNull
    private static final Companion Companion = new Companion(null);
    @NotNull
    private K[] keysArray;
    @Nullable
    private V[] valuesArray;
    @NotNull
    private int[] presenceArray;
    @NotNull
    private int[] hashArray;
    private int maxProbeDistance;
    private int length;
    private int hashShift;
    private int size;
    @Nullable
    private MapBuilderKeys<K> keysView;
    @Nullable
    private MapBuilderValues<V> valuesView;
    @Nullable
    private MapBuilderEntries<K, V> entriesView;
    private boolean isReadOnly;
    private static final int MAGIC = -1640531527;
    private static final int INITIAL_CAPACITY = 8;
    private static final int INITIAL_MAX_PROBE_DISTANCE = 2;
    private static final int TOMBSTONE = -1;

    private MapBuilder(K[] KArray, V[] VArray, int[] nArray, int[] nArray2, int n2, int n3) {
        this.keysArray = KArray;
        this.valuesArray = VArray;
        this.presenceArray = nArray;
        this.hashArray = nArray2;
        this.maxProbeDistance = n2;
        this.length = n3;
        this.hashShift = MapBuilder.Companion.computeShift(this.getHashSize());
    }

    public final int getSize() {
        return this.size;
    }

    public final boolean isReadOnly$kotlin_stdlib() {
        return this.isReadOnly;
    }

    public MapBuilder() {
        this(8);
    }

    public MapBuilder(int n2) {
        this(ListBuilderKt.arrayOfUninitializedElements(n2), null, new int[n2], new int[MapBuilder.Companion.computeHashSize(n2)], 2, 0);
    }

    @NotNull
    public final Map<K, V> build() {
        this.checkIsMutable$kotlin_stdlib();
        this.isReadOnly = true;
        return this;
    }

    private final Object writeReplace() {
        if (this.isReadOnly) {
            return new SerializedMap(this);
        }
        throw new NotSerializableException("The map cannot be serialized while it is being built.");
    }

    @Override
    public final boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public final boolean containsKey(Object object) {
        return this.findKey(object) >= 0;
    }

    @Override
    public final boolean containsValue(Object object) {
        return this.findValue(object) >= 0;
    }

    @Override
    @Nullable
    public final V get(Object object) {
        int n2 = this.findKey(object);
        if (n2 < 0) {
            return null;
        }
        Intrinsics.checkNotNull(this.valuesArray);
        return this.valuesArray[n2];
    }

    @Override
    @Nullable
    public final V put(K k2, V v2) {
        this.checkIsMutable$kotlin_stdlib();
        int n2 = this.addKey$kotlin_stdlib(k2);
        V[] VArray = this.allocateValuesArray();
        if (n2 < 0) {
            V v3 = VArray[-n2 - 1];
            VArray[-n2 - 1] = v2;
            return v3;
        }
        VArray[n2] = v2;
        return null;
    }

    @Override
    public final void putAll(@NotNull Map<? extends K, ? extends V> map) {
        Intrinsics.checkNotNullParameter(map, "");
        this.checkIsMutable$kotlin_stdlib();
        this.putAllEntries((Collection)map.entrySet());
    }

    @Override
    @Nullable
    public final V remove(Object object) {
        int n2 = this.removeKey$kotlin_stdlib(object);
        if (n2 < 0) {
            return null;
        }
        Intrinsics.checkNotNull(this.valuesArray);
        V[] VArray = this.valuesArray;
        V v2 = VArray[n2];
        ListBuilderKt.resetAt(VArray, n2);
        return v2;
    }

    @Override
    public final void clear() {
        this.checkIsMutable$kotlin_stdlib();
        IntIterator intIterator = new IntRange(0, this.length - 1).iterator();
        while (intIterator.hasNext()) {
            int n2 = intIterator.nextInt();
            int n3 = this.presenceArray[n2];
            if (n3 < 0) continue;
            this.hashArray[n3] = 0;
            this.presenceArray[n2] = -1;
        }
        ListBuilderKt.resetRange(this.keysArray, 0, this.length);
        if (this.valuesArray != null) {
            ListBuilderKt.resetRange(this.valuesArray, 0, this.length);
        }
        this.size = 0;
        this.length = 0;
    }

    @NotNull
    public final Set<K> getKeys() {
        MapBuilderKeys<Object> mapBuilderKeys = this.keysView;
        if (mapBuilderKeys == null) {
            this.keysView = mapBuilderKeys = new MapBuilderKeys(this);
            return mapBuilderKeys;
        }
        return mapBuilderKeys;
    }

    @NotNull
    public final Collection<V> getValues() {
        MapBuilderValues<V> mapBuilderValues = this.valuesView;
        if (mapBuilderValues == null) {
            this.valuesView = mapBuilderValues = new MapBuilderValues(this);
            return mapBuilderValues;
        }
        return mapBuilderValues;
    }

    @NotNull
    public final Set<Map.Entry<K, V>> getEntries() {
        MapBuilderEntries<K, V> mapBuilderEntries = this.entriesView;
        if (mapBuilderEntries == null) {
            this.entriesView = mapBuilderEntries = new MapBuilderEntries(this);
            return mapBuilderEntries;
        }
        return mapBuilderEntries;
    }

    @Override
    public final boolean equals(@Nullable Object object) {
        return object == this || object instanceof Map && this.contentEquals((Map)object);
    }

    @Override
    public final int hashCode() {
        int n2 = 0;
        EntriesItr<K, V> entriesItr = this.entriesIterator$kotlin_stdlib();
        while (entriesItr.hasNext()) {
            n2 += entriesItr.nextHashCode$kotlin_stdlib();
        }
        return n2;
    }

    @NotNull
    public final String toString() {
        StringBuilder stringBuilder = new StringBuilder(2 + this.size() * 3);
        stringBuilder.append("{");
        int n2 = 0;
        EntriesItr<K, V> entriesItr = this.entriesIterator$kotlin_stdlib();
        while (entriesItr.hasNext()) {
            if (n2 > 0) {
                stringBuilder.append(", ");
            }
            entriesItr.nextAppendString(stringBuilder);
            ++n2;
        }
        stringBuilder.append("}");
        String string = stringBuilder.toString();
        Intrinsics.checkNotNullExpressionValue(string, "");
        return string;
    }

    public final int getCapacity$kotlin_stdlib() {
        return this.keysArray.length;
    }

    private final int getHashSize() {
        return this.hashArray.length;
    }

    public final void checkIsMutable$kotlin_stdlib() {
        if (this.isReadOnly) {
            throw new UnsupportedOperationException();
        }
    }

    private final void ensureExtraCapacity(int n2) {
        if (this.shouldCompact(n2)) {
            MapBuilder mapBuilder = this;
            mapBuilder.rehash(mapBuilder.getHashSize());
            return;
        }
        MapBuilder mapBuilder = this;
        mapBuilder.ensureCapacity(mapBuilder.length + n2);
    }

    private final boolean shouldCompact(int n2) {
        int n3 = this.getCapacity$kotlin_stdlib() - this.length;
        int n4 = this.length - this.size();
        return n3 < n2 && n4 + n3 >= n2 && n4 >= this.getCapacity$kotlin_stdlib() / 4;
    }

    private final void ensureCapacity(int n2) {
        if (n2 < 0) {
            throw new OutOfMemoryError();
        }
        if (n2 > this.getCapacity$kotlin_stdlib()) {
            V[] VArray;
            int n3 = this.getCapacity$kotlin_stdlib() * 3 / 2;
            if (n2 > n3) {
                n3 = n2;
            }
            this.keysArray = ListBuilderKt.copyOfUninitializedElements(this.keysArray, n3);
            MapBuilder mapBuilder = this;
            if (mapBuilder.valuesArray != null) {
                VArray = ListBuilderKt.copyOfUninitializedElements(mapBuilder.valuesArray, n3);
            } else {
                V[] cfr_ignored_0 = mapBuilder.valuesArray;
                VArray = null;
            }
            mapBuilder.valuesArray = VArray;
            int[] nArray = Arrays.copyOf(this.presenceArray, n3);
            Intrinsics.checkNotNullExpressionValue(nArray, "");
            this.presenceArray = nArray;
            n2 = MapBuilder.Companion.computeHashSize(n3);
            if (n2 > this.getHashSize()) {
                this.rehash(n2);
            }
        }
    }

    private final V[] allocateValuesArray() {
        Object[] objectArray = this.valuesArray;
        if (this.valuesArray != null) {
            return objectArray;
        }
        this.valuesArray = objectArray = ListBuilderKt.arrayOfUninitializedElements(this.getCapacity$kotlin_stdlib());
        return objectArray;
    }

    private final int hash(K k2) {
        K k3 = k2;
        return (k3 != null ? k3.hashCode() : 0) * -1640531527 >>> this.hashShift;
    }

    private final void compact() {
        int n2 = 0;
        V[] VArray = this.valuesArray;
        for (int i2 = 0; i2 < this.length; ++i2) {
            if (this.presenceArray[i2] < 0) continue;
            this.keysArray[n2] = this.keysArray[i2];
            if (VArray != null) {
                VArray[n2] = VArray[i2];
            }
            ++n2;
        }
        ListBuilderKt.resetRange(this.keysArray, n2, this.length);
        if (VArray != null) {
            ListBuilderKt.resetRange(VArray, n2, this.length);
        }
        this.length = n2;
    }

    private final void rehash(int n2) {
        if (this.length > this.size()) {
            this.compact();
        }
        if (n2 != this.getHashSize()) {
            this.hashArray = new int[n2];
            this.hashShift = MapBuilder.Companion.computeShift(n2);
        } else {
            ArraysKt.fill(this.hashArray, 0, 0, this.getHashSize());
        }
        n2 = 0;
        while (n2 < this.length) {
            if (this.putRehash(n2++)) continue;
            throw new IllegalStateException("This cannot happen with fixed magic multiplier and grow-only hash array. Have object hashCodes changed?");
        }
    }

    private final boolean putRehash(int n2) {
        MapBuilder mapBuilder = this;
        int n3 = mapBuilder.hash(mapBuilder.keysArray[n2]);
        int n4 = this.maxProbeDistance;
        while (true) {
            int n5;
            if ((n5 = this.hashArray[n3]) == 0) {
                this.hashArray[n3] = n2 + 1;
                this.presenceArray[n2] = n3;
                return true;
            }
            if (--n4 < 0) {
                return false;
            }
            if (n3-- != 0) continue;
            n3 = this.getHashSize() - 1;
        }
    }

    private final int findKey(K k2) {
        int n2 = this.hash(k2);
        int n3 = this.maxProbeDistance;
        int n4;
        while ((n4 = this.hashArray[n2]) != 0) {
            if (n4 > 0 && Intrinsics.areEqual(this.keysArray[n4 - 1], k2)) {
                return n4 - 1;
            }
            if (--n3 < 0) {
                return -1;
            }
            if (n2-- != 0) continue;
            n2 = this.getHashSize() - 1;
        }
        return -1;
    }

    private final int findValue(V v2) {
        int n2 = this.length;
        while (--n2 >= 0) {
            if (this.presenceArray[n2] < 0) continue;
            Intrinsics.checkNotNull(this.valuesArray);
            if (!Intrinsics.areEqual(this.valuesArray[n2], v2)) continue;
            return n2;
        }
        return -1;
    }

    public final int addKey$kotlin_stdlib(K k2) {
        this.checkIsMutable$kotlin_stdlib();
        block0: while (true) {
            int n2 = this.hash(k2);
            int n3 = RangesKt.coerceAtMost(this.maxProbeDistance << 1, this.getHashSize() / 2);
            int n4 = 0;
            while (true) {
                int n5;
                if ((n5 = this.hashArray[n2]) <= 0) {
                    if (this.length >= this.getCapacity$kotlin_stdlib()) {
                        this.ensureExtraCapacity(1);
                        continue block0;
                    }
                    n5 = this.length;
                    this.length = n5 + 1;
                    n3 = n5;
                    this.keysArray[n3] = k2;
                    this.presenceArray[n3] = n2;
                    this.hashArray[n2] = n3 + 1;
                    n5 = this.size();
                    this.size = n5 + 1;
                    if (n4 > this.maxProbeDistance) {
                        this.maxProbeDistance = n4;
                    }
                    return n3;
                }
                if (Intrinsics.areEqual(this.keysArray[n5 - 1], k2)) {
                    return -n5;
                }
                if (++n4 > n3) {
                    MapBuilder mapBuilder = this;
                    mapBuilder.rehash(mapBuilder.getHashSize() << 1);
                    continue block0;
                }
                if (n2-- != 0) continue;
                n2 = this.getHashSize() - 1;
            }
            break;
        }
    }

    public final int removeKey$kotlin_stdlib(K k2) {
        this.checkIsMutable$kotlin_stdlib();
        int n2 = this.findKey(k2);
        if (n2 < 0) {
            return -1;
        }
        this.removeKeyAt(n2);
        return n2;
    }

    private final void removeKeyAt(int n2) {
        ListBuilderKt.resetAt(this.keysArray, n2);
        MapBuilder mapBuilder = this;
        mapBuilder.removeHashAt(mapBuilder.presenceArray[n2]);
        this.presenceArray[n2] = -1;
        n2 = this.size();
        this.size = n2 + -1;
    }

    private final void removeHashAt(int n2) {
        int n3 = n2;
        int n4 = 0;
        int n5 = RangesKt.coerceAtMost(this.maxProbeDistance << 1, this.getHashSize() / 2);
        do {
            if (n3-- == 0) {
                n3 = this.getHashSize() - 1;
            }
            if (++n4 > this.maxProbeDistance) {
                this.hashArray[n2] = 0;
                return;
            }
            int n6 = this.hashArray[n3];
            if (n6 == 0) {
                this.hashArray[n2] = 0;
                return;
            }
            if (n6 < 0) {
                this.hashArray[n2] = -1;
                n2 = n3;
                n4 = 0;
                continue;
            }
            MapBuilder mapBuilder = this;
            int n7 = mapBuilder.hash(mapBuilder.keysArray[n6 - 1]);
            if ((n7 - n3 & this.getHashSize() - 1) < n4) continue;
            this.hashArray[n2] = n6;
            this.presenceArray[n6 - 1] = n2;
            n2 = n3;
            n4 = 0;
        } while (--n5 >= 0);
        this.hashArray[n2] = -1;
    }

    public final boolean containsEntry$kotlin_stdlib(@NotNull Map.Entry<? extends K, ? extends V> entry) {
        Intrinsics.checkNotNullParameter(entry, "");
        int n2 = this.findKey(entry.getKey());
        if (n2 < 0) {
            return false;
        }
        Intrinsics.checkNotNull(this.valuesArray);
        return Intrinsics.areEqual(this.valuesArray[n2], entry.getValue());
    }

    private final boolean contentEquals(Map<?, ?> map) {
        return this.size() == map.size() && this.containsAllEntries$kotlin_stdlib((Collection)map.entrySet());
    }

    public final boolean containsAllEntries$kotlin_stdlib(@NotNull Collection<?> object) {
        Intrinsics.checkNotNullParameter(object, "");
        object = object.iterator();
        while (object.hasNext()) {
            Object e2 = object.next();
            try {
                if (e2 != null && this.containsEntry$kotlin_stdlib((Map.Entry)e2)) continue;
                return false;
            }
            catch (ClassCastException classCastException) {
                return false;
            }
        }
        return true;
    }

    private final boolean putEntry(Map.Entry<? extends K, ? extends V> entry) {
        int n2 = this.addKey$kotlin_stdlib(entry.getKey());
        V[] VArray = this.allocateValuesArray();
        if (n2 >= 0) {
            VArray[n2] = entry.getValue();
            return true;
        }
        V v2 = VArray[-n2 - 1];
        if (!Intrinsics.areEqual(entry.getValue(), v2)) {
            VArray[-n2 - 1] = entry.getValue();
            return true;
        }
        return false;
    }

    private final boolean putAllEntries(Collection<? extends Map.Entry<? extends K, ? extends V>> object) {
        if (object.isEmpty()) {
            return false;
        }
        this.ensureExtraCapacity(object.size());
        object = object.iterator();
        boolean bl = false;
        while (object.hasNext()) {
            if (!this.putEntry((Map.Entry)object.next())) continue;
            bl = true;
        }
        return bl;
    }

    public final boolean removeEntry$kotlin_stdlib(@NotNull Map.Entry<? extends K, ? extends V> entry) {
        Intrinsics.checkNotNullParameter(entry, "");
        this.checkIsMutable$kotlin_stdlib();
        int n2 = this.findKey(entry.getKey());
        if (n2 < 0) {
            return false;
        }
        Intrinsics.checkNotNull(this.valuesArray);
        if (!Intrinsics.areEqual(this.valuesArray[n2], entry.getValue())) {
            return false;
        }
        this.removeKeyAt(n2);
        return true;
    }

    public final boolean removeValue$kotlin_stdlib(V v2) {
        this.checkIsMutable$kotlin_stdlib();
        int n2 = this.findValue(v2);
        if (n2 < 0) {
            return false;
        }
        this.removeKeyAt(n2);
        return true;
    }

    @NotNull
    public final KeysItr<K, V> keysIterator$kotlin_stdlib() {
        return new KeysItr(this);
    }

    @NotNull
    public final ValuesItr<K, V> valuesIterator$kotlin_stdlib() {
        return new ValuesItr(this);
    }

    @NotNull
    public final EntriesItr<K, V> entriesIterator$kotlin_stdlib() {
        return new EntriesItr(this);
    }

    @Override
    public final int size() {
        return this.getSize();
    }

    @Override
    public final Set<K> keySet() {
        return this.getKeys();
    }

    @Override
    public final Collection<V> values() {
        return this.getValues();
    }

    @Override
    public final Set<Map.Entry<K, V>> entrySet() {
        return this.getEntries();
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\b\b\u0082\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\b\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\u0004H\u0002J\u0010\u0010\n\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u0004H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2={"Lkotlin/collections/builders/MapBuilder$Companion;", "", "()V", "INITIAL_CAPACITY", "", "INITIAL_MAX_PROBE_DISTANCE", "MAGIC", "TOMBSTONE", "computeHashSize", "capacity", "computeShift", "hashSize", "kotlin-stdlib"})
    static final class Companion {
        private Companion() {
        }

        private final int computeHashSize(int n2) {
            return Integer.highestOneBit(RangesKt.coerceAtLeast(n2, 1) * 3);
        }

        private final int computeShift(int n2) {
            return Integer.numberOfLeadingZeros(n2) + 1;
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000,\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\n\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0010\u0018\u0000*\u0004\b\u0002\u0010\u0001*\u0004\b\u0003\u0010\u00022\u00020\u0003B\u0019\u0012\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00028\u0002\u0012\u0004\u0012\u00028\u00030\u0005\u00a2\u0006\u0002\u0010\u0006J\u0006\u0010\u0012\u001a\u00020\u0013J\r\u0010\u0014\u001a\u00020\u0015H\u0000\u00a2\u0006\u0002\b\u0016J\u0006\u0010\u0017\u001a\u00020\u0015R\u001a\u0010\u0007\u001a\u00020\bX\u0080\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u001a\u0010\r\u001a\u00020\bX\u0080\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\n\"\u0004\b\u000f\u0010\fR \u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00028\u0002\u0012\u0004\u0012\u00028\u00030\u0005X\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011\u00a8\u0006\u0018"}, d2={"Lkotlin/collections/builders/MapBuilder$Itr;", "K", "V", "", "map", "Lkotlin/collections/builders/MapBuilder;", "(Lkotlin/collections/builders/MapBuilder;)V", "index", "", "getIndex$kotlin_stdlib", "()I", "setIndex$kotlin_stdlib", "(I)V", "lastIndex", "getLastIndex$kotlin_stdlib", "setLastIndex$kotlin_stdlib", "getMap$kotlin_stdlib", "()Lkotlin/collections/builders/MapBuilder;", "hasNext", "", "initNext", "", "initNext$kotlin_stdlib", "remove", "kotlin-stdlib"})
    @SourceDebugExtension(value={"SMAP\nMapBuilder.kt\nKotlin\n*S Kotlin\n*F\n+ 1 MapBuilder.kt\nkotlin/collections/builders/MapBuilder$Itr\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,694:1\n1#2:695\n*E\n"})
    public static class Itr<K, V> {
        @NotNull
        private final MapBuilder<K, V> map;
        private int index;
        private int lastIndex;

        public Itr(@NotNull MapBuilder<K, V> mapBuilder) {
            Intrinsics.checkNotNullParameter(mapBuilder, "");
            this.map = mapBuilder;
            this.lastIndex = -1;
            this.initNext$kotlin_stdlib();
        }

        @NotNull
        public final MapBuilder<K, V> getMap$kotlin_stdlib() {
            return this.map;
        }

        public final int getIndex$kotlin_stdlib() {
            return this.index;
        }

        public final void setIndex$kotlin_stdlib(int n2) {
            this.index = n2;
        }

        public final int getLastIndex$kotlin_stdlib() {
            return this.lastIndex;
        }

        public final void setLastIndex$kotlin_stdlib(int n2) {
            this.lastIndex = n2;
        }

        public final void initNext$kotlin_stdlib() {
            while (this.index < ((MapBuilder)this.map).length && ((MapBuilder)this.map).presenceArray[this.index] < 0) {
                int n2 = this.index;
                this.index = n2 + 1;
            }
        }

        public final boolean hasNext() {
            return this.index < ((MapBuilder)this.map).length;
        }

        public final void remove() {
            boolean bl = this.lastIndex != -1;
            if (!bl) {
                String string = "Call next() before removing element from the iterator.";
                throw new IllegalStateException(string.toString());
            }
            this.map.checkIsMutable$kotlin_stdlib();
            ((MapBuilder)this.map).removeKeyAt(this.lastIndex);
            this.lastIndex = -1;
        }
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010)\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0000\u0018\u0000*\u0004\b\u0002\u0010\u0001*\u0004\b\u0003\u0010\u00022\u000e\u0012\u0004\u0012\u0002H\u0001\u0012\u0004\u0012\u0002H\u00020\u00032\b\u0012\u0004\u0012\u0002H\u00010\u0004B\u0019\u0012\u0012\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00028\u0002\u0012\u0004\u0012\u00028\u00030\u0006\u00a2\u0006\u0002\u0010\u0007J\u000e\u0010\b\u001a\u00028\u0002H\u0096\u0002\u00a2\u0006\u0002\u0010\t\u00a8\u0006\n"}, d2={"Lkotlin/collections/builders/MapBuilder$KeysItr;", "K", "V", "Lkotlin/collections/builders/MapBuilder$Itr;", "", "map", "Lkotlin/collections/builders/MapBuilder;", "(Lkotlin/collections/builders/MapBuilder;)V", "next", "()Ljava/lang/Object;", "kotlin-stdlib"})
    public static final class KeysItr<K, V>
    extends Itr<K, V>
    implements Iterator<K>,
    KMutableIterator {
        public KeysItr(@NotNull MapBuilder<K, V> mapBuilder) {
            Intrinsics.checkNotNullParameter(mapBuilder, "");
            super(mapBuilder);
        }

        @Override
        public final K next() {
            if (this.getIndex$kotlin_stdlib() >= this.getMap$kotlin_stdlib().length) {
                throw new NoSuchElementException();
            }
            KeysItr keysItr = this;
            int n2 = keysItr.getIndex$kotlin_stdlib();
            this.setIndex$kotlin_stdlib(n2 + 1);
            keysItr.setLastIndex$kotlin_stdlib(n2);
            Object object = this.getMap$kotlin_stdlib().keysArray[this.getLastIndex$kotlin_stdlib()];
            this.initNext$kotlin_stdlib();
            return (K)object;
        }
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010)\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0000\u0018\u0000*\u0004\b\u0002\u0010\u0001*\u0004\b\u0003\u0010\u00022\u000e\u0012\u0004\u0012\u0002H\u0001\u0012\u0004\u0012\u0002H\u00020\u00032\b\u0012\u0004\u0012\u0002H\u00020\u0004B\u0019\u0012\u0012\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00028\u0002\u0012\u0004\u0012\u00028\u00030\u0006\u00a2\u0006\u0002\u0010\u0007J\u000e\u0010\b\u001a\u00028\u0003H\u0096\u0002\u00a2\u0006\u0002\u0010\t\u00a8\u0006\n"}, d2={"Lkotlin/collections/builders/MapBuilder$ValuesItr;", "K", "V", "Lkotlin/collections/builders/MapBuilder$Itr;", "", "map", "Lkotlin/collections/builders/MapBuilder;", "(Lkotlin/collections/builders/MapBuilder;)V", "next", "()Ljava/lang/Object;", "kotlin-stdlib"})
    public static final class ValuesItr<K, V>
    extends Itr<K, V>
    implements Iterator<V>,
    KMutableIterator {
        public ValuesItr(@NotNull MapBuilder<K, V> mapBuilder) {
            Intrinsics.checkNotNullParameter(mapBuilder, "");
            super(mapBuilder);
        }

        @Override
        public final V next() {
            if (this.getIndex$kotlin_stdlib() >= this.getMap$kotlin_stdlib().length) {
                throw new NoSuchElementException();
            }
            ValuesItr valuesItr = this;
            int n2 = valuesItr.getIndex$kotlin_stdlib();
            this.setIndex$kotlin_stdlib(n2 + 1);
            valuesItr.setLastIndex$kotlin_stdlib(n2);
            Object[] objectArray = this.getMap$kotlin_stdlib().valuesArray;
            Intrinsics.checkNotNull(objectArray);
            Object object = objectArray[this.getLastIndex$kotlin_stdlib()];
            this.initNext$kotlin_stdlib();
            return (V)object;
        }
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000<\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010)\n\u0002\u0010'\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0000\u0018\u0000*\u0004\b\u0002\u0010\u0001*\u0004\b\u0003\u0010\u00022\u000e\u0012\u0004\u0012\u0002H\u0001\u0012\u0004\u0012\u0002H\u00020\u00032\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0001\u0012\u0004\u0012\u0002H\u00020\u00050\u0004B\u0019\u0012\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00028\u0002\u0012\u0004\u0012\u00028\u00030\u0007\u00a2\u0006\u0002\u0010\bJ\u0015\u0010\t\u001a\u000e\u0012\u0004\u0012\u00028\u0002\u0012\u0004\u0012\u00028\u00030\nH\u0096\u0002J\u0012\u0010\u000b\u001a\u00020\f2\n\u0010\r\u001a\u00060\u000ej\u0002`\u000fJ\r\u0010\u0010\u001a\u00020\u0011H\u0000\u00a2\u0006\u0002\b\u0012\u00a8\u0006\u0013"}, d2={"Lkotlin/collections/builders/MapBuilder$EntriesItr;", "K", "V", "Lkotlin/collections/builders/MapBuilder$Itr;", "", "", "map", "Lkotlin/collections/builders/MapBuilder;", "(Lkotlin/collections/builders/MapBuilder;)V", "next", "Lkotlin/collections/builders/MapBuilder$EntryRef;", "nextAppendString", "", "sb", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "nextHashCode", "", "nextHashCode$kotlin_stdlib", "kotlin-stdlib"})
    public static final class EntriesItr<K, V>
    extends Itr<K, V>
    implements Iterator<Map.Entry<K, V>>,
    KMutableIterator {
        public EntriesItr(@NotNull MapBuilder<K, V> mapBuilder) {
            Intrinsics.checkNotNullParameter(mapBuilder, "");
            super(mapBuilder);
        }

        @Override
        @NotNull
        public final EntryRef<K, V> next() {
            if (this.getIndex$kotlin_stdlib() >= this.getMap$kotlin_stdlib().length) {
                throw new NoSuchElementException();
            }
            EntriesItr entriesItr = this;
            int n2 = entriesItr.getIndex$kotlin_stdlib();
            this.setIndex$kotlin_stdlib(n2 + 1);
            entriesItr.setLastIndex$kotlin_stdlib(n2);
            EntryRef entryRef = new EntryRef(this.getMap$kotlin_stdlib(), this.getLastIndex$kotlin_stdlib());
            this.initNext$kotlin_stdlib();
            return entryRef;
        }

        public final int nextHashCode$kotlin_stdlib() {
            if (this.getIndex$kotlin_stdlib() >= this.getMap$kotlin_stdlib().length) {
                throw new NoSuchElementException();
            }
            EntriesItr entriesItr = this;
            int n2 = entriesItr.getIndex$kotlin_stdlib();
            this.setIndex$kotlin_stdlib(n2 + 1);
            entriesItr.setLastIndex$kotlin_stdlib(n2);
            Object object = this.getMap$kotlin_stdlib().keysArray[this.getLastIndex$kotlin_stdlib()];
            int n3 = object != null ? object.hashCode() : 0;
            Object[] objectArray = this.getMap$kotlin_stdlib().valuesArray;
            Intrinsics.checkNotNull(objectArray);
            Object object2 = objectArray[this.getLastIndex$kotlin_stdlib()];
            n2 = n3 ^ (object2 != null ? object2.hashCode() : 0);
            this.initNext$kotlin_stdlib();
            return n2;
        }

        public final void nextAppendString(@NotNull StringBuilder stringBuilder) {
            Intrinsics.checkNotNullParameter(stringBuilder, "");
            if (this.getIndex$kotlin_stdlib() >= this.getMap$kotlin_stdlib().length) {
                throw new NoSuchElementException();
            }
            EntriesItr entriesItr = this;
            int n2 = entriesItr.getIndex$kotlin_stdlib();
            this.setIndex$kotlin_stdlib(n2 + 1);
            entriesItr.setLastIndex$kotlin_stdlib(n2);
            Object object = this.getMap$kotlin_stdlib().keysArray[this.getLastIndex$kotlin_stdlib()];
            if (Intrinsics.areEqual(object, this.getMap$kotlin_stdlib())) {
                stringBuilder.append("(this Map)");
            } else {
                stringBuilder.append(object);
            }
            stringBuilder.append('=');
            Object[] objectArray = this.getMap$kotlin_stdlib().valuesArray;
            Intrinsics.checkNotNull(objectArray);
            object = objectArray[this.getLastIndex$kotlin_stdlib()];
            if (Intrinsics.areEqual(object, this.getMap$kotlin_stdlib())) {
                stringBuilder.append("(this Map)");
            } else {
                stringBuilder.append(object);
            }
            this.initNext$kotlin_stdlib();
        }
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u00000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010'\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0000\b\u0000\u0018\u0000*\u0004\b\u0002\u0010\u0001*\u0004\b\u0003\u0010\u00022\u000e\u0012\u0004\u0012\u0002H\u0001\u0012\u0004\u0012\u0002H\u00020\u0003B!\u0012\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00028\u0002\u0012\u0004\u0012\u00028\u00030\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u0096\u0002J\b\u0010\u0012\u001a\u00020\u0007H\u0016J\u0015\u0010\u0013\u001a\u00028\u00032\u0006\u0010\u0014\u001a\u00028\u0003H\u0016\u00a2\u0006\u0002\u0010\u0015J\b\u0010\u0016\u001a\u00020\u0017H\u0016R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\u00028\u00028VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u001a\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00028\u0002\u0012\u0004\u0012\u00028\u00030\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\u00028\u00038VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\r\u0010\u000b\u00a8\u0006\u0018"}, d2={"Lkotlin/collections/builders/MapBuilder$EntryRef;", "K", "V", "", "map", "Lkotlin/collections/builders/MapBuilder;", "index", "", "(Lkotlin/collections/builders/MapBuilder;I)V", "key", "getKey", "()Ljava/lang/Object;", "value", "getValue", "equals", "", "other", "", "hashCode", "setValue", "newValue", "(Ljava/lang/Object;)Ljava/lang/Object;", "toString", "", "kotlin-stdlib"})
    public static final class EntryRef<K, V>
    implements Map.Entry<K, V>,
    KMutableMap.Entry {
        @NotNull
        private final MapBuilder<K, V> map;
        private final int index;

        public EntryRef(@NotNull MapBuilder<K, V> mapBuilder, int n2) {
            Intrinsics.checkNotNullParameter(mapBuilder, "");
            this.map = mapBuilder;
            this.index = n2;
        }

        @Override
        public final K getKey() {
            return (K)((MapBuilder)this.map).keysArray[this.index];
        }

        @Override
        public final V getValue() {
            Object[] objectArray = ((MapBuilder)this.map).valuesArray;
            Intrinsics.checkNotNull(objectArray);
            return (V)objectArray[this.index];
        }

        @Override
        public final V setValue(V v2) {
            this.map.checkIsMutable$kotlin_stdlib();
            Object[] objectArray = ((MapBuilder)this.map).allocateValuesArray();
            Object object = objectArray[this.index];
            objectArray[this.index] = v2;
            return (V)object;
        }

        @Override
        public final boolean equals(@Nullable Object object) {
            return object instanceof Map.Entry && Intrinsics.areEqual(((Map.Entry)object).getKey(), this.getKey()) && Intrinsics.areEqual(((Map.Entry)object).getValue(), this.getValue());
        }

        @Override
        public final int hashCode() {
            K k2 = this.getKey();
            V v2 = this.getValue();
            return (k2 != null ? k2.hashCode() : 0) ^ (v2 != null ? v2.hashCode() : 0);
        }

        @NotNull
        public final String toString() {
            return "" + this.getKey() + '=' + this.getValue();
        }
    }
}


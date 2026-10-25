/*
 * Decompiled with CFR 0.152.
 */
package kotlin.collections.builders;

import java.io.Externalizable;
import java.io.InvalidObjectException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u001e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0000\u0018\u0000 \u00112\u00020\u0001:\u0001\u0011B\u0007\b\u0016\u00a2\u0006\u0002\u0010\u0002B\u0019\u0012\n\u0010\u0003\u001a\u0006\u0012\u0002\b\u00030\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0016J\b\u0010\f\u001a\u00020\rH\u0002J\u0010\u0010\u000e\u001a\u00020\t2\u0006\u0010\u000f\u001a\u00020\u0010H\u0016R\u0012\u0010\u0003\u001a\u0006\u0012\u0002\b\u00030\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2={"Lkotlin/collections/builders/SerializedCollection;", "Ljava/io/Externalizable;", "()V", "collection", "", "tag", "", "(Ljava/util/Collection;I)V", "readExternal", "", "input", "Ljava/io/ObjectInput;", "readResolve", "", "writeExternal", "output", "Ljava/io/ObjectOutput;", "Companion", "kotlin-stdlib"})
@SourceDebugExtension(value={"SMAP\nListBuilder.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ListBuilder.kt\nkotlin/collections/builders/SerializedCollection\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,429:1\n1#2:430\n*E\n"})
public final class SerializedCollection
implements Externalizable {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private Collection<?> collection;
    private final int tag;
    private static final long serialVersionUID = 0L;
    public static final int tagList = 0;
    public static final int tagSet = 1;

    public SerializedCollection(@NotNull Collection<?> collection, int n2) {
        Intrinsics.checkNotNullParameter(collection, "");
        this.collection = collection;
        this.tag = n2;
    }

    public SerializedCollection() {
        this(CollectionsKt.emptyList(), 0);
    }

    @Override
    public final void writeExternal(@NotNull ObjectOutput objectOutput) {
        Intrinsics.checkNotNullParameter(objectOutput, "");
        objectOutput.writeByte(this.tag);
        objectOutput.writeInt(this.collection.size());
        for (Object obj : this.collection) {
            objectOutput.writeObject(obj);
        }
    }

    @Override
    public final void readExternal(@NotNull ObjectInput objectInput) {
        Collection collection;
        Intrinsics.checkNotNullParameter(objectInput, "");
        int n2 = objectInput.readByte();
        int n3 = n2 & 1;
        int n4 = n2 & 0xFFFFFFFE;
        if (n4 != 0) {
            throw new InvalidObjectException("Unsupported flags value: " + n2 + '.');
        }
        n2 = objectInput.readInt();
        if (n2 < 0) {
            throw new InvalidObjectException("Illegal size value: " + n2 + '.');
        }
        SerializedCollection serializedCollection = this;
        switch (n3) {
            case 0: {
                List<Object> list2;
                List<Object> list3 = list2 = CollectionsKt.createListBuilder(n2);
                SerializedCollection serializedCollection2 = serializedCollection;
                for (int i2 = 0; i2 < n2; ++i2) {
                    list3.add(objectInput.readObject());
                }
                SerializedCollection serializedCollection3 = serializedCollection2;
                collection = CollectionsKt.build(list2);
                break;
            }
            case 1: {
                Set<Object> set;
                Set<Object> set2 = set = SetsKt.createSetBuilder(n2);
                SerializedCollection serializedCollection4 = serializedCollection;
                for (int i3 = 0; i3 < n2; ++i3) {
                    set2.add(objectInput.readObject());
                }
                SerializedCollection serializedCollection3 = serializedCollection4;
                collection = SetsKt.build(set);
                break;
            }
            default: {
                throw new InvalidObjectException("Unsupported collection type tag: " + n3 + '.');
            }
        }
        serializedCollection3.collection = collection;
    }

    private final Object readResolve() {
        return this.collection;
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2={"Lkotlin/collections/builders/SerializedCollection$Companion;", "", "()V", "serialVersionUID", "", "tagList", "", "tagSet", "kotlin-stdlib"})
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }
}


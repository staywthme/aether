/*
 * Decompiled with CFR 0.152.
 */
package kotlin.sequences;

import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.sequences.DropTakeSequence;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010(\n\u0002\b\u0002\b\u0000\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003B#\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\bJ\u0016\u0010\f\u001a\b\u0012\u0004\u0012\u00028\u00000\u00022\u0006\u0010\r\u001a\u00020\u0006H\u0016J\u000f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00028\u00000\u000fH\u0096\u0002J\u0016\u0010\u0010\u001a\b\u0012\u0004\u0012\u00028\u00000\u00022\u0006\u0010\r\u001a\u00020\u0006H\u0016R\u0014\u0010\t\u001a\u00020\u00068BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2={"Lkotlin/sequences/SubSequence;", "T", "Lkotlin/sequences/Sequence;", "Lkotlin/sequences/DropTakeSequence;", "sequence", "startIndex", "", "endIndex", "(Lkotlin/sequences/Sequence;II)V", "count", "getCount", "()I", "drop", "n", "iterator", "", "take", "kotlin-stdlib"})
@SourceDebugExtension(value={"SMAP\nSequences.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Sequences.kt\nkotlin/sequences/SubSequence\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,680:1\n1#2:681\n*E\n"})
public final class SubSequence<T>
implements DropTakeSequence<T>,
Sequence<T> {
    @NotNull
    private final Sequence<T> sequence;
    private final int startIndex;
    private final int endIndex;

    public SubSequence(@NotNull Sequence<? extends T> sequence2, int n2, int n3) {
        Intrinsics.checkNotNullParameter(sequence2, "");
        this.sequence = sequence2;
        this.startIndex = n2;
        this.endIndex = n3;
        boolean bl = this.startIndex >= 0;
        if (!bl) {
            String string = "startIndex should be non-negative, but is " + this.startIndex;
            throw new IllegalArgumentException(string.toString());
        }
        bl = this.endIndex >= 0;
        if (!bl) {
            String string = "endIndex should be non-negative, but is " + this.endIndex;
            throw new IllegalArgumentException(string.toString());
        }
        bl = this.endIndex >= this.startIndex;
        if (!bl) {
            String string = "endIndex should be not less than startIndex, but was " + this.endIndex + " < " + this.startIndex;
            throw new IllegalArgumentException(string.toString());
        }
    }

    private final int getCount() {
        return this.endIndex - this.startIndex;
    }

    @Override
    @NotNull
    public final Sequence<T> drop(int n2) {
        if (n2 >= this.getCount()) {
            return SequencesKt.emptySequence();
        }
        return new SubSequence<T>(this.sequence, this.startIndex + n2, this.endIndex);
    }

    @Override
    @NotNull
    public final Sequence<T> take(int n2) {
        if (n2 >= this.getCount()) {
            return this;
        }
        return new SubSequence<T>(this.sequence, this.startIndex, this.startIndex + n2);
    }

    @Override
    @NotNull
    public final Iterator<T> iterator() {
        return new Iterator<T>(this){
            @NotNull
            private final Iterator<T> iterator;
            private int position;
            final /* synthetic */ SubSequence<T> this$0;
            {
                this.this$0 = subSequence;
                this.iterator = SubSequence.access$getSequence$p(subSequence).iterator();
            }

            @NotNull
            public final Iterator<T> getIterator() {
                return this.iterator;
            }

            public final int getPosition() {
                return this.position;
            }

            public final void setPosition(int n2) {
                this.position = n2;
            }

            private final void drop() {
                while (this.position < SubSequence.access$getStartIndex$p(this.this$0) && this.iterator.hasNext()) {
                    this.iterator.next();
                    int n2 = this.position;
                    this.position = n2 + 1;
                }
            }

            public final boolean hasNext() {
                this.drop();
                return this.position < SubSequence.access$getEndIndex$p(this.this$0) && this.iterator.hasNext();
            }

            public final T next() {
                this.drop();
                if (this.position >= SubSequence.access$getEndIndex$p(this.this$0)) {
                    throw new NoSuchElementException();
                }
                int n2 = this.position;
                this.position = n2 + 1;
                return this.iterator.next();
            }

            public final void remove() {
                throw new UnsupportedOperationException("Operation is not supported for read-only collection");
            }
        };
    }

    public static final /* synthetic */ Sequence access$getSequence$p(SubSequence subSequence) {
        return subSequence.sequence;
    }

    public static final /* synthetic */ int access$getStartIndex$p(SubSequence subSequence) {
        return subSequence.startIndex;
    }

    public static final /* synthetic */ int access$getEndIndex$p(SubSequence subSequence) {
        return subSequence.endIndex;
    }
}


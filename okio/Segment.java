/*
 * Decompiled with CFR 0.152.
 */
package okio;

import java.util.Arrays;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.JvmField;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import okio.SegmentPool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u000b\b\u0000\u0018\u0000 \u00192\u00020\u0001:\u0001\u0019B\u0007\b\u0016\u00a2\u0006\u0002\u0010\u0002B/\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0006\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\t\u00a2\u0006\u0002\u0010\u000bJ\u0006\u0010\u000e\u001a\u00020\u000fJ\b\u0010\u0010\u001a\u0004\u0018\u00010\u0000J\u000e\u0010\u0011\u001a\u00020\u00002\u0006\u0010\u0012\u001a\u00020\u0000J\u0006\u0010\u0013\u001a\u00020\u0000J\u000e\u0010\u0014\u001a\u00020\u00002\u0006\u0010\u0015\u001a\u00020\u0006J\u0006\u0010\u0016\u001a\u00020\u0000J\u0016\u0010\u0017\u001a\u00020\u000f2\u0006\u0010\u0018\u001a\u00020\u00002\u0006\u0010\u0015\u001a\u00020\u0006R\u0010\u0010\u0003\u001a\u00020\u00048\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000R\u0012\u0010\u0007\u001a\u00020\u00068\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\u0004\u0018\u00010\u00008\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000R\u0012\u0010\n\u001a\u00020\t8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000R\u0012\u0010\u0005\u001a\u00020\u00068\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\u0004\u0018\u00010\u00008\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000R\u0012\u0010\b\u001a\u00020\t8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001a"}, d2={"Lokio/Segment;", "", "()V", "data", "", "pos", "", "limit", "shared", "", "owner", "([BIIZZ)V", "next", "prev", "compact", "", "pop", "push", "segment", "sharedCopy", "split", "byteCount", "unsharedCopy", "writeTo", "sink", "Companion", "okio"})
@SourceDebugExtension(value={"SMAP\nSegment.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Segment.kt\nokio/Segment\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,187:1\n1#2:188\n*E\n"})
public final class Segment {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @JvmField
    @NotNull
    public final byte[] data;
    @JvmField
    public int pos;
    @JvmField
    public int limit;
    @JvmField
    public boolean shared;
    @JvmField
    public boolean owner;
    @JvmField
    @Nullable
    public Segment next;
    @JvmField
    @Nullable
    public Segment prev;
    public static final int SIZE = 8192;
    public static final int SHARE_MINIMUM = 1024;

    public Segment() {
        this.data = new byte[8192];
        this.owner = true;
        this.shared = false;
    }

    public Segment(@NotNull byte[] byArray, int n2, int n3, boolean bl, boolean bl2) {
        Intrinsics.checkNotNullParameter(byArray, "");
        this.data = byArray;
        this.pos = n2;
        this.limit = n3;
        this.shared = bl;
        this.owner = bl2;
    }

    @NotNull
    public final Segment sharedCopy() {
        this.shared = true;
        return new Segment(this.data, this.pos, this.limit, true, false);
    }

    @NotNull
    public final Segment unsharedCopy() {
        byte[] byArray = Arrays.copyOf(this.data, this.data.length);
        Intrinsics.checkNotNullExpressionValue(byArray, "");
        return new Segment(byArray, this.pos, this.limit, false, true);
    }

    @Nullable
    public final Segment pop() {
        Segment segment = this.next != this ? this.next : null;
        Intrinsics.checkNotNull(this.prev);
        this.prev.next = this.next;
        Intrinsics.checkNotNull(this.next);
        this.next.prev = this.prev;
        this.next = null;
        this.prev = null;
        return segment;
    }

    @NotNull
    public final Segment push(@NotNull Segment segment) {
        Intrinsics.checkNotNullParameter(segment, "");
        segment.prev = this;
        segment.next = this.next;
        Intrinsics.checkNotNull(this.next);
        this.next.prev = segment;
        this.next = segment;
        return segment;
    }

    @NotNull
    public final Segment split(int n2) {
        Segment segment;
        if (!(n2 > 0 && n2 <= this.limit - this.pos)) {
            String string = "byteCount out of range";
            throw new IllegalArgumentException(string.toString());
        }
        if (n2 >= 1024) {
            segment = this.sharedCopy();
        } else {
            segment = SegmentPool.take();
            ArraysKt.copyInto$default(this.data, segment.data, 0, this.pos, this.pos + n2, 2, null);
        }
        segment.limit = segment.pos + n2;
        this.pos += n2;
        Segment segment2 = this.prev;
        Intrinsics.checkNotNull(segment2);
        segment2.push(segment);
        return segment;
    }

    public final void compact() {
        int n2;
        if (!(this.prev != this)) {
            String string = "cannot compact";
            throw new IllegalStateException(string.toString());
        }
        Segment segment = this.prev;
        Intrinsics.checkNotNull(segment);
        if (!segment.owner) {
            return;
        }
        int n3 = this.limit - this.pos;
        Segment segment2 = this.prev;
        Intrinsics.checkNotNull(segment2);
        int n4 = 8192 - segment2.limit;
        Segment segment3 = this.prev;
        Intrinsics.checkNotNull(segment3);
        if (segment3.shared) {
            n2 = 0;
        } else {
            Segment segment4 = this.prev;
            Intrinsics.checkNotNull(segment4);
            n2 = segment4.pos;
        }
        int n5 = n4 + n2;
        if (n3 > n5) {
            return;
        }
        Segment segment5 = this;
        Segment segment6 = segment5.prev;
        Intrinsics.checkNotNull(segment6);
        segment5.writeTo(segment6, n3);
        this.pop();
        SegmentPool.recycle(this);
    }

    public final void writeTo(@NotNull Segment object, int n2) {
        Intrinsics.checkNotNullParameter(object, "");
        if (!((Segment)object).owner) {
            object = "only owner can write";
            throw new IllegalStateException(object.toString());
        }
        if (((Segment)object).limit + n2 > 8192) {
            if (((Segment)object).shared) {
                throw new IllegalArgumentException();
            }
            if (((Segment)object).limit + n2 - ((Segment)object).pos > 8192) {
                throw new IllegalArgumentException();
            }
            ArraysKt.copyInto$default(((Segment)object).data, ((Segment)object).data, 0, ((Segment)object).pos, ((Segment)object).limit, 2, null);
            ((Segment)object).limit -= ((Segment)object).pos;
            ((Segment)object).pos = 0;
        }
        ArraysKt.copyInto(this.data, ((Segment)object).data, ((Segment)object).limit, this.pos, this.pos + n2);
        ((Segment)object).limit += n2;
        this.pos += n2;
    }

    @Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2={"Lokio/Segment$Companion;", "", "()V", "SHARE_MINIMUM", "", "SIZE", "okio"})
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }
}


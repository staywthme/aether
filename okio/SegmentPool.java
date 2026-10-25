/*
 * Decompiled with CFR 0.152.
 */
package okio;

import java.util.concurrent.atomic.AtomicReference;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import okio.Segment;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u00c0\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0010\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00060\u000eH\u0002J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0006H\u0007J\b\u0010\u0014\u001a\u00020\u0006H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\n\u001a\u00020\u00048F\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\tR\u001e\u0010\f\u001a\u0010\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00060\u000e0\rX\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u000f\u00a8\u0006\u0015"}, d2={"Lokio/SegmentPool;", "", "()V", "HASH_BUCKET_COUNT", "", "LOCK", "Lokio/Segment;", "MAX_SIZE", "getMAX_SIZE", "()I", "byteCount", "getByteCount", "hashBuckets", "", "Ljava/util/concurrent/atomic/AtomicReference;", "[Ljava/util/concurrent/atomic/AtomicReference;", "firstRef", "recycle", "", "segment", "take", "okio"})
public final class SegmentPool {
    @NotNull
    public static final SegmentPool INSTANCE = new SegmentPool();
    private static final int MAX_SIZE = 65536;
    @NotNull
    private static final Segment LOCK = new Segment(new byte[0], 0, 0, false, false);
    private static final int HASH_BUCKET_COUNT = Integer.highestOneBit((Runtime.getRuntime().availableProcessors() << 1) - 1);
    @NotNull
    private static final AtomicReference<Segment>[] hashBuckets;

    private SegmentPool() {
    }

    public final int getMAX_SIZE() {
        return MAX_SIZE;
    }

    public final int getByteCount() {
        Segment segment = this.firstRef().get();
        if (segment == null) {
            return 0;
        }
        Segment segment2 = segment;
        return segment.limit;
    }

    @JvmStatic
    @NotNull
    public static final Segment take() {
        AtomicReference<Segment> atomicReference = INSTANCE.firstRef();
        Segment segment = atomicReference.getAndSet(LOCK);
        if (segment == LOCK) {
            return new Segment();
        }
        if (segment == null) {
            atomicReference.set(null);
            return new Segment();
        }
        atomicReference.set(segment.next);
        segment.next = null;
        segment.limit = 0;
        return segment;
    }

    @JvmStatic
    public static final void recycle(@NotNull Segment segment) {
        Intrinsics.checkNotNullParameter(segment, "");
        if (!(segment.next == null && segment.prev == null)) {
            String string = "Failed requirement.";
            throw new IllegalArgumentException(string.toString());
        }
        if (segment.shared) {
            return;
        }
        AtomicReference<Segment> atomicReference = INSTANCE.firstRef();
        Segment segment2 = atomicReference.getAndSet(LOCK);
        if (segment2 == LOCK) {
            return;
        }
        Segment segment3 = segment2;
        int n2 = segment3 != null ? segment3.limit : 0;
        if (n2 >= MAX_SIZE) {
            atomicReference.set(segment2);
            return;
        }
        segment.next = segment2;
        segment.pos = 0;
        segment.limit = n2 + 8192;
        atomicReference.set(segment);
    }

    private final AtomicReference<Segment> firstRef() {
        int n2 = (int)(Thread.currentThread().getId() & (long)HASH_BUCKET_COUNT - 1L);
        return hashBuckets[n2];
    }

    static {
        int n2 = 0;
        int n3 = HASH_BUCKET_COUNT;
        AtomicReference[] atomicReferenceArray = new AtomicReference[n3];
        while (n2 < n3) {
            int n4 = n2++;
            atomicReferenceArray[n4] = new AtomicReference();
        }
        hashBuckets = atomicReferenceArray;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package org.bridj;

import java.util.ArrayList;
import java.util.List;
import org.bridj.StructFieldDescription;

final class SolidRanges {
    public final long[] offsets;
    public final long[] lengths;

    public SolidRanges(long[] lArray, long[] lArray2) {
        this.offsets = lArray;
        this.lengths = lArray2;
    }

    static class Builder {
        List<Long> offsets = new ArrayList<Long>();
        List<Long> lengths = new ArrayList<Long>();
        long lastOffset = -1L;
        long nextOffset = 0L;
        int count;

        Builder() {
        }

        void add(StructFieldDescription structFieldDescription) {
            long l2 = structFieldDescription.byteOffset;
            long l3 = structFieldDescription.byteLength;
            if (l2 == this.lastOffset) {
                l3 = Math.max(this.lengths.get(this.count - 1), l3);
                this.lengths.set(this.count - 1, l3);
            } else if (l2 == this.nextOffset && this.count != 0) {
                this.lengths.set(this.count - 1, this.lengths.get(this.count - 1) + l3);
            } else {
                this.offsets.add(l2);
                this.lengths.add(l3);
                ++this.count;
            }
            this.lastOffset = l2;
            this.nextOffset = l2 + l3;
        }

        SolidRanges toSolidRanges() {
            long[] lArray = new long[this.count];
            long[] lArray2 = new long[this.count];
            for (int i2 = 0; i2 < this.count; ++i2) {
                lArray[i2] = this.offsets.get(i2);
                lArray2[i2] = this.lengths.get(i2);
            }
            return new SolidRanges(lArray, lArray2);
        }
    }
}


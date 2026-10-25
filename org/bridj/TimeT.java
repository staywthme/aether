/*
 * Decompiled with CFR 0.152.
 */
package org.bridj;

import java.util.Date;
import java.util.List;
import org.bridj.AbstractIntegral;
import org.bridj.BridJ;
import org.bridj.Platform;
import org.bridj.StructCustomizer;
import org.bridj.StructDescription;
import org.bridj.StructFieldDescription;
import org.bridj.StructObject;
import org.bridj.ann.Field;
import org.bridj.ann.Struct;

public final class TimeT
extends AbstractIntegral {
    public static final int SIZE = Platform.TIME_T_SIZE;

    public TimeT(long l2) {
        super(l2);
    }

    public final int byteSize() {
        return SIZE;
    }

    public final Date toDate() {
        return new Date(this.value);
    }

    public static TimeT valueOf(long l2) {
        return new TimeT(l2);
    }

    public static TimeT valueOf(Date date) {
        return TimeT.valueOf(date.getTime());
    }

    public final String toString() {
        return "TimeT(value = " + this.value + ", time = " + this.toDate() + ")";
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static class timeval_customizer
    extends StructCustomizer {
        @Override
        public void beforeLayout(StructDescription object, List<StructFieldDescription> list2) {
            object = list2.get(0);
            ((StructFieldDescription)object).byteLength = Platform.isWindows() || !Platform.is64Bits() ? 4L : 8L;
            ((StructFieldDescription)object).alignment = ((StructFieldDescription)object).byteLength;
        }
    }

    @Struct(customizer=timeval_customizer.class)
    public static class timeval
    extends StructObject {
        public long getTime() {
            return this.seconds() * 1000L + (long)this.milliseconds();
        }

        @Field(value=0)
        public long seconds() {
            return this.io.getCLongField(this, 0);
        }

        @Field(value=0)
        public timeval seconds(long l2) {
            this.io.setCLongField(this, 0, l2);
            return this;
        }

        public final long seconds_$eq(long l2) {
            this.seconds(l2);
            return l2;
        }

        @Field(value=1)
        public int milliseconds() {
            return this.io.getIntField(this, 1);
        }

        @Field(value=1)
        public timeval milliseconds(int n2) {
            this.io.setIntField(this, 1, n2);
            return this;
        }

        public final int milliseconds_$eq(int n2) {
            this.milliseconds(n2);
            return n2;
        }

        static {
            BridJ.register();
        }
    }
}


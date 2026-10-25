/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32;

import com.sun.jna.IntegerType;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.ByReference;

public interface BaseTSD {

    public static class SIZE_T
    extends ULONG_PTR {
        public SIZE_T() {
            this(0L);
        }

        public SIZE_T(long l2) {
            super(l2);
        }
    }

    public static class DWORD_PTR
    extends IntegerType {
        public DWORD_PTR() {
            this(0L);
        }

        public DWORD_PTR(long l2) {
            super(Native.POINTER_SIZE, l2);
        }
    }

    public static class ULONG_PTRByReference
    extends ByReference {
        public ULONG_PTRByReference() {
            this(new ULONG_PTR(0L));
        }

        public ULONG_PTRByReference(ULONG_PTR uLONG_PTR) {
            super(Native.POINTER_SIZE);
            this.setValue(uLONG_PTR);
        }

        public void setValue(ULONG_PTR uLONG_PTR) {
            if (Native.POINTER_SIZE == 4) {
                this.getPointer().setInt(0L, uLONG_PTR.intValue());
                return;
            }
            this.getPointer().setLong(0L, uLONG_PTR.longValue());
        }

        public ULONG_PTR getValue() {
            return new ULONG_PTR(Native.POINTER_SIZE == 4 ? (long)this.getPointer().getInt(0L) : this.getPointer().getLong(0L));
        }
    }

    public static class ULONG_PTR
    extends IntegerType {
        public ULONG_PTR() {
            this(0L);
        }

        public ULONG_PTR(long l2) {
            super(Native.POINTER_SIZE, l2, true);
        }

        public Pointer toPointer() {
            return Pointer.createConstant(this.longValue());
        }
    }

    public static class SSIZE_T
    extends LONG_PTR {
        public SSIZE_T() {
            this(0L);
        }

        public SSIZE_T(long l2) {
            super(l2);
        }
    }

    public static class LONG_PTR
    extends IntegerType {
        public LONG_PTR() {
            this(0L);
        }

        public LONG_PTR(long l2) {
            super(Native.POINTER_SIZE, l2);
        }

        public Pointer toPointer() {
            return Pointer.createConstant(this.longValue());
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.unix;

import com.sun.jna.Function;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Pointer;

public class LibCUtil {
    private static final NativeLibrary LIBC = NativeLibrary.getInstance("c");
    private static Function mmap = null;
    private static boolean mmap64 = false;
    private static Function ftruncate = null;
    private static boolean ftruncate64 = false;

    private LibCUtil() {
    }

    public static Pointer mmap(Pointer pointer, long l2, int n2, int n3, int n4, long l3) {
        Object[] objectArray = new Object[6];
        Object[] objectArray2 = objectArray;
        objectArray[0] = pointer;
        if (Native.SIZE_T_SIZE == 4) {
            LibCUtil.require32Bit(l2, "length");
            objectArray2[1] = (int)l2;
        } else {
            objectArray2[1] = l2;
        }
        objectArray2[2] = n2;
        objectArray2[3] = n3;
        objectArray2[4] = n4;
        if (mmap64 || Native.LONG_SIZE > 4) {
            objectArray2[5] = l3;
        } else {
            LibCUtil.require32Bit(l3, "offset");
            objectArray2[5] = (int)l3;
        }
        return mmap.invokePointer(objectArray2);
    }

    public static int ftruncate(int n2, long l2) {
        Object[] objectArray = new Object[2];
        Object[] objectArray2 = objectArray;
        objectArray[0] = n2;
        if (ftruncate64 || Native.LONG_SIZE > 4) {
            objectArray2[1] = l2;
        } else {
            LibCUtil.require32Bit(l2, "length");
            objectArray2[1] = (int)l2;
        }
        return ftruncate.invokeInt(objectArray2);
    }

    public static void require32Bit(long l2, String string) {
        if (l2 > Integer.MAX_VALUE) {
            throw new IllegalArgumentException(string + " exceeds 32bit");
        }
    }

    static {
        try {
            mmap = LIBC.getFunction("mmap64", 64);
            mmap64 = true;
        }
        catch (UnsatisfiedLinkError unsatisfiedLinkError) {
            mmap = LIBC.getFunction("mmap", 64);
        }
        try {
            ftruncate = LIBC.getFunction("ftruncate64", 64);
            ftruncate64 = true;
        }
        catch (UnsatisfiedLinkError unsatisfiedLinkError) {
            ftruncate = LIBC.getFunction("ftruncate", 64);
        }
    }
}


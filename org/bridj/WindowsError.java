/*
 * Decompiled with CFR 0.152.
 */
package org.bridj;

import java.lang.reflect.Field;
import org.bridj.NativeError;
import org.bridj.WinExceptionsConstants;

class WindowsError
extends NativeError {
    final int code;
    final long info;
    final long address;

    WindowsError(int n2, long l2, long l3) {
        super(WindowsError.computeMessage(n2, l2, l3));
        this.code = n2;
        this.info = l2;
        this.address = l3;
    }

    public static void throwNew(int n2, long l2, long l3) {
        throw new WindowsError(n2, l2, l3);
    }

    static String subMessage(long l2, long l3) {
        switch ((int)l2) {
            case 0: {
                return "Attempted to read from inaccessible address " + WindowsError.toHex(l3);
            }
            case 1: {
                return "Attempted to write to inaccessible address " + WindowsError.toHex(l3);
            }
            case 8: {
                return "Attempted to execute memory " + WindowsError.toHex(l3) + " that's not executable  (DEP violation)";
            }
        }
        return "?";
    }

    public static String computeMessage(int n2, long l2, long l3) {
        switch (n2) {
            case -1073741819: {
                return "EXCEPTION_ACCESS_VIOLATION : " + WindowsError.subMessage(l2, l3);
            }
            case -1073741818: {
                return "EXCEPTION_IN_PAGE_ERROR : " + WindowsError.subMessage(l2, l3);
            }
        }
        try {
            for (Field field : WinExceptionsConstants.class.getFields()) {
                int n3;
                if (!field.getName().startsWith("EXCEPTION_") || field.getType() != Integer.TYPE || (n3 = ((Integer)field.get(null)).intValue()) != n2) continue;
                return field.getName();
            }
        }
        catch (Throwable throwable) {}
        return "Windows native error (code = " + n2 + ", info = " + l2 + ", address = " + l3 + ") !";
    }
}


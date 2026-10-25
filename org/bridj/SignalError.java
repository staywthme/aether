/*
 * Decompiled with CFR 0.152.
 */
package org.bridj;

import org.bridj.NativeError;

class SignalError
extends NativeError {
    final int signal;
    final int code;
    final long address;

    SignalError(int n2, int n3, long l2) {
        super(SignalError.getFullSignalMessage(n2, n3, l2));
        this.signal = n2;
        this.code = n3;
        this.address = l2;
    }

    public int getSignal() {
        return this.signal;
    }

    public int getCode() {
        return this.code;
    }

    public long getAddress() {
        return this.address;
    }

    public boolean equals(Object object) {
        if (!(object instanceof SignalError)) {
            return false;
        }
        object = (SignalError)object;
        return this.signal == ((SignalError)object).signal && this.code == ((SignalError)object).code;
    }

    public int hashCode() {
        return Integer.valueOf(this.signal).hashCode() ^ Integer.valueOf(this.code).hashCode() ^ Long.valueOf(this.address).hashCode();
    }

    public static String getFullSignalMessage(int n2, int n3, long l2) {
        String string = SignalError.getSignalMessage(n2, 0, l2);
        if (n3 == 0) {
            return string;
        }
        String string2 = SignalError.getSignalMessage(n2, n3, l2);
        if (string2.equals(string)) {
            return string;
        }
        return string + " (" + string2 + ")";
    }

    public static void throwNew(int n2, int n3, long l2) {
        throw new SignalError(n2, n3, l2);
    }

    public static String getSignalMessage(int n2, int n3, long l2) {
        switch (n2) {
            case 11: {
                switch (n3) {
                    case 1: {
                        return "Address not mapped to object";
                    }
                    case 2: {
                        return "Invalid permission for mapped object";
                    }
                }
                return "Segmentation fault : " + SignalError.toHex(l2);
            }
            case 10: {
                switch (n3) {
                    case 1: {
                        return "Invalid address alignment";
                    }
                    case 2: {
                        return "Nonexistent physical address";
                    }
                    case 3: {
                        return "Object-specific HW error";
                    }
                }
                return "Bus error : " + SignalError.toHex(l2);
            }
            case 6: {
                return "Native exception (call to abort())";
            }
            case 8: {
                switch (n3) {
                    case 7: {
                        return "Integer divide by zero";
                    }
                    case 8: {
                        return "Integer overflow";
                    }
                    case 1: {
                        return "Floating point divide by zero";
                    }
                    case 2: {
                        return "Floating point overflow";
                    }
                    case 3: {
                        return "Floating point underflow";
                    }
                    case 4: {
                        return "Floating point inexact result";
                    }
                    case 5: {
                        return "Invalid floating point operation";
                    }
                    case 6: {
                        return "Subscript out of range";
                    }
                }
                return "Floating point error";
            }
            case 12: {
                return "Bad argument to system call";
            }
            case 5: {
                switch (n3) {
                    case 1: {
                        return "Process breakpoint";
                    }
                    case 2: {
                        return "Process trace trap";
                    }
                }
                return "Trace trap";
            }
            case 4: {
                switch (n3) {
                    case 1: {
                        return "Illegal opcode";
                    }
                    case 2: {
                        return "Illegal trap";
                    }
                    case 3: {
                        return "Privileged opcode";
                    }
                    case 4: {
                        return "Illegal operand";
                    }
                    case 5: {
                        return "Illegal addressing mode";
                    }
                    case 6: {
                        return "Privileged register";
                    }
                    case 7: {
                        return "Coprocessor error";
                    }
                    case 8: {
                        return "Internal stack error";
                    }
                }
                return "Illegal instruction";
            }
        }
        return "Native error";
    }
}


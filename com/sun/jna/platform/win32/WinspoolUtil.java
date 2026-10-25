/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.Winspool;
import com.sun.jna.ptr.IntByReference;

public abstract class WinspoolUtil {
    public static Winspool.PRINTER_INFO_1[] getPrinterInfo1() {
        IntByReference intByReference = new IntByReference();
        IntByReference intByReference2 = new IntByReference();
        Winspool.INSTANCE.EnumPrinters(2, null, 1, null, 0, intByReference, intByReference2);
        if (intByReference.getValue() <= 0) {
            return new Winspool.PRINTER_INFO_1[0];
        }
        Winspool.PRINTER_INFO_1 pRINTER_INFO_1 = new Winspool.PRINTER_INFO_1(intByReference.getValue());
        if (!Winspool.INSTANCE.EnumPrinters(2, null, 1, pRINTER_INFO_1.getPointer(), intByReference.getValue(), intByReference, intByReference2)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        pRINTER_INFO_1.read();
        return (Winspool.PRINTER_INFO_1[])pRINTER_INFO_1.toArray(intByReference2.getValue());
    }

    public static Winspool.PRINTER_INFO_2[] getPrinterInfo2() {
        return WinspoolUtil.getPrinterInfo2(2);
    }

    public static Winspool.PRINTER_INFO_2[] getAllPrinterInfo2() {
        return WinspoolUtil.getPrinterInfo2(6);
    }

    private static Winspool.PRINTER_INFO_2[] getPrinterInfo2(int n2) {
        IntByReference intByReference = new IntByReference();
        IntByReference intByReference2 = new IntByReference();
        Winspool.INSTANCE.EnumPrinters(n2, null, 2, null, 0, intByReference, intByReference2);
        if (intByReference.getValue() <= 0) {
            return new Winspool.PRINTER_INFO_2[0];
        }
        Winspool.PRINTER_INFO_2 pRINTER_INFO_2 = new Winspool.PRINTER_INFO_2(intByReference.getValue());
        if (!Winspool.INSTANCE.EnumPrinters(n2, null, 2, pRINTER_INFO_2.getPointer(), intByReference.getValue(), intByReference, intByReference2)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        pRINTER_INFO_2.read();
        return (Winspool.PRINTER_INFO_2[])pRINTER_INFO_2.toArray(intByReference2.getValue());
    }

    public static Winspool.PRINTER_INFO_2 getPrinterInfo2(String object) {
        Winspool.PRINTER_INFO_2 pRINTER_INFO_2;
        block12: {
            WinNT.HANDLEByReference hANDLEByReference;
            IntByReference intByReference;
            Object object2;
            block11: {
                object2 = new IntByReference();
                intByReference = new IntByReference();
                hANDLEByReference = new WinNT.HANDLEByReference();
                if (!Winspool.INSTANCE.OpenPrinter((String)object, hANDLEByReference, null)) {
                    throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
                }
                object = null;
                pRINTER_INFO_2 = null;
                Winspool.INSTANCE.GetPrinter(hANDLEByReference.getValue(), 2, null, 0, (IntByReference)object2);
                if (((IntByReference)object2).getValue() > 0) break block11;
                object2 = new Winspool.PRINTER_INFO_2();
                if (!Winspool.INSTANCE.ClosePrinter(hANDLEByReference.getValue())) {
                    new Win32Exception(Kernel32.INSTANCE.GetLastError());
                }
                return object2;
            }
            try {
                pRINTER_INFO_2 = new Winspool.PRINTER_INFO_2(((IntByReference)object2).getValue());
                if (!Winspool.INSTANCE.GetPrinter(hANDLEByReference.getValue(), 2, pRINTER_INFO_2.getPointer(), ((IntByReference)object2).getValue(), intByReference)) {
                    throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
                }
                pRINTER_INFO_2.read();
            }
            catch (Win32Exception win32Exception) {
                object2 = win32Exception;
                object = win32Exception;
                if (!Winspool.INSTANCE.ClosePrinter(hANDLEByReference.getValue())) {
                    object2 = new Win32Exception(Kernel32.INSTANCE.GetLastError());
                    ((Win32Exception)object2).addSuppressedReflected((Throwable)object);
                }
                break block12;
            }
            catch (Throwable throwable) {
                if (!Winspool.INSTANCE.ClosePrinter(hANDLEByReference.getValue())) {
                    new Win32Exception(Kernel32.INSTANCE.GetLastError());
                }
                throw throwable;
            }
            if (!Winspool.INSTANCE.ClosePrinter(hANDLEByReference.getValue())) {
                new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
        }
        if (object != null) {
            throw object;
        }
        return pRINTER_INFO_2;
    }

    public static Winspool.PRINTER_INFO_4[] getPrinterInfo4() {
        IntByReference intByReference = new IntByReference();
        IntByReference intByReference2 = new IntByReference();
        Winspool.INSTANCE.EnumPrinters(2, null, 4, null, 0, intByReference, intByReference2);
        if (intByReference.getValue() <= 0) {
            return new Winspool.PRINTER_INFO_4[0];
        }
        Winspool.PRINTER_INFO_4 pRINTER_INFO_4 = new Winspool.PRINTER_INFO_4(intByReference.getValue());
        if (!Winspool.INSTANCE.EnumPrinters(2, null, 4, pRINTER_INFO_4.getPointer(), intByReference.getValue(), intByReference, intByReference2)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        pRINTER_INFO_4.read();
        return (Winspool.PRINTER_INFO_4[])pRINTER_INFO_4.toArray(intByReference2.getValue());
    }

    public static Winspool.JOB_INFO_1[] getJobInfo1(WinNT.HANDLEByReference hANDLEByReference) {
        Winspool.JOB_INFO_1 jOB_INFO_1;
        IntByReference intByReference = new IntByReference();
        IntByReference intByReference2 = new IntByReference();
        Winspool.INSTANCE.EnumJobs(hANDLEByReference.getValue(), 0, 255, 1, null, 0, intByReference, intByReference2);
        if (intByReference.getValue() <= 0) {
            return new Winspool.JOB_INFO_1[0];
        }
        int n2 = 0;
        do {
            jOB_INFO_1 = new Winspool.JOB_INFO_1(intByReference.getValue());
            if (Winspool.INSTANCE.EnumJobs(hANDLEByReference.getValue(), 0, 255, 1, jOB_INFO_1.getPointer(), intByReference.getValue(), intByReference, intByReference2)) continue;
            n2 = Kernel32.INSTANCE.GetLastError();
        } while (n2 == 122);
        if (n2 != 0) {
            throw new Win32Exception(n2);
        }
        if (intByReference2.getValue() <= 0) {
            return new Winspool.JOB_INFO_1[0];
        }
        jOB_INFO_1.read();
        return (Winspool.JOB_INFO_1[])jOB_INFO_1.toArray(intByReference2.getValue());
    }
}


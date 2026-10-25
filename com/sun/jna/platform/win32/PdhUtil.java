/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.Pdh;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinReg;
import java.util.ArrayList;
import java.util.List;

public abstract class PdhUtil {
    private static final int CHAR_TO_BYTES = Boolean.getBoolean("w32.ascii") ? 1 : Native.WCHAR_SIZE;
    private static final String ENGLISH_COUNTER_KEY = "SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion\\Perflib\\009";
    private static final String ENGLISH_COUNTER_VALUE = "Counter";

    public static String PdhLookupPerfNameByIndex(String string, int n2) {
        WinDef.DWORDByReference dWORDByReference = new WinDef.DWORDByReference(new WinDef.DWORD(0L));
        int n3 = Pdh.INSTANCE.PdhLookupPerfNameByIndex(string, n2, null, dWORDByReference);
        Pointer pointer = null;
        if (n3 != -1073738819) {
            if (n3 != 0 && n3 != -2147481646) {
                throw new PdhException(n3);
            }
            if (dWORDByReference.getValue().intValue() <= 0) {
                return "";
            }
            pointer = new Memory(dWORDByReference.getValue().intValue() * CHAR_TO_BYTES);
            n3 = Pdh.INSTANCE.PdhLookupPerfNameByIndex(string, n2, pointer, dWORDByReference);
        } else {
            for (int i2 = 32; i2 <= 1024 && ((n3 = Pdh.INSTANCE.PdhLookupPerfNameByIndex(string, n2, pointer = new Memory(i2 * CHAR_TO_BYTES), dWORDByReference = new WinDef.DWORDByReference(new WinDef.DWORD((long)i2)))) == -1073738819 || n3 == -1073738814); i2 <<= 1) {
            }
        }
        if (n3 != 0) {
            throw new PdhException(n3);
        }
        if (CHAR_TO_BYTES == 1) {
            return pointer.getString(0L);
        }
        return pointer.getWideString(0L);
    }

    public static int PdhLookupPerfIndexByEnglishName(String string) {
        String[] stringArray = Advapi32Util.registryGetStringArray(WinReg.HKEY_LOCAL_MACHINE, ENGLISH_COUNTER_KEY, ENGLISH_COUNTER_VALUE);
        for (int i2 = 1; i2 < stringArray.length; i2 += 2) {
            if (!stringArray[i2].equals(string)) continue;
            try {
                return Integer.parseInt(stringArray[i2 - 1]);
            }
            catch (NumberFormatException numberFormatException) {
                return 0;
            }
        }
        return 0;
    }

    public static PdhEnumObjectItems PdhEnumObjectItems(String string, String string2, String string3, int n2) {
        String string4;
        ArrayList<String> arrayList = new ArrayList<String>();
        ArrayList<String> arrayList2 = new ArrayList<String>();
        WinDef.DWORDByReference dWORDByReference = new WinDef.DWORDByReference(new WinDef.DWORD(0L));
        WinDef.DWORDByReference dWORDByReference2 = new WinDef.DWORDByReference(new WinDef.DWORD(0L));
        int n3 = Pdh.INSTANCE.PdhEnumObjectItems(string, string2, string3, null, dWORDByReference, null, dWORDByReference2, n2, 0);
        if (n3 != 0 && n3 != -2147481646) {
            throw new PdhException(n3);
        }
        Memory memory = null;
        Memory memory2 = null;
        do {
            long l2;
            if (dWORDByReference.getValue().intValue() > 0) {
                memory = new Memory(dWORDByReference.getValue().intValue() * CHAR_TO_BYTES);
            }
            if (dWORDByReference2.getValue().intValue() > 0) {
                memory2 = new Memory(dWORDByReference2.getValue().intValue() * CHAR_TO_BYTES);
            }
            if ((n3 = Pdh.INSTANCE.PdhEnumObjectItems(string, string2, string3, memory, dWORDByReference, memory2, dWORDByReference2, n2, 0)) != -2147481646) continue;
            if (memory != null) {
                l2 = memory.size() / (long)CHAR_TO_BYTES;
                dWORDByReference.setValue(new WinDef.DWORD(l2 + 1024L));
                memory.close();
            }
            if (memory2 == null) continue;
            l2 = memory2.size() / (long)CHAR_TO_BYTES;
            dWORDByReference2.setValue(new WinDef.DWORD(l2 + 1024L));
            memory2.close();
        } while (n3 == -2147481646);
        if (n3 != 0) {
            throw new PdhException(n3);
        }
        if (memory != null) {
            int n4 = 0;
            while ((long)n4 < memory.size() && !(string4 = CHAR_TO_BYTES == 1 ? memory.getString(n4) : ((Pointer)memory).getWideString(n4)).isEmpty()) {
                arrayList.add(string4);
                n4 += (string4.length() + 1) * CHAR_TO_BYTES;
            }
        }
        if (memory2 != null) {
            int n5 = 0;
            while ((long)n5 < memory2.size() && !(string4 = CHAR_TO_BYTES == 1 ? memory2.getString(n5) : ((Pointer)memory2).getWideString(n5)).isEmpty()) {
                arrayList2.add(string4);
                n5 += (string4.length() + 1) * CHAR_TO_BYTES;
            }
        }
        return new PdhEnumObjectItems(arrayList, arrayList2);
    }

    public static final class PdhException
    extends RuntimeException {
        private final int errorCode;

        public PdhException(int n2) {
            super(String.format("Pdh call failed with error code 0x%08X", n2));
            this.errorCode = n2;
        }

        public final int getErrorCode() {
            return this.errorCode;
        }
    }

    public static class PdhEnumObjectItems {
        private final List<String> counters;
        private final List<String> instances;

        public PdhEnumObjectItems(List<String> list2, List<String> list3) {
            this.counters = this.copyAndEmptyListForNullList(list2);
            this.instances = this.copyAndEmptyListForNullList(list3);
        }

        public List<String> getCounters() {
            return this.counters;
        }

        public List<String> getInstances() {
            return this.instances;
        }

        private List<String> copyAndEmptyListForNullList(List<String> list2) {
            if (list2 == null) {
                return new ArrayList<String>();
            }
            return new ArrayList<String>(list2);
        }

        public String toString() {
            return "PdhEnumObjectItems{counters=" + this.counters + ", instances=" + this.instances + '}';
        }
    }
}


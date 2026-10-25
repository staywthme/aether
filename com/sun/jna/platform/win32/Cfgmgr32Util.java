/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.Cfgmgr32;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.W32APITypeMapper;

public abstract class Cfgmgr32Util {
    public static String CM_Get_Device_ID(int n2) throws Cfgmgr32Exception {
        int n3 = Boolean.getBoolean("w32.ascii") ? 1 : Native.WCHAR_SIZE;
        IntByReference intByReference = new IntByReference();
        int n4 = Cfgmgr32.INSTANCE.CM_Get_Device_ID_Size(intByReference, n2, 0);
        if (n4 != 0) {
            throw new Cfgmgr32Exception(n4);
        }
        Memory memory = new Memory((intByReference.getValue() + 1) * n3);
        memory.clear();
        n4 = Cfgmgr32.INSTANCE.CM_Get_Device_ID(n2, memory, intByReference.getValue(), 0);
        if (n4 == 26) {
            n4 = Cfgmgr32.INSTANCE.CM_Get_Device_ID_Size(intByReference, n2, 0);
            if (n4 != 0) {
                throw new Cfgmgr32Exception(n4);
            }
            memory = new Memory((intByReference.getValue() + 1) * n3);
            memory.clear();
            n4 = Cfgmgr32.INSTANCE.CM_Get_Device_ID(n2, memory, intByReference.getValue(), 0);
        }
        if (n4 != 0) {
            throw new Cfgmgr32Exception(n4);
        }
        if (n3 == 1) {
            return memory.getString(0L);
        }
        return ((Pointer)memory).getWideString(0L);
    }

    public static Object CM_Get_DevNode_Registry_Property(int n2, int n3) throws Cfgmgr32Exception {
        IntByReference intByReference = new IntByReference();
        IntByReference intByReference2 = new IntByReference();
        int n4 = Cfgmgr32.INSTANCE.CM_Get_DevNode_Registry_Property(n2, n3, intByReference, null, intByReference2, 0);
        if (n4 == 37) {
            return null;
        }
        if (n4 != 26) {
            throw new Cfgmgr32Exception(n4);
        }
        Memory memory = null;
        if (intByReference2.getValue() > 0 && (n4 = Cfgmgr32.INSTANCE.CM_Get_DevNode_Registry_Property(n2, n3, intByReference, memory = new Memory(intByReference2.getValue()), intByReference2, 0)) != 0) {
            throw new Cfgmgr32Exception(n4);
        }
        switch (intByReference.getValue()) {
            case 1: {
                if (memory == null) {
                    return "";
                }
                if (W32APITypeMapper.DEFAULT == W32APITypeMapper.UNICODE) {
                    return ((Pointer)memory).getWideString(0L);
                }
                return memory.getString(0L);
            }
            case 7: {
                if (memory == null) {
                    return new String[0];
                }
                return Advapi32Util.regMultiSzBufferToStringArray(memory);
            }
            case 4: {
                if (memory == null) {
                    return 0;
                }
                return ((Pointer)memory).getInt(0L);
            }
            case 0: {
                return null;
            }
        }
        if (memory == null) {
            return new byte[0];
        }
        return memory.getByteArray(0L, (int)memory.size());
    }

    public static class Cfgmgr32Exception
    extends RuntimeException {
        private final int errorCode;

        public Cfgmgr32Exception(int n2) {
            this.errorCode = n2;
        }

        public int getErrorCode() {
            return this.errorCode;
        }

        @Override
        public String toString() {
            return super.toString() + String.format(" [errorCode: 0x%08x]", this.errorCode);
        }
    }
}


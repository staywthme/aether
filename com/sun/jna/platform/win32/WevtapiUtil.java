/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Wevtapi;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.Winevt;
import com.sun.jna.ptr.IntByReference;

public abstract class WevtapiUtil {
    public static String EvtGetExtendedStatus() {
        IntByReference intByReference = new IntByReference();
        int n2 = Wevtapi.INSTANCE.EvtGetExtendedStatus(0, null, intByReference);
        if (n2 != 0 && n2 != 122) {
            throw new Win32Exception(n2);
        }
        if (intByReference.getValue() == 0) {
            return "";
        }
        char[] cArray = new char[intByReference.getValue()];
        n2 = Wevtapi.INSTANCE.EvtGetExtendedStatus(cArray.length, cArray, intByReference);
        if (n2 != 0) {
            throw new Win32Exception(n2);
        }
        return Native.toString(cArray);
    }

    public static Memory EvtRender(Winevt.EVT_HANDLE eVT_HANDLE, Winevt.EVT_HANDLE eVT_HANDLE2, int n2, IntByReference intByReference) {
        IntByReference intByReference2 = new IntByReference();
        boolean bl = Wevtapi.INSTANCE.EvtRender(eVT_HANDLE, eVT_HANDLE2, n2, 0, null, intByReference2, intByReference);
        int n3 = Kernel32.INSTANCE.GetLastError();
        if (!bl && n3 != 122) {
            throw new Win32Exception(n3);
        }
        Memory memory = new Memory(intByReference2.getValue());
        bl = Wevtapi.INSTANCE.EvtRender(eVT_HANDLE, eVT_HANDLE2, n2, (int)memory.size(), memory, intByReference2, intByReference);
        if (!bl) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        return memory;
    }

    public static String EvtFormatMessage(Winevt.EVT_HANDLE eVT_HANDLE, Winevt.EVT_HANDLE eVT_HANDLE2, int n2, int n3, Winevt.EVT_VARIANT[] eVT_VARIANTArray, int n4) {
        IntByReference intByReference = new IntByReference();
        boolean bl = Wevtapi.INSTANCE.EvtFormatMessage(eVT_HANDLE, eVT_HANDLE2, n2, n3, eVT_VARIANTArray, n4, 0, null, intByReference);
        int n5 = Kernel32.INSTANCE.GetLastError();
        if (!bl && n5 != 122) {
            throw new Win32Exception(n5);
        }
        char[] cArray = new char[intByReference.getValue()];
        bl = Wevtapi.INSTANCE.EvtFormatMessage(eVT_HANDLE, eVT_HANDLE2, n2, n3, eVT_VARIANTArray, n4, cArray.length, cArray, intByReference);
        if (!bl) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        return Native.toString(cArray);
    }

    public static Winevt.EVT_VARIANT EvtGetChannelConfigProperty(Winevt.EVT_HANDLE object, int n2) {
        IntByReference intByReference = new IntByReference();
        boolean bl = Wevtapi.INSTANCE.EvtGetChannelConfigProperty((Winevt.EVT_HANDLE)object, n2, 0, 0, null, intByReference);
        int n3 = Kernel32.INSTANCE.GetLastError();
        if (!bl && n3 != 122) {
            throw new Win32Exception(n3);
        }
        Memory memory = new Memory(intByReference.getValue());
        bl = Wevtapi.INSTANCE.EvtGetChannelConfigProperty((Winevt.EVT_HANDLE)object, n2, 0, (int)memory.size(), memory, intByReference);
        if (!bl) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        object = new Winevt.EVT_VARIANT(memory);
        ((Structure)object).read();
        return object;
    }

    public static String EvtNextPublisherId(Winevt.EVT_HANDLE eVT_HANDLE) {
        IntByReference intByReference = new IntByReference();
        boolean bl = Wevtapi.INSTANCE.EvtNextPublisherId(eVT_HANDLE, 0, null, intByReference);
        int n2 = Kernel32.INSTANCE.GetLastError();
        if (!bl && n2 != 122) {
            throw new Win32Exception(n2);
        }
        char[] cArray = new char[intByReference.getValue()];
        bl = Wevtapi.INSTANCE.EvtNextPublisherId(eVT_HANDLE, cArray.length, cArray, intByReference);
        if (!bl) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        return Native.toString(cArray);
    }

    public static Memory EvtGetPublisherMetadataProperty(Winevt.EVT_HANDLE eVT_HANDLE, int n2, int n3) {
        IntByReference intByReference = new IntByReference();
        boolean bl = Wevtapi.INSTANCE.EvtGetPublisherMetadataProperty(eVT_HANDLE, n2, n3, 0, null, intByReference);
        int n4 = Kernel32.INSTANCE.GetLastError();
        if (!bl && n4 != 122) {
            throw new Win32Exception(n4);
        }
        Memory memory = new Memory(intByReference.getValue());
        bl = Wevtapi.INSTANCE.EvtGetPublisherMetadataProperty(eVT_HANDLE, n2, n3, (int)memory.size(), memory, intByReference);
        if (!bl) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        return memory;
    }
}


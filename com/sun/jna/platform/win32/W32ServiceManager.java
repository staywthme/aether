/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.Advapi32;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.W32Service;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.Winsvc;
import com.sun.jna.ptr.IntByReference;
import java.io.Closeable;

public class W32ServiceManager
implements Closeable {
    Winsvc.SC_HANDLE _handle = null;
    String _machineName = null;
    String _databaseName = null;

    public W32ServiceManager() {
    }

    public W32ServiceManager(int n2) {
        this.open(n2);
    }

    public W32ServiceManager(String string, String string2) {
        this._machineName = string;
        this._databaseName = string2;
    }

    public W32ServiceManager(String string, String string2, int n2) {
        this._machineName = string;
        this._databaseName = string2;
        this.open(n2);
    }

    public void open(int n2) {
        this.close();
        this._handle = Advapi32.INSTANCE.OpenSCManager(this._machineName, this._databaseName, n2);
        if (this._handle == null) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
    }

    @Override
    public void close() {
        if (this._handle != null) {
            if (!Advapi32.INSTANCE.CloseServiceHandle(this._handle)) {
                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
            this._handle = null;
        }
    }

    public W32Service openService(String object, int n2) {
        if ((object = Advapi32.INSTANCE.OpenService(this._handle, (String)object, n2)) == null) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        return new W32Service((Winsvc.SC_HANDLE)object);
    }

    public Winsvc.SC_HANDLE getHandle() {
        return this._handle;
    }

    public Winsvc.ENUM_SERVICE_STATUS_PROCESS[] enumServicesStatusExProcess(int n2, int n3, String string) {
        Memory memory;
        IntByReference intByReference = new IntByReference(0);
        IntByReference intByReference2 = new IntByReference(0);
        IntByReference intByReference3 = new IntByReference(0);
        Advapi32.INSTANCE.EnumServicesStatusEx(this._handle, 0, n2, n3, Pointer.NULL, 0, intByReference, intByReference2, intByReference3, string);
        int n4 = Kernel32.INSTANCE.GetLastError();
        if (n4 != 234) {
            throw new Win32Exception(n4);
        }
        Memory memory2 = memory = new Memory(intByReference.getValue());
        boolean bl = Advapi32.INSTANCE.EnumServicesStatusEx(this._handle, 0, n2, n3, memory2, (int)memory2.size(), intByReference, intByReference2, intByReference3, string);
        n2 = bl ? 1 : 0;
        if (!bl) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        if (intByReference2.getValue() == 0) {
            return new Winsvc.ENUM_SERVICE_STATUS_PROCESS[0];
        }
        Winsvc.ENUM_SERVICE_STATUS_PROCESS eNUM_SERVICE_STATUS_PROCESS = Structure.newInstance(Winsvc.ENUM_SERVICE_STATUS_PROCESS.class, memory);
        eNUM_SERVICE_STATUS_PROCESS.read();
        return (Winsvc.ENUM_SERVICE_STATUS_PROCESS[])eNUM_SERVICE_STATUS_PROCESS.toArray(intByReference2.getValue());
    }
}


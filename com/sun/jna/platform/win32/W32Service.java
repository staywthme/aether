/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.Advapi32;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.Winsvc;
import com.sun.jna.ptr.IntByReference;
import java.io.Closeable;
import java.util.List;

public class W32Service
implements Closeable {
    Winsvc.SC_HANDLE _handle = null;

    public W32Service(Winsvc.SC_HANDLE sC_HANDLE) {
        this._handle = sC_HANDLE;
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

    private void addShutdownPrivilegeToProcess() {
        WinNT.HANDLEByReference hANDLEByReference = new WinNT.HANDLEByReference();
        WinNT.LUID lUID = new WinNT.LUID();
        Advapi32.INSTANCE.OpenProcessToken(Kernel32.INSTANCE.GetCurrentProcess(), 32, hANDLEByReference);
        Advapi32.INSTANCE.LookupPrivilegeValue("", "SeShutdownPrivilege", lUID);
        WinNT.TOKEN_PRIVILEGES tOKEN_PRIVILEGES = new WinNT.TOKEN_PRIVILEGES(1);
        tOKEN_PRIVILEGES.Privileges[0] = new WinNT.LUID_AND_ATTRIBUTES(lUID, new WinDef.DWORD(2L));
        WinNT.TOKEN_PRIVILEGES tOKEN_PRIVILEGES2 = tOKEN_PRIVILEGES;
        Advapi32.INSTANCE.AdjustTokenPrivileges(hANDLEByReference.getValue(), false, tOKEN_PRIVILEGES2, tOKEN_PRIVILEGES2.size(), null, new IntByReference());
    }

    public void setFailureActions(List<Winsvc.SC_ACTION> object, int n2, String string, String string2) {
        Winsvc.SERVICE_FAILURE_ACTIONS.ByReference byReference = new Winsvc.SERVICE_FAILURE_ACTIONS.ByReference();
        new Winsvc.SERVICE_FAILURE_ACTIONS.ByReference().dwResetPeriod = n2;
        byReference.lpRebootMsg = string;
        byReference.lpCommand = string2;
        byReference.cActions = object.size();
        byReference.lpsaActions = new Winsvc.SC_ACTION.ByReference();
        Winsvc.SC_ACTION[] sC_ACTIONArray = (Winsvc.SC_ACTION[])byReference.lpsaActions.toArray(object.size());
        boolean bl = false;
        int n3 = 0;
        object = object.iterator();
        while (object.hasNext()) {
            Winsvc.SC_ACTION sC_ACTION = (Winsvc.SC_ACTION)object.next();
            if (!bl && sC_ACTION.type == 2) {
                this.addShutdownPrivilegeToProcess();
                bl = true;
            }
            sC_ACTIONArray[n3].type = sC_ACTION.type;
            sC_ACTIONArray[n3].delay = sC_ACTION.delay;
            ++n3;
        }
        if (!Advapi32.INSTANCE.ChangeServiceConfig2(this._handle, 2, byReference)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
    }

    private Pointer queryServiceConfig2(int n2) {
        IntByReference intByReference = new IntByReference();
        Advapi32.INSTANCE.QueryServiceConfig2(this._handle, n2, Pointer.NULL, 0, intByReference);
        Memory memory = new Memory(intByReference.getValue());
        if (!Advapi32.INSTANCE.QueryServiceConfig2(this._handle, n2, memory, intByReference.getValue(), new IntByReference())) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        return memory;
    }

    public Winsvc.SERVICE_FAILURE_ACTIONS getFailureActions() {
        Object object = this.queryServiceConfig2(2);
        object = new Winsvc.SERVICE_FAILURE_ACTIONS((Pointer)object);
        return object;
    }

    public void setFailureActionsFlag(boolean bl) {
        Winsvc.SERVICE_FAILURE_ACTIONS_FLAG sERVICE_FAILURE_ACTIONS_FLAG = new Winsvc.SERVICE_FAILURE_ACTIONS_FLAG();
        int n2 = new Winsvc.SERVICE_FAILURE_ACTIONS_FLAG().fFailureActionsOnNonCrashFailures = bl ? 1 : 0;
        if (!Advapi32.INSTANCE.ChangeServiceConfig2(this._handle, 4, sERVICE_FAILURE_ACTIONS_FLAG)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
    }

    public boolean getFailureActionsFlag() {
        Object object = this.queryServiceConfig2(4);
        object = new Winsvc.SERVICE_FAILURE_ACTIONS_FLAG((Pointer)object);
        return ((Winsvc.SERVICE_FAILURE_ACTIONS_FLAG)object).fFailureActionsOnNonCrashFailures != 0;
    }

    public Winsvc.SERVICE_STATUS_PROCESS queryStatus() {
        Winsvc.SERVICE_STATUS_PROCESS sERVICE_STATUS_PROCESS;
        IntByReference intByReference = new IntByReference();
        Advapi32.INSTANCE.QueryServiceStatusEx(this._handle, 0, null, 0, intByReference);
        Winsvc.SERVICE_STATUS_PROCESS sERVICE_STATUS_PROCESS2 = sERVICE_STATUS_PROCESS = new Winsvc.SERVICE_STATUS_PROCESS(intByReference.getValue());
        if (!Advapi32.INSTANCE.QueryServiceStatusEx(this._handle, 0, sERVICE_STATUS_PROCESS2, sERVICE_STATUS_PROCESS2.size(), intByReference)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        return sERVICE_STATUS_PROCESS;
    }

    public void startService() {
        this.waitForNonPendingState();
        if (this.queryStatus().dwCurrentState == 4) {
            return;
        }
        if (!Advapi32.INSTANCE.StartService(this._handle, 0, null)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        this.waitForNonPendingState();
        if (this.queryStatus().dwCurrentState != 4) {
            throw new RuntimeException("Unable to start the service");
        }
    }

    public void stopService() {
        this.stopService(30000L);
    }

    public void stopService(long l2) {
        long l3 = System.currentTimeMillis();
        this.waitForNonPendingState();
        if (this.queryStatus().dwCurrentState == 1) {
            return;
        }
        Winsvc.SERVICE_STATUS sERVICE_STATUS = new Winsvc.SERVICE_STATUS();
        if (!Advapi32.INSTANCE.ControlService(this._handle, 1, sERVICE_STATUS)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        while (sERVICE_STATUS.dwCurrentState != 1) {
            long l4 = l2 - (System.currentTimeMillis() - l3);
            if (l4 < 0L) {
                throw new RuntimeException(String.format("Service stop exceeded timeout time of %d ms", l2));
            }
            long l5 = Math.min((long)this.sanitizeWaitTime(sERVICE_STATUS.dwWaitHint), l4);
            try {
                Thread.sleep(l5);
            }
            catch (InterruptedException interruptedException) {
                throw new RuntimeException(interruptedException);
            }
            if (Advapi32.INSTANCE.QueryServiceStatus(this._handle, sERVICE_STATUS)) continue;
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
    }

    public void continueService() {
        this.waitForNonPendingState();
        if (this.queryStatus().dwCurrentState == 4) {
            return;
        }
        if (!Advapi32.INSTANCE.ControlService(this._handle, 3, new Winsvc.SERVICE_STATUS())) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        this.waitForNonPendingState();
        if (this.queryStatus().dwCurrentState != 4) {
            throw new RuntimeException("Unable to continue the service");
        }
    }

    public void pauseService() {
        this.waitForNonPendingState();
        if (this.queryStatus().dwCurrentState == 7) {
            return;
        }
        if (!Advapi32.INSTANCE.ControlService(this._handle, 2, new Winsvc.SERVICE_STATUS())) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        this.waitForNonPendingState();
        if (this.queryStatus().dwCurrentState != 7) {
            throw new RuntimeException("Unable to pause the service");
        }
    }

    int sanitizeWaitTime(int n2) {
        if ((n2 /= 10) < 1000) {
            n2 = 1000;
        } else if (n2 > 10000) {
            n2 = 10000;
        }
        return n2;
    }

    public void waitForNonPendingState() {
        Winsvc.SERVICE_STATUS_PROCESS sERVICE_STATUS_PROCESS = this.queryStatus();
        int n2 = sERVICE_STATUS_PROCESS.dwCheckPoint;
        int n3 = Kernel32.INSTANCE.GetTickCount();
        while (this.isPendingState(sERVICE_STATUS_PROCESS.dwCurrentState)) {
            if (sERVICE_STATUS_PROCESS.dwCheckPoint != n2) {
                n2 = sERVICE_STATUS_PROCESS.dwCheckPoint;
                n3 = Kernel32.INSTANCE.GetTickCount();
            }
            if (Kernel32.INSTANCE.GetTickCount() - n3 > sERVICE_STATUS_PROCESS.dwWaitHint) {
                throw new RuntimeException("Timeout waiting for service to change to a non-pending state.");
            }
            int n4 = this.sanitizeWaitTime(sERVICE_STATUS_PROCESS.dwWaitHint);
            try {
                Thread.sleep(n4);
            }
            catch (InterruptedException interruptedException) {
                throw new RuntimeException(interruptedException);
            }
            sERVICE_STATUS_PROCESS = this.queryStatus();
        }
    }

    private boolean isPendingState(int n2) {
        switch (n2) {
            case 2: 
            case 3: 
            case 5: 
            case 6: {
                return true;
            }
        }
        return false;
    }

    public Winsvc.SC_HANDLE getHandle() {
        return this._handle;
    }

    public Winsvc.ENUM_SERVICE_STATUS[] enumDependentServices(int n2) {
        Memory memory;
        IntByReference intByReference = new IntByReference(0);
        IntByReference intByReference2 = new IntByReference(0);
        Advapi32.INSTANCE.EnumDependentServices(this._handle, n2, Pointer.NULL, 0, intByReference, intByReference2);
        int n3 = Kernel32.INSTANCE.GetLastError();
        if (n3 != 234) {
            throw new Win32Exception(n3);
        }
        Memory memory2 = memory = new Memory(intByReference.getValue());
        boolean bl = Advapi32.INSTANCE.EnumDependentServices(this._handle, n2, memory2, (int)memory2.size(), intByReference, intByReference2);
        n2 = bl ? 1 : 0;
        if (!bl) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        if (intByReference2.getValue() == 0) {
            return new Winsvc.ENUM_SERVICE_STATUS[0];
        }
        Winsvc.ENUM_SERVICE_STATUS eNUM_SERVICE_STATUS = Structure.newInstance(Winsvc.ENUM_SERVICE_STATUS.class, memory);
        eNUM_SERVICE_STATUS.read();
        return (Winsvc.ENUM_SERVICE_STATUS[])eNUM_SERVICE_STATUS.toArray(intByReference2.getValue());
    }
}


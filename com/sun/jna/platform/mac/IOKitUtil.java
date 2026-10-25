/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.mac;

import com.sun.jna.platform.mac.CoreFoundation;
import com.sun.jna.platform.mac.IOKit;
import com.sun.jna.platform.mac.SystemB;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

public class IOKitUtil {
    private static final IOKit IO = IOKit.INSTANCE;
    private static final SystemB SYS = SystemB.INSTANCE;

    private IOKitUtil() {
    }

    public static int getMasterPort() {
        IntByReference intByReference = new IntByReference();
        IO.IOMasterPort(0, intByReference);
        return intByReference.getValue();
    }

    public static IOKit.IORegistryEntry getRoot() {
        int n2 = IOKitUtil.getMasterPort();
        IOKit.IORegistryEntry iORegistryEntry = IO.IORegistryGetRootEntry(n2);
        SystemB systemB = SYS;
        systemB.mach_port_deallocate(systemB.mach_task_self(), n2);
        return iORegistryEntry;
    }

    public static IOKit.IOService getMatchingService(String object) {
        if ((object = IO.IOServiceMatching((String)object)) != null) {
            return IOKitUtil.getMatchingService((CoreFoundation.CFDictionaryRef)object);
        }
        return null;
    }

    public static IOKit.IOService getMatchingService(CoreFoundation.CFDictionaryRef pointerType) {
        int n2 = IOKitUtil.getMasterPort();
        pointerType = IO.IOServiceGetMatchingService(n2, (CoreFoundation.CFDictionaryRef)pointerType);
        SystemB systemB = SYS;
        systemB.mach_port_deallocate(systemB.mach_task_self(), n2);
        return pointerType;
    }

    public static IOKit.IOIterator getMatchingServices(String object) {
        if ((object = IO.IOServiceMatching((String)object)) != null) {
            return IOKitUtil.getMatchingServices((CoreFoundation.CFDictionaryRef)object);
        }
        return null;
    }

    public static IOKit.IOIterator getMatchingServices(CoreFoundation.CFDictionaryRef cFDictionaryRef) {
        int n2 = IOKitUtil.getMasterPort();
        PointerByReference pointerByReference = new PointerByReference();
        int n3 = IO.IOServiceGetMatchingServices(n2, cFDictionaryRef, pointerByReference);
        SystemB systemB = SYS;
        systemB.mach_port_deallocate(systemB.mach_task_self(), n2);
        if (n3 == 0 && pointerByReference.getValue() != null) {
            return new IOKit.IOIterator(pointerByReference.getValue());
        }
        return null;
    }

    public static CoreFoundation.CFMutableDictionaryRef getBSDNameMatchingDict(String object) {
        int n2 = IOKitUtil.getMasterPort();
        object = IO.IOBSDNameMatching(n2, 0, (String)object);
        SystemB systemB = SYS;
        systemB.mach_port_deallocate(systemB.mach_task_self(), n2);
        return object;
    }
}


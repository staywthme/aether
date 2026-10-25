/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.mac;

import com.sun.jna.Library;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import com.sun.jna.platform.mac.CoreFoundation;
import com.sun.jna.platform.mac.IOReturnException;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;

public interface IOKit
extends Library {
    public static final IOKit INSTANCE = Native.load("IOKit", IOKit.class);
    public static final int kIORegistryIterateRecursively = 1;
    public static final int kIORegistryIterateParents = 2;
    public static final int kIOReturnNoDevice = -536870208;
    public static final double kIOPSTimeRemainingUnlimited = -2.0;
    public static final double kIOPSTimeRemainingUnknown = -1.0;

    public int IOMasterPort(int var1, IntByReference var2);

    public CoreFoundation.CFMutableDictionaryRef IOServiceMatching(String var1);

    public CoreFoundation.CFMutableDictionaryRef IOServiceNameMatching(String var1);

    public CoreFoundation.CFMutableDictionaryRef IOBSDNameMatching(int var1, int var2, String var3);

    public IOService IOServiceGetMatchingService(int var1, CoreFoundation.CFDictionaryRef var2);

    public int IOServiceGetMatchingServices(int var1, CoreFoundation.CFDictionaryRef var2, PointerByReference var3);

    public IORegistryEntry IOIteratorNext(IOIterator var1);

    public CoreFoundation.CFTypeRef IORegistryEntryCreateCFProperty(IORegistryEntry var1, CoreFoundation.CFStringRef var2, CoreFoundation.CFAllocatorRef var3, int var4);

    public int IORegistryEntryCreateCFProperties(IORegistryEntry var1, PointerByReference var2, CoreFoundation.CFAllocatorRef var3, int var4);

    public CoreFoundation.CFTypeRef IORegistryEntrySearchCFProperty(IORegistryEntry var1, String var2, CoreFoundation.CFStringRef var3, CoreFoundation.CFAllocatorRef var4, int var5);

    public int IORegistryEntryGetRegistryEntryID(IORegistryEntry var1, LongByReference var2);

    public int IORegistryEntryGetName(IORegistryEntry var1, Pointer var2);

    public int IORegistryEntryGetChildIterator(IORegistryEntry var1, String var2, PointerByReference var3);

    public int IORegistryEntryGetChildEntry(IORegistryEntry var1, String var2, PointerByReference var3);

    public int IORegistryEntryGetParentEntry(IORegistryEntry var1, String var2, PointerByReference var3);

    public IORegistryEntry IORegistryGetRootEntry(int var1);

    public boolean IOObjectConformsTo(IOObject var1, String var2);

    public int IOObjectRelease(IOObject var1);

    public int IOServiceOpen(IOService var1, int var2, int var3, PointerByReference var4);

    public int IOServiceGetBusyState(IOService var1, IntByReference var2);

    public int IOServiceClose(IOConnect var1);

    public CoreFoundation.CFTypeRef IOPSCopyPowerSourcesInfo();

    public CoreFoundation.CFArrayRef IOPSCopyPowerSourcesList(CoreFoundation.CFTypeRef var1);

    public CoreFoundation.CFDictionaryRef IOPSGetPowerSourceDescription(CoreFoundation.CFTypeRef var1, CoreFoundation.CFTypeRef var2);

    public double IOPSGetTimeRemainingEstimate();

    public static class IOConnect
    extends IOService {
        public IOConnect() {
        }

        public IOConnect(Pointer pointer) {
            super(pointer);
        }
    }

    public static class IOService
    extends IORegistryEntry {
        public IOService() {
        }

        public IOService(Pointer pointer) {
            super(pointer);
        }
    }

    public static class IORegistryEntry
    extends IOObject {
        public IORegistryEntry() {
        }

        public IORegistryEntry(Pointer pointer) {
            super(pointer);
        }

        public long getRegistryEntryID() {
            LongByReference longByReference = new LongByReference();
            int n2 = INSTANCE.IORegistryEntryGetRegistryEntryID(this, longByReference);
            if (n2 != 0) {
                throw new IOReturnException(n2);
            }
            return longByReference.getValue();
        }

        public String getName() {
            Memory memory = new Memory(128L);
            int n2 = INSTANCE.IORegistryEntryGetName(this, memory);
            if (n2 != 0) {
                throw new IOReturnException(n2);
            }
            return memory.getString(0L);
        }

        public IOIterator getChildIterator(String string) {
            PointerByReference pointerByReference = new PointerByReference();
            int n2 = INSTANCE.IORegistryEntryGetChildIterator(this, string, pointerByReference);
            if (n2 != 0) {
                throw new IOReturnException(n2);
            }
            return new IOIterator(pointerByReference.getValue());
        }

        public IORegistryEntry getChildEntry(String string) {
            PointerByReference pointerByReference = new PointerByReference();
            int n2 = INSTANCE.IORegistryEntryGetChildEntry(this, string, pointerByReference);
            if (n2 == -536870208) {
                return null;
            }
            if (n2 != 0) {
                throw new IOReturnException(n2);
            }
            return new IORegistryEntry(pointerByReference.getValue());
        }

        public IORegistryEntry getParentEntry(String string) {
            PointerByReference pointerByReference = new PointerByReference();
            int n2 = INSTANCE.IORegistryEntryGetParentEntry(this, string, pointerByReference);
            if (n2 == -536870208) {
                return null;
            }
            if (n2 != 0) {
                throw new IOReturnException(n2);
            }
            return new IORegistryEntry(pointerByReference.getValue());
        }

        public CoreFoundation.CFTypeRef createCFProperty(CoreFoundation.CFStringRef cFStringRef) {
            return INSTANCE.IORegistryEntryCreateCFProperty(this, cFStringRef, CoreFoundation.INSTANCE.CFAllocatorGetDefault(), 0);
        }

        public CoreFoundation.CFMutableDictionaryRef createCFProperties() {
            PointerByReference pointerByReference = new PointerByReference();
            int n2 = INSTANCE.IORegistryEntryCreateCFProperties(this, pointerByReference, CoreFoundation.INSTANCE.CFAllocatorGetDefault(), 0);
            if (n2 != 0) {
                throw new IOReturnException(n2);
            }
            return new CoreFoundation.CFMutableDictionaryRef(pointerByReference.getValue());
        }

        CoreFoundation.CFTypeRef searchCFProperty(String string, CoreFoundation.CFStringRef cFStringRef, int n2) {
            return INSTANCE.IORegistryEntrySearchCFProperty(this, string, cFStringRef, CoreFoundation.INSTANCE.CFAllocatorGetDefault(), n2);
        }

        public String getStringProperty(String object) {
            String string = null;
            object = CoreFoundation.CFStringRef.createCFString((String)object);
            CoreFoundation.CFTypeRef cFTypeRef = this.createCFProperty((CoreFoundation.CFStringRef)object);
            ((CoreFoundation.CFTypeRef)object).release();
            if (cFTypeRef != null) {
                object = new CoreFoundation.CFStringRef(cFTypeRef.getPointer());
                string = ((CoreFoundation.CFStringRef)object).stringValue();
                cFTypeRef.release();
            }
            return string;
        }

        public Long getLongProperty(String object) {
            Long l2 = null;
            object = CoreFoundation.CFStringRef.createCFString((String)object);
            CoreFoundation.CFTypeRef cFTypeRef = this.createCFProperty((CoreFoundation.CFStringRef)object);
            ((CoreFoundation.CFTypeRef)object).release();
            if (cFTypeRef != null) {
                object = new CoreFoundation.CFNumberRef(cFTypeRef.getPointer());
                l2 = ((CoreFoundation.CFNumberRef)object).longValue();
                cFTypeRef.release();
            }
            return l2;
        }

        public Integer getIntegerProperty(String object) {
            Integer n2 = null;
            object = CoreFoundation.CFStringRef.createCFString((String)object);
            CoreFoundation.CFTypeRef cFTypeRef = this.createCFProperty((CoreFoundation.CFStringRef)object);
            ((CoreFoundation.CFTypeRef)object).release();
            if (cFTypeRef != null) {
                object = new CoreFoundation.CFNumberRef(cFTypeRef.getPointer());
                n2 = ((CoreFoundation.CFNumberRef)object).intValue();
                cFTypeRef.release();
            }
            return n2;
        }

        public Double getDoubleProperty(String object) {
            Double d2 = null;
            object = CoreFoundation.CFStringRef.createCFString((String)object);
            CoreFoundation.CFTypeRef cFTypeRef = this.createCFProperty((CoreFoundation.CFStringRef)object);
            ((CoreFoundation.CFTypeRef)object).release();
            if (cFTypeRef != null) {
                object = new CoreFoundation.CFNumberRef(cFTypeRef.getPointer());
                d2 = ((CoreFoundation.CFNumberRef)object).doubleValue();
                cFTypeRef.release();
            }
            return d2;
        }

        public Boolean getBooleanProperty(String object) {
            Boolean bl = null;
            object = CoreFoundation.CFStringRef.createCFString((String)object);
            CoreFoundation.CFTypeRef cFTypeRef = this.createCFProperty((CoreFoundation.CFStringRef)object);
            ((CoreFoundation.CFTypeRef)object).release();
            if (cFTypeRef != null) {
                object = new CoreFoundation.CFBooleanRef(cFTypeRef.getPointer());
                bl = ((CoreFoundation.CFBooleanRef)object).booleanValue();
                cFTypeRef.release();
            }
            return bl;
        }

        public byte[] getByteArrayProperty(String object) {
            byte[] byArray = null;
            object = CoreFoundation.CFStringRef.createCFString((String)object);
            CoreFoundation.CFTypeRef cFTypeRef = this.createCFProperty((CoreFoundation.CFStringRef)object);
            ((CoreFoundation.CFTypeRef)object).release();
            if (cFTypeRef != null) {
                object = new CoreFoundation.CFDataRef(cFTypeRef.getPointer());
                int n2 = ((CoreFoundation.CFDataRef)object).getLength();
                object = ((CoreFoundation.CFDataRef)object).getBytePtr();
                byArray = ((Pointer)object).getByteArray(0L, n2);
                cFTypeRef.release();
            }
            return byArray;
        }
    }

    public static class IOIterator
    extends IOObject {
        public IOIterator() {
        }

        public IOIterator(Pointer pointer) {
            super(pointer);
        }

        public IORegistryEntry next() {
            return INSTANCE.IOIteratorNext(this);
        }
    }

    public static class IOObject
    extends PointerType {
        public IOObject() {
        }

        public IOObject(Pointer pointer) {
            super(pointer);
        }

        public boolean conformsTo(String string) {
            return INSTANCE.IOObjectConformsTo(this, string);
        }

        public int release() {
            return INSTANCE.IOObjectRelease(this);
        }
    }
}


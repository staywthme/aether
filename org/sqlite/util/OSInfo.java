/*
 * Decompiled with CFR 0.152.
 */
package org.sqlite.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Locale;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlite.util.ProcessRunner;

public class OSInfo {
    protected static ProcessRunner processRunner = new ProcessRunner();
    private static final HashMap<String, String> archMapping = new HashMap<String, String>();
    public static final String X86 = "x86";
    public static final String X86_64 = "x86_64";
    public static final String IA64_32 = "ia64_32";
    public static final String IA64 = "ia64";
    public static final String PPC = "ppc";
    public static final String PPC64 = "ppc64";

    public static void main(String[] stringArray) {
        if (stringArray.length > 0) {
            if ("--os".equals(stringArray[0])) {
                System.out.print(OSInfo.getOSName());
                return;
            }
            if ("--arch".equals(stringArray[0])) {
                System.out.print(OSInfo.getArchName());
                return;
            }
        }
        System.out.print(OSInfo.getNativeLibFolderPathForCurrentOS());
    }

    public static String getNativeLibFolderPathForCurrentOS() {
        return OSInfo.getOSName() + "/" + OSInfo.getArchName();
    }

    public static String getOSName() {
        return OSInfo.translateOSNameToFolderName(System.getProperty("os.name"));
    }

    public static boolean isAndroid() {
        return OSInfo.isAndroidRuntime() || OSInfo.isAndroidTermux();
    }

    public static boolean isAndroidRuntime() {
        return System.getProperty("java.runtime.name", "").toLowerCase().contains("android");
    }

    public static boolean isAndroidTermux() {
        try {
            return processRunner.runAndWaitFor("uname -o").toLowerCase().contains("android");
        }
        catch (Exception exception) {
            return false;
        }
    }

    public static boolean isMusl() {
        boolean bl;
        block8: {
            Object object = Paths.get("/proc/self/map_files", new String[0]);
            object = Files.list((Path)object);
            try {
                bl = object.map(path -> {
                    try {
                        return path.toRealPath(new LinkOption[0]).toString();
                    }
                    catch (IOException iOException) {
                        return "";
                    }
                }).anyMatch(string -> string.toLowerCase().contains("musl"));
                if (object == null) break block8;
            }
            catch (Throwable throwable) {
                try {
                    if (object != null) {
                        try {
                            object.close();
                        }
                        catch (Throwable throwable2) {
                            throwable.addSuppressed(throwable2);
                        }
                    }
                    throw throwable;
                }
                catch (Exception exception) {
                    return OSInfo.isAlpineLinux();
                }
            }
            object.close();
        }
        return bl;
    }

    private static boolean isAlpineLinux() {
        boolean bl;
        block8: {
            Stream<String> stream = Files.lines(Paths.get("/etc/os-release", new String[0]));
            try {
                bl = stream.anyMatch(string -> string.startsWith("ID") && string.contains("alpine"));
                if (stream == null) break block8;
            }
            catch (Throwable throwable) {
                try {
                    if (stream != null) {
                        try {
                            stream.close();
                        }
                        catch (Throwable throwable2) {
                            throwable.addSuppressed(throwable2);
                        }
                    }
                    throw throwable;
                }
                catch (Exception exception) {
                    return false;
                }
            }
            stream.close();
        }
        return bl;
    }

    static String getHardwareName() {
        try {
            return processRunner.runAndWaitFor("uname -m");
        }
        catch (Throwable throwable) {
            LogHolder.logger.error("Error while running uname -m", throwable);
            return "unknown";
        }
    }

    static String resolveArmArchType() {
        if (System.getProperty("os.name").contains("Linux")) {
            Object object = OSInfo.getHardwareName();
            if (OSInfo.isAndroid()) {
                if (object.startsWith("aarch64")) {
                    return "aarch64";
                }
                return "arm";
            }
            if (object.startsWith("armv6")) {
                return "armv6";
            }
            if (object.startsWith("armv7")) {
                return "armv7";
            }
            if (object.startsWith("armv5")) {
                return "arm";
            }
            if (object.startsWith("aarch64")) {
                return "aarch64";
            }
            object = System.getProperty("sun.arch.abi");
            if (object != null && object.startsWith("gnueabihf")) {
                return "armv7";
            }
            object = System.getProperty("java.home");
            try {
                int n2 = Runtime.getRuntime().exec("which readelf").waitFor();
                if (n2 == 0) {
                    object = new String[]{"/bin/sh", "-c", "find '" + (String)object + "' -name 'libjvm.so' | head -1 | xargs readelf -A | grep 'Tag_ABI_VFP_args: VFP registers'"};
                    n2 = Runtime.getRuntime().exec((String[])object).waitFor();
                    if (n2 == 0) {
                        return "armv7";
                    }
                } else {
                    LogHolder.logger.warn("readelf not found. Cannot check if running on an armhf system, armel architecture will be presumed");
                }
            }
            catch (IOException | InterruptedException exception) {}
        }
        return "arm";
    }

    public static String getArchName() {
        String string = System.getProperty("org.sqlite.osinfo.architecture");
        if (string != null) {
            return string;
        }
        string = System.getProperty("os.arch");
        if (string.startsWith("arm")) {
            string = OSInfo.resolveArmArchType();
        } else {
            String string2 = string.toLowerCase(Locale.US);
            if (archMapping.containsKey(string2)) {
                return archMapping.get(string2);
            }
        }
        return OSInfo.translateArchNameToFolderName(string);
    }

    static String translateOSNameToFolderName(String string) {
        if (string.contains("Windows")) {
            return "Windows";
        }
        if (string.contains("Mac") || string.contains("Darwin")) {
            return "Mac";
        }
        if (string.contains("AIX")) {
            return "AIX";
        }
        if (OSInfo.isMusl()) {
            return "Linux-Musl";
        }
        if (OSInfo.isAndroid()) {
            return "Linux-Android";
        }
        if (string.contains("Linux")) {
            return "Linux";
        }
        return string.replaceAll("\\W", "");
    }

    static String translateArchNameToFolderName(String string) {
        return string.replaceAll("\\W", "");
    }

    static {
        archMapping.put(X86, X86);
        archMapping.put("i386", X86);
        archMapping.put("i486", X86);
        archMapping.put("i586", X86);
        archMapping.put("i686", X86);
        archMapping.put("pentium", X86);
        archMapping.put(X86_64, X86_64);
        archMapping.put("amd64", X86_64);
        archMapping.put("em64t", X86_64);
        archMapping.put("universal", X86_64);
        archMapping.put(IA64, IA64);
        archMapping.put("ia64w", IA64);
        archMapping.put(IA64_32, IA64_32);
        archMapping.put("ia64n", IA64_32);
        archMapping.put(PPC, PPC);
        archMapping.put("power", PPC);
        archMapping.put("powerpc", PPC);
        archMapping.put("power_pc", PPC);
        archMapping.put("power_rs", PPC);
        archMapping.put(PPC64, PPC64);
        archMapping.put("power64", PPC64);
        archMapping.put("powerpc64", PPC64);
        archMapping.put("power_pc64", PPC64);
        archMapping.put("power_rs64", PPC64);
        archMapping.put("ppc64el", PPC64);
        archMapping.put("ppc64le", PPC64);
    }

    static class LogHolder {
        private static final Logger logger = LoggerFactory.getLogger(OSInfo.class);

        private LogHolder() {
        }
    }
}


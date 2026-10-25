/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.io.File;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

class HostProperties {
    private static final Logger logger = Logger.getLogger(HostProperties.class.getName());
    private static final String TEMP_DIR_PROPERTY_NAME = "org.conscrypt.tmpdir";
    static final OperatingSystem OS = HostProperties.getOperatingSystem(System.getProperty("os.name", ""));
    static final Architecture ARCH = HostProperties.getArchitecture(System.getProperty("os.arch", ""));

    static boolean isWindows() {
        return OS == OperatingSystem.WINDOWS;
    }

    static boolean isOSX() {
        return OS == OperatingSystem.OSX;
    }

    static File getTempDir() {
        File file;
        try {
            file = HostProperties.toDirectory(System.getProperty(TEMP_DIR_PROPERTY_NAME));
            if (file != null) {
                return file;
            }
            file = HostProperties.toDirectory(System.getProperty("java.io.tmpdir"));
            if (file != null) {
                return file;
            }
            if (HostProperties.isWindows()) {
                file = HostProperties.toDirectory(System.getenv("TEMP"));
                if (file != null) {
                    return file;
                }
                String string = System.getenv("USERPROFILE");
                if (string != null) {
                    file = HostProperties.toDirectory(string + "\\AppData\\Local\\Temp");
                    if (file != null) {
                        return file;
                    }
                    file = HostProperties.toDirectory(string + "\\Local Settings\\Temp");
                    if (file != null) {
                        return file;
                    }
                }
            } else {
                file = HostProperties.toDirectory(System.getenv("TMPDIR"));
                if (file != null) {
                    return file;
                }
            }
        }
        catch (Exception exception) {}
        file = HostProperties.isWindows() ? new File("C:\\Windows\\Temp") : new File("/tmp");
        logger.log(Level.WARNING, "Failed to get the temporary directory; falling back to: {0}", file);
        return file;
    }

    private static File toDirectory(String object) {
        if (object == null) {
            return null;
        }
        object = new File((String)object);
        ((File)object).mkdirs();
        if (!((File)object).isDirectory()) {
            return null;
        }
        try {
            return ((File)object).getAbsoluteFile();
        }
        catch (Exception exception) {
            return object;
        }
    }

    private static String normalize(String string) {
        return string.toLowerCase(Locale.US).replaceAll("[^a-z0-9]+", "");
    }

    private static OperatingSystem getOperatingSystem(String string) {
        if ((string = HostProperties.normalize(string)).startsWith("aix")) {
            return OperatingSystem.AIX;
        }
        if (string.startsWith("hpux")) {
            return OperatingSystem.HPUX;
        }
        if (string.startsWith("os400") && (string.length() <= 5 || !Character.isDigit(string.charAt(5)))) {
            return OperatingSystem.OS400;
        }
        if (string.startsWith("linux")) {
            return OperatingSystem.LINUX;
        }
        if (string.startsWith("macosx") || string.startsWith("osx")) {
            return OperatingSystem.OSX;
        }
        if (string.startsWith("freebsd")) {
            return OperatingSystem.FREEBSD;
        }
        if (string.startsWith("openbsd")) {
            return OperatingSystem.OPENBSD;
        }
        if (string.startsWith("netbsd")) {
            return OperatingSystem.NETBSD;
        }
        if (string.startsWith("solaris") || string.startsWith("sunos")) {
            return OperatingSystem.SUNOS;
        }
        if (string.startsWith("windows")) {
            return OperatingSystem.WINDOWS;
        }
        return OperatingSystem.UNKNOWN;
    }

    private static Architecture getArchitecture(String string) {
        if ((string = HostProperties.normalize(string)).matches("^(x8664|amd64|ia32e|em64t|x64)$")) {
            return Architecture.X86_64;
        }
        if (string.matches("^(x8632|x86|i[3-6]86|ia32|x32)$")) {
            return Architecture.X86_32;
        }
        if (string.matches("^(ia64|itanium64)$")) {
            return Architecture.ITANIUM_64;
        }
        if (string.matches("^(sparc|sparc32)$")) {
            return Architecture.SPARC_32;
        }
        if (string.matches("^(sparcv9|sparc64)$")) {
            return Architecture.SPARC_64;
        }
        if (string.matches("^(arm|arm32)$")) {
            return Architecture.ARM_32;
        }
        if ("aarch64".equals(string)) {
            return Architecture.AARCH_64;
        }
        if (string.matches("^(ppc|ppc32)$")) {
            return Architecture.PPC_32;
        }
        if ("ppc64".equals(string)) {
            return Architecture.PPC_64;
        }
        if ("ppc64le".equals(string)) {
            return Architecture.PPCLE_64;
        }
        if ("s390".equals(string)) {
            return Architecture.S390_32;
        }
        if ("s390x".equals(string)) {
            return Architecture.S390_64;
        }
        return Architecture.UNKNOWN;
    }

    private HostProperties() {
    }

    static enum Architecture {
        X86_64,
        X86_32{

            @Override
            public final String getFileComponent() {
                return "x86";
            }
        }
        ,
        ITANIUM_64,
        SPARC_32,
        SPARC_64,
        ARM_32,
        AARCH_64,
        PPC_32,
        PPC_64,
        PPCLE_64,
        S390_32,
        S390_64,
        UNKNOWN;


        public String getFileComponent() {
            return this.name().toLowerCase();
        }
    }

    static enum OperatingSystem {
        AIX,
        HPUX,
        OS400,
        LINUX,
        OSX,
        FREEBSD,
        OPENBSD,
        NETBSD,
        SUNOS,
        WINDOWS,
        UNKNOWN;


        public final String getFileComponent() {
            return this.name().toLowerCase();
        }
    }
}


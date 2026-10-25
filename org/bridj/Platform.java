/*
 * Decompiled with CFR 0.152.
 */
package org.bridj;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import org.bridj.BridJ;
import org.bridj.NativeLibrary;
import org.bridj.util.ProcessUtils;
import org.bridj.util.StringUtils;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class Platform {
    static final String osName;
    private static boolean inited;
    static final String BridJLibraryName = "bridj";
    public static final int POINTER_SIZE;
    public static final int WCHAR_T_SIZE;
    public static final int SIZE_T_SIZE;
    public static final int TIME_T_SIZE;
    public static final int CLONG_SIZE;
    static final ClassLoader systemClassLoader;
    static final List<String> embeddedLibraryResourceRoots;
    static Set<File> temporaryExtractedLibraryCanonicalFiles;
    private static final String arch;
    private static boolean is64Bits;
    private static File extractedLibrariesTempDir;
    private static List<NativeLibrary> nativeLibraries;
    public static boolean useUnicodeVersionOfWindowsAPIs;
    static final long DELETE_OLD_BINARIES_AFTER_MILLIS = 86400000L;
    static final int maxTempFileAttempts = 20;
    static final /* synthetic */ boolean $assertionsDisabled;

    public static ClassLoader getClassLoader() {
        return Platform.getClassLoader(BridJ.class);
    }

    public static ClassLoader getClassLoader(Class<?> object) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader != null) {
            return classLoader;
        }
        if ((object = object == null ? null : ((Class)object).getClassLoader()) == null) {
            return systemClassLoader;
        }
        return object;
    }

    public static InputStream getResourceAsStream(String string) {
        URL uRL = Platform.getResource(string);
        try {
            if (uRL != null) {
                return uRL.openStream();
            }
            return null;
        }
        catch (IOException iOException) {
            if (BridJ.verbose) {
                BridJ.warning("Failed to get resource '" + string + "'", iOException);
            }
            return null;
        }
    }

    public static URL getResource(String string) {
        Object object;
        if (!string.startsWith("/")) {
            string = "/" + string;
        }
        if ((object = BridJ.class.getResource(string)) != null) {
            return object;
        }
        object = new ClassLoader[]{BridJ.class.getClassLoader(), Thread.currentThread().getContextClassLoader(), systemClassLoader};
        ClassLoader[] classLoaderArray = object;
        int cfr_ignored_0 = ((ClassLoader[])object).length;
        for (int i2 = 0; i2 < 3; ++i2) {
            object = classLoaderArray[i2];
            if (object == null || (object = ((ClassLoader)object).getResource(string)) == null) continue;
            return object;
        }
        return null;
    }

    public static synchronized void addEmbeddedLibraryResourceRoot(String string) {
        embeddedLibraryResourceRoots.add(0, string);
    }

    static void addTemporaryExtractedLibraryFileToDeleteOnExit(File file) throws IOException {
        file = file.getCanonicalFile();
        temporaryExtractedLibraryCanonicalFiles.add(file);
        file.deleteOnExit();
    }

    static void addNativeLibrary(NativeLibrary nativeLibrary) {
        List<NativeLibrary> list2 = nativeLibraries;
        synchronized (list2) {
            nativeLibraries.add(nativeLibrary);
            return;
        }
    }

    private static void shutdown() {
        Platform.deleteTemporaryExtractedLibraryFiles();
    }

    private static void releaseNativeLibraries() {
        List<NativeLibrary> list2 = nativeLibraries;
        synchronized (list2) {
            int n2 = nativeLibraries.size();
            while (n2-- != 0) {
                NativeLibrary nativeLibrary = nativeLibraries.get(n2);
                try {
                    nativeLibrary.release();
                }
                catch (Throwable throwable) {
                    BridJ.error("Failed to release library '" + nativeLibrary.path + "' : " + throwable, throwable);
                }
            }
            return;
        }
    }

    private static void deleteTemporaryExtractedLibraryFiles() {
        Set<File> set = temporaryExtractedLibraryCanonicalFiles;
        synchronized (set) {
            temporaryExtractedLibraryCanonicalFiles.add(extractedLibrariesTempDir);
            ArrayList<File> arrayList = new ArrayList<File>();
            for (File file : temporaryExtractedLibraryCanonicalFiles) {
                if (file.delete()) {
                    if (!BridJ.verbose) continue;
                    BridJ.info("Deleted temporary library file '" + file + "'");
                    continue;
                }
                arrayList.add(file);
            }
            if (!arrayList.isEmpty()) {
                if (BridJ.verbose) {
                    BridJ.info("Attempting to delete " + arrayList.size() + " files after JVM exit : " + StringUtils.implode(arrayList, (Object)", "));
                }
                try {
                    ProcessUtils.startJavaProcess(DeleteFiles.class, arrayList);
                }
                catch (Throwable throwable) {
                    BridJ.error("Failed to launch process to delete files after JVM exit : " + throwable, throwable);
                }
            }
            return;
        }
    }

    static ClassLoader createClassLoader() {
        ArrayList<URL> arrayList = new ArrayList<URL>();
        String[] stringArray = new String[]{"java.class.path", "sun.boot.class.path"};
        int cfr_ignored_0 = stringArray.length;
        for (int i2 = 0; i2 < 2; ++i2) {
            String[] stringArray2 = stringArray[i2];
            if ((stringArray2 = System.getProperty((String)stringArray2)) == null) continue;
            for (String string : stringArray2.split(File.pathSeparator)) {
                URL uRL;
                if ((string = string.trim()).length() == 0) continue;
                try {
                    uRL = new URL(string);
                }
                catch (MalformedURLException malformedURLException) {
                    try {
                        uRL = new File(string).toURI().toURL();
                    }
                    catch (MalformedURLException malformedURLException2) {
                        uRL = null;
                    }
                }
                if (uRL == null) continue;
                arrayList.add(uRL);
            }
        }
        ArrayList<URL> arrayList2 = arrayList;
        return new URLClassLoader(arrayList2.toArray(new URL[arrayList2.size()]));
    }

    static String getenvOrProperty(String string, String string2, String string3) {
        if ((string = System.getenv(string)) == null) {
            string = System.getProperty(string2);
        }
        if (string == null) {
            string = string3;
        }
        return string;
    }

    public static synchronized void initLibrary() {
        if (inited) {
            return;
        }
        inited = true;
        try {
            boolean bl = false;
            String string = Platform.getenvOrProperty("BRIDJ_LIBRARY", "bridj.library", null);
            String string2 = null;
            if (string != null) {
                try {
                    string2 = string;
                    System.load(string2);
                    bl = true;
                }
                catch (Throwable throwable) {
                    BridJ.error("Failed to load forced library " + string, throwable);
                }
            }
            if (!bl) {
                if (!Platform.isAndroid()) {
                    try {
                        File file = Platform.extractEmbeddedLibraryResource(BridJLibraryName);
                        if (file == null) {
                            throw new FileNotFoundException("Failed to extract embedded library 'bridj' (could be a classloader issue, or missing binary in resource path " + StringUtils.implode(embeddedLibraryResourceRoots, (Object)", ") + ")");
                        }
                        if (BridJ.veryVerbose) {
                            BridJ.info("Loading library " + file);
                        }
                        string2 = file.toString();
                        System.load(string2);
                        BridJ.setNativeLibraryFile(BridJLibraryName, file);
                        bl = true;
                    }
                    catch (IOException iOException) {
                        BridJ.error("Failed to load 'bridj'", iOException);
                    }
                }
                if (!bl) {
                    System.loadLibrary(BridJLibraryName);
                }
            }
            if (BridJ.veryVerbose) {
                BridJ.info("Loaded library " + string2);
            }
            Platform.init();
            if (BridJ.logCalls) {
                BridJ.info("Calls logs enabled");
            }
            return;
        }
        catch (Throwable throwable) {
            throw new RuntimeException("Failed to initialize " + BridJ.class.getSimpleName() + " (" + throwable + ")", throwable);
        }
    }

    private static native void init();

    public static boolean isLinux() {
        return Platform.isUnix() && osName.toLowerCase().contains("linux");
    }

    public static boolean isMacOSX() {
        return Platform.isUnix() && (osName.startsWith("Mac") || osName.startsWith("Darwin"));
    }

    public static boolean isSolaris() {
        return Platform.isUnix() && (osName.startsWith("SunOS") || osName.startsWith("Solaris"));
    }

    public static boolean isBSD() {
        return Platform.isUnix() && (osName.contains("BSD") || Platform.isMacOSX());
    }

    public static boolean isUnix() {
        return File.separatorChar == '/';
    }

    public static boolean isWindows() {
        return File.separatorChar == '\\';
    }

    public static boolean isWindows7() {
        return osName.equals("Windows 7");
    }

    private static String getArch() {
        return arch;
    }

    public static String getMachine() {
        String string = Platform.getArch();
        if (string.equals("amd64") || string.equals("x86_64")) {
            if (Platform.is64Bits()) {
                return "x86_64";
            }
            return "i386";
        }
        return string;
    }

    public static boolean isAndroid() {
        return "dalvik".equalsIgnoreCase(System.getProperty("java.vm.name")) && Platform.isLinux();
    }

    public static boolean isArm() {
        String string = Platform.getArch();
        return "arm".equals(string);
    }

    public static boolean isSparc() {
        String string = Platform.getArch();
        return "sparc".equals(string) || "sparcv9".equals(string);
    }

    public static boolean is64Bits() {
        return is64Bits;
    }

    public static boolean isAmd64Arch() {
        String string = Platform.getArch();
        return string.equals("x86_64");
    }

    static List<String> getPossibleFileNames(String string) {
        ArrayList<String> arrayList = new ArrayList<String>(1);
        if (Platform.isWindows()) {
            arrayList.add(string + ".dll");
            arrayList.add(string + ".drv");
        } else {
            String string2 = "lib" + string + ".jnilib";
            if (Platform.isMacOSX()) {
                arrayList.add("lib" + string + ".dylib");
                arrayList.add(string2);
            } else {
                arrayList.add("lib" + string + ".so");
                arrayList.add(string + ".so");
                arrayList.add(string2);
            }
        }
        if (!$assertionsDisabled && arrayList.isEmpty()) {
            throw new AssertionError();
        }
        if (string.contains(".")) {
            arrayList.add(string);
        }
        return arrayList;
    }

    static synchronized List<String> getEmbeddedLibraryPaths(String object) {
        object = new ArrayList(embeddedLibraryResourceRoots.size());
        for (String string : embeddedLibraryResourceRoots) {
            if (string == null) continue;
            if (Platform.isWindows()) {
                object.add(string + (Platform.is64Bits() ? "win64/" : "win32/"));
                continue;
            }
            if (Platform.isMacOSX()) {
                if (Platform.isArm()) {
                    object.add(string + "iphoneos_arm32_arm/");
                    continue;
                }
                object.add(string + "darwin_universal/");
                if (!Platform.isAmd64Arch()) continue;
                object.add(string + "darwin_x64/");
                continue;
            }
            if (Platform.isAndroid()) {
                if (!$assertionsDisabled && !string.equals("libs/")) {
                    throw new AssertionError();
                }
                object.add(string + "armeabi/");
                continue;
            }
            if (Platform.isLinux()) {
                if (Platform.isArm()) {
                    object.add(string + "linux_armhf/");
                    continue;
                }
                object.add(string + (Platform.is64Bits() ? "linux_x64/" : "linux_x86/"));
                continue;
            }
            if (!Platform.isSolaris()) continue;
            if (Platform.isSparc()) {
                object.add(string + (Platform.is64Bits() ? "sunos_sparc64/" : "sunos_sparc/"));
                continue;
            }
            object.add(string + (Platform.is64Bits() ? "sunos_x64/" : "sunos_x86/"));
        }
        if (object.isEmpty()) {
            throw new RuntimeException("Platform not supported ! (os.name='" + osName + "', os.arch='" + System.getProperty("os.arch") + "')");
        }
        return object;
    }

    static synchronized List<String> getEmbeddedLibraryResource(String string) {
        Object object = Platform.getEmbeddedLibraryPaths(string);
        List<String> list2 = Platform.getPossibleFileNames(string);
        ArrayList<String> arrayList = new ArrayList<String>(object.size() * list2.size());
        object = object.iterator();
        while (object.hasNext()) {
            String string2 = (String)object.next();
            for (String string3 : list2) {
                arrayList.add(string2 + string3);
            }
        }
        if (BridJ.veryVerbose) {
            BridJ.info("Embedded resource paths for library '" + string + "': " + arrayList);
        }
        return arrayList;
    }

    static void tryDeleteFilesInSameDirectory(final File file, final Pattern pattern, long l2) {
        final long l3 = System.currentTimeMillis() - l2;
        new Thread(new Runnable(){

            public final void run() {
                File file3 = file.getParentFile();
                String string = file.getName();
                try {
                    for (String string2 : file3.list()) {
                        File file2;
                        if (string2.equals(string) || !pattern.matcher(string2).matches() || (file2 = new File(file3, string2)).lastModified() > l3 || !file2.delete() || !BridJ.verbose) continue;
                        BridJ.info("Deleted old binary file '" + file2 + "'");
                    }
                    return;
                }
                catch (SecurityException securityException) {
                    BridJ.warning("Failed to delete files matching '" + pattern + "' in directory '" + file3 + "'", securityException);
                    return;
                }
                catch (Throwable throwable) {
                    BridJ.error("Unexpected error : " + throwable, throwable);
                    return;
                }
            }
        }).start();
    }

    static File extractEmbeddedLibraryResource(String object) throws IOException {
        Object object2 = Platform.getEmbeddedLibraryResource((String)object);
        if (BridJ.veryVerbose) {
            BridJ.info("Library resources for " + (String)object + ": " + object2);
        }
        object = object2.iterator();
        while (object.hasNext()) {
            int n2;
            object2 = (String)object.next();
            ((String)object2).lastIndexOf(46);
            byte[] byArray = new byte[8196];
            InputStream inputStream2 = Platform.getResourceAsStream((String)object2);
            if (inputStream2 == null) {
                if (!((File)(object2 = new File((String)object2))).exists()) {
                    object2 = new File(((File)object2).getName());
                }
                if (!((File)object2).exists()) continue;
                return ((File)object2).getCanonicalFile();
            }
            object2 = new File((String)object2).getName();
            object = new File(extractedLibrariesTempDir, (String)object2);
            object2 = new BufferedOutputStream(new FileOutputStream((File)object));
            while ((n2 = inputStream2.read(byArray)) > 0) {
                ((OutputStream)object2).write(byArray, 0, n2);
            }
            ((OutputStream)object2).close();
            inputStream2.close();
            Platform.addTemporaryExtractedLibraryFileToDeleteOnExit((File)object);
            Platform.addTemporaryExtractedLibraryFileToDeleteOnExit(((File)object).getParentFile());
            return object;
        }
        return null;
    }

    static File createTempDir(String string) throws IOException {
        for (int i2 = 0; i2 < 20; ++i2) {
            File file = File.createTempFile(string, "");
            if (!file.delete() || !file.mkdirs()) continue;
            return file;
        }
        throw new RuntimeException("Failed to create temp dir with prefix '" + string + "' despite 20" + " attempts!");
    }

    public static final void open(URL uRL) throws NoSuchMethodException {
        if (uRL.getProtocol().equals("file")) {
            Platform.open(new File(uRL.getFile()));
            return;
        }
        if (Platform.isMacOSX()) {
            Platform.execArgs("open", uRL.toString());
            return;
        }
        if (Platform.isWindows()) {
            Platform.execArgs("rundll32", "url.dll,FileProtocolHandler", uRL.toString());
            return;
        }
        if (Platform.isUnix() && Platform.hasUnixCommand("gnome-open")) {
            Platform.execArgs("gnome-open", uRL.toString());
            return;
        }
        if (Platform.isUnix() && Platform.hasUnixCommand("konqueror")) {
            Platform.execArgs("konqueror", uRL.toString());
            return;
        }
        if (Platform.isUnix() && Platform.hasUnixCommand("mozilla")) {
            Platform.execArgs("mozilla", uRL.toString());
            return;
        }
        throw new NoSuchMethodException("Cannot open urls on this platform");
    }

    public static final void open(File file) throws NoSuchMethodException {
        if (Platform.isMacOSX()) {
            Platform.execArgs("open", file.getAbsolutePath());
            return;
        }
        if (Platform.isWindows()) {
            if (file.isDirectory()) {
                Platform.execArgs("explorer", file.getAbsolutePath());
                return;
            }
            Platform.execArgs("start", file.getAbsolutePath());
            return;
        }
        if (Platform.isUnix() && Platform.hasUnixCommand("gnome-open")) {
            Platform.execArgs("gnome-open", file.toString());
            return;
        }
        if (Platform.isUnix() && Platform.hasUnixCommand("konqueror")) {
            Platform.execArgs("konqueror", file.toString());
            return;
        }
        if (Platform.isSolaris() && file.isDirectory()) {
            Platform.execArgs("/usr/dt/bin/dtfile", "-folder", file.getAbsolutePath());
            return;
        }
        throw new NoSuchMethodException("Cannot open files on this platform");
    }

    public static final void show(File file) throws NoSuchMethodException, IOException {
        if (Platform.isWindows()) {
            Platform.exec("explorer /e,/select,\"" + file.getCanonicalPath() + "\"");
            return;
        }
        Platform.open(file.getAbsoluteFile().getParentFile());
    }

    static final void execArgs(String ... object) throws NoSuchMethodException {
        try {
            Runtime.getRuntime().exec((String[])object);
            return;
        }
        catch (Exception exception) {
            object = exception;
            exception.printStackTrace();
            throw new NoSuchMethodException(((Throwable)object).toString());
        }
    }

    static final void exec(String object) throws NoSuchMethodException {
        try {
            Runtime.getRuntime().exec((String)object).waitFor();
            return;
        }
        catch (Exception exception) {
            object = exception;
            exception.printStackTrace();
            throw new NoSuchMethodException(((Throwable)object).toString());
        }
    }

    static final boolean hasUnixCommand(String object) {
        try {
            object = Runtime.getRuntime().exec(new String[]{"which", object});
            return ((Process)object).waitFor() == 0;
        }
        catch (Exception exception) {
            object = exception;
            exception.printStackTrace();
            return false;
        }
    }

    static native int sizeOf_size_t();

    static native int sizeOf_time_t();

    static native int sizeOf_wchar_t();

    static native int sizeOf_ptrdiff_t();

    static native int sizeOf_long();

    static native int getMaxDirectMappingArgCount();

    /*
     * Unable to fully structure code
     */
    static {
        Platform.$assertionsDisabled = Platform.class.desiredAssertionStatus() == false;
        Platform.osName = System.getProperty("os.name", "");
        Platform.embeddedLibraryResourceRoots = new ArrayList<String>();
        Platform.temporaryExtractedLibraryCanonicalFiles = Collections.synchronizedSet(new LinkedHashSet<E>());
        Platform.arch = System.getProperty("os.arch");
        var0 = System.getProperty("sun.arch.data.model", System.getProperty("com.ibm.vm.bitmode"));
        if ("32".equals(var0)) ** GOTO lbl-1000
        if ("64".equals(var0)) {
            v0 = true;
        } else if (Platform.arch.contains("64") || Platform.arch.equalsIgnoreCase("sparcv9")) {
            v0 = true;
        } else lbl-1000:
        // 2 sources

        {
            v0 = false;
        }
        Platform.is64Bits = v0;
        Platform.systemClassLoader = Platform.createClassLoader();
        Platform.addEmbeddedLibraryResourceRoot("libs/");
        if (!Platform.isAndroid()) {
            Platform.addEmbeddedLibraryResourceRoot("lib/");
            Platform.addEmbeddedLibraryResourceRoot("org/bridj/lib/");
            if (!"v0_7_0".equals("")) {
                Platform.addEmbeddedLibraryResourceRoot("org/bridj/v0_7_0/lib/");
            }
        }
        try {
            Platform.extractedLibrariesTempDir = Platform.createTempDir("BridJExtractedLibraries");
            Platform.initLibrary();
        }
        catch (Throwable v1) {
            var0 = v1;
            v1.printStackTrace();
        }
        Platform.POINTER_SIZE = Platform.sizeOf_ptrdiff_t();
        Platform.WCHAR_T_SIZE = Platform.sizeOf_wchar_t();
        Platform.SIZE_T_SIZE = Platform.sizeOf_size_t();
        Platform.TIME_T_SIZE = Platform.sizeOf_time_t();
        Platform.CLONG_SIZE = Platform.sizeOf_long();
        Platform.is64Bits = Platform.POINTER_SIZE == 8;
        Runtime.getRuntime().addShutdownHook(new Thread(){

            public final void run() {
                Platform.shutdown();
            }
        });
        Platform.nativeLibraries = new ArrayList<NativeLibrary>();
        Platform.useUnicodeVersionOfWindowsAPIs = "false".equals(System.getProperty("bridj.useUnicodeVersionOfWindowsAPIs")) == false && "0".equals(System.getenv("BRIDJ_USE_UNICODE_VERSION_OF_WINDOWS_APIS")) == false;
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static class DeleteFiles {
        static final long TRY_DELETE_EVERY_MILLIS = 50L;
        static final long FAIL_AFTER_MILLIS = 10000L;

        static boolean delete(List<File> list2) {
            Iterator<File> iterator2 = list2.iterator();
            while (iterator2.hasNext()) {
                File file = iterator2.next();
                if (!file.delete()) continue;
                iterator2.remove();
            }
            return list2.isEmpty();
        }

        public static void main(String[] stringArray) {
            try {
                LinkedList<File> linkedList = new LinkedList<File>();
                for (String string : stringArray) {
                    linkedList.add(new File(string));
                }
                long l2 = System.currentTimeMillis();
                while (!DeleteFiles.delete(linkedList)) {
                    long l3 = System.currentTimeMillis() - l2;
                    if (l3 > 10000L) {
                        BridJ.error("Failed to delete the following files : " + StringUtils.implode(linkedList));
                        System.exit(1);
                    }
                    Thread.sleep(50L);
                }
                return;
            }
            catch (Throwable throwable) {
                Throwable throwable2 = throwable;
                throwable.printStackTrace();
                return;
            }
            finally {
                System.exit(0);
            }
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package org.sqlite;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlite.FileException;
import org.sqlite.NativeLibraryNotFoundException;
import org.sqlite.util.LibraryLoaderUtil;
import org.sqlite.util.OSInfo;
import org.sqlite.util.StringUtils;

public class SQLiteJDBCLoader {
    private static final Logger logger = LoggerFactory.getLogger(SQLiteJDBCLoader.class);
    private static final String LOCK_EXT = ".lck";
    private static boolean extracted = false;

    public static synchronized boolean initialize() throws Exception {
        if (!extracted) {
            SQLiteJDBCLoader.cleanup();
        }
        SQLiteJDBCLoader.loadSQLiteNativeLibrary();
        return extracted;
    }

    private static File getTempDir() {
        return new File(System.getProperty("org.sqlite.tmpdir", System.getProperty("java.io.tmpdir")));
    }

    static void cleanup() {
        String string = "sqlite-" + SQLiteJDBCLoader.getVersion();
        try {
            Stream<Path> stream;
            block8: {
                block7: {
                    stream = Files.list(SQLiteJDBCLoader.getTempDir().toPath());
                    try {
                        stream.filter(path -> !path.getFileName().toString().endsWith(LOCK_EXT) && path.getFileName().toString().startsWith(string)).forEach(path -> {
                            Path path2 = Paths.get(path + LOCK_EXT, new String[0]);
                            if (Files.notExists(path2, new LinkOption[0])) {
                                try {
                                    Files.delete(path);
                                    return;
                                }
                                catch (Exception exception) {
                                    logger.error("Failed to delete old native lib", exception);
                                }
                            }
                        });
                        if (stream == null) break block7;
                    }
                    catch (Throwable throwable) {
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
                    break block8;
                }
                return;
            }
            stream.close();
        }
        catch (IOException iOException) {
            logger.error("Failed to open directory", iOException);
        }
    }

    public static boolean isNativeMode() throws Exception {
        SQLiteJDBCLoader.initialize();
        return extracted;
    }

    static String md5sum(InputStream inputStream2) throws IOException {
        inputStream2 = new BufferedInputStream(inputStream2);
        try {
            Object object = MessageDigest.getInstance("MD5");
            Closeable closeable = new DigestInputStream(inputStream2, (MessageDigest)object);
            while (((DigestInputStream)closeable).read() >= 0) {
            }
            closeable = new ByteArrayOutputStream();
            ((OutputStream)closeable).write(((MessageDigest)object).digest());
            object = ((ByteArrayOutputStream)closeable).toString();
            return object;
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new IllegalStateException("MD5 algorithm is not available: " + noSuchAlgorithmException);
        }
        finally {
            ((BufferedInputStream)inputStream2).close();
        }
    }

    private static boolean contentsEquals(InputStream inputStream2, InputStream inputStream3) throws IOException {
        int n2;
        if (!(inputStream2 instanceof BufferedInputStream)) {
            inputStream2 = new BufferedInputStream(inputStream2);
        }
        if (!(inputStream3 instanceof BufferedInputStream)) {
            inputStream3 = new BufferedInputStream(inputStream3);
        }
        int n3 = inputStream2.read();
        while (n3 != -1) {
            n2 = inputStream3.read();
            if (n3 != n2) {
                return false;
            }
            n3 = inputStream2.read();
        }
        n2 = inputStream3.read();
        return n2 == -1;
    }

    private static boolean extractAndLoadLibraryFile(String object, String string, String string2) throws FileException {
        object = (String)object + "/" + string;
        Object object2 = UUID.randomUUID().toString();
        string = String.format("sqlite-%s-%s-%s", SQLiteJDBCLoader.getVersion(), object2, string);
        object2 = string + LOCK_EXT;
        Path path = Paths.get(string2, string);
        object2 = Paths.get(string2, new String[]{object2});
        try {
            InputStream inputStream2;
            try {
                inputStream2 = SQLiteJDBCLoader.getResourceAsStream((String)object);
                try {
                    if (Files.notExists((Path)object2, new LinkOption[0])) {
                        Files.createFile((Path)object2, new FileAttribute[0]);
                    }
                    Files.copy(inputStream2, path, StandardCopyOption.REPLACE_EXISTING);
                }
                finally {
                    if (inputStream2 != null) {
                        inputStream2.close();
                    }
                }
            }
            finally {
                path.toFile().deleteOnExit();
                object2.toFile().deleteOnExit();
            }
            path.toFile().setReadable(true);
            path.toFile().setWritable(true, true);
            path.toFile().setExecutable(true);
            inputStream2 = SQLiteJDBCLoader.getResourceAsStream((String)object);
            try {
                object = Files.newInputStream(path, new OpenOption[0]);
                try {
                    if (!SQLiteJDBCLoader.contentsEquals(inputStream2, (InputStream)object)) {
                        throw new FileException(String.format("Failed to write a native library file at %s", path));
                    }
                }
                finally {
                    if (object != null) {
                        ((InputStream)object).close();
                    }
                }
            }
            finally {
                if (inputStream2 != null) {
                    inputStream2.close();
                }
            }
            return SQLiteJDBCLoader.loadNativeLibrary(string2, string);
        }
        catch (IOException iOException) {
            logger.error("Unexpected IOException", iOException);
            return false;
        }
    }

    private static InputStream getResourceAsStream(String object) {
        object = ((String)object).substring(1);
        ClassLoader classLoader = SQLiteJDBCLoader.class.getClassLoader();
        object = classLoader.getResource((String)object);
        if (object == null) {
            return null;
        }
        try {
            object = ((URL)object).openConnection();
            ((URLConnection)object).setUseCaches(false);
            return ((URLConnection)object).getInputStream();
        }
        catch (IOException iOException) {
            logger.error("Could not connect", iOException);
            return null;
        }
    }

    private static boolean loadNativeLibrary(String string, String string2) {
        File file = new File(string, string2);
        if (file.exists()) {
            try {
                System.load(new File(string, string2).getAbsolutePath());
                return true;
            }
            catch (UnsatisfiedLinkError unsatisfiedLinkError) {
                logger.error("Failed to load native library: {}. osinfo: {}", string2, OSInfo.getNativeLibFolderPathForCurrentOS(), unsatisfiedLinkError);
                return false;
            }
        }
        return false;
    }

    private static boolean loadNativeLibraryJdk() {
        try {
            System.loadLibrary("sqlitejdbc");
            return true;
        }
        catch (UnsatisfiedLinkError unsatisfiedLinkError) {
            logger.error("Failed to load native library through System.loadLibrary", unsatisfiedLinkError);
            return false;
        }
    }

    private static void loadSQLiteNativeLibrary() throws Exception {
        boolean bl;
        if (extracted) {
            return;
        }
        LinkedList<String> linkedList = new LinkedList<String>();
        String[] stringArray = System.getProperty("org.sqlite.lib.path");
        String string = System.getProperty("org.sqlite.lib.name");
        if (string == null) {
            string = LibraryLoaderUtil.getNativeLibName();
        }
        if (stringArray != null) {
            if (SQLiteJDBCLoader.loadNativeLibrary((String)stringArray, string)) {
                extracted = true;
                return;
            }
            linkedList.add((String)stringArray);
        }
        if (bl = LibraryLoaderUtil.hasNativeLib((String)(stringArray = LibraryLoaderUtil.getNativeLibResourcePath()), string)) {
            String string2 = SQLiteJDBCLoader.getTempDir().getAbsolutePath();
            if (SQLiteJDBCLoader.extractAndLoadLibraryFile((String)stringArray, string, string2)) {
                extracted = true;
                return;
            }
            linkedList.add((String)stringArray);
        }
        String string3 = System.getProperty("java.library.path", "");
        for (String string4 : string3.split(File.pathSeparator)) {
            if (string4.isEmpty()) continue;
            if (SQLiteJDBCLoader.loadNativeLibrary(string4, string)) {
                extracted = true;
                return;
            }
            linkedList.add(string4);
        }
        if (SQLiteJDBCLoader.loadNativeLibraryJdk()) {
            extracted = true;
            return;
        }
        extracted = false;
        throw new NativeLibraryNotFoundException(String.format("No native library found for os.name=%s, os.arch=%s, paths=[%s]", OSInfo.getOSName(), OSInfo.getArchName(), StringUtils.join(linkedList, File.pathSeparator)));
    }

    private static void getNativeLibraryFolderForTheCurrentOS() {
        OSInfo.getOSName();
        OSInfo.getArchName();
    }

    public static int getMajorVersion() {
        String[] stringArray = SQLiteJDBCLoader.getVersion().split("\\.");
        if (stringArray.length > 0) {
            return Integer.parseInt(stringArray[0]);
        }
        return 1;
    }

    public static int getMinorVersion() {
        String[] stringArray = SQLiteJDBCLoader.getVersion().split("\\.");
        if (stringArray.length > 1) {
            return Integer.parseInt(stringArray[1]);
        }
        return 0;
    }

    public static String getVersion() {
        return VersionHolder.VERSION;
    }

    public static final class VersionHolder {
        private static final String VERSION;

        static {
            URL uRL = VersionHolder.class.getResource("/META-INF/maven/org.xerial/sqlite-jdbc/pom.properties");
            if (uRL == null) {
                uRL = VersionHolder.class.getResource("/META-INF/maven/org.xerial/sqlite-jdbc/VERSION");
            }
            String string = "unknown";
            try {
                if (uRL != null) {
                    Properties properties = new Properties();
                    properties.load(uRL.openStream());
                    string = properties.getProperty("version", string);
                    string = string.trim().replaceAll("[^0-9\\.]", "");
                }
            }
            catch (IOException iOException) {
                LoggerFactory.getLogger(VersionHolder.class).error("Could not read version from file: {}", (Object)uRL, (Object)iOException);
            }
            VERSION = string;
        }
    }
}


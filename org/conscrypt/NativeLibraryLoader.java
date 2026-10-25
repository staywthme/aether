/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.conscrypt.HostProperties;
import org.conscrypt.NativeLibraryUtil;
import org.conscrypt.Platform;

final class NativeLibraryLoader {
    private static final Logger logger = Logger.getLogger(NativeLibraryLoader.class.getName());
    private static final String WORK_DIR_PROPERTY_NAME = "org.conscrypt.native.workdir";
    private static final String DELETE_LIB_PROPERTY_NAME = "org.conscrypt.native.deleteLibAfterLoading";
    private static final String NATIVE_RESOURCE_HOME = "META-INF/native/";
    private static final File WORKDIR;
    private static final boolean DELETE_NATIVE_LIB_AFTER_LOADING;

    private static File getWorkDir() {
        String string = System.getProperty(WORK_DIR_PROPERTY_NAME);
        if (string == null) {
            return null;
        }
        File file = new File(string);
        if (!file.mkdirs() && !file.exists()) {
            NativeLibraryLoader.log("Unable to find or create working directory: {0}", string);
            return null;
        }
        try {
            file = file.getAbsoluteFile();
        }
        catch (Exception exception) {}
        return file;
    }

    static boolean loadFirstAvailable(ClassLoader classLoader, List<LoadResult> list2, String ... stringArray) {
        for (String string : stringArray) {
            if (!NativeLibraryLoader.load(string, classLoader, list2)) continue;
            return true;
        }
        return false;
    }

    private static boolean load(String string, ClassLoader classLoader, List<LoadResult> list2) {
        return NativeLibraryLoader.loadFromWorkdir(string, classLoader, list2) || NativeLibraryLoader.loadLibrary(classLoader, string, false, list2);
    }

    private static boolean loadFromWorkdir(String string, ClassLoader classLoader, List<LoadResult> list2) {
        Object object = System.mapLibraryName(string);
        String string2 = NATIVE_RESOURCE_HOME + (String)object;
        URL uRL = classLoader.getResource(string2);
        if (uRL == null && HostProperties.isOSX()) {
            uRL = string2.endsWith(".jnilib") ? classLoader.getResource("META-INF/native/lib" + string + ".dynlib") : classLoader.getResource("META-INF/native/lib" + string + ".jnilib");
        }
        if (uRL == null) {
            return false;
        }
        int n2 = ((String)object).lastIndexOf(46);
        String string3 = ((String)object).substring(0, n2);
        object = ((String)object).substring(n2, ((String)object).length());
        File file = null;
        try {
            file = Platform.createTempFile(string3, (String)object, WORKDIR);
            if (file.isFile() && file.canRead() && !Platform.canExecuteExecutable(file)) {
                throw new IOException(MessageFormat.format("{0} exists but cannot be executed even when execute permissions set; check volume for \"noexec\" flag; use -D{1}=[path] to set native working directory separately.", file.getPath(), WORK_DIR_PROPERTY_NAME));
            }
            NativeLibraryLoader.copyLibrary(uRL, file);
            boolean bl = NativeLibraryLoader.loadLibrary(classLoader, file.getPath(), true, list2);
            return bl;
        }
        catch (IOException iOException) {
            object = new UnsatisfiedLinkError(MessageFormat.format("Failed creating temp file ({0})", file)).initCause(iOException);
            list2.add(LoadResult.newFailureResult(string, true, false, (Throwable)object));
            return false;
        }
        finally {
            if (file != null) {
                boolean bl = false;
                if (DELETE_NATIVE_LIB_AFTER_LOADING) {
                    bl = file.delete();
                }
                if (!bl) {
                    file.deleteOnExit();
                }
            }
        }
    }

    private static void copyLibrary(URL object, File file) throws IOException {
        InputStream inputStream2 = null;
        FileOutputStream fileOutputStream = null;
        try {
            int n2;
            inputStream2 = ((URL)object).openStream();
            fileOutputStream = new FileOutputStream(file);
            object = new byte[8192];
            while ((n2 = inputStream2.read((byte[])object)) > 0) {
                ((OutputStream)fileOutputStream).write((byte[])object, 0, n2);
            }
            fileOutputStream.flush();
        }
        catch (Throwable throwable) {
            NativeLibraryLoader.closeQuietly(inputStream2);
            NativeLibraryLoader.closeQuietly(fileOutputStream);
            throw throwable;
        }
        NativeLibraryLoader.closeQuietly(inputStream2);
        NativeLibraryLoader.closeQuietly(fileOutputStream);
    }

    private static boolean loadLibrary(ClassLoader object, String string, boolean bl, List<LoadResult> list2) {
        try {
            object = NativeLibraryLoader.tryToLoadClass((ClassLoader)object, NativeLibraryUtil.class);
            object = NativeLibraryLoader.loadLibraryFromHelperClassloader(object, string, bl);
            list2.add((LoadResult)object);
            if (((LoadResult)object).loaded) {
                return true;
            }
        }
        catch (Exception exception) {}
        object = NativeLibraryLoader.loadLibraryFromCurrentClassloader(string, bl);
        list2.add((LoadResult)object);
        return ((LoadResult)object).loaded;
    }

    private static LoadResult loadLibraryFromHelperClassloader(final Class<?> clazz, final String string, final boolean bl) {
        return AccessController.doPrivileged(new PrivilegedAction<LoadResult>(){

            @Override
            public LoadResult run() {
                try {
                    Method method = clazz.getMethod("loadLibrary", String.class, Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(null, string, bl);
                    return LoadResult.newSuccessResult(string, bl, true);
                }
                catch (InvocationTargetException invocationTargetException) {
                    return LoadResult.newFailureResult(string, bl, true, invocationTargetException.getCause());
                }
                catch (Throwable throwable) {
                    return LoadResult.newFailureResult(string, bl, true, throwable);
                }
            }
        });
    }

    private static LoadResult loadLibraryFromCurrentClassloader(String string, boolean bl) {
        try {
            NativeLibraryUtil.loadLibrary(string, bl);
            return LoadResult.newSuccessResult(string, bl, false);
        }
        catch (Throwable throwable) {
            return LoadResult.newFailureResult(string, bl, false, throwable);
        }
    }

    private static Class<?> tryToLoadClass(final ClassLoader classLoader, final Class<?> clazz) throws ClassNotFoundException {
        try {
            return classLoader.loadClass(clazz.getName());
        }
        catch (ClassNotFoundException classNotFoundException) {
            final byte[] byArray = NativeLibraryLoader.classToByteArray(clazz);
            return (Class)AccessController.doPrivileged(new PrivilegedAction<Class<?>>(){

                @Override
                public Class<?> run() {
                    try {
                        Method method = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, Integer.TYPE, Integer.TYPE);
                        method.setAccessible(true);
                        return (Class)method.invoke(classLoader, clazz.getName(), byArray, 0, byArray.length);
                    }
                    catch (Exception exception) {
                        throw new IllegalStateException("Define class failed!", exception);
                    }
                }
            });
        }
    }

    private static byte[] classToByteArray(Class<?> clazz) throws ClassNotFoundException {
        Object object = clazz.getName();
        int n2 = ((String)object).lastIndexOf(46);
        if (n2 > 0) {
            object = ((String)object).substring(n2 + 1);
        }
        if ((object = clazz.getResource((String)object + ".class")) == null) {
            throw new ClassNotFoundException(clazz.getName());
        }
        byte[] byArray = new byte[1024];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(4096);
        InputStream inputStream2 = null;
        try {
            int n3;
            inputStream2 = ((URL)object).openStream();
            while ((n3 = inputStream2.read(byArray)) != -1) {
                byteArrayOutputStream.write(byArray, 0, n3);
            }
            byte[] byArray2 = byteArrayOutputStream.toByteArray();
            return byArray2;
        }
        catch (IOException iOException) {
            throw new ClassNotFoundException(clazz.getName(), iOException);
        }
        finally {
            NativeLibraryLoader.closeQuietly(inputStream2);
            NativeLibraryLoader.closeQuietly(byteArrayOutputStream);
        }
    }

    private static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
                return;
            }
            catch (IOException iOException) {}
        }
    }

    private NativeLibraryLoader() {
    }

    private static void log(String string, Object object) {
        logger.log(Level.FINE, string, object);
    }

    private static void log(String string, Object object, Object object2) {
        logger.log(Level.FINE, string, new Object[]{object, object2});
    }

    private static void log(String string, Object object, Object object2, Throwable throwable) {
        NativeLibraryLoader.debug(MessageFormat.format(string, object, object2), throwable);
    }

    private static void debug(String string, Throwable throwable) {
        logger.log(Level.FINE, string, throwable);
    }

    static {
        File file = NativeLibraryLoader.getWorkDir();
        if (file == null) {
            file = HostProperties.getTempDir();
        }
        WORKDIR = file;
        NativeLibraryLoader.log("-D{0}: {1}", WORK_DIR_PROPERTY_NAME, WORKDIR);
        DELETE_NATIVE_LIB_AFTER_LOADING = Boolean.valueOf(System.getProperty(DELETE_LIB_PROPERTY_NAME, "true"));
    }

    static final class LoadResult {
        final String name;
        final boolean absolute;
        final boolean loaded;
        final boolean usingHelperClassloader;
        final Throwable error;

        private static LoadResult newSuccessResult(String string, boolean bl, boolean bl2) {
            return new LoadResult(string, bl, true, bl2, null);
        }

        private static LoadResult newFailureResult(String string, boolean bl, boolean bl2, Throwable throwable) {
            return new LoadResult(string, bl, false, bl2, throwable);
        }

        private LoadResult(String string, boolean bl, boolean bl2, boolean bl3, Throwable throwable) {
            this.name = string;
            this.absolute = bl;
            this.loaded = bl2;
            this.usingHelperClassloader = bl3;
            this.error = throwable;
        }

        final void log() {
            if (this.error != null) {
                NativeLibraryLoader.log("Unable to load the library {0} (using helper classloader={1})", this.name, this.usingHelperClassloader, this.error);
                return;
            }
            NativeLibraryLoader.log("Successfully loaded library {0}  (using helper classloader={1})", this.name, this.usingHelperClassloader);
        }
    }
}


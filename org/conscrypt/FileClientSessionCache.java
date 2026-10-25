/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSession;
import org.conscrypt.SSLClientSessionCache;
import org.conscrypt.io.IoUtils;

public final class FileClientSessionCache {
    private static final Logger logger = Logger.getLogger(FileClientSessionCache.class.getName());
    public static final int MAX_SIZE = 12;
    static final Map<File, Impl> caches = new HashMap<File, Impl>();

    private FileClientSessionCache() {
    }

    public static synchronized SSLClientSessionCache usingDirectory(File file) throws IOException {
        Impl impl = caches.get(file);
        if (impl == null) {
            impl = new Impl(file);
            caches.put(file, impl);
        }
        return impl;
    }

    static synchronized void reset() {
        caches.clear();
    }

    static class CacheFile
    extends File {
        final String name;
        long lastModified = -1L;

        CacheFile(File file, String string) {
            super(file, string);
            this.name = string;
        }

        @Override
        public long lastModified() {
            long l2 = this.lastModified;
            if (l2 == -1L) {
                l2 = this.lastModified = super.lastModified();
            }
            return l2;
        }

        @Override
        public int compareTo(File file) {
            long l2 = this.lastModified() - file.lastModified();
            if (l2 == 0L) {
                return super.compareTo(file);
            }
            if (l2 < 0L) {
                return -1;
            }
            return 1;
        }
    }

    static class Impl
    implements SSLClientSessionCache {
        final File directory;
        Map<String, File> accessOrder = Impl.newAccessOrder();
        int size;
        String[] initialFiles;

        Impl(File file) throws IOException {
            boolean bl = file.exists();
            if (bl && !file.isDirectory()) {
                throw new IOException(file + " exists but is not a directory.");
            }
            if (bl) {
                this.initialFiles = file.list();
                if (this.initialFiles == null) {
                    throw new IOException(file + " exists but cannot list contents.");
                }
                Arrays.sort(this.initialFiles);
                this.size = this.initialFiles.length;
            } else {
                if (!file.mkdirs()) {
                    throw new IOException("Creation of " + file + " directory failed.");
                }
                this.size = 0;
            }
            this.directory = file;
        }

        private static Map<String, File> newAccessOrder() {
            return new LinkedHashMap<String, File>(12, 0.75f, true);
        }

        private static String fileName(String string, int n2) {
            if (string == null) {
                throw new NullPointerException("host == null");
            }
            return string + "." + n2;
        }

        @Override
        public synchronized byte[] getSessionData(String string, int n2) {
            Object object = Impl.fileName(string, n2);
            File file = this.accessOrder.get(object);
            if (file == null) {
                if (this.initialFiles == null) {
                    return null;
                }
                if (Arrays.binarySearch(this.initialFiles, object) < 0) {
                    return null;
                }
                file = new File(this.directory, (String)object);
                this.accessOrder.put((String)object, file);
            }
            try {
                object = new FileInputStream(file);
            }
            catch (FileNotFoundException fileNotFoundException) {
                Impl.logReadError(string, file, fileNotFoundException);
                return null;
            }
            try {
                int n3 = (int)file.length();
                byte[] byArray = new byte[n3];
                new DataInputStream((InputStream)object).readFully(byArray);
                return byArray;
            }
            catch (IOException iOException) {
                Impl.logReadError(string, file, iOException);
                return null;
            }
            finally {
                IoUtils.closeQuietly((Closeable)object);
            }
        }

        static void logReadError(String string, File file, Throwable throwable) {
            logger.log(Level.WARNING, "FileClientSessionCache: Error reading session data for " + string + " from " + file + ".", throwable);
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public synchronized void putSessionData(SSLSession object, byte[] byArray) {
            FileOutputStream fileOutputStream;
            String string = object.getPeerHost();
            if (byArray == null) {
                throw new NullPointerException("sessionData == null");
            }
            object = Impl.fileName(string, object.getPeerPort());
            File file = new File(this.directory, (String)object);
            boolean bl = file.exists();
            try {
                fileOutputStream = new FileOutputStream(file);
            }
            catch (FileNotFoundException fileNotFoundException) {
                Impl.logWriteError(string, file, fileNotFoundException);
                return;
            }
            if (!bl) {
                ++this.size;
                this.makeRoom();
            }
            try {
                fileOutputStream.write(byArray);
                try {
                    fileOutputStream.close();
                    this.accessOrder.put((String)object, file);
                }
                catch (IOException iOException) {
                    Impl.logWriteError(string, file, iOException);
                    return;
                }
                finally {
                    this.delete(file);
                }
                return;
            }
            catch (IOException iOException) {
                Impl.logWriteError(string, file, iOException);
                return;
            }
            finally {
                try {
                    fileOutputStream.close();
                }
                catch (IOException iOException) {
                    Impl.logWriteError(string, file, iOException);
                }
                finally {
                    this.delete(file);
                }
            }
        }

        private void makeRoom() {
            if (this.size <= 12) {
                return;
            }
            this.indexFiles();
            int n2 = this.size - 12;
            Iterator<File> iterator2 = this.accessOrder.values().iterator();
            do {
                this.delete(iterator2.next());
                iterator2.remove();
            } while (--n2 > 0);
        }

        private void indexFiles() {
            Object object = this.initialFiles;
            if (this.initialFiles != null) {
                this.initialFiles = null;
                TreeSet<CacheFile> treeSet = new TreeSet<CacheFile>();
                for (String string : object) {
                    if (this.accessOrder.containsKey(string)) continue;
                    treeSet.add(new CacheFile(this.directory, string));
                }
                if (!treeSet.isEmpty()) {
                    object = Impl.newAccessOrder();
                    for (CacheFile cacheFile : treeSet) {
                        object.put(cacheFile.name, cacheFile);
                    }
                    object.putAll(this.accessOrder);
                    this.accessOrder = object;
                }
            }
        }

        private void delete(File serializable) {
            if (!((File)serializable).delete()) {
                serializable = new IOException("FileClientSessionCache: Failed to delete " + serializable + ".");
                logger.log(Level.WARNING, ((Throwable)serializable).getMessage(), (Throwable)serializable);
            }
            --this.size;
        }

        static void logWriteError(String string, File file, Throwable throwable) {
            logger.log(Level.WARNING, "FileClientSessionCache: Error writing session data for " + string + " to " + file + ".", throwable);
        }
    }
}


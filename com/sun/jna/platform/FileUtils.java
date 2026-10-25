/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform;

import com.sun.jna.platform.mac.MacFileUtils;
import com.sun.jna.platform.win32.W32FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public abstract class FileUtils {
    public boolean hasTrash() {
        return false;
    }

    public abstract void moveToTrash(File ... var1) throws IOException;

    public static FileUtils getInstance() {
        return Holder.INSTANCE;
    }

    static class DefaultFileUtils
    extends FileUtils {
        private DefaultFileUtils() {
        }

        private File getTrashDirectory() {
            File file = new File(System.getProperty("user.home"));
            File file2 = new File(file, ".Trash");
            if (!(file2.exists() || (file2 = new File(file, "Trash")).exists() || !(file = new File(file, "Desktop")).exists() || (file2 = new File(file, ".Trash")).exists() || (file2 = new File(file, "Trash")).exists())) {
                file2 = new File(System.getProperty("fileutils.trash", "Trash"));
            }
            return file2;
        }

        @Override
        public boolean hasTrash() {
            return this.getTrashDirectory().exists();
        }

        @Override
        public void moveToTrash(File ... fileArray) throws IOException {
            File file = this.getTrashDirectory();
            if (!file.exists()) {
                throw new IOException("No trash location found (define fileutils.trash to be the path to the trash)");
            }
            ArrayList<File> arrayList = new ArrayList<File>();
            for (int i2 = 0; i2 < fileArray.length; ++i2) {
                File file2 = fileArray[i2];
                File file3 = new File(file, file2.getName());
                if (file2.renameTo(file3)) continue;
                arrayList.add(file2);
            }
            if (arrayList.size() > 0) {
                throw new IOException("The following files could not be trashed: " + arrayList);
            }
        }
    }

    static class Holder {
        public static final FileUtils INSTANCE;

        private Holder() {
        }

        static {
            String string = System.getProperty("os.name");
            INSTANCE = string.startsWith("Windows") ? new W32FileUtils() : (string.startsWith("Mac") ? new MacFileUtils() : new DefaultFileUtils());
        }
    }
}


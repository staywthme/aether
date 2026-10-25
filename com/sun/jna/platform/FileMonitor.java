/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform;

import com.sun.jna.platform.win32.W32FileMonitor;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class FileMonitor {
    public static final int FILE_CREATED = 1;
    public static final int FILE_DELETED = 2;
    public static final int FILE_MODIFIED = 4;
    public static final int FILE_ACCESSED = 8;
    public static final int FILE_NAME_CHANGED_OLD = 16;
    public static final int FILE_NAME_CHANGED_NEW = 32;
    public static final int FILE_RENAMED = 48;
    public static final int FILE_SIZE_CHANGED = 64;
    public static final int FILE_ATTRIBUTES_CHANGED = 128;
    public static final int FILE_SECURITY_CHANGED = 256;
    public static final int FILE_ANY = 511;
    private final Map<File, Integer> watched = new HashMap<File, Integer>();
    private List<FileListener> listeners = new ArrayList<FileListener>();

    protected abstract void watch(File var1, int var2, boolean var3) throws IOException;

    protected abstract void unwatch(File var1);

    public abstract void dispose();

    public void addWatch(File file) throws IOException {
        this.addWatch(file, 511);
    }

    public void addWatch(File file, int n2) throws IOException {
        this.addWatch(file, n2, file.isDirectory());
    }

    public void addWatch(File file, int n2, boolean bl) throws IOException {
        this.watched.put(file, n2);
        this.watch(file, n2, bl);
    }

    public void removeWatch(File file) {
        if (this.watched.remove(file) != null) {
            this.unwatch(file);
        }
    }

    protected void notify(FileEvent fileEvent) {
        for (FileListener fileListener : this.listeners) {
            fileListener.fileChanged(fileEvent);
        }
    }

    public synchronized void addFileListener(FileListener fileListener) {
        ArrayList<FileListener> arrayList = new ArrayList<FileListener>(this.listeners);
        arrayList.add(fileListener);
        this.listeners = arrayList;
    }

    public synchronized void removeFileListener(FileListener fileListener) {
        ArrayList<FileListener> arrayList = new ArrayList<FileListener>(this.listeners);
        arrayList.remove(fileListener);
        this.listeners = arrayList;
    }

    protected void finalize() {
        for (File file : this.watched.keySet()) {
            this.removeWatch(file);
        }
        this.dispose();
    }

    public static FileMonitor getInstance() {
        return Holder.INSTANCE;
    }

    static class Holder {
        public static final FileMonitor INSTANCE;

        private Holder() {
        }

        static {
            String string = System.getProperty("os.name");
            if (string.startsWith("Windows")) {
                INSTANCE = new W32FileMonitor();
                return;
            }
            throw new Error("FileMonitor not implemented for " + string);
        }
    }

    public class FileEvent
    extends EventObject {
        private final File file;
        private final int type;

        public FileEvent(File file, int n2) {
            super(FileMonitor.this);
            this.file = file;
            this.type = n2;
        }

        public File getFile() {
            return this.file;
        }

        public int getType() {
            return this.type;
        }

        @Override
        public String toString() {
            return "FileEvent: " + this.file + ":" + this.type;
        }
    }

    public static interface FileListener {
        public void fileChanged(FileEvent var1);
    }
}


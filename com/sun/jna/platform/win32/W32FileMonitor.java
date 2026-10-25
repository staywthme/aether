/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32;

import com.sun.jna.Structure;
import com.sun.jna.platform.FileMonitor;
import com.sun.jna.platform.win32.BaseTSD;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Kernel32Util;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class W32FileMonitor
extends FileMonitor {
    private static final Logger LOG = Logger.getLogger(W32FileMonitor.class.getName());
    private static final int BUFFER_SIZE = 4096;
    private Thread watcher;
    private WinNT.HANDLE port;
    private final Map<File, FileInfo> fileMap = new HashMap<File, FileInfo>();
    private final Map<WinNT.HANDLE, FileInfo> handleMap = new HashMap<WinNT.HANDLE, FileInfo>();
    private boolean disposing = false;
    private static int watcherThreadID;

    private void handleChanges(FileInfo fileInfo) throws IOException {
        Kernel32 kernel32 = Kernel32.INSTANCE;
        WinNT.FILE_NOTIFY_INFORMATION fILE_NOTIFY_INFORMATION = fileInfo.info;
        ((Structure)fILE_NOTIFY_INFORMATION).read();
        do {
            FileMonitor.FileEvent fileEvent = null;
            File file = new File(fileInfo.file, fILE_NOTIFY_INFORMATION.getFilename());
            switch (fILE_NOTIFY_INFORMATION.Action) {
                case 0: {
                    break;
                }
                case 3: {
                    fileEvent = new FileMonitor.FileEvent(file, 4);
                    break;
                }
                case 1: {
                    fileEvent = new FileMonitor.FileEvent(file, 1);
                    break;
                }
                case 2: {
                    fileEvent = new FileMonitor.FileEvent(file, 2);
                    break;
                }
                case 4: {
                    fileEvent = new FileMonitor.FileEvent(file, 16);
                    break;
                }
                case 5: {
                    fileEvent = new FileMonitor.FileEvent(file, 32);
                    break;
                }
                default: {
                    LOG.log(Level.WARNING, "Unrecognized file action ''{0}''", fILE_NOTIFY_INFORMATION.Action);
                }
            }
            if (fileEvent == null) continue;
            this.notify(fileEvent);
        } while ((fILE_NOTIFY_INFORMATION = fILE_NOTIFY_INFORMATION.next()) != null);
        if (!fileInfo.file.exists()) {
            this.unwatch(fileInfo.file);
            return;
        }
        if (!kernel32.ReadDirectoryChangesW(fileInfo.handle, fileInfo.info, fileInfo.info.size(), fileInfo.recursive, fileInfo.notifyMask, fileInfo.infoLength, fileInfo.overlapped, null) && !this.disposing) {
            int n2 = kernel32.GetLastError();
            throw new IOException("ReadDirectoryChangesW failed on " + fileInfo.file + ": '" + Kernel32Util.formatMessageFromLastErrorCode(n2) + "' (" + n2 + ")");
        }
    }

    private FileInfo waitForChange() {
        Object object = new IntByReference();
        BaseTSD.ULONG_PTRByReference uLONG_PTRByReference = new BaseTSD.ULONG_PTRByReference();
        PointerByReference pointerByReference = new PointerByReference();
        if (!Kernel32.INSTANCE.GetQueuedCompletionStatus(this.port, (IntByReference)object, uLONG_PTRByReference, pointerByReference, -1)) {
            return null;
        }
        object = this;
        synchronized (object) {
            return this.handleMap.get(new WinNT.HANDLE(uLONG_PTRByReference.getValue().toPointer()));
        }
    }

    private int convertMask(int n2) {
        int n3 = 0;
        if ((n2 & 1) != 0) {
            n3 = 64;
        }
        if ((n2 & 2) != 0) {
            n3 |= 3;
        }
        if ((n2 & 4) != 0) {
            n3 |= 0x10;
        }
        if ((n2 & 0x30) != 0) {
            n3 |= 3;
        }
        if ((n2 & 0x40) != 0) {
            n3 |= 8;
        }
        if ((n2 & 8) != 0) {
            n3 |= 0x20;
        }
        if ((n2 & 0x80) != 0) {
            n3 |= 4;
        }
        if ((n2 & 0x100) != 0) {
            n3 |= 0x100;
        }
        return n3;
    }

    @Override
    public synchronized void watch(File file, int n2, boolean bl) throws IOException {
        Object object = file;
        if (!((File)object).isDirectory()) {
            bl = false;
            object = file.getParentFile();
        }
        while (object != null && !((File)object).exists()) {
            bl = true;
            object = ((File)object).getParentFile();
        }
        if (object == null) {
            throw new FileNotFoundException("No ancestor found for " + file);
        }
        object = Kernel32.INSTANCE;
        WinNT.HANDLE hANDLE = object.CreateFile(file.getAbsolutePath(), 1, 7, null, 3, 0x42000000, null);
        if (WinBase.INVALID_HANDLE_VALUE.equals(hANDLE)) {
            throw new IOException("Unable to open " + file + " (" + object.GetLastError() + ")");
        }
        n2 = this.convertMask(n2);
        FileInfo fileInfo = new FileInfo(file, hANDLE, n2, bl);
        this.fileMap.put(file, fileInfo);
        this.handleMap.put(hANDLE, fileInfo);
        this.port = object.CreateIoCompletionPort(hANDLE, this.port, hANDLE.getPointer(), 0);
        if (WinBase.INVALID_HANDLE_VALUE.equals(this.port)) {
            throw new IOException("Unable to create/use I/O Completion port for " + file + " (" + object.GetLastError() + ")");
        }
        if (!object.ReadDirectoryChangesW(hANDLE, fileInfo.info, fileInfo.info.size(), bl, n2, fileInfo.infoLength, fileInfo.overlapped, null)) {
            int n3 = object.GetLastError();
            throw new IOException("ReadDirectoryChangesW failed on " + fileInfo.file + ", handle " + hANDLE + ": '" + Kernel32Util.formatMessageFromLastErrorCode(n3) + "' (" + n3 + ")");
        }
        if (this.watcher == null) {
            this.watcher = new Thread("W32 File Monitor-" + watcherThreadID++){

                @Override
                public void run() {
                    while (true) {
                        Object object;
                        if ((object = W32FileMonitor.this.waitForChange()) == null) {
                            object = W32FileMonitor.this;
                            synchronized (object) {
                                if (W32FileMonitor.this.fileMap.isEmpty()) {
                                    W32FileMonitor.this.watcher = null;
                                    return;
                                }
                            }
                        }
                        try {
                            W32FileMonitor.this.handleChanges((FileInfo)object);
                            continue;
                        }
                        catch (IOException iOException) {
                            object = iOException;
                            iOException.printStackTrace();
                            continue;
                        }
                        break;
                    }
                }
            };
            this.watcher.setDaemon(true);
            this.watcher.start();
        }
    }

    @Override
    public synchronized void unwatch(File object) {
        if ((object = this.fileMap.remove(object)) != null) {
            this.handleMap.remove(((FileInfo)object).handle);
            Kernel32 kernel32 = Kernel32.INSTANCE;
            kernel32.CloseHandle(((FileInfo)object).handle);
        }
    }

    @Override
    public synchronized void dispose() {
        this.disposing = true;
        int n2 = 0;
        Object object = this.fileMap.keySet().toArray();
        while (!this.fileMap.isEmpty()) {
            this.unwatch((File)object[n2++]);
        }
        object = Kernel32.INSTANCE;
        object.PostQueuedCompletionStatus(this.port, 0, null, null);
        object.CloseHandle(this.port);
        this.port = null;
        this.watcher = null;
    }

    class FileInfo {
        public final File file;
        public final WinNT.HANDLE handle;
        public final int notifyMask;
        public final boolean recursive;
        public final WinNT.FILE_NOTIFY_INFORMATION info = new WinNT.FILE_NOTIFY_INFORMATION(4096);
        public final IntByReference infoLength = new IntByReference();
        public final WinBase.OVERLAPPED overlapped = new WinBase.OVERLAPPED();

        public FileInfo(File file, WinNT.HANDLE hANDLE, int n2, boolean bl) {
            this.file = file;
            this.handle = hANDLE;
            this.notifyMask = n2;
            this.recursive = bl;
        }
    }
}


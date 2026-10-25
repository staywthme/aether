/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.mac;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Structure;
import com.sun.jna.platform.FileUtils;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.PointerByReference;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MacFileUtils
extends FileUtils {
    @Override
    public boolean hasTrash() {
        return true;
    }

    @Override
    public void moveToTrash(File ... fileArray) throws IOException {
        ArrayList<String> arrayList = new ArrayList<String>();
        for (File file : fileArray) {
            FileManager.FSRef fSRef = new FileManager.FSRef();
            int n2 = FileManager.INSTANCE.FSPathMakeRefWithOptions(file.getAbsolutePath(), 1, fSRef, null);
            if (n2 != 0) {
                arrayList.add(file + " (FSRef: " + n2 + ")");
                continue;
            }
            n2 = FileManager.INSTANCE.FSMoveObjectToTrashSync(fSRef, null, 0);
            if (n2 == 0) continue;
            arrayList.add(file + " (" + n2 + ")");
        }
        if (arrayList.size() > 0) {
            throw new IOException("The following files could not be trashed: " + arrayList);
        }
    }

    public static interface FileManager
    extends Library {
        public static final FileManager INSTANCE = Native.load("CoreServices", FileManager.class);
        public static final int kFSFileOperationDefaultOptions = 0;
        public static final int kFSFileOperationsOverwrite = 1;
        public static final int kFSFileOperationsSkipSourcePermissionErrors = 2;
        public static final int kFSFileOperationsDoNotMoveAcrossVolumes = 4;
        public static final int kFSFileOperationsSkipPreflight = 8;
        public static final int kFSPathDefaultOptions = 0;
        public static final int kFSPathMakeRefDoNotFollowLeafSymlink = 1;

        public int FSRefMakePath(FSRef var1, byte[] var2, int var3);

        public int FSPathMakeRef(String var1, int var2, ByteByReference var3);

        public int FSPathMakeRefWithOptions(String var1, int var2, FSRef var3, ByteByReference var4);

        public int FSPathMoveObjectToTrashSync(String var1, PointerByReference var2, int var3);

        public int FSMoveObjectToTrashSync(FSRef var1, FSRef var2, int var3);

        @Structure.FieldOrder(value={"hidden"})
        public static class FSRef
        extends Structure {
            public byte[] hidden = new byte[80];
        }
    }
}


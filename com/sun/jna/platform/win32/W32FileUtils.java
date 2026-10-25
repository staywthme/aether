/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32;

import com.sun.jna.platform.FileUtils;
import com.sun.jna.platform.win32.Kernel32Util;
import com.sun.jna.platform.win32.Shell32;
import com.sun.jna.platform.win32.ShellAPI;
import java.io.File;
import java.io.IOException;

public class W32FileUtils
extends FileUtils {
    @Override
    public boolean hasTrash() {
        return true;
    }

    @Override
    public void moveToTrash(File ... fileArray) throws IOException {
        int n2;
        Shell32 shell32 = Shell32.INSTANCE;
        ShellAPI.SHFILEOPSTRUCT sHFILEOPSTRUCT = new ShellAPI.SHFILEOPSTRUCT();
        new ShellAPI.SHFILEOPSTRUCT().wFunc = 3;
        String[] stringArray = new String[fileArray.length];
        for (n2 = 0; n2 < stringArray.length; ++n2) {
            stringArray[n2] = fileArray[n2].getAbsolutePath();
        }
        sHFILEOPSTRUCT.pFrom = sHFILEOPSTRUCT.encodePaths(stringArray);
        sHFILEOPSTRUCT.fFlags = (short)1620;
        n2 = shell32.SHFileOperation(sHFILEOPSTRUCT);
        if (n2 != 0) {
            throw new IOException("Move to trash failed: " + sHFILEOPSTRUCT.pFrom + ": " + Kernel32Util.formatMessageFromLastErrorCode(n2));
        }
        if (sHFILEOPSTRUCT.fAnyOperationsAborted) {
            throw new IOException("Move to trash aborted");
        }
    }
}


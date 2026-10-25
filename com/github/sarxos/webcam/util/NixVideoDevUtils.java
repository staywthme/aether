/*
 * Decompiled with CFR 0.152.
 */
package com.github.sarxos.webcam.util;

import java.io.File;
import java.io.FilenameFilter;

public class NixVideoDevUtils
implements FilenameFilter {
    private static final File DEV = new File("/dev");

    @Override
    public boolean accept(File file, String string) {
        return file.getName().equals("dev") && string.startsWith("video") && string.length() > 5 && Character.isDigit(string.charAt(5));
    }

    public static File[] getVideoFiles() {
        String[] stringArray = DEV.list(new NixVideoDevUtils());
        File[] fileArray = new File[stringArray.length];
        for (int i2 = 0; i2 < stringArray.length; ++i2) {
            fileArray[i2] = new File(DEV, stringArray[i2]);
        }
        return fileArray;
    }
}


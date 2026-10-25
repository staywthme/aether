/*
 * Decompiled with CFR 0.152.
 */
package org.sqlite.util;

import org.sqlite.SQLiteJDBCLoader;
import org.sqlite.util.OSInfo;

public class LibraryLoaderUtil {
    public static final String NATIVE_LIB_BASE_NAME = "sqlitejdbc";

    public static String getNativeLibResourcePath() {
        String string = SQLiteJDBCLoader.class.getPackage().getName().replace(".", "/");
        return String.format("/%s/native/%s", string, OSInfo.getNativeLibFolderPathForCurrentOS());
    }

    public static String getNativeLibName() {
        return System.mapLibraryName(NATIVE_LIB_BASE_NAME);
    }

    public static boolean hasNativeLib(String string, String string2) {
        return SQLiteJDBCLoader.class.getResource(string + "/" + string2) != null;
    }
}


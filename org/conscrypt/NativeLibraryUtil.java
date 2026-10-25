/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

final class NativeLibraryUtil {
    public static void loadLibrary(String string, boolean bl) {
        if (bl) {
            System.load(string);
            return;
        }
        System.loadLibrary(string);
    }

    private NativeLibraryUtil() {
    }
}


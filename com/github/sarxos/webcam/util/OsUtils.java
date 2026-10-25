/*
 * Decompiled with CFR 0.152.
 */
package com.github.sarxos.webcam.util;

public enum OsUtils {
    WIN,
    NIX,
    OSX;

    private static OsUtils os;

    public static final OsUtils getOS() {
        if (os == null) {
            String string = System.getProperty("os.name").toLowerCase();
            if (string.indexOf("win") >= 0) {
                os = WIN;
            } else if (string.indexOf("mac") >= 0) {
                os = OSX;
            } else if (string.indexOf("nix") >= 0 || string.indexOf("nux") >= 0) {
                os = NIX;
            } else {
                throw new RuntimeException(string + " is not supported");
            }
        }
        return os;
    }

    static {
        os = null;
    }
}


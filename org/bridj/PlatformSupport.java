/*
 * Decompiled with CFR 0.152.
 */
package org.bridj;

import java.io.IOException;
import org.bridj.NativeLibrary;
import org.bridj.Platform;
import org.bridj.util.ClassDefiner;

class PlatformSupport {
    private static final PlatformSupport instance;

    PlatformSupport() {
    }

    public ClassDefiner getClassDefiner(ClassDefiner classDefiner, ClassLoader classLoader) {
        return classDefiner;
    }

    public static PlatformSupport getInstance() {
        return instance;
    }

    public NativeLibrary loadNativeLibrary(String string) throws IOException {
        return null;
    }

    static {
        PlatformSupport platformSupport = null;
        if (Platform.isAndroid()) {
            try {
                platformSupport = (PlatformSupport)Class.forName("org.bridj.AndroidSupport").newInstance();
            }
            catch (Exception exception) {
                throw new RuntimeException("Failed to instantiate the Android support class... Was the BridJ jar tampered with / trimmed too much ?", exception);
            }
        }
        if (platformSupport == null) {
            platformSupport = new PlatformSupport();
        }
        instance = platformSupport;
    }
}


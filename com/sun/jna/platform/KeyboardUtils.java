/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform;

import com.sun.jna.Platform;
import com.sun.jna.platform.unix.X11;
import com.sun.jna.platform.win32.User32;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;

public class KeyboardUtils {
    static final NativeKeyboardUtils INSTANCE;

    public static boolean isPressed(int n2, int n3) {
        return INSTANCE.isPressed(n2, n3);
    }

    public static boolean isPressed(int n2) {
        return INSTANCE.isPressed(n2);
    }

    static {
        if (GraphicsEnvironment.isHeadless()) {
            throw new HeadlessException("KeyboardUtils requires a keyboard");
        }
        if (Platform.isWindows()) {
            INSTANCE = new W32KeyboardUtils();
        } else {
            if (Platform.isMac()) {
                INSTANCE = new MacKeyboardUtils();
                throw new UnsupportedOperationException("No support (yet) for " + System.getProperty("os.name"));
            }
            INSTANCE = new X11KeyboardUtils();
        }
    }

    static class X11KeyboardUtils
    extends NativeKeyboardUtils {
        private X11KeyboardUtils() {
        }

        private int toKeySym(int n2, int n3) {
            if (n2 >= 65 && n2 <= 90) {
                return 97 + (n2 - 65);
            }
            if (n2 >= 48 && n2 <= 57) {
                return 48 + (n2 - 48);
            }
            if (n2 == 16) {
                if ((n3 & 3) != 0) {
                    return 65506;
                }
                return 65505;
            }
            if (n2 == 17) {
                if ((n3 & 3) != 0) {
                    return 65508;
                }
                return 65507;
            }
            if (n2 == 18) {
                if ((n3 & 3) != 0) {
                    return 65514;
                }
                return 65513;
            }
            if (n2 == 157) {
                if ((n3 & 3) != 0) {
                    return 65512;
                }
                return 65511;
            }
            return 0;
        }

        @Override
        public boolean isPressed(int n2, int n3) {
            X11 x11 = X11.INSTANCE;
            X11.Display display = x11.XOpenDisplay(null);
            if (display == null) {
                throw new Error("Can't open X Display");
            }
            try {
                byte[] byArray = new byte[32];
                x11.XQueryKeymap(display, byArray);
                n2 = this.toKeySym(n2, n3);
                for (n3 = 5; n3 < 256; ++n3) {
                    int n4 = n3 / 8;
                    int n5 = n3 % 8;
                    if ((byArray[n4] & 1 << n5) == 0 || (n4 = x11.XKeycodeToKeysym(display, (byte)n3, 0).intValue()) != n2) continue;
                    x11.XCloseDisplay(display);
                }
            }
            catch (Throwable throwable) {
                x11.XCloseDisplay(display);
                throw throwable;
            }
            {
                return true;
            }
            x11.XCloseDisplay(display);
            return false;
        }
    }

    static class MacKeyboardUtils
    extends NativeKeyboardUtils {
        private MacKeyboardUtils() {
        }

        @Override
        public boolean isPressed(int n2, int n3) {
            return false;
        }
    }

    static class W32KeyboardUtils
    extends NativeKeyboardUtils {
        private W32KeyboardUtils() {
        }

        private int toNative(int n2, int n3) {
            if (n2 >= 65 && n2 <= 90 || n2 >= 48 && n2 <= 57) {
                return n2;
            }
            if (n2 == 16) {
                if ((n3 & 3) != 0) {
                    return 161;
                }
                if ((n3 & 2) != 0) {
                    return 160;
                }
                return 16;
            }
            if (n2 == 17) {
                if ((n3 & 3) != 0) {
                    return 163;
                }
                if ((n3 & 2) != 0) {
                    return 162;
                }
                return 17;
            }
            if (n2 == 18) {
                if ((n3 & 3) != 0) {
                    return 165;
                }
                if ((n3 & 2) != 0) {
                    return 164;
                }
                return 18;
            }
            return 0;
        }

        @Override
        public boolean isPressed(int n2, int n3) {
            User32 user32 = User32.INSTANCE;
            return (user32.GetAsyncKeyState(this.toNative(n2, n3)) & 0x8000) != 0;
        }
    }

    static abstract class NativeKeyboardUtils {
        private NativeKeyboardUtils() {
        }

        public abstract boolean isPressed(int var1, int var2);

        public boolean isPressed(int n2) {
            return this.isPressed(n2, 0);
        }
    }
}


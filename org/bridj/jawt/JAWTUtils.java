/*
 * Decompiled with CFR 0.152.
 */
package org.bridj.jawt;

import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import org.bridj.JNI;
import org.bridj.NativeObject;
import org.bridj.Platform;
import org.bridj.Pointer;
import org.bridj.jawt.JAWT;
import org.bridj.jawt.JAWT_DrawingSurface;
import org.bridj.jawt.JAWT_DrawingSurfaceInfo;
import org.bridj.jawt.JawtLibrary;

public class JAWTUtils {
    public static JawtLibrary.JNIEnv getJNIEnv() {
        return new JawtLibrary.JNIEnv(JNI.getEnv());
    }

    public static JAWT getJAWT(JawtLibrary.JNIEnv jNIEnv) {
        if (GraphicsEnvironment.isHeadless()) {
            throw new HeadlessException("No native peers in headless mode.");
        }
        Object object = new JAWT().version(65540);
        if (!JawtLibrary.JAWT_GetAWT(jNIEnv, (Pointer<JAWT>)(object = Pointer.getPointer(object)))) {
            throw new RuntimeException("Failed to get JAWT pointer !");
        }
        return (JAWT)((Pointer)object).get();
    }

    public static void withLockedSurface(JawtLibrary.JNIEnv pointer, JAWT jAWT, Component component, LockedComponentRunnable lockedComponentRunnable) {
        if (component.isLightweight()) {
            throw new IllegalArgumentException("Lightweight components do not have native peers.");
        }
        if (!component.isDisplayable()) {
            throw new IllegalArgumentException("Component that are not displayable do not have native peers.");
        }
        Object object = JNI.getGlobalPointer(component);
        pointer = jAWT.GetDrawingSurface().get().invoke(pointer, (Pointer<?>)object).as(JAWT_DrawingSurface.class);
        if (pointer == null) {
            throw new RuntimeException("Cannot get drawing surface from " + component);
        }
        object = (JAWT_DrawingSurface)pointer.get();
        try {
            int n2 = ((JAWT_DrawingSurface)object).Lock().get().invoke(pointer);
            if ((n2 & 1) != 0) {
                throw new RuntimeException("Cannot lock drawing surface of " + component);
            }
            try {
                Pointer<NativeObject> pointer2 = ((JAWT_DrawingSurface)object).GetDrawingSurfaceInfo().as(JAWT_DrawingSurface.GetDrawingSurfaceInfo_callback.class);
                pointer2 = pointer2.get().invoke(pointer);
                if (pointer2 != null) {
                    pointer2 = pointer2.as(JAWT_DrawingSurfaceInfo.class);
                }
                pointer2 = ((JAWT_DrawingSurfaceInfo)pointer2.get()).platformInfo();
                long l2 = Platform.isMacOSX() ? pointer2.getPeer() : pointer2.getSizeT();
                lockedComponentRunnable.run(component, l2);
            }
            finally {
                ((JAWT_DrawingSurface)object).Unlock().get().invoke(pointer);
            }
            return;
        }
        finally {
            jAWT.FreeDrawingSurface().get().invoke(pointer);
        }
    }

    public static long getNativePeerHandle(Component component) {
        try {
            JawtLibrary.JNIEnv jNIEnv = JAWTUtils.getJNIEnv();
            JAWT jAWT = JAWTUtils.getJAWT(jNIEnv);
            final long[] lArray = new long[1];
            JAWTUtils.withLockedSurface(jNIEnv, jAWT, component, new LockedComponentRunnable(){

                public final void run(Component component, long l2) {
                    lArray[0] = l2;
                }
            });
            return lArray[0];
        }
        catch (Throwable throwable) {
            Throwable throwable2 = throwable;
            throwable.printStackTrace();
            return 0L;
        }
    }

    public static interface LockedComponentRunnable {
        public void run(Component var1, long var2);
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package org.bridj.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.bridj.BridJ;
import org.bridj.Platform;
import org.bridj.ann.Convention;
import org.bridj.ann.Library;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class ProcessUtils {
    public static int getCurrentProcessId() {
        if (Platform.isWindows()) {
            BridJ.register(Kernel32.class);
            return Kernel32.GetCurrentProcessId();
        }
        BridJ.register(LibC.class);
        return LibC.getpid();
    }

    public static String[] computeJavaProcessArgs(Class<?> object, List<?> list22) {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add(new File(new File(System.getProperty("java.home")), "bin" + File.separator + "java").toString());
        arrayList.add("-cp");
        arrayList.add(System.getProperty("java.class.path"));
        arrayList.add(((Class)((Object)object)).getName());
        for (Object object2 : list22) {
            arrayList.add(object2.toString());
        }
        ArrayList<String> arrayList2 = arrayList;
        return arrayList2.toArray(new String[arrayList2.size()]);
    }

    public static Process startJavaProcess(Class<?> clazz, List<?> list2) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(new String[0]);
        processBuilder.command(ProcessUtils.computeJavaProcessArgs(clazz, list2));
        processBuilder.redirectErrorStream(true);
        return processBuilder.start();
    }

    @Library(value="c")
    static class LibC {
        LibC() {
        }

        public static native int getpid();
    }

    @Library(value="kernel32")
    @Convention(value=Convention.Style.StdCall)
    static class Kernel32 {
        Kernel32() {
        }

        public static native int GetCurrentProcessId();
    }
}


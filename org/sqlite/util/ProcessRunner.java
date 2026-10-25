/*
 * Decompiled with CFR 0.152.
 */
package org.sqlite.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

public class ProcessRunner {
    String runAndWaitFor(String object) throws IOException, InterruptedException {
        object = Runtime.getRuntime().exec((String)object);
        ((Process)object).waitFor();
        return ProcessRunner.getProcessOutput((Process)object);
    }

    String runAndWaitFor(String object, long l2, TimeUnit timeUnit) throws IOException, InterruptedException {
        object = Runtime.getRuntime().exec((String)object);
        ((Process)object).waitFor(l2, timeUnit);
        return ProcessRunner.getProcessOutput((Process)object);
    }

    static String getProcessOutput(Process object) throws IOException {
        object = ((Process)object).getInputStream();
        try {
            int n2;
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] byArray = new byte[32];
            while ((n2 = ((InputStream)object).read(byArray, 0, byArray.length)) >= 0) {
                byteArrayOutputStream.write(byArray, 0, n2);
            }
            String string = byteArrayOutputStream.toString();
            return string;
        }
        finally {
            if (object != null) {
                ((InputStream)object).close();
            }
        }
    }
}


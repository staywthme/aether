/*
 * Decompiled with CFR 0.152.
 */
package com.github.sarxos.webcam;

import com.github.sarxos.webcam.WebcamDriver;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebcamDriverUtils {
    private static final Logger LOG = LoggerFactory.getLogger(WebcamDriverUtils.class);

    private WebcamDriverUtils() {
    }

    protected static WebcamDriver findDriver(List<String> object, List<Class<?>> list2) {
        object = object.iterator();
        while (object.hasNext()) {
            String string = (String)object.next();
            LOG.info("Searching driver {}", (Object)string);
            Class<?> clazz = null;
            for (Class<?> clazz2 : list2) {
                if (!clazz2.getCanonicalName().equals(string)) continue;
                clazz = clazz2;
                break;
            }
            if (clazz == null) {
                try {
                    clazz = Class.forName(string);
                }
                catch (ClassNotFoundException classNotFoundException) {
                    LOG.trace("Class not found {}, fall thru", (Object)string);
                }
            }
            if (clazz == null) {
                LOG.debug("Driver {} not found", (Object)string);
                continue;
            }
            LOG.info("Webcam driver {} has been found", (Object)string);
            try {
                return (WebcamDriver)clazz.newInstance();
            }
            catch (InstantiationException instantiationException) {
                throw new RuntimeException(instantiationException);
            }
            catch (IllegalAccessException illegalAccessException) {
                throw new RuntimeException(illegalAccessException);
            }
        }
        return null;
    }

    protected static Class<?>[] getClasses(String string, boolean bl) {
        ArrayList<File> serializable2 = new ArrayList<File>();
        ArrayList arrayList = new ArrayList();
        Object object = Thread.currentThread().getContextClassLoader();
        String string2 = string.replace('.', '/');
        try {
            object = ((ClassLoader)object).getResources(string2);
        }
        catch (IOException iOException) {
            throw new RuntimeException("Cannot read path " + string2, iOException);
        }
        while (object.hasMoreElements()) {
            URL uRL = (URL)object.nextElement();
            serializable2.add(new File(uRL.getFile()));
        }
        for (File file : serializable2) {
            try {
                arrayList.addAll(WebcamDriverUtils.findClasses(file, string, bl));
            }
            catch (ClassNotFoundException classNotFoundException) {
                throw new RuntimeException("Class not found", classNotFoundException);
            }
        }
        ArrayList arrayList2 = arrayList;
        return arrayList2.toArray(new Class[arrayList2.size()]);
    }

    private static List<Class<?>> findClasses(File fileArray, String string, boolean bl) throws ClassNotFoundException {
        ArrayList arrayList = new ArrayList();
        if (!fileArray.exists()) {
            return arrayList;
        }
        File[] fileArray2 = fileArray.listFiles();
        fileArray = fileArray2;
        fileArray = fileArray2;
        int n2 = fileArray2.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            File file = fileArray[i2];
            if (file.isDirectory() && !bl) {
                arrayList.addAll(WebcamDriverUtils.findClasses(file, string + "." + file.getName(), bl));
                continue;
            }
            if (!file.getName().endsWith(".class")) continue;
            arrayList.add(Class.forName(string + '.' + file.getName().substring(0, file.getName().length() - 6)));
        }
        return arrayList;
    }
}


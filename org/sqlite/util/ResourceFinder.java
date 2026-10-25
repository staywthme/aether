/*
 * Decompiled with CFR 0.152.
 */
package org.sqlite.util;

import java.net.URL;

public class ResourceFinder {
    public static URL find(Class<?> clazz, String string) {
        return ResourceFinder.find(clazz.getClassLoader(), clazz.getPackage(), string);
    }

    public static URL find(ClassLoader classLoader, Package package_, String string) {
        return ResourceFinder.find(classLoader, package_.getName(), string);
    }

    public static URL find(ClassLoader classLoader, String string, String string2) {
        string = ResourceFinder.packagePath(string);
        if (!(string = string + string2).startsWith("/")) {
            string = "/" + string;
        }
        return classLoader.getResource(string);
    }

    private static String packagePath(Class<?> clazz) {
        return ResourceFinder.packagePath(clazz.getPackage());
    }

    private static String packagePath(Package package_) {
        return ResourceFinder.packagePath(package_.getName());
    }

    private static String packagePath(String string) {
        if ((string = string.replaceAll("\\.", "/")).endsWith("/")) {
            return string;
        }
        return string + "/";
    }
}


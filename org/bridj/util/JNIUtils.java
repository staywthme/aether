/*
 * Decompiled with CFR 0.152.
 */
package org.bridj.util;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import org.bridj.BridJ;
import org.bridj.Platform;
import org.bridj.util.BytecodeAnalyzer;
import org.bridj.util.StringUtils;

public class JNIUtils {
    private static Map<String, NativeMethodsCache> nativeMethodsCache = new WeakHashMap<String, NativeMethodsCache>();
    private static final String bridjPackage = BridJ.class.getPackage() == null ? "org.bridj" : BridJ.class.getPackage().getName();
    private static final String bridjNormalPackagePrefix = bridjPackage.endsWith("v0_7_0") ? bridjPackage.substring(0, bridjPackage.length() - 6) : bridjPackage + ".";
    private static final String bridjVersionSpecificPackagePrefix = bridjPackage + ".";

    private static synchronized NativeMethodsCache getNativeMethodsCache(String string) throws IOException {
        NativeMethodsCache nativeMethodsCache = JNIUtils.nativeMethodsCache.get(string);
        if (nativeMethodsCache == null) {
            nativeMethodsCache = new NativeMethodsCache(string);
            JNIUtils.nativeMethodsCache.put(string, nativeMethodsCache);
        }
        return nativeMethodsCache;
    }

    static int findLastNonEscapeUnderscore(String string) {
        int n2;
        int n3 = n2 = string.length();
        do {
            if ((n3 = string.lastIndexOf("_", n3 - 1)) < 0 || n3 != n2 - 1 && Character.isDigit(string.charAt(n3 + 1))) continue;
            return n3;
        } while (n3 > 0);
        return -1;
    }

    public static String decodeVersionSpecificMethodNameClassAndSignature(String string, Object[] objectArray) throws NoSuchMethodException, IOException {
        return JNIUtils.decodeMethodNameClassAndSignature(string, objectArray, bridjNormalPackagePrefix, bridjVersionSpecificPackagePrefix);
    }

    static String decodeMethodNameClassAndSignature(String string, Object[] objectArray, String object, String string2) throws NoSuchMethodException, IOException {
        if (string.startsWith("_")) {
            string = string.substring(1);
        }
        if (string.startsWith("Java_")) {
            string = string.substring(5);
        }
        int n2 = JNIUtils.findLastNonEscapeUnderscore(string);
        String string3 = string.substring(0, n2).replace('_', '.');
        if (object != null && string3.startsWith((String)object) && !string3.startsWith(string2)) {
            string3 = string2 + string3.substring(((String)object).length());
        }
        string = string.substring(n2 + 1).replaceAll("_1", "_");
        object = JNIUtils.getNativeMethodsCache(string3.replace('.', '/'));
        string2 = ((NativeMethodsCache)object).get(string);
        if (string2 == null) {
            throw new NoSuchMethodException("Method " + string + " not found in class " + string3 + " : known method names = " + StringUtils.implode(((NativeMethodsCache)object).getNames(), (Object)", "));
        }
        objectArray[0] = string;
        objectArray[1] = string2;
        string = string3.replace('.', '/');
        return string;
    }

    public static String getNativeName(Class clazz) {
        return clazz.getName().replace('.', '/');
    }

    public static String getNativeSignature(Method method) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append('(');
        for (Class<?> clazz : method.getParameterTypes()) {
            stringBuffer.append(JNIUtils.getNativeSignature(clazz));
        }
        stringBuffer.append(')');
        stringBuffer.append(JNIUtils.getNativeSignature(method.getReturnType()));
        return stringBuffer.toString();
    }

    public static String getNativeSignature(Class clazz) {
        if (clazz.isPrimitive()) {
            if (clazz == Integer.TYPE) {
                return "I";
            }
            if (clazz == Long.TYPE) {
                return "J";
            }
            if (clazz == Short.TYPE) {
                return "S";
            }
            if (clazz == Byte.TYPE) {
                return "B";
            }
            if (clazz == Boolean.TYPE) {
                return "Z";
            }
            if (clazz == Double.TYPE) {
                return "D";
            }
            if (clazz == Float.TYPE) {
                return "F";
            }
            if (clazz == Character.TYPE) {
                return "C";
            }
            if (clazz == Void.TYPE) {
                return "V";
            }
            throw new RuntimeException("unexpected case");
        }
        if (clazz.isArray()) {
            return "[" + JNIUtils.getNativeSignature(clazz.getComponentType());
        }
        return "L" + JNIUtils.getNativeName(clazz) + ";";
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    static class NativeMethodsCache {
        Map<String, String> signatures = new HashMap<String, String>();

        public NativeMethodsCache(String object) throws IOException {
            for (String[] stringArray : BytecodeAnalyzer.getNativeMethodSignatures((String)object, Platform.getClassLoader())) {
                this.signatures.put(stringArray[1], stringArray[2]);
            }
        }

        public String get(String string) {
            return this.signatures.get(string);
        }

        public Set<String> getNames() {
            return this.signatures.keySet();
        }
    }
}


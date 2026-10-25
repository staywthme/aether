/*
 * Decompiled with CFR 0.152.
 */
package org.bridj.util;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.bridj.Platform;
import org.bridj.relocated.org.objectweb.asm.AnnotationVisitor;
import org.bridj.relocated.org.objectweb.asm.Attribute;
import org.bridj.relocated.org.objectweb.asm.ClassReader;
import org.bridj.relocated.org.objectweb.asm.ClassVisitor;
import org.bridj.relocated.org.objectweb.asm.FieldVisitor;
import org.bridj.relocated.org.objectweb.asm.MethodVisitor;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public final class BytecodeAnalyzer {
    private BytecodeAnalyzer() {
    }

    public static List<String[]> getNativeMethodSignatures(Class clazz) throws IOException {
        return BytecodeAnalyzer.getNativeMethodSignatures(BytecodeAnalyzer.getInternalName(clazz), Platform.getClassLoader(clazz));
    }

    public static List<String[]> getNativeMethodSignatures(String string, ClassLoader classLoader) throws IOException {
        return BytecodeAnalyzer.getNativeMethodSignatures(string, classLoader, new ArrayList<String[]>());
    }

    private static List<String[]> getNativeMethodSignatures(final String string, ClassLoader classLoader, final List<String[]> list2) throws IOException {
        ClassReader classReader = new ClassReader(BytecodeAnalyzer.readByteCode(string, classLoader));
        String string2 = classReader.getSuperName();
        if (string2 != null && !string2.equals("java/lang/Object")) {
            BytecodeAnalyzer.getNativeMethodSignatures(string2, classLoader, list2);
        }
        classReader.accept(new EmptyVisitor(){

            public final MethodVisitor visitMethod(int n2, String string4, String string2, String string3, String[] stringArray) {
                if (Modifier.isNative(n2)) {
                    list2.add(new String[]{string, string4, string2});
                }
                return null;
            }
        }, 7);
        return list2;
    }

    private static List<String> getFieldNames(String string, String string2, ClassLoader classLoader, final List<String> list2) throws IOException {
        ClassReader classReader = new ClassReader(BytecodeAnalyzer.readByteCode(string, classLoader));
        String string3 = classReader.getSuperName();
        if (string3 != null && !string3.equals("java/lang/Object") && !string2.equals(string)) {
            BytecodeAnalyzer.getFieldNames(string3, string2, classLoader, list2);
        }
        classReader.accept(new EmptyVisitor(){

            public final FieldVisitor visitField(int n2, String string, String string2, String string3, Object object) {
                list2.add(string);
                return null;
            }
        }, 7);
        return list2;
    }

    private static List<String> getMethodNames(String string, String string2, ClassLoader classLoader, final List<String> list2) throws IOException {
        ClassReader classReader = new ClassReader(BytecodeAnalyzer.readByteCode(string, classLoader));
        String string3 = classReader.getSuperName();
        if (string3 != null && !string3.equals("java/lang/Object") && !string2.equals(string)) {
            BytecodeAnalyzer.getMethodNames(string3, string2, classLoader, list2);
        }
        classReader.accept(new EmptyVisitor(){

            public final MethodVisitor visitMethod(int n2, String string, String string2, String string3, String[] stringArray) {
                list2.add(string);
                return null;
            }
        }, 7);
        return list2;
    }

    static String getInternalName(Class clazz) {
        return clazz.getName().replace('.', '/');
    }

    static URL getClassResource(Class clazz) throws FileNotFoundException {
        return BytecodeAnalyzer.getClassResource(BytecodeAnalyzer.getInternalName(clazz), Platform.getClassLoader(clazz));
    }

    static URL getClassResource(String string, ClassLoader object) throws FileNotFoundException {
        if ((object = ((ClassLoader)object).getResource(string = string + ".class")) == null) {
            throw new FileNotFoundException("Resource '" + string + "'");
        }
        return object;
    }

    static byte[] readByteCode(String string, ClassLoader classLoader) throws FileNotFoundException, IOException {
        return BytecodeAnalyzer.readBytes(BytecodeAnalyzer.getClassResource(string, classLoader).openStream(), true);
    }

    static byte[] readBytes(InputStream inputStream2, boolean bl) throws IOException {
        int n2;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] byArray = new byte[1024];
        while ((n2 = inputStream2.read(byArray)) > 0) {
            byteArrayOutputStream.write(byArray, 0, n2);
        }
        if (bl) {
            inputStream2.close();
        }
        return byteArrayOutputStream.toByteArray();
    }

    public static List<String> getFieldNames(Class clazz, Class clazz2) throws IOException {
        return BytecodeAnalyzer.getFieldNames(BytecodeAnalyzer.getInternalName(clazz), BytecodeAnalyzer.getInternalName(clazz2), Platform.getClassLoader(clazz), new ArrayList<String>());
    }

    public static List<String> getMethodNames(Class clazz, Class clazz2) throws IOException {
        return BytecodeAnalyzer.getMethodNames(BytecodeAnalyzer.getInternalName(clazz), BytecodeAnalyzer.getInternalName(clazz2), Platform.getClassLoader(clazz), new ArrayList<String>());
    }

    static class EmptyVisitor
    extends ClassVisitor {
        public EmptyVisitor() {
            super(262144);
        }

        public void visit(int n2, int n3, String string, String string2, String string3, String[] stringArray) {
        }

        public void visitSource(String string, String string2) {
        }

        public void visitOuterClass(String string, String string2, String string3) {
        }

        public AnnotationVisitor visitAnnotation(String string, boolean bl) {
            return null;
        }

        public void visitAttribute(Attribute attribute) {
        }

        public void visitInnerClass(String string, String string2, String string3, int n2) {
        }

        public FieldVisitor visitField(int n2, String string, String string2, String string3, Object object) {
            return null;
        }

        public MethodVisitor visitMethod(int n2, String string, String string2, String string3, String[] stringArray) {
            return null;
        }

        public void visitEnd() {
        }
    }
}


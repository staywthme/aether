/*
 * Decompiled with CFR 0.152.
 */
package org.bridj.util;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import org.bridj.Pointer;
import org.bridj.relocated.org.objectweb.asm.ClassReader;
import org.bridj.relocated.org.objectweb.asm.ClassVisitor;
import org.bridj.relocated.org.objectweb.asm.ClassWriter;
import org.bridj.relocated.org.objectweb.asm.FieldVisitor;
import org.bridj.relocated.org.objectweb.asm.MethodVisitor;
import org.bridj.util.ClassDefiner;
import org.bridj.util.JNIUtils;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class ASMUtils {
    public static String typeDesc(Type typeArray) {
        if (typeArray instanceof Class) {
            if ((typeArray = (Class)typeArray) == Pointer.class) {
                return "Pointer";
            }
            if (typeArray.isPrimitive()) {
                String string = typeArray.getSimpleName();
                return Character.toUpperCase(string.charAt(0)) + string.substring(1);
            }
            if (typeArray.isArray()) {
                return ASMUtils.typeDesc(typeArray.getComponentType()) + "Array";
            }
            return typeArray.getName().replace('.', '_');
        }
        typeArray = (ParameterizedType)typeArray;
        StringBuilder stringBuilder = new StringBuilder(ASMUtils.typeDesc(typeArray.getRawType()));
        for (Type type : typeArray.getActualTypeArguments()) {
            stringBuilder.append("_").append(ASMUtils.typeDesc(type));
        }
        return stringBuilder.toString();
    }

    public static void addSuperCall(ClassVisitor object, String string) {
        object = ((ClassVisitor)object).visitMethod(1, "<init>", "()V", null, null);
        ((MethodVisitor)object).visitCode();
        ((MethodVisitor)object).visitVarInsn(25, 0);
        ((MethodVisitor)object).visitMethodInsn(183, string, "<init>", "()V");
        ((MethodVisitor)object).visitInsn(177);
        ((MethodVisitor)object).visitMaxs(1, 1);
        ((MethodVisitor)object).visitEnd();
    }

    public static <T> Class<? extends T> createSubclassWithSynchronizedNativeMethodsAndNoStaticFields(Class<T> object, ClassDefiner classDefiner) throws IOException {
        Object object2 = "$SynchronizedNative";
        String string = JNIUtils.getNativeName(object);
        String string2 = ((Class)object).getName() + (String)object2;
        object2 = string + (String)object2;
        ClassWriter classWriter = new ClassWriter(0);
        object2 = new ClassVisitor(262144, classWriter, (String)object2, string){
            final /* synthetic */ String val$synchronizedInternalName;
            final /* synthetic */ String val$originalInternalName;
            {
                this.val$synchronizedInternalName = string;
                this.val$originalInternalName = string2;
                super(n2, classVisitor);
            }

            public final void visit(int n2, int n3, String string, String string2, String string3, String[] stringArray) {
                super.visit(n2, n3, this.val$synchronizedInternalName, null, this.val$originalInternalName, new String[0]);
                ASMUtils.addSuperCall(this.cv, this.val$originalInternalName);
            }

            public final void visitInnerClass(String string, String string2, String string3, int n2) {
            }

            public final FieldVisitor visitField(int n2, String string, String string2, String string3, Object object) {
                return null;
            }

            public final MethodVisitor visitMethod(int n2, String string, String string2, String string3, String[] stringArray) {
                if (!Modifier.isNative(n2)) {
                    return null;
                }
                return super.visitMethod(n2 | 0x20, string, string2, string3, stringArray);
            }
        };
        object = new ClassReader(((Class)object).getName());
        ((ClassReader)object).accept((ClassVisitor)object2, 0);
        return classDefiner.defineClass(string2, classWriter.toByteArray());
    }
}


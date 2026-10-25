/*
 * Decompiled with CFR 0.152.
 */
package org.bridj;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import org.bridj.BridJ;
import org.bridj.StructFieldDescription;
import org.bridj.ann.Alignment;
import org.bridj.ann.Array;
import org.bridj.ann.Bits;
import org.bridj.ann.CLong;
import org.bridj.ann.Field;
import org.bridj.ann.Ptr;
import org.bridj.ann.Union;
import org.bridj.util.AnnotationUtils;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
class StructFieldDeclaration {
    final StructFieldDescription desc = new StructFieldDescription();
    Method setter;
    long index = -1L;
    long unionWith = -1L;
    Class<?> valueClass;
    Class<?> declaringClass;

    StructFieldDeclaration() {
    }

    public String toString() {
        return this.desc.name + " (index = " + this.index + (this.unionWith < 0L ? "" : ", unionWith = " + this.unionWith) + ", desc = " + this.desc + ")";
    }

    protected static boolean acceptFieldGetter(Member member, boolean bl) {
        if (member instanceof Method && ((Method)member).getParameterTypes().length != (bl ? 0 : 1)) {
            return false;
        }
        if (((AnnotatedElement)((Object)member)).getAnnotation(Field.class) == null) {
            return false;
        }
        int n2 = member.getModifiers();
        return !Modifier.isStatic(n2);
    }

    protected static List<StructFieldDeclaration> listFields(Class<?> clazz) {
        Object object;
        ArrayList<StructFieldDeclaration> arrayList = new ArrayList<StructFieldDeclaration>();
        for (Method method : clazz.getMethods()) {
            if (!StructFieldDeclaration.acceptFieldGetter(method, true)) continue;
            StructFieldDeclaration object2 = StructFieldDeclaration.fromGetter(method);
            try {
                object = clazz.getMethod(method.getName(), object2.valueClass);
                if (StructFieldDeclaration.acceptFieldGetter((Member)object, false)) {
                    object2.setter = object;
                }
            }
            catch (Exception exception) {}
            if (object2 == null) continue;
            arrayList.add(object2);
        }
        int n2 = 0;
        for (java.lang.reflect.Field field : clazz.getFields()) {
            if (!StructFieldDeclaration.acceptFieldGetter(field, true) || (object = StructFieldDeclaration.fromField(field)) == null) continue;
            arrayList.add((StructFieldDeclaration)object);
            ++n2;
        }
        if (n2 > 0 && BridJ.warnStructFields) {
            BridJ.warning("Struct " + clazz.getName() + " has " + n2 + " struct fields implemented as Java fields, which won't give the best performance and might require counter-intuitive calls to BridJ.readFromNative / .writeToNative. Please consider using JNAerator to generate your struct instead, or use BRIDJ_WARN_STRUCT_FIELDS=0 or -Dbridj.warnStructFields=false to mute this warning.");
        }
        return arrayList;
    }

    protected static StructFieldDeclaration fromField(java.lang.reflect.Field field) {
        StructFieldDeclaration structFieldDeclaration = StructFieldDeclaration.fromMember(field);
        StructFieldDeclaration.fromMember(field).desc.field = field;
        structFieldDeclaration.desc.valueType = field.getGenericType();
        structFieldDeclaration.valueClass = field.getType();
        return structFieldDeclaration;
    }

    protected static StructFieldDeclaration fromGetter(Method method) {
        StructFieldDeclaration structFieldDeclaration = StructFieldDeclaration.fromMember(method);
        StructFieldDeclaration.fromMember(method).desc.getter = method;
        structFieldDeclaration.desc.valueType = method.getGenericReturnType();
        structFieldDeclaration.valueClass = method.getReturnType();
        return structFieldDeclaration;
    }

    private static StructFieldDeclaration fromMember(Member object) {
        StructFieldDeclaration structFieldDeclaration = new StructFieldDeclaration();
        new StructFieldDeclaration().declaringClass = object.getDeclaringClass();
        Object object2 = object.getName();
        if (((String)object2).matches("get[A-Z].*")) {
            object2 = Character.toLowerCase(((String)object2).charAt(3)) + ((String)object2).substring(4);
        }
        structFieldDeclaration.desc.name = object2;
        object = (AnnotatedElement)object;
        object2 = object.getAnnotation(Field.class);
        Bits bits = object.getAnnotation(Bits.class);
        Alignment alignment = object.getAnnotation(Alignment.class);
        Array array = object.getAnnotation(Array.class);
        if (object2 != null) {
            structFieldDeclaration.index = object2.value();
            structFieldDeclaration.unionWith = object2.unionWith();
        }
        if (structFieldDeclaration.unionWith < 0L && structFieldDeclaration.declaringClass.getAnnotation(Union.class) != null) {
            structFieldDeclaration.unionWith = 0L;
        }
        if (bits != null) {
            structFieldDeclaration.desc.bitLength = bits.value();
        }
        if (alignment != null) {
            structFieldDeclaration.desc.alignment = alignment.value();
        }
        if (array != null) {
            long l2 = 1L;
            long[] lArray = array.value();
            object2 = lArray;
            int n2 = lArray.length;
            for (int i2 = 0; i2 < n2; ++i2) {
                Object object3 = object2[i2];
                l2 *= object3;
            }
            structFieldDeclaration.desc.arrayLength = l2;
            structFieldDeclaration.desc.isArray = true;
        }
        structFieldDeclaration.desc.isCLong = AnnotationUtils.isAnnotationPresent(CLong.class, (AnnotatedElement)object, new Annotation[0]);
        structFieldDeclaration.desc.isSizeT = AnnotationUtils.isAnnotationPresent(Ptr.class, (AnnotatedElement)object, new Annotation[0]);
        return structFieldDeclaration;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package org.bridj.relocated.org.objectweb.asm;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class Type {
    public static final int VOID = 0;
    public static final int BOOLEAN = 1;
    public static final int CHAR = 2;
    public static final int BYTE = 3;
    public static final int SHORT = 4;
    public static final int INT = 5;
    public static final int FLOAT = 6;
    public static final int LONG = 7;
    public static final int DOUBLE = 8;
    public static final int ARRAY = 9;
    public static final int OBJECT = 10;
    public static final int METHOD = 11;
    public static final Type VOID_TYPE = new Type(0, null, 0x56050000, 1);
    public static final Type BOOLEAN_TYPE = new Type(1, null, 1509950721, 1);
    public static final Type CHAR_TYPE = new Type(2, null, 1124075009, 1);
    public static final Type BYTE_TYPE = new Type(3, null, 1107297537, 1);
    public static final Type SHORT_TYPE = new Type(4, null, 1392510721, 1);
    public static final Type INT_TYPE = new Type(5, null, 1224736769, 1);
    public static final Type FLOAT_TYPE = new Type(6, null, 1174536705, 1);
    public static final Type LONG_TYPE = new Type(7, null, 1241579778, 1);
    public static final Type DOUBLE_TYPE = new Type(8, null, 1141048066, 1);
    private final int a;
    private final char[] b;
    private final int c;
    private final int d;

    private Type(int n2, char[] cArray, int n3, int n4) {
        this.a = n2;
        this.b = cArray;
        this.c = n3;
        this.d = n4;
    }

    public static Type getType(String string) {
        return Type.a(string.toCharArray(), 0);
    }

    public static Type getObjectType(String object) {
        return new Type((object = (Object)((String)object).toCharArray())[0] == 91 ? 9 : 10, (char[])object, 0, ((Object)object).length);
    }

    public static Type getMethodType(String string) {
        return Type.a(string.toCharArray(), 0);
    }

    public static Type getMethodType(Type type, Type ... typeArray) {
        return Type.getType(Type.getMethodDescriptor(type, typeArray));
    }

    public static Type getType(Class clazz) {
        if (clazz.isPrimitive()) {
            if (clazz == Integer.TYPE) {
                return INT_TYPE;
            }
            if (clazz == Void.TYPE) {
                return VOID_TYPE;
            }
            if (clazz == Boolean.TYPE) {
                return BOOLEAN_TYPE;
            }
            if (clazz == Byte.TYPE) {
                return BYTE_TYPE;
            }
            if (clazz == Character.TYPE) {
                return CHAR_TYPE;
            }
            if (clazz == Short.TYPE) {
                return SHORT_TYPE;
            }
            if (clazz == Double.TYPE) {
                return DOUBLE_TYPE;
            }
            if (clazz == Float.TYPE) {
                return FLOAT_TYPE;
            }
            return LONG_TYPE;
        }
        return Type.getType(Type.getDescriptor(clazz));
    }

    public static Type getType(Constructor constructor) {
        return Type.getType(Type.getConstructorDescriptor(constructor));
    }

    public static Type getType(Method method) {
        return Type.getType(Type.getMethodDescriptor(method));
    }

    public static Type[] getArgumentTypes(String object) {
        Object object2;
        object = ((String)object).toCharArray();
        int n2 = 1;
        int n3 = 0;
        while ((object2 = object[n2++]) != 41) {
            if (object2 == 76) {
                while (object[n2++] != 59) {
                }
                ++n3;
                continue;
            }
            if (object2 == 91) continue;
            ++n3;
        }
        Type[] typeArray = new Type[n3];
        n2 = 1;
        n3 = 0;
        while (object[n2] != 41) {
            typeArray[n3] = Type.a((char[])object, n2);
            n2 += typeArray[n3].d + (typeArray[n3].a == 10 ? 2 : 0);
            ++n3;
        }
        return typeArray;
    }

    public static Type[] getArgumentTypes(Method classArray) {
        classArray = classArray.getParameterTypes();
        Type[] typeArray = new Type[classArray.length];
        for (int i2 = classArray.length - 1; i2 >= 0; --i2) {
            typeArray[i2] = Type.getType(classArray[i2]);
        }
        return typeArray;
    }

    public static Type getReturnType(String string) {
        char[] cArray = string.toCharArray();
        return Type.a(cArray, string.indexOf(41) + 1);
    }

    public static Type getReturnType(Method method) {
        return Type.getType(method.getReturnType());
    }

    public static int getArgumentsAndReturnSizes(String string) {
        int n2 = 1;
        int n3 = 1;
        while (true) {
            char c2;
            if ((c2 = string.charAt(n3++)) == ')') {
                c2 = string.charAt(n3);
                return n2 << 2 | (c2 == 'V' ? 0 : (c2 == 'D' || c2 == 'J' ? 2 : 1));
            }
            if (c2 == 'L') {
                while (string.charAt(n3++) != ';') {
                }
                ++n2;
                continue;
            }
            if (c2 == '[') {
                while ((c2 = string.charAt(n3)) == '[') {
                    ++n3;
                }
                if (c2 != 'D' && c2 != 'J') continue;
                --n2;
                continue;
            }
            if (c2 == 'D' || c2 == 'J') {
                n2 += 2;
                continue;
            }
            ++n2;
        }
    }

    private static Type a(char[] cArray, int n2) {
        switch (cArray[n2]) {
            case 'V': {
                return VOID_TYPE;
            }
            case 'Z': {
                return BOOLEAN_TYPE;
            }
            case 'C': {
                return CHAR_TYPE;
            }
            case 'B': {
                return BYTE_TYPE;
            }
            case 'S': {
                return SHORT_TYPE;
            }
            case 'I': {
                return INT_TYPE;
            }
            case 'F': {
                return FLOAT_TYPE;
            }
            case 'J': {
                return LONG_TYPE;
            }
            case 'D': {
                return DOUBLE_TYPE;
            }
            case '[': {
                int n3 = 1;
                while (cArray[n2 + n3] == '[') {
                    ++n3;
                }
                if (cArray[n2 + n3] == 'L') {
                    ++n3;
                    while (cArray[n2 + n3] != ';') {
                        ++n3;
                    }
                }
                return new Type(9, cArray, n2, n3 + 1);
            }
            case 'L': {
                int n4 = 1;
                while (cArray[n2 + n4] != ';') {
                    ++n4;
                }
                return new Type(10, cArray, n2 + 1, n4 - 1);
            }
        }
        return new Type(11, cArray, 0, cArray.length);
    }

    public int getSort() {
        return this.a;
    }

    public int getDimensions() {
        int n2 = 1;
        while (this.b[this.c + n2] == '[') {
            ++n2;
        }
        return n2;
    }

    public Type getElementType() {
        return Type.a(this.b, this.c + this.getDimensions());
    }

    public String getClassName() {
        switch (this.a) {
            case 0: {
                return "void";
            }
            case 1: {
                return "boolean";
            }
            case 2: {
                return "char";
            }
            case 3: {
                return "byte";
            }
            case 4: {
                return "short";
            }
            case 5: {
                return "int";
            }
            case 6: {
                return "float";
            }
            case 7: {
                return "long";
            }
            case 8: {
                return "double";
            }
            case 9: {
                StringBuffer stringBuffer = new StringBuffer(this.getElementType().getClassName());
                for (int i2 = this.getDimensions(); i2 > 0; --i2) {
                    stringBuffer.append("[]");
                }
                return stringBuffer.toString();
            }
            case 10: {
                return new String(this.b, this.c, this.d).replace('/', '.');
            }
        }
        return null;
    }

    public String getInternalName() {
        return new String(this.b, this.c, this.d);
    }

    public Type[] getArgumentTypes() {
        return Type.getArgumentTypes(this.getDescriptor());
    }

    public Type getReturnType() {
        return Type.getReturnType(this.getDescriptor());
    }

    public int getArgumentsAndReturnSizes() {
        return Type.getArgumentsAndReturnSizes(this.getDescriptor());
    }

    public String getDescriptor() {
        StringBuffer stringBuffer = new StringBuffer();
        this.a(stringBuffer);
        return stringBuffer.toString();
    }

    public static String getMethodDescriptor(Type type, Type ... typeArray) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append('(');
        for (int i2 = 0; i2 < typeArray.length; ++i2) {
            typeArray[i2].a(stringBuffer);
        }
        stringBuffer.append(')');
        type.a(stringBuffer);
        return stringBuffer.toString();
    }

    private void a(StringBuffer stringBuffer) {
        if (this.b == null) {
            stringBuffer.append((char)((this.c & 0xFF000000) >>> 24));
            return;
        }
        if (this.a == 10) {
            stringBuffer.append('L');
            stringBuffer.append(this.b, this.c, this.d);
            stringBuffer.append(';');
            return;
        }
        stringBuffer.append(this.b, this.c, this.d);
    }

    public static String getInternalName(Class clazz) {
        return clazz.getName().replace('.', '/');
    }

    public static String getDescriptor(Class clazz) {
        StringBuffer stringBuffer = new StringBuffer();
        Type.a(stringBuffer, clazz);
        return stringBuffer.toString();
    }

    public static String getConstructorDescriptor(Constructor classArray) {
        classArray = classArray.getParameterTypes();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append('(');
        for (int i2 = 0; i2 < classArray.length; ++i2) {
            Type.a(stringBuffer, classArray[i2]);
        }
        return stringBuffer.append(")V").toString();
    }

    public static String getMethodDescriptor(Method method) {
        Class<?>[] classArray = method.getParameterTypes();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append('(');
        for (int i2 = 0; i2 < classArray.length; ++i2) {
            Type.a(stringBuffer, classArray[i2]);
        }
        stringBuffer.append(')');
        Type.a(stringBuffer, method.getReturnType());
        return stringBuffer.toString();
    }

    private static void a(StringBuffer stringBuffer, Class object) {
        while (true) {
            if (((Class)object).isPrimitive()) {
                int n2 = object == Integer.TYPE ? 73 : (object == Void.TYPE ? 86 : (object == Boolean.TYPE ? 90 : (object == Byte.TYPE ? 66 : (object == Character.TYPE ? 67 : (object == Short.TYPE ? 83 : (object == Double.TYPE ? 68 : (object == Float.TYPE ? 70 : 74)))))));
                stringBuffer.append((char)n2);
                return;
            }
            if (!((Class)object).isArray()) break;
            stringBuffer.append('[');
            object = ((Class)object).getComponentType();
        }
        stringBuffer.append('L');
        object = ((Class)object).getName();
        int n3 = ((String)object).length();
        for (int i2 = 0; i2 < n3; ++i2) {
            char c2 = ((String)object).charAt(i2);
            stringBuffer.append(c2 == '.' ? (char)'/' : (char)c2);
        }
        stringBuffer.append(';');
    }

    public int getSize() {
        if (this.b == null) {
            return this.c & 0xFF;
        }
        return 1;
    }

    public int getOpcode(int n2) {
        if (n2 == 46 || n2 == 79) {
            return n2 + (this.b == null ? this.c >> 8 & 0xFF : 4);
        }
        return n2 + (this.b == null ? this.c >> 16 & 0xFF : 4);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Type)) {
            return false;
        }
        object = (Type)object;
        if (this.a != ((Type)object).a) {
            return false;
        }
        if (this.a >= 9) {
            if (this.d != ((Type)object).d) {
                return false;
            }
            int n2 = this.c;
            int n3 = ((Type)object).c;
            int n4 = n2 + this.d;
            while (n2 < n4) {
                if (this.b[n2] != ((Type)object).b[n3]) {
                    return false;
                }
                ++n2;
                ++n3;
            }
        }
        return true;
    }

    public int hashCode() {
        int n2 = 13 * this.a;
        if (this.a >= 9) {
            int n3;
            int n4 = n3 + this.d;
            for (n3 = this.c; n3 < n4; ++n3) {
                n2 = 17 * (n2 + this.b[n3]);
            }
        }
        return n2;
    }

    public String toString() {
        return this.getDescriptor();
    }
}


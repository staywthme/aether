/*
 * Decompiled with CFR 0.152.
 */
package org.bridj.demangling;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.regex.Pattern;
import org.bridj.BridJ;
import org.bridj.CLong;
import org.bridj.CRuntime;
import org.bridj.Callback;
import org.bridj.FlagSet;
import org.bridj.NativeLibrary;
import org.bridj.NativeObject;
import org.bridj.Platform;
import org.bridj.Pointer;
import org.bridj.SizeT;
import org.bridj.TimeT;
import org.bridj.ValuedEnum;
import org.bridj.ann.Convention;
import org.bridj.ann.Name;
import org.bridj.ann.Namespace;
import org.bridj.ann.Ptr;
import org.bridj.ann.Template;
import org.bridj.demangling.GCC4Demangler;
import org.bridj.demangling.VC9Demangler;
import org.bridj.util.AnnotationUtils;
import org.bridj.util.DefaultParameterizedType;
import org.bridj.util.Utils;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public abstract class Demangler {
    protected final String str;
    protected final int length;
    protected int position = 0;
    protected final NativeLibrary library;

    public static void main(String[] stringArray) {
        for (String string : stringArray) {
            Exception exception;
            try {
                System.out.println("VC9: " + ((Demangler)new VC9Demangler(null, string)).parseSymbol());
            }
            catch (Exception exception2) {
                exception = exception2;
                exception2.printStackTrace();
            }
            try {
                System.out.println("GCC4: " + ((Demangler)new GCC4Demangler(null, string)).parseSymbol());
            }
            catch (Exception exception3) {
                exception = exception3;
                exception3.printStackTrace();
            }
        }
    }

    public static Annotations annotations(final Annotation[] annotationArray) {
        return new Annotations(){

            @Override
            public final <A extends Annotation> A getAnnotation(Class<A> clazz) {
                if (annotationArray == null) {
                    return null;
                }
                Annotation[] annotationArray2 = annotationArray;
                int n2 = annotationArray.length;
                for (int i2 = 0; i2 < n2; ++i2) {
                    Annotation annotation = annotationArray2[i2];
                    if (!clazz.isInstance(annotation)) continue;
                    return (A)annotation;
                }
                return null;
            }

            @Override
            public final boolean isAnnotationPresent(Class<? extends Annotation> clazz) {
                return AnnotationUtils.isAnnotationPresent(clazz, annotationArray);
            }
        };
    }

    public static Annotations annotations(Type type) {
        return Demangler.annotations(Utils.getClass(type));
    }

    public static Annotations annotations(final AnnotatedElement annotatedElement) {
        return new Annotations(){

            @Override
            public final <A extends Annotation> A getAnnotation(Class<A> clazz) {
                return annotatedElement.getAnnotation(clazz);
            }

            @Override
            public final boolean isAnnotationPresent(Class<? extends Annotation> clazz) {
                return AnnotationUtils.isAnnotationPresent(clazz, annotatedElement, new Annotation[0]);
            }
        };
    }

    public abstract MemberRef parseSymbol() throws DemanglingException;

    public Demangler(NativeLibrary nativeLibrary, String string) {
        this.str = string;
        this.length = string.length();
        this.library = nativeLibrary;
    }

    public String getString() {
        return this.str;
    }

    protected void expectChars(char ... cArray) throws DemanglingException {
        for (char c2 : cArray) {
            char c3 = this.consumeChar();
            if (c3 == c2) continue;
            throw this.error("Expected char '" + c2 + "', found '" + c3 + "'");
        }
    }

    protected void expectAnyChar(char ... cArray) throws DemanglingException {
        char c2 = this.consumeChar();
        char[] cArray2 = cArray;
        int n2 = cArray.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            char c3 = cArray2[i2];
            if (c2 != c3) continue;
            return;
        }
        throw this.error("Expected any of " + Arrays.toString(cArray) + ", found '" + c2 + "'");
    }

    public static StringBuilder implode(StringBuilder stringBuilder, Object[] objectArray, String string) {
        return Demangler.implode(stringBuilder, Arrays.asList(objectArray), string);
    }

    public static StringBuilder implode(StringBuilder stringBuilder, Iterable<?> object, String string) {
        boolean bl = true;
        object = object.iterator();
        while (object.hasNext()) {
            Object e2 = object.next();
            if (bl) {
                bl = false;
            } else {
                stringBuilder.append(string);
            }
            stringBuilder.append(e2);
        }
        return stringBuilder;
    }

    protected char peekChar() {
        return this.peekChar(1);
    }

    protected char peekChar(int n2) {
        if ((n2 = this.position + n2 - 1) >= this.length) {
            return '\u0000';
        }
        return this.str.charAt(n2);
    }

    protected char lastChar() {
        if (this.position == 0) {
            return '\u0000';
        }
        return this.str.charAt(this.position - 1);
    }

    protected char consumeChar() throws DemanglingException {
        char c2 = this.peekChar();
        if (c2 != '\u0000') {
            ++this.position;
        } else {
            throw new DemanglingException("Reached end of string '" + this.str + "'");
        }
        return c2;
    }

    protected boolean consumeCharsIf(char ... cArray) {
        int n2 = this.position;
        try {
            for (char c2 : cArray) {
                char c3 = this.consumeChar();
                if (c3 == c2) continue;
                this.position = n2;
                return false;
            }
            return true;
        }
        catch (DemanglingException demanglingException) {
            this.position = n2;
            return false;
        }
    }

    protected boolean consumeCharIf(char ... cArray) {
        char c2 = this.peekChar();
        for (char c3 : cArray) {
            if (c3 != c2) continue;
            ++this.position;
            return true;
        }
        return false;
    }

    protected DemanglingException error(int n2) {
        return this.error(null, n2);
    }

    protected DemanglingException error(String string) {
        return this.error(string, -1);
    }

    protected DemanglingException error(String string, int n2) {
        StringBuilder stringBuilder = new StringBuilder(this.position + 1);
        n2 = this.position + n2;
        for (int i2 = 0; i2 < n2; ++i2) {
            stringBuilder.append(' ');
        }
        stringBuilder.append('^');
        return new DemanglingException("Parsing error at position " + n2 + (string == null ? "" : ": " + string) + " \n\t" + this.str + "\n\t" + stringBuilder);
    }

    public static String getMethodName(Method method) {
        Name name = method.getAnnotation(Name.class);
        if (name == null) {
            return method.getName();
        }
        return name.value();
    }

    public static String getClassName(Type clazz) {
        assert (clazz != null);
        Name name = (clazz = Utils.getClass(clazz)).getAnnotation(Name.class);
        if (name == null) {
            return clazz.getSimpleName();
        }
        return name.value();
    }

    public static String getFullClassName(Type object) {
        object = Utils.getClass((Type)object);
        String string = Demangler.getClassName(object);
        if ((object = ((Class)object).getAnnotation(Namespace.class)) != null) {
            return object.value().replaceAll("::", ".") + "." + string;
        }
        return string;
    }

    protected static TypeRef pointerType(TypeRef typeRef, boolean bl, boolean bl2) {
        return new PointerTypeRef(typeRef, bl, bl2);
    }

    protected static TypeRef classType(Class<?> clazz, Class<? extends Annotation> ... classArray) {
        return Demangler.classType(clazz, null, classArray);
    }

    protected static TypeRef classType(Class<?> clazz, Type[] typeArray, Class<? extends Annotation> ... classArray) {
        JavaTypeRef javaTypeRef = new JavaTypeRef();
        javaTypeRef.type = typeArray == null ? clazz : new DefaultParameterizedType(clazz, typeArray);
        javaTypeRef.annotations = classArray;
        return javaTypeRef;
    }

    protected static TypeRef simpleType(String string, TemplateArg ... templateArgArray) {
        return new ClassRef(new Ident(string, templateArgArray));
    }

    protected static TypeRef simpleType(Ident ident) {
        return new ClassRef(ident);
    }

    static Class<?> getTypeClass(Type type) {
        if (type instanceof Class) {
            return (Class)type;
        }
        if (type instanceof ParameterizedType) {
            Object object = (Type[])(type = (ParameterizedType)type).getRawType();
            if (ValuedEnum.class.isAssignableFrom((Class<?>)object)) {
                object = type.getActualTypeArguments();
                object = object == null || ((Type[])object).length != 1 ? Integer.TYPE : Demangler.getTypeClass(type.getActualTypeArguments()[0]);
            }
            return object;
        }
        if (type instanceof GenericArrayType && Object.class.isAssignableFrom(Demangler.getTypeClass(((GenericArrayType)type).getGenericComponentType()))) {
            return Object[].class;
        }
        return null;
    }

    private static Type normalize(Type type) {
        Type type2;
        while (type instanceof WildcardType) {
            type2 = (WildcardType)type;
            if (type2.getLowerBounds().length == 1) {
                type = type2.getLowerBounds()[0];
                continue;
            }
            return null;
        }
        type2 = Utils.getClass(type);
        if (type2 != null && ((Class)type2).isPrimitive()) {
            if (type == Float.TYPE) {
                return Float.class;
            }
            if (type == Double.TYPE) {
                return Double.class;
            }
            if (type == Byte.TYPE) {
                return Byte.class;
            }
            if (type == Character.TYPE) {
                return Character.class;
            }
            if (type == Short.TYPE) {
                return Short.class;
            }
            if (type == Integer.TYPE) {
                return Integer.class;
            }
            if (type == Long.TYPE) {
                return Long.class;
            }
            if (type == Boolean.TYPE) {
                return Boolean.class;
            }
            if (type == Void.TYPE) {
                return Void.class;
            }
        }
        return type;
    }

    static boolean equivalentTypes(Type type, Class clazz, Annotations annotations, Type type2, Class clazz2, Annotations annotations2) {
        if (Demangler.normalize(type).equals(Demangler.normalize(type2))) {
            return true;
        }
        if (annotations != null && annotations2 != null) {
            if (Demangler.xor(Demangler.isPointerLike(type, clazz, annotations), Demangler.isPointerLike(type2, clazz2, annotations2))) {
                return false;
            }
            if (Demangler.xor(Demangler.isCLong(type, clazz, annotations), Demangler.isCLong(type2, clazz2, annotations2))) {
                return false;
            }
        }
        int n2 = Demangler.getIntegralSize(type, clazz, annotations);
        int n3 = Demangler.getIntegralSize(type2, clazz2, annotations2);
        return n2 != -1 && n2 == n3;
    }

    static boolean xor(boolean bl, boolean bl2) {
        return bl && !bl2 || !bl && bl2;
    }

    static boolean isPointerLike(Type type, Class clazz, Annotations annotations) {
        if (type == Long.TYPE || type == Long.class) {
            return annotations == null && Pointer.SIZE == 8 || annotations != null && annotations.isAnnotationPresent(Ptr.class) && !annotations.isAnnotationPresent(org.bridj.ann.CLong.class);
        }
        return type == SizeT.class || Pointer.class.isAssignableFrom(clazz);
    }

    static boolean isCLong(Type type, Class clazz, Annotations annotations) {
        if (type == Long.TYPE || type == Long.class) {
            return annotations == null && CLong.SIZE == 8 || annotations != null && annotations.isAnnotationPresent(org.bridj.ann.CLong.class);
        }
        return type == CLong.class;
    }

    static int getIntegralSize(Type type, Class clazz, Annotations annotations) {
        if (type == Integer.TYPE || type == Integer.class) {
            return 4;
        }
        if (type == Long.TYPE || type == Long.class) {
            if (annotations != null) {
                if (annotations.isAnnotationPresent(Ptr.class)) {
                    return Pointer.SIZE;
                }
                if (annotations.isAnnotationPresent(org.bridj.ann.CLong.class)) {
                    return CLong.SIZE;
                }
            }
            return 8;
        }
        if (type == CLong.class) {
            return CLong.SIZE;
        }
        if (type == SizeT.class) {
            return SizeT.SIZE;
        }
        if (type == TimeT.class) {
            return TimeT.SIZE;
        }
        if (type == Byte.TYPE || type == Byte.class) {
            return 1;
        }
        if (type == Character.TYPE || type == Character.class || type == Short.TYPE || type == Short.class) {
            return 2;
        }
        if (ValuedEnum.class.isAssignableFrom(clazz)) {
            return 4;
        }
        if (Pointer.class.isAssignableFrom(clazz)) {
            return SizeT.SIZE;
        }
        return -1;
    }

    public static boolean equivalentTypes(Type type, Annotations annotations, Type type2, Annotations annotations2) {
        Type type3 = type;
        Type type4 = type2;
        return Demangler.equivalentTypes(type3, Demangler.getTypeClass(type3), annotations, type4, Demangler.getTypeClass(type4), annotations2);
    }

    static void appendTemplateArgs(StringBuilder stringBuilder, Object[] objectArray) {
        if (objectArray == null || objectArray.length == 0) {
            return;
        }
        Demangler.appendArgs(stringBuilder, '<', '>', objectArray);
    }

    static void appendArgs(StringBuilder stringBuilder, char c2, char c3, Object[] objectArray) {
        stringBuilder.append(c2);
        if (objectArray != null) {
            for (c2 = '\u0000'; c2 < objectArray.length; c2 = (char)(c2 + 1)) {
                if (c2 != '\u0000') {
                    stringBuilder.append(", ");
                }
                stringBuilder.append(objectArray[c2]);
            }
        }
        stringBuilder.append(c3);
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static class MemberRef {
        private Integer argumentsStackSize;
        private TypeRef enclosingType;
        private TypeRef valueType;
        private IdentLike memberName;
        Boolean isStatic;
        Boolean isProtected;
        Boolean isPrivate;
        public int modifiers;
        public TypeRef[] paramTypes;
        public TypeRef[] throwTypes;
        TemplateArg[] templateArguments;
        public Convention.Style callingConvention;

        public void setTemplateArguments(TemplateArg[] templateArgArray) {
            this.templateArguments = templateArgArray;
        }

        public Integer getArgumentsStackSize() {
            return this.argumentsStackSize;
        }

        public void setArgumentsStackSize(Integer n2) {
            this.argumentsStackSize = n2;
        }

        protected boolean matchesEnclosingType(Type type) {
            if (this.getEnclosingType() != null) {
                Type type2 = type;
                if (this.getEnclosingType().matches(type2, Demangler.annotations(type2))) {
                    return true;
                }
            }
            return false;
        }

        protected boolean matchesVirtualTable(Type type) {
            return this.memberName == SpecialName.VFTable && this.matchesEnclosingType(type);
        }

        protected boolean matchesConstructor(Type object, Constructor<?> constructor) {
            int n2;
            if (this.memberName != SpecialName.Constructor) {
                return false;
            }
            if (!this.matchesEnclosingType((Type)object)) {
                return false;
            }
            object = Utils.getClass((Type)object).getAnnotation(Template.class);
            Annotation[][] annotationArray = constructor.getParameterAnnotations();
            Type[] typeArray = constructor.getGenericParameterTypes();
            return this.matchesArgs(typeArray, annotationArray, (n2 = Utils.getEnclosedConstructorParametersOffset(constructor)) + (object == null ? 0 : object.value().length));
        }

        protected boolean matchesDestructor(Type type) {
            return this.memberName == SpecialName.Destructor && this.matchesEnclosingType(type);
        }

        static boolean hasInstance(Object[] objectArray, Class<? extends Annotation> ... classArray) {
            for (Object object : objectArray) {
                Class<? extends Annotation>[] classArray2 = classArray;
                int n2 = classArray.length;
                for (int i2 = 0; i2 < n2; ++i2) {
                    Class<? extends Annotation> clazz = classArray2[i2];
                    if (!clazz.isInstance(object)) continue;
                    return true;
                }
            }
            return false;
        }

        static int getArgumentsStackSize(Method annotationArray) {
            int n2 = 0;
            Type[] typeArray = annotationArray.getGenericParameterTypes();
            annotationArray = annotationArray.getParameterAnnotations();
            int n3 = typeArray.length;
            for (int i2 = 0; i2 < n3; ++i2) {
                Annotation[] annotationArray2 = Demangler.getTypeClass(typeArray[i2]);
                if (annotationArray2 == Integer.TYPE) {
                    n2 += 4;
                    continue;
                }
                if (annotationArray2 == Long.TYPE) {
                    annotationArray2 = annotationArray[i2];
                    if (AnnotationUtils.isAnnotationPresent(Ptr.class, annotationArray2) || AnnotationUtils.isAnnotationPresent(org.bridj.ann.CLong.class, annotationArray2)) {
                        n2 += Pointer.SIZE;
                        continue;
                    }
                    n2 += 8;
                    continue;
                }
                if (annotationArray2 == Float.TYPE) {
                    n2 += 4;
                    continue;
                }
                if (annotationArray2 == Double.TYPE) {
                    n2 += 8;
                    continue;
                }
                if (annotationArray2 == Byte.TYPE) {
                    ++n2;
                    continue;
                }
                if (annotationArray2 == Character.TYPE) {
                    n2 += Platform.WCHAR_T_SIZE;
                    continue;
                }
                if (annotationArray2 == CLong.class) {
                    n2 += Platform.CLONG_SIZE;
                    continue;
                }
                if (annotationArray2 == SizeT.class) {
                    n2 += Platform.SIZE_T_SIZE;
                    continue;
                }
                if (annotationArray2 == TimeT.class) {
                    n2 += Platform.TIME_T_SIZE;
                    continue;
                }
                if (annotationArray2 == Short.TYPE) {
                    n2 += 2;
                    continue;
                }
                if (annotationArray2 == Boolean.TYPE) {
                    ++n2;
                    continue;
                }
                if (Pointer.class.isAssignableFrom((Class<?>)annotationArray2)) {
                    n2 += Pointer.SIZE;
                    continue;
                }
                if (NativeObject.class.isAssignableFrom((Class<?>)annotationArray2)) {
                    n2 = (int)((long)n2 + ((CRuntime)BridJ.getRuntime(annotationArray2)).sizeOf(typeArray[i2], null));
                    continue;
                }
                if (FlagSet.class.isAssignableFrom((Class<?>)annotationArray2)) {
                    n2 += 4;
                    continue;
                }
                throw new RuntimeException("Type not handled : " + annotationArray2.getName());
            }
            return n2;
        }

        protected boolean matches(Method method) {
            if (this.memberName instanceof SpecialName) {
                return false;
            }
            if (!this.matchesEnclosingType(method)) {
                return false;
            }
            return this.matchesSignature(method);
        }

        public boolean matchesEnclosingType(Method genericDeclaration) {
            TypeRef typeRef = this.getEnclosingType();
            if (typeRef == null) {
                return true;
            }
            Annotations annotations = Demangler.annotations(genericDeclaration);
            genericDeclaration = ((Method)genericDeclaration).getDeclaringClass();
            do {
                if (!typeRef.matches((Type)((Object)genericDeclaration), annotations)) continue;
                return true;
            } while ((genericDeclaration = ((Class)genericDeclaration).getSuperclass()) != null && genericDeclaration != Object.class);
            return false;
        }

        public boolean matchesSignature(Method typeArray) {
            if (this.getArgumentsStackSize() != null && this.getArgumentsStackSize() != MemberRef.getArgumentsStackSize((Method)typeArray)) {
                return false;
            }
            if (this.getMemberName() != null && !this.getMemberName().toString().equals(Demangler.getMethodName((Method)typeArray))) {
                return false;
            }
            if (this.getValueType() != null && !this.getValueType().matches(typeArray.getReturnType(), Demangler.annotations((AnnotatedElement)typeArray))) {
                return false;
            }
            Template template = typeArray.getAnnotation(Template.class);
            Annotation[][] annotationArray = typeArray.getParameterAnnotations();
            typeArray = typeArray.getGenericParameterTypes();
            return this.paramTypes == null || this.matchesArgs(typeArray, annotationArray, template == null ? 0 : template.value().length);
        }

        private boolean matchesArgs(Type[] typeArray, Annotation[][] annotationArray, int n2) {
            Object object;
            Object object2;
            int n3;
            int n4;
            int n5 = n2;
            int n6 = n4 = this.templateArguments == null ? 0 : this.templateArguments.length;
            for (n3 = 0; n3 < n4; ++n3) {
                if (n5 >= typeArray.length) {
                    return false;
                }
                object2 = typeArray[n2 + n3];
                object = this.templateArguments[n3];
                if (object instanceof TypeRef) {
                    if (!object2.equals(Class.class)) {
                        return false;
                    }
                } else if (object instanceof Constant) {
                    try {
                        Demangler.getTypeClass((Type)object2).cast(((Constant)object).value);
                    }
                    catch (ClassCastException classCastException) {
                        return false;
                    }
                }
                ++n5;
            }
            int n7 = n4 = this.paramTypes == null ? 0 : this.paramTypes.length;
            for (n3 = 0; n3 < n4; ++n3) {
                if (n5 >= typeArray.length) {
                    return false;
                }
                object2 = this.paramTypes[n3];
                object = typeArray[n5];
                if (!((TypeRef)object2).matches((Type)object, Demangler.annotations(annotationArray == null ? null : annotationArray[n3]))) {
                    return false;
                }
                ++n5;
            }
            if (n5 != typeArray.length) {
                BridJ.error("Not enough args for " + this);
                return false;
            }
            return true;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.valueType).append(' ');
            boolean bl = false;
            if (this.enclosingType != null) {
                stringBuilder.append(this.enclosingType);
                stringBuilder.append('.');
                if (this.memberName instanceof SpecialName) {
                    switch ((SpecialName)this.memberName) {
                        case Destructor: {
                            stringBuilder.append('~');
                        }
                        case Constructor: {
                            stringBuilder.append(((ClassRef)this.enclosingType).ident.simpleName);
                            bl = true;
                        }
                    }
                }
            }
            if (!bl) {
                stringBuilder.append(this.memberName);
            }
            Demangler.appendTemplateArgs(stringBuilder, this.templateArguments);
            Demangler.appendArgs(stringBuilder, '(', ')', this.paramTypes);
            return stringBuilder.toString();
        }

        public void setMemberName(IdentLike identLike) {
            this.memberName = identLike;
        }

        public IdentLike getMemberName() {
            return this.memberName;
        }

        public void setValueType(TypeRef typeRef) {
            this.valueType = typeRef;
        }

        public TypeRef getValueType() {
            return this.valueType;
        }

        public void setEnclosingType(TypeRef typeRef) {
            this.enclosingType = typeRef;
        }

        public TypeRef getEnclosingType() {
            return this.enclosingType;
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static enum SpecialName implements IdentLike
    {
        Constructor("", true, true),
        SpecialConstructor("", true, true),
        Destructor("", true, true),
        SelfishDestructor("", true, true),
        DeletingDestructor("", true, true),
        New("new", true, true),
        Delete("delete", true, true),
        NewArray("new[]", true, true),
        DeleteArray("delete[]", true, true),
        VFTable("vftable", false, true),
        VBTable("vbtable", false, true),
        VCall("vcall", false, false),
        TypeOf("typeof", false, false),
        ScalarDeletingDestructor("'scalar deleting destructor'", true, true),
        VectorDeletingDestructor("'vector deleting destructor'", true, true),
        OperatorAssign("operator=", true, true),
        OperatorRShift("operator>>", true, true),
        OperatorDivideAssign("operator/=", true, true),
        OperatorModuloAssign("operator%=", true, true),
        OperatorRShiftAssign("operator>>=", true, true),
        OperatorLShiftAssign("operator<<=", true, true),
        OperatorBitAndAssign("operator&=", true, true),
        OperatorBitOrAssign("operator|=", true, true),
        OperatorXORAssign("operator^=", true, true),
        OperatorLShift("operator<<", true, true),
        OperatorLogicNot("operator!", true, true),
        OperatorEquals("operator==", true, true),
        OperatorDifferent("operator!=", true, true),
        OperatorSquareBrackets("operator[]", true, true),
        OperatorCast("'some cast operator'", true, true),
        OperatorArrow("operator->", true, true),
        OperatorMultiply("operator*", true, true),
        OperatorIncrement("operator++", true, true),
        OperatorDecrement("operator--", true, true),
        OperatorSubstract("operator-", true, true),
        OperatorAdd("operator+", true, true),
        OperatorBitAnd("operator&=", true, true),
        OperatorArrowStar("operator->*", true, true),
        OperatorDivide("operator/", true, true),
        OperatorModulo("operator%", true, true),
        OperatorLower("operator<", true, true),
        OperatorLowerEquals("operator<=", true, true),
        OperatorGreater("operator>", true, true),
        OperatorGreaterEquals("operator>=", true, true),
        OperatorComma("operator,", true, true),
        OperatorParenthesis("operator()", true, true),
        OperatorBitNot("operator~", true, true),
        OperatorXOR("operator^", true, true),
        OperatorBitOr("operator|", true, true),
        OperatorLogicAnd("operator&&", true, true),
        OperatorLogicOr("operator||", true, true),
        OperatorMultiplyAssign("operator*=", true, true),
        OperatorAddAssign("operator+=", true, true),
        OperatorSubstractAssign("operator-=", true, true);

        final String name;
        final boolean isFunction;
        final boolean isMember;

        private SpecialName(String string2, boolean bl, boolean bl2) {
            this.name = string2;
            this.isFunction = bl;
            this.isMember = bl2;
        }

        public final String toString() {
            return this.name;
        }

        public final boolean isFunction() {
            return this.isFunction;
        }

        public final boolean isMember() {
            return this.isMember;
        }
    }

    public static class FunctionTypeRef
    extends TypeRef {
        MemberRef function;

        public FunctionTypeRef(MemberRef memberRef) {
            this.function = memberRef;
        }

        public StringBuilder getQualifiedName(StringBuilder stringBuilder, boolean bl) {
            return null;
        }

        public boolean matches(Type genericDeclaration, Annotations annotations) {
            if (!Callback.class.isAssignableFrom((Class<?>)(genericDeclaration = Demangler.getTypeClass(genericDeclaration)))) {
                return false;
            }
            genericDeclaration = CRuntime.getInstance().getFastestCallbackMethod((Class)genericDeclaration);
            if (genericDeclaration == null) {
                return false;
            }
            return this.function.matches((Method)genericDeclaration);
        }

        public String toString() {
            return this.function.toString();
        }
    }

    public static class ClassRef
    extends TypeRef {
        private TypeRef enclosingType;
        final Ident ident;

        public ClassRef(Ident ident) {
            this.ident = ident;
        }

        public StringBuilder getQualifiedName(StringBuilder stringBuilder, boolean bl) {
            if (this.getEnclosingType() instanceof ClassRef) {
                this.getEnclosingType().getQualifiedName(stringBuilder, bl).append('$');
            } else if (this.getEnclosingType() instanceof NamespaceRef) {
                this.getEnclosingType().getQualifiedName(stringBuilder, bl).append('.');
            }
            stringBuilder.append(this.ident.simpleName);
            if (bl && this.ident.templateArguments != null) {
                int n2 = 0;
                for (TemplateArg templateArg : this.ident.templateArguments) {
                    if (!(templateArg instanceof TypeRef)) continue;
                    if (n2 == 0) {
                        stringBuilder.append('<');
                    } else {
                        stringBuilder.append(", ");
                    }
                    ((TypeRef)templateArg).getQualifiedName(stringBuilder, bl);
                    ++n2;
                }
                if (n2 > 0) {
                    stringBuilder.append('>');
                }
            }
            return stringBuilder;
        }

        public Ident getIdent() {
            return this.ident;
        }

        public void setEnclosingType(TypeRef typeRef) {
            this.enclosingType = typeRef;
        }

        public TypeRef getEnclosingType() {
            return this.enclosingType;
        }

        public boolean matches(Type object, Annotations clazz) {
            clazz = Demangler.getTypeClass((Type)object);
            if (clazz == null) {
                return false;
            }
            object = Demangler.getFullClassName((Type)(ValuedEnum.class.isAssignableFrom(clazz) ? Demangler.normalize(Utils.getUniqueParameterizedTypeParameter((Type)object)) : clazz));
            clazz = this.getQualifiedName(false);
            return object != null && ((String)object).equals(clazz);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            if (this.enclosingType != null) {
                stringBuilder.append(this.enclosingType).append('.');
            }
            stringBuilder.append(this.ident);
            return stringBuilder.toString();
        }
    }

    public static class Ident
    implements IdentLike {
        Object simpleName;
        TemplateArg[] templateArguments;

        public Ident(String string, TemplateArg ... templateArgArray) {
            this.simpleName = string;
            this.templateArguments = templateArgArray;
        }

        public boolean equals(Object object) {
            if (object == null || !(object instanceof Ident)) {
                return false;
            }
            object = (Ident)object;
            if (!this.simpleName.equals(((Ident)object).simpleName)) {
                return false;
            }
            int n2 = this.templateArguments.length;
            if (((Ident)object).templateArguments.length != n2) {
                return false;
            }
            for (int i2 = 0; i2 < n2; ++i2) {
                if (this.templateArguments[i2].equals(((Ident)object).templateArguments[i2])) continue;
                return false;
            }
            return true;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.simpleName);
            Demangler.appendTemplateArgs(stringBuilder, this.templateArguments);
            return stringBuilder.toString();
        }
    }

    public static interface IdentLike {
    }

    public static class JavaTypeRef
    extends TypeRef {
        Type type;
        Class<? extends Annotation>[] annotations;

        public StringBuilder getQualifiedName(StringBuilder stringBuilder, boolean bl) {
            return stringBuilder.append(Demangler.getFullClassName(this.type));
        }

        public boolean matches(Type type, Annotations annotations) {
            Class<?> clazz = Demangler.getTypeClass(this.type);
            Class<?> clazz2 = Demangler.getTypeClass(type);
            if (clazz2 == null) {
                return true;
            }
            if (clazz == clazz2 || clazz.equals(clazz2)) {
                return true;
            }
            if (type == Long.TYPE && Pointer.class.isAssignableFrom(clazz) || Pointer.class.isAssignableFrom(clazz2) && clazz == Long.TYPE) {
                return true;
            }
            return Demangler.equivalentTypes(type, clazz2, annotations, this.type, clazz, null);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            Class<? extends Annotation>[] classArray = this.annotations;
            int n2 = this.annotations.length;
            for (int i2 = 0; i2 < n2; ++i2) {
                Class<? extends Annotation> clazz = classArray[i2];
                stringBuilder.append(clazz.getSimpleName()).append(' ');
            }
            stringBuilder.append(this.type instanceof Class ? ((Class)this.type).getSimpleName() : this.type.toString());
            return stringBuilder.toString();
        }
    }

    public static class PointerTypeRef
    extends TypeRef {
        public TypeRef pointedType;
        public boolean isReference;
        public boolean isConst;

        public PointerTypeRef(TypeRef typeRef, boolean bl, boolean bl2) {
            assert (typeRef != null);
            this.pointedType = typeRef;
            this.isReference = bl2;
            this.isConst = bl;
        }

        public StringBuilder getQualifiedName(StringBuilder stringBuilder, boolean bl) {
            return stringBuilder.append("org.bridj.Pointer");
        }

        public String toString() {
            return (this.isConst ? "const " : "") + this.pointedType + (this.isReference ? "&" : "*");
        }

        public boolean matches(Type type, Annotations annotations) {
            if (super.matches(type, annotations)) {
                return true;
            }
            if (type == Long.TYPE && annotations.isAnnotationPresent(Ptr.class)) {
                return true;
            }
            Class<?> clazz = Demangler.getTypeClass(type);
            if (!Pointer.class.isAssignableFrom(clazz)) {
                return false;
            }
            type = Demangler.normalize(Utils.getUniqueParameterizedTypeParameter(type));
            if (this.pointedType == null || this.pointedType.toString().equals("void")) {
                return type == null;
            }
            if (type == null) {
                return true;
            }
            return this.pointedType.matches(type, annotations);
        }
    }

    public static class NamespaceRef
    extends TypeRef {
        Object[] namespace;

        public NamespaceRef(Object ... objectArray) {
            this.namespace = objectArray;
        }

        public StringBuilder getQualifiedName(StringBuilder stringBuilder, boolean bl) {
            return Demangler.implode(stringBuilder, this.namespace, ".");
        }

        public String toString() {
            return ((TypeRef)this).getQualifiedName(new StringBuilder(), true).toString();
        }
    }

    public static class Constant
    implements TemplateArg {
        Object value;

        public Constant(Object object) {
            this.value = object;
        }

        public boolean matchesParam(Object object, Annotations annotations) {
            return this.value.equals(object);
        }

        public String toString() {
            return this.value.toString();
        }
    }

    public static abstract class TypeRef
    implements TemplateArg {
        public abstract StringBuilder getQualifiedName(StringBuilder var1, boolean var2);

        public String getQualifiedName(boolean bl) {
            StringBuilder stringBuilder = this.getQualifiedName(new StringBuilder(), bl);
            if (stringBuilder == null) {
                return null;
            }
            return stringBuilder.toString();
        }

        public boolean matchesParam(Object object, Annotations annotations) {
            return object instanceof Type && this.matches((Type)object, annotations);
        }

        public boolean matches(Type object, Annotations object2) {
            object2 = this.getQualifiedName(false);
            if (object2 == null) {
                return false;
            }
            String string = Demangler.getClassName(object = Demangler.getTypeClass((Type)object));
            if (((String)object2).equals(string)) {
                return true;
            }
            Namespace namespace = ((Class)object).getAnnotation(Namespace.class);
            object = namespace == null ? (((Class)object).getPackage() == null ? "" : ((Class)object).getPackage().getName()) : namespace.value().replaceAll("\b::\b", ".").trim();
            if (((String)object).length() == 0) {
                return false;
            }
            return ((String)object2).matches("\b" + Pattern.quote((String)(object = (String)object + "." + string)));
        }

        public boolean equals(Object object) {
            return this.toString().equals(object.toString());
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static class Symbol {
        final String symbol;
        long address;
        MemberRef ref;
        boolean refParsed;
        final NativeLibrary library;
        private Convention.Style style;

        public Symbol(String string, NativeLibrary nativeLibrary) {
            this.symbol = string;
            this.library = nativeLibrary;
        }

        public NativeLibrary getLibrary() {
            return this.library;
        }

        public MemberRef getRef() {
            return this.ref;
        }

        public Convention.Style getStyle() {
            return this.style;
        }

        public String getSymbol() {
            return this.symbol;
        }

        public String toString() {
            return this.symbol + (this.ref == null ? "" : " (" + this.ref + ")");
        }

        public long getAddress() {
            if (this.address == 0L) {
                this.address = this.library.getSymbolAddress(this.symbol);
            }
            return this.address;
        }

        public void setAddress(long l2) {
            this.address = l2;
        }

        public Convention.Style getInferredCallingConvention() {
            if (this.style == null) {
                if (this.symbol.matches("_.*?@\\d+")) {
                    this.style = Convention.Style.StdCall;
                } else if (this.symbol.matches("@.*?@\\d+")) {
                    this.style = Convention.Style.FastCall;
                } else if (Platform.isWindows() && this.symbol.contains("@")) {
                    try {
                        MemberRef memberRef = this.getParsedRef();
                        if (memberRef != null) {
                            this.style = memberRef.callingConvention;
                        }
                    }
                    catch (Throwable throwable) {}
                }
            }
            return this.style;
        }

        public boolean matches(Method method) {
            if (!this.symbol.contains(Demangler.getMethodName(method))) {
                return false;
            }
            this.parse();
            try {
                if (this.ref != null) {
                    boolean bl = this.ref.matches(method);
                    if (!bl && BridJ.debug) {
                        BridJ.debug("Symbol " + this.symbol + " was a good candidate but expected demangled signature " + this.ref + " did not match the method " + method);
                    }
                    return bl;
                }
            }
            catch (Exception exception) {
                Exception exception2 = exception;
                exception.printStackTrace();
            }
            return false;
        }

        public MemberRef getParsedRef() {
            this.parse();
            return this.ref;
        }

        synchronized void parse() {
            if (!this.refParsed) {
                block4: {
                    try {
                        this.ref = this.library.parseSymbol(this.symbol);
                    }
                    catch (DemanglingException demanglingException) {
                        if (BridJ.debug) {
                            demanglingException.printStackTrace();
                        }
                        if (!BridJ.verbose) break block4;
                        BridJ.warning("Symbol parsing failed : " + demanglingException.getMessage());
                    }
                }
                this.refParsed = true;
            }
        }

        public String getName() {
            return this.symbol;
        }

        public boolean matchesVirtualTable(Class<?> serializable) {
            if (!this.symbol.contains(serializable.getSimpleName())) {
                return false;
            }
            this.parse();
            try {
                if (this.ref != null) {
                    return this.ref.matchesVirtualTable((Type)((Object)serializable));
                }
            }
            catch (Exception exception) {
                serializable = exception;
                exception.printStackTrace();
            }
            return false;
        }

        public boolean matchesConstructor(Type object, Constructor<?> constructor) {
            if (!this.symbol.contains(Demangler.getClassName((Type)object))) {
                return false;
            }
            this.parse();
            try {
                if (this.ref != null) {
                    return this.ref.matchesConstructor((Type)object, constructor);
                }
            }
            catch (Exception exception) {
                object = exception;
                exception.printStackTrace();
            }
            return false;
        }

        public boolean matchesDestructor(Class<?> serializable) {
            if (!this.symbol.contains(serializable.getSimpleName())) {
                return false;
            }
            this.parse();
            try {
                if (this.ref != null) {
                    return this.ref.matchesDestructor((Type)((Object)serializable));
                }
            }
            catch (Exception exception) {
                serializable = exception;
                exception.printStackTrace();
            }
            return false;
        }

        public boolean isVirtualTable() {
            return false;
        }
    }

    public static interface TemplateArg {
        public boolean matchesParam(Object var1, Annotations var2);
    }

    public class DemanglingException
    extends Exception {
        public DemanglingException(String string) {
            this(string, null);
        }

        public DemanglingException(String string, Throwable throwable) {
            super(string + " (in symbol '" + Demangler.this.str + "')", throwable);
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static interface Annotations {
        public <A extends Annotation> A getAnnotation(Class<A> var1);

        public boolean isAnnotationPresent(Class<? extends Annotation> var1);
    }
}


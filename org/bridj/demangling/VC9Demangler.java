/*
 * Decompiled with CFR 0.152.
 */
package org.bridj.demangling;

import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bridj.CLong;
import org.bridj.NativeLibrary;
import org.bridj.ann.Convention;
import org.bridj.demangling.Demangler;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class VC9Demangler
extends Demangler {
    List<Demangler.TypeRef> allQualifiedNames = new ArrayList<Demangler.TypeRef>();

    public VC9Demangler(NativeLibrary nativeLibrary, String string) {
        super(nativeLibrary, string);
    }

    private AccessLevelAndStorageClass parseAccessLevelAndStorageClass() throws Demangler.DemanglingException {
        AccessLevelAndStorageClass accessLevelAndStorageClass = new AccessLevelAndStorageClass();
        switch (this.consumeChar()) {
            case 'A': 
            case 'B': {
                accessLevelAndStorageClass.modifiers = 2;
                break;
            }
            case 'C': 
            case 'D': {
                accessLevelAndStorageClass.modifiers = 10;
                break;
            }
            case 'E': 
            case 'F': {
                accessLevelAndStorageClass.modifiers = 2;
                accessLevelAndStorageClass.isVirtual = true;
                break;
            }
            case 'G': 
            case 'H': {
                accessLevelAndStorageClass.modifiers = 2;
                accessLevelAndStorageClass.isThunk = true;
                break;
            }
            case 'I': 
            case 'J': {
                accessLevelAndStorageClass.modifiers = 4;
                break;
            }
            case 'K': 
            case 'L': {
                accessLevelAndStorageClass.modifiers = 12;
                break;
            }
            case 'M': 
            case 'N': {
                accessLevelAndStorageClass.modifiers = 4;
                accessLevelAndStorageClass.isVirtual = true;
                break;
            }
            case 'O': 
            case 'P': {
                accessLevelAndStorageClass.modifiers = 4;
                accessLevelAndStorageClass.isThunk = true;
                break;
            }
            case 'Q': 
            case 'R': {
                accessLevelAndStorageClass.modifiers = 1;
                break;
            }
            case 'S': 
            case 'T': {
                accessLevelAndStorageClass.modifiers = 9;
                break;
            }
            case 'U': 
            case 'V': {
                accessLevelAndStorageClass.modifiers = 1;
                accessLevelAndStorageClass.isVirtual = true;
                break;
            }
            case 'W': 
            case 'X': {
                accessLevelAndStorageClass.modifiers = 1;
                accessLevelAndStorageClass.isThunk = true;
                break;
            }
            case 'Y': 
            case 'Z': {
                accessLevelAndStorageClass.modifiers = 0;
                break;
            }
            default: {
                throw this.error("Unknown access level + storage class");
            }
        }
        return accessLevelAndStorageClass;
    }

    private Demangler.ClassRef parseTemplateType() throws Demangler.DemanglingException {
        Object object = this.parseNameFragment();
        List<Demangler.TemplateArg> list2 = this.parseTemplateParams();
        List<Object> list3 = this.parseNameQualifications();
        List<Demangler.TemplateArg> list4 = list2;
        object = new Demangler.ClassRef(new Demangler.Ident((String)object, list4.toArray(new Demangler.TemplateArg[list4.size()])));
        ((Demangler.ClassRef)object).setEnclosingType(VC9Demangler.reverseNamespace(list3));
        this.addBackRef((Demangler.TypeRef)object);
        return object;
    }

    private void parseFunctionProperty(Demangler.MemberRef memberRef) throws Demangler.DemanglingException {
        List<Demangler.TypeRef> list2;
        memberRef.callingConvention = this.parseCallingConvention();
        Demangler.TypeRef typeRef = this.consumeCharIf('@') ? VC9Demangler.classType(Void.TYPE, new Class[0]) : this.parseType(true);
        List<Demangler.TypeRef> list3 = list2 = this.parseParams();
        memberRef.paramTypes = list3.toArray(new Demangler.TypeRef[list3.size()]);
        if (!this.consumeCharIf('Z')) {
            List<Demangler.TypeRef> list4 = list2 = this.parseParams();
            memberRef.throwTypes = list4.toArray(new Demangler.TypeRef[list4.size()]);
        }
        memberRef.setValueType(typeRef);
    }

    private Demangler.TemplateArg parseTemplateParameter() throws Demangler.DemanglingException {
        switch (this.peekChar()) {
            case '?': {
                this.consumeChar();
                return new AnonymousTemplateArg("'anonymous template param " + this.parseNumber(false) + "'");
            }
            case '$': {
                this.consumeChar();
                switch (this.consumeChar()) {
                    case '0': {
                        return new Demangler.Constant(this.parseNumber(true));
                    }
                    case '2': {
                        int n2 = this.parseNumber(true);
                        int n3 = this.parseNumber(true);
                        return new Demangler.Constant((double)n2 * Math.exp(10 * (int)Math.log((double)n3 - Math.log10(n2) + 1.0)));
                    }
                    case 'D': {
                        return new AnonymousTemplateArg("'anonymous template param " + this.parseNumber(false) + "'");
                    }
                    case 'F': {
                        return new AnonymousTemplateArg("'tuple (" + this.parseNumber(true) + ", " + this.parseNumber(true) + ")'");
                    }
                    case 'G': {
                        return new AnonymousTemplateArg("'tuple (" + this.parseNumber(true) + ", " + this.parseNumber(true) + ", " + this.parseNumber(true) + ")'");
                    }
                    case 'Q': {
                        return new AnonymousTemplateArg("'anonymous non-type template param " + this.parseNumber(false) + "'");
                    }
                }
            }
        }
        return this.parseType(true);
    }

    @Override
    public Demangler.MemberRef parseSymbol() throws Demangler.DemanglingException {
        Object object;
        Demangler.MemberRef memberRef = new Demangler.MemberRef();
        int n2 = this.str.indexOf(64);
        if (n2 >= 0 && this.consumeCharIf('_') && n2 > 0) {
            memberRef.setMemberName(new Demangler.Ident(this.str.substring(1, n2), new Demangler.TemplateArg[0]));
            memberRef.setArgumentsStackSize(Integer.parseInt(this.str.substring(n2 + 1)));
            return memberRef;
        }
        if (!this.consumeCharIf('@', '?')) {
            return null;
        }
        Object object2 = this.parseFirstQualifiedTypeNameComponent();
        if (object2 instanceof Demangler.SpecialName && !((Demangler.SpecialName)(object = (Demangler.SpecialName)object2)).isFunction()) {
            return null;
        }
        memberRef.setMemberName((Demangler.IdentLike)object2);
        object = this.parseNameQualifications();
        AccessLevelAndStorageClass accessLevelAndStorageClass = this.parseAccessLevelAndStorageClass();
        CVClassModifier cVClassModifier = null;
        if (accessLevelAndStorageClass.modifiers != 0 && !Modifier.isStatic(accessLevelAndStorageClass.modifiers)) {
            cVClassModifier = this.parseCVClassModifier();
        }
        if (cVClassModifier != null && (cVClassModifier.isMember || object2 instanceof Demangler.SpecialName || Modifier.isPublic(accessLevelAndStorageClass.modifiers))) {
            object2 = object.get(0);
            object2 = object2 instanceof Demangler.ClassRef ? (Demangler.ClassRef)object2 : new Demangler.ClassRef(new Demangler.Ident((String)object2, new Demangler.TemplateArg[0]));
            object.remove(0);
            ((Demangler.ClassRef)object2).setEnclosingType(VC9Demangler.reverseNamespace((List<Object>)object));
        } else {
            object2 = VC9Demangler.reverseNamespace((List<Object>)object);
        }
        this.addBackRef((Demangler.TypeRef)object2);
        memberRef.setEnclosingType((Demangler.TypeRef)object2);
        this.parseFunctionProperty(memberRef);
        if (this.position != this.length) {
            this.error("Failed to demangle the whole symbol");
        }
        return memberRef;
    }

    Demangler.TypeRef parseReturnType() throws Demangler.DemanglingException {
        Demangler.TypeRef typeRef = this.parseType(true);
        return typeRef;
    }

    int parseNumber(boolean n2) throws Demangler.DemanglingException {
        char c2;
        int n3 = n2 = n2 != 0 && this.consumeCharIf('?') ? -1 : 1;
        if (Character.isDigit(this.peekChar())) {
            char c3 = this.consumeChar();
            return n2 * (c3 - 48);
        }
        if (this.peekChar() == '@') {
            return 0;
        }
        StringBuilder stringBuilder = new StringBuilder();
        while ((c2 = this.consumeChar()) >= 'A' && c2 <= 'P' && c2 != '@') {
        }
        String string = stringBuilder.toString().trim();
        if (c2 != '@' || string.length() == 0) {
            throw this.error("Expected a number here", -stringBuilder.length());
        }
        return n2 * Integer.parseInt(string, 16);
    }

    Demangler.TypeRef consumeIfBackRef() throws Demangler.DemanglingException {
        int n2 = this.peekChar();
        if (Character.isDigit((char)n2)) {
            this.consumeChar();
            return this.getBackRef(n2 -= 48);
        }
        return null;
    }

    Demangler.TypeRef parseType(boolean bl) throws Demangler.DemanglingException {
        Demangler.TypeRef typeRef = this.consumeIfBackRef();
        if (typeRef != null) {
            return typeRef;
        }
        char bl2 = this.consumeChar();
        switch (bl2) {
            case '_': {
                Demangler.TypeRef typeRef2;
                switch (this.consumeChar()) {
                    case 'D': 
                    case 'E': {
                        typeRef2 = VC9Demangler.classType(Byte.TYPE, new Class[0]);
                        break;
                    }
                    case 'F': 
                    case 'G': {
                        typeRef2 = VC9Demangler.classType(Short.TYPE, new Class[0]);
                        break;
                    }
                    case 'H': 
                    case 'I': {
                        typeRef2 = VC9Demangler.classType(Integer.TYPE, new Class[0]);
                        break;
                    }
                    case 'J': 
                    case 'K': {
                        typeRef2 = VC9Demangler.classType(Long.TYPE, new Class[0]);
                        break;
                    }
                    case 'L': {
                        typeRef2 = VC9Demangler.classType(BigInteger.class, new Class[0]);
                        break;
                    }
                    case 'N': {
                        typeRef2 = VC9Demangler.classType(Boolean.TYPE, new Class[0]);
                        break;
                    }
                    case '0': {
                        this.parseCVClassModifier();
                        this.parseType(false);
                        typeRef2 = VC9Demangler.classType(Object[].class, new Class[0]);
                        break;
                    }
                    case 'W': {
                        typeRef2 = VC9Demangler.classType(Character.TYPE, new Class[0]);
                        break;
                    }
                    default: {
                        throw this.error(-1);
                    }
                }
                this.addBackRef(typeRef2);
                return typeRef2;
            }
            case 'Z': {
                return VC9Demangler.classType(Object[].class, new Class[0]);
            }
            case 'O': {
                throw this.error("'long double' type cannot be mapped !", -1);
            }
            case 'C': 
            case 'D': 
            case 'E': {
                return VC9Demangler.classType(Byte.TYPE, new Class[0]);
            }
            case 'F': 
            case 'G': {
                return VC9Demangler.classType(Short.TYPE, new Class[0]);
            }
            case 'H': 
            case 'I': {
                return VC9Demangler.classType(Integer.TYPE, new Class[0]);
            }
            case 'J': 
            case 'K': {
                return VC9Demangler.classType(CLong.class, new Class[0]);
            }
            case 'M': {
                return VC9Demangler.classType(Float.TYPE, new Class[0]);
            }
            case 'N': {
                return VC9Demangler.classType(Double.TYPE, new Class[0]);
            }
            case 'Y': {
                throw this.error("TODO handle cointerfaces", -1);
            }
            case 'X': {
                if (!bl) {
                    return null;
                }
                return VC9Demangler.classType(Void.TYPE, new Class[0]);
            }
            case '?': {
                this.parseCVClassModifier();
                return this.parseType(bl);
            }
            case 'A': 
            case 'B': 
            case 'P': 
            case 'Q': 
            case 'R': 
            case 'S': {
                Demangler.TypeRef typeRef3;
                boolean bl3;
                bl = bl2 == 'A' || bl2 == 'B';
                boolean bl4 = bl3 = bl2 == 'Q' || bl2 == 'S';
                if (!this.consumeCharsIf('$', 'A')) {
                    this.consumeCharsIf('$', 'B');
                }
                Object n2 = this.parseCVClassModifier();
                if (((CVClassModifier)n2).isVariable) {
                    if (this.consumeCharIf('Y')) {
                        int clazz = this.parseNumber(false);
                        int[] typeRef4 = new int[clazz];
                        for (int i2 = 0; i2 < clazz; ++i2) {
                            typeRef4[i2] = this.parseNumber(false);
                        }
                    }
                    typeRef3 = VC9Demangler.pointerType(this.parseType(true), bl3, bl);
                } else {
                    n2 = new Demangler.MemberRef();
                    this.parseFunctionProperty((Demangler.MemberRef)n2);
                    typeRef3 = VC9Demangler.pointerType(new Demangler.FunctionTypeRef((Demangler.MemberRef)n2), bl3, bl);
                }
                this.addBackRef(typeRef3);
                return typeRef3;
            }
            case 'T': 
            case 'U': 
            case 'V': {
                return this.parseQualifiedTypeName();
            }
            case 'W': {
                Class<Number> clazz;
                switch (this.consumeChar()) {
                    case '0': 
                    case '1': {
                        clazz = Byte.TYPE;
                        break;
                    }
                    case '2': 
                    case '3': {
                        clazz = Short.TYPE;
                        break;
                    }
                    case '4': 
                    case '5': {
                        clazz = Integer.TYPE;
                        break;
                    }
                    case '6': 
                    case '7': {
                        clazz = Integer.TYPE;
                        break;
                    }
                    default: {
                        throw this.error("Unfinished enum", -1);
                    }
                }
                Demangler.TypeRef typeRef2 = this.parseQualifiedTypeName();
                this.addBackRef(typeRef2);
                return VC9Demangler.classType(clazz, new Class[0]);
            }
        }
        throw this.error(-1);
    }

    static Demangler.NamespaceRef reverseNamespace(List<Object> list2) {
        if (list2 == null || list2.isEmpty()) {
            return null;
        }
        Collections.reverse(list2);
        return new Demangler.NamespaceRef(list2.toArray());
    }

    <T> T withEmptyQualifiedNames(DemanglingOp<T> demanglingOp) throws Demangler.DemanglingException {
        List<Demangler.TypeRef> list2 = this.allQualifiedNames;
        try {
            this.allQualifiedNames = new ArrayList<Demangler.TypeRef>();
            demanglingOp = demanglingOp.run();
            return (T)demanglingOp;
        }
        finally {
            this.allQualifiedNames = list2;
        }
    }

    Demangler.IdentLike parseFirstQualifiedTypeNameComponent() throws Demangler.DemanglingException {
        if (this.consumeCharIf('?')) {
            if (this.consumeCharIf('$')) {
                return this.parseTemplateType().getIdent();
            }
            return this.parseSpecialName();
        }
        return new Demangler.Ident(this.parseNameFragment(), new Demangler.TemplateArg[0]);
    }

    Demangler.TypeRef parseQualifiedTypeName() throws Demangler.DemanglingException {
        Object object = this.consumeIfBackRef();
        if (object != null) {
            return object;
        }
        this.peekChar();
        object = this.parseNameQualifications();
        Object object2 = object.get(0);
        object.set(0, object2 instanceof String ? new Demangler.Ident((String)object2, new Demangler.TemplateArg[0]) : ((Demangler.ClassRef)object2).getIdent());
        if (object.size() == 1 && object.get(0) instanceof Demangler.TypeRef) {
            return (Demangler.TypeRef)object.get(0);
        }
        object2 = new Demangler.ClassRef((Demangler.Ident)object.get(0));
        object.remove(0);
        ((Demangler.ClassRef)object2).setEnclosingType(VC9Demangler.reverseNamespace((List<Object>)object));
        return object2;
    }

    public Demangler.IdentLike parseSpecialName() throws Demangler.DemanglingException {
        switch (this.consumeChar()) {
            case '0': {
                return Demangler.SpecialName.Constructor;
            }
            case '1': {
                return Demangler.SpecialName.Destructor;
            }
            case '2': {
                return Demangler.SpecialName.New;
            }
            case '3': {
                return Demangler.SpecialName.Delete;
            }
            case '4': {
                return Demangler.SpecialName.OperatorAssign;
            }
            case '5': {
                return Demangler.SpecialName.OperatorRShift;
            }
            case '6': {
                return Demangler.SpecialName.OperatorLShift;
            }
            case '7': {
                return Demangler.SpecialName.OperatorLogicNot;
            }
            case '8': {
                return Demangler.SpecialName.OperatorEquals;
            }
            case '9': {
                return Demangler.SpecialName.OperatorDifferent;
            }
            case 'A': {
                return Demangler.SpecialName.OperatorSquareBrackets;
            }
            case 'B': {
                return Demangler.SpecialName.OperatorCast;
            }
            case 'C': {
                return Demangler.SpecialName.OperatorArrow;
            }
            case 'D': {
                return Demangler.SpecialName.OperatorMultiply;
            }
            case 'E': {
                return Demangler.SpecialName.OperatorIncrement;
            }
            case 'F': {
                return Demangler.SpecialName.OperatorDecrement;
            }
            case 'G': {
                return Demangler.SpecialName.OperatorSubstract;
            }
            case 'H': {
                return Demangler.SpecialName.OperatorAdd;
            }
            case 'I': {
                return Demangler.SpecialName.OperatorBitAnd;
            }
            case 'J': {
                return Demangler.SpecialName.OperatorArrowStar;
            }
            case 'K': {
                return Demangler.SpecialName.OperatorDivide;
            }
            case 'L': {
                return Demangler.SpecialName.OperatorModulo;
            }
            case 'M': {
                return Demangler.SpecialName.OperatorLower;
            }
            case 'N': {
                return Demangler.SpecialName.OperatorLowerEquals;
            }
            case 'O': {
                return Demangler.SpecialName.OperatorGreater;
            }
            case 'P': {
                return Demangler.SpecialName.OperatorGreaterEquals;
            }
            case 'Q': {
                return Demangler.SpecialName.OperatorComma;
            }
            case 'R': {
                return Demangler.SpecialName.OperatorParenthesis;
            }
            case 'S': {
                return Demangler.SpecialName.OperatorBitNot;
            }
            case 'T': {
                return Demangler.SpecialName.OperatorXOR;
            }
            case 'U': {
                return Demangler.SpecialName.OperatorBitOr;
            }
            case 'V': {
                return Demangler.SpecialName.OperatorLogicAnd;
            }
            case 'W': {
                return Demangler.SpecialName.OperatorLogicOr;
            }
            case 'X': {
                return Demangler.SpecialName.OperatorMultiplyAssign;
            }
            case 'Y': {
                return Demangler.SpecialName.OperatorAddAssign;
            }
            case 'Z': {
                return Demangler.SpecialName.OperatorSubstractAssign;
            }
            case '_': {
                switch (this.consumeChar()) {
                    case '0': {
                        return Demangler.SpecialName.OperatorDivideAssign;
                    }
                    case '1': {
                        return Demangler.SpecialName.OperatorModuloAssign;
                    }
                    case '2': {
                        return Demangler.SpecialName.OperatorLShiftAssign;
                    }
                    case '3': {
                        return Demangler.SpecialName.OperatorRShiftAssign;
                    }
                    case '4': {
                        return Demangler.SpecialName.OperatorBitAndAssign;
                    }
                    case '5': {
                        return Demangler.SpecialName.OperatorBitOrAssign;
                    }
                    case '6': {
                        return Demangler.SpecialName.OperatorXORAssign;
                    }
                    case '7': {
                        return Demangler.SpecialName.VFTable;
                    }
                    case '8': {
                        return Demangler.SpecialName.VBTable;
                    }
                    case '9': {
                        return Demangler.SpecialName.VCall;
                    }
                    case 'E': {
                        return Demangler.SpecialName.VectorDeletingDestructor;
                    }
                    case 'G': {
                        return Demangler.SpecialName.ScalarDeletingDestructor;
                    }
                }
                throw this.error("unhandled extended special name");
            }
        }
        throw this.error("Invalid special name");
    }

    private List<Demangler.TypeRef> parseParams() throws Demangler.DemanglingException {
        ArrayList<Demangler.TypeRef> arrayList = new ArrayList<Demangler.TypeRef>();
        if (!this.consumeCharIf('X')) {
            char c2;
            while ((c2 = this.peekChar()) != '@' && c2 != '\u0000' && (c2 != 'Z' || this.peekChar(2) == 'Z')) {
                Demangler.TypeRef typeRef = this.parseType(false);
                if (typeRef == null) continue;
                arrayList.add(typeRef);
            }
            if (c2 == 'Z') {
                this.consumeChar();
            }
            if (c2 == '@') {
                this.consumeChar();
            }
        }
        return arrayList;
    }

    private List<Demangler.TemplateArg> parseTemplateParams() throws Demangler.DemanglingException {
        return this.withEmptyQualifiedNames(new DemanglingOp<List<Demangler.TemplateArg>>(){

            @Override
            public List<Demangler.TemplateArg> run() throws Demangler.DemanglingException {
                ArrayList<Demangler.TemplateArg> arrayList = new ArrayList<Demangler.TemplateArg>();
                if (!VC9Demangler.this.consumeCharIf('X')) {
                    char c2;
                    while ((c2 = VC9Demangler.this.peekChar()) != '@' && c2 != '\u0000') {
                        Demangler.TemplateArg templateArg = VC9Demangler.this.parseTemplateParameter();
                        if (templateArg == null) continue;
                        arrayList.add(templateArg);
                    }
                }
                return arrayList;
            }
        });
    }

    String parseNameFragment() throws Demangler.DemanglingException {
        char c2;
        CharSequence charSequence = new StringBuilder();
        while ((c2 = this.consumeChar()) != '@') {
            charSequence.append(c2);
        }
        if (charSequence.length() == 0) {
            throw new Demangler.DemanglingException("Unexpected empty name fragment");
        }
        charSequence = charSequence.toString();
        return charSequence;
    }

    void addBackRef(Demangler.TypeRef typeRef) {
        if (typeRef == null || this.allQualifiedNames.contains(typeRef)) {
            return;
        }
        this.allQualifiedNames.add(typeRef);
    }

    Demangler.TypeRef getBackRef(int n2) throws Demangler.DemanglingException {
        if (n2 == this.allQualifiedNames.size()) {
            --n2;
        }
        if (n2 < 0 || n2 >= this.allQualifiedNames.size()) {
            throw this.error("Invalid back references in name qualifications", -1);
        }
        return this.allQualifiedNames.get(n2);
    }

    private List<Object> parseNameQualifications() throws Demangler.DemanglingException {
        ArrayList<Object> arrayList = new ArrayList<Object>();
        if (Character.isDigit(this.peekChar())) {
            try {
                int n2 = this.consumeChar() - 48;
                arrayList.add(this.getBackRef(n2));
                this.expectChars('@');
                return arrayList;
            }
            catch (Exception exception) {
                throw this.error("Invalid back references in name qualifications", -1);
            }
        }
        while (this.peekChar() != '@') {
            arrayList.add(this.parseNameQualification());
        }
        this.expectChars('@');
        return arrayList;
    }

    Object parseNameQualification() throws Demangler.DemanglingException {
        if (this.consumeCharIf('?')) {
            if (this.consumeCharIf('$')) {
                return this.parseTemplateType();
            }
            if (this.peekChar() == 'A') {
                throw this.error("Anonymous numbered namespaces not handled yet");
            }
            int n2 = this.parseNumber(false);
            return String.valueOf(n2);
        }
        return this.parseNameFragment();
    }

    Convention.Style parseCallingConvention() throws Demangler.DemanglingException {
        Convention.Style style;
        switch (this.consumeChar()) {
            case 'A': 
            case 'B': {
                style = Convention.Style.CDecl;
                break;
            }
            case 'C': 
            case 'D': {
                style = Convention.Style.Pascal;
                break;
            }
            case 'E': 
            case 'F': {
                style = Convention.Style.ThisCall;
                break;
            }
            case 'G': 
            case 'H': {
                style = Convention.Style.StdCall;
                break;
            }
            case 'I': 
            case 'J': {
                style = Convention.Style.FastCall;
                break;
            }
            case 'K': 
            case 'L': {
                style = null;
                break;
            }
            case 'N': {
                style = Convention.Style.CLRCall;
                break;
            }
            default: {
                throw this.error("Unknown calling convention");
            }
        }
        return style;
    }

    CVClassModifier parseCVClassModifier() throws Demangler.DemanglingException {
        CVClassModifier cVClassModifier = new CVClassModifier();
        switch (this.peekChar()) {
            case 'E': 
            case 'F': 
            case 'I': {
                this.consumeChar();
            }
        }
        block3 : switch (this.consumeChar()) {
            case 'M': 
            case 'N': 
            case 'O': 
            case 'P': {
                cVClassModifier.isBased = true;
            }
            case 'A': 
            case 'B': 
            case 'C': 
            case 'D': 
            case 'G': 
            case 'H': 
            case 'J': 
            case 'K': 
            case 'L': {
                cVClassModifier.isVariable = true;
                cVClassModifier.isMember = false;
                break;
            }
            case '2': 
            case '3': 
            case '4': 
            case '5': {
                cVClassModifier.isBased = true;
            }
            case '0': 
            case '1': 
            case 'Q': 
            case 'R': 
            case 'S': 
            case 'T': 
            case 'U': 
            case 'V': 
            case 'W': 
            case 'X': 
            case 'Y': 
            case 'Z': {
                cVClassModifier.isVariable = true;
                cVClassModifier.isMember = true;
                break;
            }
            case '_': {
                cVClassModifier.isBased = true;
                switch (this.consumeChar()) {
                    case 'A': 
                    case 'B': {
                        cVClassModifier.isVariable = false;
                        break block3;
                    }
                    case 'C': 
                    case 'D': {
                        cVClassModifier.isVariable = false;
                        cVClassModifier.isMember = true;
                        break block3;
                    }
                }
                throw this.error("Unknown extended __based class modifier", -1);
            }
            case '6': 
            case '7': {
                cVClassModifier.isVariable = false;
                cVClassModifier.isMember = false;
                break;
            }
            case '8': 
            case '9': {
                cVClassModifier.isVariable = false;
                cVClassModifier.isMember = true;
                break;
            }
            default: {
                throw this.error("Unknown CV class modifier", -1);
            }
        }
        if (cVClassModifier.isBased) {
            switch (this.consumeChar()) {
                case '0': {
                    break;
                }
                case '2': {
                    this.parseNameQualifications();
                }
            }
        }
        return cVClassModifier;
    }

    static class CVClassModifier {
        boolean isVariable;
        boolean isMember;
        boolean isBased;

        CVClassModifier() {
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    static interface DemanglingOp<T> {
        public T run() throws Demangler.DemanglingException;
    }

    static class AccessLevelAndStorageClass {
        int modifiers;
        boolean isVirtual = false;
        boolean isThunk = false;

        AccessLevelAndStorageClass() {
        }
    }

    static class AnonymousTemplateArg
    implements Demangler.TemplateArg {
        String v;

        public AnonymousTemplateArg(String string) {
            this.v = string;
        }

        public boolean matchesParam(Object object, Demangler.Annotations annotations) {
            return true;
        }

        public String toString() {
            return this.v;
        }
    }
}


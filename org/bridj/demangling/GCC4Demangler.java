/*
 * Decompiled with CFR 0.152.
 */
package org.bridj.demangling;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bridj.CLong;
import org.bridj.NativeLibrary;
import org.bridj.demangling.Demangler;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class GCC4Demangler
extends Demangler {
    private Map<String, List<Demangler.IdentLike>> prefixShortcuts = new HashMap<String, List<Demangler.IdentLike>>(){
        {
            this.put("t", Arrays.asList(new Demangler.Ident("std", new Demangler.TemplateArg[0])));
            this.put("a", Arrays.asList(new Demangler.Ident("std", new Demangler.TemplateArg[0]), new Demangler.Ident("allocator", new Demangler.TemplateArg[0])));
            this.put("b", Arrays.asList(new Demangler.Ident("std", new Demangler.TemplateArg[0]), new Demangler.Ident("basic_string", new Demangler.TemplateArg[0])));
            GCC4Demangler.this = Demangler.classType(Byte.TYPE, new Class[0]);
            Demangler.ClassRef classRef = this.enclosed("std", new Demangler.ClassRef(new Demangler.Ident("char_traits", new Demangler.TemplateArg[]{GCC4Demangler.this})));
            Demangler.ClassRef classRef2 = this.enclosed("std", new Demangler.ClassRef(new Demangler.Ident("allocator", new Demangler.TemplateArg[]{GCC4Demangler.this})));
            this.put("d", Arrays.asList(new Demangler.Ident("std", new Demangler.TemplateArg[0]), new Demangler.Ident("basic_iostream", new Demangler.TemplateArg[]{GCC4Demangler.this, classRef})));
            this.put("i", Arrays.asList(new Demangler.Ident("std", new Demangler.TemplateArg[0]), new Demangler.Ident("basic_istream", new Demangler.TemplateArg[]{GCC4Demangler.this, classRef})));
            this.put("o", Arrays.asList(new Demangler.Ident("std", new Demangler.TemplateArg[0]), new Demangler.Ident("basic_ostream", new Demangler.TemplateArg[]{GCC4Demangler.this, classRef})));
            this.put("s", Arrays.asList(new Demangler.Ident("std", new Demangler.TemplateArg[0]), new Demangler.Ident("basic_string", Demangler.classType(Byte.TYPE, new Class[0]), classRef, classRef2)));
        }

        private Demangler.ClassRef enclosed(String string, Demangler.ClassRef classRef) {
            classRef.setEnclosingType(new Demangler.NamespaceRef(new Demangler.Ident(string, new Demangler.TemplateArg[0])));
            return classRef;
        }
    };
    private Set<String> shouldContinueAfterPrefix = new HashSet<String>(Arrays.asList("t"));
    private Map<String, Demangler.TypeRef> typeShortcuts = new HashMap<String, Demangler.TypeRef>();
    int nextShortcutId = -1;

    public GCC4Demangler(NativeLibrary nativeLibrary, String string) {
        super(nativeLibrary, string);
    }

    private <T> T ensureOfType(Object object, Class<T> clazz) throws Demangler.DemanglingException {
        if (clazz.isInstance(object)) {
            return clazz.cast(object);
        }
        throw new Demangler.DemanglingException("Internal error in demangler: trying to cast to " + clazz.getCanonicalName() + " the object '" + object.toString() + "'");
    }

    private String nextShortcutId() {
        int n2;
        if ((n2 = this.nextShortcutId++) == -1) {
            return "_";
        }
        return Integer.toString(n2, 36).toUpperCase() + "_";
    }

    private Demangler.TypeRef parsePointerType(boolean bl, boolean bl2, boolean bl3) throws Demangler.DemanglingException {
        String string = bl ? this.nextShortcutId() : null;
        Demangler.TypeRef typeRef = this.parseType();
        if (bl) {
            this.typeShortcuts.put(string, typeRef);
        }
        Demangler.TypeRef typeRef2 = GCC4Demangler.pointerType(typeRef, bl2, bl3);
        String string2 = this.nextShortcutId();
        this.typeShortcuts.put(string2, typeRef2);
        return typeRef2;
    }

    public Demangler.TemplateArg parseTemplateArg() throws Demangler.DemanglingException {
        if (this.consumeCharIf('L')) {
            char c2;
            this.parseType();
            StringBuffer stringBuffer = new StringBuffer();
            while (Character.isDigit(c2 = this.peekChar())) {
                this.consumeChar();
                stringBuffer.append(c2);
            }
            this.expectChars('E');
            return new Demangler.Constant(Integer.parseInt(stringBuffer.toString()));
        }
        return this.parseType();
    }

    public Demangler.TypeRef parseType() throws Demangler.DemanglingException {
        if (Character.isDigit(this.peekChar())) {
            GCC4Demangler gCC4Demangler = this;
            Demangler.Ident ident = gCC4Demangler.ensureOfType(gCC4Demangler.parseNonCompoundIdent(), Demangler.Ident.class);
            String string = this.nextShortcutId();
            Demangler.TypeRef typeRef = GCC4Demangler.simpleType(ident);
            this.typeShortcuts.put(string, typeRef);
            return typeRef;
        }
        char c2 = this.consumeChar();
        switch (c2) {
            case 'S': {
                Object object;
                char c3 = this.peekChar();
                int n2 = 0;
                if (Character.isDigit(c3) || Character.isUpperCase(c3) || c3 == '_') {
                    object = "";
                    while ((c2 = this.peekChar()) != '_' && c2 != '\u0000') {
                        object = (String)object + this.consumeChar();
                        ++n2;
                    }
                    if (this.peekChar() == '\u0000') {
                        throw new Demangler.DemanglingException("Encountered a unexpected end in gcc mangler shortcut '" + (String)object + "' " + this.prefixShortcuts.keySet());
                    }
                    object = (String)object + this.consumeChar();
                    ++n2;
                    if (this.typeShortcuts.containsKey(object)) {
                        if (this.peekChar() != 'I') {
                            return this.typeShortcuts.get(object);
                        }
                        Object object2 = new ArrayList<Demangler.IdentLike>((Collection)this.prefixShortcuts.get(object));
                        if ((object2 = this.parsePossibleTemplateArguments((List<Demangler.IdentLike>)object2)) != null) {
                            return this.typeShortcuts.get(object2);
                        }
                    }
                    this.position -= n2;
                }
            }
            case 'N': {
                --this.position;
                ArrayList<Demangler.IdentLike> arrayList = new ArrayList<Demangler.IdentLike>();
                String string = this.parseSimpleOrComplexIdentInto(arrayList, false);
                ArrayList<Demangler.IdentLike> arrayList2 = arrayList;
                Object object = new Demangler.ClassRef(this.ensureOfType(arrayList2.remove(arrayList2.size() - 1), Demangler.Ident.class));
                if (!arrayList.isEmpty()) {
                    ((Demangler.ClassRef)object).setEnclosingType(new Demangler.NamespaceRef(arrayList.toArray()));
                }
                if (string != null) {
                    this.typeShortcuts.put(string, (Demangler.TypeRef)object);
                }
                return object;
            }
            case 'P': 
            case 'R': {
                char c4 = this.peekChar();
                boolean bl = c2 == 'R';
                boolean bl2 = c4 == 'K';
                return this.parsePointerType(bl2 || c4 == 'N', bl2, bl);
            }
            case 'F': {
                Demangler.MemberRef memberRef = new Demangler.MemberRef();
                memberRef.setValueType(this.parseType());
                ArrayList<Demangler.TypeRef> arrayList = new ArrayList<Demangler.TypeRef>();
                while (this.peekChar() != 'E') {
                    arrayList.add(this.parseType());
                }
                ArrayList<Demangler.TypeRef> arrayList3 = arrayList;
                memberRef.paramTypes = arrayList3.toArray(new Demangler.TypeRef[arrayList3.size()]);
                this.expectChars('E');
                return new Demangler.FunctionTypeRef(memberRef);
            }
            case 'K': {
                return this.parseType();
            }
            case 'v': {
                return GCC4Demangler.classType(Void.TYPE, new Class[0]);
            }
            case 'a': 
            case 'c': 
            case 'h': {
                return GCC4Demangler.classType(Byte.TYPE, new Class[0]);
            }
            case 'b': {
                return GCC4Demangler.classType(Boolean.TYPE, new Class[0]);
            }
            case 'l': 
            case 'm': {
                return GCC4Demangler.classType(CLong.class, new Class[0]);
            }
            case 'x': 
            case 'y': {
                return GCC4Demangler.classType(Long.TYPE, new Class[0]);
            }
            case 'i': 
            case 'j': {
                return GCC4Demangler.classType(Integer.TYPE, new Class[0]);
            }
            case 's': 
            case 't': {
                return GCC4Demangler.classType(Short.TYPE, new Class[0]);
            }
            case 'f': {
                return GCC4Demangler.classType(Float.TYPE, new Class[0]);
            }
            case 'd': {
                return GCC4Demangler.classType(Double.TYPE, new Class[0]);
            }
            case 'z': {
                return GCC4Demangler.classType(Object[].class, new Class[0]);
            }
        }
        throw this.error("Unexpected type char '" + c2 + "'", -1);
    }

    String parseName() throws Demangler.DemanglingException {
        int n2;
        StringBuilder stringBuilder = new StringBuilder();
        while (Character.isDigit((char)(n2 = this.peekChar()))) {
            this.consumeChar();
            stringBuilder.append((char)n2);
        }
        try {
            n2 = Integer.parseInt(stringBuilder.toString());
        }
        catch (NumberFormatException numberFormatException) {
            throw this.error("Expected a number", 0);
        }
        stringBuilder.setLength(0);
        for (int i2 = 0; i2 < n2; ++i2) {
            stringBuilder.append(this.consumeChar());
        }
        return stringBuilder.toString();
    }

    private String parseSimpleOrComplexIdentInto(List<Demangler.IdentLike> list2, boolean bl) throws Demangler.DemanglingException {
        String string = null;
        int n2 = 0;
        boolean bl2 = false;
        if (this.consumeCharIf('N')) {
            if (this.consumeCharIf('S')) {
                this.parseShortcutInto(list2);
            }
            n2 = 1;
            bl2 = true;
        } else if (this.consumeCharIf('S')) {
            n2 = this.parseShortcutInto(list2);
        } else {
            list2.add(this.parseNonCompoundIdent());
        }
        if (n2 != 0) {
            n2 = this.nextShortcutId;
            do {
                String string2;
                string = string2 = this.nextShortcutId();
                Demangler.IdentLike identLike = this.parseNonCompoundIdent();
                list2.add(identLike);
                this.prefixShortcuts.put(string2, new ArrayList<Demangler.IdentLike>(list2));
                this.parsePossibleTemplateArguments(list2);
            } while (Character.isDigit(this.peekChar()) || this.peekChar() == 'C' || this.peekChar() == 'D');
            if (bl) {
                this.nextShortcutId = n2;
            }
        }
        this.parsePossibleTemplateArguments(list2);
        if (bl2) {
            this.expectAnyChar('E');
        }
        return string;
    }

    private String parsePossibleTemplateArguments(List<Demangler.IdentLike> list2) throws Demangler.DemanglingException {
        if (this.consumeCharIf('I')) {
            Object object = new ArrayList<Demangler.TemplateArg>();
            while (!this.consumeCharIf('E')) {
                object.add(this.parseTemplateArg());
            }
            String string = this.nextShortcutId();
            List<Demangler.IdentLike> list3 = list2;
            ArrayList<Demangler.TemplateArg> arrayList = object;
            object = new Demangler.Ident(this.ensureOfType(list3.remove(list3.size() - 1), Demangler.Ident.class).toString(), arrayList.toArray(new Demangler.TemplateArg[arrayList.size()]));
            list2.add((Demangler.IdentLike)object);
            this.prefixShortcuts.put(string, new ArrayList<Demangler.IdentLike>(list2));
            List<Demangler.IdentLike> list4 = list2 = new ArrayList<Demangler.IdentLike>(list2);
            object = new Demangler.ClassRef(this.ensureOfType(list4.remove(list4.size() - 1), Demangler.Ident.class));
            if (!list2.isEmpty()) {
                ((Demangler.ClassRef)object).setEnclosingType(new Demangler.NamespaceRef(list2.toArray()));
            }
            this.typeShortcuts.put(string, (Demangler.TypeRef)object);
            return string;
        }
        return null;
    }

    private boolean parseShortcutInto(List<Demangler.IdentLike> list2) throws Demangler.DemanglingException {
        char c2 = this.peekChar();
        if (c2 == '_') {
            List<Demangler.IdentLike> list3 = this.prefixShortcuts.get(Character.toString(this.consumeChar()));
            if (list3 == null) {
                throw new Demangler.DemanglingException("Encountered a yet undefined gcc mangler shortcut S_ (first one), i.e. '_' " + this.prefixShortcuts.keySet());
            }
            list2.addAll(list3);
            return false;
        }
        if (Character.isDigit(c2) || Character.isUpperCase(c2)) {
            String string = "";
            while ((c2 = this.peekChar()) != '_' && c2 != '\u0000') {
                string = string + this.consumeChar();
            }
            if (this.peekChar() == '\u0000') {
                throw new Demangler.DemanglingException("Encountered a unexpected end in gcc mangler shortcut '" + string + "' " + this.prefixShortcuts.keySet());
            }
            List<Demangler.IdentLike> list4 = this.prefixShortcuts.get(string = string + this.consumeChar());
            if (list4 == null) {
                throw new Demangler.DemanglingException("Encountered a unexpected gcc mangler shortcut '" + string + "' " + this.prefixShortcuts.keySet());
            }
            list2.addAll(list4);
            return false;
        }
        if (Character.isLowerCase(c2)) {
            String string = Character.toString(this.consumeChar());
            List<Demangler.IdentLike> list5 = this.prefixShortcuts.get(string);
            if (list5 == null) {
                throw new Demangler.DemanglingException("Encountered a unexpected gcc mangler built-in shortcut '" + string + "' " + this.prefixShortcuts.keySet());
            }
            list2.addAll(list5);
            return this.shouldContinueAfterPrefix.contains(string);
        }
        throw new Demangler.DemanglingException("Encountered a unexpected gcc unknown shortcut '" + c2 + "' " + this.prefixShortcuts.keySet());
    }

    Demangler.IdentLike parseNonCompoundIdent() throws Demangler.DemanglingException {
        if (this.consumeCharIf('C')) {
            if (this.consumeCharIf('1')) {
                return Demangler.SpecialName.Constructor;
            }
            if (this.consumeCharIf('2')) {
                return Demangler.SpecialName.SpecialConstructor;
            }
            throw this.error("Unknown constructor type 'C" + this.peekChar() + "'");
        }
        if (this.consumeCharIf('D')) {
            if (this.consumeCharIf('0')) {
                return Demangler.SpecialName.DeletingDestructor;
            }
            if (this.consumeCharIf('1')) {
                return Demangler.SpecialName.Destructor;
            }
            if (this.consumeCharIf('2')) {
                return Demangler.SpecialName.SelfishDestructor;
            }
            throw this.error("Unknown destructor type 'D" + this.peekChar() + "'");
        }
        String string = this.parseName();
        return new Demangler.Ident(string, new Demangler.TemplateArg[0]);
    }

    @Override
    public Demangler.MemberRef parseSymbol() throws Demangler.DemanglingException {
        Demangler.MemberRef memberRef = new Demangler.MemberRef();
        if (!this.consumeCharIf('_')) {
            memberRef.setMemberName(new Demangler.Ident(this.str, new Demangler.TemplateArg[0]));
            return memberRef;
        }
        this.consumeCharIf('_');
        if (!this.consumeCharIf('Z')) {
            return null;
        }
        if (this.consumeCharIf('T')) {
            if (this.consumeCharIf('V')) {
                GCC4Demangler gCC4Demangler = this;
                memberRef.setEnclosingType(gCC4Demangler.ensureOfType(gCC4Demangler.parseType(), Demangler.ClassRef.class));
                memberRef.setMemberName(Demangler.SpecialName.VFTable);
                return memberRef;
            }
            return null;
        }
        if (this.consumeCharsIf('d', 'l', 'P', 'v')) {
            memberRef.setMemberName(Demangler.SpecialName.Delete);
            return memberRef;
        }
        if (this.consumeCharsIf('d', 'a', 'P', 'v')) {
            memberRef.setMemberName(Demangler.SpecialName.DeleteArray);
            return memberRef;
        }
        if (this.consumeCharsIf('n', 'w', 'm')) {
            memberRef.setMemberName(Demangler.SpecialName.New);
            return memberRef;
        }
        if (this.consumeCharsIf('n', 'a', 'm')) {
            memberRef.setMemberName(Demangler.SpecialName.NewArray);
            return memberRef;
        }
        ArrayList<Demangler.IdentLike> arrayList = new ArrayList<Demangler.IdentLike>();
        this.parseSimpleOrComplexIdentInto(arrayList, true);
        ArrayList<Demangler.IdentLike> arrayList2 = arrayList;
        memberRef.setMemberName((Demangler.IdentLike)arrayList2.remove(arrayList2.size() - 1));
        if (!arrayList.isEmpty()) {
            ArrayList<Demangler.IdentLike> arrayList3 = arrayList;
            Demangler.ClassRef classRef = new Demangler.ClassRef(this.ensureOfType(arrayList3.remove(arrayList3.size() - 1), Demangler.Ident.class));
            if (memberRef.getMemberName() == Demangler.SpecialName.Constructor || memberRef.getMemberName() == Demangler.SpecialName.SpecialConstructor) {
                this.typeShortcuts.put(this.nextShortcutId(), classRef);
            }
            if (!arrayList.isEmpty()) {
                classRef.setEnclosingType(new Demangler.NamespaceRef(arrayList.toArray()));
            }
            memberRef.setEnclosingType(classRef);
        }
        if (this.consumeCharIf('v')) {
            if (this.position < this.length) {
                this.error("Expected end of symbol", 0);
            }
            memberRef.paramTypes = new Demangler.TypeRef[0];
        } else {
            arrayList = new ArrayList();
            while (this.position < this.length) {
                arrayList.add((Demangler.IdentLike)((Object)this.parseType()));
            }
            ArrayList<Demangler.IdentLike> arrayList4 = arrayList;
            memberRef.paramTypes = arrayList4.toArray(new Demangler.TypeRef[arrayList4.size()]);
        }
        return memberRef;
    }
}


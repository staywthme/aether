/*
 * Decompiled with CFR 0.152.
 */
package org.bridj.relocated.org.objectweb.asm.signature;

import org.bridj.relocated.org.objectweb.asm.signature.SignatureVisitor;

public class SignatureReader {
    private final String a;

    public SignatureReader(String string) {
        this.a = string;
    }

    public void accept(SignatureVisitor signatureVisitor) {
        int n2;
        String string = this.a;
        int n3 = string.length();
        if (string.charAt(0) == '<') {
            int n4;
            n2 = 2;
            block0: do {
                n4 = string.indexOf(58, n2);
                signatureVisitor.visitFormalTypeParameter(string.substring(n2 - 1, n4));
                n2 = n4 + 1;
                char c2 = string.charAt(n2);
                n4 = c2;
                if (c2 == 'L' || n4 == 91 || n4 == 84) {
                    n2 = SignatureReader.a(string, n2, signatureVisitor.visitClassBound());
                }
                while (true) {
                    char c3 = string.charAt(n2++);
                    n4 = c3;
                    if (c3 != ':') continue block0;
                    n2 = SignatureReader.a(string, n2, signatureVisitor.visitInterfaceBound());
                }
            } while (n4 != 62);
        } else {
            n2 = 0;
        }
        if (string.charAt(n2) == '(') {
            ++n2;
            while (string.charAt(n2) != ')') {
                n2 = SignatureReader.a(string, n2, signatureVisitor.visitParameterType());
            }
            n2 = SignatureReader.a(string, n2 + 1, signatureVisitor.visitReturnType());
            while (n2 < n3) {
                n2 = SignatureReader.a(string, n2 + 1, signatureVisitor.visitExceptionType());
            }
        } else {
            n2 = SignatureReader.a(string, n2, signatureVisitor.visitSuperclass());
            while (n2 < n3) {
                n2 = SignatureReader.a(string, n2, signatureVisitor.visitInterface());
            }
        }
    }

    public void acceptType(SignatureVisitor signatureVisitor) {
        SignatureReader.a(this.a, 0, signatureVisitor);
    }

    private static int a(String string, int n2, SignatureVisitor signatureVisitor) {
        block14: while (true) {
            char c2 = string.charAt(n2++);
            switch (c2) {
                case 'B': 
                case 'C': 
                case 'D': 
                case 'F': 
                case 'I': 
                case 'J': 
                case 'S': 
                case 'V': 
                case 'Z': {
                    signatureVisitor.visitBaseType(c2);
                    return n2;
                }
                case '[': {
                    signatureVisitor = signatureVisitor.visitArrayType();
                    continue block14;
                }
                case 'T': {
                    int n3 = string.indexOf(59, n2);
                    signatureVisitor.visitTypeVariable(string.substring(n2, n3));
                    return n3 + 1;
                }
            }
            break;
        }
        int n4 = n2;
        boolean string3 = false;
        boolean bl = false;
        block15: while (true) {
            char c2 = string.charAt(n2++);
            switch (c2) {
                case '.': 
                case ';': {
                    if (!string3) {
                        String string2 = string.substring(n4, n2 - 1);
                        if (bl) {
                            signatureVisitor.visitInnerClassType(string2);
                        } else {
                            signatureVisitor.visitClassType(string2);
                        }
                    }
                    if (c2 == ';') {
                        signatureVisitor.visitEnd();
                        return n2;
                    }
                    n4 = n2;
                    string3 = false;
                    bl = true;
                    continue block15;
                }
                case '<': {
                    String string4 = string.substring(n4, n2 - 1);
                    if (bl) {
                        signatureVisitor.visitInnerClassType(string4);
                    } else {
                        signatureVisitor.visitClassType(string4);
                    }
                    string3 = true;
                    block16: while (true) {
                        c2 = string.charAt(n2);
                        switch (c2) {
                            case '>': {
                                continue block15;
                            }
                            case '*': {
                                ++n2;
                                signatureVisitor.visitTypeArgument();
                                continue block16;
                            }
                            case '+': 
                            case '-': {
                                n2 = SignatureReader.a(string, n2 + 1, signatureVisitor.visitTypeArgument(c2));
                                continue block16;
                            }
                        }
                        n2 = SignatureReader.a(string, n2, signatureVisitor.visitTypeArgument('='));
                    }
                }
            }
        }
    }
}


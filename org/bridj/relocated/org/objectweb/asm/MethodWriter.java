/*
 * Decompiled with CFR 0.152.
 */
package org.bridj.relocated.org.objectweb.asm;

import org.bridj.relocated.org.objectweb.asm.AnnotationVisitor;
import org.bridj.relocated.org.objectweb.asm.AnnotationWriter;
import org.bridj.relocated.org.objectweb.asm.Attribute;
import org.bridj.relocated.org.objectweb.asm.ByteVector;
import org.bridj.relocated.org.objectweb.asm.ClassWriter;
import org.bridj.relocated.org.objectweb.asm.Edge;
import org.bridj.relocated.org.objectweb.asm.Frame;
import org.bridj.relocated.org.objectweb.asm.Handle;
import org.bridj.relocated.org.objectweb.asm.Handler;
import org.bridj.relocated.org.objectweb.asm.Item;
import org.bridj.relocated.org.objectweb.asm.Label;
import org.bridj.relocated.org.objectweb.asm.MethodVisitor;
import org.bridj.relocated.org.objectweb.asm.Type;

class MethodWriter
extends MethodVisitor {
    final ClassWriter b;
    private int c;
    private final int d;
    private final int e;
    private final String f;
    String g;
    int h;
    int i;
    int j;
    int[] k;
    private ByteVector l;
    private AnnotationWriter m;
    private AnnotationWriter n;
    private AnnotationWriter[] o;
    private AnnotationWriter[] p;
    private int S;
    private Attribute q;
    private ByteVector r = new ByteVector();
    private int s;
    private int t;
    private int T;
    private int u;
    private ByteVector v;
    private int w;
    private int[] x;
    private int y;
    private int[] z;
    private int A;
    private Handler B;
    private Handler C;
    private int D;
    private ByteVector E;
    private int F;
    private ByteVector G;
    private int H;
    private ByteVector I;
    private Attribute J;
    private boolean K;
    private int L;
    private final int M;
    private Label N;
    private Label O;
    private Label P;
    private int Q;
    private int R;

    MethodWriter(ClassWriter classWriter, int n2, String string, String string2, String string3, String[] stringArray, boolean bl, boolean bl2) {
        super(262144);
        if (classWriter.D == null) {
            classWriter.D = this;
        } else {
            classWriter.E.mv = this;
        }
        classWriter.E = this;
        this.b = classWriter;
        this.c = n2;
        this.d = classWriter.newUTF8(string);
        this.e = classWriter.newUTF8(string2);
        this.f = string2;
        this.g = string3;
        if (stringArray != null && stringArray.length > 0) {
            this.j = stringArray.length;
            this.k = new int[this.j];
            for (int i2 = 0; i2 < this.j; ++i2) {
                this.k[i2] = classWriter.newClass(stringArray[i2]);
            }
        }
        int n3 = bl2 ? 0 : (this.M = bl ? 1 : 2);
        if (bl || bl2) {
            if (bl2 && "<init>".equals(string)) {
                this.c |= 0x40000;
            }
            int n4 = Type.getArgumentsAndReturnSizes(this.f) >> 2;
            if ((n2 & 8) != 0) {
                --n4;
            }
            this.t = n4;
            this.T = n4;
            this.N = new Label();
            this.N.a |= 8;
            MethodWriter methodWriter = this;
            ((MethodVisitor)methodWriter).visitLabel(methodWriter.N);
        }
    }

    public AnnotationVisitor visitAnnotationDefault() {
        this.l = new ByteVector();
        return new AnnotationWriter(this.b, false, this.l, null, 0);
    }

    public AnnotationVisitor visitAnnotation(String object, boolean bl) {
        ByteVector byteVector = new ByteVector();
        byteVector.putShort(this.b.newUTF8((String)object)).putShort(0);
        ByteVector byteVector2 = byteVector;
        object = new AnnotationWriter(this.b, true, byteVector2, byteVector2, 2);
        if (bl) {
            ((AnnotationWriter)object).g = this.m;
            this.m = object;
        } else {
            ((AnnotationWriter)object).g = this.n;
            this.n = object;
        }
        return object;
    }

    public AnnotationVisitor visitParameterAnnotation(int n2, String object, boolean bl) {
        ByteVector byteVector = new ByteVector();
        if ("Ljava/lang/Synthetic;".equals(object)) {
            this.S = Math.max(this.S, n2 + 1);
            return new AnnotationWriter(this.b, false, byteVector, null, 0);
        }
        byteVector.putShort(this.b.newUTF8((String)object)).putShort(0);
        ByteVector byteVector2 = byteVector;
        object = new AnnotationWriter(this.b, true, byteVector2, byteVector2, 2);
        if (bl) {
            if (this.o == null) {
                this.o = new AnnotationWriter[Type.getArgumentTypes(this.f).length];
            }
            ((AnnotationWriter)object).g = this.o[n2];
            this.o[n2] = object;
        } else {
            if (this.p == null) {
                this.p = new AnnotationWriter[Type.getArgumentTypes(this.f).length];
            }
            ((AnnotationWriter)object).g = this.p[n2];
            this.p[n2] = object;
        }
        return object;
    }

    public void visitAttribute(Attribute attribute) {
        if (attribute.isCodeAttribute()) {
            attribute.a = this.J;
            this.J = attribute;
            return;
        }
        attribute.a = this.q;
        this.q = attribute;
    }

    public void visitCode() {
    }

    public void visitFrame(int n2, int n3, Object[] objectArray, int n4, Object[] objectArray2) {
        if (this.M == 0) {
            return;
        }
        if (n2 == -1) {
            int n5;
            this.T = n3;
            MethodWriter methodWriter = this;
            methodWriter.a(methodWriter.r.b, n3, n4);
            for (n5 = 0; n5 < n3; ++n5) {
                this.z[this.y++] = objectArray[n5] instanceof String ? 0x1700000 | this.b.c((String)objectArray[n5]) : (objectArray[n5] instanceof Integer ? (Integer)objectArray[n5] : 0x1800000 | this.b.a("", ((Label)objectArray[n5]).c));
            }
            for (n5 = 0; n5 < n4; ++n5) {
                this.z[this.y++] = objectArray2[n5] instanceof String ? 0x1700000 | this.b.c((String)objectArray2[n5]) : (objectArray2[n5] instanceof Integer ? (Integer)objectArray2[n5] : 0x1800000 | this.b.a("", ((Label)objectArray2[n5]).c));
            }
            this.b();
        } else {
            int n6;
            if (this.v == null) {
                this.v = new ByteVector();
                n6 = this.r.b;
            } else {
                n6 = this.r.b - this.w - 1;
                if (n6 < 0) {
                    if (n2 == 3) {
                        return;
                    }
                    throw new IllegalStateException();
                }
            }
            switch (n2) {
                case 0: {
                    this.T = n3;
                    this.v.putByte(255).putShort(n6).putShort(n3);
                    for (n2 = 0; n2 < n3; ++n2) {
                        this.a(objectArray[n2]);
                    }
                    this.v.putShort(n4);
                    for (n2 = 0; n2 < n4; ++n2) {
                        this.a(objectArray2[n2]);
                    }
                    break;
                }
                case 1: {
                    this.T += n3;
                    this.v.putByte(n3 + 251).putShort(n6);
                    for (n2 = 0; n2 < n3; ++n2) {
                        this.a(objectArray[n2]);
                    }
                    break;
                }
                case 2: {
                    this.T -= n3;
                    this.v.putByte(251 - n3).putShort(n6);
                    break;
                }
                case 3: {
                    if (n6 < 64) {
                        this.v.putByte(n6);
                        break;
                    }
                    this.v.putByte(251).putShort(n6);
                    break;
                }
                case 4: {
                    if (n6 < 64) {
                        this.v.putByte(n6 + 64);
                    } else {
                        this.v.putByte(247).putShort(n6);
                    }
                    this.a(objectArray2[0]);
                }
            }
            this.w = this.r.b;
            ++this.u;
        }
        this.s = Math.max(this.s, n4);
        this.t = Math.max(this.t, this.T);
    }

    public void visitInsn(int n2) {
        this.r.putByte(n2);
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(n2, 0, null, null);
            } else {
                int n3 = this.Q + Frame.a[n2];
                if (n3 > this.R) {
                    this.R = n3;
                }
                this.Q = n3;
            }
            if (n2 >= 172 && n2 <= 177 || n2 == 191) {
                this.e();
            }
        }
    }

    public void visitIntInsn(int n2, int n3) {
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(n2, n3, null, null);
            } else if (n2 != 188) {
                int n4 = this.Q + 1;
                if (n4 > this.R) {
                    this.R = n4;
                }
                this.Q = n4;
            }
        }
        if (n2 == 17) {
            this.r.b(n2, n3);
            return;
        }
        this.r.a(n2, n3);
    }

    public void visitVarInsn(int n2, int n3) {
        int n4;
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(n2, n3, null, null);
            } else if (n2 == 169) {
                this.P.a |= 0x100;
                this.P.f = this.Q;
                this.e();
            } else {
                n4 = this.Q + Frame.a[n2];
                if (n4 > this.R) {
                    this.R = n4;
                }
                this.Q = n4;
            }
        }
        if (this.M != 2 && (n4 = n2 == 22 || n2 == 24 || n2 == 55 || n2 == 57 ? n3 + 2 : n3 + 1) > this.t) {
            this.t = n4;
        }
        if (n3 < 4 && n2 != 169) {
            n4 = n2 < 54 ? 26 + (n2 - 21 << 2) + n3 : 59 + (n2 - 54 << 2) + n3;
            this.r.putByte(n4);
        } else if (n3 >= 256) {
            this.r.putByte(196).b(n2, n3);
        } else {
            this.r.a(n2, n3);
        }
        if (n2 >= 54 && this.M == 0 && this.A > 0) {
            ((MethodVisitor)this).visitLabel(new Label());
        }
    }

    public void visitTypeInsn(int n2, String object) {
        object = this.b.a((String)object);
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(n2, this.r.b, this.b, (Item)object);
            } else if (n2 == 187) {
                int n3 = this.Q + 1;
                if (n3 > this.R) {
                    this.R = n3;
                }
                this.Q = n3;
            }
        }
        this.r.b(n2, ((Item)object).a);
    }

    public void visitFieldInsn(int n2, String object, String string, String string2) {
        object = this.b.a((String)object, string, string2);
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(n2, 0, this.b, (Item)object);
            } else {
                int n3 = string2.charAt(0);
                switch (n2) {
                    case 178: {
                        n3 = this.Q + (n3 == 68 || n3 == 74 ? 2 : 1);
                        break;
                    }
                    case 179: {
                        n3 = this.Q + (n3 == 68 || n3 == 74 ? -2 : -1);
                        break;
                    }
                    case 180: {
                        n3 = this.Q + (n3 == 68 || n3 == 74 ? 1 : 0);
                        break;
                    }
                    default: {
                        n3 = this.Q + (n3 == 68 || n3 == 74 ? -3 : -2);
                    }
                }
                if (n3 > this.R) {
                    this.R = n3;
                }
                this.Q = n3;
            }
        }
        this.r.b(n2, ((Item)object).a);
    }

    public void visitMethodInsn(int n2, String object, String string, String string2) {
        boolean bl = n2 == 185;
        object = this.b.a((String)object, string, string2, bl);
        int n3 = ((Item)object).c;
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(n2, 0, this.b, (Item)object);
            } else {
                int n4;
                if (n3 == 0) {
                    ((Item)object).c = n3 = Type.getArgumentsAndReturnSizes(string2);
                }
                if ((n4 = n2 == 184 ? this.Q - (n3 >> 2) + (n3 & 3) + 1 : this.Q - (n3 >> 2) + (n3 & 3)) > this.R) {
                    this.R = n4;
                }
                this.Q = n4;
            }
        }
        if (bl) {
            if (n3 == 0) {
                ((Item)object).c = n3 = Type.getArgumentsAndReturnSizes(string2);
            }
            this.r.b(185, ((Item)object).a).a(n3 >> 2, 0);
            return;
        }
        this.r.b(n2, ((Item)object).a);
    }

    public void visitInvokeDynamicInsn(String object, String string, Handle handle, Object ... objectArray) {
        object = this.b.a((String)object, string, handle, objectArray);
        int n2 = ((Item)object).c;
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(186, 0, this.b, (Item)object);
            } else {
                int n3;
                if (n2 == 0) {
                    ((Item)object).c = n2 = Type.getArgumentsAndReturnSizes(string);
                }
                if ((n3 = this.Q - (n2 >> 2) + (n2 & 3) + 1) > this.R) {
                    this.R = n3;
                }
                this.Q = n3;
            }
        }
        this.r.b(186, ((Item)object).a);
        this.r.putShort(0);
    }

    public void visitJumpInsn(int n2, Label label) {
        Label label2 = null;
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(n2, 0, null, null);
                label.a().a |= 0x10;
                this.a(0, label);
                if (n2 != 167) {
                    label2 = new Label();
                }
            } else if (n2 == 168) {
                if ((label.a & 0x200) == 0) {
                    label.a |= 0x200;
                    ++this.L;
                }
                this.P.a |= 0x80;
                MethodWriter methodWriter = this;
                methodWriter.a(methodWriter.Q + 1, label);
                label2 = new Label();
            } else {
                this.Q += Frame.a[n2];
                MethodWriter methodWriter = this;
                methodWriter.a(methodWriter.Q, label);
            }
        }
        if ((label.a & 2) != 0 && label.c - this.r.b < Short.MIN_VALUE) {
            if (n2 == 167) {
                this.r.putByte(200);
            } else if (n2 == 168) {
                this.r.putByte(201);
            } else {
                if (label2 != null) {
                    label2.a |= 0x10;
                }
                this.r.putByte(n2 <= 166 ? (n2 + 1 ^ 1) - 1 : n2 ^ 1);
                this.r.putShort(8);
                this.r.putByte(200);
            }
            MethodWriter methodWriter = this;
            label.a(methodWriter, methodWriter.r, this.r.b - 1, true);
        } else {
            this.r.putByte(n2);
            MethodWriter methodWriter = this;
            label.a(methodWriter, methodWriter.r, this.r.b - 1, false);
        }
        if (this.P != null) {
            if (label2 != null) {
                ((MethodVisitor)this).visitLabel(label2);
            }
            if (n2 == 167) {
                this.e();
            }
        }
    }

    public void visitLabel(Label label) {
        MethodWriter methodWriter = this;
        this.K |= label.a(methodWriter, methodWriter.r.b, this.r.a);
        if ((label.a & 1) != 0) {
            return;
        }
        if (this.M == 0) {
            if (this.P != null) {
                if (label.c == this.P.c) {
                    this.P.a |= label.a & 0x10;
                    label.h = this.P.h;
                    return;
                }
                this.a(0, label);
            }
            this.P = label;
            if (label.h == null) {
                label.h = new Frame();
                label.h.b = label;
            }
            if (this.O != null) {
                if (label.c == this.O.c) {
                    this.O.a |= label.a & 0x10;
                    label.h = this.O.h;
                    this.P = this.O;
                    return;
                }
                this.O.i = label;
            }
            this.O = label;
            return;
        }
        if (this.M == 1) {
            if (this.P != null) {
                this.P.g = this.R;
                MethodWriter methodWriter2 = this;
                methodWriter2.a(methodWriter2.Q, label);
            }
            this.P = label;
            this.Q = 0;
            this.R = 0;
            if (this.O != null) {
                this.O.i = label;
            }
            this.O = label;
        }
    }

    public void visitLdcInsn(Object object) {
        int n2;
        object = this.b.a(object);
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(18, 0, this.b, (Item)object);
            } else {
                n2 = ((Item)object).b == 5 || ((Item)object).b == 6 ? this.Q + 2 : this.Q + 1;
                if (n2 > this.R) {
                    this.R = n2;
                }
                this.Q = n2;
            }
        }
        n2 = ((Item)object).a;
        if (((Item)object).b == 5 || ((Item)object).b == 6) {
            this.r.b(20, n2);
            return;
        }
        if (n2 >= 256) {
            this.r.b(19, n2);
            return;
        }
        this.r.a(18, n2);
    }

    public void visitIincInsn(int n2, int n3) {
        int n4;
        if (this.P != null && this.M == 0) {
            this.P.h.a(132, n2, null, null);
        }
        if (this.M != 2 && (n4 = n2 + 1) > this.t) {
            this.t = n4;
        }
        if (n2 > 255 || n3 > 127 || n3 < -128) {
            this.r.putByte(196).b(132, n2).putShort(n3);
            return;
        }
        this.r.putByte(132).a(n2, n3);
    }

    public void visitTableSwitchInsn(int n2, int n3, Label label, Label ... labelArray) {
        int n4 = this.r.b;
        this.r.putByte(170);
        this.r.putByteArray(null, 0, (4 - this.r.b % 4) % 4);
        MethodWriter methodWriter = this;
        label.a(methodWriter, methodWriter.r, n4, true);
        this.r.putInt(n2).putInt(n3);
        for (n2 = 0; n2 < labelArray.length; ++n2) {
            MethodWriter methodWriter2 = this;
            labelArray[n2].a(methodWriter2, methodWriter2.r, n4, true);
        }
        this.a(label, labelArray);
    }

    public void visitLookupSwitchInsn(Label label, int[] nArray, Label[] labelArray) {
        int n2 = this.r.b;
        this.r.putByte(171);
        this.r.putByteArray(null, 0, (4 - this.r.b % 4) % 4);
        MethodWriter methodWriter = this;
        label.a(methodWriter, methodWriter.r, n2, true);
        this.r.putInt(labelArray.length);
        for (int i2 = 0; i2 < labelArray.length; ++i2) {
            this.r.putInt(nArray[i2]);
            MethodWriter methodWriter2 = this;
            labelArray[i2].a(methodWriter2, methodWriter2.r, n2, true);
        }
        this.a(label, labelArray);
    }

    private void a(Label label, Label[] labelArray) {
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(171, 0, null, null);
                this.a(0, label);
                label.a().a |= 0x10;
                for (int i2 = 0; i2 < labelArray.length; ++i2) {
                    this.a(0, labelArray[i2]);
                    labelArray[i2].a().a |= 0x10;
                }
            } else {
                --this.Q;
                MethodWriter methodWriter = this;
                methodWriter.a(methodWriter.Q, label);
                for (int i3 = 0; i3 < labelArray.length; ++i3) {
                    MethodWriter methodWriter2 = this;
                    methodWriter2.a(methodWriter2.Q, labelArray[i3]);
                }
            }
            this.e();
        }
    }

    public void visitMultiANewArrayInsn(String object, int n2) {
        object = this.b.a((String)object);
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(197, n2, this.b, (Item)object);
            } else {
                this.Q += 1 - n2;
            }
        }
        this.r.b(197, ((Item)object).a).putByte(n2);
    }

    public void visitTryCatchBlock(Label label, Label label2, Label label3, String string) {
        ++this.A;
        Handler handler = new Handler();
        new Handler().a = label;
        handler.b = label2;
        handler.c = label3;
        handler.d = string;
        int n2 = handler.e = string != null ? this.b.newClass(string) : 0;
        if (this.C == null) {
            this.B = handler;
        } else {
            this.C.f = handler;
        }
        this.C = handler;
    }

    public void visitLocalVariable(String string, String string2, String string3, Label label, Label label2, int n2) {
        int n3;
        if (string3 != null) {
            if (this.G == null) {
                this.G = new ByteVector();
            }
            ++this.F;
            this.G.putShort(label.c).putShort(label2.c - label.c).putShort(this.b.newUTF8(string)).putShort(this.b.newUTF8(string3)).putShort(n2);
        }
        if (this.E == null) {
            this.E = new ByteVector();
        }
        ++this.D;
        this.E.putShort(label.c).putShort(label2.c - label.c).putShort(this.b.newUTF8(string)).putShort(this.b.newUTF8(string2)).putShort(n2);
        if (this.M != 2 && (n3 = n2 + ((n3 = string2.charAt(0)) == 74 || n3 == 68 ? 2 : 1)) > this.t) {
            this.t = n3;
        }
    }

    public void visitLineNumber(int n2, Label label) {
        if (this.I == null) {
            this.I = new ByteVector();
        }
        ++this.H;
        this.I.putShort(label.c);
        this.I.putShort(n2);
    }

    public void visitMaxs(int n2, int n3) {
        if (this.M == 0) {
            Object object;
            Type[] typeArray;
            Object object2;
            Object object3 = this.B;
            while (object3 != null) {
                object2 = ((Handler)object3).a.a();
                typeArray = ((Handler)object3).c.a();
                Label label = ((Handler)object3).b.a();
                object = ((Handler)object3).d == null ? "java/lang/Throwable" : ((Handler)object3).d;
                int n4 = 0x1700000 | this.b.c((String)object);
                typeArray.a |= 0x10;
                while (object2 != label) {
                    Edge edge = new Edge();
                    new Edge().a = n4;
                    edge.b = typeArray;
                    edge.c = ((Label)object2).j;
                    ((Label)object2).j = edge;
                    object2 = ((Label)object2).i;
                }
                object3 = ((Handler)object3).f;
            }
            object2 = this.N.h;
            typeArray = Type.getArgumentTypes(this.f);
            ((Frame)object2).a(this.b, this.c, typeArray, this.t);
            this.b((Frame)object2);
            int n5 = 0;
            object = this.N;
            while (object != null) {
                Object object4 = object;
                object = ((Label)object).k;
                ((Label)object4).k = null;
                object2 = ((Label)object4).h;
                if ((((Label)object4).a & 0x10) != 0) {
                    ((Label)object4).a |= 0x20;
                }
                ((Label)object4).a |= 0x40;
                int n6 = ((Frame)object2).d.length + ((Label)object4).g;
                if (n6 > n5) {
                    n5 = n6;
                }
                Edge edge = ((Label)object4).j;
                while (edge != null) {
                    object3 = edge.b.a();
                    boolean bl = ((Frame)object2).a(this.b, ((Label)object3).h, edge.a);
                    if (bl && ((Label)object3).k == null) {
                        ((Label)object3).k = object;
                        object = object3;
                    }
                    edge = edge.c;
                }
            }
            Label label = this.N;
            while (label != null) {
                Label label2;
                int n7;
                object2 = label.h;
                if ((label.a & 0x20) != 0) {
                    this.b((Frame)object2);
                }
                if ((label.a & 0x40) == 0 && (n7 = ((label2 = label.i) == null ? this.r.b : label2.c) - 1) >= (n2 = label.c)) {
                    n5 = Math.max(n5, 1);
                    for (int i2 = n2; i2 < n7; ++i2) {
                        this.r.a[i2] = 0;
                    }
                    this.r.a[n7] = -65;
                    this.a(n2, 0, 1);
                    this.z[this.y++] = 0x1700000 | this.b.c("java/lang/Throwable");
                    this.b();
                    this.B = Handler.a(this.B, label, label2);
                }
                label = label.i;
            }
            Handler handler = this.B;
            this.A = 0;
            while (handler != null) {
                ++this.A;
                handler = handler.f;
            }
            this.s = n5;
            return;
        }
        if (this.M == 1) {
            Object object;
            Label label;
            Label label3;
            Handler handler = this.B;
            while (handler != null) {
                Label label4 = handler.a;
                label3 = handler.c;
                label = handler.b;
                while (label4 != label) {
                    object = new Edge();
                    new Edge().a = Integer.MAX_VALUE;
                    ((Edge)object).b = label3;
                    if ((label4.a & 0x80) == 0) {
                        ((Edge)object).c = label4.j;
                        label4.j = object;
                    } else {
                        ((Edge)object).c = label4.j.c.c;
                        label4.j.c.c = object;
                    }
                    label4 = label4.i;
                }
                handler = handler.f;
            }
            if (this.L > 0) {
                int n8 = 0;
                this.N.b(null, 1L, this.L);
                label3 = this.N;
                while (label3 != null) {
                    if ((label3.a & 0x80) != 0) {
                        label = label3.j.c.b;
                        if ((label.a & 0x400) == 0) {
                            label.b(null, (long)(++n8) / 32L << 32 | 1L << n8 % 32, this.L);
                        }
                    }
                    label3 = label3.i;
                }
                label3 = this.N;
                while (label3 != null) {
                    if ((label3.a & 0x80) != 0) {
                        label = this.N;
                        while (label != null) {
                            label.a &= 0xFFFFF7FF;
                            label = label.i;
                        }
                        object = label3.j.c.b;
                        ((Label)object).b(label3, 0L, this.L);
                    }
                    label3 = label3.i;
                }
            }
            int n9 = 0;
            label3 = this.N;
            while (label3 != null) {
                label = label3;
                label3 = label3.k;
                int n10 = label.f;
                int n11 = n10 + label.g;
                if (n11 > n9) {
                    n9 = n11;
                }
                Edge edge = label.j;
                if ((label.a & 0x80) != 0) {
                    edge = edge.c;
                }
                while (edge != null) {
                    label = edge.b;
                    if ((label.a & 8) == 0) {
                        label.f = edge.a == Integer.MAX_VALUE ? 1 : n10 + edge.a;
                        label.a |= 8;
                        label.k = label3;
                        label3 = label;
                    }
                    edge = edge.c;
                }
            }
            this.s = Math.max(n2, n9);
            return;
        }
        this.s = n2;
        this.t = n3;
    }

    public void visitEnd() {
    }

    private void a(int n2, Label label) {
        Edge edge = new Edge();
        new Edge().a = n2;
        edge.b = label;
        edge.c = this.P.j;
        this.P.j = edge;
    }

    private void e() {
        if (this.M == 0) {
            Label label = new Label();
            new Label().h = new Frame();
            label.h.b = label;
            MethodWriter methodWriter = this;
            label.a(methodWriter, methodWriter.r.b, this.r.a);
            this.O.i = label;
            this.O = label;
        } else {
            this.P.g = this.R;
        }
        this.P = null;
    }

    private void b(Frame frame) {
        int n2;
        int n3;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        int[] nArray = frame.c;
        int[] nArray2 = frame.d;
        for (n3 = 0; n3 < nArray.length; ++n3) {
            n2 = nArray[n3];
            if (n2 == 0x1000000) {
                ++n4;
            } else {
                n5 += n4 + 1;
                n4 = 0;
            }
            if (n2 != 0x1000004 && n2 != 0x1000003) continue;
            ++n3;
        }
        for (n3 = 0; n3 < nArray2.length; ++n3) {
            n2 = nArray2[n3];
            ++n6;
            if (n2 != 0x1000004 && n2 != 0x1000003) continue;
            ++n3;
        }
        this.a(frame.b.c, n5, n6);
        n3 = 0;
        while (n5 > 0) {
            n2 = nArray[n3];
            this.z[this.y++] = n2;
            if (n2 == 0x1000004 || n2 == 0x1000003) {
                ++n3;
            }
            ++n3;
            --n5;
        }
        for (n3 = 0; n3 < nArray2.length; ++n3) {
            n2 = nArray2[n3];
            this.z[this.y++] = n2;
            if (n2 != 0x1000004 && n2 != 0x1000003) continue;
            ++n3;
        }
        this.b();
    }

    private void a(int n2, int n3, int n4) {
        int n5 = n3 + 3 + n4;
        if (this.z == null || this.z.length < n5) {
            this.z = new int[n5];
        }
        this.z[0] = n2;
        this.z[1] = n3;
        this.z[2] = n4;
        this.y = 3;
    }

    private void b() {
        if (this.x != null) {
            if (this.v == null) {
                this.v = new ByteVector();
            }
            this.c();
            ++this.u;
        }
        this.x = this.z;
        this.z = null;
    }

    private void c() {
        int n2 = this.z[1];
        int n3 = this.z[2];
        if ((this.b.b & 0xFFFF) < 50) {
            this.v.putShort(this.z[0]).putShort(n2);
            this.a(3, n2 + 3);
            this.v.putShort(n3);
            this.a(n2 + 3, n2 + 3 + n3);
            return;
        }
        int n4 = this.x[1];
        int n5 = 255;
        int n6 = 0;
        int n7 = this.u == 0 ? this.z[0] : this.z[0] - this.x[0] - 1;
        if (n3 == 0) {
            n6 = n2 - n4;
            switch (n6) {
                case -3: 
                case -2: 
                case -1: {
                    n5 = 248;
                    n4 = n2;
                    break;
                }
                case 0: {
                    n5 = n7 < 64 ? 0 : 251;
                    break;
                }
                case 1: 
                case 2: 
                case 3: {
                    n5 = 252;
                }
            }
        } else if (n2 == n4 && n3 == 1) {
            int n8 = n5 = n7 < 63 ? 64 : 247;
        }
        if (n5 != 255) {
            int n9 = 3;
            for (int i2 = 0; i2 < n4; ++i2) {
                if (this.z[n9] != this.x[n9]) {
                    n5 = 255;
                    break;
                }
                ++n9;
            }
        }
        switch (n5) {
            case 0: {
                this.v.putByte(n7);
                return;
            }
            case 64: {
                this.v.putByte(n7 + 64);
                this.a(n2 + 3, n2 + 4);
                return;
            }
            case 247: {
                this.v.putByte(247).putShort(n7);
                this.a(n2 + 3, n2 + 4);
                return;
            }
            case 251: {
                this.v.putByte(251).putShort(n7);
                return;
            }
            case 248: {
                this.v.putByte(n6 + 251).putShort(n7);
                return;
            }
            case 252: {
                this.v.putByte(n6 + 251).putShort(n7);
                this.a(n4 + 3, n2 + 3);
                return;
            }
        }
        this.v.putByte(255).putShort(n7).putShort(n2);
        this.a(3, n2 + 3);
        this.v.putShort(n3);
        this.a(n2 + 3, n2 + 3 + n3);
    }

    private void a(int n2, int n3) {
        while (n2 < n3) {
            int n4 = this.z[n2];
            int n5 = n4 & 0xF0000000;
            if (n5 == 0) {
                int n6 = n4 & 0xFFFFF;
                switch (n4 & 0xFF00000) {
                    case 0x1700000: {
                        this.v.putByte(7).putShort(this.b.newClass(this.b.H[n6].g));
                        break;
                    }
                    case 0x1800000: {
                        this.v.putByte(8).putShort(this.b.H[n6].c);
                        break;
                    }
                    default: {
                        this.v.putByte(n6);
                        break;
                    }
                }
            } else {
                StringBuffer stringBuffer = new StringBuffer();
                n5 >>= 28;
                while (n5-- > 0) {
                    stringBuffer.append('[');
                }
                if ((n4 & 0xFF00000) == 0x1700000) {
                    stringBuffer.append('L');
                    stringBuffer.append(this.b.H[n4 & 0xFFFFF].g);
                    stringBuffer.append(';');
                } else {
                    switch (n4 & 0xF) {
                        case 1: {
                            stringBuffer.append('I');
                            break;
                        }
                        case 2: {
                            stringBuffer.append('F');
                            break;
                        }
                        case 3: {
                            stringBuffer.append('D');
                            break;
                        }
                        case 9: {
                            stringBuffer.append('Z');
                            break;
                        }
                        case 10: {
                            stringBuffer.append('B');
                            break;
                        }
                        case 11: {
                            stringBuffer.append('C');
                            break;
                        }
                        case 12: {
                            stringBuffer.append('S');
                            break;
                        }
                        default: {
                            stringBuffer.append('J');
                        }
                    }
                }
                this.v.putByte(7).putShort(this.b.newClass(stringBuffer.toString()));
            }
            ++n2;
        }
    }

    private void a(Object object) {
        if (object instanceof String) {
            this.v.putByte(7).putShort(this.b.newClass((String)object));
            return;
        }
        if (object instanceof Integer) {
            this.v.putByte((Integer)object);
            return;
        }
        this.v.putByte(8).putShort(((Label)object).c);
    }

    final int a() {
        int n2;
        if (this.h != 0) {
            return 6 + this.i;
        }
        if (this.K) {
            this.d();
        }
        int n3 = 8;
        if (this.r.b > 0) {
            if (this.r.b > 65536) {
                throw new RuntimeException("Method code too large!");
            }
            this.b.newUTF8("Code");
            n3 = 8 + (18 + this.r.b + 8 * this.A);
            if (this.E != null) {
                this.b.newUTF8("LocalVariableTable");
                n3 += 8 + this.E.b;
            }
            if (this.G != null) {
                this.b.newUTF8("LocalVariableTypeTable");
                n3 += 8 + this.G.b;
            }
            if (this.I != null) {
                this.b.newUTF8("LineNumberTable");
                n3 += 8 + this.I.b;
            }
            if (this.v != null) {
                n2 = (this.b.b & 0xFFFF) >= 50 ? 1 : 0;
                this.b.newUTF8(n2 != 0 ? "StackMapTable" : "StackMap");
                n3 += 8 + this.v.b;
            }
            if (this.J != null) {
                n3 += this.J.a(this.b, this.r.a, this.r.b, this.s, this.t);
            }
        }
        if (this.j > 0) {
            this.b.newUTF8("Exceptions");
            n3 += 8 + 2 * this.j;
        }
        if ((this.c & 0x1000) != 0 && ((this.b.b & 0xFFFF) < 49 || (this.c & 0x40000) != 0)) {
            this.b.newUTF8("Synthetic");
            n3 += 6;
        }
        if ((this.c & 0x20000) != 0) {
            this.b.newUTF8("Deprecated");
            n3 += 6;
        }
        if (this.g != null) {
            this.b.newUTF8("Signature");
            this.b.newUTF8(this.g);
            n3 += 8;
        }
        if (this.l != null) {
            this.b.newUTF8("AnnotationDefault");
            n3 += 6 + this.l.b;
        }
        if (this.m != null) {
            this.b.newUTF8("RuntimeVisibleAnnotations");
            n3 += 8 + this.m.a();
        }
        if (this.n != null) {
            this.b.newUTF8("RuntimeInvisibleAnnotations");
            n3 += 8 + this.n.a();
        }
        if (this.o != null) {
            this.b.newUTF8("RuntimeVisibleParameterAnnotations");
            n3 += 7 + 2 * (this.o.length - this.S);
            for (n2 = this.o.length - 1; n2 >= this.S; --n2) {
                n3 += this.o[n2] == null ? 0 : this.o[n2].a();
            }
        }
        if (this.p != null) {
            this.b.newUTF8("RuntimeInvisibleParameterAnnotations");
            n3 += 7 + 2 * (this.p.length - this.S);
            for (n2 = this.p.length - 1; n2 >= this.S; --n2) {
                n3 += this.p[n2] == null ? 0 : this.p[n2].a();
            }
        }
        if (this.q != null) {
            n3 += this.q.a(this.b, null, 0, -1, -1);
        }
        return n3;
    }

    final void a(ByteVector byteVector) {
        int n2 = 0x60000 | (this.c & 0x40000) / 64;
        byteVector.putShort(this.c & ~n2).putShort(this.d).putShort(this.e);
        if (this.h != 0) {
            byteVector.putByteArray(this.b.M.b, this.h, this.i);
            return;
        }
        n2 = 0;
        if (this.r.b > 0) {
            ++n2;
        }
        if (this.j > 0) {
            ++n2;
        }
        if ((this.c & 0x1000) != 0 && ((this.b.b & 0xFFFF) < 49 || (this.c & 0x40000) != 0)) {
            ++n2;
        }
        if ((this.c & 0x20000) != 0) {
            ++n2;
        }
        if (this.g != null) {
            ++n2;
        }
        if (this.l != null) {
            ++n2;
        }
        if (this.m != null) {
            ++n2;
        }
        if (this.n != null) {
            ++n2;
        }
        if (this.o != null) {
            ++n2;
        }
        if (this.p != null) {
            ++n2;
        }
        if (this.q != null) {
            n2 += this.q.a();
        }
        byteVector.putShort(n2);
        if (this.r.b > 0) {
            n2 = 12 + this.r.b + 8 * this.A;
            if (this.E != null) {
                n2 += 8 + this.E.b;
            }
            if (this.G != null) {
                n2 += 8 + this.G.b;
            }
            if (this.I != null) {
                n2 += 8 + this.I.b;
            }
            if (this.v != null) {
                n2 += 8 + this.v.b;
            }
            if (this.J != null) {
                n2 += this.J.a(this.b, this.r.a, this.r.b, this.s, this.t);
            }
            byteVector.putShort(this.b.newUTF8("Code")).putInt(n2);
            byteVector.putShort(this.s).putShort(this.t);
            byteVector.putInt(this.r.b).putByteArray(this.r.a, 0, this.r.b);
            byteVector.putShort(this.A);
            if (this.A > 0) {
                Handler handler = this.B;
                while (handler != null) {
                    byteVector.putShort(handler.a.c).putShort(handler.b.c).putShort(handler.c.c).putShort(handler.e);
                    handler = handler.f;
                }
            }
            n2 = 0;
            if (this.E != null) {
                ++n2;
            }
            if (this.G != null) {
                ++n2;
            }
            if (this.I != null) {
                ++n2;
            }
            if (this.v != null) {
                ++n2;
            }
            if (this.J != null) {
                n2 += this.J.a();
            }
            byteVector.putShort(n2);
            if (this.E != null) {
                byteVector.putShort(this.b.newUTF8("LocalVariableTable"));
                byteVector.putInt(this.E.b + 2).putShort(this.D);
                byteVector.putByteArray(this.E.a, 0, this.E.b);
            }
            if (this.G != null) {
                byteVector.putShort(this.b.newUTF8("LocalVariableTypeTable"));
                byteVector.putInt(this.G.b + 2).putShort(this.F);
                byteVector.putByteArray(this.G.a, 0, this.G.b);
            }
            if (this.I != null) {
                byteVector.putShort(this.b.newUTF8("LineNumberTable"));
                byteVector.putInt(this.I.b + 2).putShort(this.H);
                byteVector.putByteArray(this.I.a, 0, this.I.b);
            }
            if (this.v != null) {
                n2 = (this.b.b & 0xFFFF) >= 50 ? 1 : 0;
                byteVector.putShort(this.b.newUTF8(n2 != 0 ? "StackMapTable" : "StackMap"));
                byteVector.putInt(this.v.b + 2).putShort(this.u);
                byteVector.putByteArray(this.v.a, 0, this.v.b);
            }
            if (this.J != null) {
                this.J.a(this.b, this.r.a, this.r.b, this.t, this.s, byteVector);
            }
        }
        if (this.j > 0) {
            byteVector.putShort(this.b.newUTF8("Exceptions")).putInt(2 * this.j + 2);
            byteVector.putShort(this.j);
            for (n2 = 0; n2 < this.j; ++n2) {
                byteVector.putShort(this.k[n2]);
            }
        }
        if ((this.c & 0x1000) != 0 && ((this.b.b & 0xFFFF) < 49 || (this.c & 0x40000) != 0)) {
            byteVector.putShort(this.b.newUTF8("Synthetic")).putInt(0);
        }
        if ((this.c & 0x20000) != 0) {
            byteVector.putShort(this.b.newUTF8("Deprecated")).putInt(0);
        }
        if (this.g != null) {
            byteVector.putShort(this.b.newUTF8("Signature")).putInt(2).putShort(this.b.newUTF8(this.g));
        }
        if (this.l != null) {
            byteVector.putShort(this.b.newUTF8("AnnotationDefault"));
            byteVector.putInt(this.l.b);
            byteVector.putByteArray(this.l.a, 0, this.l.b);
        }
        if (this.m != null) {
            byteVector.putShort(this.b.newUTF8("RuntimeVisibleAnnotations"));
            this.m.a(byteVector);
        }
        if (this.n != null) {
            byteVector.putShort(this.b.newUTF8("RuntimeInvisibleAnnotations"));
            this.n.a(byteVector);
        }
        if (this.o != null) {
            byteVector.putShort(this.b.newUTF8("RuntimeVisibleParameterAnnotations"));
            AnnotationWriter.a(this.o, this.S, byteVector);
        }
        if (this.p != null) {
            byteVector.putShort(this.b.newUTF8("RuntimeInvisibleParameterAnnotations"));
            AnnotationWriter.a(this.p, this.S, byteVector);
        }
        if (this.q != null) {
            this.q.a(this.b, null, 0, -1, -1, byteVector);
        }
    }

    private void d() {
        int n2;
        Object object;
        int n3;
        int n4;
        int n5;
        byte[] byArray = this.r.a;
        Object object2 = new int[]{};
        int[] nArray = new int[]{};
        boolean[] blArray = new boolean[this.r.b];
        int n6 = 3;
        do {
            if (n6 == 3) {
                n6 = 2;
            }
            n5 = 0;
            while (n5 < byArray.length) {
                int n7 = byArray[n5] & 0xFF;
                n4 = 0;
                switch (ClassWriter.a[n7]) {
                    case 0: 
                    case 4: {
                        ++n5;
                        break;
                    }
                    case 9: {
                        int n8;
                        if (n7 > 201) {
                            n7 = n7 < 218 ? n7 - 49 : n7 - 20;
                            n8 = n5 + MethodWriter.c(byArray, n5 + 1);
                        } else {
                            n8 = n5 + MethodWriter.b(byArray, n5 + 1);
                        }
                        n3 = MethodWriter.a(object2, nArray, n5, n8);
                        if (!(n3 >= Short.MIN_VALUE && n3 <= Short.MAX_VALUE || blArray[n5])) {
                            n4 = n7 == 167 || n7 == 168 ? 2 : 5;
                            blArray[n5] = true;
                        }
                        n5 += 3;
                        break;
                    }
                    case 10: {
                        n5 += 5;
                        break;
                    }
                    case 14: {
                        if (n6 == 1) {
                            n3 = MethodWriter.a(object2, nArray, 0, n5);
                            n4 = -(n3 & 3);
                        } else if (!blArray[n5]) {
                            n4 = n5 & 3;
                            blArray[n5] = true;
                        }
                        n5 = n5 + 4 - (n5 & 3);
                        n5 += 4 * (MethodWriter.a(byArray, n5 + 8) - MethodWriter.a(byArray, n5 + 4) + 1) + 12;
                        break;
                    }
                    case 15: {
                        if (n6 == 1) {
                            n3 = MethodWriter.a(object2, nArray, 0, n5);
                            n4 = -(n3 & 3);
                        } else if (!blArray[n5]) {
                            n4 = n5 & 3;
                            blArray[n5] = true;
                        }
                        n5 = n5 + 4 - (n5 & 3);
                        n5 += 8 * MethodWriter.a(byArray, n5 + 4) + 8;
                        break;
                    }
                    case 17: {
                        n7 = byArray[n5 + 1] & 0xFF;
                        if (n7 == 132) {
                            n5 += 6;
                            break;
                        }
                        n5 += 4;
                        break;
                    }
                    case 1: 
                    case 3: 
                    case 11: {
                        n5 += 2;
                        break;
                    }
                    case 2: 
                    case 5: 
                    case 6: 
                    case 12: 
                    case 13: {
                        n5 += 3;
                        break;
                    }
                    case 7: 
                    case 8: {
                        n5 += 5;
                        break;
                    }
                    default: {
                        n5 += 4;
                    }
                }
                if (n4 == 0) continue;
                object = new int[((int[])object2).length + 1];
                int[] nArray2 = new int[nArray.length + 1];
                System.arraycopy(object2, 0, object, 0, ((int[])object2).length);
                System.arraycopy(nArray, 0, nArray2, 0, nArray.length);
                object[((int[])object2).length] = n5;
                nArray2[nArray.length] = n4;
                object2 = object;
                nArray = nArray2;
                if (n4 <= 0) continue;
                n6 = 3;
            }
            if (n6 >= 3) continue;
            --n6;
        } while (n6 != 0);
        ByteVector byteVector = new ByteVector(this.r.b);
        n5 = 0;
        block24: while (n5 < this.r.b) {
            n4 = byArray[n5] & 0xFF;
            switch (ClassWriter.a[n4]) {
                case 0: 
                case 4: {
                    byteVector.putByte(n4);
                    ++n5;
                    continue block24;
                }
                case 9: {
                    int n9;
                    if (n4 > 201) {
                        n4 = n4 < 218 ? n4 - 49 : n4 - 20;
                        n9 = n5 + MethodWriter.c(byArray, n5 + 1);
                    } else {
                        n9 = n5 + MethodWriter.b(byArray, n5 + 1);
                    }
                    n3 = MethodWriter.a(object2, nArray, n5, n9);
                    if (blArray[n5]) {
                        if (n4 == 167) {
                            byteVector.putByte(200);
                        } else if (n4 == 168) {
                            byteVector.putByte(201);
                        } else {
                            byteVector.putByte(n4 <= 166 ? (n4 + 1 ^ 1) - 1 : n4 ^ 1);
                            byteVector.putShort(8);
                            byteVector.putByte(200);
                            n3 -= 3;
                        }
                        byteVector.putInt(n3);
                    } else {
                        byteVector.putByte(n4);
                        byteVector.putShort(n3);
                    }
                    n5 += 3;
                    continue block24;
                }
                case 10: {
                    int n9 = n5 + MethodWriter.a(byArray, n5 + 1);
                    n3 = MethodWriter.a(object2, nArray, n5, n9);
                    byteVector.putByte(n4);
                    byteVector.putInt(n3);
                    n5 += 5;
                    continue block24;
                }
                case 14: {
                    n6 = n5;
                    n5 = n5 + 4 - (n6 & 3);
                    byteVector.putByte(170);
                    byteVector.putByteArray(null, 0, (4 - byteVector.b % 4) % 4);
                    int n9 = n6 + MethodWriter.a(byArray, n5);
                    n3 = MethodWriter.a(object2, nArray, n6, n9);
                    byteVector.putInt(n3);
                    n4 = MethodWriter.a(byArray, n5 += 4);
                    byteVector.putInt(n4);
                    byteVector.putInt(MethodWriter.a(byArray, (n5 += 4) - 4));
                    for (n4 = MethodWriter.a(byArray, n5 += 4) - n4 + 1; n4 > 0; --n4) {
                        n9 = n6 + MethodWriter.a(byArray, n5);
                        n5 += 4;
                        n3 = MethodWriter.a(object2, nArray, n6, n9);
                        byteVector.putInt(n3);
                    }
                    continue block24;
                }
                case 15: {
                    n6 = n5;
                    n5 = n5 + 4 - (n6 & 3);
                    byteVector.putByte(171);
                    byteVector.putByteArray(null, 0, (4 - byteVector.b % 4) % 4);
                    int n9 = n6 + MethodWriter.a(byArray, n5);
                    n3 = MethodWriter.a(object2, nArray, n6, n9);
                    byteVector.putInt(n3);
                    n5 += 4;
                    byteVector.putInt(n4);
                    for (n4 = MethodWriter.a(byArray, n5 += 4); n4 > 0; --n4) {
                        byteVector.putInt(MethodWriter.a(byArray, n5));
                        n9 = n6 + MethodWriter.a(byArray, n5 += 4);
                        n5 += 4;
                        n3 = MethodWriter.a(object2, nArray, n6, n9);
                        byteVector.putInt(n3);
                    }
                    continue block24;
                }
                case 17: {
                    n4 = byArray[n5 + 1] & 0xFF;
                    if (n4 == 132) {
                        byteVector.putByteArray(byArray, n5, 6);
                        n5 += 6;
                        continue block24;
                    }
                    byteVector.putByteArray(byArray, n5, 4);
                    n5 += 4;
                    continue block24;
                }
                case 1: 
                case 3: 
                case 11: {
                    byteVector.putByteArray(byArray, n5, 2);
                    n5 += 2;
                    continue block24;
                }
                case 2: 
                case 5: 
                case 6: 
                case 12: 
                case 13: {
                    byteVector.putByteArray(byArray, n5, 3);
                    n5 += 3;
                    continue block24;
                }
                case 7: 
                case 8: {
                    byteVector.putByteArray(byArray, n5, 5);
                    n5 += 5;
                    continue block24;
                }
            }
            byteVector.putByteArray(byArray, n5, 4);
            n5 += 4;
        }
        if (this.u > 0) {
            if (this.M == 0) {
                this.u = 0;
                this.v = null;
                this.x = null;
                this.z = null;
                Frame frame = new Frame();
                new Frame().b = this.N;
                object = Type.getArgumentTypes(this.f);
                frame.a(this.b, this.c, (Type[])object, this.t);
                this.b(frame);
                Label label = this.N;
                while (label != null) {
                    n5 = label.c - 3;
                    if ((label.a & 0x20) != 0 || n5 >= 0 && blArray[n5]) {
                        MethodWriter.a(object2, nArray, label);
                        this.b(label.h);
                    }
                    label = label.i;
                }
            } else {
                this.b.L = true;
            }
        }
        Handler handler = this.B;
        while (handler != null) {
            MethodWriter.a(object2, nArray, handler.a);
            MethodWriter.a(object2, nArray, handler.b);
            MethodWriter.a(object2, nArray, handler.c);
            handler = handler.f;
        }
        for (n2 = 0; n2 < 2; ++n2) {
            ByteVector byteVector2 = n2 == 0 ? this.E : this.G;
            object = byteVector2;
            if (byteVector2 == null) continue;
            byArray = ((ByteVector)object).a;
            for (n5 = 0; n5 < ((ByteVector)object).b; n5 += 10) {
                int n10 = MethodWriter.c(byArray, n5);
                n3 = MethodWriter.a(object2, nArray, 0, n10);
                MethodWriter.a(byArray, n5, n3);
                n3 = MethodWriter.a(object2, nArray, 0, n10 += MethodWriter.c(byArray, n5 + 2)) - n3;
                MethodWriter.a(byArray, n5 + 2, n3);
            }
        }
        if (this.I != null) {
            byArray = this.I.a;
            for (n5 = 0; n5 < this.I.b; n5 += 4) {
                MethodWriter.a(byArray, n5, MethodWriter.a(object2, nArray, 0, MethodWriter.c(byArray, n5)));
            }
        }
        object = this.J;
        while (object != null) {
            Label[] labelArray = ((Attribute)object).getLabels();
            if (labelArray != null) {
                for (n2 = labelArray.length - 1; n2 >= 0; --n2) {
                    MethodWriter.a(object2, nArray, labelArray[n2]);
                }
            }
            object = ((Attribute)object).a;
        }
        this.r = byteVector;
    }

    static int c(byte[] byArray, int n2) {
        return (byArray[n2] & 0xFF) << 8 | byArray[n2 + 1] & 0xFF;
    }

    static short b(byte[] byArray, int n2) {
        return (short)((byArray[n2] & 0xFF) << 8 | byArray[n2 + 1] & 0xFF);
    }

    static int a(byte[] byArray, int n2) {
        return (byArray[n2] & 0xFF) << 24 | (byArray[n2 + 1] & 0xFF) << 16 | (byArray[n2 + 2] & 0xFF) << 8 | byArray[n2 + 3] & 0xFF;
    }

    static void a(byte[] byArray, int n2, int n3) {
        byArray[n2] = (byte)(n3 >>> 8);
        byArray[n2 + 1] = (byte)n3;
    }

    static int a(int[] nArray, int[] nArray2, int n2, int n3) {
        int n4 = n3 - n2;
        for (int i2 = 0; i2 < nArray.length; ++i2) {
            if (n2 < nArray[i2] && nArray[i2] <= n3) {
                n4 += nArray2[i2];
                continue;
            }
            if (n3 >= nArray[i2] || nArray[i2] > n2) continue;
            n4 -= nArray2[i2];
        }
        return n4;
    }

    static void a(int[] nArray, int[] nArray2, Label label) {
        if ((label.a & 4) == 0) {
            label.c = MethodWriter.a(nArray, nArray2, 0, label.c);
            label.a |= 4;
        }
    }
}


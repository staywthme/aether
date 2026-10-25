/*
 * Decompiled with CFR 0.152.
 */
package org.bridj.relocated.org.objectweb.asm;

import org.bridj.relocated.org.objectweb.asm.AnnotationVisitor;
import org.bridj.relocated.org.objectweb.asm.ByteVector;
import org.bridj.relocated.org.objectweb.asm.ClassWriter;
import org.bridj.relocated.org.objectweb.asm.Item;
import org.bridj.relocated.org.objectweb.asm.Type;

final class AnnotationWriter
extends AnnotationVisitor {
    private final ClassWriter a;
    private int b;
    private final boolean c;
    private final ByteVector d;
    private final ByteVector e;
    private final int f;
    AnnotationWriter g;
    AnnotationWriter h;

    AnnotationWriter(ClassWriter classWriter, boolean bl, ByteVector byteVector, ByteVector byteVector2, int n2) {
        super(262144);
        this.a = classWriter;
        this.c = bl;
        this.d = byteVector;
        this.e = byteVector2;
        this.f = n2;
    }

    public final void visit(String object, Object object2) {
        ++this.b;
        if (this.c) {
            this.d.putShort(this.a.newUTF8((String)object));
        }
        if (object2 instanceof String) {
            this.d.b(115, this.a.newUTF8((String)object2));
            return;
        }
        if (object2 instanceof Byte) {
            this.d.b(66, this.a.a((int)((Byte)object2).byteValue()).a);
            return;
        }
        if (object2 instanceof Boolean) {
            int n2 = (Boolean)object2 != false ? 1 : 0;
            this.d.b(90, this.a.a((int)n2).a);
            return;
        }
        if (object2 instanceof Character) {
            this.d.b(67, this.a.a((int)((Character)object2).charValue()).a);
            return;
        }
        if (object2 instanceof Short) {
            this.d.b(83, this.a.a((int)((Short)object2).shortValue()).a);
            return;
        }
        if (object2 instanceof Type) {
            this.d.b(99, this.a.newUTF8(((Type)object2).getDescriptor()));
            return;
        }
        if (object2 instanceof byte[]) {
            object = (byte[])object2;
            this.d.b(91, ((Object)object).length);
            for (int i2 = 0; i2 < ((Object)object).length; ++i2) {
                this.d.b(66, this.a.a((int)object[i2]).a);
            }
            return;
        }
        if (object2 instanceof boolean[]) {
            object = (boolean[])object2;
            this.d.b(91, ((Object)object).length);
            for (int i3 = 0; i3 < ((Object)object).length; ++i3) {
                this.d.b(90, this.a.a((int)(object[i3] != false ? 1 : 0)).a);
            }
            return;
        }
        if (object2 instanceof short[]) {
            object = (short[])object2;
            this.d.b(91, ((Object)object).length);
            for (int i4 = 0; i4 < ((Object)object).length; ++i4) {
                this.d.b(83, this.a.a((int)object[i4]).a);
            }
            return;
        }
        if (object2 instanceof char[]) {
            object = (char[])object2;
            this.d.b(91, ((Object)object).length);
            for (int i5 = 0; i5 < ((Object)object).length; ++i5) {
                this.d.b(67, this.a.a((int)object[i5]).a);
            }
            return;
        }
        if (object2 instanceof int[]) {
            object = (int[])object2;
            this.d.b(91, ((Object)object).length);
            for (int i6 = 0; i6 < ((Object)object).length; ++i6) {
                this.d.b(73, this.a.a((int)object[i6]).a);
            }
            return;
        }
        if (object2 instanceof long[]) {
            object = (long[])object2;
            this.d.b(91, ((Object)object).length);
            for (int i7 = 0; i7 < ((Object)object).length; ++i7) {
                this.d.b(74, this.a.a((long)object[i7]).a);
            }
            return;
        }
        if (object2 instanceof float[]) {
            object = (float[])object2;
            this.d.b(91, ((Object)object).length);
            for (int i8 = 0; i8 < ((Object)object).length; ++i8) {
                this.d.b(70, this.a.a((float)object[i8]).a);
            }
            return;
        }
        if (object2 instanceof double[]) {
            object = (double[])object2;
            this.d.b(91, ((Object)object).length);
            for (int i9 = 0; i9 < ((Object)object).length; ++i9) {
                this.d.b(68, this.a.a((double)object[i9]).a);
            }
            return;
        }
        object = this.a.a(object2);
        this.d.b(".s.IFJDCS".charAt(((Item)object).b), ((Item)object).a);
    }

    public final void visitEnum(String string, String string2, String string3) {
        ++this.b;
        if (this.c) {
            this.d.putShort(this.a.newUTF8(string));
        }
        this.d.b(101, this.a.newUTF8(string2)).putShort(this.a.newUTF8(string3));
    }

    public final AnnotationVisitor visitAnnotation(String string, String string2) {
        ++this.b;
        if (this.c) {
            this.d.putShort(this.a.newUTF8(string));
        }
        this.d.b(64, this.a.newUTF8(string2)).putShort(0);
        return new AnnotationWriter(this.a, true, this.d, this.d, this.d.b - 2);
    }

    public final AnnotationVisitor visitArray(String string) {
        ++this.b;
        if (this.c) {
            this.d.putShort(this.a.newUTF8(string));
        }
        this.d.b(91, 0);
        return new AnnotationWriter(this.a, false, this.d, this.d, this.d.b - 2);
    }

    public final void visitEnd() {
        if (this.e != null) {
            byte[] byArray = this.e.a;
            this.e.a[this.f] = (byte)(this.b >>> 8);
            byArray[this.f + 1] = (byte)this.b;
        }
    }

    final int a() {
        int n2 = 0;
        AnnotationWriter annotationWriter = this;
        while (annotationWriter != null) {
            n2 += annotationWriter.d.b;
            annotationWriter = annotationWriter.g;
        }
        return n2;
    }

    final void a(ByteVector byteVector) {
        int n2 = 0;
        int n3 = 2;
        AnnotationWriter annotationWriter = this;
        AnnotationWriter annotationWriter2 = null;
        while (annotationWriter != null) {
            ++n2;
            n3 += annotationWriter.d.b;
            ((AnnotationVisitor)annotationWriter).visitEnd();
            annotationWriter.h = annotationWriter2;
            annotationWriter2 = annotationWriter;
            annotationWriter = annotationWriter.g;
        }
        byteVector.putInt(n3);
        byteVector.putShort(n2);
        annotationWriter = annotationWriter2;
        while (annotationWriter != null) {
            byteVector.putByteArray(annotationWriter.d.a, 0, annotationWriter.d.b);
            annotationWriter = annotationWriter.h;
        }
    }

    static void a(AnnotationWriter[] annotationWriterArray, int n2, ByteVector byteVector) {
        int n3;
        int n4 = 1 + 2 * (annotationWriterArray.length - n2);
        for (n3 = n2; n3 < annotationWriterArray.length; ++n3) {
            n4 += annotationWriterArray[n3] == null ? 0 : annotationWriterArray[n3].a();
        }
        byteVector.putInt(n4).putByte(annotationWriterArray.length - n2);
        for (n3 = n2; n3 < annotationWriterArray.length; ++n3) {
            AnnotationWriter annotationWriter = annotationWriterArray[n3];
            AnnotationWriter annotationWriter2 = null;
            int n5 = 0;
            while (annotationWriter != null) {
                ++n5;
                ((AnnotationVisitor)annotationWriter).visitEnd();
                annotationWriter.h = annotationWriter2;
                annotationWriter2 = annotationWriter;
                annotationWriter = annotationWriter.g;
            }
            byteVector.putShort(n5);
            annotationWriter = annotationWriter2;
            while (annotationWriter != null) {
                byteVector.putByteArray(annotationWriter.d.a, 0, annotationWriter.d.b);
                annotationWriter = annotationWriter.h;
            }
        }
    }
}


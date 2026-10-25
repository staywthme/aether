/*
 * Decompiled with CFR 0.152.
 */
package org.bridj.relocated.org.objectweb.asm;

import java.io.IOException;
import java.io.InputStream;
import org.bridj.relocated.org.objectweb.asm.AnnotationVisitor;
import org.bridj.relocated.org.objectweb.asm.Attribute;
import org.bridj.relocated.org.objectweb.asm.ByteVector;
import org.bridj.relocated.org.objectweb.asm.ClassVisitor;
import org.bridj.relocated.org.objectweb.asm.ClassWriter;
import org.bridj.relocated.org.objectweb.asm.Handle;
import org.bridj.relocated.org.objectweb.asm.Item;
import org.bridj.relocated.org.objectweb.asm.Label;
import org.bridj.relocated.org.objectweb.asm.MethodVisitor;
import org.bridj.relocated.org.objectweb.asm.MethodWriter;
import org.bridj.relocated.org.objectweb.asm.Opcodes;
import org.bridj.relocated.org.objectweb.asm.Type;

public class ClassReader {
    public static final int SKIP_CODE = 1;
    public static final int SKIP_DEBUG = 2;
    public static final int SKIP_FRAMES = 4;
    public static final int EXPAND_FRAMES = 8;
    public final byte[] b;
    private final int[] a;
    private final String[] c;
    private final int d;
    public final int header;

    public ClassReader(byte[] byArray) {
        this(byArray, 0, byArray.length);
    }

    public ClassReader(byte[] byArray, int n2, int n3) {
        this.b = byArray;
        if (this.readShort(6) > 51) {
            throw new IllegalArgumentException();
        }
        this.a = new int[this.readUnsignedShort(n2 + 8)];
        n3 = this.a.length;
        this.c = new String[n3];
        int n4 = 0;
        n2 += 10;
        for (int i2 = 1; i2 < n3; ++i2) {
            int n5;
            this.a[i2] = n2 + 1;
            switch (byArray[n2]) {
                case 3: 
                case 4: 
                case 9: 
                case 10: 
                case 11: 
                case 12: 
                case 18: {
                    n5 = 5;
                    break;
                }
                case 5: 
                case 6: {
                    n5 = 9;
                    ++i2;
                    break;
                }
                case 1: {
                    n5 = 3 + this.readUnsignedShort(n2 + 1);
                    if (n5 <= n4) break;
                    n4 = n5;
                    break;
                }
                case 15: {
                    n5 = 4;
                    break;
                }
                default: {
                    n5 = 3;
                }
            }
            n2 += n5;
        }
        this.d = n4;
        this.header = n2;
    }

    public int getAccess() {
        ClassReader classReader = this;
        return classReader.readUnsignedShort(classReader.header);
    }

    public String getClassName() {
        ClassReader classReader = this;
        return classReader.readClass(classReader.header + 2, new char[this.d]);
    }

    public String getSuperName() {
        ClassReader classReader = this;
        int n2 = this.a[classReader.readUnsignedShort(classReader.header + 4)];
        if (n2 == 0) {
            return null;
        }
        return this.readUTF8(n2, new char[this.d]);
    }

    public String[] getInterfaces() {
        int n2 = this.header + 6;
        int n3 = this.readUnsignedShort(n2);
        String[] stringArray = new String[n3];
        if (n3 > 0) {
            char[] cArray = new char[this.d];
            for (int i2 = 0; i2 < n3; ++i2) {
                stringArray[i2] = this.readClass(n2 += 2, cArray);
            }
        }
        return stringArray;
    }

    void a(ClassWriter classWriter) {
        int n2;
        char[] cArray = new char[this.d];
        int n3 = this.a.length;
        Item[] itemArray = new Item[n3];
        for (n2 = 1; n2 < n3; ++n2) {
            int n4;
            int n5 = this.a[n2];
            byte by = this.b[n5 - 1];
            Item item = new Item(n2);
            switch (by) {
                case 9: 
                case 10: 
                case 11: {
                    int n6 = this.a[this.readUnsignedShort(n5 + 2)];
                    item.a(by, this.readClass(n5, cArray), this.readUTF8(n6, cArray), this.readUTF8(n6 + 2, cArray));
                    break;
                }
                case 3: {
                    item.a(this.readInt(n5));
                    break;
                }
                case 4: {
                    item.a(Float.intBitsToFloat(this.readInt(n5)));
                    break;
                }
                case 12: {
                    item.a(by, this.readUTF8(n5, cArray), this.readUTF8(n5 + 2, cArray), null);
                    break;
                }
                case 5: {
                    item.a(this.readLong(n5));
                    ++n2;
                    break;
                }
                case 6: {
                    item.a(Double.longBitsToDouble(this.readLong(n5)));
                    ++n2;
                    break;
                }
                case 1: {
                    String string = this.c[n2];
                    if (string == null) {
                        n5 = this.a[n2];
                        string = this.c[n2] = this.a(n5 + 2, this.readUnsignedShort(n5), cArray);
                    }
                    item.a(by, string, null, null);
                    break;
                }
                case 15: {
                    n4 = this.a[this.readUnsignedShort(n5 + 1)];
                    int n6 = this.a[this.readUnsignedShort(n4 + 2)];
                    item.a(20 + this.readByte(n5), this.readClass(n4, cArray), this.readUTF8(n6, cArray), this.readUTF8(n6 + 2, cArray));
                    break;
                }
                case 18: {
                    if (classWriter.A == null) {
                        this.a(classWriter, itemArray, cArray);
                    }
                    int n6 = this.a[this.readUnsignedShort(n5 + 2)];
                    item.a(this.readUTF8(n6, cArray), this.readUTF8(n6 + 2, cArray), this.readUnsignedShort(n5));
                    break;
                }
                default: {
                    item.a(by, this.readUTF8(n5, cArray), null, null);
                }
            }
            n4 = item.j % itemArray.length;
            item.k = itemArray[n4];
            itemArray[n4] = item;
        }
        n2 = this.a[1] - 1;
        classWriter.d.putByteArray(this.b, n2, this.header - n2);
        classWriter.e = itemArray;
        classWriter.f = (int)(0.75 * (double)n3);
        classWriter.c = n3;
    }

    private void a(ClassWriter classWriter, Item[] itemArray, char[] cArray) {
        int n2;
        int n3 = this.header;
        n3 += 8 + (this.readUnsignedShort(n3 + 6) << 1);
        int n4 = this.readUnsignedShort(n3);
        n3 += 2;
        while (n4 > 0) {
            n2 = this.readUnsignedShort(n3 + 6);
            n3 += 8;
            while (n2 > 0) {
                n3 += 6 + this.readInt(n3 + 2);
                --n2;
            }
            --n4;
        }
        n4 = this.readUnsignedShort(n3);
        n3 += 2;
        while (n4 > 0) {
            n2 = this.readUnsignedShort(n3 + 6);
            n3 += 8;
            while (n2 > 0) {
                n3 += 6 + this.readInt(n3 + 2);
                --n2;
            }
            --n4;
        }
        n4 = this.readUnsignedShort(n3);
        n3 += 2;
        while (n4 > 0) {
            String string = this.readUTF8(n3, cArray);
            int n5 = this.readInt(n3 + 2);
            if ("BootstrapMethods".equals(string)) {
                n4 = this.readUnsignedShort(n3 + 6);
                int n6 = n3 + 8;
                for (int i2 = 0; i2 < n4; ++i2) {
                    ClassReader classReader = this;
                    int n7 = classReader.readConst(classReader.readUnsignedShort(n6), cArray).hashCode();
                    int n8 = n6 + 4;
                    for (int i3 = this.readUnsignedShort(n6 + 2); i3 > 0; --i3) {
                        ClassReader classReader2 = this;
                        n7 ^= classReader2.readConst(classReader2.readUnsignedShort(n8), cArray).hashCode();
                        n8 += 2;
                    }
                    Item item = new Item(i2);
                    item.a(n6 - n3 - 8, n7 & Integer.MAX_VALUE);
                    n6 = item.j % itemArray.length;
                    item.k = itemArray[n6];
                    itemArray[n6] = item;
                    n6 = n8;
                }
                classWriter.z = n4;
                ByteVector byteVector = new ByteVector(n5 + 62);
                byteVector.putByteArray(this.b, n3 + 8, n5 - 2);
                classWriter.A = byteVector;
                return;
            }
            n3 += n5 + 6;
            --n4;
        }
    }

    public ClassReader(InputStream inputStream2) throws IOException {
        this(ClassReader.a(inputStream2, false));
    }

    public ClassReader(String string) throws IOException {
        this(ClassReader.a(ClassLoader.getSystemResourceAsStream(string.replace('.', '/') + ".class"), true));
    }

    private static byte[] a(InputStream inputStream2, boolean bl) throws IOException {
        if (inputStream2 == null) {
            throw new IOException("Class not found");
        }
        try {
            byte[] byArray = new byte[inputStream2.available()];
            int n2 = 0;
            while (true) {
                byte[] byArray2;
                int n3;
                if ((n3 = inputStream2.read(byArray, n2, byArray.length - n2)) == -1) {
                    if (n2 < byArray.length) {
                        byte[] byArray3 = new byte[n2];
                        System.arraycopy(byArray, 0, byArray3, 0, n2);
                        byArray = byArray3;
                    }
                    byte[] byArray4 = byArray;
                    return byArray4;
                }
                if ((n2 += n3) != byArray.length) continue;
                n3 = inputStream2.read();
                if (n3 < 0) {
                    byArray2 = byArray;
                    return byArray2;
                }
                byArray2 = new byte[byArray.length + 1000];
                System.arraycopy(byArray, 0, byArray2, 0, n2);
                byArray2[n2++] = (byte)n3;
                byArray = byArray2;
            }
        }
        finally {
            if (bl) {
                inputStream2.close();
            }
        }
    }

    public void accept(ClassVisitor classVisitor, int n2) {
        this.accept(classVisitor, new Attribute[0], n2);
    }

    /*
     * Unable to fully structure code
     */
    public void accept(ClassVisitor var1_1, Attribute[] var2_2, int var3_3) {
        var4_4 = this.b;
        var5_5 = new char[this.d];
        var6_6 = 0;
        var7_8 = 0;
        var8_9 = null;
        var9_10 = this.header;
        var10_11 = this.readUnsignedShort(var9_10);
        var11_12 = this.readClass(var9_10 + 2, var5_5);
        var12_16 = this.a[this.readUnsignedShort(var9_10 + 4)];
        var13_17 = var12_16 == 0 ? null : this.readUTF8(var12_16, var5_5);
        var14_19 = new String[this.readUnsignedShort(var9_10 + 6)];
        var15_31 = 0;
        var9_10 += 8;
        for (var16_32 = 0; var16_32 < var14_19.length; ++var16_32) {
            var14_19[var16_32] = this.readClass(var9_10, var5_5);
            var9_10 += 2;
        }
        var17_33 = (var3_3 & 1) != 0;
        var18_34 = (var3_3 & 2) != 0;
        var19_35 = (var3_3 & 8) != 0;
        var12_16 = var9_10;
        var16_32 = this.readUnsignedShort(var12_16);
        var12_16 += 2;
        while (var16_32 > 0) {
            var20_36 = this.readUnsignedShort(var12_16 + 6);
            var12_16 += 8;
            while (var20_36 > 0) {
                var12_16 += 6 + this.readInt(var12_16 + 2);
                --var20_36;
            }
            --var16_32;
        }
        var16_32 = this.readUnsignedShort(var12_16);
        var12_16 += 2;
        while (var16_32 > 0) {
            var20_36 = this.readUnsignedShort(var12_16 + 6);
            var12_16 += 8;
            while (var20_36 > 0) {
                var12_16 += 6 + this.readInt(var12_16 + 2);
                --var20_36;
            }
            --var16_32;
        }
        var21_37 = null;
        var22_40 = null;
        var23_42 = null;
        var24_45 = null;
        var25_46 = null;
        var26_48 = null;
        var27_51 = null;
        var16_32 = this.readUnsignedShort(var12_16);
        var12_16 += 2;
        while (var16_32 > 0) {
            var28_52 = this.readUTF8(var12_16, var5_5);
            if ("SourceFile".equals(var28_52)) {
                var22_40 = this.readUTF8(var12_16 + 6, var5_5);
            } else if ("InnerClasses".equals(var28_52)) {
                var15_31 = var12_16 + 6;
            } else if ("EnclosingMethod".equals(var28_52)) {
                var24_45 = this.readClass(var12_16 + 6, var5_5);
                var29_60 = this.readUnsignedShort(var12_16 + 8);
                if (var29_60 != 0) {
                    v0 = this;
                    var25_46 = v0.readUTF8(v0.a[var29_60], var5_5);
                    v1 = this;
                    var26_48 = v1.readUTF8(v1.a[var29_60] + 2, var5_5);
                }
            } else if ("Signature".equals(var28_52)) {
                var21_37 = this.readUTF8(var12_16 + 6, var5_5);
            } else if ("RuntimeVisibleAnnotations".equals(var28_52)) {
                var6_6 = var12_16 + 6;
            } else if ("Deprecated".equals(var28_52)) {
                var10_11 |= 131072;
            } else if ("Synthetic".equals(var28_52)) {
                var10_11 |= 266240;
            } else if ("SourceDebugExtension".equals(var28_52)) {
                v2 = var29_60 = this.readInt(var12_16 + 2);
                var23_42 = this.a(var12_16 + 6, v2, new char[v2]);
            } else if ("RuntimeInvisibleAnnotations".equals(var28_52)) {
                var7_8 = var12_16 + 6;
            } else if ("BootstrapMethods".equals(var28_52)) {
                var29_60 = this.readUnsignedShort(var12_16 + 6);
                var27_51 = new int[var29_60];
                var30_63 = var12_16 + 8;
                for (var20_36 = 0; var20_36 < var29_60; ++var20_36) {
                    var27_51[var20_36] = var30_63;
                    var30_63 += 2 + this.readUnsignedShort(var30_63 + 2) << 1;
                }
            } else {
                var31_68 = this.a(var2_2, (String)var28_52, var12_16 + 6, this.readInt(var12_16 + 2), var5_5, -1, null);
                if (var31_68 != null) {
                    var31_68.a = var8_9;
                    var8_9 = var31_68;
                }
            }
            var12_16 += 6 + this.readInt(var12_16 + 2);
            --var16_32;
        }
        var1_1.visit(this.readInt(4), var10_11, var11_12, var21_37, (String)var13_17, var14_19);
        if (!(var18_34 || var22_40 == null && var23_42 == null)) {
            var1_1.visitSource(var22_40, var23_42);
        }
        if (var24_45 != null) {
            var1_1.visitOuterClass((String)var24_45, (String)var25_46, (String)var26_48);
        }
        for (var16_32 = 1; var16_32 >= 0; --var16_32) {
            var12_16 = var16_32 == 0 ? var7_8 : var6_6;
            if (var12_16 == 0) continue;
            var20_36 = this.readUnsignedShort(var12_16);
            var12_16 += 2;
            while (var20_36 > 0) {
                var12_16 = this.a(var12_16 + 2, var5_5, true, var1_1.visitAnnotation(this.readUTF8(var12_16, var5_5), var16_32 != 0));
                --var20_36;
            }
        }
        while (var8_9 != null) {
            var31_68 = var8_9.a;
            var8_9.a = null;
            var1_1.visitAttribute((Attribute)var8_9);
            var8_9 = var31_68;
        }
        if (var15_31 != 0) {
            var16_32 = this.readUnsignedShort(var15_31);
            var15_31 += 2;
            while (var16_32 > 0) {
                var1_1.visitInnerClass(this.readUnsignedShort(var15_31) == 0 ? null : this.readClass(var15_31, var5_5), this.readUnsignedShort(var15_31 + 2) == 0 ? null : this.readClass(var15_31 + 2, var5_5), this.readUnsignedShort(var15_31 + 4) == 0 ? null : this.readUTF8(var15_31 + 4, var5_5), this.readUnsignedShort(var15_31 + 6));
                var15_31 += 8;
                --var16_32;
            }
        }
        var16_32 = this.readUnsignedShort(var9_10);
        var9_10 += 2;
        while (var16_32 > 0) {
            var10_11 = this.readUnsignedShort(var9_10);
            var11_12 = this.readUTF8(var9_10 + 2, var5_5);
            var13_17 = this.readUTF8(var9_10 + 4, var5_5);
            var29_60 = 0;
            var21_37 = null;
            var6_6 = 0;
            var7_8 = 0;
            var8_9 = null;
            var20_36 = this.readUnsignedShort(var9_10 + 6);
            var9_10 += 8;
            while (var20_36 > 0) {
                var28_52 = this.readUTF8(var9_10, var5_5);
                if ("ConstantValue".equals(var28_52)) {
                    var29_60 = this.readUnsignedShort(var9_10 + 6);
                } else if ("Signature".equals(var28_52)) {
                    var21_37 = this.readUTF8(var9_10 + 6, var5_5);
                } else if ("Deprecated".equals(var28_52)) {
                    var10_11 |= 131072;
                } else if ("Synthetic".equals(var28_52)) {
                    var10_11 |= 266240;
                } else if ("RuntimeVisibleAnnotations".equals(var28_52)) {
                    var6_6 = var9_10 + 6;
                } else if ("RuntimeInvisibleAnnotations".equals(var28_52)) {
                    var7_8 = var9_10 + 6;
                } else {
                    var31_68 = this.a(var2_2, (String)var28_52, var9_10 + 6, this.readInt(var9_10 + 2), var5_5, -1, null);
                    if (var31_68 != null) {
                        var31_68.a = var8_9;
                        var8_9 = var31_68;
                    }
                }
                var9_10 += 6 + this.readInt(var9_10 + 2);
                --var20_36;
            }
            var30_65 = var1_1.visitField(var10_11, var11_12, (String)var13_17, var21_37, var29_60 == 0 ? null : this.readConst(var29_60, var5_5));
            if (var30_65 != null) {
                for (var20_36 = 1; var20_36 >= 0; --var20_36) {
                    var12_16 = var20_36 == 0 ? var7_8 : var6_6;
                    if (var12_16 == 0) continue;
                    var14_20 = this.readUnsignedShort(var12_16);
                    var12_16 += 2;
                    while (var14_20 > 0) {
                        var12_16 = this.a(var12_16 + 2, var5_5, true, var30_65.visitAnnotation(this.readUTF8(var12_16, var5_5), var20_36 != 0));
                        --var14_20;
                    }
                }
                while (var8_9 != null) {
                    var31_68 = var8_9.a;
                    var8_9.a = null;
                    var30_65.visitAttribute((Attribute)var8_9);
                    var8_9 = var31_68;
                }
                var30_65.visitEnd();
            }
            --var16_32;
        }
        var16_32 = this.readUnsignedShort(var9_10);
        var9_10 += 2;
        while (var16_32 > 0) {
            var29_60 = var9_10 + 6;
            var10_11 = this.readUnsignedShort(var9_10);
            var11_12 = this.readUTF8(var9_10 + 2, var5_5);
            var13_17 = this.readUTF8(var9_10 + 4, var5_5);
            var21_37 = null;
            var6_6 = 0;
            var7_8 = 0;
            var30_66 = 0;
            var22_41 = 0;
            var23_43 = 0;
            var8_9 = null;
            var12_16 = 0;
            var15_31 = 0;
            var20_36 = this.readUnsignedShort(var9_10 + 6);
            var9_10 += 8;
            while (var20_36 > 0) {
                var28_52 = this.readUTF8(var9_10, var5_5);
                var14_21 = this.readInt(var9_10 + 2);
                var9_10 += 6;
                if ("Code".equals(var28_52)) {
                    if (!var17_33) {
                        var12_16 = var9_10;
                    }
                } else if ("Exceptions".equals(var28_52)) {
                    var15_31 = var9_10;
                } else if ("Signature".equals(var28_52)) {
                    var21_37 = this.readUTF8(var9_10, var5_5);
                } else if ("Deprecated".equals(var28_52)) {
                    var10_11 |= 131072;
                } else if ("RuntimeVisibleAnnotations".equals(var28_52)) {
                    var6_6 = var9_10;
                } else if ("AnnotationDefault".equals(var28_52)) {
                    var30_66 = var9_10;
                } else if ("Synthetic".equals(var28_52)) {
                    var10_11 |= 266240;
                } else if ("RuntimeInvisibleAnnotations".equals(var28_52)) {
                    var7_8 = var9_10;
                } else if ("RuntimeVisibleParameterAnnotations".equals(var28_52)) {
                    var22_41 = var9_10;
                } else if ("RuntimeInvisibleParameterAnnotations".equals(var28_52)) {
                    var23_43 = var9_10;
                } else {
                    var31_68 = this.a(var2_2, (String)var28_52, var9_10, var14_21, var5_5, -1, null);
                    if (var31_68 != null) {
                        var31_68.a = var8_9;
                        var8_9 = var31_68;
                    }
                }
                var9_10 += var14_21;
                --var20_36;
            }
            if (var15_31 == 0) {
                var14_19 = null;
            } else {
                var14_19 = new String[this.readUnsignedShort(var15_31)];
                var15_31 += 2;
                for (var20_36 = 0; var20_36 < var14_19.length; ++var20_36) {
                    var14_19[var20_36] = this.readClass(var15_31, var5_5);
                    var15_31 += 2;
                }
            }
            var24_45 = var1_1.visitMethod(var10_11, var11_12, (String)var13_17, var21_37, var14_19);
            if (var24_45 == null) ** GOTO lbl309
            if (!(var24_45 instanceof MethodWriter)) ** GOTO lbl-1000
            var25_46 = (MethodWriter)var24_45;
            if (var25_46.b.M != this || var21_37 != var25_46.g) ** GOTO lbl-1000
            var21_38 = false;
            if (var14_19 == null) {
                var21_38 = var25_46.j == 0;
            } else if (var14_19.length == var25_46.j) {
                var21_38 = true;
                for (var20_36 = var14_19.length - 1; var20_36 >= 0; --var20_36) {
                    if (var25_46.k[var20_36] == this.readUnsignedShort(var15_31 -= 2)) continue;
                    var21_38 = false;
                    break;
                }
            }
            if (var21_38) {
                var25_46.h = var29_60;
                var25_46.i = var9_10 - var29_60;
            } else lbl-1000:
            // 3 sources

            {
                if (var30_66 != 0) {
                    var25_46 = var24_45.visitAnnotationDefault();
                    this.a(var30_66, var5_5, null, (AnnotationVisitor)var25_46);
                    if (var25_46 != null) {
                        var25_46.visitEnd();
                    }
                }
                for (var20_36 = 1; var20_36 >= 0; --var20_36) {
                    var15_31 = var20_36 == 0 ? var7_8 : var6_6;
                    if (var15_31 == 0) continue;
                    var14_22 = this.readUnsignedShort(var15_31);
                    var15_31 += 2;
                    while (var14_22 > 0) {
                        var15_31 = this.a(var15_31 + 2, var5_5, true, var24_45.visitAnnotation(this.readUTF8(var15_31, var5_5), var20_36 != 0));
                        --var14_22;
                    }
                }
                if (var22_41 != 0) {
                    this.a(var22_41, (String)var13_17, var5_5, true, (MethodVisitor)var24_45);
                }
                if (var23_43 != 0) {
                    this.a(var23_43, (String)var13_17, var5_5, false, (MethodVisitor)var24_45);
                }
                while (var8_9 != null) {
                    var31_68 = var8_9.a;
                    var8_9.a = null;
                    var24_45.visitAttribute((Attribute)var8_9);
                    var8_9 = var31_68;
                }
lbl309:
                // 2 sources

                if (var24_45 != null && var12_16 != 0) {
                    var25_47 = this.readUnsignedShort(var12_16);
                    var21_39 = this.readUnsignedShort(var12_16 + 2);
                    var6_6 = this.readInt(var12_16 + 4);
                    var7_8 = var12_16 += 8;
                    var22_41 = var12_16 + var6_6;
                    var24_45.visitCode();
                    var23_44 = new Label[var6_6 + 2];
                    this.readLabel(var6_6 + 1, var23_44);
                    block60: while (var12_16 < var22_41) {
                        var15_31 = var12_16 - var7_8;
                        var26_49 = var4_4[var12_16] & 255;
                        switch (ClassWriter.a[var26_49]) {
                            case 0: 
                            case 4: {
                                ++var12_16;
                                continue block60;
                            }
                            case 9: {
                                this.readLabel(var15_31 + this.readShort(var12_16 + 1), var23_44);
                                var12_16 += 3;
                                continue block60;
                            }
                            case 10: {
                                this.readLabel(var15_31 + this.readInt(var12_16 + 1), var23_44);
                                var12_16 += 5;
                                continue block60;
                            }
                            case 17: {
                                var26_49 = var4_4[var12_16 + 1] & 255;
                                if (var26_49 == 132) {
                                    var12_16 += 6;
                                    continue block60;
                                }
                                var12_16 += 4;
                                continue block60;
                            }
                            case 14: {
                                var12_16 = var12_16 + 4 - (var15_31 & 3);
                                this.readLabel(var15_31 + this.readInt(var12_16), var23_44);
                                var20_36 = this.readInt(var12_16 + 8) - this.readInt(var12_16 + 4) + 1;
                                var12_16 += 12;
                                while (var20_36 > 0) {
                                    this.readLabel(var15_31 + this.readInt(var12_16), var23_44);
                                    var12_16 += 4;
                                    --var20_36;
                                }
                                continue block60;
                            }
                            case 15: {
                                var12_16 = var12_16 + 4 - (var15_31 & 3);
                                this.readLabel(var15_31 + this.readInt(var12_16), var23_44);
                                var20_36 = this.readInt(var12_16 + 4);
                                var12_16 += 8;
                                while (var20_36 > 0) {
                                    this.readLabel(var15_31 + this.readInt(var12_16 + 4), var23_44);
                                    var12_16 += 8;
                                    --var20_36;
                                }
                                continue block60;
                            }
                            case 1: 
                            case 3: 
                            case 11: {
                                var12_16 += 2;
                                continue block60;
                            }
                            case 2: 
                            case 5: 
                            case 6: 
                            case 12: 
                            case 13: {
                                var12_16 += 3;
                                continue block60;
                            }
                            case 7: 
                            case 8: {
                                var12_16 += 5;
                                continue block60;
                            }
                        }
                        var12_16 += 4;
                    }
                    var20_36 = this.readUnsignedShort(var12_16);
                    var12_16 += 2;
                    while (var20_36 > 0) {
                        v3 = this;
                        var26_48 = v3.readLabel(v3.readUnsignedShort(var12_16), var23_44);
                        v4 = this;
                        var29_62 = v4.readLabel(v4.readUnsignedShort(var12_16 + 2), var23_44);
                        v5 = this;
                        var30_67 = v5.readLabel(v5.readUnsignedShort(var12_16 + 4), var23_44);
                        var32_69 = this.readUnsignedShort(var12_16 + 6);
                        if (var32_69 == 0) {
                            var24_45.visitTryCatchBlock((Label)var26_48, var29_62, var30_67, null);
                        } else {
                            v6 = this;
                            var24_45.visitTryCatchBlock((Label)var26_48, var29_62, var30_67, v6.readUTF8(v6.a[var32_69], var5_5));
                        }
                        var12_16 += 8;
                        --var20_36;
                    }
                    var26_50 = 0;
                    var29_60 = 0;
                    var30_66 = 0;
                    var32_69 = 0;
                    var33_70 = 0;
                    var34_71 = 0;
                    var35_72 = 0;
                    var36_73 = 0;
                    var37_74 = 0;
                    var38_75 = 0;
                    var39_76 = null;
                    var40_77 = null;
                    var41_78 = true;
                    var8_9 = null;
                    var20_36 = this.readUnsignedShort(var12_16);
                    var12_16 += 2;
                    while (var20_36 > 0) {
                        var28_53 = this.readUTF8(var12_16, var5_5);
                        if ("LocalVariableTable".equals(var28_53)) {
                            if (!var18_34) {
                                var26_50 = var12_16 + 6;
                                var15_31 = var12_16 + 8;
                                for (var14_23 = this.readUnsignedShort(var12_16 + 6); var14_23 > 0; --var14_23) {
                                    var28_54 = this.readUnsignedShort(var15_31);
                                    if (var23_44[var28_54] == null) {
                                        this.readLabel((int)var28_54, (Label[])var23_44).a |= 1;
                                    }
                                    if (var23_44[var28_54 += this.readUnsignedShort(var15_31 + 2)] == null) {
                                        this.readLabel((int)var28_54, (Label[])var23_44).a |= 1;
                                    }
                                    var15_31 += 10;
                                }
                            }
                        } else if ("LocalVariableTypeTable".equals(var28_53)) {
                            var29_60 = var12_16 + 6;
                        } else if ("LineNumberTable".equals(var28_53)) {
                            if (!var18_34) {
                                var15_31 = var12_16 + 8;
                                for (var14_24 = this.readUnsignedShort(var12_16 + 6); var14_24 > 0; --var14_24) {
                                    var28_55 = this.readUnsignedShort(var15_31);
                                    if (var23_44[var28_55] == null) {
                                        this.readLabel((int)var28_55, (Label[])var23_44).a |= 1;
                                    }
                                    var23_44[var28_55].b = this.readUnsignedShort(var15_31 + 2);
                                    var15_31 += 4;
                                }
                            }
                        } else if ("StackMapTable".equals(var28_53)) {
                            if ((var3_3 & 4) == 0) {
                                var30_66 = var12_16 + 8;
                                var32_69 = this.readInt(var12_16 + 2);
                                var33_70 = this.readUnsignedShort(var12_16 + 6);
                            }
                        } else if ("StackMap".equals(var28_53)) {
                            if ((var3_3 & 4) == 0) {
                                var30_66 = var12_16 + 8;
                                var32_69 = this.readInt(var12_16 + 2);
                                var33_70 = this.readUnsignedShort(var12_16 + 6);
                                var41_78 = false;
                            }
                        } else {
                            for (var14_25 = 0; var14_25 < var2_2.length; ++var14_25) {
                                if (!var2_2[var14_25].type.equals(var28_53) || (var31_68 = var2_2[var14_25].read(this, var12_16 + 6, this.readInt(var12_16 + 2), var5_5, var7_8 - 8, var23_44)) == null) continue;
                                var31_68.a = var8_9;
                                var8_9 = var31_68;
                            }
                        }
                        var12_16 += 6 + this.readInt(var12_16 + 2);
                        --var20_36;
                    }
                    if (var30_66 != 0) {
                        var39_76 = new Object[var21_39];
                        var40_77 = new Object[var25_47];
                        if (var19_35) {
                            var28_56 = 0;
                            if ((var10_11 & 8) == 0) {
                                if ("<init>".equals(var11_12)) {
                                    ++var28_56;
                                    var39_76[0] = Opcodes.UNINITIALIZED_THIS;
                                } else {
                                    ++var28_56;
                                    v7 = this;
                                    var39_76[0] = v7.readClass(v7.header + 2, var5_5);
                                }
                            }
                            var20_36 = 1;
                            block68: while (true) {
                                var14_26 = var20_36;
                                switch (var13_17.charAt(var20_36++)) {
                                    case 'B': 
                                    case 'C': 
                                    case 'I': 
                                    case 'S': 
                                    case 'Z': {
                                        var39_76[var28_56++] = Opcodes.INTEGER;
                                        continue block68;
                                    }
                                    case 'F': {
                                        var39_76[var28_56++] = Opcodes.FLOAT;
                                        continue block68;
                                    }
                                    case 'J': {
                                        var39_76[var28_56++] = Opcodes.LONG;
                                        continue block68;
                                    }
                                    case 'D': {
                                        var39_76[var28_56++] = Opcodes.DOUBLE;
                                        continue block68;
                                    }
                                    case '[': {
                                        while (var13_17.charAt(var20_36) == '[') {
                                            ++var20_36;
                                        }
                                        if (var13_17.charAt(var20_36) == 'L') {
                                            ++var20_36;
                                            while (var13_17.charAt(var20_36) != ';') {
                                                ++var20_36;
                                            }
                                        }
                                        var39_76[var28_56++] = var13_17.substring(var14_26, ++var20_36);
                                        continue block68;
                                    }
                                    case 'L': {
                                        while (var13_17.charAt(var20_36) != ';') {
                                            ++var20_36;
                                        }
                                        var39_76[var28_56++] = var13_17.substring(var14_26 + 1, var20_36++);
                                        continue block68;
                                    }
                                }
                                break;
                            }
                            var36_73 = var28_56;
                        }
                        var35_72 = -1;
                        for (var20_36 = var30_66; var20_36 < var30_66 + var32_69 - 2; ++var20_36) {
                            if (var4_4[var20_36] != 8 || (var14_27 = this.readUnsignedShort(var20_36 + 1)) < 0 || var14_27 >= var6_6 || (var4_4[var7_8 + var14_27] & 255) != 187) continue;
                            this.readLabel(var14_27, var23_44);
                        }
                    }
                    var12_16 = var7_8;
                    block73: while (var12_16 < var22_41) {
                        var15_31 = var12_16 - var7_8;
                        var28_57 = var23_44[var15_31];
                        if (var28_57 != null) {
                            var24_45.visitLabel(var28_57);
                            if (!var18_34 && var28_57.b > 0) {
                                var24_45.visitLineNumber(var28_57.b, var28_57);
                            }
                        }
                        while (var39_76 != null && (var35_72 == var15_31 || var35_72 == -1)) {
                            if (!var41_78 || var19_35) {
                                var24_45.visitFrame(-1, var36_73, var39_76, var38_75, var40_77);
                            } else if (var35_72 != -1) {
                                var24_45.visitFrame(var34_71, var37_74, var39_76, var38_75, var40_77);
                            }
                            if (var33_70 > 0) {
                                if (var41_78) {
                                    var6_6 = var4_4[var30_66++] & 255;
                                } else {
                                    var6_6 = 255;
                                    var35_72 = -1;
                                }
                                var37_74 = 0;
                                if (var6_6 < 64) {
                                    var10_11 = var6_6;
                                    var34_71 = 3;
                                    var38_75 = 0;
                                } else if (var6_6 < 128) {
                                    var10_11 = var6_6 - 64;
                                    var30_66 = this.a(var40_77, 0, var30_66, var5_5, var23_44);
                                    var34_71 = 4;
                                    var38_75 = 1;
                                } else {
                                    var10_11 = this.readUnsignedShort(var30_66);
                                    var30_66 += 2;
                                    if (var6_6 == 247) {
                                        var30_66 = this.a(var40_77, 0, var30_66, var5_5, var23_44);
                                        var34_71 = 4;
                                        var38_75 = 1;
                                    } else if (var6_6 >= 248 && var6_6 < 251) {
                                        var34_71 = 2;
                                        var37_74 = 251 - var6_6;
                                        var36_73 -= var37_74;
                                        var38_75 = 0;
                                    } else if (var6_6 == 251) {
                                        var34_71 = 3;
                                        var38_75 = 0;
                                    } else if (var6_6 < 255) {
                                        var20_36 = var19_35 != false ? var36_73 : 0;
                                        for (var14_28 = var6_6 - 251; var14_28 > 0; --var14_28) {
                                            var30_66 = this.a(var39_76, var20_36++, var30_66, var5_5, var23_44);
                                        }
                                        var34_71 = 1;
                                        var37_74 = var6_6 - 251;
                                        var36_73 += var37_74;
                                        var38_75 = 0;
                                    } else {
                                        var34_71 = 0;
                                        var37_74 = var36_73 = this.readUnsignedShort(var30_66);
                                        var30_66 += 2;
                                        var20_36 = 0;
                                        for (var11_13 = var36_73; var11_13 > 0; --var11_13) {
                                            var30_66 = this.a(var39_76, var20_36++, var30_66, var5_5, var23_44);
                                        }
                                        var11_13 = var38_75 = this.readUnsignedShort(var30_66);
                                        var30_66 += 2;
                                        var20_36 = 0;
                                        while (var11_13 > 0) {
                                            var30_66 = this.a(var40_77, var20_36++, var30_66, var5_5, var23_44);
                                            --var11_13;
                                        }
                                    }
                                }
                                this.readLabel(var35_72 += var10_11 + 1, var23_44);
                                --var33_70;
                                continue;
                            }
                            var39_76 = null;
                        }
                        var6_6 = var4_4[var12_16] & 255;
                        switch (ClassWriter.a[var6_6]) {
                            case 0: {
                                var24_45.visitInsn(var6_6);
                                ++var12_16;
                                continue block73;
                            }
                            case 4: {
                                if (var6_6 > 54) {
                                    var24_45.visitVarInsn(54 + ((var6_6 -= 59) >> 2), var6_6 & 3);
                                } else {
                                    var24_45.visitVarInsn(21 + ((var6_6 -= 26) >> 2), var6_6 & 3);
                                }
                                ++var12_16;
                                continue block73;
                            }
                            case 9: {
                                var24_45.visitJumpInsn(var6_6, var23_44[var15_31 + this.readShort(var12_16 + 1)]);
                                var12_16 += 3;
                                continue block73;
                            }
                            case 10: {
                                var24_45.visitJumpInsn(var6_6 - 33, var23_44[var15_31 + this.readInt(var12_16 + 1)]);
                                var12_16 += 5;
                                continue block73;
                            }
                            case 17: {
                                var6_6 = var4_4[var12_16 + 1] & 255;
                                if (var6_6 == 132) {
                                    var24_45.visitIincInsn(this.readUnsignedShort(var12_16 + 2), this.readShort(var12_16 + 4));
                                    var12_16 += 6;
                                    continue block73;
                                }
                                var24_45.visitVarInsn(var6_6, this.readUnsignedShort(var12_16 + 2));
                                var12_16 += 4;
                                continue block73;
                            }
                            case 14: {
                                var12_16 = var12_16 + 4 - (var15_31 & 3);
                                var28_58 = var15_31 + this.readInt(var12_16);
                                var10_11 = this.readInt(var12_16 + 4);
                                var11_14 = this.readInt(var12_16 + 8);
                                var12_16 += 12;
                                var13_17 = new Label[var11_14 - var10_11 + 1];
                                for (var20_36 = 0; var20_36 < var13_17.length; ++var20_36) {
                                    var13_17[var20_36] = var23_44[var15_31 + this.readInt(var12_16)];
                                    var12_16 += 4;
                                }
                                var24_45.visitTableSwitchInsn(var10_11, var11_14, var23_44[var28_58], var13_17);
                                continue block73;
                            }
                            case 15: {
                                var12_16 = var12_16 + 4 - (var15_31 & 3);
                                var28_59 = var15_31 + this.readInt(var12_16);
                                var20_36 = this.readInt(var12_16 + 4);
                                var12_16 += 8;
                                var31_68 = new int[var20_36];
                                var64_79 = new Label[var20_36];
                                for (var20_36 = 0; var20_36 < ((Object)var31_68).length; ++var20_36) {
                                    var31_68[var20_36] = this.readInt(var12_16);
                                    var64_79[var20_36] = var23_44[var15_31 + this.readInt(var12_16 + 4)];
                                    var12_16 += 8;
                                }
                                var24_45.visitLookupSwitchInsn(var23_44[var28_59], (int[])var31_68, var64_79);
                                continue block73;
                            }
                            case 3: {
                                var24_45.visitVarInsn(var6_6, var4_4[var12_16 + 1] & 255);
                                var12_16 += 2;
                                continue block73;
                            }
                            case 1: {
                                var24_45.visitIntInsn(var6_6, var4_4[var12_16 + 1]);
                                var12_16 += 2;
                                continue block73;
                            }
                            case 2: {
                                var24_45.visitIntInsn(var6_6, this.readShort(var12_16 + 1));
                                var12_16 += 3;
                                continue block73;
                            }
                            case 11: {
                                var24_45.visitLdcInsn(this.readConst(var4_4[var12_16 + 1] & 255, var5_5));
                                var12_16 += 2;
                                continue block73;
                            }
                            case 12: {
                                v8 = this;
                                var24_45.visitLdcInsn(v8.readConst(v8.readUnsignedShort(var12_16 + 1), var5_5));
                                var12_16 += 3;
                                continue block73;
                            }
                            case 6: 
                            case 7: {
                                var65_81 = this.a[this.readUnsignedShort(var12_16 + 1)];
                                var66_82 = this.readClass(var65_81, var5_5);
                                var65_81 = this.a[this.readUnsignedShort(var65_81 + 2)];
                                var67_84 = this.readUTF8(var65_81, var5_5);
                                var68_85 = this.readUTF8(var65_81 + 2, var5_5);
                                if (var6_6 < 182) {
                                    var24_45.visitFieldInsn(var6_6, var66_82, var67_84, var68_85);
                                } else {
                                    var24_45.visitMethodInsn(var6_6, var66_82, var67_84, var68_85);
                                }
                                if (var6_6 == 185) {
                                    var12_16 += 5;
                                    continue block73;
                                }
                                var12_16 += 3;
                                continue block73;
                            }
                            case 8: {
                                var65_81 = this.a[this.readUnsignedShort(var12_16 + 1)];
                                var66_83 = var27_51[this.readUnsignedShort(var65_81)];
                                var65_81 = this.a[this.readUnsignedShort(var65_81 + 2)];
                                var67_84 = this.readUTF8(var65_81, var5_5);
                                var68_85 = this.readUTF8(var65_81 + 2, var5_5);
                                var69_86 = this.readUnsignedShort(var66_83);
                                var70_87 = (Handle)this.readConst(var69_86, var5_5);
                                var71_88 = this.readUnsignedShort(var66_83 + 2);
                                var72_89 = new Object[var71_88];
                                var66_83 += 4;
                                for (var73_90 = 0; var73_90 < var71_88; ++var73_90) {
                                    var74_91 = this.readUnsignedShort(var66_83);
                                    var72_89[var73_90] = this.readConst(var74_91, var5_5);
                                    var66_83 += 2;
                                }
                                var24_45.visitInvokeDynamicInsn(var67_84, var68_85, var70_87, var72_89);
                                var12_16 += 5;
                                continue block73;
                            }
                            case 5: {
                                var24_45.visitTypeInsn(var6_6, this.readClass(var12_16 + 1, var5_5));
                                var12_16 += 3;
                                continue block73;
                            }
                            case 13: {
                                var24_45.visitIincInsn(var4_4[var12_16 + 1] & 255, var4_4[var12_16 + 2]);
                                var12_16 += 3;
                                continue block73;
                            }
                        }
                        var24_45.visitMultiANewArrayInsn(this.readClass(var12_16 + 1, var5_5), var4_4[var12_16 + 3] & 255);
                        var12_16 += 4;
                    }
                    var28_52 = var23_44[var22_41 - var7_8];
                    if (var28_52 != null) {
                        var24_45.visitLabel((Label)var28_52);
                    }
                    if (!var18_34 && var26_50 != 0) {
                        var6_7 = null;
                        if (var29_60 != 0) {
                            var14_29 = this.readUnsignedShort(var29_60) * 3;
                            var15_31 = var29_60 + 2;
                            var6_7 = new int[var14_29];
                            while (var14_29 > 0) {
                                var6_7[--var14_29] = var15_31 + 6;
                                var6_7[--var14_29] = this.readUnsignedShort(var15_31 + 8);
                                var6_7[--var14_29] = this.readUnsignedShort(var15_31);
                                var15_31 += 10;
                            }
                        }
                        var15_31 = var26_50 + 2;
                        for (var14_30 = this.readUnsignedShort(var26_50); var14_30 > 0; --var14_30) {
                            var10_11 = this.readUnsignedShort(var15_31);
                            var11_15 = this.readUnsignedShort(var15_31 + 2);
                            var13_18 = this.readUnsignedShort(var15_31 + 8);
                            var31_68 = null;
                            if (var6_7 != null) {
                                for (var64_80 = 0; var64_80 < var6_7.length; var64_80 += 3) {
                                    if (var6_7[var64_80] != var10_11 || var6_7[var64_80 + 1] != var13_18) continue;
                                    var31_68 = this.readUTF8(var6_7[var64_80 + 2], var5_5);
                                    break;
                                }
                            }
                            var24_45.visitLocalVariable(this.readUTF8(var15_31 + 4, var5_5), this.readUTF8(var15_31 + 6, var5_5), (String)var31_68, var23_44[var10_11], var23_44[var10_11 + var11_15], var13_18);
                            var15_31 += 10;
                        }
                    }
                    while (var8_9 != null) {
                        var31_68 = var8_9.a;
                        var8_9.a = null;
                        var24_45.visitAttribute((Attribute)var8_9);
                        var8_9 = var31_68;
                    }
                    var24_45.visitMaxs(var25_47, var21_39);
                }
                if (var24_45 != null) {
                    var24_45.visitEnd();
                }
            }
            --var16_32;
        }
        var1_1.visitEnd();
    }

    private void a(int n2, String string, char[] cArray, boolean bl, MethodVisitor methodVisitor) {
        AnnotationVisitor annotationVisitor;
        int n3;
        int n4 = this.b[n2++] & 0xFF;
        int n5 = Type.getArgumentTypes(string).length - n4;
        for (n3 = 0; n3 < n5; ++n3) {
            annotationVisitor = methodVisitor.visitParameterAnnotation(n3, "Ljava/lang/Synthetic;", false);
            if (annotationVisitor == null) continue;
            annotationVisitor.visitEnd();
        }
        while (n3 < n4 + n5) {
            int n6 = this.readUnsignedShort(n2);
            n2 += 2;
            while (n6 > 0) {
                annotationVisitor = methodVisitor.visitParameterAnnotation(n3, this.readUTF8(n2, cArray), bl);
                n2 = this.a(n2 + 2, cArray, true, annotationVisitor);
                --n6;
            }
            ++n3;
        }
    }

    private int a(int n2, char[] cArray, boolean bl, AnnotationVisitor annotationVisitor) {
        int n3 = this.readUnsignedShort(n2);
        n2 += 2;
        if (bl) {
            while (n3 > 0) {
                n2 = this.a(n2 + 2, cArray, this.readUTF8(n2, cArray), annotationVisitor);
                --n3;
            }
        } else {
            while (n3 > 0) {
                n2 = this.a(n2, cArray, null, annotationVisitor);
                --n3;
            }
        }
        if (annotationVisitor != null) {
            annotationVisitor.visitEnd();
        }
        return n2;
    }

    private int a(int n2, char[] objectArray, String string, AnnotationVisitor annotationVisitor) {
        if (annotationVisitor == null) {
            switch (this.b[n2] & 0xFF) {
                case 101: {
                    return n2 + 5;
                }
                case 64: {
                    return this.a(n2 + 3, (char[])objectArray, true, null);
                }
                case 91: {
                    return this.a(n2 + 1, (char[])objectArray, false, null);
                }
            }
            return n2 + 3;
        }
        block5 : switch (this.b[n2++] & 0xFF) {
            case 68: 
            case 70: 
            case 73: 
            case 74: {
                ClassReader classReader = this;
                annotationVisitor.visit(string, classReader.readConst(classReader.readUnsignedShort(n2), (char[])objectArray));
                n2 += 2;
                break;
            }
            case 66: {
                ClassReader classReader = this;
                annotationVisitor.visit(string, new Byte((byte)classReader.readInt(classReader.a[this.readUnsignedShort(n2)])));
                n2 += 2;
                break;
            }
            case 90: {
                ClassReader classReader = this;
                annotationVisitor.visit(string, classReader.readInt(classReader.a[this.readUnsignedShort(n2)]) == 0 ? Boolean.FALSE : Boolean.TRUE);
                n2 += 2;
                break;
            }
            case 83: {
                ClassReader classReader = this;
                annotationVisitor.visit(string, new Short((short)classReader.readInt(classReader.a[this.readUnsignedShort(n2)])));
                n2 += 2;
                break;
            }
            case 67: {
                ClassReader classReader = this;
                annotationVisitor.visit(string, new Character((char)classReader.readInt(classReader.a[this.readUnsignedShort(n2)])));
                n2 += 2;
                break;
            }
            case 115: {
                annotationVisitor.visit(string, this.readUTF8(n2, (char[])objectArray));
                n2 += 2;
                break;
            }
            case 101: {
                annotationVisitor.visitEnum(string, this.readUTF8(n2, (char[])objectArray), this.readUTF8(n2 + 2, (char[])objectArray));
                n2 += 4;
                break;
            }
            case 99: {
                annotationVisitor.visit(string, Type.getType(this.readUTF8(n2, (char[])objectArray)));
                n2 += 2;
                break;
            }
            case 64: {
                n2 = this.a(n2 + 2, (char[])objectArray, true, annotationVisitor.visitAnnotation(string, this.readUTF8(n2, (char[])objectArray)));
                break;
            }
            case 91: {
                int n3 = this.readUnsignedShort(n2);
                n2 += 2;
                if (n3 == 0) {
                    return this.a(n2 - 2, (char[])objectArray, false, annotationVisitor.visitArray(string));
                }
                switch (this.b[n2++] & 0xFF) {
                    case 66: {
                        objectArray = new byte[n3];
                        for (int i2 = 0; i2 < n3; ++i2) {
                            ClassReader classReader = this;
                            objectArray[i2] = (char)classReader.readInt(classReader.a[this.readUnsignedShort(n2)]);
                            n2 += 3;
                        }
                        annotationVisitor.visit(string, objectArray);
                        --n2;
                        break block5;
                    }
                    case 90: {
                        objectArray = new boolean[n3];
                        for (int i3 = 0; i3 < n3; ++i3) {
                            ClassReader classReader = this;
                            objectArray[i3] = classReader.readInt(classReader.a[this.readUnsignedShort(n2)]) != 0 ? (char)'\u0001' : '\u0000';
                            n2 += 3;
                        }
                        annotationVisitor.visit(string, objectArray);
                        --n2;
                        break block5;
                    }
                    case 83: {
                        objectArray = new short[n3];
                        for (int i4 = 0; i4 < n3; ++i4) {
                            ClassReader classReader = this;
                            objectArray[i4] = (short)classReader.readInt(classReader.a[this.readUnsignedShort(n2)]);
                            n2 += 3;
                        }
                        annotationVisitor.visit(string, objectArray);
                        --n2;
                        break block5;
                    }
                    case 67: {
                        objectArray = new char[n3];
                        for (int i5 = 0; i5 < n3; ++i5) {
                            ClassReader classReader = this;
                            objectArray[i5] = (char)classReader.readInt(classReader.a[this.readUnsignedShort(n2)]);
                            n2 += 3;
                        }
                        annotationVisitor.visit(string, objectArray);
                        --n2;
                        break block5;
                    }
                    case 73: {
                        objectArray = new int[n3];
                        for (int i6 = 0; i6 < n3; ++i6) {
                            ClassReader classReader = this;
                            objectArray[i6] = classReader.readInt(classReader.a[this.readUnsignedShort(n2)]);
                            n2 += 3;
                        }
                        annotationVisitor.visit(string, objectArray);
                        --n2;
                        break block5;
                    }
                    case 74: {
                        objectArray = new long[n3];
                        for (int i7 = 0; i7 < n3; ++i7) {
                            ClassReader classReader = this;
                            objectArray[i7] = (char)classReader.readLong(classReader.a[this.readUnsignedShort(n2)]);
                            n2 += 3;
                        }
                        annotationVisitor.visit(string, objectArray);
                        --n2;
                        break block5;
                    }
                    case 70: {
                        objectArray = new float[n3];
                        for (int i8 = 0; i8 < n3; ++i8) {
                            ClassReader classReader = this;
                            objectArray[i8] = (char)Float.intBitsToFloat(classReader.readInt(classReader.a[this.readUnsignedShort(n2)]));
                            n2 += 3;
                        }
                        annotationVisitor.visit(string, objectArray);
                        --n2;
                        break block5;
                    }
                    case 68: {
                        objectArray = new double[n3];
                        for (int i9 = 0; i9 < n3; ++i9) {
                            ClassReader classReader = this;
                            objectArray[i9] = (char)Double.longBitsToDouble(classReader.readLong(classReader.a[this.readUnsignedShort(n2)]));
                            n2 += 3;
                        }
                        annotationVisitor.visit(string, objectArray);
                        --n2;
                        break block5;
                    }
                }
                n2 = this.a(n2 - 3, (char[])objectArray, false, annotationVisitor.visitArray(string));
            }
        }
        return n2;
    }

    private int a(Object[] objectArray, int n2, int n3, char[] cArray, Label[] labelArray) {
        int n4 = this.b[n3++] & 0xFF;
        switch (n4) {
            case 0: {
                objectArray[n2] = Opcodes.TOP;
                break;
            }
            case 1: {
                objectArray[n2] = Opcodes.INTEGER;
                break;
            }
            case 2: {
                objectArray[n2] = Opcodes.FLOAT;
                break;
            }
            case 3: {
                objectArray[n2] = Opcodes.DOUBLE;
                break;
            }
            case 4: {
                objectArray[n2] = Opcodes.LONG;
                break;
            }
            case 5: {
                objectArray[n2] = Opcodes.NULL;
                break;
            }
            case 6: {
                objectArray[n2] = Opcodes.UNINITIALIZED_THIS;
                break;
            }
            case 7: {
                objectArray[n2] = this.readClass(n3, cArray);
                n3 += 2;
                break;
            }
            default: {
                ClassReader classReader = this;
                objectArray[n2] = classReader.readLabel(classReader.readUnsignedShort(n3), labelArray);
                n3 += 2;
            }
        }
        return n3;
    }

    protected Label readLabel(int n2, Label[] labelArray) {
        if (labelArray[n2] == null) {
            labelArray[n2] = new Label();
        }
        return labelArray[n2];
    }

    private Attribute a(Attribute[] attributeArray, String string, int n2, int n3, char[] cArray, int n4, Label[] labelArray) {
        for (int i2 = 0; i2 < attributeArray.length; ++i2) {
            if (!attributeArray[i2].type.equals(string)) continue;
            return attributeArray[i2].read(this, n2, n3, cArray, n4, labelArray);
        }
        return new Attribute(string).read(this, n2, n3, null, -1, null);
    }

    public int getItemCount() {
        return this.a.length;
    }

    public int getItem(int n2) {
        return this.a[n2];
    }

    public int getMaxStringLength() {
        return this.d;
    }

    public int readByte(int n2) {
        return this.b[n2] & 0xFF;
    }

    public int readUnsignedShort(int n2) {
        byte[] byArray = this.b;
        return (this.b[n2] & 0xFF) << 8 | byArray[n2 + 1] & 0xFF;
    }

    public short readShort(int n2) {
        byte[] byArray = this.b;
        return (short)((this.b[n2] & 0xFF) << 8 | byArray[n2 + 1] & 0xFF);
    }

    public int readInt(int n2) {
        byte[] byArray = this.b;
        return (this.b[n2] & 0xFF) << 24 | (byArray[n2 + 1] & 0xFF) << 16 | (byArray[n2 + 2] & 0xFF) << 8 | byArray[n2 + 3] & 0xFF;
    }

    public long readLong(int n2) {
        long l2 = this.readInt(n2);
        long l3 = (long)this.readInt(n2 + 4) & 0xFFFFFFFFL;
        return l2 << 32 | l3;
    }

    public String readUTF8(int n2, char[] cArray) {
        int n3 = this.readUnsignedShort(n2);
        String string = this.c[n3];
        if (string != null) {
            return string;
        }
        int n4 = this.a[n3];
        this.c[n3] = this.a(n4 + 2, this.readUnsignedShort(n4), cArray);
        return this.c[n3];
    }

    private String a(int n2, int n3, char[] cArray) {
        n3 = n2 + n3;
        byte[] byArray = this.b;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        while (n2 < n3) {
            int n7 = byArray[n2++];
            switch (n5) {
                case 0: {
                    if ((n7 &= 0xFF) < 128) {
                        cArray[n4++] = (char)n7;
                        break;
                    }
                    if (n7 < 224 && n7 > 191) {
                        n6 = (char)(n7 & 0x1F);
                        n5 = 1;
                        break;
                    }
                    n6 = (char)(n7 & 0xF);
                    n5 = 2;
                    break;
                }
                case 1: {
                    cArray[n4++] = (char)(n6 << 6 | n7 & 0x3F);
                    n5 = 0;
                    break;
                }
                case 2: {
                    n6 = (char)(n6 << 6 | n7 & 0x3F);
                    n5 = 1;
                }
            }
        }
        return new String(cArray, 0, n4);
    }

    public String readClass(int n2, char[] cArray) {
        ClassReader classReader = this;
        return classReader.readUTF8(classReader.a[this.readUnsignedShort(n2)], cArray);
    }

    public Object readConst(int n2, char[] cArray) {
        n2 = this.a[n2];
        switch (this.b[n2 - 1]) {
            case 3: {
                return new Integer(this.readInt(n2));
            }
            case 4: {
                return new Float(Float.intBitsToFloat(this.readInt(n2)));
            }
            case 5: {
                return new Long(this.readLong(n2));
            }
            case 6: {
                return new Double(Double.longBitsToDouble(this.readLong(n2)));
            }
            case 7: {
                return Type.getObjectType(this.readUTF8(n2, cArray));
            }
            case 8: {
                return this.readUTF8(n2, cArray);
            }
            case 16: {
                return Type.getMethodType(this.readUTF8(n2, cArray));
            }
        }
        int n3 = this.readByte(n2);
        Object object = this.a;
        n2 = this.a[this.readUnsignedShort(n2 + 1)];
        String string = this.readClass(n2, cArray);
        n2 = object[this.readUnsignedShort(n2 + 2)];
        object = this.readUTF8(n2, cArray);
        String string2 = this.readUTF8(n2 + 2, cArray);
        return new Handle(n3, string, (String)object, string2);
    }
}


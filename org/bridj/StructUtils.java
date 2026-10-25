/*
 * Decompiled with CFR 0.152.
 */
package org.bridj;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import org.bridj.BridJ;
import org.bridj.NativeObject;
import org.bridj.Platform;
import org.bridj.Pointer;
import org.bridj.PointerIO;
import org.bridj.SolidRanges;
import org.bridj.StructCustomizer;
import org.bridj.StructDescription;
import org.bridj.StructFieldDeclaration;
import org.bridj.StructFieldDescription;
import org.bridj.StructIO;
import org.bridj.StructObject;
import org.bridj.ann.Alignment;
import org.bridj.ann.Struct;
import org.bridj.util.Pair;
import org.bridj.util.Utils;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
class StructUtils {
    StructUtils() {
    }

    static long alignSize(long l2, long l3) {
        long l4;
        if (l3 > 1L && (l4 = l2 % l3) != 0L) {
            l2 += l3 - l4;
        }
        return l2;
    }

    static void orderFields(List<StructFieldDeclaration> list2) {
        Collections.sort(list2, new Comparator<StructFieldDeclaration>(){

            @Override
            public final int compare(StructFieldDeclaration structFieldDeclaration, StructFieldDeclaration structFieldDeclaration2) {
                long l2 = structFieldDeclaration.index - structFieldDeclaration2.index;
                if (l2 != 0L) {
                    if (l2 < 0L) {
                        return -1;
                    }
                    if (l2 == 0L) {
                        return 0;
                    }
                    return 1;
                }
                if (structFieldDeclaration.declaringClass.isAssignableFrom(structFieldDeclaration2.declaringClass)) {
                    return -1;
                }
                if (structFieldDeclaration2.declaringClass.isAssignableFrom(structFieldDeclaration.declaringClass)) {
                    return 1;
                }
                throw new RuntimeException("Failed to order fields " + structFieldDeclaration2.desc.name + " and " + structFieldDeclaration2.desc.name);
            }
        });
    }

    static long primTypeAlignment(Class<?> clazz, long l2) {
        if (StructUtils.isDouble(clazz) && !BridJ.alignDoubles && Platform.isLinux() && !Platform.is64Bits()) {
            return 4L;
        }
        return l2;
    }

    static boolean isDouble(Class<?> clazz) {
        return clazz == Double.class || clazz == Double.TYPE;
    }

    static int primTypeLength(Class<?> clazz) {
        if (clazz == Integer.class || clazz == Integer.TYPE) {
            return 4;
        }
        if (clazz == Long.class || clazz == Long.TYPE) {
            return 8;
        }
        if (clazz == Short.class || clazz == Short.TYPE) {
            return 2;
        }
        if (clazz == Byte.class || clazz == Byte.TYPE) {
            return 1;
        }
        if (clazz == Character.class || clazz == Character.TYPE) {
            return 2;
        }
        if (clazz == Boolean.class || clazz == Boolean.TYPE) {
            return 1;
        }
        if (clazz == Float.class || clazz == Float.TYPE) {
            return 4;
        }
        if (StructUtils.isDouble(clazz)) {
            return 8;
        }
        if (Pointer.class.isAssignableFrom(clazz)) {
            return Pointer.SIZE;
        }
        throw new UnsupportedOperationException("Field type " + clazz.getName() + " not supported yet");
    }

    static long alignmentOf(Type type) {
        Class<?> clazz = PointerIO.getClass(type);
        if (StructObject.class.isAssignableFrom(clazz)) {
            return StructIO.getInstance(clazz).desc.getStructAlignment();
        }
        return BridJ.sizeOf(type);
    }

    static int compare(StructObject object, StructObject object2, SolidRanges object3) {
        object = Pointer.getPointer(object);
        object2 = Pointer.getPointer(object2);
        if (object == null || object2 == null) {
            if (object != null) {
                return 1;
            }
            if (object2 != null) {
                return -1;
            }
            return 0;
        }
        long[] lArray = ((SolidRanges)object3).offsets;
        object3 = ((SolidRanges)object3).lengths;
        int n2 = lArray.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            long l2 = lArray[i2];
            Object object4 = object3[i2];
            int n3 = ((Pointer)object).compareBytesAtOffset(l2, (Pointer<?>)object2, l2, (long)object4);
            if (n3 == 0) continue;
            return n3;
        }
        return 0;
    }

    static String describe(StructObject structObject, Type structFieldDescriptionArray, StructFieldDescription[] structFieldDescriptionArray2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(StructUtils.describe((Type)structFieldDescriptionArray)).append(" { ");
        structFieldDescriptionArray = structFieldDescriptionArray2;
        int n2 = structFieldDescriptionArray2.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            Object object = structFieldDescriptionArray[i2];
            stringBuilder.append("\n\t").append(((StructFieldDescription)object).name).append(" = ");
            try {
                object = ((StructFieldDescription)object).getter != null ? ((StructFieldDescription)object).getter.invoke(structObject, new Object[0]) : ((StructFieldDescription)object).field.get(structObject);
                if (object instanceof String) {
                    stringBuilder.append('\"').append(object.toString().replaceAll("\"", "\\\"")).append('\"');
                } else if (object instanceof Character) {
                    stringBuilder.append('\'').append(object).append('\'');
                } else if (object instanceof NativeObject) {
                    object = BridJ.describe((NativeObject)object);
                    stringBuilder.append(((String)object).replaceAll("\n", "\n\t"));
                } else {
                    stringBuilder.append(object);
                }
            }
            catch (Throwable throwable) {
                if (BridJ.debug) {
                    throwable.printStackTrace();
                }
                stringBuilder.append("?");
            }
            stringBuilder.append("; ");
        }
        stringBuilder.append("\n}");
        return stringBuilder.toString();
    }

    static String describe(Type type) {
        if (type == null) {
            return "?";
        }
        if (type instanceof Class) {
            return ((Class)type).getSimpleName();
        }
        return type.toString().replaceAll("\\bjava\\.lang\\.", "").replaceAll("\\borg\\.bridj\\.cpp\\.com\\.", "").replaceAll("\\borg\\.bridj\\.Pointer\\b", "Pointer");
    }

    static Pointer fixIntegralTypeIOToMatchLength(Pointer pointer, long l2, long l3) {
        long l4 = pointer.getTargetSize();
        if (l4 * l3 == l2) {
            return pointer;
        }
        Type type = pointer.getTargetType();
        if (!Utils.isSignedIntegral(type)) {
            return pointer;
        }
        switch ((int)l2) {
            case 1: {
                return pointer.as(Byte.TYPE);
            }
            case 2: {
                return pointer.as(Short.TYPE);
            }
            case 4: {
                return pointer.as(Integer.TYPE);
            }
            case 8: {
                return pointer.as(Long.TYPE);
            }
        }
        return pointer;
    }

    protected static void computeStructLayout(StructDescription structDescription, StructCustomizer object) {
        Object object2;
        Object object4 = StructFieldDeclaration.listFields(structDescription.structClass);
        StructUtils.orderFields(object4);
        ((StructCustomizer)((Object)object)).beforeAggregation(structDescription, (List<StructFieldDeclaration>)object4);
        Object object522 = new LinkedHashMap();
        object4 = object4.iterator();
        while (object4.hasNext()) {
            StructFieldDeclaration object32 = (StructFieldDeclaration)object4.next();
            if (object32.index < 0L) {
                throw new RuntimeException("Negative field index not allowed for field " + object32.desc.name);
            }
            long l2 = object32.unionWith >= 0L ? object32.unionWith : object32.index;
            object2 = new Pair(object32.declaringClass, l2);
            ArrayList<StructFieldDeclaration> arrayList2 = (ArrayList<StructFieldDeclaration>)object522.get(object2);
            if (arrayList2 == null) {
                arrayList2 = new ArrayList<StructFieldDeclaration>();
                object522.put(object2, arrayList2);
            }
            arrayList2.add(object32);
        }
        object4 = structDescription.structClass.getAnnotation(Alignment.class);
        structDescription.structAlignment = object4 != null ? (long)object4.value() : 1L;
        ArrayList<StructFieldDescription> arrayList = new ArrayList<StructFieldDescription>();
        for (Object object522 : object522.values()) {
            object2 = StructFieldDescription.aggregateDeclarations(structDescription.structType, (List<StructFieldDeclaration>)object522);
            if (object2 == null) continue;
            arrayList.add((StructFieldDescription)object2);
        }
        structDescription.setAggregatedFields(arrayList);
        ((StructCustomizer)((Object)object)).beforeLayout(structDescription, arrayList);
        StructUtils.performLayout(structDescription, arrayList);
        ((StructCustomizer)((Object)object)).afterLayout(structDescription, arrayList);
        object4 = new ArrayList();
        object522 = new SolidRanges.Builder();
        for (ArrayList<StructFieldDeclaration> arrayList2 : arrayList) {
            for (StructFieldDeclaration structFieldDeclaration : ((StructFieldDescription)((Object)arrayList2)).aggregatedFields) {
                StructFieldDescription structFieldDescription = structFieldDeclaration.desc;
                structFieldDeclaration.desc.byteOffset = ((StructFieldDescription)((Object)arrayList2)).byteOffset;
                if (structFieldDescription.byteLength < 0L || structFieldDescription.byteLength > ((StructFieldDescription)((Object)arrayList2)).byteLength) {
                    structFieldDescription.byteLength = ((StructFieldDescription)((Object)arrayList2)).byteLength;
                }
                structFieldDescription.bitOffset = ((StructFieldDescription)((Object)arrayList2)).bitOffset;
                structFieldDescription.bitMask = ((StructFieldDescription)((Object)arrayList2)).bitMask;
                object4.add(structFieldDescription);
                ((SolidRanges.Builder)object522).add(structFieldDescription);
                structDescription.hasFieldFields = structDescription.hasFieldFields || structFieldDescription.field != null;
            }
        }
        structDescription.solidRanges = ((SolidRanges.Builder)object522).toSolidRanges();
        Object object3 = object4;
        structDescription.fields = object3.toArray(new StructFieldDescription[object3.size()]);
    }

    /*
     * Unable to fully structure code
     */
    static void performLayout(StructDescription var0, Iterable<StructFieldDescription> var1_1) {
        block11: {
            block10: {
                var2_2 = 0L;
                var4_3 = -1L;
                var6_4 = var0.structClass.getAnnotation(Struct.class);
                v0 = var6_5 = var6_4 != null ? var6_4.pack() : -1;
                if (var0.isVirtual()) {
                    var2_2 = 0L + (long)Pointer.SIZE;
                    if ((long)Pointer.SIZE >= -1L) {
                        var4_3 = Pointer.SIZE;
                    }
                }
                var7_6 = 0;
                var1_1 = var1_1.iterator();
                while (var1_1.hasNext()) {
                    var8_7 = (StructFieldDescription)var1_1.next();
                    var4_3 = Math.max(var4_3, var8_7.alignment);
                    if (var8_7.bitLength < 0L) {
                        if (var7_6 != 0) {
                            var7_6 = 0;
                            ++var2_2;
                        }
                        var2_2 = StructUtils.alignSize(var2_2, var6_5 > 0 ? (long)var6_5 : var8_7.alignment);
                    }
                    var11_8 = var2_2;
                    var13_9 = var7_6;
                    if (var8_7.bitLength >= 0L) {
                        var7_6 = (int)((long)var7_6 + var8_7.bitLength);
                        var2_2 += (long)(var7_6 >>> 3);
                        var7_6 &= 7;
                    } else {
                        var2_2 += var8_7.byteLength;
                    }
                    var8_7.byteOffset = var11_8;
                    var8_7.bitOffset = var13_9;
                    var8_7.computeBitMask();
                }
                if (var7_6 <= 0) break block10;
                v1 = var2_2 + 1L;
                ** GOTO lbl-1000
            }
            if (var2_2 > 0L) {
                v1 = var2_2;
                ** if (var6_5 <= 0) goto lbl-1000
lbl-1000:
                // 1 sources

                {
                    v2 = (long)var6_5;
                    ** GOTO lbl42
                }
            }
            break block11;
lbl-1000:
            // 2 sources

            {
                v2 = var4_3;
            }
lbl42:
            // 2 sources

            var2_2 = StructUtils.alignSize(v1, v2);
        }
        var0.structSize = var2_2;
        var0.structAlignment = var4_3;
    }
}


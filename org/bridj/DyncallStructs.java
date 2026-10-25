/*
 * Decompiled with CFR 0.152.
 */
package org.bridj;

import java.lang.reflect.Type;
import java.util.List;
import org.bridj.BridJ;
import org.bridj.Pointer;
import org.bridj.StructDescription;
import org.bridj.StructFieldDeclaration;
import org.bridj.StructFieldDescription;
import org.bridj.StructIO;
import org.bridj.StructObject;
import org.bridj.dyncall.DyncallLibrary;
import org.bridj.util.Utils;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
class DyncallStructs {
    Pointer<DyncallLibrary.DCstruct> struct;

    DyncallStructs() {
    }

    static int toDCAlignment(long l2) {
        if (l2 <= 0L) {
            return 0;
        }
        return (int)l2;
    }

    public static Pointer<DyncallLibrary.DCstruct> buildDCstruct(StructDescription structDescription) {
        if (!BridJ.Switch.StructsByValue.enabled) {
            return null;
        }
        List<StructFieldDescription> list2 = structDescription.getAggregatedFields();
        Pointer<DyncallLibrary.DCstruct> pointer = DyncallLibrary.dcNewStruct(list2.size(), DyncallStructs.toDCAlignment(structDescription.getStructAlignment())).withReleaser(new Pointer.Releaser(){

            @Override
            public final void release(Pointer<?> pointer) {
                DyncallLibrary.dcFreeStruct(pointer.as(DyncallLibrary.DCstruct.class));
            }
        });
        DyncallStructs.fillDCstruct(structDescription.structType, pointer, list2);
        DyncallLibrary.dcCloseStruct(pointer);
        long l2 = structDescription.getStructSize();
        long l3 = DyncallLibrary.dcStructSize(pointer);
        if (l2 != l3) {
            BridJ.error("Struct size computed for " + Utils.toString(structDescription.structType) + " by BridJ (" + l2 + " bytes) and dyncall (" + l3 + " bytes) differ !");
            return null;
        }
        return pointer;
    }

    protected static void fillDCstruct(Type type, Pointer<DyncallLibrary.DCstruct> pointer, List<StructFieldDescription> object) {
        object = object.iterator();
        while (object.hasNext()) {
            int n2;
            StructFieldDescription structFieldDescription = (StructFieldDescription)object.next();
            Object object2 = structFieldDescription.aggregatedFields.get(0);
            Object object3 = ((StructFieldDeclaration)object2).desc.nativeTypeOrPointerTargetType;
            if (object3 == null) {
                object3 = ((StructFieldDeclaration)object2).desc.valueType;
            }
            Class clazz = Utils.getClass((Type)object3);
            int n3 = DyncallStructs.toDCAlignment(structFieldDescription.alignment);
            long l2 = ((StructFieldDeclaration)object2).desc.arrayLength;
            if (StructObject.class.isAssignableFrom(clazz)) {
                object2 = StructIO.getInstance(clazz, (Type)object3);
                object3 = ((StructIO)object2).desc.getAggregatedFields();
                DyncallLibrary.dcSubStruct(pointer, object3.size(), n3, l2);
                try {
                    DyncallStructs.fillDCstruct(((StructIO)object2).desc.structType, pointer, (List<StructFieldDescription>)object3);
                    continue;
                }
                finally {
                    DyncallLibrary.dcCloseStruct(pointer);
                    continue;
                }
            }
            if (clazz == Integer.TYPE) {
                n2 = 105;
            } else if (clazz == Long.TYPE || clazz == Long.class) {
                n2 = 108;
            } else if (clazz == Short.TYPE || clazz == Character.TYPE || clazz == Short.class || clazz == Character.class) {
                n2 = 115;
            } else if (clazz == Byte.TYPE || clazz == Boolean.TYPE || clazz == Byte.class || clazz == Boolean.class) {
                n2 = 99;
            } else if (clazz == Float.TYPE || clazz == Float.class) {
                n2 = 102;
            } else if (clazz == Double.TYPE || clazz == Double.class) {
                n2 = 100;
            } else if (Pointer.class.isAssignableFrom(clazz)) {
                n2 = 112;
            } else {
                throw new IllegalArgumentException("Unable to create dyncall struct field for type " + Utils.toString((Type)object3) + " in struct " + Utils.toString(type));
            }
            DyncallLibrary.dcStructField(pointer, n2, n3, l2);
        }
    }
}


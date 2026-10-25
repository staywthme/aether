/*
 * Decompiled with CFR 0.152.
 */
package org.bridj;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import org.bridj.BridJ;
import org.bridj.CLong;
import org.bridj.FlagSet;
import org.bridj.IntValuedEnum;
import org.bridj.NativeObject;
import org.bridj.Pointer;
import org.bridj.PointerIO;
import org.bridj.SizeT;
import org.bridj.StructCustomizer;
import org.bridj.StructDescription;
import org.bridj.StructFieldDescription;
import org.bridj.StructObject;
import org.bridj.StructUtils;
import org.bridj.TimeT;
import org.bridj.TypedPointer;
import org.bridj.ValuedEnum;
import org.bridj.util.Utils;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class StructIO {
    static Map<Type, StructIO> structIOs = new HashMap<Type, StructIO>();
    protected PointerIO<?> pointerIO;
    public final StructDescription desc;

    public static StructIO getInstance(Type type) {
        return StructIO.getInstance(Utils.getClass(type), type);
    }

    public static StructIO getInstance(Class clazz, Type type) {
        Map<Type, StructIO> map = structIOs;
        synchronized (map) {
            StructIO structIO = structIOs.get(type == null ? clazz : type);
            if (structIO == null) {
                structIO = new StructIO(clazz, type);
                StructIO.registerStructIO(clazz, type, structIO);
            }
            return structIO;
        }
    }

    public static synchronized StructIO registerStructIO(Class clazz, Type type, StructIO structIO) {
        structIOs.put(type, structIO);
        return structIO;
    }

    public StructIO(Class<?> clazz, Type type) {
        this.desc = new StructDescription(clazz, type, StructCustomizer.getInstance(clazz));
    }

    public String toString() {
        return "StructIO(" + this.desc + ")";
    }

    public boolean equal(StructObject structObject, StructObject structObject2) {
        return this.compare(structObject, structObject2) == 0;
    }

    public int compare(StructObject structObject, StructObject structObject2) {
        return StructUtils.compare(structObject, structObject2, this.desc.getSolidRanges());
    }

    public final String describe(StructObject structObject) {
        return this.desc.describe(structObject);
    }

    public final void writeFieldsToNative(StructObject structObject) {
        this.desc.build();
        if (!this.desc.hasFieldFields) {
            return;
        }
        try {
            StructFieldDescription[] structFieldDescriptionArray = this.desc.fields;
            int n2 = this.desc.fields.length;
            for (int i2 = 0; i2 < n2; ++i2) {
                StructFieldDescription structFieldDescription = structFieldDescriptionArray[i2];
                if (structFieldDescription.field == null || structFieldDescription.isArray) continue;
                Object object = structFieldDescription.field.get(structObject);
                if (object instanceof NativeObject) {
                    if (object == null) continue;
                    BridJ.writeToNative((NativeObject)object);
                    continue;
                }
                Pointer pointer = structObject.peer.offset(structFieldDescription.byteOffset);
                Type type = structFieldDescription.isNativeObject || structFieldDescription.isArray ? structFieldDescription.nativeTypeOrPointerTargetType : structFieldDescription.field.getGenericType();
                pointer = pointer.as(type);
                pointer = StructUtils.fixIntegralTypeIOToMatchLength(pointer, structFieldDescription.byteLength, structFieldDescription.arrayLength);
                if (structFieldDescription.isCLong && CLong.SIZE == 4 || structFieldDescription.isSizeT && SizeT.SIZE == 4) {
                    object = (int)((Long)object).longValue();
                }
                pointer.set(object);
            }
            return;
        }
        catch (Throwable throwable) {
            throw new RuntimeException("Unexpected error while writing fields from struct " + Utils.toString(this.desc.structType) + " (" + Pointer.getPointer(structObject) + ")", throwable);
        }
    }

    public final void readFieldsFromNative(StructObject structObject) {
        this.desc.build();
        if (!this.desc.hasFieldFields) {
            return;
        }
        try {
            StructFieldDescription[] structFieldDescriptionArray = this.desc.fields;
            int n2 = this.desc.fields.length;
            for (int i2 = 0; i2 < n2; ++i2) {
                StructFieldDescription structFieldDescription = structFieldDescriptionArray[i2];
                if (structFieldDescription.field == null) continue;
                Pointer pointer = structObject.peer.offset(structFieldDescription.byteOffset);
                Type type = structFieldDescription.isNativeObject || structFieldDescription.isArray ? structFieldDescription.nativeTypeOrPointerTargetType : structFieldDescription.field.getGenericType();
                pointer = pointer.as(type);
                pointer = StructUtils.fixIntegralTypeIOToMatchLength(pointer, structFieldDescription.byteLength, structFieldDescription.arrayLength);
                pointer = structFieldDescription.isArray ? pointer.validElements(structFieldDescription.arrayLength) : pointer.get();
                structFieldDescription.field.set(structObject, pointer);
                if (!(pointer instanceof NativeObject) || pointer == null) continue;
                BridJ.readFromNative((NativeObject)((Object)pointer));
            }
            return;
        }
        catch (Throwable throwable) {
            throw new RuntimeException("Unexpected error while reading fields from struct " + Utils.toString(this.desc.structType) + " (" + Pointer.getPointer(structObject) + ") : " + throwable, throwable);
        }
    }

    public final <T> Pointer<T> getPointerField(StructObject pointer, int n2) {
        StructFieldDescription structFieldDescription = this.desc.fields[n2];
        if (structFieldDescription.isArray) {
            pointer = ((NativeObject)((Object)pointer)).peer.offset(structFieldDescription.byteOffset).as(structFieldDescription.nativeTypeOrPointerTargetType);
            pointer = pointer.validElements(structFieldDescription.arrayLength);
        } else {
            pointer = ((NativeObject)((Object)pointer)).peer.getPointerAtOffset(structFieldDescription.byteOffset, structFieldDescription.nativeTypeOrPointerTargetType);
        }
        return pointer;
    }

    public final <T> void setPointerField(StructObject structObject, int n2, Pointer<T> pointer) {
        StructFieldDescription structFieldDescription = this.desc.fields[n2];
        structObject.peer.setPointerAtOffset(structFieldDescription.byteOffset, pointer);
    }

    public final <T extends TypedPointer> T getTypedPointerField(StructObject structObject, int n2) {
        StructFieldDescription structFieldDescription = this.desc.fields[n2];
        PointerIO pointerIO = PointerIO.getInstance(structFieldDescription.nativeTypeOrPointerTargetType);
        return (T)((TypedPointer)pointerIO.castTarget(structObject.peer.getSizeTAtOffset(structFieldDescription.byteOffset)));
    }

    public final <O extends NativeObject> O getNativeObjectField(StructObject structObject, int n2) {
        StructFieldDescription structFieldDescription = this.desc.fields[n2];
        return structObject.peer.offset(structFieldDescription.byteOffset).getNativeObject(structFieldDescription.nativeTypeOrPointerTargetType);
    }

    public final <O extends NativeObject> void setNativeObjectField(StructObject structObject, int n2, O o2) {
        StructFieldDescription structFieldDescription = this.desc.fields[n2];
        structObject.peer.offset(structFieldDescription.byteOffset).setNativeObject(o2, structFieldDescription.nativeTypeOrPointerTargetType);
    }

    public final <E extends Enum<E>> IntValuedEnum<E> getEnumField(StructObject structObject, int n2) {
        StructFieldDescription structFieldDescription = this.desc.fields[n2];
        return FlagSet.fromValue(structObject.peer.getIntAtOffset(structFieldDescription.byteOffset), (Class)structFieldDescription.nativeTypeOrPointerTargetType);
    }

    public final void setEnumField(StructObject structObject, int n2, ValuedEnum<?> valuedEnum) {
        StructFieldDescription structFieldDescription = this.desc.fields[n2];
        structObject.peer.setIntAtOffset(structFieldDescription.byteOffset, (int)valuedEnum.value());
    }

    private void setSignedIntegral(Pointer<?> pointer, long l2, long l3, long l4, long l5, long l6) {
        if (l4 != -1L) {
            long l7 = pointer.getSignedIntegralAtOffset(l2, l3);
            l6 <<= (int)l5;
            l6 = l7 & (l4 ^ 0xFFFFFFFFFFFFFFFFL) | l6 & l4;
        }
        pointer.setSignedIntegralAtOffset(l2, l6, l3);
    }

    public final void setIntField(StructObject structObject, int n2, int n3) {
        StructFieldDescription structFieldDescription = this.desc.fields[n2];
        if (4L != structFieldDescription.byteLength || structFieldDescription.bitMask != -1L) {
            this.setSignedIntegral(structObject.peer, structFieldDescription.byteOffset, structFieldDescription.byteLength, structFieldDescription.bitMask, structFieldDescription.bitOffset, n3);
            return;
        }
        structObject.peer.setIntAtOffset(structFieldDescription.byteOffset, n3);
    }

    public final int getIntField(StructObject structObject, int n2) {
        StructFieldDescription structFieldDescription = this.desc.fields[n2];
        int n3 = 4L != structFieldDescription.byteLength ? (int)structObject.peer.getSignedIntegralAtOffset(structFieldDescription.byteOffset, structFieldDescription.byteLength) : structObject.peer.getIntAtOffset(structFieldDescription.byteOffset);
        return (int)(((long)n3 & structFieldDescription.bitMask) >> (int)structFieldDescription.bitOffset);
    }

    public final void setLongField(StructObject structObject, int n2, long l2) {
        StructFieldDescription structFieldDescription = this.desc.fields[n2];
        if (8L != structFieldDescription.byteLength || structFieldDescription.bitMask != -1L) {
            this.setSignedIntegral(structObject.peer, structFieldDescription.byteOffset, structFieldDescription.byteLength, structFieldDescription.bitMask, structFieldDescription.bitOffset, l2);
            return;
        }
        structObject.peer.setLongAtOffset(structFieldDescription.byteOffset, l2);
    }

    public final long getLongField(StructObject structObject, int n2) {
        StructFieldDescription structFieldDescription = this.desc.fields[n2];
        long l2 = 8L != structFieldDescription.byteLength ? structObject.peer.getSignedIntegralAtOffset(structFieldDescription.byteOffset, structFieldDescription.byteLength) : structObject.peer.getLongAtOffset(structFieldDescription.byteOffset);
        return (l2 & structFieldDescription.bitMask) >> (int)structFieldDescription.bitOffset;
    }

    public final void setShortField(StructObject structObject, int n2, short s2) {
        StructFieldDescription structFieldDescription = this.desc.fields[n2];
        if (2L != structFieldDescription.byteLength || structFieldDescription.bitMask != -1L) {
            this.setSignedIntegral(structObject.peer, structFieldDescription.byteOffset, structFieldDescription.byteLength, structFieldDescription.bitMask, structFieldDescription.bitOffset, s2);
            return;
        }
        structObject.peer.setShortAtOffset(structFieldDescription.byteOffset, s2);
    }

    public final short getShortField(StructObject structObject, int n2) {
        StructFieldDescription structFieldDescription = this.desc.fields[n2];
        short s2 = 2L != structFieldDescription.byteLength ? (short)structObject.peer.getSignedIntegralAtOffset(structFieldDescription.byteOffset, structFieldDescription.byteLength) : structObject.peer.getShortAtOffset(structFieldDescription.byteOffset);
        return (short)(((long)s2 & structFieldDescription.bitMask) >> (int)structFieldDescription.bitOffset);
    }

    public final void setByteField(StructObject structObject, int n2, byte by) {
        StructFieldDescription structFieldDescription = this.desc.fields[n2];
        if (1L != structFieldDescription.byteLength || structFieldDescription.bitMask != -1L) {
            this.setSignedIntegral(structObject.peer, structFieldDescription.byteOffset, structFieldDescription.byteLength, structFieldDescription.bitMask, structFieldDescription.bitOffset, by);
            return;
        }
        structObject.peer.setByteAtOffset(structFieldDescription.byteOffset, by);
    }

    public final byte getByteField(StructObject structObject, int n2) {
        StructFieldDescription structFieldDescription = this.desc.fields[n2];
        byte by = 1L != structFieldDescription.byteLength ? (byte)structObject.peer.getSignedIntegralAtOffset(structFieldDescription.byteOffset, structFieldDescription.byteLength) : structObject.peer.getByteAtOffset(structFieldDescription.byteOffset);
        return (byte)(((long)by & structFieldDescription.bitMask) >> (int)structFieldDescription.bitOffset);
    }

    public final void setCharField(StructObject structObject, int n2, char c2) {
        StructFieldDescription structFieldDescription = this.desc.fields[n2];
        structObject.peer.setCharAtOffset(structFieldDescription.byteOffset, c2);
    }

    public final char getCharField(StructObject structObject, int n2) {
        StructFieldDescription structFieldDescription = this.desc.fields[n2];
        char c2 = structObject.peer.getCharAtOffset(structFieldDescription.byteOffset);
        return c2;
    }

    public final void setFloatField(StructObject structObject, int n2, float f2) {
        StructFieldDescription structFieldDescription = this.desc.fields[n2];
        structObject.peer.setFloatAtOffset(structFieldDescription.byteOffset, f2);
    }

    public final float getFloatField(StructObject structObject, int n2) {
        StructFieldDescription structFieldDescription = this.desc.fields[n2];
        float f2 = structObject.peer.getFloatAtOffset(structFieldDescription.byteOffset);
        return f2;
    }

    public final void setDoubleField(StructObject structObject, int n2, double d2) {
        StructFieldDescription structFieldDescription = this.desc.fields[n2];
        structObject.peer.setDoubleAtOffset(structFieldDescription.byteOffset, d2);
    }

    public final double getDoubleField(StructObject structObject, int n2) {
        StructFieldDescription structFieldDescription = this.desc.fields[n2];
        double d2 = structObject.peer.getDoubleAtOffset(structFieldDescription.byteOffset);
        return d2;
    }

    public final void setBooleanField(StructObject structObject, int n2, boolean bl) {
        StructFieldDescription structFieldDescription = this.desc.fields[n2];
        structObject.peer.setBooleanAtOffset(structFieldDescription.byteOffset, bl);
    }

    public final boolean getBooleanField(StructObject structObject, int n2) {
        StructFieldDescription structFieldDescription = this.desc.fields[n2];
        boolean bl = structObject.peer.getBooleanAtOffset(structFieldDescription.byteOffset);
        return bl;
    }

    public final void setSizeTField(StructObject structObject, int n2, long l2) {
        StructFieldDescription structFieldDescription = this.desc.fields[n2];
        structObject.peer.setSizeTAtOffset(structFieldDescription.byteOffset, l2);
    }

    public final long getSizeTField(StructObject structObject, int n2) {
        StructFieldDescription structFieldDescription = this.desc.fields[n2];
        return structObject.peer.getSizeTAtOffset(structFieldDescription.byteOffset);
    }

    public final void setCLongField(StructObject structObject, int n2, long l2) {
        StructFieldDescription structFieldDescription = this.desc.fields[n2];
        structObject.peer.setCLongAtOffset(structFieldDescription.byteOffset, l2);
    }

    public final long getCLongField(StructObject structObject, int n2) {
        StructFieldDescription structFieldDescription = this.desc.fields[n2];
        return structObject.peer.getCLongAtOffset(structFieldDescription.byteOffset);
    }

    public final void setTimeTField(StructObject structObject, int n2, TimeT timeT) {
        StructFieldDescription structFieldDescription = this.desc.fields[n2];
        structObject.peer.setIntegralAtOffset(structFieldDescription.byteOffset, timeT);
    }

    public final TimeT getTimeTField(StructObject structObject, int n2) {
        StructFieldDescription structFieldDescription = this.desc.fields[n2];
        return new TimeT(structObject.peer.getIntegralAtOffset(structFieldDescription.byteOffset, TimeT.SIZE));
    }
}


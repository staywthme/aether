/*
 * Decompiled with CFR 0.152.
 */
package org.bridj;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.bridj.BridJ;
import org.bridj.CLong;
import org.bridj.IntValuedEnum;
import org.bridj.Pointer;
import org.bridj.PointerIO;
import org.bridj.SizeT;
import org.bridj.StructDescription;
import org.bridj.StructFieldDeclaration;
import org.bridj.StructIO;
import org.bridj.StructObject;
import org.bridj.StructUtils;
import org.bridj.TypedPointer;
import org.bridj.ValuedEnum;
import org.bridj.cpp.CPPObject;
import org.bridj.cpp.CPPType;
import org.bridj.util.DefaultParameterizedType;
import org.bridj.util.Utils;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class StructFieldDescription {
    public final List<StructFieldDeclaration> aggregatedFields = new ArrayList<StructFieldDeclaration>();
    public long alignment = -1L;
    public long byteOffset = -1L;
    public long byteLength = -1L;
    public long bitOffset = 0L;
    public long bitLength = -1L;
    public long arrayLength = 1L;
    public long bitMask = -1L;
    public boolean isArray;
    public boolean isNativeObject;
    public Type nativeTypeOrPointerTargetType;
    public Field field;
    Type valueType;
    Method getter;
    String name;
    boolean isCLong;
    boolean isSizeT;

    public void offset(long l2) {
        this.byteOffset += l2;
    }

    public String toString() {
        return "Field(byteOffset = " + this.byteOffset + ", byteLength = " + this.byteLength + ", bitOffset = " + this.bitOffset + ", bitLength = " + this.bitLength + (this.nativeTypeOrPointerTargetType == null ? "" : ", ttype = " + this.nativeTypeOrPointerTargetType) + ")";
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    static Type resolveType(Type type, Type type2) {
        if (type == null || type instanceof WildcardType) {
            return null;
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType)type;
            Type[] typeArray = parameterizedType.getActualTypeArguments();
            Object[] objectArray = new Type[typeArray.length];
            for (int i2 = 0; i2 < typeArray.length; ++i2) {
                objectArray[i2] = StructFieldDescription.resolveType(typeArray[i2], type2);
            }
            Type type3 = StructFieldDescription.resolveType(parameterizedType.getOwnerType(), type2);
            type2 = parameterizedType.getRawType();
            type = type instanceof CPPType || CPPObject.class.isAssignableFrom(Utils.getClass(type2)) ? new CPPType(type3, type2, objectArray) : new DefaultParameterizedType(type3, type2, (Type[])objectArray);
        } else if (type instanceof TypeVariable) {
            TypeVariable typeVariable = (TypeVariable)type;
            Class clazz = Utils.getClass(type2);
            TypeVariable<Class<T>>[] typeVariableArray = clazz.getTypeParameters();
            int n2 = Arrays.asList(typeVariableArray).indexOf(typeVariable);
            if (n2 < 0) throw new RuntimeException("Type param " + type + " not found in params of " + type2 + " (" + Arrays.asList(typeVariableArray) + ")");
            if (!(type2 instanceof ParameterizedType)) throw new RuntimeException("Type " + type2 + " does not have params, cannot resolve " + type);
            type2 = (ParameterizedType)type2;
            type = type2.getActualTypeArguments()[n2];
        }
        assert (!Utils.containsTypeVariables(type));
        return type;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    static StructFieldDescription aggregateDeclarations(Type type, List<StructFieldDeclaration> iterator2) {
        StructFieldDescription structFieldDescription = new StructFieldDescription();
        boolean bl = iterator2.size() > 1;
        structFieldDescription.aggregatedFields.addAll((Collection<StructFieldDeclaration>)((Object)iterator2));
        iterator2 = iterator2.iterator();
        while (iterator2.hasNext()) {
            Object object;
            StructFieldDeclaration structFieldDeclaration = (StructFieldDeclaration)iterator2.next();
            if (structFieldDeclaration.valueClass.isArray()) {
                throw new RuntimeException("Struct fields cannot be array types : please use a combination of Pointer and @Array (for instance, an int[10] is a @Array(10) Pointer<Integer>).");
            }
            if (structFieldDeclaration.valueClass.isPrimitive()) {
                if (structFieldDeclaration.desc.isCLong) {
                    structFieldDeclaration.desc.byteLength = CLong.SIZE;
                } else if (structFieldDeclaration.desc.isSizeT) {
                    structFieldDeclaration.desc.byteLength = SizeT.SIZE;
                } else {
                    structFieldDeclaration.desc.byteLength = StructUtils.primTypeLength(structFieldDeclaration.valueClass);
                    if (structFieldDeclaration.desc.alignment < 0L) {
                        structFieldDeclaration.desc.alignment = StructUtils.primTypeAlignment(structFieldDeclaration.valueClass, structFieldDeclaration.desc.byteLength);
                    }
                }
            } else if (structFieldDeclaration.valueClass == CLong.class) {
                structFieldDeclaration.desc.byteLength = CLong.SIZE;
            } else if (structFieldDeclaration.valueClass == SizeT.class) {
                structFieldDeclaration.desc.byteLength = SizeT.SIZE;
            } else if (StructObject.class.isAssignableFrom(structFieldDeclaration.valueClass)) {
                structFieldDeclaration.desc.nativeTypeOrPointerTargetType = StructFieldDescription.resolveType(structFieldDeclaration.desc.valueType, type);
                object = StructIO.getInstance(structFieldDeclaration.valueClass, (Type)structFieldDeclaration.desc.valueType).desc;
                structFieldDeclaration.desc.byteLength = ((StructDescription)object).getStructSize();
                if (structFieldDeclaration.desc.alignment < 0L) {
                    structFieldDeclaration.desc.alignment = ((StructDescription)object).getStructAlignment();
                }
                structFieldDeclaration.desc.isNativeObject = true;
            } else if (ValuedEnum.class.isAssignableFrom(structFieldDeclaration.valueClass)) {
                structFieldDeclaration.desc.nativeTypeOrPointerTargetType = StructFieldDescription.resolveType(structFieldDeclaration.desc.valueType instanceof ParameterizedType ? PointerIO.getClass(((ParameterizedType)structFieldDeclaration.desc.valueType).getActualTypeArguments()[0]) : null, type);
                object = PointerIO.getClass(structFieldDeclaration.desc.nativeTypeOrPointerTargetType);
                if (!IntValuedEnum.class.isAssignableFrom((Class<?>)object)) throw new RuntimeException("Enum type unknown : " + object);
                structFieldDeclaration.desc.byteLength = 4L;
            } else if (TypedPointer.class.isAssignableFrom(structFieldDeclaration.valueClass)) {
                structFieldDeclaration.desc.nativeTypeOrPointerTargetType = StructFieldDescription.resolveType(structFieldDeclaration.desc.valueType, type);
                if (structFieldDeclaration.desc.isArray) {
                    throw new RuntimeException("Typed pointer field cannot be an array : " + structFieldDeclaration.desc.name);
                }
                structFieldDeclaration.desc.byteLength = Pointer.SIZE;
            } else if (Pointer.class.isAssignableFrom(structFieldDeclaration.valueClass)) {
                object = structFieldDeclaration.desc.valueType instanceof ParameterizedType ? ((ParameterizedType)structFieldDeclaration.desc.valueType).getActualTypeArguments()[0] : null;
                structFieldDeclaration.desc.nativeTypeOrPointerTargetType = StructFieldDescription.resolveType((Type)object, type);
                if (structFieldDeclaration.desc.isArray) {
                    structFieldDeclaration.desc.byteLength = BridJ.sizeOf(structFieldDeclaration.desc.nativeTypeOrPointerTargetType);
                    if (structFieldDeclaration.desc.alignment < 0L) {
                        structFieldDeclaration.desc.alignment = StructUtils.alignmentOf(structFieldDeclaration.desc.nativeTypeOrPointerTargetType);
                    }
                } else {
                    structFieldDeclaration.desc.byteLength = Pointer.SIZE;
                }
            } else if (Buffer.class.isAssignableFrom(structFieldDeclaration.valueClass)) {
                if (structFieldDeclaration.valueClass == IntBuffer.class) {
                    structFieldDeclaration.desc.byteLength = 4L;
                } else if (structFieldDeclaration.valueClass == LongBuffer.class) {
                    structFieldDeclaration.desc.byteLength = 8L;
                } else if (structFieldDeclaration.valueClass == ShortBuffer.class) {
                    structFieldDeclaration.desc.byteLength = 2L;
                } else if (structFieldDeclaration.valueClass == ByteBuffer.class) {
                    structFieldDeclaration.desc.byteLength = 1L;
                } else if (structFieldDeclaration.valueClass == FloatBuffer.class) {
                    structFieldDeclaration.desc.byteLength = 4L;
                } else {
                    if (structFieldDeclaration.valueClass != DoubleBuffer.class) throw new UnsupportedOperationException("Field array type " + structFieldDeclaration.valueClass.getName() + " not supported yet");
                    structFieldDeclaration.desc.byteLength = 8L;
                }
            } else if (structFieldDeclaration.valueClass.isArray() && structFieldDeclaration.valueClass.getComponentType().isPrimitive()) {
                structFieldDeclaration.desc.byteLength = StructUtils.primTypeLength(structFieldDeclaration.valueClass.getComponentType());
                if (structFieldDeclaration.desc.alignment < 0L) {
                    structFieldDeclaration.desc.alignment = StructUtils.primTypeAlignment(structFieldDeclaration.valueClass, structFieldDeclaration.desc.byteLength);
                }
            } else {
                object = StructIO.getInstance(structFieldDeclaration.valueClass, (Type)structFieldDeclaration.desc.valueType).desc;
                long l2 = ((StructDescription)object).getStructSize();
                if (l2 <= 0L) throw new UnsupportedOperationException("Field type " + structFieldDeclaration.valueClass.getName() + " not supported yet");
                structFieldDeclaration.desc.byteLength = l2;
            }
            structFieldDescription.alignment = Math.max(structFieldDescription.alignment, structFieldDeclaration.desc.alignment >= 0L ? structFieldDeclaration.desc.alignment : structFieldDeclaration.desc.byteLength);
            long l3 = structFieldDeclaration.desc.arrayLength * structFieldDeclaration.desc.byteLength;
            if (l3 >= structFieldDescription.byteLength) {
                structFieldDescription.byteLength = l3;
            }
            if (structFieldDeclaration.desc.bitLength < 0L) continue;
            if (bl) {
                throw new RuntimeException("No support for bit fields unions yet !");
            }
            structFieldDescription.bitLength = structFieldDeclaration.desc.bitLength;
            structFieldDescription.byteLength = (structFieldDescription.bitLength >>> 3) + (long)((structFieldDescription.bitLength & 7L) != 0L ? 1 : 0);
        }
        return structFieldDescription;
    }

    void computeBitMask() {
        if (this.bitLength < 0L) {
            this.bitMask = -1L;
            return;
        }
        this.bitMask = 0L;
        int n2 = 0;
        while ((long)n2 < this.bitLength) {
            this.bitMask = this.bitMask << 1 | 1L;
            ++n2;
        }
        this.bitMask <<= (int)this.bitOffset;
    }
}


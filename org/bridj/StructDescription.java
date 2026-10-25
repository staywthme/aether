/*
 * Decompiled with CFR 0.152.
 */
package org.bridj;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import org.bridj.BridJ;
import org.bridj.SolidRanges;
import org.bridj.StructCustomizer;
import org.bridj.StructFieldDescription;
import org.bridj.StructObject;
import org.bridj.StructUtils;
import org.bridj.ann.Virtual;
import org.bridj.util.Utils;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class StructDescription {
    protected volatile StructFieldDescription[] fields;
    protected long structSize = -1L;
    protected long structAlignment = -1L;
    protected final Class<?> structClass;
    protected final Type structType;
    protected boolean hasFieldFields;
    StructCustomizer customizer;
    private List<StructFieldDescription> aggregatedFields;
    SolidRanges solidRanges;

    public void prependBytes(long l2) {
        this.build();
        StructFieldDescription[] structFieldDescriptionArray = this.fields;
        int n2 = this.fields.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            StructFieldDescription structFieldDescription = structFieldDescriptionArray[i2];
            structFieldDescription.offset(l2);
        }
        this.structSize += l2;
    }

    public void appendBytes(long l2) {
        this.build();
        this.structSize += l2;
    }

    public void setFieldOffset(String string, long l2, boolean bl) {
        StructFieldDescription structFieldDescription;
        int n2;
        this.build();
        long l3 = 0L;
        long l4 = 0L;
        StructFieldDescription[] structFieldDescriptionArray = this.fields;
        int n3 = this.fields.length;
        for (n2 = 0; n2 < n3; ++n2) {
            structFieldDescription = structFieldDescriptionArray[n2];
            if (!structFieldDescription.name.equals(string)) continue;
            l4 = structFieldDescription.byteOffset;
            l3 = l2 - structFieldDescription.byteOffset;
            structFieldDescription.offset(l3);
            if (bl) continue;
            long l5 = l2 + structFieldDescription.byteLength;
            this.structSize = this.structSize < l5 ? l5 : this.structSize;
            return;
        }
        this.structSize += l3;
        structFieldDescriptionArray = this.fields;
        n3 = this.fields.length;
        for (n2 = 0; n2 < n3; ++n2) {
            structFieldDescription = structFieldDescriptionArray[n2];
            if (structFieldDescription.name.equals(string) || structFieldDescription.byteOffset <= l4) continue;
            structFieldDescription.offset(l3);
        }
    }

    public StructDescription(Class<?> clazz, Type type, StructCustomizer structCustomizer) {
        this.structClass = clazz;
        this.structType = type;
        this.customizer = structCustomizer;
        if (Utils.containsTypeVariables(type)) {
            throw new RuntimeException("Type " + type + " contains unresolved type variables!");
        }
    }

    boolean isVirtual() {
        for (Method method : this.structClass.getMethods()) {
            if (method.getAnnotation(Virtual.class) == null) continue;
            return true;
        }
        return false;
    }

    public Class<?> getStructClass() {
        return this.structClass;
    }

    public Type getStructType() {
        return this.structType;
    }

    public String toString() {
        return "StructDescription(" + Utils.toString(this.structType) + ")";
    }

    void build() {
        if (this.fields == null) {
            StructDescription structDescription = this;
            synchronized (structDescription) {
                if (this.fields == null) {
                    StructDescription structDescription2 = this;
                    StructUtils.computeStructLayout(structDescription2, structDescription2.customizer);
                    this.customizer.afterBuild(this);
                    if (BridJ.debug) {
                        BridJ.info(this.describe());
                    }
                }
                return;
            }
        }
    }

    public final long getStructSize() {
        this.build();
        return this.structSize;
    }

    public final long getStructAlignment() {
        this.build();
        return this.structAlignment;
    }

    public void setAggregatedFields(List<StructFieldDescription> list2) {
        this.aggregatedFields = list2;
    }

    public List<StructFieldDescription> getAggregatedFields() {
        this.build();
        return this.aggregatedFields;
    }

    public SolidRanges getSolidRanges() {
        this.build();
        return this.solidRanges;
    }

    public final String describe(StructObject structObject) {
        return StructUtils.describe(structObject, this.structType, this.fields);
    }

    public final String describe() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("// ");
        stringBuilder.append("size = ").append(this.structSize).append(", ");
        stringBuilder.append("alignment = ").append(this.structAlignment);
        stringBuilder.append("\nstruct ");
        stringBuilder.append(StructUtils.describe(this.structType)).append(" { ");
        int n2 = this.fields.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            StructFieldDescription structFieldDescription = this.fields[i2];
            stringBuilder.append("\n\t");
            stringBuilder.append("@Field(").append(i2).append(") ");
            if (structFieldDescription.isCLong) {
                stringBuilder.append("@CLong ");
            } else if (structFieldDescription.isSizeT) {
                stringBuilder.append("@Ptr ");
            }
            stringBuilder.append(StructUtils.describe(structFieldDescription.valueType)).append(" ").append(structFieldDescription.name).append("; ");
            stringBuilder.append("// ");
            stringBuilder.append("offset = ").append(structFieldDescription.byteOffset).append(", ");
            stringBuilder.append("length = ").append(structFieldDescription.byteLength).append(", ");
            if (structFieldDescription.bitOffset != 0L) {
                stringBuilder.append("bitOffset = ").append(structFieldDescription.bitOffset).append(", ");
            }
            if (structFieldDescription.bitLength != -1L) {
                stringBuilder.append("bitLength = ").append(structFieldDescription.bitLength).append(", ");
            }
            if (structFieldDescription.arrayLength != 1L) {
                stringBuilder.append("arrayLength = ").append(structFieldDescription.arrayLength).append(", ");
            }
            if (structFieldDescription.alignment == 1L) continue;
            stringBuilder.append("alignment = ").append(structFieldDescription.alignment);
        }
        stringBuilder.append("\n}");
        return stringBuilder.toString();
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.TypeMapper;
import com.sun.jna.WString;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class Union
extends Structure {
    private Structure.StructField activeField;

    public Union() {
    }

    public Union(Pointer pointer) {
        super(pointer);
    }

    protected Union(Pointer pointer, int n2) {
        super(pointer, n2);
    }

    protected Union(TypeMapper typeMapper) {
        super(typeMapper);
    }

    protected Union(Pointer pointer, int n2, TypeMapper typeMapper) {
        super(pointer, n2, typeMapper);
    }

    @Override
    protected List<String> getFieldOrder() {
        Object object = this.getFieldList();
        ArrayList<String> arrayList = new ArrayList<String>(object.size());
        object = object.iterator();
        while (object.hasNext()) {
            Field field = (Field)object.next();
            arrayList.add(field.getName());
        }
        return arrayList;
    }

    public void setType(Class<?> clazz) {
        this.ensureAllocated();
        for (Structure.StructField structField : this.fields().values()) {
            if (structField.type != clazz) continue;
            this.activeField = structField;
            return;
        }
        throw new IllegalArgumentException("No field of type " + clazz + " in " + this);
    }

    public void setType(String string) {
        this.ensureAllocated();
        Structure.StructField structField = this.fields().get(string);
        if (structField != null) {
            this.activeField = structField;
            return;
        }
        throw new IllegalArgumentException("No field named " + string + " in " + this);
    }

    @Override
    public Object readField(String string) {
        this.ensureAllocated();
        this.setType(string);
        return super.readField(string);
    }

    @Override
    public void writeField(String string) {
        this.ensureAllocated();
        this.setType(string);
        super.writeField(string);
    }

    @Override
    public void writeField(String string, Object object) {
        this.ensureAllocated();
        this.setType(string);
        super.writeField(string, object);
    }

    public Object getTypedValue(Class<?> clazz) {
        this.ensureAllocated();
        for (Structure.StructField structField : this.fields().values()) {
            if (structField.type != clazz) continue;
            this.activeField = structField;
            this.read();
            Union union = this;
            return union.getFieldValue(union.activeField.field);
        }
        throw new IllegalArgumentException("No field of type " + clazz + " in " + this);
    }

    public Object setTypedValue(Object object) {
        Structure.StructField structField = this.findField(object.getClass());
        if (structField != null) {
            this.activeField = structField;
            this.setFieldValue(structField.field, object);
            return this;
        }
        throw new IllegalArgumentException("No field of type " + object.getClass() + " in " + this);
    }

    private Structure.StructField findField(Class<?> clazz) {
        this.ensureAllocated();
        for (Structure.StructField structField : this.fields().values()) {
            if (!structField.type.isAssignableFrom(clazz)) continue;
            return structField;
        }
        return null;
    }

    @Override
    protected void writeField(Structure.StructField structField) {
        if (structField == this.activeField) {
            super.writeField(structField);
        }
    }

    @Override
    protected Object readField(Structure.StructField structField) {
        if (structField == this.activeField || !Structure.class.isAssignableFrom(structField.type) && !String.class.isAssignableFrom(structField.type) && !WString.class.isAssignableFrom(structField.type)) {
            return super.readField(structField);
        }
        return null;
    }

    @Override
    protected int getNativeAlignment(Class<?> clazz, Object object, boolean bl) {
        return super.getNativeAlignment(clazz, object, true);
    }
}


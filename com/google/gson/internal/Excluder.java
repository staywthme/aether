/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson.internal;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.Since;
import com.google.gson.annotations.Until;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class Excluder
implements TypeAdapterFactory,
Cloneable {
    public static final double IGNORE_VERSIONS = -1.0;
    public static final Excluder DEFAULT = new Excluder();
    public double version = -1.0;
    public int modifiers = 136;
    public boolean serializeInnerClasses = true;
    public boolean requireExpose;
    public List<ExclusionStrategy> serializationStrategies = Collections.emptyList();
    public List<ExclusionStrategy> deserializationStrategies = Collections.emptyList();

    protected final Excluder clone() {
        try {
            return (Excluder)super.clone();
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            throw new AssertionError((Object)cloneNotSupportedException);
        }
    }

    public final Excluder withVersion(double d2) {
        Excluder excluder = this.clone();
        this.clone().version = d2;
        return excluder;
    }

    public final Excluder withModifiers(int ... nArray) {
        Excluder excluder = this.clone();
        this.clone().modifiers = 0;
        for (int n2 : nArray) {
            excluder.modifiers |= n2;
        }
        return excluder;
    }

    public final Excluder disableInnerClassSerialization() {
        Excluder excluder = this.clone();
        this.clone().serializeInnerClasses = false;
        return excluder;
    }

    public final Excluder excludeFieldsWithoutExposeAnnotation() {
        Excluder excluder = this.clone();
        this.clone().requireExpose = true;
        return excluder;
    }

    public final Excluder withExclusionStrategy(ExclusionStrategy exclusionStrategy, boolean bl, boolean bl2) {
        Excluder excluder = this.clone();
        if (bl) {
            excluder.serializationStrategies = new ArrayList<ExclusionStrategy>(this.serializationStrategies);
            excluder.serializationStrategies.add(exclusionStrategy);
        }
        if (bl2) {
            excluder.deserializationStrategies = new ArrayList<ExclusionStrategy>(this.deserializationStrategies);
            excluder.deserializationStrategies.add(exclusionStrategy);
        }
        return excluder;
    }

    @Override
    public final <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
        boolean bl;
        Class<T> clazz = typeToken.getRawType();
        boolean bl2 = this.excludeClassChecks(clazz);
        final boolean bl3 = bl2 || this.excludeClassInStrategy(clazz, true);
        boolean bl4 = bl = bl2 || this.excludeClassInStrategy(clazz, false);
        if (!bl3 && !bl) {
            return null;
        }
        return new TypeAdapter<T>(){
            private TypeAdapter<T> delegate;

            @Override
            public T read(JsonReader jsonReader) throws IOException {
                if (bl) {
                    jsonReader.skipValue();
                    return null;
                }
                return this.delegate().read(jsonReader);
            }

            @Override
            public void write(JsonWriter jsonWriter, T t2) throws IOException {
                if (bl3) {
                    jsonWriter.nullValue();
                    return;
                }
                this.delegate().write(jsonWriter, t2);
            }

            private TypeAdapter<T> delegate() {
                TypeAdapter typeAdapter = this.delegate;
                if (typeAdapter != null) {
                    return typeAdapter;
                }
                this.delegate = gson.getDelegateAdapter(Excluder.this, typeToken);
                return this.delegate;
            }
        };
    }

    public final boolean excludeField(Field object, boolean bl) {
        Object object2;
        if ((this.modifiers & ((Field)object).getModifiers()) != 0) {
            return true;
        }
        if (this.version != -1.0 && !this.isValidVersion(((Field)object).getAnnotation(Since.class), ((Field)object).getAnnotation(Until.class))) {
            return true;
        }
        if (((Field)object).isSynthetic()) {
            return true;
        }
        if (this.requireExpose && ((object2 = ((Field)object).getAnnotation(Expose.class)) == null || (bl ? !object2.serialize() : !object2.deserialize()))) {
            return true;
        }
        if (!this.serializeInnerClasses && this.isInnerClass(((Field)object).getType())) {
            return true;
        }
        if (this.isAnonymousOrNonStaticLocal(((Field)object).getType())) {
            return true;
        }
        object2 = bl ? this.serializationStrategies : this.deserializationStrategies;
        if (!object2.isEmpty()) {
            object = new FieldAttributes((Field)object);
            Iterator iterator2 = object2.iterator();
            while (iterator2.hasNext()) {
                object2 = (ExclusionStrategy)iterator2.next();
                if (!object2.shouldSkipField((FieldAttributes)object)) continue;
                return true;
            }
        }
        return false;
    }

    private boolean excludeClassChecks(Class<?> clazz) {
        if (this.version != -1.0 && !this.isValidVersion(clazz.getAnnotation(Since.class), clazz.getAnnotation(Until.class))) {
            return true;
        }
        if (!this.serializeInnerClasses && this.isInnerClass(clazz)) {
            return true;
        }
        return this.isAnonymousOrNonStaticLocal(clazz);
    }

    public final boolean excludeClass(Class<?> clazz, boolean bl) {
        return this.excludeClassChecks(clazz) || this.excludeClassInStrategy(clazz, bl);
    }

    private boolean excludeClassInStrategy(Class<?> clazz, boolean bl) {
        Object object = bl ? this.serializationStrategies : this.deserializationStrategies;
        object = object.iterator();
        while (object.hasNext()) {
            ExclusionStrategy exclusionStrategy = (ExclusionStrategy)object.next();
            if (!exclusionStrategy.shouldSkipClass(clazz)) continue;
            return true;
        }
        return false;
    }

    private boolean isAnonymousOrNonStaticLocal(Class<?> clazz) {
        return !Enum.class.isAssignableFrom(clazz) && !this.isStatic(clazz) && (clazz.isAnonymousClass() || clazz.isLocalClass());
    }

    private boolean isInnerClass(Class<?> clazz) {
        return clazz.isMemberClass() && !this.isStatic(clazz);
    }

    private boolean isStatic(Class<?> clazz) {
        return (clazz.getModifiers() & 8) != 0;
    }

    private boolean isValidVersion(Since since, Until until) {
        return this.isValidSince(since) && this.isValidUntil(until);
    }

    private boolean isValidSince(Since since) {
        if (since != null) {
            double d2 = since.value();
            return this.version >= d2;
        }
        return true;
    }

    private boolean isValidUntil(Until until) {
        if (until != null) {
            double d2 = until.value();
            return this.version < d2;
        }
        return true;
    }
}


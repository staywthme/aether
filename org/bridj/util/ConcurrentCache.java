/*
 * Decompiled with CFR 0.152.
 */
package org.bridj.util;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class ConcurrentCache<K, V> {
    protected final ConcurrentHashMap<K, V> map = new ConcurrentHashMap();
    protected final Class<V> valueClass;
    private volatile Constructor<V> valueConstructor;

    public ConcurrentCache(Class<V> clazz) {
        this.valueClass = clazz;
    }

    private Constructor<V> getValueConstructor() {
        if (this.valueConstructor == null) {
            try {
                this.valueConstructor = this.valueClass.getConstructor(new Class[0]);
                if (this.valueConstructor != null && this.valueConstructor.isAccessible()) {
                    this.valueConstructor.setAccessible(true);
                }
            }
            catch (Exception exception) {
                throw new RuntimeException("No accessible default constructor in class " + (this.valueClass == null ? "null" : this.valueClass.getName()), exception);
            }
        }
        return this.valueConstructor;
    }

    protected V newInstance(K k2) {
        try {
            return this.getValueConstructor().newInstance(new Object[0]);
        }
        catch (Exception exception) {
            throw new RuntimeException("Failed to call constructor " + this.valueConstructor, exception);
        }
    }

    public V get(K object) {
        Object object2;
        block0: {
            object2 = this.map.get(object);
            if (object2 != null || (object = this.map.putIfAbsent(object, object2 = this.newInstance(object))) == null) break block0;
            object2 = object;
        }
        return object2;
    }

    public void clear() {
        this.map.clear();
    }

    public Iterable<Map.Entry<K, V>> entrySet() {
        return this.map.entrySet();
    }
}


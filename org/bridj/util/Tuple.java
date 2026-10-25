/*
 * Decompiled with CFR 0.152.
 */
package org.bridj.util;

public class Tuple {
    protected final Object[] data;

    public Tuple(Object[] objectArray) {
        this.data = objectArray;
    }

    public Tuple(int n2) {
        this.data = new Object[n2];
    }

    public int hashCode() {
        int n2 = 0;
        int n3 = this.size();
        for (int i2 = 0; i2 < n3; ++i2) {
            Object object = this.get(i2);
            if (object == null) continue;
            n2 ^= object.hashCode();
        }
        return n2;
    }

    public boolean equals(Object object) {
        if (object == null || !(object instanceof Tuple)) {
            return false;
        }
        object = (Tuple)object;
        int n2 = this.size();
        if (((Tuple)object).size() != n2) {
            return false;
        }
        for (int i2 = 0; i2 < n2; ++i2) {
            Object object2 = this.get(i2);
            Object object3 = ((Tuple)object).get(i2);
            if (!(object2 == null ? object3 != null : !object2.equals(object3))) continue;
            return false;
        }
        return true;
    }

    public Object get(int n2) {
        return this.data[n2];
    }

    public void set(int n2, Object object) {
        this.data[n2] = object;
    }

    public int size() {
        return this.data.length;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("{");
        int n2 = this.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            if (i2 != 0) {
                stringBuilder.append(',');
            }
            stringBuilder.append('\n');
            stringBuilder.append('\t').append(this.get(i2));
        }
        stringBuilder.append("\n}");
        return stringBuilder.toString();
    }
}


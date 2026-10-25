/*
 * Decompiled with CFR 0.152.
 */
package org.bridj.util;

import java.util.Map;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class Pair<U, V>
implements Comparable<Pair<U, V>>,
Map.Entry<U, V> {
    private U first;
    private V second;

    public Pair(U u2, V v2) {
        this.first = u2;
        this.second = v2;
    }

    public Pair() {
    }

    public U getFirst() {
        return this.first;
    }

    public V getSecond() {
        return this.second;
    }

    public void setFirst(U u2) {
        this.first = u2;
    }

    public void setSecond(V v2) {
        this.second = v2;
    }

    @Override
    public int compareTo(Pair<U, V> pair) {
        Comparable comparable = (Comparable)this.getFirst();
        if (comparable == null) {
            if (this.first != null) {
                return 1;
            }
        } else {
            int n2 = comparable.compareTo(pair.getFirst());
            if (n2 != 0) {
                return n2;
            }
        }
        if ((comparable = (Comparable)this.getSecond()) == null) {
            if (this.second != null) {
                return 1;
            }
            return -1;
        }
        return comparable.compareTo(pair.getSecond());
    }

    public String toString() {
        return "Pair(" + this.first + ", " + this.second + ")";
    }

    @Override
    public U getKey() {
        return this.first;
    }

    @Override
    public V getValue() {
        return this.second;
    }

    @Override
    public V setValue(V v2) {
        V v3 = this.second;
        this.second = v2;
        return v3;
    }

    @Override
    public int hashCode() {
        int n2 = 31 + (this.first == null ? 0 : this.first.hashCode());
        n2 = n2 * 31 + (this.second == null ? 0 : this.second.hashCode());
        return n2;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (this.getClass() != object.getClass()) {
            return false;
        }
        object = (Pair)object;
        if (this.first == null ? ((Pair)object).first != null : !this.first.equals(((Pair)object).first)) {
            return false;
        }
        return !(this.second == null ? ((Pair)object).second != null : !this.second.equals(((Pair)object).second));
    }

    public boolean isFull() {
        return this.getFirst() != null && this.getSecond() != null;
    }

    public boolean isEmpty() {
        return this.getFirst() == null && this.getSecond() == null;
    }
}


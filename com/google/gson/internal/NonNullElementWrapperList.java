/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson.internal;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.RandomAccess;

public class NonNullElementWrapperList<E>
extends AbstractList<E>
implements RandomAccess {
    private final ArrayList<E> delegate;

    public NonNullElementWrapperList(ArrayList<E> arrayList) {
        this.delegate = Objects.requireNonNull(arrayList);
    }

    @Override
    public E get(int n2) {
        return this.delegate.get(n2);
    }

    @Override
    public int size() {
        return this.delegate.size();
    }

    private E nonNull(E e2) {
        if (e2 == null) {
            throw new NullPointerException("Element must be non-null");
        }
        return e2;
    }

    @Override
    public E set(int n2, E e2) {
        return this.delegate.set(n2, this.nonNull(e2));
    }

    @Override
    public void add(int n2, E e2) {
        this.delegate.add(n2, this.nonNull(e2));
    }

    @Override
    public E remove(int n2) {
        return this.delegate.remove(n2);
    }

    @Override
    public void clear() {
        this.delegate.clear();
    }

    @Override
    public boolean remove(Object object) {
        return this.delegate.remove(object);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return this.delegate.removeAll(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return this.delegate.retainAll(collection);
    }

    @Override
    public boolean contains(Object object) {
        return this.delegate.contains(object);
    }

    @Override
    public int indexOf(Object object) {
        return this.delegate.indexOf(object);
    }

    @Override
    public int lastIndexOf(Object object) {
        return this.delegate.lastIndexOf(object);
    }

    @Override
    public Object[] toArray() {
        return this.delegate.toArray();
    }

    @Override
    public <T> T[] toArray(T[] TArray) {
        return this.delegate.toArray(TArray);
    }

    @Override
    public boolean equals(Object object) {
        return this.delegate.equals(object);
    }

    @Override
    public int hashCode() {
        return this.delegate.hashCode();
    }
}


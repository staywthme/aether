/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna;

public final class WString
implements CharSequence,
Comparable {
    private String string;

    public WString(String string) {
        if (string == null) {
            throw new NullPointerException("String initializer must be non-null");
        }
        this.string = string;
    }

    @Override
    public final String toString() {
        return this.string;
    }

    public final boolean equals(Object object) {
        return object instanceof WString && this.toString().equals(object.toString());
    }

    public final int hashCode() {
        return this.toString().hashCode();
    }

    public final int compareTo(Object object) {
        return this.toString().compareTo(object.toString());
    }

    @Override
    public final int length() {
        return this.toString().length();
    }

    @Override
    public final char charAt(int n2) {
        return this.toString().charAt(n2);
    }

    @Override
    public final CharSequence subSequence(int n2, int n3) {
        return this.toString().subSequence(n2, n3);
    }
}


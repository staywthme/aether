/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

final class Preconditions {
    private Preconditions() {
    }

    static <T> T checkNotNull(T t2, String string) {
        if (t2 == null) {
            throw new NullPointerException(string);
        }
        return t2;
    }

    static void checkArgument(boolean bl, String string) {
        if (!bl) {
            throw new IllegalArgumentException(string);
        }
    }

    static void checkArgument(boolean bl, String string, Object object) {
        if (!bl) {
            throw new IllegalArgumentException(String.format(string, object));
        }
    }

    static void checkPositionIndexes(int n2, int n3, int n4) {
        if (n2 < 0 || n3 < n2 || n3 > n4) {
            throw new IndexOutOfBoundsException(Preconditions.badPositionIndexes(n2, n3, n4));
        }
    }

    private static String badPositionIndexes(int n2, int n3, int n4) {
        if (n2 < 0 || n2 > n4) {
            return Preconditions.badPositionIndex(n2, n4, "start index");
        }
        if (n3 < 0 || n3 > n4) {
            return Preconditions.badPositionIndex(n3, n4, "end index");
        }
        return String.format("end index (%s) must not be less than start index (%s)", n3, n2);
    }

    private static String badPositionIndex(int n2, int n3, String string) {
        if (n2 < 0) {
            return String.format("%s (%s) must not be negative", string, n2);
        }
        if (n3 < 0) {
            throw new IllegalArgumentException("negative size: " + n3);
        }
        return String.format("%s (%s) must not be greater than size (%s)", string, n2, n3);
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package org.bridj;

import org.bridj.NativeError;

public class LastError
extends NativeError {
    final int code;
    final int kind;
    String description;
    static final int eLastErrorKindWindows = 1;
    static final int eLastErrorKindCLibrary = 2;
    private static final ThreadLocal<LastError> lastError = new ThreadLocal();

    LastError(int n2, int n3) {
        super((String)null);
        this.code = n2;
        this.kind = n3;
    }

    public int hashCode() {
        return Integer.valueOf(this.code).hashCode() ^ Integer.valueOf(this.kind).hashCode();
    }

    public boolean equals(Object object) {
        if (!(object instanceof LastError)) {
            return false;
        }
        object = (LastError)object;
        return this.code == ((LastError)object).code && this.kind == ((LastError)object).kind;
    }

    public int getCode() {
        return this.code;
    }

    public String getDescription() {
        if (this.description == null) {
            this.description = LastError.getDescription(this.code, this.kind);
        }
        return this.description;
    }

    public String getMessage() {
        String string = this.getDescription();
        return (string == null ? "?" : string.trim()) + " (error code = " + this.code + ")";
    }

    private static native String getDescription(int var0, int var1);

    public static LastError getLastError() {
        return lastError.get();
    }

    static LastError setLastError(int n2, int n3) {
        if (n2 == 0) {
            lastError.set(null);
            return null;
        }
        LastError lastError = new LastError(n2, n3);
        lastError.fillInStackTrace();
        LastError.lastError.set(lastError);
        return lastError;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.WString;

class NativeString
implements CharSequence,
Comparable {
    static final String WIDE_STRING = "--WIDE-STRING--";
    private Pointer pointer;
    private String encoding;

    public NativeString(String string) {
        this(string, Native.getDefaultStringEncoding());
    }

    public NativeString(String string, boolean bl) {
        this(string, bl ? WIDE_STRING : Native.getDefaultStringEncoding());
    }

    public NativeString(WString wString) {
        this(wString.toString(), WIDE_STRING);
    }

    public NativeString(String string, String object) {
        if (string == null) {
            throw new NullPointerException("String must not be null");
        }
        this.encoding = object;
        if (WIDE_STRING.equals(this.encoding)) {
            int n2 = (string.length() + 1) * Native.WCHAR_SIZE;
            this.pointer = new StringMemory(n2);
            this.pointer.setWideString(0L, string);
            return;
        }
        object = Native.getBytes(string, (String)object);
        this.pointer = new StringMemory(((Object)object).length + 1);
        this.pointer.write(0L, (byte[])object, 0, ((Object)object).length);
        this.pointer.setByte(((Object)object).length, (byte)0);
    }

    public int hashCode() {
        return this.toString().hashCode();
    }

    public boolean equals(Object object) {
        if (object instanceof CharSequence) {
            return this.compareTo(object) == 0;
        }
        return false;
    }

    @Override
    public String toString() {
        boolean bl = WIDE_STRING.equals(this.encoding);
        if (bl) {
            return this.pointer.getWideString(0L);
        }
        return this.pointer.getString(0L, this.encoding);
    }

    public Pointer getPointer() {
        return this.pointer;
    }

    @Override
    public char charAt(int n2) {
        return this.toString().charAt(n2);
    }

    @Override
    public int length() {
        return this.toString().length();
    }

    @Override
    public CharSequence subSequence(int n2, int n3) {
        return this.toString().subSequence(n2, n3);
    }

    public int compareTo(Object object) {
        if (object == null) {
            return 1;
        }
        return this.toString().compareTo(object.toString());
    }

    class StringMemory
    extends Memory {
        public StringMemory(long l2) {
            super(l2);
        }

        @Override
        public String toString() {
            return NativeString.this.toString();
        }
    }
}


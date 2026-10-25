/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.ptr.ByReference;
import java.io.UnsupportedEncodingException;

public interface WTypes {
    public static final int CLSCTX_INPROC_SERVER = 1;
    public static final int CLSCTX_INPROC_HANDLER = 2;
    public static final int CLSCTX_LOCAL_SERVER = 4;
    public static final int CLSCTX_INPROC_SERVER16 = 8;
    public static final int CLSCTX_REMOTE_SERVER = 16;
    public static final int CLSCTX_INPROC_HANDLER16 = 32;
    public static final int CLSCTX_RESERVED1 = 64;
    public static final int CLSCTX_RESERVED2 = 128;
    public static final int CLSCTX_RESERVED3 = 256;
    public static final int CLSCTX_RESERVED4 = 512;
    public static final int CLSCTX_NO_CODE_DOWNLOAD = 1024;
    public static final int CLSCTX_RESERVED5 = 2048;
    public static final int CLSCTX_NO_CUSTOM_MARSHAL = 4096;
    public static final int CLSCTX_ENABLE_CODE_DOWNLOAD = 8192;
    public static final int CLSCTX_NO_FAILURE_LOG = 16384;
    public static final int CLSCTX_DISABLE_AAA = 32768;
    public static final int CLSCTX_ENABLE_AAA = 65536;
    public static final int CLSCTX_FROM_DEFAULT_CONTEXT = 131072;
    public static final int CLSCTX_ACTIVATE_32_BIT_SERVER = 262144;
    public static final int CLSCTX_ACTIVATE_64_BIT_SERVER = 524288;
    public static final int CLSCTX_ENABLE_CLOAKING = 0x100000;
    public static final int CLSCTX_APPCONTAINER = 0x400000;
    public static final int CLSCTX_ACTIVATE_AAA_AS_IU = 0x800000;
    public static final int CLSCTX_PS_DLL = Integer.MIN_VALUE;
    public static final int CLSCTX_SERVER = 21;
    public static final int CLSCTX_ALL = 7;

    public static class VARTYPEByReference
    extends ByReference {
        public VARTYPEByReference() {
            super(2);
        }

        public VARTYPEByReference(VARTYPE vARTYPE) {
            super(2);
            this.setValue(vARTYPE);
        }

        public VARTYPEByReference(short s2) {
            super(2);
            this.getPointer().setShort(0L, s2);
        }

        public void setValue(VARTYPE vARTYPE) {
            this.getPointer().setShort(0L, vARTYPE.shortValue());
        }

        public VARTYPE getValue() {
            return new VARTYPE(this.getPointer().getShort(0L));
        }
    }

    public static class VARTYPE
    extends WinDef.USHORT {
        private static final long serialVersionUID = 1L;

        public VARTYPE() {
            this(0);
        }

        public VARTYPE(int n2) {
            super((long)n2);
        }
    }

    public static class LPOLESTR
    extends PointerType {
        public LPOLESTR() {
            super(Pointer.NULL);
        }

        public LPOLESTR(Pointer pointer) {
            super(pointer);
        }

        public LPOLESTR(String string) {
            super(new Memory(((long)string.length() + 1L) * (long)Native.WCHAR_SIZE));
            this.setValue(string);
        }

        public void setValue(String string) {
            this.getPointer().setWideString(0L, string);
        }

        public String getValue() {
            Pointer pointer = this.getPointer();
            String string = null;
            if (pointer != null) {
                string = pointer.getWideString(0L);
            }
            return string;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static class ByReference
        extends LPOLESTR
        implements Structure.ByReference {
        }
    }

    public static class LPWSTR
    extends PointerType {
        public LPWSTR() {
            super(Pointer.NULL);
        }

        public LPWSTR(Pointer pointer) {
            super(pointer);
        }

        public LPWSTR(String string) {
            this(new Memory(((long)string.length() + 1L) * (long)Native.WCHAR_SIZE));
            this.setValue(string);
        }

        public void setValue(String string) {
            this.getPointer().setWideString(0L, string);
        }

        public String getValue() {
            Pointer pointer = this.getPointer();
            String string = null;
            if (pointer != null) {
                string = pointer.getWideString(0L);
            }
            return string;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static class ByReference
        extends LPWSTR
        implements Structure.ByReference {
        }
    }

    public static class LPSTR
    extends PointerType {
        public LPSTR() {
            super(Pointer.NULL);
        }

        public LPSTR(Pointer pointer) {
            super(pointer);
        }

        public LPSTR(String string) {
            this(new Memory((long)string.length() + 1L));
            this.setValue(string);
        }

        public void setValue(String string) {
            this.getPointer().setString(0L, string);
        }

        public String getValue() {
            Pointer pointer = this.getPointer();
            String string = null;
            if (pointer != null) {
                string = pointer.getString(0L);
            }
            return string;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static class ByReference
        extends LPSTR
        implements Structure.ByReference {
        }
    }

    public static class BSTRByReference
    extends ByReference {
        public BSTRByReference() {
            super(Native.POINTER_SIZE);
        }

        public BSTRByReference(BSTR bSTR) {
            this();
            this.setValue(bSTR);
        }

        public void setValue(BSTR bSTR) {
            this.getPointer().setPointer(0L, bSTR.getPointer());
        }

        public BSTR getValue() {
            return new BSTR(this.getPointer().getPointer(0L));
        }

        public String getString() {
            BSTR bSTR = this.getValue();
            if (bSTR == null) {
                return null;
            }
            return bSTR.getValue();
        }
    }

    public static class BSTR
    extends PointerType {
        public BSTR() {
            super(Pointer.NULL);
        }

        public BSTR(Pointer pointer) {
            super(pointer);
        }

        @Deprecated
        public BSTR(String string) {
            this.setValue(string);
        }

        @Deprecated
        public void setValue(String object) {
            if (object == null) {
                object = "";
            }
            try {
                object = ((String)object).getBytes("UTF-16LE");
                Memory memory = new Memory(4 + ((Object)object).length + 2);
                memory.clear();
                ((Pointer)memory).setInt(0L, ((Object)object).length);
                ((Pointer)memory).write(4L, (byte[])object, 0, ((Object)object).length);
                this.setPointer(((Pointer)memory).share(4L));
                return;
            }
            catch (UnsupportedEncodingException unsupportedEncodingException) {
                throw new RuntimeException("UTF-16LE charset is not supported", unsupportedEncodingException);
            }
        }

        public String getValue() {
            try {
                Pointer pointer = this.getPointer();
                if (pointer == null) {
                    return "";
                }
                int n2 = pointer.getInt(-4L);
                return new String(pointer.getByteArray(0L, n2), "UTF-16LE");
            }
            catch (UnsupportedEncodingException unsupportedEncodingException) {
                throw new RuntimeException("UTF-16LE charset is not supported", unsupportedEncodingException);
            }
        }

        @Override
        public String toString() {
            return this.getValue();
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32;

import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import com.sun.jna.Structure;
import java.security.SecureRandom;
import java.util.Arrays;

public interface Guid {
    public static final IID IID_NULL = new IID();

    public static class IID
    extends GUID {
        public IID() {
        }

        public IID(Pointer pointer) {
            super(pointer);
        }

        public IID(String string) {
            super(string);
        }

        public IID(byte[] byArray) {
            super(byArray);
        }

        public IID(GUID gUID) {
            this(gUID.toGuidString());
        }
    }

    public static class REFIID
    extends PointerType {
        public REFIID() {
        }

        public REFIID(Pointer pointer) {
            super(pointer);
        }

        public REFIID(IID iID) {
            super(iID.getPointer());
        }

        public void setValue(IID iID) {
            this.setPointer(iID.getPointer());
        }

        public IID getValue() {
            return new IID(this.getPointer());
        }

        @Override
        public boolean equals(Object object) {
            if (object == null) {
                return false;
            }
            if (this == object) {
                return true;
            }
            if (this.getClass() != object.getClass()) {
                return false;
            }
            object = (REFIID)object;
            return ((Structure)this.getValue()).equals(((REFIID)object).getValue());
        }

        @Override
        public int hashCode() {
            return ((Structure)this.getValue()).hashCode();
        }
    }

    public static class CLSID
    extends GUID {
        public CLSID() {
        }

        public CLSID(String string) {
            super(string);
        }

        public CLSID(GUID gUID) {
            super(gUID);
        }

        public static class ByReference
        extends GUID {
            public ByReference() {
            }

            public ByReference(GUID gUID) {
                super(gUID);
            }

            public ByReference(Pointer pointer) {
                super(pointer);
            }
        }
    }

    @Structure.FieldOrder(value={"Data1", "Data2", "Data3", "Data4"})
    public static class GUID
    extends Structure {
        public int Data1;
        public short Data2;
        public short Data3;
        public byte[] Data4 = new byte[8];

        public GUID() {
        }

        public GUID(GUID gUID) {
            this.Data1 = gUID.Data1;
            this.Data2 = gUID.Data2;
            this.Data3 = gUID.Data3;
            this.Data4 = gUID.Data4;
            this.writeFieldsToMemory();
        }

        public GUID(String string) {
            this(GUID.fromString(string));
        }

        public GUID(byte[] byArray) {
            this(GUID.fromBinary(byArray));
        }

        public GUID(Pointer pointer) {
            super(pointer);
            this.read();
        }

        @Override
        public boolean equals(Object object) {
            if (object == null) {
                return false;
            }
            if (this == object) {
                return true;
            }
            if (this.getClass() != object.getClass()) {
                return false;
            }
            object = (GUID)object;
            return this.Data1 == ((GUID)object).Data1 && this.Data2 == ((GUID)object).Data2 && this.Data3 == ((GUID)object).Data3 && Arrays.equals(this.Data4, ((GUID)object).Data4);
        }

        @Override
        public int hashCode() {
            return this.Data1 + this.Data2 & 65535 + this.Data3 & 65535 + Arrays.hashCode(this.Data4);
        }

        public static GUID fromBinary(byte[] byArray) {
            if (byArray.length != 16) {
                throw new IllegalArgumentException("Invalid data length: " + byArray.length);
            }
            GUID gUID = new GUID();
            long l2 = byArray[0] & 0xFF;
            l2 <<= 8;
            l2 |= (long)(byArray[1] & 0xFF);
            l2 <<= 8;
            l2 |= (long)(byArray[2] & 0xFF);
            l2 <<= 8;
            gUID.Data1 = (int)(l2 |= (long)(byArray[3] & 0xFF));
            int n2 = byArray[4] & 0xFF;
            n2 <<= 8;
            gUID.Data2 = (short)(n2 |= byArray[5] & 0xFF);
            n2 = byArray[6] & 0xFF;
            n2 <<= 8;
            gUID.Data3 = (short)(n2 |= byArray[7] & 0xFF);
            gUID.Data4[0] = byArray[8];
            gUID.Data4[1] = byArray[9];
            gUID.Data4[2] = byArray[10];
            gUID.Data4[3] = byArray[11];
            gUID.Data4[4] = byArray[12];
            gUID.Data4[5] = byArray[13];
            gUID.Data4[6] = byArray[14];
            gUID.Data4[7] = byArray[15];
            gUID.writeFieldsToMemory();
            return gUID;
        }

        public static GUID fromString(String string) {
            int n2;
            int n3 = 0;
            char[] cArray = new char[32];
            char[] cArray2 = string.toCharArray();
            byte[] byArray = new byte[16];
            GUID gUID = new GUID();
            if (string.length() > 38) {
                throw new IllegalArgumentException("Invalid guid length: " + string.length());
            }
            for (n2 = 0; n2 < cArray2.length; ++n2) {
                if (cArray2[n2] == '{' || cArray2[n2] == '-' || cArray2[n2] == '}') continue;
                cArray[n3++] = cArray2[n2];
            }
            for (n2 = 0; n2 < 32; n2 += 2) {
                byArray[n2 / 2] = (byte)((Character.digit(cArray[n2], 16) << 4) + Character.digit(cArray[n2 + 1], 16));
            }
            int cfr_ignored_0 = byArray.length;
            long l2 = byArray[0] & 0xFF;
            l2 <<= 8;
            l2 |= (long)(byArray[1] & 0xFF);
            l2 <<= 8;
            l2 |= (long)(byArray[2] & 0xFF);
            l2 <<= 8;
            gUID.Data1 = (int)(l2 |= (long)(byArray[3] & 0xFF));
            int n4 = byArray[4] & 0xFF;
            n4 <<= 8;
            gUID.Data2 = (short)(n4 |= byArray[5] & 0xFF);
            n4 = byArray[6] & 0xFF;
            n4 <<= 8;
            gUID.Data3 = (short)(n4 |= byArray[7] & 0xFF);
            gUID.Data4[0] = byArray[8];
            gUID.Data4[1] = byArray[9];
            gUID.Data4[2] = byArray[10];
            gUID.Data4[3] = byArray[11];
            gUID.Data4[4] = byArray[12];
            gUID.Data4[5] = byArray[13];
            gUID.Data4[6] = byArray[14];
            gUID.Data4[7] = byArray[15];
            gUID.writeFieldsToMemory();
            return gUID;
        }

        public static GUID newGuid() {
            SecureRandom secureRandom = new SecureRandom();
            byte[] byArray = new byte[16];
            secureRandom.nextBytes(byArray);
            byArray[6] = (byte)(byArray[6] & 0xF);
            byArray[6] = (byte)(byArray[6] | 0x40);
            byArray[8] = (byte)(byArray[8] & 0x3F);
            byArray[8] = (byte)(byArray[8] | 0x80);
            return new GUID(byArray);
        }

        public byte[] toByteArray() {
            byte[] byArray = new byte[16];
            byte[] byArray2 = new byte[4];
            byte[] byArray3 = byArray2;
            byArray2[0] = (byte)(this.Data1 >> 24);
            byArray3[1] = (byte)(this.Data1 >> 16);
            byArray3[2] = (byte)(this.Data1 >> 8);
            byArray3[3] = (byte)this.Data1;
            byte[] byArray4 = new byte[4];
            byte[] byArray5 = byArray4;
            byArray4[0] = (byte)(this.Data2 >> 24);
            byArray5[1] = (byte)(this.Data2 >> 16);
            byArray5[2] = (byte)(this.Data2 >> 8);
            byArray5[3] = (byte)this.Data2;
            byte[] byArray6 = new byte[4];
            byte[] byArray7 = byArray6;
            byArray6[0] = (byte)(this.Data3 >> 24);
            byArray7[1] = (byte)(this.Data3 >> 16);
            byArray7[2] = (byte)(this.Data3 >> 8);
            byArray7[3] = (byte)this.Data3;
            System.arraycopy(byArray3, 0, byArray, 0, 4);
            System.arraycopy(byArray5, 2, byArray, 4, 2);
            System.arraycopy(byArray7, 2, byArray, 6, 2);
            System.arraycopy(this.Data4, 0, byArray, 8, 8);
            return byArray;
        }

        public String toGuidString() {
            byte[] byArray = this.toByteArray();
            StringBuilder stringBuilder = new StringBuilder(2 * byArray.length);
            stringBuilder.append("{");
            for (int i2 = 0; i2 < byArray.length; ++i2) {
                char c2 = "0123456789ABCDEF".charAt((byArray[i2] & 0xF0) >> 4);
                char c3 = "0123456789ABCDEF".charAt(byArray[i2] & 0xF);
                stringBuilder.append(c2).append(c3);
                if (i2 != 3 && i2 != 5 && i2 != 7 && i2 != 9) continue;
                stringBuilder.append("-");
            }
            stringBuilder.append("}");
            return stringBuilder.toString();
        }

        protected void writeFieldsToMemory() {
            for (String string : this.getFieldOrder()) {
                this.writeField(string);
            }
        }

        public static class ByReference
        extends GUID
        implements Structure.ByReference {
            public ByReference() {
            }

            public ByReference(GUID gUID) {
                super(gUID.getPointer());
                this.Data1 = gUID.Data1;
                this.Data2 = gUID.Data2;
                this.Data3 = gUID.Data3;
                this.Data4 = gUID.Data4;
            }

            public ByReference(Pointer pointer) {
                super(pointer);
            }
        }

        public static class ByValue
        extends GUID
        implements Structure.ByValue {
            public ByValue() {
            }

            public ByValue(GUID gUID) {
                super(gUID.getPointer());
                this.Data1 = gUID.Data1;
                this.Data2 = gUID.Data2;
                this.Data3 = gUID.Data3;
                this.Data4 = gUID.Data4;
            }

            public ByValue(Pointer pointer) {
                super(pointer);
            }
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Union;
import com.sun.jna.platform.win32.COM.Dispatch;
import com.sun.jna.platform.win32.COM.IDispatch;
import com.sun.jna.platform.win32.COM.Unknown;
import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.platform.win32.OleAuto;
import com.sun.jna.platform.win32.WTypes;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.DoubleByReference;
import com.sun.jna.ptr.FloatByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.ShortByReference;
import java.util.Date;

public interface Variant {
    public static final int VT_EMPTY = 0;
    public static final int VT_NULL = 1;
    public static final int VT_I2 = 2;
    public static final int VT_I4 = 3;
    public static final int VT_R4 = 4;
    public static final int VT_R8 = 5;
    public static final int VT_CY = 6;
    public static final int VT_DATE = 7;
    public static final int VT_BSTR = 8;
    public static final int VT_DISPATCH = 9;
    public static final int VT_ERROR = 10;
    public static final int VT_BOOL = 11;
    public static final int VT_VARIANT = 12;
    public static final int VT_UNKNOWN = 13;
    public static final int VT_DECIMAL = 14;
    public static final int VT_I1 = 16;
    public static final int VT_UI1 = 17;
    public static final int VT_UI2 = 18;
    public static final int VT_UI4 = 19;
    public static final int VT_I8 = 20;
    public static final int VT_UI8 = 21;
    public static final int VT_INT = 22;
    public static final int VT_UINT = 23;
    public static final int VT_VOID = 24;
    public static final int VT_HRESULT = 25;
    public static final int VT_PTR = 26;
    public static final int VT_SAFEARRAY = 27;
    public static final int VT_CARRAY = 28;
    public static final int VT_USERDEFINED = 29;
    public static final int VT_LPSTR = 30;
    public static final int VT_LPWSTR = 31;
    public static final int VT_RECORD = 36;
    public static final int VT_INT_PTR = 37;
    public static final int VT_UINT_PTR = 38;
    public static final int VT_FILETIME = 64;
    public static final int VT_BLOB = 65;
    public static final int VT_STREAM = 66;
    public static final int VT_STORAGE = 67;
    public static final int VT_STREAMED_OBJECT = 68;
    public static final int VT_STORED_OBJECT = 69;
    public static final int VT_BLOB_OBJECT = 70;
    public static final int VT_CF = 71;
    public static final int VT_CLSID = 72;
    public static final int VT_VERSIONED_STREAM = 73;
    public static final int VT_BSTR_BLOB = 4095;
    public static final int VT_VECTOR = 4096;
    public static final int VT_ARRAY = 8192;
    public static final int VT_BYREF = 16384;
    public static final int VT_RESERVED = 32768;
    public static final int VT_ILLEGAL = 65535;
    public static final int VT_ILLEGALMASKED = 4095;
    public static final int VT_TYPEMASK = 4095;
    public static final OaIdl.VARIANT_BOOL VARIANT_TRUE = new OaIdl.VARIANT_BOOL(65535L);
    public static final OaIdl.VARIANT_BOOL VARIANT_FALSE = new OaIdl.VARIANT_BOOL(0L);

    @Structure.FieldOrder(value={"variantArg"})
    public static class VariantArg
    extends Structure {
        public VARIANT[] variantArg;

        public VariantArg() {
            this.variantArg = new VARIANT[1];
        }

        public VariantArg(Pointer pointer) {
            super(pointer);
            this.variantArg = new VARIANT[1];
        }

        public VariantArg(VARIANT[] vARIANTArray) {
            this.variantArg = vARIANTArray;
        }

        public void setArraySize(int n2) {
            this.variantArg = new VARIANT[n2];
            this.read();
        }

        public static class ByReference
        extends VariantArg
        implements Structure.ByReference {
            public ByReference() {
            }

            public ByReference(VARIANT[] vARIANTArray) {
                super(vARIANTArray);
            }
        }
    }

    public static class VARIANT
    extends Union {
        public static final VARIANT VARIANT_MISSING = new VARIANT();
        public _VARIANT _variant;
        public OaIdl.DECIMAL decVal;

        public VARIANT() {
            this.setType("_variant");
            this.read();
        }

        public VARIANT(Pointer pointer) {
            super(pointer);
            this.setType("_variant");
            this.read();
        }

        public VARIANT(WTypes.BSTR bSTR) {
            this();
            this.setValue(8, (Object)bSTR);
        }

        public VARIANT(WTypes.BSTRByReference bSTRByReference) {
            this();
            this.setValue(16392, (Object)bSTRByReference);
        }

        public VARIANT(OaIdl.VARIANT_BOOL vARIANT_BOOL) {
            this();
            this.setValue(11, (Object)vARIANT_BOOL);
        }

        public VARIANT(WinDef.BOOL bOOL) {
            this(bOOL.booleanValue());
        }

        public VARIANT(WinDef.LONG lONG) {
            this();
            this.setValue(3, (Object)lONG);
        }

        public VARIANT(WinDef.SHORT sHORT) {
            this();
            this.setValue(2, (Object)sHORT);
        }

        public VARIANT(OaIdl.DATE dATE) {
            this();
            this.setValue(7, (Object)dATE);
        }

        public VARIANT(byte by) {
            this(new WinDef.BYTE((long)by));
        }

        public VARIANT(WinDef.BYTE bYTE) {
            this();
            this.setValue(17, (Object)bYTE);
        }

        public VARIANT(char c2) {
            this();
            this.setValue(18, (Object)new WinDef.USHORT((long)c2));
        }

        public VARIANT(WinDef.CHAR cHAR) {
            this();
            this.setValue(16, (Object)cHAR);
        }

        public VARIANT(short s2) {
            this();
            this.setValue(2, (Object)new WinDef.SHORT((long)s2));
        }

        public VARIANT(int n2) {
            this();
            this.setValue(3, (Object)new WinDef.LONG((long)n2));
        }

        public VARIANT(IntByReference intByReference) {
            this();
            this.setValue(16406, (Object)intByReference);
        }

        public VARIANT(long l2) {
            this();
            this.setValue(20, (Object)new WinDef.LONGLONG(l2));
        }

        public VARIANT(float f2) {
            this();
            this.setValue(4, (Object)Float.valueOf(f2));
        }

        public VARIANT(double d2) {
            this();
            this.setValue(5, (Object)d2);
        }

        public VARIANT(String object) {
            this();
            object = OleAuto.INSTANCE.SysAllocString((String)object);
            this.setValue(8, object);
        }

        public VARIANT(boolean bl) {
            this();
            this.setValue(11, (Object)new OaIdl.VARIANT_BOOL(bl));
        }

        @Deprecated
        public VARIANT(IDispatch iDispatch) {
            this();
            this.setValue(9, (Object)iDispatch);
        }

        public VARIANT(Dispatch dispatch) {
            this();
            this.setValue(9, (Object)dispatch);
        }

        public VARIANT(Date date) {
            this();
            this.setValue(7, (Object)new OaIdl.DATE(date));
        }

        public VARIANT(OaIdl.SAFEARRAY sAFEARRAY) {
            this();
            this.setValue(sAFEARRAY);
        }

        public VARIANT(OaIdl.SAFEARRAYByReference sAFEARRAYByReference) {
            this();
            this.setValue(sAFEARRAYByReference);
        }

        public WTypes.VARTYPE getVarType() {
            this.read();
            return this._variant.vt;
        }

        public void setVarType(short s2) {
            this._variant.vt = new WTypes.VARTYPE(s2);
        }

        public void setValue(int n2, Object object) {
            this.setValue(new WTypes.VARTYPE(n2), object);
        }

        public void setValue(OaIdl.SAFEARRAY sAFEARRAY) {
            this.setValue(sAFEARRAY.getVarType().intValue() | 0x2000, (Object)sAFEARRAY);
        }

        public void setValue(OaIdl.SAFEARRAYByReference sAFEARRAYByReference) {
            this.setValue(sAFEARRAYByReference.pSAFEARRAY.getVarType().intValue() | 0x2000 | 0x4000, (Object)sAFEARRAYByReference);
        }

        public void setValue(WTypes.VARTYPE vARTYPE, Object object) {
            int n2 = vARTYPE.intValue();
            switch (n2) {
                case 17: {
                    ((Structure)this._variant.__variant).writeField("bVal", object);
                    break;
                }
                case 2: {
                    ((Structure)this._variant.__variant).writeField("iVal", object);
                    break;
                }
                case 3: {
                    ((Structure)this._variant.__variant).writeField("lVal", object);
                    break;
                }
                case 20: {
                    ((Structure)this._variant.__variant).writeField("llVal", object);
                    break;
                }
                case 4: {
                    ((Structure)this._variant.__variant).writeField("fltVal", object);
                    break;
                }
                case 5: {
                    ((Structure)this._variant.__variant).writeField("dblVal", object);
                    break;
                }
                case 11: {
                    ((Structure)this._variant.__variant).writeField("boolVal", object);
                    break;
                }
                case 10: {
                    ((Structure)this._variant.__variant).writeField("scode", object);
                    break;
                }
                case 6: {
                    ((Structure)this._variant.__variant).writeField("cyVal", object);
                    break;
                }
                case 7: {
                    ((Structure)this._variant.__variant).writeField("date", object);
                    break;
                }
                case 8: {
                    ((Structure)this._variant.__variant).writeField("bstrVal", object);
                    break;
                }
                case 13: {
                    ((Structure)this._variant.__variant).writeField("punkVal", object);
                    break;
                }
                case 9: {
                    ((Structure)this._variant.__variant).writeField("pdispVal", object);
                    break;
                }
                case 16401: {
                    ((Structure)this._variant.__variant).writeField("pbVal", object);
                    break;
                }
                case 16386: {
                    ((Structure)this._variant.__variant).writeField("piVal", object);
                    break;
                }
                case 16387: {
                    ((Structure)this._variant.__variant).writeField("plVal", object);
                    break;
                }
                case 16404: {
                    ((Structure)this._variant.__variant).writeField("pllVal", object);
                    break;
                }
                case 16388: {
                    ((Structure)this._variant.__variant).writeField("pfltVal", object);
                    break;
                }
                case 16389: {
                    ((Structure)this._variant.__variant).writeField("pdblVal", object);
                    break;
                }
                case 16395: {
                    ((Structure)this._variant.__variant).writeField("pboolVal", object);
                    break;
                }
                case 16394: {
                    ((Structure)this._variant.__variant).writeField("pscode", object);
                    break;
                }
                case 16390: {
                    ((Structure)this._variant.__variant).writeField("pcyVal", object);
                    break;
                }
                case 16391: {
                    ((Structure)this._variant.__variant).writeField("pdate", object);
                    break;
                }
                case 16392: {
                    ((Structure)this._variant.__variant).writeField("pbstrVal", object);
                    break;
                }
                case 16397: {
                    ((Structure)this._variant.__variant).writeField("ppunkVal", object);
                    break;
                }
                case 16393: {
                    ((Structure)this._variant.__variant).writeField("ppdispVal", object);
                    break;
                }
                case 16396: {
                    ((Structure)this._variant.__variant).writeField("pvarVal", object);
                    break;
                }
                case 16384: {
                    ((Structure)this._variant.__variant).writeField("byref", object);
                    break;
                }
                case 16: {
                    ((Structure)this._variant.__variant).writeField("cVal", object);
                    break;
                }
                case 18: {
                    ((Structure)this._variant.__variant).writeField("uiVal", object);
                    break;
                }
                case 19: {
                    ((Structure)this._variant.__variant).writeField("ulVal", object);
                    break;
                }
                case 21: {
                    ((Structure)this._variant.__variant).writeField("ullVal", object);
                    break;
                }
                case 22: {
                    ((Structure)this._variant.__variant).writeField("intVal", object);
                    break;
                }
                case 23: {
                    ((Structure)this._variant.__variant).writeField("uintVal", object);
                    break;
                }
                case 16398: {
                    ((Structure)this._variant.__variant).writeField("pdecVal", object);
                    break;
                }
                case 16400: {
                    ((Structure)this._variant.__variant).writeField("pcVal", object);
                    break;
                }
                case 16402: {
                    ((Structure)this._variant.__variant).writeField("puiVal", object);
                    break;
                }
                case 16403: {
                    ((Structure)this._variant.__variant).writeField("pulVal", object);
                    break;
                }
                case 16405: {
                    ((Structure)this._variant.__variant).writeField("pullVal", object);
                    break;
                }
                case 16406: {
                    ((Structure)this._variant.__variant).writeField("pintVal", object);
                    break;
                }
                case 16407: {
                    ((Structure)this._variant.__variant).writeField("puintVal", object);
                    break;
                }
                case 36: {
                    ((Structure)this._variant.__variant).writeField("pvRecord", object);
                    break;
                }
                default: {
                    if ((n2 & 0x2000) <= 0) break;
                    if ((n2 & 0x4000) > 0) {
                        ((Structure)this._variant.__variant).writeField("pparray", object);
                        break;
                    }
                    ((Structure)this._variant.__variant).writeField("parray", object);
                }
            }
            this._variant.writeField("vt", vARTYPE);
            this.write();
        }

        public Object getValue() {
            int n2 = this.getVarType().intValue();
            switch (n2) {
                case 17: {
                    return ((Structure)this._variant.__variant).readField("bVal");
                }
                case 2: {
                    return ((Structure)this._variant.__variant).readField("iVal");
                }
                case 3: {
                    return ((Structure)this._variant.__variant).readField("lVal");
                }
                case 20: {
                    return ((Structure)this._variant.__variant).readField("llVal");
                }
                case 4: {
                    return ((Structure)this._variant.__variant).readField("fltVal");
                }
                case 5: {
                    return ((Structure)this._variant.__variant).readField("dblVal");
                }
                case 11: {
                    return ((Structure)this._variant.__variant).readField("boolVal");
                }
                case 10: {
                    return ((Structure)this._variant.__variant).readField("scode");
                }
                case 6: {
                    return ((Structure)this._variant.__variant).readField("cyVal");
                }
                case 7: {
                    return ((Structure)this._variant.__variant).readField("date");
                }
                case 8: {
                    return ((Structure)this._variant.__variant).readField("bstrVal");
                }
                case 13: {
                    return ((Structure)this._variant.__variant).readField("punkVal");
                }
                case 9: {
                    return ((Structure)this._variant.__variant).readField("pdispVal");
                }
                case 16401: {
                    return ((Structure)this._variant.__variant).readField("pbVal");
                }
                case 16386: {
                    return ((Structure)this._variant.__variant).readField("piVal");
                }
                case 16387: {
                    return ((Structure)this._variant.__variant).readField("plVal");
                }
                case 16404: {
                    return ((Structure)this._variant.__variant).readField("pllVal");
                }
                case 16388: {
                    return ((Structure)this._variant.__variant).readField("pfltVal");
                }
                case 16389: {
                    return ((Structure)this._variant.__variant).readField("pdblVal");
                }
                case 16395: {
                    return ((Structure)this._variant.__variant).readField("pboolVal");
                }
                case 16394: {
                    return ((Structure)this._variant.__variant).readField("pscode");
                }
                case 16390: {
                    return ((Structure)this._variant.__variant).readField("pcyVal");
                }
                case 16391: {
                    return ((Structure)this._variant.__variant).readField("pdate");
                }
                case 16392: {
                    return ((Structure)this._variant.__variant).readField("pbstrVal");
                }
                case 16397: {
                    return ((Structure)this._variant.__variant).readField("ppunkVal");
                }
                case 16393: {
                    return ((Structure)this._variant.__variant).readField("ppdispVal");
                }
                case 16396: {
                    return ((Structure)this._variant.__variant).readField("pvarVal");
                }
                case 16384: {
                    return ((Structure)this._variant.__variant).readField("byref");
                }
                case 16: {
                    return ((Structure)this._variant.__variant).readField("cVal");
                }
                case 18: {
                    return ((Structure)this._variant.__variant).readField("uiVal");
                }
                case 19: {
                    return ((Structure)this._variant.__variant).readField("ulVal");
                }
                case 21: {
                    return ((Structure)this._variant.__variant).readField("ullVal");
                }
                case 22: {
                    return ((Structure)this._variant.__variant).readField("intVal");
                }
                case 23: {
                    return ((Structure)this._variant.__variant).readField("uintVal");
                }
                case 16398: {
                    return ((Structure)this._variant.__variant).readField("pdecVal");
                }
                case 16400: {
                    return ((Structure)this._variant.__variant).readField("pcVal");
                }
                case 16402: {
                    return ((Structure)this._variant.__variant).readField("puiVal");
                }
                case 16403: {
                    return ((Structure)this._variant.__variant).readField("pulVal");
                }
                case 16405: {
                    return ((Structure)this._variant.__variant).readField("pullVal");
                }
                case 16406: {
                    return ((Structure)this._variant.__variant).readField("pintVal");
                }
                case 16407: {
                    return ((Structure)this._variant.__variant).readField("puintVal");
                }
                case 36: {
                    return ((Structure)this._variant.__variant).readField("pvRecord");
                }
            }
            if ((n2 & 0x2000) > 0) {
                if ((n2 & 0x4000) > 0) {
                    return ((Structure)this._variant.__variant).readField("pparray");
                }
                return ((Structure)this._variant.__variant).readField("parray");
            }
            return null;
        }

        public byte byteValue() {
            return ((Number)this.getValue()).byteValue();
        }

        public short shortValue() {
            return ((Number)this.getValue()).shortValue();
        }

        public int intValue() {
            return ((Number)this.getValue()).intValue();
        }

        public long longValue() {
            return ((Number)this.getValue()).longValue();
        }

        public float floatValue() {
            return ((Number)this.getValue()).floatValue();
        }

        public double doubleValue() {
            return ((Number)this.getValue()).doubleValue();
        }

        public String stringValue() {
            WTypes.BSTR bSTR = (WTypes.BSTR)this.getValue();
            if (bSTR == null) {
                return null;
            }
            return bSTR.getValue();
        }

        public boolean booleanValue() {
            return ((OaIdl.VARIANT_BOOL)this.getValue()).booleanValue();
        }

        public Date dateValue() {
            OaIdl.DATE dATE = (OaIdl.DATE)this.getValue();
            if (dATE == null) {
                return null;
            }
            return dATE.getAsJavaDate();
        }

        static {
            VARIANT_MISSING.setValue(10, (Object)new WinDef.SCODE(-2147352572L));
        }

        @Structure.FieldOrder(value={"vt", "wReserved1", "wReserved2", "wReserved3", "__variant"})
        public static class _VARIANT
        extends Structure {
            public WTypes.VARTYPE vt;
            public short wReserved1;
            public short wReserved2;
            public short wReserved3;
            public __VARIANT __variant;

            public _VARIANT() {
            }

            public _VARIANT(Pointer pointer) {
                super(pointer);
                this.read();
            }

            public static class __VARIANT
            extends Union {
                public WinDef.LONGLONG llVal;
                public WinDef.LONG lVal;
                public WinDef.BYTE bVal;
                public WinDef.SHORT iVal;
                public Float fltVal;
                public Double dblVal;
                public OaIdl.VARIANT_BOOL boolVal;
                public WinDef.SCODE scode;
                public OaIdl.CURRENCY cyVal;
                public OaIdl.DATE date;
                public WTypes.BSTR bstrVal;
                public Unknown punkVal;
                public Dispatch pdispVal;
                public OaIdl.SAFEARRAY.ByReference parray;
                public ByteByReference pbVal;
                public ShortByReference piVal;
                public WinDef.LONGByReference plVal;
                public WinDef.LONGLONGByReference pllVal;
                public FloatByReference pfltVal;
                public DoubleByReference pdblVal;
                public OaIdl.VARIANT_BOOLByReference pboolVal;
                public OaIdl._VARIANT_BOOLByReference pbool;
                public WinDef.SCODEByReference pscode;
                public OaIdl.CURRENCY.ByReference pcyVal;
                public OaIdl.DATE.ByReference pdate;
                public WTypes.BSTRByReference pbstrVal;
                public Unknown.ByReference ppunkVal;
                public Dispatch.ByReference ppdispVal;
                public OaIdl.SAFEARRAYByReference pparray;
                public ByReference pvarVal;
                public WinDef.PVOID byref;
                public WinDef.CHAR cVal;
                public WinDef.USHORT uiVal;
                public WinDef.ULONG ulVal;
                public WinDef.ULONGLONG ullVal;
                public Integer intVal;
                public WinDef.UINT uintVal;
                public OaIdl.DECIMAL.ByReference pdecVal;
                public WinDef.CHARByReference pcVal;
                public WinDef.USHORTByReference puiVal;
                public WinDef.ULONGByReference pulVal;
                public WinDef.ULONGLONGByReference pullVal;
                public IntByReference pintVal;
                public WinDef.UINTByReference puintVal;
                public BRECORD pvRecord;

                public __VARIANT() {
                    this.read();
                }

                public __VARIANT(Pointer pointer) {
                    super(pointer);
                    this.read();
                }

                @Structure.FieldOrder(value={"pvRecord", "pRecInfo"})
                public static class BRECORD
                extends Structure {
                    public WinDef.PVOID pvRecord;
                    public Pointer pRecInfo;

                    public BRECORD() {
                    }

                    public BRECORD(Pointer pointer) {
                        super(pointer);
                    }

                    public static class ByReference
                    extends BRECORD
                    implements Structure.ByReference {
                    }
                }
            }
        }

        public static class ByValue
        extends VARIANT
        implements Structure.ByValue {
            public ByValue(VARIANT vARIANT) {
                this.setValue(vARIANT.getVarType(), vARIANT.getValue());
            }

            public ByValue(Pointer pointer) {
                super(pointer);
            }

            public ByValue() {
            }
        }

        public static class ByReference
        extends VARIANT
        implements Structure.ByReference {
            public ByReference(VARIANT vARIANT) {
                this.setValue(vARIANT.getVarType(), vARIANT.getValue());
            }

            public ByReference(Pointer pointer) {
                super(pointer);
            }

            public ByReference() {
            }
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package org.bridj.cpp.com;

import org.bridj.Pointer;
import org.bridj.StructObject;
import org.bridj.ann.CLong;
import org.bridj.ann.Field;
import org.bridj.ann.Runtime;
import org.bridj.ann.Union;
import org.bridj.cpp.com.COMRuntime;
import org.bridj.cpp.com.CY;
import org.bridj.cpp.com.DECIMAL;
import org.bridj.cpp.com.IDispatch;
import org.bridj.cpp.com.IRecordInfo;
import org.bridj.cpp.com.IUnknown;
import org.bridj.cpp.com.SAFEARRAY;

@Runtime(value=COMRuntime.class)
public class VARIANT
extends StructObject {
    public VARIANT(Object object) {
        this.setValue(object);
    }

    public VARIANT() {
    }

    public VARIANT clone() {
        return COMRuntime.clone(this);
    }

    @Field(value=0)
    public __VARIANT_NAME_1_union __VARIANT_NAME_1() {
        return (__VARIANT_NAME_1_union)this.io.getNativeObjectField(this, 0);
    }

    public Object getValue() {
        return COMRuntime.getValue(this);
    }

    public VARIANT setValue(Object object) {
        return COMRuntime.setValue(this, object);
    }

    public String toString() {
        return COMRuntime.toString(this);
    }

    @Union
    public static class __VARIANT_NAME_1_union
    extends StructObject {
        @Field(value=0)
        public __tagVARIANT __VARIANT_NAME_2() {
            return (__tagVARIANT)this.io.getNativeObjectField(this, 0);
        }

        @Field(value=1)
        public DECIMAL decVal() {
            return (DECIMAL)this.io.getNativeObjectField(this, 1);
        }

        public static class __tagVARIANT
        extends StructObject {
            @Field(value=0)
            public short vt() {
                return this.io.getShortField(this, 0);
            }

            @Field(value=0)
            public __tagVARIANT vt(short s2) {
                this.io.setShortField(this, 0, s2);
                return this;
            }

            public final short vt_$eq(short s2) {
                this.vt(s2);
                return s2;
            }

            @Field(value=1)
            public short wReserved1() {
                return this.io.getShortField(this, 1);
            }

            @Field(value=1)
            public __tagVARIANT wReserved1(short s2) {
                this.io.setShortField(this, 1, s2);
                return this;
            }

            public final short wReserved1_$eq(short s2) {
                this.wReserved1(s2);
                return s2;
            }

            @Field(value=2)
            public short wReserved2() {
                return this.io.getShortField(this, 2);
            }

            @Field(value=2)
            public __tagVARIANT wReserved2(short s2) {
                this.io.setShortField(this, 2, s2);
                return this;
            }

            public final short wReserved2_$eq(short s2) {
                this.wReserved2(s2);
                return s2;
            }

            @Field(value=3)
            public short wReserved3() {
                return this.io.getShortField(this, 3);
            }

            @Field(value=3)
            public __tagVARIANT wReserved3(short s2) {
                this.io.setShortField(this, 3, s2);
                return this;
            }

            public final short wReserved3_$eq(short s2) {
                this.wReserved3(s2);
                return s2;
            }

            @Field(value=4)
            public __VARIANT_NAME_3_union __VARIANT_NAME_3() {
                return (__VARIANT_NAME_3_union)this.io.getNativeObjectField(this, 4);
            }

            /*
             * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
             */
            @Union
            public static class __VARIANT_NAME_3_union
            extends StructObject {
                @Field(value=0)
                public long llval() {
                    return this.io.getLongField(this, 0);
                }

                @Field(value=0)
                public __VARIANT_NAME_3_union llval(long l2) {
                    this.io.setLongField(this, 0, l2);
                    return this;
                }

                public final long llval_$eq(long l2) {
                    this.llval(l2);
                    return l2;
                }

                @CLong
                @Field(value=1)
                public long lVal() {
                    return this.io.getCLongField(this, 1);
                }

                @CLong
                @Field(value=1)
                public __VARIANT_NAME_3_union lVal(long l2) {
                    this.io.setCLongField(this, 1, l2);
                    return this;
                }

                public final long lVal_$eq(long l2) {
                    this.lVal(l2);
                    return l2;
                }

                @Field(value=2)
                public byte bVal() {
                    return this.io.getByteField(this, 2);
                }

                @Field(value=2)
                public __VARIANT_NAME_3_union bVal(byte by) {
                    this.io.setByteField(this, 2, by);
                    return this;
                }

                public final byte bVal_$eq(byte by) {
                    this.bVal(by);
                    return by;
                }

                @Field(value=3)
                public short iVal() {
                    return this.io.getShortField(this, 3);
                }

                @Field(value=3)
                public __VARIANT_NAME_3_union iVal(short s2) {
                    this.io.setShortField(this, 3, s2);
                    return this;
                }

                public final short iVal_$eq(short s2) {
                    this.iVal(s2);
                    return s2;
                }

                @Field(value=4)
                public float fltVal() {
                    return this.io.getFloatField(this, 4);
                }

                @Field(value=4)
                public __VARIANT_NAME_3_union fltVal(float f2) {
                    this.io.setFloatField(this, 4, f2);
                    return this;
                }

                public final float fltVal_$eq(float f2) {
                    this.fltVal(f2);
                    return f2;
                }

                @Field(value=5)
                public double dblVal() {
                    return this.io.getDoubleField(this, 5);
                }

                @Field(value=5)
                public __VARIANT_NAME_3_union dblVal(double d2) {
                    this.io.setDoubleField(this, 5, d2);
                    return this;
                }

                public final double dblVal_$eq(double d2) {
                    this.dblVal(d2);
                    return d2;
                }

                @Field(value=6)
                public int boolVal() {
                    return this.io.getIntField(this, 6);
                }

                @Field(value=6)
                public __VARIANT_NAME_3_union boolVal(int n2) {
                    this.io.setIntField(this, 6, n2);
                    return this;
                }

                public final int boolVal_$eq(int n2) {
                    this.boolVal(n2);
                    return n2;
                }

                @Field(value=7)
                public int bool() {
                    return this.io.getIntField(this, 7);
                }

                @Field(value=7)
                public __VARIANT_NAME_3_union bool(int n2) {
                    this.io.setIntField(this, 7, n2);
                    return this;
                }

                public final int bool_$eq(int n2) {
                    this.bool(n2);
                    return n2;
                }

                @Field(value=8)
                public int scode() {
                    return this.io.getIntField(this, 8);
                }

                @Field(value=8)
                public __VARIANT_NAME_3_union scode(int n2) {
                    this.io.setIntField(this, 8, n2);
                    return this;
                }

                public final int scode_$eq(int n2) {
                    this.scode(n2);
                    return n2;
                }

                @Field(value=9)
                public CY cyVal() {
                    return (CY)this.io.getNativeObjectField(this, 9);
                }

                @Field(value=10)
                public double date() {
                    return this.io.getDoubleField(this, 10);
                }

                @Field(value=10)
                public __VARIANT_NAME_3_union date(double d2) {
                    this.io.setDoubleField(this, 10, d2);
                    return this;
                }

                public final double date_$eq(double d2) {
                    this.date(d2);
                    return d2;
                }

                @Field(value=11)
                public Pointer<Byte> bstrVal() {
                    return this.io.getPointerField(this, 11);
                }

                @Field(value=11)
                public __VARIANT_NAME_3_union bstrVal(Pointer<Byte> pointer) {
                    this.io.setPointerField(this, 11, pointer);
                    return this;
                }

                public final Pointer<Byte> bstrVal_$eq(Pointer<Byte> pointer) {
                    this.bstrVal(pointer);
                    return pointer;
                }

                @Field(value=12)
                public Pointer<IUnknown> punkVal() {
                    return this.io.getPointerField(this, 12);
                }

                @Field(value=12)
                public __VARIANT_NAME_3_union punkVal(Pointer<IUnknown> pointer) {
                    this.io.setPointerField(this, 12, pointer);
                    return this;
                }

                public final Pointer<IUnknown> punkVal_$eq(Pointer<IUnknown> pointer) {
                    this.punkVal(pointer);
                    return pointer;
                }

                @Field(value=13)
                public Pointer<IDispatch> pdispVal() {
                    return this.io.getPointerField(this, 13);
                }

                @Field(value=13)
                public __VARIANT_NAME_3_union pdispVal(Pointer<IDispatch> pointer) {
                    this.io.setPointerField(this, 13, pointer);
                    return this;
                }

                public final Pointer<IDispatch> pdispVal_$eq(Pointer<IDispatch> pointer) {
                    this.pdispVal(pointer);
                    return pointer;
                }

                @Field(value=14)
                public Pointer<SAFEARRAY> parray() {
                    return this.io.getPointerField(this, 14);
                }

                @Field(value=14)
                public __VARIANT_NAME_3_union parray(Pointer<SAFEARRAY> pointer) {
                    this.io.setPointerField(this, 14, pointer);
                    return this;
                }

                public final Pointer<SAFEARRAY> parray_$eq(Pointer<SAFEARRAY> pointer) {
                    this.parray(pointer);
                    return pointer;
                }

                @Field(value=15)
                public Pointer<Byte> pbVal() {
                    return this.io.getPointerField(this, 15);
                }

                @Field(value=15)
                public __VARIANT_NAME_3_union pbVal(Pointer<Byte> pointer) {
                    this.io.setPointerField(this, 15, pointer);
                    return this;
                }

                public final Pointer<Byte> pbVal_$eq(Pointer<Byte> pointer) {
                    this.pbVal(pointer);
                    return pointer;
                }

                @Field(value=16)
                public Pointer<Short> piVal() {
                    return this.io.getPointerField(this, 16);
                }

                @Field(value=16)
                public __VARIANT_NAME_3_union piVal(Pointer<Short> pointer) {
                    this.io.setPointerField(this, 16, pointer);
                    return this;
                }

                public final Pointer<Short> piVal_$eq(Pointer<Short> pointer) {
                    this.piVal(pointer);
                    return pointer;
                }

                @Field(value=17)
                public Pointer<CLong> plVal() {
                    return this.io.getPointerField(this, 17);
                }

                @Field(value=17)
                public __VARIANT_NAME_3_union plVal(Pointer<CLong> pointer) {
                    this.io.setPointerField(this, 17, pointer);
                    return this;
                }

                public final Pointer<CLong> plVal_$eq(Pointer<CLong> pointer) {
                    this.plVal(pointer);
                    return pointer;
                }

                @Field(value=18)
                public Pointer<Long> pllVal() {
                    return this.io.getPointerField(this, 18);
                }

                @Field(value=18)
                public __VARIANT_NAME_3_union pllVal(Pointer<Long> pointer) {
                    this.io.setPointerField(this, 18, pointer);
                    return this;
                }

                public final Pointer<Long> pllVal_$eq(Pointer<Long> pointer) {
                    this.pllVal(pointer);
                    return pointer;
                }

                @Field(value=19)
                public Pointer<Float> pfltVal() {
                    return this.io.getPointerField(this, 19);
                }

                @Field(value=19)
                public __VARIANT_NAME_3_union pfltVal(Pointer<Float> pointer) {
                    this.io.setPointerField(this, 19, pointer);
                    return this;
                }

                public final Pointer<Float> pfltVal_$eq(Pointer<Float> pointer) {
                    this.pfltVal(pointer);
                    return pointer;
                }

                @Field(value=20)
                public Pointer<Double> pdblVal() {
                    return this.io.getPointerField(this, 20);
                }

                @Field(value=20)
                public __VARIANT_NAME_3_union pdblVal(Pointer<Double> pointer) {
                    this.io.setPointerField(this, 20, pointer);
                    return this;
                }

                public final Pointer<Double> pdblVal_$eq(Pointer<Double> pointer) {
                    this.pdblVal(pointer);
                    return pointer;
                }

                @Field(value=21)
                public Pointer<Integer> pboolVal() {
                    return this.io.getPointerField(this, 21);
                }

                @Field(value=21)
                public __VARIANT_NAME_3_union pboolVal(Pointer<Integer> pointer) {
                    this.io.setPointerField(this, 21, pointer);
                    return this;
                }

                public final Pointer<Integer> pboolVal_$eq(Pointer<Integer> pointer) {
                    this.pboolVal(pointer);
                    return pointer;
                }

                @Field(value=22)
                public Pointer<Integer> pbool() {
                    return this.io.getPointerField(this, 22);
                }

                @Field(value=22)
                public __VARIANT_NAME_3_union pbool(Pointer<Integer> pointer) {
                    this.io.setPointerField(this, 22, pointer);
                    return this;
                }

                public final Pointer<Integer> pbool_$eq(Pointer<Integer> pointer) {
                    this.pbool(pointer);
                    return pointer;
                }

                @Field(value=23)
                public Pointer<Integer> pscode() {
                    return this.io.getPointerField(this, 23);
                }

                @Field(value=23)
                public __VARIANT_NAME_3_union pscode(Pointer<Integer> pointer) {
                    this.io.setPointerField(this, 23, pointer);
                    return this;
                }

                public final Pointer<Integer> pscode_$eq(Pointer<Integer> pointer) {
                    this.pscode(pointer);
                    return pointer;
                }

                @Field(value=24)
                public Pointer<CY> pcyVal() {
                    return this.io.getPointerField(this, 24);
                }

                @Field(value=24)
                public __VARIANT_NAME_3_union pcyVal(Pointer<CY> pointer) {
                    this.io.setPointerField(this, 24, pointer);
                    return this;
                }

                public final Pointer<CY> pcyVal_$eq(Pointer<CY> pointer) {
                    this.pcyVal(pointer);
                    return pointer;
                }

                @Field(value=25)
                public Pointer<Double> pdate() {
                    return this.io.getPointerField(this, 25);
                }

                @Field(value=25)
                public __VARIANT_NAME_3_union pdate(Pointer<Double> pointer) {
                    this.io.setPointerField(this, 25, pointer);
                    return this;
                }

                public final Pointer<Double> pdate_$eq(Pointer<Double> pointer) {
                    this.pdate(pointer);
                    return pointer;
                }

                @Field(value=26)
                public Pointer<Pointer<Byte>> pbstrVal() {
                    return this.io.getPointerField(this, 26);
                }

                @Field(value=26)
                public __VARIANT_NAME_3_union pbstrVal(Pointer<Pointer<Byte>> pointer) {
                    this.io.setPointerField(this, 26, pointer);
                    return this;
                }

                public final Pointer<Pointer<Byte>> pbstrVal_$eq(Pointer<Pointer<Byte>> pointer) {
                    this.pbstrVal(pointer);
                    return pointer;
                }

                @Field(value=27)
                public Pointer<Pointer<IUnknown>> ppunkVal() {
                    return this.io.getPointerField(this, 27);
                }

                @Field(value=27)
                public __VARIANT_NAME_3_union ppunkVal(Pointer<Pointer<IUnknown>> pointer) {
                    this.io.setPointerField(this, 27, pointer);
                    return this;
                }

                public final Pointer<Pointer<IUnknown>> ppunkVal_$eq(Pointer<Pointer<IUnknown>> pointer) {
                    this.ppunkVal(pointer);
                    return pointer;
                }

                @Field(value=28)
                public Pointer<Pointer<IDispatch>> ppdispVal() {
                    return this.io.getPointerField(this, 28);
                }

                @Field(value=28)
                public __VARIANT_NAME_3_union ppdispVal(Pointer<Pointer<IDispatch>> pointer) {
                    this.io.setPointerField(this, 28, pointer);
                    return this;
                }

                public final Pointer<Pointer<IDispatch>> ppdispVal_$eq(Pointer<Pointer<IDispatch>> pointer) {
                    this.ppdispVal(pointer);
                    return pointer;
                }

                @Field(value=29)
                public Pointer<Pointer<SAFEARRAY>> pparray() {
                    return this.io.getPointerField(this, 29);
                }

                @Field(value=29)
                public __VARIANT_NAME_3_union pparray(Pointer<Pointer<SAFEARRAY>> pointer) {
                    this.io.setPointerField(this, 29, pointer);
                    return this;
                }

                public final Pointer<Pointer<SAFEARRAY>> pparray_$eq(Pointer<Pointer<SAFEARRAY>> pointer) {
                    this.pparray(pointer);
                    return pointer;
                }

                @Field(value=30)
                public Pointer<VARIANT> pvarVal() {
                    return this.io.getPointerField(this, 30);
                }

                @Field(value=30)
                public __VARIANT_NAME_3_union pvarVal(Pointer<VARIANT> pointer) {
                    this.io.setPointerField(this, 30, pointer);
                    return this;
                }

                public final Pointer<VARIANT> pvarVal_$eq(Pointer<VARIANT> pointer) {
                    this.pvarVal(pointer);
                    return pointer;
                }

                @Field(value=31)
                public Pointer<Pointer<?>> byref() {
                    return this.io.getPointerField(this, 31);
                }

                @Field(value=31)
                public __VARIANT_NAME_3_union byref(Pointer<Pointer<?>> pointer) {
                    this.io.setPointerField(this, 31, pointer);
                    return this;
                }

                public final Pointer<Pointer<?>> byref_$eq(Pointer<Pointer<?>> pointer) {
                    this.byref(pointer);
                    return pointer;
                }

                @Field(value=32)
                public byte cVal() {
                    return this.io.getByteField(this, 32);
                }

                @Field(value=32)
                public __VARIANT_NAME_3_union cVal(byte by) {
                    this.io.setByteField(this, 32, by);
                    return this;
                }

                public final byte cVal_$eq(byte by) {
                    this.cVal(by);
                    return by;
                }

                @Field(value=33)
                public short uiVal() {
                    return this.io.getShortField(this, 33);
                }

                @Field(value=33)
                public __VARIANT_NAME_3_union uiVal(short s2) {
                    this.io.setShortField(this, 33, s2);
                    return this;
                }

                public final short uiVal_$eq(short s2) {
                    this.uiVal(s2);
                    return s2;
                }

                @Field(value=34)
                public int ulVal() {
                    return this.io.getIntField(this, 34);
                }

                @Field(value=34)
                public __VARIANT_NAME_3_union ulVal(int n2) {
                    this.io.setIntField(this, 34, n2);
                    return this;
                }

                public final int ulVal_$eq(int n2) {
                    this.ulVal(n2);
                    return n2;
                }

                @Field(value=35)
                public long ullVal() {
                    return this.io.getLongField(this, 35);
                }

                @Field(value=35)
                public __VARIANT_NAME_3_union ullVal(long l2) {
                    this.io.setLongField(this, 35, l2);
                    return this;
                }

                public final long ullVal_$eq(long l2) {
                    this.ullVal(l2);
                    return l2;
                }

                @Field(value=36)
                public int intVal() {
                    return this.io.getIntField(this, 36);
                }

                @Field(value=36)
                public __VARIANT_NAME_3_union intVal(int n2) {
                    this.io.setIntField(this, 36, n2);
                    return this;
                }

                public final int intVal_$eq(int n2) {
                    this.intVal(n2);
                    return n2;
                }

                @Field(value=37)
                public int uintVal() {
                    return this.io.getIntField(this, 37);
                }

                @Field(value=37)
                public __VARIANT_NAME_3_union uintVal(int n2) {
                    this.io.setIntField(this, 37, n2);
                    return this;
                }

                public final int uintVal_$eq(int n2) {
                    this.uintVal(n2);
                    return n2;
                }

                @Field(value=38)
                public Pointer<DECIMAL> pdecVal() {
                    return this.io.getPointerField(this, 38);
                }

                @Field(value=38)
                public __VARIANT_NAME_3_union pdecVal(Pointer<DECIMAL> pointer) {
                    this.io.setPointerField(this, 38, pointer);
                    return this;
                }

                public final Pointer<DECIMAL> pdecVal_$eq(Pointer<DECIMAL> pointer) {
                    this.pdecVal(pointer);
                    return pointer;
                }

                @Field(value=39)
                public Pointer<Byte> pcVal() {
                    return this.io.getPointerField(this, 39);
                }

                @Field(value=39)
                public __VARIANT_NAME_3_union pcVal(Pointer<Byte> pointer) {
                    this.io.setPointerField(this, 39, pointer);
                    return this;
                }

                public final Pointer<Byte> pcVal_$eq(Pointer<Byte> pointer) {
                    this.pcVal(pointer);
                    return pointer;
                }

                @Field(value=40)
                public Pointer<Short> puiVal() {
                    return this.io.getPointerField(this, 40);
                }

                @Field(value=40)
                public __VARIANT_NAME_3_union puiVal(Pointer<Short> pointer) {
                    this.io.setPointerField(this, 40, pointer);
                    return this;
                }

                public final Pointer<Short> puiVal_$eq(Pointer<Short> pointer) {
                    this.puiVal(pointer);
                    return pointer;
                }

                @Field(value=41)
                public Pointer<Integer> pulVal() {
                    return this.io.getPointerField(this, 41);
                }

                @Field(value=41)
                public __VARIANT_NAME_3_union pulVal(Pointer<Integer> pointer) {
                    this.io.setPointerField(this, 41, pointer);
                    return this;
                }

                public final Pointer<Integer> pulVal_$eq(Pointer<Integer> pointer) {
                    this.pulVal(pointer);
                    return pointer;
                }

                @Field(value=42)
                public Pointer<Long> pullVal() {
                    return this.io.getPointerField(this, 42);
                }

                @Field(value=42)
                public __VARIANT_NAME_3_union pullVal(Pointer<Long> pointer) {
                    this.io.setPointerField(this, 42, pointer);
                    return this;
                }

                public final Pointer<Long> pullVal_$eq(Pointer<Long> pointer) {
                    this.pullVal(pointer);
                    return pointer;
                }

                @Field(value=43)
                public Pointer<Integer> pintVal() {
                    return this.io.getPointerField(this, 43);
                }

                @Field(value=43)
                public __VARIANT_NAME_3_union pintVal(Pointer<Integer> pointer) {
                    this.io.setPointerField(this, 43, pointer);
                    return this;
                }

                public final Pointer<Integer> pintVal_$eq(Pointer<Integer> pointer) {
                    this.pintVal(pointer);
                    return pointer;
                }

                @Field(value=44)
                public Pointer<Integer> puintVal() {
                    return this.io.getPointerField(this, 44);
                }

                @Field(value=44)
                public __VARIANT_NAME_3_union puintVal(Pointer<Integer> pointer) {
                    this.io.setPointerField(this, 44, pointer);
                    return this;
                }

                public final Pointer<Integer> puintVal_$eq(Pointer<Integer> pointer) {
                    this.puintVal(pointer);
                    return pointer;
                }

                @Field(value=45)
                public __tagBRECORD __VARIANT_NAME_4() {
                    return (__tagBRECORD)this.io.getNativeObjectField(this, 45);
                }

                /*
                 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
                 */
                public static class __tagBRECORD
                extends StructObject {
                    @Field(value=0)
                    public Pointer<?> pvRecord() {
                        return this.io.getPointerField(this, 0);
                    }

                    @Field(value=0)
                    public __tagBRECORD pvRecord(Pointer<?> pointer) {
                        this.io.setPointerField(this, 0, pointer);
                        return this;
                    }

                    public final Pointer<?> pvRecord_$eq(Pointer<?> pointer) {
                        this.pvRecord(pointer);
                        return pointer;
                    }

                    @Field(value=1)
                    public Pointer<IRecordInfo> pRecInfo() {
                        return this.io.getPointerField(this, 1);
                    }

                    @Field(value=1)
                    public __tagBRECORD pRecInfo(Pointer<IRecordInfo> pointer) {
                        this.io.setPointerField(this, 1, pointer);
                        return this;
                    }

                    public final Pointer<IRecordInfo> pRecInfo_$eq(Pointer<IRecordInfo> pointer) {
                        this.pRecInfo(pointer);
                        return pointer;
                    }
                }
            }
        }
    }
}


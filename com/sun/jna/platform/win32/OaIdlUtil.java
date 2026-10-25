/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.platform.win32.WTypes;
import com.sun.jna.platform.win32.WinDef;
import java.lang.reflect.Array;

public abstract class OaIdlUtil {
    public static Object toPrimitiveArray(OaIdl.SAFEARRAY sAFEARRAY, boolean bl) {
        Object object = sAFEARRAY.accessData();
        try {
            int n2;
            int n3 = sAFEARRAY.getDimensionCount();
            int[] nArray = new int[n3];
            int[] nArray2 = new int[n3];
            int n4 = sAFEARRAY.getVarType().intValue();
            for (n2 = 0; n2 < n3; ++n2) {
                nArray[n2] = sAFEARRAY.getUBound(n2) - sAFEARRAY.getLBound(n2) + 1;
            }
            for (n2 = n3 - 1; n2 >= 0; --n2) {
                nArray2[n2] = n2 == n3 - 1 ? 1 : nArray2[n2 + 1] * nArray[n2 + 1];
            }
            if (n3 == 0) {
                throw new IllegalArgumentException("Supplied Array has no dimensions.");
            }
            n2 = nArray2[0] * nArray[0];
            switch (n4) {
                case 16: 
                case 17: {
                    object = object.getByteArray(0L, n2);
                    break;
                }
                case 2: 
                case 11: 
                case 18: {
                    object = object.getShortArray(0L, n2);
                    break;
                }
                case 3: 
                case 10: 
                case 19: 
                case 22: 
                case 23: {
                    object = object.getIntArray(0L, n2);
                    break;
                }
                case 4: {
                    object = object.getFloatArray(0L, n2);
                    break;
                }
                case 5: 
                case 7: {
                    object = object.getDoubleArray(0L, n2);
                    break;
                }
                case 8: {
                    object = object.getPointerArray(0L, n2);
                    break;
                }
                case 12: {
                    Variant.VARIANT vARIANT = new Variant.VARIANT((Pointer)object);
                    object = vARIANT.toArray(n2);
                    break;
                }
                default: {
                    throw new IllegalStateException("Type not supported: " + n4);
                }
            }
            Object object2 = Array.newInstance(Object.class, nArray);
            OaIdlUtil.toPrimitiveArray(object, object2, nArray, nArray2, n4, new int[0]);
            object = object2;
            return object;
        }
        finally {
            sAFEARRAY.unaccessData();
            if (bl) {
                sAFEARRAY.destroy();
            }
        }
    }

    private static void toPrimitiveArray(Object object, Object object2, int[] nArray, int[] nArray2, int n2, int[] nArray3) {
        int n3 = nArray3.length;
        int[] nArray4 = new int[nArray3.length + 1];
        System.arraycopy(nArray3, 0, nArray4, 0, n3);
        block24: for (int i2 = 0; i2 < nArray[n3]; ++i2) {
            nArray4[n3] = i2;
            if (n3 == nArray.length - 1) {
                int n4;
                int n5 = 0;
                for (n4 = 0; n4 < n3; ++n4) {
                    n5 += nArray2[n4] * nArray3[n4];
                }
                n5 += nArray4[n3];
                n4 = nArray4[n3];
                switch (n2) {
                    case 11: {
                        Array.set(object2, n4, Array.getShort(object, n5) != 0);
                        continue block24;
                    }
                    case 16: 
                    case 17: {
                        Array.set(object2, n4, Array.getByte(object, n5));
                        continue block24;
                    }
                    case 2: 
                    case 18: {
                        Array.set(object2, n4, Array.getShort(object, n5));
                        continue block24;
                    }
                    case 3: 
                    case 19: 
                    case 22: 
                    case 23: {
                        Array.set(object2, n4, Array.getInt(object, n5));
                        continue block24;
                    }
                    case 10: {
                        Array.set(object2, n4, new WinDef.SCODE((long)Array.getInt(object, n5)));
                        continue block24;
                    }
                    case 4: {
                        Array.set(object2, n4, Float.valueOf(Array.getFloat(object, n5)));
                        continue block24;
                    }
                    case 5: {
                        Array.set(object2, n4, Array.getDouble(object, n5));
                        continue block24;
                    }
                    case 7: {
                        Array.set(object2, n4, new OaIdl.DATE(Array.getDouble(object, n5)).getAsJavaDate());
                        continue block24;
                    }
                    case 8: {
                        Array.set(object2, n4, new WTypes.BSTR((Pointer)Array.get(object, n5)).getValue());
                        continue block24;
                    }
                    case 12: {
                        Variant.VARIANT vARIANT = (Variant.VARIANT)Array.get(object, n5);
                        switch (vARIANT.getVarType().intValue()) {
                            case 0: 
                            case 1: {
                                Array.set(object2, n4, null);
                                continue block24;
                            }
                            case 11: {
                                Array.set(object2, n4, vARIANT.booleanValue());
                                continue block24;
                            }
                            case 16: 
                            case 17: {
                                Array.set(object2, n4, vARIANT.byteValue());
                                continue block24;
                            }
                            case 2: 
                            case 18: {
                                Array.set(object2, n4, vARIANT.shortValue());
                                continue block24;
                            }
                            case 3: 
                            case 19: 
                            case 22: 
                            case 23: {
                                Array.set(object2, n4, vARIANT.intValue());
                                continue block24;
                            }
                            case 10: {
                                Array.set(object2, n4, new WinDef.SCODE((long)vARIANT.intValue()));
                                continue block24;
                            }
                            case 4: {
                                Array.set(object2, n4, Float.valueOf(vARIANT.floatValue()));
                                continue block24;
                            }
                            case 5: {
                                Array.set(object2, n4, vARIANT.doubleValue());
                                continue block24;
                            }
                            case 7: {
                                Array.set(object2, n4, vARIANT.dateValue());
                                continue block24;
                            }
                            case 8: {
                                Array.set(object2, n4, vARIANT.stringValue());
                                continue block24;
                            }
                            default: {
                                throw new IllegalStateException("Type not supported: " + vARIANT.getVarType().intValue());
                            }
                        }
                    }
                    default: {
                        throw new IllegalStateException("Type not supported: " + n2);
                    }
                }
            }
            OaIdlUtil.toPrimitiveArray(object, Array.get(object2, i2), nArray, nArray2, n2, nArray4);
        }
    }
}


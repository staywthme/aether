/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32.COM.util;

import com.sun.jna.platform.win32.COM.Dispatch;
import com.sun.jna.platform.win32.COM.IDispatch;
import com.sun.jna.platform.win32.COM.IUnknown;
import com.sun.jna.platform.win32.COM.Unknown;
import com.sun.jna.platform.win32.COM.util.IComEnum;
import com.sun.jna.platform.win32.COM.util.ObjectFactory;
import com.sun.jna.platform.win32.COM.util.ProxyObject;
import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.platform.win32.OleAuto;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.platform.win32.WTypes;
import com.sun.jna.platform.win32.WinDef;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.Date;

class Convert {
    Convert() {
    }

    public static Variant.VARIANT toVariant(Object object) {
        if (object instanceof Variant.VARIANT) {
            return (Variant.VARIANT)object;
        }
        if (object instanceof Byte) {
            return new Variant.VARIANT((Byte)object);
        }
        if (object instanceof Character) {
            return new Variant.VARIANT(((Character)object).charValue());
        }
        if (object instanceof Short) {
            return new Variant.VARIANT((Short)object);
        }
        if (object instanceof Integer) {
            return new Variant.VARIANT((Integer)object);
        }
        if (object instanceof Long) {
            return new Variant.VARIANT((Long)object);
        }
        if (object instanceof Float) {
            return new Variant.VARIANT(((Float)object).floatValue());
        }
        if (object instanceof Double) {
            return new Variant.VARIANT((Double)object);
        }
        if (object instanceof String) {
            return new Variant.VARIANT((String)object);
        }
        if (object instanceof Boolean) {
            return new Variant.VARIANT((Boolean)object);
        }
        if (object instanceof Dispatch) {
            return new Variant.VARIANT((Dispatch)object);
        }
        if (object instanceof Date) {
            return new Variant.VARIANT((Date)object);
        }
        if (object instanceof Proxy) {
            InvocationHandler invocationHandler = Proxy.getInvocationHandler(object);
            ProxyObject proxyObject = (ProxyObject)invocationHandler;
            return new Variant.VARIANT(proxyObject.getRawDispatch());
        }
        if (object instanceof IComEnum) {
            IComEnum iComEnum = (IComEnum)object;
            return new Variant.VARIANT(new WinDef.LONG(iComEnum.getValue()));
        }
        Constructor<?> constructor = null;
        if (object != null) {
            for (Constructor<?> constructor2 : Variant.VARIANT.class.getConstructors()) {
                Class<?>[] classArray = constructor2.getParameterTypes();
                if (classArray.length != 1 || !classArray[0].isAssignableFrom(object.getClass())) continue;
                constructor = constructor2;
            }
        }
        if (constructor != null) {
            try {
                return (Variant.VARIANT)constructor.newInstance(object);
            }
            catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }
        return null;
    }

    public static Object toJavaObject(Variant.VARIANT object, Class<?> clazz, ObjectFactory object2, boolean bl, boolean bl2) {
        Object object3;
        int n2 = object != null ? ((Variant.VARIANT)object).getVarType().intValue() : 1;
        if (n2 == 0 || n2 == 1) {
            return null;
        }
        if (clazz != null && !clazz.isAssignableFrom(Object.class)) {
            if (clazz.isAssignableFrom(object.getClass())) {
                return object;
            }
            object3 = ((Variant.VARIANT)object).getValue();
            if (object3 != null && clazz.isAssignableFrom(object3.getClass())) {
                return object3;
            }
        }
        object3 = object;
        if (n2 == 16396) {
            object = (Variant.VARIANT)((Variant.VARIANT)object).getValue();
            n2 = ((Variant.VARIANT)object).getVarType().intValue();
        }
        if (clazz == null || clazz.isAssignableFrom(Object.class)) {
            clazz = null;
            switch (n2) {
                case 16: 
                case 17: {
                    clazz = Byte.class;
                    break;
                }
                case 2: {
                    clazz = Short.class;
                    break;
                }
                case 18: {
                    clazz = Character.class;
                    break;
                }
                case 3: 
                case 19: 
                case 22: 
                case 23: {
                    clazz = Integer.class;
                    break;
                }
                case 20: 
                case 21: {
                    clazz = Long.class;
                    break;
                }
                case 4: {
                    clazz = Float.class;
                    break;
                }
                case 5: {
                    clazz = Double.class;
                    break;
                }
                case 11: {
                    clazz = Boolean.class;
                    break;
                }
                case 10: {
                    clazz = WinDef.SCODE.class;
                    break;
                }
                case 6: {
                    clazz = OaIdl.CURRENCY.class;
                    break;
                }
                case 7: {
                    clazz = Date.class;
                    break;
                }
                case 8: {
                    clazz = String.class;
                    break;
                }
                case 13: {
                    clazz = IUnknown.class;
                    break;
                }
                case 9: {
                    clazz = com.sun.jna.platform.win32.COM.util.IDispatch.class;
                    break;
                }
                case 16396: {
                    clazz = Variant.class;
                    break;
                }
                case 16384: {
                    clazz = WinDef.PVOID.class;
                    break;
                }
                case 16398: {
                    clazz = OaIdl.DECIMAL.class;
                    break;
                }
                default: {
                    if ((n2 & 0x2000) <= 0) break;
                    clazz = OaIdl.SAFEARRAY.class;
                }
            }
        }
        if (Byte.class.equals(clazz) || Byte.TYPE.equals(clazz)) {
            object = ((Variant.VARIANT)object).byteValue();
        } else if (Short.class.equals(clazz) || Short.TYPE.equals(clazz)) {
            object = ((Variant.VARIANT)object).shortValue();
        } else if (Character.class.equals(clazz) || Character.TYPE.equals(clazz)) {
            object = Character.valueOf((char)((Variant.VARIANT)object).intValue());
        } else if (Integer.class.equals(clazz) || Integer.TYPE.equals(clazz)) {
            object = ((Variant.VARIANT)object).intValue();
        } else if (Long.class.equals(clazz) || Long.TYPE.equals(clazz) || IComEnum.class.isAssignableFrom(clazz)) {
            object = ((Variant.VARIANT)object).longValue();
        } else if (Float.class.equals(clazz) || Float.TYPE.equals(clazz)) {
            object = Float.valueOf(((Variant.VARIANT)object).floatValue());
        } else if (Double.class.equals(clazz) || Double.TYPE.equals(clazz)) {
            object = ((Variant.VARIANT)object).doubleValue();
        } else if (Boolean.class.equals(clazz) || Boolean.TYPE.equals(clazz)) {
            object = ((Variant.VARIANT)object).booleanValue();
        } else if (Date.class.equals(clazz)) {
            object = ((Variant.VARIANT)object).dateValue();
        } else if (String.class.equals(clazz)) {
            object = ((Variant.VARIANT)object).stringValue();
        } else if ((object = ((Variant.VARIANT)object).getValue()) instanceof Dispatch) {
            object = (Dispatch)object;
            if (clazz != null && clazz.isInterface()) {
                object2 = ((ObjectFactory)object2).createProxy(clazz, (IDispatch)object);
                if (!bl) {
                    ((Unknown)object).Release();
                }
                object = object2;
            }
        }
        if (IComEnum.class.isAssignableFrom(clazz)) {
            Class<Object> clazz2 = clazz;
            object = clazz2.cast(Convert.toComEnum(clazz2, object));
        }
        if (bl2) {
            Convert.free((Variant.VARIANT)object3, object);
        }
        return object;
    }

    public static <T extends IComEnum> T toComEnum(Class<T> iComEnumArray, Object object) {
        try {
            iComEnumArray = iComEnumArray.getMethod("values", new Class[0]);
            IComEnum[] iComEnumArray2 = (IComEnum[])iComEnumArray.invoke(null, new Object[0]);
            iComEnumArray = iComEnumArray2;
            iComEnumArray = iComEnumArray2;
            int n2 = iComEnumArray2.length;
            for (int i2 = 0; i2 < n2; ++i2) {
                IComEnum iComEnum = iComEnumArray[i2];
                if (!object.equals(iComEnum.getValue())) continue;
                return (T)iComEnum;
            }
        }
        catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | InvocationTargetException exception) {}
        return null;
    }

    public static void free(Variant.VARIANT object, Class<?> clazz) {
        if ((clazz == null || !WTypes.BSTR.class.isAssignableFrom(clazz)) && object != null && ((Variant.VARIANT)object).getVarType().intValue() == 8 && (object = ((Variant.VARIANT)object).getValue()) instanceof WTypes.BSTR) {
            OleAuto.INSTANCE.SysFreeString((WTypes.BSTR)object);
        }
    }

    public static void free(Variant.VARIANT vARIANT, Object object) {
        Convert.free(vARIANT, object == null ? null : object.getClass());
    }
}


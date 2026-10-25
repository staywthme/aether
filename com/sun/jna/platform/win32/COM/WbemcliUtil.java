/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32.COM;

import com.sun.jna.IntegerType;
import com.sun.jna.NativeMapped;
import com.sun.jna.Pointer;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.COM.COMException;
import com.sun.jna.platform.win32.COM.COMUtils;
import com.sun.jna.platform.win32.COM.Unknown;
import com.sun.jna.platform.win32.COM.Wbemcli;
import com.sun.jna.platform.win32.Ole32;
import com.sun.jna.platform.win32.OleAuto;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class WbemcliUtil {
    public static final WbemcliUtil INSTANCE = new WbemcliUtil();
    public static final String DEFAULT_NAMESPACE = "ROOT\\CIMV2";

    public static boolean hasNamespace(String wmiResult) {
        String string = wmiResult;
        if (((String)((Object)wmiResult)).toUpperCase().startsWith("ROOT\\")) {
            string = ((String)((Object)wmiResult)).substring(5);
        }
        wmiResult = new WmiQuery<NamespaceProperty>("ROOT", "__NAMESPACE", NamespaceProperty.class);
        wmiResult = ((WmiQuery)((Object)wmiResult)).execute();
        for (int i2 = 0; i2 < wmiResult.getResultCount(); ++i2) {
            if (!string.equalsIgnoreCase((String)wmiResult.getValue(NamespaceProperty.NAME, i2))) continue;
            return true;
        }
        return false;
    }

    public static Wbemcli.IWbemServices connectServer(String object) {
        NativeMapped nativeMapped = Wbemcli.IWbemLocator.create();
        if (nativeMapped == null) {
            throw new COMException("Failed to create WbemLocator object.");
        }
        object = nativeMapped.ConnectServer((String)object, null, null, null, 0, null, null);
        nativeMapped.Release();
        nativeMapped = Ole32.INSTANCE.CoSetProxyBlanket((Unknown)object, 10, 0, null, 3, 3, null, 0);
        if (COMUtils.FAILED((WinNT.HRESULT)nativeMapped)) {
            ((Unknown)object).Release();
            throw new COMException("Could not set proxy blanket.", (WinNT.HRESULT)nativeMapped);
        }
        return object;
    }

    public class WmiResult<T extends Enum<T>> {
        private Map<T, List<Object>> propertyMap;
        private Map<T, Integer> vtTypeMap;
        private Map<T, Integer> cimTypeMap;
        private int resultCount = 0;

        public WmiResult(Class<T> clazz) {
            this.propertyMap = new EnumMap<T, List<Object>>(clazz);
            this.vtTypeMap = new EnumMap<T, Integer>(clazz);
            this.cimTypeMap = new EnumMap<T, Integer>(clazz);
            for (Enum enum_ : (Enum[])clazz.getEnumConstants()) {
                this.propertyMap.put(enum_, new ArrayList());
                this.vtTypeMap.put(enum_, 1);
                this.cimTypeMap.put(enum_, 0);
            }
        }

        public Object getValue(T t2, int n2) {
            return this.propertyMap.get(t2).get(n2);
        }

        public int getVtType(T t2) {
            return this.vtTypeMap.get(t2);
        }

        public int getCIMType(T t2) {
            return this.cimTypeMap.get(t2);
        }

        private void add(int n2, int n3, T t2, Object object) {
            this.propertyMap.get(t2).add(object);
            if (n2 != 1 && this.vtTypeMap.get(t2).equals(1)) {
                this.vtTypeMap.put(t2, n2);
            }
            if (this.cimTypeMap.get(t2).equals(0)) {
                this.cimTypeMap.put(t2, n3);
            }
        }

        public int getResultCount() {
            return this.resultCount;
        }

        private void incrementResultCount() {
            ++this.resultCount;
        }
    }

    public static class WmiQuery<T extends Enum<T>> {
        private String nameSpace;
        private String wmiClassName;
        private Class<T> propertyEnum;

        public WmiQuery(String string, String string2, Class<T> clazz) {
            this.nameSpace = string;
            this.wmiClassName = string2;
            this.propertyEnum = clazz;
        }

        public WmiQuery(String string, Class<T> clazz) {
            this(WbemcliUtil.DEFAULT_NAMESPACE, string, clazz);
        }

        public Class<T> getPropertyEnum() {
            return this.propertyEnum;
        }

        public String getNameSpace() {
            return this.nameSpace;
        }

        public void setNameSpace(String string) {
            this.nameSpace = string;
        }

        public String getWmiClassName() {
            return this.wmiClassName;
        }

        public void setWmiClassName(String string) {
            this.wmiClassName = string;
        }

        public WmiResult<T> execute() {
            try {
                return this.execute(-1);
            }
            catch (TimeoutException timeoutException) {
                throw new COMException("Got a WMI timeout when infinite wait was specified. This should never happen.");
            }
        }

        public WmiResult<T> execute(int n2) throws TimeoutException {
            if (((Enum[])this.getPropertyEnum().getEnumConstants()).length <= 0) {
                throw new IllegalArgumentException("The query's property enum has no values.");
            }
            Wbemcli.IWbemServices iWbemServices = WbemcliUtil.connectServer(this.getNameSpace());
            try {
                WmiResult<T> wmiResult;
                Wbemcli.IEnumWbemClassObject iEnumWbemClassObject = WmiQuery.selectProperties(iWbemServices, this);
                try {
                    wmiResult = WmiQuery.enumerateProperties(iEnumWbemClassObject, this.getPropertyEnum(), n2);
                    iEnumWbemClassObject.Release();
                }
                catch (Throwable throwable) {
                    iEnumWbemClassObject.Release();
                    throw throwable;
                }
                return wmiResult;
            }
            finally {
                iWbemServices.Release();
            }
        }

        private static <T extends Enum<T>> Wbemcli.IEnumWbemClassObject selectProperties(Wbemcli.IWbemServices iWbemServices, WmiQuery<T> wmiQuery) {
            Enum[] enumArray = (Enum[])wmiQuery.getPropertyEnum().getEnumConstants();
            StringBuilder stringBuilder = new StringBuilder("SELECT ");
            stringBuilder.append(enumArray[0].name());
            for (int i2 = 1; i2 < enumArray.length; ++i2) {
                stringBuilder.append(',').append(enumArray[i2].name());
            }
            stringBuilder.append(" FROM ").append(wmiQuery.getWmiClassName());
            return iWbemServices.ExecQuery("WQL", stringBuilder.toString().replaceAll("\\\\", "\\\\\\\\"), 48, null);
        }

        private static <T extends Enum<T>> WmiResult<T> enumerateProperties(Wbemcli.IEnumWbemClassObject iEnumWbemClassObject, Class<T> clazz, int n2) throws TimeoutException {
            WbemcliUtil wbemcliUtil = INSTANCE;
            wbemcliUtil.getClass();
            WmiResult<T> wmiResult = wbemcliUtil.new WmiResult<T>(clazz);
            Pointer[] pointerArray = new Pointer[1];
            IntByReference intByReference = new IntByReference(0);
            HashMap<Enum[], WString> hashMap = new HashMap<Enum[], WString>();
            Object object = (Enum[])clazz.getEnumConstants();
            int n3 = ((Enum[])object).length;
            for (int i2 = 0; i2 < n3; ++i2) {
                Enum[] enumArray = object[i2];
                hashMap.put(enumArray, new WString(enumArray.name()));
            }
            while (iEnumWbemClassObject.getPointer() != Pointer.NULL && ((IntegerType)(object = iEnumWbemClassObject.Next(n2, pointerArray.length, pointerArray, intByReference))).intValue() != 1 && ((IntegerType)object).intValue() != 262149) {
                if (((IntegerType)object).intValue() == 262148) {
                    throw new TimeoutException("No results after " + n2 + " ms.");
                }
                if (COMUtils.FAILED((WinNT.HRESULT)object)) {
                    throw new COMException("Failed to enumerate results.", (WinNT.HRESULT)object);
                }
                object = new Variant.VARIANT.ByReference();
                IntByReference intByReference2 = new IntByReference();
                Wbemcli.IWbemClassObject iWbemClassObject = new Wbemcli.IWbemClassObject(pointerArray[0]);
                for (Enum enum_ : (Enum[])clazz.getEnumConstants()) {
                    iWbemClassObject.Get((WString)hashMap.get(enum_), 0, (Variant.VARIANT.ByReference)object, intByReference2, null);
                    int n4 = (((Variant.VARIANT)object).getValue() == null ? Integer.valueOf(1) : ((Variant.VARIANT)object).getVarType()).intValue();
                    int n5 = intByReference2.getValue();
                    switch (n4) {
                        case 8: {
                            ((WmiResult)wmiResult).add(n4, n5, enum_, ((Variant.VARIANT)object).stringValue());
                            break;
                        }
                        case 3: {
                            ((WmiResult)wmiResult).add(n4, n5, enum_, ((Variant.VARIANT)object).intValue());
                            break;
                        }
                        case 17: {
                            ((WmiResult)wmiResult).add(n4, n5, enum_, ((Variant.VARIANT)object).byteValue());
                            break;
                        }
                        case 2: {
                            ((WmiResult)wmiResult).add(n4, n5, enum_, ((Variant.VARIANT)object).shortValue());
                            break;
                        }
                        case 11: {
                            ((WmiResult)wmiResult).add(n4, n5, enum_, ((Variant.VARIANT)object).booleanValue());
                            break;
                        }
                        case 4: {
                            ((WmiResult)wmiResult).add(n4, n5, enum_, Float.valueOf(((Variant.VARIANT)object).floatValue()));
                            break;
                        }
                        case 5: {
                            ((WmiResult)wmiResult).add(n4, n5, enum_, ((Variant.VARIANT)object).doubleValue());
                            break;
                        }
                        case 0: 
                        case 1: {
                            ((WmiResult)wmiResult).add(n4, n5, enum_, null);
                            break;
                        }
                        default: {
                            if ((n4 & 0x2000) == 8192 || (n4 & 0xD) == 13 || (n4 & 9) == 9 || (n4 & 0x1000) == 4096) {
                                ((WmiResult)wmiResult).add(n4, n5, enum_, null);
                                break;
                            }
                            ((WmiResult)wmiResult).add(n4, n5, enum_, ((Variant.VARIANT)object).getValue());
                        }
                    }
                    OleAuto.INSTANCE.VariantClear((Variant.VARIANT)object);
                }
                iWbemClassObject.Release();
                ((WmiResult)wmiResult).incrementResultCount();
            }
            return wmiResult;
        }
    }

    static enum NamespaceProperty {
        NAME;

    }
}


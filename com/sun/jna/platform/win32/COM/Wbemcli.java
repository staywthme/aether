/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32.COM;

import com.sun.jna.IntegerType;
import com.sun.jna.NativeMapped;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.COM.COMUtils;
import com.sun.jna.platform.win32.COM.Unknown;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.platform.win32.OaIdlUtil;
import com.sun.jna.platform.win32.Ole32;
import com.sun.jna.platform.win32.OleAuto;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.platform.win32.WTypes;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

public interface Wbemcli {
    public static final int WBEM_FLAG_RETURN_WBEM_COMPLETE = 0;
    public static final int WBEM_FLAG_RETURN_IMMEDIATELY = 16;
    public static final int WBEM_FLAG_FORWARD_ONLY = 32;
    public static final int WBEM_FLAG_NO_ERROR_OBJECT = 64;
    public static final int WBEM_FLAG_SEND_STATUS = 128;
    public static final int WBEM_FLAG_ENSURE_LOCATABLE = 256;
    public static final int WBEM_FLAG_DIRECT_READ = 512;
    public static final int WBEM_MASK_RESERVED_FLAGS = 126976;
    public static final int WBEM_FLAG_USE_AMENDED_QUALIFIERS = 131072;
    public static final int WBEM_FLAG_STRONG_VALIDATION = 0x100000;
    public static final int WBEM_INFINITE = -1;
    public static final int WBEM_S_NO_ERROR = 0;
    public static final int WBEM_S_FALSE = 1;
    public static final int WBEM_S_TIMEDOUT = 262148;
    public static final int WBEM_S_NO_MORE_DATA = 262149;
    public static final int WBEM_E_INVALID_NAMESPACE = -2147217394;
    public static final int WBEM_E_INVALID_CLASS = -2147217392;
    public static final int WBEM_E_INVALID_QUERY = -2147217385;
    public static final int CIM_ILLEGAL = 4095;
    public static final int CIM_EMPTY = 0;
    public static final int CIM_SINT8 = 16;
    public static final int CIM_UINT8 = 17;
    public static final int CIM_SINT16 = 2;
    public static final int CIM_UINT16 = 18;
    public static final int CIM_SINT32 = 3;
    public static final int CIM_UINT32 = 19;
    public static final int CIM_SINT64 = 20;
    public static final int CIM_UINT64 = 21;
    public static final int CIM_REAL32 = 4;
    public static final int CIM_REAL64 = 5;
    public static final int CIM_BOOLEAN = 11;
    public static final int CIM_STRING = 8;
    public static final int CIM_DATETIME = 101;
    public static final int CIM_REFERENCE = 102;
    public static final int CIM_CHAR16 = 103;
    public static final int CIM_OBJECT = 13;
    public static final int CIM_FLAG_ARRAY = 8192;

    public static class IWbemContext
    extends Unknown {
        public static final Guid.CLSID CLSID_WbemContext = new Guid.CLSID("674B6698-EE92-11D0-AD71-00C04FD8FDFF");
        public static final Guid.GUID IID_IWbemContext = new Guid.GUID("44aca674-e8fc-11d0-a07c-00c04fb68820");

        public IWbemContext() {
        }

        public static IWbemContext create() {
            PointerByReference pointerByReference = new PointerByReference();
            WinNT.HRESULT hRESULT = Ole32.INSTANCE.CoCreateInstance(CLSID_WbemContext, null, 1, IID_IWbemContext, pointerByReference);
            if (COMUtils.FAILED(hRESULT)) {
                return null;
            }
            return new IWbemContext(pointerByReference.getValue());
        }

        public IWbemContext(Pointer pointer) {
            super(pointer);
        }

        public void SetValue(String object, int n2, Variant.VARIANT vARIANT) {
            object = OleAuto.INSTANCE.SysAllocString((String)object);
            try {
                WinNT.HRESULT hRESULT = (WinNT.HRESULT)this._invokeNativeObject(8, new Object[]{this.getPointer(), object, n2, vARIANT}, WinNT.HRESULT.class);
                COMUtils.checkRC(hRESULT);
                return;
            }
            finally {
                OleAuto.INSTANCE.SysFreeString((WTypes.BSTR)object);
            }
        }

        public void SetValue(String string, int n2, boolean bl) {
            Variant.VARIANT vARIANT = new Variant.VARIANT();
            vARIANT.setValue(11, (Object)(bl ? Variant.VARIANT_TRUE : Variant.VARIANT_FALSE));
            this.SetValue(string, n2, vARIANT);
            OleAuto.INSTANCE.VariantClear(vARIANT);
        }

        public void SetValue(String string, int n2, String object) {
            Variant.VARIANT vARIANT = new Variant.VARIANT();
            object = OleAuto.INSTANCE.SysAllocString((String)object);
            try {
                vARIANT.setValue(30, object);
                this.SetValue(string, n2, vARIANT);
                return;
            }
            finally {
                OleAuto.INSTANCE.SysFreeString((WTypes.BSTR)object);
            }
        }
    }

    public static class IWbemServices
    extends Unknown {
        public IWbemServices() {
        }

        public IWbemServices(Pointer pointer) {
            super(pointer);
        }

        public WinNT.HRESULT ExecMethod(WTypes.BSTR bSTR, WTypes.BSTR bSTR2, int n2, IWbemContext iWbemContext, Pointer pointer, PointerByReference pointerByReference, PointerByReference pointerByReference2) {
            return (WinNT.HRESULT)this._invokeNativeObject(24, new Object[]{this.getPointer(), bSTR, bSTR2, n2, iWbemContext, pointer, pointerByReference, pointerByReference2}, WinNT.HRESULT.class);
        }

        public IWbemClassObject ExecMethod(String object, String object2, int n2, IWbemContext iWbemContext, IWbemClassObject iWbemClassObject) {
            object = OleAuto.INSTANCE.SysAllocString((String)object);
            object2 = OleAuto.INSTANCE.SysAllocString((String)object2);
            try {
                PointerByReference pointerByReference = new PointerByReference();
                NativeMapped nativeMapped = this.ExecMethod((WTypes.BSTR)object, (WTypes.BSTR)object2, n2, iWbemContext, iWbemClassObject.getPointer(), pointerByReference, null);
                COMUtils.checkRC(nativeMapped);
                nativeMapped = new IWbemClassObject(pointerByReference.getValue());
                return nativeMapped;
            }
            finally {
                OleAuto.INSTANCE.SysFreeString((WTypes.BSTR)object);
                OleAuto.INSTANCE.SysFreeString((WTypes.BSTR)object2);
            }
        }

        public WinNT.HRESULT ExecQuery(WTypes.BSTR bSTR, WTypes.BSTR bSTR2, int n2, IWbemContext iWbemContext, PointerByReference pointerByReference) {
            return (WinNT.HRESULT)this._invokeNativeObject(20, new Object[]{this.getPointer(), bSTR, bSTR2, n2, iWbemContext, pointerByReference}, WinNT.HRESULT.class);
        }

        public IEnumWbemClassObject ExecQuery(String object, String object2, int n2, IWbemContext iWbemContext) {
            object = OleAuto.INSTANCE.SysAllocString((String)object);
            object2 = OleAuto.INSTANCE.SysAllocString((String)object2);
            try {
                PointerByReference pointerByReference = new PointerByReference();
                NativeMapped nativeMapped = this.ExecQuery((WTypes.BSTR)object, (WTypes.BSTR)object2, n2, iWbemContext, pointerByReference);
                COMUtils.checkRC(nativeMapped);
                nativeMapped = new IEnumWbemClassObject(pointerByReference.getValue());
                return nativeMapped;
            }
            finally {
                OleAuto.INSTANCE.SysFreeString((WTypes.BSTR)object);
                OleAuto.INSTANCE.SysFreeString((WTypes.BSTR)object2);
            }
        }

        public WinNT.HRESULT GetObject(WTypes.BSTR bSTR, int n2, IWbemContext iWbemContext, PointerByReference pointerByReference, PointerByReference pointerByReference2) {
            return (WinNT.HRESULT)this._invokeNativeObject(6, new Object[]{this.getPointer(), bSTR, n2, iWbemContext, pointerByReference, pointerByReference2}, WinNT.HRESULT.class);
        }

        public IWbemClassObject GetObject(String object, int n2, IWbemContext iWbemContext) {
            object = OleAuto.INSTANCE.SysAllocString((String)object);
            try {
                PointerByReference pointerByReference = new PointerByReference();
                NativeMapped nativeMapped = this.GetObject((WTypes.BSTR)object, n2, iWbemContext, pointerByReference, null);
                COMUtils.checkRC(nativeMapped);
                nativeMapped = new IWbemClassObject(pointerByReference.getValue());
                return nativeMapped;
            }
            finally {
                OleAuto.INSTANCE.SysFreeString((WTypes.BSTR)object);
            }
        }
    }

    public static class IWbemLocator
    extends Unknown {
        public static final Guid.CLSID CLSID_WbemLocator = new Guid.CLSID("4590f811-1d3a-11d0-891f-00aa004b2e24");
        public static final Guid.GUID IID_IWbemLocator = new Guid.GUID("dc12a687-737f-11cf-884d-00aa004b2e24");

        public IWbemLocator() {
        }

        private IWbemLocator(Pointer pointer) {
            super(pointer);
        }

        public static IWbemLocator create() {
            PointerByReference pointerByReference = new PointerByReference();
            WinNT.HRESULT hRESULT = Ole32.INSTANCE.CoCreateInstance(CLSID_WbemLocator, null, 1, IID_IWbemLocator, pointerByReference);
            if (COMUtils.FAILED(hRESULT)) {
                return null;
            }
            return new IWbemLocator(pointerByReference.getValue());
        }

        public WinNT.HRESULT ConnectServer(WTypes.BSTR bSTR, WTypes.BSTR bSTR2, WTypes.BSTR bSTR3, WTypes.BSTR bSTR4, int n2, WTypes.BSTR bSTR5, IWbemContext iWbemContext, PointerByReference pointerByReference) {
            return (WinNT.HRESULT)this._invokeNativeObject(3, new Object[]{this.getPointer(), bSTR, bSTR2, bSTR3, bSTR4, n2, bSTR5, iWbemContext, pointerByReference}, WinNT.HRESULT.class);
        }

        public IWbemServices ConnectServer(String object, String object2, String object3, String object4, int n2, String object5, IWbemContext iWbemContext) {
            object = OleAuto.INSTANCE.SysAllocString((String)object);
            object2 = OleAuto.INSTANCE.SysAllocString((String)object2);
            object3 = OleAuto.INSTANCE.SysAllocString((String)object3);
            object4 = OleAuto.INSTANCE.SysAllocString((String)object4);
            object5 = OleAuto.INSTANCE.SysAllocString((String)object5);
            PointerByReference pointerByReference = new PointerByReference();
            try {
                NativeMapped nativeMapped = this.ConnectServer((WTypes.BSTR)object, (WTypes.BSTR)object2, (WTypes.BSTR)object3, (WTypes.BSTR)object4, n2, (WTypes.BSTR)object5, iWbemContext, pointerByReference);
                COMUtils.checkRC(nativeMapped);
                nativeMapped = new IWbemServices(pointerByReference.getValue());
                return nativeMapped;
            }
            finally {
                OleAuto.INSTANCE.SysFreeString((WTypes.BSTR)object);
                OleAuto.INSTANCE.SysFreeString((WTypes.BSTR)object2);
                OleAuto.INSTANCE.SysFreeString((WTypes.BSTR)object3);
                OleAuto.INSTANCE.SysFreeString((WTypes.BSTR)object4);
                OleAuto.INSTANCE.SysFreeString((WTypes.BSTR)object5);
            }
        }
    }

    public static class IEnumWbemClassObject
    extends Unknown {
        public IEnumWbemClassObject() {
        }

        public IEnumWbemClassObject(Pointer pointer) {
            super(pointer);
        }

        public WinNT.HRESULT Next(int n2, int n3, Pointer[] pointerArray, IntByReference intByReference) {
            return (WinNT.HRESULT)this._invokeNativeObject(4, new Object[]{this.getPointer(), n2, n3, pointerArray, intByReference}, WinNT.HRESULT.class);
        }

        public IWbemClassObject[] Next(int n2, int n3) {
            Pointer[] pointerArray = new Pointer[n3];
            IntByReference intByReference = new IntByReference();
            IWbemClassObject[] iWbemClassObjectArray = this.Next(n2, n3, pointerArray, intByReference);
            COMUtils.checkRC((WinNT.HRESULT)iWbemClassObjectArray);
            iWbemClassObjectArray = new IWbemClassObject[intByReference.getValue()];
            for (n3 = 0; n3 < intByReference.getValue(); ++n3) {
                iWbemClassObjectArray[n3] = new IWbemClassObject(pointerArray[n3]);
            }
            return iWbemClassObjectArray;
        }
    }

    public static class IWbemQualifierSet
    extends Unknown {
        public IWbemQualifierSet(Pointer pointer) {
            super(pointer);
        }

        public WinNT.HRESULT Get(WString wString, int n2, Variant.VARIANT.ByReference byReference, IntByReference intByReference) {
            return (WinNT.HRESULT)this._invokeNativeObject(3, new Object[]{this.getPointer(), wString, n2, byReference, intByReference}, WinNT.HRESULT.class);
        }

        public String Get(String object) {
            object = new WString((String)object);
            Variant.VARIANT.ByReference byReference = new Variant.VARIANT.ByReference();
            if (((IntegerType)(object = this.Get((WString)object, 0, byReference, null))).intValue() == -2147217406) {
                return null;
            }
            int n2 = byReference.getVarType().intValue();
            switch (n2) {
                case 11: {
                    return String.valueOf(byReference.booleanValue());
                }
                case 8: {
                    return byReference.stringValue();
                }
            }
            return null;
        }

        public WinNT.HRESULT GetNames(int n2, PointerByReference pointerByReference) {
            return (WinNT.HRESULT)this._invokeNativeObject(6, new Object[]{this.getPointer(), n2, pointerByReference}, WinNT.HRESULT.class);
        }

        public String[] GetNames() {
            Object[] objectArray = new PointerByReference();
            COMUtils.checkRC(this.GetNames(0, (PointerByReference)objectArray));
            objectArray = (Object[])OaIdlUtil.toPrimitiveArray(new OaIdl.SAFEARRAY(objectArray.getValue()), true);
            String[] stringArray = new String[objectArray.length];
            for (int i2 = 0; i2 < objectArray.length; ++i2) {
                stringArray[i2] = (String)objectArray[i2];
            }
            return stringArray;
        }
    }

    public static class IWbemClassObject
    extends Unknown {
        public IWbemClassObject() {
        }

        public IWbemClassObject(Pointer pointer) {
            super(pointer);
        }

        public WinNT.HRESULT Get(WString wString, int n2, Variant.VARIANT.ByReference byReference, IntByReference intByReference, IntByReference intByReference2) {
            return (WinNT.HRESULT)this._invokeNativeObject(4, new Object[]{this.getPointer(), wString, n2, byReference, intByReference, intByReference2}, WinNT.HRESULT.class);
        }

        public WinNT.HRESULT Get(String string, int n2, Variant.VARIANT.ByReference byReference, IntByReference intByReference, IntByReference intByReference2) {
            return this.Get(string == null ? null : new WString(string), n2, byReference, intByReference, intByReference2);
        }

        public WinNT.HRESULT GetMethod(String string, int n2, PointerByReference pointerByReference, PointerByReference pointerByReference2) {
            return this.GetMethod(string == null ? null : new WString(string), n2, pointerByReference, pointerByReference2);
        }

        public WinNT.HRESULT GetMethod(WString wString, int n2, PointerByReference pointerByReference, PointerByReference pointerByReference2) {
            return (WinNT.HRESULT)this._invokeNativeObject(19, new Object[]{this.getPointer(), wString, n2, pointerByReference, pointerByReference2}, WinNT.HRESULT.class);
        }

        public IWbemClassObject GetMethod(String object) {
            PointerByReference pointerByReference = new PointerByReference();
            object = this.GetMethod((String)object, 0, pointerByReference, null);
            COMUtils.checkRC((WinNT.HRESULT)object);
            return new IWbemClassObject(pointerByReference.getValue());
        }

        public WinNT.HRESULT GetNames(String string, int n2, Variant.VARIANT.ByReference byReference, PointerByReference pointerByReference) {
            return this.GetNames(string == null ? null : new WString(string), n2, byReference, pointerByReference);
        }

        public WinNT.HRESULT GetNames(WString wString, int n2, Variant.VARIANT.ByReference byReference, PointerByReference pointerByReference) {
            return (WinNT.HRESULT)this._invokeNativeObject(7, new Object[]{this.getPointer(), wString, n2, byReference, pointerByReference}, WinNT.HRESULT.class);
        }

        public String[] GetNames(String objectArray, int n2, Variant.VARIANT.ByReference byReference) {
            PointerByReference pointerByReference = new PointerByReference();
            COMUtils.checkRC(this.GetNames((String)objectArray, n2, byReference, pointerByReference));
            objectArray = (Object[])OaIdlUtil.toPrimitiveArray(new OaIdl.SAFEARRAY(pointerByReference.getValue()), true);
            String[] stringArray = new String[objectArray.length];
            for (int i2 = 0; i2 < objectArray.length; ++i2) {
                stringArray[i2] = (String)objectArray[i2];
            }
            return stringArray;
        }

        public WinNT.HRESULT GetQualifierSet(PointerByReference pointerByReference) {
            return (WinNT.HRESULT)this._invokeNativeObject(3, new Object[]{this.getPointer(), pointerByReference}, WinNT.HRESULT.class);
        }

        public IWbemQualifierSet GetQualifierSet() {
            PointerType pointerType = new PointerByReference();
            WinNT.HRESULT hRESULT = this.GetQualifierSet((PointerByReference)pointerType);
            COMUtils.checkRC(hRESULT);
            pointerType = new IWbemQualifierSet(pointerType.getValue());
            return pointerType;
        }

        public WinNT.HRESULT GetPropertyQualifierSet(WString wString, PointerByReference pointerByReference) {
            return (WinNT.HRESULT)this._invokeNativeObject(11, new Object[]{this.getPointer(), wString, pointerByReference}, WinNT.HRESULT.class);
        }

        public IWbemQualifierSet GetPropertyQualifierSet(String object) {
            object = new WString((String)object);
            PointerByReference pointerByReference = new PointerByReference();
            COMUtils.checkRC(this.GetPropertyQualifierSet((WString)object, pointerByReference));
            object = new IWbemQualifierSet(pointerByReference.getValue());
            return object;
        }

        public WinNT.HRESULT Put(String string, int n2, Variant.VARIANT vARIANT, int n3) {
            return this.Put(string == null ? null : new WString(string), n2, vARIANT, n3);
        }

        public WinNT.HRESULT Put(WString wString, int n2, Variant.VARIANT vARIANT, int n3) {
            return (WinNT.HRESULT)this._invokeNativeObject(5, new Object[]{this.getPointer(), wString, n2, vARIANT, n3}, WinNT.HRESULT.class);
        }

        public void Put(String object, String object2) {
            Variant.VARIANT vARIANT = new Variant.VARIANT();
            object2 = OleAuto.INSTANCE.SysAllocString((String)object2);
            try {
                vARIANT.setValue(8, object2);
                object = this.Put((String)object, 0, vARIANT, 0);
                COMUtils.checkRC((WinNT.HRESULT)object);
                return;
            }
            finally {
                OleAuto.INSTANCE.VariantClear(vARIANT);
            }
        }

        public WinNT.HRESULT SpawnInstance(int n2, PointerByReference pointerByReference) {
            return (WinNT.HRESULT)this._invokeNativeObject(15, new Object[]{this.getPointer(), n2, pointerByReference}, WinNT.HRESULT.class);
        }

        public IWbemClassObject SpawnInstance() {
            PointerByReference pointerByReference = new PointerByReference();
            WinNT.HRESULT hRESULT = this.SpawnInstance(0, pointerByReference);
            COMUtils.checkRC(hRESULT);
            return new IWbemClassObject(pointerByReference.getValue());
        }
    }

    public static interface WBEM_CONDITION_FLAG_TYPE {
        public static final int WBEM_FLAG_ALWAYS = 0;
        public static final int WBEM_FLAG_ONLY_IF_TRUE = 1;
        public static final int WBEM_FLAG_ONLY_IF_FALSE = 2;
        public static final int WBEM_FLAG_ONLY_IF_IDENTICAL = 3;
        public static final int WBEM_MASK_PRIMARY_CONDITION = 3;
        public static final int WBEM_FLAG_KEYS_ONLY = 4;
        public static final int WBEM_FLAG_REFS_ONLY = 8;
        public static final int WBEM_FLAG_LOCAL_ONLY = 16;
        public static final int WBEM_FLAG_PROPAGATED_ONLY = 32;
        public static final int WBEM_FLAG_SYSTEM_ONLY = 48;
        public static final int WBEM_FLAG_NONSYSTEM_ONLY = 64;
        public static final int WBEM_MASK_CONDITION_ORIGIN = 112;
        public static final int WBEM_FLAG_CLASS_OVERRIDES_ONLY = 256;
        public static final int WBEM_FLAG_CLASS_LOCAL_AND_OVERRIDES = 512;
        public static final int WBEM_MASK_CLASS_CONDITION = 768;
    }
}


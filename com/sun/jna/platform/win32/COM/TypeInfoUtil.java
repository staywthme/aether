/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32.COM;

import com.sun.jna.platform.win32.COM.COMUtils;
import com.sun.jna.platform.win32.COM.ITypeInfo;
import com.sun.jna.platform.win32.COM.ITypeLib;
import com.sun.jna.platform.win32.COM.IUnknown;
import com.sun.jna.platform.win32.COM.TypeComp;
import com.sun.jna.platform.win32.COM.TypeInfo;
import com.sun.jna.platform.win32.COM.TypeLib;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.platform.win32.OleAuto;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.platform.win32.WTypes;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

public class TypeInfoUtil {
    public static final OleAuto OLEAUTO = OleAuto.INSTANCE;
    private ITypeInfo typeInfo;

    public TypeInfoUtil(ITypeInfo iTypeInfo) {
        this.typeInfo = iTypeInfo;
    }

    public OaIdl.TYPEATTR getTypeAttr() {
        PointerByReference pointerByReference = new PointerByReference();
        WinNT.HRESULT hRESULT = this.typeInfo.GetTypeAttr(pointerByReference);
        COMUtils.checkRC(hRESULT);
        return new OaIdl.TYPEATTR(pointerByReference.getValue());
    }

    public TypeComp getTypeComp() {
        PointerByReference pointerByReference = new PointerByReference();
        WinNT.HRESULT hRESULT = this.typeInfo.GetTypeComp(pointerByReference);
        COMUtils.checkRC(hRESULT);
        return new TypeComp(pointerByReference.getValue());
    }

    public OaIdl.FUNCDESC getFuncDesc(int n2) {
        PointerByReference pointerByReference = new PointerByReference();
        WinNT.HRESULT hRESULT = this.typeInfo.GetFuncDesc(new WinDef.UINT((long)n2), pointerByReference);
        COMUtils.checkRC(hRESULT);
        return new OaIdl.FUNCDESC(pointerByReference.getValue());
    }

    public OaIdl.VARDESC getVarDesc(int n2) {
        PointerByReference pointerByReference = new PointerByReference();
        WinNT.HRESULT hRESULT = this.typeInfo.GetVarDesc(new WinDef.UINT((long)n2), pointerByReference);
        COMUtils.checkRC(hRESULT);
        return new OaIdl.VARDESC(pointerByReference.getValue());
    }

    public String[] getNames(OaIdl.MEMBERID integerType, int n2) {
        WTypes.BSTR[] bSTRArray = new WTypes.BSTR[n2];
        WinDef.UINTByReference uINTByReference = new WinDef.UINTByReference();
        integerType = this.typeInfo.GetNames((OaIdl.MEMBERID)integerType, bSTRArray, new WinDef.UINT((long)n2), uINTByReference);
        COMUtils.checkRC((WinNT.HRESULT)integerType);
        int n3 = uINTByReference.getValue().intValue();
        String[] stringArray = new String[n3];
        for (n2 = 0; n2 < stringArray.length; ++n2) {
            stringArray[n2] = bSTRArray[n2].getValue();
            OLEAUTO.SysFreeString(bSTRArray[n2]);
        }
        return stringArray;
    }

    public OaIdl.HREFTYPE getRefTypeOfImplType(int n2) {
        OaIdl.HREFTYPEByReference hREFTYPEByReference = new OaIdl.HREFTYPEByReference();
        WinNT.HRESULT hRESULT = this.typeInfo.GetRefTypeOfImplType(new WinDef.UINT((long)n2), hREFTYPEByReference);
        COMUtils.checkRC(hRESULT);
        return hREFTYPEByReference.getValue();
    }

    public int getImplTypeFlags(int n2) {
        IntByReference intByReference = new IntByReference();
        WinNT.HRESULT hRESULT = this.typeInfo.GetImplTypeFlags(new WinDef.UINT((long)n2), intByReference);
        COMUtils.checkRC(hRESULT);
        return intByReference.getValue();
    }

    public OaIdl.MEMBERID[] getIDsOfNames(WTypes.LPOLESTR[] object, int n2) {
        OaIdl.MEMBERID[] mEMBERIDArray = new OaIdl.MEMBERID[n2];
        object = this.typeInfo.GetIDsOfNames((WTypes.LPOLESTR[])object, new WinDef.UINT((long)n2), mEMBERIDArray);
        COMUtils.checkRC((WinNT.HRESULT)object);
        return mEMBERIDArray;
    }

    public Invoke Invoke(WinDef.PVOID nativeMapped, OaIdl.MEMBERID mEMBERID, WinDef.WORD wORD, OleAuto.DISPPARAMS.ByReference byReference) {
        Variant.VARIANT.ByReference byReference2 = new Variant.VARIANT.ByReference();
        OaIdl.EXCEPINFO.ByReference byReference3 = new OaIdl.EXCEPINFO.ByReference();
        WinDef.UINTByReference uINTByReference = new WinDef.UINTByReference();
        nativeMapped = this.typeInfo.Invoke((WinDef.PVOID)nativeMapped, mEMBERID, wORD, byReference, byReference2, byReference3, uINTByReference);
        COMUtils.checkRC((WinNT.HRESULT)nativeMapped);
        return new Invoke(byReference2, byReference3, uINTByReference.getValue().intValue());
    }

    public TypeInfoDoc getDocumentation(OaIdl.MEMBERID object) {
        WTypes.BSTRByReference bSTRByReference = new WTypes.BSTRByReference();
        WTypes.BSTRByReference bSTRByReference2 = new WTypes.BSTRByReference();
        WinDef.DWORDByReference dWORDByReference = new WinDef.DWORDByReference();
        WTypes.BSTRByReference bSTRByReference3 = new WTypes.BSTRByReference();
        object = this.typeInfo.GetDocumentation((OaIdl.MEMBERID)object, bSTRByReference, bSTRByReference2, dWORDByReference, bSTRByReference3);
        COMUtils.checkRC((WinNT.HRESULT)object);
        object = new TypeInfoDoc(bSTRByReference.getString(), bSTRByReference2.getString(), dWORDByReference.getValue().intValue(), bSTRByReference3.getString());
        OLEAUTO.SysFreeString(bSTRByReference.getValue());
        OLEAUTO.SysFreeString(bSTRByReference2.getValue());
        OLEAUTO.SysFreeString(bSTRByReference3.getValue());
        return object;
    }

    public DllEntry GetDllEntry(OaIdl.MEMBERID integerType, OaIdl.INVOKEKIND iNVOKEKIND) {
        WTypes.BSTRByReference bSTRByReference = new WTypes.BSTRByReference();
        WTypes.BSTRByReference bSTRByReference2 = new WTypes.BSTRByReference();
        WinDef.WORDByReference wORDByReference = new WinDef.WORDByReference();
        integerType = this.typeInfo.GetDllEntry((OaIdl.MEMBERID)integerType, iNVOKEKIND, bSTRByReference, bSTRByReference2, wORDByReference);
        COMUtils.checkRC((WinNT.HRESULT)integerType);
        OLEAUTO.SysFreeString(bSTRByReference.getValue());
        OLEAUTO.SysFreeString(bSTRByReference2.getValue());
        return new DllEntry(bSTRByReference.getString(), bSTRByReference2.getString(), wORDByReference.getValue().intValue());
    }

    public ITypeInfo getRefTypeInfo(OaIdl.HREFTYPE integerType) {
        PointerByReference pointerByReference = new PointerByReference();
        integerType = this.typeInfo.GetRefTypeInfo((OaIdl.HREFTYPE)integerType, pointerByReference);
        COMUtils.checkRC((WinNT.HRESULT)integerType);
        return new TypeInfo(pointerByReference.getValue());
    }

    public PointerByReference AddressOfMember(OaIdl.MEMBERID integerType, OaIdl.INVOKEKIND iNVOKEKIND) {
        PointerByReference pointerByReference = new PointerByReference();
        integerType = this.typeInfo.AddressOfMember((OaIdl.MEMBERID)integerType, iNVOKEKIND, pointerByReference);
        COMUtils.checkRC((WinNT.HRESULT)integerType);
        return pointerByReference;
    }

    public PointerByReference CreateInstance(IUnknown object, Guid.REFIID rEFIID) {
        PointerByReference pointerByReference = new PointerByReference();
        object = this.typeInfo.CreateInstance((IUnknown)object, rEFIID, pointerByReference);
        COMUtils.checkRC((WinNT.HRESULT)object);
        return pointerByReference;
    }

    public String GetMops(OaIdl.MEMBERID integerType) {
        WTypes.BSTRByReference bSTRByReference = new WTypes.BSTRByReference();
        integerType = this.typeInfo.GetMops((OaIdl.MEMBERID)integerType, bSTRByReference);
        COMUtils.checkRC((WinNT.HRESULT)integerType);
        return bSTRByReference.getString();
    }

    public ContainingTypeLib GetContainingTypeLib() {
        PointerByReference pointerByReference = new PointerByReference();
        WinDef.UINTByReference uINTByReference = new WinDef.UINTByReference();
        WinNT.HRESULT hRESULT = this.typeInfo.GetContainingTypeLib(pointerByReference, uINTByReference);
        COMUtils.checkRC(hRESULT);
        return new ContainingTypeLib(new TypeLib(pointerByReference.getValue()), uINTByReference.getValue().intValue());
    }

    public void ReleaseTypeAttr(OaIdl.TYPEATTR tYPEATTR) {
        this.typeInfo.ReleaseTypeAttr(tYPEATTR);
    }

    public void ReleaseFuncDesc(OaIdl.FUNCDESC fUNCDESC) {
        this.typeInfo.ReleaseFuncDesc(fUNCDESC);
    }

    public void ReleaseVarDesc(OaIdl.VARDESC vARDESC) {
        this.typeInfo.ReleaseVarDesc(vARDESC);
    }

    public static class ContainingTypeLib {
        private ITypeLib typeLib;
        private int index;

        public ContainingTypeLib(ITypeLib iTypeLib, int n2) {
            this.typeLib = iTypeLib;
            this.index = n2;
        }

        public ITypeLib getTypeLib() {
            return this.typeLib;
        }

        public void setTypeLib(ITypeLib iTypeLib) {
            this.typeLib = iTypeLib;
        }

        public int getIndex() {
            return this.index;
        }

        public void setIndex(int n2) {
            this.index = n2;
        }
    }

    public static class DllEntry {
        private String dllName;
        private String name;
        private int ordinal;

        public DllEntry(String string, String string2, int n2) {
            this.dllName = string;
            this.name = string2;
            this.ordinal = n2;
        }

        public String getDllName() {
            return this.dllName;
        }

        public void setDllName(String string) {
            this.dllName = string;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String string) {
            this.name = string;
        }

        public int getOrdinal() {
            return this.ordinal;
        }

        public void setOrdinal(int n2) {
            this.ordinal = n2;
        }
    }

    public static class TypeInfoDoc {
        private String name;
        private String docString;
        private int helpContext;
        private String helpFile;

        public TypeInfoDoc(String string, String string2, int n2, String string3) {
            this.name = string;
            this.docString = string2;
            this.helpContext = n2;
            this.helpFile = string3;
        }

        public String getName() {
            return this.name;
        }

        public String getDocString() {
            return this.docString;
        }

        public int getHelpContext() {
            return this.helpContext;
        }

        public String getHelpFile() {
            return this.helpFile;
        }
    }

    public static class Invoke {
        private Variant.VARIANT.ByReference pVarResult;
        private OaIdl.EXCEPINFO.ByReference pExcepInfo;
        private int puArgErr;

        public Invoke(Variant.VARIANT.ByReference byReference, OaIdl.EXCEPINFO.ByReference byReference2, int n2) {
            this.pVarResult = byReference;
            this.pExcepInfo = byReference2;
            this.puArgErr = n2;
        }

        public Variant.VARIANT.ByReference getpVarResult() {
            return this.pVarResult;
        }

        public OaIdl.EXCEPINFO.ByReference getpExcepInfo() {
            return this.pExcepInfo;
        }

        public int getPuArgErr() {
            return this.puArgErr;
        }
    }
}


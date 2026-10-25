/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32.COM.tlb.imp;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.COM.IDispatch;
import com.sun.jna.platform.win32.COM.ITypeInfo;
import com.sun.jna.platform.win32.COM.IUnknown;
import com.sun.jna.platform.win32.COM.TypeInfoUtil;
import com.sun.jna.platform.win32.COM.TypeLibUtil;
import com.sun.jna.platform.win32.COM.tlb.imp.TlbBase;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.platform.win32.WTypes;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;

public abstract class TlbAbstractMethod
extends TlbBase
implements Variant {
    protected TypeInfoUtil.TypeInfoDoc typeInfoDoc;
    protected String methodName;
    protected String docStr;
    protected short vtableId;
    protected OaIdl.MEMBERID memberid;
    protected short paramCount;
    protected String returnType;
    protected String methodparams = "";
    protected String methodvariables = "";

    public TlbAbstractMethod(int n2, TypeLibUtil typeLibUtil, OaIdl.FUNCDESC fUNCDESC, TypeInfoUtil typeInfoUtil) {
        super(n2, typeLibUtil, typeInfoUtil);
        this.typeInfoDoc = typeInfoUtil.getDocumentation(fUNCDESC.memid);
        this.methodName = this.typeInfoDoc.getName();
        this.docStr = this.typeInfoDoc.getDocString();
        this.vtableId = fUNCDESC.oVft.shortValue();
        this.memberid = fUNCDESC.memid;
        this.paramCount = fUNCDESC.cParams.shortValue();
        this.returnType = this.getType(fUNCDESC);
    }

    public TypeInfoUtil.TypeInfoDoc getTypeInfoDoc() {
        return this.typeInfoDoc;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public String getDocStr() {
        return this.docStr;
    }

    protected String getVarType(WTypes.VARTYPE vARTYPE) {
        switch (vARTYPE.intValue()) {
            case 0: {
                return "";
            }
            case 1: {
                return "null";
            }
            case 2: {
                return "short";
            }
            case 3: {
                return "int";
            }
            case 4: {
                return "float";
            }
            case 5: {
                return "double";
            }
            case 6: {
                return OaIdl.CURRENCY.class.getSimpleName();
            }
            case 7: {
                return OaIdl.DATE.class.getSimpleName();
            }
            case 8: {
                return WTypes.BSTR.class.getSimpleName();
            }
            case 9: {
                return IDispatch.class.getSimpleName();
            }
            case 10: {
                return WinDef.SCODE.class.getSimpleName();
            }
            case 11: {
                return WinDef.BOOL.class.getSimpleName();
            }
            case 12: {
                return Variant.VARIANT.class.getSimpleName();
            }
            case 13: {
                return IUnknown.class.getSimpleName();
            }
            case 14: {
                return OaIdl.DECIMAL.class.getSimpleName();
            }
            case 16: {
                return WinDef.CHAR.class.getSimpleName();
            }
            case 17: {
                return WinDef.UCHAR.class.getSimpleName();
            }
            case 18: {
                return WinDef.USHORT.class.getSimpleName();
            }
            case 19: {
                return WinDef.UINT.class.getSimpleName();
            }
            case 20: {
                return WinDef.LONG.class.getSimpleName();
            }
            case 21: {
                return WinDef.ULONG.class.getSimpleName();
            }
            case 22: {
                return "int";
            }
            case 23: {
                return WinDef.UINT.class.getSimpleName();
            }
            case 24: {
                return WinDef.PVOID.class.getSimpleName();
            }
            case 25: {
                return WinNT.HRESULT.class.getSimpleName();
            }
            case 26: {
                return Pointer.class.getSimpleName();
            }
            case 27: {
                return "safearray";
            }
            case 28: {
                return "carray";
            }
            case 29: {
                return "userdefined";
            }
            case 30: {
                return WTypes.LPSTR.class.getSimpleName();
            }
            case 31: {
                return WTypes.LPWSTR.class.getSimpleName();
            }
            case 36: {
                return "record";
            }
            case 37: {
                return WinDef.INT_PTR.class.getSimpleName();
            }
            case 38: {
                return WinDef.UINT_PTR.class.getSimpleName();
            }
            case 64: {
                return WinBase.FILETIME.class.getSimpleName();
            }
            case 66: {
                return "steam";
            }
            case 67: {
                return "storage";
            }
            case 68: {
                return "steamed_object";
            }
            case 69: {
                return "stored_object";
            }
            case 70: {
                return "blob_object";
            }
            case 71: {
                return "cf";
            }
            case 72: {
                return Guid.CLSID.class.getSimpleName();
            }
            case 73: {
                return "";
            }
            case 4096: {
                return "";
            }
            case 8192: {
                return "";
            }
            case 16384: {
                return WinDef.PVOID.class.getSimpleName();
            }
            case 32768: {
                return "";
            }
            case 65535: {
                return "illegal";
            }
        }
        return null;
    }

    protected String getUserdefinedType(OaIdl.HREFTYPE object) {
        object = this.typeInfoUtil.getRefTypeInfo((OaIdl.HREFTYPE)object);
        object = new TypeInfoUtil((ITypeInfo)object);
        object = ((TypeInfoUtil)object).getDocumentation(OaIdl.MEMBERID_NIL);
        return ((TypeInfoUtil.TypeInfoDoc)object).getName();
    }

    protected String getType(OaIdl.FUNCDESC structure) {
        structure = structure.elemdescFunc;
        return this.getType((OaIdl.ELEMDESC)structure);
    }

    protected String getType(OaIdl.ELEMDESC structure) {
        structure = structure.tdesc;
        return this.getType((OaIdl.TYPEDESC)structure);
    }

    protected String getType(OaIdl.TYPEDESC object) {
        WTypes.VARTYPE vARTYPE = ((OaIdl.TYPEDESC)object).vt;
        if (vARTYPE.intValue() == 26) {
            object = ((OaIdl.TYPEDESC)object)._typedesc.getLptdesc();
            object = this.getType((OaIdl.TYPEDESC)object);
        } else if (vARTYPE.intValue() == 27 || vARTYPE.intValue() == 28) {
            object = ((OaIdl.TYPEDESC)object)._typedesc.getLpadesc().tdescElem;
            object = this.getType((OaIdl.TYPEDESC)object);
        } else if (vARTYPE.intValue() == 29) {
            object = ((OaIdl.TYPEDESC)object)._typedesc.hreftype;
            object = this.getUserdefinedType((OaIdl.HREFTYPE)object);
        } else {
            object = this.getVarType(vARTYPE);
        }
        return object;
    }

    protected String replaceJavaKeyword(String string) {
        if (string.equals("final")) {
            return "_" + string;
        }
        if (string.equals("default")) {
            return "_" + string;
        }
        if (string.equals("case")) {
            return "_" + string;
        }
        if (string.equals("char")) {
            return "_" + string;
        }
        if (string.equals("private")) {
            return "_" + string;
        }
        if (string.equals("default")) {
            return "_" + string;
        }
        return string;
    }
}


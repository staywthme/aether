/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32.COM.tlb.imp;

import com.sun.jna.platform.win32.COM.ITypeInfo;
import com.sun.jna.platform.win32.COM.TypeInfoUtil;
import com.sun.jna.platform.win32.COM.TypeLibUtil;
import com.sun.jna.platform.win32.COM.tlb.imp.TlbAbstractMethod;
import com.sun.jna.platform.win32.COM.tlb.imp.TlbBase;
import com.sun.jna.platform.win32.COM.tlb.imp.TlbFunctionDispId;
import com.sun.jna.platform.win32.COM.tlb.imp.TlbFunctionVTable;
import com.sun.jna.platform.win32.COM.tlb.imp.TlbPropertyGet;
import com.sun.jna.platform.win32.COM.tlb.imp.TlbPropertyPut;
import com.sun.jna.platform.win32.OaIdl;

public class TlbCoClass
extends TlbBase {
    public TlbCoClass(int n2, String string, TypeLibUtil object, String string2) {
        super(n2, (TypeLibUtil)object, null);
        object = ((TypeLibUtil)object).getTypeInfoUtil(n2);
        Object object2 = this.typeLibUtil.getDocumentation(n2);
        String string3 = ((TypeLibUtil.TypeLibDoc)object2).getDocString();
        if (((TypeLibUtil.TypeLibDoc)object2).getName().length() > 0) {
            this.name = ((TypeLibUtil.TypeLibDoc)object2).getName();
        }
        this.logInfo("Type of kind 'CoClass' found: " + this.name);
        this.createPackageName(string);
        TlbCoClass tlbCoClass = this;
        tlbCoClass.createClassName(tlbCoClass.name);
        TlbCoClass tlbCoClass2 = this;
        tlbCoClass2.setFilename(tlbCoClass2.name);
        object2 = this.typeLibUtil.getLibAttr().guid.toGuidString();
        int n3 = this.typeLibUtil.getLibAttr().wMajorVerNum.intValue();
        int n4 = this.typeLibUtil.getLibAttr().wMinorVerNum.intValue();
        String string4 = n3 + "." + n4;
        Object object3 = ((TypeInfoUtil)object).getTypeAttr().guid.toGuidString();
        this.createJavaDocHeader((String)object2, string4, string3);
        this.createCLSID((String)object3);
        TlbCoClass tlbCoClass3 = this;
        tlbCoClass3.createCLSIDName(tlbCoClass3.name);
        object2 = ((TypeInfoUtil)object).getTypeAttr();
        int n5 = ((OaIdl.TYPEATTR)object2).cImplTypes.intValue();
        string4 = "";
        for (int i2 = 0; i2 < n5; ++i2) {
            object3 = ((TypeInfoUtil)object).getRefTypeOfImplType(i2);
            object3 = ((TypeInfoUtil)object).getRefTypeInfo((OaIdl.HREFTYPE)object3);
            object3 = new TypeInfoUtil((ITypeInfo)object3);
            this.createFunctions((TypeInfoUtil)object3, string2);
            object3 = ((TypeInfoUtil)object3).getDocumentation(new OaIdl.MEMBERID(-1));
            string4 = string4 + ((TypeInfoUtil.TypeInfoDoc)object3).getName();
            if (i2 >= n5 - 1) continue;
            string4 = string4 + ", ";
        }
        this.createInterfaces(string4);
        TlbCoClass tlbCoClass4 = this;
        tlbCoClass4.createContent(tlbCoClass4.content);
    }

    protected void createFunctions(TypeInfoUtil typeInfoUtil, String object) {
        object = typeInfoUtil.getTypeAttr();
        int n2 = ((OaIdl.TYPEATTR)object).cFuncs.intValue();
        for (int i2 = 0; i2 < n2; ++i2) {
            OaIdl.FUNCDESC fUNCDESC = typeInfoUtil.getFuncDesc(i2);
            TlbAbstractMethod tlbAbstractMethod = null;
            if (fUNCDESC.invkind.value == OaIdl.INVOKEKIND.INVOKE_FUNC.value) {
                tlbAbstractMethod = this.isVTableMode() ? new TlbFunctionVTable(i2, this.index, this.typeLibUtil, fUNCDESC, typeInfoUtil) : new TlbFunctionDispId(i2, this.index, this.typeLibUtil, fUNCDESC, typeInfoUtil);
            } else if (fUNCDESC.invkind.value == OaIdl.INVOKEKIND.INVOKE_PROPERTYGET.value) {
                tlbAbstractMethod = new TlbPropertyGet(i2, this.index, this.typeLibUtil, fUNCDESC, typeInfoUtil);
            } else if (fUNCDESC.invkind.value == OaIdl.INVOKEKIND.INVOKE_PROPERTYPUT.value) {
                tlbAbstractMethod = new TlbPropertyPut(i2, this.index, this.typeLibUtil, fUNCDESC, typeInfoUtil);
            } else if (fUNCDESC.invkind.value == OaIdl.INVOKEKIND.INVOKE_PROPERTYPUTREF.value) {
                tlbAbstractMethod = new TlbPropertyPut(i2, this.index, this.typeLibUtil, fUNCDESC, typeInfoUtil);
            }
            if (!this.isReservedMethod(tlbAbstractMethod.getMethodName())) {
                this.content = this.content + tlbAbstractMethod.getClassBuffer();
                if (i2 < n2 - 1) {
                    this.content = this.content + "\n";
                }
            }
            typeInfoUtil.ReleaseFuncDesc(fUNCDESC);
        }
    }

    protected void createJavaDocHeader(String string, String string2, String string3) {
        this.replaceVariable("uuid", string);
        this.replaceVariable("version", string2);
        this.replaceVariable("helpstring", string3);
    }

    protected void createCLSIDName(String string) {
        this.replaceVariable("clsidname", string.toUpperCase());
    }

    protected void createCLSID(String string) {
        this.replaceVariable("clsid", string);
    }

    protected void createInterfaces(String string) {
        this.replaceVariable("interfaces", string);
    }

    @Override
    protected String getClassTemplate() {
        return "com/sun/jna/platform/win32/COM/tlb/imp/TlbCoClass.template";
    }
}


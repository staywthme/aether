/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32.COM.tlb.imp;

import com.sun.jna.platform.win32.COM.TypeInfoUtil;
import com.sun.jna.platform.win32.COM.TypeLibUtil;
import com.sun.jna.platform.win32.COM.tlb.imp.TlbBase;
import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.platform.win32.Variant;

public class TlbEnum
extends TlbBase {
    public TlbEnum(int n2, String object, TypeLibUtil typeLibUtil) {
        super(n2, typeLibUtil, null);
        Object object2 = this.typeLibUtil.getDocumentation(n2);
        Object object3 = ((TypeLibUtil.TypeLibDoc)object2).getDocString();
        if (((TypeLibUtil.TypeLibDoc)object2).getName().length() > 0) {
            this.name = ((TypeLibUtil.TypeLibDoc)object2).getName();
        }
        this.logInfo("Type of kind 'Enum' found: " + this.name);
        this.createPackageName((String)object);
        TlbEnum tlbEnum = this;
        tlbEnum.createClassName(tlbEnum.name);
        TlbEnum tlbEnum2 = this;
        tlbEnum2.setFilename(tlbEnum2.name);
        TypeInfoUtil typeInfoUtil = typeLibUtil.getTypeInfoUtil(n2);
        object = typeInfoUtil.getTypeAttr();
        this.createJavaDocHeader(((OaIdl.TYPEATTR)object).guid.toGuidString(), (String)object3);
        int n3 = ((OaIdl.TYPEATTR)object).cVars.intValue();
        for (int i2 = 0; i2 < n3; ++i2) {
            object2 = typeInfoUtil.getVarDesc(i2);
            object3 = ((OaIdl.VARDESC)object2)._vardesc.lpvarValue;
            object3 = ((Variant.VARIANT)object3).getValue();
            Object object4 = ((OaIdl.VARDESC)object2).memid;
            object4 = typeInfoUtil.getDocumentation((OaIdl.MEMBERID)object4);
            this.content = this.content + "\t\t//" + ((TypeInfoUtil.TypeInfoDoc)object4).getName() + "\n";
            this.content = this.content + "\t\tpublic static final int " + ((TypeInfoUtil.TypeInfoDoc)object4).getName() + " = " + object3.toString() + ";";
            if (i2 < n3 - 1) {
                this.content = this.content + "\n";
            }
            typeInfoUtil.ReleaseVarDesc((OaIdl.VARDESC)object2);
        }
        TlbEnum tlbEnum3 = this;
        tlbEnum3.createContent(tlbEnum3.content);
    }

    protected void createJavaDocHeader(String string, String string2) {
        this.replaceVariable("uuid", string);
        this.replaceVariable("helpstring", string2);
    }

    @Override
    protected String getClassTemplate() {
        return "com/sun/jna/platform/win32/COM/tlb/imp/TlbEnum.template";
    }
}


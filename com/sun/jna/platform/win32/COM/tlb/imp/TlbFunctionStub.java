/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32.COM.tlb.imp;

import com.sun.jna.platform.win32.COM.TypeInfoUtil;
import com.sun.jna.platform.win32.COM.TypeLibUtil;
import com.sun.jna.platform.win32.COM.tlb.imp.TlbAbstractMethod;
import com.sun.jna.platform.win32.OaIdl;

public class TlbFunctionStub
extends TlbAbstractMethod {
    public TlbFunctionStub(int n2, TypeLibUtil object, OaIdl.FUNCDESC fUNCDESC, TypeInfoUtil stringArray) {
        super(n2, (TypeLibUtil)object, fUNCDESC, (TypeInfoUtil)stringArray);
        Object object2 = stringArray.getDocumentation(fUNCDESC.memid);
        object = ((TypeInfoUtil.TypeInfoDoc)object2).getName();
        object2 = ((TypeInfoUtil.TypeInfoDoc)object2).getDocString();
        stringArray = stringArray.getNames(fUNCDESC.memid, this.paramCount + 1);
        if (this.paramCount > 0) {
            this.methodvariables = ", ";
        }
        for (int i2 = 0; i2 < this.paramCount; ++i2) {
            OaIdl.ELEMDESC eLEMDESC = fUNCDESC.lprgelemdescParam.elemDescArg[i2];
            String string = stringArray[i2 + 1].toLowerCase();
            this.methodparams = this.methodparams + this.getType(eLEMDESC.tdesc) + " " + this.replaceJavaKeyword(string);
            this.methodvariables = this.methodvariables + string;
            if (i2 >= this.paramCount - 1) continue;
            this.methodparams = this.methodparams + ", ";
            this.methodvariables = this.methodvariables + ", ";
        }
        this.replaceVariable("helpstring", (String)object2);
        this.replaceVariable("returntype", this.returnType);
        this.replaceVariable("methodname", (String)object);
        this.replaceVariable("methodparams", this.methodparams);
        this.replaceVariable("vtableid", String.valueOf(this.vtableId));
        this.replaceVariable("memberid", String.valueOf(this.memberid));
    }

    @Override
    protected String getClassTemplate() {
        return "com/sun/jna/platform/win32/COM/tlb/imp/TlbFunctionStub.template";
    }
}


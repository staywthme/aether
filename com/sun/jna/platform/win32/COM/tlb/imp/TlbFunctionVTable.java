/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32.COM.tlb.imp;

import com.sun.jna.platform.win32.COM.TypeInfoUtil;
import com.sun.jna.platform.win32.COM.TypeLibUtil;
import com.sun.jna.platform.win32.COM.tlb.imp.TlbAbstractMethod;
import com.sun.jna.platform.win32.OaIdl;

public class TlbFunctionVTable
extends TlbAbstractMethod {
    public TlbFunctionVTable(int n2, int n3, TypeLibUtil typeLibUtil, OaIdl.FUNCDESC fUNCDESC, TypeInfoUtil object) {
        super(n3, typeLibUtil, fUNCDESC, (TypeInfoUtil)object);
        String[] stringArray = ((TypeInfoUtil)object).getNames(fUNCDESC.memid, this.paramCount + 1);
        if (this.paramCount > 0) {
            this.methodvariables = ", ";
        }
        for (int i2 = 0; i2 < this.paramCount; ++i2) {
            object = fUNCDESC.lprgelemdescParam.elemDescArg[i2];
            String string = stringArray[i2 + 1].toLowerCase();
            this.methodparams = this.methodparams + this.getType(((OaIdl.ELEMDESC)object).tdesc) + " " + this.replaceJavaKeyword(string);
            this.methodvariables = this.methodvariables + string;
            if (i2 >= this.paramCount - 1) continue;
            this.methodparams = this.methodparams + ", ";
            this.methodvariables = this.methodvariables + ", ";
        }
        this.replaceVariable("helpstring", this.docStr);
        this.replaceVariable("returntype", this.returnType);
        this.replaceVariable("methodname", this.methodName);
        this.replaceVariable("methodparams", this.methodparams);
        this.replaceVariable("methodvariables", this.methodvariables);
        this.replaceVariable("vtableid", String.valueOf(this.vtableId));
        this.replaceVariable("memberid", String.valueOf(this.memberid));
        this.replaceVariable("functionCount", String.valueOf(n2));
    }

    @Override
    protected String getClassTemplate() {
        return "com/sun/jna/platform/win32/COM/tlb/imp/TlbFunctionVTable.template";
    }
}


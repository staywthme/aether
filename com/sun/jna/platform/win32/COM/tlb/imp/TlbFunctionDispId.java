/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32.COM.tlb.imp;

import com.sun.jna.platform.win32.COM.TypeInfoUtil;
import com.sun.jna.platform.win32.COM.TypeLibUtil;
import com.sun.jna.platform.win32.COM.tlb.imp.TlbAbstractMethod;
import com.sun.jna.platform.win32.OaIdl;

public class TlbFunctionDispId
extends TlbAbstractMethod {
    public TlbFunctionDispId(int n2, int n3, TypeLibUtil typeLibUtil, OaIdl.FUNCDESC fUNCDESC, TypeInfoUtil object) {
        super(n3, typeLibUtil, fUNCDESC, (TypeInfoUtil)object);
        String[] stringArray = ((TypeInfoUtil)object).getNames(fUNCDESC.memid, this.paramCount + 1);
        for (int i2 = 0; i2 < this.paramCount; ++i2) {
            object = fUNCDESC.lprgelemdescParam.elemDescArg[i2];
            String string = stringArray[i2 + 1].toLowerCase();
            object = this.getType(((OaIdl.ELEMDESC)object).tdesc);
            string = this.replaceJavaKeyword(string);
            this.methodparams = this.methodparams + (String)object + " " + string;
            this.methodvariables = ((String)object).equals("VARIANT") ? this.methodvariables + string : this.methodvariables + "new VARIANT(" + string + ")";
            if (i2 >= this.paramCount - 1) continue;
            this.methodparams = this.methodparams + ", ";
            this.methodvariables = this.methodvariables + ", ";
        }
        String string = this.returnType.equalsIgnoreCase("VARIANT") ? "pResult" : "((" + this.returnType + ") pResult.getValue())";
        this.replaceVariable("helpstring", this.docStr);
        this.replaceVariable("returntype", this.returnType);
        this.replaceVariable("returnvalue", string);
        this.replaceVariable("methodname", this.methodName);
        this.replaceVariable("methodparams", this.methodparams);
        this.replaceVariable("methodvariables", this.methodvariables);
        this.replaceVariable("vtableid", String.valueOf(this.vtableId));
        this.replaceVariable("memberid", String.valueOf(this.memberid));
        this.replaceVariable("functionCount", String.valueOf(n2));
    }

    @Override
    protected String getClassTemplate() {
        return "com/sun/jna/platform/win32/COM/tlb/imp/TlbFunctionDispId.template";
    }
}


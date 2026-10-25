/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32.COM.tlb.imp;

import com.sun.jna.platform.win32.COM.TypeInfoUtil;
import com.sun.jna.platform.win32.COM.TypeLibUtil;
import com.sun.jna.platform.win32.COM.tlb.imp.TlbAbstractMethod;
import com.sun.jna.platform.win32.OaIdl;

public class TlbPropertyPutStub
extends TlbAbstractMethod {
    public TlbPropertyPutStub(int n2, TypeLibUtil object, OaIdl.FUNCDESC fUNCDESC, TypeInfoUtil stringArray) {
        super(n2, (TypeLibUtil)object, fUNCDESC, (TypeInfoUtil)stringArray);
        Object object2 = stringArray.getDocumentation(fUNCDESC.memid);
        object = ((TypeInfoUtil.TypeInfoDoc)object2).getDocString();
        object2 = "set" + ((TypeInfoUtil.TypeInfoDoc)object2).getName();
        stringArray = stringArray.getNames(fUNCDESC.memid, this.paramCount + 1);
        for (int i2 = 0; i2 < this.paramCount; ++i2) {
            Object object3 = fUNCDESC.lprgelemdescParam.elemDescArg[i2];
            object3 = this.getType((OaIdl.ELEMDESC)object3);
            this.methodparams = this.methodparams + (String)object3 + " " + this.replaceJavaKeyword(stringArray[i2].toLowerCase());
            if (i2 >= this.paramCount - 1) continue;
            this.methodparams = this.methodparams + ", ";
        }
        this.replaceVariable("helpstring", (String)object);
        this.replaceVariable("methodname", (String)object2);
        this.replaceVariable("methodparams", this.methodparams);
        this.replaceVariable("vtableid", String.valueOf(this.vtableId));
        this.replaceVariable("memberid", String.valueOf(this.memberid));
    }

    @Override
    protected String getClassTemplate() {
        return "com/sun/jna/platform/win32/COM/tlb/imp/TlbPropertyPutStub.template";
    }
}


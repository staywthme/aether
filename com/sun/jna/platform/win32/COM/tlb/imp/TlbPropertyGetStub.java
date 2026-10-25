/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32.COM.tlb.imp;

import com.sun.jna.platform.win32.COM.TypeInfoUtil;
import com.sun.jna.platform.win32.COM.TypeLibUtil;
import com.sun.jna.platform.win32.COM.tlb.imp.TlbAbstractMethod;
import com.sun.jna.platform.win32.OaIdl;

public class TlbPropertyGetStub
extends TlbAbstractMethod {
    public TlbPropertyGetStub(int n2, TypeLibUtil object, OaIdl.FUNCDESC fUNCDESC, TypeInfoUtil typeInfoUtil) {
        super(n2, (TypeLibUtil)object, fUNCDESC, typeInfoUtil);
        Object object2 = typeInfoUtil.getDocumentation(fUNCDESC.memid);
        object = ((TypeInfoUtil.TypeInfoDoc)object2).getDocString();
        object2 = "get" + ((TypeInfoUtil.TypeInfoDoc)object2).getName();
        this.replaceVariable("helpstring", (String)object);
        this.replaceVariable("returntype", this.returnType);
        this.replaceVariable("methodname", (String)object2);
        this.replaceVariable("vtableid", String.valueOf(this.vtableId));
        this.replaceVariable("memberid", String.valueOf(this.memberid));
    }

    @Override
    protected String getClassTemplate() {
        return "com/sun/jna/platform/win32/COM/tlb/imp/TlbPropertyGetStub.template";
    }
}


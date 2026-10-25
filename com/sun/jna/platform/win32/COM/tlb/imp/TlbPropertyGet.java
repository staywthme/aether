/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32.COM.tlb.imp;

import com.sun.jna.platform.win32.COM.TypeInfoUtil;
import com.sun.jna.platform.win32.COM.TypeLibUtil;
import com.sun.jna.platform.win32.COM.tlb.imp.TlbAbstractMethod;
import com.sun.jna.platform.win32.OaIdl;

public class TlbPropertyGet
extends TlbAbstractMethod {
    public TlbPropertyGet(int n2, int n3, TypeLibUtil typeLibUtil, OaIdl.FUNCDESC fUNCDESC, TypeInfoUtil typeInfoUtil) {
        super(n3, typeLibUtil, fUNCDESC, typeInfoUtil);
        this.methodName = "get" + this.getMethodName();
        this.replaceVariable("helpstring", this.docStr);
        this.replaceVariable("returntype", this.returnType);
        this.replaceVariable("methodname", this.methodName);
        this.replaceVariable("vtableid", String.valueOf(this.vtableId));
        this.replaceVariable("memberid", String.valueOf(this.memberid));
        this.replaceVariable("functionCount", String.valueOf(n2));
    }

    @Override
    protected String getClassTemplate() {
        return "com/sun/jna/platform/win32/COM/tlb/imp/TlbPropertyGet.template";
    }
}


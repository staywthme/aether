/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32.COM.tlb.imp;

import com.sun.jna.platform.win32.COM.TypeInfoUtil;
import com.sun.jna.platform.win32.COM.TypeLibUtil;
import com.sun.jna.platform.win32.COM.tlb.imp.TlbAbstractMethod;
import com.sun.jna.platform.win32.COM.tlb.imp.TlbBase;
import com.sun.jna.platform.win32.COM.tlb.imp.TlbFunctionStub;
import com.sun.jna.platform.win32.COM.tlb.imp.TlbPropertyGetStub;
import com.sun.jna.platform.win32.COM.tlb.imp.TlbPropertyPutStub;
import com.sun.jna.platform.win32.OaIdl;

public class TlbDispInterface
extends TlbBase {
    public TlbDispInterface(int n2, String object, TypeLibUtil typeLibUtil) {
        super(n2, typeLibUtil, null);
        Object object2 = this.typeLibUtil.getDocumentation(n2);
        String string = ((TypeLibUtil.TypeLibDoc)object2).getDocString();
        if (((TypeLibUtil.TypeLibDoc)object2).getName().length() > 0) {
            this.name = ((TypeLibUtil.TypeLibDoc)object2).getName();
        }
        this.logInfo("Type of kind 'DispInterface' found: " + this.name);
        this.createPackageName((String)object);
        TlbDispInterface tlbDispInterface = this;
        tlbDispInterface.createClassName(tlbDispInterface.name);
        TlbDispInterface tlbDispInterface2 = this;
        tlbDispInterface2.setFilename(tlbDispInterface2.name);
        object = typeLibUtil.getTypeInfoUtil(n2);
        object2 = ((TypeInfoUtil)object).getTypeAttr();
        this.createJavaDocHeader(((OaIdl.TYPEATTR)object2).guid.toGuidString(), string);
        int n3 = ((OaIdl.TYPEATTR)object2).cFuncs.intValue();
        for (int i2 = 0; i2 < n3; ++i2) {
            OaIdl.FUNCDESC fUNCDESC = ((TypeInfoUtil)object).getFuncDesc(i2);
            Object object3 = fUNCDESC.memid;
            object3 = ((TypeInfoUtil)object).getDocumentation((OaIdl.MEMBERID)object3);
            object3 = ((TypeInfoUtil.TypeInfoDoc)object3).getName();
            TlbAbstractMethod tlbAbstractMethod = null;
            if (!this.isReservedMethod((String)object3)) {
                if (fUNCDESC.invkind.value == OaIdl.INVOKEKIND.INVOKE_FUNC.value) {
                    tlbAbstractMethod = new TlbFunctionStub(n2, typeLibUtil, fUNCDESC, (TypeInfoUtil)object);
                } else if (fUNCDESC.invkind.value == OaIdl.INVOKEKIND.INVOKE_PROPERTYGET.value) {
                    tlbAbstractMethod = new TlbPropertyGetStub(n2, typeLibUtil, fUNCDESC, (TypeInfoUtil)object);
                } else if (fUNCDESC.invkind.value == OaIdl.INVOKEKIND.INVOKE_PROPERTYPUT.value) {
                    tlbAbstractMethod = new TlbPropertyPutStub(n2, typeLibUtil, fUNCDESC, (TypeInfoUtil)object);
                } else if (fUNCDESC.invkind.value == OaIdl.INVOKEKIND.INVOKE_PROPERTYPUTREF.value) {
                    tlbAbstractMethod = new TlbPropertyPutStub(n2, typeLibUtil, fUNCDESC, (TypeInfoUtil)object);
                }
                this.content = this.content + tlbAbstractMethod.getClassBuffer();
                if (i2 < n3 - 1) {
                    this.content = this.content + "\n";
                }
            }
            ((TypeInfoUtil)object).ReleaseFuncDesc(fUNCDESC);
        }
        TlbDispInterface tlbDispInterface3 = this;
        tlbDispInterface3.createContent(tlbDispInterface3.content);
    }

    protected void createJavaDocHeader(String string, String string2) {
        this.replaceVariable("uuid", string);
        this.replaceVariable("helpstring", string2);
    }

    @Override
    protected String getClassTemplate() {
        return "com/sun/jna/platform/win32/COM/tlb/imp/TlbDispInterface.template";
    }
}


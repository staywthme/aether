/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32.COM;

import com.sun.jna.platform.win32.COM.COMBindingBaseObject;
import com.sun.jna.platform.win32.COM.COMException;
import com.sun.jna.platform.win32.COM.Dispatch;
import com.sun.jna.platform.win32.COM.IDispatch;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.OleAuto;
import com.sun.jna.platform.win32.Variant;
import java.util.Date;

public class COMLateBindingObject
extends COMBindingBaseObject {
    public COMLateBindingObject(IDispatch iDispatch) {
        super(iDispatch);
    }

    public COMLateBindingObject(Guid.CLSID cLSID, boolean bl) {
        super(cLSID, bl);
    }

    public COMLateBindingObject(String string, boolean bl) throws COMException {
        super(string, bl);
    }

    protected IDispatch getAutomationProperty(String string) {
        Variant.VARIANT.ByReference byReference = new Variant.VARIANT.ByReference();
        this.oleMethod(2, byReference, string);
        return (IDispatch)byReference.getValue();
    }

    protected IDispatch getAutomationProperty(String string, Variant.VARIANT vARIANT) {
        Variant.VARIANT.ByReference byReference = new Variant.VARIANT.ByReference();
        this.oleMethod(2, byReference, string, vARIANT);
        return (IDispatch)byReference.getValue();
    }

    @Deprecated
    protected IDispatch getAutomationProperty(String string, COMLateBindingObject object) {
        object = new Variant.VARIANT.ByReference();
        this.oleMethod(2, (Variant.VARIANT.ByReference)object, string);
        return (IDispatch)((Variant.VARIANT)object).getValue();
    }

    @Deprecated
    protected IDispatch getAutomationProperty(String string, COMLateBindingObject object, Variant.VARIANT vARIANT) {
        object = new Variant.VARIANT.ByReference();
        this.oleMethod(2, (Variant.VARIANT.ByReference)object, string, vARIANT);
        return (IDispatch)((Variant.VARIANT)object).getValue();
    }

    @Deprecated
    protected IDispatch getAutomationProperty(String string, IDispatch object) {
        object = new Variant.VARIANT.ByReference();
        this.oleMethod(2, (Variant.VARIANT.ByReference)object, string);
        return (IDispatch)((Variant.VARIANT)object).getValue();
    }

    protected boolean getBooleanProperty(String string) {
        Variant.VARIANT.ByReference byReference = new Variant.VARIANT.ByReference();
        this.oleMethod(2, byReference, string);
        return byReference.booleanValue();
    }

    protected Date getDateProperty(String string) {
        Variant.VARIANT.ByReference byReference = new Variant.VARIANT.ByReference();
        this.oleMethod(2, byReference, string);
        return byReference.dateValue();
    }

    protected int getIntProperty(String string) {
        Variant.VARIANT.ByReference byReference = new Variant.VARIANT.ByReference();
        this.oleMethod(2, byReference, string);
        return byReference.intValue();
    }

    protected short getShortProperty(String string) {
        Variant.VARIANT.ByReference byReference = new Variant.VARIANT.ByReference();
        this.oleMethod(2, byReference, string);
        return byReference.shortValue();
    }

    protected String getStringProperty(String string) {
        Variant.VARIANT.ByReference byReference = new Variant.VARIANT.ByReference();
        this.oleMethod(2, byReference, string);
        string = byReference.stringValue();
        OleAuto.INSTANCE.VariantClear(byReference);
        return string;
    }

    protected Variant.VARIANT invoke(String string) {
        Variant.VARIANT.ByReference byReference = new Variant.VARIANT.ByReference();
        this.oleMethod(1, byReference, string);
        return byReference;
    }

    protected Variant.VARIANT invoke(String string, Variant.VARIANT vARIANT) {
        Variant.VARIANT.ByReference byReference = new Variant.VARIANT.ByReference();
        this.oleMethod(1, byReference, string, vARIANT);
        return byReference;
    }

    protected Variant.VARIANT invoke(String string, Variant.VARIANT[] vARIANTArray) {
        Variant.VARIANT.ByReference byReference = new Variant.VARIANT.ByReference();
        this.oleMethod(1, byReference, string, vARIANTArray);
        return byReference;
    }

    protected Variant.VARIANT invoke(String string, Variant.VARIANT vARIANT, Variant.VARIANT vARIANT2) {
        return this.invoke(string, new Variant.VARIANT[]{vARIANT, vARIANT2});
    }

    protected Variant.VARIANT invoke(String string, Variant.VARIANT vARIANT, Variant.VARIANT vARIANT2, Variant.VARIANT vARIANT3) {
        return this.invoke(string, new Variant.VARIANT[]{vARIANT, vARIANT2, vARIANT3});
    }

    protected Variant.VARIANT invoke(String string, Variant.VARIANT vARIANT, Variant.VARIANT vARIANT2, Variant.VARIANT vARIANT3, Variant.VARIANT vARIANT4) {
        return this.invoke(string, new Variant.VARIANT[]{vARIANT, vARIANT2, vARIANT3, vARIANT4});
    }

    @Deprecated
    protected void invokeNoReply(String string, IDispatch iDispatch) {
        this.oleMethod(1, null, iDispatch, string);
    }

    @Deprecated
    protected void invokeNoReply(String string, COMLateBindingObject cOMLateBindingObject) {
        this.oleMethod(1, null, cOMLateBindingObject.getIDispatch(), string);
    }

    protected void invokeNoReply(String string, Variant.VARIANT vARIANT) {
        this.oleMethod(1, null, string, vARIANT);
    }

    @Deprecated
    protected void invokeNoReply(String string, IDispatch iDispatch, Variant.VARIANT vARIANT) {
        this.oleMethod(1, null, iDispatch, string, vARIANT);
    }

    @Deprecated
    protected void invokeNoReply(String string, IDispatch iDispatch, Variant.VARIANT vARIANT, Variant.VARIANT vARIANT2) {
        this.oleMethod(1, null, iDispatch, string, new Variant.VARIANT[]{vARIANT, vARIANT2});
    }

    @Deprecated
    protected void invokeNoReply(String string, COMLateBindingObject cOMLateBindingObject, Variant.VARIANT vARIANT, Variant.VARIANT vARIANT2) {
        this.oleMethod(1, null, cOMLateBindingObject.getIDispatch(), string, new Variant.VARIANT[]{vARIANT, vARIANT2});
    }

    @Deprecated
    protected void invokeNoReply(String string, COMLateBindingObject cOMLateBindingObject, Variant.VARIANT vARIANT) {
        this.oleMethod(1, null, cOMLateBindingObject.getIDispatch(), string, vARIANT);
    }

    @Deprecated
    protected void invokeNoReply(String string, IDispatch iDispatch, Variant.VARIANT[] vARIANTArray) {
        this.oleMethod(1, null, iDispatch, string, vARIANTArray);
    }

    protected void invokeNoReply(String string) {
        Variant.VARIANT.ByReference byReference = new Variant.VARIANT.ByReference();
        this.oleMethod(1, byReference, string);
    }

    protected void invokeNoReply(String string, Variant.VARIANT[] vARIANTArray) {
        Variant.VARIANT.ByReference byReference = new Variant.VARIANT.ByReference();
        this.oleMethod(1, byReference, string, vARIANTArray);
    }

    protected void invokeNoReply(String string, Variant.VARIANT vARIANT, Variant.VARIANT vARIANT2) {
        this.invokeNoReply(string, new Variant.VARIANT[]{vARIANT, vARIANT2});
    }

    protected void invokeNoReply(String string, Variant.VARIANT vARIANT, Variant.VARIANT vARIANT2, Variant.VARIANT vARIANT3) {
        this.invokeNoReply(string, new Variant.VARIANT[]{vARIANT, vARIANT2, vARIANT3});
    }

    protected void invokeNoReply(String string, Variant.VARIANT vARIANT, Variant.VARIANT vARIANT2, Variant.VARIANT vARIANT3, Variant.VARIANT vARIANT4) {
        this.invokeNoReply(string, new Variant.VARIANT[]{vARIANT, vARIANT2, vARIANT3, vARIANT4});
    }

    protected void setProperty(String string, boolean bl) {
        this.oleMethod(4, null, string, new Variant.VARIANT(bl));
    }

    protected void setProperty(String string, Date date) {
        this.oleMethod(4, null, string, new Variant.VARIANT(date));
    }

    protected void setProperty(String string, Dispatch dispatch) {
        this.oleMethod(4, null, string, new Variant.VARIANT(dispatch));
    }

    @Deprecated
    protected void setProperty(String string, IDispatch iDispatch) {
        this.oleMethod(4, null, string, new Variant.VARIANT(iDispatch));
    }

    protected void setProperty(String string, int n2) {
        this.oleMethod(4, null, string, new Variant.VARIANT(n2));
    }

    protected void setProperty(String string, short s2) {
        this.oleMethod(4, null, string, new Variant.VARIANT(s2));
    }

    protected void setProperty(String string, String object) {
        object = new Variant.VARIANT((String)object);
        try {
            this.oleMethod(4, null, string, (Variant.VARIANT)object);
            return;
        }
        finally {
            OleAuto.INSTANCE.VariantClear((Variant.VARIANT)object);
        }
    }

    protected void setProperty(String string, Variant.VARIANT vARIANT) {
        this.oleMethod(4, null, string, vARIANT);
    }

    @Deprecated
    protected void setProperty(String string, IDispatch iDispatch, Variant.VARIANT vARIANT) {
        this.oleMethod(4, null, iDispatch, string, vARIANT);
    }

    @Deprecated
    protected void setProperty(String string, COMLateBindingObject cOMLateBindingObject, Variant.VARIANT vARIANT) {
        this.oleMethod(4, null, cOMLateBindingObject.getIDispatch(), string, vARIANT);
    }

    public Variant.VARIANT toVariant() {
        return new Variant.VARIANT(this.getIDispatch());
    }
}


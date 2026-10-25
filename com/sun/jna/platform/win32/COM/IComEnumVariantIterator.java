/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32.COM;

import com.sun.jna.platform.win32.COM.EnumVariant;
import com.sun.jna.platform.win32.COM.IUnknown;
import com.sun.jna.platform.win32.COM.util.IDispatch;
import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.ptr.PointerByReference;
import java.io.Closeable;
import java.util.Iterator;

public class IComEnumVariantIterator
implements Closeable,
Iterable<Variant.VARIANT>,
Iterator<Variant.VARIANT> {
    private Variant.VARIANT nextValue;
    private EnumVariant backingIteration;

    public static IComEnumVariantIterator wrap(IDispatch object) {
        PointerByReference pointerByReference = new PointerByReference();
        object = object.getProperty(IUnknown.class, OaIdl.DISPID_NEWENUM, new Object[0]);
        object.QueryInterface(EnumVariant.REFIID, pointerByReference);
        object.Release();
        object = new EnumVariant(pointerByReference.getValue());
        return new IComEnumVariantIterator((EnumVariant)object);
    }

    public IComEnumVariantIterator(EnumVariant enumVariant) {
        this.backingIteration = enumVariant;
        this.retrieveNext();
    }

    @Override
    public boolean hasNext() {
        return this.nextValue != null;
    }

    @Override
    public Variant.VARIANT next() {
        Variant.VARIANT vARIANT = this.nextValue;
        this.retrieveNext();
        return vARIANT;
    }

    private void retrieveNext() {
        if (this.backingIteration == null) {
            return;
        }
        Variant.VARIANT[] vARIANTArray = this.backingIteration.Next(1);
        if (vARIANTArray.length == 0) {
            this.close();
            return;
        }
        this.nextValue = vARIANTArray[0];
    }

    @Override
    public void close() {
        if (this.backingIteration != null) {
            this.nextValue = null;
            this.backingIteration.Release();
            this.backingIteration = null;
        }
    }

    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }

    @Override
    public Iterator<Variant.VARIANT> iterator() {
        return this;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("remove");
    }
}


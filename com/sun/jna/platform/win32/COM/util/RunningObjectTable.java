/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32.COM.util;

import com.sun.jna.PointerType;
import com.sun.jna.platform.win32.COM.COMException;
import com.sun.jna.platform.win32.COM.COMUtils;
import com.sun.jna.platform.win32.COM.IEnumMoniker;
import com.sun.jna.platform.win32.COM.util.EnumMoniker;
import com.sun.jna.platform.win32.COM.util.IDispatch;
import com.sun.jna.platform.win32.COM.util.IRunningObjectTable;
import com.sun.jna.platform.win32.COM.util.ObjectFactory;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;
import java.util.ArrayList;
import java.util.List;

public class RunningObjectTable
implements IRunningObjectTable {
    ObjectFactory factory;
    com.sun.jna.platform.win32.COM.RunningObjectTable raw;

    protected RunningObjectTable(com.sun.jna.platform.win32.COM.RunningObjectTable runningObjectTable, ObjectFactory objectFactory) {
        this.raw = runningObjectTable;
        this.factory = objectFactory;
    }

    @Override
    public Iterable<IDispatch> enumRunning() {
        assert (COMUtils.comIsInitialized()) : "COM not initialized";
        PointerType pointerType = new PointerByReference();
        WinNT.HRESULT hRESULT = this.raw.EnumRunning((PointerByReference)pointerType);
        COMUtils.checkRC(hRESULT);
        pointerType = new com.sun.jna.platform.win32.COM.EnumMoniker(pointerType.getValue());
        return new EnumMoniker((IEnumMoniker)((Object)pointerType), this.raw, this.factory);
    }

    @Override
    public <T> List<T> getActiveObjectsByInterface(Class<T> clazz) {
        assert (COMUtils.comIsInitialized()) : "COM not initialized";
        ArrayList<IDispatch> arrayList = new ArrayList<IDispatch>();
        for (IDispatch iDispatch : this.enumRunning()) {
            try {
                iDispatch = iDispatch.queryInterface(clazz);
                arrayList.add(iDispatch);
            }
            catch (COMException cOMException) {}
        }
        return arrayList;
    }
}


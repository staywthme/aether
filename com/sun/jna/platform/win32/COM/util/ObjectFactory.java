/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32.COM.util;

import com.sun.jna.platform.win32.COM.COMException;
import com.sun.jna.platform.win32.COM.COMUtils;
import com.sun.jna.platform.win32.COM.Dispatch;
import com.sun.jna.platform.win32.COM.IDispatch;
import com.sun.jna.platform.win32.COM.IDispatchCallback;
import com.sun.jna.platform.win32.COM.Unknown;
import com.sun.jna.platform.win32.COM.util.CallbackProxy;
import com.sun.jna.platform.win32.COM.util.IComEventCallbackListener;
import com.sun.jna.platform.win32.COM.util.IRunningObjectTable;
import com.sun.jna.platform.win32.COM.util.ProxyObject;
import com.sun.jna.platform.win32.COM.util.RunningObjectTable;
import com.sun.jna.platform.win32.COM.util.annotation.ComObject;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Ole32;
import com.sun.jna.platform.win32.OleAuto;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ObjectFactory {
    private final List<WeakReference<ProxyObject>> registeredObjects = new LinkedList<WeakReference<ProxyObject>>();
    private static final WinDef.LCID LOCALE_USER_DEFAULT = Kernel32.INSTANCE.GetUserDefaultLCID();
    private WinDef.LCID LCID;

    protected void finalize() throws Throwable {
        try {
            this.disposeAll();
            return;
        }
        finally {
            super.finalize();
        }
    }

    public IRunningObjectTable getRunningObjectTable() {
        assert (COMUtils.comIsInitialized()) : "COM not initialized";
        Object object = new PointerByReference();
        WinNT.HRESULT hRESULT = Ole32.INSTANCE.GetRunningObjectTable(new WinDef.DWORD(0L), (PointerByReference)object);
        COMUtils.checkRC(hRESULT);
        object = new com.sun.jna.platform.win32.COM.RunningObjectTable(((PointerByReference)object).getValue());
        object = new RunningObjectTable((com.sun.jna.platform.win32.COM.RunningObjectTable)object, this);
        return object;
    }

    public <T> T createProxy(Class<T> clazz, IDispatch object) {
        assert (COMUtils.comIsInitialized()) : "COM not initialized";
        object = new ProxyObject(clazz, (IDispatch)object, this);
        object = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, (InvocationHandler)object);
        clazz = clazz.cast(object);
        return (T)clazz;
    }

    public <T> T createObject(Class<T> clazz) {
        assert (COMUtils.comIsInitialized()) : "COM not initialized";
        Object object = clazz.getAnnotation(ComObject.class);
        if (object == null) {
            throw new COMException("createObject: Interface must define a value for either clsId or progId via the ComInterface annotation");
        }
        object = this.discoverClsId((ComObject)object);
        PointerByReference pointerByReference = new PointerByReference();
        object = Ole32.INSTANCE.CoCreateInstance((Guid.GUID)object, null, 21, IDispatch.IID_IDISPATCH, pointerByReference);
        COMUtils.checkRC((WinNT.HRESULT)object);
        object = new Dispatch(pointerByReference.getValue());
        clazz = this.createProxy(clazz, (IDispatch)object);
        ((Unknown)object).Release();
        return (T)clazz;
    }

    public <T> T fetchObject(Class<T> clazz) throws COMException {
        assert (COMUtils.comIsInitialized()) : "COM not initialized";
        Object object = clazz.getAnnotation(ComObject.class);
        if (object == null) {
            throw new COMException("createObject: Interface must define a value for either clsId or progId via the ComInterface annotation");
        }
        object = this.discoverClsId((ComObject)object);
        PointerByReference pointerByReference = new PointerByReference();
        object = OleAuto.INSTANCE.GetActiveObject((Guid.GUID)object, null, pointerByReference);
        COMUtils.checkRC((WinNT.HRESULT)object);
        object = new Dispatch(pointerByReference.getValue());
        clazz = this.createProxy(clazz, (IDispatch)object);
        ((Unknown)object).Release();
        return (T)clazz;
    }

    Guid.GUID discoverClsId(ComObject object) {
        assert (COMUtils.comIsInitialized()) : "COM not initialized";
        Object object2 = object.clsId();
        object = object.progId();
        if (object2 != null && !((String)object2).isEmpty()) {
            return new Guid.CLSID((String)object2);
        }
        if (object != null && !((String)object).isEmpty()) {
            object2 = new Guid.CLSID.ByReference();
            object = Ole32.INSTANCE.CLSIDFromProgID((String)object, (Guid.CLSID.ByReference)object2);
            COMUtils.checkRC((WinNT.HRESULT)object);
            return object2;
        }
        throw new COMException("ComObject must define a value for either clsId or progId");
    }

    IDispatchCallback createDispatchCallback(Class<?> clazz, IComEventCallbackListener iComEventCallbackListener) {
        return new CallbackProxy(this, clazz, iComEventCallbackListener);
    }

    public void register(ProxyObject proxyObject) {
        List<WeakReference<ProxyObject>> list2 = this.registeredObjects;
        synchronized (list2) {
            this.registeredObjects.add(new WeakReference<ProxyObject>(proxyObject));
            return;
        }
    }

    public void unregister(ProxyObject proxyObject) {
        List<WeakReference<ProxyObject>> list2 = this.registeredObjects;
        synchronized (list2) {
            Iterator<WeakReference<ProxyObject>> iterator2 = this.registeredObjects.iterator();
            while (iterator2.hasNext()) {
                Object object = iterator2.next();
                if ((object = (ProxyObject)((Reference)object).get()) != null && object != proxyObject) continue;
                iterator2.remove();
            }
            return;
        }
    }

    public void disposeAll() {
        List<WeakReference<ProxyObject>> list2 = this.registeredObjects;
        synchronized (list2) {
            Object object = new ArrayList<WeakReference<ProxyObject>>(this.registeredObjects);
            object = object.iterator();
            while (object.hasNext()) {
                Object object2 = (WeakReference)object.next();
                if ((object2 = (ProxyObject)((Reference)object2).get()) == null) continue;
                ((ProxyObject)object2).dispose();
            }
            this.registeredObjects.clear();
            return;
        }
    }

    public WinDef.LCID getLCID() {
        if (this.LCID != null) {
            return this.LCID;
        }
        return LOCALE_USER_DEFAULT;
    }

    public void setLCID(WinDef.LCID lCID) {
        this.LCID = lCID;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32.COM.util;

import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import com.sun.jna.Structure;
import com.sun.jna.WString;
import com.sun.jna.internal.ReflectionUtils;
import com.sun.jna.platform.win32.COM.COMException;
import com.sun.jna.platform.win32.COM.COMUtils;
import com.sun.jna.platform.win32.COM.ConnectionPoint;
import com.sun.jna.platform.win32.COM.ConnectionPointContainer;
import com.sun.jna.platform.win32.COM.Dispatch;
import com.sun.jna.platform.win32.COM.IDispatch;
import com.sun.jna.platform.win32.COM.IDispatchCallback;
import com.sun.jna.platform.win32.COM.IUnknownCallback;
import com.sun.jna.platform.win32.COM.Unknown;
import com.sun.jna.platform.win32.COM.util.ComEventCallbackCookie;
import com.sun.jna.platform.win32.COM.util.Convert;
import com.sun.jna.platform.win32.COM.util.IComEventCallbackCookie;
import com.sun.jna.platform.win32.COM.util.IComEventCallbackListener;
import com.sun.jna.platform.win32.COM.util.IConnectionPoint;
import com.sun.jna.platform.win32.COM.util.IConnectionPointContainer;
import com.sun.jna.platform.win32.COM.util.IRawDispatchHandle;
import com.sun.jna.platform.win32.COM.util.IUnknown;
import com.sun.jna.platform.win32.COM.util.ObjectFactory;
import com.sun.jna.platform.win32.COM.util.annotation.ComInterface;
import com.sun.jna.platform.win32.COM.util.annotation.ComMethod;
import com.sun.jna.platform.win32.COM.util.annotation.ComProperty;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.Kernel32Util;
import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.platform.win32.OleAuto;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyObject
implements IConnectionPoint,
com.sun.jna.platform.win32.COM.util.IDispatch,
IRawDispatchHandle,
InvocationHandler {
    private long unknownId = -1L;
    private final Class<?> theInterface;
    private final ObjectFactory factory;
    private final IDispatch rawDispatch;

    public ProxyObject(Class<?> clazz, IDispatch iDispatch, ObjectFactory objectFactory) {
        this.rawDispatch = iDispatch;
        this.theInterface = clazz;
        this.factory = objectFactory;
        this.rawDispatch.AddRef();
        this.getUnknownId();
        objectFactory.register(this);
    }

    private long getUnknownId() {
        assert (COMUtils.comIsInitialized()) : "COM not initialized";
        if (-1L == this.unknownId) {
            try {
                Object object = new PointerByReference();
                Object object2 = Thread.currentThread();
                ((Thread)object2).getName();
                object2 = com.sun.jna.platform.win32.COM.IUnknown.IID_IUNKNOWN;
                object2 = this.getRawDispatch().QueryInterface(new Guid.REFIID((Guid.IID)object2), (PointerByReference)object);
                if (!WinNT.S_OK.equals(object2)) {
                    object = Kernel32Util.formatMessage((WinNT.HRESULT)object2);
                    throw new COMException("getUnknownId: " + (String)object, (WinNT.HRESULT)object2);
                }
                object = new Dispatch(((PointerByReference)object).getValue());
                this.unknownId = Pointer.nativeValue(((PointerType)object).getPointer());
                ((Unknown)object).Release();
            }
            catch (RuntimeException runtimeException) {
                RuntimeException runtimeException2 = runtimeException;
                if (runtimeException instanceof COMException) {
                    throw runtimeException2;
                }
                throw new COMException("Error occured when trying get Unknown Id ", runtimeException2);
            }
        }
        return this.unknownId;
    }

    protected void finalize() throws Throwable {
        this.dispose();
        super.finalize();
    }

    public synchronized void dispose() {
        if (((Dispatch)this.rawDispatch).getPointer() != Pointer.NULL) {
            this.rawDispatch.Release();
            ((Dispatch)this.rawDispatch).setPointer(Pointer.NULL);
            this.factory.unregister(this);
        }
    }

    @Override
    public IDispatch getRawDispatch() {
        return this.rawDispatch;
    }

    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object instanceof ProxyObject) {
            object = (ProxyObject)object;
            return this.getUnknownId() == super.getUnknownId();
        }
        if (Proxy.isProxyClass(object.getClass())) {
            if ((object = Proxy.getInvocationHandler(object)) instanceof ProxyObject) {
                try {
                    object = (ProxyObject)object;
                    return this.getUnknownId() == super.getUnknownId();
                }
                catch (Exception exception) {
                    return false;
                }
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        long l2 = this.getUnknownId();
        return (int)((l2 >>> 32) + (l2 & 0xFFFFFFFFL));
    }

    public String toString() {
        return this.theInterface.getName() + "{unk=" + this.hashCode() + "}";
    }

    @Override
    public Object invoke(Object object, Method method, Object[] objectArray) throws Throwable {
        boolean bl = method.getAnnotation(ComMethod.class) != null || method.getAnnotation(ComProperty.class) != null;
        if (!bl && (method.getDeclaringClass().equals(Object.class) || method.getDeclaringClass().equals(IRawDispatchHandle.class) || method.getDeclaringClass().equals(IUnknown.class) || method.getDeclaringClass().equals(com.sun.jna.platform.win32.COM.util.IDispatch.class) || method.getDeclaringClass().equals(IConnectionPoint.class))) {
            try {
                return method.invoke(this, objectArray);
            }
            catch (InvocationTargetException invocationTargetException) {
                InvocationTargetException invocationTargetException2 = invocationTargetException;
                throw invocationTargetException.getCause();
            }
        }
        if (!bl && ReflectionUtils.isDefault(method)) {
            Object object2 = ReflectionUtils.getMethodHandle(method);
            return ReflectionUtils.invokeDefaultMethod(object, object2, objectArray);
        }
        Class<?> clazz = method.getReturnType();
        int n2 = Void.TYPE.equals(clazz);
        ComProperty comProperty = method.getAnnotation(ComProperty.class);
        if (comProperty != null) {
            int n3 = comProperty.dispId();
            Object[] objectArray2 = this.unfoldWhenVarargs(method, objectArray);
            if (n2 != 0) {
                if (n3 != -1) {
                    this.setProperty(new OaIdl.DISPID(n3), objectArray2);
                    return null;
                }
                String string = this.getMutatorName(method, comProperty);
                this.setProperty(string, objectArray2);
                return null;
            }
            if (n3 != -1) {
                return this.getProperty(clazz, new OaIdl.DISPID(n3), objectArray);
            }
            String string = this.getAccessorName(method, comProperty);
            return this.getProperty(clazz, string, objectArray);
        }
        ComMethod comMethod = method.getAnnotation(ComMethod.class);
        if (comMethod != null) {
            Object[] objectArray3 = this.unfoldWhenVarargs(method, objectArray);
            n2 = comMethod.dispId();
            if (n2 != -1) {
                return this.invokeMethod(clazz, new OaIdl.DISPID(n2), objectArray3);
            }
            String string = this.getMethodName(method, comMethod);
            return this.invokeMethod(clazz, string, objectArray3);
        }
        return null;
    }

    private ConnectionPoint fetchRawConnectionPoint(Guid.IID object) {
        assert (COMUtils.comIsInitialized()) : "COM not initialized";
        Object object2 = this.queryInterface(IConnectionPointContainer.class);
        object2 = (Dispatch)object2.getRawDispatch();
        object2 = new ConnectionPointContainer(((PointerType)object2).getPointer());
        object = new Guid.REFIID(((Structure)object).getPointer());
        PointerByReference pointerByReference = new PointerByReference();
        object = ((ConnectionPointContainer)object2).FindConnectionPoint((Guid.REFIID)object, pointerByReference);
        COMUtils.checkRC((WinNT.HRESULT)object);
        object = new ConnectionPoint(pointerByReference.getValue());
        return object;
    }

    @Override
    public IComEventCallbackCookie advise(Class<?> object, IComEventCallbackListener iComEventCallbackListener) throws COMException {
        assert (COMUtils.comIsInitialized()) : "COM not initialized";
        try {
            Object object2 = ((Class)object).getAnnotation(ComInterface.class);
            if (object2 == null) {
                throw new COMException("advise: Interface must define a value for either iid via the ComInterface annotation");
            }
            object2 = this.getIID((ComInterface)object2);
            object2 = this.fetchRawConnectionPoint((Guid.IID)object2);
            object = this.factory.createDispatchCallback((Class<?>)object, iComEventCallbackListener);
            iComEventCallbackListener.setDispatchCallbackListener((IDispatchCallback)object);
            WinDef.DWORDByReference dWORDByReference = new WinDef.DWORDByReference();
            object = ((ConnectionPoint)object2).Advise((IUnknownCallback)object, dWORDByReference);
            ((Unknown)object2).Release();
            COMUtils.checkRC((WinNT.HRESULT)object);
            return new ComEventCallbackCookie(dWORDByReference.getValue());
        }
        catch (RuntimeException runtimeException) {
            RuntimeException runtimeException2 = runtimeException;
            if (runtimeException instanceof COMException) {
                throw runtimeException2;
            }
            throw new COMException("Error occured in advise when trying to connect the listener " + iComEventCallbackListener, runtimeException2);
        }
    }

    @Override
    public void unadvise(Class<?> object, IComEventCallbackCookie object2) throws COMException {
        assert (COMUtils.comIsInitialized()) : "COM not initialized";
        try {
            object = ((Class)object).getAnnotation(ComInterface.class);
            if (object == null) {
                throw new COMException("unadvise: Interface must define a value for iid via the ComInterface annotation");
            }
            object = this.getIID((ComInterface)object);
            object = this.fetchRawConnectionPoint((Guid.IID)object);
            object2 = ((ConnectionPoint)object).Unadvise(((ComEventCallbackCookie)object2).getValue());
            ((Unknown)object).Release();
            COMUtils.checkRC((WinNT.HRESULT)object2);
            return;
        }
        catch (RuntimeException runtimeException) {
            object = runtimeException;
            if (runtimeException instanceof COMException) {
                throw object;
            }
            throw new COMException("Error occured in unadvise when trying to disconnect the listener from " + this, (Throwable)object);
        }
    }

    @Override
    public <T> void setProperty(String object, T t2) {
        ProxyObject proxyObject = this;
        object = proxyObject.resolveDispId(proxyObject.getRawDispatch(), (String)object);
        this.setProperty((OaIdl.DISPID)object, t2);
    }

    @Override
    public <T> void setProperty(OaIdl.DISPID integerType, T t2) {
        assert (COMUtils.comIsInitialized()) : "COM not initialized";
        Variant.VARIANT vARIANT = Convert.toVariant(t2);
        integerType = this.oleMethod(4, null, this.getRawDispatch(), (OaIdl.DISPID)integerType, vARIANT);
        Convert.free(vARIANT, t2);
        COMUtils.checkRC((WinNT.HRESULT)integerType);
    }

    private void setProperty(String object, Object ... objectArray) {
        assert (COMUtils.comIsInitialized()) : "COM not initialized";
        ProxyObject proxyObject = this;
        object = proxyObject.resolveDispId(proxyObject.getRawDispatch(), (String)object);
        this.setProperty((OaIdl.DISPID)object, objectArray);
    }

    private void setProperty(OaIdl.DISPID dISPID, Object ... objectArray) {
        assert (COMUtils.comIsInitialized()) : "COM not initialized";
        Variant.VARIANT[] vARIANTArray = objectArray == null ? new Variant.VARIANT[]{} : new Variant.VARIANT[objectArray.length];
        for (int i2 = 0; i2 < vARIANTArray.length; ++i2) {
            vARIANTArray[i2] = Convert.toVariant(objectArray[i2]);
        }
        WinNT.HRESULT hRESULT = this.oleMethod(4, null, this.getRawDispatch(), dISPID, vARIANTArray);
        for (int i3 = 0; i3 < vARIANTArray.length; ++i3) {
            Convert.free(vARIANTArray[i3], objectArray[i3]);
        }
        COMUtils.checkRC(hRESULT);
    }

    @Override
    public <T> T getProperty(Class<T> clazz, String object, Object ... objectArray) {
        ProxyObject proxyObject = this;
        object = proxyObject.resolveDispId(proxyObject.getRawDispatch(), (String)object);
        return this.getProperty(clazz, (OaIdl.DISPID)object, objectArray);
    }

    @Override
    public <T> T getProperty(Class<T> clazz, OaIdl.DISPID integerType, Object ... objectArray) {
        Variant.VARIANT[] vARIANTArray = objectArray == null ? new Variant.VARIANT[]{} : new Variant.VARIANT[objectArray.length];
        for (int i2 = 0; i2 < vARIANTArray.length; ++i2) {
            vARIANTArray[i2] = Convert.toVariant(objectArray[i2]);
        }
        Variant.VARIANT.ByReference byReference = new Variant.VARIANT.ByReference();
        integerType = this.oleMethod(2, byReference, this.getRawDispatch(), (OaIdl.DISPID)integerType, vARIANTArray);
        for (int i3 = 0; i3 < vARIANTArray.length; ++i3) {
            Convert.free(vARIANTArray[i3], objectArray[i3]);
        }
        COMUtils.checkRC((WinNT.HRESULT)integerType);
        return (T)Convert.toJavaObject(byReference, clazz, this.factory, false, true);
    }

    @Override
    public <T> T invokeMethod(Class<T> clazz, String object, Object ... objectArray) {
        ProxyObject proxyObject = this;
        object = proxyObject.resolveDispId(proxyObject.getRawDispatch(), (String)object);
        return this.invokeMethod(clazz, (OaIdl.DISPID)object, objectArray);
    }

    @Override
    public <T> T invokeMethod(Class<T> clazz, OaIdl.DISPID integerType, Object ... objectArray) {
        assert (COMUtils.comIsInitialized()) : "COM not initialized";
        Variant.VARIANT[] vARIANTArray = objectArray == null ? new Variant.VARIANT[]{} : new Variant.VARIANT[objectArray.length];
        for (int i2 = 0; i2 < vARIANTArray.length; ++i2) {
            vARIANTArray[i2] = Convert.toVariant(objectArray[i2]);
        }
        Variant.VARIANT.ByReference byReference = new Variant.VARIANT.ByReference();
        integerType = this.oleMethod(1, byReference, this.getRawDispatch(), (OaIdl.DISPID)integerType, vARIANTArray);
        for (int i3 = 0; i3 < vARIANTArray.length; ++i3) {
            Convert.free(vARIANTArray[i3], objectArray[i3]);
        }
        COMUtils.checkRC((WinNT.HRESULT)integerType);
        return (T)Convert.toJavaObject(byReference, clazz, this.factory, false, true);
    }

    private Object[] unfoldWhenVarargs(Method objectArray, Object[] objectArray2) {
        if (objectArray2 == null) {
            return null;
        }
        if (objectArray2.length == 0 || !objectArray.isVarArgs() || !(objectArray2[objectArray2.length - 1] instanceof Object[])) {
            return objectArray2;
        }
        objectArray = (Object[])objectArray2[objectArray2.length - 1];
        Object[] objectArray3 = new Object[objectArray2.length - 1 + objectArray.length];
        System.arraycopy(objectArray2, 0, objectArray3, 0, objectArray2.length - 1);
        System.arraycopy(objectArray, 0, objectArray3, objectArray2.length - 1, objectArray.length);
        return objectArray3;
    }

    @Override
    public <T> T queryInterface(Class<T> clazz) throws COMException {
        assert (COMUtils.comIsInitialized()) : "COM not initialized";
        try {
            Object object = clazz.getAnnotation(ComInterface.class);
            if (object == null) {
                throw new COMException("queryInterface: Interface must define a value for iid via the ComInterface annotation");
            }
            object = this.getIID((ComInterface)object);
            Object object2 = new PointerByReference();
            object = this.getRawDispatch().QueryInterface(new Guid.REFIID((Guid.IID)object), (PointerByReference)object2);
            if (WinNT.S_OK.equals(object)) {
                object2 = new Dispatch(((PointerByReference)object2).getValue());
                object = this.factory.createProxy(clazz, (IDispatch)object2);
                ((Unknown)object2).Release();
                return (T)object;
            }
            object2 = Kernel32Util.formatMessage((WinNT.HRESULT)object);
            throw new COMException("queryInterface: " + (String)object2, (WinNT.HRESULT)object);
        }
        catch (RuntimeException runtimeException) {
            RuntimeException runtimeException2 = runtimeException;
            if (runtimeException instanceof COMException) {
                throw runtimeException2;
            }
            throw new COMException("Error occured when trying to query for interface " + clazz.getName(), runtimeException2);
        }
    }

    private Guid.IID getIID(ComInterface object) {
        if ((object = object.iid()) != null && !((String)object).isEmpty()) {
            return new Guid.IID((String)object);
        }
        throw new COMException("ComInterface must define a value for iid");
    }

    private String getAccessorName(Method object, ComProperty comProperty) {
        if (comProperty.name().isEmpty()) {
            if (((String)(object = ((Method)object).getName())).startsWith("get")) {
                return ((String)object).replaceFirst("get", "");
            }
            throw new RuntimeException("Property Accessor name must start with 'get', or set the anotation 'name' value");
        }
        return comProperty.name();
    }

    private String getMutatorName(Method object, ComProperty comProperty) {
        if (comProperty.name().isEmpty()) {
            if (((String)(object = ((Method)object).getName())).startsWith("set")) {
                return ((String)object).replaceFirst("set", "");
            }
            throw new RuntimeException("Property Mutator name must start with 'set', or set the anotation 'name' value");
        }
        return comProperty.name();
    }

    private String getMethodName(Method object, ComMethod comMethod) {
        if (comMethod.name().isEmpty()) {
            object = ((Method)object).getName();
            return object;
        }
        return comMethod.name();
    }

    protected OaIdl.DISPID resolveDispId(String string) {
        ProxyObject proxyObject = this;
        return proxyObject.resolveDispId(proxyObject.getRawDispatch(), string);
    }

    protected WinNT.HRESULT oleMethod(int n2, Variant.VARIANT.ByReference byReference, String string, Variant.VARIANT vARIANT) throws COMException {
        return this.oleMethod(n2, byReference, string, new Variant.VARIANT[]{vARIANT});
    }

    protected WinNT.HRESULT oleMethod(int n2, Variant.VARIANT.ByReference byReference, OaIdl.DISPID dISPID, Variant.VARIANT vARIANT) throws COMException {
        return this.oleMethod(n2, byReference, dISPID, new Variant.VARIANT[]{vARIANT});
    }

    protected WinNT.HRESULT oleMethod(int n2, Variant.VARIANT.ByReference byReference, String string) throws COMException {
        return this.oleMethod(n2, byReference, string, (Variant.VARIANT[])null);
    }

    protected WinNT.HRESULT oleMethod(int n2, Variant.VARIANT.ByReference byReference, OaIdl.DISPID dISPID) throws COMException {
        return this.oleMethod(n2, byReference, dISPID, (Variant.VARIANT[])null);
    }

    protected WinNT.HRESULT oleMethod(int n2, Variant.VARIANT.ByReference byReference, String string, Variant.VARIANT[] vARIANTArray) throws COMException {
        return this.oleMethod(n2, byReference, this.resolveDispId(string), vARIANTArray);
    }

    protected WinNT.HRESULT oleMethod(int n2, Variant.VARIANT.ByReference byReference, OaIdl.DISPID dISPID, Variant.VARIANT[] vARIANTArray) throws COMException {
        return this.oleMethod(n2, byReference, this.getRawDispatch(), dISPID, vARIANTArray);
    }

    @Deprecated
    protected OaIdl.DISPID resolveDispId(IDispatch object, String wStringArray) {
        assert (COMUtils.comIsInitialized()) : "COM not initialized";
        if (object == null) {
            throw new COMException("pDisp (IDispatch) parameter is null!");
        }
        wStringArray = new WString[]{new WString((String)wStringArray)};
        OaIdl.DISPIDByReference dISPIDByReference = new OaIdl.DISPIDByReference();
        object = object.GetIDsOfNames(new Guid.REFIID(Guid.IID_NULL), wStringArray, 1, this.factory.getLCID(), dISPIDByReference);
        COMUtils.checkRC((WinNT.HRESULT)object);
        return dISPIDByReference.getValue();
    }

    @Deprecated
    protected WinNT.HRESULT oleMethod(int n2, Variant.VARIANT.ByReference byReference, IDispatch iDispatch, String string, Variant.VARIANT vARIANT) throws COMException {
        return this.oleMethod(n2, byReference, iDispatch, string, new Variant.VARIANT[]{vARIANT});
    }

    @Deprecated
    protected WinNT.HRESULT oleMethod(int n2, Variant.VARIANT.ByReference byReference, IDispatch iDispatch, OaIdl.DISPID dISPID, Variant.VARIANT vARIANT) throws COMException {
        return this.oleMethod(n2, byReference, iDispatch, dISPID, new Variant.VARIANT[]{vARIANT});
    }

    @Deprecated
    protected WinNT.HRESULT oleMethod(int n2, Variant.VARIANT.ByReference byReference, IDispatch iDispatch, String string) throws COMException {
        return this.oleMethod(n2, byReference, iDispatch, string, (Variant.VARIANT[])null);
    }

    @Deprecated
    protected WinNT.HRESULT oleMethod(int n2, Variant.VARIANT.ByReference byReference, IDispatch iDispatch, OaIdl.DISPID dISPID) throws COMException {
        return this.oleMethod(n2, byReference, iDispatch, dISPID, (Variant.VARIANT[])null);
    }

    @Deprecated
    protected WinNT.HRESULT oleMethod(int n2, Variant.VARIANT.ByReference byReference, IDispatch iDispatch, String string, Variant.VARIANT[] vARIANTArray) throws COMException {
        return this.oleMethod(n2, byReference, iDispatch, this.resolveDispId(iDispatch, string), vARIANTArray);
    }

    @Deprecated
    protected WinNT.HRESULT oleMethod(int n2, Variant.VARIANT.ByReference byReference, IDispatch iDispatch, OaIdl.DISPID dISPID, Variant.VARIANT[] vARIANTArray) throws COMException {
        int n3;
        assert (COMUtils.comIsInitialized()) : "COM not initialized";
        if (iDispatch == null) {
            throw new COMException("pDisp (IDispatch) parameter is null!");
        }
        int n4 = 0;
        Variant.VARIANT[] vARIANTArray2 = null;
        OleAuto.DISPPARAMS.ByReference byReference2 = new OleAuto.DISPPARAMS.ByReference();
        OaIdl.EXCEPINFO.ByReference byReference3 = new OaIdl.EXCEPINFO.ByReference();
        IntByReference intByReference = new IntByReference();
        if (vARIANTArray != null && vARIANTArray.length > 0) {
            n4 = vARIANTArray.length;
            vARIANTArray2 = new Variant.VARIANT[n4];
            n3 = n4;
            for (int i2 = 0; i2 < n4; ++i2) {
                vARIANTArray2[i2] = vARIANTArray[--n3];
            }
        }
        if (n2 == 4) {
            byReference2.setRgdispidNamedArgs(new OaIdl.DISPID[]{OaIdl.DISPID_PROPERTYPUT});
        }
        n3 = n2 == 1 || n2 == 2 ? 3 : n2;
        if (n4 > 0) {
            byReference2.setArgs(vARIANTArray2);
            byReference2.write();
        }
        WinNT.HRESULT hRESULT = iDispatch.Invoke(dISPID, new Guid.REFIID(Guid.IID_NULL), this.factory.getLCID(), new WinDef.WORD((long)n3), byReference2, byReference, byReference3, intByReference);
        COMUtils.checkRC(hRESULT, byReference3, intByReference);
        return hRESULT;
    }
}


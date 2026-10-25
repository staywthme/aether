/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32.COM.util;

import com.sun.jna.platform.win32.COM.COMException;
import com.sun.jna.platform.win32.COM.IDispatch;
import com.sun.jna.platform.win32.COM.IDispatchCallback;
import com.sun.jna.platform.win32.COM.util.CallbackProxy;
import com.sun.jna.platform.win32.COM.util.ComThread;
import com.sun.jna.platform.win32.COM.util.IComEventCallbackListener;
import com.sun.jna.platform.win32.COM.util.IRunningObjectTable;
import com.sun.jna.platform.win32.COM.util.ObjectFactory;
import com.sun.jna.platform.win32.COM.util.annotation.ComObject;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.platform.win32.OleAuto;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class Factory
extends ObjectFactory {
    private ComThread comThread;

    public Factory() {
        this(new ComThread("Default Factory COM Thread", 5000L, new Thread.UncaughtExceptionHandler(){

            @Override
            public void uncaughtException(Thread thread2, Throwable throwable) {
            }
        }));
    }

    public Factory(ComThread comThread) {
        this.comThread = comThread;
    }

    @Override
    public <T> T createProxy(Class<T> object, IDispatch object2) {
        object2 = super.createProxy(object, (IDispatch)object2);
        object2 = new ProxyObject2(object2);
        object = Proxy.newProxyInstance(((Class)object).getClassLoader(), new Class[]{object}, (InvocationHandler)object2);
        return (T)object;
    }

    @Override
    Guid.GUID discoverClsId(final ComObject comObject) {
        return this.runInComThread(new Callable<Guid.GUID>(){

            @Override
            public Guid.GUID call() throws Exception {
                return Factory.super.discoverClsId(comObject);
            }
        });
    }

    @Override
    public <T> T fetchObject(final Class<T> clazz) throws COMException {
        return this.runInComThread(new Callable<T>(){

            @Override
            public T call() throws Exception {
                return Factory.super.fetchObject(clazz);
            }
        });
    }

    @Override
    public <T> T createObject(final Class<T> clazz) {
        return this.runInComThread(new Callable<T>(){

            @Override
            public T call() throws Exception {
                return Factory.super.createObject(clazz);
            }
        });
    }

    @Override
    IDispatchCallback createDispatchCallback(Class<?> clazz, IComEventCallbackListener iComEventCallbackListener) {
        Factory factory2 = this;
        return factory2.new CallbackProxy2(factory2, clazz, iComEventCallbackListener);
    }

    @Override
    public IRunningObjectTable getRunningObjectTable() {
        return super.getRunningObjectTable();
    }

    private <T> T runInComThread(Callable<T> object) {
        try {
            return this.comThread.execute(object);
        }
        catch (InterruptedException | TimeoutException exception) {
            throw new RuntimeException(exception);
        }
        catch (ExecutionException executionException) {
            object = executionException;
            Throwable throwable = executionException.getCause();
            if (throwable instanceof RuntimeException) {
                Factory.appendStacktrace((Exception)object, throwable);
                throw (RuntimeException)throwable;
            }
            if (throwable instanceof InvocationTargetException && (throwable = ((InvocationTargetException)throwable).getTargetException()) instanceof RuntimeException) {
                Factory.appendStacktrace((Exception)object, throwable);
                throw (RuntimeException)throwable;
            }
            throw new RuntimeException((Throwable)object);
        }
    }

    private static void appendStacktrace(Exception stackTraceElementArray, Throwable throwable) {
        stackTraceElementArray = stackTraceElementArray.getStackTrace();
        StackTraceElement[] stackTraceElementArray2 = throwable.getStackTrace();
        StackTraceElement[] stackTraceElementArray3 = new StackTraceElement[stackTraceElementArray.length + stackTraceElementArray2.length];
        System.arraycopy(stackTraceElementArray, 0, stackTraceElementArray3, stackTraceElementArray2.length, stackTraceElementArray.length);
        System.arraycopy(stackTraceElementArray2, 0, stackTraceElementArray3, 0, stackTraceElementArray2.length);
        throwable.setStackTrace(stackTraceElementArray3);
    }

    public ComThread getComThread() {
        return this.comThread;
    }

    class CallbackProxy2
    extends CallbackProxy {
        public CallbackProxy2(ObjectFactory objectFactory, Class<?> clazz, IComEventCallbackListener iComEventCallbackListener) {
            super(objectFactory, clazz, iComEventCallbackListener);
        }

        @Override
        public WinNT.HRESULT Invoke(OaIdl.DISPID integerType, Guid.REFIID rEFIID, WinDef.LCID lCID, WinDef.WORD wORD, OleAuto.DISPPARAMS.ByReference byReference, Variant.VARIANT.ByReference byReference2, OaIdl.EXCEPINFO.ByReference byReference3, IntByReference intByReference) {
            ComThread.setComThread(true);
            try {
                integerType = super.Invoke((OaIdl.DISPID)integerType, rEFIID, lCID, wORD, byReference, byReference2, byReference3, intByReference);
                return integerType;
            }
            finally {
                ComThread.setComThread(false);
            }
        }
    }

    class ProxyObject2
    implements InvocationHandler {
        private final Object delegate;

        public ProxyObject2(Object object) {
            this.delegate = object;
        }

        @Override
        public Object invoke(Object object, final Method method, final Object[] objectArray) throws Throwable {
            if (objectArray != null) {
                for (int i2 = 0; i2 < objectArray.length; ++i2) {
                    InvocationHandler invocationHandler;
                    if (objectArray[i2] == null || !Proxy.isProxyClass(objectArray[i2].getClass()) || !((invocationHandler = Proxy.getInvocationHandler(objectArray[i2])) instanceof ProxyObject2)) continue;
                    objectArray[i2] = ((ProxyObject2)invocationHandler).delegate;
                }
            }
            return Factory.this.runInComThread(new Callable<Object>(){

                @Override
                public Object call() throws Exception {
                    return method.invoke(ProxyObject2.this.delegate, objectArray);
                }
            });
        }
    }
}


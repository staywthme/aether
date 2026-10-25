/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Method;
import java.net.Socket;
import javax.crypto.SecretKey;
import javax.net.ssl.SSLEngine;
import org.conscrypt.PSKKeyManager;

@Deprecated
final class DuckTypedPSKKeyManager
implements PSKKeyManager {
    private final Object mDelegate;

    private DuckTypedPSKKeyManager(Object object) {
        this.mDelegate = object;
    }

    static DuckTypedPSKKeyManager getInstance(Object object) throws NoSuchMethodException {
        Class<?> clazz = object.getClass();
        for (GenericDeclaration genericDeclaration : PSKKeyManager.class.getMethods()) {
            if (genericDeclaration.isSynthetic()) continue;
            Method method = clazz.getMethod(genericDeclaration.getName(), genericDeclaration.getParameterTypes());
            Class<?> clazz2 = method.getReturnType();
            if (((Class)(genericDeclaration = genericDeclaration.getReturnType())).isAssignableFrom(clazz2)) continue;
            throw new NoSuchMethodException(method + " return value (" + clazz2 + ") incompatible with target return value (" + genericDeclaration + ")");
        }
        return new DuckTypedPSKKeyManager(object);
    }

    @Override
    public final String chooseServerKeyIdentityHint(Socket socket) {
        try {
            return (String)this.mDelegate.getClass().getMethod("chooseServerKeyIdentityHint", Socket.class).invoke(this.mDelegate, socket);
        }
        catch (Exception exception) {
            throw new RuntimeException("Failed to invoke chooseServerKeyIdentityHint", exception);
        }
    }

    @Override
    public final String chooseServerKeyIdentityHint(SSLEngine sSLEngine) {
        try {
            return (String)this.mDelegate.getClass().getMethod("chooseServerKeyIdentityHint", SSLEngine.class).invoke(this.mDelegate, sSLEngine);
        }
        catch (Exception exception) {
            throw new RuntimeException("Failed to invoke chooseServerKeyIdentityHint", exception);
        }
    }

    @Override
    public final String chooseClientKeyIdentity(String string, Socket socket) {
        try {
            return (String)this.mDelegate.getClass().getMethod("chooseClientKeyIdentity", String.class, Socket.class).invoke(this.mDelegate, string, socket);
        }
        catch (Exception exception) {
            throw new RuntimeException("Failed to invoke chooseClientKeyIdentity", exception);
        }
    }

    @Override
    public final String chooseClientKeyIdentity(String string, SSLEngine sSLEngine) {
        try {
            return (String)this.mDelegate.getClass().getMethod("chooseClientKeyIdentity", String.class, SSLEngine.class).invoke(this.mDelegate, string, sSLEngine);
        }
        catch (Exception exception) {
            throw new RuntimeException("Failed to invoke chooseClientKeyIdentity", exception);
        }
    }

    @Override
    public final SecretKey getKey(String string, String string2, Socket socket) {
        try {
            return (SecretKey)this.mDelegate.getClass().getMethod("getKey", String.class, String.class, Socket.class).invoke(this.mDelegate, string, string2, socket);
        }
        catch (Exception exception) {
            throw new RuntimeException("Failed to invoke getKey", exception);
        }
    }

    @Override
    public final SecretKey getKey(String string, String string2, SSLEngine sSLEngine) {
        try {
            return (SecretKey)this.mDelegate.getClass().getMethod("getKey", String.class, String.class, SSLEngine.class).invoke(this.mDelegate, string, string2, sSLEngine);
        }
        catch (Exception exception) {
            throw new RuntimeException("Failed to invoke getKey", exception);
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna;

import java.lang.reflect.InvocationTargetException;

abstract class Klass {
    private Klass() {
    }

    public static <T> T newInstance(Class<T> object) {
        try {
            return ((Class)object).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        }
        catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException exception) {
            object = "Can't create an instance of " + object + ", requires a public no-arg constructor: " + exception;
            throw new IllegalArgumentException((String)object, exception);
        }
        catch (InvocationTargetException invocationTargetException) {
            InvocationTargetException invocationTargetException2 = invocationTargetException;
            if (invocationTargetException.getCause() instanceof RuntimeException) {
                throw (RuntimeException)invocationTargetException2.getCause();
            }
            object = "Can't create an instance of " + object + ", requires a public no-arg constructor: " + invocationTargetException2;
            throw new IllegalArgumentException((String)object, invocationTargetException2);
        }
    }
}


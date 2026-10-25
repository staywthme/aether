/*
 * Decompiled with CFR 0.152.
 */
package org.bridj.cpp.mfc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.bridj.Callback;
import org.bridj.NativeObject;
import org.bridj.Pointer;
import org.bridj.cpp.CPPRuntime;
import org.bridj.cpp.mfc.CObject;
import org.bridj.cpp.mfc.CRuntimeClass;
import org.bridj.cpp.mfc.MessageMapBuilder;
import org.bridj.cpp.mfc.OnCommand;
import org.bridj.cpp.mfc.OnCommandEx;
import org.bridj.cpp.mfc.OnMessage;
import org.bridj.cpp.mfc.OnRegisteredMessage;
import org.bridj.cpp.mfc.OnUpdateCommand;
import org.bridj.util.Utils;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class MFCRuntime
extends CPPRuntime {
    Method mfcGetMessageMap;
    String mfcGetMessageMapMangling;
    Callback mfcGetMessageMapCallback;
    Set<Class<?>> hasMessageMap = new HashSet();

    @Override
    public <T extends NativeObject> Class<? extends T> getActualInstanceClass(Pointer<T> clazz, Type type) {
        Pointer<CRuntimeClass> pointer;
        Class clazz2 = Utils.getClass(type);
        if (CObject.class.isAssignableFrom(clazz2) && (pointer = new CObject((Pointer<? extends CObject>)((Object)clazz), this).GetRuntimeClass()) != null) {
            clazz = pointer.get();
            try {
                clazz = this.getMFCClass(((CRuntimeClass)((Object)clazz)).m_lpszClassName());
                if (clazz2 == null || clazz2.isAssignableFrom(clazz)) {
                    return clazz;
                }
            }
            catch (ClassNotFoundException classNotFoundException) {}
            return clazz2;
        }
        return super.getActualInstanceClass(clazz, type);
    }

    private Class<?> getMFCClass(Pointer<Byte> pointer) throws ClassNotFoundException {
        throw new ClassNotFoundException(pointer.getCString());
    }

    public void getExtraFieldsOfNewClass(Class<?> clazz, Map<String, Type> map) {
        if (!this.hasMessageMap.contains(clazz)) {
            return;
        }
        map.put("messageMap", (Type)((Object)Pointer.class));
    }

    public void getOverriddenVirtualMethods(Map<String, Pointer<?>> map) {
        map.put("mfcGetMessageMap", Pointer.getPointer(this.mfcGetMessageMapCallback));
    }

    @Override
    public void register(Type clazz) {
        super.register(clazz);
        clazz = Utils.getClass(clazz);
        MessageMapBuilder messageMapBuilder = new MessageMapBuilder();
        for (Method method : clazz.getMethods()) {
            Annotation annotation = method.getAnnotation(OnCommand.class);
            if (annotation != null) {
                messageMapBuilder.add(method, (OnCommand)annotation);
            }
            if ((annotation = method.getAnnotation(OnCommandEx.class)) != null) {
                messageMapBuilder.add(method, (OnCommandEx)annotation);
            }
            if ((annotation = method.getAnnotation(OnUpdateCommand.class)) != null) {
                messageMapBuilder.add(method, (OnUpdateCommand)annotation);
            }
            if ((annotation = method.getAnnotation(OnRegisteredMessage.class)) != null) {
                messageMapBuilder.add(method, (OnRegisteredMessage)annotation);
            }
            if ((annotation = method.getAnnotation(OnMessage.class)) == null) continue;
            messageMapBuilder.add(method, (OnMessage)annotation);
        }
        if (!messageMapBuilder.isEmpty()) {
            messageMapBuilder.register(this, clazz);
        }
    }
}


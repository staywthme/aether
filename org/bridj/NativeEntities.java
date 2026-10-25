/*
 * Decompiled with CFR 0.152.
 */
package org.bridj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bridj.BridJ;
import org.bridj.JNI;
import org.bridj.MethodCallInfo;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class NativeEntities {
    Map<Class<?>, CBInfo> functions = new HashMap();
    Map<Class<?>, CBInfo> virtualMethods = new HashMap();
    Map<Class<?>, CBInfo> javaToNativeCallbacks = new HashMap();
    Map<Class<?>, CBInfo> objcMethodInfos = new HashMap();

    public void release() {
        if (BridJ.debugNeverFree) {
            return;
        }
        for (CBInfo cBInfo : this.functions.values()) {
            JNI.freeCFunctionBindings(cBInfo.handle, cBInfo.size);
        }
        for (CBInfo cBInfo : this.javaToNativeCallbacks.values()) {
            JNI.freeJavaToCCallbacks(cBInfo.handle, cBInfo.size);
        }
        for (CBInfo cBInfo : this.virtualMethods.values()) {
            JNI.freeVirtualMethodBindings(cBInfo.handle, cBInfo.size);
        }
        for (CBInfo cBInfo : this.objcMethodInfos.values()) {
            JNI.freeObjCMethodBindings(cBInfo.handle, cBInfo.size);
        }
    }

    public void finalize() {
        this.release();
    }

    public void addDefinitions(Class<?> clazz, Builder builder) {
        try {
            int n2 = builder.functionInfos.size();
            if (n2 != 0) {
                this.functions.put(clazz, new CBInfo(JNI.bindJavaMethodsToCFunctions(builder.functionInfos.toArray(new MethodCallInfo[n2])), n2));
            }
            if ((n2 = builder.virtualMethods.size()) != 0) {
                this.virtualMethods.put(clazz, new CBInfo(JNI.bindJavaMethodsToVirtualMethods(builder.virtualMethods.toArray(new MethodCallInfo[n2])), n2));
            }
            if ((n2 = builder.javaToNativeCallbacks.size()) != 0) {
                this.javaToNativeCallbacks.put(clazz, new CBInfo(JNI.bindJavaToCCallbacks(builder.javaToNativeCallbacks.toArray(new MethodCallInfo[n2])), n2));
            }
            if ((n2 = builder.objcMethodInfos.size()) != 0) {
                this.objcMethodInfos.put(clazz, new CBInfo(JNI.bindJavaMethodsToObjCMethods(builder.objcMethodInfos.toArray(new MethodCallInfo[n2])), n2));
            }
            return;
        }
        catch (Throwable throwable) {
            assert (BridJ.error("Failed to add native definitions for class " + clazz.getName(), throwable));
            return;
        }
    }

    public static class Builder {
        List<MethodCallInfo> functionInfos = new ArrayList<MethodCallInfo>();
        List<MethodCallInfo> virtualMethods = new ArrayList<MethodCallInfo>();
        List<MethodCallInfo> javaToNativeCallbacks = new ArrayList<MethodCallInfo>();
        List<MethodCallInfo> cppMethodInfos = new ArrayList<MethodCallInfo>();
        List<MethodCallInfo> objcMethodInfos = new ArrayList<MethodCallInfo>();

        public void addFunction(MethodCallInfo methodCallInfo) {
            this.functionInfos.add(methodCallInfo);
        }

        public void addVirtualMethod(MethodCallInfo methodCallInfo) {
            this.virtualMethods.add(methodCallInfo);
        }

        public void addJavaToNativeCallback(MethodCallInfo methodCallInfo) {
            this.javaToNativeCallbacks.add(methodCallInfo);
        }

        public void addMethodFunction(MethodCallInfo methodCallInfo) {
            this.cppMethodInfos.add(methodCallInfo);
        }

        public void addObjCMethod(MethodCallInfo methodCallInfo) {
            this.objcMethodInfos.add(methodCallInfo);
        }
    }

    static class CBInfo {
        long handle;
        int size;

        public CBInfo(long l2, int n2) {
            this.handle = l2;
            this.size = n2;
        }
    }
}


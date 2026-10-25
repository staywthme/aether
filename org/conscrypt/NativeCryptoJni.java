/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.conscrypt.HostProperties;
import org.conscrypt.NativeCrypto;
import org.conscrypt.NativeLibraryLoader;

final class NativeCryptoJni {
    private static final String STATIC_LIB_NAME = "conscrypt";
    private static final String DYNAMIC_LIB_NAME_PREFIX = "conscrypt_openjdk_jni";

    static void init() throws UnsatisfiedLinkError {
        ArrayList<NativeLibraryLoader.LoadResult> arrayList = new ArrayList<NativeLibraryLoader.LoadResult>();
        if (!NativeLibraryLoader.loadFirstAvailable(NativeCryptoJni.classLoader(), arrayList, NativeCryptoJni.platformLibName(), DYNAMIC_LIB_NAME_PREFIX, STATIC_LIB_NAME)) {
            NativeCryptoJni.logResults(arrayList);
            NativeCryptoJni.throwBestError(arrayList);
        }
    }

    private NativeCryptoJni() {
    }

    private static void logResults(List<NativeLibraryLoader.LoadResult> object) {
        object = object.iterator();
        while (object.hasNext()) {
            NativeLibraryLoader.LoadResult loadResult = (NativeLibraryLoader.LoadResult)object.next();
            loadResult.log();
        }
    }

    private static void throwBestError(List<NativeLibraryLoader.LoadResult> object) {
        Collections.sort(object, ErrorComparator.INSTANCE);
        Throwable throwable = object.get((int)0).error;
        for (NativeLibraryLoader.LoadResult loadResult : object.subList(1, object.size())) {
            throwable.addSuppressed(loadResult.error);
        }
        if (throwable instanceof Error) {
            throw (Error)throwable;
        }
        throw (Error)new UnsatisfiedLinkError(throwable.getMessage()).initCause(throwable);
    }

    private static ClassLoader classLoader() {
        return NativeCrypto.class.getClassLoader();
    }

    private static String platformLibName() {
        return "conscrypt_openjdk_jni-" + NativeCryptoJni.osName() + '-' + NativeCryptoJni.archName();
    }

    private static String osName() {
        return HostProperties.OS.getFileComponent();
    }

    private static String archName() {
        return HostProperties.ARCH.getFileComponent();
    }

    static final class ErrorComparator
    implements Comparator<NativeLibraryLoader.LoadResult> {
        static final ErrorComparator INSTANCE = new ErrorComparator();

        private ErrorComparator() {
        }

        @Override
        public final int compare(NativeLibraryLoader.LoadResult object, NativeLibraryLoader.LoadResult object2) {
            int n2;
            object = ((NativeLibraryLoader.LoadResult)object).error;
            object2 = ((NativeLibraryLoader.LoadResult)object2).error;
            int n3 = object instanceof UnsatisfiedLinkError ? 1 : 0;
            int n4 = n2 = object2 instanceof UnsatisfiedLinkError ? 1 : 0;
            if (n3 != n2) {
                return n2 - n3;
            }
            object = ((Throwable)object).getMessage();
            object2 = ((Throwable)object2).getMessage();
            n3 = object != null && ((String)object).contains("java.library.path") ? 0 : 1;
            n2 = object2 != null && ((String)object2).contains("java.library.path") ? 0 : 1;
            return n2 - n3;
        }
    }
}


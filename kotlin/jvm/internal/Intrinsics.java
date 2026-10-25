/*
 * Decompiled with CFR 0.152.
 */
package kotlin.jvm.internal;

import java.util.Arrays;
import kotlin.KotlinNullPointerException;
import kotlin.SinceKotlin;
import kotlin.UninitializedPropertyAccessException;

public class Intrinsics {
    private Intrinsics() {
    }

    public static String stringPlus(String string, Object object) {
        return string + object;
    }

    public static void checkNotNull(Object object) {
        if (object == null) {
            Intrinsics.throwJavaNpe();
        }
    }

    public static void checkNotNull(Object object, String string) {
        if (object == null) {
            Intrinsics.throwJavaNpe(string);
        }
    }

    public static void throwNpe() {
        throw Intrinsics.sanitizeStackTrace(new KotlinNullPointerException());
    }

    public static void throwNpe(String string) {
        throw Intrinsics.sanitizeStackTrace(new KotlinNullPointerException(string));
    }

    @SinceKotlin(version="1.4")
    public static void throwJavaNpe() {
        throw Intrinsics.sanitizeStackTrace(new NullPointerException());
    }

    @SinceKotlin(version="1.4")
    public static void throwJavaNpe(String string) {
        throw Intrinsics.sanitizeStackTrace(new NullPointerException(string));
    }

    public static void throwUninitializedProperty(String string) {
        throw Intrinsics.sanitizeStackTrace(new UninitializedPropertyAccessException(string));
    }

    public static void throwUninitializedPropertyAccessException(String string) {
        Intrinsics.throwUninitializedProperty("lateinit property " + string + " has not been initialized");
    }

    public static void throwAssert() {
        throw Intrinsics.sanitizeStackTrace(new AssertionError());
    }

    public static void throwAssert(String string) {
        throw Intrinsics.sanitizeStackTrace(new AssertionError((Object)string));
    }

    public static void throwIllegalArgument() {
        throw Intrinsics.sanitizeStackTrace(new IllegalArgumentException());
    }

    public static void throwIllegalArgument(String string) {
        throw Intrinsics.sanitizeStackTrace(new IllegalArgumentException(string));
    }

    public static void throwIllegalState() {
        throw Intrinsics.sanitizeStackTrace(new IllegalStateException());
    }

    public static void throwIllegalState(String string) {
        throw Intrinsics.sanitizeStackTrace(new IllegalStateException(string));
    }

    public static void checkExpressionValueIsNotNull(Object object, String string) {
        if (object == null) {
            throw Intrinsics.sanitizeStackTrace(new IllegalStateException(string + " must not be null"));
        }
    }

    public static void checkNotNullExpressionValue(Object object, String string) {
        if (object == null) {
            throw Intrinsics.sanitizeStackTrace(new NullPointerException(string + " must not be null"));
        }
    }

    public static void checkReturnedValueIsNotNull(Object object, String string, String string2) {
        if (object == null) {
            throw Intrinsics.sanitizeStackTrace(new IllegalStateException("Method specified as non-null returned null: " + string + "." + string2));
        }
    }

    public static void checkReturnedValueIsNotNull(Object object, String string) {
        if (object == null) {
            throw Intrinsics.sanitizeStackTrace(new IllegalStateException(string));
        }
    }

    public static void checkFieldIsNotNull(Object object, String string, String string2) {
        if (object == null) {
            throw Intrinsics.sanitizeStackTrace(new IllegalStateException("Field specified as non-null is null: " + string + "." + string2));
        }
    }

    public static void checkFieldIsNotNull(Object object, String string) {
        if (object == null) {
            throw Intrinsics.sanitizeStackTrace(new IllegalStateException(string));
        }
    }

    public static void checkParameterIsNotNull(Object object, String string) {
        if (object == null) {
            Intrinsics.throwParameterIsNullIAE(string);
        }
    }

    public static void checkNotNullParameter(Object object, String string) {
        if (object == null) {
            Intrinsics.throwParameterIsNullNPE(string);
        }
    }

    private static void throwParameterIsNullIAE(String string) {
        throw Intrinsics.sanitizeStackTrace(new IllegalArgumentException(Intrinsics.createParameterIsNullExceptionMessage(string)));
    }

    private static void throwParameterIsNullNPE(String string) {
        throw Intrinsics.sanitizeStackTrace(new NullPointerException(Intrinsics.createParameterIsNullExceptionMessage(string)));
    }

    private static String createParameterIsNullExceptionMessage(String string) {
        Object object = Thread.currentThread().getStackTrace();
        String string2 = Intrinsics.class.getName();
        int n2 = 0;
        while (!object[n2].getClassName().equals(string2)) {
            ++n2;
        }
        while (object[n2].getClassName().equals(string2)) {
            ++n2;
        }
        object = object[n2];
        string2 = ((StackTraceElement)object).getClassName();
        object = ((StackTraceElement)object).getMethodName();
        return "Parameter specified as non-null is null: method " + string2 + "." + (String)object + ", parameter " + string;
    }

    public static int compare(long l2, long l3) {
        if (l2 < l3) {
            return -1;
        }
        if (l2 == l3) {
            return 0;
        }
        return 1;
    }

    public static int compare(int n2, int n3) {
        if (n2 < n3) {
            return -1;
        }
        if (n2 == n3) {
            return 0;
        }
        return 1;
    }

    public static boolean areEqual(Object object, Object object2) {
        if (object == null) {
            return object2 == null;
        }
        return object.equals(object2);
    }

    @SinceKotlin(version="1.1")
    public static boolean areEqual(Double d2, Double d3) {
        if (d2 == null) {
            return d3 == null;
        }
        return d3 != null && d2.doubleValue() == d3.doubleValue();
    }

    @SinceKotlin(version="1.1")
    public static boolean areEqual(Double d2, double d3) {
        return d2 != null && d2 == d3;
    }

    @SinceKotlin(version="1.1")
    public static boolean areEqual(double d2, Double d3) {
        return d3 != null && d2 == d3;
    }

    @SinceKotlin(version="1.1")
    public static boolean areEqual(Float f2, Float f3) {
        if (f2 == null) {
            return f3 == null;
        }
        return f3 != null && f2.floatValue() == f3.floatValue();
    }

    @SinceKotlin(version="1.1")
    public static boolean areEqual(Float f2, float f3) {
        return f2 != null && f2.floatValue() == f3;
    }

    @SinceKotlin(version="1.1")
    public static boolean areEqual(float f2, Float f3) {
        return f3 != null && f2 == f3.floatValue();
    }

    public static void throwUndefinedForReified() {
        Intrinsics.throwUndefinedForReified("This function has a reified type parameter and thus can only be inlined at compilation time, not called directly.");
    }

    public static void throwUndefinedForReified(String string) {
        throw new UnsupportedOperationException(string);
    }

    public static void reifiedOperationMarker(int n2, String string) {
        Intrinsics.throwUndefinedForReified();
    }

    public static void reifiedOperationMarker(int n2, String string, String string2) {
        Intrinsics.throwUndefinedForReified(string2);
    }

    public static void needClassReification() {
        Intrinsics.throwUndefinedForReified();
    }

    public static void needClassReification(String string) {
        Intrinsics.throwUndefinedForReified(string);
    }

    public static void checkHasClass(String string) throws ClassNotFoundException {
        string = string.replace('/', '.');
        try {
            Class.forName(string);
            return;
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw Intrinsics.sanitizeStackTrace(new ClassNotFoundException("Class " + string + " is not found. Please update the Kotlin runtime to the latest version", classNotFoundException));
        }
    }

    public static void checkHasClass(String string, String string2) throws ClassNotFoundException {
        string = string.replace('/', '.');
        try {
            Class.forName(string);
            return;
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw Intrinsics.sanitizeStackTrace(new ClassNotFoundException("Class " + string + " is not found: this code requires the Kotlin runtime of version at least " + string2, classNotFoundException));
        }
    }

    private static <T extends Throwable> T sanitizeStackTrace(T t2) {
        return Intrinsics.sanitizeStackTrace(t2, Intrinsics.class.getName());
    }

    static <T extends Throwable> T sanitizeStackTrace(T t2, String string) {
        StackTraceElement[] stackTraceElementArray = t2.getStackTrace();
        int n2 = stackTraceElementArray.length;
        int n3 = -1;
        for (int i2 = 0; i2 < n2; ++i2) {
            if (!string.equals(stackTraceElementArray[i2].getClassName())) continue;
            n3 = i2;
        }
        StackTraceElement[] stackTraceElementArray2 = Arrays.copyOfRange(stackTraceElementArray, n3 + 1, n2);
        t2.setStackTrace(stackTraceElementArray2);
        return t2;
    }

    @SinceKotlin(version="1.4")
    public static class Kotlin {
        private Kotlin() {
        }
    }
}


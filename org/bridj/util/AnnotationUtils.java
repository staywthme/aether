/*
 * Decompiled with CFR 0.152.
 */
package org.bridj.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import org.bridj.ann.Forwardable;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class AnnotationUtils {
    public static <A extends Annotation> A getInheritableAnnotation(Class<A> clazz, AnnotatedElement annotatedElement, Annotation ... annotationArray) {
        return AnnotationUtils.getAnnotation(clazz, true, annotatedElement, annotationArray);
    }

    public static <A extends Annotation> A getAnnotation(Class<A> clazz, AnnotatedElement annotatedElement, Annotation ... annotationArray) {
        return AnnotationUtils.getAnnotation(clazz, false, annotatedElement, annotationArray);
    }

    private static boolean isForwardable(Class<? extends Annotation> clazz) {
        return clazz.isAnnotationPresent(Forwardable.class);
    }

    public static boolean isAnnotationPresent(Class<? extends Annotation> clazz, Annotation ... annotationArray) {
        Class<? extends Annotation> clazz2 = clazz;
        return AnnotationUtils.isAnnotationPresent(clazz2, AnnotationUtils.isForwardable(clazz2), annotationArray);
    }

    private static boolean isAnnotationPresent(Class<? extends Annotation> clazz, boolean bl, Annotation ... annotationArray) {
        for (Annotation annotation : annotationArray) {
            if (clazz.isInstance(annotation)) {
                return true;
            }
            if (!bl || !annotation.annotationType().isAnnotationPresent(clazz)) continue;
            return true;
        }
        return false;
    }

    public static boolean isAnnotationPresent(Class<? extends Annotation> clazz, AnnotatedElement annotatedElement, Annotation ... annotationArray) {
        boolean bl = AnnotationUtils.isForwardable(clazz);
        if (annotatedElement != null && (bl ? AnnotationUtils.isAnnotationPresent(clazz, true, annotatedElement.getAnnotations()) : annotatedElement.isAnnotationPresent(clazz))) {
            return true;
        }
        if (annotationArray != null) {
            return AnnotationUtils.isAnnotationPresent(clazz, bl, annotationArray);
        }
        return false;
    }

    /*
     * WARNING - void declaration
     */
    private static <A extends Annotation> A getAnnotation(Class<A> clazz, boolean bl, AnnotatedElement clazz2, Annotation ... object) {
        while (true) {
            void var6_19;
            void var3_4;
            if (var3_4 != null) {
                for (void var6_17 : var3_4) {
                    if (!clazz.isInstance(var6_17)) continue;
                    return (A)((Annotation)clazz.cast(var6_17));
                }
            }
            if (clazz2 == null) {
                return null;
            }
            A a2 = clazz2.getAnnotation((Class<?>)clazz);
            if (a2 != null) {
                return a2;
            }
            if (!bl) break;
            if (clazz2 instanceof Member) {
                Annotation[] annotationArray = new Annotation[]{};
                clazz2 = ((Member)((Object)clazz2)).getDeclaringClass();
                continue;
            }
            if (!(clazz2 instanceof Class)) break;
            Class<?> clazz3 = clazz2;
            Class<?> clazz4 = clazz3.getDeclaringClass();
            Class<?> clazz5 = clazz3.getSuperclass();
            while (var6_19 != null) {
                A a3 = AnnotationUtils.getAnnotation(clazz, true, (AnnotatedElement)var6_19, new Annotation[0]);
                if (a3 != null) {
                    return a3;
                }
                Class clazz6 = var6_19.getSuperclass();
            }
            if (clazz4 == null) break;
            Annotation[] annotationArray = new Annotation[]{};
            clazz2 = clazz4;
        }
        return null;
    }
}


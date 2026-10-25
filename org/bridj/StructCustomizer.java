/*
 * Decompiled with CFR 0.152.
 */
package org.bridj;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.bridj.StructDescription;
import org.bridj.StructFieldDeclaration;
import org.bridj.StructFieldDescription;
import org.bridj.ann.Struct;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
@Deprecated
public class StructCustomizer {
    private static StructCustomizer dummyCustomizer = new StructCustomizer();
    private static ConcurrentHashMap<Class, StructCustomizer> customizers = new ConcurrentHashMap();

    public void beforeAggregation(StructDescription structDescription, List<StructFieldDeclaration> list2) {
    }

    public void beforeLayout(StructDescription structDescription, List<StructFieldDescription> list2) {
    }

    public void afterLayout(StructDescription structDescription, List<StructFieldDescription> list2) {
    }

    public void afterBuild(StructDescription structDescription) {
    }

    static StructCustomizer getInstance(Class<?> clazz) {
        StructCustomizer structCustomizer = customizers.get(clazz);
        if (structCustomizer == null) {
            Object object = clazz.getAnnotation(Struct.class);
            if (object != null && (object = object.customizer()) != null && object != StructCustomizer.class) {
                try {
                    structCustomizer = (StructCustomizer)((Class)object).newInstance();
                }
                catch (Throwable throwable) {
                    throw new RuntimeException("Failed to create customizer of class " + ((Class)object).getName() + " for struct class " + clazz.getName() + " : " + throwable, throwable);
                }
            }
            if (structCustomizer == null) {
                structCustomizer = dummyCustomizer;
            }
            if ((object = customizers.putIfAbsent(clazz, structCustomizer)) != null) {
                return object;
            }
        }
        return structCustomizer;
    }
}


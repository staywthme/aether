/*
 * Decompiled with CFR 0.152.
 */
package org.bridj.cpp.std;

import org.bridj.Platform;
import org.bridj.Pointer;
import org.bridj.StructCustomizer;
import org.bridj.StructDescription;
import org.bridj.cpp.std.list;
import org.bridj.cpp.std.vector;

public final class STL
extends StructCustomizer {
    public final void afterBuild(StructDescription structDescription) {
        if (!Platform.isWindows()) {
            return;
        }
        Class<?> clazz = structDescription.getStructClass();
        if (clazz == vector.class) {
            structDescription.prependBytes(3 * Pointer.SIZE);
            return;
        }
        if (clazz == list.class || clazz == list.list_node.class) {
            structDescription.setFieldOffset("prev", 5 * Pointer.SIZE, false);
            if (clazz == list.list_node.class) {
                structDescription.setFieldOffset("data", 6 * Pointer.SIZE, false);
            }
        }
    }
}


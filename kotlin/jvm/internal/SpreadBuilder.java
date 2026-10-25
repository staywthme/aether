/*
 * Decompiled with CFR 0.152.
 */
package kotlin.jvm.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class SpreadBuilder {
    private final ArrayList<Object> list;

    public SpreadBuilder(int n2) {
        this.list = new ArrayList(n2);
    }

    public void addSpread(Object object) {
        if (object == null) {
            return;
        }
        if (object instanceof Object[]) {
            if (((Object[])(object = (Object[])object)).length > 0) {
                this.list.ensureCapacity(this.list.size() + ((Object)object).length);
                Collections.addAll(this.list, object);
            }
            return;
        }
        if (object instanceof Collection) {
            this.list.addAll((Collection)object);
            return;
        }
        if (object instanceof Iterable) {
            for (Object e2 : (Iterable)object) {
                this.list.add(e2);
            }
            return;
        }
        if (object instanceof Iterator) {
            object = (Iterator)object;
            while (object.hasNext()) {
                this.list.add(object.next());
            }
            return;
        }
        throw new UnsupportedOperationException("Don't know how to spread " + object.getClass());
    }

    public int size() {
        return this.list.size();
    }

    public void add(Object object) {
        this.list.add(object);
    }

    public Object[] toArray(Object[] objectArray) {
        return this.list.toArray(objectArray);
    }
}


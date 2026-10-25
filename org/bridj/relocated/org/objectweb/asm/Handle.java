/*
 * Decompiled with CFR 0.152.
 */
package org.bridj.relocated.org.objectweb.asm;

public final class Handle {
    final int a;
    final String b;
    final String c;
    final String d;

    public Handle(int n2, String string, String string2, String string3) {
        this.a = n2;
        this.b = string;
        this.c = string2;
        this.d = string3;
    }

    public final int getTag() {
        return this.a;
    }

    public final String getOwner() {
        return this.b;
    }

    public final String getName() {
        return this.c;
    }

    public final String getDesc() {
        return this.d;
    }

    public final boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof Handle)) {
            return false;
        }
        object = (Handle)object;
        return this.a == ((Handle)object).a && this.b.equals(((Handle)object).b) && this.c.equals(((Handle)object).c) && this.d.equals(((Handle)object).d);
    }

    public final int hashCode() {
        return this.a + this.b.hashCode() * this.c.hashCode() * this.d.hashCode();
    }

    public final String toString() {
        return this.b + '.' + this.c + this.d + " (" + this.a + ')';
    }
}


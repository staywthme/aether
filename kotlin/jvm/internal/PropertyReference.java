/*
 * Decompiled with CFR 0.152.
 */
package kotlin.jvm.internal;

import kotlin.SinceKotlin;
import kotlin.jvm.internal.CallableReference;
import kotlin.jvm.internal.Intrinsics;
import kotlin.reflect.KCallable;
import kotlin.reflect.KProperty;

public abstract class PropertyReference
extends CallableReference
implements KProperty {
    private final boolean syntheticJavaProperty;

    public PropertyReference() {
        this.syntheticJavaProperty = false;
    }

    @SinceKotlin(version="1.1")
    public PropertyReference(Object object) {
        super(object);
        this.syntheticJavaProperty = false;
    }

    @SinceKotlin(version="1.4")
    public PropertyReference(Object object, Class clazz, String string, String string2, int n2) {
        super(object, clazz, string, string2, (n2 & 1) == 1);
        this.syntheticJavaProperty = (n2 & 2) == 2;
    }

    @Override
    @SinceKotlin(version="1.1")
    protected KProperty getReflected() {
        if (this.syntheticJavaProperty) {
            throw new UnsupportedOperationException("Kotlin reflection is not yet supported for synthetic Java properties");
        }
        return (KProperty)super.getReflected();
    }

    @Override
    public KCallable compute() {
        if (this.syntheticJavaProperty) {
            return this;
        }
        return super.compute();
    }

    @Override
    @SinceKotlin(version="1.1")
    public boolean isLateinit() {
        return this.getReflected().isLateinit();
    }

    @Override
    @SinceKotlin(version="1.1")
    public boolean isConst() {
        return this.getReflected().isConst();
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof PropertyReference) {
            object = (PropertyReference)object;
            return this.getOwner().equals(((CallableReference)object).getOwner()) && this.getName().equals(((CallableReference)object).getName()) && this.getSignature().equals(((CallableReference)object).getSignature()) && Intrinsics.areEqual(this.getBoundReceiver(), ((CallableReference)object).getBoundReceiver());
        }
        if (object instanceof KProperty) {
            return object.equals(((CallableReference)this).compute());
        }
        return false;
    }

    public int hashCode() {
        return (this.getOwner().hashCode() * 31 + this.getName().hashCode()) * 31 + this.getSignature().hashCode();
    }

    public String toString() {
        KCallable kCallable = ((CallableReference)this).compute();
        if (kCallable != this) {
            return kCallable.toString();
        }
        return "property " + this.getName() + " (Kotlin reflection is not available)";
    }
}


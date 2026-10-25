/*
 * Decompiled with CFR 0.152.
 */
package kotlin.jvm.internal;

import java.io.Serializable;
import kotlin.SinceKotlin;
import kotlin.jvm.internal.CallableReference;
import kotlin.jvm.internal.FunctionBase;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KDeclarationContainer;

@SinceKotlin(version="1.4")
public class AdaptedFunctionReference
implements Serializable,
FunctionBase {
    protected final Object receiver;
    private final Class owner;
    private final String name;
    private final String signature;
    private final boolean isTopLevel;
    private final int arity;
    private final int flags;

    public AdaptedFunctionReference(int n2, Class clazz, String string, String string2, int n3) {
        this(n2, CallableReference.NO_RECEIVER, clazz, string, string2, n3);
    }

    public AdaptedFunctionReference(int n2, Object object, Class clazz, String string, String string2, int n3) {
        this.receiver = object;
        this.owner = clazz;
        this.name = string;
        this.signature = string2;
        this.isTopLevel = (n3 & 1) == 1;
        this.arity = n2;
        this.flags = n3 >> 1;
    }

    @Override
    public int getArity() {
        return this.arity;
    }

    public KDeclarationContainer getOwner() {
        if (this.owner == null) {
            return null;
        }
        if (this.isTopLevel) {
            return Reflection.getOrCreateKotlinPackage(this.owner);
        }
        return Reflection.getOrCreateKotlinClass(this.owner);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof AdaptedFunctionReference)) {
            return false;
        }
        object = (AdaptedFunctionReference)object;
        return this.isTopLevel == ((AdaptedFunctionReference)object).isTopLevel && this.arity == ((AdaptedFunctionReference)object).arity && this.flags == ((AdaptedFunctionReference)object).flags && Intrinsics.areEqual(this.receiver, ((AdaptedFunctionReference)object).receiver) && Intrinsics.areEqual(this.owner, ((AdaptedFunctionReference)object).owner) && this.name.equals(((AdaptedFunctionReference)object).name) && this.signature.equals(((AdaptedFunctionReference)object).signature);
    }

    public int hashCode() {
        int n2 = this.receiver != null ? this.receiver.hashCode() : 0;
        n2 = n2 * 31 + (this.owner != null ? this.owner.hashCode() : 0);
        n2 = n2 * 31 + this.name.hashCode();
        n2 = n2 * 31 + this.signature.hashCode();
        n2 = n2 * 31 + (this.isTopLevel ? 1231 : 1237);
        n2 = n2 * 31 + this.arity;
        n2 = n2 * 31 + this.flags;
        return n2;
    }

    public String toString() {
        return Reflection.renderLambdaToString(this);
    }
}


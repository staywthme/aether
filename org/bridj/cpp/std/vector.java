/*
 * Decompiled with CFR 0.152.
 */
package org.bridj.cpp.std;

import java.lang.reflect.Type;
import java.util.NoSuchElementException;
import org.bridj.BridJ;
import org.bridj.Pointer;
import org.bridj.ann.Field;
import org.bridj.ann.Struct;
import org.bridj.ann.Template;
import org.bridj.cpp.CPPObject;
import org.bridj.cpp.CPPRuntime;
import org.bridj.cpp.std.STL;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
@Template(value={Type.class})
@Struct(customizer=STL.class)
public class vector<T>
extends CPPObject {
    @Deprecated
    @Field(value=0)
    public Pointer<T> _M_start() {
        return this.io.getPointerField(this, 0);
    }

    @Deprecated
    @Field(value=1)
    public Pointer<T> _M_finish() {
        return this.io.getPointerField(this, 1);
    }

    @Deprecated
    @Field(value=2)
    public Pointer<T> _M_end_of_storage() {
        return this.io.getPointerField(this, 2);
    }

    public vector(Type type) {
        super((Void)null, -2, type);
    }

    public vector(Pointer<? extends vector<T>> pointer, Type type) {
        super((Pointer<? extends CPPObject>)pointer, new Object[]{type});
        if (!this.isValid()) {
            throw new RuntimeException("Invalid vector internal data ! Are you trying to use an unsupported version of the STL ?");
        }
    }

    protected boolean isValid() {
        long l2 = Pointer.getPeer(this._M_start());
        long l3 = Pointer.getPeer(this._M_finish());
        long l4 = Pointer.getPeer(this._M_end_of_storage());
        if (l2 == 0L || l3 == 0L || l4 == 0L) {
            return false;
        }
        return l2 <= l3 && l3 <= l4;
    }

    private void checkNotEmpty() {
        this.checkIndex(0L);
    }

    private void checkIndex(long l2) {
        long l3 = this.size();
        if (l2 < 0L || l2 >= l3) {
            throw new NoSuchElementException("index " + l2 + " (size = " + l3 + ")");
        }
    }

    public boolean empty() {
        return this.size() == 0L;
    }

    public T get(long l2) {
        this.checkIndex(l2);
        Pointer pointer = this._M_start().as(this.T());
        return (T)pointer.get(l2);
    }

    public T get(int n2) {
        return this.get((long)n2);
    }

    public T front() {
        return this.get(0);
    }

    public T back() {
        vector vector2 = this;
        return vector2.get(vector2.size() - 1L);
    }

    public void push_back(T t2) {
        throw new UnsupportedOperationException();
    }

    protected Type T() {
        CPPRuntime.getInstance();
        return (Type)CPPRuntime.getTemplateParameters(this, vector.class)[0];
    }

    protected long byteSize() {
        return this._M_finish().getPeer() - this._M_start().getPeer();
    }

    public long size() {
        long l2 = this.byteSize();
        long l3 = BridJ.sizeOf(this.T());
        return l2 / l3;
    }
}


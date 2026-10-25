/*
 * Decompiled with CFR 0.152.
 */
package org.bridj.cpp.std;

import java.lang.reflect.Type;
import java.util.NoSuchElementException;
import org.bridj.BridJ;
import org.bridj.Pointer;
import org.bridj.ann.Array;
import org.bridj.ann.Field;
import org.bridj.ann.Library;
import org.bridj.ann.Ptr;
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
public class list<T>
extends CPPObject {
    protected volatile Type _T;

    @Library(value="c")
    @Ptr
    protected static native long malloc(@Ptr long var0);

    @Library(value="c")
    protected static native void free(@Ptr long var0);

    protected Type T() {
        if (this._T == null) {
            CPPRuntime.getInstance();
            this._T = (Type)CPPRuntime.getTemplateParameters(this, list.class)[0];
        }
        return this._T;
    }

    protected list_node<T> createNode() {
        Type type = this.T();
        long l2 = BridJ.sizeOf(type);
        return new list_node(Pointer.pointerToAddress(list.malloc(l2)), type);
    }

    protected void deleteNode(list_node<T> list_node2) {
        list.free(Pointer.getAddress(list_node2, list_node.class));
    }

    private Pointer<list_node<T>> node() {
        return Pointer.getPointer(this).as(list_node.class);
    }

    @Deprecated
    @Field(value=0)
    public Pointer<list_node<T>> next() {
        return this.io.getPointerField(this, 0);
    }

    @Deprecated
    @Field(value=0)
    public void next(Pointer<list_node<T>> pointer) {
        this.io.setPointerField(this, 0, pointer);
    }

    @Deprecated
    @Field(value=1)
    public Pointer<list_node<T>> prev() {
        return this.io.getPointerField(this, 1);
    }

    @Deprecated
    @Field(value=1)
    public void prev(Pointer<list_node<T>> pointer) {
        this.io.setPointerField(this, 1, pointer);
    }

    public list(Type type) {
        super((Void)null, -2, type);
    }

    public list(Pointer<? extends list<T>> pointer, Type type) {
        super((Pointer<? extends CPPObject>)pointer, new Object[]{type});
    }

    private void checkNotEmpty() {
        list list2 = this;
        if (list2.isRoot(list2.next())) {
            list list3 = this;
            if (list3.isRoot(list3.prev())) {
                throw new NoSuchElementException();
            }
        }
    }

    public boolean empty() {
        return Pointer.getPeer(this.next()) == Pointer.getPeer(this.node());
    }

    public T back() {
        this.checkNotEmpty();
        return this.next().get().get();
    }

    public T front() {
        this.checkNotEmpty();
        Object object = this.prev();
        Pointer<list_node<T>> pointer = this.next();
        if (pointer != null) {
            pointer.get().prev();
        }
        if (object != null) {
            ((Pointer)object).get().next();
        }
        if (pointer != null) {
            pointer.getInts(20);
        }
        if (object != null) {
            ((Pointer)object).getInts(20);
        }
        object = ((Pointer)object).get();
        Pointer.pointerTo(object).getInts(20);
        return ((list_node)object).get();
    }

    private boolean same(Pointer pointer, Pointer pointer2) {
        return Pointer.getPeer(pointer) == Pointer.getPeer(pointer2);
    }

    private boolean isRoot(Pointer pointer) {
        return this.same(pointer, Pointer.getPointer(this));
    }

    protected void hook(Pointer<list_node<T>> pointer, Pointer<list_node<T>> pointer2, T t2) {
        list_node<T> list_node2 = this.createNode();
        Pointer<list_node<T>> pointer3 = Pointer.getPointer(list_node2);
        list_node2.set(t2);
        list_node2.next(pointer2);
        list_node2.prev(pointer);
        if (!this.isRoot(pointer2)) {
            pointer2.get().prev(pointer3);
        }
        if (!this.isRoot(pointer)) {
            pointer.get().next(pointer3);
        }
    }

    public void push_back(T t2) {
        list list2 = this;
        list2.hook(list2.prev(), null, t2);
    }

    public void push_front(T t2) {
        this.hook(null, this.next(), t2);
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    @Template(value={Type.class})
    public static class list_node<T>
    extends CPPObject {
        @Deprecated
        @Field(value=0)
        public Pointer<list_node<T>> next() {
            return this.io.getPointerField(this, 0);
        }

        @Deprecated
        @Field(value=0)
        public void next(Pointer<list_node<T>> pointer) {
            this.io.setPointerField(this, 0, pointer);
        }

        @Deprecated
        @Field(value=1)
        public Pointer<list_node<T>> prev() {
            return this.io.getPointerField(this, 1);
        }

        @Field(value=1)
        public void prev(Pointer<list_node<T>> pointer) {
            this.io.setPointerField(this, 1, pointer);
        }

        @Deprecated
        @Field(value=2)
        @Array(value={1L})
        public Pointer<T> data() {
            return this.io.getPointerField(this, 2);
        }

        public list_node(Type type) {
            super((Void)null, -2, type);
        }

        public list_node(Pointer<? extends list_node> pointer, Type type) {
            super((Pointer<? extends CPPObject>)pointer, new Object[]{type});
            if (!this.isValid()) {
                throw new RuntimeException("Invalid list internal data ! Are you trying to use an unsupported version of the STL ?");
            }
        }

        protected boolean isValid() {
            long l2 = Pointer.getPeer(this.next());
            long l3 = Pointer.getPeer(this.prev());
            return l2 != 0L || l3 != 0L;
        }

        public T get() {
            return this.data().get();
        }

        public void set(T t2) {
            this.data().set(t2);
        }
    }
}


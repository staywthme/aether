/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna;

import com.sun.jna.Callback;
import com.sun.jna.FromNativeContext;
import com.sun.jna.FromNativeConverter;
import com.sun.jna.Function;
import com.sun.jna.IntegerType;
import com.sun.jna.Klass;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.NativeMapped;
import com.sun.jna.NativeMappedConverter;
import com.sun.jna.NativeString;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.StructureReadContext;
import com.sun.jna.StructureWriteContext;
import com.sun.jna.ToNativeContext;
import com.sun.jna.ToNativeConverter;
import com.sun.jna.TypeMapper;
import com.sun.jna.Union;
import com.sun.jna.WString;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.nio.Buffer;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Structure {
    private static final Logger LOG = Logger.getLogger(Structure.class.getName());
    public static final int ALIGN_DEFAULT = 0;
    public static final int ALIGN_NONE = 1;
    public static final int ALIGN_GNUC = 2;
    public static final int ALIGN_MSVC = 3;
    protected static final int CALCULATE_SIZE = -1;
    static final Map<Class<?>, LayoutInfo> layoutInfo = new WeakHashMap();
    static final Map<Class<?>, List<String>> fieldOrder = new WeakHashMap();
    private Pointer memory;
    private int size = -1;
    private int alignType;
    private String encoding;
    private int actualAlignType;
    private int structAlignment;
    private Map<String, StructField> structFields;
    private final Map<String, NativeStringTracking> nativeStrings = new HashMap<String, NativeStringTracking>(8);
    private TypeMapper typeMapper;
    private long typeInfo;
    private boolean autoRead = true;
    private boolean autoWrite = true;
    private Structure[] array;
    private boolean readCalled;
    private static final ThreadLocal<Map<Pointer, Structure>> reads = new ThreadLocal<Map<Pointer, Structure>>(){

        @Override
        protected final synchronized Map<Pointer, Structure> initialValue() {
            return new HashMap<Pointer, Structure>();
        }
    };
    private static final ThreadLocal<Set<Structure>> busy = new ThreadLocal<Set<Structure>>(){

        @Override
        protected final synchronized Set<Structure> initialValue() {
            return new StructureSet();
        }
    };
    private static final Pointer PLACEHOLDER_MEMORY = new Pointer(0L){

        @Override
        public final Pointer share(long l2, long l3) {
            return this;
        }
    };

    public Structure() {
        this(0);
    }

    public Structure(TypeMapper typeMapper) {
        this(null, 0, typeMapper);
    }

    protected Structure(int n2) {
        this(null, n2);
    }

    protected Structure(int n2, TypeMapper typeMapper) {
        this(null, n2, typeMapper);
    }

    public Structure(Pointer pointer) {
        this(pointer, 0);
    }

    protected Structure(Pointer pointer, int n2) {
        this(pointer, n2, null);
    }

    public Structure(Pointer pointer, int n2, TypeMapper typeMapper) {
        this.setAlignType(n2);
        Structure structure = this;
        structure.setStringEncoding(Native.getStringEncoding(structure.getClass()));
        this.initializeTypeMapper(typeMapper);
        this.validateFields();
        if (pointer != null) {
            this.useMemory(pointer, 0, true);
        } else {
            this.allocateMemory(-1);
        }
        this.initializeFields();
    }

    Map<String, StructField> fields() {
        return this.structFields;
    }

    TypeMapper getTypeMapper() {
        return this.typeMapper;
    }

    private void initializeTypeMapper(TypeMapper typeMapper) {
        if (typeMapper == null) {
            typeMapper = Native.getTypeMapper(this.getClass());
        }
        this.typeMapper = typeMapper;
        this.layoutChanged();
    }

    private void layoutChanged() {
        if (this.size != -1) {
            this.size = -1;
            if (this.memory instanceof AutoAllocated) {
                this.memory = null;
            }
            this.ensureAllocated();
        }
    }

    protected void setStringEncoding(String string) {
        this.encoding = string;
    }

    protected String getStringEncoding() {
        return this.encoding;
    }

    protected void setAlignType(int n2) {
        this.alignType = n2;
        if (n2 == 0 && (n2 = Native.getStructureAlignment(this.getClass())) == 0) {
            n2 = Platform.isWindows() ? 3 : 2;
        }
        this.actualAlignType = n2;
        this.layoutChanged();
    }

    protected Memory autoAllocate(int n2) {
        return new AutoAllocated(n2);
    }

    protected void useMemory(Pointer pointer) {
        this.useMemory(pointer, 0);
    }

    protected void useMemory(Pointer pointer, int n2) {
        this.useMemory(pointer, n2, false);
    }

    void useMemory(Pointer pointer, int n2, boolean bl) {
        try {
            this.nativeStrings.clear();
            if (this instanceof ByValue && !bl) {
                byte[] byArray = new byte[this.size()];
                pointer.read(0L, byArray, 0, byArray.length);
                this.memory.write(0L, byArray, 0, byArray.length);
            } else {
                if (this.size == -1) {
                    this.size = this.calculateSize(false);
                }
                this.memory = this.size != -1 ? pointer.share(n2, this.size) : pointer.share(n2);
            }
            this.array = null;
            this.readCalled = false;
            return;
        }
        catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            throw new IllegalArgumentException("Structure exceeds provided memory bounds", indexOutOfBoundsException);
        }
    }

    protected void ensureAllocated() {
        this.ensureAllocated(false);
    }

    private void ensureAllocated(boolean bl) {
        if (this.memory == null) {
            this.allocateMemory(bl);
            return;
        }
        if (this.size == -1) {
            this.size = this.calculateSize(true, bl);
            if (!(this.memory instanceof AutoAllocated)) {
                try {
                    this.memory = this.memory.share(0L, this.size);
                    return;
                }
                catch (IndexOutOfBoundsException indexOutOfBoundsException) {
                    throw new IllegalArgumentException("Structure exceeds provided memory bounds", indexOutOfBoundsException);
                }
            }
        }
    }

    protected void allocateMemory() {
        this.allocateMemory(false);
    }

    private void allocateMemory(boolean bl) {
        Structure structure = this;
        structure.allocateMemory(structure.calculateSize(true, bl));
    }

    protected void allocateMemory(int n2) {
        if (n2 == -1) {
            n2 = this.calculateSize(false);
        } else if (n2 <= 0) {
            throw new IllegalArgumentException("Structure size must be greater than zero: " + n2);
        }
        if (n2 != -1) {
            if (this.memory == null || this.memory instanceof AutoAllocated) {
                this.memory = this.autoAllocate(n2);
            }
            this.size = n2;
        }
    }

    public int size() {
        this.ensureAllocated();
        return this.size;
    }

    public void clear() {
        this.ensureAllocated();
        this.nativeStrings.clear();
        this.memory.clear(this.size());
    }

    public Pointer getPointer() {
        this.ensureAllocated();
        return this.memory;
    }

    static Set<Structure> busy() {
        return busy.get();
    }

    static Map<Pointer, Structure> reading() {
        return reads.get();
    }

    void conditionalAutoRead() {
        if (!this.readCalled) {
            this.autoRead();
        }
    }

    public void read() {
        if (this.memory == PLACEHOLDER_MEMORY) {
            return;
        }
        this.readCalled = true;
        this.ensureAllocated();
        if (!Structure.busy().add(this)) {
            return;
        }
        if (this instanceof ByReference) {
            Structure.reading().put(this.getPointer(), this);
        }
        try {
            for (StructField structField : this.fields().values()) {
                this.readField(structField);
            }
            Structure.busy().remove(this);
        }
        catch (Throwable throwable) {
            Structure.busy().remove(this);
            if (this instanceof ByReference && Structure.reading().get(this.getPointer()) == this) {
                Structure.reading().remove(this.getPointer());
            }
            throw throwable;
        }
        if (this instanceof ByReference && Structure.reading().get(this.getPointer()) == this) {
            Structure.reading().remove(this.getPointer());
            return;
        }
    }

    public int fieldOffset(String string) {
        this.ensureAllocated();
        StructField structField = this.fields().get(string);
        if (structField == null) {
            throw new IllegalArgumentException("No such field: " + string);
        }
        return structField.offset;
    }

    public Object readField(String string) {
        this.ensureAllocated();
        StructField structField = this.fields().get(string);
        if (structField == null) {
            throw new IllegalArgumentException("No such field: " + string);
        }
        return this.readField(structField);
    }

    Object getFieldValue(Field field) {
        try {
            return field.get(this);
        }
        catch (Exception exception) {
            throw new Error("Exception reading field '" + field.getName() + "' in " + this.getClass(), exception);
        }
    }

    void setFieldValue(Field field, Object object) {
        this.setFieldValue(field, object, false);
    }

    private void setFieldValue(Field field, Object object, boolean bl) {
        try {
            field.set(this, object);
            return;
        }
        catch (IllegalAccessException illegalAccessException) {
            int n2 = field.getModifiers();
            if (Modifier.isFinal(n2)) {
                if (bl) {
                    throw new UnsupportedOperationException("This VM does not support Structures with final fields (field '" + field.getName() + "' within " + this.getClass() + ")", illegalAccessException);
                }
                throw new UnsupportedOperationException("Attempt to write to read-only field '" + field.getName() + "' within " + this.getClass(), illegalAccessException);
            }
            throw new Error("Unexpectedly unable to write to field '" + field.getName() + "' within " + this.getClass(), illegalAccessException);
        }
    }

    static <T extends Structure> T updateStructureByReference(Class<T> clazz, T object, Pointer pointer) {
        if (pointer == null) {
            object = null;
        } else if (object == null || !pointer.equals(((Structure)object).getPointer())) {
            object = Structure.reading().get(pointer);
            if (object != null && clazz.equals(object.getClass())) {
                ((Structure)object).autoRead();
            } else {
                object = Structure.newInstance(clazz, pointer);
                ((Structure)object).conditionalAutoRead();
            }
        } else {
            ((Structure)object).autoRead();
        }
        return (T)object;
    }

    protected Object readField(StructField structField) {
        Object object;
        Object object2;
        int n2 = structField.offset;
        Object object3 = structField.type;
        FromNativeConverter fromNativeConverter = structField.readConverter;
        if (fromNativeConverter != null) {
            object3 = fromNativeConverter.nativeType();
        }
        Object object4 = object2 = Structure.class.isAssignableFrom((Class<?>)object3) || Callback.class.isAssignableFrom((Class<?>)object3) || Platform.HAS_BUFFERS && Buffer.class.isAssignableFrom((Class<?>)object3) || Pointer.class.isAssignableFrom((Class<?>)object3) || NativeMapped.class.isAssignableFrom((Class<?>)object3) || ((Class)object3).isArray() ? this.getFieldValue(structField.field) : null;
        Object object5 = object3 == String.class ? ((object = this.memory.getPointer(n2)) == null ? null : ((Pointer)object).getString(0L, this.encoding)) : this.memory.getValue(n2, (Class<?>)object3, object2);
        if (fromNativeConverter != null) {
            object5 = fromNativeConverter.fromNative(object5, structField.context);
            if (object2 != null && object2.equals(object5)) {
                object5 = object2;
            }
        }
        if (object3.equals(String.class) || object3.equals(WString.class)) {
            if (object5 != null) {
                object = new NativeStringTracking(object5);
                object3 = this.nativeStrings.put(structField.name, (NativeStringTracking)object);
                if (object3 != null) {
                    ((NativeStringTracking)object).peer = ((NativeStringTracking)object3).peer;
                }
            } else {
                this.nativeStrings.remove(structField.name);
            }
        }
        this.setFieldValue(structField.field, object5, true);
        return object5;
    }

    public void write() {
        if (this.memory == PLACEHOLDER_MEMORY) {
            return;
        }
        this.ensureAllocated();
        if (this instanceof ByValue) {
            this.getTypeInfo();
        }
        if (!Structure.busy().add(this)) {
            return;
        }
        try {
            for (StructField structField : this.fields().values()) {
                if (structField.isVolatile) continue;
                this.writeField(structField);
            }
            return;
        }
        finally {
            Structure.busy().remove(this);
        }
    }

    public void writeField(String string) {
        this.ensureAllocated();
        StructField structField = this.fields().get(string);
        if (structField == null) {
            throw new IllegalArgumentException("No such field: " + string);
        }
        this.writeField(structField);
    }

    public void writeField(String string, Object object) {
        this.ensureAllocated();
        StructField structField = this.fields().get(string);
        if (structField == null) {
            throw new IllegalArgumentException("No such field: " + string);
        }
        this.setFieldValue(structField.field, object);
        this.writeField(structField, object);
    }

    protected void writeField(StructField structField) {
        if (structField.isReadOnly) {
            return;
        }
        Object object = this.getFieldValue(structField.field);
        this.writeField(structField, object);
    }

    private void writeField(StructField structField, Object object) {
        int n2 = structField.offset;
        Class<?> clazz = structField.type;
        Object object2 = structField.writeConverter;
        if (object2 != null) {
            object = object2.toNative(object, new StructureWriteContext(this, structField.field));
            clazz = object2.nativeType();
        }
        if (String.class == clazz || WString.class == clazz) {
            if (object != null) {
                object2 = new NativeStringTracking(object);
                NativeStringTracking nativeStringTracking = this.nativeStrings.put(structField.name, (NativeStringTracking)object2);
                if (nativeStringTracking != null && object.equals(nativeStringTracking.value)) {
                    ((NativeStringTracking)object2).peer = nativeStringTracking.peer;
                    return;
                }
                boolean bl = clazz == WString.class;
                object = bl ? new NativeString(object.toString(), true) : new NativeString(object.toString(), this.encoding);
                ((NativeStringTracking)object2).peer = (NativeString)object;
                object = ((NativeString)object).getPointer();
            } else {
                this.nativeStrings.remove(structField.name);
            }
        }
        try {
            this.memory.setValue(n2, object, clazz);
            return;
        }
        catch (IllegalArgumentException illegalArgumentException) {
            String string = "Structure field \"" + structField.name + "\" was declared as " + structField.type + (structField.type == clazz ? "" : " (native type " + clazz + ")") + ", which is not supported within a Structure";
            throw new IllegalArgumentException(string, illegalArgumentException);
        }
    }

    public List<String> getFieldOrder() {
        LinkedList<String> linkedList = new LinkedList<String>();
        for (Class<?> clazz = this.getClass(); clazz != Structure.class; clazz = clazz.getSuperclass()) {
            FieldOrder fieldOrder = clazz.getAnnotation(FieldOrder.class);
            if (fieldOrder == null) continue;
            linkedList.addAll(0, Arrays.asList(fieldOrder.value()));
        }
        return Collections.unmodifiableList(linkedList);
    }

    protected void sortFields(List<Field> list2, List<String> list3) {
        block0: for (int i2 = 0; i2 < list3.size(); ++i2) {
            String string = list3.get(i2);
            for (int i3 = 0; i3 < list2.size(); ++i3) {
                Field field = list2.get(i3);
                if (!string.equals(field.getName())) continue;
                Collections.swap(list2, i2, i3);
                continue block0;
            }
        }
    }

    public List<Field> getFieldList() {
        ArrayList<Field> arrayList = new ArrayList<Field>();
        Class<?> clazz = this.getClass();
        while (!clazz.equals(Structure.class)) {
            ArrayList<Field> arrayList2 = new ArrayList<Field>();
            Field[] fieldArray = clazz.getDeclaredFields();
            for (int i2 = 0; i2 < fieldArray.length; ++i2) {
                int n2 = fieldArray[i2].getModifiers();
                if (Modifier.isStatic(n2) || !Modifier.isPublic(n2)) continue;
                arrayList2.add(fieldArray[i2]);
            }
            arrayList.addAll(0, arrayList2);
            clazz = clazz.getSuperclass();
        }
        return arrayList;
    }

    private List<String> fieldOrder() {
        Class<?> clazz = this.getClass();
        Map<Class<?>, List<String>> map = fieldOrder;
        synchronized (map) {
            List<String> list2 = fieldOrder.get(clazz);
            if (list2 == null) {
                list2 = this.getFieldOrder();
                fieldOrder.put(clazz, list2);
            }
            return list2;
        }
    }

    public static List<String> createFieldsOrder(List<String> list2, String ... stringArray) {
        return Structure.createFieldsOrder(list2, Arrays.asList(stringArray));
    }

    public static List<String> createFieldsOrder(List<String> list2, List<String> list3) {
        ArrayList<String> arrayList = new ArrayList<String>(list2.size() + list3.size());
        arrayList.addAll(list2);
        arrayList.addAll(list3);
        return Collections.unmodifiableList(arrayList);
    }

    public static List<String> createFieldsOrder(String string) {
        return Collections.unmodifiableList(Collections.singletonList(string));
    }

    public static List<String> createFieldsOrder(String ... stringArray) {
        return Collections.unmodifiableList(Arrays.asList(stringArray));
    }

    private static <T extends Comparable<T>> List<T> sort(Collection<? extends T> collection) {
        collection = new ArrayList<T>(collection);
        Collections.sort(collection);
        return collection;
    }

    protected List<Field> getFields(boolean bl) {
        List<Field> list2 = this.getFieldList();
        HashSet<String> hashSet2 = new HashSet<String>();
        for (Field hashSet3 : list2) {
            hashSet2.add(hashSet3.getName());
        }
        List<String> list3 = this.fieldOrder();
        if (list3.size() != list2.size() && list2.size() > 1) {
            if (bl) {
                throw new Error("Structure.getFieldOrder() on " + this.getClass() + (list3.size() < list2.size() ? " does not provide enough" : " provides too many") + " names [" + list3.size() + "] (" + Structure.sort(list3) + ") to match declared fields [" + list2.size() + "] (" + Structure.sort(hashSet2) + ")");
            }
            return null;
        }
        HashSet hashSet = new HashSet(list3);
        if (!hashSet.equals(hashSet2)) {
            throw new Error("Structure.getFieldOrder() on " + this.getClass() + " returns names (" + Structure.sort(list3) + ") which do not match declared field names (" + Structure.sort(hashSet2) + ")");
        }
        this.sortFields(list2, list3);
        return list2;
    }

    protected int calculateSize(boolean bl) {
        return this.calculateSize(bl, false);
    }

    static int size(Class<? extends Structure> clazz) {
        return Structure.size(clazz, null);
    }

    static <T extends Structure> int size(Class<T> clazz, T t2) {
        LayoutInfo layoutInfo;
        Map<Class<?>, LayoutInfo> map = Structure.layoutInfo;
        synchronized (map) {
            layoutInfo = Structure.layoutInfo.get(clazz);
        }
        int n2 = layoutInfo != null && !layoutInfo.variable ? layoutInfo.size : -1;
        if (n2 == -1) {
            if (t2 == null) {
                t2 = Structure.newInstance(clazz, PLACEHOLDER_MEMORY);
            }
            n2 = t2.size();
        }
        return n2;
    }

    int calculateSize(boolean bl, boolean bl2) {
        LayoutInfo layoutInfo;
        int n2 = -1;
        Class<?> clazz = this.getClass();
        Map<Class<?>, LayoutInfo> map = Structure.layoutInfo;
        synchronized (map) {
            layoutInfo = Structure.layoutInfo.get(clazz);
        }
        if (layoutInfo == null || this.alignType != layoutInfo.alignType || this.typeMapper != layoutInfo.typeMapper) {
            layoutInfo = this.deriveLayout(bl, bl2);
        }
        if (layoutInfo != null) {
            this.structAlignment = layoutInfo.alignment;
            this.structFields = layoutInfo.fields;
            if (!layoutInfo.variable) {
                map = Structure.layoutInfo;
                synchronized (map) {
                    if (!Structure.layoutInfo.containsKey(clazz) || this.alignType != 0 || this.typeMapper != null) {
                        Structure.layoutInfo.put(clazz, layoutInfo);
                    }
                }
            }
            n2 = layoutInfo.size;
        }
        return n2;
    }

    private void validateField(String string, Class<?> clazz) {
        ToNativeConverter toNativeConverter;
        while (this.typeMapper != null && (toNativeConverter = this.typeMapper.getToNativeConverter(clazz)) != null) {
            clazz = toNativeConverter.nativeType();
        }
        if (clazz.isArray()) {
            this.validateField(string, clazz.getComponentType());
            return;
        }
        try {
            this.getNativeSize(clazz);
            return;
        }
        catch (IllegalArgumentException illegalArgumentException) {
            string = "Invalid Structure field in " + this.getClass() + ", field name '" + string + "' (" + clazz + "): " + illegalArgumentException.getMessage();
            throw new IllegalArgumentException(string, illegalArgumentException);
        }
    }

    private void validateFields() {
        Object object = this.getFieldList();
        object = object.iterator();
        while (object.hasNext()) {
            Field field = (Field)object.next();
            this.validateField(field.getName(), field.getType());
        }
    }

    private LayoutInfo deriveLayout(boolean bl, boolean bl2) {
        int n2 = 0;
        List<Field> list2 = this.getFields(bl);
        if (list2 == null) {
            return null;
        }
        LayoutInfo layoutInfo = new LayoutInfo();
        layoutInfo.alignType = this.alignType;
        layoutInfo.typeMapper = this.typeMapper;
        int n3 = 1;
        for (Field field : list2) {
            int n4 = field.getModifiers();
            Class<?> clazz = field.getType();
            if (clazz.isArray()) {
                layoutInfo.variable = true;
            }
            StructField structField = new StructField();
            new StructField().isVolatile = Modifier.isVolatile(n4);
            structField.isReadOnly = Modifier.isFinal(n4);
            if (structField.isReadOnly) {
                if (!Platform.RO_FIELDS) {
                    throw new IllegalArgumentException("This VM does not support read-only fields (field '" + field.getName() + "' within " + this.getClass() + ")");
                }
                field.setAccessible(true);
            }
            structField.field = field;
            structField.name = field.getName();
            structField.type = clazz;
            if (Callback.class.isAssignableFrom(clazz) && !clazz.isInterface()) {
                throw new IllegalArgumentException("Structure Callback field '" + field.getName() + "' must be an interface");
            }
            if (clazz.isArray() && Structure.class.equals(clazz.getComponentType())) {
                String string = "Nested Structure arrays must use a derived Structure type so that the size of the elements can be determined";
                throw new IllegalArgumentException(string);
            }
            if (Modifier.isPublic(field.getModifiers())) {
                Object object;
                ToNativeConverter toNativeConverter;
                Object object2 = this.getFieldValue(structField.field);
                if (object2 == null && clazz.isArray()) {
                    if (bl) {
                        throw new IllegalStateException("Array fields must be initialized");
                    }
                    return null;
                }
                Class<Object> clazz2 = clazz;
                if (NativeMapped.class.isAssignableFrom(clazz)) {
                    toNativeConverter = NativeMappedConverter.getInstance(clazz);
                    clazz2 = ((NativeMappedConverter)toNativeConverter).nativeType();
                    structField.writeConverter = toNativeConverter;
                    structField.readConverter = toNativeConverter;
                    structField.context = new StructureReadContext(this, field);
                } else if (this.typeMapper != null) {
                    toNativeConverter = this.typeMapper.getToNativeConverter(clazz);
                    object = this.typeMapper.getFromNativeConverter(clazz);
                    if (toNativeConverter != null && object != null) {
                        clazz2 = (object2 = toNativeConverter.toNative(object2, new StructureWriteContext(this, structField.field))) != null ? object2.getClass() : Pointer.class;
                        structField.writeConverter = toNativeConverter;
                        structField.readConverter = object;
                        structField.context = new StructureReadContext(this, field);
                    } else if (toNativeConverter != null || object != null) {
                        String string = "Structures require bidirectional type conversion for " + clazz;
                        throw new IllegalArgumentException(string);
                    }
                }
                if (object2 == null) {
                    object2 = this.initializeField(structField.field, clazz);
                }
                try {
                    structField.size = this.getNativeSize(clazz2, object2);
                    n3 = this.getNativeAlignment(clazz2, object2, n3 != 0);
                }
                catch (IllegalArgumentException illegalArgumentException) {
                    if (!bl && this.typeMapper == null) {
                        return null;
                    }
                    object = "Invalid Structure field in " + this.getClass() + ", field name '" + structField.name + "' (" + structField.type + "): " + illegalArgumentException.getMessage();
                    throw new IllegalArgumentException((String)object, illegalArgumentException);
                }
                if (n3 == 0) {
                    throw new Error("Field alignment is zero for field '" + structField.name + "' within " + this.getClass());
                }
                LayoutInfo layoutInfo2 = layoutInfo;
                layoutInfo2.alignment = Math.max(layoutInfo2.alignment, n3);
                if (n2 % n3 != 0) {
                    n2 += n3 - n2 % n3;
                }
                if (this instanceof Union) {
                    structField.offset = 0;
                    n2 = Math.max(n2, structField.size);
                } else {
                    structField.offset = n2;
                    n2 += structField.size;
                }
                layoutInfo.fields.put(structField.name, structField);
            }
            n3 = 0;
        }
        if (n2 > 0) {
            int n5 = this.addPadding(n2, layoutInfo.alignment);
            if (this instanceof ByValue && !bl2) {
                this.getTypeInfo();
            }
            layoutInfo.size = n5;
            return layoutInfo;
        }
        throw new IllegalArgumentException("Structure " + this.getClass() + " has unknown or zero size (ensure all fields are public)");
    }

    private void initializeFields() {
        Object object = this.getFieldList();
        object = object.iterator();
        while (object.hasNext()) {
            Field field = (Field)object.next();
            try {
                Object object2 = field.get(this);
                if (object2 != null) continue;
                Field field2 = field;
                this.initializeField(field2, field2.getType());
            }
            catch (Exception exception) {
                throw new Error("Exception reading field '" + field.getName() + "' in " + this.getClass(), exception);
            }
        }
    }

    private Object initializeField(Field object, Class<?> object2) {
        NativeMapped nativeMapped = null;
        if (Structure.class.isAssignableFrom((Class<?>)object2) && !ByReference.class.isAssignableFrom((Class<?>)object2)) {
            try {
                nativeMapped = (NativeMapped)Structure.newInstance(object2, PLACEHOLDER_MEMORY);
                this.setFieldValue((Field)object, nativeMapped);
            }
            catch (IllegalArgumentException illegalArgumentException) {
                object = "Can't determine size of nested structure";
                throw new IllegalArgumentException((String)object, illegalArgumentException);
            }
        } else if (NativeMapped.class.isAssignableFrom((Class<?>)object2)) {
            object2 = NativeMappedConverter.getInstance(object2);
            nativeMapped = ((NativeMappedConverter)object2).defaultValue();
            this.setFieldValue((Field)object, nativeMapped);
        }
        return nativeMapped;
    }

    private int addPadding(int n2) {
        return this.addPadding(n2, this.structAlignment);
    }

    private int addPadding(int n2, int n3) {
        if (this.actualAlignType != 1 && n2 % n3 != 0) {
            n2 += n3 - n2 % n3;
        }
        return n2;
    }

    protected int getStructAlignment() {
        if (this.size == -1) {
            this.calculateSize(true);
        }
        return this.structAlignment;
    }

    protected int getNativeAlignment(Class<?> clazz, Object object, boolean bl) {
        int n2;
        if (NativeMapped.class.isAssignableFrom(clazz)) {
            NativeMappedConverter nativeMappedConverter = NativeMappedConverter.getInstance(clazz);
            clazz = nativeMappedConverter.nativeType();
            object = nativeMappedConverter.toNative(object, new ToNativeContext());
        }
        int n3 = Native.getNativeSize(clazz, object);
        if (clazz.isPrimitive() || Long.class == clazz || Integer.class == clazz || Short.class == clazz || Character.class == clazz || Byte.class == clazz || Boolean.class == clazz || Float.class == clazz || Double.class == clazz) {
            n2 = n3;
        } else if (Pointer.class.isAssignableFrom(clazz) && !Function.class.isAssignableFrom(clazz) || Platform.HAS_BUFFERS && Buffer.class.isAssignableFrom(clazz) || Callback.class.isAssignableFrom(clazz) || WString.class == clazz || String.class == clazz) {
            n2 = Native.POINTER_SIZE;
        } else if (Structure.class.isAssignableFrom(clazz)) {
            if (ByReference.class.isAssignableFrom(clazz)) {
                n2 = Native.POINTER_SIZE;
            } else {
                if (object == null) {
                    object = Structure.newInstance(clazz, PLACEHOLDER_MEMORY);
                }
                n2 = ((Structure)object).getStructAlignment();
            }
        } else if (clazz.isArray()) {
            n2 = this.getNativeAlignment(clazz.getComponentType(), null, bl);
        } else {
            throw new IllegalArgumentException("Type " + clazz + " has unknown native alignment");
        }
        if (this.actualAlignType == 1) {
            n2 = 1;
        } else if (this.actualAlignType == 3) {
            n2 = Math.min(8, n2);
        } else if (this.actualAlignType == 2) {
            if (!(bl && Platform.isMac() && Platform.isPPC())) {
                n2 = Math.min(Native.MAX_ALIGNMENT, n2);
            }
            if (!bl && Platform.isAIX() && (clazz == Double.TYPE || clazz == Double.class)) {
                n2 = 4;
            }
        }
        return n2;
    }

    public String toString() {
        return this.toString(Boolean.getBoolean("jna.dump_memory"));
    }

    public String toString(boolean bl) {
        return this.toString(0, true, bl);
    }

    private String format(Class<?> object) {
        object = ((Class)object).getName();
        int n2 = ((String)object).lastIndexOf(".");
        return ((String)object).substring(n2 + 1);
    }

    private String toString(int n2, boolean bl, boolean bl2) {
        Object object;
        this.ensureAllocated();
        String string = System.lineSeparator();
        Structure structure = this;
        String string2 = structure.format(structure.getClass()) + "(" + this.getPointer() + ")";
        if (!(this.getPointer() instanceof Memory)) {
            string2 = string2 + " (" + this.size() + " bytes)";
        }
        String string3 = "";
        for (int i2 = 0; i2 < n2; ++i2) {
            string3 = string3 + "  ";
        }
        String string4 = string;
        if (!bl) {
            string4 = "...}";
        } else {
            Iterator<StructField> iterator2 = this.fields().values().iterator();
            while (iterator2.hasNext()) {
                object = iterator2.next();
                Object object2 = this.getFieldValue(((StructField)object).field);
                String string5 = this.format(((StructField)object).type);
                String string6 = "";
                string4 = string4 + string3;
                if (((StructField)object).type.isArray() && object2 != null) {
                    string5 = this.format(((StructField)object).type.getComponentType());
                    string6 = "[" + Array.getLength(object2) + "]";
                }
                string4 = string4 + String.format("  %s %s%s@0x%X", string5, ((StructField)object).name, string6, ((StructField)object).offset);
                if (object2 instanceof Structure) {
                    object2 = ((Structure)object2).toString(n2 + 1, !(object2 instanceof ByReference), bl2);
                }
                string4 = string4 + "=";
                string4 = object2 instanceof Long ? string4 + String.format("0x%08X", (Long)object2) : (object2 instanceof Integer ? string4 + String.format("0x%04X", (Integer)object2) : (object2 instanceof Short ? string4 + String.format("0x%02X", (Short)object2) : (object2 instanceof Byte ? string4 + String.format("0x%01X", (Byte)object2) : string4 + String.valueOf(object2).trim())));
                string4 = string4 + string;
                if (iterator2.hasNext()) continue;
                string4 = string4 + string3 + "}";
            }
        }
        if (n2 == 0 && bl2) {
            string4 = string4 + string + "memory dump" + string;
            object = this.getPointer().getByteArray(0L, this.size());
            for (int i3 = 0; i3 < ((Object)object).length; ++i3) {
                if (i3 % 4 == 0) {
                    string4 = string4 + "[";
                }
                if (object[i3] >= 0 && object[i3] < 16) {
                    string4 = string4 + "0";
                }
                string4 = string4 + Integer.toHexString(object[i3] & 0xFF);
                if (i3 % 4 != 3 || i3 >= ((Object)object).length - 1) continue;
                string4 = string4 + "]" + string;
            }
            string4 = string4 + "]";
        }
        return string2 + " {" + string4;
    }

    public Structure[] toArray(Structure[] structureArray) {
        int n2;
        this.ensureAllocated();
        if (this.memory instanceof AutoAllocated) {
            Memory memory = (Memory)this.memory;
            n2 = structureArray.length * this.size();
            if (memory.size() < (long)n2) {
                Structure structure = this;
                structure.useMemory(structure.autoAllocate(n2));
            }
        }
        structureArray[0] = this;
        int n3 = this.size();
        for (n2 = 1; n2 < structureArray.length; ++n2) {
            structureArray[n2] = Structure.newInstance(this.getClass(), this.memory.share(n2 * n3, n3));
            structureArray[n2].conditionalAutoRead();
        }
        if (!(this instanceof ByValue)) {
            this.array = structureArray;
        }
        return structureArray;
    }

    public Structure[] toArray(int n2) {
        Structure structure = this;
        return structure.toArray((Structure[])Array.newInstance(structure.getClass(), n2));
    }

    private Class<?> baseClass() {
        if ((this instanceof ByReference || this instanceof ByValue) && Structure.class.isAssignableFrom(this.getClass().getSuperclass())) {
            return this.getClass().getSuperclass();
        }
        return this.getClass();
    }

    public boolean dataEquals(Structure structure) {
        return this.dataEquals(structure, false);
    }

    public boolean dataEquals(Structure object, boolean bl) {
        byte[] byArray;
        if (bl) {
            ((Structure)object).getPointer().clear(((Structure)object).size());
            ((Structure)object).write();
            this.getPointer().clear(this.size());
            this.write();
        }
        if (((Object)(object = (Object)((Structure)object).getPointer().getByteArray(0L, ((Structure)object).size()))).length == (byArray = this.getPointer().getByteArray(0L, this.size())).length) {
            for (int i2 = 0; i2 < ((Object)object).length; ++i2) {
                if (object[i2] == byArray[i2]) continue;
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean equals(Object object) {
        return object instanceof Structure && object.getClass() == this.getClass() && ((Structure)object).getPointer().equals(this.getPointer());
    }

    public int hashCode() {
        Pointer pointer = this.getPointer();
        if (pointer != null) {
            return this.getPointer().hashCode();
        }
        return this.getClass().hashCode();
    }

    protected void cacheTypeInfo(Pointer pointer) {
        this.typeInfo = pointer.peer;
    }

    FFIType getFieldTypeInfo(StructField object) {
        ToNativeConverter toNativeConverter;
        Class<?> clazz = ((StructField)object).type;
        object = this.getFieldValue(((StructField)object).field);
        if (this.typeMapper != null && (toNativeConverter = this.typeMapper.getToNativeConverter(clazz)) != null) {
            clazz = toNativeConverter.nativeType();
            object = toNativeConverter.toNative(object, new ToNativeContext());
        }
        return FFIType.get(object, clazz);
    }

    Pointer getTypeInfo() {
        Pointer pointer = Structure.getTypeInfo(this).getPointer();
        this.cacheTypeInfo(pointer);
        return pointer;
    }

    public void setAutoSynch(boolean bl) {
        this.setAutoRead(bl);
        this.setAutoWrite(bl);
    }

    public void setAutoRead(boolean bl) {
        this.autoRead = bl;
    }

    public boolean getAutoRead() {
        return this.autoRead;
    }

    public void setAutoWrite(boolean bl) {
        this.autoWrite = bl;
    }

    public boolean getAutoWrite() {
        return this.autoWrite;
    }

    static FFIType getTypeInfo(Object object) {
        return FFIType.get(object);
    }

    private static <T extends Structure> T newInstance(Class<T> clazz, long l2) {
        try {
            clazz = Structure.newInstance(clazz, l2 == 0L ? PLACEHOLDER_MEMORY : new Pointer(l2));
            if (l2 != 0L) {
                ((Structure)((Object)clazz)).conditionalAutoRead();
            }
            return (T)clazz;
        }
        catch (Throwable throwable) {
            LOG.log(Level.WARNING, "JNA: Error creating structure", throwable);
            return null;
        }
    }

    public static <T extends Structure> T newInstance(Class<T> object, Pointer pointer) throws IllegalArgumentException {
        Constructor<T> constructor;
        try {
            constructor = Structure.getPointerConstructor(object);
            if (constructor != null) {
                return (T)((Structure)constructor.newInstance(pointer));
            }
        }
        catch (SecurityException securityException) {
        }
        catch (InstantiationException instantiationException) {
            object = "Can't instantiate " + object;
            throw new IllegalArgumentException((String)object, instantiationException);
        }
        catch (IllegalAccessException illegalAccessException) {
            object = "Instantiation of " + object + " (Pointer) not allowed, is it public?";
            throw new IllegalArgumentException((String)object, illegalAccessException);
        }
        catch (InvocationTargetException invocationTargetException) {
            object = "Exception thrown while instantiating an instance of " + object;
            throw new IllegalArgumentException((String)object, invocationTargetException);
        }
        constructor = Structure.newInstance(object);
        if (pointer != PLACEHOLDER_MEMORY) {
            ((Structure)((Object)constructor)).useMemory(pointer);
        }
        return (T)constructor;
    }

    public static <T extends Structure> T newInstance(Class<T> object) throws IllegalArgumentException {
        if ((object = (Structure)Klass.newInstance(object)) instanceof ByValue) {
            ((Structure)object).allocateMemory();
        }
        return (T)object;
    }

    private static <T> Constructor<T> getPointerConstructor(Class<T> constructorArray) {
        for (Constructor<?> constructor : constructorArray.getConstructors()) {
            Class<?>[] classArray = constructor.getParameterTypes();
            if (classArray.length != 1 || !classArray[0].equals(Pointer.class)) continue;
            return constructor;
        }
        return null;
    }

    private static void structureArrayCheck(Structure[] object) {
        if (ByReference[].class.isAssignableFrom(object.getClass())) {
            return;
        }
        Pointer pointer = object[0].getPointer();
        int n2 = object[0].size();
        for (int i2 = 1; i2 < ((Structure[])object).length; ++i2) {
            if (object[i2].getPointer().peer == pointer.peer + (long)(n2 * i2)) continue;
            object = "Structure array elements must use contiguous memory (bad backing address at Structure array index " + i2 + ")";
            throw new IllegalArgumentException((String)object);
        }
    }

    public static void autoRead(Structure[] structureArray) {
        Structure.structureArrayCheck(structureArray);
        if (structureArray[0].array == structureArray) {
            structureArray[0].autoRead();
            return;
        }
        for (int i2 = 0; i2 < structureArray.length; ++i2) {
            if (structureArray[i2] == null) continue;
            structureArray[i2].autoRead();
        }
    }

    public void autoRead() {
        if (this.getAutoRead()) {
            this.read();
            if (this.array != null) {
                for (int i2 = 1; i2 < this.array.length; ++i2) {
                    this.array[i2].autoRead();
                }
            }
        }
    }

    public static void autoWrite(Structure[] structureArray) {
        Structure.structureArrayCheck(structureArray);
        if (structureArray[0].array == structureArray) {
            structureArray[0].autoWrite();
            return;
        }
        for (int i2 = 0; i2 < structureArray.length; ++i2) {
            if (structureArray[i2] == null) continue;
            structureArray[i2].autoWrite();
        }
    }

    public void autoWrite() {
        if (this.getAutoWrite()) {
            this.write();
            if (this.array != null) {
                for (int i2 = 1; i2 < this.array.length; ++i2) {
                    this.array[i2].autoWrite();
                }
            }
        }
    }

    protected int getNativeSize(Class<?> clazz) {
        return this.getNativeSize(clazz, null);
    }

    protected int getNativeSize(Class<?> clazz, Object object) {
        return Native.getNativeSize(clazz, object);
    }

    static void validate(Class<? extends Structure> clazz) {
        try {
            clazz.getConstructor(new Class[0]);
            return;
        }
        catch (NoSuchMethodException | SecurityException exception) {
            throw new IllegalArgumentException("No suitable constructor found for class: " + clazz.getName());
        }
    }

    static class AutoAllocated
    extends Memory {
        public AutoAllocated(int n2) {
            super(n2);
            super.clear();
        }

        @Override
        public String toString() {
            return "auto-" + super.toString();
        }
    }

    @FieldOrder(value={"size", "alignment", "type", "elements"})
    static class FFIType
    extends Structure {
        private static final Map<Class, Map<Integer, FFIType>> typeInfoMap = new WeakHashMap<Class, Map<Integer, FFIType>>();
        private static final Map<Class, FFIType> unionHelper = new WeakHashMap<Class, FFIType>();
        private static final Map<Pointer, FFIType> ffiTypeInfo = new HashMap<Pointer, FFIType>();
        private static final int FFI_TYPE_STRUCT = 13;
        public size_t size;
        public short alignment;
        public short type = (short)13;
        public Pointer elements;

        private static boolean isIntegerType(FFIType object) {
            return ((Pointer)(object = ((Structure)object).getPointer())).equals(FFITypes.ffi_type_uint8) || ((Pointer)object).equals(FFITypes.ffi_type_sint8) || ((Pointer)object).equals(FFITypes.ffi_type_uint16) || ((Pointer)object).equals(FFITypes.ffi_type_sint16) || ((Pointer)object).equals(FFITypes.ffi_type_uint32) || ((Pointer)object).equals(FFITypes.ffi_type_sint32) || ((Pointer)object).equals(FFITypes.ffi_type_uint64) || ((Pointer)object).equals(FFITypes.ffi_type_sint64) || ((Pointer)object).equals(FFITypes.ffi_type_pointer);
        }

        private static boolean isFloatType(FFIType object) {
            return ((Pointer)(object = ((Structure)object).getPointer())).equals(FFITypes.ffi_type_float) || ((Pointer)object).equals(FFITypes.ffi_type_double);
        }

        public FFIType(FFIType fFIType) {
            this.size = fFIType.size;
            this.alignment = fFIType.alignment;
            this.type = fFIType.type;
            this.elements = fFIType.elements;
        }

        public FFIType() {
        }

        public FFIType(Structure structure) {
            Object object;
            structure.ensureAllocated(true);
            if (structure instanceof Union) {
                Structure structure2 = null;
                int n2 = 0;
                boolean bl = false;
                for (StructField structField : structure.fields().values()) {
                    FFIType fFIType = structure.getFieldTypeInfo(structField);
                    if (FFIType.isIntegerType(fFIType)) {
                        bl = true;
                    }
                    if (structure2 != null && n2 >= structField.size && (n2 != structField.size || !Structure.class.isAssignableFrom(structField.type))) continue;
                    structure2 = fFIType;
                    n2 = structField.size;
                }
                if ((Platform.isIntel() && Platform.is64Bit() && !Platform.isWindows() || Platform.isARM() || Platform.isLoongArch()) && bl && FFIType.isFloatType(structure2)) {
                    structure2 = new FFIType((FFIType)structure2);
                    if (((FFIType)structure2).size.intValue() == 4) {
                        ((FFIType)structure2).type = FFIType.ffiTypeInfo.get((Object)FFITypes.ffi_type_uint32).type;
                    } else if (((FFIType)structure2).size.intValue() == 8) {
                        ((FFIType)structure2).type = FFIType.ffiTypeInfo.get((Object)FFITypes.ffi_type_uint64).type;
                    }
                    structure2.write();
                }
                object = new Pointer[]{structure2.getPointer(), null};
                unionHelper.put(structure.getClass(), (FFIType)structure2);
            } else {
                object = new Pointer[structure.fields().size() + 1];
                int n3 = 0;
                for (StructField structField : structure.fields().values()) {
                    object[n3++] = structure.getFieldTypeInfo(structField).getPointer();
                }
            }
            this.init((Pointer[])object);
            this.write();
        }

        public FFIType(Object object, Class<?> object2) {
            int n2 = Array.getLength(object);
            Pointer[] pointerArray = new Pointer[n2 + 1];
            object2 = FFIType.get(null, ((Class)object2).getComponentType()).getPointer();
            for (int i2 = 0; i2 < n2; ++i2) {
                pointerArray[i2] = object2;
            }
            this.init(pointerArray);
            this.write();
        }

        private void init(Pointer[] pointerArray) {
            this.elements = new Memory(Native.POINTER_SIZE * pointerArray.length);
            this.elements.write(0L, pointerArray, 0, pointerArray.length);
            this.write();
        }

        static FFIType get(Object map) {
            if (map == null) {
                map = typeInfoMap;
                synchronized (map) {
                    return FFIType.getTypeInfo(Pointer.class, 0);
                }
            }
            if (map instanceof Class) {
                return FFIType.get(null, (Class)((Object)map));
            }
            Map<Class, Map<Integer, FFIType>> map2 = map;
            return FFIType.get(map2, map2.getClass());
        }

        private static FFIType get(Object object, Class<?> clazz) {
            Object object2 = Native.getTypeMapper(clazz);
            if (object2 != null && (object2 = object2.getToNativeConverter(clazz)) != null) {
                clazz = object2.nativeType();
            }
            object2 = typeInfoMap;
            synchronized (object2) {
                Class<?> clazz2 = clazz;
                Object object3 = FFIType.getTypeInfo(clazz2, clazz2.isArray() ? Array.getLength(object) : 0);
                if (object3 != null) {
                    return object3;
                }
                if (Platform.HAS_BUFFERS && Buffer.class.isAssignableFrom(clazz) || Callback.class.isAssignableFrom(clazz)) {
                    typeInfoMap.put(clazz, typeInfoMap.get(Pointer.class));
                    return typeInfoMap.get(Pointer.class).get(0);
                }
                if (Structure.class.isAssignableFrom(clazz)) {
                    if (object == null) {
                        object = FFIType.newInstance(clazz, PLACEHOLDER_MEMORY);
                    }
                    if (ByReference.class.isAssignableFrom(clazz)) {
                        typeInfoMap.put(clazz, typeInfoMap.get(Pointer.class));
                        return typeInfoMap.get(Pointer.class).get(0);
                    }
                    object3 = new FFIType((Structure)object);
                    FFIType.storeTypeInfo(clazz, (FFIType)object3);
                    return object3;
                }
                if (NativeMapped.class.isAssignableFrom(clazz)) {
                    object3 = NativeMappedConverter.getInstance(clazz);
                    return FFIType.get(((NativeMappedConverter)object3).toNative(object, new ToNativeContext()), ((NativeMappedConverter)object3).nativeType());
                }
                if (clazz.isArray()) {
                    object3 = new FFIType(object, clazz);
                    FFIType.storeTypeInfo(clazz, Array.getLength(object), (FFIType)object3);
                    return object3;
                }
                throw new IllegalArgumentException("Unsupported type " + clazz);
            }
        }

        private static FFIType getTypeInfo(Class object, int n2) {
            if ((object = typeInfoMap.get(object)) != null) {
                return (FFIType)object.get(n2);
            }
            return null;
        }

        private static void storeTypeInfo(Class clazz, FFIType fFIType) {
            FFIType.storeTypeInfo(clazz, 0, fFIType);
        }

        private static void storeTypeInfo(Class clazz, int n2, FFIType fFIType) {
            Map<Class, Map<Integer, FFIType>> map = typeInfoMap;
            synchronized (map) {
                Map<Integer, FFIType> map2 = typeInfoMap.get(clazz);
                if (map2 == null) {
                    map2 = new HashMap<Integer, FFIType>();
                    typeInfoMap.put(clazz, map2);
                }
                map2.put(n2, fFIType);
                return;
            }
        }

        static {
            if (Native.POINTER_SIZE == 0) {
                throw new Error("Native library not initialized");
            }
            if (FFITypes.ffi_type_void == null) {
                throw new Error("FFI types not initialized");
            }
            ffiTypeInfo.put(FFITypes.ffi_type_void, Structure.newInstance(FFIType.class, FFITypes.ffi_type_void));
            ffiTypeInfo.put(FFITypes.ffi_type_float, Structure.newInstance(FFIType.class, FFITypes.ffi_type_float));
            ffiTypeInfo.put(FFITypes.ffi_type_double, Structure.newInstance(FFIType.class, FFITypes.ffi_type_double));
            ffiTypeInfo.put(FFITypes.ffi_type_longdouble, Structure.newInstance(FFIType.class, FFITypes.ffi_type_longdouble));
            ffiTypeInfo.put(FFITypes.ffi_type_uint8, Structure.newInstance(FFIType.class, FFITypes.ffi_type_uint8));
            ffiTypeInfo.put(FFITypes.ffi_type_sint8, Structure.newInstance(FFIType.class, FFITypes.ffi_type_sint8));
            ffiTypeInfo.put(FFITypes.ffi_type_uint16, Structure.newInstance(FFIType.class, FFITypes.ffi_type_uint16));
            ffiTypeInfo.put(FFITypes.ffi_type_sint16, Structure.newInstance(FFIType.class, FFITypes.ffi_type_sint16));
            ffiTypeInfo.put(FFITypes.ffi_type_uint32, Structure.newInstance(FFIType.class, FFITypes.ffi_type_uint32));
            ffiTypeInfo.put(FFITypes.ffi_type_sint32, Structure.newInstance(FFIType.class, FFITypes.ffi_type_sint32));
            ffiTypeInfo.put(FFITypes.ffi_type_uint64, Structure.newInstance(FFIType.class, FFITypes.ffi_type_uint64));
            ffiTypeInfo.put(FFITypes.ffi_type_sint64, Structure.newInstance(FFIType.class, FFITypes.ffi_type_sint64));
            ffiTypeInfo.put(FFITypes.ffi_type_pointer, Structure.newInstance(FFIType.class, FFITypes.ffi_type_pointer));
            for (FFIType fFIType : ffiTypeInfo.values()) {
                fFIType.read();
            }
            FFIType.storeTypeInfo(Void.TYPE, ffiTypeInfo.get(FFITypes.ffi_type_void));
            FFIType.storeTypeInfo(Void.class, ffiTypeInfo.get(FFITypes.ffi_type_void));
            FFIType.storeTypeInfo(Float.TYPE, ffiTypeInfo.get(FFITypes.ffi_type_float));
            FFIType.storeTypeInfo(Float.class, ffiTypeInfo.get(FFITypes.ffi_type_float));
            FFIType.storeTypeInfo(Double.TYPE, ffiTypeInfo.get(FFITypes.ffi_type_double));
            FFIType.storeTypeInfo(Double.class, ffiTypeInfo.get(FFITypes.ffi_type_double));
            FFIType.storeTypeInfo(Long.TYPE, ffiTypeInfo.get(FFITypes.ffi_type_sint64));
            FFIType.storeTypeInfo(Long.class, ffiTypeInfo.get(FFITypes.ffi_type_sint64));
            FFIType.storeTypeInfo(Integer.TYPE, ffiTypeInfo.get(FFITypes.ffi_type_sint32));
            FFIType.storeTypeInfo(Integer.class, ffiTypeInfo.get(FFITypes.ffi_type_sint32));
            FFIType.storeTypeInfo(Short.TYPE, ffiTypeInfo.get(FFITypes.ffi_type_sint16));
            FFIType.storeTypeInfo(Short.class, ffiTypeInfo.get(FFITypes.ffi_type_sint16));
            FFIType fFIType = Native.WCHAR_SIZE == 2 ? ffiTypeInfo.get(FFITypes.ffi_type_uint16) : ffiTypeInfo.get(FFITypes.ffi_type_uint32);
            FFIType.storeTypeInfo(Character.TYPE, fFIType);
            FFIType.storeTypeInfo(Character.class, fFIType);
            FFIType.storeTypeInfo(Byte.TYPE, ffiTypeInfo.get(FFITypes.ffi_type_sint8));
            FFIType.storeTypeInfo(Byte.class, ffiTypeInfo.get(FFITypes.ffi_type_sint8));
            FFIType.storeTypeInfo(Pointer.class, ffiTypeInfo.get(FFITypes.ffi_type_pointer));
            FFIType.storeTypeInfo(String.class, ffiTypeInfo.get(FFITypes.ffi_type_pointer));
            FFIType.storeTypeInfo(WString.class, ffiTypeInfo.get(FFITypes.ffi_type_pointer));
            FFIType.storeTypeInfo(Boolean.TYPE, ffiTypeInfo.get(FFITypes.ffi_type_uint32));
            FFIType.storeTypeInfo(Boolean.class, ffiTypeInfo.get(FFITypes.ffi_type_uint32));
        }

        static class FFITypes {
            private static Pointer ffi_type_void;
            private static Pointer ffi_type_float;
            private static Pointer ffi_type_double;
            private static Pointer ffi_type_longdouble;
            private static Pointer ffi_type_uint8;
            private static Pointer ffi_type_sint8;
            private static Pointer ffi_type_uint16;
            private static Pointer ffi_type_sint16;
            private static Pointer ffi_type_uint32;
            private static Pointer ffi_type_sint32;
            private static Pointer ffi_type_uint64;
            private static Pointer ffi_type_sint64;
            private static Pointer ffi_type_pointer;

            private FFITypes() {
            }
        }

        public static class size_t
        extends IntegerType {
            private static final long serialVersionUID = 1L;

            public size_t() {
                this(0L);
            }

            public size_t(long l2) {
                super(Native.SIZE_T_SIZE, l2);
            }
        }
    }

    protected static class StructField {
        public String name;
        public Class<?> type;
        public Field field;
        public int size = -1;
        public int offset = -1;
        public boolean isVolatile;
        public boolean isReadOnly;
        public FromNativeConverter readConverter;
        public ToNativeConverter writeConverter;
        public FromNativeContext context;

        protected StructField() {
        }

        public String toString() {
            return this.name + "@" + this.offset + "[" + this.size + "] (" + this.type + ")";
        }
    }

    static class LayoutInfo {
        private int size = -1;
        private int alignment = 1;
        private final Map<String, StructField> fields = Collections.synchronizedMap(new LinkedHashMap());
        private int alignType = 0;
        private TypeMapper typeMapper;
        private boolean variable;

        private LayoutInfo() {
        }
    }

    @Documented
    @Retention(value=RetentionPolicy.RUNTIME)
    @Target(value={ElementType.TYPE})
    public static @interface FieldOrder {
        public String[] value();
    }

    static class StructureSet
    extends AbstractCollection<Structure>
    implements Set<Structure> {
        Structure[] elements;
        private int count;

        StructureSet() {
        }

        private void ensureCapacity(int n2) {
            if (this.elements == null) {
                this.elements = new Structure[n2 * 3 / 2];
                return;
            }
            if (this.elements.length < n2) {
                Structure[] structureArray = new Structure[n2 * 3 / 2];
                System.arraycopy(this.elements, 0, structureArray, 0, this.elements.length);
                this.elements = structureArray;
            }
        }

        public Structure[] getElements() {
            return this.elements;
        }

        @Override
        public int size() {
            return this.count;
        }

        @Override
        public boolean contains(Object object) {
            return this.indexOf((Structure)object) != -1;
        }

        @Override
        public boolean add(Structure structure) {
            if (!this.contains(structure)) {
                StructureSet structureSet = this;
                structureSet.ensureCapacity(structureSet.count + 1);
                this.elements[this.count++] = structure;
                return true;
            }
            return false;
        }

        private int indexOf(Structure structure) {
            for (int i2 = 0; i2 < this.count; ++i2) {
                Structure structure2 = this.elements[i2];
                if (structure != structure2 && (structure.getClass() != structure2.getClass() || structure.size() != structure2.size() || !structure.getPointer().equals(structure2.getPointer()))) continue;
                return i2;
            }
            return -1;
        }

        @Override
        public boolean remove(Object object) {
            int n2 = this.indexOf((Structure)object);
            if (n2 != -1) {
                if (--this.count >= 0) {
                    this.elements[n2] = this.elements[this.count];
                    this.elements[this.count] = null;
                }
                return true;
            }
            return false;
        }

        @Override
        public Iterator<Structure> iterator() {
            Structure[] structureArray = new Structure[this.count];
            if (this.count > 0) {
                System.arraycopy(this.elements, 0, structureArray, 0, this.count);
            }
            return Arrays.asList(structureArray).iterator();
        }
    }

    static class NativeStringTracking {
        private final Object value;
        private NativeString peer;

        NativeStringTracking(Object object) {
            this.value = object;
        }
    }

    public static interface ByReference {
    }

    public static interface ByValue {
    }
}


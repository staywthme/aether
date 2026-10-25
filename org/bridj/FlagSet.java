/*
 * Decompiled with CFR 0.152.
 */
package org.bridj;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.WeakHashMap;
import org.bridj.IntValuedEnum;
import org.bridj.ValuedEnum;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class FlagSet<E extends Enum<E>>
implements ValuedEnum<E> {
    private final long value;
    private final Class<E> enumClass;
    private E[] enumClassValues;
    private static Map<Class<?>, Object[]> enumsCache = new WeakHashMap();

    protected FlagSet(long l2, Class<E> clazz, E[] EArray) {
        this.enumClass = clazz;
        this.value = l2;
        this.enumClassValues = EArray;
    }

    private static synchronized <EE extends Enum<EE>> EE[] getValues(Class<EE> clazz) {
        Object object = (Enum[])enumsCache.get(clazz);
        if (object == null) {
            try {
                object = clazz.getMethod("values", new Class[0]);
                Class<?> clazz2 = ((Method)object).getReturnType();
                if (!clazz2.isArray() || !ValuedEnum.class.isAssignableFrom(clazz2.getComponentType())) {
                    throw new RuntimeException();
                }
                object = (Enum[])((Method)object).invoke(null, new Object[0]);
                enumsCache.put(clazz, (Object[])object);
            }
            catch (Exception exception) {
                throw new IllegalArgumentException("Class " + clazz + " does not have a public static " + ValuedEnum.class.getName() + "[] values() method.", exception);
            }
        }
        return object;
    }

    public boolean equals(Object object) {
        if (!(object instanceof ValuedEnum)) {
            return false;
        }
        return this.value() == ((ValuedEnum)object).value();
    }

    public int hashCode() {
        return Long.valueOf(this.value()).hashCode();
    }

    @Override
    public Iterator<E> iterator() {
        return this.getMatchingEnums().iterator();
    }

    public E toEnum() {
        Enum enum_ = null;
        Enum enum_2 = null;
        for (Enum enum_3 : this.getMatchingEnums()) {
            if (((ValuedEnum)((Object)enum_3)).value() == 0L) {
                enum_ = enum_3;
                continue;
            }
            if (enum_2 == null) {
                enum_2 = enum_3;
                continue;
            }
            throw new NoSuchElementException("More than one enum value corresponding to " + this + " : " + enum_3 + " and " + enum_2 + "...");
        }
        if (enum_2 != null) {
            return (E)enum_2;
        }
        if (this.value() == 0L) {
            return (E)enum_;
        }
        throw new NoSuchElementException("No enum value corresponding to " + this);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.enumClass.getSimpleName()).append("(").append(this.value()).append(" = ");
        try {
            boolean bl = true;
            for (Enum enum_ : this.getMatchingEnums()) {
                if (bl) {
                    bl = false;
                } else {
                    stringBuilder.append(" | ");
                }
                stringBuilder.append(enum_);
            }
        }
        catch (Throwable throwable) {
            stringBuilder.append("?");
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public static <EE extends Enum<EE>> FlagSet<EE> createFlagSet(long l2, Class<EE> clazz) {
        return new FlagSet(l2, clazz, null);
    }

    public static <EE extends Enum<EE>> IntFlagSet<EE> createFlagSet(int n2, Class<EE> clazz) {
        return new IntFlagSet((long)n2, clazz, null);
    }

    public static <EE extends Enum<EE>> FlagSet<EE> fromValue(ValuedEnum<EE> valuedEnum) {
        if (valuedEnum instanceof Enum) {
            return FlagSet.createFlagSet((long)valuedEnum.value(), (Enum[])new Enum[]{(Enum)((Object)valuedEnum)});
        }
        return (FlagSet)valuedEnum;
    }

    public static <EE extends Enum<EE>> FlagSet<EE> createFlagSet(long l2, EE ... EEArray) {
        if (EEArray == null) {
            throw new IllegalArgumentException("Expected at least one enum value");
        }
        Class<?> clazz = EEArray[0].getClass();
        if (IntValuedEnum.class.isAssignableFrom(clazz)) {
            return new IntFlagSet(l2, clazz, EEArray);
        }
        return new FlagSet(l2, clazz, EEArray);
    }

    public static <EE extends Enum<EE>> IntValuedEnum<EE> fromValue(int n2, Class<EE> clazz) {
        Class<EE> clazz2 = clazz;
        return (IntValuedEnum)FlagSet.fromValue((long)n2, clazz2, (Enum[])((Enum[])clazz2.getEnumConstants()));
    }

    public static <EE extends Enum<EE>> IntValuedEnum<EE> fromValue(int n2, EE ... EEArray) {
        return (IntValuedEnum)FlagSet.fromValue((long)n2, EEArray);
    }

    public static <EE extends Enum<EE>> ValuedEnum<EE> fromValue(long l2, EE ... EEArray) {
        if (EEArray == null || EEArray.length == 0) {
            throw new IllegalArgumentException("Expected at least one enum value");
        }
        Class<?> clazz = EEArray[0].getClass();
        return FlagSet.fromValue((long)l2, clazz, EEArray);
    }

    protected static <EE extends Enum<EE>> ValuedEnum<EE> fromValue(long l2, Class<EE> clazz, EE ... object) {
        object = FlagSet.getMatchingEnums((long)l2, (Enum[])((Enum[])clazz.getEnumConstants()));
        if (object.size() == 1) {
            return (ValuedEnum)object.get(0);
        }
        if (IntValuedEnum.class.isAssignableFrom(clazz)) {
            return new IntFlagSet(l2, clazz, object.toArray((Enum[])Array.newInstance(clazz, object.size())));
        }
        return new FlagSet(l2, clazz, object.toArray((Enum[])Array.newInstance(clazz, object.size())));
    }

    public static List<Long> getBits(long l2) {
        ArrayList<Long> arrayList = new ArrayList<Long>();
        for (int i2 = 0; i2 < 64; ++i2) {
            long l3 = 1L << i2;
            if ((l2 & l3) == 0L) continue;
            arrayList.add(l3);
        }
        return arrayList;
    }

    @Override
    public long value() {
        return this.value;
    }

    public Class<E> getEnumClass() {
        return this.enumClass;
    }

    protected E[] getEnumClassValues() {
        if (this.enumClassValues == null) {
            this.enumClassValues = FlagSet.getValues(this.enumClass);
            return this.enumClassValues;
        }
        return this.enumClassValues;
    }

    public boolean is(E ... EArray) {
        return this.value() == FlagSet.orValue(EArray);
    }

    public boolean has(E ... EArray) {
        return (this.value() & FlagSet.orValue(EArray)) != 0L;
    }

    public FlagSet<E> or(E ... EArray) {
        return new FlagSet(this.value() | FlagSet.orValue(EArray), this.enumClass, null);
    }

    static <E extends Enum<E>> long orValue(E ... EArray) {
        long l2 = 0L;
        for (E e2 : EArray) {
            l2 |= ((ValuedEnum)e2).value();
        }
        return l2;
    }

    public FlagSet<E> without(E ... EArray) {
        return new FlagSet(this.value() & (FlagSet.orValue(EArray) ^ 0xFFFFFFFFFFFFFFFFL), this.enumClass, null);
    }

    public FlagSet<E> and(E ... EArray) {
        return new FlagSet(this.value() & FlagSet.orValue(EArray), this.enumClass, null);
    }

    protected List<E> getMatchingEnums() {
        if (this.enumClass == null) {
            return Collections.EMPTY_LIST;
        }
        return FlagSet.getMatchingEnums((long)this.value, (Enum[])this.getEnumClassValues());
    }

    protected static <EE extends Enum<EE>> List<EE> getMatchingEnums(long l2, EE[] EEArray) {
        ArrayList<EE> arrayList = new ArrayList<EE>();
        for (EE EE : EEArray) {
            long l3 = ((ValuedEnum)EE).value();
            if ((l3 != 0L || l2 != 0L) && (l3 == 0L || (l2 & l3) != l3)) continue;
            arrayList.add(EE);
        }
        return arrayList;
    }

    public static <E extends Enum<E>> FlagSet<E> fromValues(E ... EArray) {
        long l2 = 0L;
        Class<?> clazz = null;
        E[] EArray2 = EArray;
        int n2 = EArray.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            E e2 = EArray2[i2];
            if (e2 == null) continue;
            if (clazz == null) {
                clazz = e2.getClass();
            }
            l2 |= ((ValuedEnum)e2).value();
        }
        return new FlagSet(l2, clazz, EArray);
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static class IntFlagSet<E extends Enum<E>>
    extends FlagSet<E>
    implements IntValuedEnum<E> {
        protected IntFlagSet(long l2, Class<E> clazz, E[] EArray) {
            super(l2, clazz, EArray);
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package org.bridj;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import org.bridj.FlagSet;
import org.bridj.NativeObject;
import org.bridj.Pointer;
import org.bridj.PointerIO;
import org.bridj.TypedPointer;
import org.bridj.dyncall.DyncallLibrary;

interface CallIO {
    public Object newInstance(long var1);

    public long getDCStruct();

    public long getPeer(Object var1);

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static final class GenericPointerHandler
    implements CallIO {
        private final Type targetType;
        private final PointerIO pointerIO;

        public GenericPointerHandler(Type type) {
            this.targetType = type;
            this.pointerIO = PointerIO.getInstance(type);
        }

        @Override
        public final Pointer<?> newInstance(long l2) {
            return Pointer.pointerToAddress(l2, this.pointerIO);
        }

        @Override
        public final long getDCStruct() {
            return 0L;
        }

        @Override
        public final long getPeer(Object object) {
            return Pointer.getPeer((Pointer)object);
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static class NativeObjectHandler
    implements CallIO {
        final Class<? extends NativeObject> nativeClass;
        final Type nativeType;
        final Pointer<DyncallLibrary.DCstruct> pStruct;

        public NativeObjectHandler(Class<? extends NativeObject> clazz, Type type, Pointer<DyncallLibrary.DCstruct> pointer) {
            this.nativeClass = clazz;
            this.nativeType = type;
            this.pStruct = pointer;
        }

        @Override
        public NativeObject newInstance(long l2) {
            return Pointer.pointerToAddress(l2).getNativeObject(this.nativeClass);
        }

        @Override
        public long getDCStruct() {
            return Pointer.getPeer(this.pStruct);
        }

        @Override
        public long getPeer(Object object) {
            return Pointer.getAddress((NativeObject)object, this.nativeClass);
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static class TypedPointerIO
    implements CallIO {
        Class<? extends TypedPointer> type;
        Constructor<?> constructor;

        public TypedPointerIO(Class<? extends TypedPointer> clazz) {
            this.type = clazz;
            try {
                this.constructor = clazz.getConstructor(Long.TYPE);
                return;
            }
            catch (Exception exception) {
                throw new RuntimeException("Failed to create " + CallIO.class.getName() + " for type " + clazz.getName(), exception);
            }
        }

        @Override
        public Pointer<?> newInstance(long l2) {
            try {
                if (l2 == 0L) {
                    return null;
                }
                return (Pointer)this.constructor.newInstance(l2);
            }
            catch (Exception exception) {
                throw new RuntimeException("Failed to instantiate pointer of type " + this.type.getName(), exception);
            }
        }

        @Override
        public long getDCStruct() {
            return 0L;
        }

        @Override
        public long getPeer(Object object) {
            return Pointer.getPeer((Pointer)object);
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static class Utils {
        public static CallIO createPointerCallIOToTargetType(Type type) {
            return new GenericPointerHandler(type);
        }

        public static <EE extends Enum<EE>> CallIO createValuedEnumCallIO(final Class<EE> clazz) {
            return new CallIO(){

                public final Object newInstance(long l2) {
                    return FlagSet.fromValue((long)l2, (Class)clazz, (Enum[])new Enum[0]);
                }

                public final long getDCStruct() {
                    return 0L;
                }

                public final long getPeer(Object object) {
                    return 0L;
                }
            };
        }

        public static CallIO createPointerCallIO(Type type) {
            return Utils.createPointerCallIO(org.bridj.util.Utils.getClass(type), type);
        }

        public static CallIO createPointerCallIO(Class<?> clazz, Type type) {
            if (clazz == Pointer.class) {
                return Utils.createPointerCallIOToTargetType(org.bridj.util.Utils.getUniqueParameterizedTypeParameter(type));
            }
            assert (TypedPointer.class.isAssignableFrom(clazz));
            return new TypedPointerIO(clazz);
        }
    }
}


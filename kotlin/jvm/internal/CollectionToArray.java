/*
 * Decompiled with CFR 0.152.
 */
package kotlin.jvm.internal;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.jvm.JvmName;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=2, xi=48, d1={"\u00002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u001e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a#\u0010\u0006\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00012\n\u0010\u0007\u001a\u0006\u0012\u0002\b\u00030\bH\u0007\u00a2\u0006\u0004\b\t\u0010\n\u001a5\u0010\u0006\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00012\n\u0010\u0007\u001a\u0006\u0012\u0002\b\u00030\b2\u0010\u0010\u000b\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0002\u0018\u00010\u0001H\u0007\u00a2\u0006\u0004\b\t\u0010\f\u001a~\u0010\r\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00012\n\u0010\u0007\u001a\u0006\u0012\u0002\b\u00030\b2\u0014\u0010\u000e\u001a\u0010\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00010\u000f2\u001a\u0010\u0010\u001a\u0016\u0012\u0004\u0012\u00020\u0005\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00010\u00112(\u0010\u0012\u001a$\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0001\u0012\u0004\u0012\u00020\u0005\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00010\u0013H\u0082\b\u00a2\u0006\u0002\u0010\u0014\"\u0018\u0010\u0000\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0001X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0003\"\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2={"EMPTY", "", "", "[Ljava/lang/Object;", "MAX_SIZE", "", "collectionToArray", "collection", "", "toArray", "(Ljava/util/Collection;)[Ljava/lang/Object;", "a", "(Ljava/util/Collection;[Ljava/lang/Object;)[Ljava/lang/Object;", "toArrayImpl", "empty", "Lkotlin/Function0;", "alloc", "Lkotlin/Function1;", "trim", "Lkotlin/Function2;", "(Ljava/util/Collection;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function2;)[Ljava/lang/Object;", "kotlin-stdlib"})
@JvmName(name="CollectionToArray")
@SourceDebugExtension(value={"SMAP\nCollectionToArray.kt\nKotlin\n*S Kotlin\n*F\n+ 1 CollectionToArray.kt\nkotlin/jvm/internal/CollectionToArray\n+ 2 ArrayIntrinsics.kt\nkotlin/ArrayIntrinsicsKt\n*L\n1#1,82:1\n57#1,22:83\n57#1,22:105\n26#2:127\n*S KotlinDebug\n*F\n+ 1 CollectionToArray.kt\nkotlin/jvm/internal/CollectionToArray\n*L\n19#1:83,22\n31#1:105,22\n14#1:127\n*E\n"})
public final class CollectionToArray {
    @NotNull
    private static final Object[] EMPTY = new Object[0];
    private static final int MAX_SIZE = 0x7FFFFFFD;

    @JvmName(name="toArray")
    @NotNull
    public static final Object[] toArray(@NotNull Collection<?> object) {
        Intrinsics.checkNotNullParameter(object, "");
        int n2 = object.size();
        if (n2 == 0) {
            return EMPTY;
        }
        if (!(object = object.iterator()).hasNext()) {
            return EMPTY;
        }
        Object[] objectArray = new Object[n2];
        int n3 = 0;
        while (true) {
            objectArray[n3++] = object.next();
            if (n3 >= objectArray.length) {
                if (!object.hasNext()) {
                    return objectArray;
                }
                int n4 = n3 * 3 + 1 >>> 1;
                if (n4 <= n3) {
                    if (n3 >= 0x7FFFFFFD) {
                        throw new OutOfMemoryError();
                    }
                    n4 = 0x7FFFFFFD;
                }
                Intrinsics.checkNotNullExpressionValue(Arrays.copyOf(objectArray, n4), "");
                continue;
            }
            if (!object.hasNext()) break;
        }
        int n5 = n3;
        Object[] objectArray2 = Arrays.copyOf(objectArray, n5);
        Intrinsics.checkNotNullExpressionValue(objectArray2, "");
        return objectArray2;
    }

    @JvmName(name="toArray")
    @NotNull
    public static final Object[] toArray(@NotNull Collection<?> collection, @Nullable Object[] objectArray) {
        Object[] objectArray2;
        Intrinsics.checkNotNullParameter(collection, "");
        if (objectArray == null) {
            throw new NullPointerException();
        }
        int n2 = collection.size();
        if (n2 == 0) {
            if (objectArray.length > 0) {
                objectArray[0] = null;
            }
            return objectArray;
        }
        Iterator<?> iterator2 = collection.iterator();
        if (!iterator2.hasNext()) {
            if (objectArray.length > 0) {
                objectArray[0] = null;
            }
            return objectArray;
        }
        if (n2 <= objectArray.length) {
            objectArray2 = objectArray;
        } else {
            Object object = Array.newInstance(objectArray.getClass().getComponentType(), n2);
            Intrinsics.checkNotNull(object);
            objectArray2 = (Object[])object;
        }
        Object[] objectArray3 = objectArray2;
        int n3 = 0;
        while (true) {
            objectArray3[n3++] = iterator2.next();
            if (n3 >= objectArray3.length) {
                if (!iterator2.hasNext()) {
                    return objectArray3;
                }
                n2 = n3 * 3 + 1 >>> 1;
                if (n2 <= n3) {
                    if (n3 >= 0x7FFFFFFD) {
                        throw new OutOfMemoryError();
                    }
                    n2 = 0x7FFFFFFD;
                }
                Intrinsics.checkNotNullExpressionValue(Arrays.copyOf(objectArray3, n2), "");
                continue;
            }
            if (!iterator2.hasNext()) break;
        }
        Object[] objectArray4 = objectArray3;
        if (objectArray3 == objectArray) {
            objectArray[n3] = null;
            return objectArray;
        }
        Object[] objectArray5 = Arrays.copyOf(objectArray4, n3);
        Intrinsics.checkNotNullExpressionValue(objectArray5, "");
        return objectArray5;
    }

    private static final Object[] toArrayImpl(Collection<?> object, Function0<Object[]> objectArray, Function1<? super Integer, Object[]> function1, Function2<? super Object[], ? super Integer, Object[]> function2) {
        int n2 = object.size();
        if (n2 == 0) {
            return objectArray.invoke();
        }
        if (!(object = object.iterator()).hasNext()) {
            return objectArray.invoke();
        }
        objectArray = function1.invoke((Integer)n2);
        int n3 = 0;
        while (true) {
            objectArray[n3++] = object.next();
            if (n3 >= objectArray.length) {
                if (!object.hasNext()) {
                    return objectArray;
                }
                n2 = n3 * 3 + 1 >>> 1;
                if (n2 <= n3) {
                    if (n3 >= 0x7FFFFFFD) {
                        throw new OutOfMemoryError();
                    }
                    n2 = 0x7FFFFFFD;
                }
                Intrinsics.checkNotNullExpressionValue(Arrays.copyOf(objectArray, n2), "");
                continue;
            }
            if (!object.hasNext()) break;
        }
        return function2.invoke((Object[])objectArray, (Integer)n3);
    }
}


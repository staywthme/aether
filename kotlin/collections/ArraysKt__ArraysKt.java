/*
 * Decompiled with CFR 0.152.
 */
package kotlin.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.PublishedApi;
import kotlin.SinceKotlin;
import kotlin.TuplesKt;
import kotlin.UByteArray;
import kotlin.UIntArray;
import kotlin.ULongArray;
import kotlin.UShortArray;
import kotlin.collections.ArraysKt;
import kotlin.collections.ArraysKt__ArraysJVMKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.unsigned.UArraysKt;
import kotlin.internal.InlineOnly;
import kotlin.jvm.JvmName;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.ranges.RangesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=5, xi=49, d1={"\u0000H\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a5\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\f\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0018\u00010\u00032\u0010\u0010\u0004\u001a\f\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0018\u00010\u0003H\u0001\u00a2\u0006\u0004\b\u0005\u0010\u0006\u001a#\u0010\u0007\u001a\u00020\b\"\u0004\b\u0000\u0010\u0002*\f\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0018\u00010\u0003H\u0001\u00a2\u0006\u0004\b\t\u0010\n\u001a?\u0010\u000b\u001a\u00020\f\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\n\u0010\r\u001a\u00060\u000ej\u0002`\u000f2\u0010\u0010\u0010\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u00030\u0011H\u0002\u00a2\u0006\u0004\b\u0012\u0010\u0013\u001a+\u0010\u0014\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0015\"\u0004\b\u0000\u0010\u0002*\u0012\u0012\u000e\b\u0001\u0012\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00030\u0003\u00a2\u0006\u0002\u0010\u0016\u001a;\u0010\u0017\u001a\u0002H\u0018\"\u0010\b\u0000\u0010\u0019*\u0006\u0012\u0002\b\u00030\u0003*\u0002H\u0018\"\u0004\b\u0001\u0010\u0018*\u0002H\u00192\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u0002H\u00180\u001bH\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001c\u001a)\u0010\u001d\u001a\u00020\u0001*\b\u0012\u0002\b\u0003\u0018\u00010\u0003H\u0087\b\u0082\u0002\u000e\n\f\b\u0000\u0012\u0002\u0018\u0001\u001a\u0004\b\u0003\u0010\u0000\u00a2\u0006\u0002\u0010\u001e\u001aG\u0010\u001f\u001a\u001a\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u0015\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00180\u00150 \"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0018*\u0016\u0012\u0012\b\u0001\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00180 0\u0003\u00a2\u0006\u0002\u0010!\u0082\u0002\u0007\n\u0005\b\u009920\u0001\u00a8\u0006\""}, d2={"contentDeepEqualsImpl", "", "T", "", "other", "contentDeepEquals", "([Ljava/lang/Object;[Ljava/lang/Object;)Z", "contentDeepToStringImpl", "", "contentDeepToString", "([Ljava/lang/Object;)Ljava/lang/String;", "contentDeepToStringInternal", "", "result", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "processed", "", "contentDeepToStringInternal$ArraysKt__ArraysKt", "([Ljava/lang/Object;Ljava/lang/StringBuilder;Ljava/util/List;)V", "flatten", "", "([[Ljava/lang/Object;)Ljava/util/List;", "ifEmpty", "R", "C", "defaultValue", "Lkotlin/Function0;", "([Ljava/lang/Object;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "isNullOrEmpty", "([Ljava/lang/Object;)Z", "unzip", "Lkotlin/Pair;", "([Lkotlin/Pair;)Lkotlin/Pair;", "kotlin-stdlib"}, xs="kotlin/collections/ArraysKt")
@SourceDebugExtension(value={"SMAP\nArrays.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Arrays.kt\nkotlin/collections/ArraysKt__ArraysKt\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,161:1\n1#2:162\n*E\n"})
class ArraysKt__ArraysKt
extends ArraysKt__ArraysJVMKt {
    @NotNull
    public static final <T> List<T> flatten(@NotNull T[][] TArray) {
        Intrinsics.checkNotNullParameter(TArray, "");
        Object[] objectArray = (Object[])TArray;
        int n2 = 0;
        for (Object object : objectArray) {
            Object[] objectArray2 = (Object[])object;
            int n3 = n2;
            n2 = objectArray2.length;
            n2 = n3 + n2;
        }
        int n4 = n2;
        ArrayList arrayList = new ArrayList(n4);
        n2 = ((Object[])TArray).length;
        for (int i2 = 0; i2 < n2; ++i2) {
            T[] TArray2 = TArray[i2];
            CollectionsKt.addAll((Collection)arrayList, TArray2);
        }
        return arrayList;
    }

    @NotNull
    public static final <T, R> Pair<List<T>, List<R>> unzip(@NotNull Pair<? extends T, ? extends R>[] pairArray) {
        Intrinsics.checkNotNullParameter(pairArray, "");
        ArrayList<T> arrayList = new ArrayList<T>(pairArray.length);
        ArrayList<R> arrayList2 = new ArrayList<R>(pairArray.length);
        for (Pair<T, R> pair : pairArray) {
            arrayList.add(pair.getFirst());
            arrayList2.add(pair.getSecond());
        }
        return TuplesKt.to(arrayList, arrayList2);
    }

    @SinceKotlin(version="1.3")
    @InlineOnly
    private static final boolean isNullOrEmpty(Object[] objectArray) {
        return objectArray == null || objectArray.length == 0;
    }

    @SinceKotlin(version="1.3")
    @InlineOnly
    private static final <C extends Object[], R> R ifEmpty(C c2, Function0<? extends R> function0) {
        Intrinsics.checkNotNullParameter(function0, "");
        if (((C)c2).length == 0) {
            return function0.invoke();
        }
        return (R)c2;
    }

    @SinceKotlin(version="1.3")
    @PublishedApi
    @JvmName(name="contentDeepEquals")
    public static final <T> boolean contentDeepEquals(@Nullable T[] TArray, @Nullable T[] TArray2) {
        if (TArray == TArray2) {
            return true;
        }
        if (TArray == null || TArray2 == null || TArray.length != TArray2.length) {
            return false;
        }
        int n2 = TArray.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            Object object = TArray[i2];
            Object object2 = TArray2[i2];
            if (object == object2) continue;
            if (object == null || object2 == null) {
                return false;
            }
            if (!(object instanceof Object[] && object2 instanceof Object[] ? !ArraysKt.contentDeepEquals(object = (Object[])object, object2 = (Object[])object2) : (object instanceof byte[] && object2 instanceof byte[] ? !Arrays.equals((byte[])object, (byte[])object2) : (object instanceof short[] && object2 instanceof short[] ? !Arrays.equals((short[])object, (short[])object2) : (object instanceof int[] && object2 instanceof int[] ? !Arrays.equals((int[])object, (int[])object2) : (object instanceof long[] && object2 instanceof long[] ? !Arrays.equals((long[])object, (long[])object2) : (object instanceof float[] && object2 instanceof float[] ? !Arrays.equals((float[])object, (float[])object2) : (object instanceof double[] && object2 instanceof double[] ? !Arrays.equals((double[])object, (double[])object2) : (object instanceof char[] && object2 instanceof char[] ? !Arrays.equals((char[])object, (char[])object2) : (object instanceof boolean[] && object2 instanceof boolean[] ? !Arrays.equals((boolean[])object, (boolean[])object2) : (object instanceof UByteArray && object2 instanceof UByteArray ? !UArraysKt.contentEquals-kV0jMPg(((UByteArray)object).unbox-impl(), ((UByteArray)object2).unbox-impl()) : (object instanceof UShortArray && object2 instanceof UShortArray ? !UArraysKt.contentEquals-FGO6Aew(((UShortArray)object).unbox-impl(), ((UShortArray)object2).unbox-impl()) : (object instanceof UIntArray && object2 instanceof UIntArray ? !UArraysKt.contentEquals-KJPZfPQ(((UIntArray)object).unbox-impl(), ((UIntArray)object2).unbox-impl()) : (object instanceof ULongArray && object2 instanceof ULongArray ? !UArraysKt.contentEquals-lec5QzE(((ULongArray)object).unbox-impl(), ((ULongArray)object2).unbox-impl()) : !Intrinsics.areEqual(object, object2))))))))))))))) continue;
            return false;
        }
        return true;
    }

    @SinceKotlin(version="1.3")
    @PublishedApi
    @JvmName(name="contentDeepToString")
    @NotNull
    public static final <T> String contentDeepToString(@Nullable T[] TArray) {
        StringBuilder stringBuilder;
        if (TArray == null) {
            return "null";
        }
        int n2 = RangesKt.coerceAtMost(TArray.length, 0x19999999) * 5 + 2;
        StringBuilder stringBuilder2 = stringBuilder = new StringBuilder(n2);
        ArraysKt__ArraysKt.contentDeepToStringInternal$ArraysKt__ArraysKt(TArray, stringBuilder2, new ArrayList());
        String string = stringBuilder.toString();
        Intrinsics.checkNotNullExpressionValue(string, "");
        return string;
    }

    private static final <T> void contentDeepToStringInternal$ArraysKt__ArraysKt(T[] TArray, StringBuilder stringBuilder, List<Object[]> list2) {
        if (list2.contains(TArray)) {
            stringBuilder.append("[...]");
            return;
        }
        list2.add(TArray);
        stringBuilder.append('[');
        int n2 = TArray.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            T t2;
            if (i2 != 0) {
                stringBuilder.append(", ");
            }
            T t3 = t2 = TArray[i2];
            if (t2 == null) {
                stringBuilder.append("null");
                continue;
            }
            if (t3 instanceof Object[]) {
                ArraysKt__ArraysKt.contentDeepToStringInternal$ArraysKt__ArraysKt((Object[])t2, stringBuilder, list2);
                continue;
            }
            if (t3 instanceof byte[]) {
                String string = Arrays.toString((byte[])t2);
                Intrinsics.checkNotNullExpressionValue(string, "");
                stringBuilder.append(string);
                continue;
            }
            if (t3 instanceof short[]) {
                String string = Arrays.toString((short[])t2);
                Intrinsics.checkNotNullExpressionValue(string, "");
                stringBuilder.append(string);
                continue;
            }
            if (t3 instanceof int[]) {
                String string = Arrays.toString((int[])t2);
                Intrinsics.checkNotNullExpressionValue(string, "");
                stringBuilder.append(string);
                continue;
            }
            if (t3 instanceof long[]) {
                String string = Arrays.toString((long[])t2);
                Intrinsics.checkNotNullExpressionValue(string, "");
                stringBuilder.append(string);
                continue;
            }
            if (t3 instanceof float[]) {
                String string = Arrays.toString((float[])t2);
                Intrinsics.checkNotNullExpressionValue(string, "");
                stringBuilder.append(string);
                continue;
            }
            if (t3 instanceof double[]) {
                String string = Arrays.toString((double[])t2);
                Intrinsics.checkNotNullExpressionValue(string, "");
                stringBuilder.append(string);
                continue;
            }
            if (t3 instanceof char[]) {
                String string = Arrays.toString((char[])t2);
                Intrinsics.checkNotNullExpressionValue(string, "");
                stringBuilder.append(string);
                continue;
            }
            if (t3 instanceof boolean[]) {
                String string = Arrays.toString((boolean[])t2);
                Intrinsics.checkNotNullExpressionValue(string, "");
                stringBuilder.append(string);
                continue;
            }
            if (t3 instanceof UByteArray) {
                UByteArray uByteArray = (UByteArray)t2;
                stringBuilder.append(UArraysKt.contentToString-2csIQuQ((byte[])(uByteArray != null ? uByteArray.unbox-impl() : null)));
                continue;
            }
            if (t3 instanceof UShortArray) {
                UShortArray uShortArray = (UShortArray)t2;
                stringBuilder.append(UArraysKt.contentToString-d-6D3K8((short[])(uShortArray != null ? uShortArray.unbox-impl() : null)));
                continue;
            }
            if (t3 instanceof UIntArray) {
                UIntArray uIntArray = (UIntArray)t2;
                stringBuilder.append(UArraysKt.contentToString-XUkPCBk((int[])(uIntArray != null ? uIntArray.unbox-impl() : null)));
                continue;
            }
            if (t3 instanceof ULongArray) {
                ULongArray uLongArray = (ULongArray)t2;
                stringBuilder.append(UArraysKt.contentToString-uLth9ew((long[])(uLongArray != null ? uLongArray.unbox-impl() : null)));
                continue;
            }
            stringBuilder.append(t2.toString());
        }
        stringBuilder.append(']');
        List<Object[]> list3 = list2;
        list3.remove(CollectionsKt.getLastIndex(list3));
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package kotlin.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import kotlin.Metadata;
import kotlin.PublishedApi;
import kotlin.SinceKotlin;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.builders.ListBuilder;
import kotlin.internal.InlineOnly;
import kotlin.internal.PlatformImplementationsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.CollectionToArray;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 8, 0}, k=5, xi=49, d1={"\u0000T\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010!\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u001e\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u001c\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\"\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0004H\u0001\u001a?\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0006\u001a\u00020\u00072\u001d\u0010\b\u001a\u0019\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u0004\u0012\u0004\u0012\u00020\n0\t\u00a2\u0006\u0002\b\u000bH\u0081\b\u00f8\u0001\u0000\u001a7\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u001d\u0010\b\u001a\u0019\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u0004\u0012\u0004\u0012\u00020\n0\t\u00a2\u0006\u0002\b\u000bH\u0081\b\u00f8\u0001\u0000\u001a\u0011\u0010\f\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\u0007H\u0081\b\u001a\u0011\u0010\u000e\u001a\u00020\u00072\u0006\u0010\u000f\u001a\u00020\u0007H\u0081\b\u001a\"\u0010\u0010\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00120\u00112\n\u0010\u0013\u001a\u0006\u0012\u0002\b\u00030\u0014H\u0081\b\u00a2\u0006\u0002\u0010\u0015\u001a4\u0010\u0010\u001a\b\u0012\u0004\u0012\u0002H\u00160\u0011\"\u0004\b\u0000\u0010\u00162\n\u0010\u0013\u001a\u0006\u0012\u0002\b\u00030\u00142\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u0002H\u00160\u0011H\u0081\b\u00a2\u0006\u0002\u0010\u0018\u001a\u0014\u0010\u0019\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0004\"\u0004\b\u0000\u0010\u0002H\u0001\u001a\u001c\u0010\u0019\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0004\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0006\u001a\u00020\u0007H\u0001\u001a\u001f\u0010\u001a\u001a\b\u0012\u0004\u0012\u0002H\u00160\u0001\"\u0004\b\u0000\u0010\u00162\u0006\u0010\u001b\u001a\u0002H\u0016\u00a2\u0006\u0002\u0010\u001c\u001a1\u0010\u001d\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010\u00120\u0011\"\u0004\b\u0000\u0010\u0016*\n\u0012\u0006\b\u0001\u0012\u0002H\u00160\u00112\u0006\u0010\u001e\u001a\u00020\u001fH\u0000\u00a2\u0006\u0002\u0010 \u001a\u001e\u0010!\u001a\b\u0012\u0004\u0012\u0002H\u00160\u0001\"\u0004\b\u0000\u0010\u0016*\b\u0012\u0004\u0012\u0002H\u00160\"H\u0007\u001a&\u0010!\u001a\b\u0012\u0004\u0012\u0002H\u00160\u0001\"\u0004\b\u0000\u0010\u0016*\b\u0012\u0004\u0012\u0002H\u00160\"2\u0006\u0010#\u001a\u00020$H\u0007\u001a\u001f\u0010%\u001a\b\u0012\u0004\u0012\u0002H\u00160\u0001\"\u0004\b\u0000\u0010\u0016*\b\u0012\u0004\u0012\u0002H\u00160&H\u0087\b\u0082\u0002\u0007\n\u0005\b\u009920\u0001\u00a8\u0006'"}, d2={"build", "", "E", "builder", "", "buildListInternal", "capacity", "", "builderAction", "Lkotlin/Function1;", "", "Lkotlin/ExtensionFunctionType;", "checkCountOverflow", "count", "checkIndexOverflow", "index", "copyToArrayImpl", "", "", "collection", "", "(Ljava/util/Collection;)[Ljava/lang/Object;", "T", "array", "(Ljava/util/Collection;[Ljava/lang/Object;)[Ljava/lang/Object;", "createListBuilder", "listOf", "element", "(Ljava/lang/Object;)Ljava/util/List;", "copyToArrayOfAny", "isVarargs", "", "([Ljava/lang/Object;Z)[Ljava/lang/Object;", "shuffled", "", "random", "Ljava/util/Random;", "toList", "Ljava/util/Enumeration;", "kotlin-stdlib"}, xs="kotlin/collections/CollectionsKt")
@SourceDebugExtension(value={"SMAP\nCollectionsJVM.kt\nKotlin\n*S Kotlin\n*F\n+ 1 CollectionsJVM.kt\nkotlin/collections/CollectionsKt__CollectionsJVMKt\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,120:1\n1#2:121\n*E\n"})
public class CollectionsKt__CollectionsJVMKt {
    @NotNull
    public static final <T> List<T> listOf(T t2) {
        List<T> list2 = Collections.singletonList(t2);
        Intrinsics.checkNotNullExpressionValue(list2, "");
        return list2;
    }

    @PublishedApi
    @SinceKotlin(version="1.3")
    @InlineOnly
    private static final <E> List<E> buildListInternal(Function1<? super List<E>, Unit> function1) {
        Intrinsics.checkNotNullParameter(function1, "");
        List<E> list2 = CollectionsKt.createListBuilder();
        function1.invoke(list2);
        return CollectionsKt.build(list2);
    }

    @PublishedApi
    @SinceKotlin(version="1.3")
    @InlineOnly
    private static final <E> List<E> buildListInternal(int n2, Function1<? super List<E>, Unit> function1) {
        Intrinsics.checkNotNullParameter(function1, "");
        List<E> list2 = CollectionsKt.createListBuilder(n2);
        function1.invoke(list2);
        return CollectionsKt.build(list2);
    }

    @PublishedApi
    @SinceKotlin(version="1.3")
    @NotNull
    public static final <E> List<E> createListBuilder() {
        return new ListBuilder();
    }

    @PublishedApi
    @SinceKotlin(version="1.3")
    @NotNull
    public static final <E> List<E> createListBuilder(int n2) {
        return new ListBuilder(n2);
    }

    @PublishedApi
    @SinceKotlin(version="1.3")
    @NotNull
    public static final <E> List<E> build(@NotNull List<E> list2) {
        Intrinsics.checkNotNullParameter(list2, "");
        return ((ListBuilder)list2).build();
    }

    @InlineOnly
    private static final <T> List<T> toList(Enumeration<T> enumeration) {
        Intrinsics.checkNotNullParameter(enumeration, "");
        ArrayList<T> arrayList = Collections.list(enumeration);
        Intrinsics.checkNotNullExpressionValue(arrayList, "");
        return arrayList;
    }

    @SinceKotlin(version="1.2")
    @NotNull
    public static final <T> List<T> shuffled(@NotNull Iterable<? extends T> iterable) {
        Intrinsics.checkNotNullParameter(iterable, "");
        iterable = CollectionsKt.toMutableList(iterable);
        List<T> list2 = iterable;
        Collections.shuffle(iterable);
        return iterable;
    }

    @SinceKotlin(version="1.2")
    @NotNull
    public static final <T> List<T> shuffled(@NotNull Iterable<? extends T> iterable, @NotNull Random random) {
        Intrinsics.checkNotNullParameter(iterable, "");
        Intrinsics.checkNotNullParameter(random, "");
        iterable = CollectionsKt.toMutableList(iterable);
        List<T> list2 = iterable;
        Collections.shuffle(iterable, random);
        return iterable;
    }

    @InlineOnly
    private static final Object[] copyToArrayImpl(Collection<?> collection) {
        Intrinsics.checkNotNullParameter(collection, "");
        return CollectionToArray.toArray(collection);
    }

    @InlineOnly
    private static final <T> T[] copyToArrayImpl(Collection<?> collection, T[] TArray) {
        Intrinsics.checkNotNullParameter(collection, "");
        Intrinsics.checkNotNullParameter(TArray, "");
        return CollectionToArray.toArray(collection, TArray);
    }

    @NotNull
    public static final <T> Object[] copyToArrayOfAny(@NotNull T[] TArray, boolean bl) {
        Intrinsics.checkNotNullParameter(TArray, "");
        if (bl && Intrinsics.areEqual(TArray.getClass(), Object[].class)) {
            return TArray;
        }
        Object[] objectArray = Arrays.copyOf(TArray, TArray.length, Object[].class);
        Intrinsics.checkNotNullExpressionValue(objectArray, "");
        return objectArray;
    }

    @PublishedApi
    @SinceKotlin(version="1.3")
    @InlineOnly
    private static final int checkIndexOverflow(int n2) {
        if (n2 < 0) {
            if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 3, 0)) {
                CollectionsKt.throwIndexOverflow();
            } else {
                throw new ArithmeticException("Index overflow has happened.");
            }
        }
        return n2;
    }

    @PublishedApi
    @SinceKotlin(version="1.3")
    @InlineOnly
    private static final int checkCountOverflow(int n2) {
        if (n2 < 0) {
            if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 3, 0)) {
                CollectionsKt.throwCountOverflow();
            } else {
                throw new ArithmeticException("Count overflow has happened.");
            }
        }
        return n2;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package kotlin.collections.builders;

import java.util.Arrays;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 8, 0}, k=2, xi=48, d1={"\u00002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0002\u001a!\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u0000\u00a2\u0006\u0002\u0010\u0005\u001a+\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0001\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070\u00012\u0006\u0010\b\u001a\u00020\u0004H\u0000\u00a2\u0006\u0002\u0010\t\u001a%\u0010\n\u001a\u00020\u000b\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\f\u001a\u00020\u0004H\u0000\u00a2\u0006\u0002\u0010\r\u001a-\u0010\u000e\u001a\u00020\u000b\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u000f\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u00020\u0004H\u0000\u00a2\u0006\u0002\u0010\u0011\u001a9\u0010\u0012\u001a\u00020\u0013\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070\u00012\u0006\u0010\u0014\u001a\u00020\u00042\u0006\u0010\u0015\u001a\u00020\u00042\n\u0010\u0016\u001a\u0006\u0012\u0002\b\u00030\u0017H\u0002\u00a2\u0006\u0002\u0010\u0018\u001a-\u0010\u0019\u001a\u00020\u0004\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070\u00012\u0006\u0010\u0014\u001a\u00020\u00042\u0006\u0010\u0015\u001a\u00020\u0004H\u0002\u00a2\u0006\u0002\u0010\u001a\u001a/\u0010\u001b\u001a\u00020\u001c\"\u0004\b\u0000\u0010\u0007*\n\u0012\u0006\b\u0001\u0012\u0002H\u00070\u00012\u0006\u0010\u0014\u001a\u00020\u00042\u0006\u0010\u0015\u001a\u00020\u0004H\u0002\u00a2\u0006\u0002\u0010\u001d\u00a8\u0006\u001e"}, d2={"arrayOfUninitializedElements", "", "E", "size", "", "(I)[Ljava/lang/Object;", "copyOfUninitializedElements", "T", "newSize", "([Ljava/lang/Object;I)[Ljava/lang/Object;", "resetAt", "", "index", "([Ljava/lang/Object;I)V", "resetRange", "fromIndex", "toIndex", "([Ljava/lang/Object;II)V", "subarrayContentEquals", "", "offset", "length", "other", "", "([Ljava/lang/Object;IILjava/util/List;)Z", "subarrayContentHashCode", "([Ljava/lang/Object;II)I", "subarrayContentToString", "", "([Ljava/lang/Object;II)Ljava/lang/String;", "kotlin-stdlib"})
@SourceDebugExtension(value={"SMAP\nListBuilder.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ListBuilder.kt\nkotlin/collections/builders/ListBuilderKt\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,429:1\n1#2:430\n*E\n"})
public final class ListBuilderKt {
    @NotNull
    public static final <E> E[] arrayOfUninitializedElements(int n2) {
        boolean bl = n2 >= 0;
        if (!bl) {
            String string = "capacity must be non-negative.";
            throw new IllegalArgumentException(string.toString());
        }
        return new Object[n2];
    }

    private static final <T> String subarrayContentToString(T[] TArray, int n2, int n3) {
        StringBuilder stringBuilder = new StringBuilder(2 + n3 * 3);
        stringBuilder.append("[");
        for (int i2 = 0; i2 < n3; ++i2) {
            if (i2 > 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(TArray[n2 + i2]);
        }
        stringBuilder.append("]");
        String string = stringBuilder.toString();
        Intrinsics.checkNotNullExpressionValue(string, "");
        return string;
    }

    private static final <T> int subarrayContentHashCode(T[] TArray, int n2, int n3) {
        int n4 = 1;
        for (int i2 = 0; i2 < n3; ++i2) {
            T t2;
            T t3 = t2 = TArray[n2 + i2];
            n4 = n4 * 31 + (t3 != null ? t3.hashCode() : 0);
        }
        return n4;
    }

    private static final <T> boolean subarrayContentEquals(T[] TArray, int n2, int n3, List<?> list2) {
        if (n3 != list2.size()) {
            return false;
        }
        for (int i2 = 0; i2 < n3; ++i2) {
            if (Intrinsics.areEqual(TArray[n2 + i2], list2.get(i2))) continue;
            return false;
        }
        return true;
    }

    @NotNull
    public static final <T> T[] copyOfUninitializedElements(@NotNull T[] TArray, int n2) {
        Intrinsics.checkNotNullParameter(TArray, "");
        T[] TArray2 = Arrays.copyOf(TArray, n2);
        Intrinsics.checkNotNullExpressionValue(TArray2, "");
        return TArray2;
    }

    public static final <E> void resetAt(@NotNull E[] EArray, int n2) {
        Intrinsics.checkNotNullParameter(EArray, "");
        EArray[n2] = null;
    }

    public static final <E> void resetRange(@NotNull E[] EArray, int n2, int n3) {
        Intrinsics.checkNotNullParameter(EArray, "");
        while (n2 < n3) {
            ListBuilderKt.resetAt(EArray, n2);
            ++n2;
        }
    }

    public static final /* synthetic */ int access$subarrayContentHashCode(Object[] objectArray, int n2, int n3) {
        return ListBuilderKt.subarrayContentHashCode(objectArray, n2, n3);
    }

    public static final /* synthetic */ String access$subarrayContentToString(Object[] objectArray, int n2, int n3) {
        return ListBuilderKt.subarrayContentToString(objectArray, n2, n3);
    }

    public static final /* synthetic */ boolean access$subarrayContentEquals(Object[] objectArray, int n2, int n3, List list2) {
        return ListBuilderKt.subarrayContentEquals(objectArray, n2, n3, list2);
    }
}

